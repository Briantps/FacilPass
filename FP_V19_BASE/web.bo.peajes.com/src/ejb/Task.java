/**
 * 
 */
package ejb;

import java.sql.Timestamp;

import javax.ejb.Remote;

/**
 * @author tmolina
 *
 */
@Remote
public interface Task {
	
	/**
	 * Creates a Task.
	 * @param idTask
	 * @param taskState
	 * @param taskCreateDate
	 * @param taskAdsDate
	 * @param taskPriority
	 * @param taskData
	 * @param tbTaskTypeId
	 * @param creationUser
	 * @param ip
	 * @param referencedId
	 * @return <code>true</code> if successful, <code>false</code> otherwise.
	 */
	public boolean createTask(Long idTask, Integer taskState,
			Timestamp taskCreateDate, Timestamp taskAdsDate,
			Integer taskPriority, String taskData, Long tbTaskTypeId,
			Long creationUser, String ip, Long referencedId);
}