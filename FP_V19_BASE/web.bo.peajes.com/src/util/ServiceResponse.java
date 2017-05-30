package util;

import java.io.Serializable;

/**
 * Clase utilizada para respuestas de servicios.
 * Puede manejar identificadores, mensajes y boolean.
 * @author r.bautista
 *
 */
/**
 * Clase utilizada para respuestas de servicios.
 * Puede manejar identificadores, mensajes y boolean.
 * @author r.bautista
 *
 */
public class ServiceResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Indica si el resultado fue satisfactorio
	 */
	private boolean success;
	
	/**
	 * Identificador de códigos
	 */
	private Integer id;
	
	/**
	 * Mensaje específico
	 */
	private String msj;
	
	/**
	 * Contiene la entidad maneja
	 */
	private Serializable entity;
	
	/**
	 * 
	 */
	public ServiceResponse() {
		this.success = false;
		this.msj = null;
		this.id = null;
		this.entity = null;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMsj() {
		return msj;
	}

	public void setMsj(String msj) {
		this.msj = msj;
	}

	public Serializable getEntity() {
		return entity;
	}

	public void setEntity(Serializable entity) {
		this.entity = entity;
	}

}
