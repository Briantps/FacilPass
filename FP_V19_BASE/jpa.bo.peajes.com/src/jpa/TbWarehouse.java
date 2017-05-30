/**
 * 
 */
package jpa;

import java.io.Serializable;
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
 * The persistent class for the TB_WAREHOUSE database table.
 * @author tmolina
 */
 @Entity             
 @Table(name="TB_WAREHOUSE")
public class TbWarehouse implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(sequenceName="TB_WAREHOUSE_SEQ", name="TB_WAREHOUSE_WAREHOUSEID_GENERATOR",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TB_WAREHOUSE_WAREHOUSEID_GENERATOR")
	@Column(name="WAREHOUSE_ID")
	private Long warehouseId;
	
	@ManyToOne
	@JoinColumn(name="WAREHOUSE_OPERATION_TYPE_ID")
	private TbWarehouseOperationType tbWarehouseOperationType;
	
	@Column(name="ORDER_NUMBER")
	private String orderNumber;
	
	@Column(name="DEVICE_QUANTITY")
	private Long deviceQuantity;
	
	@Column(name="CREATION_DATE")
	private Timestamp creationDate;
	
	@ManyToOne
	@JoinColumn(name="ORIGIN_ID")
	private TbWarehouseDependency originId;
	
	@ManyToOne
	@JoinColumn(name="DESTINATION_ID")
	private TbWarehouseDependency destinationId;
	
	@ManyToOne
	@JoinColumn(name="WAREHOUSE_STATE_ID")
	private TbWarehouseState tbWarehouseState;
	
	@Column(name="WAREHOUSE_IS_CARD")
	private boolean warehouseIsCard;
	
	/**
	 * Constructor.
	 */
	public TbWarehouse() {
		super();
	}

	/**
	 * @param warehouseId the warehouseId to set
	 */
	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}

	/**
	 * @return the warehouseId
	 */
	public Long getWarehouseId() {
		return warehouseId;
	}

	/**
	 * @param deviceQuantity the deviceQuantity to set
	 */
	public void setDeviceQuantity(Long deviceQuantity) {
		this.deviceQuantity = deviceQuantity;
	}

	/**
	 * @return the deviceQuantity
	 */
	public Long getDeviceQuantity() {
		return deviceQuantity;
	}

	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return the creationDate
	 */
	public Timestamp getCreationDate() {
		return creationDate;
	}

	/**
	 * @param tbWarehouseOperationType the tbWarehouseOperationType to set
	 */
	public void setTbWarehouseOperationType(TbWarehouseOperationType tbWarehouseOperationType) {
		this.tbWarehouseOperationType = tbWarehouseOperationType;
	}

	/**
	 * @return the tbWarehouseOperationType
	 */
	public TbWarehouseOperationType getTbWarehouseOperationType() {
		return tbWarehouseOperationType;
	}

	/**
	 * @param orderNumber the orderNumber to set
	 */
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	/**
	 * @return the orderNumber
	 */
	public String getOrderNumber() {
		return orderNumber;
	}
	
	/**
	 * @return the originId
	 */
	public TbWarehouseDependency getOriginId() {
		return originId;
	}

	/**
	 * @param originId the originId to set
	 */
	public void setOriginId(TbWarehouseDependency originId) {
		this.originId = originId;
	}

	/**
	 * @param destinationId the destinationId to set
	 */
	public void setDestinationId(TbWarehouseDependency destinationId) {
		this.destinationId = destinationId;
	}

	/**
	 * @return the destinationId
	 */
	public TbWarehouseDependency getDestinationId() {
		return destinationId;
	}

	/**
	 * @param tbWarehouseState the tbWarehouseState to set
	 */
	public void setTbWarehouseState(TbWarehouseState tbWarehouseState) {
		this.tbWarehouseState = tbWarehouseState;
	}

	/**
	 * @return the tbWarehouseState
	 */
	public TbWarehouseState getTbWarehouseState() {
		return tbWarehouseState;
	}

	/**
	 * @param warehouseIsCard the warehouseIsCard to set
	 */
	public void setWarehouseIsCard(boolean warehouseIsCard) {
		this.warehouseIsCard = warehouseIsCard;
	}

	/**
	 * @return the warehouseIsCard
	 */
	public boolean isWarehouseIsCard() {
		return warehouseIsCard;
	}
}