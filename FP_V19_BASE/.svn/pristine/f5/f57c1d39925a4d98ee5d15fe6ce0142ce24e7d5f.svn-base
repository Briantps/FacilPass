package ejb.taskeng;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jpa.TbProcessTrackDetail;
import jpa.TbTask;
import jpa.TbTaskDetail;
import jpa.TbTaskType;
import ejb.balance.TimerBalance;
import ejb.email.TimerEmailTransaction;
import ejb.taskeng.workflow.ExpiredTaskChecker;
import ejb.taskeng.workflow.Step;
import ejb.taskeng.workflow.Task;
import ejb.taskeng.workflow.TaskDefinition;
import ejb.taskeng.workflow.TaskExecutor;
import ejb.taskeng.workflow.TaskFactory;
import ejb.taskeng.workflow.factory.XMLTaskFactory;

/**
 * BackOffice's base implementation of Task Engine
 * 
 * @author Mauricio Gil
 */
@Stateless(mappedName = "ejb/TaskEngine")
public class TaskEngineEJB implements TaskEngine {
	@PersistenceContext(unitName = "bo")
	private EntityManager em;
	
	@EJB(mappedName="ejb/TaskExecutor")
	private TaskExecutor executor;
	
	@EJB(mappedName="ejb/ExpiredTaskChecker")
	private ExpiredTaskChecker taskCheck;
	
	@EJB(mappedName="ejb/TimerBalance")
	private TimerBalance timerbalance;
	
	@EJB(mappedName="ejb/TimerEmailTransaction")
	private TimerEmailTransaction timeremailtran;
	
	private TaskFactory factory = XMLTaskFactory.getInstance();
	
	public TaskEngineEJB(){
		
	}
	
	@PostConstruct
	public void _init(){
	}
	
	/**
	 * Creates internal queries to database and initializes task
	 * executor's timer to execute tasks found in database.
	 */
	public void init(){
		System.out.println("Task engine init");
		
		executor.createTimer();
		//taskCheck.createTimer();
		timerbalance.createTimer();
		//timeremailtran.createTimer();
	}
	
	/* (non-Javadoc)
	 * @see taskeng.TaskEngine#execute(java.lang.String, java.util.Map, java.util.List)
	 */
	public boolean execute(long procId, String taskName, String params){
		//Get task definitions from factory
		List<TaskDefinition> taskDefs = factory.getTaskDefinitions();
		
		//Iterate over all task definitions and check name.
		//Once the desired task definition is found, look for a user with the less number of tasks
		//assigned and retrieve it. Initialize a new task object, set the user information and
		//store it. The associated task executor should find the new task on database to execute it
		//later
		for (TaskDefinition taskDef : taskDefs) {
			Map<Integer, Task> tasks = taskDef.getTasks();
			Set<Entry<Integer, Task>> entrySet = tasks.entrySet();
			
			for (Entry<Integer, Task> entry : entrySet) {
				Task task = entry.getValue();
				int curTaskId = task.getId();
				
				TbTaskType tbTaskType = em.find(TbTaskType.class, new Long(curTaskId));
				
				if(task.getName().equals(taskName)){
					System.out.println("Executing " + taskName + " ID:" + curTaskId);
					Map<Integer, Step> steps = task.getSteps();
					if(steps.size() == 0)
						throw new IllegalStateException("No steps found for task " + taskName);
					
					Iterator<Entry<Integer, Step>> iterator = steps.entrySet().iterator();
					Step step = iterator.next().getValue();
					
					if(step == null){
						throw new NullPointerException("Invalid first step for task " + taskName);
					}
					
					TbProcessTrackDetail ptd = em.find(TbProcessTrackDetail.class, procId);
					
					System.out.println("Creating TbTask");
					TbTask tbTask = new TbTask();
					tbTask.setTaskId(procId);
					tbTask.setTaskState(ptd.getProcessTrackDetailState());
					tbTask.setTaskCreateDate(new Timestamp(System.currentTimeMillis()));
					tbTask.setTaskPriority(ptd.getTbProcessTrackDetailType().getDetailTypePriority());
					tbTask.setTaskStateDescription(step.getName());
					tbTask.setTaskAdsDate(calculateAds(ptd.getTbProcessTrackDetailType().getAdsTime()));
					tbTask.setTbTaskType(tbTaskType);
					tbTask.setTaskActive(false);
					
					tbTask.setTaskData(params);
					
					System.out.println("Storing TbTask");
					em.persist(tbTask);
					
					System.out.println("Creating TbTaskDetail");
					TbTaskDetail detail = new TbTaskDetail();
					detail.setTbTask(tbTask);
					detail.setTaskDescription(task.getName());
					detail.setTaskDetailCreate(new Timestamp(System
							.currentTimeMillis()));

					System.out.println("Storing TbTaskDetail");
					em.persist(detail);
					em.flush();
					
					System.out.println("Succeded task execution");
					return true;
				}
			}
		}
		
		System.out.println("Failed task execution");
		return false;
	}
	
	public boolean execute(long procId, int taskId, String params){
		//Get task definitions from factory
		List<TaskDefinition> taskDefs = factory.getTaskDefinitions();
		
		//Iterate over all task definitions and check name.
		//Once the desired task definition is found, look for a user with the less number of tasks
		//assigned and retrieve it. Initialize a new task object, set the user information and
		//store it. The associated task executor should find the new task on database to execute it
		//later
		for (TaskDefinition taskDef : taskDefs) {
			Map<Integer, Task> tasks = taskDef.getTasks();
			Set<Entry<Integer, Task>> entrySet = tasks.entrySet();
			
			for (Entry<Integer, Task> entry : entrySet) {
				Task task = entry.getValue();
				int curTaskId = task.getId();
				
				TbTaskType tbTaskType = em.find(TbTaskType.class, new Long(curTaskId));
				
				if(task.getId() == taskId){
					String taskName = task.getName();
					System.out.println("Executing " + taskName + " ID:" + curTaskId);
					Map<Integer, Step> steps = task.getSteps();
					if(steps.size() == 0)
						throw new IllegalStateException("No steps found for task " + taskName);
					
					Iterator<Entry<Integer, Step>> iterator = steps.entrySet().iterator();
					Step step = iterator.next().getValue();
					
					if(step == null){
						throw new NullPointerException("Invalid first step for task " + taskName);
					}
					
					TbProcessTrackDetail ptd = em.find(TbProcessTrackDetail.class, procId);
					
					System.out.println("Creating TbTask");
					TbTask tbTask = new TbTask();
					tbTask.setTaskId(procId);
					tbTask.setTaskState(ptd.getProcessTrackDetailState());
					tbTask.setTaskCreateDate(new Timestamp(System.currentTimeMillis()));
					tbTask.setTaskPriority(ptd.getTbProcessTrackDetailType().getDetailTypePriority());
					tbTask.setTaskStateDescription(step.getName());
					tbTask.setTaskAdsDate(calculateAds(ptd.getTbProcessTrackDetailType().getAdsTime()));
					tbTask.setTbTaskType(tbTaskType);
					tbTask.setTaskActive(false);
					
					tbTask.setTaskData(params);
					
					System.out.println("Storing TbTask");
					em.persist(tbTask);
					
					System.out.println("Creating TbTaskDetail");
					TbTaskDetail detail = new TbTaskDetail();
					detail.setTbTask(tbTask);
					detail.setTaskDescription(task.getName());
					detail.setTaskDetailCreate(new Timestamp(System
							.currentTimeMillis()));

					System.out.println("Storing TbTaskDetail");
					em.persist(detail);
					em.flush();
					
					System.out.println("Succeded task execution");
					return true;
				}
			}
		}
		
		System.out.println("Failed task execution");
		return false;
	}
	
	/**
	 * Retrieves a user id with the less quantity of assigned tasks.
	 * 
	 * @param assignedUsers List of user ids
	 * @return User id
	 */
	public long loadBalancer(List<Long> assignedUsers) {
		if (assignedUsers.size() > 0) {
			int tasks = 0;
			long user = -1;
			for (Long userId : assignedUsers) {
				tasks = getOpenTaskByUser(userId);
				for (Long userId2 : assignedUsers) {
					int numOpenTasks = getOpenTaskByUser(userId2);
					if (tasks > numOpenTasks) {
						user = userId2;
					} else if (tasks < numOpenTasks) {
						tasks = numOpenTasks;
					} else if (tasks == numOpenTasks) {
						user = userId;
					}
				}
			}
			return user;
		} else {
			return -1L;
		}
	}
	
	/**
	 * Retrieves the number of tasks assigned to a given user
	 * 
	 * @param idUser Identifier of user
	 * @return Task count
	 */
	public int getOpenTaskByUser(long idUser) {
		try {
			Query taskOpenQuery;

			taskOpenQuery = em.createNativeQuery("SELECT COUNT(*) "
					+ "FROM Tb_Task task WHERE task.task_close_date is "
					+ "null AND task.user_id=?1");

			BigDecimal singleResult = (BigDecimal) taskOpenQuery.setParameter(1, idUser).getSingleResult();
			if(singleResult != null)
				return singleResult.intValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * Converts a time to complete task in days to a Timestamp representation
	 * @param adsTime Time in days
	 * @return Time in timestamp format
	 */
	private Timestamp calculateAds(int adsTime) {
		return new Timestamp(System.currentTimeMillis() + 86400000L * adsTime);
	}
}
