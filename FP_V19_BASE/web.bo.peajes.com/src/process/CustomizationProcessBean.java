/**
 * 
 */
package process;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import ejb.Vehicle;

import jpa.TbProcessTrackDetail;
import jpa.TbVehicle;

/**
 * Bean to manage Device Process Track.
 * @author tmolina
 *
 */
public class CustomizationProcessBean implements Serializable {
	private static final long serialVersionUID = 1758966582439674378L;

	@EJB(mappedName="ejb/Process")
	private ejb.Process process;
	
	@EJB(mappedName = "ejb/Vehicle")
	private Vehicle vehicleEJB;
	
	// Attributes ------------- //
	
	private List<TbProcessTrackDetail> details;
	
	private String plate;
	
	private TbVehicle vehicle;
	
	// Control Visibility --------------- //

	private String message;
	
	private boolean showMessage;
	
	private boolean showData;

	/**
	 * Constructor.
	 */
	public CustomizationProcessBean() {
	}
	
	// Actions --------------- //
	
	/**
	 * Searches process track associated with a plate.
	 */
	public String search() {
			if (plate != null && !plate.equals("")) {
				vehicle = vehicleEJB.getVehicleByPlate(plate);
				if(details == null){
					details = new ArrayList<TbProcessTrackDetail>();
				}else{
					details.clear();
				}
				
				details = process.getCustomizationProcessDetailByPlate(plate);
				
				if (details.size() <= 0) {
					setShowData(false);
					setShowMessage(true);
					setMessage("No se Encontraron Procesos de Personalización relacionados a la placa " + plate + ".");
				} else {
						setShowData(true);
						setShowMessage(false);
				}
			} else{
				setShowData(false);
				setShowMessage(true);
				setMessage("Digite La Placa Por Favor.");
			}
		return null;
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
	 * @return the plate
	 */
	public String getPlate() {
		return plate;
	}

	/**
	 * @param plate the plate to set
	 */
	public void setPlate(String plate) {
		this.plate = plate;
	}

}