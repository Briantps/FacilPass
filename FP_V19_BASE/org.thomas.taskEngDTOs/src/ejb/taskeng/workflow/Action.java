package ejb.taskeng.workflow;

import java.util.ArrayList;

/**
 * Representa una accion para realizar en un paso de ejecucion
 * @author Mauricio Gil
 */
public interface Action extends Cloneable {

	public Object clone();
	
	/**
	 * Establece la lista de parametros que afectan esta accion.
	 * @param params List of ActionParams
	 */
	public void setParams(ArrayList<ActionParam> params);

	/**
	 * Obtiene la lista de parametros asociada a esta accion
	 * @return List of ActionParams
	 */
	public ArrayList<ActionParam> getParams();

	/**
	 * Agrega un parametro a esta accion.
	 * @param param
	 */
	public void addParam(ActionParam param);

	/**
	 * Establece el nombre de esta accion.
	 * @param name
	 */
	public void setName(String name);

	/**
	 * Obtiene el nombre de esta accion. 
	 * @return
	 */
	public String getName();

	/**
	 * Obtiene el numero de parametros que contiene esta accion.
	 * @return
	 */
	public int getParamCount();

	/**
	 * Obtiene el primer parametro de esta accion
	 * @return
	 */
	public ActionParam getFirst();

}