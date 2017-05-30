package ejb;
import java.util.List;

import javax.ejb.Remote;

import util.EMailDef;

/**
 * 
 * @author tmolina
 *
 */
@Remote
public interface ConfigXML {
	
	/**
	 * 
	 * @param mailDef (type of email definition) to edit, example user_mail, task_mail, etc.
	 */
	public void editXML(EMailDef mailDef);
	
	/**
	 * 
	 * @return List of Email type definition.
	 */
	public List<util.EMailDef> getEMailTypeList();
	
	/**
	 * Save/add a new message node.
	 * 
	 * @param type Type of email definition.
	 * @param name Attribute name of the message node.
	 * @param content The message text.
	 */
	public void saveMessage(String type, String name, String content);
	
	/**
	 * Save a new type of email configuration on the XML file. 
	 * 
	 * @param def The new EMailDef to be added.
	 * @param msg The text of a message.
	 * @param nameMsg The name of a message.
	 */
	public void saveConfigEMail(EMailDef def, String msg, String  nameMsg);
}