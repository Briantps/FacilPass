/**
 * 
 */
package process.affiliation;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import constant.EmailProcess;
import constant.LogAction;
import constant.LogReference;

import sessionVar.SessionUtil;
import util.AllInfoAccount;
import util.ObjectRechage;
import util.ws.WSRecharge;
import validator.Validation;

import ejb.AssociationPaymentMethod;
import ejb.Log;
import ejb.User;
import ejb.crud.BankAccount;
import ejb.email.Outbox;

import jpa.TbAccountType;
import jpa.TbBank;
import jpa.TbCodeType;
import jpa.TbSystemUser;
import linews.InvokeSqdm;


/**
 * Bean to manage client account creation.
 * @author tmolina
 *
 */
public class AccountClient implements Serializable {
	private static final long serialVersionUID = -6247866949442545193L;
	
	@PersistenceContext(unitName = "bo") 
	EntityManager em;
	
	@EJB(mappedName="ejb/User")
	private User user;
	
	@EJB(mappedName="ejb/BankAccount")
	private BankAccount bankAccount;
	
	@EJB(mappedName ="ejb/email/Outbox")
	private Outbox outbox;
	
	@EJB(mappedName = "ejb/AssociationPaymentMethod")
	private AssociationPaymentMethod associationPayment;
	
	@EJB(mappedName = "util/ws/WSRecharge")
	private WSRecharge WSrecharge;
	
	// Attributes ------------- //
	
	private List<TbCodeType> codeTypes;
	
	private List<SelectItem> codeTypesList;
	
	private Long codeType;
	
	private String codeClient="";
	
	private List<SelectItem> clientNames;
	
	private Long idClient;
	
	private List<TbSystemUser> clients; 
	
	private TbSystemUser client;
	
	private String userNames;
	
	private String userSecondNames;
	
    // Control Visibility -----//
	
	private boolean showData;
		
	// Control Modal ------//
	
	private boolean showModal;
	
	private String modalMessage;
	
	private boolean showConfirmation;
	
	private boolean showType;
	
	private String corfirmMessage;
	
	private List<AllInfoAccount> accountList;
	
	private Long idType;
	
	private List<SelectItem> types;
	
	private Long idBank=-1L;
	
	private Long idProduct=-1L;
	
	private List<SelectItem> banks;
	
	private List<SelectItem> products;
	
	@EJB(mappedName="ejb/Log")
	private Log log;

	/**
	 * Constructor.
	 */
	public AccountClient() {

	}
	
	/**
	 * 
	 */
	public void init() {
		this.setCodeTypesList(null);
		this.setClientNames(null);
		this.setClients(null);
		this.setShowData(false);
		this.setCodeClient("");
		this.setUserNames("");
		this.setUserSecondNames("");
		this.setShowModal(false);
		this.setIdClient(-1L);
	}
	
	// Actions ------------------- //
	public void clearFilter(){
		this.setShowModal(false);
		this.setShowData(false);
		this.setModalMessage("");
		this.setCodeClient("");
		this.setUserNames("");
		this.setUserSecondNames("");
		this.setIdClient(null);
		this.setCodeType(1L);
	}
	
	public String ocult() {
		setShowModal(false);
		showData = false;
		return null;
	}
	
	/**
	 * Searches a client by code and type code.
	 */
	public String search() {
		this.setShowData(false);
		this.setShowModal(false);
		idClient = -1L;
		if (codeClient!="") {
			client = user.getUserByCode(codeClient, codeType);
			if (client != null) {
				idClient = client.getUserId();
					if(client.getSystemUserMasterId()==null){
						if(user.isClient(client.getUserId())){
							this.setIdClient(client.getUserId());
							accountList = bankAccount.getClientAccount(idClient);
							setShowData(true);
							setShowModal(false);
						}
						else{
							this.setShowData(false);
							this.setShowModal(true);
							this.setModalMessage("Error: A un usuario del sistema no se le puede crear cuenta FacilPass.");
						}
					}
					else{
						this.setShowData(false);
						this.setShowModal(true);
						this.setModalMessage("Error: A un usuario de cliente maestro no se le deben crear cuentas facilpass.");
					}		
			} else {
				System.out.println("Entre al else de client == null");
				this.setShowData(false);
				this.setShowModal(true);
				this.setModalMessage("Error: No se encontraron datos para el cliente digitado.");
			}
		} else {
			System.out.println("Entre al else de codeClient == null");
			this.setShowModal(true);
			this.setModalMessage("Error: Debe digitar el numero de identificación del cliente.");
			this.setShowData(false);
			
		}
		return null;
	}
	
	/** Método encargado de realizar búsqueda por código, nombre o apellido del cliente
	 * @author psanchez
	 */	
	public void searchClient() {
		this.setShowModal(false);
		this.setShowData(false);
		this.setModalMessage("");

		if (codeClient.equals("") && userNames.equals("") && userSecondNames.equals("")) {
			this.setModalMessage("Ingrese filtro de búsqueda." );
			this.setShowModal(true);
			this.setShowData(false);
		}else if(!codeClient.equals("") && !Validation.isNumeric(codeClient)){
			this.setModalMessage("El campo No. Identificación contiene caracteres inválidos.");
			this.setShowModal(true);
			this.setShowData(false);
    	}else if(userNames!="" && !userNames.matches("([a-z]|[A-Z]|[áéíóú]|[ÁÉÍÓÚ]|[0-9]|[&/]|[_-]|[ñÑ]|[.]|\\s)+")){
	    	this.setModalMessage("El campo Nombre contiene caracteres inválidos.");
	    	this.setShowModal(true);
			this.setShowData(false);
    	 }else if(userSecondNames!="" && !userSecondNames.matches("([a-z]|[A-Z]|[áéíóú]|[ÁÉÍÓÚ]|[ñÑ]|\\s)+")){
	    	this.setModalMessage("El campo Apellidos contiene caracteres inválidos.");
	    	this.setShowModal(true);
			this.setShowData(false);
    	  }
			Long userId = user.getUserFromFiltersCreateNote(codeClient, codeType, userNames, userSecondNames);
			if (userId.equals(-2L)) {
				setShowModal(true);
				setModalMessage("Hay más de un resultado en la consulta, por favor agregue otro campo en el filtro.");
				setShowData(false);
			}		
			else if (userId.equals(-1L)) {
				this.setModalMessage("Ocurrió un error al momento de realizar la búsqueda.");
				this.setShowModal(true);
				this.setShowData(false);
			}
			else if (userId.equals(0L)) {
				this.setModalMessage("No hay información para el cliente digitado.");
				this.setShowModal(true);
				this.setShowData(false);
			}
			else if (userId>-1L) {
				client = em.find(TbSystemUser.class, userId);
				codeClient = client.getUserCode();
				userNames = client.getUserNames();
				userSecondNames = client.getUserSecondNames();
				if(client.getSystemUserMasterId()==null){
					if(user.isClient(client.getUserId())){
						accountList = bankAccount.getClientAccount(client.getUserId());
						setShowData(true);
						setShowModal(false);
					}
					else{
						this.setShowModal(true);
						this.setModalMessage("Error: A un usuario del sistema no se le puede crear cuenta FacilPass.");
					}
				}
				else{
					this.setShowModal(true);
					this.setModalMessage("Error: A un usuario de cliente maestro no se le deben crear cuentas facilpass.");
				}
		    }
	}
	
	/*
	 * Change the data of client.
	 */
	public String changeClient() {
		setShowModal(false);
		if (idClient.longValue() == -1) {
			showData = false;
			this.codeType = null;
			this.codeClient = "";
		} else {
			this.showData = false;
			for(TbSystemUser u : getClients()){
				if(idClient.longValue() == u.getUserId().longValue()){
					accountList = bankAccount.getClientAccount(idClient);
					this.client = u;
					this.codeType = u.getTbCodeType().getCodeTypeId();
					this.codeClient = u.getUserCode();
					this.showData = true;
					break;
				}
			}
		}
		return null;
	}
	
	/**
	 * Clean Modal Panel.
	 */
	public String hideModal(){
		setShowModal(false);
		setModalMessage("");
		setShowConfirmation(false);
		setShowType(false);
		this.setIdBank(-1L);
		this.setIdProduct(-1L);
		this.setIdType(0L);
		this.showModal=false;
		return null;
	}
	
	/**
	 * Init Confirmation.
	 */
	public String initCreateAccount(){
		setShowModal(false);
		setShowConfirmation(true);
		setCorfirmMessage("¿Está seguro que desea realizar la transacción?");
		return null;
	}
	
	public String chooseAccountType(){
		setShowModal(false);
		setShowConfirmation(false);
		setShowType(true);
		return null;
	}
	

	public void setIdType(Long idType) {
		this.idType = idType;
	}

	public Long getIdType() {
		return idType;
	}
	public void setTypes(List<SelectItem> types) {
		this.types = types;
	}

	public List<SelectItem> getTypes() {
		types= new ArrayList<SelectItem>();
		for(TbAccountType t : user.getTypes()){
			types.add(new SelectItem(t.getAccountTypeId(), t.getNameType()));
		}
		return types;
	}
	
	/**
	 * Creates a client account.
	 */
	public String createAccount() {
		try{
			System.out.println("idType en createAccount" + idType);
			System.out.println("idBank en createAccount" + idBank);
			System.out.println("idProduct en createAccount" + idProduct);
			long cod=codeType.longValue();
			long type=idType.longValue();
			long flag=0;
			System.out.println("tipo " + idType);
			setShowModal(false);
			setShowConfirmation(false);
			setShowType(false);
			BigDecimal umbral = new BigDecimal(0);
			if((cod!=3) && (type==50000) || (type==50001) ){
				flag=1;
			}
			else if((cod==3) && (type!=50000)){
				flag=1;
			}
			else{
				flag=0;
			}
			String validaProductos = bankAccount.haveProductAssociation(idProduct);
			if(bankAccount.isProcessDisociationById(idProduct)){
				setModalMessage("El producto bancario seleccionado se encuentra en proceso de disociación.");
			}else if(validaProductos!=null){
					setModalMessage(validaProductos);
			}
			else{
				if(flag==1){
					String r2 = null;
					String resp = null, resp1=null, respRe = null;
					Long accountId = user.createAccount(client.getUserId(), idType, SessionUtil
								.sessionUser().getUserId(), SessionUtil.ip(),idBank, idProduct);
						
					if (accountId != null) {
						if(accountId >0L){
							setModalMessage("Cuenta Facilpass creada Correctamente, el número de Cuenta es: "
										+ accountId + ".");
							client = user.getUserByCode(client.getUserCode(), codeType);
								
								
							if(idProduct>0){
							    	
							    System.out.println("accountId" + accountId);
								System.out.println("idProduct en createAccount" + idProduct);
									
								Object[] o=user.getInfAssociationProdcut(accountId,idProduct);
								    
//							    AccountAdminClient account= new AccountAdminClient();
//								resp=account.invokeAccountAdmin(o);
									
								InvokeSqdm account2= new InvokeSqdm();
								String dataService=account2.InvokeAccountAdmin(o);
								String array[]=dataService.split(";");
								Long response=Long.valueOf(array[0].trim());
								String accountNumber=bankAccount.getProductNumber(idProduct);
								if(response!=0){
									user.deleteAssociation(idProduct, accountId);
									System.out.println("Se eliminó asociacion");
									resp="Se creó cuenta facilPass Nro: " + accountId + " pero no se recibio respuesta positiva desde el webservices, respuesta : "+ response + " por lo tanto no se asoció el producto bancario seleccionado." +accountNumber;									
									resp1="Estimado Ususario, \r\n  \r\nEl producto bancario creado no se pudo asociar a su cuenta facilpass " + accountNumber + " Por favor comuníquese con la linea 018000xxxxxxx para conocer el detalle del rechazo y los pasos a seguir. \r\n  \r\nCordialmente, \r\nFacilpass" ;
									outbox.insertMessageOutbox(client.getUserId(), 
								                   EmailProcess.ACCOUNT_ACTIVATION_REJECTED,
								                   accountId,
								                   idProduct,
								                   null, 
								                   null,
								                   null,
								                   null,								                   
								                   idBank,
								                   null,
								                   SessionUtil.sessionUser().getUserId(),
								                   null,
								                   null,
								                   null,
								                   true,
								                   null);
								}
								else{
									resp="Se asoció producto bancario " +accountNumber+ "  correctamente a la cuenta Nro: " + accountId +" respuesta "+response;
									resp1="Estimado usuario, \r\n \r\nSu cuenta FacilPass "+ accountId+ " ha sido activada en el sistema. Para poder hacer uso del producto por favor ingrese a www.facilpass.com con su usuario y contraseña y realice una asignación de recursos.  \r\n  \r\nCordialmente, \r\nFacilpass" ;
										
									outbox.insertMessageOutbox(client.getUserId(), 
								                   EmailProcess.ACCOUNT_ACTIVATION_SUCCESSFUL,
								                   accountId,
								                   idProduct,
								                   null, 
								                   null,
								                   null,
								                   null,								                   
								                   idBank,
								                   null,
								                   SessionUtil.sessionUser().getUserId(),
								                   null,
								                   null,
								                   "Asociación",
								                   true,
								                   null);
										
									user.createProcess(client.getUserId(),SessionUtil.sessionUser().getUserId(), SessionUtil.ip(), accountNumber ,accountId,idBank);
										
									System.out.println("Entre a recargar");							       
									List<Object> lis =user.initRecharge(response, accountId);
									Long idBank = null;
									for(Object ob : lis){
										umbral= (BigDecimal) ob;
										System.out.println("umbral para recarga en AccountAdmin" + umbral);

										//Object[] obj=user.recharge(umbral);
										ObjectRechage obj = WSrecharge.createListObj(umbral.longValue());
										idBank = Long.parseLong(obj.getBankId());
										r2=account2.InvokeRecharge(obj);
										 String diaArray[] = r2.split(";");
										   r2=diaArray[0];
										   Long r3=Long.valueOf(r2);
										System.out.println("Respuesta webservice recarga: " + r2);
										if(r2=="0"){
											respRe="Estimado Usuario, \r\n \r\nLa asignación de recursos para su cuenta Facilpass " + accountId + " ha sido aprobada. Por favor ingrese a www.facilpass.com con su usuario y contraseña y verifique su saldo. \r\n \r\nCordialmente, \r\nFacilpass";
										}
										else{
												respRe="Estimado Usuario, \r\n \r\nLa asignacion de recursos para su cuenta Facilpass " + accountId + " ha sido rechazada. Por favor comuníquese con la linea 018000xxxxxxx para conocer el detalle del rechazo y los pasos a seguir. \r\n \r\nCordialmente, \r\nFacilpass";
										}
										user.createProcessRecharge(client.getUserId(),SessionUtil.sessionUser().getUserId(), SessionUtil.ip() ,accountId,idProduct,idBank, r3);
									}
								}
									
								System.out.println("respuesta webservices asociacion: " + response);
								System.out.println("respuesta webservices recarga : " + r2);
								//resp="Resultado desde el webservices al asociar producto bancario desde el bos : " +resp;
									
								this.setShowModal(true);
							}
							else{
							    resp="Se creó cuenta facilPass Nro: " + accountId +" pero no se le asoció producto bancario por lo tanto no se invocó al webservices";
							    resp1="Estimado Usuario, \r\n \r\nSu cuenta facilpass ha sido preinscrita en el sistema. Para activarla y poder hacer uso del producto por favor ingrese a www.facilpass.com con su usuario y contraseña y  continúe con los pasos requeridos. \r\n \r\nCordialmente, \r\nFacilpass";

							    System.out.println(resp);
							    System.out.println(resp1);
							}
						  
							log.insertLog(resp, LogReference.BANKACCOUNT, LogAction.CREATE,
										SessionUtil.ip(), SessionUtil.sessionUser().getUserId());
							    
							if(respRe!=null){
								Long transactionId = WSrecharge.getTransactionByUmbral(umbral.longValue());
							    if(r2=="0"){							    	
							    	outbox.insertMessageOutbox(client.getUserId(), 
								                   EmailProcess.RESOURCE_ALLOCATION_SUCCESSFUL,
								                   accountId,
								                   idProduct, 
								                   transactionId, 
								                   null,
								                   null,
								                   null,
								                   idBank,
								                   null,
								                   SessionUtil.sessionUser().getUserId(),
								                   null,
								                   null,
								                   null,
								                   true,
								                   null);
							    }else{
							    	outbox.insertMessageOutbox(client.getUserId(),
								                   EmailProcess.RESOURCE_ALLOCATION_REJECTED,
								                   accountId,
								                   idProduct, 
								                   transactionId, 
								                   null,
								                   null,
								                   null,
								                   idBank,
								                   null,
								                   SessionUtil.sessionUser().getUserId(),
								                   null,
								                   null,
								                   null,
								                   true,
								                   null);
							    }
								log.insertLog("Respuesta de servicio de recarga:" + r2, LogReference.BANKACCOUNT, LogAction.CREATE,
											SessionUtil.ip(), SessionUtil.sessionUser().getUserId());
							}
					    }
						else if(accountId==-2L){
							setModalMessage("Error: El cliente se encuentra inactivo por lo tanto no se puede crear la cuenta facilpass.");
						}
                        else if(accountId==-3L){
                            setModalMessage("Error: El cliente no existe. Verifique por favor");
						}
                        else if(accountId==-4L){
                        	setModalMessage("Error: No se creó la cuenta");
                        }
						accountList = bankAccount.getClientAccount(client.getUserId());
					} 
					else {
						setModalMessage("Error en Transacción.");
					}
				}
				else{
					setModalMessage("Error: El tipo de documento no corresponde con el tipo de cuenta. Verifique la información por favor");
				}
			}
			
			setShowModal(true);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	
	// Getters and setters -------------- //

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
	 * @param codeClient the codeClient to set
	 */
	public void setCodeClient(String codeClient) {
		this.codeClient = codeClient;
	}

	/**
	 * @return the codeClient
	 */
	public String getCodeClient() {
		return codeClient;
	}

	/**
	 * @param clientNames the clientNames to set
	 */
	public void setClientNames(List<SelectItem> clientNames) {
		this.clientNames = clientNames;
	}

	/**
	 * @return the clientNames
	 */
	public List<SelectItem> getClientNames() {
		
		if (clientNames == null) {
			clientNames = new ArrayList<SelectItem>();
			
			clientNames.add(new SelectItem(-1, "Seleccione un Cliente"));
			for(TbSystemUser tu : getClients()){
				String name = tu.getUserNames();
				if(tu.getTbCodeType().getCodeTypeId().longValue() != 3){
					name = name + " " + tu.getUserSecondNames();
				}
				clientNames.add(new SelectItem(tu.getUserId(), name));
			}
		}		
		return clientNames;
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
	 * @param clients the clients to set
	 */
	public void setClients(List<TbSystemUser> clients) {
		this.clients = clients;
	}

	/**
	 * @return the clients
	 */
	public List<TbSystemUser> getClients() {
		if (clients == null) {
			clients = new ArrayList<TbSystemUser>();
			clients = user.getAllActiveClient();
		}
		return clients;
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
	 * @param accountList the accountList to set
	 */
	public void setAccountList(List<AllInfoAccount> accountList) {
		this.accountList = accountList;
	}

	/**
	 * @return the accountList
	 */
	public List<AllInfoAccount> getAccountList() {
		return accountList;
	}

	public void setShowType(boolean showType) {
		this.showType = showType;
	}

	public boolean isShowType() {
		return showType;
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
	 * @param idProduct the idProduct to set
	 */
	public void setIdProduct(Long idProduct) {
		this.idProduct = idProduct;
	}

	/**
	 * @return the idProduct
	 */
	public Long getIdProduct() {
		return idProduct;
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
		System.out.println("CodeType: " +codeType);
		banks= new ArrayList<SelectItem>();
		//client= user.getSystemUserById(idClient);
		banks.add(new SelectItem(-1,"NO REGISTRAR"));
		for(TbBank t: associationPayment.getBanksByClient2(codeClient,codeType)){
			if(t!=null){
				banks.add(new SelectItem(t.getBankId(), t.getBankName()));
			}

		}
		return banks;
	}

	/**
	 * @param products the products to set
	 */
	public void setProducts(List<SelectItem> products) {
		this.products = products;
	}

	/**
	 * @return the products
	 */
	public List<SelectItem> getProducts() {
		System.out.println("idBank " + idBank);
		System.out.println("codeType: "+codeType);
		System.out.println("idProduct en getProducts: "+idProduct);
		products= new ArrayList<SelectItem>();
		products.add(new SelectItem(-1, "NO REGISTRAR"));

		System.out.println("Entra a obtener productos");
		for(jpa.TbBankAccount tb: bankAccount.getProductsByClient2(idBank, codeClient,codeType)){
			String prod="Nro. Producto  " + tb.getBankAccountNumber();
			String type=(tb.getBankAccountType()!=null?tb.getBankAccountType().getProductTypeDescription():"");
			String var= prod + " -  " + type;
			products.add(new SelectItem(tb.getBankAccountId(), var));
		}
//		if(products.size()==0){
//			products.add(new SelectItem(0, "NO REGISTRAR"));
//		}


		
		return products;
	}

	public void setUserNames(String userNames) {
		this.userNames = userNames;
	}

	public String getUserNames() {
		return userNames;
	}

	public void setUserSecondNames(String userSecondNames) {
		this.userSecondNames = userSecondNames;
	}

	public String getUserSecondNames() {
		return userSecondNames;
	}

}