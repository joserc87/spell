package nl.thedocumentwizard.spell.parser;

import nl.thedocumentwizard.wizardconfiguration.jaxb.*;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ControlParserSpec extends spock.lang.Specification {

    private TerminalNode mockOfTerminalNode(String s) {
        TerminalNode node = mock(TerminalNode.class)
        when(node.getText()).thenReturn(s)
        return node
    }

    private SpellParser.LiteralContext mockOfStringLiteral(String s) {
        def ctx = mock(SpellParser.LiteralContext.class)
        def term = mockOfTerminalNode(s)
        when(ctx.STRING()).thenReturn(term)
        return ctx
    }

    private SpellParser.LiteralContext mockOfNumLiteral(String s) {
        def ctx = mock(SpellParser.LiteralContext.class)
        def term = mockOfTerminalNode(s)
        when(ctx.NUM()).thenReturn(term)
        return ctx;
    }

    private SpellParser.LiteralContext mockOfTrueBooleanLiteral(String s) {
        def ctx = mock(SpellParser.LiteralContext.class);
        def boolContext = mock(SpellParser.BoolContext.class);
        def term = mockOfTerminalNode(s);
        when(boolContext.TRUE()).thenReturn(term);
        when(ctx.bool()).thenReturn(boolContext);
        return ctx;
    }

    private SpellParser.LiteralContext mockOfFalseBooleanLiteral(String s) {
        def ctx = mock(SpellParser.LiteralContext.class);
        def boolContext = mock(SpellParser.BoolContext.class);
        def term = mockOfTerminalNode(s);
        when(boolContext.TRUE()).thenReturn(term);
        when(ctx.bool()).thenReturn(boolContext);
        return ctx;
    }

    private SpellParser.Control_attributeContext createStringAttribute(String attribute, String value) {
        def name = mockOfTerminalNode(attribute)
        def stringValue = mockOfStringLiteral(value)
        def ctx = mock(SpellParser.Control_attributeContext.class)
        when(ctx.NAME()).thenReturn(name)
        when(ctx.literal()).thenReturn(stringValue)
        return ctx
    }

    private SpellParser.Control_attributeContext createNumberAttribute(String attribute, String value) {
        def name = mockOfTerminalNode(attribute)
        def numValue = mockOfNumLiteral(value)
        def ctx = mock(SpellParser.Control_attributeContext.class)
        when(ctx.NAME()).thenReturn(name)
        when(ctx.literal()).thenReturn(numValue)
        return ctx
    }

    private SpellParser.Control_attributeContext createBooleanAttribute(String attribute, boolean value) {
        def name = mockOfTerminalNode(attribute)
        def boolValue = value ? mockOfTrueBooleanLiteral("True") : mockOfFalseBooleanLiteral("False")
        def ctx = mock(SpellParser.Control_attributeContext.class)
        when(ctx.NAME()).thenReturn(name)
        when(ctx.literal()).thenReturn(boolValue)
        return ctx
    }

    def 'getSetterMethodName should return the setter method name'() {
        given: 'a ControlParser with no factory or helper'
            def parser = new ControlParser(null, null)
        and: 'an attribute'
            def attribute = 'myAttribute'
        when: 'we retrieve the method name'
            def method = parser.getSetterMethodName(attribute)
        then: 'the method name should be setMyAttribute'
            method == 'setMyAttribute'
    }

    def 'setAbstractControl should set custom control attributes'() throws Exception {
        given: 'a control parser'
            def factory = new ObjectFactory()
            def helper = new ParsingHelper()
            def controlParser = new ControlParser(factory, helper)
        and: 'a basic control'
            def ctx = mock(SpellParser.Named_basic_controlContext.class)
            def controlAttributeList = mock(SpellParser.Control_attribute_listContext.class)
            when(ctx.control_attribute_list()).thenReturn(controlAttributeList)
            def attributes = new ArrayList<SpellParser.Control_attributeContext>()
            when(controlAttributeList.control_attribute()).thenReturn(attributes)
        and: 'an attribute minValue = "1"'
            attributes.add(createNumberAttribute("minValue", "1"))
        and: 'an attribute trimTrailedZeros = "true"'
            attributes.add(createBooleanAttribute("trimTrailedZeros", true))
        and: 'an attribute outputFormat = "###"'
            attributes.add(createStringAttribute("outputFormat", "'###'"))

        when: 'setAbstractControl is called'
            def control = new NumberControl()
            controlParser.setAbstractControl(ctx, control)

        then: 'the minValue should be 1'
            control.minValue == 1
        and: 'the trimTrailedZeros should be true'
            control.trimTrailedZeros == true
        and: 'the outputFormat should be "###"'
            control.outputFormat == '###'
    }

    def 'setAbstractControl should accept label as a custom control attribute'() throws Exception {
        given: 'a control parser'
            def factory = new ObjectFactory()
            def helper = new ParsingHelper()
            def controlParser = new ControlParser(factory, helper)
        and: 'a basic control'
            def ctx = mock(SpellParser.Named_basic_controlContext.class)
            def controlAttributeList = mock(SpellParser.Control_attribute_listContext.class)
            when(ctx.control_attribute_list()).thenReturn(controlAttributeList)
            def attributes = new ArrayList<SpellParser.Control_attributeContext>()
            when(controlAttributeList.control_attribute()).thenReturn(attributes)
        and: 'an attribute label = "my label"'
            attributes.add(createStringAttribute("label", "'my label'"))

        when: 'setAbstractControl is called'
            def control = new CheckboxControl()
            controlParser.setAbstractControl(ctx, control)

        then: 'the label should be "my label"'
            control.label == 'my label'
    }

    def 'setAbstractControl should set separator attribute'() throws Exception {
        given: 'a control parser'
            def factory = new ObjectFactory()
            def helper = new ParsingHelper()
            def controlParser = new ControlParser(factory, helper)
        and: 'a basic control'
            def ctx = mock(SpellParser.Named_basic_controlContext.class)
            def controlAttributeList = mock(SpellParser.Control_attribute_listContext.class)
            when(ctx.control_attribute_list()).thenReturn(controlAttributeList)
            def attributes = new ArrayList<SpellParser.Control_attributeContext>()
            when(controlAttributeList.control_attribute()).thenReturn(attributes)
        and: 'an attribute decimalSeparator = ","'
            attributes.add(createStringAttribute("decimalSeparator", "','"))

        when: 'setAbstractControl is called'
            def control = new NumberControl()
            controlParser.setAbstractControl(ctx, control)

        then: 'the decimal separator should be Separator.COMMA'
            control.decimalSeparator == Separator.COMMA
    }
}
