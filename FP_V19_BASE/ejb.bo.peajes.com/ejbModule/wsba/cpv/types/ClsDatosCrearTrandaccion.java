
package wsba.cpv.types;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for clsDatosCrearTrandaccion complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="clsDatosCrearTrandaccion">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="convenioId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="correo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descripcion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numeroDocumento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="pseCodigoBanco" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="pseUserType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="referenciaPago1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="referenciaPago2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="referenciaPago3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="referenciaPago4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoDocumentoId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="urlRespuesta" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="valor" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "clsDatosCrearTrandaccion", namespace = "http://ws.cpv.avvillas.com/", propOrder = {
    "convenioId",
    "correo",
    "descripcion",
    "numeroDocumento",
    "pseCodigoBanco",
    "pseUserType",
    "referenciaPago1",
    "referenciaPago2",
    "referenciaPago3",
    "referenciaPago4",
    "tipoDocumentoId",
    "urlRespuesta",
    "valor"
})
public class ClsDatosCrearTrandaccion {

    protected int convenioId;
    protected String correo;
    protected String descripcion;
    protected String numeroDocumento;
    protected String pseCodigoBanco;
    protected String pseUserType;
    protected String referenciaPago1;
    protected String referenciaPago2;
    protected String referenciaPago3;
    protected String referenciaPago4;
    protected Integer tipoDocumentoId;
    protected String urlRespuesta;
    protected BigDecimal valor;

    /**
     * Gets the value of the convenioId property.
     * 
     */
    public int getConvenioId() {
        return convenioId;
    }

    /**
     * Sets the value of the convenioId property.
     * 
     */
    public void setConvenioId(int value) {
        this.convenioId = value;
    }

    /**
     * Gets the value of the correo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * Sets the value of the correo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCorreo(String value) {
        this.correo = value;
    }

    /**
     * Gets the value of the descripcion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Sets the value of the descripcion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcion(String value) {
        this.descripcion = value;
    }

    /**
     * Gets the value of the numeroDocumento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    /**
     * Sets the value of the numeroDocumento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroDocumento(String value) {
        this.numeroDocumento = value;
    }

    /**
     * Gets the value of the pseCodigoBanco property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPseCodigoBanco() {
        return pseCodigoBanco;
    }

    /**
     * Sets the value of the pseCodigoBanco property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPseCodigoBanco(String value) {
        this.pseCodigoBanco = value;
    }

    /**
     * Gets the value of the pseUserType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPseUserType() {
        return pseUserType;
    }

    /**
     * Sets the value of the pseUserType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPseUserType(String value) {
        this.pseUserType = value;
    }

    /**
     * Gets the value of the referenciaPago1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReferenciaPago1() {
        return referenciaPago1;
    }

    /**
     * Sets the value of the referenciaPago1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReferenciaPago1(String value) {
        this.referenciaPago1 = value;
    }

    /**
     * Gets the value of the referenciaPago2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReferenciaPago2() {
        return referenciaPago2;
    }

    /**
     * Sets the value of the referenciaPago2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReferenciaPago2(String value) {
        this.referenciaPago2 = value;
    }

    /**
     * Gets the value of the referenciaPago3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReferenciaPago3() {
        return referenciaPago3;
    }

    /**
     * Sets the value of the referenciaPago3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReferenciaPago3(String value) {
        this.referenciaPago3 = value;
    }

    /**
     * Gets the value of the referenciaPago4 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReferenciaPago4() {
        return referenciaPago4;
    }

    /**
     * Sets the value of the referenciaPago4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReferenciaPago4(String value) {
        this.referenciaPago4 = value;
    }

    /**
     * Gets the value of the tipoDocumentoId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTipoDocumentoId() {
        return tipoDocumentoId;
    }

    /**
     * Sets the value of the tipoDocumentoId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTipoDocumentoId(Integer value) {
        this.tipoDocumentoId = value;
    }

    /**
     * Gets the value of the urlRespuesta property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrlRespuesta() {
        return urlRespuesta;
    }

    /**
     * Sets the value of the urlRespuesta property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrlRespuesta(String value) {
        this.urlRespuesta = value;
    }

    /**
     * Gets the value of the valor property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValor() {
        return valor;
    }

    /**
     * Sets the value of the valor property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValor(BigDecimal value) {
        this.valor = value;
    }

}
