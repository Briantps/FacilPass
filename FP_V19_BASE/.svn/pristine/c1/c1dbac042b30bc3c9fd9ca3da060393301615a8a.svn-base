package jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="TB_STATE_OTP")
public class TbStateOtp implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id	
	@Column(name="STATE_OTP_ID")
	private Long stateOtpId;
	
	@Column (name="STATE_OTP_DETA")
	private String stateOtpDeta;
	
	
	
	public TbStateOtp(){
		super();
	}



	public Long getStateOtpId() {
		return stateOtpId;
	}



	public void setStateOtpId(Long stateOtpId) {
		this.stateOtpId = stateOtpId;
	}



	public String getStateOtpDeta() {
		return stateOtpDeta;
	}



	public void setStateOtpDeta(String stateOtpDeta) {
		this.stateOtpDeta = stateOtpDeta;
	}

}
