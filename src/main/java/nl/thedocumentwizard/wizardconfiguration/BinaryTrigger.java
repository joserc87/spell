//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.12.04 at 06:05:10 PM CET 
//


package nl.thedocumentwizard.wizardconfiguration;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BinaryTrigger complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BinaryTrigger">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.thedocumentwizard.nl/wizardconfiguration/2.0}Trigger">
 *       &lt;sequence>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="Control" type="{http://www.thedocumentwizard.nl/wizardconfiguration/2.0}ControlTriggerValue" minOccurs="0"/>
 *           &lt;element name="Const" type="{http://www.thedocumentwizard.nl/wizardconfiguration/2.0}ConstTriggerValue" minOccurs="0"/>
 *           &lt;element name="Metadata" type="{http://www.thedocumentwizard.nl/wizardconfiguration/2.0}MetadataTriggerValue" minOccurs="0"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *       &lt;attribute name="dataType" type="{http://www.thedocumentwizard.nl/wizardconfiguration/2.0}EDataType" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BinaryTrigger", propOrder = {
    "controlOrConstOrMetadata"
})
@XmlSeeAlso({
    GreaterOrEqualThanComparisonTrigger.class,
    LessThanComparisonTrigger.class,
    GreaterThanComparisonTrigger.class,
    LessOrEqualThanComparisonTrigger.class,
    EqualComparisonTrigger.class,
    DifferentComparisonTrigger.class
})
public abstract class BinaryTrigger
    extends Trigger
{

    @XmlElements({
        @XmlElement(name = "Control", type = ControlTriggerValue.class),
        @XmlElement(name = "Const", type = ConstTriggerValue.class),
        @XmlElement(name = "Metadata", type = MetadataTriggerValue.class)
    })
    protected List<TriggerValue> controlOrConstOrMetadata;
    @XmlAttribute(name = "dataType")
    protected EDataType dataType;

    /**
     * Gets the value of the controlOrConstOrMetadata property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the controlOrConstOrMetadata property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getControlOrConstOrMetadata().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ControlTriggerValue }
     * {@link ConstTriggerValue }
     * {@link MetadataTriggerValue }
     * 
     * 
     */
    public List<TriggerValue> getControlOrConstOrMetadata() {
        if (controlOrConstOrMetadata == null) {
            controlOrConstOrMetadata = new ArrayList<TriggerValue>();
        }
        return this.controlOrConstOrMetadata;
    }

    /**
     * Gets the value of the dataType property.
     * 
     * @return
     *     possible object is
     *     {@link EDataType }
     *     
     */
    public EDataType getDataType() {
        return dataType;
    }

    /**
     * Sets the value of the dataType property.
     * 
     * @param value
     *     allowed object is
     *     {@link EDataType }
     *     
     */
    public void setDataType(EDataType value) {
        this.dataType = value;
    }

}
