package ejb.taskeng.workflow.factory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.sun.org.apache.commons.digester.Digester;

import ejb.taskeng.workflow.TaskDefinition;
import ejb.taskeng.workflow.TaskFactory;
import ejb.taskeng.workflow.impl.ActionBase;
import ejb.taskeng.workflow.impl.ActionParamBase;
import ejb.taskeng.workflow.impl.StepBase;
import ejb.taskeng.workflow.impl.TaskBase;
import ejb.taskeng.workflow.impl.TaskDefinitionBase;

public class XMLTaskFactory implements TaskFactory {
	private Digester digester = new Digester();
	private List<TaskDefinition> taskDefinitions = new ArrayList<TaskDefinition>();
	private List<File> taskFiles = new ArrayList<File>();
	
	private static TaskFactory INSTANCE;
	
	public static TaskFactory getInstance(){
		if(INSTANCE == null){
			INSTANCE = new XMLTaskFactory();
		}
		
		return INSTANCE;
	}
	
	private XMLTaskFactory(){
		try {
			String path = "tasks"+File.separator;
			File xmlConfigDir = new File(path);
			
			FilenameFilter filter = new FilenameFilter(){
				public boolean accept(File dir, String name) {
					boolean matches = name.matches("^.*\\.xml$");
					if(matches && !name.equals("mailFormat.xml")){
						String filePath = dir.getPath() + File.separator + name;
						taskFiles.add(new File(filePath));
					}
					return matches;
				}
			};
			
			digester.setClassLoader(getClass().getClassLoader());
			addRuleInstances();
			digester.setValidating(false);
			
			for(String fileName : xmlConfigDir.list(filter)){
				InputStream fis = new FileInputStream(path+File.separator+fileName);
				Reader reader = new InputStreamReader(fis, "ISO-8859-1");
				TaskDefinition taskDef = (TaskDefinition) digester.parse(reader);
				
				if(taskDef != null)
					taskDefinitions.add(taskDef);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<File> getTaskFiles() {
		return taskFiles;
	}

	/* (non-Javadoc)
	 * @see workflow.TaskFactory#getTaskDefinition(int)
	 */
	public TaskDefinition getTaskDefinition(int i){
		return taskDefinitions.get(i);
	}
	
	private void addRuleInstances() {
		digester.addObjectCreate("task-definition", TaskDefinitionBase.class);
		
		digester.addObjectCreate("task-definition/task", TaskBase.class);
		digester.addSetProperties("task-definition/task", "name", "name");
		digester.addSetProperties("task-definition/task", "id", "id");
		digester.addBeanPropertySetter("task-definition/task/assigned-to", "assignedTo");
		digester.addBeanPropertySetter("task-definition/task/mail-to", "mailTo");
		digester.addSetNext("task-definition/task", "addTask");
		
		digester.addObjectCreate("task-definition/task/step", StepBase.class);
		digester.addSetProperties("task-definition/task/step", "id", "id");
		digester.addSetProperties("task-definition/task/step", "name", "name");
		digester.addBeanPropertySetter("task-definition/task/step/state-init", "stateInit");
		digester.addSetNext("task-definition/task/step", "addStep");
				
		digester.addObjectCreate("task-definition/task/step/action", ActionBase.class);
		digester.addSetProperties("task-definition/task/step/action", "name", "name");
		digester.addSetNext("task-definition/task/step/action", "addAction");
		
		digester.addObjectCreate("task-definition/task/step/action/param", ActionParamBase.class);
		digester.addSetProperties("task-definition/task/step/action/param", "name", "name");
		digester.addSetProperties("task-definition/task/step/action/param", "value", "value");
		digester.addSetNext("task-definition/task/step/action/param", "addParam");
	}

	/* (non-Javadoc)
	 * @see workflow.TaskFactory#setTaskDefinitions(java.util.List)
	 */
	public void setTaskDefinitions(List<TaskDefinition> taskDefinitions) {
		this.taskDefinitions = taskDefinitions;
	}

	/* (non-Javadoc)
	 * @see workflow.TaskFactory#getTaskDefinitions()
	 */
	public List<TaskDefinition> getTaskDefinitions() {
		return taskDefinitions;
	}
}
