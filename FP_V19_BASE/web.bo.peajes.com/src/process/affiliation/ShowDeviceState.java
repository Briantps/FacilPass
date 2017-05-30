package process.affiliation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import ejb.Device;

import jpa.TbDevice;

public class ShowDeviceState implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@EJB(mappedName = "ejb/Device")
	private Device deviceEJB;

	private Long stateId;
	
	private List<SelectItem> states;
	
	private boolean showData;
	
	private boolean showModal;
	
	private String modalMessage;
	
	private List<TbDevice> deviceList;

	private String device="";
	
	public ShowDeviceState(){
		super();
		this.setShowData(false);
		this.setDevice("");
		this.setStateId(-1L);
	}
	
	/**
	 * @param stateId the stateId to set
	 */
	public void setStateId(Long stateId) {
		this.stateId = stateId;
	}

	/**
	 * @return the stateId
	 */
	public Long getStateId() {
		return stateId;
	}

	/**
	 * @param states the states to set
	 */
	public void setStates(List<SelectItem> states) {
		this.states = states;
	}

	/**
	 * @return the states
	 */
	public List<SelectItem> getStates() {
		states= new ArrayList<SelectItem>();
		states.add(new SelectItem(-1L, "Todos"));
		states.add(new SelectItem(6L, "Activo"));
		states.add(new SelectItem(9L, "Lista Negra"));
		states.add(new SelectItem(7L, "Desactivado"));
		states.add(new SelectItem(4L, "Saldo Bajo"));
		states.add(new SelectItem(3L, "Sin Saldo"));
		return states;
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
	 * @param deviceList the deviceList to set
	 */
	public void setDeviceList(List<TbDevice> deviceList) {
		this.deviceList = deviceList;
	}

	/**
	 * @return the deviceList
	 */
	public List<TbDevice> getDeviceList() {
		return deviceList;
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
	
	public String hideModal(){
		this.setShowData(false);
		this.setModalMessage("");
		this.setShowModal(false);
		this.setDevice("");
		return null;
	}
	
	public String search(){
		deviceList=deviceEJB.getDevicesByState(stateId,device);
		if(deviceList!=null){
			if(deviceList.size()>0){
				this.setShowModal(false);
				this.setShowData(true);
			}
			else{
				this.setShowData(false);
				this.setModalMessage("No se encontraron resultados para la consulta");
				this.setShowModal(true);
			}
		}
		else{
			this.setShowData(false);
			this.setModalMessage("No se encontraron resultados para la consulta");
			this.setShowModal(true);
		}
		return null;
	}

	/**
	 * @param device the device to set
	 */
	public void setDevice(String device) {
		this.device = device;
	}

	/**
	 * @return the device
	 */
	public String getDevice() {
		return device;
	}
	
	public void ocult(){
		this.setShowData(false);
	}
	
	public void ocult1(){
		this.setDevice("");
		this.setShowData(false);
	}
}
