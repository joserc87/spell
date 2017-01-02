package nl.thedocumentwizard.wizardconfiguration;

import nl.thedocumentwizard.wizardconfiguration.decorator.ControlTriggerValueDecorator;
import nl.thedocumentwizard.wizardconfiguration.jaxb.ControlTriggerValue;

/**
 * Custom ControlTriggerValue
 */
public class ControlValue extends ControlTriggerValueDecorator {
    private String stepAlias;
    private String controlAlias;

    public ControlValue(ControlTriggerValue decoratedControlTriggerValue) {
        super(decoratedControlTriggerValue);
    }

    public String getStepAlias() {
        return stepAlias;
    }

    public void setStepAlias(String stepAlias) {
        this.stepAlias = stepAlias;
    }

    public String getControlAlias() {
        return controlAlias;
    }

    public void setControlAlias(String controlAlias) {
        this.controlAlias = controlAlias;
    }
}
