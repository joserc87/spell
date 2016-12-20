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

        Assert.assertNotNull(ctx.getValue().string_control());
        Assert.assertEquals("'question name'", ctx.getValue().string_control().STRING().getText());
        Assert.assertEquals("myAlias", ctx.getValue().string_control().alias().NAME().getText());
    }

    // CONTROL TYPES
    // Label
    @Test
    public void should_parse_label_question() throws IOException {
        SpellListener listener = parseQuestion("label '' = 'label text'\n");
        ArgumentCaptor<SpellParser.QuestionContext> ctx = ArgumentCaptor.forClass((SpellParser.QuestionContext.class));
        verify(listener).enterQuestion(ctx.capture());

        Assert.assertEquals("label", ctx.getValue().basic_control().basic_control_type().getText());
        Assert.assertEquals("''", ctx.getValue().basic_control().STRING().getText());
        Assert.assertEquals("'label text'", ctx.getValue().basic_control().default_value().literal().STRING().getText());
    }

    // String
    @Test
    public void should_parse_string_question() throws IOException {
        SpellListener listener = parseQuestion("string 'q' = 'default value'\n");
        ArgumentCaptor<SpellParser.QuestionContext> ctx = ArgumentCaptor.forClass((SpellParser.QuestionContext.class));
        verify(listener).enterQuestion(ctx.capture());

        Assert.assertEquals("string", ctx.getValue().basic_control().basic_control_type().getText());
        Assert.assertEquals("'q'", ctx.getValue().basic_control().STRING().getText());
        Assert.assertEquals("'default value'", ctx.getValue().basic_control().default_value().literal().STRING().getText());
    }

    // Email
    @Test
    public void should_parse_email_question() throws IOException {
        SpellListener listener = parseQuestion("email 'your email here' -> $metadataName\n");
        ArgumentCaptor<SpellParser.QuestionContext> ctx = ArgumentCaptor.forClass((SpellParser.QuestionContext.class));
        verify(listener).enterQuestion(ctx.capture());

        Assert.assertEquals("email", ctx.getValue().basic_control().basic_control_type().getText());
        Assert.assertEquals("'your email here'", ctx.getValue().basic_control().STRING().getText());
        Assert.assertEquals("$metadataName", ctx.getValue().basic_control().ctrl_metadata().METADATA().getText());
    }

    // Text
    @Test
    public void should_parse_text_question() throws IOException {
        SpellListener listener = parseQuestion("text 'text field' = $inputMetadata -> $outputMetadata\n");
        ArgumentCaptor<SpellParser.QuestionContext> ctx = ArgumentCaptor.forClass((SpellParser.QuestionContext.class));
        verify(listener).enterQuestion(ctx.capture());

        Assert.assertEquals("text", ctx.getValue().basic_control().basic_control_type().getText());
        Assert.assertEquals("'text field'", ctx.getValue().basic_control().STRING().getText());
        Assert.assertEquals("$inputMetadata", ctx.getValue().basic_control().default_value().METADATA().getText());
        Assert.assertEquals("$outputMetadata", ctx.getValue().basic_control().ctrl_metadata().METADATA().getText());
    }

    // Date
    @Test
    public void should_parse_date_question() throws IOException {
        SpellListener listener = parseQuestion("date 'q' = $inputMetadata -> $outputMetadata\n");
        ArgumentCaptor<SpellParser.QuestionContext> ctx = ArgumentCaptor.forClass((SpellParser.QuestionContext.class));
        verify(listener).enterQuestion(ctx.capture());

        Assert.assertEquals("date", ctx.getValue().basic_control().basic_control_type().getText());
        Assert.assertEquals("'q'", ctx.getValue().basic_control().STRING().getText());
        Assert.assertEquals("$inputMetadata", ctx.getValue().basic_control().default_value().METADATA().getText());
        Assert.assertEquals("$outputMetadata", ctx.getValue().basic_control().ctrl_metadata().METADATA().getText());
    }

    // Number
    @Test
    public void should_parse_number_question() throws IOException {
        SpellListener listener = parseQuestion("number 'q' = 123 -> $metadataName\n");
        ArgumentCaptor<SpellParser.QuestionContext> ctx = ArgumentCaptor.forClass((SpellParser.QuestionContext.class));
        verify(listener).enterQuestion(ctx.capture());

        Assert.assertEquals("number", ctx.getValue().basic_control().basic_control_type().getText());
        Assert.assertEquals("'q'", ctx.getValue().basic_control().STRING().getText());
        Assert.assertEquals("123", ctx.getValue().basic_control().default_value().literal().NUM().getText());
        Assert.assertEquals("$metadataName", ctx.getValue().basic_control().ctrl_metadata().METADATA().getText());
    }

    // Checkbox
    @Test
    public void should_parse_checkbox_question_with_default_selected() throws IOException {
        SpellListener listener = parseQuestion("checkbox 'q' = selected -> $metadataName as myAlias\n");
        ArgumentCaptor<SpellParser.QuestionContext> ctx = ArgumentCaptor.forClass((SpellParser.QuestionContext.class));
        verify(listener).enterQuestion(ctx.capture());

        Assert.assertEquals("checkbox", ctx.getValue().basic_control().basic_control_type().getText());
        Assert.assertEquals("'q'", ctx.getValue().basic_control().STRING().getText());
        Assert.assertEquals("selected", ctx.getValue().basic_control().default_value().literal().bool().TRUE().getText());
        Assert.assertNull(ctx.getValue().basic_control().default_value().literal().bool().FALSE());
        Assert.assertEquals("$metadataName", ctx.getValue().basic_control().ctrl_metadata().METADATA().getText());
        Assert.assertEquals("myAlias", ctx.getValue().basic_control().alias().NAME().getText());
    }
    @Test
    public void should_parse_checkbox_question_with_default_unselected() throws IOException {
        SpellListener listener = parseQuestion("checkbox 'q' = unselected -> $metadataName\n");
        ArgumentCaptor<SpellParser.QuestionContext> ctx = ArgumentCaptor.forClass((SpellParser.QuestionContext.class));
        verify(listener).enterQuestion(ctx.capture());

        Assert.assertEquals("checkbox", ctx.getValue().basic_control().basic_control_type().getText());
        Assert.assertEquals("'q'", ctx.getValue().basic_control().STRING().getText());
        Assert.assertEquals("unselected", ctx.getValue().basic_control().default_value().literal().bool().FALSE().getText());
        Assert.assertNull(ctx.getValue().basic_control().default_value().literal().bool().TRUE());
        Assert.assertEquals("$metadataName", ctx.getValue().basic_control().ctrl_metadata().METADATA().getText());
    }

    @Test
    public void should_parse_checkbox_question_with_default_yes() throws IOException {
        SpellListener listener = parseQuestion("checkbox 'q' = yes -> $metadataName\n");
        ArgumentCaptor<SpellParser.QuestionContext> ctx = ArgumentCaptor.forClass((SpellParser.QuestionContext.class));
        verify(listener).enterQuestion(ctx.capture());

        Assert.assertEquals("checkbox", ctx.getValue().basic_control().basic_control_type().getText());
        Assert.assertEquals("'q'", ctx.getValue().basic_control().STRING().getText());
        Assert.assertEquals("yes", ctx.getValue().basic_control().default_value().literal().bool().TRUE().getText());
        Assert.assertNull(ctx.getValue().basic_control().default_value().literal().bool().FALSE());
        Assert.assertEquals("$metadataName", ctx.getValue().basic_control().ctrl_metadata().METADATA().getText());
    }
    @Test
    public void should_parse_checkbox_question_with_default_no() throws IOException {
        SpellListener listener = parseQuestion("checkbox 'q' = no -> $metadataName\n");
        ArgumentCaptor<SpellParser.QuestionContext> ctx = ArgumentCaptor.forClass((SpellParser.QuestionContext.class));
        verify(listener).enterQuestion(ctx.capture());

        Assert.assertEquals("checkbox", ctx.getValue().basic_control().basic_control_type().getText());
        Assert.assertEquals("'q'", ctx.getValue().basic_control().STRING().getText());
        Assert.assertEquals("no", ctx.getValue().basic_control().default_value().literal().bool().FALSE().getText());
        Assert.assertNull(ctx.getValue().basic_control().default_value().literal().bool().TRUE());
        Assert.assertEquals("$metadataName", ctx.getValue().basic_control().ctrl_metadata().METADATA().getText());
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

        Assert.assertNotNull(ctx.getValue().list_control());
        Assert.assertEquals("'q'", ctx.getValue().list_control().STRING().getText());
        Assert.assertEquals("1", ctx.getValue().list_control().default_value().literal().NUM().getText());
        Assert.assertEquals("$metadataName", ctx.getValue().list_control().ctrl_metadata().METADATA().getText());
        Assert.assertEquals("myAlias", ctx.getValue().list_control().alias().NAME().getText());
        // Check items:
        Assert.assertEquals(3, ctx.getValue().list_control().list_item().size());
        Assert.assertEquals("'1'", ctx.getValue().list_control().list_item(0).string_or_metadata(0).STRING().getText());
        Assert.assertEquals("'2'", ctx.getValue().list_control().list_item(1).string_or_metadata(0).STRING().getText());
        Assert.assertEquals("'second item'", ctx.getValue().list_control().list_item(1).string_or_metadata(1).STRING().getText());
        Assert.assertEquals("$meta3", ctx.getValue().list_control().list_item(2).string_or_metadata(0).METADATA().getText());
        Assert.assertEquals("$meta4", ctx.getValue().list_control().list_item(2).string_or_metadata(1).METADATA().getText());
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
        Assert.assertEquals("'question name'", ctx.getValue().question(0).string_control().STRING().getText());
        Assert.assertEquals("'another question'", ctx.getValue().question(1).string_control().STRING().getText());
    }

}
