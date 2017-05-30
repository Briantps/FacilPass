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
 * The persistent class for the TB_DEVICE_STATE database table.
 * @author tmolina
 */
@Entity
@Table(name="TB_DEVICE_STATE")
public class TbDeviceState implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TBDEVICESTATE_DEVICESTATEID_GENERATOR")
	@SequenceGenerator(name = "TBDEVICESTATE_DEVICESTATEID_GENERATOR", sequenceName = "TB_DEVICE_STATE_SEQ")
	@Column(name = "DEVICE_STATE_ID")
	private Long deviceStateId;
	
	@Column(name="DEVICE_STATE_DESCRIPTION")
	private String deviceStateDescription;

	/**
	 * Constructor. 
	 */
	public TbDeviceState() {
		super();
	}

	/**
	 * @param deviceStateId the deviceStateId to set
	 */
	public void setDeviceStateId(Long deviceStateId) {
		this.deviceStateId = deviceStateId;
	}

	/**
	 * @return the deviceStateId
	 */
	public Long getDeviceStateId() {
		return deviceStateId;
	}

	/**
	 * @param deviceStateDescription the deviceStateDescription to set
	 */
	public void setDeviceStateDescription(String deviceStateDescription) {
		this.deviceStateDescription = deviceStateDescription;
	}

	/**
	 * @return the deviceStateDescription
	 */
	public String getDeviceStateDescription() {
		return deviceStateDescription;
	}
}