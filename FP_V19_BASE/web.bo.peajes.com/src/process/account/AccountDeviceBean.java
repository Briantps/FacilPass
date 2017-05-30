/**
 * 
 */
package process.account;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import security.UserLogged;

import jpa.TbAccount;
import jpa.TbCodeType;
import jpa.TbDevice;
import jpa.TbSystemUser;

import ejb.Device;
import ejb.Transaction;
import ejb.User;
import constant.DistributionType;

/**
 * @author tmolina
 *
 */
public class AccountDeviceBean implements Serializable{
	private static final long serialVersionUID = 8553061711544184301L;	

	@PersistenceContext(unitName = "bo") 
	EntityManager em;
	
	@EJB(mappedName = "ejb/User")
	private User userEJB;
	
	@EJB(mappedName = "ejb/Device")
	private Device deviceEJB;
	
	@EJB(mappedName = "ejb/Transaction")
	private Transaction transactionEJB;
	
	// Attributes --- //
	private List<SelectItem> clientAccounts;
	private List<SelectItem> myClientAccounts;
	private List<SelectItem> codeTypesList;
	
	private List<TbDevice> devicesList;
	private Long deviceId;
	private List<TbCodeType> codeTypes;
	private Long codeType;
	private List<TbAccount> accountList;
	

	private Long availableBalance;
	private Long idClient;
	private Long idClientAccount= null;
	private String idClientAccountStr= null;
	private Long result = 0L;
	private List<TbAccount> availableBalanceUser;
	private String codeClient;
		
	private TbSystemUser client;
	
	private boolean renderAste = false;
	
   // Control Modal ------//
	private boolean showManualDistribution;
	private boolean showData;
	private boolean showMessage;
	private boolean showMessageError;
	private boolean showModal;
	private String modalMessage;
	private String messageError;
	
	private UserLogged usrs;
		
	/**
	 * Constructor.
	 */
	public AccountDeviceBean() {
		usrs = (UserLogged) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
	}
	
	// Actions --- //
	
	/**
	 * 
	 * @return
	 */
	public String init() {
		setShowData(false);
		setShowMessageError(false);
		return null;
	}
	
	/**
	 * Método encargado de buscar dispositivos por tipo de documento y número de documento ó cuenta facilpass.
	 * @author psanchez
	 */
	public String search() {
	 try{
		 if (codeClient != "") {
			 if (idClientAccount!= -1L) {
				 System.out.println(" [ ingrese por aqui AccountDeviceBean.search ] ");
				devicesList = deviceEJB.getDevicesByAccount(idClientAccount);
				if (devicesList.size() > 0) {
					this.setAvailableBalance(transactionEJB.getAvailableBalanceAccount(idClientAccount));
					this.setShowManualDistribution(true);
					TbAccount ta =em.find(TbAccount.class, idClientAccount);
					if (ta.getDistributionTypeId().getDistributionTypeId().equals(DistributionType.BAGMONEY.getDistributionID())) {
						result = availableBalance;
						this.setShowManualDistribution(false);
					}else if (ta.getDistributionTypeId().getDistributionTypeId().equals(DistributionType.AUTOMATIC.getDistributionID())) {
						result = availableBalance / devicesList.size();
						this.setShowManualDistribution(true);
					}
					this.setShowData(true);
					this.setShowMessageError(false);
				} else {
					this.setMessageError("No se encontraron resultados de la búsqueda.");
					this.setShowData(false);
					this.setShowMessageError(true);
				}
			 }else {
					this.setShowMessageError(true);
					this.setMessageError("Seleccione una cuenta de cliente." );
					this.setShowData(false);	
				}
			}else {
				this.setShowMessageError(true);
				this.setMessageError("Digite el número de identificación del cliente" );
				this.setShowData(false);	
			}
		} catch (Exception e) {
			   System.out.println(" [ Error en AccountDeviceBean.search ] ");
			   this.setMessageError("Error en transación");
			   e.printStackTrace();
		} 
		return null;
	}
	
	public void getInfoDevicesByFilters() {
	  try{
			if ((codeClient.equals(null) || codeClient.equals("")) 
					&& (idClientAccountStr == null || idClientAccountStr.equals("")) 
					&& (codeType<0L)) { 
			    this.setMessageError("No existe datos para realizar la búsqueda." );
			    this.setShowData(false);
			    this.setShowMessageError(true);
			  }else if ((codeClient.equals(null) || codeClient.equals("")) && (codeType>0L)){
				    this.setMessageError("Digite No. de Identificación." );
				    this.setShowData(false);
				    this.setShowMessageError(true);
			      }else if (codeClient.length()>0 && codeType<0L){
			    	  if(!codeClient.matches("([0-9]|\\s)+")){
			    		  this.setMessageError("El número de identificación tiene caracteres inválidos, por favor verifique.");
			    	  }else{
			    		  this.setMessageError("Seleccione Tipo Documento." );
			    	  }
			    	  this.setShowData(false);
			    	  this.setShowMessageError(true);
	  				}else {
	  					if((codeClient!=null && !codeClient.equals("")) && !codeClient.matches("([0-9]|\\s)+")){
	  						this.setMessageError("El número de identificación tiene caracteres inválidos, por favor verifique.");
							this.setShowData(false);
							this.setShowMessageError(true);
	  					}else if((idClientAccountStr!=null && !idClientAccountStr.equals("")) 
	  							&& !idClientAccountStr.matches("([0-9]|\\s)+")){
	  						this.setMessageError("El campo Cuenta Facilpass tiene caracteres inválidos, por favor verifique.");
							this.setShowData(false);
							this.setShowMessageError(true);
	  					}else{
	  						if(idClientAccountStr==null || idClientAccountStr.equals("")){
	  							idClientAccount=null;
	  						}else{
	  							idClientAccount=Long.parseLong(idClientAccountStr);
	  						}
	  						devicesList = deviceEJB.getInfoDevicesByFilters(codeType,idClientAccount,codeClient);
							if (devicesList.size() > 0) {
								availableBalanceUser=transactionEJB.getAvailableBalanceUserAccount(codeType,idClientAccount,codeClient);
								this.setShowManualDistribution(true);
								if(idClientAccount==null){
									devicesList.clear();
								}
								for (TbAccount ta : availableBalanceUser) {
									if(idClientAccount!=null){
										if (ta.getDistributionTypeId().getDistributionTypeId().equals(DistributionType.BAGMONEY.getDistributionID())) {
											result = ta.getAccountBalance().longValue();
											this.setShowManualDistribution(false);
										}else if (ta.getDistributionTypeId().getDistributionTypeId().equals(DistributionType.AUTOMATIC.getDistributionID())) {
											result = ta.getAccountBalance().longValue() / devicesList.size();
											this.setShowManualDistribution(true);
										}
									}else{
										List<TbDevice> devAux=new ArrayList<TbDevice>();
										devAux.clear();
										devAux=deviceEJB.getInfoDevicesByFilters(codeType,ta.getAccountId(),codeClient);
										this.setShowManualDistribution(true);
										if (ta.getDistributionTypeId().getDistributionTypeId().equals(DistributionType.BAGMONEY.getDistributionID())) {
											result = ta.getAccountBalance().longValue();
											for(int i=0;i<devAux.size();i++){
												TbDevice dev=devAux.get(i);
												dev.setDeviceCurrentBalance(new BigDecimal(result));
												devicesList.add(dev);
											}
										}else if (ta.getDistributionTypeId().getDistributionTypeId().equals(DistributionType.AUTOMATIC.getDistributionID())) {
											result = ta.getAccountBalance().longValue() / devAux.size();
											for(int i=0;i<devAux.size();i++){
												TbDevice dev=devAux.get(i);
												devicesList.add(dev);
											}
										}else if (ta.getDistributionTypeId().getDistributionTypeId().equals(DistributionType.MANUAL.getDistributionID())) {
											for(int i=0;i<devAux.size();i++){
												TbDevice dev=devAux.get(i);
												devicesList.add(dev);
											}
										}
									}
									this.setShowData(true);
									this.setShowMessageError(false);
								}
							} else {
								this.setMessageError("No se encontraron resultados de la búsqueda.");
								this.setShowData(false);
								this.setShowMessageError(true);
							}
	  					}
				}
	        }catch (Exception e) {
				   System.out.println(" [ Error en AccountDeviceBean.getInfoDevicesByFilters ] ");
				   this.setMessageError("Error en transación");
				   e.printStackTrace();
			} 
	}
	
	public String changeTypeDoc() {
		if(getCodeType().equals(-1L)){
			setRenderAste(false);
		}else{
			setRenderAste(true);
		}
		this.setShowMessageError(false);
		this.setShowMessage(false);
		this.showData = false;
		this.idClient = null;
		this.codeClient = null;
		this.idClientAccount = null;
		this.idClientAccountStr = null;
		return null;
	}
	
	public void clearFilter(){
		setRenderAste(false);
		this.showData = false;
		idClientAccount  = null;
		idClientAccountStr  = null;
		codeClient = null;
		codeType= -1L;
	}
	
	/**
	 * Hides modal windows.
	 */
	public String hideModal(){
		this.setShowMessageError(false);
		this.setMessageError(null);
		return null;
	}
	

	// Getters and Setters --- //

	public boolean isShowManualDistribution() {
		return showManualDistribution;
	}

	public void setShowManualDistribution(boolean showManualDistribution) {
		this.showManualDistribution = showManualDistribution;
	}

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
		if(clientAccounts == null){
			clientAccounts = new ArrayList<SelectItem>();
		} else {
			clientAccounts.clear();
		}
		clientAccounts.add(new SelectItem(-1L, "--Seleccione Una--"));
		for(TbAccount su : userEJB.getClientAccount(null)){
			String name = su.getTbSystemUser().getUserNames();
			if(su.getTbSystemUser().getTbCodeType().getCodeTypeId().longValue() != 3L){
				name += " " + su.getTbSystemUser().getUserSecondNames();
			}
			clientAccounts.add(new SelectItem(su.getAccountId(),"Cuenta No. " + su.getAccountId() + "-" + name ));
		}
		return clientAccounts;
	}

	public void setShowData(boolean showData) {
		this.showData = showData;
	}

	public boolean isShowData() {
		return showData;
	}

	public void setDevicesList(List<TbDevice> devicesList) {
		this.devicesList = devicesList;
	}

	public List<TbDevice> getDevicesList() {
		return devicesList;
	}

	public Long getResult() {
		return result;
	}

	public void setResult(Long result) {
		this.result = result;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	public Long getDeviceId() {
		return deviceId;
	}

	/**
	 * @param myClientAccounts the myClientAccounts to set
	 */
	public void setMyClientAccounts(List<SelectItem> myClientAccounts) {
		this.myClientAccounts = myClientAccounts;
	}

	/**
	 * @return the myClientAccounts
	 */
	public List<SelectItem> getMyClientAccounts() {
		Long userId=userEJB.getSystemMasterById(usrs.getUserId());
		if(myClientAccounts == null){
			myClientAccounts = new ArrayList<SelectItem>();
		} else {
			myClientAccounts.clear();
		}
		myClientAccounts.add(new SelectItem(-1L, "--Seleccione Una--"));
		for (TbAccount su : userEJB.getClientAccount(userId)) {
			myClientAccounts.add(new SelectItem(su.getAccountId(),
					"Cuenta No. " + su.getAccountId()));
		}
		return myClientAccounts;
	}

	public void setAvailableBalance(Long availableBalance) {
		this.availableBalance = availableBalance;
	}

	public Long getAvailableBalance() {
		return availableBalance;
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
		codeTypes = userEJB.getCodeTypes();
		return codeTypes;
	}

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
		}else{
			codeTypesList.clear();
		}
		codeTypesList.add(new SelectItem(-1L,"-- Seleccione Uno --"));
		for (TbCodeType type : getCodeTypes()) {
			codeTypesList.add(new SelectItem(type.getCodeTypeId(), type
					.getCodeTypeAbbreviation()));
		}
		return codeTypesList;
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

	public void setShowModal(boolean showModal) {
		this.showModal = showModal;
	}

	public boolean isShowModal() {
		return showModal;
	}

	public void setShowMessage(boolean showMessage) {
		this.showMessage = showMessage;
	}

	public boolean isShowMessage() {
		return showMessage;
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
	 * @param accountList the accountList to set
	 */
	public void setAccountList(List<TbAccount> accountList) {
		this.accountList = accountList;
	}

	/**
	 * @return the accountList
	 */
	public List<TbAccount> getAccountList() {
		return accountList;
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

	/**
	 * @param list the availableBalanceUser to set
	 */
	public void setAvailableBalanceUser(List<TbAccount> list) {
		this.availableBalanceUser = list;
	}

	/**
	 * @return the availableBalanceUser
	 */
	public List<TbAccount> getAvailableBalanceUser() {
		return availableBalanceUser;
	}

	/**
	 * @param renderAste the renderAste to set
	 */
	public void setRenderAste(boolean renderAste) {
		this.renderAste = renderAste;
	}

	/**
	 * @return the renderAste
	 */
	public boolean isRenderAste() {
		return renderAste;
	}

	public String getIdClientAccountStr() {
		return idClientAccountStr;
	}

	public void setIdClientAccountStr(String idClientAccountStr) {
		this.idClientAccountStr = idClientAccountStr;
	}
	
}