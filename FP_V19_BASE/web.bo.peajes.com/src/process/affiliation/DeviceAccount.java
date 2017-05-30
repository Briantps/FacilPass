/**
 * 
 */
package process.affiliation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import jpa.ReAccountDevice;
import jpa.TbAccount;
import jpa.TbDevice;
import jpa.TbDeviceType;
import jpa.TbSystemUser;
import jpa.TbVehicle;
import sessionVar.SessionUtil;
import ejb.Device;
import ejb.User;
import ejb.Vehicle;

/**
 * Bean to manage device association with an account.
 * @author tmolina
 *
 */
public class DeviceAccount implements Serializable {
	private static final long serialVersionUID = -3391105455976820169L;
	
	@EJB(mappedName = "ejb/Device")
	private Device deviceEJB;
	
	@EJB(mappedName = "ejb/User")
	private User user;
	
	@EJB(mappedName = "ejb/Vehicle")
	private Vehicle vehicle;
	
	
		
	// Attributes --------------- //
	
	private List<SelectItem> devices; 
	
	private List<TbSystemUser> clientsWithAccount;	
	
	// Control Modal ----------- //
	
	private boolean showModal;
	
	private boolean showConfirmation;
	
	private String modalMessage;
	
	private String corfirmMessage;	
	
	// -- //
	
	private List<SelectItem> devicesTypes;
	
	private Long typeDevId;
	
	private String facial;
	
	private TbDevice device;
	
	private Long deviceId;
	
	private List<SelectItem> facials;
	
	/**
	 * List of devices with open/active process.
	 */
	private List<TbDevice> devicesList;
	
	private List<ReAccountDevice> accountList;
	
	private List<SelectItem> clientAccounts;
	
	private Long idClientAccount;
	
	private List<SelectItem> listVehicle;
	
	private Long idVehicle;
		
	// Control Visibility --------------- //

	private String message;
	
	private boolean showMessage;
	
	private boolean showData;
	
	private boolean showSaveRelation;

	/**
	 * Constructor.
	 */
	public DeviceAccount() {
		devicesList = new ArrayList<TbDevice>();
		facials = new ArrayList<SelectItem>();
	}
	
	// Actions -------------------- //
	
	public String init(){
		hideModal();
		setClientsWithAccount(null);
		setDevices(null);
		setClientAccounts(null);
		setDeviceId(-1L);
		setTypeDevId(-2L);
		setIdClientAccount(-1L);
		setIdVehicle(-1L);
		setShowData(false);
		setShowMessage(false);
		setFacial(null);
		if(facials!=null){
			facials.clear();
		}
		setShowSaveRelation(false);
		return null;
	}
	
	/**
	 * Shows windows confirmation.
	 */
	public String initSave(){
		setCorfirmMessage("¿Está seguro de realizar la transacción?");
		setShowConfirmation(true);
		return null;
	}
	
	/**
	 * Hides Modal Window.
	 */
	public String hideModal(){
		setShowConfirmation(false);
		setShowModal(false);
		setModalMessage("");
		setCorfirmMessage("");
		return null;
	}
	
	/**
	 * Saves the association.
	 */
	public String saveProcess() {
		try{
		setShowConfirmation(false);
		if (deviceId.longValue() != -1L) {
			if (idClientAccount.longValue() != -1L) {
				if(idVehicle.longValue()!=-1){
					if(deviceEJB.maxDevice(idClientAccount)){
						Long state=deviceEJB.stateManufacterByDevice(deviceId);
						System.out.println("state: " + state);
						if(state!=-1){
							if(state!=1){
								if(state==0){
									System.out.println("fabricante activo");
									if(!deviceEJB.plateIsEnrollOtherClient(idVehicle, idClientAccount)){
										if (deviceEJB.saveAssociation(idClientAccount,deviceId, idVehicle, SessionUtil.ip(), SessionUtil.sessionUser().getUserId())) {
											devices = null;
											init();
											setModalMessage("Transacción Exitosa");
										} else{
											setModalMessage("Error en Transacción. Verifique que la cuenta esté activa.");
										}
									}
									else{
										System.out.println("El vehículo que intenta enrolar pertenece a otro cliente");
										setModalMessage("El vehículo que intenta enrolar pertenece a otro cliente.");
									}
								}
								else{
									System.out.println("fabricante eliminado");
									setModalMessage("Error al consultar fabricante del tag");
								}
							}
							else{
								System.out.println("fabricante eliminado");
								setModalMessage("Error. El Fabricante del tag fue eliminado");
							}
						}
						else{
							System.out.println("sin fabricante");
							setModalMessage("Error. El tag no posee fabricante");
						}

					}
					else{
						setModalMessage("El cliente no puede asociar mas tags a su cuenta su tipo de cuenta no se lo permite.");
					}
				}
				 else {
						setModalMessage("Debe Seleccionar una Placa.");
					}
			} else {
				setModalMessage("Debe Seleccionar una Cuenta Cliente.");
			}
		} else {
			setModalMessage("Verifique que este dispositivo ya se encuentre enrolado, o que se encuentre en lista negra");
		}
		}catch(Exception e){
			/**
			 * @author ablasquez
			 * Se valida errorres en el proceso de asociacion del dispositivo
			 */
			e.printStackTrace();
			setModalMessage("Se ha Presentado un Error al momento de Asociar el Dispositivo.");
		}
		setShowConfirmation(false);
		setShowModal(true);
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public String changeTypeDev() {
		showSaveRelation = false;
		showMessage = false;
		showData = false;
		facial = null;
		device = null;
		deviceId = -1L;
		facials.clear();
		if (typeDevId == -2L) {
			setMessage("Debe Seleccionar Un Tipo de Dispositivo.");
			setShowData(false);
			setShowSaveRelation(false);
			setShowMessage(true);
		} else {
			devicesList = deviceEJB.getAvailableDeviceByTypeNoAssig(typeDevId);	
			facials.add(new SelectItem(-1,""));
			String fac;
			for(TbDevice d : devicesList){
				if(d.getDeviceFacialId()!= null) {
					fac = d.getDeviceFacialId();
				} else {
					fac = "";
				}
				facials.add(new SelectItem(d.getDeviceId(), fac + " - " + d.getDeviceCode()));
			}
		}
		return null;
	}
	
	/**
	 * 
	 */
	public String search() {
		setShowSaveRelation(false);
		setShowData(false);
		setShowModal(false);
		device = null;
		deviceId = -1L;
		System.out.println("typeDevId" + typeDevId);
		if (typeDevId <0 || typeDevId==null ) {
			setModalMessage("Error: Debe Seleccionar Un Tipo de Dispositivo.");
			setShowData(false);
			setShowSaveRelation(false);
			setShowModal(true);
		} else {
			if (facial != null && !facial.equals("")) {
				devicesList = deviceEJB.getAvailableDeviceByType(typeDevId);	
				for(TbDevice d : devicesList){
					if(d.getDeviceFacialId()!=null && d.getDeviceFacialId().equals(facial.toUpperCase())){
						device = d;
						deviceId = d.getDeviceId();
						accountList = deviceEJB.getAccountDevice(deviceId);
						break;
					}
				}
				if (device == null) {
					setShowSaveRelation(false);
					setShowData(false);
					setShowModal(true);
					setModalMessage("Error: No se encontró un dispositivo con el facial ingresado.");
				} else {
					if(deviceEJB.haveActiveRelation(deviceId)){
						System.out.println("El dispositivo tiene relacion activa");
						setShowSaveRelation(false);
						setShowData(true);
						setModalMessage("El dispositivo ya se encuentra enrolado.");
						setShowModal(true);
					}
					else{
						System.out.println("El dispositivo no tiene relacion activa");
						setShowModal(false);
						setShowSaveRelation(true);
						setShowData(true);
						
					}
				}
			} else{
				setShowData(false);
				setShowModal(true);
				setShowSaveRelation(false);
				setModalMessage("Error: Digite El Facial del Dispositivo.");
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public String changeDevice(){
		if(deviceId == -1L){
			showMessage = false;
			showData = false;
			showSaveRelation = false;
			device = null;
			facial = null;
		} else {
			for(TbDevice d : devicesList){
				if(deviceId.longValue() ==  d.getDeviceId().longValue()){
					this.device = d;
					facial = d.getDeviceFacialId();
					accountList = deviceEJB.getAccountDevice(d.getDeviceId());
					break;
				}
			}
//			if(accountList.size() > 0) {
//				setShowSaveRelation(false);
//			} else {
//				setShowSaveRelation(true);
//			}
			setShowSaveRelation(true);
			showData = true;
		}
		return null;
	}
	
	// Getters and Setters ----------- //

	/**
	 * @param devices the devices to set
	 */
	public void setDevices(List<SelectItem> devices) {
		this.devices = devices;
	}

	/**
	 * @return the devices
	 */
	public List<SelectItem> getDevices() {
		if(devices == null){
			devices = new ArrayList<SelectItem>();
			devices.add(new SelectItem(-1, " "));
			for(TbDevice d : deviceEJB.getDevicesWithNoAccount()){
				devices.add(new SelectItem(d.getDeviceId(), d.getDeviceCode()));
			}
		}
		return devices;
	}

	/**
	 * @param clientsWithAccount the clientsWithAccount to set
	 */
	public void setClientsWithAccount(List<TbSystemUser> clientsWithAccount) {
		this.clientsWithAccount = clientsWithAccount;
	}

	/**
	 * @return the clientsWithAccount
	 */
	public List<TbSystemUser> getClientsWithAccount() {
		if (clientsWithAccount == null) {
			clientsWithAccount = new ArrayList<TbSystemUser>();
			clientsWithAccount = user.getClientsWithAccount();
		}
		return clientsWithAccount;
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
	 * @param devicesTypes the devicesTypes to set
	 */
	public void setDevicesTypes(List<SelectItem> devicesTypes) {
		this.devicesTypes = devicesTypes;
	}

	/**
	 * @return the devicesTyepes
	 */
	public List<SelectItem> getDevicesTypes() {
		if (devicesTypes == null) {
			devicesTypes = new ArrayList<SelectItem>();
			devicesTypes.add(new SelectItem(-2, ""));
			devicesTypes.add(new SelectItem(-1, "Sin Definir"));
			for (TbDeviceType dt : deviceEJB.getDevicesTypes()) {
				devicesTypes.add(new SelectItem(dt.getDeviceTypeId(), dt
						.getDeviceTypeName()));
			}
		}
		return devicesTypes;
	}

	/**
	 * @param typeDevId the typeDevId to set
	 */
	public void setTypeDevId(Long typeDevId) {
		this.typeDevId = typeDevId;
	}

	/**
	 * @return the typeDevId
	 */
	public Long getTypeDevId() {
		return typeDevId;
	}

	/**
	 * @param facial the facial to set
	 */
	public void setFacial(String facial) {
		this.facial = facial;
	}

	/**
	 * @return the facial
	 */
	public String getFacial() {
		return facial;
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
	 * @param facials the facials to set
	 */
	public void setFacials(List<SelectItem> facials) {
		this.facials = facials;
	}

	/**
	 * @return the facials
	 */
	public List<SelectItem> getFacials() {
		return facials;
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
	 * @param accountList the accountList to set
	 */
	public void setAccountList(List<ReAccountDevice> accountList) {
		this.accountList = accountList;
	}

	/**
	 * @return the accountList
	 */
	public List<ReAccountDevice> getAccountList() {
		return accountList;
	}

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
			clientAccounts.add(new SelectItem(-1L, "--Seleccione Una--"));
			for(TbAccount su : user.getClientAccount(null)){
				String name = su.getTbSystemUser().getUserNames();
				if(su.getTbSystemUser().getTbCodeType().getCodeTypeId().longValue() != 3L){
					name += " " + su.getTbSystemUser().getUserSecondNames();
				}
				clientAccounts.add(new SelectItem(su.getAccountId(), "Cuenta No. " + su.getAccountId() +"-"+name));
			}
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
	 * @param showSaveRelation the showSaveRelation to set
	 */
	public void setShowSaveRelation(boolean showSaveRelation) {
		this.showSaveRelation = showSaveRelation;
	}

	/**
	 * @return the showSaveRelation
	 */
	public boolean isShowSaveRelation() {
		return showSaveRelation;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setListVehicle(List<SelectItem> listVehicle) {
		this.listVehicle = listVehicle;
	}

	public List<SelectItem> getListVehicle() {
		if(listVehicle == null){
			listVehicle = new ArrayList<SelectItem>();
		} else {
			listVehicle.clear();
		}
		listVehicle.add(new SelectItem(-1L, "--Seleccione Una Opción--"));
		try {
			for(TbVehicle v : vehicle.getVehicleNoRegister()){
				listVehicle.add(new SelectItem(v.getVehicleId(),v.getVehiclePlate()));
			}
		}catch (NullPointerException ne) {
			ne.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();			
		}		
		return listVehicle;
	}

	public void setIdVehicle(Long idVehicle) {
		this.idVehicle = idVehicle;
	}

	public Long getIdVehicle() {
		return idVehicle;
	}

	
}