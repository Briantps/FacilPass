package ejb.taskeng.email.factory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.sun.org.apache.commons.digester.Digester;

import ejb.taskeng.email.EmailDef;
import ejb.taskeng.email.EmailFactory;
import ejb.taskeng.email.EmailTypes;
import ejb.taskeng.email.impl.AttachmentBase;
import ejb.taskeng.email.impl.BodyBase;
import ejb.taskeng.email.impl.EmailDefBase;
import ejb.taskeng.email.impl.EmailTypesBase;
import ejb.taskeng.email.impl.FromFieldBase;
import ejb.taskeng.email.impl.HeaderBase;
import ejb.taskeng.email.impl.MessageBase;
import ejb.taskeng.email.impl.SubjectFieldBase;

public class XMLEmailFactory implements EmailFactory {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Digester digester = new Digester();
	private EmailTypes emailTypes;
	private static EmailFactory INSTANCE;

	public static EmailFactory getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new XMLEmailFactory();
		}

		return INSTANCE;
	}

	private XMLEmailFactory() {
		String file = null;
		try {
			System.out.println("Init XMLEmailFactory");
			digester.setClassLoader(getClass().getClassLoader());
			
			System.out.println("Adding rule instances");
			addRuleInstances();
			
			digester.setValidating(false);

			String path = "tasks";
			String fileName = File.separator + "mailFormat.xml";
			file = path + fileName;
			
			System.out.println("Reading mail format from " + file);
			InputStream fis = new FileInputStream(file);
			Reader reader = new InputStreamReader(fis, "ISO-8859-1");
			
			System.out.println("Parsing email types");
			emailTypes = (EmailTypes) digester.parse(reader);

			System.out.println("email types parsed");
		} catch (SAXParseException e) {
			System.out.println("Error parsing " + file + "Line number: "
					+ e.getLineNumber() + " Column Number: "
					+ e.getColumnNumber());
			
			System.out.println(e.getLocalizedMessage());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

	private void addRuleInstances() {
		digester.addObjectCreate("email_types", EmailTypesBase.class);

		digester.addObjectCreate("email_types/email", EmailDefBase.class);
		digester.addSetProperties("email_types/email", "type", "type");
		digester.addSetNext("email_types/email", "addEmail");

		digester.addObjectCreate("email_types/email/header", HeaderBase.class);

		digester.addObjectCreate("email_types/email/header/from", FromFieldBase.class);
		digester.addBeanPropertySetter("email_types/email/header/from/address", "address");
		digester.addBeanPropertySetter("email_types/email/header/from/name", "name");

		digester.addSetNext("email_types/email/header/from", "setFromField");
		digester.addBeanPropertySetter("email_types/email/header/priority", "priority");
		digester.addSetNext("email_types/email/header", "setHeader");

		digester.addObjectCreate("email_types/email/header/subject",
				SubjectFieldBase.class);
		digester.addSetProperties("email_types/email/header/subject", "id",
				"id");
		digester.addSetProperties("email_types/email/header/subject", "value",
				"content");
		digester.addSetNext("email_types/email/header/subject",
				"addSubjectField");

		digester.addObjectCreate("email_types/email/body", BodyBase.class);

		digester.addObjectCreate("email_types/email/body/message",
				MessageBase.class);
		digester.addSetProperties("email_types/email/body/message", "id", "id");
		digester.addSetProperties("email_types/email/body/message", "value",
				"content");
		digester.addSetNext("email_types/email/body/message", "addMessage");

		digester.addObjectCreate("email_types/email/body/attachment",
				AttachmentBase.class);
		digester.addSetProperties("email_types/email/body/attachment", "value",
				"filePath");
		digester.addSetNext("email_types/email/body/attachment",
				"addAttachment");

		digester.addSetNext("email_types/email/body", "setBody");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see email.EmailFactory#setEmailTypes(email.EmailTypes)
	 */
	public void setEmailTypes(EmailTypes emailTypes) {
		this.emailTypes = emailTypes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see email.EmailFactory#getEmailTypes()
	 */
	public EmailTypes getEmailTypes() {
		return emailTypes;
	}

	public EmailDef getEmail(String type) {
		return emailTypes.getEmail(type);
	}
}
