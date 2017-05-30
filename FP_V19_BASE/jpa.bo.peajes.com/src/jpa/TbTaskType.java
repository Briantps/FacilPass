package jpa;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Set;


/**
 * The persistent class for the TB_TASK_TYPE database table.
 * 
 */
@Entity
@Table(name="TB_TASK_TYPE")
public class TbTaskType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "TB_TASK_TYPE_TBTASKTYPEID_GENERATOR")
	@SequenceGenerator(name = "TB_TASK_TYPE_TBTASKTYPEID_GENERATOR", sequenceName = "TB_TASK_TYPE_SEQ",allocationSize=1)
	@Column(name="TASK_TYPE_ID")
	private long taskTypeId;

	@Column(name="TASK_TYPE_NAME")
	private String taskTypeName;

	//bi-directional many-to-one association to TbTask
	@OneToMany(mappedBy="tbTaskType")
	private Set<TbTask> tbTasks;

	/**
	 * Constructor
	 */
    public TbTaskType() {
    	super();
    }

    /**
     * 
     * @return taskTypeId
     */
	public long getTaskTypeId() {
		return this.taskTypeId;
	}

	/**
	 * Setter for taskTypeId
	 * @param taskTypeId
	 */
	public void setTaskTypeId(long taskTypeId) {
		this.taskTypeId = taskTypeId;
	}

	/**
	 * 
	 * @return taskTypeName
	 */
	public String getTaskTypeName() {
		return this.taskTypeName;
	}

	/**
	 * Setter for taskTypeName
	 * @param taskTypeName
	 */
	public void setTaskTypeName(String taskTypeName) {
		this.taskTypeName = taskTypeName;
	}

	/**
	 * 
	 * @return tbTasks
	 */
	public Set<TbTask> getTbTasks() {
		return this.tbTasks;
	}

	/**
	 * Setter for tbTasks
	 * @param tbTasks
	 */
	public void setTbTasks(Set<TbTask> tbTasks) {
		this.tbTasks = tbTasks;
	}
}