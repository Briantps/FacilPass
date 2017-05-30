package jpa;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="TB_TRAN_TAG")
public class TbTranTag implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="ID_TRAN_TAG")
	private Long transactionId;
	
	@Column(name="EQUIPMENTOBUID")
	private String deviceId;
	
	@Column(name="TIPO_OPERACION")
	private Long transactionTypeId;
	
	@Column(name="SALDO_ANT")
	private Long previousBalance;
	
	@Column(name="NUEVO_SALDO")
	private Long newBalance;
	
	@Column(name="VALOR_OPERACION")
	private Long transactionValue;
	
	@Column(name="FECHA")
	private Timestamp transactionTime;
	
	@Column(name="ORIGEN")
	private String origin;
	
	@Column(name="OBSERVACIONES")
	private String observ;

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionTypeId(Long transactionTypeId) {
		this.transactionTypeId = transactionTypeId;
	}

	public Long getTransactionTypeId() {
		return transactionTypeId;
	}

	public void setPreviousBalance(Long previousBalance) {
		this.previousBalance = previousBalance;
	}

	public Long getPreviousBalance() {
		return previousBalance;
	}

	public void setNewBalance(Long newBalance) {
		this.newBalance = newBalance;
	}

	public Long getNewBalance() {
		return newBalance;
	}

	public void setTransactionValue(Long transactionValue) {
		this.transactionValue = transactionValue;
	}

	public Long getTransactionValue() {
		return transactionValue;
	}

	public void setTransactionTime(Timestamp transactionTime) {
		this.transactionTime = transactionTime;
	}

	public Timestamp getTransactionTime() {
		return transactionTime;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getOrigin() {
		return origin;
	}

	public void setObserv(String observ) {
		this.observ = observ;
	}

	public String getObserv() {
		return observ;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceId() {
		return deviceId;
	}
}
