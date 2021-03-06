/**
 * 
 */
package mBeans;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import jpa.TbSystemUser;
import security.UserLogged;
import sessionVar.SessionUtil;
import util.AllInfoAccount;
import util.Encryptor;
import util.LoginUser;
import util.ws.PseWS;

import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.application.ConfigNavigationCase;

import constant.TPS_Constants;

import ejb.Login;
import ejb.Permission;
import ejb.SecurityQuestions;
import ejb.SystemParameters;
import ejb.User;

/**
 * @author choyos
 * 
 */
public class LoginMBean implements Serializable {
	private static final long serialVersionUID = 1476236986501936932L;

	// Attributes ------------------------------------------------------
		
	@EJB(mappedName = "ejb/Login")
	private Login loginEJB;

	@EJB(mappedName= "ejb/Permission")
	private Permission permission;
	
	@EJB(mappedName="ejb/SecurityQuestions")
	private SecurityQuestions securityQuestionsEJB;
	
	@EJB(mappedName="ejb/SystemParameters")
	private SystemParameters sp;
	
	@EJB(mappedName ="util/ws/PseWS")
	private PseWS pseWS;
	
	@EJB(mappedName="ejb/User")
	private User userEJB;
	
	private String mail;
	
	private String password;

	private String userName;
	
	private String lastName;
	
	private Long userId;
	
	private String ip = SessionUtil.ip();
	
	private String lastEntry;

	private List<util.Permission> listPermission;
	private List<AllInfoAccount> accountList;

	private TbSystemUser systemUser;
	
	private UserLogged currentUser;
	
	private List<String> permissionPageList;
	
	private String modalMessage;
	private String msgCambioContrase�a;
	private String msgQuestionResponse;
	private String userMasterName;
	private String lastMasterName;
	private String sessionId;
	private String otp;
	
	private boolean exist;
	private boolean logged;
	private boolean showClientMaster;
	private boolean showOtp;	
	private boolean showMsjOtp;
	private boolean showMsjOtpValidity;
	private boolean showMsjUsExis;
	private boolean showAcptYesSup;
	private boolean showAcptNoSup;
	private boolean showCreateQuestions;
	private boolean showMesssage;
	private boolean showMesssage1;
	
	private boolean showRegistred;
	private String message;
	
	private String pseIdStr;
	private String txtLogin;
	private Long pseId;

	private int count;

	//@author TPS r.bautista
	private static final String INGRESAR = "INGRESAR";


	// Constructor -------------------------------------
	public LoginMBean() {
		exist = false;
		showMesssage = false;
		showMesssage1 = false;
		modalMessage = "";	
		showClientMaster = false;
		showOtp = false;
		showMsjOtp = false;
		showMsjOtpValidity = false;
		showCreateQuestions = false;
		this.setShowMsjUsExis(false);
		this.setShowAcptNoSup(false);
		this.setShowAcptYesSup(false);

		this.showRegistred=true;
		//message =getMessage();
		//Si la URL Aval ha exedido el tiempo maximo permitido correspondiente al parametro 77 oculta botones y muestra mensaje del parametro 81
		String into =(String)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("aval");
		if(into	!= null){
			message ="Aqui debe ir el texto Configurable";
			this.setMessage(message);
			this.showRegistred=false;
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
		// init TPS r.bautista
		String value = (String)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(TPS_Constants.MENSAJE_TXT_LOGIN);
		if (value != null){
			this.setShowRegistred(false);
		}
		// end TPS r.bautista
		
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
	 * @return the msgCambioContrase�a
	 */
	public String getMsgCambioContrase�a() {
		return msgCambioContrase�a;
	}

	/**
	 * @param msgCambioContrase�a the msgCambioContrase�a to set
	 */
	public void setMsgCambioContrase�a(String msgCambioContrase�a) {
		this.msgCambioContrase�a = msgCambioContrase�a;
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
	public void setOtp(String otp) {		
		this.otp = otp;
	}

	public String getOtp() {
		return otp;
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
		try{
		boolean validate=true;
		boolean noValidate=false;


		// @author TPS r.bautista init
		String redirect = (String)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(TPS_Constants.MENSAJE_TXT_LOGIN);
		if (redirect != null) {
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(TPS_Constants.MENSAJE_TXT_LOGIN);

		}
		// end

		pseIdStr=(String)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pse");
		
		if(pseIdStr!=null){
			pseId=Long.parseLong(pseWS.decodePSETransaction(pseIdStr));
		}else{
			pseId=null;
		}
		showClientMaster = false;
		LoginUser lu = loginEJB.validateUser(mail, password, ip);	
		systemUser = lu.getLoginUser();
		
		HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		
		if(origRequest.getPathInfo().contains("login.jspx")&&pseId==null){
			exist = false;
			modalMessage = "Se ha Presentado un Error en el Sistema.Por favor Ingrese nuevamente";
			setLogged(true);
			return "outPSE";
		}else{
			if (systemUser != null) {
				if(pseId!=null&&pseWS.validateUserTransaction(pseId, systemUser.getUserId())==false){
					validate=false;
				}
				if(validate){
					showOtp = false;
					this.setShowMsjUsExis(false);
					setExist(false);			
					setModalMessage("");
					if (systemUser.getTbCodeType().getCodeTypeId() != 3) {
						userName    = systemUser.getUserNames();
						lastName	= systemUser.getUserSecondNames(); //Person
					}else{
						userName    = systemUser.getUserNames();//Corporate body
						lastName	= systemUser.getUserSecondNames(); //Person
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
						for (ConfigNavigationCase o : list){					
							if(outcome.startsWith("#")){
								if(outcome.equals(o.getFromAction())){
									permissionPageList.add(o.getToViewId());
								}
							}else if(outcome.equals(o.getFromOutcome())){
								permissionPageList.add(o.getToViewId());
							}
						}
					}
					try {
						try{	
							FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", currentUser);						
						}catch(IllegalStateException i){
							System.out.println("[ Error en LoginMBean.validateUser.FacesContext ]");
							i.printStackTrace();
							modalMessage = "Se ha Presentado un Error en el Sistema. Por favor Intente mas tarde.";
							exist = true;
							setLogged(false);
							showMsjOtpValidity = false;
							if(pseId!=null){
								return "outPSE";
							}else{
								return "sucess";
							}
						}
						
						if(Encryptor.verifyPwd(systemUser.getUserPassword(), systemUser.getUserCode()) && 
						  (systemUser.getUserFirstLogin()==4 || systemUser.getUserFirstLogin()==5)){
							System.out.println("10");
							this.setShowOtp(true);
						}else if(Encryptor.verifyPwd(systemUser.getUserPassword(), systemUser.getUserCode()) && 
								systemUser.getUserOtpState()==0){
							System.out.println("35");
							userEJB.resetPasswordClient(systemUser.getUserId(), ip, systemUser.getUserId());
							this.setShowOtp(true);
						}else if(!Encryptor.verifyPwd(systemUser.getUserPassword(), systemUser.getUserCode()) &&
		                         !securityQuestionsEJB.validateQuestionsResponse(systemUser.getUserId()) && 
		                         (systemUser.getUserFirstLogin()==4 || systemUser.getUserFirstLogin()==5)){
							System.out.println("11");
							this.setUserId(currentUser.getUserId());
							this.setLogged(true);
							if(pseId!=null){
								return "successPSE";
							}else{
								return "securityQuestionsNew";
							}
						}else if(Encryptor.verifyPwd(systemUser.getUserPassword(), loginEJB.userOtpLogin(systemUser.getUserId())) && 
								 securityQuestionsEJB.validateQuestionsResponse(systemUser.getUserId())){
							loginEJB.inactiveOtp(loginEJB.userOtpLogin(systemUser.getUserId()), systemUser.getUserId());
							System.out.println("12");
							this.setUserId(currentUser.getUserId());
							this.setLogged(true);
							if(pseId!=null){
								return "successPSE";
							}else{
								return "passwordReset";
							}
						}
						/** @author psanchez
						* Se agrega validaci�n si no existen preguntas de seguridad configuradas por el usuario,
						* ejecuta acciones dependiendo de si es usuario existente o nuevo*/
						else if(!securityQuestionsEJB.validateQuestionsResponse(systemUser.getUserId())){
							//No tiene preguntas de seguridad configuradas							
							if(systemUser.getUserOtpState()==0){ //Usuarios existentes								
								if(securityQuestionsEJB.userResponseRelation(systemUser.getUserId())==false){//No ha superado el tiempo para definir las preguntas de seguirdad
									modalMessage = sp.getParameterValueById(49L);
									this.setShowCreateQuestions(true);
									this.setShowMsjUsExis(false);
								}else { //Super� el tiempo para definir las preguntas de seguridad
									modalMessage = sp.getParameterValueById(49L);
									this.setShowMsjUsExis(true);
									this.setShowCreateQuestions(false);
								}
							}else if(systemUser.getUserOtpState()==1){ // Usuarios Nuevos
								this.setShowCreateQuestions(false);
								this.setShowOtp(true);
							}							
					    }else if(Encryptor.verifyPwd(systemUser.getUserPassword(), systemUser.getUserCode()) && 
								systemUser.getUserOtpState()==1){
							System.out.println("36");
							this.setShowOtp(true);
						}else {
							/**
							* Variable encargadas de mostrar el mensaje inicial al usuario en sesion 
							* @author psanchez
							*/
							this.setMsgQuestionResponse(loginEJB.questionResponseReminder(userId));
							this.setMsgCambioContrase�a(loginEJB.passwordChangeReminder(userId));
							boolean changePasswordReminder  = loginEJB.msgChangeReminder(userId);
							String messageAccount = sp.getParameterValueById(67L);
					
							if(loginEJB.preenrollUser(userId)==true){//muestra el mesaje de cliente preinscrito
								this.setShowMesssage1(true);
								System.out.println("3");
								setLogged(true);
								if(pseId!=null){
									return "successPSE";
								}else{
									return "successPU";
								}
							}else if((msgCambioContrase�a.length()>0&& changePasswordReminder==true) ||
									  msgQuestionResponse.length()>0){//muestra el mensaje de cambio de contrase�a
								System.out.println("4");
								setLogged(true);
								if(messageAccount.equals("SI") && this.getAccountLoginList().size()>0){
									this.setShowMesssage(true);
									if(pseId!=null){
										return "successPSE";
									}else{
										return "successCP";
									}
								}else if(pseId!=null){
									return "successPSE";
								}else{
									return "successCP";
								}
							}else {
								System.out.println("6"); //muestra el mensaje de politicas de datos personales
								setLogged(true);
								if(messageAccount.equals("SI") && this.getAccountLoginList().size()>0){
									this.setShowMesssage(true);
									if(pseId!=null){
										return "successPSE";
									}else{
										return "success";
									}
								}else if(pseId!=null){
									return "successPSE";
								}else{
									return "success";
								}
							} 
						}
					} catch (NullPointerException n){
						System.out.println("[ Error en LoginMBean.validateUser ]");
						n.printStackTrace();
						setLogged(false);
						modalMessage = "Se ha Presentado un Error en el Sistema. Por favor Intente mas tarde.";
						exist = true;
						showMsjOtpValidity = false;
						return "successOut";
					}catch(Exception e){
						System.out.println("[ Error en LoginMBean.validateUser.Exception ]");
						e.printStackTrace();	
						setLogged(false);
						modalMessage = "Se ha Presentado un Error en el Sistema. Por favor Intente mas tarde.";
						exist = true;
						showMsjOtpValidity = false;
						if(pseId!=null){
							return "outPSE";
						}else{
							return "successOut";
						}
					}
				}else{
					System.out.println("5");
					exist = false;
					showMsjOtpValidity = true;
					modalMessage = "Se ha Presentado un Error en el Sistema.Por favor Ingrese nuevamente";
					setLogged(true);
					return "outPSE";
				}
			}else if(lu.getMessage().equals("1")){		
					// Not a registered user.
					modalMessage = "La informaci�n de nombre de usuario o contrase�a ingresada no es correcta, " +
							"si olvid� su contrase�a la puede restablecer dando clic en el bot�n Restablecer Contrase�a.";
					System.out.println("else---: "+modalMessage);
					exist = true;
					showMsjOtpValidity = false;
					setLogged(false);
					if(pseId!=null){
						return null;
					}else{
						return "successOut";
					}
			}else {		
				// Not a registered user.
				System.out.println("else: "+lu.getMessage());		
				modalMessage = lu.getMessage();
				exist = false;
				showMsjOtpValidity = true;
				setLogged(false);
				if(pseId!=null){
					return null;
				}else{
					return "successOut";
				}
			}
		}
		if(pseId!=null){
			if(noValidate==false){
				return "outPSE";
			}else{
				return null;
			}
		}else{
			return "successOut";
		}
		} catch (NullPointerException n){// Not a registered user.
			System.out.println("7");
			modalMessage = "La informaci�n de nombre de usuario o contrase�a ingresada no es correcta, " +
			"si olvid� su contrase�a la puede restablecer dando clic en el bot�n Restablecer Contrase�a.";
			System.out.println("NullPointerException else---: "+modalMessage);
			exist = true;
			showMsjOtpValidity = false;
			setLogged(false);
			if(pseId!=null){
				return null;
			}else{
				return "successOut";
			}
		}
	}
	
	
	public String rememberQuestionsAfter(){
		try{
			this.setMsgQuestionResponse(loginEJB.questionResponseReminder(userId));
			this.setMsgCambioContrase�a(loginEJB.passwordChangeReminder(userId));
			boolean changePasswordReminder  = loginEJB.msgChangeReminder(userId);
			String messageAccount = sp.getParameterValueById(67L);

			if(loginEJB.preenrollUser(currentUser.getUserId())){
				System.out.println("17");
				this.securityQuestionsEJB.rememberQuestionsAfter(currentUser.getUserId(), ip);
				this.setShowCreateQuestions(false);
				this.setShowMesssage1(true);
				this.setLogged(true);
				if(pseId!=null){
					return "successPSE";
				}else{
					return "successPU";
				}
			}else if((msgCambioContrase�a.length()>0 && changePasswordReminder==true) || msgQuestionResponse.length()>0){//muestra el mensaje de cambio de contrase�a
				this.securityQuestionsEJB.rememberQuestionsAfter(currentUser.getUserId(), ip);
				System.out.println("8");
				this.setShowCreateQuestions(false);
				this.setLogged(true);
				if(messageAccount.equals("SI") && this.getAccountLoginList().size()>0){
					this.setShowMesssage(true);
					if(pseId!=null){
						return "successPSE";
					}else{
						return "successCP";
					}
				}else if(pseId!=null){
					return "successPSE";
				}else{
					return "successCP";
				}
			}else {
				this.securityQuestionsEJB.rememberQuestionsAfter(currentUser.getUserId(), ip);
				System.out.println("18");
				this.setShowCreateQuestions(false);
				this.setLogged(true);
				if(messageAccount.equals("SI") && this.getAccountLoginList().size()>0){
					this.setShowMesssage(true);
					if(pseId!=null){
						return "successPSE";
					}else{
						return "success";
					}
				}else if(pseId!=null){
					return "successPSE";
				}else{
					return "success";
				}
			}
		}catch (NullPointerException n){
			System.out.println("[ Error en LoginMBean.rememberQuestionsAfter ]");
			n.printStackTrace();
			setLogged(false);
			setExist(true);	
			return "successOut";
		}
	}
	
	/** @author psanchez
	 * M�todo encargado de validar la vigencia del c�digo OTP Usuarios Nuevos*/
	public String validarOtp(){
		String msj ="";
		count++;
		try{ 
			if(systemUser.getUserState().getUserStateId()==1){
				this.setShowMsjOtpValidity(false);
				this.setModalMessage("El Usuario se encuentra inactivo, Comun�quese con Servicio al Cliente.");
				this.setShowOtp(false);
				this.setExist(false);
				this.setShowMsjOtp(true);
			}else if(otp.equals(null) || otp.equals("")){
				loginEJB.userOtpModal(systemUser.getUserId(),count,ip);
				this.setShowMsjOtpValidity(false);
				this.setShowMsjOtp(true);
				if(loginEJB.userOtpLockedModal(systemUser.getUserId(),count,ip)){
					this.setModalMessage("Ha superado el n�mero de intentos permitidos para ingresar su c�digo," +
		             " nuestros asesores se comunicar�n con usted.");	
				}else{
					this.setModalMessage("El c�digo OTP es obligatorio.");
				}
			}else if(Long.valueOf(otp)<100001 || Long.valueOf(otp)>999999999999999L){
				loginEJB.userOtpModal(systemUser.getUserId(),count,ip);
				this.setShowMsjOtpValidity(false);
				this.setShowMsjOtp(true);
				if(loginEJB.userOtpLockedModal(systemUser.getUserId(),count,ip)){
					this.setModalMessage("Ha superado el n�mero de intentos permitidos para ingresar su c�digo," +
		             " nuestros asesores se comunicar�n con usted.");	
				}else{
					this.setModalMessage("Se�or usuario, el c�digo ingresado no corresponde " +
		             "al generado por el sistema, intente nuevamente.");
				}
			}else if(loginEJB.validateOtpModal(otp, systemUser.getUserId(),ip).equals("1")){
				loginEJB.inactiveOtp(otp, systemUser.getUserId());
				this.setShowMsjOtpValidity(false);
				this.setShowMsjOtp(true);
				if(loginEJB.userOtpLockedModal(systemUser.getUserId(),count,ip)){
					this.setModalMessage("Ha superado el n�mero de intentos permitidos para ingresar su c�digo," +
		             " nuestros asesores se comunicar�n con usted.");	
				}else{
					this.setModalMessage("Se�or usuario su c�digo ya expir�, para lo cual se envi� " +
							"uno nuevo a su cuenta de correo electr�nico registrada en el sistema, " +
							"por favor valide su cuenta e ingr�selo para poder continuar.");
				}
			}else if(loginEJB.validateOtpModal(otp, systemUser.getUserId(),ip).equals("3") ){
				loginEJB.inactiveOtp(otp, systemUser.getUserId());
				loginEJB.userOtpModal(systemUser.getUserId(),count,ip);
				this.setShowMsjOtpValidity(false);
				this.setShowMsjOtp(true);
				if(loginEJB.userOtpLockedModal(systemUser.getUserId(),count,ip)){
					this.setModalMessage("Ha superado el n�mero de intentos permitidos para ingresar su c�digo," +
		             " nuestros asesores se comunicar�n con usted.");	
				}else{
					this.setModalMessage("Se�or usuario, el c�digo ingresado no corresponde " +
							             "al generado por el sistema, intente nuevamente.");
				}
			}else if(loginEJB.validateOtpModal(otp, systemUser.getUserId(),ip).equals("2") && 
		    	!securityQuestionsEJB.validateQuestionsResponse(systemUser.getUserId())){
				loginEJB.inactiveOtp(otp, systemUser.getUserId());
				System.out.println("14");
				//Al validar el codigo OTP redirecciona a la configuracion de preguntas de seguridad
				this.setShowOtp(false);
				this.setUserId(currentUser.getUserId());
				this.setLogged(true);
				this.setExist(false);
				loginEJB.updateLastLogin(userId);
				count=0;
				if(pseId!=null){
					return "successPSE";
				}else{
					return "securityQuestionsNew";
				}
		   }else if(loginEJB.validateOtpModal(otp, systemUser.getUserId(),ip).equals("2") && 
		    	securityQuestionsEJB.validateQuestionsResponse(systemUser.getUserId())){
				loginEJB.inactiveOtp(otp, systemUser.getUserId());
				System.out.println("16");
				//Al validar el codigo OTP redirecciona al cambio de contrase�a
				this.setShowOtp(false);
				this.setUserId(currentUser.getUserId());
				this.setLogged(true);
				this.setExist(false);
				loginEJB.updateLastLogin(userId);
		    	count=0;
				if(pseId!=null){
					return "successPSE";
				}else{
					return "passwordReset";
				}
		    }
		}catch(NumberFormatException ex){
			loginEJB.inactiveOtp(otp, systemUser.getUserId());
			loginEJB.userOtpModal(systemUser.getUserId(),count,ip);
			this.setShowMsjOtpValidity(false);
			this.setModalMessage("Se�or usuario, el c�digo ingresado no corresponde " +
						         "al generado por el sistema, intente nuevamente.");
			setShowMsjOtp(true);
    	}catch(NullPointerException ex){
			loginEJB.inactiveOtp(otp, systemUser.getUserId());
    		System.out.println("Su Sesi�n ha Terminado....");
			this.setModalMessage("Su Sesi�n ha Terminado.");
			this.setLogged(true);
			this.setExist(false);
			setShowMsjOtp(false);
			setShowMsjOtpValidity(false);
    	}catch (Exception e) {
			loginEJB.inactiveOtp(otp, systemUser.getUserId());
    		System.out.println("Su Sesi�n ha Terminado,,,");
			this.setModalMessage("Su Sesi�n ha Terminado.");
			this.setLogged(true);
			this.setExist(false);
			setShowMsjOtp(false);
			setShowMsjOtpValidity(false);
		}
    	if(pseId!=null){
    		if(msj==null||msj.equals("")){
    			return null;
    		}else{
    			return "successPSE";
    		}
		}else{
			return msj;
		}
	}

		
	/**@author psanchez
	 * M�todo encargado de redireccionar al m�dulo de configuraci�n de preguntas Usuario Existentes*/
	public String rediConfig(){
		System.out.println("15");
		try{
			this.setUserId(currentUser.getUserId());
			this.setLogged(true);
			this.setShowCreateQuestions(false);
			this.securityQuestionsEJB.InicioConfPregVP(currentUser.getUserId(), ip);
			this.setShowMsjUsExis(false);
			if(pseId!=null){
				return "successPSE";
			}else{
				return "securityQuestionsN";
			}
		}catch(NullPointerException ex){
    		System.out.println("19");
			setLogged(false);			
			return "successOut";
    	}
	} 
	
	/**
	 * Invalidates the current user session.
	 */
	public String logout() {	
		System.out.println("22");
		FacesContext context = FacesContext.getCurrentInstance();

		// @author TPS r.bautista
		String value = (String)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(TPS_Constants.MENSAJE_TXT_LOGIN);
		// fin 

		HttpSession session = (HttpSession) context.getExternalContext()
				.getSession(false);
		if (session != null) {
		  if (userId != null){	
			  this.loginEJB.updateLastLogin(userId);


		  }


		  session.invalidate();
		}

		// @author TPS r.bautista
		if ( value != null ){
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(TPS_Constants.MENSAJE_TXT_LOGIN, value);

		}
    	// end

		if(pseId!=null){
			return "outPSE";
		}else{
			return "successOut";
		}

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
		System.out.println("Salir por carga de pagina wellcome");
		FacesContext context = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) context.getExternalContext().getSession(false);

		if (session != null) {
		  if(userId != null){	
			this.loginEJB.updateLastLogin(userId);
		  }	
			session.invalidate();
		}
		if(pseId!=null){
			return "outPSE";
		}else{
			return "successOut";
		}
	}
	
	/**
	 * Control modal
	 */
	public String hideModal(){	
		System.out.println("21");
	    HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
        .getExternalContext().getSession(false);
	    
	    if(session!=null){
	    	session.invalidate();
	    	System.out.print("la session estaba activa");
	    }
           
		exist = false;
		modalMessage = "";
		logged = false;
		this.setShowMsjOtpValidity(false);
		this.setShowOtp(false);
		this.setShowMsjOtp(false);
		this.setOtp(null);
		this.setShowMsjUsExis(false);
		this.setShowAcptNoSup(false);
		this.setShowAcptYesSup(false);
		this.setShowCreateQuestions(false);
		return null;
	}
	
	public String redirecRestartUser(){
		exist = false;
	
		return "usrValidation";
	}
	
	public void hideModalOtpValidity(){
		System.out.println("hideModalValidityOtp--");
		exist = false;
		modalMessage = "";
		logged = false;
		this.setShowOtp(false);
		this.setShowMsjOtpValidity(false);
		this.setOtp(null);
		this.setShowMsjUsExis(false);
		this.setShowAcptNoSup(false);
		this.setShowAcptYesSup(false);
		this.setShowCreateQuestions(false);		
	}
	
	public void hideModalOtp(){
		System.out.println("hideModalOtp--");
		exist = false;
		modalMessage = "";
		logged = false;
		this.setShowMsjOtpValidity(false);
		this.setShowMsjOtp(false);
		this.setOtp(null);
		this.setShowMsjUsExis(false);
		this.setShowAcptNoSup(false);
		this.setShowAcptYesSup(false);
		this.setShowCreateQuestions(false);		
	}
	
	public String hideModalOtpPSE(){
		exist = false;
		modalMessage = "";
		logged = false;
		this.setShowOtp(false);
		this.setOtp(null);
		this.setShowMsjUsExis(false);
		this.setShowAcptNoSup(false);
		this.setShowAcptYesSup(false);
		this.setShowCreateQuestions(false);
		if(pseId!=null){
			try{
				FacesContext context = FacesContext.getCurrentInstance();
				HttpSession session = (HttpSession) context.getExternalContext()
						.getSession(false);
				System.out.println("LoginMBean.session: "+session);
				System.out.println("LoginMBean.pseId: "+pseId);
				if (session != null) {
					System.out.println("LoginMBean.invalidate");
					session.invalidate();
				}
				FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.put("pse", pseWS.encodePSETransaction(String.valueOf(pseId)));
			}catch(IllegalStateException i){
				System.out.println("[ Error en LoginMBean.FacesContext ]");
				i.printStackTrace();
			}
		}
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


		// @author TPS r.bautista

		String value = sp.getParameterValueById(66L);
		if (value != null){

			this.txtLogin = value;
		} else {


			this.txtLogin = LoginMBean.INGRESAR;;






		}

		return this.txtLogin;
	}

	/**
	 * Retorna el titulo de encabezado cuando se redirigi� al login desde recarga autom�tica, de lo contrario retorna INGRESAR
	 * @return String con el valor adecuado
	 * @author TPS r.bautista
	 */
	public String getRedirectTxtLogin(){
		String value = (String)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(TPS_Constants.MENSAJE_TXT_LOGIN);
		if (value != null) {

			this.txtLogin = value.replace("<p>", "");
			this.setShowRegistred(false);
		} else {
			value = LoginMBean.INGRESAR;;
		}
		
		return value;
	}

	public void setTxtLogin(String txtLogin) {
		this.txtLogin = txtLogin;
	}

	public String getPseIdStr() {
		return pseIdStr;
	}

	public void setPseIdStr(String pseIdStr) {
		this.pseIdStr = pseIdStr;
	}

	public Long getPseId() {
		return pseId;
	}

	public void setPseId(Long pseId) {
		this.pseId = pseId;
	}

	public void setShowMsjOtpValidity(boolean showMsjOtpValidity) {
		this.showMsjOtpValidity = showMsjOtpValidity;
	}

	public boolean isShowMsjOtpValidity() {
		return showMsjOtpValidity;
	}

	public void setShowMsjOtp(boolean showMsjOtp) {
		this.showMsjOtp = showMsjOtp;
	}

	public boolean isShowMsjOtp() {
		return showMsjOtp;
	}

	public void setAccountList(List<AllInfoAccount> accountList) {
		this.accountList = accountList;
	}

	public List<AllInfoAccount> getAccountLoginList() {
		if(accountList == null) {
			accountList = new ArrayList<AllInfoAccount>();
		}else{
			accountList.clear();
		}
		accountList = loginEJB.getAccountLoginList(systemUser.getUserId());
		return accountList;
	}

	public boolean isShowRegistred() {
		return showRegistred;
	}

	public void setShowRegistred(boolean showRegistred) {
		this.showRegistred = showRegistred;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}