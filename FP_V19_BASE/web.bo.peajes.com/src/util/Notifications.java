package util;

import java.io.Serializable;

import jpa.ReUserEmailProcess;

/**
 * 
 * @author Ximena Blasquez
 * @version 1
 *
 */
public class Notifications implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1499820085456719303L;
	
	private ReUserEmailProcess userEmail;
	
	private boolean active;
	
	public Notifications(ReUserEmailProcess userEmail, boolean active) {
		this.userEmail = userEmail;
		this.active = active;
	}
	
	
	public void setUserEmail(ReUserEmailProcess userEmail) {
		this.userEmail = userEmail;
	}

	public ReUserEmailProcess getUserEmail() {
		return userEmail;
	}

	public void setActive(boolean active) {		
		this.active = active;
	}

	public boolean isActive() {
		return active;
	}

}
