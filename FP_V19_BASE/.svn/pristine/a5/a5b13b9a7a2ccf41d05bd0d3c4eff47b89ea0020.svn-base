
package wsba.cpv.types;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BankListOut complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BankListOut">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="bancoACHData" type="{http://com/ath/service/payments/pseservices}BankACHData" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BankListOut", propOrder = {
    "bancoACHData"
})
public class BankListOut {

    @XmlElement(nillable = true)
    protected List<BankACHData> bancoACHData;

    /**
     * Gets the value of the bancoACHData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bancoACHData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBancoACHData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BankACHData }
     * 
     * 
     */
    public List<BankACHData> getBancoACHData() {
        if (bancoACHData == null) {
            bancoACHData = new ArrayList<BankACHData>();
        }
        return this.bancoACHData;
    }

}
