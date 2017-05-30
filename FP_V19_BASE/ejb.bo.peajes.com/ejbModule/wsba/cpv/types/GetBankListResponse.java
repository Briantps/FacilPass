
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
 *         &lt;element name="bankListOut" type="{http://com/ath/service/payments/pseservices}BankListOut"/>
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
    "bankListOut"
})
@XmlRootElement(name = "getBankListResponse")
public class GetBankListResponse {

    @XmlElement(required = true, nillable = true)
    protected BankListOut bankListOut;

    /**
     * Gets the value of the bankListOut property.
     * 
     * @return
     *     possible object is
     *     {@link BankListOut }
     *     
     */
    public BankListOut getBankListOut() {
        return bankListOut;
    }

    /**
     * Sets the value of the bankListOut property.
     * 
     * @param value
     *     allowed object is
     *     {@link BankListOut }
     *     
     */
    public void setBankListOut(BankListOut value) {
        this.bankListOut = value;
    }

}
