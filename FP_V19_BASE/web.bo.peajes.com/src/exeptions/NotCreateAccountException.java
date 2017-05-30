package exeptions;
/**
 * Excepcion que se lanza cuando no se puede crear una cuenta
 * facilpass
 * @author cparra
 *
 */
public class NotCreateAccountException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Descripcion de la excepcion
	 */
	public final String DESCRIPTION ="No se pudo crear la cuenta Facilpass ";
	
	private String messageError;
	
	public NotCreateAccountException(Long opcion) {
		messageError="";
		if (opcion==null||opcion==-4) {
			messageError =DESCRIPTION;
		}else if (opcion==-2) {
			messageError=DESCRIPTION+"El cliente se encuentra inactivo.";
		}else if (opcion==-3) {
			messageError=DESCRIPTION+"No se encontró el cliente/usuario.";
		}
		
	}
	/**
	 * Retorna el mensaje de error
	 * @return
	 */
	public String getMessageError() {
		return messageError;
	}
	
}
