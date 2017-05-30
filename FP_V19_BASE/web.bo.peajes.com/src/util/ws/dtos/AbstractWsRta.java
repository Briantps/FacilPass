/**
 * 
 */
package util.ws.dtos;

/**
 * Clase abstracta que representa una respuesta de consumo de Web Service
 * @author r.bautista
 *
 */
public class AbstractWsRta implements IWsRta {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Indica si hubo problemas de conexión
	 */
	private boolean connectProbs;

	/**
	 * Contiene el código de resultado
	 */
	private Long code;

	/**
	 * Contiene la descripción del resultado
	 */
	private String descripcion;

	/**
	 * Indica si toda la operacion fue exitosa
	 */
	private boolean success;
	
	/**
	 * Contiene el identificador del error
	 */
	private Long errorId;

	/**
	 * 
	 */
	public AbstractWsRta() {
		this.success = false;
		this.connectProbs = false;
		this.errorId = null;
	}

	/* (non-Javadoc)
	 * @see util.ws.dtos.IWsRta#isConnectionProblems()
	 */
	@Override
	public boolean isConnectionProblems() {
		return this.connectProbs;
	}

	public void setConnectionProbs(boolean connectionProbs){
		this.connectProbs = connectionProbs;
	}
	
	/* (non-Javadoc)
	 * @see util.ws.dtos.IWsRta#getStatusCode()
	 */
	@Override
	public Long getStatusCode() {
		return this.code;
	}

	public void setStatusCode(Long code){
		this.code = code;
	}
	
	/* (non-Javadoc)
	 * @see util.ws.dtos.IWsRta#getStatusDescription()
	 */
	@Override
	public String getStatusDescription() {
		return this.descripcion;
	}

	public void setDescription(String description){
		this.descripcion = description;
	}
	
	/* (non-Javadoc)
	 * @see util.ws.dtos.IWsRta#getDetailedStatusDescription()
	 */
	@Override
	public String getDetailedStatusDescription() {
		return "" + this.code + " - " + this.descripcion;
	}

	@Override
	public boolean isSuccessOperation() {
		return this.success;
	}

	/**
	 * Sets success
	 * @param success the value to set
	 */
	public void setSuccess(boolean success){
		this.success = success;
	}

	@Override
	public Long getErrorId() {
		return this.errorId;
	}

	/**
	 * Sets the errorId
	 * @param errorId the value to set
	 */
	public void setErrorId(Long errorId){
		this.errorId = errorId;
	}
}
