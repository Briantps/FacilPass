/**
 * 
 */
package arecatis;

import java.io.Serializable;

import javax.ejb.EJB;

import constant.PurchaseOrderState;

import util.PurchaseOrder;

import ejb.Purchase;

/**
 * Bean to manage the consult by user of one purchase oder.
 * @author tmolina
 * 
 */
public class ConsultOrder implements Serializable {
	private static final long serialVersionUID = -4376650841572850349L;
	
	@EJB(mappedName="ejb/Purchase")
	private Purchase purchase;
	
	// Attributes ------------------ //
	
	private Long orderNumber;
	
	private PurchaseOrder order;
	
	// Control Visibility ----------- //
	
	private boolean showData;
	
	private boolean showMessage;
	
	private boolean consigShow;
	
	private boolean detailShow;

	/**
	 * Constructor.
	 */
	public ConsultOrder() {
	}
	
	// Actions ------------ //
	
	/**
	 * Hides the modal. 
	 */
	public String hideModal(){
		setConsigShow(false);
		setDetailShow(false);
		return null;
	}
	
	public String search(){
		order = purchase.getPurchaseOrderByState(orderNumber, PurchaseOrderState.NEW, false);
		if (order != null) {
			setShowData(true);
			setShowMessage(false);
		} else {
			setShowData(false);
			setShowMessage(true);
		}
		return null;
	}
	
	/**
	 * Init show consignment. 
	 */
	public String iniShowAccount(){
		setConsigShow(true);
		return null;
	}
	
	/**
	 * Init show order details.
	 */
	public String initShowDetail(){
		setDetailShow(true);
		return null;
	}
	
	/*
	 * Print the PDF.
	 */
	public String printPdf() {
		System.out.println("[INFO] Enters print PDF.");
		// Printing the oder to a pdf.
    	OrderPDF pdf = new OrderPDF(order);
    	pdf.execute();   	
		return null;
	}
	
	// Getters and Setters  ------------- //

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

	/**
	 * @param orderNumber the orderNumber to set
	 */
	public void setOrderNumber(Long orderNumber) {
		this.orderNumber = orderNumber;
	}

	/**
	 * @return the orderNumber
	 */
	public Long getOrderNumber() {
		return orderNumber;
	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(PurchaseOrder order) {
		this.order = order;
	}

	/**
	 * @return the order
	 */
	public PurchaseOrder getOrder() {
		return order;
	}
}
