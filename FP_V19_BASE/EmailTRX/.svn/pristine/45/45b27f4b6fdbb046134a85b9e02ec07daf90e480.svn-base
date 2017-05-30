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
@Table(name="TB_OUTBOX")
public class TbOutbox implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TB_OUTBOX_SEQ_GENERATOR")
	@SequenceGenerator(name = "TB_OUTBOX_SEQ_GENERATOR", sequenceName = "TB_OUTBOX_SEQ", allocationSize=1)
	@Column(name="OUTBOX_ID")
	private Long outboxId;
	
	@ManyToOne
	@JoinColumn(name="USER_ID")
	private TbSystemUser tbSystemUser;
	
	@ManyToOne
	@JoinColumn(name="EMAIL_TYPE_ID")
	private TbEmailType tbEmailType;
	
	@Column(name="OUTBOX_MESSAGE")
	private String outboxMessage;
	
	@ManyToOne
	@JoinColumn(name="OUTBOX_STATE_ID")
	private TbOutboxState tbOutboxState;
	
	@Column(name="OUTBOX_CREATE_DATE")
	private Timestamp outboxCreate;
	
	@Column(name="OUTBOX_SEND_DATE")
	private Timestamp outboxSend;
	
	@Column(name="OUTBOX_SEND_LIST")
	private String outboxSendList;

	public void setOutboxId(Long outboxId) {
		this.outboxId = outboxId;
	}

	public Long getOutboxId() {
		return outboxId;
	}

	public void setTbSystemUser(TbSystemUser tbSystemUser) {
		this.tbSystemUser = tbSystemUser;
	}

	public TbSystemUser getTbSystemUser() {
		return tbSystemUser;
	}

	public void setTbEmailType(TbEmailType tbEmailType) {
		this.tbEmailType = tbEmailType;
	}

	public TbEmailType getTbEmailType() {
		return tbEmailType;
	}

	public void setTbOutboxState(TbOutboxState tbOutboxState) {
		this.tbOutboxState = tbOutboxState;
	}

	public TbOutboxState getTbOutboxState() {
		return tbOutboxState;
	}

	public void setOutboxCreate(Timestamp outboxCreate) {
		this.outboxCreate = outboxCreate;
	}

	public Timestamp getOutboxCreate() {
		return outboxCreate;
	}

	public void setOutboxSend(Timestamp outboxSend) {
		this.outboxSend = outboxSend;
	}

	public Timestamp getOutboxSend() {
		return outboxSend;
	}

	public void setOutboxMessage(String outboxMessage) {
		this.outboxMessage = outboxMessage;
	}

	public String getOutboxMessage() {
		return outboxMessage;
	}

	public void setOutboxSendList(String outboxSendList) {
		this.outboxSendList = outboxSendList;
	}

	public String getOutboxSendList() {
		return outboxSendList;
	}
}
