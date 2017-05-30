package process.account;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import constant.AccountStateType;

import jpa.TbAccount;
import security.UserLogged;
import sessionVar.SessionUtil;

import ejb.User;

/**
 * 
 * @author ablasquez
 *
 */
public class AccountCloseBean implements Serializable {
	
	private static final long serialVersionUID = 2L;
	
	@EJB(mappedName = "ejb/User")
	private User user;
	
	private List<SelectItem> clientAccounts;
	
	private Long idAccClient;
	
	private String reason;
	
	private boolean showModal;
	
	private String modalMessage;
	
	private String msgError;
	
	private boolean showError;
	
	private UserLogged usrs;

	public AccountCloseBean() {
		setUsrs((UserLogged) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user"));
		init();
	}
	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setClientAccounts(List<SelectItem> clientAccounts) {
		this.clientAccounts = clientAccounts;
	}

	public List<SelectItem> getClientAccounts() {
		Long userId=user.getSystemMasterById(usrs.getUserId());
		if(clientAccounts == null){
			clientAccounts = new ArrayList<SelectItem>();
		} else {
			clientAccounts.clear();
		}
		clientAccounts.add(new SelectItem(-1L, "--Seleccione Una cuenta--"));
		try {
		for(TbAccount su : user.getClientAccount(userId)){
			System.out.println("estado de cuenta "+su.getAccountId()+" estado "+su.getAccountState());
			if((su.getAccountState().longValue()!=AccountStateType.CLOSE.getId().longValue()) && 
			   (su.getAccountState().longValue()!=AccountStateType.PENDING_FOR_CLOSURE.getId().longValue()) &&
			   (su.getAccountState().longValue()!=AccountStateType.TO_CLOSE.getId().longValue())){
				clientAccounts.add(new SelectItem(su.getAccountId(), "Cuenta No. " + su.getAccountId()));
			}
		}	
		} catch (NullPointerException e ){
			e.printStackTrace();
		}
		return clientAccounts;
	}

	public void setIdAccClient(Long idAccClient) {
		this.idAccClient = idAccClient;
	}

	public Long getIdAccClient() {
		return idAccClient;
	}

	public void setShowModal(boolean showModal) {
		this.showModal = showModal;
	}

	public boolean isShowModal() {
		return showModal;
	}

	public void setModalMessage(String modalMessage) {
		this.modalMessage = modalMessage;
	}

	public String getModalMessage() {
		return modalMessage;
	}
	
	public void setMsgError(String msgError) {
		this.msgError = msgError;
	}
	public String getMsgError() {
		return msgError;
	}
	public void setShowError(boolean showError) {
		this.showError = showError;
	}
	public boolean isShowError() {
		return showError;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getReason() {
		return reason;
	}
	public String init(){
		modalMessage = "";
		showModal = false;	
		msgError =("");
		showError = (false);
		this.setIdAccClient(-1L);		
		return null;
	}
	
	public String hideModal() {
		showModal = false;
		return null;
	}
	
	public String hideError(){
		showError = (false);		
		return null;
	}
	public void msgSave(){
		if (getIdAccClient()==-1L){
			msgError =("No ha seleccionado una Cuenta");
			showError = (true);
		} else {
			if(reason.length()>4000){
				modalMessage = "";
				showModal = false;
				msgError =("El motivo de cancelación no debe tener más de 4000 caracteres");
				showError = (true);
			}else{
				msgError =("");
				showError = (false);
				modalMessage = "¿Está seguro de Solicitar el Cierre de la Cuenta:"+idAccClient+"?, si acepta tenga en cuenta que a partir de este momento su(s) vehículo(s) no podrán utilizar el servicio FacilPass";
				showModal = true;
			}
		}
	}
	
	
	
	public void apply(){
		msgError =("");
		showError = (false);
		modalMessage = "";
		showModal = false;
		try{			
			if (user.accountCloseRequest(idAccClient, reason, SessionUtil
					.sessionUser().getUserId(),SessionUtil.ip())){
				msgError =("Transacción Exitosa");
				showError = (true);				
				this.getClientAccounts();
				this.setIdAccClient(-1L);
				reason = "";
				FacesContext context = FacesContext.getCurrentInstance();
				context.getExternalContext().getSessionMap().remove("accountSettingsBean");
			} else {
				msgError =("Se ha Presentado un Error");
				showError = (true);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * @param usrs the usrs to set
	 */
	public void setUsrs(UserLogged usrs) {
		this.usrs = usrs;
	}
	/**
	 * @return the usrs
	 */
	public UserLogged getUsrs() {
		return usrs;
	}	
	
}
