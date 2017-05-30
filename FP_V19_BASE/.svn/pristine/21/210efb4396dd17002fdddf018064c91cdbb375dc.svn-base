/**
 * 
 */
package process.warehouse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import sessionVar.SessionUtil;
import util.PurchaseOrder;

import ejb.Purchase;

/**
 * Bean to Manage the purchase oreder's confirmation by warehouse.
 * @author tmolina
 *
 */
public class ConfirmOrder implements Serializable {
	private static final long serialVersionUID = 2696811400434230268L;
	
	@EJB(mappedName="ejb/Purchase")
	private Purchase purchase;
	
	// Attributes --------------- // 
	
	private List<PurchaseOrder> orders;
	
	private Long idOrder;
	
	private PurchaseOrder po;
	
	// Control Modal ---------- //
	
	private boolean showModal;
	
	private String confirmMessage;
	
	private boolean confirmationShow;
	
	private String modalMessage;
	
	private boolean consigShow;
	
	private boolean detailShow;

	/**
	 * Constructor.
	 */
	public ConfirmOrder() {
	}
	
	// Actions ------------ //
	
	/**
	 * Hides the modal. 
	 */
	public String hideModal(){
		setShowModal(false);
		setConfirmationShow(false);
		setConfirmMessage("");
		setModalMessage("");
		setConsigShow(false);
		setDetailShow(false);
		return null;
	}
	
	/**
	 * Confirmation  of purchase order's approbation
	 */
	public String initConfirm(){
		setConfirmationShow(true);
		setConfirmMessage("¿Esta seguro de realizar la transacción?");
		getSelectedOrder();
		return null;
	}
	
	/**
	 * Approbation of purchase order.
	 */
	public String confirm(){
		setConfirmationShow(false);
		if (purchase.confirmOrder(idOrder, SessionUtil.sessionUser()
				.getUserId(), SessionUtil.ip())) {
			setModalMessage("Transacción Exitosa.");
		} else {
			setModalMessage("Error en Transacción");
		}
		setShowModal(true);
		return null;
	}
	
	/**
	 * Init show consignment. 
	 */
	public String iniShowConsig(){
		setConsigShow(true);
		getSelectedOrder();
		return null;
	}
	
	/**
	 * Init show order details.
	 */
	public String initShowDetail(){
		setDetailShow(true);
		getSelectedOrder();
		return null;
	}
	
	/**
	 * Gets the selected purchase order.
	 */
	private void getSelectedOrder(){
		for(PurchaseOrder p : this.orders){
			if(p.getOrder().getPurchaseOrderId().longValue() == idOrder.longValue()){
				this.po = p;
				break;
			}
		}
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
	 * @param orders the orders to set
	 */
	public void setOrders(List<PurchaseOrder> orders) {
		this.orders = orders;
	}

	/**
	 * @return the orders
	 */
	public List<PurchaseOrder> getOrders() {
		orders = new ArrayList<PurchaseOrder>();
		//orders = purchase.getPurchaseOrdersByState(2L); // Approved by treasury
		return orders;
	}

	/**
	 * @param idOrder the idOrder to set
	 */
	public void setIdOrder(Long idOrder) {
		this.idOrder = idOrder;
	}

	/**
	 * @return the idOrder
	 */
	public Long getIdOrder() {
		return idOrder;
	}

	/**
	 * @param confirmMessage the confirmMessage to set
	 */
	public void setConfirmMessage(String confirmMessage) {
		this.confirmMessage = confirmMessage;
	}

	/**
	 * @return the confirmMessage
	 */
	public String getConfirmMessage() {
		return confirmMessage;
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
	 * @param po the po to set
	 */
	public void setPo(PurchaseOrder po) {
		this.po = po;
	}

	/**
	 * @return the po
	 */
	public PurchaseOrder getPo() {
		return po;
	}

	/**
	 * @param consigShow the consigShow to set
	 */
	public void setConsigShow(boolean consigShow) {
		this.consigShow = consigShow;
	}

	/**
	 * @return the consigShow
	 */
	public boolean isConsigShow() {
		return consigShow;
	}

	/**
	 * @param detailShow the detailShow to set
	 */
	public void setDetailShow(boolean detailShow) {
		this.detailShow = detailShow;
	}

	/**
	 * @return the detailShow
	 */
	public boolean isDetailShow() {
		return detailShow;
	}
}