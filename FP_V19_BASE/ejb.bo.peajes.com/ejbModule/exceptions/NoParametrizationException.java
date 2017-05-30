/**
 * 
 */
package exceptions;

/**
 * Excepcion que identifica errores de falta de parametrizacion en el sistema
 * @author r.bautista
 *
 */
public class NoParametrizationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public NoParametrizationException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public NoParametrizationException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public NoParametrizationException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public NoParametrizationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

}
