
package com.grupoaval.telepeajes.payments.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.grupoaval.telepeajes.xsd.ifx.BillPmtInfoType;
import com.grupoaval.telepeajes.xsd.ifx.SvcRqType;


/**
 * <p>Java class for RechargeAddRq_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RechargeAddRq_Type">
 *   &lt;complexContent>
 *     &lt;extension base="{urn://grupoaval.com/telepeajes/xsd/ifx/}SvcRq_Type">
 *       &lt;sequence>
 *         &lt;element name="BillPmtInfo" type="{urn://grupoaval.com/telepeajes/xsd/ifx/}BillPmtInfo_Type" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RechargeAddRq_Type", propOrder = {
    "billPmtInfo"
})
public class RechargeAddRqType
    extends SvcRqType
{

    @XmlElement(name = "BillPmtInfo")
    protected BillPmtInfoType billPmtInfo;

    /**
     * Gets the value of the billPmtInfo property.
     * 
     * @return
     *     possible object is
     *     {@link BillPmtInfoType }
     *     
     */
    public BillPmtInfoType getBillPmtInfo() {
        return billPmtInfo;
    }

    /**
     * Sets the value of the billPmtInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link BillPmtInfoType }
     *     
     */
    public void setBillPmtInfo(BillPmtInfoType value) {
        this.billPmtInfo = value;
    }

}
