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
 * The persistent class for the TB_SPECIAL_STATION database table.
 * @author tmolina
 */
 @Entity
 @Table(name="TB_SPECIAL_STATION")
public class TbSpecialStation implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TB_SPECIAL_STATION_SPECIALSTATIONID_GENERATOR")
	@SequenceGenerator(name="TB_SPECIAL_STATION_SPECIALSTATIONID_GENERATOR",  sequenceName = "TB_SPECIAL_STATION_SEQ",allocationSize=1)
	@Column(name="SPECIAL_STATION_ID")
	private Long specialStationId;
	
	@ManyToOne
	@JoinColumn(name="DEVICE_CUSTOMIZATION_ID")
	private TbDeviceCustomization tbDeviceCustomization;
	
	@ManyToOne
	@JoinColumn(name="STATION_BO_ID")
	private TbStationBO tbStationBO;
	
	/**
	 * Constructor.
	 */
	public TbSpecialStation(){
		super();
	}

	/**
	 * @param specialStationId the specialStationId to set
	 */
	public void setSpecialStationId(Long specialStationId) {
		this.specialStationId = specialStationId;
	}

	/**
	 * @return the specialStationId
	 */
	public Long getSpecialStationId() {
		return specialStationId;
	}

	/**
	 * @param tbDeviceCustomization the tbDeviceCustomization to set
	 */
	public void setTbDeviceCustomization(TbDeviceCustomization tbDeviceCustomization) {
		this.tbDeviceCustomization = tbDeviceCustomization;
	}

	/**
	 * @return the tbDeviceCustomization
	 */
	public TbDeviceCustomization getTbDeviceCustomization() {
		return tbDeviceCustomization;
	}

	/**
	 * @param tbStationBO the tbStationBO to set
	 */
	public void setTbStationBO(TbStationBO tbStationBO) {
		this.tbStationBO = tbStationBO;
	}

	/**
	 * @return the tbStationBO
	 */
	public TbStationBO getTbStationBO() {
		return tbStationBO;
	}
}