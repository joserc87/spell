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
public class StepAliasHelperTest {

    StepAliasHelper helper;

    @Before
    public void becauseThereIsAHelper() {
        helper = new StepAliasHelper();
    }

    @Test
    public void getAliasHelperForStep_should_return_null_when_step_not_known() throws Exception {
        Assert.assertNull(helper.getAliasHelperForStep(new Steptype()));
    }

    @Test
    public void getAliasHelperForStep_should_return_step_when_known() throws Exception {
        Steptype step = new Steptype();
        Assert.assertNull(helper.getAliasHelperForStep(step));
        helper.registerStep(step);
        ControlAliasHelper controlAliasHelper = helper.getAliasHelperForStep(step);
        Assert.assertNotNull(controlAliasHelper);
    }

    @Test
    public void getAliasHelperForStepAlias_should_return_step_when_known() throws Exception {
        Steptype step = new Steptype();
        Assert.assertNull(helper.getAliasHelperForStepAlias("alias"));
        helper.registerStep("alias", step);
        ControlAliasHelper controlAliasHelper = helper.getAliasHelperForStep(step);
        Assert.assertNotNull(controlAliasHelper);
        Assert.assertSame(controlAliasHelper, helper.getAliasHelperForStepAlias("alias"));
    }

    @Test
    public void getAliasHelperForStepAlias_should_return_null_when_not_known() throws Exception {
        Steptype step = new Steptype();
        helper.registerStep(null, step);
        Assert.assertNull(helper.getAliasHelperForStepAlias("alias"));
    }
}