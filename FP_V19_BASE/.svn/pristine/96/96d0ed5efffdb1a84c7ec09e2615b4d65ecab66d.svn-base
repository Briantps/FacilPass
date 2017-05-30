/**
 * 
 */
package mBeans;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;


import constant.ValidatePassword;
import ejb.PasswordsManagement;

import sessionVar.SessionUtil;

import jpa.TbSystemUser;

/**
 * @author tmolina
 *
 */

public class AdminUserAccount implements Serializable{
	private static final long serialVersionUID = 6691382103410194220L;
	
	@EJB(mappedName="ejb/User")
	private ejb.User userEjb;
	
	@EJB(mappedName="ejb/PasswordsManagement")
	private PasswordsManagement pM;
	
	// Properties --------------------------- //
	
	private TbSystemUser user;
	
	private boolean modal;
	
	private boolean modalCS;
	
	private boolean showMsjClose;
	
	private String msgModal;
	
	/* Change Password Attributes */
	private String currentPassword;
	private ValidatePassword validatePassword;

	private String newPassword;
	private String newPasswordC;
	
	private String msjTextError = "";
	
	private String messageConfirm;
	
	private String msgChange;
	
	private String message;
	
	private boolean disabled;

	private String tipoDoc;
	private String UserCode;
	private Long userId;

	// Constructor ----------------------- //

	public AdminUserAccount(){
		super();
		user= new TbSystemUser();	
		currentPassword = "";
	}

@PostConstruct
	public void init(){
		try {
			user = SessionUtil.sessionUser();
			tipoDoc = user.getTbCodeType().getCodeTypeDescription();
			UserCode=user.getUserCode();
			currentPassword = "";
			userId=user.getUserId();
			currentPassword = "";
		} catch (NullPointerException n){
			 // n.printStackTrace();
		}
	}
	

	

	
	public void setMsgChange(String msgChange) {
		this.msgChange = msgChange;
	}

	public String getMsgChange() {
		return msgChange;
	}
	
	
	
	// Getters and Setters ------------------- //
	
	/**
	 * @param user the user to set
	 */
	public void setUser(TbSystemUser user) {
		this.user = user;
	}

	/**
	 * @return the user
	 */
	public TbSystemUser getUser() {
		return user;
	}

	/**
	 * @param modal the modal to set
	 */
	public void setModal(boolean modal) {
		this.modal = modal;
	}

	/**
	 * @return the modal
	 */
	public boolean isModal() {
		return modal;
	}

	/**
	 * @param msgModal the msgModal to set
	 */
	public void setMsgModal(String msgModal) {
		this.msgModal = msgModal;
	}

	/**
	 * @return the msgModal
	 */
	public String getMsgModal() {
		return msgModal;
	}
	
	/**
	 * @param currentPassword the currentPassword to set
	 */
	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	/**
	 * @return the currentPassword
	 */
	public String getCurrentPassword() {
		return currentPassword;
	}

	/**
	 * @param newPassword the newPassword to set
	 */
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	/**
	 * @return the newPassword
	 */
	public String getNewPassword() {
		return newPassword;
	}

	/**
	 * @param confirmNewPassword the confirmNewPassword to set
	 */
	public void setConfirmNewPassword(String confirmNewPassword) {
		this.newPasswordC = confirmNewPassword;
	}

	/**
	 * @return the confirmNewPassword
	 */
	public String getConfirmNewPassword() {
		return newPasswordC;
	}

	/**
	 * @param messageConfirm the messageConfirm to set
	 */
	public void setMessageConfirm(String messageConfirm) {
		this.messageConfirm = messageConfirm;
	}

	/**
	 * @return the messageConfirm
	 */
	public String getMessageConfirm() {
		if(!newPasswordC.equals(newPassword)){
			messageConfirm = " Las contraseñas nueva y de confirmación no son iguales.";
		} else {
			messageConfirm = "";
		}
		return messageConfirm;
	}
	
	/**
	 * Changes the password
	 */
		
	
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

	/**
	 * @param disabled the disabled to set
	 */
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	/**
	 * @return the disabled
	 */
	public boolean isDisabled() {
		return disabled;
	}

	
	// Actions -------------------------- //
	
	public void setTipoDoc(String tipoDoc) {
		this.tipoDoc = tipoDoc;
	}

	public String getTipoDoc() {
		return tipoDoc;
	}

	/**
	 * Changes the password
	 */
	public void chagePasswordReset() {
		try {
			if(SessionUtil.sessionUser().getUserId() != null){
			validatePassword= new ValidatePassword();
			 if(newPassword.trim().length()==0){
	        	message="La nueva contraseña es requerida.";
				setMsgModal(message);
			}
	        else if(newPasswordC.trim().length()==0){
	        	message="La confirmación de la nueva contraseña es requerida.";
				setMsgModal(message);
	        }
	        else if(!newPasswordC.equals(newPassword)){
	        	message = " Las contraseñas nueva y de confirmación no son iguales.";
	        	setMsgModal(message);
			} 
	        else if (validatePassword.validateContainRepeatNumPassword(newPassword.toUpperCase())){
				message="La contraseña no debe tener números o caracteres repetidos de forma consecutiva.";
				setMsgModal(message);
			}
			else if (validatePassword.validateContainSeqNumPassword(newPassword.toUpperCase())){
				message="La contraseña no debe tener una secuencia lógica de números.";
				setMsgModal(message);
			}
			else if (validatePassword.validateContainSeqPassword(newPassword.toUpperCase())){
				message="La contraseña no debe tener una secuencia lógica de letras.";
				setMsgModal(message);
			}
	        else {
				message=userEjb.changePasswordReset(user.getUserId(), newPassword,
								SessionUtil.sessionUser().getUserId(),
								SessionUtil.ip());
				setMsgModal(message);
			}
			setModal(true);
			}
		}catch(NullPointerException ex){
			System.out.println(" [ chagePasswordReset ] "+ex.getMessage());
			setMsgModal("Su sesión ha terminado.");
			this.setShowMsjClose(true);
		}
	}
	
	public void chagePasswordFirst() {
		try {
		validatePassword= new ValidatePassword();
		if((currentPassword.equals(""))||(currentPassword.equals(null))){
			message = "Falta Ingresar la contraseña actual.";
			setMsgModal(message);
			setModal(true);
	    }else
	    	if(userEjb.validatePassword(user.getUserPassword(), currentPassword)){	
				 if(newPassword.trim().length()==0){
		        	message="La nueva contraseña es requerida.";
					setMsgModal(message);
					setModal(true);
				}
		        else if(newPasswordC.trim().length()==0){
		        	message="La confirmación de la nueva contraseña es requerida.";
					setMsgModal(message);
					setModal(true);
		        }
		        else if(!newPasswordC.equals(newPassword)){
		        	message = " Las contraseñas nueva y de confirmación no son iguales.";
		        	setMsgModal(message);
		    		setModal(true);
				}
		        else if (validatePassword.validateContainRepeatNumPassword(newPassword.toUpperCase())){
					message="La contraseña no debe tener números o caracteres repetidos de forma consecutiva.";
					setMsgModal(message);
					setModal(true);
				}
				else if (validatePassword.validateContainSeqNumPassword(newPassword.toUpperCase())){
					message="La contraseña no debe tener una secuencia lógica de números.";
					setMsgModal(message);
					setModal(true);
				}
				else if (validatePassword.validateContainSeqPassword(newPassword.toUpperCase())){
					message="La contraseña no debe tener una secuencia lógica de letras.";
					setMsgModal(message);
					setModal(true);
				}
		        else {
					message=userEjb.changePassword(user.getUserId(), newPassword,
									SessionUtil.sessionUser().getUserId(),
									SessionUtil.ip());
					setMsgModal(message);
					if(message.equals("En este momento su clave se encuentra activa y tiene una vigencia mínima para cambiarla, lo invitamos a ingresar con la clave que registró.")){
						setModalCS(true);
					}else{
						setModal(true);	
					}
		        
				}
	    	} else{
	    		message = "La contraseña ingresada no corresponde a su actual contraseña. Rectifique.";	
	    		setMsgModal(message);
				setModal(true);
	    	}
		
		}catch(NullPointerException ex){
			System.out.println(" [ chagePasswordFirst ] "+ex.getMessage());
			setMsgModal("Error al actualizar contraseña.");
			setModal(true);
		}
	}
	
	
	public String cancelChangePassword(){
		try{
			pM.regVerProcesosRC(user.getUserId(),SessionUtil.ip());
			FacesContext context = FacesContext.getCurrentInstance();
			HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
			if (session != null) {
				pM.regVerProcesosRC(user.getUserId(),SessionUtil.ip());
				session.invalidate();
			}
		} catch (NullPointerException e) {
			setMsgModal("Su sesión ha terminado");
			setModal(true);
		}
		return "successOut";
	}
	
	/**
	 * Control ModalPanel.
	 */
	public void hideModal(){
		this.setMsgModal("");
		this.setModal(false);
		this.setModalCS(false);	
		if(message == null){
			message = msgChange;
		}
		if(msgChange!=null){
			if(msgChange.equals("Contraseña actualizada correctamente.")){
				this.setDisabled(true);
			}
			else{
				this.setDisabled(false);
			}
		}
		
		if(message!=null){
			if(message.equals("Contraseña actualizada correctamente.")){
				this.setDisabled(true);
			}
			else{
				this.setDisabled(false);
			}
		}
		
		
	}
	
	public String logout() {
		try{
			this.setMsgModal("");
			this.setShowMsjClose(false);
			this.setModal(false);
			this.setModalCS(false);
			disabled = false;
				if(SessionUtil.sessionUser().getUserId() != null){
				FacesContext context = FacesContext.getCurrentInstance();
				HttpSession session = (HttpSession) context.getExternalContext()
						.getSession(false);
				if (session != null) {
					session.invalidate();
				}
			}
		} catch (NullPointerException e) {
			e.getMessage();
		}catch (Exception e) {
			e.getMessage();
		}
		return "successOut";
	}

	public String getNewPasswordC() {
		return newPasswordC;
	}

	public void setNewPasswordC(String newPasswordC) {
		this.newPasswordC = newPasswordC;
	}

	public String getMsjTextError() {
		return msjTextError;
	}

	public void setMsjTextError(String msjTextError) {
		this.msjTextError = msjTextError;
	}

	public void setModalCS(boolean modalCS) {
		this.modalCS = modalCS;
	}

	public boolean isModalCS() {
		return modalCS;
	}

	public void setUserCode(String userCode) {
		UserCode = userCode;
	}

	public String getUserCode() {
		return UserCode;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setShowMsjClose(boolean showMsjClose) {
		this.showMsjClose = showMsjClose;
	}

	public boolean isShowMsjClose() {
		return showMsjClose;
	}


	

	
	
}