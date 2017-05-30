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
 * The persistent class for the TB_ACTION database table.
 * 
 */
@Entity
@Table(name="TB_ACTION")
public class TbAction implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TB_ACTION_TBACTIONID_GENERATOR")
	@SequenceGenerator(name="TB_ACTION_TBACTIONID_GENERATOR",  sequenceName = "TB_ACTION_SEQ",allocationSize=1)
	@Column(name="ACTION_ID")
	private long actionId;

	@Column(name="ACTION_NAME")
	private String actionName;

	/**
	 * Constructor
	 */
    public TbAction() {
    	super();
    }

    /**
     * 
     * @return actionId
     */
	public long getActionId() {
		return this.actionId;
	}

	/**
	 * Setter for actionId
	 * @param actionId
	 */
	public void setActionId(long actionId) {
		this.actionId = actionId;
	}

	/**
	 * 
	 * @return actionName
	 */
	public String getActionName() {
		return this.actionName;
	}

	/**
	 * Setter for actionName
	 * @param actionName
	 */
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
}