package ejb;

/**
 * @author Frances Zucchet
 */
import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Remote;

import util.UsrRole;

import jpa.TbRole;
import jpa.TbTypeRole;

@Remote
public interface Role {

	/**
	 * 
	 * @param name
	 * @return
	 */
	public boolean createRole(String roleName, Long typeRoleId);

	/**
	 * 
	 * @param roleId
	 * @return
	 */
	public boolean updateRole(Long roleId, String roleName, Long typeRoleId);

	/**
	 * 
	 * @return List of Roles
	 */
	public List<TbRole> getAllRoles();

	/**
	 * 
	 * @param systemUser
	 * @return List userRole for the user.
	 */
	public List<util.UsrRole> getUsrRole(long userId);

	/**
	 * 
	 * @param usrRole
	 * @param userId
	 * @return True if the userRole are properly updated, otherwise false.
	 */
	public boolean updateUserRole(List<util.UsrRole> usrRole, long userId);
	
	/**
	 * 
	 * @param usrRole
	 * @param userId
	 * @return true if there's a role associated with an user or true if
	 *         a role is going to be assigned, otherwise false.
	 */
	public boolean checkIfAnyUserRole(List<util.UsrRole> usrRole, long userId);
	
	/**
	 * 
	 * @param userId
	 * @param roleName
	 * @return true if successful, false otherwise.
	 */
	public boolean createUserRole(Long userId, String roleName);
	
	/**
	 * 
	 * @param userId
	 * @param role
	 * @return
	 */
	public boolean validateUserRole(long userId, long role);
	
	public boolean isClient(Long userId);
	
	public boolean createUserRole(Long userId, long roleId);

	public List<TbRole> getAllRoles2();
	
	public List<TbTypeRole> getTypeRoleList();
	
	public List<TbRole> getTypeRoleClientList();

	public boolean existRoleI(String nameRole);
	
	public boolean existRoleU(Long roleId, String nameRole);
	
	public boolean createUserRoleMaster(long userId, long roleId, String ip, long creationUser);
	
	public BigDecimal getCountRoles();

	public List<util.UsrRole> getCreateUsrRole(long userId);

}
