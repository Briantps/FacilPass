package ejb;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface Action {

	/**
	 * 
	 * @param option Option
	 * @return List of Actions by option
	 */
	public List<util.Permission> getActionsByOption(long option);

	/**
	 * 
	 * @return List of all Actions.
	 */
	public List<jpa.TbAction> getAllActions();

	/**
	 * 
	 * @param actionName Action Name
	 * @return true if the Action is created correctly, otherwise false.
	 */
	public boolean createAction(String actionName);

	/**
	 * 
	 * @param actionId Action Id
	 * @param actionName Action Name
	 * @return True if an Action is properly updated, otherwise false.
	 */
	public boolean updateAction(long actionId, String actionName);

	/**
	 * 
	 * @param permissions
	 * @return True if an OptAct is properly updated, otherwise false.
	 */
	public boolean updateOptAct(List<util.Permission> permissions);

	/**
	 * 
	 * @param permissions Permissions List.
	 * @param optId Option ID.
	 * @return true if there's an opt-act associated with the option or true if
	 *         an opt-act is going to be assigned, otherwise false.
	 */
	public boolean checkIfAnyOptAct(List<util.Permission> permissions, long optId);
	
	/**
	 * 
	 * @param idOption
	 * @param idAction
	 * @return String with the roles that are related with an option-action.
	 */
	public String valRoleOptionAction(long idOption, long idAction);
	
	/**
	 * 
	 * @param idOption
	 * @param idAction
	 * @param behavior
	 * @return
	 */
	public String valBehavior(long idOption, long idAction, String behavior);
	
	public boolean existRelation(long idOption, long idAction);
}