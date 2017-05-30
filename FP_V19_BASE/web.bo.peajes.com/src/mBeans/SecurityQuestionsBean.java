package mBeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;

import security.UserLogged;
import sessionVar.SessionUtil;
import util.Encryptor;
import validator.Validation;

import jpa.ReGroupPassQuestion;
import jpa.TbSystemUser;
import ejb.Login;
import ejb.SecurityQuestions;
import ejb.SystemParameters;
import ejb.User;

public class SecurityQuestionsBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@EJB(mappedName="ejb/SecurityQuestions")
	private SecurityQuestions sq;
	
	@EJB(mappedName="ejb/User")
	private User userEJB;
	
	@EJB(mappedName = "ejb/Login")
	private Login loginEJB;
	
	@EJB(mappedName="ejb/SystemParameters")
	private SystemParameters SystemParameters;
	
	@PersistenceContext(unitName = "bo")
	private EntityManager em;
	
	private Long idPrg1;
	private Long idPrg2;
	private Long idPrg3;
	private Long idPrg4;
	
	private String rp1;
	private String rp2;
	private String rp3;
	private String rp4;
	private String rp5;
	private String p5;	
	private String modalMessage;
	private int numero=0;
	
	private boolean showModalNew;
	private boolean showModalmjm;
	private boolean showMsjClose;
	private boolean showModalValidate;
	private boolean showConfirmation;
	private boolean showConfirmationCancel;

	private List<SelectItem> listaPrg1;
	private List<SelectItem> listaPrg2;
	private List<SelectItem> listaPrg3;
	private List<SelectItem> listaPrg4;
	
	private ArrayList<String> resp;
	private ArrayList<Long> idPreg;
		
	private UserLogged usrs;

	public SecurityQuestionsBean(){
		usrs = (UserLogged) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
	}
	
	@PostConstruct
	public void init(){
		resp = new ArrayList<String>();
		idPreg = new ArrayList<Long>();	
		hideModal2();
	}
	
	/**
	 * @author psanchez
	 * Método encargado de guardar las respuestas ingresadas por el usuario. antiguo
	 */
	
	public void save(){	
		this.hideModal3();		
		resp.add(rp1.toUpperCase());
		resp.add(rp2.toUpperCase());
		resp.add(rp3.toUpperCase());
		resp.add(rp4.toUpperCase());
		resp.add(rp5.toUpperCase());
		
		idPreg.add(idPrg1);
		idPreg.add(idPrg2);
		idPreg.add(idPrg3);
		idPreg.add(idPrg4);
				
		if(sq.saveResponse(usrs.getUserId(), resp, idPreg, p5)){
			this.setModalMessage("Se guardó exitosamente la configuración de preguntas de seguridad.");
			this.setShowModalmjm(true);
			this.clearFilter();
		}else{
			this.setModalMessage("Error al realizar la configuración de preguntas de seguridad, por favor intente de nuevo.");
			this.setShowMsjClose(true);
		}
	}
	
	/** Método encargado de guardar las respuestas de seguridad del usuario nuevo
	 * @author psanchez
	 */	
	public void saveNew(){	
		this.hideModal3();		
		resp.add(rp1.toUpperCase());
		resp.add(rp2.toUpperCase());
		resp.add(rp3.toUpperCase());
		resp.add(rp4.toUpperCase());
		resp.add(rp5.toUpperCase());
		
		idPreg.add(idPrg1);
		idPreg.add(idPrg2);
		idPreg.add(idPrg3);
		idPreg.add(idPrg4);
			
		if(sq.saveResponse(usrs.getUserId(), resp, idPreg, p5)){
			this.setModalMessage("Se guardó exitosamente la configuración de preguntas de seguridad.");
			this.setShowModalNew(true);
			this.clearFilter();
		}else{
			this.setModalMessage("Error al realizar la configuración de preguntas de seguridad, por favor intente de nuevo.");
			this.setShowMsjClose(true);
		}
	}
	
	/**
	 * @author psanchez 
	 * Método encargado de validar las respuestas ingresadas por el usuario, las cuales no deben ser iguales ni vacias.**/
	
	public boolean validateResponse(){	
		try{
			if(usrs != null){
				if(idPrg1.equals(-1L)){
					this.setModalMessage("Debe seleccionar la pregunta 1.");
					this.setShowModalValidate(true);
					this.setNumero(1);
				}else if(rp1.trim().equals("") || rp1.trim().equals(null)){
					this.setModalMessage("Debe responder la pregunta 1. ");
					this.setShowModalValidate(true);
					this.setNumero(2);
				}else if(rp1.trim().length() < 5 ){
					this.setModalMessage("La respuesta 1 debe ser mínimo de 5 caracteres.");
					this.setShowModalValidate(true);
					this.setNumero(2);
				}else if(rp1.trim().length() > 250 ){
					this.setModalMessage("La respuesta 1 debe ser máximo de 250 caracteres.");
					this.setShowModalValidate(true);
					this.setNumero(2);
				}else if(validateResponses(this.getUsrs().getUserId(), rp1.trim(), 1).length()>0){ 
					String Message=validateResponses(this.getUsrs().getUserId(), rp1.trim(), 1);
					this.setModalMessage(Message);
					this.setShowModalValidate(true);
					this.setNumero(2);
				}else if (!Validation.validationQuestionCharacters(rp1.trim())) {
					this.setModalMessage("El campo respuesta pregunta 1 contiene caracteres inválidos.");
					this.setShowModalValidate(true);
					this.setNumero(2);
				}
				else if(idPrg2.equals(-1L)){
					this.setModalMessage("Debe seleccionar la pregunta 2. ");
					this.setShowModalValidate(true);
					this.setNumero(3);
				}else if(rp2.trim().equals("") || rp2.trim().equals(null)){
					this.setModalMessage("Debe responder la pregunta 2. ");
					this.setShowModalValidate(true);
					this.setNumero(4);
				}else if(rp2.trim().length() < 5 ){
					this.setModalMessage("La respuesta 2 debe ser mínimo de 5 caracteres.");
					this.setShowModalValidate(true);
					this.setNumero(4);
				}else if(rp2.trim().length() > 250 ){
					this.setModalMessage("La respuesta 2 debe ser máximo de 250 caracteres.");
					this.setShowModalValidate(true);
					this.setNumero(4);
				}else if(validateResponses(this.getUsrs().getUserId(), rp2.trim(), 2).length()>0){ 
					String Message=validateResponses(this.getUsrs().getUserId(), rp2, 2);
					this.setModalMessage(Message);
					this.setNumero(4);
					this.setShowModalValidate(true);
				}else if (!Validation.validationQuestionCharacters(rp2.trim())) {
					this.setModalMessage("El campo respuesta pregunta 2 contiene caracteres inválidos.");
					this.setShowModalValidate(true);
					this.setNumero(4);
				}
				else if(idPrg3.equals(-1L)){
					this.setModalMessage("Debe seleccionar la pregunta 3. ");
					this.setShowModalValidate(true);
					this.setNumero(5);
				}else if(rp3.trim().equals("") || rp3.trim().equals(null)){
					this.setModalMessage("Debe responder la pregunta 3. ");
					this.setShowModalValidate(true);
					this.setNumero(6);
				}else if(rp3.trim().length() < 5 ){
					this.setModalMessage("La respuesta 3 debe ser mínimo de 5 caracteres.");
					this.setShowModalValidate(true);
					this.setNumero(6);
				}else if(rp3.trim().length() > 250 ){
					this.setModalMessage("La respuesta 3 debe ser máximo de 250 caracteres.");
					this.setShowModalValidate(true);
					this.setNumero(6);
				}else if(validateResponses(this.getUsrs().getUserId(), rp3.trim(), 3).length()>0){ 
					String Message=validateResponses(this.getUsrs().getUserId(), rp3.trim(), 3);
					this.setModalMessage(Message);
					this.setShowModalValidate(true);
					this.setNumero(6);
				}else if (!Validation.validationQuestionCharacters(rp3.trim())) {
					this.setModalMessage("El campo respuesta pregunta 3 contiene caracteres inválidos.");
					this.setShowModalValidate(true);
					this.setNumero(6);
				}
				else if(idPrg4.equals(-1L)){
					this.setModalMessage("Debe seleccionar la pregunta 4. ");
					this.setShowModalValidate(true);
					this.setNumero(7);
				}else if(rp4.trim().equals("") || rp4.trim().equals(null)){
					this.setModalMessage("Debe responder la pregunta 4. ");
					this.setShowModalValidate(true);
					this.setNumero(8);
				}else if(rp4.trim().length() < 5 ){
					this.setModalMessage("La respuesta 4 debe ser mínimo de 5 caracteres.");
					this.setShowModalValidate(true);
					this.setNumero(8);
				}else if(rp4.trim().length() > 250 ){
					this.setModalMessage("La respuesta 4 debe ser máximo de 250 caracteres.");
					this.setShowModalValidate(true);
					this.setNumero(8);
				}else if(validateResponses(this.getUsrs().getUserId(), rp4.trim(), 4).length()>0){ 
					String Message=validateResponses(this.getUsrs().getUserId(), rp4.trim(), 4);
					this.setModalMessage(Message);
					this.setNumero(8);
					this.setShowModalValidate(true);
				}else if (!Validation.validationQuestionCharacters(rp4.trim())) {
					this.setModalMessage("El campo respuesta pregunta 4 contiene caracteres inválidos.");
					this.setShowModalValidate(true);
					this.setNumero(8);
				}
				else if(p5.equals("") || p5.equals(null)){
					this.setModalMessage("Debe ingresar la pregunta 5. ");
					this.setShowModalValidate(true);
					this.setNumero(9);
				}else if(p5.trim().length() < 5 ){
					this.setModalMessage("La pregunta 5 debe ser mínimo de 5 caracteres.");
					this.setShowModalValidate(true);
					this.setNumero(9);
				}else if(p5.trim().length() > 90 ){
					this.setModalMessage("La pregunta 5 debe ser máximo de 90 caracteres.");
					this.setShowModalValidate(true);
					this.setNumero(9);
				}else if(rp5.trim().equals("") || rp5.trim().equals(null)){
					this.setModalMessage("Debe responder la pregunta 5. ");
					this.setShowModalValidate(true);
					this.setNumero(10);
				}else if(rp5.trim().length() < 5 ){
					this.setModalMessage("La respuesta 5 debe ser mínimo de 5 caracteres.");
					this.setShowModalValidate(true);
					this.setNumero(10);
				}else if(rp5.trim().length() > 250 ){
					this.setModalMessage("La respuesta 5 debe ser máximo de 250 caracteres.");
					this.setShowModalValidate(true);
					this.setNumero(10);
				}else if(validateResponses(this.getUsrs().getUserId(), rp5.trim(), 5).length()>0){ 
					String Message=validateResponses(this.getUsrs().getUserId(), rp5.trim(), 5);
					this.setModalMessage(Message);
					this.setShowModalValidate(true);
					this.setNumero(10);
				}else if (!Validation.validationQuestionCharacters(rp5.trim())) {
					this.setModalMessage("El campo respuesta pregunta 5 contiene caracteres inválidos.");
					this.setShowModalValidate(true);
					this.setNumero(10);
				}
				else if(rp1.trim().equals(rp2.trim())){
					this.setModalMessage("La respuesta a la pregunta 1, no debe ser igual a la respuesta de la pregunta 2. ");
					this.setShowModalValidate(true);
					this.setNumero(2);
				}else if(rp1.trim().equals(rp3.trim())){ 
					this.setModalMessage("La respuesta a la pregunta 1, no debe ser igual a la respuesta de la pregunta 3. ");
					this.setShowModalValidate(true);
					this.setNumero(2);
				}else if(rp1.trim().equals(rp4.trim())){
					this.setModalMessage("La respuesta a la pregunta 1, no debe ser igual a la respuesta de la pregunta 4. ");
					this.setShowModalValidate(true);
					this.setNumero(2);
				}else if(rp1.trim().equals(rp5.trim())){
					this.setModalMessage("La respuesta a la pregunta 1, no debe ser igual a la respuesta de la pregunta 5. ");
					this.setShowModalValidate(true);
					this.setNumero(2);
				}
				else if(rp2.trim().equals(rp1.trim())){
					this.setModalMessage("La respuesta a la pregunta 2, no debe ser igual a la respuesta de la pregunta 1. ");
					this.setShowModalValidate(true);
					this.setNumero(4);
				}else if(rp2.trim().equals(rp3.trim())){
					this.setModalMessage("La respuesta a la pregunta 2, no debe ser igual a la respuesta de la pregunta 3. ");
					this.setShowModalValidate(true);
					this.setNumero(4);
				}else if(rp2.trim().equals(rp4.trim())){
					this.setModalMessage("La respuesta a la pregunta 2, no debe ser igual a la respuesta de la pregunta 4. ");
					this.setShowModalValidate(true);
					this.setNumero(4);
				}else if(rp2.trim().equals(rp5.trim())){
					this.setModalMessage("La respuesta a la pregunta 2, no debe ser igual a la respuesta de la pregunta 5. ");
					this.setShowModalValidate(true);
					this.setNumero(4);
				}
				else if(rp3.trim().equals(rp1.trim())){
					this.setModalMessage("La respuesta a la pregunta 3, no debe ser igual a la respuesta de la pregunta 1. ");
					this.setShowModalValidate(true);
					this.setNumero(6);
				}else if(rp3.trim().equals(rp2.trim())){
					this.setModalMessage("La respuesta a la pregunta 3, no debe ser igual a la respuesta de la pregunta 2. ");
					this.setShowModalValidate(true);
					this.setNumero(6);
				}else if(rp3.trim().equals(rp4.trim())){
					this.setModalMessage("La respuesta a la pregunta 3, no debe ser igual a la respuesta de la pregunta 4. ");
					this.setShowModalValidate(true);
					this.setNumero(6);
				}else if(rp3.trim().equals(rp5.trim())){
					this.setModalMessage("La respuesta a la pregunta 3, no debe ser igual a la respuesta de la pregunta 5. ");
					this.setShowModalValidate(true);
					this.setNumero(6);
				}
				else if(rp4.trim().equals(rp1.trim())){
					this.setModalMessage("La respuesta a la pregunta 4, no debe ser igual a la respuesta de la pregunta 1. ");
					this.setShowModalValidate(true);
					this.setNumero(8);
				}else if(rp4.trim().equals(rp2.trim())){
					this.setModalMessage("La respuesta a la pregunta 4, no debe ser igual a la respuesta de la pregunta 2. ");
					this.setShowModalValidate(true);
					this.setNumero(8);
				}else if(rp4.trim().equals(rp3.trim())){
					this.setModalMessage("La respuesta a la pregunta 4, no debe ser igual a la respuesta de la pregunta 3. ");
					this.setShowModalValidate(true);
					this.setNumero(8);
				}else if(rp4.trim().equals(rp5.trim())){
					this.setModalMessage("La respuesta a la pregunta 4, no debe ser igual a la respuesta de la pregunta 5. ");
					this.setShowModalValidate(true);
					this.setNumero(8);
				}
				else if(rp5.trim().equals(rp1.trim())){
					this.setModalMessage("La respuesta a la pregunta 5, no debe ser igual a la respuesta de la pregunta 1. ");
					this.setShowModalValidate(true);
					this.setNumero(10);
				}else if(rp5.trim().equals(rp2.trim())){
					this.setModalMessage("La respuesta a la pregunta 5, no debe ser igual a la respuesta de la pregunta 2. ");
					this.setShowModalValidate(true);
					this.setNumero(10);
				}else if(rp5.trim().equals(rp3.trim())){
					this.setModalMessage("La respuesta a la pregunta 5, no debe ser igual a la respuesta de la pregunta 3. ");
					this.setShowModalValidate(true);
					this.setNumero(10);
				}else if(rp5.trim().equals(rp4.trim())){
					this.setModalMessage("La respuesta a la pregunta 5, no debe ser igual a la respuesta de la pregunta 4. ");
					this.setShowModalValidate(true);
					this.setNumero(10);
				}else {
					this.setModalMessage("¿Está seguro de guardar la configuración de preguntas de seguridad?");
					this.setShowConfirmation(true);
				}
			}
		} catch (NullPointerException e) {
			System.out.println(" [ Error en SecurityQuestionsBean.validateResponse.NullPointerException ] "+e.getMessage());
			this.setModalMessage("Su sesión ha terminado.");
			this.setShowMsjClose(true);
			return false;
		} catch (Exception e) {
			System.out.println(" [ Error en SecurityQuestionsBean.validateResponse.Exception ] "+e.getMessage());
			this.setModalMessage("Su sesión ha terminado.");
			this.setShowMsjClose(true);
			return false;
		}
		return false;
	}

	/**
	 * Método creado para validar respuestas de seguridad contengan la identificación y/o secuencias repetidas
	 * @return return message
	 * @author psanchez
	 */
	public String validateResponses(Long userId, String newPassword, int questionId) {	
		String message = "";	
		try {
			String validateIdentificationResponse = SystemParameters.getParameterValueById(17L);
			String validateSequenceResponse = SystemParameters.getParameterValueById(19L);
			
			if(validateIdentificationResponse.equals("NO") && userEJB.containIdentification(newPassword, userId)){
				message="La respuesta de la pregunta "+ questionId +" no puede contener su número de identificación";
			}
			else if(validateSequenceResponse.equals("NO")) {
				message=userEJB.logicalSequences(newPassword,questionId);
			}
		} catch (NullPointerException e) {
			message="Error en el sistema, por favor intente de nuevo.";
			e.getMessage();
		}catch (Exception e) {
			message="Error en el sistema, por favor intente de nuevo.";
			e.getMessage();
		}
		return message; 
	}

	
	public String hideModalNew(){
		this.hideModal2();
		TbSystemUser systemUser = em.find(TbSystemUser.class, SessionUtil.sessionUser().getUserId());
		if(Encryptor.verifyPwd(systemUser.getUserPassword(), systemUser.getUserCode()) || 
		   Encryptor.verifyPwd(systemUser.getUserPassword(), loginEJB.userOtpLogin(systemUser.getUserId()))){
			return "successFL";
		}else if(loginEJB.preenrollUser(SessionUtil.sessionUser().getUserId())){
			return "securityQuestionsNewClient";
		}else {
			return "successCP";
		}
	}
	
	public void hideModal2(){
        this.hideModal3();
		this.clearFilter();
	}
	
	public void hideModal3(){
		this.setShowModalValidate(false);
		this.setShowConfirmation(false);
		this.setShowConfirmationCancel(false);
		this.setShowModalmjm(false);
		this.setShowMsjClose(false);
		this.setModalMessage(null);
	}
	
	
	public String logout() {
		try{
				FacesContext context = FacesContext.getCurrentInstance();
				HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
				hideModal2();
				sq.cancelConfPregVP(usrs.getUserId(),SessionUtil.ip());	
				if (session != null) {
					  if(usrs != null){	
						  loginEJB.updateLastLogin(usrs.getUserId());
						  session.invalidate();
					  }
				}
		} catch (NullPointerException e) {
			this.hideModal2();
			return "successOut";
		}
		return "successOut";
	}
	
	/**
	 * Método encargado de limpiar campos de preguntas y respuestas de seguridad
	 * @author psanchez
	 */
	public void clearFilter(){
		idPrg1 = -1L;
		idPrg2 = -1L;
		idPrg3 = -1L;
		idPrg4 = -1L;
		p5 = "";
		rp1 ="";
		rp2 ="";
		rp3 ="";
		rp4 ="";
		rp5 ="";
	}
	

	public void cancelResponse(){
		try{
			if(SessionUtil.sessionUser().getUserId() != null){
			FacesContext context = FacesContext.getCurrentInstance();
			HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
				if (session != null) {
					this.setModalMessage("¿Está seguro de cancelar el restablecimiento de la contraseña?");
					this.setShowConfirmationCancel(true);
				}
			}
		} catch (NullPointerException e) {
			this.setModalMessage("Su sesión ha terminado.");
			this.setShowMsjClose(true);
			e.getMessage();
		}catch (Exception e) {
			this.setModalMessage("Su sesión ha terminado.");
			this.setShowMsjClose(true);
			e.getMessage();
		}
	}

	
	/**Getters and Setters **/
	
	public void setIdPrg1(Long idPrg1) {
		this.idPrg1 = idPrg1;
	}

	public Long getIdPrg1() {
		return idPrg1;
	}

	public void setIdPrg2(Long idPrg2) {
		this.idPrg2 = idPrg2;
	}

	public Long getIdPrg2() {
		return idPrg2;
	}

	public void setIdPrg3(Long idPrg3) {
		this.idPrg3 = idPrg3;
	}

	public Long getIdPrg3() {
		return idPrg3;
	}

	public void setIdPrg4(Long idPrg4) {
		this.idPrg4 = idPrg4;
	}

	public Long getIdPrg4() {
		return idPrg4;
	}

	public void setListaPrg1(List<SelectItem> listaPrg1) {
		this.listaPrg1 = listaPrg1;
	}

	public List<SelectItem> getListaPrg1() {
		listaPrg1 = new ArrayList<SelectItem>();
		listaPrg1.add(new SelectItem(new Long(-1L), "--Seleccione una pregunta--"));
		for(ReGroupPassQuestion rgpq: sq.getAllQuestion(1L)){
			listaPrg1.add(new SelectItem(rgpq.getPassQuestionId().getPassQuestionId(),rgpq.getPassQuestionId().getPassQuestDesc().toString()));
		}
		return listaPrg1;
	}

	public void setListaPrg2(List<SelectItem> listaPrg2) {
		this.listaPrg2 = listaPrg2;
	}

	public List<SelectItem> getListaPrg2() {
		listaPrg2 = new ArrayList<SelectItem>();
		listaPrg2.add(new SelectItem(new Long(-1L), "--Seleccione una pregunta--"));
		for(ReGroupPassQuestion rgpq: sq.getAllQuestion(2L)){
			listaPrg2.add(new SelectItem(rgpq.getPassQuestionId().getPassQuestionId(),rgpq.getPassQuestionId().getPassQuestDesc()));
		}
		return listaPrg2;
	}

	public void setListaPrg3(List<SelectItem> listaPrg3) {		
		this.listaPrg3 = listaPrg3;
	}

	public List<SelectItem> getListaPrg3() {
		listaPrg3 = new ArrayList<SelectItem>();
		listaPrg3.add(new SelectItem(new Long(-1L), "--Seleccione una pregunta--"));
		for(ReGroupPassQuestion rgpq: sq.getAllQuestion(3L)){
			listaPrg3.add(new SelectItem(rgpq.getPassQuestionId().getPassQuestionId(),rgpq.getPassQuestionId().getPassQuestDesc()));
		}
		return listaPrg3;
	}

	public void setListaPrg4(List<SelectItem> listaPrg4) {
		this.listaPrg4 = listaPrg4;
	}

	public List<SelectItem> getListaPrg4() {
		listaPrg4 = new ArrayList<SelectItem>();
		listaPrg4.add(new SelectItem(new Long(-1L), "--Seleccione una pregunta--"));
		for(ReGroupPassQuestion rgpq: sq.getAllQuestion(4L)){
			listaPrg4.add(new SelectItem(rgpq.getPassQuestionId().getPassQuestionId(),rgpq.getPassQuestionId().getPassQuestDesc()));
		}
		return listaPrg4;
	}

	public void setRp1(String rp1) {
		this.rp1 = rp1;
	}

	public String getRp1() {
		return rp1;
	}

	public void setRp2(String rp2) {
		this.rp2 = rp2;
	}

	public String getRp2() {
		return rp2;
	}

	public void setRp3(String rp3) {
		this.rp3 = rp3;
	}

	public String getRp3() {
		return rp3;
	}

	public void setRp4(String rp4) {
		this.rp4 = rp4;
	}

	public String getRp4() {
		return rp4;
	}

	public void setP5(String p5) {
		this.p5 = p5;
	}

	public String getP5() {
		return p5;
	}

	public void setRp5(String rp5) {
		this.rp5 = rp5;
	}

	public String getRp5() {
		return rp5;
	}

	public void setShowModalmjm(boolean showModalmjm) {
		this.showModalmjm = showModalmjm;
	}

	public boolean isShowModalmjm() {
		return showModalmjm;
	}

	public void setModalMessage(String modalMessage) {
		this.modalMessage = modalMessage;
	}

	public String getModalMessage() {
		return modalMessage;
	}

	public void setShowModalValidate(boolean showModalValidate) {
		this.showModalValidate = showModalValidate;
	}

	public boolean isShowModalValidate() {
		return showModalValidate;
	}

	public void setUsrs(UserLogged usrs) {
		this.usrs = usrs;
	}



	public UserLogged getUsrs() {
		return usrs;
	}

	public void setResp(ArrayList<String> resp) {
		this.resp = resp;
	}

	public ArrayList<String> getResp() {
		return resp;
	}

	public void setIdPreg(ArrayList<Long> idPreg) {
		this.idPreg = idPreg;
	}

	public ArrayList<Long> getIdPreg() {
		return idPreg;
	}
	
	public void setShowConfirmation(boolean showConfirmation) {
		this.showConfirmation = showConfirmation;
	}

	public boolean isShowConfirmation() {
		return showConfirmation;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public int getNumero() {
		return numero;
	}

	public void setShowConfirmationCancel(boolean showConfirmationCancel) {
		this.showConfirmationCancel = showConfirmationCancel;
	}

	public boolean isShowConfirmationCancel() {
		return showConfirmationCancel;
	}

	public void setShowModalNew(boolean showModalNew) {
		this.showModalNew = showModalNew;
	}

	public boolean isShowModalNew() {
		return showModalNew;
	}

	public void setShowMsjClose(boolean showMsjClose) {
		this.showMsjClose = showMsjClose;
	}

	public boolean isShowMsjClose() {
		return showMsjClose;
	}

}
