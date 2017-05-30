/**
 * 
 */
package mBeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import jpa.TbRole;
import jpa.TbTypeRole;

import ejb.Role;

/**
 * @author cavellaneda
 *
 */
public class AdminRoleMBean implements Serializable{
	private static final long serialVersionUID = 5371504689499648558L;

	// Attributes --------------------------------------------------------------------------
	
	@EJB(mappedName="ejb/Role")
	private Role roleEJB;
	
	private List<jpa.TbRole> listRoles;
	
	private List<SelectItem> typeRoleList;
	private Long typeRoleId;
	
	private Long roleId;
	private String roleName;
	
	private String msg;
	private boolean create;
	private boolean error;
	private boolean modificate;
	private boolean update;
	
	// Constructor ------------------------------------------------------------------------
	
	public AdminRoleMBean() {
		listRoles = new ArrayList<jpa.TbRole>();	
	}
	
	// Actions ---------------------------------------------------------------------------
	
	/**
	 * Save a role.
	 */
	public String saveRole(){
		if((roleName.equals(null)) || (roleName.equals("")))
		{
			setMsg("El nombre de rol es requerido.");
			setError(true);			
		}
		else if(!roleName.matches("([a-z]|[.]|[A-Z]|[·ÈÌÛ˙]|[¡…Õ”⁄]|\\s)+")){
			setMsg("El nombre de rol tiene caracteres inv·lidos, por favor verifique.");
			setError(true);
		}
		else if(roleName.trim().length() < 3)
		{
			setMsg("El nombre de rol debe contener mÌnimo 3 caracteres.");
			setError(true);
		}else if (typeRoleId.longValue() == -1L) 
		{	
			setMsg("El tipo rol es requerido.");
			setError(true);
		}
		else if(roleEJB.existRoleI(roleName)){
			setMsg("Ya existe un rol con el mismo nombre.");
			setError(true);
		}
		else{
			if(roleEJB.createRole(roleName, typeRoleId)){
				//setMsg("El Rol \"" + roleName + "\"  ha sido creado con Èxito.");
				setMsg("El Rol ha sido creado con Èxito.");
				clearVar();
				setCreate(true);
			}else{
				setMsg("Ha superado la cantidad de roles parametrizada, comunÌquese con el Administrador.");
				setError(true);
			}
		}
		
		return null;
	}
	
	
	/**
	 * Modifies a Role.
	 */
	public String changeRole(){
		setModificate(false);
		for (TbRole role : listRoles) {
			if(roleId.longValue()  == role.getRoleId().longValue()){
				this.setRoleName(role.getRoleName());
				if (typeRoleId != null) {
					if (!typeRoleId.equals("")) {
						this.setTypeRoleId(role.getTbTypeRole().getTypeRoleId());
					}
				}
				this.update = true;
			}
		}
		return null;
	}
	
	/**
	 * Updates a Role.
	 */
	public String updateRole(){
		this.update = false;
		if((roleName.equals(null)) || (roleName.equals("")))
		{
			setMsg("El nombre de rol es requerido.");
			setError(true);			
		}
		else if(!roleName.matches("([a-z]|[.]|[A-Z]|[·ÈÌÛ˙]|[¡…Õ”⁄]|\\s)+")){
			setMsg("El nombre de rol tiene caracteres inv·lidos, por favor verifique.");
			setError(true);
		}
		else if(roleName.trim().length() < 3)
		{
			setMsg("El nombre de rol debe contener mÌnimo 3 caracteres.");
			setError(true);
		}
		else if(roleEJB.existRoleU(roleId,roleName)){
			setMsg("Ya existe un rol con el mismo nombre.");
			setError(true);
		}
		else{
			if(roleEJB.updateRole(roleId, roleName, typeRoleId)){
				setMsg("El Rol \""+ roleName +"\" ha sido Modificado con Èxito.");
				setModificate(true);
			}else{
				setMsg("Se ha producido un error durante la TransacciÛn del Rol, comunÌquese con el Administrador.");
				setError(true);
			}
		}
		return null;
	}
	
	/**
	 * Clear variables.
	 */
	private void clearVar() {
		typeRoleId = -1L;
		roleName = "";	
	}

	/**
	 * Control modal.
	 */
	public String hideModal() {
		setCreate(false);
		setError(false);
		setModificate(false);
		setUpdate(false);
		this.setRoleName("");
		roleId=0L;
		return null;
	}

	// Getters and Setters -------------------------------------------------------------

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
	 * @param create the create to set
	 */
	public void setCreate(boolean create) {
		this.create = create;
	}

	/**
	 * @return the create
	 */
	public boolean isCreate() {
		return create;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(boolean error) {
		this.error = error;
	}

	/**
	 * @return the error
	 */
	public boolean isError() {
		return error;
	}

	/**
	 * @param listRoles the listRoles to set
	 */
	public void setListRoles(List<jpa.TbRole> listRoles) {
		this.listRoles = listRoles;
	}

	/**
	 * @return the listRoles
	 */
	public List<jpa.TbRole> getListRoles() {
		listRoles = (List<jpa.TbRole>)roleEJB.getAllRoles();
		return listRoles;
	}

	/**
	 * @param roleId the roleId to set
	 */
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	/**
	 * @return the roleId
	 */
	public Long getRoleId() {
		return roleId;
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
	 * @param roleName the roleName to set
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/**
	 * @return the roleName
	 */
	public String getRoleName() {
		return roleName;
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
	 * @param roleList the roleList to set
	 */
	public void setTypeRoleList(List<SelectItem> typeRoleList) {
		this.typeRoleList = typeRoleList;
	}

	/**
	 * @return the roleList
	 */
	public List<SelectItem> getTypeRoleList() {
		if(typeRoleList == null){
			typeRoleList = new ArrayList<SelectItem>();
		}else{
			typeRoleList.clear();
		}
		for(TbTypeRole typeRole : roleEJB.getTypeRoleList()){
			typeRoleList.add(new SelectItem(typeRole.getTypeRoleId(), typeRole.getTypeRoleDescription()));
		}
		return typeRoleList;
	}

	/**
	 * @param typeRoleId the typeRoleId to set
	 */
	public void setTypeRoleId(Long typeRoleId) {
		this.typeRoleId = typeRoleId;
	}

	/**
	 * @return the typeRoleId
	 */
	public Long getTypeRoleId() {
		return typeRoleId;
	}
	
}