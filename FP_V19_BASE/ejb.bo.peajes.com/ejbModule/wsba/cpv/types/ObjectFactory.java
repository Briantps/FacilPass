
package wsba.cpv.types;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the wsba.cpv.types package. 
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

    private final static QName _FinalizarTransaccionResponse_QNAME = new QName("http://ws.cpv.avvillas.com/", "finalizarTransaccionResponse");
    private final static QName _GetEstadoTransaccionResponse_QNAME = new QName("http://ws.cpv.avvillas.com/", "getEstadoTransaccionResponse");
    private final static QName _GetListaBancosResponse_QNAME = new QName("http://ws.cpv.avvillas.com/", "getListaBancosResponse");
    private final static QName _GetEstadoTransaccion_QNAME = new QName("http://ws.cpv.avvillas.com/", "getEstadoTransaccion");
    private final static QName _CrearTransaccion_QNAME = new QName("http://ws.cpv.avvillas.com/", "crearTransaccion");
    private final static QName _CrearTransaccionResponse_QNAME = new QName("http://ws.cpv.avvillas.com/", "crearTransaccionResponse");
    private final static QName _FinalizarTransaccion_QNAME = new QName("http://ws.cpv.avvillas.com/", "finalizarTransaccion");
    private final static QName _GetListaBancos_QNAME = new QName("http://ws.cpv.avvillas.com/", "getListaBancos");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: wsba.cpv.types
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CrearTransaccion }
     * 
     */
    public CrearTransaccion createCrearTransaccion() {
        return new CrearTransaccion();
    }

    /**
     * Create an instance of {@link GetEstadoTransaccion }
     * 
     */
    public GetEstadoTransaccion createGetEstadoTransaccion() {
        return new GetEstadoTransaccion();
    }

    /**
     * Create an instance of {@link GetListaBancosResponse }
     * 
     */
    public GetListaBancosResponse createGetListaBancosResponse() {
        return new GetListaBancosResponse();
    }

    /**
     * Create an instance of {@link GetEstadoTransaccionResponse }
     * 
     */
    public GetEstadoTransaccionResponse createGetEstadoTransaccionResponse() {
        return new GetEstadoTransaccionResponse();
    }

    /**
     * Create an instance of {@link FinalizarTransaccionResponse }
     * 
     */
    public FinalizarTransaccionResponse createFinalizarTransaccionResponse() {
        return new FinalizarTransaccionResponse();
    }

    /**
     * Create an instance of {@link CrearTransaccionResponse }
     * 
     */
    public CrearTransaccionResponse createCrearTransaccionResponse() {
        return new CrearTransaccionResponse();
    }

    /**
     * Create an instance of {@link GetListaBancos }
     * 
     */
    public GetListaBancos createGetListaBancos() {
        return new GetListaBancos();
    }

    /**
     * Create an instance of {@link FinalizarTransaccion }
     * 
     */
    public FinalizarTransaccion createFinalizarTransaccion() {
        return new FinalizarTransaccion();
    }

    /**
     * Create an instance of {@link ClsEstadoTransaccion }
     * 
     */
    public ClsEstadoTransaccion createClsEstadoTransaccion() {
        return new ClsEstadoTransaccion();
    }

    /**
     * Create an instance of {@link ClsRespuestaWsCpv }
     * 
     */
    public ClsRespuestaWsCpv createClsRespuestaWsCpv() {
        return new ClsRespuestaWsCpv();
    }

    /**
     * Create an instance of {@link ClsRespuestaCrearTransaccion }
     * 
     */
    public ClsRespuestaCrearTransaccion createClsRespuestaCrearTransaccion() {
        return new ClsRespuestaCrearTransaccion();
    }

    /**
     * Create an instance of {@link ClsDatosCrearTrandaccion }
     * 
     */
    public ClsDatosCrearTrandaccion createClsDatosCrearTrandaccion() {
        return new ClsDatosCrearTrandaccion();
    }

    /**
     * Create an instance of {@link FinalizeTransactionPayment }
     * 
     */
    public FinalizeTransactionPayment createFinalizeTransactionPayment() {
        return new FinalizeTransactionPayment();
    }

    /**
     * Create an instance of {@link FinalizeTransactionPaymentInp }
     * 
     */
    public FinalizeTransactionPaymentInp createFinalizeTransactionPaymentInp() {
        return new FinalizeTransactionPaymentInp();
    }

    /**
     * Create an instance of {@link CreateTransactionPaymentResponse }
     * 
     */
    public CreateTransactionPaymentResponse createCreateTransactionPaymentResponse() {
        return new CreateTransactionPaymentResponse();
    }

    /**
     * Create an instance of {@link TransactionPaymentOut }
     * 
     */
    public TransactionPaymentOut createTransactionPaymentOut() {
        return new TransactionPaymentOut();
    }

    /**
     * Create an instance of {@link FinalizeTransactionPaymentResponse }
     * 
     */
    public FinalizeTransactionPaymentResponse createFinalizeTransactionPaymentResponse() {
        return new FinalizeTransactionPaymentResponse();
    }

    /**
     * Create an instance of {@link FinalizeTransactionPaymentOut }
     * 
     */
    public FinalizeTransactionPaymentOut createFinalizeTransactionPaymentOut() {
        return new FinalizeTransactionPaymentOut();
    }

    /**
     * Create an instance of {@link GetTransactionInformation }
     * 
     */
    public GetTransactionInformation createGetTransactionInformation() {
        return new GetTransactionInformation();
    }

    /**
     * Create an instance of {@link TransactionInformationInp }
     * 
     */
    public TransactionInformationInp createTransactionInformationInp() {
        return new TransactionInformationInp();
    }

    /**
     * Create an instance of {@link GetBankList }
     * 
     */
    public GetBankList createGetBankList() {
        return new GetBankList();
    }

    /**
     * Create an instance of {@link BankListInp }
     * 
     */
    public BankListInp createBankListInp() {
        return new BankListInp();
    }

    /**
     * Create an instance of {@link GetBankListResponse }
     * 
     */
    public GetBankListResponse createGetBankListResponse() {
        return new GetBankListResponse();
    }

    /**
     * Create an instance of {@link BankListOut }
     * 
     */
    public BankListOut createBankListOut() {
        return new BankListOut();
    }

    /**
     * Create an instance of {@link GetTransactionInformationResponse }
     * 
     */
    public GetTransactionInformationResponse createGetTransactionInformationResponse() {
        return new GetTransactionInformationResponse();
    }

    /**
     * Create an instance of {@link TransactionInformationOut }
     * 
     */
    public TransactionInformationOut createTransactionInformationOut() {
        return new TransactionInformationOut();
    }

    /**
     * Create an instance of {@link CreateTransactionPayment }
     * 
     */
    public CreateTransactionPayment createCreateTransactionPayment() {
        return new CreateTransactionPayment();
    }

    /**
     * Create an instance of {@link TransactionPaymentInp }
     * 
     */
    public TransactionPaymentInp createTransactionPaymentInp() {
        return new TransactionPaymentInp();
    }

    /**
     * Create an instance of {@link BankACHData }
     * 
     */
    public BankACHData createBankACHData() {
        return new BankACHData();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FinalizarTransaccionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.cpv.avvillas.com/", name = "finalizarTransaccionResponse")
    public JAXBElement<FinalizarTransaccionResponse> createFinalizarTransaccionResponse(FinalizarTransaccionResponse value) {
        return new JAXBElement<FinalizarTransaccionResponse>(_FinalizarTransaccionResponse_QNAME, FinalizarTransaccionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetEstadoTransaccionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.cpv.avvillas.com/", name = "getEstadoTransaccionResponse")
    public JAXBElement<GetEstadoTransaccionResponse> createGetEstadoTransaccionResponse(GetEstadoTransaccionResponse value) {
        return new JAXBElement<GetEstadoTransaccionResponse>(_GetEstadoTransaccionResponse_QNAME, GetEstadoTransaccionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetListaBancosResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.cpv.avvillas.com/", name = "getListaBancosResponse")
    public JAXBElement<GetListaBancosResponse> createGetListaBancosResponse(GetListaBancosResponse value) {
        return new JAXBElement<GetListaBancosResponse>(_GetListaBancosResponse_QNAME, GetListaBancosResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetEstadoTransaccion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.cpv.avvillas.com/", name = "getEstadoTransaccion")
    public JAXBElement<GetEstadoTransaccion> createGetEstadoTransaccion(GetEstadoTransaccion value) {
        return new JAXBElement<GetEstadoTransaccion>(_GetEstadoTransaccion_QNAME, GetEstadoTransaccion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CrearTransaccion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.cpv.avvillas.com/", name = "crearTransaccion")
    public JAXBElement<CrearTransaccion> createCrearTransaccion(CrearTransaccion value) {
        return new JAXBElement<CrearTransaccion>(_CrearTransaccion_QNAME, CrearTransaccion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CrearTransaccionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.cpv.avvillas.com/", name = "crearTransaccionResponse")
    public JAXBElement<CrearTransaccionResponse> createCrearTransaccionResponse(CrearTransaccionResponse value) {
        return new JAXBElement<CrearTransaccionResponse>(_CrearTransaccionResponse_QNAME, CrearTransaccionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FinalizarTransaccion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.cpv.avvillas.com/", name = "finalizarTransaccion")
    public JAXBElement<FinalizarTransaccion> createFinalizarTransaccion(FinalizarTransaccion value) {
        return new JAXBElement<FinalizarTransaccion>(_FinalizarTransaccion_QNAME, FinalizarTransaccion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetListaBancos }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.cpv.avvillas.com/", name = "getListaBancos")
    public JAXBElement<GetListaBancos> createGetListaBancos(GetListaBancos value) {
        return new JAXBElement<GetListaBancos>(_GetListaBancos_QNAME, GetListaBancos.class, null, value);
    }

}
