package ejb;

import javax.ejb.Remote;
import javax.mail.MessagingException;

import constant.EmailSubject;
import constant.EmailType;

import jpa.TbTask;

import ejb.taskeng.workflow.Task;


@Remote
public interface SendMail {

	public void sendMail(String addressTo, String addressFrom, String nameFrom, String subject,
			String priority, String message) throws MessagingException;

	/**
	 * 
	 * @param typeEMail
	 * @param destinataries
	 * @param sujectId
	 * @param parameter
	 * @return <code>true</code> if successful, <code>false</code> otherwise.
	 */
	public boolean sendMail(EmailType typeEMail, String destinataries,
			EmailSubject sujectId, String parameter);

	/**
	 * 
	 * @param typeEmail Email Type
	 * @param destinataries
	 * @param task
	 * @param id
	 * @return
	 */
	public boolean sendMail(String typeEmail, String destinataries, Task task, int id);

	public boolean notifyAdsPastTime(TbTask taskReg, Task task);
	
	public boolean sendMailtoOutBox(String addressTo, String addressFrom, String nameFrom,
			String subject, String priority, String message) throws MessagingException;
}