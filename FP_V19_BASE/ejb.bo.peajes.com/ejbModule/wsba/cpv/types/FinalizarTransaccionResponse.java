
package wsba.cpv.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for finalizarTransaccionResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="finalizarTransaccionResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://ws.cpv.avvillas.com/}clsEstadoTransaccion" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "finalizarTransaccionResponse", namespace = "http://ws.cpv.avvillas.com/", propOrder = {
    "_return"
})
public class FinalizarTransaccionResponse {

    @XmlElement(name = "return")
    protected ClsEstadoTransaccion _return;

    /**
     * Gets the value of the return property.
     * 
     * @return
     *     possible object is
     *     {@link ClsEstadoTransaccion }
     *     
     */
    public ClsEstadoTransaccion getReturn() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     * 
     * @param value
     *     allowed object is
     *     {@link ClsEstadoTransaccion }
     *     
     */
    public void setReturn(ClsEstadoTransaccion value) {
        this._return = value;
    }

}
