package ejb.taskeng.workflow.impl;

import java.io.Serializable;

import jpa.TbTask;
import ejb.taskeng.workflow.Task;

public class TaskToExecute implements Serializable{

	private static final long serialVersionUID = -2393580270417009670L;
	
	private TbTask taskReg;
	private Task task;
	private boolean running;
	
	public TaskToExecute(boolean running) {
		this.running = running;
	}

	public TbTask getTaskReg() {
		return taskReg;
	}

	public void setTaskReg(TbTask taskReg) {
		this.taskReg = taskReg;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
}