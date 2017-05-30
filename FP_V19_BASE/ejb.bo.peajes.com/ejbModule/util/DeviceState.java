package util;

import java.io.Serializable;

public class DeviceState implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long deviceId;
	
	private String DeviceFacialId;
	
	private String state;
	
	public DeviceState(){
		
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceFacialId(String deviceFacialId) {
		DeviceFacialId = deviceFacialId;
	}

	public String getDeviceFacialId() {
		return DeviceFacialId;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getState() {
		return state;
	}
	


}
