
package com.grupoaval.telepeajes.payments.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RechargeAddRs" type="{urn://grupoaval.com/telepeajes/payments/v1/}RechargeAddRs_Type"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "rechargeAddRs"
})
@XmlRootElement(name = "addRechargeResponse")
public class AddRechargeResponse {

    @XmlElement(name = "RechargeAddRs", required = true)
    protected RechargeAddRsType rechargeAddRs;

    /**
     * Gets the value of the rechargeAddRs property.
     * 
     * @return
     *     possible object is
     *     {@link RechargeAddRsType }
     *     
     */
    public RechargeAddRsType getRechargeAddRs() {
        return rechargeAddRs;
    }

    /**
     * Sets the value of the rechargeAddRs property.
     * 
     * @param value
     *     allowed object is
     *     {@link RechargeAddRsType }
     *     
     */
    public void setRechargeAddRs(RechargeAddRsType value) {
        this.rechargeAddRs = value;
    }

}
