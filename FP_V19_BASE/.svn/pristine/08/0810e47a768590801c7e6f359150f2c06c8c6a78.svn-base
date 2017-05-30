/**
 * 
 */
package mBeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.naming.Context;
import javax.naming.InitialContext;

import ejb.Permission;
import ejb.Role;

/**
 * @author Cristian Avellaneda
 * 
 */
public class PermissionMBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	// Attributes ----------------------------------------------------------------------------------------------------------

	private List<util.Permission> listPermission;
	
	private List<util.Permission> listPermissionTemp;
	
	private List<SelectItem> listRoles;
	
	private Long role = -1L;
	
	private Context context;
	
	private boolean render;
	
	private boolean result;
	
	private Role roleEjb;
	
	private String msg;
	
	private Permission perm;
	
	private boolean showList;
	
	// Constructor --------------------------------------------------------------------------------------------------------

	public PermissionMBean() {
		try {
			listPermission = new ArrayList<util.Permission>();
			listPermissionTemp = new ArrayList<util.Permission>();
			context = new InitialContext();
			
			roleEjb = (Role) context.lookup("ejb/Role");
			perm = (Permission) context.lookup("ejb/Permission");
			
			listRoles = new ArrayList<SelectItem>();
			List<jpa.TbRole> listRol = new ArrayList<jpa.TbRole>();
			listRol = roleEjb.getAllRoles();
			listRoles.add(new SelectItem("-1", "--Seleccione un Rol--"));
			for (jpa.TbRole roleObj : listRol) {
				listRoles.add(new SelectItem(roleObj.getRoleId(), roleObj
						.getRoleName()));
			}
			setListRoles(listRoles);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Actions ------------------------------------------------------------------------------------------------------------
	
	/**
	 * Loads user permission list.
	 */
	public void loadPermission() {
		System.out.println("role " + role);
		listPermission.clear();
		//role = event.getNewValue().toString();
		if(!role.equals(-1L)){
			listPermission = perm.getPermissionByRole(role);
			listPermissionTemp = perm.getPermissionByRole(role);
			this.setShowList(true);
		}else{
			System.out.println("role es = -1");
			msg = "Seleccione un Rol.";
			this.setResult(true);
		}
	}
	
	public void ocult(){
		this.setShowList(false);
	}

	/**
	 * Updates user permission list.
	 */
	public String viewPermAsig() {	
		
		boolean change = false;
		
		for (int i = 0; i < listPermission.size(); i++){
			util.Permission p = listPermission.get(i);
			if(p.isActive() != listPermissionTemp.get(i).isActive()){
				change = true;
				break;
			}
		}
		
		if(!role.equals(-1L)){
			if(change){
				result = perm.updatePermission(listPermission,role);	
				if(result){
					msg = "Se han guardado los Permisos con Éxito. Para visualizar los cambios debe salir y volver a iniciar sesión";
					listPermission.clear();
					listPermission = perm.getPermissionByRole(role);
					listPermissionTemp = perm.getPermissionByRole(role);
				}else{
					msg = "Ha ocurrido un error, comuníquese con el Administrador.";
				}
			}else{
				msg = "No ha realizado cambios. No se realizó la transacción.";
			}
		}else{
			msg = "Seleccione un Rol.";
		}
		setResult(true);
		return null;
	}
	
	/**
	 * Control Modal panel.
	 */
	public void hideModal(){
		setResult(false);
		setMsg("");
	}
	
	// Getters and Setters ------------------------------------------------------------------------------------------------

	/**
	 * @return listPermission
	 */
	public List<util.Permission> getListPermission() {
		return listPermission;
	}

	/**
	 * Setter for listPermission
	 * @param listPermission
	 */
	public void setListPermission(List<util.Permission> listPermission) {
		this.listPermission = listPermission;
	}

	/**
	 * Setter for role
	 * @param role
	 */
	public void setRole(Long role) {
		this.role = role;
	}

	/**
	 * @return role
	 */
	public Long getRole() {
		return role;
	}

	/**
	 * @return listRoles
	 */
	public List<SelectItem> getListRoles() {
		return listRoles;
	}

	/**
	 * Setter for listRoles
	 * @param listRoles
	 */
	public void setListRoles(List<SelectItem> listRoles) {
		this.listRoles = listRoles;
	}

	/**
	 * Setter for render
	 * @param render
	 */
	public void setRender(boolean render) {
		this.render = render;
	}

	/**
	 * @return render
	 */
	public boolean isRender() {
		return render;
	}

	/**
	 * Setter for result
	 * @param result
	 */
	public void setResult(boolean result) {
		this.result = result;
	}

	/**
	 * @return result
	 */
	public boolean isResult() {
		return result;
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
	 * @param listPermissionTemp the listPermissionTemp to set
	 */
	public void setListPermissionTemp(List<util.Permission> listPermissionTemp) {
		this.listPermissionTemp = listPermissionTemp;
	}

	/**
	 * @return the listPermissionTemp
	 */
	public List<util.Permission> getListPermissionTemp() {
		return listPermissionTemp;
	}

	/**
	 * @param showList the showList to set
	 */
	public void setShowList(boolean showList) {
		this.showList = showList;
	}

	/**
	 * @return the showList
	 */
	public boolean isShowList() {
		return showList;
	}
}