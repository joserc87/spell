package nl.thedocumentwizard.wizardconfiguration;

/**
 * A wrapper class for the RadioControl
 */
public class RadioControl extends nl.thedocumentwizard.wizardconfiguration.jaxb.RadioControl {

    String aliasDefaultValue;

    public String getAliasDefaultValue() {
        return aliasDefaultValue;
    }

    public void setAliasDefaultValue(String aliasDefaultValue) {
        this.aliasDefaultValue = aliasDefaultValue;
    }
}
