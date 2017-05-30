/**
 * 
 */
package arecatis;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import constant.DeviceType;
import constant.PurchaseOrderState;

import jpa.TbAccount;
import jpa.TbDevice;
import jpa.TbPoint;
import jpa.TbStationBO;

import sessionVar.SessionUtil;
import util.OrderDetail;
import util.PurchaseOrder;
import util.device.ServiceCommand;
import ejb.Device;
import ejb.Purchase;
import ejb.Transaction;
import ejb.crud.Point;

/**
 * Bean to manage the request to procedure with the purchase operation.
 * @author tmolina
 *
 */
public class PurchaseBean implements Serializable {
	private static final long serialVersionUID = 4411508988644456744L;

	@EJB(mappedName="ejb/Purchase")
	private Purchase purchase;
	
	@EJB(mappedName="ejb/Device")
	private Device device;
	
	@EJB(mappedName="ejb/Transaction")
	private Transaction transaction;
	
	@EJB(mappedName="ejb/Point")
	private Point point;
		
	// Attributes -------------- // 
	
	private List<TbAccount> accountList;
	
	private Long orderNumber;
	
	private PurchaseOrder order;
	
	private Integer numOper;
	
	private Long detailTargetId;
		
	// Details ------------------ //
	
	private List<SelectItem> stations;
	
	private List<SelectItem> devices;
	
	private Long idDetail;
	
	// Visibility --------------- //
	
	private boolean showData;
	
	private boolean showMessage;
	
	private boolean showConsig;
	
	private boolean confirmationShow;
	
	private String corfirmMessage;
	
	private boolean showAccountDetail;
	
	private boolean showValue;
	
	// Consignment Relation ------ //
		
	private Long accountId;
	
	private boolean showConsignments;
	
	// Transfer between cards------------ //
	
	/**
	 * When there's an operation type = Transfer between cards. 
	 */
	private boolean showTran; 
	
	private Long consigValueT;
	
	private boolean confirmationShowT;
	
	// confirm save order, send to treasury.
	
	private boolean confirmSend;
	
	// Control Modal ------------ //
	
	private boolean showModal;
	
	private String modalMessage;
	
	//---- 
	
	private String invoiceNumber;
	
	//---

	private TbPoint tbPoint;
	
	private boolean confirmRecharge;
	
	private List<String> loga;
	
	private List<String> logc;
	
	private String deviceCode;
	
	private Long balance;
	
	private ServiceCommand command;
	
	/**
	 * Constructor.
	 */
	public PurchaseBean() {
		loga = new ArrayList<String>();
		logc = new ArrayList<String>();
	}
	
	// Actions ------------- //
	
	/**
	 * 
	 */
	public String initRecharge() { //TODO
		// Start with the IP address and if can make recharge.
		tbPoint = point.getUserPoint(SessionUtil.sessionUser().getUserId(),
				SessionUtil.ip());
		if(tbPoint != null) {
			
			// Searching the detail
			OrderDetail temp = null;
			for (OrderDetail d : order.getToDetailList()){
				if (d.getDetail().getPurchaseOrderDetailId().longValue() == idDetail
						.longValue()) {
					temp = d;
					break;
				}
			}
			
			
			
			//validate value different from null o Zero.
			if (temp.getValueTo() != null && temp.getValueTo() != 0L) {
				
				// validate device
				if (temp.getIdDevice() != null) {
					
					//validate operation station 
					if (temp.getIdStation() != null) {
						
						// validate balance account
						
						if (transaction.getAvailableBalanceAccount(
								order.getTbAccount().getAccountId())
								.longValue() >= temp.getValueTo().longValue()) {
							
							purchase.updateDetail(temp.getDetail().getPurchaseOrderDetailId(), temp
									.getIdDevice(), temp.getIdStation(), temp.getValueTo().longValue(),
									SessionUtil.ip(), SessionUtil.sessionUser().getUserId());
							clear();
							order = purchase.getPurchaseOrderByState(orderNumber, PurchaseOrderState.NEW, true);
							numOper = order.getDetails().size();
							
							// Confirmation
							setConfirmRecharge(true);
							setCorfirmMessage("¿Está seguro de realizar esta operación?");
							
						} else {
							setModalMessage("El Valor de la Recarga supera el saldo disponible en la cuenta.");
							setShowModal(true);
						}
						
					} else {
						setModalMessage("Debe seleccionar una estación de operación.");
						setShowModal(true);
					}
				} else {
					setModalMessage("Debe seleccionar un dispositivo.");
					setShowModal(true);
				}
				
			} else{
				setModalMessage("Digite el valor de la Operación. Debe ser mayor que Cero.");
				setShowModal(true);
			}
		} else {
			setModalMessage("El Usuario en Sesión no Tiene permisos para realizar ésta operación desde la dirección IP actual.");
			setShowModal(true);
		}
		return null;
	}
	
	/**
	 * 
	 */
	public String recharge (){	
		setConfirmRecharge(false);
		// Searching the detail
		OrderDetail temp = null;
		for (OrderDetail d : order.getToDetailList()){
			if (d.getDetail().getPurchaseOrderDetailId().longValue() == idDetail
					.longValue()) {
				temp = d;
				break;
			}
		}
		
		if(temp.getDetail().getTbDevice().getTbDeviceType()!= null) {
			
			// see if it's not a tag device
			if(temp.getDetail().getTbDevice().getTbDeviceType().getDeviceTypeId().longValue() != DeviceType.TAG.getId().longValue()){
				System.out.println("Enters card");
				if(this.rechargeCard(temp.getValueTo(), temp.getDetail()
						.getTbDevice().getDeviceCode(), temp.getDetail()
						.getTbDevice().getDeviceFacialId())){
					} else {
						return null;
					}			
			} 
			if (purchase.saveRecharge(idDetail, order.getTbAccount().getAccountId(), temp.getValueTo(),
					SessionUtil.ip(), SessionUtil.sessionUser().getUserId(), temp.getIdStation())) {
				loga.add("["+getCurrentTime()+"] " +  "Se ha registrado la transacción Exitasamente.");
			} else {
				loga.add("["+getCurrentTime()+"] " +  "No se pudo registrar la transacción.");
			}
			clear();
			order = purchase.getPurchaseOrderByState(orderNumber, PurchaseOrderState.NEW, true);
			numOper = order.getDetails().size();
		} else {
			loga.add("["+getCurrentTime()+"] " +  "El dispositivo no posee un tipo no se puede hacer la transacción.");
		}
		return null;
	}
	
	/**
	 * Doing the card recharge.
	 * @return
	 */
	private boolean rechargeCard(Long valueToRecharge, String deviceCodeSent, String facial){
		boolean result = false;
		String response;
		command = new ServiceCommand(tbPoint.getPointIp(), tbPoint
				.getPointPort().toString());
		try {
			response = command.sendCommand("1");
			System.out.println("firts=" + response);
			if (response.contains("*") ||  response == null || response.equals("")) {
				processRequest(response);
				deviceCode = null;
				balance = null;
			} else {
				System.out.println(response);
				String[] res = response.split(";");
				System.out.println(res.length);
				deviceCode = res[0];
				
				String b = res[1];
				try {
					balance = Long.parseLong(b);
						
					// Validate that the code of the card that's on the reader is the same of the detail that's going to be recharge.
					if(deviceCodeSent.equals(deviceCode)){
							
						//Doing the recharge.
						String confirmation = command.sendReload("2", deviceCode,
								valueToRecharge.toString());
							
						if(!confirmation.equals("-2")){
							
							result = true;							
							logc.add("["+getCurrentTime()+"] " +  "Recarga de Tarjeta Finalizada. Transacción Exitosa.");
							
							System.out.println("[ Response before Print -> "+confirmation + " ]");
							
							command.sendPrint("1", getCurrentTime().toUpperCase(), null, null, balance.toString(), 
									valueToRecharge.toString(), Long.toString(balance + valueToRecharge.longValue()),
								    deviceCode, facial);				
							
						} else{
							logc.add("["+getCurrentTime()+"] " +  "No se pudo recargar la tarjeta, Intente Nuevamente.");
						}
					} else{
						logc.add("["+getCurrentTime()+"] " +  "La Tarjeta en el lector no corresponde al Item Seleccionado a ser Recargado, Verifique, por favor.");
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
		return result;
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
	 * Searches Purchase Order Data  by  Order Number.
	 */
	public String search(){
		order = purchase.getPurchaseOrderByState(orderNumber,
				PurchaseOrderState.NEW, true);
		if (order != null) {
			numOper = order.getDetails().size();
			setShowData(true);
			setShowMessage(false);
		} else {
			setShowData(false);
			setShowMessage(true);
		}
		return null;
	}
	
	/**
	 * Control Modal.
	 */
	public String hideModal(){
		setModalMessage("");
		setShowModal(false);
		setShowConsig(false);
		setConfirmationShow(false);
		setShowAccountDetail(false);
		setShowValue(false);
		setConfirmationShowT(false);
		setShowTran(false);
		setConfirmSend(false);
		setConfirmRecharge(false);
		return null;
	}
	
	/**
	 * Init Consignment.
	 */
	public String initConsig(){
		hideModal();
		clear();
		setShowConsig(true);
		return null;
	}
	
	/**
	 * Shows consignment confirmation. 
	 */
	public String initConfirm(){		
		setCorfirmMessage("¿Está Seguro que desea realizar la Transacción ?");
		setShowValue(false);
		setConfirmationShow(true);
		return null;
	}
	

	
	/**
	 * Saves the accountRelation
	 */
	public String saveAccountRelation(){
		setConfirmationShow(false);
		if (purchase.saveOrderAccount(order.getOrder().getPurchaseOrderId(), SessionUtil
						.sessionUser().getUserId(), accountId, SessionUtil.ip())) {
			setModalMessage("Se ha asociado la Cuenta con la orden de pedido Satisfactoriamente.");
			order = purchase.getPurchaseOrderByState(orderNumber,
					PurchaseOrderState.NEW, true);
			numOper = order.getDetails().size();
		} else {
			setModalMessage("No se pudo realizar la Transacción.");
		}
		setShowModal(true);
		return null;
	}
	
	/**
	 * Shows view Account detail.
	 */
	public String initAccountDetail(){
		setShowAccountDetail(true);
		return null;
	}
	
	public String initDetail(){
		System.out.println(detailTargetId);
		return null;
	}
	
	/**
	 * Clear fields.
	 */
	public void clear(){
		setConsigValueT(null);
	}
	
	/**
	 * Searches Consignment association with client. 
	 */
	public String initValue(){
		hideModal();
		clear();
		setShowValue(true);
		if(accountList == null) {
			accountList = new ArrayList<TbAccount>();
		}else{
			accountList.clear();
		}
		accountList = transaction.getClientAccounts(order.getOrder().getTbUserData()
						.getTbSystemUser().getUserId());
		if (accountList.size() > 0) {
			showConsignments = true;
		} else {
			showConsignments = false;
		}
		return null;
	}
	
	/**
	 * Init send to treasury
	 */
	public String initSaveDetails(){
		setConfirmSend(true);
		setCorfirmMessage("¿Está seguro de realizar la Transacción?");
		return null;
	}
	
	/**
	 * Save Detailed.
	 */
	public String save() {
		setConfirmSend(false);
		for(OrderDetail po : order.getToDetailList()){
			if(po.getDetail().getDetailOperationValue() == null){
				setModalMessage("Hay Items sin detallar, Verifique.");
				setShowModal(true);
				return null;
			} 
		}
		if (purchase.orderSendTreasury(order.getOrder().getPurchaseOrderId(), SessionUtil.ip(), SessionUtil
				.sessionUser().getUserId(), invoiceNumber, order.getValueTo(), order.getTbAccount().getAccountId())) {
			setModalMessage("Transacción Exitosa.");
			setShowModal(true);
			order = purchase.getPurchaseOrderByState(orderNumber,PurchaseOrderState.NEW, true);
			setShowData(false);
		} else {
			setModalMessage("Error en Transacción.");
			setShowModal(true);
		}
		return null;
	}
	
	// Getters and Setters --------------- //
	
	/**
	 * @param orderNumber the orderNumber to set
	 */
	public void setOrderNumber(Long orderNumber) {
		this.orderNumber = orderNumber;
	}

	/**
	 * @return the orderNumber
	 */
	public Long getOrderNumber() {
		return orderNumber;
	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(PurchaseOrder order) {
		this.order = order;
	}

	/**
	 * @return the order
	 */
	public PurchaseOrder getOrder() {
		return order;
	}

	/**
	 * @param showModal the showModal to set
	 */
	public void setShowModal(boolean showModal) {
		this.showModal = showModal;
	}

	/**
	 * @return the showModal
	 */
	public boolean isShowModal() {
		return showModal;
	}

	/**
	 * @param modalMessage the modalMessage to set
	 */
	public void setModalMessage(String modalMessage) {
		this.modalMessage = modalMessage;
	}

	/**
	 * @return the modalMessage
	 */
	public String getModalMessage() {
		return modalMessage;
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
	 * @param numOper the numOper to set
	 */
	public void setNumOper(Integer numOper) {
		this.numOper = numOper;
	}

	/**
	 * @return the numOper
	 */
	public Integer getNumOper() {
		return numOper;
	}

	/**
	 * @param showConsig the showConsig to set
	 */
	public void setShowConsig(boolean showConsig) {
		this.showConsig = showConsig;
	}

	/**
	 * @return the showConsig
	 */
	public boolean isShowConsig() {
		return showConsig;
	}

	/**
	 * @param confirmationShow the confirmationShow to set
	 */
	public void setConfirmationShow(boolean confirmationShow) {
		this.confirmationShow = confirmationShow;
	}

	/**
	 * @return the confirmationShow
	 */
	public boolean isConfirmationShow() {
		return confirmationShow;
	}

	/**
	 * @param corfirmMessage the corfirmMessage to set
	 */
	public void setCorfirmMessage(String corfirmMessage) {
		this.corfirmMessage = corfirmMessage;
	}

	/**
	 * @return the corfirmMessage
	 */
	public String getCorfirmMessage() {
		return corfirmMessage;
	}

	/**
	 * @param detailTargetId the detailTargetId to set
	 */
	public void setDetailTargetId(Long detailTargetId) {
		this.detailTargetId = detailTargetId;
	}

	/**
	 * @return the detailTargetId
	 */
	public Long getDetailTargetId() {
		return detailTargetId;
	}

	/**
	 * @param showValue the showValue to set
	 */
	public void setShowValue(boolean showValue) {
		this.showValue = showValue;
	}

	/**
	 * @return the showValue
	 */
	public boolean isShowValue() {
		return showValue;
	}

	/**
	 * @param stations the stations to set
	 */
	public void setStations(List<SelectItem> stations) {
		this.stations = stations;
	}

	/**
	 * @return the stations
	 */
	public List<SelectItem> getStations() {
		if(stations == null){
			stations = new ArrayList<SelectItem>();
			List<TbStationBO> stationsO = purchase.getRechargeStations();
			
			for(TbStationBO s : stationsO){
				stations.add(new SelectItem(s.getStationBOId(), s.getStationBOName()));
			}
		}
		return stations;
	}

	/**
	 * @param devices the devices to set
	 */
	public void setDevices(List<SelectItem> devices) {
		this.devices = devices;
	}

	/**
	 * @return the devices
	 */
	public List<SelectItem> getDevices() {
		if(devices == null){
			devices = new ArrayList<SelectItem>();
		} else {
			devices.clear();
		}
		String facial;
		for(TbDevice d :  device.getDevicesByAccount(order.getTbAccount().getAccountId())){
			if (d.getDeviceFacialId() == null) {
				facial = "[Facial No Asignado]";
			} else {
				facial = d.getDeviceFacialId();
			}
			devices.add(new SelectItem(d.getDeviceId(), d.getDeviceCode() + " - " + facial));
		}
		return devices;
	}

	/**
	 * @param showTran the showTran to set
	 */
	public void setShowTran(boolean showTran) {
		this.showTran = showTran;
	}

	/**
	 * @return the showTran
	 */
	public boolean isShowTran() {
		return showTran;
	}

	/**
	 * @param consigValueT the consigValueT to set
	 */
	public void setConsigValueT(Long consigValueT) {
		this.consigValueT = consigValueT;
	}

	/**
	 * @return the consigValueT
	 */
	public Long getConsigValueT() {
		return consigValueT;
	}

	/**
	 * @param confirmationShowT the confirmationShowT to set
	 */
	public void setConfirmationShowT(boolean confirmationShowT) {
		this.confirmationShowT = confirmationShowT;
	}

	/**
	 * @return the confirmationShowT
	 */
	public boolean isConfirmationShowT() {
		return confirmationShowT;
	}

	/**
	 * @param idDetail the idDetail to set
	 */
	public void setIdDetail(Long idDetail) {
		this.idDetail = idDetail;
	}

	/**
	 * @return the idDetail
	 */
	public Long getIdDetail() {
		return idDetail;
	}

	/**
	 * @param confirmSend the confirmSend to set
	 */
	public void setConfirmSend(boolean confirmSend) {
		this.confirmSend = confirmSend;
	}

	/**
	 * @return the confirmSend
	 */
	public boolean isConfirmSend() {
		return confirmSend;
	}

	/**
	 * @param showConsignments the showConsignments to set
	 */
	public void setShowConsignments(boolean showConsignments) {
		this.showConsignments = showConsignments;
	}

	/**
	 * @return the showConsignments
	 */
	public boolean isShowConsignments() {
		return showConsignments;
	}

	/**
	 * @param invoiceNumber the invoiceNumber to set
	 */
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	/**
	 * @return the invoiceNumber
	 */
	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	/**
	 * @param accountList the accountList to set
	 */
	public void setAccountList(List<TbAccount> accountList) {
		this.accountList = accountList;
	}

	/**
	 * @return the accountList
	 */
	public List<TbAccount> getAccountList() {
		return accountList;
	}

	/**
	 * @param accountId the accountId to set
	 */
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	/**
	 * @return the accountId
	 */
	public Long getAccountId() {
		return accountId;
	}

	/**
	 * @param showAccountDetail the showAccountDetail to set
	 */
	public void setShowAccountDetail(boolean showAccountDetail) {
		this.showAccountDetail = showAccountDetail;
	}

	/**
	 * @return the showAccountDetail
	 */
	public boolean isShowAccountDetail() {
		return showAccountDetail;
	}

	/**
	 * @param confirmRecharge the confirmRecharge to set
	 */
	public void setConfirmRecharge(boolean confirmRecharge) {
		this.confirmRecharge = confirmRecharge;
	}

	/**
	 * @return the confirmRecharge
	 */
	public boolean isConfirmRecharge() {
		return confirmRecharge;
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
}