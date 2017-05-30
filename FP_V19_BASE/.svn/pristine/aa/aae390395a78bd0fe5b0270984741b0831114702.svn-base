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
 * The persistent class for the RE_DE_REGISTRATION database table.
 * 
 */
@Entity
@Table(name = "RE_AGREEMENT_CHANEL")
public class reAgreementChannel implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 9007697138383407895L;

	@Id
	@SequenceGenerator(name = "RE_AGREEMENT_CHANELRE_AGREEMENT_CHANELID_GENERATOR", sequenceName = "RE_AGREEMENT_CHANEL_SEQ",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RE_AGREEMENT_CHANELRE_AGREEMENT_CHANELID_GENERATOR")
	@Column(name = "AGREEMENT_CHANEL_ID", unique = true, nullable = false, precision = 19)
	private long reAgreementChannel;
  
	@ManyToOne
	@JoinColumn(name="AGREEMENT_ID")
	private TbAgreement agreementId; 
	
	@ManyToOne
	@JoinColumn(name="CHANEL_ID")
	private TbChanel channelId;
	
	@Column(name="STATE_ID")
     private Long stateId;  
	
	
	@Column(name="DATE_CREATE_RELATION")
	private Timestamp dateCreateRelation;
	
	@Column(name="DATE_INACTIVE_RELATION")
	private Timestamp dateInactiveRelation;

	public long getReAgreementChannel() {
		return reAgreementChannel;
	}

	public void setReAgreementChannel(long reAgreementChannel) {
		this.reAgreementChannel = reAgreementChannel;
	}

	public TbAgreement getAgreementId() {
		return agreementId;
	}

	public void setAgreementId(TbAgreement agreementId) {
		this.agreementId = agreementId;
	}

	public TbChanel getChannelId() {
		return channelId;
	}

	public void setChannelId(TbChanel channelId) {
		this.channelId = channelId;
	}

	public Long getStateId() {
		return stateId;
	}

	public void setStateId(Long stateId) {
		this.stateId = stateId;
	}

	public Timestamp getDateCreateRelation() {
		return dateCreateRelation;
	}

	public void setDateCreateRelation(Timestamp dateCreateRelation) {
		this.dateCreateRelation = dateCreateRelation;
	}

	public Timestamp getDateInactiveRelation() {
		return dateInactiveRelation;
	}

	public void setDateInactiveRelation(Timestamp dateInactiveRelation) {
		this.dateInactiveRelation = dateInactiveRelation;
	}
	
	
	
	
}
