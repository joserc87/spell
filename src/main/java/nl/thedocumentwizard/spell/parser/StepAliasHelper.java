package nl.thedocumentwizard.spell.parser;

import nl.thedocumentwizard.wizardconfiguration.jaxb.Steptype;

import java.util.ArrayList;

/**
 * Created by jose on 02/01/2017.
 */
public class StepAliasHelper {

    // The array with all the control alias helpers related to each step
    // Each element will contain the step alias.
    // The provisional StepID will be -i-1 where i is the index of the element.
    private ArrayList<ControlAliasHelper> steps;

    /**
     * Constructor
     */
    public StepAliasHelper() {
        steps = new ArrayList<>();
    }

    /**
     * Gets the helper used to find controls within the step, given the actual step.
     * @param step The wizard step linked to the helper. This must be the exact same object that was registered with
     *             registerStep.
     * @return The alias helper when getStep() == step, or null when the helper was not found.
     */
    public ControlAliasHelper getAliasHelperForStep(Steptype step) {
        for (ControlAliasHelper cah : steps) {
            if (cah.getStep() == step) {
                return cah;
            }
        }
        return null;
    }

    public ControlAliasHelper getAliasHelperForStepAlias(String alias) {
        for (ControlAliasHelper cah : steps) {
            String cahAlias = cah.getStepAlias();
            if (cahAlias != null && cahAlias.equals(alias)) {
                return cah;
            }
        }
        return null;
    }

    public void registerStep(Steptype step) {
        registerStep(null, step);
    }

    public void registerStep(String alias, Steptype step) {
        steps.add(new ControlAliasHelper(alias, step));
    }
}
