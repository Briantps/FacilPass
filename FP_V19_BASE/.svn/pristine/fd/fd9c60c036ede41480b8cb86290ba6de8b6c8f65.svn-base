package ejb.taskeng.workflow;

import java.io.Serializable;

import jpa.TbTask;

/**
 * Represents an action to execute on a workflow. 
 * @author Mauricio Gil
 */
public interface ActionCommand extends Serializable {
	/**
	 * Executes an operation. Is supposed to be implemented
	 * in a class.
	 * @param taskReg TODO
	 * @param task Task object associated to this command
	 * @param data Step object associated to this command
	 * @param params TODO
	 * 
	 * @return Final state of this command after execution
	 */
	public int execute(TbTask taskReg, Task task, Step data, String params);
}
