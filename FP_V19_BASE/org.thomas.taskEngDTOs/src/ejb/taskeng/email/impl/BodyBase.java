package ejb.taskeng.email.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ejb.taskeng.email.Attachment;
import ejb.taskeng.email.Body;
import ejb.taskeng.email.Message;


/**
 * Implementacion de Body. El mapa de mensajes interno es de la clase HashMap,
 * y la lista de archivos adjuntos es de la clase ArrayList.
 * @author Mauricio Gil
 */
public class BodyBase implements Body {
	
	private static final long serialVersionUID = 2190623250284245571L;
	
	private Map<Integer, Message> messages = new HashMap<Integer, Message>();
	private List<Attachment> attachments = new ArrayList<Attachment>();
	
	public BodyBase(){
	}
	
	public void addMessage(Message e) {
		if (!messages.containsKey(e.getId())) {
			messages.put(e.getId(), e);
		}
	}

	public Message getMessage(int id) {
		return messages.get(id);
	}

	public void setMessages(Map<Integer, Message> messages) {
		this.messages = messages;
	}

	public Map<Integer, Message> getMessages() {
		return messages;
	}

	public void addAttachment(Attachment e){
		if(!attachments.contains(e)){
			attachments.add(e);
		}
	}
	
	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	public List<Attachment> getAttachments() {
		return attachments;
	}

}
