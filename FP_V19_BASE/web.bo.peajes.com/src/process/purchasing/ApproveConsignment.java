/**
 * 
 */
package process.purchasing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import constant.ConsignmentState;

import sessionVar.SessionUtil;

import ejb.Purchase;

import jpa.TbConsignment;

/**
 * Bean To manage the approbation  of consignment. 
 * @author tmolina
 *
 */
public class ApproveConsignment implements Serializable {
	private static final long serialVersionUID = 7113442670702923215L;
	
	@EJB(mappedName="ejb/Purchase")
	private Purchase purchase;
	
	// Attributes ------------- //
	
	private List<TbConsignment> conList;
	
	private Long idConsig;
	
	// Modal Control -------- //
	
	private boolean showModal;
	
	private String modalMessage;
	
	private boolean confirmationShow;
	
	private boolean confirmationShowR;
	
	private String corfirmMessage;
	
	private boolean showData;

	/**
	 * Constructor 
	 */
	public ApproveConsignment() {
	}
	
	// Actions ---------------- //
	
	/**
	 * Hides Modal.
	 */
	public String hideModal(){
		setShowModal(false);
		setModalMessage("");
		setConfirmationShow(false);
		setConfirmationShowR(false);
		return null;
	}
	
	/**
	 * Shows confirmation. 
	 */
	public String initApprove(){
		setConfirmationShow(true);
		setConfirmationShowR(false);
		setCorfirmMessage("¿Esta seguro de realizar esta transacción?");
		return null;
	}
	
	/**
	 * Shows confirmation. 
	 */
	public String initReject(){
		setConfirmationShow(false);
		setConfirmationShowR(true);
		setCorfirmMessage("¿Esta seguro de realizar esta transacción?");
		return null;
	}
	
	/**
	 * Approbation of consignment. 
	 */
	public String approve(){
		setConfirmationShow(false);
		setConfirmationShowR(false);
		System.out.println("Idconsig: "+idConsig);
		System.out.println("IP: "+SessionUtil.ip());
		System.out.println("Usuario: "+SessionUtil
				.sessionUser().getUserId());
		
		if (purchase.approve(idConsig, SessionUtil.ip(), SessionUtil
				.sessionUser().getUserId())) {
			setModalMessage("Transacción Exitosa.");
		} else {
			setModalMessage("Error en Transacción.");
		}
		setShowModal(true);
		return null;
	}
	
	/**
	 * Rejection of consignment. 
	 */
	public String reject(){
		setConfirmationShowR(false);
		setConfirmationShow(false);
		if (purchase.reject(idConsig, SessionUtil.ip(), SessionUtil
				.sessionUser().getUserId())) {
			setModalMessage("Transacción Exitosa.");
		} else {
			setModalMessage("Error en Transacción.");
		}
		setShowModal(true);
		return null;
	}
	
	
	// Getters and Setters -------- //

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
	 * @param confirmationShow the confirmationShow to set
	 */
	public void setConfirmationShow(boolean confirmationShow) {
		this.confirmationShow = confirmationShow;
	}

	/**
	 * @return the confirmationShow
	 */
	public boolean isConfirmationShow() {
		return confirmationShow;
	}

	/**
	 * @param corfirmMessage the corfirmMessage to set
	 */
	public void setCorfirmMessage(String corfirmMessage) {
		this.corfirmMessage = corfirmMessage;
	}

	/**
	 * @return the corfirmMessage
	 */
	public String getCorfirmMessage() {
		return corfirmMessage;
	}

	/**
	 * @param conList the conList to set
	 */
	public void setConList(List<TbConsignment> conList) {
		this.conList = conList;
	}

	/**
	 * @return the conList
	 */
	public List<TbConsignment> getConList() {
		conList = new ArrayList<TbConsignment>();
		conList = purchase.getConsignmentByState(ConsignmentState.NEW);
		return conList;
	}

	/**
	 * @param idConsig the idConsig to set
	 */
	public void setIdConsig(Long idConsig) {
		this.idConsig = idConsig;
	}

	/**
	 * @return the idConsig
	 */
	public Long getIdConsig() {
		return idConsig;
	}

	/**
	 * @param confirmationShowR the confirmationShowR to set
	 */
	public void setConfirmationShowR(boolean confirmationShowR) {
		this.confirmationShowR = confirmationShowR;
	}

	/**
	 * @return the confirmationShowR
	 */
	public boolean isConfirmationShowR() {
		return confirmationShowR;
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
		if(this.getConList().size() > 0) {
			showData = true;
		} else {
			showData = false;
		}
		return showData;
	}
}