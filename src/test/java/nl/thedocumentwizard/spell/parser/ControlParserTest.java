package nl.thedocumentwizard.spell.parser;

import jdk.nashorn.internal.ir.Terminal;
import nl.thedocumentwizard.wizardconfiguration.jaxb.ObjectFactory;
import nl.thedocumentwizard.wizardconfiguration.jaxb.StringControl;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by jose on 19/12/2016.
 */
public class ControlParserTest {

    private ControlParser controlParser;

    public TerminalNode mockOfTerminalNode(String s) {
        TerminalNode node = mock(TerminalNode.class);
        when(node.getText()).thenReturn(s);
        return node;
    }

    public SpellParser.LiteralContext mockOfStringLiteral(String s) {
        SpellParser.LiteralContext ctx = mock (SpellParser.LiteralContext.class);
        TerminalNode term = mockOfTerminalNode(s);
        when(ctx.STRING()).thenReturn(term);
        return ctx;
    }

    @Before
    public void becauseThereIsAParser() {
        ObjectFactory factory = new ObjectFactory();
        ParsingHelper helper = new ParsingHelper();
        controlParser = new ControlParser(factory, helper);
    }

    @Test
    public void should_set_string_default_value() throws Exception {
        // Because context contains the default value as a literal
        SpellParser.String_controlContext ctx = mock(SpellParser.String_controlContext.class);

        SpellParser.Default_valueContext defVal = mock(SpellParser.Default_valueContext.class);
        when(ctx.default_value()).thenReturn(defVal);

        // Default value is a string literal
        SpellParser.LiteralContext STRING = mockOfStringLiteral("'default value'");
        when(defVal.literal()).thenReturn(STRING);

        // When setAbstractControl
        StringControl control = new StringControl();
        controlParser.setAbstractControl(ctx, control);

        // Then
        Assert.assertEquals("default value", control.getDefaultValue());
    }

    @Test
    public void should_set_metadata_default_value() throws Exception {
        // Because context contains the default value as a literal
        SpellParser.String_controlContext ctx = mock(SpellParser.String_controlContext.class);

        SpellParser.Default_valueContext defVal = mock(SpellParser.Default_valueContext.class);
        when(ctx.default_value()).thenReturn(defVal);

        // Default value is a string literal
        TerminalNode node = mockOfTerminalNode("$metadataName");
        when(defVal.METADATA()).thenReturn(node);

        // When setAbstractControl
        StringControl control = new StringControl();
        controlParser.setAbstractControl(ctx, control);

        // Then
        Assert.assertEquals("metadataName", control.getDefaultValueMetadataName());
    }

    @Test
    public void should_set_metadata_value() throws Exception {
        // Because context contains the default value as a literal
        SpellParser.String_controlContext ctx = mock(SpellParser.String_controlContext.class);

        SpellParser.Ctrl_metadataContext ctrlMetadata = mock(SpellParser.Ctrl_metadataContext.class);
        when(ctx.ctrl_metadata()).thenReturn(ctrlMetadata);

        // Default value is a string literal
        TerminalNode node = mockOfTerminalNode("$metadataName");
        when(ctrlMetadata.METADATA()).thenReturn(node);

        // When setAbstractControl
        StringControl control = new StringControl();
        controlParser.setAbstractControl(ctx, control);

        // Then
        Assert.assertEquals("metadataName", control.getMetadataName());
    }

}
