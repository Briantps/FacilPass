package util;

import java.io.Serializable;

public class ClientRecharge implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long automaticRechargeId;
	
    private String bankName;
    
    private String bankAccountNumber;
    
    private Long accountId;
    
    private Long automaticRechargeValue;
    
    private Long frecuencyId;
    
    private String frequencyDescript;
    
    private String automaticRechargeDate;   
    
    private Integer estado;
    
    //Added fields to hold FRD152 data, Cristhian Buchely
    
    private Integer programmingType;
    
    private Double minimumBalance;
      
    public ClientRecharge(){
    	super();
    }
 
 /** Setters/Getters for the FRD152 data
 */
 public Integer getProgrammingType(){ return programmingType; }
 public void setProgrammingType(Integer type){programmingType=type;}
     
 public Double getMinimumBalance(){ return minimumBalance; }
 public void setMinimumBalance(Double var){minimumBalance=var;}


	/**
	 * @param bankName the bankName to set
	 */
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}


	/**
	 * @return the bankName
	 */
	public String getBankName() {
		return bankName;
	}


	/**
	 * @param bankAccountNumber the bankAccountNumber to set
	 */
	public void setBankAccountNumber(String bankAccountNumber) {
		this.bankAccountNumber = bankAccountNumber;
	}


	/**
	 * @return the bankAccountNumber
	 */
	public String getBankAccountNumber() {
		return bankAccountNumber;
	}


	/**
	 * @param accountId the accountId to set
	 */
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}


	/**
	 * @return the accountId
	 */
	public Long getAccountId() {
		return accountId;
	}


	/**
	 * @param automaticRechargeValue the automaticRechargeValue to set
	 */
	public void setAutomaticRechargeValue(Long automaticRechargeValue) {
		this.automaticRechargeValue = automaticRechargeValue;
	}


	/**
	 * @return the automaticRechargeValue
	 */
	public Long getAutomaticRechargeValue() {
		return automaticRechargeValue;
	}


	/**
	 * @param frecuencyId the frecuencyId to set
	 */
	public void setFrecuencyId(Long frecuencyId) {
		this.frecuencyId = frecuencyId;
	}


	/**
	 * @return the frecuencyId
	 */
	public Long getFrecuencyId() {
		return frecuencyId;
	}

	/**
	 * @param frequencyDescript the frequencyDescript to set
	 */
	public void setFrequencyDescript(String frequencyDescript) {
		this.frequencyDescript = frequencyDescript;
	}


	/**
	 * @return the frequencyDescript
	 */
	public String getFrequencyDescript() {
		return frequencyDescript;
	}


	/**
	 * @param automaticRechargeDate the automaticRechargeDate to set
	 */
	public void setAutomaticRechargeDate(String automaticRechargeDate) {
		this.automaticRechargeDate = automaticRechargeDate;
	}


	/**
	 * @return the automaticRechargeDate
	 */
	public String getAutomaticRechargeDate() {
		return automaticRechargeDate;
	}


	/**
	 * @param estado the estado to set
	 */
	public void setEstado(Integer estado) {
		this.estado = estado;
	}


	/**
	 * @return the estado
	 */
	public Integer getEstado() {
		return estado;
	}


	/**
	 * @param automaticRechargeId the automaticRechargeId to set
	 */
	public void setAutomaticRechargeId(Long automaticRechargeId) {
		this.automaticRechargeId = automaticRechargeId;
	}


	/**
	 * @return the automaticRechargeId
	 */
	public Long getAutomaticRechargeId() {
		return automaticRechargeId;
	}

}