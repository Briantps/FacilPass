package process.recharge;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import jpa.TbAvalTransaction;
import jpa.TbSystemUser;

import com.facilpass.util.MessageParser;
import com.facilpass.util.classes.SimpleTagMessageParser;

import security.UserLogged;
import sessionVar.SessionUtil;
import util.ws.PseWS;
import constant.EEmailParam;
import constant.EmailProcess;
import ejb.Login;
import ejb.User;
import ejb.email.Outbox;

/**
 * 
 * @author jromero
 *
 */
public class VerificatorBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String idPseTransaction;
	private String idAvalTransaction;
	private String message;
	private UserLogged usrs;
	private Long userId=-1L;
	private Long userMId=-1L;
	private Long pseId=-1L;
	private Long avalId=-1L;
	private String name="";
	private String action="";
	private String decodeId="";
	private TbAvalTransaction trx;
	
	@EJB(mappedName ="util/ws/PseWS")
	private PseWS pseWS;

	@EJB(mappedName = "ejb/User")
	private User user;
	
	@EJB(mappedName ="ejb/email/Outbox")
	private Outbox outbox;
	
	@EJB(mappedName = "ejb/Login")
	private Login loginEJB;
	
	public VerificatorBean() {
	}

	@PostConstruct
	public void init(){
		
		/*
		 * Se verifica inicialmente a que tipo pertenece la URL en el momento existe PSE => v & AVAL => a
		 * Se valida mediante un switch a que caso ingresar siendo v el Caso => 0 y a el caso =>1
		 * Si la URL No existe para AVAL se envia la notificacion NTF 07
		 * Si la URL Fue utilizada para AVAL se envia NTF 09
		 */
		boolean notification=false;
		EmailProcess code=null;
		ArrayList<String> parameters=new ArrayList<String>();
		Integer key=-1;
		String url=null;
		action="failConn";
		
		HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		//parameters.add("#ACCIP="+SessionUtil.ip());
		
		idPseTransaction=(String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("v");
		idAvalTransaction=(String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("a");
		
		
		
		
		if(idPseTransaction!=null&&!idPseTransaction.equals("")){
			key=0;
			//parameters.add("#ACCURL="+origRequest.getRequestURL().substring(0)+"?v="+idPseTransaction);
			url=origRequest.getRequestURL().substring(0)+"?v="+idPseTransaction;
			
		}
		if(idAvalTransaction!=null&&!idAvalTransaction.equals("")){
			key=1;
			//parameters.add("#ACCURL="+origRequest.getRequestURL().substring(0)+"?a="+idAvalTransaction);
			url=origRequest.getRequestURL().substring(0)+"?a="+idAvalTransaction;
		}
		
		
		System.out.println("idPseTransaction: "+idPseTransaction);
		System.out.println("idAvalTransaction: "+idAvalTransaction);
		
		usrs = (UserLogged) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
		
		switch(key){
		
		case 0:
			//La URL enviada contiene el caracter V perteneciente a PSE
		if(idPseTransaction!=null&&!idPseTransaction.equals("")){
			decodeId=pseWS.decodePSETransaction(idPseTransaction);
			if(decodeId!=null){
				try{
					System.out.println("tran: "+decodeId);
					String[] a=decodeId.split(Pattern.quote("+"));
					pseId=Long.parseLong(a[0]);
					userId=Long.parseLong(a[1]);
				}catch (Exception e) {
					pseId=-1L;
					userId=-1L;
					e.printStackTrace();
					System.out.println(" [ VerificatorBean.split ] ");
				}
				if(pseId!=-1L&&userId!=-1L){
					userMId=user.getSystemMasterById(userId);
					System.out.println("userMId: "+userMId);
					boolean existUrl=pseWS.existURL(pseId);
					System.out.println("existUrl: "+existUrl);
					if(existUrl==true){
						boolean usedUrl=pseWS.isUsedURL(pseId);
						System.out.println("usedUrl: "+usedUrl);
						if(usedUrl==false){
							boolean onDayURL=pseWS.isOnDayURLbyUmbral(pseId);
							System.out.println("onDayURL: "+onDayURL);
							if(onDayURL==true){
								boolean onTimeUrl=pseWS.isOnTimeURLbyUmbral(pseId);
								System.out.println("onTimeUrl: "+onTimeUrl);
								if(onTimeUrl==true){
									if(usrs!=null){
										System.out.println("usrs1: "+usrs.getUserId());
										System.out.println("usrs2: "+usrs.getUsername());
										if(userMId.equals(user.getSystemMasterById(usrs.getUserId()))){
											try{	
												FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
												.put("pse", pseWS.encodePSETransaction(String.valueOf(pseId)));
											}catch(IllegalStateException i){
												System.out.println("[ Error en VerificatorBean.FacesContext2 ]");
												i.printStackTrace();
											}
											action="okConn";
										}
									}
								}else{
									try{
										FacesContext context = FacesContext.getCurrentInstance();
										HttpSession session = (HttpSession) context.getExternalContext()
												.getSession(false);
										System.out.println("session: "+session);
										System.out.println("userId: "+userId);
										if (session != null) {
										  if(userId != null){	
											this.loginEJB.updateLastLogin(userId);
										  }	
											session.invalidate();
										}
										FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
										.put("pse", pseWS.encodePSETransaction(String.valueOf(pseId)));
									}catch(IllegalStateException i){
										System.out.println("[ Error en VerificatorBean.FacesContext3 ]");
										i.printStackTrace();
									}
									action="pendingConn";
								}
							}
						}
					}else{
						notification=true;
						System.out.print("ID DE CONSTANTE EMAILPROCCES"+EmailProcess.PSE_URL_NO_AUTHORIZED.getId());
						code= EmailProcess.PSE_URL_NO_AUTHORIZED;
					}
				}else{
					System.out.print("ID DE CONSTANTE EMAILPROCCES"+EmailProcess.PSE_URL_NO_AUTHORIZED.getId());
					code= EmailProcess.PSE_URL_NO_AUTHORIZED;
					notification=true;
				}
			}else{
				notification=true;
				System.out.print("ID DE CONSTANTE EMAILPROCCES"+EmailProcess.PSE_URL_NO_AUTHORIZED.getId());
				code= EmailProcess.PSE_URL_NO_AUTHORIZED;
			}
		}else{
			notification=true;
			System.out.print("ID DE CONSTANTE EMAILPROCCES"+EmailProcess.PSE_URL_NO_AUTHORIZED.getId());
			code= EmailProcess.PSE_URL_NO_AUTHORIZED;
		}
		break;

		case 1:
			//la URL enviada Contenia el caracter A perteneciente a AVAL
		if(idAvalTransaction!=null&&!idAvalTransaction.equals("")){
			decodeId=pseWS.decodePSETransaction(idAvalTransaction);
			if(decodeId!=null){
				try{
					System.out.println("tran: "+decodeId);
					String[] a=decodeId.split(Pattern.quote("+"));
					avalId=Long.parseLong(a[0]);
					userId=Long.parseLong(a[1]);
				}catch (Exception e) {
					avalId=-1L;
					userId=-1L;
					e.printStackTrace();
					System.out.println(" [ VerificatorBean.split ] ");
				}
				if(avalId!=-1L&&userId!=-1L){
					userMId=user.getSystemMasterById(userId);
					System.out.println("userMId: "+userMId);
					boolean existUrl=pseWS.existURLAval(avalId);
					System.out.println("existUrl: "+existUrl);
					if(existUrl==true){
						boolean usedUrl=pseWS.isUsedURLAval(avalId);
						System.out.println("usedUrl: "+usedUrl);
						if(usedUrl==false){
							boolean onDayURL=pseWS.isOnDayURLbyUmbralAval(avalId);
							System.out.println("onDayURL: "+onDayURL);
							if(onDayURL==true){
								boolean onTimeUrl=pseWS.isOnTimeURLbyUmbralAval(avalId);
								System.out.println("onTimeUrl: "+onTimeUrl);
								if(onTimeUrl==true){
									if(usrs!=null){
										System.out.println("usrs1: "+usrs.getUserId());
										System.out.println("usrs2: "+usrs.getUsername());
										if(userMId.equals(user.getSystemMasterById(usrs.getUserId()))){
											try{	
												FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
												.put("aval", pseWS.encodePSETransaction(String.valueOf(avalId)));
											}catch(IllegalStateException i){
												System.out.println("[ Error en VerificatorBean.FacesContext2 ]");
												i.printStackTrace();
											}
											action="okConn";
										}
									}
								}else{
									
									try{
																				
										FacesContext context = FacesContext.getCurrentInstance();
										HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
										System.out.println("session: "+session);
										System.out.println("userId: "+userId);
										if (session != null) {
										  if(userId != null){	
											this.loginEJB.updateLastLogin(userId);
										  }	
											session.invalidate();
										}
										FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
										.put("aval", pseWS.encodePSETransaction(String.valueOf(avalId)));
										
										
										
									}catch(IllegalStateException i){
										System.out.println("[ Error en VerificatorBean.FacesContext3 ]");
										i.printStackTrace();
									}
									action="pendingConn";
								}
							}
						}else{
							//URL USADA
							//REDIRECIONAR AL LOGIN Y ENVIAR NOTIFICACION NTF-09
							System.out.print("ID DE CONSTANTE EMAILPROCCES"+EmailProcess.AVAL_URL_USED.getId());
							code=EmailProcess.AVAL_URL_USED;
							trx=pseWS.getTransactionAval(avalId);
							
							
							//METODO NUEVO PARA ENVIO DE NOTIFICACIONES 
							Map<String,Object> tags = new HashMap<String,Object>();
							Date date = Calendar.getInstance().getTime();
							tags.put(EEmailParam.NOMB_CLI.getTag(), userId); //ok-4
							tags.put(EEmailParam.CUENTA_FACIL_PASS.getTag(),trx.getUmbralId().getTbAccount().getAccountId() ); //ok-1 
							tags.put(EEmailParam.TIPO_ID.getTag(), trx.getUserId().getTbCodeType().getCodeTypeId());//OK-17
							tags.put(EEmailParam.NUM_ID.getTag(), userId);//OK-5
							tags.put(EEmailParam.NOMB_CONVENIO.getTag(), trx.getAgreementId().getAgreementId()); //Ok-66
							tags.put(EEmailParam.ERRAVALC.getTag(), "Informa a los interesados que la URL " +
									"ya fue utilizada en el sistema");//OK-46
							tags.put(EEmailParam.VAL_RECARG.getTag(), trx.getValueTransaction());//ok-49
							tags.put(EEmailParam.FECHA_ACCION.getTag(), date);//ok-18
							tags.put(EEmailParam.HORA_ACCION.getTag(), date);//ok-19
							tags.put(EEmailParam.EMAIL_USU.getTag(), userId);//ok-65
							tags.put(EEmailParam.URL_ACCION.getTag(), url);//ok-45
							tags.put(EEmailParam.IP_ACCION.getTag(), SessionUtil.ip());//ok-44
							MessageParser mp = new SimpleTagMessageParser(tags);
							outbox.insertMessageOutbox2(userId, code, true, mp);
							
						}
					}else{
						//URL NO EXISTE EN EL SISTEMA
						//ENVIAR NTF-07 Y REDIRECCIONAR A LOGIN
						System.out.print("ID DE CONSTANTE EMAILPROCCES"+EmailProcess.AVAL_URL_NOT_VALID.getId());
						code=EmailProcess.AVAL_URL_NOT_VALID;
						notification=true;
					}
				}else{
					//NTF-07
					notification=true;
					System.out.print("ID DE CONSTANTE EMAILPROCCES"+EmailProcess.AVAL_URL_NOT_VALID.getId());
					code=EmailProcess.AVAL_URL_NOT_VALID;
				}
			}else{
				//NTF-07
				notification=true;
				System.out.print("ID DE CONSTANTE EMAILPROCCES"+EmailProcess.AVAL_URL_NOT_VALID.getId());
				code=EmailProcess.AVAL_URL_NOT_VALID;
			}
		}
		break;
		
		default:
			System.out.println("Se presento un error en el switchs");
		break;
	}
		
		
		
		
		
		
		if(idAvalTransaction ==	null && idPseTransaction ==null){
			//Si no ingresa ninguna URL pero consume verificatorBean
			//parameters.add("#ACCURL="+origRequest.getRequestURL().substring(0));
			url=origRequest.getRequestURL().substring(0);
			System.out.print("ID DE CONSTANTE EMAILPROCCES"+EmailProcess.PSE_URL_NO_AUTHORIZED.getId());
			code= EmailProcess.PSE_URL_NO_AUTHORIZED;
		}
		
		System.out.println("notification: "+notification);
		
		if(notification==true){
			System.out.println("verificartor.parameters: "+parameters.toString());
			//BM
			//METODO NUEVO PARA ENVIO DE NOTIFICACIONES NTF-07
			Map<String,Object> tags = new HashMap<String,Object>();
			Date date = Calendar.getInstance().getTime();
			tags.put(EEmailParam.IP_ACCION.getTag(), SessionUtil.ip());//OK-44
			tags.put(EEmailParam.URL_ACCION.getTag(), url); //OK-45
			tags.put(EEmailParam.FECHA_ACCION.getTag(), date);//OK-18
			tags.put(EEmailParam.HORA_ACCION.getTag(), date);//OK-19
			MessageParser mp = new SimpleTagMessageParser(tags);
			outbox.insertMessageOutbox2(userId, code, true, mp);
			
		}
		FacesContext.getCurrentInstance().getApplication().getNavigationHandler().
		handleNavigation(FacesContext.getCurrentInstance(), null, 
				action);
	}

	public String getIdPseTransaction() {
		return idPseTransaction;
	}

	public void setIdPseTransaction(String idPseTransaction) {
		this.idPseTransaction = idPseTransaction;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public UserLogged getUsrs() {
		return usrs;
	}

	public void setUsrs(UserLogged usrs) {
		this.usrs = usrs;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Long getPseId() {
		return pseId;
	}

	public void setPseId(Long pseId) {
		this.pseId = pseId;
	}

	public String getDecodeId() {
		return decodeId;
	}

	public void setDecodeId(String decodeId) {
		this.decodeId = decodeId;
	}
	
}