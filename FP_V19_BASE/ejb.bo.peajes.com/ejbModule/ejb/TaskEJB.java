package ejb;

import java.sql.Timestamp;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import jpa.TbTask;
import jpa.TbTaskType;
import constant.LogAction;
import constant.LogReference;
import crud.ObjectEM;

/**
 * Session Bean implementation class TaskEJB
 */
@Stateless(mappedName = "ejb/Task")
public class TaskEJB implements Task {

	@PersistenceContext(unitName = "bo")
	private EntityManager em;
	
	private ObjectEM objectEm;
	
	@EJB(mappedName="ejb/Log")
	private Log log;

	/**
	 * Default constructor.
	 */
	public TaskEJB() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Task#createTask(java.lang.Long, java.lang.Integer,
	 * java.sql.Timestamp, java.sql.Timestamp, java.lang.Integer,
	 * java.lang.String, java.lang.Long, java.lang.Long, java.lang.String,
	 * java.lang.Long)
	 */
	@Override
	public boolean createTask(Long idTask, Integer taskState,
			Timestamp taskCreateDate, Timestamp taskAdsDate,
			Integer taskPriority, String taskData, Long tbTaskTypeId,
			Long creationUser, String ip, Long referencedId) {
		try {
			TbTask task = new TbTask();
			task.setTaskId(idTask);
			task.setTaskState(taskState);
			task.setTaskCreateDate(taskCreateDate);
			task.setTaskAdsDate(taskAdsDate);
			task.setTaskPriority(taskPriority);
			task.setTaskData(taskData);
			task.setTbTaskType(em.find(TbTaskType.class, tbTaskTypeId));
			task.setReferencedId(referencedId);
			
			objectEm = new ObjectEM(em);
			
			if(objectEm.create(task)){
				log.insertLog(" Creación de Tarea | Se ha creado la tarea ID :" + task.getTaskId(), 
						LogReference.TASK,LogAction.CREATE, ip, creationUser);
				return true;
			} else {
				log.insertLog(" Creación de Tarea | Error al crear tarea.  ID process detail: " + idTask, 
						LogReference.TASK,LogAction.CREATEFAIL, ip, creationUser);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en TaskEJB.createTask ]");
			e.printStackTrace();
		}
		return false;
	}
}