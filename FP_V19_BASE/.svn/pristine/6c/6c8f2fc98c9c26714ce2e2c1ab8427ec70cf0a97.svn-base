/**
 * 
 */
package process.device;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import sessionVar.SessionUtil;
import validator.Validation;

import ejb.Device;
import ejb.Purchase;
import ejb.User;

import jpa.TbAccount;
import jpa.TbDevice;

/**
 * @author tmolina
 *
 */
public class SavePayment implements Serializable {
	private static final long serialVersionUID = -3099986808739679757L;
	
	@EJB(mappedName = "ejb/User")
	private User user;
	
	@EJB(mappedName = "ejb/Device")
	private Device deviceEJB;
	
	@EJB(mappedName="ejb/Purchase")
	private Purchase purchase;

	// Attributes --- //
	
	private Long idClientAccount;
	
	private List<SelectItem> clientAccounts;
	
	private TbAccount account;
	
	private List<TbDevice> devicesList;
	
	private Long deviceId;
	
	private Long value;
	
	private String valueTxt;
	
	// Modal --- //
	
	private boolean showModal;
	
	private String modalMessage;
	
	private boolean showConfirmation;
	
	private String confirmMessage;
	
	private boolean showData;
	
	private boolean showPayWindow;
	
	private boolean showMessageError;
	
	private String messageError;

	/**
	 * Constructor.
	 */
	public SavePayment() {
	}
	
	// Actions --- //
	
	/**
	 * 
	 */
	public String hideModal() {
		setModalMessage(null);
		setShowModal(false);
		setConfirmMessage(null);
		setShowConfirmation(false);
		setShowPayWindow(false);
		this.setValueTxt("");
		this.setValue(0L);
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public String init() {
		hideModal();
		setShowData(false);
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public String initSavePay() {
		setShowPayWindow(true);
		setMessageError(null);
		setShowMessageError(false);
		setValue(null);
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public String initConfirm() {
		System.out.println("valueTxt en initConfirm: "+ valueTxt);
		if((valueTxt.equals("")) || (valueTxt.equals(null))){
			value = 0L;
			setShowPayWindow(false);
			setShowModal(true);
			setModalMessage("Debe Digitar el valor a pagar.");
		} 

		else if (!Validation.isNumericPuntoYComaNoConsecutive(valueTxt)){
			value = 0L;
			setShowPayWindow(false);
			setShowModal(true);
			setModalMessage("Error: El campo Valor a Pagar solo debe contener números.");
		}
		else{
			value = Long.parseLong(valueTxt.replace(".", "").replace(",", ""));
			
			if(value==0L){
				setShowPayWindow(false);
				setShowModal(true);
				setModalMessage("Error: El valor digitado debe ser mayor a cero. Verifique.");
			}
			
			else if(value.longValue() > account.getAccountBalance().longValue()) {
				setShowPayWindow(false);
				setShowModal(true);
				setModalMessage("Error: El valor digitado es mayor que el saldo disponible. Verifique.");
			}
			else {
				setShowPayWindow(false);
				setShowConfirmation(true);
				setMessageError(null);
				setShowMessageError(false);
				setConfirmMessage("¿Está seguro de realizar la transacción?");
			}
		}
	
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public String savePay() {
		setShowConfirmation(false);
		System.out.println("valueTxt: "+ valueTxt);
		System.out.println("value: " +value);
		if (purchase.savePayment(idClientAccount, deviceId, value, SessionUtil
				.ip(), SessionUtil.sessionUser().getUserId())) {
			setModalMessage("Transacción Exitosa.");
			setShowData(false);
			setIdClientAccount(-1L);
		} else {
			setModalMessage("Error en Transacción.");
		}
		setShowModal(true);
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public String changeAccount() {
		if (idClientAccount.longValue() != -1L) {
			for (TbAccount a : user.getClientAccount(null)) {
				if (a.getAccountId() == idClientAccount.longValue()) {
					account = a;
					devicesList = deviceEJB.getDevicesByAccount(idClientAccount);
					setShowData(true);
					break;
				}
			}
		} else {
			setShowData(false);
		}
		return null;
	}
	
	// Getters and Setters --- //
	
	/**
	 * @param clientAccounts the clientAccounts to set
	 */
	public void setClientAccounts(List<SelectItem> clientAccounts) {
		this.clientAccounts = clientAccounts;
	}

	/**
	 * @return the clientAccounts
	 */
	public List<SelectItem> getClientAccounts() {
		if(clientAccounts == null){
			clientAccounts = new ArrayList<SelectItem>();
		} else {
			clientAccounts.clear();
		}
		clientAccounts.add(new SelectItem(-1L, "--Seleccione Una--"));
		for(TbAccount su : user.getClientAccount(null)){
			String name = su.getTbSystemUser().getUserNames();
			if(su.getTbSystemUser().getTbCodeType().getCodeTypeId().longValue() != 3L){
				name += " " + su.getTbSystemUser().getUserSecondNames();
			}
			clientAccounts.add(new SelectItem(su.getAccountId(), "Cuenta No. "+su.getAccountId()+ " - " +name ));
		}
		return clientAccounts;
	}

	/**
	 * @param idClientAccount the idClientAccount to set
	 */
	public void setIdClientAccount(Long idClientAccount) {
		this.idClientAccount = idClientAccount;
	}

	/**
	 * @return the idClientAccount
	 */
	public Long getIdClientAccount() {
		return idClientAccount;
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
	 * @param showConfirmation the showConfirmation to set
	 */
	public void setShowConfirmation(boolean showConfirmation) {
		this.showConfirmation = showConfirmation;
	}
	
	
	

	public String getValueTxt() {
		return valueTxt;
	}

	public void setValueTxt(String valueTxt) {
		this.valueTxt = valueTxt;
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
	 * @param account the account to set
	 */
	public void setAccount(TbAccount account) {
		this.account = account;
	}

	/**
	 * @return the account
	 */
	public TbAccount getAccount() {
		return account;
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
	 * @param devicesList the devicesList to set
	 */
	public void setDevicesList(List<TbDevice> devicesList) {
		this.devicesList = devicesList;
	}

	/**
	 * @return the devicesList
	 */
	public List<TbDevice> getDevicesList() {
		return devicesList;
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
	 * @param showPayWindow the showPayWindow to set
	 */
	public void setShowPayWindow(boolean showPayWindow) {
		this.showPayWindow = showPayWindow;
	}

	/**
	 * @return the showPayWindow
	 */
	public boolean isShowPayWindow() {
		return showPayWindow;
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
	 * @param showMessageError the showMessageError to set
	 */
	public void setShowMessageError(boolean showMessageError) {
		this.showMessageError = showMessageError;
	}

	/**
	 * @return the showMessageError
	 */
	public boolean isShowMessageError() {
		return showMessageError;
	}

	/**
	 * @param messageError the messageError to set
	 */
	public void setMessageError(String messageError) {
		this.messageError = messageError;
	}

	/**
	 * @return the messageError
	 */
	public String getMessageError() {
		return messageError;
	}
}