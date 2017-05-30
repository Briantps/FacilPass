package ejb.taskeng.actions;

import java.sql.Timestamp;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import jpa.TbTask;
import ejb.taskeng.workflow.Step;
import ejb.taskeng.workflow.Task;

@Stateless(mappedName = "ejb/CloseProcess")
public class CloseProcessEJB implements CloseProcess {
	@PersistenceContext(unitName = "bo")
	private EntityManager em;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public int execute(TbTask taskReg, Task task, Step data, String params) {
		taskReg.setTaskActive(false);
		taskReg.setTaskCloseDate(new Timestamp(System.currentTimeMillis()));
		em.merge(taskReg);
		em.flush();
		
		return 1;
	}
}
