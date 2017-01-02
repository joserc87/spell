package nl.thedocumentwizard.wizardconfiguration.decorator;

import nl.thedocumentwizard.wizardconfiguration.jaxb.ArrayOfWizardAdvancedRule;
import nl.thedocumentwizard.wizardconfiguration.jaxb.ArrayOfWizardCondition;
import nl.thedocumentwizard.wizardconfiguration.jaxb.ArrayOfWizardQuestion;
import nl.thedocumentwizard.wizardconfiguration.jaxb.Steptype;

/**
 * Decorator class for Steptype
 */
public class StepDecorator extends Steptype {

    Steptype decoratedStep;

    public StepDecorator(Steptype decoratedStep) {
        this.decoratedStep = decoratedStep;
    }

    public Steptype getDecoratedStep() {
        return decoratedStep;
    }

    public ArrayOfWizardCondition getConditions() {
        return decoratedStep.getConditions();
    }

    public void setConditions(ArrayOfWizardCondition value) {
        decoratedStep.setConditions(value);
    }

    public ArrayOfWizardAdvancedRule getAdvancedRules() {
        return decoratedStep.getAdvancedRules();
    }

    public void setAdvancedRules(ArrayOfWizardAdvancedRule value) {
        decoratedStep.setAdvancedRules(value);
    }

    public ArrayOfWizardQuestion getQuestions() {
        return decoratedStep.getQuestions();
    }

    public void setQuestions(ArrayOfWizardQuestion value) {
        decoratedStep.setQuestions(value);
    }

    public int getId() {
        return decoratedStep.getId();
    }

    public void setId(int value) {
        decoratedStep.setId(value);
    }

    public String getName() {
        return decoratedStep.getName();
    }

    public void setName(String value) {
        decoratedStep.setName(value);
    }

    public String getGroupName() {
        return decoratedStep.getGroupName();
    }

    public void setGroupName(String value) {
        decoratedStep.setGroupName(value);
    }

    public String getType() {
        return decoratedStep.getType();
    }

    public void setType(String value) {
        decoratedStep.setType(value);
    }

    public Integer getNextStepID() {
        return decoratedStep.getNextStepID();
    }

    public void setNextStepID(Integer value) {
        decoratedStep.setNextStepID(value);
    }
}
