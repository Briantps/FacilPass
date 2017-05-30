package jpa;

import java.io.Serializable;

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
@Table(name="TB_EMAIL_TYPE")
public class TbEmailType implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TB_EMAIL_TYPE_SEQ_GENERATOR")
	@SequenceGenerator(name = "TB_EMAIL_TYPE_SEQ_GENERATOR", sequenceName = "TB_EMAIL_TYPE_SEQ", allocationSize=1)
	@Column(name="EMAIL_TYPE_ID")
	private Long emailTypeId;
	
	@Column(name="EMAIL_TYPE_SUBJECT")
	private String emailTypeSubject;
	
	@Column(name="EMAIL_TYPE_MESSAGE")
	private String emailTypeMessage;
	
	@ManyToOne
	@JoinColumn(name = "OPT_ACT_REFERENCE_ID")
	private TbOptActRefefenceType tbReference;
	
	@ManyToOne
	@JoinColumn(name = "EMAIL_PROCESS_ID")
	private TbEmailProcess tbEmailProcess;
	
	@Column(name="EMAIL_ADDRESS_LIST")
	private String emailAddressList;
	
	@Column(name="EMAIL_STATUS")
	private int emailStatus;

	public void setEmailTypeId(Long emailTypeId) {
		this.emailTypeId = emailTypeId;
	}

	public Long getEmailTypeId() {
		return emailTypeId;
	}

	public void setEmailTypeSubject(String emailTypeSubject) {
		this.emailTypeSubject = emailTypeSubject;
	}

	public String getEmailTypeSubject() {
		return emailTypeSubject;
	}

	public void setEmailTypeMessage(String emailTypeMessage) {
		this.emailTypeMessage = emailTypeMessage;
	}

	public String getEmailTypeMessage() {
		return emailTypeMessage;
	}

	public void setTbReference(TbOptActRefefenceType tbReference) {
		this.tbReference = tbReference;
	}

	public TbOptActRefefenceType getTbReference() {
		return tbReference;
	}

		public void setTbEmailProcess(TbEmailProcess tbEmailProcess) {
		this.tbEmailProcess = tbEmailProcess;
	}

	public TbEmailProcess getTbEmailProcess() {
		return tbEmailProcess;
	}

	public void setEmailAddressList(String emailAddressList) {
		this.emailAddressList = emailAddressList;
	}

	public String getEmailAddressList() {
		return emailAddressList;
	}

	public void setEmailStatus(int emailStatus) {
		this.emailStatus = emailStatus;
	}

	public int getEmailStatus() {
		return emailStatus;
	}
	
	
}
