package nl.thedocumentwizard.spell.parser;

import nl.thedocumentwizard.wizardconfiguration.jaxb.*;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;
import org.antlr.v4.runtime.tree.pattern.TokenTagToken;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by jose on 23/12/2016.
 */
public class WhenParserTest {

    WhenParser parser;
    @Before
    public void setUp() {
        parser = new WhenParser(new ObjectFactory(), new ParsingHelper());
    }

    @Test
    public void testParseSimpleMetadataComparison() throws Exception {
        // when $metadata is true:
        SpellParser.TestContext ctx = mock(SpellParser.TestContext.class);
        SpellParser.Or_testContext or = mock(SpellParser.Or_testContext.class); when(ctx.or_test()).thenReturn(or);

        ArrayList<SpellParser.And_testContext> and_testContextArray = new ArrayList<SpellParser.And_testContext>();
        when(or.and_test()).thenReturn(and_testContextArray);

        SpellParser.And_testContext and = mock(SpellParser.And_testContext.class); and_testContextArray.add(and);
        when(or.and_test(0)).thenReturn(and_testContextArray.get(0));

        ArrayList<SpellParser.Not_testContext> not_testContext = new ArrayList<SpellParser.Not_testContext>();
        when(and.not_test()).thenReturn(not_testContext);

        SpellParser.Not_testContext not = mock(SpellParser.Not_testContext.class); and.not_test().add(not);
        when(and.not_test(0)).thenReturn(not_testContext.get(0));

        SpellParser.ComparisonContext comparison = mock(SpellParser.ComparisonContext.class); when(not.comparison()).thenReturn(comparison);

        ArrayList<SpellParser.Comp_opContext> comp_opContext = new ArrayList<SpellParser.Comp_opContext>();
        when(comparison.comp_op()).thenReturn(comp_opContext);
        SpellParser.Comp_opContext comp_op = mock(SpellParser.Comp_opContext.class); comp_opContext.add(comp_op);
        when(comparison.comp_op(0)).thenReturn(comp_opContext.get(0));

        when(comp_op.getText()).thenReturn("");
        when(comp_op.IS()).thenReturn(new TerminalNodeImpl(new CommonToken(1, "is")));
        ArrayList<SpellParser.TermContext> termContextArray = new ArrayList<SpellParser.TermContext>();
        when(comparison.term()).thenReturn(termContextArray);
        SpellParser.TermContext term = mock(SpellParser.TermContext.class); comparison.term().add(term);
        when(comparison.term(0)).thenReturn(termContextArray.get(0));
        when(term.METADATA()).thenReturn(new TerminalNodeImpl(new CommonToken(1, "$metadata")));

        term = mock(SpellParser.TermContext.class); comparison.term().add(term);
        when(comparison.term(1)).thenReturn(termContextArray.get(1));

        SpellParser.LiteralContext literal = mock(SpellParser.LiteralContext.class); when(term.literal()).thenReturn(literal);

        SpellParser.BoolContext selected = mock(SpellParser.BoolContext.class); when(literal.bool()).thenReturn(selected);

        when(selected.TRUE()).thenReturn(new TerminalNodeImpl(new CommonToken(1, "true")));

        // It should return an EQUAL (checkbox, True)
        Trigger t = parser.parseTrigger(ctx);
        Assert.assertEquals(EqualComparisonTrigger.class, t.getClass());
        EqualComparisonTrigger equal = (EqualComparisonTrigger)t;

        Assert.assertEquals(MetadataTriggerValue.class, equal.getControlOrConstOrMetadata().get(0).getClass());
        Assert.assertEquals(ConstTriggerValue.class, equal.getControlOrConstOrMetadata().get(1).getClass());

        MetadataTriggerValue metadata = (MetadataTriggerValue) equal.getControlOrConstOrMetadata().get(0);
        ConstTriggerValue val = (ConstTriggerValue) equal.getControlOrConstOrMetadata().get(1);

        Assert.assertEquals("metadata", metadata.getName());
        Assert.assertEquals("True", val.getVal());
    }

    @Test
    public void testParseSimpleCheckbox() throws Exception {
        // when chk is selected:
        SpellParser.TestContext ctx = mock(SpellParser.TestContext.class);
        SpellParser.Or_testContext or = mock(SpellParser.Or_testContext.class); when(ctx.or_test()).thenReturn(or);

        when(or.and_test()).thenReturn(new ArrayList<SpellParser.And_testContext>());

        SpellParser.And_testContext and = mock(SpellParser.And_testContext.class); or.and_test().add(and);

        when(and.not_test()).thenReturn(new ArrayList<SpellParser.Not_testContext>());

        SpellParser.Not_testContext not = mock(SpellParser.Not_testContext.class); and.not_test().add(not);

        SpellParser.ComparisonContext comparison = mock(SpellParser.ComparisonContext.class); when(not.comparison()).thenReturn(comparison);

        when(comparison.comp_op()).thenReturn(new ArrayList<SpellParser.Comp_opContext>());
        SpellParser.Comp_opContext comp_op = mock(SpellParser.Comp_opContext.class); comparison.comp_op().add(comp_op);

        when(comp_op.getText()).thenReturn("");
        when(comp_op.IS()).thenReturn(new TerminalNodeImpl(new CommonToken(1, "is")));
        when(comparison.term()).thenReturn(new ArrayList<SpellParser.TermContext>());
        SpellParser.TermContext term = mock(SpellParser.TermContext.class); comparison.term().add(term);

        SpellParser.ControlContext ctrl = mock(SpellParser.ControlContext.class); when(term.control()).thenReturn(ctrl);

        when(ctrl.NAME()).thenReturn(new ArrayList<TerminalNode>());

        ctrl.NAME().add(new TerminalNodeImpl(new CommonToken(2, "chk")));

        term = mock(SpellParser.TermContext.class); comparison.term().add(term);
        SpellParser.LiteralContext literal = mock(SpellParser.LiteralContext.class); when(term.literal()).thenReturn(literal);

        SpellParser.BoolContext selected = mock(SpellParser.BoolContext.class); when(literal.bool()).thenReturn(selected);

        when(selected.TRUE()).thenReturn(new TerminalNodeImpl(new CommonToken(1, "selected")));

        // It should return an EQUAL (checkbox, True)

    }
    @Test
    public void testParseTrigger() throws Exception {
        // (control1 == 1 and
        SpellParser.TestContext ctx = mock(SpellParser.TestContext.class);
        SpellParser.Or_testContext or = mock(SpellParser.Or_testContext.class); when(ctx.or_test()).thenReturn(or);
        Trigger trigger = parser.parseTrigger(ctx);
    }
}