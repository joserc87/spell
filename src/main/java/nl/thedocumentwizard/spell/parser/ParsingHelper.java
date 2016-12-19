package nl.thedocumentwizard.spell.parser;

import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * Created by jose on 19/12/2016.
 */
public class ParsingHelper {

    /**
     * Default constructor
     */
    public ParsingHelper() {

    }

    /**
     * Parses a string with single or double quotes and returns the content
     * 
     * @param quotedString A string with the format "content"
     *
     * @return A string with the content
     */
    public String getString(String quotedString) {
        assert (quotedString != null && quotedString.length() >= 2 &&
                quotedString.charAt(0) == quotedString.charAt(quotedString.length() - 1) &&
                (quotedString.charAt(0) == '"' || quotedString.charAt(0) == '\''));
        return quotedString.substring(1, quotedString.length() - 1);

    }

    /**
     * Accepts a TerminalNode of type STRING as a parameter and returns the
     * content of the string, without quotes
     * 
     * @param node The terminal node
     *
     * @return A string with the content
     */
    public String getString(TerminalNode node) {
        if (node != null && node.getText() != null) {
            return this.getString(node.getText());
        } else {
            return null;
        }
    }

    /**
     * Parses a metadata string with a $ and returns the metadata name without
     * the $
     * 
     * @param $metadataName A string with the metadata name, including the
     * dollar sign
     *
     * @return A string with the metadata name, excluding the first position
     * ($).
     */
    public String getMetadataName(String $metadataName) {
        assert ($metadataName != null && $metadataName.length() >= 1 && $metadataName.charAt(0) == '$');
        return $metadataName.substring(1);
    }

    /**
     * Accepts a TerminalNode of type METADATA as a parameter and returns the
     * content of the metadata name, without the preceding dollar sign
     * 
     * @param node The terminal node
     *
     * @return A string with the metadata name
     */
    public String getMetadataName(TerminalNode node) {
        if (node != null) {
            String $metadataName = node.getText();
            return this.getMetadataName($metadataName);
        } else {
            return null;
        }
    }
}
