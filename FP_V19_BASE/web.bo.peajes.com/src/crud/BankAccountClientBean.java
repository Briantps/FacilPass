package crud;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import menu.SideMenuBean;

import security.UserLogged;
import sessionVar.SessionUtil;
import util.ReAccountBank;

import ejb.User;
import ejb.crud.Bank;
import ejb.crud.BankAccountClient;

import jpa.TbBank;
import jpa.TbProductType;

/**
 * Bean to Manage the administration of Bank Accounts.
 * @author psanchez
 *
 */
public class BankAccountClientBean implements Serializable {
	private static final long serialVersionUID = 731945118516042526L;

	@EJB(mappedName="ejb/BankAccountClient")
	private BankAccountClient bankAccountClientEJB;
	
	@EJB(mappedName="ejb/User")
	private User user;
	
	@EJB(mappedName="ejb/Bank")
	private Bank bank;
	
	/** Control Modal **/
	private boolean showModal;
	private boolean showInsert;
	private boolean showConfirmation;
	private boolean showModalValidate;
	private String corfirmMessage;
	private String modalMessage;
	
	/** Atributos lista, consulta e ingreso producto bancario**/
	private List<ReAccountBank> bankAccountList;
	private String bankAccountNumber;
		
	private List<SelectItem> bankList;
	private Long idBank;
	
	private List<SelectItem> bankAccountTypes;
	private Long bankAccountType;
	
	private String bankAccountHolder;
	private Long initValue; //Valor recarga inicial
	private String valueRechText;
	
	/** Código y nombre del usuario que hizo login **/
	private UserLogged usrs;
	@SuppressWarnings("unused")
	private String userCode;
	private Long userId;

	/**
	 * Constructor.
	 */
	public BankAccountClientBean() {
		usrs = (UserLogged) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
	}
	
	@PostConstruct
	public void init(){
		userId=user.getSystemMasterById(usrs.getUserId());
		this.getBankAccountList();
	}
	
	/** Acciones **/
	public void initAddBankAccountClient(){
		this.setShowInsert(true);
		this.setBankAccountNumber(null);
		this.setBankAccountType(null);
		this.setIdBank(0L);
		this.setValueRechText(null);
	    this.setInitValue(0L);
	}
	
	
	/**
	 * Método que lista los productos bancarios correspondientes al cliente en sesión
	 * @author psanchez
	 */
	public List<ReAccountBank> getBankAccountList() {
		if(bankAccountList == null) {
			bankAccountList = new ArrayList<ReAccountBank>();
		} else{
			bankAccountList.clear();
		}
			bankAccountList = bankAccountClientEJB.getBankAccounts(userId);
		return bankAccountList;		
    }
	
	
	/**
	 * Método encargado de validar los valores del formulario para crear el producto bancario del cliente en session.
	 * @author psanchez
	 */
	
	public void addBankAccountClient(){
	   this.setShowModal(false);
	   this.setShowInsert(false);
	   try{
		   if (postValidationNew()){ 
			   /* if((valueRechText.equals("")) || (valueRechText.equals(null))){
					initValue = 0L;
				}else{
					initValue = Long.parseLong(valueRechText.replace(".", ""));
				}*/
			    if(idBank.intValue()<0) {
			    	this.setModalMessage("Seleccione el Banco.");
					this.setShowModal(true); 
				}else if(bankAccountNumber.length() != 4) {
					this.setModalMessage("Ingrese los 4 últimos dígitos del Producto Bancario.");
					this.setShowModal(true); 
				}else if(bankAccountClientEJB.existBankAccountClient(userId, idBank, bankAccountNumber, bankAccountType)==false) {
				    this.setModalMessage("Error: El Producto Bancario ya existe. Verifique.");
					this.setShowModal(true);
				}else{
					this.setShowModal(false);
					this.setShowConfirmation(true);
					this.setModalMessage(null);
					this.setCorfirmMessage("¿Desea definir el Producto Bancario?");
				} 
		    }
		 }catch(NumberFormatException nfe){
	 			this.setModalMessage("El campo Valor Recarga Inicial tiene caracteres inválidos. Verifique.");
	 			this.setShowModal(true);
				System.out.println(" [ Error en BankAccountClientBean.addBankAccountClient. ] "+nfe.getMessage());
		}
	}
	
	/**
	 * Método encargado de crear el producto bancario del cliente en sesión.
	 * @author psanchez
	 */
	public void insertBankAccountClient(){
	   try{
			this.setModalMessage(null);
			this.setShowModal(false);
			this.setShowModalValidate(false);
			this.setShowConfirmation(false);
			
			boolean result = bankAccountClientEJB.insertBankAccountClient(userId, idBank, bankAccountNumber, 
					bankAccountType, initValue, SessionUtil.ip(), SessionUtil.sessionUser().getUserId());
			if(result!=false){
				this.setModalMessage("Producto Bancario definido correctamente. " +
									 "Diríjase a Asociar Producto Bancario.");
				this.setShowModal(false);
				this.setShowModalValidate(true);
				this.setShowConfirmation(false);
				this.setBankAccountList(null);
				initValue = null;
				valueRechText =	"";
				System.out.println("Se ha creado un producto bancario y se llama el menu");
				SideMenuBean mnu = new SideMenuBean(); 
				mnu.setPodCreate(true);										
				mnu.createMenu();
				mnu.getPanelMenu();	
			} else {
				this.setModalMessage("Error: El Producto Bancario ya existe. Verifique.");
				this.setShowModal(false);
				this.setShowModalValidate(true);
				this.setShowConfirmation(false);			}
		}catch(NullPointerException n){
		    this.setModalMessage("Error en la Transacción");
		    this.setShowModal(false);
			this.setShowModalValidate(true);
			this.setShowConfirmation(false);
		 }
	 }
	

		
	/**
	 * Método encargado de obtener el tipo de cuenta.
	 * @return the bankAccountTypes
	 */	
	  public List<SelectItem> getBankAccountTypes() {  
		bankAccountTypes = new ArrayList<SelectItem>();
		  for(TbProductType t:bankAccountClientEJB.getProductType()){
			  bankAccountTypes.add(new SelectItem(t.getProductTypeId(), t.getProductTypeDescription()));
			}
		return bankAccountTypes;
	 }
	
	
    /**
	 * Método encargado de obtener la lista de bancos.
	 * @return the bankList
	 */
	 public List<SelectItem> getBanks() {
		bankList= new ArrayList<SelectItem>();
		bankList.add(new SelectItem(-1,"--Seleccione Entidad--"));
		for(TbBank t: bank.getBanksAval()){
			bankList.add(new SelectItem(t.getBankId(), t.getBankName()));
		}
		return bankList;
	 }
	 
	 /**
		 * Método encargado de validar los cuatro dígtos del producto
		 * @return the bankList
		 */
	 private boolean postValidationNew(){
		if(bankAccountNumber!="" && (!bankAccountNumber.matches("([0-9])+"))){
			this.setModalMessage("El campo Producto Bancario tiene caracteres inválidos. Verifique.");
			this.setShowModal(true);
			return false;
		}/*else if(valueRechText!="" && (!valueRechText.matches("([0-9.])+"))){
			this.setModalMessage("El campo Valor Recarga Inicial tiene caracteres inválidos. Verifique.");
			this.setShowModal(true);
			return false;
		}*/
		return true;		
	}
		
	 
	/**
	 * Hides modal windows.
	 */
	 public String hideModal(){
		this.setShowInsert(true);
		this.setShowModal(false);
		this.setShowConfirmation(false);
		this.setShowModalValidate(false);
		this.setModalMessage(null);
		return "ProdcutCreate";
	 }
	 
	 
	 public void hideModal2() {
		this.setShowInsert(false);
		this.setShowModal(false);
		this.setShowModalValidate(false);
		this.setShowConfirmation(false);
	}
	 
	 
	/** Getters and setters **/
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
	
	/**
	 * @param bankAccountList the bankAccountList to set
	 */
	public void setBankAccountList(List<ReAccountBank> bankAccountList) {
		this.bankAccountList = bankAccountList;
	}

	/**
	 * @param bankList the bankList to set
	 */
	public void setBankList(List<SelectItem> bankList) {
		this.bankList = bankList;
	}

	/**
	 * @param idBank the idBank to set
	 */
	public void setIdBank(Long idBank) {
		this.idBank = idBank;
	}

	/**
	 * @return the idBank
	 */
	public Long getIdBank() {
		return idBank;
	}

	/**
	 * @param corfirmMessage the corfirmMessage to set
	 */
	public void setCorfirmMessage(String corfirmMessage) {
		this.corfirmMessage = corfirmMessage;
	}

	/**
	 * @return the corfirmMessage
	 */
	public String getCorfirmMessage() {
		return corfirmMessage;
	}
	
	/**
	 * @param initValue the initValue to set
	 */
	public void setInitValue(Long initValue) {
		this.initValue = initValue;
	}

	/**
	 * @return the initValue
	 */
	public Long getInitValue() {
		return initValue;
	}

	/**
	 * @param valueRechText the valueRechText to set
	 */
	public void setValueRechText(String valueRechText) {
		this.valueRechText = valueRechText;
	}

	/**
	 * @return the valueRechText
	 */
	public String getValueRechText() {
		return valueRechText;
	}

	/**
	 * @param showConfirmationInsert the showConfirmation to set
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
	 * @param showModalValidate the showModalValidate to set
	 */
	public void setShowModalValidate(boolean showModalValidate) {
		this.showModalValidate = showModalValidate;
	}

	/**
	 * @return the showModalValidate
	 */
	public boolean isShowModalValidate() {
		return showModalValidate;
	}

	/**
	 * @param userCode the userCode to set
	 */
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	/**
	 * @return the userCode
	 */
	public String getUserCode() {
	    String userCode=user.getSystemUserCode(userId);
		return userCode;
	}
	
}