/**
 * 
 */
package jpa;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Persistence class for table TB_DEVICE_TYPE
 * @author tmolina
 */
@Entity
@Table(name="TB_DEVICE_TYPE")
public class TbDeviceType implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="DEVICE_TYPE_ID")
	private Long deviceTypeId;
	
	@Column(name="DEVICE_TYPE_NAME")
	private String deviceTypeName;	
	
	
	//bi-directional one-to-many association to TbDevice
	@OneToMany(mappedBy="tbDeviceType")
	private List<TbDevice> tbDevices;
	
	/**
	 * Constructor
	 */
	public TbDeviceType(){
		super();
	}

	/**
	 * @param deviceTypeId the deviceTypeId to set
	 */
	public void setDeviceTypeId(Long deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}

	/**
	 * @return the deviceTypeId
	 */
	public Long getDeviceTypeId() {
		return deviceTypeId;
	}

	/**
	 * @param deviceTypeName the deviceTypeName to set
	 */
	public void setDeviceTypeName(String deviceTypeName) {
		this.deviceTypeName = deviceTypeName;
	}

	/**
	 * @return the deviceTypeName
	 */
	public String getDeviceTypeName() {
		return deviceTypeName;
	}

	/**
	 * @param tbDevices the tbDevices to set
	 */
	public void setTbDevices(List<TbDevice> tbDevices) {
		this.tbDevices = tbDevices;
	}

	/**
	 * @return the tbDevices
	 */
	public List<TbDevice> getTbDevices() {
		return tbDevices;
	}
	
}