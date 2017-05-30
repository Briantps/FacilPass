package email;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import jpa.TbSystemUser;
import sessionVar.SessionUtil;
import ejb.email.ManagementNotifications;

/**
 * 16/07/2014
 * @author Ximena Blasquez
 * @version 1
 *
 */
public class ManagementNotificationsClientBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@EJB(mappedName="ejb/email/ManagementNotifications")
	private ManagementNotifications notifications;
	
	private boolean showNotifications;
	private boolean showModal;
	private String msgModal;
	private TbSystemUser client;
	private List<util.Notifications> notificationsClient;
	private List<util.Notifications> notificationsClientTemp;
	private List<util.Notifications> notificationsClientViewer;
	private String listAccountsClient;
	private Long userEmailProcessId;
	private boolean showConfirm;
	private String msgConfirm;
	
	public ManagementNotificationsClientBean(){
		notificationsClient = new ArrayList<util.Notifications>();
		notificationsClientTemp = new ArrayList<util.Notifications>();
		showNotifications = false;
		showModal = false;
		msgModal = "";
		System.out.println("Iniciado ManagementNotificationsClientBean");
	}
	
	@PostConstruct
	public void init(){
		client = notifications.getClientByUserId(SessionUtil.sessionUser().getUserId());
		if(client!=null){
			this.searchClient();
			showNotifications = true;			
		}else{
			msgModal="Módulo no disponible";
			showModal=true;
		}
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
		
		if(client!=null){				
				this.setListAccountsClient(notifications.getAccountsByClient(client.getUserId()));
				if(loadNotifications(this.getClient().getUserId())){
					this.setShowNotifications(true);	
				}else{
					this.setMsgModal("Se presentó un error al realizar la carga de las notificaciones");
					this.setShowModal(true);
				}
		}else{
			this.setMsgModal("Error al consultar la información del cliente");
			this.setShowModal(true);
		}
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
			System.out.println("resultado "+resultado);
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
