
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
 *         &lt;element name="bankListInp" type="{http://com/ath/service/payments/pseservices}BankListInp"/>
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
    "bankListInp"
})
@XmlRootElement(name = "getBankList")
public class GetBankList {

    @XmlElement(required = true, nillable = true)
    protected BankListInp bankListInp;

    /**
     * Gets the value of the bankListInp property.
     * 
     * @return
     *     possible object is
     *     {@link BankListInp }
     *     
     */
    public BankListInp getBankListInp() {
        return bankListInp;
    }

    /**
     * Sets the value of the bankListInp property.
     * 
     * @param value
     *     allowed object is
     *     {@link BankListInp }
     *     
     */
    public void setBankListInp(BankListInp value) {
        this.bankListInp = value;
    }

}
