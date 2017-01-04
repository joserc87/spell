package nl.thedocumentwizard.wizardconfiguration;

import nl.thedocumentwizard.wizardconfiguration.jaxb.*;

/**
 * Created by jose on 05/12/2016.
 */
public class MyObjectFactory extends ObjectFactory {
    public MyObjectFactory() {
        super();
    }

    @Override
    public ArrayOfWizardCondition createArrayOfWizardCondition() {
        return super.createArrayOfWizardCondition();
    }

    @Override
    public ArrayOfWizardAdvancedRule createArrayOfWizardAdvancedRule() {
        return super.createArrayOfWizardAdvancedRule();
    }

    @Override
    public ArrayOfWizardQuestion createArrayOfWizardQuestion() {
        return super.createArrayOfWizardQuestion();
    }

    @Override
    public Wizard createWizard() {
        return new WizardConfiguration();
    }

    @Override
    public ArrayOfSteptype createArrayOfSteptype() {
        return super.createArrayOfSteptype();
    }

    @Override
    public ImplicitWizardMetadata createImplicitWizardMetadata() {
        return super.createImplicitWizardMetadata();
    }

    @Override
    public ListControl createListControl() {
        return super.createListControl();
    }

    @Override
    public StringControl createStringControl() {
        return super.createStringControl();
    }

    @Override
    public AndTrigger createAndTrigger() {
        return super.createAndTrigger();
    }

    @Override
    public ArrayOfImplicitWizardMetadata createArrayOfImplicitWizardMetadata() {
        return super.createArrayOfImplicitWizardMetadata();
    }

    @Override
    public XPathDataSource createXPathDataSource() {
        return super.createXPathDataSource();
    }

    @Override
    public EmptyTrigger createEmptyTrigger() {
        return super.createEmptyTrigger();
    }

    @Override
    public LabelControl createLabelControl() {
        return super.createLabelControl();
    }

    @Override
    public NotTrigger createNotTrigger() {
        return super.createNotTrigger();
    }

    @Override
    public GreaterOrEqualThanComparisonTrigger createGreaterOrEqualThanComparisonTrigger() {
        return super.createGreaterOrEqualThanComparisonTrigger();
    }

    @Override
    public ImageFileControl createImageFileControl() {
        return super.createImageFileControl();
    }

    @Override
    public Steptype createSteptype() {
        return new Step();
    }

    @Override
    public CheckboxControl createCheckboxControl() {
        return super.createCheckboxControl();
    }

    @Override
    public ExplicitWizardMetadata createExplicitWizardMetadata() {
        return super.createExplicitWizardMetadata();
    }

    @Override
    public MultiControl createMultiControl() {
        return super.createMultiControl();
    }

    @Override
    public LessThanComparisonTrigger createLessThanComparisonTrigger() {
        return super.createLessThanComparisonTrigger();
    }

    @Override
    public TextControl createTextControl() {
        return super.createTextControl();
    }

    @Override
    public MetadataTriggerValue createMetadataTriggerValue() {
        return super.createMetadataTriggerValue();
    }

    @Override
    public OrTrigger createOrTrigger() {
        return super.createOrTrigger();
    }

    @Override
    public ControlTriggerValue createControlTriggerValue() {
        return new ControlValue();
    }

    @Override
    public ArrayOfListItem createArrayOfListItem() {
        return super.createArrayOfListItem();
    }

    @Override
    public EmailControl createEmailControl() {
        return super.createEmailControl();
    }

    @Override
    public FileNameControl createFileNameControl() {
        return super.createFileNameControl();
    }

    @Override
    public RegexTrigger createRegexTrigger() {
        return super.createRegexTrigger();
    }

    @Override
    public GreaterThanComparisonTrigger createGreaterThanComparisonTrigger() {
        return super.createGreaterThanComparisonTrigger();
    }

    @Override
    public ConstTriggerValue createConstTriggerValue() {
        return super.createConstTriggerValue();
    }

    @Override
    public ArrayOfChoice2 createArrayOfChoice2() {
        return super.createArrayOfChoice2();
    }

    @Override
    public ArrayOfChoice1 createArrayOfChoice1() {
        return super.createArrayOfChoice1();
    }

    @Override
    public RadioControl createRadioControl() {
        return super.createRadioControl();
    }

    @Override
    public AttachmentFileControl createAttachmentFileControl() {
        return super.createAttachmentFileControl();
    }

    @Override
    public FileControl createFileControl() {
        return super.createFileControl();
    }

    @Override
    public LessOrEqualThanComparisonTrigger createLessOrEqualThanComparisonTrigger() {
        return super.createLessOrEqualThanComparisonTrigger();
    }

    @Override
    public DateControl createDateControl() {
        return super.createDateControl();
    }

    @Override
    public EqualComparisonTrigger createEqualComparisonTrigger() {
        return super.createEqualComparisonTrigger();
    }

    @Override
    public DifferentComparisonTrigger createDifferentComparisonTrigger() {
        return super.createDifferentComparisonTrigger();
    }

    @Override
    public ListItem createListItem() {
        return super.createListItem();
    }

    @Override
    public NumberControl createNumberControl() {
        return super.createNumberControl();
    }

    @Override
    public ArrayOfWizardCondition.Condition createArrayOfWizardConditionCondition() {
        return new Condition();
    }

    @Override
    public ArrayOfWizardAdvancedRule.AdvancedRule createArrayOfWizardAdvancedRuleAdvancedRule() {
        return super.createArrayOfWizardAdvancedRuleAdvancedRule();
    }

    @Override
    public ArrayOfWizardQuestion.Question createArrayOfWizardQuestionQuestion() {
        return super.createArrayOfWizardQuestionQuestion();
    }
}
