package jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Entity implementation class for Entity: TbDailyConciliationState
 *
 */
@Entity
@Table(name="TB_DAILY_CONCILIATION_STATE")
public class TbDailyConciliationState implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TB_DAILY_CONC_STATE_GENERATOR")
	@SequenceGenerator(name = "TB_DAILY_CONC_STATE_GENERATOR", sequenceName = "TB_DAILY_CONC_STATE_SEQ",allocationSize=1)
	@Column(name="DAILY_CONC_STATE_ID")
	private Long dailyConcStateId;
	
	@Column(name = "DAILY_CONC_STATE_NAME")
	private String dailyConcStateName;

	@Column(name = "DAILY_CONC_STATE_DESC")
	private String dailyConcStateDesc;
	
	public TbDailyConciliationState() {
		super();
	}

	public long getDailyConcStateId() {
		return dailyConcStateId;
	}

	public void setDailyConcStateId(long dailyConcStateId) {
		this.dailyConcStateId = dailyConcStateId;
	}

	public String getDailyConcStateName() {
		return dailyConcStateName;
	}

	public void setDailyConcStateName(String dailyConcStateName) {
		this.dailyConcStateName = dailyConcStateName;
	}

	public String getDailyConcStateDesc() {
		return dailyConcStateDesc;
	}

	public void setDailyConcStateDesc(String dailyConcStateDesc) {
		this.dailyConcStateDesc = dailyConcStateDesc;
	}
	
}