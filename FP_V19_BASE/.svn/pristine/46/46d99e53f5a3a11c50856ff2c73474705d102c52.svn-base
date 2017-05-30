package jpa;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="RE_VALIDATE_DEVICE_DETAIL")
public class ReValidateDeviceDetail implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "RE_VALIDATE_DEVICE_DETAIL_SEQ", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "RE_VALIDATE_DEVICE_DETAIL_SEQ", sequenceName = "RE_VALIDATE_DEVICE_DETAIL_SEQ")
	
	@Column(name="DEVICE_VALIDATE_DESC_ID")
	private Long deviceValidateDescId;
	
	@Column (name="DEVICE_VALIDATE_DESC_DATE")
	private Timestamp deviceValidateDescDate;
	
	@Column (name="DEVICE_VALIDATE_DESC_MAC")
	private String deviceValidateDescMac;
	
	@Column (name="COURIER_ID")
	private Long courierId;
	
	@Column (name="DEVICE_ID")
	private Long deviceId;
		
	public ReValidateDeviceDetail(){
		super();
	}

	public void setDeviceValidateDescId(Long deviceValidateDescId) {
		this.deviceValidateDescId = deviceValidateDescId;
	}

	public Long getDeviceValidateDescId() {
		return deviceValidateDescId;
	}

	public void setDeviceValidateDescDate(Timestamp deviceValidateDescDate) {
		this.deviceValidateDescDate = deviceValidateDescDate;
	}

	public Timestamp getDeviceValidateDescDate() {
		return deviceValidateDescDate;
	}

	public void setDeviceValidateDescMac(String deviceValidateDescMac) {
		this.deviceValidateDescMac = deviceValidateDescMac;
	}

	public String getDeviceValidateDescMac() {
		return deviceValidateDescMac;
	}
	
	public void setCourierId(Long courierId) {
		this.courierId = courierId;
	}

	public Long getCourierId() {
		return courierId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	public Long getDeviceId() {
		return deviceId;
	}
}
