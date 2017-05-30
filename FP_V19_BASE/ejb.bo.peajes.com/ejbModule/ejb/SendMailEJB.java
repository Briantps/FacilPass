package ejb;


import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import constant.EmailSubject;
import constant.EmailType;

import jpa.TbSystemUser;
import jpa.TbTask;
import util.EMailDef;
import util.Email;
import util.MessageDef;
import ejb.taskeng.email.EmailDef;
import ejb.taskeng.email.EmailFactory;
import ejb.taskeng.email.FromField;
import ejb.taskeng.email.Header;
import ejb.taskeng.email.Message;
import ejb.taskeng.email.SubjectField;
import ejb.taskeng.email.factory.XMLEmailFactory;
import ejb.taskeng.workflow.Task;

/**
 * Session Bean implementation class SendMail
 * @author tmolina
 */
@Stateless(mappedName = "ejb/sendMail")
public class SendMailEJB implements SendMail {

	@PersistenceContext(unitName="bo")
	private EntityManager em;

	//Object EMail that will be sending the e-mails.
	private Email mail;
	
	private EmailFactory factory = XMLEmailFactory.getInstance();
	
	@EJB(mappedName = "ejb/ConfigManager")
	private ConfigManager configManager;
		
	/**
     * Default constructor. 
     */
    public SendMailEJB() {  	
    }
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.SendMail#sendMail(java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void sendMail(String addressTo, String addressFrom, String nameFrom,
			String subject, String priority, String message) throws MessagingException {
		try {
			mail = new Email();
			mail.setTo(addressTo);
			mail.setFrom(addressFrom, nameFrom);		
			mail.setPriority(priority);
			mail.setSubject(subject);
			mail.setBody(message);
			mail.sendMsg();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String postProcessFields(String input, Task task){
		Map<String, String> keywords = new HashMap<String, String>();
		keywords.put("Nombre", task.getName());
		keywords.put("ID", String.valueOf(task.getId()));
		
		String output = input;
		Set<Entry<String, String>> entrySet = keywords.entrySet();
		for (Entry<String, String> entry : entrySet) {
			String name = entry.getKey();
			String value = entry.getValue();
			output = output.replaceAll("#"+name+"#", value);
		}
		return output;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.SendMail#sendMail(constant.EmailType, java.lang.String,
	 * constant.EmailSubject, java.lang.String)
	 */
	@Override
	public boolean sendMail(EmailType typeEmail, String destinataries, 
			EmailSubject sujectId, String parameter) {
		try {
			EMailDef emMailDef = configManager.getEmailDef().get(typeEmail.toString());
			mail = new Email();
			if (destinataries != null) {
				mail.setTo(destinataries);
			} else {
				mail.setTo(emMailDef.getAddressTo());
			}
			mail.setFrom(emMailDef.getAddressFrom(), emMailDef.getNameFrom());		
			mail.setPriority(emMailDef.getPriority());
			mail.setSubject(emMailDef.getSubject());
			
			String msg = ((MessageDef) emMailDef.getMessage().get(
					sujectId.getId())).getContent();
			
			if( parameter != null) {
				System.out.println("#PARAMETRO# "+ parameter);
				msg = msg.replaceAll("#PARAMETRO#", parameter);
			}
			
			mail.setBody(msg);
			mail.sendMsg();
			return true;
		} catch (Exception e) {
			System.out.println("[ Error al enviar el correo. ]");
			e.printStackTrace();
			return false;
		}	
	}
	
	public boolean notifyAdsPastTime(TbTask taskReg, Task task){
		String assignedUsrs = task.getAssignedTo();
		String destinataries = "";
		String[] usrIds = assignedUsrs.split(",");
		try {
		if(usrIds.length > 0){
			long id = Long.valueOf(usrIds[0]);
			TbSystemUser usr = em.find(TbSystemUser.class, id);
			
			destinataries = usr.getUserEmail();
			
			for(int i = 1; i < usrIds.length; i++){
				id = Long.valueOf(usrIds[i]); 
				usr = em.find(TbSystemUser.class, id);
				destinataries += "," + usr.getUserEmail();
			}
			
			return sendMail("task_mail", destinataries, task, 5);
		}
		} catch(NullPointerException e){
			//
		}
		return false;
	}
		
	public boolean sendMail(String typeEmail, String destinataries, Task task, int id){
		EmailDef emailDef = factory.getEmail(typeEmail);
		Header header = emailDef.getHeader();
		FromField fromField = header.getFromField();
		
		Message message = null;
		SubjectField subjectField = null;
		
		try {
			message = emailDef.getBody().getMessage(id);
			message.setContent(postProcessFields(message.getContent(), task));
			
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Error buscando el mensaje con ID=" + id + " del email tipo " + typeEmail);
			return false;
		}
		
		try {
			subjectField = header.getSubjectField(0);
			subjectField.setContent(postProcessFields(subjectField.getContent(), task));
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Error buscando el asunto con ID=0 del email tipo " + typeEmail);
			return false;
		}
		
		try {
			sendMail(destinataries, fromField.getAddress()
					, fromField.getName()
					, subjectField.getContent()
					, header.getPriority()
					, message.getContent());
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean sendMailtoOutBox(String addressTo, String addressFrom, String nameFrom,
			String subject, String priority, String message) throws MessagingException {
		try {			
			mail = new Email();
			mail.setTo(addressTo);
			mail.setFrom(addressFrom, nameFrom);		
			mail.setPriority(priority);
			mail.setSubject(subject);
			mail.setBody(message);
			mail.sendMsg();			
			return true;
		} catch (UnsupportedEncodingException e) {
			//e.printStackTrace();
			return false;
		} catch (Exception e) {
			//e.printStackTrace();
			return false;
		}
	}
}