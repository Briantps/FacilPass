package process.affiliation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import com.sun.facelets.FaceletContext;

import sessionVar.SessionUtil;
import util.DeviceState;
import util.DevicesAvailable;
import util.InfoClient;
import validator.Validation;

import ejb.EnrollDevice;
import ejb.SystemParameters;
import ejb.User;
import jpa.TbAccount;
import jpa.TbCodeType;
import jpa.TbCourier;
import jpa.TbDevice;
import jpa.TbRollo;
import jpa.TbVehicle;

public class AdminTagsBean implements Serializable{

	private static final long serialVersionUID = 1L;

	@EJB(mappedName="ejb/User")
	private User userEjb;
	
	@EJB(mappedName="ejb/EnrollDevice")
	private EnrollDevice enrollDevice;
	
	@EJB(mappedName="ejb/SystemParameters")
	private SystemParameters systemParameter;
	
	private TbDevice device;
	
	private Long typeId;
	
	private String codClient;
	
	private String nomClient;
	
	private String apeClient;
	
	private String user;
	
	private String accountClient="";
	
	private String plateClient;
	
	private String aditional1;
	
	private String aditional2;
	
	private String aditional3;
	
	private List<SelectItem> typeList;
	
	private InfoClient client;
	
	private Long accountId;
	
	private Long actionId;
	
	private Long courierId;
	
	private Long rolloId;
	
	private String message;
	
	private Long cuenta;
	
	private Long vehicleId;
	
	private String plateFilter="";
	
	private String corfirmMessage;
	
	private String observation;
	
	private String facialOld;
	
	private String cobro;
	
	private Long valorCobro;
	
	private int scrollerPage=1;
	
	private int tamanio;
	
	private String mileageFilter; 
	
	private boolean showOneAccount;
	
	private boolean showModalError;
	
	private boolean activeRollo;
	
	private boolean activeCouriers;
	
	private boolean showGrillaEnrolar;
	
	private boolean showFormChange;
	
	private boolean showFormPay;
	
	private boolean activeAllTags=false;
	
	private boolean showConfirmation;
	
	private boolean showConfirmation2;
	
	private boolean showConfirmation3;
	
	private boolean showFormCambioyRep;
	
	private boolean showFormCobro;
	
	private boolean showModal;
	
	private boolean showModalError2;
	
	private boolean showClient=false;
	
	private boolean showActions=false;
	
	private List<DevicesAvailable> devices;
	
	private List<DeviceState> devices2;
	
	private List<SelectItem> actionList;
	
	private List<SelectItem> courierList;
	
	private List<SelectItem> rolloList;
	
	private List<SelectItem> accountList;
	
	private List<DevicesAvailable> listaTemp;
	
	private List<SelectItem> vehicleList;
	
	private Boolean activeOne;
	
	private Long plateId;
	
	private Long tagId;
	
	
	
	public AdminTagsBean(){
		activeRollo=true;
		activeCouriers=true;
		showGrillaEnrolar=false;
		setShowModalError(false);
	}

	/**
	 * @param typeId the typeId to set
	 */
	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	/**
	 * @return the typeId
	 */
	public Long getTypeId() {
		return typeId;
	}

	/**
	 * @param codClient the codClient to set
	 */
	public void setCodClient(String codClient) {
		this.codClient = codClient;
	}

	/**
	 * @return the codClient
	 */
	public String getCodClient() {
		return codClient;
	}

	/**
	 * @param nomClient the nomClient to set
	 */
	public void setNomClient(String nomClient) {
		this.nomClient = nomClient;
	}

	/**
	 * @return the nomClient
	 */
	public String getNomClient() {
		return nomClient;
	}

	/**
	 * @param apeClient the apeClient to set
	 */
	public void setApeClient(String apeClient) {
		this.apeClient = apeClient;
	}

	/**
	 * @return the apeClient
	 */
	public String getApeClient() {
		return apeClient;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param accountClient the accountClient to set
	 */
	public void setAccountClient(String accountClient) {
		this.accountClient = accountClient;
	}

	/**
	 * @return the accountClient
	 */
	public String getAccountClient() {
		return accountClient;
	}

	/**
	 * @param plateClient the plateClient to set
	 */
	public void setPlateClient(String plateClient) {
		this.plateClient = plateClient;
	}

	/**
	 * @return the plateClient
	 */
	public String getPlateClient() {
		return plateClient;
	}

	/**
	 * @param aditional1 the aditional1 to set
	 */
	public void setAditional1(String aditional1) {
		this.aditional1 = aditional1;
	}

	/**
	 * @return the aditional1
	 */
	public String getAditional1() {
		return aditional1;
	}

	/**
	 * @param aditional2 the aditional2 to set
	 */
	public void setAditional2(String aditional2) {
		this.aditional2 = aditional2;
	}

	/**
	 * @return the aditional2
	 */
	public String getAditional2() {
		return aditional2;
	}

	/**
	 * @param aditional3 the aditional3 to set
	 */
	public void setAditional3(String aditional3) {
		this.aditional3 = aditional3;
	}

	/**
	 * @return the aditional3
	 */
	public String getAditional3() {
		return aditional3;
	}

	/**
	 * @param typeList the typeList to set
	 */
	public void setTypeList(List<SelectItem> typeList) {
		this.typeList = typeList;
	}

	/**
	 * @return the typeList
	 */
	public List<SelectItem> getTypeList() {
		if(typeList==null){
			typeList = new ArrayList<SelectItem>();
			typeList.add(new SelectItem(-1L,"--Seleccione Uno--"));
			for (TbCodeType type : userEjb.getCodeTypes()) {
				typeList.add(new SelectItem(type.getCodeTypeId(), type.getCodeTypeAbbreviation()));
			}
		}
		return typeList;
	}

	/**
	 * @param actionId the actionId to set
	 */
	public void setActionId(Long actionId) {
		this.actionId = actionId;
	}

	/**
	 * @return the actionId
	 */
	public Long getActionId() {
		return actionId;
	}

	/**
	 * @param courierId the courierId to set
	 */
	public void setCourierId(Long courierId) {
		this.courierId = courierId;
	}

	/**
	 * @return the courierId
	 */
	public Long getCourierId() {
		return courierId;
	}

	/**
	 * @param rolloId the rolloId to set
	 */
	public void setRolloId(Long rolloId) {
		this.rolloId = rolloId;
	}

	/**
	 * @return the rolloId
	 */
	public Long getRolloId() {
		return rolloId;
	}

	/**
	 * @param actionList the actionList to set
	 */
	public void setActionList(List<SelectItem> actionList) {
		this.actionList = actionList;
	}

	/**
	 * @return the actionList
	 */
	public List<SelectItem> getActionList() {
		actionList= new ArrayList<SelectItem>();
		actionList.add(new SelectItem(-1, "--Seleccione Uno--"));
		actionList.add(new SelectItem(1, "Enrolar"));
		actionList.add(new SelectItem(2, "Cambio"));
		actionList.add(new SelectItem(3, "Reposición"));
		
		return actionList;
	}

	/**
	 * @param courierList the courierList to set
	 */
	public void setCourierList(List<SelectItem> courierList) {
		this.courierList = courierList;
	}

	/**
	 * @return the courierList
	 */
	public List<SelectItem> getCourierList() {
		courierList= new ArrayList<SelectItem>();
		courierList.add(new SelectItem(-1, "--Seleccione Uno--"));
		for(TbCourier tc : enrollDevice.getCouries()){
			courierList.add(new SelectItem(tc.getCourierId(), tc.getCourierName()));
		}
		return courierList;
	}

	/**
	 * @param rolloList the rolloList to set
	 */
	public void setRolloList(List<SelectItem> rolloList) {
		this.rolloList = rolloList;
	}

	/**
	 * @return the rolloList
	 */
	public List<SelectItem> getRolloList() {
		rolloList= new ArrayList<SelectItem>();
		rolloList.add(new SelectItem(-1, "--Seleccione Uno--"));
		if(courierId!=null && courierId>-1){
			for(TbRollo tr: enrollDevice.getRollosByCourier(courierId)){
				rolloList.add(new SelectItem(tr.getRollId(), tr.getRollName()));	
			}
		}
		return rolloList;
	}

	public String search(){
		this.setShowActions(false);
		this.setShowClient(false);
		this.setShowGrillaEnrolar(false);
		this.setShowFormCambioyRep(false);
		this.setShowFormCobro(false);
		this.setShowModalError(false);
		this.setActiveCouriers(true);
		this.setActiveRollo(true);
		this.setActionId(-1L);
		this.setCobro("");
		this.setObservation("");
		this.setVehicleId(-1L);
		this.setRolloId(-1L);
		this.setCourierId(-1L);
		this.setActiveCouriers(true);
		this.setActiveRollo(true);
		
		if(accountList!=null){
			accountList.clear();	
			accountList.add(new SelectItem(0, "--Seleccione Cuenta--"));
		}
		else{
			accountList= new ArrayList<SelectItem>();	
			accountList.add(new SelectItem(0, "--Seleccione Cuenta--"));
		}
		this.setShowOneAccount(false);
		System.out.println("typeId: " + typeId);
		System.out.println("codClient: " + codClient);
		System.out.println("nomClient: " + nomClient);
		System.out.println("apeClient: " + apeClient);
		System.out.println("user: " + user);
		System.out.println("accountClient: " + accountClient);
		System.out.println("plateClient: " + plateClient);
		System.out.println("aditional1: " + aditional1);
		System.out.println("aditional2: " + aditional2);
		System.out.println("aditional3: " + aditional3);
		
		if(codClient.equals("") && nomClient.equals("") && apeClient.equals("") && user.equals("") && (accountClient==null || accountClient.equals(""))
				&& plateClient.equals("") && aditional1.equals("") && aditional2.equals("") && aditional3.equals("")){
			
			this.setMessage("Digite un filtro de búsqueda");
			this.setShowModalError(true);
			
		}else if (!codClient.equals("") && typeId==-1L){
			this.setMessage("Debe seleccionar un tipo de Identificación.");
			this.setShowModalError(true);			
		}else if(!codClient.equals("") && !Validation.isNumeric(codClient)){
			this.setMessage("El campo No. Identificación solo debe contener números.");
			this.setShowModalError(true);
		}
		else if (!nomClient.equals("") && !Validation.isAlphaNumCompany2(nomClient)){
			this.setMessage("El campo Nombre tiene caracteres inválidos.");
			this.setShowModalError(true);
		}
		else if(!apeClient.equals("") && !Validation.apellidoCliente(apeClient)){
			this.setMessage("El campo Apellidos tiene caracteres inválidos.");
			this.setShowModalError(true);
		}
		else if(user!="" && !Validation.isEmail2(user)){
				this.setMessage("El campo Usuario tiene caracteres inválidos.");
				this.setShowModalError(true);
		}
		else if(accountClient!=null && !accountClient.equals("") && !Validation.isNumeric(String.valueOf(accountClient))){
			this.setMessage("El campo Cuenta FacilPass solo debe contener números.");
			this.setShowModalError(true);
		}
		else if(!plateClient.equals("") && !Validation.isAlphaNum(plateClient)){
			this.setMessage("El campo Placa solo debe contener números y letras.");
			this.setShowModalError(true);
		}
		else if(aditional1!="" && !Validation.aditional(aditional1)){
			this.setMessage("El campo Adicional 1 tiene caracteres inválidos.");
			this.setShowModal(true);
		}
		else if(aditional2!="" && !Validation.aditional(aditional2)){
			this.setMessage("El campo Adicional 2 tiene caracteres inválidos.");
			this.setShowModal(true);
		}
		else if(aditional3!="" && !Validation.aditional(aditional3)){
			this.setMessage("El campo Adicional 3 tiene caracteres inválidos.");
			this.setShowModal(true);
		}
		else{
			int flag=0;
			Long cuenta=null;
			try{
				if(accountClient!=null && !accountClient.trim().equals("") ){
					cuenta= Long.parseLong(accountClient);
					flag=0;
				}
				else if(accountClient==null || accountClient.trim().equals("") ){
					flag=0;
				}
			}
			catch(Exception ex){
				ex.printStackTrace();
				flag=1;
			}
			
			if(flag==0){
				System.out.println("Entre a validar cliente");
				Long cont= enrollDevice.validateClient(typeId, codClient, nomClient, apeClient, user, cuenta, plateClient, 
						aditional1, aditional2, aditional3);

				if(cont==0){
					this.setShowClient(false);
					this.setMessage("No se encontraron resultados para la búsqueda solicitada.");
					this.setShowModalError(true);
					
				}
				else if(cont==-1){
					this.setShowClient(false);
					this.setMessage("Error en Transacción.");
					this.setShowModalError(true);
					
				}
				else if (cont>1){
					this.setShowClient(false);
					this.setMessage("El filtro ingresado generó más de un Cliente. Debe ingresar más filtros para continuar.");
					this.setShowModalError(true);
					
				}
				else{
					this.setShowModalError(false);
					this.setClient(enrollDevice.searchClient(typeId, codClient, nomClient,apeClient, user, cuenta,
		                    plateClient,aditional1,aditional2,aditional3));
					this.setShowClient(true);
					System.out.println("setShowClient: "+showClient);
					List<TbAccount> lista= new ArrayList<TbAccount>();
					if(client!=null){
						System.out.println("Entre a obtener cuentas del cliente "+client.getIdClient());						
						lista= userEjb.getClientAccount(client.getIdClient());
					}
					System.out.println("cuenta1: "+cuenta);
					if(lista!=null){
						System.out.println("Tamaño lista: "+lista.size());
						if(lista.size()==0){
							this.setAccountId(0L);
							this.setShowOneAccount(true);
						}
						if(lista.size()==1){
							this.setAccountId(lista.get(0).getAccountId());
							this.setShowOneAccount(true);
							this.setShowActions(true);
						}
						else if(client!=null && accountClient!=null && !accountClient.equals("") && cuenta!=null){
							this.setAccountId(cuenta);
							this.setShowOneAccount(true);
							this.setShowActions(true);
						}
						else if(lista.size()>1){
							this.setAccountId(0L);
							for(TbAccount t: lista){
								accountList.add(new SelectItem(t.getAccountId(), t.getAccountId().toString()));
								this.setShowOneAccount(false);	
							}
						}
					}
					else{
						this.setShowOneAccount(true);
						this.setAccountId(0L);
					}
					System.out.println("cuenta2: "+cuenta);
				}
			}
			else{
				this.setMessage("Error al consultar la Cuenta FacilPass.");
				this.setShowModalError(true);
			}
		}

		return "";
	}
	
	public String reset(){
		this.setShowActions(false);
		this.setTypeId(-1L);
		this.setCodClient("");
		this.setNomClient("");
		this.setApeClient("");
		this.setUser("");
		this.setAccountClient("");
		this.setPlateClient("");
		this.setAditional1("");
		this.setAditional2("");
		this.setAditional3("");
		this.setShowClient(false);
		//this.setClient(null);
		this.setAccountList(null);
		this.setShowGrillaEnrolar(false);
		this.setShowFormChange(false);
		this.setShowFormPay(false);
		this.setShowFormCambioyRep(false);
		this.setShowFormCobro(false);
		this.setShowModalError(false);
		this.setShowModalError2(false);
		this.setAccountId(0L);
		this.setActionId(-1L);
		this.setCourierId(-1L);
		this.setRolloId(-1L);
		this.setCobro("");
		this.setObservation("");
		this.setVehicleId(-1L);
		this.setActiveCouriers(true);
		this.setActiveRollo(true);
		
		if(vehicleList!=null){
			vehicleList.clear();	
			vehicleList.add(new SelectItem(-1, "--Seleccione Placa--"));
		}
		else{
			vehicleList= new ArrayList<SelectItem>();	
			vehicleList.add(new SelectItem(-1, "--Seleccione Placa--"));
		}
	
		
	    return "";	
	}
	
	public void hideModal(){
		this.setShowModalError(false);
		this.setShowModal(false);
		this.setShowModalError2(false);
		this.setShowConfirmation(false);
		this.setShowConfirmation2(false);
		this.setShowConfirmation3(false);
		this.setMessage("");
		this.setCorfirmMessage("");
	}
	
	public void hideModal1(){
		this.setShowModal(false);
		this.setShowModalError(false);
		this.setShowModalError2(false);
		this.setShowConfirmation(false);
		this.setShowConfirmation2(false);
		this.setShowConfirmation3(false);
		this.setMessage("");
		this.setCorfirmMessage("");
		this.setActionId(-1L);
		this.setCourierId(-1L);
		this.setRolloId(-1L);
		this.setActiveCouriers(true);
		this.setActiveRollo(true);
		this.setObservation("");
		this.setVehicleId(-1L);
		this.setValorCobro(0L);
		this.setCobro("");
		this.setShowGrillaEnrolar(false);
		this.setScrollerPage(1);
		this.setDevices(null);
		this.setDevices2(null);
		this.setListaTemp(null);
		this.setActiveAllTags(false);
	}
	
	public void hideModal2(){
		this.setShowModalError2(false);
		this.setShowModal(false);
		this.setShowModalError(false);
		this.setMessage("");
		this.setCourierId(-1L);
		this.setRolloId(-1L);
		this.setActiveCouriers(true);
		this.setActiveRollo(true);
		this.setObservation("");
		this.setVehicleId(-1L);
		this.setActionId(-1L);
		this.setValorCobro(0L);
		this.setCobro("");
		this.setShowGrillaEnrolar(false);
		this.setDevices(null);
		this.setDevices2(null);
		this.setListaTemp(null);
		try{
			if(vehicleList!=null){
				vehicleList.clear();	
				vehicleList.add(new SelectItem(-1, "--Seleccione Placa--"));
			}
			else{
				vehicleList= new ArrayList<SelectItem>();	
				vehicleList.add(new SelectItem(-1, "--Seleccione Placa--"));
			}
		}catch(java.util.ConcurrentModificationException ex){
			ex.printStackTrace();
		}
		
	}
	
	public void hideModalCuenta(){
		System.out.println("accountId_ "+accountId);
		if(accountId>0L){
			this.setShowActions(true);
		}else{
			this.setShowActions(false);
		}
		this.setShowFormCobro(false);
		this.setShowFormCambioyRep(false);
		this.setShowModalError2(false);
		this.setShowModal(false);
		this.setShowModalError(false);
		this.setMessage("");
		this.setCourierId(-1L);
		this.setRolloId(-1L);
		this.setActiveCouriers(true);
		this.setActiveRollo(true);
		this.setObservation("");
		this.setVehicleId(-1L);
		this.setActionId(-1L);
		this.setValorCobro(0L);
		this.setCobro("");
		this.setShowGrillaEnrolar(false);
		this.setDevices(null);
		this.setDevices2(null);
		this.setListaTemp(null);
		try{
			if(vehicleList!=null){
				vehicleList.clear();	
				vehicleList.add(new SelectItem(-1, "--Seleccione Placa--"));
			}
			else{
				vehicleList= new ArrayList<SelectItem>();	
				vehicleList.add(new SelectItem(-1, "--Seleccione Placa--"));
			}
		}catch(java.util.ConcurrentModificationException ex){
			ex.printStackTrace();
		}
		
	}
	
	public void cancelar(){
		this.setCorfirmMessage("");
		this.setShowConfirmation(false);
		this.setShowConfirmation2(false);
		this.setShowConfirmation3(false);
		this.setMessage("La acción ha sido cancelada.");
		this.setShowModalError(true);
	}

	/**
	 * @param client the client to set
	 */
	public void setClient(InfoClient client) {
		this.client = client;
	}

	/**
	 * @return the client
	 */
	public InfoClient getClient() {
		return client;
	}

	/**
	 * @param accountList the accountList to set
	 */
	public void setAccountList(List<SelectItem> accountList) {
		this.accountList = accountList;
	}

	/**
	 * @return the accountList
	 */
	public List<SelectItem> getAccountList() {
		return accountList;
	}

	/**
	 * @param showOneAccount the showOneAccount to set
	 */
	public void setShowOneAccount(boolean showOneAccount) {
		this.showOneAccount = showOneAccount;
	}

	/**
	 * @return the showOneAccount
	 */
	public boolean isShowOneAccount() {
		return showOneAccount;
	}

	/**
	 * @param accountId the accountId to set
	 */
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	/**
	 * @return the accountId
	 */
	public Long getAccountId() {
		return accountId;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param showModalError the showModalError to set
	 */
	public void setShowModalError(boolean showModalError) {
		this.showModalError = showModalError;
	}

	/**
	 * @return the showModalError
	 */
	public boolean isShowModalError() {
		return showModalError;
	}

	/**
	 * @param cuenta the cuenta to set
	 */
	public void setCuenta(Long cuenta) {
		this.cuenta = cuenta;
	}

	/**
	 * @return the cuenta
	 */
	public Long getCuenta() {
		return cuenta;
	}
	

	/**
	 * @param activeRollo the activeRollo to set
	 */
	public void setActiveRollo(boolean activeRollo) {
		this.activeRollo = activeRollo;
	}

	/**
	 * @return the activeRollo
	 */
	public boolean isActiveRollo() {
		return activeRollo;
	}
	
	
	/**
	 * @param activeCouriers the activeCouriers to set
	 */
	public void setActiveCouriers(boolean activeCouriers) {
		this.activeCouriers = activeCouriers;
	}

	/**
	 * @return the activeCouriers
	 */
	public boolean isActiveCouriers() {
		return activeCouriers;
	}

	/**
	 * @param showGrillaEnrolar the showGrillaEnrolar to set
	 */
	public void setShowGrillaEnrolar(boolean showGrillaEnrolar) {
		this.showGrillaEnrolar = showGrillaEnrolar;
	}

	/**
	 * @return the showGrillaEnrolar
	 */
	public boolean isShowGrillaEnrolar() {
		return showGrillaEnrolar;
	}

	/**
	 * @param showFormChange the showFormChange to set
	 */
	public void setShowFormChange(boolean showFormChange) {
		this.showFormChange = showFormChange;
	}

	/**
	 * @return the showFormChange
	 */
	public boolean isShowFormChange() {
		return showFormChange;
	}

	/**
	 * @param showFormPay the showFormPay to set
	 */
	public void setShowFormPay(boolean showFormPay) {
		this.showFormPay = showFormPay;
	}

	/**
	 * @return the showFormPay
	 */
	public boolean isShowFormPay() {
		return showFormPay;
	}

	/**
	 * @param devices the devices to set
	 */
	public void setDevices(List<DevicesAvailable> devices) {
		this.devices = devices;
	}

	/**
	 * @return the devices
	 */
	public List<DevicesAvailable> getDevices() {
		return devices;
	}

	public void setDevices2(List<DeviceState> devices2) {
		this.devices2 = devices2;
	}

	public List<DeviceState> getDevices2() {
		return devices2;
	}

	/**
	 * @param activeAllTags the activeAllTags to set
	 */
	public void setActiveAllTags(boolean activeAllTags) {
		this.activeAllTags = activeAllTags;
	}

	/**
	 * @return the activeAllTags
	 */
	public boolean isActiveAllTags() {
		return activeAllTags;
	}

	/**
	 * @param vehicleId the vehicleId to set
	 */
	public void setVehicleId(Long vehicleId) {
		this.vehicleId = vehicleId;
	}

	/**
	 * @return the vehicleId
	 */
	public Long getVehicleId() {
		return vehicleId;
	}

	/**
	 * @param plateFilter the plateFilter to set
	 */
	public void setPlateFilter(String plateFilter) {
		this.plateFilter = plateFilter;
	}

	/**
	 * @return the plateFilter
	 */
	public String getPlateFilter() {
		return plateFilter;
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
	 * @param listaTemp the listaTemp to set
	 */
	public void setListaTemp(List<DevicesAvailable> listaTemp) {
		this.listaTemp = listaTemp;
	}

	/**
	 * @return the listaTemp
	 */
	public List<DevicesAvailable> getListaTemp() {
		return listaTemp;
	}

	/**
	 * @param vehicleList the vehicleList to set
	 */
	public void setVehicleList(List<SelectItem> vehicleList) {
		this.vehicleList = vehicleList;
	}

	/**
	 * @return the vehicleList
	 */
	public List<SelectItem> getVehicleList() {
		return vehicleList;
	}

	/**
	 * @param showFormCambioyRep the showFormCambioyRep to set
	 */
	public void setShowFormCambioyRep(boolean showFormCambioyRep) {
		this.showFormCambioyRep = showFormCambioyRep;
	}

	/**
	 * @return the showFormCambioyRep
	 */
	public boolean isShowFormCambioyRep() {
		return showFormCambioyRep;
	}

	/**
	 * @param observation the observation to set
	 */
	public void setObservation(String observation) {
		this.observation = observation;
	}

	/**
	 * @return the observation
	 */
	public String getObservation() {
		return observation;
	}

	/**
	 * @param device the device to set
	 */
	public void setDevice(TbDevice device) {
		this.device = device;
	}

	/**
	 * @return the device
	 */
	public TbDevice getDevice() {
		return device;
	}


	/**
	 * @param showConfirmation2 the showConfirmation2 to set
	 */
	public void setShowConfirmation2(boolean showConfirmation2) {
		this.showConfirmation2 = showConfirmation2;
	}

	/**
	 * @return the showConfirmation2
	 */
	public boolean isShowConfirmation2() {
		return showConfirmation2;
	}

	/**
	 * @param facialOld the facialOld to set
	 */
	public void setFacialOld(String facialOld) {
		this.facialOld = facialOld;
	}

	/**
	 * @return the facialOld
	 */
	public String getFacialOld() {
		return facialOld;
	}

	/**
	 * @param cobro the cobro to set
	 */
	public void setCobro(String cobro) {
		this.cobro = cobro;
	}

	/**
	 * @return the cobro
	 */
	public String getCobro() {
		return cobro;
	}

	/**
	 * @param valorCobro the valorCobro to set
	 */
	public void setValorCobro(Long valorCobro) {
		this.valorCobro = valorCobro;
	}

	/**
	 * @return the valorCobro
	 */
	public Long getValorCobro() {
		return valorCobro;
	}

	/**
	 * @param showFormCobro the showFormCobro to set
	 */
	public void setShowFormCobro(boolean showFormCobro) {
		this.showFormCobro = showFormCobro;
	}

	/**
	 * @return the showFormCobro
	 */
	public boolean isShowFormCobro() {
		return showFormCobro;
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
	 * @param scrollerPage the scrollerPage to set
	 */
	public void setScrollerPage(int scrollerPage) {
		this.scrollerPage = scrollerPage;
	}

	/**
	 * @return the scrollerPage
	 */
	public int getScrollerPage() {
		return scrollerPage;
	}

	/**
	 * @param showModalError2 the showModalError2 to set
	 */
	public void setShowModalError2(boolean showModalError2) {
		this.showModalError2 = showModalError2;
	}

	/**
	 * @return the showModalError2
	 */
	public boolean isShowModalError2() {
		return showModalError2;
	}


	public void setPlateId(Long plateId) {
		this.plateId = plateId;
	}

	public Long getPlateId() {
		return plateId;
	}

	public void setTagId(Long tagId) {
		this.tagId = tagId;
	}

	public Long getTagId() {
		return tagId;
	}

	public void setShowConfirmation3(boolean showConfirmation3) {
		this.showConfirmation3 = showConfirmation3;
	}

	public boolean isShowConfirmation3() {
		return showConfirmation3;
	}

	public void setShowClient(boolean showClient) {
		this.showClient = showClient;
	}

	public boolean isShowClient() {
		return showClient;
	}



	public void activeCourier(){
		System.out.println("accountId: " + accountId);
		if(vehicleList!=null){
			vehicleList.clear();	
			vehicleList.add(new SelectItem(-1, "--Seleccione Placa--"));
		}
		else{
			vehicleList= new ArrayList<SelectItem>();	
			vehicleList.add(new SelectItem(-1, "--Seleccione Placa--"));
		}
		
		this.setShowGrillaEnrolar(false);
		this.setShowFormCambioyRep(false);
		this.setShowFormCobro(false);
		this.setShowModalError(false);
		this.setShowModalError2(false);
		System.out.println("actionId "+actionId);
		
		String value = FacesContext.getCurrentInstance().
		getExternalContext().getRequestParameterMap().get("form:dTable:0:accounts");
		
		System.out.println("value: "+value);
		
		if(accountId==0L && value!=null && !value.equals("")){
			accountId = Long.parseLong(value);
		}
		
		
		if(actionId!=null && actionId>-1){
				this.setCourierId(-1L);
				this.setRolloId(-1L);
				this.setActiveCouriers(false);
				this.setActiveRollo(false);
				if(actionId==2L || actionId==3L){
					System.out.println("Entre al actionid =2");
					System.out.println("accountId: " + accountId);
					if(accountId!=null  && accountId>0){
						this.setShowFormCambioyRep(true);
					    List<TbVehicle> lis=enrollDevice.getVehiclesActiveByClient(client.getIdClient(), accountId);
						for(TbVehicle tv: lis){
						   vehicleList.add(new SelectItem(tv.getVehicleId(), tv.getVehiclePlate()));
						}
						if(actionId==3L){
							this.setShowFormCobro(true);
						}
					}
					else{
						this.setShowFormCobro(false);
						this.setShowFormCambioyRep(false);
						this.setMessage("Seleccione la Cuenta FacilPass");
						this.setShowModalError2(true);
					}
				}
				else if(actionId==1L){
					if(accountId!=null){
						if(accountId<=0){
							this.setShowFormCobro(false);
							this.setShowFormCambioyRep(false);
							this.setMessage("Seleccione la Cuenta FacilPass");
							this.setShowModalError2(true);
						}
					}
					else{
						this.setShowFormCobro(false);
						this.setShowFormCambioyRep(false);
						this.setMessage("Seleccione la Cuenta FacilPass");
						this.setShowModalError2(true);
					}
				}
		}
		else{
			this.setRolloId(-1L);
			this.setCourierId(-1L);
			this.setActiveCouriers(true);
			this.setActiveRollo(true);
		}
	}

	
	public void activeRoll(){
		this.setShowGrillaEnrolar(false);
		this.setShowModalError(false);
		this.setShowModalError2(false);
		if(courierId!=null && courierId!=-1){
			this.setRolloId(-1L);
			this.setActiveRollo(false);
		}
		else{
			this.setActiveRollo(true);
		}
	}
	
	
	public void setActiveOne(Boolean activeOne) {
		this.activeOne = activeOne;
	}

	public Boolean getActiveOne() {
		return activeOne;
	}

	public void showForm(){
		this.setShowGrillaEnrolar(false);
		this.setShowFormChange(false);
		this.setShowFormPay(false);
		this.setShowModalError(false);
		this.setShowModalError2(false);
		this.setActiveAllTags(false);
		System.out.println("cliente: "+ client);
		if(client!=null){
			System.out.println("clientId en activeRoll: "+client.getIdClient());
			System.out.println("accountId en activeRoll: "+accountId);
			System.out.println("courierId en activeRoll: "+courierId);
			System.out.println("rolloId en activeRoll: "+rolloId);
			
			if(rolloId !=null && rolloId!=-1){
				if(actionId!=null && actionId==1){
					this.setDevices(enrollDevice.getVehiclesAvaliableByClient(client.getIdClient()));
					if(devices!=null){
						System.out.println("devices no es null");
						if(devices.size()>0){
							System.out.println("devices mayor a cero, se precargan los tags disponibles");
							this.setDevices2(enrollDevice.getDevices(rolloId));
							this.setShowGrillaEnrolar(true);
						}
						else{
							System.out.println("devices igual a cero");
							this.setMessage("El cliente no tiene placas asociadas pendientes por enrolar.");
							this.setShowModalError(true);
						}
					}
					else{
						System.out.println("devices es null");
						this.setMessage("El cliente no tiene placas asociadas pendientes por enrolar.");
						this.setShowModalError(true);
					}
					
				}
				else if(actionId==2){
					this.setShowFormChange(true);
				}
				else{
					this.setShowFormPay(true);
				}
			}
			else{
				this.setMessage("El campo Rollo es requerido.");
				this.setShowModalError(true);
			}
		}
		
		else{
			System.out.println("client es null en activeRoll");
			this.setMessage("El cliente es requerido.");
			this.setShowModalError(true);
		}
		
	}
	

	public void selectOne(){
		System.out.println("activeOne: " + activeOne);
		System.out.println("plateId: " + plateId);
		System.out.println("scrollerPage: " +scrollerPage);
		if(activeOne){
			//se recorren las placas del cliente y se busca la placa seleccionada y se le asigna el tag correspondiente
			for(DevicesAvailable da : devices){
				if(da.getVehicleId().equals(plateId)){
					System.out.println("encontro placa" + da.getVehiclePlate());
					//se obtiene el primer tag disponible y se le cambia el estado a 1 para que no pueda ser tomado
					DeviceState dev=obtenerTag();
					System.out.println("facial: " + (dev!=null?dev.getDeviceFacialId():""));
					da.setDeviceId(dev!=null?dev.getDeviceId():0L);
					da.setDeviceFacialId(dev!=null?dev.getDeviceFacialId():"");
				}
			}
		}
		else{
			System.out.println("tagId :" +tagId);
			
			//se liberan los demas tags seleccionados previamente
			for(DeviceState de1 : devices2){
		        if(de1.getState().equals("1")){
		        	de1.setState("0");
		        }
			}
			
			//se busca la placa seleccionada y se le setea a null el dispositivo
			for(DevicesAvailable da : devices){
				if(da.isActive()){
					System.out.println("activo");
					da.setDeviceId(null);
					da.setDeviceFacialId("-");
				}
				if(da.getVehicleId().equals(plateId)){
					System.out.println("encontro placa" + da.getVehiclePlate());
					da.setDeviceId(null);
					da.setDeviceFacialId("-");
					da.setActive(false);
				}
			}
		
			DeviceState ds1;
			for(DevicesAvailable da1 : devices){
				if(da1.isActive()){
					System.out.println("Reorganizando tags a las placas que fueron seleccionadas");
					ds1 = obtenerTag(); 
  					da1.setDeviceId(ds1!=null?ds1.getDeviceId():0L);
  		  			da1.setDeviceFacialId(ds1!=null?ds1.getDeviceFacialId():"");
				}
			}
		}
		
	}
	
	public DeviceState obtenerTag(){
		DeviceState d = null;
		for(DeviceState ds: devices2){
			if(ds.getState().equals("0")){
				System.out.println("Entre a cambiar el estado a 1 del tag: " + ds.getDeviceFacialId());
				ds.setState("1");
				d=ds;
                break;
			}
		}
		return d;
	}
	
	public DeviceState obtenerTag2(){
		for(DeviceState ds: devices2){
			if(ds.getState().equals("0")){
				ds.setState("1");
				System.out.println("Entre a cambiar el estado a 1 del tag: " + ds.getDeviceFacialId());
                return ds;
			}
		}
		return null;
	}
	
	public void cambiarEstado(Long tagId){
		for(DeviceState ds: devices2){
			if(ds.getDeviceId()!=null){
				if(ds.getDeviceId().equals(tagId)){
					System.out.println("Entre a cambiar el estado a 0 del tag: " + tagId);
					ds.setState("0");
					break;
				}
			}
		}
	}
	
	public void selectAll(){
	    		 
		System.out.println("activeAllTags: " +activeAllTags);
	    if(activeAllTags==true) {
	    	int cont=0, var=0;
	        System.out.println("Checked");   
	        System.out.println("scrollerPage: " +scrollerPage);
	        if(scrollerPage==1){
	            for(DevicesAvailable dis : devices){
	  			    if(dis.getVehicleId()!=null && (dis.getDeviceId()==null || dis.getDeviceId()==0L)){
	  					if(cont<20){
	  						dis.setActive(true);
	  						DeviceState ds = obtenerTag2(); 
		  					dis.setDeviceId(ds!=null?ds.getDeviceId():0L);
		  		  			dis.setDeviceFacialId(ds!=null?ds.getDeviceFacialId():"");
	  						cont++;
	  						System.out.println("se selecciono registro");
	  					}
	  					
	  				}
	  			}
	        }
	        else{
	        	int cont2=0;
	        	var=(scrollerPage-1)*20;
	        	System.out.println("var: " +var);
	        	for(DevicesAvailable dis : devices){
	  				if(dis.getVehicleId()!=null && (dis.getDeviceId()==null || dis.getDeviceId()==0L)){
	  				    if(cont>=var && cont2<20){
	  					    if(dis!=null){
	  						    dis.setActive(true);
	  							DeviceState ds = obtenerTag2(); 
			  					dis.setDeviceId(ds!=null?ds.getDeviceId():0L);
			  		  			dis.setDeviceFacialId(ds!=null?ds.getDeviceFacialId():"-");
			  		  		    cont2++;
			  		  		    System.out.println("se selecciono registro");
	  						    System.out.println("Facial: " + dis.getDeviceFacialId());
			  				}

	  				     }
	  					 cont++;	
	  				}
	  			}
	  					
	  		}
	        System.out.println("cont: " + cont);
	    } else {
	        System.out.println("Un Checked");
	        for(DevicesAvailable dis : devices){
			  cambiarEstado(dis.getDeviceId());
			  dis.setDeviceId(null);
			  dis.setDeviceFacialId("-");
			  dis.setActive(false);
			}
	    }                
	}
	
	public void disabledSelectAll(){
		try{
			Thread.sleep(250);
			System.out.println("activeAllTags en disabledSelectAll: " +activeAllTags);
			if(activeAllTags){
				this.setActiveAllTags(false);
				System.out.println("Un Checked");
		        for(DevicesAvailable dis : devices){
		        	cambiarEstado(dis.getDeviceId());
		        	dis.setDeviceId(null);
		        	dis.setDeviceFacialId("-");
		        	dis.setActive(false);
				}
			}
		}catch(ConcurrentModificationException ex){
			ex.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void enrolarConfirm(){
		String cant= systemParameter.getParameterValueById(41L);
		int cant2;
		if(cant!=null){
			if(cant.equals("")){
				if(Validation.isNumeric(cant)){
					cant2= Integer.parseInt(cant);
				}
				else{
					cant2=50;
				}
			}
			else{
				cant2=50;
			}
		}
		else{
			cant2=50;
		}
		
		this.setShowConfirmation(false);
		this.setShowModalError(false);
		this.setShowModalError2(false);
		System.out.print(" activeAllTags: " +activeAllTags);
		int con=0;
		if(accountId<=0){
			this.setMessage("La cuenta FacilPass es requerida.");
			this.setShowModalError(true);
		}
		else{
			for(DevicesAvailable dis : devices){
				if(dis.getVehicleId()!=null && dis.getDeviceId()!=null && dis.getDeviceId()!=0L && dis.isActive()){
				    con++;	
				}
			}
			
			if(con==0){
				this.setMessage("Debe seleccionar un vehículo y un tag para ser enrolado.");
				this.setShowModalError(true);
			}
			else if(con>cant2){
				this.setMessage("Solo puede enrolar un máximo de " + cant2+" tags a la vez." );
				this.setShowModalError(true);
			}
			else{
				this.setCorfirmMessage("Por favor confirme las asignaciones de PLACA - TAG, si alguna no es correcta por favor de clic" +
						" en Cancelar ya que es necesario realizar la reasignación de los TAG nuevamente.");
		        listaTemp= new ArrayList<DevicesAvailable>();
				if(activeAllTags==true){
				    for(DevicesAvailable da : devices){
				    	if(da.getVehicleId()!=null && da.getDeviceId()!=null && da.getDeviceId()!=0L  && da.isActive()){
				    		listaTemp.add(da);
				    	}
				    }
				}
				else{
				    for(DevicesAvailable da : devices){
					    if(da.getVehicleId()!=null && da.getDeviceId()!=null && da.getDeviceId()!=0L && da.isActive()){
					        listaTemp.add(da);
					    }
					}
				}
				
				this.setShowConfirmation(true);
			}
		}
	
	}
	
	public void ocult(){
		this.setShowModalError(false);
		this.setShowModalError2(false);
		this.setShowGrillaEnrolar(false);
		this.setShowFormChange(false);
		this.setShowFormPay(false);
		this.setAccountId(0L);
		this.setCourierId(-1L);
		this.setRolloId(-1L);
	}
	
	public void ocult1(){
		try{
			this.setShowClient(false);
			//this.setClient(null);
			this.setShowGrillaEnrolar(false);
			this.setShowFormChange(false);
			this.setShowFormPay(false);
			this.setShowModalError(false);
			this.setShowModalError2(false);
			this.setAccountId(0L);
			
			this.setActionId(-1L);
			this.setCourierId(-1L);
			this.setRolloId(-1L);
			this.setActiveCouriers(true);
			this.setActiveRollo(true);
			
			this.setObservation("");
			this.setCobro("");

			if(vehicleList!=null){
				vehicleList.clear();	
				vehicleList.add(new SelectItem(-1, "--Seleccione Placa--"));
			}
			else{
				vehicleList= new ArrayList<SelectItem>();	
				vehicleList.add(new SelectItem(-1, "--Seleccione Placa--"));
			}
		}catch(java.util.ConcurrentModificationException ex){
			System.out.println("Entre al error ConcurrentModificationException");
			ex.printStackTrace();
		}
		
	}
	
	public void enrolar(){
		this.setShowConfirmation(false);
		this.setShowModal(false);
		this.setShowModalError(false);
		System.out.println("listaTemp: "+ listaTemp);
		System.out.println("Entre a enrolar");
		
		if(client!=null){
			if(enrollDevice.enrolar(client.getIdClient(),accountId, listaTemp, SessionUtil.ip(), SessionUtil.sessionUser().getUserId())){
				this.setMessage("Transacción Exitosa.");
				this.setShowModal(true);
				this.setDevices(enrollDevice.getVehiclesAvaliableByClient(client.getIdClient()));
				this.setShowGrillaEnrolar(true);
			}
			else{
				this.setMessage("Error en Tansacción.");
				this.setShowModalError(true);
			}
		}
		else{
			this.setMessage("El cliente es requerido.");
			this.setShowModalError(true);
		}
	
	}
	
	public void saveChange(){
		this.setShowModalError(false);
		System.out.println("Entre al metodo saveChange");
		String plac="";
		if(client!=null){
		       if(courierId!=null && courierId!=-1){
				    if(rolloId!=null && rolloId!=-1){
					    if(vehicleId!=null && vehicleId!=-1){
						    this.setFacialOld(enrollDevice.getFacialByVehicle(vehicleId));
							plac= enrollDevice.getVehicleById(vehicleId);
							this.setDevice(enrollDevice.getDeviceAvailableByRollo(rolloId));
							if(observation!=null){
							    if(!observation.trim().equals("")){
								    if(device!=null){
									    if(actionId==2){
		                                    this.setCorfirmMessage("¿Está seguro de realizar el siguiente cambio de dispositivo de la placa " +
											     		""+plac+ " para el cliente " + (client!=null?client.getNom():"-") + " " + (client!=null?client.getApe():"-")+"? TAG ANTERIOR "+
											     		facialOld+ " y TAG NUEVO "+device.getDeviceFacialId());
											this.setShowConfirmation2(true);
										}
										else if(actionId==3){
											 if((cobro==null) || (cobro.equals(""))){
												  this.setMessage("El campo Cobro es requerido.");
												  this.setShowModalError(true);
											  } else if(!Validation.isNumericPuntoYComaNoConsecutive(cobro)){
												  this.setMessage("El campo Cobro contiene caracteres inválidos, por favor verifique.");
												  this.setShowModalError(true);
											  } else {
												    valorCobro = Long.parseLong(cobro.replace(".", "").replace(",", ""));
											        this.setCorfirmMessage("¿Está seguro de realizar la siguiente reposición de dispositivo de la placa " +
											     		""+plac+ " para el cliente " + client.getNom()+ " " + client.getApe()+" con costo de $"+valorCobro+"? TAG ANTERIOR "+
											     		facialOld+ " y TAG NUEVO "+device.getDeviceFacialId());
											        this.setShowConfirmation3(true);
											  }
										}
										else{
											this.setMessage("Acción no válida.");
											this.setShowModalError(true);
										}
										
									}
									else{
										this.setMessage("No se encontró ningún TAG disponible.");
										this.setShowModalError(true);
									}
								}
								else{
									this.setMessage("El campo Observación es requerido.");
									this.setShowModalError(true);
								}
							}
							else{
								this.setMessage("El campo Observación es requerido.");
								this.setShowModalError(true);
							}
					    }
						
						else{
						    this.setMessage("El campo Placa es requerido.");
							this.setShowModalError(true);
						}
					}
					else{
						this.setMessage("El campo Rollo es requerido.");
						this.setShowModalError(true);
					}
				}
				else{
					this.setMessage("El campo Courier es requerido.");
					this.setShowModalError(true);
				}	
		}
		else{
			this.setMessage("El cliente es requerido.");
			this.setShowModalError(true);
		}
 	
	}
	
	
	public void cambiar(){
		this.setShowConfirmation2(false);
		this.setShowConfirmation3(false);
		this.setShowModalError(false);
		this.setShowModal(false);
		
		if(client!=null && device!=null){
			System.out.println("clienteId en metodo cambio: " + client.getIdClient());
			System.out.println("accountId en metodo cambio: " + accountId);
			System.out.println("deviceId en metodo cambio: " + device!=null?device.getDeviceId():0L);
			System.out.println("vehicleId en metodo cambio: " + vehicleId);
			System.out.println("observation en metodo cambio: " + observation);
			System.out.println("facialOld en metodo cambio: " + facialOld);
			System.out.println("valorCobro en metodo cambio: " + valorCobro);
			
			if(actionId==2L){
				if(enrollDevice.saveChangeDevice(client.getIdClient(), accountId, (device!=null?device.getDeviceId():0L), (device!=null?device.getDeviceFacialId():""), facialOld,  
						                            vehicleId, observation, SessionUtil.sessionUser().getUserId(), SessionUtil.ip())){
					this.setMessage("Transacción de Cambio realizada exitosamente.");
					this.setShowModal(true);
				}
				else{
					this.setMessage("Error en Transacción.");
					this.setShowModalError(true);
				}
			}
			else if(actionId==3L){
				if(enrollDevice.saveReplaceDevice(client.getIdClient(), accountId, (device!=null?device.getDeviceId():0L), (device!=null?device.getDeviceFacialId():""), facialOld,
						vehicleId, observation, SessionUtil.sessionUser().getUserId(), SessionUtil.ip(), valorCobro)){
					this.setMessage("Transacción de Reposición realizada exitosamente.");
					this.setShowModal(true);
				}
				else{
					this.setMessage("Error en Transacción.");
					this.setShowModalError(true);
				}
			}
			else{
				this.setMessage("Acción no válida.");
				this.setShowModalError(true);
			}
		}
		else{
			this.setMessage("Error en Transacción.");
			this.setShowModalError(true);
		}
		
	}

	public void image(){

	}
	
	 public void setMileageFilter(String mileageFilter) {
		this.mileageFilter = mileageFilter;
	}

	public String getMileageFilter() {
		return mileageFilter;
	}

	public boolean filterDevicesAvailable(Object current) {
		DevicesAvailable device = (DevicesAvailable)current;
		   if (plateFilter.length()==0) {
		      return true;
		   }
		   if(plateFilter.length()>=4){
			   if (device.getVehiclePlate().contains(plateFilter)) {
				      return true;
				   } 
			   else{
				   return false;
			   }
		   }

		   else {
			return false; 
		   }
		}


	public void setTamanio(int tamanio) {
		this.tamanio = tamanio;
	}

	public int getTamanio() {
		if(plateFilter!=null && !plateFilter.trim().equals("")){
			tamanio=plateFilter.length();
		}
		else{
			tamanio=0;
		}
		return tamanio;
	}
	
	public boolean filterPlate(Object current) {
		DevicesAvailable currentWonder = (DevicesAvailable) current;
		plateFilter=plateFilter.toUpperCase();
		   if (plateFilter.length()==0) {
		      return true;
		   }
		   if (currentWonder.getVehiclePlate().startsWith(plateFilter.toUpperCase())) {
			   System.out.println("scrollerPage: " + scrollerPage);
		      return true;
		   } 
		   else {
			   System.out.println("scrollerPage: " + scrollerPage);
			return false; 
		   }
		   
		}

	public boolean isShowActions() {
		return showActions;
	}

	public void setShowActions(boolean showActions) {
		this.showActions = showActions;
	}
        
}
