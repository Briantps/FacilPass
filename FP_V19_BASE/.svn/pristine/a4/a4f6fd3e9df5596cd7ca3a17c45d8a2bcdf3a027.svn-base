package ejb.taskeng.workflow;

import java.util.HashMap;

/**
 * Representa una tarea del motor de tareas. 
 * @author Mauricio Gil
 */
public interface Task extends Cloneable {

	public Object clone();
	
	/**
	 * Establece el identificador de esta tarea.
	 * @param type
	 */
	public void setId(int id);

	/**
	 * Obtiene el identificador de esta tarea.
	 * @return
	 */
	public int getId();

	/**
	 * Obtiene el nombre de esta tarea.
	 * @return
	 */
	public String getName();

	/**
	 * Establece el nombre de esta tarea
	 * @param nameTask
	 */
	public void setName(String nameTask);

	/**
	 * Obtiene el mapa de pasos de esta tarea.
	 * @return
	 */
	public HashMap<Integer, Step> getSteps();

	/**
	 * Establece el mapa de pasos para esta tarea.
	 * @param steps
	 */
	public void setSteps(HashMap<Integer, Step> steps);

	/**
	 * Agrega un paso a esta tarea.
	 * @param s
	 */
	public void addStep(Step s);

	/**
	 * Establece la lista separada por comas de los correos
	 * a los que se les envia notificacion de ejecucion
	 * @param mailTo
	 */
	public void setMailTo(String mailTo);

	/**
	 * Obtiene la lista separada por comas de los correos
	 * a los que se les envia notificacion de ejecucion
	 * @return
	 */
	public String getMailTo();

	/**
	 * Establece la lista separada por comas de los posibles
	 * usuarios a los que se les puede asignar esta tarea.
	 * @param assignedTo
	 */
	public void setAssignedTo(String assignedTo);

	/**
	 * Obtiene la lista separada por comas de los posibles
	 * usuarios a los que se les puede asignar esta tarea.
	 * @return
	 */
	public String getAssignedTo();

	/**
	 * Obtiene el primer paso del mapa de pasos.
	 * @return
	 */
	public Step getFirst();
	
}
