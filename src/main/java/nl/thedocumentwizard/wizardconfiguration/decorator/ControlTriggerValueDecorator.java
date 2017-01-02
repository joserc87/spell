package nl.thedocumentwizard.wizardconfiguration.decorator;

import nl.thedocumentwizard.wizardconfiguration.jaxb.ControlTriggerValue;

/**
 * Decorator class for ControlTriggerValue
 */
public class ControlTriggerValueDecorator extends ControlTriggerValue {

    ControlTriggerValue decoratedControlTriggerValue;

    public ControlTriggerValueDecorator(ControlTriggerValue decoratedControlTriggerValue) {
        this.decoratedControlTriggerValue = decoratedControlTriggerValue;
    }

    public ControlTriggerValue getDecoratedControlTriggerValue() {
        return decoratedControlTriggerValue;
    }

    public String getId() {
        return decoratedControlTriggerValue.getId();
    }

    public void setId(String value) {
        decoratedControlTriggerValue.setId(value);
    }

    public Integer getStep() {
        return decoratedControlTriggerValue.getStep();
    }

    public void setStep(Integer value) {
        decoratedControlTriggerValue.setStep(value);
    }
}
