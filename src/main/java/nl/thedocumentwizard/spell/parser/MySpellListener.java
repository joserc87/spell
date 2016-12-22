
package nl.thedocumentwizard.spell.parser;

import nl.thedocumentwizard.spell.parser.SpellBaseListener;
import nl.thedocumentwizard.spell.parser.SpellParser;
import nl.thedocumentwizard.wizardconfiguration.jaxb.*;
import org.antlr.v4.runtime.tree.TerminalNode;

public class MySpellListener extends SpellBaseListener {

    private ObjectFactory objectFactory;
    private ControlParser controlParser;
    private ParsingHelper helper;
    private Wizard wizard;
    public MySpellListener(ObjectFactory factory, ControlParser controlParser, ParsingHelper helper) {
        this.objectFactory = factory;
        this.controlParser = controlParser;
        this.helper = helper;
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
        step.setName(helper.getString(ctx.STRING(0)));
        if (ctx.STRING().size() > 1) {
            step.setGroupName(helper.getString(ctx.STRING(1)));
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
        if (ctx.named_string_control() != null) {
            name = helper.getString(ctx.named_string_control().STRING());
        } else if (ctx.named_basic_control() != null) {
            name = helper.getString(ctx.named_basic_control().STRING());
        } else if (ctx.named_upload_control() != null) {
            name = helper.getString(ctx.named_upload_control().STRING());
        } else if (ctx.named_container_control() != null) {
            name = helper.getString(ctx.named_container_control().STRING());
        }
        if (name != null) {
            question.setName(name);
        }
        // Parse the control
        AbstractControl control = controlParser.getControl(ctx);
        if (control instanceof StringControl) {
            question.setString((StringControl) control);
        } else if (control instanceof LabelControl) {
            question.setLabel((LabelControl) control);
        } else if (control instanceof EmailControl) {
            question.setEmail((EmailControl) control);
        } else if (control instanceof TextControl) {
            question.setText((TextControl) control);
        } else if (control instanceof DateControl) {
            question.setDate((DateControl) control);
        } else if (control instanceof NumberControl) {
            question.setNumber((NumberControl) control);
        } else if (control instanceof CheckboxControl) {
            question.setCheckbox((CheckboxControl) control);
        } else if (control instanceof ListControl) {
            question.setList((ListControl) control);
        } else if (control instanceof RadioControl) {
            question.setRadio((RadioControl) control);
        } else if (control instanceof MultiControl) {
            question.setMulti((MultiControl) control);
        } else if (control instanceof AttachmentFileControl) {
            question.setAttachment((AttachmentFileControl) control);
        } else if (control instanceof ImageFileControl) {
            question.setImage((ImageFileControl) control);
        }
        return question;
    }
}
