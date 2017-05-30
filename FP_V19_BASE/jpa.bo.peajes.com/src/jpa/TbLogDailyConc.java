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
 * Entity implementation class for Entity: TbLogDailyConc
 *
 */
@Entity
@Table(name="TB_LOG_DAILY_CONC")
public class TbLogDailyConc implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TB_LOG_DAILY_CONC_GENERATOR")
	@SequenceGenerator(name = "TB_LOG_DAILY_CONC_GENERATOR", sequenceName = "TB_LOG_DAILY_CONC_SEQ",allocationSize=1)
	@Column(name="LOG_DAILY_CONC_ID")
	private Long logDailyConcId;
	
	@Column(name="LOG_DAILY_CONC_DATE")
	private Timestamp logDailyConcDate;
	
	@Column(name="LOG_DAILY_CONC_BALA")
	private Long logDailyConcBala;
	
	@Column(name="LOG_DAILY_CONC_EFFE")
	private Long logDailyConcEffe;
	
	@Column(name="LOG_DAILY_CONC_DIFF")
	private Long logDailyConcDiff;
	
	@Column(name="LOG_DAILY_CONC_OBSE")
	private String logDailyConcObse;
	
	@ManyToOne
	@JoinColumn(name = "DAILY_CONC_ID")
	private TbDailyConciliation dailyConcId;
	
	@ManyToOne
	@JoinColumn(name = "USER_ID")
	private TbSystemUser userId;
	
	@ManyToOne
	@JoinColumn(name = "DAILY_CONC_STATE_ID")
	private TbDailyConciliationState dailyConcStateId;

	public TbLogDailyConc() {
		super();
	}

	public Long getLogDailyConcId() {
		return logDailyConcId;
	}

	public void setLogDailyConcId(Long logDailyConcId) {
		this.logDailyConcId = logDailyConcId;
	}

	public Timestamp getLogDailyConcDate() {
		return logDailyConcDate;
	}

	public void setLogDailyConcDate(Timestamp logDailyConcDate) {
		this.logDailyConcDate = logDailyConcDate;
	}

	public Long getLogDailyConcBala() {
		return logDailyConcBala;
	}

	public void setLogDailyConcBala(Long logDailyConcBala) {
		this.logDailyConcBala = logDailyConcBala;
	}

	public Long getLogDailyConcEffe() {
		return logDailyConcEffe;
	}

	public void setLogDailyConcEffe(Long logDailyConcEffe) {
		this.logDailyConcEffe = logDailyConcEffe;
	}

	public Long getLogDailyConcDiff() {
		return logDailyConcDiff;
	}

	public void setLogDailyConcDiff(Long logDailyConcDiff) {
		this.logDailyConcDiff = logDailyConcDiff;
	}

	public String getLogDailyConcObse() {
		return logDailyConcObse;
	}

	public void setLogDailyConcObse(String logDailyConcObse) {
		this.logDailyConcObse = logDailyConcObse;
	}

	public TbDailyConciliation getDailyConcId() {
		return dailyConcId;
	}

	public void setDailyConcId(TbDailyConciliation dailyConcId) {
		this.dailyConcId = dailyConcId;
	}

	public TbSystemUser getUserId() {
		return userId;
	}

	public void setUserId(TbSystemUser userId) {
		this.userId = userId;
	}

	public TbDailyConciliationState getDailyConcStateId() {
		return dailyConcStateId;
	}

	public void setDailyConcStateId(TbDailyConciliationState dailyConcStateId) {
		this.dailyConcStateId = dailyConcStateId;
	}

}