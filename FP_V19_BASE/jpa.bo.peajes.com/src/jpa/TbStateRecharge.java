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
 * Entity implementation class for Entity: TB_STATE_RECHARGE
 *
 */
@Entity
@Table(name="TB_STATE_RECHARGE")
public class TbStateRecharge implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TB_STATE_RECHARGE_GENERATOR")
	@SequenceGenerator(name = "TB_STATE_RECHARGE_GENERATOR", sequenceName = "TB_STATE_RECHARGE_SEQ",allocationSize=1)
	@Column(name="STATE_RECHARGE_ID")
	private Long stateRechargeId;
	
	@Column(name="NAME_STATE_RECHARGE")
	private String nameStateRecharge;
	
	@Column(name="DESCRIPTION_STATE_RECHARGE")
	private String descriptionStateRecharge;
	
	@Column(name="STATE_RECHARGE")
	private String stateRecharge;

	public void setStateRechargeId(Long stateRechargeId) {
		this.stateRechargeId = stateRechargeId;
	}

	public Long getStateRechargeId() {
		return stateRechargeId;
	}

	public void setNameStateRecharge(String nameStateRecharge) {
		this.nameStateRecharge = nameStateRecharge;
	}

	public String getNameStateRecharge() {
		return nameStateRecharge;
	}

	public void setDescriptionStateRecharge(String descriptionStateRecharge) {
		this.descriptionStateRecharge = descriptionStateRecharge;
	}

	public String getDescriptionStateRecharge() {
		return descriptionStateRecharge;
	}

	public void setStateRecharge(String stateRecharge) {
		this.stateRecharge = stateRecharge;
	}

	public String getStateRecharge() {
		return stateRecharge;
	}


	
}
