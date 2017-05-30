/**
 * 
 */
package process;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import util.PurchaseOrder;

import constant.ProcessTrackType;
import constant.PurchaseOrderState;

import ejb.Purchase;

import jpa.TbProcessTrackDetail;

/**
 * Bean to manage Device Process Track.
 * @author tmolina
 *
 */
public class PurchaseOrderProcessBean implements Serializable {
	private static final long serialVersionUID = 1758966582439674378L;

	@EJB(mappedName="ejb/Process")
	private ejb.Process process;
	
	@EJB(mappedName = "ejb/Purchase")
	private Purchase purchase;
	
	// Attributes ------------- //
	
	private List<TbProcessTrackDetail> details;
	
	private Long orderNumber;
	
	private PurchaseOrder order;
	
	// Control Visibility --------------- //

	private String message;
	
	private boolean showMessage;
	
	private boolean showData;

	/**
	 * Constructor.
	 */
	public PurchaseOrderProcessBean() {
	}
	
	// Actions --------------- //
	
	/**
	 * Searches process track.
	 */
	public String search() {
		if (orderNumber != null) {
			order = purchase.getPurchaseOrderByState(orderNumber,PurchaseOrderState.NEW, false);			
			if (order != null) {
				if (details == null) {
					details = new ArrayList<TbProcessTrackDetail>();
				} else {
					details.clear();
				}
				details = process.getProcessDetails(order.getOrder()
						.getPurchaseOrderId(), ProcessTrackType.PURCHASE_ORDER);
								
				if (details.size() <= 0) {
					setShowData(false);
					setShowMessage(true);
					setMessage("No se Encontró un proceso para la Orden No. " + orderNumber + ".");
				} else {
					setShowData(true);
					setShowMessage(false);
				}
			}else{
					setShowMessage(true);
					setMessage("No se encontró la Orden No. " + orderNumber + ".");
			}
		} else {
				setShowData(false);
				setShowMessage(true);
				setMessage("Digite el número de Orden de Pedido Por Favor.");
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