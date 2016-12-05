package nl.thedocumentwizard.wizardconfiguration;

import com.sun.xml.txw2.output.IndentingXMLStreamWriter;
import nl.thedocumentwizard.wizardconfiguration.decorator.WizardDecorator;
import nl.thedocumentwizard.wizardconfiguration.jaxb.Wizard;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Adds functionality to the JAXB class
 */
public class WizardConfiguration extends WizardDecorator {

    public WizardConfiguration(Wizard decoratedObject) {
        super(decoratedObject);
    }

    /**
     * Writes a wizard to an XML file in compact format.
     *
     * Like marshall(File, boolean), but passing false for prettyPrint
     */
    public boolean marshall(File wizardFile) {
        return this.marshall(wizardFile, false);
    }
    /**
     * Writes a wizard into an XML file.
     *
     * @param wizardFile The file to write
     * @param prettyPrint Output a formatted XML? false by default
     * @category IO
     */
    public boolean marshall(File wizardFile, boolean prettyPrint) {
        Wizard wizard = this.getDecoratedWizard();
        boolean ok = false;
        try{
            //Prepare JAXB objects
            JAXBContext jc = JAXBContext.newInstance(Wizard.class);
            Marshaller m = jc.createMarshaller();

            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            //Create a new org.dom4j.io.XMLWriter that will serve as the
            //ContentHandler for our filter.
            XMLStreamWriter strWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(new FileOutputStream(wizardFile), "UTF-8");
            if (prettyPrint) {
                strWriter = new IndentingXMLStreamWriter(strWriter);
            }
            //Tell JAXB to marshall to the filter which in turn will call the writer
            m.marshal(wizard, strWriter);
            ok = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JAXBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return (ok);
    }
}
