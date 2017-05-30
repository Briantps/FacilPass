package util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TaskDef implements Serializable {
	private static final long serialVersionUID = -382106258787059318L;
	
	private int type;
	private String mailTo;
	private String idTask;
	private String nameTask;
	private String dependsOn;
	private String conditionalState;
	private List<Step> steps = new ArrayList<Step>();
	private Map<String, TaskDef> subtasks = new LinkedHashMap<String, TaskDef>();
	
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

	public void setSubtasks(Map<String, TaskDef> subtasks) {
		this.subtasks = subtasks;
	}

	public Map<String, TaskDef> getSubtasks() {
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

	public void setDependsOn(String dependsOn) {
		this.dependsOn = dependsOn;
	}

	public String getDependsOn() {
		return dependsOn;
	}

	public void setConditionalState(String conditionalState) {
		this.conditionalState = conditionalState;
	}

	public String getConditionalState() {
		return conditionalState;
	}
}