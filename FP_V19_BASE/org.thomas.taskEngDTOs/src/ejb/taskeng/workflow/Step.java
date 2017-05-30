package ejb.taskeng.workflow;

import java.util.ArrayList;

/**
 * Representa un paso de ejecucion en una tarea
 * @author Mauricio Gil
 */
public interface Step extends Cloneable {

	public Object clone();
	
	/**
	 * Establece el nombre de este paso
	 * @param name
	 */
	public void setName(String name);

	/**
	 * Obtiene el nombre de este paso
	 * @return
	 */
	public String getName();

	/**
	 * Establece el identificador de este paso 
	 * @param id
	 */
	public void setId(int id);

	/**
	 * Obtiene el identificador de este paso
	 * @return
	 */
	public int getId();

	/**
	 * Establece la lista de acciones asociada a este paso
	 * @param action
	 */
	public void setActions(ArrayList<Action> action);

	/**
	 * Obtiene la lista de acciones asociada a este paso
	 * @return
	 */
	public ArrayList<Action> getActions();

	/**
	 * Obtiene el estado inicial de este paso
	 * @return
	 */
	public int getStateInit();

	/**
	 * Establece el estado inicial de este paso
	 * @param stateInit
	 */
	public void setStateInit(int stateInit);

	/**
	 * Obtiene el numero de acciones que realiza este paso
	 * @return
	 */
	public int getActionCount();

	/**
	 * Obtiene la primer accion que realiza este paso.
	 * @return
	 */
	public Action getFirst();

	/**
	 * Agrega una accion a este paso.
	 * @param action
	 */
	public void addAction(Action action);
}