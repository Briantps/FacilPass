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
 * 
 * @author jromero
 *
 */
@Entity
@Table(name="TB_PSE_TRANSACTION")
public class TbPseTransaction implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TB_PSE_TRANSACTION_GENERATOR")
	@SequenceGenerator(name = "TB_PSE_TRANSACTION_GENERATOR", sequenceName = "TB_PSE_TRANSACTION_SEQ",
			allocationSize=1, initialValue=1)
	@Column(name="PSE_TRANSACTION_ID")
	private Long pseTransactionId;
	
	@Column(name="CONVENIO_ID")
	private Long convenioId;
	
	@Column(name="REFERENCIA_PAGO1")
	private String referenciaPago1;

	@Column(name="REFERENCIA_PAGO2")
	private String referenciaPago2;
	
	@Column(name="PSE_USER_TYPE")
	private String pseUserType;
	
	@Column(name="PSE_CODIGO_BANCO")
	private String pseCodigoBanco;
	
	@Column(name="NOMBRE_BANCO")
	private String nombreBanco;
	
	@Column(name="VALOR")
	private Long valor;
	
	@Column(name="DESCRIPCION")
	private String descripcion;
	
	@Column(name="URL_RESPUESTA")
	private String urlRespuesta;
	
	@Column(name="CODIGO_ESTADO_CREAR")
	private Long codigoEstadoCrear;
	
	@Column(name="DESCRIPCION_ESTADO_CREAR")
	private String descripcionEstadoCrear;

	@Column(name="TRANSACTION_ID")
	private Long TransactionId;
	
	@Column(name="URL_REDIRIGIR")
	private String urlRedirigir;
	
	@Column(name="CODIGO_ESTADO_FINALIZAR")
	private Long codigoEstadoFinalizar;
	
	@Column(name="DESCRIPCION_ESTADO_FINALIZAR")
	private String descripcionEstadoFinalizar;
	
	@Column(name="APROBADO")
	private Long aprobado;
	
	@Column(name="FECHA_TRANSACCION")
	private Timestamp fechaTransaccion;
	
	@Column(name="FECHA_FIN_TRANSACCION")
	private Timestamp fechaFinTransaccion;
	
	@Column(name="STATE_URL")
	private Long stateUrl;
	
	@ManyToOne
	@JoinColumn(name="UMBRAL_ID")
	private TbUmbral tbUmbral;

	public TbPseTransaction() {
		super();
	}

	public Long getPseTransactionId() {
		return pseTransactionId;
	}

	public void setPseTransactionId(Long pseTransactionId) {
		this.pseTransactionId = pseTransactionId;
	}

	public Long getConvenioId() {
		return convenioId;
	}

	public void setConvenioId(Long convenioId) {
		this.convenioId = convenioId;
	}

	public String getReferenciaPago1() {
		return referenciaPago1;
	}

	public void setReferenciaPago1(String referenciaPago1) {
		this.referenciaPago1 = referenciaPago1;
	}

	public String getReferenciaPago2() {
		return referenciaPago2;
	}

	public void setReferenciaPago2(String referenciaPago2) {
		this.referenciaPago2 = referenciaPago2;
	}

	public String getPseUserType() {
		return pseUserType;
	}

	public void setPseUserType(String pseUserType) {
		this.pseUserType = pseUserType;
	}

	public String getPseCodigoBanco() {
		return pseCodigoBanco;
	}

	public void setPseCodigoBanco(String pseCodigoBanco) {
		this.pseCodigoBanco = pseCodigoBanco;
	}

	public String getNombreBanco() {
		return nombreBanco;
	}

	public void setNombreBanco(String nombreBanco) {
		this.nombreBanco = nombreBanco;
	}

	public Long getValor() {
		return valor;
	}

	public void setValor(Long valor) {
		this.valor = valor;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getUrlRespuesta() {
		return urlRespuesta;
	}

	public void setUrlRespuesta(String urlRespuesta) {
		this.urlRespuesta = urlRespuesta;
	}

	public Long getCodigoEstadoCrear() {
		return codigoEstadoCrear;
	}

	public void setCodigoEstadoCrear(Long codigoEstadoCrear) {
		this.codigoEstadoCrear = codigoEstadoCrear;
	}

	public String getDescripcionEstadoCrear() {
		return descripcionEstadoCrear;
	}

	public void setDescripcionEstadoCrear(String descripcionEstadoCrear) {
		this.descripcionEstadoCrear = descripcionEstadoCrear;
	}

	public Long getTransactionId() {
		return TransactionId;
	}

	public void setTransactionId(Long transactionId) {
		TransactionId = transactionId;
	}

	public String getUrlRedirigir() {
		return urlRedirigir;
	}

	public void setUrlRedirigir(String urlRedirigir) {
		this.urlRedirigir = urlRedirigir;
	}

	public Long getCodigoEstadoFinalizar() {
		return codigoEstadoFinalizar;
	}

	public void setCodigoEstadoFinalizar(Long codigoEstadoFinalizar) {
		this.codigoEstadoFinalizar = codigoEstadoFinalizar;
	}

	public String getDescripcionEstadoFinalizar() {
		return descripcionEstadoFinalizar;
	}

	public void setDescripcionEstadoFinalizar(String descripcionEstadoFinalizar) {
		this.descripcionEstadoFinalizar = descripcionEstadoFinalizar;
	}

	public Long getAprobado() {
		return aprobado;
	}

	public void setAprobado(Long aprobado) {
		this.aprobado = aprobado;
	}

	public Timestamp getFechaTransaccion() {
		return fechaTransaccion;
	}

	public void setFechaTransaccion(Timestamp fechaTransaccion) {
		this.fechaTransaccion = fechaTransaccion;
	}

	public Timestamp getFechaFinTransaccion() {
		return fechaFinTransaccion;
	}

	public void setFechaFinTransaccion(Timestamp fechaFinTransaccion) {
		this.fechaFinTransaccion = fechaFinTransaccion;
	}

	public TbUmbral getTbUmbral() {
		return tbUmbral;
	}

	public void setTbUmbral(TbUmbral tbUmbral) {
		this.tbUmbral = tbUmbral;
	}

	public Long getStateUrl() {
		return stateUrl;
	}

	public void setStateUrl(Long stateUrl) {
		this.stateUrl = stateUrl;
	}
	
}