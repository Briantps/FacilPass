package jpa;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;




/**
 * The persistent class for the TB_ROLE database table.
 * 
 */
@Entity
@Table(name = "TB_CONF_AUTOMATIC_RECHARGE")
public class TbConfAutomaticRecharge implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "CONF_AUTOMATIC_RECHARGE_SEQ", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "CONF_AUTOMATIC_RECHARGE_SEQ", sequenceName = "CONF_AUTOMATIC_RECHARGE_SEQ",allocationSize=1)
	
	@Column(name="CONF_AUTOMATIC_RECHARGE_ID")
	private Long confiAutomaticRechargeId;
		
	@Column(name="CONF_AUTOMATIC_RECHARGE_TXT")
	private String confAutomaticRechargeTxt;
	
	@Column(name="CONF_AUTOMATIC_RECHARGE_DATE")
	private Timestamp confAutomaticRechargeDate;
	
	@ManyToOne
	@JoinColumn(name="TYPE_CONF_AUTO_RECHAR_ID")
	private TbTypeConfAutoRechar typeConfAutomRechar;
	
	@ManyToOne
	@JoinColumn(name="USER_ID")
	private TbSystemUser userId;
	
	@ManyToOne
	@JoinColumn(name="STATE_ID")
	private TbState stateId;
	

	/**
	 * Constructor
	 */
    public TbConfAutomaticRecharge() {
    	super();
    }


	public Long getConfiAutomaticRechargeId() {
		return confiAutomaticRechargeId;
	}


	public void setConfiAutomaticRechargeId(Long confiAutomaticRechargeId) {
		this.confiAutomaticRechargeId = confiAutomaticRechargeId;
	}


	public String getConfAutomaticRechargeTxt() {
		return confAutomaticRechargeTxt;
	}


	public void setConfAutomaticRechargeTxt(String confAutomaticRechargeTxt) {
		this.confAutomaticRechargeTxt = confAutomaticRechargeTxt;
	}


	public Timestamp getConfAutomaticRechargeDate() {
		return confAutomaticRechargeDate;
	}


	public void setConfAutomaticRechargeDate(Timestamp confAutomaticRechargeDate) {
		this.confAutomaticRechargeDate = confAutomaticRechargeDate;
	}


	public TbTypeConfAutoRechar getTypeConfAutomRechar() {
		return typeConfAutomRechar;
	}


	public void setTypeConfAutomRechar(TbTypeConfAutoRechar typeConfAutomRechar) {
		this.typeConfAutomRechar = typeConfAutomRechar;
	}


	public TbSystemUser getUserId() {
		return userId;
	}


	public void setUserId(TbSystemUser userId) {
		this.userId = userId;
	}


	public TbState getStateId() {
		return stateId;
	}


	public void setStateId(TbState stateId) {
		this.stateId = stateId;
	}
}