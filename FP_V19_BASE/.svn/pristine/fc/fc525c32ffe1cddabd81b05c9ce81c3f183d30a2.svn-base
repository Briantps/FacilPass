/**
 * 
 */
package process.device.perti;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import sessionVar.SessionUtil;

import jpa.TbDeviceCustomization;
import jpa.TbDeviceType;
import jpa.TbInstallationLocation;
import jpa.TbSystemUser;
//import jpa.TbOperationType;

import ejb.Master;
import ejb.User;
import ejb.crud.InstallationLocation;

/**
 * Bean to manage the register of a ties installation.
 * @author tmolina
 *
 */
public class InstallationBean implements Serializable {
	private static final long serialVersionUID = 8975774268409526043L;
	
	@EJB(mappedName = "ejb/Master")
	private Master master;
	
	@EJB(mappedName="ejb/InstallationLocation")
	private InstallationLocation location;
	
	@EJB(mappedName="ejb/User")
	private User userEjb;
	
//	// Attributes --- //
//	
//	/* --- Operation Type: New Or Reposition --- */
//	
//	private List<SelectItem> operationTypes;
//	
//	private Long operationTypeId;
	
	/* --- Device Type : Special Or Exempt --- */
	
	private Long deviceTypeId;
	
	private List<SelectItem> typeList;
	
	private boolean showData;
	
	private TbDeviceCustomization customization;
	
	private String modalMessage;
	
	private boolean showModal;
	
	private boolean searchByPlate;
	
	private boolean searchByCardId;
	
	private String cardId;
	
	private boolean searchByCardFacial;
	
	private String cardFacial;
	
	private String plate;
	
	/* -- To save -- */
	
	private String observation;
	
	private String installed;
	
	private List<SelectItem> installedList;
	
	private String installationCertificate;
	
	private List<SelectItem> installationCertificateOptions;
	
	private String installationSupport;
	
	private List<SelectItem> iinstallationSupportOptions;
	
	private String receiverName;
	
	private String receiverIdentificationNumber;
	
	private Date installationDate;
	
	private List<SelectItem> locationList;
	
	private Long locationId;
	
	private List<SelectItem> installationUsers;
	
	private Long installationUserId;
	
	private boolean showErrorMessage;
	
	private String errorMessage;

	/**
	 * Constructor.
	 */
	public InstallationBean() {
		setSearchByPlate(true);
	}
	
	// Actions ------------ //
	
	/**
	 * Hides modal panel.
	 */
	public String hideModal() {
		setShowModal(false);
		setModalMessage(null);
		return null;
	}
	
	/**
	 * Saves the installation.
	 */
	public String save() {
		setErrorMessage(null);
		setShowErrorMessage(false);
		if(installed!= null && !installed.equals("-1")) {
			if(installationCertificate!=null && !installationCertificate.equals("-1")){
				if(installationSupport!=null && !installationSupport.equals("-1")){
					if(installationUserId!=null && installationUserId.longValue() !=-1L){
						if(locationId!=null && locationId.longValue() !=-1L){
							if(installationDate!=null){
								if(receiverName !=null && receiverName.trim().length()> 0 ){
									if (master.saveInstallation(customization
											.getDeviceCustomizationId(),
											installed, installationCertificate,
											installationSupport, receiverName,
											receiverIdentificationNumber,
											installationDate, locationId,
											installationUserId, SessionUtil
													.sessionUser().getUserId(),
											SessionUtil.ip(), observation)) {
										setModalMessage("Transacción exitosa");
										clear();
										setShowData(false);
										setShowModal(true);
									} else {
										setModalMessage("Error en Transacción.");
										setShowModal(true);
									}
								} else {
									setErrorMessage("Digite el nombre de la persona que recibió.");
									setShowErrorMessage(true);
								}
							} else {
								setErrorMessage("Seleccione La fecha de instalación.");
								setShowErrorMessage(true);
							}
						} else {
							setErrorMessage("Seleccione el lugar de instalación.");
							setShowErrorMessage(true);
						}
					} else {
						setErrorMessage("Seleccione La persona que realizó la instalación.");
						setShowErrorMessage(true);
					}
				} else {
					setErrorMessage("Seleccione una opción de Soporte.");
					setShowErrorMessage(true);
				}
			} else {
				setErrorMessage("Seleccione una opción de Acta.");
				setShowErrorMessage(true);
			}
		} else {
			setErrorMessage("Seleccione una opción de Instalada.");
			setShowErrorMessage(true);
		}
		return null;
	}
	
	/**
	 * Clear Data.
	 */
	public String clear(){
		setLocationList(null);
		setInstallationUsers(null);
		setIinstallationSupportOptions(null);
		setInstallationCertificateOptions(null);
		setInstalledList(null);
		setPlate(null);
		setCardFacial(null);
		setCardId(null);
		setInstallationDate(null);
		setObservation(null);
		setReceiverIdentificationNumber(null);
		setReceiverName(null);
		setShowErrorMessage(false);
		setErrorMessage(null);
		return null;
	}
	
	/**
	 * searches by the criteria to register the installation.
	 */
	public String search() {
		if (searchByPlate || searchByCardFacial || searchByCardId) {
			if(searchByPlate) {
				if( plate!=null && plate.trim().length()==6 && validatePlate(plate)){
				} else {
					setModalMessage("Debe digitar una placa válida.");
					setShowModal(true);
					return null;
				}
			} 
			
			if(searchByCardFacial){
				if (cardFacial != null && cardFacial.trim().length() > 0) {
				} else {
					setModalMessage("Debe digitar un Facial válido.");
					setShowModal(true);
					return null;
				}
			}
			
			if(searchByCardId){
				if (cardId != null && cardId.trim().length() > 0) {
				} else {
					setModalMessage("Debe digitar un ID de tarjeta válido.");
					setShowModal(true);
					return null;
				}
			}
			
			this.customization = master.getCustomization(searchByPlate,
					searchByCardId, cardId, searchByCardFacial, cardFacial,
					plate, deviceTypeId);
			
			if(customization != null) {
				setShowData(true);
				clear();
			} else {
				setShowData(false);
				setShowModal(true);
				setModalMessage("No se ha encontrado un proceso con los datos ingresados o todavía no se ha hecho personalización alguna.");
			}
		} else {
			setModalMessage("Debe escoger un criterio de busqueda.");
			setShowModal(true);
		}
		return null;
	}
	
	// Helpers --- //
	
	/**
	 * Validates a vehicle plate.
	 */
	private boolean validatePlate(String plate){
		Pattern p = Pattern.compile("[a-zA-Z]{2}[0-9]{4}");
	    Matcher m = p.matcher(plate);
	    
	    boolean match = false;
	    
	    if (m.matches()) {
			match = true;
	    } else {
			p = Pattern.compile("[a-zA-Z]{3}[0-9]{3}");
			m = p.matcher(plate);
			match = m.matches();
		}	
	    return match;
	}
	
	// Getters and Setters ----------- //

//	/**
//	 * @param operationTypes the operationTypes to set
//	 */
//	public void setOperationTypes(List<SelectItem> operationTypes) {
//		this.operationTypes = operationTypes;
//	}
//
//	/**
//	 * @return the operationTypes
//	 */
//	public List<SelectItem> getOperationTypes() {
//		if(operationTypes == null){
//			operationTypes = new ArrayList<SelectItem>();
//			List<TbOperationType> list = master.getPertiOperationTypes();
//			if (list.size() > 0) {
//				operationTypeId = list.get(0).getOperationTypeId();
//			}
//			for (TbOperationType ot : list) {
//				operationTypes.add(new SelectItem(ot.getOperationTypeId(), ot
//						.getOperationTypeName()));
//			}
//		}
//		return operationTypes;
//	}
//
//	/**
//	 * @param operationTypeId the operationTypeId to set
//	 */
//	public void setOperationTypeId(Long operationTypeId) {
//		this.operationTypeId = operationTypeId;
//	}
//
//	/**
//	 * @return the operationTypeId
//	 */
//	public Long getOperationTypeId() {
//		return operationTypeId;
//	}

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
	 * @param deviceTypeId the deviceTypeId to set
	 */
	public void setDeviceTypeId(Long deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}

	/**
	 * @return the deviceTypeId
	 */
	public Long getDeviceTypeId() {
		return deviceTypeId;
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
		if (typeList == null) {
			typeList = new ArrayList<SelectItem>();
			List<TbDeviceType> list = master.getPertiDeviceTypes();
			if (list.size() > 0) {
				deviceTypeId = list.get(0).getDeviceTypeId();
			}
			
			for (TbDeviceType dt : list) {
					typeList.add(new SelectItem(dt.getDeviceTypeId(), dt
							.getDeviceTypeName()));
			}
		}
		return typeList;
	}

	/**
	 * @param customization the customization to set
	 */
	public void setCustomization(TbDeviceCustomization customization) {
		this.customization = customization;
	}

	/**
	 * @return the customization
	 */
	public TbDeviceCustomization getCustomization() {
		return customization;
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
	 * @param searchByPlate the searchByPlate to set
	 */
	public void setSearchByPlate(boolean searchByPlate) {
		this.searchByPlate = searchByPlate;
	}

	/**
	 * @return the searchByPlate
	 */
	public boolean isSearchByPlate() {
		return searchByPlate;
	}

	/**
	 * @param plate the plate to set
	 */
	public void setPlate(String plate) {
		this.plate = plate;
	}

	/**
	 * @return the plate
	 */
	public String getPlate() {
		return plate;
	}

	/**
	 * @param searchByCardId the searchByCardId to set
	 */
	public void setSearchByCardId(boolean searchByCardId) {
		this.searchByCardId = searchByCardId;
	}

	/**
	 * @return the searchByCardId
	 */
	public boolean isSearchByCardId() {
		return searchByCardId;
	}

	/**
	 * @param searchByCardFacial the searchByCardFacial to set
	 */
	public void setSearchByCardFacial(boolean searchByCardFacial) {
		this.searchByCardFacial = searchByCardFacial;
	}

	/**
	 * @return the searchByCardFacial
	 */
	public boolean isSearchByCardFacial() {
		return searchByCardFacial;
	}

	/**
	 * @param cardFacial the cardFacial to set
	 */
	public void setCardFacial(String cardFacial) {
		this.cardFacial = cardFacial;
	}

	/**
	 * @return the cardFacial
	 */
	public String getCardFacial() {
		return cardFacial;
	}

	/**
	 * @param cardId the cardId to set
	 */
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	/**
	 * @return the cardId
	 */
	public String getCardId() {
		return cardId;
	}

	/**
	 * @param installed the installed to set
	 */
	public void setInstalled(String installed) {
		this.installed = installed;
	}

	/**
	 * @return the installed
	 */
	public String getInstalled() {
		return installed;
	}

	/**
	 * @param installedList the installedList to set
	 */
	public void setInstalledList(List<SelectItem> installedList) {
		this.installedList = installedList;
	}

	/**
	 * @return the installedList
	 */
	public List<SelectItem> getInstalledList() {
		if(installedList == null ){
			installedList = new ArrayList<SelectItem>();
			installedList.add(new SelectItem("-1",""));
			installedList.add(new SelectItem("SI","SI"));
			installedList.add(new SelectItem("NO","NO"));
		}
		return installedList;
	}

	/**
	 * @param installationCertificate the installationCertificate to set
	 */
	public void setInstallationCertificate(String installationCertificate) {
		this.installationCertificate = installationCertificate;
	}

	/**
	 * @return the installationCertificate
	 */
	public String getInstallationCertificate() {
		return installationCertificate;
	}

	/**
	 * @param installationCertificateOptions the installationCertificateOptions to set
	 */
	public void setInstallationCertificateOptions(
			List<SelectItem> installationCertificateOptions) {
		this.installationCertificateOptions = installationCertificateOptions;
	}

	/**
	 * @return the installationCertificateOptions
	 */
	public List<SelectItem> getInstallationCertificateOptions() {
		if(installationCertificateOptions == null ){
			installationCertificateOptions = new ArrayList<SelectItem>();
			installationCertificateOptions.add(new SelectItem("-1",""));
			installationCertificateOptions.add(new SelectItem("SI","SI"));
			installationCertificateOptions.add(new SelectItem("NO","NO"));
		}
		return installationCertificateOptions;
	}

	/**
	 * @param installationSupport the installationSupport to set
	 */
	public void setInstallationSupport(String installationSupport) {
		this.installationSupport = installationSupport;
	}

	/**
	 * @return the installationSupport
	 */
	public String getInstallationSupport() {
		return installationSupport;
	}

	/**
	 * @param iinstallationSupportOptions the iinstallationSupportOptions to set
	 */
	public void setIinstallationSupportOptions(
			List<SelectItem> iinstallationSupportOptions) {
		this.iinstallationSupportOptions = iinstallationSupportOptions;
	}

	/**
	 * @return the iinstallationSupportOptions
	 */
	public List<SelectItem> getIinstallationSupportOptions() {
		if(iinstallationSupportOptions == null ){
			iinstallationSupportOptions = new ArrayList<SelectItem>();
			iinstallationSupportOptions.add(new SelectItem("-1",""));
			iinstallationSupportOptions.add(new SelectItem("SI","SI"));
			iinstallationSupportOptions.add(new SelectItem("NO","NO"));
		}
		return iinstallationSupportOptions;
	}

	/**
	 * @param receiverName the receiverName to set
	 */
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	/**
	 * @return the receiverName
	 */
	public String getReceiverName() {
		return receiverName;
	}

	/**
	 * @param receiverIdentificationNumber the receiverIdentificationNumber to set
	 */
	public void setReceiverIdentificationNumber(
			String receiverIdentificationNumber) {
		this.receiverIdentificationNumber = receiverIdentificationNumber;
	}

	/**
	 * @return the receiverIdentificationNumber
	 */
	public String getReceiverIdentificationNumber() {
		return receiverIdentificationNumber;
	}

	/**
	 * @param installationDate the installationDate to set
	 */
	public void setInstallationDate(Date installationDate) {
		this.installationDate = installationDate;
	}

	/**
	 * @return the installationDate
	 */
	public Date getInstallationDate() {
		return installationDate;
	}

	/**
	 * @param locationList the locationList to set
	 */
	public void setLocationList(List<SelectItem> locationList) {
		this.locationList = locationList;
	}

	/**
	 * @return the locationList
	 */
	public List<SelectItem> getLocationList() {
		if(locationList == null){
			locationList = new ArrayList<SelectItem>();
			locationList.add(new SelectItem(-1L, ""));
			for (TbInstallationLocation l : location.getInstallationLocations()) {
				locationList.add(new SelectItem(l.getInstallationLocationId(),
						l.getInstallationLocationName()));
			}
		}
		return locationList;
	}

	/**
	 * @param locationId the locationId to set
	 */
	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	/**
	 * @return the locationId
	 */
	public Long getLocationId() {
		return locationId;
	}

	/**
	 * @param installationUserId the installationUserId to set
	 */
	public void setInstallationUserId(Long installationUserId) {
		this.installationUserId = installationUserId;
	}

	/**
	 * @return the installationUserId
	 */
	public Long getInstallationUserId() {
		return installationUserId;
	}

	/**
	 * @param installationUsers the installationUsers to set
	 */
	public void setInstallationUsers(List<SelectItem> installationUsers) {
		this.installationUsers = installationUsers;
	}

	/**
	 * @return the installationUsers
	 */
	public List<SelectItem> getInstallationUsers() {
		if(installationUsers == null){
			installationUsers= new ArrayList<SelectItem>();
			installationUsers.add(new SelectItem(-1L,""));
			for (TbSystemUser su : userEjb.getInstallationUsers()) {
				installationUsers.add(new SelectItem(su.getUserId(), su
						.getUserNames()
						+ " " + su.getUserSecondNames()));
			}
		}
		return installationUsers;
	}

	/**
	 * @param showErrorMessage the showErrorMessage to set
	 */
	public void setShowErrorMessage(boolean showErrorMessage) {
		this.showErrorMessage = showErrorMessage;
	}

	/**
	 * @return the showErrorMessage
	 */
	public boolean isShowErrorMessage() {
		return showErrorMessage;
	}

	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
}