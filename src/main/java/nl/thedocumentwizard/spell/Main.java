package nl.thedocumentwizard.spell;

import nl.thedocumentwizard.wizardconfiguration.jaxb.ArrayOfSteptype;
import nl.thedocumentwizard.wizardconfiguration.jaxb.ObjectFactory;
import nl.thedocumentwizard.wizardconfiguration.jaxb.Steptype;
import nl.thedocumentwizard.wizardconfiguration.jaxb.Wizard;

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
    public static void main(String args[]) {
        ObjectFactory factory = new ObjectFactory();
        Wizard wizard = factory.createWizard();
        wizard.setSteps(factory.createArrayOfSteptype());

        wizard.getSteps().getStep().add(factory.createSteptype());
        wizard.getSteps().getStep().add(factory.createSteptype());
        System.out.println("Marshalling Wizard");
        Main.writeXML(wizard, new File("output.spl"));
        System.out.println("Finished");
    }

    /**
     * Writes a wizard into an XML file.
     * The file is writed with a filter that will remove all the tcf: namespaces.
     * More info at: http://stackoverflow.com/questions/277502/jaxb-how-to-ignore-namespace-during-unmarshalling-xml-document
     *
     * @param wizard The JAXBObject to write
     * @param wizardFile The file to write
     * @category IO
     */
    public static boolean writeXML (Wizard wizard, File wizardFile){
        boolean ok = false;
        try{
            //Prepare JAXB objects
            JAXBContext jc = JAXBContext.newInstance(Wizard.class);
            Marshaller m = jc.createMarshaller();

            //Create a filter that will remove the xmlns attribute
            //NamespaceFilter outFilter = new NamespaceFilter(null, false);

            //Create a new org.dom4j.io.XMLWriter that will serve as the
            //ContentHandler for our filter.
            XMLStreamWriter strWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(new FileOutputStream(wizardFile), "UTF-8");
            strWriter.setNamespaceContext(
                    new NamespaceContext() {
                        public Iterator getPrefixes(String namespaceURI) {
                            return null;
                        }

                        public String getPrefix(String namespaceURI) {
                            return "";
                        }

                        public String getNamespaceURI(String prefix) {
                            return null;
                        }
                    });

            //Tell JAXB to marshall to the filter which in turn will call the writer
            m.marshal(wizard, strWriter);
            ok = true;
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (XMLStreamException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }catch (JAXBException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return (ok);
    }
}
