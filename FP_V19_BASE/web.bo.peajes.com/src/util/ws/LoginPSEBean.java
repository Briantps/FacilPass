/**
 * 
 */
package util.ws;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import mBeans.LoginMBean;

import jpa.TbSystemUser;
import security.UserLogged;
import sessionVar.SessionUtil;
import util.LoginUser;

import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.application.ConfigNavigationCase;

import ejb.Login;
import ejb.Permission;
import ejb.SecurityQuestions;
import ejb.SystemParameters;

/**
 * @author jromero
 * 
 */
public class LoginPSEBean implements Serializable {
	private static final long serialVersionUID = 1476236986501936932L;

	// Attributes ------------------------------------------------------
		
	@EJB(mappedName = "ejb/Login")
	private Login loginEJB;

	@EJB(mappedName= "ejb/Permission")
	private Permission permission;
	
	@EJB(mappedName="ejb/SecurityQuestions")
	private SecurityQuestions sq;
	
	@EJB(mappedName="ejb/SystemParameters")
	private SystemParameters sp;
	
	@EJB(mappedName ="util/ws/PseWS")
	private PseWS pseWS;
	
	private String mail;
	
	private String password;

	private String userName;
	
	private String lastName;
	
	private Long userId;
	
	private String ip = SessionUtil.ip();
	
	private String lastEntry;

	private List<util.Permission> listPermission;

	private TbSystemUser systemUser;

	private boolean exist;

	private boolean logged;

	private String sessionId;
	
	private String modalMessage;
	
	private UserLogged currentUser;
	
	private List<String> permissionPageList;
	
	private boolean showMesssage;
	private boolean showMesssage1;
	
	private String msgCambioContraseña;
	private String msgQuestionResponse;
	
	private String userMasterName;
	
	private String lastMasterName;
	
	private boolean showClientMaster;
	private boolean showOtp;	
	private Long otp;
	private boolean showMsjOtp;
	private boolean showMsjUsExis;
	private boolean showAcptYesSup;
	private boolean showAcptNoSup;
	private boolean showCreateQuestions;
	
	private String pseIdStr;
	
	private Long pseId;
	
	private String txtLogin;
	
	// Constructor -------------------------------------
	public LoginPSEBean() {
		exist = false;
		showMesssage = false;
		modalMessage = "";	
		showClientMaster = false;
		showOtp = false;
		showMsjOtp = false;
		this.setShowMsjUsExis(false);
		this.setShowAcptNoSup(false);
		this.setShowAcptYesSup(false);
		
	}
	
	@PostConstruct
	public void init(){
		System.out.println("init.validateUserPSE.pseIdStr1: "+pseIdStr);
		pseIdStr=(String)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pse");
		System.out.println("init.validateUserPSE.pseIdStr: "+pseIdStr);
		String val="outPSE";
		if(pseIdStr!=null){
			pseId=Long.parseLong(pseWS.decodePSETransaction(pseIdStr));
			System.out.println("validateUserPSE.pseId: "+pseId);
		}else{
			FacesContext.getCurrentInstance().getApplication().getNavigationHandler().
			handleNavigation(FacesContext.getCurrentInstance(), null, 
					val);
		}
	}
	
	// Getters and setters -----------------------------------------------------------------------------------

	/**
	 * @return exist
	 */
	public boolean isExist() {
		return exist;
	}
	
	/**
	 * Setter for exist
	 * @param exist
	 */
	public void setExist(boolean exist) {
		this.exist = exist;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public List<util.Permission> getListPermission() {
		return listPermission;
	}

	public void setListPermission(List<util.Permission> listPermission) {
		this.listPermission = listPermission;
	}

	public void setSystemUser(TbSystemUser systemUser) {
		this.systemUser = systemUser;
	}

	public TbSystemUser getSystemUser() {
		return systemUser;
	}

	public void setMail(String mail) {
		this.mail = mail.toLowerCase().trim();
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMail() {
		return mail;
	}

	public String getPassword() {
		return password;
	}

	public void setLogged(boolean logged) {
		this.logged = logged;
	}

	public boolean isLogged() {
		return logged;
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
	 * @param sessionId the sessionId to set
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * @param currentUser the currentUser to set
	 */
	public void setCurrentUser(UserLogged currentUser) {
		this.currentUser = currentUser;
	}

	/**
	 * @return the currentUser
	 */
	public UserLogged getCurrentUser() {
		return currentUser;
	}

	/**
	 * @param permissionPageList the permissionPageList to set
	 */
	public void setPermissionPageList(List<String> permissionPageList) {
		this.permissionPageList = permissionPageList;
	}

	/**
	 * @return the permissionPageList
	 */
	public List<String> getPermissionPageList() {
		return permissionPageList;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastEntry(String lastEntry) {
		this.lastEntry = lastEntry;
	}

	public String getLastEntry() {
		return lastEntry;
	}

	/**
	 * @param showMesssage the showMesssage to set
	 */
	public void setShowMesssage(boolean showMesssage) {
		this.showMesssage = showMesssage;
	}

	/**
	 * @return the showMesssage
	 */
	public boolean isShowMesssage() {
		return showMesssage;
	}
	
	/**
	 * @param showMesssage the showMesssage1 to set
	 */
	public void setShowMesssage1(boolean showMesssage1) {
		this.showMesssage1 = showMesssage1;
	}
	
	/**
	 * @return the showMesssage1
	 */
	public boolean isShowMesssage1() {
		return showMesssage1;
	}
	
	/**
	 * @return the msgCambioContraseña
	 */
	public String getMsgCambioContraseña() {
		return msgCambioContraseña;
	}

	/**
	 * @param msgCambioContraseña the msgCambioContraseña to set
	 */
	public void setMsgCambioContraseña(String msgCambioContraseña) {
		this.msgCambioContraseña = msgCambioContraseña;
	}

	public void setUserMasterName(String userMasterName) {
		this.userMasterName = userMasterName;
	}

	public String getUserMasterName() {
		return userMasterName;
	}

	public void setLastMasterName(String lastMasterName) {
		this.lastMasterName = lastMasterName;
	}

	public String getLastMasterName() {
		return lastMasterName;
	}

	public void setShowClientMaster(boolean showClientMaster) {
		this.showClientMaster = showClientMaster;
	}

	public boolean isShowClientMaster() {
		return showClientMaster;
	}

	public void setShowOtp(boolean showOtp) {
		this.showOtp = showOtp;
	}

	public boolean isShowOtp() {
		return showOtp;
	}
	public void setOtp(Long otp) {		
		this.otp = otp;
	}

	public Long getOtp() {
		return otp;
	}
	
	public void setShowMsjOtp(boolean showMsjOtp) {
		this.showMsjOtp = showMsjOtp;
	}

	public boolean isShowMsjOtp() {
		return showMsjOtp;
	}
	
	public void setShowMsjUsExis(boolean showMsjUsExis) {
		this.showMsjUsExis = showMsjUsExis;
	}

	public boolean isShowMsjUsExis() {
		return showMsjUsExis;
	}
	
	public void setShowAcptYesSup(boolean showAcptYesSup) {
		this.showAcptYesSup = showAcptYesSup;
	}


	public boolean isShowAcptYesSup() {
		return showAcptYesSup;
	}


	public void setShowAcptNoSup(boolean showAcptNoSup) {
		this.showAcptNoSup = showAcptNoSup;
	}


	public boolean isShowAcptNoSup() {
		return showAcptNoSup;
	}
	
	// Actions ----------------------------------------
	/**
	 * Validates an User
	 */
	public String validateUser() {
		System.out.println("validateUser.pseId1: "+pseId);
		System.out.println("validateUser.pseIdStr1: "+pseIdStr);
		showClientMaster = false;
		LoginUser lu = loginEJB.validateUser(mail, password, ip);	
		systemUser = lu.getLoginUser();
		
		if (systemUser != null) {
			System.out.println("115444");
			if(pseWS.validateUserTransaction(pseId, systemUser.getUserId())){
				System.out.println("51478888");
				showOtp = false;
				this.setShowMsjUsExis(false);
				setExist(false);
				setModalMessage("");
				if (systemUser.getTbCodeType().getCodeTypeId() != 3) {
					userName     = systemUser.getUserNames();
					lastName	=	systemUser.getUserSecondNames(); //Person
				}else{
					userName     = systemUser.getUserNames();//Corporate body
					lastName	=	systemUser.getUserSecondNames(); //Person
				}
						
				userId = systemUser.getUserId();					
						
				/**
				* @author ablasquez
				* valida si es usuario de cliente y visualiza el nombre del cliente
				*/
				showClientMaster = false;
				if(systemUser.getSystemUserMasterId()!=null){
					TbSystemUser um = null;
					um = loginEJB.getUserMasterById(systemUser.getSystemUserMasterId());
					if(um!=null){
						if (um.getTbCodeType().getCodeTypeId() != 3) {
							userMasterName  = um.getUserNames();
							lastMasterName	= um.getUserSecondNames(); //Person
						}else{
							userMasterName  = um.getUserNames();
							lastMasterName	= ""; //Person
						}					
						showClientMaster = true;
					}				
				}
						
				Timestamp fecha = (systemUser.getUserLastLogin()!=null?systemUser.getUserLastLogin():new Timestamp(System.currentTimeMillis()));
						
				lastEntry = (": "+loginEJB.getLastEntry(fecha));
						
				currentUser = new UserLogged();
				currentUser.setUserId(systemUser.getUserId());
				currentUser.setUserName(userName);					
		
				/**
				* Variable encargadas de mostrar el mensaje inicial al usuario en sesion 
				* @author psanchez
				*/
				String reminderAnswersQuestions = loginEJB.questionResponseReminder(userId);
				String timeChangePassword       = loginEJB.passwordChangeReminder(userId);
				boolean changePasswordReminder  = loginEJB.msgChangeReminder(userId);
				
				msgCambioContraseña = timeChangePassword;
				msgQuestionResponse = reminderAnswersQuestions;
		
				//Getting User Permissions
				listPermission = permission.getPermissionByUser(systemUser.getUserId());
						
				//Getting the navigation configured Map List
				Map<String, List<ConfigNavigationCase>> navRule = ApplicationAssociate
				.getInstance(FacesContext.getCurrentInstance().getExternalContext())
				.getNavigationCaseListMappings();
						
				// List of navigation cases
				List<ConfigNavigationCase> list = new ArrayList<ConfigNavigationCase>();
						
				//Getting the navigation rule for from-view-id = /jsf/* witch is basically the side menu.
				list= navRule.get("/jsf/*");
						
				permissionPageList = new ArrayList<String>();
				permissionPageList.add("/jsf/home.jsp");
				for(util.Permission p: listPermission){
					String outcome = p.getOptAct().getBehavior();
					System.out.println("outcome: "+outcome);
					for (ConfigNavigationCase o : list){					
						if(outcome.startsWith("#")){
							System.out.println("111");
							if(outcome.equals(o.getFromAction())){
								System.out.println("112");
								permissionPageList.add(o.getToViewId());
							}
						}else if(outcome.equals(o.getFromOutcome())){
							System.out.println("113");
							permissionPageList.add(o.getToViewId());
						}
					}
				}
				try {
					try{	
						FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", currentUser);
						System.out.println("user_pse");
					}catch(IllegalStateException i){
						System.out.println("[ Error en LoginPSEBean.validateUser.FacesContext ]");
						i.printStackTrace();
						modalMessage = "Se ha Presentado un Error en el Sistema. Por favor Intente mas tarde.";
						exist = true;
						setLogged(true);
						return "outPSE";
					}
					
					/**@author Nramírez
					* Se agrega validación si no existen preguntas de seguridad configuradas por el usuario,
					* ejecuta acciones dependiendo de si es usuario existente o nuevo*/
					if(!sq.validateQuestionsResponse(systemUser.getUserId())){
						//No tiene preguntas de seguridad configuradas							
						System.out.println("Configuración de preguntas de seguridad");							
						if(systemUser.getUserOtpState()==0){ //Usuarios existentes								
							if(sq.userResponseRelation(systemUser.getUserId())==false){//No ha superado el tiempo para definir las pregurnas de seguirdad
								System.out.println("888777");
								modalMessage = sp.getParameterValueById(49L);
								this.setShowCreateQuestions(true);
								this.setShowMsjUsExis(false);
							}else { //Superó el tiempo para definir las preguntas de seguridad
								System.out.println("889");
								modalMessage = sp.getParameterValueById(49L);
								this.setShowMsjUsExis(true);
								this.setShowCreateQuestions(false);
							}
						}else if(systemUser.getUserOtpState()==1){ // Usuarios Nuevos
							System.out.println("888");
							this.setShowCreateQuestions(false);
							showOtp = true;
						}							
					}else if(systemUser.getUserOtpState()==1){
						System.out.println("899999999999999"+lu.getMessage());	
						this.setUserId(currentUser.getUserId());
						this.setLogged(true);
						return "successPSE";
					}else{
						if(timeChangePassword.length()>0&& changePasswordReminder){
							System.out.println("1");	
							int fl = systemUser.getUserFirstLogin() == null ? 0 : systemUser.getUserFirstLogin();					
							if (fl == 0){
								System.out.println("2");
								setLogged(true);
								return "successPSE";
							}else {
								boolean res4 = loginEJB.preenrollUser(userId);	//valida el mensaje que aparece al ingresar a la aplicación.
								if(res4==true){
									this.setShowMesssage1(true);
									System.out.println("3");
									setLogged(true);
									return "successPSE";
								}else { 	
									System.out.println("4");
									setLogged(true);
									this.setShowMesssage(true);
									return "successPSE";
								}
							}
						}else {
							int fl = systemUser.getUserFirstLogin() == null ? 0 : systemUser.getUserFirstLogin();					
							if (fl == 0){
								System.out.println("5");
								setLogged(true);
								return "successPSE";
							}else{ 
								boolean res4 = loginEJB.preenrollUser(userId);	
								if(res4==true){
									this.setShowMesssage1(true);
									System.out.println("6");
									setLogged(true);
									return "successPSE";
								}else {
									System.out.println("7"); //valida el mensaje que aparece al ingresar a la aplicación.
									this.setShowMesssage(false);
									setLogged(true);
									return "successPSE";
								} 
							}	
						}
					}
				} catch (NullPointerException n){
					System.out.println("[ Error en LoginPSEBean.validateUser ]");
					n.printStackTrace();
					setLogged(true);
					return "outPSE";
				}catch(Exception e){
					System.out.println("[ Error en LoginPSEBean.validateUser.Exception ]");
					e.printStackTrace();	
					setLogged(false);
					modalMessage = "Se ha Presentado un Error en el Sistema. Por favor Intente mas tarde.";
					exist = true;
					return "outPSE";			
				}
			}else{
				System.out.println("LoginPSEBean no ingreso de otro usuario");
				return "outPSE";
			}
		}else{		
				// Not a registered user.
				System.out.println("else: "+lu.getMessage());		
				modalMessage = lu.getMessage();
				exist = true;
				/*modalMessage = "Se ha Presentado un Error en el Sistema.Por favor Ingrese nuevamente";
				exist = true;*/
				setLogged(false);
				return "outPSE";
		
		}
		return "outPSE";	
	}
	
	
	public String rememberQuestionsAfter(){
		this.sq.rememberQuestionsAfter(currentUser.getUserId(), ip);
		this.setShowCreateQuestions(false);
		
		System.out.println("7"); //valida el mensaje que aparece al ingresar a la aplicación.
		this.setShowMesssage(false);
		setLogged(true);
		return "successCP";
	}
	
	/** @author Nramirez
	 * Método encargado de validar la vigencia del código OTP Usuarios Nuevos*/
	public String validarOtp(){
		System.out.println("LoginMBean.validarOtp()");		
		System.out.println("systemUser.getUserId(): " + systemUser.getUserId());
	
		String msj ="";
		try{
			msj = loginEJB.validateOtpModal(otp.toString(), systemUser.getUserId(), ip);
			System.out.println("msj: " + msj);
			if(msj.equals("1")){
				System.out.println("Entre al primero");
				this.setModalMessage("Señor usuario su código ya expiró para lo cual se envió uno nuevo a su cuenta de correo electrónico registrada en el sistema, por favor valide su cuenta e ingréselo para poder continuar. ");
				showMsjOtp = true;
			}else if(msj.equals("2")){
				System.out.println("Entre al segundo");				
				this.setUserId(currentUser.getUserId());
				setLogged(true);
				return "securityQuestionsN";				
			}else if(msj.equals("3")){
				System.out.println("Entre al tercero");
				this.setModalMessage("Señor usuario el código ingresado no corresponde al generado por el sistema, intente nuevamente.");
				showMsjOtp = true;
			}
		}catch (Exception e) {
			e.printStackTrace();			
			this.setModalMessage("No se pudo validar el código OTP");
			this.setShowMsjOtp(true);
		}		
		return msj;
	}
		
	/** @author Nramirez
	 * Método encargado de redireccionar al módulo de configuración de preguntas Usuario Existentes*/
	public String rediConfig(){
		System.out.println("LoginMBean.rediConfig()");
		
		this.setUserId(currentUser.getUserId());
		setLogged(true);
		this.sq.InicioConfPregVP(currentUser.getUserId(), ip);
		return "securityQuestionsN";
	} 
	
	/**
	 * Invalidates the current user session.
	 */
	public String logout() {	
		//System.out.println("salida");
		FacesContext context = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) context.getExternalContext()
				.getSession(false);
		if (session != null) {
		  if(userId != null){	
			this.loginEJB.updateLastLogin(userId);
		  }	
			session.invalidate();
		}
		return "successOut";
	}
	
	public void executeRedirect(){
		logout();
		try {
			FacesContext.getCurrentInstance()
			.getExternalContext().redirect("http://www.facilpass.com");
			System.out.println("redirecciona a facilpass");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("no redirecciona");
		}
	}

	
	public String exit(){
		System.out.println("Salir por pagina wellcomePSE");
		System.out.println("exit.pseId1: "+pseId);
		System.out.println("exit.pseIdStr1: "+pseIdStr);
		FacesContext context = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) context.getExternalContext()
		.getSession(false);
		System.out.println("session: "+session);
		System.out.println("userId: "+userId);
		if (session != null) {
			System.out.println("Entro a session");
			if(userId != null){	
				System.out.println("Entro a user");
				this.loginEJB.updateLastLogin(userId);
			}	
			System.out.println("exit.pseId: "+pseId);
			System.out.println("exit.pseIdStr: "+pseIdStr);
			session.invalidate();
		}
		return "successOut";
	}
	
	/**
	 * Control modal
	 */
	public String hideModal(){
		exist = false;
		modalMessage = "";
		logged = false;
		this.setShowOtp(false);
		this.setShowMsjOtp(false);
		this.setOtp(null);
		this.setShowMsjUsExis(false);
		this.setShowAcptNoSup(false);
		this.setShowAcptYesSup(false);
		return null;
		
	}

	public void setMsgQuestionResponse(String msgQuestionResponse) {
		this.msgQuestionResponse = msgQuestionResponse;
	}

	public String getMsgQuestionResponse() {
		return msgQuestionResponse;
	}

	public void setShowCreateQuestions(boolean showCreateQuestions) {
		this.showCreateQuestions = showCreateQuestions;
	}

	public boolean isShowCreateQuestions() {
		return showCreateQuestions;
	}

	public String getTxtLogin() {
		txtLogin=(sp.getParameterValueById(66L)!=null?sp.getParameterValueById(66L):"INGRESAR");
		return txtLogin;
	}

	public void setTxtLogin(String txtLogin) {
		this.txtLogin = txtLogin;
	}
	
}