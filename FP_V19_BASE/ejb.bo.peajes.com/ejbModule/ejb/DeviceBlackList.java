package ejb;

import javax.ejb.Remote;

@Remote
public interface DeviceBlackList {
	
	public boolean enterDeviceBL(String deviceCode, String ip, Long user);
	
}
