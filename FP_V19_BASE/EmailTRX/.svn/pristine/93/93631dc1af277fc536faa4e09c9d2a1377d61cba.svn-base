package jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="TB_EMAIL_PROCESS")
public class TbEmailProcess implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="EMAIL_PROCESS_ID")
	private Long emailProcessId;
	
	@Column(name="EMAIL_PROCESS_NAME")
	private String emailProcessName;

	public void setEmailProcessName(String emailProcessName) {
		this.emailProcessName = emailProcessName;
	}

	public String getEmailProcessName() {
		return emailProcessName;
	}

	public void setEmailProcessId(Long emailProcessId) {
		this.emailProcessId = emailProcessId;
	}

	public Long getEmailProcessId() {
		return emailProcessId;
	}

	
}
