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
    public void should_parse_step_with_group_name() throws IOException {
        SpellListener listener = parseStep("step \"step name\", 'group name':\n");
        ArgumentCaptor<SpellParser.StepContext> ctx = ArgumentCaptor.forClass((SpellParser.StepContext.class));
        verify(listener).enterStep(ctx.capture());

        Assert.assertEquals("\"step name\"", ctx.getValue().STRING(0).getText());
        Assert.assertEquals("'group name'", ctx.getValue().STRING(1).getText());
    }

    @Test
    public void should_parse_question_without_type_as_string_control() throws IOException {
        SpellListener listener = parseQuestion("'question name'\n");
        ArgumentCaptor<SpellParser.QuestionContext> ctx = ArgumentCaptor.forClass((SpellParser.QuestionContext.class));
        verify(listener).enterQuestion(ctx.capture());

        Assert.assertEquals("'question name'", ctx.getValue().STRING().getText());
    }

}
