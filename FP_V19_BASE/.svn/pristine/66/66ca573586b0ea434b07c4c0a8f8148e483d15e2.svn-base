package ejb.taskeng.workflow.impl;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TimedObject;
import javax.ejb.Timer;
import javax.ejb.TimerHandle;
import javax.ejb.TimerService;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jpa.TbTask;
import ejb.SendMail;
import ejb.taskeng.workflow.ExpiredTaskChecker;
import ejb.taskeng.workflow.Task;
import ejb.taskeng.workflow.TaskDefinition;
import ejb.taskeng.workflow.TaskFactory;
import ejb.taskeng.workflow.factory.XMLTaskFactory;

@Stateless(mappedName = "ejb/ExpiredTaskChecker")
public class ExpiredTaskCheckerEJB implements ExpiredTaskChecker, SessionBean,
		TimedObject {

	private static final long serialVersionUID = 1L;

	@PersistenceContext(unitName = "bo")
	private EntityManager em;
	
	private TaskFactory factory = XMLTaskFactory.getInstance();
	private SessionContext sc;

	@SuppressWarnings("unused")
	private InitialContext context;

	private TimerHandle timerHandle = null;
	private Timer timer;

	@EJB(mappedName = "ejb/sendMail")
	private SendMail mailHandler;
	
//	@EJB(mappedName = "ejb/TaskOnExecution")
//	private TaskOnExecution taskOnExec;

	public ExpiredTaskCheckerEJB() {
	}

	@PostConstruct
	public void init() {
		try {
			System.out.println("ExpiredTaskChecker.init");
			context = new InitialContext();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void ejbTimeout(Timer timer) {
		System.out.println("Executing timer for expired tasks");
		
		Query latestTask = em.createQuery("From TbTask ttask " +
				"Where ttask.taskActive = true " +
				"and ttask.taskCloseDate = null " +
				"and ttask.taskState != 2 " + //Estado 2 = tarea de reproceso
				"Order By ttask.taskPriority desc");
		
		List<TbTask> taskList = (List<TbTask>) latestTask.getResultList();

		if (!taskList.isEmpty()) {
			System.out.println("Inside timer for expired tasks");
			for (TbTask taskReg : taskList) {
				
				String taskDefId = String.valueOf(taskReg.getTbTaskType()
						.getTaskTypeId());
				List<TaskDefinition> taskDefs = factory.getTaskDefinitions();
				Task task = null;
				for (TaskDefinition taskDef : taskDefs) {
					task = taskDef.getTask(Integer.valueOf(taskDefId));
					if (task != null) {
						break;
					}
				}
				
				if(task == null){
					System.out.println("No task found on expired task checker " +
							"for TbTask with ID=" + taskReg.getTaskId());
					continue;
				}
				
				Timestamp taskAdsDate = taskReg.getTaskAdsDate();
				Date curDate = new Date();
				if (taskAdsDate.getTime() < curDate.getTime()) {
					Calendar cal = Calendar.getInstance();
					if(cal.get(Calendar.DAY_OF_MONTH) % 2 == 0
						&& cal.get(Calendar.HOUR_OF_DAY) == 8){
						System.out.println("Notifying expired task to user id's: "
								+ task.getAssignedTo());
						mailHandler.notifyAdsPastTime(taskReg, task);
					}
				}
			}
		}
	}

	@Override
	public void ejbActivate() throws EJBException, RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void ejbPassivate() throws EJBException, RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void ejbRemove() throws EJBException, RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSessionContext(SessionContext sc) throws EJBException,
			RemoteException {
		this.sc = sc;
	}

	@Override
	public void createTimer() {
		try {
			TimerService ts = sc.getTimerService();
			if (timer == null) {
				timer = ts.createTimer(0, 86400000L, "expiredTaskChecker");
				timerHandle = timer.getHandle();
				System.out.println("ExpiredTaskChecker timer");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}

	public void cancelTimer(String timerName) {
		try {
			TimerService ts = sc.getTimerService();
			Iterator<?> it = ts.getTimers().iterator();
			while (it.hasNext()) {
				Timer myTimer = (Timer) it.next();
				if ((myTimer.getInfo().equals(timerName))) {
					myTimer.cancel();
				}
			}
		} catch (Exception e) {
			System.out.println("Exception after cancelling timer : "
					+ e.toString());
		}
		return;
	}

	public void getTimerInfo() {
		if (timerHandle != null) {
			Timer timer = timerHandle.getTimer();
			System.out.println("Timer information: " + timer.getInfo());
		}
	}
}
