package jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * Entity implementation class for Entity: TB_PROCESS_RECHARGE_AVAL
 * @author nathaly.ramirez
 */
@Entity
@Table(name="TB_PROCESS_RECHARGE_AVAL")
public class TbProcessRechargeAval implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id	
	@Column(name="PROCESS_RECHARGE_AVAL_ID")
	private long processRechargeAvalId;
	
	@Column(name="PROCESS_RECHARGE_AVAL_NAME")
	private long processRechargeAvalName;
	
	
	public  TbProcessRechargeAval(){
		super();
	}


	public void setProcessRechargeAvalId(long processRechargeAvalId) {
		this.processRechargeAvalId = processRechargeAvalId;
	}


	public long getProcessRechargeAvalId() {
		return processRechargeAvalId;
	}


	public void setProcessRechargeAvalName(long processRechargeAvalName) {
		this.processRechargeAvalName = processRechargeAvalName;
	}


	public long getProcessRechargeAvalName() {
		return processRechargeAvalName;
	}

}
