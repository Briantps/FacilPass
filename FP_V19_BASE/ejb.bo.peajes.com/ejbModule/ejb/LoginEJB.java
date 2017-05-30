package ejb;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import constant.EmailProcess;
import constant.LogAction;
import constant.LogReference;
import constant.ProcessTrackDetailType;
import constant.ProcessTrackType;
import constant.UserState;

import crud.ObjectEM;
import ejb.email.Outbox;

import util.AllInfoAccount;
import util.Encryptor;
import util.LoginUser;

import jpa.ReUserOtp;
import jpa.TbOtp;
import jpa.TbProcessTrack;
import jpa.TbStateOtp;
import jpa.TbSystemUser;
import jpa.TbUserStateType;

/**
 * Session Bean implementation class LoginEJB
 * 
 * @author Cesar Hoyos
 */

@Stateless(mappedName = "ejb/Login")
public class LoginEJB implements Login {

	@PersistenceContext(unitName = "bo")
	private EntityManager em;
	
	@EJB(mappedName="ejb/Log")
	private Log log;
	
	@EJB(mappedName="ejb/User")
	private User userEJB;
	
	@EJB(mappedName="ejb/Process")
	private Process process;
	
	@EJB(mappedName="ejb/SystemParameters")
	private SystemParameters SystemParametersEJB;
	
	@EJB(mappedName="ejb/SecurityQuestions")
	private SecurityQuestions securityQuestionsEJB;
	
	@EJB(mappedName ="ejb/email/Outbox")
	private Outbox outbox;

	private Query validateQuery;
	private Query infoUserQuery;
	
	private int count;
	
	long day = 86400000;

	/**
	 * Default constructor.
	 */
	public LoginEJB() {

	}

	@PostConstruct
	public void init() {
		validateQuery = em
				.createQuery("FROM TbSystemUser user WHERE user.userEmail = ?1");
		infoUserQuery = em
				.createNativeQuery("SELECT role_Id FROM re_user_role WHERE user_Id = ?1");
		
	    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Login#validateUser(java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public LoginUser validateUser(String mail, String password, String ip) {
		LoginUser login = new LoginUser();
		count++;
		try {
			mail = mail.toLowerCase();
			TbSystemUser user = null;
			user = (TbSystemUser) validateQuery.setParameter(1, mail).getSingleResult();
			if(lockAccount(user.getUserId())==true){
				log.insertLog("Usuario Inactivo intenta ingresar al Sistema.",LogReference.LOGIN, LogAction.ACCESSDENIED, ip , user.getUserId());
				login.setMessage("Su contraseña se encuentra inactiva temporalmente, Intente más tarde.");
				return login;
		    }else if (Encryptor.verifyPwd(user.getUserPassword(), password) && expirationOtpLogin(password, user.getUserId(), ip)){
				login.setLoginUser(null);
				inactiveOtp(password, user.getUserId());
				login.setMessage("Señor usuario, el código ingresado caduco, por favor valide " +
							     "el nuevo código enviado a su cuenta de correo electrónico.");				
				return login;
			}else if (!Encryptor.verifyPwd(user.getUserPassword(), password) && 
					   Encryptor.verifyPwd(user.getUserPassword(), userOtpLogin(user.getUserId()).toString())){
				// Password incorrect.
					log.insertLog("Clave Inválida. E-Mail: " + mail +". Password: " + Encryptor.getEncrypt(password),
							LogReference.LOGIN, LogAction.ACCESSDENIED, ip , user.getUserId());
					login.setLoginUser(null);
					if(resetCount(user.getUserId())==true){
						this.validateUser(mail, password, ip);
						return login;
					}else if(userPassNew(user.getUserId(),count,ip)==true){
							login.setMessage("Señor usuario, sobrepasó el número de intentos permitidos para restablecer contraseña, " +
									"nuestros asesores se comunicarán con usted para atender su solicitud.");
							return login;
					}else{
						this.validateOtpLogin(password, user.getUserId(),ip);
						login.setMessage("El código de seguridad ingresado no es correcto.");						
						return login;
					}						
		    }else if (!Encryptor.verifyPwd(user.getUserPassword(), password) && securityQuestionsEJB.validateQuestionsResponse(user.getUserId()) &&
		    		  Encryptor.verifyPwd(user.getUserPassword(), userOtpLogin(user.getUserId()).toString())){
				// Password incorrect.
					log.insertLog("Clave Inválida. E-Mail: " + mail + ". Password: " + Encryptor.getEncrypt(password),
							LogReference.LOGIN, LogAction.ACCESSDENIED, ip , user.getUserId());
					login.setLoginUser(null);
					login.setMessage("1");
					if(resetCount(user.getUserId())==true){
						this.validateUser(mail, password, ip);
						return login;
					}else if(userPassNew(user.getUserId(),count,ip)==true){
						login.setMessage("Señor usuario, sobrepasó el número de intentos permitidos para el ingreso de la " +
								         "contraseña correcta, nuestros asesores se comunicarán con usted para atender su solicitud.");
						return login;
					}
		    }else if (!Encryptor.verifyPwd(user.getUserPassword(), password)){
				// Password incorrect.
				log.insertLog("Clave Inválida. E-Mail: " + mail + ". Password: " + Encryptor.getEncrypt(password),
						LogReference.LOGIN, LogAction.ACCESSDENIED, ip , user.getUserId());
				login.setLoginUser(null);
				login.setMessage("1");
				if(resetCount(user.getUserId())==true){
					this.validateUser(mail, password, ip);
					return login;
				}else if(userPassIntent(user.getUserId(),count,ip)==true){
					login.setMessage("Señor usuario, sobrepasó el número de intentos permitidos para restablecer contraseña, " +
							"nuestros asesores se comunicarán con usted para atender su solicitud.");
					return login;
				}
	         } else if (user.getUserState().getUserStateId()==1){
				//Inactive User
				log.insertLog("Usuario Inactivo intenta ingresar al Sistema.",LogReference.LOGIN, LogAction.ACCESSDENIED, ip , user.getUserId());
				login.setMessage("El Usuario se encuentra inactivo, Comuníquese con Servicio al Cliente.");						
				return login;		
		     } else if(lockAccount(user.getUserId())==true){
				login.setMessage("Su contraseña se encuentra inactiva temporalmente, Intente más tarde.");
				return login;
		     } else if(maximumPasswordAge(user.getUserId())==true){
				login.setMessage("Su contraseña ha caducado, Comuníquese con Servicio al Cliente.");
				return login;
		     } else if(userPassExpiration(user.getUserId())==true){
				login.setMessage("Su contraseña expiró. Comuníquese con Servicio al Cliente.");
				return login;
		     } else {
				if(Encryptor.verifyPwd(user.getUserPassword(), userOtpLogin(user.getUserId()).toString())==true){
					inactiveOtp(password, user.getUserId());					
				}
		    	log.insertLog("Usuario ha ingresado al sistema.",
					LogReference.LOGIN, LogAction.ACCESS, ip , user.getUserId());
				login.setLoginUser(user);
				login.setMessage("OK");
				this.updateAccessLogin(user.getUserId());
				count=0;
		   }				  
		} catch (NoResultException nre) {
			//Unknown user is trying to enter the system, mail incorrect.
			log.insertLog("Usuario Incorrecto/Inválido/No Existe en BD. E-Mail: " + mail,
					LogReference.LOGIN, LogAction.ACCESSDENIED, ip , null);
			login.setLoginUser(null);
			login.setMessage("1");
		}catch (Exception e) {
			System.out.println(" [ Error en LoginEJB.validateUSer ] ");
			login.setMessage("El servicio no se encuentra disponible. Comuníquese con Servicio al Cliente.");
			e.printStackTrace();
		}
		return login;
	}

	
	/**
	 * Método creado para validar la vigencia máxima de la contraseña.
	 * @return return true si la vigencia máxima esta activa o false en otro caso.
	 * @author psanchez
	 */
	private boolean maximumPasswordAge(Long userId) {	
		boolean response=false;	
		try {
			  //dia máximo cambio contraseña
			  String daysParameter = SystemParametersEJB.getParameterValueById(21L);
			  long millisecondMaximum=Long.parseLong(daysParameter)*day;

			  //consulta último cambio contraseña de usuario
			  Query query= em.createNativeQuery("SELECT tsu.user_pwd_date FROM tb_system_user tsu WHERE tsu.user_id=?1 ");
			  query.setParameter(1,userId);
			  Timestamp lastDate=(Timestamp) query.getSingleResult();

		      if(this.roleAdmin(userId)==true && new Date(System.currentTimeMillis()).getTime() - lastDate.getTime() >= millisecondMaximum){
			      response=true;
		      }else{
				  response=false;
			  }
		 }catch(NoResultException ex){
				System.out.println(" [ Error en LoginEJB.maximumPasswordAge ] ");
				response=false;
		 } 
		return response;
     }
	
	/**
	 * Método creado para validar la vigencia mínima de la contraseña.
	 * @return return true si la vigencia minima esta activa o false en otro caso.
	 * @author psanchez
	 */
    public String minimumPasswordAge(long userId){
    	try {
			//Establece el número mínimo de días parametrizados que deben transcurrir antes de que se pueda cambiar una contraseña.
    		String dayParameter = SystemParametersEJB.getParameterValueById(20L);
		    long daysParameter = Long.parseLong(dayParameter)*day;

			//consulta la último fecha del cambio de contraseña del usuario.
			Query query= em.createNativeQuery("SELECT tsu.user_pwd_date FROM tb_system_user tsu WHERE tsu.user_id=?1 ");
			query.setParameter(1,userId);
		    Timestamp lastDate=(Timestamp) query.getSingleResult();
			long missingDate = new Date(System.currentTimeMillis()).getTime() - lastDate.getTime();
			
			if(this.roleAdmin(userId)==true && daysParameter>=missingDate){
				double days = Math.floor(daysParameter-missingDate/day);
				if (days>=1){
					return ("En este momento su clave se encuentra activa y tiene una vigencia mínima para cambiarla, lo invitamos a ingresar con la clave que registró."); 
				}
			}
    	} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return "";
    }
	
	

	/**
	 * Método creado para validar la vigencia de la contraseña de usuario (dia/mes/año), e impedir ingresar en una 
	 * fecha posterior a la definida por el parámetro.
	 * @return return true si la fecha actual es mayor a la de expiración o false en otro caso.
	 * @author psanchez
	 */
	 public boolean userPassExpiration(Long userId){
		boolean response=false;
		try{	
    		 String userExpirationDate = SystemParametersEJB.getParameterValueById(26L);
    	     SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");

             if(this.roleAdmin(userId)==true && new Date().compareTo(sdf.parse(userExpirationDate))>0 || new Date().compareTo(sdf.parse(userExpirationDate))==0 ){
        		response=true;
           	 }else if(new Date().compareTo(sdf.parse(userExpirationDate))<0){
           		response=false;
             }
        }catch (ParseException e){
           System.out.println("[ Error en LoginEJB.userPassExpiration ]");
           response=false;
        }
		return response;
	}
	 

	/**
	 * Método creado para validar el no vencimiento de la cuenta del rol administrador.
	 * @return return true si usuario en sesión es diferente del administrador.
	 * @author psanchez
	 */
	 @Override
	 public boolean roleAdmin(long userId){	
		boolean response=false;	
		try{	
             Query query= em.createNativeQuery("SELECT rer.role_Id FROM re_user_role rer "+
            		 						   "INNER JOIN tb_system_user tu ON tu.user_id=rer.user_id " +
            		 						   "WHERE tu.user_id=?1 AND rer.role_id=1 "); 
             query.setParameter(1,userId);
             long roleId =Long.parseLong(query.getSingleResult().toString());		
			 
             if(roleId != 1){
         			response=true;
         	  }else {
         			response=false;
         	  }
		}catch(NoResultException ex){
		 response=true;
	    }
	  return response;
    }
	

	/**
	 * Método creado para validar el aviso de cambio de contraseña.
	 * @return return true si la fecha actual es mayor a la de cambio de contraseña o false en otro caso.
	 * @author psanchez
	 */
	 public boolean msgChangeReminder(Long userId){	
		boolean response=false;
		try{	
			//días mínimos parametrizados
    		String daysMinimum = SystemParametersEJB.getParameterValueById(20L);
		    long minimumMillisecondDays = Long.parseLong(daysMinimum)*day;
			
			//días máximos  parametrizados
    		String daysMaximum = SystemParametersEJB.getParameterValueById(21L);
		    long maximumMillisecondDays = Long.parseLong(daysMaximum)*day;
					
			//consulta último cambio contraseña de usuario
			Query query= em.createNativeQuery("SELECT tsu.user_pwd_date FROM tb_system_user tsu WHERE tsu.user_id=?1 ");
			query.setParameter(1,userId);
			Timestamp lastDate=(Timestamp) query.getSingleResult();
	    
		    if(this.roleAdmin(userId)==true && 
		    	new Date(System.currentTimeMillis()).getTime() - lastDate.getTime() >= minimumMillisecondDays && 
		    	new Date(System.currentTimeMillis()).getTime() - lastDate.getTime() <= maximumMillisecondDays){
				response=true;
			}else{
			    response=false;
			}	
		}catch(NoResultException ex){
			System.out.println(" [ Error en LoginEJB.msgChangeReminder ] ");
			response=false;
		}
	 return response;
     }
	
	
	 /**
	 * Método encargada de mostrar el mensaje de cambio de contraseña al usuario en sesión, de acuerdo a los dias parametrizados.
	 * @return return numero de dias.
	 * @author psanchez
	 */
	@Override
	public String passwordChangeReminder(Long userId) {		
		try{
			//días máximos  parametrizados
    		String daysMaximum = SystemParametersEJB.getParameterValueById(21L);
		    long maximumMillisecondDays = Long.parseLong(daysMaximum)*day;
					
			//consulta último cambio contraseña de usuario
			Query query= em.createNativeQuery("SELECT tsu.user_pwd_date FROM tb_system_user tsu WHERE tsu.user_id=?1 ");
			query.setParameter(1,userId);
			Timestamp fechaFinalMs=(Timestamp) query.getSingleResult();
		    
			//consulta encargada de mostrar el mensaje de cambio de contraseña al usuario en sesión, de acuerdo a los días parametrizados.
		    String dayMessage = SystemParametersEJB.getParameterValueById(33L);
		    double daysMessage = Math.floor(Long.parseLong(dayMessage));

			Long diferenciaMs = new Date(System.currentTimeMillis()).getTime() - fechaFinalMs.getTime();	
			Long diferenciaMax = maximumMillisecondDays-diferenciaMs;
			double days = Math.floor(diferenciaMax / day);
				if (days<=daysMessage && days>=2){
					return ("Su contraseña vencerá en los próximos "+(int) days+ " días. Por favor cambiela!!!");
				} else if(days<=daysMessage && days==1){
					return ("Su contraseña vencerá en "+(int) days+ " día. Por favor cambiela!!!");
				} else if(days<=daysMessage && days==0){
					return ("Su contraseña vence hoy. Por favor cambiela!!!");
				} 
		}catch(NoResultException ex){
			System.out.println(" [ Error en LoginEJB.passwordChangeReminder ] ");
			return "";
		}
		return "";
  	}
	
	
	/**
	 * Método creado para reiniciar el contador de intentos fallidos, siempre y cuando no se haya alcanzado el umbral.
	 * @return return true si el contador de intentos no sobrepasa el umbral de intentos fallidos o false en otro caso.
	 * @author psanchez
	 */
	public boolean resetCount(Long userId){
		boolean response=false;	
		try{
			//consulta horas de bloqueo parametrizado para ingresar a la aplicación a usuarios diferente al rol admin
			String hourParameter = SystemParametersEJB.getParameterValueById(22L);
			long hoursCounterReset=Long.parseLong(hourParameter)*3600000;          
			
			//consulta cantidad de intentos fallidos parametrizado antes de inactivar a usuario diferente al rol admin
			String maxFaliledAttempts = SystemParametersEJB.getParameterValueById(24L);
	        
			//consulta cantidad de intentos fallidos del usuario
	        Query q3= em.createNativeQuery("SELECT tsu.user_count_intent FROM Tb_System_User tsu WHERE tsu.user_id=?1 "); 
	        q3.setParameter(1, userId);
	        long faliledAttemptsCount=Long.parseLong(q3.getSingleResult()== null ? "0" : q3.getSingleResult().toString());	
	         
			//consulta fecha del último ingreso del usuario a la aplicación
	        Query query= em.createNativeQuery("SELECT tsu.user_last_intent_date FROM Tb_System_User tsu WHERE to_char(tsu.user_last_intent_date,'dd/MM/yyyy hh24:mi:ss') <= to_char(sysdate,'dd/MM/yyyy hh24:mi:ss') and tsu.user_id=? "); 
	        query.setParameter(1, userId);
	        Timestamp userLastAttemptDate= (Timestamp) query.getSingleResult();
	        
			 if((faliledAttemptsCount < Long.parseLong(maxFaliledAttempts)) && (new Date(System.currentTimeMillis()).getTime() - userLastAttemptDate.getTime() >= hoursCounterReset )){
	    			Query query1= em.createNativeQuery("UPDATE tb_system_user tsu SET tsu.user_count_intent=0, tsu.user_last_intent_date='' WHERE tsu.user_id=?1 ");
	    			query1.setParameter(1, userId);
	    			query1.executeUpdate();
				    response=true;
		     	}else{
				  response=false;
				}
		}catch(NoResultException ex){
			response=false;
		}catch(NullPointerException ex){
			System.out.println(" [ Error en LoginEJB.resetCount.NullPointerException ] ");
			response=false;
		}
		return response;
	}
	

	/**
	 * Método creado para validar el tiempo de bloqueo al alcanzar el umbral de intentos fallidos de ingreso a la aplicación.
	 * @return return true si la fecha actual sobrepasa el tiempo de bloqueo o false en otro caso.
	 * @author psanchez
	 */
	@Override
	public boolean lockAccount(Long userId){
		boolean response=false;
		try{
			 //consulta minutos de bloqueo parametrizado para el ingreso a la aplicación
			 String parameterHour = SystemParametersEJB.getParameterValueById(27L);
			 long hourLock = Long.parseLong(parameterHour)*60000;

			 Query q2= em.createNativeQuery("SELECT tsu.user_lock_date FROM Tb_System_User tsu WHERE tsu.user_id=?1 "); 
	         q2.setParameter(1, userId);
	         Timestamp userLockDate= (Timestamp) q2.getSingleResult();
	         if(this.roleAdmin(userId)==true && userLockDate!=null){
		         if(new Date(System.currentTimeMillis()).getTime() - userLockDate.getTime() >= hourLock){
		        	 
	        	    Query quer= em.createNativeQuery("SELECT tsu.user_state_old FROM tb_system_user tsu WHERE tsu.user_id=?1 ");		 
	                quer.setParameter(1,userId);
	                int userState =((BigDecimal) quer.getSingleResult()).intValue();
		                
		        	Query query= em.createNativeQuery("UPDATE tb_system_user tsu SET tsu.user_lock_date='', tsu.user_state=?2, tsu.user_count_intent=0 WHERE tsu.user_id=?1 ");
		        	query.setParameter(1, userId);
		        	query.setParameter(2, userState);
		        	query.executeUpdate();
		 			response=false;
		 		 }else{
					response=true;
				 }
			 }else{
				response=false;
			 }
		   }catch(NoResultException ex){
				System.out.println(" [ Error en LoginEJB.lockAccount ] ");
				response=false;
		   }
		return response;
	}
	
	/**
	 * Método creado para validar si la contraseña de ingreso es el código otp 
	 * @author psanchez
	 */
	public String userOtpLogin(Long userId) {
		BigDecimal userOtp, otpId = null;
		try {
			Query q = em.createNativeQuery("select max(user_otp_id) " +
					"from re_user_otp " +
					"where user_id=?1 " +
					"order by date_relation desc ");
			q.setParameter(1,userId);
			userOtp = (BigDecimal) q.getSingleResult();
			
			Query query = em.createNativeQuery("select otp_id " +
					"from re_user_otp " +
					"where user_otp_id=?1 " +
					"and user_id=?2 ");
			query.setParameter(1, Long.parseLong(userOtp.toString()));
			query.setParameter(2,userId);
			otpId = (BigDecimal) query.getSingleResult();
			
		} catch (NullPointerException e) {
			return "";
		}catch (Exception e) {
			return "";
		}
		return otpId.toString();
	}

	
	/**
	 * Método creado para validar el número de ingresos fallidos del código otp. 
	 * @return return true si el contador de ingresos fallidos del código otp sobrepasa el umbral o false en otro caso.
	 * @author psanchez
	 */
	 @SuppressWarnings("unused")
	public boolean userOtpModal(Long userId, int count, String ip) {
		boolean response=false;
		try{
			TbSystemUser user = em.find(TbSystemUser.class, userId);

			Query query2 = em.createNativeQuery("UPDATE tb_system_user SET user_count_intent=0 WHERE user_count_intent is null");
	        query2.executeUpdate();
	        
		    Query query = em.createNativeQuery("UPDATE tb_system_user tsu SET tsu.user_count_intent=?3, tsu.user_last_intent_date=?1 WHERE tsu.user_id=?2 ");
		    query.setParameter(1,new Timestamp(System.currentTimeMillis()));
		    query.setParameter(2,userId);
		    query.setParameter(3,count);
		    query.executeUpdate();

			TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, user.getUserId() );
			Long idPTA;
				if(pt==null){
					idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  user.getUserId(), ip, user.getUserId());
				}else{
					idPTA=pt.getProcessTrackId();
				}
			Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.PASSWORD_MANAGEMENT, 
					   "Ingreso de código de seguridad incorrecto por el usuario "+ user.getUserNames()+ " " + user.getUserSecondNames()+".",
					   user.getUserId(), ip, 1,"",0, "", "",null,null,null);
			//proceso cliente
			TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,user.getUserId());
			Long idPTC;
				if(ptc==null){
					idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, user.getUserId(), ip, user.getUserId());
				}else{
					idPTC=ptc.getProcessTrackId();
				}
			Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.PASSWORD_MANAGEMENT, 
					   "Ingreso de código de seguridad incorrecto por el usuario "+ user.getUserNames()+ " " + user.getUserSecondNames()+".",
					   user.getUserId() , ip, 1, "", 0,"","",null,null,null);
		    response=true;
			}catch(NoResultException ex){
				System.out.println(" [ Error en LoginEJB.userOtpIntent.NoResultException ] ");
				response=false;
			}catch(NullPointerException ex){
				System.out.println(" [ Error en LoginEJB.userOtpIntent.NullPointerException ] ");
				response=false;
			}
		return response;
	}
	 
	 /**
	 * Método creado para validar el número de ingresos fallidos del código otp en el modal 
	 * @return return true si el contador de ingresos fallidos del código otp sobrepasa el umbral o false en otro caso.
	 * @author psanchez
	 */
	 @SuppressWarnings("unused")
	public boolean userOtpLockedModal(Long userId, int count, String ip) {
		boolean response=false;
		try{
			TbSystemUser user = em.find(TbSystemUser.class, userId);
			Query query2 = em.createNativeQuery("UPDATE tb_system_user SET user_count_intent=0 WHERE user_count_intent is null");
	        query2.executeUpdate();
	        
		    Query query = em.createNativeQuery("UPDATE tb_system_user tsu SET tsu.user_count_intent=?3, " +
		    								   "tsu.user_last_intent_date=?1 WHERE tsu.user_id=?2 ");
		    query.setParameter(1,new Timestamp(System.currentTimeMillis()));
		    query.setParameter(2,userId);
		    query.setParameter(3,count);
		    query.executeUpdate();

			String maxFailedLogin = SystemParametersEJB.getParameterValueById(24L);
 
        	Query q3= em.createNativeQuery("SELECT tsu.user_count_intent FROM tb_system_user tsu WHERE tsu.user_id=?1 "); 
            q3.setParameter(1,userId);
            long count_failed_login_user = Long.parseLong(q3.getSingleResult().toString());

	            if(/*this.roleAdmin(userId)==true && */count_failed_login_user>= Long.parseLong(maxFailedLogin)){	
	            	
	    			Query quer= em.createNativeQuery("SELECT tsu.user_state FROM tb_system_user tsu WHERE tsu.user_id=?1 ");		 
	                quer.setParameter(1,userId);
	                int userState =((BigDecimal) quer.getSingleResult()).intValue();
	                
	    		    Query q4 = em.createNativeQuery("UPDATE tb_system_user tsu SET tsu.user_lock_date=?1, " +
	    		    		                        "tsu.user_state_old=?3, tsu.user_state=1 WHERE tsu.user_id=?2 ");
	    			q4.setParameter(1,new Timestamp(System.currentTimeMillis()));
	    			q4.setParameter(2,userId);
	    			q4.setParameter(3,userState);
	    			q4.executeUpdate();
	    			
	    			//proceso administrador
			        TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, user.getUserId() );
					Long idPTA;
						if(pt==null){
							idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  user.getUserId(), ip, user.getUserId());
						}else{
							idPTA=pt.getProcessTrackId();
						}
					Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.PASSWORD_MANAGEMENT, 
							   "El usuario " + user.getUserNames()+ " " + user.getUserSecondNames() + 
							   " sobrepasó el número de intentos permitidos para el ingreso del código de seguridad correcto.",
							   user.getUserId(), ip, 1,"",0, "", "",null,null,null);
					//proceso cliente
					TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,user.getUserId());
					Long idPTC;
						if(ptc==null){
							idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, user.getUserId(), ip, user.getUserId());
						}else{
							idPTC=ptc.getProcessTrackId();
						}
					Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.PASSWORD_MANAGEMENT, 
							"El usuario " + user.getUserNames()+ " " + user.getUserSecondNames() + 
							" sobrepasó el número de intentos permitidos para el ingreso del código de seguridad correcto.",
							user.getUserId() , ip, 1, "", 0,"","",null,null,null);					
	    			response=true;
	    		 }else{
	    		    response=false;
	    		 }
			}catch(NoResultException ex){
				System.out.println(" [ Error en LoginEJB.userOtpLockedModal.NoResultException ] ");
				response=false;
			}catch(NullPointerException ex){
				System.out.println(" [ Error en LoginEJB.userOtpLockedModal.NullPointerException ] ");
				response=false;
			}
		return response;
	}
	 
	 
	 /**
	 * Método creado para registrar de ingresos fallidos del código otp en el login 
	 * @return return true si el contador de ingresos fallidos se registro en bd o false en otro caso.
	 * @author psanchez
	 */
	public boolean userOtpLoginCount(Long userId, int count, String ip) {
		boolean response=false;
		try{
			Query query = em.createNativeQuery("UPDATE tb_system_user tsu SET tsu.user_count_intent=?1, tsu.user_last_intent_date=?2 WHERE tsu.user_id=?3 ");
		    query.setParameter(1,count);
		    query.setParameter(2,new Timestamp(System.currentTimeMillis()));
		    query.setParameter(3,userId);
		    query.executeUpdate();
	    	response=true;
			}catch(NoResultException ex){
				System.out.println(" [ Error en LoginEJB.userOtpLoginCount.NoResultException ] ");
				response=false;
			}catch(NullPointerException ex){
				System.out.println(" [ Error en LoginEJB.userOtpLoginCount.NullPointerException ] ");
				response=false;
			}
		return response;
	} 
	
	 /**
	 * Método creado para registrar en el contador y ver procesos el número de ingresos fallidos del código otp. 
	 * @author psanchez
	 */ 
	@SuppressWarnings("unused")
	public void compareOtpLogin(Long userId, String ip){
		/*Query query2 = em.createNativeQuery("UPDATE tb_system_user SET user_count_intent=0 WHERE user_count_intent is null");
        query2.executeUpdate();
        
	    Query query = em.createNativeQuery("UPDATE tb_system_user tsu SET tsu.user_count_intent=tsu.user_count_intent+1, tsu.user_last_intent_date=?1 WHERE tsu.user_id=?2 ");
	    query.setParameter(1,new Timestamp(System.currentTimeMillis()));
	    query.setParameter(2,userId);
	    query.executeUpdate();*/
	    
		TbSystemUser user = em.find(TbSystemUser.class, userId);	
		TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, user.getUserId() );
		Long idPTA;
			if(pt==null){
				idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  user.getUserId(), ip, user.getUserId());
			}else{
				idPTA=pt.getProcessTrackId();
			}
		Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.PASSWORD_MANAGEMENT, 
				   "El usuario "+ user.getUserNames()+ " " + user.getUserSecondNames()+" " +
				   "ingresó un código de seguridad incorrecto.",user.getUserId(), ip, 1,"",0, "", "",null,null,null);
		//proceso cliente
		TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,user.getUserId());
		Long idPTC;
			if(ptc==null){
				idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, user.getUserId(), ip, user.getUserId());
				System.out.println("ptc es null");
			}else{
				idPTC=ptc.getProcessTrackId();
			}
		Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.PASSWORD_MANAGEMENT, 
				   "El usuario "+ user.getUserNames()+ " " + user.getUserSecondNames()+" " +
				   "ingresó un código de seguridad incorrecto.", user.getUserId() , ip, 1, "", 0,"","",null,null,null);		
	}
	
	 
	/**
	 * Método creado para validar el número de intentos de inicio de sesión fallidos. 
	 * @return return true si el contador de intentos fallidos sobrepasa el umbral o false en otro caso.
	 * @author psanchez
	 */
	 public boolean userPassIntent(long userId, int count, String ip) {
		boolean response=false;
		
		try{
		    Query query = em.createNativeQuery("UPDATE tb_system_user tsu SET tsu.user_count_intent=tsu.user_count_intent+1, tsu.user_last_intent_date=?2 WHERE tsu.user_id=?3 ");
		    //query.setParameter(1,count);
		    query.setParameter(2,new Timestamp(System.currentTimeMillis()));
		    query.setParameter(3,userId);
		    query.executeUpdate();

			String maxFailedLogin = SystemParametersEJB.getParameterValueById(24L);
 
        	Query q3= em.createNativeQuery("SELECT tsu.user_count_intent FROM tb_system_user tsu WHERE tsu.user_id=?1 "); 
            q3.setParameter(1,userId);
            Long count_failed_login_user = Long.parseLong(q3.getSingleResult().toString());
            
	            if(this.roleAdmin(userId)==true && count_failed_login_user>= Long.parseLong(maxFailedLogin)){
	            	Query quer= em.createNativeQuery("SELECT tsu.user_state FROM tb_system_user tsu WHERE tsu.user_id=?1 ");
	                quer.setParameter(1,userId);
	                int userState =((BigDecimal) quer.getSingleResult()).intValue();
	             
	    		    Query q4 = em.createNativeQuery("UPDATE tb_system_user tsu SET tsu.user_lock_date=?1, tsu.user_state_old=?3, tsu.user_state=1 WHERE tsu.user_id=?2 ");
	    			q4.setParameter(1,new Timestamp(System.currentTimeMillis()));
	    			q4.setParameter(2,userId);
	    			q4.setParameter(3,userState);
	    			q4.executeUpdate();
	    			
	    			this.processPassIntent(userId, ip);
	    			response=true;
		    	}
	            else if(this.roleAdmin(userId)==false && count_failed_login_user>= Long.parseLong(maxFailedLogin)){
	            	this.processPassIntent(userId, ip);
	            	response=false;
		    	}
			}catch(NoResultException ex){
				System.out.println(" [ Error en LoginEJB.userPassIntent ] ");
				response=false;
			}catch(NullPointerException ex){
				System.out.println(" [ Error en LoginEJB.userPassIntent.NullPointerException ] ");
				response=false;
			}
		return response;
	}
	 
	
	 /**
	 * Método creado para validar el número de intentos de inicio de sesión fallidos de usuario nuevo. 
	 * @return return true si el contador de intentos fallidos sobrepasa el umbral o false en otro caso.
	 * @author psanchez
	 */
	 public boolean userPassNew(long userId, int count, String ip) {
		boolean response=false;
		
		try{
		    Query query = em.createNativeQuery("UPDATE tb_system_user tsu SET tsu.user_count_intent=tsu.user_count_intent+1, tsu.user_last_intent_date=?2 WHERE tsu.user_id=?3 ");
		    //query.setParameter(1,count);
		    query.setParameter(2,new Timestamp(System.currentTimeMillis()));
		    query.setParameter(3,userId);
		    query.executeUpdate();

			String maxFailedLogin = SystemParametersEJB.getParameterValueById(24L);
 
        	Query q3= em.createNativeQuery("SELECT tsu.user_count_intent FROM tb_system_user tsu WHERE tsu.user_id=?1 "); 
            q3.setParameter(1,userId);
            Long count_failed_login_user = Long.parseLong(q3.getSingleResult().toString());
            
	            if(/*this.roleAdmin(userId)==true && */count_failed_login_user>= Long.parseLong(maxFailedLogin)){
	            	Query quer= em.createNativeQuery("SELECT tsu.user_state FROM tb_system_user tsu WHERE tsu.user_id=?1 ");
	                quer.setParameter(1,userId);
	                int userState =((BigDecimal) quer.getSingleResult()).intValue();
	             
	    		    Query q4 = em.createNativeQuery("UPDATE tb_system_user tsu SET tsu.user_lock_date=?1, tsu.user_state_old=?3, tsu.user_state=1 WHERE tsu.user_id=?2 ");
	    			q4.setParameter(1,new Timestamp(System.currentTimeMillis()));
	    			q4.setParameter(2,userId);
	    			q4.setParameter(3,userState);
	    			q4.executeUpdate();
	    			
	    			this.processPassNew(userId, ip);
	    			response=true;
		    	}
	            /*else if(this.roleAdmin(userId)==false && count_failed_login_user>= Long.parseLong(maxFailedLogin)){
	            	this.processPassNew(userId, ip);
	            	response=false;
		    	}*/
			}catch(NoResultException ex){
				System.out.println(" [ Error en LoginEJB.userPassNew ] ");
				response=false;
			}catch(NullPointerException ex){
				System.out.println(" [ Error en LoginEJB.userPassNew.NullPointerException ] ");
				response=false;
			}
		return response;
	}
	 
	 /**
	 * Método creado para registrar procesos inicio de sesión fallidos de usuario nuevo.
	 * @author psanchez
	 */
	 @SuppressWarnings("unused")
	public void processPassNew(long userId, String ip) {		
		try{           	
         	TbSystemUser user   = em.find(TbSystemUser.class, userId);
			//proceso administrador
	        TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, user.getUserId() );
			Long idPTA;
				if(pt==null){
					idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  user.getUserId(), ip, user.getUserId());
				}else{
					idPTA=pt.getProcessTrackId();
				}
			Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.PASSWORD_MANAGEMENT, 
					   "El usuario " + user.getUserNames()+" "+user.getUserSecondNames() + 
					   " sobrepasó el número permitido de intentos para restablecer la contraseña.",
					   user.getUserId(), ip, 1,"",0, "", "",null,null,null);
			//proceso cliente
			TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,user.getUserId());
			Long idPTC;
				if(ptc==null){
					idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, user.getUserId(), ip, user.getUserId());
				}else{
					idPTC=ptc.getProcessTrackId();
				}
			Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.PASSWORD_MANAGEMENT, 
					 "El usuario " + user.getUserNames()+" "+user.getUserSecondNames() + 
					 " sobrepasó el número permitido de intentos para restablecer la contraseña.",
					 user.getUserId(), ip, 1, "", 0,"","",null,null,null);
			
			outbox.insertMessageOutbox(userId,
	                   EmailProcess.FAILED_LOGIN_COUNTER,
	                   null,
	                   null, 
	                   null, 
	                   null,
	                   null,
	                   null,
	                   null,
	                   null,
	                   userId,
		               null,
		               null,
		               null,
	                   true,
	                   null);			
			}catch(NoResultException ex){
				System.out.println(" [ Error en LoginEJB.processPassNew ] ");
			}
	}
		 
		 
	 /**
	 * Método creado para registrar procesos en el perfil usuario y admin
	 * @author psanchez
	 */
	 @SuppressWarnings("unused")
	public void processPassIntent(long userId, String ip) {		
		try{           	
         	TbSystemUser user   = em.find(TbSystemUser.class, userId);
			//proceso administrador
	        TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, user.getUserId() );
			Long idPTA;
				if(pt==null){
					idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  user.getUserId(), ip, user.getUserId());
				}else{
					idPTA=pt.getProcessTrackId();
				}
			Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.PASSWORD_MANAGEMENT, 
					   "El usuario " + user.getUserNames()+" "+user.getUserSecondNames() + 
					   " sobrepasó el número de intentos permitidos para el ingreso de la contraseña correcta.",
					   user.getUserId(), ip, 1,"",0, "", "",null,null,null);
			//proceso cliente
			TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,user.getUserId());
			Long idPTC;
				if(ptc==null){
					idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, user.getUserId(), ip, user.getUserId());
				}else{
					idPTC=ptc.getProcessTrackId();
				}
			Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.PASSWORD_MANAGEMENT, 
					 "El usuario " + user.getUserNames()+" "+user.getUserSecondNames() + 
					  " sobrepasó el número de intentos permitidos para el ingreso de la contraseña correcta.",
					 user.getUserId(), ip, 1, "", 0,"","",null,null,null);
			
			outbox.insertMessageOutbox(userId,
	                   EmailProcess.FAILED_LOGIN_COUNTER,
	                   null,
	                   null, 
	                   null, 
	                   null,
	                   null,
	                   null,
	                   null,
	                   null,
	                   userId,
		               null,
		               null,
		               null,
	                   true,
	                   null);			
			}catch(NoResultException ex){
				System.out.println(" [ Error en LoginEJB.processPassIntent ] ");
			}
	}
	 
		 
	   /**
		 * Método creado para obtener los usuarios pre-enrolados. 
		 * @return return true si el usuario esta pre-enrolado o false en otro caso.
		 * @author psanchez
		 */
		public boolean preenrollUser(Long userId){
           boolean response=false;
		   try{	
				Query query= em.createNativeQuery("SELECT tsu.user_state FROM tb_system_user tsu WHERE tsu.user_state=-1 AND tsu.user_id=?1 "); 
				query.setParameter(1, userId);
				long stateUser = Long.parseLong(query.getSingleResult().toString());	
				 
	               if(stateUser<0){
	            		response=true;
		           	}else {
		           		response=false;
		              }
				}catch(NoResultException ex){
					response=false;
				}
				return response;
		}
		
	
	@Override
	public boolean inactivateUser(long userId, String ip) {
		boolean result = false;
		try {
			TbSystemUser systemUser = em.find(TbSystemUser.class, userId);
			systemUser.setUserState(em.find(TbUserStateType.class, UserState.INACTIVE.getId()));
			systemUser.setUserLockDate(new Timestamp(System.currentTimeMillis()));
			new ObjectEM(em).update(systemUser);
			 if(this.roleAdmin(userId)==true) {
				result = true;
				log.insertLog("Inactivar Usuario | ID Usuario: " + systemUser.getUserId(), LogReference.USER, LogAction.UPDATE, ip, userId);
			  }else{
				result = false;
				log.insertLog("Inactivar Usuario | Error al desactivar ID Usuario: " + systemUser.getUserId(), LogReference.USER, LogAction.UPDATEFAIL, ip, userId);
			  } 	
		} catch (Exception e) {
			result = false;
			System.out.println(" [ Error en UserEJB.inactivateUser ] ");
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public boolean activateUser(long userId) {
		boolean result = false;
		try {
			 TbSystemUser systemUser = em.find(TbSystemUser.class, userId);
			 systemUser.setUserState(em.find(TbUserStateType.class, UserState.ACTIVE.getId()));
			 new ObjectEM(em).update(systemUser);
			 result = true;
			} catch (Exception e) {
			   System.out.println(" [ Error en LoginEJB.activateUser ] ");
			   e.printStackTrace();
			}
		return result;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Login#getRoleByUser(long)
	 */
	@Override
	public long getRoleByUser(long systemUserId) {
		Long role;
		Object obj = new Object();
		try {
			obj = infoUserQuery.setParameter(1, systemUserId).getSingleResult();
			role = Long.parseLong(obj.toString());
			return role;
		} catch (Exception e) {
			e.printStackTrace();
			return 0L;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Login#updateLastLogin(long)
	 */
	@Override
	public boolean updateLastLogin(long userId) {
		boolean result = false;
		try {
			TbSystemUser systemUser = em.find(TbSystemUser.class, userId);
			systemUser.setUserLastLogin(systemUser.getUserAccesstLogin());
			systemUser.setUserCountIntent(0);
			systemUser.setUserStateConnection(1);
			new ObjectEM(em).update(systemUser);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public void inactiveOtp(String otp, Long userId){
		try {
			Query query = em.createNativeQuery("select user_otp_id " +
					"from re_user_otp " +
					"where otp_id=?1 " +
					"and user_id=?2 ");
			query.setParameter(1, Long.parseLong(otp.toString()));
			query.setParameter(2,userId);
			BigDecimal userOtpId = (BigDecimal) query.getSingleResult();
			ReUserOtp reu = em.find(ReUserOtp.class,userOtpId.longValue());
			reu.setStateOtp(em.find(TbStateOtp.class,1L));
			em.merge(reu);
			em.flush();
		}catch (NumberFormatException nu) {
			System.out.println("Formato de numero incorrecto");	
		} catch (NoResultException ne){
			System.out.println("No se encontro OTP para el usuario que ingreso");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al inactivar el OPT");
		}
		
		
	}

	/**@author psanchez 
	 * Método encargado de validar si existe el código otp ingresado en el modal
	 */
	public String validateOtpModal(String otp, Long userId, String ip){
		String msjValidatePassword= null;
		try{
			TbSystemUser user = em.find(TbSystemUser.class, userId);
			Timestamp factual = new Timestamp(System.currentTimeMillis());
			String timeOtp = SystemParametersEJB.getParameterValueById(47L);
			
			Query query = em.createNativeQuery("select user_otp_id " +
					"from re_user_otp " +
					"where otp_id=?1 " +
					"and user_id=?2 ");
			query.setParameter(1, Long.parseLong(otp.toString()));
			query.setParameter(2,userId);
			BigDecimal userOtpId = (BigDecimal) query.getSingleResult();
			
			Query query2 = em.createNativeQuery("select nvl(state_otp_id,0) " +
					"from re_user_otp " +
					"where otp_id=?1 " +
					"and user_id=?2 ");
			query2.setParameter(1, Long.parseLong(otp.toString()));
			query2.setParameter(2,userId);
			BigDecimal userOtpState = (BigDecimal) query2.getSingleResult();
			
			
			Query q = em.createNativeQuery("select max(user_otp_id) " +
					"from re_user_otp " +
					"where user_id=?1 " +
					"order by date_relation desc ");
			q.setParameter(1,userId);
			BigDecimal userOtp = (BigDecimal) q.getSingleResult();
			
			Query dateRelation = em.createNativeQuery("select ruo.date_relation from re_user_otp ruo where ruo.otp_id = ?1 " );
			dateRelation.setParameter(1, Long.parseLong(otp.toString()));
			Timestamp fRelation = (Timestamp) dateRelation.getSingleResult();
			
			int H = Integer.parseInt(timeOtp.split(":")[0]);
			int M = Integer.parseInt(timeOtp.split(":")[1]);
			int S = Integer.parseInt(timeOtp.split(":")[2]);
			
			long hour = H*3600000;
			long minute = M*60000;
			long second = S*1000;
			
			long vigenciaOtp = hour+minute+second;
			long result = factual.getTime()- fRelation.getTime();

			if(userOtp.compareTo(userOtpId)==0 && (result>vigenciaOtp || userOtpState.intValue()==1)){
				Long newOtp = userEJB.numRad(userId);//se genera nuevamente el numero OTP
				ReUserOtp reu = em.find(ReUserOtp.class,userOtp.longValue());
				reu.setStateOtp(em.find(TbStateOtp.class,1L));
				try{
					ArrayList<String> parameters=new ArrayList<String>();
					parameters.add("#OTP="+newOtp);
									
					outbox.insertMessageOutbox(user.getUserId(), 
			                   EmailProcess.NEW_OTP,
			                   null,
			                   null,
			                   null, 
			                   null,
			                   null,
			                   null,
			                   null,
			                   null,
			                   user.getUserId(),
				               null,
				               null,
				               null,
			                   true,
			                   parameters);
					
				}catch (Exception e) {
					System.out.println(" [ Error en SecurityQuestionsEjb.validatePassword Error Al Enviar Correo ] ");
					e.printStackTrace();
				}		
				this.processExpirationOtpModal(userId, ip);
				msjValidatePassword = "1";
				
			}else if(userOtp.compareTo(userOtpId)==0 && result<vigenciaOtp && userOtpState.intValue()==0){
				TbOtp to = em.find(TbOtp.class, Long.parseLong(otp.toString()));
				long otpAux = to.getOtpId();
				
				if(otpAux == Long.parseLong(otp.toString())){
					msjValidatePassword= "2";				
				}			
			}else if(userOtp.compareTo(userOtpId) != 0){
				msjValidatePassword = "3";	
			}
		}catch(NoResultException ex){
			msjValidatePassword = "3";	
		}catch(NullPointerException ex){
			msjValidatePassword = "3";	
		}catch (Exception e) {
			msjValidatePassword = "3";	
		}
	  return msjValidatePassword;	
	}
	
	/**@author psanchez 
	 * Método encargado de validar si existe o no el otp, como contraseña ingresado por el usuario.
	 * */
	public boolean validateOtpLogin(String otp, Long userId, String ip){
		boolean result=false;
		try{		
			Query query = em.createNativeQuery("select user_otp_id " +
					"from re_user_otp " +
					"where otp_id=?1 " +
					"and user_id=?2 ");
			query.setParameter(1, Long.parseLong(otp.toString()));
			query.setParameter(2,userId);
			BigDecimal userOtpId = (BigDecimal) query.getSingleResult();
			
			Query q = em.createNativeQuery("select max(user_otp_id) " +
					"from re_user_otp " +
					"where user_id=?1 " +
					"order by date_relation desc ");
			q.setParameter(1,userId);
			BigDecimal userOtp = (BigDecimal) q.getSingleResult();			
			
			if(userOtp.compareTo(userOtpId) != 0){
				this.compareOtpLogin(userId, ip);
				result=true;
			}
		}catch(NoResultException ex){
			this.compareOtpLogin(userId, ip);
			result=false;
		}catch(NumberFormatException ex){
			this.compareOtpLogin(userId, ip);
			result=false;
		}
	  return result;	
	}
	

	public boolean expirationOtpLogin(String otp, Long userId, String ip){
        boolean result = false;
		try{
			Timestamp factual = new Timestamp(System.currentTimeMillis());		
			TbSystemUser user = em.find(TbSystemUser.class, userId);
			
			Query dateRelation = em.createNativeQuery("select ruo.date_relation from re_user_otp ruo where ruo.otp_id = ?1 " );
			dateRelation.setParameter(1, Long.parseLong(otp.toString()));
			Timestamp fRelation =  (Timestamp) dateRelation.getSingleResult();
			
			Query query2 = em.createNativeQuery("select nvl(state_otp_id,0) " +
					"from re_user_otp " +
					"where otp_id=?1 " +
					"and user_id=?2 ");
			query2.setParameter(1, Long.parseLong(otp.toString()));
			query2.setParameter(2,userId);
			BigDecimal userOtpState = (BigDecimal) query2.getSingleResult();
			
			String timeOtp = SystemParametersEJB.getParameterValueById(47L);
			
			int H = Integer.parseInt(timeOtp.split(":")[0]);
			int M = Integer.parseInt(timeOtp.split(":")[1]);
			int S = Integer.parseInt(timeOtp.split(":")[2]);
			
			long hour = H*3600000;
			long minute = M*60000;
			long second = S*1000;
			
			long vigenciaOtp = hour+minute+second;
			long diferencia = factual.getTime()- fRelation.getTime();

			if(diferencia>vigenciaOtp || userOtpState.longValue()==1L){
				Long newOtp = userEJB.numRad(userId);//se genera nuevamente el numero OTP
				try{
					ArrayList<String> parameters=new ArrayList<String>();
					parameters.add("#OTP="+newOtp);
									
					outbox.insertMessageOutbox(user.getUserId(), 
			                   EmailProcess.NEW_OTP,
			                   null,
			                   null,
			                   null, 
			                   null,
			                   null,
			                   null,
			                   null,
			                   null,
			                   user.getUserId(),
				               null,
				               null,
				               null,
			                   true,
			                   parameters);
					user.setUserPassword(Encryptor.getEncrypt(newOtp.toString()));
					user.setUserPwdDate(new Timestamp(System.currentTimeMillis()));
					
				}catch (Exception e) {
					System.out.println(" [ Error en expirationOtpLogin Error Al Enviar Correo ] ");
					e.printStackTrace();
				}						
				this.processExpirationOtpLogin(userId, ip);
				result = true;
			}						
		}catch(NoResultException ex){
			result = false;			
		} catch (Exception e) {
			result = false;			
		}
		return result;
	}
	
	
	@SuppressWarnings("unused")
	public void processExpirationOtpModal(Long userId, String ip){		
		TbSystemUser user = em.find(TbSystemUser.class, userId);
		
		//proceso administrador
        TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, user.getUserId() );
		Long idPTA;
			if(pt==null){
				idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  user.getUserId(), ip, user.getUserId());
			}else{
				idPTA=pt.getProcessTrackId();
			}
		Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.PASSWORD_MANAGEMENT, 
				   "Generación de código de seguridad por caducidad para el usuario " + 
				   user.getUserNames()+" "+ user.getUserSecondNames()+".", user.getUserId(), ip, 1,"",0,"","",null,null,null);
		//proceso cliente
		TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,user.getUserId());
		Long idPTC;
			if(ptc==null){
				idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, user.getUserId(), ip, user.getUserId());
			}else{
				idPTC=ptc.getProcessTrackId();
			}
		Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.PASSWORD_MANAGEMENT, 
				"Generación de código de seguridad por caducidad para el usuario " + 
				user.getUserNames()+" "+ user.getUserSecondNames()+".", user.getUserId(), ip, 1, "", 0,"","",null,null,null);
	}
	
	
	@SuppressWarnings("unused")
	public void processExpirationOtpLogin(Long userId, String ip){		
		TbSystemUser user = em.find(TbSystemUser.class, userId);
		
		//proceso administrador
        TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, user.getUserId() );
		Long idPTA;
			if(pt==null){
				idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  user.getUserId(), ip, user.getUserId());
			}else{
				idPTA=pt.getProcessTrackId();
			}
		Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.PASSWORD_MANAGEMENT, 
				   "Se generó un nuevo código de seguridad para restablecer la contraseña del usuario " + 
				   user.getUserNames()+" "+ user.getUserSecondNames()+".", user.getUserId(), ip, 1,"",0,"","",null,null,null);
		//proceso cliente
		TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,user.getUserId());
		Long idPTC;
			if(ptc==null){
				idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, user.getUserId(), ip, user.getUserId());
			}else{
				idPTC=ptc.getProcessTrackId();
			}
		Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.PASSWORD_MANAGEMENT, 
				"Se generó un nuevo código de seguridad para restablecer la contraseña del usuario " + 
				user.getUserNames()+" "+ user.getUserSecondNames()+".", user.getUserId(), ip, 1, "", 0,"","",null,null,null);
	}

	/**
	 * Método encargada de mostar mensaje de configuración de Preguntas Seguridad al usuario en sesión
	 * @return return mensaje.
	 * @author psanchez
	 */
	@Override
	public String questionResponseReminder(Long userId) {
		try{	
			//consulta si el usuario ha creado preguntas de seguridad
			if(securityQuestionsEJB.validateQuestionsResponse(userId)==false){
				//dias maximos parametrizados
	    		String daysMaximum = SystemParametersEJB.getParameterValueById(21L);
			    long maximumMillisecondDays = Long.parseLong(daysMaximum)*day;
			    
				//consulta último cambio contraseña de usuario
				Query query= em.createNativeQuery("SELECT tsu.user_pwd_date FROM tb_system_user tsu WHERE tsu.user_id=?1 ");
				query.setParameter(1,userId);
				Timestamp fechaFinalMs=(Timestamp) query.getSingleResult();			    
			    
			    //consulta encargada de mostrar el mensaje de cambio de contraseña al usuario en sesión, de acuerdo a los dias parametrizados.
			    String dayMessage = SystemParametersEJB.getParameterValueById(33L);
			    double daysMessage = Math.floor(Long.parseLong(dayMessage));
			    
			    Long diferenciaMs = new Date(System.currentTimeMillis()).getTime() - fechaFinalMs.getTime();	
				Long diferenciaMax = maximumMillisecondDays-diferenciaMs;
				double days = Math.floor(diferenciaMax / day);
				if (days<=daysMessage){
					String message = SystemParametersEJB.getParameterValueById(50L);
					return message;
				}
			}else{
				return "";
			}
		}catch(NoResultException ex){
			System.out.println(" [ Error en LoginEJB.questionResponseReminder ] ");
			return "";
		}
		return "";
  	}
	
	 /**
	 * Método creado para guardar la fecha de acceso a la aplicación del usuario en session.. 
	 * @return return true actualizo la fecha o false en otro caso.
	 * @author psanchez
	 */
	@Override
	public boolean updateAccessLogin(long userId) {
		boolean result = false;
		try {
			TbSystemUser systemUser = em.find(TbSystemUser.class, userId);
			systemUser.setUserAccesstLogin(new Timestamp(System.currentTimeMillis()));
			if(systemUser.getUserQuestionsDate() == null){
				systemUser.setUserQuestionsDate(new Timestamp(System.currentTimeMillis()));
			}				
			systemUser.setUserStateConnection(0);
			new ObjectEM(em).update(systemUser);
			result = true;
		} catch (Exception e) {
			System.out.println(" [ Error en LoginEJB.updateAccessLogin ] ");
			e.printStackTrace();
		}
		return result;
	}

	public String getLastEntry(Timestamp dateLastEntry){
		String fechaUltEntrada = "";
		SimpleDateFormat formatoDeFecha = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
		
		Query q = em.createNativeQuery("select to_char(?1, 'D') from dual");
		q.setParameter(1, dateLastEntry);
		
		int result = Integer.parseInt((String) q.getSingleResult());
		
		switch(result) {
		 case 1: 
			 fechaUltEntrada ="Domingo";
		     break;
		 case 2: 
			 fechaUltEntrada ="Lunes";
		     break;
		 case 3: 
			 fechaUltEntrada ="Martes";
		     break;
		 case 4: 
			 fechaUltEntrada ="Miércoles";
		     break;
		 case 5: 
			 fechaUltEntrada ="Jueves";
		     break;
		 case 6: 
			 fechaUltEntrada ="Viernes";
		     break;
		 case 7: 
			 fechaUltEntrada ="Sábado";
		     break;    
		 default: 
			 fechaUltEntrada="Otro";
		     break;
		 }
		
		fechaUltEntrada = fechaUltEntrada+", "+formatoDeFecha.format(dateLastEntry);
		
		return fechaUltEntrada;
	}
	
	public TbSystemUser getUserMasterById(Long userId){
		TbSystemUser um = null;
		um = em.find(TbSystemUser.class, userId);
		return um;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AllInfoAccount> getAccountLoginList(Long userId) {
		List<AllInfoAccount> list = new ArrayList<AllInfoAccount>();
		
		try {
			Query query= em.createNativeQuery("SELECT DISTINCT to_char(ta.account_id), ta.account_balance, " +
					"tf.frequency_description, ta.account_opening_date " +
					"FROM tb_system_user tu " +
					"LEFT JOIN re_user_role rer ON tu.user_id = rer.user_id " +
					"LEFT JOIN tb_role r ON rer.role_id = r.role_id " +
					"LEFT JOIN tb_account ta ON ta.user_id = tu.user_id " +
					"LEFT JOIN re_account_bank rab ON ta.account_id = rab.account_id " +
					"LEFT JOIN tb_automatic_recharge tar ON ta.account_id = tar.account_id " +
					"LEFT JOIN tb_frequency tf ON tar.frecuency_id = tf.frequency_id " +
					"WHERE tu.user_id = ?1 " +
					"AND r.role_id = ?2 " +
					"AND tar.estado = 0 " +
					"AND rab.state_account_bank = 1 " +
					"UNION " +
					"SELECT DISTINCT to_char(ta.account_id), ta.account_balance, " +
					"'No Aplica', ta.account_opening_date " +
					"FROM tb_system_user tu " +
					"LEFT JOIN re_user_role rer ON tu.user_id = rer.user_id " +
					"LEFT JOIN tb_role r ON rer.role_id = r.role_id " +
					"LEFT JOIN tb_account ta ON ta.user_id = tu.user_id " +
					"LEFT JOIN re_account_bank rab ON ta.account_id = rab.account_id " +
					"WHERE tu.user_id = ?1 " +
					"AND r.role_id = ?2 " +
					"AND rab.state_account_bank = 1 " +
					"AND ta.account_id NOT IN (SELECT DISTINCT to_char(ta.account_id) " +
					"FROM tb_system_user tu " +
					"LEFT JOIN re_user_role rer ON tu.user_id = rer.user_id " +
					"LEFT JOIN tb_role r ON rer.role_id = r.role_id " +
					"LEFT JOIN tb_account ta ON ta.user_id = tu.user_id " +
					"LEFT JOIN tb_automatic_recharge tar ON ta.account_id = tar.account_id " +
					"LEFT JOIN tb_frequency tf ON tar.frecuency_id = tf.frequency_id " +
					"WHERE tu.user_id = ?1 " +
					"AND r.role_id = ?2 " +
					"AND tar.estado = 0 " +
					"AND rab.state_account_bank = 1) " +
					"ORDER BY 4 ");
			
			   query.setParameter(1, userId);
		       query.setParameter(2, constant.Role.CLIENT.getId());
		       List<Object> lis= (List<Object>)query.getResultList();
			
				for(int i=0;i<lis.size();i++){
					Object[] obj=(Object[]) lis.get(i);
					
					AllInfoAccount accountUser= new AllInfoAccount();
					
					if(accountUser!=null){
						accountUser.setAccountIdU(obj[0].toString());
						accountUser.setAccountBalanceU(obj[1]!=null?Long.valueOf(obj[1].toString()):0);
						accountUser.setFrequencyDescriptionU(obj[2]!=null?obj[2].toString():"No Aplica");
						list.add(accountUser);
					}
				 }	
		} catch(NullPointerException ex){
		} catch (Exception e) {
			System.out.println(" [ Error en LoginEJB.getAccountLoginList. ] "+e.getMessage());
		}
			return list;
	}

}
    	