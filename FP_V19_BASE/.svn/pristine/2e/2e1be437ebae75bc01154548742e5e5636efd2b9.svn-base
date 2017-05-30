/**
 * 
 */
package crud;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import sessionVar.SessionUtil;

import ejb.crud.BankAccount;
import ejb.crud.BankAccountClient;

import jpa.TbBankAccount;
import jpa.TbProductType;

/**
 * Bean to Manage the administration of Bank Accounts.
 * @author tmolina
 *
 */
public class BankAccountBean implements Serializable {
	private static final long serialVersionUID = 731945118516042526L;

	@EJB(mappedName="ejb/BankAccount")
	private BankAccount bank;
	
	@EJB(mappedName="ejb/BankAccountClient")
	private BankAccountClient bankAccount;
	
	// Attributes.
	
	private boolean showModal;
	
	private String modalMessage;
	
	private List<TbBankAccount> bankAccountList;
	
	private boolean showInsert;
	
	private String bankAccountHolder;
	
	private String bankAccountHolderNit;
	
	private String product;
	
	private String bankAccountNumber;
	
	private Long bankAccountType;
	
	private List<SelectItem> bankAccountTypes;

	/**
	 * Constructor.
	 */
	public BankAccountBean() {
		System.out.println(" [Entre aqui ] ");
	}
	
	// Actions ------------ //
	
	/**
	 * Init Add Bank.
	 */
	public String initAddBank(){
		setShowInsert(true);
	    setBankAccountHolder(null);
	    setBankAccountHolderNit(null);
	    setBankAccountNumber(null);
	    setBankAccountType(null);
	    setProduct(null);
		return null;
	}
	
	/**
	 * Inserts a new  bank account.
	 */
	public String addBank(){
		setShowInsert(false);
	/*	Long result = bank.insertBankAccount(bankAccountHolder, bankAccountHolderNit, SessionUtil.ip(), SessionUtil
						.sessionUser().getUserId(), product, bankAccountNumber,bankAccountType);*/
		
			Long result = bank.insertBankAccount(bankAccountHolder, bankAccountHolderNit, SessionUtil.ip(), SessionUtil
		.sessionUser().getUserId(), product, bankAccountNumber,bankAccountType);
		if (result != null) {
			if(result != -1L){
				setModalMessage("Transacción Exitosa.");
				setBankAccountList(null);
			} else {
				setModalMessage("Ya hay una Cuenta de Banco con ese número de Cuenta. Verifique.");
			}
		} else {
			setModalMessage("Error en Transacción.");
		}
		setShowModal(true);
		return null;
	}
	
	/**
	 * Hides modal windows.
	 */
	public String hideModal(){
		setShowInsert(false);
		setShowModal(false);
		setModalMessage(null);
		return null;
	}
	
	// Getters and Setters ------------ //

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
	 * @param showInsert the showInsert to set
	 */
	public void setShowInsert(boolean showInsert) {
		this.showInsert = showInsert;
	}

	/**
	 * @return the showInsert
	 */
	public boolean isShowInsert() {
		return showInsert;
	}

	/**
	 * @param bankAccountList the bankAccountList to set
	 */
	public void setBankAccountList(List<TbBankAccount> bankAccountList) {
		this.bankAccountList = bankAccountList;
	}

	/**
	 * @return the bankAccountList
	 */
	public List<TbBankAccount> getBankAccountList() {
		if(bankAccountList == null) {
			bankAccountList = new ArrayList<TbBankAccount>();
			bankAccountList = bank.getBankAccounts();
		}
		return bankAccountList;
	}

	/**
	 * @param bankAccountHolder the bankAccountHolder to set
	 */
	public void setBankAccountHolder(String bankAccountHolder) {
		this.bankAccountHolder = bankAccountHolder;
	}

	/**
	 * @return the bankAccountHolder
	 */
	public String getBankAccountHolder() {
		return bankAccountHolder;
	}

	/**
	 * @param bankAccountHolderNit the bankAccountHolderNit to set
	 */
	public void setBankAccountHolderNit(String bankAccountHolderNit) {
		this.bankAccountHolderNit = bankAccountHolderNit;
	}

	/**
	 * @return the bankAccountHolderNit
	 */
	public String getBankAccountHolderNit() {
		return bankAccountHolderNit;
	}

	/**
	 * @param product the product to set
	 */
	public void setProduct(String product) {
		this.product = product;
	}

	/**
	 * @return the product
	 */
	public String getProduct() {
		return product;
	}

	/**
	 * @param bankAccountNumber the bankAccountNumber to set
	 */
	public void setBankAccountNumber(String bankAccountNumber) {
		this.bankAccountNumber = bankAccountNumber;
	}

	/**
	 * @return the bankAccountNumber
	 */
	public String getBankAccountNumber() {
		return bankAccountNumber;
	}

	
	/**
	 * @param bankAccountType the bankAccountType to set
	 */
	public void setBankAccountType(Long bankAccountType) {
		this.bankAccountType = bankAccountType;
	}

	/**
	 * @return the bankAccountType
	 */
	public Long getBankAccountType() {
		return bankAccountType;
	}

	/**
	 * @param bankAccountTypes the bankAccountTypes to set
	 */
	public void setBankAccountTypes(List<SelectItem> bankAccountTypes) {
		this.bankAccountTypes = bankAccountTypes;
	}
	
	public List<SelectItem> getBankAccountTypes() {
		
		System.out.println(" [Entre aqui ] ");
		bankAccountTypes = new ArrayList<SelectItem>();
		for(TbProductType t:bankAccount.getProductType()){
			bankAccountTypes.add(new SelectItem(t.getProductTypeId(), t.getProductTypeDescription()));
		}
	return bankAccountTypes;
}
	
	

}