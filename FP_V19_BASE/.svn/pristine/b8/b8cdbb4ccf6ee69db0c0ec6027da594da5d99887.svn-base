
package com.grupoaval.telepeajes.xsd.ifx;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CustId_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CustId_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CustPermId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CustLoginId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GovIssueIdent" type="{urn://grupoaval.com/telepeajes/xsd/ifx/}GovIssueIdent_Type"/>
 *         &lt;element name="CustName" type="{urn://grupoaval.com/telepeajes/xsd/ifx/}CustName_Type" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CustId_Type", propOrder = {
    "custPermId",
    "custLoginId",
    "govIssueIdent",
    "custName"
})
@XmlSeeAlso({
    UserIdType.class
})
public class CustIdType {

    @XmlElement(name = "CustPermId")
    protected String custPermId;
    @XmlElement(name = "CustLoginId")
    protected String custLoginId;
    @XmlElement(name = "GovIssueIdent", required = true)
    protected GovIssueIdentType govIssueIdent;
    @XmlElement(name = "CustName")
    protected CustNameType custName;

    /**
     * Gets the value of the custPermId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustPermId() {
        return custPermId;
    }

    /**
     * Sets the value of the custPermId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustPermId(String value) {
        this.custPermId = value;
    }

    /**
     * Gets the value of the custLoginId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustLoginId() {
        return custLoginId;
    }

    /**
     * Sets the value of the custLoginId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustLoginId(String value) {
        this.custLoginId = value;
    }

    /**
     * Gets the value of the govIssueIdent property.
     * 
     * @return
     *     possible object is
     *     {@link GovIssueIdentType }
     *     
     */
    public GovIssueIdentType getGovIssueIdent() {
        return govIssueIdent;
    }

    /**
     * Sets the value of the govIssueIdent property.
     * 
     * @param value
     *     allowed object is
     *     {@link GovIssueIdentType }
     *     
     */
    public void setGovIssueIdent(GovIssueIdentType value) {
        this.govIssueIdent = value;
    }

    /**
     * Gets the value of the custName property.
     * 
     * @return
     *     possible object is
     *     {@link CustNameType }
     *     
     */
    public CustNameType getCustName() {
        return custName;
    }

    /**
     * Sets the value of the custName property.
     * 
     * @param value
     *     allowed object is
     *     {@link CustNameType }
     *     
     */
    public void setCustName(CustNameType value) {
        this.custName = value;
    }

}
