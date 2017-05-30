package ejb;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import util.BankAccount;

import jpa.ReAccountBank;
import jpa.ReAccountDevice;
import jpa.ReUserRole;
import jpa.TbAccount;
import jpa.TbAlarmBalance;
import jpa.TbBank;
import jpa.TbBankAccount;
import jpa.TbDevice;
import jpa.TbDeviceState;
import jpa.TbPaymentMethod;
import jpa.TbProcessTrack;
import jpa.TbProductType;
import jpa.TbPse;
import jpa.TbRole;
import jpa.TbSystemUser;
import jpa.TbTag;
import jpa.TbTransaction;
import jpa.TbTransactionType;
import constant.AccountStateType;
import constant.DeviceState;
import constant.EmailProcess;
import constant.LogAction;
import constant.LogReference;
import constant.ProcessTrackDetailType;
import constant.ProcessTrackType;
import constant.TypeTask;
import crud.ObjectEM;
import ejb.balance.TimerBalance;
import ejb.email.Outbox;
import ejb.taskeng.TransitTask;

@Stateless(mappedName = "ejb/AssociationPaymentMethod")
public class AssociationPaymentMethodEJB implements AssociationPaymentMethod {
   
	@PersistenceContext(unitName = "bo") 
	EntityManager em;

	private ObjectEM objectEM;
	
	@EJB(mappedName ="ejb/email/Outbox")
	private Outbox outbox;
	
	@EJB(mappedName="ejb/Log")
	private Log log;
	
	@EJB(mappedName="ejb/Process")
	private Process process;
	
	@EJB(mappedName="ejb/Role")
	private Role role;
	
	@EJB(mappedName ="ejb/User")
	private User userEJB;
	
	@EJB(mappedName="ejb/TransitTask")
	private TransitTask transitTask;
	
	@EJB(mappedName="ejb/TimerBalance")
	private TimerBalance balanceCalulations;
	
	@EJB(mappedName="ejb/MinimumBalance")
	private MinimumBalance MinimumBalanceEJB;

	
	/**
	 * Método creado para asociar la cuenta facilpass con el producto bancario aval:
	 * re_account_bank (state_account_bank=2), tb_device (device_state_id=7), tag (equipmentstatus=7).
	 * @return the msg
	 * @author psanchez
	 */
	@Override
	public Long associateProductsAccount(Long userId, Long accountId, String codeClient, 
			Long creationUser, String ip, Long idBank, Long idProduct, Long initValue) {
		try {			
		    TbSystemUser user = em.find(TbSystemUser.class, userId);
			objectEM = new ObjectEM(em);	
			
			if (user != null) {
			
		    TbAccount account = em.find(TbAccount.class, accountId);
		    TbBankAccount tba= em.find(TbBankAccount.class, idProduct);			    
		    
		    /*Valida si el producto bancario asociado ya tenia una asociación previa*/
		    ReAccountBank rab = this.getRelationProduct(accountId, idProduct);
		    if(rab!=null){
		    	/*Se activa el producto bancario existente y se crea el umbral*/
		    	rab.setDate(new Timestamp(System.currentTimeMillis()));
		    	rab.setDigits(null);
				rab.setPriority(1L);
				rab.setState_account_bank(1);
				rab.setDateSendBank(null);
				rab.setPaymentMethodId(em.find(TbPaymentMethod.class, (long) constant.PaymentMethod.POSTPAID.getId()));
				em.merge(rab);
				em.flush();
		    }	
		    else{
				rab= new ReAccountBank();
				rab.setAccountId(account);
				rab.setBankAccountId(tba);
				rab.setDate(new Timestamp(System.currentTimeMillis()));		
				rab.setDigits(null);
				rab.setPriority(1L);
				rab.setState_account_bank(1);
				rab.setPaymentMethodId(em.find(TbPaymentMethod.class, (long) constant.PaymentMethod.POSTPAID.getId()));
				em.persist(rab);
				em.flush();
		    }

		    Query qry = em.createQuery("select p.systemParameterValue from TbSystemParameter p where p.systemParameterId in (4L)");
		    
		    Long alarmBalance = null;		        	    
			Query qra = em.createNativeQuery("select id_alarm_balance from tb_alarm_balance where id_tip_alarm = 4 and account_id = ?1");
			qra.setParameter(1, account);
			alarmBalance = ((BigDecimal) qra.getSingleResult()).longValue();
			if (alarmBalance!=null){						
				TbAlarmBalance alarm = em.find(TbAlarmBalance.class, alarmBalance);						
				alarm.setValMinAlarm(Long.parseLong((String) qry.getSingleResult()));
				alarm.setValminimo(0L);
				objectEM.update(alarm);						
			}
		    /* Se elimina el for ya que no cumple funcion alguna
		    for(Object obj : qry.getResultList()) {
		    	TbSystemParameter parameter = (TbSystemParameter) obj;
		    	System.out.println("Nombre del Parametro: "+parameter.getSystemParameterName());
				System.out.println("valor: "+parameter.getSystemParameterValue());	    
	            
	            //se elimina la persistencia en tb_alarm_balance ya que esto solo se debe realizar cuando se crea la cuenta facilpass 
	            
	            TbAlarmBalance alarm = new TbAlarmBalance();
	    		alarm.setIdTipAlarm(parameter.getSystemParameterId());
				alarm.setValMinAlarm(new Long(parameter.getSystemParameterValue()));
	    		alarm.setFrequency(new Long(frecuency));
	    		alarm.setLastExecution(new Timestamp(System.currentTimeMillis()));
	    		alarm.setEstAlarm(1);
	    		alarm.setAccountId(account.getAccountId());
	    		em.persist(alarm);
				em.flush();			
		    }
			*/
			if(!role.validateUserRole(user.getUserId(), (long)constant.Role.CLIENT.getId())){
				ReUserRole ur = new ReUserRole();
				ur.setTbRole(em.find(TbRole.class, (long) constant.Role.CLIENT.getId()));
				ur.setTbSystemUser(user);
				em.persist(ur);
				em.flush();
			}
		    
		    //Se envia tarea por comunicaciones informando al banco para que proceda a activar el asocio de la cuenta facilPass con la cuenta de banco
			transitTask.createTask(TypeTask.ACCOUNT, account.getAccountId().toString());	
			return account.getAccountId();
			  
		}	
			
	} catch (Exception e) {
		System.out.println(" [ Error AssociationPaymentMethodEJB.associateProductsAccount ] ");
		e.printStackTrace();
	}
     return null;
    }
	
	
	@SuppressWarnings("unused")
	@Override
	public Long processAssociation(Long userId, String codeClient, Long accountId, Long creationUser, 
			                       String ip, Long idBank, Long idProduct, Long resp, Long lastIdBankAccount) {
	  try {
			objectEM = new ObjectEM(em);	
		    TbSystemUser user = em.find(TbSystemUser.class, userId);
		    TbAccount account = em.find(TbAccount.class, accountId);
		    TbBankAccount tba= em.find(TbBankAccount.class, idProduct);			    
		    TbBank tb= em.find(TbBank.class, idBank);
		    
		    user.setUserState(userEJB.getState(2));
	        em.merge(user);
	        em.flush();
	        
	        /** se realiza reembolso del saldo de la cuenta si este es mayor a cero
	         * @author ablasquez 
	         */
	        Long saldoActual = balanceCalulations.saldoActualCta(account.getAccountId(), account.getDistributionTypeId().getDistributionTypeId().intValue()); 
	        System.out.println("account.getAccountBalance().longValue()-->"+saldoActual);
	        System.out.println("lastIdBankAccount-->"+lastIdBankAccount);
	        if(saldoActual>0L && lastIdBankAccount!=null){
	        	ReAccountBank rab = em.find(ReAccountBank.class, lastIdBankAccount);
	        	
	        	TbTransaction ttran = new TbTransaction();
	        	ttran.setTbAccount(account);
	        	ttran.setTbTransactionType(em.find(TbTransactionType.class,7L));
	        	ttran.setTransactionTime(new Timestamp(System.currentTimeMillis()));
	        	ttran.setPreviousBalance(account.getAccountBalance().longValue());
	        	ttran.setTransactionValue(account.getAccountBalance().longValue());
	        	ttran.setTransactionDescription("Reembolso por disociación de producto bancario "+rab.getBankAccountId().getBankAccountNumber());
	        	ttran.setNewBalance(0L);
	        	ttran.setCodeResult("0");
	        	ttran.setAccountBankId(lastIdBankAccount);
	        	ttran.setUserId(account.getTbSystemUser().getUserId());
	        	
	        	objectEM.create(ttran);
	        	
	        	rab.setCodeMoneyBack(2);	        	
	        	objectEM.update(rab);
	        	
	        	account.setAccountBalance(new BigDecimal(0));
	        	objectEM.update(account);
	        }
	        
	        /** Se reactivan los dispositivos asociados a la cuenta 
	         * @author ablasquez
	         */
	        this.activeDeviceByDissociation(accountId);
	        
	        outbox.insertMessageOutbox(user.getUserId(), 
	                   EmailProcess.ACCOUNT_ACTIVATION_SUCCESSFUL,
	                   accountId,
	                   tba.getBankAccountId(),	                   
	                   null, 
	                   null,
	                   null,
	                   null,
	                   tb.getBankId(),
	                   null,
	                   creationUser,
	                   null,
	                   null,
	                   "Asociación",
	                   true,
	                   null);
	     
		    log.insertLog("Asociar Cuenta Cliente |La Cuenta FacilPass No." + account.getAccountId() + " " +
		    			  "se vinculó al producto bancario No. "+ tba.getBankAccountNumber()+ " correspondiente al "
		    			  +tb.getBankName()+".",LogReference.ACCOUNT,LogAction.ASSOCIATION, ip, creationUser);
		       			        
	       //proceso administrador
	        TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, userId);
			Long idPTA;
				if(pt==null){
					idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  userId, ip, creationUser);
				}else{
					idPTA=pt.getProcessTrackId();
				}
			Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.CLIENT_ACCOUNT_ASSOCIATION, 
					   "Se asoció su Cuenta FacilPass No "+ account.getAccountId() + " con el Producto Bancario No "
					   + tba.getBankAccountNumber()+" del "+tb.getBankName()+".", creationUser, ip, 1, "", null,null,null,null);
								
			//proceso cliente
			TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,userId);
			Long idPTC;
				if(ptc==null){
					idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, userId, ip, creationUser);
				}else{
					idPTC=ptc.getProcessTrackId();
				}
			Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.CLIENT_ACCOUNT_ASSOCIATION, 
					 "Se asoció su Cuenta FacilPass No "+ account.getAccountId() + " con el Producto Bancario No "+ 
					 tba.getBankAccountNumber()+" del "+tb.getBankName()+".", creationUser, ip, 1, "", null,null,null,null);
			return 0L;
		 } catch (Exception e) {
				System.out.println(" [ Error AssociationPaymentMethodEJB.processAssociation ] ");
				e.printStackTrace();
		}
       return null;
    }
	

	/**
	 * Método creado para disociar la cuenta facilpass y bloquear pasos de carril a traves de las tablas:
	 * re_account_bank (state_account_bank=2), tb_device (device_state_id=7), tag (equipmentstatus=7).
	 * @return the msg
	 * @author psanchez
	 */
	@SuppressWarnings("unused")
	@Override
	public String dissociationBankAccount(Long userId, Long accountBankId, Long bankId, String ip, Long creationUser) {
		String msg= null;
		String descripProc = null;
		TbBank bank = em.find(TbBank.class, bankId);  
        TbSystemUser user = em.find(TbSystemUser.class, userId);    
		ReAccountBank rab= em.find(ReAccountBank.class, accountBankId);	
		
			try { 
				if(bank.getBankAval()==0){
					rab.setState_account_bank(4); // 4-Pendiente de aprobación de disociación
					rab.setDateDissociation(new Timestamp(System.currentTimeMillis()));
					rab.setCodeMoneyBack(1);
					rab.setDateSendBank(null);
					rab.setDateDissosiationBank(null);
					rab.setCodeResult(null);
					rab.setDateApproveDissociation(null);
					descripProc="Se disoció la cuenta FacilPass "+rab.getAccountId().getAccountId()+" de la entidad "+rab.getBankAccountId().getProduct().getBankName()+".";
				}
				else if(bank.getBankAval()==1){	
					rab.setState_account_bank(2); // desasocio = 2
					rab.setDateDissociation(new Timestamp(System.currentTimeMillis()));
					rab.setCodeMoneyBack(1);
					rab.setDateSendBank(null);
					rab.setDateDissosiationBank(null);
					rab.setCodeResult(null);
					rab.setDateApproveDissociation(new Timestamp(System.currentTimeMillis()));
					descripProc="Se solicitó la disociación de la cuenta FacilPass "+rab.getAccountId().getAccountId()+" del producto bancario N° "+rab.getBankAccountId().getBankAccountNumber()+" de la entidad "+rab.getBankAccountId().getProduct().getBankName()+".";
				}
				Query tagAccount = 
			    	 em.createNativeQuery("SELECT rab.account_id FROM re_account_bank rab " +
			    	 		              "WHERE rab.account_bank_id = ?1 ");
					 tagAccount.setParameter(1, accountBankId);
					 BigDecimal account = (BigDecimal) tagAccount.getSingleResult();
	
					 String stateTag = DeviceState.INACTIVE_BY_DISSOCIATION.getId().toString();
				     Query query = em.createNativeQuery("UPDATE tag SET equipmentstatus=?1 WHERE id_account=?2 and equipmentstatus in (1,3,4)" +
				     		"");
				     query.setParameter(1, stateTag);
			    	 query.setParameter(2, account);
			    	 query.executeUpdate();
			     
			     Query deviceCode = 
					 em.createNativeQuery("SELECT td.device_code FROM re_account_device red  " +
										  "INNER JOIN re_account_bank rab ON red.account_id = rab.account_id " +
										  "INNER JOIN tb_device td ON red.device_id = td.device_id " +
							              "WHERE rab.account_bank_id = ?1 and red.relation_state=0 and td.DEVICE_STATE_ID in (6,3,4)");
				    deviceCode.setParameter(1, accountBankId);
				    List<?> device1 = (List<?>)deviceCode.getResultList();
				    Query query2;
					 for (Object s : device1){
						   int stateDevice = DeviceState.INACTIVE_BY_DISSOCIATION.getId().intValue();
						   if (s != null && s instanceof String) {
							query2 = em.createNativeQuery("UPDATE tb_device SET device_state_id=?1 WHERE device_code =?2 and DEVICE_STATE_ID in(6,3,4)"); // desasocio = 2
							query2.setParameter(1, stateDevice);
							query2.setParameter(2, s);
							query2.executeUpdate();
						   }
					  }
		        em.merge(rab);
				objectEM = new ObjectEM(em);	
				 
				 outbox.insertMessageOutbox(user.getUserId(), 
		                   EmailProcess.DISSOCIATION_ACCOUNT,
		                   rab.getAccountId().getAccountId(),
		                   rab.getBankAccountId().getBankAccountId(), 
		                   null, 
		                   null,
		                   null,
		                   null,
		                   rab.getBankAccountId().getProduct().getBankId(),
		                   null,
		                   creationUser,
		                   user.getTbCodeType().getCodeTypeId(),
		                   null,
		                   "Disociación",
		                   true,
		                   null);
        
				if(objectEM.create(rab)){
					log.insertLog("Desvincular Cuenta FacilPass | Pendiente por Desvincular la Cuenta FacilPass No."+rab.getAccountId().getAccountId()+ 
							      " correspondiente a la entidad "+rab.getBankAccountId().getProduct().getBankName()+" No."+rab.getBankAccountId().getBankAccountNumber()+".",
							      LogReference.BANKACCOUNT, LogAction.DISSOCIATION, ip, creationUser);
					msg="Transacción Exitosa.";	
				} else {
					log.insertLog("Desvincular Cuenta FacilPass | Pendiente por Desvincular la Cuenta FacilPass No."+rab.getAccountId().getAccountId()+ 
							      " correspondiente a la entidad "+rab.getBankAccountId().getProduct().getBankName()+" No."+rab.getBankAccountId().getBankAccountNumber()+".",
							       LogReference.BANKACCOUNT, LogAction.DISSOCIATIONFAIL, ip, creationUser);
					msg="Error en Transacción.";	
			  } 
				
				   //proceso administrador
		        TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, userId);
				Long idPTA;
					if(pt==null){
						idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  userId, ip, creationUser);
					}else{
						idPTA=pt.getProcessTrackId();
					}
				Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.CLIENT_ACCOUNT_ASSOCIATION, 
						descripProc, creationUser, ip, 1, "", null,null,null,null);
									
				//proceso cliente
				TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,userId);
				Long idPTC;
					if(ptc==null){
						idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, userId, ip, creationUser);
					}else{
						idPTC=ptc.getProcessTrackId();
					}
				Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.CLIENT_ACCOUNT_ASSOCIATION, 
						descripProc, creationUser, ip, 1, "", null,null,null,null);
						
		   }catch(NoResultException e){
			   System.out.println(" [ Error AssociationPaymentMethodEJB.dissociationBankAccount.NoResultException ] ");
			   e.printStackTrace();
	       }
		return msg;
	}
	
	
	@Override
	public boolean haveAccountAssociation(Long idProduct) {
		BigDecimal count=null;
		boolean response=false;
		try{
			Query query= 
				em.createNativeQuery("select count(account_id) from re_account_bank " +
									 "where bank_account_id=?1 " +
								     "and state_account_bank=1 ");
			query.setParameter(1, idProduct);
			
			count= (BigDecimal) query.getSingleResult();			
			if (count.longValue()>0L){
				response=true;
			}else{
				response=false;
			}
		}catch(NoResultException ex){
			response=false;			
		}
		return response;
	}
	
	
	@Override
	public String haveProductAssociation(Long idAccount) {
		BigDecimal accountAssociate;
		String result= null;
		try{
			
			Query query= em.createNativeQuery("select ACCOUNT_BANK_ID from VW_LAST_PRODUCT_BANK where ACCOUNT_ID=?1");
			query.setParameter(1, idAccount);
			accountAssociate= (BigDecimal) query.getSingleResult();
			if(accountAssociate!=null){
				ReAccountBank rab = em.find(ReAccountBank.class, accountAssociate.longValue());
				//Se valida si la cuenta esta en proceso de disociación
				if(rab.getState_account_bank()==2 || rab.getState_account_bank()==4){
					if(rab.getDateSendBank() !=null){
						// el proceso se ha enviado al banco y no se ha recibio respuesta del banco
						if(rab.getDateDissosiationBank()==null){
							result="La cuenta FacilPass está en proceso de disociación, por favor intente más tarde.";
						}
					}
				}else if(rab.getState_account_bank()==1){
					result="Esta Cuenta Facilpass ya está asociada a un Producto Bancario.";
				}					
			}			
		}catch(NoResultException e){
			System.out.println("La consulta AssociationPaymentMethodEJB.haveProductAssociation "+e.getLocalizedMessage());
			return null;
		}catch(Exception ex){
			System.out.println(" [ Error AssociationPaymentMethodEJB.haveProductAssociation. ] ");
			return null;
		}
		return result;
	}
	
	
	@Override
	public boolean getAccountAssociation(Long accountId) {
		BigDecimal accountAssociate;
		boolean result= false;
		try{
			
			Query query= em.createNativeQuery("select ACCOUNT_BANK_ID from VW_LAST_PRODUCT_BANK where ACCOUNT_ID=?1");
			query.setParameter(1, accountId);
			accountAssociate= (BigDecimal) query.getSingleResult();
			if(accountAssociate!=null){
				ReAccountBank rab = em.find(ReAccountBank.class, accountAssociate.longValue());
				//Se valida si la cuenta esta en proceso de disociación
				if(rab.getState_account_bank()==1){
					result=true;
				}else {
					result=false;
				}
			}			
		}catch(NoResultException ex){
			System.out.println("La consulta AssociationPaymentMethodEJB.getAccountAssociation "+ex.getLocalizedMessage());
			return false;
		}catch(Exception ex){
			System.out.println("AssociationPaymentMethodEJB.getAccountAssociation "+ex.getLocalizedMessage());
			return false;
		}
		return result;
	}
	
	/**
	 * Método creado para obtener la lista de bancos del cliente en sesión.
	 * @return the list
	 * @author psanchez
	 */
	public List<TbBank> getBanksByClient(Long userId) {		
		List<TbBank> list = new ArrayList<TbBank>();
		try {
			Query q = em.createNativeQuery("select distinct BANK_ID "
											+" from VW_PRODUCTS_ASSOCIATED_USER "
											+" where USRS=?1 "
											+" and ( "
											+"   case  "
											+" WHEN (STATE_ACCOUNT_BANK IS NULL OR STATE_ACCOUNT_BANK=3) THEN 'S' "
											+" WHEN (STATE_ACCOUNT_BANK=2 AND CODE_RESULT IS NOT NULL) THEN 'S' "
											+" WHEN (STATE_ACCOUNT_BANK=2 AND DATE_SEND_BANK IS NULL AND CODE_RESULT IS NULL) THEN 'S' " 
											+"   end "
											+" ) = 'S' "
											+" UNION " 
											+" SELECT distinct BANK_ID " 
											+" FROM Tb_Bank " 
											+" WHERE bank_Aval=0 "
											+" ORDER BY 1 ");

			
			q.setParameter(1, userId);
			for (Object obj : q.getResultList()) {
				list.add(em.find(TbBank.class, Long.parseLong(obj.toString())));
			}
		} catch (Exception e) {
			System.out.println(" [ Error AssociationPaymentMethodEJB.getBanksByClient. ] ");
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * Método creado para obtener la lista de tipo(s) de producto bancario del cliente en sesión.
	 * @return the list
	 * @author psanchez
	 */
	public List<TbProductType> getProductTypeByClient(Long idBank, Long userId) {	
		List<TbProductType> list = new ArrayList<TbProductType>();
		try {
			Query q = 
				em.createNativeQuery("select distinct to_char(PRODUCT_TYPE_ID) "
									+" from VW_PRODUCTS_ASSOCIATED_USER "
									+" where USRS=?2 "
									+" and ( "
									+"   case  "
									+" WHEN (STATE_ACCOUNT_BANK IS NULL OR STATE_ACCOUNT_BANK=3) THEN 'S' "
									+" WHEN (STATE_ACCOUNT_BANK=2 AND CODE_RESULT IS NOT NULL) THEN 'S' "
									+" WHEN (STATE_ACCOUNT_BANK=2 AND DATE_SEND_BANK IS NULL AND CODE_RESULT IS NULL) THEN 'S' "
									+"   end "
									+" ) = 'S' "
									+" and BANK_ID=?1");

			q.setParameter(1, idBank);
			q.setParameter(2, userId.toString());
			for (Object obj : q.getResultList()) {
				list.add(em.find(TbProductType.class, Long.parseLong(obj.toString())));
			}
		} catch (NullPointerException npe){
			System.out.println("[ La consulta AssociationPaymentMethodEJB.getProductTypeByClient no arroja resultado ]"+idBank +";"+userId);
			npe.printStackTrace();
		} catch (Exception e) {
			System.out.println(" [ Error AssociationPaymentMethodEJB.getProductTypeByClient. ] ");
			e.printStackTrace();
		}
		return list;
	}


	public List<TbBank> getBanksByClient2(String codeClient, Long codeType) {		
		List<TbBank> list = new ArrayList<TbBank>();
		try {
			if(codeClient != null && codeType != null){
				Query q=em.createNativeQuery("select tb.user_id from tb_system_user tb where tb.user_code=?1 and tb.code_type_id=?2");
				q.setParameter(1, codeClient);
				q.setParameter(2, codeType);
				
				BigDecimal userId= (BigDecimal) q.getSingleResult();

				Query q1 = em.createNativeQuery("select distinct BANK_ID "
											+" from VW_PRODUCTS_ASSOCIATED_USER "
											+" where USRS=?1 "
											+" and ( "
											+"   case  "
											+" WHEN (STATE_ACCOUNT_BANK IS NULL OR STATE_ACCOUNT_BANK=3) THEN 'S' "
											+" WHEN (STATE_ACCOUNT_BANK=2 AND CODE_RESULT IS NOT NULL) THEN 'S' "
											+" WHEN (STATE_ACCOUNT_BANK=2 AND DATE_SEND_BANK IS NULL AND CODE_RESULT IS NULL) THEN 'S' "
											+"   end "
											+" ) = 'S'");

				q1.setParameter(1, userId);
				for (Object obj : q1.getResultList()) {
					list.add(em.find(TbBank.class, Long.parseLong(obj.toString())));
				}
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * Metodo encargado de validar el saldo de la cuenta antes de disociar un producto bancario
	 * @author ablasquez
	 * @return the ReAccountBank
	 */
	@Override
	public ReAccountBank validateBalanceAccount(Long accountBankId) {		
		try{
			ReAccountBank rab = null;
			rab = em.find(ReAccountBank.class, accountBankId);
			return rab;
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Método encargado de validar si el producto bancario a asociar es el mismo anterior producto bancario
	 * @author ablasquez
	 * @return 0 si es el mismo producto bancario, 1 no es el mismo producto bancario o es el mismo producto bancario pero ya se disocio en el banco, 2 error en el proceso de verificacion
	 */
	@SuppressWarnings("unused")
	@Override
	public int samePreviousProductAssociation(Long accountId,Long bankAccountId, Long creationUser,String ip,Long userId) {
		try{
			int retorno = 0;
			objectEM = new ObjectEM(em);	
			Query q1 = em.createQuery("Select rab From ReAccountBank rab Where rab.accountId.accountId=?1 and rab.bankAccountId.bankAccountId=?2 Order by rab.date desc");
			q1.setParameter(1, accountId);
			q1.setParameter(2, bankAccountId);
			@SuppressWarnings("unchecked")
			List<Object> objs = (List<Object>) q1.getResultList();
			ReAccountBank rab = null;
			if(objs!=null && objs.size()>0){
				rab = (ReAccountBank) objs.get(0);
			}			 
			
			if(rab == null){
				retorno= 1;
			} else{
				Query q2 = em.createQuery("Select rab From ReAccountBank rab Where rab.accountId.accountId=?1 Order by rab.date desc");
				q2.setParameter(1, accountId);
				ReAccountBank previuosRab = (ReAccountBank) q2.getResultList().get(0);
				
				if(previuosRab!=null){					
					if(rab.getAccountBankId().equals(previuosRab.getAccountBankId())){
						
						//se valida el estado de la anterior asociacion
						if((previuosRab.getState_account_bank()==2 && previuosRab.getDateSendBank()==null) || 
						   (previuosRab.getState_account_bank()==2 && previuosRab.getDateSendBank()!=null && rab.getCodeResult()!=0L && rab.getCodeResult()!=null)){
							System.out.println("Esta asociando el mismo producto bancario anterior");
							
							/** Se reactivan los tags */
							if(activeDeviceByDissociation(accountId)){
								/** Se reactiva la asociación */
								previuosRab.setState_account_bank(1);
								previuosRab.setDate(new Timestamp(System.currentTimeMillis()));
								previuosRab.setDateSendBank(null);
								if(objectEM.update(previuosRab)){
									retorno = 0;
									
									log.insertLog("Asociar Cuenta Cliente |La Cuenta FacilPass No." + accountId + " " +
							    			  "se vinculó al producto bancario No. "+ rab.getBankAccountId().getBankAccountNumber()+ " correspondiente al "
							    			  +rab.getBankAccountId().getProduct().getBankName()+".",LogReference.ACCOUNT,LogAction.ASSOCIATION, ip, creationUser);
							       			        
						       //proceso administrador
						        TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, userId);
								Long idPTA;
									if(pt==null){
										idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  userId, ip, creationUser);
									}else{
										idPTA=pt.getProcessTrackId();
									}
								Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.CLIENT_ACCOUNT_ASSOCIATION, 
										   "Se asoció su Cuenta FacilPass No "+ accountId + " con el Producto Bancario No "
										   + rab.getBankAccountId().getBankAccountNumber()+" del "+rab.getBankAccountId().getProduct().getBankName()+".", creationUser, ip, 1, "", null,null,null,null);
													
								//proceso cliente
								TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,userId);
								Long idPTC;
									if(ptc==null){
										idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, userId, ip, creationUser);
									}else{
										idPTC=ptc.getProcessTrackId();
									}
								Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.CLIENT_ACCOUNT_ASSOCIATION, 
										 "Se asoció su Cuenta FacilPass No "+ accountId + " con el Producto Bancario No "+ 
										 rab.getBankAccountId().getBankAccountNumber()+" del "+rab.getBankAccountId().getProduct().getBankName()+".", creationUser, ip, 1, "", null,null,null,null);
									
									
								}else{
									retorno = 2;
								}
							}else{
								retorno= 2;
							}
						}else if (previuosRab.getState_account_bank()==3){
							System.out.println("Esta asociando el mismo producto bancario anterior  con envio de asociacion al banco");
							retorno = 1;
						}
					}else{
						retorno = 1;
					}
				}else{
					retorno= 0;
				}
			}
			return retorno;
		}catch(NoResultException e){
			System.out.println("NoResultException");
			e.printStackTrace();
			return 2;
		}catch (Exception e){
			e.printStackTrace();
			return 2;
		}		
	}
	
	
	/**
	 * Método encargado de validar si el producto pse a asociar es el mismo producto pse
	 * @author psanchez
	 * @return 0 si es el mismo producto bancario, 1 no es el mismo producto bancario, 2 error en el proceso de verificacion
	 */
	@SuppressWarnings("unused")
	@Override
	public int samePreviousProductAssociationPSE(Long accountId, Long bankAccountId, Long creationUser, String ip, Long userId) {

		try{
			int retorno = 0;
			objectEM = new ObjectEM(em);
			Query q1 = em.createQuery("Select rab From ReAccountBank rab Where rab.accountId.accountId=?1 and rab.bankAccountId.bankAccountId=?2 Order by rab.date desc");
			q1.setParameter(1, accountId);
			q1.setParameter(2, bankAccountId);
			@SuppressWarnings("unchecked")
			List<Object> objs = (List<Object>) q1.getResultList();
			ReAccountBank rab = null;
			if(objs!=null && objs.size()>0){
				rab = (ReAccountBank) objs.get(0);
			}			 
			
			if(rab == null){
				retorno= 1;
			} else{
				Query q2 = em.createQuery("Select rab From ReAccountBank rab Where rab.accountId.accountId=?1 Order by rab.date desc");
				q2.setParameter(1, accountId);
				ReAccountBank previuosRab = (ReAccountBank) q2.getResultList().get(0);
				
				if(previuosRab!=null){					
					if(rab.getAccountBankId().equals(previuosRab.getAccountBankId())){
						System.out.println("Esta asociando el mismo producto bancario anterior PSE");
						
						/** Se reactivan los tags */
						if(activeDeviceByDissociation(accountId)){
							/** Se reactiva la asociación */
							previuosRab.setState_account_bank(1);
							previuosRab.setDate(new Timestamp(System.currentTimeMillis()));
							previuosRab.setDateSendBank(null);
							if(objectEM.update(previuosRab)){
								retorno = 0;
								//Se elimina ya que se agrega caculo de saldo Minimo
								//this.alarmBalance(accountId,creationUser,ip);
								MinimumBalanceEJB.setCalculateMinimumBalanceAccount(accountId,creationUser,ip);
								
							log.insertLog("Asociar Cuenta Cliente |La Cuenta FacilPass No." + accountId +  " La Cuenta FacilPass No "+ 
									      rab.getBankAccountId().getBankAccountNumber()+ " se vinculó a la entidad "+
										  rab.getBankAccountId().getProduct().getBankName()+".",LogReference.ACCOUNT,LogAction.ASSOCIATION, ip, creationUser);
						       			        
					       //proceso administrador
					        TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, userId);
							Long idPTA;
								if(pt==null){
									idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  userId, ip, creationUser);
								}else{
									idPTA=pt.getProcessTrackId();
								}
							Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.CLIENT_ACCOUNT_ASSOCIATION, "La Cuenta FacilPass No "+ accountId +
									" se vinculó a la entidad "+rab.getBankAccountId().getProduct().getBankName()+".", creationUser, ip, 1, "", null,null,null,null);
												
							//proceso cliente
							TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,userId);
							Long idPTC;
								if(ptc==null){
									idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, userId, ip, creationUser);
								}else{
									idPTC=ptc.getProcessTrackId();
								}
							Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.CLIENT_ACCOUNT_ASSOCIATION, "La Cuenta FacilPass No "+ accountId +
									" se vinculó a la entidad "+rab.getBankAccountId().getProduct().getBankName()+".", creationUser, ip, 1, "", null,null,null,null);
							}else{
								retorno = 2;								
								/** Se agrega mensaje en Seguimiento de procesos y Ver procesos segun FRD064
								* @author Nramirez
								*/	
								this.createProcessAssociationPSE(userId,accountId,ip);
							}
						}else{
							retorno= 2;							
							/** Se agrega mensaje en Seguimiento de procesos y Ver procesos segun FRD064
							* @author Nramirez
							*/	
							this.createProcessAssociationPSE(userId,accountId,ip);
						}
					}else{
						retorno = 1;
					}
				}else{
					retorno= 0;
				}
			}
			return retorno;
		}catch(NoResultException e){
			e.printStackTrace();			
			/** Se agrega mensaje en Seguimiento de procesos y Ver procesos segun FRD064
			* @author Nramirez
			*/	
			this.createProcessAssociationPSE(userId,accountId,ip);			
			return 2;
			
		}catch (Exception e){
			/*e.getMessage();
			e.getLocalizedMessage();*/
			e.printStackTrace();			
			/** Se agrega mensaje en Seguimiento de procesos y Ver procesos segun FRD064
			* @author Nramirez
			*/	
			this.createProcessAssociationPSE(userId,accountId,ip);
			return 2;
		}		
	}
	
	/**
	 * Método encargado de reactivar los tag inactivos por disociacion
	 * @author ablasquez
	 * @return true cuando se reactivan los dispositivos, de los contrario false
	 */
	@Override
	public boolean activeDeviceByDissociation(Long accountId){
		try{
			boolean retorno = false;
			objectEM = new ObjectEM(em);	
			/** Se obtiene el estado de los tags de acuerdo al saldo de la cuenta */
			TbDeviceState tds = this.getStateDeviceByBalanceAccount(accountId);
			Query q = em.createQuery("Select rad From ReAccountDevice rad Where rad.tbAccount.accountId=?1 and rad.tbDevice.tbDeviceState.deviceStateId=?2");
			q.setParameter(1, accountId);
			q.setParameter(2, DeviceState.INACTIVE_BY_DISSOCIATION.getId());
			
			@SuppressWarnings("unchecked")
			List<Object> objects = (List<Object>)q.getResultList();
			
			if(objects!=null && objects.size()>0){				
				for(Object o : objects){
					ReAccountDevice rad = (ReAccountDevice) o;
					TbDevice td = rad.getTbDevice();
					System.out.println("Activando dispositivo "+td.getDeviceId());
					td.setTbDeviceState(tds);
					if(objectEM.update(td)){
						TbTag tt = em.find(TbTag.class, td.getDeviceCode());
						if(tt!=null){
							if(tds.getDeviceStateId()==DeviceState.ACTIVE.getId()){
								tt.setDeviceStateId(1L);
							}else{
								tt.setDeviceStateId(tds.getDeviceStateId());
							}
							System.out.println("Activando Tag "+tt.getDeviceCode());
							objectEM.update(tt);
						}
					}
					retorno = true;				
				}
			} else{
				System.out.println("No tiene dispositivos");
				retorno = true;
			}			
			
			return retorno;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	/** Método que se encarga de retornar el estado que deben tener los dispositivos deacuerdo al saldo de la cuenta 
	 * @author ablasquez
	 * @return the TbDeviceState
	 */
	public TbDeviceState getStateDeviceByBalanceAccount(Long accountId){
		try{
			TbDeviceState tds = null;
			
			TbAccount ta = em.find(TbAccount.class, accountId);
			
			Long saldoActual = balanceCalulations.saldoActualCta(ta.getAccountId(), ta.getDistributionTypeId().getDistributionTypeId().intValue());
			Long umbralActual = balanceCalulations.umbralActual(accountId);
			
			if(saldoActual<=0){
				tds = em.find(TbDeviceState.class, 3L);
			}else if(saldoActual>0 && saldoActual<=umbralActual){
				tds = em.find(TbDeviceState.class, 4L);
			}else if(saldoActual >umbralActual){
				tds = em.find(TbDeviceState.class, 6L);
			}
			
			return tds;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	/** Método encargado de devolver el ultimo producto bancario asociado en proceso de disociación PSE
	 * @author psanchez
	 * @return the reaccountbankid 
	 */
	@Override
	public Long getLastBankingProductPSE(Long accountId) {
		BigDecimal idAccountBank =null;
		try{
			Query q2 = em.createNativeQuery("select ACCOUNT_BANK_ID from (Select rab.ACCOUNT_BANK_ID From Re_Account_Bank rab Where rab.account_Id=?1  and rab.STATE_ACCOUNT_BANK = 4 Order by rab.DATE_ASSOCIATION desc) where ROWNUM=1");
			q2.setParameter(1, accountId);
			idAccountBank = ((BigDecimal) q2.getSingleResult());
			System.out.println("idAccountBank.longValue(): "+idAccountBank.longValue());
			return idAccountBank.longValue();
		} catch(NoResultException e){
			System.out.println("La consulta AssociationPaymentMethodEJB.getLastBankingProductPSE "+e.getLocalizedMessage());
			return null;
		}catch(Exception e){
			System.out.println(" [ Error AssociationPaymentMethodEJB.getLastBankingProductPSE. ] ");
			e.printStackTrace();
			return null;
		}
	}
	
	/** Método encargado de devolver el ultimo producto bancario asociado en proceso de disociación
	 * @author ablasquez
	 * @return the reaccountbankid 
	 */
	@Override
	public Long getLastBankingProduct(Long accountId) {
		BigDecimal idAccountBank =null;
		try{
			Query q2 = em.createNativeQuery("select ACCOUNT_BANK_ID from (Select rab.ACCOUNT_BANK_ID From Re_Account_Bank rab Where rab.account_Id=?1  and (rab.STATE_ACCOUNT_BANK = 2 or rab.STATE_ACCOUNT_BANK = 4) Order by rab.DATE_ASSOCIATION desc) where ROWNUM=1");
			q2.setParameter(1, accountId);
			
			idAccountBank = ((BigDecimal) q2.getSingleResult());
			
			return idAccountBank.longValue();
		} catch(NoResultException e){
			System.out.println("La consulta AssociationPaymentMethodEJB.getLastBankingProduct "+e.getLocalizedMessage());
			return null;
		}catch(Exception e){
			System.out.println(" [ Error AssociationPaymentMethodEJB.getLastBankingProduct. ] ");
			e.printStackTrace();
			return null;
		}
	}
	
	/** Método encargado de devolver el idAccountbank si existe para la cuenta 
	 * @author ablasquez
	 * @return the ReAccountBank 
	 */
	@Override 
	public ReAccountBank getRelationProduct (Long accountId, Long bankAccountId){
		BigDecimal idAccountBank =null;
		try{
			System.out.println("getRelationProduct-->accountId: "+accountId);
			System.out.println("getRelationProduct-->bankAccountId: "+bankAccountId);
			Query q2 = em.createNativeQuery("Select rab.ACCOUNT_BANK_ID From Re_Account_Bank rab Where rab.account_Id=?1  and rab.BANK_ACCOUNT_ID=?2");
			q2.setParameter(1, accountId);
			q2.setParameter(2, bankAccountId);
			
			idAccountBank = ((BigDecimal) q2.getSingleResult());
		
			ReAccountBank rab = em.find(ReAccountBank.class, idAccountBank.longValue());
			return rab;
			
		}catch(NoResultException e){
			System.out.println("La consulta AssociationPaymentMethodEJB.getRelationProduct "+e.getLocalizedMessage());
			return null;
		}catch(Exception e){
			System.out.println(" [ Error AssociationPaymentMethodEJB.getRelationProduct. ] ");
			e.printStackTrace();
			return null;
		}
	}

	
	/**
	 * Metodo encargado de validar si el producto bancario a asociar se encuentra en proceso de disociación
	 * @author ablasquez
	 * @return true cuando el producto bancario esta en proceso de disociación, false cuando se puede asociar sin inconveniente
	 */
	@Override
	public boolean isProcessDisociationById(Long bankAccountId, Long accountId){
		boolean result=false;
		BigDecimal rabId = null;
		try{
			/** Se valida si el producto bancario a asociar se encuentra o no en proceso de disociación	 */
			Query q = em.createNativeQuery("Select account_bank_id from RE_ACCOUNT_BANK where bank_account_id=?1 order by date_association desc");
			q.setParameter(1, bankAccountId);
			
			List<?> lst = q.getResultList();
			
			if(lst==null || lst.size()==0){
				result = false; 
			}else{
				rabId = (BigDecimal) lst.get(0);
			}			
			
			if(rabId!=null){
				ReAccountBank rab =  em.find(ReAccountBank.class, rabId.longValue());
				
				if(rab.getState_account_bank().equals(2)){
					if(rab.getDateDissosiationBank()== null){ /* aun no se ha recibido respuesta del banco no se puede asociar*/
						if(rab.getAccountId().getAccountId().equals(accountId)){
							result = false;
						}else{
							result = true;
						}
					}else{
						if(rab.getCodeResult().equals(0L)){
							result = false;
						}else{
							if(rab.getAccountId().getAccountId().equals(accountId)){ // es la misma cuenta a la que se desea asociar el producto bancario
								result = false;
							}else{
								result = true;
							}						
						}
					}
				}
			}
			return result;
		}catch(NoResultException e){
			return result;
		}catch(Exception e){
			e.printStackTrace();
			return result;
		}
	}
	
	/**
	 * Método creado para obtener la lista de producto(s) bancario(s) disponible(s) del cliente en sesión.
	 * @return the list
	 * @author ablasquez
	 */
	@Override
	public List<TbBankAccount> getProductsByClient(Long idBank,Long idType, Long userId) {	
		List<TbBankAccount> list = new ArrayList<TbBankAccount>();
		try {
			Query q = 
				em.createNativeQuery("select distinct BANK_ACCOUNT_ID "
									+" from VW_PRODUCTS_ASSOCIATED_USER "
									+" where USRS=?2 "
									+" and ( "
									+"   case  "
									+" WHEN (STATE_ACCOUNT_BANK IS NULL OR STATE_ACCOUNT_BANK=3) THEN 'S' "
									+" WHEN (STATE_ACCOUNT_BANK=2 AND CODE_RESULT IS NOT NULL) THEN 'S' "
									+" WHEN (STATE_ACCOUNT_BANK=2 AND DATE_SEND_BANK IS NULL AND CODE_RESULT IS NULL) THEN 'S' "
									+"   end "
									+" ) = 'S' "
									+" and BANK_ID=?1 "
									+" and PRODUCT_TYPE_ID=?3 ");
			q.setParameter(1, idBank);
			q.setParameter(2, userId.toString());
			q.setParameter(3, idType);
			for (Object obj : q.getResultList()) {
				list.add(em.find(TbBankAccount.class, Long.parseLong(obj.toString())));
			}
		} catch (Exception e) {
			System.out.println(" [ Error AssociationPaymentMethodEJB.getProductsByClient. ] ");
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * Método creado para obtener la lista de cuentas facilpass disponible(s) del cliente en sesión.
	 * @return the list
	 * @author ablasquez
	 */
	@Override
	public List<TbAccount> getClientAccount(Long userId){
		List<TbAccount> list = new ArrayList<TbAccount>();
		try {
			Query q = 
				em.createNativeQuery("select ta.ACCOUNT_ID "
									+" from TB_ACCOUNT ta "
									+" left join VW_PRODUCTS_ASSOCIATED_USER vw on ta.ACCOUNT_ID=vw.ACCOUNT_ID "
									+" where (vw.STATE_ACCOUNT_BANK is null or vw.STATE_ACCOUNT_BANK <> 1) "
									+" and ta.ACCOUNT_STATE_TYPE_ID not in (3,4) "
									+" and ta.USER_ID = ?1 ");
			q.setParameter(1, userId);
			
			for (Object obj : q.getResultList()) {
				list.add(em.find(TbAccount.class, Long.parseLong(obj.toString())));
			}
		} catch (Exception e) {
			System.out.println(" [ Error AssociationPaymentMethodEJB.getClientAccount ] ");
			e.printStackTrace();
		}
		return list;
	}

	
	/**                                PSE
	 * Método creado para registra el proceso de la vinculación de la cta facilpass y pse
	 * @author psanchez
	 */
	@SuppressWarnings("unused")
	@Override
	public Long processAssociationPSE(long userId, Long accountId, Long creationUser, 
			String ip, Long idBank, Long idProduct, Long lastIdBankAccount) {
		try {
			Query query = 
				em.createNativeQuery("SELECT bank_account_id FROM tb_bank_account " +
									 "WHERE bank_account_id=?3 " +
									 "AND usrs=?1 " +
									 "AND product=?2 ");
			query.setParameter(1, userId);
			query.setParameter(2, idBank);
			query.setParameter(3, idProduct);
			
			Long bankAccountId = ((BigDecimal) query.getSingleResult()).longValue();
			TbSystemUser user = em.find(TbSystemUser.class, userId);			
			TbAccount account = em.find(TbAccount.class, accountId);
			TbBankAccount tba= em.find(TbBankAccount.class, bankAccountId);	
			TbBank tb= em.find(TbBank.class, idBank);
			
			user.setUserState(userEJB.getState(2));
			em.merge(user);
			em.flush();
			
			/** se realiza reembolso del saldo de la cuenta si este es mayor a cero
			* @author ablasquez 
			*/
			Long saldoActual = balanceCalulations.saldoActualCta(account.getAccountId(), account.getDistributionTypeId().getDistributionTypeId().intValue());
			System.out.println("account.getAccountBalance().longValue()-->*************************"+saldoActual);
	        System.out.println("lastIdBankAccount-->*****************************"+lastIdBankAccount);
				if(saldoActual>0L && lastIdBankAccount!=null){
					ReAccountBank rab = em.find(ReAccountBank.class, lastIdBankAccount);
					
					TbTransaction ttran = new TbTransaction();
					ttran.setTbAccount(account);
					ttran.setTbTransactionType(em.find(TbTransactionType.class,7L));
					ttran.setTransactionTime(new Timestamp(System.currentTimeMillis()));
					ttran.setPreviousBalance(account.getAccountBalance().longValue());
					ttran.setTransactionValue(account.getAccountBalance().longValue());
					ttran.setTransactionDescription("Reembolso por disociación de producto bancario "+rab.getBankAccountId().getBankAccountNumber());
					ttran.setNewBalance(0L);
					ttran.setCodeResult("0");
					ttran.setAccountBankId(lastIdBankAccount);
					ttran.setUserId(account.getTbSystemUser().getUserId());
					objectEM.create(ttran);	
					
					rab.setCodeMoneyBack(2);
					objectEM.update(rab);
					
					account.setAccountBalance(new BigDecimal(0));
					objectEM.update(account);
				}
				
			/** Se reactivan los dispositivos asociados a la cuenta 
			* @author ablasquez
			*/
			if(activeDeviceByDissociation(accountId)){
					outbox.insertMessageOutbox(user.getUserId(), 
					    EmailProcess.ACCOUNT_ACTIVATION_SUCCESSFUL,
					    accountId,
		                tba.getBankAccountId(),	                   
					    null, 
					    null,
					    null,
					    null,
					    tb.getBankId(),
					    null,
					    creationUser,
					    null,
					    null,
					    "Asociación",
					    true,
					    null);
					
					log.insertLog("Asociar Cuenta Cliente |La Cuenta FacilPass No." + account.getAccountId() + " " +
							  "se vinculó al producto bancario No. "+ idProduct+ " correspondiente al "
							  +tb.getBankName()+".",LogReference.ACCOUNT,LogAction.ASSOCIATION, ip, creationUser);
							        
					//proceso administrador
					TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, userId);
					Long idPTA;
					if(pt==null){
						idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  userId, ip, creationUser);
					}else{
						idPTA=pt.getProcessTrackId();
					}
					Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.CLIENT_ACCOUNT_ASSOCIATION, 
							"La Cuenta FacilPass No "+ account.getAccountId() + " se vinculó a la entidad "+
							tb.getBankName()+".", creationUser, ip, 1, "", null,null,null,null);
									
					//proceso cliente
					TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,userId);
					Long idPTC;
					if(ptc==null){
						idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, userId, ip, creationUser);
					}else{
						idPTC=ptc.getProcessTrackId();
					}
					Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.CLIENT_ACCOUNT_ASSOCIATION, 
						 "La Cuenta FacilPass No "+ account.getAccountId() + " se vinculó a la entidad "
						  +tb.getBankName()+".", creationUser, ip, 1, "", null,null,null,null);
				   return 0L;
				} else {
					outbox.insertMessageOutbox(user.getUserId(), 
						    EmailProcess.ACCOUNT_ACTIVATION_SUCCESSFUL,
						    accountId,
						    tba.getBankAccountId(),	                   
						    null, 
						    null,
						    null,
						    null,
						    tb.getBankId(),
						    null,
						    creationUser,
						    null,
						    null,
						    "Asociación",
						    true,
						    null);
						
						log.insertLog("Asociar Cuenta Cliente |La Cuenta FacilPass No." + account.getAccountId() + " " +
								  "no se vinculó al producto bancario No. "+ idProduct+ " correspondiente al "
								  +tb.getBankName()+".",LogReference.ACCOUNT,LogAction.ASSOCIATION, ip, creationUser);
								        
						//proceso administrador
						TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, userId);
						Long idPTA;
						if(pt==null){
							idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  userId, ip, creationUser);
						}else{
							idPTA=pt.getProcessTrackId();
						}
						Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.CLIENT_ACCOUNT_ASSOCIATION, 
								"La Cuenta FacilPass No "+ account.getAccountId() + " no se vinculó a la entidad "+
								 tb.getBankName()+".", creationUser, ip, 1, "", null,null,null,null);
										
						//proceso cliente
						TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,userId);
						Long idPTC;
						if(ptc==null){
							idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, userId, ip, creationUser);
						}else{
							idPTC=ptc.getProcessTrackId();
						}
						Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.CLIENT_ACCOUNT_ASSOCIATION, 
							 "La Cuenta FacilPass No "+ account.getAccountId() + " no se vinculó a la entidad "+
							 tb.getBankName()+".", creationUser, ip, 1, "", null,null,null,null);
					return null;	
				}
		}catch (NoResultException ex) {
				
			System.out.println(" [ Error AssociationPaymentMethodEJB.processAssociationPSE  NoResultException] ");
			ex.printStackTrace();
			/** Se agrega mensaje en Seguimiento de procesos y Ver procesos segun FRD064
			* @author Nramirez
			*/	
			this.createProcessAssociationPSE(userId,accountId,ip);
			
		}		
		catch (Exception e) {
			System.out.println(" [ Error AssociationPaymentMethodEJB.processAssociationPSE Exception] ");
			e.printStackTrace();	
			/** Se agrega mensaje en Seguimiento de procesos y Ver procesos segun FRD064
			* @author Nramirez
			*/	
			this.createProcessAssociationPSE(userId,accountId,ip);
		}
	  return null;
	}
	
	

	/**
	 * Método creado para obtener el consecutivo de la tabla tb_PSE.
	 * @author psanchez
	 */
	@Override
	public Long getIdPSE(Long userId, Long creationUser) {
		try{
			TbPse pse = new TbPse();
			pse.setUserId(userId);
			pse.setUserCreator(creationUser);
			pse.setDateCreator(new Timestamp(System.currentTimeMillis()));
			objectEM = new ObjectEM(em);
			if(objectEM.create(pse)){
				return pse.getPseId();
			}
		} catch(NoResultException e){
			e.printStackTrace();
			return null;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return null;
	}


	/**                                
	 * Método creado para listar los productos bancarios o registro PSE asociados del cliente en sesión
	 * @author psanchez
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<BankAccount> getAccountBank(long userId) {
		List<BankAccount> list = new ArrayList<BankAccount>();
		
		try {
			Query query= em.createNativeQuery(
						"SELECT rab.account_bank_id, rab.account_id, rab.bank_account_id, " +
						"tba.bank_account_number, tp.product_type_description, tb.bank_id, " +
						"tb.bank_name, tb.bank_aval, ta.account_state_type_id, rab.date_association " +
						"FROM tb_bank_account tba " +
						"INNER JOIN re_account_bank rab ON tba.bank_account_id=rab.bank_account_id " +
						"LEFT JOIN tb_account ta ON rab.account_id=ta.account_id " +
						"INNER JOIN tb_bank tb ON tba.product=tb.bank_id " +
						"LEFT JOIN tb_product_type tp ON tba.bank_account_type=tp.product_type_id " +
						"WHERE tba.usrs=?1 " +
						"AND rab.state_account_bank = 1 " +
						"AND ta.account_state_type_id NOT in (3,4) " +
						"ORDER BY rab.date_association DESC ");
						query.setParameter(1,userId);
                
    		List<Object> lis= (List<Object>)query.getResultList();
			for(int i=0;i<lis.size();i++){
				Object[] obj=(Object[]) lis.get(i);		                                       					
				BankAccount tba = new BankAccount ();
				if(tba!=null){	
					tba.setAccountBankIdU(Long.parseLong(obj[0].toString()));
					tba.setAccountIdU(Long.parseLong(obj[1].toString()));
					tba.setBankAccountIdU(Long.parseLong(obj[2].toString()));
					tba.setBankAccountNumberU(obj[3].toString());
					tba.setProductTypeDescriptionU(obj[4]!=null?obj[4].toString():"");
					tba.setBankIdU(Long.parseLong(obj[5].toString()));
					tba.setBankNameU(obj[6].toString());
					tba.setBankAvalU(Long.parseLong(obj[7].toString()));
					tba.setStateU(Long.parseLong(obj[8].toString()));
					tba.setDateU(obj[9].toString());
					
    			list.add(tba);
			}
		  }
		} catch (Exception e) {
			System.out.println(" [ Error en AssociationPaymentMethodEJB.getAccountBank. ] ");
			e.printStackTrace();
		}
		return list;
	}

	/**                                
	 * Método creado para asociar el producto PSE del cliente en sesión
	 * @author psanchez
	 */
	@Override
	public Long associateAccountPSE(Long userId, Long accountId,
			Long creationUser, String ip, Long idBank, Long bankAccountId, Long initValue) {
    	try {			
			    TbSystemUser systemUser = em.find(TbSystemUser.class, userId);
				TbBank bank=em.find(TbBank.class, idBank);	
			    TbAccount account = em.find(TbAccount.class, accountId);
			    TbBankAccount tba = em.find(TbBankAccount.class, bankAccountId);
			    
				objectEM = new ObjectEM(em);	
				if (systemUser != null) {   
				    /*Valida si el producto bancario asociado ya tenia una asociación previa*/
				    ReAccountBank rab = this.getRelationProduct(accountId, bankAccountId);
					/*Pendiente primera recarga cuenta FacilPass*/
					account.setAccountState(AccountStateType.ACTIVE.getId());
					em.merge(account);
			    	System.out.println("--Se valida si el producto bancario existente--");			    				    	
				    if(tba!=null){
				    	System.out.println("/*Se activa el producto bancario existente y crea asociación associateAccountPSE*/ ");			    				    	
						    if(rab!=null){
						    	/*Se activa el producto bancario existente y se crea el umbral*/
						    	rab.setDate(new Timestamp(System.currentTimeMillis()));
						    	rab.setDigits(null);
								rab.setPriority(1L);
								rab.setState_account_bank(1);
								rab.setDateSendBank(null);
								rab.setPaymentMethodId(em.find(TbPaymentMethod.class, (long) constant.PaymentMethod.PREPAID.getId()));
								em.merge(rab);
								em.flush();
								
								MinimumBalanceEJB.setCalculateMinimumBalanceAccount(accountId,creationUser,ip);
						    }else{
						    	
						    	rab= new ReAccountBank();
						    	rab.setAccountId(account);
								rab.setBankAccountId(tba);
								rab.setDate(new Timestamp(System.currentTimeMillis()));		
								rab.setDigits(null);
								rab.setPriority(1L);
								rab.setState_account_bank(1);
								rab.setDateDissociation(null);
								rab.setDateSendBank(null);
								rab.setDateDissosiationBank(null);
								rab.setCodeResult(null);
								rab.setCodeMoneyBack(null);
								rab.setPaymentMethodId(em.find(TbPaymentMethod.class, (long) constant.PaymentMethod.PREPAID.getId()));
								em.persist(rab);
								em.flush();
								
								//Se elimina ya que se agrega caculo de saldo Minimo
								//this.alarmBalance(accountId,creationUser,ip);
								MinimumBalanceEJB.setCalculateMinimumBalanceAccount(accountId,creationUser,ip);
						    }						
				    }else{
				    	System.out.println("/*Se crea producto bancario y asociación*/ associateAccountPSE");
				    	tba= new TbBankAccount();

				    	tba.setBankAccountHolder(systemUser.getUserNames());
				    	tba.setBankAccountHolderNit(systemUser.getUserCode());
				    	tba.setBankAccountNumber(bankAccountId.toString());
				    	tba.setBankAccountType(null);	
				    	tba.setDate(new Timestamp(System.currentTimeMillis()));
				    	tba.setDescription("CLIENTE");
				    	tba.setProduct(bank);
				    	tba.setUsrs(systemUser);
				    	tba.setInitValueRecharge(initValue);
						em.persist(tba);
						em.flush();
						
						rab= new ReAccountBank();
						
						rab.setAccountId(account);
						rab.setBankAccountId(tba);
						rab.setDate(new Timestamp(System.currentTimeMillis()));		
						rab.setDigits(null);
						rab.setPriority(1L);
						rab.setState_account_bank(1);
						rab.setPaymentMethodId(em.find(TbPaymentMethod.class, (long) constant.PaymentMethod.PREPAID.getId()));
						em.persist(rab);
						em.flush();
						
						//Se elimina ya que se agrega caculo de saldo Minimo
						//this.alarmBalance(accountId,creationUser,ip);
						MinimumBalanceEJB.setCalculateMinimumBalanceAccount(accountId,creationUser,ip);
				    }
					if(!role.validateUserRole(systemUser.getUserId(), (long)constant.Role.CLIENT.getId())){
						ReUserRole ur = new ReUserRole();
						ur.setTbRole(em.find(TbRole.class, (long) constant.Role.CLIENT.getId()));
						ur.setTbSystemUser(systemUser);
						em.persist(ur);
						em.flush();
					}
				    return account.getAccountId();  
				}			
		} catch (NoResultException nre) {
			System.out.println(" [ la consulta associateAccountPSE no arroja resultado ] ");
			/** Se agrega mensaje en Seguimiento de procesos y Ver procesos segun FRD064
			* @author Nramirez
			*/	
			this.createProcessAssociationPSE(userId,accountId,ip);
			 return null;
		} catch (Exception e) {
			System.out.println(" [ Error AssociationPaymentMethodEJB.associateAccountPSEt ] ");
			e.printStackTrace();
			/** Se agrega mensaje en Seguimiento de procesos y Ver procesos segun FRD064
			* @author Nramirez
			*/	
			this.createProcessAssociationPSE(userId,accountId,ip);
		}
	     return null;  
	}	
	
	/**                                
	 * Método creado para asociar el producto bancario o registro PSE del cliente en sesión
	 * @author psanchez
	 */
	@Override
	public Long associateAccountNewPSE(Long userId, Long accountId,
			Long creationUser, String ip, Long idBank, Long bankAccountId, Long initValue) {
    	try {			
			    TbSystemUser systemUser = em.find(TbSystemUser.class, userId);
				TbBank bank=em.find(TbBank.class, idBank);	
			    TbAccount account = em.find(TbAccount.class, accountId);
			    
			    account.setAccountState(AccountStateType.ACTIVE.getId());
				em.merge(account);
				
				objectEM = new ObjectEM(em);	
				if (systemUser != null) {   
				    	System.out.println("/*Se crea producto bancario y asociación*/ associateAccountNewPSE");
				    	TbBankAccount tba= new TbBankAccount();
				    	tba.setBankAccountHolder(systemUser.getUserNames());
				    	tba.setBankAccountHolderNit(systemUser.getUserCode());
				    	tba.setBankAccountNumber(bankAccountId.toString());
				    	tba.setBankAccountType(null);	
				    	tba.setDate(new Timestamp(System.currentTimeMillis()));
				    	tba.setDescription("CLIENTE");
				    	tba.setProduct(bank);
				    	tba.setUsrs(systemUser);
				    	tba.setInitValueRecharge(initValue);
						em.persist(tba);
						em.flush();
						tba.getBankAccountId();
						
						ReAccountBank rab= new ReAccountBank();
						rab.setAccountId(account);
						rab.setBankAccountId(tba);
						rab.setDate(new Timestamp(System.currentTimeMillis()));		
						rab.setDigits(null);
						rab.setPriority(1L);
						rab.setState_account_bank(1);
						rab.setPaymentMethodId(em.find(TbPaymentMethod.class, (long) constant.PaymentMethod.PREPAID.getId()));
						em.persist(rab);
						em.flush();
						
						//Se elimina ya que se agrega caculo de saldo Minimo
						//this.alarmBalance(accountId,creationUser,ip);
						MinimumBalanceEJB.setCalculateMinimumBalanceAccount(accountId,creationUser,ip);
				    }
					if(!role.validateUserRole(systemUser.getUserId(), (long)constant.Role.CLIENT.getId())){
						ReUserRole ur = new ReUserRole();
						ur.setTbRole(em.find(TbRole.class, (long) constant.Role.CLIENT.getId()));
						ur.setTbSystemUser(systemUser);
						em.persist(ur);
						em.flush();
					}
				    return account.getAccountId();  		
		} catch (NoResultException nre) {
			System.out.println(" [ la consulta associateAccountPSE no arroja resultado ] ");
			/** Se agrega mensaje en Seguimiento de procesos y Ver procesos segun FRD064
			* @author Nramirez
			*/	
			this.createProcessAssociationPSE(userId,accountId,ip);
			 return null;
		} catch (Exception e) {
			System.out.println(" [ Error AssociationPaymentMethodEJB.associateAccountPSEt ] ");		
			e.printStackTrace();
			/** Se agrega mensaje en Seguimiento de procesos y Ver procesos segun FRD064
			* @author Nramirez
			*/	
			this.createProcessAssociationPSE(userId,accountId,ip);
		}
	     return null;  
	}
	

	/**                                
	 * Método creado para asociar el producto bancario o registro PSE, Módulo Cuentas de Banco-->Crear Cuenta Bancaria -perfil admón
	 * @author psanchez
	 */
	@Override
	public Long insertBankAccountAdminPSE(Long accountId, Long bankId, Long bankAccountId, Long userId, Long creationUser, String ip) {
		try {	
			TbSystemUser systemUser = em.find(TbSystemUser.class, userId);
			TbBank bank=em.find(TbBank.class, bankId);	
		    TbAccount account = em.find(TbAccount.class, accountId);
		    TbBankAccount bankAccount = em.find(TbBankAccount.class, bankAccountId);
		    
			objectEM = new ObjectEM(em);	
			if (systemUser != null) {   
			    /*Valida si el producto bancario asociado ya tenia una asociación previa*/
			    ReAccountBank rab = this.getRelationProduct(accountId, bankAccountId);

			    account.setAccountState(AccountStateType.ACTIVE.getId());
				em.merge(account);
			    
				if(bankAccount!=null){
			    	    System.out.println("/*Se activa el producto bancario existente y crea asociación insertBankAccountAdminPSE*/");
			    				    	
					    if(rab!=null){
					    	/*Se activa el producto bancario existente y se crea el umbral*/
					    	rab.setDate(new Timestamp(System.currentTimeMillis()));
					    	rab.setDigits(null);
							rab.setPriority(1L);
							rab.setState_account_bank(1);
							rab.setDateSendBank(null);
							rab.setPaymentMethodId(em.find(TbPaymentMethod.class, (long) constant.PaymentMethod.PREPAID.getId()));
							em.merge(rab);
							em.flush();
							
							MinimumBalanceEJB.setCalculateMinimumBalanceAccount(accountId,creationUser,ip);
					    }else{    	
					    	rab= new ReAccountBank();
					    	rab.setAccountId(account);
							rab.setBankAccountId(bankAccount);
							rab.setDate(new Timestamp(System.currentTimeMillis()));		
							rab.setDigits(null);
							rab.setPriority(1L);
							rab.setState_account_bank(1);
							rab.setDateDissociation(null);
							rab.setDateSendBank(null);
							rab.setDateDissosiationBank(null);
							rab.setCodeResult(null);
							rab.setCodeMoneyBack(null);
							rab.setPaymentMethodId(em.find(TbPaymentMethod.class, (long) constant.PaymentMethod.PREPAID.getId()));
							em.persist(rab);
							em.flush();
							
							//Se elimina ya que se agrega caculo de saldo Minimo
							//this.alarmBalance(accountId,creationUser,ip);
							MinimumBalanceEJB.setCalculateMinimumBalanceAccount(accountId,creationUser,ip);
					    }					
			    }else{
			    	System.out.println("/*Se crea producto bancario y asociación*/ insertBankAccountAdminPSE");
			    	bankAccount= new TbBankAccount();

			    	bankAccount.setBankAccountHolder(systemUser.getUserNames());
			    	bankAccount.setBankAccountHolderNit(systemUser.getUserCode());
			    	bankAccount.setBankAccountNumber(bankAccountId.toString());
			    	bankAccount.setBankAccountType(null);	
			    	bankAccount.setDate(new Timestamp(System.currentTimeMillis()));
			    	bankAccount.setDescription("ADMIN FACILPASS");
					bankAccount.setProduct(bank);
					bankAccount.setUsrs(systemUser);
					bankAccount.setInitValueRecharge(null);
					em.persist(bankAccount);
					em.flush();
					
					rab= new ReAccountBank();
					
					rab.setAccountId(account);
					rab.setBankAccountId(bankAccount);
					rab.setDate(new Timestamp(System.currentTimeMillis()));		
					rab.setDigits(null);
					rab.setPriority(1L);
					rab.setState_account_bank(1);
					rab.setPaymentMethodId(em.find(TbPaymentMethod.class, (long) constant.PaymentMethod.PREPAID.getId()));
					em.persist(rab);
					em.flush();
					
					//Se elimina ya que se agrega caculo de saldo Minimo
					//this.alarmBalance(accountId,creationUser,ip);
					MinimumBalanceEJB.setCalculateMinimumBalanceAccount(accountId,creationUser,ip);
				  }
				return account.getAccountId();  
			}			
		} catch (NoResultException nre) {
			System.out.println(" [ la consulta insertBankAccountAdminPSE no arroja resultado ] ");
			 return null;
		} catch (Exception e) {
			System.out.println(" [ Error AssociationPaymentMethodEJB.insertBankAccountAdminPSE ] ");
			e.printStackTrace();
		}
	     return null;  
	}


	public Long getIdFreePSE(Long userId, Long accountId, Long idBank) {
		Long bankAccountId = null;
		try {
			Query q = em.createNativeQuery("SELECT count(*) " +
					"FROM re_account_bank rab " +
					"LEFT JOIN tb_bank_account tba ON rab.bank_account_id=tba.bank_account_id " +
					"INNER JOIN tb_bank tb ON tba.product=tb.bank_id " +
					"WHERE tb.bank_aval=0 " +
					"AND rab.state_account_bank=3 " +
					"AND tba.usrs=?1 " +
					"AND tba.product=?2 " +
					"ORDER BY tba.date_creation desc ");
			q.setParameter(1,userId);
			q.setParameter(2,idBank);
			long count = ((BigDecimal)q.getSingleResult()).longValue();
			
			if(count>0){
				Query query = em.createNativeQuery("SELECT tba.bank_account_id " +
						"FROM re_account_bank rab " +
						"LEFT JOIN tb_bank_account tba ON rab.bank_account_id=tba.bank_account_id " +
						"INNER JOIN tb_bank tb ON tba.product=tb.bank_id " +
						"WHERE tb.bank_aval=0 " +
						"AND rab.state_account_bank=3 " +
						"AND tba.usrs=?1 " +
						"AND tba.product=?2 " +
						"ORDER BY tba.date_creation ASC ");
					
					query.setParameter(1,userId);
					query.setParameter(2,idBank);
				    List<?> bankAccountt = (List<?>)query.getResultList();

				    Query query2= em.createNativeQuery("SELECT bank_account_id " +
								  "FROM re_account_bank  " +
								  "WHERE bank_account_id IN " + bankAccountt.toString().replace('[', '(').replace(']', ')') +
								  "AND state_account_bank=1 ");
				    
				    List<?> bankAccountt1 = (List<?>)query2.getResultList();
					System.out.println("bankAccountt1.size() "+bankAccountt1.size());
				    if(bankAccountt1.size()>0){
					    Query query3= em.createNativeQuery("SELECT bank_account_id " +
					    		  "FROM(SELECT rab.bank_account_id " +
					    		  "FROM re_account_bank rab " +
					    		  " LEFT JOIN tb_bank_account tba ON rab.bank_account_id=tba.bank_account_id " +
					    		  "INNER JOIN tb_bank tb ON tba.product=tb.bank_id " +
					    		  "WHERE rab.bank_account_id NOT IN " + bankAccountt1.toString().replace('[', '(').replace(']', ')') +
					    		  "AND tb.bank_aval=0 " +
					    		  "AND rab.state_account_bank=3 " +
					    		  "AND tba.usrs=?1 " +
					    		  "AND tba.product=?2 " +
								  "ORDER BY rab.bank_account_id DESC) " +
					              "WHERE ROWNUM=1 ");
			    	   
					    query3.setParameter(1,userId);
					    query3.setParameter(2,idBank);
					   bankAccountId=((BigDecimal)query3.getSingleResult()).longValue();
				    }else {
				    	 Query query3= em.createNativeQuery("SELECT bank_account_id " +
							        "FROM(SELECT tba.bank_account_id " +
									"FROM re_account_bank rab " +
									"LEFT JOIN tb_bank_account tba ON rab.bank_account_id=tba.bank_account_id " +
									"INNER JOIN tb_bank tb ON tba.product=tb.bank_id " +
									"WHERE tb.bank_aval=0 " +
									"AND rab.state_account_bank=3 " +
									"AND tba.usrs=?1 " +
									"AND tba.product=?2 " +
									"ORDER BY rab.bank_account_id DESC) " +
							        "WHERE ROWNUM=1 ");
				    	 
				    	    query3.setParameter(1,userId);
				    	    query3.setParameter(2,idBank);
						    bankAccountId=((BigDecimal)query3.getSingleResult()).longValue();
				    }
			}
		}  catch (NoResultException e) {
			System.out.println("la consulta getIdFreePSE no arroja resultado "+e.getLocalizedMessage());
			 return null;
		} 	catch (Exception ex) {
			System.out.println(" [ Error AssociationPaymentMethodEJB.getIdFreePSE ] ");
			return null;
		}
		System.out.println("getIdFreePSE "+bankAccountId);
		return bankAccountId;	
	}
	
	
	/*@SuppressWarnings("unchecked")
	public boolean alarmBalance(Long accountId, Long creationUser, String ip){
	    boolean result=false;	    	    
	    MinimumBalanceEJB.setCalculateMinimumBalanceAccount(accountId, creationUser, ip);	
	    
	    String saldoMinimoPSE = SystemParametersEJB.getParameterValueById(44L);
    	long saldoMinimoPSEL = Long.parseLong(saldoMinimoPSE);
		try{
			Query query= 
				em.createNativeQuery("select max(Val_Min_Alarm) " +
									 "from Tb_Alarm_Balance " +
									 "where account_id=?1 " +
									 "and Id_Tip_Alarm=4 ");
			query.setParameter(1, accountId);

			List<Object> list= new ArrayList<Object>();
			list=query.getResultList();
			if(list.size()>0){
				Query updUmbral = em.createNativeQuery("update tb_alarm_balance " +
                        "set val_min_alarm = ?1, " + //VALOR SALDO BAJO = PARAMETRO * 2
                        "Val_minimo = ?2 " +//PARAMETRO
                        "where account_id = ?3 " +
                        "and id_tip_alarm =4 ");
				updUmbral.setParameter(1, saldoMinimoPSEL*2);
				updUmbral.setParameter(2, saldoMinimoPSEL);
				updUmbral.setParameter(3, accountId);
				updUmbral.executeUpdate();
				result=true;
			 }else{
				//Query qryf = em.createNativeQuery("select system_parameter_value from tb_system_parameter where system_parameter_id in (6)");
				Query qryf =em.createQuery("select systemParameterValue from TbSystemParameter where systemParameterId in (6L)");
						
			    String frecuency = (String) qryf.getSingleResult();
		        //se actualiza tb_alarm_balance correspondiente a la cuenta facilpass 
	            TbAlarmBalance alarm = new TbAlarmBalance();
	    		alarm.setIdTipAlarm(4);
				alarm.setValMinAlarm(saldoMinimoPSEL);
	    		alarm.setFrequency(new Long(frecuency));
	    		alarm.setLastExecution(new Timestamp(System.currentTimeMillis()));
	    		alarm.setEstAlarm(1);
	    		alarm.setAccountId(accountId);
	    		alarm.setValminimo(saldoMinimoPSEL*2);
	    		objectEM.update(alarm);
				result=true; 
			  }
		}catch(NoResultException ex){
			System.out.println(" [ Error en AssociationPaymentMethodEJB.alarmBalance. ] ");
			result=false;
		}
		return result;
	}*/


	@Override
	public String getProductBank(Long idBankAccount) {
		String bankAccountNumber = "";
		try {
			 Query query = em.createNativeQuery("select BANK_ACCOUNT_NUMBER from TB_BANK_ACCOUNT WHERE BANK_ACCOUNT_ID=?1 ");
			
			 query.setParameter(1,idBankAccount);
		
			 Object bank = query.getSingleResult();
			 bankAccountNumber=bank.toString();
		}  catch (NoResultException nre) {
			System.out.println(" [ la consulta getProductBank no arroja resultado ] ");
			 return null;
		}	catch (Exception e) {
			System.out.println(" [ Error AssociationPaymentMethodEJB.getProductBank ] ");
			e.printStackTrace();
			return null;
		}
		return bankAccountNumber;
	}
	
	
	/**                                
	 * Método creado para obtener el producto bancario o registro PSE del cliente en sesión
	 * @author psanchez
	 */
	@Override
	public Long BankAccountNewPSE(Long userId, Long idBank, Long bankAccountNumber) {
		BigDecimal idAccountBank =null;

    	try {			
			    TbSystemUser systemUser = em.find(TbSystemUser.class, userId);
			    
			    Query query = em.createNativeQuery("select BANK_ACCOUNT_ID from TB_BANK_ACCOUNT " +
			    		                            "where BANK_ACCOUNT_NUMBER=?1 " +
			    		                            "and PRODUCT=?2 " +
			    		                            "and BANK_ACCOUNT_HOLDER_NIT=?3 ");
				query.setParameter(1,bankAccountNumber);
				query.setParameter(2,idBank);
				query.setParameter(3,systemUser.getUserCode());
				idAccountBank = ((BigDecimal) query.getSingleResult());
				return idAccountBank.longValue();
			}  catch (NoResultException nre) {
				System.out.println(" [ la consulta BankAccountNewPSE no arroja resultado ] ");
				 return null;
			}	catch (Exception e) {
				System.out.println(" [ Error BankAccountNewPSE ] ");
				e.printStackTrace();
				return null;
			}	
		}
	
	/** Se agrega mensaje en Seguimiento de procesos y Ver procesos segun FRD064
	* @author Nramirez
	*/
	@SuppressWarnings("unused")
	public void createProcessAssociationPSE(long userId, Long accountId, String ip){
		
		String RESPONCE_TYPE_COD = "3060";
		String codeError="";
		String msgProcessAdmin = "";
		String msgProcessClient = "";		
	    Long RESPONSE_ENTITY=0L;
	   
		codeError=this.process.getResponseDescByCode(Long.valueOf(RESPONCE_TYPE_COD), RESPONSE_ENTITY);
		msgProcessAdmin="La asociación de la cuenta FacilPass "+accountId+" con la entidad PSE presentó el siguiente error: "+codeError+" - "+RESPONCE_TYPE_COD;
		
		//proceso administrador
		TbProcessTrack idProcClient = this.process.searchProcess(ProcessTrackType.CLIENT, userId);
		Long newProcClient = null;
		if(idProcClient == null){
			newProcClient = this.process.createProcessTrack(ProcessTrackType.CLIENT,userId, ip,userId);
		}else{
			newProcClient = idProcClient.getProcessTrackId();
		}
		Long detailA = this.process.createProcessDetail(newProcClient, ProcessTrackDetailType.CLIENT_ACCOUNT_ASSOCIATION,
				msgProcessAdmin, userId, ip,1, "Error al Crear Proceso para Asociación PSE de la cuenta FacilPass  "+accountId,1,null,null,null,null,1L);
		
		
		msgProcessClient="La asociación de la cuenta FacilPass "+accountId+" con la entidad PSE presentó el siguiente error: "+codeError+".";

		//proceso cliente
		TbProcessTrack idProc = this.process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, userId);
		Long newProc = null;
		if(idProc == null){
			newProc = this.process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, userId, ip, userId);
		}else{
			newProc = idProc.getProcessTrackId();
		}
		Long detailC = this.process.createProcessDetail(newProc, ProcessTrackDetailType.CLIENT_ACCOUNT_ASSOCIATION,
				msgProcessClient, userId, ip, 1,"Error al Crear Proceso para Asociación PSE de la cuenta FacilPass  "+accountId,1,null,null,null,null,1L);

	}

}
