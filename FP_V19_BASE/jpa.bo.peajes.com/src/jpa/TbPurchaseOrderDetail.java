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
 * The persistent class for the TB_PURCHASE_ORDER_DETAIL database table.
 * @author tmolina
 *
 */
@Entity
@Table(name="TB_PURCHASE_ORDER_DETAIL")
public class TbPurchaseOrderDetail implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBPURCHASEORDERDETAIL_PURCHASEORDERDETAILID_GENERATOR")
	@SequenceGenerator(name="TBPURCHASEORDERDETAIL_PURCHASEORDERDETAILID_GENERATOR", sequenceName="TB_PURCHASE_ORDER_DETAIL_SEQ",allocationSize=1)
	@Column(name="PURCHASE_ORDER_DETAIL_ID")
	private Long purchaseOrderDetailId;
	
	@ManyToOne
	@JoinColumn(name="PURCHASE_ORDER_ID")
	private TbPurchaseOrder tbPurchaseOrder;
	
	@Column(name="DETAIL_OPERATION_VALUE")
	private BigDecimal detailOperationValue;
	
	@Column(name="DETAIL_STATE")
	private Integer detailState;
	
	@ManyToOne
	@JoinColumn(name="OPERATION_TYPE_ID")
	private TbOperationType tbOperationType;
	
	@ManyToOne
	@JoinColumn(name="STATION_BO_ID")
	private TbStationBO tbStationBO;
	
	@ManyToOne
	@JoinColumn(name="DEVICE_ID")
	private TbDevice tbDevice;
	
	@Column(name="RECHARGE_DATE")
	private Timestamp rechargeDate;
	
	/**
	 * @return the tbPurchaseOrder
	 */
	public TbPurchaseOrder getTbPurchaseOrder() {
		return tbPurchaseOrder;
	}

	/**
	 * @param tbPurchaseOrder the tbPurchaseOrder to set
	 */
	public void setTbPurchaseOrder(TbPurchaseOrder tbPurchaseOrder) {
		this.tbPurchaseOrder = tbPurchaseOrder;
	}

	/**
	 * @return the detailOperationValue
	 */
	public BigDecimal getDetailOperationValue() {
		return detailOperationValue;
	}

	/**
	 * @param detailOperationValue the detailOperationValue to set
	 */
	public void setDetailOperationValue(BigDecimal detailOperationValue) {
		this.detailOperationValue = detailOperationValue;
	}

	/**
	 * @return the detailState
	 */
	public Integer getDetailState() {
		return detailState;
	}

	/**
	 * @param detailState the detailState to set
	 */
	public void setDetailState(Integer detailState) {
		this.detailState = detailState;
	}

	/**
	 * @return the tbOperationType
	 */
	public TbOperationType getTbOperationType() {
		return tbOperationType;
	}

	/**
	 * @param tbOperationType the tbOperationType to set
	 */
	public void setTbOperationType(TbOperationType tbOperationType) {
		this.tbOperationType = tbOperationType;
	}

	/**
	 * @return the tbDevice
	 */
	public TbDevice getTbDevice() {
		return tbDevice;
	}

	/**
	 * @param tbDevice the tbDevice to set
	 */
	public void setTbDevice(TbDevice tbDevice) {
		this.tbDevice = tbDevice;
	}

	/**
	 * Constructor.
	 */
	public TbPurchaseOrderDetail() {
		super();
	}

	/**
	 * @param purchaseOrderDetailId the purchaseOrderDetailId to set
	 */
	public void setPurchaseOrderDetailId(Long purchaseOrderDetailId) {
		this.purchaseOrderDetailId = purchaseOrderDetailId;
	}

	/**
	 * @return the purchaseOrderDetailId
	 */
	public Long getPurchaseOrderDetailId() {
		return purchaseOrderDetailId;
	}

	/**
	 * @param rechargeDate the rechargeDate to set
	 */
	public void setRechargeDate(Timestamp rechargeDate) {
		this.rechargeDate = rechargeDate;
	}

	/**
	 * @return the rechargeDate
	 */
	public Timestamp getRechargeDate() {
		return rechargeDate;
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