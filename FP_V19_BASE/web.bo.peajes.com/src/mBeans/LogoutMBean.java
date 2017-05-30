/**
 * 
 */
package mBeans;

import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;


/**
 * @author Cristian Avellaneda
 * 
 */
public class LogoutMBean implements Serializable {
	private static final long serialVersionUID = 1172542755438336854L;

	/**
	 * Constructor
	 */
	public LogoutMBean() {
	}

	/**
	 * Invalidates the current user session.
	 */
	public String logout() {	
		FacesContext context = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) context.getExternalContext()
				.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		return "successOut";
	}
}