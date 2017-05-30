
package mBeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollerEvent;

import jpa.TbSystemUser;
import sessionVar.SessionUtil;
import util.Cities;
import util.UsrRole;
import ejb.Role;
import ejb.User;

/**
 * @author cavellaneda
 * 
 */
public class ListUser implements Serializable {
	private static final long serialVersionUID = 1L;

	// Attributes ------------------ //
	
	@EJB(mappedName="ejb/User")
	private User userEjb;

	@EJB(mappedName="ejb/Role")
	private Role roleEjb;

	private List<TbSystemUser> listUser;

	private List<UsrRole> listRol;

	private List<Integer> listaScroll;

	private TbSystemUser systemUserEdit;

	private Long userId;
	private String useLastName;
	private String userSecondNames;
    private String userCode;


	private boolean modificate;

	private boolean disabled;

	private boolean reset;
	private boolean confirmResert;
	private boolean confirmResertQuestion;

	private boolean update;

	private String msg;
	
	private List<SelectItem> cities;	
	
	private boolean updateUser;
	
	private boolean showEditUser;
	
	private boolean updateRole;
	
	private boolean showConfirm;
		
	private int page=1;
	private int valuesFor;

	// Constructor ------------------------//
	
	public ListUser() {
		setSystemUserEdit(new TbSystemUser());
		setListUser(new ArrayList<TbSystemUser>());
		listRol = new ArrayList<UsrRole>();
	}

	// Actions ----------------------------//
	
	/**
	 * Prepares to edit an user.
	 */
	public void editUser() {
		for (TbSystemUser user : listUser) {
			if (user.getUserId() == userId) {
				systemUserEdit = user;
			}
		}
		this.showEditUser = true;
	}

	/**
	 * Saves the edited system user.
	 */
	public void saveUser() {
		
		this.showEditUser = false;
		if(userEjb.updateUser(systemUserEdit)){
			msg = "El Usuario fue Actualizado con Éxito.";
		}else{
			msg = "Error al realizar la Transacción, comuníquese con el Administrador.";
		}
		getListUser();
		setUpdateUser(true);
		setShowEditUser(false);
	}

	/**
	 * Inactive an user.
	 */
	public void changeStateUser() {
		disabled = userEjb.inactivateUser(userId, SessionUtil.ip(), SessionUtil
				.sessionUser().getUserId());
		listUser = userEjb.getAllUsersSystem(0,15);
		if (disabled) {
			msg = "El Usuario fue Inactivado con Éxito.";
		} else {
			msg = "Error al realizar la Transacción, comuníquese con el Administrador.";
		}
		setDisabled(true);
	}
	
	/**
	 * Activates an user.
	 */
	public void activateUser() {
		disabled = userEjb.activateUser(userId);
		listUser = userEjb.getAllUsersSystem(0,15);
		if (disabled) {
			msg = "El Usuario fue Activado con Éxito.";
		} else {
			msg = "Error al realizar la Transacción, comuníquese con el Administrador.";
		}
		setDisabled(true);
	}

	/**
	 * 
	 *Pop para confirmacion para restablecer la contraseña 
	 */
	public void confirmationRestartPassword(){		
		msg=" ¿Está seguro de restablecer la contraseña del usuario "+useLastName+" "+userSecondNames+". "+
		userEjb.getDocumentClient(userCode, userId, SessionUtil.ip(), SessionUtil.sessionUser().getUserId())+". "+userCode+"? ";
		this.setConfirmResert(true);
	}

	
	/**
	 * Reestablish the user password.
	 */
	public void resetPassword() {
		reset = userEjb.resetPassword(userId, SessionUtil.ip(), SessionUtil
				.sessionUser().getUserId());
		if(reset) {
			msg = "La contraseña del usuario fue restablecida con Éxito. Verificar correo electrónico.";
		} else {
			msg = "Error al realizar la Transacción, comuníquese con el Administrador.";
		}
		setConfirmResert(false);
		setReset(true);
	}
	
	
	public void confirmationRestartQuestion(){		
		msg=" ¿Está seguro de restablecer las preguntas de seguridad del usuario "+useLastName+" "+userSecondNames+". "+
		userEjb.getDocumentClient(userCode, userId, SessionUtil.ip(), SessionUtil.sessionUser().getUserId())+" "+userCode+"? ";	
		this.setConfirmResertQuestion(true);
	}
	/**
	 * Restablece preguntas de seguridad del usuario
	 * @psanchez
	 */
	public void restartQuestionsSecurity(){		
		reset = userEjb.resetSecurityQuestions(userId, SessionUtil.ip(), SessionUtil.sessionUser().getUserId());
		if(reset) {
			msg = "La preguntas de seguridad del usuario fue restablecida con Éxito.";
		} else {
			msg = "El usuario no tiene preguntas de seguridad configuradas.";

		}
		setConfirmResertQuestion(false);
		setReset(true);		
	}

	/**
	 * Prepares for update roles.
	 */
	public void changePermission() {
		listRol = roleEjb.getUsrRole(userId);
		this.update = true;
	}

	/**
	 * Updates a Role.
	 */
	public void updateRole() {
		setUpdate(false);
		setShowConfirm(false);
		if (roleEjb.updateUserRole(listRol, userId)) {
			this.setMsg("El rol ha sido actualizado con Éxito.");
		} else {
			this.setMsg("Error al realizar la Transacción, comuníquese con el Administrador.");
		}
		setUpdateRole(true);
	}
	
	public void showConfirmation(){
		this.setUpdateRole(false);
		this.setUpdate(false);
		this.setMsg("¿Está seguro que desea guardar los cambios?");
		this.setShowConfirm(true);	
	}

	/**
	 * Control Modal Panel.
	 */
	public void hideModal() {
		setModificate(false);
		setDisabled(false);
		setUpdate(false);
		setReset(false);
		setConfirmResert(false);
		setConfirmResertQuestion(false);
		setShowEditUser(false);
		setUpdateUser(false);
		setUpdateRole(false);
		setShowConfirm(false);
	}
	
	// Getters and Setters ----------------------//

	/**
	 * @param userEjb the userEjb to set
	 */
	public void setUserEjb(User userEjb) {
		this.userEjb = userEjb;
	}

	/**
	 * @return the userEjb
	 */
	public User getUserEjb() {
		return userEjb;
	}

	/**
	 * @param listUser the listUser to set
	 */
	public void setListUser(List<TbSystemUser> listUser) {
		this.listUser = listUser;
	}

	/**
	 * @return the listUser
	 */
	public List<TbSystemUser> getListUser() {
		if(listUser == null) {
			listUser = new ArrayList<TbSystemUser>();			
	    }else{
	    	listUser.clear(); 
		}
		this.getDataForScroll();
		listUser = userEjb.getAllUsersSystem(page,15);
		return listUser;
	}

	public List<SelectItem> getSelectListEmail(){
		List<SelectItem> items = new ArrayList<SelectItem>(0);
		
		for (TbSystemUser sysUser : userEjb.getAllUsersSystem(0,15)) {
			items.add(new SelectItem(sysUser.getUserId(), sysUser.getUserEmail()));
		}
		
		return items;
	}
	
	public List<SelectItem> getSelectListUser(){
		List<SelectItem> items = new ArrayList<SelectItem>(0);
		
		for (TbSystemUser sysUser : userEjb.getAllUsersSystem(0,15)) {
			items.add(new SelectItem(sysUser.getUserId(), sysUser.getUserNames()));
		}
		
		return items;
	}
	
	public void getDataForScroll(){
		try {
			 this.setValuesFor(Integer.parseInt(String.valueOf(userEjb.getAllUsersSystem(0, 15).get(0))));
			 listaScroll=new ArrayList<Integer>();
				for(int i=0;i<getValuesFor();i++){	
					listaScroll.add(i);
				}
		} catch (Exception e) {
	     	e.printStackTrace();
		}
	}
	
	public void dataScroller(ActionEvent event)throws AbortProcessingException {
		DataScrollerEvent events=(DataScrollerEvent)event;
		page = events.getPage();
		setPage(1);
	}
	
	/**
	 * 
	 * @return the systemUserEdit
	 */
	public TbSystemUser getSystemUserEdit() {
		return systemUserEdit;
	}

	/**
	 * 
	 * @param systemUserEdit
	 */
	public void setSystemUserEdit(TbSystemUser systemUserEdit) {
		this.systemUserEdit = systemUserEdit;
	}

	/**
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}

	
	
	


	public String getUseLastName() {
		return useLastName;
	}

	public void setUseLastName(String useLastName) {
		this.useLastName = useLastName;
	}

	public String getUserSecondNames() {
		return userSecondNames;
	}

	public void setUserSecondNames(String userSecondNames) {
		this.userSecondNames = userSecondNames;
	}

	/**
	 * @param modificate the modificate to set
	 */
	public void setModificate(boolean modificate) {
		this.modificate = modificate;
	}

	/**
	 * @return the modificate
	 */
	public boolean isModificate() {
		return modificate;
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

	/**
	 * @param reset  the reset to set
	 */
	public void setReset(boolean reset) {
		this.reset = reset;
	}

	/**
	 * @return the reset
	 */
	public boolean isReset() {
		return reset;
	}

	/**
	 * @param update the update to set
	 */
	public void setUpdate(boolean update) {
		this.update = update;
	}

	/**
	 * @return the update
	 */
	public boolean isUpdate() {
		return update;
	}

	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param listRol the listRol to set
	 */
	public void setListRol(List<UsrRole> listRol) {
		this.listRol = listRol;
	}

	/**
	 * @return the listRol
	 */
	public List<UsrRole> getListRol() {
		return listRol;
	}

	/**
	 * @param cities the cities to set
	 */
	public void setCities(List<SelectItem> cities) {
		this.cities = cities;
	}

	/**
	 * @return the cities
	 */
	public List<SelectItem> getCities() {
		cities = new ArrayList<SelectItem>();
		List<Cities> c = userEjb.getCities();
		for(Cities ci : c){
			cities.add(new SelectItem(ci.getCityCode(), ci.getCity() + " - "  + "(" + ci.getDepartment().substring(0,4) + ")"));
		}
		return cities;
	}

	/**
	 * @param updateUser the updateUser to set
	 */
	public void setUpdateUser(boolean updateUser) {
		this.updateUser = updateUser;
	}

	/**
	 * @return the updateUser
	 */
	public boolean isUpdateUser() {
		return updateUser;
	}

	/**
	 * @param showEditUser the showEditUser to set
	 */
	public void setShowEditUser(boolean showEditUser) {
		this.showEditUser = showEditUser;
	}

	/**
	 * @return the showEditUser
	 */
	public boolean isShowEditUser() {
		return showEditUser;
	}

	/**
	 * @param updateRole the updateRole to set
	 */
	public void setUpdateRole(boolean updateRole) {
		this.updateRole = updateRole;
	}

	/**
	 * @return the updateRole
	 */
	public boolean isUpdateRole() {
		return updateRole;
	}

	/**
	 * @param showConfirm the showConfirm to set
	 */
	public void setShowConfirm(boolean showConfirm) {
		this.showConfirm = showConfirm;
	}

	/**
	 * @return the showConfirm
	 */
	public boolean isShowConfirm() {
		return showConfirm;
	}

	public boolean isConfirmResert() {
		return confirmResert;
	}

	public void setConfirmResert(boolean confirmResert) {
		this.confirmResert = confirmResert;
	}

	public boolean isConfirmResertQuestion() {
		return confirmResertQuestion;
	}

	public void setConfirmResertQuestion(boolean confirmResertQuestion) {
		this.confirmResertQuestion = confirmResertQuestion;
	}
	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public void setListaScroll(List<Integer> listaScroll) {
		this.listaScroll = listaScroll;
	}

	public List<Integer> getListaScroll() {
		return listaScroll;
	}
	
	public void setPage(int page) {
		this.page = page;
	}

	public int getPage() {
		return page;
	}

	public void setValuesFor(int valuesFor) {
		this.valuesFor = valuesFor;
	}

	public int getValuesFor() {
		return valuesFor;
	}
	
}