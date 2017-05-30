
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
 *         &lt;element name="AcctAddRq" type="{urn://grupoaval.com/telepeajes/accounts/v1/}AcctAddRq_Type"/>
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
    "acctAddRq"
})
@XmlRootElement(name = "addAccount")
public class AddAccount {

    @XmlElement(name = "AcctAddRq", required = true)
    protected AcctAddRqType acctAddRq;

    /**
     * Gets the value of the acctAddRq property.
     * 
     * @return
     *     possible object is
     *     {@link AcctAddRqType }
     *     
     */
    public AcctAddRqType getAcctAddRq() {
        return acctAddRq;
    }

    /**
     * Sets the value of the acctAddRq property.
     * 
     * @param value
     *     allowed object is
     *     {@link AcctAddRqType }
     *     
     */
    public void setAcctAddRq(AcctAddRqType value) {
        this.acctAddRq = value;
    }

}
