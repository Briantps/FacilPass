package jpa;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;



/**
 * Entity implementation class for Entity: TB_AVAL_TRANSACTION
 * @author nathaly.ramirez
 */
@Entity
@Table(name="TB_AVAL_TRANSACTION")
public class TbAvalTransaction implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TB_AVAL_TRANSACTION_GENERATOR")
	@SequenceGenerator(name = "TB_AVAL_TRANSACTION_GENERATOR", sequenceName = "TB_AVAL_TRANSACTION_SEQ",allocationSize=1)
	@Column(name="AVAL_TRANSACTION_ID")
	private Long avalTransactionId;
	
	@ManyToOne
	@JoinColumn(name="USER_ID")
	private TbSystemUser userId;
	
	@ManyToOne
	@JoinColumn(name="AGREEMENT_ID")
	private TbAgreement agreementId;
	
	@ManyToOne
	@JoinColumn(name="CHANEL_ID")
	private TbChanel chanelId;
	
	@Column(name="NURE")
	private String nure;
	
	@Column(name="VALUE_TRANSACTION")
	private Long valueTransaction;
	
	@Column(name="REFERENCE1")
	private String reference1;
	
	@Column(name="IP_TRANSACTION")
	private String ipTransaction;
	
	@Column(name="URL_RETURN")
	private String urlReturn;
	
	@Column(name="SECRET_ID")
	private String secretId;
	
	@Column(name="SECRET")
	private String secret;
	
	@Column(name="DATE_TRANSACTION")
	private Timestamp dateTransaction;
	
	@Column(name="TRANSACTION_TYPE")
	private String transactionType;
	
	@Column(name="REQUEST_ID")
	private Long requestId;
	
	@Column(name="APROBADO")
	private Long aprobado;
	
	@Column(name="STATE_TRANSACTION_ID")
	private Long stateTransactionId;	
	
	@Column(name="URL_REQUEST")
	private String urlRequest;
		
	@Column(name="DATE_CREATE_REQUEST")
	private Timestamp dateCreateRequest;
	
	@Column(name="TRANSACTION_IDENTIFIER")
	private String transactionIdentifier;
	
	@ManyToOne
	@JoinColumn(name="UMBRAL_ID")
	private TbUmbral umbralId;
	
	@Column(name="STATE_URL")
	private Long stateurl;

	public  TbAvalTransaction(){
		super();
	}

	public void setAvalTransactionId(Long avalTransactionId) {
		this.avalTransactionId = avalTransactionId;
	}

	public Long getAvalTransactionId() {
		return avalTransactionId;
	}

	public void setUserId(TbSystemUser userId) {
		this.userId = userId;
	}

	public TbSystemUser getUserId() {
		return userId;
	}

	public void setAgreementId(TbAgreement agreementId) {
		this.agreementId = agreementId;
	}

	public TbAgreement getAgreementId() {
		return agreementId;
	}

	public void setChanelId(TbChanel chanelId) {
		this.chanelId = chanelId;
	}

	public TbChanel getChanelId() {
		return chanelId;
	}

	public void setNure(String nure) {
		this.nure = nure;
	}

	public String getNure() {
		return nure;
	}

	public void setValueTransaction(Long valueTransaction) {
		this.valueTransaction = valueTransaction;
	}

	public Long getValueTransaction() {
		return valueTransaction;
	}

	public void setReference1(String reference1) {
		this.reference1 = reference1;
	}

	public String getReference1() {
		return reference1;
	}

	public void setIpTransaction(String ipTransaction) {
		this.ipTransaction = ipTransaction;
	}

	public String getIpTransaction() {
		return ipTransaction;
	}

	public void setUrlReturn(String urlReturn) {
		this.urlReturn = urlReturn;
	}

	public String getUrlReturn() {
		return urlReturn;
	}

	public void setSecretId(String secretId) {
		this.secretId = secretId;
	}

	public String getSecretId() {
		return secretId;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getSecret() {
		return secret;
	}

	public void setDateTransaction(Timestamp dateTransaction) {
		this.dateTransaction = dateTransaction;
	}

	public Timestamp getDateTransaction() {
		return dateTransaction;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

	public Long getRequestId() {
		return requestId;
	}

	public void setStateTransactionId(Long stateTransactionId) {
		this.stateTransactionId = stateTransactionId;
	}

	public Long getStateTransactionId() {
		return stateTransactionId;
	}

	public void setUrlRequest(String urlRequest) {
		this.urlRequest = urlRequest;
	}

	public String getUrlRequest() {
		return urlRequest;
	}

	public void setDateCreateRequest(Timestamp dateCreateRequest) {
		this.dateCreateRequest = dateCreateRequest;
	}

	public Timestamp getDateCreateRequest() {
		return dateCreateRequest;
	}

	public void setTransactionIdentifier(String transactionIdentifier) {
		this.transactionIdentifier = transactionIdentifier;
	}

	public String getTransactionIdentifier() {
		return transactionIdentifier;
	}

	public void setUmbralId(TbUmbral umbralId) {
		this.umbralId = umbralId;
	}

	public TbUmbral getUmbralId() {
		return umbralId;
	}

	public void setStateUrl(Long stateUrl) {
		this.stateurl=stateUrl;
		
	}

	public Long getStateUrl() {
		return stateurl;
	}

	public Long getAprobado() {
		return aprobado;
	}
	public void setAprobado(Long aprobado) {
		this.aprobado = aprobado;
	}
	
}

