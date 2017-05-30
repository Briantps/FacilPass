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
@Table(name="RE_USER_POLICY_PERSONAL_DATA")
public class ReUserPolicyPersonalData implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "RE_USER_POLICY_PERS_DATA_SEQ", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "RE_USER_POLICY_PERS_DATA_SEQ", sequenceName = "RE_USER_POLICY_PERS_DATA_SEQ")
	
	@Column(name="USER_POLICY_PERSONAL_DATA_ID")
	private Long userPolicyPersonalDataId;
		
	@ManyToOne
	@JoinColumn(name="USER_ID")
	private TbSystemUser userPolicyUserId;
	
	@ManyToOne
	@JoinColumn(name="POLICY_PERSONAL_DATA_ID")
	private TbPolicyPersonalData userPolicyPersonDataId;
	
	@Column(name="APROVE_DATE")
	private Timestamp userPolicyPersonalAprobe;
	
	@Column(name="REJECTED_DATE")
	private Timestamp userPolicyPersonalRejected;
			
	@ManyToOne
	@JoinColumn(name="US_POLICY_PERS_DATA_STATE")
	private TbUserPolicyDataState userPolicyState;
	
	@Column(name="REGISTER_IP")
	private String policyIp;
	
		
	public ReUserPolicyPersonalData(){
		super();
	}


	public Long getUserPolicyPersonalDataId() {
		return userPolicyPersonalDataId;
	}


	public void setUserPolicyPersonalDataId(Long userPolicyPersonalDataId) {
		this.userPolicyPersonalDataId = userPolicyPersonalDataId;
	}


	public TbSystemUser getUserPolicyUserId() {
		return userPolicyUserId;
	}


	public void setUserPolicyUserId(TbSystemUser userPolicyUserId) {
		this.userPolicyUserId = userPolicyUserId;
	}


	public TbPolicyPersonalData getUserPolicyPersonDataId() {
		return userPolicyPersonDataId;
	}


	public void setUserPolicyPersonDataId(
			TbPolicyPersonalData userPolicyPersonDataId) {
		this.userPolicyPersonDataId = userPolicyPersonDataId;
	}


	public Timestamp getUserPolicyPersonalAprobe() {
		return userPolicyPersonalAprobe;
	}


	public void setUserPolicyPersonalAprobe(Timestamp userPolicyPersonalAprobe) {
		this.userPolicyPersonalAprobe = userPolicyPersonalAprobe;
	}


	public Timestamp getUserPolicyPersonalRejected() {
		return userPolicyPersonalRejected;
	}


	public void setUserPolicyPersonalRejected(Timestamp userPolicyPersonalRejected) {
		this.userPolicyPersonalRejected = userPolicyPersonalRejected;
	}




	public String getPolicyIp() {
		return policyIp;
	}


	public TbUserPolicyDataState getUserPolicyState() {
		return userPolicyState;
	}


	public void setUserPolicyState(TbUserPolicyDataState userPolicyState) {
		this.userPolicyState = userPolicyState;
	}


	public void setPolicyIp(String policyIp) {
		this.policyIp = policyIp;
	}

	
}
