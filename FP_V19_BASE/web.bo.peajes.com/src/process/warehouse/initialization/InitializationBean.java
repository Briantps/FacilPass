/**
 * 
 */
package process.warehouse.initialization;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import constant.DeviceState;
import constant.InclusionState;

import jpa.TbDevice;
import jpa.TbInclusion;
import jpa.TbPoint;

import sessionVar.SessionUtil;
import util.device.ServiceCommand;
import util.perti.InclusionDetail;

import ejb.Master;
import ejb.Warehouse;
import ejb.crud.Point;


/**
 * Bean to manage Initialization. 
 * @author tmolina
 *
 */
public class InitializationBean implements Serializable {
	private static final long serialVersionUID = 7852740138143137862L;
	
	@EJB(mappedName = "ejb/Master")
	private Master master;
	
	@EJB(mappedName="ejb/Point")
	private Point point;
	
	@EJB(mappedName="ejb/Device")
	private ejb.Device deviceEJB;
	
	@EJB(mappedName="ejb/Warehouse")
	private Warehouse warehouse;

	// Attributes --------------- //
	
	private List<TbInclusion> inclusionList;
	
	private List<InclusionDetail> details;
	
	private boolean showPanel;
	
	private Long inclusionId;
	
	private TbInclusion inclusion;
	
	private boolean showDetail;
	
	private Long inclusionDetailId;
	
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
	public InitializationBean() {
	}
	
	public void init(){
		setDetails(null);
		setInclusionList(null);
		setDetails(null);
		setShowDetail(false);
		hideModal();
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
	public String initIni() {
		for(TbInclusion i : this.getInclusionList()){
			if(i.getInclusionId().longValue() == inclusionId.longValue()){
				this.inclusion = i;
				break;
			}
		}
		details = master.getInclusionDetails(inclusionId);
		setShowDetail(true);
		return null;
	}
	
	/**
	 * 
	 */
	public String initAssociateId() {
		
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
									.longValue() == DeviceState.WAREHOUSE.getId().longValue()) {
								
								String facial = "";
								for(InclusionDetail id: this.getDetails()){
									if (id.getDetail().getInclusionDetailId()
											.longValue() == inclusionDetailId.longValue()) {
										facial = id.getDetail().getDeviceFacialId();
									}
								}
								
								// Confirmation window
								setConfirmModal(true);
								setModalMessage("¿Confirma que desea Asociar el Id " + idDevice + " al facial " + facial + "?");
							} else {
								setModalMessage("El Dispositivo con ID Interno : " + idDevice + ", no tiene el estado indicado para hacer la inicialización," +
										" el cual es 'En Almacén', su estado actual es: "+ device.getTbDeviceState().getDeviceStateDescription()+ ". Verifique.");
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
	public String associateId(){
		hideModal();
		if (warehouse.saveInitialitazion(device.getDeviceId(),
				inclusionDetailId, SessionUtil.ip(), SessionUtil.sessionUser().getUserId())) {
			init();
			details = master.getInclusionDetails(inclusionId);
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
	 * @param details the details to set
	 */
	public void setDetails(List<InclusionDetail> details) {
		this.details = details;
	}

	/**
	 * @return the details
	 */
	public List<InclusionDetail> getDetails() {
		return details;
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
		if (this.getInclusionList().size() > 0) {
			showPanel = true;
		} else {
			showPanel = false;
		}
		return showPanel;
	}

	/**
	 * @param inclusionList the inclusionList to set
	 */
	public void setInclusionList(List<TbInclusion> inclusionList) {
		this.inclusionList = inclusionList;
	}

	/**
	 * @return the inclusionList
	 */
	public List<TbInclusion> getInclusionList() {
		if (inclusionList == null) {
			inclusionList = new ArrayList<TbInclusion>();
			inclusionList = master.getInclusionsByState(InclusionState.SENT);
		}
		return inclusionList;
	}

	/**
	 * @param inclusionId the inclusionId to set
	 */
	public void setInclusionId(Long inclusionId) {
		this.inclusionId = inclusionId;
	}

	/**
	 * @return the inclusionId
	 */
	public Long getInclusionId() {
		return inclusionId;
	}

	/**
	 * @param inclusion the inclusion to set
	 */
	public void setInclusion(TbInclusion inclusion) {
		this.inclusion = inclusion;
	}

	/**
	 * @return the inclusion
	 */
	public TbInclusion getInclusion() {
		return inclusion;
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
	 * @param inclusionDetailId the inclusionDetailId to set
	 */
	public void setInclusionDetailId(Long inclusionDetailId) {
		this.inclusionDetailId = inclusionDetailId;
	}

	/**
	 * @return the inclusionDetailId
	 */
	public Long getInclusionDetailId() {
		return inclusionDetailId;
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
}