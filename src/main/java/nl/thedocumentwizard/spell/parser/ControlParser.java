package nl.thedocumentwizard.spell.parser;

import nl.thedocumentwizard.wizardconfiguration.jaxb.*;

import java.lang.reflect.InvocationTargetException;

/**
 * Parses ANTLR controls into WizardControls
 */
public class ControlParser {
    private ObjectFactory objectFactory;
    private ParsingHelper helper;

    public ControlParser(ObjectFactory factory, ParsingHelper helper) {
        this.objectFactory = factory;
        this.helper = helper;
    }

    /**
     * Sets the common attributes for all controls using reflection:
     * 
     * @param ctx The context object. It contains the default value / default value metadata, the metadata, etc.
     * @param control The abstract control to set
     */
    public void setAbstractControl(Object ctx, AbstractControl control) {
        // Default value (control "question" = defautValue)
        try {
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
        } catch (NoSuchMethodException e) {
            // It's ok if the method does not exist
        } catch (InvocationTargetException e) {
            // It's ok if the method does not exist
        } catch (IllegalAccessException e) {
            // It's ok if the method does not exist
        }
    }

    private ExplicitWizardMetadata stringOrMetadataToExplicitWizardMetadata(SpellParser.String_or_metadataContext ctx) {
        ExplicitWizardMetadata m = new ExplicitWizardMetadata();
        if (ctx.METADATA() != null) {
            m.setMetadataName(helper.getMetadataName(ctx.METADATA()));
        } else if (ctx.STRING() != null) {
            m.setValue(helper.getString(ctx.STRING()));
        }
        return m;
    }

    public void setListControl(SpellParser.List_controlContext ctx, ListControl control) {
        if (ctx.list_item() != null) {
            ArrayOfListItem items = control.getItems();
            if (items == null && ctx.list_item().size() > 0) {
                items = new ArrayOfListItem();
                control.setItems(items);
            }
            for (SpellParser.List_itemContext li : ctx.list_item()) {
                ListItem item = new ListItem();
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

    public AbstractControl getControl(SpellParser.QuestionContext ctx) {
        AbstractControl control = null;
        Object controlContext = null;
        // Check the control type
        if (ctx.string_control() != null) { // by default, string
            control = objectFactory.createStringControl();
            controlContext = ctx.string_control();
        } else if (ctx.basic_control() != null) { // Basic controls
            if (ctx.basic_control().basic_control_type().STRING_TYPE() != null) {
                control = objectFactory.createStringControl();
            } else if (ctx.basic_control().basic_control_type().LABEL_TYPE() != null) {
                control = objectFactory.createLabelControl();
            } else if (ctx.basic_control().basic_control_type().EMAIL_TYPE() != null) {
                control = objectFactory.createEmailControl();
            } else if (ctx.basic_control().basic_control_type().TEXT_TYPE() != null) {
                control = objectFactory.createTextControl();
            } else if (ctx.basic_control().basic_control_type().DATE_TYPE() != null) {
                control = objectFactory.createDateControl();
            } else if (ctx.basic_control().basic_control_type().NUMBER_TYPE() != null) {
                control = objectFactory.createNumberControl();
            } else if (ctx.basic_control().basic_control_type().CHECKBOX_TYPE() != null) {
                control = objectFactory.createCheckboxControl();
            }
            controlContext = ctx.basic_control();
        } else if (ctx.list_control() != null) { // List controls
            ListControl listControl;
            control = listControl = objectFactory.createListControl();
            controlContext = ctx.list_control();
            this.setListControl(ctx.list_control(), listControl);
        } else if (ctx.container_control() != null) { // Container controls
            if (ctx.container_control().container_control_type().RADIO_TYPE() != null) {
                control = objectFactory.createRadioControl();
            } else if (ctx.container_control().container_control_type().MULTI_TYPE() != null) {
                control = objectFactory.createMultiControl();
            }
            controlContext = ctx.container_control();
        } else if (ctx.upload_control() != null) {
            if (ctx.upload_control().upload_control_type().ATTACHMENT_TYPE() != null) {
                control = objectFactory.createAttachmentFileControl();
            } else if (ctx.upload_control().upload_control_type().IMAGE_TYPE() != null) {
                control = objectFactory.createImageFileControl();
            }
            controlContext = ctx.upload_control();
        }
        if (controlContext != null) {
            this.setAbstractControl(controlContext, control);
        }
        return control;
    }
}
