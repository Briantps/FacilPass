/**
 * 
 */
package jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The persistent class for the RE_ACCOUNT_PURCHASE_ORDER database table.
 * @author tmolina
 */
@Entity
@Table(name="RE_ACCOUNT_PURCHASE_ORDER")
public class ReAccountPurchaseOrder implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RE_ACCOUNT_PURCHASE_ORDER_ACCOUNTPURCHASEORDERID_GENERATOR")
	@SequenceGenerator(name = "RE_ACCOUNT_PURCHASE_ORDER_ACCOUNTPURCHASEORDERID_GENERATOR", sequenceName = "RE_ACCOUNT_PURCHASE_ORDER_SEQ",allocationSize=1)
	@Column(name="ACCOUNT_PURCHASE_ORDER_ID")
	private Long accountPurchaseOrderId;
	
	@ManyToOne
	@JoinColumn(name="PURCHASE_ORDER_ID")
	private TbPurchaseOrder tbPurchaseOrder;
	
	@ManyToOne
	@JoinColumn(name="ACCOUNT_ID")
	private TbAccount tbAccount;
	
	/**
	 * Constructor
	 */
	public ReAccountPurchaseOrder(){
		super();
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
	 * @param accountPurchaseOrderId the accountPurchaseOrderId to set
	 */
	public void setAccountPurchaseOrderId(Long accountPurchaseOrderId) {
		this.accountPurchaseOrderId = accountPurchaseOrderId;
	}

	/**
	 * @return the accountPurchaseOrderId
	 */
	public Long getAccountPurchaseOrderId() {
		return accountPurchaseOrderId;
	}

	/**
	 * @param tbPurchaseOrder the tbPurchaseOrder to set
	 */
	public void setTbPurchaseOrder(TbPurchaseOrder tbPurchaseOrder) {
		this.tbPurchaseOrder = tbPurchaseOrder;
	}

	/**
	 * @return the tbPurchaseOrder
	 */
	public TbPurchaseOrder getTbPurchaseOrder() {
		return tbPurchaseOrder;
	}
}