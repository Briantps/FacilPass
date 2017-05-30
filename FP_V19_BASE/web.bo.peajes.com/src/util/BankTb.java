package util;

import java.io.Serializable;

public class BankTb implements Serializable{
	
	//datos de la entidad
	private static final long serialVersionUID = 1L;
	private Long bankId;
	private String bankName;
	private String bankCode;
	private Long bankAval;
	private Long bankAvalCheck;
	
	//datos relación convenio-banco
	private Long idAgreementBank;
	private Long idAgreement;
	private Long codeAgreement;
	private Long idState;
	private Long paymentMethodId;
	private Long deleteAgreement;
	private Long activeRecharge;
	private String rechargeValue;	
	private String nameAgreement;
	private String namePaymentMethod;
	private String descriptionAgreement;
	private String stateReAgreementBank;
	
	public BankTb(){
		super();
	}
	

	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}
	public Long getBankId() {
		return bankId;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankAval(Long bankAval) {
		this.bankAval = bankAval;
	}
	public Long getBankAval() {
		return bankAval;
	}

	public void setBankAvalCheck(Long bankAvalCheck) {
		this.bankAvalCheck = bankAvalCheck;
	}

	public Long getBankAvalCheck() {
		return bankAvalCheck;
	}

	public void setIdAgreement(Long idAgreement) {
		this.idAgreement = idAgreement;
	}


	public Long getIdAgreement() {
		return idAgreement;
	}


	public void setCodeAgreement(Long codeAgreement) {
		this.codeAgreement = codeAgreement;
	}


	public Long getCodeAgreement() {
		return codeAgreement;
	}


	public void setNameAgreement(String nameAgreement) {
		this.nameAgreement = nameAgreement;
	}


	public String getNameAgreement() {
		return nameAgreement;
	}


	public void setDescriptionAgreement(String descriptionAgreement) {
		this.descriptionAgreement = descriptionAgreement;
	}


	public String getDescriptionAgreement() {
		return descriptionAgreement;
	}


	public void setIdAgreementBank(Long idAgreementBank) {
		this.idAgreementBank = idAgreementBank;
	}


	public Long getIdAgreementBank() {
		return idAgreementBank;
	}


	public void setStateReAgreementBank(String stateReAgreementBank) {
		this.stateReAgreementBank = stateReAgreementBank;
	}


	public String getStateReAgreementBank() {
		return stateReAgreementBank;
	}


	public void setIdState(Long idState) {
		this.idState = idState;
	}


	public Long getIdState() {
		return idState;
	}


	public void setNamePaymentMethod(String namePaymentMethod) {
		this.namePaymentMethod = namePaymentMethod;
	}


	public String getNamePaymentMethod() {
		return namePaymentMethod;
	}

	
	public void setPaymentMethodId(Long paymentMethodId) {
		this.paymentMethodId = paymentMethodId;
	}


	public Long getPaymentMethodId() {
		return paymentMethodId;
	}


	public void setDeleteAgreement(Long deleteAgreement) {
		this.deleteAgreement = deleteAgreement;
	}


	public Long getDeleteAgreement() {
		return deleteAgreement;
	}


	public void setActiveRecharge(Long activeRecharge) {
		this.activeRecharge = activeRecharge;
	}


	public Long getActiveRecharge() {
		return activeRecharge;
	}


	public void setRechargeValue(String rechargeValue) {
		this.rechargeValue = rechargeValue;
	}


	public String getRechargeValue() {
		return rechargeValue;
	}



}
