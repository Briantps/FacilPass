package jpa;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * The persistent class for the TB_TASK database table.
 * 
 */
@Entity
@Table(name="TB_TASK")
public class TbTask implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="TASK_ID")
	private Long taskId;
	
	@Column(name="TASK_STATE")
	private Integer taskState;
	
	@Column(name="TASK_CREATE_DATE")
	private Timestamp taskCreateDate;
	
	@Column(name="TASK_UPDATE_DATE")
	private Timestamp taskUpdateDate;
	
	@Column(name = "TASK_ADS_DATE")
	private Timestamp taskAdsDate;
	
	@Column(name="TASK_PRIORITY")
	private Integer taskPriority;

	@Column(name="TASK_CLOSE_DATE")
	private Timestamp taskCloseDate;

	@Column(name="TASK_DATA")
	private String taskData;
	
	//bi-directional many-to-one association to TbTaskType
    @ManyToOne
	@JoinColumn(name="TASK_TYPE_ID")
	private TbTaskType tbTaskType;

	//bi-directional many-to-one association to TbTaskDetail
	@OneToMany(mappedBy="tbTask", fetch=FetchType.EAGER)
	private List<TbTaskDetail> tbTaskDetails;
	
	@ManyToOne
	@JoinColumn(name="USER_ID")
	private TbSystemUser user;
	
	@Column(name="TASK_STATE_DESCRIPTION")
	private String taskStateDescription;

	@Column(name="TASK_ACTIVE")
	private boolean taskActive;
	
	@Column(name="REFERENCED_ID")
	private Long referencedId;
	
	/**
	 * Constructor
	 */
    public TbTask() {
    }

    /**
     * 
     * @return taskId
     */
	public Long getTaskId() {
		return this.taskId;
	}

	/**
	 * Setter for taskId
	 * @param taskId
	 */
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	/**
	 * 
	 * @return taskCloseDate
	 */
	public Timestamp getTaskCloseDate() {
		return this.taskCloseDate;
	}

	/**
	 * Setter for taskCloseDate
	 * @param taskCloseDate
	 */
	public void setTaskCloseDate(Timestamp taskCloseDate) {
		this.taskCloseDate = taskCloseDate;
	}

	/**
	 * 
	 * @return taskCreateDate
	 */
	public Timestamp getTaskCreateDate() {
		return this.taskCreateDate;
	}

	/**
	 * Setter for taskCreateDate
	 * @param taskCreateDate
	 */
	public void setTaskCreateDate(Timestamp taskCreateDate) {
		this.taskCreateDate = taskCreateDate;
	}

	/**
	 * 
	 * @return taskPriority
	 */
	public Integer getTaskPriority() {
		return this.taskPriority;
	}

	/**
	 * Setter for taskPriority
	 * @param taskPriority
	 */
	public void setTaskPriority(Integer taskPriority) {
		this.taskPriority = taskPriority;
	}

	/**
	 * 
	 * @return taskState
	 */
	public Integer getTaskState() {
		return this.taskState;
	}

	/**
	 * Setter for taskState
	 * @param taskState
	 */
	public void setTaskState(Integer taskState) {
		this.taskState = taskState;
	}

	/**
	 * 
	 * @return taskUpdateDate
	 */
	public Timestamp getTaskUpdateDate() {
		return this.taskUpdateDate;
	}

	/**
	 * Setter for taskUpdateDate
	 * @param taskUpdateDate
	 */
	public void setTaskUpdateDate(Timestamp taskUpdateDate) {
		this.taskUpdateDate = taskUpdateDate;
	}
	
	/**
	 * 
	 * @return tbTaskType
	 */
	public TbTaskType getTbTaskType() {
		return this.tbTaskType;
	}

	/**
	 * Setter for tbTaskType
	 * @param tbTaskType
	 */
	public void setTbTaskType(TbTaskType tbTaskType) {
		this.tbTaskType = tbTaskType;
	}

	/**
	 * @param taskAdsDate the taskAdsDate to set
	 */
	public void setTaskAdsDate(Timestamp taskAdsDate) {
		this.taskAdsDate = taskAdsDate;
	}

	/**
	 * @return the taskAdsDate
	 */
	public Timestamp getTaskAdsDate() {
		return taskAdsDate;
	}

	/**
	 * @param tbTaskDetails the tbTaskDetails to set
	 */
	public void setTbTaskDetails(List<TbTaskDetail> tbTaskDetails) {
		this.tbTaskDetails = tbTaskDetails;
	}

	/**
	 * @return the tbTaskDetails
	 */
	public List<TbTaskDetail> getTbTaskDetails() {
		return tbTaskDetails;
	}

	/**
	 * @param taskStateDescription the taskStateDescription to set
	 */
	public void setTaskStateDescription(String taskStateDescription) {
		this.taskStateDescription = taskStateDescription;
	}

	/**
	 * @return the taskStateDescription
	 */
	public String getTaskStateDescription() {
		return taskStateDescription;
	}

	public void setUser(TbSystemUser user) {
		this.user = user;
	}

	public TbSystemUser getUser() {
		return user;
	}

	public void setTaskActive(boolean taskActive) {
		this.taskActive = taskActive;
	}

	public boolean isTaskActive() {
		return taskActive;
	}

	public void setTaskData(String taskData) {
		this.taskData = taskData;
	}

	public String getTaskData() {
		return taskData;
	}

	/**
	 * @param referencedId the referencedId to set
	 */
	public void setReferencedId(Long referencedId) {
		this.referencedId = referencedId;
	}

	/**
	 * @return the referencedId
	 */
	public Long getReferencedId() {
		return referencedId;
	}
}