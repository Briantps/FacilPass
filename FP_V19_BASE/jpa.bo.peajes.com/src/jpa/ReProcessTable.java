/**
 * 
 */
package jpa;

import java.io.Serializable;

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
 * The persistent class for the RE_PROCESS_TABLE database table.
 * @author tmolina
 *
 */
@Entity
@Table(name="RE_PROCESS_TABLE")
public class ReProcessTable implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="RE_PROCESS_TABLE_PROCESSTABLEID_GENERATOR")
	@SequenceGenerator(name="RE_PROCESS_TABLE_PROCESSTABLEID_GENERATOR", sequenceName="RE_PROCESS_TABLE_SEQ",allocationSize=1)
	@Column(name="PROCESS_TABLE_ID")
	private Long processTableId;
	
	@ManyToOne
	@JoinColumn(name="PROCESS_TRACK_DETAIL_ID")
	private TbProcessTrackDetail tbProcessTrackDetail;
	
	@ManyToOne
	@JoinColumn(name="TABLE_ID")
	private TbTable tbTable;
	
	@Column(name="TABLE_REFERENCE_ID")
	private Long tableReferenceId;

	/**
	 * Constructor.
	 */
	public ReProcessTable() {
		super();
	}

	/**
	 * @param processTableId the processTableId to set
	 */
	public void setProcessTableId(Long processTableId) {
		this.processTableId = processTableId;
	}

	/**
	 * @return the processTableId
	 */
	public Long getProcessTableId() {
		return processTableId;
	}

	/**
	 * @param tbProcessTrackDetail the tbProcessTrackDetail to set
	 */
	public void setTbProcessTrackDetail(TbProcessTrackDetail tbProcessTrackDetail) {
		this.tbProcessTrackDetail = tbProcessTrackDetail;
	}

	/**
	 * @return the tbProcessTrackDetail
	 */
	public TbProcessTrackDetail getTbProcessTrackDetail() {
		return tbProcessTrackDetail;
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

	/**
	 * @param tableReferenceId the tableReferenceId to set
	 */
	public void setTableReferenceId(Long tableReferenceId) {
		this.tableReferenceId = tableReferenceId;
	}

	/**
	 * @return the tableReferenceId
	 */
	public Long getTableReferenceId() {
		return tableReferenceId;
	}
}