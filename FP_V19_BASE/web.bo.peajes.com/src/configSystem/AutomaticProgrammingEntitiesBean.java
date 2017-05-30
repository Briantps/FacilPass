package configSystem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;

import security.UserLogged;
import sessionVar.SessionUtil;
import util.UListbank;
import ejb.crud.Bank;


public class AutomaticProgrammingEntitiesBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	
	/*@EJB(mappedName="ejb/IAutomaticProgrammingEntities")
	private IAutomaticProgrammingEntities AutomaticProgrammingEntitiesEJB;*/
	
	@EJB(mappedName="ejb/Bank")
	private Bank bankEJB;
	private UserLogged usrs;
	
	private Long bankId;
	
	private int flag;
	private List<UListbank> bankList;
	private List<UListbank> bankListTemp;
	private List<UListbank> bankListSend;
	
	private boolean modalModifyURL;
	private boolean modifyPermProgramm;
	private boolean modalAlert;
	private boolean modalAlertVali;
	private boolean modalconfirm;
	
	private String msnmodal="";
	private String msnmodalURL="";
	private String modifyURL;	
	private String ip;
	
	//BM
	private boolean permEdit;
	private boolean permCon;
	private String nameperm;
	
	
	public AutomaticProgrammingEntitiesBean() {
		// TODO Auto-generated constructor stub
		bankList = null;
		bankListTemp = null;
		bankListSend = null;
		usrs = (UserLogged) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");	
		ip=SessionUtil.ip();
	} 
	
	
	@PostConstruct
	public void init(){
			
		bankList = new ArrayList<UListbank>();
		bankListTemp = new ArrayList<UListbank>();
		bankListSend = new ArrayList<UListbank>();
		
		bankList = bankEJB.getListBankAutomaticProgramming();
		//bankListTemp.addAll(bankList);
		bankListTemp = bankEJB.getListBankAutomaticProgramming();
		
		
		//BM
		nameperm = "editautomaticProgrammingEntities";
		permEdit = bankEJB.getpermission(usrs.getUserId(),nameperm);
		System.out.println("Permiso de Editar " + permEdit);
		
		/*
		nameperm = "conautomaticProgrammingEntities";
		permCon = bankEJB.getpermission(usrs.getUserId(),nameperm);
		System.out.println("Permiso de Consultar " + permCon);
		*/
	}
	
	public boolean isPermEdit() {
		return permEdit;
	}


	public void setPermEdit(boolean permEdit) {
		this.permEdit = permEdit;
	}


	public boolean isPermCon() {
		return permCon;
	}


	public void setPermVer(boolean permCon) {
		this.permCon = permCon;
	}


	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}
	public Long getBankId() {
		return bankId;
	}
	
	public void setModalModifyURL(boolean modalModifyURL) {
		this.modalModifyURL = modalModifyURL;
	}
	public boolean isModalModifyURL() {
		return modalModifyURL;
	}
			
	public void setMsnmodal(String msnmodal) {
		this.msnmodal = msnmodal;
	}
	public String getMsnmodal() {
		return msnmodal;
	}
	
	public void setModalconfirm(boolean modalconfirm) {
		this.modalconfirm = modalconfirm;
	}
	public boolean isModalconfirm() {
		return modalconfirm;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public int getFlag() {
		return flag;
	}
	public void setModifyPermProgramm(boolean modifyPermProgramm) {
		this.modifyPermProgramm = modifyPermProgramm;
	}
	public boolean isModifyPermProgramm() {
		return modifyPermProgramm;
	}
		
	public void setModifyURL(String modifyURL) {
		this.modifyURL = modifyURL;
	}
	public String getModifyURL() {
		return modifyURL;
	}	
	
	public boolean isModalAlert() {
		return modalAlert;
	}
	public void setModalAlert(boolean modalAlert) {
		this.modalAlert = modalAlert;
	}
	
	public List<UListbank> getBankList() {
		return bankList;
	}

	public void setBankList(List<UListbank> bankList) {
		this.bankList = bankList;
	}

	public List<UListbank> getBankListTemp() {
		return bankListTemp;
	}

	public void setBankListTemp(List<UListbank> bankListTemp) {
		this.bankListTemp = bankListTemp;
	}

	public List<UListbank> getBankListSend() {
		return bankListSend;
	}

	public void setBankListSend(List<UListbank> bankListSend) {
		this.bankListSend = bankListSend;
	}
	
	
	public String getMsnmodalURL() {
		return msnmodalURL;
	}


	public void setMsnmodalURL(String msnmodalURL) {
		this.msnmodalURL = msnmodalURL;
	}


	public UserLogged getUsrs() {
		return usrs;
	}


	public void setUsrs(UserLogged usrs) {
		this.usrs = usrs;
	}


	public String getIp() {
		return ip;
	}


	public void setIp(String ip) {
		this.ip = ip;
	}

	

	public boolean isModalAlertVali() {
		return modalAlertVali;
	}


	public void setModalAlertVali(boolean modalAlertVali) {
		this.modalAlertVali = modalAlertVali;
	}


	public boolean preValidate(){
		System.out.println("-"+modifyURL+"-");
		
		if(modifyURL.equals(null) || modifyURL.replace(" ", "").equals("")){
			setMsnmodal("El campo URL es obligatorio");
			return false;
		}else{
			return true;
		}		
	}
	
	public void aceptsModifyURL(){
		System.out.println("------------------>"+modifyURL);
		if(preValidate()){
			for (UListbank uListbank : bankList) {
				if(uListbank.getBankId()==bankId){
					uListbank.setBankUrl(modifyURL);
					setModifyURL("");
				}
			}		
			ocult();
		}else{
			setModalAlertVali(true);
		}		
	}
	
	public void selectChek(){
		for (UListbank uListbank : bankList) {
			if(uListbank.getBankId()==bankId){
				uListbank.setBankProgramming(modifyPermProgramm);				
			}
		}
	}
	
	public void confirmModify(){
		setMsnmodalURL(""); // Cristhian Buchely : Se agrega esta linea para no loggear acciones pasadas en seguimiento
		setMsnmodal("");
		setMsnmodal("Está seguro de realizar los siguientes cambios:<br>");
		for (UListbank uListbankOrg : bankList) {
			for (UListbank uListbantemp : bankListTemp) {
				if(uListbankOrg.getBankId().longValue()==uListbantemp.getBankId().longValue()){					
					if(uListbankOrg.isBankProgramming()!=uListbantemp.isBankProgramming()){
						if(uListbankOrg.isBankProgramming()){
							setMsnmodalURL(getMsnmodalURL()+"Se habilitó ");
							setMsnmodal(getMsnmodal()+"Habilitar ");
						}else{
							setMsnmodalURL(getMsnmodalURL()+"Se deshabilitó ");
							setMsnmodal(getMsnmodal()+"Deshabilitar ");
						}
						if(!uListbankOrg.getBankUrl().equals(uListbantemp.getBankUrl())){
							setMsnmodalURL(getMsnmodalURL()+"y modificó la URL a la entidad " + uListbankOrg.getBankName()+" \n\r");
							setMsnmodal(getMsnmodal()+"y modificar la URL a la entidad " + uListbankOrg.getBankName()+" <br>");							
							bankListSend.add(uListbankOrg);
						}else{
							setMsnmodalURL(getMsnmodalURL()+"la entidad "+uListbankOrg.getBankName()+" \n\r");
							setMsnmodal(getMsnmodal()+"la entidad "+uListbankOrg.getBankName()+" <br>");
							bankListSend.add(uListbankOrg);
						}
						setFlag(1);
					}else{
						if(!uListbankOrg.getBankUrl().equals(uListbantemp.getBankUrl())){
							setMsnmodalURL(getMsnmodalURL()+"Se modificó la URL a la entidad " + uListbankOrg.getBankName()+" \n\r");
							setMsnmodal(getMsnmodal()+"Modificar la URL a la entidad " + uListbankOrg.getBankName()+" <br>");
							setFlag(1);							
							bankListSend.add(uListbankOrg);
						}						
					}					
				}
			}			
		}
		if(getFlag()==1){			
			setModalconfirm(true);
		}else{
			setMsnmodal("No se han encontrado cambios");
			setModalAlert(true);
		}		
	}
	
	public void editURLBank(){	
		System.out.println("------------------>"+bankId);
		setModifyURL("");
		setModalModifyURL(true);
	}
	
	public void ocult(){
		setMsnmodal("");
		setModalModifyURL(false);
		setModalAlert(false);
		setModalconfirm(false);		
		setModalAlertVali(false);
		setFlag(0);
	}
	
	public void ocultTwo(){
		setModalAlertVali(false);
		setMsnmodal("");
		setModalconfirm(false);		
	}
	
	public void apply(){
		setModalconfirm(false);
		boolean setChangesResult=false;
		
		try
		{
		 setChangesResult=bankEJB.setListBankAutomaticProgramming(bankListSend,getMsnmodalURL(),getUsrs().getUserId(),getIp());
		}
		catch(Exception e){}
		
		if(setChangesResult)
		{
			init();
			setMsnmodal("Se han realizado los cambios con Éxito");
			setModalAlert(true);			
		}
		else
		{
			setMsnmodal("Ocurrió un error en el proceso. Intente nuevamente");
			setModalAlert(true);
		}
	}		
	public void cancelModify(){
		ocult();
		init();
	}
	
}
