package nl.thedocumentwizard.wizardconfiguration.decorator;

import nl.thedocumentwizard.wizardconfiguration.jaxb.*;

/**
 * Decorator class for ArrayOfWizardCondition.Condition
 */
public class ConditionDecorator extends ArrayOfWizardCondition.Condition {

    protected ArrayOfWizardCondition.Condition decoratedCondition;

    public ConditionDecorator(ArrayOfWizardCondition.Condition condition) {
        this.decoratedCondition = condition;
    }

    public ArrayOfWizardCondition.Condition getDecoratedCondition() {
        return decoratedCondition;
    }

    public OrTrigger getOr() {
        return decoratedCondition.getOr();
    }

    public void setOr(OrTrigger value) {
        decoratedCondition.setOr(value);
    }

    public NotTrigger getNot() {
        return decoratedCondition.getNot();
    }

    public void setNot(NotTrigger value) {
        decoratedCondition.setNot(value);
    }

    public RegexTrigger getRegEx() {
        return decoratedCondition.getRegEx();
    }

    public void setRegEx(RegexTrigger value) {
        decoratedCondition.setRegEx(value);
    }

    public EmptyTrigger getEmpty() {
        return decoratedCondition.getEmpty();
    }

    public void setEmpty(EmptyTrigger value) {
        decoratedCondition.setEmpty(value);
    }

    public EqualComparisonTrigger getEqual() {
        return decoratedCondition.getEqual();
    }

    public void setEqual(EqualComparisonTrigger value) {
        decoratedCondition.setEqual(value);
    }

    public DifferentComparisonTrigger getDifferent() {
        return decoratedCondition.getDifferent();
    }

    public void setDifferent(DifferentComparisonTrigger value) {
        decoratedCondition.setDifferent(value);
    }

    public GreaterOrEqualThanComparisonTrigger getGreaterOrEqualThan() {
        return decoratedCondition.getGreaterOrEqualThan();
    }

    public void setGreaterOrEqualThan(GreaterOrEqualThanComparisonTrigger value) {
        decoratedCondition.setGreaterOrEqualThan(value);
    }

    public GreaterThanComparisonTrigger getGreaterThan() {
        return decoratedCondition.getGreaterThan();
    }

    public void setGreaterThan(GreaterThanComparisonTrigger value) {
        decoratedCondition.setGreaterThan(value);
    }

    public LessOrEqualThanComparisonTrigger getLessOrEqualThan() {
        return decoratedCondition.getLessOrEqualThan();
    }

    public void setLessOrEqualThan(LessOrEqualThanComparisonTrigger value) {
        decoratedCondition.setLessOrEqualThan(value);
    }

    public LessThanComparisonTrigger getLessThan() {
        return decoratedCondition.getLessThan();
    }

    public void setLessThan(LessThanComparisonTrigger value) {
        decoratedCondition.setLessThan(value);
    }

    public AndTrigger getAnd() {
        return decoratedCondition.getAnd();
    }

    public void setAnd(AndTrigger value) {
        decoratedCondition.setAnd(value);
    }

    public int getNextStepID() {
        return decoratedCondition.getNextStepID();
    }

    public void setNextStepID(int value) {
        decoratedCondition.setNextStepID(value);
    }

    public String getNextStepName() {
        return decoratedCondition.getNextStepName();
    }

    public void setNextStepName(String value) {
        decoratedCondition.setNextStepName(value);
    }

    public String getNextStepGroupName() {
        return decoratedCondition.getNextStepGroupName();
    }

    public void setNextStepGroupName(String value) {
        decoratedCondition.setNextStepGroupName(value);
    }
}
