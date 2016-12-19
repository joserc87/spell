
package nl.thedocumentwizard.spell.parser;

import nl.thedocumentwizard.spell.parser.SpellBaseListener;
import nl.thedocumentwizard.spell.parser.SpellParser;
import nl.thedocumentwizard.wizardconfiguration.jaxb.*;
import org.antlr.v4.runtime.tree.TerminalNode;

public class MySpellListener extends SpellBaseListener {

    private ObjectFactory objectFactory;
    private Wizard wizard;
    public MySpellListener(ObjectFactory factory) {
        this.objectFactory = factory;
    }

    public Wizard getWizard() {
        return wizard;
    }

    @Override
    public void enterWizard(SpellParser.WizardContext ctx) {
        this.wizard = objectFactory.createWizard();
        this.wizard.setSteps(objectFactory.createArrayOfSteptype());
        for (SpellParser.StepContext stepContext : ctx.step()) {
            this.wizard.getSteps().getStep().add(this.getStep(stepContext));
        }
        System.out.println("Wizard parsed");
    }

    // Helper methods

    private String getString(TerminalNode node) {
        String s = null;
        if (node != null && node.getText() != null) {
            s = node.getText();
            if (s.length() < 2) {
                System.err.println("String " + s + " is not of correct size");
            } else {
                s = s.substring(1, s.length() - 1);
            }
        }
        return s;
    }

    /**
     * Converts a StepContext into a WizardStep
     * 
     * @param ctx The context that defines the step
     * @return A Steptype
     */
    protected Steptype getStep(SpellParser.StepContext ctx) {
        Steptype step = objectFactory.createSteptype();
        // Step name and groupName
        step.setName(getString(ctx.STRING(0)));
        if (ctx.STRING().size() > 1) {
            step.setGroupName(getString(ctx.STRING(1)));
        }
        // Questions:
        for (SpellParser.QuestionContext questionContext : ctx.question()) {
            if (step.getQuestions() == null) {
                step.setQuestions(new ArrayOfWizardQuestion());
            }
            step.getQuestions().getQuestion().add(getQuestion(questionContext));
        }
        // Conditions and Advanced rules
        for (SpellParser.WhenContext whenContext : ctx.when()) {
            parseWhen(whenContext, step, null);
        }
        return step;
    }

    /**
     * Parses a "when" block, recursively ("when" blocks can contain other blocks)
     *
     * @param ctx The context to parse, which contains the Trigger and the conditions/advancedrules
     * @param step The step where to add the conditions and advanced rules
     * @param parentTrigger The trigger of the parent "when". The new trigger will be AND {parentTrigger, newTrigger}
     */
    protected void parseWhen(SpellParser.WhenContext ctx, Steptype step, Trigger parentTrigger) {

    }

    /**
     * Converts a QuestionContext into a WizardQuestion
     *
     * @param ctx The context that defines the question
     * @return A Question
     */
    protected ArrayOfWizardQuestion.Question getQuestion(SpellParser.QuestionContext ctx) {
        ArrayOfWizardQuestion.Question question = objectFactory.createArrayOfWizardQuestionQuestion();
        // Question required?:
        if (ctx.REQUIRED() != null) {
            question.setRequired(true);
        }
        // Question name:
        String name = null;
        if (ctx.string_control() != null) {
            name = getString(ctx.string_control().STRING());
        } else if (ctx.basic_control() != null) {
            name = getString(ctx.basic_control().STRING());
        } else if (ctx.upload_control() != null) {
            name = getString(ctx.upload_control().STRING());
        } else if (ctx.container_control() != null) {
            name = getString(ctx.container_control().STRING());
        }
        if (name != null) {
            question.setName(name);
        }
        // Check the control type
        if (ctx.string_control() != null) { // by default, string
            question.setString(getStringControl(ctx));
        } else if (ctx.basic_control() != null) { // Basic controls
            if (ctx.basic_control().basic_control_type().LABEL_TYPE() != null) {
                question.setLabel(getLabelControl(ctx));
            } else if (ctx.basic_control().basic_control_type().EMAIL_TYPE() != null) {
                question.setEmail(getEmailControl(ctx));
            } else if (ctx.basic_control().basic_control_type().TEXT_TYPE() != null) {
                question.setText(getTextControl(ctx));
            } else if (ctx.basic_control().basic_control_type().DATE_TYPE() != null) {
                question.setDate(getDateControl(ctx));
            } else if (ctx.basic_control().basic_control_type().NUMBER_TYPE() != null) {
                question.setNumber(getNumberControl(ctx));
            } else if (ctx.basic_control().basic_control_type().CHECKBOX_TYPE() != null) {
                question.setCheckbox(getCheckboxControl(ctx));
            }
        } else if (ctx.list_control() != null) { // List controls
            question.setList(getListControl(ctx));
        } else if (ctx.container_control() != null) { // Container controls
            if (ctx.container_control().container_control_type().RADIO_TYPE() != null) {
                question.setRadio(getRadioControl(ctx));
            } else if (ctx.container_control().container_control_type().MULTI_TYPE() != null) {
                question.setMulti(getMultiControl(ctx));
            }
        } else if (ctx.upload_control() != null) {
            if (ctx.upload_control().upload_control_type().ATTACHMENT_TYPE() != null) {
                question.setAttachment(getAttachmentControl(ctx));
            } else if (ctx.upload_control().upload_control_type().IMAGE_TYPE() != null) {
                question.setImage(getImageControl(ctx));
            }
        }
        return question;
    }

    private String getMetadataIDForName(String metadataName) {
        return "????????-????-????-????-????????????";
    }

    private String getMetadataName(TerminalNode node) {
        if (node != null) {
            String $metadataName = node.getText();
            assert ($metadataName != null && $metadataName.length() > 1 && $metadataName.charAt(0) == '$');
            return $metadataName.substring(1);
        } else {
            return null;
        }
    }

    ///////////////
    // CONTROLS: //
    ///////////////

    protected void setAbstractControl(SpellParser.String_controlContext ctx, AbstractControl control) {
        // Default value (control "question" = defautValue)
        if (ctx.default_value() != null) {
            String value = null;
            String metadataName = null;
            if (ctx.default_value().literal() != null) {
                String stringValue = getString(ctx.default_value().literal().STRING());
                value = stringValue;
            } else {
                metadataName = getMetadataName(ctx.default_value().METADATA());
            }
            if (value != null) {
                control.setDefaultValue(value);
            } else if (metadataName != null) {
                control.setDefaultValueMetadataName(metadataName);
                control.setDefaultValueMetadataID(getMetadataIDForName(metadataName));
            }
        }
        // Metadata:
        if (ctx.ctrl_metadata() != null) {
            String metadataName = null;
            if (ctx.ctrl_metadata().METADATA() != null) {
                metadataName = getMetadataName(ctx.ctrl_metadata().METADATA());
            }
            if (metadataName != null) {
                control.setMetadataName(metadataName);
                control.setMetadataID(getMetadataIDForName(metadataName));
            }
        }
    }
    protected void setAbstractControl(SpellParser.Basic_controlContext ctx, AbstractControl control) {
        // Default value (control "question" = defautValue)
        if (ctx.default_value() != null) {
            String value = null;
            String metadataName = null;
            if (ctx.default_value().literal() != null) {
                String stringValue = getString(ctx.default_value().literal().STRING());
                value = stringValue;
            } else {
                metadataName = getMetadataName(ctx.default_value().METADATA());
            }
            if (value != null) {
                control.setDefaultValue(value);
            } else if (metadataName != null) {
                // TODO: Search the metadata ID for that name
                control.setDefaultValueMetadataName(metadataName);
                control.setDefaultValueMetadataID(getMetadataIDForName(metadataName));
            }
        }
        // Metadata:
        if (ctx.ctrl_metadata() != null) {
            String metadataName = null;
            if (ctx.ctrl_metadata().METADATA() != null) {
                metadataName = getMetadataName(ctx.ctrl_metadata().METADATA());
            }
            if (metadataName != null) {
                control.setMetadataName(metadataName);
                control.setMetadataID(getMetadataIDForName(metadataName));
            }
        }
    }
    protected void setAbstractControl(SpellParser.List_controlContext ctx, AbstractControl control) {
        // Default value (control "question" = defautValue)
        if (ctx.default_value() != null) {
            String value = null;
            String metadataName = null;
            if (ctx.default_value().literal() != null) {
                String stringValue = getString(ctx.default_value().literal().STRING());
                value = stringValue;
            } else {
                metadataName = getMetadataName(ctx.default_value().METADATA());
            }
            if (value != null) {
                control.setDefaultValue(value);
            } else if (metadataName != null) {
                // TODO: Search the metadata ID for that name
                control.setDefaultValueMetadataName(metadataName);
                control.setDefaultValueMetadataID(getMetadataIDForName(metadataName));
            }
        }
        // Metadata:
        if (ctx.ctrl_metadata() != null) {
            String metadataName = null;
            if (ctx.ctrl_metadata().METADATA() != null) {
                metadataName = getMetadataName(ctx.ctrl_metadata().METADATA());
            }
            if (metadataName != null) {
                control.setMetadataName(metadataName);
                control.setMetadataID(getMetadataIDForName(metadataName));
            }
        }
    }
    protected void setAbstractControl(SpellParser.Upload_controlContext ctx, AbstractControl control) {
        // Default value (control "question" = defautValue)
        if (ctx.default_value() != null) {
            String value = null;
            String metadataName = null;
            if (ctx.default_value().literal() != null) {
                String stringValue = getString(ctx.default_value().literal().STRING());
                value = stringValue;
            } else {
                metadataName = getMetadataName(ctx.default_value().METADATA());
            }
            if (value != null) {
                control.setDefaultValue(value);
            } else if (metadataName != null) {
                // TODO: Search the metadata ID for that name
                control.setDefaultValueMetadataName(metadataName);
                control.setDefaultValueMetadataID(getMetadataIDForName(metadataName));
            }
        }
        // Metadata:
        if (ctx.ctrl_metadata() != null) {
            String metadataName = null;
            if (ctx.ctrl_metadata().METADATA() != null) {
                metadataName = getMetadataName(ctx.ctrl_metadata().METADATA());
            }
            if (metadataName != null) {
                control.setMetadataName(metadataName);
                control.setMetadataID(getMetadataIDForName(metadataName));
            }
        }
    }
    protected void setAbstractControl(SpellParser.Container_controlContext ctx, AbstractControl control) {
        // Default value (control "question" = defautValue)
        if (ctx.default_value() != null) {
            String value = null;
            String metadataName = null;
            if (ctx.default_value().literal() != null) {
                String stringValue = getString(ctx.default_value().literal().STRING());
                value = stringValue;
            } else {
                metadataName = getMetadataName(ctx.default_value().METADATA());
            }
            if (value != null) {
                control.setDefaultValue(value);
            } else if (metadataName != null) {
                // TODO: Search the metadata ID for that name
                control.setDefaultValueMetadataName(metadataName);
                control.setDefaultValueMetadataID(getMetadataIDForName(metadataName));
            }
        }
        // Metadata:
        if (ctx.ctrl_metadata() != null) {
            String metadataName = null;
            if (ctx.ctrl_metadata().METADATA() != null) {
                metadataName = getMetadataName(ctx.ctrl_metadata().METADATA());
            }
            if (metadataName != null) {
                control.setMetadataName(metadataName);
                control.setMetadataID(getMetadataIDForName(metadataName));
            }
        }
    }
    /**
     * Converts the QuestionContext context into a String control
     *
     * @param ctx The context that defines the control
     * @return A StringControl
     */
    protected StringControl getStringControl(SpellParser.QuestionContext ctx) {
        StringControl control = objectFactory.createStringControl();
        if (ctx.string_control() != null) {
            setAbstractControl(ctx.string_control(), control);
        } else {
            setAbstractControl(ctx.basic_control(), control);

        }
        return control;
    }

    protected LabelControl getLabelControl(SpellParser.QuestionContext ctx) {
        LabelControl control = objectFactory.createLabelControl();
        setAbstractControl(ctx.basic_control(), control);
        return control;
    }

    protected EmailControl getEmailControl(SpellParser.QuestionContext ctx) {
        EmailControl control = objectFactory.createEmailControl();
        setAbstractControl(ctx.basic_control(), control);
        return control;
    }

    protected TextControl getTextControl(SpellParser.QuestionContext ctx) {
        TextControl control = objectFactory.createTextControl();
        setAbstractControl(ctx.basic_control(), control);
        return control;
    }

    protected DateControl getDateControl(SpellParser.QuestionContext ctx) {
        DateControl control = objectFactory.createDateControl();
        setAbstractControl(ctx.basic_control(), control);
        return control;
    }

    protected NumberControl getNumberControl(SpellParser.QuestionContext ctx) {
        NumberControl control = objectFactory.createNumberControl();
        setAbstractControl(ctx.basic_control(), control);
        return control;
    }

    protected CheckboxControl getCheckboxControl(SpellParser.QuestionContext ctx) {
        CheckboxControl control = objectFactory.createCheckboxControl();
        setAbstractControl(ctx.basic_control(), control);
        return control;
    }

    protected ListControl getListControl(SpellParser.QuestionContext ctx) {
        ListControl control = objectFactory.createListControl();
        setAbstractControl(ctx.list_control(), control);
        return control;
    }

    protected RadioControl getRadioControl(SpellParser.QuestionContext ctx) {
        RadioControl control = objectFactory.createRadioControl();
        setAbstractControl(ctx.container_control(), control);
        return control;
    }

    protected AttachmentFileControl getAttachmentControl(SpellParser.QuestionContext ctx) {
        AttachmentFileControl control = objectFactory.createAttachmentFileControl();
        setAbstractControl(ctx.upload_control(), control);
        return control;
    }

    protected ImageFileControl getImageControl(SpellParser.QuestionContext ctx) {
        ImageFileControl control = objectFactory.createImageFileControl();
        setAbstractControl(ctx.upload_control(), control);
        return control;
    }

    protected MultiControl getMultiControl(SpellParser.QuestionContext ctx) {
        MultiControl control = objectFactory.createMultiControl();
        setAbstractControl(ctx.container_control(), control);
        return control;
    }
}
