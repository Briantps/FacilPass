package email;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import jpa.TbCodeType;
import jpa.TbSystemUser;
import sessionVar.SessionUtil;
import validator.Validation;
import ejb.User;
import ejb.email.ManagementNotifications;

/**
 * 14/07/2014
 * @author Ximena Blasquez
 * @version 1
 *
 */
public class ManagementNotificationsBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@EJB(mappedName="ejb/User")
	private User userEJB;
	
	@EJB(mappedName="ejb/email/ManagementNotifications")
	private ManagementNotifications notifications;
	
	private List<SelectItem> listTypeDocument;
	private Long codeTypeDocument;
	private String documentClient;
	private String nameClient;
	private String lastNameClient;
	private String emailClient;
	private Long accountClient;
	private Long userEmailProcessId;	
	
	
	private boolean showNotifications;
	private boolean showModal;
	private String msgModal;
	private TbSystemUser client;
	private List<util.Notifications> notificationsClient;
	private List<util.Notifications> notificationsClientTemp;
	private List<util.Notifications> notificationsClientViewer;
	private String listAccountsClient;
	private boolean showConfirm;
	private String msgConfirm;
	
	public ManagementNotificationsBean(){
		notificationsClient = new ArrayList<util.Notifications>();
		notificationsClientTemp = new ArrayList<util.Notifications>();
		showNotifications = false;
		showModal = false;
		msgModal = "";
		showConfirm = false;
		System.out.println("Iniciado ManagementNotificationsBean");
	}
	
	public List<SelectItem> getListTypeDocument() {
		if(listTypeDocument == null){
			listTypeDocument = new ArrayList<SelectItem>();
			listTypeDocument.add(new SelectItem(-1L,"-Seleccione Uno-"));
			
			for (TbCodeType ct : userEJB.getCodeTypes()){
				listTypeDocument.add(new SelectItem(ct.getCodeTypeId(),ct.getCodeTypeAbbreviation().toUpperCase()));
			}
		}
		return listTypeDocument;
	}

	public void setListTypeDocument(List<SelectItem> listTypeDocument) {
		this.listTypeDocument = listTypeDocument;
	}

	public Long getCodeTypeDocument() {
		return codeTypeDocument;
	}

	public void setCodeTypeDocument(Long codeTypeDocument) {
		this.codeTypeDocument = codeTypeDocument;
	}

	public String getDocumentClient() {
		return documentClient;
	}

	public void setDocumentClient(String documentClient) {
		this.documentClient = documentClient;
	}

	public String getNameClient() {
		return nameClient;
	}

	public void setNameClient(String nameClient) {
		this.nameClient = nameClient;
	}

	public String getLastNameClient() {
		return lastNameClient;
	}

	public void setLastNameClient(String lastNameClient) {
		this.lastNameClient = lastNameClient;
	}

	public String getEmailClient() {
		return emailClient;
	}

	public void setEmailClient(String emailClient) {
		this.emailClient = emailClient;
	}

	public Long getAccountClient() {
		return accountClient;
	}

	public void setAccountClient(Long accountClient) {
		this.accountClient = accountClient;
	}

	public boolean isShowNotifications() {
		return showNotifications;
	}

	public void setShowNotifications(boolean showNotifications) {
		this.showNotifications = showNotifications;
	}

	public boolean isShowModal() {
		return showModal;
	}

	public void setShowModal(boolean showModal) {
		this.showModal = showModal;
	}

	public String getMsgModal() {
		return msgModal;
	}

	public void setMsgModal(String msgModal) {
		this.msgModal = msgModal;
	}
	
	public void setClient(TbSystemUser client) {		
		this.client = client;
	}

	public TbSystemUser getClient() {
		return client;
	}

	

	public void setListAccountsClient(String listAccountsClient) {
		this.listAccountsClient = listAccountsClient;
	}

	public String getListAccountsClient() {
		return listAccountsClient;
	}

	

	public void setNotificationsClient(List<util.Notifications> notificationsClient) {
		this.notificationsClient = notificationsClient;
	}

	public List<util.Notifications> getNotificationsClient() {
		return notificationsClient;
	}

	public void setNotificationsClientTemp(List<util.Notifications> notificationsClientTemp) {
		this.notificationsClientTemp = notificationsClientTemp;
	}

	public List<util.Notifications> getNotificationsClientTemp() {
		return notificationsClientTemp;
	}

	public void setNotificationsClientViewer(
			List<util.Notifications> notificationsClientViewer) {
		this.notificationsClientViewer = notificationsClientViewer;
	}

	public List<util.Notifications> getNotificationsClientViewer() {
		return notificationsClientViewer;
	}

	public void setUserEmailProcessId(Long userEmailProcessId) {
		System.out.println("Entre a userEmailProcessId");
		this.userEmailProcessId = userEmailProcessId;
	}

	public Long getUserEmailProcessId() {
		return userEmailProcessId;
	}

	public void setShowConfirm(boolean showConfirm) {
		this.showConfirm = showConfirm;
	}

	public boolean isShowConfirm() {
		return showConfirm;
	}

	public void setMsgConfirm(String msgConfirm) {
		this.msgConfirm = msgConfirm;
	}

	public String getMsgConfirm() {
		return msgConfirm;
	}

	public void searchClient(){
		showNotifications= false;
		if(emailClient!=null){
			emailClient = emailClient.toLowerCase();
		}
		String validations = this.validates();
		System.out.println(validations);
		if(validations.equals("")){
			String[] filters = new String[6];
			filters[0] = String.valueOf(codeTypeDocument);
			filters[1] = documentClient;
			filters[2] = nameClient;
			filters[3] = lastNameClient;
			filters[4] = emailClient;
			if(accountClient!=null){
				filters[5] = String.valueOf(accountClient);
			}else{
				filters[5] = null;
			}
			
			
			long n = notifications.validateFilters(filters);
			if(n==0){
				this.setMsgModal("No se encontraron resultados para los criterios de busqueda ingresados");
				this.setShowModal(true);
			}else if(n>1){
				this.setMsgModal("Se obtuvo mas de un resultado. Por favor ingrese otro criterio de busqueda");
				this.setShowModal(true);
			}else if(n==-1){
				this.setMsgModal("Se presentó un error al realizar la busqueda");
				this.setShowModal(true);
			}else{
				this.setClient(notifications.getClientByFilters(filters));
				this.setListAccountsClient(notifications.getAccountsByClient(this.getClient().getUserId()));
				if(loadNotifications(this.getClient().getUserId())){
					this.setShowNotifications(true);	
				}else{
					this.setMsgModal("Se presentó un error al realizar la carga de las notificaciones");
					this.setShowModal(true);
				}
			}
		}else{
			this.setMsgModal(validations);
			this.setShowModal(true);
		}
	}
	
	public String validates(){		
		String message="";
		System.out.println("documentClient: "+documentClient);
		System.out.println("nameClient: "+nameClient);
		System.out.println("lastNameClient: "+lastNameClient);
		System.out.println("emailClient: "+emailClient);
		
		if(documentClient!=null && !documentClient.trim().equals("")){
			if(!Validation.isNumeric(documentClient)){
				message=message+" El campo No. Identificación contiene caracteres inválidos.";				
			}
		}
		if(nameClient!=null && !nameClient.trim().equals("")){
			if(!Validation.isAlphaES(nameClient)){
				message=message+" El campo Nombre contiene caracteres inválidos.";				
			}
		}
		
		if(lastNameClient!=null && !lastNameClient.trim().equals("")){
			if(!Validation.isAlphaES(lastNameClient)){
				message=message+" El campo Apellidos contiene caracteres inválidos.";				
			}
		}
		
		if(emailClient!=null && !emailClient.trim().equals("")){
			if(!Validation.isEmail(emailClient)){
				message=message+" El campo Usuario contiene caracteres inválidos.";				
			}
		}
		
		return message;
	}
	
	public void clean(){
		codeTypeDocument = -1L;
		documentClient = null;
		nameClient = null;
		lastNameClient = null;
		emailClient = null;
		accountClient = null;
		listAccountsClient = null;
		this.setShowNotifications(false);
		showConfirm = false;
	}
	
	public void saveNotifications(){
		boolean change = false;
		List<util.Notifications> notificationsModif = new ArrayList<util.Notifications>();
		for (int i = 0; i < notificationsClient.size(); i++){
			util.Notifications n = notificationsClient.get(i);
			if(n.isActive() != notificationsClientTemp.get(i).isActive()){
				notificationsModif.add(n);
				change = true;				
			}
		}
		
		if(change){
			Long resultado = notifications.saveNotifications(notificationsModif,SessionUtil.sessionUser().getUserId(),SessionUtil.ip());
			if(resultado!=null){
				if(resultado==0L){
					this.setMsgModal("No se realizaron cambios en las notificaciones");
					this.setShowModal(true);
				}else{
					this.setMsgModal("Se han guardado las notificaciones exitosamente");
					this.setShowModal(true);
				}
			}else{
				System.out.println("Se presentó un error al momento de guardar las notificaciones para el cliente "+this.getClient().getUserId());
				this.setMsgModal("Se presentó un error al momento de guardar las notificaciones");
				this.setShowModal(true);
				loadNotifications(this.getClient().getUserId());
			}
		}else{
			this.setMsgModal("No se realizaron cambios en las notificaciones");
			this.setShowModal(true);
		}
	}
	
	public void hideModal(){
		this.setShowModal(false);
		this.setMsgModal("");
	}
	
	public boolean loadNotifications(Long clientId){
		showConfirm = false;
		notificationsClient.clear();
		if(clientId!=null){
			notificationsClient = notifications.getListNotificationsByClient(clientId);
			notificationsClientTemp = notifications.getListNotificationsByClient(clientId);
			notificationsClientViewer =  notifications.getListNotificationsByClient(clientId);
			return true;
		}else{
			return false;
		}			
	}
	
	public void changeNotification(){
		boolean estadoActual = false;
		boolean estadoNuevo = false;
		String notfName = "";
		
		System.out.println("Ingrese a changeNotification");
		
		System.out.println("userEmailProcessId: "+userEmailProcessId);
		for(util.Notifications nT : notificationsClientViewer){
			System.out.println("nt.getUserEmail().getUserEmailProcessId(): "+nT.getUserEmail().getUserEmailProcessId());
			if(nT.getUserEmail().getUserEmailProcessId().longValue()==userEmailProcessId.longValue()){
				estadoActual = nT.isActive();
				break;
			}
		}
		
		System.out.println("userEmailProcessId: "+userEmailProcessId);
		for(util.Notifications n : notificationsClient){
			System.out.println("n.getUserEmail().getUserEmailProcessId(): "+n.getUserEmail().getUserEmailProcessId());
			if(n.getUserEmail().getUserEmailProcessId()==userEmailProcessId){
				estadoNuevo = n.isActive();
				notfName = n.getUserEmail().getTbEmailProcess().getEmailProcessName();
				break;
			}
		}
				
		
		System.out.println("estadoActual: "+estadoActual);
		System.out.println("estadoNuevo: "+estadoNuevo);
		
		if((estadoActual==true) && (estadoNuevo==false)){
			System.out.println("Desactivación de notificacion "+notfName);
			
			msgConfirm="Está seguro de desactivar esta notificación";
			showConfirm = true;
		}else if((estadoActual==false) && (estadoNuevo==true)){
			System.out.println("Activación de notificacion "+notfName);
			msgConfirm="Está seguro de activar esta notificación";
			showConfirm = true;
		}
	}
	
	public void activeCheck(){
		boolean estadoNuevo = false;
		System.out.println("userEmailProcessId [activeCheck]: "+userEmailProcessId);
		for(util.Notifications n : notificationsClient){
			System.out.println("n.getUserEmail().getUserEmailProcessId(): "+n.getUserEmail().getUserEmailProcessId());
			if(n.getUserEmail().getUserEmailProcessId().longValue()==userEmailProcessId.longValue()){
				estadoNuevo = n.isActive();			
				break;
			}
		}		
		for(util.Notifications nT : notificationsClientViewer){
			System.out.println("nt.getUserEmail().getUserEmailProcessId(): "+nT.getUserEmail().getUserEmailProcessId());
			if(nT.getUserEmail().getUserEmailProcessId().longValue()==userEmailProcessId.longValue()){
				nT.setActive(estadoNuevo);				
				break;
			}
		}
		showConfirm = false;
	}
	
	public void noActiveCheck(){		
		System.out.println("userEmailProcessId [noActiveCheck]: "+userEmailProcessId);
		for(util.Notifications n : notificationsClient){
			System.out.println("n.getUserEmail().getUserEmailProcessId(): "+n.getUserEmail().getUserEmailProcessId());
			if(n.getUserEmail().getUserEmailProcessId().longValue()==userEmailProcessId.longValue()){
				if(n.isActive()){
					n.setActive(false);					
				}
				else{
					n.setActive(true);
				}
				break;
			}
		}
		showConfirm = false;
	}
}
