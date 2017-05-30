
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
 *         &lt;element name="RechargeAddRq" type="{urn://grupoaval.com/telepeajes/payments/v1/}RechargeAddRq_Type"/>
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
    "rechargeAddRq"
})
@XmlRootElement(name = "addRecharge")
public class AddRecharge {

    @XmlElement(name = "RechargeAddRq", required = true)
    protected RechargeAddRqType rechargeAddRq;

    /**
     * Gets the value of the rechargeAddRq property.
     * 
     * @return
     *     possible object is
     *     {@link RechargeAddRqType }
     *     
     */
    public RechargeAddRqType getRechargeAddRq() {
        return rechargeAddRq;
    }

    /**
     * Sets the value of the rechargeAddRq property.
     * 
     * @param value
     *     allowed object is
     *     {@link RechargeAddRqType }
     *     
     */
    public void setRechargeAddRq(RechargeAddRqType value) {
        this.rechargeAddRq = value;
    }

}
