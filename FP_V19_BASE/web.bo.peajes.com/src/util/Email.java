package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Session Bean implementation class Email
 * Create a multipart message with the second block of the 
 * message being the given file for an attachment.
 */
@Stateless(mappedName = "util/email")
public class Email implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
		
	/** Static variable representing a high priority message. */
    public final static String HIGH_PRIORITY = "1";
    /** Static variable representing a normal priority message. */
    public final static String NORMAL_PRIORITY = "3";
    /** Static variable representing a low priority message. */
    public final static String LOW_PRIORITY = "5";

    MimeMessage msg = null;
    Properties props = null;
    Multipart mp = null;
    Session session =null;   

    /**
     * Create a new object to send email messages.
     */
    public Email() {
    	props = new Properties();
		try {
			props.load(new FileInputStream(new File("mail.properties")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Session session = Session.getInstance(props, new SMTPAuthenticator());
		session.setDebug(true);
        msg = new MimeMessage(session);
        mp = new MimeMultipart();
    }
    
    
    /**
     * Set who the email message is to. 
     * The to email address may be a comma separated list of email addresses so the message will be sent to multiple recipients. 
     * @param  recipient The email address of who the message is to. e.g. "admin@test.com"
     * @throws MessagingException If an error occurs.
     * @throws UnsupportedEncodingException If an error occurs.
     *
     */  
	public void setTo(String recipient) throws MessagingException,
			UnsupportedEncodingException {
    	this.setTo(recipient, null);
		
	}

	
	/**
     * Set who the email message is to. 
     * The to email address may be a comma separated list of email addresses so the message will be sent to multiple recipients. 
     * If the message is sent to multiple recipients, then the name parameter of who to send to is ignored, and the
     * displayed recipient name is the recipient email address.
     * @param recipient     The email address of who the message is to. e.g. "admin@test.com"
     * @param recipientName The textual name of who the message is to. e.g. "Administrator Name". This parameter is ignored if the to email address
     *                      is a comma separated list of multiple email recipients.
     * @throws MessagingException If an error occurs.
     * @throws UnsupportedEncodingException If an error occurs.
     */     
	public void setTo(String recipient, String recipientName)
			throws MessagingException, UnsupportedEncodingException {
		this.processRecipientList(Message.RecipientType.TO, recipient, recipientName);
		
	}
	
	/**
     * Set who is CC on the email message. 
     * The to email address may be a comma separated list of email addresses who will be CC'ed on the email.
     * @param  cc The email address of who the message is to. e.g. "admin@test.com"
     * @throws MessagingException If an error occurs.
     * @throws UnsupportedEncodingException If an error occurs.
     *
     */   
	public void setCC(String cc) throws MessagingException,
			UnsupportedEncodingException {
		this.setCC(cc, null);
		
	}

	/**
     * Set who is CC on the email message. 
     * The to email address may be a comma separated list of email addresses who will be CC'ed on the email.
     * If the message is sent to multiple recipients, then the name parameter of who to send to is ignored, and the
     * displayed recipient name is the recipient email address.
     * @param cc     The email address of who the message is to. e.g. "admin@test.com"
     * @param ccName The textual name of who the message is to. e.g. "Administrator Name". This parameter is ignored if the to email address
     *               is a comma separated list of multiple email recipients.
     * @throws MessagingException If an error occurs.
     * @throws UnsupportedEncodingException If an error occurs.
     *
     */     
	public void setCC(String cc, String ccName) throws MessagingException,
			UnsupportedEncodingException {
		this.processRecipientList(Message.RecipientType.CC, cc, ccName);
		
	}
	
	/**
     * Set who is BCC on the email message. 
     * The to email address may be a comma separated list of email addresses who will be BCC'ed on the email.
     * @param bcc The email address of who the message is to. e.g. "admin@test.com"
     * @throws MessagingException If an error occurs.
     * @throws UnsupportedEncodingException If an error occurs.
     */  
	public void setBCC(String bcc) throws MessagingException,
			UnsupportedEncodingException {
		this.setBCC(bcc, null);
		
	}

	/**
     * Set who is BCC'ed on the email message. 
     * The to email address may be a comma separated list of email addresses who will be BCC'ed on the email.
     * If the message is sent to multiple recipients, then the name parameter of who to send to is ignored, and the
     * displayed recipient name is the recipient email address.
     * @param bcc 	  The email address of who the message is to. e.g. "admin@test.com"
     * @param bccName The textual name of who the message is to. e.g. "Administrator Name". This parameter is ignored if the to email address
     *                is a comma separated list of multiple email recipients.
     * @throws MessagingException If an error occurs.
     * @throws UnsupportedEncodingException If an error occurs.	
     */   
	public void setBCC(String bcc, String bccName) throws MessagingException,
			UnsupportedEncodingException {
		this.processRecipientList(Message.RecipientType.BCC, bcc, bccName);
		
	}
	
	/**
     * Set who the email message is from.
     * @param  from The email address of who the message is from. e.g. "admin@test.com"
     * @throws MessagingException If an error occurs.
     * @throws UnsupportedEncodingException If an error occurs.
     *
     */       	
	public void setFrom(String from) throws MessagingException,
			UnsupportedEncodingException {
		this.setFrom(from, null);
		
	}

	/**
     * Set who the email message is from.
     * @param from The email address of who the message is from. e.g. "admin@test.com"
     * @param fromName The textual name of who the message is from. e.g. "Administrator Name"
     * @throws MessagingException If an error occurs.
     * @throws UnsupportedEncodingException If an error occurs.
     */  
	public void setFrom(String from, String fromName)
			throws MessagingException, UnsupportedEncodingException {
	  try {	
		 String msg_from = from;
	     String msg_from_name = fromName;
	     if (fromName != null && fromName.length() > 0) {
	    	 msg.setFrom(new InternetAddress(msg_from, msg_from_name));
	     }else{
	         msg.setFrom(new InternetAddress(msg_from));
	     }
	  } catch (Exception ex){
		  ex.printStackTrace();
	  }
	}
	
	/**
     * Set the subject of the email message.
     * @param subject The subject of the email message.
     * @throws MessagingException If an error occurs.
     */ 
	public void setSubject(String subject) throws MessagingException {
		msg.setSubject(subject);
	}
	
	/**
     * Set the body of the email message.
     * @param  body The body of the email message.
     * @throws MessagingException If an error occurs.
     */ 
	public void setBody(String body) throws MessagingException {
		MimeBodyPart mbpMsgText = new MimeBodyPart();
        mbpMsgText.setText(body);
        mp.addBodyPart(mbpMsgText);
	}

	/**
     * Set the priority of a message. 
     * 1 or 2 are high priority. 
     * 3 is normal priority. 
     * 4 or 5 are low priority. 
     * Most mail readers don't differentiate 1-2 and 4-5.
     * @param priorityValue The priority code of the message.
     * @throws Exception  If an error occurs.
     */    
	public void setPriority(String priorityValue) throws Exception {
		//if (priorityValue < 1 || priorityValue > 5) {
        //    throw new Exception ("An invalid priority value of " + priorityValue + " has been specified for the email.");
        //}
        if (priorityValue != null && priorityValue.trim().length() > 0) {
            msg.setHeader("X-Priority", priorityValue);
        }
	}

	/**
     * Add a file attachment to the email message.
     * @param  fileName The fully qualified name of the file to add to the message.
     * @throws MessagingException If an error occurs.
     */       
	public void addFileAttachment(String fileName) throws MessagingException {
		MimeBodyPart mbpFile = new MimeBodyPart();
        
		/**Attach the file to the message*/
        FileDataSource fds = new FileDataSource(fileName);
        mbpFile.setDataHandler(new DataHandler(fds));
        mbpFile.setFileName(fds.getName());
        mp.addBodyPart(mbpFile); 
	}

	/**
     * Send the email message.
     * @throws MessagingException If an error occurs.
     */   
	public void sendMsg() throws MessagingException {
		/**add the Multipart to the message*/
        msg.setContent(mp);
        /** set the Date: header*/
        msg.setSentDate(new Date());
        msg.saveChanges();
        
        session = Session.getInstance(props, new SMTPAuthenticator());
        Transport transport = session.getTransport("smtp");
        transport.connect ();
        transport.sendMessage(msg, msg.getAllRecipients());
        transport.close();
        throw new MessagingException("error-email ");	 
	}

	/**
     * Process a recipient list (To, CC, BCC) and setup the appropriate Internet addresses to send to. 
     * This method will parse a list of recipients which may be separated by commas.
     * @param recipientType The type of recipient the recipient list is being parsed for and sent to.
     * @param to The email address of who the message is to. e.g. "admin@test.com"
     * @param toName The textual name of who the message is to. e.g. "Administrator Name". This parameter is ignored if the to email address
     *               is a comma separated list of multiple email recipients.
     * @throws MessagingException If an error occurs.
     * @throws UnsupportedEncodingException If an error occurs.
     *
     */       
    private void processRecipientList (Message.RecipientType recipientType, String to, String toName) throws MessagingException, UnsupportedEncodingException {
        
        /**If the to message address has a comma in it, then it must be a comma separated list of email recipients*/
        StringTokenizer st = new StringTokenizer(to, ",");
        int tokenCount = st.countTokens();
        InternetAddress[] recipientList = new InternetAddress[tokenCount];
        
        /** Tokenize the recipient list, and create the Internet Address Array of Recipients */
        for (int i = 0; st.hasMoreTokens(); i++) {
            /**Get the next token**/
            String msgTo = st.nextToken();

            /**Ensure the token received is a valid address*/
            if (msgTo != null && msgTo.trim().length() > 0) {
                /**If we only have one email address then we can display the to name*/
                if (tokenCount == 1 && toName != null && toName.length() > 0) {
                    recipientList[i] = new InternetAddress(msgTo, toName);
                }
                /**Otherwise just display the email address as the to name.*/
                else{
                    recipientList[i] = new InternetAddress(msgTo);
                }
            }
        }
        msg.setRecipients(recipientType, recipientList);        
    }
	
	//--------------------------------CLASS SMTPAuthenticator ------------------------------------//   
    /**
	 * @author choyos
	 */
	private class SMTPAuthenticator extends javax.mail.Authenticator {
		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(props.getProperty("mail.smtp.user"),
					props.getProperty("mail.smtp.password"));
		}
	}
	
	public String getString(String key) {
		return props.getProperty(key);
	} 
}