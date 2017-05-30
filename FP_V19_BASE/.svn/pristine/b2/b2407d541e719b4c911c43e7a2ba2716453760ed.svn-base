package process.account;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import sessionVar.SessionUtil;
import util.AllInfoAccount;

import constant.AccountStateType;

import jpa.TbAccountCloseLog;
import ejb.User;
import ejb.AccountClose;


/**
 * 
 * @author ablasquez
 * @author psanchez
 *
 */
public class ListAccountCloseBean implements Serializable {
	
	private static final long serialVersionUID = 2L;
	
	@EJB(mappedName = "ejb/User")
	private User userEJB;
	
	@EJB(mappedName="ejb/AccountClose")
	private AccountClose accountCloseEJB;
	
	private List<AllInfoAccount> accountCloseList;
	
	private List<TbAccountCloseLog> listAccount;
	
	private Long accountBankId;
	
	private Long account;
	
	private Long bankId;
	
	private String bankAvalPSE;
	
	private Long accountStateType;
	
	private Long stateAccountBank;
	
	private String observ;
	
	private String bankName;
	
	private boolean modalClose;
	
	private boolean showModal;
	
	private String modalMessage;
	
	private String msgError;
	
	private boolean showError;
	
	private boolean modalReactivation;
	
	private String modalMessageReactivation;
	
	private boolean showModalReactivation;
	
	private boolean showModalClose;
	
	private String modalMessageClose;
	
	
	
	public String getBankAvalPSE() {
		return bankAvalPSE;
	}


	public void setBankAvalPSE(String bankAvalPSE) {
		this.bankAvalPSE = bankAvalPSE;
	}


	public void msgSave(){
		msgError =("");
		showError = (false);		
		if(observ.length()>2000){
			msgError ="El campo Observación supera los 2000 caracteres.";
			showError = (true);
		}else{
			modalClose = false;
			modalMessage = "¿Está seguro de Realizar el Cierre de la Cuenta: "+this.getAccount()+"?";
			showModal = true;
		}		
	}
	
	
	public void apply(){
		msgError =("");
		showError = (false);
		modalMessage = "";
		showModal = false;
		modalMessageReactivation = "";
		showModalReactivation = false;
		try{			
			if (userEJB.closeAccount(account, observ, SessionUtil
					.sessionUser().getUserId(),SessionUtil.ip())){
				msgError =("Transacción Exitosa.");
				showError = (true);				
				observ = "";
				showModal = false;
				modalClose = false;
				showModalReactivation = false;
			} else {
				msgError =("Se ha Presentado un Error");
				showError = (true);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void msgNoCancel(){
		msgError =("");
		showError = (false);
		if(observ.length()>2000){
			msgError ="El campo Observación supera los 2000 caracteres.";
			showError = (true);
		}else{
			modalReactivation = false;
			modalMessageReactivation=("¿Está seguro de realizar la reactivación de la Cuenta: "+this.getAccount()+"?");
			showModalReactivation=(true);
		}
	}
	

	
	public void reactivation(){
		msgError =("");
		showError = (false);
		modalMessage = "";
		showModal = false;
		modalMessageReactivation = "";
		showModalReactivation = false;
		modalMessageClose = "";
		showModalClose = false;
		try{			
			if (userEJB.accountReactivation(account, observ, SessionUtil
					.sessionUser().getUserId(),SessionUtil.ip())){
				msgError =("Transacción Exitosa.");
				showError = (true);				
				observ = "";
				showModal = false;
				modalClose = false;
				showModalReactivation = false;
				modalReactivation = false;
			} else {
				msgError =("Se ha Presentado un Error");
				showError = (true);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public void panelCloseAccount(){
		try{
			if(bankAvalPSE.equals("1") && accountStateType==2L){
				modalClose = true;	
		    }else if(bankAvalPSE.equals("0") && accountStateType==2L){
				modalClose = true;	
		    }else if(bankAvalPSE.equals("0") && stateAccountBank==4L){
		    	modalClose = false;	
				showModalClose = true;
				modalMessageClose = "¿Está seguro de autorizar la disociación de la cuenta FacilPass "+this.getAccount()+ " de la entidad "+this.getBankName()+"?";
		    }
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Método encargado de desvincular la cuenta facilpass al producto PSE.
	 * @author psanchez
	 */
	public void accountClose(){
		msgError =("");
		showError = (false);
		modalMessage = "";
		showModal = false;
		modalMessageReactivation = "";
		showModalReactivation = false;
		modalMessageClose = "";
		showModalClose = false;
		
		try{	
			if (accountCloseEJB.closeAccount(account, accountBankId, SessionUtil.ip(),  SessionUtil.sessionUser().getUserId())){
				msgError =("Transacción Exitosa.");
				showError = (true);				
				observ = "";
				showModal = false;
				modalClose = false;
				showModalReactivation = false;
				modalReactivation = false;
			} else {
				msgError =("Se ha Presentado un Error");
				showError = (true);
			}		
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String hideModal() {
		showModal = false;
		showModalReactivation = false;
		showModalClose = false;
		return null;
	}
	
	public String hideError(){
		showError = (false);		
		return null;
	}
	
	public void cancelClose(){
		observ = "";
		account = null;
		modalClose = false;	
	}
	
	public void cancelReactivation(){
		observ = "";
		account = null;
		modalReactivation = false;		
	}
	
	public void panelNoCloseAccount(){
		showModalReactivation = false;
		modalReactivation = true;
	}
	
	public void hideModalReac(){
		modalReactivation = true;
		showModalReactivation = false;
	}

	
	public void setAccount(Long account) {
		this.account = account;
	}

	public Long getAccount() {
		return account;
	}
	
	public void setObserv(String observ) {
		this.observ = observ;
	}

	public String getObserv() {
		return observ;
	}

	public void setModalClose(boolean modalClose) {
		this.modalClose = modalClose;
	}

	public boolean isModalClose() {
		return modalClose;
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

	public void setModalReactivation(boolean modalReactivation) {
		this.modalReactivation = modalReactivation;
	}

	public boolean isModalReactivation() {
		return modalReactivation;
	}

	public void setModalMessageReactivation(String modalMessageReactivation) {
		this.modalMessageReactivation = modalMessageReactivation;
	}

	public String getModalMessageReactivation() {
		return modalMessageReactivation;
	}

	public void setShowModalReactivation(boolean showModalReactivation) {
		this.showModalReactivation = showModalReactivation;
	}

	public boolean isShowModalReactivation() {
		return showModalReactivation;
	}

	public void setAccountCloseEJB(AccountClose accountCloseEJB) {
		this.accountCloseEJB = accountCloseEJB;
	}

	public AccountClose getAccountCloseEJB() {
		return accountCloseEJB;
	}

	/**
	 * @param accountCloseList the accountCloseList to set
	 */
	public void setAccountCloseList(List<AllInfoAccount> accountCloseList) {
		this.accountCloseList = accountCloseList;
	}

	/**
	 * Método encargado de listar las cuentas pendientes de cierre (account_state_type_id=2 tabla tb-account) 
	 * y Pendiente de aprobación de disociación (state_account_bank=4 tabla re-account_bank)
	 * @return the accountCloseList
	 * @author psanchez
	 */
	public List<AllInfoAccount> getAccountCloseList() {
		if(accountCloseList == null) {
			accountCloseList = new ArrayList<AllInfoAccount>();
		}else{
			accountCloseList.clear();
		}
		accountCloseList = accountCloseEJB.getAllAccountClose();
		return accountCloseList;
	}

	public void setListAccount(List<TbAccountCloseLog> listAccount) {
		this.listAccount = listAccount;
	}

	public List<TbAccountCloseLog> getListAccount() {	
		if(listAccount == null) {
			listAccount = new ArrayList<TbAccountCloseLog>();
		} else {
			listAccount.clear();
		}
		listAccount = userEJB.getAccountPendingClose(AccountStateType.PENDING_FOR_CLOSURE);
				
		return listAccount;
	}

	/**
	 * @param showModalClose the showModalClose to set
	 */
	public void setShowModalClose(boolean showModalClose) {
		this.showModalClose = showModalClose;
	}

	/**
	 * @return the showModalClose
	 */
	public boolean isShowModalClose() {
		return showModalClose;
	}

	/**
	 * @param modalMessageClose the modalMessageClose to set
	 */
	public void setModalMessageClose(String modalMessageClose) {
		this.modalMessageClose = modalMessageClose;
	}

	/**
	 * @return the modalMessageClose
	 */
	public String getModalMessageClose() {
		return modalMessageClose;
	}

	/**
	 * @param bankName the bankName to set
	 */
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	/**
	 * @return the bankName
	 */
	public String getBankName() {
		return bankName;
	}

	/**
	 * @param accountStateType the accountStateType to set
	 */
	public void setAccountStateType(Long accountStateType) {
		this.accountStateType = accountStateType;
	}


	/**
	 * @return the accountStateType
	 */
	public Long getAccountStateType() {
		return accountStateType;
	}


	/**
	 * @param stateAccountBank the stateAccountBank to set
	 */
	public void setStateAccountBank(Long stateAccountBank) {
		this.stateAccountBank = stateAccountBank;
	}

	/**
	 * @return the stateAccountBank
	 */
	public Long getStateAccountBank() {
		return stateAccountBank;
	}

	/**
	 * @param accountBankId the accountBankId to set
	 */
	public void setAccountBankId(Long accountBankId) {
		this.accountBankId = accountBankId;
	}

	/**
	 * @return the accountBankId
	 */
	public Long getAccountBankId() {
		return accountBankId;
	}


	/**
	 * @param bankId the bankId to set
	 */
	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}


	/**
	 * @return the bankId
	 */
	public Long getBankId() {
		return bankId;
	}
}
