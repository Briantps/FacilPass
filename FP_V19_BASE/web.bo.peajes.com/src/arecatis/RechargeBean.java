/**
 * 
 */
package arecatis;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import javax.ejb.EJB;

import jpa.TbPoint;

import sessionVar.SessionUtil;
import util.device.ServiceCommand;
import util.device.ToRecharge;

import ejb.Purchase;
import ejb.crud.Point;

/**
 * Bean to manage the recharge. 
 * @author tmolina
 *
 */
public class RechargeBean implements Serializable {
	private static final long serialVersionUID = 3665632480250207073L;
	
	@EJB(mappedName="ejb/Purchase")
	private Purchase purchase;
	
	@EJB(mappedName="ejb/Point")
	private Point point;
	
	// Attributes ---------- //
	
	private List<ToRecharge> rechargeList;
	
	private List<String> loga;
	
	private List<String> logc;
	
	private Long detailId;
	
	private String deviceCode;
	
	private Long balance;
	
	// ----
	
	private ServiceCommand command;
	
	private TbPoint tbPoint;
	
	private boolean showData;
	
	/**
	 * Constructor.
	 */
	public RechargeBean() {
		loga = new ArrayList<String>();
		logc = new ArrayList<String>();
	}
	
	// Actions ------------------- //
	
	/**
	 * 
	 * Clears Communication Log.
	 */
	public String clearLogC(){
		logc.clear();
		return null;
	}
	
	/**
	 * 
	 * Clears Application Log.
	 */
	public String clearLogA(){
		loga.clear();
		return null;
	}
	
	/**
	 * Doing the recharge.
	 * @return
	 */
	public String recharge(){
		String response;
		StringTokenizer stringTokenizer = null;
		command = new ServiceCommand(tbPoint.getPointIp(), tbPoint.getPointPort().toString());
		try {
			response = command.sendCommand("1");
			if (response.contains("*")) {
				processRequest(response);
				deviceCode = null;
				balance = null;
			} else {
				stringTokenizer = new StringTokenizer(response);
				deviceCode = (stringTokenizer.nextToken(";"));
				
				String b = stringTokenizer.nextToken(";");
				try {
					balance = Long.parseLong(b);
					
					ToRecharge detail = null;
					
					for(ToRecharge tr : rechargeList){
						if (detailId.longValue() == tr.getDetail()
								.getPurchaseOrderDetailId().longValue()) {
							detail = tr;
						}
					}
					
					if (detail != null) {
						
						// Validate that the code of the card that's on the reader is the same of the detail that's going to be recharge.
						if(detail.getDetail().getTbDevice().getDeviceCode().equals(deviceCode)){
							
							//Doing the recharge.
							String confirmation = command.sendReload("2", deviceCode,
									detail.getDetail().getDetailOperationValue().toString());
							
							if(!confirmation.equals("-2")){
								
								logc.add("["+getCurrentTime()+"] " +  "Recarga de Tarjeta: OK");
								
								System.out.println("[ Response before Print -> "+confirmation + " ]");
								
								command.sendPrint("1", getCurrentTime().toUpperCase(), null, null,
										balance.toString(), detail.getDetail().getDetailOperationValue().toString(),
										Long.toString(balance + detail.getDetail().getDetailOperationValue().longValue()),
									    deviceCode, detail.getDetail().getTbDevice().getDeviceFacialId());
								
								if (purchase.recharge(detail, balance, balance + detail.getDetail()
												.getDetailOperationValue().longValue(), SessionUtil.ip(),
										SessionUtil.sessionUser().getUserId())) {
									loga.add("["+getCurrentTime()+"] " +  "Se ha registrado la transacción Exitasamente.");
								} else {
									loga.add("["+getCurrentTime()+"] " +  "No se pudo registrar la transacción. Comuníquese con servicio al Cliente.");
								}
								
							} else{
								logc.add("["+getCurrentTime()+"] " +  "No se pudo recargar la tarjeta, Intente Nuevamente.");
							}
						} else{
							logc.add("["+getCurrentTime()+"] " +  "La Tarjeta en el lector no corresponde al Item Selccionado a ser Recargado, Verifique, por favor.");
						}
					}
				}catch (Exception e){	
					balance = null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			command.sendCommand("0");
		}
		return null;
	}
	
	/**
	 * Gets current time, String format
	 * @return
	 */
	private String getCurrentTime(){
		try {
			SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			return f.format(new Date());
		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * 
	 * @param response
	 */
	private void processRequest(String response) {
		if (response.contains("TIMEOUT")) {
			logc.add("["+getCurrentTime()+"] " +  "No se ha detectado tarjeta en el lector.");
		} else if (response.contains("TRANSACTIONLOCKED")) {
			logc.add("["+getCurrentTime()+"] " +  "No es posible realizar lectura de la tarjeta.");
		} else if (response.contains("ERR")) {
			logc.add("["+getCurrentTime()+"] " +  "No es posible realizar la transacción.");
		} else if (response.contains("SALDOMAX")) {
			logc.add("["+getCurrentTime()+"] " +  "No es posible realizar la recarga porque excede el valor máximo de recarga.");
		} else if (response.contains("NOACCESS")) {
			logc.add("["+getCurrentTime()+"] " +  "No es posible leer la Tarjeta.");
		} else if (response.contains("VENCIDA")) {
			logc.add("["+getCurrentTime()+"] " +  "La tarjeta se encuentra vencida.");
		} else {
			logc.add("["+getCurrentTime()+"] " +  "No es posible realizar la transacción por favor verifique el Lector.");
		}
	}
	
	// Getters and Setters --------------- //

	/**
	 * @param rechargeList the rechargeList to set
	 */
	public void setRechargeList(List<ToRecharge> rechargeList) {
		this.rechargeList = rechargeList;
	}

	/**
	 * @return the rechargeList
	 */
	public List<ToRecharge> getRechargeList() {
		if (rechargeList == null) {
			rechargeList = new ArrayList<ToRecharge>();
		} else {
			rechargeList.clear();
		}
		rechargeList = purchase.getRechageList();
		return rechargeList;
	}

	/**
	 * @param loga the loga to set
	 */
	public void setLoga(List<String> loga) {
		this.loga = loga;
	}

	/**
	 * @return the loga
	 */
	public List<String> getLoga() {
		return loga;
	}

	/**
	 * @param logc the logc to set
	 */
	public void setLogc(List<String> logc) {
		this.logc = logc;
	}

	/**
	 * @return the logc
	 */
	public List<String> getLogc() {
		return logc;
	}

	/**
	 * @param detailId the detailId to set
	 */
	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}

	/**
	 * @return the detailId
	 */
	public Long getDetailId() {
		return detailId;
	}

	/**
	 * @param balance the balance to set
	 */
	public void setBalance(Long balance) {
		this.balance = balance;
	}

	/**
	 * @return the balance
	 */
	public Long getBalance() {
		return balance;
	}

	/**
	 * @param deviceCode the deviceCode to set
	 */
	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}

	/**
	 * @return the deviceCode
	 */
	public String getDeviceCode() {
		return deviceCode;
	}

	/**
	 * @param showData the showData to set
	 */
	public void setShowData(boolean showData) {
		this.showData = showData;
	}

	/**
	 * @return the showData
	 */
	public boolean isShowData() {
		tbPoint = point.getUserPoint(SessionUtil.sessionUser().getUserId(),
				SessionUtil.ip());
		if(tbPoint != null){
			showData =true;
		} else {
			showData=false;
		}
		return showData;
	}
}