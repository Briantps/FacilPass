package ejb.taskeng;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jpa.TbSystemUser;
import jpa.TbTaskType;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import ejb.taskeng.workflow.Action;
import ejb.taskeng.workflow.ActionParam;
import ejb.taskeng.workflow.Step;
import ejb.taskeng.workflow.Task;
import ejb.taskeng.workflow.TaskDefinition;
import ejb.taskeng.workflow.TaskFactory;
import ejb.taskeng.workflow.factory.XMLTaskFactory;
import ejb.taskeng.workflow.impl.DisplayToClassName;

/**
 * Bean implementation of Task
 * 
 * @author Mauricio Gil
 */
@Stateless(mappedName = "ejb/ConfigTaskEng")
public class ConfigTaskEngEJB implements ConfigTaskEng {

	@PersistenceContext(unitName = "bo")
	private EntityManager em;

	private TaskFactory taskFactory;
	private TaskDefinition curTaskDef;
	private File currentFile;
	private Task currentTask;
	private Step curTaskStep;
	private ActionParam curStepParam;
	
	public ConfigTaskEngEJB() {
	}

	/**
	 * Performs initialization to internal queries
	 */
	@PostConstruct
	public void init() {
		System.out.println("ConfigTaskEngEJB.init");
		taskFactory = XMLTaskFactory.getInstance();
		List<TaskDefinition> taskDefs = taskFactory.getTaskDefinitions();

		if (taskDefs != null) {

			if (taskDefs.size() > 0) {
				System.out.println("TaskDefs available: " + taskDefs.size());

				curTaskDef = taskDefs.get(0);
				currentFile = taskFactory.getTaskFiles().get(0);
				
				Map<Integer, Task> tasks = curTaskDef.getTasks();
				Set<Entry<Integer, Task>> entrySet = tasks.entrySet();

				System.out.println("For the current task definition");
				for (Entry<Integer, Task> entry : entrySet) {
					System.out.printf(String.format(
							"Task key(%d) - Task Value(%s)", entry.getKey(),
							entry.getValue().getName()));
				}

				currentTask = curTaskDef.getTask(1);

				if (currentTask != null) {
					Map<Integer, Step> curTaskSteps = currentTask.getSteps();
					if (curTaskSteps != null) {
						if (curTaskSteps.size() > 0) {
							System.out
									.println("Steps available for first task: "
											+ curTaskSteps.size());
							Set<Entry<Integer, Step>> entryStep = curTaskSteps
									.entrySet();
							for (Entry<Integer, Step> entry : entryStep) {
								System.out.println(String.format(
										"Step key(%d) - Step Value(%s)", entry
												.getKey(), entry.getValue()
												.getName()));
							}

							curTaskStep = curTaskSteps.entrySet().iterator().next().getValue();
							if(curTaskStep != null){
								List<Action> actions = curTaskStep.getActions();
								if(actions != null){
									//Solo deberia utilizarse una accion por paso,
									//pero es necesario iterar por la lista de acciones
									//en caso de que en una implementacion futura se
									//utilicen mas
									System.out.println("Actions available for first step: " + actions.size());
									for (Action action : actions) {
										System.out.println(action.getName());
										List<ActionParam> params = action.getParams();
										if(params != null){
											for (ActionParam param : params) {
												System.out.println(String.format("Param Name(%s) - " +
														"Param Value(%s)", param.getName(), param.getValue()));
											}
											
											if(!params.isEmpty()){
												curStepParam = params.iterator().next();
											}
										} else {//if(params != null)
											System.out.println("No parameters available");
										}
									}
								} else {//if(actions != null)
									System.out.println("No actions available");
								}
							} else {//if(curTaskStep != null)
								System.out.println("No current step for task selected");
							}
						} else {//if (curTaskSteps.size() > 0)
							System.out.println("Steps not available");
						}
					}
				} else {//if (curTaskSteps != null)
					System.out.println("Tasks not available");
				}
			} else {//if (currentTask != null)
				System.out.println("TaskDefs not available");
			}//if (taskDefs.size() > 0)
		}//if (taskDefs != null)
	}
	
	public void storeTaskDef(TaskDefinition td, File f){
		System.out.println("ConfigTaskEngEJB.storeTaskDef");
		Element root = new Element("task-definition");
		TaskDefinition taskDef = td;
		
		Map<Integer, Task> taskMap = taskDef.getTasks();
		for(Entry<Integer, Task> taskEntry : taskMap.entrySet()){
			Element taskElement = new Element("task");
			Task taskValue = taskEntry.getValue();
			taskElement.setAttribute(new Attribute("name", taskValue.getName()));
			taskElement.setAttribute(new Attribute("id", String.valueOf(taskValue.getId())));
			
			Element assignedToElement = new Element("assigned-to");
			assignedToElement.setText(taskValue.getAssignedTo());
			
			Element mailToElement = new Element("mail-to");
			mailToElement.setText(taskValue.getMailTo());
			
			taskElement.addContent(assignedToElement);
			taskElement.addContent(mailToElement);
						
			Map<Integer, Step> stepMap = taskEntry.getValue().getSteps();
			for(Entry<Integer, Step> stepEntry : stepMap.entrySet()){
				Element stepElement = new Element("step");
				Step stepValue = stepEntry.getValue();
				stepElement.setAttribute(new Attribute("name", stepValue.getName()));
				stepElement.setAttribute(new Attribute("id", String.valueOf(stepValue.getId())));
				
				Element stateInitElement = new Element("state-init");
				stateInitElement.setText(String.valueOf(stepValue.getStateInit()));
				stepElement.addContent(stateInitElement);
				
				List<Action> actions = stepEntry.getValue().getActions();
				for (Action action : actions) {
					Element actionElement = new Element("action");
					actionElement.setAttribute(new Attribute("name", action.getName()));
					
					List<ActionParam> params = action.getParams();
					for (ActionParam param : params) {
						Element paramElement = new Element("param");
						String name = param.getName();
						if(name == null){
							name = "";
						}
						
						paramElement.setAttribute(new Attribute("name", name));
						paramElement.setAttribute(new Attribute("value", param.getValue()));
						actionElement.addContent(paramElement);
					}
					
					stepElement.addContent(actionElement);
				}
				
				taskElement.addContent(stepElement);
			}
			
			root.addContent(taskElement);
		}
		
		Document doc = new Document(root);
		
		try {
			Format format = Format.getPrettyFormat();
			format.setEncoding("ISO-8859-1");
			
			XMLOutputter serializer = new XMLOutputter(format);
			FileWriter writer = new FileWriter(f);
			
			serializer.output(doc, writer);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		setCurrentTaskDefinition(td);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @seeejb.taskeng.ConfigTaskEng#addTaskDefinition(ejb.taskeng.workflow.
	 * TaskDefinition)
	 */
	public void addTaskDefinition(TaskDefinition td) {
		System.out.println("ConfigTaskEngEJB.addTaskDefinition");
		List<TaskDefinition> tds = taskFactory.getTaskDefinitions();
		tds.add(td);
	}

	public List<SelectItem> getTaskFiles() {
		System.out.println("ConfigTaskEngEJB.getTaskDefinitions");
		List<SelectItem> items = new ArrayList<SelectItem>();
		List<File> files = taskFactory.getTaskFiles();

		int i = 0;
		for (File file : files) {
			items.add(new SelectItem(i++, file.getName()));
		}

		System.out.println("ConfigTaskEngEJB.getTaskDefinitions(r). items:"
				+ files.size());
		return items;
	}
	
	public List<TaskDefinition> getTaskDefinitions(){
		return taskFactory.getTaskDefinitions();
	}

	@SuppressWarnings("unchecked")
	public List<TbSystemUser> getUsers(String usrIds){
		Query q = em.createQuery(String.format("From TbSystemUser u " +
				"where u.userId in (%s)", usrIds));
		
		return (List<TbSystemUser>)q.getResultList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.taskeng.ConfigTaskEng#getAvailableProcTypes()
	 */
	@SuppressWarnings("unchecked")
	public List<SelectItem> getAvailableProcTypes() {
		System.out.println("ConfigTaskEngEJB.getAvailableProcTypes");
		Query q = em.createQuery("From TbTaskType tt");
		List<TbTaskType> types = q.getResultList();

		System.out
				.println("ConfigTaskEngEJB.getAvailableProcTypes - number of types: "
						+ types.size());

		List<SelectItem> items = new ArrayList<SelectItem>();

		for (TbTaskType type : types) {
			items.add(new SelectItem(type.getTaskTypeId(), type.getTaskTypeName()));
		}

		return items;
	}
	
	public String getTaskTypeName(int id){
		Query q = em.createQuery("select tt from TbTaskType tt where tt.taskTypeId = :id");
		q.setParameter("id", new Long(id));
		
		TbTaskType tt = (TbTaskType)q.getSingleResult();
		return tt.getTaskTypeName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.taskeng.ConfigTaskEng#setCurrentTask(ejb.taskeng.workflow.Task)
	 */
	public void setCurrentTask(Task currentTask) {
		System.out.println("ConfigTaskEngEJB.setCurrentTask. Name: " + currentTask.getName());
		this.currentTask = currentTask;
		setCurTaskStep(currentTask.getFirst());
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.taskeng.ConfigTaskEng#getCurrentTask()
	 */
	public Task getCurrentTask() {
		System.out.println("ConfigTaskEngEJB.getCurrentTask");
		if(currentTask != null)
			return (Task) currentTask.clone();
		
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.taskeng.ConfigTaskEng#getAvailableActions()
	 */
	public List<SelectItem> getAvailableActions() {
		System.out.println("ConfigTaskEngEJB.getAvailableActions");
		DisplayToClassName nameConv = new DisplayToClassName();
		Properties props = nameConv.getProperties();

		List<SelectItem> items = new ArrayList<SelectItem>();
		Set<Entry<Object, Object>> entrySet = props.entrySet();
		
		for (Entry<Object, Object> entry : entrySet) {
			items.add(new SelectItem(entry.getValue(), entry.getValue()
					.toString()));
		}

		return items;
	}

	public void setCurrentTaskDefinition(TaskDefinition curTaskDef) {
		System.out.println("ConfigTaskEngEJB.setCurrentTaskDefinition");
		this.curTaskDef = curTaskDef;
		setCurrentTask(curTaskDef.getFirst());
	}

	public TaskDefinition getCurrentTaskDefinition() {
		System.out.println("ConfigTaskEngEJB.getCurrentTaskDefinition");
		return (TaskDefinition) curTaskDef.clone();
	}

	public void setCurTaskStep(Step curTaskStep) {
		this.curTaskStep = curTaskStep;
		Action uniqueAction = curTaskStep.getFirst();
		ActionParam firstParam = uniqueAction.getFirst();
		setCurStepParam(firstParam);
	}
	
	public Step getCurTaskStep() {
		System.out.println("ConfigTaskEngEJB.getCurTaskStep");
		if (curTaskStep != null) {
			System.out.println("ConfigTaskEngEJB.getCurTaskStep. Name: " + curTaskStep.getName());
			return (Step) curTaskStep.clone();
		}
		
		return null;
	}

	public void setCurrentFile(File currentFile) {
		this.currentFile = currentFile;
	}

	public File getCurrentFile() {
		return currentFile;
	}

	public void setCurStepParam(ActionParam curStepParam) {
		System.out.println("ConfigTaskEngEJB.setCurStepParam");
		this.curStepParam = curStepParam;
	}

	public ActionParam getCurStepParam() {
		System.out.println("ConfigTaskEngEJB.getCurStepParam");
		if (curStepParam != null) {
			return (ActionParam) curStepParam.clone();
		}
		
		return null;
	}
}
