/**
 * 
 */
package jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
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
	@SequenceGenerator(name="TBBANK_BANKID_GENERATOR",sequenceName="TB_BANK_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator ="TBBANK_BANKID_GENERATOR")
	@Column(name="BANK_ID")
	private Long bankId;
	
	@Column(name="BANK_NAME")
	private String bankName;

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
}