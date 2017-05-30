package util.ws.dtos;

import java.io.Serializable;

import jpa.TbWsResponses;

public interface IWsRta extends Serializable {
	/**
	 * Indica si se presentaron problemas de conexi�n. No se logr� consumir el web service.
	 * @return 
	 */
	boolean isConnectionProblems();

	/**
	 * Indica si toda la operaci�n fue exitosa.
	 * @return
	 */
	boolean isSuccessOperation();
	
	/**
	 * Contiene el codigo de resultado de la transacci�n.
	 * @return
	 */
	Long getStatusCode();
	
	/**
	 * Contiene la descripci�n del resultado de la transaccion
	 * @return
	 */
	String getStatusDescription();
	
	/**
	 * Contiene el c�digo y la descripci�n del resultado
	 * @return codigo - descripcion
	 */
	String getDetailedStatusDescription();
	
	/**
	 * Retorna el identificador del error en caso de haber ocurrido
	 * @return identificador del error de {@link TbWsResponses}
	 */
	Long getErrorId();
}
