/**
 * 
 */
package mBeans;

import java.util.ArrayList;
import java.util.List;

import util.UserLogged;

/**
 * @author Cristian Avellaneda
 * 
 */
@SuppressWarnings("deprecation")
public class AplicationVar {

	private List<UserLogged> listUserLog;

	// Constructor ---------------------------------------------------------------------------------------------------------------
	
	public AplicationVar() {
		listUserLog = new ArrayList<UserLogged>();
	}

	/**
	 * Setter for listUserLog
	 * @param listUserLog
	 */
	public void setListUserLog(List<UserLogged> listUserLog) {
		this.listUserLog = listUserLog;
	}

	/**
	 * 
	 * @return listUserLog
	 */
	public List<UserLogged> getListUserLog() {
		return listUserLog;
	}
}