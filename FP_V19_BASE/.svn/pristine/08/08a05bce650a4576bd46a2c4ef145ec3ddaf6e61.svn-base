/**
 * 
 */
package arecatis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import sessionVar.SessionUtil;
import util.Order;
import util.PurchaseOrder;

import ejb.Purchase;
import ejb.User;

import jpa.TbCodeType;
import jpa.TbStationBO;
import jpa.TbSystemUser;
import jpa.TbUserData;

/**
 * @author tmolina
 * 
 */
public class OrderBean implements Serializable {
	private static final long serialVersionUID = -6107959070920153387L;
	
	@EJB(mappedName="ejb/User")
	private User user;
	
	@EJB(mappedName="ejb/Purchase")
	private Purchase purchase;
	
	// Attributes ----------//
	
	private List<TbCodeType> codeTypes;
	
	private Long codeType;
	
	private List<SelectItem> codeTypesList;
	
	private String codeClient;
	
	private List<TbUserData> clientData;
	
	private TbSystemUser client;
	
	private List<SelectItem> clientDataList;
	
	private Long clientDataId;
	
	private TbUserData data;
	
	private Long transNumber;
	
	private List<SelectItem> clientNames;
	
	private Long idClient;
	
	private List<TbSystemUser> clients; 
	
	private PurchaseOrder po;
	
	private List<SelectItem> stations;
	
	private Long station;
	
	private List<Order> operationTypes;
	
	// Control Visibility -----//
	
	private boolean showData;
	
	private boolean showMessage;
	
	private boolean showPdf;
	
	// Control Modal ------//
	
	private boolean showModal;
	
	private String modalMessage;
	
	/**
	 * Constructor.
	 */
	public OrderBean() {
		clientData = new ArrayList<TbUserData>();
		clientDataList = new ArrayList<SelectItem>(); 
		transNumber = 0L;
	}
	
	// Actions ------------------//
	
	/**
	 * Clean Modal Panel.
	 */
	public String hideModal(){
		setShowModal(false);
		setModalMessage("");
		//setShowPdf(false);
		return null;
	}
	
	/**
	 * Searches a client by code and type code.
	 */
	public String search(){
		setShowPdf(false);
		setShowModal(false);
		operationTypes = null;
		idClient = -1L;
		if (codeClient != null) {
			clientData = user.getClientData(codeClient, codeType);
			if (clientData.size() > 0) {
				
				data = clientData.get(0);
				
				clientDataId = data.getUserDataId();
				client = data.getTbSystemUser();
				idClient = client.getUserId();

				clientDataList.clear();
				for (TbUserData ud : clientData) {
					clientDataList.add(new SelectItem(ud.getUserDataId(), ud
							.getUserDataOfficeName()));
				}
				
				setShowData(true);
				setShowMessage(false);
			}else{
				setShowData(false);
				setShowMessage(true);
			}
		} else {
			setShowData(false);
			setShowMessage(true);
		}
		return null;
	}
	
	/**
	 * Changes the dependency.
	 */
	public String changeOffice(){
		setShowModal(false);
		setShowPdf(false);
		operationTypes = null;
		for(TbUserData ud : getClientData()){
			if(ud.getUserDataId().longValue() == clientDataId.longValue()){
				data = ud;
			}
		}
		return null;
	}
	
	/**
	 * Saves the order and print to pdf file the purchase order.
	 */
	public String printOrder(){
		if (transNumber.longValue() != 0) {
			
			po = purchase.savePuchaseOrder(data.getUserDataId(), transNumber,
					SessionUtil.ip(), SessionUtil.sessionUser().getUserId(), station, operationTypes);
			if (po != null) {			
				showPdf = true;
				setModalMessage("Se ha guardado la Orden de Pedido Número: "
						+ po.getOrder().getPuchaseOrderNumber() + ".");
			} else {
				showPdf = false;
				setModalMessage("No se pudo realizar la Transacción, comuníquese con Servicio al Cliente.");
			}
		} else {
			setModalMessage("Debe por lo menos realizar una operación para poder guardar la orden de pedido.");
		}
		
		setShowModal(true);
		return null;
	}
	
	/*
	 * Print the PDF.
	 */
	public String printPdf() {
		System.out.println("[INFO] Enters print PDF.");
		// Printing the oder to a pdf.
		setShowModal(false);
    	OrderPDF pdf = new OrderPDF(po);
    	pdf.execute();   	
		return null;
	}
	
	/**
	 * Change the data of client.
	 */
	public String changeClient(){
		setShowModal(false);
		setShowPdf(false);
		operationTypes = null;
		showMessage = false;
		if(idClient.longValue() == -1){
			showData = false;
			this.codeType = null;
			this.codeClient = null;
		} else {
			for(TbSystemUser u : getClients()){
				if(idClient.longValue() == u.getUserId().longValue()){
					this.client = u;
					this.codeType = u.getTbCodeType().getCodeTypeId();
					this.codeClient = u.getUserCode();
					
					clientData = user.getClientData(codeClient, codeType);
					
					data = clientData.get(0);
					
					if (clientData.size() > 0) {
						
						clientDataList.clear();
						for (TbUserData ud : clientData) {
							clientDataList.add(new SelectItem(ud.getUserDataId(), ud
									.getUserDataOfficeName()));
						}
						showData = true;
					} else {
						showData = false;
					}				
					break;
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public String changeTypeDoc() {
		setShowModal(false);
		setShowPdf(false);
		operationTypes = null;
		showMessage = false;
		showData = false;
		this.idClient = null;
		this.codeClient = null;
		return null;
	}
	
	// Getters and Setters ---------//

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
	 * @param codeTypes the codeTypes to set
	 */
	public void setCodeTypes(List<TbCodeType> codeTypes) {
		this.codeTypes = codeTypes;
	}

	/**
	 * @return the codeTypes
	 */
	public List<TbCodeType> getCodeTypes() {
		if(codeTypes == null){
			codeTypes = new ArrayList<TbCodeType>();
		}
		codeTypes = user.getCodeTypes();
		return codeTypes;
	}

	/**
	 * @param codeType the codeType to set
	 */
	public void setCodeType(Long codeType) {
		this.codeType = codeType;
	}

	/**
	 * @return the codeType
	 */
	public Long getCodeType() {
		return codeType;
	}

	/**
	 * @param codeTypesList the codeTypesList to set
	 */
	public void setCodeTypesList(List<SelectItem> codeTypesList) {
		this.codeTypesList = codeTypesList;
	}

	/**
	 * @return the codeTypesList
	 */
	public List<SelectItem> getCodeTypesList() {
		if(codeTypesList==null){
			codeTypesList = new ArrayList<SelectItem>();
			for (TbCodeType type : getCodeTypes()) {
				codeTypesList.add(new SelectItem(type.getCodeTypeId(), type
						.getCodeTypeAbbreviation()));
			}
		}
		
		return codeTypesList;
	}

	/**
	 * @param codeClient the codeClient to set
	 */
	public void setCodeClient(String codeClient) {
		this.codeClient = codeClient;
	}

	/**
	 * @return the codeClient
	 */
	public String getCodeClient() {
		return codeClient;
	}

	/**
	 * @param clientData the clientData to set
	 */
	public void setClientData(List<TbUserData> clientData) {
		this.clientData = clientData;
	}

	/**
	 * @return the clientData
	 */
	public List<TbUserData> getClientData() {
		return clientData;
	}

	/**
	 * @param client the client to set
	 */
	public void setClient(TbSystemUser client) {
		this.client = client;
	}

	/**
	 * @return the client
	 */
	public TbSystemUser getClient() {
		return client;
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
	 * @param clientDataList the clientDataList to set
	 */
	public void setClientDataList(List<SelectItem> clientDataList) {
		this.clientDataList = clientDataList;
	}

	/**
	 * @return the clientDataList
	 */
	public List<SelectItem> getClientDataList() {
		return clientDataList;
	}

	/**
	 * @param clientDataId the clientDataId to set
	 */
	public void setClientDataId(Long clientDataId) {
		this.clientDataId = clientDataId;
	}

	/**
	 * @return the clientDataId
	 */
	public Long getClientDataId() {
		return clientDataId;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(TbUserData data) {
		this.data = data;
	}

	/**
	 * @return the data
	 */
	public TbUserData getData() {
		return data;
	}

	/**
	 * @param transNumber the transNumber to set
	 */
	public void setTransNumber(Long transNumber) {
		this.transNumber = transNumber;
	}

	/**
	 * @return the transNumber
	 */
	public Long getTransNumber() {
		transNumber = 0L;
		for(Order ot: getOperationTypes()){
			if(ot.getNumberOfOperations()!=null){
				transNumber += ot.getNumberOfOperations();
			}
		}
		return transNumber;
	}

	/**
	 * @param clientNames the clientNames to set
	 */
	public void setClientNames(List<SelectItem> clientNames) {
		this.clientNames = clientNames;
	}

	/**
	 * @return the clientNames
	 */
	public List<SelectItem> getClientNames() {
		if (clientNames == null) {
			clientNames = new ArrayList<SelectItem>();
			
			clientNames.add(new SelectItem(-1, " "));
			for(TbSystemUser tu : getClients()){
				String name = tu.getUserNames();
				if(tu.getTbCodeType().getCodeTypeId().longValue() != 3){
					name = name + " " + tu.getUserSecondNames();
				}
				clientNames.add(new SelectItem(tu.getUserId(), name));
			}
		}		
		return clientNames;
	}

	/**
	 * @param clients the clients to set
	 */
	public void setClients(List<TbSystemUser> clients) {
		this.clients = clients;
	}

	/**
	 * @return the clients
	 */
	public List<TbSystemUser> getClients() {
		if (clients == null) {
			clients = new ArrayList<TbSystemUser>();
			clients = user.getAllActiveClient();
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
	 * @param showPdf the showPdf to set
	 */
	public void setShowPdf(boolean showPdf) {
		this.showPdf = showPdf;
	}

	/**
	 * @return the showPdf
	 */
	public boolean isShowPdf() {
		return showPdf;
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
	 * @param station the station to set
	 */
	public void setStation(Long station) {
		this.station = station;
	}

	/**
	 * @return the station
	 */
	public Long getStation() {
		return station;
	}

	/**
	 * @param operationTypes the operationTypes to set
	 */
	public void setOperationTypes(List<Order> operationTypes) {
		this.operationTypes = operationTypes;
	}

	/**
	 * @return the operationTypes
	 */
	public List<Order> getOperationTypes() {
		if(operationTypes == null){
			operationTypes = new ArrayList<Order>();
			operationTypes = purchase.getOperationTypes();
		}
		return operationTypes;
	}
}