package nl.thedocumentwizard.spell;

import com.streamserve.schemas.documenttype._1.DocumentTypeType;
import nl.thedocumentwizard.helper.DocumentTypes;
import nl.thedocumentwizard.spell.parser.*;
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
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by jose on 04/12/2016.
 */
public class Main {

    // Arguments
    boolean prettyPrint;
    File documentTypeFile;
    String documentTypeName;
    // Others
    InputStream inputStream;
    OutputStream outputStream;
    MetadataStore metadataStore;
    DocumentTypeType documentType;

    StepAliasHelper aliasHelper;

    public void printUsage() {
        System.out.println(
                ////////////////////////////////////////////////////////////////////////////////
                "Usage: java -jar spell.jar <inputFile.spl> [options...]\n\n" +
                "where options include:\n" +
                "    -h[elp]                            displays this help message\n" +
                "    -v[ersion]                         displays the version string\n" +
                "    -o[utput] <output_file>            output the document type to a file\n" +
                "    -pretty-print                      beautify the output XML\n" +
                "    -document-types-xml <doctypes.xml> uses the document types xml.\n" +
                "    -document-type <documentTypeName>  the name of the document type to use.\n"
                ////////////////////////////////////////////////////////////////////////////////
        );
    }

    public void printVersion() {
        String version = getClass().getPackage().getImplementationVersion();
        if (version == null) {
            version = "DEVELOPMENT";
        }
        System.out.println(version);
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

    public void run() {
        WizardConfiguration wizard = parseWizard();
        postProcess(wizard);
        writeWizard(wizard);
    }

    public boolean parseArgs(String args[]) throws IllegalArgumentException {
        File inputFile = null;
        File outputFile = null;

        this.prettyPrint = false;
        List<String> unknownArgs = new ArrayList<>();

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            if (arg.equals("-h") || arg.equals("-help")) {
                // -h or -help will show usage
                this.printUsage();
                return false;
            } else if (arg.equals("-v") || arg.equals("-version")) {
                // -v or -version will show the version string
                this.printVersion();
                return false;
            } else if (arg.equals("-pretty-print")) {
                this.prettyPrint = true;
            } else if (arg.equals("-o") || arg.equals("-output")) {
                i++;
                if (i < args.length) {
                    outputFile = new File(args[i]);
                } else {
                    throw new IllegalArgumentException("Expected ouput file after option '" + arg + "'");
                }
            } else if (arg.equals("-document-type")) {
                i++;
                if (i < args.length) {
                    documentTypeName = args[i];
                } else {
                    throw new IllegalArgumentException("Expected document type name after option '" + arg + "'");
                }
            } else if (arg.equals("-document-types-xml")) {
                i++;
                if (i < args.length) {
                    documentTypeFile = new File(args[i]);
                } else {
                    throw new IllegalArgumentException("Expected document type file XML after option '" + arg + "'");
                }
            } else if (inputFile == null && arg.charAt(0) != '-') {
                // If it's not an option and the input file has not been specified yet
                inputFile = new File(arg);
            } else {
                unknownArgs.add(arg);
            }
        }
        // If there are unknown arguemnts, throw an excepiton
        if (unknownArgs.size() > 0) {
            String arg = unknownArgs.get(0);
            String message = null;
            if (arg.charAt(0) == '-') {
                message = "Unknown option " + unknownArgs.get(0);
            } else {
                message = "Too many arguments. Unexpected argument '" + unknownArgs.get(0) + "'";
            }
            throw new IllegalArgumentException(message);
        }

        // Make sure that, at least, the input file is given
        if (inputFile == null) {
            this.inputStream = System.in;
        } else {
            try {
                this.inputStream = new FileInputStream(inputFile);
            } catch (FileNotFoundException e) {
                System.err.println("Cannot read input file " + inputFile + ". File does not exist.");
                return false;
            }
        }
        if (outputFile != null) {
            try {
                this.outputStream = new FileOutputStream(outputFile);
            } catch (FileNotFoundException e) {
                System.err.println("Cannot write to output file " + outputFile);
                return false;
            }
        } else {
            // If output not specified, print to stdout
            this.outputStream = System.out;
        }

        // Check that the documentType exists:
        if(!this.loadDocumentType()) {
            return false;
        }

        return true;
    }

    public boolean loadDocumentType() {
        if (this.documentTypeFile != null) {
            try {
                DocumentTypes documentTypes = DocumentTypes.unmarshall(new FileInputStream (this.documentTypeFile));
                if (this.documentTypeName != null) {
                    this.documentType = documentTypes.getDocumentTypeByName(this.documentTypeName);
                    if (this.documentType == null) {
                        System.err.println("Document type '" + this.documentTypeName + "' not found in xml " + this.documentTypeFile + ".");
                        return false;
                    }
                } else {
                    if (documentTypes.getDocumentTypes() != null && documentTypes.getDocumentTypes().getDocumentType().size() >= 1) {
                        this.documentType = documentTypes.getDocumentTypes().getDocumentType().get(0);
                        if (documentTypes.getDocumentTypes().getDocumentType().size() > 1) {
                            System.err.println("Document type name not specified. Using " + this.documentType.getName() + " by default.");
                        }
                    }
                }
                this.metadataStore = new MetadataStore();
                this.metadataStore.loadDocumentType(documentType);
            } catch (FileNotFoundException e) {
                System.err.println("Error reading document types file");
                return true;
            }
        } else if (this.documentTypeName != null) {
            System.err.println("Document type name specified but no documenttypes.xml");
            return false;
        }
        return true;
    }

    public static void main(String args[]) {
        Main program = new Main();

        try {
            if (program.parseArgs(args)) {
                program.run();
            }
        } catch (IllegalArgumentException e) {
            // The user entered some illegal command line arguments
            // Show the message
            System.err.println(e.getMessage() + "\n");
            // Show the usage
            program.printUsage();

        }
    }
}
