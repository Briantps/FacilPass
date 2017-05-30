
package wsba.cpv.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for clsRespuestaCrearTransaccion complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="clsRespuestaCrearTransaccion">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ws.cpv.avvillas.com/}clsRespuestaWsCpv">
 *       &lt;sequence>
 *         &lt;element name="transaccionId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="urlRedirigir" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "clsRespuestaCrearTransaccion", namespace = "http://ws.cpv.avvillas.com/", propOrder = {
    "transaccionId",
    "urlRedirigir"
})
public class ClsRespuestaCrearTransaccion
    extends ClsRespuestaWsCpv
{

    protected Integer transaccionId;
    protected String urlRedirigir;

    /**
     * Gets the value of the transaccionId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTransaccionId() {
        return transaccionId;
    }

    /**
     * Sets the value of the transaccionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTransaccionId(Integer value) {
        this.transaccionId = value;
    }

    /**
     * Gets the value of the urlRedirigir property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrlRedirigir() {
        return urlRedirigir;
    }

    /**
     * Sets the value of the urlRedirigir property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrlRedirigir(String value) {
        this.urlRedirigir = value;
    }

}
