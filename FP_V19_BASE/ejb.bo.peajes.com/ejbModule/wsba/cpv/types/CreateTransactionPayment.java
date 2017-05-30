
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
 *         &lt;element name="transactionPaymentInp" type="{http://com/ath/service/payments/pseservices}TransactionPaymentInp"/>
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
    "transactionPaymentInp"
})
@XmlRootElement(name = "createTransactionPayment")
public class CreateTransactionPayment {

    @XmlElement(required = true)
    protected TransactionPaymentInp transactionPaymentInp;

    /**
     * Gets the value of the transactionPaymentInp property.
     * 
     * @return
     *     possible object is
     *     {@link TransactionPaymentInp }
     *     
     */
    public TransactionPaymentInp getTransactionPaymentInp() {
        return transactionPaymentInp;
    }

    /**
     * Sets the value of the transactionPaymentInp property.
     * 
     * @param value
     *     allowed object is
     *     {@link TransactionPaymentInp }
     *     
     */
    public void setTransactionPaymentInp(TransactionPaymentInp value) {
        this.transactionPaymentInp = value;
    }

}
