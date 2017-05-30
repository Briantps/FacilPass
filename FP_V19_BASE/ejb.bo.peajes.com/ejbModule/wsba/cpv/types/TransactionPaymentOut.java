
package wsba.cpv.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for TransactionPaymentOut complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TransactionPaymentOut">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="trazabilityCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="returnCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="bankUrl" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="soliciteDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="transactionCycle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TransactionPaymentOut", propOrder = {
    "trazabilityCode",
    "returnCode",
    "bankUrl",
    "soliciteDate",
    "transactionCycle"
})
public class TransactionPaymentOut {

    protected String trazabilityCode;
    protected String returnCode;
    protected String bankUrl;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar soliciteDate;
    protected String transactionCycle;

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

    /**
     * Gets the value of the bankUrl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBankUrl() {
        return bankUrl;
    }

    /**
     * Sets the value of the bankUrl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBankUrl(String value) {
        this.bankUrl = value;
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

}
