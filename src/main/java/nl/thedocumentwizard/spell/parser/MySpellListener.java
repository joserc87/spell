
package nl.thedocumentwizard.spell.parser;

import nl.thedocumentwizard.wizardconfiguration.Condition;
import nl.thedocumentwizard.wizardconfiguration.Step;
import nl.thedocumentwizard.wizardconfiguration.jaxb.*;

public class MySpellListener extends SpellBaseListener {

    private ObjectFactory objectFactory;
    private ControlParser controlParser;
    private WhenParser whenParser;
    private ParsingHelper helper;
    private StepAliasHelper aliasHelper;
    private Wizard wizard;
    public MySpellListener(ObjectFactory factory,
                           ControlParser controlParser,
                           WhenParser whenParser,
                           ParsingHelper helper,
                           StepAliasHelper aliasHelper) {
        this.objectFactory = factory;
        this.controlParser = controlParser;
        this.whenParser = whenParser;
        this.helper = helper;
        this.aliasHelper = aliasHelper;
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

    public ArrayOfWizardAdvancedRule.AdvancedRule createAlwaysTrueAdvancedRule() {
        ArrayOfWizardAdvancedRule.AdvancedRule ar = objectFactory.createArrayOfWizardAdvancedRuleAdvancedRule();

        ar.setEqual(objectFactory.createEqualComparisonTrigger());
        ConstTriggerValue val = objectFactory.createConstTriggerValue();
        val.setVal("1");
        // If 1 == 1:
        ar.getEqual().getControlOrConstOrMetadata().add(val);
        ar.getEqual().getControlOrConstOrMetadata().add(val);
        return ar;
    }

    /**
     * Converts a StepContext into a WizardStep
     * 
     * @param ctx The context that defines the step
     * @return A Steptype
     */
    protected Steptype getStep(SpellParser.StepContext ctx) {
        Step step = (Step)objectFactory.createSteptype();
        // Step name and groupName
        step.setName(helper.getString(ctx.STRING(0)));
        if (ctx.STRING().size() > 1) {
            step.setGroupName(helper.getString(ctx.STRING(1)));
        }
        // Alias:
        if (ctx.alias() != null) {
            String alias = ctx.alias().NAME().getText();
            step.setNextStepAlias(alias);
            aliasHelper.registerStep(alias, step);
        } else {
            aliasHelper.registerStep(step);
        }
        controlParser.setAliasHelper(aliasHelper.getAliasHelperForStep(step));
        // Questions:
        for (SpellParser.QuestionContext questionContext : ctx.question()) {
            if (step.getQuestions() == null) {
                step.setQuestions(objectFactory.createArrayOfWizardQuestion());
            }
            step.getQuestions().getQuestion().add(getQuestion(questionContext));
        }
        // Conditions and Advanced rules
        for (SpellParser.WhenContext whenContext : ctx.when()) {
            parseWhen(whenContext, step, null);
        }
        // Next step:
        if (ctx.jump() != null && ctx.jump().size() > 0) {
            String nextStepName = ctx.jump().get(ctx.jump().size() - 1).NAME().getText();
            step.setNextStepAlias(nextStepName);
            // step.setNextStepName(nextStepName);
        }
        // Advanced rules:
        if (ctx.metadata_assignment() != null && ctx.metadata_assignment().size() > 0) {
            // If the step doesn't have advanced rules, create an empty list
            if (step.getAdvancedRules() == null) {
                step.setAdvancedRules(objectFactory.createArrayOfWizardAdvancedRule());
            }

            // Advanced rule trigger: always true
            ArrayOfWizardAdvancedRule.AdvancedRule ar = createAlwaysTrueAdvancedRule();

            // Advanced rule metadatas:
            ar.setMetadatas(objectFactory.createArrayOfImplicitWizardMetadata());
            for (SpellParser.Metadata_assignmentContext maCtx : ctx.metadata_assignment()) {
                ImplicitWizardMetadata iwm = objectFactory.createImplicitWizardMetadata();
                ar.getMetadatas().getMetadata().add(iwm);
                iwm.setName(this.helper.getMetadataName(maCtx.METADATA()));
                iwm.setValue(this.helper.getString(maCtx.STRING()));
            }

            // Add advanced rule to the list
            step.getAdvancedRules().getAdvancedRule().add(ar);

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
        Trigger t;
        Trigger subCondition = whenParser.parseTrigger(ctx.test());
        // Trigger
        if (parentTrigger != null) {
            AndTrigger and = objectFactory.createAndTrigger();
            and.getOrOrLessThanOrRegEx().add(parentTrigger);
            and.getOrOrLessThanOrRegEx().add(subCondition);
            t = and;
        } else {
            t = subCondition;
        }
        // Create Advanced rules and conditions
        if (ctx.when_instruction() != null) {
            ArrayOfWizardAdvancedRule.AdvancedRule ar = null;
            Condition condition = null;
            for (SpellParser.When_instructionContext instruction : ctx.when_instruction()) {
                // Advanced rule:
                if (instruction.metadata_assignment() != null) {
                    // If it' the first metadata assignment, create advanced rule. Otherwise, just add the metadata
                    if (ar == null) {
                        ar = objectFactory.createArrayOfWizardAdvancedRuleAdvancedRule();
                        if (step.getAdvancedRules() == null) {
                            step.setAdvancedRules(objectFactory.createArrayOfWizardAdvancedRule());
                        }
                        step.getAdvancedRules().getAdvancedRule().add(ar);
                        this.setAdvancedRuleTrigger(ar, t);
                        ar.setMetadatas(objectFactory.createArrayOfImplicitWizardMetadata());
                    }
                    // Advanced rule metadatas:
                    ImplicitWizardMetadata iwm = objectFactory.createImplicitWizardMetadata();
                    ar.getMetadatas().getMetadata().add(iwm);
                    iwm.setName(this.helper.getMetadataName(instruction.metadata_assignment().METADATA()));
                    iwm.setValue(this.helper.getString(instruction.metadata_assignment().STRING()));
                } else if (instruction.jump() != null) {
                    // Condition
                    if (condition == null) {
                        condition = (Condition) objectFactory.createArrayOfWizardConditionCondition();
                        if (step.getConditions() == null) {
                            step.setConditions(objectFactory.createArrayOfWizardCondition());
                        }
                        step.getConditions().getCondition().add(condition);
                        this.setConditionTrigger(condition, t);
                    }
                    // Goto
                    String nextStepName = instruction.jump().NAME().getText();
                    condition.setNextStepAlias(nextStepName);
                } else if (instruction.when() != null) {
                    parseWhen(instruction.when(), step, t);
                }
            }
        }
    }

    private void setAdvancedRuleTrigger(ArrayOfWizardAdvancedRule.AdvancedRule ar, Trigger t) {
        if (t instanceof AndTrigger) { ar.setAnd((AndTrigger) t); }
        else if (t instanceof OrTrigger) { ar.setOr((OrTrigger) t); }
        else if (t instanceof NotTrigger) { ar.setNot((NotTrigger) t); }
        else if (t instanceof EmptyTrigger) { ar.setEmpty((EmptyTrigger) t); }
        else if (t instanceof RegexTrigger) { ar.setRegEx((RegexTrigger) t); }
        else if (t instanceof EqualComparisonTrigger) { ar.setEqual((EqualComparisonTrigger) t); }
        else if (t instanceof DifferentComparisonTrigger) { ar.setDifferent((DifferentComparisonTrigger) t); }
        else if (t instanceof GreaterOrEqualThanComparisonTrigger) { ar.setGreaterOrEqualThan((GreaterOrEqualThanComparisonTrigger) t); }
        else if (t instanceof GreaterThanComparisonTrigger) { ar.setGreaterThan((GreaterThanComparisonTrigger) t); }
        else if (t instanceof LessOrEqualThanComparisonTrigger) { ar.setLessOrEqualThan((LessOrEqualThanComparisonTrigger) t); }
        else if (t instanceof LessThanComparisonTrigger) { ar.setLessThan((LessThanComparisonTrigger) t); }
        else { System.err.println("Trigger error " + t); }
    }

    private void setConditionTrigger(ArrayOfWizardCondition.Condition condition, Trigger t) {
        if (t instanceof AndTrigger) { condition.setAnd((AndTrigger) t); }
        else if (t instanceof OrTrigger) { condition.setOr((OrTrigger) t); }
        else if (t instanceof NotTrigger) { condition.setNot((NotTrigger) t); }
        else if (t instanceof EmptyTrigger) { condition.setEmpty((EmptyTrigger) t); }
        else if (t instanceof RegexTrigger) { condition.setRegEx((RegexTrigger) t); }
        else if (t instanceof EqualComparisonTrigger) { condition.setEqual((EqualComparisonTrigger) t); }
        else if (t instanceof DifferentComparisonTrigger) { condition.setDifferent((DifferentComparisonTrigger) t); }
        else if (t instanceof GreaterOrEqualThanComparisonTrigger) { condition.setGreaterOrEqualThan((GreaterOrEqualThanComparisonTrigger) t); }
        else if (t instanceof GreaterThanComparisonTrigger) { condition.setGreaterThan((GreaterThanComparisonTrigger) t); }
        else if (t instanceof LessOrEqualThanComparisonTrigger) { condition.setLessOrEqualThan((LessOrEqualThanComparisonTrigger) t); }
        else if (t instanceof LessThanComparisonTrigger) { condition.setLessThan((LessThanComparisonTrigger) t); }
        else { System.err.println("Trigger error " + t); }
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
