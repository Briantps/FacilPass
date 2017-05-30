package jpa;

import java.io.Serializable;

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
 * The persistent class for the RE_DE_REGISTRATION database table.
 * 
 */
@Entity
@Table(name = "RE_DE_REGISTRATION")
public class ReDeRegistration implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "RE_DE_REGISTRATION_REDEREGISTRATIONID_GENERATOR", sequenceName = "RE_DE_REGISTRATION_SEQ",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RE_DE_REGISTRATION_REDEREGISTRATIONID_GENERATOR")
	@Column(name = "RE_DE_REGISTRATION_ID", unique = true, nullable = false, precision = 19)
	private long reDeRegistrationId;

	//bi-directional many-to-one association to TbDevice
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEVICE_ID", nullable = false)
	private TbDevice tbDevice;

	//bi-directional many-to-one association to TbReceiver
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="RECEIVER_ID", nullable=false)
	private TbReceiver tbReceiver;

	/**
	 * Constructor 
	 */
    public ReDeRegistration() {
    	super();
    }

    /**
     * 
     * @return reDeRegistrationId
     */
	public long getReDeRegistrationId() {
		return this.reDeRegistrationId;
	}

	/**
	 * Setter for reDeRegistrationId
	 * @param reDeRegistrationId
	 */
	public void setReDeRegistrationId(long reDeRegistrationId) {
		this.reDeRegistrationId = reDeRegistrationId;
	}

	/**
	 * 
	 * @return tbDevice
	 */
	public TbDevice getTbDevice() {
		return this.tbDevice;
	}

	/**
	 * Setter for tbDevice
	 * @param tbDevice
	 */
	public void setTbDevice(TbDevice tbDevice) {
		this.tbDevice = tbDevice;
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
}