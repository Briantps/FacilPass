package jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="TB_PROCESS_TRACK_TYPE")
public class TbProcessTrackType implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="PROCESS_TRACK_TYPE_ID")
	private Long processTrackTypeId;
	
	@Column(name="PROCESS_TRACK_TYPE_NAME")
	private String processTrackTypeName;
	
	@ManyToOne
	@JoinColumn(name="TABLE_ID")
	private TbTable tbTable;

	/**
	 * Constructor.
	 */
	public TbProcessTrackType(){
		super();
	}

	/**
	 * @param processTrackTypeId the processTrackTypeId to set
	 */
	public void setProcessTrackTypeId(Long processTrackTypeId) {
		this.processTrackTypeId = processTrackTypeId;
	}

	/**
	 * @return the processTrackTypeId
	 */
	public Long getProcessTrackTypeId() {
		return processTrackTypeId;
	}

	/**
	 * @param processTrackTypeName the processTrackTypeName to set
	 */
	public void setProcessTrackTypeName(String processTrackTypeName) {
		this.processTrackTypeName = processTrackTypeName;
	}

	/**
	 * @return the processTrackTypeName
	 */
	public String getProcessTrackTypeName() {
		return processTrackTypeName;
	}

	/**
	 * @param tbTable the tbTable to set
	 */
	public void setTbTable(TbTable tbTable) {
		this.tbTable = tbTable;
	}

	/**
	 * @return the tbTable
	 */
	public TbTable getTbTable() {
		return tbTable;
	}
}
