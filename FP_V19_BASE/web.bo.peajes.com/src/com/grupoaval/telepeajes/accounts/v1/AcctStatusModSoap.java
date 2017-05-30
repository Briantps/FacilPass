
package com.grupoaval.telepeajes.accounts.v1;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.1 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebService(name = "AcctStatusModSoap", targetNamespace = "urn://grupoaval.com/telepeajes/accounts/v1/")
@XmlSeeAlso({
    com.grupoaval.telepeajes.accounts.v1.ObjectFactory.class,
    com.grupoaval.telepeajes.payments.v1.ObjectFactory.class,
    com.grupoaval.telepeajes.xsd.ifx.ObjectFactory.class
})
public interface AcctStatusModSoap {


    /**
     * 
     * @param acctStatusModRq
     * @return
     *     returns com.grupoaval.telepeajes.accounts.v1.AcctStatusModRsType
     */
    @WebMethod(action = "urn://grupoaval.com/telepeajes/accounts/v1/modAccountStatus")
    @WebResult(name = "AcctStatusModRs", targetNamespace = "urn://grupoaval.com/telepeajes/accounts/v1/")
    @RequestWrapper(localName = "modAccountStatus", targetNamespace = "urn://grupoaval.com/telepeajes/accounts/v1/", className = "com.grupoaval.telepeajes.accounts.v1.ModAccountStatus")
    @ResponseWrapper(localName = "modAccountStatusResponse", targetNamespace = "urn://grupoaval.com/telepeajes/accounts/v1/", className = "com.grupoaval.telepeajes.accounts.v1.ModAccountStatusResponse")
    public AcctStatusModRsType modAccountStatus(
        @WebParam(name = "AcctStatusModRq", targetNamespace = "urn://grupoaval.com/telepeajes/accounts/v1/")
        AcctStatusModRqType acctStatusModRq);

}
