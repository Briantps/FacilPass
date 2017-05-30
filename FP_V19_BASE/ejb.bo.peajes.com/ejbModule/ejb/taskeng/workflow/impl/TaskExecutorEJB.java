package ejb.taskeng.workflow.impl;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TimedObject;
import javax.ejb.Timer;
import javax.ejb.TimerHandle;
import javax.ejb.TimerService;
import javax.mail.MessagingException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import util.ObjectRechage;
import util.TimeUtil;
import util.ws.WSRecharge;

import constant.EmailProcess;
import constant.EmailSubject;
import constant.EmailType;
import constant.LogAction;
import constant.LogReference;
import constant.ProcessTrackDetailType;
import constant.ProcessTrackType;
import crud.ObjectEM;

import jpa.TbAccount;
import jpa.TbAutomaticRecharge;
import jpa.TbAutomaticRechargeDeta;
import jpa.TbLog;
import jpa.TbProcessTrack;
import jpa.TbProcessTrackDetailType;
import jpa.TbResponseType;
import jpa.TbSystemParameter;
import jpa.TbTask;
import ejb.Log;
import ejb.SendMail;
import ejb.taskeng.workflow.TaskExecutor;
import ejb.taskeng.workflow.TaskFactory;
import ejb.taskeng.workflow.TaskOnExecution;
import ejb.taskeng.workflow.factory.XMLTaskFactory;
import jpa.TbUmbral;
import linews.InvokeSqdm;
import linews.RechargeClient;

import ejb.email.Outbox;
import ejb.process.ProcessAndTask;

@Stateless(mappedName = "ejb/TaskExecutor")
public class TaskExecutorEJB implements TaskExecutor, SessionBean, TimedObject {

	private static final long serialVersionUID = 1L;

	@PersistenceContext(unitName = "bo")
	private EntityManager em;	
	
	@SuppressWarnings("unused")
	private TaskFactory factory = XMLTaskFactory.getInstance();
	private SessionContext sc;
	
	@SuppressWarnings("unused")
	private InitialContext context;

	private TimerHandle timerHandle = null;
	private Timer timer;

	@EJB(mappedName = "ejb/TaskOnExecution")
	private TaskOnExecution taskOnExec;

	@EJB(mappedName = "ejb/sendMail")
	private SendMail mailHandler;
	
	@EJB(mappedName = "ejb/ProcessAndTask")
	private ProcessAndTask process;
	
	/*@EJB(mappedName = "ejb/TransitTask")
	private TransitTask tareaTrans;*/
	
	@EJB(mappedName ="ejb/sendMail")
	private SendMail sendMail;
	
	@EJB(mappedName ="ejb/email/Outbox")
	private Outbox outbox;
	
	@EJB(mappedName = "util/ws/WSRecharge")
	private WSRecharge WSrecharge;
	
	@EJB(mappedName="ejb/Log")
	private Log log;
	
	private ObjectEM emObj;
	
	public TaskExecutorEJB() {
	}

	@PostConstruct
	public void init() {
		try {
			System.out.println("TaskExecutor.init");
			context = new InitialContext();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void ejbTimeout(Timer timer) {
		System.out.println("Call to task executor : "+new Timestamp(System.currentTimeMillis()));
		
		/*Query latestTask = em.createNativeQuery("select ttask.task_id,tu.user_email,tbtt.task_type_name,decode(tbtt.task_type_message,null,'*',tbtt.task_type_message) message,ttask.task_data, ttask.task_Priority"+
						   	" from re_task_type_user ttu"+
							" inner join tb_task_type tbtt on ttu.task_type_id=tbtt.task_type_id"+
							" inner join tb_task ttask on ttask.task_type_id=tbtt.task_type_id"+
							" inner join tb_system_user tu on ttu.user_id=tu.user_id"+
							" where ttask.task_Close_Date is null"+ 
							" and ttask.task_Ads_Date > ?1"+
							" and ttask.task_Active = 0"+ 
							" Order By ttask.task_Priority desc");
		latestTask.setParameter(1,new Timestamp(System.currentTimeMillis()));					
		
		List<?> result = (List<?>)latestTask.getResultList();
		
		if(result != null){			
			for (Object s : result) {				
				if (s != null && s instanceof Object[]) {					
					Object[] datos = (Object[]) s;
					TbTask tarea = em.find(TbTask.class, ((BigDecimal)datos[0]).longValue());
					String msg ="";
					if(((String)datos[3]).equals("*") || ((String)datos[3]).equals("")){
						msg = (String)datos[4];
					} else if(((String)datos[3]).contains("#PARAMETRO#")){
						msg = (String)datos[3];
						msg = msg.replaceAll("#PARAMETRO#", (String)datos[4]);
					}
					try {
						mailHandler.sendMail((String)datos[1],
								"soporteservicio@facilpass.com",
								"FacilPass",
								(String) datos[2],
								(String)datos[5],
								msg);
						tarea.setTaskActive(true);
						em.merge(tarea);
						em.flush();
					} catch (MessagingException e) {
						System.out.println("Error al Enviar Correo: Tarea : "+tarea.getTaskId()+" | Cuenta : "+(String)datos[1]);
						e.printStackTrace();
					}
				}
			}
		}
				

		//System.out.println("Inicio Recarga Automatica : "+new Timestamp(System.currentTimeMillis()));
		//this.sendAutomaticRecharge();
		//System.out.println("Final Recarga Automatica : "+new Timestamp(System.currentTimeMillis()));
		//llamado a metodo de envio de faturas a cliente
		//System.out.println("Inicio Correo de Factura : "+new Timestamp(System.currentTimeMillis()));
		//this.sendMailInvoices();
		//System.out.println("Final Correo de Factura : "+new Timestamp(System.currentTimeMillis()));
		//llamado para alarma de saldo bajo por email
		//llamado de metodo para calculo de transacciones
		/*System.out.println("Inicio Correo Saldo Bajo : "+new Timestamp(System.currentTimeMillis()));
		this.sendMailBalanceLow();
		System.out.println("Final Correo Saldo Bajo : "+new Timestamp(System.currentTimeMillis()));
		*/
		
		
		
	}


	@Override
	public void ejbActivate() throws EJBException, RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void ejbPassivate() throws EJBException, RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void ejbRemove() throws EJBException, RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSessionContext(SessionContext sc) throws EJBException,
			RemoteException {
		this.sc = sc;
	}

	@Override
	public void createTimer() {
		try {
			TimerService ts = sc.getTimerService();
			if (timer == null) {
				timer = ts.createTimer(1, 60000, null);
				timerHandle = timer.getHandle();
				System.out.println("TaskExecutor timer started");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}

	public void cancelTimer(String timerName) {
		try {
			TimerService ts = sc.getTimerService();
			Iterator<?> it = ts.getTimers().iterator();
			while (it.hasNext()) {
				Timer myTimer = (Timer) it.next();
				if ((myTimer.getInfo().equals(timerName))) {
					myTimer.cancel();
				}
			}
		} catch (Exception e) {
			System.out.println("Exception after cancelling timer : "
					+ e.toString());
		}
		return;
	}

	public void getTimerInfo() {
		if (timerHandle != null) {
			Timer timer = timerHandle.getTimer();
			System.out.println("Timer information: " + timer.getInfo());
		}
	}

	public void setTaskOnExec(TaskOnExecution taskOnExec) {
		this.taskOnExec = taskOnExec;
	}

	public TaskOnExecution getTaskOnExec() {
		return taskOnExec;
	}
	
	
	/*public void sendMailBalanceLow(){
		Timestamp fecActual= new Timestamp(System.currentTimeMillis());
		System.out.println("ingreso a sendMailBalanceLow");
		Query q = em.createNativeQuery("select a.account_id," 
					+"	sum((case a.distribution_type_id "
					+"	when 1 then a.account_balance "
					+"	else d.device_current_balance "
					+"	end)) as balance,"
					+"	ab.val_min_alarm,"
					+"	ab.last_execution," 
					+"	ab.frequency,"
					+"	u.user_email"
					+"	from tb_account a"
					+"	inner join tb_alarm_balance ab on ab.account_id=a.account_id"
					+"	inner join re_account_device rad on rad.account_id=a.account_id"
					+"	inner join tb_device d on rad.device_id=d.device_id"
					+"	inner join tb_system_user u on a.user_id=u.user_id"
					+"	where ab.id_tip_alarm=4"
					+"	and ab.est_alarm=1"
					+"	group by a.account_id," 
					+"	ab.val_min_alarm,"
					+"	ab.last_execution, "
					+"	ab.frequency,"
					+"	u.user_email");
		
		List<?> result = (List<?>)q.getResultList();
		
		if(result != null){
			
			for (Object s : result) {
				
				if (s != null && s instanceof Object[]) {	
					
					Object[] datos = (Object[]) s;
					
					BigDecimal cta = (BigDecimal) datos[0];
					BigDecimal saldoAct = (BigDecimal) datos[1];
					BigDecimal valorMin = (BigDecimal) datos[2];					
					Timestamp fechaUltAct = (Timestamp) datos[3];
					BigDecimal frecuencia = (BigDecimal)datos[4];
					String correo = (String) datos[5];    
					
					//System.out.println("cuenta: "+cta);
					if((fecActual.getTime()-fechaUltAct.getTime())>=(frecuencia.intValue()*60000)){
						if(saldoAct.longValue()<= valorMin.longValue()){
							
							//mailHandler.sendMail(EmailType.CLIENT,correo, EmailSubject.CLIENT_ALARM_BALANCE, cta.toString()+ " es de "+saldoAct.toString()+". Su saldo es muy Bajo");
							/*outbox.insertMessageOutbox(ta.getTbSystemUser().getUserId(), 
					                   EmailProcess.LOW_BALANCE,
					                   accountId,
					                   null,
					                   null, 
					                   null,
					                   true);*/
							/*System.out.println("llamado de calculationThreshold");
							//this.calculationThreshold(cta.toString(),saldoAct, valorMin);
							Query updFec = em.createNativeQuery("update tb_alarm_balance set last_execution = ?1 where account_id = ?2");
					       	updFec.setParameter(1, fecActual);
					       	updFec.setParameter(2, cta.intValue());
					       	updFec.executeUpdate();
						}
					}				  					
				}
			}		
		}
		
	}*/
	
	/*public void calculationThreshold(String cta, BigDecimal saldoAct, BigDecimal valUmbralAct){
		Long promedio = 45L;
		Long diasCalculo = 15L;		
		BigDecimal limitValue = new BigDecimal(0);
		Long proId = 0L;
	  Date diaActual= new Date();
	  
	  Date fecha=new Date();
      SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
      String hoy = sdf.format(fecha);
      try {
		diaActual = sdf.parse(hoy);
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  
	  System.out.println("dia actual: "+diaActual);
	 // Query pr = em.createNativeQuery("select system_parameter_id, system_parameter_value from tb_system_parameter where system_parameter_id in (8,9,10)");
	  Query pr =em.createQuery("select p.systemParameterValue from TbSystemParameter p where p.systemParameterId in (8L,9L,10L)");
	  List<?> param = (List<?>)pr.getResultList();
	  if(param != null){		  
			for (Object s : param) {
				if (s != null && s instanceof Object[]) {	
					Object[] parametro = (Object[]) s;					
					if (((BigDecimal)parametro[0]).longValue() == 8L){
						promedio = Long.parseLong((String) parametro[1]);
					}
					
					if (((BigDecimal)parametro[0]).longValue() == 9L){
						diasCalculo = Long.parseLong((String) parametro[1]);
					}
					
					if (((BigDecimal)parametro[0]).longValue() == 10L){
						limitValue = new BigDecimal((String) parametro[1]);
					}
				}
			}
	  }
	  
	  Query qr = em.createNativeQuery("select max(umbral_time), umbral_state, umbral_id from tb_umbral where account_id=?1 group by umbral_state,umbral_id");
	  qr.setParameter(1,Integer.parseInt(cta));
      List<?> resultqr = (List<?>)qr.getResultList();
      
	   if(resultqr != null){
		  for (Object s : resultqr) {
				if (s != null && s instanceof Object[]) {	
					Object[] datos = (Object[]) s;
					Timestamp fec = (Timestamp) datos[0];
					@SuppressWarnings("unused")
					Date fecUmbral = new Date();
					String fecSql = sdf.format(fec);
				      try {
						fecUmbral = sdf.parse(fecSql);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					/*System.out.println("cuenta: "+Integer.parseInt(cta));
					System.out.println("diferencia: "+(diaActual.getTime()-fecUmbral.getTime()));
					System.out.println("promedio: "+((promedio-1)*86400000));
					*/
					/*int estado = ((BigDecimal)datos[1]).intValue();
					Long idUmbral = ((BigDecimal) datos[2]).longValue();
					
					//if(diaActual.getTime()-fecUmbral.getTime() >= ((promedio-1)*86400000)){
						//System.out.println("Ingreso para actualizacion de umbral");
						if(estado == 3){
							Query updEstOld = em.createNativeQuery("update tb_umbral set umbral_state=5 where umbral_id=?1");
							updEstOld.setParameter(1, idUmbral);							
							updEstOld.executeUpdate();
						}
	  
	  	
						Query q = em.createNativeQuery("select  cuenta, (ceil(avg(promedio_dia))) as promedio_total, (ceil(avg(promedio_dia))*?3) as valor_umbral, val_min_alarm as valor_actual, user_email"
									+" 	from (select a.account_id as cuenta, to_date(to_char(tt.transaction_time,'ddmmyy'),'ddmmyy') as fecha, ceil(avg(tt.transaction_value))  as promedio_dia,"
									+"    b.val_min_alarm,u.user_email"
									+"    from re_account_device rad"
									+"    inner join tb_account a on rad.account_id=a.account_id"
									+"    inner join tb_system_user u on a.user_id=u.user_id"
									+"    inner join tb_device td on rad.device_id=td.device_id"
									+"    inner join tb_transaction tt on tt.device_id = td.device_id"
									+"    inner join tb_device_type tdt on td.device_type_id=tdt.device_type_id"
									+"    inner join tb_alarm_balance b on b. account_id=rad.account_id"
									+"    and tt.account_id is null"
									+"    and b.id_tip_alarm = 4"
									+"    and tt.transaction_type_id=2"
									+"    and tdt.device_type_class=0"
									+"    and a.account_id=?4"
									+"    and to_date(to_char(tt.transaction_time,'ddmmyy'),'ddmmyy') between (?1-interval '"+promedio+"' day) and ?1"
									+"    group by a.account_id, to_date(to_char(tt.transaction_time,'ddmmyy'),'ddmmyy'), b.val_min_alarm, u.user_email"
									+"    order by a.account_id, to_date(to_char(tt.transaction_time,'ddmmyy'),'ddmmyy'))"
									+" group by cuenta, val_min_alarm, user_email"
									+" order by cuenta");
	  
						  q.setParameter(1,diaActual);	  
						  q.setParameter(3,diasCalculo);
						  q.setParameter(4,Integer.parseInt(cta));
						  
						  List<?> result = (List<?>)q.getResultList();
	  
						  TbAccount acount = em.find(TbAccount.class, new Long(cta));
						  ObjectEM emObj = new ObjectEM(em);
						  if(result != null){
							  for (Object s1 : result) {
									if (s1 != null && s1 instanceof Object[]) {	
										Object[] datos2 = (Object[]) s1;
										@SuppressWarnings("unused")
										BigDecimal valActual = (BigDecimal)datos2[3];
										BigDecimal valNuevo = (BigDecimal)datos2[2];
										if(valNuevo.longValue() > limitValue.longValue()){
											
											
											//Creacion de valor de Umbral
											/*TbUmbral umbral = new TbUmbral();
											umbral.setTbAccount(acount);
											umbral.setAverage((BigDecimal)datos2[1]);
											umbral.setUmbralValue(valNuevo);
											umbral.setUmbralTime(new Timestamp(System.currentTimeMillis()));
											umbral.setUmbralState(3L);
											umbral.setTypeMessageId(2L);
											*/
											
										/*	Long umbral = WSrecharge.createUmbral(
													   acount.getAccountId(), 
													   (BigDecimal)datos2[1], 
													   valNuevo, 
													   new Timestamp(System.currentTimeMillis()), 
													   3L, 
													   2L);
											
											if (umbral != null){
											   //Crea proceso y tarea
											   
												// Creating the process track, relation with the client
												TbProcessTrack proceso = process.searchProcess(ProcessTrackType.CLIENT,acount.getTbSystemUser().getUserId());
												if(proceso != null){
													proId = proceso.getProcessTrackId();
												} else {
												    proId = process.createProcessTrack(ProcessTrackType.CLIENT, acount.getTbSystemUser().getUserId(), "localhost", null);
												}							
												
												// If the process track was created.
												if (proId != null) {// Creating the detail of the process.
													String message = "Ha Cambiado el Valor del Umbral del Saldo Bajo del Cliente: "+ acount.getTbSystemUser().getUserNames()+" "+acount.getTbSystemUser().getUserSecondNames()+" Para la Cuenta Facil Pass No. "+acount.getAccountId();
													// Indicating that the potential client has been created. Process detail type = 1
													Long detailId = process.createProcessDetail(proId, ProcessTrackDetailType.CHANGE_LOW_BALANCE_THRESHOLD, message , 0L, "localhost", 0, 
															" El valor del umbral esta pendiente de Autorizacion");
																					
													TbProcessTrackDetailType dt = em.find(TbProcessTrackDetailType.class, ProcessTrackDetailType.CHANGE_LOW_BALANCE_THRESHOLD.getId());
													
													// Task Creation.
																					
													process.createTaskToProcess(detailId, 0, new Timestamp(System.currentTimeMillis()), 
															TimeUtil.calculateAds(dt.getAdsTime()), dt.getDetailTypePriority(), 
															message, 
															dt.getTbTaskType().getTaskTypeId(), 0L, "localhost", null);
												}
												
											}
											
										} else { //
											if(saldoAct.longValue() <= valNuevo.longValue()){
												
												//se crea registro en tabla tb_umbral con estado 0 pendiente de aplicacion							
												
												Long umbral = WSrecharge.createUmbral(
														   acount.getAccountId(), 
														   (BigDecimal)datos2[1], 
														   valNuevo, 
														   new Timestamp(System.currentTimeMillis()), 
														   0L, 
														   2L);
												
												
												/*TbUmbral umbral = new TbUmbral();
												umbral.setTbAccount(acount);
												umbral.setAverage((BigDecimal)datos2[1]);
												umbral.setUmbralValue(valNuevo);
												umbral.setUmbralTime(new Timestamp(System.currentTimeMillis()));
												umbral.setUmbralState(0L);
												umbral.setTypeMessageId(2L);
												*/
												
											/*	if (umbral != null){
													TbProcessTrack proceso = process.searchProcess(ProcessTrackType.CLIENT,acount.getTbSystemUser().getUserId());
													
													if(proceso != null){
														proId = proceso.getProcessTrackId();
													} else {
													    proId = process.createProcessTrack(ProcessTrackType.CLIENT, acount.getTbSystemUser().getUserId(), "localhost", null);
													}												
												
													
													// If the process track was created.
													if (proId != null) {// Creating the detail of the process.
														String message = "Ha Cambiado el Valor del Umbral del Saldo Bajo del Cliente: "+ acount.getTbSystemUser().getUserNames()+" "+acount.getTbSystemUser().getUserSecondNames()+" Para la Cuenta Facil Pass No. "+acount.getAccountId();
														// Indicating that the potential client has been created. Process detail type = 1
														@SuppressWarnings("unused")
														Long detailId = process.createProcessDetail(proId, ProcessTrackDetailType.CHANGE_LOW_BALANCE_THRESHOLD, message , 0L, "localhost", 0, 
																" El valor del umbral esta pendiente de Autorizacion");
													}	
												}
												
												boolean res = cancelRechargeUmbral();				
												if(res==true){
												//se crea tarea en tarea trans
												InvokeSqdm rechargeWS= new InvokeSqdm();
												Object[] obj =WSrecharge.createListObj(umbral);	
												if(obj != null){
												Long rs = rechargeWS.InvokeRecharge(obj);
												
												
												if(rs != null){
													TbLog log = new TbLog();
													TbResponseType resp = em.find(TbResponseType.class, rs);
													log.setLogMessage(resp.getResponseDescrip());
													log.setLogReference(LogReference.RECHARGESTATION.toString());
													log.setLogAction(LogAction.RECHARGE_MESSAGE.toString());
													log.setLogDate(new Timestamp(System.currentTimeMillis()));
													log.setLogIp("localhost");
													log.setUserId(0L);
													
													emObj.create(log);
												  }
												}
											    }
												
												/*tareaTrans.createTask(TypeTask.THRESHOLD, umbral.
														getUmbralId());*/
												
												
											/*	Query updUmbral = em.createNativeQuery("update tb_alarm_balance set val_min_alarm = ?1 where account_id = ?2 and id_tip_alarm=4");
												updUmbral.setParameter(1, valNuevo);
												updUmbral.setParameter(2, Integer.parseInt(cta));
												updUmbral.executeUpdate();
												//mailHandler.sendMail(EmailType.CLIENT,(String)datos2[4], EmailSubject.CLIENT_ALARM_BALANCE_UMBRAL, cta+", El Nuevo Valor Es: "+valNuevo.toString());
											} else {
												
												//se crea registro en tabla tb_umbral con estado 2 No requiere aplicacion
												/*TbUmbral umbral = new TbUmbral();
												umbral.setTbAccount(acount);
												umbral.setAverage((BigDecimal)datos2[1]);
												umbral.setUmbralValue(valNuevo);
												umbral.setUmbralTime(new Timestamp(System.currentTimeMillis()));
												umbral.setUmbralState(2L);
												umbral.setTypeMessageId(2L);
												*/
												
											/*	@SuppressWarnings("unused")
												Long umbral = WSrecharge.createUmbral(
														   acount.getAccountId(), 
														   (BigDecimal)datos2[1], 
														   valNuevo, 
														   new Timestamp(System.currentTimeMillis()), 
														   2L, 
														   2L);
												
												Query updUmbral = em.createNativeQuery("update tb_alarm_balance set val_min_alarm = ?1 where account_id = ?2 and id_tip_alarm=4");
												updUmbral.setParameter(1, valNuevo);
												updUmbral.setParameter(2, Integer.parseInt(cta));
												updUmbral.executeUpdate();
												
												//mailHandler.sendMail(EmailType.CLIENT,(String)datos2[4], EmailSubject.CLIENT_ALARM_BALANCE_UMBRAL, cta+", El Nuevo Valor Es: "+valNuevo.toString());
											}
										}//
									}
							  }
						  } 
	  
					//}
				}
		  }
	  }
	  System.out.println("fin calculationThreshold ");
	}*/
	

	@SuppressWarnings("unchecked")
	public void sendMailInvoices() {
		
		Timestamp today= new Timestamp(System.currentTimeMillis());
		Query q1= em.createNativeQuery("select t.frequency_equivalent from tb_frequency t where t.frequency_description='Diaria'");		
		
		//frecuencia en que se envian las facturas
		BigDecimal frequency=(BigDecimal) q1.getSingleResult();
		if(frequency==null){
		    frequency=new BigDecimal(86400);	
		}
		Query q2= em.createNativeQuery("select date_last_generation from tb_frequency_send_invoice");
		
		//fecha del ultimo envio de faturas
		Timestamp date=(Timestamp) q2.getSingleResult();
		
		if(today.getTime() - date.getTime() >= (frequency.longValue()*1000)){
			System.out.println("Entre al if");
			
			q2= em.createNativeQuery("select ti.invoice_id, ti.address_client, ti.type_invoice,  ti.account_id, ti.rut from tb_invoice ti "
                                     + " inner join tb_detail_invoice td on ti.invoice_id= td.invoice_id "
                                     + " where ti.send_state=0  group by ti.rut, ti.account_id, ti.address_client,ti.invoice_id ,ti.type_invoice");
                                     
			List<Object[]> lis=(List<Object[]>)q2.getResultList();
			for(Object[] o :lis){
				Long id= Long.parseLong(o[0].toString());
				String email=o[1].toString();
				Long type= Long.parseLong(o[2].toString());
				String account= o[3].toString();
				@SuppressWarnings("unused")
				String cod= o[4].toString();
				
				String message="Buen Dia, \r\n Se generó factura para su cuenta FacilPass Nro. " + account;
				if(type==1L){
				    message=message + " de tipo Recarga";	
				}
				else if(type==2L){
					message=message + " de tipo Descuento";	
				}
				else{
				}
				TbAccount ta = em.find(TbAccount.class, Long.parseLong(account));
				if(outbox.insertMessageOutbox(ta.getTbSystemUser().getUserId(), 
		                   EmailProcess.CREATE_INVOICE,
		                   Long.parseLong(account),
		                   null,
		                   null, 
		                   null,
		                   null,
		                   null,
		                   null,
		                   null,
		                   null,
		                   null,
		                   null,
		                   null,
		                   true,
		                   null)){
				
				//if(sendMail.sendMail(EmailType.CLIENT, email, EmailSubject.CLIENT_INVOICE,message)){
					q2= em.createNativeQuery("update tb_invoice set send_state=1 where invoice_id=?1");
					q2.setParameter(1, id);
					q2.executeUpdate();
					
				}
			}
			q2= em.createNativeQuery("update tb_frequency_send_invoice set date_last_generation=?1");
			q2.setParameter(1, today);
			q2.executeUpdate();
		}
	}
	
	

	@SuppressWarnings("unchecked")
	public String initRecharge(){
		try{
			Timestamp today= new Timestamp(System.currentTimeMillis());
			Query q1= em.createNativeQuery("select t.frequency_equivalent from tb_frequency t where t.frequency_description='Hora'");		
			@SuppressWarnings("unused")
			boolean res = false;
			String response = null;
		
			BigDecimal frequency=(BigDecimal) q1.getSingleResult();
			
			if(frequency==null){
			    frequency=new BigDecimal(3600000);	
			}
			Query q2= em.createNativeQuery("select date_last_recharge from tb_frequency_init_recharge");
			
			
			Timestamp date=(Timestamp) q2.getSingleResult();
			
			System.out.println("fecha actual init recharge" + today);
			System.out.println("fecha ult  init recharge" + date);
			
			if(today.getTime() - date.getTime() >= (frequency.longValue())){
				InvokeSqdm account2= new InvokeSqdm();
				System.out.println("Entre al if de recharge");
				
				try {
				q2= em.createNativeQuery("select umbral_id, umbral_value,account_id from tb_umbral where umbral_state=0 and umbral_value >0");
				System.out.println("select umbral");
	
							
				List<Object[]> lis=(List<Object[]>)q2.getResultList();
				System.out.println("select resultados");
				
				for(Object[] o :lis){
					try {
					BigDecimal umbral = (BigDecimal)o[0];
					BigDecimal valor = (BigDecimal)o[1];
					BigDecimal cuenta = (BigDecimal)o[2];
					
					System.out.println("umbral " + umbral);
					System.out.println("valor " + valor);
					System.out.println("cuenta " + cuenta);
					
					q2= em.createNativeQuery("select BOSID, BANKID, MESSAGETYPE, ITEMID, DOCUMENTTYPE, CLIENTID, STARTDATE, BANKNUMBER, BANKTYPE, AMOUNT, REVER  from table(f_recharge(?1))");
					q2.setParameter(1, umbral);
					Object[] ob= (Object[]) q2.getSingleResult();
					
					ObjectRechage obj = WSrecharge.createListObj(umbral.longValue());
					
					String data=account2.InvokeRecharge(obj);
					String array[]=data.split(";");
					Long resp=Long.valueOf(array[0]);
					
					System.out.println("Respuesta webservice recarga: " + resp);
				
					if(resp!=null){
						if(resp == 0L){
						    Query q3= em.createNativeQuery("select umbral_state from tb_umbral where umbral_id=?1");
						    q3.setParameter(1, umbral);
						
						    @SuppressWarnings("unused")
						    int stateUm = ((BigDecimal) q3.getSingleResult()).intValue();
						    if (stateUm == 1){
							    if(cuenta!=null){
								    TbAccount ta = em.find(TbAccount.class, cuenta.longValue());
								   // mailHandler.sendMail(EmailType.CLIENT,ta.getTbSystemUser().getUserEmail(), EmailSubject.RECHARGE_NOTIFICATION, "No. "+cuenta+". Valor Operacion "+valor+".");
							    }							
						    }
						}
						TbResponseType rt= em.find(TbResponseType.class, resp);
						if(rt!=null){
							response=rt.getResponseDescrip();
						}	
					}
					response="Respuesta de conexión y entrega de datos desde el webservices de recarga inicial para el itemId : " + umbral + " - " + response;
					System.out.println(response);
					log.insertLog(resp + " " + " "  + response, LogReference.BANKACCOUNT, LogAction.CREATE,null,null);
					} catch ( NoResultException ex ){
					System.out.println("error en consulta de recargas iniciales");
					//ex.printStackTrace();
					}
				}
				q2= em.createNativeQuery("update tb_frequency_init_recharge set date_last_recharge=?1");
				q2.setParameter(1, today);
				q2.executeUpdate();        
		}catch(NoResultException ex){
			//ex.printStackTrace();
			System.out.println("No se encontraron recargas iniciales pendientes por realizar");
		}
	}
		} catch(Exception ex){
			//ex.printStackTrace();
		}
		return null;
		
	}
	
	/**
	 * Método encargado de crear el proceso de recarga automática en las fechas programadas.
	 * @author psanchez
	 */
	public void sendAutomaticRecharge() {
	/*	String msgProcess = "";
		try {
			 int contador=0;
			 Timestamp fechaActual= new Timestamp(System.currentTimeMillis());
		     Query q1= 
			    em.createNativeQuery("SELECT tar.account_id, tar.automatic_recharge_value, tf.frequency_equivalent, tar.automatic_recharge_id " +
			    					 "FROM tb_automatic_recharge tar " +
			    					 "INNER JOIN tb_frequency tf ON tar.frecuency_id = tf.frequency_id " +
				                     "WHERE tar.estado=0 " +
	                                 "AND tf.frequency_state=1 " +
	                                 "AND tar.proceso=0 ");  
			List<?> result = (List<?>)q1.getResultList();
			
			 Query q3= 
				    em.createNativeQuery("SELECT count(tar.account_id) " +
				    					 "FROM tb_automatic_recharge tar " +
				    					 "INNER JOIN tb_frequency tf ON tar.frecuency_id = tf.frequency_id " +
					                     "WHERE tar.estado=0 " +
		                                 "AND tf.frequency_state=1 " +
		                                 "AND tar.proceso=0 ");
			 BigDecimal count= (BigDecimal) q3.getSingleResult();
			
	         if(result != null){	
				for (Object obje : result) {
					contador++;
					if (obje != null && obje instanceof Object[]) {		
						Object[] datos = (Object[]) obje;
					  if(datos != null){		
						 Long idClientAccount = ((BigDecimal) datos[0]).longValue();
						 BigDecimal vlrRecarga = (BigDecimal) datos[1];
						 BigDecimal freEquivalente = (BigDecimal) datos[2];	
						 BigDecimal automaticRechargeId = (BigDecimal) datos[3];
						 
					     Query lastExecutionD= 
							    em.createNativeQuery("SELECT max(tad.last_execution) " +
							    		"FROM tb_automatic_recharge_deta tad " +
							    		"INNER JOIN tb_automatic_recharge tar ON tar.last_execution = tad.last_execution " +
							    		"INNER JOIN tb_frequency tf ON tar.frecuency_id = tf.frequency_id " +
							    		"WHERE tar.automatic_recharge_id =?1 AND tar.estado=0 AND tar.proceso=0 ")
							    		.setParameter(1, automaticRechargeId);
					     
					     Timestamp lastExecutionDet = (Timestamp) lastExecutionD.getSingleResult();	     
					     long fechaActualMs = fechaActual.getTime();
					     try {	   
					    	 long lastExecutionDeta = lastExecutionDet.getTime();
						     long nuevaRecargaMs = lastExecutionDeta + freEquivalente.intValue();

						      if((lastExecutionDeta+nuevaRecargaMs> lastExecutionDeta) && (contador>0 && contador<=count.longValue())) {
							     if(fechaActualMs>=nuevaRecargaMs && lastExecutionDeta<nuevaRecargaMs){
								  TbAutomaticRechargeDeta tad = new TbAutomaticRechargeDeta();
								  TbAutomaticRecharge tar = em.find(TbAutomaticRecharge.class, ((BigDecimal)automaticRechargeId).longValue());
								  
								  tar.setLastExecution(fechaActual);
								  tar.setProceso(2);
							      tad.setAutomaticRechargeId(tar);
			                      tad.setLastExecution(tar.getLastExecution());		
			                      tad.setEstado(1);
								  
								  TbAccount ta=em.find(TbAccount.class, idClientAccount);	  					    
								   Long umbral = WSrecharge.createUmbral(
											   ta.getAccountId(), 
											   (BigDecimal)datos[3],  
											   vlrRecarga, 
											   new Timestamp(System.currentTimeMillis()), 
											   0L, 
											   1L);
									
									InvokeSqdm rechargeWS= new InvokeSqdm();
									Object[] obj = WSrecharge.createListObj(umbral);
									if(obj!=null){
									Long response = rechargeWS.InvokeRecharge(obj);
									String bankName = WSrecharge.getBankNameByAccount(idClientAccount);
									System.out.println("Entre response "+response);
									   if(response == 0L){
											//sendMail.sendMail(EmailType.CLIENT, user.getSystemUser(SessionUtil.sessionUser().getUserId()).getUserEmail(), EmailSubject.RECHARGE_NOTIFICATION, "Se ha recargado su cuenta FacilPass "+idClientAccount+", valor de la Recarga "+valueRechText);
											outbox.insertMessageOutbox(ta.getTbSystemUser().getUserId(), 
													                   EmailProcess.RESOURCE_ALLOCATION_SUCCESSFUL,
													                   idClientAccount,
													                   null, 
													                   null, 
													                   null,
													                   true);
											
											msgProcess = "Asignación de Recursos por valor de $"+new DecimalFormat("#,###,###,###").format(vlrRecarga)+" para la Cuenta FacilPass No."+idClientAccount+" aprobada por "+bankName;
											
										}else{
										//sendMail.sendMail(EmailType.CLIENT, user.getSystemUser(SessionUtil.sessionUser().getUserId()).getUserEmail(), EmailSubject.RECHARGE_NOTIFICATION, "Error al Recargar su cuenta FacilPass "+idClientAccount);
										outbox.insertMessageOutbox(ta.getTbSystemUser().getUserId(),
								                   EmailProcess.RESOURCE_ALLOCATION_REJECTED,
								                   idClientAccount,
								                   null, 
								                   null, 
								                   null,
								                   true);
										
										  msgProcess = "Asignación de Recursos por valor de $"+new DecimalFormat("#,###,###,###").format(vlrRecarga)+" para la Cuenta FacilPass No."+idClientAccount+" rechazada por "+bankName;
									    }
									}
									tar.setProceso(0);
				          			em.persist(tad);
									em.flush();
									emObj = new ObjectEM(em);
									if(emObj.create(tar)){
									log.insertLog("Asignación de Recursos Programados | Se ha Asignado Recursos Programados ID " + tar.getAutomaticRechargeId()+ ", para la Cuenta FacilPass No." + tar.getAccountId().getAccountId() + 
										          " por Valor de $"+ new DecimalFormat("#,###,###,###").format(vlrRecarga)+ " y Frecuencia " + tar.getFrequencyId().getFrequencyDescript() ,
										           LogReference.AUTOMATICRECHARGE, LogAction.CREATE, "localhost", 0L);
									
										 //proceso administrador
								        TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, ta.getTbSystemUser().getUserId());
										Long idPTA;
											if(pt==null){
												idPTA=process.createProcessTrack(ProcessTrackType.CLIENT, ta.getTbSystemUser().getUserId(), "localhost", ta.getTbSystemUser().getUserId());
											}else{
												idPTA=pt.getProcessTrackId();
											}
										Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.AUTHOMATHIC_RECHARGE, 
												     msgProcess+".", ta.getTbSystemUser().getUserId(), "localhost", 1, "");
										
										//proceso cliente
										TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, ta.getTbSystemUser().getUserId());
										Long idPTC;
											if(ptc==null){
												idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, ta.getTbSystemUser().getUserId(), "localhost", ta.getTbSystemUser().getUserId());
											}else{
												idPTC=ptc.getProcessTrackId();
											}
										Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.AUTHOMATHIC_RECHARGE, 
												     msgProcess+".", ta.getTbSystemUser().getUserId(), "localhost", 1, "");
									  }else {
										log.insertLog("Asignar Recursos Programados| No se ha creado Recursos Programados ID " + tar.getAutomaticRechargeId()+ ", para la Cuenta FacilPass No." + tar.getAccountId().getAccountId() + 
										              " por Valor de $" + new DecimalFormat("#,###,###,###").format(tar.getAutomaticRechargeValue())+ " y Frecuencia " + tar.getFrequencyId().getFrequencyDescript() ,
										               LogReference.AUTOMATICRECHARGE, LogAction.CREATE, "localhost", ta.getTbSystemUser().getUserId());
									   }
							      	}
						   		}						 	  
						      } catch (NullPointerException e){ 		   
						 		 Query lastExecutionD1= 
						 			 em.createNativeQuery("SELECT max(tar.last_execution) " +
								    		"FROM tb_automatic_recharge tar " +
					    					"LEFT JOIN tb_automatic_recharge_deta tad ON tar.last_execution = tad.last_execution " +
								    		"INNER JOIN tb_frequency tf ON tar.frecuency_id = tf.frequency_id " +
								    		"WHERE tar.automatic_recharge_id =?1 AND tar.estado=0 AND tar.proceso=0 ")
								    		.setParameter(1, automaticRechargeId);
						 		 
						 		 Timestamp lastExecutionDet1 = (Timestamp) lastExecutionD1.getSingleResult();	     
							     long lastExecutionDeta = lastExecutionDet1.getTime();
							     long nuevaRecargaMs = lastExecutionDeta + freEquivalente.intValue();

							      if((lastExecutionDeta+nuevaRecargaMs> lastExecutionDeta) && (contador>0 && contador<=count.longValue())) {
								     if(fechaActualMs>=nuevaRecargaMs && lastExecutionDeta<nuevaRecargaMs){
									  TbAutomaticRechargeDeta tad = new TbAutomaticRechargeDeta();
									  TbAutomaticRecharge tar = em.find(TbAutomaticRecharge.class, ((BigDecimal)automaticRechargeId).longValue());
									  
									  tar.setLastExecution(fechaActual);
									  tar.setProceso(2);
								      tad.setAutomaticRechargeId(tar);
				                      tad.setLastExecution(tar.getLastExecution());		
				                      tad.setEstado(1);
									  
									  TbAccount ta=em.find(TbAccount.class, idClientAccount);	  					    
									   Long umbral = WSrecharge.createUmbral(
												   ta.getAccountId(), 
												   (BigDecimal)datos[3],  
												   vlrRecarga, 
												   new Timestamp(System.currentTimeMillis()), 
												   0L, 
												   1L);
										
										InvokeSqdm rechargeWS= new InvokeSqdm();
										Object[] obj = WSrecharge.createListObj(umbral);
										if(obj!=null){
										Long response = rechargeWS.InvokeRecharge(obj);
										String bankName = WSrecharge.getBankNameByAccount(idClientAccount);
										System.out.println("Entre response "+response);
										   if(response == 0L){
												//sendMail.sendMail(EmailType.CLIENT, user.getSystemUser(SessionUtil.sessionUser().getUserId()).getUserEmail(), EmailSubject.RECHARGE_NOTIFICATION, "Se ha recargado su cuenta FacilPass "+idClientAccount+", valor de la Recarga "+valueRechText);
												outbox.insertMessageOutbox(ta.getTbSystemUser().getUserId(), 
														                   EmailProcess.RESOURCE_ALLOCATION_SUCCESSFUL,
														                   idClientAccount,
														                   null, 
														                   null, 
														                   null,
														                   true);
												
												msgProcess = "Asignación de Recursos por valor de $"+new DecimalFormat("#,###,###,###").format(vlrRecarga)+" para la Cuenta FacilPass No."+idClientAccount+" aprobada por "+bankName;
												
											}else{
											//sendMail.sendMail(EmailType.CLIENT, user.getSystemUser(SessionUtil.sessionUser().getUserId()).getUserEmail(), EmailSubject.RECHARGE_NOTIFICATION, "Error al Recargar su cuenta FacilPass "+idClientAccount);
											outbox.insertMessageOutbox(ta.getTbSystemUser().getUserId(),
									                   EmailProcess.RESOURCE_ALLOCATION_REJECTED,
									                   idClientAccount,
									                   null, 
									                   null, 
									                   null,
									                   true);
											
											  msgProcess = "Asignación de Recursos por valor de $"+new DecimalFormat("#,###,###,###").format(vlrRecarga)+" para la Cuenta FacilPass No."+idClientAccount+" rechazada por "+bankName;
										    }
										}
										tar.setProceso(0);
					          			em.persist(tad);
										em.flush();
										emObj = new ObjectEM(em);
										if(emObj.create(tar)){
										log.insertLog("Asignación de Recursos Programados | Se ha Asignado Recursos Programados ID " + tar.getAutomaticRechargeId()+ ", para la Cuenta FacilPass No." + tar.getAccountId().getAccountId() + 
											          " por Valor de $" + new DecimalFormat("#,###,###,###").format(vlrRecarga)+ " y Frecuencia " + tar.getFrequencyId().getFrequencyDescript() ,
											           LogReference.AUTOMATICRECHARGE, LogAction.CREATE, "localhost", 0L);
										
											 //proceso administrador
									        TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, ta.getTbSystemUser().getUserId());
											Long idPTA;
												if(pt==null){
													idPTA=process.createProcessTrack(ProcessTrackType.CLIENT, ta.getTbSystemUser().getUserId(), "localhost", ta.getTbSystemUser().getUserId());
												}else{
													idPTA=pt.getProcessTrackId();
												}
											Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.AUTHOMATHIC_RECHARGE, 
													     msgProcess+".", ta.getTbSystemUser().getUserId(), "localhost", 1, "");
											
											//proceso cliente
											TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, ta.getTbSystemUser().getUserId());
											Long idPTC;
												if(ptc==null){
													idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, ta.getTbSystemUser().getUserId(), "localhost", ta.getTbSystemUser().getUserId());
												}else{
													idPTC=ptc.getProcessTrackId();
												}
											Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.AUTHOMATHIC_RECHARGE, 
													     msgProcess+".", ta.getTbSystemUser().getUserId(), "localhost", 1, "");
										  }else {
											log.insertLog("Asignar Recursos Programados| No se ha creado Recursos Programados ID " + tar.getAutomaticRechargeId()+ ", para la Cuenta FacilPass No." + tar.getAccountId().getAccountId() + 
											              " por Valor de $" + new DecimalFormat("#,###,###,###").format(vlrRecarga)+ " y Frecuencia " + tar.getFrequencyId().getFrequencyDescript() ,
											               LogReference.AUTOMATICRECHARGE, LogAction.CREATE, "localhost", ta.getTbSystemUser().getUserId());
										   }
								      	}
							   		}
						 	  }
					 	  }

					  	} 		
			        } 
				}
		  } catch (NoResultException nr){
				System.out.println(" [ Error en TaskExecutorEJB.sendAutomaticRecharge ] ");
				nr.printStackTrace();
		  } */
	   }
	
	
	/**
	 * Método creado para cancelar el umbral de recargas.
	 * @return return true si realiza la recarga o false en otro caso.
	 * @author psanchez
	 */
	@Override
	public boolean cancelRechargeUmbral() {
		try {
			//Query q= em.createNativeQuery("select system_parameter_value from tb_system_parameter where system_parameter_id in (25)");
			Query q =em.createQuery("select systemParameterValue from TbSystemParameter where systemParameterId in (25L)");
			String sp = q.getSingleResult().toString();
			if(sp.equals("SI")) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			System.out.println(" [ Error en TaskExecutorEJB.cancelRechargeUmbral ] ");
			e.printStackTrace();
		}
		return false;
	}

  }