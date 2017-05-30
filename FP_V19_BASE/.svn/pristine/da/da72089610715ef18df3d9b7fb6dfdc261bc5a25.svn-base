package jpa;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="TB_LOG_ADMIN_DEVICE")
public class TbLogAdminDevice implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "TB_LOG_ADMIN_DEVICE_SEQ", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "TB_LOG_ADMIN_DEVICE_SEQ", sequenceName = "TB_LOG_ADMIN_DEVICE_SEQ",allocationSize=1)
	@Column(name="LOG_ADMIN_DEVICE_ID")
	private Long logAdminDeviceId;
	
	@Column(name="USER_ID")
	private Long userId;
	
	@Column(name="DEVICE_FACIAL_ID_OLD")
	private String deviceFacialIdOld;
	
	@Column(name="DEVICE_FACIAL_ID_NEW")
	private String deviceFacialIdNew;
	
	@Column(name="PLATE")
	private String plate;
	
	@Column(name="COURIER_ID")
	private Long courierId;
	
	@Column(name="ROLL_ID")
	private Long rollId;
	
	@Column(name="COBRO")
	private BigDecimal cobro;
	
	@Column(name="OBSERVATION")
	private String observation;
	
	@Column(name="DATE_TRANSACTION")
	private Timestamp dateTransaction;
	
	@Column(name="ACTION_NAME")
	private String actionName;
	
	@Column(name="ACCOUNT_ID")
	private Long accountId;

	
	public TbLogAdminDevice(){
		
	}


	/**
	 * @param logAdminDeviceId the logAdminDeviceId to set
	 */
	public void setLogAdminDeviceId(Long logAdminDeviceId) {
		this.logAdminDeviceId = logAdminDeviceId;
	}


	/**
	 * @return the logAdminDeviceId
	 */
	public Long getLogAdminDeviceId() {
		return logAdminDeviceId;
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


	/**
	 * @param deviceFacialIdOld the deviceFacialIdOld to set
	 */
	public void setDeviceFacialIdOld(String deviceFacialIdOld) {
		this.deviceFacialIdOld = deviceFacialIdOld;
	}


	/**
	 * @return the deviceFacialIdOld
	 */
	public String getDeviceFacialIdOld() {
		return deviceFacialIdOld;
	}


	/**
	 * @param deviceFacialIdNew the deviceFacialIdNew to set
	 */
	public void setDeviceFacialIdNew(String deviceFacialIdNew) {
		this.deviceFacialIdNew = deviceFacialIdNew;
	}


	/**
	 * @return the deviceFacialIdNew
	 */
	public String getDeviceFacialIdNew() {
		return deviceFacialIdNew;
	}


	/**
	 * @param plate the plate to set
	 */
	public void setPlate(String plate) {
		this.plate = plate;
	}


	/**
	 * @return the plate
	 */
	public String getPlate() {
		return plate;
	}


	/**
	 * @param courierId the courierId to set
	 */
	public void setCourierId(Long courierId) {
		this.courierId = courierId;
	}


	/**
	 * @return the courierId
	 */
	public Long getCourierId() {
		return courierId;
	}


	/**
	 * @param rollId the rollId to set
	 */
	public void setRollId(Long rollId) {
		this.rollId = rollId;
	}


	/**
	 * @return the rollId
	 */
	public Long getRollId() {
		return rollId;
	}


	/**
	 * @param cobro the cobro to set
	 */
	public void setCobro(BigDecimal cobro) {
		this.cobro = cobro;
	}


	/**
	 * @return the cobro
	 */
	public BigDecimal getCobro() {
		return cobro;
	}


	/**
	 * @param observation the observation to set
	 */
	public void setObservation(String observation) {
		this.observation = observation;
	}


	/**
	 * @return the observation
	 */
	public String getObservation() {
		return observation;
	}


	/**
	 * @param dateTransaction the dateTransaction to set
	 */
	public void setDateTransaction(Timestamp dateTransaction) {
		this.dateTransaction = dateTransaction;
	}


	/**
	 * @return the dateTransaction
	 */
	public Timestamp getDateTransaction() {
		return dateTransaction;
	}


	/**
	 * @param actionName the actionName to set
	 */
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}


	/**
	 * @return the actionName
	 */
	public String getActionName() {
		return actionName;
	}


	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}


	public Long getAccountId() {
		return accountId;
	}
}
