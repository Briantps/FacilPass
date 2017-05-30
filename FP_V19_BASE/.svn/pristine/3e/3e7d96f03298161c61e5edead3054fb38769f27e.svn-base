package jpa;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Timestamp;

/**
 * The persistent class for the TB_ACCOUNT database table.
 * 
 */
@Entity
@Table(name="TB_ACCOUNT_CLOSE_LOG")
public class TbAccountCloseLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TB_ACCOUNT_CLOSE_LOG_GENERATOR")
	@SequenceGenerator(name = "TB_ACCOUNT_CLOSE_LOG_GENERATOR", sequenceName = "TB_ACCOUNT_CLOSE_LOG_SEQ",allocationSize=1)
	@Column(name="ACCOUNT_CLOSE_LOG_ID")
	private Long accountCloseLogId;
	
	@ManyToOne
	@JoinColumn(name="ACCOUNT_ID")
	private TbAccount tbAccount;
	
	@Column(name="OBSERVATIONS")
	private String observ;
	
	@Column(name="ACCOUNT_CLOSE_LOG_DATE")
	private Timestamp accountCloseDate;
	
	@Column(name="IP")
	private String ip;
	
	@Column(name="USER_ID")
	private Long tbSystemUser;
	
	@Column(name="ACCOUNT_CLOSE_LOG_TYPE")
	private Long accountCloseType;
	
	@Column(name="BANK_SEND_DATE")
	private Timestamp bankSendDate;
	
	@Column(name="BANK_RESPONSE_DATE")
	private Timestamp bankResponseDate;
	
	@Column(name="CODE_RESULT")
	private Long codeResult;
	
	@Column(name="ACCOUNT_CLOSE_LOG_STATE")
	private Long accountCloseState;

	public void setAccountCloseLogId(Long accountCloseLogId) {
		this.accountCloseLogId = accountCloseLogId;
	}

	public Long getAccountCloseLogId() {
		return accountCloseLogId;
	}

	public void setTbAccount(TbAccount tbAccount) {
		this.tbAccount = tbAccount;
	}

	public TbAccount getTbAccount() {
		return tbAccount;
	}

	public void setObserv(String observ) {
		this.observ = observ;
	}

	public String getObserv() {
		return observ;
	}

	public void setAccountCloseDate(Timestamp accountCloseDate) {
		this.accountCloseDate = accountCloseDate;
	}

	public Timestamp getAccountCloseDate() {
		return accountCloseDate;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getIp() {
		return ip;
	}

	public void setTbSystemUser(Long tbSystemUser) {
		this.tbSystemUser = tbSystemUser;
	}

	public Long getTbSystemUser() {
		return tbSystemUser;
	}

	public void setAccountCloseType(Long accountCloseType) {
		this.accountCloseType = accountCloseType;
	}

	public Long getAccountCloseType() {
		return accountCloseType;
	}

	public void setBankSendDate(Timestamp bankSendDate) {
		this.bankSendDate = bankSendDate;
	}

	public Timestamp getBankSendDate() {
		return bankSendDate;
	}

	public void setBankResponseDate(Timestamp bankResponseDate) {
		this.bankResponseDate = bankResponseDate;
	}

	public Timestamp getBankResponseDate() {
		return bankResponseDate;
	}

	public void setCodeResult(Long codeResult) {
		this.codeResult = codeResult;
	}

	public Long getCodeResult() {
		return codeResult;
	}

	public void setAccountCloseState(Long accountCloseState) {
		this.accountCloseState = accountCloseState;
	}

	public Long getAccountCloseState() {
		return accountCloseState;
	}
}