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
 * Entity implementation class for Entity: ReAgreementChanel
 *
 */
@Entity
@Table(name="RE_AGREEMENT_CHANEL")
public class ReAgreementChanel implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RE_AGREEMENT_CHANEL_GENERATOR")
	@SequenceGenerator(name = "RE_AGREEMENT_CHANEL_GENERATOR", sequenceName = "RE_AGREEMENT_CHANEL_SEQ",allocationSize=1, initialValue=1)
	@Column(name="AGREEMENT_CHANEL_ID")
	private Long agreementChanelId;
	
	@ManyToOne
	@JoinColumn(name="AGREEMENT_ID")
	private TbAgreement agreementId;
	
	@ManyToOne
	@JoinColumn(name="CHANEL_ID")
	private TbChanel chanelId;
	
	@ManyToOne
	@JoinColumn(name="STATE_ID ")
	private TbState stateId;
		
	@Column(name="DATE_CREATE_RELATION")
	private Timestamp dateCreateRelation;
	
	@Column(name="DATE_INACTIVE_RELATION")
	private Timestamp dateInactiveRelation;

	public void setAgreementChanelId(Long agreementChanelId) {
		this.agreementChanelId = agreementChanelId;
	}

	public Long getAgreementChanelId() {
		return agreementChanelId;
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
