package util.ws;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import javax.xml.ws.WebServiceException;

import util.ws.dtos.WsRtaTransactionAval;

import com.corficolombiana.facilpass.ws.connector.commons.dto.StatusDTO;
import com.corficolombiana.facilpass.ws.connector.commons.exception.WSConnectorException;
import com.corficolombiana.facilpass.ws.connector.custlogin.delegate.WSDelegate;
import com.corficolombiana.facilpass.ws.connector.custlogin.dto.RequestDTO;
import com.corficolombiana.facilpass.ws.connector.custlogin.dto.ResponseDTO;

import constant.EWebServices;

/**
 * Clase encargada de consumir WS de aval para crear transaccion autorizacion recarga programada
 * @author r.bautista
 *
 */
public class WSCreateTransactionAval extends AbstractWSClient {

	/**
	 * Elementos para invocación del web-service
	 */
	private WSDelegate del;
	private RequestDTO req;
	private ResponseDTO responseDTO;

	/**
	 * Campos para invocar el servicio
	 */
	// campos por doc
	private Long rqUID;
	private String channel;
	private Date date;
	private String ip;
	private String idType;
	private String idNum;
	private String bankId;
	private String trnType;
	private String acctId;
	private String returnUrl;
	
	/**
	 * Constructor vacío
	 */
	public WSCreateTransactionAval() {
		super();
		this.setWSUrl("");
		this.del = null;
		this.req = null;
		this.setRta(new WsRtaTransactionAval());
	}

	/**
	 * @see util.ws.IWsClient#execute()
	 */
//	public WsRtaTransactionAval execute(){
//		WsRtaTransactionAval rta = new WsRtaTransactionAval();
//		WSDelegate del = null;
//		RequestDTO req = null;
//		ResponseDTO ar;
//		StatusDTO st;
//		Long statCode;
//		TbWsResponses wsResp;
//
//		if (this.getWSUrl() == null) {
//			this.setWSUrl("");
//		}
//		
//		try {
//			del = new WSDelegate(new URL(this.getWSUrl()));
//			req = new RequestDTO(this.rqUID, this.channel, this.date, this.ip, this.idType, 
//					this.idNum, this.bankId, this.trnType, this.acctId, this.returnUrl);
//		} catch (MalformedURLException e1) {
//			e1.printStackTrace();
//			rta.setConnectionProbs(true);
//		} catch(WebServiceException e3){
//			e3.printStackTrace();
//			rta.setConnectionProbs(true);
//		} catch(Exception e4){
//			e4.printStackTrace();
//			rta.setConnectionProbs(true);
//		}
//
//		if (rta.isConnectionProblems()){
//			return (WsRtaTransactionAval)this.createTimeError(rta);
//		}
//
//		try {
//			ar = del.invokeWS(req);
//		} catch (WSConnectorException e1) {
//			e1.printStackTrace();
//			rta.setConnectionProbs(true);
//			return (WsRtaTransactionAval)this.createTimeError(rta);
//		}
//
//		st = ar.getStatus();
//
//		statCode = st.getStatusCode();		
//		wsResp = this.getCods().get(statCode);
//		if (wsResp == null) {
//			System.out.println("Código recibido desconocido - " + statCode);
//			wsResp = this.getCods().get(EWebServices.WS_CREATE_TRX_AVAL.getGeneralErrorCode());
//		}
//
//		if (!wsResp.isValid()) {
//			rta.setStatusCode(wsResp.getCode());
//			rta.setDescription(wsResp.getDescription());
//			return rta;
//		}
//
//		// Resultado satisfactorio
//		rta.setStatusCode(wsResp.getCode());
//		rta.setSuccess(true);
//		// TODO Se debe preguntar si el RqUID() es el transactionID a guardar o qué campo es??? 
//		rta.setTransactId(ar.getRqUID());
//		rta.setBankUrl(ar.getUrl());
//		return rta;
//	}


	@Override
	protected Long getTimeOutErrorCode() {
		return EWebServices.WS_CREATE_TRX_AVAL.getTimeOutCode();
	}

	@Override
	protected void createRtaFromRequest() {
		
		try {
			this.del = new WSDelegate(new URL(this.getWSUrl()));
			this.req = new RequestDTO(this.rqUID, this.channel, this.date, this.ip, this.idType, 
					this.idNum, this.bankId, this.trnType, this.acctId, this.returnUrl);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
			this.getRta().setConnectionProbs(true);
		} catch(WebServiceException e3){
			e3.printStackTrace();
			this.getRta().setConnectionProbs(true);
		} catch(Exception e4){
			e4.printStackTrace();
			this.getRta().setConnectionProbs(true);
		}

	}

	@Override
	protected void invokeService() {

		try {
			this.responseDTO = this.del.invokeWS(req);
		} catch (WSConnectorException e1) {
			e1.printStackTrace();
			this.getRta().setConnectionProbs(true);
			this.createTimeError();
		}

	}

	@Override
	protected StatusDTO getResultStatus() {
		return this.responseDTO.getStatus();
	}

	@Override
	protected void setFinalResp() {
		WsRtaTransactionAval tRta = (WsRtaTransactionAval)this.getRta();
		// TODO Se debe preguntar si el RqUID() es el transactionID a guardar o qué campo es??? 
		tRta.setTransactId(this.responseDTO.getRqUID());
		tRta.setBankUrl(this.responseDTO.getUrl());
	}

	@Override
	protected Long getGeneralErrorCode() {
		return EWebServices.WS_CREATE_TRX_AVAL.getGeneralErrorCode();
	}

	public Long getRqUID() {
		return rqUID;
	}

	public void setRqUID(Long rqUID) {
		this.rqUID = rqUID;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getIdNum() {
		return idNum;
	}

	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getTrnType() {
		return trnType;
	}

	public void setTrnType(String trnType) {
		this.trnType = trnType;
	}

	public String getAcctId() {
		return acctId;
	}

	public void setAcctId(String acctId) {
		this.acctId = acctId;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

}
