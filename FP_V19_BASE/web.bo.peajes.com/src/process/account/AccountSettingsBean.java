/**
 * 
 */
package process.account;

import java.io.Serializable;
import java.text.DecimalFormat;
//import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import jpa.TbAccount;
import jpa.TbDevice;
import jpa.TbDistributionType;
import jpa.TbProcessTrack;
import security.UserLogged;
import sessionVar.SessionUtil;
//import sun.java2d.pipe.SpanShapeRenderer.Simple;
import constant.AccountStateType;
import constant.DistributionType;
import constant.ProcessTrackDetailType;
import constant.ProcessTrackType;
import ejb.Device;
import ejb.Process;
import ejb.Purchase;
import ejb.Transaction;
import ejb.TypeDistribution;
import ejb.User;

/**
 * @author ablasquez
 *
 */
public class AccountSettingsBean implements Serializable {
	private static final long serialVersionUID = 8162813547847156018L;
	
	@EJB(mappedName = "ejb/Purchase")
	private Purchase purchase;
	
	@EJB(mappedName = "ejb/User")
	private User user;
	
	@EJB(mappedName = "ejb/TypeDistribution")
	private TypeDistribution tipos;
	
	@EJB(mappedName = "ejb/Transaction")
	private Transaction transaction;
	
	@EJB(mappedName = "ejb/Device")
	private Device deviceEJB;
	// Attributes --- //
	
	@EJB(mappedName="ejb/Process")
	private Process process;
	
	private Long idClientAccount;
	
	private List<SelectItem> clientAccounts;
	
	private Long idTypeDist;
	
	private List<SelectItem> selectTypeDist;
	
	private boolean showModal;
	
	private String modalMessage;	
	
	private boolean showConfirmationModal;
	
	private String modalConfirmationMessage;
	
	private boolean showInfo;
	
	private String typeAccount;
	
	private boolean showchage;
	
	private String messagechange;
	
	private String msgInfo;
	
	private boolean showData;
	
	private boolean showBalance;
	
	private boolean showManualDistribution;
	
	private boolean showRechargeWindow;
	
	private Long value;
	
	private String valueTxt;
	
	private Long deviceId;
	
	private Long availableBalance;
	
	private Long totalBalance;
	
	private List<TbDevice> devicesList;
	
	private String msg1;
	
	private String msg2;
	
	private boolean enableMsg2;
	
	private UserLogged usrs;
	
	private Boolean sizeDevicesList=false;


	
	/**
	 * Constructor.
	 */
	public AccountSettingsBean() {
		usrs = (UserLogged) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
		init();
	}
		
	public boolean isShowManualDistribution() {
		return showManualDistribution;
	}

	public void setShowManualDistribution(boolean showManualDistribution) {
		this.showManualDistribution = showManualDistribution;
	}

	public boolean isShowBalance() {
		return showBalance;
	}

	
	public void setShowBalance(boolean showBalance) {
		this.showBalance = showBalance;
	}
	
	public boolean isShowRechargeWindow() {
		return showRechargeWindow;
	}

	public void setShowRechargeWindow(boolean showRechargeWindow) {
		this.showRechargeWindow = showRechargeWindow;
	}

	public Long getValue() {
		return value;
	}

	public void setValue(Long value) {
		this.value = value;
	}

	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	public void setIdClientAccount(Long idClientAccount) {
		this.idClientAccount = idClientAccount;
	}
	
	public Long getTotalBalance() {
		return totalBalance;
	}

	public void setTotalBalance(Long totalBalance) {
		this.totalBalance = totalBalance;
	}

	public String getValueTxt() {
		return valueTxt;
	}

	public void setValueTxt(String valueTxt) {
		this.valueTxt = valueTxt;
	}
	
	public String getMsg1() {
		return msg1;
	}

	public void setMsg1(String msg1) {
		this.msg1 = msg1;
	}

	public String getMsg2() {
		return msg2;
	}

	public void setMsg2(String msg2) {
		this.msg2 = msg2;
	}

	public boolean isEnableMsg2() {
		return enableMsg2;
	}

	public void setEnableMsg2(boolean enableMsg2) {
		this.enableMsg2 = enableMsg2;
	}

	/**
	 * 
	 * @return
	 */
	public Long getIdClientAccount() {
		return idClientAccount;
	}

	/**
	 * 
	 * @param clientAccounts
	 */
	public void setClientAccounts(List<SelectItem> clientAccounts) {
		this.clientAccounts = clientAccounts;
	}

	/**
	 * 
	 * @return
	 */
	public List<SelectItem> getClientAccounts() {
		Long userId=user.getSystemMasterById(usrs.getUserId());
		if(clientAccounts == null){
			clientAccounts = new ArrayList<SelectItem>();
		} else {
			clientAccounts.clear();
		}
		clientAccounts.add(new SelectItem(-1L, "--Seleccione Una--"));
		
		List<TbAccount> tbAccount = null;
		
		try {
			tbAccount = user.getClientAccount(userId);
		} catch (Exception e) {
			e.printStackTrace();
			tbAccount=null;
		}
		
		if(tbAccount!=null){
			for(TbAccount su : tbAccount){
				if(su.getAccountState().longValue()==AccountStateType.ACTIVE.getId().longValue()){
					clientAccounts.add(new SelectItem(su.getAccountId(), "Cuenta No. " + su.getAccountId()));
				}			
			}
		}
		
		
		return clientAccounts;
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



	public void setSelectTypeDist(List<SelectItem> selectTypeDist) {
		this.selectTypeDist = selectTypeDist;
	}



	public List<SelectItem> getSelectTypeDist() {		
		if(selectTypeDist == null){
			selectTypeDist = new ArrayList<SelectItem>();
		} else {
			selectTypeDist.clear();
		}
		selectTypeDist.add(new SelectItem(-1L, "--Seleccione Una Opción--"));
		for(TbDistributionType t : tipos.getDistributionType()){
			selectTypeDist.add(new SelectItem(t.getDistributionTypeId(),t.getDistributionTypeName()));
		}
		return selectTypeDist;
	}



	public void setIdTypeDist(Long idTypeDist) {
		this.idTypeDist = idTypeDist;
	}



	public Long getIdTypeDist() {
		return idTypeDist;
	}
	
	

	public void setShowConfirmationModal(boolean showConfirmationModal) {
		this.showConfirmationModal = showConfirmationModal;
	}



	public boolean isShowConfirmationModal() {
		return showConfirmationModal;
	}



	public void setModalConfirmationMessage(String modalConfirmationMessage) {
		this.modalConfirmationMessage = modalConfirmationMessage;
	}



	public String getModalConfirmationMessage() {
		return modalConfirmationMessage;
	}
	
	public void setShowInfo(boolean showInfo) {
		this.showInfo = showInfo;
	}



	public boolean isShowInfo() {
		return showInfo;
	}



	public void setTypeAccount(String typeAccount) {
		this.typeAccount = typeAccount;
	}



	public String getTypeAccount() {
		return typeAccount;
	}



	public void setShowchage(boolean showchage) {
		this.showchage = showchage;
	}



	public boolean isShowchage() {
		return showchage;
	}



	public void msgSave(){
		if(idTypeDist == -1L){
			modalMessage = "No ha seleccionado un tipo de distribución";
			showModal	= true;
		} else {
			modalConfirmationMessage = "¿Está seguro de realizar este cambio?";
			showConfirmationModal = true;
			
		}	
	}

	public String hideModal() {
		showConfirmationModal = false;
		showModal = false;
		showRechargeWindow = false;
		showchage = false;
		valueTxt="";
		showRechargeWindow=false;
		return null;
	}
	
	public String hideModalChange() {
		messagechange = "";
		showchage = false;
		modalConfirmationMessage = "";
		showConfirmationModal = false;
		modalMessage = "Cambio realizado correctamente";
		showModal	= true;	
		showInfo = false;
		showData =  false;
		this.setIdClientAccount(-1L);
		return null;
	}
	
	public String save(){
		showConfirmationModal = false;
		String changeDistribution="";

		switch(idTypeDist.intValue()){
		case 1:
			changeDistribution="Bolsa de Dinero";
			break;
		case 2:
			changeDistribution="Distribución Automatica";
			break;
		case 3:
			changeDistribution="Distribución Manual";
			break;	
		}
		if(tipos.saveTypeDistribution(idClientAccount, idTypeDist,SessionUtil.ip(), SessionUtil
				.sessionUser().getUserId())){
			System.out.println(idTypeDist);
			System.out.println(DistributionType.AUTOMATIC);
			this.apply();
			messagechange = "";
			showchage = false;
			modalConfirmationMessage = "";
			showConfirmationModal = false;
			modalMessage = "Cambio realizado correctamente";
			showModal	= true;	
			showInfo = true;
			changeAccounts();
			//showData = false;
			//this.setIdClientAccount(-1L);
			
			//proceso administrador
			TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT,usrs.getUserId());
			Long idPTA;
			if(pt==null){
				idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,usrs.getUserId(),"",SessionUtil.sessionUser().getUserId());
			}
			else{
				idPTA=pt.getProcessTrackId();
			}
			Long detailA=process.createProcessDetail(idPTA, ProcessTrackDetailType.MANUAL_DISTRIBUTION,  
					"Se realizo cambio en la distribución de fondos a "+changeDistribution+" para la cuenta "+idClientAccount,usrs.getUserId(), "", 1,"",null,null,null,null);
						
			//proceso cliente
			TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,usrs.getUserId());
			Long idPTC;
			if(ptc==null){
				idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS,usrs.getUserId(),"",SessionUtil.sessionUser().getUserId());
			}
			else{
				idPTC=ptc.getProcessTrackId();
			}
			Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.MANUAL_DISTRIBUTION, 
					"Se realizo cambio en la distribución de fondos a "+changeDistribution+" para la cuenta "+idClientAccount,usrs.getUserId(),"", 1, "", null,null,null,null);	
			
			
		}else{
			modalConfirmationMessage = "";
			showConfirmationModal = false;
			modalMessage = "Se ha presentado un Error";
			showModal	= true;
			this.setIdClientAccount(-1L);
			this.setIdTypeDist(-1L);
			typeAccount = "";
			showInfo = false;
			messagechange = "";
			showchage = false;
			showData = false;
		}
		return null;
	}
	
	public void init(){
		typeAccount = "";		
		modalConfirmationMessage = "";
		showConfirmationModal = false;
		modalMessage = "";
		showModal = false;
		showInfo = false;
		this.setIdClientAccount(-1L);
		this.setIdTypeDist(-1L);
		messagechange = "";
		showchage = false;
		showData = false;
	}
	
	public String changeAccounts(){
		System.out.println("changeAccounts:"+idClientAccount);
		if(!(idClientAccount == -1L)){
			idTypeDist=tipos.getAccount(idClientAccount).getDistributionTypeId().getDistributionTypeId();
			if(idTypeDist==DistributionType.AUTOMATIC.getDistributionID().longValue()){
				setMsg2("Saldo Cuenta FacilPass:");
				setMsg1("");
				setEnableMsg2(false);
				setShowBalance(true);
				setShowManualDistribution(false);
			}else if(idTypeDist==DistributionType.MANUAL.getDistributionID().longValue()){
				setMsg2("Saldo Disponible para Distribución:");
				setMsg1("Saldo Cuenta FacilPass:");
				setEnableMsg2(true);
				setShowBalance(true);
				setShowManualDistribution(true);
			}else{
				setMsg2("Saldo Cuenta FacilPass:");
				setMsg1("");
				setEnableMsg2(false);
				setShowBalance(false);
				setShowManualDistribution(false);
			}
			typeAccount = tipos.getAccount(idClientAccount).getDistributionTypeId().getDistributionTypeName();
			availableBalance = transaction.getAvailableBalanceAccount(idClientAccount);
			devicesList = deviceEJB.getDevicesByAccount(idClientAccount);
			if(devicesList.size()>0){
				  setSizeDevicesList(true);	
				}
			else {
				setSizeDevicesList(false);
			}
			
			if(idTypeDist==DistributionType.AUTOMATIC.getDistributionID().longValue()){
				totalBalance=null;
				for(int i=0;i<devicesList.size();i++){
					availableBalance=availableBalance+devicesList.get(i).getDeviceCurrentBalance().longValue();
				}
			}else if(idTypeDist==DistributionType.MANUAL.getDistributionID().longValue()){
				totalBalance=0L;
				for(int i=0;i<devicesList.size();i++){
					totalBalance=totalBalance+devicesList.get(i).getDeviceCurrentBalance().longValue();
				}
				totalBalance=totalBalance+availableBalance;
			}else{
				totalBalance=null;
			}
			showInfo = true;			
			modalConfirmationMessage = "";
			showConfirmationModal = false;
			modalMessage = "";
			showModal = false;
			messagechange = "";
			showchage = false;
			showData = true;
		} else {
			typeAccount = "";		
			modalConfirmationMessage = "";
			showConfirmationModal = false;
			modalMessage = "";
			showModal = false;
			showInfo = false;
			messagechange = "";
			showchage = false;
			showData = false;
		}		
		return null;
	}

	public String apply(){
		if(tipos.applyChageInDevice(idClientAccount, idTypeDist,SessionUtil.ip(), SessionUtil
					.sessionUser().getUserId())){
			
			messagechange = "";
			showchage = false;
			modalConfirmationMessage = "";
			showConfirmationModal = false;
			modalMessage = "Cambio realizado correctamente";
			showModal	= true;	
			showInfo = false;
			//showData = false;
			//this.setIdClientAccount(-1L);
			//this.setIdTypeDist(-1L);
		}else{
			modalConfirmationMessage = "";
			showConfirmationModal = false;
			modalMessage = "Se ha presentado un Error";
			showModal	= true;
			this.setIdClientAccount(-1L);
			this.setIdTypeDist(-1L);
			typeAccount = "";
			showInfo = false;
			messagechange = "";
			showchage = false;
			showData = false;
		}
		return null;
	}

	public void setMessagechange(String messagechange) {
		this.messagechange = messagechange;
	}



	public String getMessagechange() {
		return messagechange;
	}



	public void setMsgInfo(String msgInfo) {
		this.msgInfo = msgInfo;
	}



	public String getMsgInfo() {
		return msgInfo;
	}
	
	public void changeOptions(){
		msgInfo = tipos.getDescriptionType(idTypeDist);		
	}



	public void setShowData(boolean showData) {
		this.showData = showData;
	}



	public boolean isShowData() {
		return showData;
	}



	public void setAvailableBalance(Long availableBalance) {
		this.availableBalance = availableBalance;
	}



	public Long getAvailableBalance() {
		return availableBalance;
	}



	public void setDevicesList(List<TbDevice> devicesList) {
		this.devicesList = devicesList;
	}



	public List<TbDevice> getDevicesList() {
		return devicesList;
	}

	public String initRecharge() {
		setShowRechargeWindow(true);
		setValue(null);
		setShowConfirmationModal(false);
		setModalConfirmationMessage(null);
		setShowModal(false);
		setModalMessage(null);
		setShowchage(false);
		setMessagechange(null);
		showModal = false;
		modalMessage = "";
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
public String initConfirm() {
		
		Boolean validation=false;
		value = 0L;

		try {
			value = Long.parseLong(valueTxt.replace(".", "").replace(",", ""));
		} catch (Exception e) {
			validation=true;
		}
		
		if(valueTxt.equals("")){
			modalMessage =("Error, No ha digitado un valor para la recarga del dispositivo.");
			showModal = (true);
			showRechargeWindow=false;
		}
		
		else if(!valueTxt.matches("([0-9.]|\\s)+")){
			modalMessage =("El valor ingresado tiene caracteres inválidos. Verifique.");
			showModal = (true);
			showRechargeWindow=false;
		}
		
		else if(validation){
			modalMessage =("Error, por favor intente de nuevo.");
			showModal = (true);
			showRechargeWindow=false;
		}
		
		else if(value <= 0L){
			modalMessage =("Error, El valor digitado debe ser mayor a Cero. Verifique.");
			showModal = (true);
			showRechargeWindow=false;
			valueTxt="";
		} else if(value.longValue() > transaction.getAvailableBalanceAccount(idClientAccount).longValue()) {
			modalMessage =("Error, El valor digitado es mayor que el saldo disponible. Verifique.");
			showModal = (true);
			showRechargeWindow=false;
			valueTxt="";
		} else {
			setShowRechargeWindow(false);
			setShowConfirmationModal(false);
			setModalMessage(null);
			setShowModal(false);
			modalMessage = "";
			setModalConfirmationMessage(null);
			setMessagechange("¿Está seguro de realizar este cambio?");
			setShowchage(true);
			valueTxt="";	
			showRechargeWindow=false;
		}
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public String saveRecharge() {
		setShowchage(false);
		System.out.println(deviceId);

		
		if (purchase.recharge1(deviceId, value, idClientAccount, SessionUtil
				.ip(), SessionUtil.sessionUser().getUserId(), null, SessionUtil
				.sessionUser().getUserEmail())) {
			DecimalFormat n = new DecimalFormat("#,###,###,###");
			modalMessage =("Transacción Exitosa.");
			typeAccount = tipos.getAccount(idClientAccount).getDistributionTypeId().getDistributionTypeName();
			devicesList = deviceEJB.getDevicesByAccount(idClientAccount);
			availableBalance = transaction.getAvailableBalanceAccount(idClientAccount);
			
			//proceso administrador
			TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT,usrs.getUserId());
			Long idPTA;
			if(pt==null){
				idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,usrs.getUserId(),"",SessionUtil.sessionUser().getUserId());
			}
			else{
				idPTA=pt.getProcessTrackId();
			}
			
			String idVehicle=null;
			String plate=null;
			
			try {
				idVehicle = purchase.selectReAccountDevice(deviceId);
				plate = purchase.getTbVehiclePlate(idVehicle);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			
			Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.MANUAL_DISTRIBUTION, 
					"Se realizó Distribución Manual por valor de: "+n.format(value)+" para la placa "+plate,usrs.getUserId(),"", 1, "", null,null,null,null);			
			//proceso cliente
			TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,usrs.getUserId());
			Long idPTC;
			if(ptc==null){
				idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS,usrs.getUserId(),"",SessionUtil.sessionUser().getUserId());
			}
			else{
				idPTC=ptc.getProcessTrackId();
			}
			Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.MANUAL_DISTRIBUTION, 
					"Se realizó Distribución Manual por valor de: "+n.format(value)+" para la placa "+plate,usrs.getUserId(),"", 1, "", null,null,null,null);
			//msg="Transacción Exitosa.";	
			
		} else {
			modalMessage =("El dispositivo se encuentra inactivo.");
		}
		valueTxt="";
		setShowModal(true);
		return null;
	}

	public void setSizeDevicesList(Boolean sizeDevicesList) {
		this.sizeDevicesList = sizeDevicesList;
	}

	public Boolean getSizeDevicesList() {
		return sizeDevicesList;
	}
	
	
}