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
 * @author tmolina
 *
 */
@Entity
@Table(name="RE_STATION_DEVICE")
public class ReStationDevice implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RE_STATION_DEVICE_STATIONDEVICEID_GENERATOR")
	@SequenceGenerator(name = "RE_STATION_DEVICE_STATIONDEVICEID_GENERATOR", sequenceName = "RE_STATION_DEVICE_SEQ",allocationSize=1)
	@Column(name="STATION_DEVICE_ID")
	private Long stationDeviceId;
	
	@ManyToOne
	@JoinColumn(name="STATION_ID")
	private TbStationBO tbStation;
	
	@ManyToOne
	@JoinColumn(name="DEVICE_ID")
	private TbDevice tbDevice;
	
	/**
	 * Constructor
	 */
	public ReStationDevice(){
		super();
	}

	/**
	 * @param stationDeviceId the stationDeviceId to set
	 */
	public void setStationDeviceId(Long stationDeviceId) {
		this.stationDeviceId = stationDeviceId;
	}

	/**
	 * @return the stationDeviceId
	 */
	public Long getStationDeviceId() {
		return stationDeviceId;
	}

	/**
	 * @param tbStation the tbStation to set
	 */
	public void setTbStation(TbStationBO tbStation) {
		this.tbStation = tbStation;
	}

	/**
	 * @return the tbStation
	 */
	public TbStationBO getTbStation() {
		return tbStation;
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
}