/**
 * 
 */
package process.device.perti;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;

import sessionVar.SessionUtil;
import util.perti.InclusionDetail;

import ejb.Master;


/**
 * Bean to manage Inclusions. 
 * @author tmolina
 *
 */
public class InclusionBean implements Serializable {
	private static final long serialVersionUID = 7852740138143137862L;
	
	@EJB(mappedName = "ejb/Master")
	private Master master;
	
	// Attributes --------------- //
	
	/* Inclusion*/
	
	private Date date;
	
	/* Inclusion Detail */
	
	private List<InclusionDetail> details;
	
	private boolean showPanel;
	
	/* -- Modal --*/
	
	private boolean modal;
	
	private String modalMessage;
	
	// Constructor ---------- //
	
	/**
	 * Constructor.
	 */
	public InclusionBean() {
	}
	
	public void init(){
		setDetails(null);
	}
	
	// Actions ----------- //
	
	/**
	 * Saves an inclusion.
	 */
	public String saveInclusion() {
		boolean oneAtLeast = false;
		for (InclusionDetail i: details){
			if(i.isSelected()){
				oneAtLeast = true;
				break;
			}
		}
		if (oneAtLeast) {
			Long response = master.saveInclusion(details, SessionUtil.ip(),
					SessionUtil.sessionUser().getUserId());
			if (response != null) {
				setModalMessage("Transacción Exitosa, el Número de Inclusión es: "
						+ response);
				setDetails(null);
			} else {
				setModalMessage("Error en Transaccón");
			}
		} else {
			setModalMessage("Debe Seleccionar por lo menos un registro para hacer la Inclusión.");
		}
		setModal(true);
		return null;
	}
	
	/**
	 * Controls visibility of modal panel.
	 */
	public String hideModal(){
		setModal(false);
		setModalMessage(null);
		return null;
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
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		date = new Date();
		return date;
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
		if (details == null) {
			details = new ArrayList<InclusionDetail>();
			details = master.getDeviceCustomizationsToInclude();
		}
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
		if (this.getDetails().size() > 0) {
			showPanel = true;
		} else {
			showPanel = false;
		}
		return showPanel;
	}
}