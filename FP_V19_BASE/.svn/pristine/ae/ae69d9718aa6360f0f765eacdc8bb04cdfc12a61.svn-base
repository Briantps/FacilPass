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
@Stateless(mappedName = "ejb/NotifyProcess")
public class NotifyProcessEJB implements NotifyProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
//	@PersistenceContext(unitName = "bo")
//	private EntityManager em;
	
	@EJB(mappedName = "ejb/sendMail")
	private SendMail mailHandler;

	public NotifyProcessEJB() {
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
			
			String mailTo = task.getMailTo();
			if(!"".equals(mailTo)){
				destinataries += "," + mailTo;
			}
				
			Map<String, Object> stParams = ParamList.getMap(data);
			System.out.println("Email to send: " + destinataries);
			
			try {
				mailHandler.sendMail(destinataries, "soporteservicio@facilpass.com",
				 "FacilPass", stParams.get("subject").toString()
				 , (String) stParams.get("priority"), params);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Unable to retrieve user data to send email");
		}
		
		return 1;
	}

}
