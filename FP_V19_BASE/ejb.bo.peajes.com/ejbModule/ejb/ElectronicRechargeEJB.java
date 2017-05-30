package ejb;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jpa.ReAccountBank;
import jpa.TbAccount;
import jpa.TbAgreement;
import jpa.TbArMinBalance;
import jpa.TbAutomaticRecharge;
import jpa.TbBank;
import jpa.TbBankAccount;
import jpa.TbBankTransact;
import jpa.TbChanel;
import jpa.TbFrequency;
import jpa.TbProcessTrack;
import jpa.TbResponseType;
import jpa.TbStateRecharge;
import jpa.TbSystemUser;
import jpa.TbTypeProgramming;
import jpa.TbUserData;
import jpa.TbWebServices;
import jpa.TbWsResponses;
import util.ClientRecharge;
import util.ReElectronicRecharge;
import util.ServiceResponse;
import util.ws.PseWS;
import util.ws.WSConsultTransactionAval;
import util.ws.WSCreateTransactionAval;
import util.ws.WsResponses;
import util.ws.dtos.WsRtaConsultTransAval;
import util.ws.dtos.WsRtaTransactionAval;

import com.facilpass.util.MessageParser;
import com.facilpass.util.classes.SimpleTagMessageParser;

import constant.EEmailParam;
import constant.EResponseType;
import constant.ESystemParameters;
import constant.ETbProcessRerchargeAVAL;
import constant.ETypePerson;
import constant.EWebServices;
import constant.EmailProcess;
import constant.LogAction;
import constant.LogReference;
import constant.ProcessTrackDetailType;
import constant.ProcessTrackType;
import constant.TPS_Constants;
import constant.TypeProgramming;
import crud.ObjectEM;
import ejb.email.Outbox;
import exceptions.NoParametrizationException;


@Stateless(mappedName = "ejb/ElectronicRecharge")
public class ElectronicRechargeEJB implements ElectronicRecharge {
	
	@PersistenceContext(unitName = "bo")
	private EntityManager em;

	private ObjectEM objectEM;
	
	@EJB(mappedName="ejb/Process")
	private Process process;
				
	@EJB(mappedName="ejb/Log")
	private Log log;

	@EJB(mappedName ="ejb/email/Outbox")
	private Outbox outbox;
	

	@EJB(mappedName ="util/ws/PseWS")
	private PseWS pseWS;
	
	@EJB(mappedName ="util/ws/WsResponses")
	private WsResponses wsResponses;


	@EJB(mappedName ="ejb/SystemParameters")
	private SystemParameters parametros;

    /**
     * Default constructor. 
     */
    public ElectronicRechargeEJB() {
    }


    /**
	 * M�todo creado para listar las cuentas asociadas del cliente en sesi�n
	 * @author psanchez
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ReElectronicRecharge> getAccountAsociatesByClient(Long userCode) {
		List<ReElectronicRecharge> list = new ArrayList<ReElectronicRecharge>();
		
		try {
			Query q= em.createNativeQuery("select distinct rab.account_bank_id, rab.account_id, rab.bank_account_id, rab.date_association, ta.account_state_type_id from tb_bank_account tba "
                                        + "inner join re_account_bank rab on tba.bank_account_id= rab.bank_account_id "
                                        + "left join tb_account ta on rab.account_id= ta.account_id "
                                        + "inner join tb_bank tb on tba.product= tb.bank_id "
		                                + "WHERE ta.user_id=?1 AND ta.account_state_type_id <>2 ").setParameter(1,userCode);
			                                
			    		List<Object> lis= (List<Object>)q.getResultList();
			    		
						for(int i=0;i<lis.size();i++){
							Object[] o=(Object[]) lis.get(i);
							
							ReElectronicRecharge rab= new ReElectronicRecharge();
							
							if(rab!=null){
							rab.setAccountBankId(o[0].toString());
							rab.setAccountId(o[1].toString());
							rab.setBankAccountId(o[2].toString());
							rab.setDateAssociation(o[3].toString());
							list.add(rab);
						}
					 }			
				} catch (Exception e) {
					System.out.println(" [ Error en ElectronicRechargeEJB.getAccountAsociatesByClient. ] ");
					e.printStackTrace();
				}
				return list;
		}
    
	/**
	 * @see ejb.ElectronicRechargeEJB#insertElectronicRechargePrepaid(Long, Long, Long, Long, Long, String, Long, Date, Long, TypeProgramming, Integer, Long)
	 */
	@Override
	public Long insertElectronicRechargePrepaid(Long tbtId, Long userId, Long idClientAccount, Long valueRecharge, Long frecuencySelect,
			String ip, Long creationUser, Date dateRecharge, Long entityId, TypeProgramming tPrograming, Integer status, Long balanceValue){

		if (tbtId == null) {
			throw new IllegalArgumentException("El valor de la transaccion bancaria iniciada no puede ser vacio");
		}

		if (entityId == null){
			throw new IllegalArgumentException("El valor del banco no puede ser vacio");
		}
		
		if (tPrograming == null){
			throw new IllegalArgumentException("El valor del tipo de programaci�n no puede ser vacio");
		}
		
		switch (tPrograming){
			case FREQUENCY :
			case MINIMUM_BALANCE :
				break;
			default :
				throw new IllegalArgumentException("Tipo de frecuencia inv�lido.");
		}
		

		try {
			Query q = em.createNativeQuery("SELECT tar.automatic_recharge_id, tb.bank_name, tba.bank_account_number, tar.automatic_recharge_value, tar.account_id, " + 
						" tf.frequency_description, tar.automatic_recharge_date " +
						"FROM tb_automatic_recharge tar " +
						"INNER JOIN re_account_bank rab ON tar.account_id= rab.account_id "  +
						"INNER JOIN tb_bank_account tba ON rab.bank_account_id = tba.bank_account_id "  +
						"INNER JOIN tb_bank tb ON tba.product= tb.bank_id "  +
						"INNER JOIN tb_frequency tf ON tar.frecuency_id = tf.frequency_id "  +
						"WHERE tar.account_id=?1 AND tar.estado <>2 "+
						"and rab.STATE_ACCOUNT_BANK=1");
		
			q.setParameter(1, idClientAccount);
			q.getSingleResult();
			return -1L;
		 } catch (NoResultException e) {
			 Timestamp nextExecution = null;
			 Timestamp lastexecution = null;
			 SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			 String dateRec = null;

			 TbFrequency frequency  = em.find(TbFrequency.class, frecuencySelect);
			 if ( tPrograming.equals(TypeProgramming.FREQUENCY) ){
				dateRec = sdf.format(dateRecharge);
				String timeRec = parametros.getParameterValueById(ESystemParameters.H_HORA_RECARGA.getId());
				if (timeRec == null) {
					timeRec = "02:00:00";
				}

				String fullDateRec = dateRec + " " + timeRec;
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				Date parsedDate=null;
				try {
					parsedDate = dateFormat.parse(fullDateRec);
				} catch (ParseException e1) {
					e1.printStackTrace();
				}

				Timestamp tm = new Timestamp(parsedDate.getTime());
				Long difMillisecons = tm.getTime() - dateRecharge.getTime();
				Calendar dateRechageCal = new GregorianCalendar();
				dateRechageCal.setTime(dateRecharge);
				dateRechageCal.add(Calendar.MILLISECOND, difMillisecons.intValue());
				nextExecution = new Timestamp(dateRechageCal.getTimeInMillis());
				Calendar frecuenciaCalendar=Calendar.getInstance();
				frecuenciaCalendar.setTimeInMillis(frequency.getFrequencyEquiv().longValue());
				Calendar cLastexecution = this.returnCalendarLastExecution(frecuenciaCalendar, dateRechageCal);
				lastexecution = new Timestamp(cLastexecution.getTimeInMillis());
			 }

			 TbAccount account = em.find(TbAccount.class, idClientAccount);
			 TbTypeProgramming typeProgramming = em.find(TbTypeProgramming.class, tPrograming.getId());
			 Date arDate = null;
			 if (tPrograming.equals(TypeProgramming.FREQUENCY)){
				 arDate = dateRecharge;
			 }
			
			 TbAutomaticRecharge re = new TbAutomaticRecharge();
			 re.setAccountId(account);
			 re.setAutomaticRechargeValue(valueRecharge);
			 re.setFrequencyId(frequency);
			 re.setAutomaticRechargeDate(arDate);
			 re.setNextExecution(nextExecution);
			 re.setLastExecution(lastexecution);
			 re.setEstado(status);
			 re.setProceso(0);
			 re.setTypeProgramming(typeProgramming);

			 TbBank tBank = em.find(TbBank.class, entityId);
			 if (tBank == null){
				 throw new IllegalArgumentException("El banco con el valor indicado no existe");
			 }
			
			 TbBankTransact tbt = em.find(TbBankTransact.class, tbtId);
			 if (tbt == null){
				 throw new IllegalArgumentException("La transacci�n bancaria iniciada con el identificador indicado no existe");
			 }

			 re.setCreationDate(tbt.getGenerationDate());
			 objectEM = new ObjectEM(em);
			 if (objectEM.create(re) ) {
				 // Se actualiza la transaccion bancaria para que se asocie a la recarga automatica
				 tbt.setAutomaticRecharge(re);
				 objectEM.update(tbt);
				 // se asocia el trigger si es necesario
				 this.createTrigger(tPrograming, re, balanceValue);
				 //proceso administrador
				 String mensaje = null;
				 String mensajeLog = null;
				 DecimalFormat decForm = new DecimalFormat("#,###,###,###");
				 String sRecharge = decForm.format(valueRecharge);
				
				 switch (tPrograming){
				 case FREQUENCY :
					 mensajeLog = "Inicia solicitud Recursos Programados | Se inicia solicitud de autorizacion para la recarga de recursos programados para la Cuenta FacilPass No. " + account.getAccountId() + 
				              " por Valor de $" + decForm.format(valueRecharge)+ " y Frecuencia " + frequency.getFrequencyDescript();

					mensaje = "Se inici� la solicitud de autorizaci�n para la recarga de recursos programados a su Cuenta FacilPass No " +
							account.getAccountId() + " por Valor de $" + 
							sRecharge + " de la entidad " + tBank.getBankName() + " con " + typeProgramming.getTypeProgrammingDesc() + 
							" " + frequency.getFrequencyDescript() +
							" iniciando en la siguiente fecha:" + dateRec;
					break;
				 case MINIMUM_BALANCE :
					 String sBalance = decForm.format(balanceValue);

					 mensajeLog = "Inicia solicitud Recursos Programados | Se inicia solicitud de autorizacion para la recarga de recursos programados para la Cuenta FacilPass No. " + account.getAccountId() + 
							 " por Valor de $" + decForm.format(valueRecharge)+ " y Saldo M�nimo " + sBalance;

					 mensaje = "Se inici� la solicitud de autorizaci�n para la recarga de recursos programados a su Cuenta FacilPass No " +
							account.getAccountId() + " por Valor de $" +
							sRecharge + " de la entidad " + tBank.getBankName() + ", cuando el saldo de la cuenta FacilPass llegue a " +
							sBalance;
					 break;
				 }

				 // Se agrega log
				 log.insertLog( mensajeLog, LogReference.AUTOMATICRECHARGE, LogAction.CREATE, ip, creationUser);
				 this.createTbProcessTrack(ProcessTrackType.CLIENT, userId, ip, creationUser, mensaje);
				 //	proceso cliente
				 this.createTbProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, userId, ip, creationUser, mensaje);
				 return re.getAutomaticRechargeId();
			 }

		 } catch (Exception e) {
			System.out.println("ejb.ElectronicRechargeEJB#insertElectronicRecharge(Long, Long, Long, Long, String, Long, Date, Long, TypeProgramming, Integer, Long)");
			e.printStackTrace();
		 }

		 return null;
			
	}

	/**
	 * Crea un process detail y retorna el id asociado al mismo
	 * @param idPTC id del proceso
	 * @param mensaje mensaje a insertar
	 * @param ip ip donde se realiza la operacion
	 * @param creationUser identificador del usuario que realiza la transaccion
	 * @return id del detalle de proceso
	 * @author TPS r.bautista
	 */
	private Long createProcessDetailWithString(Long idPTC, String mensaje, String ip, Long creationUser){
		return process.createProcessDetail( idPTC, ProcessTrackDetailType.AUTHOMATHIC_RECHARGE, 
				  mensaje, creationUser, ip, 1, "", null, null, null, null );
		
	}
	
	/**
	 * @see ejb.ElectronicRecharge#getAutomaticRechargebyClient(Long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ClientRecharge> getAutomaticRechargebyClient(Long userId) {
List<ClientRecharge> list = new ArrayList<ClientRecharge>();
		
		try {	
            StringBuilder builder=new StringBuilder();
            
            
            builder.append("SELECT tar.automatic_recharge_id, tb.bank_name, tba.bank_account_number, tar.automatic_recharge_value, tar.account_id, tf.frequency_description, tar.creation_date, tar.type_programming_id, tarmb.min_balance ");
            builder.append("FROM tb_automatic_recharge tar ");
            builder.append("INNER JOIN re_account_bank rab ON tar.account_id= rab.account_id ");
            builder.append("INNER JOIN tb_bank_account tba ON rab.bank_account_id = tba.bank_account_id ");
            builder.append("INNER JOIN tb_bank tb ON tba.product= tb.bank_id ");
            builder.append("LEFT JOIN tb_frequency tf ON tar.frecuency_id = tf.frequency_id ");
            builder.append("AND tf.frequency_state=1");
            builder.append("LEFT JOIN tb_ar_min_balance tarmb ON tarmb.automatic_recharge_id=tar.automatic_recharge_id ");
            builder.append("WHERE tba.usrs=?1 ");
            builder.append("AND rab.state_account_bank =1 ");
            builder.append("AND tar.estado=0 ");
            
            Query q= em.createNativeQuery(builder.toString()).setParameter(1,userId);
    		List<Object> lis= (List<Object>)q.getResultList();
			for(int i=0;i<lis.size();i++){
				Object[] o=(Object[]) lis.get(i);
						                                       					
				ClientRecharge cr = new ClientRecharge();
				if(cr!=null){	
				    cr.setAutomaticRechargeId(Long.parseLong(o[0].toString()));
					cr.setBankName(o[1].toString());	
					cr.setBankAccountNumber(o[2].toString());
					cr.setAutomaticRechargeValue(Long.parseLong(o[3].toString()));
					cr.setAccountId(Long.parseLong(o[4].toString()));
					if (o[5] != null){
						cr.setFrequencyDescript(o[5].toString());
					} else {
						cr.setFrequencyDescript("");
					}

					cr.setAutomaticRechargeDate(o[6].toString());
					cr.setProgrammingType((o[7] instanceof Number)?((Number)o[7]).intValue():null);
					cr.setMinimumBalance((o[8] instanceof Number)?((Number)o[8]).doubleValue():null);
	    			list.add(cr);
				}

			}

		} catch (Exception e) {
			System.out.println(" [ Error en ElectronicRechargeEJB.getAutomaticRechargebyClient. ] ");
            			e.printStackTrace();
		}

		return list;
	}
	
	/**
	 * M�todo creado para cancelar las recargas autom�ticas del cliente en sesi�n
	 * @return return true si la recarga fu� anulada o false en otro caso.
	 * @author psanchez
	 */
	@SuppressWarnings("unused")
	@Override
	public boolean undoElectronicRecharge(Long automaticRechargeId, String ip, Long creationUser) {
        Long accountId = null;
		try {
			TbAutomaticRecharge tar= em.find(TbAutomaticRecharge.class, automaticRechargeId);
			accountId = tar.getAccountId().getAccountId();
            tar.setEstado(2);
            objectEM = new ObjectEM(em);	
            
			if(objectEM.create(tar)){
				
				log.insertLog("Desactivaci�n Recursos Programados | Se ha desactivado Recursos Programados ID: " + tar.getAutomaticRechargeId()+ 
				              " por valor de $ " + new DecimalFormat("#,###,###,###").format(tar.getAutomaticRechargeValue())+ " para la Cuenta FacilPass No." + accountId 
				                +(tar.getFrequencyId()!=null?( ", Frecuencia: " + tar.getFrequencyId().getFrequencyDescript()):" Por saldo m�nimo ") +  
				                ", �ltima ejecuci�n: " + tar.getLastExecution(),LogReference.AUTOMATICRECHARGE, LogAction.DELETE, ip, creationUser);
				
				
				StringBuilder builder=new StringBuilder();
				
				TbTypeProgramming ttp=tar.getTypeProgramming();
				
				switch(ttp.getTypeProgramminId().intValue())
				{
				 case 1:
				 	builder.append("Se desactivo la MMMM de recursos programados a su cuenta FacilPass No YYYY por valor de $XXXX de la entidad EE con FFPP");
				  break;
				 case 2:
				 	break;
				}
				
				String userProcessLogMessage=createProcessMessage(tar);
				
				   //proceso administrador
		  TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, creationUser);
				Long idPTA;
					if(pt==null){
						idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  creationUser, ip, creationUser);
					}else{
						idPTA=pt.getProcessTrackId();
					}
				Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.AUTHOMATHIC_RECHARGE,  
						     /*"Se desactiv� la asignaci�n de recursos programados para su Cuenta FacilPass No "+tar.getAccountId().getAccountId()+"."*/userProcessLogMessage, creationUser, ip, 1, "", null,null,null,null);
									
				//proceso cliente
				TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,creationUser);
				Long idPTC;
					if(ptc==null){
						idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, creationUser, ip, creationUser);
					}else{
						idPTC=ptc.getProcessTrackId();
					}
				Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.AUTHOMATHIC_RECHARGE,  
							/*"Se desactiv� la asignaci�n de recursos programados para su Cuenta FacilPass No "+tar.getAccountId().getAccountId()+ "."*/userProcessLogMessage, creationUser, ip, 1, "", null,null,null,null);
				return true;
			} 
			else
			{
				log.insertLog("Desactivaci�n Recursos Programados | No se ha desactivado Recursos Programados ID: " + tar.getAutomaticRechargeId()+ 
						      " por valor de $" + new DecimalFormat("#,###,###,###").format(tar.getAutomaticRechargeValue())+ " para la Cuenta FacilPass No." + accountId + ", Frecuencia: " + tar.getFrequencyId().getFrequencyDescript() + 
						      ", �ltima ejecuci�n " + tar.getLastExecution(),LogReference.AUTOMATICRECHARGE, LogAction.DELETEFAIL, ip, creationUser);
				return false;
			}
			
	   } catch (Exception e) {
			System.out.println(" [ Error en ElectronicRechargeEJB.undoElectronicRecharge ] ");
			e.printStackTrace();
			return false;
		}		
	}
 	
	public List<TbFrequency> getListFrequency() {
		List<TbFrequency> list = new ArrayList<TbFrequency>();
		try{
		  Query q = em.createQuery("Select fq From TbFrequency fq where frequencyState=1 ");
		  for (Object obj : q.getResultList()) {
  			  list.add((TbFrequency) obj);
		  }
		} catch (Exception e) {			
			e.printStackTrace();
		}
		return list;
	}
	
	
	/**
	 * M�todo creado para obtener los productos disociados del cliente en sesi�n
	 * @return return true si la cuenta fu� disociada o false en otro caso.
	 * @author psanchez
	 */
	@Override
	public boolean haveProductDissociation(Long idClientAccount) {
		BigDecimal id=null;
		boolean res=false;
		
		try{
			Query q = em.createNativeQuery("select account_bank_id from (select * from RE_ACCOUNT_BANK where ACCOUNT_ID=?1 order by DATE_ASSOCIATION desc) where ROWNUM=1");
			q.setParameter(1, idClientAccount);
			
			id = (BigDecimal) q.getSingleResult();
			
			if(id==null){
				res= true;
			}else{
				ReAccountBank rab = em.find(ReAccountBank.class, id.longValue());
				if(rab.getState_account_bank().equals(1)){
					res= false;
				}else if(rab.getState_account_bank().equals(3)){
					res= true;
				}else if(rab.getState_account_bank().equals(2)){
					res= true;
				}
			}
			/*Query q= 
				em.createNativeQuery("SELECT account_bank_id FROM re_account_bank WHERE account_id=?1 " +
									 "AND state_account_bank <> 1").setParameter(1, idClientAccount);
			id= (BigDecimal) q.getSingleResult();
			if(id!=null){
				res=true;
			}
			else{
				res=false;
			}*/
		}catch(NoResultException ex){
			res=false;
		}
		return res;
	}
	
	private Calendar returnCalendarLastExecution(Calendar frecuencia,Calendar calendarNextExecution){
	      int days=frecuencia.get(Calendar.DAY_OF_YEAR);
	      if(frecuencia.getTimeInMillis()<86400000){
	            calendarNextExecution.add(Calendar.MILLISECOND,-((int) frecuencia.getTimeInMillis()));
	      }else if(days==7||days==8){
	            calendarNextExecution.add(Calendar.WEEK_OF_YEAR, -1);
	      }else if(days==14||days==15){
	            calendarNextExecution.add(Calendar.WEEK_OF_YEAR, -2);
	      }else if(days==30||days==31){
	            calendarNextExecution.add(Calendar.MONTH, -1);
	      }else if(days==90){
	            calendarNextExecution.add(Calendar.MONTH, -3);
	      }else if(days==180){
	            calendarNextExecution.add(Calendar.MONTH, -6);
	      }else if(days>=360&&days<=365){
	            calendarNextExecution.add(Calendar.YEAR, -1);
	      }else{
	            calendarNextExecution.add(Calendar.DATE, -days);
	      }
	      return calendarNextExecution;
	}
	
	@SuppressWarnings("unchecked")
	public List<TbTypeProgramming> getListTypeProgramming(){
		List<TbTypeProgramming> list = null;		
		try {
			list = new ArrayList<TbTypeProgramming>();
			
			list.addAll(em.createNativeQuery("select * from tb_type_programming where state_id = 1",TbTypeProgramming.class).getResultList());
								
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al momento de obtener la lista getListTypeProgramming()");
		}		
		return list;
	}


	/**
	 * @see ejb.ElectronicRecharge#getBankTransactByURL(String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public TbBankTransact getBankTransactByURL(String url) {
		TbBankTransact resp = null;

		try {
			Query q = em.createQuery("select t from TbBankTransact t where t.urlGenerada LIKE ?1 ");
			q.setParameter(1, "%=" + url);
			List<TbBankTransact> list = q.getResultList();

			if ( (list == null) || (list.isEmpty())) {
				return resp;
			}

			if (list.size() > 1){
				throw new RuntimeException("Existe m�s de un TbBankTransact con la url indicada");
			}

			resp = list.get(0);
		} catch( NoResultException e){
			// Nada por hacer...
		}
		
		return resp;
	}

	/**
	 * @see ejb.ElectronicRecharge#updateElectronicRecharge(Long, Long, boolean, Long, String, Long)
	 */
	@Override
	public void updateElectronicRecharge(Long bankTransactId, Long entityId,
			boolean aceptada, Long authorUserId, String ip, Long errorId) {
		TbBankTransact tb;
		Long accountId;

		if (bankTransactId == null){
			throw new IllegalArgumentException("El valor de el bank transaction no puede ser vac�o");
		}
		
		if (entityId == null){
			throw new IllegalArgumentException("El valor de la entidad no puede ser vac�o");
		}

		TbBank tbank = em.find(TbBank.class, entityId);
		if (tbank == null){
			throw new IllegalArgumentException("No existe entidad bancaria con el valor indicado");
		}
		
		tb = em.find(TbBankTransact.class, bankTransactId);
		if (tb == null){
			throw new IllegalArgumentException("No existe transaccion con url creada para el valor indicado");
		}

		TbSystemUser user = em.find(TbSystemUser.class, tb.getUserId());
		DecimalFormat dFormat = new DecimalFormat("#,###,###,###");
		String valorTransaccion = dFormat.format(tb.getAutomaticRecharge().getAutomaticRechargeValue());
		String estado = aceptada ? "Autoriz�" : "Rechaz�";
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String mensajeLog, mensajeProceso;
		accountId = tb.getAutomaticRecharge().getAccountId().getAccountId();
		String valorSaldo = null;
		boolean esFrecuencia = tb.getAutomaticRecharge().getTypeProgramming().getTypeProgramminId().equals(TypeProgramming.FREQUENCY.getId());
		if (esFrecuencia){
			String dateRec = sdf.format(tb.getAutomaticRecharge().getAutomaticRechargeDate());
			mensajeLog = "Solicitud de Recursos Programados actualizada | Se recibi� respuesta de la entidad " + tbank.getBankName() + 
					" para la Cuenta FacilPass No. " + accountId + " para recarga por Valor de $" + 
					valorTransaccion + " y Frecuencia " + tb.getAutomaticRecharge().getFrequencyId().getFrequencyDescript() +
					" y su estado es " + estado;
			mensajeProceso = "La entidad " + tbank.getBankName() + " " + estado + 
					" la recarga de recursos programados a su Cuenta FacilPass No " + accountId +
					" por valor de $" + valorTransaccion + " con " + tb.getAutomaticRecharge().getTypeProgramming().getTypeProgrammingDesc() +
					" " + tb.getAutomaticRecharge().getFrequencyId().getFrequencyDescript() + " Iniciando en la siguiente fecha " + dateRec;
		} else {
			valorSaldo = dFormat.format(tb.getBalanceValue());
			mensajeLog = "Solicitud de Recursos Programados actualizada | Se recibi� respuesta de la entidad " + tbank.getBankName() + 
					" para la Cuenta FacilPass No. " + accountId + " para recarga por Valor de $" + 
					valorTransaccion + " cuando el sado de la cuenta sea " + valorSaldo;
			mensajeProceso = "La entidad " + tbank.getBankName() + " " + estado +
					" la recarga de recursos programados a su Cuenta FacilPass No " + accountId + 
					" por valor de $" + valorTransaccion + " con " + tb.getAutomaticRecharge().getTypeProgramming().getTypeProgrammingDesc() +
					", ser� efectiva cada vez que el saldo de la cuenta FacilPass llegue a " + valorSaldo;
		}
		
		log.insertLog(mensajeLog, LogReference.AUTOMATICRECHARGE, LogAction.CREATE, ip, authorUserId);
		this.createTbProcessTrack(ProcessTrackType.CLIENT, tb.getUserId(), ip, authorUserId, mensajeProceso);
		this.createTbProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, tb.getUserId(), ip, authorUserId, mensajeProceso);
		// Env�o mensaje
		Map<String,Object> tags = new HashMap<String,Object>();
		Date date = Calendar.getInstance().getTime();

		tags.put(EEmailParam.TIPO_ID.getTag(), user.getTbCodeType().getCodeTypeId());
		tags.put(EEmailParam.NUM_ID.getTag(), user.getUserId());
		tags.put(EEmailParam.NOMB_CLI.getTag(), user.getUserId());
		tags.put(EEmailParam.CUENTA_FACIL_PASS.getTag(), accountId);
		tags.put(EEmailParam.NOMB_BANC.getTag(), entityId);
		tags.put(EEmailParam.VAL_RECARG.getTag(), tb.getAutomaticRecharge().getAutomaticRechargeValue());
		tags.put(EEmailParam.FECHA_ACCION.getTag(), date);
		tags.put(EEmailParam.HORA_ACCION.getTag(), date);
		tags.put(EEmailParam.NOMB_CONVENIO.getTag(), tb.getAgreementId());
		tags.put(EEmailParam.TIPO_PROGRAMACION.getTag(), tb.getAutomaticRecharge().getTypeProgramming().getTypeProgramminId());
		tags.put(EEmailParam.EMAIL_USU.getTag(), user.getUserId());

		MessageParser mp = new SimpleTagMessageParser(tags);
		if (esFrecuencia){
			tags.put(EEmailParam.FRECUENCIA.getTag(), tb.getAutomaticRecharge().getFrequencyId().getFrequencyId());
			tags.put(EEmailParam.SALDO_BANK_TRANSAC.getTag(), TPS_Constants.EMAIL_PARAM_EMPTY);
		} else {
			tags.put(EEmailParam.FRECUENCIA.getTag(), TPS_Constants.EMAIL_PARAM_EMPTY);
			tags.put(EEmailParam.SALDO_BANK_TRANSAC.getTag(), bankTransactId);
		}

		if (aceptada){
			// NTF-13
			outbox.insertMessageOutbox2(tb.getUserId(), EmailProcess.REC_FREQ_APROBADA_AUT, true, mp);
		} else {
			tags.put(EEmailParam.ERROR_AVAL.getTag(), errorId);
			// NTF-12
			outbox.insertMessageOutbox2(tb.getUserId(), EmailProcess.ERROR_RECARG_FREQ_AUT, true, mp);
		}

		TbAutomaticRecharge tba = em.find(TbAutomaticRecharge.class, tb.getAutomaticRecharge().getAutomaticRechargeId());
		if (aceptada) {
			tba.setEstado(0); // se activa la recarga porque fue aceptada
		} else {
			tba.setEstado(2); // se inactiva la recarga porque fue rechazada
		}

		tba.setProceso(0);
		new ObjectEM(em).update(tba);
	}
	
	/**
	 * Busca el proceso acorde al usuario indicado. Si lo encuentra actualiza el detalle con el mensaje indicado
	 * usando la ip y el usuario de auditoria
	 * @param type tipo de proceso
	 * @param userId usuario del proceso
	 * @param ip direccion ip de la maquina
	 * @param auditUserId usuario de auditoria
	 * @param mensaje mensaje
	 * @author TPS r.bautista
	 */
	private void createTbProcessTrack (ProcessTrackType type, Long userId, String ip, Long auditUserId, String mensaje ){
		Long idPTC;
		TbProcessTrack ptc = process.searchProcess(type, userId);
		
		if( ptc == null ){
			idPTC = process.createProcessTrack(type, userId, ip, auditUserId);
		} else {
			idPTC = ptc.getProcessTrackId();
		}
		
		this.createProcessDetailWithString( idPTC, mensaje, ip, auditUserId);
	}


	/**
	 * @see ejb.ElectronicRecharge#createTransactionAuthorization(String, String, Long, Long, Long, Long, TypeProgramming, Long, ETypePerson)
	 */
	@Override
	public ServiceResponse createTransactionAuthorization(
			String context, String ip, Long accountId, Long value, Long bankId, Long userId, Long balance, TypeProgramming eTypeProg, Long frequencyId, ETypePerson eTypePerson) {
		ServiceResponse resp = new ServiceResponse();
		ETbProcessRerchargeAVAL processType = ETbProcessRerchargeAVAL.PAGO_PROG_AVAL_PREPAGO;
		TbBankTransact tbt = null;

		if (context == null){
			throw new IllegalArgumentException("El valor del contexto no puede ser vacio");
		}
		
		if ( accountId == null ){
			throw new IllegalArgumentException("El valor de la cuenta no puede ser vacio");
		}
		
		if (bankId == null){
			throw new IllegalArgumentException("El valor de la entidad AVAL no puede ser vacio");
		}
		
		if (userId == null){
			throw new IllegalArgumentException("El valor del usario no puede ser vacio");
		}
		
		if (eTypeProg == null){
			throw new IllegalArgumentException("El valor del tipo de programaci�n no puede ser vacio");
		}
		
		if ((ip == null) || (ip.isEmpty())){
			throw new IllegalArgumentException("El valor de la ip no puede ser vacio");
		}
		
		switch(eTypeProg){
		case FREQUENCY :
			if (frequencyId == null){
				throw new IllegalArgumentException("El valor de la frecuencia no puede ser vacio");
			}
			break;
		case MINIMUM_BALANCE:
			if (balance == null){
				throw new IllegalArgumentException("El valor del saldo no puede ser vacio");
			}
			break;
		}

		Object[] rArray= this.consultProcessRecharge(processType);
		Long channelId = ((BigDecimal) rArray[0]).longValue();
		Long agreementId = ((BigDecimal) rArray[1]).longValue();
		
		TbChanel chann = em.find(TbChanel.class, channelId);
		if (chann == null){
			throw new RuntimeException("El valor del canal asociado al tipo de proceso no existe");
		}
		
		TbAgreement agree = em.find(TbAgreement.class, agreementId);
		if (agree == null){
			throw new RuntimeException("El valor del convenio asociado al tipo de proceso no existe");
		}

		TbAccount ta = em.find(TbAccount.class, accountId);
		if (ta == null){
			throw new RuntimeException("Problemas generando la transacci�n, cuenta con identificador " + accountId + " no encontrada");
		}

		// Estado de la autorizaci�n inicia en pendiente 
		Long stateCode = 0L; 
		TbStateRecharge tsr = em.find(TbStateRecharge.class, stateCode);
		if (tsr == null){
			throw new RuntimeException("Problemas generando la transacci�n, estado con c�digo " + stateCode + " no encontrado");
		}

		// Se valida el usuario y el banco antes de persistir la transaccion de manera que se pueda evitar inconsistencia previa
		TbSystemUser user = em.find(TbSystemUser.class, userId);
		if (user == null){
			throw new RuntimeException("Problemas generando la transacci�n, usuario con identificador " + userId + " no encontrada");
		}

		TbBank bank = em.find(TbBank.class, bankId);
		if (bank == null){
			throw new RuntimeException("Problemas generando la transacci�n, banco con identificador " + bankId + " no encontrado");
		}
		
		Date generationDate = new Date();
		// El mensaje a cifrar lo formo con userId, Fecha, accountId
		String origen = "" + userId + "" + generationDate + "" + accountId;
		String url = context + "jsf/bankAccount/electronicRecharge.jspx?v="+ pseWS.encodePSETransaction(origen);

		tbt = new TbBankTransact();
		Timestamp tGenerationDate = new Timestamp(generationDate.getTime());
		tbt.setGenerationDate(tGenerationDate);
		tbt.setUserId(userId);
		// Estado: cero es no usada - uno usada 
		tbt.setState(0);
		tbt.setBalanceValue(balance);
		tbt.setBankId(bankId);
		tbt.setAgreementId(agreementId);
		tbt.setChannelId(channelId);
		tbt.setValue(value);
		tbt.setUrlGenerada(url);
		tbt.setNure(ta.getNure());
		tbt.setStateCode(tsr.getStateRechargeId());
		tbt.setStateDescripcion(tsr.getDescriptionStateRecharge());
		if (eTypePerson != null) {
			tbt.setPersonType(eTypePerson.getId());
		}

		TbTypeProgramming progType = em.find(TbTypeProgramming.class, eTypeProg.getId());
		if (progType == null) {
			throw new RuntimeException("Problemas generando la transacci�n, tipo de programacion " + eTypeProg.getId() + " no encontrada");
		}

		TbFrequency tbf = em.find(TbFrequency.class, frequencyId);
		if ( eTypeProg.equals(TypeProgramming.FREQUENCY) ){
			if(tbf == null){
				throw new RuntimeException("Problemas generando la transacci�n, frecuencia con identificador " + frequencyId + " no encontrada");
			}

		}
		
		Query q = em.createQuery("Select p From TbUserData p where p.tbSystemUser.userId =?1 ");
		q.setParameter(1, userId);
		
		TbUserData userData = (TbUserData) q.getSingleResult();
		if (userData == null){
			throw new RuntimeException("Problemas generando la transacci�n, informaci�n del usuario con identificador " + userId + " no encontrada");
		}

		objectEM = new ObjectEM(em);	
		if( !objectEM.create(tbt) ){
			return resp;
		}

		resp.setEntity(tbt);
		// se consume el WebService para crear la transaccion
		WSCreateTransactionAval ws = new WSCreateTransactionAval();
		TbWebServices tbWs = wsResponses.getWebService(EWebServices.WS_CREATE_TRX_AVAL.getId());
		Map<Long, TbWsResponses> resps = this.getMapResponses(tbWs);
		
		// Se setean los campos para consumir el web-service
		// ws.setWSUrl(tbWs.getUrl());
		ws.setWSUrl(bank.getBankUrl());
		ws.setCods(resps);
		// Se setean los parametros para consumir el servicio
		ws.setRqUID(tbt.getBankTransactId());
		// TODO Hay que verificar que todos los campos sean los que se setearon y no otros
		ws.setChannel("" + channelId.intValue());
		ws.setDate(tGenerationDate);
		ws.setIp(ip);
		ws.setIdType(wsResponses.getHomologation(  user.getTbCodeType().getCodeTypeId()));
		ws.setIdNum(user.getUserCode());
		ws.setBankId("" + bankId.intValue());
		// Por documentaci�n del servicio el tipo de transacci�n debe ser 3
		ws.setTrnType("3");
		ws.setAcctId("" + accountId.intValue());
		ws.setReturnUrl(url);
		// se ejecuta el cliente del servicio web
		WsRtaTransactionAval wsRta = (WsRtaTransactionAval)ws.execute();
		// Se verifica si hubo problemas de conexion
		if (wsRta.isConnectionProblems()){
			// Actualizar el estado de registro a ERROR
			String status = "1"; // Estado 1 es activo
			q = em.createQuery("Select p From TbStateRecharge p where p.nameStateRecharge =?1 and p.stateRecharge = ?2");
			q.setParameter(1, "Error");
			q.setParameter(2, status);
			
			tsr = (TbStateRecharge)q.getSingleResult();
			tbt.setStateCode(tsr.getStateRechargeId());
			tbt.setStateDescripcion(tsr.getDescriptionStateRecharge());
			objectEM.update(tbt);
			this.sendMailErrorWSAval(tbt, ta, user, eTypeProg, frequencyId, wsRta.getDetailedStatusDescription(), agreementId);
			resp.setMsj("No hay comunicaci�n con su entidad financiera, intente m�s tarde");
			return resp;
		}
		
		TbWsResponses wsCodeRta = resps.get(wsRta.getStatusCode());
		if (!wsCodeRta.isValid()) {
			this.sendMailErrorWSAval(tbt, ta, user, eTypeProg, frequencyId, wsRta.getDetailedStatusDescription(), agreementId);
			resp.setMsj(wsRta.getStatusDescription());
			return resp;
		} 

		tbt.setTransactId(wsRta.getTransactId());
		tbt.setBankUrl(wsRta.getBankUrl());
		objectEM.update(tbt);
		resp.setSuccess(true);
		return resp;
	}

	/**
	 * Retorna las respuestas asociadas al tbwebservices indicado dentro de un mapa con codigo, error
	 * @param tbws web service que contiene el mapa
	 * @return Map<Long, TbWsResponses> 
	 */
	private Map<Long, TbWsResponses> getMapResponses(TbWebServices tbws){
		Map<Long, TbWsResponses> resp = new HashMap<Long, TbWsResponses>();
		
		if(tbws.getTbWsResponses() != null && !tbws.getTbWsResponses().isEmpty()){
			resp = new HashMap<Long, TbWsResponses>();
			for(TbWsResponses tr : tbws.getTbWsResponses()){
				resp.put(tr.getCode(), tr);
			}

		} else {
			resp = wsResponses.getResponses(EWebServices.WS_CREATE_TRX_AVAL);
		}
		
		return resp;
	}
	
	/**
	 * envia mensaje de error acorde a la informacion de los parametros recibidos
	 * @param tbt TbBankTransact a autorizar
	 * @param ta TbAccount cuenta facilpass
	 * @param user usuario sobre el cual se parametriza
	 * @param type TbTypeProgramming tyipo de programacion
	 * @param typeProg Tipo de programaci�n
	 * @param freqId Identificador de la frecuencia
	 * @param agreeId Identificador del convenio
	 * @param error codigo y descripcion del error
	 * @author TPS r.bautista
	 */
	private void sendMailErrorWSAval(TbBankTransact tbt, TbAccount ta, TbSystemUser user, TypeProgramming typeProg, Long freqId, String error, Long agreeId){
		Map<String, Object> tags = new HashMap<String, Object>();
		
		Date date = Calendar.getInstance().getTime();

//		DecimalFormat dFormat = new DecimalFormat("#,###,###,###");
		// String valorTransaccion = dFormat.format(tbt.getValue());
		
		tags.put(EEmailParam.NOMB_CLI.getTag(), user.getUserId());
		tags.put(EEmailParam.CUENTA_FACIL_PASS.getTag(), ta.getAccountId());
		tags.put(EEmailParam.TIPO_ID.getTag(), user.getTbCodeType().getCodeTypeId());
		tags.put(EEmailParam.NUM_ID.getTag(), user.getUserId());
		tags.put(EEmailParam.NOMB_CONVENIO.getTag(), agreeId);
		tags.put(EEmailParam.ERROR_DINAMICO.getTag(), error);
		tags.put(EEmailParam.VAL_RECARG.getTag(), tbt.getValue());
		tags.put(EEmailParam.TIPO_PROGRAMACION.getTag(), typeProg.getId());
		tags.put(EEmailParam.FECHA_ACCION.getTag(), date);
		tags.put(EEmailParam.HORA_ACCION.getTag(), date);
		tags.put(EEmailParam.EMAIL_USU.getTag(), user.getUserId());
		
		MessageParser mp = new SimpleTagMessageParser(tags);
		switch(typeProg){
		case FREQUENCY :
			tags.put(EEmailParam.FRECUENCIA.getTag(), freqId);
			tags.put(EEmailParam.SALDO_BANK_TRANSAC.getTag(), TPS_Constants.EMAIL_PARAM_EMPTY);
			break;
		case MINIMUM_BALANCE :
			tags.put(EEmailParam.FRECUENCIA.getTag(), TPS_Constants.EMAIL_PARAM_EMPTY);
			tags.put(EEmailParam.SALDO_BANK_TRANSAC.getTag(), tbt.getBankTransactId());
			break;
		}
		
		// NTF-09
		outbox.insertMessageOutbox2(user.getUserId(), EmailProcess.WS_COM_ERROR, true, mp);
	}


	/**
	 * Servicio de espera
	 */
	private boolean esperarWS(ServiceResponse srta, TbBankTransact tbt){
		TbBankTransact tbtLocal;
		boolean retornar = false;

		TbAutomaticRecharge tbTemp = em.find(TbAutomaticRecharge.class, tbt.getAutomaticRecharge().getAutomaticRechargeId());
		if (tbTemp.getProceso().intValue() == 1){
			// lo consumi� la sonda... hacer algo...
			int cont = 0;
			try {
				while (cont < 3) {
					Thread.sleep(1300);
					tbTemp = em.find(TbAutomaticRecharge.class, tbt.getAutomaticRecharge().getAutomaticRechargeId());
					if (tbTemp.getProceso().intValue() == 0){
						cont = 2;
					}

					cont++;
				}
				
				System.out.println("Se acab� la espera...");
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}

		// Estado pendiente
		Long estado = 1L;
		if (tbTemp.getEstado().intValue() == estado.intValue() ){
			// esta pendiente por lo tanto la proceso
			tbTemp.setProceso(1);
			new ObjectEM(em).update(tbTemp);
		} else {
			// no est� pendiente por lo tanto ya fue procesada...
			// debo actualizar tbanktransact al estado nuevo dejado por la sonda...
			tbtLocal = this.em.find(TbBankTransact.class, tbt.getBankTransactId());
			// se mapea el estado de automatic_recharge a tb_state_recharge
			switch(tbtLocal.getAutomaticRecharge().getEstado()) {
				case 0: // autorizada
					estado = 1L;
					break;
				case 1 : // pendiente
					estado = 0L;
					break;
				case 2 : // eliminada
					estado = 2L;
					break;
				case 3 : // rechazada
					estado = 2L;
				break;
			}
			
			
			TbStateRecharge tbsr = em.find(TbStateRecharge.class, estado);
			tbtLocal.setStateCode(tbsr.getStateRechargeId());
			tbtLocal.setStateDescripcion(tbsr.getDescriptionStateRecharge());
			new ObjectEM(em).update(tbtLocal);
			// Se construye la respuesta como consumo...
			WsRtaConsultTransAval rTemp = new WsRtaConsultTransAval();
			rTemp.setConnectionProbs(false);
			rTemp.setSuccess(true);
			rTemp.setBankTrans(tbtLocal);
			rTemp.setProcesoSonda(true);
			srta.setEntity(rTemp);
			srta.setSuccess(true);
			retornar = true;
		}

		return retornar;
	}
	
	/**
	 * @see ejb.ElectronicRecharge#checkURL(Long, String, String)
	 */
	@SuppressWarnings({ "unchecked" })
	@Override
	public ServiceResponse checkURL(Long userId, String url, String ip) {
		Date fechaSistema;
		ETbProcessRerchargeAVAL processType = ETbProcessRerchargeAVAL.PAGO_PROG_AVAL_PREPAGO; 
		ServiceResponse srta = new ServiceResponse();

		if ( userId == null){
			throw new IllegalArgumentException("El valor del usuario no puede ser vac�o");
		}
		
		if ( url == null){
			throw new IllegalArgumentException("El valor de la url no puede ser vac�o");
		}

		if ( ip == null){
			throw new IllegalArgumentException("El valor de la ip no puede ser vac�o");
		}
		
		Map<String, Object> tags;
		Date date;

		Query q = em.createQuery("select t from TbBankTransact t where t.urlGenerada LIKE ?1 ");
		q.setParameter(1, "%=" + url);
		List<TbBankTransact> list = q.getResultList();
		TbBankTransact tb = null;
		if ((list != null) && (!list.isEmpty())){
			tb = list.get(0);
		}

		if ( (list == null) || (list.isEmpty()) || (tb.getAutomaticRecharge() == null)){
			// No existe la URL
			date = Calendar.getInstance().getTime();

			tags = new HashMap<String, Object>();
			tags.put(EEmailParam.FECHA_ACCION.getTag(), date);
			tags.put(EEmailParam.HORA_ACCION.getTag(), date);
			tags.put(EEmailParam.IP_ACCION.getTag(), ip);
			tags.put(EEmailParam.URL_ACCION.getTag(), url);

			MessageParser mp = new SimpleTagMessageParser(tags);
			// NTF-15
			outbox.insertMessageOutbox2(userId, EmailProcess.AVAL_URL_NOT_EXIST, true, mp);
			srta.setId(TPS_Constants.NOT_EXIST);
			return srta;
		}

		if (processType == null){
			throw new IllegalArgumentException("El valor del tipo de proceso de pago no puede ser vac�o");
		}

		TbSystemUser user = em.find(TbSystemUser.class, tb.getUserId());
		if (user == null){
			throw new RuntimeException("No se encontro el usuario asignado a la solicitud.");
		}
		
		if (!tb.isAvailable()) {
			// URL ya fue usada
			TbTypeProgramming ttp = em.find(TbTypeProgramming.class, tb.getAutomaticRecharge().getTypeProgramming().getTypeProgramminId());
			if (ttp == null){
				throw new RuntimeException("No se encontro el tipo de programaci�n asignado a la solicitud.");
			}

			Object[] conv = this.consultProcessRecharge(processType);
			Long agreeId = ((BigDecimal)conv[1]).longValue();
			date = Calendar.getInstance().getTime();

			TbAgreement agree = em.find(TbAgreement.class, agreeId);
			if (agree == null){
				throw new RuntimeException("No se encontro el convenio asociado al tipo de proceso de pago.");
			}

			TbResponseType respType = em.find(TbResponseType.class, EResponseType.URL_USED.getId());
			if (respType == null)  {
				throw new RuntimeException("No se encontro la descripci�n del error.");
			}
			
			String desc = "" + respType.getResponseTypeCode() + " - " + respType.getResponseDescrip();
			
			tags = new HashMap<String, Object>();
			tags.put(EEmailParam.CUENTA_FACIL_PASS.getTag(), tb.getAutomaticRecharge().getAccountId().getAccountId());
			tags.put(EEmailParam.TIPO_ID.getTag(), user.getTbCodeType().getCodeTypeId());
			tags.put(EEmailParam.FECHA_ACCION.getTag(), date);
			tags.put(EEmailParam.HORA_ACCION.getTag(), date);
			tags.put(EEmailParam.NUM_ID.getTag(), tb.getUserId());
			tags.put(EEmailParam.NOMB_CLI.getTag(), tb.getUserId());
			tags.put(EEmailParam.IP_ACCION.getTag(), ip);
			tags.put(EEmailParam.URL_ACCION.getTag(), url);
			tags.put(EEmailParam.ERROR_DINAMICO.getTag(), desc);
			tags.put(EEmailParam.VAL_RECARG.getTag(), tb.getValue());
			tags.put(EEmailParam.EMAIL_USU.getTag(), tb.getUserId());
			tags.put(EEmailParam.NOMB_CONVENIO.getTag(), agreeId);
			tags.put(EEmailParam.TIPO_PROGRAMACION.getTag(), ttp.getTypeProgramminId());

			MessageParser mp = new SimpleTagMessageParser(tags);
			TypeProgramming type = TypeProgramming.getTypePrograming(ttp.getTypeProgramminId());
			switch(type){
				case FREQUENCY :
					TbFrequency freq = em.find(TbFrequency.class, tb.getAutomaticRecharge().getFrequencyId().getFrequencyId());
					if (freq == null){
						throw new RuntimeException("No se encontro la frecuencia programada asignada a la solicitud.");
					}

					tags.put(EEmailParam.FRECUENCIA.getTag(), freq.getFrequencyId());
					tags.put(EEmailParam.SALDO_BANK_TRANSAC.getTag(), TPS_Constants.EMAIL_PARAM_EMPTY );
					break;
				case MINIMUM_BALANCE :
					tags.put(EEmailParam.FRECUENCIA.getTag(), TPS_Constants.EMAIL_PARAM_EMPTY);
					tags.put(EEmailParam.SALDO_BANK_TRANSAC.getTag(), tb.getBankTransactId() );
				break;
			}

			// NTF-16			
			outbox.insertMessageOutbox2(userId, EmailProcess.AVAL_URL_USED_PROG_RECH, true, mp);

			srta.setId(TPS_Constants.USED);
			return srta;
		}

		// Al estar disponible se debe pasar a usada (1)
		tb.setState(1);
		fechaSistema = new Date();
		tb.setExecutionDate(new Timestamp(fechaSistema.getTime()));
		new ObjectEM(em).update(tb);

		if (this.isUrlExpired(tb)){
			// La URL expir�
			String mensaje = parametros.getParameterValueById(ESystemParameters.M_RECARGA_ENTIDAD_AVAL.getId());
			srta.setId(TPS_Constants.EXPIRED);
			srta.setMsj(mensaje);
			return srta;
		}
		
		if ( this.esperarWS(srta, tb) ){
			return srta;
		}

		TbWebServices tbWs = wsResponses.getWebService(EWebServices.WS_CONSULT_TRX_AVAL.getId());
		Map<Long, TbWsResponses> resps = this.getMapResponses(tbWs);

		// Consumo del WS
		WSConsultTransactionAval wsct = new WSConsultTransactionAval();
		// Se setean los valores para invocar el web-service
		wsct.setWSUrl(tbWs.getUrl());
		wsct.setCods(resps);
		// se setean valores propios del servicio
		wsct.setRqUID(tb.getBankTransactId());
		// Por documentaci�n t�cnica del servicio el canal debe ser 1
		wsct.setChannel("1");
		wsct.setDate(fechaSistema);
		wsct.setIp(ip);
		wsct.setIdType(wsResponses.getHomologation(user.getTbCodeType().getCodeTypeId()));
		wsct.setIdNum(user.getUserCode());
		// Por documentaci�n t�cnica del servicio el banco debe ser 6
		wsct.setBankId("6");
		wsct.setAcctId("" + tb.getAutomaticRecharge().getAccountId().getAccountId().intValue());
		// se ejecuta el cliente del servicio web
		WsRtaConsultTransAval rta = (WsRtaConsultTransAval)wsct.execute();
		srta.setEntity(rta);
		if (rta.isConnectionProblems()){
			tags = new HashMap<String, Object>();
			date = Calendar.getInstance().getTime();

			TbBank bank = em.find(TbBank.class, tb.getBankId());
			if (bank == null){
				throw new RuntimeException("No se encontr� la entidad AVAL asociada a la transaccion");
			}

			Object[] conv = this.consultProcessRecharge(processType);
			Long agreeId = ((BigDecimal)conv[1]).longValue();
			TbAgreement agree = em.find(TbAgreement.class, agreeId);
			if (agree == null){
				throw new RuntimeException("No se encontro el convenio asociado al tipo de proceso de pago.");
			}

			TbResponseType respType = em.find(TbResponseType.class, EResponseType.CONNECTION_PROBS.getId());
			if (respType == null)  {
				throw new RuntimeException("No se encontro la descripci�n del error.");
			}
			
			String desc = "" + respType.getResponseTypeCode() + " - " + respType.getResponseDescrip();
			
			tags.put(EEmailParam.CUENTA_FACIL_PASS.getTag(), tb.getAutomaticRecharge().getAccountId().getAccountId());
			tags.put(EEmailParam.NOMB_BANC.getTag(), tb.getBankId());
			tags.put(EEmailParam.NOMB_CLI.getTag(), user.getUserId());
			tags.put(EEmailParam.NUM_ID.getTag(), user.getUserId());
			tags.put(EEmailParam.TIPO_ID.getTag(), user.getTbCodeType().getCodeTypeId());
			tags.put(EEmailParam.FECHA_ACCION.getTag(), date);
			tags.put(EEmailParam.HORA_ACCION.getTag(), date);
			tags.put(EEmailParam.URL_ACCION.getTag(), tb.getUrlGenerada());
			tags.put(EEmailParam.VAL_RECARG.getTag(), tb.getValue());
			tags.put(EEmailParam.EMAIL_USU.getTag(), user.getUserId());
			tags.put(EEmailParam.NOMB_CONVENIO.getTag(), agreeId);
			tags.put(EEmailParam.ERROR_DINAMICO.getTag(), desc);
			tags.put(EEmailParam.TIPO_PROGRAMACION.getTag(), tb.getAutomaticRecharge().getTypeProgramming().getTypeProgramminId());

			MessageParser mp = new SimpleTagMessageParser(tags);
			TypeProgramming type = TypeProgramming.getTypePrograming(tb.getAutomaticRecharge().getTypeProgramming().getTypeProgramminId());
			
			switch(type){
			case FREQUENCY :
				TbFrequency freq = em.find(TbFrequency.class, tb.getAutomaticRecharge().getFrequencyId().getFrequencyId());
				if (freq == null){
					throw new RuntimeException("No se encontro la frecuencia programada asignada a la solicitud.");
				}

				tags.put(EEmailParam.FRECUENCIA.getTag(), freq.getFrequencyId());
				tags.put(EEmailParam.SALDO_BANK_TRANSAC.getTag(), TPS_Constants.EMAIL_PARAM_EMPTY);
				break;
			case MINIMUM_BALANCE :
				tags.put(EEmailParam.FRECUENCIA.getTag(), TPS_Constants.EMAIL_PARAM_EMPTY);
				tags.put(EEmailParam.SALDO_BANK_TRANSAC.getTag(), tb.getBankTransactId());
				break;
			}

			// NTF-10
			outbox.insertMessageOutbox2(user.getUserId(), EmailProcess.ERROR_AUTO_FREQ, true, mp);
			return srta;
		} else {
			TbWsResponses wsCodeRta = resps.get(rta.getStatusCode());
			// se inicializa en pendiente
			Long id = 0L;
			
			if (!wsCodeRta.isValid()) {
				// Error
				srta.setMsj(wsCodeRta.getDetailedDescription());
				return srta;
			}

			if (rta.isAccepted()){
				// se actualiza a aceptado
				id = 1L;
			} else if (rta.isRejected()){
				// se actualiza a rechazado
				id = 2L;
				// TODO Que error id debo asignarle??? por el momento le asigno el error general
				wsCodeRta = resps.get(EWebServices.WS_CONSULT_TRX_AVAL.getGeneralErrorCode());
				rta.setErrorId(wsCodeRta.getTbWsResponsesId());
			}

			TbStateRecharge tbsr = em.find(TbStateRecharge.class, id);
			tb.setStateCode(tbsr.getStateRechargeId());
			tb.setStateDescripcion(tbsr.getDescriptionStateRecharge());
			new ObjectEM(em).update(tb);
			rta.setBankTrans(tb);
		}

		srta.setSuccess(true);
		return srta;
	}

	/**
	 * Indica si la url ya expiro
	 * @param tb transaccion que tiene las fechas de la url
	 * @return true si ya paso el tiempo con respecto a parametros
	 * @author TPS r.bautista
	 */
	private boolean isUrlExpired(TbBankTransact tb){
		int mins = Integer.parseInt(parametros.getParameterValueById(ESystemParameters.V_TIEMPO_VIDA_URL_AVAL.getId()));
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis( tb.getGenerationDate().getTime() );
		c.add( Calendar.MINUTE, mins );
		return !c.getTime().after( new Date(System.currentTimeMillis()));
	}

	/**
	 * Retorna un array con el identificador del channel y del agreement
	 * @param type tipo de proceso 
	 * @return [0] channel Id
	 * 		   [1] agreement Id
	 * @author TPS r.bautista
	 */
	@SuppressWarnings("unchecked")
	private Object[] consultProcessRecharge(ETbProcessRerchargeAVAL type){
		StringBuilder sb = new StringBuilder("SELECT CHANEL_ID, AGREEMENT_ID ").
				append(" FROM TB_PROCESS_AGREEMENT_CHANNEL ").
				append(" WHERE PROCESS_RECHARGE_AVAL_ID =?1 ");
		
		Query q = em.createNativeQuery(sb.toString());
		q.setParameter(1, type.getId());
		List<Object> rList = q.getResultList();
		
		if (rList == null || rList.isEmpty()){
			throw new NoParametrizationException("No se encontr� parametrizaci�n para el tipo de proceso");
		}
		
		if (rList.size() > 1){
			throw new RuntimeException("Se encontr� m�s de una parametrizaci�n para el tipo de proceso");
		}

		Object o = rList.get(0);
		Object[] a = (Object[])o;
		try {
			((BigDecimal)a[0]).longValue();
		} catch (Exception e ){
			throw new RuntimeException("No se encontr� canal configurado para el proceso");
		}
		
		try {
			((BigDecimal)a[1]).longValue();
		} catch (Exception e ){
			throw new RuntimeException("No se encontr� convenio configurado para el proceso");
		}
		
		return a;
	}

	/**
	 * Retorna un array con el identificador del channel y del agreement
	 * @param type tipo de proceso 
	 * @return [0] channel Id
	 * 		   [1] agreement Id
	 * @author TPS r.bautista
	 */
	@SuppressWarnings("unchecked")
	private Long consultRelationChannelAgreement(Long channelId, Long agreeId){
		Long a;
		StringBuilder sb = new StringBuilder(" select STATE_ID ").
				append(" from RE_AGREEMENT_CHANEL ").
				append(" where CHANEL_ID =?1 ").
				append(" and AGREEMENT_ID =?2 ");
		
		Query q = em.createNativeQuery(sb.toString());
		q.setParameter(1, channelId);
		q.setParameter(2, agreeId);
		
		List<Object> rList = q.getResultList();
		if (rList == null || rList.isEmpty()){
			throw new NoParametrizationException("No se encontr� parametrizaci�n para la relacion canal - acuerdo");
		}
		
		if (rList.size() > 1){
			throw new RuntimeException("Se encontr� m�s de una parametrizaci�n para la relaci�n canal - acuerdo");
		}

		Object o = rList.get(0);
		// Object[] a = (Object[])o;
		try {
			a = ((BigDecimal)o).longValue();  
			// ((BigDecimal)a[0]).longValue();
		} catch (Exception e ){
			throw new RuntimeException("No se encontr� estado configurado para la relaci�n canal - acuerdo");
		}

		return a;
	}

	/**
	 * @see ejb.ElectronicRecharge#checkMinRechargeValue(Long, ETbProcessRerchargeAVAL, Long, Long)
	 */
	@Override
	public ServiceResponse checkMinRechargeValue(Long value,
			ETbProcessRerchargeAVAL processType, Long userId, Long accountId) {
		ServiceResponse resp = new ServiceResponse();
		
		if(value == null){
			throw new IllegalArgumentException("El valor de la recarga no puede ser vac�o");
		}
		
		if (processType == null){
			throw new IllegalArgumentException("El valor del tipo de proceso de recarga no puede ser vac�o");
		}
		
		if (userId == null){
			throw new IllegalArgumentException("El valor del usuario de recarga no puede ser vac�o");
		}
		
		if (accountId == null){
			throw new IllegalArgumentException("El valor de la cuenta facilPass no puede ser vac�o");
		}
		
		if (!processType.equals(ETbProcessRerchargeAVAL.PAGO_PROG_AVAL_PREPAGO) && 
			!processType.equals(ETbProcessRerchargeAVAL.PAGO_PROG_AVAL_POSTPAGO)){
			throw new IllegalArgumentException("El valor del tipo de proceso no es valido");
		}
		
		Long valorMinimo; 
		Object [] aResp;
		Long channelId, agreeId, stateId;
		try {
			aResp = this.consultProcessRecharge(processType);
		} catch (NoParametrizationException e){
			// se envia mensaje  ntf 39
			// TODO
			this.sendMailMinRechargeValue(userId, accountId, e.getMessage());
			return resp;
		}
		
		channelId = ((BigDecimal) aResp[0]).longValue();
		agreeId = ((BigDecimal) aResp[1]).longValue();
		try {
			stateId = this.consultRelationChannelAgreement(channelId, agreeId);
		} catch(NoParametrizationException e){
			// se envia mensaje  ntf 39
			this.sendMailMinRechargeValue(userId, accountId, e.getMessage());
			return resp;
		}
		
		// Valores tomados de la tabla tb_state 0 - Inactivo / 2 - Eliminado
		if (stateId.longValue() == 0L || stateId.longValue() == 2L){
			// la relaci�n est� inactiva
			// se envia mensaje  ntf 39
			String error = "La relaci�n canal - convenio se encuentra inactiva";
			this.sendMailMinRechargeValue(userId, accountId, error);
			return resp;
		}
		
		TbChanel channel = em.find(TbChanel.class, channelId);

		// Se toma valor de la documentacion de la tabla donde indica que 0 - Inactivo / 1 - Activo
		if (channel.getChanelState().longValue() == 0L){
			// canal inactivo
			// se envia mensaje  ntf 39
			String error = "El canal se encuentra inactivo";
			this.sendMailMinRechargeValue(userId, accountId, error);
			return resp;
		}

		String errorMsg = null;
		if (channel.isValidaRecAsigMinima()){
			// el canal tiene habilitado el campo asignacion minima
			valorMinimo = channel.getMinimumValue();
		} else {

			// el canal NO tiene habilitado el campo asignacion minima
			TbAgreement agree = em.find(TbAgreement.class, agreeId);
			// Valores tomados de la tabla tb_state 0 - Inactivo / 2 - Eliminado
			if (agree.getTbState().getStateId().longValue() == 0L || agree.getTbState().getStateId().longValue() == 2L){
				String error = "El convenio se encuentra inactivo";
				this.sendMailMinRechargeValue(userId, accountId, error);
				return resp;
			}
			
			if (agree.isValidaRecAsigMinima()){
				// el acuerdo tiene habilitado el campo asignacion minima
				valorMinimo = agree.getMinimumValue();
			} else {
				// el acuerdo NO tiene habilitado el campo asignacion minima
				Long paramId = null;
				switch(processType.getPpaymentMethod()){
				case PREPAID :
					paramId = ESystemParameters.V_MIN_RECARGA_PREPAG.getId();
					break;
				case POSTPAID :
					paramId = ESystemParameters.V_MIN_ASIG_CUENTA_POST.getId();
					break;
				default :
					// Nada que hacer por ac� NUNCA debe entrar
						break;
				}
				
				valorMinimo = Long.parseLong(parametros.getParameterValueById(paramId));
			}

		}

		// No valido que valorMinimo no sea nulo... en BD no debe ser nulo...
		if (value.longValue() < valorMinimo.longValue()){
			switch(processType.getPpaymentMethod()){
			case PREPAID :
				errorMsg = "El valor ingresado es menor al valor m�nimo para la recarga";
				break;
			case POSTPAID :
				errorMsg = "El valor ingresado es menor al valor m�nimo para la asignaci�n de recursos";
				break;
			default :
				// Nada que hacer por ac� NUNCA debe entrar
					break;
			}
			
			resp.setMsj(errorMsg);
		} else {
			resp.setSuccess(true);
		}
		
		return resp;

	}

	/**
	 * Env�a mensaje con la plantilla EmailProcess.ERROR_INACTIVE_ELEMENTS acorde a la informacion de los parametros
	 * recibidos
	 * @param userId Identificador del usuario para cargar los datos para el mensaje
	 * @param accountId identificador de la cuenta FacilPass para enviar el mensaje
	 * @param error descripcion del error
	 * @author TPS r.bautista
	 */
	private void sendMailMinRechargeValue(Long userId, Long accountId, String error){
		Map<String, Object> tags = new HashMap<String, Object>();
		Date date;
		
		TbSystemUser user = em.find(TbSystemUser.class, userId);
		if (user == null){
			throw new RuntimeException("No se encontro el usuario asignado a la solicitud.");
		}

		date = Calendar.getInstance().getTime();
		
		tags.put(EEmailParam.TIPO_ID.getTag(), user.getTbCodeType().getCodeTypeId());
		tags.put(EEmailParam.NUM_ID.getTag(), userId);
		tags.put(EEmailParam.NOMB_CLI.getTag(), userId);
		tags.put(EEmailParam.EMAIL_USU.getTag(), userId);
		tags.put(EEmailParam.CUENTA_FACIL_PASS.getTag(), accountId);
		tags.put(EEmailParam.FECHA_ACCION.getTag(), date);
		tags.put(EEmailParam.HORA_ACCION.getTag(), date);
		tags.put(EEmailParam.ERROR_DESC.getTag(), error);
		
		MessageParser mp = new SimpleTagMessageParser(tags);
		// NTF-39
		outbox.insertMessageOutbox2(userId, EmailProcess.ERROR_INACTIVE_ELEMENTS, true, mp);
	}


	/**
	 * @see ejb.ElectronicRecharge#insertElectronicRechargePostpaid(String, Long, Long, Long, Long, Long, Date, TypeProgramming, Long)
	 */
	@Override
	public Long insertElectronicRechargePostpaid(String ip, Long userId,
			Long idClientAccount, Long valueRecharge, Long frecuencySelect,
			Long creationUser, Date dateRecharge, TypeProgramming tPrograming, 
			Long balanceValue) {
		Long autoRechargeId = null;
		

		try {
			// Se mantiene porque Luis Gabriel dijo
			Query q = 
					em.createNativeQuery("SELECT tar.automatic_recharge_id, tb.bank_name, tba.bank_account_number, tar.automatic_recharge_value, tar.account_id, tf.frequency_description, tar.automatic_recharge_date " +
						             "FROM tb_automatic_recharge tar " +
			                         "INNER JOIN re_account_bank rab ON tar.account_id= rab.account_id "  +
			                         "INNER JOIN tb_bank_account tba ON rab.bank_account_id = tba.bank_account_id "  +
			                         "INNER JOIN tb_bank tb ON tba.product= tb.bank_id "  +
			                         "INNER JOIN tb_frequency tf ON tar.frecuency_id = tf.frequency_id "  +
			                         "WHERE tar.account_id=?1 AND tar.estado <>2 "+
			                         "and rab.STATE_ACCOUNT_BANK=1");
		
			q.setParameter(1, idClientAccount);
			q.getSingleResult();
			return -1L;
		 } catch (NoResultException e) {
			 Timestamp nextExecution = null;
			 Timestamp lastexecution = null;
			 SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			 String dateRec = null;

			 TbFrequency frequency  = em.find(TbFrequency.class, frecuencySelect);
			 if ( tPrograming.equals(TypeProgramming.FREQUENCY) ){
				 dateRec = sdf.format(dateRecharge);
				 String timeRec = parametros.getParameterValueById(ESystemParameters.H_HORA_RECARGA.getId());
				 if (timeRec == null) {
					 timeRec = "02:00:00";
				 }
				
				 String fullDateRec = dateRec + " " + timeRec;
				 SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				 Date parsedDate = null;
				 try {
					 parsedDate = dateFormat.parse(fullDateRec);
				 } catch (ParseException e1) {
					 e1.printStackTrace();
				 }

				 Timestamp tm = new Timestamp(parsedDate.getTime());
				 Long difMillisecons = tm.getTime() - dateRecharge.getTime();
				 Calendar dateRechageCal = new GregorianCalendar();
				 dateRechageCal.setTime(dateRecharge);
				 dateRechageCal.add(Calendar.MILLISECOND, difMillisecons.intValue());
				 nextExecution = new Timestamp(dateRechageCal.getTimeInMillis());
				 Calendar frecuenciaCalendar=Calendar.getInstance();
				 frecuenciaCalendar.setTimeInMillis(frequency.getFrequencyEquiv().longValue());
				 Calendar cLastexecution = this.returnCalendarLastExecution(frecuenciaCalendar, dateRechageCal);
				 lastexecution = new Timestamp(cLastexecution.getTimeInMillis());
			 }

			 TbAccount account = em.find(TbAccount.class, idClientAccount);
			 TbTypeProgramming typeProgramming = em.find(TbTypeProgramming.class, tPrograming.getId());
			 Date arDate = null;
			
			// TypeProgramming tPrograming
			 if ( tPrograming.equals(TypeProgramming.FREQUENCY) ){
				 arDate = dateRecharge;
			 }
			
			 Integer status = 0;  // Estado Activo

			 TbAutomaticRecharge re = new TbAutomaticRecharge();
			 re.setAccountId(account);
			 re.setAutomaticRechargeValue(valueRecharge);
			 re.setFrequencyId(frequency);
			 re.setAutomaticRechargeDate(arDate);
			 re.setNextExecution(nextExecution);
			 re.setLastExecution(lastexecution);
			 re.setEstado(status);
			 // Se deja proceso CERO porque Luis Gabriel dijo
			 re.setProceso(0);
			 re.setCreationDate(new Date());
			 re.setTypeProgramming(typeProgramming);

			 objectEM = new ObjectEM(em);
			 if (objectEM.create(re)) {
				 // Se asocia el trigger si es necesario
				 this.createTrigger(tPrograming, re, balanceValue);
				 
				 DecimalFormat fValue = new DecimalFormat("#,###,###,###");
				 String stReValue = fValue.format(re.getAutomaticRechargeValue());
				
				 String mensajeLog, mensajeProceso;
				 if (tPrograming.equals(TypeProgramming.FREQUENCY)){
					 mensajeLog = "Crear Recursos Programados | Se ha creado Recursos Programados ID " + re.getAutomaticRechargeId()+ ", " +
							  " asociado a la Cuenta FacilPass No. " + re.getAccountId().getAccountId() + 
				              " por Valor de $" + stReValue + " y Frecuencia " + re.getFrequencyId().getFrequencyDescript();
					 mensajeProceso = "Se cre� la asignaci�n de recursos programados a su Cuenta FacilPass No " + 
							  re.getAccountId().getAccountId() + " por Valor de $" + 
							  stReValue + " con Frecuencia " + re.getFrequencyId().getFrequencyDescript() + 
							  " Iniciando en la siguiente fecha: " + dateRec;
				 } else {
					 String stMinBalance = fValue.format(balanceValue);
					 mensajeLog = "Crear Recursos Programados | Se ha creado Recursos Programados ID " + re.getAutomaticRechargeId()+ ", " +
							  " asociado a la Cuenta FacilPass No. " + re.getAccountId().getAccountId() + 
				              " por Valor de $" + stReValue + " manejando Saldo M�nimo de " + stMinBalance;
					 mensajeProceso = "Se cre� la asignaci�n de recursos programados a su Cuenta FacilPass No " +
							 re.getAccountId().getAccountId() + " por valor de $" + stReValue + " con Saldo M�nimo " +
							 ", ser� efectiva cada vez que el saldo de la cuenta FacilPass llegue a " + stMinBalance;
				 }
				
				 log.insertLog( mensajeLog, LogReference.AUTOMATICRECHARGE, LogAction.CREATE, ip, creationUser);
					
				 ///////////////////////////
				 //proceso administrador 
				 this.createTbProcessTrack(ProcessTrackType.CLIENT, userId, ip, creationUser, mensajeProceso);
				 //	proceso cliente
				 this.createTbProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, userId, ip, creationUser, mensajeProceso);
				 autoRechargeId = re.getAutomaticRechargeId();
			 }

		 } catch (Exception e) {
			 System.out.println(" [ Error en ElectronicRechargeEJB.insertElectronicRecharge. ] ");
			 e.printStackTrace();
		 }
		
		return autoRechargeId;
	}

	/**
	 * Asocia a la recarga automatica un trigger en caso de ser saldo minimo
	 * @param tPrograming tipo de programacion
	 * @param ar automatic recharge
	 * @param balanceValue valor del saldo
	 * @author TPS r.bautista
	 */
	private void createTrigger(TypeProgramming tPrograming, TbAutomaticRecharge ar, Long balanceValue){
		if ( tPrograming.equals(TypeProgramming.MINIMUM_BALANCE)) {
			TbArMinBalance tam = new TbArMinBalance();
			// TODO se debe preguntar si se debe validar para asignar CERO o UNO al trigger
			tam.setTransactionTrigger(0);  // Inactivo
			tam.setArMinBalanceState(0L);  // Activo
			tam.setTbAutomaticRecharge(ar);
			tam.setMinBalance(balanceValue.doubleValue());
			objectEM.create(tam);
		}

	}

	@SuppressWarnings("rawtypes")
	private String createProcessMessage(TbAutomaticRecharge tar)
	{
		DecimalFormat df=new DecimalFormat();
		df.setMaximumFractionDigits(0);
		
		StringBuilder builder=new StringBuilder();
		String MMMM=null;
		String YYYY=""+tar.getAccountId().getAccountId();
		String XXXX=""+df.format(tar.getAutomaticRechargeValue());
		String EE=null;
		String FF=null;
		String PP=null;
		String SS=null;
		
		TbAccount accountId = tar.getAccountId();
		Query query1=em.createQuery("SELECT rab FROM ReAccountBank rab WHERE rab.accountId=:account_param");
		query1.setParameter("account_param", accountId);
		List results=query1.getResultList();
		
		if(results!=null && results.size()>0)
		{
		 TbBankAccount tba=((ReAccountBank)results.get(0)).getBankAccountId();
		 EE=tba.getProduct().getBankName();
		 
		 ReAccountBank rab=(ReAccountBank)results.get(0);
		 
		 switch(rab.getPaymentMethodId().getPaymentMethodId().intValue())
		 {
		  case 1:
		  	MMMM=" recarga ";
		  	break;
		  case 2:
		  	MMMM=" asignaci�n ";
		  	break;
		 }
		}
		
		
		TbTypeProgramming ttp = tar.getTypeProgramming();
		switch(ttp.getTypeProgramminId().intValue())
		{
		 case 1:
		 	{
		 	 FF=" programaci�n por frecuencia ";
		 	 PP=tar.getFrequencyId().getFrequencyDescript();
		 	 builder.append("Se desactivo la ").append(MMMM).append(" de recursos programados a su cuenta FacilPass No ").append(YYYY);
		 	 builder.append(" por valor de $").append(XXXX).append(" de la entidad ").append(EE).append(" con ").append(FF).append(" ").append(PP);
		 	}
		 	break;
		 case 2:
		 	{
		 		
		 		
		 		FF=" programaci�n por saldo m�nimo ";
		 				
		 	 Query query=em.createQuery("SELECT tarmb FROM TbArMinBalance tarmb WHERE tarmb.tbAutomaticRecharge=:aut_param");
		 	 query.setParameter("aut_param", tar);
		 	 TbArMinBalance balanceObject=(TbArMinBalance)query.getSingleResult();
		 	 SS="$" + df.format(balanceObject.getMinBalance());
		 	 
		 	 
		 	 
		 	 builder.append("Se desactivo la ").append(MMMM).append(" de recursos programados a su cuenta FacilPass No ").append(YYYY);
		 	 builder.append(" por valor de $").append(XXXX).append(" con ").append(FF).append(" para la entidad ").append(EE).append(" se ejecutaba cada vez que el saldo de la cuenta FacilPass ");
		 	 builder.append(" llegaba a ").append(SS);
		 	}
		 	break;
		}
		return builder.toString();
	}


	/**
	 * @see ejb.ElectronicRecharge#getPrepaidValue()
	 */
	@Override
	public String getPrepaidValue() {
		return parametros.getParameterValueById(ESystemParameters.V_MIN_RECARGA_PREPAG.getId());
	}

	/**
	 * @see ejb.ElectronicRecharge#getPostPaidValue()
	 */
	@Override
	public String getPostPaidValue(){
		return parametros.getParameterValueById(ESystemParameters.V_MIN_ASIG_CUENTA_POST.getId());
	}

}