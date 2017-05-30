package jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="RE_EMAIL_PARAMETER_PROCESS")
public class ReEmailParametersProcess implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="EMAIL_PARAMETER_PROCESS_ID")
	private Long userEmailProcessId;
	
	@ManyToOne
	@JoinColumn(name="EMAIL_PROCESS_ID")
	private TbEmailProcess emailProcessId;
	
	@ManyToOne
	@JoinColumn(name="EMAIL_PARAMETERS_ID")
	private TbEmailParameters emailParameterId;

	public void setUserEmailProcessId(Long userEmailProcessId) {
		this.userEmailProcessId = userEmailProcessId;
	}

	public Long getUserEmailProcessId() {
		return userEmailProcessId;
	}

	public void setEmailProcessId(TbEmailProcess emailProcessId) {
		this.emailProcessId = emailProcessId;
	}

	public TbEmailProcess getEmailProcessId() {
		return emailProcessId;
	}

	public void setEmailParameterId(TbEmailParameters emailParameterId) {
		this.emailParameterId = emailParameterId;
	}

	public TbEmailParameters getEmailParameterId() {
		return emailParameterId;
	}
	
	
	
}
