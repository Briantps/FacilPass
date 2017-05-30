
package wsba.cpv.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="transactionInformationInp" type="{http://com/ath/service/payments/pseservices}TransactionInformationInp"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "transactionInformationInp"
})
@XmlRootElement(name = "getTransactionInformation")
public class GetTransactionInformation {

    @XmlElement(required = true)
    protected TransactionInformationInp transactionInformationInp;

    /**
     * Gets the value of the transactionInformationInp property.
     * 
     * @return
     *     possible object is
     *     {@link TransactionInformationInp }
     *     
     */
    public TransactionInformationInp getTransactionInformationInp() {
        return transactionInformationInp;
    }

    /**
     * Sets the value of the transactionInformationInp property.
     * 
     * @param value
     *     allowed object is
     *     {@link TransactionInformationInp }
     *     
     */
    public void setTransactionInformationInp(TransactionInformationInp value) {
        this.transactionInformationInp = value;
    }

}
