package process.images;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import security.UserLogged;
import util.Images;
import util.NotificationsList;
import util.TransactionStatus;
import ejb.Device;
import ejb.Image;
import ejb.Transaction;
import ejb.User;
import ejb.crud.Company;
import ejb.crud.Station;
import jpa.ReAccountDevice;
import jpa.TbAccount;
import jpa.TbCodeImagesRejected;
import jpa.TbCodeType;
import jpa.TbCompany;
import jpa.TbLane;
import jpa.TbStationBO;
import jpa.TbSystemUser;
import jpa.TbTransactionDac;

public class ImageRejectedBean implements Serializable{

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
    
    private String notification;
    
    private Long typeId=-1L;
    
    private List<SelectItem> types;
    
    private boolean showNotification;
    
    private String messageNotification;
    
    private String idPhoto=null;
    
    private boolean showDesc;
    
    private String name=null;
    
    private Long transactionId2;
    
    private List<NotificationsList> listNotifications;
    
    private boolean showListNotifications;
    
    private boolean showMessageNotification;
    
    private String messageModal;
    
    private boolean showModal;
    
    private boolean disabled;
    
    
	public ImageRejectedBean(){
		super();
		initDate= new Date();
		endDate= new Date();
		date=new Date();
		accountList= new ArrayList<SelectItem>();
		accountList.add(new SelectItem(-1,"--Seleccione Cuenta--"));
		details= new ArrayList<TransactionStatus>();
		listNotifications= new ArrayList<NotificationsList>();
		this.setIdPhoto(null);
		this.setName(null);
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
	 * @param idPhoto the idPhoto to set
	 */
	public void setIdPhoto(String idPhoto) {
		this.idPhoto = idPhoto;
	}

	/**
	 * @return the idPhoto
	 */
	public String getIdPhoto() {
		return idPhoto;
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
						this.setMessageModal("No se encontraron resultados.");
						this.setShowModal(true);
					}
				}
				else{
					this.setMessageModal("Error: El campo concesión es requerido.");
					this.setShowModal(true);
				}
			}
			else{
				this.setShowData(false);
				this.setMessageModal("Error. La fecha fin no debe ser menor a la fecha inicio.");
				this.setShowModal(true);
			}
		}
		
		else{
			this.setShowData(false);
			this.setMessageModal("Error. El campo fecha inicio y fecha fin no deben estar vacios.");
			this.setShowModal(true);
		}
		
		return null;
	}
	
	public String getPictures(){
		String obj = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idtran");
		String dateT = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("date");
		
		Long tran= Long.parseLong(obj);
		System.out.println("fecha de transacción" + dateT);
		SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yy");
		Date day;
		try {
			lane= transaction.getLaneByTransaction(tran);
			String d=dateT.substring(0, 8);
			day = sdf.parse(d);

			String fec= sdf.format(day); 
			fec=fec.replace("/", "");
			fec=fec.trim();
			
			String hour= dateT.substring(9);
			hour=hour.replace(":", "");
			hour=hour.trim();
			
			System.out.println("d: " + d);
			System.out.println("dia: " + day);
			System.out.println("fec: " + fec);
			System.out.println("hora: " + hour);
			String url= "localhost/webdav/" +lane+"/"+fec+"/"+hour+"/";
			String url1= "c:\\properties\\pictures\\"+lane+"\\"+fec+"\\"+hour+"\\";
			System.out.println("url: " + url);
			images= image.getImages(url1, lane+"/"+fec+"/"+hour+"/");
			if(images!=null){
                this.setShowImages(true);
			}
			else{
				this.setShowImages(false);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		
		return null;
	}
	
	public String getPictures3(){
//		String obj = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idtran");
//		String dateT = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("date");
//		
//		Long tran= Long.parseLong(obj);
		System.out.println("fecha de transacción" + dateTransaction);
		SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
		Date day;
		try {
			lane= transaction.getLaneByTransaction(transactionId);
			String d=dateTransaction.substring(0, 10);
			day = sdf.parse(d);

			String fec= sdf.format(day); 
			fec=fec.replace("/", "");
			fec=fec.trim();
			
			String hour= dateTransaction.substring(11);
			hour=hour.replace(":", "");
			hour=hour.trim();
			
			System.out.println("d: " + d);
			System.out.println("dia: " + day);
			System.out.println("fec: " + fec);
			System.out.println("hora: " + hour);
			String url= "localhost/webdav/" +lane+"/"+fec+"/"+hour+"/";
			//String url1="\\\\127.0.0.1\\properties\\pictures\\"+lane+"\\"+fec+"\\"+hour+"\\";
			String url1=lane+"\\"+fec+"\\"+hour+"\\";
			System.out.println("url: " + url);
			images= image.getImages(url1, lane+"/"+fec+"/"+hour+"/");
			if(images!=null){
				this.setShowModal(false);
                this.setShowImages(true);
			}
			else{
				this.setShowImages(false);
				this.setMessageNotification("No se encontraron imagenes asociadas a la transacción.");
				this.setShowNotification(true);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		
		return null;
	}
	
	public String saveNotification(){
		try{
			//idPhoto = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idPhoto1");
			//name = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("name1");
			UserLogged usrs =  (UserLogged) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
			String user=usrs.getUsername();


			System.out.println("id foto" + idPhoto);
			System.out.println("name" + name);
			System.out.println("usrs" + user);
			System.out.println("transactionId" + transactionId);
			System.out.println("date en bean" + date);
			if(typeId<0L){
				this.setShowModal(true);
	            this.setMessageModal("Error: al crear registro, debe seleccionar un motivo de rechazo");
			}
			else{
				if(idPhoto!=null){
					String message=image.createNotificationRejected(idPhoto, date, name,"SE RECHAZÓ IMAGEN", typeId,user, transactionId);
					this.setShowNotification(true);
					this.setMessageNotification(message);
				}
				else{
					this.setShowModal(true);
					this.setMessageModal("Error: Debe seleccionar una foto");
				}
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	
		return null;
	}
	
	public String getNotifications(){
		try{
			System.out.println("Estoy en getNotifications2()");
			System.out.println("id" + transactionId2);
			
			listNotifications=image.getNotifications2(transactionId2);
			
			if(listNotifications.size()>0){
				this.setShowListNotifications(true);
				this.setShowModal(false);
			}
			else{
				this.setShowListNotifications(false);
				this.setMessageNotification("No se encontraron imagenes rechazadas para esta transacción.");
				this.setShowNotification(true);
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
				this.setMessageModal("Error: No existe información para el cliente digitado.");
				this.setShowModal(true);
			}
		}else{
			this.setMessageModal("Error: Debe digitar el número de identificación del cliente.");
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
		this.setShowImages(false);
		this.setDateTransaction("");
		this.setTransactionDac(null);
		this.setShowMessage1(false);
		this.setIdPhoto(null);
		this.setName(null);
	}
	
	public void hideModal2(){
		this.setShowMessage1(false);
		this.setShowNotification(false);
		this.setMessageNotification("");
		this.setNotification("");
		this.setTypeId(0L);
		this.setShowImages(false);
		this.setShowListNotifications(false);
		this.setIdPhoto(null);
		this.setName(null);
		this.setShowModal(false);
		this.setMessageModal("");
		this.setShowMessageNotification(false);
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
		types.add(new SelectItem(-1, "SELECCIONE"));
		for (TbCodeImagesRejected t : image.getCodesImagesRejected2()){
			types.add(new SelectItem(t.getNotificationErrorId(), t.getDescription()));
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

    public String showNoti(){
    	this.setShowDesc(false);
    	this.setShowMessageNotification(false);
    	if(typeId<=0){
    		this.setShowDesc(false);
    		this.setShowMessageNotification(false);
    	}
    	else{
    		this.setShowDesc(true);
    		this.setShowMessageNotification(true);
    	}
    	return null;
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
