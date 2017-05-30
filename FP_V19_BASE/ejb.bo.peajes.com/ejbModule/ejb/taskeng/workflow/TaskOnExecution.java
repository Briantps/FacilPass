package ejb.taskeng.workflow;

import javax.ejb.Remote;

import jpa.TbTask;
import ejb.taskeng.workflow.Task;
import ejb.taskeng.workflow.impl.TaskToExecute;

@Remote
public interface TaskOnExecution {

	public void cancelTimer(TaskToExecute tte);

	public void createTimer(TaskToExecute tte);

	public void resetTimer(TaskToExecute tte);

	public Task findTask(TbTask taskReg);
}