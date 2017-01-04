package nl.thedocumentwizard.wizardconfiguration;

import nl.thedocumentwizard.wizardconfiguration.jaxb.Steptype;

/**
 * A custom WizardStep
 */
public class Step extends Steptype {

    private String nextStepAlias;

    public Step() {
        super();
    }

    public String getNextStepAlias() {
        return nextStepAlias;
    }

    public void setNextStepAlias(String nextStepAlias) {
        this.nextStepAlias = nextStepAlias;
    }
}
