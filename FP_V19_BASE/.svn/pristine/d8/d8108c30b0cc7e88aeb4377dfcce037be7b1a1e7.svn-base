/**
 * 
 */
package process.account;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import constant.DeviceType;

import security.UserLogged;
import sessionVar.SessionUtil;

import jpa.TbAccount;
import jpa.TbDevice;

import ejb.Device;
import ejb.Purchase;
import ejb.Transaction;
import ejb.TypeDistribution;
import ejb.User;

/**
 * @author tmolina
 *
 */
public class DistributionTagBean implements Serializable {
	private static final long serialVersionUID = 8162813547847156018L;

	@EJB(mappedName = "ejb/User")
	private User user;
	
	@EJB(mappedName = "ejb/Device")
	private Device deviceEJB;
	
	@EJB(mappedName = "ejb/Transaction")
	private Transaction transaction;
	
	@EJB(mappedName = "ejb/Purchase")
	private Purchase purchase;
	
	@EJB(mappedName = "ejb/TypeDistribution")
	private TypeDistribution td;
	
	// Attributes --- //
	
	private Long idClientAccount;
	
	private List<SelectItem> clientAccounts;
	
	private List<TbDevice> devicesList;
	
	private Long deviceId;
	
	private Long availableBalance;
	
	private boolean showRechargeWindow;
	
	private Long value;
	
	private String valueTxt;
	
	private boolean showConfirmation;
	
	private String confirmMessage;
	
	// Control --- //
	
	private boolean showData;
		
	private boolean showMessageError;
	
	private String messageError;
	
	private boolean showMessageErrorModal;
	
	private boolean showModal;
	
	private String modalMessage;	
	
	private UserLogged usrs;
	
	/**
	 * Constructor.
	 */
	public DistributionTagBean() {
		usrs = (UserLogged) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
	}
	
	// Actions --- //
	
	/**
	 * 
	 * @return
	 */
	public String init() {
		setShowData(false);
		setIdClientAccount(-1L);
		setShowData(false);
		setMessageError(null);
		hideModal();
		modalMessage = "";
		showModal = false;
		
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public String hideModal() {
		setShowRechargeWindow(false);
		setMessageError(null);
		setShowConfirmation(false);
		setShowModal(false);
		modalMessage = "";
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public String search() {
		if (idClientAccount.longValue() != -1L) {			
			
			if(td.getAccount(idClientAccount.longValue()).getDistributionTypeId().getDistributionTypeId()==3L){
						
				devicesList = deviceEJB.getDevicesByAccountAndType(idClientAccount, DeviceType.TAG);
				setShowData(true);
				
				if (devicesList.size() > 0) {
					
					availableBalance = transaction.getAvailableBalanceAccount(idClientAccount);
					setShowData(true);
					setShowMessageError(false);
				} else {
					modalMessage =("La cuenta no tiene Tags asociados.");
					setShowData(false);
					showModal = (true);
				}
			} else {
				modalMessage =("La cuenta seleccionada está Bloqueada para esta Operación.");
				setShowData(false);
				showModal = (true);
			}
			
		} else {
			modalMessage =("Seleccione una cuenta.");
			setShowData(false);
			showModal = (true);
		}
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public String initRecharge() {
		setShowRechargeWindow(true);
		setValue(null);
		setShowMessageErrorModal(false);
		setMessageError(null);
		showModal = false;
		modalMessage = "";
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public String initConfirm() {
		if((valueTxt.equals("")) || (valueTxt.equals(null))){
			value = 0L;
		} else{
			value = Long.parseLong(valueTxt.replace(".", "").replace(",", ""));
		}
		if(valueTxt.equals("")){
			modalMessage =("Error, No ha digitado un valor para la recarga del dispositivo.");
			showModal = (true);
		}else if(value <= 0L){
			modalMessage =("Error, El valor digitado debe ser mayor a Cero. Verifique.");
			showModal = (true);
		} else if(value.longValue() > transaction.getAvailableBalanceAccount(idClientAccount).longValue()) {
			modalMessage =("Error, El valor digitado es mayor que el saldo disponible. Verifique.");
			showModal = (true);
		} else {
			setShowRechargeWindow(false);
			setShowConfirmation(true);
			setMessageError(null);
			setShowMessageError(false);
			setShowMessageErrorModal(false);
			showModal = false;
			modalMessage = "";
			setConfirmMessage("¿Está seguro de realizar la transacción?");
		}
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public String saveRecharge() {
		setShowConfirmation(false);
		System.out.println(deviceId);
		if (purchase.recharge1(deviceId, value, idClientAccount, SessionUtil
				.ip(), SessionUtil.sessionUser().getUserId(), null, SessionUtil
				.sessionUser().getUserEmail())) {
			modalMessage =("Transacción Exitosa.");
			devicesList = deviceEJB.getDevicesByAccountAndType(idClientAccount, DeviceType.TAG);
			availableBalance = transaction.getAvailableBalanceAccount(idClientAccount);
		} else {
			modalMessage =("Error en Transacción.");
		}
		setShowModal(true);
		return null;
	}
		
	// Getters and Setters --- //

	/**
	 * 
	 */
	public void setIdClientAccount(Long idClientAccount) {
		this.idClientAccount = idClientAccount;
	}

	/**
	 * 
	 * @return
	 */
	public Long getIdClientAccount() {
		return idClientAccount;
	}

	/**
	 * 
	 * @param clientAccounts
	 */
	public void setClientAccounts(List<SelectItem> clientAccounts) {
		this.clientAccounts = clientAccounts;
	}

	/**
	 * 
	 * @return
	 */
	public List<SelectItem> getClientAccounts() {
		Long userId=user.getSystemMasterById(usrs.getUserId());
		if(clientAccounts == null){
			clientAccounts = new ArrayList<SelectItem>();
		} else {
			clientAccounts.clear();
		}
		clientAccounts.add(new SelectItem(-1L, "--Seleccione Una--"));
		for(TbAccount su : user.getClientAccount(userId)){
			clientAccounts.add(new SelectItem(su.getAccountId(), "Cuenta No. " + su.getAccountId()));
		}
		return clientAccounts;
	}

	public void setShowData(boolean showData) {
		this.showData = showData;
	}

	public boolean isShowData() {
		return showData;
	}

	public void setShowMessageError(boolean showMessageError) {
		this.showMessageError = showMessageError;
	}

	public boolean isShowMessageError() {
		return showMessageError;
	}

	public void setMessageError(String messageError) {
		this.messageError = messageError;
	}

	public String getMessageError() {
		return messageError;
	}

	public void setDevicesList(List<TbDevice> devicesList) {
		this.devicesList = devicesList;
	}

	public List<TbDevice> getDevicesList() {
		return devicesList;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	public Long getDeviceId() {
		return deviceId;
	}
	
	public String getValueTxt() {
		return valueTxt;
	}

	public void setValueTxt(String valueTxt) {
		this.valueTxt = valueTxt;
	}

	/**
	 * @param availableBalance the availableBalance to set
	 */
	public void setAvailableBalance(Long availableBalance) {
		this.availableBalance = availableBalance;
	}

	/**
	 * @return the availableBalance
	 */
	public Long getAvailableBalance() {
		return availableBalance;
	}

	/**
	 * @param showRechargeWindow the showRechargeWindow to set
	 */
	public void setShowRechargeWindow(boolean showRechargeWindow) {
		this.showRechargeWindow = showRechargeWindow;
	}

	/**
	 * @return the showRechargeWindow
	 */
	public boolean isShowRechargeWindow() {
		return showRechargeWindow;
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
	 * @param showConfirmation the showConfirmation to set
	 */
	public void setShowConfirmation(boolean showConfirmation) {
		this.showConfirmation = showConfirmation;
	}

	/**
	 * @return the showConfirmation
	 */
	public boolean isShowConfirmation() {
		return showConfirmation;
	}

	/**
	 * @param confirmMessage the confirmMessage to set
	 */
	public void setConfirmMessage(String confirmMessage) {
		this.confirmMessage = confirmMessage;
	}

	/**
	 * @return the confirmMessage
	 */
	public String getConfirmMessage() {
		return confirmMessage;
	}

	/**
	 * @param showMessageErrorModal the showMessageErrorModal to set
	 */
	public void setShowMessageErrorModal(boolean showMessageErrorModal) {
		this.showMessageErrorModal = showMessageErrorModal;
	}

	/**
	 * @return the showMessageErrorModal
	 */
	public boolean isShowMessageErrorModal() {
		return showMessageErrorModal;
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

	public void setTd(TypeDistribution td) {
		this.td = td;
	}

	public TypeDistribution getTd() {
		return td;
	}
}
