
package wsba.cpv.types;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for TransactionInformationOut complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TransactionInformationOut">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TicketID" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/>
 *         &lt;element name="trazabilityCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EntityCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="transactionValue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VatValue" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="SoliciteDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="BankProcessDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="transactionCycle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="transactionState" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="returnCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TransactionInformationOut", propOrder = {
    "ticketID",
    "trazabilityCode",
    "entityCode",
    "transactionValue",
    "vatValue",
    "soliciteDate",
    "bankProcessDate",
    "transactionCycle",
    "transactionState",
    "returnCode"
})
public class TransactionInformationOut {

    @XmlElement(name = "TicketID")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger ticketID;
    protected String trazabilityCode;
    @XmlElement(name = "EntityCode")
    protected String entityCode;
    protected String transactionValue;
    @XmlElement(name = "VatValue")
    protected BigDecimal vatValue;
    @XmlElement(name = "SoliciteDate")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar soliciteDate;
    @XmlElement(name = "BankProcessDate")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar bankProcessDate;
    protected String transactionCycle;
    protected String transactionState;
    protected String returnCode;

    /**
     * Gets the value of the ticketID property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTicketID() {
        return ticketID;
    }

    /**
     * Sets the value of the ticketID property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTicketID(BigInteger value) {
        this.ticketID = value;
    }

    /**
     * Gets the value of the trazabilityCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTrazabilityCode() {
        return trazabilityCode;
    }

    /**
     * Sets the value of the trazabilityCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTrazabilityCode(String value) {
        this.trazabilityCode = value;
    }

    /**
     * Gets the value of the entityCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntityCode() {
        return entityCode;
    }

    /**
     * Sets the value of the entityCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntityCode(String value) {
        this.entityCode = value;
    }

    /**
     * Gets the value of the transactionValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactionValue() {
        return transactionValue;
    }

    /**
     * Sets the value of the transactionValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactionValue(String value) {
        this.transactionValue = value;
    }

    /**
     * Gets the value of the vatValue property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getVatValue() {
        return vatValue;
    }

    /**
     * Sets the value of the vatValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setVatValue(BigDecimal value) {
        this.vatValue = value;
    }

    /**
     * Gets the value of the soliciteDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getSoliciteDate() {
        return soliciteDate;
    }

    /**
     * Sets the value of the soliciteDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setSoliciteDate(XMLGregorianCalendar value) {
        this.soliciteDate = value;
    }

    /**
     * Gets the value of the bankProcessDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getBankProcessDate() {
        return bankProcessDate;
    }

    /**
     * Sets the value of the bankProcessDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setBankProcessDate(XMLGregorianCalendar value) {
        this.bankProcessDate = value;
    }

    /**
     * Gets the value of the transactionCycle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactionCycle() {
        return transactionCycle;
    }

    /**
     * Sets the value of the transactionCycle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactionCycle(String value) {
        this.transactionCycle = value;
    }

    /**
     * Gets the value of the transactionState property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactionState() {
        return transactionState;
    }

    /**
     * Sets the value of the transactionState property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactionState(String value) {
        this.transactionState = value;
    }

    /**
     * Gets the value of the returnCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReturnCode() {
        return returnCode;
    }

    /**
     * Sets the value of the returnCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReturnCode(String value) {
        this.returnCode = value;
    }

}
