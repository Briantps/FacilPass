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
 * The persistent class for the RE_DEVICE_TAG_TYPE database table.
 * @author tmolina
 *
 */
@Entity
@Table(name="RE_DEVICE_TAG_TYPE")
public class ReDeviceTagType implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="REDEVICETAGTYPE_DEVICETAGTYPEID_GENERATOR",sequenceName="RE_DEVICE_TAG_TYPE_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="REDEVICETAGTYPE_DEVICETAGTYPEID_GENERATOR")
	@Column(name="DEVICE_TAG_TYPE_ID")
	private Long deviceTagTypeId;
	
	@ManyToOne
	@JoinColumn(name="DEVICE_ID")
	private TbDevice tbDevice;
	
	@ManyToOne
	@JoinColumn(name="TAG_TYPE_ID")
	private TbTagType tbTagType;
	
	/**
	 * Constructor.
	 */
	public ReDeviceTagType() {
		super();
	}

	/**
	 * @param deviceTagTypeId the deviceTagTypeId to set
	 */
	public void setDeviceTagTypeId(Long deviceTagTypeId) {
		this.deviceTagTypeId = deviceTagTypeId;
	}

	/**
	 * @return the deviceTagTypeId
	 */
	public Long getDeviceTagTypeId() {
		return deviceTagTypeId;
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
	 * @param tbTagType the tbTagType to set
	 */
	public void setTbTagType(TbTagType tbTagType) {
		this.tbTagType = tbTagType;
	}

	/**
	 * @return the tbTagType
	 */
	public TbTagType getTbTagType() {
		return tbTagType;
	}
}