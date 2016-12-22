package nl.thedocumentwizard.helper;

import com.streamserve.schemas.documenttype._1.DocumentTypeType;
import com.streamserve.schemas.documenttype._1.Strs;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.print.Doc;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.transform.sax.SAXSource;
import java.io.InputStream;

/**
 * Unmarshalls the documentTypesXML
 * It is needed to subclass Strs to change the namespace for unmarshalling
 */
@XmlRootElement(name = "strs", namespace = "http://schemas.streamserve.com/designcenterresource/2.0")
public class DocumentTypes extends Strs{
    /**
     * Reads a document types XML and returns a JAXB object.
     *
     * @param inputStream The stream to read
     * @return A JAXB object
     * @category IO
     */
    public static DocumentTypes unmarshall (InputStream inputStream){
        DocumentTypes root = null;
        try{
            // Prepare JAXB objects
            JAXBContext jaxbContext = JAXBContext.newInstance(DocumentTypes.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            // Create an XMLReader to use with the filter
            XMLReader reader = XMLReaderFactory.createXMLReader();

            // Prepare the input, in this case a java.io.File (output)
            InputSource is = new InputSource(inputStream);

            // Create a SAXSource specifing the filter
            SAXSource source = new SAXSource (is);

            // Unmarshall
            root = (DocumentTypes) jaxbUnmarshaller.unmarshal(source);
        }catch (JAXBException e){
            root = null;
            e.printStackTrace();
        }catch (SAXException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return root;
    }

    public DocumentTypeType getDocumentTypeByName(String tcfDemo) {
        if (documentTypes != null) {
            for (DocumentTypeType docType : documentTypes.getDocumentType()) {
                if (docType.getName().equals(tcfDemo)) {
                    return docType;
                }
            }
        }
        return null;
    }
}
