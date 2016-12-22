package nl.thedocumentwizard.spell.parser;

import nl.thedocumentwizard.spell.MetadataStore;
import nl.thedocumentwizard.wizardconfiguration.WizardConfiguration;
import nl.thedocumentwizard.wizardconfiguration.jaxb.*;

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
}
