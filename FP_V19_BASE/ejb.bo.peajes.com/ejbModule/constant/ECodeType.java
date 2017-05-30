/**
 * 
 */
package constant;

import java.io.Serializable;

/**
 * Enumeración que representa la tabla tb_code_type
 * @author TPS-r.bautista
 *
 */
public enum ECodeType implements Serializable {

	CEDULA_CIUDADANIA(1L),
	CEDULA_EXTRANJERIA(2L),
	NIT(3L),
	TARJETA_IDENTIDAD(4L),
	PASAPORTE(5L),
	NIT_EXTRANJERIA(6L),
	SOCIEDAD_SIN_NIT(7L),
	NIT_PERSONA_NAT(8L),
	FIDEICOMISO(9L)
	;

	/**
	 * Identificador del tipo de documento
	 */
	private Long id;

	private ECodeType(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}

	/**
	 * Retorna el ECodeType asociado al identificador indicado
	 * @param id identificador del ECodeType a retornar
	 * @return ECodeType con el id indicado o null
	 */
	public static ECodeType getECodeType(Long id){
		ECodeType resp = null ;
		if (id != null){
			for (ECodeType ec : ECodeType.values()){
				if (ec.getId().longValue() == id.longValue()){
					resp = ec;
					break;
				}
			}
		}
		
		return resp;
	}
}
