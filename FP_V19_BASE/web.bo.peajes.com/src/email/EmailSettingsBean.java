package email;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import jpa.ReEmailParametersProcess;
import jpa.TbEmailProcess;
import jpa.TbOptActRefefenceType;
import sessionVar.SessionUtil;
import ejb.email.EmailConfig;

public class EmailSettingsBean implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@EJB(mappedName = "ejb/email/EmailConfig")
	private EmailConfig emailCfg;
		
	private String subject;
	
	private String message="";
	
	private Long processId;
	
	private List<SelectItem> lstProcess;
	
	private List<SelectItem> lstTypeMsg;
	
	private String typeMsgId;	
	
	private String msgExample="";
	
	private boolean showPreview;
	
	private boolean showModal;
	
	private boolean modalConfirm;
	
	private String modalmsg;
	
	private List<SelectItem> lstParameters;
	
	private Long idParameter;	
	
	private boolean showEmailAddressList;
	
	private String emailAddressList;

	
	public EmailSettingsBean(){
		msgExample = "";
		message = "";
		showPreview = false;
		showModal = false;
		modalmsg = "";
		subject = "";
		System.out.println("Inici� a EmailSettingsBean");
	}
	
	
	
	public boolean isModalConfirm() {
		return modalConfirm;
	}



	public void setModalConfirm(boolean modalConfirm) {
		this.modalConfirm = modalConfirm;
	}



	public void setLstTypeMsg(List<SelectItem> lstTypeMsg) {
		this.lstTypeMsg = lstTypeMsg;
	}

	public List<SelectItem> getLstTypeMsg() {
		if(lstTypeMsg == null){
			lstTypeMsg = new ArrayList<SelectItem>();
		} else{
			lstTypeMsg.clear();			
		}
		lstTypeMsg.add(new SelectItem(-1L,"-Seleccione uno-"));
		for (TbOptActRefefenceType lst : emailCfg.getListReferences(processId)){
			lstTypeMsg.add(new SelectItem(lst.getOptActReferenceId(),lst.getOptActDescription()));
			if(lstTypeMsg.size()==1){
				if(lst.getOptActReferenceId().equals("U")){
					showEmailAddressList = true;
				}else{
					showEmailAddressList = false;
				}
			}
		}
		
		if(typeMsgId !=null){
			if(typeMsgId.equals("U")){
				showEmailAddressList = true;
			}else{
				showEmailAddressList = false;
			}
		}		
		return lstTypeMsg;
	}

	public void setTypeMsgId(String typeMsgId) {
		this.typeMsgId = typeMsgId;
	}

	public String getTypeMsgId() {
		return typeMsgId;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSubject() {
		return subject;
	}

	public void setMessage(String message) {		
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
	}
	
	public void preview(){	
		msgExample = subject+"\n\r\n\r"+emailCfg.replaceParameterPrev(message);
		showPreview = true;
	}

	public Long getProcessId() {
		return processId;
	}

	public void setLstProcess(List<SelectItem> lstProcess) {
		this.lstProcess = lstProcess;
	}

	public List<SelectItem> getLstProcess() {
		if(lstProcess == null){
			lstProcess = new ArrayList<SelectItem>();
			lstProcess.add(new SelectItem(-1L,"-Seleccione uno-"));
			for (TbEmailProcess lst : emailCfg.getListProcessNoMessage()){			
				lstProcess.add(new SelectItem(lst.getEmailProcessId(),lst.getEmailProcessName()));
				if(lstProcess.size()==1){
					processId = lst.getEmailProcessId();
				}
			}
		}
		return lstProcess;
	}
	
	public void addParameter(){
		String ab = emailCfg.getAbbreviationByParameter(idParameter);
		message = message+" "+ab;		
		showPreview = false;		
	}
	

	public void setMsgExample(String msgExample) {
		this.msgExample = msgExample;
	}

	public String getMsgExample() {		
		return msgExample;
	}

	public void setShowPreview(boolean showPreview) {
		this.showPreview = showPreview;
	}

	public boolean isShowPreview() {
		return showPreview;
	}
	
	public void setModalmsg(String modalmsg) {
		this.modalmsg = modalmsg;
	}

	public String getModalmsg() {
		return modalmsg;
	}

	public void setShowModal(boolean showModal) {
		this.showModal = showModal;
	}

	public boolean isShowModal() {
		return showModal;
	}
	
	public void save(){
		showModal = false;
		modalmsg = "";
		System.out.println("-------------------> "+processId+" Type: "+typeMsgId);
		if(processId==-1L){
			modalmsg = "No ha seleccionado un proceso";
			showModal = true;	
		}else if(typeMsgId.equals("-1")){
			modalmsg = "No ha seleccionado un tipo de destinatario";
			showModal = true;	
		}else if(subject.equals("")){
			modalmsg = "No ha escrito ning�n Asunto para el Mensaje";
			showModal = true;			
		} else if(message.replaceAll(" ", "").equals("")){
			modalmsg = "El Mensaje no puede estar en Blanco";
			showModal = true;
		//Se agrega validacion para verificar si el mensaje ya se encuentra creado. se agrega refuerzo.
	    } else if (emailCfg.validateProcessTypeMsg(processId,typeMsgId)){	    	
	    	modalmsg = "El Mensaje ya se encuentra creado";
			showModal = true;
		}else{				
			/**
			 * Se realiza la validaci�n de los campos
			 */
			String validation = this.validateForm();
			if(validation==null || validation.equals("")){
				modalmsg="�Esta seguro de crear el mensaje?";
				setModalConfirm(true);
			}else{
				modalmsg = validation;
				showModal = true;
			}
		}
	}
	public void save1(){
		setModalConfirm(false);
		if((typeMsgId.equals(null)) || (typeMsgId.equals(""))){
			typeMsgId = "C";
		}
		if(emailCfg.setMessageEmail(typeMsgId, processId, subject, emailAddressList , message.replaceAll("[\n\r]","#SL"),
				SessionUtil.ip(), SessionUtil
				.sessionUser().getUserId())){
			modalmsg = "Mensaje creado correctamente";
			showModal = true;
			msgExample = "";
			message = "";
			showPreview = false;				
			subject = "";
			emailAddressList = "";
			lstProcess=null;
			this.getLstParameters();
		}else{
			modalmsg = "Error al crear el Mensaje";
			showModal = true;			
		}
	}
	
	public void hideModal(){
		showModal = false;	
		if (modalmsg.equals("Mensaje creado correctamente")){
			clear();
		}
	}
	
	public void hideModalConfirm(){
		setModalConfirm(false);
	}
	
	public void clear(){
		msgExample = "";
		message = "";
		showPreview = false;
		showModal = false;
		setModalConfirm(false);
		modalmsg = "";
		subject = "";
		emailAddressList = "";
		setProcessId(-1L);
		setTypeMsgId("-1");
		this.getLstTypeMsg();
		this.getLstProcess();
	}

	public void setLstParameters(List<SelectItem> lstParameters) {
		this.lstParameters = lstParameters;
	}

	public List<SelectItem> getLstParameters() {
		if(lstParameters == null){
			lstParameters = new ArrayList<SelectItem>();
		} else{
			lstParameters.clear();
			
		}
		System.out.println("processId: "+processId);
		for (ReEmailParametersProcess lst : emailCfg.getParameters(processId)){
			lstParameters.add(new SelectItem(lst.getEmailParameterId().getEmailParametersId(),lst.getEmailParameterId().getEmailParametersName()));
		}
		return lstParameters;
	}

	public void setIdParameter(Long idParameter) {
		this.idParameter = idParameter;
	}

	public Long getIdParameter() {
		return idParameter;
	}
	
	

	public void setShowEmailAddressList(boolean showEmailAddressList) {
		this.showEmailAddressList = showEmailAddressList;
	}

	public boolean isShowEmailAddressList() {
		return showEmailAddressList;
	}

	public void setEmailAddressList(String emailAddressList) {
		this.emailAddressList = emailAddressList;
	}

	public String getEmailAddressList() {
		return emailAddressList;
	}

	public void changeProcess(){
		this.subject="";
		this.message="";
		this.emailAddressList = "";
		showEmailAddressList = false;
		this.getLstParameters();
		typeMsgId = null;
		idParameter = null;
		System.out.println("EmailAdminBean.changeProcess");
	}
	
	public void changeTypeMsg(){
		System.out.println("typeMsgId: "+typeMsgId);
		if(typeMsgId.equals("U")){
			showEmailAddressList = true;
		}else{
			showEmailAddressList = false;			
		}		
	}
	
	public String validateForm(){
		String result="";
		if(typeMsgId.equals("U")){
			if(emailAddressList==null || emailAddressList.replaceAll(" ", "").equals("")){
				result = result+" La lista de destinatarios no puede estar vac�a.";
			}else{
				String[] lstAddr = emailAddressList.split(";");
				for (String email: lstAddr){
					if(!validator.Validation.isEmail(email.trim())){
						result = result+" El campo lista de destinatarios contiene un correo inv�lido.";
						break;
					}
				}
			}
		}
		if(subject==null || subject.replaceAll(" ", "").equals("")){
			result = result+" El asunto no puede estar vac�o.";
		}else if(!validator.Validation.isAlphaNumCompany(subject)){
			result = result+" El campo asunto contiene caracteres inv�lidos.";
		}
		return result;
	}
}