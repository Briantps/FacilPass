/**
 * 
 */
package process;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import constant.ProcessTrackType;


import jpa.TbDevice;
import jpa.TbDeviceType;
import jpa.TbProcessTrackDetail;

/**
 * Bean to manage Device Process Track.
 * @author tmolina
 *
 */
public class DeviceProcessBean implements Serializable {
	private static final long serialVersionUID = 139445617433342361L;

	@EJB(mappedName="ejb/Process")
	private ejb.Process process;
	
	@EJB(mappedName="ejb/Device")
	private ejb.Device deviceEJB;
	
	// Attributes ------------- //
	
	private List<SelectItem> devicesTypes;
	
	private Long typeDevId;
	
	/**
	 * List of devices with open/active process.
	 */
	private List<TbDevice> devicesList;
	
	private List<TbDevice> devicesList2;
	
	private List<TbProcessTrackDetail> details;
	
	private String facial;
	
	private Long deviceId;
	
	private List<SelectItem> facials;
	
	private TbDevice device;
	
	// Control Visibility --------------- //

	private String message;
	
	private boolean showMessage;
	
	private boolean showData;

	/**
	 * Constructor.
	 */
	public DeviceProcessBean() {
		devicesList = new ArrayList<TbDevice>();
		facials = new ArrayList<SelectItem>();
	}
	
	// Actions --------------- //
	
	/**
	 * 
	 * @return
	 */
	public String changeTypeDev() {
		showMessage = false;
		showData = false;
		facial = null;
		device = null;
		deviceId = -1L;
		facials.clear();
		facials.add(new SelectItem(-1,""));
		if (typeDevId == -2L) {
			setMessage("Debe Seleccionar Un Tipo de Dispositivo.");
			setShowData(false);
			setShowMessage(true);
		} else {
			devicesList = process.getDevicesWithProcessByType(typeDevId);	
			
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
	 * @return
	 */
	public String changeDevice(){
		if(deviceId == -1L){
			showMessage = false;
			showData = false;
			device = null;
			facial = null;
		} else {
			for(TbDevice d : devicesList){
				if(deviceId.longValue() ==  d.getDeviceId().longValue()){
					this.device = d;
					facial = d.getDeviceFacialId();
					details = process.getProcessDetails(d.getDeviceId(), ProcessTrackType.DEVICE);
					break;
				}
			}
			showData = true;
		}
		return null;
	}
	
	/**
	 * 
	 */
	public String search() {
		try{
			device = null;
			deviceId = -1L;
			if (typeDevId <0 || typeDevId==null) {
				setMessage("Error: Debe Seleccionar Un Tipo de Dispositivo.");
				setShowData(false);
				setShowMessage(true);
			} else{
				if(facial !=null && !facial.equals("")){
					devicesList2 = process.getDevicesWithProcessByType(typeDevId);	
					for(TbDevice d : devicesList2){
						if(d.getDeviceFacialId()!=null && d.getDeviceFacialId().equals(facial.toUpperCase())){
							device = d;
							deviceId = d.getDeviceId();
							details = process.getProcessDetails(d.getDeviceId(), ProcessTrackType.DEVICE);
							break;
						}
					}
					if (device == null) {
						setShowData(false);
						setShowMessage(true);
						setMessage("Error: No se encontró un proceso relacionado al dispositivo Consultado.");
					} else {
						setShowData(true);
						setShowMessage(false);
					}
				} else{
					setShowData(false);
					setShowMessage(true);
					setMessage("Error: Digite El Facial del Dispositivo.");
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	
	
	public void hideModal(){
		setShowData(false);
		setShowMessage(false);
		setMessage("");
		setFacial("");
	}
	
	// Getters and Setters ------ //

	/**
	 * @param details the details to set
	 */
	public void setDetails(List<TbProcessTrackDetail> details) {
		this.details = details;
	}

	/**
	 * @return the details
	 */
	public List<TbProcessTrackDetail> getDetails() {
		return details;
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
			devicesTypes.add(new SelectItem(-2,""));
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

	public void setDevicesList2(List<TbDevice> devicesList2) {
		this.devicesList2 = devicesList2;
	}

	public List<TbDevice> getDevicesList2() {
		return devicesList2;
	}
}