/**
 * 
 */
package process.device.perti;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import constant.CustomizationState;
import constant.DeviceState;

import jpa.TbDevice;
import jpa.TbDeviceCustomization;
import jpa.TbOperationType;
import jpa.TbPoint;

import sessionVar.SessionUtil;
import util.device.ServiceCommand;
import util.perti.Personalization;

import ejb.Master;
import ejb.crud.Point;


/**
 * Bean to manage Personalization. 
 * @author tmolina
 *
 */
public class PersonalizationBean implements Serializable {
	private static final long serialVersionUID = 7852740138143137862L;
	
	@EJB(mappedName = "ejb/Master")
	private Master master;
	
	@EJB(mappedName="ejb/Point")
	private Point point;
	
	@EJB(mappedName="ejb/Device")
	private ejb.Device deviceEJB;

	// Attributes --------------- //
	
	private List<Personalization> cusList;
	
	private Long cusId;
	
	/* --- Operation Type: New Or Reposition --- */
	
	private List<SelectItem> operationTypes;
	
	private Long operationTypeId;
	
	private boolean showPanel;
	
	private boolean showDetail;
	
	private boolean showTable;
	
	/* -- Modal --*/
	
	private boolean modal;
	
	private boolean confirmModal;
	
	private String modalMessage;
	
	/* Card reading */
	
	private ServiceCommand command;
	
	private String idDevice;
	
	private TbPoint tbPoint;
	
	private TbDevice device;
	
	// Constructor ---------- //
	
	/**
	 * Constructor.
	 */
	public PersonalizationBean() {
	}
	
	@PostConstruct
	public void initC(){
		init();
	}
	
	public void init(){
		setCusList(null);
		setOperationTypes(null);
		hideModal();
		setOperationTypeId(-1L);
		
		if (cusList == null) {
			cusList = new ArrayList<Personalization>();
		} else {
			cusList.clear();
		}
		cusList = master.getSpecialDeviceCustomization(
				CustomizationState.APPROVED, operationTypeId);

		if (cusList.size() > 0) {
			showPanel = true;
			showTable = true;
		} else {
			showPanel = false;
		}
	}
	
	// Actions ----------- //

	/**
	 * Controls visibility of modal panel.
	 */
	public String hideModal(){
		setModal(false);
		setModalMessage(null);
		setConfirmModal(false);
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public String changeOperation() {
		cusList = master.getSpecialDeviceCustomization(
				CustomizationState.APPROVED, operationTypeId);

		if (cusList.size() > 0) {
			showTable = true;
		} else {
			showTable = false;
		}
		return null;
	}
	
	/**
	 * 
	 */
	public String initPerso() {
		
		// first: searching point of transaction associated with user session
		tbPoint = point.getUserPoint(SessionUtil.sessionUser().getUserId(),
				SessionUtil.ip());
		
		if (tbPoint != null) {
			// Then: read the card.
			String response;
			command = new ServiceCommand(tbPoint.getPointIp(), tbPoint
					.getPointPort().toString());
			try {
				response = command.sendCommand("1");
				if (response != null && !response.equals("")) {
					if (response.contains("*")) {
						// Error reading
						idDevice = null;
						setModalMessage(processRequest(response));
						setModal(true);
					} else {
						String cad[] = response.split(";");
						idDevice = cad [0];
						
						// Searching the device in data base
						device = deviceEJB.getDeviceByCode(idDevice);
						if(device!= null){
							
							// checking if the state of device is correct to be initialized
							if (device.getTbDeviceState().getDeviceStateId()
									.longValue() == DeviceState.INITIALIZED.getId().longValue()) {
								
								//checking if internal id corresponds with data base data
								TbDeviceCustomization c = null;
								for (Personalization p : this.getCusList()) {
									if (cusId.longValue() == p.getCus()
											.getDeviceCustomizationId()
											.longValue()) {
										c = p.getCus();
									}
								}
								
								device = c.getTbDevice();
								
								if (device.getDeviceCode().equals(idDevice)) {

									// Confirmation window
									setConfirmModal(true);
									setModalMessage("¿Está seguro de Personalizar la tarjeta de ID " + idDevice + " con el facial " + c.getTbDevice().getDeviceFacialId()
											+ ", la placa: " + c.getTbVehicle().getVehiclePlate()+ " y la categoría: " +
											c.getCategoryConcession()+ "?");
								} else {
									setModalMessage("La tarjeta en el lector no corresponde a la tarjeta que está relacionada al registro seleccionado. Verifique.");
									setModal(true);
								}
								
							} else {
								setModalMessage("El Dispositivo con ID Interno : " + idDevice + ", no tiene el estado indicado para hacer la pasonalización," +
										" el cual es 'Dispositivo Inicializado', su estado actual es: "+ device.getTbDeviceState().getDeviceStateDescription()+ ". Verifique.");
								setModal(true);
							}
						} else {
							setModalMessage("El Dispositivo con ID Interno : " + idDevice + ", no ha sido registrado en el sitema, veifique.");
							setModal(true);
						}
					}
				} else {
					setModalMessage("Asegurese que el lector esté conectado y el servicio esté activo.");
					setModal(true);
				}
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				command.sendCommand("0");
			}
			
		} else {
			setModal(true);
			setModalMessage("El usuario en Sesión no posee permisos desde la direción actual para hacer transacciones.");
		}
		
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public String personalization(){
		hideModal();
		if (master.savePersonalization(cusId, SessionUtil.ip(), SessionUtil
				.sessionUser().getUserId())) {
			init();
			setModalMessage("Transacción Exitosa.");
		} else {
			setModalMessage("Error en Transacción.");
		}
		setModal(true);
		return null;
	}
	
	// Helpers methods 
	
	/**
	 * 
	 */
	private String processRequest(String response) {
		if (response.contains("TIMEOUT")) {
			return  "No se ha detectado tarjeta en el lector.";
		} else if (response.contains("TRANSACTIONLOCKED")) {
			return  "No es posible realizar lectura de la tarjeta.";
		} else if (response.contains("ERR")) {
			return  "No es posible realizar la lectura.";
		} else if (response.contains("SALDOMAX")) {
			return "No es posible realizar la recarga porque excede el valor máximo de recarga.";
		} else if (response.contains("NOACCESS")) {
			return"No es posible leer la Tarjeta.";
		} else if (response.contains("VENCIDA")) {
			return  "La tarjeta se encuentra vencida.";
		} else {
			return  "No es posible realizar la lectura por favor verifique el Lector.";
		}
	}

	
	// Getters and Setters ------------- //

	/**
	 * @param modal the modal to set
	 */
	public void setModal(boolean modal) {
		this.modal = modal;
	}

	/**
	 * @return the modal
	 */
	public boolean isModal() {
		return modal;
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
	 * @param showPanel the showPanel to set
	 */
	public void setShowPanel(boolean showPanel) {
		this.showPanel = showPanel;
	}

	/**
	 * @return the showPanel
	 */
	public boolean isShowPanel() {
		return showPanel;
	}

	/**
	 * @param showDetail the showDetail to set
	 */
	public void setShowDetail(boolean showDetail) {
		this.showDetail = showDetail;
	}

	/**
	 * @return the showDetail
	 */
	public boolean isShowDetail() {
		return showDetail;
	}

	/**
	 * @param idDevice the idDevice to set
	 */
	public void setIdDevice(String idDevice) {
		this.idDevice = idDevice;
	}

	/**
	 * @return the idDevice
	 */
	public String getIdDevice() {
		return idDevice;
	}

	/**
	 * @param confirmModal the confirmModal to set
	 */
	public void setConfirmModal(boolean confirmModal) {
		this.confirmModal = confirmModal;
	}

	/**
	 * @return the confirmModal
	 */
	public boolean isConfirmModal() {
		return confirmModal;
	}

	/**
	 * @param cusList the cusList to set
	 */
	public void setCusList(List<Personalization> cusList) {
		this.cusList = cusList;
	}

	/**
	 * @return the cusList
	 */
	public List<Personalization> getCusList() {
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
	 * @param operationTypes the operationTypes to set
	 */
	public void setOperationTypes(List<SelectItem> operationTypes) {
		this.operationTypes = operationTypes;
	}

	/**
	 * @return the operationTypes
	 */
	public List<SelectItem> getOperationTypes() {
		if (operationTypes == null) {
			operationTypes = new ArrayList<SelectItem>();
			operationTypes.add(new SelectItem(-1L, "Todas"));
			setOperationTypeId(-1L);
			for (TbOperationType ot : master.getPertiOperationTypes()) {
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
	 * @param showTable the showTable to set
	 */
	public void setShowTable(boolean showTable) {
		this.showTable = showTable;
	}

	/**
	 * @return the showTable
	 */
	public boolean isShowTable() {
		return showTable;
	}
}