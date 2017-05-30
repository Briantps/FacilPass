
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
 *         &lt;element name="transactionInformationOut" type="{http://com/ath/service/payments/pseservices}TransactionInformationOut"/>
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
    "transactionInformationOut"
})
@XmlRootElement(name = "getTransactionInformationResponse")
public class GetTransactionInformationResponse {

    @XmlElement(required = true)
    protected TransactionInformationOut transactionInformationOut;

    /**
     * Gets the value of the transactionInformationOut property.
     * 
     * @return
     *     possible object is
     *     {@link TransactionInformationOut }
     *     
     */
    public TransactionInformationOut getTransactionInformationOut() {
        return transactionInformationOut;
    }

    /**
     * Sets the value of the transactionInformationOut property.
     * 
     * @param value
     *     allowed object is
     *     {@link TransactionInformationOut }
     *     
     */
    public void setTransactionInformationOut(TransactionInformationOut value) {
        this.transactionInformationOut = value;
    }

}
