package nl.thedocumentwizard.wizardconfiguration;

import nl.thedocumentwizard.wizardconfiguration.decorator.ConditionDecorator;
import nl.thedocumentwizard.wizardconfiguration.jaxb.ArrayOfWizardCondition;

/**
 * Created by jose on 02/01/2017.
 */
public class Condition extends ConditionDecorator {

    private String nextStepAlias;

    public Condition(ArrayOfWizardCondition.Condition delegate) {
        super(delegate);
    }

    public String getNextStepAlias() {
        return nextStepAlias;
    }

    public void setNextStepAlias(String nextStepAlias) {
        this.nextStepAlias = nextStepAlias;
    }
}
