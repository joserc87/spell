package nl.thedocumentwizard.spell.parser;

import nl.thedocumentwizard.spell.MetadataStore;
import nl.thedocumentwizard.wizardconfiguration.WizardConfiguration;
import nl.thedocumentwizard.wizardconfiguration.jaxb.*;

/**
 * Created by jose on 21/12/2016.
 */
public class PostProcessor {

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

    private void assignMetadataIDsToExplicitWizardMetadata(ExplicitWizardMetadata metadata, MetadataStore metadataStore) {
        if (metadata != null && metadata.getMetadataName() != null) {
            metadata.setMetadataID(metadataStore.findMetadataIDByName(metadata.getMetadataName()));
        }
    }

    private void assignMetadataIDsToImplicitWizardMetadata(ImplicitWizardMetadata metadata, MetadataStore metadataStore) {
        if (metadata != null && metadata.getName() != null) {
            metadata.setId(metadataStore.findMetadataIDByName(metadata.getName()));
        }
    }

    private void assignMetadataIDsToControl(AbstractControl control, MetadataStore metadataStore) {
        // Control can be null
        if (control != null) {
            // For al controls:
            // set metadataID
            if (control.getMetadataName() != null) {
                control.setMetadataID(metadataStore.findMetadataIDByName(control.getMetadataName()));
            }
            // set defaultValueMetadataID
            if (control.getDefaultValueMetadataName() != null) {
                control.setDefaultValueMetadataID(metadataStore.findMetadataIDByName(control.getDefaultValueMetadataName()));
            }
            // For controls with sub controls or items, recurse:
            if (control instanceof RadioControl) {
                RadioControl radio = (RadioControl) control;
                if (radio.getItems() != null) {
                    for (AbstractControl subControl : radio.getItems().getTextOrCheckboxOrAttachment()) {
                        this.assignMetadataIDsToControl(subControl, metadataStore);
                    }
                }
            } else if (control instanceof MultiControl) {
                MultiControl multi = (MultiControl) control;
                if (multi.getControls() != null) {
                    for (AbstractControl subControl : multi.getControls().getTextOrNumberOrAttachment()) {
                        this.assignMetadataIDsToControl(subControl, metadataStore);
                    }
                }
            } else if (control instanceof ListControl) {
                ListControl list = (ListControl) control;
                if (list.getItems() != null) {
                    for (ListItem item : list.getItems().getItem()) {
                        this.assignMetadataIDsToExplicitWizardMetadata(item.getDisplayText(), metadataStore);
                        this.assignMetadataIDsToExplicitWizardMetadata(item.getValue(), metadataStore);
                    }
                }
            } else if (control instanceof FileControl) {
                FileControl file = (FileControl) control;
                if (file.getFileName() != null) {
                    this.assignMetadataIDsToControl(file.getFileName(), metadataStore);
                }

            }
        }
    }

    private void assignMetadataIDsToTrigger(Trigger trigger, MetadataStore metadataStore) {
        // Control can be null
        if (trigger != null) {
            if (trigger instanceof ComplexTrigger) {
                ComplexTrigger complexTrigger = (ComplexTrigger) trigger;
                if (complexTrigger.getOrOrLessThanOrRegEx() != null) {
                    for (Trigger subTrigger : complexTrigger.getOrOrLessThanOrRegEx()) {
                        this.assignMetadataIDsToTrigger(subTrigger, metadataStore);
                    }
                }
            } else if (trigger instanceof UnaryTrigger) {
                UnaryTrigger unaryTrigger = (UnaryTrigger) trigger;
                this.assignMetadataIDToMetadataTriggerValue(unaryTrigger.getMetadata(), metadataStore);
            } else if (trigger instanceof BinaryTrigger) {
                BinaryTrigger binaryTrigger = (BinaryTrigger) trigger;
                if (binaryTrigger.getControlOrConstOrMetadata() != null) {
                    for (TriggerValue value : binaryTrigger.getControlOrConstOrMetadata()) {
                        if (value instanceof  MetadataTriggerValue) {
                            this.assignMetadataIDToMetadataTriggerValue((MetadataTriggerValue) value, metadataStore);
                        }
                    }
                }
            }
        }
    }

    private void assignMetadataIDToMetadataTriggerValue(MetadataTriggerValue mtv, MetadataStore metadataStore) {
        if (mtv != null && mtv.getName() != null) {
            mtv.setId(metadataStore.findMetadataIDByName(mtv.getName()));
        }
    }

    public void assignMetadataIDs(WizardConfiguration wizard, MetadataStore metadataStore) {
        if (wizard.getSteps() != null) {
            for (Steptype step : wizard.getSteps().getStep()) {
                // Look for metadatas in controls
                if (step.getQuestions() != null) {
                    for(ArrayOfWizardQuestion.Question question : step.getQuestions().getQuestion()) {
                        this.assignMetadataIDsToControl(question.getLabel(), metadataStore);
                        this.assignMetadataIDsToControl(question.getString(), metadataStore);
                        this.assignMetadataIDsToControl(question.getEmail(), metadataStore);
                        this.assignMetadataIDsToControl(question.getText(), metadataStore);
                        this.assignMetadataIDsToControl(question.getNumber(), metadataStore);
                        this.assignMetadataIDsToControl(question.getDate(), metadataStore);
                        this.assignMetadataIDsToControl(question.getImage(), metadataStore);
                        this.assignMetadataIDsToControl(question.getAttachment(), metadataStore);
                        this.assignMetadataIDsToControl(question.getCheckbox(), metadataStore);
                        this.assignMetadataIDsToControl(question.getRadio(), metadataStore);
                        this.assignMetadataIDsToControl(question.getList(), metadataStore);
                        this.assignMetadataIDsToControl(question.getMulti(), metadataStore);
                    }
                }

                // Look for metadatas in advanced rules
                if (step.getAdvancedRules() != null) {
                    for (ArrayOfWizardAdvancedRule.AdvancedRule ar : step.getAdvancedRules().getAdvancedRule()) {
                        if (ar.getMetadatas() != null) {
                            for (ImplicitWizardMetadata metadata : ar.getMetadatas().getMetadata()) {
                                this.assignMetadataIDsToImplicitWizardMetadata(metadata, metadataStore);
                            }
                        }
                    }
                }
                // Look for metadatas in triggers in conditions and advanced rules
                if (step.getAdvancedRules() != null) {
                    for (ArrayOfWizardAdvancedRule.AdvancedRule ar : step.getAdvancedRules().getAdvancedRule()) {
                        this.assignMetadataIDsToTrigger(ar.getAnd(), metadataStore);
                        this.assignMetadataIDsToTrigger(ar.getOr(), metadataStore);
                        this.assignMetadataIDsToTrigger(ar.getNot(), metadataStore);
                        this.assignMetadataIDsToTrigger(ar.getEmpty(), metadataStore);
                        this.assignMetadataIDsToTrigger(ar.getEqual(), metadataStore);
                        this.assignMetadataIDsToTrigger(ar.getDifferent(), metadataStore);
                        this.assignMetadataIDsToTrigger(ar.getGreaterOrEqualThan(), metadataStore);
                        this.assignMetadataIDsToTrigger(ar.getGreaterThan(), metadataStore);
                        this.assignMetadataIDsToTrigger(ar.getLessOrEqualThan(), metadataStore);
                        this.assignMetadataIDsToTrigger(ar.getLessThan(), metadataStore);
                        this.assignMetadataIDsToTrigger(ar.getRegEx(), metadataStore);
                    }
                }

                // Look for metadatas in triggers in conditions and advanced rules
                if (step.getConditions() != null) {
                    for (ArrayOfWizardCondition.Condition condition : step.getConditions().getCondition()) {
                        this.assignMetadataIDsToTrigger(condition.getAnd(), metadataStore);
                        this.assignMetadataIDsToTrigger(condition.getOr(), metadataStore);
                        this.assignMetadataIDsToTrigger(condition.getNot(), metadataStore);
                        this.assignMetadataIDsToTrigger(condition.getEmpty(), metadataStore);
                        this.assignMetadataIDsToTrigger(condition.getEqual(), metadataStore);
                        this.assignMetadataIDsToTrigger(condition.getDifferent(), metadataStore);
                        this.assignMetadataIDsToTrigger(condition.getGreaterOrEqualThan(), metadataStore);
                        this.assignMetadataIDsToTrigger(condition.getGreaterThan(), metadataStore);
                        this.assignMetadataIDsToTrigger(condition.getLessOrEqualThan(), metadataStore);
                        this.assignMetadataIDsToTrigger(condition.getLessThan(), metadataStore);
                        this.assignMetadataIDsToTrigger(condition.getRegEx(), metadataStore);
                    }
                }

            }

        }


    }
}
