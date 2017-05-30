/**
 * 
 */
package constant;

/**
 * Clase para manejo de constantes en la capa WEB
 * @author r.bautista
 *
 */
public class TPS_Constants {

	/**
	 * Usado cuando se redirige al login y debe mostrar mensaje en la parte superior.
	 */
	public static final String MENSAJE_TXT_LOGIN = "Login_TXT";
	
	
	/**
	 * Usado cuando un elemento no existe
	 */
	public static final int NOT_EXIST = 0;
	/**
	 * Usado cuando un elemento fue usado
	 */
	public static final int USED = 1;
	/**
	 * Usado cuando un elemento expiro
	 */
	public static final int EXPIRED = 2;
	
	/**
	 * Contenido de tag para que un parametro se remplace por vacío
	 */
	public static final Long EMAIL_PARAM_EMPTY = -1L;
}
