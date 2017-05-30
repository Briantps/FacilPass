package ejb.taskeng.workflow;

import javax.ejb.Remote;

@Remote
public interface ExpiredTaskChecker {

	public void createTimer();

}