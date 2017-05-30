package jpa;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="RE_ACCOUNT_BANK")
public class ReAccountBank implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "REACCOUNT_BANK_SEQ", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "REACCOUNT_BANK_SEQ", sequenceName = "RE_ACCOUNT_BANK_SEQ",allocationSize=1)
	
	@Column(name="ACCOUNT_BANK_ID")
	private Long accountBankId;
	
	@ManyToOne
	@JoinColumn(name="ACCOUNT_ID")
	private TbAccount accountId;
	
	@ManyToOne
	@JoinColumn(name="BANK_ACCOUNT_ID")
	private TbBankAccount bankAccountId;
	
	@Column(name="DATE_ASSOCIATION")
	private Timestamp date;
	
	@Column(name="DATE_DISSOCIATION")
	private Timestamp dateDissociation;
	
	@Column(name="DATE_APPROVE_DISSOCIATION")
	private Timestamp dateApproveDissociation;
	
	private Long digits;
	
	@Column(name = "STATE_ACCOUNT_BANK")
	private Integer state_account_bank;
	
	@Column(name = "CODE_MONEY_BACK")
	private Integer codeMoneyBack;
	
	@Column(name="DATE_SEND_BANK")
	private Date dateSendBank;
	
	@Column(name="DATE_DISSOCIATION_BANK")
	private Timestamp dateDissosiationBank;
	
	@Column(name = "CODE_RESULT")
	private Long codeResult;
	
	private Long priority;	
	//bi-directional many-to-one association to TbPaymentMethod
    @ManyToOne
	@JoinColumn(name="PAYMENT_METHOD_ID")
	private TbPaymentMethod paymentMethodId;
	
	public ReAccountBank(){
		super();
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
	 * @param bankAccountId the bankAccountId to set
	 */
	public void setBankAccountId(TbBankAccount bankAccountId) {
		this.bankAccountId = bankAccountId;
	}

	/**
	 * @return the bankAccountId
	 */
	public TbBankAccount getBankAccountId() {
		return bankAccountId;
	}

	/**
	 * @param accountId the accountId to set
	 */
	public void setAccountId(TbAccount accountId) {
		this.accountId = accountId;
	}

	/**
	 * @return the accountId
	 */
	public TbAccount getAccountId() {
		return accountId;
	}

	/**
	 * @param digits the digits to set
	 */
	public void setDigits(Long digits) {
		this.digits = digits;
	}

	/**
	 * @return the digits
	 */
	public Long getDigits() {
		return digits;
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
	 * @param state_account_bank the state_account_bank to set
	 */
	public void setState_account_bank(Integer state_account_bank) {
		this.state_account_bank = state_account_bank;
	}

	/**
	 * @return the state_account_bank
	 */
	public Integer getState_account_bank() {
		return state_account_bank;
	}

	/**
	 * @param dateDissociation the dateDissociation to set
	 */
	public void setDateDissociation(Timestamp dateDissociation) {
		this.dateDissociation = dateDissociation;
	}

	/**
	 * @return the dateDissociation
	 */
	public Timestamp getDateDissociation() {
		return dateDissociation;
	}

	public void setCodeMoneyBack(Integer codeMoneyBack) {
		this.codeMoneyBack = codeMoneyBack;
	}

	public Integer getCodeMoneyBack() {
		return codeMoneyBack;
	}

	public void setDateSendBank(Date dateSendBank) {
		this.dateSendBank = dateSendBank;
	}

	public Date getDateSendBank() {
		return dateSendBank;
	}

	public void setDateDissosiationBank(Timestamp dateDissosiationBank) {
		this.dateDissosiationBank = dateDissosiationBank;
	}

	public Timestamp getDateDissosiationBank() {
		return dateDissosiationBank;
	}

	public void setCodeResult(Long codeResult) {
		this.codeResult = codeResult;
	}

	public Long getCodeResult() {
		return codeResult;
	}

	/**
	 * @param dateApproveDissociation the dateApproveDissociation to set
	 */
	public void setDateApproveDissociation(Timestamp dateApproveDissociation) {
		this.dateApproveDissociation = dateApproveDissociation;
	}

	/**
	 * @return the dateApproveDissociation
	 */
	public Timestamp getDateApproveDissociation() {
		return dateApproveDissociation;
	}

	public void setPaymentMethodId(TbPaymentMethod paymentMethodId) {
		this.paymentMethodId = paymentMethodId;
	}

	public TbPaymentMethod getPaymentMethodId() {
		return paymentMethodId;
	}

}
