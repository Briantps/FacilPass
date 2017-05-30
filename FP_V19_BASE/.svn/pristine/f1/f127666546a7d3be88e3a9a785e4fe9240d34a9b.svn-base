package process.affiliation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import sessionVar.SessionUtil;
import validator.Validation;

import ejb.Device;

import jpa.ReAccountDevice;

public class DisableDeviceAccount implements Serializable{

	private static final long serialVersionUID = 1L;

	@EJB(mappedName = "ejb/Device")
	private Device deviceEJB;
	
	private Long relationId;
	
	private Long searchId;
	
	private String value;
	
	private String corfirmMessage;
	
	private String modalMessage;
	
	private List<SelectItem> searchList;
	
	private List<ReAccountDevice> accountDeviceList;
	
	private boolean showData;
	
	private boolean showConfirmation;
	
	private boolean showModal;
	
	private Long accountId;
	
	private String deviceCode;
	
	private String deviceFacialId;
	
	private String vehiclePlate;
	
	private String ip;
	
	private Long user;
	
	public DisableDeviceAccount(){
		super();
	}

	/**
	 * @param relationId the relationId to set
	 */
	public void setRelationId(Long relationId) {
		this.relationId = relationId;
	}

	/**
	 * @return the relationId
	 */
	public Long getRelationId() {
		return relationId;
	}

	/**
	 * @param searchId the searchId to set
	 */
	public void setSearchId(Long searchId) {
		this.searchId = searchId;
	}

	/**
	 * @return the searchId
	 */
	public Long getSearchId() {
		return searchId;
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
	 * @param searchList the searchList to set
	 */
	public void setSearchList(List<SelectItem> searchList) {
		this.searchList = searchList;
	}

	/**
	 * @return the searchList
	 */
	public List<SelectItem> getSearchList() {
		searchList= new ArrayList<SelectItem>();
		searchList.add(new SelectItem(1L, "Placa"));
		searchList.add(new SelectItem(2L, "Id Dispositivo"));
		searchList.add(new SelectItem(3L, "Id Facial"));
		return searchList;
	}

	/**
	 * @param accountDeviceList the accountDeviceList to set
	 */
	public void setAccountDeviceList(List<ReAccountDevice> accountDeviceList) {
		this.accountDeviceList = accountDeviceList;
	}

	/**
	 * @return the accountDeviceList
	 */
	public List<ReAccountDevice> getAccountDeviceList() {
		return accountDeviceList;
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
	 * @param deviceCode the deviceCode to set
	 */
	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}

	/**
	 * @return the deviceCode
	 */
	public String getDeviceCode() {
		return deviceCode;
	}

	/**
	 * @param deviceFacialId the deviceFacialId to set
	 */
	public void setDeviceFacialId(String deviceFacialId) {
		this.deviceFacialId = deviceFacialId;
	}

	/**
	 * @return the deviceFacialId
	 */
	public String getDeviceFacialId() {
		return deviceFacialId;
	}

	/**
	 * @param vehiclePlate the vehiclePlate to set
	 */
	public void setVehiclePlate(String vehiclePlate) {
		this.vehiclePlate = vehiclePlate;
	}

	/**
	 * @return the vehiclePlate
	 */
	public String getVehiclePlate() {
		return vehiclePlate;
	}

	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(Long user) {
		this.user = user;
	}

	/**
	 * @return the user
	 */
	public Long getUser() {
		return user;
	}

	public String search(){
		int flag=0;
		if(searchId!=null){
			if(value!=null){
				if(!value.equals("")){
					this.setValue(this.getValue().toUpperCase());

					if(Validation.isAlphaNumWithOutSpace(value)){
						flag=0;
					}
					else{
						flag=1;
					}
					System.out.println("flag: "+ flag);

				    if(flag==0){
				    	accountDeviceList=deviceEJB.searchReAccountDevice(searchId,value);
						if(accountDeviceList!=null){
							if(accountDeviceList.size()>0){
								this.setShowData(true);
								this.setShowModal(false);
							}
							else{
								this.setShowData(false);
								this.setModalMessage("No se encontró información");
								this.setShowModal(true);
							}
						}
						else{
							this.setShowData(false);
							this.setModalMessage("No se encontró información");
							this.setShowModal(true);
						}
				    }
				    else{
				    	this.setModalMessage("El parámetro no debe contener caracteres especiales, solo letras y números");
						this.setShowModal(true);
				    }
				}
				else{
					this.setModalMessage("Debe digitar un valor para la búsqueda");
					this.setShowModal(true);
				}
			}
			else{
				this.setModalMessage("Debe digitar un valor para la búsqueda");
				this.setShowModal(true);
			}
		}
		else{
			this.setModalMessage("Debe seleccionar Criterio de Búsqueda");
			this.setShowModal(true);
		}
		return null;
	}
	
	public void confirmation(){
		System.out.println("relationId: " + relationId);
		System.out.println("accountId: " + accountId);
		System.out.println("deviceCode: " + deviceCode);
		System.out.println("deviceFacialId: " + deviceFacialId);
		System.out.println("vehiclePlate: " + vehiclePlate);
		this.setCorfirmMessage("¿Está seguro que desea desactivar la relación del Tag: " +deviceCode+
				" - Facial: " +deviceFacialId  + " asociado a la placa: " + vehiclePlate + " con cuenta FacilPass No: " + accountId+ "?");
		this.setShowConfirmation(true);
	}
	
	public String hideModal(){
		this.setShowConfirmation(false);
		this.setShowModal(false);
		this.setModalMessage("");
		this.setCorfirmMessage("");
		this.setValue("");
		this.setShowData(false);
		return null;
	}
	
	public String disabled(){
		this.setShowConfirmation(false);
		System.out.println("Se entró al metodo para deshabilitar la relacion con relationId: " + relationId);
		if(deviceEJB.disableReAccountDevice(accountId,deviceCode,relationId,vehiclePlate,SessionUtil.ip(), SessionUtil.sessionUser().getUserId())){
			
			this.setModalMessage("Se desactivó la relación Tag - Vehículo correctamente");
			this.setShowModal(true);
			
		}
		else{
			this.setModalMessage("Ocurrió un error al desactivar la relación Tag - Vehículo.");
			this.setShowModal(true);
		}
		
		return null;
	}
	
	private boolean isPlacaValida(String plac) {
		Pattern p = Pattern.compile("[a-zA-Z]{2}[0-9]{4}");
		Matcher m = p.matcher(plac);

		if (m.matches()) {
			return true;
		} else {
			p = Pattern.compile("[a-zA-Z]{3}[0-9]{3}");
			m = p.matcher(plac);
			return m.matches();
		}
	}
	public void ocult(){
		this.setShowData(false);
		this.setValue("");
	}
	
	public void ocult1(){
		this.setShowData(false);
	}

	
}
