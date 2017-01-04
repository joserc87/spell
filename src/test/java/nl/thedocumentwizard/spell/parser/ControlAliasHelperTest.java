package nl.thedocumentwizard.spell.parser;

import nl.thedocumentwizard.wizardconfiguration.jaxb.AbstractControl;
import nl.thedocumentwizard.wizardconfiguration.jaxb.Steptype;
import nl.thedocumentwizard.wizardconfiguration.jaxb.StringControl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by jose on 02/01/2017.
 */
public class ControlAliasHelperTest {

    ControlAliasHelper helper;
    Steptype step;

    @Before
    public void becauseThereIsAHelper() {
        step = new Steptype();
        helper = new ControlAliasHelper("alias", step);
    }

    @Test
    public void getControlIdForAlias_should_return_same_alias_as_the_id() throws Exception {
        // For now, the control ID will be the same as the alias, but that can change and it will depend on the helper
        Assert.assertEquals("myControl", helper.getControlIdForAlias("myControl"));
    }

    @Test
    public void getStepAlias_should_return_the_alias() throws Exception {
        Assert.assertEquals("alias", helper.getStepAlias());
    }

    @Test
    public void getStep_should_return_the_step() throws Exception {
        Assert.assertSame(step, helper.getStep());
    }

    @Test
    public void getControl_should_return_null_when_not_registered() throws Exception {
        Assert.assertNull(helper.findControl("controlAlias"));
    }

    @Test
    public void getControl_should_return_registered_control() throws Exception {
        Assert.assertNull(helper.findControl("controlAlias"));
        AbstractControl control = new StringControl();
        helper.registerControl("controlAlias", control);
        Assert.assertSame(control, helper.findControl("controlAlias"));
    }
}