package ejb.process;

import java.sql.Timestamp;

import javax.ejb.Remote;

import jpa.TbProcessTrack;

import constant.ProcessTrackDetailType;
import constant.ProcessTrackType;

@Remote
public interface ProcessAndTask {
  
	public Long createProcessTrack(ProcessTrackType processTrackTypeId, Long id, String ip, Long creationUser);
	
	public Long createProcessDetail(Long processTrackId, ProcessTrackDetailType detailType,
			String message, Long creationUser, String ip, Integer state, String messageError);
	
	public boolean createTaskToProcess(Long idTask, Integer taskState,
			Timestamp taskCreateDate, Timestamp taskAdsDate,
			Integer taskPriority, String taskData, Long tbTaskTypeId,
			Long creationUser, String ip, Long referencedId);
	
	public TbProcessTrack searchProcess(ProcessTrackType processType, Long referencedId);
}
