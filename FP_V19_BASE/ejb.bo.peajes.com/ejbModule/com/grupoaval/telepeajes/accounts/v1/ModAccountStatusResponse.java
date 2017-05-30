
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
 *         &lt;element name="AcctStatusModRs" type="{urn://grupoaval.com/telepeajes/accounts/v1/}AcctStatusModRs_Type"/>
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
    "acctStatusModRs"
})
@XmlRootElement(name = "modAccountStatusResponse")
public class ModAccountStatusResponse {

    @XmlElement(name = "AcctStatusModRs", required = true)
    protected AcctStatusModRsType acctStatusModRs;

    /**
     * Gets the value of the acctStatusModRs property.
     * 
     * @return
     *     possible object is
     *     {@link AcctStatusModRsType }
     *     
     */
    public AcctStatusModRsType getAcctStatusModRs() {
        return acctStatusModRs;
    }

    /**
     * Sets the value of the acctStatusModRs property.
     * 
     * @param value
     *     allowed object is
     *     {@link AcctStatusModRsType }
     *     
     */
    public void setAcctStatusModRs(AcctStatusModRsType value) {
        this.acctStatusModRs = value;
    }

}
