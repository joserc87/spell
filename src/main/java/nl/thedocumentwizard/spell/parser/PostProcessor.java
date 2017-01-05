package nl.thedocumentwizard.spell.parser;

import nl.thedocumentwizard.spell.MetadataStore;
import nl.thedocumentwizard.wizardconfiguration.*;
import nl.thedocumentwizard.wizardconfiguration.jaxb.*;
import nl.thedocumentwizard.wizardconfiguration.jaxb.RadioControl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jose on 21/12/2016.
 */
public class PostProcessor {

    MetadataStore metadataStore;
    public PostProcessor(MetadataStore metadataStore) {
        this.metadataStore = metadataStore;
    }

    /**
     * Set the step IDs in an incremental order.
     * @param wizard The wizard that contains the steps to change.
     */
    public void assignStepIDs(WizardConfiguration wizard) {
        if (wizard.getSteps() != null) {
            int i = 1;
            for (Steptype step : wizard.getSteps().getStep()) {
                step.setId(i);
                i++;
            }
        }
    }

    /**
     * Set the IDs for the questions and the controls inside the questions.
     * @param wizard The wizard that contains the questions and controls
     */
    public void assignQuestionIDs(WizardConfiguration wizard) {
        if (wizard.getSteps() != null) {
            for (Steptype step : wizard.getSteps().getStep()) {
                if (step.getQuestions() != null) {
                    int questionNum = 1;
                    for (ArrayOfWizardQuestion.Question question : step.getQuestions().getQuestion()) {
                        question.setId("QUESTION_" + questionNum);
                        String controlID = "CONTROL_" + questionNum;
                        this.assignControlID(question.getLabel(), controlID);
                        this.assignControlID(question.getString(), controlID);
                        this.assignControlID(question.getEmail(), controlID);
                        this.assignControlID(question.getText(), controlID);
                        this.assignControlID(question.getNumber(), controlID);
                        this.assignControlID(question.getDate(), controlID);
                        this.assignControlID(question.getImage(), controlID);
                        this.assignControlID(question.getAttachment(), controlID);
                        this.assignControlID(question.getCheckbox(), controlID);
                        this.assignControlID(question.getRadio(), controlID);
                        this.assignControlID(question.getList(), controlID);
                        this.assignControlID(question.getMulti(), controlID);
                        questionNum++;
                    }
                }
            }
        }
    }

    /**
     * Set the ID for a control, and all the subcontrols inside, recursively
     * @param control The control to set the ID
     * @param id The ID of the control, e.g. "CONTROL_2_1". Subcontrols will be set with id + "_X".
     */
    private void assignControlID(AbstractControl control, String id) {
        if (control != null) {
            control.setId(id);
            // Set ids to subcontrols, recursively
            if (control instanceof RadioControl) {
                RadioControl radio = (RadioControl) control;
                if (radio.getItems() != null) {
                    int i = 1;
                    for (AbstractControl subcontrol : radio.getItems().getTextOrCheckboxOrAttachment()) {
                        assignControlID(subcontrol, id + "_" + i);
                        i++;
                    }
                }
            } else if (control instanceof MultiControl) {
                MultiControl multi = (MultiControl) control;
                if (multi.getControls() != null) {
                    int i = 1;
                    for (AbstractControl subcontrol : multi.getControls().getTextOrNumberOrAttachment()) {
                        assignControlID(subcontrol, id + "_" + i);
                        i++;
                    }
                }
            } else if (control instanceof FileControl) {
                FileControl fileControl = (FileControl) control;
                assignControlID(fileControl.getFileName(), id + "_1");
            }
        }
    }

    private String findMetadataIDByName(String name) {
        if (this.metadataStore != null) {
            String guid = this.metadataStore.findMetadataIDByName(name);
            if (guid == null) {
                System.err.println("Metadata with name '" + name + "' not found");
                return null;
            } else {
                return guid;
            }
        } else {
            return "????????-????-????-????-????????????";
        }
    }

    private void assignMetadataIDsToExplicitWizardMetadata(ExplicitWizardMetadata metadata) {
        if (metadata != null && metadata.getMetadataName() != null) {
            metadata.setMetadataID(findMetadataIDByName(metadata.getMetadataName()));
        }
    }

    private void assignMetadataIDsToImplicitWizardMetadata(ImplicitWizardMetadata metadata) {
        if (metadata != null && metadata.getName() != null) {
            metadata.setId(findMetadataIDByName(metadata.getName()));
        }
    }

    private void assignMetadataIDsToControl(AbstractControl control) {
        // Control can be null
        if (control != null) {
            // For al controls:
            // set metadataID
            if (control.getMetadataName() != null) {
                control.setMetadataID(findMetadataIDByName(control.getMetadataName()));
            }
            // set defaultValueMetadataID
            if (control.getDefaultValueMetadataName() != null) {
                control.setDefaultValueMetadataID(findMetadataIDByName(control.getDefaultValueMetadataName()));
            }
            // For controls with sub controls or items, recurse:
            if (control instanceof RadioControl) {
                RadioControl radio = (RadioControl) control;
                if (radio.getItems() != null) {
                    for (AbstractControl subControl : radio.getItems().getTextOrCheckboxOrAttachment()) {
                        this.assignMetadataIDsToControl(subControl);
                    }
                }
            } else if (control instanceof MultiControl) {
                MultiControl multi = (MultiControl) control;
                if (multi.getControls() != null) {
                    for (AbstractControl subControl : multi.getControls().getTextOrNumberOrAttachment()) {
                        this.assignMetadataIDsToControl(subControl);
                    }
                }
            } else if (control instanceof ListControl) {
                ListControl list = (ListControl) control;
                if (list.getItems() != null) {
                    for (ListItem item : list.getItems().getItem()) {
                        this.assignMetadataIDsToExplicitWizardMetadata(item.getDisplayText());
                        this.assignMetadataIDsToExplicitWizardMetadata(item.getValue());
                    }
                }
            } else if (control instanceof FileControl) {
                FileControl file = (FileControl) control;
                if (file.getFileName() != null) {
                    this.assignMetadataIDsToControl(file.getFileName());
                }

            }
        }
    }

    private void assignMetadataIDsToTrigger(Trigger trigger) {
        // Control can be null
        if (trigger != null) {
            if (trigger instanceof ComplexTrigger) {
                ComplexTrigger complexTrigger = (ComplexTrigger) trigger;
                if (complexTrigger.getOrOrLessThanOrRegEx() != null) {
                    for (Trigger subTrigger : complexTrigger.getOrOrLessThanOrRegEx()) {
                        this.assignMetadataIDsToTrigger(subTrigger);
                    }
                }
            } else if (trigger instanceof UnaryTrigger) {
                UnaryTrigger unaryTrigger = (UnaryTrigger) trigger;
                this.assignMetadataIDToMetadataTriggerValue(unaryTrigger.getMetadata());
            } else if (trigger instanceof BinaryTrigger) {
                BinaryTrigger binaryTrigger = (BinaryTrigger) trigger;
                if (binaryTrigger.getControlOrConstOrMetadata() != null) {
                    for (TriggerValue value : binaryTrigger.getControlOrConstOrMetadata()) {
                        if (value instanceof  MetadataTriggerValue) {
                            this.assignMetadataIDToMetadataTriggerValue((MetadataTriggerValue) value);
                        }
                    }
                }
            }
        }
    }

    private void assignMetadataIDToMetadataTriggerValue(MetadataTriggerValue mtv) {
        if (mtv != null && mtv.getName() != null) {
            mtv.setId(findMetadataIDByName(mtv.getName()));
        }
    }

    public void assignMetadataIDs(WizardConfiguration wizard) {
        if (wizard.getSteps() != null) {
            for (Steptype step : wizard.getSteps().getStep()) {
                // Look for metadatas in controls
                if (step.getQuestions() != null) {
                    for(ArrayOfWizardQuestion.Question question : step.getQuestions().getQuestion()) {
                        this.assignMetadataIDsToControl(question.getLabel());
                        this.assignMetadataIDsToControl(question.getString());
                        this.assignMetadataIDsToControl(question.getEmail());
                        this.assignMetadataIDsToControl(question.getText());
                        this.assignMetadataIDsToControl(question.getNumber());
                        this.assignMetadataIDsToControl(question.getDate());
                        this.assignMetadataIDsToControl(question.getImage());
                        this.assignMetadataIDsToControl(question.getAttachment());
                        this.assignMetadataIDsToControl(question.getCheckbox());
                        this.assignMetadataIDsToControl(question.getRadio());
                        this.assignMetadataIDsToControl(question.getList());
                        this.assignMetadataIDsToControl(question.getMulti());
                    }
                }

                // Look for metadatas in advanced rules
                if (step.getAdvancedRules() != null) {
                    for (ArrayOfWizardAdvancedRule.AdvancedRule ar : step.getAdvancedRules().getAdvancedRule()) {
                        if (ar.getMetadatas() != null) {
                            for (ImplicitWizardMetadata metadata : ar.getMetadatas().getMetadata()) {
                                this.assignMetadataIDsToImplicitWizardMetadata(metadata);
                            }
                        }
                    }
                }
                // Look for metadatas in triggers in conditions and advanced rules
                if (step.getAdvancedRules() != null) {
                    for (ArrayOfWizardAdvancedRule.AdvancedRule ar : step.getAdvancedRules().getAdvancedRule()) {
                        this.assignMetadataIDsToTrigger(ar.getAnd());
                        this.assignMetadataIDsToTrigger(ar.getOr());
                        this.assignMetadataIDsToTrigger(ar.getNot());
                        this.assignMetadataIDsToTrigger(ar.getEmpty());
                        this.assignMetadataIDsToTrigger(ar.getEqual());
                        this.assignMetadataIDsToTrigger(ar.getDifferent());
                        this.assignMetadataIDsToTrigger(ar.getGreaterOrEqualThan());
                        this.assignMetadataIDsToTrigger(ar.getGreaterThan());
                        this.assignMetadataIDsToTrigger(ar.getLessOrEqualThan());
                        this.assignMetadataIDsToTrigger(ar.getLessThan());
                        this.assignMetadataIDsToTrigger(ar.getRegEx());
                    }
                }

                // Look for metadatas in triggers in conditions and advanced rules
                if (step.getConditions() != null) {
                    for (ArrayOfWizardCondition.Condition condition : step.getConditions().getCondition()) {
                        this.assignMetadataIDsToTrigger(condition.getAnd());
                        this.assignMetadataIDsToTrigger(condition.getOr());
                        this.assignMetadataIDsToTrigger(condition.getNot());
                        this.assignMetadataIDsToTrigger(condition.getEmpty());
                        this.assignMetadataIDsToTrigger(condition.getEqual());
                        this.assignMetadataIDsToTrigger(condition.getDifferent());
                        this.assignMetadataIDsToTrigger(condition.getGreaterOrEqualThan());
                        this.assignMetadataIDsToTrigger(condition.getGreaterThan());
                        this.assignMetadataIDsToTrigger(condition.getLessOrEqualThan());
                        this.assignMetadataIDsToTrigger(condition.getLessThan());
                        this.assignMetadataIDsToTrigger(condition.getRegEx());
                    }
                }

            }

        }
    }

    public void resolveAlias(WizardConfiguration wizard, StepAliasHelper aliasHelper) {
        if (wizard.getSteps() != null) {
            for (Steptype step : wizard.getSteps().getStep()) {
                ControlAliasHelper currentStepAliasHelper = aliasHelper.getAliasHelperForStep(step);
                String nextStepAlias = step instanceof Step ? ((Step) step).getNextStepAlias() : null;
                // Translate next step:
                if (nextStepAlias != null) {
                    ControlAliasHelper nextStepAliasHelper = aliasHelper.getAliasHelperForStepAlias(nextStepAlias);
                    if (nextStepAliasHelper != null) { // If next step exists:
                        // Set the ID for the next step
                        step.setNextStepID(nextStepAliasHelper.getStep().getId());
                    } else {
                        System.err.println("In step " + step.getId() +
                                ": Step '" + nextStepAlias + "' does not exist");
                    }
                }
                // Translate conditional next step:
                if (step.getConditions() != null) {
                    for(ArrayOfWizardCondition.Condition condition : step.getConditions().getCondition()) {
                        String conditionalNextStepAlias = ((Condition)condition).getNextStepAlias();
                        if (conditionalNextStepAlias != null) {
                            ControlAliasHelper nextStepAliasHelper = aliasHelper.getAliasHelperForStepAlias(conditionalNextStepAlias);
                            if (nextStepAliasHelper != null) { // If next step exists:
                                // Set the ID for the next step
                                condition.setNextStepID(nextStepAliasHelper.getStep().getId());
                            } else {
                                System.err.println("In condition in step " + step.getId() +
                                        ": Step '" + conditionalNextStepAlias + "' does not exist.");
                            }
                        }
                    }
                }
                // Translate control default value
                if (step.getQuestions() != null) {
                    for (ArrayOfWizardQuestion.Question question : step.getQuestions().getQuestion()) {
                        this.resolveAliasDefaultValue(question.getRadio(), currentStepAliasHelper);
                        this.resolveAliasDefaultValue(question.getMulti(), currentStepAliasHelper);
                    }
                }
                // Translate ControlTriggerValue:
                ArrayList<ControlValue> controlValues = new ArrayList<ControlValue>();
                // Conditions
                if (step.getConditions() != null) {
                    for (ArrayOfWizardCondition.Condition condition : step.getConditions().getCondition()) {
                        findControlValues(condition.getAnd()               , controlValues);
                        findControlValues(condition.getOr()                , controlValues);
                        findControlValues(condition.getNot()               , controlValues);
                        findControlValues(condition.getEmpty()             , controlValues);
                        findControlValues(condition.getEqual()             , controlValues);
                        findControlValues(condition.getDifferent()         , controlValues);
                        findControlValues(condition.getGreaterOrEqualThan(), controlValues);
                        findControlValues(condition.getGreaterThan()       , controlValues);
                        findControlValues(condition.getLessOrEqualThan()   , controlValues);
                        findControlValues(condition.getLessThan()          , controlValues);
                        findControlValues(condition.getRegEx()             , controlValues);
                    }
                }
                // AdvancedRules
                if (step.getAdvancedRules() != null) {
                    for (ArrayOfWizardAdvancedRule.AdvancedRule ar : step.getAdvancedRules().getAdvancedRule()) {
                        findControlValues(ar.getAnd()               , controlValues);
                        findControlValues(ar.getOr()                , controlValues);
                        findControlValues(ar.getNot()               , controlValues);
                        findControlValues(ar.getEmpty()             , controlValues);
                        findControlValues(ar.getEqual()             , controlValues);
                        findControlValues(ar.getDifferent()         , controlValues);
                        findControlValues(ar.getGreaterOrEqualThan(), controlValues);
                        findControlValues(ar.getGreaterThan()       , controlValues);
                        findControlValues(ar.getLessOrEqualThan()   , controlValues);
                        findControlValues(ar.getLessThan()          , controlValues);
                        findControlValues(ar.getRegEx()             , controlValues);
                    }
                }
                for (ControlValue val : controlValues) {
                    ControlAliasHelper referencedStepAliasHelper;
                    // If STEP.CONTROL, find the step
                    if (val.getStepAlias() != null) {
                        referencedStepAliasHelper = aliasHelper.getAliasHelperForStepAlias(val.getStepAlias());
                    } else {
                        // Otherwise, use the current step implicitly
                        referencedStepAliasHelper = currentStepAliasHelper;
                    }

                    if (referencedStepAliasHelper != null) {
                        AbstractControl referencedControl = referencedStepAliasHelper.findControl(val.getControlAlias());
                        // The step ID will only be explicit when the form Step.Control is used.
                        if (val.getStepAlias() != null) {
                            val.setStep(referencedStepAliasHelper.getStep().getId());
                        }
                        if (referencedControl != null) {
                            val.setId(referencedControl.getId());
                        } else {
                            System.err.println("Control " + val.getControlAlias() + " does not exist");
                        }
                    } else {
                        System.err.println("Step " + val.getStepAlias() + " does not exist");
                    }
                }
            }
        }
    }

    private void resolveAliasDefaultValue(AbstractControl control, ControlAliasHelper currentStepAliasHelper) {
        if (control != null) {
            if (control instanceof nl.thedocumentwizard.wizardconfiguration.RadioControl) {
                nl.thedocumentwizard.wizardconfiguration.RadioControl radio = (nl.thedocumentwizard.wizardconfiguration.RadioControl) control;
                String alias = radio.getAliasDefaultValue();
                if (alias != null) {
                    AbstractControl referencedControl = currentStepAliasHelper.findControl(alias);
                    if (referencedControl != null) {
                        radio.setDefaultValue(referencedControl.getId());
                    } else {
                        System.err.println("Control " + alias + " does not exist");
                    }
                }
            }
            // Recursive
            if (control instanceof RadioControl) {
                RadioControl radio = (RadioControl) control;
                if (radio.getItems() != null) {
                    for (AbstractControl subControl : radio.getItems().getTextOrCheckboxOrAttachment()) {
                        this.resolveAliasDefaultValue(subControl, currentStepAliasHelper);
                    }
                }
            }
            if (control instanceof MultiControl) {
                MultiControl multi = (MultiControl) control;
                if (multi.getControls() != null) {
                    for (AbstractControl subControl : multi.getControls().getTextOrNumberOrAttachment()) {
                        this.resolveAliasDefaultValue(subControl, currentStepAliasHelper);
                    }
                }
            }
        }

    }

    public void findControlValues(Trigger t, List<ControlValue> values) {
        if (t != null) {
            if (t instanceof AndTrigger) {
                AndTrigger and = (AndTrigger) t;
                for (Trigger subt : and.getOrOrLessThanOrRegEx()) {
                    findControlValues(subt, values);
                }
            } else if (t instanceof OrTrigger) {
                OrTrigger or = (OrTrigger) t;
                for (Trigger subt : or.getOrOrLessThanOrRegEx()) {
                    findControlValues(subt, values);
                }
            } else if (t instanceof NotTrigger) {
                NotTrigger not = (NotTrigger) t;
                for (Trigger subt : not.getOrOrLessThanOrRegEx()) {
                    findControlValues(subt, values);
                }
            } else if (t instanceof EmptyTrigger) {
                EmptyTrigger et = (EmptyTrigger) t;
                if (et.getControl() != null) {
                    values.add((ControlValue)et.getControl());
                }
            } else if (t instanceof RegexTrigger) {
                RegexTrigger rt = (RegexTrigger) t;
                if (rt.getControl() != null) {
                    values.add((ControlValue)rt.getControl());
                }
            } else if (t instanceof EqualComparisonTrigger) {
                EqualComparisonTrigger eqt = (EqualComparisonTrigger)t;
                for (TriggerValue val : eqt.getControlOrConstOrMetadata()) {
                    if (val instanceof ControlTriggerValue) {
                        values.add((ControlValue)val);
                    }
                }
            } else if (t instanceof DifferentComparisonTrigger) {
                DifferentComparisonTrigger dift = (DifferentComparisonTrigger)t;
                for (TriggerValue val : dift.getControlOrConstOrMetadata()) {
                    if (val instanceof ControlTriggerValue) {
                        values.add((ControlValue)val);
                    }
                }
            } else if (t instanceof GreaterOrEqualThanComparisonTrigger) {
                GreaterOrEqualThanComparisonTrigger get = (GreaterOrEqualThanComparisonTrigger)t;
                for (TriggerValue val : get.getControlOrConstOrMetadata()) {
                    if (val instanceof ControlTriggerValue) {
                        values.add((ControlValue)val);
                    }
                }
            } else if (t instanceof GreaterThanComparisonTrigger) {
                GreaterThanComparisonTrigger gt = (GreaterThanComparisonTrigger)t;
                for (TriggerValue val : gt.getControlOrConstOrMetadata()) {
                    if (val instanceof ControlTriggerValue) {
                        values.add((ControlValue)val);
                    }
                }
            } else if (t instanceof LessOrEqualThanComparisonTrigger) {
                LessOrEqualThanComparisonTrigger let = (LessOrEqualThanComparisonTrigger)t;
                for (TriggerValue val : let.getControlOrConstOrMetadata()) {
                    if (val instanceof ControlTriggerValue) {
                        values.add((ControlValue)val);
                    }
                }
            } else if (t instanceof LessThanComparisonTrigger) {
                LessThanComparisonTrigger lt = (LessThanComparisonTrigger)t;
                for (TriggerValue val : lt.getControlOrConstOrMetadata()) {
                    if (val instanceof ControlTriggerValue) {
                        values.add((ControlValue)val);
                    }
                }
            }
        }
    }
}
