
package crud;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import jpa.TbBank;
import jpa.TbSystemUser;

import util.Cities;

import ejb.User;

/**
 * @author psanchez
 * 
 */
public class AutheAdminAuthoUserBean implements Serializable {
	private static final long serialVersionUID = 1L;

	// Attributes ------------------ //
	
	@EJB(mappedName="ejb/User")
	private User userEjb;

	private List<TbSystemUser> listUser;

	private TbSystemUser systemUserEdit;

	private long userId;

	private boolean modificate;

	private boolean update;

	private String msg;
	
	private List<SelectItem> cities;	
	
	private boolean updateUser;
	
	private boolean showEditUser;
	
	private boolean updateRole;
	
    private String userNames;
	
	private String userSecondNames;
	
	private String userEmail;
	
	private boolean showModal;
	private boolean showInsert;
	private boolean showEdit;

	// Constructor ------------------------//
	
	public AutheAdminAuthoUserBean() {
		setSystemUserEdit(new TbSystemUser());
		setListUser(new ArrayList<TbSystemUser>());
		//listRol = new ArrayList<UsrRole>();
	}

	// Actions ----------------------------//
	
	/**
	 * Prepares to edit an user.
	 */
	public String editUser() {
		for (TbSystemUser user : listUser) {
			if (user.getUserId() == userId) {
				systemUserEdit = user;
			}
		}
		this.showEditUser = true;
		return null;
	}

	/**
	 * Saves the edited system user.
	 */
	public String saveUser() {
		
		this.showEditUser = false;
		if(userEjb.updateUser(systemUserEdit)){
			msg = "El Usuario fue Actualizado con Éxito.";
		}else{
			msg = "Error al realizar la Transacción, comuníquese con el Administrador.";
		}
		getListUser();
		setUpdateUser(true);
		setShowEditUser(false);
		return null;
	}
	
	/**
	 * Init Edition of bank.
	 */
	public String initEditBank(){
	/*	setShowInsert(false);
		setShowEdit(true);
		for(TbBank b : bankList){
			if(bankId.longValue() ==b.getBankId().longValue()){
				bankName = b.getBankName();
				break;
			}
		}*/
		return null;
	}


	/**
	 * Control Modal Panel.
	 */
	public void hideModal() {
		setModificate(false);
		setUpdate(false);
		setShowEditUser(false);
		setUpdateUser(false);
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
		listUser = userEjb.getAllUsers();
		return listUser;
	}

	public List<SelectItem> getSelectListEmail(){
		List<SelectItem> items = new ArrayList<SelectItem>(0);
		
		for (TbSystemUser sysUser : userEjb.getAllUsers()) {
			items.add(new SelectItem(sysUser.getUserId(), sysUser.getUserEmail()));
		}
		
		return items;
	}
	
	public List<SelectItem> getSelectListUser(){
		List<SelectItem> items = new ArrayList<SelectItem>(0);
		
		for (TbSystemUser sysUser : userEjb.getAllUsers()) {
			items.add(new SelectItem(sysUser.getUserId(), sysUser.getUserNames()));
		}
		
		return items;
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
	 * @param userNames the userNames to set
	 */
	public void setUserNames(String userNames) {
		this.userNames = userNames;
	}

	/**
	 * @return the userNames
	 */
	public String getUserNames() {
		return userNames;
	}

	/**
	 * @param userSecondNames the userSecondNames to set
	 */
	public void setUserSecondNames(String userSecondNames) {
		this.userSecondNames = userSecondNames;
	}

	/**
	 * @return the userSecondNames
	 */
	public String getUserSecondNames() {
		return userSecondNames;
	}

	/**
	 * @param userEmail the userEmail to set
	 */
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	/**
	 * @return the userEmail
	 */
	public String getUserEmail() {
		return userEmail;
	}
}