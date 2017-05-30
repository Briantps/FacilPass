package ejb;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jpa.ReAccountBank;
import jpa.TbAccount;
import jpa.TbProcessTrack;
import jpa.TbProcessTrackDetailType;

import constant.EmailProcess;
import constant.LogAction;
import constant.LogReference;
import constant.ProcessTrackDetailType;
import constant.ProcessTrackType;

import crud.ObjectEM;

import ejb.email.Outbox;

import util.AllInfoAccount;
import util.TimeUtil;

/**
 * Session Bean implementation class AccountCloseEJB
 */
@Stateless(mappedName = "ejb/AccountClose")
public class AccountCloseEJB implements AccountClose{
	
	@PersistenceContext(unitName = "bo")
	private EntityManager em;
	
	private ObjectEM objectEM;
	
	@EJB(mappedName="ejb/User")
	private User UserEJB;
	
	@EJB(mappedName="ejb/Device")
	private Device deviceEJB;
	
	@EJB(mappedName="ejb/Process")
	private Process processEJB;
	
	@EJB(mappedName="ejb/Log")
	private Log logEJB;
	
	@EJB(mappedName="ejb/Task")
	private Task taskEJB;
	
	@EJB(mappedName ="ejb/email/Outbox")
	private Outbox outboxEJB;
	
    /**
     * Default constructor. 
     */
    public AccountCloseEJB() {
    }


	@SuppressWarnings("unchecked")
	@Override
	public List<AllInfoAccount> getAllAccountClose() {
		List<AllInfoAccount> list = new ArrayList<AllInfoAccount>();
		
		try {
			Query q= em.createNativeQuery("SELECT tsu.USER_CODE, " +
					"tsu.user_names ||' '|| tsu.user_second_names, " +
					"rab.account_bank_id, " +
					"rab.account_id, " +
					"re.observations, " +
					"ta.account_state_type_id, " +
					"rab.state_account_bank, " +
					"tb.bank_id, " +
					"tb.BANK_NAME, " +
					"decode(re.account_close_log_date,null,' ',TO_CHAR(re.account_close_log_date, 'DD/MM/YYYY HH:MI:SS AM')), "+
					"decode(rab.date_dissociation,null,' ',TO_CHAR(rab.date_dissociation, 'DD/MM/YYYY HH:MI:SS AM')), " +
					"tb.bank_aval "+
					"FROM RE_ACCOUNT_BANK rab " +
					"INNER JOIN " +
					"   (SELECT account_id, MAX(DATE_ASSOCIATION) fecha " +
					"   FROM RE_ACCOUNT_BANK " +
					"   GROUP BY account_id) rel " +
					"ON rab.DATE_ASSOCIATION=rel.fecha " +
					"INNER JOIN TB_ACCOUNT ta ON rab.account_id=ta.account_id " +
					"LEFT JOIN " +
					"   (SELECT tl.observations, tl.account_id, tl.account_close_log_date " +
					"   FROM tb_account_close_log tl " +
					"   INNER JOIN tb_account ta ON tl.account_id=ta.account_id " +
					"   WHERE tl.ACCOUNT_CLOSE_LOG_STATE=0) re " +
					"ON ta.account_id=re.account_id " +
					"INNER JOIN tb_system_user tsu ON ta.USER_ID=tsu.USER_ID " +
					"INNER JOIN TB_BANK_ACCOUNT tba ON rab.BANK_ACCOUNT_ID=tba.BANK_ACCOUNT_ID " +
					"INNER JOIN TB_BANK tb ON tba.PRODUCT =tb.BANK_ID " +
					"WHERE ta.account_state_type_id=2 " +
					"union " +
					"SELECT tsu.USER_CODE, " +
					"tsu.user_names ||' '|| tsu.user_second_names, " +
					"rab.account_bank_id, " +
					"rab.account_id, " +
					"re.observations, " +
					"ta.account_state_type_id, " +
					"rab.state_account_bank, " +
					"tb.bank_id, " +
					"tb.BANK_NAME, " +
					"decode(re.account_close_log_date,null,' ',TO_CHAR(re.account_close_log_date, 'DD/MM/YYYY HH:MI:SS AM')), "+
					"decode(rab.date_dissociation,null,' ',TO_CHAR(rab.date_dissociation, 'DD/MM/YYYY HH:MI:SS AM')), " +
					"tb.bank_aval "+
					"FROM RE_ACCOUNT_BANK rab " +
					"INNER JOIN " +
					"   (SELECT account_id, DATE_ASSOCIATION fecha " +
					"   FROM RE_ACCOUNT_BANK " +
					"   GROUP BY account_id,DATE_ASSOCIATION) rel " +
					"ON rab.DATE_ASSOCIATION=rel.fecha " +
					"INNER JOIN TB_ACCOUNT ta ON rab.account_id=ta.account_id " +
					"LEFT JOIN " +
					"   (SELECT tl.observations, tl.account_id, tl.account_close_log_date " +
					"   FROM tb_account_close_log tl " +
					"   INNER JOIN tb_account ta ON tl.account_id=ta.account_id " +
					"   WHERE tl.ACCOUNT_CLOSE_LOG_STATE=0) re " +
					"ON ta.account_id=re.account_id " +
					"INNER JOIN tb_system_user tsu ON ta.USER_ID=tsu.USER_ID " +
					"INNER JOIN TB_BANK_ACCOUNT tba ON rab.BANK_ACCOUNT_ID=tba.BANK_ACCOUNT_ID " +
					"INNER JOIN TB_BANK tb ON tba.PRODUCT =tb.BANK_ID " +
					"WHERE rab.state_account_bank = 4 " +
					"union " +
					"Select tu.user_code, " +
					"tu.user_names||' '||tu.user_second_names, " +
					"nvl(rab.ACCOUNT_BANK_ID,0), " +
					"ta.account_id, " +
					"tacl.observations, " +
					"ta.account_state_type_id, " +
					"nvl(rab.state_account_bank,0), " +
					"nvl(tb.bank_id,0), " +
					"tb.bank_name, " +
					"decode(tacl.account_close_log_date,null,' ',TO_CHAR(tacl.account_close_log_date, 'DD/MM/YYYY HH:MI:SS AM')), " +
					"decode(rab.date_dissociation,null,' ',TO_CHAR(rab.date_dissociation, 'DD/MM/YYYY HH:MI:SS AM')), " + 
					"nvl(tb.bank_aval,1) " +
					"From Tb_Account_Close_Log tacl " +
					"inner join tb_account ta on tacl.account_id=ta.account_id " +
					"inner join tb_system_user tu on ta.user_id = tu.user_id " +
					"left join re_account_bank rab on rab.account_id = ta.account_id " +
					"left join tb_bank_account tba on rab.bank_account_id = tba.bank_account_id " +
					"left join tb_bank tb on tba.product = tb.bank_id " +
					"Where ta.ACCOUNT_STATE_TYPE_ID=2 " +
					"And tacl.ACCOUNT_CLOSE_LOG_TYPE=1 " +
					"And tacl.ACCOUNT_CLOSE_LOG_STATE=0 " +
			"and rab.ACCOUNT_BANK_ID is null");
                        
			List<Object> lis= (List<Object>)q.getResultList();
			
				for(int i=0;i<lis.size();i++){
					Object[] obj=(Object[]) lis.get(i);
					
					AllInfoAccount acoountClose= new AllInfoAccount();
					
					if(acoountClose!=null){
						acoountClose.setUserCodeU(obj[0].toString());
						acoountClose.setUserNameU(obj[1].toString());
						acoountClose.setAccountBankIdU(obj[2]!=null?obj[2].toString():"-");
						acoountClose.setAccountIdU(obj[3].toString());
						acoountClose.setObservationU(obj[4]!=null?obj[4].toString():"-");
						acoountClose.setAccountStateTypeU(obj[5].toString());
						acoountClose.setStateAccountBankU(obj[6]!=null?obj[6].toString():"-");
						acoountClose.setBankIdU(obj[7]!=null?obj[7].toString():"-");
						acoountClose.setBankNameU(obj[8]!=null?obj[8].toString():"-");
						acoountClose.setAccountCloseLogDateU(obj[9]!=null?obj[9].toString():"-");
						acoountClose.setDateDissociationU(obj[10]!=null?obj[10].toString():"-");
						acoountClose.setTipBank(obj[11]!=null?obj[11].toString():"-");
					list.add(acoountClose);
					}
				 }	
			} catch (Exception e) {
				System.out.println(" [ Error en AccountCloseEJB.getAllAccountClose. ] ");
				e.printStackTrace();
			}
			return list;
	}


	@Override
	public boolean closeAccount(Long accountId, Long accountBankId, String ip, Long creationUser) {
			boolean result= false;
			String message ="";
			Long idProc = 0L;
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
					q.setParameter(1,accountId);
					tipBnk = ((BigDecimal) q.getSingleResult()).longValue();
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Error en consulta tipo de banco closeAccount");
					tipBnk =1L;
				}
				try {
					Query q2 = em.createNativeQuery(" select ACCOUNT_STATE_TYPE_ID from TB_ACCOUNT where ACCOUNT_ID =  ?1");
					q2.setParameter(1,accountId);
					stateAccount = ((BigDecimal) q2.getSingleResult()).longValue();
					System.out.println("Estado de cuenta Q " + stateAccount);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Error en consulta estado de cuenta");
					stateAccount =0L;
				}
				
				
				System.out.println("Tipo de Cuenta: " + tipBnk);
				System.out.println("Estado de cuenta " + stateAccount);
				System.out.println("accountBankId " + accountBankId);
				
				Long rabId = UserEJB.getProductByAccount(accountId);
				ReAccountBank rab = em.find(ReAccountBank.class, rabId);
				TbAccount account = em.find(TbAccount.class, accountId);

				Query query = em.createNativeQuery("UPDATE re_account_bank " +
							                        "SET date_approve_dissociation=?1, " +
							                        "state_account_bank=2 " +
							                        "WHERE account_id=?2 " +
							                        "AND account_bank_id = ?3 ");
				query.setParameter(1,new Timestamp(System.currentTimeMillis()));
				query.setParameter(2,accountId);
				query.setParameter(3,accountBankId);
				query.executeUpdate();

				objectEM = new ObjectEM(em);
				if(objectEM.update(rab)){	
					 //proceso administrador
						TbProcessTrack proc = processEJB.searchProcess(ProcessTrackType.ACCOUNT1, accountId);				
						if(proc != null){
							idProc = proc.getProcessTrackId();
						} else {
							idProc = processEJB.createProcessTrack(ProcessTrackType.ACCOUNT1, accountId, ip, creationUser);
						}
						if(tipBnk==0L && stateAccount==0L){	
								message = "Se ha Autorizado la disociación de la Cuenta FacilPass "+accountId+".";
						}else{
							if(rabId!=null){
								message = "Se ha Autorizado la cancelación de la Cuenta FacilPass "+accountId+".";
							}else{
								message = "Se ha Realizado la cancelación de la Cuenta FacilPass "+accountId+".";
							}
						}
						if(idProc != 0L){			
							Long detailId = processEJB.createProcessDetail(idProc, ProcessTrackDetailType.CLOSE_ACCOUNT, message, creationUser, ip, 0, 
									" No Se ha podido crear el detalle del proceso ID: " + 
									idProc + ", Tipo detalle:"+ProcessTrackDetailType.CLOSE_ACCOUNT.getId()+".",null,null,null,null);
							
							TbProcessTrackDetailType dt = em.find(TbProcessTrackDetailType.class, ProcessTrackDetailType.CLOSE_ACCOUNT.getId());
							
							// Task Creation.
							taskEJB.createTask(detailId, 0, new Timestamp(System.currentTimeMillis()), 
									TimeUtil.calculateAds(dt.getAdsTime()), dt.getDetailTypePriority(), 
									message, dt.getTbTaskType().getTaskTypeId(), creationUser, ip, null);
							
							//sendMail.sendMail(EmailType.CLIENT, account.getTbSystemUser().getUserEmail(), EmailSubject.CLOSE_ACCOUNT, account.getAccountId().toString());
							outboxEJB.insertMessageOutbox(account.getTbSystemUser().getUserId(), 
					                   EmailProcess.CANCEL_ACCOUNT_APPLY,
					                   accountId,
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
							logEJB.insertLog(message, LogReference.ACCOUNT_TRANSACTION, LogAction.CREATE,ip, creationUser);
							
							result= true;
						} else{
							logEJB.insertLog(message, LogReference.ACCOUNT_TRANSACTION, LogAction.CREATEFAIL,ip, creationUser);
							result= false;
						}
						
						if(UserEJB.intregateBalance(accountId, creationUser, ip)){
							// se crea proceso de dispositivos
							message = "Se ha reintegrado el saldo de los dispositivos relacionados a la cuenta "+account.getAccountId()+
							" del cliente "+account.getTbSystemUser().getUserNames()+" "+account.getTbSystemUser().getUserSecondNames()+".";
							
							TbProcessTrack procDev = processEJB.searchProcess(ProcessTrackType.ACCOUNT1, account.getAccountId());					
							if(procDev != null){
								idProc = procDev.getProcessTrackId();
							} else {
								idProc = processEJB.createProcessTrack(ProcessTrackType.ACCOUNT1, account.getAccountId(), ip, creationUser);
							}
							
							if(idProc != 0L){			
								@SuppressWarnings("unused")
								Long detailId = processEJB.createProcessDetail(idProc, ProcessTrackDetailType.CLOSE_ACCOUNT, message, creationUser, ip, 1, 
										" No Se ha podido crear el detalle del proceso ID: " + 
										idProc + ", Tipo detalle:"+ProcessTrackDetailType.CLOSE_ACCOUNT.getId()+".",null,null,null,null);
								
								@SuppressWarnings("unused")
								TbProcessTrackDetailType dt = em.find(TbProcessTrackDetailType.class, ProcessTrackDetailType.CLOSE_ACCOUNT.getId());
								
								logEJB.insertLog(message, LogReference.ACCOUNT_TRANSACTION, LogAction.CREATE,ip, creationUser);
								result= true;
							} else{
								logEJB.insertLog(message, LogReference.ACCOUNT_TRANSACTION, LogAction.CREATEFAIL,ip, creationUser);
								result= false;
							}
							
							//proceso cliente
							TbProcessTrack 	procDevClient = processEJB.searchProcess(ProcessTrackType.CLIENT, account.getTbSystemUser().getUserId());				
							if(procDevClient != null){
								idProc = procDevClient.getProcessTrackId();
							} else {
								idProc = processEJB.createProcessTrack(ProcessTrackType.CLIENT, account.getTbSystemUser().getUserId(), ip, creationUser);
							}
							
							if(idProc != 0L){			
								@SuppressWarnings("unused")
								Long detailIdClient = processEJB.createProcessDetail(idProc, ProcessTrackDetailType.CLOSE_ACCOUNT, message, creationUser, ip, 1, 
										" No Se ha podido crear el detalle del proceso ID: " + 
										idProc + ", Tipo detalle:"+ProcessTrackDetailType.CLOSE_ACCOUNT.getId()+".",null,null,null,null);
								
								@SuppressWarnings("unused")
								TbProcessTrackDetailType dt = em.find(TbProcessTrackDetailType.class, ProcessTrackDetailType.CLOSE_ACCOUNT.getId());
								
								logEJB.insertLog(message, LogReference.CLIENT, LogAction.CREATE,ip, creationUser);
								result= true;
							} else{
								logEJB.insertLog(message, LogReference.CLIENT, LogAction.CREATEFAIL,ip, creationUser);
								result= false;
							}
							
							//Se crea el proceso de para Mi Proceso
							TbProcessTrack 	procDevMiClient = processEJB.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, account.getTbSystemUser().getUserId());				
							if(procDevMiClient != null){
								idProc = procDevMiClient.getProcessTrackId();
							} else {
								idProc = processEJB.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, account.getTbSystemUser().getUserId(), ip, creationUser);
							}
								
							if(idProc != 0L){			
								
								@SuppressWarnings("unused")
								Long detailIdClient = processEJB.createProcessDetail(idProc, ProcessTrackDetailType.CLOSE_ACCOUNT, message, creationUser, ip, 1, 
										" No Se ha podido crear el detalle del proceso ID: " + 
										idProc + ", Tipo detalle:"+ProcessTrackDetailType.CLOSE_ACCOUNT.getId()+".",null,null,null,null);
								
								@SuppressWarnings("unused")
								TbProcessTrackDetailType dt = em.find(TbProcessTrackDetailType.class, ProcessTrackDetailType.CLOSE_ACCOUNT.getId());
								
								logEJB.insertLog(message, LogReference.CLIENT, LogAction.CREATE,ip, creationUser);
								result= true;
							} else{
								logEJB.insertLog(message, LogReference.CLIENT, LogAction.CREATEFAIL,ip, creationUser);
								result= false;
							}
						}else{
							if(deviceEJB.getDevicesByAccount(accountId).size()<=0){
								//Se crea el proceso de cliente cuando no se tienen dispositivos asociados
								
								TbProcessTrack 	procDevClient = processEJB.searchProcess(ProcessTrackType.CLIENT, account.getTbSystemUser().getUserId());				
								if(procDevClient != null){
									idProc = procDevClient.getProcessTrackId();
								} else {
									idProc = processEJB.createProcessTrack(ProcessTrackType.CLIENT, account.getTbSystemUser().getUserId(), ip, creationUser);
								}
								
								if(idProc != 0L){			
									@SuppressWarnings("unused")
									Long detailIdClient = processEJB.createProcessDetail(idProc, ProcessTrackDetailType.CLOSE_ACCOUNT, message, creationUser, ip, 1, 
											" No Se ha podido crear el detalle del proceso ID: " + 
											idProc + ", Tipo detalle:"+ProcessTrackDetailType.CLOSE_ACCOUNT.getId()+".",null,null,null,null);
									
									@SuppressWarnings("unused")
									TbProcessTrackDetailType dt = em.find(TbProcessTrackDetailType.class, ProcessTrackDetailType.CLOSE_ACCOUNT.getId());
									
									logEJB.insertLog(message, LogReference.CLIENT, LogAction.CREATE,ip, creationUser);
									result= true;
								} else{
									logEJB.insertLog(message, LogReference.CLIENT, LogAction.CREATEFAIL,ip, creationUser);
									result= false;
								}
								
								//Se crea el proceso de para Mi Proceso cuando no se tienen dispositivos asociados
								TbProcessTrack 	procDevMiClient = processEJB.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, account.getTbSystemUser().getUserId());				
								if(procDevMiClient != null){
									idProc = procDevMiClient.getProcessTrackId();
								} else {
									idProc = processEJB.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, account.getTbSystemUser().getUserId(), ip, creationUser);
								}
									
								if(idProc != 0L){			
									@SuppressWarnings("unused")
									Long detailIdClient = processEJB.createProcessDetail(idProc, ProcessTrackDetailType.CLOSE_ACCOUNT, message, creationUser, ip, 1, 
											" No Se ha podido crear el detalle del proceso ID: " + 
											idProc + ", Tipo detalle:"+ProcessTrackDetailType.CLOSE_ACCOUNT.getId()+".",null,null,null,null);
									
									@SuppressWarnings("unused")
									TbProcessTrackDetailType dt = em.find(TbProcessTrackDetailType.class, ProcessTrackDetailType.CLOSE_ACCOUNT.getId());
									
									logEJB.insertLog(message, LogReference.CLIENT, LogAction.CREATE,ip, creationUser);
									result= true;
								} else{
									logEJB.insertLog(message, LogReference.CLIENT, LogAction.CREATEFAIL,ip, creationUser);
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
						logEJB.insertLog(message, LogReference.ACCOUNT_TRANSACTION, LogAction.CREATEFAIL,ip, creationUser);
						result= false;
					}
			} catch (Exception e) {
				System.out.println(" [ Error en AccountCloseEJB.closeAccount ] ");
				e.printStackTrace();
				return false;
			}	
			return result;
		}



}
