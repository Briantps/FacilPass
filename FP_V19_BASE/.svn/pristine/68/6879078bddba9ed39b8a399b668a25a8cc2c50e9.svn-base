package ejb.taskeng.email.impl;

import java.util.HashMap;
import java.util.Map;

import ejb.taskeng.email.EmailDef;
import ejb.taskeng.email.EmailTypes;



public class EmailTypesBase implements EmailTypes {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<String, EmailDef> emails = new HashMap<String, EmailDef>();

	public EmailTypesBase(){
	}
	
	/* (non-Javadoc)
	 * @see email.EmailTypes#setEmails(java.util.List)
	 */
	public void setEmails(Map<String, EmailDef> emails) {
		this.emails = emails;
	}

	/* (non-Javadoc)
	 * @see email.EmailTypes#getEmails()
	 */
	public Map<String, EmailDef> getEmails() {
		return emails;
	}
	
	/* (non-Javadoc)
	 * @see email.EmailTypes#addEmail(email.Email)
	 */
	public void addEmail(EmailDef e){
		if(!emails.containsKey(e)){
			emails.put(e.getType(), e);
		}
	}
	
	/* (non-Javadoc)
	 * @see email.EmailTypes#getEmail(int)
	 */
	public EmailDef getEmail(String type){
		return emails.get(type);
	}
}
