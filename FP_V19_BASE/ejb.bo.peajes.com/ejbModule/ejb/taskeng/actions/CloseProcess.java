package ejb.taskeng.actions;

import javax.ejb.Remote;

import jpa.TbTask;
import ejb.taskeng.workflow.ActionCommand;
import ejb.taskeng.workflow.Step;
import ejb.taskeng.workflow.Task;

@Remote
public interface CloseProcess extends ActionCommand {

	public int execute(TbTask taskReg, Task task, Step data,
			String params);

}