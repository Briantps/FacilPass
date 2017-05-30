/**
 * 
 */
package jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Persistence class for table TB_DEVICE_FACIAL
 * @author tmolina
 */
@Entity
@Table(name="TB_DEVICE_FACIAL")
public class TbDeviceFacial implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="DEVICE_FACIAL_ID")
	private Long deviceFacialId;
	
	@ManyToOne
	@JoinColumn(name="DEVICE_TYPE_ID")
	private TbDeviceType tbDeviceType;
	
	@ManyToOne
	@JoinColumn(name="COMPANY_ID")
	private TbCompany tbCompany;
	
	@Column(name="DEVICE_FACIAL_PREFIX")
	private String deviceFacialPrefix;
	
	@Column(name="FACIAL_CONSECUTIVE")
	private Long facialConsecutive;
	
	@Column(name="FACIAL_LENGTH")
	private int facialLenght;
	
	/**
	 * Constructor
	 */
	public TbDeviceFacial(){
		super();
	}

	/**
	 * @param deviceFacialId the deviceFacialId to set
	 */
	public void setDeviceFacialId(Long deviceFacialId) {
		this.deviceFacialId = deviceFacialId;
	}

	/**
	 * @return the deviceFacialId
	 */
	public Long getDeviceFacialId() {
		return deviceFacialId;
	}

	/**
	 * @param tbDeviceType the tbDeviceType to set
	 */
	public void setTbDeviceType(TbDeviceType tbDeviceType) {
		this.tbDeviceType = tbDeviceType;
	}

	/**
	 * @return the tbDeviceType
	 */
	public TbDeviceType getTbDeviceType() {
		return tbDeviceType;
	}

	/**
	 * @param tbCompany the tbCompany to set
	 */
	public void setTbCompany(TbCompany tbCompany) {
		this.tbCompany = tbCompany;
	}

	/**
	 * @return the tbCompany
	 */
	public TbCompany getTbCompany() {
		return tbCompany;
	}

	/**
	 * @param deviceFacialPrefix the deviceFacialPrefix to set
	 */
	public void setDeviceFacialPrefix(String deviceFacialPrefix) {
		this.deviceFacialPrefix = deviceFacialPrefix;
	}

	/**
	 * @return the deviceFacialPrefix
	 */
	public String getDeviceFacialPrefix() {
		return deviceFacialPrefix;
	}

	/**
	 * @param facialConsecutive the facialConsecutive to set
	 */
	public void setFacialConsecutive(Long facialConsecutive) {
		this.facialConsecutive = facialConsecutive;
	}

	/**
	 * @return the facialConsecutive
	 */
	public Long getFacialConsecutive() {
		return facialConsecutive;
	}

	/**
	 * @param facialLenght the facialLenght to set
	 */
	public void setFacialLenght(int facialLenght) {
		this.facialLenght = facialLenght;
	}

	/**
	 * @return the facialLenght
	 */
	public int getFacialLenght() {
		return facialLenght;
	}
}