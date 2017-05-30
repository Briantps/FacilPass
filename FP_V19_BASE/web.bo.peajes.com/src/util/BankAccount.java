package util;

import java.io.Serializable;

public class BankAccount implements Serializable{

	private static final long serialVersionUID = 1L;
	
    private Long accountBankIdU;
    
    private Long accountIdU;
    
    private String dateU;
    
    private Long bankIdU;
    
    private String bankNameU;
    
    private Long bankAvalU;
    
    private Long bankAccountIdU;
    
    private String bankAccountNumberU;
    
    private String productTypeDescriptionU;
	
	private Long stateU;

	/**
	 * @param accountBankIdU the accountBankIdU to set
	 */
	public void setAccountBankIdU(Long accountBankIdU) {
		this.accountBankIdU = accountBankIdU;
	}

	/**
	 * @return the accountBankIdU
	 */
	public Long getAccountBankIdU() {
		return accountBankIdU;
	}

	/**
	 * @param accountIdU the accountIdU to set
	 */
	public void setAccountIdU(Long accountIdU) {
		this.accountIdU = accountIdU;
	}

	/**
	 * @return the accountIdU
	 */
	public Long getAccountIdU() {
		return accountIdU;
	}

	/**
	 * @param dateU the dateU to set
	 */
	public void setDateU(String dateU) {
		this.dateU = dateU;
	}

	/**
	 * @return the dateU
	 */
	public String getDateU() {
		return dateU;
	}

	/**
	 * @param bankIdU the bankIdU to set
	 */
	public void setBankIdU(Long bankIdU) {
		this.bankIdU = bankIdU;
	}

	/**
	 * @return the bankIdU
	 */
	public Long getBankIdU() {
		return bankIdU;
	}

	/**
	 * @param bankNameU the bankNameU to set
	 */
	public void setBankNameU(String bankNameU) {
		this.bankNameU = bankNameU;
	}

	/**
	 * @return the bankNameU
	 */
	public String getBankNameU() {
		return bankNameU;
	}

	/**
	 * @param bankAvalU the bankAvalU to set
	 */
	public void setBankAvalU(Long bankAvalU) {
		this.bankAvalU = bankAvalU;
	}

	/**
	 * @return the bankAvalU
	 */
	public Long getBankAvalU() {
		return bankAvalU;
	}

	/**
	 * @param bankAccountIdU the bankAccountIdU to set
	 */
	public void setBankAccountIdU(Long bankAccountIdU) {
		this.bankAccountIdU = bankAccountIdU;
	}

	/**
	 * @return the bankAccountIdU
	 */
	public Long getBankAccountIdU() {
		return bankAccountIdU;
	}

	/**
	 * @param bankAccountNumberU the bankAccountNumberU to set
	 */
	public void setBankAccountNumberU(String bankAccountNumberU) {
		this.bankAccountNumberU = bankAccountNumberU;
	}

	/**
	 * @return the bankAccountNumberU
	 */
	public String getBankAccountNumberU() {
		return bankAccountNumberU;
	}

	/**
	 * @param productTypeDescriptionU the productTypeDescriptionU to set
	 */
	public void setProductTypeDescriptionU(String productTypeDescriptionU) {
		this.productTypeDescriptionU = productTypeDescriptionU;
	}

	/**
	 * @return the productTypeDescriptionU
	 */
	public String getProductTypeDescriptionU() {
		return productTypeDescriptionU;
	}

	/**
	 * @param stateU the stateU to set
	 */
	public void setStateU(Long stateU) {
		this.stateU = stateU;
	}

	/**
	 * @return the stateU
	 */
	public Long getStateU() {
		return stateU;
	}

}
