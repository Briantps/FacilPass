/**
 * 
 */
package util.device;

import java.io.Serializable;

import jpa.TbPurchaseOrderDetail;

/**
 * @author tmolina
 *
 */
public class ToRecharge implements Serializable {
	private static final long serialVersionUID = 4054872865729578812L;
	
	// Attributes --------- //
	
	private TbPurchaseOrderDetail detail;

	/**
	 * Constructor.
	 */
	public ToRecharge() {
	}

	/**
	 * @param detail the detail to set
	 */
	public void setDetail(TbPurchaseOrderDetail detail) {
		this.detail = detail;
	}

	/**
	 * @return the detail
	 */
	public TbPurchaseOrderDetail getDetail() {
		return detail;
	}
}