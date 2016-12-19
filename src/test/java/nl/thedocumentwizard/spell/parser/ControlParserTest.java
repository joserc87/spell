package nl.thedocumentwizard.spell.parser;

import jdk.nashorn.internal.ir.Terminal;
import nl.thedocumentwizard.wizardconfiguration.jaxb.AbstractControl;
import nl.thedocumentwizard.wizardconfiguration.jaxb.ListControl;
import nl.thedocumentwizard.wizardconfiguration.jaxb.ObjectFactory;
import nl.thedocumentwizard.wizardconfiguration.jaxb.StringControl;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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

    // For setAbstractControl

    @Test
    public void setAbstractControl_should_set_string_default_value() throws Exception {
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
    public void setAbstractControl_should_set_metadata_default_value() throws Exception {
        // Because context contains the default value metadata
        SpellParser.String_controlContext ctx = mock(SpellParser.String_controlContext.class);

        SpellParser.Default_valueContext defVal = mock(SpellParser.Default_valueContext.class);
        when(ctx.default_value()).thenReturn(defVal);

        // Default value is a metadata
        TerminalNode node = mockOfTerminalNode("$metadataName");
        when(defVal.METADATA()).thenReturn(node);

        // When setAbstractControl
        StringControl control = new StringControl();
        controlParser.setAbstractControl(ctx, control);

        // Then
        Assert.assertEquals("metadataName", control.getDefaultValueMetadataName());
    }

    @Test
    public void setAbstractControl_should_set_metadata_value() throws Exception {
        // Because context contains the control metadata
        SpellParser.String_controlContext ctx = mock(SpellParser.String_controlContext.class);

        SpellParser.Ctrl_metadataContext ctrlMetadata = mock(SpellParser.Ctrl_metadataContext.class);
        when(ctx.ctrl_metadata()).thenReturn(ctrlMetadata);

        TerminalNode node = mockOfTerminalNode("$metadataName");
        when(ctrlMetadata.METADATA()).thenReturn(node);

        // When setAbstractControl
        StringControl control = new StringControl();
        controlParser.setAbstractControl(ctx, control);

        // Then
        Assert.assertEquals("metadataName", control.getMetadataName());
    }

    @Test
    public void getControl_should_set_default_value_and_metadata_for_string_control() {
        SpellParser.QuestionContext ctx = mock(SpellParser.QuestionContext.class);
        SpellParser.String_controlContext controlContext = mock(SpellParser.String_controlContext.class);
        when(ctx.string_control()).thenReturn(controlContext);

        SpellParser.Default_valueContext defVal = mock(SpellParser.Default_valueContext.class);
        when(controlContext.default_value()).thenReturn(defVal);

        // Default value is a string literal
        SpellParser.LiteralContext STRING = mockOfStringLiteral("'default value'");
        when(defVal.literal()).thenReturn(STRING);

        SpellParser.Ctrl_metadataContext ctrlMetadata = mock(SpellParser.Ctrl_metadataContext.class);
        when(controlContext.ctrl_metadata()).thenReturn(ctrlMetadata);

        // Default value is a string literal
        TerminalNode node = mockOfTerminalNode("$metadataName");
        when(ctrlMetadata.METADATA()).thenReturn(node);

        // When setAbstractControl
        AbstractControl control = controlParser.getControl(ctx);

        // Then it should return a string control
        Assert.assertEquals(StringControl.class, control.getClass());
        Assert.assertEquals("default value", control.getDefaultValue());
        Assert.assertEquals("metadataName", control.getMetadataName());
    }

    public SpellParser.String_or_metadataContext createString_or_metadataContext(TerminalNode string, TerminalNode metadata) {
        SpellParser.String_or_metadataContext ctx = mock(SpellParser.String_or_metadataContext.class);
        if (string != null) {
            when(ctx.STRING()).thenReturn(string);
        }
        if (metadata != null) {
            when(ctx.METADATA()).thenReturn(metadata);
        }
        return ctx;
    }

    @Test
    public void getListControl_should_add_list_items() {
        SpellParser.QuestionContext ctx = mock(SpellParser.QuestionContext.class);
        SpellParser.List_controlContext controlContext = mock(SpellParser.List_controlContext.class);
        when(ctx.list_control()).thenReturn(controlContext);

        // No default value nor metadata.
        // List contains items
        List<SpellParser.List_itemContext> items = new ArrayList<SpellParser.List_itemContext>();
        when(controlContext.list_item()).thenReturn(items);

        // Values of the items
        TerminalNode listDisplayValue1 = mockOfTerminalNode("\"displayValue1\"");
        TerminalNode listValue1 = mockOfTerminalNode("\"value1\"");
        TerminalNode listMetadataDisplayValue2 = mockOfTerminalNode("$metadata1");
        TerminalNode listMetadataValue2 = mockOfTerminalNode("$metadata2");
        TerminalNode listMetadatavalue3 = mockOfTerminalNode("$metadata3");

        SpellParser.String_or_metadataContext displayValue1 = createString_or_metadataContext(mockOfTerminalNode("'displayValue1'"), null);
        SpellParser.String_or_metadataContext value1 = createString_or_metadataContext(mockOfTerminalNode("'value1'"), null);
        SpellParser.String_or_metadataContext displayValue2 = createString_or_metadataContext(null, mockOfTerminalNode("$metadata1"));
        SpellParser.String_or_metadataContext value2 = createString_or_metadataContext(null, mockOfTerminalNode("$metadata2"));
        SpellParser.String_or_metadataContext value3 = createString_or_metadataContext(null, mockOfTerminalNode("$metadata3"));

        // 3 items
        SpellParser.List_itemContext item1 = mock(SpellParser.List_itemContext.class);
        when(item1.string_or_metadata(0)).thenReturn(displayValue1);
        when(item1.string_or_metadata(1)).thenReturn(value1);

        SpellParser.List_itemContext item2 = mock(SpellParser.List_itemContext.class);
        when(item2.string_or_metadata(0)).thenReturn(displayValue2);
        when(item2.string_or_metadata(1)).thenReturn(value2);

        SpellParser.List_itemContext item3 = mock(SpellParser.List_itemContext.class);
        when(item3.string_or_metadata(0)).thenReturn(value3);

        items.add(item1);
        items.add(item2);
        items.add(item3);

        // When setAbstractControl
        AbstractControl control = controlParser.getControl(ctx);

        // Then it should return a list control
        Assert.assertEquals(ListControl.class, control.getClass());

        // The control should have 3 items
        ListControl listControl = (ListControl) control;
        Assert.assertEquals(3, listControl.getItems().getItem().size());
        Assert.assertEquals("displayValue1", listControl.getItems().getItem().get(0).getDisplayText().getValue());
        Assert.assertEquals("value1", listControl.getItems().getItem().get(0).getValue().getValue());
        Assert.assertEquals("metadata1", listControl.getItems().getItem().get(1).getDisplayText().getMetadataName());
        Assert.assertEquals("metadata2", listControl.getItems().getItem().get(1).getValue().getMetadataName());
        Assert.assertEquals("metadata3", listControl.getItems().getItem().get(2).getValue().getMetadataName());
    }
}
