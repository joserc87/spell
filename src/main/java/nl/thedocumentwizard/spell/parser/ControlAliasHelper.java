package nl.thedocumentwizard.spell.parser;

import nl.thedocumentwizard.wizardconfiguration.jaxb.AbstractControl;
import nl.thedocumentwizard.wizardconfiguration.jaxb.Steptype;

import java.util.HashMap;

/**
 * Created by jose on 02/01/2017.
 */
public class ControlAliasHelper {

    // Holds the control that corresponds to each alias, local to this step
    private String stepAlias;
    private Steptype step;
    private HashMap<String, AbstractControl> controlForAlias;

    /**
     * Constructor
     * @param stepAlias The alias of the step
     * @param step
     */
    public ControlAliasHelper(String stepAlias, Steptype step) {
        this.stepAlias = stepAlias;
        this.step = step;
        this.controlForAlias = new HashMap<>();
    }

    public String getControlIdForAlias(String  alias) {
        // For now, the control ID will be the same as the alias, but that can change and it will depend on the helper
        return alias;
    }

    public AbstractControl findControl(String controlAlias) {
        if (controlForAlias.containsKey(controlAlias)) {
            return controlForAlias.get(controlAlias);
        } else {
            return null;
        }
    }

    public void registerControl(String controlAlias, AbstractControl control) {
        if (!controlForAlias.containsKey(controlAlias)) {
            controlForAlias.put(controlAlias, control);
        } else {
            System.err.println("Step already contains a control with name " + controlAlias);
        }
    }

    public String getStepAlias() {
        return stepAlias;
    }

    public Steptype getStep() {
        return step;
    }
}
