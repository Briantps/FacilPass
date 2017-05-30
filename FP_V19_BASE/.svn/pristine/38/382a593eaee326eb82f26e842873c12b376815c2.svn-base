package mBeans;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import security.UserLogged;
import sessionVar.SessionUtil;
import validator.Validation;

import ejb.Login;
import ejb.PasswordsManagement;
import ejb.SecurityQuestions;
import ejb.SystemParameters;
import ejb.User;

public class PasswordResetBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@EJB(mappedName="ejb/PasswordsManagement")
	private PasswordsManagement pM;
	
	@EJB(mappedName="ejb/SecurityQuestions")
	private SecurityQuestions securityQuestionsEJB;
	
	@EJB(mappedName="ejb/SystemParameters")
	private SystemParameters SystemParametersEJB;
	
	@EJB(mappedName = "ejb/Login")
	private Login loginEJB;
	
	@EJB(mappedName = "ejb/User")
	private User userEJB;

	
	private String preg1="";
	private String preg2="";
	private String preg3="";
	private String preg4="";
	private String preg5="";
	private String resp1="";
	private String resp2="";
	private String resp3="";
	private String resp4="";
	private String resp5="";
	private String modalMessage;
		
	private int count;
	
	private boolean showMsj;
	private boolean showMsjClose;
	private boolean showModalValidate;
	
	private UserLogged usrs;
	
	public  PasswordResetBean(){
		usrs = (UserLogged) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
	}
	
	@PostConstruct
	public void init(){
		this.getPreg1();
		this.getPreg2();
		this.getPreg3();
		this.getPreg4();
		this.getPreg5();
		this.hideModal();
	}

	/**Método encargado de validar las respuestas de seguridad al restablecer contraseña
	 * @return return message
	 * @author psanchez
	 */	
	public String resetValidateResponses(){
		try{
			if(validateResponse()){
				count++;
				int validateAnswer = securityQuestionsEJB.validateAnswer(usrs.getUserId(), resp1, resp2, resp3, resp4);
				boolean validateAnswerIntent=securityQuestionsEJB.validateAnswerIntent(usrs.getUserId(),count, SessionUtil.ip());	//46-usuario sobrepasé el número de intentos fallidos para las respuestas de las preguntas de seguridad 
				boolean validateAnswerFive = securityQuestionsEJB.validateAnswerFive(usrs.getUserId(), resp5);
				String minimumValidResponses = SystemParametersEJB.getParameterValueById(51L);//51 Mínimo preguntas validas de seguridad 
				
				if(validateAnswerIntent){
					this.setModalMessage("Ha sobrepasado el límite de intentos, nuestros asesores se comunicarán con usted para atender su solicitud.");
					this.setShowMsjClose(true);	
				}else if(Integer.parseInt(minimumValidResponses)>validateAnswer || validateAnswerFive==false){
					securityQuestionsEJB.validateResponseReset(usrs.getUserId(), SessionUtil.ip());
					this.setModalMessage("Señor usuario, algunas de sus respuestas no coinciden con las del sistema, por favor inténtelo nuevamente.");
					this.setShowModalValidate(true);	
				}else{
					//si las respuestas de seguridad son correctas redireciona al cambio de contraseña
					count=0;
					securityQuestionsEJB.userCountQuestions(usrs.getUserId());
					return "successFL";
				}
			}
		} catch(NullPointerException ex){
			System.out.println(" [ Error en PasswordResetBean.resetValidateResponses.NullPointerException ] "+ex.getMessage());
			this.setModalMessage("Se ha Presentado un Error en el Sistema. Por favor Intente mas tarde. ");
			this.setShowMsjClose(true);
		} catch (Exception e) {
			System.out.println(" [ Error en PasswordResetBean.resetValidateResponses.Exception ] " +e.getMessage());
			this.setModalMessage("Se ha Presentado un Error en el Sistema. Por favor Intente mas tarde. ");
			this.setShowMsjClose(true);
		}
		return modalMessage;
	}
		
	/**
	 * Función que elimina acentos y caracteres especiales de
	 * una cadena de texto.
	 * @param input
	 * @return cadena de texto limpia de acentos y caracteres especiales.
	 */
	public String reemplazarCaracteresRaros(String input) {
	    // Cadena de caracteres original a sustituir.
	    String original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ";
	    // Cadena de caracteres ASCII que reemplazarán los originales.
	    String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
	    String output = input;
	    for (int i=0; i<original.length(); i++) {
	        // Reemplazamos los caracteres especiales.
	        output = output.replace(original.charAt(i), ascii.charAt(i));
	    }//for i
	    return output;
	}//reemplazarCaracteresRaros

	
	/**Método encargado de validar las respuestas de seguridad 
	 * @author psanchez
	 */	
	 public boolean validateResponse(){
		try{
			if(usrs != null){
				if(resp1.trim().equals("") || resp1.trim().equals(null)){
					this.setModalMessage("Debe responder la pregunta 1. ");
					this.setShowModalValidate(true);
					return false;
				}else if(resp1.trim().length() < 5 ){
					this.setModalMessage("La respuesta 1 debe ser mínima de 5 caracteres.");
					this.setShowModalValidate(true);
					return false;
				}else if(resp1.trim().length() > 250 ){
					this.setModalMessage("La respuesta 1 debe ser máxima de 250 caracteres.");
					this.setShowModalValidate(true);
					return false;
				}else if(validateResponses(usrs.getUserId(), resp1.trim(), 1).length()>0){
					String Message=validateResponses(usrs.getUserId(), resp1.trim(), 1);
					this.setModalMessage(Message);
					this.setShowModalValidate(true);
					return false;
				}else if (!Validation.validationQuestionCharacters(resp1.trim())) {
					this.setModalMessage("La respuesta de la pregunta 1 contiene caracteres inválidos.");
					this.setShowModalValidate(true);
					return false;
				}
				else if(resp2.trim().equals("") || resp2.equals(null)){
					this.setModalMessage("Debe responder la pregunta 2. ");
					this.setShowModalValidate(true);
					return false;
				}else if(resp2.trim().length() < 5 ){
					this.setModalMessage("La respuesta 2 debe ser mínima de 5 caracteres.");
					this.setShowModalValidate(true);
					return false;
				}else if(resp2.trim().length() > 250 ){
					this.setModalMessage("La respuesta 2 debe ser máxima de 250 caracteres.");
					this.setShowModalValidate(true);
					return false;
				}else if(validateResponses(usrs.getUserId(), resp2.trim(), 2).length()>0){
					String Message=validateResponses(usrs.getUserId(), resp2, 2);
					this.setModalMessage(Message);
					this.setShowModalValidate(true);
					return false;
				}else if (!Validation.validationQuestionCharacters(resp2.trim())) {
					this.setModalMessage("La respuesta de la pregunta 2 contiene caracteres inválidos.");
					this.setShowModalValidate(true);
					return false;
				}
				else if(resp3.trim().trim().equals("") || resp3.trim().equals(null)){
					this.setModalMessage("Debe responder la pregunta 3. ");
					this.setShowModalValidate(true);
					return false;
				}else if(resp3.trim().length() < 5 ){
					this.setModalMessage("La respuesta 3 debe ser mínima de 5 caracteres.");
					this.setShowModalValidate(true);
					return false;
				}else if(resp3.trim().length() > 250 ){
					this.setModalMessage("La respuesta 3 debe ser máxima de 250 caracteres.");
					this.setShowModalValidate(true);
					return false;
				}else if(validateResponses(usrs.getUserId(), resp3.trim(), 3).length()>0){
					String Message=validateResponses(usrs.getUserId(), resp3, 3);
					this.setModalMessage(Message);
					this.setShowModalValidate(true);
					return false;
				}else if (!Validation.validationQuestionCharacters(resp3.trim())) {
					this.setModalMessage("La respuesta de la pregunta 3 contiene caracteres inválidos.");
					this.setShowModalValidate(true);
					return false;
				}
				else if(resp4.trim().equals("") || resp4.equals(null)){
					this.setModalMessage("Debe responder la pregunta 4. ");
					this.setShowModalValidate(true);
					return false;
				}else if(resp4.trim().length() < 5 ){
					this.setModalMessage("La respuesta 4 debe ser mínima de 5 caracteres.");
					this.setShowModalValidate(true);
					return false;
				}else if(resp4.trim().length() > 250 ){
					this.setModalMessage("La respuesta 4 debe ser máxima de 250 caracteres.");
					this.setShowModalValidate(true);
					return false;
				}else if(validateResponses(usrs.getUserId(), resp4.trim(), 4).length()>0){
					String Message=validateResponses(usrs.getUserId(), resp4.trim(), 4);
					this.setModalMessage(Message);
					this.setShowModalValidate(true);
					return false;
				}else if (!Validation.validationQuestionCharacters(resp4.trim())) {
					this.setModalMessage("La respuesta de la pregunta 4 contiene caracteres inválidos.");
					this.setShowModalValidate(true);
					return false;
				}
				 else if(resp5.trim().equals("") || resp5.trim().equals(null)){
					this.setModalMessage("Debe responder la pregunta 5. ");
					this.setShowModalValidate(true);
					return false;
				}else if(resp5.trim().length() < 5 ){
					this.setModalMessage("La respuesta 5 debe ser mínima de 5 caracteres.");
					this.setShowModalValidate(true);
					return false;
				}else if(resp5.trim().length() > 90 ){
					this.setModalMessage("La respuesta 5 debe ser máxima de 90 caracteres.");
					this.setShowModalValidate(true);
					return false;
				}else if(validateResponses(usrs.getUserId(), resp5.trim(), 5).length()>0){
					String Message=validateResponses(usrs.getUserId(), resp5.trim(), 5);
					this.setModalMessage(Message);
					this.setShowModalValidate(true);
					return false;
				}else if (!Validation.validationQuestionCharacters(resp5.trim())) {
					this.setModalMessage("La respuesta de la pregunta 5 contiene caracteres inválidos.");
					this.setShowModalValidate(true);
					return false;
				}
				else if(resp1.trim().equals(resp2.trim())){
					this.setModalMessage("La respuesta a la pregunta 1, no debe ser igual a la respuesta de la pregunta 2. ");
					this.setShowModalValidate(true);
					return false;
				}else if(resp1.trim().equals(resp3.trim())){ 
					this.setModalMessage("La respuesta a la pregunta 1, no debe ser igual a la respuesta de la pregunta 3. ");
					this.setShowModalValidate(true);
					return false;
				}else if(resp1.trim().equals(resp4.trim())){
					this.setModalMessage("La respuesta a la pregunta 1, no debe ser igual a la respuesta de la pregunta 4. ");
					this.setShowModalValidate(true);
					return false;
				}else if(resp1.trim().equals(resp5.trim())){
					this.setModalMessage("La respuesta a la pregunta 1, no debe ser igual a la respuesta de la pregunta 5. ");
					this.setShowModalValidate(true);
					return false;
				}
				else if(resp2.trim().equals(resp1.trim())){
					this.setModalMessage("La respuesta a la pregunta 2, no debe ser igual a la respuesta de la pregunta 1. ");
					this.setShowModalValidate(true);
					return false;
				}else if(resp2.trim().equals(resp3.trim())){
					this.setModalMessage("La respuesta a la pregunta 2, no debe ser igual a la respuesta de la pregunta 3. ");
					this.setShowModalValidate(true);
					return false;
				}else if(resp2.trim().equals(resp4.trim())){
					this.setModalMessage("La respuesta a la pregunta 2, no debe ser igual a la respuesta de la pregunta 4. ");
					this.setShowModalValidate(true);
					return false;
				}else if(resp2.trim().equals(resp5.trim())){
					this.setModalMessage("La respuesta a la pregunta 2, no debe ser igual a la respuesta de la pregunta 5. ");
					this.setShowModalValidate(true);
					return false;
				}
				else if(resp3.trim().equals(resp1.trim())){
					this.setModalMessage("La respuesta a la pregunta 3, no debe ser igual a la respuesta de la pregunta 1. ");
					this.setShowModalValidate(true);
					return false;
				}else if(resp3.trim().equals(resp2.trim())){
					this.setModalMessage("La respuesta a la pregunta 3, no debe ser igual a la respuesta de la pregunta 2. ");
					this.setShowModalValidate(true);
					return false;
				}else if(resp3.trim().equals(resp4.trim())){
					this.setModalMessage("La respuesta a la pregunta 3, no debe ser igual a la respuesta de la pregunta 4. ");
					this.setShowModalValidate(true);
					return false;
				}else if(resp3.trim().equals(resp5.trim())){
					this.setModalMessage("La respuesta a la pregunta 3, no debe ser igual a la respuesta de la pregunta 5. ");
					this.setShowModalValidate(true);
					return false;
				}
				else if(resp4.trim().equals(resp1.trim())){
					this.setModalMessage("La respuesta a la pregunta 4, no debe ser igual a la respuesta de la pregunta 1. ");
					this.setShowModalValidate(true);
					return false;
				}else if(resp4.trim().equals(resp2.trim())){
					this.setModalMessage("La respuesta a la pregunta 4, no debe ser igual a la respuesta de la pregunta 2. ");
					this.setShowModalValidate(true);
					return false;
				}else if(resp4.trim().equals(resp3.trim())){
					this.setModalMessage("La respuesta a la pregunta 4, no debe ser igual a la respuesta de la pregunta 3. ");
					this.setShowModalValidate(true);
					return false;
				}else if(resp4.trim().equals(resp5.trim())){
					this.setModalMessage("La respuesta a la pregunta 4, no debe ser igual a la respuesta de la pregunta 5. ");
					this.setShowModalValidate(true);
					return false;
				}
				else if(resp5.trim().equals(resp1.trim())){
					this.setModalMessage("La respuesta a la pregunta 5, no debe ser igual a la respuesta de la pregunta 1. ");
					this.setShowModalValidate(true);
					return false;
				}else if(resp5.trim().equals(resp2.trim())){
					this.setModalMessage("La respuesta a la pregunta 5, no debe ser igual a la respuesta de la pregunta 2. ");
					this.setShowModalValidate(true);
					return false;
				}else if(resp5.trim().equals(resp3.trim())){
					this.setModalMessage("La respuesta a la pregunta 5, no debe ser igual a la respuesta de la pregunta 3. ");
					this.setShowModalValidate(true);
					return false;
				}else if(resp5.trim().equals(resp4.trim())){
					this.setModalMessage("La respuesta a la pregunta 5, no debe ser igual a la respuesta de la pregunta 4. ");
					this.setShowModalValidate(true);
					return false;
				}
			}
		} catch(NullPointerException ex){
			System.out.println(" [ Error en PasswordResetBean.validateResponse.NullPointerException ] "+ex.getMessage());
			this.setModalMessage("Su Sesión ha Terminado.");
			this.setShowMsjClose(true);
			return false;
		} catch (Exception e) {
			System.out.println(" [ Error en PasswordResetBean.validateResponse.Exception ] "+e.getMessage());
			this.setModalMessage("Su Sesión ha Terminado.");
			this.setShowMsjClose(true);
			return false;
		}
		return true;
	 }

	/** Método creado para validar las respuestas de seguridad ingresadas por el cliente.
	 * @return return message
	 * @author psanchez
	 */
	public String validateResponses(Long userId, String newPassword, int questionId) {	
		String message = "";	
		try {
			String validateIdentificationResponse = SystemParametersEJB.getParameterValueById(17L);
			String validateSequenceResponse = SystemParametersEJB.getParameterValueById(19L);

			if(validateIdentificationResponse.equals("NO") && userEJB.containIdentification(newPassword, userId)){
				message="La respuesta de la pregunta "+ questionId +" no puede contener su número de identificación.";
			}
			else if(validateSequenceResponse.equals("NO")) {
				message=userEJB.logicalSequences(newPassword,questionId);
			}
		} catch(NullPointerException ex){
			System.out.println(" [ Error en PasswordResetBean.validateResponses.NullPointerException ] ");
			message="Su Sesión ha Terminado.";
		} catch (Exception e) {
			System.out.println(" [ Error en PasswordResetBean.validateResponses.Exception ] ");
			message="Su Sesión ha Terminado.";
		}
		return message; 
	}
		 
	
	public void cancelResponse(){
		try{
			if(SessionUtil.sessionUser().getUserId() != null){
			FacesContext context = FacesContext.getCurrentInstance();
			HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
				if (session != null) {
					this.setModalMessage("¿Está seguro de cancelar el restablecimiento de la contraseña?");
					this.setShowMsj(true);
				}
			}
		} catch (NullPointerException e) {
			this.setModalMessage("Su sesión ha terminado.");
			this.setShowMsjClose(true);
		}
	}
	
	
	public String logout() {
		try{
			FacesContext context = FacesContext.getCurrentInstance();
			HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
			this.hideModal();
			pM.regVerProcesosRC(usrs.getUserId(),SessionUtil.ip());
			if (session != null) {
				  if(usrs != null){	
					  loginEJB.updateLastLogin(usrs.getUserId());
					  session.invalidate();
				  }
			}
		} catch (NullPointerException e) {
			this.hideModal();			
			return "successOut";
		}
		return "successOut";
	}
	
	public void hideModal(){
		this.setShowModalValidate(false);
		this.setShowMsjClose(false);
		this.setShowMsj(false);
		this.setModalMessage(null);
	}
	
	public void clearFilter(){
		resp1 ="";
		resp2 ="";
		resp3 ="";
		resp4 ="";
		resp5 ="";
	}


	public void setShowModalValidate(boolean showModalValidate) {
		this.showModalValidate = showModalValidate;
	}

	public boolean isShowModalValidate() {
		return showModalValidate;
	}

	public void setPreg1(String preg1) {
		this.preg1 = preg1;
	}

	public String getPreg1() {
	 try{
		preg1 = pM.returnPgr(usrs.getUserId(),1);
	 }catch(Exception e){
		  System.out.println(" [ PasswordResetBean.getPreg1.Exception) ] "+e.getMessage());
	 }
	return preg1;
	}

	public void setPreg2(String preg2) {
		this.preg2 = preg2;
	}

	public String getPreg2() {
	  try{
		 preg2 = pM.returnPgr(usrs.getUserId(),2);
	  }catch(Exception e){
		  System.out.println(" [ PasswordResetBean.getPreg2.Exception) ] "+e.getMessage()); 
	  }
	return preg2;
	}

	public void setPreg3(String preg3) {		
		this.preg3 = preg3;
	}

	public String getPreg3() {
	  try{
		 preg3 = pM.returnPgr(usrs.getUserId(),3);
	  }catch(Exception e){
		  System.out.println(" [ PasswordResetBean.getPreg3.Exception) ] "+e.getMessage()); 
	  }
		return preg3;
	}

	public void setPreg4(String preg4) {
		this.preg4 = preg4;
	}

	public String getPreg4() {
	  try{
		 preg4 = pM.returnPgr(usrs.getUserId(),4);
	  }catch(Exception e){
		  System.out.println(" [ PasswordResetBean.getPreg4.Exception) ] "+e.getMessage()); 
	  }
		return preg4;
	}

	public void setPreg5(String preg5) {
		this.preg5 = preg5;
	}

	public String getPreg5() {
	  try{
		 preg5 = pM.returnPgr(usrs.getUserId(),5);
	  }catch(Exception e){
		  System.out.println(" [ PasswordResetBean.getPreg5.Exception) ] "+e.getMessage()); 
	  }
		return preg5;
	}

	public void setResp1(String resp1) {
		this.resp1 = resp1;
	}

	public String getResp1() {
		return resp1;
	}

	public void setResp2(String resp2) {
		this.resp2 = resp2;
	}

	public String getResp2() {
		return resp2;
	}

	public void setResp3(String resp3) {
		this.resp3 = resp3;
	}

	public String getResp3() {
		return resp3;
	}

	public void setResp4(String resp4) {
		this.resp4 = resp4;
	}

	public String getResp4() {
		return resp4;
	}

	public void setResp5(String resp5) {
		this.resp5 = resp5;
	}

	public String getResp5() {
		return resp5;
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
	
	public void setShowMsjClose(boolean showMsjClose) {
		this.showMsjClose = showMsjClose;
	}

	public boolean isShowMsjClose() {
		return showMsjClose;
	}

}
