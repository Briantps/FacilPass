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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The persistent class for the TB_TRANSACTION database table.
 * 
 */
@Entity
@Table(name = "TB_TRANSACTION")
public class TbTransaction implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "TB_TRANSACTION_TRANSACTIONID_GENERATOR", sequenceName = "TB_TRANSACTION_SEQ",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TB_TRANSACTION_TRANSACTIONID_GENERATOR")
	@Column(name = "TRANSACTION_ID", unique = true, nullable = false, precision = 19)
	private Long transactionId;
	
	@ManyToOne
	@JoinColumn(name="ACCOUNT_ID")
	private TbAccount tbAccount;
	
	@ManyToOne
	@JoinColumn(name="TRANSACTION_TYPE_ID")
	private TbTransactionType tbTransactionType;
	
	@Column(name = "TRANSACTION_TIME", nullable = false)
	private Timestamp transactionTime;
	
	@Column(name = "PREVIOUS_BALANCE", nullable = false)
	private Long previousBalance;
	
	@Column(name = "NEW_BALANCE", nullable = false)
	private Long newBalance;
	
	@Column(name = "TRANSACTION_VALUE", nullable = false)
	private Long transactionValue;
	
	@Column(name="TRANSACTION_DESCRIPTION", length=4000)
	private String transactionDescription;

	@Column(name = "BACK_OFFICE_TIME")
	private Timestamp backOfficeTime;

	@ManyToOne
	@JoinColumn(name = "DEVICE_ID")
	private TbDevice tbDevice;

	@ManyToOne
	@JoinColumn(name = "LANE_ID")
	private TbLane tbLane;
	
	@OneToOne
	@JoinColumn(name="CONSIGNMENT_ID")
	private TbConsignment tbConsignment;

	@ManyToOne
	@JoinColumn(name="STATION_BO_ID")
	private TbStationBO tbStationBO;
	
	@ManyToOne
	@JoinColumn(name = "VEHICLE_ID")
	private TbVehicle tbVehicle;
	
	
	@Column(name="TRANSACTION_PROCESS_TIME")
	private Timestamp transactionProcess;
	
	@Column(name="USER_ID")
	private Long userId;
	
	@Column(name = "CODE_RESULT")
	private String codeResult;
	
	@Column(name = "ACCOUNT_BANK_ID")
	private Long accountBankId;
	
	/**
	 * Constructor.
	 */
	public TbTransaction() {
		super();
	}

	/**
	 * 
	 * @param transactionId
	 */
	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	/**
	 * 
	 * @return
	 */
	public Long getTransactionId() {
		return transactionId;
	}

	/**
	 * 
	 * @param tbAccount
	 */
	public void setTbAccount(TbAccount tbAccount) {
		this.tbAccount = tbAccount;
	}

	/**
	 * 
	 * @return
	 */
	public TbAccount getTbAccount() {
		return tbAccount;
	}

	/**
	 * 
	 * @param tbTransactionType
	 */
	public void setTbTransactionType(TbTransactionType tbTransactionType) {
		this.tbTransactionType = tbTransactionType;
	}

	/**
	 * 
	 * @return
	 */
	public TbTransactionType getTbTransactionType() {
		return tbTransactionType;
	}

	/**
	 * 
	 * @param transactionTime
	 */
	public void setTransactionTime(Timestamp transactionTime) {
		this.transactionTime = transactionTime;
	}

	/**
	 * 
	 * @return
	 */
	public Timestamp getTransactionTime() {
		return transactionTime;
	}

	/**
	 * 
	 * @param previousBalance
	 */
	public void setPreviousBalance(Long previousBalance) {
		this.previousBalance = previousBalance;
	}

	/**
	 * 
	 * @return
	 */
	public Long getPreviousBalance() {
		return previousBalance;
	}

	/**
	 * 
	 * @param newBalance
	 */
	public void setNewBalance(Long newBalance) {
		this.newBalance = newBalance;
	}

	/**
	 * 
	 * @return
	 */
	public Long getNewBalance() {
		return newBalance;
	}

	/**
	 * 
	 * @param transactionValue
	 */
	public void setTransactionValue(Long transactionValue) {
		this.transactionValue = transactionValue;
	}

	/**
	 * 
	 * @return
	 */
	public Long getTransactionValue() {
		return transactionValue;
	}

	/**
	 * 
	 * @param transactionDescription
	 */
	public void setTransactionDescription(String transactionDescription) {
		this.transactionDescription = transactionDescription;
	}

	/**
	 * 
	 * @return
	 */
	public String getTransactionDescription() {
		return transactionDescription;
	}

	/**
	 * 
	 * @param backOfficeTime
	 */
	public void setBackOfficeTime(Timestamp backOfficeTime) {
		this.backOfficeTime = backOfficeTime;
	}

	/**
	 * 
	 * @return
	 */
	public Timestamp getBackOfficeTime() {
		return backOfficeTime;
	}

	/**
	 * 
	 * @param tbDevice
	 */
	public void setTbDevice(TbDevice tbDevice) {
		this.tbDevice = tbDevice;
	}

	/**
	 * 
	 * @return
	 */
	public TbDevice getTbDevice() {
		return tbDevice;
	}

	/**
	 * 
	 * @param tbLane
	 */
	public void setTbLane(TbLane tbLane) {
		this.tbLane = tbLane;
	}

	/**
	 * 
	 * @return
	 */
	public TbLane getTbLane() {
		return tbLane;
	}

	/**
	 * 
	 * @param tbConsignment
	 */
	public void setTbConsignment(TbConsignment tbConsignment) {
		this.tbConsignment = tbConsignment;
	}

	/**
	 * 
	 * @return
	 */
	public TbConsignment getTbConsignment() {
		return tbConsignment;
	}

	/**
	 * 
	 * @param tbStationBO
	 */
	public void setTbStationBO(TbStationBO tbStationBO) {
		this.tbStationBO = tbStationBO;
	}

	/**
	 * 
	 * @return
	 */
	public TbStationBO getTbStationBO() {
		return tbStationBO;
	}
	
	/**
	 * @param tbVehicle the tbVehicle to set
	 */
	public void setTbVehicle(TbVehicle tbVehicle) {
		this.tbVehicle = tbVehicle;
	}

	/**
	 * @return the tbVehicle
	 */
	public TbVehicle getTbVehicle() {
		return tbVehicle;
	}

	/**
	 * @param transactionProcess the transactionProcess to set
	 */
	public void setTransactionProcess(Timestamp transactionProcess) {
		this.transactionProcess = transactionProcess;
	}

	/**
	 * @return the transactionProcess
	 */
	public Timestamp getTransactionProcess() {
		return transactionProcess;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}

	public void setCodeResult(String codeResult) {
		this.codeResult = codeResult;
	}

	public String getCodeResult() {
		return codeResult;
	}

	public void setAccountBankId(Long accountBankId) {
		this.accountBankId = accountBankId;
	}

	public Long getAccountBankId() {
		return accountBankId;
	}
}