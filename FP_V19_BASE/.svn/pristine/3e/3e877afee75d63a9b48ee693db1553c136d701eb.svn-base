package mBeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.EJB;

import constant.EmailSubject;
import constant.EmailType;

import util.EMailDef;
import util.MessageDef;
import ejb.ConfigXML;
import ejb.SendMail;

/**
 * E-mail Manage Bean.
 * @author tmolina
 */
public class EmailMBean implements Serializable{
	private static final long serialVersionUID = -123263418835867391L;
	
	@EJB(mappedName="ejb/xml")
	private ConfigXML configXML;
	
	@EJB(mappedName="ejb/sendMail")
	private SendMail smr;
	
	// Attributes ----------//
	
	private List<EMailDef> listTypes;
	
	private String typeMail; //E-Mail type configuration
	
	private EMailDef def;
	
	private List<MessageDef> mssgs; //Message List of a determined E-Mail configuration
	
	/** New Message **/
	
	private String msgName;
	
	private String msgContent;

	/** New E-Mail Config**/
	
	private String emailSubject;
	
	private String emailPriority;
	
	private String emailAddressTo;
	
	private String emailAddressFrom = "no-reply@peajes.com";
	
	private String emailAddressName = "Admin.";
	
	private String emailTypeName;
	
	/** MODAL CONTROL  **/
	
	private boolean showModal;
	
	private String modalMessage;
	
	private boolean showEmail;
	
	private boolean selectMessage;
	
	private boolean showNewMail;
	
	private boolean showEditMail;
	
	private boolean showModalEdit;
	
	private boolean showNewMsg;
	
	// Constructor ------------------//
	
	public EmailMBean(){
		listTypes = new ArrayList<EMailDef>();
	}
	
	// Actions ----------------------//
	
	/**
	 *  Send an E-Mail.
	 */
	public String sendEMail() {
		def = this.getEMailDefByType();
		if (smr.sendMail(EmailType.CLIENT, def.getAddressTo(), EmailSubject.CLIENTCREATION, null)) {
			this.setModalMessage("Transacción Exitosa.");
		} else {
			this.setModalMessage("Ha ocurrido un error, intente nuevamente.");
		}
		this.setShowModal(true);
		return null;
	}
	
	/**
	 * Shows an E-Mail type.
	 */
	public String showTypeEMail(){
		def = this.getEMailDefByType();
		this.showEmail = true;
		return null;
	}
	
	/**
	 * Edits an E-Mail type.
	 */
	public String editTypeEMail(){
		def = this.getEMailDefByType();
		mssgs = def.getMessage();
		this.showEditMail = true;
		return null;
	}
	
	/**
	 * Gets an E-mail Definition Object by type.
	 */
	public EMailDef getEMailDefByType(){
		EMailDef def = null;
		for (Object o: listTypes){
			if(((EMailDef)o).getTypeMail().equals(typeMail)){
				return ((EMailDef)o);
			}
		}
		return def;
	}
	
	/**
	 * Edits an E-Mail Type.
	 */
	public String edit(){
		this.setShowEditMail(false);
		if(validate(def.getAddressTo())){
			configXML.editXML(def);
		} else{
			this.setShowModalEdit(true);
			this.setModalMessage("Correo Inválido. Verifique. No se realizó ninguna acción.");
			return null;
		}
		return null;
	}
	
	/**
	 * Saves a new type E-Mail Message.
	 */
	public String saveMessage(){
		configXML.saveMessage(typeMail, msgName, msgContent);
		listTypes = configXML.getEMailTypeList();
		def = this.getEMailDefByType();
		mssgs = def.getMessage();
		msgName = msgContent = "";
		this.setShowNewMsg(false);
		this.setShowEditMail(true);
		return null;
	}
	
	/**
	 * Prepares to save a new e-mail type configuration.
	 */
	public String newE(){
		typeMail = "";
		emailTypeName = "";
		emailAddressTo = "";
		emailPriority = 	"";
		emailSubject = "";
		msgName = "";	
		msgContent = "";
		this.showNewMail = true;
		return null;
	}
	
	/**
	 * Saves a new e-mail type configuration.
	 */
	public String saveEMail(){
		EMailDef mailDef = new EMailDef();
		mailDef.setTypeMail(typeMail);
		mailDef.setNameTypeMail(emailTypeName);
		mailDef.setAddressFrom(emailAddressFrom);
		mailDef.setNameFrom(emailAddressName);
		mailDef.setAddressTo(emailAddressTo);
		mailDef.setSubject(emailSubject);
		mailDef.setPriority(emailPriority);
		configXML.saveConfigEMail(mailDef, msgContent, msgName);
		listTypes = configXML.getEMailTypeList();
		this.showNewMail = false;
		return null;
	}	
	
	/**
	 * Shows the messages list of a determined email configuration.
	 */
	public String selectMsg(){
		def = this.getEMailDefByType();
		mssgs = def.getMessage();
		this.selectMessage = true;
		return null;
	}
	
	public String successSelectMsg(){
		this.selectMessage = false;
		return null;
	}
	
	/**
	 * Control modal panel.
	 */
	public String reset() {
		this.setShowModal(false);
		this.setModalMessage("");
		this.setShowEmail(false);
		this.setSelectMessage(false);
		this.setShowNewMail(false);
		this.setShowEditMail(false);
		this.setShowModalEdit(false);
		this.setShowNewMsg(false);
		return "sendEmail";
	}
	
	/**
	 * Show panel create new msg
	 * @return
	 */
	public String newMsg(){
		this.showNewMsg = true;
		this.setShowEditMail(false);
		return null;
	}
	
	/**
	 * Validates an E-mail
	 * @param value
	 * @return
	 */
	public boolean validate(String value){		
		String mail = value.toString();
		Pattern p = Pattern.compile("\\D\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*([,;]\\S\\D\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*)*");
		Matcher m = p.matcher(mail);
	    return m.matches();	
	}
	
	// Getters and Setters -----------------//
	
	/**
	 * @param listTypes the listTypes to set
	 */
	public void setListTypes(List<EMailDef> listTypes) {
		this.listTypes = listTypes;
	}

	/**
	 * @return the listTypes
	 */
	public List<EMailDef> getListTypes() {
		if(listTypes.size()==0)
			listTypes = configXML.getEMailTypeList();
		return listTypes;
	}
	
	/**
	 * @return the typeMail
	 */
	public String getTypeMail() {
		return typeMail;
	}

	/**
	 * @param typeMail the typeMail to set
	 */
	public void setTypeMail(String typeMail) {
		this.typeMail = typeMail;
	}

	/**
	 * @param def the def to set
	 */
	public void setDef(EMailDef def) {
		this.def = def;
	}

	/**
	 * @return the def
	 */
	public EMailDef getDef() {
		return def;
	}

	/**
	 * @param mssgs the mssgs to set
	 */
	public void setMssgs(List<MessageDef> mssgs) {
		this.mssgs = mssgs;
	}

	/**
	 * @return the mssgs
	 */
	public List<MessageDef> getMssgs() {
		return mssgs;
	}

	/**
	 * @param msgName the msgName to set
	 */
	public void setMsgName(String msgName) {
		this.msgName = msgName;
	}

	/**
	 * @return the msgName
	 */
	public String getMsgName() {
		return msgName;
	}

	/**
	 * @param msgContent the msgContent to set
	 */
	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	/**
	 * @return the msgContent
	 */
	public String getMsgContent() {
		return msgContent;
	}

	/**
	 * @param emailSubject the emailSubject to set
	 */
	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

	/**
	 * @return the emailSubject
	 */
	public String getEmailSubject() {
		return emailSubject;
	}

	/**
	 * @param emailPriority the emailPriority to set
	 */
	public void setEmailPriority(String emailPriority) {
		this.emailPriority = emailPriority;
	}

	/**
	 * @return the emailPriority
	 */
	public String getEmailPriority() {
		return emailPriority;
	}

	/**
	 * @param emailAddressTo the emailAddressTo to set
	 */
	public void setEmailAddressTo(String emailAddressTo) {
		this.emailAddressTo = emailAddressTo;
	}

	/**
	 * @return the emailAddressTo
	 */
	public String getEmailAddressTo() {
		return emailAddressTo;
	}

	/**
	 * @param emailAddressName the emailAddressName to set
	 */
	public void setEmailAddressName(String emailAddressName) {
		this.emailAddressName = emailAddressName;
	}

	/**
	 * @return the emailAddressName
	 */
	public String getEmailAddressName() {
		return emailAddressName;
	}

	/**
	 * @param emailAddressFrom the emailAddressFrom to set
	 */
	public void setEmailAddressFrom(String emailAddressFrom) {
		this.emailAddressFrom = emailAddressFrom;
	}

	/**
	 * @return the emailAddressFrom
	 */
	public String getEmailAddressFrom() {
		return emailAddressFrom;
	}

	/**
	 * @param emailTypeName the emailTypeName to set
	 */
	public void setEmailTypeName(String emailTypeName) {
		this.emailTypeName = emailTypeName;
	}

	/**
	 * @return the emailTypeName
	 */
	public String getEmailTypeName() {
		return emailTypeName;
	}

	/**
	 * @param showModal the showModal to set
	 */
	public void setShowModal(boolean showModal) {
		this.showModal = showModal;
	}

	/**
	 * @return the showModal
	 */
	public boolean isShowModal() {
		return showModal;
	}

	/**
	 * @param modalMessage the modalMessage to set
	 */
	public void setModalMessage(String modalMessage) {
		this.modalMessage = modalMessage;
	}

	/**
	 * @return the modalMessage
	 */
	public String getModalMessage() {
		return modalMessage;
	}

	/**
	 * @param showEmail the showEmail to set
	 */
	public void setShowEmail(boolean showEmail) {
		this.showEmail = showEmail;
	}

	/**
	 * @return the showEmail
	 */
	public boolean isShowEmail() {
		return showEmail;
	}

	/**
	 * @param selectMessage the selectMessage to set
	 */
	public void setSelectMessage(boolean selectMessage) {
		this.selectMessage = selectMessage;
	}

	/**
	 * @return the selectMessage
	 */
	public boolean isSelectMessage() {
		return selectMessage;
	}

	/**
	 * @param showNewMail the showNewMail to set
	 */
	public void setShowNewMail(boolean showNewMail) {
		this.showNewMail = showNewMail;
	}

	/**
	 * @return the showNewMail
	 */
	public boolean isShowNewMail() {
		return showNewMail;
	}

	/**
	 * @param showEditMail the showEditMail to set
	 */
	public void setShowEditMail(boolean showEditMail) {
		this.showEditMail = showEditMail;
	}

	/**
	 * @return the showEditMail
	 */
	public boolean isShowEditMail() {
		return showEditMail;
	}

	/**
	 * @param showModalEdit the showModalEdit to set
	 */
	public void setShowModalEdit(boolean showModalEdit) {
		this.showModalEdit = showModalEdit;
	}

	/**
	 * @return the showModalEdit
	 */
	public boolean isShowModalEdit() {
		return showModalEdit;
	}

	/**
	 * @param showNewMsg the showNewMsg to set
	 */
	public void setShowNewMsg(boolean showNewMsg) {
		this.showNewMsg = showNewMsg;
	}

	/**
	 * @return the showNewMsg
	 */
	public boolean isShowNewMsg() {
		return showNewMsg;
	}
}