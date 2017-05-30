/**
 * 
 */
package process.device.perti;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import constant.CustomizationState;

import sessionVar.SessionUtil;

import ejb.Master;

import jpa.TbDeviceCustomization;
import jpa.TbDeviceType;
import jpa.TbOperationType;
import jpa.TbVehicle;

/**
 * Bean To manage Authorize Master.
 * @author tmolina
 *
 */
public class AuthorizeVipsa implements Serializable {
	private static final long serialVersionUID = -7408851373217138347L;
	
	@EJB(mappedName = "ejb/Master")
	private Master master;
	
	// Attributes ------------- //
	
	private List<TbDeviceCustomization> cusList;
	
	private Long cusId;
	
	private boolean confirmReject;
	
	private String confirmMessage;
	
	private String modalMessage;
	
	private boolean showModal;
	
	private boolean confirm;
	
	private TbVehicle vehicle;
	
	private boolean office;
	
	private boolean property;
	
	private boolean consig;
	
	private String plate;
	
	private String chassis;
	
	private boolean showMessage;
	
	private boolean showData;
	
	/* --- Operation Type: New Or Reposition --- */
	
	private List<SelectItem> operationTypes;
	
	private Long operationTypeId;
	
	/* --- Device Type : Special Or Exempt --- */
	
	private Long idType;
	
	private List<SelectItem> typeList;

	/**
	 * Constructor.
	 */
	public AuthorizeVipsa() {
		setIdType(-1L);
	}
	
	// Helpers -------- //
	
	/**
	 * Initialize fields.
	 */
	public void init() {
		setCusList(null);
		setOffice(false);
	    setProperty(false);
		setConsig(false);
		setShowData(false);
		setShowMessage(false);
		setPlate(null);
		setChassis(null);
		setTypeList(null);
		setOperationTypes(null);
		setIdType(-1L);
	}
	
	// Actions -------------- //
	
	/**
	 * Init Confirmation. 
	 */
	public String initConfirm(){
		setConfirm(true);
		setConfirmReject(false);
		for(TbDeviceCustomization cus : cusList){
			if(cus.getDeviceCustomizationId().longValue() == cusId.longValue()){
				vehicle = cus.getTbVehicle();
				break;
			}
		}
		return null;
	}	
	
	/**
	 * Init Rejection. 
	 */
	public String initReject(){
		setConfirmReject(true);
		setConfirm(false);
		setConfirmMessage("¿Esta seguro de rechazar esta transacción?");
		return null;
	}
	
	/**
	 * 
	 * Rejects the transaction.
	 */
	public String reject(){
		setConfirmReject(false);
		if (master.rejectMaster(cusId, SessionUtil.ip(), SessionUtil
				.sessionUser().getUserId())) {
			setModalMessage("Transacción Exitosa");
			setCusList(null);
		} else {
			setModalMessage("Error en Transacción. Comuníquese con el Administrador.");
		}
		setShowModal(true);
		return null;
	}
	
	/**
	 * Confirm transaction.
	 */
	public String confirmT(){
		setConfirm(false);
		if(office && property){
			if (master.confirmMaster(cusId, SessionUtil.ip(), SessionUtil
					.sessionUser().getUserId(), office, property, consig)) {
				setModalMessage("Transacción Exitosa.");
				setCusList(null);
				setOffice(false);
				setProperty(false);
				setConsig(false);
			} else {
				setModalMessage("Error en Transacción.");
			}
		}else{
			setModalMessage("Los Datos No están Completos. Por favor verifíquelos e intente de nuevo.");
		}
		setShowModal(true);
		return null;
	}
	
	/**
	 * Searches by plate.
	 */
	public String searchByPlate(){
			cusList = master.getDeviceCustomization(CustomizationState.NEW.getId(),
				plate, chassis, idType, operationTypeId);
		if (cusList.size() <= 0) {
				setCusList(null);
				setModalMessage("No se encontraron datos para los parámetros seleccionados.");
				setShowModal(true);
			}
		return null;
	}
	
	public String listAll(){
		setCusList(null);
		return null;
	}
	
	/**
	 * Hides Modal
	 */
	public String hideModal(){
		setConfirmReject(false);
		setConfirmMessage("");
		setModalMessage("");
		setShowModal(false);
		setConfirm(false);
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
		if(cusList == null){
			cusList = new ArrayList<TbDeviceCustomization>();
			cusList = master.getDeviceCustomizationByState(CustomizationState.NEW.getId());
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
	 * @param confirmReject the confirmReject to set
	 */
	public void setConfirmReject(boolean confirmReject) {
		this.confirmReject = confirmReject;
	}

	/**
	 * @return the confirmReject
	 */
	public boolean isConfirmReject() {
		return confirmReject;
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
	 * @param confirm the confirm to set
	 */
	public void setConfirm(boolean confirm) {
		this.confirm = confirm;
	}

	/**
	 * @return the confirm
	 */
	public boolean isConfirm() {
		return confirm;
	}

	/**
	 * @param vehicle the vehicle to set
	 */
	public void setVehicle(TbVehicle vehicle) {
		this.vehicle = vehicle;
	}

	/**
	 * @return the vehicle
	 */
	public TbVehicle getVehicle() {
		return vehicle;
	}

	/**
	 * @param office the office to set
	 */
	public void setOffice(boolean office) {
		this.office = office;
	}

	/**
	 * @return the office
	 */
	public boolean isOffice() {
		return office;
	}

	/**
	 * @param property the property to set
	 */
	public void setProperty(boolean property) {
		this.property = property;
	}

	/**
	 * @return the property
	 */
	public boolean isProperty() {
		return property;
	}

	/**
	 * @param consig the consig to set
	 */
	public void setConsig(boolean consig) {
		this.consig = consig;
	}

	/**
	 * @return the consig
	 */
	public boolean isConsig() {
		return consig;
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
	 * @param operationTypes the operationTypes to set
	 */
	public void setOperationTypes(List<SelectItem> operationTypes) {
		this.operationTypes = operationTypes;
	}

	/**
	 * @return the operationTypes
	 */
	public List<SelectItem> getOperationTypes() {
		if(operationTypes == null){
			operationTypes = new ArrayList<SelectItem>();
			List<TbOperationType> list = master.getPertiOperationTypes();
			if (list.size() > 0) {
				setOperationTypeId(list.get(0).getOperationTypeId());
			}
			for (TbOperationType ot : list) {
				operationTypes.add(new SelectItem(ot.getOperationTypeId(), ot
						.getOperationTypeName()));
			}
		}
		return operationTypes;
	}

	/**
	 * @param operationTypeId the operationTypeId to set
	 */
	public void setOperationTypeId(Long operationTypeId) {
		this.operationTypeId = operationTypeId;
	}

	/**
	 * @return the operationTypeId
	 */
	public Long getOperationTypeId() {
		return operationTypeId;
	}

	/**
	 * @param chassis the chassis to set
	 */
	public void setChassis(String chassis) {
		this.chassis = chassis;
	}

	/**
	 * @return the chassis
	 */
	public String getChassis() {
		return chassis;
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
			typeList.add(new SelectItem(-1L, "Todas"));
			List<TbDeviceType> list = master.getPertiDeviceTypes();
			for (TbDeviceType dt : list) {
					typeList.add(new SelectItem(dt.getDeviceTypeId(), dt
							.getDeviceTypeName()));
			}
		}
		return typeList;
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
}