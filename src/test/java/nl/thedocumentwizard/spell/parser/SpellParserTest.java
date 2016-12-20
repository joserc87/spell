package nl.thedocumentwizard.spell.parser;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.*;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Test the parsing
 */
public class SpellParserTest {
    public SpellParser getParserFromString(String input) throws IOException {
        // Get our lexer
        InputStream stream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        SpellLexer lexer = new SpellLexer(new ANTLRInputStream(stream));

        // Get a list of matched tokens
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // Pass the tokens to the parser
        return new SpellParser(tokens);
    }

    public SpellListener parseQuestion(String s) throws IOException {
        SpellParser parser = getParserFromString(s);

        // The entry point is a question
        SpellParser.QuestionContext questionSentenceContext = parser.question();

        // Walk it and attach our listener
        ParseTreeWalker walker = new ParseTreeWalker();
        // Mock this:
        SpellListener listener = mock(SpellListener.class);
        walker.walk(listener, questionSentenceContext);
        return listener;
    }

    public SpellListener parseStep(String s) throws IOException {
        SpellParser parser = getParserFromString(s);

        // The entry point is a question
        SpellParser.StepContext questionSentenceContext = parser.step();

        // Walk it and attach our listener
        ParseTreeWalker walker = new ParseTreeWalker();
        // Mock this:
        SpellListener listener = mock(SpellListener.class);
        walker.walk(listener, questionSentenceContext);
        return listener;
    }

    // STEPS
    @Test
    public void should_parse_step_without_group_name() throws IOException {
        SpellListener listener = parseStep("step 'step name':\n");
        ArgumentCaptor<SpellParser.StepContext> ctx = ArgumentCaptor.forClass((SpellParser.StepContext.class));
        verify(listener).enterStep(ctx.capture());

        Assert.assertEquals("'step name'", ctx.getValue().STRING(0).getText());
        Assert.assertNull(ctx.getValue().STRING(1));
    }

    @Test
    public void should_parse_step_with_group_name() throws IOException {
        SpellListener listener = parseStep("step \"step name\", 'group name':\n");
        ArgumentCaptor<SpellParser.StepContext> ctx = ArgumentCaptor.forClass((SpellParser.StepContext.class));
        verify(listener).enterStep(ctx.capture());

        Assert.assertEquals("\"step name\"", ctx.getValue().STRING(0).getText());
        Assert.assertEquals("'group name'", ctx.getValue().STRING(1).getText());
    }

    // QUESTIONS
    @Test
    public void should_parse_question_without_type() throws IOException {
        SpellListener listener = parseQuestion("'question name' as myAlias\n");
        ArgumentCaptor<SpellParser.QuestionContext> ctx = ArgumentCaptor.forClass((SpellParser.QuestionContext.class));
        verify(listener).enterQuestion(ctx.capture());

        Assert.assertNotNull(ctx.getValue().named_string_control());
        Assert.assertEquals("'question name'", ctx.getValue().named_string_control().STRING().getText());
        Assert.assertEquals("myAlias", ctx.getValue().named_string_control().alias().NAME().getText());
    }

    // CONTROL TYPES
    // Label
    @Test
    public void should_parse_label_question() throws IOException {
        SpellListener listener = parseQuestion("label '' = 'label text'\n");
        ArgumentCaptor<SpellParser.QuestionContext> ctx = ArgumentCaptor.forClass((SpellParser.QuestionContext.class));
        verify(listener).enterQuestion(ctx.capture());

        Assert.assertEquals("label", ctx.getValue().named_basic_control().basic_control_type().getText());
        Assert.assertEquals("''", ctx.getValue().named_basic_control().STRING().getText());
        Assert.assertEquals("'label text'", ctx.getValue().named_basic_control().default_value().literal().STRING().getText());
    }

    // String
    @Test
    public void should_parse_string_question() throws IOException {
        SpellListener listener = parseQuestion("string 'q' = 'default value'\n");
        ArgumentCaptor<SpellParser.QuestionContext> ctx = ArgumentCaptor.forClass((SpellParser.QuestionContext.class));
        verify(listener).enterQuestion(ctx.capture());

        Assert.assertEquals("string", ctx.getValue().named_basic_control().basic_control_type().getText());
        Assert.assertEquals("'q'", ctx.getValue().named_basic_control().STRING().getText());
        Assert.assertEquals("'default value'", ctx.getValue().named_basic_control().default_value().literal().STRING().getText());
    }

    // Email
    @Test
    public void should_parse_email_question() throws IOException {
        SpellListener listener = parseQuestion("email 'your email here' -> $metadataName\n");
        ArgumentCaptor<SpellParser.QuestionContext> ctx = ArgumentCaptor.forClass((SpellParser.QuestionContext.class));
        verify(listener).enterQuestion(ctx.capture());

        Assert.assertEquals("email", ctx.getValue().named_basic_control().basic_control_type().getText());
        Assert.assertEquals("'your email here'", ctx.getValue().named_basic_control().STRING().getText());
        Assert.assertEquals("$metadataName", ctx.getValue().named_basic_control().ctrl_metadata().METADATA().getText());
    }

    // Text
    @Test
    public void should_parse_text_question() throws IOException {
        SpellListener listener = parseQuestion("text 'text field' = $inputMetadata -> $outputMetadata\n");
        ArgumentCaptor<SpellParser.QuestionContext> ctx = ArgumentCaptor.forClass((SpellParser.QuestionContext.class));
        verify(listener).enterQuestion(ctx.capture());

        Assert.assertEquals("text", ctx.getValue().named_basic_control().basic_control_type().getText());
        Assert.assertEquals("'text field'", ctx.getValue().named_basic_control().STRING().getText());
        Assert.assertEquals("$inputMetadata", ctx.getValue().named_basic_control().default_value().METADATA().getText());
        Assert.assertEquals("$outputMetadata", ctx.getValue().named_basic_control().ctrl_metadata().METADATA().getText());
    }

    // Date
    @Test
    public void should_parse_date_question() throws IOException {
        SpellListener listener = parseQuestion("date 'q' = $inputMetadata -> $outputMetadata\n");
        ArgumentCaptor<SpellParser.QuestionContext> ctx = ArgumentCaptor.forClass((SpellParser.QuestionContext.class));
        verify(listener).enterQuestion(ctx.capture());

        Assert.assertEquals("date", ctx.getValue().named_basic_control().basic_control_type().getText());
        Assert.assertEquals("'q'", ctx.getValue().named_basic_control().STRING().getText());
        Assert.assertEquals("$inputMetadata", ctx.getValue().named_basic_control().default_value().METADATA().getText());
        Assert.assertEquals("$outputMetadata", ctx.getValue().named_basic_control().ctrl_metadata().METADATA().getText());
    }

    // Number
    @Test
    public void should_parse_number_question() throws IOException {
        SpellListener listener = parseQuestion("number 'q' = 123 -> $metadataName\n");
        ArgumentCaptor<SpellParser.QuestionContext> ctx = ArgumentCaptor.forClass((SpellParser.QuestionContext.class));
        verify(listener).enterQuestion(ctx.capture());

        Assert.assertEquals("number", ctx.getValue().named_basic_control().basic_control_type().getText());
        Assert.assertEquals("'q'", ctx.getValue().named_basic_control().STRING().getText());
        Assert.assertEquals("123", ctx.getValue().named_basic_control().default_value().literal().NUM().getText());
        Assert.assertEquals("$metadataName", ctx.getValue().named_basic_control().ctrl_metadata().METADATA().getText());
    }

    // Checkbox
    @Test
    public void should_parse_checkbox_question_with_default_selected() throws IOException {
        SpellListener listener = parseQuestion("checkbox 'q' = selected -> $metadataName as myAlias\n");
        ArgumentCaptor<SpellParser.QuestionContext> ctx = ArgumentCaptor.forClass((SpellParser.QuestionContext.class));
        verify(listener).enterQuestion(ctx.capture());

        Assert.assertEquals("checkbox", ctx.getValue().named_basic_control().basic_control_type().getText());
        Assert.assertEquals("'q'", ctx.getValue().named_basic_control().STRING().getText());
        Assert.assertEquals("selected", ctx.getValue().named_basic_control().default_value().literal().bool().TRUE().getText());
        Assert.assertNull(ctx.getValue().named_basic_control().default_value().literal().bool().FALSE());
        Assert.assertEquals("$metadataName", ctx.getValue().named_basic_control().ctrl_metadata().METADATA().getText());
        Assert.assertEquals("myAlias", ctx.getValue().named_basic_control().alias().NAME().getText());
    }
    @Test
    public void should_parse_checkbox_question_with_default_unselected() throws IOException {
        SpellListener listener = parseQuestion("checkbox 'q' = unselected -> $metadataName\n");
        ArgumentCaptor<SpellParser.QuestionContext> ctx = ArgumentCaptor.forClass((SpellParser.QuestionContext.class));
        verify(listener).enterQuestion(ctx.capture());

        Assert.assertEquals("checkbox", ctx.getValue().named_basic_control().basic_control_type().getText());
        Assert.assertEquals("'q'", ctx.getValue().named_basic_control().STRING().getText());
        Assert.assertEquals("unselected", ctx.getValue().named_basic_control().default_value().literal().bool().FALSE().getText());
        Assert.assertNull(ctx.getValue().named_basic_control().default_value().literal().bool().TRUE());
        Assert.assertEquals("$metadataName", ctx.getValue().named_basic_control().ctrl_metadata().METADATA().getText());
    }

    @Test
    public void should_parse_checkbox_question_with_default_yes() throws IOException {
        SpellListener listener = parseQuestion("checkbox 'q' = yes -> $metadataName\n");
        ArgumentCaptor<SpellParser.QuestionContext> ctx = ArgumentCaptor.forClass((SpellParser.QuestionContext.class));
        verify(listener).enterQuestion(ctx.capture());

        Assert.assertEquals("checkbox", ctx.getValue().named_basic_control().basic_control_type().getText());
        Assert.assertEquals("'q'", ctx.getValue().named_basic_control().STRING().getText());
        Assert.assertEquals("yes", ctx.getValue().named_basic_control().default_value().literal().bool().TRUE().getText());
        Assert.assertNull(ctx.getValue().named_basic_control().default_value().literal().bool().FALSE());
        Assert.assertEquals("$metadataName", ctx.getValue().named_basic_control().ctrl_metadata().METADATA().getText());
    }
    @Test
    public void should_parse_checkbox_question_with_default_no() throws IOException {
        SpellListener listener = parseQuestion("checkbox 'q' = no -> $metadataName\n");
        ArgumentCaptor<SpellParser.QuestionContext> ctx = ArgumentCaptor.forClass((SpellParser.QuestionContext.class));
        verify(listener).enterQuestion(ctx.capture());

        Assert.assertEquals("checkbox", ctx.getValue().named_basic_control().basic_control_type().getText());
        Assert.assertEquals("'q'", ctx.getValue().named_basic_control().STRING().getText());
        Assert.assertEquals("no", ctx.getValue().named_basic_control().default_value().literal().bool().FALSE().getText());
        Assert.assertNull(ctx.getValue().named_basic_control().default_value().literal().bool().TRUE());
        Assert.assertEquals("$metadataName", ctx.getValue().named_basic_control().ctrl_metadata().METADATA().getText());
    }

    @Test
    public void should_parse_list_question() throws IOException {
        SpellListener listener = parseQuestion("list 'q' = 1 -> $metadataName as myAlias:\n" +
                "   '1'\n" +
                "   '2' = 'second item'\n" +
                "   $meta3 = $meta4\n" +
                "\n");
        ArgumentCaptor<SpellParser.QuestionContext> ctx = ArgumentCaptor.forClass((SpellParser.QuestionContext.class));
        verify(listener).enterQuestion(ctx.capture());

        Assert.assertNotNull(ctx.getValue().named_list_control());
        Assert.assertEquals("'q'", ctx.getValue().named_list_control().STRING().getText());
        Assert.assertEquals("1", ctx.getValue().named_list_control().default_value().literal().NUM().getText());
        Assert.assertEquals("$metadataName", ctx.getValue().named_list_control().ctrl_metadata().METADATA().getText());
        Assert.assertEquals("myAlias", ctx.getValue().named_list_control().alias().NAME().getText());
        // Check items:
        Assert.assertEquals(3, ctx.getValue().named_list_control().list_item().size());
        Assert.assertEquals("'1'", ctx.getValue().named_list_control().list_item(0).string_or_metadata(0).STRING().getText());
        Assert.assertEquals("'2'", ctx.getValue().named_list_control().list_item(1).string_or_metadata(0).STRING().getText());
        Assert.assertEquals("'second item'", ctx.getValue().named_list_control().list_item(1).string_or_metadata(1).STRING().getText());
        Assert.assertEquals("$meta3", ctx.getValue().named_list_control().list_item(2).string_or_metadata(0).METADATA().getText());
        Assert.assertEquals("$meta4", ctx.getValue().named_list_control().list_item(2).string_or_metadata(1).METADATA().getText());
    }

    @Test
    public void should_parse_radio_question() throws IOException {
        SpellListener listener = parseQuestion(
                "required radio 'q' = radioYes -> $metadataName as myAlias:\n" +
                        "   label = 'yes' as radioYes\n" +
                        "   multi as radioNo:\n" +
                        "       label = 'no'\n" +
                        "       string\n" +
                        "   list:\n" +
                        "       $meta1\n" +
                        "       'item2' = $meta2\n" +
                        "\n");
        ArgumentCaptor<SpellParser.QuestionContext> ctx = ArgumentCaptor.forClass((SpellParser.QuestionContext.class));
        verify(listener).enterQuestion(ctx.capture());

        Assert.assertNotNull(ctx.getValue().named_container_control().container_control_type().RADIO_TYPE());
        Assert.assertEquals("'q'", ctx.getValue().named_container_control().STRING().getText());
        Assert.assertEquals("radioYes", ctx.getValue().named_container_control().default_value().NAME().getText());
        Assert.assertEquals("$metadataName", ctx.getValue().named_container_control().ctrl_metadata().METADATA().getText());
        Assert.assertEquals("myAlias", ctx.getValue().named_container_control().alias().NAME().getText());
        // Check radio items:
        Assert.assertEquals(3, ctx.getValue().named_container_control().sub_control().size());

        // label = 'yes' as radioYes
        Assert.assertNotNull(ctx.getValue().named_container_control().sub_control(0).unnamed_basic_control().basic_control_type().LABEL_TYPE());
        Assert.assertEquals("'yes'", ctx.getValue().named_container_control().sub_control(0).unnamed_basic_control().default_value().literal().STRING().getText());
        Assert.assertEquals("radioYes", ctx.getValue().named_container_control().sub_control(0).unnamed_basic_control().alias().NAME().getText());

        // multi as radioNo:
        SpellParser.Unnamed_container_controlContext multi = ctx.getValue().named_container_control().sub_control(1).unnamed_container_control();
        Assert.assertNotNull(multi.container_control_type().MULTI_TYPE());
        Assert.assertEquals("radioNo", multi.alias().NAME().getText());
        //   label = 'no'
        Assert.assertNotNull(multi.sub_control(0).unnamed_basic_control().basic_control_type().LABEL_TYPE());
        Assert.assertEquals("'no'", multi.sub_control(0).unnamed_basic_control().default_value().literal().STRING().getText());
        //   string
        Assert.assertNotNull(multi.sub_control(1).unnamed_basic_control().basic_control_type().STRING_TYPE());

        // list
        SpellParser.Unnamed_list_controlContext list = ctx.getValue().named_container_control().sub_control(2).unnamed_list_control();
        Assert.assertNotNull(list);

        Assert.assertEquals(2, list.list_item().size());
        Assert.assertEquals("$meta1", list.list_item(0).string_or_metadata(0).METADATA().getText());
        Assert.assertEquals("'item2'", list.list_item(1).string_or_metadata(0).STRING().getText());
        Assert.assertEquals("$meta2", list.list_item(1).string_or_metadata(1).METADATA().getText());
    }

    @Test
    public void should_parse_multi_question() throws IOException {
        SpellListener listener = parseQuestion(
                "multi 'q':\n" +
                "   label = 'text'\n" +
                "   radio:\n" +
                "       label = 'yes'\n" +
                "       label = 'no'\n" +
                "\n");
        ArgumentCaptor<SpellParser.QuestionContext> ctx = ArgumentCaptor.forClass((SpellParser.QuestionContext.class));
        verify(listener).enterQuestion(ctx.capture());

        Assert.assertNotNull(ctx.getValue().named_container_control().container_control_type().MULTI_TYPE());
        Assert.assertEquals("'q'", ctx.getValue().named_container_control().STRING().getText());
        // Check radio items:
        Assert.assertEquals(2, ctx.getValue().named_container_control().sub_control().size());

        // label = 'text'
        Assert.assertNotNull(ctx.getValue().named_container_control().sub_control(0).unnamed_basic_control().basic_control_type().LABEL_TYPE());
        Assert.assertEquals("'text'", ctx.getValue().named_container_control().sub_control(0).unnamed_basic_control().default_value().literal().STRING().getText());

        // radio:
        SpellParser.Unnamed_container_controlContext radio = ctx.getValue().named_container_control().sub_control(1).unnamed_container_control();
        Assert.assertNotNull(radio.container_control_type().RADIO_TYPE());
        //   label = 'yes'
        Assert.assertNotNull(radio.sub_control(0).unnamed_basic_control().basic_control_type().LABEL_TYPE());
        Assert.assertEquals("'yes'", radio.sub_control(0).unnamed_basic_control().default_value().literal().STRING().getText());
        //   label = 'no'
        Assert.assertNotNull(radio.sub_control(1).unnamed_basic_control().basic_control_type().LABEL_TYPE());
        Assert.assertEquals("'no'", radio.sub_control(1).unnamed_basic_control().default_value().literal().STRING().getText());
    }

    @Test
    public void should_parse_step_with_questions() throws IOException {
        SpellListener listener = parseStep("step 'step name', 'step group':\n" +
                "  'question name'\n" +
                "  'another question'\n\n");
        ArgumentCaptor<SpellParser.StepContext> ctx = ArgumentCaptor.forClass((SpellParser.StepContext.class));
        verify(listener).enterStep(ctx.capture());

        Assert.assertEquals("'step name'", ctx.getValue().STRING(0).getText());
        Assert.assertEquals("'step group'", ctx.getValue().STRING(1).getText());

        Assert.assertEquals(2, ctx.getValue().question().size());
        Assert.assertEquals("'question name'", ctx.getValue().question(0).named_string_control().STRING().getText());
        Assert.assertEquals("'another question'", ctx.getValue().question(1).named_string_control().STRING().getText());
    }

}
