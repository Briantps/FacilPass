/**
 * 
 */
package mBeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import util.GetApplicationBean;
import util.UserLogged;

/**
 * @author Cristian Avellaneda
 * 
 */
@Deprecated
public class ValSessionMBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5296564701145554310L;
	/**
	 * 
	 */

	private boolean rediret;

	public ValSessionMBean() {
		rediret = false;
	}

	public String validarSession() {
		System.out.println("entro a la validacion");
		FacesContext context = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) context.getExternalContext()
				.getSession(false);
		LoginMBean loginMBean = (LoginMBean) session.getAttribute("loginMBean");
		if (loginMBean.isLogged() == false) {
			rediret = true;
		} else {
			AplicationVar aplicationVar = (AplicationVar) new GetApplicationBean()
					.getBeanAplication("aplicationVar");
			List<UserLogged> lista = new ArrayList<UserLogged>(aplicationVar
					.getListUserLog());
			for (UserLogged userLogged : lista) {
				if((userLogged.getUserId() == loginMBean.getSystemUser().getUserId()) && userLogged.getSessionId().equals(loginMBean.getSessionId())){
					session.invalidate();
					rediret = true;
					break;
				}
			}
		}
		String var = rediret == true? "fail":"success"; 
		return var;
	}

	public void setRediret(boolean rediret) {
		this.rediret = rediret;
	}

	public boolean isRediret() {
		return rediret;
	}

}