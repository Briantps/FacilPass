/**
 * 
 */
package jpa;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

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
 * @author tmolina
 *
 */
@Entity
@Table(name="TB_PURCHASE_ORDER")
public class TbPurchaseOrder implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBPURCHASEORDER_PURCHASEORDERID_GENERATOR")
	@SequenceGenerator(name="TBPURCHASEORDER_PURCHASEORDERID_GENERATOR", sequenceName="TB_PURCHASE_ORDER_SEQ",allocationSize=1)
	@Column(name="PURCHASE_ORDER_ID")
	private Long purchaseOrderId;
	
	@Column(name="PURCHASE_ORDER_NUMBER")
	private Long puchaseOrderNumber;
	
	@Column(name="PURCHASE_ORDER_DATE")
	private Timestamp purchaseOrderDate;
	
	@ManyToOne
	@JoinColumn(name="USER_DATA_ID")
	private TbUserData tbUserData;
	
	@Column(name="TRANSACTIONS_TO_DO")
	private Long transactionsToDo;
	
	@Column(name="PURCHASE_ORDER_STATE")
	private Integer purchaseOrderState;
	
	@ManyToOne
	@JoinColumn(name="STATION_BO_ID")
	private TbStationBO tbStationBO;
	
	@Column(name="INVOICE_NUMBER")
	private String invoiceNumber;
	
	@Column(name="PURCHASE_ORDER_VALUE")
	private BigDecimal purchaseOrderValue;
	
	/**
	 * Constructor.
	 */
	public TbPurchaseOrder() {
		super();
	}

	/**
	 * @param purchaseOrderId the purchaseOrderId to set
	 */
	public void setPurchaseOrderId(Long purchaseOrderId) {
		this.purchaseOrderId = purchaseOrderId;
	}

	/**
	 * @return the purchaseOrderId
	 */
	public Long getPurchaseOrderId() {
		return purchaseOrderId;
	}

	/**
	 * @param puchaseOrderNumber the puchaseOrderNumber to set
	 */
	public void setPuchaseOrderNumber(Long puchaseOrderNumber) {
		this.puchaseOrderNumber = puchaseOrderNumber;
	}

	/**
	 * @return the puchaseOrderNumber
	 */
	public Long getPuchaseOrderNumber() {
		return puchaseOrderNumber;
	}

	/**
	 * @param purchaseOrderDate the purchaseOrderDate to set
	 */
	public void setPurchaseOrderDate(Timestamp purchaseOrderDate) {
		this.purchaseOrderDate = purchaseOrderDate;
	}

	/**
	 * @return the purchaseOrderDate
	 */
	public Timestamp getPurchaseOrderDate() {
		return purchaseOrderDate;
	}

	/**
	 * @param tbUserData the tbUserData to set
	 */
	public void setTbUserData(TbUserData tbUserData) {
		this.tbUserData = tbUserData;
	}

	/**
	 * @return the tbUserData
	 */
	public TbUserData getTbUserData() {
		return tbUserData;
	}

	/**
	 * @param transactionsToDo the transactionsToDo to set
	 */
	public void setTransactionsToDo(Long transactionsToDo) {
		this.transactionsToDo = transactionsToDo;
	}

	/**
	 * @return the transactionsToDo
	 */
	public Long getTransactionsToDo() {
		return transactionsToDo;
	}

	/**
	 * @param purchaseOrderState the purchaseOrderState to set
	 */
	public void setPurchaseOrderState(Integer purchaseOrderState) {
		this.purchaseOrderState = purchaseOrderState;
	}

	/**
	 * @return the purchaseOrderState
	 */
	public Integer getPurchaseOrderState() {
		return purchaseOrderState;
	}

	/**
	 * @param invoiceNumber the invoiceNumber to set
	 */
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	/**
	 * @return the invoiceNumber
	 */
	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	/**
	 * @param purchaseOrderValue the purchaseOrderValue to set
	 */
	public void setPurchaseOrderValue(BigDecimal purchaseOrderValue) {
		this.purchaseOrderValue = purchaseOrderValue;
	}

	/**
	 * @return the purchaseOrderValue
	 */
	public BigDecimal getPurchaseOrderValue() {
		return purchaseOrderValue;
	}

	/**
	 * @param tbStationBO the tbStationBO to set
	 */
	public void setTbStationBO(TbStationBO tbStationBO) {
		this.tbStationBO = tbStationBO;
	}

	/**
	 * @return the tbStationBO
	 */
	public TbStationBO getTbStationBO() {
		return tbStationBO;
	}
}