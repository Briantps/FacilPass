package jpa;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The persistent class for the TR_RE_NOTIFIER database table.
 * 
 */
@Entity
@Table(name="TR_RE_NOTIFIER")
public class TrReNotifier implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TR_RE_NOTIFIER_TRREID_GENERATOR", sequenceName="TR_RE_NOTIFIER_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TR_RE_NOTIFIER_TRREID_GENERATOR")
	@Column(name="TR_RE_ID", unique=true, nullable=false, precision=19)
	private long trReId;

	@Column(name="NOTIFICATION_STATUS", nullable=false, precision=1)
	private BigDecimal notificationStatus;

	//bi-directional many-to-one association to TbReceiver
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="RECEIVER_ID", nullable=false)
	private TbReceiver tbReceiver;

	//bi-directional many-to-one association to TbTransaction
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="TRANSACTION_ID", nullable=false)
	private TbTransaction tbTransaction;

	/**
	 * Constructor
	 */
    public TrReNotifier() {
    }

    /**
     * 
     * @return trReId
     */
	public long getTrReId() {
		return this.trReId;
	}

	/**
	 * Setter for trReId
	 * @param trReId
	 */
	public void setTrReId(long trReId) {
		this.trReId = trReId;
	}

	/**
	 * 
	 * @return notificationStatus
	 */
	public BigDecimal getNotificationStatus() {
		return this.notificationStatus;
	}

	/**
	 * Setter for notificationStatus
	 * @param notificationStatus
	 */
	public void setNotificationStatus(BigDecimal notificationStatus) {
		this.notificationStatus = notificationStatus;
	}

	/**
	 * 
	 * @return tbReceiver
	 */
	public TbReceiver getTbReceiver() {
		return this.tbReceiver;
	}

	/**
	 * Setter for tbReceiver
	 * @param tbReceiver
	 */
	public void setTbReceiver(TbReceiver tbReceiver) {
		this.tbReceiver = tbReceiver;
	}
	
	/**
	 * 
	 * @return
	 */
	public TbTransaction getTbTransaction() {
		return this.tbTransaction;
	}

	/**
	 * 
	 * @param tbTransaction
	 */
	public void setTbTransaction(TbTransaction tbTransaction) {
		this.tbTransaction = tbTransaction;
	}
}