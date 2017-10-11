package nl.thedocumentwizard.spell;

import com.streamserve.schemas.documenttype._1.DocumentTypeType;
import nl.thedocumentwizard.spell.parser.*;
import nl.thedocumentwizard.wizardconfiguration.MyObjectFactory;
import nl.thedocumentwizard.wizardconfiguration.WizardConfiguration;
import nl.thedocumentwizard.wizardconfiguration.jaxb.ObjectFactory;

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
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


public class Compiler {
    // Options
    boolean prettyPrint;

    InputStream inputStream;
    OutputStream outputStream;
    MetadataStore metadataStore;
    DocumentTypeType documentType;

    // Helpers
    StepAliasHelper aliasHelper;

    public Compiler(InputStream inputStream, OutputStream outputStream, MetadataStore metadataStore, DocumentTypeType documentType, boolean prettyPrint) {
        this.inputStream   = inputStream;
        this.outputStream  = outputStream;
        this.metadataStore = metadataStore;
        this.documentType  = documentType;
        this.prettyPrint   = prettyPrint;
    }

    public WizardConfiguration parseWizard() {
        // Dependencies
        ParsingHelper helper = new ParsingHelper();
        aliasHelper = new StepAliasHelper();
        ObjectFactory factory = new MyObjectFactory();
        ControlParser controlParser = new ControlParser(factory, helper);
        WhenParser whenParser = new WhenParser(factory, helper);

        // Get our lexer
        SpellLexer lexer = null;
        try {
            lexer = new SpellLexer(new ANTLRInputStream(this.inputStream));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error reading input file.");
            return null;
        }

        // Get a list of matched tokens
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // Pass the tokens to the parser
        SpellParser parser = new SpellParser(tokens);

        // Specify our entry point
        SpellParser.WizardContext wizardSentenceContext = parser.wizard();

        // Walk it and attach our listener
        ParseTreeWalker walker = new ParseTreeWalker();
        MySpellListener listener = new MySpellListener(factory, controlParser, whenParser, helper, aliasHelper);
        walker.walk(listener, wizardSentenceContext);

        // Marshall the wizard:
        WizardConfiguration wizard = (WizardConfiguration) listener.getWizard();
        return wizard;
    }

    public void postProcess(WizardConfiguration wizard) {
        // Postprocess the wizard:
        PostProcessor postProcessor = new PostProcessor(this.metadataStore);
        postProcessor.assignStepIDs(wizard);
        postProcessor.assignQuestionIDs(wizard);
        if (this.metadataStore != null) {
            postProcessor.assignMetadataIDs(wizard);
        }
        // Set document type (if specified)
        if (this.documentType != null) {
            wizard.setDocumentTypeName(this.documentType.getName());
            wizard.setDocumentTypeID(this.documentType.getGuid());
        }
        // Link the steps and controls by alias
        postProcessor.resolveAlias(wizard, this.aliasHelper);
    }

    public void writeWizard(WizardConfiguration wizard) {
        String comments = null;
        if (this.prettyPrint) {
            SimpleDateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
            String date = df.format(new Date());
            String username = System.getProperty("user.name");
            String user = username == null || username.length() <= 0 ? "" : "by " + username + " ";
            comments = "\n  WIZARD CONFIGURATION XML." +
                    "\n" +
                    "\n  This file was auto-generated from SPELL" +
                    "\n  " + user + "on " + date + "." +
                    "\n" +
                    "\n  Changes to this file may be overwritten.\n";
        }
        wizard.marshall(this.outputStream, this.prettyPrint, comments);
        try {
            this.outputStream.close();
        } catch (IOException e) {
            System.err.println("Error closing output file");
        }
    }

    public void compile() {
        WizardConfiguration wizard = parseWizard();
        postProcess(wizard);
        writeWizard(wizard);
    }
}
