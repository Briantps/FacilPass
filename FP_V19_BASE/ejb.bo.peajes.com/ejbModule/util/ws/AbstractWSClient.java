package util.ws;

import java.util.Map;

import jpa.TbWsResponses;
import util.ws.dtos.AbstractWsRta;

import com.corficolombiana.facilpass.ws.connector.commons.dto.StatusDTO;

/**
 * Clase abstracta de un IWSClient
 * @author TPS-r.bautista
 *
 */
public abstract class AbstractWSClient implements IWsClient<AbstractWsRta> {

	/**
	 * URL del web-service para ser invocado
	 */
	private String wsUrl;
	
	/**
	 * Respuesta de ejecución del servicio
	 */
	private AbstractWsRta rta;

	/**
	 * Codigos de respuesta
	 */
	private Map<Long, TbWsResponses> cods;

	/**
	 * @see util.ws.IWsClient#setWSUrl(String)
	 */
	@Override
	public void setWSUrl(String url) {
		this.wsUrl = url;
	}

	/**
	 * @see util.ws.IWsClient#getWSUrl()
	 */
	@Override
	public String getWSUrl(){
		return this.wsUrl;
	}

	@Override
	public AbstractWsRta execute() {
		StatusDTO st;
		Long statCode;
		TbWsResponses wsResp;
		
		if (this.getWSUrl() == null) {
			this.setWSUrl("");
		}
		
		this.createRtaFromRequest();

		if (this.getRta().isConnectionProblems()){
			this.createTimeError();
			return this.getRta();
		}

		this.invokeService();
		if (this.getRta().isConnectionProblems()){
			return rta;
		}

		st = this.getResultStatus();

		statCode = st.getStatusCode();		
		wsResp = this.getCods().get(statCode);
		if (wsResp == null) {
			System.out.println("Código recibido desconocido - " + statCode);
			wsResp = this.getCods().get(this.getGeneralErrorCode());
		}

		if (!wsResp.isValid()) {
			this.getRta().setStatusCode(wsResp.getCode());
			this.getRta().setDescription(wsResp.getDescription());
			return this.getRta();
		}

		// Resultado satisfactorio
		this.getRta().setStatusCode(wsResp.getCode());
		this.getRta().setSuccess(true);
		this.setFinalResp();
		return this.getRta();
	}

	/**
	 * Retorna la AbstractWsRta habiendo creado el request
	 * @return AbstractWsRta posterior a crear el request
	 */
	protected abstract void createRtaFromRequest();

	/**
	 * invocar el servicio y modifica
	 */
	protected abstract void invokeService();

	/**
	 * Retorna el Status de respuesta de invocación del servicio
	 * @return StatusDTO recibido al invocar el servicio
	 */
	protected abstract StatusDTO getResultStatus();

	/**
	 * Retorna la AbstractWsRta cuando es satisfactoria la respuesta
	 * @param rta AbstractWsRta al cual se setean los valores finales
	 * @return AbstractWsRta con resultado satisfactorio
	 */
	protected abstract void setFinalResp();
	
	/**
	 * Retorna la respuesta indicada con código y descripción de timeOut
	 * @param rta respuesta a retornar
	 * @return AbstractWsRta con código de timeOut
	 */
	protected void createTimeError(){
		TbWsResponses wsResp = this.cods.get(this.getTimeOutErrorCode());
		this.getRta().setStatusCode(wsResp.getCode());
		this.getRta().setDescription(wsResp.getDescription());
	}

	/**
	 * Retorna el código de error para TimeOut
	 * @return código de error de timeOut para el web-service
	 */
	protected abstract Long getTimeOutErrorCode();

	/**
	 * Retorna el código de error para error general
	 * @return código de error general para el web-service
	 */
	protected abstract Long getGeneralErrorCode();

	/**
	 * Retorna los códigos de respuesta
	 * @return 
	 */
	public Map<Long, TbWsResponses> getCods() {
		return this.cods;
	}

	/**
	 * Sets los códigos de respuesta
	 * @param cods
	 */
	public void setCods(Map<Long, TbWsResponses> cods) {
		this.cods = cods;
	}

	/**
	 * Retorna la respuesta del servicio
	 * @return
	 */
	protected AbstractWsRta getRta() {
		return this.rta;
	}

	/**
	 * Modifica la respuesta del servicio
	 * @param rta
	 */
	protected void setRta(AbstractWsRta rta) {
		this.rta = rta;
	}

}
