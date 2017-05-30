package email;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import jpa.ReEmailParametersProcess;
import jpa.TbEmailProcess;
import jpa.TbEmailType;
import jpa.TbOptActRefefenceType;
import sessionVar.SessionUtil;
import ejb.email.EmailConfig;

public class EmailAdminBean implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@EJB(mappedName = "ejb/email/EmailConfig")
	private EmailConfig emailCfg;
	
	private List<SelectItem> lstTypeMsg;
	
	private String typeMsgId="C";
	
	private String subject;
	
	private String message="";
	
	private Long processId;
	
	private List<SelectItem> lstProcess;
	
	private String msgExample="";
	
	private boolean showPreview;
	
	private boolean showModal;
	
	private String modalmsg;
	
	private List<SelectItem> lstParameters;
	
	private Long idParameter;
	
	private Long emailTypeId;
	
	private List<TbEmailType> lstEmailType;
	
	private boolean showPanelEdit;
	
	private String processName;
	
	private boolean showModalConfirm;
	
	private String modalmsgConfirm;
	
	private boolean showModalConfirmDel; 
	
	private String destinationType;
	
	private String emailAddressList;
	
	private boolean showEmailAddressList;
	
	private boolean showModalConfirmAct;

	public EmailAdminBean(){
		msgExample = "";
		message = "";
		showPreview = false;
		showModal = false;
		showPanelEdit = false;
		modalmsg = "";
		subject = "";
		this.processId = -1L;
		showModalConfirm = false;
		modalmsgConfirm = "";
		destinationType = "";
		emailAddressList = "";
		showEmailAddressList = false;
		showModalConfirmAct = false;
		showModalConfirmDel = false;
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
		for (TbOptActRefefenceType lst : emailCfg.getListReferences(processId)){
			lstTypeMsg.add(new SelectItem(lst.getOptActReferenceId(),lst.getOptActDescription()));
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
			lstProcess.add(new SelectItem(-1L,"Seleccione uno"));
			for (TbEmailProcess lst : emailCfg.getListProcess()){
				lstProcess.add(new SelectItem(lst.getEmailProcessId(),lst.getEmailProcessName()));
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
		showModalConfirm = false;
		showModalConfirmAct = false;
		showModalConfirmDel = false;
		showModal = false;
		modalmsg = "";
		if(subject.equals("")){
			modalmsg = "No ha escrito ningún Asunto para el Mensaje";
			showModal = true;			
		} else if(message.replaceAll(" ", "").equals("")){
			modalmsg = "El Mensaje no puede estar en Blanco";
			showModal = true;
		}else 
		if (message.length()>2000){
			modalmsg = "El Mensaje no puede contener más de 2000 caracteres. Por favor verifique.";
			showModal = true;
		}
		else{
			/**
			 * Se realiza la validación de los campos
			 */
			String validation = this.validateForm();
			if(validation==null || validation.equals("")){
				if(emailCfg.editMessageEmail(emailTypeId, typeMsgId, processId, subject, emailAddressList, message.replaceAll("[\n\r]","#SL"),
						SessionUtil.ip(), SessionUtil
						.sessionUser().getUserId())){
					showModalConfirm = false;
					showPanelEdit = false;
					modalmsg = "Mensaje Modificado correctamente";
					showModal = true;
					msgExample = "";
					message = "";
					showPreview = false;				
					subject = "";
					destinationType = "";
					emailAddressList = "";
					showEmailAddressList = false;
				}else{
					modalmsg = "Error al Procesar la Operación";
					showModal = true;
				}
			}else{
				modalmsg = validation;
				showModal = true;
			}
		}
	}
	
	public void hideModal(){
		showModal = false;
	}
	
	public void clear(){
		msgExample = "";
		message = "";
		showPreview = false;
		showModal = false;
		showPanelEdit = false;
		modalmsg = "";
		subject = "";
		destinationType="";
		emailAddressList = "";
		showEmailAddressList = false;
		this.getLstTypeMsg();
		this.getLstProcess();
		this.processId = -1L;
		showModalConfirmAct = false;
		showModalConfirmDel = false;
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

	public void setLstEmailType(List<TbEmailType> lstEmailType) {
		this.lstEmailType = lstEmailType;
	}

	public List<TbEmailType> getLstEmailType() {
		if(lstEmailType == null){
			lstEmailType = new ArrayList<TbEmailType>();
		}else{
			lstEmailType.clear();
		}
		if(processId != -1L){
			lstEmailType = emailCfg.getListEmailTypeByProcess(processId);
		}
		return lstEmailType;
	}
	
	public void searchMessages(){
		showModalConfirmAct = false;
		showModalConfirmDel = false;
		showPanelEdit = false;
		if(processId == -1L){
			//Se elimina mensaje inecesario ya que no aparece al momento de seleccionar -1
			/*modalmsg="No ha Seleccionado ningún proceso";
			showModal =  true;*/
			System.out.println("-----------------------> Se selecciona -1");			
			
		}else{
			this.getLstEmailType();
		}
	}

	public void setEmailTypeId(Long emailTypeId) {
		this.emailTypeId = emailTypeId;
	}

	public Long getEmailTypeId() {
		return emailTypeId;
	}
	
	public void setShowPanelEdit(boolean showPanelEdit) {
		this.showPanelEdit = showPanelEdit;
	}

	public boolean isShowPanelEdit() {
		return showPanelEdit;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getProcessName() {
		return processName;
	}
	
	public void showMessagepanel(){
		showPanelEdit = false;
		showEmailAddressList = false;
		showModalConfirmAct = false;
		showModalConfirmDel = false;
		TbEmailType et = emailCfg.getEmailTypebyId(emailTypeId);
		if(et != null){
			processName = et.getTbEmailProcess().getEmailProcessName();
			subject = et.getEmailTypeSubject();			
			message = et.getEmailTypeMessage().replaceAll("#SL#SL","\n\r");
			typeMsgId =  et.getTbReference().getOptActReferenceId();
			if(typeMsgId.equals("U")){
				destinationType = et.getTbReference().getOptActDescription();
				emailAddressList = et.getEmailAddressList();
				showEmailAddressList = true;
			}
			showPanelEdit = true;
		}else{
			modalmsg="Error al solicita Mensaje";
			showModal =  true;
		}
	}

	public void cancel(){
		showModalConfirm = false;
		showPanelEdit = false;
		processName = "";
		subject = "";
		message = "";
		msgExample = "";
		showPreview = false;
		modalmsgConfirm = "";
		showModalConfirmDel = false;
		showEmailAddressList = false;
		destinationType = "";
		emailAddressList = "";
		showModalConfirmAct = false;		
	}
	
	public void msgsave(){
		showModalConfirmAct = false;
		showModalConfirmDel = false;
		showModalConfirm = false;
		modalmsgConfirm = "¿Está seguro de realizar esta operación?";
		showModalConfirm = true;	
	}

	public void setModalmsgConfirm(String modalmsgConfirm) {
		this.modalmsgConfirm = modalmsgConfirm;
	}

	public String getModalmsgConfirm() {
		return modalmsgConfirm;
	}

	public void setShowModalConfirm(boolean showModalConfirm) {
		this.showModalConfirm = showModalConfirm;
	}

	public boolean isShowModalConfirm() {
		return showModalConfirm;
	}
	
	public void showMsgdelete(){
		showModalConfirmAct = false;		
		showModalConfirm = false;
		modalmsgConfirm = "¿Está seguro de realizar la Inactivación?";
		showModalConfirmDel = true;
	}
	
	public void showMsgactive(){		
		showModalConfirmDel = false;
		showModalConfirm = false;
		modalmsgConfirm = "¿Está seguro de realizar la activación?";
		showModalConfirmAct = true;
	}
	
	public void setShowModalConfirmDel(boolean showModalConfirmDel) {
		this.showModalConfirmDel = showModalConfirmDel;
	}

	public boolean isShowModalConfirmDel() {
		return showModalConfirmDel;
	}
	
	public void setDestinationType(String destinationType) {
		this.destinationType = destinationType;
	}

	public String getDestinationType() {
		return destinationType;
	}

	public void setEmailAddressList(String emailAddressList) {
		this.emailAddressList = emailAddressList;
	}

	public String getEmailAddressList() {
		return emailAddressList;
	}

	public void setShowEmailAddressList(boolean showEmailAddressList) {
		this.showEmailAddressList = showEmailAddressList;
	}

	public boolean isShowEmailAddressList() {
		return showEmailAddressList;
	}

	public void setShowModalConfirmAct(boolean showModalConfirmAct) {
		this.showModalConfirmAct = showModalConfirmAct;
	}

	public boolean isShowModalConfirmAct() {
		return showModalConfirmAct;
	}

	public void delete(){
		showModalConfirmDel = false;
		showModalConfirmAct = false;
		if(emailCfg.deleteMessageEmail(emailTypeId,SessionUtil.ip(), SessionUtil
				.sessionUser().getUserId())){
			showModalConfirm = false;
			showModalConfirmDel = false;
			showPanelEdit = false;
			modalmsg = "Mensaje Inactivado correctamente";
			showModal = true;
			msgExample = "";
			message = "";
			showPreview = false;				
			subject = "";
			destinationType="";
			emailAddressList="";
			showEmailAddressList = false;
		}else{
			modalmsg = "Error al Procesar la Operación de Inactivación";
			showModal = true;
		}
	}
	
	public void changeProcess(){
		this.setShowPanelEdit(false);
		this.getLstParameters();
		System.out.println("EmailAdminBean.changeProcess");
	}
	
	public String validateForm(){
		String result="";
		if(typeMsgId.equals("U")){
			if(emailAddressList==null || emailAddressList.replaceAll(" ", "").equals("")){
				result = result+" La lista de destinatarios no puede estar vacía.";
			}else{
				String[] lstAddr = emailAddressList.split(";");
				for (String email: lstAddr){
					if(!validator.Validation.isEmail(email.trim())){
						result = result+" El campo lista de destinatarios contiene un correo inválido.";
						break;
					}
				}
			}
		}
		if(subject==null || subject.replaceAll(" ", "").equals("")){
			result = result+" El asunto no puede estar vacio.";
		}else if(!validator.Validation.isAlphaNumCompany(subject)){
			result = result+" El campo asunto contiene caracteres inválidos.";
		}
		return result;
	}
	
	public void active(){
		showModalConfirmDel = false;
		showModalConfirmAct = false;
		if(emailCfg.activeMessageEmail(emailTypeId,SessionUtil.ip(), SessionUtil
				.sessionUser().getUserId())){
			showModalConfirm = false;
			showModalConfirmDel = false;
			showPanelEdit = false;
			modalmsg = "Mensaje Activado correctamente";
			showModal = true;
			msgExample = "";
			message = "";
			showPreview = false;				
			subject = "";
			destinationType="";
			emailAddressList="";
			showEmailAddressList = false;
		}else{
			modalmsg = "Error al Procesar la Operación de Activación";
			showModal = true;
		}
	}
}
