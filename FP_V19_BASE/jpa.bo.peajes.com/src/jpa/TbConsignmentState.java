package jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The persistent class for the TB_CONSIGNMENT_STATE database table.
 * 
 */
@Entity
@Table(name="TB_CONSIGNMENT_STATE")
public class TbConsignmentState implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="CONSIGNMENT_STATE_ID")
	private long consignmentStatetId;

	@Column(name="CONSIGNMENT_STATE_NAME")
	private String consignmentStateName;

	/**
	 * Constructor
	 */
    public TbConsignmentState() {
    	super();
    }

	/**
	 * @param consignmentStatetId the consignmentStatetId to set
	 */
	public void setConsignmentStatetId(long consignmentStatetId) {
		this.consignmentStatetId = consignmentStatetId;
	}

	/**
	 * @return the consignmentStatetId
	 */
	public long getConsignmentStatetId() {
		return consignmentStatetId;
	}

	/**
	 * @param consignmentStateName the consignmentStateName to set
	 */
	public void setConsignmentStateName(String consignmentStateName) {
		this.consignmentStateName = consignmentStateName;
	}

	/**
	 * @return the consignmentStateName
	 */
	public String getConsignmentStateName() {
		return consignmentStateName;
	}
}