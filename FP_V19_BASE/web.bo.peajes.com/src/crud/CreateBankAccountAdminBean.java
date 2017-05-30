package crud;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import constant.AccountStateType;
import sessionVar.SessionUtil;
import util.ReAccountBank;
import validator.Validation;

import ejb.AssociationPaymentMethod;
import ejb.User;
import ejb.crud.Bank;
import ejb.crud.BankAccount;

import jpa.TbAccount;
import jpa.TbBank;
import jpa.TbProductType;
import jpa.TbSystemUser;

public class CreateBankAccountAdminBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@EJB(mappedName="ejb/User")
	private User user;
	
	@EJB(mappedName="ejb/Bank")
	private Bank bank;
	
	@EJB(mappedName="ejb/BankAccount")
	private BankAccount bankAccount;
	
	@EJB(mappedName = "ejb/AssociationPaymentMethod")
	private AssociationPaymentMethod associationPaymentMethod;
	
	private List<SelectItem> clientAccounts;
	private Long accountId;
	
	private List<SelectItem> clients;
	private Long clientId=-1L;
	
	private List<SelectItem> banks;
	private Long bankId;
	
	private List<SelectItem> typeProducts;
	private Long typeProductsId;
	
	private String accountNumber="0";
	
	private List<TbSystemUser> clientsList; 
		
	private List<ReAccountBank> listBankAccount;
	
	private boolean showModal;
	
	private boolean showModalValidate;
			
	private boolean showData;
	
	private boolean showData2;
	
	private boolean showMessage;
	
	private boolean showOcult=true;
	
	private boolean showOcultPSE=false;
	
	private boolean stateProduct;
	
	private String modalMessage;
		
	private String value="0";
	
	private Long initValue=0L;

	
	public CreateBankAccountAdminBean(){
	}
	
    @PostConstruct
	public void init(){
    	this.getClientsList();
    	this.getClients();
    	this.getBanks();
	}
	
	/**
	 * @param clientId the clientId to set
	 */
	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	/**
	 * @return the clientId
	 */
	public Long getClientId() {
		return clientId;
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

	/**
	 * @param typeProductsId the typeProductsId to set
	 */
	public void setTypeProductsId(Long typeProductsId) {
		this.typeProductsId = typeProductsId;
	}

	/**
	 * @return the typeProductsId
	 */
	public Long getTypeProductsId() {
		return typeProductsId;
	}

	/**
	 * @param accountNumber the accountNumber to set
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	/**
	 * @return the accountNumber
	 */
	public String getAccountNumber() {
		return accountNumber;
	}

	/**
	 * @param clients the clients to set
	 */
	public void setClients(List<SelectItem> clients) {
		this.clients = clients;
	}

	
	/**
	 * Método encargado de ocultar en el jsf el tipo producto y producto bancario al seleccionar PSE.
	 * @return the banks
	 * @author psanchez
	 */
	public void changeMenu(ValueChangeEvent event){
		bankId = (Long) event.getNewValue();
		Long IdBank = bank.getOtherBanks(bankId);
		if (IdBank!=null){
			this.setShowOcult(false);
			this.setShowOcultPSE(true);
		}else{
			this.setShowOcultPSE(false);
			this.setShowOcult(true);
		}
	}

	/**
	 * @return the clients
	 */
	public List<SelectItem> getClients() {
			clients = new ArrayList<SelectItem>();
			clients.add(new SelectItem(-1, " "));
			for(TbSystemUser tu : getClientsList()){
				String name = tu.getUserCode()+" - "+tu.getUserNames();
				if(tu.getTbCodeType().getCodeTypeId().longValue() != 3){
					name = name + " " + tu.getUserSecondNames();
				}
				clients.add(new SelectItem(tu.getUserId(), name));
			}
		return clients;
	}

	/**
	 * @param banks the banks to set
	 */
	public void setBanks(List<SelectItem> banks) {
		this.banks = banks;
	}

	/**
	 * @return the banks
	 */
	public List<SelectItem> getBanks() {
		banks= new ArrayList<SelectItem>();
		banks.add(new SelectItem(-1L,"-Seleccione uno-"));
		for(TbBank t: bank.getBanks()){
			banks.add(new SelectItem(t.getBankId(), t.getBankName()));
		}
		return banks;
	}

	/**
	 * @param typeProducts the typeProducts to set
	 */
	public void setTypeProducts(List<SelectItem> typeProducts) {
		this.typeProducts = typeProducts;
	}

	/**
	 * @return the typeProducts
	 */
	public List<SelectItem> getTypeProducts() {
		typeProducts= new ArrayList<SelectItem>();
		for(TbProductType t:bankAccount.getProductType()){
			typeProducts.add(new SelectItem(t.getProductTypeId(), t.getProductTypeDescription()));
		}
		return typeProducts;
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

	
	public String addAssociateProductsAccountClient(){
		if(accountNumber==null){
			this.setShowModalValidate(true);
			this.setModalMessage("Error: El campo Número de producto es obligatorio.");
		}
		else if(accountNumber=="0" || accountNumber==null){
			this.setShowModalValidate(true);
			this.setModalMessage("Error: El campo Número de producto es obligatorio.");
		}
		else if(clientId==null || clientId==-1L){
			this.setShowModalValidate(true);
			this.setModalMessage("Error: Debe seleccionar un cliente.");
		}
		else if(bankId<=0){
			this.setShowModalValidate(true);
			this.setModalMessage("Error: debe seleccionar una entidad.");
		}
		else if((accountNumber=="") || (accountNumber==null)){
			initValue = 0L;
			this.setShowModalValidate(true);
			this.setModalMessage("Error: El campo Producto Bancario no puede estar vacío.");
		} 
		else if((value.equals("")) || (value.equals(null))){
			initValue = 0L;
			this.setShowModalValidate(true);
			this.setModalMessage("Error: El campo Valor asignación de recursos inicial no puede estar vacío.");
		}
		else if (!Validation.isNumeric(accountNumber)){
			initValue=0L;
			this.setShowModalValidate(true);
			this.setModalMessage("Error: El campo Producto Bancario contiene caracteres no numéricos.");
		}
		else if (!Validation.isNumericPuntoYComaNoConsecutive(value)){
			initValue=0L;
			this.setShowModalValidate(true);
			this.setModalMessage("Error: El campo Valor asignación de recursos inicial solo debe contener números.");
		}
		
		else{
			if(accountNumber!=null){
				if(accountNumber.length()<4){
					this.setShowModalValidate(true);
					this.setModalMessage("Error: El tamaño del campo producto bancario debe ser igual a 4.");
				}
				else{
					if(value!=null){
						initValue = Long.parseLong(value.replace(".", "").replace(",", ""));
					}
					else{
						initValue=0L;
					}
					if(bankAccount.insertBankAccountAdmin(clientId, bankId, typeProductsId, accountNumber,SessionUtil.ip(), SessionUtil
							.sessionUser().getUserId(), initValue)){
						TbSystemUser tu=user.getSystemUser(clientId);
						String cod="";
						if(tu!=null){
							cod=tu.getUserCode();
						}	
						this.setListBankAccount(bankAccount.getProductsByClient2(clientId));
						this.setShowModal(true);
						this.setModalMessage("Cuenta bancaria creada correctamente al cliente con Nro de Identificación " + cod);
					}
					else{
						this.setShowModal(true);
						this.setModalMessage("Error al crear cuenta bancaria. Verifique que no se encuentre registrado este producto bancario en el sistema.");
					}	
				}
			}
		}
		
		return null;
	}
	
	
	/**
	 * Método encargado de asociar cuenta FacilPass con PSE (cliente)
	 * @author psanchez
	 */
	public void addAssociateProductsAccountClientPSE() {
	   this.setShowModal(false);
	   this.setShowModalValidate(false);
	   String result=null;
	   Long bankAccountId=0L;

		try{  
			if(clientId.longValue() != -1L) {
				 if(accountId.longValue() != -1L) {	
					result=associationPaymentMethod.haveProductAssociation(accountId);
					if(result!=null){
						this.setModalMessage(result);
						this.setShowModal(true);
					} else if(bankId.longValue() != -1L){
					        bankAccountId=associationPaymentMethod.getIdFreePSE(clientId, accountId, bankId);
							accountNumber=String.valueOf(bankAccountId);
							String bankName=bank.haveBankName(bankId);
							if(bankAccountId!=null && associationPaymentMethod.haveAccountAssociation(bankAccountId)==false && 
									associationPaymentMethod.getAccountAssociation(accountId)==false){
									boolean associationResult = this.saveAssociateProductsAccountClientPSE(accountId, bankId, bankAccountId, clientId);
									if(associationResult==true){
									    System.out.println("[ PSE libre ]");
									    this.stateProduct=false;	
										TbSystemUser tu=user.getSystemUser(clientId);
										this.setListBankAccount(bankAccount.getProductsByClient2(clientId));
										this.setShowModal(true);
										this.setModalMessage("Cuenta FacilPass "+ accountId +" asociada a la entidad "+ bankName +" correctamente para el cliente con Nro de Identificación " + tu.getUserCode());
									}else{
										this.setShowModal(true);
										this.setModalMessage("Error en la transacción.");
									}
			
							     } else {
							    	System.out.println("[ PSE nuevo ]");
									this.stateProduct=true;	
									bankAccountId=associationPaymentMethod.getIdPSE(clientId, SessionUtil.sessionUser().getUserId());
									boolean associationResult = this.saveAssociateProductsAccountClientPSE(accountId, bankId, bankAccountId, clientId);
									if(associationResult==true){
										TbSystemUser tu=user.getSystemUser(clientId);
										this.setListBankAccount(bankAccount.getProductsByClient2(clientId));
										this.setShowModal(true);
										this.setModalMessage("Cuenta FacilPass "+ accountId +" asociada a la entidad "+ bankName +" correctamente para el cliente con Nro de Identificación " + tu.getUserCode());
									}else{
										this.setShowModal(true);
										this.setModalMessage("Error en la transacción.");
									}
			
								 }
				      }else {
				    	this.setShowModalValidate(true);
						this.setModalMessage("Error: Seleccione la Entidad.");
					  }
				   } else {
					   this.setShowModalValidate(true);
					   this.setModalMessage("Seleccione la Cuenta FacilPass.");
				   }
				} else {
				   this.setShowModalValidate(true);
				   this.setModalMessage("Seleccione el Cliente.");
			   }
		 }catch(Exception ex){
				ex.printStackTrace();
			 }
		}
	
	
	public boolean saveAssociateProductsAccountClientPSE(Long accountId, Long bankId, Long bankAccountId, Long clientId){
		   try{
			   this.setShowModal(false);
			   this.setShowModalValidate(false);

				/** Se valida si se esta asociando el mismo producto bancario*/
				System.out.println("saveAssociateProductsAccountClientPSE-->stateProduct: "+stateProduct);

				if(stateProduct==false){
						/** Se obtiene el ultimo producto bancario asociado */
						Long lastIdBankAccount = associationPaymentMethod.getLastBankingProduct(accountId);					
						Long accountAssociate = associationPaymentMethod.insertBankAccountAdminPSE(accountId, bankId, bankAccountId, 
								clientId, SessionUtil.sessionUser().getUserId(), SessionUtil.ip());
											
						if (accountAssociate != null && bankAccountId>0) {
								associationPaymentMethod.processAssociationPSE(clientId, accountId, SessionUtil
									                  .sessionUser().getUserId(), SessionUtil.ip(), bankId, bankAccountId, lastIdBankAccount);
								return true;							
			            }else{
			    			return false;
					    }
				}else if(stateProduct==true) {
					/** Se obtiene el ultimo producto bancario asociado */
					Long lastIdBankAccount = associationPaymentMethod.getLastBankingProduct(accountId);					
					Long accountAssociate = associationPaymentMethod.insertBankAccountAdminPSE(accountId, bankId, bankAccountId, 
							clientId, SessionUtil.sessionUser().getUserId(), SessionUtil.ip());
										
					if (accountAssociate != null) {
						Long bankAccountIdNew = associationPaymentMethod.BankAccountNewPSE(clientId, bankId, bankAccountId);
						System.out.println("bankAccountIdNew Admin-->: "+bankAccountIdNew);
						if(bankAccountIdNew>0L){
							associationPaymentMethod.processAssociationPSE(clientId, accountId, SessionUtil
								                  .sessionUser().getUserId(), SessionUtil.ip(), bankId, bankAccountIdNew, lastIdBankAccount);
							return true;							
			            }else{
			    			return false;
					    }
					}else {
						return false;
					}	
				}
			 }catch(NullPointerException n){
				 return false;
			 }
		return false;
	  }
	
	public String hideModal(){
		this.setShowModal(false);
		this.setShowOcult(true);
		this.setShowOcultPSE(false);
		this.setShowData2(false);
		this.setModalMessage("");
		this.setClientId(0L);
		this.setBankId(-1L);
		this.setTypeProductsId(0L);
		this.setAccountNumber("0");
		this.setInitValue(0L);
		this.setValue("0");
		this.setClients(null);
		this.setClientsList(null);
		return null;
	}

	
	 public String hideModal2() {
		this.setShowModal(false);
		this.setShowModalValidate(false);
		return null;
	}

	/**
	 * @param clientsList the clientsList to set
	 */
	public void setClientsList(List<TbSystemUser> clientsList) {
		this.clientsList = clientsList;
	}


	/**
	 * @return the clientsList
	 */
	public List<TbSystemUser> getClientsList() {
		clientsList = new ArrayList<TbSystemUser>();
		clientsList = user.getOnlyClients();
		return clientsList;
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

	
	public String showlistAccount(){
		try{
			if(clientId==-1L || clientId==null){
				this.setShowData2(false);
				this.setModalMessage("Debe seleccionar un cliente.");
				this.setShowModal(true);
			}
			else{
				listBankAccount= new ArrayList<ReAccountBank>();
				listBankAccount=bankAccount.getProductsByClient2(clientId);
				if(listBankAccount.size()>0){
					this.setShowData2(true);
					this.setShowMessage(false);
				}
				else{
					this.setShowData2(false);
					this.setModalMessage("El cliente no tiene productos asociados.");
					this.setShowModal(true);
				}
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		
		return null;
	}


	/**
	 * @param showData2 the showData2 to set
	 */
	public void setShowData2(boolean showData2) {
		this.showData2 = showData2;
	}


	/**
	 * @return the showData2
	 */
	public boolean isShowData2() {
		return showData2;
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
	
	public void ocult(){
		this.setShowData2(false);
	}


	/**
	 * @param listBankAccount the listBankAccount to set
	 */
	public void setListBankAccount(List<ReAccountBank> listBankAccount) {
		this.listBankAccount = listBankAccount;
	}


	/**
	 * @return the listBankAccount
	 */
	public List<ReAccountBank> getListBankAccount() {
		return listBankAccount;
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
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	public void setShowOcult(boolean showOcult) {
		this.showOcult = showOcult;
	}

	public boolean isShowOcult() {
		return showOcult;
	}

	public void setShowOcultPSE(boolean showOcultPSE) {
		this.showOcultPSE = showOcultPSE;
	}

	public boolean isShowOcultPSE() {
		return showOcultPSE;
	}

	public void setClientAccounts(List<SelectItem> clientAccounts) {
		this.clientAccounts = clientAccounts;
	}

	/**
	 * Método encargado de listar las cuentas asociadas cliente en session. 
	 * @return the  clientAccounts
	 * @author psanchez
	 */
	public List<SelectItem> getClientAccounts() {
		if(clientAccounts == null){
			clientAccounts = new ArrayList<SelectItem>();
		} else {
			clientAccounts.clear();
		}		
		clientAccounts.add(new SelectItem(new Long(-1L), "--Seleccione Cuenta--"));
		for(TbAccount su : associationPaymentMethod.getClientAccount(clientId)){
			if((su.getAccountState()!=AccountStateType.CLOSE.getId()) && (su.getAccountState()!=AccountStateType.TO_CLOSE.getId())){
				clientAccounts.add(new SelectItem(su.getAccountId(), "Cuenta No. " + su.getAccountId()));			
			}
		}
		return clientAccounts;
	}
	

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public void setShowModalValidate(boolean showModalValidate) {
		this.showModalValidate = showModalValidate;
	}

	public boolean isShowModalValidate() {
		return showModalValidate;
	}

	public void setStateProduct(boolean stateProduct) {
		this.stateProduct = stateProduct;
	}

	public boolean isStateProduct() {
		return stateProduct;
	}
	
}
