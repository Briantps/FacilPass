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
 *  The persistent class for the TB_PROCESS_TRACK database table.
 * @author tmolina
 *
 */
@Entity
@Table(name="TB_PROCESS_TRACK")
public class TbProcessTrack implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="TB_PROCESS_TRACK_PROCESSTRACKID_GENERATOR")
	@SequenceGenerator(name="TB_PROCESS_TRACK_PROCESSTRACKID_GENERATOR", sequenceName="TB_PROCESS_TRACK_SEQ",allocationSize=1)
	@Column(name="PROCESS_TRACK_ID")
	private Long processTrackId;
	
	@Column(name="PROCESS_TRACK_CREATION_DATE")
	private Timestamp processTrackCreationDate;
	
	@Column(name="PROCESS_TRACK_STATE")
	private Integer processTrackState;
	
	@ManyToOne
	@JoinColumn(name="PROCESS_TRACK_TYPE_ID")
	private TbProcessTrackType tbProcessTrackType;
	
	@Column(name="PROCESS_TRACK_REFERENCED_ID")
	private Long processTrackReferencedId;
	
	
	/**
	 * Constructor
	 */
	public TbProcessTrack(){
		super();
	}

	/**
	 * @param processTrackId the processTrackId to set
	 */
	public void setProcessTrackId(Long processTrackId) {
		this.processTrackId = processTrackId;
	}

	/**
	 * @return the processTrackId
	 */
	public Long getProcessTrackId() {
		return processTrackId;
	}

	/**
	 * @param processTrackCreationDate the processTrackCreationDate to set
	 */
	public void setProcessTrackCreationDate(Timestamp processTrackCreationDate) {
		this.processTrackCreationDate = processTrackCreationDate;
	}

	/**
	 * @return the processTrackCreationDate
	 */
	public Timestamp getProcessTrackCreationDate() {
		return processTrackCreationDate;
	}

	/**
	 * @param processTrackState the processTrackState to set
	 */
	public void setProcessTrackState(Integer processTrackState) {
		this.processTrackState = processTrackState;
	}

	/**
	 * @return the processTrackState
	 */
	public Integer getProcessTrackState() {
		return processTrackState;
	}

	/**
	 * @param tbProcessTrackType the tbProcessTrackType to set
	 */
	public void setTbProcessTrackType(TbProcessTrackType tbProcessTrackType) {
		this.tbProcessTrackType = tbProcessTrackType;
	}

	/**
	 * @return the tbProcessTrackType
	 */
	public TbProcessTrackType getTbProcessTrackType() {
		return tbProcessTrackType;
	}

	/**
	 * @param processTrackReferencedId the processTrackReferencedId to set
	 */
	public void setProcessTrackReferencedId(Long processTrackReferencedId) {
		this.processTrackReferencedId = processTrackReferencedId;
	}

	/**
	 * @return the processTrackReferencedId
	 */
	public Long getProcessTrackReferencedId() {
		return processTrackReferencedId;
	}

}