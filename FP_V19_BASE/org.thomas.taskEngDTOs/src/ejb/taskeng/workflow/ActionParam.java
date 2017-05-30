package ejb.taskeng.workflow;

/**
 * Parametro de una accion. Posee un nombre y un valor.
 * 
 * @author Mauricio Gil
 */
public interface ActionParam extends Cloneable {

	public Object clone();
	
	/**
	 * Establece el nombre de este parametro
	 * @param name
	 */
	public void setName(String name);

	/**
	 * Obtiene el nombre de este parametro
	 * @return
	 */
	public String getName();

	/**
	 * Establece el valor de este parametro
	 * @param value
	 */
	public void setValue(String value);

	/**
	 * Obtiene el valor de este parametro
	 * @return
	 */
	public String getValue();
}