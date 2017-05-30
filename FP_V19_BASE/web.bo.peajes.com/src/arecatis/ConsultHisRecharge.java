/**
 * 
 */
package arecatis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import ejb.Purchase;

import jpa.TbDevice;
import jpa.TbOperationType;
import jpa.TbPurchaseOrderDetail;
import jpa.TbSystemUser;

/**
 * Bean no manage the recharge historic consult.
 * @author tmolina
 *
 */
public class ConsultHisRecharge implements Serializable {
	private static final long serialVersionUID = 3158765762921072696L;
	
	@EJB(mappedName="ejb/Purchase")
	private Purchase purchase;
	
	// Attributes ----------- //
	
	private List<SelectItem> operationTypes;
	
	private Long idOperationType;
	
	private boolean checkDate;
	
	private Date begDate;
	
	private Date endDate;
	
	private boolean checkClient;
	
	private List<SelectItem> clients;
	
	private List<TbSystemUser> clientList;
	
	private Long idClient;
	
	private String clientName;
	
	private boolean checkTisc;
	
	private List<TbDevice> devices;
	
	private List<SelectItem> deviceCodes;
	
	private List<SelectItem> deviceFacials;
	
	private Long idDevice;
	
	private boolean checkValue;
	
	private Long value;
	
	private boolean showMessage;
	
	private String message;
	
	private boolean showData;
	
	private List<TbPurchaseOrderDetail> details;

	/**
	 * Constructor
	 */
	public ConsultHisRecharge() {
	}
	
	// Actions ---------------- //
	
	/**
	 * Gets the selected client name.
	 */
	public String changeName(){
		if(idClient.longValue() == -1L){
			setClientName(null);
		} else {
			for(TbSystemUser tsu : getClientList()){
				if(tsu.getUserId().longValue() == idClient.longValue()){
					setClientName(tsu.getUserNames());
					if(tsu.getTbCodeType().getCodeTypeId().longValue()!=3){
						setClientName(getClientName()+ " " + tsu.getUserSecondNames());
					}
					break;
				}
			}
		}
		return null;
	}
	
	/**
	 * Consult.
	 */
	public String consult() {
		if(!checkClient && !checkDate && !checkTisc && !checkValue){
			setShowMessage(true);
			setMessage("Debe Seleccionar por lo menos un criterio de busqueda.");
			setShowData(false);
		} else {
			if(idOperationType.longValue() == 1){
				if(checkDate){
					if(begDate == null && endDate == null){
						setShowMessage(true);
						setMessage("Debe seleccionar las fechas.");
						setShowData(false);
						return null;
					}
				}
			}
			if(checkClient){
				if(idClient.longValue()==-1L){
					setShowMessage(true);
					setMessage("Debe seleccionar un Cliente.");
					setShowData(false);
					return null;
				}
			}
			if(checkTisc){
				if(idDevice.longValue()==-1L){
					setShowMessage(true);
					setMessage("Debe seleccionar un Dispositivo.");
					setShowData(false);
					return null;
				}
			}
			if(checkValue){
				if (value == null) {
					setShowMessage(true);
					setMessage("Debe digitar un Valor.");
					setShowData(false);
					return null;
				}
			}
			if(details== null){
				details=new ArrayList<TbPurchaseOrderDetail>();
			}else{
				details.clear();
			}
			details = purchase.histoConsult(idOperationType, checkDate,
					begDate, endDate, checkClient, idClient, checkTisc,
					idDevice, checkValue, value);
			if(details.size() >0){
				setShowMessage(false);
				setShowData(true);
			}else{
				setShowMessage(true);
				setMessage("No hay Información para los parámetros Ingresados.");
				setShowData(false);
			}
		}
		return null;
	}
	
	/**
	 * Clear Data.
	 */
	public String clear() {
		setShowData(false);
		setShowMessage(false);
		setCheckClient(false);
		setCheckDate(false);
		setCheckTisc(false);
		setCheckValue(false);
		setValue(null);
		setBegDate(null);
		setEndDate(null);
		setIdDevice(-1L);
		setIdClient(-1L);
		setIdOperationType(1L);
		return null;
	}
	
	/**
	 * Prints The result into pdf.
	 */
	public String printPdf(){
		HistoRechargePDF pdf = new HistoRechargePDF(this.details);
		pdf.execute();
		return null;
	}
		
	// Getters and Setters ------------ //

	/**
	 * @param checkDate the checkDate to set
	 */
	public void setCheckDate(boolean checkDate) {
		this.checkDate = checkDate;
	}

	/**
	 * @return the checkDate
	 */
	public boolean isCheckDate() {
		return checkDate;
	}

	/**
	 * @param begDate the begDate to set
	 */
	public void setBegDate(Date begDate) {
		this.begDate = begDate;
	}

	/**
	 * @return the begDate
	 */
	public Date getBegDate() {
		return begDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param checkClient the checkClient to set
	 */
	public void setCheckClient(boolean checkClient) {
		this.checkClient = checkClient;
	}

	/**
	 * @return the checkClient
	 */
	public boolean isCheckClient() {
		return checkClient;
	}

	/**
	 * @param clients the clients to set
	 */
	public void setClients(List<SelectItem> clients) {
		this.clients = clients;
	}

	/**
	 * @return the clients
	 */
	public List<SelectItem> getClients() {
		if(clients==null){
			clients = new ArrayList<SelectItem>();
			clients.add(new SelectItem(-1, "<Seleccione Cliente>"));
			for ( TbSystemUser tsu : getClientList()){
				clients.add(new SelectItem(tsu.getUserId(), tsu.getUserCode()
						+ "-" + tsu.getTbCodeType().getCodeTypeAbbreviation()));
			}
		}
		return clients;
	}

	/**
	 * @param idClient the idClient to set
	 */
	public void setIdClient(Long idClient) {
		this.idClient = idClient;
	}

	/**
	 * @return the idClient
	 */
	public Long getIdClient() {
		return idClient;
	}

	/**
	 * @param clientList the clientList to set
	 */
	public void setClientList(List<TbSystemUser> clientList) {
		this.clientList = clientList;
	}

	/**
	 * @return the clientList
	 */
	public List<TbSystemUser> getClientList() {
		if(clientList == null){
			clientList = new ArrayList<TbSystemUser>();
			clientList = purchase.getOrderClients();
		}
		return clientList;
	}

	/**
	 * @param clientName the clientName to set
	 */
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	/**
	 * @return the clientName
	 */
	public String getClientName() {
		return clientName;
	}

	/**
	 * @param checkTisc the checkTisc to set
	 */
	public void setCheckTisc(boolean checkTisc) {
		this.checkTisc = checkTisc;
	}

	/**
	 * @return the checkTisc
	 */
	public boolean isCheckTisc() {
		return checkTisc;
	}

	/**
	 * @param deviceCodes the deviceCodes to set
	 */
	public void setDeviceCodes(List<SelectItem> deviceCodes) {
		this.deviceCodes = deviceCodes;
	}

	/**
	 * @return the deviceCodes
	 */
	public List<SelectItem> getDeviceCodes() {
		if(deviceCodes == null){
			deviceCodes = new ArrayList<SelectItem>();
			deviceCodes.add(new SelectItem(-1,"<Seleccione  Código>"));
			for(TbDevice td : getDevices()){
				deviceCodes.add(new SelectItem(td.getDeviceId(), td.getDeviceCode()));
			}
		}
		return deviceCodes;
	}

	/**
	 * @param deviceFacials the deviceFacials to set
	 */
	public void setDeviceFacials(List<SelectItem> deviceFacials) {
		this.deviceFacials = deviceFacials;
	}

	/**
	 * @return the deviceFacials
	 */
	public List<SelectItem> getDeviceFacials() {
		if(deviceFacials == null){
			deviceFacials = new ArrayList<SelectItem>();
			deviceFacials.add(new SelectItem(-1,"<Seleccione  Facial>"));
			for(TbDevice td : getDevices()){
				deviceFacials.add(new SelectItem(td.getDeviceId(), td.getDeviceFacialId()));
			}
		}
		return deviceFacials;
	}

	/**
	 * @param idDevice the idDevice to set
	 */
	public void setIdDevice(Long idDevice) {
		this.idDevice = idDevice;
	}

	/**
	 * @return the idDevice
	 */
	public Long getIdDevice() {
		return idDevice;
	}

	/**
	 * @param devices the devices to set
	 */
	public void setDevices(List<TbDevice> devices) {
		this.devices = devices;
	}

	/**
	 * @return the devices
	 */
	public List<TbDevice> getDevices() {
		if(devices == null){
			devices = new ArrayList<TbDevice>();
			devices = purchase.getOrderDevices();
		}
		return devices;
	}

	/**
	 * @param checkValue the checkValue to set
	 */
	public void setCheckValue(boolean checkValue) {
		this.checkValue = checkValue;
	}

	/**
	 * @return the checkValue
	 */
	public boolean isCheckValue() {
		return checkValue;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Long value) {
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public Long getValue() {
		return value;
	}

	/**
	 * @param operationTypes the operationTypes to set
	 */
	public void setOperationTypes(List<SelectItem> operationTypes) {
		this.operationTypes = operationTypes;
	}

	/**
	 * @return the operationTypes
	 */
	public List<SelectItem> getOperationTypes() {
		if(operationTypes == null){
			operationTypes = new ArrayList<SelectItem>();
			for(TbOperationType ot : purchase.getListOfOperationTypes()){
				operationTypes.add(new SelectItem(ot.getOperationTypeId(), ot.getOperationTypeName()));
			}
		}
		return operationTypes;
	}

	/**
	 * @param idOperationType the idOperationType to set
	 */
	public void setIdOperationType(Long idOperationType) {
		this.idOperationType = idOperationType;
	}

	/**
	 * @return the idOperationType
	 */
	public Long getIdOperationType() {
		return idOperationType;
	}

	/**
	 * @param showMessage the showMessage to set
	 */
	public void setShowMessage(boolean showMessage) {
		this.showMessage = showMessage;
	}

	/**
	 * @return the showMessage
	 */
	public boolean isShowMessage() {
		return showMessage;
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
		return showData;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param details the details to set
	 */
	public void setDetails(List<TbPurchaseOrderDetail> details) {
		this.details = details;
	}

	/**
	 * @return the details
	 */
	public List<TbPurchaseOrderDetail> getDetails() {
		return details;
	}
}