package nl.thedocumentwizard.wizardconfiguration;

import com.sun.xml.txw2.output.IndentingXMLStreamWriter;
import nl.thedocumentwizard.wizardconfiguration.jaxb.Wizard;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.OutputStream;

/**
 * Adds functionality to the JAXB class
 */
public class WizardConfiguration extends Wizard {

    public WizardConfiguration() {
        super();
    }

    /**
     * Writes a wizard to an XML file in compact format.
     *
     * Like marshall(File, boolean), but passing false for prettyPrint
     */
    public boolean marshall(OutputStream os) {
        return this.marshall(os, false, null);
    }
    /**
     * Writes a wizard into an XML file.
     *
     * @param os The output stream to write the XML
     * @param prettyPrint Output a formatted XML? false by default
     * @param comments The comments to output in the preamble of the XML. Pass null to omit the comments.
     * @category IO
     */
    public boolean marshall(OutputStream os, boolean prettyPrint, String comments) {
        boolean ok = false;
        try{
            //Prepare JAXB objects
            JAXBContext jc = JAXBContext.newInstance(Wizard.class);
            Marshaller m = jc.createMarshaller();

            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            //Create a new org.dom4j.io.XMLWriter that will serve as the
            //ContentHandler for our filter.
            XMLStreamWriter strWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(os, "UTF-8");
            if (prettyPrint) {
                strWriter = new IndentingXMLStreamWriter(strWriter);
            }
            if (comments != null) {
                m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
                strWriter.writeProcessingInstruction("xml version=\"1.0\"");
                strWriter.writeCharacters("\n");
                strWriter.writeComment(comments);
                strWriter.writeCharacters("\n");
            }
            //Tell JAXB to marshall to the filter which in turn will call the writer
            m.marshal(this, strWriter);
            ok = true;
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
