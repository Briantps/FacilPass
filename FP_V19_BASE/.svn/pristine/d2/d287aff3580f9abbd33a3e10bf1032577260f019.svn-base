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
 * The persistent class for the RE_AGREEMENT_BANK database table.
 * 
 */
@Entity
@Table(name="RE_AGREEMENT_BANK")
public class ReAgreementBank implements Serializable {	
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="RE_AGREEMENT_BANK_GENERATOR", sequenceName="RE_AGREEMENT_BANK_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="RE_AGREEMENT_BANK_GENERATOR")
	
	@Column(name="AGREEMENT_BANK_ID", unique=true, nullable=false, precision=19)
	private Long agreementBankId;
	
	@ManyToOne
	@JoinColumn(name="AGREEMENT_ID")
	private TbAgreement agreementId;
	
	@ManyToOne
	@JoinColumn(name="BANK_ID")
	private TbBank bankId;
	
    @ManyToOne
	@JoinColumn(name="STATE_ID")
	private TbState stateId;
	
	@Column(name="DATE_CREATE_RELATION")
	private Timestamp dateCreateRelation;
	
	@Column(name="DATE_INACTIVE_RELATION")
	private Timestamp dateInactiveRelation;
	
	public ReAgreementBank(){
		super();
	}

	public void setAgreementBankId(Long agreementBankId) {
		this.agreementBankId = agreementBankId;
	}

	public Long getAgreementBankId() {
		return agreementBankId;
	}

	public void setAgreementId(TbAgreement agreementId) {
		this.agreementId = agreementId;
	}

	public TbAgreement getAgreementId() {
		return agreementId;
	}

	public void setBankId(TbBank bankId) {
		this.bankId = bankId;
	}

	public TbBank getBankId() {
		return bankId;
	}

	public void setStateId(TbState stateId) {
		this.stateId = stateId;
	}

	public TbState getStateId() {
		return stateId;
	}

	public void setDateCreateRelation(Timestamp dateCreateRelation) {
		this.dateCreateRelation = dateCreateRelation;
	}

	public Timestamp getDateCreateRelation() {
		return dateCreateRelation;
	}

	public void setDateInactiveRelation(Timestamp dateInactiveRelation) {
		this.dateInactiveRelation = dateInactiveRelation;
	}

	public Timestamp getDateInactiveRelation() {
		return dateInactiveRelation;
	}
}
