
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
 *         &lt;element name="AcctAddRs" type="{urn://grupoaval.com/telepeajes/accounts/v1/}AcctAddRs_Type"/>
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
    "acctAddRs"
})
@XmlRootElement(name = "addAccountResponse")
public class AddAccountResponse {

    @XmlElement(name = "AcctAddRs", required = true)
    protected AcctAddRsType acctAddRs;

    /**
     * Gets the value of the acctAddRs property.
     * 
     * @return
     *     possible object is
     *     {@link AcctAddRsType }
     *     
     */
    public AcctAddRsType getAcctAddRs() {
        return acctAddRs;
    }

    /**
     * Sets the value of the acctAddRs property.
     * 
     * @param value
     *     allowed object is
     *     {@link AcctAddRsType }
     *     
     */
    public void setAcctAddRs(AcctAddRsType value) {
        this.acctAddRs = value;
    }

}
