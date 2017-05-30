/**
 * 
 */
package crud;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import sessionVar.SessionUtil;

import ejb.crud.InstallationLocation;

import jpa.TbInstallationLocation;

/**
 * Bean to Manage the administration of installation locations.
 * @author tmolina
 *
 */
public class InstallationLocationBean implements Serializable {
	private static final long serialVersionUID = -6898499325299460152L;
	
	@EJB(mappedName="ejb/InstallationLocation")
	private InstallationLocation location;
	
	// Attributes.
	
	private boolean showModal;
	
	private String modalMessage;
	
	private List<TbInstallationLocation> locationList;
	
	private Long locationId;
	
	private boolean showInsert;
	
	private String locationName;
	
	private boolean showEdit;

	/**
	 * Constructor.
	 */
	public InstallationLocationBean() {
	}
	
	// Actions ------------ //
	
	/**
	 * Init Add Installation Location.
	 */
	public String initAddLocation(){
		setShowInsert(true);
		setShowEdit(false);
		setLocationName(null);
		return null;
	}
	
	/**
	 * Inserts a new installation location.
	 */
	public String addLocation(){
		setShowInsert(false);
		if(locationName.equals(null) || locationName.equals("") || !locationName.matches(".*\\w.*"))
		{
			setModalMessage("El nombre de la Ubicaci�n de Instalaci�n es Requerido.");
		}
		else if(locationName.length() > 50)
		{
			setModalMessage("La longitud del nombre de la Ubicaci�n de Instalaci�n no es correcta. Recuerde que debe ser m�ximo 50 caracteres.");
		}
		else
		{
			Long result = location.insertLocation(locationName, SessionUtil.ip(), SessionUtil.sessionUser().getUserId());
			if (result != null) {
				if(result != -1L){
					setModalMessage("La ubicaci�n de instalaci�n ha sido creada con �xito.");
					setLocationList(null);
				} else {
					setModalMessage("Ya hay una ubicaci�n de Instalaci�n con ese nombre. Verifique.");
				}
			} else {
				setModalMessage("Error en Transacci�n.");
			}
		}
		setShowModal(true);
		return null;
	}
	
	/**
	 * Init Edition of location.
	 */
	public String initEditLocation(){
		setShowInsert(false);
		setShowEdit(true);
		for(TbInstallationLocation il : locationList){
			if(locationId.longValue() == il.getInstallationLocationId().longValue()){
				locationName = il.getInstallationLocationName();
				break;
			}
		}
		return null;
	}
	
	/**
	 * Edits an installation location.
	 */
	public String editLocation(){
		setShowEdit(false);
		setShowInsert(false);
		if(locationName.equals(null) || locationName.equals("") || !locationName.matches(".*\\w.*"))
		{
			setModalMessage("El nombre de la Ubicaci�n de Instalaci�n es Requerido.");
		}
		else if(locationName.length() > 50)
		{
			setModalMessage("La longitud del nombre de la Ubicaci�n de Instalaci�n no es correcta. Recuerde que debe ser m�ximo 50 caracteres.");
		}
		else
		{		
			if(locationName!=null){
				for(TbInstallationLocation il : locationList){
					if(locationId.longValue() != il.getInstallationLocationId().longValue()
							&& il.getInstallationLocationName().equals(locationName.toUpperCase())){
						setModalMessage("Ya hay una ubicaci�n de Instalaci�n con ese nombre, Verifique.");
						setShowModal(true);
						return null;
					}
				}
				
				if (location.editLocation(locationId, locationName, SessionUtil.ip(),
						SessionUtil.sessionUser().getUserId())) {
					setModalMessage("La ubicaci�n de instalaci�n ha sido actualizada con �xito.");
					setLocationList(null);
				} else {
					setModalMessage("Error en Transacci�n.");
				}				
			}
		}
		setShowModal(true);
		return null;
	}
	
	/**
	 * Hides modal windows.
	 */
	public String hideModal(){
		setShowInsert(false);
		setShowEdit(false);
		setShowModal(false);
		setModalMessage(null);
		return null;
	}
	
	// Getters and Setters ------------ //

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
	 * @param locationList the locationList to set
	 */
	public void setLocationList(List<TbInstallationLocation> locationList) {
		this.locationList = locationList;
	}

	/**
	 * @return the locationList
	 */
	public List<TbInstallationLocation> getLocationList() {
		if(locationList == null){
			locationList = new ArrayList<TbInstallationLocation>();
			locationList = location.getInstallationLocations();
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
	 * @param showInsert the showInsert to set
	 */
	public void setShowInsert(boolean showInsert) {
		this.showInsert = showInsert;
	}

	/**
	 * @return the showInsert
	 */
	public boolean isShowInsert() {
		return showInsert;
	}

	/**
	 * @param locationName the locationName to set
	 */
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	/**
	 * @return the locationName
	 */
	public String getLocationName() {
		return locationName;
	}

	/**
	 * @param showEdit the showEdit to set
	 */
	public void setShowEdit(boolean showEdit) {
		this.showEdit = showEdit;
	}

	/**
	 * @return the showEdit
	 */
	public boolean isShowEdit() {
		return showEdit;
	}
}