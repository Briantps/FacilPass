package ejb.taskeng.actions;

import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.MessagingException;

import jpa.TbSystemUser;
import jpa.TbTask;
import ejb.SendMail;
import ejb.taskeng.util.ParamList;
import ejb.taskeng.workflow.Step;
import ejb.taskeng.workflow.Task;

/**
 * Implementation EJB of NotificarCorreo.
 * 
 * @author Mauricio Gil
 */
@Stateless(mappedName = "ejb/ReProcess")
public class ReProcessEJB implements ReProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@EJB(mappedName = "ejb/sendMail")
	private SendMail mailHandler;

	public ReProcessEJB() {
	}

	/**
	 * Performs the current action.
	 * @param task
	 *            Associated task to this action
	 * @param params
	 *            Map of parameters where the following keys are used: to_X has
	 *            a destinatary. X is a number that starts from 1, so "to_1" is
	 *            destinatary 1, "to_2" is destinatary 2, ... subject is the
	 *            purpose of the email to send priority is self explanatory.
	 *            Values are 1 or 2 (high priority), 3 (normal priority) and 4
	 *            or 5 (low priority). message is the content of the email
	 * @param Step
	 *            current step on task
	 */
	@Override
	public int execute(TbTask taskReg, Task task, Step data, String params) {
		TbSystemUser user = taskReg.getUser();
		if(user != null){
			String destinataries = user.getUserEmail();
			Map<String, Object> stParams = ParamList.getMap(data);
			System.out.println("Email to send by reprocess: " + destinataries);
			
			try {
				mailHandler.sendMail(destinataries, "no-reply@peajes.com",
					"Webmaster", stParams.get("subject").toString()
					, (String) stParams.get("priority")
					, "La tarea \"" + task.getName() + "\" requiere reproceso. " +
							"Causa: " + params);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Unable to retrieve user data to send email");
		}
		
		return 1;
	}

}
