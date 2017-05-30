/**
 * 
 */
package jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The persistent class for the TB_BANK database table.
 * @author tmolina
 *
 */
@Entity
@Table(name="TB_BANK")
public class TbBank implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="BANK_ID")
	private Long bankId;
	
	@Column(name="BANK_NAME")
	private String bankName;

	@Column(name="BANK_CODE")
	private String bankCode;
	
	@Column(name="BANK_AVAL")
	private Long bankAval;
	
	/**
	 * TPS FRD152
	 * @author Cristhian David Buchely
	 */
	
	@Column(name="BANK_PROGRAMMING")
	private Long bankProgramming;
		
	@Column(name="BANK_URL")
	private String bankUrl;
	
	/**
	 * Constructor.
	 */
	public TbBank() {
		super();
	}

	/**
	 * @param bankId the bankId to set
	 */
	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}

	/**
	 * @return the bankId
	 */
	public Long getBankId() {
		return bankId;
	}

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

	public void setBankUrl(String bankUrl) {
		this.bankUrl = bankUrl;
	}

	public String getBankUrl() {
		return bankUrl;
	}
	
	public Long getBankProgramming() {
		return bankProgramming;
	}

	public void setBankProgramming(Long bankProgramming) {
		this.bankProgramming = bankProgramming;
	}

}