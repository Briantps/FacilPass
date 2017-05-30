package ejb.crud;
import java.util.List;

import javax.ejb.Remote;

import jpa.TbInstallationLocation;

@Remote
public interface InstallationLocation {
	
	/**
	 * 
	 * @return {@link TbInstallationLocation} {@link List}
	 */
	public List<TbInstallationLocation> getInstallationLocations();
	
	/**
	 * 
	 * @param location
	 * @param ip
	 * @param creationUser
	 * @return {@link Long} -1 if the location exits, null if error, 0 if successful.
	 */
	public Long insertLocation(String location, String ip, Long creationUser);
	
	/**
	 * @param locationId
	 * @param location
	 * @param ip
	 * @param creationUser
	 * @return <code>true</code> if successful, <code>false</code> otherwise.
	 */
	public boolean editLocation(Long locationId, String location, String ip, Long creationUser);
}
