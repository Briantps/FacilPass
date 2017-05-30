package mBeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import jpa.TbPassGroup;
import jpa.TbPassState;
import jpa.TbSystemUser;

import security.UserLogged;
import sessionVar.SessionUtil;

import util.ReGroupPassQ;
import validator.Validation;

import ejb.PasswordsManagement;


public class PasswordsManagementBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@EJB(mappedName = "ejb/PasswordsManagement")
	private PasswordsManagement passwordsManagementEJB;
	
	private List<SelectItem> groupList;
	private List<SelectItem> groupLisMod;
	private Long groupId;
	private Long groupIdSearch;
	
	private List<SelectItem> stateList;
	private List<SelectItem> stateListMod;
	private Long stateId;
	private Long stateIdSearch;
	
	private List<SelectItem> userList;
	private String userEmail;
	
	private List<ReGroupPassQ> questionsList;
	private Long questionResponseId;

	private Date startDate;
	private Date finalDate;
	
	private boolean showModalMessage;
	private boolean showUpdateQuestion;
	private boolean showCreateQuestion=true;
	private boolean showModalValidateSearch;
	private boolean showModalValidateCreate;
	private boolean showModalValidateUpdate;
	private boolean showModalConfirmationCreate;
	private boolean showModalConfirmationDelete;
	private boolean showModalConfirmationUpdate;
	private boolean showModalCancel;

	private boolean viewPanelCreateQuestion= false;
	private boolean disableFields=false;

	private String stateQuestion;
	private String question;
	private String message;
	private String questionTemp;
	private Long stateIdTemp;
	private Long groupIdTemp;
	private Long questionId;
	
	private UserLogged usrs;
	
	public PasswordsManagementBean() {
		setUsrs((UserLogged) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user"));
		startDate = new Date();
		finalDate = new Date();
	}
	
	@PostConstruct
	public void init() { 
		if(questionsList == null) {
			questionsList = new ArrayList<ReGroupPassQ>();
		}else{
			questionsList.clear();
		}
		questionsList = passwordsManagementEJB.getListAllGrupPassQ();
	}
	
	/**
	 * Método encargado de listar resultados al ingresar filtros de búsqueda
	 * @author psanchez
	 */
	public void searchFilter(){
		 try{
			 if(postValidationSearch()){
				List<ReGroupPassQ> listfilter = null;
				listfilter = passwordsManagementEJB.getListGrupPassByFilters(groupIdSearch, stateIdSearch, startDate, finalDate, userEmail);
				if (listfilter.size() <= 0) {
					this.setMessage("No se encontraron resultados de la búsqueda.");
					this.setShowModalMessage(true);
					this.questionsList.clear();
				} else {
					this.questionsList = listfilter;
				}
			 }
		 } catch (Exception e) {
			this.setMessage("Error en transación");
			this.setShowModalMessage(true);
			e.printStackTrace();
		 } 
	}
	
	/**
	 * Método encargado de validar los filtros de búsqueda
	 * @return return true si el filtro cumple con las validaciones o false en otro caso.
	 * @author psanchez
	 */
	private boolean postValidationSearch(){
		if (groupIdSearch == -1L && stateIdSearch == -1L && userEmail.equals("-1") && startDate == null && finalDate == null) {
			this.setMessage("No existe datos para realizar la búsqueda.");
			this.setShowModalMessage(true);
			return false;
		}
		else if(startDate!=null && finalDate==null) {
	    	this.setMessage("La fecha final de creación no debe estar vacía.");
			this.setShowModalValidateSearch(true);
			return false;
		}
		else if(finalDate!=null && startDate==null) {
	    	this.setMessage("La fecha inicial de creación no debe estar vacía.");
			this.setShowModalValidateSearch(true);
			return false;
		}
		else if(startDate!=null && (startDate.getTime()>new Date().getTime())) {
	    	this.setMessage("La fecha inicial de creación no debe ser mayor a la fecha actual.");
			this.setShowModalValidateSearch(true);
			return false;
		}
		else if(finalDate!=null && (finalDate.getTime()>(new Date()).getTime())) {
	    	this.setMessage("La fecha final de creación no debe ser mayor a la fecha actual.");
			this.setShowModalValidateSearch(true);
			return false;
		}		
	    else if(startDate!=null && (startDate.getTime()>finalDate.getTime())) {
	    	this.setMessage("La fecha inicial de creación debe ser menor a la fecha final de creación.");
			this.setShowModalValidateSearch(true);
			return false;
	    }	 
		return true;
	}
	
	/**
	 * Método encargado de confirmar la eliminación de la pregunta
	 * @author psanchez
	 */
	public void showConfirmDelete(){
		this.setDisableFields(true);
		this.setShowModalConfirmationDelete(false);
		this.setShowCreateQuestion(true);
		this.setShowUpdateQuestion(false);
		this.setViewPanelCreateQuestion(false);
		this.setShowModalConfirmationDelete(true);
		this.setMessage("¿Está seguro de eliminar la pregunta: " +question+"?");
	}


	/**
	 * @author Nramíez Método encargado de eliminar pregunta de seguridad
	 **/
	public void deleteQuestion() {
		this.setShowModalConfirmationDelete(false);
		try {
			if(passwordsManagementEJB.deleteQuestion(usrs.getUserId(), questionResponseId, SessionUtil.ip(), SessionUtil.sessionUser().getUserId())){
				this.setMessage("Se ha eliminado la pregunta exitosamente.");
				this.setShowModalMessage(true);
			} else {
				this.setMessage("No se pudo eliminar la pregunta.");
				this.setShowModalMessage(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.setMessage("No se pudo eliminar la pregunta.");
			this.setShowModalMessage(true);
		}
	}
	
	public void panelUpdateQuestion(){
		this.setDisableFields(true);
		this.setShowUpdateQuestion(true);
		this.setShowCreateQuestion(false);
		questionTemp = question;
		stateIdTemp = stateId;
		groupIdTemp = groupId;
		List<ReGroupPassQ> listfilter = null;
		listfilter = passwordsManagementEJB.getListGrupPassByFilters(-1L, -1L, null, null, "-1");
		if (listfilter.size() <= 0) {
			this.questionsList.clear();
		} else {
			this.questionsList = listfilter;
		}
	}

	/**
	 * Método encargado de validar la pregunta de seguridad modificada
	 * @author psanchez
	 */
	public void validateUpdateQuestion() {
		String dato = passwordsManagementEJB.validateQuestionUpdate(question.trim(), groupIdTemp, groupId);
			if (groupId == null || groupId == -1L) {
				this.setMessage("Falta seleccionar el grupo.");
				this.setShowModalValidateUpdate(true);
			}
		    else if (stateId == null || stateId == -1L) {
				this.setMessage("Falta seleccionar un estado.");
				this.setShowModalValidateUpdate(true);
			}
			else if (question.equals("") || question.trim().length() == 0) {
				this.setMessage("El campo pregunta no puede estar vacío.");
				this.setShowModalValidateUpdate(true);
			}
		    else if (dato != null) {
		    	this.setMessage("La pregunta ingresada ya se encuentra registrada en el sistema para el grupo " + dato + ".");
				this.setShowModalValidateUpdate(true);
			}
		    else if (!Validation.isObservationPSE(question)) {
				this.setMessage("El campo pregunta contiene caracteres inválidos.");
				this.setShowModalValidateUpdate(true);
		    }
			else if (question.length() < 5) {
				this.setMessage("La pregunta debe tener una longitud mínima de 5 caracteres.");
	            this.setShowModalValidateUpdate(true);
			}
			else if (question.length() > 90 ) {
				this.setMessage("La pregunta debe tener una longitud máxima de 90 caracteres.");
	            this.setShowModalValidateUpdate(true);
			}
			else {
				this.setMessage("¿Está seguro de modificar la pregunta \n "
						+ " para el grupo "
						+ groupId
						+ "?");
				this.setShowModalConfirmationUpdate(true);
			}
	}
	
	/**
	 * Método creado para modificar preguntas
	 * @author Nramíez Método encargado de modificar la pregunta de seguridad creadas
	 * @author psanchez Método encargado de modificar la pregunta de seguridad creadas de acuerdo al AND-038 Autogestión de Password TD04 item 7
	 **/
	public void updateQuestion() {
		try {
			this.setShowModalConfirmationUpdate(false);
			if (!question.equals(questionTemp)) {//modificacion de la pregunta cuando no es igual a la que digito de la que hay en el sistema
				passwordsManagementEJB.createQuestion(usrs.getUserId(), groupId, stateId, question.trim(), 
						SessionUtil.ip(), SessionUtil.sessionUser().getUserId());
				passwordsManagementEJB.updateQuestion(usrs.getUserId(), groupIdTemp, 4L, questionId,
						questionTemp.trim(), questionResponseId, 1L, SessionUtil.ip(), SessionUtil.sessionUser().getUserId());
				this.setMessage("Se ha modificado la pregunta exitosamente.");
				this.init();
				this.setShowModalMessage(true);
			} else if (stateId.longValue() != stateIdTemp.longValue()) {//modificacion del estado de la pregunta
				passwordsManagementEJB.updateQuestion(usrs.getUserId(), groupId, stateId, questionId,
						questionTemp.trim(), questionResponseId, 1L, SessionUtil.ip(), SessionUtil.sessionUser().getUserId());
				this.setMessage("Se ha modificado la pregunta exitosamente.");
				this.init();
				this.setShowModalMessage(true);
			} else if (groupId.longValue() != groupIdTemp.longValue()) { //modificacion del grupo de la pregunta
				passwordsManagementEJB.createQuestion(usrs.getUserId(), groupId, stateId, question.trim(), 
						SessionUtil.ip(), SessionUtil.sessionUser().getUserId());
				passwordsManagementEJB.updateQuestion(usrs.getUserId(), groupIdTemp, 4L, questionId,
						questionTemp.trim(), questionResponseId, 1L, SessionUtil.ip(), SessionUtil.sessionUser().getUserId());
				this.setMessage("Se ha modificado la pregunta exitosamente.");
				this.init();
				this.setShowModalMessage(true);
			} else {
				this.setMessage("La pregunta no ha sido modificada.");
				this.init();
				this.setShowModalMessage(true);
			}
	   }  catch (Exception e) {
			e.printStackTrace();
			this.setMessage("Se ha presentado un error al momento de modificar la pregunta, por favor intente nuevamente.");
			this.setShowModalMessage(true);
	   }
	}
	
	/**
	 * Método encargado de confirmar modificación de la pregunta de seguridad
	 * @author psanchez
	 */
	public void cancelUpdateQuestion(){
		this.setMessage("¿Está seguro de cancelar la modificación de la pregunta.?");
		this.setShowModalCancel(true);
	}
	
	/**
	 * Método encargado de confirmar cancelación de la creación de la pregunta de seguridad
	 * @author psanchez
	 */
	public void cancelCreateQuestion(){
		this.setMessage("¿Está seguro de cancelar la creación de la pregunta.?");
		this.setShowModalCancel(true);
	}

	
	public void panelCreateQuestion() {
		this.setDisableFields(true);
		this.setViewPanelCreateQuestion(true);
		this.setGroupId(-1L);
		this.setStateId(-1L);
		this.setQuestion(null);
		List<ReGroupPassQ> listfilter = null;
		listfilter = passwordsManagementEJB.getListGrupPassByFilters(-1L, -1L, null, null, "-1");
		if (listfilter.size() <= 0) {
			this.questionsList.clear();
		} else {
			this.questionsList = listfilter;
		}
	}
	
	/**
	 * Método encargado de validar la creación pregunta de seguridad
	 * @author psanchez
	 */
	public void validateCreateQuestion() {
		String dato = passwordsManagementEJB.validateQuestion(question.trim());
		    if (groupId == null || groupId == -1L) {
				this.setMessage("Falta seleccionar el grupo.");
				this.setShowModalValidateCreate(true);
			}
			else if (question.equals("")|| question.trim().length() == 0) {
				this.setMessage("El campo pregunta no puede estar vacío.");
				this.setShowModalValidateCreate(true);
			}
			else if (dato != null) {
				this.setMessage("La pregunta ingresada ya se encuentra registrada en el sistema para el grupo " + dato + ".");
				this.setShowModalValidateCreate(true);
			}
		    else if (!Validation.isObservationPSE(question)) {
				this.setMessage("El campo pregunta contiene caracteres inválidos.");
				this.setShowModalValidateCreate(true);
		    }
			else if (question.length() < 5) {
				this.setMessage("La pregunta debe tener una longitud mínima de 5 caracteres.");
                this.setShowModalValidateCreate(true);
			} 
			else if (question.length() > 90) {
				this.setMessage("La pregunta debe tener una longitud máxima de 90 caracteres.");
                this.setShowModalValidateCreate(true);
			}
			else {
				this.setMessage("¿Está seguro de crear la pregunta "
						+ question
						+ " para el grupo "
						+ groupId
						+ "?");
				this.setShowModalConfirmationCreate(true);
			}
	}
	
	public void createQuestion() {
		try {
			this.setShowModalConfirmationCreate(false);
			if (passwordsManagementEJB.createQuestion(usrs.getUserId(), groupId, null, question.trim(), 
					SessionUtil.ip(), SessionUtil.sessionUser().getUserId())){
				this.setMessage("Se ha registrado la pregunta exitosamente.");
				this.init();
				this.setShowModalMessage(true);
			} else {
				this.setMessage("Se ha presentado un error al momento de registrar la pregunta, por favor intente nuevamente.");
				this.setShowModalMessage(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.setMessage("No se pudo crear la pregunta.");
			this.setShowModalMessage(true);
		}
	}
	
	/**
	 * Método encargado de limpiar filtros de búsqueda
	 * @author psanchez
	 */
	public void clearFilter(){
		this.setGroupIdSearch(-1L);
		this.setStateIdSearch(-1L);
		this.setUserEmail("-1L");
		this.setStartDate(null);
		this.setFinalDate(null);
		this.init();
	}
	
	public void hideModal(){
		this.init();
		this.setShowModalMessage(false);
		this.setDisableFields(false);
		this.setShowCreateQuestion(true);
		this.setShowUpdateQuestion(false);
		this.setViewPanelCreateQuestion(false);
		this.setShowModalValidateSearch(false);
		this.setShowModalValidateUpdate(false);
		this.setShowModalValidateCreate(false);
		this.setShowModalConfirmationCreate(false);
		this.setShowModalConfirmationDelete(false);
		this.setShowModalConfirmationUpdate(false);
		this.setShowModalCancel(false);
		this.setMessage(null);
	}
	
	public void hideModal2(){
		this.setDisableFields(false);
		this.setShowModalMessage(false);
		this.setShowCreateQuestion(true);
		this.setViewPanelCreateQuestion(false);
		this.setShowModalValidateSearch(false);
		this.setShowModalConfirmationCreate(false);
		this.setShowModalConfirmationDelete(false);
		this.setShowModalConfirmationUpdate(false);
		this.setMessage(null);
	}
	
	public void hideModal3(){
		this.setShowModalConfirmationCreate(false);
		this.setShowModalConfirmationDelete(false);
		this.setShowModalConfirmationUpdate(false);
		this.setShowModalCancel(false);
		this.setMessage(null);
	}
	
	public void hideModalValidateCreate(){
		this.setShowModalMessage(false);
		this.setShowCreateQuestion(true);
		this.setViewPanelCreateQuestion(true);
		this.setShowModalValidateSearch(false);
		this.setShowModalValidateCreate(false);
		this.setShowModalConfirmationDelete(false);
		this.setMessage(null);
	}
	
	public void hideModalValidateUpdate(){
		this.setShowModalMessage(false);
		this.setShowCreateQuestion(false);
		this.setShowUpdateQuestion(true);
		this.setShowModalValidateUpdate(false);
		this.setViewPanelCreateQuestion(false);
		this.setShowModalValidateSearch(false);
		this.setShowModalValidateCreate(false);
		this.setShowModalConfirmationDelete(false);
		this.setMessage(null);
	}
	
	public void setGroupList(List<SelectItem> groupList) {
		this.groupList = groupList;
	}
	
	public List<SelectItem> getGroupList() {
		groupList = new ArrayList<SelectItem>();
		groupList.add(new SelectItem(new Long(-1L), "--Seleccione Uno--"));
		for (TbPassGroup tpg : passwordsManagementEJB.getAllPassGroup()) {
			groupList.add(new SelectItem(tpg.getPassGroupId(), tpg.getPassGroupName().toUpperCase()));
		}
		return groupList;
	}
	
	public void setStateList(List<SelectItem> stateList) {
		this.stateList = stateList;
	}
	
	public List<SelectItem> getStateList() {
		stateList = new ArrayList<SelectItem>();
		stateList.add(new SelectItem(new Long(-1L), "--Seleccione Uno--"));
		for (TbPassState tps : passwordsManagementEJB.getAllPassEst()) {
			stateList.add(new SelectItem(tps.getPassStateId(), tps.getPassStateDescription().toUpperCase()));
		}
		return stateList;
	}
	
	public void setUserList(List<SelectItem> userList) {
		this.userList = userList;
	}
	
	public List<SelectItem> getUserList() {
		userList = new ArrayList<SelectItem>();
		userList.add(new SelectItem(new Long(-1L), "--Seleccione Uno--"));
		for (TbSystemUser tsu : passwordsManagementEJB.getUserCreate()) {
			userList.add(new SelectItem(tsu.getUserEmail(), tsu.getUserEmail()));
		}
		return userList;
	}
	
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public Long getGroupId() {
		return groupId;
	}
	public void setStateId(Long stateId) {
		this.stateId = stateId;
	}
	public Long getStateId() {
		return stateId;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setFinalDate(Date finalDate) {
		this.finalDate = finalDate;
	}
	public Date getFinalDate() {
		return finalDate;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setQuestionsList(List<ReGroupPassQ> questionsList) {
		this.questionsList = questionsList;
	}

	public List<ReGroupPassQ> getQuestionsList() {
		return questionsList;
	}

	public void setUsrs(UserLogged usrs) {
		this.usrs = usrs;
	}

	public UserLogged getUsrs() {
		return usrs;
	}

	public void setShowCreateQuestion(boolean showCreateQuestion) {
		this.showCreateQuestion = showCreateQuestion;
	}

	public boolean isShowCreateQuestion() {
		return showCreateQuestion;
	}

	public void setViewPanelCreateQuestion(boolean viewPanelCreateQuestion) {
		this.viewPanelCreateQuestion = viewPanelCreateQuestion;
	}

	public boolean isViewPanelCreateQuestion() {
		return viewPanelCreateQuestion;
	}
	
	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public void setStateQuestion(String stateQuestion) {
		this.stateQuestion = stateQuestion;
	}

	public String getStateQuestion() {
		return stateQuestion;
	}

	public void setDisableFields(boolean disableFields) {
		this.disableFields = disableFields;
	}

	public boolean isDisableFields() {
		return disableFields;
	}

	public void setQuestionResponseId(Long questionResponseId) {
		this.questionResponseId = questionResponseId;
	}

	public Long getQuestionResponseId() {
		return questionResponseId;
	}
	
	public boolean isShowModalValidateCreate() {
		return showModalValidateCreate;
	}
	
	public void setShowModalValidateCreate(boolean showModalValidateCreate) {
		this.showModalValidateCreate = showModalValidateCreate;
	}
	
	public void setShowModalValidateSearch(boolean showModalValidateSearch) {
		this.showModalValidateSearch = showModalValidateSearch;
	}

	public boolean isShowModalValidateSearch() {
		return showModalValidateSearch;
	}

	public void setShowModalConfirmationDelete(boolean showModalConfirmationDelete) {
		this.showModalConfirmationDelete = showModalConfirmationDelete;
	}

	public boolean isShowModalConfirmationDelete() {
		return showModalConfirmationDelete;
	}

	public void setShowModalConfirmationCreate(boolean showModalConfirmationCreate) {
		this.showModalConfirmationCreate = showModalConfirmationCreate;
	}

	public boolean isShowModalConfirmationCreate() {
		return showModalConfirmationCreate;
	}

	public void setShowUpdateQuestion(boolean showUpdateQuestion) {
		this.showUpdateQuestion = showUpdateQuestion;
	}

	public boolean isShowUpdateQuestion() {
		return showUpdateQuestion;
	}

	public void setShowModalConfirmationUpdate(boolean showModalConfirmationUpdate) {
		this.showModalConfirmationUpdate = showModalConfirmationUpdate;
	}

	public boolean isShowModalConfirmationUpdate() {
		return showModalConfirmationUpdate;
	}

	public void setShowModalValidateUpdate(boolean showModalValidateUpdate) {
		this.showModalValidateUpdate = showModalValidateUpdate;
	}

	public boolean isShowModalValidateUpdate() {
		return showModalValidateUpdate;
	}

	public void setShowModalMessage(boolean showModalMessage) {
		this.showModalMessage = showModalMessage;
	}

	public boolean isShowModalMessage() {
		return showModalMessage;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public void setShowModalCancel(boolean showModalCancel) {
		this.showModalCancel = showModalCancel;
	}

	public boolean isShowModalCancel() {
		return showModalCancel;
	}

	public void setGroupIdSearch(Long groupIdSearch) {
		this.groupIdSearch = groupIdSearch;
	}

	public Long getGroupIdSearch() {
		return groupIdSearch;
	}

	public void setStateIdSearch(Long stateIdSearch) {
		this.stateIdSearch = stateIdSearch;
	}

	public Long getStateIdSearch() {
		return stateIdSearch;
	}

	public void setGroupLisMod(List<SelectItem> groupLisMod) {
		this.groupLisMod = groupLisMod;
	}

	public List<SelectItem> getGroupLisMod() {
		groupLisMod = new ArrayList<SelectItem>();
		groupLisMod.add(new SelectItem(new Long(-1L), "--Seleccione Uno--"));
		for (TbPassGroup tpg : passwordsManagementEJB.getAllPassGroup()) {
			groupLisMod.add(new SelectItem(tpg.getPassGroupId(), tpg.getPassGroupName().toUpperCase()));
		}
		return groupLisMod;
	}

	public void setStateListMod(List<SelectItem> stateListMod) {
		this.stateListMod = stateListMod;
	}

	public List<SelectItem> getStateListMod() {
		stateListMod = new ArrayList<SelectItem>();
		stateListMod.add(new SelectItem(new Long(-1L), "--Seleccione Uno--"));
		for (TbPassState tps : passwordsManagementEJB.getAllPassEst()) {
			stateListMod.add(new SelectItem(tps.getPassStateId(), tps.getPassStateDescription().toUpperCase()));
		}
		return stateListMod;
	}

}