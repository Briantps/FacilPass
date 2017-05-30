package jpa;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The persistent class for the TB_RECEIVER database table.
 * 
 */
@Entity
@Table(name="TB_RECEIVER")
public class TbReceiver implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TB_RECEIVER_RECEIVERID_GENERATOR", sequenceName="TB_RECEIVER_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TB_RECEIVER_RECEIVERID_GENERATOR")
	@Column(name="RECEIVER_ID", unique=true, nullable=false, precision=19)
	private long receiverId;

	@Column(name="RECEIVER_IPV4", nullable=false, length=15)
	private String receiverIpv4;

	@Column(name="RECEIVER_NAME", length=50)
	private String receiverName;

	@Column(name="RECEIVER_STATE", nullable=false, precision=1)
	private BigDecimal receiverState;

	//bi-directional many-to-one association to ReDeRegistration
	@OneToMany(mappedBy="tbReceiver")
	private List<ReDeRegistration> reDeRegistrations;

	//bi-directional many-to-one association to TrReNotifier
	@OneToMany(mappedBy="tbReceiver")
	private List<TrReNotifier> trReNotifiers;

	/**
	 * Constructor
	 */
    public TbReceiver() {
    	super();
    }

    /**
     * 
     * @return receiverId
     */
	public long getReceiverId() {
		return this.receiverId;
	}

	/**
	 * Setter for receiverId
	 * @param receiverId
	 */
	public void setReceiverId(long receiverId) {
		this.receiverId = receiverId;
	}

	/**
	 * 
	 * @return receiverIpv4
	 */
	public String getReceiverIpv4() {
		return this.receiverIpv4;
	}

	/**
	 * Setter for receiverIpv4
	 * @param receiverIpv4
	 */
	public void setReceiverIpv4(String receiverIpv4) {
		this.receiverIpv4 = receiverIpv4;
	}

	/**
	 * 
	 * @return receiverName
	 */
	public String getReceiverName() {
		return this.receiverName;
	}

	/**
	 * Setter for receiverName
	 * @param receiverName
	 */
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	/**
	 * 
	 * @return receiverState
	 */
	public BigDecimal getReceiverState() {
		return this.receiverState;
	}

	/**
	 * Setter for receiverState
	 * @param receiverState
	 */
	public void setReceiverState(BigDecimal receiverState) {
		this.receiverState = receiverState;
	}

	/**
	 * 
	 * @return reDeRegistrations
	 */
	public List<ReDeRegistration> getReDeRegistrations() {
		return this.reDeRegistrations;
	}

	/**
	 * Setter for reDeRegistrations
	 * @param reDeRegistrations
	 */
	public void setReDeRegistrations(List<ReDeRegistration> reDeRegistrations) {
		this.reDeRegistrations = reDeRegistrations;
	}
	
	/**
	 * 
	 * @return trReNotifiers
	 */
	public List<TrReNotifier> getTrReNotifiers() {
		return this.trReNotifiers;
	}

	/**
	 * Setter for trReNotifiers
	 * @param trReNotifiers
	 */
	public void setTrReNotifiers(List<TrReNotifier> trReNotifiers) {
		this.trReNotifiers = trReNotifiers;
	}
}