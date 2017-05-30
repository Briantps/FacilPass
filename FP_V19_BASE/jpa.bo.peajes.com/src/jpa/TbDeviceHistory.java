/**
 * 
 */
package jpa;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * The persistent class for the TB_DEVICE_HISTORY database table.
 * @author jromero
 */
@Entity
@Table(name="TB_DEVICE_HISTORY")
public class TbDeviceHistory implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TBDEVICEHISTORY_DEVICEHISTOID_GENERATOR")
	@SequenceGenerator(name = "TBDEVICEHISTORY_DEVICEHISTOID_GENERATOR", sequenceName = "TB_DEVICE_HISTORY_SEQ", allocationSize=1)
	@Column(name = "DEVICE_HISTORY_ID")
	private Long deviceHistoryId;
	
	@ManyToOne
	@JoinColumn(name="DEVICE_ID")
	private TbDevice deviceId;
	
	@Column(name="LAST_STATE_DEVICE")
	private Long lastStateDevice;
	
	@Column(name="LAST_STATE_TAG")
	private Long lastStateTag;
	
	@Column(name="BALANCE_DEVICE")
	private BigDecimal balanceDevice;
	
	@Column(name="REGISTRATION_DATE")
	private Timestamp registrationDate;

	/**
	 * Constructor. 
	 */
	public TbDeviceHistory() {
		super();
	}

	public Long getDeviceHistoryId() {
		return deviceHistoryId;
	}

	public void setDeviceHistoryId(Long deviceHistoryId) {
		this.deviceHistoryId = deviceHistoryId;
	}

	public TbDevice getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(TbDevice deviceId) {
		this.deviceId = deviceId;
	}

	public Long getLastStateDevice() {
		return lastStateDevice;
	}

	public void setLastStateDevice(Long lastStateDevice) {
		this.lastStateDevice = lastStateDevice;
	}

	public Long getLastStateTag() {
		return lastStateTag;
	}

	public void setLastStateTag(Long lastStateTag) {
		this.lastStateTag = lastStateTag;
	}

	public BigDecimal getBalanceDevice() {
		return balanceDevice;
	}

	public void setBalanceDevice(BigDecimal balanceDevice) {
		this.balanceDevice = balanceDevice;
	}

	public Timestamp getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Timestamp registrationDate) {
		this.registrationDate = registrationDate;
	}
}