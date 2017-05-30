
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
 *         &lt;element name="RechargeNotificationAddRq" type="{urn://grupoaval.com/telepeajes/payments/v1/}RechargeNotificationAddRq_Type"/>
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
    "rechargeNotificationAddRq"
})
@XmlRootElement(name = "notifyRecharge")
public class NotifyRecharge {

    @XmlElement(name = "RechargeNotificationAddRq", required = true)
    protected RechargeNotificationAddRqType rechargeNotificationAddRq;

    /**
     * Gets the value of the rechargeNotificationAddRq property.
     * 
     * @return
     *     possible object is
     *     {@link RechargeNotificationAddRqType }
     *     
     */
    public RechargeNotificationAddRqType getRechargeNotificationAddRq() {
        return rechargeNotificationAddRq;
    }

    /**
     * Sets the value of the rechargeNotificationAddRq property.
     * 
     * @param value
     *     allowed object is
     *     {@link RechargeNotificationAddRqType }
     *     
     */
    public void setRechargeNotificationAddRq(RechargeNotificationAddRqType value) {
        this.rechargeNotificationAddRq = value;
    }

}
