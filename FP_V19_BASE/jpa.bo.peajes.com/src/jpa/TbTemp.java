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
 * The persistent class for the TB_DEVICE database table.
 * 
 */
@Entity
@Table(name="TB_TEMP")
public class TbTemp implements Serializable {	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TB_TEMP_GENERATOR", sequenceName="TB_TEMP_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TB_TEMP_GENERATOR")
	@Column(name="TEMP_ID", unique=true, nullable=false, precision=19)
	private Long tempId;

	@Column(name="DEVICE_CODE", nullable=false, length=50)
	private String deviceCode;

	@Column(name="DEVICE_FACIAL_ID")
	private String deviceFacialId;
	
	@Column(name="ROLL_ID")
	private Long rollId;
	
	@Column(name="COURIER_ID")
	private Long courierId;

	/**
	 * Constructor
	 */
    public TbTemp() {
    }


	public void setTempId(Long tempId) {
		this.tempId = tempId;
	}


	public Long getTempId() {
		return tempId;
	}


	/**
	 * 
	 * @return deviceCode
	 */
	public String getDeviceCode() {
		return this.deviceCode;
	}

	/**
	 * Setter for deviceCode
	 * @param deviceCode
	 */
	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}
	

	/**
	 * @param deviceFacialId the deviceFacialId to set
	 */
	public void setDeviceFacialId(String deviceFacialId) {
		this.deviceFacialId = deviceFacialId;
	}

	/**
	 * @return the deviceFacialId
	 */
	public String getDeviceFacialId() {
		return deviceFacialId;
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

}