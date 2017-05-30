package crud;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import constant.AccountStateType;
import constant.EmailProcess;
import constant.LogAction;
import constant.LogReference;
import constant.ProcessTrackDetailType;
import constant.ProcessTrackType;

import security.UserLogged;
import sessionVar.SessionUtil;
import util.ObjectRechage;
import util.BankAccount;
import util.ws.WSRecharge;

import ejb.AssociationPaymentMethod;
import ejb.Log;
import ejb.Process;
import ejb.User;
import ejb.crud.Bank;
import ejb.email.Outbox;

import jpa.ReAccountBank;
import jpa.TbAccount;
import jpa.TbBank;
import jpa.TbBankAccount;
import jpa.TbProcessTrack;
import jpa.TbProductType;

import linews.InvokeSqdm;

/**
 * Bean to manage Association Account and products.
 * @author psanchez
 *
 */
public class AssociationPaymentMethodBean implements Serializable {
	private static final long serialVersionUID = -6247866949442545193L;
	
	@EJB(mappedName = "ejb/AssociationPaymentMethod")
	private AssociationPaymentMethod associationPaymentMethodEJB;
	
	@EJB(mappedName ="ejb/email/Outbox")
	private Outbox outbox;
	
	@EJB(mappedName="ejb/Process")
	private Process process;
			
	@EJB(mappedName="ejb/User")
	private User userEJB;
	
	@EJB(mappedName="ejb/Log")
	private Log log;
	
	@EJB(mappedName="ejb/Bank")
	private Bank bankEJB;
	
	@EJB(mappedName = "util/ws/WSRecharge")
	private WSRecharge WSrecharge;
		
	/** Control Modal **/
	private boolean showModal;
	private boolean showModalNeg;
	private boolean showInsert;
	private boolean showEdit;
	private boolean showConfirmation;
	private boolean showConfirmationPSE;
	private boolean showModalValidate;
	private boolean showModalValidatePSE;
	private boolean showOcult=true;
	private boolean showOcultPSE=false;
	private String corfirmMessage;
	private String modalMessage;
	private String modalMessageNeg;
	
	/** Atributos lista, consulta e ingreso asocio producto bancario-cuenta **/
	private List<SelectItem> clientAccounts;
	private Long accountId;

	private List<SelectItem> banks;
	private Long idBank=-1L;
	
	private List<SelectItem> products;
	private Long idProduct;	
	
    private List<BankAccount> accountBankList;
    private Long accountBankId;
	
    private List<SelectItem> bankAccountTypes;
	private Long idType;	
	
	private Long initValue=0L;
	
	private Long bankId;
	
	private Long bankAval;
	
	private boolean stateProduct;
		
	/** Código del Usuario que hizo login **/
	private String userCode;
	private UserLogged usrs;
	private Long userId;
	
	private int processError;
	/**
	 * Constructor.
	 */
	public AssociationPaymentMethodBean() {
		usrs = (UserLogged) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
	}
	
	@PostConstruct
	public void init(){
		userId=userEJB.getSystemMasterById(usrs.getUserId());
		this.getAccountBankList();
		this.setInitValue(0L);
	}
	

	public void initAssociateProductsAccountClient(){
		this.setShowInsert(true);
		this.setShowOcult(true);
		this.setShowOcultPSE(false);
		this.setAccountId(0L);
		this.setIdBank(0L);
		this.setIdType(0L);
	    this.setInitValue(0L);
	}
	
	public void showConfirm(){
		setShowInsert(false);
		setShowEdit(false);
		setShowConfirmation(true);
		setCorfirmMessage("¿Está seguro de realizar la Asociación del Producto Bancario?");
	}
	
	public void showConfirmRemove(){
		setShowInsert(false);
		setShowConfirmation(true);
		setCorfirmMessage("Está seguro de eliminar la Asociación del Producto Bancario?, si acepta tenga en cuenta que a partir de este momento su(s) vehículo(s) no podrán utilizar el servicio FacilPass.");
	}
	
	public void hideModal(){
		this.setShowInsert(true);
		this.setShowEdit(false);
		this.setShowModal(false);
		this.setShowConfirmation(false);
		this.setModalMessage(null);
		this.setShowModalNeg(false);
		this.setModalMessageNeg(null);
	}
	
	
	 public void hideModal2() {
			this.setShowInsert(false);
			this.setShowModal(false);
			this.setShowModalValidate(false);
			this.setShowModalValidatePSE(false);
			this.setShowConfirmation(false);
	 	    this.setShowConfirmationPSE(false);
	  }
	
	/**
	 * Método encargado de listar las cuentas asociadas al cliente en session.
	 * @return the accountBankList
	 * @author psanchez
	 */
	public List<BankAccount> getAccountBankList() {
		if(accountBankList == null) {
			accountBankList = new ArrayList<BankAccount>();
		}else{
			accountBankList.clear();
		}
		accountBankList = associationPaymentMethodEJB.getAccountBank(userId);
	  return accountBankList;		
    }
	
	
	/**
	 * Método encargado de asociar cuentas con producto bancario (cliente)
	 * @author psanchez
	 */
	public void addAssociateProductsAccountClient() {
	  this.setShowConfirmation(false);
	  this.setShowInsert(false);
	  
	  try{  
		    String result=null;
			if (accountId.longValue() != -1L) {	
				result=associationPaymentMethodEJB.haveProductAssociation(accountId);
				if(result!=null){
					setModalMessage(result);
					showConfirmation = false;
					showModal = true;
				}else if(idBank.longValue() != -1L){
					    if(idType.longValue() != -1L){
							if(idProduct.longValue()<=0){
								setModalMessage("Seleccione el Producto Bancario.");
								showConfirmation = false;
								showModal = true;
							}
							if(associationPaymentMethodEJB.haveAccountAssociation(idProduct)){
								setModalMessage("Este Producto Bancario ya está asociado a una Cuenta FacilPass.");
								showConfirmation = false;
								showModal = true;
							}else if(associationPaymentMethodEJB.isProcessDisociationById(idProduct, accountId)){
								setModalMessage("El producto bancario seleccionado se encuentra en proceso de disociación.");
								showConfirmation = false;
								showModal = true;
							}else{
					           setModalMessage("¿Está seguro de realizar la Asociación del Producto Bancario?");
							   showModal = false;
							   showConfirmation = true;
							}
					    } else {
							setModalMessage("Seleccione el Tipo de Cuenta.");
							showConfirmation = false;
							showModal = true;
						}
		         } else {
					setModalMessage("Seleccione la Entidad.");
					showConfirmation = false;
					showModal = true;
				 }
		   }else {
			  setModalMessage("Seleccione la Cuenta FacilPass.");
			  showConfirmation = false;
			  showModal = true;
		   }
		 }catch(Exception ex){
			System.out.println(" [ AssociationPaymentMethodBean.addAssociateProductsAccountClient.Exception ] ");
			ex.printStackTrace();
		 }
	}
	
	 
	/**
	 * Método encargado de asociar cuenta FacilPass con PSE (cliente)
	 * @author psanchez
	 */
	public void addAssociateProductsAccountClientPSE() {
		  this.setShowConfirmation(false);
		  this.setShowInsert(false);
		  String result=null;
		  Long bankAccountId=0L;
		try{  
			 if(accountId.longValue() != -1L) {	
				result=associationPaymentMethodEJB.haveProductAssociation(accountId);
				if(result!=null){
					setModalMessage(result);
					showConfirmation = false;
					showModal = true;
				} else if(idBank.longValue() != -1L){
				    bankAccountId=associationPaymentMethodEJB.getIdFreePSE(userId, accountId, idBank);
				    String bankName=bankEJB.haveBankName(idBank);
				    if(bankAccountId!=null && associationPaymentMethodEJB.haveAccountAssociation(bankAccountId)==false && 
				       associationPaymentMethodEJB.getAccountAssociation(accountId)==false){
						System.out.println("[ PSE libre ]");
						   this.stateProduct=false;	
						   this.idProduct=Long.valueOf(bankAccountId).longValue();
				           setModalMessage("¿Está seguro de realizar la Asociación de su cuenta FacilPass a la entidad "+ bankName+", de esta forma su cuenta estará en modalidad prepago.?");
						   showModal = false;
						   showConfirmationPSE = true;
				     }else {
							System.out.println("[ PSE nuevo ]");
						   this.stateProduct=true;	
				    	   Long pseId=associationPaymentMethodEJB.getIdPSE(userId, SessionUtil.sessionUser().getUserId());
						   this.idProduct=Long.valueOf(pseId).longValue();
				           setModalMessage("¿Está seguro de realizar la Asociación de su cuenta FacilPass a la entidad "+ bankName+", de esta forma su cuenta estará en modalidad prepago.?");
						   showModal = false;
						   showConfirmationPSE = true;
					 }
			      }else {
					setModalMessage("Seleccione la Entidad.");
					showConfirmation = false;
					showModal = true;
				  }
			   } else {
				  setModalMessage("Seleccione la Cuenta FacilPass.");
				  showConfirmation = false;
				  showModal = true;
			   }
		 }catch(Exception ex){
				System.out.println(" [ AssociationPaymentMethodBean.addAssociateProductsAccountClientPSE.Exception ] ");
				ex.printStackTrace();
			 }
		}
	

		 
	public void saveAssociateProductsAccountClientPSE(){
	    this.setModalMessage(null);
		this.setShowModal(false);
		this.setShowModalValidatePSE(false);
		this.setShowConfirmation(false);
	   try{	
			//Se valida si se esta asociando el mismo producto bancario
			System.out.println("stateProduct------: "+stateProduct);
			if(stateProduct==false){
				int respSame = associationPaymentMethodEJB.samePreviousProductAssociationPSE(accountId, idProduct, 
						SessionUtil.sessionUser().getUserId(), SessionUtil.ip(),userId);
				System.out.println("respSame------: "+respSame);
				if(respSame==0){
					outbox.insertMessageOutbox(userEJB.getSystemUser(SessionUtil.sessionUser().getUserId()).getUserId(), 
			                   EmailProcess.REACTIVATION_ASSOCIATION_ACCOUNT_BANK,
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
					
					setModalMessage("Transacción Exitosa. Para hacer uso del servicio FacilPass; es necesario tener vehículos registrados y " +
							"asignar recursos el cual será indicado en el momento en que se definan las categorías de los vehículos asociados.");
					setShowModal(false);
					setShowConfirmation(false);
					setShowModalValidatePSE(true);
					setAccountBankList(null);
				}else if(respSame==1){
					//Se obtiene el ultimo producto bancario asociado 
					Long lastIdBankAccount = associationPaymentMethodEJB.getLastBankingProduct(accountId);			
					Long accountAssociate = associationPaymentMethodEJB.associateAccountPSE(userId, accountId, 
							SessionUtil.sessionUser().getUserId(), SessionUtil.ip(), idBank, idProduct, initValue);
					
					System.out.println("accountAssociate: "+accountAssociate);
					System.out.println("lastIdBankAccount->"+lastIdBankAccount);
					
					if (accountAssociate != null && idProduct>0L) {
							associationPaymentMethodEJB.processAssociationPSE(userId, accountId, SessionUtil
								                  .sessionUser().getUserId(), SessionUtil.ip(), idBank, idProduct, lastIdBankAccount);
		
							setModalMessage("Transacción Exitosa. Para hacer uso del servicio FacilPass; es necesario tener vehículos registrados y " +
									"asignar recursos el cual será indicado en el momento en que se definan las categorías de los vehículos asociados.");
							setShowModal(false);
							setShowConfirmation(false);
							setShowModalValidatePSE(true);
							setShowOcult(true);
							setAccountBankList(null);							
			            }else{
			    			setModalMessage("Error en Transacción.");
			    			setShowModal(false);
							setShowConfirmation(false);
							setShowModalValidate(true);
							
							/** Se agrega mensaje en Seguimiento de procesos y Ver procesos segun FRD064
							* @author Nramirez
							*/
			    			System.out.println(" [ AssociationPaymentMethodBean.saveAssociateProductsAccountClientPSE.Error en Transacción.1 ] ");
							this.associationPaymentMethodEJB.createProcessAssociationPSE(SessionUtil.sessionUser().getUserId(),accountId,SessionUtil.ip());
					    }
				}else if(respSame==2){
	    			System.out.println(" [ AssociationPaymentMethodBean.saveAssociateProductsAccountClientPSE.Error en Transacción.2 ] ");
					setModalMessage("Error en la Transacción");
				    setShowModal(false);
					setShowConfirmation(false);
					setShowModalValidate(true);
				}
			}else if(stateProduct==true) {
				//Se obtiene el ultimo producto bancario asociado 
				Long lastIdBankAccount = associationPaymentMethodEJB.getLastBankingProduct(accountId);			
				Long accountAssociate = associationPaymentMethodEJB.associateAccountNewPSE(userId, accountId, 
						SessionUtil.sessionUser().getUserId(), SessionUtil.ip(), idBank, idProduct, initValue);
				
				System.out.println("lastIdBankAccountPSE-->"+lastIdBankAccount);
				System.out.println("accountAssociate-->: "+accountAssociate);
				Long bankAccountIdNew = associationPaymentMethodEJB.BankAccountNewPSE(userId, idBank, idProduct);
				if (accountAssociate != null && bankAccountIdNew>0L){
						associationPaymentMethodEJB.processAssociationPSE(userId, accountId, SessionUtil
							                  .sessionUser().getUserId(), SessionUtil.ip(), idBank, bankAccountIdNew, lastIdBankAccount);
	
						setModalMessage("Transacción Exitosa. Para hacer uso del servicio FacilPass; es necesario tener vehículos registrados y " +
								"asignar recursos el cual será indicado en el momento en que se definan las categorías de los vehículos asociados.");
						setShowModal(false);
						setShowConfirmation(false);
						setShowModalValidatePSE(true);
						setShowOcult(true);
						setAccountBankList(null);							
		            }else{
		    			System.out.println(" [ AssociationPaymentMethodBean.saveAssociateProductsAccountClientPSE.Error en Transacción.3 ] ");
		    			setModalMessage("Error en Transacción.");
		    			setShowModal(false);
						setShowConfirmation(false);
						setShowModalValidate(true);
						
						/** Se agrega mensaje en Seguimiento de procesos y Ver procesos segun FRD064
						* @author Nramirez
						*/	
						this.associationPaymentMethodEJB.createProcessAssociationPSE(SessionUtil.sessionUser().getUserId(),accountId,SessionUtil.ip());
				    }
				}
		 }catch(NullPointerException n){
		    setModalMessage("Error en la Transacción");
		    setShowModal(false);
			setShowConfirmation(false);
			setShowModalValidate(true);
			
			/** Se agrega mensaje en Seguimiento de procesos y Ver procesos segun FRD064
			* @author Nramirez
			*/	
			System.out.println(" [ AssociationPaymentMethodBean.saveAssociateProductsAccountClientPSE.NullPointerException ] ");
			this.associationPaymentMethodEJB.createProcessAssociationPSE(SessionUtil.sessionUser().getUserId(),accountId,SessionUtil.ip());
		 }
	 }
	
	public void onComplete() {  
	       FacesContext.getCurrentInstance().
	       addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
	       		"Transacción en proceso...", "Transacción en proceso..."));  
	}
	

	 @SuppressWarnings("unused")
	public void saveAssociateProductsAccountClient(){
	   try{
		    this.setModalMessage(null);
			this.setShowModal(false);
			this.setShowModalValidate(false);
			this.setShowConfirmation(false);

			String msgEmailR=null;
			Long responseServiceRecharge = null;
			BigDecimal umbral= new BigDecimal(0);
			/** Se valida si se esta asociando el mismo producto bancario*/
			int respSame = associationPaymentMethodEJB.samePreviousProductAssociation(accountId, idProduct,SessionUtil.sessionUser().getUserId(), SessionUtil.ip(),userId);
			//0 si es el mismo producto bancario, 1 no es el mismo producto bancario, 2 error en el proceso de verificacion
			if(respSame==0){
				outbox.insertMessageOutbox(userEJB.getSystemUser(SessionUtil.sessionUser().getUserId()).getUserId(), 
		                   EmailProcess.REACTIVATION_ASSOCIATION_ACCOUNT_BANK,
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
				
				setModalMessage("Transacción Exitosa.");
				setShowModal(false);
				setShowConfirmation(false);
				setShowModalValidate(true);
				setAccountBankList(null);
			}else if(respSame==1){
				/** Se obtiene el ultimo producto bancario asociado */
				Long lastIdBankAccount = associationPaymentMethodEJB.getLastBankingProduct(accountId);
				System.out.println("(1)lastIdBankAccount: "+lastIdBankAccount);
				
				Long accountFP = associationPaymentMethodEJB.associateProductsAccount(userId, accountId, userCode, 
						SessionUtil.sessionUser().getUserId(), SessionUtil.ip(), idBank, idProduct, initValue);
								
				if (accountFP != null && idProduct>0) {
					    Object[] o=userEJB.getInfAssociationProdcut(accountFP,idProduct);
					    String bankName=bankEJB.haveBankName(idBank);	
						InvokeSqdm account2= new InvokeSqdm();
						String data=account2.InvokeAccountAdmin(o);
						String dataServiceArray[] = data.split(";");
					    Long resp=Long.parseLong(dataServiceArray[0].toString().trim());
	                    String nameFileRq=dataServiceArray[1].toString().trim();
	                    String nameFileRp=dataServiceArray[2].toString().trim();
	                    System.out.println("DATOS TRAIDOS DESDE EL WEBSERVICE"+dataServiceArray[0].toString().trim());
						System.out.println("DATOS TRAIDOS DESDE EL WEBSERVICE"+dataServiceArray[1].toString().trim());
				        System.out.println("DATOS TRAIDOS DESDE EL WEBSERVICE"+dataServiceArray[2].toString().trim());
				        String msnError=process.getResponseDescByCode(resp, 1L);
						
				        boolean responseNegative=false;
						if(resp!=0){
							
							processError=1;
							responseNegative=userEJB.deleteAssociation(idProduct, accountFP);
							System.out.println("Se eliminó asociacion");
							setModalMessage(msnError);
							setShowModal(false);
							setShowConfirmation(false);
							setShowModalValidate(true);
							setAccountBankList(null);
							String respLog = "Se creó Cuenta FacilPass No: " + accountFP + " pero no se recibio respuesta positiva desde el webservices, respuesta : "+ resp + " por lo tanto no se asoció el producto bancario seleccionado." +idProduct;									
							
							outbox.insertMessageOutbox(userEJB.getSystemUser(SessionUtil.sessionUser().getUserId()).getUserId(), 
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
							log.insertLog("Asociación Cuenta Cliente:" + respLog, LogReference.BANKACCOUNT, LogAction.CREATE,
									SessionUtil.ip(), SessionUtil.sessionUser().getUserId());
							
						
							
							//proceso administrador
					        TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, userId);
							Long idPTA;
								if(pt==null){
									idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  userId,
											SessionUtil.ip(), SessionUtil.sessionUser().getUserId());
								}else{
									idPTA=pt.getProcessTrackId();
								}
							Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.CLIENT_ACCOUNT_ASSOCIATION, 
									"La asociación del producto bancario "+associationPaymentMethodEJB.getProductBank(idProduct)+ " de la entidad "+bankName+
									   " con la cuenta FacilPass "+ accountId + " presentó el siguiente error: "+
									   process.getResponseDescByCode(resp, 1L)+" - "+resp,
									   SessionUtil.sessionUser().getUserId()
									   , SessionUtil.ip(), 1, "", processError,nameFileRq,nameFileRp,null,null,0L);
												
							//proceso cliente
							TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,userId);
							Long idPTC;
								if(ptc==null){
									idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, userId, SessionUtil.ip(), 
											SessionUtil.sessionUser().getUserId());
								}else{
									idPTC=ptc.getProcessTrackId();
								}
							Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.CLIENT_ACCOUNT_ASSOCIATION, 
									"La asociación del producto bancario "+associationPaymentMethodEJB.getProductBank(idProduct)+ " de la entidad "+bankName+
									   " con la cuenta FacilPass "+ accountId + " presentó el siguiente error: "+
									   process.getResponseDescByCode(resp, 1L), SessionUtil.sessionUser().getUserId(), 
									 SessionUtil.ip(), 1, "", null,null,null,null);
						}else{
							
							processError=0;
												
							Long responseService = associationPaymentMethodEJB.processAssociation(userId, userCode, accountId, SessionUtil
								                  .sessionUser().getUserId(), SessionUtil.ip(), idBank, idProduct, resp, lastIdBankAccount);
		
							Long bankId = null;
							List<Object> lis =userEJB.initRecharge(resp, accountFP);
							for(Object ob : lis){
								umbral= (BigDecimal) ob;
								System.out.println("umbral para recarga en AccountAdmin" + umbral);
		
								//Object[] obj=userEJB.recharge(umbral);
								ObjectRechage obj = WSrecharge.createListObj(umbral.longValue());
								String dataService=account2.InvokeRecharge(obj);
								System.out.println("**********DATOS QUE TRAE DE LA BASE DE DATOS****** "+dataService);
								
								 String[] array = data.split(";");
								     responseServiceRecharge=Long.parseLong(array[0]);
								     String message=process.getResponseDescByCode(responseServiceRecharge, 1L);
								     System.out.println("**********DATO SEPARADO POR EL SPLIT   ****** "+responseServiceRecharge);
										
								 bankId = Long.parseLong(obj.getBankId());
								System.out.println("Respuesta webservice recarga: " + responseServiceRecharge);
								if(responseServiceRecharge==0L){
									 setModalMessage(message);
									 setShowModal(false);
									 setShowConfirmation(false);
									 setShowModalValidate(true);
									 msgEmailR="Estimado Usuario, \r\n \r\nLa asignación de recursos para su cuenta Facilpass " + accountFP + " ha sido aprobada. Por favor ingrese a www.facilpass.com con su usuario y contraseña y verifique su saldo. \r\n \r\nCordialmente, \r\nFacilpass";
									 setAccountBankList(null);
								}else{
									setModalMessage(message);
									setShowModal(false);
									setShowConfirmation(false);
									setShowModalValidate(true);
									msgEmailR="Estimado Usuario, \r\n \r\nLa asignacion de recursos para su cuenta Facilpass " + accountFP + " ha sido rechazada. Por favor comuníquese con la linea 018000xxxxxxx para conocer el detalle del rechazo y los pasos a seguir. \r\n \r\nCordialmente, \r\nFacilpass";
								}
							}
						setModalMessage("Transacción Exitosa.");
						setShowModal(false);
						setShowConfirmation(false);
						setShowModalValidate(true);
						setAccountBankList(null);
							
						  if(msgEmailR!=null){
							  Long transactionId = WSrecharge.getTransactionByUmbral(umbral.longValue());
							  
							  if(responseServiceRecharge==0L){
								  outbox.insertMessageOutbox(userEJB.getSystemUser(SessionUtil.sessionUser().getUserId()).getUserId(), 
						                   EmailProcess.RESOURCE_ALLOCATION_SUCCESSFUL,
						                   accountId,
						                   idProduct, 
						                   transactionId, 
						                   null,
						                   null,
						                   null,
						                   bankId,
						                   null,
						                   SessionUtil.sessionUser().getUserId(),
						                   null,
						                   null,
						                   null,
						                   true,
						                   null); 
								  
								  userEJB.createProcessRechargeClient(SessionUtil.sessionUser().getUserId(),SessionUtil.sessionUser().getUserId(), SessionUtil.ip() ,accountFP, idProduct, idBank);
							      log.insertLog("Respuesta de servicio de recarga:" + responseServiceRecharge, LogReference.BANKACCOUNT, LogAction.CREATE,
										SessionUtil.ip(), SessionUtil.sessionUser().getUserId());
								}else{
									outbox.insertMessageOutbox(userEJB.getSystemUser(SessionUtil.sessionUser().getUserId()).getUserId(),
							                   EmailProcess.RESOURCE_ALLOCATION_REJECTED,
							                   accountId,
							                   idProduct, 
							                   transactionId, 
							                   null,
							                   null,
							                   null,
							                   bankId,
							                   null,
							                   SessionUtil.sessionUser().getUserId(),
							                   null,
							                   null,
							                   null,
							                   true,
							                   null);
								}
						    }
						  	  log.insertLog("Respuesta de servicio de recarga:" + resp, LogReference.BANKACCOUNT, LogAction.CREATE,
										SessionUtil.ip(), SessionUtil.sessionUser().getUserId());					      
					   }							
				}else {
					System.out.println(" [ AssociationPaymentMethodBean.saveAssociateProductsAccountClient.Error en Transacción1 ] ");
					setModalMessage("Error en Transacción.");
					setShowModal(false);
					setShowConfirmation(false);
					setShowModalValidate(true);
				}
			}else if(respSame==2){
				System.out.println(" [ AssociationPaymentMethodBean.saveAssociateProductsAccountClient.Error en Transacción2 ] ");
				setModalMessage("Error en la Transacción");
			    setShowModal(false);
				setShowConfirmation(false);
				setShowModalValidate(true);
			}			  
		 }catch(NullPointerException n){
			System.out.println(" [ AssociationPaymentMethodBean.saveAssociateProductsAccountClient.NullPointerException ] ");
		    setModalMessage("Error en la Transacción");
		    setShowModal(false);
			setShowConfirmation(false);
			setShowModalValidate(true);
		 }
	 }
	 
	 
	 
	 
	
	/**
	 * Método encargado de desactivar relación cuenta-producto bancario (re_account_bank) cliente en session.
	 * @author psanchez
	 */
	public void cancelAssociationBankAccount(){
		this.setShowConfirmation(false);
		if(userId!=null){
			ReAccountBank rab = associationPaymentMethodEJB.validateBalanceAccount(accountBankId);
			if(rab!=null){
				if((rab.getAccountId().getAccountBalance().compareTo(new BigDecimal(0))==0) || (rab.getAccountId().getAccountBalance().compareTo(new BigDecimal(0))==1) )	{					
					if (associationPaymentMethodEJB.dissociationBankAccount(userId, accountBankId, bankId, SessionUtil.ip(), SessionUtil.sessionUser().getUserId()) != null){
						/**
						 * Se realiza la cancelacion de la recargas programadas de la cuenta que se esta disociando
						 * @author ablasquez
						 */
						userEJB.cancelAutomaticRechage(rab.getAccountId().getAccountId());
						this.setModalMessage("Transacción Exitosa.");
						this.setAccountBankList(null);
						this.setShowModal(true);
					}else{
						System.out.println(" [ AssociationPaymentMethodBean.cancelAssociationBankAccountt.Error en Transacción1 ] ");
						this.setModalMessage("Error en Transacción.");
						this.setAccountBankList(null);
						this.setShowModal(true);
					}
				  } else{
					  this.setModalMessageNeg("No es posible realizar la desvinculación de la cuenta ya que su saldo es negativo, para poderlo realizar primero lo invitamos a asignar recursos por un valor de $"+rab.getAccountId().getAccountBalance().abs());
					  this.setAccountBankList(null);
					  this.setShowModalNeg(true);
				  }
			}else{
				System.out.println(" [ AssociationPaymentMethodBean.cancelAssociationBankAccountt.Error en Transacción2 ] ");
				this.setModalMessage("Error en Transacción.");
				this.setAccountBankList(null);
				this.setShowModal(true);
			}
		}else{
			this.setAccountBankList(null);
		}
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
		clientAccounts.add(new SelectItem(-1L, "--Seleccione Cuenta--"));
		for(TbAccount su : associationPaymentMethodEJB.getClientAccount(userId)){
			if((su.getAccountState()!=AccountStateType.CLOSE.getId()) && (su.getAccountState()!=AccountStateType.TO_CLOSE.getId())){
				clientAccounts.add(new SelectItem(su.getAccountId(), "Cuenta No. " + su.getAccountId()));			
			}
		}
		return clientAccounts;
	}
	
	/**
	 * Método encargado de listar productos bancarios asociadas cliente en sesión.
	 * @return the products
	 * @author psanchez
	 */
	public List<SelectItem> getProducts() {		
		products= new ArrayList<SelectItem>();
		if(idBank!=null && idType!=null){
			for(TbBankAccount tb: associationPaymentMethodEJB.getProductsByClient(idBank, idType, userId)){
				products.add(new SelectItem(tb.getBankAccountId(), tb.getBankAccountNumber()));
			}
			if(products.size()==0){
				products.add(new SelectItem(0, "No existe información"));
			}
		}
		else{
			products.add(new SelectItem(-1, "Seleccione"));
		}
		return products;
	}
	
	
	/**
	 * Método encargado de llenar el combo de bancos del cliente en sesión.
	 * @return the banks
	 * @author psanchez
	 */
		public List<SelectItem> getBanks() {						
			if(banks == null){
				banks = new ArrayList<SelectItem>();
			} else {
				banks.clear();
			}
			banks.add(new SelectItem(-1L, "--Seleccione Entidad--"));
			for(TbBank t: associationPaymentMethodEJB.getBanksByClient(userId)){
				banks.add(new SelectItem(t.getBankId(),t.getBankName()));
			}
			return banks;
		}
		
	
		/**
		 * Método encargado de llenar el combo de tipo de producto bancario relacionados al cliente en sesión.
		 * @return the banks
		 * @author psanchez
		 */
		public List<SelectItem> getBankAccountTypes() {
			if(bankAccountTypes == null){
				bankAccountTypes = new ArrayList<SelectItem>();
			} else {
				bankAccountTypes.clear();				
			}
			bankAccountTypes.add(new SelectItem(-1,"--Seleccione Tipo Cuenta--"));
			for(TbProductType t: associationPaymentMethodEJB.getProductTypeByClient(idBank, userId)){
				 bankAccountTypes.add(new SelectItem(t.getProductTypeId(), t.getProductTypeDescription()));
			}
		  return bankAccountTypes;
		}
	
	/**
	 * Método encargado de ocultar en el jsf el tipo producto y producto bancario al seleccionar PSE.
	 * @return the banks
	 * @author psanchez
	 */
	public void changeMenu(ValueChangeEvent event){
		idBank = (Long) event.getNewValue();
		Long bankId = bankEJB.getOtherBanks(idBank);
		if (bankId!=null){
			this.setShowOcult(false);
			this.setShowOcultPSE(true);
		}else{
			this.setShowOcultPSE(false);
			this.setShowOcult(true);
		}
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
	 * @param showEdit the showEdit to set
	 */
	public void setShowEdit(boolean showEdit) {
		this.showEdit = showEdit;
	}

	/**
	 * @return the showEdit
	 */
	public boolean isShowEdit() {
		return showEdit;
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
	 * @param banks the banks to set
	 */
	public void setBanks(List<SelectItem> banks) {
		this.banks = banks;
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
	 * @param bankAccountTypes the bankAccountTypes to set
	 */
	public void setBankAccountTypes(List<SelectItem> bankAccountTypes) {
		this.bankAccountTypes = bankAccountTypes;
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
	 * 
	 * @param clientAccounts
	 */
	public void setClientAccounts(List<SelectItem> clientAccounts) {
		this.clientAccounts = clientAccounts;
	}


	/**
	 * @param associationPaymentMethod the associationPaymentMethod to set
	 */
	public void setAssociationPaymentMethod(AssociationPaymentMethod associationPaymentMethod) {
		this.associationPaymentMethodEJB = associationPaymentMethod;
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
	

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}


	public void setIdType(Long idType) {
		this.idType = idType;
	}

	public Long getIdType() {
		return idType;
	}
	

	/**
	 * @param accountBankList the accountBankList to set
	 */
	public void setAccountBankList(List<BankAccount> accountBankList) {
		this.accountBankList = accountBankList;
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
	 * @param userCode the userCode to set
	 */
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	/**
	 * @return the userCode
	 */
	public String getUserCode() {
		String userCode=userEJB.getSystemUserCode(userId);
		return userCode;
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

	public void setShowModalNeg(boolean showModalNeg) {
		this.showModalNeg = showModalNeg;
	}

	public boolean isShowModalNeg() {
		return showModalNeg;
	}

	public void setModalMessageNeg(String modalMessageNeg) {
		this.modalMessageNeg = modalMessageNeg;
	}

	public String getModalMessageNeg() {
		return modalMessageNeg;
	}

	public void setShowOcult(boolean showOcult) {
		this.showOcult = showOcult;
	}

	public boolean isShowOcult() {
		return showOcult;
	}

	public void setShowConfirmationPSE(boolean showConfirmationPSE) {
		this.showConfirmationPSE = showConfirmationPSE;
	}

	public boolean isShowConfirmationPSE() {
		return showConfirmationPSE;
	}

	public void setShowOcultPSE(boolean showOcultPSE) {
		this.showOcultPSE = showOcultPSE;
	}

	public boolean isShowOcultPSE() {
		return showOcultPSE;
	}

	public void setShowModalValidatePSE(boolean showModalValidatePSE) {
		this.showModalValidatePSE = showModalValidatePSE;
	}

	public boolean isShowModalValidatePSE() {
		return showModalValidatePSE;
	}

	public void setBankId(long bankId) {
		this.bankId = bankId;
	}

	public long getBankId() {
		return bankId;
	}

	/**
	 * @param bankAval the bankAval to set
	 */
	public void setBankAval(Long bankAval) {
		this.bankAval = bankAval;
	}

	/**
	 * @return the bankAval
	 */
	public Long getBankAval() {
		return bankAval;
	}

	public void setStateProduct(boolean stateProduct) {
		this.stateProduct = stateProduct;
	}

	public boolean getStateProduct() {
		return stateProduct;
	}
	
}