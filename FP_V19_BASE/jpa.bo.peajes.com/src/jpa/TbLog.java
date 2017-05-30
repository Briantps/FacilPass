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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *  The persistent class for the TB_LOG database table.
 * @author tmolina
 *
 */
@Entity
@Table(name="TB_LOG")
public class TbLog implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator="TB_LOG_LOGID_GENERATOR", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="TB_LOG_LOGID_GENERATOR", sequenceName="TB_LOG_SEQ", allocationSize=1)
	@Column(name="LOD_ID")
	private Long logId;
	
	@Column(name="LOG_MESSAGE")
	private String logMessage;
	
	@Column(name="LOG_REFERENCE")
	private String logReference;
	
	@Column(name="LOG_ACTION")
	private String logAction;
	
	@Column(name="LOG_DATE")
	private Timestamp logDate;
	
	@Column(name="LOG_IP")
	private String logIp;
	
	@Column(name="USER_ID")
	private Long userId;

	/**
	 * Constructor
	 */
	public TbLog(){
		super();
	}

	/**
	 * @param logId the logId to set
	 */
	public void setLogId(Long logId) {
		this.logId = logId;
	}

	/**
	 * @return the logId
	 */
	public Long getLogId() {
		return logId;
	}

	/**
	 * @param logMessage the logMessage to set
	 */
	public void setLogMessage(String logMessage) {
		this.logMessage = logMessage;
	}

	/**
	 * @return the logMessage
	 */
	public String getLogMessage() {
		return logMessage;
	}

	/**
	 * @param logReference the logReference to set
	 */
	public void setLogReference(String logReference) {
		this.logReference = logReference;
	}

	/**
	 * @return the logReference
	 */
	public String getLogReference() {
		return logReference;
	}

	/**
	 * @param logAction the logAction to set
	 */
	public void setLogAction(String logAction) {
		this.logAction = logAction;
	}

	/**
	 * @return the logAction
	 */
	public String getLogAction() {
		return logAction;
	}

	/**
	 * @param logDate the logDate to set
	 */
	public void setLogDate(Timestamp logDate) {
		this.logDate = logDate;
	}

	/**
	 * @return the logDate
	 */
	public Timestamp getLogDate() {
		return logDate;
	}

	/**
	 * @param logIp the logIp to set
	 */
	public void setLogIp(String logIp) {
		this.logIp = logIp;
	}

	/**
	 * @return the logIp
	 */
	public String getLogIp() {
		return logIp;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}
}