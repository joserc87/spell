//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.12.04 at 07:42:22 PM CET 
//


package nl.thedocumentwizard.wizardconfiguration.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RadioControl complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RadioControl">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.thedocumentwizard.nl/wizardconfiguration/2.0}AbstractControl">
 *       &lt;sequence>
 *         &lt;element name="Items" type="{http://www.thedocumentwizard.nl/wizardconfiguration/2.0}ArrayOfChoice1" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RadioControl", propOrder = {
    "items"
})
public class RadioControl
    extends AbstractControl
{

    @XmlElement(name = "Items")
    protected ArrayOfChoice1 items;

    /**
     * Gets the value of the items property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfChoice1 }
     *     
     */
    public ArrayOfChoice1 getItems() {
        return items;
    }

    /**
     * Sets the value of the items property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfChoice1 }
     *     
     */
    public void setItems(ArrayOfChoice1 value) {
        this.items = value;
    }

}