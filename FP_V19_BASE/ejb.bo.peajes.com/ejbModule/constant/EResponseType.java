/**
 * 
 */
package constant;

import java.io.Serializable;

/**
 * Enumeración que representa la tabla tb_response_type, cada registro de la tabla debe existir en la enum.
 * @author r.bautista 
 *
 */
public enum EResponseType implements Serializable {

	
	URL_USED(4000L),
	CONNECTION_PROBS(4001L),
	;
	
	/**
	 * Identificador del registro
	 */
	private Long id;

	private EResponseType(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}
	
}
