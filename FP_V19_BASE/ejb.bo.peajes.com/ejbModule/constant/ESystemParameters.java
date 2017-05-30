/**
 * 
 */
package constant;

import java.io.Serializable;

/**
 * Enumeracion que representa los parametros del sistema TB_SYSTEM_PARAMETER
 * Cada registro de la tabla se debe agregar aquí
 * @author r.bautista
 *
 */
public enum ESystemParameters implements Serializable {
	// Inician con V los que tienen valores numericos
	// Inician con M los que tienen valores de textos
	// Inician con H los que son texto tipo hora
	
	V_MIN_ASIG_CUENTA_POST(30L),
	H_HORA_RECARGA(42L),
	V_MIN_RECARGA_PREPAG(71L),
	V_TIEMPO_VIDA_URL_AVAL(77L),
	M_RECARGA_ENTIDAD_AVAL(81L),
	;
	
	/**
	 * Identificador del parametro
	 */
	private Long id;

	private ESystemParameters(Long id) {
		this.id = id;
	}

	/**
	 * 
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	
}
