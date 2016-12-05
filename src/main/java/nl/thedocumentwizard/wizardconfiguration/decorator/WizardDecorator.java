package nl.thedocumentwizard.wizardconfiguration.decorator;

import nl.thedocumentwizard.wizardconfiguration.jaxb.ArrayOfSteptype;
import nl.thedocumentwizard.wizardconfiguration.jaxb.Wizard;

public class WizardDecorator extends Wizard {
    protected Wizard decoratedWizard;

    /**
     * Constructor to build the decorator
     *
     * @param wizard A JAXB wizard object to extend the functionality
     */
    public WizardDecorator(Wizard wizard) {
        this.decoratedWizard = wizard;
    }

    /**
     * Retrieves the decorated JAXB object
     *
     * @return a Wizard object
     */
    public Wizard getDecoratedWizard() {
        return decoratedWizard;
    }

    // Redirect methods to the decorated object:

    public ArrayOfSteptype getSteps() {
        return decoratedWizard.getSteps();
    }

    public void setSteps(ArrayOfSteptype value) {
        decoratedWizard.setSteps(value);
    }

    public String getName() {
        return decoratedWizard.getName();
    }

    public void setName(String value) {
        decoratedWizard.setName(value);
    }

    public String getDocumentTypeID() {
        return decoratedWizard.getDocumentTypeID();
    }

    public void setDocumentTypeID(String value) {
        decoratedWizard.setDocumentTypeID(value);
    }

    public String getDocumentTypeName() {
        return decoratedWizard.getDocumentTypeName();
    }

    public void setDocumentTypeName(String value) {
        decoratedWizard.setDocumentTypeName(value);
    }
}
