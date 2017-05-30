
package com.grupoaval.telepeajes.xsd.ifx;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.grupoaval.telepeajes.xsd.ifx package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Channel_QNAME = new QName("urn://grupoaval.com/telepeajes/xsd/ifx/", "Channel");
    private final static QName _IPAddr_QNAME = new QName("urn://grupoaval.com/telepeajes/xsd/ifx/", "IPAddr");
    private final static QName _Reverse_QNAME = new QName("urn://grupoaval.com/telepeajes/xsd/ifx/", "Reverse");
    private final static QName _Language_QNAME = new QName("urn://grupoaval.com/telepeajes/xsd/ifx/", "Language");
    private final static QName _KeyAcctId_QNAME = new QName("urn://grupoaval.com/telepeajes/xsd/ifx/", "KeyAcctId");
    private final static QName _NextDay_QNAME = new QName("urn://grupoaval.com/telepeajes/xsd/ifx/", "NextDay");
    private final static QName _SessKey_QNAME = new QName("urn://grupoaval.com/telepeajes/xsd/ifx/", "SessKey");
    private final static QName _ClientDt_QNAME = new QName("urn://grupoaval.com/telepeajes/xsd/ifx/", "ClientDt");
    private final static QName _BankInfoTypePostalCode_QNAME = new QName("urn://grupoaval.com/telepeajes/xsd/ifx/", "PostalCode");
    private final static QName _BankInfoTypeStateProv_QNAME = new QName("urn://grupoaval.com/telepeajes/xsd/ifx/", "StateProv");
    private final static QName _BankInfoTypePostAddr_QNAME = new QName("urn://grupoaval.com/telepeajes/xsd/ifx/", "PostAddr");
    private final static QName _BankInfoTypeCity_QNAME = new QName("urn://grupoaval.com/telepeajes/xsd/ifx/", "City");
    private final static QName _BankInfoTypeCountry_QNAME = new QName("urn://grupoaval.com/telepeajes/xsd/ifx/", "Country");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.grupoaval.telepeajes.xsd.ifx
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link BankInfoType }
     * 
     */
    public BankInfoType createBankInfoType() {
        return new BankInfoType();
    }

    /**
     * Create an instance of {@link CurrencyAmountType }
     * 
     */
    public CurrencyAmountType createCurrencyAmountType() {
        return new CurrencyAmountType();
    }

    /**
     * Create an instance of {@link ClientAppType }
     * 
     */
    public ClientAppType createClientAppType() {
        return new ClientAppType();
    }

    /**
     * Create an instance of {@link SvcRqType }
     * 
     */
    public SvcRqType createSvcRqType() {
        return new SvcRqType();
    }

    /**
     * Create an instance of {@link GovIssueIdentType }
     * 
     */
    public GovIssueIdentType createGovIssueIdentType() {
        return new GovIssueIdentType();
    }

    /**
     * Create an instance of {@link UserIdType }
     * 
     */
    public UserIdType createUserIdType() {
        return new UserIdType();
    }

    /**
     * Create an instance of {@link CustNameType }
     * 
     */
    public CustNameType createCustNameType() {
        return new CustNameType();
    }

    /**
     * Create an instance of {@link DepAcctIdFromType }
     * 
     */
    public DepAcctIdFromType createDepAcctIdFromType() {
        return new DepAcctIdFromType();
    }

    /**
     * Create an instance of {@link RefInfoType }
     * 
     */
    public RefInfoType createRefInfoType() {
        return new RefInfoType();
    }

    /**
     * Create an instance of {@link BillPmtInfoType }
     * 
     */
    public BillPmtInfoType createBillPmtInfoType() {
        return new BillPmtInfoType();
    }

    /**
     * Create an instance of {@link StatusType }
     * 
     */
    public StatusType createStatusType() {
        return new StatusType();
    }

    /**
     * Create an instance of {@link DepAcctIdType }
     * 
     */
    public DepAcctIdType createDepAcctIdType() {
        return new DepAcctIdType();
    }

    /**
     * Create an instance of {@link CustIdType }
     * 
     */
    public CustIdType createCustIdType() {
        return new CustIdType();
    }

    /**
     * Create an instance of {@link GenericAcctIdType }
     * 
     */
    public GenericAcctIdType createGenericAcctIdType() {
        return new GenericAcctIdType();
    }

    /**
     * Create an instance of {@link SvcRsType }
     * 
     */
    public SvcRsType createSvcRsType() {
        return new SvcRsType();
    }

    /**
     * Create an instance of {@link MsgRqHdrType }
     * 
     */
    public MsgRqHdrType createMsgRqHdrType() {
        return new MsgRqHdrType();
    }

    /**
     * Create an instance of {@link PostAddrType }
     * 
     */
    public PostAddrType createPostAddrType() {
        return new PostAddrType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn://grupoaval.com/telepeajes/xsd/ifx/", name = "Channel")
    public JAXBElement<String> createChannel(String value) {
        return new JAXBElement<String>(_Channel_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn://grupoaval.com/telepeajes/xsd/ifx/", name = "IPAddr")
    public JAXBElement<String> createIPAddr(String value) {
        return new JAXBElement<String>(_IPAddr_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn://grupoaval.com/telepeajes/xsd/ifx/", name = "Reverse")
    public JAXBElement<Boolean> createReverse(Boolean value) {
        return new JAXBElement<Boolean>(_Reverse_QNAME, Boolean.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn://grupoaval.com/telepeajes/xsd/ifx/", name = "Language")
    public JAXBElement<String> createLanguage(String value) {
        return new JAXBElement<String>(_Language_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn://grupoaval.com/telepeajes/xsd/ifx/", name = "KeyAcctId")
    public JAXBElement<String> createKeyAcctId(String value) {
        return new JAXBElement<String>(_KeyAcctId_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn://grupoaval.com/telepeajes/xsd/ifx/", name = "NextDay")
    public JAXBElement<Object> createNextDay(Object value) {
        return new JAXBElement<Object>(_NextDay_QNAME, Object.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn://grupoaval.com/telepeajes/xsd/ifx/", name = "SessKey")
    public JAXBElement<String> createSessKey(String value) {
        return new JAXBElement<String>(_SessKey_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn://grupoaval.com/telepeajes/xsd/ifx/", name = "ClientDt")
    public JAXBElement<Object> createClientDt(Object value) {
        return new JAXBElement<Object>(_ClientDt_QNAME, Object.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn://grupoaval.com/telepeajes/xsd/ifx/", name = "PostalCode", scope = BankInfoType.class)
    public JAXBElement<String> createBankInfoTypePostalCode(String value) {
        return new JAXBElement<String>(_BankInfoTypePostalCode_QNAME, String.class, BankInfoType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn://grupoaval.com/telepeajes/xsd/ifx/", name = "StateProv", scope = BankInfoType.class)
    public JAXBElement<String> createBankInfoTypeStateProv(String value) {
        return new JAXBElement<String>(_BankInfoTypeStateProv_QNAME, String.class, BankInfoType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PostAddrType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn://grupoaval.com/telepeajes/xsd/ifx/", name = "PostAddr", scope = BankInfoType.class)
    public JAXBElement<PostAddrType> createBankInfoTypePostAddr(PostAddrType value) {
        return new JAXBElement<PostAddrType>(_BankInfoTypePostAddr_QNAME, PostAddrType.class, BankInfoType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn://grupoaval.com/telepeajes/xsd/ifx/", name = "City", scope = BankInfoType.class)
    public JAXBElement<String> createBankInfoTypeCity(String value) {
        return new JAXBElement<String>(_BankInfoTypeCity_QNAME, String.class, BankInfoType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn://grupoaval.com/telepeajes/xsd/ifx/", name = "Country", scope = BankInfoType.class)
    public JAXBElement<String> createBankInfoTypeCountry(String value) {
        return new JAXBElement<String>(_BankInfoTypeCountry_QNAME, String.class, BankInfoType.class, value);
    }

}
