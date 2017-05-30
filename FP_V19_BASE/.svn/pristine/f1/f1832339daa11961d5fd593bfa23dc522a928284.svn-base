package ejb.taskeng.workflow.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

import ejb.taskeng.workflow.Step;
import ejb.taskeng.workflow.Task;

public class TaskBase implements Task, Serializable  {
	private static final long serialVersionUID = -382106258787059318L;
	
	private int id;
	private String name;
	private HashMap<Integer, Step> steps = new HashMap<Integer, Step>();
	private String mailTo;
	private String assignedTo;

	public Object clone(){
		TaskBase obj = new TaskBase();
		obj.setId(this.id);
		obj.setName(this.name);
		obj.setSteps(new HashMap<Integer, Step>(this.steps));
		obj.setMailTo(this.mailTo);
		obj.setAssignedTo(this.assignedTo);
		return obj;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append('{');
		sb.append("ID=" + id);
		sb.append(";Name=" + name);
		sb.append(";mailTo="+mailTo);
		sb.append(";assignedTo="+assignedTo);
		sb.append(";steps=" + steps);
		sb.append('}');
		return sb.toString();
	}
	
	/**
	 * Obtiene el primer paso del mapa de pasos.
	 * 
	 * @return Si el mapa de pasos no contiene elementos, retorna null.
	 * En caso contrario obtiene el primer elemento. 
	 */
	public Step getFirst(){
		Collection<Step> values = steps.values();
		if(values.size() > 0)
			return values.iterator().next();
		
		return null;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String nameTask) {
		this.name = nameTask;
	}

	public HashMap<Integer, Step> getSteps() {
		return steps;
	}

	public void setSteps(HashMap<Integer, Step> steps) {
		this.steps = steps;
	}

	/**
	 * Agrega un paso a esta tarea. El identificador se 
	 * determina contando el numero de pasos que hay mas uno, luego
	 * se agrega el paso al mapa de pasos.
	 * @param s
	 */
	public void addStep(Step s){
		int id = steps.size() + 1;
		s.setId(id);
		steps.put(id, s);
	}

	public void setMailTo(String mailTo) {
		this.mailTo = mailTo;
	}

	public String getMailTo() {
		return mailTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public String getAssignedTo() {
		return assignedTo;
	}
	
}