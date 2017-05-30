package ejb.taskeng.workflow.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;

import ejb.taskeng.workflow.Task;
import ejb.taskeng.workflow.TaskDefinition;

/**
 * Implementacion de TaskDefinition. El mapa de tareas esta implementado
 * utilizando la clase LinkedHashMap, que mantiene una asociacion de 
 * identificador - tarea, sin aplicar ordenamiento.
 * 
 * Esto se hizo asi para mantener el orden de almacenamiento de las tareas
 * igual al que tienen las tareas en los archivos xml (por ahora uno que es
 * default.xml). 
 * 
 * @author Mauricio Gil
 */
public class TaskDefinitionBase implements TaskDefinition, Serializable {
	private static final long serialVersionUID = -575047918485541188L;
	private LinkedHashMap<Integer, Task> tasks = new LinkedHashMap<Integer, Task>();
	
	public TaskDefinitionBase(){
	}
	
	public Object clone(){
		TaskDefinition obj = new TaskDefinitionBase();
		obj.setTasks(new LinkedHashMap<Integer, Task>(this.tasks));
		return obj;
	}
	
	public Task getFirst(){
		if(tasks.size() > 0){
			Collection<Task> values = tasks.values();
			return values.iterator().next();
		}
		
		return null;
	}
	
	public LinkedHashMap<Integer, Task> getTasks(){
		return tasks;
	}

	public void setTasks(LinkedHashMap<Integer, Task> tasks){
		this.tasks = tasks;
	}
	
	public void addTask(Task t){
		tasks.put(t.getId(), t);
	}
	
	public Task getTask(int id){
		return tasks.get(id);
	}
}
