package mBeans;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;

import security.UserLogged;
import sessionVar.SessionUtil;

import ejb.PasswordsManagement;
import ejb.SecurityQuestions;

public class UserValidationBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	
	@EJB(mappedName="ejb/PasswordsManagement")
	private PasswordsManagement pM;
	
	@EJB(mappedName="ejb/SecurityQuestions")
	private SecurityQuestions sq;
	
	private String userEmail;
	private String modalMessage;
	
	private boolean showMsj = false;
	private boolean showMsjPass = false;

	private UserLogged currentUser;
	
	public UserValidationBean(){
		currentUser = (UserLogged) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
	}
	
	//Métodos
	public void validate() {
		if (!(userEmail.trim().length()==0)) {
			if (isEmail(userEmail)) {
				Long userId=pM.validateUser(userEmail);
				if (userId==null) {
					this.setModalMessage("El usuario ingresado no existe en el sistema, valide por favor.");
					this.setShowMsj(true);
				}else if(sq.validateQuestionsResponse(userId)==false){
					modalMessage=pM.registrarVerProcesos(userId, SessionUtil.ip());
					this.setShowMsjPass(true);
				}else if (pM.generarOtpRestart(userId, SessionUtil.ip())) {
						this.setModalMessage("Señor usuario se ha enviado al correo registrado en el sistema el código OTP, por favor ingreselo como contraseña.");
						this.setShowMsjPass(true);
				}
			} else {
				this.setModalMessage("Correo inválido. Verifique.");
				this.setShowMsj(true);
			}
		} else {
			this.setModalMessage("No se ha registrado ningún correo electrónico para validar.");
			this.setShowMsj(true);
		}
	}
	//METODO PARA VALIDAR SI ES UN EMAIL-s
	public boolean isEmail(String useString) {
		Pattern pat = null;
		Matcher mat = null;
		pat = Pattern.compile("^[A-Za-z0-9._+-]+@[A-Za-z0-9.-]+\\.[a-zA-Z]{2,4}$");
		mat = pat.matcher(useString);
		return mat.matches();
	}
	
	public void hideModal(){
		this.setShowMsj(false);
		this.setShowMsjPass(false);
	}
	
	public void noRedirect(){
		this.setShowMsj(false);	
	}
	
	public String redirect(){
		this.setShowMsj(false);
		this.setShowMsjPass(false);
		this.setModalMessage("");
		this.setUserEmail("");
		return "successOut";
	}
	
   //Getters and Setters
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail.toLowerCase().trim();
	}

	public String getUserEmail() {
		return userEmail;
	}
	
	public void setShowMsj(boolean showMsj) {
		this.showMsj = showMsj;
	}

	public boolean isShowMsj() {
		return showMsj;
	}
	
	public void setModalMessage(String modalMessage) {
		this.modalMessage = modalMessage;
	}

	public String getModalMessage() {
		return modalMessage;
	}
	
	public void setCurrentUser(UserLogged currentUser) {
		this.currentUser = currentUser;
	}

	public UserLogged getCurrentUser() {
		return currentUser;
	}

	
	public void setShowMsjPass(boolean showMsjPass) {
		this.showMsjPass = showMsjPass;
	}

	public boolean isShowMsjPass() {
		return showMsjPass;
	}
}
