package jpa;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Timestamp;

/**
 * The persistent class for the TB_ALARM_BALANCE database table.
 * 
 */
@Entity
@Table(name="TB_ALARM_BALANCE")
public class TbAlarmBalance implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TB_ALARM_BALANCE_GENERATOR")
	@SequenceGenerator(name = "TB_ALARM_BALANCE_GENERATOR", sequenceName = "TB_ALARM_BALANCE_SEQ",allocationSize=1)
	@Column(name="ID_ALARM_BALANCE")
	private Long idAlarmBalance;
	
	@Column(name="ID_TIP_ALARM")
	private long idTipAlarm;
	
	@Column(name="VAL_MIN_ALARM")
	private long valMinAlarm;
	
	@Column(name="FREQUENCY")
	private long frequency;
	
	@Column(name="LAST_EXECUTION")
	private Timestamp lastExecution;
	
	@Column(name="EST_ALARM")
	private long estAlarm;
	
	@Column(name="ACCOUNT_ID")
	private Long accountId;
	
	@Column(name="VAL_MINIMO")
	private Long valminimo;
	
	@Column(name="NOTE")
	private String Observacion;
	
	public TbAlarmBalance() {
		super();
	}

	public void setIdAlarmBalance(long idAlarmBalance) {
		this.idAlarmBalance = idAlarmBalance;
	}

	public long getIdAlarmBalance() {
		return idAlarmBalance;
	}

	public void setIdTipAlarm(long idTipAlarm) {
		this.idTipAlarm = idTipAlarm;
	}

	public long getIdTipAlarm() {
		return idTipAlarm;
	}

	public void setValMinAlarm(long valMinAlarm) {
		this.valMinAlarm = valMinAlarm;
	}

	public long getValMinAlarm() {
		return valMinAlarm;
	}

	public void setFrequency(long frequency) {
		this.frequency = frequency;
	}

	public long getFrequency() {
		return frequency;
	}

	public void setLastExecution(Timestamp lastExecution) {
		this.lastExecution = lastExecution;
	}

	public Timestamp getLastExecution() {
		return lastExecution;
	}

	public void setEstAlarm(long estAlarm) {
		this.estAlarm = estAlarm;
	}

	public long getEstAlarm() {
		return estAlarm;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setValminimo(Long valminimo) {
		this.valminimo = valminimo;
	}

	public Long getValminimo() {
		return valminimo;
	}

	public String getObservacion() {
		return Observacion;
	}

	public void setObservacion(String observacion) {
		Observacion = observacion;
	}

	
	
   
}

