package entity;


import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import radomic.Radom;

import main.EmailTransactionMain;

import util.ConnectionHibernateUtilities;
import util.Email;

import jpa.ReAccountBank;
import jpa.ReAccountDevice;
import jpa.ReUserEmailProcess;
import jpa.ReUserOtp;
import jpa.TbAccount;
import jpa.TbEmailParameter;
import jpa.TbEmailType;
import jpa.TbOtp;
import jpa.TbOutbox;
import jpa.TbOutboxState;
import jpa.TbRechargeOnline;
import jpa.TbSystemUser;
import jpa.TbTransaction;
import jpa.TbUmbral;


import constant.DistributionType;
import constant.EmailProcess;
import crud.ObjectEM;


@Deprecated
public class EntityDaoEmailTransaction {
	
	//@PersistenceContext(unitName = "bo")
	EntityManager em= ConnectionHibernateUtilities.getEntityManager();
	
	private ObjectEM objectEM;

	private Email mail;

	private Radom rad;
	
	public EntityDaoEmailTransaction(){
		
	}

	@SuppressWarnings("unused")
	public void getTrasactions(){
		
		
		EmailTransactionMain.logs.getNotificationLog().info("getTrasactions() procesando...");
		
			List<?> listTran = null;			
			/*Se buscan los correos pendientes por enviar de acuerdo a transacciones*/
			if(em.isOpen()){
				try{  				
					try{
						em = ConnectionHibernateUtilities.getEntityManager();
						Query q = em.createNativeQuery("select outbox_transaction_id from tb_outbox_transaction " +
						"where outbox_transaction_state=0").setMaxResults(EmailTransactionMain.cantMessages);		
						listTran = (List<?>) q.getResultList();
					}catch (Exception e) {
						e.printStackTrace();
					}								
					EmailTransactionMain.logs.getNotificationLog().info("getTrasactions().Correos pendientes: " + listTran.size());
					System.out.println("listTran.size(): "+listTran.size());
					if(listTran !=null){
						if(listTran.size()>0){
							for (Object obj : listTran){
								if (obj != null && obj instanceof Object) {
									Object object = (Object) obj;
									try{
										Long tranOutId =  ((BigDecimal)object).longValue();
										EmailTransactionMain.logs.getNotificationLog().info("getTrasactions().tranOutId: "+tranOutId);
										System.out.println("getTrasaction().tranOutId: "+tranOutId);

										em.getTransaction().begin();
										Query q1=em.createNativeQuery("update tb_outbox_transaction " +
												"set outbox_transaction_state =2, transaction_process_date = SYSTIMESTAMP " +
												"where outbox_transaction_id=?1").setParameter(1,tranOutId);
										q1.executeUpdate();
										em.getTransaction().commit();
										EmailTransactionMain.logs.getNotificationLog().info("getTrasactions().cambio de estado a 2");

									}
									catch(Exception e){
										EmailTransactionMain.logs.getErrorLog().error("getTrasactions().Error en correo de transacciones");
										EmailTransactionMain.logs.getErrorLog().error("getTrasactions().Exception: ",e);
										System.out.println("error en correo de transacciones");
										e.printStackTrace();	
									}
								}        			  
							}
						}
						else{
							System.out.println("No hay correos pendientes por enviar");						
						}
					}
					else{
						System.out.println("No hay correos pendientes por enviar");
					}
				}catch(Exception e){
					EmailTransactionMain.logs.getErrorLog().error("getTrasactions().Error en el envio de correo de transacciones");
					EmailTransactionMain.logs.getErrorLog().error("getTrasactions().Exception: ",e);
					System.out.println("getTrasactions().Error en el envio de correo de transacciones");
					e.printStackTrace();
				}
			}else{
				EmailTransactionMain.logs.getErrorLog().error("getTrasactions().Error de conexion a la base de datos");
				System.out.println("getTrasactions().Error de conexion a la base de datos");				
			}
	}
	
	@SuppressWarnings("unused")
	public void sendEmailTrasaction(){
		
		EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction() procesando...");
			//System.out.println("creo el entitymanager Envio");
			/*Se buscan los correos pendientes por enviar de acuerdo a transacciones*/
			try{    			
				Query q = em.createNativeQuery("select transaction_id, email_process_id, outbox_transaction_id " +
						"from tb_outbox_transaction " +
						"where outbox_transaction_state=2");		
				List<?> listTran = (List<?>) q.getResultList();
				EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction().Correos en proceso de envío: " + listTran.size());
				if(listTran !=null){
					if(listTran.size()>0){
						for (Object obj : listTran){	
							if (obj != null && obj instanceof Object[]) {
								Object[] object = (Object[]) obj;
								try{	
									Long tranId =  ((BigDecimal)object[0]).longValue();
									Long processId = ((BigDecimal)object[1]).longValue();
									Long tranOutId = ((BigDecimal)object[2]).longValue();

									System.out.println("sendEmailTrasaction.tranId: "+tranId);
									System.out.println("sendEmailTrasaction.processId: "+processId);
									System.out.println("sendEmailTrasaction.tranOutId: "+tranOutId);
									
									EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction().tranId: " + tranId);
									EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction().processId: " + processId);
									EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction().tranOutId: " + tranOutId);
									
									
									//Creación de cliente desde carril
									if(processId == EmailProcess.PREREGISTRATION.getId()){
										EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction().PREREGISTRATION: " + EmailProcess.PREREGISTRATION.getId());
										try{																						
											TbSystemUser tsu = em.find(TbSystemUser.class, tranId);
																			
											Long otp = this.numRad(tsu.getUserId());
											System.out.println("otp: " + otp);
											EmailTransactionMain.logs.getNotificationLog().info("otp: " + otp);
											
											if(tsu != null){
												ArrayList<String> parameters=new ArrayList<String>();
												parameters.add("#OTP="+otp);
												insertMessageOutbox(tsu.getUserId(), 
										                   EmailProcess.PREREGISTRATION,
										                   null,
										                   null,
										                   null, 
										                   null,
										                   null,
										                   null,
										                   null,
										                   null,
										                   tsu.getUserId(),
											               null,
											               null,
											               null,
										                   true,
										                   parameters);
												EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction().PREREGISTRATION.insertMessageOutbox: Procesado " + tsu.getUserEmail());
											}else{
												EmailTransactionMain.logs.getErrorLog().info("sendEmailTrasaction().Error.PREREGISTRATION: No se encontró el usuario " + tsu.getUserId());
											}										
										}catch (Exception e) {
											e.printStackTrace();
										}									
										EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction().ACCOUNT_CREATION: Procesado");
									}//Creación de cuenta desde carril
									else if(processId == EmailProcess.ACCOUNT_CREATION.getId()){
										EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction().ACCOUNT_CREATION: " + EmailProcess.ACCOUNT_CREATION.getId());
										try{
											
											TbAccount ta = em.find(TbAccount.class, tranId);
											
											if(ta != null){
												insertMessageOutbox( ta.getTbSystemUser().getUserId(), 
														EmailProcess.ACCOUNT_CREATION,
														ta.getAccountId(),
														null,
														null, 
														null,
														null,
														null,
														null,
														null,
														ta.getTbSystemUser().getUserId(),
														null,
														null,
														null,
														true,
														null);
												EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction().ACCOUNT_CREATION.insertMessageOutbox: Procesado "+ ta.getTbSystemUser().getUserEmail());
											}else{
												EmailTransactionMain.logs.getErrorLog().info("sendEmailTrasaction().Error.ACCOUNT_CREATION: No se encontró la cuenta " + ta.getAccountId());
											}											
										}catch (Exception e) {
											e.printStackTrace();
										}									
										EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction().RESOURCE_ALLOCATION_SUCCESSFUL: Procesado");
									}//Asociacion de producto bancario desde banco exitosa o desde carril
									else if(processId == EmailProcess.ACCOUNT_ACTIVATION_SUCCESSFUL.getId()){
										EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction().ACCOUNT_ACTIVATION_SUCCESSFUL: " + EmailProcess.ACCOUNT_ACTIVATION_SUCCESSFUL.getId());
										try{
											ReAccountBank rab = em.find(ReAccountBank.class, tranId);										
											
											if(rab!=null){
												insertMessageOutbox(rab.getAccountId().getTbSystemUser().getUserId(), 
														EmailProcess.ACCOUNT_ACTIVATION_SUCCESSFUL,
														rab.getAccountId().getAccountId(),
														rab.getBankAccountId().getBankAccountId(),	        				                   	                   
														null, 
														null,
														null,
														null,
														rab.getBankAccountId().getProduct().getBankId(),	         				                   
														null,
														-1001L,
														null,
														null,
														"Asociación",
														true,
														null);
												EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction().ACCOUNT_ACTIVATION_SUCCESSFUL.insertMessageOutbox: Procesado "+ rab.getAccountId().getTbSystemUser().getUserEmail());
											}
											else{
												EmailTransactionMain.logs.getErrorLog().info("sendEmailTrasaction().Error.ACCOUNT_ACTIVATION_SUCCESSFUL: No se encontró la relación de la cuenta con el producto bancario");
											}
										}catch (Exception e) {
											e.printStackTrace();
										}										
										EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction().ACCOUNT_ACTIVATION_SUCCESSFUL: Procesado");
									}//Disociación de producto bancario desde banco exitosa 
									else if(processId == EmailProcess.DISSOCIATION_ACCOUNT.getId()){
										EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction().DISSOCIATION_ACCOUNT: " + EmailProcess.DISSOCIATION_ACCOUNT.getId());
										
										try{
											ReAccountBank rab = em.find(ReAccountBank.class, tranId);									
											
											if(rab!=null){
												System.out.println("rab.getDateDissociation():"+rab.getDateDissociation());
												insertMessageOutbox(rab.getAccountId().getTbSystemUser().getUserId(), 
														EmailProcess.DISSOCIATION_ACCOUNT,
														rab.getAccountId().getAccountId(),
														rab.getBankAccountId().getBankAccountId(),	        				                   	                   
														null, 
														null,
														null,
														null,
														rab.getBankAccountId().getProduct().getBankId(),	        				                   
														null,
														-1001L,
														null,
														null,
														"Disociación",
														true,null);
												EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction().DISSOCIATION_ACCOUNT.insertMessageOutbox: Procesado "+ rab.getAccountId().getTbSystemUser().getUserEmail());
											}else{
												EmailTransactionMain.logs.getErrorLog().info("sendEmailTrasaction().Error.DISSOCIATION_ACCOUNT: No se encontró la relación de la cuenta con el producto bancario para la disociación");
											}
										}catch (Exception e) {
											e.printStackTrace();
										}
										
										EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction().DISSOCIATION_ACCOUNT: Procesado");			
									}//Utilizaciones
									else if(processId == EmailProcess.UTILIZATION.getId()){
										EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction().UTILIZATION: " + EmailProcess.UTILIZATION.getId());
										TbTransaction tt = em.find(TbTransaction.class, tranId);
										ReAccountDevice rad = null;
										try{
											Query q2 = em.createQuery("Select rad from ReAccountDevice rad where rad.tbDevice.deviceId = ?1 and rad.relationState=0");
											q2.setParameter(1, tt.getTbDevice().getDeviceId());

											rad = (ReAccountDevice) q2.getSingleResult();	
										}catch (Exception e) {
											EmailTransactionMain.logs.getErrorLog().info("sendEmailTrasaction().Error: No entity found for query. ");
											e.printStackTrace();
										}									
										try{
											if(rad != null){
												
												insertMessageOutbox(rad.getTbAccount().getTbSystemUser().getUserId(), 
														EmailProcess.UTILIZATION,
														rad.getTbAccount().getAccountId(),
														null,			             
														tt.getTransactionId(),
														tt.getTbDevice().getDeviceId(),
														tt.getTbVehicle().getVehicleId(),
														null,
														null,
														null,
														null,
														null,
														null,
														null,
														true, 
														null);

												validaSaldoCta(rad.getTbAccount().getAccountId(),em);		
												EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction().UTILIZATION.insertMessageOutbox: Procesado "+ rad.getTbAccount().getTbSystemUser().getUserEmail());
											}
											else{
												EmailTransactionMain.logs.getErrorLog().info("sendEmailTrasaction().Error.UTILIZATION: No se encontró la relación del dispositivo con la cuenta " + tt.getTbSystemUser().getUserId());
											}
											
										}catch (Exception e) {
											e.printStackTrace();
										}
																			
										EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction().UTILIZATION: Procesado");
									}
									
									/*Recarga desde banco exitosa o desde carril*/
									else if(processId == EmailProcess.RESOURCE_ALLOCATION_SUCCESSFUL.getId()){
										EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction().RESOURCE_ALLOCATION_SUCCESSFUL: " + EmailProcess.RESOURCE_ALLOCATION_SUCCESSFUL.getId());
										try{
											TbTransaction tt = em.find(TbTransaction.class, tranId);
											
											if(tt!=null){
												ReAccountBank rab = null;
												Query qReAccountBank = em.createQuery("Select rab From ReAccountBank rab where rab.accountId.accountId=?1 and rab.state_account_bank = 1 ");
												qReAccountBank.setParameter(1, tt.getTbAccount().getAccountId());
												rab = (ReAccountBank) qReAccountBank.getSingleResult();
												System.out.println("rab" + rab);
												try{
													rab = (ReAccountBank) qReAccountBank.getSingleResult();
													System.out.println("rab" + rab);
												} catch(NoClassDefFoundError e){
													System.out.println("No se encontro ReAccountBank para la cuenta "+tt.getTbAccount().getAccountId());
												}
												insertMessageOutbox(tt.getTbAccount().getTbSystemUser().getUserId(), 
														EmailProcess.RESOURCE_ALLOCATION_SUCCESSFUL,
														tt.getTbAccount().getAccountId(),
														rab.getBankAccountId().getBankAccountId(),	        				                   	                   
														tranId, 
														null,
														null,
														null,
														rab.getBankAccountId().getProduct().getBankId(),	        				                   
														null,
														-1001L,
														null,
														null,
														null,
														true, 
														null);
												EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction().RESOURCE_ALLOCATION_SUCCESSFUL.insertMessageOutbox: Procesado "+ tt.getTbAccount().getTbSystemUser().getUserEmail());
											}
										}catch (Exception e) {
											e.printStackTrace();
										}
									
										EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction().RESOURCE_ALLOCATION_SUCCESSFUL: Procesado");
									}//Asociación de dispositivo desde carril
									else if(processId == EmailProcess.ASSOCIATION_DEVICE.getId()){
										EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction().ASSOCIATION_DEVICE: " + EmailProcess.ASSOCIATION_DEVICE.getId());
										try{
											
											ReAccountDevice rad = em.find(ReAccountDevice.class, tranId);
											
											if(rad != null){
												insertMessageOutbox(rad.getTbAccount().getTbSystemUser().getUserId(), 
										                   EmailProcess.ASSOCIATION_DEVICE,
										                   rad.getTbAccount().getAccountId(),
										                   null,
										                   null, 
										                   rad.getTbDevice().getDeviceId(),
										                   rad.getTbVehicle().getVehicleId(),
										                   null,
										                   null,
										                   null,
										                   rad.getTbAccount().getTbSystemUser().getUserId(),
										                   null,
										                   null,
										                   null,
										                   true,
										                   null);
												
												EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction().ASSOCIATION_DEVICE.insertMessageOutbox: Procesado " + rad.getTbAccount().getTbSystemUser().getUserEmail());
											}else{
												EmailTransactionMain.logs.getErrorLog().info("sendEmailTrasaction().Error.ASSOCIATION_DEVICE: No se encontró la relación del dispositivo con la cuenta ");
											}
										}catch (Exception e) {
											e.printStackTrace();
										}
										EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction().ASSOCIATION_DEVICE: Procesado");
									}
									//Recarga en línea exitoso
									else if(processId == EmailProcess.SUCCESSFUL_ONLINE_RECHARGE.getId()){
										EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction().SUCCESSFUL_ONLINE_RECHARGE: " + EmailProcess.SUCCESSFUL_ONLINE_RECHARGE.getId());
										List<?> list;
										Long  umbralId;
										Long rechargeOnlineId = 0L;
										try{
											
											TbTransaction tt = em.find(TbTransaction.class, tranId);
											
											try{
												Query query = em.createNativeQuery("select tu.umbral_id, tro.recharge_online_id from tb_umbral tu, tb_recharge_online tro " +
														"where tu.umbral_id = tro.umbral_id " +
														"and tu.transaction_id = ?1 ");
												query.setParameter(1, tranId);
												list = (List<?>) query.getResultList(); 
												
												for(Object ob : list){
													Object[] objec = (Object[]) ob;
													umbralId = ((BigDecimal)objec[0]).longValue();
													rechargeOnlineId = ((BigDecimal)objec[1]).longValue();													
												}
												
											}catch (Exception e) {
												System.out.println("Error en consulta tbUmbral por transactionId Recarga Exitoso");
												e.printStackTrace();
											}
											
											if(tt != null){
											
												ArrayList<String> parameters=new ArrayList<String>();
												
												parameters.add("#CHANNEL="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#NURE="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#FTRO="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#HTRO="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#RECONLVAL="+(rechargeOnlineId==null?0L:rechargeOnlineId));																								
												parameters.add("#ETRO="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#BALANCE="+(tt.getTbAccount().getAccountId()==null?0L:tt.getTbAccount().getAccountId()));
												System.out.println("PseWSEJB.parameters: "+parameters.toString());
												
												insertMessageOutbox(tt.getTbAccount().getTbSystemUser().getUserId(), 
														EmailProcess.SUCCESSFUL_ONLINE_RECHARGE,
														tt.getTbAccount().getAccountId(),
														null,	        				                   	                   
														tranId, 
														null,
														null,
														null,
														null,	        				                   
														null,
														-1001L,
														tt.getTbAccount().getTbSystemUser().getTbCodeType().getCodeTypeId(),
														null,
														null,
														true, 
														parameters);
												EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction().SUCCESSFUL_ONLINE_RECHARGE.insertMessageOutbox: Procesado "+ tt.getTbAccount().getTbSystemUser().getUserEmail());
											}else{
												EmailTransactionMain.logs.getErrorLog().info("sendEmailTrasaction().Error.SUCCESSFUL_ONLINE_RECHARGE: No se encontró la relación del dispositivo con la cuenta ");
											}
										}catch (Exception e) {
											e.printStackTrace();
										}
										EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction().SUCCESSFUL_ONLINE_RECHARGE: Procesado");
									}
									//Recarga en línea error
									else if(processId == EmailProcess.ERROR_RECHARGE_ONLINE.getId()){
										EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction().ERROR_RECHARGE_ONLINE: " + EmailProcess.ERROR_RECHARGE_ONLINE.getId());
										
										try{
											
										TbRechargeOnline to = em.find(TbRechargeOnline.class, tranId);									

											if(to != null){
											
												ArrayList<String> parameters=new ArrayList<String>();
												
												parameters.add("#ERCHANNEL="+(to.getRechargeOnlineId()==null?0L:to.getRechargeOnlineId()));
												parameters.add("#REQUESTID="+(to.getRechargeOnlineId()==null?0L:to.getRechargeOnlineId()));
												parameters.add("#NURE="+(to.getRechargeOnlineId()==null?0L:to.getRechargeOnlineId()));												
												parameters.add("#ERAGREEM="+(to.getRechargeOnlineId()==null?0L:to.getRechargeOnlineId()));
												parameters.add("#PROCERR="+(to.getRechargeOnlineId()==null?0L:to.getRechargeOnlineId()));
												parameters.add("#FTRO="+(to.getRechargeOnlineId()==null?0L:to.getRechargeOnlineId()));
												parameters.add("#HTRO="+(to.getRechargeOnlineId()==null?0L:to.getRechargeOnlineId()));
												parameters.add("#NTRO="+(to.getRechargeOnlineId()==null?0L:to.getRechargeOnlineId()));
												parameters.add("#RECONLVAL="+(to.getRechargeOnlineId()==null?0L:to.getRechargeOnlineId()));
												parameters.add("#ERAGRBANK="+(to.getRechargeOnlineId()==null?0L:to.getRechargeOnlineId()));
												parameters.add("#ESREQUEST="+(to.getRechargeOnlineId()==null?0L:to.getRechargeOnlineId()));
												parameters.add("#BANKER="+(to.getRechargeOnlineId()==null?0L:to.getRechargeOnlineId()));
												System.out.println("PseWSEJB.parameters: "+parameters.toString());
												
												insertMessageOutbox(null,
														EmailProcess.ERROR_RECHARGE_ONLINE,
														null,
														null,	        				                   	                   
														tranId, 
														null,
														null,
														null,
														null,	        				                   
														null,
														-1001L,
														null,
														null,
														null,
														true, 
														parameters);
												
												EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction().ERROR_RECHARGE_ONLINE.insertMessageOutbox: Procesado " );
											}else{
												EmailTransactionMain.logs.getErrorLog().info("sendEmailTrasaction().Error.ERROR_RECHARGE_ONLINE: No se encontró la relación del dispositivo con la cuenta ");
											}
										}catch (Exception e) {
											e.printStackTrace();
										}
										EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction().ERROR_RECHARGE_ONLINE: Procesado");
									}								
									//Recarga en línea Reverso
									else if(processId == EmailProcess.ONLINE_RECHARGE_REVERT.getId()){
										EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction().SUCCESSFUL_ONLINE_RECHARGE: " + EmailProcess.SUCCESSFUL_ONLINE_RECHARGE.getId());
										List<?> list;
										Long  umbralId;
										Long rechargeOnlineId = 0L;
										try{
											
											TbTransaction tt = em.find(TbTransaction.class, tranId);
											try{
												Query query = em.createNativeQuery("select tu.umbral_id, tro.recharge_online_id from tb_umbral tu, tb_recharge_online tro " +
														"where tu.umbral_id = tro.umbral_id " +
														"and tu.transaction_id = ?1 ");
												query.setParameter(1, tranId);
												list = (List<?>) query.getResultList();
												
												for(Object ob : list){
													Object[] objec = (Object[]) ob;
													umbralId = ((BigDecimal)objec[0]).longValue();
													rechargeOnlineId = ((BigDecimal)objec[1]).longValue();													
												}
												
											}catch (Exception e) {
												System.out.println("Error en consulta tbUmbral por transactionId Reverso Recarga en línea");
												e.printStackTrace();
											}
											
											if(tt != null){
												
												ArrayList<String> parameters=new ArrayList<String>();
												System.out
														.println("rechargeOnlineId: " + rechargeOnlineId);
												parameters.add("#FTRO="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#HTRO="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#NTRO="+(rechargeOnlineId==null?0L:rechargeOnlineId));		
												parameters.add("#ETRO="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												System.out.println("PseWSEJB.parameters: "+parameters.toString());
												
												insertMessageOutbox(tt.getTbAccount().getTbSystemUser().getUserId(), 
														EmailProcess.ONLINE_RECHARGE_REVERT,
														tt.getTbAccount().getAccountId(),
														null,	        				                   	                   
														tranId, 
														null,
														null,
														null,
														null,	        				                   
														null,
														-1001L,
														tt.getTbAccount().getTbSystemUser().getTbCodeType().getCodeTypeId(),
														null,
														null,
														true, 
														parameters);
												EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction().ONLINE_RECHARGE_REVERT.insertMessageOutbox: Procesado "+ tt.getTbAccount().getTbSystemUser().getUserEmail());
											}else{
												EmailTransactionMain.logs.getErrorLog().info("sendEmailTrasaction().Error.ONLINE_RECHARGE_REVERT: No se encontró la relación del dispositivo con la cuenta ");
											}
										}catch (Exception e) {
											e.printStackTrace();
										}
										EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction().ONLINE_RECHARGE_REVERT: Procesado");
									}
									//Error Recarga en línea Reverso
									else if(processId == EmailProcess.ERROR_ONLINE_RECHARGE_REVERT.getId()){
										EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction().ERROR_ONLINE_RECHARGE_REVERT: " + EmailProcess.ERROR_ONLINE_RECHARGE_REVERT.getId());
										List<?> list;
										Long rechargeOnlineId = 0L;
										try{
											
											TbRechargeOnline to = em.find(TbRechargeOnline.class, tranId);
											rechargeOnlineId = to.getRechargeOnlineId()==null?0L:to.getRechargeOnlineId();
											
											if(to != null){
												
												ArrayList<String> parameters=new ArrayList<String>();
												
												parameters.add("#FTRO="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#HTRO="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#NTRO="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#RECONLVAL="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#PROCERR="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#REQUESTID="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#BANKER="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#NURE="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#ERAGREEM="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#ERCHANNEL="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												System.out.println("PseWSEJB.parameters: "+parameters.toString());
												
												insertMessageOutbox(null, 
														EmailProcess.ERROR_ONLINE_RECHARGE_REVERT,
														null,
														null,	        				                   	                   
														tranId, 
														null,
														null,
														null,
														null,	        				                   
														null,
														-1001L,
														null,
														null,
														null,
														true, 
														parameters);
												EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction().ERROR_ONLINE_RECHARGE_REVERT.insertMessageOutbox: Procesado ");
											}else{
												EmailTransactionMain.logs.getErrorLog().info("sendEmailTrasaction().Error.ERROR_ONLINE_RECHARGE_REVERT: No se encontró la relación del dispositivo con la cuenta ");
											}
										}catch (Exception e) {
											e.printStackTrace();
										}
										EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction().ONLINE_RECHARGE_REVERT: Procesado");
									}
									
									else if(processId == EmailProcess.FREQUENCY_POSTPAID_SCHEDULED_TOPUP_APROVED .getId()){
										EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction().ERROR_ONLINE_RECHARGE_REVERT: " + EmailProcess.ERROR_ONLINE_RECHARGE_REVERT.getId());
										List<?> list;
										Long rechargeOnlineId = 0L;
										try{
											
											TbRechargeOnline to = em.find(TbRechargeOnline.class, tranId);
											rechargeOnlineId = to.getRechargeOnlineId()==null?0L:to.getRechargeOnlineId();
											
											if(to != null){
												
												ArrayList<String> parameters=new ArrayList<String>();
												
												parameters.add("#FTRO="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#HTRO="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#NTRO="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#RECONLVAL="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#PROCERR="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#REQUESTID="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#BANKER="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#NURE="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#ERAGREEM="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#ERCHANNEL="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												System.out.println("PseWSEJB.parameters: "+parameters.toString());
												
												insertMessageOutbox(null, 
														EmailProcess.FREQUENCY_POSTPAID_SCHEDULED_TOPUP_APROVED,
														null,
														null,	        				                   	                   
														tranId, 
														null,
														null,
														null,
														null,	        				                   
														null,
														-1001L,
														null,
														null,
														null,
														true, 
														parameters);
												EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction().ERROR_ONLINE_RECHARGE_REVERT.insertMessageOutbox: Procesado ");
											}else{
												EmailTransactionMain.logs.getErrorLog().info("sendEmailTrasaction().Error.ERROR_ONLINE_RECHARGE_REVERT: No se encontró la relación del dispositivo con la cuenta ");
											}
										}catch (Exception e) {
											e.printStackTrace();
										}
										EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction().ONLINE_RECHARGE_REVERT: Procesado");
									}
									
									else if(processId == EmailProcess.FREQUENCY_POSTPAID_SCHEDULED_TOPUP_ERROR .getId()){
										EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction().ERROR_ONLINE_RECHARGE_REVERT: " + EmailProcess.ERROR_ONLINE_RECHARGE_REVERT.getId());
										List<?> list;
										Long rechargeOnlineId = 0L;
										try{
											
											TbRechargeOnline to = em.find(TbRechargeOnline.class, tranId);
											rechargeOnlineId = to.getRechargeOnlineId()==null?0L:to.getRechargeOnlineId();
											
											if(to != null){
												
												ArrayList<String> parameters=new ArrayList<String>();
												
												parameters.add("#FTRO="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#HTRO="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#NTRO="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#RECONLVAL="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#PROCERR="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#REQUESTID="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#BANKER="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#NURE="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#ERAGREEM="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#ERCHANNEL="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												System.out.println("PseWSEJB.parameters: "+parameters.toString());
												
												insertMessageOutbox(null, 
														EmailProcess.FREQUENCY_POSTPAID_SCHEDULED_TOPUP_ERROR,
														null,
														null,	        				                   	                   
														tranId, 
														null,
														null,
														null,
														null,	        				                   
														null,
														-1001L,
														null,
														null,
														null,
														true, 
														parameters);
												EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction().ERROR_ONLINE_RECHARGE_REVERT.insertMessageOutbox: Procesado ");
											}else{
												EmailTransactionMain.logs.getErrorLog().info("sendEmailTrasaction().Error.ERROR_ONLINE_RECHARGE_REVERT: No se encontró la relación del dispositivo con la cuenta ");
											}
										}catch (Exception e) {
											e.printStackTrace();
										}
										EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction().ONLINE_RECHARGE_REVERT: Procesado");
									}
									
									
									else if(processId == EmailProcess.MBALACE_POSTPAID_SCHEDULED_TOPUP_APROVED .getId()){
										EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction().ERROR_ONLINE_RECHARGE_REVERT: " + EmailProcess.ERROR_ONLINE_RECHARGE_REVERT.getId());
										List<?> list;
										Long rechargeOnlineId = 0L;
										try{
											
											TbRechargeOnline to = em.find(TbRechargeOnline.class, tranId);
											rechargeOnlineId = to.getRechargeOnlineId()==null?0L:to.getRechargeOnlineId();
											
											if(to != null){
												
												ArrayList<String> parameters=new ArrayList<String>();
												
												parameters.add("#FTRO="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#HTRO="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#NTRO="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#RECONLVAL="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#PROCERR="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#REQUESTID="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#BANKER="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#NURE="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#ERAGREEM="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#ERCHANNEL="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												System.out.println("PseWSEJB.parameters: "+parameters.toString());
												
												insertMessageOutbox(null, 
														EmailProcess.MBALACE_POSTPAID_SCHEDULED_TOPUP_APROVED,
														null,
														null,	        				                   	                   
														tranId, 
														null,
														null,
														null,
														null,	        				                   
														null,
														-1001L,
														null,
														null,
														null,
														true, 
														parameters);
												EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction().ERROR_ONLINE_RECHARGE_REVERT.insertMessageOutbox: Procesado ");
											}else{
												EmailTransactionMain.logs.getErrorLog().info("sendEmailTrasaction().Error.ERROR_ONLINE_RECHARGE_REVERT: No se encontró la relación del dispositivo con la cuenta ");
											}
										}catch (Exception e) {
											e.printStackTrace();
										}
										EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction().ONLINE_RECHARGE_REVERT: Procesado");
									}
									
									
									else if(processId == EmailProcess.MBALACE_PREPAID_SCHEDULED_TOPUP_ERROR .getId()){
										EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction().ERROR_ONLINE_RECHARGE_REVERT: " + EmailProcess.ERROR_ONLINE_RECHARGE_REVERT.getId());
										List<?> list;
										Long rechargeOnlineId = 0L;
										try{
											
											TbRechargeOnline to = em.find(TbRechargeOnline.class, tranId);
											rechargeOnlineId = to.getRechargeOnlineId()==null?0L:to.getRechargeOnlineId();
											
											if(to != null){
												
												ArrayList<String> parameters=new ArrayList<String>();
												
												parameters.add("#FTRO="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#HTRO="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#NTRO="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#RECONLVAL="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#PROCERR="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#REQUESTID="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#BANKER="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#NURE="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#ERAGREEM="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												parameters.add("#ERCHANNEL="+(rechargeOnlineId==null?0L:rechargeOnlineId));
												System.out.println("PseWSEJB.parameters: "+parameters.toString());
												
												insertMessageOutbox(null, 
														EmailProcess.MBALACE_PREPAID_SCHEDULED_TOPUP_ERROR,
														null,
														null,	        				                   	                   
														tranId, 
														null,
														null,
														null,
														null,	        				                   
														null,
														-1001L,
														null,
														null,
														null,
														true, 
														parameters);
												EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction().ERROR_ONLINE_RECHARGE_REVERT.insertMessageOutbox: Procesado ");
											}else{
												EmailTransactionMain.logs.getErrorLog().info("sendEmailTrasaction().Error.ERROR_ONLINE_RECHARGE_REVERT: No se encontró la relación del dispositivo con la cuenta ");
											}
										}catch (Exception e) {
											e.printStackTrace();
										}
										EmailTransactionMain.logs.getNotificationLog().info("sendEmailTrasaction().ONLINE_RECHARGE_REVERT: Procesado");
									}
								
									
									
									
									
									try{
										em.getTransaction().begin();
										Query q1=em.createNativeQuery("update tb_outbox_transaction set outbox_transaction_state =1, transaction_send_date = SYSTIMESTAMP " +
																	  "where outbox_transaction_id=?1").setParameter(1,tranOutId);
										q1.executeUpdate();
										em.getTransaction().commit();
									}catch (Exception e) {
										e.printStackTrace();
									}
									
									EmailTransactionMain.logs.getNotificationLog().info("getTrasactions().cambio de estado a 1, notificación enviada");
								}catch(Exception e){
									EmailTransactionMain.logs.getErrorLog().error("Error en correo de transacciones ");
									EmailTransactionMain.logs.getErrorLog().error("sendEmailTrasaction().Exception: " + e);
								}
							}        			  
						}
					}
					else{
						EmailTransactionMain.logs.getNotificationLog().info("No hay correos pendientes por procesar");
						System.out.println("No hay correos pendientes por procesar");
					}
				}
				else{
					EmailTransactionMain.logs.getNotificationLog().info("No hay correos pendientes por procesar");
					System.out.println("No hay correos pendientes por procesar");
				}
			}catch(Exception e){
				EmailTransactionMain.logs.getErrorLog().error("Error en el envio de correo de transacciones");
				EmailTransactionMain.logs.getErrorLog().error("sendEmailTrasaction().Exception: " + e);
				e.printStackTrace();
			}
	}
	
	/** @author Nramírez 
	 * Método encargado de generar el OTP para FRD124 Autogestión de Password*/

	@SuppressWarnings("unchecked")
	public Long numRad(Long userId){
		Long newotp= 0L;
		
		try{
			rad = new Radom();
			  List<Long> lisOtp = new ArrayList<Long>();
			  Query otps = em.createNativeQuery("select totp.otp_id from tb_otp totp ");
			  lisOtp = otps.getResultList();
			  
			  if(lisOtp != null){
				  TbSystemUser tsu = em.find(TbSystemUser.class, userId);

				  Long numOtp= rad.generate(100001L, 999999999999999L, lisOtp);
				  TbOtp otp = new TbOtp();
				  otp.setOtpId(numOtp);
				  otp.setDateOtp(new Timestamp(System.currentTimeMillis()));
				  objectEM = new ObjectEM(em);					
				  objectEM.create(otp,em);
				  
				  newotp = otp.getOtpId();
				  
				  ReUserOtp ruo = new ReUserOtp();
				  ruo.setOtpId(otp);
				  ruo.setUserId(tsu);
				  ruo.setDateRelation(new Timestamp(System.currentTimeMillis()));
				  objectEM = new ObjectEM(em);					
				  objectEM.create(ruo,em); 
				  EmailTransactionMain.logs.getNotificationLog().info("numRad().Se creó el código OTP correctamente. " );
			  }
		}catch (Exception e)  {
			EmailTransactionMain.logs.getErrorLog().error("Error en la generación del código OTP: " + e);
			
			e.printStackTrace();	
			System.out.println("Error en numRad()");
		}
		return newotp;
	}
	
	/**
	 * Metodo encargado del envio de notificaciones - gestor de correos
	 * @author Nramirez
	 * @param userid	Id del usuario 
	 * @param EmailProcess	Proceso del gestor de correo al que esta asociada la notificacion
	 * @param accountId	Numero de cuenta del cliente 
	 * @param bankAccountId	Id de la relacion entre cuenta y producto bancario
	 * @param transactionId	Id de tb_transaction
	 * @param deviceId	Id del dispositivo
	 * @param vehicleId Id de la tabla ttb_vehicle
	 * @param numberVehicles	Numero de vehiculos si es requerido en la notificacion
	 * @param documentId	id de la tabla tb_document
	 * @param userCreator	id del usuario que esta realizando la operacion
	 * @param codeTypeId	id de la tabla tb_code_type
	 * @param facialId		id facila del tag anterior
	 * @param actionType	tipo de acción 
	 * @param fechaOp	tipo de acción
	 * @param sendImmediate siempre de envia true
	 */
	public boolean insertMessageOutbox(Long userid,EmailProcess processEmail, Long accountId, Long bankAccountId,Long transactionId, Long deviceId, 
									   Long vehicleId, Long numberVehicles, Long bankId, Long documentId,Long userCreator, Long codeTypeId, String facialId, 
									   String actionType,boolean sendImmediate, ArrayList<String> parameters) {
		
		EmailTransactionMain.logs.getNotificationLog().info("insertMessageOutbox() Procesando...");	
		
		try{
			boolean resultado = false;
			String message = "";
			String messageTo = ""; 
			/**
			 * Se valida si el cliente tiene la platilla habilitada para el mensaje
			 */
			Query es = em.createQuery("Select ruep From ReUserEmailProcess ruep where ruep.tbEmailProcess.emailProcessId = ?1 and ruep.tbSystemUser.userId =?2");
			es.setParameter(1, processEmail.getId());
			es.setParameter(2, userid);
			int stateNotification = 1;
			try{
				ReUserEmailProcess ruep = (ReUserEmailProcess) es.getSingleResult();				
				stateNotification = ruep.getUserEmailProcessState();
				messageTo =ruep.getUserEmailProcessTo();
				
			}catch(NoResultException n){
				stateNotification = 1;
			}


			if(stateNotification ==1){
				EmailTransactionMain.logs.getNotificationLog().info("insertMessageOutbox().stateNotification ==1");
				//sebusca la plantilla del mensaje de acuerdo al proceso
				Query q = em.createQuery("Select e from TbEmailType e where tbEmailProcess.emailProcessId = ?1 and e.emailStatus=0");
				q.setParameter(1, processEmail.getId());
			
				
				for (Object obj : q.getResultList()) {
					
					TbEmailType te = (TbEmailType) obj;
					//se crea un mensaje en la bandeja de salida TB_OUTBOX
					message = "";
					message=this.replaceParameters(te.getEmailTypeMessage(), userid, accountId, bankAccountId, transactionId, deviceId,vehicleId,
							numberVehicles,bankId,documentId, userCreator, codeTypeId, facialId, actionType, parameters);

					TbOutbox to = new TbOutbox();
					String referencia  = te.getTbReference().getOptActReferenceId();
					System.out.println("referencia: "+referencia);
					EmailTransactionMain.logs.getNotificationLog().info("insertMessageOutbox().referencia: "+ referencia);
					System.out.println("userid "+userid);
					EmailTransactionMain.logs.getNotificationLog().info("insertMessageOutbox().userid " + userid);
					if((referencia.equals("A")) || (referencia.equals("C"))){
						System.out.println("Ingreso a Referencia");
						to.setTbSystemUser(em.find(TbSystemUser.class, userid));
						if(messageTo.equals("")){
							messageTo = to.getTbSystemUser().getUserEmail();
						}	
					}else{
						to.setTbSystemUser(null);
						messageTo = te.getEmailAddressList();
					}
					to.setTbEmailType(te);
					to.setOutboxMessage(message);
					to.setTbOutboxState(em.find(TbOutboxState.class,0L));
					to.setOutboxCreate(new Timestamp(System.currentTimeMillis()));

					if(messageTo.equals("")){
						messageTo = to.getTbSystemUser().getUserEmail();
					}
					to.setOutboxSendList(messageTo);

					objectEM = new ObjectEM(em);
					if(objectEM.create(to, em)){
						if(sendImmediate){

							String[] messageToUnique = messageTo.split(";");

							for(String addressTo : messageToUnique){

								Email popEmail = new Email();
								String from = popEmail.getString("mail.smtp.user");		
								System.out.println("from: "+from);
								System.out.println("to: "+addressTo);
								resultado = (sendMailtoOutBox(
										addressTo,
										from,
										"FacilPass", 
										to.getTbEmailType().getEmailTypeSubject(),
										"1",
										to.getOutboxMessage()));
								if(resultado){
									System.out.println("TbOutBoxId: "+to.getOutboxId());
									EmailTransactionMain.logs.getNotificationLog().info("insertMessageOutbox().TbOutBoxId: "+to.getOutboxId());
									TbOutbox send = em.find(TbOutbox.class, to.getOutboxId());
									send.setOutboxSend(new Timestamp(System.currentTimeMillis()));
									send.setTbOutboxState(em.find(TbOutboxState.class,1L));
									objectEM.update(send);
								}else{
									System.out.println("TbOutBoxId: "+to.getOutboxId());
									EmailTransactionMain.logs.getNotificationLog().info("insertMessageOutbox().TbOutBoxId: "+to.getOutboxId());
									TbOutbox send = em.find(TbOutbox.class, to.getOutboxId());
									send.setOutboxSend(new Timestamp(System.currentTimeMillis()));
									send.setTbOutboxState(em.find(TbOutboxState.class,2L));
									objectEM.update(send);
								}
							}
						}else{
							TbOutbox send = em.find(TbOutbox.class, to.getOutboxId());							
							send.setTbOutboxState(em.find(TbOutboxState.class,0L));
							objectEM.update(send);
						}
					}else{
						resultado = false;
					}
				}

			}else{
				System.out.println("el usuario "+userid+" no tiene habilitada la notificación para el proceso "+processEmail.getId());
				EmailTransactionMain.logs.getNotificationLog().info("el usuario "+userid+" no tiene habilitada la notificación para el proceso "+processEmail.getId());
			}
			return resultado;
		}catch(Exception e){
			System.out.println("[ Error en insertMessageOutbox ]");
			EmailTransactionMain.logs.getErrorLog().error("insertMessageOutbox().Exception: " + e);
			e.printStackTrace();
			
			return false;
		}
		finally{
			em.clear();
		}
	}
	
	public void validaSaldoCta(Long accountId,EntityManager em){
		
		EmailTransactionMain.logs.getNotificationLog().info("validaSaldoCta() procesando...");
		try{
			//se valida cual es el tipo de distribucion de la cuenta
			TbAccount ta = em.find(TbAccount.class, accountId);
			EmailTransactionMain.logs.getNotificationLog().info("validaSaldoCta().TbAccount: " + ta.getAccountId());
			if(ta != null){
				Long saldo = this.saldoActualCta(ta.getAccountId(),ta.getDistributionTypeId().getDistributionTypeId().intValue(),em);
				Long umbral = this.umbralActual(ta.getAccountId(),em);
				
				if(saldo < umbral ){		
					
					insertMessageOutbox(ta.getTbSystemUser().getUserId(), 
				             EmailProcess.LOW_BALANCE,
				             ta.getAccountId(),
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
				             null);
					EmailTransactionMain.logs.getNotificationLog().info("Correo procesado de umbral saldo bajo para la cuenta: " + accountId + " al correo: " + ta.getTbSystemUser().getUserEmail());
				}
			}
		}catch(Exception e){
			EmailTransactionMain.logs.getErrorLog().error("validaSaldoCta().Error validación en la distribución de la cuenta ");	
			EmailTransactionMain.logs.getErrorLog().error("validaSaldoCta().Exception: " + e);			
		}
	}
	
	public Long saldoActualCta(Long accountId, int tipoCta, EntityManager em){
		EmailTransactionMain.logs.getNotificationLog().info("saldoActualCta() procesando... ");
		
		Long result =0L;
		Query q = null;
		Query qd = null;
		try{
			if(tipoCta == DistributionType.BAGMONEY.getDistributionID().intValue()){
				q = em.createNativeQuery("select Account_Balance from Tb_Account where Account_Id=?1");
				q.setParameter(1, accountId);
				
				result = ((BigDecimal) q.getSingleResult()).longValue();
				EmailTransactionMain.logs.getNotificationLog().info("saldoActualCta().result.Account_Balance: " + result);
			}else if((tipoCta == DistributionType.MANUAL.getDistributionID().intValue()) || 
					 (tipoCta == DistributionType.AUTOMATIC.getDistributionID().intValue())){
				
				q = em.createNativeQuery("select Account_Balance from Tb_Account where Account_Id=?1");
				q.setParameter(1, accountId);
				
				Long saldoCta = ((BigDecimal) q.getSingleResult()).longValue();
				EmailTransactionMain.logs.getNotificationLog().info("saldoActualCta().saldoCta: " + saldoCta);
				
				qd = em.createNativeQuery("Select sum(td.device_current_balance)"+
										 " From Tb_Device Td"+
										 " Inner Join Re_Account_Device Rad On Rad.Device_Id=Td.Device_Id"+
										 " where rad.account_id=?1");
				qd.setParameter(1, accountId);
				
				Long saldoDisp = ((BigDecimal) qd.getSingleResult()).longValue();
				EmailTransactionMain.logs.getNotificationLog().info("saldoActualCta().saldoDisp: " + saldoDisp);
				
				result = (saldoCta+saldoDisp);
				EmailTransactionMain.logs.getNotificationLog().info("saldoActualCta().result = (saldoCta+saldoDisp): " + result);
			}
		}catch(Exception e){
			EmailTransactionMain.logs.getErrorLog().error("saldoActualCta().Error al consultar el saldo de la cuenta");	
			EmailTransactionMain.logs.getErrorLog().error("validaSaldoCta().Exception: " + e);		
		}
		return result;
	}
	
	public Long umbralActual(Long accountId, EntityManager em){
		EmailTransactionMain.logs.getNotificationLog().info("umbralActual() procesando... ");
		Long maxValMinAlarm = 0L;
		try{
		 Query q = em.createNativeQuery("select max(Val_Min_Alarm) from Tb_Alarm_Balance where account_id=?1 and Id_Tip_Alarm=4");
		 q.setParameter(1, accountId);
		 maxValMinAlarm = ((BigDecimal) q.getSingleResult()).longValue();
		 EmailTransactionMain.logs.getNotificationLog().info("umbralActual().maxValMinAlarm: " + maxValMinAlarm);
		 
		}catch(NoResultException n){
			maxValMinAlarm = 0L;
		}
		return maxValMinAlarm;
	}
	
	private String replaceParameters(String templateMessage, Long userId, Long accountId, Long bankAccountId,
			Long transactionId, Long deviceId, Long vehicleId, Long numberVehicles,Long bankId, Long documentId,
			Long userCreator, Long codeTypeId, String facialId, String actionType, ArrayList<String> parameters){
		EmailTransactionMain.logs.getNotificationLog().info("replaceParameters() procesando... ");
		
		try{
			Long accountNew=null;
			Long codeTypeNew=null;
			Long accountDeviceId=null;
			Long minimunId=null;
			Long newOtp=null;
			String errorsSignatureDigital=null;
			Long categoryId= null;
			Long valMini=null;
			String obsm = null;
			String obsmE = null;			
			String urlAcc=null;
			String ipAcc=null;
			Long valRec=null;
			String messageError=null;
			Long codErr=null;
			String codBank=null;
			String nomBank=null;
			Long rechargeonlineId=null;
			Long ChanelId = null;
			if(parameters!=null){
				for(int x=0;x<parameters.size();x++){
					String[] par=parameters.get(x).split("=");
					if(par[0].equals("#NEWCFP")){
						accountNew=Long.parseLong(par[1]);
					}
					if(par[0].equals("#NEWTPCC")){
						codeTypeNew=Long.parseLong(par[1]);
					}
					if(par[0].equals("#DATEINAC")){
						accountDeviceId=Long.parseLong(par[1]);
					}					
					if(par[0].equals("#MINIID")){
						minimunId=Long.parseLong(par[1]);
					}
					if(par[0].equals("#OTP")){
						newOtp=Long.parseLong(par[1]);
					}
					if(par[0].equals("#ERR")){
						errorsSignatureDigital=par[1].toString();
					}
					if(par[0].equals("#CATM")){
						categoryId=Long.parseLong(par[1]);
					}
					if(par[0].equals("#VALM")){
						valMini=Long.parseLong(par[1]);
					}
					if(par[0].equals("#OBMIN")){
						obsm=par[1].toString();
					}
					if(par[0].equals("#OBERROR")){
						obsmE=par[1].toString();
					}
					if(par[0].equals("#ACCURL")){
						urlAcc=par[1].toString();
					}
					if(par[0].equals("#ACCIP")){
						ipAcc=par[1].toString();
					}
					if(par[0].equals("#PSEERR")){
						messageError=par[1].toString();
					}
					if(par[0].equals("#PSECODERR")){
						codErr=Long.parseLong(par[1]);
					}
					if(par[0].equals("#PSECB")){
						codBank=par[1].toString();
					}
					if(par[0].equals("#PSENB")){
						nomBank=par[1].toString();
					}
					if(par[0].equals("#VALREC")){
						valRec=Long.parseLong(par[1]);
					}
					if(par[0].equals("#FTRO")){
						rechargeonlineId=Long.parseLong(par[1]);					
					}
					if(par[0].equals("#HTRO")){
						rechargeonlineId=Long.parseLong(par[1]);
					}
					if(par[0].equals("#NTRO")){
						rechargeonlineId=Long.parseLong(par[1]);
					}
					if(par[0].equals("#CHANNEL")){
						ChanelId=Long.parseLong(par[1]);
					}
					if(par[0].equals("#PROCERR")){
						rechargeonlineId=Long.parseLong(par[1]);
					}
					if(par[0].equals("#NURE")){
						rechargeonlineId=Long.parseLong(par[1]);
					}
					if(par[0].equals("#ETRO")){
						rechargeonlineId=Long.parseLong(par[1]);
					}
					if(par[0].equals("#ERCHANNEL")){
						rechargeonlineId=Long.parseLong(par[1]);
					}
					if(par[0].equals("#ERAGREEM")){
						rechargeonlineId=Long.parseLong(par[1]);
					}					
					if(par[0].equals("#RECONLVAL")){
						rechargeonlineId=Long.parseLong(par[1]);
					}
					if(par[0].equals("#ERAGRBANK")){
						rechargeonlineId=Long.parseLong(par[1]);
					}
					if(par[0].equals("#ESREQUEST")){
						rechargeonlineId=Long.parseLong(par[1]);
					}
					if(par[0].equals("#BANKER")){
						rechargeonlineId=Long.parseLong(par[1]);
					}
					if(par[0].equals("#REQUESTID")){
						rechargeonlineId=Long.parseLong(par[1]);
					}			
					
				}
			}
			System.out.println("userId: "+userId);
			System.out.println("accountId: "+accountId);
			System.out.println("bankAccountId: "+bankAccountId);
			System.out.println("transactionId: "+transactionId);
			System.out.println("deviceId: "+deviceId);
			System.out.println("vehicleId: "+vehicleId);
			System.out.println("numberVehicles: "+numberVehicles);
			System.out.println("documentId: "+documentId);
			System.out.println("bankId: "+bankId);
			System.out.println("userCreator: "+userCreator);
			System.out.println("codeTypeId: "+codeTypeId);
			System.out.println("facialId: "+facialId);
			System.out.println("actionType: "+actionType);
						
			EmailTransactionMain.logs.getNotificationLog().info("replaceParameters().userId: " + userId);
			EmailTransactionMain.logs.getNotificationLog().info("replaceParameters().accountId: " + accountId);
			EmailTransactionMain.logs.getNotificationLog().info("replaceParameters().bankAccountId: " + bankAccountId);
			EmailTransactionMain.logs.getNotificationLog().info("replaceParameters().transactionId: " + transactionId);
			EmailTransactionMain.logs.getNotificationLog().info("replaceParameters().deviceId: " + deviceId);
			EmailTransactionMain.logs.getNotificationLog().info("replaceParameters().vehicleId: " + vehicleId);
			EmailTransactionMain.logs.getNotificationLog().info("replaceParameters().numberVehicles: " + numberVehicles);
			EmailTransactionMain.logs.getNotificationLog().info("replaceParameters().documentId: " + documentId);
			EmailTransactionMain.logs.getNotificationLog().info("replaceParameters().bankId: " + bankId);
			EmailTransactionMain.logs.getNotificationLog().info("replaceParameters().userCreator: " + userCreator);
			EmailTransactionMain.logs.getNotificationLog().info("replaceParameters().codeTypeId: " + codeTypeId);
			EmailTransactionMain.logs.getNotificationLog().info("replaceParameters().facialId: " + facialId);
			EmailTransactionMain.logs.getNotificationLog().info("replaceParameters().actionType: " + actionType);
						
			String qry = "";
			String r = null;
			Query p = null;
			String mensaje = "";
			mensaje = templateMessage;
			Query q = em.createQuery("Select p from TbEmailParameter p order by p.emailParametersId");
			for (Object obj : q.getResultList()) {
				
				TbEmailParameter tp = (TbEmailParameter) obj;
				qry = "";
				if(mensaje.contains(tp.getEmailParametersAbbreviation())){
					try{
					 
					 if(tp.getEmailParametersInput()!=null){
						 
						 /*version 15
						  * qry = "Select to_char("+tp.getEmailParametersTableRef()+"."+tp.getEmailParametersFieldRef()+
							  ") From "+tp.getEmailParametersTableRef()+
							  " "+tp.getEmailParametersWhere()+"=?1"; */
						 //versión 16
						qry = "Select to_char("+tp.getEmailParametersFieldRef()+
							  ") From "+tp.getEmailParametersTableRef()+
							  " "+tp.getEmailParametersWhere()+"=?1";
						System.out.println("qry: "+qry);//
						p = em.createNativeQuery(qry);
						if(tp.getEmailParametersInput().trim().equals("userId")){
							if(tp.getEmailParametersAbbreviation().equals("#USER")){
								if(userCreator==-1001L){ // debe ir el nombre del banco
									Query qnb = em.createQuery("Select p from TbEmailParameter p  where p.emailParametersAbbreviation='#BN' order by p.emailParametersId");
									TbEmailParameter nb = (TbEmailParameter) qnb.getSingleResult();
									if(nb!=null){
										qry = "Select to_char("+nb.getEmailParametersFieldRef()+
										  ") From "+nb.getEmailParametersTableRef()+
										  " "+nb.getEmailParametersWhere()+"=?1";
										System.out.println("segundo query: " + qry);
										p = em.createNativeQuery(qry);
										if(bankId != null){
											p.setParameter(1, bankId);
										}else{
											p.setParameter(1, -1L);
										}
									}
								}else{
									p.setParameter(1, userCreator);
								}
							}else{
								p.setParameter(1, userId);
							}							
						}else if(tp.getEmailParametersInput().trim().equals("accountId")){
							if(accountId != null){
								p.setParameter(1, accountId);
							}else{
								p.setParameter(1, -1L);
							}
						}else if(tp.getEmailParametersInput().trim().equals("bankAccountId")){
							if(bankAccountId != null){
								p.setParameter(1, bankAccountId);
							}else{
								p.setParameter(1, -1L);
							}
						}else if(tp.getEmailParametersInput().trim().equals("transactionId")){
							if(transactionId != null){
								p.setParameter(1, transactionId);
							}else{
								p.setParameter(1, -1L);
							}
						}else if(tp.getEmailParametersInput().trim().equals("deviceId")){
							if(deviceId != null){
								p.setParameter(1, deviceId);
							}else{
								p.setParameter(1, -1L);
							}
						}else if(tp.getEmailParametersInput().trim().equals("vehicleId")){
							if(vehicleId != null){
								p.setParameter(1, vehicleId);
							}else{
								p.setParameter(1, -1L);
							}
						}else if(tp.getEmailParametersInput().trim().equals("bankId")){
							if(bankId != null){
								p.setParameter(1, bankId);
							}else{
								p.setParameter(1, -1L);
							}
						}else if(tp.getEmailParametersInput().trim().equals("documentId")){
							if(documentId != null){
								p.setParameter(1, documentId);
							}else{
								p.setParameter(1, -1L);
							}
						}else if(tp.getEmailParametersInput().trim().equals("codeTypeId")){
							if(codeTypeId != null){
								p.setParameter(1, codeTypeId);
							}else{
								p.setParameter(1, -1L);
							}
						}else if(tp.getEmailParametersInput().trim().equals("newOtp")){
							if(newOtp != null){
								p.setParameter(1, newOtp);
							}else{
								p.setParameter(1, -1L);
							}

						}else if(tp.getEmailParametersInput().trim().equals("rechargeonlineId")){
							if(rechargeonlineId != null){
								p.setParameter(1, rechargeonlineId);
							}else{
								p.setParameter(1, -1L);
							}

						}else if(tp.getEmailParametersInput().trim().equals("ChanelId")){
							if(ChanelId != null){
								p.setParameter(1, ChanelId);
							}else{
								p.setParameter(1, -1L);
							}

						}
						
					    r = (String) p.getSingleResult();
					 }
						String replace=null;
					   
						if(numberVehicles!= null){
					    	if(tp.getEmailParametersAbbreviation().equals("#NV")){
					    		//mensaje = mensaje.replaceAll(tp.getEmailParametersAbbreviation(), numberVehicles.toString());
					    		mensaje=myReplaceAll(mensaje,tp.getEmailParametersAbbreviation(), numberVehicles.toString());
					    		
					    	}
					    }
						if(facialId!= null){
					    	if(tp.getEmailParametersAbbreviation().equals("#FCANT")){
					    		//mensaje = mensaje.replaceAll(tp.getEmailParametersAbbreviation(), facialId.toString());
					    		mensaje=myReplaceAll(mensaje,tp.getEmailParametersAbbreviation(), facialId.toString());
					    	}
					    }
						if(actionType!= null){
					    	if(tp.getEmailParametersAbbreviation().equals("#ACTTYPE")){
					    		//mensaje = mensaje.replaceAll(tp.getEmailParametersAbbreviation(), actionType.toString());
					    		mensaje=myReplaceAll(mensaje,tp.getEmailParametersAbbreviation(), actionType.toString());
					    	}
					    }
						
						
						
						if(tp.getEmailParametersAbbreviation().equals("#FECPROC")){
							String date = new SimpleDateFormat("dd/MM/yyyy").format(new Timestamp(System.currentTimeMillis()));
				    		//mensaje = mensaje.replaceAll(tp.getEmailParametersAbbreviation(), date);
							mensaje=myReplaceAll(mensaje,tp.getEmailParametersAbbreviation(), date);
				    	}
						if(tp.getEmailParametersAbbreviation().equals("#HORPROC")){
							String hour = new SimpleDateFormat("HH:mm:ss").format(new Timestamp(System.currentTimeMillis()));
				    		//mensaje = mensaje.replaceAll(tp.getEmailParametersAbbreviation(), hour);
							mensaje=myReplaceAll(mensaje,tp.getEmailParametersAbbreviation(), hour);
				    	}
						
						if(r!=null){
							if(tp.getEmailParametersAbbreviation().equals("#VLT") && !r.contains("xxxxx")){
								DecimalFormat formateador = new DecimalFormat("###,###.##");								
								r = "$"+formateador.format(new BigDecimal(r));
							}
							try{
								System.out.println("r: " + r);						
								replace = (tp.getEmailParametersReplace()!=null?tp.getEmailParametersReplace():"");
								System.out.println("replace: " + replace);
								System.out.println("tp.getEmailParametersAbbreviation(): " + tp.getEmailParametersAbbreviation());
								System.out.println("replace+r: " + replace+r);
								//mensaje = mensaje.replaceAll(tp.getEmailParametersAbbreviation(), replace+r);
								mensaje= myReplaceAll(mensaje,tp.getEmailParametersAbbreviation(), replace+r);
								System.out.println("mensaje: "+mensaje);
								EmailTransactionMain.logs.getNotificationLog().info("replaceParameters().mensaje: "+mensaje);
							}catch (Exception e) {
								System.out.println("error ");
								e.printStackTrace();
							}
							
						}else{
							//mensaje = mensaje.replaceAll(tp.getEmailParametersAbbreviation(), replace+tp.getEmailParametersDemo());
							mensaje= myReplaceAll(mensaje,tp.getEmailParametersAbbreviation(), replace+tp.getEmailParametersDemo());
							System.out.println("mensaje: "+mensaje);
							EmailTransactionMain.logs.getNotificationLog().info("replaceParameters().mensaje: " + mensaje);
						}
					}catch(NoResultException nr){
						System.out.println("[ Error NoResultException en replaceParameters() ]");
						EmailTransactionMain.logs.getErrorLog().error("replaceParameters().Error NoResultException en replaceParameters()");	
						EmailTransactionMain.logs.getErrorLog().error("replaceParameters().NoResultException: " + nr);
						String replace = (tp.getEmailParametersReplace()!=null?tp.getEmailParametersReplace():"");
						//mensaje = mensaje.replaceAll(tp.getEmailParametersAbbreviation(), replace+tp.getEmailParametersDemo());
						mensaje= myReplaceAll(mensaje,tp.getEmailParametersAbbreviation(), replace+tp.getEmailParametersDemo());
						
					}
				}
				r = null;
			}
			mensaje = mensaje.replaceAll("#SL#SL", "\n\r");
			System.out.println("mensaje: "+mensaje);
			EmailTransactionMain.logs.getNotificationLog().info("replaceParameters().mensaje: " + mensaje);
			return mensaje;
			
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("[ Error en replaceParameters()]");
			EmailTransactionMain.logs.getErrorLog().error("[ Error en replaceParameters()]");	
			EmailTransactionMain.logs.getErrorLog().error("replaceParameters().Exception: " + e);		
			return templateMessage;
		}
	}
	
	public String myReplaceAll(String mensaje, String inRegex, String replacement){
		String reemplazo = "";
		int tamanio = replacement.length();
		char dato;
		for (int i=0; i<tamanio; i++){
			dato = replacement.charAt(i);				
			if (((int)dato) <48){
				reemplazo=reemplazo+"\\"+dato;
			}else if ((((int)dato) >57) && (((int)dato) <65)){
				reemplazo=reemplazo+"\\"+dato;
			}else if ((((int)dato) >90) && (((int)dato) <97)){
				reemplazo=reemplazo+"\\"+dato;
			}else if (((int)dato) >122){
				reemplazo=reemplazo+"\\"+dato;
			}else{
				reemplazo=reemplazo+""+dato;
			}
		}



		String rpl = mensaje.replaceAll(inRegex, reemplazo);

		return rpl;
	}
	
	public boolean sendMailtoOutBox(String addressTo, String addressFrom, String nameFrom,
			String subject, String priority, String message) throws MessagingException {
		EmailTransactionMain.logs.getNotificationLog().info("sendMailtoOutBox() procesando... ");
		try {			
			System.out.println("addressFrom: " + addressFrom);
			EmailTransactionMain.logs.getNotificationLog().info("sendMailtoOutBox().addressFrom: " + addressFrom);
			System.out.println("nameFrom: " + nameFrom);
			EmailTransactionMain.logs.getNotificationLog().info("sendMailtoOutBox().nameFrom: " + nameFrom);
			
			mail = new Email();
			mail.setTo(addressTo);
			mail.setFrom(addressFrom, nameFrom);		
			mail.setPriority(priority);
			mail.setSubject(subject);
			mail.setBody(message);
			mail.sendMsg();			
			return true;
		} catch (UnsupportedEncodingException e) {
			EmailTransactionMain.logs.getErrorLog().error("[ Error UnsupportedEncodingException en sendMailtoOutBox()]");	
			EmailTransactionMain.logs.getErrorLog().error("sendMailtoOutBox().UnsupportedEncodingException: " + e);		
			return false;
		} catch (Exception e) {
			EmailTransactionMain.logs.getErrorLog().error("[ Error Exception en sendMailtoOutBox()]");	
			EmailTransactionMain.logs.getErrorLog().error("sendMailtoOutBox().Exception: " + e);
			return false;
		}
	}
}

