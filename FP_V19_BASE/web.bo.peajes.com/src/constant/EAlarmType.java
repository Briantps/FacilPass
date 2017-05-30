package constant;

import java.io.Serializable;

/**
 * Enumeracion que representa los tipos de alarma. 
 * No representa tabla pero se usa en la tabla TB_ALARM_BALANCE.ID_TIP_ALARM
 * cualquier nuevo tipo se debe añadir 
 * @author r.bautista
 *
 */
public enum EAlarmType implements Serializable {

	MONEY(4L),
	STEPS(5L),
	;

	private Long id;

	private EAlarmType(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}
	
}
