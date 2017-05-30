package ejb.taskeng.email;

import java.io.Serializable;

/**
 * Define la estructura de un correo electronico.   
 * @author Mauricio Gil
 */
public interface EmailDef extends Serializable {

	/**
	 * Set header data
	 * @param headers
	 */
	public void setHeader(Header header);

	/**
	 * Set header data
	 * @return
	 */
	public Header getHeader();

	/**
	 * Set type of email. Type of email is like an identification.
	 * @param type
	 */
	public void setType(String type);

	/**
	 * Get type of email. Type of email is like an identification.
	 * @return
	 */
	public String getType();

	/**
	 * Set body data of this email definition.
	 * @param body
	 */
	public void setBody(Body body);

	/**
	 * Get body data of this email definition.
	 * @return
	 */
	public Body getBody();

}