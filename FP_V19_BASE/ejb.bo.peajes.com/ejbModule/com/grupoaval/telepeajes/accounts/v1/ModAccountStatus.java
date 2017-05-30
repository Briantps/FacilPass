
package com.grupoaval.telepeajes.accounts.v1;

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
 *         &lt;element name="AcctStatusModRq" type="{urn://grupoaval.com/telepeajes/accounts/v1/}AcctStatusModRq_Type"/>
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
    "acctStatusModRq"
})
@XmlRootElement(name = "modAccountStatus")
public class ModAccountStatus {

    @XmlElement(name = "AcctStatusModRq", required = true)
    protected AcctStatusModRqType acctStatusModRq;

    /**
     * Gets the value of the acctStatusModRq property.
     * 
     * @return
     *     possible object is
     *     {@link AcctStatusModRqType }
     *     
     */
    public AcctStatusModRqType getAcctStatusModRq() {
        return acctStatusModRq;
    }

    /**
     * Sets the value of the acctStatusModRq property.
     * 
     * @param value
     *     allowed object is
     *     {@link AcctStatusModRqType }
     *     
     */
    public void setAcctStatusModRq(AcctStatusModRqType value) {
        this.acctStatusModRq = value;
    }

}
