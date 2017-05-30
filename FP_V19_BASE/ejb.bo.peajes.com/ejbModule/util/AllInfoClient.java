package util;

import java.io.Serializable;

//import jpa.ReAccountDevice;
//import jpa.TbAccount;
import jpa.TbCodeType;
import jpa.TbUserData;

public class AllInfoClient implements Serializable{
	private static final long serialVersionUID = -8394058584955856126L;
	
	private TbUserData tbUserData;
	private TbUserData tbMunicipality;
	private TbCodeType codeTypeId;
	
	private Long userIdU;
	private Long userDataIdU;
	private String creationDateU;
	private Long codeTypeU;
	private String codeTypeAbbrU;
	private String numberDocU;
	private String nameU;
	private String secondNameU;
	private String emailU;		
	private Long accountIdU;
	private Long stateIdU;
	private String stateDescU;
	private String phoneU;
	private String optionalPhoneU;
	private String addressU;
	private String cityU;
	private String distributionTypeU;
	private Long balanceAccountU;
	private String nameBankU;
	private Long digitsBankU;
	private String placaU;
	private Long categoryU;
	private String tagIdU;
	private String facialU;
	private String aditional1U;
	private String aditional2U;
	private String aditional3U;
	
	
	 public AllInfoClient(){
	    	super();
	    }


	public void setTbUserData(TbUserData tbUserData) {
		this.tbUserData = tbUserData;
	}


	public TbUserData getTbUserData() {
		return tbUserData;
	}


	public void setTbMunicipality(TbUserData tbMunicipality) {
		this.tbMunicipality = tbMunicipality;
	}


	public TbUserData getTbMunicipality() {
		return tbMunicipality;
	}


	public void setCodeTypeId(TbCodeType codeTypeId) {
		this.codeTypeId = codeTypeId;
	}


	public TbCodeType getCodeTypeId() {
		return codeTypeId;
	}


	public void setUserIdU(Long userIdU) {
		this.userIdU = userIdU;
	}


	public Long getUserIdU() {
		return userIdU;
	}


	public void setUserDataIdU(Long userDataIdU) {
		this.userDataIdU = userDataIdU;
	}


	public Long getUserDataIdU() {
		return userDataIdU;
	}


	public void setCreationDateU(String creationDateU) {
		this.creationDateU = creationDateU;
	}


	public String getCreationDateU() {
		return creationDateU;
	}


	public void setCodeTypeU(Long codeTypeU) {
		this.codeTypeU = codeTypeU;
	}


	public Long getCodeTypeU() {
		return codeTypeU;
	}


	public void setCodeTypeAbbrU(String codeTypeAbbrU) {
		this.codeTypeAbbrU = codeTypeAbbrU;
	}


	public String getCodeTypeAbbrU() {
		return codeTypeAbbrU;
	}


	public void setNumberDocU(String numberDocU) {
		this.numberDocU = numberDocU;
	}


	public String getNumberDocU() {
		return numberDocU;
	}


	public void setNameU(String nameU) {
		this.nameU = nameU;
	}


	public String getNameU() {
		return nameU;
	}


	public void setSecondNameU(String secondNameU) {
		this.secondNameU = secondNameU;
	}


	public String getSecondNameU() {
		return secondNameU;
	}


	public void setEmailU(String emailU) {
		this.emailU = emailU;
	}


	public String getEmailU() {
		return emailU;
	}


	public void setAccountIdU(Long accountIdU) {
		this.accountIdU = accountIdU;
	}


	public Long getAccountIdU() {
		return accountIdU;
	}


	public void setStateIdU(Long stateIdU) {
		this.stateIdU = stateIdU;
	}


	public Long getStateIdU() {
		return stateIdU;
	}


	public void setStateDescU(String stateDescU) {
		this.stateDescU = stateDescU;
	}


	public String getStateDescU() {
		return stateDescU;
	}


	public void setPhoneU(String phoneU) {
		this.phoneU = phoneU;
	}


	public String getPhoneU() {
		return phoneU;
	}


	public void setOptionalPhoneU(String optionalPhoneU) {
		this.optionalPhoneU = optionalPhoneU;
	}


	public String getOptionalPhoneU() {
		return optionalPhoneU;
	}


	public void setAddressU(String addressU) {
		this.addressU = addressU;
	}


	public String getAddressU() {
		return addressU;
	}


	public void setCityU(String cityU) {
		this.cityU = cityU;
	}


	public String getCityU() {
		return cityU;
	}


	public void setDistributionTypeU(String distributionTypeU) {
		this.distributionTypeU = distributionTypeU;
	}


	public String getDistributionTypeU() {
		return distributionTypeU;
	}


	public void setBalanceAccountU(Long balanceAccountU) {
		this.balanceAccountU = balanceAccountU;
	}


	public Long getBalanceAccountU() {
		return balanceAccountU;
	}


	public void setNameBankU(String nameBankU) {
		this.nameBankU = nameBankU;
	}


	public String getNameBankU() {
		return nameBankU;
	}


	public void setDigitsBankU(Long digitsBankU) {
		this.digitsBankU = digitsBankU;
	}


	public Long getDigitsBankU() {
		return digitsBankU;
	}


	public void setPlacaU(String placaU) {
		this.placaU = placaU;
	}


	public String getPlacaU() {
		return placaU;
	}


	public void setCategoryU(Long categoryU) {
		this.categoryU = categoryU;
	}


	public Long getCategoryU() {
		return categoryU;
	}


	public void setTagIdU(String tagIdU) {
		this.tagIdU = tagIdU;
	}


	public String getTagIdU() {
		return tagIdU;
	}


	public void setFacialU(String facialU) {
		this.facialU = facialU;
	}


	public String getFacialU() {
		return facialU;
	}


	/**
	 * @param aditional1U the aditional1U to set
	 */
	public void setAditional1U(String aditional1U) {
		this.aditional1U = aditional1U;
	}


	/**
	 * @return the aditional1U
	 */
	public String getAditional1U() {
		return aditional1U;
	}


	/**
	 * @param aditional2U the aditional2U to set
	 */
	public void setAditional2U(String aditional2U) {
		this.aditional2U = aditional2U;
	}


	/**
	 * @return the aditional2U
	 */
	public String getAditional2U() {
		return aditional2U;
	}


	/**
	 * @param aditional3U the aditional3U to set
	 */
	public void setAditional3U(String aditional3U) {
		this.aditional3U = aditional3U;
	}


	/**
	 * @return the aditional3U
	 */
	public String getAditional3U() {
		return aditional3U;
	}
	



}