package nl.thedocumentwizard.spell.parser;

import nl.thedocumentwizard.wizardconfiguration.ControlValue;
import nl.thedocumentwizard.wizardconfiguration.jaxb.*;

/**
 * Created by jose on 23/12/2016.
 */
public class WhenParser {
    private ObjectFactory objectFactory;
    private ParsingHelper helper;

    public WhenParser(ObjectFactory factory, ParsingHelper helper) {
        this.objectFactory = factory;
        this.helper = helper;
    }

    Trigger parseTrigger(SpellParser.TestContext test) {
        if (test != null) {
            return this.parseOrTest(test.or_test());
        } else {
            return null;
        }
    }

    private Trigger parseOrTest(SpellParser.Or_testContext ctx) {
        if (ctx != null && ctx.and_test() != null && ctx.and_test().size() > 0) {
            if (ctx.and_test().size() == 1) {
                // If there is only one, it's not really an OR
                return this.parseAndTest(ctx.and_test(0));
            } else if (ctx.and_test().size() > 1) {
                // If there are multiple, get all of them and return in an OR
                OrTrigger trigger = objectFactory.createOrTrigger();
                for(SpellParser.And_testContext and : ctx.and_test()) {
                    trigger.getOrOrLessThanOrRegEx().add(this.parseAndTest(and));
                }
                return trigger;
            }
        }
        return null;
    }

    private Trigger parseAndTest(SpellParser.And_testContext ctx) {
        if (ctx != null && ctx.not_test() != null && ctx.not_test().size() > 0) {
            if (ctx.not_test().size() == 1) {
                // If there is only one, it's not really an OR
                return this.parseNotTest(ctx.not_test(0));
            } else if (ctx.not_test().size() > 1) {
                // If there are multiple, get all of them and return in an AND
                OrTrigger trigger = objectFactory.createOrTrigger();
                for(SpellParser.Not_testContext not : ctx.not_test()) {
                    trigger.getOrOrLessThanOrRegEx().add(this.parseNotTest(not));
                }
                return trigger;
            }
        }
        return null;
    }

    private Trigger parseNotTest(SpellParser.Not_testContext ctx) {
        if (ctx != null) {
            // not_test
            // : NOT not_test
            // | '(' test ')'
            // | comparison
            // When not_test, negate
            if (ctx.not_test() != null) {
                NotTrigger trigger = objectFactory.createNotTrigger();
                trigger.getOrOrLessThanOrRegEx().add(this.parseNotTest(ctx.not_test()));
                return trigger;
            } else if (ctx.test() != null) {
                return parseTrigger(ctx.test());
            } else if (ctx.comparison() != null) {
                return parseComparison(ctx.comparison());
            }
        }
        return null;
    }

    private Trigger parseComparison(SpellParser.ComparisonContext comparison) {
        // There are 3 cases:
        // 1. There is only one term.
        //   1.1 If it's a literal, it has to be a boolean. Otherwise, return error.
        //   1.2 If it's a metadata, compare with "True"
        //   1.3 If it's a control, it has to be a checkbox. Otherwise, return error.
        // 2. There are 2 terms. Just return a simple comparison.
        // 3. There are multiple terms. Then there is an implicit AND (a == b < c -> a == b AND b < c)
        if (comparison != null && comparison.term() != null && comparison.term().size() > 0) {
            if (comparison.term().size() == 1) {
                SpellParser.TermContext term = comparison.term().get(0);
                if (term.literal() != null) {
                    if (term.literal().bool() != null) {
                        return this.parseBooleanLiteral(term.literal().bool());
                    } else if (term.literal().NUM() != null){
                        this.logError("Expected boolean expression but found numeric value '" + term.literal().NUM().getText() + "'");
                    } else if (term.literal().STRING_LITERAL() != null){
                        this.logError("Expected boolean expression but found string value " + term.literal().STRING_LITERAL().getText());
                    }
                } else if (term.METADATA() != null) {
                    // Return metadata == "True"
                    MetadataTriggerValue metadata = objectFactory.createMetadataTriggerValue();
                    metadata.setName(this.helper.getMetadataName(term.METADATA()));
                    ConstTriggerValue constVal = objectFactory.createConstTriggerValue();
                    constVal.setVal("True");

                    EqualComparisonTrigger equal = objectFactory.createEqualComparisonTrigger();
                    equal.getControlOrConstOrMetadata().add(metadata);
                    equal.getControlOrConstOrMetadata().add(constVal);
                    return equal;
                } else if (term.control() != null) {
                    // TODO:
                    // - Get the control
                    // - Check that the control is a checkbox.
                    // - Return checkbox == "True". Otherwise, error
                    ControlValue control = (ControlValue) objectFactory.createControlTriggerValue();
                    if (term.control().NAME().size() > 1) {
                        control.setStepAlias(term.control().NAME(0).getText());
                        control.setControlAlias(term.control().NAME(1).getText());
                    } else {
                        control.setControlAlias(term.control().NAME(0).getText());
                    }

                    ConstTriggerValue constVal = objectFactory.createConstTriggerValue();
                    constVal.setVal("True");

                    EqualComparisonTrigger equal = objectFactory.createEqualComparisonTrigger();
                    equal.getControlOrConstOrMetadata().add(control);
                    equal.getControlOrConstOrMetadata().add(constVal);
                    return equal;
                }
            } if (comparison.term().size() == 2) {
                return this.getComparison(comparison.term(0), comparison.term(1), comparison.comp_op(0));
            } else { // > 2
                AndTrigger and = objectFactory.createAndTrigger();
                for (int i = 0; i < comparison.term().size() - 1; i++) {
                    and.getOrOrLessThanOrRegEx().add(this.getComparison(
                            comparison.term(i),
                            comparison.term(i + 1),
                            comparison.comp_op(i)
                    ));
                }
                return and;
            }
        } else {
            return null;
        }
    }

    private Trigger getComparison(SpellParser.TermContext term1, SpellParser.TermContext term2, SpellParser.Comp_opContext op) {
        // comp_op
        // : '<'
        // | '>'
        // | '=='
        // | '>='
        // | '<='
        // | '<>'
        // | '!='
        // | IN
        // | NOT IN
        // | IS
        // | IS NOT
        // ;

        TriggerValue v1 = this.parseTermContext(term1);
        TriggerValue v2 = this.parseTermContext(term2);

        if (op.getText().equals("<")) {
            LessThanComparisonTrigger trigger = objectFactory.createLessThanComparisonTrigger();
            trigger.getControlOrConstOrMetadata().add(v1);
            trigger.getControlOrConstOrMetadata().add(v2);
            return trigger;
        } else if (op.getText().equals(">")) {
            GreaterThanComparisonTrigger trigger = objectFactory.createGreaterThanComparisonTrigger();
            trigger.getControlOrConstOrMetadata().add(v1);
            trigger.getControlOrConstOrMetadata().add(v2);
            return trigger;
        } else if (op.getText().equals("==")) {
            EqualComparisonTrigger trigger = objectFactory.createEqualComparisonTrigger();
            trigger.getControlOrConstOrMetadata().add(v1);
            trigger.getControlOrConstOrMetadata().add(v2);
            return trigger;
        } else if (op.getText().equals(">=")) {
            GreaterOrEqualThanComparisonTrigger trigger = objectFactory.createGreaterOrEqualThanComparisonTrigger();
            trigger.getControlOrConstOrMetadata().add(v1);
            trigger.getControlOrConstOrMetadata().add(v2);
            return trigger;
        } else if (op.getText().equals("<=")) {
            LessOrEqualThanComparisonTrigger trigger = objectFactory.createLessOrEqualThanComparisonTrigger();
            trigger.getControlOrConstOrMetadata().add(v1);
            trigger.getControlOrConstOrMetadata().add(v2);
            return trigger;
        } else if (op.getText().equals("<>")) {
            DifferentComparisonTrigger trigger = objectFactory.createDifferentComparisonTrigger();
            trigger.getControlOrConstOrMetadata().add(v1);
            trigger.getControlOrConstOrMetadata().add(v2);
            return trigger;
        } else if (op.getText().equals("!=")) {
            DifferentComparisonTrigger trigger = objectFactory.createDifferentComparisonTrigger();
            trigger.getControlOrConstOrMetadata().add(v1);
            trigger.getControlOrConstOrMetadata().add(v2);
            return trigger;
        } else if (op.IS() != null) {
            if (op.NOT() == null) {
                // IS
                EqualComparisonTrigger trigger = objectFactory.createEqualComparisonTrigger();
                trigger.getControlOrConstOrMetadata().add(v1);
                trigger.getControlOrConstOrMetadata().add(v2);
                return trigger;
            } else {
                // IS NOT
                DifferentComparisonTrigger trigger = objectFactory.createDifferentComparisonTrigger();
                trigger.getControlOrConstOrMetadata().add(v1);
                trigger.getControlOrConstOrMetadata().add(v2);
                return trigger;
            }
        }
        return null;
    }

    private TriggerValue parseTermContext(SpellParser.TermContext term) {
        if (term.literal() != null) {
            if (term.literal().bool() != null) {
                if (term.literal().bool().TRUE() != null) {
                    ConstTriggerValue val = objectFactory.createConstTriggerValue();
                    val.setVal("True");
                    return val;
                } else if (term.literal().bool().TRUE() != null) {
                    ConstTriggerValue val = objectFactory.createConstTriggerValue();
                    val.setVal("False");
                    return val;
                }
            } else if (term.literal().NUM() != null) {
                ConstTriggerValue val = objectFactory.createConstTriggerValue();
                val.setVal(term.literal().NUM().getText());
                return val;
            } else if (term.literal().STRING_LITERAL() != null) {
                ConstTriggerValue val = objectFactory.createConstTriggerValue();
                val.setVal(this.helper.getString(term.literal().STRING_LITERAL().getText()));
                return val;
            }
        } else if (term.METADATA() != null) {
            MetadataTriggerValue metadata = objectFactory.createMetadataTriggerValue();
            metadata.setName(this.helper.getMetadataName(term.METADATA().getText()));
            return metadata;
        } else if (term.control() != null) {
            ControlValue control = (ControlValue) objectFactory.createControlTriggerValue();
            if (term.control().NAME().size() > 1) {
                control.setStepAlias(term.control().NAME(0).getText());
                control.setControlAlias(term.control().NAME(1).getText());
            } else {
                control.setControlAlias(term.control().NAME(0).getText());
            }
            return control;
        }
        return null;
    }

    private Trigger parseBooleanLiteral(SpellParser.BoolContext bool) {
        if (bool.TRUE() != null) {
            if (bool.TRUE().getText().equals("true")) {
                return this.getBooleanTrigger(true);
            } else {
                this.logError("Value '" + bool.TRUE().getText() + "' not allowed here");
                return null;
            }
        } else if (bool.FALSE() != null) {
            if (bool.FALSE().getText().equals("false")) {
                return this.getBooleanTrigger(false);
            } else {
                this.logError("Value '" + bool.FALSE().getText() + "' not allowed here");
                return null;
            }
        }
        return null;
    }

    private Trigger getBooleanTrigger(boolean b) {
        ConstTriggerValue val1 = objectFactory.createConstTriggerValue();
        val1.setVal("a");
        ConstTriggerValue val2 = objectFactory.createConstTriggerValue();
        val2.setVal(b ? "a" : "b");
        EqualComparisonTrigger eq = objectFactory.createEqualComparisonTrigger();
        eq.getControlOrConstOrMetadata().add(val1);
        eq.getControlOrConstOrMetadata().add(val2);
        return eq;
    }

    private void logError(String s) {
        System.err.println(s);
    }
}
