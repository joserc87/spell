package nl.thedocumentwizard.spell.parser;

import nl.thedocumentwizard.wizardconfiguration.jaxb.*;

import java.lang.reflect.InvocationTargetException;

/**
 * Parses ANTLR controls into WizardControls
 */
public class ControlParser {
    private ObjectFactory objectFactory;
    private ParsingHelper helper;
    private ControlAliasHelper aliasHelper;

    public ControlParser(ObjectFactory factory, ParsingHelper helper) {
        this.objectFactory = factory;
        this.helper = helper;
    }

    public ControlAliasHelper getAliasHelper() {
        return aliasHelper;
    }

    public void setAliasHelper(ControlAliasHelper aliasHelper) {
        this.aliasHelper = aliasHelper;
    }

    /**
     * Sets the common attributes for all controls using reflection:
     * 
     * @param ctx The context object. It contains the default value / default value metadata, the metadata, etc.
     * @param control The abstract control to set
     */
    public void setAbstractControl(Object ctx, AbstractControl control) {
        try {
            // Default value (control "question" = defaultValue)
            SpellParser.Default_valueContext defaultValue = (SpellParser.Default_valueContext) ctx.getClass().getMethod("default_value").invoke(ctx);
            if (defaultValue != null) {
                String value = null;
                String metadataName = null;
                if (defaultValue.literal() != null) {
                    String stringValue = helper.getString(defaultValue.literal().STRING());
                    value = stringValue;
                } else {
                    metadataName = helper.getMetadataName(defaultValue.METADATA());
                }
                if (value != null) {
                    control.setDefaultValue(value);
                } else if (metadataName != null) {
                    control.setDefaultValueMetadataName(metadataName);
                }
            }
            // Metadata:
            SpellParser.Ctrl_metadataContext ctrlMetadata = (SpellParser.Ctrl_metadataContext) ctx.getClass().getMethod("ctrl_metadata").invoke(ctx);
            if (ctrlMetadata != null) {
                String metadataName = null;
                if (ctrlMetadata.METADATA() != null) {
                    metadataName = helper.getMetadataName(ctrlMetadata.METADATA());
                }
                if (metadataName != null) {
                    control.setMetadataName(metadataName);
                }
            }
            // Alias:
            SpellParser.AliasContext alias = (SpellParser.AliasContext) ctx.getClass().getMethod("alias").invoke(ctx);
            if (alias != null) {
                String aliasName = alias.NAME().getText();
                aliasHelper.registerControl(aliasName, control);
            }
        } catch (NoSuchMethodException e) {
            // It's ok if the method does not exist
        } catch (InvocationTargetException e) {
            // It's ok if the method does not exist
        } catch (IllegalAccessException e) {
            // It's ok if the method does not exist
        }
    }

    private ExplicitWizardMetadata stringOrMetadataToExplicitWizardMetadata(SpellParser.String_or_metadataContext ctx) {
        ExplicitWizardMetadata m = objectFactory.createExplicitWizardMetadata();
        if (ctx.METADATA() != null) {
            m.setMetadataName(helper.getMetadataName(ctx.METADATA()));
        } else if (ctx.STRING() != null) {
            m.setValue(helper.getString(ctx.STRING()));
        }
        return m;
    }

    public void setListControl(SpellParser.Named_list_controlContext ctx, ListControl control) {
        if (ctx.list_item() != null) {
            ArrayOfListItem items = control.getItems();
            if (items == null && ctx.list_item().size() > 0) {
                items = objectFactory.createArrayOfListItem();
                control.setItems(items);
            }
            for (SpellParser.List_itemContext li : ctx.list_item()) {
                ListItem item = objectFactory.createListItem();
                if (li.string_or_metadata(1) != null) { // 2 string_or_metadata : displayText = value
                    item.setDisplayText(stringOrMetadataToExplicitWizardMetadata(li.string_or_metadata(0)));
                    item.setValue(stringOrMetadataToExplicitWizardMetadata(li.string_or_metadata(1)));
                } else { // 1 string_or_metadata : value
                    item.setValue(stringOrMetadataToExplicitWizardMetadata(li.string_or_metadata(0)));
                }
                items.getItem().add(item);
            }
        }
    }

    public void setListControl(SpellParser.Unnamed_list_controlContext ctx, ListControl control) {
        if (ctx.list_item() != null) {
            ArrayOfListItem items = control.getItems();
            if (items == null && ctx.list_item().size() > 0) {
                items = objectFactory.createArrayOfListItem();
                control.setItems(items);
            }
            for (SpellParser.List_itemContext li : ctx.list_item()) {
                ListItem item = objectFactory.createListItem();
                if (li.string_or_metadata(1) != null) { // 2 string_or_metadata : displayText = value
                    item.setDisplayText(stringOrMetadataToExplicitWizardMetadata(li.string_or_metadata(0)));
                    item.setValue(stringOrMetadataToExplicitWizardMetadata(li.string_or_metadata(1)));
                } else { // 1 string_or_metadata : value
                    item.setValue(stringOrMetadataToExplicitWizardMetadata(li.string_or_metadata(0)));
                }
                items.getItem().add(item);
            }
        }
    }

    public void setRadioControl(SpellParser.Named_container_controlContext ctx, RadioControl control) {
        if (ctx.sub_control() != null) {
            ArrayOfChoice1 subControls = control.getItems();
            if (subControls == null && ctx.sub_control().size() > 0) {
                subControls = objectFactory.createArrayOfChoice1();
                control.setItems(subControls);
            }
            for (SpellParser.Sub_controlContext subControlCtx : ctx.sub_control()) {
                AbstractControl subControl = this.getControl(subControlCtx);
                subControls.getTextOrCheckboxOrAttachment().add(subControl);
            }
        }
    }

    public void setRadioControl(SpellParser.Unnamed_container_controlContext ctx, RadioControl control) {
        if (ctx.sub_control() != null) {
            ArrayOfChoice1 subControls = control.getItems();
            if (subControls == null && ctx.sub_control().size() > 0) {
                subControls = objectFactory.createArrayOfChoice1();
                control.setItems(subControls);
            }
            for (SpellParser.Sub_controlContext subControlCtx : ctx.sub_control()) {
                AbstractControl subControl = this.getControl(subControlCtx);
                subControls.getTextOrCheckboxOrAttachment().add(subControl);
            }
        }
    }

    public void setMultiControl(SpellParser.Named_container_controlContext ctx, MultiControl control) {
        if (ctx.sub_control() != null) {
            ArrayOfChoice2 subControls = control.getControls();
            if (subControls == null && ctx.sub_control().size() > 0) {
                subControls = objectFactory.createArrayOfChoice2();
                control.setControls(subControls);
            }
            for (SpellParser.Sub_controlContext subControlCtx : ctx.sub_control()) {
                AbstractControl subControl = this.getControl(subControlCtx);
                subControls.getTextOrNumberOrAttachment().add(subControl);
            }
        }
    }

    public void setMultiControl(SpellParser.Unnamed_container_controlContext ctx, MultiControl control) {
        if (ctx.sub_control() != null) {
            ArrayOfChoice2 subControls = control.getControls();
            if (subControls == null && ctx.sub_control().size() > 0) {
                subControls = objectFactory.createArrayOfChoice2();
                control.setControls(subControls);
            }
            for (SpellParser.Sub_controlContext subControlCtx : ctx.sub_control()) {
                AbstractControl subControl = this.getControl(subControlCtx);
                subControls.getTextOrNumberOrAttachment().add(subControl);
            }
        }
    }

    public AbstractControl getControl(SpellParser.QuestionContext ctx) {
        AbstractControl control = null;
        Object controlContext = null;
        // Check the control type
        if (ctx.named_string_control() != null) { // by default, string
            control = objectFactory.createStringControl();
            controlContext = ctx.named_string_control();
        } else if (ctx.named_basic_control() != null) { // Basic controls
            if (ctx.named_basic_control().basic_control_type().STRING_TYPE() != null) {
                control = objectFactory.createStringControl();
            } else if (ctx.named_basic_control().basic_control_type().LABEL_TYPE() != null) {
                control = objectFactory.createLabelControl();
            } else if (ctx.named_basic_control().basic_control_type().EMAIL_TYPE() != null) {
                control = objectFactory.createEmailControl();
            } else if (ctx.named_basic_control().basic_control_type().TEXT_TYPE() != null) {
                control = objectFactory.createTextControl();
            } else if (ctx.named_basic_control().basic_control_type().DATE_TYPE() != null) {
                control = objectFactory.createDateControl();
            } else if (ctx.named_basic_control().basic_control_type().NUMBER_TYPE() != null) {
                control = objectFactory.createNumberControl();
            } else if (ctx.named_basic_control().basic_control_type().CHECKBOX_TYPE() != null) {
                control = objectFactory.createCheckboxControl();
            }
            controlContext = ctx.named_basic_control();
        } else if (ctx.named_list_control() != null) { // List controls
            ListControl listControl;
            control = listControl = objectFactory.createListControl();
            controlContext = ctx.named_list_control();
            this.setListControl(ctx.named_list_control(), listControl);
        } else if (ctx.named_container_control() != null) { // Container controls
            if (ctx.named_container_control().container_control_type().RADIO_TYPE() != null) {
                RadioControl radioControl;
                control = radioControl = objectFactory.createRadioControl();
                this.setRadioControl(ctx.named_container_control(), radioControl);
            } else if (ctx.named_container_control().container_control_type().MULTI_TYPE() != null) {
                MultiControl multiControl;
                control = multiControl = objectFactory.createMultiControl();
                this.setMultiControl(ctx.named_container_control(), multiControl);
            }
            controlContext = ctx.named_container_control();
        } else if (ctx.named_upload_control() != null) {
            if (ctx.named_upload_control().upload_control_type().ATTACHMENT_TYPE() != null) {
                control = objectFactory.createAttachmentFileControl();
            } else if (ctx.named_upload_control().upload_control_type().IMAGE_TYPE() != null) {
                control = objectFactory.createImageFileControl();
            }
            controlContext = ctx.named_upload_control();
        }
        if (controlContext != null) {
            this.setAbstractControl(controlContext, control);
        }
        return control;
    }

    public AbstractControl getControl(SpellParser.Sub_controlContext ctx) {
        AbstractControl control = null;
        Object controlContext = null;
        // Check the control type
        if (ctx.unnamed_basic_control() != null) { // Basic controls
            if (ctx.unnamed_basic_control().basic_control_type().STRING_TYPE() != null) {
                control = objectFactory.createStringControl();
            } else if (ctx.unnamed_basic_control().basic_control_type().LABEL_TYPE() != null) {
                control = objectFactory.createLabelControl();
            } else if (ctx.unnamed_basic_control().basic_control_type().EMAIL_TYPE() != null) {
                control = objectFactory.createEmailControl();
            } else if (ctx.unnamed_basic_control().basic_control_type().TEXT_TYPE() != null) {
                control = objectFactory.createTextControl();
            } else if (ctx.unnamed_basic_control().basic_control_type().DATE_TYPE() != null) {
                control = objectFactory.createDateControl();
            } else if (ctx.unnamed_basic_control().basic_control_type().NUMBER_TYPE() != null) {
                control = objectFactory.createNumberControl();
            } else if (ctx.unnamed_basic_control().basic_control_type().CHECKBOX_TYPE() != null) {
                control = objectFactory.createCheckboxControl();
            }
            controlContext = ctx.unnamed_basic_control();
        } else if (ctx.unnamed_list_control() != null) { // List controls
            ListControl listControl;
            control = listControl = objectFactory.createListControl();
            controlContext = ctx.unnamed_list_control();
            this.setListControl(ctx.unnamed_list_control(), listControl);
        } else if (ctx.unnamed_container_control() != null) { // Container controls
            if (ctx.unnamed_container_control().container_control_type().RADIO_TYPE() != null) {
                RadioControl radioControl;
                control = radioControl = objectFactory.createRadioControl();
                this.setRadioControl(ctx.unnamed_container_control(), radioControl);
            } else if (ctx.unnamed_container_control().container_control_type().MULTI_TYPE() != null) {
                MultiControl multiControl;
                control = multiControl = objectFactory.createMultiControl();
                this.setMultiControl(ctx.unnamed_container_control(), multiControl);
            }
            controlContext = ctx.unnamed_container_control();
        } else if (ctx.unnamed_upload_control() != null) {
            if (ctx.unnamed_upload_control().upload_control_type().ATTACHMENT_TYPE() != null) {
                control = objectFactory.createAttachmentFileControl();
            } else if (ctx.unnamed_upload_control().upload_control_type().IMAGE_TYPE() != null) {
                control = objectFactory.createImageFileControl();
            }
            controlContext = ctx.unnamed_upload_control();
        }
        if (controlContext != null) {
            this.setAbstractControl(controlContext, control);
        }
        return control;
    }
}
