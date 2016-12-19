package nl.thedocumentwizard.spell.parser;

import nl.thedocumentwizard.wizardconfiguration.jaxb.AbstractControl;
import nl.thedocumentwizard.wizardconfiguration.jaxb.ObjectFactory;

import java.lang.reflect.InvocationTargetException;

/**
 * Parses ANTLR controls into WizardControls
 */
public class ControlParser {
    private ObjectFactory objectFactory;
    private ParsingHelper helper;

    public ControlParser(ObjectFactory factory, ParsingHelper helper) {
        this.objectFactory = factory;
        this.helper = helper;
    }

    /**
     * Sets the common attributes for all controls using reflection:
     * 
     * @param ctx The context object. It contains the default value / default value metadata, the metadata, etc.
     * @param control The abstract control to set
     */
    public void setAbstractControl(Object ctx, AbstractControl control) {
        // Default value (control "question" = defautValue)
        try {
            SpellParser.Default_valueContext defaultValue = (SpellParser.Default_valueContext) ctx.getClass().getMethod("default_value").invoke(ctx);
            if (defaultValue != null) {
                String value = null;
                String metadataName = null;
                if (defaultValue.literal() != null) {
                    String stringValue = helper.getString(defaultValue.literal().STRING());
                    value = stringValue;
                } else {
                    metadataName = helper.getMetadataName(defaultValue.METADATA());
                }
                if (value != null) {
                    control.setDefaultValue(value);
                } else if (metadataName != null) {
                    control.setDefaultValueMetadataName(metadataName);
                }
            }
            // Metadata:
            SpellParser.Ctrl_metadataContext ctrlMetadata = (SpellParser.Ctrl_metadataContext) ctx.getClass().getMethod("ctrl_metadata").invoke(ctx);
            if (ctrlMetadata != null) {
                String metadataName = null;
                if (ctrlMetadata.METADATA() != null) {
                    metadataName = helper.getMetadataName(ctrlMetadata.METADATA());
                }
                if (metadataName != null) {
                    control.setMetadataName(metadataName);
                }
            }
        } catch (NoSuchMethodException e) {
            // It's ok if the method does not exist
        } catch (InvocationTargetException e) {
            // It's ok if the method does not exist
        } catch (IllegalAccessException e) {
            // It's ok if the method does not exist
        }
    }
}
