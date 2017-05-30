package home;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import util.ClientsDB;
import ejb.User;


/**
 * @author jromero
 */
public class HomeBean implements Serializable{
	private static final long serialVersionUID = 199399153082288754L;

	// Attributes -----------------------------------------------------------------------

	@EJB(mappedName="ejb/User")
	private User user;

	private long preReg;
	
	private long valDoc;
	
	private long creBan;
	
	private long assBan;
	
	private long valClie;
	
	private String modalMsg;
		
	private String msgPreReg;
	
	private String msgValDoc;
	
	private String msgBanCre;
	
	private String msgBanAso;
	
	private String msgValCli;
	
	private List<ClientsDB> list;

	private boolean modal;
	
	private boolean modalPreReg;
	
	private boolean modalBanCre;
	
	private boolean modalBanAso;
	
	private boolean modalValDoc;
	
	private boolean modalValCli;
	
	private boolean hideModal;
	
	private boolean hideModalPreReg;
	
	private boolean hideModalBanCre;
	
	private boolean hideModalBanAso;
	
	private boolean hideModalValDoc;
	
	private boolean hideModalValCli;
	
	public long getPreReg() {
		return preReg;
	}

	public void setPreReg(long preReg) {
		this.preReg = preReg;
	}

	public long getValDoc() {
		return valDoc;
	}

	public void setValDoc(long valDoc) {
		this.valDoc = valDoc;
	}

	public long getCreBan() {
		return creBan;
	}

	public void setCreBan(long creBan) {
		this.creBan = creBan;
	}



	public long getAssBan() {
		return assBan;
	}

	public void setAssBan(long assBan) {
		this.assBan = assBan;
	}
	
	// Constructor ----------------------------------------------------------------------

	public HomeBean() {
	}
	
	// Actions --------------------------------------------------------------------------
	
	public String getModalMsg() {
		return modalMsg;
	}

	public void setModalMsg(String modalMsg) {
		this.modalMsg = modalMsg;
	}

	public String getMsgPreReg() {
		return msgPreReg;
	}

	public void setMsgPreReg(String msgPreReg) {
		this.msgPreReg = msgPreReg;
	}

	public String getMsgValDoc() {
		return msgValDoc;
	}

	public void setMsgValDoc(String msgValDoc) {
		this.msgValDoc = msgValDoc;
	}

	public String getMsgBanCre() {
		return msgBanCre;
	}

	public void setMsgBanCre(String msgBanCre) {
		this.msgBanCre = msgBanCre;
	}

	public String getMsgBanAso() {
		return msgBanAso;
	}

	public void setMsgBanAso(String msgBanAso) {
		this.msgBanAso = msgBanAso;
	}

	public List<ClientsDB> getList() {
		return list;
	}

	public void setList(List<ClientsDB> list) {
		this.list = list;
	}

	public boolean isModal() {
		return modal;
	}

	public void setModal(boolean modal) {
		this.modal = modal;
	}

	public boolean isModalPreReg() {
		return modalPreReg;
	}

	public void setModalPreReg(boolean modalPreReg) {
		this.modalPreReg = modalPreReg;
	}

	public boolean isModalBanCre() {
		return modalBanCre;
	}

	public void setModalBanCre(boolean modalBanCre) {
		this.modalBanCre = modalBanCre;
	}

	public boolean isModalBanAso() {
		return modalBanAso;
	}

	public void setModalBanAso(boolean modalBanAso) {
		this.modalBanAso = modalBanAso;
	}

	public boolean isModalValDoc() {
		return modalValDoc;
	}

	public void setModalValDoc(boolean modalValDoc) {
		this.modalValDoc = modalValDoc;
	}
	
	public boolean isHideModal() {
		return hideModal;
	}

	public void setHideModal(boolean hideModal) {
		this.hideModal = hideModal;
	}

	public boolean isHideModalPreReg() {
		return hideModalPreReg;
	}

	public void setHideModalPreReg(boolean hideModalPreReg) {
		this.hideModalPreReg = hideModalPreReg;
	}

	public boolean isHideModalBanCre() {
		return hideModalBanCre;
	}

	public void setHideModalBanCre(boolean hideModalBanCre) {
		this.hideModalBanCre = hideModalBanCre;
	}

	public boolean isHideModalBanAso() {
		return hideModalBanAso;
	}

	public void setHideModalBanAso(boolean hideModalBanAso) {
		this.hideModalBanAso = hideModalBanAso;
	}

	public boolean isHideModalValDoc() {
		return hideModalValDoc;
	}

	public void setHideModalValDoc(boolean hideModalValDoc) {
		this.hideModalValDoc = hideModalValDoc;
	}

	public void preRegistration(){
		if(list == null) {
			list = new ArrayList<ClientsDB>();			
		}else{
			list.clear();
		}
		list = user.getLisClientsDB(1);
		modalBanCre=false;
		modalBanAso=false;
		modalValDoc=false;
		modalValCli=false;
		hideModalPreReg=false;
		hideModalBanCre=false;
		hideModalBanAso=false;
		hideModalValDoc=false;
		hideModalValCli=false;
		msgPreReg="";
		msgValDoc="";
		msgBanCre="";
		msgBanAso="";
		msgValCli="";
		if(list!=null&&list.size()!=0){
			modal=false;
			modalPreReg=true;
			modalMsg="";
		}else{
			modal=true;
			modalPreReg=false;
			modalMsg="No se obtuvieron datos";
		}
	}
	
	public void validationDocuments(){
		if(list == null) {
			list = new ArrayList<ClientsDB>();			
		}else{
			list.clear();
		}
		list = user.getLisClientsDB(2);
		modalPreReg=false;
		modalBanCre=false;
		modalBanAso=false;
		modalValCli=false;
		hideModalPreReg=false;
		hideModalBanCre=false;
		hideModalBanAso=false;
		hideModalValDoc=false;
		hideModalValCli=false;
		msgPreReg="";
		msgValDoc="";
		msgBanCre="";
		msgBanAso="";
		msgValCli="";
		if(list!=null&&list.size()!=0){
			modal=false;
			modalValDoc=true;
			modalMsg="";
		}else{
			modal=true;
			modalValDoc=false;
			modalMsg="No se obtuvieron datos";
		}
	}
	
	public void bankingProductCreation(){
		if(list == null) {
			list = new ArrayList<ClientsDB>();			
		}else{
			list.clear();
		}
		list = user.getLisClientsDB(3);
		modalPreReg=false;
		modalBanAso=false;
		modalValDoc=false;
		modalValCli=false;
		hideModalPreReg=false;
		hideModalBanCre=false;
		hideModalBanAso=false;
		hideModalValDoc=false;
		hideModalValCli=false;
		msgPreReg="";
		msgValDoc="";
		msgBanCre="";
		msgBanAso="";
		msgValCli="";
		setMsgValCli("");
		if(list!=null&&list.size()!=0){
			modal=false;
			modalBanCre=true;
			modalMsg="";
		}else{
			modal=true;
			modalBanCre=false;
			modalMsg="No se obtuvieron datos";
		}
	}
	
	public void associationBankingProduct(){
		if(list == null) {
			list = new ArrayList<ClientsDB>();			
		}else{
			list.clear();
		}
		list = user.getLisClientsDB(4);
		modalPreReg=false;
		modalBanCre=false;
		modalValDoc=false;
		modalValCli=false;
		hideModalPreReg=false;
		hideModalBanCre=false;
		hideModalBanAso=false;
		hideModalValDoc=false;
		hideModalValCli=false;
		msgPreReg="";
		msgValDoc="";
		msgBanCre="";
		msgBanAso="";
		msgValCli="";
		if(list!=null&&list.size()!=0){
			modal=false;
			modalBanAso=true;
			modalMsg="";
		}else{
			modal=true;
			modalBanAso=false;
			modalMsg="No se obtuvieron datos";
		}
	}
	
	public void validationClients(){
		if(list == null) {
			list = new ArrayList<ClientsDB>();			
		}else{
			list.clear();
		}
		list = user.getLisClientsDB(5);
		modalPreReg=false;
		modalBanCre=false;
		modalBanAso=false;
		modalValCli=false;
		hideModalPreReg=false;
		hideModalBanCre=false;
		hideModalBanAso=false;
		hideModalValDoc=false;
		hideModalValCli=false;
		msgPreReg="";
		msgValDoc="";
		msgBanCre="";
		msgBanAso="";
		msgValCli="";
		if(list!=null&&list.size()!=0){
			modal=false;
			modalValCli=true;
			modalMsg="";
		}else{
			modal=true;
			modalValCli=false;
			modalMsg="No se obtuvieron datos";
		}
	}
	
	public void cancel(){
		modal=false;
		modalPreReg=false;
		modalBanCre=false;
		modalBanAso=false;
		modalValDoc=false;
		modalValCli=false;
		hideModalPreReg=false;
		hideModalBanCre=false;
		hideModalBanAso=false;
		hideModalValDoc=false;
		hideModalValCli=false;
		modalMsg="";
		msgPreReg="";
		msgValDoc="";
		msgBanCre="";
		msgBanAso="";
		msgValCli="";
	}

	public void setValClie(long valClie) {
		this.valClie = valClie;
	}

	public long getValClie() {
		return valClie;
	}

	public void setModalValCli(boolean modalValCli) {
		this.modalValCli = modalValCli;
	}

	public boolean isModalValCli() {
		return modalValCli;
	}

	public void setHideModalValCli(boolean hideModalValCli) {
		this.hideModalValCli = hideModalValCli;
	}

	public boolean isHideModalValCli() {
		return hideModalValCli;
	}

	public void setMsgValCli(String msgValCli) {
		this.msgValCli = msgValCli;
	}

	public String getMsgValCli() {
		return msgValCli;
	}
	
	
}