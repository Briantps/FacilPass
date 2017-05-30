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
public class SendInclusionBean implements Serializable {
	private static final long serialVersionUID = -2686109427283793505L;

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
	
	// Constructor ---------- //
	
	/**
	 * Constructor.
	 */
	public SendInclusionBean() {
	}
	
	/**
	 * Init
	 */
	public void init() {
		setInclusionList(null);
		hideModal();
	}
	
	// Actions ----------- //

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
		} else {
			setModalMessage("Error en Transacción.");
		}
		return null;
	}
	
	/**
	 * Controls visibility of modal panel.
	 */
	public String hideModal(){
		setModal(false);
		setModalMessage(null);
		setDetail(false);
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
}