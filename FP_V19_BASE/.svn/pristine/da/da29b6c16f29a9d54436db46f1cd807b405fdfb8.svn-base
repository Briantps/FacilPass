package ejb;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;

import util.ConfigLoader;
import util.EMailDef;
import util.MessageDef;

/**
 * Session Bean implementation class ConfigXMLEJB
 * @author tmolina
 */
@Stateless(mappedName = "ejb/xml")
public class ConfigXMLEJB implements ConfigXML {
	
	//File config loader
	private ConfigLoader loader;
	
	@EJB(mappedName = "ejb/ConfigManager")
	private ConfigManager configManager;

    /**
     * Default constructor. 
     */
    public ConfigXMLEJB() {
    	loader = new ConfigLoader();
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ejb.ConfigXML#editXML(util.EMailDef)
	 */
	@Override
	public void editXML(EMailDef mailDef) {
		try {
			Document doc = loader.getDoc("email");
			
			// Getting the e-mail type to edit.
			Element node = getType(mailDef.getTypeMail(), doc.getRootElement());
			
			// Setting the priority.
			node.getChild("header").getChild("priority").setText(mailDef.getPriority());
			
			// Setting address To.
			node.getChild("header").getChild("to").getChild("address").setText(mailDef.getAddressTo());
			
			// Message List
			List<MessageDef> listMsg = mailDef.getMessage();
			for (Object object: listMsg){
				MessageDef def = (MessageDef) object;
				// Setting a message
				Element e = (Element) node.getChild("body").getChildren().get(Integer.parseInt(def.getId()));
				e.getAttribute("value").setValue(def.getContent());
			}
			
			loader.saveDocument(doc,"email");
			
			configManager.remove();
			configManager.initialize();
		} catch (Exception e) {
			System.out.println(" [ Error en ConfigXMLEJB.editXML ] ");
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.ConfigXML#getEMailTypeList()
	 */
	@Override
	public List<EMailDef> getEMailTypeList() {
		List<EMailDef> lista = new ArrayList<EMailDef>();
		try {
			Iterator<Entry<String, EMailDef>> it = configManager.getEmailDef()
					.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, EMailDef> e = (Entry<String, EMailDef>) it.next();
				lista.add(e.getValue());
			}
		} catch (Exception e) {
			System.out.println(" [ Error en ConfigXMLEJB.getEMailTypeList ] ");
			e.printStackTrace();
		}
		return lista;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.ConfigXML#saveMessage(java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void saveMessage(String type, String name, String content) {
		try {
			Document doc = loader.getDoc("email");
			Element node = getType(type, doc.getRootElement());

			// The id is a consecutive starting in 0, so we get the size of the messages list
			// and that size will be the value of the id attribute for the new message node.
			String id = String.valueOf(node.getChild("body").getChildren()
					.size());

			// Creating the new message node.
			Element msg = new Element("message");
			msg.setAttribute(new Attribute("id", id));
			msg.setAttribute(new Attribute("name", name));
			msg.setAttribute(new Attribute("value", content));

			// Adding the new message node to the body node.
			node.getChild("body").addContent(msg);

			loader.saveDocument(doc, "email");

			configManager.remove();
			configManager.initialize();
		} catch (Exception e) {
			System.out.println(" [ Error en ConfigXMLEJB.saveMessage ] ");
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.ConfigXML#saveConfigEMail(util.EMailDef, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void saveConfigEMail(EMailDef def, String msg, String nameMsg) {
		try {
			Document doc = loader.getDoc("email");
			Element root = doc.getRootElement();
			
			// Email node.
			Element email = new Element("email");
			email.setAttribute(new Attribute("type", def.getTypeMail()));
			email.setAttribute(new Attribute("name", def.getNameTypeMail()));
			
			// Header Node.
			Element header = new Element("header");
			
			// Header-From
			Element from = new Element("from");
			Element addressfrom = new Element("address");
			addressfrom.addContent(def.getAddressFrom());
			Element addressfromName = new Element("name");
			addressfromName.addContent(def.getNameFrom());
			
			from.addContent(addressfrom);
			from.addContent(addressfromName);
			
			// Header-To
			Element to = new Element("to");
			Element addressTo = new Element("address");
			addressTo.addContent(def.getAddressTo());
			to.addContent(addressTo);
			
			// Header-Subject
			Element subject = new Element("subject");
			subject.setAttribute(new Attribute("id","0"));
			subject.setAttribute(new Attribute("value",def.getSubject()));
			
			// Header-Priority
			Element priority = new Element("priority");
			priority.addContent(def.getPriority());
			
			header.addContent(from);
			header.addContent(to);
			header.addContent(subject);
			header.addContent(priority);
			
			// Body Node.
			Element body = new Element("body");
			Element message = new Element("message");
			message.setAttribute(new Attribute("id", "0"));
			message.setAttribute(new Attribute("name", nameMsg));
			message.setAttribute(new Attribute("value", msg));
			body.addContent(message);
			
			// Attachment Node.
			Element attachment = new Element("attachment");
			
			email.addContent(header);
			email.addContent(body);
			email.addContent(attachment);
			
			root.addContent(email);
			
			loader.saveDocument(doc, "email");
			
			configManager.remove();
			configManager.initialize();
			
		} catch (Exception e) {
			System.out.println(" [ Error en ConfigXMLEJB.saveConfigEMail ] ");
			e.printStackTrace();
		}
	}

	/**
	 * Gets the Element depending of the required e-mail type, 
	 * example, if its user_mail or task_mail, etc.
	 * @param type Attribute.
	 * @param root Element root.
	 * @return Element.
	 */
	private Element getType(String type, Element root){
		Element node = null;
		for(Object object:root.getChildren()){
			if(  ((Element)object).getAttributeValue("type").equals(type)  ){
				node = (Element)object;
				break;
			}
		}
		return node;
	}

}
