package ejb;

import java.util.List;

import javax.ejb.Remote;

import util.OptionActions;

/**
 * 
 * @author Frances Zucchet
 * 
 */
@Remote
public interface Permission {

	/**
	 * 
	 * @param role
	 * @return List permissions for the role.
	 */
	public List<util.Permission> getPermissionByRole(long role);

	/**
	 * 
	 * @param permission
	 * @param role
	 * @return True if the permissions are properly updated, otherwise false.
	 */
	public boolean updatePermission(List<util.Permission> permission, long role);

	/**
	 * 
	 * @param userId
	 * @return List permissions for user.
	 */
	public List<util.Permission> getPermissionByUser(long userId);

	/**
	 * 
	 * @param userId
	 * @return List Options-Actions
	 */
	public List<OptionActions> getOpctionAction(long userId);
	
	public List<OptionActions> getOpctionActionPrev(String referenceId);
}
