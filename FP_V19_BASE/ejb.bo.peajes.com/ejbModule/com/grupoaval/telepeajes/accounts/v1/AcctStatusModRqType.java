
package com.grupoaval.telepeajes.accounts.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.grupoaval.telepeajes.xsd.ifx.DepAcctIdType;
import com.grupoaval.telepeajes.xsd.ifx.DepAcctStatusType;
import com.grupoaval.telepeajes.xsd.ifx.SvcRqType;


/**
 * <p>Java class for AcctStatusModRq_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AcctStatusModRq_Type">
 *   &lt;complexContent>
 *     &lt;extension base="{urn://grupoaval.com/telepeajes/xsd/ifx/}SvcRq_Type">
 *       &lt;sequence>
 *         &lt;element name="DepAcctIdTo" type="{urn://grupoaval.com/telepeajes/xsd/ifx/}DepAcctId_Type" minOccurs="0"/>
 *         &lt;element name="DepAcctIdFrom" type="{urn://grupoaval.com/telepeajes/xsd/ifx/}DepAcctId_Type" minOccurs="0"/>
 *         &lt;element name="DepAcctStatus" type="{urn://grupoaval.com/telepeajes/xsd/ifx/}DepAcctStatus_Type" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AcctStatusModRq_Type", propOrder = {
    "depAcctIdTo",
    "depAcctIdFrom",
    "depAcctStatus"
})
public class AcctStatusModRqType
    extends SvcRqType
{

    @XmlElement(name = "DepAcctIdTo")
    protected DepAcctIdType depAcctIdTo;
    @XmlElement(name = "DepAcctIdFrom")
    protected DepAcctIdType depAcctIdFrom;
    @XmlElement(name = "DepAcctStatus")
    protected DepAcctStatusType depAcctStatus;

    /**
     * Gets the value of the depAcctIdTo property.
     * 
     * @return
     *     possible object is
     *     {@link DepAcctIdType }
     *     
     */
    public DepAcctIdType getDepAcctIdTo() {
        return depAcctIdTo;
    }

    /**
     * Sets the value of the depAcctIdTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link DepAcctIdType }
     *     
     */
    public void setDepAcctIdTo(DepAcctIdType value) {
        this.depAcctIdTo = value;
    }

    /**
     * Gets the value of the depAcctIdFrom property.
     * 
     * @return
     *     possible object is
     *     {@link DepAcctIdType }
     *     
     */
    public DepAcctIdType getDepAcctIdFrom() {
        return depAcctIdFrom;
    }

    /**
     * Sets the value of the depAcctIdFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link DepAcctIdType }
     *     
     */
    public void setDepAcctIdFrom(DepAcctIdType value) {
        this.depAcctIdFrom = value;
    }

    /**
     * Gets the value of the depAcctStatus property.
     * 
     * @return
     *     possible object is
     *     {@link DepAcctStatusType }
     *     
     */
    public DepAcctStatusType getDepAcctStatus() {
        return depAcctStatus;
    }

    /**
     * Sets the value of the depAcctStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link DepAcctStatusType }
     *     
     */
    public void setDepAcctStatus(DepAcctStatusType value) {
        this.depAcctStatus = value;
    }

}
