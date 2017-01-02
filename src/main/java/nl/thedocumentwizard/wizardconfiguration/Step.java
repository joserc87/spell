package nl.thedocumentwizard.wizardconfiguration;

import nl.thedocumentwizard.wizardconfiguration.decorator.StepDecorator;
import nl.thedocumentwizard.wizardconfiguration.jaxb.Steptype;

/**
 * A custom WizardStep
 */
public class Step extends StepDecorator {

    private String nextStepAlias;

    public Step(Steptype decoratedStep) {
        super(decoratedStep);
    }

    public String getNextStepAlias() {
        return nextStepAlias;
    }

    public void setNextStepAlias(String nextStepAlias) {
        this.nextStepAlias = nextStepAlias;
    }
}
