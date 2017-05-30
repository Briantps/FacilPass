package jpa;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * The persistent class for the TB_OTP database table.
 * 
 */

@Entity
@Table(name="TB_OTP")
public class TbOtp implements Serializable{


	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="OTP_ID", unique=true, nullable=false)
	private Long otpId;
	
	@Column(name="DATE_OTP ")
	private Timestamp dateOtp;

	public TbOtp(){
		super();
	}

	public void setOtpId(Long otpId) {
		this.otpId = otpId;
	}

	public Long getOtpId() {
		return otpId;
	}

	public void setDateOtp(Timestamp dateOtp) {
		this.dateOtp = dateOtp;
	}

	public Timestamp getDateOtp() {
		return dateOtp;
	}
}
