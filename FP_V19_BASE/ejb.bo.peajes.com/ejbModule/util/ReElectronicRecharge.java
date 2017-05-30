package util;

import java.io.Serializable;

public class ReElectronicRecharge implements Serializable{

	private static final long serialVersionUID = 1L;
	
    private String AccountBankId;
    private String accountId;     
    private String dateAssociation;
    private Long accountState;
    
      
    public ReElectronicRecharge(){
    	super();
    }

	/**
	 * @param accountId the accountId to set
	 */
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	/**
	 * @return the accountId
	 */
	public String getAccountId() {
		return accountId;
	}

	public void setBankAccountId(String string) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param dateAssociation the dateAssociation to set
	 */
	public void setDateAssociation(String dateAssociation) {
		this.dateAssociation = dateAssociation;
	}

	/**
	 * @return the dateAssociation
	 */
	public String getDateAssociation() {
		return dateAssociation;
	}

	/**
	 * @param accountBankId the accountBankId to set
	 */
	public void setAccountBankId(String accountBankId) {
		AccountBankId = accountBankId;
	}

	/**
	 * @return the accountBankId
	 */
	public String getAccountBankId() {
		return AccountBankId;
	}

	/**
	 * @param accountState the accountState to set
	 */
	public void setAccountState(Long accountState) {
		this.accountState = accountState;
	}

	/**
	 * @return the accountState
	 */
	public Long getAccountState() {
		return accountState;
	}

}
