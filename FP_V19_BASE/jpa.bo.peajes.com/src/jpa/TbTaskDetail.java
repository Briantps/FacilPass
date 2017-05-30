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
 * The persistent class for the TB_TASK_DETAIL database table.
 * 
 */
@Entity
@Table(name="TB_TASK_DETAIL")
public class TbTaskDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TB_TASK_DETAIL_TBTASKDETAILID_GENERATOR")
	@SequenceGenerator(name = "TB_TASK_DETAIL_TBTASKDETAILID_GENERATOR", sequenceName = "TB_TASK_DETAIL_SEQ",allocationSize=1)
	@Column(name="TASK_DETAIL_ID")
	private long taskDetailId;

	@Column(name="TASK_DESCRIPTION")
	private String taskDescription;

	@Column(name="TASK_DETAIL_CREATE")
	private Timestamp taskDetailCreate;

	@Column(name="TASK_DETAIL_UPDATE")
	private Timestamp taskDetailUpdate;

	//bi-directional many-to-one association to TbTask
    @ManyToOne
	@JoinColumn(name="TASK_ID")
	private TbTask tbTask;
    
    /**
     * Constructor
     */
    public TbTaskDetail() {
    	super();
    }

    /**
     * 
     * @return taskDetailId
     */
	public long getTaskDetailId() {
		return this.taskDetailId;
	}

	/**
	 * Setter for taskDetailId
	 * @param taskDetailId
	 */
	public void setTaskDetailId(long taskDetailId) {
		this.taskDetailId = taskDetailId;
	}

	/**
	 * 
	 * @return taskDescription
	 */
	public String getTaskDescription() {
		return this.taskDescription;
	}

	/**
	 * Setter for taskDescription
	 * @param taskDescription
	 */
	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}

	/**
	 * 
	 * @return taskDetailCreate
	 */
	public Timestamp getTaskDetailCreate() {
		return this.taskDetailCreate;
	}

	/**
	 * Setter for taskDetailCreate
	 * @param taskDetailCreate
	 */
	public void setTaskDetailCreate(Timestamp taskDetailCreate) {
		this.taskDetailCreate = taskDetailCreate;
	}

	/**
	 * 
	 * @return taskDetailUpdate
	 */
	public Timestamp getTaskDetailUpdate() {
		return this.taskDetailUpdate;
	}

	/**
	 * Setter for taskDetailUpdate
	 * @param taskDetailUpdate
	 */
	public void setTaskDetailUpdate(Timestamp taskDetailUpdate) {
		this.taskDetailUpdate = taskDetailUpdate;
	}

	/**
	 * 
	 * @return tbTask
	 */
	public TbTask getTbTask() {
		return this.tbTask;
	}

	/**
	 * Setter for tbTask
	 * @param tbTask
	 */
	public void setTbTask(TbTask tbTask) {
		this.tbTask = tbTask;
	}
}