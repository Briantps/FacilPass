/**
 * 
 */
package process.device;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import javax.ejb.EJB;

import ejb.crud.Point;

import jpa.TbPoint;

import sessionVar.SessionUtil;
import util.device.ServiceCommand;

/**
 * Bean to manage card balance consult. 
 * @author tmolina
 */
public class ConsultCard implements Serializable {
	private static final long serialVersionUID = -8409839888116140163L;
	
	// Attributes
	
	@EJB(mappedName="ejb/Point")
	private Point point;
	
	private String idDevice;
	
	private Long balance;
	
	private List<String> loga;
	
	private List<String> logc;
	
	private TbPoint tbPoint;
	
	private ServiceCommand command;
	
	private boolean showData;

	/**
	 * Constructor.
	 */
	public ConsultCard() {
		loga = new ArrayList<String>();
		logc = new ArrayList<String>();
	}
	
	// Actions ----------------- //
	
	/**
	 * Get balance of card
	 */
	public String getBalanceDevice(){
		String response;
		StringTokenizer stringTokenizer = null;
		command = new ServiceCommand(tbPoint.getPointIp(), tbPoint.getPointPort().toString());
		try {
			response = command.sendCommand("1");
			if (response.contains("*") || response.equals("") || response == null) {
				processRequest(response);
				idDevice = null;
				balance = null;
			} else {
				stringTokenizer = new StringTokenizer(response);
				idDevice = (stringTokenizer.nextToken(";"));
				
				String b = stringTokenizer.nextToken(";");
				try{
					balance = Long.parseLong(b);
					command.sendPrintBalance(getCurrentTime(), balance.toString(), idDevice);
				}catch (Exception e){	
					balance = null;
				}
				logc.add("["+getCurrentTime()+"] " +  "Se ha leído el saldo de la Tarjeta ID: " + idDevice);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			command.sendCommand("0");
		}
		return null;
	}
	
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
	
	// Getter and Setter ----------------- // 

	/**
	 * @param idDevice the idDevice to set
	 */
	public void setIdDevice(String idDevice) {
		this.idDevice = idDevice;
	}

	/**
	 * @return the idDevice
	 */
	public String getIdDevice() {
		return idDevice;
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
	 * @param log the log to set
	 */
	public void setLoga(List<String> loga) {
		this.loga = loga;
	}

	/**
	 * @return the log
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
	 * @param showData the showData to set
	 */
	public void setShowData(boolean showData) {
		this.showData = showData;
	}

	/**
	 * @return the showData
	 */
	public boolean isShowData() {
		//System.out.println("ip: "+SessionUtil.ip());
		
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
