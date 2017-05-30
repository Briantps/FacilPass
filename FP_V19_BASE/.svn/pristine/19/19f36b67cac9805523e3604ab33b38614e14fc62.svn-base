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
 * Entity implementation class for Entity: ReUserEmailProcess
 * @author ablasquez
 */
@Entity
@Table(name="RE_USER_EMAIL_PROCESS")
public class ReUserEmailProcess implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ReUserEmailProcessSeq")
	@SequenceGenerator(name = "ReUserEmailProcessSeq", sequenceName = "RE_USER_EMAIL_PROCESS_SEQ", allocationSize=1)
	@Column(name="USER_EMAIL_PROCESS_ID")
	private Long userEmailProcessId;
	
	@ManyToOne
	@JoinColumn(name="USER_ID")
	private TbSystemUser tbSystemUser;
	
	@ManyToOne
	@JoinColumn(name="EMAIL_PROCESS_ID")
	private TbEmailProcess tbEmailProcess;
	
	@Column(name="USER_EMAIL_PROCESS_STATE")
	private int userEmailProcessState;
	
	@Column(name="USER_EMAIL_PROCESS_TO")
	private String userEmailProcessTo;
	
	@Column(name="MODIFICATION_DATE")
	private Timestamp modificationDate;
	
	

	public void setUserEmailProcessId(Long userEmailProcessId) {
		this.userEmailProcessId = userEmailProcessId;
	}

	public Long getUserEmailProcessId() {
		return userEmailProcessId;
	}

	public void setTbSystemUser(TbSystemUser tbSystemUser) {
		this.tbSystemUser = tbSystemUser;
	}

	public TbSystemUser getTbSystemUser() {
		return tbSystemUser;
	}

	public void setTbEmailProcess(TbEmailProcess tbEmailProcess) {
		this.tbEmailProcess = tbEmailProcess;
	}

	public TbEmailProcess getTbEmailProcess() {
		return tbEmailProcess;
	}

	public void setUserEmailProcessState(int userEmailProcessState) {
		this.userEmailProcessState = userEmailProcessState;
	}

	public int getUserEmailProcessState() {
		return userEmailProcessState;
	}

	public void setUserEmailProcessTo(String userEmailProcessTo) {
		this.userEmailProcessTo = userEmailProcessTo;
	}

	public String getUserEmailProcessTo() {
		return userEmailProcessTo;
	}

	public void setModificationDate(Timestamp modificationDate) {
		this.modificationDate = modificationDate;
	}

	public Timestamp getModificationDate() {
		return modificationDate;
	}
	
}
