package nl.thedocumentwizard.spell.parser;

import nl.thedocumentwizard.wizardconfiguration.*;
import nl.thedocumentwizard.wizardconfiguration.jaxb.*;
import nl.thedocumentwizard.wizardconfiguration.jaxb.RadioControl;
import org.antlr.v4.runtime.ParserRuleContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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

    private void printError(ParserRuleContext ctx, String message) {
        System.err.println("line " + ctx.getStart().getLine() +
                ":" + ctx.getStart().getCharPositionInLine() +
                " " + message);
    }

    /**
     * Retrieves the setter method name for an attribute.
     *
     * @param attributeName The attribute, e.g. maxLength
     * @return The name of the setter method. E.g. setMaxLength
     */
    private String getSetterMethodName(String attributeName) {
        return "set" + attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1);
    }

    /**
     * Converts a string value into it's enum representation, depending on the
     * attribute.
     *
     * The enums handled by this method are.
     *
     * - decimalSeparator in NumberControl
     *
     * @param attribute The name of the attribute.
     * @param value The string with the enum value (possibly)
     * @return True if the exception was handled, or false otherwise
     */
    private Object convertEnum(String attribute, Object val) {
        if (attribute.equals("decimalSeparator")) {
            if (val.equals(",")) {
                return Separator.COMMA;
            } else if (val.equals(".")) {
                return Separator.DOT;
            }
        }
        return null;
    }

    /**
     * Executes a setter (with one argument), where the argument can be any type, String, Integer or Boolean.
     *
     * @param methodName The name of the setter
     * @param obj The object to call the setter on
     * @param value The new value for the object. If this is a number, both
     * Float and Integer setters will be tried.
     * @return True if the method was found, or false otherwise
     */
    private boolean invokeSetter(String methodName, Object obj, Object value) throws IllegalAccessException, InvocationTargetException {
        // Try the guessed type
        try {
            Method setAttribute = obj.getClass().getMethod(methodName, value.getClass());
            setAttribute.invoke(obj, value);
            return true;
        } catch (NoSuchMethodException e) {
        }
        // For numbers, check int, float and double:
        if (value instanceof Number) {
            Number num = (Number)value;
            // Integer
            try {
                obj.getClass().getMethod(methodName, Integer.class).invoke(obj, num.intValue());
                return true;
            } catch (NoSuchMethodException e) { }
            // Float
            try {
                obj.getClass().getMethod(methodName, Float.class).invoke(obj, num.floatValue());
                return true;
            } catch (NoSuchMethodException e) { }
            // Double
            try {
                obj.getClass().getMethod(methodName, Double.class).invoke(obj, num.doubleValue());
                return true;
            } catch (NoSuchMethodException e) { }
            // Short
            try {
                obj.getClass().getMethod(methodName, Short.class).invoke(obj, num.shortValue());
                return true;
            } catch (NoSuchMethodException e) { }
            // Long
            try {
                obj.getClass().getMethod(methodName, Long.class).invoke(obj, num.longValue());
                return true;
            } catch (NoSuchMethodException e) { }
            // Byte
            try {
                obj.getClass().getMethod(methodName, Byte.class).invoke(obj, num.byteValue());
                return true;
            } catch (NoSuchMethodException e) { }

        }
        return false;
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
                    if (defaultValue.literal().STRING() != null) {
                        // The default value is a string
                        value = helper.getString(defaultValue.literal().STRING());
                    } else if (defaultValue.literal().NUM() != null) {
                        // The default value is a number
                        value = defaultValue.literal().NUM().getText();
                    } else if (defaultValue.literal().bool() != null) {
                        // The default value is a boolean (true/false or selected/unselected)
                        if (defaultValue.literal().bool().TRUE() != null) {
                            value = "True";
                        } else {
                            value = "False";
                        }
                    }
                } else if (defaultValue.METADATA() != null) {
                    metadataName = helper.getMetadataName(defaultValue.METADATA());
                } else if (defaultValue.NAME() != null) {
                    // Default value is a name. e.g:
                    // radio "" = item1
                    //   label "no" item1
                    //   label "yes" item2
                    if (control instanceof nl.thedocumentwizard.wizardconfiguration.RadioControl) {
                        nl.thedocumentwizard.wizardconfiguration.RadioControl radio = (nl.thedocumentwizard.wizardconfiguration.RadioControl) control;
                        radio.setAliasDefaultValue(defaultValue.NAME().getText());
                    } else {
                        // If it's not a radio, it doesn't make sense to have a name as the default value
                        printError(defaultValue, "unexpected default value '" +
                                defaultValue.NAME().getText() +
                                "'. Only radio controls can have an alias as the default value.");
                    }
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
                control.setId(aliasName);
            }
            // Custom attributes
            SpellParser.Control_attribute_listContext attributes = (SpellParser.Control_attribute_listContext) ctx.getClass().getMethod("control_attribute_list").invoke(ctx);
            if (attributes != null && attributes.control_attribute() != null) {
                for (SpellParser.Control_attributeContext attribute : attributes.control_attribute()) {
                    if (attribute.NAME() == null) {
                        printError(attribute, "error setting attribute. '" + attribute.getText() + "' is not valid.");
                    } else {
                        String attributeKey = attribute.NAME().getText();
                        Object attributeValue = null;
                        String methodName = getSetterMethodName(attributeKey);
                        if (attribute.literal().STRING() != null) {
                            // The value is a string
                            attributeValue = helper.getString(attribute.literal().STRING());
                            // Check if the attribute is an enum:
                            Object enumValue = convertEnum(attributeKey, attributeValue);
                            if (enumValue != null) {
                                attributeValue = enumValue;
                            }
                        } else if (attribute.literal().NUM() != null) {
                            // The value is a number
                            attributeValue = Float.parseFloat(attribute.literal().NUM().getText());
                        } else if (attribute.literal().bool() != null) {
                            // The value is a boolean (true/false or selected/unselected)
                            attributeValue = attribute.literal().bool().TRUE() != null;
                        }
                        if (!invokeSetter(methodName, control, attributeValue)) {
                            printError(attribute, "error setting attribute." +
                                    " No attribute " + attributeKey +
                                    " with type " + attributeValue.getClass() +
                                    " found for control " + control.getClass());
                        }
                    }
                }
            }
        } catch (NoSuchMethodException e) {
            // It's ok if the method does not exist
        } catch (InvocationTargetException e) {
            System.err.println(e);
        } catch (IllegalAccessException e) {
            System.err.println(e);
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
