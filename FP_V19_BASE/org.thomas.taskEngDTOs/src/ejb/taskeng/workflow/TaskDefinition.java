package ejb.taskeng.workflow;

import java.util.LinkedHashMap;

/**
 * Representa una definicion de tareas.
 * 
 * Una definicion de tareas contiene la descripcion de las etiquetas
 * task que se han extraido de un archivo xml, que por defecto es el
 * archivo default.xml en la carpeta domain/config/tasks.
 * 
 * 
 * @author Mauricio Gil
 */
public interface TaskDefinition extends Cloneable {

	public Object clone();
	
	/**
	 * Obtiene el mapa de tareas asociado a esta definicion de tareas.
	 * @return
	 */
	public LinkedHashMap<Integer, Task> getTasks();

	/**
	 * Establece el mapa de tareas asociado a esta definicion de tareas.
	 * @param tasks
	 */
	public void setTasks(LinkedHashMap<Integer, Task> tasks);

	/**
	 * Agrega una tarea al motor de tareas
	 * @param t
	 */
	public void addTask(Task t);

	/**
	 * Obtiene una tarea
	 * @param id Identificador de la tarea
	 * @return
	 */
	public Task getTask(int id);

	/**
	 * Obtiene la primer tarea.
	 * @return
	 */
	public Task getFirst();

}