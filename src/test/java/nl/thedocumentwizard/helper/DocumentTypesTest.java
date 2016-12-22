package nl.thedocumentwizard.helper;

import org.junit.Assert;
import org.junit.Test;

import javax.print.Doc;
import java.io.File;
import java.io.FileInputStream;

import static org.junit.Assert.*;

/**
 * Created by jose on 22/12/2016.
 */
public class DocumentTypesTest {

    @Test
    public void testUnmarshall() throws Exception {
        File documentTypes = new File(getClass().getResource("/documenttypes.xml").getFile());
        FileInputStream is = new FileInputStream(documentTypes);
        DocumentTypes docTypes = DocumentTypes.unmarshall(is);

        Assert.assertEquals(1 ,docTypes.getDocumentTypes().getDocumentType().size());
        Assert.assertEquals("TCFDemo" ,docTypes.getDocumentTypes().getDocumentType().get(0).getName());
        Assert.assertEquals("f96680fd-e945-4d05-a977-e211106edbed" ,docTypes.getDocumentTypes().getDocumentType().get(0).getGuid());
    }
}