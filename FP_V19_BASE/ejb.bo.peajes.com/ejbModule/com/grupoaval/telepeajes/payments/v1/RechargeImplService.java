
package com.grupoaval.telepeajes.payments.v1;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.1 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebServiceClient(name = "RechargeImplService", targetNamespace = "http://v1.payments.telepeajes.grupoaval.com/", wsdlLocation = "http://localhost:8997/Recharge?wsdl")
public class RechargeImplService
    extends Service
{

    private final static URL RECHARGEIMPLSERVICE_WSDL_LOCATION;

    static {
        URL url = null;
        try {
            url = new URL("http://localhost:8997/Recharge?wsdl");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        RECHARGEIMPLSERVICE_WSDL_LOCATION = url;
    }

    public RechargeImplService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public RechargeImplService() {
        super(RECHARGEIMPLSERVICE_WSDL_LOCATION, new QName("http://v1.payments.telepeajes.grupoaval.com/", "RechargeImplService"));
    }

    /**
     * 
     * @return
     *     returns RechargeSoap
     */
    @WebEndpoint(name = "RechargeImplPort")
    public RechargeSoap getRechargeImplPort() {
        return (RechargeSoap)super.getPort(new QName("http://v1.payments.telepeajes.grupoaval.com/", "RechargeImplPort"), RechargeSoap.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns RechargeSoap
     */
    @WebEndpoint(name = "RechargeImplPort")
    public RechargeSoap getRechargeImplPort(WebServiceFeature... features) {
        return (RechargeSoap)super.getPort(new QName("http://v1.payments.telepeajes.grupoaval.com/", "RechargeImplPort"), RechargeSoap.class, features);
    }

}
