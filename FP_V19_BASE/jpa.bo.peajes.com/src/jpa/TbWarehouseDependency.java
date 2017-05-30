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
 * The persistent class for the TB_WAREHOUSE_DEPENDENCY database table.
 * @author tmolina
 */
 @Entity
 @Table(name="TB_WAREHOUSE_DEPENDENCY")
public class TbWarehouseDependency implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TB_WAREHOUSE_DEPENDENCY_WAREHOUSEDEPENDENCYID_GENERATOR")
	@SequenceGenerator(name="TB_WAREHOUSE_DEPENDENCY_WAREHOUSEDEPENDENCYID_GENERATOR",  sequenceName = "TB_WAREHOUSE_DEPENDENCY_SEQ",allocationSize=1)
	@Column(name="WAREHOUSE_DEPENDENCY_ID")
	private Long warehouseDependencyId;
	
	@Column(name="WAREHOUSE_DEPENDENCY_NAME")
	private String warehouseDependencyName;
	
	/**
	 * Constructor.
	 */
	public TbWarehouseDependency(){
		super();
	}

	/**
	 * @param warehouseDependencyId the warehouseDependencyId to set
	 */
	public void setWarehouseDependencyId(Long warehouseDependencyId) {
		this.warehouseDependencyId = warehouseDependencyId;
	}

	/**
	 * @return the warehouseDependencyId
	 */
	public Long getWarehouseDependencyId() {
		return warehouseDependencyId;
	}

	/**
	 * @param warehouseDependencyName the warehouseDependencyName to set
	 */
	public void setWarehouseDependencyName(String warehouseDependencyName) {
		this.warehouseDependencyName = warehouseDependencyName;
	}

	/**
	 * @return the warehouseDependencyName
	 */
	public String getWarehouseDependencyName() {
		return warehouseDependencyName;
	}
}