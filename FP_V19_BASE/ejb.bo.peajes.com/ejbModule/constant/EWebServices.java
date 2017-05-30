package constant;

import java.io.Serializable;

/**
 * Enumeracion que representa la tabla TB_WEB_SERVICES. Todo registro debe ser adicionado a esta
 * @author r.bautista
 *
 */
public enum EWebServices implements Serializable{

	WS_CREATE_TRX_AVAL(1L, 100L, 1L),  // CU_A03
	WS_CONSULT_TRX_AVAL(2L, 100L, 1L),  // CU_A04
	;
	
	/**
	 * Identificador del web service
	 */
	private Long id;

	/**
	 * Contiene el código del error general para el web service
	 */
	private Long generalErrorCode;
	
	/**
	 * Contiene el código del error por timeout
	 */
	private Long timeOutCode;
	
	/**
	 * Constructor
	 * 
	 * @param id identificador del servicio
	 * @param errorCode codigo para error general
	 * @param timeCode codigo para error por timeout
	 */
	private EWebServices(Long id, Long errorCode, Long timeCode) {
		this.id = id;
		this.generalErrorCode = errorCode;
		this.timeOutCode = timeCode;
	}

	/**
	 * Retorna el id del web-service
	 * @return
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * Retorna el codigo del error general del web-service
	 * @return generalErrorCode
	 */
	public Long getGeneralErrorCode() {
		return this.generalErrorCode;
	}

	/**
	 * Retorna el código de error por timeout
	 * @return timeOutCode
	 */
	public Long getTimeOutCode() {
		return this.timeOutCode;
	}

}
