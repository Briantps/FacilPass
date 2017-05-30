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
 * The persistent class for the RE_USER_OTP database table.
 * 
 */

@Entity
@Table(name="RE_USER_OTP")
public class ReUserOtp  implements Serializable{


	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="RE_USER_OTP_GENERATOR", sequenceName="RE_USER_OTP_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="RE_USER_OTP_GENERATOR")
	@Column(name="USER_OTP_ID", unique=true, nullable=false, precision=19)
	private Long userOtpId;
	
	@ManyToOne
	@JoinColumn(name="OTP_ID")
	private TbOtp otpId;
	
	@ManyToOne
	@JoinColumn(name="USER_ID")
	private TbSystemUser userId;
	
	@Column(name="DATE_RELATION")
	private Timestamp dateRelation;
	
	@ManyToOne
	@JoinColumn(name="STATE_OTP_ID")
	private TbStateOtp stateOtp;
	
	public ReUserOtp(){
		super();
	}

	public void setUserOtpId(Long userOtpId) {
		this.userOtpId = userOtpId;
	}

	public Long getUserOtpId() {
		return userOtpId;
	}	

	public void setDateRelation(Timestamp dateRelation) {
		this.dateRelation = dateRelation;
	}

	public Timestamp getDateRelation() {
		return dateRelation;
	}

	public void setOtpId(TbOtp otpId) {
		this.otpId = otpId;
	}

	public TbOtp getOtpId() {
		return otpId;
	}

	public void setUserId(TbSystemUser userId) {
		this.userId = userId;
	}

	public TbSystemUser getUserId() {
		return userId;
	}

	public TbStateOtp getStateOtp() {
		return stateOtp;
	}

	public void setStateOtp(TbStateOtp stateOtp) {
		this.stateOtp = stateOtp;
	}
	
}
