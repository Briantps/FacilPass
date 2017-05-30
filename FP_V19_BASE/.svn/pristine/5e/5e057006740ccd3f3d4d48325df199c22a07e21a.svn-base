package ejb.process;

import java.sql.Timestamp;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jpa.TbProcessTrack;
import jpa.TbProcessTrackDetail;
import jpa.TbProcessTrackDetailType;
import jpa.TbProcessTrackType;
import jpa.TbSystemUser;
import jpa.TbTask;
import jpa.TbTaskType;

import constant.LogAction;
import constant.LogReference;
import constant.ProcessTrackDetailType;
import constant.ProcessTrackType;
import crud.ObjectEM;
import ejb.Log;


@Stateless(mappedName = "ejb/ProcessAndTask")
public class ProcessAndTaskEJB implements ProcessAndTask {
	
	@PersistenceContext(unitName = "bo") 
	EntityManager em;
	
	@EJB(mappedName="ejb/Log")
	private Log log;
	
	private ObjectEM objectEM;
	

	
	@Override
	public Long createProcessTrack(ProcessTrackType processTrackTypeId,
			Long id, String ip, Long creationUser) {
		try {
	    	TbProcessTrackType ptt = em.find(TbProcessTrackType.class, processTrackTypeId.getId());
	    	
			TbProcessTrack process = new TbProcessTrack();
			process.setProcessTrackCreationDate(new Timestamp(System.currentTimeMillis()));
			process.setProcessTrackState(0);
			process.setTbProcessTrackType(ptt);
			process.setProcessTrackReferencedId(id);
			
			objectEM = new ObjectEM(em);	
			if(objectEM.create(process)){
				log.insertLog("Crear Proceso | Se ha creado el proceso para el Tipo: " + ptt.getProcessTrackTypeName() + " e ID: " + id + ".",
						LogReference.PROCESS, LogAction.CREATE, ip, creationUser);
				return process.getProcessTrackId();
			} else {
				log.insertLog("Crear Proceso | No se ha podido crear el proceso para el Tipo: " + ptt.getProcessTrackTypeName() + " e ID: " + id + ".",
						LogReference.PROCESS, LogAction.CREATEFAIL, ip, creationUser);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en ProcessEJB.createProcessTrack ] ");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean createTaskToProcess(Long idTask, Integer taskState,
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
			
			objectEM = new ObjectEM(em);
			
			if(objectEM.create(task)){
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

	@Override
	public Long createProcessDetail(Long processTrackId,
			ProcessTrackDetailType detailType, String message,
			Long creationUser, String ip, Integer state, String messageError) {
		try {
			TbProcessTrackDetail detail = new TbProcessTrackDetail();
			detail.setTbProcessTrack(em.find(TbProcessTrack.class, processTrackId));
			detail.setProcessTrackDetailDate(new Timestamp(System.currentTimeMillis()));
			detail.setProcessTrackDetailMessage(message);
			detail.setTbProcessTrackDetailType(em.find(TbProcessTrackDetailType.class, detailType.getId()));
			detail.setProcessTrackDetailState(state);
			
			if(creationUser != null){
				detail.setTbSystemUser(em.find(TbSystemUser.class, creationUser));
			}
			
			objectEM = new ObjectEM(em);	
			if(objectEM.create(detail)){
				Long detailL = detail.getProcessTrackDetailId();
				log.insertLog("Se ha creado el detalle del proceso ID: " + detailL, LogReference.PROCESSDETAIL, LogAction.CREATE,
						 ip, creationUser);
				return detailL ;
			} else {
				log.insertLog(messageError, LogReference.PROCESSDETAIL, LogAction.CREATEFAIL, ip, creationUser);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error en ProcessEJB.createProcessTrackDetail ] ");
		}
		return null;
	}
	
	@Override
	public TbProcessTrack searchProcess(ProcessTrackType processType, Long referencedId) {
		try {
			Query q = em
					.createQuery("SELECT pt FROM TbProcessTrack pt WHERE pt.tbProcessTrackType.processTrackTypeId = ?1 "
							+ " AND pt.processTrackReferencedId = ?2 ");
			q.setParameter(1, processType.getId());
			q.setParameter(2, referencedId);

			TbProcessTrack pt = (TbProcessTrack) q.getSingleResult();
			return pt;
		} catch (NoResultException nre) {
			System.out
					.println(" [ Error en ProcessEJB.searchProcess: No se encontraron resultados para el Id "
							+ processType.getId() + " : "+ referencedId+ ". ] ");
		} catch (Exception e) {
			System.out.println(" [ Error en ProcessEJB.searchProcess ] ");
			e.printStackTrace();
		}
		return null;
	}

}
