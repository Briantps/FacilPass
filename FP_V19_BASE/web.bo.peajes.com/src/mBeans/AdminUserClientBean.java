package mBeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import sessionVar.SessionUtil;

import jpa.TbSystemUser;

import ejb.User;

/**
 * @author psanchez
 * 
 */
public class AdminUserClientBean implements Serializable {
	private static final long serialVersionUID = 1L;

	// Attributes ------------------ //
	
	@EJB(mappedName="ejb/User")
	private User userEjb;

	private List<TbSystemUser> usersClientsList;
	private List<TbSystemUser> enableUsersList;
	private List<TbSystemUser> deleteUsersList;
	private List<TbSystemUser> onlineUsersList;

	private boolean remove;
	private boolean disabled;
	private boolean reset;
	
	private long userId;
	
	private boolean showEdit;
	private boolean showModal;
	private String modalMessage;

	// Constructor ------------------------//
	
	public AdminUserClientBean() {
	}
	
	/**
	 * Método encargado de listar los usuarios del sistema.
	 * @return the usersClientsList
	 * @author psanchez
	 */
	public List<TbSystemUser> getUsersClientsList() {
		if(usersClientsList == null) {
			usersClientsList = new ArrayList<TbSystemUser>();
			usersClientsList = userEjb.getAllUsersClients();
		}
		return usersClientsList;		
	}
	
	
	/**
	 * Método encargado de listar los usuarios activos del sistema.
	 * @return the enableUsersList
	 * @author psanchez
	 */
	public List<TbSystemUser> getEnableUsersList() {
		if(enableUsersList == null) {
			enableUsersList = new ArrayList<TbSystemUser>();
			enableUsersList = userEjb.getEnableUsersList();
			}
		return enableUsersList;
	}
	
	
	/**
	 * Método encargado de listar los usuarios a eliminar, siempre y cuando no tenga transacción asociadas.
	 * @return the deleteUsersList
	 * @author psanchez
	 */
	public List<TbSystemUser> getDeleteUsersList() {
		if(deleteUsersList == null) {
			deleteUsersList = new ArrayList<TbSystemUser>();
			deleteUsersList = userEjb.getDeleteUsersClients();
			}
		return deleteUsersList;
	}
	
	/**
	 * Método encargado de listar los usuarios conectados a la aplicación.
	 * @return the listOnlineUser
	 * @author psanchez
	 */
	public List<TbSystemUser> getOnlineUsersList() {
		if(onlineUsersList == null) {
		   onlineUsersList = new ArrayList<TbSystemUser>();
		   onlineUsersList = userEjb.getOnlineUsersList();
		}
		return onlineUsersList;
	}
	
	
	/**
	 * Método usado para eliminar usuarios del sistema.
	 * @author psanchez
	 */
	public String removeUser() {
		remove = userEjb.deleteUser(userId, SessionUtil.ip(), SessionUtil
				.sessionUser().getUserId());
		deleteUsersList = userEjb.getDeleteUsersClients();
		if (remove) {
			setShowModal(true);
			modalMessage = "El Usuario fué Eliminado con Éxito.";
		} else {
			modalMessage= "Error en Transacción.";
		}
		setShowModal(true);
		setRemove(true);
		return null;
	}
	

	/**
	 * Método usado para inactivar usuarios del sistema.
	 */
	public String changeStateUser() {
		disabled = userEjb.inactivateUser(userId, SessionUtil.ip(), SessionUtil
				.sessionUser().getUserId());
		enableUsersList = userEjb.getEnableUsersList();
		if (disabled) {
			setShowModal(true);
			modalMessage = "El Usuario fué Inactivado con Éxito.";
		} else {
			modalMessage= "Error en Transacción.";
		}
		setDisabled(true);
		return null;
	}

	
	/**
	 * Método usado para restablecer contaseñas de usuarios del sistema.
	 */
	public String resetPassword() {
		reset = userEjb.resetPassword(userId, SessionUtil.ip(), SessionUtil
				.sessionUser().getUserId());
		if(reset) {
			setShowModal(true);
		    modalMessage = "La Contraseña del Usuario Fué Restablecida con Éxito. Verificar el Correo Electrónico.";
		} else {
			modalMessage= "Error en Transacción.";
		}
		setReset(true);
		return null;
	}
	
	/**
	 * Control Modal Panel.
	 */
	public void hideModal() {
		this.setShowModal(false);
		this.setModalMessage(null);
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
	 * @param listOnlineUser the listOnlineUser to set
	 */
	public void setOnlineUsersList(List<TbSystemUser> onlineUsersList) {
		this.onlineUsersList = onlineUsersList;
	}

	/**
	 * @param listUsersClients the listUsersClients to set
	 */
	public void setUsersClientsList(List<TbSystemUser> usersClientsList) {
		this.usersClientsList = usersClientsList;
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
	 * @return the remove
	 */
	public boolean isRemove() {
		return remove;
	}

	/**
	 * @param remove the remove to set
	 */
	public void setRemove(boolean remove) {
		this.remove = remove;
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
	 * @param reset the reset to set
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
	 * @param modalMessage the modalMessage to set
	 */
	public void setModalMessage(String modalMessage) {
		this.modalMessage = modalMessage;
	}

	/**
	 * @return the modalMessage
	 */
	public String getModalMessage() {
		return modalMessage;
	}
	
	
	/**
	 * @param showModal the showModal to set
	 */
	public void setShowModal(boolean showModal) {
		this.showModal = showModal;
	}

	/**
	 * @return the showModal
	 */
	public boolean isShowModal() {
		return showModal;
	}
	
	/**
	 * @param showEdit the showEdit to set
	 */
	public void setShowEdit(boolean showEdit) {
		this.showEdit = showEdit;
	}

	/**
	 * @return the showEdit
	 */
	public boolean isShowEdit() {
		return showEdit;
	}

	/**
	 * @param deleteUsersList the deleteUsersList to set
	 */
	public void setDeleteUsersList(List<TbSystemUser> deleteUsersList) {
		this.deleteUsersList = deleteUsersList;
	}

	/**
	 * @param enableUsersList the enableUsersList to set
	 */
	public void setEnableUsersList(List<TbSystemUser> enableUsersList) {
		this.enableUsersList = enableUsersList;
	}

}