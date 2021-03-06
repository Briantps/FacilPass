package ejb;

import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import radomic.Radom;

import constant.AccountStateType;
import constant.DistributionType;
import constant.EmailProcess;
import constant.EmailSubject;
import constant.EmailType;
import constant.LogAction;
import constant.LogReference;
import constant.ProcessTrackDetailType;
import constant.ProcessTrackType;
import constant.TypeTask;
import constant.UserState;
import constant.ValidatePassword;


import jpa.ReAccountBank;
import jpa.ReUserOtp;
import jpa.ReUserRole;
import jpa.TbAccount;
import jpa.TbAccountCloseLog;
import jpa.TbAccountType;
import jpa.TbAlarmBalance;
import jpa.TbAutomaticRecharge;
import jpa.TbBank;
import jpa.TbBankAccount;
import jpa.TbCodeType;
import jpa.TbDevice;
import jpa.TbDeviceHistory;
import jpa.TbDeviceState;
import jpa.TbDistributionType;
import jpa.TbMunicipality;
import jpa.TbOtp;
import jpa.TbPasswordUser;
import jpa.TbPaymentMethod;
import jpa.TbProcessTrack;
import jpa.TbProcessTrackDetailType;
import jpa.TbRole;
import jpa.TbStateOtp;
import jpa.TbSystemParameter;
import jpa.TbSystemUser;
import jpa.TbTag;
import jpa.TbUmbral;
import jpa.TbUserData;
import jpa.TbUserStateType;
import jpa.TbVehicle;

import util.AllInfoClient;
import util.Cities;
import util.ClientsDB;
import util.Encryptor;
import util.HistoricalRecharges;
import util.TimeUtil;
import util.ValidationPassword;

import crud.ObjectEM;

import ejb.email.Outbox;
import ejb.taskeng.TransitTask;


/**
 * Session Bean implementation class UserEJB
 * 
 * @author Frances Zucchet
 */
@Stateless(mappedName = "ejb/User")
public class UserEJB implements User {

	@PersistenceContext(unitName = "bo") 
	EntityManager em;

	//private Query userQuery;
	private ObjectEM objectEM;
	
	@EJB(mappedName="ejb/Contract")
	private Contract contractEJB;
	
	@EJB(mappedName="ejb/Device")
	private Device device;
		
	@EJB(mappedName ="ejb/sendMail")
	private SendMail sendMail;
	
	@EJB(mappedName ="ejb/email/Outbox")
	private Outbox outbox;
	
	@EJB(mappedName="ejb/Process")
	private Process process;
	
	@EJB(mappedName="ejb/Role")
	private Role role;
	
	@EJB(mappedName="ejb/Log")
	private Log log;
	
	@EJB(mappedName="ejb/Task")
	private Task task;
	
	@EJB(mappedName="ejb/Login")
	private Login LoginEJB;
	
	@EJB(mappedName="ejb/SecurityQuestions")
	private SecurityQuestions securityQuestionsEJB;
	
	@EJB(mappedName="ejb/TransitTask")
	private TransitTask transitTask;
	
	@EJB(mappedName="ejb/DataPolicies")
	private DataPolicies dataPoliciesEJB;
	
	@EJB(mappedName="ejb/SystemParameters")
	private SystemParameters SystemParameters;
	
	private ValidatePassword validatePassword;
	
	private Radom rad;
	
	DecimalFormat df;
	
	/**
	 * Default constructor.
	 */
	public UserEJB() {
		df= new DecimalFormat("#,###,###,###");
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.User#createClient(boolean, java.lang.Long, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	//@SuppressWarnings("unchecked")
	@Override
	public String createClient(boolean fromOutside, Long creationUser, String ip,
			String userCodeType, String userCode, String names,
			String lastNames, String phone1, String phone2, String address,
			String userCity, String userEmail, String userPassword, String dv,
			String legalTypeId,String legalId,String foreignCity,String foreignCountry) {
		//@SuppressWarnings("unused")
		long result = -1L; boolean res1=false, res2=false;
		boolean respcomplex = false;
		objectEM = new ObjectEM(em);
		String password=Encryptor.getEncrypt(userPassword);
		String messageResp = null;
		String validatePasswords = null,validateDictionary=null, validateSeq=null, validateComplex=null;
		//System.out.println("tipo codigo" + userCodeType);
		try {
			//Query q= em.createNativeQuery("select system_parameter_value from tb_system_parameter where system_parameter_id in (13,14,15,17,18,19,32)");
			//List<Object> lis=q.getResultList();
			//validatePasswords=lis.get(2)!=null?lis.get(2).toString():"SI";
			validatePasswords = SystemParameters.getParameterValueById(15L);
			if(fromOutside==true){
				int min = 0, max = 0;
				String validateIdentificationPassword=null;
				Query lengthMin =em.createQuery("select systemParameterValue from TbSystemParameter where systemParameterId in (13L)");
				Query lengthMax =em.createQuery("select systemParameterValue from TbSystemParameter where systemParameterId in (14L)");
	    		min = Integer.valueOf((String)lengthMin.getSingleResult());
				max = Integer.valueOf((String)lengthMax.getSingleResult());	
				validateIdentificationPassword = SystemParameters.getParameterValueById(17L);	    		
				validateDictionary = SystemParameters.getParameterValueById(18L);
				validateSeq = SystemParameters.getParameterValueById(19L);
				validateComplex = SystemParameters.getParameterValueById(32L);			
				/*if(lis!=null){
					min=lis.get(0)!=null?Integer.parseInt(lis.get(0).toString()):8;
					max=lis.get(1)!=null?Integer.parseInt(lis.get(1).toString()):10;
					validateIdentificationPassword=lis.get(3)!=null?lis.get(3).toString():"NO";
					validateDictionary=lis.get(4)!=null?lis.get(4).toString():"NO";
					validateSeq=lis.get(5)!=null?lis.get(5).toString():"NO";
					validateComplex=lis.get(6)!=null?lis.get(6).toString():"NO";
				}*/
				//System.out.println("min " + min);
				//System.out.println("max " + max); 
				
				boolean res=validateSize(userPassword, min, max);
				if(res){
					messageResp="La longitud de la contrase�a debe ser m�nimo de " + min +" caracteres y m�ximo de " +max;
					return messageResp;
				}
				//System.out.println("validateIdentificationPassword fromoutside: " + validateIdentificationPassword);
				//Error de seguridad la contrase�a no debe imprimirse
				/*System.out.println("userPassword fromoutside:" + userPassword);*/
				//System.out.println("userCode fromoutside:" + userCode);
				if(validateIdentificationPassword.equals("NO")){
					res2=containIdentificationCod(userPassword, userCode);
					//System.out.println("res2 de containIdentificationCod: " + res2);
					if(res2){
						messageResp="La contrase�a no puede contener su n�mero de identificaci�n";
						return messageResp;
					}
				}
				
				if(validateDictionary.equals("SI")){
					String message2;
					boolean res3=validatePasswordDictionary(userPassword);
					if(res3){
						message2="La contrase�a no est� permitida en el sistema, debido a que existe en el diccionario de contrase�as inv�lidas.";
						return message2;
					}
				}
				if(validateSeq.equals("SI")){
					String message1=validate1(userPassword);
					if(message1!=null){
						return message1;
					}
				}
				
				/**
				 * @author ablasquez
				 * Se valida la complejodad de la contrase�a
				 * debe contener m�nimo una letra may�scula, una letra minuscula, un numero y un caracter especial	
				 */
				if(validateComplex.equals("SI")){
					boolean mayus=false, minus=false, especial=false, num=false;
					int tamanio=userPassword.length();
					char letra;
					int valascii = 0;
					for (int i=0; i<tamanio; i++){
						letra = userPassword.charAt(i);
						valascii = (int)letra;
						//System.out.println("Letra: "+letra+" Acsii: "+valascii);
						if(!mayus){
							if((valascii >= 65)&& (valascii <= 90)){
								mayus = true;
							}
						}
						
						if(!minus){
							if((valascii >= 97)&& (valascii <= 122)){
								minus = true;
							}
						}
						
						if(!num){
							if((valascii >= 48)&& (valascii <= 57)){
								num = true;
							}
						}
						
						if(!especial){
							if((valascii >= 33)&& (valascii <= 47) || 
									(valascii >= 58)&& (valascii <= 64) || 
									(valascii >= 91)&& (valascii <= 96) || 
									(valascii >= 123)&& (valascii <= 126)){
								especial = true;
							}
						}
					}
					if(mayus && minus && especial && num){
						respcomplex = true;
					}else{
						messageResp = "La contrase�a debe tener una combinaci�n de may�sculas, min�sculas, n�meros y s�mbolos.";					
						respcomplex = false;
						return messageResp;
					}
					
				}else{
					//System.out.println("No requiere validar la complejidad de la contrase�a");
					respcomplex = true;
				}
				
			}else{
				//System.out.println("No requiere validar la complejidad de la contrase�a");
				respcomplex = true;
			}
			//System.out.println("respcomplex uc: "+respcomplex);
			
			if (res1 == false && res2 == false && respcomplex == true) {
				// Encrypt client password
				TbSystemUser user = new TbSystemUser();
				if (userCodeType != null) {
					if(userCodeType.equals("3")){
						userCode=userCode+dv;
					}
					
					//System.out.println("userCode: " + userCode);
					user.setUserCode(userCode);
					user.setTbCodeType(em.find(TbCodeType.class,
							Long.valueOf(userCodeType)));
					user.setUserNames(names);
					user.setUserSecondNames(lastNames);
					user.setUserEmail(userEmail);
					user.setUserPassword(password);
					user.setUserPwdDate(new Timestamp(System
							.currentTimeMillis()));
					user.setUserState(em.find(TbUserStateType.class,
							UserState.PREENROLL.getId()));
					user.setUserStateOld(em.find(TbUserStateType.class,
							UserState.PREENROLL.getId()));
					user.setUserRegistrationDate(new Timestamp(System
							.currentTimeMillis()));
					user.setUserCountIntent(0);
					user.setUserCountQuestions(0);
					user.setUserOtpState(1);
					if (fromOutside == true) {
						user.setUserFirstLogin(1);
					} else {
						user.setUserFirstLogin(0);
					}

					if (objectEM.create(user)) {// Insert the system log
						if(creationUser == null){
						   creationUser = user.getUserId();
						}	
						try {
							
							log.insertLog("Se ha creado un cliente Codigo: "
									+ user.getUserCode()
									+ "- "
									+ user.getTbCodeType()
											.getCodeTypeDescription(),
									LogReference.CLIENT, LogAction.CREATE, ip,
									creationUser);
						} catch (Exception e) {
							System.out
									.println("Se debe revisar tabla tb_log, problemas con secuencia.");
						}

						// Creating client Data
						TbUserData data = new TbUserData();

						if (userCodeType.equals("3")) { // If NIT
							data.setUserDataOfficeName(names);
							data.setUserDataContact(lastNames);
							data.setUserDataContactTypeId(Long
									.parseLong(legalTypeId));
							data.setUserDataContactId(Long.parseLong(legalId));
							data.setForeignCountry(foreignCountry);
						} else {
							data.setUserDataOfficeName(names + " " + lastNames);
							data.setUserDataContact(data
									.getUserDataOfficeName());

						}

						data.setUserDataPhone(phone1);
						if (phone2 != null && !phone2.equals("")) {
							data.setUserDataOptionalPhone(phone2);
						}

						data.setUserDataAddress(address);
						data.setTbMunicipality(em.find(TbMunicipality.class,
								Long.parseLong(userCity)));
						if (userCity.equals("0")) {
							data.setForeignCity(foreignCity);
							data.setForeignCountry(foreignCountry);
						}
						data.setTbSystemUser(user);
						data.setUserMainDependency(1L);
						if (objectEM.create(data)) {
							try {
								log.insertLog(
										"Se ha creado la dependencia al cliente C�digo: "
												+ user.getUserCode()
												+ "- "
												+ user.getTbCodeType()
														.getCodeTypeDescription(),
										LogReference.CLIENTDATA,
										LogAction.CREATE, ip, creationUser);
							} catch (Exception e) {
								System.out
										.println("Se debe revisar tabla tb_log, problemas con secuencia.");
							}

							if (validatePasswords.equals("SI")) {
								TbPasswordUser p = new TbPasswordUser();
								p.setPasswordNumber(password);
								p.setDateCreation(new Timestamp(System
										.currentTimeMillis()));
								p.setUser(user);
								if (objectEM.create(p)) {
									try {
										log.insertLog(
												"Se ha creado contrase�a en historico de contrase�as del usuario: "
														+ user.getUserCode()
														+ "- "
														+ user.getTbCodeType()
																.getCodeTypeDescription(),
												LogReference.CLIENT,
												LogAction.CREATE, ip,
												creationUser);
									} catch (Exception e) {
										System.out
												.println("Se debe revisar tabla tb_log, problemas con secuencia.");
									}

								}
							}

						} else {
							try {
								log.insertLog(
										"Error al crear la dependencia del cliente C�digo: "
												+ user.getUserCode()
												+ "- "
												+ user.getTbCodeType()
														.getCodeTypeDescription(),
										LogReference.CLIENTDATA,
										LogAction.CREATEFAIL, ip, creationUser);
							} catch (Exception e) {
								System.out
										.println("Se debe revisar tabla tb_log, problemas con secuencia.");
							}

						}

						// Se Realiza la sepracion User Role para NIT =
						// PRE-ENROLADO y NATURAL = CLIENTE
						if (role.createUserRole(user.getUserId(),
								constant.Role.PREENROLLED.getId().longValue())) {// Insert
																					// a
																					// system
																					// log
																					// of
																					// process
							try {
								log.insertLog(
										" Se ha asignado el rol PRE-ENROLADO al potencial cliente con c�digo: "
												+ user.getUserCode()
												+ "-"
												+ user.getTbCodeType()
														.getCodeTypeDescription(),
										LogReference.ROLE, LogAction.CREATE,
										ip, creationUser);
							} catch (Exception e) {
								System.out
										.println("Se debe revisar tabla tb_log, problemas con secuencia.");
							}

						} else {
							try {
								log.insertLog(
										" No se pudo asignar el rol PRE-ENROLADO al potencial cliente con c�digo: "
												+ user.getUserCode()
												+ "-"
												+ user.getTbCodeType()
														.getCodeTypeDescription(),
										LogReference.ROLE,
										LogAction.CREATEFAIL, ip, creationUser);
							} catch (Exception e) {
								System.out
										.println("Se debe revisar tabla tb_log, problemas con secuencia.");
							}

						}

						// Creating a message to track process of the client.					
						String message = "Se ha Preinscrito el Cliente en el Sistema. En espera de la documentaci�n de: "
								+ user.getUserNames();

						if (user.getTbCodeType().getCodeTypeId() == 1
								|| user.getTbCodeType().getCodeTypeId() == 2) {
							message += " " + user.getUserSecondNames();
						} else {
							message += " (Rept. Legal: "
									+ user.getUserSecondNames() + ")";
						}

						// Creating the process track, relation with the client
						Long proId = process.createProcessTrack(
								ProcessTrackType.CLIENT, user.getUserId(), ip,
								creationUser);
						Long proCliId = process.createProcessTrack(
								ProcessTrackType.MY_CLIENT_PROCESS,
								user.getUserId(), ip, creationUser);

						// If the process track was created.
						if (proId != null) {// Creating the detail of the
											// process.
							
							// Indicating that the potential client has been
							// created. Process detail type = 1
							Long detailId = process.createProcessDetail(proId,
									ProcessTrackDetailType.CLIENT_CREATION,
									message, creationUser, ip, 1,
									" No Se ha podido crear el detalle del proceso ID: "
											+ proId + ", Tipo detalle: 100.",null,null,null,null);

							TbProcessTrackDetailType dt = em.find(
									TbProcessTrackDetailType.class,
									ProcessTrackDetailType.CLIENT_CREATION
											.getId());

							// Task Creation.
							task.createTask(detailId, 0,
									new Timestamp(System.currentTimeMillis()),
									TimeUtil.calculateAds(dt.getAdsTime()), dt
											.getDetailTypePriority(), message,
									dt.getTbTaskType().getTaskTypeId(),
									creationUser, ip, null);

							// sendMail.sendMail(EmailType.CLIENT,
							// user.getUserEmail(), EmailSubject.CLIENTCREATION,
							// null);
							
							/** @author Nramirez 
							 * Se agrega generaci�n de OTP, M�todo numRad**/
							
							Long otp = this.numRad(user.getUserId());
							//System.out.println("otp: " + otp);
							
							ArrayList<String> parameters=new ArrayList<String>();
							parameters.add("#OTP="+otp);
							
							outbox.insertMessageOutbox(user.getUserId(),
									EmailProcess.PREREGISTRATION, null, null,
									null, null, null, null, null, null,
									creationUser, null, null, null, true, parameters);
							
							if (proCliId != null) {
								process.createProcessDetail(
										proCliId,
										ProcessTrackDetailType.MY_CLIENT_CREATION,
										"Ha sido Preinscrito en el sistema FacilPass.",
										creationUser, ip, 1,
										" No Se ha podido crear el detalle del proceso ID: "
												+ proId
												+ ", Tipo detalle: 600.",null,null,null,null);
							}
						}

						Query quc = em
								.createNativeQuery("SELECT su.user_id FROM Tb_system_user su WHERE su.user_id = ?1");
						quc.setParameter(1, user.getUserId());
						result = Long.parseLong(quc.getSingleResult()
								.toString());
						messageResp = "Registro Exitoso";
						// se quita porque en esta parte no se asocia una cuenta
						// // Creacion de la Cuenta-Facil-Pass Persona Natural
						//
						// if (userCodeType != "3" && result > -1L){
						// createAccount(user.getUserId() ,
						// 50000L,user.getUserId(),ip, -1L,-1L);
						// }
						try {
							if (user.getUserId() != null
									&& userCodeType.equals("1")
									|| userCodeType.equals("2")) {

								System.out
										.println("Se recibe Usuario natural aceptacion de Datos");
								if (fromOutside) {
									System.out
											.println("Se ingresa por formulario Externo "
													+ fromOutside);

									Long idPolitica = dataPoliciesEJB.getIdHTML(2L);
									if (idPolitica > 0) {
										dataPoliciesEJB.setCreatesPermission(
												user.getUserId(), idPolitica, 1L,
												ip, fromOutside, null);
									} else {
										System.out
												.println("No se encontro Politica Generada");
									}
								}

							}
						} catch (Exception e) {
							System.out.println("Error en la creacion de la Politica USEREJB");
							e.printStackTrace();
						}
						
						
						return messageResp;
					} else {
						try {
							log.insertLog("Error al crear un cliente C�digo: "
									+ user.getUserCode()
									+ "- "
									+ user.getTbCodeType()
											.getCodeTypeDescription(),
									LogReference.CLIENT, LogAction.CREATEFAIL,
									ip, creationUser);
						} catch (Exception e) {
							System.out
									.println("Se debe revisar tabla tb_log, problemas con secuencia.");
						}

						
					}
					
					
					// Creating a message to track process of the client.
					String message = "Se ha Preinscrito el Cliente en el Sistema. En espera de la documentaci�n de: " + user.getUserNames();
					//System.out.println("Antes de OTP Se ha Preinscrito el Cliente en el Sistema. En espera de la documentaci�n de: " + user.getUserNames());
					if (user.getTbCodeType().getCodeTypeId() == 1
							|| user.getTbCodeType().getCodeTypeId() == 2) {
						message += " " + user.getUserSecondNames();
					} else {
						message += " (Rept. Legal: " + user.getUserSecondNames() + ")"; 
					}
					
					
					/** @author Nramirez 
					 * Se agrega generaci�n de OTP, M�todo numRad**/
					
					Long otp = this.numRad(user.getUserId());
					//System.out.println("otp.usrsEjb: " + otp);
					
					// Creating the process track, relation with the client
					Long proId = process.createProcessTrack(ProcessTrackType.CLIENT, user.getUserId(), ip, creationUser);
					Long proCliId = process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, user.getUserId(), ip, creationUser);
					
					// If the process track was created.
					if (proId != null) {// Creating the detail of the process.
						
						// Indicating that the potential client has been created. Process detail type = 1
						Long detailId = process.createProcessDetail(proId, ProcessTrackDetailType.CLIENT_CREATION, message, creationUser, ip, 1, 
								" No Se ha podido crear el detalle del proceso ID: " + 
								proId + ", Tipo detalle: 100.",0,"","",null,null,null);
						
						TbProcessTrackDetailType dt = em.find(TbProcessTrackDetailType.class, ProcessTrackDetailType.CLIENT_CREATION.getId());
						
						// Task Creation.
						task.createTask(detailId, 0, new Timestamp(System.currentTimeMillis()), 
								TimeUtil.calculateAds(dt.getAdsTime()), dt.getDetailTypePriority(), 
								message, 
								dt.getTbTaskType().getTaskTypeId(), creationUser, ip, null);
						
						//sendMail.sendMail(EmailType.CLIENT, user.getUserEmail(), EmailSubject.CLIENTCREATION, null);

						ArrayList<String> parameters=new ArrayList<String>();
						parameters.add("#OTP="+otp);
						
						outbox.insertMessageOutbox(user.getUserId(), 
				                   EmailProcess.PREREGISTRATION,
				                   null,
				                   null,
				                   null, 
				                   null,
				                   null,
				                   null,
				                   null,
				                   null,
				                   creationUser,
					               null,
					               null,
					               null,
				                   true,
				                   parameters);
						//System.out.println("envio correo con otp ... ");
						if (proCliId != null) {
							process.createProcessDetail(proCliId,
									ProcessTrackDetailType.MY_CLIENT_CREATION, "Ha sido Preinscrito en el sistema FacilPass.", creationUser, ip, 1,
									" No Se ha podido crear el detalle del proceso ID: "
											+ proId + ", Tipo detalle: 600.",0,"","",null,null,null);
						}				
					} 
					
					Query quc = em.createNativeQuery("SELECT su.user_id FROM Tb_system_user su WHERE su.user_id = ?1");
					quc.setParameter(1, user.getUserId());
					result = Long.parseLong(quc.getSingleResult().toString());
					//System.out.println("createClient.res: "+result);
					messageResp="Registro Exitoso";
					
					// Creacion de la Cuenta-Facil-Pass Persona Natural 
					
					if (userCodeType != "3" && result > -1L){
						//System.out.println("----------***************---------------------");
						createAccount(user.getUserId() , 50000L,user.getUserId(),ip, -1L,-1L);
					}
					return messageResp;
				} else {
					try {
						log
						.insertLog("Error al crear un cliente C�digo: " + user.getUserCode() + "- "
								+ user.getTbCodeType().getCodeTypeDescription(), LogReference.CLIENT, LogAction.CREATEFAIL,
								ip, creationUser);
					} catch (Exception e) {
						System.out.println("Se debe revisar tabla tb_log, problemas con secuencia.");

					}
				}
			}
		} catch (Exception e) {
			messageResp="Error en el sistema, por favor intente de nuevo.";
			System.out.println("Error en UserEJB.createClient ");
			e.printStackTrace();
		} 
		return messageResp;
	}
	
	/** @author Nram�rez 
	 * M�todo encargado de generar el OTP para FRD124 Autogesti�n de Password*/
	@Override
	@SuppressWarnings("unchecked")
	public Long numRad(Long userId){
		Long newotp= 0L;
		
		try{
			rad = new Radom();
			  List<Long> lisOtp = new ArrayList<Long>();
			  Query otps = em.createNativeQuery("select totp.otp_id from tb_otp totp");
			  lisOtp = otps.getResultList();
			  
			  if(lisOtp != null){
				  TbSystemUser tsu = em.find(TbSystemUser.class, userId);
				  em.merge(tsu);
				  em.flush();

				  Long numOtp= rad.generate(100001L, 999999999999999L, lisOtp);
				  TbOtp otp = new TbOtp();
				  otp.setOtpId(numOtp);
				  otp.setDateOtp(new Timestamp(System.currentTimeMillis()));
				  em.persist(otp);
				  em.flush();
				  
				  newotp = otp.getOtpId();
				  
				  ReUserOtp ruo = new ReUserOtp();
				  ruo.setOtpId(otp);
				  ruo.setUserId(tsu);
				  ruo.setDateRelation(new Timestamp(System.currentTimeMillis()));
				  ruo.setStateOtp(em.find(TbStateOtp.class,0L));
				  em.merge(ruo);
				  em.flush();  
			  }
		}catch (Exception e)  {
			e.printStackTrace();	
			System.out.println("Error en UserEJB.numRad()");
		}
		return newotp;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.User#createSystemUser(java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.Long,
	 * java.lang.String)
	 */
	@Override
	public Long createSystemUser(String userCode, String names,
			String lastNames, String userMail, String userCodeType,
			Long creationUser, String ip) {
		long result = -1L;
		objectEM = new ObjectEM(em);
		try {
			TbSystemUser user = new TbSystemUser();
			user.setUserCode(userCode);
			user.setTbCodeType(em.find(TbCodeType.class, Long
					.valueOf(userCodeType)));
			user.setUserNames(names);
			user.setUserSecondNames(lastNames);
			user.setUserEmail(userMail);
			user.setUserPassword(Encryptor.getEncrypt(userCode)); // First, the user code is the password.
			user.setUserPwdDate(new Timestamp(System.currentTimeMillis()));
			user.setUserState(em.find(TbUserStateType.class,UserState.ACTIVE.getId()));
			user.setUserStateOld(em.find(TbUserStateType.class,UserState.ACTIVE.getId()));
			user.setUserFirstLogin(0);
			user.setUserOtpState(1);
			if (objectEM.create(user)) { // If user is created.
				
				/*try {
					ReUserRole reuser = new ReUserRole();
					reuser.setTbSystemUser(user);
					objectEM.create(reuser);
					
				} catch (Exception e) {
					e.printStackTrace();
				}*/
				
				// Save the log
				
				/** @author Nramirez 
				 * Se agrega generaci�n de OTP, M�todo numRad**/
				
				Long otp = this.numRad(user.getUserId());
				
				try {
					log.insertLog("Se Ha creado un Usuario del Sistema. ID: " + user.getUserId(), LogReference.USER, LogAction.CREATE,
							ip, creationUser);
				} catch (Exception e) {
					System.out.println("Se debe revisar tabla tb_log, problemas con secuencia.");
				}
				
				
				result = user.getUserId();
				
				ArrayList<String> parameters=new ArrayList<String>();
				parameters.add("#OTP="+otp);
				
				// Sending Email.
				//sendMail.sendMail(EmailType.USER, user.getUserEmail(), EmailSubject.USERCREATION, null);
				outbox.insertMessageOutbox(user.getUserId(), 
		                   EmailProcess.REGISTER_USER,
		                   null,
		                   null,
		                   null, 
		                   null,
		                   null,
		                   null,
		                   null,
		                   null,
		                   creationUser,
			               null,
			               null,
			               null,
		                   true,
		                   parameters);				
			}else{
				try {
					log.insertLog("No Se Ha podido crear el Usuario del Sistema. C�digo Ingresado: "
							+ userCode, LogReference.USER, LogAction.CREATEFAIL,  ip, creationUser);
				} catch (Exception e) {
					System.out.println("Se debe revisar tabla tb_log, problemas con secuencia.");
				}
				
			}
		} catch (Exception e) {
			System.out.println(" [ Error en UserEJB.createSystemUser ] ");
			e.printStackTrace();
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.User#createSystemUser(java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.Long,
	 * java.lang.String)
	 */
	@Override
	public Long createUserClient(String userCode, String names, String lastNames, 
		   String userPhone1, String userPhone2, String userAdress, String userCity, 
		   String userMail, String userCodeType, Long creationUser, String ip) {
		
		long result = -1L;
		objectEM = new ObjectEM(em);
		try {	
				TbSystemUser user = new TbSystemUser();
				user.setUserCode(userCode);
				user.setTbCodeType(em.find(TbCodeType.class, Long.valueOf(userCodeType)));
				user.setUserNames(names);
				user.setUserSecondNames(lastNames);
				user.setUserEmail(userMail);
				user.setUserPassword(Encryptor.getEncrypt(userCode)); // First, the user code is the password.
				user.setUserPwdDate(new Timestamp(System.currentTimeMillis()));
				user.setUserState(em.find(TbUserStateType.class,UserState.ACTIVE.getId()));
				user.setUserStateOld(em.find(TbUserStateType.class,UserState.ACTIVE.getId()));
				user.setSystemUserMasterId(creationUser);
				user.setUserFirstLogin(0);
				user.setUserOtpState(1);
				
				if (objectEM.create(user)) { // If user is created.	
					// Save the log
					
					/** @author Nramirez 
					 * Se agrega generaci�n de OTP, M�todo numRad**/
					
					Long otp = this.numRad(user.getUserId());
					//System.out.println("otp: " + otp);
					
					try {
						log.insertLog("Se Ha creado un Usuario del Sistema. ID: " + user.getUserId(), LogReference.USER, LogAction.CREATE, ip, creationUser);
						result = user.getUserId();
					} catch (Exception e) {
						System.out.println("Se debe revisar tabla tb_log, problemas con secuencia.");
					}
					
					ArrayList<String> parameters=new ArrayList<String>();
					parameters.add("#OTP="+otp);
					
					// Sending Email.
					//sendMail.sendMail(EmailType.USER, user.getUserEmail(), EmailSubject.USERCREATION, null);
					outbox.insertMessageOutbox(user.getUserId(), 
			                   EmailProcess.PREREGISTRATION,
			                   null,
			                   null,
			                   null, 
			                   null,
			                   null,
			                   null,
			                   null,
			                   null,
			                   creationUser,
				               null,
				               null,
				               null,
			                   true,
			                   parameters);
					System.out.println("envio correo con otp createUserClient()... ");
				}else{
					try {
						log.insertLog("No Se Ha podido crear el Usuario del Sistema. C�digo Ingresado: "
								+ userCode, LogReference.USER, LogAction.CREATEFAIL,  ip, creationUser);
					} catch (Exception e) {
						System.out.println("Se debe revisar tabla tb_log, problemas con secuencia.");
					}
					
				}
			} catch (Exception e) {
				System.out.println(" [ Error en UserEJB.createUserClient ] ");
				e.printStackTrace();
			}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.User#updateUser(jpa.TbSystemUser)
	 */
	@Override
	public boolean updateUser(TbSystemUser systemUser) {
		TbSystemUser u = em.find(TbSystemUser.class,systemUser.getUserId()) ;
		u.setUserNames(systemUser.getUserNames());
		u.setUserSecondNames(systemUser.getUserSecondNames());
		u.setUserState(systemUser.getUserState());
		return new ObjectEM(em).update(u);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.User#getAllUsers()
	 */
	@Override
	public List<TbSystemUser> getAllUsers() {
		List<TbSystemUser> users = new ArrayList<TbSystemUser>();
		Long idUser = null;
		try {
			Query q = em.createNativeQuery("select distinct tu.user_id,tu.user_names from tb_system_user tu "+
					"inner join re_user_role rer on tu.user_id=rer.user_id "+
					"inner join tb_role r on rer.role_id=r.role_id "+
					"order by tu.user_names");
			
			List<?> result = q.getResultList();
			
			if(result != null){
				for (Object o : result) {
					if (o != null && o instanceof Object[]) {
						Object[] obj = (Object[]) o;
						idUser = Long.parseLong(((String) obj[0]));
						TbSystemUser systemUser = em.find(TbSystemUser.class, idUser);
						users.add(systemUser);
					}
				}
			}			
			
		} catch (Exception e) {
			System.out.println(" [ Error en UserEJB.getAllUsers ] ");
			e.printStackTrace();
		}
		return users;
	}
	
	
	@Override
	public List<TbSystemUser>  getAllUsersClients() {
		List<TbSystemUser> users = new ArrayList<TbSystemUser>();
		Long idUser = null;
		try {
			Query q = em.createNativeQuery("select distinct to_char(tu.user_id),tu.user_names from tb_system_user tu "+
					"inner join re_user_role rer on tu.user_id=rer.user_id "+
					"inner join tb_role r on rer.role_id=r.role_id "+
					"where r.role_id in (2,3) "+
					"order by tu.user_names");
			
			List<?> result = q.getResultList();
			
			if(result != null){
				for (Object o : result) {
					if (o != null && o instanceof Object[]) {
						Object[] obj = (Object[]) o;
						idUser = Long.parseLong(((String) obj[0]));
						//idUser = ((BigDecimal) obj[0]).longValue();
						TbSystemUser systemUser = em.find(TbSystemUser.class, idUser);
						users.add(systemUser);
					}
				}
			}			
			
		} catch (Exception e) {
			System.out.println(" [ Error en UserEJB.getAllUsersClients ] ");
			e.printStackTrace();
		}
		return users;
	}
	

	@Override
	public boolean deleteUser(long userId, String ip, Long creationUser) {
		System.out.println("userId"+userId);
		System.out.println("ip"+ip);
		System.out.println("creationUser"+creationUser);
		boolean result = false;
		try {
			TbSystemUser systemUser = em.find(TbSystemUser.class, userId);
			if (new ObjectEM(em).delete(systemUser)) {
				log.insertLog("Eliminar Usuario | ID Usuario: " + systemUser.getUserId(), LogReference.USER,
						LogAction.DELETE, ip, creationUser);
				result = true;
			} else {
				log.insertLog("Eliminar Usuario | Error al eliminar ID Usuario: " + systemUser.getUserId(), LogReference.USER,
						LogAction.DELETEFAIL, ip, creationUser);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.User#inactivateUser(long, java.lang.String, java.lang.Long)
	 */
	@Override
	public boolean inactivateUser(long userId, String ip, Long creationUser) {
		boolean result = false;
		try {
			TbSystemUser systemUser = em.find(TbSystemUser.class, userId);
			systemUser.setUserState(em.find(TbUserStateType.class,UserState.INACTIVE.getId()));
			if (new ObjectEM(em).update(systemUser)) {
				log.insertLog("Inactivar Usuario | ID Usuario: " + systemUser.getUserId(), LogReference.USER,
						LogAction.UPDATE, ip, creationUser);
				result = true;
			} else {
				log.insertLog("Inactivar Usuario | Error al desactivar ID Usuario: " + systemUser.getUserId(), LogReference.USER,
						LogAction.UPDATEFAIL, ip, creationUser);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
		
	/**
	 * M�todo creado bloquear pasos de carril a traves de las tablas:
	 * tb_device (device_state_id=7), tag (equipmentstatus=2).
	 * @return the msg
	 * @author psanchez
	 */
	@SuppressWarnings("unused")
	@Override
	public boolean cancelSteps(Long userId, String ip, Long creationUser) {			
        TbSystemUser user = em.find(TbSystemUser.class, userId);    
		try {
			Query accounts = 
		    	 em.createNativeQuery("SELECT distinct ta.account_id FROM re_account_device red " +
						              "INNER JOIN tb_device td ON red.device_id = td.device_id  " +
						              "LEFT JOIN tag ON td.device_code = tag.equipmentobuid " +
						              "INNER JOIN tb_account ta ON red.account_id = ta.account_id " +
						              "WHERE ta.user_id = ?1 AND red.relation_state = 0 AND tag.equipmentstatus IN (1,3,4) ");
				accounts.setParameter(1, userId);
				 List<?> account = (List<?>)accounts.getResultList();
				 for (Object accountId : account){
					 if (accountId != null && accountId instanceof Object) {	
						 Query query = em.createNativeQuery("UPDATE tag SET equipmentstatus = 2 WHERE id_account = ?1 ");
				    	 query.setParameter(1, accountId);
				    	 query.executeUpdate();
					 }	
				 }
		     
		     Query deviceCodes = 
				 em.createNativeQuery("SELECT td.device_code FROM re_account_device red  " +
									  "INNER JOIN tb_device td ON red.device_id = td.device_id " +
									  "INNER JOIN tb_account ta ON red.account_id = ta.account_id " +
						              "WHERE ta.user_id = ?1 AND red.relation_state = 0 AND td.device_state_id IN (6,3,4) ");
			    deviceCodes.setParameter(1, userId);
			    List<?> deviceCode = (List<?>)deviceCodes.getResultList();
				 for (Object device : deviceCode){
					   if (device != null && device instanceof String) {
						Query query2 = em.createNativeQuery("UPDATE tb_device SET device_state_id = 7 WHERE device_code = ?1 "); // desasocio = 2
						query2.setParameter(1, device);
						query2.executeUpdate();
					   }
				  }
				 user.setUserState(em.find(TbUserStateType.class,UserState.INACTIVE_FOR_ADMIN.getId()));
				 //Se inactiva los permisos al cliente con rol INACTIVO POR ADMIN psanchez	 
				 Query q = em.createNativeQuery("UPDATE re_user_role SET role_id=?1 WHERE user_id =?2 ");
				 q.setParameter(1, constant.Role.INACTIVEUSER.getId());
				 q.setParameter(2, user.getUserId());
				 q.executeUpdate();
				 
				 Query query = em.createNativeQuery("UPDATE (SELECT rur.role_id AS new_role, rur.role_id_old AS old_role, tsu.user_id " +
											 		"FROM re_user_role rur " +
											 		"INNER JOIN tb_system_user tsu ON tsu.user_id = rur.user_id " +
											 		"WHERE tsu.system_user_master_id = ?2) SET old_role = new_role, new_role = ?1 ");
				 query.setParameter(1, constant.Role.INACTIVEUSER.getId());
				 query.setParameter(2, user.getUserId());
				 query.executeUpdate();
				 
					if (new ObjectEM(em).update(user)) {
						log.insertLog("Inactivar Usuario | ID Usuario: " + user.getUserId(), LogReference.USER,
								LogAction.UPDATE, ip, creationUser);
						//proceso administrador
						TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, userId);
						Long idPTA;
						if(pt==null){
							idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  userId, ip, creationUser);
						}
						else{
							idPTA=pt.getProcessTrackId();
						}
						Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.BANK_ACCOUNT_CLIENT, 
								"Se inactivo el usuario No."+user.getUserCode()+ " Nombre: "+ user.getUserNames()+" "+user.getUserSecondNames()+".", userId, ip, 1, "", null,null,null,null);			
						//proceso cliente
						TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,userId);
						Long idPTC;
						if(ptc==null){
							idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, userId, ip, creationUser);
						}
						else{
							idPTC=ptc.getProcessTrackId();
						}
						Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.BANK_ACCOUNT_CLIENT, 
								"Se inactivo el usuario No."+user.getUserCode()+ " Nombre: "+ user.getUserNames()+" "+user.getUserSecondNames()+".", userId, ip, 1, "", null,null,null,null);
						return true;
					} else {
						log.insertLog("Inactivar Usuario | Error al inactivar usuario ID: " + user.getUserId(), LogReference.USER,
								LogAction.UPDATEFAIL, ip, creationUser);
						return false;	
					}	 
		   }catch(NoResultException e){
			   System.out.println(" [ Error UserEJB.cancelSteps ] ");
			   e.printStackTrace();
		   }
		return false;
	}

	/**
	 * Retrieves a user, given an id
	 * @param userId User identification
	 * @return User if found, null otherwise
	 */
	public TbSystemUser getSystemUser(long userId){
		return em.find(TbSystemUser.class, userId);
	}
	
	/*
	 * (non-Javadoc)
	 * @see ejb.User#resetPassword(long, java.lang.String, java.lang.Long)
	 */
	@Override
	//Restablecer contrase�a de usuarios m�dulo Usuarios-->Administrar-->link Restablecer
	public boolean resetPassword(long userId, String ip, Long creationUser) {
		try {
			TbSystemUser systemUser = em.find(TbSystemUser.class, userId);
			
			systemUser.setUserPassword(Encryptor.getEncrypt(systemUser.getUserCode()));
			systemUser.setUserFirstLogin(4);	
			systemUser.setUserState(em.find(TbUserStateType.class, 0));
			systemUser.setUserCountIntent(0);
			systemUser.setUserLockDate(null);
			systemUser.setUserPwdExpiration(null);
			systemUser.setUserPwdDate(new Timestamp(System.currentTimeMillis()));
			@SuppressWarnings("unused")
			final String email = systemUser.getUserEmail();
			
			if (new ObjectEM(em).update(systemUser)) {
				log.insertLog("Restablecer Contrase�a | ID Usuario: " + systemUser.getUserId(), LogReference.USER,
						LogAction.UPDATE, ip, creationUser);
				
				Long newOtp = this.numRad(systemUser.getUserId());
				if(newOtp!= null){
					ArrayList<String> parameters=new ArrayList<String>();
					parameters.add("#OTP="+newOtp);
				outbox.insertMessageOutbox(systemUser.getUserId(), 
		                   EmailProcess.PASSWORD_CHANGE,
		                   null,
		                   null,
		                   null, 
		                   null,
		                   null,
		                   null,
		                   null,
		                   null,
		                   creationUser,
			               null,
			               null,
			               null,
		                   true,
		                   parameters);
				}
				/*new Thread () {
					@Override
					public void run() {
						//sendMail.sendMail(EmailType.USER, email, EmailSubject.USERRESETPASSWORD, null);						
					}
				}.start();*/
				// Es usuario de cliente maestro
				if(systemUser.getSystemUserMasterId() != null){
					/**
					 * @author ablasquez
					 * Se crea registro de proceso para cliente al para cuando se restablece la contrase�a
					 */
					TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, systemUser.getSystemUserMasterId().longValue());
					Long idPTA;
					if(pt==null){
						idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  systemUser.getSystemUserMasterId().longValue(), ip, creationUser);
					}
					else{
						idPTA=pt.getProcessTrackId();
					}
					Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.RESET_PASSWORD, 
							"La contrase�a fue restablecida satisfactoriamente para el Usuario "+systemUser.getUserNames()+" "+systemUser.getUserSecondNames(),
							creationUser, ip,1,"Error Restablecer Contrase�a",null,null,null,null);			
					//proceso cliente
					TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,systemUser.getSystemUserMasterId().longValue());
					Long idPTC;
					if(ptc==null){
						idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, systemUser.getSystemUserMasterId().longValue(), ip, creationUser);
					}
					else{
						idPTC=ptc.getProcessTrackId();
					}
					Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.RESET_PASSWORD, 
							"La contrase�a fue restablecida satisfactoriamente para el Usuario "+systemUser.getUserNames()+" "+systemUser.getUserSecondNames(), creationUser, ip, 1, "Error Restablecer Contrase�a",null,null,null,null);
					
					/****/
				}
				
			} else {
				log.insertLog("Restablecer Contrase�a | Error - ID Usuario: " + systemUser.getUserId(), LogReference.USER,
						LogAction.UPDATEFAIL, ip, creationUser);
			}
			return true;
		} catch (Exception e) {
			System.out.println("[ Ocurri� un error al restablecer la contrase�a del usuario. ]");
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.User#getCities()
	 */
	@Override
	public List<Cities> getCities() {
		List<Cities> list = new ArrayList<Cities>();
		try{
			Query q = em
					.createQuery("SELECT tm FROM TbMunicipality tm ORDER BY tm.municipalityName");
			
			for (Object obj : q.getResultList()) {
				TbMunicipality tm = (TbMunicipality) obj;
				Cities c = new Cities();
				c.setDepartment(tm.getTbDepartment().getDepartmentName().trim());
				c.setCity(tm.getMunicipalityName().trim());
				c.setCityCode(tm.getMunicipalityId() + "");
				list.add(c);
			}
		}catch (Exception e) {
			System.out.println(" [ Error en UserEJB.getCities.] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.User#validateUserCodeUK(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean validateUserCodeUK(String userCodeType, String userCode) {
		try {
			if(userCodeType.equals("NIT")){
				userCodeType = "3";
			}
			Query q = em.createQuery("SELECT su FROM TbSystemUser su WHERE su.tbCodeType.codeTypeId = ?1 AND su.userCode = ?2");
			q.setParameter(1, Long.parseLong(userCodeType));
			q.setParameter(2, userCode.trim());
			
			if(q.getResultList().size() > 0){
				return true;
			}
		} catch (Exception e) {
			System.out.println(" [ Error en UserEJB.validateUserCodeUK ] ");
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.User#validateUserEmailUK(java.lang.String)
	 */
	@Override
	public boolean validateUserEmailUK(String userEmail) {
		boolean result = false;
		try {
			Query q = em.createQuery("SELECT su FROM TbSystemUser su WHERE su.userEmail = ?1 ");
			q.setParameter(1, userEmail);
			
			if(q.getResultList().size() > 0)
				result=true;
		} catch (Exception e) {
			System.out.println(" [ Error en UserEJB.validateUserEmailUK ] ");
			e.printStackTrace();
			result=false;
		}
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.User#validateUserEmailUK(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean editUserEmailUK(long userId, String userEmail) {
		boolean result=false;
		try{
			Query query= 
				em.createNativeQuery("SELECT su.user_email FROM tb_system_user su " +
						             "WHERE su.user_id= ?1 ");
			      query.setParameter(1, userId);
		    String user_Email=(String) query.getSingleResult();
			if(userEmail.equals(user_Email)){
				result=true;
			}else{
				Query query1= 
					em.createNativeQuery("SELECT to_char(su.user_id) FROM tb_system_user su " +
										 "WHERE su.user_id <> ?1 " +
										 "AND su.user_email=?2 ");
				query1.setParameter(1, userId);
				query1.setParameter(2, userEmail.trim());
				List<Object> list= new ArrayList<Object>();
				list=query1.getResultList();
				  if(list.size()>0){
					 result=false;
				  }else{
					 result=true; 
				  }
			}
		}catch(NoResultException ex){
			System.out.println(" [ Error en UserEJB.existTagTypeU. ] ");
			 result=false;
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.User#activateUser(long)
	 */
	@Override
	public boolean activateUser(long userId) {
		boolean result = false;
		try {
			TbSystemUser systemUser = em.find(TbSystemUser.class, userId);
			systemUser.setUserState(em.find(TbUserStateType.class, UserState.ACTIVE.getId()));
			new ObjectEM(em).update(systemUser);
			result = true;
		} catch (Exception e) {
			System.out.println(" [ Error en UserEJB.activateUser ] ");
			e.printStackTrace();
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.User#validatePassword(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean validatePassword(String userPassword, String password) {
		return Encryptor.verifyPwd(userPassword, password);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.User#changePassword(long, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String changePassword(long userId, String newPassword,
			Long creationUser, String ip) {
		String message = null;		
		validatePassword= new ValidatePassword();
		boolean res1=false, res2=false, respcomplex = false;
		try {
			message=this.LoginEJB.minimumPasswordAge(userId);
			if(message.length()>0){
				return message;
			}	

			//Query q= em.createNativeQuery("select system_parameter_value from tb_system_parameter where system_parameter_id in (13,14,15,16,17,18,19,32) order by system_parameter_id"); 
			//List<Object> lis=q.getResultList();
			int min = 0, max = 0, numValidatePassword = 0;
			String validatePasswords = null, validateIdentificationPassword=null,validateDictionary=null;
			String validateSeq=null;
			String validateComplex=null;
			
			Query lengthMin =em.createQuery("select systemParameterValue from TbSystemParameter where systemParameterId in (13L)");
			Query lengthMax =em.createQuery("select systemParameterValue from TbSystemParameter where systemParameterId in (14L)");
			Query historicalPassword =em.createQuery("select systemParameterValue from TbSystemParameter where systemParameterId in (16L)");

			min = Integer.valueOf((String)lengthMin.getSingleResult());
			max = Integer.valueOf((String)lengthMax.getSingleResult());	
			validatePasswords = SystemParameters.getParameterValueById(15L);
			numValidatePassword = Integer.valueOf((String)historicalPassword.getSingleResult());
			validateIdentificationPassword = SystemParameters.getParameterValueById(17L);	    		
			validateDictionary = SystemParameters.getParameterValueById(18L);
			validateSeq = SystemParameters.getParameterValueById(19L);
			validateComplex = SystemParameters.getParameterValueById(32L);
			/*if(lis!=null){
				min=lis.get(0)!=null?Integer.parseInt(lis.get(0).toString()):8;
				max=lis.get(1)!=null?Integer.parseInt(lis.get(1).toString()):10;
				validatePasswords=lis.get(2)!=null?lis.get(2).toString():"SI";
				numValidatePassword=lis.get(3)!=null?Integer.parseInt(lis.get(3).toString()):24;
				validateIdentificationPassword=lis.get(4)!=null?lis.get(4).toString():"NO";
				validateDictionary=lis.get(5)!=null?lis.get(5).toString():"NO";
				validateSeq=lis.get(6)!=null?lis.get(6).toString():"NO";
				validateComplex=lis.get(7)!=null?lis.get(7).toString():"NO";
				System.out.println("validateComplex: "+validateComplex);
			}*/
			System.out.println("min " + min);
			System.out.println("max " + max); 
			System.out.println("validateComplex: "+validateComplex);

			boolean res=validateSize(newPassword, min, max);
			if(res){
				message="La longitud de la contrase�a debe ser m�nimo de " + min +" caracteres y m�ximo de " +max;
				return message;
			}
			
			if(validateIdentificationPassword.equals("NO")){
				res2=containIdentification(newPassword, userId);
				if(res2){
					message="La contrase�a no puede contener su n�mero de identificaci�n";
					return message;
				}
			}
			
			if(validateDictionary.equals("SI")){
				String message2;
				boolean res3=validatePasswordDictionary(newPassword);
				if(res3){
					message2="La contrase�a no est� permitida en el sistema, debido a que existe en el diccionario de contrase�as inv�lidas.";
					return message2;
				}
			}
			System.out.println("continuo, la respues fue false");
			if(validateSeq.equals("SI")){
				String message1=validate1(newPassword);
				if(message1!=null){
					return message1;
				}
			}
			
			if(validatePasswords.equals("SI")){
				String userPassword=Encryptor.getEncrypt(newPassword);
				res1=existPassword(userId, userPassword, numValidatePassword);
				if(res1){
					System.out.println("contrase�a repetida " + "res1 " + res1);
					message="La contrase�a no debe estar repetida";
					return message;
				}
			}
			
			/**
			 * @author ablasquez
			 * Se valida la complejodad de la contrase�a
			 * debe contener m�nimo una leetra may�sucula, una letra minuscula, un numero y un caracter especial	
			 */
			if(validateComplex.equals("SI")){
				boolean mayus=false, minus=false, especial=false, num=false;
				respcomplex = false;
				int tamanio=newPassword.length();
				char letra;
				int valascii = 0;
				for (int i=0; i<tamanio; i++){
					letra = newPassword.charAt(i);
					valascii = (int)letra;
					//System.out.println("Letra: "+letra+" Acsii: "+valascii);
					if(!mayus){
						if((valascii >= 65)&& (valascii <= 90)){
							mayus = true;
						}
					}
					
					if(!minus){
						if((valascii >= 97)&& (valascii <= 122)){
							minus = true;
						}
					}
					
					if(!num){
						if((valascii >= 48)&& (valascii <= 57)){
							num = true;
						}
					}
					
					if(!especial){
						if((valascii >= 33)&& (valascii <= 47) || 
								(valascii >= 58)&& (valascii <= 64) || 
								(valascii >= 91)&& (valascii <= 96) || 
								(valascii >= 123)&& (valascii <= 126)){
							especial = true;
						}
					}
				}
				if(mayus && minus && especial && num){
					respcomplex = true;
				}else{
					message = "La contrase�a debe tener una combinaci�n de may�sculas, min�sculas, n�meros y simbolos.";					
					respcomplex = false;
					return message;
				}
				
			}else{
				System.out.println("No requiere validar la complejidad de la contrase�a");
				respcomplex = true;
			}
			 System.out.println("respcomplex cp: "+respcomplex);
			if(res1==false && res2==false && respcomplex == true){
				 String userPassword1=Encryptor.getEncrypt(newPassword);

					TbSystemUser tu = em.find(TbSystemUser.class, userId);
					tu.setUserPassword(userPassword1);
					tu.setUserPwdDate(new Timestamp(System.currentTimeMillis()));
					tu.setUserFirstLogin(3);
					objectEM = new ObjectEM(em);
					if(objectEM.update(tu)){
						log.insertLog("Cambio de contrase�a | Se ha actualizado la contrase�a del usuario del sistema ID:"
										+ tu.getUserId(), LogReference.USER,
								LogAction.UPDATE, ip, creationUser);
						
						if(validatePasswords.equals("SI")){
							if(savePassword(userPassword1, tu, userId)){
								 log.insertLog("Se ha creado contrase�a en historico de contrase�as del usuario: " + tu.getUserCode() + "- "
											+ tu.getTbCodeType().getCodeTypeDescription(), LogReference.CLIENT, LogAction.CREATE,
											ip, creationUser);
							}
						}
						
						// Sending email to client/user
						outbox.insertMessageOutbox(tu.getUserId(), 
				                   EmailProcess.PASSWORD_CHANGE,
				                   null,
				                   null,
				                   null, 
				                   null,
				                   null,
				                   null,
				                   null,
				                   null,
				                   creationUser,
					               null,
					               null,
					               null,
				                   true,
							       null);
						
												
						 message="Contrase�a actualizada correctamente.";
						 
						 
						 /**
							 * @author ablasquez
							 * Se crea registro de proceso para cliente  cuando se cambia la contrase�a
							 */
							TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, userId);
							Long idPTA;
							if(pt==null){
								idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  userId, ip, creationUser);
							}
							else{
								idPTA=pt.getProcessTrackId();
							}
							Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.CHANGE_PASSWORD, 
									"El cambio de contrase�a fue realizado satisfactoriamente.",
									userId, ip,1,"Error Cambio de Contrase�a",null,null,null,null);			
							//proceso cliente
							TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,userId);
							Long idPTC;
							if(ptc==null){
								idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, userId, ip, creationUser);
							}
							else{
								idPTC=ptc.getProcessTrackId();
							}
							Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.CHANGE_PASSWORD, 
									"El cambio de contrase�a fue realizado satisfactoriamente.", userId, ip, 1, "Error Cambio de Contrase�a",null,null,null,null);
							
							/****/
					} else {
						log.insertLog("Cambio de contrase�a | No se ha podido actualizar la contrase�a del usuario del sistema ID:"
										+ tu.getUserId(), LogReference.USER,
								LogAction.UPDATEFAIL, ip, creationUser);
					}
				}	
			System.out.println("message " + message);
		  
		}catch (Exception e) {
			message="!!!Error al actualizar contrase�a.";
			e.printStackTrace();
			System.out.println( " [ Error en UserEJB.changePassword ] " );
		}
		return message;
	}
	

	@Override
	public String changePasswordReset(Long userId, String newPassword, Long creationUser, String ip) {
		String message = null;		
		boolean res1=false, res2=false, respcomplex = false;
		TbSystemUser user = em.find(TbSystemUser.class, userId);
		try {
			int min = 0, max = 0, numValidatePassword = 0;
			String validatePasswords=null;
			String validateIdentificationPassword=null;
			String validateDictionary=null;
			String validateSeq=null;
			String validateComplex=null;
			
			min = Integer.valueOf((String)SystemParameters.getParameterValueById(13L));
			max = Integer.valueOf((String)SystemParameters.getParameterValueById(14L));
			numValidatePassword = Integer.valueOf((String)SystemParameters.getParameterValueById(16L));
			validatePasswords = SystemParameters.getParameterValueById(15L);
			validateIdentificationPassword = SystemParameters.getParameterValueById(17L);	    		
			validateDictionary = SystemParameters.getParameterValueById(18L);
			validateSeq = SystemParameters.getParameterValueById(19L);
			validateComplex = SystemParameters.getParameterValueById(32L);
		
			boolean res=validateSize(newPassword, min, max);
			if(res){
				message="La longitud de la contrase�a debe ser m�nimo de " + min +" caracteres y m�ximo de " +max+".";
				return message;
			}
			
			if(validateIdentificationPassword.equals("NO")){
				res2=containIdentification(newPassword, userId);
				if(res2){
					message="La contrase�a no puede contener su n�mero de identificaci�n";
					return message;
				}
			}
			
			if(validateDictionary.equals("SI")){
				String message2;
				boolean res3=validatePasswordDictionary(newPassword);
				if(res3){
					message2="La contrase�a no est� permitida en el sistema, debido a que existe en el diccionario de contrase�as inv�lidas.";
					return message2;
				}
			}
			if(validateSeq.equals("SI")){
				String message1=validate1(newPassword);
				if(message1!=null){
					return message1;
				}
			}
			
			if(Encryptor.getEncrypt(user.getUserPassword()).equals(newPassword)){
				message="La contrase�a actual y la que esta ingresando son iguales. Verifique.";
				return message;
			}

			if(validatePasswords.equals("SI")){
				String userPassword=Encryptor.getEncrypt(newPassword);
				res1=existPassword(userId, userPassword, numValidatePassword);
				if(res1){
					message="La contrase�a no debe estar repetida.";
					return message;
				}
			}
			
			/**
			 * @author ablasquez
			 * Se valida la complejodad de la contrase�a
			 * debe contener m�nimo una leetra may�sucula, una letra minuscula, un numero y un caracter especial	
			 */
			if(validateComplex.equals("SI")){
				boolean mayus=false, minus=false, especial=false, num=false;
				respcomplex = false;
				int tamanio=newPassword.length();
				char letra;
				int valascii = 0;
				for (int i=0; i<tamanio; i++){
					letra = newPassword.charAt(i);
					valascii = (int)letra;
					//System.out.println("Letra: "+letra+" Acsii: "+valascii);
					if(!mayus){
						if((valascii >= 65)&& (valascii <= 90)){
							mayus = true;
						}
					}
					
					if(!minus){
						if((valascii >= 97)&& (valascii <= 122)){
							minus = true;
						}
					}
					
					if(!num){
						if((valascii >= 48)&& (valascii <= 57)){
							num = true;
						}
					}
					
					if(!especial){
						if((valascii >= 33)&& (valascii <= 47) || 
								(valascii >= 58)&& (valascii <= 64) || 
								(valascii >= 91)&& (valascii <= 96) || 
								(valascii >= 123)&& (valascii <= 126)){
							especial = true;
						}
					}
				}
				if(mayus && minus && especial && num){
					respcomplex = true;
				}else{
					message = "La contrase�a debe tener una combinaci�n de may�sculas, min�sculas, n�meros y simbolos.";					
					respcomplex = false;
					return message;
				}
				
			}else{
				System.out.println("No requiere validar la complejidad de la contrase�a");
				respcomplex = true;
			}
			if(res1==false && res2==false && respcomplex == true){
				 String userPassword1=Encryptor.getEncrypt(newPassword);

					TbSystemUser tu = em.find(TbSystemUser.class, userId);
					tu.setUserPassword(userPassword1);
					tu.setUserFirstLogin(3);
					tu.setUserPwdDate(new Timestamp(System.currentTimeMillis()));
					objectEM = new ObjectEM(em);
					if(objectEM.update(tu)){
						log.insertLog("Cambio de contrase�a | Se ha actualizado la contrase�a del usuario del sistema ID:"
										+ tu.getUserId(), LogReference.USER,
								LogAction.UPDATE, ip, creationUser);
						
						if(validatePasswords.equals("SI")){
							if(savePassword(userPassword1, tu, userId)){
								 log.insertLog("Se ha creado contrase�a en historico de contrase�as del usuario: " + tu.getUserCode() + "- "
											+ tu.getTbCodeType().getCodeTypeDescription(), LogReference.CLIENT, LogAction.CREATE,
											ip, creationUser);
							}
						}
						
						// Sending email to client/user
						outbox.insertMessageOutbox(tu.getUserId(), 
				                   EmailProcess.PASSWORD_CHANGE,
				                   null,
				                   null,
				                   null, 
				                   null,
				                   null,
				                   null,
				                   null,
				                   null,
				                   creationUser,
					               null,
					               null,
					               null,
				                   true,
							       null);
						
												
						 message="Contrase�a actualizada correctamente.";
						 
						 
						 /**
							 * @author ablasquez
							 * Se crea registro de proceso para cliente  cuando se cambia la contrase�a
							 */
							TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, userId);
							Long idPTA;
							if(pt==null){
								idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  userId, ip, creationUser);
							}
							else{
								idPTA=pt.getProcessTrackId();
							}
							Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.CHANGE_PASSWORD, 
									"La contrase�a fue restablecida satisfactoriamente para el usuario " + tu.getUserNames()+ " "+ tu.getUserSecondNames()+".",
									userId, ip,1,"Error Cambio de Contrase�a",null,null,null,null);			
							//proceso cliente
							TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,userId);
							Long idPTC;
							if(ptc==null){
								idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, userId, ip, creationUser);
							}
							else{
								idPTC=ptc.getProcessTrackId();
							}
							Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.CHANGE_PASSWORD, 
									"La contrase�a fue restablecida satisfactoriamente para el usuario " + tu.getUserNames()+ " "+ tu.getUserSecondNames()+".",
									userId, ip, 1, "Error Cambio de Contrase�a",null,null,null,null);
							
					} else {
						log.insertLog("Cambio de contrase�a | No se ha podido actualizar la contrase�a del usuario del sistema ID:"
										+ tu.getUserId(), LogReference.USER,
								LogAction.UPDATEFAIL, ip, creationUser);
					}
				}	
		  
		}catch (NullPointerException e) {
			message="!!!Error al actualizar contrase�a.";
			e.printStackTrace();
			System.out.println( " [ Error NullPointerException UserEJB.changePasswordReset ] " );
		}catch (Exception e) {
			message="!!!Error al actualizar contrase�a.";
			e.printStackTrace();
			System.out.println( " [ Error Exception UserEJB.changePasswordReset ] " );
		}
		return message;
	}
	
	
	/*
	 * Metodos validacion contrase�a
	 */
	
    //Metodo que valida si una contrase�a existe en el diccionario de contrase�as invalidas creadas previamente
	public boolean validatePasswordDictionary(String userPassword) {
		boolean res=false;
		try{
			Query q= em.createNativeQuery("select passwordnumber from  tb_password_dictionary where passwordnumber=?1");
			String pass=(String) q.setParameter(1,userPassword).setMaxResults(1).getSingleResult();
			if(pass!=null){
				res=true;
			}
			else{
				res=false;
			}
		}catch(NoResultException ex){
			res=false;
		}
		return res;
		
	}
	
	//Metodo que valida que la nueva contrase�a no tenga secuencias l�gicas de n�meros o letras y que no tenga caracteres repetidos consecutivamente
	public String validate1(String newPassword){
		String message = null;
		validatePassword= new ValidatePassword();
		if (validatePassword.validateContainRepeatNumPassword(newPassword.toUpperCase())){
			message="La contrase�a no debe tener numeros o caracteres repetidos de forma consecutiva.";
		}
		else if (validatePassword.validateContainSeqNumPassword(newPassword.toUpperCase())){
			message="La contrase�a no debe tener una secuencia l�gica de n�meros.";
		}
		else if (validatePassword.validateContainSeqPassword(newPassword.toUpperCase())){
			message="La contrase�a no debe tener una secuencia l�gica de letras.";
		}
		System.out.println("message1 " + message);
		return message;
	}
	
  	
	
    
    public boolean roleAdmin(Long userId){	
		long roleId= -1L;
		long roleAdmin=1L;
		boolean response=false;
		
		try{	
             Query query= em.createNativeQuery("SELECT rer.role_Id FROM re_user_role rer "+
            		 						   "INNER JOIN tb_system_user tu ON tu.user_id=rer.user_id " +
            		 						   "WHERE tu.user_id=?1 AND rer.role_id=1 "); 
             query.setParameter(1,userId);
			 roleId =Long.parseLong(query.getSingleResult().toString());		
			 
	             if(roleId != roleAdmin){
	         			response=false;
	         	  }else {
	         			response=true;
	         	  }
			}catch(NoResultException ex){
			 response=false;
		}
	  return response;
    }
	
    
	public boolean validateSize(String newPassword, int min, int max){
		boolean res=false;
		if(newPassword.length()<min || newPassword.length()>max){
			res=true;
		}
		else{
			res=false;
		}
		return res;
	}
	
	//Metodo que valida si un usuario en especifico ya tiene registrada una contrase�a usada con anterioridad
	@SuppressWarnings("unchecked")
	public boolean existPassword(long userId, String userPassword, int numValidatePassword){
	    boolean res=false;
	    List<String> listOldPasswords=null;
	    Query qq= em.createNativeQuery("select password_number from tb_password_user where user_id=?1 order by date_creation desc").setMaxResults(numValidatePassword);
	    qq.setParameter(1, userId);
	    listOldPasswords=qq.getResultList();
	    
	    for(int i=0;i<=listOldPasswords.size()-1;i++){
	    	//System.out.println("contrase�a "+ i + listOldPasswords.get(i));
	    	
	    	if(listOldPasswords.get(i).equals(userPassword)){
	    		System.out.println("se encontr� contrase�a igual");
	    		res=true;
	    		return res;
	    	}
	    }

	    System.out.println("res en existPassword " + res);
	    return res;
	}
	
	
	//Metodo que valida si una contrase�a contiene el codigo de identificacion del usuario.
	public boolean containIdentification(String newPassword, Long userId){
		boolean result=false;
	    TbSystemUser tu = em.find(TbSystemUser.class, userId);
		String identification=tu.getUserCode();
		if(newPassword.contains(identification)){
			result=true;		
		}else{
			result=false;
		}
		return result;
	}
	
	//Metodo que valida si una contrase�a contiene el codigo de identificacion del usuario.
	public boolean containIdentificationCod(String newPassword, String cod){
		if(newPassword.contains(cod)){
			return true;
		}else{
			return false;
		}
	}
	
	//Metodo que salva la contrase�a de un usuario en el historial de contrase�as
	public boolean savePassword(String userPassword, TbSystemUser tu, long userId){
		BigDecimal pass = null;
		try{
			Query q= em.createNativeQuery("select password_id from tb_password_user where user_id=?1 and password_number=?2");
			q.setParameter(1, userId);
			q.setParameter(2, userPassword);
			pass=(BigDecimal) q.getSingleResult();
			//System.out.println("respuesta " + pass);
			if(pass!=null){
				TbPasswordUser p=em.find(TbPasswordUser.class,pass.longValue());
				p.setDateCreation(new Timestamp(System.currentTimeMillis()));
				if(objectEM.update(p)){
					System.out.println("se actualizo contrase�a");
					return true;
				}
			}
		}catch(NoResultException ex){
			System.out.println("Entre al catch NoResultException");
			TbPasswordUser p= new TbPasswordUser();	
			p.setPasswordNumber(userPassword);
			p.setDateCreation(new Timestamp(System.currentTimeMillis()));
			p.setUser(tu);
			if(objectEM.create(p)){
				System.out.println("se inserto contrase�a");
				 return true;    
			}
		}
		return false;
	}
	

	/*
	 * (non-Javadoc)
	 * @see ejb.User#getAllActiveUsers()
	 */
	@Override
	public List<TbSystemUser> getAllNoneInactiveUsers() {
		List<TbSystemUser> users = new ArrayList<TbSystemUser>();
		try {
			Query q = em.createQuery("SELECT us FROM TbSystemUser us WHERE us.userState != ?1");
			q.setParameter(1, UserState.INACTIVE.getId());
			for (Object object : q.getResultList()) {
				TbSystemUser systemUser = (TbSystemUser) object;
				users.add(systemUser);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en UserEJB.getAllActiveUsers ] ");
		}
		return users;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.User#getClients()
	 */
	@Override
	public List<String> getClients(String userCode) {
		List<String> clients = new ArrayList<String>();
		try {
			Query q = em.createNativeQuery("SELECT DISTINCT tsu.user_Id FROM Tb_System_User tsu " +
					                       "INNER JOIN Re_User_Role rur ON tsu.user_id = rur.user_id " +
									       "WHERE tsu.user_State != ?1 " +
									       "AND rur.role_Id IN (?2, ?3) " +
									       "AND tsu.user_code != ?4 ");
			q.setParameter(1, UserState.INACTIVE.getId());
			q.setParameter(2, constant.Role.PREENROLLED.getId());
			q.setParameter(3, constant.Role.CLIENT.getId());
			q.setParameter(4, userCode);
			for(Object obj : q.getResultList()){
				TbSystemUser tb = em.find(TbSystemUser.class, Long.parseLong(obj.toString()));
				clients.add( tb.getUserCode() + "-" + tb.getTbCodeType().getCodeTypeAbbreviation() );
			}
		} catch (Exception e) {
			System.out.println(" [ Error en UserEJB.getClients ] ");
			e.printStackTrace();
		}
		return clients;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.User#getClients()
	 */
	@Override
	public List<String> getClients() {
		List<String> clients = new ArrayList<String>();
		try {
			Query q = em.createNativeQuery("SELECT DISTINCT tsu.user_Id FROM " +
																	  "Tb_System_User tsu INNER JOIN Re_User_Role rur " +
																	  "ON tsu.user_id = rur.user_id " +
																  "WHERE " +
																	  "tsu.user_State != ?1 AND rur.role_Id IN (?2, ?3) ");
			q.setParameter(1, UserState.INACTIVE.getId());
			q.setParameter(2, constant.Role.PREENROLLED.getId());
			q.setParameter(3, constant.Role.CLIENT.getId());
			for(Object obj : q.getResultList()){
				TbSystemUser tb = em.find(TbSystemUser.class, Long.parseLong(obj.toString()));
				clients.add( tb.getUserCode() + "-" + tb.getTbCodeType().getCodeTypeAbbreviation() );
			}
		} catch (Exception e) {
			System.out.println(" [ Error en UserEJB.getClients ] ");
			e.printStackTrace();
		}
		return clients;
	}
		

	/*
	 * (non-Javadoc)
	 * @see ejb.User#getAllActiveClient()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TbSystemUser> getAllActiveClient() {
		List<TbSystemUser> list = new ArrayList<TbSystemUser>();
		try {
			/*Query q = em.createNativeQuery("SELECT distinct pt.process_track_referenced_id " +
																"FROM tb_process_track pt " +
																"WHERE pt.process_track_state = 0 " +
																"AND pt.process_track_type_id = ?1 ");
			q.setParameter(1, ProcessTrackType.CLIENT.getId());
			for (Object obj : q.getResultList()) {
				list.add(em.find(TbSystemUser.class, Long.parseLong(obj
						.toString())));
			}*/
			
			Query q = em.createNativeQuery("Select distinct Tsu.user_id, Tsu.user_names "+ 
					"From Tb_System_User  Tsu, Re_User_Role Rur "+
					"Where Tsu.User_Id=Rur.User_Id "+
					"And Rur.Role_Id in (?1,?2) "+
					"And Tsu.User_State not in (-1) "+
					"order by Tsu.user_names");
			q.setParameter(1,constant.Role.PREENROLLED.getId());
			q.setParameter(2,constant.Role.CLIENT.getId());	
			List<Object[]> lis=q.getResultList();
			for (Object[] obj : lis) {
				//System.out.println("obj[0]" + obj[0]);
				//System.out.println("obj[1]" + obj[1]);
				list.add(em.find(TbSystemUser.class, Long.parseLong(obj[0]
						.toString())));
			}
		} catch (Exception e) {
			System.out.println(" [ Error en UserEJB.getAllActiveClient ] ");
			e.printStackTrace();
		}
		return list;
	}
	
	/*
	 * (non-Javadoc)
	 * @see ejb.User#getAllActiveClient()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TbSystemUser> getAllActiveClientMaster() {
		List<TbSystemUser> list = new ArrayList<TbSystemUser>();
		try {

			
			Query q = em.createNativeQuery("Select distinct Tsu.user_id, Tsu.user_names "+ 
					"From Tb_System_User  Tsu, Re_User_Role Rur "+
					"Where Tsu.User_Id=Rur.User_Id "+
					"And Rur.Role_Id in (?1,?2) "+
					"And Tsu.User_State not in (-1) " +
					"And tsu.system_user_master_id is null"+
					"order by Tsu.user_names");
			q.setParameter(1,constant.Role.PREENROLLED.getId());
			q.setParameter(2,constant.Role.CLIENT.getId());	
			List<Object[]> lis=q.getResultList();
			for (Object[] obj : lis) {
				System.out.println("obj[0]" + obj[0]);
				System.out.println("obj[1]" + obj[1]);
				list.add(em.find(TbSystemUser.class, Long.parseLong(obj[0]
						.toString())));
			}
		} catch (Exception e) {
			System.out.println(" [ Error en UserEJB.getAllActiveClient ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.User#getClientData(java.lang.Long)
	 */
	@Override
	public List<TbUserData> getClientData(Long userId) {
		List<TbUserData> list = new ArrayList<TbUserData>();
		try {   			
			Query q1 = 
				em.createNativeQuery("select td.user_data_id from tb_user_data td " +
									  "where td.user_data_id in " +
									  "(select min(td.user_data_id) from tb_user_data td " +
									  "left join tb_system_user tu on td.user_id=tu.user_id " +
									  "where tu.system_user_master_id= ?1 and tu.user_id in " +
									  "(select td.user_id from tb_user_data td group by td.user_id having count(*) > 1)" +
									  "and td.user_main_dependency=1) ");
			q1.setParameter(1, userId);
			BigDecimal userData=(BigDecimal) q1.getSingleResult();			
            Query query = 
            	em.createNativeQuery("UPDATE tb_user_data td SET td.user_id=?1 WHERE td.user_data_id=?2 ");
		    query.setParameter(1, userId);
            query.setParameter(2, userData);
	    	query.executeUpdate();	
	    	
	    	Query q = em.createQuery("SELECT ud FROM TbUserData ud " +
					                 "WHERE tbSystemUser.userId = ?1 " +
					                 "ORDER BY ud.userDataId ");
			q.setParameter(1, userId);
			for (Object obj : q.getResultList()) {
				list.add((TbUserData) obj);
			}
		} catch (NoResultException e) {
			Query q = em.createQuery("SELECT ud FROM TbUserData ud " +
					                 "WHERE tbSystemUser.userId = ?1 " +
					                 "ORDER BY ud.userDataId ");
			q.setParameter(1, userId);
			for (Object obj : q.getResultList()) {
				list.add((TbUserData) obj);
			}
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.User#saveClientData(long, java.lang.String, java.lang.String,
	 * java.lang.Long, java.lang.Long, java.lang.String, java.lang.String,
	 * java.lang.Long, java.lang.Long)
	 */
	@Override
	public boolean saveClientData(long idClient, String officeName,
			String address, String phone, String optionalPhone, String contactName,
			String ip, Long creationUser, Long idCity) {
		try {
			
				objectEM = new ObjectEM(em);			
				TbUserData data = new TbUserData();
				data.setUserDataAddress(address);
				data.setUserDataContact(contactName.toUpperCase());
				data.setUserDataOfficeName(officeName.toUpperCase());
				data.setUserDataPhone(phone);
				data.setTbMunicipality(em.find(TbMunicipality.class, idCity));
				data.setUserMainDependency(0L);
				TbSystemUser user = em.find(TbSystemUser.class, idClient);
				data.setTbSystemUser(em.find(TbSystemUser.class, idClient));
				
				if (optionalPhone != null) {
					if (!optionalPhone.equals("")) {
						data.setUserDataOptionalPhone(optionalPhone);
					}
				}
				
				// Saving the new Data
				if (objectEM.create(data)) {
					log.insertLog("Se ha creado la dependencia al cliente C�digo: " + user.getUserCode() + "- "
							+ user.getTbCodeType().getCodeTypeDescription(), LogReference.CLIENTDATA, LogAction.CREATE,
							ip, creationUser);
					return true;
				} else {
					log.insertLog("Error al crear la dependencia del cliente C�digo: " + user.getUserCode() + "- "
							+ user.getTbCodeType().getCodeTypeDescription(), LogReference.CLIENTDATA, LogAction.CREATEFAIL,
							ip, creationUser);
				}
		} catch (Exception e) {
			System.out.println(" [ Error en UserEJB.saveClientData ] ");
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.User#getCodeTypes()
	 */
	@Override
	public List<TbCodeType> getCodeTypes() {
		List<TbCodeType> list = new ArrayList<TbCodeType>();
		try {
			Query q = em.createQuery("SELECT ct FROM TbCodeType ct order by ct.codeTypeId");
			for (Object obj : q.getResultList()) {
				list.add((TbCodeType) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en UserEJB.getCodeTypes ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.User#getClientData(java.lang.Long, java.lang.Long)
	 */
	@Override
	public List<TbUserData> getClientData(String code, Long codeType) {
		List<TbUserData> list = new ArrayList<TbUserData>();
		try {
			Query q = em.createQuery("SELECT ud FROM TbUserData ud WHERE " +
					"ud.tbSystemUser.userCode = ?1 AND ud.tbSystemUser.tbCodeType.codeTypeId =?2");
			q.setParameter(1, code);
			q.setParameter(2, codeType);
			for (Object obj : q.getResultList()) {
				list.add((TbUserData) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en UserEJB.getClientData ] ");
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * M�todo creado para obtener el tipo de documento del cliente en sesi�n.
	 * @return the typeDocument
	 * @author psanchez
	 */
	@Override
	public String getDocumentClient(String code, long userId, String ip, long creationUser) {
		String typeDocument = null;
		try {
			Query q = em.createNativeQuery(" SELECT ct.code_type_abbreviation FROM Tb_System_User su " +
					                       " INNER JOIN Tb_Code_Type ct" +
					                       " ON su.code_type_id = ct.code_type_id" +
					                       " WHERE su.user_code= ?1 " +
					                       " AND su.user_id= ?2 ");
			                                q.setParameter(1, code);
			                                q.setParameter(2, userId);
			                                typeDocument=(String) q.getSingleResult();
		} catch (Exception e) {
			System.out.println(" [ Error en UserEJB.getDocumentClient ] ");
			e.printStackTrace();
		}
		return typeDocument;
	}
	
	
	/**
	 * M�todo creado para obtener el tipo de documento del cliente en sesi�n.
	 * @return the typeDocument
	 * @author psanchez
	 */
	@Override
	public BigDecimal getCodeTypeId(Long userId) {
		BigDecimal codeTypeId = null;
		try {
			Query q = em.createNativeQuery("SELECT ct.code_type_id FROM Tb_System_User su " +
					                       "INNER JOIN Tb_Code_Type ct " +
					                       "ON su.code_type_id = ct.code_type_id " +
					                       "WHERE su.user_id= ?1 ");
			                                q.setParameter(1, userId);
			                                codeTypeId=(BigDecimal) q.getSingleResult();
			} catch (Exception e) {
			System.out.println(" [ Error en UserEJB.getCodeTypeId ] ");
			e.printStackTrace();
		}
		return codeTypeId;
	}
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.User#modClientData(long, java.lang.String, java.lang.String,
	 * java.lang.Long, java.lang.Long, java.lang.String, java.lang.String,
	 * java.lang.Long, java.lang.Long, java.lang.Long)
	 */
	@Override
	public boolean modClientData(long idClient, String officeName,
			String address, String phone, String optionalPhone, String contactName,
			String ip, Long creationUser, Long idCity, Long idUserData) {
		try {
			
			objectEM = new ObjectEM(em);			
			TbUserData data = em.find(TbUserData.class, idUserData); 
			
			TbSystemUser user = em.find(TbSystemUser.class, idClient);
			data.setTbSystemUser(em.find(TbSystemUser.class, idClient));
			
			String message = "Se ha modificado la dependencia al cliente C�digo: " + user.getUserCode() + "- "
			+ user.getTbCodeType().getCodeTypeAbbreviation() + ". Valores Anteriores (direcci�n, contato, oficina, tel, tel Opc, ciudad id) : "
			+ data.getUserDataAddress()  + "," + data.getUserDataContact() + "," + data.getUserDataOfficeName() + "," 
			+ data.getUserDataPhone() + "," +  data.getUserDataOptionalPhone() +"," + data.getTbMunicipality().getMunicipalityId() +".";
			
			data.setUserDataAddress(address);
			data.setUserDataContact(contactName.toUpperCase());
			data.setUserDataOfficeName(officeName.toUpperCase());
			data.setUserDataPhone(phone);
			data.setTbMunicipality(em.find(TbMunicipality.class, idCity));
			
			if (optionalPhone != null) {
				if (!optionalPhone.equals("")) {
					data.setUserDataOptionalPhone(optionalPhone);
				}
			}
			
			// Updating the  Data
			if (objectEM.update(data)) {
				log.insertLog( message , LogReference.CLIENTDATA, LogAction.UPDATE, ip, creationUser);
				return true;
			} else {
				log.insertLog("Error al modificar la dependencia del cliente C�digo: " + user.getUserCode() + "- "
						+ user.getTbCodeType().getCodeTypeDescription(), LogReference.CLIENTDATA, LogAction.UPDATEFAIL,
						ip, creationUser);
			}
		} catch (Exception e) {
			System.out.println( " [ Error en UserEJB.modClientData ] " );
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public TbSystemUser getUserByCode(String code, Long codeType) {
		try {
			Query q = em.createQuery("SELECT su FROM TbSystemUser su  WHERE " +
						" su.userCode = ?1 and su.tbCodeType.codeTypeId=?2 order by su.userNames");
			q.setParameter(1, code.toString());
			q.setParameter(2, codeType);
			return (TbSystemUser) q.getSingleResult();
		} catch (NoResultException nr){
			System.out.println(" No se encontr� un usuario con c�digo: "  + code +  ". ] ");
		} catch (Exception e) {
			System.out.println(" [ Error en UserEJB.getUserByCode. ] ");
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public Long getUserFromFiltersCreateNote(String userCode, Long codeType, String userNames, String userSecondNames) {
		String qry= "";
		try {
			qry= "SELECT su.user_id FROM Tb_System_User su " +
				 "WHERE su.code_type_id = ?1 ";
			
			if(!userCode.equals("")){
				qry=qry+"AND su.user_code like '%"+userCode+"%' ";
			}
			if(!userNames.equals("")){
				qry=qry+"AND upper(su.user_names) like '%"+userNames.toUpperCase()+"%' ";
			}
			if(!userSecondNames.equals("")){
				qry=qry+"AND upper(su.user_second_names) like '%"+userSecondNames.toUpperCase()+"%' ";
			}
			Query query=em.createNativeQuery(qry).setParameter(1, codeType);
			BigDecimal userId= (BigDecimal) query.getSingleResult();
			return userId.longValue();
		}catch (NonUniqueResultException nre) {
			return -2L;
		}catch (NoResultException nr){
			return 0L;
		} catch (Exception e) {
			e.printStackTrace();
			return -1L;
		}
	}
	
	public List<TbAccountType> getTypes(){
		List<TbAccountType> types= new ArrayList<TbAccountType>();
		Query q= em.createQuery("select t from TbAccountType t order by t.nameType");
		List<?> lis= q.getResultList();
		
		for(Object o: lis){
			TbAccountType tt= (TbAccountType) o;
			types.add(tt);
		}
		
		return types;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.User#createAccount(java.lang.Long, java.lang.Long,
	 * java.lang.String)
	 */	
	/*
	 * se crea cuenta m�dulo clientes-->crear cuenta cliente del cliente - perfil adm�n 
	 */
	@Override
	public Long createAccount(Long idClient, Long type, Long creationUser, String ip, Long idBank,Long idProduct) {
		try {
			// Searching the user/client.
			TbSystemUser u = em.find(TbSystemUser.class, idClient);
			if (u != null) {
				Integer estado=u.getUserState().getUserStateId();
				if(!estado.equals(UserState.PREENROLL.getId())){
					System.out.print("usuario activo");
					// Creating the account. 
					//Query qryd = em.createNativeQuery("select p.system_parameter_value from tb_system_parameter p where p.system_parameter_id in (7)");
					Query qryd =em.createQuery("select systemParameterValue from TbSystemParameter where systemParameterId in (7L)");	
				    String defaultDistribution = (String) qryd.getSingleResult();
				    
					TbAccount a = new TbAccount();
					a.setAccountBalance(new BigDecimal(0));
					a.setAccountOpeningDate(new Timestamp(System.currentTimeMillis()));
					a.setAccountState(AccountStateType.INACTIVE.getId()); // new - Inactive
					a.setTbSystemUser(u);
					TbDistributionType t = em.find(TbDistributionType.class, new Long(defaultDistribution));
					a.setDistributionTypeId(t);
					TbAccountType tc= em.find(TbAccountType.class, type);
					a.setTbAccountType(tc);
					TbSystemUser su =em.find(TbSystemUser.class, new Long(creationUser));
					su.setSystemUserMasterId(null);
					System.out.println("type" + type);
					System.out.println("idBank" + idBank);
					System.out.println("idProduct" + idProduct);
					if(((type.longValue())!=50001) && (idBank.longValue()==1 || idBank.longValue()==2 || idBank.longValue()==23 || idBank.longValue()==52)){
						a.setPreference("PREFERENCIAL");
					}
					else{
						a.setPreference("COMUN");
					}
					TbBankAccount tba=null;
					objectEM = new ObjectEM(em);			
					if (objectEM.create(a)) {
						String nure=a.getAccountId().toString().concat(u.getUserCode().substring(u.getUserCode().length()-4));
						a.setNure(nure);
						if(idProduct!=null && idProduct>0){
							tba= em.find(TbBankAccount.class, idProduct);
							
							ReAccountBank rab= new ReAccountBank();
							rab.setAccountId(a);
							rab.setBankAccountId(tba);
							rab.setDate(new Timestamp(System.currentTimeMillis()));
							rab.setDigits(0L);
							rab.setPriority(1L);
							rab.setState_account_bank(1);
							rab.setPaymentMethodId(em.find(TbPaymentMethod.class, (long) constant.PaymentMethod.POSTPAID.getId()));
							
							objectEM.create(rab);
						}
						//se cambia estado del cliente a estado 0 estado CLIENTE
						if(u.getUserState().getUserStateId()!=2){
							System.out.println("-----Entre a cambiar estado del cliente a 0-----");
							u.setUserState(this.getState(0));
							objectEM.update(u);
						}
						
						//Query qryf = em.createNativeQuery("select p.system_parameter_value from tb_system_parameter p where p.system_parameter_id in (6)");*/
						Query qryf =em.createQuery("select systemParameterValue from TbSystemParameter where systemParameterId in (6L)");
					    String frecuency = (String) qryf.getSingleResult();
						
					    Query qry = em.createQuery("select p "+
								"from TbSystemParameter p "+
								"where p.systemParameterId in (4L,5L)");
					    
					    for(Object obj : qry.getResultList()) {
					    	TbSystemParameter parameter = (TbSystemParameter) obj;
					    	System.out.println("Nombre del Parametro: "+parameter.getSystemParameterName());
							System.out.println("valor: "+parameter.getSystemParameterValue());	    
				            TbAlarmBalance alarm = new TbAlarmBalance();
				    		alarm.setIdTipAlarm(parameter.getSystemParameterId());
							alarm.setValMinAlarm(new Long(parameter.getSystemParameterValue()));
				    		alarm.setFrequency(new Long(frecuency));
				    		alarm.setLastExecution(new Timestamp(System.currentTimeMillis()));
				    		alarm.setEstAlarm(1);
				    		alarm.setAccountId(a.getAccountId());
				    		alarm.setValminimo(0L);
				    		em.persist(alarm);
							em.flush();
					    }
					    	try{
					    		//Se envia tarea por comunicaciones informando al banco para que proceda a activar el asocio de la cuenta facilPass con la cuenta de banco								
								transitTask.createTaskAccount(TypeTask.ACCOUNT, a.getAccountId().toString());
					    	}catch (Exception e) {
					    		System.out.println("Error en la tarea, la aplicacion realiza USEREJB");
					    		log.insertLog("Crear Cuenta Cliente | No se pudo crear la cuenta.",
										LogReference.ACCOUNT, LogAction.CREATEFAIL, ip, creationUser);
								return -4L;
							}	   
					 
					    
						log.insertLog("Crear Cuenta Cliente | Se ha creado la cuenta ID: "
										+ a.getAccountId() + ", se ha asociado al cliente ID: "
										+ u.getUserId(), LogReference.ACCOUNT,
								LogAction.CREATE, ip, creationUser);
						
						//sendMail.sendMail(EmailType.CLIENT, u.getUserEmail(), EmailSubject.CLIENT_ACCOUNT_CREATION, "Estimado Usuario, \r\n  \r\nSu cuenta Facilpass " + a.getAccountId()+ " ha sido creada en el sistema. Para activarla y poder hacer uso del producto por favor ingrese a www.facilpass.com con su usuario y contrase�a y contin�e con los pasos requeridos. \r\n \r\nCordialmente, \r\nFacilpass" );
						outbox.insertMessageOutbox(u.getUserId(), 
				                  EmailProcess.ACCOUNT_CREATION,
				                  a.getAccountId(),
				                  null,
				                  null, 
				                  null,
				                  null,
				                  null,
				                  null,
				                  null,
				                  creationUser,
				                  null,
				                  null,
				                  null,
				                  true,
							       null);
							if(!role.validateUserRole(u.getUserId(), (long)constant.Role.CLIENT.getId())){
								System.out.println("Cambio rol");
								try{
									System.out.println("Entra validaci�n rol");
									Query q = em.createNativeQuery("SELECT user_role_id FROM re_user_role " +
										"WHERE user_id=?1 ");
									q.setParameter(1, u.getUserId());
									BigDecimal rol=(BigDecimal)q.getSingleResult();
									ReUserRole ru = em.find(ReUserRole.class, rol.longValue());
									if(!(ru.getTbRole().getRoleId()== 3L)){						
										TbRole r = em.find(TbRole.class, 3L);
										ru.setTbRole(r);
										em.merge(ru);
										em.flush();
									}
									System.out.println("Termina validaci�n rol cliente");
								}catch (Exception e) {
									System.out.println("No result exception con rol en cuenta");
									ReUserRole ur = new ReUserRole();
									ur.setTbRole(em.find(TbRole.class, (long) constant.Role.CLIENT.getId()));
									ur.setTbSystemUser(u);
									em.persist(ur);
									em.flush();
								}
							} 
						
							TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, idClient);
							TbProcessTrack ptClient = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, idClient);
							Long client, cclient;
							if (pt == null) {
								client=process.createProcessTrack(ProcessTrackType.CLIENT, idClient, ip, creationUser);
								cclient=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, idClient, ip, creationUser);
							
							}
							else{
								client=pt.getProcessTrackId();
								cclient=ptClient.getProcessTrackId();
							}
							process.createProcessDetail(client, ProcessTrackDetailType.CLIENT_ACCOUNT_CREATION,
									"La cuenta FacilPass No "+a.getAccountId()+" se ha creado satisfactoriamente", creationUser, ip, 1, "No se pudo registar en el proceso la creaci�n de la cuenta: "
									+ a.getAccountId() + ", ascociada al cliente ID: " + idClient + ". ID Proceso :"
									+ pt.getProcessTrackId()+".",null,null,null,null);
							process.createProcessDetail(cclient, ProcessTrackDetailType.CLIENT_ACCOUNT_CREATION,
									"La cuenta FacilPass No "+a.getAccountId()+" se ha creado satisfactoriamente", creationUser, ip, 1, "No se pudo registar en el proceso la creaci�n de la cuenta: "
									+ a.getAccountId() + ", ascociada al cliente ID: " + idClient + ". ID Proceso :"
									+ ptClient.getProcessTrackId()+".",null,null,null,null);
							
							
							
							return a.getAccountId();
					} else {
						log.insertLog("Crear Cuenta Cliente | No se pudo crear la cuenta.",
								LogReference.ACCOUNT, LogAction.CREATEFAIL, ip, creationUser);
						return -4L;
					}
				}
				else {
					System.out.println("usuario inactivo");
					log.insertLog("Crear Cuenta Cliente | El cliente se encuentra inactivo, no se puede crear la cuenta facilpass.",
									LogReference.ACCOUNT, LogAction.CREATEFAIL, ip, creationUser);
					return -2L;
				}
			} else {
				log.insertLog("Crear Cuenta Cliente | No se encontr� el cliente/usuario.",
								LogReference.ACCOUNT, LogAction.NOTFOUND, ip, creationUser);
				    return -3L;
			}
		} catch (Exception e) {
			System.out.println(" [ Error en UserEJB.createAccount ] ");
			e.printStackTrace();
			return -4L;
		}
	}
	
	
	public boolean createProcess(Long idClient, Long creationUser, String ip, String productNumber ,Long account, Long idBank){
		boolean res=false;
		TbBank tbb= em.find(TbBank.class, idBank);
		TbSystemUser tsu= em.find(TbSystemUser.class, idClient);
		tsu.setUserState(getState(2));
		em.merge(tsu);
		em.flush();
		
		TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, idClient);
		TbProcessTrack ptClient = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, idClient);
		Long client, cclient;
		if (pt == null) {
			client=process.createProcessTrack(ProcessTrackType.CLIENT, idClient, ip, creationUser);
			cclient=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, idClient, ip, creationUser);
		
		}
		else{
			client=pt.getProcessTrackId();
			cclient=ptClient.getProcessTrackId();
		}
		
		process.createProcessDetail(client, ProcessTrackDetailType.CLIENT_ACCOUNT_CREATION,
				"Se asoci� la Cuenta FacilPass No " + account +" con el Producto Bancario No " +productNumber +" del "+tbb.getBankName(), creationUser, ip, 1, "No se pudo registar en el proceso la creaci�n de la cuenta: "
				+ account + ", ascociada al cliente ID: " + idClient + ". ID Proceso :"
				+ pt.getProcessTrackId()+".",null,null,null,null);
		process.createProcessDetail(cclient, ProcessTrackDetailType.CLIENT_ACCOUNT_CREATION,
				"Se asoci� la Cuenta FacilPass No " + account +" con el Producto Bancario No " +productNumber +" del "+tbb.getBankName() , creationUser, ip, 1, "No se pudo registar en el proceso la creaci�n de la cuenta: "
				+ account + ", ascociada al cliente ID: " + idClient + ". ID Proceso :"
				+ pt.getProcessTrackId()+".",null,null,null,null);
		
		return res;
	}

	public boolean createProcessRecharge(Long idClient, Long creationUser, String ip ,Long account,Long idProduct, Long idBank, Long respuesta){
		boolean res=false;
		
		TbBank tb= em.find(TbBank.class, idBank);
		TbBankAccount tba= em.find(TbBankAccount.class, idProduct);
		
		TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, idClient);
		TbProcessTrack ptClient = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, idClient);
		Long client, cclient;
		if (pt == null) {
			client=process.createProcessTrack(ProcessTrackType.CLIENT, idClient, ip, creationUser);
			cclient=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, idClient, ip, creationUser);
		
		}
		else{
			client=pt.getProcessTrackId();
			cclient=ptClient.getProcessTrackId();
		}
		if(respuesta==0){
			process.createProcessDetail(client, ProcessTrackDetailType.INIT_RECHARGE,
					"Se realiz� la asignaci�n de recursos inicial por valor de $"+ df.format(tba!=null?tba.getInitValueRecharge():0)  +" a la Cuenta FacilPass No "+account+ ", aprobada por el "+(tb!=null?tb.getBankName():"")  , creationUser, ip, 1, "No se pudo registar la recarga inicial a la cuenta: "
					+ account,null,null,null,null );
			process.createProcessDetail(cclient, ProcessTrackDetailType.INIT_RECHARGE,
					"Se realiz� la asignaci�n de recursos inicial por valor de $"+ df.format(tba!=null?tba.getInitValueRecharge():0)  +" a su Cuenta FacilPass No "+account+ ", aprobada por el "+(tb!=null?tb.getBankName():"")  , creationUser, ip, 1, "No se pudo registar la recarga inicial a la cuenta: "
					+ account,null,null,null,null );
		}
		else{
			process.createProcessDetail(client, ProcessTrackDetailType.INIT_RECHARGE,
					"Se realiz� la asignaci�n de recursos inicial por valor de $"+ df.format(tba!=null?tba.getInitValueRecharge():0)  +" a la Cuenta FacilPass No "+account+ ", rechazada por el "+(tb!=null?tb.getBankName():"")  , creationUser, ip, 1, "No se pudo registar la recarga inicial a la cuenta: "
					+ account,null,null,null,null);
			process.createProcessDetail(cclient, ProcessTrackDetailType.INIT_RECHARGE,
					"Se realiz� la asignaci�n de recursos inicial por valor de $"+ df.format(tba!=null?tba.getInitValueRecharge():0)  +" a su Cuenta FacilPass No "+account+ ", rechazada por el "+(tb!=null?tb.getBankName():"")  , creationUser, ip, 1, "No se pudo registar la recarga inicial a la cuenta: "
					+ account,null,null,null,null);
		}
		return res;
	}
	
	
	public boolean createProcessRechargeClient(Long idClient, Long creationUser, String ip ,Long account,Long idProduct, Long idBank){
		boolean res=false;
		
		TbBank tb= em.find(TbBank.class, idBank);
		TbBankAccount tba= em.find(TbBankAccount.class, idProduct);
		
		TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, idClient);
		TbProcessTrack ptClient = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, idClient);
		Long client, cclient;
		if (pt == null) {
			client=process.createProcessTrack(ProcessTrackType.CLIENT, idClient, ip, creationUser);
			cclient=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, idClient, ip, creationUser);
		
		}
		else{
			client=pt.getProcessTrackId();
			cclient=ptClient.getProcessTrackId();
		}
		process.createProcessDetail(client, ProcessTrackDetailType.INIT_RECHARGE,
				"Se realiz� la asignaci�n de recursos inicial por valor de $"+ df.format(tba!=null?tba.getInitValueRecharge():0)  +
				" a la Cuenta FacilPass No "+account+ ", aprobada por el "+(tb!=null?tb.getBankName():"")  , creationUser, ip, 1, "No se pudo registar la recarga inicial a la cuenta: "
				+ account,null,null,null,null );
		process.createProcessDetail(cclient, ProcessTrackDetailType.INIT_RECHARGE,
				"Se realiz� la asignaci�n de recursos inicial por valor de $"+ df.format(tba!=null?tba.getInitValueRecharge():0)  +
				" a su Cuenta FacilPass No "+account+ ", aprobada por el "+(tb!=null?tb.getBankName():"")  , creationUser, ip, 1, "No se pudo registar la recarga inicial a la cuenta: "
				+ account,null,null,null,null );
		
		return res;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.User#getClientsWithAccount()
	 */
	@Override
	public List<TbSystemUser> getClientsWithAccount() {
		List<TbSystemUser> list = new ArrayList<TbSystemUser>();
		try {
			Query q = em
					.createQuery("SELECT ta.tbSystemUser FROM TbAccount ta ");
			for (Object tbSystemUser : q.getResultList()) {
				list.add((TbSystemUser) tbSystemUser);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en UserEJB.getClientsWithAccount ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see ejb.User#getInstallationUsers()
	 */
	@Override
	public List<TbSystemUser> getInstallationUsers() {
		List<TbSystemUser> list = new ArrayList<TbSystemUser>();
		try {
			Query q = em
					.createQuery("SELECT su FROM TbSystemUser su, ReUserRole re " +
							" WHERE re.tbSystemUser.userId = su.userId AND re.tbRole.roleId = ?1 ");
			q.setParameter(1, constant.Role.INSTALLER.getId());
			for (Object tbSystemUser : q.getResultList()) {
				list.add((TbSystemUser) tbSystemUser);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en UserEJB.getInstallationUsers ] ");
			e.printStackTrace();
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TbSystemUser> getUsersByName(String name) {
		Query q = em.createQuery("select su from TbSystemUser su where su.userNames = :name");
		q.setParameter("name", name);
		return q.getResultList();
	}

	/**
	 * M�todo creado para restablecer la contrase�a del cliente->administrar-->link restablecer
	 * @author psanchez
	 */
	@Override
	public boolean resetPasswordClient(Long userId, String ip, Long creationUser) {
		try {
			TbSystemUser systemUser = em.find(TbSystemUser.class, userId);

			systemUser.setUserPassword(Encryptor.getEncrypt(systemUser.getUserCode()));
			systemUser.setUserFirstLogin(4);
			if(systemUser.getUserState().getUserStateId()!=5L){//al restablecer contrase�a no debe cambiar el estado inactivo por admin
				systemUser.setUserState(em.find(TbUserStateType.class, isClientActive(systemUser)));
			}
			systemUser.setUserCountIntent(0);
			systemUser.setUserLockDate(null);
			systemUser.setUserPwdExpiration(null);
			systemUser.setUserPwdDate(new Timestamp(System.currentTimeMillis()));
			if (new ObjectEM(em).update(systemUser)) {
				log.insertLog("Restablecer Contrase�a Cliente | ID Cliente: " + systemUser.getUserId(), LogReference.CLIENT,
						LogAction.UPDATE, ip, creationUser);
				
				Long newOtp = this.numRad(systemUser.getUserId());
				if(newOtp!= null){
					ArrayList<String> parameters=new ArrayList<String>();
					parameters.add("#OTP="+newOtp);
				outbox.insertMessageOutbox(systemUser.getUserId(), 
		                   EmailProcess.PASSWORD_CHANGE,
		                   null,
		                   null,
		                   null, 
		                   null,
		                   null,
		                   null,
		                   null,
		                   null,
		                   creationUser,
		                   null,
		                   null,
		                   null,
		                   true,
		                   parameters);
				}
				TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, userId);
				Long idPTA;
				if(pt==null){
					idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  userId, ip, creationUser);
				}
				else{
					idPTA=pt.getProcessTrackId();
				}
				Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.RESET_PASSWORD, 
						"El cambio de contrase�a fue realizado satisfactoriamente.",
						creationUser, ip,1,"Error Restablecer Contrase�a",null,null,null,null);			
				//proceso cliente
				TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,userId);
				Long idPTC;
				if(ptc==null){
					idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, userId, ip, creationUser);
				}
				else{
					idPTC=ptc.getProcessTrackId();
				}
				Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.RESET_PASSWORD, 
						"El cambio de contrase�a fue realizado satisfactoriamente.", creationUser, ip, 1, 
						"Error Restablecer Contrase�a",null,null,null,null);
			} else {
				log.insertLog("Restablecer Contrase�a Cliente | Error - ID Cliente: " + systemUser.getUserId(), LogReference.CLIENT,
						LogAction.UPDATEFAIL, ip, creationUser);
			}
			return true;
		} catch (Exception e) {
			System.out.println("[ Ocurri� un error al restablecer la contrase�a del Cliente. ]");
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.User#getClientAccount(java.lang.Long)
	 */
	@Override
	public List<TbAccount> getClientAccount(Long clientId) {
		List<TbAccount> list = new ArrayList<TbAccount>();
		try {
			String query = "SELECT ac FROM TbAccount ac ";
			if (clientId != null) {
				query += " WHERE ac.tbSystemUser.userId =?1 order by ac.accountId";
			}
			else{
				query += " ORDER BY ac.tbSystemUser.userId";
			}
			
			
			Query q = em.createQuery(query);
			
			if (clientId != null) {
				q.setParameter(1, clientId);
			}
			
			for (Object obj : q.getResultList()) {
				list.add((TbAccount) obj);
			}
		} catch (Exception e) {
			System.out.println("[ Error en UserEJB.getClientAccount. ]");
			e.printStackTrace();
		}
		return list;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.User#getClientAccount(java.lang.Long)
	 */
	@Override
	public List<TbAccount> getClientAccount2(String userCode) {
		List<TbAccount> list = new ArrayList<TbAccount>();
		System.out.println("userCode" +userCode);
		try {
			String query = "select ta from TbAccount ta, TbSystemUser tu where ta.tbSystemUser.userId= tu.userId " +
					" and  tu.userCode=?1";
			
			Query q = em.createQuery(query);
			
			if (userCode != null) {
				q.setParameter(1, userCode);
			}
			
			for (Object obj : q.getResultList()) {
				list.add((TbAccount) obj);
			}
		} catch (Exception e) {
			System.out.println("[ Error en UserEJB.getClientAccount2. ]");
			e.printStackTrace();
		}
		return list;
	}
	
	
	@Override
	public boolean createNoteAccount(Long idClient, Long idAccount, String nota,Long typeId, String ip, Long user){
		boolean res=false;
		Long idPT, c;
		try{    
			
			TbSystemUser cl= em.find(TbSystemUser.class, idClient);
			if(cl!=null){
				//proceso cuenta
				TbProcessTrack pt = process.searchProcess(ProcessTrackType.ACCOUNT1, idAccount);
				if(pt==null){
					idPT=process.createProcessTrack(ProcessTrackType.ACCOUNT1, idAccount, ip, user);
				}
				else{
					idPT=pt.getProcessTrackId();
				}
				process.createProcessDetail(idPT,ProcessTrackDetailType.NOTE_CREATION , nota, user, ip, 1, "", typeId,null,null,null);
				
				//proceso cliente
				TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, idClient);
				if(ptc==null){
					c=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, idClient, ip, user);
				}
				else{
					c=ptc.getProcessTrackId();
				}
				process.createProcessDetail(c,ProcessTrackDetailType.NOTE_CREATION , nota, user, ip, 1, "", typeId,null,null,null);
				
				if(typeId==1L){
					/*sendMail.sendMail(EmailType.CLIENT, cl.getUserEmail(), EmailSubject.CLIENT_CREATION_NOTE,
					   "Buen Dia, \r\n Se ha creado nota p�blica a su cuenta FacilPass con Nro. " + idAccount+"." + " Cordialmente");*/
					outbox.insertMessageOutbox(cl.getUserId(), 
			                   EmailProcess.NOTES,
			                   idAccount,
			                   null,
			                   null, 
			                   null,
			                   null,
			                   null,
			                   null,
			                   null,
			                   user,
				               null,
				               null,
				               null,
			                   true,
						       null);
				}
				res=true;
			}
			else{
				res=false;
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
			res=false;
		}
		return res;
	}

	@Override
	public TbUserData getUserDataById(Long idClient) {
		TbUserData u = null;
	
		try{
			u= (TbUserData) em.createQuery("select u from TbUserData u where u.tbSystemUser.userId=?1 " +
					"and u.userMainDependency=1 and u.tbSystemUser.systemUserMasterId is null")
			.setParameter(1, idClient).getSingleResult();
			
		}catch(NoResultException e){u=null;}
		 catch(Exception ex){
			ex.printStackTrace();

		}
		return u;
	}
	
	

	@Override
	public boolean accountCloseRequest(Long idAccount, String reason,Long userId, String ip) {
		boolean result= false;
		try {
			String message ="";
			ReAccountBank rab = null;
			objectEM = new ObjectEM(em);	
			Long idProc = 0L;
			TbAccount account = em.find(TbAccount.class, idAccount);
			account.setAccountState(AccountStateType.PENDING_FOR_CLOSURE.getId());
			Long rabId = this.getProductByAccount(idAccount);
			if(rabId!=null){
				rab = em.find(ReAccountBank.class, rabId);
			}
			 
			if(objectEM.update(account)){
				
				Query qup = em.createNativeQuery("update Tb_Account_Close_Log set account_close_log_state=1 where account_id=?1");
				qup.setParameter(1,idAccount);
				qup.executeUpdate();
				
				TbAccountCloseLog accountCloseLog = new TbAccountCloseLog();				
				accountCloseLog.setTbAccount(account);
				accountCloseLog.setObserv(reason);
				accountCloseLog.setAccountCloseDate(new Timestamp(System.currentTimeMillis()));
				accountCloseLog.setIp(ip);
				accountCloseLog.setTbSystemUser(userId);
				accountCloseLog.setAccountCloseType(1L);
				accountCloseLog.setAccountCloseState(0L);
				
				if(objectEM.create(accountCloseLog)){
				   // se crea proceso de cuenta
					TbProcessTrack proc = process.searchProcess(ProcessTrackType.ACCOUNT1, account.getAccountId());					
					if(proc != null){
						idProc = proc.getProcessTrackId();
					} else {
						idProc = process.createProcessTrack(ProcessTrackType.ACCOUNT1, account.getAccountId(), ip, userId);
					}
					
					message = "Se ha solicitado el cierre de la Cuenta FacilPass No "+account.getAccountId();
					
					if(idProc != 0L){			
						
						Long detailId = process.createProcessDetail(idProc, ProcessTrackDetailType.REQUEST_CLOSE_ACCOUNT, message, userId, ip, 0, 
								" No Se ha podido crear el detalle del proceso ID: " + 
								idProc + ", Tipo detalle:"+ProcessTrackDetailType.REQUEST_CLOSE_ACCOUNT.getId()+".",null,null,null,null);
						
						TbProcessTrackDetailType dt = em.find(TbProcessTrackDetailType.class, ProcessTrackDetailType.REQUEST_CLOSE_ACCOUNT.getId());
						
						// Task Creation.
						task.createTask(detailId, 0, new Timestamp(System.currentTimeMillis()), 
								TimeUtil.calculateAds(dt.getAdsTime()), dt.getDetailTypePriority(), 
								message, 
								dt.getTbTaskType().getTaskTypeId(), userId, ip, null);
						
						//sendMail.sendMail(EmailType.CLIENT, account.getTbSystemUser().getUserEmail(), EmailSubject.REQUEST_CLOSE_ACCOUNT, account.getAccountId().toString());
						outbox.insertMessageOutbox(account.getTbSystemUser().getUserId(), 
				                   EmailProcess.CANCEL_ACCOUNT,
				                   account.getAccountId(),
				                   (rab!=null?rab.getBankAccountId().getBankAccountId():null),
				                   null, 
				                   null,
				                   null,
				                   null,
				                   (rab!=null?rab.getBankAccountId().getProduct().getBankId():null),
				                   null,
				                   userId,
				                   account.getTbSystemUser().getTbCodeType().getCodeTypeId(),
					               null,
					               "Cierre",
				                   true,
							       null);
						
						log.insertLog(message, LogReference.ACCOUNT_TRANSACTION, LogAction.CREATE,
								ip, userId);
						
						result= true;
					} else{
						log.insertLog(message, LogReference.ACCOUNT_TRANSACTION, LogAction.CREATEFAIL,
								ip, userId);
						result= false;
					}
					
					//Se crea el proceso de cliente
					
					TbProcessTrack 	procClient = process.searchProcess(ProcessTrackType.CLIENT, account.getTbSystemUser().getUserId());				
					if(procClient != null){
						idProc = procClient.getProcessTrackId();
					} else {
						idProc = process.createProcessTrack(ProcessTrackType.CLIENT, account.getTbSystemUser().getUserId(), ip, userId);
					}
					
					if(rab!=null){
						message = "Se solicit� la cancelaci�n de la cuenta FacilPass "+account.getAccountId()+" con producto bancario N� "+(rab!=null?rab.getBankAccountId().getBankAccountNumber():"")+
						" del banco "+(rab!=null?rab.getBankAccountId().getProduct().getBankName():"")+".";
					}else{
						message = "Se solicit� la cancelaci�n de la cuenta FacilPass "+account.getAccountId()+" sin producto bancario asociado.";
					}
					
					
					
					if(idProc != 0L){			
						
						@SuppressWarnings("unused")
						Long detailIdClient = process.createProcessDetail(idProc, ProcessTrackDetailType.REQUEST_CLOSE_ACCOUNT, message, userId, ip, 1, 
								" No Se ha podido crear el detalle del proceso ID: " + 
								idProc + ", Tipo detalle:"+ProcessTrackDetailType.REQUEST_CLOSE_ACCOUNT.getId()+".",null,null,null,null);
						
						@SuppressWarnings("unused")
						TbProcessTrackDetailType dt = em.find(TbProcessTrackDetailType.class, ProcessTrackDetailType.REQUEST_CLOSE_ACCOUNT.getId());
						
						log.insertLog(message, LogReference.CLIENT, LogAction.CREATE,
								ip, userId);
						
						result= true;
					} else{
						log.insertLog(message, LogReference.CLIENT, LogAction.CREATEFAIL,
								ip, userId);
						result= false;
					}
					
					//Se crea el proceso de para Mi Proceso
					
					TbProcessTrack 	procMiClient = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, account.getTbSystemUser().getUserId());				
					if(procMiClient != null){
						idProc = procMiClient.getProcessTrackId();
					} else {
						idProc = process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, account.getTbSystemUser().getUserId(), ip, userId);
					}
					
					if(rab!=null){
						message = "Se solicit� la cancelaci�n de la cuenta FacilPass "+account.getAccountId()+" con producto bancario N� "+(rab!=null?rab.getBankAccountId().getBankAccountNumber():"")+
						" del banco "+(rab!=null?rab.getBankAccountId().getProduct().getBankName():"")+".";
					}else{
						message = "Se solicit� la cancelaci�n de la cuenta FacilPass "+account.getAccountId()+" sin producto bancario asociado.";
					}
						
					if(idProc != 0L){			
						
						@SuppressWarnings("unused")
						Long detailIdClient = process.createProcessDetail(idProc, ProcessTrackDetailType.REQUEST_CLOSE_ACCOUNT, message, userId, ip, 1, 
								" No Se ha podido crear el detalle del proceso ID: " + 
								idProc + ", Tipo detalle:"+ProcessTrackDetailType.REQUEST_CLOSE_ACCOUNT.getId()+".",null,null,null,null);
						
						@SuppressWarnings("unused")
						TbProcessTrackDetailType dt = em.find(TbProcessTrackDetailType.class, ProcessTrackDetailType.REQUEST_CLOSE_ACCOUNT.getId());
						
						log.insertLog(message, LogReference.CLIENT, LogAction.CREATE,
								ip, userId);
						
						result= true;
					} else{
						log.insertLog(message, LogReference.CLIENT, LogAction.CREATEFAIL,
								ip, userId);
						result= false;
					}
					if(disableDevices(idAccount,userId, ip)){
						// se crea proceso de dispositivos
						message = "Se han desactivado los dispositivos relacionados con la cuenta "+account.getAccountId()+
						" del cliente "+account.getTbSystemUser().getUserNames()+" "+account.getTbSystemUser().getUserSecondNames()+".";
						
						TbProcessTrack procDev = process.searchProcess(ProcessTrackType.ACCOUNT1, account.getAccountId());					
						if(procDev != null){
							idProc = procDev.getProcessTrackId();
						} else {
							idProc = process.createProcessTrack(ProcessTrackType.ACCOUNT1, account.getAccountId(), ip, userId);
						}
						
						if(idProc != 0L){			
							
							@SuppressWarnings("unused")
							Long detailId = process.createProcessDetail(idProc, ProcessTrackDetailType.REQUEST_CLOSE_ACCOUNT, message, userId, ip, 1, 
									" No Se ha podido crear el detalle del proceso ID: " + 
									idProc + ", Tipo detalle:"+ProcessTrackDetailType.REQUEST_CLOSE_ACCOUNT.getId()+".",null,null,null,null);
							
							@SuppressWarnings("unused")
							TbProcessTrackDetailType dt = em.find(TbProcessTrackDetailType.class, ProcessTrackDetailType.REQUEST_CLOSE_ACCOUNT.getId());
							
							log.insertLog(message, LogReference.ACCOUNT_TRANSACTION, LogAction.CREATE,
									ip, userId);
							
							result= true;
						} else{
							log.insertLog(message, LogReference.ACCOUNT_TRANSACTION, LogAction.CREATEFAIL,
									ip, userId);
							result= false;
						}
						
						//Se crea el proceso de cliente
						
						TbProcessTrack 	procDevClient = process.searchProcess(ProcessTrackType.CLIENT, account.getTbSystemUser().getUserId());				
						if(procDevClient != null){
							idProc = procDevClient.getProcessTrackId();
						} else {
							idProc = process.createProcessTrack(ProcessTrackType.CLIENT, account.getTbSystemUser().getUserId(), ip, userId);
						}
						
						if(idProc != 0L){			
							
							@SuppressWarnings("unused")
							Long detailIdClient = process.createProcessDetail(idProc, ProcessTrackDetailType.REQUEST_CLOSE_ACCOUNT, message, userId, ip, 1, 
									" No Se ha podido crear el detalle del proceso ID: " + 
									idProc + ", Tipo detalle:"+ProcessTrackDetailType.REQUEST_CLOSE_ACCOUNT.getId()+".",null,null,null,null);
							
							@SuppressWarnings("unused")
							TbProcessTrackDetailType dt = em.find(TbProcessTrackDetailType.class, ProcessTrackDetailType.REQUEST_CLOSE_ACCOUNT.getId());
							
							log.insertLog(message, LogReference.CLIENT, LogAction.CREATE,
									ip, userId);
							
							result= true;
						} else{
							log.insertLog(message, LogReference.CLIENT, LogAction.CREATEFAIL,
									ip, userId);
							result= false;
						}
						
						//Se crea el proceso de para Mi Proceso
						
						TbProcessTrack 	procDevMiClient = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, account.getTbSystemUser().getUserId());				
						if(procDevMiClient != null){
							idProc = procDevMiClient.getProcessTrackId();
						} else {
							idProc = process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, account.getTbSystemUser().getUserId(), ip, userId);
						}
							
						if(idProc != 0L){			
							
							@SuppressWarnings("unused")
							Long detailIdClient = process.createProcessDetail(idProc, ProcessTrackDetailType.REQUEST_CLOSE_ACCOUNT, message, userId, ip, 1, 
									" No Se ha podido crear el detalle del proceso ID: " + 
									idProc + ", Tipo detalle:"+ProcessTrackDetailType.REQUEST_CLOSE_ACCOUNT.getId()+".",null,null,null,null);
							
							@SuppressWarnings("unused")
							TbProcessTrackDetailType dt = em.find(TbProcessTrackDetailType.class, ProcessTrackDetailType.REQUEST_CLOSE_ACCOUNT.getId());
							
							log.insertLog(message, LogReference.CLIENT, LogAction.CREATE,
									ip, userId);
							
							result= true;
						} else{
							log.insertLog(message, LogReference.CLIENT, LogAction.CREATEFAIL,
									ip, userId);
							result= false;
						}
						
					}else{
						if(device.getDevicesByAccount(idAccount).size()<=0){
							result= true;
						}else{
							result= false;
						}
					}
				}else{
					log.insertLog(message, LogReference.ACCOUNT_TRANSACTION, LogAction.CREATEFAIL,
							ip, userId);
					result= false;
				}				
			}
			
		/**
		 * se cancancelan las recargas programadas para la cuenta que se solicito la cancelacion
		 * @author ablasquez	
		 */
		  if(result){
			  this.cancelAutomaticRechage(idAccount);
		  }
		} catch (Exception e) {
			System.out.println(" [ Error en UserEJB.accountCloseRequest ] ");
			e.printStackTrace();
			return false;
		}	
		return result;
	}
	
		
	@Override
	public boolean haveAccountAssociation(Long idProduct) {
		BigDecimal id=null;
		boolean res=false;
		try{
			Query q= em.createNativeQuery("select account_bank_id from re_account_bank where bank_account_id=?1")
			.setParameter(1, idProduct);
			
			id= (BigDecimal) q.getSingleResult();
			
			if(id!=null){
				res=true;
			}
			else{
				res=false;
			}
		}catch(NoResultException ex){
			res=false;
		}
		return res;
	}
	
	
	public List<TbBankAccount> getProductsByClient(Long idBank, String codeClient) {		
		List<TbBankAccount> list = new ArrayList<TbBankAccount>();
		try {
			Query q = em.createQuery("SELECT tb FROM TbBankAccount tb where tb.product.bankId=?1 and tb.bankAccountHolderNit=?2")
			.setParameter(1, idBank).setParameter(2, codeClient);
			for (Object obj : q.getResultList()) {
				list.add((jpa.TbBankAccount) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error UserEJB.getProductsByClient. ] ");
			e.printStackTrace();
		}
		return list;
	}

	@SuppressWarnings({ "unused" })
	public List<TbAccountCloseLog> getAccountPendingClose(AccountStateType typeAccountState){
		List<TbAccountCloseLog> listAccounts = null;
		
		if (listAccounts != null){
			listAccounts.clear();
			System.out.println("limpieza de lista");
		}
			/*Query q = em.createQuery("Select tacl From TbAccountCloseLog tacl Where tacl.tbAccount.accountState = ?1 Order by tacl.tbAccount.accountId");
			q.setParameter(1, typeAccountState.getId());
			listAccounts = (List<TbAccountCloseLog>) q.getResultList();			
			System.out.println("creacion de lista");*/
		
			Query q = em.createNativeQuery("Select tacl.ACCOUNT_CLOSE_LOG_ID "+
											"From Tb_Account_Close_Log tacl "+
											"inner join tb_account ta on tacl.account_id=ta.account_id "+ 
											"Where ta.ACCOUNT_STATE_TYPE_ID=?1 "+
											"And tacl.ACCOUNT_CLOSE_LOG_TYPE=1 "+
											"And tacl.ACCOUNT_CLOSE_LOG_STATE=0 "+
											"Order by tacl.account_Id");
			q.setParameter(1, typeAccountState.getId());
			
			if(q.getResultList()!=null){
				listAccounts = new ArrayList<TbAccountCloseLog>();
				for(Object o: q.getResultList()){
					Long idLogClose = ((BigDecimal) o).longValue();
					System.out.println("idLogClose: "+idLogClose);
					TbAccountCloseLog closeLog = em.find(TbAccountCloseLog.class, idLogClose);
					if(closeLog != null){
						listAccounts.add(closeLog);
					}
				}
			}
		return listAccounts;
	}
	
	public boolean closeAccount(Long idAccount, String reason,Long userId, String ip) {
		boolean result= false;
		Long tipBnk;
		Long stateAccount;
		try {	
			//Se agrega para identificar el tipo de banco al que se encuentra asociado la cuenta. AVAL o PSE
			try {
				Query q = em.createNativeQuery(" select tb.bank_aval from re_account_bank rab "+
						"inner join tb_bank_account tba on tba.bank_account_id = rab.bank_account_id "+
						"inner join tb_bank tb on tba.product = tb.bank_id "+
						"where rab.account_id = ?1 " +
						"and rownum < 2 " +
						"order by rab.date_association desc");
				q.setParameter(1,idAccount);
				tipBnk = ((BigDecimal) q.getSingleResult()).longValue();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error en consulta tipo de banco closeAccount");
				tipBnk =1L;
			}
			try {
				Query q2 = em.createNativeQuery(" select ACCOUNT_STATE_TYPE_ID from TB_ACCOUNT where ACCOUNT_ID =  ?1");
				q2.setParameter(1,idAccount);
				stateAccount = ((BigDecimal) q2.getSingleResult()).longValue();
				System.out.println("Estado de cuenta Q " + stateAccount);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error en consulta estado de cuenta");
				stateAccount =0L;
			}
			
			System.out.println("Tipo de Cuenta: " + tipBnk);
			System.out.println("Estado de cuenta " + stateAccount);
			
			String message ="";
			objectEM = new ObjectEM(em);	
			Long idProc = 0L;
			ReAccountBank rab = null;
			Long rabId = this.getProductByAccount(idAccount);
			if(rabId!=null){
				rab = em.find(ReAccountBank.class, rabId);
			}
			TbAccount account = em.find(TbAccount.class, idAccount);
			if(rab!=null){
				account.setAccountState(AccountStateType.TO_CLOSE.getId());
			}else{
				account.setAccountState(AccountStateType.CLOSE.getId());
			}
				
			
			if(objectEM.update(account)){
				
				Query qup = em.createNativeQuery("update Tb_Account_Close_Log set account_close_log_state=1 where account_id=?1");
				qup.setParameter(1,idAccount);
				qup.executeUpdate();
				
				TbAccountCloseLog accountCloseLog = new TbAccountCloseLog();
				
				accountCloseLog.setTbAccount(account);
				accountCloseLog.setObserv(reason);
				accountCloseLog.setAccountCloseDate(new Timestamp(System.currentTimeMillis()));
				accountCloseLog.setIp(ip);
				accountCloseLog.setTbSystemUser(userId);
				accountCloseLog.setAccountCloseType(2L);
				accountCloseLog.setAccountCloseState(0L);
				
				if(objectEM.create(accountCloseLog)){
				
					TbProcessTrack proc = process.searchProcess(ProcessTrackType.ACCOUNT1, account.getAccountId());				
					if(proc != null){
						idProc = proc.getProcessTrackId();
					} else {
						idProc = process.createProcessTrack(ProcessTrackType.ACCOUNT1, account.getAccountId(), ip, userId);
					}
					if(tipBnk==0L && stateAccount==0L){			
						message = "Se ha Autorizado la disociaci�n de la Cuenta FacilPass "+account.getAccountId()+".";
					}else{
						if(rabId!=null){
							message = "Se ha Autorizado la cancelaci�n de la Cuenta FacilPass "+account.getAccountId()+".";
						}else{
							message = "Se ha Realizado la cancelaci�n de la Cuenta FacilPass "+account.getAccountId()+".";
						}
					}
					
					if(idProc != 0L){			
						
						Long detailId = process.createProcessDetail(idProc, ProcessTrackDetailType.CLOSE_ACCOUNT, message, userId, ip, 0, 
								" No Se ha podido crear el detalle del proceso ID: " + 
								idProc + ", Tipo detalle:"+ProcessTrackDetailType.CLOSE_ACCOUNT.getId()+".",null,null,null,null);
						
						TbProcessTrackDetailType dt = em.find(TbProcessTrackDetailType.class, ProcessTrackDetailType.CLOSE_ACCOUNT.getId());
						
						// Task Creation.
						task.createTask(detailId, 0, new Timestamp(System.currentTimeMillis()), 
								TimeUtil.calculateAds(dt.getAdsTime()), dt.getDetailTypePriority(), 
								message, 
								dt.getTbTaskType().getTaskTypeId(), userId, ip, null);
						
						//sendMail.sendMail(EmailType.CLIENT, account.getTbSystemUser().getUserEmail(), EmailSubject.CLOSE_ACCOUNT, account.getAccountId().toString());
						outbox.insertMessageOutbox(account.getTbSystemUser().getUserId(), 
				                   EmailProcess.CANCEL_ACCOUNT_APPLY,
				                   account.getAccountId(),
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
						log.insertLog(message, LogReference.ACCOUNT_TRANSACTION, LogAction.CREATE,
								ip, userId);
						
						result= true;
					} else{
						log.insertLog(message, LogReference.ACCOUNT_TRANSACTION, LogAction.CREATEFAIL,
								ip, userId);
						result= false;
					}
					if(intregateBalance(idAccount,userId, ip)){
						// se crea proceso de dispositivos
						message = "Se ha reintegrado el saldo de los dispositivos relacionados a la cuenta "+account.getAccountId()+
						" del cliente "+account.getTbSystemUser().getUserNames()+" "+account.getTbSystemUser().getUserSecondNames()+".";
						
						TbProcessTrack procDev = process.searchProcess(ProcessTrackType.ACCOUNT1, account.getAccountId());					
						if(procDev != null){
							idProc = procDev.getProcessTrackId();
						} else {
							idProc = process.createProcessTrack(ProcessTrackType.ACCOUNT1, account.getAccountId(), ip, userId);
						}
						
						if(idProc != 0L){			
							
							@SuppressWarnings("unused")
							Long detailId = process.createProcessDetail(idProc, ProcessTrackDetailType.CLOSE_ACCOUNT, message, userId, ip, 1, 
									" No Se ha podido crear el detalle del proceso ID: " + 
									idProc + ", Tipo detalle:"+ProcessTrackDetailType.CLOSE_ACCOUNT.getId()+".",null,null,null,null);
							
							@SuppressWarnings("unused")
							TbProcessTrackDetailType dt = em.find(TbProcessTrackDetailType.class, ProcessTrackDetailType.CLOSE_ACCOUNT.getId());
							
							log.insertLog(message, LogReference.ACCOUNT_TRANSACTION, LogAction.CREATE,
									ip, userId);
							
							result= true;
						} else{
							log.insertLog(message, LogReference.ACCOUNT_TRANSACTION, LogAction.CREATEFAIL,
									ip, userId);
							result= false;
						}
						
						//Se crea el proceso de cliente
						
						TbProcessTrack 	procDevClient = process.searchProcess(ProcessTrackType.CLIENT, account.getTbSystemUser().getUserId());				
						if(procDevClient != null){
							idProc = procDevClient.getProcessTrackId();
						} else {
							idProc = process.createProcessTrack(ProcessTrackType.CLIENT, account.getTbSystemUser().getUserId(), ip, userId);
						}
						
						if(idProc != 0L){			
							
							@SuppressWarnings("unused")
							Long detailIdClient = process.createProcessDetail(idProc, ProcessTrackDetailType.CLOSE_ACCOUNT, message, userId, ip, 1, 
									" No Se ha podido crear el detalle del proceso ID: " + 
									idProc + ", Tipo detalle:"+ProcessTrackDetailType.CLOSE_ACCOUNT.getId()+".",null,null,null,null);
							
							@SuppressWarnings("unused")
							TbProcessTrackDetailType dt = em.find(TbProcessTrackDetailType.class, ProcessTrackDetailType.CLOSE_ACCOUNT.getId());
							
							log.insertLog(message, LogReference.CLIENT, LogAction.CREATE,
									ip, userId);
							
							result= true;
						} else{
							log.insertLog(message, LogReference.CLIENT, LogAction.CREATEFAIL,
									ip, userId);
							result= false;
						}
						
						//Se crea el proceso de para Mi Proceso
						
						TbProcessTrack 	procDevMiClient = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, account.getTbSystemUser().getUserId());				
						if(procDevMiClient != null){
							idProc = procDevMiClient.getProcessTrackId();
						} else {
							idProc = process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, account.getTbSystemUser().getUserId(), ip, userId);
						}
							
						if(idProc != 0L){			
							
							@SuppressWarnings("unused")
							Long detailIdClient = process.createProcessDetail(idProc, ProcessTrackDetailType.CLOSE_ACCOUNT, message, userId, ip, 1, 
									" No Se ha podido crear el detalle del proceso ID: " + 
									idProc + ", Tipo detalle:"+ProcessTrackDetailType.CLOSE_ACCOUNT.getId()+".",null,null,null,null);
							
							@SuppressWarnings("unused")
							TbProcessTrackDetailType dt = em.find(TbProcessTrackDetailType.class, ProcessTrackDetailType.CLOSE_ACCOUNT.getId());
							
							log.insertLog(message, LogReference.CLIENT, LogAction.CREATE,
									ip, userId);
							
							result= true;
						} else{
							log.insertLog(message, LogReference.CLIENT, LogAction.CREATEFAIL,
									ip, userId);
							result= false;
						}
					}else{
						if(device.getDevicesByAccount(idAccount).size()<=0){
							//Se crea el proceso de cliente cuando no se tienen dispositivos asociados
							
							TbProcessTrack 	procDevClient = process.searchProcess(ProcessTrackType.CLIENT, account.getTbSystemUser().getUserId());				
							if(procDevClient != null){
								idProc = procDevClient.getProcessTrackId();
							} else {
								idProc = process.createProcessTrack(ProcessTrackType.CLIENT, account.getTbSystemUser().getUserId(), ip, userId);
							}
							
							if(idProc != 0L){			
								
								@SuppressWarnings("unused")
								Long detailIdClient = process.createProcessDetail(idProc, ProcessTrackDetailType.CLOSE_ACCOUNT, message, userId, ip, 1, 
										" No Se ha podido crear el detalle del proceso ID: " + 
										idProc + ", Tipo detalle:"+ProcessTrackDetailType.CLOSE_ACCOUNT.getId()+".",null,null,null,null);
								
								@SuppressWarnings("unused")
								TbProcessTrackDetailType dt = em.find(TbProcessTrackDetailType.class, ProcessTrackDetailType.CLOSE_ACCOUNT.getId());
								
								log.insertLog(message, LogReference.CLIENT, LogAction.CREATE,
										ip, userId);
								
								result= true;
							} else{
								log.insertLog(message, LogReference.CLIENT, LogAction.CREATEFAIL,
										ip, userId);
								result= false;
							}
							
							//Se crea el proceso de para Mi Proceso cuando no se tienen dispositivos asociados
							
							TbProcessTrack 	procDevMiClient = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, account.getTbSystemUser().getUserId());				
							if(procDevMiClient != null){
								idProc = procDevMiClient.getProcessTrackId();
							} else {
								idProc = process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, account.getTbSystemUser().getUserId(), ip, userId);
							}
								
							if(idProc != 0L){			
								
								@SuppressWarnings("unused")
								Long detailIdClient = process.createProcessDetail(idProc, ProcessTrackDetailType.CLOSE_ACCOUNT, message, userId, ip, 1, 
										" No Se ha podido crear el detalle del proceso ID: " + 
										idProc + ", Tipo detalle:"+ProcessTrackDetailType.CLOSE_ACCOUNT.getId()+".",null,null,null,null);
								
								@SuppressWarnings("unused")
								TbProcessTrackDetailType dt = em.find(TbProcessTrackDetailType.class, ProcessTrackDetailType.CLOSE_ACCOUNT.getId());
								
								log.insertLog(message, LogReference.CLIENT, LogAction.CREATE,
										ip, userId);
								
								result= true;
							} else{
								log.insertLog(message, LogReference.CLIENT, LogAction.CREATEFAIL,
										ip, userId);
								result= false;
							}
							result= true;
						}else{
							if(account.getDistributionTypeId().getDistributionTypeId()==1){
								result= true;
							}else{
								result= false;
							}
						}
					}
				}else{
					log.insertLog(message, LogReference.ACCOUNT_TRANSACTION, LogAction.CREATEFAIL,
							ip, userId);
					result= false;
				}		
				
			}
		} catch (Exception e) {
			System.out.println(" [ Error en UserEJB.closeAccount ] ");
			e.printStackTrace();
			return false;
		}	
		return result;
	}
	
	
	public boolean accountReactivation(Long idAccount, String reason,Long userId, String ip) {
		boolean result= false;
		try {	
			String message ="";
			objectEM = new ObjectEM(em);	
			Long idProc = 0L;
			TbAccount account = em.find(TbAccount.class, idAccount);
			account.setAccountState(AccountStateType.ACTIVE.getId());			
			if(objectEM.update(account)){
				
				Query qup = em.createNativeQuery("update Tb_Account_Close_Log set account_close_log_state=1 where account_id=?1");
				qup.setParameter(1,idAccount);
				qup.executeUpdate();
				
				TbAccountCloseLog accountCloseLog = new TbAccountCloseLog();
				
				accountCloseLog.setTbAccount(account);
				accountCloseLog.setObserv(reason);
				accountCloseLog.setAccountCloseDate(new Timestamp(System.currentTimeMillis()));
				accountCloseLog.setIp(ip);
				accountCloseLog.setTbSystemUser(userId);
				accountCloseLog.setAccountCloseType(3L);
				accountCloseLog.setAccountCloseState(0L);
				
				if(objectEM.create(accountCloseLog)){
				
					TbProcessTrack proc = process.searchProcess(ProcessTrackType.ACCOUNT1, account.getAccountId());				
					if(proc != null){
						idProc = proc.getProcessTrackId();
					} else {
						idProc = process.createProcessTrack(ProcessTrackType.ACCOUNT1, account.getAccountId(), ip, userId);
					}
					
					message = "Se ha reactivado la Cuenta "+account.getAccountId()+".";
					
					if(idProc != 0L){			
						
						Long detailId = process.createProcessDetail(idProc, ProcessTrackDetailType.REACTIVATION_ACCOUNT, message, userId, ip, 0, 
								" No Se ha podido crear el detalle del proceso ID: " + 
								idProc + ", Tipo detalle:"+ProcessTrackDetailType.REACTIVATION_ACCOUNT.getId()+".",null,null,null,null);
						
						TbProcessTrackDetailType dt = em.find(TbProcessTrackDetailType.class, ProcessTrackDetailType.REACTIVATION_ACCOUNT.getId());
						
						// Task Creation.
						task.createTask(detailId, 0, new Timestamp(System.currentTimeMillis()), 
								TimeUtil.calculateAds(dt.getAdsTime()), dt.getDetailTypePriority(), 
								message, 
								dt.getTbTaskType().getTaskTypeId(), userId, ip, null);
						
						//sendMail.sendMail(EmailType.CLIENT, account.getTbSystemUser().getUserEmail(), EmailSubject.REACTIVATION_ACCOUNT, account.getAccountId().toString());
						outbox.insertMessageOutbox(account.getTbSystemUser().getUserId(), 
				                   EmailProcess.REACTIVATION_ACCOUNT,
				                   account.getAccountId(),
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
						log.insertLog(message, LogReference.ACCOUNT_TRANSACTION, LogAction.CREATE,
								ip, userId);
						
						result= true;
					} else{
						log.insertLog(message, LogReference.ACCOUNT_TRANSACTION, LogAction.CREATEFAIL,
								ip, userId);
						result= false;
					}
					if(activeDevices(idAccount,userId, ip)){
						// se crea proceso de dispositivos
						message = "Se han reactivado los dispositivos relacionados con la cuenta "+account.getAccountId()+
						" del cliente "+account.getTbSystemUser().getUserNames()+" "+account.getTbSystemUser().getUserSecondNames()+".";
						
						TbProcessTrack procDev = process.searchProcess(ProcessTrackType.ACCOUNT1, account.getAccountId());					
						if(procDev != null){
							idProc = procDev.getProcessTrackId();
						} else {
							idProc = process.createProcessTrack(ProcessTrackType.ACCOUNT1, account.getAccountId(), ip, userId);
						}
						
						if(idProc != 0L){			
							
							@SuppressWarnings("unused")
							Long detailId = process.createProcessDetail(idProc, ProcessTrackDetailType.REACTIVATION_ACCOUNT, message, userId, ip, 1, 
									" No Se ha podido crear el detalle del proceso ID: " + 
									idProc + ", Tipo detalle:"+ProcessTrackDetailType.REACTIVATION_ACCOUNT.getId()+".",null,null,null,null);
							
							@SuppressWarnings("unused")
							TbProcessTrackDetailType dt = em.find(TbProcessTrackDetailType.class, ProcessTrackDetailType.REACTIVATION_ACCOUNT.getId());
							
							log.insertLog(message, LogReference.ACCOUNT_TRANSACTION, LogAction.CREATE,
									ip, userId);
							
							result= true;
						} else{
							log.insertLog(message, LogReference.ACCOUNT_TRANSACTION, LogAction.CREATEFAIL,
									ip, userId);
							result= false;
						}
						
						//Se crea el proceso de cliente
						
						TbProcessTrack 	procDevClient = process.searchProcess(ProcessTrackType.CLIENT, account.getTbSystemUser().getUserId());				
						if(procDevClient != null){
							idProc = procDevClient.getProcessTrackId();
						} else {
							idProc = process.createProcessTrack(ProcessTrackType.CLIENT, account.getTbSystemUser().getUserId(), ip, userId);
						}
						
						if(idProc != 0L){			
							
							@SuppressWarnings("unused")
							Long detailIdClient = process.createProcessDetail(idProc, ProcessTrackDetailType.REACTIVATION_ACCOUNT, message, userId, ip, 1, 
									" No Se ha podido crear el detalle del proceso ID: " + 
									idProc + ", Tipo detalle:"+ProcessTrackDetailType.REACTIVATION_ACCOUNT.getId()+".",null,null,null,null);
							
							@SuppressWarnings("unused")
							TbProcessTrackDetailType dt = em.find(TbProcessTrackDetailType.class, ProcessTrackDetailType.REACTIVATION_ACCOUNT.getId());
							
							log.insertLog(message, LogReference.CLIENT, LogAction.CREATE,
									ip, userId);
							
							result= true;
						} else{
							log.insertLog(message, LogReference.CLIENT, LogAction.CREATEFAIL,
									ip, userId);
							result= false;
						}
						
						//Se crea el proceso de para Mi Proceso
						
						TbProcessTrack 	procDevMiClient = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, account.getTbSystemUser().getUserId());				
						if(procDevMiClient != null){
							idProc = procDevMiClient.getProcessTrackId();
						} else {
							idProc = process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, account.getTbSystemUser().getUserId(), ip, userId);
						}
							
						if(idProc != 0L){			
							
							@SuppressWarnings("unused")
							Long detailIdClient = process.createProcessDetail(idProc, ProcessTrackDetailType.REACTIVATION_ACCOUNT, message, userId, ip, 1, 
									" No Se ha podido crear el detalle del proceso ID: " + 
									idProc + ", Tipo detalle:"+ProcessTrackDetailType.REACTIVATION_ACCOUNT.getId()+".",null,null,null,null);
							
							@SuppressWarnings("unused")
							TbProcessTrackDetailType dt = em.find(TbProcessTrackDetailType.class, ProcessTrackDetailType.REACTIVATION_ACCOUNT.getId());
							
							log.insertLog(message, LogReference.CLIENT, LogAction.CREATE,
									ip, userId);
							
							result= true;
						} else{
							log.insertLog(message, LogReference.CLIENT, LogAction.CREATEFAIL,
									ip, userId);
							result= false;
						}
						
						
						
						//Se crea el proceso de cliente de la reactivacion de la cuenta
						
						message = "Se ha reactivado la Cuenta "+account.getAccountId()+".";
						
						procDevClient = process.searchProcess(ProcessTrackType.CLIENT, account.getTbSystemUser().getUserId());				
						if(procDevClient != null){
							idProc = procDevClient.getProcessTrackId();
						} else {
							idProc = process.createProcessTrack(ProcessTrackType.CLIENT, account.getTbSystemUser().getUserId(), ip, userId);
						}
						
						if(idProc != 0L){			
							
							@SuppressWarnings("unused")
							Long detailIdClient = process.createProcessDetail(idProc, ProcessTrackDetailType.REACTIVATION_ACCOUNT, message, userId, ip, 1, 
									" No Se ha podido crear el detalle del proceso ID: " + 
									idProc + ", Tipo detalle:"+ProcessTrackDetailType.REACTIVATION_ACCOUNT.getId()+".",null,null,null,null);
							
							@SuppressWarnings("unused")
							TbProcessTrackDetailType dt = em.find(TbProcessTrackDetailType.class, ProcessTrackDetailType.REACTIVATION_ACCOUNT.getId());
							
							log.insertLog(message, LogReference.CLIENT, LogAction.CREATE,
									ip, userId);
							
							result= true;
						} else{
							log.insertLog(message, LogReference.CLIENT, LogAction.CREATEFAIL,
									ip, userId);
							result= false;
						}
						
						//Se crea el proceso de para Mi Proceso para la reactivacion de la cuenta
						
						procDevMiClient = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, account.getTbSystemUser().getUserId());				
						if(procDevMiClient != null){
							idProc = procDevMiClient.getProcessTrackId();
						} else {
							idProc = process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, account.getTbSystemUser().getUserId(), ip, userId);
						}
							
						if(idProc != 0L){			
							
							@SuppressWarnings("unused")
							Long detailIdClient = process.createProcessDetail(idProc, ProcessTrackDetailType.REACTIVATION_ACCOUNT, message, userId, ip, 1, 
									" No Se ha podido crear el detalle del proceso ID: " + 
									idProc + ", Tipo detalle:"+ProcessTrackDetailType.REACTIVATION_ACCOUNT.getId()+".",null,null,null,null);
							
							@SuppressWarnings("unused")
							TbProcessTrackDetailType dt = em.find(TbProcessTrackDetailType.class, ProcessTrackDetailType.REACTIVATION_ACCOUNT.getId());
							
							log.insertLog(message, LogReference.CLIENT, LogAction.CREATE,
									ip, userId);
							
							result= true;
						} else{
							log.insertLog(message, LogReference.CLIENT, LogAction.CREATEFAIL,
									ip, userId);
							result= false;
						}
					}else{
						if(device.getDevicesHistoryByAccount(idAccount).size()<=0){
							//Se crea el proceso de cliente cuando no existe dispositivos en la cuenta
							
							TbProcessTrack 	procDevClient = process.searchProcess(ProcessTrackType.CLIENT, account.getTbSystemUser().getUserId());				
							if(procDevClient != null){
								idProc = procDevClient.getProcessTrackId();
							} else {
								idProc = process.createProcessTrack(ProcessTrackType.CLIENT, account.getTbSystemUser().getUserId(), ip, userId);
							}
							
							if(idProc != 0L){			
								
								@SuppressWarnings("unused")
								Long detailIdClient = process.createProcessDetail(idProc, ProcessTrackDetailType.REACTIVATION_ACCOUNT, message, userId, ip, 1, 
										" No Se ha podido crear el detalle del proceso ID: " + 
										idProc + ", Tipo detalle:"+ProcessTrackDetailType.REACTIVATION_ACCOUNT.getId()+".",null,null,null,null);
								
								@SuppressWarnings("unused")
								TbProcessTrackDetailType dt = em.find(TbProcessTrackDetailType.class, ProcessTrackDetailType.REACTIVATION_ACCOUNT.getId());
								
								log.insertLog(message, LogReference.CLIENT, LogAction.CREATE,
										ip, userId);
								
								result= true;
							} else{
								log.insertLog(message, LogReference.CLIENT, LogAction.CREATEFAIL,
										ip, userId);
								result= false;
							}
							
							//Se crea el proceso de para Mi Proceso cuando no existe dispositivos en la cuenta
							
							TbProcessTrack 	procDevMiClient = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, account.getTbSystemUser().getUserId());				
							if(procDevMiClient != null){
								idProc = procDevMiClient.getProcessTrackId();
							} else {
								idProc = process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, account.getTbSystemUser().getUserId(), ip, userId);
							}
								
							if(idProc != 0L){			
								
								@SuppressWarnings("unused")
								Long detailIdClient = process.createProcessDetail(idProc, ProcessTrackDetailType.REACTIVATION_ACCOUNT, message, userId, ip, 1, 
										" No Se ha podido crear el detalle del proceso ID: " + 
										idProc + ", Tipo detalle:"+ProcessTrackDetailType.REACTIVATION_ACCOUNT.getId()+".",null,null,null,null);
								
								@SuppressWarnings("unused")
								TbProcessTrackDetailType dt = em.find(TbProcessTrackDetailType.class, ProcessTrackDetailType.REACTIVATION_ACCOUNT.getId());
								
								log.insertLog(message, LogReference.CLIENT, LogAction.CREATE,
										ip, userId);
								
								result= true;
							} else{
								log.insertLog(message, LogReference.CLIENT, LogAction.CREATEFAIL,
										ip, userId);
								result= false;
							}
							result= true;
						}else{
							result= false;
						}
					}
				}else{
					log.insertLog(message, LogReference.ACCOUNT_TRANSACTION, LogAction.CREATEFAIL,
							ip, userId);
					result= false;
				}				
			}
		} catch (Exception e) {
			System.out.println(" [ Error en UserEJB.accountReactivation ] ");
			e.printStackTrace();
			return false;
		}	
		return result;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public String changePasswordFirst(long userId, String newPassword,
			Long creationUser, String ip) {
		String message = "";
		validatePassword= new ValidatePassword();
		boolean res1=false, res2=false;
		boolean respcomplex = false;
		try {			
			//Query q= em.createNativeQuery("select system_parameter_value from tb_system_parameter where system_parameter_id in (13,14,15,16,17,18,19,32)"); 
			//List<Object> lis=q.getResultList();
			int min = 0, max = 0, numValidatePassword = 0;
			String validatePasswords = null, validateIdentificationPassword=null,validateDictionary=null;
			String validateSeq=null;
			String validateComplex = null;
			
			Query lengthMin =em.createQuery("select systemParameterValue from TbSystemParameter where systemParameterId in (13L)");
			Query lengthMax =em.createQuery("select systemParameterValue from TbSystemParameter where systemParameterId in (14L)");
			Query historicalPassword =em.createQuery("select systemParameterValue from TbSystemParameter where systemParameterId in (16L)");

			min = Integer.valueOf((String)lengthMin.getSingleResult());
			max = Integer.valueOf((String)lengthMax.getSingleResult());	
			validatePasswords = SystemParameters.getParameterValueById(15L);
			numValidatePassword = Integer.valueOf((String)historicalPassword.getSingleResult());
			validateIdentificationPassword = SystemParameters.getParameterValueById(17L);	    		
			validateDictionary = SystemParameters.getParameterValueById(18L);
			validateSeq = SystemParameters.getParameterValueById(19L);
			validateComplex = SystemParameters.getParameterValueById(32L);
			/*if(lis!=null){
				min=lis.get(0)!=null?Integer.parseInt(lis.get(0).toString()):8;
				max=lis.get(1)!=null?Integer.parseInt(lis.get(1).toString()):10;
				validatePasswords=lis.get(2)!=null?lis.get(2).toString():"SI";
				numValidatePassword=lis.get(3)!=null?Integer.parseInt(lis.get(3).toString()):24;
				validateIdentificationPassword=lis.get(4)!=null?lis.get(4).toString():"NO";
				validateDictionary=lis.get(5)!=null?lis.get(5).toString():"NO";
				validateSeq=lis.get(6)!=null?lis.get(6).toString():"NO";
				validateComplex=lis.get(7)!=null?lis.get(7).toString():"NO";
				System.out.println("validateComplex: "+validateComplex);
			}*/
			System.out.println("validateComplex: "+validateComplex);
			System.out.println("min " + min);
			System.out.println("max " + max); 
			
			boolean res=validateSize(newPassword, min, max);
			if(res){
				message="La longitud de la contrase�a debe ser m�nimo de " + min +" caracteres y m�ximo de " +max;
				return message;
			}
			
			if(validateIdentificationPassword.equals("NO")){
				res2=containIdentification(newPassword, userId);
				if(res2){
					message="La contrase�a no puede contener su n�mero de identificaci�n";
					return message;
				}
			}
			
			if(validateDictionary.equals("SI")){
				String message2;
				boolean res3=validatePasswordDictionary(newPassword);
				if(res3){
					message2="La contrase�a no est� permitida en el sistema, debido a que existe en el diccionario de contrase�as inv�lidas.";
					return message2;
				}
			}
			System.out.println("continuo, la respues fue false");
			if(validateSeq.equals("SI")){
				String message1=validate1(newPassword);
				if(message1!=null){
					return message1;
				}
			}
			
			if(validatePasswords.equals("SI")){
				String userPassword=Encryptor.getEncrypt(newPassword);
				res1=existPassword(userId, userPassword, numValidatePassword);
				if(res1){
					System.out.println("contrase�a repetida " + "res1 " + res1);
					message="La contrase�a no debe estar repetida";
					return message;
				}
			}
			/**
			 * @author ablasquez
			 * Se valida la complejodad de la contrase�a
			 * debe contener m�nimo una letra may�sucula, una letra minuscula, un numero y un caracter especial	
			 */
			if(validateComplex.equals("SI")){
				boolean mayus=false, minus=false, especial=false, num=false;
				respcomplex = false;
				int tamanio=newPassword.length();
				char letra;
				int valascii = 0;
				for (int i=0; i<tamanio; i++){
					letra = newPassword.charAt(i);
					valascii = (int)letra;
					//System.out.println("Letra: "+letra+" Acsii: "+valascii);
					if(!mayus){
						if((valascii >= 65)&& (valascii <= 90)){
							mayus = true;
						}
					}
					
					if(!minus){
						if((valascii >= 97)&& (valascii <= 122)){
							minus = true;
						}
					}
					
					if(!num){
						if((valascii >= 48)&& (valascii <= 57)){
							num = true;
						}
					}
					
					if(!especial){
						if((valascii >= 33)&& (valascii <= 47) || 
								(valascii >= 58)&& (valascii <= 64) || 
								(valascii >= 91)&& (valascii <= 96) || 
								(valascii >= 123)&& (valascii <= 126)){
							especial = true;
						}
					}
				}
				if(mayus && minus && especial && num){
					respcomplex = true;
				}else{
					message = "La contrase�a debe tener una combinaci�n de may�sculas, min�sculas, n�meros y simbolos.";					
					respcomplex = false;
					return message;
				}
				
			}else{
				System.out.println("No requiere validar la complejidad de la contrase�a");
				respcomplex = true;
			}
				
			System.out.println("res1 "+res1);									
			System.out.println("res2 "+res2);
			System.out.println("respcomplex "+respcomplex);
		

			if(res1==false && res2==false && respcomplex==true){
				 String userPassword1=Encryptor.getEncrypt(newPassword);
					TbSystemUser tu = em.find(TbSystemUser.class, userId);
					tu.setUserPassword(userPassword1);
					tu.setUserFirstLogin(3);
					tu.setUserPwdDate(new Timestamp(System.currentTimeMillis()));
					objectEM = new ObjectEM(em);
					if(objectEM.update(tu)){
						log.insertLog("Cambio de contrase�a | Se ha actualizado la contrase�a del usuario del sistema ID:"
										+ tu.getUserId(), LogReference.USER,
								LogAction.UPDATE, ip, creationUser);
						
						if(validatePasswords.equals("SI")){
							if(savePassword(userPassword1, tu, userId)){
								 log.insertLog("Se ha creado contrase�a en historico de contrase�as del usuario: " + tu.getUserCode() + "- "
											+ tu.getTbCodeType().getCodeTypeDescription(), LogReference.CLIENT, LogAction.CREATE,
											ip, creationUser);
							}
						}
						
						// Sending email to client/user
						outbox.insertMessageOutbox(tu.getUserId(), 
				                   EmailProcess.PASSWORD_CHANGE,
				                   null,
				                   null,
				                   null, 
				                   null,
				                   null,
				                   null,
				                   null,
				                   null,
				                   creationUser,
				                   null,
				                   null,
				                   null,
				                   true,
							       null);						
						
						 message="Contrase�a actualizada correctamente.";
						 
						 /**
							 * @author ablasquez
							 * Se crea registro de proceso para cliente al para cuando se restablece la contrase�a
							 */
							TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, userId);
							Long idPTA;
							if(pt==null){
								idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  userId, ip, creationUser);
							}
							else{
								idPTA=pt.getProcessTrackId();
							}
							Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.CHANGE_PASSWORD, 
									"El cambio de contrase�a fue realizado satisfactoriamente.",
									userId, ip,1,"Error Cambio de Contrase�a",null,null,null,null);			
							//proceso cliente
							TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,userId);
							Long idPTC;
							if(ptc==null){
								idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, userId, ip, creationUser);
							}
							else{
								idPTC=ptc.getProcessTrackId();
							}
							Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.CHANGE_PASSWORD, 
									"El cambio de contrase�a fue realizado satisfactoriamente.",
									userId, ip, 1, "Error Cambio de Contrase�a",null,null,null,null);
							
							/****/
					} else {
						log.insertLog("Cambio de contrase�a | No se ha podido actualizar la contrase�a del usuario del sistema ID:"
										+ tu.getUserId(), LogReference.USER,
								LogAction.UPDATEFAIL, ip, creationUser);
					}
				}	
			System.out.println("message " + message);
		  
		}catch (Exception e) {
			message="!!!Error al actualizar contrase�a.";
			e.printStackTrace();
			System.out.println( " [ Error en UserEJB.changePasswordFirst ] " );
		}
		return message;
	}
	
	
	@Override
	public Object[] getInfAssociationProdcut(Long accountId, Long idProduct){
		 Query q;
		 TbBankAccount tba= em.find(TbBankAccount.class, idProduct);
		 System.out.println("Producto " + tba.getBankAccountNumber());
		 q= em.createNativeQuery("select bos_id, bank_id, message_type, item_id, document_type, " +
                 " client_id, start_date, account_type, bank_number " +
                 " from table(func_association_account(?1,?2)) ").setMaxResults(1);

         q.setParameter(1, accountId).setParameter(2, tba.getBankAccountNumber());
         Object[] o= (Object[]) q.getSingleResult();
         
         return o;
	}
	
	

	@Override
	public boolean deleteAssociation(Long idProduct, Long acountId){
		boolean res = false;
	
		Query q= em.createNativeQuery("delete from re_account_bank where account_id=?1 and bank_account_id=?2");
		q.setParameter(1, acountId).setParameter(2, idProduct);
		
		int i=q.executeUpdate();
		if(i>0){
			res=true;
		}
		else{
			res=false;
		}
		return res;
	}
	
	
	/**
	 * M�todo creado para listar usuarios de que no tengan transacciones asociadas.
	 * @return the users
	 * @author psanchez
	 */
	@Override
	public List<TbSystemUser> getDeleteUsersClients() {
		List<TbSystemUser> users = new ArrayList<TbSystemUser>();
		try {
			Query q = em.createQuery("SELECT su FROM TbSystemUser su WHERE NOT EXISTS (SELECT ta FROM TbAccount ta WHERE ta.tbSystemUser = su.userId) ORDER BY TO_NUMBER(su.userCode) ASC ");
			for (Object object : q.getResultList()) {
				TbSystemUser systemUser = (TbSystemUser) object;
				users.add(systemUser);
			}
		} catch (Exception e) {
			System.out.println(" [Error en UserEJB.getDeleteUsersClients] ");
			e.printStackTrace();
		}
		return users;
	}

	/**
	 * M�todo creado para listar usuarios a inactivar
	 * @return the users
	 * @author psanchez
	 */
	@Override
	public List<TbSystemUser> getEnableUsersList() {
		List<TbSystemUser> users = new ArrayList<TbSystemUser>();
		try {
			Query q = em.createQuery("SELECT su FROM TbSystemUser su " +
				                  	 "WHERE su.userState='0' ORDER BY su.userCode");
			for (Object object : q.getResultList()) {
				TbSystemUser systemUser = (TbSystemUser) object;
				users.add(systemUser);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en UserEJB.getEnableUsersList] ");
			e.printStackTrace();
		}
		return users;
	}
	
	/**
	 * M�todo creado para listar usuarios en sesi�n.
	 * @return the users
	 * @author psanchez
	 */
	@Override
	public List<TbSystemUser> getOnlineUsersList() {
		
		List<TbSystemUser> users = new ArrayList<TbSystemUser>();
		try {
			Query q = em.createQuery("SELECT su FROM TbSystemUser su " +
				                  	 "WHERE su.userStateConnection='0' ORDER BY su.userCode");
			for (Object object : q.getResultList()) {
				TbSystemUser systemUser = (TbSystemUser) object;
				users.add(systemUser);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en UserEJB.getListOnlineUser ] ");
			e.printStackTrace();
		}
		return users;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TbSystemUser>  getAllUsersSystem(int page, int rows) {
		List<TbSystemUser> users = new ArrayList<TbSystemUser>();
		String qry1, qry2, qry3= "";
		Long idUser = null;
		try {
              qry1= "SELECT COUNT(*) FROM (" ;
			
              qry2= "SELECT * FROM (" ;		
			
			  qry3= "SELECT a.*, rownum rnum FROM (select distinct tu.user_id, tu.user_names from tb_system_user tu "+
					"inner join re_user_role rer on tu.user_id=rer.user_id "+
					"inner join tb_role r on rer.role_id=r.role_id "+
					"where r.role_id not in (2,3) "+
					"union " +
					"select distinct tu.user_id, tu.user_names from tb_system_user tu " +
					"inner join re_user_role rer on tu.user_id=rer.user_id " +
					"where rer.role_id is null " +
					"order by 2 ";
			
			if(page!=0){
				qry3 = qry3+") a WHERE rownum <= "+(page*rows)+") WHERE rnum > "+((page*rows)-rows)+" ";
				Query query = em.createNativeQuery(qry2+qry3);
				List<Object> result= (List<Object>)query.getResultList();
				for (Object o : result) {
					if (o != null && o instanceof Object[]) {
						Object[] obj = (Object[]) o;
						idUser = Long.parseLong((obj[0].toString()));
						TbSystemUser systemUser = em.find(TbSystemUser.class, idUser);
						users.add(systemUser);
					}
				}
			}else {
				Query query = em.createNativeQuery(qry1+qry3+")a )");
				users= query.getResultList();
			}			
		} catch (Exception e) {
			System.out.println(" [ Error en UserEJB.getAllUsersSystem ] ");
			e.printStackTrace();
		}
		return users;
	}


	@Override
	public TbUserStateType getState(Integer stateId) {
		try{
			TbUserStateType state = em.find(TbUserStateType.class, stateId);
			return state;
		}catch(NoResultException e){
			System.out.println("No Hay Resultados [UserEJB.getState ]");
			return null;
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AllInfoClient> getAllInfoClients(int page, int rows) {
		List<AllInfoClient> clients = new ArrayList<AllInfoClient>();
		String qry1, qry2, qry3 = "";
		try {
			qry1= "SELECT COUNT(*) FROM (";
			
            qry2= "SELECT * FROM (";
			
			qry3= "SELECT a.*, rownum rnum FROM (SELECT DISTINCT to_char(tu.user_id), ud.user_data_id, tu.user_pwd_date, " +
	              "tu.code_type_id, tu.user_code, tu.user_names, tu.user_second_names, tu.user_email, ta.account_id, " +
	              "us.user_state_id, us.user_state_description, ct.code_type_abbreviation, ud.user_data_phone, " +
	              "ud.user_data_optional_phone, ud.user_data_address, ud.municipality_id, Td.Distribution_Type_Name, " +
	              "ta.account_balance, Tba.Bank_Name, tb.bank_account_number, To_Char(Tb.Vehicle_Plate), Tb.Category_Id, " +
	              "td.device_code, td.device_facial_id " +
			      "FROM tb_system_user tu " +
			      "INNER JOIN tb_code_type ct ON tu.code_type_id = ct.code_type_id " +
			      "INNER JOIN tb_user_state_type us ON tu.user_state = us.user_state_id " +
			      "INNER JOIN re_user_role rer ON tu.user_id = rer.user_id " +
			      "INNER JOIN tb_role r ON rer.role_id = r.role_id " +
			      "INNER JOIN tb_user_data ud ON tu.user_id = ud.user_id " +
			      "INNER JOIN tb_account ta ON tu.user_id = ta.user_id " +
			      "INNER JOIN tb_distribution_type td ON ta.distribution_type_id = td.distribution_type_id " +
			      "INNER JOIN Re_Account_Bank Rab ON Ta.Account_Id = Rab.Account_Id " +
			      "INNER JOIN Tb_Bank_Account Tb ON Rab.Bank_Account_Id = Tb.Bank_Account_Id " +
			      "INNER JOIN tb_bank tba ON Tb.Product = Tba.Bank_Id " +
			      "INNER JOIN Re_Account_Device Rad ON Ta.Account_Id = Rad.Account_Id " +
			      "INNER JOIN tb_device td ON rad.device_id = td.device_id " +
			      "INNER JOIN Tb_Vehicle Tb ON Rad.Vehicle_Id = Tb.Vehicle_Id " +
			      "WHERE r.role_id IN (2,3,5) ";

				if(page!=0){
					qry3 = qry3+ "ORDER BY 6,7,9 desc) a WHERE rownum  <="+(page*rows)+") WHERE rnum > "+((page*rows)-rows)+" ";	
					Query query = em.createNativeQuery(qry2+qry3);
					List<Object> result= (List<Object>)query.getResultList();
					for(int i=0;i<result.size();i++){
						Object[] obj=(Object[]) result.get(i);					
						AllInfoClient client = new AllInfoClient();	
						if(client!=null){
							client.setUserIdU(Long.parseLong(obj[0].toString()));
							client.setUserDataIdU(Long.parseLong(obj[1].toString()));
							client.setCreationDateU(new SimpleDateFormat("dd/MM/yyyy").
							format(new SimpleDateFormat("yyyy-MM-dd").parse(obj[2].toString())));
							client.setCodeTypeU(Long.parseLong(obj[3].toString()));
							client.setNumberDocU(obj[4].toString());
							client.setNameU(obj[5].toString());	
							client.setSecondNameU(obj[6].toString());	
							client.setEmailU(obj[7].toString());
							client.setAccountIdU(Long.parseLong(obj[8]!=null?obj[8].toString():"0"));
							client.setStateIdU(Long.parseLong(obj[9].toString()));
							client.setStateDescU(obj[10].toString());
							client.setCodeTypeAbbrU(obj[11].toString());
							client.setPhoneU(obj[12].toString());
							client.setOptionalPhoneU(obj[13]!=null?obj[13].toString():"");
							client.setAddressU(obj[14].toString());
							client.setCityU(obj[15].toString());
							client.setDistributionTypeU(obj[16]!=null?obj[16].toString():"-");	
							client.setBalanceAccountU(Long.parseLong(obj[17]!=null?obj[17].toString():"0"));
							client.setNameBankU(obj[18]!=null?obj[18].toString():"-");	
							client.setDigitsBankU(Long.parseLong(obj[19]!=null?obj[19].toString():"0"));
							client.setPlacaU(obj[20]!=null?obj[20].toString():"-");
							client.setCategoryU(Long.parseLong(obj[21]!=null?obj[21].toString():"0"));
							client.setTagIdU(obj[22]!=null?obj[22].toString():"-");	
							client.setFacialU(obj[23]!=null?obj[23].toString():"-");	
							clients.add(client);
						}
					}
				}
				else {
					Query query = em.createNativeQuery(qry1+qry3+")a )");
					clients= query.getResultList();
				}				
		} catch (Exception e) {
			System.out.println(" [ Error en UserEJB.getAllInfoClients ] ");
			e.printStackTrace();
		}	
		return clients;
	}

	
	 @SuppressWarnings("unchecked")
		public List<Object> initRecharge(Long account, Long itemId){
	    	List<Object> lis= new ArrayList<Object>();
	    	if(account==0){
				System.out.println("Entre a recargar");
				System.out.println("cuenta" + itemId);
				Query q2;
				q2= em.createNativeQuery("select umbral_id "+
                       					"from (select umbral_id from tb_umbral " +
                       					"where umbral_state=0 " +
                       					"and umbral_value >0 " +
                       					"and type_message_id = 2 " +
                       					"and account_id=?1 " +
                       					"and average=-1 " +
                       					"order by umbral_time desc ) where ROWNUM=1");
				q2.setParameter(1, itemId);
				lis=q2.getResultList(); 
			
			}
	    	return lis;
	    }
	    
	    public Object[] recharge(BigDecimal umbral){
	    	Query q2;
	    	q2= em.createNativeQuery("select BOSID, BANKID, MESSAGETYPE, ITEMID, DOCUMENTTYPE, CLIENTID, STARTDATE, BANKNUMBER, BANKTYPE, AMOUNT, REVER  from table(f_recharge(?1))");
			q2.setParameter(1, umbral);
			Object[] obj= (Object[]) q2.getSingleResult();
			
			return obj;
	    }

		@Override
		public List<TbBankAccount> getProductsByClient(Long idBank, Long idType, Long userId) {		
			List<TbBankAccount> list = new ArrayList<TbBankAccount>();
			try {				
				Query q = 
					em.createNativeQuery("select distinct tba.BANK_ACCOUNT_ID "+
							" from TB_BANK_ACCOUNT tba "+
							" left join VW_LAST_PRODUCT_BANK rab on tba.BANK_ACCOUNT_ID=rab.BANK_ACCOUNT_ID "+
							" where tba.USRS=?2 and tba.PRODUCT=?1 and tba.bank_account_type=?3 "+
							" and (rab.STATE_ACCOUNT_BANK is null or " +
							"(rab.STATE_ACCOUNT_BANK=2 and rab.DATE_SEND_BANK is null) or " +
							"(rab.STATE_ACCOUNT_BANK=3 and rab.CODE_RESULT=0) or " +
							"(rab.STATE_ACCOUNT_BANK=2 and rab.DATE_DISSOCIATION_BANK is not null))");
				q.setParameter(1, idBank);
				q.setParameter(2, userId.toString());
				q.setParameter(3, idType);
				for (Object obj : q.getResultList()) {
					list.add(em.find(TbBankAccount.class, Long.parseLong(obj.toString())));
				}
				
			} catch (Exception e) {
				System.out.println(" [ Error en UserEJB.getProductsByClient ] ");
				e.printStackTrace();
			}
			return list;
			}
		
		public int isClientActive(TbSystemUser systemUser){
		  try{	
			
			int estado = systemUser.getUserState().getUserStateId();
			Query q = em.createNativeQuery("select count(*) from tb_account where user_id=?1");
			q.setParameter(1, systemUser.getUserId());
									
			int result =((BigDecimal)q.getSingleResult()).intValue();
			
			Query q1 = em.createNativeQuery("select count(*) "+
					"from re_account_bank rab "+
					"inner join tb_account ta on rab.account_id=ta.account_id "+
					"where ta.user_id=?1 "+
					"and rab.state_account_bank = 1");
			q1.setParameter(1, systemUser.getUserId());
			int result2 =((BigDecimal)q1.getSingleResult()).intValue();
			
			Query q2 = em.createNativeQuery("select count(*) from tb_document where user_id = ?1 and state_document = 3 ");
			q2.setParameter(1, systemUser.getUserId());
			int result3 =((BigDecimal)q2.getSingleResult()).intValue();			
			
				if(result > 0 && result2>0){
					estado = 2;
				}else if(result > 0){
					estado = 0;
				}else if(result3 > 0){
					estado = 3;
				}else {
					Query query= em.createNativeQuery("SELECT tsu.user_state_old FROM tb_system_user tsu WHERE tsu.user_id=?1 ");		 
			        query.setParameter(1,systemUser.getUserId());
			        int userState = ((BigDecimal) query.getSingleResult()).intValue();
					return userState;
				}
			return estado;
		  }catch(Exception e){
			  e.printStackTrace();
			  return systemUser.getUserState().getUserStateId();
		  }
		}

		@Override
		public boolean getIsAccountdissociationProcess(Long accountId) {
			try{
				boolean result = false;
				Query q = em.createNativeQuery("select account_bank_id from (select * from RE_ACCOUNT_BANK where ACCOUNT_ID=?1 order by DATE_ASSOCIATION desc) where ROWNUM=1");
				q.setParameter(1, accountId);
				
				BigDecimal idBankAccount = (BigDecimal) q.getSingleResult();
				
				if(idBankAccount==null){
					result= true;
				}else{
					ReAccountBank rab = em.find(ReAccountBank.class, idBankAccount.longValue());
					if(rab.getState_account_bank().equals(1)){
						result= false;
					}else if(rab.getState_account_bank().equals(3)){
						result= true;
					}else if(rab.getState_account_bank().equals(2)){
						result= true;
					}else if(rab.getState_account_bank().equals(4)){
						result= true;
					}
				}				
				
				return result;
			}catch(NoResultException n){
				n.printStackTrace();
				return false;
			}			
		}

		@SuppressWarnings("unchecked")
		@Override
		public List<AllInfoClient> getInfoClientsByFilters(Long codeType, String numberDoc, String name, 
				String secondName, String email, String accountId, String placa, int page, int rows, 
				int type, int filtroId, String filtroDescription) {
			List<AllInfoClient> clients = new ArrayList<AllInfoClient>();
			String qry1, qry2, qry3 = "";

			try {
			    qry1="SELECT COUNT(*) FROM (";
				
				qry2="SELECT * FROM (";
				
				qry3="SELECT a.*, rownum rnum FROM (SELECT DISTINCT(tu.user_id), ud.user_data_id, tu.user_pwd_date, ct.code_type_id, tu.user_code, " +
					"tu.user_names, tu.user_second_names, tu.user_email, ta.account_id, us.user_state_id, us.user_state_description, " +
					"ct.code_type_abbreviation, ud.user_data_phone, decode(to_char(ud.user_data_optional_phone),null,'',to_char(ud.user_data_optional_phone)), " +
					"ud.user_data_address, ud.municipality_id, Td.Distribution_Type_Name, ta.account_balance, Tba.Bank_Name, " +
					"tb.bank_account_number, To_Char(Tb.Vehicle_Plate), Tb.Category_Id, td.device_code, td.device_facial_id " +
					"FROM tb_system_user tu " +
					"LEFT JOIN tb_code_type ct ON tu.code_type_id=ct.code_type_id " +
					"INNER JOIN tb_user_state_type us ON tu.user_state=us.user_state_id " +
					"LEFT JOIN re_user_role rer ON tu.user_id=rer.user_id " +
					"LEFT JOIN tb_role r ON rer.role_id=r.role_id " +
					"LEFT JOIN tb_account ta ON ta.user_id=tu.user_id " +
					"LEFT JOIN tb_distribution_type td ON ta.distribution_type_id=td.distribution_type_id " +
					"INNER JOIN tb_user_data ud ON ud.user_id=tu.user_id " +
					"LEFT JOIN Re_Account_Device Rad ON Rad.Account_Id=Ta.Account_Id " +
					"LEFT JOIN Tb_Vehicle Tb ON Rad.Vehicle_Id=Tb.Vehicle_Id " +
					"LEFT JOIN tb_device td ON rad.device_id = td.device_id " +
					"LEFT JOIN Re_Account_Bank Rab ON Ta.Account_Id=Rab.Account_Id " +
					"LEFT JOIN Tb_Bank_Account Tb ON Rab.Bank_Account_Id=Tb.Bank_Account_Id " +
					"LEFT JOIN tb_bank tba ON Tb.Product=Tba.Bank_Id " +
					"WHERE r.role_id IN (2,3,5) ";
						
				if(codeType!=-1L && type==2){
					qry3 = qry3+"AND ct.code_type_id="+codeType+" ";
				}
				if(!numberDoc.equals("") && type==2){
					qry3 = qry3+"AND tu.user_code like '%"+numberDoc.trim()+"%' ";
				}
				if(!name.equals("") && type==2){
					qry3 = qry3+"AND Upper(tu.user_names) like '%"+name.toUpperCase()+"%' ";
				}
				if(!secondName.equals("") && type==2){
					qry3 = qry3+"AND Upper(tu.user_second_names) like '%"+secondName.toUpperCase()+"%' ";
				}
				if(!email.equals("") && email.contains("_") && type==2){
					qry3 = qry3+"AND Upper(tu.user_email) like '%\\_%' ESCAPE '\\' ";
				}
				if(!email.equals("") && !email.equals("_") && type==2){
					qry3 = qry3+"AND lower(tu.user_email) like '%"+email.toLowerCase()+"%' ";
				}
				if(!accountId.equals("") && type==2) {
					qry3 = qry3+"AND ta.account_id like '%"+accountId+"%' ";
				}
				if(!placa.equals("") && !placa.equals("-") && type==2){
					qry3 = qry3+"AND upper(tb.vehicle_plate) like '%"+placa.toUpperCase()+"%' ";
				}				
				if(!filtroDescription.equals("") && filtroId==1 && type==1){
					qry3 = qry3+"AND tu.user_code like '%"+filtroDescription.trim()+"%' ";
				}
				if(!filtroDescription.equals("") && filtroId==2 && type==1){
					qry3 = qry3+"AND Upper(tu.user_names) like '%"+filtroDescription.toUpperCase().trim()+"%' ";
				}
				if(!filtroDescription.equals("") && filtroId==3 && type==1){
					qry3 = qry3+"AND Upper(tu.user_second_names) like '%"+filtroDescription.toUpperCase().trim()+"%' ";
				}
				if (!filtroDescription.equals("") && filtroId==4 && type==1) {
					qry3 = qry3+"AND ta.account_id like '%"+filtroDescription.trim()+"%' ";
				}
				if(!filtroDescription.equals("") && filtroId==5 && type==1){
					qry3 = qry3+"AND upper(tb.vehicle_plate) like '%"+filtroDescription.toUpperCase().trim()+"%' ";
				}
				if(!filtroDescription.equals("")&& filtroId==6 && type==1){
					qry3 = qry3+"AND upper(td.device_code) like '%"+filtroDescription.toUpperCase().trim()+"%' ";
				}
				if(!filtroDescription.equals("") && filtroId==7 && type==1){
					qry3 = qry3+"AND upper(td.device_facial_id) like '%"+filtroDescription.toUpperCase().trim()+"%' ";
				}
				if(!filtroDescription.equals("") && filtroId==8 && type==1){
					qry3 = qry3+"AND upper(tb.aditional1) like '%"+filtroDescription.toUpperCase().trim()+"%' ";
				}
				if(!filtroDescription.equals("")&& filtroId==9 && type==1){
					qry3 = qry3+"AND upper(tb.aditional2) like '%"+filtroDescription.toUpperCase().trim()+"%' ";
				}
				if(!filtroDescription.equals("") && filtroId==10 && type==1){
					qry3 = qry3+"AND upper(tb.aditional3) like '%"+filtroDescription.toUpperCase().trim()+"%' ";
				}
				
				if(page!=0){
					qry3 = qry3+ "ORDER BY 6,7,9 desc) a WHERE rownum <= "+(page*rows)+") WHERE rnum > "+((page*rows)-rows)+" ";	
					Query query = em.createNativeQuery(qry2+qry3);

					List<Object> result= (List<Object>)query.getResultList();
					for(int i=0;i<result.size();i++){
						Object[] obj=(Object[]) result.get(i);
						AllInfoClient client = new AllInfoClient();	
						if(client!=null){
							client.setUserIdU(Long.parseLong(obj[0].toString()));
							client.setUserDataIdU(Long.parseLong(obj[1].toString()));
							client.setCreationDateU(new SimpleDateFormat("dd/MM/yyyy").
								   format(new SimpleDateFormat("yyyy-MM-dd").parse(obj[2].toString())));
							client.setCodeTypeU(Long.parseLong(obj[3].toString()));
							client.setNumberDocU(obj[4].toString());
							client.setNameU(obj[5].toString());	
							client.setSecondNameU(obj[6].toString());	
							client.setEmailU(obj[7].toString());
							client.setAccountIdU(Long.parseLong(obj[8]!=null?obj[8].toString():"0"));
							client.setStateIdU(Long.parseLong(obj[9].toString()));
							client.setStateDescU(obj[10].toString());
							client.setCodeTypeAbbrU(obj[11].toString());
							client.setPhoneU(obj[12].toString());
							client.setOptionalPhoneU(obj[13]!=null?obj[13].toString():"");
							client.setAddressU(obj[14].toString());
							client.setCityU(obj[15].toString());
							client.setDistributionTypeU(obj[16]!=null?obj[16].toString():"-");	
							client.setBalanceAccountU(Long.parseLong(obj[17]!=null?obj[17].toString():"0"));
							client.setNameBankU(obj[18]!=null?obj[18].toString():"-");	
							client.setDigitsBankU(Long.parseLong(obj[19]!=null?obj[19].toString():"0"));
							client.setPlacaU(obj[20]!=null?obj[20].toString():"-");
							client.setCategoryU(Long.parseLong(obj[21]!=null?obj[21].toString():"0"));
							client.setTagIdU(obj[22]!=null?obj[22].toString():"-");	
							client.setFacialU(obj[23]!=null?obj[23].toString():"-");	
							clients.add(client);
						}
					}
				}
				else {
					Query query = em.createNativeQuery(qry1+qry3+")a )");
					clients= query.getResultList();
				}	
			} catch (Exception e) {
				System.out.println(" [ Error en UserEJB.getInfoClientsByFilters ] ");
				e.printStackTrace();
			}
			return clients;
		}

		@Override
		public List<TbMunicipality> getListCity() {
			try{
				List<TbMunicipality> list = new ArrayList<TbMunicipality>();
				Query query = 
					em.createQuery("Select m from TbMunicipality m order by m.municipalityName");
				    
				    for (Object obj : query.getResultList()) {
						list.add((TbMunicipality) obj);
					}
				    return list;
			}catch(Exception e){
				System.out.println("[ Error en UserEJB.getListCity ]");
				e.printStackTrace();
				return null;
			}			
		}

		@Override
		public List<TbUserStateType> getListUserStates() {
			try{
				List<TbUserStateType> list = new ArrayList<TbUserStateType>();
				Query query = 
					em.createQuery("Select m from TbUserStateType m order by m.userStateId");
				    
				    for (Object obj : query.getResultList()) {
						list.add((TbUserStateType) obj);
					}
				    return list;
			}catch(Exception e){
				System.out.println("[ Error en UserEJB.getListUserStates ]");
				e.printStackTrace();
				return null;
			}
		}


		@Override
		public boolean editClientData(Long dataId, Long city, String userPhone1,
				String userPhone2, String userAdress, String ip, Long userId) {
			 try {	
				 objectEM = new ObjectEM(em);			
					TbUserData data = em.find(TbUserData.class, dataId); 
					
					TbSystemUser user = em.find(TbSystemUser.class, userId);
					data.setUserDataAddress(userAdress);
					data.setUserDataPhone(userPhone1);
					data.setTbMunicipality(em.find(TbMunicipality.class, city));
					
					if (userPhone2 != null) {
						if (!userPhone2.equals("")) {
							data.setUserDataOptionalPhone(userPhone2);
						}
					}
					
					// Updating the  Data
					if (objectEM.update(data)) {
					//	log.insertLog( message , LogReference.CLIENTDATA, LogAction.UPDATE, ip, userId);
						return true;
					} else {
						log.insertLog("Error al modificar la dependencia del cliente C�digo: " + user.getUserCode() + "- "
								+ user.getTbCodeType().getCodeTypeDescription(), LogReference.CLIENTDATA, LogAction.UPDATEFAIL,
								ip, userId);
					}
				} catch (Exception e) {
					System.out.println( " [ Error en UserEJB.editClientData ] " );
					e.printStackTrace();
				}
				return false;
			}


		@SuppressWarnings("unchecked")
		@Override
		public List<ClientsDB> getLisClientsDB(long type) {
			SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			List<ClientsDB> list=new ArrayList<ClientsDB>();
			//String query="select ts.user_code,tc.code_type_abbreviation,ts.user_names,ts.user_second_names,ts.user_email";
			String query=null;
			if(type==1){
				query="select distinct tus.user_code,tus.user_names,tus.user_second_names,tus.user_email,tc.code_type_abbreviation "+
				",decode(tus.user_registration_date,null,tus.user_pwd_date,tus.user_registration_date) "+
				"from tb_system_user tus "+
				"inner join tb_user_state_type tut on tus.USER_STATE=tut.USER_STATE_ID "+
				"inner join RE_USER_ROLE rur on tus.user_id=rur.USER_ID "+
				"inner join tb_code_type tc on tus.CODE_TYPE_ID=tc.CODE_TYPE_ID "+
				"where rur.role_id in (2,3) and rur.user_id not in (select user_id from re_user_role where role_id not in (2,3)) "+
				"and tut.USER_STATE_ID=-1 "+
				"order by 6";
			}else if(type==2){
				query="select distinct tus.user_code,tus.user_names,tus.user_second_names,tus.user_email,tc.code_type_abbreviation,td.UPLOAD_DATE_DOCUMENT "+
                "from tb_system_user tus "+
                "inner join tb_document td on td.USER_ID=tus.user_id "+
                "inner join RE_USER_ROLE rur on tus.user_id=rur.USER_ID "+
                "inner join tb_code_type tc on tus.CODE_TYPE_ID=tc.CODE_TYPE_ID "+
                "where rur.role_id in (2,3) and rur.user_id not in (select user_id from re_user_role where role_id not in (2,3)) "+
                "and td.state_document=4 "+
                "and td.UPLOAD_DATE_DOCUMENT=(select min(tdd.UPLOAD_DATE_DOCUMENT)as fecha from tb_document tdd where tdd.user_id=tus.user_id and tdd.state_document=4) "+
                "order by 6";
		}else if(type==3){
				query="select ts.user_code,ts.user_names,ts.user_second_names,ts.user_email,decode(ts.code_type_id,1,'CC',2,'CE',3,'NIT'),min(vwpas.date_creation),to_char(min(vwpas.BANK_ACCOUNT_NUMBER)) "+
				" from tb_system_user ts "+ 
				" inner join VW_PRODUCTS_ASSOCIATED_USER vwpas on vwpas.USRS=ts.USER_ID "+
				" inner join RE_USER_ROLE rur on ts.user_id=rur.USER_ID "+
				" where (vwpas.STATE_ACCOUNT_BANK is null or vwpas.STATE_ACCOUNT_BANK=3) "+
				" and rur.role_id in (2,3) and rur.user_id not in (select user_id from re_user_role where role_id not in (2,3)) "+
				" group by ts.user_code,ts.user_names,ts.user_second_names,ts.user_email,decode(ts.code_type_id,1,'CC',2,'CE',3,'NIT')";
			}else if(type==4){
				query="select distinct ts.user_code,ts.user_names,ts.user_second_names,ts.user_email,decode(ts.code_type_id,1,'CC',2,'CE',3,'NIT'), min(vw.date_association),to_char(min(vw.BANK_ACCOUNT_NUMBER)) "+
				" from tb_system_user ts "+
				" inner join VW_LAST_PRODUCT_BANK vw on vw.USER_ID=ts.USER_ID "+
				" inner join TB_ACCOUNT ta on vw.ACCOUNT_ID=ta.ACCOUNT_ID "+
				" inner join RE_USER_ROLE rur on ts.user_id=rur.USER_ID  "+
				" where (vw.STATE_ACCOUNT_BANK=1 or (vw.STATE_ACCOUNT_BANK=2 and (vw.CODE_RESULT is null or vw.CODE_RESULT <> 0))) "+
				" and rur.role_id in (2,3) and rur.user_id not in (select user_id from re_user_role where role_id not in (2,3)) "+
				" and ta.ACCOUNT_STATE_TYPE_ID in (0,7) "+
				" group by ts.user_code,ts.user_names,ts.user_second_names,ts.user_email,ts.code_type_id";
			}else if(type==5){
				query="select distinct tus.user_code,tus.user_names,tus.user_second_names,tus.user_email,tc.code_type_abbreviation,td.UPLOAD_DATE_DOCUMENT "+
                "from tb_system_user tus "+
                "inner join tb_document td on td.USER_ID=tus.user_id "+
                "inner join RE_USER_ROLE rur on tus.user_id=rur.USER_ID "+
                "inner join tb_code_type tc on tus.CODE_TYPE_ID=tc.CODE_TYPE_ID "+
                "where rur.role_id in (2,3) and rur.user_id not in (select user_id from re_user_role where role_id not in (2,3)) "+
                "and td.state_document=3 "+
                "and td.UPLOAD_DATE_DOCUMENT=(select min(tdd.UPLOAD_DATE_DOCUMENT)as fecha from tb_document tdd where tdd.user_id=tus.user_id and tdd.state_document=3) "+
                "order by 6";
			}
			
			try{
				Query q=em.createNativeQuery(query);
				List<Object[]> result = q.getResultList();
				
				System.out.println("size-->"+result.size());

				if(result != null){
					for (Object[] o : result) {
						ClientsDB client = new ClientsDB();
						client.setID(o[0].toString());
						client.setFirstName(o[1].toString());
						client.setSecondName(o[2].toString());
						client.setEmail(o[3].toString());
						client.setTypeID(o[4].toString());
						System.out.println("-"+o[0]+"-"+o[1]+"-"+o[2]+"-"+o[3]+"-"+o[4]);
						
						try {
							if(o[5].toString()==null){}
							else{
								System.out.println(o[5]);
								client.setDate(sf.parse(o[5].toString()));
							}

						} catch (NullPointerException e) {
							System.out.println("NullPointerException date");
						}

						if(type==3 || type==4){

							try {
								if(o[6].toString()==null){}
								else{
									System.out.println(o[6]);
									client.setAccount(o[6].toString());
								}
							} catch (NullPointerException e) {
								System.out.println("NullPointerException account");
							}											

						}
						/*else{
							client.setAccount(Long.parseLong(o[5].toString()));
							client.setDate(sf.parse(o[6].toString()));
						}*/
						list.add(client);
					}
				}	
				
				return list;
				
			} catch (Exception e) {
				System.out.println(" [ Error en UserEJB.getLisClientsDB ] ");
				e.printStackTrace();
				return null;
			}
		}

		@Override
		public void setStateSystemUser(long userId, Integer state) {
			try{
				TbSystemUser tu=getSystemUser(userId);
				tu.setUserState(em.find(TbUserStateType.class, state));
				em.merge(tu);
				em.flush();
			}catch(Exception e){
				e.printStackTrace();
				System.out.println("[ UserEJB.setStateSystemUser ]");
			}
		}

		@Override
		public Long[] getNumClientsDB() {
			Long[] cont=new Long[5];
			for(int pos=0;pos<5;pos++){
				String query=null;
				if(pos==0){
					query="select distinct tus.user_code,tus.user_names,tus.user_second_names,tus.user_email,tc.code_type_abbreviation "+
					",decode(tus.user_registration_date,null,tus.user_pwd_date,tus.user_registration_date) "+
					"from tb_system_user tus "+
					"inner join tb_user_state_type tut on tus.USER_STATE=tut.USER_STATE_ID "+
					"inner join RE_USER_ROLE rur on tus.user_id=rur.USER_ID "+
					"inner join tb_code_type tc on tus.CODE_TYPE_ID=tc.CODE_TYPE_ID "+
					"where rur.role_id in (2,3) and rur.user_id not in (select user_id from re_user_role where role_id not in (2,3)) "+
					"and tut.USER_STATE_ID=-1 "+
					"order by 6";
				}else if(pos==1){
					query="select distinct tus.user_code,tus.user_names,tus.user_second_names,tus.user_email,tc.code_type_abbreviation,td.UPLOAD_DATE_DOCUMENT "+
                    "from tb_system_user tus "+
                    "inner join tb_document td on td.USER_ID=tus.user_id "+
                    "inner join RE_USER_ROLE rur on tus.user_id=rur.USER_ID "+
                    "inner join tb_code_type tc on tus.CODE_TYPE_ID=tc.CODE_TYPE_ID "+
                    "where rur.role_id in (2,3) and rur.user_id not in (select user_id from re_user_role where role_id not in (2,3)) "+
                    "and td.state_document=4 "+
                    "and td.UPLOAD_DATE_DOCUMENT=(select min(tdd.UPLOAD_DATE_DOCUMENT)as fecha from tb_document tdd where tdd.user_id=tus.user_id and tdd.state_document=4) "+
                    "order by 6";
				}else if(pos==2){
					query="select ts.user_code,ts.user_names,ts.user_second_names,ts.user_email,decode(ts.code_type_id,1,'CC',2,'CE',3,'NIT'),min(vwpas.date_creation),to_char(min(vwpas.BANK_ACCOUNT_NUMBER)) "+
					" from tb_system_user ts "+ 
					" inner join VW_PRODUCTS_ASSOCIATED_USER vwpas on vwpas.USRS=ts.USER_ID "+
					" inner join RE_USER_ROLE rur on ts.user_id=rur.USER_ID "+
					" where (vwpas.STATE_ACCOUNT_BANK is null or vwpas.STATE_ACCOUNT_BANK=3) "+
					" and rur.role_id in (2,3) and rur.user_id not in (select user_id from re_user_role where role_id not in (2,3)) "+
					" group by ts.user_code,ts.user_names,ts.user_second_names,ts.user_email,decode(ts.code_type_id,1,'CC',2,'CE',3,'NIT')";
				}else if(pos==3){
					query="select distinct ts.user_code,ts.user_names,ts.user_second_names,ts.user_email,decode(ts.code_type_id,1,'CC',2,'CE',3,'NIT'), min(vw.date_association),to_char(min(vw.BANK_ACCOUNT_NUMBER)) "+
						" from tb_system_user ts "+
						" inner join VW_LAST_PRODUCT_BANK vw on vw.USER_ID=ts.USER_ID "+
						" inner join TB_ACCOUNT ta on vw.ACCOUNT_ID=ta.ACCOUNT_ID "+
						" inner join RE_USER_ROLE rur on ts.user_id=rur.USER_ID  "+
						" where (vw.STATE_ACCOUNT_BANK=1 or (vw.STATE_ACCOUNT_BANK=2 and (vw.CODE_RESULT is null or vw.CODE_RESULT <> 0))) "+
						" and rur.role_id in (2,3) and rur.user_id not in (select user_id from re_user_role where role_id not in (2,3)) "+
						" and ta.ACCOUNT_STATE_TYPE_ID in (0,7) "+
						" group by ts.user_code,ts.user_names,ts.user_second_names,ts.user_email,ts.code_type_id";
				}else if(pos==4){
					query="select distinct tus.user_code,tus.user_names,tus.user_second_names,tus.user_email,tc.code_type_abbreviation,td.UPLOAD_DATE_DOCUMENT "+
                    "from tb_system_user tus "+
                    "inner join tb_document td on td.USER_ID=tus.user_id "+
                    "inner join RE_USER_ROLE rur on tus.user_id=rur.USER_ID "+
                    "inner join tb_code_type tc on tus.CODE_TYPE_ID=tc.CODE_TYPE_ID "+
                    "where rur.role_id in (2,3) and rur.user_id not in (select user_id from re_user_role where role_id not in (2,3)) "+
                    "and td.state_document=3 "+
                    "and td.UPLOAD_DATE_DOCUMENT=(select min(tdd.UPLOAD_DATE_DOCUMENT)as fecha from tb_document tdd where tdd.user_id=tus.user_id and tdd.state_document=3) "+
                    "order by 6";
				}
				
				try{
					Query q;
					try {
						q=em.createNativeQuery(query);
						@SuppressWarnings("unchecked")
						List<Object[]> result = q.getResultList();
						cont[pos]=new Long(result.size());
					} catch (NullPointerException e) {
						cont[pos]=0l;
					}

					query="";
				} catch (Exception e) {
					System.out.println(" [ Error en UserEJB.getNumClientsDB ] ");
					e.printStackTrace();
					return null;
				}
			}
			return cont;
		}


		@Override
		public boolean editClientInformation(Long clientId, Long userDataId, String email, Long city, String address, 
				       String phone, String optionalPhone, String ip, Long creationUser) {
			 try {	
				 objectEM = new ObjectEM(em);			
					TbUserData data   = em.find(TbUserData.class, userDataId); 
					TbSystemUser user = em.find(TbSystemUser.class, clientId);
					TbMunicipality tm = em.find(TbMunicipality.class, city);
					
					if(optionalPhone != null && !optionalPhone.equals("")){
						data.setUserDataOptionalPhone(optionalPhone);
					}else{
						data.setUserDataOptionalPhone("");
					}
					
					String message = "Se modificaron los datos del cliente con identificaci�n: " + user.getUserCode() + "- "
					+ user.getTbCodeType().getCodeTypeAbbreviation() + ". Valores Anteriores (email, direcci�n, cel, tel Opc, municipio Dpto) : "
					+ user.getUserEmail() + "," + data.getUserDataAddress() + ","  
					+ data.getUserDataPhone() + "," +  data.getUserDataOptionalPhone() +"," + tm.getMunicipalityName().trim()+" "+tm.getTbDepartment().getDepartmentName()+".";
					
					user.setUserEmail(email);
					data.setUserDataAddress(address);
					data.setUserDataPhone(phone);
					data.setTbMunicipality(em.find(TbMunicipality.class, city));
	
						// Updating the  Data
						if (objectEM.update(data)) {
							log.insertLog( message , LogReference.CLIENTDATA, LogAction.UPDATE, ip, creationUser);
							
							//proceso administrador
							TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, clientId);
							Long idPTA;
							if(pt==null){
								idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  clientId, ip, creationUser);
							}
							else{
								idPTA=pt.getProcessTrackId();
							}
							Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.CLIENT_CREATION, 
									     message, creationUser, ip, 1, "",null,null,null,null);						
							//proceso cliente
							TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,clientId);
							Long idPTC;
							if(ptc==null){
								idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, clientId, ip, creationUser);
							}
							else{
								idPTC=ptc.getProcessTrackId();
							}
							Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.BANK_ACCOUNT_CLIENT, 
									     message, creationUser, ip, 1, "",null,null,null,null);
							return true;
						} else {
							log.insertLog("Error al modificar los datos del cliente C�digo: " + user.getUserCode() + "- "
									+ user.getTbCodeType().getCodeTypeDescription(), LogReference.CLIENTDATA, LogAction.UPDATEFAIL,
									ip, creationUser);
							return false;
						}
				} catch (Exception e) {
					System.out.println( " [ Error en UserEJB.editClientInformation. ] " );
					e.printStackTrace();
				}
				return false;
		}


		@SuppressWarnings("unused")
		@Override
		public boolean editInformationLegalPerson(Long clientId,
				Long userDataId, String name, String secondName, String email,
				Long city, String address, String phone, String optionalPhone,
				String ip, Long creationUser) {
			 try {	
				 objectEM = new ObjectEM(em);			
					TbUserData data   = em.find(TbUserData.class, userDataId); 
					TbSystemUser user = em.find(TbSystemUser.class, clientId);
					TbMunicipality tm = em.find(TbMunicipality.class, city);
					data.setTbSystemUser(em.find(TbSystemUser.class, clientId));
					
					if(optionalPhone != null && !optionalPhone.equals("")){
						data.setUserDataOptionalPhone(optionalPhone.trim());
					}else{
						data.setUserDataOptionalPhone("");
					}
					String message = "Se modificaron los datos del cliente con identificaci�n: " + user.getUserCode() + "- "
					+ user.getTbCodeType().getCodeTypeAbbreviation() + ". Valores Anteriores (nombre(s), apellido(s), email, direcci�n, cel, tel Opc, municipio Dpto) : "
					+ user.getUserNames() + ", " + user.getUserSecondNames() + ","  + user.getUserEmail() + ", " + data.getUserDataAddress() + ","  
					+ data.getUserDataPhone() + ", " +  data.getUserDataOptionalPhone() +", " +tm.getMunicipalityName().trim()+" "+tm.getTbDepartment().getDepartmentName()+".";
					
					data.setUserDataAddress(address.trim());
					user.setUserNames(name.trim().toUpperCase());
					user.setUserSecondNames(secondName.trim().toUpperCase());
					user.setUserEmail(email.trim().toLowerCase());
					data.setUserDataPhone(phone.trim());
					data.setTbMunicipality(em.find(TbMunicipality.class, city));

						// Updating the  Data
						if (objectEM.update(data)) {
							log.insertLog( message , LogReference.CLIENTDATA, LogAction.UPDATE, ip, creationUser);
							
							//proceso administrador
							TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, clientId);
							Long idPTA;
							if(pt==null){
								idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  creationUser, ip, creationUser);
							}
							else{
								idPTA=pt.getProcessTrackId();
							}
							Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.CLIENT_CREATION, 
									     message, creationUser, ip, 1, "", null,null,null,null);						
							//proceso cliente
							TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,clientId);
							Long idPTC;
							if(ptc==null){
								idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, clientId, ip, creationUser);
							}
							else{
								idPTC=ptc.getProcessTrackId();
							}
							Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.BANK_ACCOUNT_CLIENT, 
									     message, creationUser, ip, 1, "",null,null,null,null);
							return true;
						} else {
						log.insertLog("Error al modificar los datos del cliente C�digo: " + user.getUserCode() + "- "
									+ user.getTbCodeType().getCodeTypeDescription(), LogReference.CLIENTDATA, LogAction.UPDATEFAIL,
									ip, creationUser);
							return false;
						}
				} catch (Exception e) {
					System.out.println( " [ Error en UserEJB.editClientInformation. ] " );
					e.printStackTrace();
				}
			return false;
		}

		@Override
		public boolean editDataClient(Long userId, Long dataId, Long city, String userAdress, 
				String userPhone1, String userPhone2, String ip, Long creationUser) {
			 try {	
				 objectEM = new ObjectEM(em);			
					TbUserData data   = em.find(TbUserData.class, dataId); 
					TbSystemUser user = em.find(TbSystemUser.class, userId);
					TbMunicipality tm = em.find(TbMunicipality.class, city);
					
					if(userPhone2 != null && !userPhone2.equals("")){
						data.setUserDataOptionalPhone(userPhone2.trim());
					}else{
						data.setUserDataOptionalPhone("");
					}
					
					String message = "Se modificaron los datos del cliente con identificaci�n: " + user.getUserCode() + "- "
					+ user.getTbCodeType().getCodeTypeAbbreviation() + ". Valores Anteriores (email, direcci�n, cel, tel Opc, municipio Dpto) : "
					+ user.getUserEmail() + " ," + data.getUserDataAddress() + " ,"  
					+ data.getUserDataPhone() + " ," +  data.getUserDataOptionalPhone() +" ," + tm.getMunicipalityName().trim()+" "+tm.getTbDepartment().getDepartmentName()+".";
					
					data.setUserDataAddress(userAdress.trim());
					data.setUserDataPhone(userPhone1.trim());
					data.setTbMunicipality(em.find(TbMunicipality.class, city));
					
						// Updating the  Data
						if (objectEM.update(data)) {							
							//proceso administrador
							TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, userId);
							Long idPTA;
							if(pt==null){
								idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  userId, ip, creationUser);
							}
							else{
								idPTA=pt.getProcessTrackId();
							}
							Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.BANK_ACCOUNT_CLIENT, 
									     message, creationUser, ip, 1, "",null,null,null,null);						
							//proceso cliente
							TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,userId);
							Long idPTC;
							if(ptc==null){
								idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, userId, ip, creationUser);
							}
							else{
								idPTC=ptc.getProcessTrackId();
							}
							Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.BANK_ACCOUNT_CLIENT, 
									     message, creationUser, ip, 1, "",null,null,null,null);
							return true;
						} 
				} catch (Exception e) {
					System.out.println( " [ Error en UserEJB.editDataClient. ] " );
					e.printStackTrace();
					return false;
				}
			return false;
		}

		@Override
		public Long getSystemMasterById(long userId) {
			BigDecimal systemMasterById = null;
			try{
				Query q1= em.createNativeQuery("select system_user_master_id from tb_system_user where user_id=?1 ");
				systemMasterById = (BigDecimal) q1.setParameter(1, userId).getSingleResult();
				 if(systemMasterById== null){
						return userId;
				 }else{
					 return systemMasterById.longValue();
				 }
			}catch(Exception ex){
				ex.printStackTrace();

			}
			return systemMasterById.longValue();
		}
		
		
		@Override
		public String getSystemUserCode(long userId) {
			String systemCode = null;
			try{
				Query q1= em.createNativeQuery("select user_code from tb_system_user where user_id=?1 ");
				systemCode = (String) q1.setParameter(1, userId).getSingleResult();				
				}catch(Exception ex){
					ex.printStackTrace();
				}
			return systemCode;
		}		
		
		
		@Override
		public String getContractNP(String userCodeType, String userCode,
				String names, String lastNames, String address,
				String userCity, String userCountry, String userEmail) {
			System.out.println(userCodeType);
			System.out.println(userCode);
			System.out.println(names);
			System.out.println(lastNames);
			System.out.println(address);
			System.out.println(userCity);
			System.out.println(userCountry);
			System.out.println(userEmail);
			String query="select contract_text from tb_contracts where contract_id=1";
			String txt="";
			String muni="";
			try{
				String codeType=(String)em.createNativeQuery("select CODE_TYPE_DESCRIPTION from TB_CODE_TYPE " +
						"where CODE_TYPE_ID=?1").setParameter(1, userCodeType).getSingleResult();
				System.out.println(codeType);
				if(userCodeType.equals("2")){
					try{
						Long.parseLong(userCity);
						muni=(String)em.createNativeQuery("select MUNICIPALITY_NAME from TB_MUNICIPALITY " +
						"where MUNICIPALITY_ID=?1").setParameter(1, userCity).getSingleResult();
					}catch(Exception e){
						muni=userCity;
					}
				}else{
					muni=(String)em.createNativeQuery("select MUNICIPALITY_NAME from TB_MUNICIPALITY " +
					"where MUNICIPALITY_ID=?1").setParameter(1, userCity).getSingleResult();
				}
				System.out.println("muni:"+muni);
				Query q=em.createNativeQuery(query);
				Clob clob=(Clob) q.getSingleResult();
				txt=clob.getSubString(1, (int) clob.length());
				txt=txt.replace("�", "-");
				txt=txt.replace("�", "&quot;").replace("�", "&quot;");
				txt=txt.replace("<pre>", "<p>").replace("</pre>", "");
				System.out.println("txtN: "+txt);
				Date fecha=new Date();
				txt=txt.replace("{NOMBRE}", names+" "+lastNames).replace("{TIPOID}", codeType)
				.replace("{ID}", userCode).replace("{DIRECCION}", address).replace("{CIUDAD}", muni)
				.replace("{PAIS}", userCountry).replace("{MAIL}", userEmail)
				.replace("{A�O}", new SimpleDateFormat("yyyy").format(fecha))
				.replace("{MES}", new SimpleDateFormat("MMMM").format(fecha))
				.replace("{DIA}", new SimpleDateFormat("dd").format(fecha))
				.replace("{DIANOMBRE}", getNomDayByNum(Integer.parseInt(
						new SimpleDateFormat("dd").format(fecha))))
				.replace("{DIRFACILPASS}", contractEJB.getParameterValueById(25L));
			} catch (Exception e) {
				System.out.println(" [ Error en UserEJB.getContractNP ] ");
				e.printStackTrace();
				return null;
			}
			return txt;//.replaceAll("\\<.*?\\>", "");
		}
			
		@Override
		public String getContractLP(String userCodeType, String userCode,
				String names, String lastNames, String address,
				String userCity, String userCountry, String userEmail,
				String legalTypeCode, String legalCode,
				String countryConstitution) {
			System.out.println(userCodeType);
			System.out.println(userCode);
			System.out.println(names);
			System.out.println(lastNames);
			System.out.println(address);
			System.out.println(userCity);
			System.out.println(userCountry);
			System.out.println(userEmail);
			System.out.println(legalTypeCode);
			System.out.println(legalCode);
			System.out.println(countryConstitution);
			String query="select contract_text from tb_contracts where contract_id=2";
			String txt="";
			String muni="";
			try{
				muni=(String)em.createNativeQuery("select MUNICIPALITY_NAME from TB_MUNICIPALITY " +
					"where MUNICIPALITY_ID=?1").setParameter(1, userCity).getSingleResult();
				Query q=em.createNativeQuery(query);
				Clob clob=(Clob) q.getSingleResult();
				txt=clob.getSubString(1, (int) clob.length());
				txt=txt.replace("�", "-");
				txt=txt.replace("�", "&quot;").replace("�", "&quot;");
				txt=txt.replace("<pre>", "<p>").replace("</pre>", "");
				System.out.println("txtL: "+txt);
				Date fecha=new Date();
				txt=txt.replace("{NOMBRE}", names).replace("{IDLEGAL}", legalCode)
				.replace("{PAISRC}", countryConstitution).replace("{ID}", userCode)
				.replace("{CIUDAD}", muni).replace("{NOMLEGAL}", lastNames)
				.replace("{DIRECCION}", address).replace("{PAIS}", "COLOMBIA")
				.replace("{MAIL}", userEmail)
				.replace("{A�O}", new SimpleDateFormat("yyyy").format(fecha))
				.replace("{MES}", new SimpleDateFormat("MMMM").format(fecha))
				.replace("{DIA}", new SimpleDateFormat("dd").format(fecha))
				.replace("{DIANOMBRE}", getNomDayByNum(Integer.parseInt(
						new SimpleDateFormat("d").format(fecha))))
				.replace("{DIRFACILPASS}", contractEJB.getParameterValueById(25L));
			} catch (Exception e) {
				System.out.println(" [ Error en UserEJB.getContractLP ] ");
				e.printStackTrace();
				return null;
			}
			return txt;//.replaceAll("\\<.*?\\>", "");
		}

		
		@Override
		public TbAccount getAccountById(Long accountId) {
			TbAccount ta= em.find(TbAccount.class, accountId);
			return ta;
		}
		
		public String getNomDayByNum(int day){
			String salida="";
			switch(day) {
			 case 1: 
			     salida="uno";
			     break;
			 case 2: 
			     salida="dos";
			     break;
			 case 3: 
			     salida="tres";
			     break;
			 case 4: 
			     salida="Cuatro";
			     break;
			 case 5: 
			     salida="cinco";
			     break;
			 case 6: 
			     salida="seis";
			     break;
			 case 7: 
			     salida="siete";
			     break;
			 case 8: 
			     salida="ocho";
			     break;
			 case 9: 
			     salida="nueve";
			     break;
			 case 10: 
			     salida="diez";
			     break;
			 case 11: 
			     salida="once";
			     break;
			 case 12: 
			     salida="doce";
			     break;
			 case 13: 
			     salida="trece";
			     break;
			 case 14: 
			     salida="catorce";
			     break;
			 case 15: 
			     salida="quince";
			     break;
			 case 16: 
			     salida="diecis�is";
			     break;
			 case 17: 
			     salida="diecisiete";
			     break;
			 case 18: 
			     salida="dieciocho";
			     break;
			 case 19: 
			     salida="diecinueve";
			     break;
			 case 20: 
			     salida="veinte";
			     break;
			 case 21: 
			     salida="veintiuno";
			     break;
			 case 22: 
			     salida="veintid�s";
			     break;
			 case 23: 
			     salida="veintitr�s";
			     break;
			 case 24: 
			     salida="veinticuatro";
			     break;
			 case 25: 
			     salida="veinticinco";
			     break;
			 case 26: 
			     salida="veintis�is";
			     break;
			 case 27: 
			     salida="veintisiete";
			     break;
			 case 28: 
			     salida="veintiocho";
			     break;
			 case 29: 
			     salida="veintinueve";
			     break;
			 case 30: 
			     salida="treinta";
			     break;
			 case 31: 
			     salida="treintaiuno";
			     break;
			 default: 
			     salida="Otro n�mero";
			     break;
			 }
			return salida;
		}

		
		@SuppressWarnings({ "unused", "unchecked" })
		@Override
		public boolean activateClient(Long userId, String ip, Long creationUser) {
			Long saldoActual = null;

	        try {
				Query accounts = 
					 em.createNativeQuery("SELECT ta.account_id, td.device_id, ta.distribution_type_id FROM re_account_device red  " +
										  "INNER JOIN tb_device td ON red.device_id = td.device_id " +
								          "LEFT JOIN tag ON td.device_code = tag.equipmentobuid " +
										  "INNER JOIN tb_account ta ON red.account_id = ta.account_id " +
							              "WHERE ta.user_id = ?1 AND red.relation_state = 0 AND td.device_state_id = 7 " +
							              "AND ta.account_state_type_id = 0 ");
				    accounts.setParameter(1, userId);
				    List<Object []> account = (List<Object []>)accounts.getResultList();
				    
				    for (Object [] accountId : account){
						 if (accountId != null && accountId instanceof Object[]) {	
							 Long cuenta= Long.parseLong(accountId[0].toString());
							 Long deviceId= Long.parseLong(accountId[1].toString());
							 Long tipoCta= Long.parseLong(accountId[2].toString()); 
							 
							 Query query = em.createNativeQuery("SELECT MAX(Val_Min_Alarm) FROM Tb_Alarm_Balance " +
							 		                            "WHERE account_id = ?1 AND Id_Tip_Alarm = 4 ");
							 query.setParameter(1, cuenta);
							 Long umbralActual = ((BigDecimal) query.getSingleResult()).longValue();
							
							
							if(tipoCta == DistributionType.BAGMONEY.getDistributionID().intValue()){
								Query q = em.createNativeQuery("SELECT Account_Balance FROM Tb_Account WHERE Account_Id = ?1");
								q.setParameter(1, cuenta);								
								saldoActual = ((BigDecimal) q.getSingleResult()).longValue();
							}
							else if((tipoCta == DistributionType.MANUAL.getDistributionID().intValue()) || 
									(tipoCta == DistributionType.AUTOMATIC.getDistributionID().intValue())){
								
								Query q = em.createNativeQuery("SELECT Account_Balance FROM Tb_Account WHERE Account_Id = ?1");
								q.setParameter(1, cuenta);
								
								Long saldoCta = ((BigDecimal) q.getSingleResult()).longValue();
								
								Query qd = em.createNativeQuery("SELECT SUM(td.device_current_balance) "+
														  "FROM Tb_Device Td "+
														  "INNER JOIN Re_Account_Device Rad ON Rad.Device_Id = Td.Device_Id "+
														  "WHERE rad.account_id = ?1 ");
								qd.setParameter(1, cuenta);
								Long saldoDisp = ((BigDecimal) qd.getSingleResult()).longValue();								
								saldoActual = (saldoCta+saldoDisp);
							}
							
							 TbDevice td = em.find(TbDevice.class, deviceId);							
							 TbTag tt = em.find(TbTag.class, td.getDeviceCode());

							 if(saldoActual<=0){
								TbDeviceState tds = em.find(TbDeviceState.class, 3L);
								td.setTbDeviceState(tds);
								tt.setDeviceStateId(tds.getDeviceStateId());
							 }else if(saldoActual>0 && saldoActual<=umbralActual){
								TbDeviceState tds = em.find(TbDeviceState.class, 4L);
								td.setTbDeviceState(tds);
								tt.setDeviceStateId(tds.getDeviceStateId());
							 }else if(saldoActual >umbralActual){
								TbDeviceState tds = em.find(TbDeviceState.class, 6L);
								td.setTbDeviceState(tds);
								tt.setDeviceStateId(1L);
							 }
						 }
				    }
				    
				 TbSystemUser user = em.find(TbSystemUser.class, userId);				
				 user.setUserState(em.find(TbUserStateType.class, isClientActive(user)));
				 
				 Query roleId = em.createNativeQuery("SELECT role_id FROM re_user_role " +
													 "WHERE user_id = ?1 ");
				 roleId.setParameter(1, user.getUserId());
				 Long rol = ((BigDecimal) roleId.getSingleResult()).longValue();
				 if(rol == 5L){						 
					 Query sqlQuery = em.createNativeQuery("UPDATE re_user_role SET role_id=?1 WHERE user_id = ?2 ");
					 sqlQuery.setParameter(1, constant.Role.CLIENT.getId());
					 sqlQuery.setParameter(2, user.getUserId());
					 sqlQuery.executeUpdate();
					 
					 Query query = em.createNativeQuery("UPDATE (SELECT rur.role_id AS new_role, rur.role_id_old AS old_role, tsu.user_id " +
						 		"FROM re_user_role rur " +
						 		"INNER JOIN tb_system_user tsu ON tsu.user_id = rur.user_id " +
						 		"WHERE tsu.system_user_master_id = ?2) SET new_role = old_role, old_role = ?1 ");
					 query.setParameter(1, constant.Role.INACTIVEUSER.getId());
					 query.setParameter(2, user.getUserId());
					 query.executeUpdate();
				 }
				
				if (new ObjectEM(em).update(user)) {
					log.insertLog("Activar Usuario | usuario ID: " + user.getUserId(), LogReference.USER,
							LogAction.UPDATE, ip, creationUser);
					
					//proceso administrador
					TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, userId);
					Long idPTA;
					if(pt==null){
						idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  userId, ip, creationUser);
					}
					else{
						idPTA=pt.getProcessTrackId();
					}
					Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.BANK_ACCOUNT_CLIENT, 
							"Se activo el usuario No."+user.getUserCode()+ " Nombre: "+ user.getUserNames()+" "+user.getUserSecondNames()+".", userId, ip, 1, "",null,null,null,null);			
					//proceso cliente
					TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,userId);
					Long idPTC;
					if(ptc==null){
						idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, userId, ip, creationUser);
					}
					else{
						idPTC=ptc.getProcessTrackId();
					}
					Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.BANK_ACCOUNT_CLIENT, 
							"Se activo el usuario No."+user.getUserCode()+ " Nombre: "+ user.getUserNames()+" "+user.getUserSecondNames()+".", userId, ip, 1, "",null,null,null,null);
					
					return true;
				} else {
					log.insertLog("Activar Usuario | Error al activar usuario ID: " + user.getUserId(), LogReference.USER,
							LogAction.UPDATEFAIL, ip, creationUser);
					return false;
				}	
		   }catch(NoResultException e){
			   System.out.println(" [ Error UserEJB.activateClient ] ");
			   e.printStackTrace();
	     }
			return false;
		}

		@Override
		public ValidationPassword validationPassword(String userPassword, String userCode) {
			
			ValidationPassword valiPass = new ValidationPassword();
			
			valiPass.setValidation(true);
			
			//Boolean result = false;
			
			try {
				String validatePasswords = null,validateDictionary=null, validateSeq=null, validateComplex=null;
				//Query q= em.createNativeQuery("select system_parameter_value from tb_system_parameter where system_parameter_id in (13,14,15,17,18,19,32)");
				//List<Object> lis=q.getResultList();
				//validatePasswords=lis.get(2)!=null?lis.get(2).toString():"SI";
				validatePasswords = SystemParameters.getParameterValueById(15L);    		
				int min = 0, max = 0;
				String validateIdentificationPassword=null;
				/*if(lis!=null){
					min=lis.get(0)!=null?Integer.parseInt(lis.get(0).toString()):8;
					max=lis.get(1)!=null?Integer.parseInt(lis.get(1).toString()):10;
					validateIdentificationPassword=lis.get(1)!=null?lis.get(1).toString():"NO";
					validateDictionary=lis.get(2)!=null?lis.get(2).toString():"NO";
					validateSeq=lis.get(3)!=null?lis.get(3).toString():"NO";
					validateComplex=lis.get(4)!=null?lis.get(4).toString():"NO";
				}*/
				Query lengthMin =em.createQuery("select systemParameterValue from TbSystemParameter where systemParameterId in (13L)");
				Query lengthMax =em.createQuery("select systemParameterValue from TbSystemParameter where systemParameterId in (14L)");
	    		min = Integer.valueOf((String)lengthMin.getSingleResult());
				max = Integer.valueOf((String)lengthMax.getSingleResult());	
				validateIdentificationPassword = SystemParameters.getParameterValueById(17L);	    		
				validateDictionary = SystemParameters.getParameterValueById(18L);
				validateSeq = SystemParameters.getParameterValueById(19L);
				validateComplex = SystemParameters.getParameterValueById(32L);
				System.out.println("min " + min);
				System.out.println("max " + max); 
				
				boolean res=validateSize(userPassword, min, max);
				if(res){
					valiPass.setMessageValidate("La longitud de la contrase�a debe ser m�nimo de " + min +" caracteres y m�ximo de " +max);
					valiPass.setValidation(false);
					return valiPass;
				}
				System.out.println("validateIdentificationPassword fromoutside: " + validateIdentificationPassword);
				//System.out.println("userPassword fromoutside:" + userPassword);
			//	System.out.println("userCode fromoutside:" + userCode);
				if(validateIdentificationPassword.equals("NO")){
					Boolean res2=containIdentificationCod(userPassword, userCode);
					System.out.println("res2 de containIdentificationCod: " + res2);
					if(res2){
						valiPass.setMessageValidate("La contrase�a no puede contener su n�mero de identificaci�n");
						valiPass.setValidation(false);
						return valiPass;
					}
				}
				
				if(validateDictionary.equals("SI")){
					boolean res3=validatePasswordDictionary(userPassword);
					if(res3){
						valiPass.setMessageValidate("La contrase�a no est� permitida en el sistema, debido a que existe en el diccionario de contrase�as inv�lidas.");
						valiPass.setValidation(false);
						return valiPass;
					}
				}
				if(validateSeq.equals("SI")){
					String message1=validate1(userPassword);
					if(message1!=null){
						valiPass.setValidation(false);
						valiPass.setMessageValidate(message1);
						return valiPass;
					}
				}
				
				
				if(validateComplex.equals("SI")){
					boolean mayus=false, minus=false, especial=false, num=false;
					int tamanio=userPassword.length();
					char letra;
					int valascii = 0;
					for (int i=0; i<tamanio; i++){
						letra = userPassword.charAt(i);
						valascii = (int)letra;
						//System.out.println("Letra: "+letra+" Acsii: "+valascii);
						if(!mayus){
							if((valascii >= 65)&& (valascii <= 90)){
								mayus = true;
							}
						}
						
						if(!minus){
							if((valascii >= 97)&& (valascii <= 122)){
								minus = true;
							}
						}
						
						if(!num){
							if((valascii >= 48)&& (valascii <= 57)){
								num = true;
							}
						}
						
						if(!especial){
							if((valascii >= 33)&& (valascii <= 47) || 
									(valascii >= 58)&& (valascii <= 64) || 
									(valascii >= 91)&& (valascii <= 96) || 
									(valascii >= 123)&& (valascii <= 126)){
								especial = true;
							}
						}
					}
					if(mayus && minus && especial && num){
						valiPass.setValidation(true);	
						return valiPass;
					}else{
						valiPass.setMessageValidate("La contrase�a debe tener una combinaci�n de may�sculas, min�sculas, n�meros y s�mbolos.");					
						valiPass.setValidation(false);	
						return valiPass;
					}
					
				}else{
					System.out.println("No requiere validar la complejidad de la contrase�a");
					valiPass.setValidation(true);
					return valiPass;
				}
				
				//System.out.println("EJB/UserEJB validationPassword ejecutado correctamente!");
			} catch (Exception e) {
				e.printStackTrace();
				valiPass.setValidation(false);
				valiPass.setMessageValidate("La contrase�a no cumple con las caracteristicas requeridas!");
				return valiPass;
			}
		}
		
		
		
		@Override
		public boolean createUserRoleMaster(String userCode, String userCodeType, 
				String userNames, String userSecondNames, String userPhone1, 
				String userPhone2, String userAdress, String userCity, 
				String userEmail, long roleId, String ip, long creationUser,String foreignCity,String foreignCount) {
			
			    System.out.println("foreignCity:"+foreignCity);
			    System.out.println("foreignCount:"+foreignCount);
			    boolean result=false;
				String messageResp = null;
				objectEM = new ObjectEM(em);
			try {				    
				    TbSystemUser user = new TbSystemUser();
					user.setUserCode(userCode.trim());
					user.setTbCodeType(em.find(TbCodeType.class, Long.valueOf(userCodeType)));
					user.setUserNames(userNames.trim());
					user.setUserSecondNames(userSecondNames.trim());
					user.setUserEmail(userEmail.trim());
					user.setUserPassword(Encryptor.getEncrypt(userCode));
					user.setUserPwdDate(new Timestamp(System.currentTimeMillis()));
					user.setUserState(em.find(TbUserStateType.class, 0));
					user.setUserStateOld(em.find(TbUserStateType.class, 0));
					user.setUserRegistrationDate(new Timestamp(System.currentTimeMillis()));
					user.setUserFirstLogin(0);
					user.setSystemUserMasterId(getSystemMasterById(creationUser));
					user.setUserOtpState(1);
					user.setUserCountIntent(0);
					user.setUserCountQuestions(0);
					if (objectEM.create(user)) {// Insert the system log
						log.insertLog("Se ha creado un cliente C�digo: " + user.getUserCode() + "- "
								+ user.getTbCodeType().getCodeTypeDescription(), LogReference.CLIENT, LogAction.CREATE,
								ip, creationUser);
						
						if (user.getUserId()!=null && userCodeType.equals("1") || userCodeType.equals("2")){
							System.out.println("Se recibe Usuario natural aceptacion de Datos");
							//Se inserta 3 porque el usuario que debe alamcenar es usuario cliente segun la tabla tb_type_role
							Long idPolitica = dataPoliciesEJB.getIdHTML(3L);
							if(idPolitica > 0){
								dataPoliciesEJB.setCreatesPermission(creationUser,idPolitica,1L,ip,false,user.getUserId());
							}else {
								System.out.println("No se encontro Politica Generada");
							}
							
						}
						
						// Creating client Data
						TbUserData data = new TbUserData();
							data.setUserDataOfficeName(userNames + " " + userSecondNames);
							data.setUserDataContact(data.getUserDataOfficeName());
						
						
						data.setUserDataPhone(userPhone1);
						if(userPhone2 != null && !userPhone2.equals("")){
							data.setUserDataOptionalPhone(userPhone2);
						}else{
							data.setUserDataOptionalPhone("");
						}
						
						data.setUserDataAddress(userAdress);
						data.setTbMunicipality(em.find(TbMunicipality.class, Long.parseLong(userCity)));
						data.setTbSystemUser(user);
						data.setUserMainDependency(1L);
						
						if(!"".equals(foreignCity)){
							data.setForeignCity(foreignCity);
						}
						
						if(!"".equals(foreignCount)){
							data.setForeignCountry(foreignCount);
						}
						
						if (objectEM.create(data)) {
							log.insertLog("Se ha creado la dependencia al cliente C�digo: " + user.getUserCode() + "- "
									+ user.getTbCodeType().getCodeTypeDescription(), LogReference.CLIENTDATA, LogAction.CREATE,
									ip, creationUser);	
						}/* else {
							log.insertLog("Error al crear la dependencia del cliente C�digo: " + user.getUserCode() + "- "
									+ user.getTbCodeType().getCodeTypeDescription(), LogReference.CLIENTDATA, LogAction.CREATEFAIL,
									ip, creationUser);
						}*/
						
						// Assigning role to user
						Query query = em.createQuery("SELECT ro FROM TbRole ro WHERE ro.roleId = ?1");
					    query.setParameter(1, roleId);
					    TbRole role = (TbRole) query.getSingleResult();
					    
					    if (role != null) {
							ReUserRole reUserRole = new ReUserRole();
							reUserRole.setTbRole(role);
							reUserRole.setTbSystemUser(em.find(TbSystemUser.class, user.getUserId()));
							
							objectEM = new ObjectEM(em);	
							if(objectEM.create(reUserRole)){
							}
							
							log.insertLog(
									" Se ha asignado el rol "+ reUserRole.getTbSystemUser() + "al usuario con c�digo: "
											+ user.getUserCode() + "-"
											+ user.getTbCodeType().getCodeTypeDescription(),
									LogReference.ROLE, LogAction.CREATE, ip, creationUser);
						} /*else {
							log.insertLog(
									" No se pudo asignar el rol al usuario con c�digo: "
											+ user.getUserCode() + "-"
											+ user.getTbCodeType().getCodeTypeDescription(),
									LogReference.ROLE, LogAction.CREATEFAIL, ip, creationUser);
						}*/
						
						// Creating a message to track process of the client.
					    String message = "El usuario "+ user.getUserNames()+" " +user.getUserSecondNames() + 
					    ", fue creado satisfactoriamente con el perfil " + role.getRoleName();
					    
						if (user.getTbCodeType().getCodeTypeId() == 1
								|| user.getTbCodeType().getCodeTypeId() == 2) {
							message += ".";
						} 
						
						// Creating the process track, relation with the client
						Long proId = process.createProcessTrack(ProcessTrackType.CLIENT, user.getUserId(), ip, creationUser);
						Long proCliId = process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, user.getUserId(), ip, creationUser);
						
						// If the process track was created.
						if (proId != null) {// Creating the detail of the process.
							
							// Indicating that the potential client has been created. Process detail type = 1
							Long detailId = process.createProcessDetail(proId, ProcessTrackDetailType.CLIENT_CREATION, message, creationUser, ip, 0, 
									" No Se ha podido crear el detalle del proceso ID: " + 
									proId + ", Tipo detalle: 100.",null,null,null,null);
							
							TbProcessTrackDetailType dt = em.find(TbProcessTrackDetailType.class, ProcessTrackDetailType.CLIENT_CREATION.getId());
							
							// Task Creation.
							task.createTask(detailId, 0, new Timestamp(System.currentTimeMillis()), 
									TimeUtil.calculateAds(dt.getAdsTime()), dt.getDetailTypePriority(), 
									message,dt.getTbTaskType().getTaskTypeId(), creationUser, ip, null);
							
							//sendMail.sendMail(EmailType.CLIENT, user.getUserEmail(), EmailSubject.CLIENTCREATION, null);
							Long newOtp = this.numRad(user.getUserId());
							if(newOtp!= null){
								ArrayList<String> parameters=new ArrayList<String>();
								parameters.add("#OTP="+newOtp);
								outbox.insertMessageOutbox(user.getUserId(), 
						                   EmailProcess.CREATE_USER_CLIENT,
						                   null,
						                   null,
						                   null, 
						                   null,
						                   null,
						                   null,
						                   null,
						                   null,
						                   creationUser,
						                   null,
						                   null,
						                   null,
						                   true,
						                   parameters);
								
							}if (proCliId != null) {
								process.createProcessDetail(proCliId,
										ProcessTrackDetailType.MY_CLIENT_CREATION, message, creationUser, ip, 1,
										" No Se ha podido crear el detalle del proceso ID: "
												+ proId + ", Tipo detalle: 600.",null,null,null,null);
							}				
						} 
					}
				result=true;	
			} catch (Exception e) {
				messageResp="El usuario ya existe en el sistema.";
				System.out.println("Error en UserEJB.createUserRoleMaster ");
				e.printStackTrace();
				result=false;	
			} 
			return result;
		}

		@Override
		public List<TbSystemUser> getOnlyClients() {
			List<TbSystemUser> list = new ArrayList<TbSystemUser>();
			try {				
				
				Query q = em.createNativeQuery("Select distinct Tsu.user_id, Tsu.user_names "+ 
						"From Tb_System_User  Tsu, Re_User_Role Rur "+
						"Where Tsu.User_Id=Rur.User_Id "+
						"And Rur.Role_Id in (?1,?2) "+
						"And Tsu.user_id not in (select user_id from re_user_role where USER_ID in (select user_id from re_user_role where role_id in (?1,?2)) and ROLE_ID not in (?1,?2)) "+
						"And Tsu.User_State not in (-1) "+
						"order by Tsu.user_names");
				q.setParameter(1,constant.Role.PREENROLLED.getId());
				q.setParameter(2,constant.Role.CLIENT.getId());	
				List<Object[]> lis=q.getResultList();
				for (Object[] obj : lis) {
					list.add(em.find(TbSystemUser.class, Long.parseLong(obj[0]
							.toString())));
				}
			} catch (Exception e) {
				System.out.println(" [ Error en UserEJB.getAllActiveClient ] ");
				e.printStackTrace();
			}
			return list;
		}

		@Override
		public Long getSizePassword() {
			String maxSize=null;
			try {
				//maxSize = em.createNativeQuery("select SYSTEM_PARAMETER_VALUE from TB_SYSTEM_PARAMETER where SYSTEM_PARAMETER_ID=14").getSingleResult().toString();
				Query q =em.createQuery("select systemParameterValue from TbSystemParameter where systemParameterId in (14L)");
				maxSize = (String) q.getSingleResult();
				System.out.println("max longitud:"+maxSize);
			} catch (Exception e) {
				return 50l;
			}
			
			return new Long(maxSize);
		}
		@Override
		public boolean disableDevices(long idAccount,Long userId, String ip) {
			boolean respu=false;
			boolean upDevh=false;
			boolean upTag=false;
			Long idProc = 0L;
			TbAccount acon=em.find(TbAccount.class, idAccount);
			try{
				List<TbDevice> devices=device.getDevicesByAccount(idAccount);
				if(devices.size()>0){
					for(int i=0;i<devices.size();i++){
						System.out.println("Si tiene dispositivos");
						objectEM = new ObjectEM(em);
						TbDevice dev=devices.get(i);
						TbTag tag=null;
						Query q=em.createQuery("SELECT devh FROM TbDeviceHistory devh " +
								"WHERE devh.deviceId=?1");
						q.setParameter(1, dev);
						TbDeviceHistory devh=null;
						try{
							devh=(TbDeviceHistory) q.getSingleResult();
							upDevh=true;
						}catch(NoResultException nre){
							devh=new TbDeviceHistory();
							devh.setDeviceId(dev);
							upDevh=false;
						}
						System.out.println("+"+upDevh);
						devh.setBalanceDevice(dev.getDeviceCurrentBalance());
						devh.setLastStateDevice(dev.getTbDeviceState().getDeviceStateId());
						devh.setRegistrationDate(new Timestamp(System.currentTimeMillis()));
						dev.setTbDeviceState(em.find(TbDeviceState.class, 7L));
						if(!objectEM.update(dev)){
							respu=false;
							break;
						}else{
							respu=true;
						}
						try{
							tag=em.find(TbTag.class, dev.getDeviceCode());
							System.out.println("Existe el registro en tag");
							devh.setLastStateTag(tag.getDeviceStateId());
							tag.setDeviceStateId(2L);
							upTag=true;
						}catch(Exception e){
							Long valu=1L;
							Long clas=null;
							String plat=null;
							try{
								TbVehicle veh=null;
								Query q3=em.createQuery("SELECT DISTINCT rad.tbVehicle " +
										"FROM ReAccountDevice rad " +
										"WHERE rad.tbDevice=?1");
								q3.setParameter(1, dev);
								veh=(TbVehicle)q3.getSingleResult();
								clas=veh.getTbCategory().getCategoryId();
								plat=veh.getVehiclePlate();
							}catch(NoResultException n){
								System.out.println("No hay existe relaci�n con el veh�culo");
								clas=null;
								plat=null;
							}catch(Exception ex){
								clas=null;
								plat=null;
							}
							try{
								Query q2=em.createNativeQuery("select EQUIPMENTSTATUS " +
										"from (SELECT count(EQUIPMENTSTATUS) cantidad,EQUIPMENTSTATUS " +
										"from TAG where EQUIPMENTOBUID in " +
										"(select DISTINCT d.DEVICE_CODE " +
										"from TB_DEVICE d,RE_ACCOUNT_DEVICE rea " +
										"where d.DEVICE_CODE is not null " +
										"and d.DEVICE_ID=rea.DEVICE_ID " +
										"and rea.RELATION_STATE=0 " +
										"and rea.ACCOUNT_ID=?1) " +
										"group by EQUIPMENTSTATUS " +
										"order by cantidad desc) " +
										"where rownum<2");
								q2.setParameter(1, idAccount);
								valu = (Long.parseLong((String)
										q2.getSingleResult()));
							}catch(NoResultException n){
								valu=1L;
							}
							System.out.println("++"+valu);
							System.out.println("No existe el registro en tag");
							Query q1=em.createQuery("SELECT red.tbTagType.tagTypeCode " +
									"FROM ReDeviceTagType red " +
									"WHERE red.tbDevice=?1");
							q1.setParameter(1, dev);
							devh.setLastStateTag(valu);
							tag=new TbTag();
							tag.setDeviceTypeId(1L);
							tag.setCategoryId(clas);
							tag.setTagTypeCode(Long.parseLong((String)q1.getSingleResult()));
							tag.setAccountId(idAccount);
							tag.setContractualAuth(5L);
							tag.setDeviceCode(dev.getDeviceFacialId());
							tag.setDeviceCurrentBalance(new BigDecimal(0L));
							tag.setDeviceId(dev.getDeviceCode());
							tag.setDeviceStateId(2L);
							tag.setEquipmentClass("0");
							tag.setObeStatus("0");
							tag.setOctetstring("0");
							tag.setPaymentMeans("0");
							tag.setPaymentUnit("0");
							tag.setReceipData1("0");
							tag.setReceipData2("0");
							tag.setVehicleAuth("0");
							tag.setVehicleAxles("0");
							tag.setVehicleCharacteristics("0");
							tag.setVehicleDimensions("0");
							tag.setVehiclePlate(plat);
							tag.setVerificator("0");
							upTag=false;
						}
						if(upDevh){
							if(!objectEM.update(devh)){
								respu=false;
								break;
							}else{
								respu=true;
							}
						}else{
							if(!objectEM.create(devh)){
								respu=false;
								break;
							}else{
								respu=true;
							}
						}
						if(upTag){
							if(!objectEM.update(tag)){
								respu=false;
								break;
							}else{
								respu=true;
							}
						}else{	
							if(!objectEM.create(tag)){
								respu=false;
								break;
							}else{
								respu=true;
							}
						}
						if(respu==true){
								String message="Se ha desactivado el Dispositivo Electr�nico TAG " +
									"con ID Facial: "+dev.getDeviceFacialId()+" de la Cuenta FacilPass No. "
									+acon.getAccountId()+".";
								//Se crea el proceso de para Mi Proceso
								TbProcessTrack 	procDev = process.searchProcess(ProcessTrackType.DEVICE, dev.getDeviceId());				
								if(procDev != null){
									idProc = procDev.getProcessTrackId();
								} else {
									idProc = process.createProcessTrack(ProcessTrackType.DEVICE, dev.getDeviceId(), ip, userId);
								}
									
								if(idProc != 0L){			
									
									@SuppressWarnings("unused")
									Long detailIdClient = process.createProcessDetail(idProc, ProcessTrackDetailType.REQUEST_CLOSE_ACCOUNT, message, userId, ip, 1, 
											" No Se ha podido crear el detalle del proceso ID: " + 
											idProc + ", Tipo detalle:"+ProcessTrackDetailType.REQUEST_CLOSE_ACCOUNT.getId()+".",null,null,null,null);
									
									@SuppressWarnings("unused")
									TbProcessTrackDetailType dt = em.find(TbProcessTrackDetailType.class, ProcessTrackDetailType.REQUEST_CLOSE_ACCOUNT.getId());
									
									log.insertLog(message, LogReference.DEVICE, LogAction.CREATE,
											ip, userId);
								} else{
									log.insertLog(message, LogReference.DEVICE, LogAction.CREATEFAIL,
											ip, userId);
								}
							}
					}
					//respu=true;
				}else{
					System.out.println("No tiene dispositivos");
					respu=false;
				}
			}catch(Exception e){
				System.out.println(" [ Error en UserEJB.disableDevices ] ");
				e.printStackTrace();
				respu=false;
			}
			return respu;
		}

		@Override
		public boolean activeDevices(long idAccount,Long userId, String ip) {
			boolean respu=false;
			Long idProc = 0L;
			TbAccount acon=em.find(TbAccount.class, idAccount);
			try{
				List<TbDevice> devices=device.getDevicesByAccount(idAccount);
				if(devices.size()>0){
					for(int i=0;i<devices.size();i++){
						System.out.println("Si tiene dispositivos");
						objectEM = new ObjectEM(em);
						TbDevice dev=devices.get(i);
						TbTag tag=null;
						Query q=em.createQuery("SELECT devh FROM TbDeviceHistory devh " +
								"WHERE devh.deviceId=?1");
						q.setParameter(1, dev);
						TbDeviceHistory devh=null;
						try{
							System.out.println("Tiene historico de los dispositivos");
							devh=(TbDeviceHistory) q.getSingleResult();
							dev.setTbDeviceState(em.find(TbDeviceState.class, 
									devh.getLastStateDevice()));
							tag=em.find(TbTag.class, dev.getDeviceCode());
							tag.setDeviceStateId(devh.getLastStateTag());
							if(objectEM.update(dev)){
								if(objectEM.update(tag)){
									respu=true;
								}else{
									respu=false;
									break;
								}
							}else{
								respu=false;
								break;
							}
							if(respu==true){
								String message="Se ha activado el Dispositivo Electr�nico TAG " +
									"con ID Facial: "+dev.getDeviceFacialId()+" de la Cuenta FacilPass No. "
									+acon.getAccountId()+".";
								//Se crea el proceso de para Mi Proceso
								TbProcessTrack 	procDev = process.searchProcess(ProcessTrackType.DEVICE, dev.getDeviceId());				
								if(procDev != null){
									idProc = procDev.getProcessTrackId();
								} else {
									idProc = process.createProcessTrack(ProcessTrackType.DEVICE, dev.getDeviceId(), ip, userId);
								}
									
								if(idProc != 0L){			
									
									@SuppressWarnings("unused")
									Long detailIdClient = process.createProcessDetail(idProc, ProcessTrackDetailType.REACTIVATION_ACCOUNT, message, userId, ip, 1, 
											" No Se ha podido crear el detalle del proceso ID: " + 
											idProc + ", Tipo detalle:"+ProcessTrackDetailType.REACTIVATION_ACCOUNT.getId()+".",null,null,null,null);
									
									@SuppressWarnings("unused")
									TbProcessTrackDetailType dt = em.find(TbProcessTrackDetailType.class, ProcessTrackDetailType.REACTIVATION_ACCOUNT.getId());
									
									log.insertLog(message, LogReference.DEVICE, LogAction.CREATE,
											ip, userId);
								} else{
									log.insertLog(message, LogReference.DEVICE, LogAction.CREATEFAIL,
											ip, userId);
								}
							}
						}catch(NoResultException nre){
							System.out.println("No tiene registro historico para dispositivos");
							respu=false;
							break;
						}
					}
				}else{
					System.out.println("No tiene dispositivos");
					respu=false;
				}
				
			}catch(Exception e){
				System.out.println(" [ Error en UserEJB.activeDevices ] ");
				e.printStackTrace();
				respu=false;
			}
			return respu;
		}

		@Override
		public boolean intregateBalance(long idAccount,Long userId, String ip) {
			boolean respu=false;
			boolean upTag=false;
			Long idProc = 0L;
			TbAccount acon=em.find(TbAccount.class, idAccount);
			List<TbDeviceHistory> listDevh=new ArrayList<TbDeviceHistory>();
			listDevh=device.getDevicesHistoryByAccount(idAccount);
			int cantDevh=listDevh.size();
			
			try{
				List<TbDevice> devices=device.getDevicesByAccount(idAccount);
				if(devices.size()>0){
					System.out.println("Si tiene dispositivos");
					if(acon.getDistributionTypeId().getDistributionTypeId()==2||
							acon.getDistributionTypeId().getDistributionTypeId()==3){
						for(int i=0;i<devices.size();i++){
							objectEM = new ObjectEM(em);
							TbDevice dev=devices.get(i);
							Long general=acon.getAccountBalance().longValue();
							System.out.println("general1:"+general);
							System.out.println("disp"+dev.getDeviceCurrentBalance().longValue());
							general=general+dev.getDeviceCurrentBalance().longValue();
							System.out.println("generalF:"+general);
							acon.setAccountBalance(new BigDecimal(general));
							dev.setDeviceCurrentBalance(new BigDecimal(0L));
							TbTag tag=null;
							if(cantDevh<=0){
								System.out.println("No tiene registros en TB_DEVICE_HISTORY");
								dev.setTbDeviceState(em.find(TbDeviceState.class, 7L));
								try{
									tag=em.find(TbTag.class, dev.getDeviceCode());
									System.out.println("Existe el registro en tag");
									tag.setDeviceStateId(2L);
									upTag=true;
								}catch(Exception e){
									Long valu=1L;
									Long clas=null;
									String plat=null;
									try{
										TbVehicle veh=null;
										Query q3=em.createQuery("SELECT DISTINCT rad.tbVehicle " +
												"FROM ReAccountDevice rad " +
												"WHERE rad.tbDevice=?1");
										q3.setParameter(1, dev);
										veh=(TbVehicle)q3.getSingleResult();
										clas=veh.getTbCategory().getCategoryId();
										plat=veh.getVehiclePlate();
									}catch(NoResultException n){
										System.out.println("No hay existe relaci�n con el veh�culo");
										clas=null;
										plat=null;
									}catch(Exception ex){
										clas=null;
										plat=null;
									}
									try{
										Query q2=em.createNativeQuery("select EQUIPMENTSTATUS " +
												"from (SELECT count(EQUIPMENTSTATUS) cantidad,EQUIPMENTSTATUS " +
												"from TAG where EQUIPMENTOBUID in " +
												"(select DISTINCT d.DEVICE_CODE " +
												"from TB_DEVICE d,RE_ACCOUNT_DEVICE rea " +
												"where d.DEVICE_CODE is not null " +
												"and d.DEVICE_ID=rea.DEVICE_ID " +
												"and rea.RELATION_STATE=0 " +
												"and rea.ACCOUNT_ID=?1) " +
												"group by EQUIPMENTSTATUS " +
												"order by cantidad desc) " +
												"where rownum<2");
										q2.setParameter(1, idAccount);
										valu = (Long.parseLong((String)
												q2.getSingleResult()));
									}catch(NoResultException n){
										valu=1L;
									}
									System.out.println("++"+valu);
									System.out.println("No existe el registro en tag");
									Query q1=em.createQuery("SELECT red.tbTagType.tagTypeCode " +
											"FROM ReDeviceTagType red " +
											"WHERE red.tbDevice=?1");
									q1.setParameter(1, dev);
									tag=new TbTag();
									tag.setDeviceTypeId(1L);
									tag.setCategoryId(clas);
									tag.setTagTypeCode(Long.parseLong((String)q1.getSingleResult()));
									tag.setAccountId(idAccount);
									tag.setContractualAuth(5L);
									tag.setDeviceCode(dev.getDeviceFacialId());
									tag.setDeviceCurrentBalance(new BigDecimal(0L));
									tag.setDeviceId(dev.getDeviceCode());
									tag.setDeviceStateId(2L);
									tag.setEquipmentClass("0");
									tag.setObeStatus("0");
									tag.setOctetstring("0");
									tag.setPaymentMeans("0");
									tag.setPaymentUnit("0");
									tag.setReceipData1("0");
									tag.setReceipData2("0");
									tag.setVehicleAuth("0");
									tag.setVehicleAxles("0");
									tag.setVehicleCharacteristics("0");
									tag.setVehicleDimensions("0");
									tag.setVehiclePlate(plat);
									tag.setVerificator("0");
									upTag=false;
								}
							}
							if(objectEM.update(dev)){
								if(cantDevh<=0){
									System.out.println("No tiene registros en TB_DEVICE_HISTORY");
									if(upTag){
										if(!objectEM.update(tag)){
											respu=false;
											break;
										}else{
											respu=true;
										}
									}else{	
										if(!objectEM.create(tag)){
											respu=false;
											break;
										}else{
											respu=true;
										}
									}
								}
								if(objectEM.update(acon)){
									respu=true;
								}else{
									respu=false;
									break;
								}
							}else{
								respu=false;
								break;
							}
							if(respu==true){
								String message="Se ha reintegrado el saldo del Dispositivo Electr�nico TAG " +
									"con ID Facial: "+dev.getDeviceFacialId()+" a la Cuenta FacilPass No. "
									+acon.getAccountId()+".";
								//Se crea el proceso de para Mi Proceso
								TbProcessTrack 	procDev = process.searchProcess(ProcessTrackType.DEVICE, dev.getDeviceId());				
								if(procDev != null){
									idProc = procDev.getProcessTrackId();
								} else {
									idProc = process.createProcessTrack(ProcessTrackType.DEVICE, dev.getDeviceId(), ip, userId);
								}
									
								if(idProc != 0L){			
									
									@SuppressWarnings("unused")
									Long detailIdClient = process.createProcessDetail(idProc, ProcessTrackDetailType.CLOSE_ACCOUNT, message, userId, ip, 1, 
											" No Se ha podido crear el detalle del proceso ID: " + 
											idProc + ", Tipo detalle:"+ProcessTrackDetailType.CLOSE_ACCOUNT.getId()+".",null,null,null,null);
									
									@SuppressWarnings("unused")
									TbProcessTrackDetailType dt = em.find(TbProcessTrackDetailType.class, ProcessTrackDetailType.CLOSE_ACCOUNT.getId());
									
									log.insertLog(message, LogReference.DEVICE, LogAction.CREATE,
											ip, userId);
								} else{
									log.insertLog(message, LogReference.DEVICE, LogAction.CREATEFAIL,
											ip, userId);
								}
							}
						}
					}else{
						if(cantDevh<=0){
							System.out.println("No tiene registros en TB_DEVICE_HISTORY");
							if(devices.size()>0){
								for(int i=0;i<devices.size();i++){
									objectEM = new ObjectEM(em);
									TbDevice dev=devices.get(i);
									TbTag tag=null;
									dev.setTbDeviceState(em.find(TbDeviceState.class, 7L));
									if(objectEM.update(dev)){
										System.out.println("Actualizacion device");
									}else{
										System.out.println("No Actualizacion device");
									}
									try{
										tag=em.find(TbTag.class, dev.getDeviceCode());
										System.out.println("Existe el registro en tag");
										tag.setDeviceStateId(2L);
										upTag=true;
									}catch(Exception e){
										Long valu=1L;
										Long clas=null;
										String plat=null;
										try{
											TbVehicle veh=null;
											Query q3=em.createQuery("SELECT DISTINCT rad.tbVehicle " +
													"FROM ReAccountDevice rad " +
													"WHERE rad.tbDevice=?1");
											q3.setParameter(1, dev);
											veh=(TbVehicle)q3.getSingleResult();
											clas=veh.getTbCategory().getCategoryId();
											plat=veh.getVehiclePlate();
										}catch(NoResultException n){
											System.out.println("No hay existe relaci�n con el veh�culo");
											clas=null;
											plat=null;
										}catch(Exception ex){
											clas=null;
											plat=null;
										}
										try{
											Query q2=em.createNativeQuery("select EQUIPMENTSTATUS " +
													"from (SELECT count(EQUIPMENTSTATUS) cantidad,EQUIPMENTSTATUS " +
													"from TAG where EQUIPMENTOBUID in " +
													"(select DISTINCT d.DEVICE_CODE " +
													"from TB_DEVICE d,RE_ACCOUNT_DEVICE rea " +
													"where d.DEVICE_CODE is not null " +
													"and d.DEVICE_ID=rea.DEVICE_ID " +
													"and rea.RELATION_STATE=0 " +
													"and rea.ACCOUNT_ID=?1) " +
													"group by EQUIPMENTSTATUS " +
													"order by cantidad desc) " +
													"where rownum<2");
											q2.setParameter(1, idAccount);
											valu = (Long.parseLong((String)
													q2.getSingleResult()));
										}catch(NoResultException n){
											valu=1L;
										}
										System.out.println("++"+valu);
										System.out.println("No existe el registro en tag");
										Query q1=em.createQuery("SELECT red.tbTagType.tagTypeCode " +
												"FROM ReDeviceTagType red " +
												"WHERE red.tbDevice=?1");
										q1.setParameter(1, dev);
										tag=new TbTag();
										tag.setDeviceTypeId(1L);
										tag.setCategoryId(clas);
										tag.setTagTypeCode(Long.parseLong((String)q1.getSingleResult()));
										tag.setAccountId(idAccount);
										tag.setContractualAuth(5L);
										tag.setDeviceCode(dev.getDeviceFacialId());
										tag.setDeviceCurrentBalance(new BigDecimal(0L));
										tag.setDeviceId(dev.getDeviceCode());
										tag.setDeviceStateId(2L);
										tag.setEquipmentClass("0");
										tag.setObeStatus("0");
										tag.setOctetstring("0");
										tag.setPaymentMeans("0");
										tag.setPaymentUnit("0");
										tag.setReceipData1("0");
										tag.setReceipData2("0");
										tag.setVehicleAuth("0");
										tag.setVehicleAxles("0");
										tag.setVehicleCharacteristics("0");
										tag.setVehicleDimensions("0");
										tag.setVehiclePlate(plat);
										tag.setVerificator("0");
										upTag=false;
									}
									if(upTag){
										if(!objectEM.update(tag)){
											respu=false;
											break;
										}else{
											respu=true;
										}
									}else{	
										if(!objectEM.create(tag)){
											respu=false;
											break;
										}else{
											respu=true;
										}
									}
								}
							}
						}
						System.out.println("Tipo de distribuci�n bolsa de dinero");
						respu=false;
					}
				}else{
					System.out.println("No tiene dispositivos");
					respu=false;
				}
			}catch(Exception e){
				System.out.println(" [ Error en UserEJB.intregateBalance ] ");
				e.printStackTrace();
				respu=false;
			}
			return respu;
		}
		
		
    @SuppressWarnings("unchecked")
	public boolean isClient(Long idClient){
	    boolean res=false;
	    
	    try{
	    	Query q= em.createNativeQuery(" select user_role_id from re_user_role where user_id=?1 and role_id=3 ");
		    q.setParameter(1, idClient);
		    
		    List<BigDecimal> listIdUserRole= q.getResultList();
		    
		    if(listIdUserRole!=null){
		    	if(listIdUserRole.size()>0){
		    		res=true;
		    	}
		    	else{
		    		res=false;
		    	}
		    		
		    }
		    else{
		    	res=false;
		    }
	    }
	    catch(NoResultException e){
	    	res=false;
	    	e.printStackTrace();
	    }
	    
	    return res;
	}
    /** M�todo encargado de devolver el tipo de Rol Cliente Maestro o Cliente
	 * @author JGomez
	 */
    
    public Long getReTypeRoleNat(Long userId){
	      Long resp;
	    try{
	    	Query q= em.createNativeQuery("select tor.type_role_id from tb_system_user ts "+
	    			"inner join re_user_role ru on ru.user_id = ts.user_id "+
	    			"inner join tb_role tor on tor.role_id = ru.role_id "+
	    			"inner join tb_code_type tc on tc.code_type_id = ts.code_type_id "+
	    			"inner join tb_type_role tt on tt.type_role_id = tor.type_role_id "+
	    			"where tc.code_type_id in (1,2) "+
	    			"and tt.type_role_id <> 1 "+
	    			"and ru.user_id = ?1");
	    	q.setParameter(1, userId);
	    	resp = ((BigDecimal) q.getSingleResult()).longValue();
		    
		    if (resp==null){
				resp=0L;
			}
		    
	    }catch (NoResultException ex) {
			resp=0L;	
			System.out.println("Entre a NoResultexeption en UserEJB.getReTypeRole");
		}catch (Exception e) {
			resp=0L;
			System.out.println("Error en UserEJB.getReTypeRole");
			e.printStackTrace();
		}
		return resp;
	}
    
    
   
	public Long getReTypeRole(Long userId){
	      Long resp;
	    try{
	    	Query q= em.createNativeQuery("select min(tt.type_role_id) from tb_system_user ts " +
	    			"inner join re_user_role ru on ru.user_id = ts.user_id " +
	    			"inner join tb_role tor on tor.role_id = ru.role_id " +
	    			"inner join tb_code_type tc on tc.code_type_id = ts.code_type_id " +
	    			"inner join tb_type_role tt on tt.type_role_id = tor.type_role_id " +
	    			"and ru.user_id = ?1");
	    	q.setParameter(1, userId);	    	
	    	resp = ((BigDecimal) q.getSingleResult()).longValue();
		    
		    if (resp==null){
				resp=0L;
			}
		    
	    }catch (NoResultException ex) {
			resp=0L;	
			System.out.println("Entre a NoResultexeption en UserEJB.getReTypeRole");
		}catch (Exception e) {
			resp=0L;
			System.out.println("Error en UserEJB.getReTypeRole");
			e.printStackTrace();
		}
		System.out.println("Usuario getReTypeRole: "+userId);
		System.out.println("Respuesta getReTypeRole: "+resp);
		return resp;
	}
    
    /** M�todo encargado de devolver el ultimo producto bancario asociado
	 * @author ablasquez
	 * @return the reaccountbankid 
	 */
	public Long getProductByAccount(Long accountId) {
		try{
			System.out.println("accountId: "+accountId);
			Query q2 = em.createNativeQuery("Select rab.ACCOUNT_BANK_ID From VW_LAST_PRODUCT_BANK rab where rab.STATE_ACCOUNT_BANK <> 3 and rab.ACCOUNT_ID=?1 Order by rab.DATE_ASSOCIATION desc");
			q2.setParameter(1, accountId);
			
			
			BigDecimal idAccountBank = ((BigDecimal) q2.getSingleResult());
			
			if(idAccountBank== null){
				return null;
			}else{
				return idAccountBank.longValue();
			}
			
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Long getUserFromFiltersVehicle(Long codeType, String code,
			String firstName, String lastName,String email, String accountId) {
		String qry = "SELECT DISTINCT US.USER_ID " +
				"FROM TB_SYSTEM_USER US " +
				"INNER JOIN TB_CODE_TYPE TC " +
				"ON US.CODE_TYPE_ID=TC.CODE_TYPE_ID " +
				"INNER JOIN RE_USER_ROLE UR " +
				"ON US.USER_ID = UR.USER_ID " +
				"LEFT JOIN TB_ACCOUNT TA " +
				"ON TA.USER_ID=US.USER_ID " +
				"WHERE UR.ROLE_ID IN(2,3) ";
		String qryEnd="ORDER BY US.USER_ID";
		try {
			if(codeType!=0L&&codeType!=-1L){
				qry=qry+"AND US.CODE_TYPE_ID="+codeType+" ";
			}
			if(!code.equals("")){
				qry=qry+"AND US.USER_CODE like '%"+code+"%' ";
			}
			if(!firstName.equals("")){
				qry=qry+"AND UPPER(US.USER_NAMES) like '%"+firstName.toUpperCase()+"%' ";
			}
			if(!lastName.equals("")){
				qry=qry+"AND UPPER(US.USER_SECOND_NAMES) like '%"+lastName.toUpperCase()+"%' ";
			}
			if(!accountId.equals("")){
				qry=qry+"AND TA.ACCOUNT_ID like '%"+accountId+"%' ";
			}
			if(!email.equals("")){
				qry = qry+"AND LOWER(US.USER_EMAIL) LIKE '%"+email.toLowerCase()+"%' ";
			}
			
			System.out.println("qry4:"+qry+qryEnd);
			Query q=em.createNativeQuery(qry+qryEnd);
			
			Long cant=((BigDecimal) q.getSingleResult()).longValue();
			return cant;
		} catch (NonUniqueResultException e) {
			System.out.println(" [ Error NonUniqueResultException en UserEJB.getUserFromFiltersVehicle ] ");
			e.printStackTrace();
			return -2L;
		} catch (NoResultException e) {
			System.out.println(" [ Error NoResultException en UserEJB.getUserFromFiltersVehicle ] ");
			e.printStackTrace();
			return 0L;
		} catch (Exception e) {
			System.out.println(" [ Error en UserEJB.getUserFromFiltersVehicle ] ");
			e.printStackTrace();
			return -1L;
		}
	}
	
	/**
	 * Metodo encargado de cancelar las recargas programadas
	 * @author ablasquez
	 * @param accountId	 * 
	 */
	@Override
	public void cancelAutomaticRechage(Long accountId){
		try{
			Query q = em.createQuery("Select tar From TbAutomaticRecharge tar Where tar.accountId.accountId=?1 and estado=0");
			q.setParameter(1, accountId);
			for(Object obj: q.getResultList()){
				TbAutomaticRecharge tar = (TbAutomaticRecharge) obj;
				tar.setEstado(2);
				em.merge(tar);
				em.flush();
			}
			
		}catch(NoResultException e){
			System.out.println("la cuenta "+accountId+" no tiene recargas programadas");
		}
	}

	
	public Long getacceptanceofcontract (Long usrs){
		Long resp;
		System.out.println("Entre a UserEJB.getacceptanceofcontract");
		try{
			Query q = em.createNativeQuery("select count(*) from tb_accepts_contract where user_id = ?1");
			q.setParameter(1, usrs);
			resp = ((BigDecimal) q.getSingleResult()).longValue();
			
			if (resp == null){
				resp = 0L;
			}
			
			
		}catch(NoResultException e){
			resp = 0L;
		}
		
		return resp;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.User#getClientAccountPSE(java.lang.Long)
	 */
	@Override
	public List<TbAccount> getClientAccountWithoutPSE(Long clientId) {
		List<TbAccount> list = new ArrayList<TbAccount>();
		try {
			String query = "SELECT distinct rab.accountId.accountId FROM ReAccountBank rab "+
							"WHERE rab.bankAccountId.product.bankAval = 1 and rab.state_account_bank = 1 ";
			if (clientId != null) {
				query += "AND rab.accountId.tbSystemUser.userId =?1 " +
						"order by rab.accountId.accountId";
			}
			
			else{
				query += " ORDER BY rab.accountId.tbSystemUser.userId";
			}
			System.out.println("query: "+query);
			
			Query q = em.createQuery(query);
			
			if (clientId != null) {
				q.setParameter(1, clientId);
			}
			
			for (Object obj : q.getResultList()) {
				list.add(em.find(TbAccount.class, (Long) obj));
			}
		} catch (Exception e) {
			System.out.println("[ Error en UserEJB.getClientAccountWithoutPSE. ]");
			e.printStackTrace();
		}
		return list;
	}
	
	
	
	/**
	 * @see ejb.User#getClientAccountProgrammingAutomatic(Long)
	 */
	@Override
	public List<TbAccount> getClientAccountProgrammingAutomatic(Long clientId) {
		List<TbAccount> list = new ArrayList<TbAccount>();
		List<?> result;
		try {
			String query = " select distinct rab.account_id " + 
						   " from re_account_bank rab " +
						   " INNER JOIN TB_ACCOUNT an " +
						   " on rab.account_id = an.account_id " +
						   " LEFT JOIN TB_AUTOMATIC_RECHARGE ar " +
						   " on ar.account_id = rab.account_id " +
						   " where an.user_id =?1 " +
						   " and ar.estado = 1 " +
						   " and ar.type_programming_id in (1,2) " +
						   " and rab.account_id not in ( " +
						   	" select distinct rab.account_id " + 
						    " from re_account_bank rab " +
						    " INNER JOIN TB_ACCOUNT an " +
						   " on rab.account_id = an.account_id " + 
						   " LEFT JOIN TB_AUTOMATIC_RECHARGE ar " +
						   " on ar.account_id = rab.account_id " +
						   " where an.user_id =?1 " +
						   " and ar.ESTADO = 0 " +
						   " ) " +
						   " union " +
						   " select distinct rab.account_id " +
						   " from re_account_bank rab " +
						   " INNER JOIN TB_ACCOUNT an " +
						   " on rab.account_id = an.account_id " +
						   " where an.user_id =?1 " +
						   " and rab.account_id not in ( " +
						   " select distinct rab.account_id " +
						   " from re_account_bank rab " +
						   " INNER JOIN TB_ACCOUNT an " +
						   " on rab.account_id = an.account_id " + 
						   " LEFT JOIN TB_AUTOMATIC_RECHARGE ar " +
						   " on ar.account_id = rab.account_id " +
						   " where an.user_id =?1 " +
						   " and ar.ESTADO = 0 " +
						   " ) ";
			
						
			Query q = em.createNativeQuery(query);
						
			if (clientId != null) {
				q.setParameter(1, clientId);
			}
			
			result = q.getResultList();
			
			if(result != null){
				TbAccount tbAccount=null;
				for (Object o : result) {
					tbAccount = new TbAccount();					
					tbAccount = em.find(TbAccount.class,Long.parseLong(o.toString()));
					list.add(tbAccount);																
				}

			}

		} catch (Exception e) {
			System.out.println("[ Error en UserEJB.getClientAccountProgrammingAutomatic. ]");
			e.printStackTrace();
		}

		return list;
	}

	
	
	public boolean validatePseAccount(Long accountId) throws Exception{
		try{
			Query q = em.createNativeQuery("SELECT b.bank_aval " +
					"FROM re_account_bank rab,tb_bank_account ba,tb_bank b " +
					"WHERE rab.bank_account_id=ba.bank_account_id " +
					"AND ba.product=b.bank_id " +
					"AND rab.state_account_bank=1 " +
					"AND rab.account_id=?1");
			q.setParameter(1, accountId);
			Long isAval=((BigDecimal)q.getSingleResult()).longValue();
			if(isAval==1L){
				return false;
			}else{
				return true;
			}
		}catch (Exception e) {
			System.out.println("[ Error en UserEJB.validatePseAccount. ]");
			e.printStackTrace();
			throw e;			
		}
	}
	@Override
	public String getCityNameByID(Long cityId) {
		try{
			TbMunicipality c=em.find(TbMunicipality.class, cityId);
			return c.getMunicipalityName();
		}catch (Exception e) {
			System.out.println("[ Error en UserEJB.getCityNameByID. ]");
			e.printStackTrace();
			return "";
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.User#createAccount(java.lang.Long, java.lang.Long,
	 * java.lang.String)
	 */	
	
	@Override
	public Long createAccountCarril(Long idClient, Long creationUser, String ip) {
		try {
			Long type=50000L;
			// Searching the user/client.
			TbSystemUser u = em.find(TbSystemUser.class, idClient);
			if (u != null) {
				// Creating the account. 
				//Query qryd = em.createNativeQuery("select p.system_parameter_value from tb_system_parameter p where p.system_parameter_id in (7)");
				Query qryd =em.createQuery("select systemParameterValue from TbSystemParameter where systemParameterId in (7L)");	
			    String defaultDistribution = (String) qryd.getSingleResult();
			    
				TbAccount a = new TbAccount();
				a.setAccountBalance(new BigDecimal(0));
				a.setAccountOpeningDate(new Timestamp(System.currentTimeMillis()));
				a.setAccountState(AccountStateType.PENDING_FOR_FIRST_RECHARGE.getId());
				a.setTbSystemUser(u);
				TbDistributionType t = em.find(TbDistributionType.class, new Long(defaultDistribution));
				a.setDistributionTypeId(t);
				TbAccountType tc= em.find(TbAccountType.class, type);
				a.setTbAccountType(tc);
				TbSystemUser su =em.find(TbSystemUser.class, new Long(creationUser));
				su.setSystemUserMasterId(null);
				System.out.println("type" + type);
				a.setPreference("COMUN");
				objectEM = new ObjectEM(em);			
				if (objectEM.create(a)) {
					
					Query qryf = em.createNativeQuery("select system_parameter_value from tb_system_parameter where system_parameter_id in (6L)");
					String frecuency = (String) qryf.getSingleResult();
					
				    Query qry = em.createQuery("select p from TbSystemParameter p where p.systemParameterId in (4L,5L)");
				    
				    for(Object obj : qry.getResultList()) {
				    	TbSystemParameter parameter = (TbSystemParameter) obj;
				    	System.out.println("Nombre del Parametro: "+parameter.getSystemParameterName());
						System.out.println("valor: "+parameter.getSystemParameterValue());	    
			            TbAlarmBalance alarm = new TbAlarmBalance();
			    		alarm.setIdTipAlarm(parameter.getSystemParameterId());
						alarm.setValMinAlarm(new Long(parameter.getSystemParameterValue()));
			    		alarm.setFrequency(new Long(frecuency));
			    		alarm.setLastExecution(new Timestamp(System.currentTimeMillis()));
			    		alarm.setEstAlarm(1);
			    		alarm.setAccountId(a.getAccountId());
			    		alarm.setValminimo(0L);
			    		em.persist(alarm);
						em.flush();
				    }
					
					log.insertLog("Crear Cuenta Cliente | Se ha creado la cuenta ID: "
									+ a.getAccountId() + ", se ha asociado al cliente ID: "
									+ u.getUserId(), LogReference.ACCOUNT,
							LogAction.CREATE, ip, creationUser);
					
						TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, idClient);
						TbProcessTrack ptClient = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, idClient);
						Long client, cclient;
						if (pt == null) {
							client=process.createProcessTrack(ProcessTrackType.CLIENT, idClient, ip, creationUser);
							cclient=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, idClient, ip, creationUser);
						
						}
						else{
							client=pt.getProcessTrackId();
							cclient=ptClient.getProcessTrackId();
						}
						process.createProcessDetail(client, ProcessTrackDetailType.CLIENT_ACCOUNT_CREATION,
								"Se ha creado la Cuenta FacilPass No. "+a.getAccountId(), creationUser, ip, 1,
								"No se pudo registar en el proceso la creaci�n de la cuenta: "
								+ a.getAccountId() + ", ascociada al cliente ID: " + idClient + ". ID Proceso :"
								+ pt.getProcessTrackId()+".",null,null,null,null);
						process.createProcessDetail(cclient, ProcessTrackDetailType.CLIENT_ACCOUNT_CREATION,
								"Se ha creado la Cuenta FacilPass No. "+a.getAccountId(), creationUser, ip, 1,
								"No se pudo registar en el proceso la creaci�n de la cuenta: "
								+ a.getAccountId() + ", ascociada al cliente ID: " + idClient + ". ID Proceso :"
								+ ptClient.getProcessTrackId()+".",null,null,null,null);
						
					return a.getAccountId();
				} else {
					log.insertLog("Crear Cuenta Cliente | No se pudo crear la cuenta..",
							LogReference.ACCOUNT, LogAction.CREATEFAIL, ip, creationUser);
				}
			} else {
				log.insertLog("Crear Cuenta Cliente | No se encontr� el cliente/usuario.",
								LogReference.ACCOUNT, LogAction.NOTFOUND, ip, creationUser);
				    return -3L;
			}
		} catch (Exception e) {
			System.out.println(" [ Error en UserEJB.createAccountCarril ] ");
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.User#createClient(boolean, java.lang.Long, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String createClientCarril(Long creationUser, String ip,
			String userCodeType, String userCode, String names,
			String lastNames, String phone1, String phone2, String address,
			String userCity, String userEmail, String userPassword, String dv,
			String legalTypeId,String legalId,String foreignCity,String foreignCountry) { 
		boolean fromOutside=false;
		@SuppressWarnings("unused")
		long result = -1L; boolean res1=false, res2=false;
		boolean respcomplex = true;
		objectEM = new ObjectEM(em);
		String password=Encryptor.getEncrypt(userPassword);
		String messageResp = null;
		String validatePasswords = null;
		System.out.println("tipo codigo" + userCodeType);
		try {
			//Query q= em.createNativeQuery("select system_parameter_value from tb_system_parameter where system_parameter_id in (13,14,15,17,18,19,32)");
			//List<Object> lis=q.getResultList();
			//validatePasswords=lis.get(2)!=null?lis.get(2).toString():"SI";
			validatePasswords = SystemParameters.getParameterValueById(15L);
			System.out.println("respcomplex uc: "+respcomplex);
			if(res1==false && res2==false && respcomplex==true){
				//Encrypt client password
				TbSystemUser user = new TbSystemUser();
				if(userCodeType!=null){
					if(userCodeType.equals("3")){
						userCode=userCode+dv;
					}
				}
				
				
				System.out.println("userCode: " +userCode);
				user.setUserCode(userCode);
				user.setTbCodeType(em.find(TbCodeType.class, Long.valueOf(userCodeType)));
				user.setUserNames(names);
				user.setUserSecondNames(lastNames);
				user.setUserEmail(userEmail);
				user.setUserPassword(password);
				user.setUserPwdDate(new Timestamp(System.currentTimeMillis()));
				user.setUserState(em.find(TbUserStateType.class, UserState.PREENROLL.getId()));
				user.setUserStateOld(em.find(TbUserStateType.class, UserState.PREENROLL.getId()));
				user.setUserRegistrationDate(new Timestamp(System.currentTimeMillis()));
				user.setUserOtpState(1);
				if(fromOutside==true){
					user.setUserFirstLogin(1);
				}
				else{
					user.setUserFirstLogin(0);
				}
				
				
				if (objectEM.create(user)) {// Insert the system log
					
					try {
						log.insertLog("Se ha creado un cliente Codigo: " + user.getUserCode() + "- "
								+ user.getTbCodeType().getCodeTypeDescription(), LogReference.CLIENT, LogAction.CREATE,
								ip, creationUser);
					} catch (Exception e) {
						System.out.println("Se debe revisar tabla tb_log, problemas con secuencia.");
					}
					
					
					// Creating client Data
					TbUserData data = new TbUserData();
					
					if (userCodeType.equals("3")){ // If NIT
						data.setUserDataOfficeName(names);
						data.setUserDataContact(lastNames);
						data.setUserDataContactTypeId(Long.parseLong(legalTypeId));
						data.setUserDataContactId(Long.parseLong(legalId));
						data.setForeignCountry(foreignCountry);
					} else {
						data.setUserDataOfficeName(names + " " + lastNames);
						data.setUserDataContact(data.getUserDataOfficeName());
					}
					
					data.setUserDataPhone(phone1);
					if(phone2 != null && !phone2.equals("")){
						data.setUserDataOptionalPhone(phone2);
					}
					
					data.setUserDataAddress(address);
					data.setTbMunicipality(em.find(TbMunicipality.class, Long.parseLong(userCity)));
					if(userCity.equals("0")){
						data.setForeignCity(foreignCity);
						data.setForeignCountry(foreignCountry);
					}
					data.setTbSystemUser(user);
					data.setUserMainDependency(1L);
					if (objectEM.create(data)) {
						try {
							log
							.insertLog("Se ha creado la dependencia al cliente C�digo: " + user.getUserCode() + "- "
									+ user.getTbCodeType().getCodeTypeDescription(), LogReference.CLIENTDATA, LogAction.CREATE,
									ip, creationUser);
						} catch (Exception e) {
							System.out.println("Se debe revisar tabla tb_log, problemas con secuencia.");
						}
						
						
						if(validatePasswords.equals("SI")){
						    TbPasswordUser p= new TbPasswordUser();	
						    p.setPasswordNumber(password);
						    p.setDateCreation(new Timestamp(System.currentTimeMillis()));
						    p.setUser(user);
						    if(objectEM.create(p)){
						    	try {
						    		log.insertLog("Se ha creado contrase�a en historico de contrase�as del usuario: " + user.getUserCode() + "- "
											+ user.getTbCodeType().getCodeTypeDescription(), LogReference.CLIENT, LogAction.CREATE,
											ip, creationUser);
								} catch (Exception e) {
									System.out.println("Se debe revisar tabla tb_log, problemas con secuencia.");
								}
						        
						    }
						}
						
					} else {
						try {
							log.insertLog("Error al crear la dependencia del cliente C�digo: " + user.getUserCode() + "- "
									+ user.getTbCodeType().getCodeTypeDescription(), LogReference.CLIENTDATA, LogAction.CREATEFAIL,
									ip, creationUser);
						} catch (Exception e) {
							System.out.println("Se debe revisar tabla tb_log, problemas con secuencia.");
						}
						
					}
					
					// Assigning role = 'PRE-ENROLADO' to client
					if (role.createUserRole(user.getUserId(), constant.Role.PREENROLLED.getId().longValue())) {// Insert a system log of process
						try {
							log.insertLog(
									" Se ha asignado el rol PRE-ENROLADO al potencial cliente con c�digo: "
											+ user.getUserCode() + "-"
											+ user.getTbCodeType().getCodeTypeDescription(),
									LogReference.ROLE, LogAction.CREATE, ip, creationUser);
						} catch (Exception e) {
							System.out.println("Se debe revisar tabla tb_log, problemas con secuencia.");
						}
						
					} else {
						try {
							log.insertLog(
									" No se pudo asignar el rol PRE-ENROLADO al potencial cliente con c�digo: "
											+ user.getUserCode() + "-"
											+ user.getTbCodeType().getCodeTypeDescription(),
									LogReference.ROLE, LogAction.CREATEFAIL, ip, creationUser);
						} catch (Exception e) {
							System.out.println("Se debe revisar tabla tb_log, problemas con secuencia.");
						}
						
					}
					
					
					// Creating a message to track process of the client.
					String message = "Se ha Preinscrito el Cliente en el Sistema. En espera de la documentaci�n de: " + user.getUserNames();
					
					if (user.getTbCodeType().getCodeTypeId() == 1
							|| user.getTbCodeType().getCodeTypeId() == 2) {
						message += " " + user.getUserSecondNames();
					} else {
						message += " (Rept. Legal: " + user.getUserSecondNames() + ")"; 
					}
					
					// Creating the process track, relation with the client
					Long proId = process.createProcessTrack(ProcessTrackType.CLIENT, user.getUserId(), ip, creationUser);
					Long proCliId = process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, user.getUserId(), ip, creationUser);
					
					// If the process track was created.
					if (proId != null) {// Creating the detail of the process.
						
						// Indicating that the potential client has been created. Process detail type = 1
						Long detailId = process.createProcessDetail(proId, ProcessTrackDetailType.CLIENT_CREATION, message, creationUser, ip, 1, 
								" No Se ha podido crear el detalle del proceso ID: " + 
								proId + ", Tipo detalle: 100.",null,null,null,null);
						
						TbProcessTrackDetailType dt = em.find(TbProcessTrackDetailType.class, ProcessTrackDetailType.CLIENT_CREATION.getId());
						
						// Task Creation.
						task.createTask(detailId, 0, new Timestamp(System.currentTimeMillis()), 
								TimeUtil.calculateAds(dt.getAdsTime()), dt.getDetailTypePriority(), 
								message, 
								dt.getTbTaskType().getTaskTypeId(), creationUser, ip, null);
						
						
						if (proCliId != null) {
							process.createProcessDetail(proCliId,
									ProcessTrackDetailType.MY_CLIENT_CREATION, "Ha sido Preinscrito en el sistema FacilPass.", creationUser, ip, 1,
									" No Se ha podido crear el detalle del proceso ID: "
											+ proId + ", Tipo detalle: 600.",null,null,null,null);
						}				
					} 
					
					Query quc = em.createNativeQuery("SELECT su.user_id FROM Tb_system_user su WHERE su.user_id = ?1");
					quc.setParameter(1, user.getUserId());
					result = Long.parseLong(quc.getSingleResult().toString());
					messageResp="Registro Exitoso";
					return messageResp;
				} else {
					try {
						log
						.insertLog("Error al crear un cliente C�digo: " + user.getUserCode() + "- "
								+ user.getTbCodeType().getCodeTypeDescription(), LogReference.CLIENT, LogAction.CREATEFAIL,
								ip, creationUser);
					} catch (Exception e) {
						System.out.println("Se debe revisar tabla tb_log, problemas con secuencia.");
					}
					
				}
			}
		
		} catch (Exception e) {
			messageResp="Error en el sistema, por favor intente de nuevo.";
			System.out.println("Error en UserEJB.createClientCarril ");
			e.printStackTrace();
		} 
		return messageResp;
	}
	
	public void sendMessageOutboxLane(Long userId,Long accountId,Long deviceId,Long vehicleId,Long transactionId,Long creationUser){
		try{
			outbox.insertMessageOutbox(userId, 
		            EmailProcess.CREATE_CLIENT,
		            accountId,
		            null,
		            transactionId, 
		            deviceId,
		            vehicleId,
		            null,
		            null,
		            null,
		            creationUser,
		            null,
		            null,
		            null,
		            true,
				       null);
		}catch (Exception e) {
			System.out.println("Error en UserEJB.sendMessageOutboxLane ");
			e.printStackTrace();
		}
	}

	@Override
	public Long makeRechargeCarril(Long umbralId,Long userId,String ip,Long creatorUser) {
		try{
			Query q=em.createNativeQuery("SELECT f_recharge_other(?1,0,0) FROM dual");
			q.setParameter(1, umbralId);
			Long tranId=((BigDecimal) q.getSingleResult()).longValue();
			String msgProcess="";
			TbUmbral u =em.find(TbUmbral.class, umbralId);
			if(tranId!=null&&tranId>0L){
				msgProcess = "Asignaci�n de Recursos Inicial por valor de $"+new DecimalFormat("#,###,###,###").format(u.getUmbralValue().longValue())
				+" para la cuenta "+u.getTbAccount().getAccountId()+".";
			}else{
				tranId=-1L;
				msgProcess = "Asignaci�n de Recursos Inicial por valor de $"+new DecimalFormat("#,###,###,###").format(u.getUmbralValue().longValue())
				+" para la cuenta "+u.getTbAccount().getAccountId()+" rechazada.";
			}
			
			//creacion de proceso de cliente en Mi proceso
			TbProcessTrack idProc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, userId);
			Long newProc = null;
			if(idProc == null){
				newProc = process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, userId, ip, creatorUser);
			}else{
				newProc = idProc.getProcessTrackId();
			}
			process.createProcessDetail(newProc, ProcessTrackDetailType.MANUAL_RECHARGE,
					msgProcess, creatorUser, ip, 1, "Error al Crear Proceso para Recarga Manual de la cuenta "
					+u.getTbAccount().getAccountId(),null,null,null,null);
			
			//creacion de proceso de cliente
			TbProcessTrack idProcClient = process.searchProcess(ProcessTrackType.CLIENT, userId);
			Long newProcClient = null;
			if(idProcClient == null){
				newProcClient = process.createProcessTrack(ProcessTrackType.CLIENT, userId, ip, creatorUser);
			}else{
				newProcClient = idProcClient.getProcessTrackId();
			}
			process.createProcessDetail(newProcClient, ProcessTrackDetailType.MANUAL_RECHARGE,
					msgProcess, creatorUser, ip, 1, "Error al Crear Proceso para Recarga Manual de la cuenta "
					+u.getTbAccount().getAccountId(),null,null,null,null);
			
			return tranId;
		}catch (Exception e) {
			System.out.println("Error en UserEJB.makeRechargeCarril ");
			e.printStackTrace();
			return -1L;
		}
	}
	
	public boolean getPermission(Long userId, String namePerm) {
		Long result = 0L;
		boolean resp = false;
		try {

			System.out.println("getpermission EJB. Entre a Validar Permisos " + userId + "," + namePerm);
			Query q = em.createNativeQuery ("select count(*) from re_role_option_action rroa "+
					"inner join re_option_action roa on roa.option_action_id = rroa.option_action_id "+
					"inner join re_user_role ruo on rroa.role_id  = ruo.role_id  "+
					"inner join tb_system_user tu on tu.user_id = ruo.user_id "+
					"where tu.user_id = ?1 "+
					"and roa.behavior = ?2 "+
			"and roa.option_action_state = 2");
			q.setParameter(1, userId);
			q.setParameter(2, namePerm);
			result =((BigDecimal) q.getSingleResult()).longValue();
			System.out.println("getpermission EJB. result: "+result);
			if (result > 0){
				resp = true;
			}
		}catch(NoResultException ex){
			resp = false;
			System.out.println("Error en UserEJB.getPermission NoResultException ");
			ex.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
			resp = false;
			System.out.println("Error en UserEJB.getPermission ");
			e.printStackTrace();
		}
		return resp;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getContractNPExisting(Long idUser,Long typePerson) {
		System.out.println("Tipo de persona: " + typePerson+ ", UserId: "+ idUser);
		String respu = "";
		try {
			System.out.println("Entre a UserEJB.getContractNPExisting");
			List<TbUserData> list = new ArrayList<TbUserData>();
			
			Query q =em.createQuery ("select tud from TbUserData tud where tud.tbSystemUser.userId = ?1");
			q.setParameter(1, idUser);
			
			list = q.getResultList();
			
			
			if (list.size()>0){
				for (int i = 0; i < list.size(); i++) {
					TbUserData tud = list.get(i);
					if(typePerson==3){
						respu = getContractLP(tud.getTbSystemUser().getTbCodeType().getCodeTypeDescription(),tud.getTbSystemUser().getUserCode(),
								tud.getTbSystemUser().getUserNames(),tud.getTbSystemUser().getUserSecondNames(),tud.getUserDataAddress(),tud.getTbMunicipality().getMunicipalityId().toString(),
								tud.getForeignCountry(),tud.getTbSystemUser().getUserEmail(),tud.getUserDataContactTypeId().toString(),tud.getUserDataContactId().toString(),tud.getForeignCountry());
						
					}else {
						respu = getContractNP(tud.getTbSystemUser().getTbCodeType().getCodeTypeId().toString(), tud.getTbSystemUser().getUserCode(), 
								tud.getTbSystemUser().getUserNames(), tud.getTbSystemUser().getUserSecondNames(), tud.getUserDataAddress(), 
								tud.getTbMunicipality().getMunicipalityId().toString(),"COLOMBIA", tud.getTbSystemUser().getUserEmail());
					}
														
				}
				
			}else{
				System.out.println("No se encuentran los datos");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error en UserEJB.getContractNPExisting");
		}
		
		return respu;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.User#getLastAccountByUserId(java.lang.Long)
	 */
	@Override
	public Long getLastAccountByUserId(Long clientId) {
		try {
			String query = "SELECT account_id "+ 
						"FROM (SELECT account_id FROM tb_account "+
						"WHERE account_state_type_id NOT IN(2,3,4,5) "+
						"AND user_id=?1 ORDER BY account_opening_date DESC) "+
						"WHERE ROWNUM<2";
			Query q = em.createNativeQuery(query);
			q.setParameter(1, clientId);
			Long id=((BigDecimal) q.getSingleResult()).longValue();
			return id;
		} catch (Exception e) {
			System.out.println("[ Error en UserEJB.getLastAccountByUserId. ]");
			e.printStackTrace();
			return -1L;
		}
	}
		
	
	//Metodo que valida que la nueva contrase�a no tenga secuencias l�gicas de n�meros o letras y que no tenga caracteres repetidos consecutivamente
	public String logicalSequences(String newPassword, int questionId){
		String message = "";
		//System.out.println("[ newPassword  W3TWET]"+newPassword);
		//System.out.println("[ questionId ETETETRY]"+questionId);
		validatePassword= new ValidatePassword();
		if (validatePassword.validateContainRepeatNumPassword(newPassword.toUpperCase())==true){
			message="La respuesta de la pregunta "+ questionId +" no debe tener n�meros o caracteres repetidos de forma consecutiva.";
		}
		else if (validatePassword.validateContainSeqNumPassword(newPassword.toUpperCase())==true){
			message="La respuesta de la pregunta "+ questionId +" no debe tener una secuencia l�gica de n�meros.";
		}
		else if (validatePassword.validateContainSeqPassword(newPassword.toUpperCase())==true){
			message="La respuesta de la pregunta"+ questionId +" no debe tener una secuencia l�gica de letras.";
		}
		System.out.println("message1 " + message);
		return message;
	}

	@Override
	public ArrayList<HistoricalRecharges> getHistoricalRechargesByAccount(
			Long accountId) {
		ArrayList<HistoricalRecharges> r=new ArrayList<HistoricalRecharges>();
		List<?> result = null;
		try{
			String query="SELECT umbral_id,umbral_time,type_message,umbral_value, bank_name, state, umbral_channel,umbral_state " +
			"FROM vw_historical_recharges " +
			"WHERE ROWNUM<4 " +
			"AND account_id=?1 " +
			"AND umbral_state IN(1,2,9) " +
			"UNION " +
			"SELECT umbral_id,umbral_time,type_message,umbral_value, bank_name, state, umbral_channel,umbral_state " +
			"FROM vw_historical_recharges " +
			"WHERE account_id=?1 " +
			"AND umbral_state in (0,4,5)" +
			"and umbral_channel = 1 " +
			"ORDER BY umbral_time DESC";
			
			String query1="SELECT umbral_id,umbral_time,type_message,umbral_value, bank_name, state, umbral_channel,umbral_state " +
					"FROM vw_historical_recharges " +
					"WHERE ROWNUM<4 " +
					"AND account_id=?1 " +
					"AND umbral_state IN(1,2) " +
					"UNION " +
					"SELECT umbral_id,umbral_time,type_message,umbral_value, bank_name, state, umbral_channel,umbral_state " +
					"FROM vw_historical_recharges " +
					"WHERE account_id=?1 " +
					"AND umbral_state = 0 " +
					"and umbral_channel = 0 " +
					"ORDER BY umbral_time DESC ";
			
			if(validatePseAccount(accountId)){
				try{
					System.out.println("getHistoricalRechargesByAccount.query PSE: "+query);
					Query q=em.createNativeQuery(query);
					q.setParameter(1, accountId);
					result = q.getResultList();
				}catch (NoResultException e) {
					System.out.println("getHistoricalRechargesByAccount.query PSE ");
					e.printStackTrace();
				}
			}else{
				try{
					System.out.println("getHistoricalRechargesByAccount.query AVAL: " +query1);
					Query q=em.createNativeQuery(query1);
					q.setParameter(1, accountId);
					result = q.getResultList();
				}catch (NoResultException e) {
					System.out.println("getHistoricalRechargesByAccount.query Aval ");
					e.printStackTrace();
				}
			}
			int cont=0;
			if(result != null){
				HistoricalRecharges h=null;
				for (Object o : result) {
					if (o != null && o instanceof Object[]) {
						h=new HistoricalRecharges();
						Object[] obj = (Object[]) o;
						h.setUmbralId(((BigDecimal)obj[0]).longValue());
						h.setUmbralTime(((Timestamp) obj[1]));
						h.setTypeRecharge(((String) obj[2]));
						h.setUmbralValue(((BigDecimal)obj[3]).longValue());
						h.setRechargeBank(((String) obj[4]));
						h.setUmbralState(((String) obj[5]));
						h.setUmbralChannel(((BigDecimal)obj[6]).longValue());
						h.setUmbralStateId(((BigDecimal)obj[7]).longValue());
						h.setNumColm(cont);
						System.out.println(h.toString());
						r.add(h);
						cont++;
					}
				}
			}
		}catch (Exception e) {
			System.out.println("[ Error en UserEJB.getHistoricalRechargesByAccount. ]");
			e.printStackTrace();
		}
		return r;
	}

	@Override
	public boolean changeRoleUser(Long userId,Long roleId){
		boolean res=false;
		TbSystemUser u=null;
		try{
			u=em.find(TbSystemUser.class, userId);
			System.out.println("Entra validaci�n rol");
			Query q = em.createNativeQuery("SELECT user_role_id FROM re_user_role " +
				"WHERE user_id=?1 ");
			q.setParameter(1, u.getUserId());
			BigDecimal rol=(BigDecimal)q.getSingleResult();
			ReUserRole ru = em.find(ReUserRole.class, rol.longValue());
			if(!(ru.getTbRole().getRoleId()== roleId)){						
				TbRole r = em.find(TbRole.class, roleId);
				ru.setTbRole(r);
				em.merge(ru);
				em.flush();
			}
			System.out.println("Termina validaci�n rol cliente");
			res=true;
		}catch (Exception e) {
			System.out.println("No result exception con rol en cuenta");
			ReUserRole ur = new ReUserRole();
			ur.setTbRole(em.find(TbRole.class, roleId));
			ur.setTbSystemUser(u);
			em.persist(ur);
			em.flush();
		}
		return res;
	}
	/**
	 * M�todo encargado de validar si existe o no una cuenta para el usuario, 
	 * si existe no debe permitir crear una nueva al aceptar el contrato, 
	 * esto con un usuario nuevo, seg�n FRD127.
	 */
	@SuppressWarnings("null")
	public boolean verificationCta(Long userId){
		boolean ext = false;
		List<?> listAccounId;
		
		try{
			Query q = em.createNativeQuery("select account_id FROM tb_account WHERE user_id = ?1");
			q.setParameter(1, userId);
			listAccounId = (List<?>) q.getResultList();
			if(listAccounId==null){
				System.out.println("List Account NULL");
				ext = false;
			}else if(listAccounId.size() != 0){
				System.out.println("Se encontraron cuentas ya creadas para el Cliente");
				ext = true;
			}else{
				ext = false;
				
				/*outbox.insertMessageOutbox(u.getUserId(), 
		                  EmailProcess.ACCOUNT_CREATION,
		                  a.getAccountId(),
		                  null,
		                  null, 
		                  null,
		                  null,
		                  null,
		                  null,
		                  null,
		                  creationUser,
		                  null,
		                  null,
		                  null,
		                  true,
					       null);*/
				
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("verificationCta.ext: " + ext);
		return ext;
	}

	@Override
	public int getUserFirstLogin(Long userId) {
		TbSystemUser systemUser = em.find(TbSystemUser.class, userId);
		
		Query query = em.createNativeQuery("select USER_FIRST_LOGIN from tb_system_user where user_id=?1");
		query.setParameter(1, systemUser.getUserId());						
		int result =((BigDecimal)query.getSingleResult()).intValue();
		
		return result;
	}

	
	/**
	 * M�todo creado para restablecer las preguntas de seguridad->administrar-->link restablecer preguntas
	 * @author psanchez
	 */
	@Override
	public boolean resetSecurityQuestions(Long userId, String ip, Long creationUser) {
		try {
			TbSystemUser systemUser = em.find(TbSystemUser.class, userId);
			
			if(securityQuestionsEJB.validateQuestionsResponse(systemUser.getUserId())){
				systemUser.setUserFirstLogin(5);	
				if(systemUser.getUserState().getUserStateId()!=5L){//al restablecer preguntas de seguridad no debe cambiar el estado inactivo por admin
					systemUser.setUserState(em.find(TbUserStateType.class, isClientActive(systemUser)));
				}
				systemUser.setUserCountQuestions(0);
				systemUser.setUserLockDate(null);
				systemUser.setUserQuestionsDate(null);
				systemUser.setUserPwdExpiration(null);
				systemUser.setUserPwdDate(new Timestamp(System.currentTimeMillis()));
				
				Query query= em.createNativeQuery("UPDATE re_group_pass_question SET pass_state_id=3 WHERE user_id=?1 AND pass_group_id=5 ");
				query.setParameter(1, userId);
				query.executeUpdate();
				
				Query query1= em.createNativeQuery("UPDATE re_question_response SET state_relation=1 WHERE user_id=?1 ");
				query1.setParameter(1, userId);
				query1.executeUpdate();
				
				if (new ObjectEM(em).update(systemUser)) {
					log.insertLog("Restablecer Preguntas de Seguridad | ID Cliente: " + systemUser.getUserId(), LogReference.CLIENT,
							LogAction.UPDATE, ip, creationUser);
	
					outbox.insertMessageOutbox(systemUser.getUserId(), 
			                   EmailProcess.SECURITY_QUESTIONS,
			                   null,
			                   null,
			                   null, 
			                   null,
			                   null,
			                   null,
			                   null,
			                   null,
			                   creationUser,
			                   null,
			                   null,
			                   null,
			                   true,
			                   null);
	
					TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, userId);
					Long idPTA;
					if(pt==null){
						idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  userId, ip, creationUser);
					}
					else{
						idPTA=pt.getProcessTrackId();
					}
					Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.RESET_QUESTIONS, 
							"El restablecimiento de las preguntas de seguridad fue realizado satisfactoriamente.",
							creationUser, ip,1,"Error Restablecer Preguntas de Seguridad",null,null,null,null);			
					//proceso cliente
					TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,userId);
					Long idPTC;
					if(ptc==null){
						idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, userId, ip, creationUser);
					}
					else{
						idPTC=ptc.getProcessTrackId();
					}
					Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.RESET_PASSWORD, 
							"El restablecimiento de las preguntas de seguridad fue realizado satisfactoriamente.", 
							creationUser, ip, 1, "Error Restablecer Preguntas de Seguridad",null,null,null,null);
					
				} else {
					log.insertLog("Restablecer Preguntas Seguridad | Error - ID Cliente: " + systemUser.getUserId(), LogReference.CLIENT,
							LogAction.UPDATEFAIL, ip, creationUser);
				}
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			System.out.println("[ Ocurri� un error al restablecer las preguntas de seguridad del Cliente. ]");
			e.printStackTrace();
		}
		return false;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AllInfoClient> getInfoClientsByFilters(int page, int rows, int filtroId, String filtroDescription) {
		List<AllInfoClient> clients = new ArrayList<AllInfoClient>();
		String qry1, qry2, qry3 = "";

		try {
		    qry1="SELECT COUNT(*) FROM (";
			
			qry2="SELECT * FROM (";
			
			qry3="SELECT a.*, rownum rnum FROM (SELECT DISTINCT(tu.user_id), tu.user_code, tu.user_names, " +
				"tu.user_second_names, ta.account_id, To_Char(Tb.Vehicle_Plate), td.device_code, td.device_facial_id " +
				"FROM Tb_System_User tu " +
				"INNER JOIN Tb_Code_Type ct ON tu.code_type_id=ct.code_type_id " +
				"INNER JOIN Tb_User_State_Type us ON tu.user_state=us.user_state_id " +
				"INNER JOIN Re_User_Role rer ON tu.user_id=rer.user_id " +
				"INNER JOIN Tb_Role r ON rer.role_id=r.role_id " +
				"LEFT JOIN Tb_Account ta ON ta.user_id=tu.user_id " +
				"LEFT JOIN Re_Account_Device rad ON rad.Account_Id=Ta.Account_Id " +
				"LEFT JOIN Tb_Vehicle tb ON rad.Vehicle_Id=Tb.Vehicle_Id " +
				"LEFT JOIN Tb_Device td ON rad.device_id = td.device_id " +
				"WHERE r.role_id IN (2,3,5) ";
									
			if(!filtroDescription.equals("") && filtroId==1){
				qry3 = qry3+"AND tu.user_code like '%"+filtroDescription.trim()+"%' ";
			}
			if(!filtroDescription.equals("") && filtroId==2){
				qry3 = qry3+"AND Upper(tu.user_names) like '%"+filtroDescription.toUpperCase().trim()+"%' ";
			}
			if(!filtroDescription.equals("") && filtroId==3 ){
				qry3 = qry3+"AND Upper(tu.user_second_names) like '%"+filtroDescription.toUpperCase().trim()+"%' ";
			}
			if (!filtroDescription.equals("") && filtroId==4) {
				qry3 = qry3+"AND ta.account_id like '%"+filtroDescription.trim()+"%' ";
			}
			if(!filtroDescription.equals("") && filtroId==5){
				qry3 = qry3+"AND upper(tb.vehicle_plate) like '%"+filtroDescription.toUpperCase().trim()+"%' ";
			}
			if(!filtroDescription.equals("")&& filtroId==6){
				qry3 = qry3+"AND upper(td.device_code) like '%"+filtroDescription.toUpperCase().trim()+"%' ";
			}
			if(!filtroDescription.equals("") && filtroId==7){
				qry3 = qry3+"AND upper(td.device_facial_id) like '%"+filtroDescription.toUpperCase().trim()+"%' ";
			}
			if(!filtroDescription.equals("") && filtroId==8){
				qry3 = qry3+"AND upper(tb.aditional1) like '%"+filtroDescription.toUpperCase().trim()+"%' ";
			}
			if(!filtroDescription.equals("")&& filtroId==9){
				qry3 = qry3+"AND upper(tb.aditional2) like '%"+filtroDescription.toUpperCase().trim()+"%' ";
			}
			if(!filtroDescription.equals("") && filtroId==10){
				qry3 = qry3+"AND upper(tb.aditional3) like '%"+filtroDescription.toUpperCase().trim()+"%' ";
			}
			
			if(page!=0){
				qry3 = qry3+ "ORDER BY 3,4,5 desc) a WHERE rownum <= "+(page*rows)+") WHERE rnum > "+((page*rows)-rows)+" ";	
				Query query = em.createNativeQuery(qry2+qry3);

				List<Object> result= (List<Object>)query.getResultList();
				for(int i=0;i<result.size();i++){
					Object[] obj=(Object[]) result.get(i);
					AllInfoClient client = new AllInfoClient();	
					if(client!=null){
						client.setUserIdU(Long.parseLong(obj[0].toString()));
						client.setNumberDocU(obj[1].toString());
						client.setNameU(obj[2].toString());	
						client.setSecondNameU(obj[3].toString());	
						client.setAccountIdU(Long.parseLong(obj[4]!=null?obj[4].toString():"0"));
						client.setPlacaU(obj[5]!=null?obj[5].toString():"-");
						client.setTagIdU(obj[6]!=null?obj[6].toString():"-");	
						client.setFacialU(obj[7]!=null?obj[7].toString():"-");	
						clients.add(client);
					}
				}
			}
			else {
				Query query = em.createNativeQuery(qry1+qry3+")a )");
				clients= query.getResultList();
			}	
		} catch (Exception e) {
			System.out.println(" [ Error en UserEJB.getInfoClientsByFilters ] ");
			e.printStackTrace();
		}
		return clients;
	}

}