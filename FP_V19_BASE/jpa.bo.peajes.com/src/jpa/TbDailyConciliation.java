package jpa;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

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
 * Entity implementation class for Entity: TbDailyConciliation
 *
 */
@Entity
@Table(name="TB_DAILY_CONCILIATION")
public class TbDailyConciliation implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TB_DAILY_CONCILIATION_GENERATOR")
	@SequenceGenerator(name = "TB_DAILY_CONCILIATION_GENERATOR", sequenceName = "TB_DAILY_CONCILIATION_SEQ",allocationSize=1)
	@Column(name="DAILY_CONC_ID")
	private Long dailyConcId;
	
	@Column(name="DAILY_CONC_DATE")
	private Date dailyConcDate;
	
	@Column(name="DAILY_CONC_BALANCE")
	private Long dailyConcBalance;
	
	@Column(name="DAILY_CONC_EFFECTIVE")
	private Long dailyConcEffective;
	
	@Column(name="DAILY_CONC_DIFF")
	private Long dailyConcDiff;
	
	@Column(name="DATE_TRANSACTION")
	private Timestamp dateTransaction;
	
	@ManyToOne
	@JoinColumn(name = "USER_ID")
	private TbSystemUser userId;
	
	@ManyToOne
	@JoinColumn(name = "DAILY_CONC_STATE_ID")
	private TbDailyConciliationState dailyConcStateId;

	public TbDailyConciliation() {
		super();
	}

	public Long getDailyConcId() {
		return dailyConcId;
	}

	public void setDailyConcId(Long dailyConcId) {
		this.dailyConcId = dailyConcId;
	}

	public Date getDailyConcDate() {
		return dailyConcDate;
	}

	public void setDailyConcDate(Date dailyConcDate) {
		this.dailyConcDate = dailyConcDate;
	}

	public Long getDailyConcBalance() {
		return dailyConcBalance;
	}

	public void setDailyConcBalance(Long dailyConcBalance) {
		this.dailyConcBalance = dailyConcBalance;
	}

	public Long getDailyConcEffective() {
		return dailyConcEffective;
	}

	public void setDailyConcEffective(Long dailyConcEffective) {
		this.dailyConcEffective = dailyConcEffective;
	}

	public Long getDailyConcDiff() {
		return dailyConcDiff;
	}

	public void setDailyConcDiff(Long dailyConcDiff) {
		this.dailyConcDiff = dailyConcDiff;
	}

	public Timestamp getDateTransaction() {
		return dateTransaction;
	}

	public void setDateTransaction(Timestamp dateTransaction) {
		this.dateTransaction = dateTransaction;
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