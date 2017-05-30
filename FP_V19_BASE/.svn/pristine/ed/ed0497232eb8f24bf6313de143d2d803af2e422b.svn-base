package ejb;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import util.ConfigLoader;
import util.EMailDef;
import util.TaskDef;

/**
 * Session Bean implementation class ConfigManagerEJB
 */
@Stateless(mappedName = "ejb/ConfigManager")
public class ConfigManagerEJB implements ConfigManager {

	private Map<String, TaskDef> taskDef;
	private Map<String, EMailDef> emailDef;
	private ConfigLoader configLoader;

	/**
	 * Default constructor.
	 */
	public ConfigManagerEJB() {
		//taskDef = new HashMap<String, TaskDef>();
		emailDef = new HashMap<String, EMailDef>();
		configLoader = new ConfigLoader();
		System.out.println("[ Instanciando Config Manager. ]");
	}

	@PostConstruct
	public void initialize() {
		//taskDef = configLoader.processTaskDefinition();
		emailDef = configLoader.processEMailDefinition();
	}

	/**
	 * @return the taskDef
	 */
	public Map<String, TaskDef> getTaskDef() {
		return taskDef;
	}

	/**
	 * @param taskDef The taskDef to set
	 */
	public void setTaskDef(Map<String, TaskDef> taskDef) {
		this.taskDef = taskDef;
	}

	/**
	 * @return the emailDef
	 */
	public Map<String, EMailDef> getEmailDef() {
		return emailDef;
	}

	/**
	 * @param emailDef The emailDef to set
	 */
	public void setEmailDef(Map<String, EMailDef> emailDef) {
		this.emailDef = emailDef;
	}

	/**
	 * Destroying ConfigManager.
	 */
	public void remove() {
		System.out.println("[ Invocando destrucción del Config Manager. ]");
		try {
//			taskDef.clear();
//			taskDef = null;
			emailDef.clear();
			emailDef = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}