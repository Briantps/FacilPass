package jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="TB_OUTBOX_STATE")
public class TbOutboxState implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="OUTBOX_STATE_ID")
	private Long outboxStateId;
	
	@Column(name="OUTBOX_STATE_DESCRIPTION")
	private String outboxStateDescription;

	public void setOutboxStateId(Long outboxStateId) {
		this.outboxStateId = outboxStateId;
	}

	public Long getOutboxStateId() {
		return outboxStateId;
	}

	public void setOutboxStateDescription(String outboxStateDescription) {
		this.outboxStateDescription = outboxStateDescription;
	}

	public String getOutboxStateDescription() {
		return outboxStateDescription;
	}
}
