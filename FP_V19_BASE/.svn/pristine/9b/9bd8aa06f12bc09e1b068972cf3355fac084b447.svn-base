package jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="TB_TRANSACTION_STATE")
public class TbTransactionState implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="STATE_ID")
	private Long stateId;
	
	@Column(name="NAME_STATE")
	private String nameState;
	
	@Column(name="DESCRIPTION_STATE")
	private String descriptionstate;
	
	
	public TbTransactionState (){
		super();
	}

	/**
	 * @param stateId the stateId to set
	 */
	public void setStateId(Long stateId) {
		this.stateId = stateId;
	}

	/**
	 * @return the stateId
	 */
	public Long getStateId() {
		return stateId;
	}

	/**
	 * @param nameState the nameState to set
	 */
	public void setNameState(String nameState) {
		this.nameState = nameState;
	}

	/**
	 * @return the nameState
	 */
	public String getNameState() {
		return nameState;
	}

	/**
	 * @param descriptionstate the descriptionstate to set
	 */
	public void setDescriptionstate(String descriptionstate) {
		this.descriptionstate = descriptionstate;
	}

	/**
	 * @return the descriptionstate
	 */
	public String getDescriptionstate() {
		return descriptionstate;
	}
	
}
