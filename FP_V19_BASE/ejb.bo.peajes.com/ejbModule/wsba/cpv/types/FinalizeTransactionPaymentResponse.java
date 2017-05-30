
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
 *         &lt;element name="finalizeTransactionPaymentOut" type="{http://com/ath/service/payments/pseservices}FinalizeTransactionPaymentOut"/>
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
    "finalizeTransactionPaymentOut"
})
@XmlRootElement(name = "finalizeTransactionPaymentResponse")
public class FinalizeTransactionPaymentResponse {

    @XmlElement(required = true)
    protected FinalizeTransactionPaymentOut finalizeTransactionPaymentOut;

    /**
     * Gets the value of the finalizeTransactionPaymentOut property.
     * 
     * @return
     *     possible object is
     *     {@link FinalizeTransactionPaymentOut }
     *     
     */
    public FinalizeTransactionPaymentOut getFinalizeTransactionPaymentOut() {
        return finalizeTransactionPaymentOut;
    }

    /**
     * Sets the value of the finalizeTransactionPaymentOut property.
     * 
     * @param value
     *     allowed object is
     *     {@link FinalizeTransactionPaymentOut }
     *     
     */
    public void setFinalizeTransactionPaymentOut(FinalizeTransactionPaymentOut value) {
        this.finalizeTransactionPaymentOut = value;
    }

}
