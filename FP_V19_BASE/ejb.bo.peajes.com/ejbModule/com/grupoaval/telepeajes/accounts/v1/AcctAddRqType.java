
package com.grupoaval.telepeajes.accounts.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.grupoaval.telepeajes.xsd.ifx.DepAcctIdType;
import com.grupoaval.telepeajes.xsd.ifx.SvcRqType;


/**
 * <p>Java class for AcctAddRq_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AcctAddRq_Type">
 *   &lt;complexContent>
 *     &lt;extension base="{urn://grupoaval.com/telepeajes/xsd/ifx/}SvcRq_Type">
 *       &lt;sequence>
 *         &lt;element name="DepAcctId" type="{urn://grupoaval.com/telepeajes/xsd/ifx/}DepAcctId_Type" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AcctAddRq_Type", propOrder = {
    "depAcctId"
})
public class AcctAddRqType
    extends SvcRqType
{

    @XmlElement(name = "DepAcctId")
    protected DepAcctIdType depAcctId;

    /**
     * Gets the value of the depAcctId property.
     * 
     * @return
     *     possible object is
     *     {@link DepAcctIdType }
     *     
     */
    public DepAcctIdType getDepAcctId() {
        return depAcctId;
    }

    /**
     * Sets the value of the depAcctId property.
     * 
     * @param value
     *     allowed object is
     *     {@link DepAcctIdType }
     *     
     */
    public void setDepAcctId(DepAcctIdType value) {
        this.depAcctId = value;
    }

}
