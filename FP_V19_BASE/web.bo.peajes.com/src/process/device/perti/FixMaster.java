/**
 * 
 */
package process.device.perti;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import sessionVar.SessionUtil;

import constant.CustomizationState;
import constant.DeviceType;
import constant.OperationType;

import ejb.BrandManager;
import ejb.Category;
import ejb.Color;
import ejb.Master;
import ejb.User;
import ejb.Vehicle;

import jpa.TbAccount;
import jpa.TbBrand;
import jpa.TbCategory;
import jpa.TbColor;
import jpa.TbDepartment;
import jpa.TbDeviceCustomization;
import jpa.TbDeviceType;
import jpa.TbSpecialExemptOffice;
import jpa.TbSpecialExemptType;
import jpa.TbStationBO;
import jpa.TbSystemUser;
import jpa.TbVehicle;

/**
 * Bean To manage Fix Master.
 * @author tmolina
 *
 */
public class FixMaster implements Serializable {
	private static final long serialVersionUID = -5450793606098513498L;

	@EJB(mappedName = "ejb/Master")
	private Master master;
	
	@EJB(mappedName="ejb/User")
	private User user;
	
	@EJB(mappedName = "ejb/Category")
	private Category category;
	
	@EJB(mappedName="ejb/Color")
	private Color color;
	
	@EJB(mappedName="ejb/BrandManager")
	private BrandManager brandManager;
	
	@EJB(mappedName = "ejb/Vehicle")
	private Vehicle vehicle;
	
	// Attributes ------------- //
	
	private List<TbDeviceCustomization> cusList;
	
	private Long cusId;
	
	private String confirmMessage;
	
	private String modalMessage;
	
	private boolean showModal;
	
	private boolean showMessage;
	
	private boolean showData;
	
	private boolean showEditArea;
	
	private TbDeviceCustomization cus;
	
	private TbVehicle vehicleObject;
	
	private boolean showVehicle;
	
	private String filingNumber;
	
	private Date filingDate;
	
	private Date corReceivedDate;
	
	/* --- Device Type : Special Or Exempt --- */
	
	private Long idType;
	
	private List<SelectItem> typeList;
	
	/* --- Special or Exempt type --- */
	
	private Long especialExemptId;
	
	private List<SelectItem> especialExemptList;
	
	/* --- Exempt Office ---- */
	
	private Long exemptOfficeId;
	
	private List<SelectItem> exemptOfficeList;
	
	private boolean exempt;
	
	private boolean special;
	
	/* --  Client -- */
	
	private Long idClientAccount;
	
	private List<SelectItem> clientNames;
	
	private boolean showClient;
	
	private TbSystemUser client;
	
	/* --Category --*/
	
	private Long idCategory;
	
	private List<SelectItem> categories;
	
	/* -- Department -- */
	
	private Long departmentId;
	
	private List<SelectItem> departments;
	
	/* -- Stations -- */
	
	private boolean showAddStation;
	
	private Long departmentIdA;
	
	private List<SelectItem> departmentsA;
	
	private Long stationIdA;
	
	private List<SelectItem> stationsA;
	
	private List<TbStationBO> stationList;
	
	private boolean showStations;
	
	private Long idStationToDelete;
	
	/* Others */
	
	private String officialDocumentNumber;
	
	private Date officialDocuementDate;
	
	private String payOption;
	
	private List<SelectItem> payOptions;
	
	private String typedOption;
	
	private List<SelectItem> typedOptions;
	
	private Long surplusValue;
	
	private boolean surplus;
	
	private String observation;
	
	/* Confirmation ----- */
	
	private boolean confirmationShow;
	
	private boolean confirmationSaveShow;
	
	/* -- New -- vehicle */
	
	private boolean showAddVehicle;
	
	private String vehiclePlate;
	
	private String vehicleChassis;
	
	private String vehicleInternalCode;
	
	private Long colorId;
	
	private List<SelectItem> colorsList;
	
	private Long brandId;
	
	private List<SelectItem> brandList;
	
	private Long categoryId;
	
	private List<SelectItem> categoryList;
	
	private TbVehicle vehicleTemp;
	
	/* -- Search -- */
	
	private boolean showSearchVehicle;
	
	
	private boolean confirmationChangeShow;
	
	
	/**
	 * Constructor.
	 */
	public FixMaster() {
	}
	
	// Actions -------------- //
		
	/**
	 * Init Edition.
	 */
	public String initEdit() {
		this.cus = null;
		for (TbDeviceCustomization d : this.getCusList()) {
			if (cusId.longValue() == d.getDeviceCustomizationId().longValue()) {
				this.cus = d;
				break;
			}
		}
		if (this.cus != null) {
			setShowEditArea(true);
			setShowVehicle(true);
			vehicleObject = cus.getTbVehicle();
			filingDate = cus.getFilingDate();
			filingNumber = cus.getFilingNumber();
			corReceivedDate = cus.getCorReceivedDate();
			idType = cus.getTbSpecialExemptType().getTbDeviceType().getDeviceTypeId();
			especialExemptId = cus.getTbSpecialExemptType().getSpecialExemptTypeId();
			stationList = new ArrayList<TbStationBO>();
			
			if(cus.getTbSpecialExemptOffice() != null) {
				exempt = true;
				special = false;
				setExemptOfficeId(cus.getTbSpecialExemptOffice().getSpecialExemptOfficeId());
				departmentId = cus.getTbDepartment().getDepartmentId();
			} else {
				exempt = false;
				special = true;
				stationList = master.getSpecialStation(cus.getDeviceCustomizationId());
				showStations = true;
			}
			
			client = cus.getTbAccount().getTbSystemUser();
			idClientAccount = cus.getTbAccount().getAccountId();
			showClient = true;
			
			
			for(TbCategory c : category.getCategories()){
				if(c.getCategoryCode().equals(cus.getCategoryConcession()) &&
						c.getCategoryInviasCode().equals(cus.getCategoryInvias())){
					idCategory = c.getCategoryId();
					break;
				}
			}
			
			officialDocumentNumber = cus.getOfficialDocumentNumber();
			officialDocuementDate = cus.getOfficialDocuemntDate();
			payOption = cus.getPay();
			
			typedOption = cus.getTyped();
			
			if(cus.getSurplusValue() != null ){
				surplus = true;
				surplusValue = cus.getSurplusValue();
			} else {
				surplus = false;
				surplusValue = null;
			}
			
			if(cus.getObservation()!= null) {
				observation = cus.getObservation();
			} else {
				observation = null;
			}
			
		} else {
			setShowEditArea(false);
		}
		return null;
	}
	
	/**
	 * Init creation of vehicle.
	 */
	public String initCreateVehicle(){
		setShowAddVehicle(true);
		setConfirmationShow(false);
		setShowSearchVehicle(false);
		setShowAddStation(false);
		setVehiclePlate(null);
		setVehicleChassis(null);
		setVehicleInternalCode(null);
		setConfirmMessage(null);
		return null;
	}
	
	/**
	 * Shows Modal Panel For confirmation. 
	 */
	public String initConfirm() {
		setShowAddVehicle(false);
		setConfirmationShow(true);
		setConfirmMessage("¿Está Seguro de Registrar un vehículo con Placa: " + vehiclePlate.toUpperCase()  + "?");
		return null;
	}
	
	/**
	 * Creates a vehicle.
	 */
	public String createVehicle() {
		setConfirmationShow(false);
		Long result = vehicle.createVehicle(SessionUtil.sessionUser().getUserId(), SessionUtil.ip(),  vehiclePlate,
				vehicleChassis, vehicleInternalCode, colorId, brandId, categoryId,
				null,null,null,null,null,null,
				user.getSystemMasterById(SessionUtil.sessionUser().getUserId()));
		if (result == 0L) {
			setShowModal(true);
			setModalMessage("Error al crear el vehículo.");
		} else if (result == -1L) {
			setShowModal(true);
			setModalMessage("Ya Existe un Vehículo registrado con la placa: " + vehiclePlate.toUpperCase() + ".");
		} else {
			vehicleObject = vehicle.getVehicle(result);
			setShowVehicle(true);
		}
		return null;
	}
	
	/**
	 * Init Searches a vehicle by plate.
	 */
	public String initSearchVehicle(){
		setShowSearchVehicle(true);
		setShowAddVehicle(false);
		setShowAddStation(false);
		vehiclePlate = null;
		return null;
	}
	
	/**
	 * Searches a vehicle By Plate.
	 */
	public String searchVehicle() { 
		setShowSearchVehicle(false);
		vehicleTemp = vehicle.getVehicleByPlate(vehiclePlate);
		
		if (vehicleTemp != null) {
			String result =  master.validatePlate(vehicleTemp.getVehiclePlate());
			if (result != null) {
				setModalMessage(result);
				setShowModal(true);
			} else {
				setConfirmationChangeShow(true);
				setConfirmMessage("¿Está Seguro de Reemplazar La Placa Actual: " + vehicleObject.getVehiclePlate().toUpperCase()
						+ " por la Placa: " + vehicleTemp.getVehiclePlate().toUpperCase() + "?");
			}
		} else {
			setShowModal(true);
			setModalMessage("No se ha encontrado un vehículo con la placa: " + vehiclePlate.toUpperCase() + ".");
		}
		return null;
	}
	
	/**
	 * Replace Vehicle Object
	 */
	public String changeVehicle() {
		setConfirmationChangeShow(false);
		vehicleObject = vehicleTemp;
		return null;
	}
	
	/**
	 * Clears Data.
	 */
	public String clearData(){
		setVehicleObject(null);
		setShowVehicle(false);
		setFilingDate(null);
		setFilingNumber(null);
		setCorReceivedDate(null);
		setIdType(null);
		setIdClientAccount(-1L);
		setShowClient(false);
		setIdCategory(-1L);
		setStationList(null);
		setObservation(null);
		setOfficialDocuementDate(null);
		setOfficialDocumentNumber(null);
		return null;
	}
	
	
	/**
	 * Method that hides the modal window
	 */
	public String hideModal() {
		this.setModalMessage("");
		setShowModal(false);
		setShowAddVehicle(false);
		setShowSearchVehicle(false);
		setShowAddStation(false);
		setConfirmMessage(null);
		setConfirmationShow(false);
		setConfirmationSaveShow(false);
		setConfirmationChangeShow(false);
		return null;
	}
	
	/**
	 * Change the data of client.
	 */
	public String changeClient(){
		if (idClientAccount.longValue() == -1) {
			showClient = false;
		} else {
			for (TbAccount u : user.getClientAccount(null)) {
				if (idClientAccount.longValue() == u.getAccountId()) {
					this.client = u.getTbSystemUser();
					showClient = true;
					break;
				}
			}
		}
		return null;
	}
	
	/**
	 * Init add station
	 */
	public String initAddStation(){
		setShowAddStation(true);
		return null;
	}
	
	/**
	 * Removes an Station.
	 */
	public String removeStation(){
		for(int i = 0; i < this.getStationList().size() ; i ++){
			if (this.getStationList().get(i).getStationBOId() == idStationToDelete
					.longValue()) {
				this.getStationList().remove(i);
				break;
			}
		}
		if (this.getStationList().size() > 0) {
			showStations = true;
		} else {
			showStations = false;
		}
		return  null;
	}
	
	/**
	 * Adds a station to station array if special.
	 */
	public String addStation(){
		setShowAddStation(false);
		if(stationIdA != null){
			for(TbStationBO ts :this.getStationList()){
				if(ts.getStationBOId() == stationIdA.longValue()){
					return null;
				}
			}
			for (TbStationBO ts : master.getStationsByDepartment(departmentIdA)) {
				if (ts.getStationBOId() == stationIdA.longValue()) {
					stationList.add(ts);
					break;
				}
			}
		}
		if (this.getStationList().size() > 0) {
			showStations = true;
		} else {
			showStations = false;
		}
		return null;
	}
	
	/**
	 * Shows confirmation before saving.
	 */
	public String initSave() {
		setConfirmationSaveShow(true);
		setConfirmationShow(false);
		setConfirmMessage("¿Está Seguro que desea guardar los cambios a la personalización para la placa: " + vehicleObject.getVehiclePlate().toUpperCase()  +"?");
		return null;
	} 
	
	
	/**
	 * Saves the new master or replacement.
	 */
	public String save() {
		setModalMessage("");
		setConfirmationSaveShow(false);
			if (filingNumber != null && filingNumber.trim().length() > 0) {
				if (filingDate != null) {
					if (corReceivedDate != null) {
						if(especialExemptId.longValue() != -1L){
							if ( (idType.longValue() == DeviceType.EXEMPT.getId().longValue() && exemptOfficeId != -1) 
									|| idType.longValue() != DeviceType.EXEMPT.getId().longValue()) {
								if (idClientAccount != null && idClientAccount.longValue() != -1L) {
									if (idCategory != null && idCategory != -1L) {
										
										if (idType.longValue() == DeviceType.EXEMPT.getId().longValue() 
												&& departmentId != null && departmentId.longValue() == -1L) {
											setModalMessage("Debe Seleccionar una Regional Válida.");
											setShowModal(true);
											return null;
										}
										if (idType.longValue() != DeviceType.EXEMPT.getId().longValue()
												&& this.getStationList() != null && this.getStationList().size() <= 0) {
											setModalMessage("Debe Añadir Por lo menos un Peaje.");
											setShowModal(true);
											return null;
										}
										if (officialDocumentNumber != null && officialDocumentNumber.trim().length() > 0) {
											if(officialDocuementDate!=null) { 
												if (payOption.equals("EXCEDENTE") && surplusValue == null){
													setModalMessage("Debe digitar un Valor Excedente Válido.");
													setShowModal(true);
													return null;
												}
												
												//Fix.
												if (master.saveFixMaster(cusId, SessionUtil.sessionUser().getUserId(),
														SessionUtil.ip(), idType, filingNumber, filingDate,
														corReceivedDate,idCategory, departmentId,
														officialDocumentNumber,officialDocuementDate,
														payOption, typedOption,surplusValue, observation,
														idClientAccount, vehicleObject.getVehicleId(),
														exemptOfficeId,stationList, especialExemptId)) {
													setModalMessage("Se ha Modificado la Información Exitosamente.");
													setShowEditArea(false);
													setCusList(null);
													clearData();
												} else {
													setModalMessage("Ha Ocurrido un Error al Guardar los datos.");
												}
											} else {
												setModalMessage("Seleccione la Fecha del Oficio.");
											}
										} else {
											setModalMessage("Digite un Número de Oficio válido.");
										}
									} else {
										setModalMessage("Debe Seleccionar la Categoría.");
									}
								} else {
									setModalMessage("Debe Seleccionar una Cuenta de Cliente.");
								}
							} else {
								setModalMessage("Debe Seleccionar la Depedencia.");
							}
						} else {
							setModalMessage("Debe Seleccionar un Tipo de Exento/Especial/Carreteable.");
						}
					} else {
						setModalMessage("Seleccione la Fecha de Recibido COR.");
					}
				} else {
					setModalMessage("Seleccione la Fecha de Radicación.");
				}
			} else {
				setModalMessage("Digite un número de Radicación Válido.");
			}
		
		setShowModal(true);
		return null;
	}
	
	// Getters and Setters ------------ //

	/**
	 * @param cusList the cusList to set
	 */
	public void setCusList(List<TbDeviceCustomization> cusList) {
		this.cusList = cusList;
	}

	/**
	 * @return the cusList
	 */
	public List<TbDeviceCustomization> getCusList() {
		if (cusList == null) {
			cusList = new ArrayList<TbDeviceCustomization>();
			cusList = master.getDeviceCustomizationByStateAndOperationType(
					CustomizationState.REJECTED.getId(), OperationType.NEW.getId());
		}
		return cusList;
	}

	/**
	 * @param cusId the cusId to set
	 */
	public void setCusId(Long cusId) {
		this.cusId = cusId;
	}

	/**
	 * @return the cusId
	 */
	public Long getCusId() {
		return cusId;
	}

	/**
	 * @param confirmMessage the confirmMessage to set
	 */
	public void setConfirmMessage(String confirmMessage) {
		this.confirmMessage = confirmMessage;
	}

	/**
	 * @return the confirmMessage
	 */
	public String getConfirmMessage() {
		return confirmMessage;
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
	 * @param showMessage the showMessage to set
	 */
	public void setShowMessage(boolean showMessage) {
		this.showMessage = showMessage;
	}

	/**
	 * @return the showMessage
	 */
	public boolean isShowMessage() {
		if(this.getCusList().size() > 0) {
			setShowMessage(false);
		} else {
			setShowMessage(true);
		}
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
		if(cusList.size() > 0) {
			setShowData(true);
		} else {
			setShowData(false);
		}
		return showData;
	}

	/**
	 * @param showEditArea the showEditArea to set
	 */
	public void setShowEditArea(boolean showEditArea) {
		this.showEditArea = showEditArea;
	}

	/**
	 * @return the showEditArea
	 */
	public boolean isShowEditArea() {
		return showEditArea;
	}

	/**
	 * @param cus the cus to set
	 */
	public void setCus(TbDeviceCustomization cus) {
		this.cus = cus;
	}

	/**
	 * @return the cus
	 */
	public TbDeviceCustomization getCus() {
		return cus;
	}

	/**
	 * @param vehicleObject the vehicleObject to set
	 */
	public void setVehicleObject(TbVehicle vehicleObject) {
		this.vehicleObject = vehicleObject;
	}

	/**
	 * @return the vehicleObject
	 */
	public TbVehicle getVehicleObject() {
		return vehicleObject;
	}

	/**
	 * @param showVehicle the showVehicle to set
	 */
	public void setShowVehicle(boolean showVehicle) {
		this.showVehicle = showVehicle;
	}

	/**
	 * @return the showVehicle
	 */
	public boolean isShowVehicle() {
		return showVehicle;
	}

	/**
	 * @param filingNumber the filingNumber to set
	 */
	public void setFilingNumber(String filingNumber) {
		this.filingNumber = filingNumber;
	}

	/**
	 * @return the filingNumber
	 */
	public String getFilingNumber() {
		return filingNumber;
	}

	/**
	 * @param filingDate the filingDate to set
	 */
	public void setFilingDate(Date filingDate) {
		this.filingDate = filingDate;
	}

	/**
	 * @return the filingDate
	 */
	public Date getFilingDate() {
		return filingDate;
	}

	/**
	 * @param corReceivedDate the corReceivedDate to set
	 */
	public void setCorReceivedDate(Date corReceivedDate) {
		this.corReceivedDate = corReceivedDate;
	}

	/**
	 * @return the corReceivedDate
	 */
	public Date getCorReceivedDate() {
		return corReceivedDate;
	}

	/**
	 * @param idType the idType to set
	 */
	public void setIdType(Long idType) {
		this.idType = idType;
	}

	/**
	 * @return the idType
	 */
	public Long getIdType() {
		return idType;
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
			for (TbDeviceType dt : list) {
					typeList.add(new SelectItem(dt.getDeviceTypeId(), dt
							.getDeviceTypeName()));
			}
		}
		return typeList;
	}

	/**
	 * @param especialExemptId the especialExemptId to set
	 */
	public void setEspecialExemptId(Long especialExemptId) {
		this.especialExemptId = especialExemptId;
	}

	/**
	 * @return the especialExemptId
	 */
	public Long getEspecialExemptId() {
		return especialExemptId;
	}

	/**
	 * @param especialExemptList the especialExemptList to set
	 */
	public void setEspecialExemptList(List<SelectItem> especialExemptList) {
		this.especialExemptList = especialExemptList;
	}

	/**
	 * @return the especialExemptList
	 */
	public List<SelectItem> getEspecialExemptList() {
		if(especialExemptList == null){
			especialExemptList = new ArrayList<SelectItem>();
		} else {
			especialExemptList.clear();
		}
		especialExemptList.add(new SelectItem(-1L, "--Seleccione Un tipo--"));
		for (TbSpecialExemptType ee : master.getSpecialExemptTypes(idType)) {
			especialExemptList.add(new SelectItem(ee.getSpecialExemptTypeId(),
					ee.getSpecialExemptTypeName()));
		}
		return especialExemptList;
	}

	/**
	 * @param exempt the exempt to set
	 */
	public void setExempt(boolean exempt) {
		this.exempt = exempt;
	}

	/**
	 * @return the exempt
	 */
	public boolean isExempt() {
		if (idType != null && idType.longValue() == DeviceType.EXEMPT.getId().longValue()) {
			exempt = true;
		} else {
			exempt = false;
		}
		return exempt;
	}

	/**
	 * @param exemptOfficeId the exemptOfficeId to set
	 */
	public void setExemptOfficeId(Long exemptOfficeId) {
		this.exemptOfficeId = exemptOfficeId;
	}

	/**
	 * @return the exemptOfficeId
	 */
	public Long getExemptOfficeId() {
		return exemptOfficeId;
	}

	/**
	 * @param exemptOfficeList the exemptOfficeList to set
	 */
	public void setExemptOfficeList(List<SelectItem> exemptOfficeList) {
		this.exemptOfficeList = exemptOfficeList;
	}

	/**
	 * @return the exemptOfficeList
	 */
	public List<SelectItem> getExemptOfficeList() {
		if(exemptOfficeList == null){
			exemptOfficeList = new ArrayList<SelectItem>();
		} else {
			exemptOfficeList.clear();
		}
		exemptOfficeList.add(new SelectItem(-1L, "--Seleccione una Dependencia--"));
		for (TbSpecialExemptOffice eo : master.getSpecialExemptOffice(especialExemptId)) {
			exemptOfficeList.add(new SelectItem(eo.getSpecialExemptOfficeId(),
					eo.getOfficeName()));
		}
		return exemptOfficeList;
	}

	/**
	 * @param showClient the showClient to set
	 */
	public void setShowClient(boolean showClient) {
		this.showClient = showClient;
	}

	/**
	 * @return the showClient
	 */
	public boolean isShowClient() {
		return showClient;
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
			
			clientNames.add(new SelectItem(-1, " --Seleccione el Cliente --"));
			for(TbAccount tu : user.getClientAccount(null)){
				String name = tu.getTbSystemUser().getUserNames();
				if(tu.getTbSystemUser().getTbCodeType().getCodeTypeId().longValue() != 3){
					name = name + " " + tu.getTbSystemUser().getUserSecondNames();
				}
				clientNames.add(new SelectItem(tu.getAccountId(), name + " - No. Cuenta: " + tu.getAccountId()));
			}
		}		
		return clientNames;
	}

	/**
	 * @param idCategory the idCategory to set
	 */
	public void setIdCategory(Long idCategory) {
		this.idCategory = idCategory;
	}

	/**
	 * @return the idCategory
	 */
	public Long getIdCategory() {
		return idCategory;
	}

	/**
	 * @param categories the categories to set
	 */
	public void setCategories(List<SelectItem> categories) {
		this.categories = categories;
	}

	/**
	 * @return the categories
	 */
	public List<SelectItem> getCategories() {
		if(categories == null){
			categories = new ArrayList<SelectItem>();
			categories.add(new SelectItem(-1L, "--Seleccione la Categoría--"));
			for (TbCategory c : category.getCategories()) {
				categories.add(new SelectItem(c.getCategoryId(), "Categoría Concesión " + c.getCategoryCode() + " - Categoría INVIAS " + c.getCategoryInviasCode()));
			}
		}
		return categories;
	}

	/**
	 * @param special the special to set
	 */
	public void setSpecial(boolean special) {
		this.special = special;
	}

	/**
	 * @return the special
	 */
	public boolean isSpecial() {
		if (idType != null && (idType.longValue() ==DeviceType.SPECIAL.getId().longValue()|| 
				idType.longValue() == DeviceType.CARRETEABLE.getId().longValue()) ){
			special = true;
		} else {
			special = false;
		}
		return special;
	}

	/**
	 * @param departmentId the departmentId to set
	 */
	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	/**
	 * @return the departmentId
	 */
	public Long getDepartmentId() {
		return departmentId;
	}

	/**
	 * @param departments the departments to set
	 */
	public void setDepartments(List<SelectItem> departments) {
		this.departments = departments;
	}

	/**
	 * @return the departments
	 */
	public List<SelectItem> getDepartments() {
		if(departments == null){
			departments = new ArrayList<SelectItem>();
			departments.add(new SelectItem(-1L, "--Seleccione Una--"));
			for (TbDepartment d : master.getDepartments()) {
				departments.add(new SelectItem(d.getDepartmentId(), d.getDepartmentName()));
			}
		}
		return departments;
	}

	/**
	 * @param showAddStation the showAddStation to set
	 */
	public void setShowAddStation(boolean showAddStation) {
		this.showAddStation = showAddStation;
	}

	/**
	 * @return the showAddStation
	 */
	public boolean isShowAddStation() {
		return showAddStation;
	}

	/**
	 * @param departmentIdA the departmentIdA to set
	 */
	public void setDepartmentIdA(Long departmentIdA) {
		this.departmentIdA = departmentIdA;
	}

	/**
	 * @return the departmentIdA
	 */
	public Long getDepartmentIdA() {
		return departmentIdA;
	}

	/**
	 * @param departmentsA the departmentsA to set
	 */
	public void setDepartmentsA(List<SelectItem> departmentsA) {
		this.departmentsA = departmentsA;
	}

	/**
	 * @return the departmentsA
	 */
	public List<SelectItem> getDepartmentsA() {
		if(departmentsA == null){
			departmentsA = new ArrayList<SelectItem>();
			departmentsA.add(new SelectItem(-1L, "--Seleccione Una--"));
			for (TbDepartment d : master.getDepartments()) {
				departmentsA.add(new SelectItem(d.getDepartmentId(), d.getDepartmentName()));
			}
		}
		return departmentsA;
	}

	/**
	 * @param stationIdA the stationIdA to set
	 */
	public void setStationIdA(Long stationIdA) {
		this.stationIdA = stationIdA;
	}

	/**
	 * @return the stationIdA
	 */
	public Long getStationIdA() {
		return stationIdA;
	}

	/**
	 * @param stationsA the stationsA to set
	 */
	public void setStationsA(List<SelectItem> stationsA) {
		this.stationsA = stationsA;
	}

	/**
	 * @return the stationsA
	 */
	public List<SelectItem> getStationsA() {
		if(stationsA==null){
			stationsA = new ArrayList<SelectItem>();
		}else{
			stationsA.clear();
		}
		if (departmentIdA != null) {
			for (TbStationBO s : master.getStationsByDepartment(departmentIdA)) {
				stationsA.add(new SelectItem(s.getStationBOId(), s.getStationBOName()));
			}
		}
		return stationsA;
	}

	/**
	 * @param stationList the stationList to set
	 */
	public void setStationList(List<TbStationBO> stationList) {
		this.stationList = stationList;
	}

	/**
	 * @return the stationList
	 */
	public List<TbStationBO> getStationList() {
		return stationList;
	}

	/**
	 * @param showStations the showStations to set
	 */
	public void setShowStations(boolean showStations) {
		this.showStations = showStations;
	}

	/**
	 * @return the showStations
	 */
	public boolean isShowStations() {
		return showStations;
	}

	/**
	 * @param idStationToDelete the idStationToDelete to set
	 */
	public void setIdStationToDelete(Long idStationToDelete) {
		this.idStationToDelete = idStationToDelete;
	}

	/**
	 * @return the idStationToDelete
	 */
	public Long getIdStationToDelete() {
		return idStationToDelete;
	}

	/**
	 * @param officialDocumentNumber the officialDocumentNumber to set
	 */
	public void setOfficialDocumentNumber(String officialDocumentNumber) {
		this.officialDocumentNumber = officialDocumentNumber;
	}

	/**
	 * @return the officialDocumentNumber
	 */
	public String getOfficialDocumentNumber() {
		return officialDocumentNumber;
	}

	/**
	 * @param officialDocuementDate the officialDocuementDate to set
	 */
	public void setOfficialDocuementDate(Date officialDocuementDate) {
		this.officialDocuementDate = officialDocuementDate;
	}

	/**
	 * @return the officialDocuementDate
	 */
	public Date getOfficialDocuementDate() {
		return officialDocuementDate;
	}

	/**
	 * @param payOption the payOption to set
	 */
	public void setPayOption(String payOption) {
		this.payOption = payOption;
	}

	/**
	 * @return the payOption
	 */
	public String getPayOption() {
		return payOption;
	}

	/**
	 * @param payOptions the payOptions to set
	 */
	public void setPayOptions(List<SelectItem> payOptions) {
		this.payOptions = payOptions;
	}

	/**
	 * @return the payOptions
	 */
	public List<SelectItem> getPayOptions() {
		if(payOptions == null){
			payOptions = new ArrayList<SelectItem>();
			payOptions.add(new SelectItem("NO","NO"));
			payOptions.add(new SelectItem("SI","SI"));
			payOptions.add(new SelectItem("EXCEDENTE","EXCEDENTE"));
		}
		return payOptions;
	}

	/**
	 * @param typedOption the typedOption to set
	 */
	public void setTypedOption(String typedOption) {
		this.typedOption = typedOption;
	}

	/**
	 * @return the typedOption
	 */
	public String getTypedOption() {
		return typedOption;
	}

	/**
	 * @param typedOptions the typedOptions to set
	 */
	public void setTypedOptions(List<SelectItem> typedOptions) {
		this.typedOptions = typedOptions;
	}

	/**
	 * @return the typedOptions
	 */
	public List<SelectItem> getTypedOptions() {
		if(typedOptions == null){
			typedOptions = new ArrayList<SelectItem>();
			typedOptions.add(new SelectItem("NO","NO"));
			typedOptions.add(new SelectItem("SI","SI"));
		}
		return typedOptions;
	}

	/**
	 * @param surplusValue the surplusValue to set
	 */
	public void setSurplusValue(Long surplusValue) {
		this.surplusValue = surplusValue;
	}

	/**
	 * @return the surplusValue
	 */
	public Long getSurplusValue() {
		return surplusValue;
	}

	/**
	 * @param surplus the surplus to set
	 */
	public void setSurplus(boolean surplus) {
		this.surplus = surplus;
	}

	/**
	 * @return the surplus
	 */
	public boolean isSurplus() {
		if (payOption != null && payOption.equals("EXCEDENTE")) {
			surplus = true;
		} else {
			surplus = false;
		}
		return surplus;
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
	 * @param confirmationShow the confirmationShow to set
	 */
	public void setConfirmationShow(boolean confirmationShow) {
		this.confirmationShow = confirmationShow;
	}

	/**
	 * @return the confirmationShow
	 */
	public boolean isConfirmationShow() {
		return confirmationShow;
	}

	/**
	 * @param confirmationSaveShow the confirmationSaveShow to set
	 */
	public void setConfirmationSaveShow(boolean confirmationSaveShow) {
		this.confirmationSaveShow = confirmationSaveShow;
	}

	/**
	 * @return the confirmationSaveShow
	 */
	public boolean isConfirmationSaveShow() {
		return confirmationSaveShow;
	}

	/**
	 * @param showAddVehicle the showAddVehicle to set
	 */
	public void setShowAddVehicle(boolean showAddVehicle) {
		this.showAddVehicle = showAddVehicle;
	}

	/**
	 * @return the showAddVehicle
	 */
	public boolean isShowAddVehicle() {
		return showAddVehicle;
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
	 * @param vehicleChassis the vehicleChassis to set
	 */
	public void setVehicleChassis(String vehicleChassis) {
		this.vehicleChassis = vehicleChassis;
	}

	/**
	 * @return the vehicleChassis
	 */
	public String getVehicleChassis() {
		return vehicleChassis;
	}

	/**
	 * @param vehicleInternalCode the vehicleInternalCode to set
	 */
	public void setVehicleInternalCode(String vehicleInternalCode) {
		this.vehicleInternalCode = vehicleInternalCode;
	}

	/**
	 * @return the vehicleInternalCode
	 */
	public String getVehicleInternalCode() {
		return vehicleInternalCode;
	}

	/**
	 * @param colorId the colorId to set
	 */
	public void setColorId(Long colorId) {
		this.colorId = colorId;
	}

	/**
	 * @return the colorId
	 */
	public Long getColorId() {
		return colorId;
	}

	/**
	 * @param colorsList the colorsList to set
	 */
	public void setColorsList(List<SelectItem> colorsList) {
		this.colorsList = colorsList;
	}

	/**
	 * @return the colorsList
	 */
	public List<SelectItem> getColorsList() {
		if(colorsList == null){
			colorsList = new ArrayList<SelectItem>();
			for (TbColor c : color.getColors()) {
				colorsList.add(new SelectItem(c.getColorId(), c.getColorName()));
			}
		}
		return colorsList;
	}

	/**
	 * @param brandId the brandId to set
	 */
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	/**
	 * @return the brandId
	 */
	public Long getBrandId() {
		return brandId;
	}

	/**
	 * @param brandList the brandList to set
	 */
	public void setBrandList(List<SelectItem> brandList) {
		this.brandList = brandList;
	}

	/**
	 * @return the brandList
	 */
	public List<SelectItem> getBrandList() {
		if(brandList == null){
			brandList = new ArrayList<SelectItem>();
			for (TbBrand b : brandManager.getBrands()) {
				brandList.add(new SelectItem(b.getBrandId(), b.getBrandName()));
			}
		}
		return brandList;
	}

	/**
	 * @param categoryId the categoryId to set
	 */
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	/**
	 * @return the categoryId
	 */
	public Long getCategoryId() {
		return categoryId;
	}

	/**
	 * @param categoryList the categoryList to set
	 */
	public void setCategoryList(List<SelectItem> categoryList) {
		this.categoryList = categoryList;
	}

	/**
	 * @return the categoryList
	 */
	public List<SelectItem> getCategoryList() {
		if(categoryList == null){
			categoryList = new ArrayList<SelectItem>();
			for (TbCategory c : category.getCategories()) {
				categoryList.add(new SelectItem(c.getCategoryId(), c.getCategoryName()));
			}
		}
		return categoryList;
	}

	/**
	 * @param showSearchVehicle the showSearchVehicle to set
	 */
	public void setShowSearchVehicle(boolean showSearchVehicle) {
		this.showSearchVehicle = showSearchVehicle;
	}

	/**
	 * @return the showSearchVehicle
	 */
	public boolean isShowSearchVehicle() {
		return showSearchVehicle;
	}

	/**
	 * @param vehicleTemp the vehicleTemp to set
	 */
	public void setVehicleTemp(TbVehicle vehicleTemp) {
		this.vehicleTemp = vehicleTemp;
	}

	/**
	 * @return the vehicleTemp
	 */
	public TbVehicle getVehicleTemp() {
		return vehicleTemp;
	}

	/**
	 * @param confirmationChangeShow the confirmationChangeShow to set
	 */
	public void setConfirmationChangeShow(boolean confirmationChangeShow) {
		this.confirmationChangeShow = confirmationChangeShow;
	}

	/**
	 * @return the confirmationChangeShow
	 */
	public boolean isConfirmationChangeShow() {
		return confirmationChangeShow;
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
}