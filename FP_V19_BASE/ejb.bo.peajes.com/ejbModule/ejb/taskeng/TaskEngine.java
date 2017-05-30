package ejb.taskeng;

import java.util.List;

import javax.ejb.Remote;

/**
 * BackOffice's Task Engine interface.
 *  * 
 * @author Mauricio Gil
 * @see ejb.taskeng.TaskEngineEJB
 */
@Remote
public interface TaskEngine {

	/**
	 * Retrieves a user id with the less quantity of assigned tasks.
	 * 
	 * @param assignedUsers List of user ids
	 * @return User id
	 */
	public long loadBalancer(List<Long> assignedUsers);

	public boolean execute(long procId, String taskName, String params);

	public boolean execute(long procId, int taskId, String params);

	/**
	 * Creates internal queries to database and initializes task
	 * executor's timer to execute tasks found in database.
	 */
	public void init();
}