package nl.thedocumentwizard.wizardconfiguration;

import nl.thedocumentwizard.wizardconfiguration.jaxb.ArrayOfWizardCondition;

/**
 * Created by jose on 02/01/2017.
 */
public class Condition extends ArrayOfWizardCondition.Condition {

    private String nextStepAlias;

    public Condition() {
        super();
    }

    public String getNextStepAlias() {
        return nextStepAlias;
    }

    public void setNextStepAlias(String nextStepAlias) {
        this.nextStepAlias = nextStepAlias;
    }
}
