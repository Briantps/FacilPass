package util;

import java.io.Serializable;

public class ReAccountBank implements Serializable{

	private static final long serialVersionUID = 1L;
	
    private String date;
    
    private String client;
    
    private String bank;
    
    private long bankAval;
    
    private String account;
    
    private String typeProduct;
    
    private String desc;
    
    private String accountId;
    
    private String accountState;
    
    private Long priority;
    
    private Long accountBankId;
    
    
    public ReAccountBank(){
    	super();
    }

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param client the client to set
	 */
	public void setClient(String client) {
		this.client = client;
	}

	/**
	 * @return the client
	 */
	public String getClient() {
		return client;
	}

	/**
	 * @param bank the bank to set
	 */
	public void setBank(String bank) {
		this.bank = bank;
	}

	/**
	 * @return the bank
	 */
	public String getBank() {
		return bank;
	}

	/**
	 * @param account the account to set
	 */
	public void setAccount(String account) {
		this.account = account;
	}

	/**
	 * @return the account
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * @param typeProduct the typeProduct to set
	 */
	public void setTypeProduct(String typeProduct) {
		this.typeProduct = typeProduct;
	}

	/**
	 * @return the typeProduct
	 */
	public String getTypeProduct() {
		return typeProduct;
	}

	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
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

	/**
	 * @param accountState the accountState to set
	 */
	public void setAccountState(String accountState) {
		this.accountState = accountState;
	}

	/**
	 * @return the accountState
	 */
	public String getAccountState() {
		return accountState;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(Long priority) {
		this.priority = priority;
	}

	/**
	 * @return the priority
	 */
	public Long getPriority() {
		return priority;
	}

	/**
	 * @param accountBankId the accountBankId to set
	 */
	public void setAccountBankId(Long accountBankId) {
		this.accountBankId = accountBankId;
	}

	/**
	 * @return the accountBankId
	 */
	public Long getAccountBankId() {
		return accountBankId;
	}

	public void setBankAval(long bankAval) {
		this.bankAval = bankAval;
	}

	public long getBankAval() {
		return bankAval;
	}



}
