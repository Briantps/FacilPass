
package wsba.cpv.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for finalizarTransaccion complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="finalizarTransaccion">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="transaccionId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "finalizarTransaccion", namespace = "http://ws.cpv.avvillas.com/", propOrder = {
    "transaccionId"
})
public class FinalizarTransaccion {

    protected int transaccionId;

    /**
     * Gets the value of the transaccionId property.
     * 
     */
    public int getTransaccionId() {
        return transaccionId;
    }

    /**
     * Sets the value of the transaccionId property.
     * 
     */
    public void setTransaccionId(int value) {
        this.transaccionId = value;
    }

}
