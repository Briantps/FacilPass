/**
 * 
 */
package sessionVar;

import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import mBeans.LoginMBean;

import jpa.TbSystemUser;

/**
 * @author tmolina
 *
 */
public final class SessionUtil implements Serializable{
	private static final long serialVersionUID = 6668207898147859583L;

	// Constructor ----------------------------------------------------------------------------
	
	private SessionUtil(){
	}

	// Getters --------------------------------------------------------------------------------

	/**
	 * @return the ip.
	 */
	public static String ip(){
		return ((HttpServletRequest) FacesContext.getCurrentInstance()
				.getExternalContext().getRequest()).getRemoteAddr().toString();	
	}
	
	/**
	 * @return the sessionUser
	 */
	public static TbSystemUser sessionUser() {
		try{
			LoginMBean login = (LoginMBean) FacesContext.getCurrentInstance()
			.getExternalContext().getSessionMap().get("loginMBean");
	
			if(login.isLogged()){
				return login.getSystemUser();
			}	
			
		}
		catch (NullPointerException e) {
			System.out.println(" [ Error en SessionUtil.getSessionUser ] "+e.getLocalizedMessage());
		}catch (Exception e) {
			System.out.println(" [ Error en SessionUtil.getSessionUser ] ");
			e.printStackTrace();
		}
		return null;
	}
}