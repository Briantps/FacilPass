package ejb.taskeng;

import java.io.File;
import java.util.List;

import javax.ejb.Remote;
import javax.faces.model.SelectItem;

import jpa.TbSystemUser;
import ejb.taskeng.workflow.ActionParam;
import ejb.taskeng.workflow.Step;
import ejb.taskeng.workflow.Task;
import ejb.taskeng.workflow.TaskDefinition;

@Remote
public interface ConfigTaskEng {

	public void storeTaskDef(TaskDefinition td, File f);

	public void addTaskDefinition(TaskDefinition td);

	public List<SelectItem> getTaskFiles();

	public List<TbSystemUser> getUsers(String usrIds);

	public List<SelectItem> getAvailableProcTypes();

	public void setCurrentTask(Task currentTask);

	public Task getCurrentTask();

	public List<SelectItem> getAvailableActions();

	public void setCurrentTaskDefinition(TaskDefinition curTaskDef);

	public TaskDefinition getCurrentTaskDefinition();

	public void setCurTaskStep(Step curTaskStep);

	public Step getCurTaskStep();

	public void setCurrentFile(File currentFile);

	public File getCurrentFile();

	public void setCurStepParam(ActionParam curStepParam);

	public ActionParam getCurStepParam();

	public List<TaskDefinition> getTaskDefinitions();

	public String getTaskTypeName(int id);

}