package nl.thedocumentwizard.spell.parser;

import nl.thedocumentwizard.wizardconfiguration.WizardConfiguration;
import nl.thedocumentwizard.wizardconfiguration.jaxb.ArrayOfSteptype;
import nl.thedocumentwizard.wizardconfiguration.jaxb.Steptype;
import nl.thedocumentwizard.wizardconfiguration.jaxb.Wizard;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by jose on 21/12/2016.
 */
public class PostProcessorTest {

    PostProcessor postProcessor;

    @Before
    public void becauseThereIsAPostProcessor() {
        postProcessor = new PostProcessor();
    }

    @Test
    public void testAssignStepIDs() throws Exception {
        WizardConfiguration wizard = new WizardConfiguration(new Wizard());
        wizard.setSteps(new ArrayOfSteptype());
        wizard.getSteps().getStep().add(new Steptype());
        wizard.getSteps().getStep().add(new Steptype());
        wizard.getSteps().getStep().add(new Steptype());
        wizard.getSteps().getStep().add(new Steptype());

        postProcessor.assignStepIDs(wizard);

        Assert.assertEquals(1, wizard.getSteps().getStep().get(0).getId());
        Assert.assertEquals(2, wizard.getSteps().getStep().get(1).getId());
        Assert.assertEquals(3, wizard.getSteps().getStep().get(2).getId());
        Assert.assertEquals(4, wizard.getSteps().getStep().get(3).getId());
    }
}