package nl.thedocumentwizard.spell.parser;

import nl.thedocumentwizard.wizardconfiguration.jaxb.*;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
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
        SpellParser.LiteralContext ctx = mock(SpellParser.LiteralContext.class);
        TerminalNode term = mockOfTerminalNode(s);
        when(ctx.STRING_LITERAL()).thenReturn(term);
        return ctx;
    }

    public SpellParser.LiteralContext mockOfNumberLiteral(String s) {
        SpellParser.LiteralContext ctx = mock(SpellParser.LiteralContext.class);
        TerminalNode term = mockOfTerminalNode(s);
        when(ctx.NUM()).thenReturn(term);
        return ctx;
    }

    public SpellParser.LiteralContext mockOfTrueBooleanLiteral(String s) {
        SpellParser.LiteralContext ctx = mock(SpellParser.LiteralContext.class);
        SpellParser.BoolContext boolContext = mock(SpellParser.BoolContext.class); 
        TerminalNode term = mockOfTerminalNode(s);
        when(boolContext.TRUE()).thenReturn(term);
        when(ctx.bool()).thenReturn(boolContext);
        return ctx;
    }

    public SpellParser.Default_valueContext mockOfStringDefaultValue(String s) {
        SpellParser.Default_valueContext defVal = mock(SpellParser.Default_valueContext.class);
        SpellParser.LiteralContext STRING_LITERAL = mockOfStringLiteral(s);
        when(defVal.literal()).thenReturn(STRING_LITERAL);
        return defVal;
    }

    public SpellParser.Default_valueContext mockOfNumberDefaultValue(String s) {
        SpellParser.Default_valueContext defVal = mock(SpellParser.Default_valueContext.class);
        SpellParser.LiteralContext NUMBER = mockOfNumberLiteral(s);
        when(defVal.literal()).thenReturn(NUMBER);
        return defVal;
    }

    public SpellParser.Default_valueContext mockOfTrueBooleanDefaultValue(String s) {
        SpellParser.Default_valueContext defVal = mock(SpellParser.Default_valueContext.class);
        SpellParser.LiteralContext Boolean = mockOfTrueBooleanLiteral(s);
        when(defVal.literal()).thenReturn(Boolean);
        return defVal;
    }

    public SpellParser.Default_valueContext mockOfMetadataDefaultValue(String m) {
        SpellParser.Default_valueContext defVal = mock(SpellParser.Default_valueContext.class);
        TerminalNode METADATA = mockOfTerminalNode(m);
        when(defVal.METADATA()).thenReturn(METADATA);
        return defVal;
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
        SpellParser.Named_string_controlContext ctx = mock(SpellParser.Named_string_controlContext.class);

        SpellParser.Default_valueContext defVal = mockOfStringDefaultValue("'default value'");
        when(ctx.default_value()).thenReturn(defVal);

        // When setAbstractControl
        StringControl control = new StringControl();
        controlParser.setAbstractControl(ctx, control);

        // Then
        Assert.assertEquals("default value", control.getDefaultValue());
    }

    @Test
    public void setAbstractControl_should_set_number_default_value() throws Exception {
        // Because context contains the default value as a literal
        SpellParser.Named_string_controlContext ctx = mock(SpellParser.Named_string_controlContext.class);

        SpellParser.Default_valueContext defVal = mockOfNumberDefaultValue("123");
        when(ctx.default_value()).thenReturn(defVal);

        // When setAbstractControl
        NumberControl control = new NumberControl();
        controlParser.setAbstractControl(ctx, control);

        // Then
        Assert.assertEquals("123", control.getDefaultValue());
    }

    @Test
    public void setAbstractControl_should_set_boolean_default_value() throws Exception {
        // Because context contains the default value as a literal
        SpellParser.Named_string_controlContext ctx = mock(SpellParser.Named_string_controlContext.class);

        SpellParser.Default_valueContext defVal = mockOfTrueBooleanDefaultValue("selected");
        when(ctx.default_value()).thenReturn(defVal);

        // When setAbstractControl
        CheckboxControl control = new CheckboxControl();
        controlParser.setAbstractControl(ctx, control);

        // Then
        Assert.assertEquals("True", control.getDefaultValue());
    }

    @Test
    public void setAbstractControl_should_set_metadata_default_value() throws Exception {
        // Because context contains the default value metadata
        SpellParser.Named_string_controlContext ctx = mock(SpellParser.Named_string_controlContext.class);

        SpellParser.Default_valueContext defVal = mockOfMetadataDefaultValue("$metadataName");
        when(ctx.default_value()).thenReturn(defVal);

        // When setAbstractControl
        StringControl control = new StringControl();
        controlParser.setAbstractControl(ctx, control);

        // Then
        Assert.assertEquals("metadataName", control.getDefaultValueMetadataName());
    }

    @Test
    public void setAbstractControl_should_set_metadata_value() throws Exception {
        // Because context contains the control metadata
        SpellParser.Named_string_controlContext ctx = mock(SpellParser.Named_string_controlContext.class);

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
        SpellParser.Named_string_controlContext controlContext = mock(SpellParser.Named_string_controlContext.class);
        when(ctx.named_string_control()).thenReturn(controlContext);

        SpellParser.Default_valueContext defVal = mockOfStringDefaultValue("'default value'");
        when(controlContext.default_value()).thenReturn(defVal);

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

    @Test
    public void getControl_should_register_alias_and_set_id() {
        SpellParser.QuestionContext ctx = mock(SpellParser.QuestionContext.class);
        SpellParser.Named_string_controlContext controlContext = mock(SpellParser.Named_string_controlContext.class);
        when(ctx.named_string_control()).thenReturn(controlContext);

        SpellParser.AliasContext alias = mock(SpellParser.AliasContext.class);
        when(controlContext.alias()).thenReturn(alias);

        TerminalNode name = mockOfTerminalNode("myAlias");
        when(alias.NAME()).thenReturn(name);

        // Mock the alias helper
        ControlAliasHelper aliasHelper = mock(ControlAliasHelper.class);
        controlParser.setAliasHelper(aliasHelper);

        // When setAbstractControl
        AbstractControl control = controlParser.getControl(ctx);

        // Then it should return a string control
        Assert.assertEquals(StringControl.class, control.getClass());
        Assert.assertEquals("myAlias", control.getId());

        // It should have registered the control for that alias
        verify(aliasHelper).registerControl("myAlias", control);
    }

    public SpellParser.String_or_metadataContext createString_or_metadataContext(TerminalNode string, TerminalNode metadata) {
        SpellParser.String_or_metadataContext ctx = mock(SpellParser.String_or_metadataContext.class);
        if (string != null) {
            when(ctx.STRING_LITERAL()).thenReturn(string);
        }
        if (metadata != null) {
            when(ctx.METADATA()).thenReturn(metadata);
        }
        return ctx;
    }

    @Test
    public void getListControl_should_add_list_items() {
        SpellParser.QuestionContext ctx = mock(SpellParser.QuestionContext.class);
        SpellParser.Named_list_controlContext controlContext = mock(SpellParser.Named_list_controlContext.class);
        when(ctx.named_list_control()).thenReturn(controlContext);

        // No default value nor metadata.
        // List contains items
        List<SpellParser.List_itemContext> items = new ArrayList<>();
        when(controlContext.list_item()).thenReturn(items);

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

    @Test
    public void getRadioControl_should_add_sub_items() {
        // Question -> Radio
        // radio:
        //   label = 'option 1'
        //   multi:
        //     label = 'option 2'
        //     string

        SpellParser.QuestionContext ctx = mock(SpellParser.QuestionContext.class);
        SpellParser.Named_container_controlContext controlContext = mock(SpellParser.Named_container_controlContext.class);
        when(ctx.named_container_control()).thenReturn(controlContext);
        // Root is a radio
        SpellParser.Container_control_typeContext radio_control_typeContext = mock(SpellParser.Container_control_typeContext.class);
        TerminalNode radio_type = mockOfTerminalNode("radio");
        when(radio_control_typeContext.RADIO_TYPE()).thenReturn(radio_type);
        when(controlContext.container_control_type()).thenReturn(radio_control_typeContext);

        // No default value nor metadata.
        // Radio contains subcontrols
        List<SpellParser.Sub_controlContext> subcontrols = new ArrayList<>();
        when(controlContext.sub_control()).thenReturn(subcontrols);

        // Sub-control 1 : label
        SpellParser.Basic_control_typeContext label_control_typeContext = mock(SpellParser.Basic_control_typeContext.class);
        TerminalNode label_type = mockOfTerminalNode("label");
        when(label_control_typeContext.LABEL_TYPE()).thenReturn(label_type);

        SpellParser.Unnamed_basic_controlContext label_ctx = mock(SpellParser.Unnamed_basic_controlContext.class);
        when(label_ctx.basic_control_type()).thenReturn(label_control_typeContext);
        SpellParser.Default_valueContext defVal = mockOfStringDefaultValue("'option 1'");
        when(label_ctx.default_value()).thenReturn(defVal);

        // Sub-control 2 : multi
        SpellParser.Container_control_typeContext multi_control_typeContext = mock(SpellParser.Container_control_typeContext.class);
        TerminalNode multi_type = mockOfTerminalNode("multi");
        when(multi_control_typeContext.MULTI_TYPE()).thenReturn(multi_type);

        SpellParser.Unnamed_container_controlContext multi_ctx = mock(SpellParser.Unnamed_container_controlContext.class);
        when(multi_ctx.container_control_type()).thenReturn(multi_control_typeContext);

        // Multi contains subsubcontrols
        List<SpellParser.Sub_controlContext> subsubcontrols = new ArrayList<>();
        when(multi_ctx.sub_control()).thenReturn(subsubcontrols);

        // Sub-sub-control 2.1: label
        SpellParser.Unnamed_basic_controlContext sub_label_ctx = mock(SpellParser.Unnamed_basic_controlContext.class);
        when(sub_label_ctx.basic_control_type()).thenReturn(label_control_typeContext);
        SpellParser.Default_valueContext sub_label_defVal = mockOfStringDefaultValue("'option 2'");
        when(sub_label_ctx.default_value()).thenReturn(sub_label_defVal);

        // Sub-sub-control 2.2: string
        SpellParser.Basic_control_typeContext string_control_typeContext = mock(SpellParser.Basic_control_typeContext.class);
        TerminalNode string_type = mockOfTerminalNode("string");
        when(string_control_typeContext.STRING_TYPE()).thenReturn(string_type);

        SpellParser.Unnamed_basic_controlContext sub_string_ctx = mock(SpellParser.Unnamed_basic_controlContext.class);
        when(sub_string_ctx.basic_control_type()).thenReturn(string_control_typeContext);

        // Add subcontrols
        SpellParser.Sub_controlContext label_subcontrol = mock(SpellParser.Sub_controlContext.class);
        when(label_subcontrol.unnamed_basic_control()).thenReturn(label_ctx);

        SpellParser.Sub_controlContext multi_subcontrol = mock(SpellParser.Sub_controlContext.class);
        when(multi_subcontrol.unnamed_container_control()).thenReturn(multi_ctx);

        SpellParser.Sub_controlContext sub_label_subcontrol = mock(SpellParser.Sub_controlContext.class);
        when(sub_label_subcontrol.unnamed_basic_control()).thenReturn(sub_label_ctx);

        SpellParser.Sub_controlContext sub_string_subcontrol = mock(SpellParser.Sub_controlContext.class);
        when(sub_string_subcontrol.unnamed_basic_control()).thenReturn(sub_string_ctx);

        subcontrols.add(label_subcontrol);
        subcontrols.add(multi_subcontrol);

        subsubcontrols.add(sub_label_subcontrol);
        subsubcontrols.add(sub_string_subcontrol);

        // When setAbstractControl
        AbstractControl control = controlParser.getControl(ctx);

        // THEN it should return a radio control
        Assert.assertEquals(RadioControl.class, control.getClass());

        // The control should have 3 items
        RadioControl radioControl = (RadioControl) control;

        List<AbstractControl> subControls = radioControl.getItems().getTextOrCheckboxOrAttachment();
        Assert.assertEquals(2, subControls.size());
        LabelControl label = (LabelControl) subControls.get(0);
        Assert.assertEquals("option 1", label.getDefaultValue());
        MultiControl multi = (MultiControl) subControls.get(1);

        List<AbstractControl> subSubControls = multi.getControls().getTextOrNumberOrAttachment();
        Assert.assertEquals(2, subSubControls.size());
        LabelControl sublabel = (LabelControl) subSubControls.get(0);
        Assert.assertEquals("option 2", sublabel.getDefaultValue());
        StringControl substring = (StringControl) subSubControls.get(1);
    }
}
