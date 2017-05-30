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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The persistent class for the TB_WAREHOUSE_CARD_TYPE database table.
 * @author tmolina
 */
 @Entity
 @Table(name="TB_WAREHOUSE_CARD_TYPE")
public class TbWarehouseCardType implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TB_WAREHOUSE_CARD_TYPE_TB_WAREHOUSECARDTYPEID_GENERATOR")
	@SequenceGenerator(name="TB_WAREHOUSE_CARD_TYPE_TB_WAREHOUSECARDTYPEID_GENERATOR",  sequenceName = "TB_WAREHOUSE_CARD_TYPE_SEQ",allocationSize=1)
	@Column(name="WAREHOUSE_CARD_TYPE_ID")
	private Long warehouseCardTypeId;
	
	@Column(name="WAREHOUSE_CARD_TYPE_NAME")
	private String warehouseCardTypeName;
	
	/**
	 * Constructor.
	 */
	public TbWarehouseCardType(){
		super();
	}

	/**
	 * @param warehouseCardTypeId the warehouseCardTypeId to set
	 */
	public void setWarehouseCardTypeId(Long warehouseCardTypeId) {
		this.warehouseCardTypeId = warehouseCardTypeId;
	}

	/**
	 * @return the warehouseCardTypeId
	 */
	public Long getWarehouseCardTypeId() {
		return warehouseCardTypeId;
	}

	/**
	 * @param warehouseCardTypeName the warehouseCardTypeName to set
	 */
	public void setWarehouseCardTypeName(String warehouseCardTypeName) {
		this.warehouseCardTypeName = warehouseCardTypeName;
	}

	/**
	 * @return the warehouseCardTypeName
	 */
	public String getWarehouseCardTypeName() {
		return warehouseCardTypeName;
	}
}