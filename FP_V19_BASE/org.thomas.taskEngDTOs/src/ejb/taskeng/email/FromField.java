package ejb.taskeng.email;

import java.io.Serializable;

/**
 * Represents data holded on a from field in a mail header
 * @author Mauricio Gil
 */
public interface FromField extends Serializable {

	/**
	 * Get address field
	 * @return
	 */
	public String getAddress();

	/**
	 * Set address field
	 * @param address
	 */
	public void setAddress(String address);

	/**
	 * Get name field
	 * @return
	 */
	public String getName();

	/**
	 * Set name field
	 * @param name
	 */
	public void setName(String name);

}