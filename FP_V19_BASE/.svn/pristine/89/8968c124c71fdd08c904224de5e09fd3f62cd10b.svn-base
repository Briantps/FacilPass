package ejb;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jpa.ReAccountDevice;
import jpa.TbAccount;
import jpa.TbAlarmBalance;
import jpa.TbCategory;
import jpa.TbDevice;
import jpa.TbMinimumBalanceState;
import jpa.TbProcessTrack;
import jpa.TbSystemUser;
import util.TbMinimumBalance;
import constant.EmailProcess;
import constant.LogAction;
import constant.LogReference;
import constant.ProcessTrackDetailType;
import constant.ProcessTrackType;
import crud.ObjectEM;
import ejb.email.Outbox;


@Stateless(mappedName = "ejb/MinimumBalance")
public class MinimumBalanceEJB implements MinimumBalance{
	
	@PersistenceContext(unitName = "bo") 
	EntityManager em;
	
	private ObjectEM objectEM;
	
	@EJB(mappedName ="ejb/email/Outbox")
	private Outbox outbox;
	@EJB(mappedName="ejb/Process")
	private Process process;
	@EJB(mappedName="ejb/Log")
	private Log log;
	@EJB(mappedName="ejb/Device")
	private Device deviceEJB;
	@EJB(mappedName="ejb/Vehicle")
	private Vehicle vehicleEJB;
	
	public boolean getpermission(Long userId, String nameperm) {
		long result = 0L;
		boolean resp = false;
		try {
			
			System.out.println("getpermission EJB. Entre a Validar Permisos " + userId + "," + nameperm);
			Query q = em.createNativeQuery ("select count(*) from re_role_option_action rroa "+
												"inner join re_option_action roa on roa.option_action_id = rroa.option_action_id "+
												"inner join re_user_role ruo on rroa.role_id  = ruo.role_id  "+
												"inner join tb_system_user tu on tu.user_id = ruo.user_id "+
												"where tu.user_id = ?1 "+
												"and roa.behavior = ?2 "+
												"and roa.option_action_state = 2");
			q.setParameter(1, userId);
			q.setParameter(2, nameperm);
			result =((BigDecimal) q.getSingleResult()).longValue();
				if (result > 0){
					resp = true;
				}
		}catch(NoResultException ex){
			resp = false;
		}catch(Exception e){
			e.printStackTrace();
			resp = false;
		}
		return resp;
				
		
	}



	@Override
	public ArrayList<TbMinimumBalance> getCategoryRates() {
		ArrayList<TbMinimumBalance> list = new ArrayList<TbMinimumBalance> ();
		//Se optienen las ultimas categoras asignadas
try {
			
			
			Query q= em.createNativeQuery("select tbm.category_id,tbm.category_value,tbm.minimum_balance_value,tbm.minimum_balance_date,tsu.user_email, " + 
												"tbm.minimum_balance_observ,tms.minimum_balance_state_descript,tbm.minimum_balance_id " +
												"from (select tca.category_id as categorymaxi,max(tbmb.minimum_balance_date) as fechamaxi from tb_minimum_balance tbmb " +
												"inner join tb_category tca on tbmb.category_id = tca.category_id " +
												"inner join tb_minimum_balance tbm on tbm.category_id = tca.category_id and tbmb.minimum_balance_date = tbmb.minimum_balance_date " +
												"group by tca.category_id  order by tca.category_id) " +
												"inner join tb_minimum_balance tbm on tbm.category_id = categorymaxi and tbm.minimum_balance_date = fechamaxi " +
												"inner join tb_minimum_balance_state tms on tms.minimum_balance_state_id = tbm.minimum_balance_state_id " +
												"inner join tb_system_user tsu on tsu.user_id = tbm.user_id " +
												"order by tbm.category_id");
			
			@SuppressWarnings("unchecked")
			List<Object> lis= (List<Object>)q.getResultList();
			System.out.println("Estoy llenando el objeto " + lis.size());
			for(int i=0;i<lis.size();i++){
				Object[] o=(Object[]) lis.get(i);
				TbMinimumBalance r= new TbMinimumBalance();
				
								
				r.setCategory(o[0]!=null?o[0].toString():"-");
				r.setCategoryvalue(formateador((o[1]!=null?((BigDecimal)o[1]):new BigDecimal(0))));
				r.setMinimumvalue(formateador((o[2]!=null?((BigDecimal)o[2]):new BigDecimal(0))));
				/*r.setCategoryvalue(o[1]!=null?o[1].toString():"-");*/
				/*r.setMinimumvalue(o[2]!=null?o[2].toString():"-");*/
				r.setDate (o[3]!=null?o[3].toString():"-");
				r.setUser(o[4]!=null?o[4].toString():"-");
				r.setObser(o[5]!=null?o[5].toString():"-");
				r.setState(o[6]!=null?o[6].toString():"-");
				r.setEditar("Editar");	
				r.setIdminimum(o[7]!=null?o[7].toString():"-");
				list.add(r);
				
							 
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.out.println("No se encontraron Categorias.");
		}
		System.out.println("list.size()----->"+list.size());
		
		return list;
	}
	
	
	private String formateador (BigDecimal number){
		 
		 String valueChargeTxt = null;
		 
		 if((number != null)){
			 DecimalFormat formateador = new DecimalFormat("###,###.##");
			 valueChargeTxt = formateador.format(number);
			 System.out.println("valueChargeTxt: "+valueChargeTxt);
			 valueChargeTxt = valueChargeTxt.replaceAll(",", ".");

		 }else {
			 valueChargeTxt = "0";
		 }
			 
		return valueChargeTxt;
		 
	 }
	
	public String getcreate(Long categoryId, String valor, String saldoMinimo,String observacion, Long userId,String ip){
		String respu = "";
		try {
			
			System.out.println("Entre a Insertar");
			
			jpa.TbMinimumBalance min = new jpa.TbMinimumBalance();
			min.setCategoryid(em.find(TbCategory.class,categoryId));
			min.setCategpryvalue(Long.parseLong(valor.replace(".", "")));
			min.setMinimumbalancevalue(Long.parseLong(saldoMinimo.replace(".", "")));
			min.setMinimumbalancedate(new Timestamp(System.currentTimeMillis()));
			min.setUserId(em.find(TbSystemUser.class,userId));
			min.setMinimumbalanceobserv(observacion);
			min.setMinimumbalancestateid(em.find(TbMinimumBalanceState.class,1L));
						
			
			
			objectEM = new ObjectEM(em);	
			if(objectEM.create(min)){
				
				
				try {
					ArrayList<String> parameters=new ArrayList<String>();
					parameters.add("#MINIID="+min.getMinimumbalanceid());
					parameters.add("#CATM="+min.getCategoryid().getCategoryId());
					parameters.add("#VALM="+min.getMinimumbalancevalue());
					parameters.add("#OBMIN="+min.getMinimumbalanceobserv());
					System.out.println("parameters: "+parameters.toString());
					outbox.insertMessageOutbox(userId,
							EmailProcess.MIN_BALANCE_MAGNAGEMENT, 
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
							"Creación de Saldo Mínimo", true,
							parameters);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Erro en MinimumBalanceEJB.getcreate en envio del correo");
				}
				
				
			log.insertLog("Saldo Minimo | Se ha creado la Categoria : " + min.getCategoryid() + 
			  " fecha" + min.getMinimumbalancedate(), 
			  LogReference.MINIMUMBALANCE, LogAction.CREATE, ip, userId);
			  respu = "Se ha registrado la Categoría " + categoryId + ", Por valor de $ " + valor;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			respu = "Error al crear la Categoría";
		}
		
		return respu;
	}
	
	public void createLog (){
		
		
		
	}
	
	public String getEdit(String valor, String saldoMinimo,
			String observacion, Long userId, String idMinimum, String ip){
			System.out.println("Entre a getEdit");
			String respu = "";
		try {
			
			jpa.TbMinimumBalance min = em.find(jpa.TbMinimumBalance.class, Long.parseLong(idMinimum));
			
			jpa.TbMinimumBalance min2 = new jpa.TbMinimumBalance();
			min2.setCategoryid(min.getCategoryid());
			min2.setCategpryvalue(Long.parseLong(valor.replace(".", "")));
			min2.setMinimumbalancevalue(Long.parseLong(saldoMinimo.replace(".", "")));
			min2.setMinimumbalancedate(new Timestamp(System.currentTimeMillis()));
			min2.setMinimumbalanceobserv(observacion);
			min2.setUserId(em.find(TbSystemUser.class,userId));
			min2.setMinimumbalancestateid(em.find(TbMinimumBalanceState.class,3L));
			
			
			objectEM = new ObjectEM(em);	
			if(objectEM.create(min2)){
				
				System.out.println("IdMin " +  min2.getMinimumbalanceid());				
				try {
					ArrayList<String> parameters=new ArrayList<String>();
					parameters.add("#MINIID="+min2.getMinimumbalanceid());
					parameters.add("#CATM="+min2.getCategoryid().getCategoryId());
					parameters.add("#VALM="+min2.getMinimumbalancevalue());
					parameters.add("#OBMIN="+min2.getMinimumbalanceobserv());					
					System.out.println("parameters: "+parameters.toString());
					outbox.insertMessageOutbox(userId,
							EmailProcess.MIN_BALANCE_MAGNAGEMENT, 
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
							"Edición de Saldo Mínimo", true,
							parameters);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Erro en MinimumBalanceEJB.getEdit en envio del correo");
				}
				
				
			log.insertLog("Saldo Minimo | Se ha editado la Categoria : " + min.getCategoryid() + 
			  " fecha" + min.getMinimumbalancedate(), 
			  LogReference.MINIMUMBALANCE, LogAction.UPDATE, ip, userId);
			
			  respu= "Categoría modificada con éxito";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			respu= "Error al editar la Categoría";
		}
		
		return respu;
	}



	@Override
	public String getapprovesCategory(String idMinimum, String saldoMinimo, Long userId, String ip) {

		System.out.println("Entre a EJB getapprovesCategory");
		Long balance;
		Long alarmBalance;
		String Resp;

		try {

			jpa.TbMinimumBalance min = em.find(jpa.TbMinimumBalance.class, Long.parseLong(idMinimum));

			min.setMinimumbalancestateid(em.find(TbMinimumBalanceState.class,2L));

			objectEM = new ObjectEM(em);
			if(objectEM.update(min)){

				log.insertLog("Saldo Minimo | Se ha aprobado la Categoria: " + min.getCategoryid().getCategoryId() + 
						" fecha: " + min.getMinimumbalancedate(), 
						LogReference.MINIMUMBALANCE, LogAction.UPDATE, ip, userId);
				
				Long account;
				Long RelationID;				
				List<BigDecimal> accountPSE;
				accountPSE = getAccountPSE();
				System.out.println("La Categoria que se aprobo es " + min.getCategoryid().getCategoryCode() + " Con el Min: " + min.getMinimumbalanceid());
				//Se recorre la lista de todas las cuentas PSE
				if (accountPSE.size()>0){
					System.out.println("Entre a Cuentas PSE. Se encontraron cuentas");
					for (int i = 0; i < accountPSE.size(); i++) {
						account = accountPSE.get(i).longValue();

						System.out.println("Cuenta PSE " + account);
						TbAccount ta = em.find(TbAccount.class,account);
						Long categoryClient = vehicleEJB.getMaxCategoryClient(account);
						//Se identifica si para la cuenta las maxima categoria es igual a la aprobada
						if (categoryClient.equals(min.getCategoryid().getCategoryId())){
																
							System.out.println("Entre a PSE Categoria Maxima Aprobada para la Cuenta: "+ account + " Categoria: " + categoryClient);
							//Se actualiza la tabla TB_ALARM_BALANCE de acuerdo a su nuevo valor por la categoria	
							setAlarmBalance(account,min.getMinimumbalanceid(),userId,ta.getTbSystemUser().getUserId(),ip);
							List<BigDecimal> relationDevices;							
							relationDevices = getRelationDevices(account);							
							Long stateDevice = deviceEJB.getListReplicationAvalState(account);						
							System.out.println("el estado para los registros de la cuenta: " + account + " ,es: " + stateDevice);
							
							if (relationDevices.size()>0){
								System.out.println("Entre. se encontraron dispositivos Asociados");
								for (int j = 0; j < relationDevices.size(); j++) {
									RelationID = relationDevices.get(j).longValue();
									System.out.println("Relacion PSE " + RelationID);
									ReAccountDevice re = em.find(ReAccountDevice.class, RelationID);
									TbDevice de = em.find(TbDevice.class, re.getTbDevice().getDeviceId());
									System.out.println("Dispositivo: " + re.getTbDevice().getDeviceCode());
									deviceEJB.setStateChange(re.getTbDevice().getDeviceId(),stateDevice);
									System.out.println("Dispositivo: " + de.getDeviceCode() + " Cambia al estado: " +  stateDevice);

								}
								if (stateDevice==3L) {
									ta.setWithoutBalance(1L);
								}else{
									ta.setWithoutBalance(0L);
								}

								em.merge(ta);
								em.flush();								

							}else{
								System.out.println("No se encontraron dispositivos Activos");
							}
							
						}else{
							System.out.println("No se actualiza la Cuenta ya que no corresponde a la Aprobada");
						}
						
					}
					
					try {
						ArrayList<String> parameters=new ArrayList<String>();
						parameters.add("#MINIID="+min.getMinimumbalanceid());
						parameters.add("#CATM="+min.getCategoryid().getCategoryId());
						parameters.add("#VALM="+min.getMinimumbalancevalue());
						parameters.add("#OBMIN="+min.getMinimumbalanceobserv());
						System.out.println("parameters: "+parameters.toString());
						outbox.insertMessageOutbox(userId,
								EmailProcess.MIN_BALANCE_MAGNAGEMENT, 
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
								"Aprobación de Saldo Mínimo", true,
								parameters);
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("Erro en MinimumBalanceEJB.getapprovesCategory en envio del correo");
					}
					
					Resp ="La categoría se ha aprobado con éxito";	
										
					return Resp;
				}else{
					System.out.println("No se enconraron cuentas Asociadas a PSE");
					Resp="La categoría se ha aprobado con éxito";
					return Resp;
				}

			}		
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error en: MinimumBalanceEJB.getapprovesCategory");
			Resp = "Error al aprobar la categoría";
			return Resp;
		}
		Resp = "Error al aprobar la categoría";
		return Resp;
	}
	
	@SuppressWarnings("unchecked")
	public List<BigDecimal>  getAccountPSE(){
		System.out.println("Entre a Consultar listas Cuentas PSE");
		List<BigDecimal> lista = new ArrayList<BigDecimal>();
		try {
			
			/*Query q= em.createNativeQuery("select distinct nvl(ta.account_id,0) from tb_account ta " + 
												"inner join re_account_bank ra on ra.account_id = ta.account_id " + 
												"inner join tb_bank_account tb on ra.bank_account_id = tb.bank_account_id " + 
												"inner join tb_bank tk on tk.bank_id = tb.product " +  
												"inner join re_account_device rd on rd.account_id = ta.account_id " + 
												"where tk.bank_aval = 0 " + 
	                      						"and rd.relation_state = 0");*/
			
			//Se agrega relacion ya que solo se actualizaran las activas
			Query q= em.createNativeQuery("select distinct nvl(ta.account_id,0) from tb_account ta " + 
					"inner join re_account_bank ra on ra.account_id = ta.account_id " + 
					"inner join tb_bank_account tb on ra.bank_account_id = tb.bank_account_id " + 
					"inner join tb_bank tk on tk.bank_id = tb.product " +					
					"where tk.bank_aval = 0 " +
					"and ra.state_account_bank = 1");
			
			lista=q.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error en: MinimumBalanceEJB.getAccountPSE");
		}
		
		
		return lista;
	}
	
	@SuppressWarnings("unchecked")
	public List<BigDecimal> getRelationDevices(Long account){
		
		System.out.println("Entre a Consultar Relaciones Cuentas PSE");
		List<BigDecimal> lista= new ArrayList<BigDecimal>();
		
		try {
			Query q= em.createNativeQuery("select nvl(rad.account_device_id,0) from re_account_device rad " +
					"inner join tb_device td on td.device_id = rad.device_id " +
					"where rad.relation_state = 0 " +
					"and td.device_state_id in (3,4,6) " +
					"and rad.account_id = ?1");
			q.setParameter(1, account);
			lista=q.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error en: MinimumBalanceEJB.getRelationDevices");
		}
		
		return lista;
	}
	
	
	public Long getMaxCategoryAprob (Long categoryClient){
		Long result = 0L;
		
		System.out.println("Entre a Verificar Maxima Categoria Aprobada : " + categoryClient);
		try {
			Query q= em.createNativeQuery("select * from(select minimum_balance_id from tb_minimum_balance " +
					"where category_id=?1 " +
					"and minimum_balance_state_id=2 " +
					"order by minimum_balance_date desc) " +
					"where rownum=1");
			q.setParameter(1, categoryClient);
			result = ((BigDecimal) q.getSingleResult()).longValue();
		}catch (NoResultException e) {
			result = 0L;
			System.out.println("No se nencontraron categorias Aprobadas.");
		
		} catch (Exception e) {
			e.printStackTrace();
			result = 0L;
			System.out.println("Error en: MinimumBalanceEJB.getMaxCategoryAprob");
		}
		return result;
	}



	@Override
	public boolean getExitsCategory(Long categoryId) {
		System.out.println("Entre a MinimumBalanceEJB.getExitsCategory");
		try {

			Query q= em.createNativeQuery("SELECT COUNT(category_id) FROM tb_minimum_balance WHERE category_id = ?1");
			q.setParameter(1, categoryId);
			Long result = ((BigDecimal) q.getSingleResult()).longValue();
			
			if (result > 0){
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	public boolean setAlarmBalance (Long account,Long idMini,Long userId,Long userIdClient,String ip){

		Long alarmBalance = null;
		System.out.println("Id TbAcount: " + account);
		System.out.println("Id TbMinimumBalance: " + idMini);
		System.out.println("Id UserConfig: " + userId);
		System.out.println("Id UserClient: " + userIdClient);

		try {

			Query q = em.createNativeQuery("select id_alarm_balance from tb_alarm_balance where id_tip_alarm = 4 and account_id = ?1");
			q.setParameter(1, account);
			alarmBalance = ((BigDecimal) q.getSingleResult()).longValue();

		} catch (Exception e) {
			alarmBalance = null;
			e.printStackTrace();
			System.out.println("Error en: MinimumBalanceEJB.setAlarmBalance");
		}


		if(alarmBalance == null){

			try {

				TbAlarmBalance alm = new TbAlarmBalance();
				alm.setAccountId(account);
				alm.setIdTipAlarm(4L);
				alm.setValminimo(0L);
				alm.setLastExecution(new Timestamp(System.currentTimeMillis()));
				alm.setEstAlarm(1L);
				//Query q1 = em.createNativeQuery("select system_parameter_value from tb_system_parameter where system_parameter_id = 9");
				Query q1 =em.createQuery("select systemParameterValue from TbSystemParameter where systemParameterId in (9L)");
				alm.setFrequency((Long)q1.getSingleResult());
				//Query q2 = em.createNativeQuery("select system_parameter_value from tb_system_parameter where system_parameter_id = 44");
				Query q2 =em.createQuery("select systemParameterValue from TbSystemParameter where systemParameterId in (44L)");
				alm.setValMinAlarm((Long) q2.getSingleResult());

				em.persist(alm);
				em.flush();

				System.out.println("Se crea el registro en TbAlarmBalance ya que no existia " + alm.getIdAlarmBalance());

				alarmBalance = alm.getIdAlarmBalance();
				if (alarmBalance == null){
					alarmBalance = 0L;
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error en: MinimumBalanceEJB.setAlarmBalance Insert Alarm");
			}	
		}
		TbAlarmBalance tab = em.find(TbAlarmBalance.class, alarmBalance);	
		
		if (idMini >  0 && alarmBalance > 0){

			System.out.println("Se encontro Alarma y Saldo Minimo: setAlarmBalance");
			jpa.TbMinimumBalance tm = em.find(jpa.TbMinimumBalance.class, idMini);


			System.out.println("La maxima Categoria aprobada para el cliente: " + tm.getCategoryid().getCategoryId());

			try {
				if (tab.getValminimo()!= tm.getMinimumbalancevalue()){
					System.out.println("Saldo Minimo Diferente Cat Aprob " + tab.getValminimo() + " diferente a " + tm.getCategpryvalue());
					tab.setValminimo(tm.getMinimumbalancevalue());
					em.merge(tab);
					em.flush();

					try {
						ArrayList<String> parameters=new ArrayList<String>();
						/*parameters.add("#MINIID="+idMini);*/
						
						parameters.add("#CATM="+tm.getCategoryid().getCategoryId());
						parameters.add("#VALM="+tm.getMinimumbalancevalue());
						System.out.println("parameters: "+parameters.toString());

						outbox.insertMessageOutbox(userIdClient,
								EmailProcess.UPDATE_MINIMUM_BALANCE, 
								account, 
								null, 
								null, 
								null, 
								null, 
								null, 
								null, 
								null, 
								userIdClient, 
								null, 
								null,
								"Actualizacion de Saldo Minimo", true,
								parameters);	
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("Erro en MinimumBalanceEJB.setAlarmBalance en envio del correo");
					}


					TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, userIdClient);
					Long idPTA;
					if(pt==null){
						idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  userIdClient, ip, userId);
					}
					else{
						idPTA=pt.getProcessTrackId();
					}
					Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.UPDATE_MINIMUM_BALANCE, 
							"Se asignó a su cuenta FacilPass No. "+ account + " el saldo mínimo por valor de $ "+tab.getValminimo(),
							userId, ip,1,"Error en la Asignacion",null,null,null,null);			

					//proceso cliente
					TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,userIdClient);
					Long idPTC;
					if(ptc==null){
						idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, userIdClient, ip, 1L);
					}
					else{
						idPTC=ptc.getProcessTrackId();
					}
					Long detailB=process.createProcessDetail(idPTC,ProcessTrackDetailType.UPDATE_MINIMUM_BALANCE, 
							"Se asignó a su cuenta FacilPass No. "+ account + " el saldo mínimo por valor de $ "+tab.getValminimo(),
							userId, ip,1,"Error en la Asignacion",null,null,null,null);
				}


				if(tab.getValMinAlarm()< tm.getMinimumbalancevalue()*2){
					System.out.println("El saldo Bajo es menor al doble del Saldo Minimo");
					tab.setValMinAlarm(tm.getMinimumbalancevalue()*2);
					em.merge(tab);
					em.flush();

					try {
						System.out.println("Saldo Minimo "+ tab.getValminimo());
						System.out.println("Saldo Bajo "+ tab.getValMinAlarm());
						//Ojo Verificar el envio para los usuarios
						outbox.insertMessageOutbox(userIdClient, 
								EmailProcess.UPDATE_LOW_BALANCE, 
								account, 
								null, 
								null, 
								null, 
								null, 
								null, 
								null, 
								null, 
								userIdClient, 
								null, 
								null,
								"Actualizacion de Saldo Bajo", true,
								null);	
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("Erro en MinimumBalanceEJB.setAlarmBalance en envio del correo");
					}
				}
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error en: MinimumBalanceEJB.setAlarmBalance line 675 Calculate Balance");
				return false;
			}
		}else if (idMini == 0) {

			System.out.println("----------> No se encontro Saldo Minimo por lo tanto se agrega el parameto: setAlarmBalance  ");

			try {	

				//Query q3 = em.createNativeQuery("select system_parameter_value from tb_system_parameter where system_parameter_id = 44");
				Query q3 =em.createQuery("select systemParameterValue from TbSystemParameter where systemParameterId in (44L)");
				Long Param = Long.parseLong(((String) q3.getSingleResult()));

				if (!tab.getValminimo().equals(Param)){
					System.out.println("Saldo Minimo Diferente Cat No Aprob " + tab.getValminimo() + " diferente a " + Param);
					tab.setValminimo(Param);
					em.merge(tab);
					em.flush();
					
					//Mensaje de actualizacion Saldo Minimo Usuario-Cuenta

						ArrayList<String> parameters=new ArrayList<String>();
						/*parameters.add("#MINIID="+idMini);*/
						
						parameters.add("#CATM="+vehicleEJB.getMaxCategoryClient(account));
						parameters.add("#VALM="+Param);
						System.out.println("parameters: "+parameters.toString());

						outbox.insertMessageOutbox(userIdClient,
								EmailProcess.UPDATE_MINIMUM_BALANCE, 
								account, 
								null, 
								null, 
								null, 
								null, 
								null, 
								null, 
								null, 
								userIdClient, 
								null, 
								null,
								"Actualizacion de Saldo Minimo", true,
								parameters);
						
					TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, userIdClient);
					Long idPTA;
					if(pt==null){
						idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  userIdClient, ip, 1L);
					}
					else{
						idPTA=pt.getProcessTrackId();
					}
					Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.UPDATE_MINIMUM_BALANCE, 
							"Se asignó a su cuenta FacilPass No. "+ account + " el saldo mínimo por valor de $ "+tab.getValminimo(),
							userId, ip,1,"Error en la Asignacion",null,null,null,null);			
					//proceso cliente
					TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,userIdClient);
					Long idPTC;
					if(ptc==null){
						idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, userIdClient, ip, 1L);
					}
					else{
						idPTC=ptc.getProcessTrackId();
					}
					Long detailB=process.createProcessDetail(idPTC,ProcessTrackDetailType.UPDATE_MINIMUM_BALANCE, 
							"Se asignó a su cuenta FacilPass No. "+ account + " el saldo mínimo por valor de $ "+tab.getValminimo(),
							userId, ip,1,"Error en la Asignacion",null,null,null,null);	
				}else{
					System.out.println("El Valor del Saldo Minimo es igual al que se encuentra configurado");
				}
				if(tab.getValMinAlarm()< Param*2){
					System.out.println("El saldo Bajo es menor al doble del Saldo Minimo");
					tab.setValMinAlarm(Param*2);
					em.merge(tab);
					em.flush();
					try {
						System.out.println("Saldo Minimo "+ tab.getValminimo());
						System.out.println("Saldo Bajo "+ tab.getValMinAlarm());
						//Ojo Verificar el envio para los usuarios
						outbox.insertMessageOutbox(userIdClient, 
								EmailProcess.UPDATE_LOW_BALANCE, 
								account, 
								null, 
								null, 
								null, 
								null, 
								null, 
								null, 
								null, 
								userIdClient, 
								null, 
								null,
								"Actualizacion de Saldo Bajo", true,
								null);

					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("Erro en MinimumBalanceEJB.setAlarmBalance en envio del correo");
					}

				}



				return true;
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error en: MinimumBalanceEJB.setAlarmBalance Calculate Balance");
				return false;
			}		

		}

		return true;
	}	
	
	public boolean getExistCategoryesAprob(){
		
		try {
			Query q = em.createNativeQuery("select nvl(count(*),0) from tb_minimum_balance where minimum_balance_state_id = 2");
			Long cant = ((BigDecimal) q.getSingleResult()).longValue();
			
			if (cant>0){
				System.out.println("------>Se encontraron categorias Aprobadas");
				return true;
			}else{
				System.out.println("------>No se encontraron categorias Aprobadas");
				return false;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error en: MinimumBalanceEJB.getExistCategoryesAprob");
			return false;
		}		
	}



	@Override
	public void setCalculateMinimumBalanceAccount(Long accountId,
			Long creationUser, String ip) {
		try {
	    	Long stateDevice = null;
			Long RelationID;
	    		 TbAccount ta = em.find(TbAccount.class, accountId);
		    	 Long categoryId = 0L;
		    	 Long categoryClient = vehicleEJB.getMaxCategoryClient(accountId.longValue());
		    	 Long idMini = getMaxCategoryAprob(categoryClient);
		    	 
					if(idMini == 0){
						categoryId = 0L;
					}else{
						jpa.TbMinimumBalance tm = em.find(jpa.TbMinimumBalance.class, idMini);
						categoryId = tm.getCategoryid().getCategoryId() ;
						System.out.println("Id TbMinimumBalance: " + idMini);
					}
		    	 if(categoryClient.equals(categoryId)){
		    		 System.out.println("Entre a PSE con Categoria Maxima Aprobada");
		    		 setAlarmBalance(accountId.longValue(),idMini,creationUser,ta.getTbSystemUser().getUserId(),ip);
		    		 List<BigDecimal> relationDevices;							
		    		 relationDevices = getRelationDevices(accountId.longValue());	
		    		 stateDevice = deviceEJB.getListReplicationAvalState(accountId.longValue());
		    		 if (relationDevices.size()>0){
							System.out.println("Entre. se encontraron dispositivos Asociados");
							for (int j = 0; j < relationDevices.size(); j++) {

								RelationID = relationDevices.get(j).longValue();
								System.out.println("Relacion PSE " + RelationID);
								ReAccountDevice re = em.find(ReAccountDevice.class, RelationID);
								System.out.println("Dispositivo: " + re.getTbDevice().getDeviceCode());
								deviceEJB.setStateChange(re.getTbDevice().getDeviceId(),stateDevice);
								System.out.println("Dispositivo: " + re.getTbDevice().getDeviceCode() + " Cambia al estado: " +  stateDevice);

							}
							if (stateDevice==3L) {
								ta.setWithoutBalance(1L);
							}else{
								ta.setWithoutBalance(0L);
							}

							em.merge(ta);
							em.flush();								

						}else{
							System.out.println("No se encontraron dispositivos Activos");
						}
		    		 
		    	 }else if (categoryClient==-1L){
		    		 setAlarmBalance(accountId.longValue(),0L,creationUser,ta.getTbSystemUser().getUserId(),ip);
		    	 }else{
		    		 System.out.println("Entre a PSE con Categoria Maxima NO Aprobada");
		    		 setAlarmBalance(accountId.longValue(),0L,creationUser,ta.getTbSystemUser().getUserId(),ip);
		    		 
		    		 List<BigDecimal> relationDevices;							
		    		 relationDevices = getRelationDevices(accountId.longValue());	
		    		 stateDevice = deviceEJB.getListReplicationAvalState(accountId.longValue());
		    		 if (relationDevices.size()>0){
							System.out.println("Entre. se encontraron dispositivos Asociados");
							for (int j = 0; j < relationDevices.size(); j++) {

								RelationID = relationDevices.get(j).longValue();
								System.out.println("Relacion PSE " + RelationID);
								ReAccountDevice re = em.find(ReAccountDevice.class, RelationID);
								System.out.println("Dispositivo: " + re.getTbDevice().getDeviceCode());
								deviceEJB.setStateChange(re.getTbDevice().getDeviceId(),stateDevice);
								System.out.println("Dispositivo: " + re.getTbDevice().getDeviceCode() + " Cambia al estado: " +  stateDevice);

							}
							if (stateDevice==3L) {
								ta.setWithoutBalance(1L);
							}else{
								ta.setWithoutBalance(0L);
							}

							em.merge(ta);
							em.flush();								

						}else{
							System.out.println("No se encontraron dispositivos Activos");
						}
		    		 
		    		 if (!getExistCategoryesAprob()){
		    			 	ArrayList<String> parameters=new ArrayList<String>();
							
							parameters.add("#CATM="+categoryClient);
							parameters.add("#OBERROR="+"FPSM_001");
							System.out.println("parameters: "+parameters.toString());
							outbox.insertMessageOutbox(creationUser,
									EmailProcess.ERROR_MINIMUM_BALANCE, 
									accountId.longValue(), 
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
									"No existen Categorías Aprobadas", true,
									parameters);
		    		 }
		    			 
		    			 	ArrayList<String> parameterst=new ArrayList<String>();										
							parameterst.add("#CATM="+categoryClient);
							parameterst.add("#OBERROR="+"FPSM_002");
							System.out.println("parameters: "+parameterst.toString());
							outbox.insertMessageOutbox(creationUser,
									EmailProcess.ERROR_MINIMUM_BALANCE, 
									accountId.longValue(), 
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
									"Categoría no Aprobada", true,
									parameterst);			    			 
		    		
		    	 }		     
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error en EnrollDeviceEJB.getActiveDispositive");
		}
		
	}	
		
}