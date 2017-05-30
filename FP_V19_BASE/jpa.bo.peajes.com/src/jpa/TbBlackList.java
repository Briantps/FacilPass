/**
 * 
 */
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
 * The persistent class for the TB_BLACK_LIST database table.
 * @author tmolina
 *
 */
@Entity
@Table(name="TB_BLACK_LIST")
public class TbBlackList implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator="TB_BLACK_LIST_BLACKLISTID_GENERATOR", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="TB_BLACK_LIST_BLACKLISTID_GENERATOR", sequenceName="TB_BLACK_LIST_SEQ",allocationSize=1)
	@Column(name="BLACK_LIST_ID")
	private Long blackListId;
	
	@Column(name="BLACK_LIST_DATE")
	private Timestamp blackListDate;
	
	@Column(name="BLACK_LIST_OFFICIAL_NUMBER")
	private String blackListOfficialNumber;
	
	@Column(name="BLACK_LIST_OFFICIAL_DATE")
	private Timestamp blackListOfficialDate;
	
	@Column(name="REPORTED")
	private String reported;
	
	@Column(name="RETURNED")
	private String returned;
	
	@ManyToOne
	@JoinColumn(name="DEVICE_ID")
	private TbDevice tbDevice;
	
	/**
	 * Constructor.
	 */
	public TbBlackList() {
		super();
	}

	/**
	 * @param blackListId the blackListId to set
	 */
	public void setBlackListId(Long blackListId) {
		this.blackListId = blackListId;
	}

	/**
	 * @return the blackListId
	 */
	public Long getBlackListId() {
		return blackListId;
	}

	/**
	 * @param blackListDate the blackListDate to set
	 */
	public void setBlackListDate(Timestamp blackListDate) {
		this.blackListDate = blackListDate;
	}

	/**
	 * @return the blackListDate
	 */
	public Timestamp getBlackListDate() {
		return blackListDate;
	}

	/**
	 * @param blackListOfficialNumber the blackListOfficialNumber to set
	 */
	public void setBlackListOfficialNumber(String blackListOfficialNumber) {
		this.blackListOfficialNumber = blackListOfficialNumber;
	}

	/**
	 * @return the blackListOfficialNumber
	 */
	public String getBlackListOfficialNumber() {
		return blackListOfficialNumber;
	}

	/**
	 * @param blackListOfficialDate the blackListOfficialDate to set
	 */
	public void setBlackListOfficialDate(Timestamp blackListOfficialDate) {
		this.blackListOfficialDate = blackListOfficialDate;
	}

	/**
	 * @return the blackListOfficialDate
	 */
	public Timestamp getBlackListOfficialDate() {
		return blackListOfficialDate;
	}

	/**
	 * @param reported the reported to set
	 */
	public void setReported(String reported) {
		this.reported = reported;
	}

	/**
	 * @return the reported
	 */
	public String getReported() {
		return reported;
	}

	/**
	 * @param returned the returned to set
	 */
	public void setReturned(String returned) {
		this.returned = returned;
	}

	/**
	 * @return the returned
	 */
	public String getReturned() {
		return returned;
	}

	/**
	 * @param tbDevice the tbDevice to set
	 */
	public void setTbDevice(TbDevice tbDevice) {
		this.tbDevice = tbDevice;
	}

	/**
	 * @return the tbDevice
	 */
	public TbDevice getTbDevice() {
		return tbDevice;
	}
}