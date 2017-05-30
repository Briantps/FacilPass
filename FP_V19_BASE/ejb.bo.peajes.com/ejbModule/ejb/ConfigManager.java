package ejb;

import java.util.Map;

import javax.ejb.Remote;

import util.EMailDef;
import util.TaskDef;

@Remote
public interface ConfigManager {

	public Map<String, TaskDef> getTaskDef();

	public Map<String, EMailDef> getEmailDef();
	
	public void remove();

	public void initialize();
}
