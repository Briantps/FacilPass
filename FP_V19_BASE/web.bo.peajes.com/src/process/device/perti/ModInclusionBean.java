/**
 * 
 */
package process.device.perti;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import constant.InclusionState;

import sessionVar.SessionUtil;
import util.perti.InclusionDetail;

import jpa.TbInclusion;

import ejb.Master;

/**
 * Bean to manage Inclusions. 
 * @author tmolina
 *
 */
public class ModInclusionBean implements Serializable {
	private static final long serialVersionUID = -903682478609948610L;

	@EJB(mappedName = "ejb/Master")
	private Master master;

	// Attributes --------------- //
	
	private List<TbInclusion> inclusionList;
	
	private Long inclusionId;
	
	/* -- Modal --*/
	
	private boolean modal;
	
	private String modalMessage;
	
	private boolean detail;
	
	private List<InclusionDetail> details;
	
	private boolean showPanel;
	
	private boolean removeWindow;
	
	private boolean addWindow;
	
	private List<InclusionDetail> detailsToAdd;
	
	private boolean noDataToInclude;
	
	// Constructor ---------- //
	
	/**
	 * Constructor.
	 */
	public ModInclusionBean() {
	}
	
	/**
	 * Init
	 */
	public void init() {
		setInclusionList(null);
		hideModal();
		noDataToInclude = false;
	}
	
	// Actions ----------- //
	
	/**
	 * 
	 */
	public String initAddInclusion() {
		hideModal();
		setAddWindow(true);
		if(detailsToAdd == null) {
			detailsToAdd = new ArrayList<InclusionDetail>();
		} else {
			detailsToAdd.clear();
		}
		detailsToAdd = master.getDeviceCustomizationsToInclude();
		if (detailsToAdd.size() > 0) {
			noDataToInclude = false;
		} else {
			noDataToInclude = true;
		}
		return null;
	}
	
	/**
	 * 
	 */
	public String initDeleteInclusion() {
		hideModal();
		details = master.getInclusionDetails(inclusionId);
		setRemoveWindow(true);
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public String delete(){
		setRemoveWindow(false);
		boolean oneAtLeast = false;
		for (InclusionDetail i: details){
			if(i.isSelected()){
				oneAtLeast = true;
				break;
			}
		}
		if (oneAtLeast) {
			if (master.removeDetailInclusion(inclusionId, details, SessionUtil
					.ip(), SessionUtil.sessionUser().getUserId())) {
				setModalMessage("Transacción Exitosa.");
			} else {
				setModalMessage("Error en Transacción.");
			}
		} else {
			setModalMessage("La Inclusión debe por lo menos tener un registro seleccionado. Verifique.");
		}
		setModal(true);
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public String add() {
		setAddWindow(false);
		boolean oneAtLeast = false;
		for (InclusionDetail i: detailsToAdd){
			if(i.isSelected()){
				oneAtLeast = true;
				break;
			}
		}
		if (oneAtLeast) {
			if ( master.addDetailsToInclusion(inclusionId, detailsToAdd, SessionUtil.ip(),
					SessionUtil.sessionUser().getUserId())) {
				setModalMessage("Transacción Exitosa.");
				setInclusionList(null);
			} else {
				setModalMessage("Error en Transaccón");
			}
		} else {
			setModalMessage("Debe Seleccionar por lo menos un registro para ser añadido a la Inclusión.");
		}
		setModal(true);
		return null;
	}
	
	/**
	 * 
	 */
	public String initViewInclusion() {
		hideModal();
		details = master.getInclusionDetails(inclusionId);
		setDetail(true);
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public String sendInclusion() {
		setDetail(false);
		if (master.sendInclusion(inclusionId, SessionUtil.ip(), SessionUtil
				.sessionUser().getUserId())) {
			setModalMessage("Transacción Exitosa.");
			setInclusionList(null);
		} else {
			setModalMessage("Error en Transacción.");
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
		setDetail(false);
		setRemoveWindow(false);
		setAddWindow(false);
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
	 * @param inclusionList the inclusionList to set
	 */
	public void setInclusionList(List<TbInclusion> inclusionList) {
		this.inclusionList = inclusionList;
	}

	/**
	 * @return the inclusionList
	 */
	public List<TbInclusion> getInclusionList() {
		if(inclusionList == null){
			inclusionList = new ArrayList<TbInclusion>();
			inclusionList = master.getInclusionsByState(InclusionState.NEW);
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
	 * @param detail the detail to set
	 */
	public void setDetail(boolean detail) {
		this.detail = detail;
	}

	/**
	 * @return the detail
	 */
	public boolean isDetail() {
		return detail;
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
	 * @param removeWindow the removeWindow to set
	 */
	public void setRemoveWindow(boolean removeWindow) {
		this.removeWindow = removeWindow;
	}

	/**
	 * @return the removeWindow
	 */
	public boolean isRemoveWindow() {
		return removeWindow;
	}

	/**
	 * @param addWindow the addWindow to set
	 */
	public void setAddWindow(boolean addWindow) {
		this.addWindow = addWindow;
	}

	/**
	 * @return the addWindow
	 */
	public boolean isAddWindow() {
		return addWindow;
	}

	/**
	 * @param detailsToAdd the detailsToAdd to set
	 */
	public void setDetailsToAdd(List<InclusionDetail> detailsToAdd) {
		this.detailsToAdd = detailsToAdd;
	}

	/**
	 * @return the detailsToAdd
	 */
	public List<InclusionDetail> getDetailsToAdd() {
		return detailsToAdd;
	}

	/**
	 * @param noDataToInclude the noDataToInclude to set
	 */
	public void setNoDataToInclude(boolean noDataToInclude) {
		this.noDataToInclude = noDataToInclude;
	}

	/**
	 * @return the noDataToInclude
	 */
	public boolean isNoDataToInclude() {
		return noDataToInclude;
	}
}