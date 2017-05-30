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

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import ejb.crud.Point;

import jpa.TbDevice;
import jpa.TbDeviceState;
import jpa.TbDeviceType;
import jpa.TbPoint;

import sessionVar.SessionUtil;
import util.device.ServiceCommand;

/**
 * Bean to Manage Card Addition.
 * @author tmolina
 */
public class AddCard implements Serializable {
	private static final long serialVersionUID = -7148175549095352008L;
	
	@EJB(mappedName="ejb/Point")
	private Point point;
	
	@EJB(mappedName="ejb/Device")
	private ejb.Device deviceEJB;

	// Attributes
	
	private boolean showPanelEntryCard;
	
	private TbPoint tbPoint;
	
	// ---- 
	
	private String deviceCode;
	
	private Long balance;
	
	private List<String> loga;
	
	private List<String> logc;
	
	private ServiceCommand command;
	
	private List<SelectItem> devicesTypes;
	
	private Long typeDevId;
	
	private List<SelectItem> deviceStates;
	
	private Long deviceStateId;
	
	private String facial;

	/**
	 * Constructor.
	 */
	public AddCard() {
	}
	
	@PostConstruct
	public void beforInit() {
		init();
	}
	
	/**
	 * 
	 */
	public void init() {
		tbPoint = point.getUserPoint(SessionUtil.sessionUser().getUserId(),
				SessionUtil.ip());
		if (tbPoint != null) {
			setShowPanelEntryCard(true);
		} else {
			setShowPanelEntryCard(false);
		}
		this.setBalance(null);
		this.setLoga(null);
		this.setLogc(null);
		this.setDeviceCode(null);
		this.setDevicesTypes(null);
		this.setDeviceStates(null);
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
		command = new ServiceCommand(tbPoint.getPointIp(), tbPoint
				.getPointPort().toString());
		try {
			response = command.sendCommand("3");
			if (response.contains("*")) {
				processRequest(response);
				deviceCode = null;
				balance = null;
			} else {
				stringTokenizer = new StringTokenizer(response);
				deviceCode = (stringTokenizer.nextToken(";"));
				
				String b = stringTokenizer.nextToken(";");
				
				if((b.equals("null")) || (b.equals(null))){
					b = "0";
				}
				
				try{
					balance = Long.parseLong(b);
				}catch (Exception e){	
					balance = null;
				}
				logc.add("["+getCurrentTime()+"] " +  "Se ha leído la Tarjeta ID: " + deviceCode);
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
	
	/**
	 * 
	 * @return
	 */
	public String save() {
		if (deviceCode != null && !deviceCode.equals("")) {
			if(facial != null && !facial.equals("")){
				TbDevice de = deviceEJB.getDeviceByCode(deviceCode);
				if (de == null) {
					if (deviceEJB.saveDevice(deviceCode, facial, balance,
							typeDevId, deviceStateId,null,null, SessionUtil.ip(),
							SessionUtil.sessionUser().getUserId())) {
						loga.add("["+getCurrentTime()+"] " +  "Se ha guardado la Tarjeta en el Sistema Exitosamente.");
					} else {
						loga.add("["+getCurrentTime()+"] " +  "No se ha podido guardar la Tarjeta en el Sistema, Intente Nuevamente.");
					}
				}else{
					loga.add("["+getCurrentTime()+"] " +  "Hay un dispositivo registrado con el Mismo Código, Tipo:" 
							+ de.getTbDeviceType().getDeviceTypeName() + ". Estado: " + de.getTbDeviceState().getDeviceStateDescription()+
							". Saldo Actual: "+ de.getDeviceCurrentBalance() + ".");
				}
			}else{
				loga.add("["+getCurrentTime()+"] " +  "Debe Digitar el Facial de la  Tarjeta antes de Guardar.");
			}
		}else{
			loga.add("["+getCurrentTime()+"] " +  "Debe Consultar la Tarjeta para obtener el Id antes de Guardar.");
		}
		return null;
	}
	
	// Getter and Setter ----------------- // 

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
	 * @param devicesTypes the devicesTypes to set
	 */
	public void setDevicesTypes(List<SelectItem> devicesTypes) {
		this.devicesTypes = devicesTypes;
	}

	/**
	 * @return the devicesTypes
	 */
	public List<SelectItem> getDevicesTypes() {
		if (devicesTypes == null) {
			devicesTypes = new ArrayList<SelectItem>();
			for (TbDeviceType dt : deviceEJB.getDevicesTypes()) {
				if((dt.getDeviceTypeId().longValue() != 0L) && (dt.getDeviceTypeId().longValue() != 10L)){
					devicesTypes.add(new SelectItem(dt.getDeviceTypeId(), dt
							.getDeviceTypeName()));
				}
			}
		}
		return devicesTypes;
	}

	/**
	 * @param typeDevId the typeDevId to set
	 */
	public void setTypeDevId(Long typeDevId) {
		this.typeDevId = typeDevId;
	}

	/**
	 * @return the typeDevId
	 */
	public Long getTypeDevId() {
		return typeDevId;
	}

	/**
	 * @param facial the facial to set
	 */
	public void setFacial(String facial) {
		this.facial = facial;
	}

	/**
	 * @return the facial
	 */
	public String getFacial() {
		return facial;
	}

	/**
	 * @param deviceStates the deviceStates to set
	 */
	public void setDeviceStates(List<SelectItem> deviceStates) {
		this.deviceStates = deviceStates;
	}

	/**
	 * @return the deviceStates
	 */
	public List<SelectItem> getDeviceStates() {
		if (deviceStates == null) {
			deviceStates = new ArrayList<SelectItem>();
			for (TbDeviceState ds : deviceEJB.getDeviceStates()) {
				deviceStates.add(new SelectItem(ds.getDeviceStateId(), ds.getDeviceStateDescription()));
			}
		}
		return deviceStates;
	}

	/**
	 * @param deviceStateId the deviceStateId to set
	 */
	public void setDeviceStateId(Long deviceStateId) {
		this.deviceStateId = deviceStateId;
	}

	/**
	 * @return the deviceStateId
	 */
	public Long getDeviceStateId() {
		return deviceStateId;
	}

	/**
	 * @param showPanelEntryCard the showPanelEntryCard to set
	 */
	public void setShowPanelEntryCard(boolean showPanelEntryCard) {
		this.showPanelEntryCard = showPanelEntryCard;
	}

	/**
	 * @return the showPanelEntryCard
	 */
	public boolean isShowPanelEntryCard() {
		return showPanelEntryCard;
	}
}
