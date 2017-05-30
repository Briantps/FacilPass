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
 *  The persistent class for the TB_OPERATION_TYPE database table.
 * @author tmolina.
 */
@Entity
@Table(name="TB_OPERATION_TYPE")
public class TbOperationType implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="OPERATION_TYPE_ID")
	private Long operationTypeId;
	
	@Column(name="OPERATION_TYPE_NAME")
	private String operationTypeName;

	/**
	 * Constructor.
	 */
	public TbOperationType() {
		super();
	}

	/**
	 * @param operationTypeId the operationTypeId to set
	 */
	public void setOperationTypeId(Long operationTypeId) {
		this.operationTypeId = operationTypeId;
	}

	/**
	 * @return the operationTypeId
	 */
	public Long getOperationTypeId() {
		return operationTypeId;
	}

	/**
	 * @param operationTypeName the operationTypeName to set
	 */
	public void setOperationTypeName(String operationTypeName) {
		this.operationTypeName = operationTypeName;
	}

	/**
	 * @return the operationTypeName
	 */
	public String getOperationTypeName() {
		return operationTypeName;
	}
}