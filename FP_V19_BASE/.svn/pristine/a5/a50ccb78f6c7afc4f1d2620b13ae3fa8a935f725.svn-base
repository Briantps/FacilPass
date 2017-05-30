package jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="TB_PROCESS_TRACK_DETAIL_TYPE")
public class TbProcessTrackDetailType implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="PROCESS_TRACK_DETAIL_TYPE_ID")
	private Long processTrackDetailTypeId;
	
	@Column(name="DETAIL_TYPE_DESCRIPTION")
	private String detailTypeDescription;
	
	@Column(name="DETAIL_TYPE_PRIORITY")
	private Integer detailTypePriority;

	@Column(name="ADS_TIME")
	private Integer adsTime;
	
	@ManyToOne
	@JoinColumn(name="TASK_TYPE_ID")
	private TbTaskType tbTaskType;
	
	/**
	 * Constructor.
	 */
	public TbProcessTrackDetailType(){
		super();
	}

	/**
	 * @param processTrackDetailTypeId the processTrackDetailTypeId to set
	 */
	public void setProcessTrackDetailTypeId(Long processTrackDetailTypeId) {
		this.processTrackDetailTypeId = processTrackDetailTypeId;
	}

	/**
	 * @return the processTrackDetailTypeId
	 */
	public Long getProcessTrackDetailTypeId() {
		return processTrackDetailTypeId;
	}

	/**
	 * @param detailTypeDescription the detailTypeDescription to set
	 */
	public void setDetailTypeDescription(String detailTypeDescription) {
		this.detailTypeDescription = detailTypeDescription;
	}

	/**
	 * @return the detailTypeDescription
	 */
	public String getDetailTypeDescription() {
		return detailTypeDescription;
	}

	/**
	 * @param detailTypePriority the detailTypePriority to set
	 */
	public void setDetailTypePriority(Integer detailTypePriority) {
		this.detailTypePriority = detailTypePriority;
	}

	/**
	 * @return the detailTypePriority
	 */
	public Integer getDetailTypePriority() {
		return detailTypePriority;
	}

	/**
	 * @param adsTime the adsTime to set
	 */
	public void setAdsTime(Integer adsTime) {
		this.adsTime = adsTime;
	}

	/**
	 * @return the adsTime
	 */
	public Integer getAdsTime() {
		return adsTime;
	}

	/**
	 * @param tbTaskType the tbTaskType to set
	 */
	public void setTbTaskType(TbTaskType tbTaskType) {
		this.tbTaskType = tbTaskType;
	}

	/**
	 * @return the tbTaskType
	 */
	public TbTaskType getTbTaskType() {
		return tbTaskType;
	}
}