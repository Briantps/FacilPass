package jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="TB_POLICY_PERSONAL_DATA_STATE")
public class TbPolicyPersonalDataState implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "TB_POLICY_PERS_DATA_STATE_SEQ", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "TB_POLICY_PERS_DATA_STATE_SEQ", sequenceName = "TB_POLICY_PERS_DATA_STATE_SEQ")
	
	@Column(name="POLICY_PERSONAL_DATA_STATE")
	private Long policyPersonalDataStateId;
	
	@Column (name="POLICY_PERSONAL_DATA_DESC")
	private String policyStateDescrip;
	
	
	
	public TbPolicyPersonalDataState(){
		super();
	}



	public Long getPolicyPersonalDataStateId() {
		return policyPersonalDataStateId;
	}



	public void setPolicyPersonalDataStateId(Long policyPersonalDataStateId) {
		this.policyPersonalDataStateId = policyPersonalDataStateId;
	}



	public String getPolicyStateDescrip() {
		return policyStateDescrip;
	}



	public void setPolicyStateDescrip(String policyStateDescrip) {
		this.policyStateDescrip = policyStateDescrip;
	}
	
	

}
