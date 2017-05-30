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
 * The persistent class for the TB_CONSIGNMENT database table.
 * @author tmolina
 *
 */
@Entity
@Table(name="TB_CONSIGNMENT")
public class TbConsignment implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TB_CONSIGNMENT_ICOSNSIGNMENTID_GENERATOR")
	@SequenceGenerator(name="TB_CONSIGNMENT_ICOSNSIGNMENTID_GENERATOR", sequenceName="TB_CONSIGNMENT_SEQ")
	@Column(name="CONSIGNMENT_ID")
	private Long consignmentId;
	
	@ManyToOne
	@JoinColumn(name="ACCOUNT_ID")
	private TbAccount tbAccount;
	
	@Column(name="CONSIGNMENT_NUMBER")
	private String consignmentNumber;
	
	@Column(name="CONSIGNMENT_VALUE")
	private Long consignmentValue;
	
	@Column(name="CONSIGNMENT_DATE")
	private Timestamp consignmentDate;
	
	@Column(name="CONSIGNMENT_REGISTER_DATE")
	private Timestamp consignmentRegisterDate;
	
	@ManyToOne
	@JoinColumn(name="PAY_TYPE_ID")
	private TbPayType tbPayType;
	
	@Column(name="ORIGIN_ACCOUNT_NUMBER")
	private String originAccountNumber;
	
	@Column(name="CONSIGNMENT_OFFICE")
	private String consignmentOffice;
	
	@Column(name="CONSIGNMENT_CHECK_NUMBER")
	private String consignmentCheckNumber;
	
	@ManyToOne
	@JoinColumn(name="BANK_ID")
	private TbBank tbBank;
	
	@ManyToOne
	@JoinColumn(name="BANK_ACCOUNT_ID")
	private TbBankAccount tbBankAccount;
	
	@ManyToOne
	@JoinColumn(name="CONSIGNMENT_STATE_ID")
	private TbConsignmentState tbConsignmentState;
	
	@Column(name="CONSIGNMENT_CONCEPT")
	private String consignmentConcept;
	
	/**
	 * Constructor.
	 */
	public TbConsignment(){
		super();
	}

	/**
	 * @param consignmentId the consignmentId to set
	 */
	public void setConsignmentId(Long consignmentId) {
		this.consignmentId = consignmentId;
	}

	/**
	 * @return the consignmentId
	 */
	public Long getConsignmentId() {
		return consignmentId;
	}

	/**
	 * @return the consignmentNumber
	 */
	public String getConsignmentNumber() {
		return consignmentNumber;
	}

	/**
	 * @param consignmentNumber the consignmentNumber to set
	 */
	public void setConsignmentNumber(String consignmentNumber) {
		this.consignmentNumber = consignmentNumber;
	}

	/**
	 * @return the consignmentValue
	 */
	public Long getConsignmentValue() {
		return consignmentValue;
	}

	/**
	 * @param consignmentValue the consignmentValue to set
	 */
	public void setConsignmentValue(Long consignmentValue) {
		this.consignmentValue = consignmentValue;
	}

	/**
	 * @return the consignmentDate
	 */
	public Timestamp getConsignmentDate() {
		return consignmentDate;
	}

	/**
	 * @param consignmentDate the consignmentDate to set
	 */
	public void setConsignmentDate(Timestamp consignmentDate) {
		this.consignmentDate = consignmentDate;
	}

	/**
	 * @return the consignmentRegisterDate
	 */
	public Timestamp getConsignmentRegisterDate() {
		return consignmentRegisterDate;
	}

	/**
	 * @param consignmentRegisterDate the consignmentRegisterDate to set
	 */
	public void setConsignmentRegisterDate(Timestamp consignmentRegisterDate) {
		this.consignmentRegisterDate = consignmentRegisterDate;
	}

	/**
	 * @param originAccountNumber the originAccountNumber to set
	 */
	public void setOriginAccountNumber(String originAccountNumber) {
		this.originAccountNumber = originAccountNumber;
	}

	/**
	 * @return the originAccountNumber
	 */
	public String getOriginAccountNumber() {
		return originAccountNumber;
	}

	/**
	 * @param consignmentOffice the consignmentOffice to set
	 */
	public void setConsignmentOffice(String consignmentOffice) {
		this.consignmentOffice = consignmentOffice;
	}

	/**
	 * @return the consignmentOffice
	 */
	public String getConsignmentOffice() {
		return consignmentOffice;
	}

	/**
	 * @param consignmentCheckNumber the consignmentCheckNumber to set
	 */
	public void setConsignmentCheckNumber(String consignmentCheckNumber) {
		this.consignmentCheckNumber = consignmentCheckNumber;
	}

	/**
	 * @return the consignmentCheckNumber
	 */
	public String getConsignmentCheckNumber() {
		return consignmentCheckNumber;
	}

	/**
	 * @param tbBank the tbBank to set
	 */
	public void setTbBank(TbBank tbBank) {
		this.tbBank = tbBank;
	}

	/**
	 * @return the tbBank
	 */
	public TbBank getTbBank() {
		return tbBank;
	}

	/**
	 * @param tbBankAccount the tbBankAccount to set
	 */
	public void setTbBankAccount(TbBankAccount tbBankAccount) {
		this.tbBankAccount = tbBankAccount;
	}

	/**
	 * @return the tbBankAccount
	 */
	public TbBankAccount getTbBankAccount() {
		return tbBankAccount;
	}

	/**
	 * @param tbPayType the tbPayType to set
	 */
	public void setTbPayType(TbPayType tbPayType) {
		this.tbPayType = tbPayType;
	}

	/**
	 * @return the tbPayType
	 */
	public TbPayType getTbPayType() {
		return tbPayType;
	}

	/**
	 * @param tbAccount the tbAccount to set
	 */
	public void setTbAccount(TbAccount tbAccount) {
		this.tbAccount = tbAccount;
	}

	/**
	 * @return the tbAccount
	 */
	public TbAccount getTbAccount() {
		return tbAccount;
	}

	/**
	 * @param tbConsignmentState the tbConsignmentState to set
	 */
	public void setTbConsignmentState(TbConsignmentState tbConsignmentState) {
		this.tbConsignmentState = tbConsignmentState;
	}

	/**
	 * @return the tbConsignmentState
	 */
	public TbConsignmentState getTbConsignmentState() {
		return tbConsignmentState;
	}

	/**
	 * @param consignmentConcept the consignmentConcept to set
	 */
	public void setConsignmentConcept(String consignmentConcept) {
		this.consignmentConcept = consignmentConcept;
	}

	/**
	 * @return the consignmentConcept
	 */
	public String getConsignmentConcept() {
		return consignmentConcept;
	}
}