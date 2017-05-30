/**
 * 
 */
package util;

import java.io.Serializable;

import jpa.TbSystemUser;

/**
 * @author tmolina
 *
 */
public class LoginUser implements Serializable{
	private static final long serialVersionUID = 4489070196070592992L;
	
	// Attributes
	
	private TbSystemUser loginUser;
	
	private String message;
	
	/**
	 * Constructor
	 */
	public LoginUser(){
	}

	/**
	 * @param loginUser the loginUser to set
	 */
	public void setLoginUser(TbSystemUser loginUser) {
		this.loginUser = loginUser;
	}

	/**
	 * @return the loginUser
	 */
	public TbSystemUser getLoginUser() {
		return loginUser;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
}