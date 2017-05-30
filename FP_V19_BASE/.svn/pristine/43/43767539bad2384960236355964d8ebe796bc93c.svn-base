package process.messageStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import util.TransactionStatus;

import ejb.Device;
import ejb.Transaction;
import ejb.User;
import ejb.crud.Company;
import ejb.crud.Station;

import jpa.ReAccountDevice;
import jpa.TbAccount;
import jpa.TbCodeType;
import jpa.TbCompany;
import jpa.TbLane;
import jpa.TbStationBO;
import jpa.TbSystemUser;
import jpa.TbTransactionDac;

public class MessageStatusBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@EJB(mappedName="ejb/Company")
	private Company company;
	
	@EJB(mappedName="ejb/Station")
	private Station station;
	
	@EJB(mappedName="ejb/User")
	private User user;
	
	@EJB(mappedName="ejb/Device")
	private Device device;
	
	@EJB(mappedName="ejb/Transaction")
	private Transaction transaction;
	
	private TbSystemUser client;
	
	private TbTransactionDac transactionDac;
	
	private Long transactionId;
	
	private Date initDate;
	
	private Date endDate;
	
	private Long conce=-1L;
	
	private Long stationId=-1L;
	
    private Long laneId=-1L;
	
	private String codClient="";
	
	private Long accountId=-1L;
	
    private Long deviceId=-1L;
    
	private Long codeType;
	
	private Long idClient=-1L;
    
    private List<TbCodeType> codeTypes;
	
	private List<SelectItem> codeTypesList;
	
    private List<SelectItem> conceList;
    
    private List<SelectItem> stationList;
    
    private List<SelectItem> laneList;
    
    private List<SelectItem> accountList;
    
	private List<SelectItem> deviceList;
	
	private List<TbAccount> accounts;
	
	private List<TransactionStatus> details;
	
	private boolean showMessage;
	
	private boolean showMessage1;
	
	private boolean showData;
	
	private boolean showDac;
	
	private Long def=new Long(-1);
	
	private Long def3=new Long(-3);
	
    private String dateTransaction;
	
	private String companyTransaction;
	
	private String stationTransaction;
	
	private Long laneTransaction;
	
	private Long cateTransaction;
	
	private String messageModal;
	
	private boolean showModal;
	
	private boolean disabled;
	
	
	public MessageStatusBean(){
		super();
		initDate= new Date();
		endDate= new Date();
		accountList= new ArrayList<SelectItem>();
		accountList.add(new SelectItem(-1,"--Seleccione Cuenta--"));
		details= new ArrayList<TransactionStatus>();
	}

	public void setInitDate(Date initDate) {
		this.initDate = initDate;
	}

	public Date getInitDate() {
		return initDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setConce(Long conce) {
		this.conce = conce;
	}

	public Long getConce() {
		return conce;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setLaneId(Long laneId) {
		this.laneId = laneId;
	}

	public Long getLaneId() {
		return laneId;
	}

	public void setCodClient(String codClient) {
		this.codClient = codClient;
	}

	public String getCodClient() {
		return codClient;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Long getAccountId() {
		return accountId;
	}

	/**
	 * @param deviceId the deviceId to set
	 */
	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * @return the deviceId
	 */
	public Long getDeviceId() {
		return deviceId;
	}

	/**
	 * @param conceList the conceList to set
	 */
	public void setConceList(List<SelectItem> conceList) {
		this.conceList = conceList;
	}

	/**
	 * @return the conceList
	 */
	public List<SelectItem> getConceList() {
		if(conceList == null) {
			conceList = new ArrayList<SelectItem>();
			conceList.add(new SelectItem(-1L, "--Seleccione La Concesión--"));
			for(TbCompany c : company.getConcession()) {
				conceList.add(new SelectItem(c.getCompanyId(), c.getCompanyName()));
			}

		}
		return conceList;
	}

	/**
	 * @param stationList the stationList to set
	 */
	public void setStationList(List<SelectItem> stationList) {
		this.stationList = stationList;
	}

	/**
	 * @return the stationList
	 */
	public List<SelectItem> getStationList() {
		stationList= new ArrayList<SelectItem>();
		
		if(!conce.equals(def)){
			List<TbStationBO> lis=station.getStationListByCompany(conce);
			if(lis.size()==0){
				stationList.add(new SelectItem(-2, "No Existe informacion"));
			}
			else{
				stationList.add(new SelectItem(-3, "Seleccione"));
				stationList.add(new SelectItem(0, "TODAS"));
				for(TbStationBO t: lis){
					stationList.add(new SelectItem(t.getStationBOId(), t.getStationBOName()));
				}
			}
		}
		else{
			stationList.add(new SelectItem(-1, "--Seleccione Estación--"));
		}
		return stationList;
	}

	/**
	 * @param laneList the laneList to set
	 */
	public void setLaneList(List<SelectItem> laneList) {
		this.laneList = laneList;
	}

	/**
	 * @return the laneList
	 */
	public List<SelectItem> getLaneList() {
		laneList= new ArrayList<SelectItem>();
		if(!stationId.equals(def) && !conce.equals(def) && !stationId.equals(def3)){
			List<TbLane> lis=station.getLaneListByStation(stationId);
			
			if(lis.size()==0 && !stationId.equals(0L)){
				laneList.add(new SelectItem(-2, "No Existe informacion"));
			}

			else{
				laneList.add(new SelectItem(0, "TODOS"));
				for(TbLane t: lis){
					laneList.add(new SelectItem(t.getLaneId(),t.getLaneCode()));
				}
			}
			
		}
		else{
			laneList.add(new SelectItem(-1, "--Seleccione Carril--"));
		}
		return laneList;
	}

	/**
	 * @param accountList the accountList to set
	 */
	public void setAccountList(List<SelectItem> accountList) {
		this.accountList = accountList;
	}


	/**
	 * @param deviceList the deviceList to set
	 */
	public void setDeviceList(List<SelectItem> deviceList) {
		this.deviceList = deviceList;
	}

	/**
	 * @return the deviceList
	 */
	public List<SelectItem> getDeviceList() {
		deviceList= new ArrayList<SelectItem>();
		if(!accountId.equals(def)){
			List<ReAccountDevice> lis=device.getAccountDeviceByAccount(accountId);
			if(lis.size()==0 && !accountId.equals(0L) && !accountId.equals(-3L)){
				deviceList.add(new SelectItem(-2, "No Existe informacion"));
			}
			
			else{
				deviceList.add(new SelectItem(-3, "Seleccione"));
				deviceList.add(new SelectItem(0, "TODOS"));
				for(ReAccountDevice t : lis){
					Long idD= t.getTbDevice().getDeviceId();
					String inf=t.getTbDevice().getDeviceCode() + "-" + (t.getTbVehicle()!=null?t.getTbVehicle().getVehiclePlate():"");
					deviceList.add(new SelectItem(idD, inf));
				}
			}
			
		}
		else{
			deviceList.add(new SelectItem(-1, "--Seleccione--"));
		}
		return deviceList;
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

	
	public String getTransactions(){
		System.out.println("initDate " + initDate);
		System.out.println("endDate " + endDate);
		if(initDate!=null && endDate!=null){
			if(initDate.getTime()<=endDate.getTime()){
				
				if(conce!=null && conce!=-1L){
					if(idClient!=null && idClient!=-1L){
						details=transaction.getTransaction(initDate, endDate, conce, stationId, laneId, idClient, accountId, deviceId);
						if(details.size()>0){
							this.setShowModal(false);
							this.setShowData(true);
						}
						else{
							this.setShowData(false);
							this.setMessageModal("No se encontraron resultados.");
							this.setShowModal(true);
						}
					}
					else{
						this.setMessageModal("No se encontraron resultados.");
						this.setShowModal(true);
						this.setShowData(false);
					}
				}
				else{
					this.setMessageModal("Error: El campo concesión es requerido.");
					this.setShowModal(true);
					this.setShowData(false);
				}
			}
			else{
				this.setMessageModal("Error: La fecha fin no debe ser menor a la fecha inicio.");
				this.setShowModal(true);
				this.setShowData(false);
			}
		}
		else{
			this.setMessageModal("Error: El campo fecha inicio y fecha fin no deben estar vacios.");
			this.setShowModal(true);
			this.setShowData(false);
		}
	
		return null;
	}
	
	public String getStatus(){
		transactionDac= transaction.getDetection(transactionId);
		if(transactionDac!=null){
			this.setShowDac(true);
			this.setShowModal(false);
		}
		else{
			this.setShowDac(false);
			this.setMessageModal("No se encontró detección para esta transacción");
			this.setShowModal(true);
		}
		return null;
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
	 * 
	 */
	public String changeTypeDoc() {
		showMessage = false;
		this.idClient=null;
		this.codClient = null;
		return null;
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
	 * Searches a client by code and type code.
	 */
	public String search() {
		this.setShowDac(false);
		accountList.clear();
		this.setDisabled(false);
		idClient = -1L;
		accountList.add(new SelectItem(-3,"Seleccione"));
		this.setShowModal(false);
		this.setShowData(false);
		System.out.println("codClient" + codClient);
		if (!codClient.equals("")) {
			client = user.getUserByCode(codClient.trim(), codeType);
			if (client != null) {
				idClient = client.getUserId();
				accounts = user.getClientAccount(idClient);
				if(accounts.size()>0){
					accountList.add(new SelectItem(0, "TODAS"));
					for(TbAccount t: accounts){
						accountList.add(new SelectItem(t.getAccountId(), t.getAccountId().toString()));
					}
				}
				else{
					accountList.add(new SelectItem(-2,"No existen cuentas"));
				}
				
			}
			else{
				this.setMessageModal("Error: No existe información para el cliente digitado.");
				this.setShowModal(true);
			}
		}else{
			this.setMessageModal("Error: Debe digitar el número de identificación del cliente");
			this.setShowModal(true);
		}
		return null;
	}

	/**
	 * @param accounts the accounts to set
	 */
	public void setAccounts(List<TbAccount> accounts) {
		this.accounts = accounts;
	}

	/**
	 * @return the accounts
	 */
	public List<TbAccount> getAccounts() {
		return accounts;
	}
	

	/**
	 * @return the accountList
	 */
	public List<SelectItem> getAccountList() {
		return accountList;
	}

	/**
	 * @param details the details to set
	 */
	public void setDetails(List<TransactionStatus> details) {
		this.details = details;
	}

	/**
	 * @return the details
	 */
	public List<TransactionStatus> getDetails() {
		return details;
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
	 * @param transactionId the transactionId to set
	 */
	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	/**
	 * @return the transactionId
	 */
	public Long getTransactionId() {
		return transactionId;
	}

	/**
	 * @param transactionDac the transactionDac to set
	 */
	public void setTransactionDac(TbTransactionDac transactionDac) {
		this.transactionDac = transactionDac;
	}

	/**
	 * @return the transactionDac
	 */
	public TbTransactionDac getTransactionDac() {
		return transactionDac;
	}

	/**
	 * @param showDac the showDac to set
	 */
	public void setShowDac(boolean showDac) {
		this.showDac = showDac;
	}

	/**
	 * @return the showDac
	 */
	public boolean isShowDac() {
		return showDac;
	}

	/**
	 * @param dateTransaction the dateTransaction to set
	 */
	public void setDateTransaction(String dateTransaction) {
		this.dateTransaction = dateTransaction;
	}

	/**
	 * @return the dateTransaction
	 */
	public String getDateTransaction() {
		return dateTransaction;
	}

	/**
	 * @param companyTransaction the companyTransaction to set
	 */
	public void setCompanyTransaction(String companyTransaction) {
		this.companyTransaction = companyTransaction;
	}

	/**
	 * @return the companyTransaction
	 */
	public String getCompanyTransaction() {
		return companyTransaction;
	}

	/**
	 * @param stationTransaction the stationTransaction to set
	 */
	public void setStationTransaction(String stationTransaction) {
		this.stationTransaction = stationTransaction;
	}

	/**
	 * @return the stationTransaction
	 */
	public String getStationTransaction() {
		return stationTransaction;
	}

	/**
	 * @param laneTransaction the laneTransaction to set
	 */
	public void setLaneTransaction(Long laneTransaction) {
		this.laneTransaction = laneTransaction;
	}

	/**
	 * @return the laneTransaction
	 */
	public Long getLaneTransaction() {
		return laneTransaction;
	}

	/**
	 * @param cateTransaction the cateTransaction to set
	 */
	public void setCateTransaction(Long cateTransaction) {
		this.cateTransaction = cateTransaction;
	}

	/**
	 * @return the cateTransaction
	 */
	public Long getCateTransaction() {
		return cateTransaction;
	}
	
	public void hideModal(){
		this.setShowDac(false);
		this.setDateTransaction("");
		this.setCompanyTransaction("");
		this.setStationTransaction("");
		this.setLaneTransaction(0L);
		this.setTransactionDac(null);
		this.setShowMessage1(false);
		this.setMessageModal("");
		this.setShowModal(false);
	}

	/**
	 * @param showMessage1 the showMessage1 to set
	 */
	public void setShowMessage1(boolean showMessage1) {
		this.showMessage1 = showMessage1;
	}

	/**
	 * @return the showMessage1
	 */
	public boolean isShowMessage1() {
		return showMessage1;
	}

	/**
	 * @param messageModal the messageModal to set
	 */
	public void setMessageModal(String messageModal) {
		this.messageModal = messageModal;
	}

	/**
	 * @return the messageModal
	 */
	public String getMessageModal() {
		return messageModal;
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
	 * @param disabled the disabled to set
	 */
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	/**
	 * @return the disabled
	 */
	public boolean isDisabled() {
		return disabled;
	}
	
	public String ocult(){ 	
	    this.setShowData(false);
	    this.setDisabled(true);
	    return null;
	}
	  

}
