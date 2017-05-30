package jpa;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the TB_DEVICE database table.
 * 
 */
@Entity
@Table(name="TB_DEVICE")
public class TbDevice implements Serializable {	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TB_DEVICE_DEVICEID_GENERATOR", sequenceName="TB_DEVICE_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TB_DEVICE_DEVICEID_GENERATOR")
	@Column(name="DEVICE_ID", unique=true, nullable=false, precision=19)
	private Long deviceId;

	@Column(name="DEVICE_CODE", nullable=false, length=50)
	private String deviceCode;

	@Column(name="DEVICE_CURRENT_BALANCE", nullable=false, precision=19)
	private BigDecimal deviceCurrentBalance;

	@ManyToOne
	@JoinColumn(name = "DEVICE_STATE_ID")
	private TbDeviceState tbDeviceState;
	
	//bi-directional many-to-one association to TbDeviceType
	@ManyToOne
	@JoinColumn(name="DEVICE_TYPE_ID")
	private TbDeviceType tbDeviceType;

	//bi-directional many-to-one association to TbTransaction
	@OneToMany(mappedBy="tbDevice")
	private List<TbTransaction> tbTransactions;
	
	@Column(name="DEVICE_FACIAL_ID")
	private String deviceFacialId;
	
	@Column(name="DEVICE_ENTRY_DATE")
	private Timestamp deviceEntryDate;
	
	@Column(name="DEVICE_PAID")
	private boolean devicePaid;

	/**
	 * Constructor
	 */
    public TbDevice() {
    }

    /**
     * 
     * @return deviceId
     */
	public Long getDeviceId() {
		return this.deviceId;
	}

	/**
	 * Setter for deviceId
	 * @param deviceId
	 */
	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * 
	 * @return deviceCode
	 */
	public String getDeviceCode() {
		return this.deviceCode;
	}

	/**
	 * Setter for deviceCode
	 * @param deviceCode
	 */
	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}

	/**
	 * 
	 * @return deviceCurrentBalance
	 */
	public BigDecimal getDeviceCurrentBalance() {
		return this.deviceCurrentBalance;
	}

	/**
	 * Setter for deviceCurrentBalance
	 * @param deviceCurrentBalance
	 */
	public void setDeviceCurrentBalance(BigDecimal deviceCurrentBalance) {
		this.deviceCurrentBalance = deviceCurrentBalance;
	}
	
	/**
	 * 
	 * @return tbTransactions
	 */
	public List<TbTransaction> getTbTransactions() {
		return this.tbTransactions;
	}

	/**
	 * Setter for tbTransactions
	 * @param tbTransactions
	 */
	public void setTbTransactions(List<TbTransaction> tbTransactions) {
		this.tbTransactions = tbTransactions;
	}

	/**
	 * @param tbDeviceType the tbDeviceType to set
	 */
	public void setTbDeviceType(TbDeviceType tbDeviceType) {
		this.tbDeviceType = tbDeviceType;
	}

	/**
	 * @return the tbDeviceType
	 */
	public TbDeviceType getTbDeviceType() {
		return tbDeviceType;
	}

	/**
	 * @param tbDeviceState the tbDeviceState to set
	 */
	public void setTbDeviceState(TbDeviceState tbDeviceState) {
		this.tbDeviceState = tbDeviceState;
	}

	/**
	 * @return the tbDeviceState
	 */
	public TbDeviceState getTbDeviceState() {
		return tbDeviceState;
	}

	/**
	 * @param deviceFacialId the deviceFacialId to set
	 */
	public void setDeviceFacialId(String deviceFacialId) {
		this.deviceFacialId = deviceFacialId;
	}

	/**
	 * @return the deviceFacialId
	 */
	public String getDeviceFacialId() {
		return deviceFacialId;
	}

	/**
	 * @param deviceEntryDate the deviceEntryDate to set
	 */
	public void setDeviceEntryDate(Timestamp deviceEntryDate) {
		this.deviceEntryDate = deviceEntryDate;
	}

	/**
	 * @return the deviceEntryDate
	 */
	public Timestamp getDeviceEntryDate() {
		return deviceEntryDate;
	}

	/**
	 * @param devicePaid the devicePaid to set
	 */
	public void setDevicePaid(boolean devicePaid) {
		this.devicePaid = devicePaid;
	}

	/**
	 * @return the devicePaid
	 */
	public boolean isDevicePaid() {
		return devicePaid;
	}
}