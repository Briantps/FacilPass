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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The persistent class for the RE_ACCOUNT_DEVICE database table.
 * @author tmolina
 */
@Entity
@Table(name="RE_ACCOUNT_DEVICE")
public class ReAccountDevice implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RE_ACCOUNT_DEVICE_REACCOUNTDEVICEID_GENERATOR")
	@SequenceGenerator(name = "RE_ACCOUNT_DEVICE_REACCOUNTDEVICEID_GENERATOR", sequenceName = "RE_ACCOUNT_DEVICE_SEQ")
	@Column(name="ACCOUNT_DEVICE_ID")
	private Long accountDeviceId;
	
	@ManyToOne
	@JoinColumn(name="DEVICE_ID")
	private TbDevice tbDevice;
	
	@ManyToOne
	@JoinColumn(name="ACCOUNT_ID")
	private TbAccount tbAccount;
	
	@ManyToOne
	@JoinColumn(name="VEHICLE_ID")
	private TbVehicle tbVehicle;
	
	@Column(name="RELATION_STATE")
	private Integer relationState;
		
	/**
	 * Constructor
	 */
	public ReAccountDevice(){
		super();
	}

	/**
	 * @param accountDeviceId the accountDeviceId to set
	 */
	public void setAccountDeviceId(Long accountDeviceId) {
		this.accountDeviceId = accountDeviceId;
	}

	/**
	 * @return the accountDeviceId
	 */
	public Long getAccountDeviceId() {
		return accountDeviceId;
	}

	/**
	 * @param tbDevice the tbDevice to set
	 */
	public void setTbDevice(TbDevice tbDevice) {
		this.tbDevice = tbDevice;
	}

	/**
	 * @return the tbDevice
	 */
	public TbDevice getTbDevice() {
		return tbDevice;
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
	 * @param relationState the relationState to set
	 */
	public void setRelationState(Integer relationState) {
		this.relationState = relationState;
	}

	/**
	 * @return the relationState
	 */
	public Integer getRelationState() {
		return relationState;
	}
}