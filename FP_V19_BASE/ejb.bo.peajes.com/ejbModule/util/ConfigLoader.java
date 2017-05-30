/**
 * 
 */
package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.xml.sax.InputSource;

/**
 * @author Frances Zucchet
 * 
 */
public class ConfigLoader implements Serializable {
	private static final long serialVersionUID = 2112589853288554893L;

	// private String pathFile = "C:\\Sun\\SDK\\domains\\domain1\\config\\";
	private String path;

	private SAXBuilder builder = new SAXBuilder();

	private Map<String, TaskDef> taskDefinitions = new HashMap<String, TaskDef>();

	private Map<String, EMailDef> eMailDefinitions;

	private Element root;

	private Properties properties;

	public ConfigLoader() {
	}
	
	public Map<String, TaskDef> processTaskDefinition() {
		return processTaskDefinition(getDoc("task"));
	}
	
	public Map<String, TaskDef> processTaskDefinition(Document taskDefFile) {
		TaskDef taskDef = null;
		try {
			root = taskDefFile.getRootElement();
			
			for (Object object : root.getChildren("task")) {
				taskDef = new TaskDef();
				List<Step> steps = new ArrayList<Step>();
				Element element = (Element) object;

				taskDef.setIdTask(element.getAttributeValue("id"));
				
				int type = Integer.parseInt(element.getAttributeValue("type"));
				taskDef.setType(type);
				taskDef.setNameTask(element.getAttributeValue("name"));
				taskDef.setMailTo(element.getChild("mail-to").getTextTrim());
				taskDef.setDependsOn(element.getAttributeValue("dependsOn"));
				taskDef.setConditionalState(element.getAttributeValue("conditionalState"));
				
				for (Object object1 : element.getChildren("step")) {
					Element element1 = (Element) object1;
					Step step = new Step();

					String elemName = element1.getAttributeValue("name");
					step.setName(elemName != null ? elemName : "");

					for (Object o : element1.getChildren()) {
						Element e = (Element) o;
						String name = e.getName();
						if (name.equals("ads-time"))
							step.setAdsTime(Integer.parseInt(e.getValue()));
						if (name.equals("priority"))
							step.setPriority(Integer.parseInt(e.getValue()));
						if (name.equals("navigate-to"))
							step.setNavigateTo(e.getValue());
					}
					steps.add(step);
				}
				taskDef.setSteps(steps);
				taskDefinitions.put(taskDef.getIdTask(), taskDef);
			}

			for (Object object : root.getChildren("task")) {
				Element elem = (Element) object;
				for (Object obj : elem.getChildren("subtask")) {
					Element elem2 = (Element) obj;
					TaskDef taskDef2 = taskDefinitions.get(elem2.getAttributeValue("id"));
					taskDef.getSubtasks().put(taskDef2.getIdTask(), taskDef2);
				}
			}
			
			for (Object object : root.getChildren("include")) {
				Element element = (Element) object;
				String inclusion = element.getAttributeValue("src");
				String filePath = new File(".").getCanonicalPath();
				inclusion = filePath + File.separator 
					+ "xml-config" + File.separator + inclusion;
				
				FileInputStream fis = new FileInputStream(inclusion);
				Reader reader = new InputStreamReader(fis, "ISO-8859-1");
				
				InputSource source = new InputSource(reader);
				source.setEncoding("ISO-8859-1");
				
				Document docIncl = builder.build(source);
				processTaskDefinition(docIncl);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskDefinitions;
	}

	/**
	 * Process the XML file related to e-mail configuration.
	 * 
	 * @return Map with the email info.
	 */
	public Map<String, EMailDef> processEMailDefinition() {
		eMailDefinitions = new HashMap<String, EMailDef>();
		EMailDef eMailDef = null;
		try {
			Document document = getDoc("email");
			root = document.getRootElement();
			for (Object object : root.getChildren()) {
				eMailDef = new EMailDef();
				Element element = (Element) object;

				// Set E-Mail type
				eMailDef.setTypeMail(element.getAttributeValue("type"));

				// Set Name E-Mail Type
				eMailDef.setNameTypeMail(element.getAttributeValue("name"));

				Element header = element.getChild("header");

				// Set From Address
				eMailDef.setAddressFrom(header.getChild("from")
						.getChildTextTrim("address"));

				// Set From Name
				eMailDef.setNameFrom(header.getChild("from").getChildTextTrim(
						"name"));
				
				//Set To Address
				eMailDef.setAddressTo(header.getChild("to")
						.getChildTextTrim("address"));

				// Set subject
				eMailDef.setSubject(header.getChild("subject").getAttributeValue("value"));

				// Set Mail Priority
				eMailDef.setPriority(header.getChildTextTrim("priority"));

				Element body = element.getChild("body");

				// Set Array message
				List<MessageDef> message = new ArrayList<MessageDef>();
				for (Object oMssg : body.getChildren("message")) {
					MessageDef md = new MessageDef();
					md.setId(((Element) oMssg).getAttributeValue("id"));
					md.setName(((Element) oMssg).getAttributeValue("name"));
					md.setContent(((Element) oMssg).getAttributeValue("value"));
					message.add(md);
				}
				eMailDef.setMessage(message);

				// SetAttachment
				for(Object objAtt : body.getChildren("attachment")){
					Element elemAtt = (Element)objAtt;
					String filePath = elemAtt.getChildTextTrim("attachment");
					if(filePath != null)
						eMailDef.getAttachments().add(new AttachmentDef(filePath));
				}
				eMailDefinitions.put(eMailDef.getTypeMail(), eMailDef);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return eMailDefinitions;
	}

	/**
	 * Gets the Document Object depending of the format needed.
	 * 
	 * @param type
	 *            document type, example: mail, task
	 * @return a JDOM Document
	 */
	public Document getDoc(String type) {
		Document document = null;
		
		if(properties == null){
			properties = new Properties();
			try {
				FileInputStream inStream = new FileInputStream(new File(
						"config.properties"));
				properties.load(inStream);
				path = properties.getProperty("path");
	
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		try {
			FileInputStream fis = new FileInputStream(path + properties.getProperty(type));
			Reader reader = new InputStreamReader(fis, "ISO-8859-1");
			
			InputSource source = new InputSource(reader);
			source.setEncoding("ISO-8859-1");
			
			document = builder.build(source);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return document;
	}

	/**
	 * Method to save a JDom Document
	 * 
	 * @param document
	 *            Document to be saved.
	 * @param type
	 *            of XML configuration type, example: email, task, etc.
	 */
	public void saveDocument(Document document, String type) {
		try {
			Format format = Format.getPrettyFormat();
			// Use XML-style names like "UTF-8" or "ISO-8859-1" or "US-ASCII"
			format.setEncoding("ISO-8859-1");
			XMLOutputter serializer = new XMLOutputter(format);
			FileWriter writer = new FileWriter(path
					+ properties.getProperty(type));
			serializer.output(document, writer);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
