/**
 * 
 */
package jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The persistent class for the TB_WAREHOUSE_OPERATION_TYPE database table.
 * @author tmolina
 */
 @Entity
 @Table(name="TB_WAREHOUSE_OPERATION_TYPE")
public class TbWarehouseOperationType implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="WAREHOUSE_OPERATION_TYPE_ID")
	private Long warehouseOperationTypeId;
	
	@Column(name="WAREHOUSE_OPERATION_TYPE")
	private String warehouseOperationType;
	
	/**
	 * Constructor.
	 */
	public TbWarehouseOperationType(){
		super();
	}

	/**
	 * @param warehouseOperationTypeId the warehouseOperationTypeId to set
	 */
	public void setWarehouseOperationTypeId(Long warehouseOperationTypeId) {
		this.warehouseOperationTypeId = warehouseOperationTypeId;
	}

	/**
	 * @return the warehouseOperationTypeId
	 */
	public Long getWarehouseOperationTypeId() {
		return warehouseOperationTypeId;
	}

	/**
	 * @param warehouseOperationType the warehouseOperationType to set
	 */
	public void setWarehouseOperationType(String warehouseOperationType) {
		this.warehouseOperationType = warehouseOperationType;
	}

	/**
	 * @return the warehouseOperationType
	 */
	public String getWarehouseOperationType() {
		return warehouseOperationType;
	}
}