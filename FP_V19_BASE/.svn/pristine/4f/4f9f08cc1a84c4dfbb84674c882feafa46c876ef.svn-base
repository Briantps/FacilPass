package process.images;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import security.UserLogged;
import sessionVar.SessionUtil;
import util.Images;
import util.NotificationsList;
import util.TransactionStatus;
import ejb.Device;
import ejb.Image;
import ejb.Transaction;
import ejb.User;
import ejb.Vehicle;
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
import jpa.TbTransactionState;
import jpa.TbVehicle;

public class ReviewManualTransactionBean implements Serializable{

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
	
	@EJB(mappedName="ejb/Image")
	private Image image;
	
	@EJB(mappedName="ejb/Vehicle")
	private Vehicle vehicle;
	
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
	
	private Long def=new Long(-1);
	
	private Long def3=new Long(-3);
	
    private String dateTransaction;
    
    private Long laneTransaction;
    
    private Long lane;
    
    private List<Images> images;
	
    private boolean showImages;
    
    private String path;
    
    private List<String> imageString;
    
    private Long imageId;
    
    private Date date;
    
    private String notification=null;
    
    private Long typeId=-1L;
    
    private List<SelectItem> types;
    
    private boolean showNotification;
    
    private String messageNotification;
    
    
    private boolean showDesc;
    
    private String name=null;
    
    private Long transactionId2;
    
    private List<NotificationsList> listNotifications;
    
    private boolean showListNotifications;
    
    private boolean showMessageNotification;
    
    private boolean showModal;
    
    private String messageModal;
    
    private boolean disabled;
    
    private String plac;
    
    private String plate;
    
    private String brand;
    
    private String color;
    
    private Long category;
    
    private TbVehicle tbVehicle;
    
    private String document;
    
    private UserLogged usrs;
    
    private boolean noPhotos;
    
    private String newPlate;
    
    private Long newCate;
    
    private String tag;
    
    private boolean showModalInfo;
    
    private List<String> listInfo;
    
    private String corfirmMessage;
    
    private boolean showConfirmation;
    
    private boolean showMessageC;
    
    
	public ReviewManualTransactionBean(){
		super();
		initDate= new Date();
		endDate= new Date();
		date=new Date();
		accountList= new ArrayList<SelectItem>();
		accountList.add(new SelectItem(-1,"--Seleccione Cuenta--"));
		details= new ArrayList<TransactionStatus>();
		listNotifications= new ArrayList<NotificationsList>();
		this.setName(null);
		usrs =  (UserLogged) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
	}

	@PostConstruct
	public void init(){
		getUsrs();
	}
	
	public void getUsrs(){
		document=user.getSystemUser(usrs.getUserId()).getUserCode();
		
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
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}
	

	/**
	 * @param transactionId2 the transactionId2 to set
	 */
	public void setTransactionId2(Long transactionId2) {
		this.transactionId2 = transactionId2;
	}

	/**
	 * @return the transactionId2
	 */
	public Long getTransactionId2() {
		return transactionId2;
	}

	/**
	 * @param listNotifications the listNotifications to set
	 */
	public void setListNotifications(List<NotificationsList> listNotifications) {
		this.listNotifications = listNotifications;
	}

	/**
	 * @return the listNotifications
	 */
	public List<NotificationsList> getListNotifications() {
		return listNotifications;
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
		laneList.add(new SelectItem(-1, "--Seleccione Carril--"));
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
		if(accountId!=null && !accountId.equals(def)){
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
			deviceList.add(new SelectItem(-1, "--Seleccione Dispositivo--"));
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
							this.setMessageNotification("No se encontraron resultados.");
							this.setShowNotification(true);
						}
					}
					else{
						this.setMessageModal("No se encontraron resultados."  );
						this.setShowModal(true);
					}
				}
				else{
					this.setMessageModal("Error: El campo concesión es requerido.");
					this.setShowModal(true);
				}
			}
			else{
				this.setMessageModal("Error: La fecha fin no debe ser menor a la fecha inicio.");
				this.setShowModal(true);
			}
		}
		else{
			this.setMessageModal("Error: El campo fecha inicio y fecha fin no deben estar vacíos.");
			this.setShowModal(true);
		}
		
		return null;
	}
	
	public String getPictures3(){

		System.out.println("fecha de transacción" + dateTransaction);
		SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
		Date day;
		try {
			lane= transaction.getLaneByTransaction(transactionId);
			System.out.println("transactionId: " +transactionId);
			Object[] ob= transaction.getDeviceByTransaction(transactionId);
			
			if(ob!=null){
				tag = ob[0]!=null? ob[0].toString():"";
			    plate= ob[1]!=null?ob[1].toString():"-";
			    category= ob[2]!=null?Long.parseLong(ob[2].toString()):0L;
			    
			    newPlate= ob[1]!=null?ob[1].toString():"-";
			    newCate= ob[2]!=null?Long.parseLong(ob[2].toString()):0L;
			}
			else{
				tag ="";
				plate= "-";
			    category= 0L;
			    
			    newPlate= "-";
			    newCate= 0L;
			}
			
			String d=dateTransaction.substring(0, 10);
			day = sdf.parse(d);

			String fec= sdf.format(day); 
			fec=fec.replace("/", "");
			fec=fec.trim();
			
			String hour= dateTransaction.substring(11,19);
			hour=hour.replace(":", "");
			hour=hour.trim();
			
			System.out.println("d: " + d);
			System.out.println("dia: " + day);
			System.out.println("fec: " + fec);
			System.out.println("hora: " + hour);
			String url= "localhost/webdav/" +lane+"/"+fec+"/"+hour+"/";
			//String url1= "c:\\properties\\pictures\\"+lane+"\\"+fec+"\\"+hour+"\\";
			//String url1="\\\\127.0.0.1\\properties\\pictures\\"+lane+"\\"+fec+"\\"+hour+"\\";
			String url1=lane+"\\"+fec+"\\"+hour+"\\";
			
			System.out.println("url: " + url);
			images= image.getImages(url1, lane+"/"+fec+"/"+hour+"/");

			if(images!=null){
				this.setNoPhotos(false);
			}
			else{
				this.setNoPhotos(true);
			}
            this.setShowImages(true);
		
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		
		return null;
	}
	
	public void change(){
		this.setShowConfirmation(false);
		this.setShowImages(false);
		System.out.println("showMessageC al inicio:  " +  showMessageC);
		System.out.println("Entre a realizar cambio en el tag");
		System.out.println("plate: " + plate);
		System.out.println("category: " + category);
		System.out.println("newPlate: " + newPlate);
		System.out.println("newCate: " + newCate);
		
		int change=0;
		if(plate!=null && newPlate!=null && category!=null && newCate!=null){
			if(!plate.equals("") && !newPlate.equals("")){
				if(!plate.toUpperCase().equals(newPlate.toUpperCase()) || !category.equals(newCate)){
					newPlate=newPlate.toUpperCase();
					change=1;
				}
			}
		}
		
		if(change==1){
			Long res= transaction.isEnrollVehicleOtherDevice(tag, newPlate);
			if(res!=null && !res.equals(2L)){
				System.out.println("--1");
				if(res!=null && !res.equals(0L)){
					System.out.println("--2");
					if(transaction.changeInfoTag(tag, newPlate, newCate,SessionUtil.sessionUser()
							.getUserId(), SessionUtil.ip(), res)){
						System.out.println("--3");
						this.setMessageModal("Cambio realizado correctamente al tag. Continúe con la creación de la notificación.");
						this.setShowMessageC(true);
					}
					else{
						System.out.println("--4");
						this.setMessageModal("Error en transacción.");
						this.setShowMessageC(true);
					}
				}

			    else{
			    	System.out.println("--5");
					this.setCorfirmMessage("La placa se encuentra enrolada a otro tag con estado activo. Desea continuar con el cambio de placa. ");
					this.setShowConfirmation(true);
					//this.setShowModalChange(false);
			    }
			}
			else{
				System.out.println("--6");
				this.setMessageModal("Error en transacción.");
				this.setShowMessageC(true);
			}
		}
		else{

			System.out.println("--7");
			this.setMessageModal("No se realizó ninguna modificación en los datos.");
			this.setShowMessageC(true);
			System.out.println("showMessageC:  " +  showMessageC);
		}

	
	}
	
	public void changeOldTag(){
		if(transaction.changeInfoTagReplace(tag, newPlate, newCate,SessionUtil.sessionUser()
				.getUserId(), SessionUtil.ip())){
			this.setMessageModal("Cambio realizado correctamente al tag. Continúe con la creación de la notificación.");
			this.setShowMessageC(true);
			this.setShowConfirmation(false);
		}
		else{
			this.setMessageModal("Error en transacción.");
			this.setShowMessageC(true);
			this.setShowConfirmation(false);
		}
	}
	
	
	public void showInfo(){
		listInfo= image.getInfoTransactionValidate(transactionId2);
		this.setShowModalInfo(true);
	}
	
	public String saveValidation(){
		try{

			System.out.println("name" + name);
			System.out.println("usrs" + user);
			System.out.println("transactionId" + transactionId);
			System.out.println("date en bean" + date);
			System.out.println("cliente" + idClient);
			System.out.println("fecha tran" + dateTransaction);
			System.out.println("----------------------");
			System.out.println("initDate" + initDate);
			System.out.println("endDate" + endDate);
			System.out.println("conce" + conce);
			System.out.println("stationId" + stationId);
			System.out.println("laneId" + laneId);
			System.out.println("idClient" + idClient);
			
			if(notification!=null){
				if(!notification.equals("")){
					if(image.crateValidation(typeId, notification, usrs.getUserId(), transactionId)){
						details=transaction.getTransaction(initDate, endDate, conce, stationId, laneId, idClient, accountId, deviceId);
						this.setMessageNotification("Se Clasificó Transacción Correctamente.");
						this.setShowNotification(true);
						this.setShowModal(false);
					}else{
						this.setMessageModal("Error al Crear Clasificación.");
						this.setShowModal(true);
						this.setShowNotification(false);
					}
				}
				else{
					this.setMessageModal("Error: el campo Observación no puede estar vacío.");
					this.setShowModal(true);
					this.setShowNotification(false);
				}
			
			}
			else{
				this.setMessageModal("Error: el campo Observación no puede estar vacío.");
				this.setShowModal(true);
				this.setShowNotification(false);
			}
		
		}catch(Exception ex){
			ex.printStackTrace();
		}
	
		return null;
	}
	
	public String validar(){
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
		this.setShowImages(false);
		this.setDisabled(false);
		accountList.clear();
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
				this.setShowModal(true);
				this.setMessageModal("Error: No existe información para el cliente digitado.");
				this.setShowData(false);
			}
		}else{
			this.setShowModal(true);
			this.setMessageModal("Error: Digite el número de identificación del cliente.");
			this.setShowData(false);
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

	public void hideModal(){
		this.setShowModal(false);
		this.setMessageModal("");
		this.setShowConfirmation(false);
	}
	
	public void hideModal2(){
		this.setShowMessage1(false);
		this.setShowNotification(false);
		this.setMessageNotification("");
		this.setNotification("");
		this.setTypeId(0L);
		this.setShowImages(false);
		this.setShowListNotifications(false);
		this.setName(null);
		this.setMessageModal("");
		this.setShowModal(false);
		this.setTbVehicle(null);
		this.setPlate(null);
		this.setPlac("");
		this.setBrand(null);
		this.setCategory(null);
		this.setColor("");
		this.setShowConfirmation(false);

	}
	
	public void hideModalC(){
		this.setShowMessageC(false);
		this.setMessageModal("");
		Object[] ob= transaction.getDeviceByTransaction(transactionId);
		
		if(ob!=null){
			tag = ob[0]!=null? ob[0].toString():"";
		    plate= ob[1]!=null?ob[1].toString():"-";
		    category= ob[2]!=null?Long.parseLong(ob[2].toString()):0L;
		    
		    newPlate= ob[1]!=null?ob[1].toString():"-";
		    newCate= ob[2]!=null?Long.parseLong(ob[2].toString()):0L;
		}
		else{
			tag ="";
			plate= "-";
		    category= 0L;
		    
		    newPlate= "-";
		    newCate= 0L;
		}
		this.setShowModalInfo(false);
		this.setShowConfirmation(false);
		this.setShowImages(true);
		
	}
	
	public void hideModalI(){
		this.setShowModalInfo(false);
		this.setShowMessageC(false);
		this.setShowImages(false);
		this.setShowConfirmation(false);
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
	 * @param lane the lane to set
	 */
	public void setLane(Long lane) {
		this.lane = lane;
	}

	/**
	 * @return the lane
	 */
	public Long getLane() {
		return lane;
	}

	/**
	 * @param showImages the showImages to set
	 */
	public void setShowImages(boolean showImages) {
		this.showImages = showImages;
	}

	/**
	 * @return the showImages
	 */
	public boolean isShowImages() {
		return showImages;
	}

	/**
	 * @param images the images to set
	 */
	public void setImages(List<Images> images) {
		this.images = images;
	}

	/**
	 * @return the images
	 */
	public List<Images> getImages() {
		return images;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param imageString the imageString to set
	 */
	public void setImageString(List<String> imageString) {
		this.imageString = imageString;
	}

	/**
	 * @return the imageString
	 */
	public List<String> getImageString() {
		return imageString;
	}

	/**
	 * @param imageId the imageId to set
	 */
	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}

	/**
	 * @return the imageId
	 */
	public Long getImageId() {
		return imageId;
	}


	/**
	 * @param notification the notification to set
	 */
	public void setNotification(String notification) {
		this.notification = notification;
	}

	/**
	 * @return the notification
	 */
	public String getNotification() {

	return notification;
	}

	/**
	 * @param types the types to set
	 */
	public void setTypes(List<SelectItem> types) {
		this.types = types;
	}

	/**
	 * @return the types
	 */
	public List<SelectItem> getTypes() {
		this.setShowDesc(false);
		types= new ArrayList<SelectItem>();
		for (TbTransactionState t : image.getStateTransaction()){
			types.add(new SelectItem(t.getStateId(), t.getNameState()));
		}
      
		return types;
	}

	/**
	 * @param typeId the typeId to set
	 */
	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	/**
	 * @return the typeId
	 */
	public Long getTypeId() {
		return typeId;
	}


	/**
	 * @param showNotification the showNotification to set
	 */
	public void setShowNotification(boolean showNotification) {
		this.showNotification = showNotification;
	}

	/**
	 * @return the showNotification
	 */
	public boolean isShowNotification() {
		return showNotification;
	}

	/**
	 * @param messageNotification the messageNotification to set
	 */
	public void setMessageNotification(String messageNotification) {
		this.messageNotification = messageNotification;
	}

	/**
	 * @return the messageNotification
	 */
	public String getMessageNotification() {
		return messageNotification;
	}

	/**
	 * @param showDesc the showDesc to set
	 */
	public void setShowDesc(boolean showDesc) {
		this.showDesc = showDesc;
	}

	/**
	 * @return the showDesc
	 */
	public boolean isShowDesc() {
		return showDesc;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param showListNotifications the showListNotifications to set
	 */
	public void setShowListNotifications(boolean showListNotifications) {
		this.showListNotifications = showListNotifications;
	}

	/**
	 * @return the showListNotifications
	 */
	public boolean isShowListNotifications() {
		return showListNotifications;
	}

	/**
	 * @param showMessageNotification the showMessageNotification to set
	 */
	public void setShowMessageNotification(boolean showMessageNotification) {
		this.showMessageNotification = showMessageNotification;
	}

	/**
	 * @return the showMessageNotification
	 */
	public boolean isShowMessageNotification() {
		return showMessageNotification;
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

    public String ocult(){
    	
    	this.setShowData(false);
    	this.setDisabled(true);
    	return null;
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

	/**
	 * @param plac the plac to set
	 */
	public void setPlac(String plac) {
		this.plac = plac;
	}

	/**
	 * @return the plac
	 */
	public String getPlac() {
		return plac;
	}

	/**
	 * @param plate the plate to set
	 */
	public void setPlate(String plate) {
		this.plate = plate;
	}

	/**
	 * @return the plate
	 */
	public String getPlate() {
		return plate;
	}

	/**
	 * @param brand the brand to set
	 */
	public void setBrand(String brand) {
		this.brand = brand;
	}

	/**
	 * @return the brand
	 */
	public String getBrand() {
		return brand;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(Long category) {
		this.category = category;
	}

	/**
	 * @return the category
	 */
	public Long getCategory() {
		return category;
	}


	/**
	 * @param document the document to set
	 */
	public void setDocument(String document) {
		this.document = document;
	}

	/**
	 * @return the document
	 */
	public String getDocument() {
		return document;
	}
	

	public String validate(){
		try{
			if(plac!=null){
				if(this.isPlacValid()){
					this.setShowModal(false);
		            tbVehicle=vehicle.getVehicleByPlate(plac.toUpperCase());
		            
		            if(tbVehicle!=null){
		            	plate= tbVehicle.getVehiclePlate();
		            	brand= tbVehicle.getTbBrand().getBrandName();
		            	color= tbVehicle.getTbColor().getColorName();
		            	category= tbVehicle.getTbCategory().getCategoryId();
		            }
		            else{
		            	this.setMessageModal("Error: No se encontró vehiculo con la placa digitada");
		    			this.setShowModal(true);
		            }
				}
				else{
					this.setMessageModal("Error: Digite una placa valida.");
	    			this.setShowModal(true);
				}
			}
			else{
				this.setMessageModal("Digite una placa");
				this.setShowModal(true);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return null;
	}

	/**
	 * @param noPhotos the noPhotos to set
	 */
	public void setNoPhotos(boolean noPhotos) {
		this.noPhotos = noPhotos;
	}

	/**
	 * @return the noPhotos
	 */
	public boolean isNoPhotos() {
		return noPhotos;
	}

	/**
	 * @param tbVehicle the tbVehicle to set
	 */
	public void setTbVehicle(TbVehicle tbVehicle) {
		this.tbVehicle = tbVehicle;
	}

	/**
	 * @return the tbVehicle
	 */
	public TbVehicle getTbVehicle() {
		return tbVehicle;
	}
	
	private boolean isPlacValid(){
		Pattern p = Pattern.compile("[a-zA-Z]{2}[0-9]{4}");
	    Matcher m = p.matcher(plac);
	    
	    if(m.matches()){
	    	return true;
	    }else{
	    	p = Pattern.compile("[a-zA-Z]{3}[0-9]{3}");
	    	m = p.matcher(plac);
		    return m.matches();
	    }	  
	}

	public void setNewPlate(String newPlate) {
		this.newPlate = newPlate;
	}

	public String getNewPlate() {
		return newPlate;
	}

	public void setNewCate(Long newCate) {
		this.newCate = newCate;
	}

	public Long getNewCate() {
		return newCate;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getTag() {
		return tag;
	}

	public void setShowModalInfo(boolean showModalInfo) {
		this.showModalInfo = showModalInfo;
	}

	public boolean isShowModalInfo() {
		return showModalInfo;
	}

	public void setListInfo(List<String> listInfo) {
		this.listInfo = listInfo;
	}

	public List<String> getListInfo() {
		return listInfo;
	}

	public void setCorfirmMessage(String corfirmMessage) {
		this.corfirmMessage = corfirmMessage;
	}

	public String getCorfirmMessage() {
		return corfirmMessage;
	}

	public void setShowConfirmation(boolean showConfirmation) {
		this.showConfirmation = showConfirmation;
	}

	public boolean isShowConfirmation() {
		return showConfirmation;
	}

	public void setShowMessageC(boolean showMessageC) {
		this.showMessageC = showMessageC;
	}

	public boolean isShowMessageC() {
		return showMessageC;
	}
	
}
