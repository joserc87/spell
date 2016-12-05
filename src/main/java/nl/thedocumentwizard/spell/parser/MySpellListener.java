
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
        step.setName(getString(ctx.STRING(0)));
        if (ctx.STRING().size() > 1) {
            step.setGroupName(getString(ctx.STRING(1)));
        }
        for (SpellParser.QuestionContext questionContext : ctx.question()) {
            if (step.getQuestions() == null) {
                step.setQuestions(new ArrayOfWizardQuestion());
            }
            step.getQuestions().getQuestion().add(getQuestion(questionContext));
        }
        return step;
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
        String name = getString(ctx.STRING());
        if (name != null) {
            question.setName(name);
        }
        // Check the control type
        if (ctx.control_type() == null ||
                ctx.control_type().STRING_TYPE() != null) { // by default, string
            question.setString(getStringControl(ctx));
        } else if (ctx.control_type().LABEL_TYPE() != null) {
            question.setLabel(getLabelControl(ctx));
        } else if (ctx.control_type().EMAIL_TYPE() != null) {
            question.setEmail(getEmailControl(ctx));
        } else if (ctx.control_type().TEXT_TYPE() != null) {
            question.setText(getTextControl(ctx));
        } else if (ctx.control_type().DATE_TYPE() != null) {
            question.setDate(getDateControl(ctx));
        } else if (ctx.control_type().NUMBER_TYPE() != null) {
            question.setNumber(getNumberControl(ctx));
        } else if (ctx.control_type().CHECKBOX_TYPE() != null) {
            question.setCheckbox(getCheckboxControl(ctx));
        } else if (ctx.control_type().LIST_TYPE() != null) {
            question.setList(getListControl(ctx));
        } else if (ctx.control_type().RADIO_TYPE() != null) {
            question.setRadio(getRadioControl(ctx));
        } else if (ctx.control_type().ATTACHMENT_TYPE() != null) {
            question.setAttachment(getAttachmentControl(ctx));
        } else if (ctx.control_type().IMAGE_TYPE() != null) {
            question.setImage(getImageControl(ctx));
        } else if (ctx.control_type().MULTI_TYPE() != null) {
            question.setMulti(getMultiControl(ctx));
        }
        return question;
    }

    /**
     * Converts the QuestionContext context into a String control
     *
     * @param ctx The context that defines the control
     * @return A StringControl
     */
    protected StringControl getStringControl(SpellParser.QuestionContext ctx) {
        StringControl control = objectFactory.createStringControl();
        return control;
    }

    protected LabelControl getLabelControl(SpellParser.QuestionContext ctx) {
        LabelControl control = objectFactory.createLabelControl();
        return control;
    }

    protected EmailControl getEmailControl(SpellParser.QuestionContext ctx) {
        EmailControl control = objectFactory.createEmailControl();
        return control;
    }

    protected TextControl getTextControl(SpellParser.QuestionContext ctx) {
        TextControl control = objectFactory.createTextControl();
        return control;
    }

    protected DateControl getDateControl(SpellParser.QuestionContext ctx) {
        DateControl control = objectFactory.createDateControl();
        return control;
    }

    protected NumberControl getNumberControl(SpellParser.QuestionContext ctx) {
        NumberControl control = objectFactory.createNumberControl();
        return control;
    }

    protected CheckboxControl getCheckboxControl(SpellParser.QuestionContext ctx) {
        CheckboxControl control = objectFactory.createCheckboxControl();
        return control;
    }

    protected ListControl getListControl(SpellParser.QuestionContext ctx) {
        ListControl control = objectFactory.createListControl();
        return control;
    }

    protected RadioControl getRadioControl(SpellParser.QuestionContext ctx) {
        RadioControl control = objectFactory.createRadioControl();
        return control;
    }

    protected AttachmentFileControl getAttachmentControl(SpellParser.QuestionContext ctx) {
        AttachmentFileControl control = objectFactory.createAttachmentFileControl();
        return control;
    }

    protected ImageFileControl getImageControl(SpellParser.QuestionContext ctx) {
        ImageFileControl control = objectFactory.createImageFileControl();
        return control;
    }

    protected MultiControl getMultiControl(SpellParser.QuestionContext ctx) {
        MultiControl control = objectFactory.createMultiControl();
        return control;
    }
}
