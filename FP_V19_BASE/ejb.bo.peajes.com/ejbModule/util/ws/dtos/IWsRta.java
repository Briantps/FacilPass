package util.ws.dtos;

import java.io.Serializable;

import jpa.TbWsResponses;

public interface IWsRta extends Serializable {
	/**
	 * Indica si se presentaron problemas de conexión. No se logró consumir el web service.
	 * @return 
	 */
	boolean isConnectionProblems();

	/**
	 * Indica si toda la operación fue exitosa.
	 * @return
	 */
	boolean isSuccessOperation();
	
	/**
	 * Contiene el codigo de resultado de la transacción.
	 * @return
	 */
	Long getStatusCode();
	
	/**
	 * Contiene la descripción del resultado de la transaccion
	 * @return
	 */
	String getStatusDescription();
	
	/**
	 * Contiene el código y la descripción del resultado
	 * @return codigo - descripcion
	 */
	String getDetailedStatusDescription();
	
	/**
	 * Retorna el identificador del error en caso de haber ocurrido
	 * @return identificador del error de {@link TbWsResponses}
	 */
	Long getErrorId();
}
