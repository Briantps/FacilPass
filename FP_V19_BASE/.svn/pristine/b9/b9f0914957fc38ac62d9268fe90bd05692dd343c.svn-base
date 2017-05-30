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
 * Persistence class for Table TB_TABLE
 * @author tmolina
 *
 */
@Entity
@Table(name="TB_TABLE")
public class TbTable implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="TABLE_ID")
	private Long tableId;
	
	@Column(name="TABLE_NAME")
	private String tableName;
	
	@Column(name="TABLE_MAPPED_NAME")
	private String tableMappedName;
	
	public TbTable(){
		super();
	}

	/**
	 * @param tableId the tableId to set
	 */
	public void setTableId(Long tableId) {
		this.tableId = tableId;
	}

	/**
	 * @return the tableId
	 */
	public Long getTableId() {
		return tableId;
	}

	/**
	 * @param tableName the tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * @param tableMappedName the tableMappedName to set
	 */
	public void setTableMappedName(String tableMappedName) {
		this.tableMappedName = tableMappedName;
	}

	/**
	 * @return the tableMappedName
	 */
	public String getTableMappedName() {
		return tableMappedName;
	}
}