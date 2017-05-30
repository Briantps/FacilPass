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
@Table(name="TB_USER_POLICY_DATA_STATE")
public class TbUserPolicyDataState implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "TB_USER_POLICITY_DATA_STATE_SEQ", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "TB_USER_POLICITY_DATA_STATE_SEQ", sequenceName = "TB_USER_POLICITY_DATA_STATE_SEQ")
	
	@Column(name="US_POLICY_PERS_DATA_STATE")
	private Long usPolicyPerDataStateId;
	
	@Column (name="US_POLICY_PERS_DATA_DESC")
	private String usPolicyStateDescrip;
	
	
	
	public TbUserPolicyDataState(){
		super();
	}



	public Long getUsPolicyPerDataStateId() {
		return usPolicyPerDataStateId;
	}



	public void setUsPolicyPerDataStateId(Long usPolicyPerDataStateId) {
		this.usPolicyPerDataStateId = usPolicyPerDataStateId;
	}



	public String getUsPolicyStateDescrip() {
		return usPolicyStateDescrip;
	}



	public void setUsPolicyStateDescrip(String usPolicyStateDescrip) {
		this.usPolicyStateDescrip = usPolicyStateDescrip;
	}

	
}
