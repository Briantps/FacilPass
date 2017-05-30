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
 * The persistent class for the TB_INCLUSION_STATE database table.
 * @author tmolina
 */
 @Entity
 @Table(name="TB_INCLUSION_STATE")
public class TbInclusionState implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="INCLUSION_STATE_ID")
	private Long inclusionStateId;
	
	@Column(name="INCLUSION_STATE_NAME")
	private String inclusionStateName;
	
	/**
	 * Constructor.
	 */
	public TbInclusionState(){
		super();
	}

	/**
	 * @param inclusionStateId the inclusionStateId to set
	 */
	public void setInclusionStateId(Long inclusionStateId) {
		this.inclusionStateId = inclusionStateId;
	}

	/**
	 * @return the inclusionStateId
	 */
	public Long getInclusionStateId() {
		return inclusionStateId;
	}

	/**
	 * @param inclusionStateName the inclusionStateName to set
	 */
	public void setInclusionStateName(String inclusionStateName) {
		this.inclusionStateName = inclusionStateName;
	}

	/**
	 * @return the inclusionStateName
	 */
	public String getInclusionStateName() {
		return inclusionStateName;
	}
}