package ejb.taskeng.workflow.impl;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TimedObject;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import jpa.TbSystemUser;
import jpa.TbTask;
import ejb.taskeng.TaskEngine;
import ejb.taskeng.workflow.ActionCommand;
import ejb.taskeng.workflow.Step;
import ejb.taskeng.workflow.Task;
import ejb.taskeng.workflow.TaskFactory;
import ejb.taskeng.workflow.TaskOnExecution;
import ejb.taskeng.workflow.factory.XMLTaskFactory;

@Stateless(mappedName = "ejb/TaskOnExecution")
public class TaskOnExecutionEJB implements TaskOnExecution, SessionBean, TimedObject {

	private static final long serialVersionUID = 1L;

	@PersistenceContext(unitName = "bo")
	private EntityManager em;
	
	@SuppressWarnings("unused")
	private TaskFactory factory = XMLTaskFactory.getInstance();
	private SessionContext sc;
	
	private InitialContext context;
	private Map<TaskToExecute, Timer> timerMap = new HashMap<TaskToExecute, Timer>();

//	@EJB(mappedName = "ejb/sendMail")
//	private SendMail mailHandler;
	
	@EJB(mappedName = "ejb/TaskEngine")
	private TaskEngine taskEng;

	public TaskOnExecutionEJB() {
	}

	public Task findTask(TbTask taskReg){
		Set<Entry<TaskToExecute, Timer>> entrySet = timerMap.entrySet();
		for (Entry<TaskToExecute, Timer> entry : entrySet) {
			TaskToExecute key = entry.getKey();
			
			if(key.getTaskReg().getTaskId() == taskReg.getTaskId()){
				return key.getTask();
			}
		}
		return null;
	}
	
	@PostConstruct
	public void init() {
		try {
			context = new InitialContext();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void ejbTimeout(Timer timer) {
		try {
			System.out.println("Getting task to execute from timer");
			TaskToExecute tte = (TaskToExecute) timer.getInfo();
			timerMap.put(tte, timer);
			
			System.out.println("Getting task register");
			TbTask taskReg = tte.getTaskReg();
			
			DisplayToClassName nameConv = new DisplayToClassName();
			
			Task task = tte.getTask();
			System.out.println("Getting steps");
			
			if(task == null){
				System.out.println("No assigned task object for Task Reg ID=" + tte.getTaskReg().getTaskId());
				cancelTimer(tte);
				return;
			}
			
			Map<Integer, Step> steps = task.getSteps();
			
			
			String assignedUsrs = task.getAssignedTo();
			if (assignedUsrs != null) {
				System.out.println("Assigned usrs: " + assignedUsrs);
				String[] ids = assignedUsrs.split("\\s*,\\s*");
				List<Long> usrList = new ArrayList<Long>();
				for (String id : ids) {
					usrList.add(Long.valueOf(id));
				}
				
				System.out.println("Number of users:" + usrList.size());
				long selectedUser = taskEng.loadBalancer(usrList);
				
				System.out.println("Selected user id: " + selectedUser);
				TbSystemUser user = em.find(TbSystemUser.class, selectedUser);
				
				if (user != null) {
					System.out.println("Selected user name: "
							+ user.getUserNames());
					taskReg.setUser(user);
					em.merge(taskReg);
					em.flush();
					
				} else {
					System.out.println("Unable to retrieve user data");
					cancelTimer(tte);
					return;
				}
				
				Set<Entry<Integer, Step>> entrySet = steps.entrySet();
				for (Entry<Integer, Step> entry : entrySet) {
					Step step = entry.getValue();

					tte.setRunning(true);
					System.out.println("Executing pending task");

					String stepActionDisplayName = step.getActions().get(0)
							.getName();
					System.out.println("Action display name: " + stepActionDisplayName);
					
					if(step.getStateInit() == taskReg.getTaskState()){
						String stepActionClassName = nameConv.get(stepActionDisplayName);
						System.out.println("Step action class name: " + stepActionClassName);
						
						ActionCommand action = (ActionCommand) context
								.lookup("ejb/" + stepActionClassName);

						action.execute(taskReg, task, step, taskReg
								.getTaskData());
					}
					
					tte.setRunning(false);
					
					taskReg = em.find(TbTask.class, taskReg.getTaskId());
				}
			} else {//assignedUsrs != null
				System.out.println("No users where assigned to execute task");
			}
			
			System.out.println("Canceling timer");
			cancelTimer(tte);
		} catch (NamingException e) {
			e.printStackTrace();
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
	public void createTimer(TaskToExecute tte) {
		try {
			TimerService ts = sc.getTimerService();
			ts.createTimer(1, tte);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}

	public void cancelTimer(TaskToExecute tte){
		Timer timer = timerMap.get(tte);
		timer.cancel();
	}
	
	public void resetTimer(TaskToExecute tte){
		cancelTimer(tte);
		createTimer(tte);
	}
}
