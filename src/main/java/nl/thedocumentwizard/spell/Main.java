package nl.thedocumentwizard.spell;

import nl.thedocumentwizard.spell.parser.MySpellListener;
import nl.thedocumentwizard.spell.parser.antlr.SpellLexer;
import nl.thedocumentwizard.spell.parser.antlr.SpellParser;
import nl.thedocumentwizard.wizardconfiguration.MyObjectFactory;
import nl.thedocumentwizard.wizardconfiguration.WizardConfiguration;
import nl.thedocumentwizard.wizardconfiguration.jaxb.ArrayOfSteptype;
import nl.thedocumentwizard.wizardconfiguration.jaxb.ObjectFactory;
import nl.thedocumentwizard.wizardconfiguration.jaxb.Steptype;
import nl.thedocumentwizard.wizardconfiguration.jaxb.Wizard;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Iterator;

/**
 * Created by jose on 04/12/2016.
 */
public class Main {
    public static void generateSampleXML(String args[]) {
        ObjectFactory factory = new MyObjectFactory();
        WizardConfiguration wizard = (WizardConfiguration) factory.createWizard();
        wizard.setName("My wizard");
        wizard.setSteps(factory.createArrayOfSteptype());

        wizard.getSteps().getStep().add(factory.createSteptype());
        wizard.getSteps().getStep().add(factory.createSteptype());
        System.out.println("Marshalling Wizard");
        wizard.marshall(new File("output.xml"));
        System.out.println("Finished");
    }

    public static void printWizard(String spellSentence) {
        // Get our lexer
        SpellLexer lexer = new SpellLexer(new ANTLRInputStream(spellSentence));

        // Get a list of matched tokens
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // Pass the tokens to the parser
        SpellParser parser = new SpellParser(tokens);

        // Specify our entry point
        SpellParser.WizardContext drinkSentenceContext = parser.wizard();

        // Walk it and attach our listener
        ParseTreeWalker walker = new ParseTreeWalker();
        MySpellListener listener = new MySpellListener();
        walker.walk(listener, drinkSentenceContext);
    }

    public static void main(String args[]) {
        //generateSampleXML(args);
        printWizard("step 'first step':\r\n" +
                "step \"second step\":\n");
    }
}