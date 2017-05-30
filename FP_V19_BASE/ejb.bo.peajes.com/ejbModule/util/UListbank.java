package util;

import java.io.Serializable;

public class UListbank implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long bankId;
	private String bankName;
	private String bankAval;
	private boolean bankProgramming;
	private String bankUrl;
	public Long getBankId() {
		return bankId;
	}
	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankAval() {
		return bankAval;
	}
	public void setBankAval(String bankAval) {
		this.bankAval = bankAval;
	}
	public boolean isBankProgramming() {
		return bankProgramming;
	}
	public void setBankProgramming(boolean bankProgramming) {
		this.bankProgramming = bankProgramming;
	}
	public String getBankUrl() {
		return bankUrl;
	}
	public void setBankUrl(String bankUrl) {
		this.bankUrl = bankUrl;
	}
	
	
}
