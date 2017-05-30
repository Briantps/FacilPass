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
* Entity implementation class for Entity: TbState
*
*/
@Entity
@Table(name="TB_STATE")
public class TbState implements Serializable{
private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TB_STATE_GENERATOR")
	@SequenceGenerator(name = "TB_STATE_GENERATOR", sequenceName = "TB_STATE_SEQ",allocationSize=1, initialValue=1)
	@Column(name="STATE_ID")
	private Long stateId;
	
	@Column(name="NAME_STATE")
	private String nameState;
	
	@Column(name="DESCRIPTION_STATE")
	private String descriptionState;

	public void setStateId(Long stateId) {
		this.stateId = stateId;
	}

	public Long getStateId() {
		return stateId;
	}

	public void setNameState(String nameState) {
		this.nameState = nameState;
	}

	public String getNameState() {
		return nameState;
	}

	public void setDescriptionState(String descriptionState) {
		this.descriptionState = descriptionState;
	}

	public String getDescriptionState() {
		return descriptionState;
	}
	

}
