package util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TaskDef implements Serializable {
	private static final long serialVersionUID = -382106258787059318L;
	
	private String idTask;
	private int type;
	private String nameTask;
	private String mailTo;
	private List<TaskDef> subtasks = new ArrayList<TaskDef>();
	private List<Step> steps = new ArrayList<Step>();

	public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	/**
	 * @return the nameTask
	 */
	public String getNameTask() {
		return nameTask;
	}

	/**
	 * @param nameTask the nameTask to set
	 */
	public void setNameTask(String nameTask) {
		this.nameTask = nameTask;
	}

	public void setSubtasks(List<TaskDef> subtasks) {
		this.subtasks = subtasks;
	}

	public List<TaskDef> getSubtasks() {
		return subtasks;
	}

	/**
	 * @return the steps
	 */
	public List<Step> getSteps() {
		return steps;
	}

	/**
	 * @param steps the steps to set
	 */
	public void setSteps(List<Step> steps) {
		this.steps = steps;
	}

	public void setIdTask(String idTask) {
		this.idTask = idTask;
	}

	public String getIdTask() {
		return idTask;
	}

	public void setMailTo(String mailTo) {
		this.mailTo = mailTo;
	}

	public String getMailTo() {
		return mailTo;
	}
}