/**
 * 
 */
package constant;

import java.io.Serializable;

/**
 * Enumeración que representa la tabla TB_FRECUENCY la cual no es administrable
 * @author ln
 *
 */
public enum EFrequency implements Serializable {

	DIARIA(1L),
	QUINCENAL(2L),
	MENSUAL(3L),
	TRIMESTRAL(5L),
	SEMESTRAL(6L),
	ANUAL(7L),
	HORA(8L),
	SEMANAL(9L),
	;
	
	
	/**
	 * Identificador de la frecuencia
	 */
	private Long id;

	private EFrequency(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public static EFrequency getFrequency(Long id){
		EFrequency ef = null;
		
		if (id == null){
			return ef;
		}
		
		for (EFrequency efi: EFrequency.values()){
			if (efi.getId().longValue() == id.longValue()){
				ef = efi;
				break;
			}
		}
		
		return ef;
	}
}
