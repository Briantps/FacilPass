package ejb.taskeng.actions;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import jpa.TbSystemUser;
import jpa.TbTask;
import constant.ProcessTrackDetailType;
import constant.ProcessTrackType;
import ejb.Process;
import ejb.taskeng.util.ParamList;
import ejb.taskeng.workflow.Step;
import ejb.taskeng.workflow.Task;

/**
 * Implementation EJB of NotificarCorreo.
 * 
 * @author Mauricio Gil
 */
@Stateless(mappedName = "ejb/CreateProcess")
public class CreateProcessEJB implements CreateProcess {

	@PersistenceContext(unitName = "bo")
	private EntityManager em;

	@EJB(mappedName = "ejb/Process")
	private Process process;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// @EJB(mappedName = "ejb/sendMail")
	// private SendMail mailHandler;

	public CreateProcessEJB() {
	}

	@Override
	public int execute(TbTask taskReg, Task task, Step data, String params) {
		Map<String, Object> stParams = ParamList.getMap(data);
		TbSystemUser user = taskReg.getUser();

		if (user != null) {
			String notCreateIf = (String) stParams.get("notCreateIf");
			int procDetailID = Integer.valueOf(notCreateIf).intValue();
			
			if (procDetailID != taskReg.getReferencedId().intValue()) {
				Class<ProcessTrackType> enumType = ProcessTrackType.class;
				String procTypeName = String.valueOf(stParams.get("procType"));
				ProcessTrackType ptt = ProcessTrackType.valueOf(enumType, procTypeName);
				
				String hostAddress;
				try {
					hostAddress = InetAddress.getLocalHost().getHostAddress();
				} catch (UnknownHostException e) {
					e.printStackTrace();
					hostAddress = "";
				}
				
				Long procId = process.createProcessTrack(ptt, taskReg
						.getTaskId(), hostAddress,
						taskReg.getUser().getUserId());

				String procDetailTypeName = String.valueOf(stParams.get("procDetailType"));
				
				ProcessTrackDetailType ptdt = ProcessTrackDetailType
						.valueOf(ProcessTrackDetailType.class, procDetailTypeName);
				
				process.createProcessDetail(procId, ptdt,
						String.valueOf(stParams.get("message_"+ taskReg.getReferencedId().intValue())), user.getUserId(),
						hostAddress, 0,
						"No se pudo crear proceso desde tarea programada. ID="
								+ taskReg.getTaskId(),null,null,null,null);
			} else {
				taskReg.setTaskActive(false);
				taskReg.setTaskCloseDate(new Timestamp(System.currentTimeMillis()));
				em.merge(taskReg);
				em.flush();
			}
		} else {
		}
		return 1;
	}

}
