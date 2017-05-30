package jpa;

import java.io.Serializable;

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
* Entity implementation class for Entity: TbAgreement
*
*/
@Entity
@Table(name="TB_AGREEMENT")
public class TbAgreement implements Serializable{
private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TB_AGREEMENT_GENERATOR")
	@SequenceGenerator(name = "TB_AGREEMENT_GENERATOR", sequenceName = "TB_AGREEMENT_SEQ")
	@Column(name="AGREEMENT_ID")
	private Long agreementId;
	
	@Column(name="CODE_AGREEMENT")
	private Long codeAgreement;
	
	@Column(name="DESCRIPTION_AGREEMENT")
	private String descriptionAgreement;
	
	@ManyToOne
	@JoinColumn(name="STATE_ID")
	private TbState stateId;
	
	@ManyToOne
	@JoinColumn(name="PAYMENT_METHOD_ID")
	private TbPaymentMethod paymentMethodId;

	public void setAgreementId(Long agreementId) {
		this.agreementId = agreementId;
	}

	public Long getAgreementId() {
		return agreementId;
	}

	public void setCodeAgreement(Long codeAgreement) {
		this.codeAgreement = codeAgreement;
	}

	public Long getCodeAgreement() {
		return codeAgreement;
	}

	public void setDescriptionAgreement(String descriptionAgreement) {
		this.descriptionAgreement = descriptionAgreement;
	}

	public String getDescriptionAgreement() {
		return descriptionAgreement;
	}

	public void setStateId(TbState stateId) {
		this.stateId = stateId;
	}

	public TbState getStateId() {
		return stateId;
	}

	public void setPaymentMethodId(TbPaymentMethod paymentMethodId) {
		this.paymentMethodId = paymentMethodId;
	}

	public TbPaymentMethod getPaymentMethodId() {
		return paymentMethodId;
	}

}
