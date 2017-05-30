package mBeans;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import jpa.TbSystemUser;
import ejb.taskeng.ConfigTaskEng;
import ejb.taskeng.workflow.Action;
import ejb.taskeng.workflow.ActionParam;
import ejb.taskeng.workflow.Step;
import ejb.taskeng.workflow.Task;
import ejb.taskeng.workflow.TaskDefinition;
import ejb.taskeng.workflow.impl.ActionBase;
import ejb.taskeng.workflow.impl.ActionParamBase;
import ejb.taskeng.workflow.impl.StepBase;
import ejb.taskeng.workflow.impl.TaskBase;

public class ConfigTaskEngBean implements Serializable {

	private static final long serialVersionUID = 7365971012264062158L;

	@EJB(mappedName="ejb/ConfigTaskEng")
	private ConfigTaskEng configTaskEng;
	
	@EJB(mappedName="ejb/TaskEngine")
	private ejb.taskeng.TaskEngine taskEng;
	
	@EJB(mappedName="ejb/User")
	private ejb.User userBean;
	
	private List<TbSystemUser> availableUsers = new ArrayList<TbSystemUser>();
	private List<Long> selectedUsers = new ArrayList<Long>();
	private List<Long> selectedMails = new ArrayList<Long>();
	private List<Task> taskList = new ArrayList<Task>();
	
	private String newTaskAssignedUsers;
	private String newTaskAssignedMails;
	private int currentTaskDefID;
	
	private File curTaskFile;
	private TaskDefinition curTaskDef;
	private Task currentTask;
	private Task newTask;
	private Step curTaskStep;
	private Step newTaskStep;
	
	private ActionParam curStepParam;
	private ActionParam newStepParam;
	private Action newAction;

	private boolean renderedCurStepDialog = true;
	
	public ConfigTaskEngBean(){
		newTask = new TaskBase();
		newTaskStep = new StepBase();
		newAction = new ActionBase();
		newTaskStep.getActions().add(newAction);
		
		newStepParam = new ActionParamBase();
	}
	
	@PostConstruct
	public void init(){
		System.out.println("ConfigTaskEngBean.init");
		curTaskFile = configTaskEng.getCurrentFile();
		curTaskDef = configTaskEng.getCurrentTaskDefinition();
		currentTask = configTaskEng.getCurrentTask();
		curTaskStep = configTaskEng.getCurTaskStep();
		taskList = getCurTaskDefTasks();
		resetUserList();
	}
	
	public String getInit(){
		init();
		return "";
	}
	
	public void setInit(String s){
	}
	
	public boolean getRenderedCurStepDialog(){
		return renderedCurStepDialog;
	}
	
	public void setRenderedCurStepDialog(boolean renderedCurStepDialog){
		this.renderedCurStepDialog = renderedCurStepDialog;
	}
	
	private void resetUserList() {
		System.out.println("ConfigTaskEngBean.resetUserList");
		availableUsers = userBean.getAllUsers();
	}
	
	private String getActionParam(String key) {
		return FacesContext.getCurrentInstance().
				getExternalContext().getRequestParameterMap().get(key);
	}
	
	public void onClickTask(ActionEvent e){
		System.out.println("ConfigTaskEngBean.onClickTask");
		Integer itemId = Integer.parseInt(getActionParam("itemId"));
		
		currentTask = curTaskDef.getTask(itemId);
		curTaskStep = currentTask.getFirst();
		System.out.println("Selected Task: " + itemId);
	}
	
	public void onClickEditTask(ActionEvent e){
		System.out.println("ConfigTaskEngBean.onClickEditTask");
		Integer itemId = Integer.parseInt(getActionParam("itemId"));
		currentTask = curTaskDef.getTask(itemId);
		System.out.println("Task: " + currentTask);
		curTaskStep = currentTask.getFirst();
		
		resetUserList();
		
		if(currentTask != null){
			System.out.println("CurrentTask Assigned Users: " + currentTask.getAssignedTo());
			List<TbSystemUser> users = configTaskEng.getUsers(currentTask.getAssignedTo());
			System.out.println("TbSystemUser's: " + users.size());
			
			for (TbSystemUser usr : users) {
				selectedUsers.add(usr.getUserId());
			}
			
		} else {
			System.out.println("ConfigTaskEngBean.onClickEditTask. No current task.");
		}
	}
	
	public Object onClickDelTask(){
		if(currentTask != null){
			System.out.println("ConfigTaskEngBean.onClickDelTask. Current Task: " + currentTask.getName());
			
			System.out.println("CurTaskDef size before remove: " + curTaskDef.getTasks().size());
			curTaskDef.getTasks().remove(currentTask.getId());
			System.out.println("CurTaskDef size after remove: " + curTaskDef.getTasks().size());
			
			currentTask = curTaskDef.getFirst();
			if(currentTask != null){
				curTaskStep = currentTask.getFirst();
			} else {
				curTaskStep = null;
			}
		} else {
			System.out.println("ConfigTaskEngBean.onClickDelTask. No current task.");
		}
		
		return "";
	}
	
	public void onClickEditStep(ActionEvent e){
		System.out.println("ConfigTaskEngBean.onClickEditStep");
		Integer itemId = Integer.parseInt(getActionParam("itemId"));

		curTaskStep = (Step) currentTask.getSteps().get(itemId).clone();
		
		System.out.println("selected step name: " + curTaskStep.getName());
		System.out.println("step's action name: " + curTaskStep.getActions().get(0).getName());
	}
	
	public Object onClickDelStep(){
		System.out.println("ConfigTaskEngBean.onClickDelStep");
		if(curTaskStep != null){
			System.out.println("ConfigTaskEngBean.onClickDelStep. Step to delete: " + curTaskStep.getId());
			currentTask.getSteps().remove(curTaskStep.getId());
			curTaskStep = currentTask.getFirst();
		}
		return "";
	}
	
	public String getAssignedUsrsFromList(){
		System.out.println("ConfigTaskEngBean.getAssignedUsrsFromList");
		String assignedTo = null;
		
		Iterator<Long> it = selectedUsers.iterator();
		
		if (it.hasNext()) {
			assignedTo = String.valueOf(it.next());
			
			while (it.hasNext()) {
				assignedTo += "," + it.next();
			}
		}
		
		return assignedTo;
	}
	
	public List<SelectItem> getAvailableMails(){
		List<SelectItem> selList = new ArrayList<SelectItem>();
		for (TbSystemUser usr : availableUsers) {
			selList.add(new SelectItem(usr.getUserId(), usr.getUserEmail()));
		}
		return selList;
	}
	
	public String getAssignedMails(){
		System.out.println("ConfigTaskEngBean.getAssignedMails");
		String assignedTo = "";
		
		Iterator<Long> it = selectedMails.iterator();
		
		if (it.hasNext()) {
			TbSystemUser usr = this.userBean.getSystemUser(it.next());
			assignedTo = usr.getUserEmail();
			
			while (it.hasNext()) {
				usr = this.userBean.getSystemUser(it.next());
				assignedTo += "," + usr.getUserEmail();
			}
		}
		
		return assignedTo;
	}
	
	public List<TbSystemUser> getListFromAssignedUsrs(String usrs){
		return configTaskEng.getUsers(usrs);
	}
	
	public void onClickStoreStepForCurTask(ActionEvent e){
		System.out.println("ConfigTaskEngBean.onClickStoreStepForCurTask");
		currentTask.getSteps().put(curTaskStep.getId(), curTaskStep);
	}
	
	public Object onClickStoreUsrsForCurTask(){
		if(selectedUsers.size() > 0){
			currentTask.setAssignedTo(getAssignedUsrsFromList());
			configTaskEng.setCurrentTask(currentTask);
		}
		return "";
	}
	
	public Object onClickStoreUsersForNewTask(){
		System.out.println("ConfigTaskEngBean.onClickStoreUsersForNewTask");
		newTaskAssignedUsers = getAssignedUsrsFromList();
		
		System.out.println("ConfigTaskEngBean.onClickStoreUsersForNewTask" +
				". newTaskAssignedUsers: " + newTaskAssignedUsers);
		
		newTask.setAssignedTo(newTaskAssignedUsers);
		return "";
	}
	
	public Object onClickStoreMailsForCurTask(){
		System.out.println("ConfigTaskEngBean.onClickStoreMailsForCurTask");
		currentTask.setMailTo(getAssignedMails());
		return "";
	}
	
	public Object onClickStoreMailsForNewTask(){
		System.out.println("ConfigTaskEngBean.onClickStoreMailsForNewTask");
		newTaskAssignedMails = getAssignedMails();
		newTask.setMailTo(newTaskAssignedMails);
		return "";
	}
	
	public void valueChangeFileSelection(ValueChangeEvent e){
		List<TaskDefinition> taskDefs = configTaskEng.getTaskDefinitions();
		curTaskDef = taskDefs.get(currentTaskDefID);
	}
	
	public List<SelectItem> getTaskFiles(){
		return configTaskEng.getTaskFiles();
	}
	
	public String onClickStoreTaskDefinition(){
		System.out.println("Current file: " + curTaskFile.getName());
		configTaskEng.storeTaskDef(curTaskDef, curTaskFile);
		
		taskEng.init();
		return "";
	}
	
	public Object onClickAddNewTask(){
		System.out.println("ConfigTaskEngBean.onClickAddNewTask");
		newTask.setAssignedTo(getAssignedUsrsFromList());
		newTask.setMailTo(getAssignedMails());
		
		System.out.println("Task: " + newTask);
		
		System.out.println("Size of curTaskDef before add: " + curTaskDef.getTasks().size());
		curTaskDef.addTask((Task) newTask.clone());
		System.out.println("Size of curTaskDef after add: " + curTaskDef.getTasks().size());
		
		selectedUsers.clear();
		resetUserList();
		
		currentTask = newTask;
		newTask = new TaskBase();
		return "";
	}

	public Object onClickChangeTask(){
		curTaskDef.getTasks().put(currentTask.getId(), currentTask);
		selectedUsers.clear();
		resetUserList();
		return "";
	}
	
	public Object onClickAddNewStep(){
		System.out.println("ConfigTaskEngBean.onClickAddNewStep");
		
		int size = currentTask.getSteps().size();
		System.out.println("MBean. Current Task size before add: " + size);
		currentTask.addStep((Step) newTaskStep.clone());
		
		size = currentTask.getSteps().size();
		System.out.println("MBean. Current Task size after add: " + size);
		
		newTaskStep = new StepBase();
		newAction = new ActionBase();
		newTaskStep.getActions().add(newAction);
		return "";
	}
	
	public void setCurrentTaskDefinition(TaskDefinition currentTaskDefinition){
		curTaskDef = currentTaskDefinition;
	}

	public TaskDefinition getCurrentTaskDefinition(){
		return curTaskDef;
	}

	public List<SelectItem> getAvailableProcTypes() {
		return configTaskEng.getAvailableProcTypes();
	}

	public void setCurrentTask(Task currentTask) {
		this.currentTask = currentTask;
	}

	public Task getCurrentTask() {
		return this.currentTask;
	}

	public List<SelectItem> getAvailableActions() {
		return configTaskEng.getAvailableActions();
	}
	
	public List<Task> getCurTaskDefTasks(){
		System.out.println("ConfigTaskEngEJB.getCurTaskDefTasks");
		if (curTaskDef != null) {
			Map<Integer, Task> map = curTaskDef.getTasks();
			Set<Integer> keys = map.keySet();

			taskList.clear();
			for (Integer key : keys) {
				taskList.add(map.get(key));
			}

			System.out.println("Number of tasks: " + taskList.size());
			return taskList;
		} else {
			System.out
					.println("No current task definition for getCurTaskDefTasks");
		}

		return new ArrayList<Task>();
	}
	
	public List<Step> getCurTaskSteps(){
		System.out.println("ConfigTaskEngBean.getCurTaskSteps");

		if (currentTask != null) {
			Map<Integer, Step> map = currentTask.getSteps();
			Set<Integer> keys = map.keySet();

			List<Step> steps = new ArrayList<Step>();
			for (Integer key : keys) {
				steps.add(map.get(key));
			}

			System.out.println("Number of steps: " + steps.size());
			return steps;
		} else {
			System.out.println("No current task object for getCurTaskSteps");
		}

		return new ArrayList<Step>();
	}
	
	public void onClickEditCurStepParam(ActionEvent e){
		System.out.println("ConfigTaskEngBean.onClickEditCurStepParam");
		
		String name = getActionParam("itemName");
		System.out.println("From a4j:actionparam - itemName: " + name);
		
		ArrayList<ActionParam> params = curTaskStep.getFirst().getParams();
		for (ActionParam p : params) {
			if(p.getName().equals(name)){
				System.out.println("ConfigTaskEngBean.onClickEditCurStepParam. Name: " 
						+ p.getName() + " Value: "
						+ p.getValue());
				
				curStepParam = (ActionParam) p.clone();
				break;
			}
		}
	}
	
	public void onClickEditNewStepParam(ActionEvent e){
		System.out.println("ConfigTaskEngBean.onClickEditNewStepParam");
		
	}
	
	public Object onClickStoreCurStepParam(){
		configTaskEng.setCurStepParam(curStepParam);
		return "";
	}
	
	public Object onClickStoreNewStepParam(){
		System.out.println("ConfigTaskEngBean.onClickStoreNewStepParam");
		
		System.out.println("Old parameter count: " + newAction.getParams().size());
		System.out.println("Inserting param - name: " + newStepParam.getName()
				+ " value: " + newStepParam.getValue());
		
		newAction.addParam(newStepParam);
		System.out.println("New parameter count: " + newAction.getParams().size());
		return "";
	}
	
	public Object onClickDelCurStepParam(){
		System.out.println("ConfigTaskEngBean.onClickDelCurStepParam");
		if(curStepParam != null){
			Action uniqueAction = curTaskStep.getFirst();
			ArrayList<ActionParam> params = uniqueAction.getParams();
			params.remove(curStepParam);
			
			curStepParam = uniqueAction.getFirst();
		}
		else
			System.out.println("Error getting current parameter");
		
		return "";
	}
	
	public Object onClickChangeCurStep(){
		currentTask.getSteps().put(curTaskStep.getId(), curTaskStep);
		return "";
	}
	
	public void onClickUpdateCurStep(ActionEvent e){
		curTaskStep = configTaskEng.getCurTaskStep();
	}
	
	public Object onClickDelNewStepParam(){
		System.out.println("ConfigTaskEngBean.onClickDelNewStepParam");
		ArrayList<ActionParam> params = newAction.getParams();
		params.remove(newStepParam);
		return "";
	}
	
	public void setCurTaskStep(Step curTaskStep){
		this.curTaskStep = curTaskStep;
	}

	public Step getCurTaskStep(){
		return curTaskStep;
	}

	public List<SelectItem> getSelectionForAvailableUsers(){
		List<SelectItem> items = new ArrayList<SelectItem>();
		for(TbSystemUser usr : availableUsers){
			items.add(new SelectItem(usr.getUserId(), usr.getUserNames()));
		}
		
		return items;
	}
	
	public void setAvailableUsers(List<TbSystemUser> availableUsers) {
		this.availableUsers = availableUsers;
	}

	public List<TbSystemUser> getAvailableUsers() {
		return availableUsers;
	}
	
	public List<SelectItem> getProcessTypes(){
		return configTaskEng.getAvailableProcTypes();
	}

	public void setSelectedUsers(List<Long> selectedUsers) {
		this.selectedUsers = selectedUsers;
	}

	public List<Long> getSelectedUsers() {
		return selectedUsers;
	}

	public void setCurrentFile(File currentFile){
		configTaskEng.setCurrentFile(currentFile);
	}

	public File getCurrentFile(){
		return configTaskEng.getCurrentFile();
	}

	
	public void setNewTaskAssignedUsers(String newTaskAssignedUsers) {
		this.newTaskAssignedUsers = newTaskAssignedUsers;
	}

	public String getNewTaskAssignedUsers() {
		return newTaskAssignedUsers;
	}

	public void setCurStepParam(ActionParam curStepParam) {
		this.curStepParam = curStepParam;
	}

	public ActionParam getCurStepParam() {
		return curStepParam;
	}

	public void setNewAction(Action newAction) {
		this.newAction = newAction;
	}

	public Action getNewAction() {
		return newAction;
	}

	public void setNewStepParam(ActionParam newStepParam) {
		this.newStepParam = newStepParam;
	}

	public ActionParam getNewStepParam() {
		return newStepParam;
	}

	public void setNewTaskStep(Step newTaskStep) {
		this.newTaskStep = newTaskStep;
	}

	public Step getNewTaskStep() {
		return newTaskStep;
	}

	public void setSelectedMails(List<Long> selectedMails) {
		this.selectedMails = selectedMails;
	}

	public List<Long> getSelectedMails() {
		return selectedMails;
	}

	public void setNewTaskAssignedMails(String newTaskAssignedMails) {
		this.newTaskAssignedMails = newTaskAssignedMails;
	}

	public String getNewTaskAssignedMails() {
		return newTaskAssignedMails;
	}

	public void setCurrentTaskDefID(int currentTaskDefID) {
		this.currentTaskDefID = currentTaskDefID;
	}

	public int getCurrentTaskDefID() {
		return currentTaskDefID;
	}

	public void setTaskList(List<Task> taskList) {
		this.taskList = taskList;
	}

	public List<Task> getTaskList() {
		return taskList;
	}

	public void setNewTask(Task newTask) {
		this.newTask = newTask;
	}

	public Task getNewTask() {
		return newTask;
	}
}
