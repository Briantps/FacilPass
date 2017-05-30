package ejb.taskeng;

import javax.ejb.Remote;
import constant.TypeTask;

@Remote
public interface TransitTask {
  
	public void createTask(TypeTask tipoTarea, String idTarea);
	public boolean createTaskAccount(TypeTask tipoTarea, String idTarea);

}
