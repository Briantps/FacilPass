/**
 * 
 */
package mBeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import sessionVar.SessionUtil;
import util.Cities;

import ejb.User;

import jpa.TbCodeType;
import jpa.TbSystemUser;
import jpa.TbUserData;

/**
 * @author tmolina
 *
 */
public class ClientBean implements Serializable{
	private static final long serialVersionUID = -5606921759933774034L;
	
	@EJB(mappedName="ejb/User")
	private User userEJB;
	
	// Attributes ----------------//
	
	private Long codeTypeId;
	
	private List<SelectItem> codeTypeList;
	
	private String userCode;
	
	private String codeClient;
	
	private TbSystemUser client;
	
	private List<TbUserData> clientData;

	// New Office --------------//
	
	private boolean showCreate;
	
	private String officeName;
	
	private String address;
	
	private String phone;
	
	private String optionalPhone;
	
	private String contactName;
	
	private List<SelectItem> cities;
	
	private Long city;
	
	private Long dataId;
	
	// Control Visibility ---------//
	
	private boolean showMessage;
	
	private boolean showData;
	
	private boolean showModificate;
	
	// Control modal -----------//
	
	//private boolean showModal;
	
	private boolean showModalMod;
	
	//private String modalMessage;
	
	private String message;

	/**
	 * Constructor.
	 */
	public ClientBean() {
	}
	
	/**
	 * 
	 */
	@PostConstruct
	public void init() {
		codeTypeList = new ArrayList<SelectItem>();
		for(TbCodeType c : userEJB.getCodeTypes()) {
			codeTypeList.add(new SelectItem(c.getCodeTypeId(), c.getCodeTypeDescription()));
		}
	}
	// Actions -------------------//
	
	/**
	 * Hides modal Panel.
	 */
	public String hideModal(){
		setShowMessage(false);
		setShowCreate(false);
		setShowModificate(false);
		setShowModalMod(false);
		setShowData(false);
		setMessage("");
		return null;
	}
	
	/**
	 * Searches the client.
	 * Modificación realizada para que el filtro se realice por medio de
	 * la selección de tipo de documento y número de documento por separado.
	 * Por Andrés Tejedor
	 */
	
	public String search() {
		if(userCode != "") {
			client = userEJB.getUserByCode(userCode, codeTypeId);
			if(client != null) {
				setShowData(true);
				setShowMessage(false);
				clientData = userEJB.getClientData(client.getUserId());
			}
			else {
				setMessage("No hay información para el cliente digitado.");
				setShowMessage(true);
				setShowData(false);
			}
		}
		else {	
			setMessage("Digíte el Número de Identificación del Usuario.");
			setShowMessage(true);
			setShowData(false);
		}
		return null;
	}	
	
	/**
	 * Init create new Office.
	 */
	public String initClientBean(){
		clear();
		setShowCreate(true);
		return null;
	}
	
	/**
	 * Saves the new Office.
	 */
	public String saveOffice(){
		try{
			
			Boolean wrongPhone=false;
			Boolean wrongPhone2=false;
			
			try {
				int phoneWrong = Integer.parseInt(phone);
				if(phoneWrong<=10){
					wrongPhone=true;
				}
			} catch (Exception e) {
               System.out.println("Numero con extension.");
			}
			
			if(!"".equals(optionalPhone)){				
				try {
					int phoneWrong2 = Integer.parseInt(optionalPhone);
					if(phoneWrong2<=10){
						wrongPhone2=true;
					}
				} catch (Exception e) {
					System.out.println("Numero con extension.");
				}
			}
			
			if(officeName.equals(null) || officeName.equals("")){
				this.setMessage("El campo Dependencia es requerido.");
			}else if(officeName.length() < 3 || officeName.length() > 50){
				this.setMessage("La longitud del campo Dependencia no es correcta. Recuerde que debe estar entre 3 y 50 caracteres.");
			}else if(address.equals(null) || address.equals("")){
				this.setMessage("El campo Dirección es requerido.");
			}else if(address.length() < 7 || address.length() > 50){
				this.setMessage("La longitud del campo Dirección no es correcta. Recuerde que debe estar entre 7 y 50 caracteres.");
			}else if (phone.equals(null) || phone.equals("")){
				this.setMessage("El campo Teléfono es requerido.");
			}else if(phone.length() < 7 || phone.length() > 15){
				this.setMessage("La longitud del campo Teléfono no es correcta. Recuerde que debe estar entre 7 y 15 caracteres.");
			}else if(optionalPhone.length() != 0&&(optionalPhone.length() < 7 || optionalPhone.length() > 15)){
				this.setMessage("La longitud del campo Teléfono Opcional no es correcta. Recuerde que debe estar entre 7 y 15 caracteres.");
			}else if(contactName.equals(null) || contactName.equals("")){
				this.setMessage("El campo Contacto es requerido.");
			}else if(contactName.length() < 3 || contactName.length() > 50){
				this.setMessage("La longitud del campo Contacto no es correcta. Recuerde que debe estar entre 3 y 50 caracteres.");
			}
			else if(wrongPhone){this.setMessage("El numero telefonico "+phone+" no es valido.");}
			else if(wrongPhone2){this.setMessage("El numero telefonico "+optionalPhone+" no es valido.");}
			
			else {
				if(userEJB.saveClientData(client.getUserId(), officeName, address, phone,
					optionalPhone, contactName, SessionUtil.ip(), SessionUtil
							.sessionUser().getUserId(), city)){
					this.setMessage("Transacción Exitosa.");
				}else{
					this.setMessage("Error en transacción.");
					System.out.println("message if" + message);
				}
			}
			this.setShowCreate(false);
			this.setShowMessage(true);
			clientData = userEJB.getClientData(client.getUserId());
			System.out.println("message -" + message);
		}catch(Exception ex){
			System.out.println("Error ClientBean.saveOffice. " + message);
			ex.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Clear Data.
	 */
	private void clear(){
		setAddress("");
		setPhone(null);
		setOptionalPhone(null);
		setOfficeName("");
		setContactName("");
	}
	
	/**
	 * 
	 * Init modificate office.
	 */
	public String initMod(){
		for(TbUserData ud: clientData){
			if(dataId.longValue() == ud.getUserDataId().longValue()){
				setAddress(ud.getUserDataAddress());
				setPhone(ud.getUserDataPhone());
				setOptionalPhone(ud.getUserDataOptionalPhone());
				setOfficeName(ud.getUserDataOfficeName());
				setContactName(ud.getUserDataContact());
				setCity(ud.getTbMunicipality().getMunicipalityId());
				break;
			}
		}
		setShowModificate(true);
		return null;
	}
	
	/**
	 * Modificate the Office selected.
	 */
	public String modOffice(){
		
		Boolean wrongPhone=false;
		Boolean wrongPhone2=false;
		
		try {
			int phoneWrong = Integer.parseInt(phone);
			if(phoneWrong<=10){
				wrongPhone=true;
			}
		} catch (Exception e) {
			System.out.println("Numero con extension.");
		}
		
		if(!"".equals(optionalPhone)){				
			try {
				int phoneWrong2 = Integer.parseInt(optionalPhone);
				if(phoneWrong2<=10){
					wrongPhone2=true;
				}
			} catch (Exception e) {
				System.out.println("Numero con extension.");
			}
		}
		
		if(officeName.equals(null) || officeName.equals(""))
		{
			setMessage("El campo Dependencia es requerido.");
		}
		else if(officeName.length() < 3 || officeName.length() > 50)
		{
			setMessage("La longitud del campo Dependencia no es correcta. Recuerde que debe estar entre 3 y 50 caracteres.");
		}
		else if(address.equals(null) || address.equals(""))
		{
			setMessage("El campo Dirección es requerido.");
		}
		else if(address.length() < 7 || address.length() > 50)
		{
			setMessage("La longitud del campo Dirección no es correcta. Recuerde que debe estar entre 3 y 50 caracteres.");
		}
		else if(phone.equals(null) || phone.equals(""))
		{
			setMessage("El campo Teléfono es requerido.");
		}
		else if(phone.length() < 7 || phone.length() > 15)
		{
			setMessage("La longitud del campo Teléfono no es correcta. Recuerde que debe estar entre 7 y 15 caracteres.");
		}
		else if(optionalPhone.length() != 0&&(optionalPhone.length() < 7 || optionalPhone.length() > 15))
		{
			setMessage("La longitud del campo Teléfono Opcional no es correcta. Recuerde que debe estar entre 7 y 15 caracteres.");
		}
		else if(contactName.equals(null) || contactName.equals(""))
		{
			setMessage("El campo Contacto es requerido.");
		}
		else if(contactName.length() < 3 || contactName.length() > 50)
		{
			setMessage("La longitud del campo Contacto no es correcta. Recuerde que debe estar entre 3 y 50 caracteres.");
		}
		else if(wrongPhone){this.setMessage("El numero telefonico "+phone+" no es valido.");}
		else if(wrongPhone2){this.setMessage("El numero telefonico "+optionalPhone+" no es valido.");}
		
		else{
			if (userEJB.modClientData(client.getUserId(), officeName, address, phone,optionalPhone, contactName, SessionUtil.ip(), SessionUtil
					.sessionUser().getUserId(), city, dataId)){
		                setMessage("Actualización Exitosa.");
		                clientData = userEJB.getClientData(client.getUserId());
	         }
			else
			{
				setMessage("Error en Transacción, comuníquese con Servicio al Cliente.");
			}
		}
		
				
		setShowModificate(false);
		setShowMessage(true);
		return null;
	}
	
	// Getters and Setters ----------//

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
	 * @param showModal the showModal to set
	 */
	/*public void setShowModal(boolean showModal) {
		this.showModal = showModal;
	}*/

	/**
	 * @return the showModal
	 */
	/*public boolean isShowModal() {
		return showModal;
	}*/

	/**
	 * @param modalMessage the modalMessage to set
	 */
	/*public void setModalMessage(String modalMessage) {
		this.modalMessage = modalMessage;
	}*/

	/**
	 * @return the modalMessage
	 */
	/*public String getModalMessage() {
		return modalMessage;
	}*/
	
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the errorMessage
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
	 * @param clientData the clientData to set
	 */
	public void setClientData(List<TbUserData> clientData) {
		this.clientData = clientData;
	}

	/**
	 * @return the clientData
	 */
	public List<TbUserData> getClientData() {
		return clientData;
	}

	/**
	 * @param showCreate the showCreate to set
	 */
	public void setShowCreate(boolean showCreate) {
		this.showCreate = showCreate;
	}

	/**
	 * @return the showCreate
	 */
	public boolean isShowCreate() {
		return showCreate;
	}

	/**
	 * @param officeName the officeName to set
	 */
	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	/**
	 * @return the officeName
	 */
	public String getOfficeName() {
		return officeName;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param optionalPhone the optionalPhone to set
	 */
	public void setOptionalPhone(String optionalPhone) {
		this.optionalPhone = optionalPhone;
	}

	/**
	 * @return the optionalPhone
	 */
	public String getOptionalPhone() {
		return optionalPhone;
	}

	/**
	 * @param contactName the contactName to set
	 */
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	/**
	 * @return the contactName
	 */
	public String getContactName() {
		return contactName;
	}
	
	/**
	 * @param cities the cities to set
	 */
	public void setCities(List<SelectItem> cities) {
		this.cities = cities;
	}

	/**
	 * @return the cities
	 */
	public List<SelectItem> getCities() {
		cities = new ArrayList<SelectItem>();
		List<Cities> c = userEJB.getCities();
		for(Cities ci : c){
			cities.add(new SelectItem(ci.getCityCode(), ci.getCity() + " - "  + "(" + ci.getDepartment().substring(0,4) + ")"));
		}
		return cities;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(Long city) {
		this.city = city;
	}

	/**
	 * @return the city
	 */
	public Long getCity() {
		return city;
	}

	/**
	 * @param showModificate the showModificate to set
	 */
	public void setShowModificate(boolean showModificate) {
		this.showModificate = showModificate;
	}

	/**
	 * @return the showModificate
	 */
	public boolean isShowModificate() {
		return showModificate;
	}

	/**
	 * @param dataId the dataId to set
	 */
	public void setDataId(Long dataId) {
		this.dataId = dataId;
	}

	/**
	 * @return the dataId
	 */
	public Long getDataId() {
		return dataId;
	}

	/**
	 * @param showModalMod the showModalMod to set
	 */
	public void setShowModalMod(boolean showModalMod) {
		this.showModalMod = showModalMod;
	}

	/**
	 * @return the showModalMod
	 */
	public boolean isShowModalMod() {
		return showModalMod;
	}
	
	/**
	 * @param codeTypeId the codeTypeId to set
	 */
	public void setCodeTypeId(Long codeTypeId) {
		this.codeTypeId = codeTypeId;
	}

	/**
	 * @return the codeTypeId
	 */
	public Long getCodeTypeId() {
		return codeTypeId;
	}
	
	/**
	 * @param codeTypeList the codeTypeList to set
	 */
	public void setCodeTypeList(List<SelectItem> codeTypeList) {
		this.codeTypeList = codeTypeList;
	}

	/**
	 * @return the codeTypeList
	 */
	public List<SelectItem> getCodeTypeList() {
		return codeTypeList;
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
		return userCode;
	}	
}