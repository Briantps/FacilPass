package jpa;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the TB_DEVICE_STEP database table.
 * 
 */
@Entity
@Table(name="TB_DEVICE_STEP")
public class TbDeviceStep implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="TBDEVICESTEP_GENERATOR",sequenceName="TB_DEVICE_STEP_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBDEVICESTEP_GENERATOR")
	@Column(name="DEVICE_STEP_ID")
	private Long deviceStepId;
	
	@ManyToOne
	@JoinColumn(name="DEVICE_ID")
	private TbDevice tbDevice;

	@ManyToOne
	@JoinColumn(name="STATION_BO_ID")
	private TbStationBO tbStation;
	
	@Column(name="DEVICE_STEP_BALANCE")
	private Long deviceStepBalance;

	public void setDeviceStepId(Long deviceStepId) {
		this.deviceStepId = deviceStepId;
	}

	public Long getDeviceStepId() {
		return deviceStepId;
	}

	public void setTbDevice(TbDevice tbDevice) {
		this.tbDevice = tbDevice;
	}

	public TbDevice getTbDevice() {
		return tbDevice;
	}

	public void setTbStation(TbStationBO tbStation) {
		this.tbStation = tbStation;
	}

	public TbStationBO getTbStation() {
		return tbStation;
	}

	public void setDeviceStepBalance(Long deviceStepBalance) {
		this.deviceStepBalance = deviceStepBalance;
	}

	public Long getDeviceStepBalance() {
		return deviceStepBalance;
	}
}
