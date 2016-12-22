package nl.thedocumentwizard.spell;

import com.streamserve.schemas.documenttype._1.DocumentTypeType;
import nl.thedocumentwizard.helper.DocumentTypes;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;

import static org.junit.Assert.*;

/**
 * Created by jose on 22/12/2016.
 */
public class MetadataStoreTest {

    @Test
    public void testFindMetadataIDByName() throws Exception {
        // Ideally I would create the DocumentTypes manually, but I'm too lazy, so I'll just unmarshall it:
        File documentTypes = new File(getClass().getResource("/documenttypes.xml").getFile());
        FileInputStream is = new FileInputStream(documentTypes);
        DocumentTypes docTypes = DocumentTypes.unmarshall(is);
        DocumentTypeType docType = docTypes.getDocumentTypeByName("TCFDemo");

        MetadataStore metadataStore = new MetadataStore();
        metadataStore.loadDocumentType(docType);

        Assert.assertEquals("C86E986C-2BD7-42E9-80D3-612C0D08A422", metadataStore.findMetadataIDByName("Part.ApplicationDomainID"));
        Assert.assertEquals("111ebf50-7198-422d-9b3a-182f868d9539", metadataStore.findMetadataIDByName("String1"));
        Assert.assertEquals("dd3c545f-2168-4264-ad63-49d6b766606d", metadataStore.findMetadataIDByName("WizardSearch"));
        Assert.assertEquals("67b53f5b-a1e3-46da-aa06-e55c9fff3dba", metadataStore.findMetadataIDByName("Date0"));
    }
}