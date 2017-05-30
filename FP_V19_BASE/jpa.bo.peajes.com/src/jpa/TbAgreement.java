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
 * Entity implementation class for Entity: TB_AGREEMENT
 *
 */
@Entity
@Table(name="TB_AGREEMENT")
public class TbAgreement implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TB_AGREEMENT_GENERATOR")
	@SequenceGenerator(name = "TB_AGREEMENT_GENERATOR", sequenceName = "TB_AGREEMENT_SEQ",allocationSize=1)
	@Column(name="AGREEMENT_ID")
	private Long agreementId;
	
	@Column(name="CODE_AGREEMENT")
	private Long codeAgreement;
	
	@Column(name="NAME_AGREEMENT")
	private String nameAgreement;
	
	@Column(name="DESCRIPTION_AGREEMENT")
	private String descriptionAgreement;
	
	//bi-directional many-to-one association to TbState
    @ManyToOne
	@JoinColumn(name="STATE_ID")
	private TbState tbState;
	
	//bi-directional many-to-one association to TbPaymentMethod
    @ManyToOne
	@JoinColumn(name="PAYMENT_METHOD_ID")
	private TbPaymentMethod tbPaymentMethod;

	/**
	 * @author TPS r.bautista
	 */
    @Column(name="AGREEMENT_MINIMUM_ACTIVE")
	private Long minimumActive;

	/**
	 * @author TPS r.bautista
	 */
    @Column(name="AGREEMENT_MINIMUM_ALLOCATION")
	private Long minimumValue;
    
	public void setAgreementId(Long agreementId) {
		this.agreementId = agreementId;
	}

	public Long getAgreementId() {
		return agreementId;
	}

	public void setCodeAgreement(Long codeAgreement) {
		this.codeAgreement = codeAgreement;
	}

	public void setNameAgreement(String nameAgreement) {
		this.nameAgreement = nameAgreement;
	}

	public String getNameAgreement() {
		return nameAgreement;
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

	public void setTbState(TbState tbState) {
		this.tbState = tbState;
	}

	public TbState getTbState() {
		return tbState;
	}

	public void setTbPaymentMethod(TbPaymentMethod tbPaymentMethod) {
		this.tbPaymentMethod = tbPaymentMethod;
	}

	public TbPaymentMethod getTbPaymentMethod() {
		return tbPaymentMethod;
	}

	/**
	 * @author TPS r.bautista
	 */
	public Long getMinimumActive() {
		return minimumActive;
	}

	/**
	 * @author TPS r.bautista
	 */
	public void setMinimumActive(Long minimumActive) {
		this.minimumActive = minimumActive;
	}

	/**
	 * @author TPS r.bautista
	 */
	public Long getMinimumValue() {
		return minimumValue;
	}

	/**
	 * @author TPS r.bautista
	 */
	public void setMinimumValue(Long minimumValue) {
		this.minimumValue = minimumValue;
	}

	/**
	 * Indica si la recarga tiene activo la minima recarga
	 * @return true si tiene activa la minima recarga
	 * @author TPS r.bautista
	 */
	public boolean isValidaRecAsigMinima(){
		if (this.minimumActive == null){
			return false;
		}

		return this.minimumActive.intValue() == 1;
	}

}
