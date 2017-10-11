package nl.thedocumentwizard.spell.parser;

import org.antlr.v4.runtime.tree.TerminalNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test the helper
 */
public class ParsingHelperTest {

    ParsingHelper helper; // SUT
    @Before
    public void becauseThereIsAHelper() {
        helper = new ParsingHelper();
    }

    @Test
    public void should_get_content_of_string_when_correct() {
        Assert.assertEquals("", helper.getString("\'\'"));
        Assert.assertEquals("a", helper.getString("\'a\'"));
        Assert.assertEquals("asdf", helper.getString("\"asdf\""));
        Assert.assertEquals(" text \n with \t multiple \n lines ", helper.getString("\' text \n with \t multiple \n lines \'"));
        Assert.assertEquals(" text \n with \t multiple \n lines ", helper.getString("\'\'\' text \n with \t multiple \n lines \'\'\'"));
    }

    @Test
    public void should_get_content_of_string_node_when_correct() {
        TerminalNode node = mock(TerminalNode.class);

        when(node.getText()).thenReturn("\'\'");
        Assert.assertEquals("", helper.getString(node));

        when(node.getText()).thenReturn("\'a\'");
        Assert.assertEquals("a", helper.getString(node));

        when(node.getText()).thenReturn("\"asdf\"");
        Assert.assertEquals("asdf", helper.getString(node));

        when(node.getText()).thenReturn("\' text \n with \t multiple \n lines \'");
        Assert.assertEquals(" text \n with \t multiple \n lines ", helper.getString(node));
    }

    @Test
    public void should_get_content_of_metadata_string_when_correct() {
        Assert.assertEquals("", helper.getMetadataName("$"));
        Assert.assertEquals("metadata1", helper.getMetadataName("$metadata1"));
        Assert.assertEquals("$metadata1", helper.getMetadataName("$$metadata1"));
    }

    @Test
    public void should_get_content_of_metadata_node_when_correct() {
        TerminalNode node = mock(TerminalNode.class);

        when(node.getText()).thenReturn("$");
        Assert.assertEquals("", helper.getMetadataName(node));

        when(node.getText()).thenReturn("$metadata1");
        Assert.assertEquals("metadata1", helper.getMetadataName(node));

        when(node.getText()).thenReturn("$$metadata1");
        Assert.assertEquals("$metadata1", helper.getMetadataName(node));
    }

    @Test
    public void should_get_content_of_script_when_correct() {
        Assert.assertEquals("", helper.getScript("``````"));
        Assert.assertEquals("\nsome code\n", helper.getScript("```\nsome code\n```"));
    }
}
