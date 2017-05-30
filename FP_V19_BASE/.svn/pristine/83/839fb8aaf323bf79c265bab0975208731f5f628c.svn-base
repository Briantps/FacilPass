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
 * @author psanchez
 *
 */
@Entity
@Table(name="TB_LOG_TAG")
public class TbLogTag implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator="TB_LOG_TAG_LOGID_GENERATOR", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="TB_LOG_TAG_LOGID_GENERATOR", sequenceName="TB_LOG_TAG_SEQ", allocationSize=1)
	@Column(name="LOG_TAG_ID")
	private Long logTagId;
	
	@Column(name="LOG_TAG_ROW_COUNT")
	private Long logTagRowCount;
	
	@Column(name="LOG_TAG_ROW")
	private Long logTagRow;
	
	@Column(name="LOG_TAG_FIELD")
	private String logTagField;
	
	@Column(name="LOG_TAG_VALUE")
	private String logTagValue;

	@Column(name="LOG_TAG_FILE")
	private String logTagFile;
	
	@Column(name="LOG_TAG_DESCRIPTION")
	private String logTagDescription;
	
	@Column(name="LOG_TAG_SOLUTION")
	private String logTagSolution;
	
	@Column(name="LOG_TAG_DATE")
	private Timestamp logTagDate;
	
	@Column(name="USER_ID")
	private Long userId;

	/**
	 * Constructor
	 */
	public TbLogTag(){
		super();
	}

	public void setLogTagId(Long logTagId) {
		this.logTagId = logTagId;
	}

	public Long getLogTagId() {
		return logTagId;
	}

	public void setLogTagRowCount(Long logTagRowCount) {
		this.logTagRowCount = logTagRowCount;
	}

	public Long getLogTagRowCount() {
		return logTagRowCount;
	}

	public void setLogTagRow(Long logTagRow) {
		this.logTagRow = logTagRow;
	}

	public Long getLogTagRow() {
		return logTagRow;
	}

	public void setLogTagField(String logTagField) {
		this.logTagField = logTagField;
	}

	public String getLogTagField() {
		return logTagField;
	}

	public void setLogTagValue(String logTagValue) {
		this.logTagValue = logTagValue;
	}

	public String getLogTagValue() {
		return logTagValue;
	}

	public void setLogTagFile(String logTagFile) {
		this.logTagFile = logTagFile;
	}

	public String getLogTagFile() {
		return logTagFile;
	}
	
	public void setLogTagDescription(String logTagDescription) {
		this.logTagDescription = logTagDescription;
	}

	public String getLogTagDescription() {
		return logTagDescription;
	}

	public void setLogTagSolution(String logTagSolution) {
		this.logTagSolution = logTagSolution;
	}

	public String getLogTagSolution() {
		return logTagSolution;
	}

	public void setLogTagDate(Timestamp logTagDate) {
		this.logTagDate = logTagDate;
	}

	public Timestamp getLogTagDate() {
		return logTagDate;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getUserId() {
		return userId;
	}

	
}