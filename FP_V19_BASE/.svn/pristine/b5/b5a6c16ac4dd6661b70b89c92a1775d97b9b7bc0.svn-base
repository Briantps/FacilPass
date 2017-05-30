
package wsba.cpv.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for clsEstadoTransaccion complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="clsEstadoTransaccion">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ws.cpv.avvillas.com/}clsRespuestaWsCpv">
 *       &lt;sequence>
 *         &lt;element name="aprobado" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="fechaFinTransaccion" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="fechaTransaccion" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="transaccionId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "clsEstadoTransaccion", namespace = "http://ws.cpv.avvillas.com/", propOrder = {
    "aprobado",
    "fechaFinTransaccion",
    "fechaTransaccion",
    "transaccionId"
})
public class ClsEstadoTransaccion
    extends ClsRespuestaWsCpv
{

    protected Boolean aprobado;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaFinTransaccion;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaTransaccion;
    protected Integer transaccionId;

    /**
     * Gets the value of the aprobado property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAprobado() {
        return aprobado;
    }

    /**
     * Sets the value of the aprobado property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAprobado(Boolean value) {
        this.aprobado = value;
    }

    /**
     * Gets the value of the fechaFinTransaccion property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaFinTransaccion() {
        return fechaFinTransaccion;
    }

    /**
     * Sets the value of the fechaFinTransaccion property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaFinTransaccion(XMLGregorianCalendar value) {
        this.fechaFinTransaccion = value;
    }

    /**
     * Gets the value of the fechaTransaccion property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaTransaccion() {
        return fechaTransaccion;
    }

    /**
     * Sets the value of the fechaTransaccion property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaTransaccion(XMLGregorianCalendar value) {
        this.fechaTransaccion = value;
    }

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

}
