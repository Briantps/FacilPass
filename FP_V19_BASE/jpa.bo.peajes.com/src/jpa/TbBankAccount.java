/**
 * 
 */
package jpa;

import java.io.Serializable;
import java.sql.Timestamp;

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
 * The persistent class for the TB_BANK_ACCOUNT database table.
 * @author tmolina
 *
 */
@Entity
@Table(name="TB_BANK_ACCOUNT")
public class TbBankAccount implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "TBBANKACCOUNT_BANKACCOUNTID_GENERATOR", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "TBBANKACCOUNT_BANKACCOUNTID_GENERATOR", sequenceName = "TB_BANK_ACCOUNT_SEQ",allocationSize=1)
	@Column(name = "BANK_ACCOUNT_ID")
	private Long bankAccountId;
	
	@Column(name="BANK_ACCOUNT_HOLDER")
	private String bankAccountHolder;
	
	@Column(name="BANK_ACCOUNT_HOLDER_NIT")
	private String bankAccountHolderNit;
	
	@Column(name="BANK_ACCOUNT_NUMBER")
	private String bankAccountNumber;
	
	@ManyToOne
	@JoinColumn(name="BANK_ACCOUNT_TYPE")
	private TbProductType bankAccountType;
	
	@Column(name="DATE_CREATION")
	private Timestamp date;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	
	@Column(name="INIT_VALUE")
	private Long initValueRecharge;
	
	@ManyToOne
	@JoinColumn(name = "PRODUCT")
	private TbBank product;
	
	@ManyToOne
	@JoinColumn(name = "USRS")
	private TbSystemUser usrs;
	

	/**
	 * Constructor.
	 */
	public TbBankAccount() {
		super();
	}

	/**
	 * @param bankAccountId the bankAccountId to set
	 */
	public void setBankAccountId(Long bankAccountId) {
		this.bankAccountId = bankAccountId;
	}

	/**
	 * @return the bankAccountId
	 */
	public Long getBankAccountId() {
		return bankAccountId;
	}

	/**
	 * @param bankAccountHolder the bankAccountHolder to set
	 */
	public void setBankAccountHolder(String bankAccountHolder) {
		this.bankAccountHolder = bankAccountHolder;
	}

	/**
	 * @return the bankAccountHolder
	 */
	public String getBankAccountHolder() {
		return bankAccountHolder;
	}

	/**
	 * @param bankAccountHolderNit the bankAccountHolderNit to set
	 */
	public void setBankAccountHolderNit(String bankAccountHolderNit) {
		this.bankAccountHolderNit = bankAccountHolderNit;
	}

	/**
	 * @return the bankAccountHolderNit
	 */
	public String getBankAccountHolderNit() {
		return bankAccountHolderNit;
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
	 * @param product the product to set
	 */
	public void setProduct(TbBank product) {
		this.product = product;
	}

	/**
	 * @return the product
	 */
	public TbBank getProduct() {
		return this.product;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Timestamp date) {
		this.date = date;
	}

	/**
	 * @return the date
	 */
	public Timestamp getDate() {
		return date;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param usrs the usrs to set
	 */
	public void setUsrs(TbSystemUser usrs) {
		this.usrs = usrs;
	}

	/**
	 * @return the usrs
	 */
	public TbSystemUser getUsrs() {
		return usrs;
	}

	/**
	 * @param bankAccountType the bankAccountType to set
	 */
	public void setBankAccountType(TbProductType bankAccountType) {
		this.bankAccountType = bankAccountType;
	}

	/**
	 * @return the bankAccountType
	 */
	public TbProductType getBankAccountType() {
		return bankAccountType;
	}

	/**
	 * @param initValueRecharge the initValueRecharge to set
	 */
	public void setInitValueRecharge(Long initValueRecharge) {
		this.initValueRecharge = initValueRecharge;
	}

	/**
	 * @return the initValueRecharge
	 */
	public Long getInitValueRecharge() {
		return initValueRecharge;
	}
}