package ejb.taskeng.email;

import java.io.Serializable;

/**
 * Representation of a source used to retrieve email definitions.
 * 
 * @author Mauricio Gil
 */
public interface EmailFactory extends Serializable {

	/**
	 * Set email collection 
	 * @param emailTypes
	 */
	public void setEmailTypes(EmailTypes emailTypes);

	/**
	 * Get email collection
	 * @return
	 */
	public EmailTypes getEmailTypes();

	/**
	 * Retrieves an email definition from a specified type
	 * @param type Email type. An email type is like an id.
	 * @return
	 */
	public EmailDef getEmail(String type);

}