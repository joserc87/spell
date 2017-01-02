package nl.thedocumentwizard.wizardconfiguration;

import nl.thedocumentwizard.wizardconfiguration.jaxb.ControlTriggerValue;
import nl.thedocumentwizard.wizardconfiguration.jaxb.Steptype;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by jose on 02/01/2017.
 */
public class MyObjectFactoryTest {

    MyObjectFactory factory;
    @Before
    public void setUp() throws Exception {
        factory = new MyObjectFactory();
    }

    @Test
    public void testCreateWizard() throws Exception {
        Assert.assertEquals(WizardConfiguration.class, factory.createWizard().getClass());
    }

    @Test
    public void testCreateArrayOfWizardConditionCondition() throws Exception {
        Assert.assertEquals(Condition.class, factory.createArrayOfWizardConditionCondition().getClass());
    }

    @Test
    public void testCreateSteptype() throws Exception {
        Assert.assertEquals(Step.class, factory.createSteptype().getClass());
    }

    @Test
    public void testCreateControlTriggerValue() throws Exception {
        Assert.assertEquals(ControlValue.class, factory.createControlTriggerValue().getClass());
    }
}