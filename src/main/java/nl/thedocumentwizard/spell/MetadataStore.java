package nl.thedocumentwizard.spell;

import com.streamserve.schemas.documenttype._1.DocumentTypeType;
import com.streamserve.schemas.documenttype._1.MetadataGroupType;
import com.streamserve.schemas.documenttype._1.MetadataTypesType;

import java.util.HashMap;

/**
 * Created by jose on 21/12/2016.
 */
public class MetadataStore {
    public HashMap<String, MetadataTypesType.Metadata> metadataByName;

    public String findMetadataIDByName(String metadataName) {
        if (this.metadataByName != null) {
            MetadataTypesType.Metadata metadata = this.metadataByName.get(metadataName);
            if (metadata != null) {
                return metadata.getGuid();
            }
        }
        return null;
    }

    public void loadDocumentType(DocumentTypeType docType) {
        if (this.metadataByName == null) {
            this.metadataByName = new HashMap<>();
        }
        for (MetadataGroupType metadataGroup : docType.getMetadataGroup()) {
            if (metadataGroup.getMetadataTypes() != null) {
                for (MetadataTypesType.Metadata metadata : metadataGroup.getMetadataTypes().getMetadata()) {
                    this.metadataByName.put(metadata.getName(), metadata);
                }
            }
        }
    }
}
