/**
 * 
 */
package util;

import java.io.Serializable;
import java.util.List;

import jpa.TbAccount;
import jpa.TbPurchaseOrder;
import jpa.TbPurchaseOrderDetail;

/**
 * @author tmolina
 *
 */
public class PurchaseOrder implements Serializable {
	private static final long serialVersionUID = 5257042831319400826L;
	
	private TbPurchaseOrder order;
	
	private List<TbPurchaseOrderDetail> details;
	
	private boolean account;
	
	private boolean transfer;
	
	private boolean allowDetail;
	
	private boolean showAccountDetail;
	
	private List<OrderDetail> toDetailList;
	
	private Long valueToAsign;
	
	private boolean allApproved;
	
	private TbAccount tbAccount;
	
	private Long valueTo;

	/**
	 * Constructor.
	 */
	public PurchaseOrder() {
	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(TbPurchaseOrder order) {
		this.order = order;
	}

	/**
	 * @return the order
	 */
	public TbPurchaseOrder getOrder() {
		return order;
	}

	/**
	 * @param details the details to set
	 */
	public void setDetails(List<TbPurchaseOrderDetail> details) {
		this.details = details;
	}

	/**
	 * @return the details
	 */
	public List<TbPurchaseOrderDetail> getDetails() {
		return details;
	}

	/**
	 * @param transfer the transfer to set
	 */
	public void setTransfer(boolean transfer) {
		this.transfer = transfer;
	}

	/**
	 * @return the transfer
	 */
	public boolean isTransfer() {
		return transfer;
	}

	/**
	 * @param allowDetail the allowDetail to set
	 */
	public void setAllowDetail(boolean allowDetail) {
		this.allowDetail = allowDetail;
	}

	/**
	 * @return the allowDetail
	 */
	public boolean isAllowDetail() {
		return allowDetail;
	}

	/**
	 * @param toDetailList the toDetailList to set
	 */
	public void setToDetailList(List<OrderDetail> toDetailList) {
		this.toDetailList = toDetailList;
	}

	/**
	 * @return the toDetailList
	 */
	public List<OrderDetail> getToDetailList() {
		return toDetailList;
	}

	/**
	 * @param valueToAsign the valueToAsign to set
	 */
	public void setValueToAsign(Long valueToAsign) {
		this.valueToAsign = valueToAsign;
	}

	/**
	 * @return the valueToAsign
	 */
	public Long getValueToAsign() {
		return valueToAsign;
	}

	/**
	 * @param allApproved the allApproved to set
	 */
	public void setAllApproved(boolean allApproved) {
		this.allApproved = allApproved;
	}

	/**
	 * @return the allApproved
	 */
	public boolean isAllApproved() {
		return allApproved;
	}

	/**
	 * @param showAccountDetail the showAccountDetail to set
	 */
	public void setShowAccountDetail(boolean showAccountDetail) {
		this.showAccountDetail = showAccountDetail;
	}

	/**
	 * @return the showAccountDetail
	 */
	public boolean isShowAccountDetail() {
		return showAccountDetail;
	}

	/**
	 * @param account the account to set
	 */
	public void setAccount(boolean account) {
		this.account = account;
	}

	/**
	 * @return the account
	 */
	public boolean isAccount() {
		return account;
	}

	/**
	 * @param tbAccount the tbAccount to set
	 */
	public void setTbAccount(TbAccount tbAccount) {
		this.tbAccount = tbAccount;
	}

	/**
	 * @return the tbAccount
	 */
	public TbAccount getTbAccount() {
		return tbAccount;
	}

	/**
	 * @param valueTo the valueTo to set
	 */
	public void setValueTo(Long valueTo) {
		this.valueTo = valueTo;
	}

	/**
	 * @return the valueTo
	 */
	public Long getValueTo() {
		return valueTo;
	}
}