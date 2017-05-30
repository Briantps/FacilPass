/**
 * 
 */
package constant;

/**
 * Enum for LogAction 
 * 
 * @author tmolina
 * @since 01-10-2010
 */
public enum LogAction {
	
	CREATE("Creación"),
	CREATEFAIL("Creación Fallida"),
	UPDATE("Modificación"),
	UPDATEFAIL("Modificación Fallida"),
	DELETE("Borrado"),
	DELETEFAIL("Borrado Fallido"),
	CONSULT("Consulta"),
	CONSULTFAIL("Consulta Fallida"),
	ACCESS("Acceso"),
	ACCESSDENIED("Acceso Denegado"),
	NOTFOUND("No encontrado"),
	OPERATIONFAILED("Operación Fallida"),
	ATTEMPT_ENTRY_DEVICE("Intento de Ingresar Dispositivo"),
	RECHARGE_MESSAGE("Mensaje de Recarga"),
	ASSOCIATION ("Asociación"),
    ASSOCIATIONFAIL("Asociación Fallida"),
    DISSOCIATION ("Disociación"),
    DISSOCIATIONFAIL("Disociación Fallida"),
	SEND("Envío de Documentos"),
	ERRORRECHARGEPSE("Error Recarga PSE");


	private String logAction;

	private LogAction(String str) {
		this.logAction = str;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return this.logAction;
	}
}