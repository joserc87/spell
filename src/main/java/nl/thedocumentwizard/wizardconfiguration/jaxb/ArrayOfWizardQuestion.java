//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.12.04 at 07:42:22 PM CET 
//


package nl.thedocumentwizard.wizardconfiguration.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfWizardQuestion complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfWizardQuestion">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Question" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;choice>
 *                     &lt;element name="Date" type="{http://www.thedocumentwizard.nl/wizardconfiguration/2.0}DateControl" minOccurs="0"/>
 *                     &lt;element name="Attachment" type="{http://www.thedocumentwizard.nl/wizardconfiguration/2.0}AttachmentFileControl" minOccurs="0"/>
 *                     &lt;element name="Multi" type="{http://www.thedocumentwizard.nl/wizardconfiguration/2.0}MultiControl" minOccurs="0"/>
 *                     &lt;element name="Number" type="{http://www.thedocumentwizard.nl/wizardconfiguration/2.0}NumberControl" minOccurs="0"/>
 *                     &lt;element name="Radio" type="{http://www.thedocumentwizard.nl/wizardconfiguration/2.0}RadioControl" minOccurs="0"/>
 *                     &lt;element name="String" type="{http://www.thedocumentwizard.nl/wizardconfiguration/2.0}StringControl" minOccurs="0"/>
 *                     &lt;element name="Email" type="{http://www.thedocumentwizard.nl/wizardconfiguration/2.0}EmailControl" minOccurs="0"/>
 *                     &lt;element name="Image" type="{http://www.thedocumentwizard.nl/wizardconfiguration/2.0}ImageFileControl" minOccurs="0"/>
 *                     &lt;element name="Checkbox" type="{http://www.thedocumentwizard.nl/wizardconfiguration/2.0}CheckboxControl" minOccurs="0"/>
 *                     &lt;element name="Label" type="{http://www.thedocumentwizard.nl/wizardconfiguration/2.0}LabelControl" minOccurs="0"/>
 *                     &lt;element name="List" type="{http://www.thedocumentwizard.nl/wizardconfiguration/2.0}ListControl" minOccurs="0"/>
 *                     &lt;element name="Text" type="{http://www.thedocumentwizard.nl/wizardconfiguration/2.0}TextControl" minOccurs="0"/>
 *                   &lt;/choice>
 *                 &lt;/sequence>
 *                 &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfWizardQuestion", propOrder = {
    "question"
})
public class ArrayOfWizardQuestion {

    @XmlElement(name = "Question")
    protected List<ArrayOfWizardQuestion.Question> question;

    /**
     * Gets the value of the question property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the question property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getQuestion().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ArrayOfWizardQuestion.Question }
     * 
     * 
     */
    public List<ArrayOfWizardQuestion.Question> getQuestion() {
        if (question == null) {
            question = new ArrayList<ArrayOfWizardQuestion.Question>();
        }
        return this.question;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;choice>
     *           &lt;element name="Date" type="{http://www.thedocumentwizard.nl/wizardconfiguration/2.0}DateControl" minOccurs="0"/>
     *           &lt;element name="Attachment" type="{http://www.thedocumentwizard.nl/wizardconfiguration/2.0}AttachmentFileControl" minOccurs="0"/>
     *           &lt;element name="Multi" type="{http://www.thedocumentwizard.nl/wizardconfiguration/2.0}MultiControl" minOccurs="0"/>
     *           &lt;element name="Number" type="{http://www.thedocumentwizard.nl/wizardconfiguration/2.0}NumberControl" minOccurs="0"/>
     *           &lt;element name="Radio" type="{http://www.thedocumentwizard.nl/wizardconfiguration/2.0}RadioControl" minOccurs="0"/>
     *           &lt;element name="String" type="{http://www.thedocumentwizard.nl/wizardconfiguration/2.0}StringControl" minOccurs="0"/>
     *           &lt;element name="Email" type="{http://www.thedocumentwizard.nl/wizardconfiguration/2.0}EmailControl" minOccurs="0"/>
     *           &lt;element name="Image" type="{http://www.thedocumentwizard.nl/wizardconfiguration/2.0}ImageFileControl" minOccurs="0"/>
     *           &lt;element name="Checkbox" type="{http://www.thedocumentwizard.nl/wizardconfiguration/2.0}CheckboxControl" minOccurs="0"/>
     *           &lt;element name="Label" type="{http://www.thedocumentwizard.nl/wizardconfiguration/2.0}LabelControl" minOccurs="0"/>
     *           &lt;element name="List" type="{http://www.thedocumentwizard.nl/wizardconfiguration/2.0}ListControl" minOccurs="0"/>
     *           &lt;element name="Text" type="{http://www.thedocumentwizard.nl/wizardconfiguration/2.0}TextControl" minOccurs="0"/>
     *         &lt;/choice>
     *       &lt;/sequence>
     *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "date",
        "attachment",
        "multi",
        "number",
        "radio",
        "string",
        "email",
        "image",
        "checkbox",
        "label",
        "list",
        "text"
    })
    public static class Question {

        @XmlElement(name = "Date")
        protected DateControl date;
        @XmlElement(name = "Attachment")
        protected AttachmentFileControl attachment;
        @XmlElement(name = "Multi")
        protected MultiControl multi;
        @XmlElement(name = "Number")
        protected NumberControl number;
        @XmlElement(name = "Radio")
        protected RadioControl radio;
        @XmlElement(name = "String")
        protected StringControl string;
        @XmlElement(name = "Email")
        protected EmailControl email;
        @XmlElement(name = "Image")
        protected ImageFileControl image;
        @XmlElement(name = "Checkbox")
        protected CheckboxControl checkbox;
        @XmlElement(name = "Label")
        protected LabelControl label;
        @XmlElement(name = "List")
        protected ListControl list;
        @XmlElement(name = "Text")
        protected TextControl text;
        @XmlAttribute(name = "id")
        protected String id;
        @XmlAttribute(name = "name")
        protected String name;
        @XmlAttribute(name = "required")
        protected Boolean required;

        /**
         * Gets the value of the date property.
         * 
         * @return
         *     possible object is
         *     {@link DateControl }
         *     
         */
        public DateControl getDate() {
            return date;
        }

        /**
         * Sets the value of the date property.
         * 
         * @param value
         *     allowed object is
         *     {@link DateControl }
         *     
         */
        public void setDate(DateControl value) {
            this.date = value;
        }

        /**
         * Gets the value of the attachment property.
         * 
         * @return
         *     possible object is
         *     {@link AttachmentFileControl }
         *     
         */
        public AttachmentFileControl getAttachment() {
            return attachment;
        }

        /**
         * Sets the value of the attachment property.
         * 
         * @param value
         *     allowed object is
         *     {@link AttachmentFileControl }
         *     
         */
        public void setAttachment(AttachmentFileControl value) {
            this.attachment = value;
        }

        /**
         * Gets the value of the multi property.
         * 
         * @return
         *     possible object is
         *     {@link MultiControl }
         *     
         */
        public MultiControl getMulti() {
            return multi;
        }

        /**
         * Sets the value of the multi property.
         * 
         * @param value
         *     allowed object is
         *     {@link MultiControl }
         *     
         */
        public void setMulti(MultiControl value) {
            this.multi = value;
        }

        /**
         * Gets the value of the number property.
         * 
         * @return
         *     possible object is
         *     {@link NumberControl }
         *     
         */
        public NumberControl getNumber() {
            return number;
        }

        /**
         * Sets the value of the number property.
         * 
         * @param value
         *     allowed object is
         *     {@link NumberControl }
         *     
         */
        public void setNumber(NumberControl value) {
            this.number = value;
        }

        /**
         * Gets the value of the radio property.
         * 
         * @return
         *     possible object is
         *     {@link RadioControl }
         *     
         */
        public RadioControl getRadio() {
            return radio;
        }

        /**
         * Sets the value of the radio property.
         * 
         * @param value
         *     allowed object is
         *     {@link RadioControl }
         *     
         */
        public void setRadio(RadioControl value) {
            this.radio = value;
        }

        /**
         * Gets the value of the string property.
         * 
         * @return
         *     possible object is
         *     {@link StringControl }
         *     
         */
        public StringControl getString() {
            return string;
        }

        /**
         * Sets the value of the string property.
         * 
         * @param value
         *     allowed object is
         *     {@link StringControl }
         *     
         */
        public void setString(StringControl value) {
            this.string = value;
        }

        /**
         * Gets the value of the email property.
         * 
         * @return
         *     possible object is
         *     {@link EmailControl }
         *     
         */
        public EmailControl getEmail() {
            return email;
        }

        /**
         * Sets the value of the email property.
         * 
         * @param value
         *     allowed object is
         *     {@link EmailControl }
         *     
         */
        public void setEmail(EmailControl value) {
            this.email = value;
        }

        /**
         * Gets the value of the image property.
         * 
         * @return
         *     possible object is
         *     {@link ImageFileControl }
         *     
         */
        public ImageFileControl getImage() {
            return image;
        }

        /**
         * Sets the value of the image property.
         * 
         * @param value
         *     allowed object is
         *     {@link ImageFileControl }
         *     
         */
        public void setImage(ImageFileControl value) {
            this.image = value;
        }

        /**
         * Gets the value of the checkbox property.
         * 
         * @return
         *     possible object is
         *     {@link CheckboxControl }
         *     
         */
        public CheckboxControl getCheckbox() {
            return checkbox;
        }

        /**
         * Sets the value of the checkbox property.
         * 
         * @param value
         *     allowed object is
         *     {@link CheckboxControl }
         *     
         */
        public void setCheckbox(CheckboxControl value) {
            this.checkbox = value;
        }

        /**
         * Gets the value of the label property.
         * 
         * @return
         *     possible object is
         *     {@link LabelControl }
         *     
         */
        public LabelControl getLabel() {
            return label;
        }

        /**
         * Sets the value of the label property.
         * 
         * @param value
         *     allowed object is
         *     {@link LabelControl }
         *     
         */
        public void setLabel(LabelControl value) {
            this.label = value;
        }

        /**
         * Gets the value of the list property.
         * 
         * @return
         *     possible object is
         *     {@link ListControl }
         *     
         */
        public ListControl getList() {
            return list;
        }

        /**
         * Sets the value of the list property.
         * 
         * @param value
         *     allowed object is
         *     {@link ListControl }
         *     
         */
        public void setList(ListControl value) {
            this.list = value;
        }

        /**
         * Gets the value of the text property.
         * 
         * @return
         *     possible object is
         *     {@link TextControl }
         *     
         */
        public TextControl getText() {
            return text;
        }

        /**
         * Sets the value of the text property.
         * 
         * @param value
         *     allowed object is
         *     {@link TextControl }
         *     
         */
        public void setText(TextControl value) {
            this.text = value;
        }

        /**
         * Gets the value of the id property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getId() {
            return id;
        }

        /**
         * Sets the value of the id property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setId(String value) {
            this.id = value;
        }

        /**
         * Gets the value of the name property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getName() {
            return name;
        }

        /**
         * Sets the value of the name property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setName(String value) {
            this.name = value;
        }

        /**
         * Gets the value of the required property.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
         */
        public Boolean isRequired() {
            return required;
        }

        /**
         * Sets the value of the required property.
         * 
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        public void setRequired(Boolean value) {
            this.required = value;
        }

    }

}
