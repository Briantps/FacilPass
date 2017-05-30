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

@Entity
@Table(name="TB_POLICY_PERSONAL_DATA")
public class TbPolicyPersonalData implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "TB_POLICY_PERSONAL_DATA_SEQ", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "TB_POLICY_PERSONAL_DATA_SEQ", sequenceName = "TB_POLICY_PERSONAL_DATA_SEQ")
	
	@Column(name="POLICY_PERSONAL_DATA_ID")
	private Long policyPersonalDataId;
	
	@Column (name="POLICY_PERSONAL_DATA_TXT")
	private String policyTXT;
	
	@ManyToOne
	@JoinColumn(name="USER_ID")
	private TbSystemUser policyUserId;
			
	@Column(name="POLICY_PERSONAL_DATA_DATE")
	private Timestamp policyPersonalDate;
	
	@ManyToOne
	@JoinColumn(name = "POLICY_PERSONAL_DATA_STATE")
	private TbPolicyPersonalDataState policyState;
	
	@ManyToOne
	@JoinColumn(name="TYPE_ROLE_ID")
	private TbTypeRole policyTypeRole;
	
		
	public TbPolicyPersonalData(){
		super();
	}


	public Long getPolicyPersonalDataId() {
		return policyPersonalDataId;
	}


	public void setPolicyPersonalDataId(Long policyPersonalDataId) {
		this.policyPersonalDataId = policyPersonalDataId;
	}


	public String getPolicyTXT() {
		return policyTXT;
	}


	public void setPolicyTXT(String policyTXT) {
		this.policyTXT = policyTXT;
	}


	public TbSystemUser getPolicyUserId() {
		return policyUserId;
	}


	public void setPolicyUserId(TbSystemUser policyUserId) {
		this.policyUserId = policyUserId;
	}


	public Timestamp getPolicyPersonalDate() {
		return policyPersonalDate;
	}


	public void setPolicyPersonalDate(Timestamp policyPersonalDate) {
		this.policyPersonalDate = policyPersonalDate;
	}


	public TbPolicyPersonalDataState getPolicyState() {
		return policyState;
	}


	public void setPolicyState(TbPolicyPersonalDataState policyState) {
		this.policyState = policyState;
	}


	public TbTypeRole getPolicyTypeRole() {
		return policyTypeRole;
	}


	public void setPolicyTypeRole(TbTypeRole policyTypeRole) {
		this.policyTypeRole = policyTypeRole;
	}

	

}
