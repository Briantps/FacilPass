package ejb.taskeng.email;

import java.io.Serializable;
import java.util.Map;

/**
 * Represents a collections of email discriminated by type
 * @author Mauricio Gil
 */
public interface EmailTypes extends Serializable {

	/**
	 * Set map of email definitions
	 * @param emails
	 */
	public void setEmails(Map<String, EmailDef> emails);

	/**
	 * Get map of email definitions
	 * @return
	 */
	public Map<String, EmailDef> getEmails();

	/**
	 * Adds a new email definition
	 * @param e
	 */
	public void addEmail(EmailDef e);

	/**
	 * Gets an email definition.
	 * @param type Type of email definition. Works like an id.
	 * @return
	 */
	public EmailDef getEmail(String type);

}