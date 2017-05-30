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
 * The persistent class for the TB_WAREHOUSE_STATE database table.
 * @author tmolina
 */
 @Entity
 @Table(name="TB_WAREHOUSE_STATE")
public class TbWarehouseState implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="WAREHOUSE_STATE_ID")
	private Long warehouseStateId;
	
	@Column(name="WAREHOUSE_STATE_NAME")
	private String warehouseStateName;
	
	/**
	 * Constructor.
	 */
	public TbWarehouseState(){
		super();
	}

	/**
	 * @param warehouseStateId the warehouseStateId to set
	 */
	public void setWarehouseStateId(Long warehouseStateId) {
		this.warehouseStateId = warehouseStateId;
	}

	/**
	 * @return the warehouseStateId
	 */
	public Long getWarehouseStateId() {
		return warehouseStateId;
	}

	/**
	 * @param warehouseStateName the warehouseStateName to set
	 */
	public void setWarehouseStateName(String warehouseStateName) {
		this.warehouseStateName = warehouseStateName;
	}

	/**
	 * @return the warehouseStateName
	 */
	public String getWarehouseStateName() {
		return warehouseStateName;
	}
}