package nl.thedocumentwizard.spell.parser;

import nl.thedocumentwizard.wizardconfiguration.WizardConfiguration;
import nl.thedocumentwizard.wizardconfiguration.jaxb.Steptype;

/**
 * Created by jose on 21/12/2016.
 */
public class PostProcessor {

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
}
