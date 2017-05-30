/**
 * 
 */
package util;

import java.io.Serializable;

/**
 * @author FZucchet
 * 
 */
@Deprecated
public class UserLogged implements Serializable {
	private static final long serialVersionUID = -3896510521529736462L;
	
	private long userId;
	private String sessionId;

	/**
	 * Default constructor
	 */
	public UserLogged(long userId, String sessionId) {
		this.setUserId(userId);
		this.setSessionId(sessionId);
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getUserId() {
		return userId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getSessionId() {
		return sessionId;
	}
}