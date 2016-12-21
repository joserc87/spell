package nl.thedocumentwizard.spell.parser;

import nl.thedocumentwizard.spell.MetadataStore;
import nl.thedocumentwizard.wizardconfiguration.WizardConfiguration;
import nl.thedocumentwizard.wizardconfiguration.jaxb.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import java.awt.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    @Test
    public void testAssignMetadataIDs() {
        WizardConfiguration wizard = new WizardConfiguration(new Wizard());
        wizard.setSteps(new ArrayOfSteptype());
        wizard.getSteps().getStep().add(new Steptype());
        wizard.getSteps().getStep().add(new Steptype());
        wizard.getSteps().getStep().add(new Steptype());

        // Step 1, control 1 has metadata1
        wizard.getSteps().getStep().get(0).setQuestions(new ArrayOfWizardQuestion());
        ArrayOfWizardQuestion.Question question1_1 = new ArrayOfWizardQuestion.Question();
        wizard.getSteps().getStep().get(0).getQuestions().getQuestion().add(question1_1);

        question1_1.setString(new StringControl());
        question1_1.getString().setMetadataName("metadata1");

        // Step 2, control 1.1.1 (Radio->Multi->Label) has metadata 2 as default value
        wizard.getSteps().getStep().get(1).setQuestions(new ArrayOfWizardQuestion());
        ArrayOfWizardQuestion.Question question2_1 = new ArrayOfWizardQuestion.Question();
        wizard.getSteps().getStep().get(1).getQuestions().getQuestion().add(question2_1);

        question2_1.setRadio(new RadioControl());
        question2_1.getRadio().setItems(new ArrayOfChoice1());
        MultiControl multi = new MultiControl(); // radio -> multi
        question2_1.getRadio().getItems().getTextOrCheckboxOrAttachment().add(multi);
        multi.setControls(new ArrayOfChoice2());
        LabelControl label = new LabelControl(); // multi -> label
        multi.getControls().getTextOrNumberOrAttachment().add(label);
        label.setDefaultValueMetadataName("metadata2");

        // Step 2, control 1.2 (Radio -> list) has a list item with metadata 3 and 4 as value and display value
        ListControl list = new ListControl();
        question2_1.getRadio().getItems().getTextOrCheckboxOrAttachment().add(list);
        list.setItems(new ArrayOfListItem());
        list.getItems().getItem().add(new ListItem());
        list.getItems().getItem().get(0).setValue(new ExplicitWizardMetadata());
        list.getItems().getItem().get(0).setDisplayText(new ExplicitWizardMetadata());
        list.getItems().getItem().get(0).getValue().setMetadataName("metadata3");
        list.getItems().getItem().get(0).getDisplayText().setMetadataName("metadata4");

        // Step 3, advanced rule 1 has metadata5
        wizard.getSteps().getStep().get(2).setAdvancedRules(new ArrayOfWizardAdvancedRule());
        ArrayOfWizardAdvancedRule.AdvancedRule ar = new ArrayOfWizardAdvancedRule.AdvancedRule();
        wizard.getSteps().getStep().get(2).getAdvancedRules().getAdvancedRule().add(ar);
        ar.setMetadatas(new ArrayOfImplicitWizardMetadata());
        ar.getMetadatas().getMetadata().add(new ImplicitWizardMetadata());
        ar.getMetadatas().getMetadata().get(0).setName("metadata5");

        // Step 3, condition 1 has metadata6 in a trigger comparison
        wizard.getSteps().getStep().get(2).setConditions(new ArrayOfWizardCondition());
        ArrayOfWizardCondition.Condition condition = new ArrayOfWizardCondition.Condition();
        wizard.getSteps().getStep().get(2).getConditions().getCondition().add(condition);
        condition.setAnd(new AndTrigger());
        OrTrigger or = new OrTrigger(); condition.getAnd().getOrOrLessThanOrRegEx().add(or);
        EqualComparisonTrigger eq = new EqualComparisonTrigger(); or.getOrOrLessThanOrRegEx().add(eq);
        MetadataTriggerValue metadata = new MetadataTriggerValue();
        eq.getControlOrConstOrMetadata().add(metadata);
        metadata.setName("metadata6");

        MetadataStore metadataStore = mock(MetadataStore.class);

        when(metadataStore.findMetadataIDByName("metadata1")).thenReturn("11111");
        when(metadataStore.findMetadataIDByName("metadata2")).thenReturn("22222");
        when(metadataStore.findMetadataIDByName("metadata3")).thenReturn("33333");
        when(metadataStore.findMetadataIDByName("metadata4")).thenReturn("44444");
        when(metadataStore.findMetadataIDByName("metadata5")).thenReturn("55555");
        when(metadataStore.findMetadataIDByName("metadata6")).thenReturn("66666");

        postProcessor.assignMetadataIDs(wizard, metadataStore);

        Assert.assertEquals("11111",
                wizard.getSteps().getStep().get(0).getQuestions().getQuestion().get(0).getString().getMetadataID());
        Assert.assertEquals("22222",
                ((MultiControl) wizard.getSteps().getStep().get(1).getQuestions().getQuestion().get(0).getRadio()
                        // Radio
                        .getItems().getTextOrCheckboxOrAttachment().get(0))
                        // Multi
                        .getControls().getTextOrNumberOrAttachment().get(0).getDefaultValueMetadataID());
        Assert.assertEquals("33333",
                ((ListControl) wizard.getSteps().getStep().get(1).getQuestions().getQuestion().get(0).getRadio()
                        // Radio
                        .getItems().getTextOrCheckboxOrAttachment().get(1))
                        // List
                        .getItems().getItem().get(0).getValue().getMetadataID());
        Assert.assertEquals("44444",
                ((ListControl) wizard.getSteps().getStep().get(1).getQuestions().getQuestion().get(0).getRadio()
                        // Radio
                        .getItems().getTextOrCheckboxOrAttachment().get(1))
                        // List
                        .getItems().getItem().get(0).getDisplayText().getMetadataID());
        Assert.assertEquals("55555",
                wizard.getSteps().getStep().get(2).getAdvancedRules().getAdvancedRule().get(0)
                        //AdvancedRule
                        .getMetadatas().getMetadata().get(0).getId());
        Assert.assertEquals("66666",
                ((MetadataTriggerValue)((EqualComparisonTrigger) ((OrTrigger) wizard.getSteps().getStep().get(2).getConditions().getCondition().get(0).getAnd()
                        // And
                        .getOrOrLessThanOrRegEx().get(0))
                        // Or
                        .getOrOrLessThanOrRegEx().get(0))
                        // Equal
                        .getControlOrConstOrMetadata().get(0)).getId());

    }
}
