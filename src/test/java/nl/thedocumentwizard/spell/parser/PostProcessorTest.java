package nl.thedocumentwizard.spell.parser;

import nl.thedocumentwizard.spell.MetadataStore;
import nl.thedocumentwizard.wizardconfiguration.*;
import nl.thedocumentwizard.wizardconfiguration.jaxb.*;
import nl.thedocumentwizard.wizardconfiguration.jaxb.RadioControl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by jose on 21/12/2016.
 */
public class PostProcessorTest {

    MetadataStore metadataStore;
    PostProcessor postProcessor;

    @Before
    public void becauseThereIsAPostProcessor() {
        metadataStore = mock(MetadataStore.class);
        postProcessor = new PostProcessor(metadataStore);
    }

    @Test
    public void testAssignStepIDs() throws Exception {
        WizardConfiguration wizard = new WizardConfiguration();
        wizard.setSteps(new ArrayOfSteptype());
        wizard.getSteps().getStep().add(new Steptype());
        wizard.getSteps().getStep().add(new Steptype());
        wizard.getSteps().getStep().add(new Steptype());
        wizard.getSteps().getStep().add(new Steptype());

        postProcessor.assignStepIDs(wizard);

        // IDs should be assigned in sequential order
        Assert.assertEquals(1, wizard.getSteps().getStep().get(0).getId());
        Assert.assertEquals(2, wizard.getSteps().getStep().get(1).getId());
        Assert.assertEquals(3, wizard.getSteps().getStep().get(2).getId());
        Assert.assertEquals(4, wizard.getSteps().getStep().get(3).getId());

        // First step should have type START and last step type FINISH. The
        // rest should not have a type.
        Assert.assertEquals("START", wizard.getSteps().getStep().get(0).getType());
        Assert.assertEquals("FINISH", wizard.getSteps().getStep().get(3).getType());
        Assert.assertEquals(null, wizard.getSteps().getStep().get(1).getType());
        Assert.assertEquals(null, wizard.getSteps().getStep().get(2).getType());
    }

    @Test
    public void testAssignQuestionIDs() throws Exception {
        WizardConfiguration wizard = new WizardConfiguration();
        wizard.setSteps(new ArrayOfSteptype());
        wizard.getSteps().getStep().add(new Steptype());
        wizard.getSteps().getStep().add(new Steptype());

        // Step 1 has 3 questions
        wizard.getSteps().getStep().get(0).setQuestions(new ArrayOfWizardQuestion());
        wizard.getSteps().getStep().get(0).getQuestions().getQuestion().add(new ArrayOfWizardQuestion.Question());
        wizard.getSteps().getStep().get(0).getQuestions().getQuestion().add(new ArrayOfWizardQuestion.Question());
        wizard.getSteps().getStep().get(0).getQuestions().getQuestion().add(new ArrayOfWizardQuestion.Question());

        wizard.getSteps().getStep().get(0).getQuestions().getQuestion().get(0).setString(new StringControl());
        // control 2 will have an alias, so the ID should be different
        wizard.getSteps().getStep().get(0).getQuestions().getQuestion().get(1).setLabel(new LabelControl());
        wizard.getSteps().getStep().get(0).getQuestions().getQuestion().get(1).getLabel().setId("aliasLabel");
        wizard.getSteps().getStep().get(0).getQuestions().getQuestion().get(2).setRadio(new RadioControl());

        // Control 3 is a radio { label, multi { label, string } }
        RadioControl radio = wizard.getSteps().getStep().get(0).getQuestions().getQuestion().get(2).getRadio();
        radio.setItems(new ArrayOfChoice1());
        radio.getItems().getTextOrCheckboxOrAttachment().add(new LabelControl());
        MultiControl multi = new MultiControl();
        radio.getItems().getTextOrCheckboxOrAttachment().add(multi);
        multi.setControls(new ArrayOfChoice2());
        multi.getControls().getTextOrNumberOrAttachment().add(new LabelControl());
        multi.getControls().getTextOrNumberOrAttachment().add(new StringControl());

        // Step 2 has only 1 question
        wizard.getSteps().getStep().get(1).setQuestions(new ArrayOfWizardQuestion());
        wizard.getSteps().getStep().get(1).getQuestions().getQuestion().add(new ArrayOfWizardQuestion.Question());
        wizard.getSteps().getStep().get(1).getQuestions().getQuestion().get(0).setImage(new ImageFileControl());
        wizard.getSteps().getStep().get(1).getQuestions().getQuestion().get(0).getImage().setFileName(new FileNameControl());

        postProcessor.assignQuestionIDs(wizard);

        // Question IDs
        List<ArrayOfWizardQuestion.Question> s1Questions = wizard.getSteps().getStep().get(0).getQuestions().getQuestion();
        List<ArrayOfWizardQuestion.Question> s2Questions = wizard.getSteps().getStep().get(1).getQuestions().getQuestion();
        Assert.assertEquals("QUESTION_1", s1Questions.get(0).getId());
        Assert.assertEquals("QUESTION_2", s1Questions.get(1).getId());
        Assert.assertEquals("QUESTION_3", s1Questions.get(2).getId());
        Assert.assertEquals("QUESTION_1", s2Questions.get(0).getId());

        // Control IDs
        Assert.assertEquals("CONTROL_1", s1Questions.get(0).getString().getId());
        Assert.assertEquals("aliasLabel", s1Questions.get(1).getLabel().getId());
        Assert.assertEquals("CONTROL_3", s1Questions.get(2).getRadio().getId());
        Assert.assertEquals("CONTROL_3_1", radio.getItems().getTextOrCheckboxOrAttachment().get(0).getId());
        Assert.assertEquals("CONTROL_3_2", radio.getItems().getTextOrCheckboxOrAttachment().get(1).getId());
        Assert.assertEquals("CONTROL_3_2_1", multi.getControls().getTextOrNumberOrAttachment().get(0).getId());
        Assert.assertEquals("CONTROL_3_2_2", multi.getControls().getTextOrNumberOrAttachment().get(1).getId());
        Assert.assertEquals("CONTROL_1", s2Questions.get(0).getImage().getId());
        Assert.assertEquals("CONTROL_1_1", s2Questions.get(0).getImage().getFileName().getId());
    }

    @Test
    public void testAssignMetadataIDs() {
        WizardConfiguration wizard = new WizardConfiguration();
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

        when(metadataStore.findMetadataIDByName("metadata1")).thenReturn("11111");
        when(metadataStore.findMetadataIDByName("metadata2")).thenReturn("22222");
        when(metadataStore.findMetadataIDByName("metadata3")).thenReturn("33333");
        when(metadataStore.findMetadataIDByName("metadata4")).thenReturn("44444");
        when(metadataStore.findMetadataIDByName("metadata5")).thenReturn("55555");
        when(metadataStore.findMetadataIDByName("metadata6")).thenReturn("66666");

        postProcessor.assignMetadataIDs(wizard);

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

    @Test
    public void resolveAlias_should_translate_default_next_step() throws Exception {
        // The wizard
        WizardConfiguration wizard = new WizardConfiguration();
        wizard.setSteps(new ArrayOfSteptype());
        wizard.getSteps().getStep().add(new Step());
        wizard.getSteps().getStep().add(new Step());

        // Step 0: goto stepAlias
        ((Step)wizard.getSteps().getStep().get(0)).setNextStepAlias("stepAlias");
        // Step 1 with id 123
        wizard.getSteps().getStep().get(1).setId(123);

        // The alias helper:
        StepAliasHelper stepAliasHelper = mock(StepAliasHelper.class);
        ControlAliasHelper referencedAliasHelper = mock(ControlAliasHelper.class);

        when(stepAliasHelper.getAliasHelperForStepAlias("stepAlias")).thenReturn(referencedAliasHelper);
        when(referencedAliasHelper.getStep()).thenReturn(wizard.getSteps().getStep().get(1));

        // translate
        postProcessor.resolveAlias(wizard, stepAliasHelper);

        // The next step should have been translated
        Assert.assertEquals(123, (int)wizard.getSteps().getStep().get(0).getNextStepID());
    }

    @Test
    public void resolveAlias_should_leave_default_next_step_to_null_by_default() throws Exception {
        // The wizard
        WizardConfiguration wizard = new WizardConfiguration();
        wizard.setSteps(new ArrayOfSteptype());
        wizard.getSteps().getStep().add(new Step());
        wizard.getSteps().getStep().add(new Step());

        // Step 0: goto stepAlias
        ((Step)wizard.getSteps().getStep().get(0)).setNextStepAlias("stepAlias");
        // Step 1 with id 123
        wizard.getSteps().getStep().get(1).setId(123);

        // The alias helper:
        StepAliasHelper stepAliasHelper = mock(StepAliasHelper.class);
        ControlAliasHelper referencedAliasHelper = mock(ControlAliasHelper.class);

        when(stepAliasHelper.getAliasHelperForStepAlias("stepAlias")).thenReturn(referencedAliasHelper);
        when(referencedAliasHelper.getStep()).thenReturn(wizard.getSteps().getStep().get(1));

        // translate
        postProcessor.resolveAlias(wizard, stepAliasHelper);

        // The next step should have been translated
        Assert.assertEquals(null, wizard.getSteps().getStep().get(1).getNextStepID());
    }

    @Test
    public void resolveAlias_should_translate_condition_next_step() throws Exception {
        // The wizard
        WizardConfiguration wizard = new WizardConfiguration();
        wizard.setSteps(new ArrayOfSteptype());
        wizard.getSteps().getStep().add(new Step());
        wizard.getSteps().getStep().add(new Step());

        // Step 0: when X goto stepAlias
        Condition condition = new Condition();
        wizard.getSteps().getStep().get(0).setConditions(new ArrayOfWizardCondition());
        wizard.getSteps().getStep().get(0).getConditions().getCondition().add(condition);
        condition.setNextStepAlias("stepAlias");
        // Step 1 with id 123 (as stepAlias)
        wizard.getSteps().getStep().get(1).setId(123);

        // The alias helper:
        StepAliasHelper stepAliasHelper = mock(StepAliasHelper.class);
        ControlAliasHelper referencedAliasHelper = mock(ControlAliasHelper.class);

        when(stepAliasHelper.getAliasHelperForStepAlias("stepAlias")).thenReturn(referencedAliasHelper);
        when(referencedAliasHelper.getStep()).thenReturn(wizard.getSteps().getStep().get(1));

        // translate
        postProcessor.resolveAlias(wizard, stepAliasHelper);

        // The next step of the condition should have been translated
        Assert.assertEquals(123, wizard.getSteps().getStep().get(0).getConditions().getCondition().get(0).getNextStepID());
    }

    @Test
    public void resolveAlias_should_translate_control_trigger_value() throws Exception {
        // The wizard
        WizardConfiguration wizard = new WizardConfiguration();
        wizard.setSteps(new ArrayOfSteptype());
        wizard.getSteps().getStep().add(new Step());
        wizard.getSteps().getStep().add(new Step());
        wizard.getSteps().getStep().get(0).setId(123);
        wizard.getSteps().getStep().get(1).setId(456);

        // Step 1, String as controlAlias1:
        wizard.getSteps().getStep().get(0).setQuestions(new ArrayOfWizardQuestion());
        wizard.getSteps().getStep().get(0).getQuestions().getQuestion().add(new ArrayOfWizardQuestion.Question());
        wizard.getSteps().getStep().get(0).getQuestions().getQuestion().get(0).setString(new StringControl());
        wizard.getSteps().getStep().get(0).getQuestions().getQuestion().get(0).getString().setId("CONTROL_123");
        // Step 2, Label as controlAlias2
        wizard.getSteps().getStep().get(1).setQuestions(new ArrayOfWizardQuestion());
        wizard.getSteps().getStep().get(1).getQuestions().getQuestion().add(new ArrayOfWizardQuestion.Question());
        wizard.getSteps().getStep().get(1).getQuestions().getQuestion().get(0).setLabel(new LabelControl());
        wizard.getSteps().getStep().get(1).getQuestions().getQuestion().get(0).getLabel().setId("CONTROL_456");

        // when ControlAlias ...
        Condition condition = new Condition();
        wizard.getSteps().getStep().get(0).setConditions(new ArrayOfWizardCondition());
        wizard.getSteps().getStep().get(0).getConditions().getCondition().add(condition);
        condition.setEqual(new EqualComparisonTrigger());
        ControlValue controlValue1 = new ControlValue();
        condition.getEqual().getControlOrConstOrMetadata().add(controlValue1);
        controlValue1.setControlAlias("controlAlias");

        // when StepAlias.ControlAlias ...
        Condition condition2 = new Condition();
        wizard.getSteps().getStep().get(0).getConditions().getCondition().add(condition2);
        condition2.setEqual(new EqualComparisonTrigger());
        ControlValue controlValue2 = new ControlValue();
        condition2.getEqual().getControlOrConstOrMetadata().add(controlValue2);
        controlValue2.setControlAlias("controlAlias");
        controlValue2.setStepAlias("stepAlias");

        // Step 1 with id 123 (as stepAlias)
        wizard.getSteps().getStep().get(1).setId(123);

        // The alias helper:
        StepAliasHelper stepAliasHelper = mock(StepAliasHelper.class);
        ControlAliasHelper aliasHelperStep1 = mock(ControlAliasHelper.class);
        ControlAliasHelper aliasHelperStep2 = mock(ControlAliasHelper.class);

        // step 1 without alias
        when(stepAliasHelper.getAliasHelperForStep(wizard.getSteps().getStep().get(0))).thenReturn(aliasHelperStep1);
        // step 2 as stepAlias
        when(stepAliasHelper.getAliasHelperForStepAlias("stepAlias")).thenReturn(aliasHelperStep2);

        // Step 1, controlAlias
        when(aliasHelperStep1.findControl("controlAlias")).thenReturn(wizard.getSteps().getStep().get(0).getQuestions().getQuestion().get(0).getString());
        // Step 2, controlAlias
        when(aliasHelperStep2.getStep()).thenReturn(wizard.getSteps().getStep().get(0));
        when(aliasHelperStep2.findControl("controlAlias")).thenReturn(wizard.getSteps().getStep().get(1).getQuestions().getQuestion().get(0).getLabel());

        // translate
        postProcessor.resolveAlias(wizard, stepAliasHelper);

        // The next step of the condition should have been translated
        Assert.assertNull(((ControlTriggerValue) wizard.getSteps().getStep().get(0).getConditions().getCondition().get(0).getEqual().getControlOrConstOrMetadata().get(0)).getStep());
        Assert.assertEquals("CONTROL_123",((ControlTriggerValue) wizard.getSteps().getStep().get(0).getConditions().getCondition().get(0).getEqual().getControlOrConstOrMetadata().get(0)).getId());
        Assert.assertEquals(123, (int) ((ControlTriggerValue) wizard.getSteps().getStep().get(0).getConditions().getCondition().get(1).getEqual().getControlOrConstOrMetadata().get(0)).getStep());
        Assert.assertEquals("CONTROL_456",((ControlTriggerValue) wizard.getSteps().getStep().get(0).getConditions().getCondition().get(1).getEqual().getControlOrConstOrMetadata().get(0)).getId());
    }

    @Test
    public void resolveAlias_should_translate_control_default_value() throws Exception {
        // The wizard
        WizardConfiguration wizard = new WizardConfiguration();
        wizard.setSteps(new ArrayOfSteptype());
        wizard.getSteps().getStep().add(new Step());

        // Step 1, String as controlAlias1:
        wizard.getSteps().getStep().get(0).setQuestions(new ArrayOfWizardQuestion());
        // Question 1: Radio
        wizard.getSteps().getStep().get(0).getQuestions().getQuestion().add(new ArrayOfWizardQuestion.Question());
        wizard.getSteps().getStep().get(0).getQuestions().getQuestion().get(0).setRadio(new nl.thedocumentwizard.wizardconfiguration.RadioControl());
        // Question 2: Multi { Radio }
        wizard.getSteps().getStep().get(0).getQuestions().getQuestion().add(new ArrayOfWizardQuestion.Question());
        wizard.getSteps().getStep().get(0).getQuestions().getQuestion().get(1).setMulti(new MultiControl());
        wizard.getSteps().getStep().get(0).getQuestions().getQuestion().get(1).getMulti().setControls(new ArrayOfChoice2());
        wizard.getSteps().getStep().get(0).getQuestions().getQuestion().get(1).getMulti().getControls().getTextOrNumberOrAttachment().add(new nl.thedocumentwizard.wizardconfiguration.RadioControl());

        // Question 3: String as strControl
        wizard.getSteps().getStep().get(0).getQuestions().getQuestion().add(new ArrayOfWizardQuestion.Question());
        wizard.getSteps().getStep().get(0).getQuestions().getQuestion().get(2).setString(new StringControl());

        // radio "" = strControl
        ((nl.thedocumentwizard.wizardconfiguration.RadioControl) wizard.getSteps().getStep().get(0).getQuestions().getQuestion().get(0).getRadio()).setAliasDefaultValue("strControl");
        ((nl.thedocumentwizard.wizardconfiguration.RadioControl) wizard.getSteps().getStep().get(0).getQuestions().getQuestion().get(1).getMulti().getControls().getTextOrNumberOrAttachment().get(0)).setAliasDefaultValue("strControl");
        // string "" as strControl
        wizard.getSteps().getStep().get(0).getQuestions().getQuestion().get(2).getString().setId("CONTROL_123");

        // The alias helper:
        StepAliasHelper stepAliasHelper = mock(StepAliasHelper.class);
        ControlAliasHelper currentStepAliasHelper = mock(ControlAliasHelper.class);

        when(stepAliasHelper.getAliasHelperForStep(wizard.getSteps().getStep().get(0))).thenReturn(currentStepAliasHelper);
        when(currentStepAliasHelper.findControl("strControl")).thenReturn(wizard.getSteps().getStep().get(0).getQuestions().getQuestion().get(2).getString());

        // translate
        postProcessor.resolveAlias(wizard, stepAliasHelper);

        // The default value should have been translated
        Assert.assertEquals("CONTROL_123", wizard.getSteps().getStep().get(0).getQuestions().getQuestion().get(0).getRadio().getDefaultValue());
        Assert.assertEquals("CONTROL_123", wizard.getSteps().getStep().get(0).getQuestions().getQuestion().get(1).getMulti().getControls().getTextOrNumberOrAttachment().get(0).getDefaultValue());
    }
}
