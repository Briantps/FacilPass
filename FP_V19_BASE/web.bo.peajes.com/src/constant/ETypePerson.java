package constant;

import java.io.Serializable;

/**
 * Enumeraci�n que representa el tipo de persona.
 * A�n no tiene representaci�n en base de datos.
 * @author TPS r.bautista
 *
 */
public enum ETypePerson implements Serializable {

	NATURAL_PERSON(0L, "Natural"),
	JURIDIC_PERSON(1L, "Jur�dica"),
	;
	
	private Long id;
	
	private String nombre;

	private ETypePerson(Long id, String nombre) {
		this.id = id;
		this.nombre = nombre;
	}

	public Long getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	/**
	 * Retorna el {@link ETypePerson} asociado al identificador indicado
	 * @param id identificador del {@link ETypePerson}
	 * @return {@link ETypePerson} asociado al id indicado o null
	 */
	public static ETypePerson getTypePerson(Long id){
		ETypePerson typeP = null;

		if (id != null){
			for(ETypePerson tp : ETypePerson.values()){
				if (tp.getId().longValue() == id.longValue()){
					typeP = tp;
					break;
				}

			}

		}

		return typeP;
	}

}
