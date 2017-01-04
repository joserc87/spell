package nl.thedocumentwizard.wizardconfiguration;

import nl.thedocumentwizard.wizardconfiguration.jaxb.ControlTriggerValue;

/**
 * Custom ControlTriggerValue
 */
public class ControlValue extends ControlTriggerValue {
    private String stepAlias;
    private String controlAlias;

    public ControlValue() {
        super();
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
