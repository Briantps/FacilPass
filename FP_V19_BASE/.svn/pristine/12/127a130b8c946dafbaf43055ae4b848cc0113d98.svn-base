package ejb;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import util.Objections;

import constant.AjustmentStateType;
import constant.EmailProcess;
import constant.EmailSubject;
import constant.EmailType;
import constant.LogAction;
import constant.LogReference;
import constant.ObjectionStateType;
import constant.ProcessTrackDetailType;
import constant.ProcessTrackType;

import crud.ObjectEM;
import ejb.email.Outbox;

import jpa.TbAccount;
import jpa.TbCharges;
import jpa.TbCompany;
import jpa.TbLane;
import jpa.TbObjection;
import jpa.TbAdjustmentObjection;
import jpa.TbProcessTrack;
import jpa.TbStationBO;
import jpa.TbSystemUser;

@Stateless(mappedName = "ejb/Objection")
public class ObjectionEJB implements Objection {
	
	@PersistenceContext(unitName = "bo") 
	EntityManager em;
	
	@EJB(mappedName="ejb/Log")
	private Log log;	
	
	@EJB(mappedName="ejb/Process")
	private Process process;
	
	@EJB(mappedName ="ejb/sendMail")
	private SendMail sendMail;
	
	@EJB(mappedName ="ejb/email/Outbox")
	private Outbox outbox;
	
	@EJB(mappedName ="ejb/User")
	private User userEjb;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TbObjection> getObjectionByState(Long state) {
	  try {	
		Query q = em.createQuery("select to from TbObjection to where to.state=?1 order by to.date");
		q.setParameter(1, state);
		
		List<TbObjection> to = new ArrayList<TbObjection>();
		to = q.getResultList();
		return to;
	  } catch(Exception e){
		  System.out.println("[ Error en ObjectionEJB.getObjectionByState ]");
		  e.printStackTrace();
	  }
	  return null;
	}


	@Override
	public TbObjection getObjectionById(Long idObjection) {
		try {
		 TbObjection obj = em.find(TbObjection.class, idObjection);
		 return obj;
		} catch (Exception e){
			System.out.println("[ Error en ObjectionEJB.getObjectionById ("+idObjection+") ]");
			  e.printStackTrace();
		}		
		return null;
	}


	@Override
	public boolean setAjustmentObjection(Long value, Long objectionId, AjustmentStateType tipo,
			String ip, Long user) {
		try{
			ObjectEM objectEM = new ObjectEM(em);
			String message = null;
			TbObjection to = em.find(TbObjection.class, objectionId);
			if(tipo == AjustmentStateType.PENDING_FOR_APPLY){
				to.setState(ObjectionStateType.AUTHORIZED.getId());				
				to.setAccountBankId(userEjb.getProductByAccount(to.getAccountId().getAccountId()));
				message="Se aplicó la objeción No.";
			} else if(tipo == AjustmentStateType.REJECTED){
				to.setState(ObjectionStateType.REJECTED.getId());
				message="Se rechazó la objeción No";
			}
			
			if(objectEM.update(to)){
				TbAccount account=to.getAccountId();
				Long userAccount = null, accountId = null;
				if(account!=null){
					userAccount=account.getTbSystemUser().getUserId();
					accountId=account.getAccountId();
				}
				TbAdjustmentObjection tao = new TbAdjustmentObjection();			
				tao.setObjectionId(to);
				tao.setAdjustmentValue(value);
				tao.setUserVerifierId(user);
				tao.setAdjustmentDate(new Timestamp(System.currentTimeMillis()));
				tao.setAdjustmentState(tipo.getId());
						
				Long idPT,c;
				if(objectEM.create(tao)){
					if(userAccount!=null){
						//proceso administrador
						TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, userAccount);
						if(pt==null){
							idPT=process.createProcessTrack(ProcessTrackType.CLIENT, userAccount, ip,user);
						}
						else{
							idPT=pt.getProcessTrackId();
						}
						Long detail=process.createProcessDetail(idPT,ProcessTrackDetailType.ADJUSTMENT_OBJECTION, message+to.getObjectionId()+" para la cuenta: "+accountId , user, ip, 0, "", null,null,null,null);
						
						//proceso cliente
						TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,userAccount);
						if(ptc==null){
							c=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS,  userAccount, ip,user);
						}
						else{
							c=ptc.getProcessTrackId();
						}
						process.createProcessDetail(c,ProcessTrackDetailType.ADJUSTMENT_OBJECTION , message+to.getObjectionId()+" para la cuenta: "+accountId,user, "ip", 0, "", null,null,null,null);
						
						
					}
					log.insertLog("Se a creado un ajuste Para la cuenta: " + tao.getObjectionId().getAccountId().getAccountId(), LogReference.ACCOUNT_TRANSACTION, LogAction.CREATE,
							ip, user);
				 return true;
				} else {
					log.insertLog("Error al crear ajuste Para la cuenta: " + tao.getObjectionId().getAccountId().getAccountId(), LogReference.ACCOUNT_TRANSACTION, LogAction.CREATEFAIL,
							ip, user);
				  return false;
				}
			}			
			
		} catch (Exception e){
			System.out.println("[ Error en ObjectionEJB.setAjustmentObjection ]");
			  e.printStackTrace();
		}
		return false;
	}


	@Override
	public boolean createObjection(Date date, Date dateTransaction, String objection, TbSystemUser usrs, Long accountId, String ip, Long chargeId, Long companyId, Long stationId, Long laneId, boolean res1){
		boolean res=false;
		try{
			TbCharges tc= em.find(TbCharges.class, chargeId);
			TbAccount ta=em.find(TbAccount.class, accountId);
			Long userAccount =ta.getTbSystemUser().getUserId();
			Long userCreator= usrs.getUserId();
			String emailClient = null;
			System.out.println("userAccount " + userAccount);
			
			if(res1){
				System.out.println("Creada por cliente");
				emailClient=ta.getTbSystemUser().getUserEmail();
			}
			else{
				System.out.println("Creada por administrador");
				emailClient=ta.getTbSystemUser().getUserEmail();
			}
			
			TbObjection to= new TbObjection();
			to.setDate(new Timestamp(date.getTime()));
			to.setDateTransaction(new Timestamp(dateTransaction.getTime()));
			to.setObjection(objection);
			if(usrs!=null){
				to.setUsrs(usrs.getUserId().toString());
			}
			
			to.setAccountId(ta);
			to.setCharge(tc);
			to.setCompanyId(companyId);
			to.setStationId(stationId);
			to.setLaneId(laneId);
			to.setState(ObjectionStateType.PENDING_AUTHORIZATION.getId());
			
			em.persist(to);
			Long idPT, c;
			
			//proceso administrador
			TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, userAccount);
			if(pt==null){
				idPT=process.createProcessTrack(ProcessTrackType.CLIENT, userAccount, ip,userCreator);
			}
			else{
				idPT=pt.getProcessTrackId();
			}
			Long detail=process.createProcessDetail(idPT,ProcessTrackDetailType.OBJECTION_ACCOUNT, "Se crea la reclamación a la Cuenta FacilPass No "+accountId, userCreator, ip, 0, "", null,null,null,null);
			
			//proceso cliente
			TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,userAccount);
			if(ptc==null){
				c=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS,  userAccount, ip,userCreator);
			}
			else{
				c=ptc.getProcessTrackId();
			}
			process.createProcessDetail(c,ProcessTrackDetailType.OBJECTION_ACCOUNT , "Se crea la reclamación a su Cuenta FacilPass No "+accountId,userCreator, "ip", 0, "", null,null,null,null);
			
			try{
				
					System.out.println("Enviando correo al cliente " + emailClient);
					/*sendMail.sendMail(EmailType.CLIENT, emailClient, EmailSubject.ADMIN_CREATE_OBJECTION,
							"Buen Dia, \r\n El Usuario " + usrsCreador.getUserCode() + " - "+ usrsCreador.getUserNames()+ " " + usrsCreador.getUserSecondNames() +" creó la objeción asociada a su cuenta " + accountId);*/
					
					outbox.insertMessageOutbox(ta.getTbSystemUser().getUserId(), 
			                   EmailProcess.CREATE_OBJECTION,
			                   accountId,
			                   null,
			                   null, 
			                   null,
			                   null,
			                   null,
			                   null,
			                   null,
			                   userCreator,
			                   null,
			                   null,
			                   null,
			                   true,
						       null);
					
				
			    if(emailClient!=null){
				    //sendMail(usrs, accountId,detail,ip, emailClient, " Creó objeción asociada a la cuenta: ");
			    }
			    
			}catch(Exception ex){
				System.out.println("Fallo al enviar correo");
				ex.printStackTrace();
				res=true;
			}
		
			
			res=true;
		}catch(Exception ex){
			res=false;
			ex.printStackTrace();
		}
		
		return res;
	}
	
	public void sendMail(TbSystemUser usrsCreador, Long accountId, Long detailId, String ip, String emailClient, String message){
		Query q= em.createNativeQuery("select ts.user_email from tb_task_type tt "
                                       + " inner join re_task_type_user tu on tt.task_type_id= tu.task_type_id "
                                       + " inner join tb_system_user ts on tu.user_id= ts.user_id "
                                       + " where tt.task_type_id=12");
		
		List<?> lis=q.getResultList();
		
		if(emailClient!=null){
			System.out.println("Enviando correo al cliente " + emailClient);
			/*sendMail.sendMail(EmailType.CLIENT, emailClient, EmailSubject.ADMIN_CREATE_OBJECTION,
					"Buen Dia, \r\n El Usuario " + usrsCreador.getUserCode() + " - "+ usrsCreador.getUserNames()+ " " + usrsCreador.getUserSecondNames() +" creó la objeción asociada a su cuenta " + accountId);*/
		}
	
		for(Object o: lis){
			String email=(String) o;
			System.out.println("Enviando correo al administrador encargado " + email);
			
			sendMail.sendMail(EmailType.ADMIN, email, EmailSubject.CLIENT_CREATE_OBJECTION,
					"Buen Dia, \r\n El Usuario " + usrsCreador.getUserCode() + " - "+ usrsCreador.getUserNames()+ " " + usrsCreador.getUserSecondNames() + message + accountId);	
			
		}
	}


	@Override
	public TbLane getCarrilById(Long idCarril) {
		TbLane tl = em.find(TbLane.class, idCarril);
		return tl;
	}


	@Override
	public TbCompany getConcesionById(Long idConcesion) {
		TbCompany tc = em.find(TbCompany.class, idConcesion);
		return tc;
	}


	@Override
	public TbStationBO getStationById(Long idStation) {
		TbStationBO tsb = em.find(TbStationBO.class, idStation);
		return tsb;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Objections> getObjectionByClient(Long userId) {
		List<Objections> lista= new ArrayList<Objections>();
		Query q= em.createNativeQuery("select tob.objection_id, tob.transaction_date, tob.account_id, tob.objection_state from tb_objection tob "+
										" inner join tb_account ta on tob.account_id=ta.account_id "+
										" where ta.user_id=?1 order by transaction_date");
		q.setParameter(1,userId);
		
		List<Object> lis= q.getResultList();
		for(int i=0;i<=lis.size()-1;i++){
			Object[] o= (Object[]) lis.get(i);
			
			Objections ob= new Objections();
			ob.setObjectionId(Long.parseLong(o[0].toString()));
			String f=o[1]!=null?o[1].toString():null;
			Timestamp fec = null;
			if(f!=null){
				System.out.println("fecha no es null");
				fec= Timestamp.valueOf(f);
			}
			ob.setDate(fec);
			ob.setAccountId(o[2]!=null?Long.parseLong(o[2].toString()):null);
			ob.setState(o[3]!=null?o[3].toString():"-");
			
			lista.add(ob);
		}
		
		return lista;
	}


	@Override
	public boolean updateObjection(Long objectionId, Date newDate,
			String newObjection, TbSystemUser tt, Long newAccountId, String ip,
			Long newChargeId, Long newCompanyId, Long newStationId,
			Long newLaneId, boolean res1) {
		
		boolean res=false;
		try{
			TbObjection to= em.find(TbObjection.class, objectionId);
			TbCharges tc= em.find(TbCharges.class, newChargeId);
			TbAccount ta=em.find(TbAccount.class, newAccountId);
			Long userAccount =ta.getTbSystemUser().getUserId();
			Long userCreator= tt.getUserId();
			String emailClient = null;
			System.out.println("userAccount " + userAccount);
			
			if(res1){
				System.out.println("Modificada por cliente");
				emailClient=ta.getTbSystemUser().getUserEmail();
			}
			else{
				System.out.println("Modificada por administrador");
				emailClient=ta.getTbSystemUser().getUserEmail();
			}
			
			if(to!=null){
				to.setDateTransaction(new Timestamp(newDate.getTime()));
				to.setObjection(newObjection);
				if(userCreator!=null){
					to.setUsrs(userCreator.toString());
				}
				
				to.setAccountId(ta);
				to.setCharge(tc);
				
				if(tc.getChargeId()==3){
					to.setCompanyId(newCompanyId);
					to.setStationId(newStationId);
					to.setLaneId(newLaneId);
				}
				else{
					to.setCompanyId(0L);
					to.setStationId(0L);
					to.setLaneId(0L);
				}
				
				to.setState(ObjectionStateType.PENDING_AUTHORIZATION.getId());
				
				em.merge(to);
				Long idPT, c;
				
				//proceso administrador
				TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, userAccount);
				if(pt==null){
					idPT=process.createProcessTrack(ProcessTrackType.CLIENT, userAccount, ip,userCreator);
				}
				else{
					idPT=pt.getProcessTrackId();
				}
				Long detail=process.createProcessDetail(idPT,ProcessTrackDetailType.OBJECTION_ACCOUNT, "El cliente con Nro. de identificación "+tt.getUserCode()+" modificó la objeción asociada a la cuenta: "+newAccountId +" - "+newObjection, userCreator, ip, 0, "", null,null,null,null);
				
				//proceso cliente
				TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,userAccount);
				if(ptc==null){
					c=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS,  userAccount, ip,userCreator);
				}
				else{
					c=ptc.getProcessTrackId();
				}
				process.createProcessDetail(c,ProcessTrackDetailType.OBJECTION_ACCOUNT , "Ha modificado la objeción asociada a la cuenta: "+newAccountId +" - "+newObjection,userCreator, "ip", 0, "", null,null,null,null);
				
				try{
					
						System.out.println("Enviando correo al cliente " + emailClient);

						outbox.insertMessageOutbox(ta.getTbSystemUser().getUserId(), 
				                   EmailProcess.UPDATE_OBJECTION,
				                   newAccountId,
				                   null,
				                   null, 
				                   null,
				                   null,
				                   null,
				                   null,
				                   null,
				                   userCreator,
				                   null,
				                   null,
				                   null,
				                   true,
							       null);
						
					
				if(emailClient!=null){
					//sendMail(tt, newAccountId,detail,ip, emailClient, " Modificó la objeción asociada  a la cuenta: ");
				}
				
				}catch(Exception ex){
					System.out.println("Fallo al enviar correo");
					ex.printStackTrace();
					res=true;
				}
				
				res=true;
			}
			
		}catch(Exception ex){
			res=false;
			ex.printStackTrace();
		}
		
		return res;

	}


	@Override
	public boolean deleteObjection(Long objectionId) {
		boolean res=false;
		TbObjection to= em.find(TbObjection.class, objectionId);
		try{
			if(to!=null){
				to.setState(3L);
				em.merge(to);
				Long idPT, c;
				TbAccount ta=to.getAccountId();
				if(ta!=null){
					TbSystemUser user=ta.getTbSystemUser();
					if(user!=null){
						//proceso administrador
						TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, user.getUserId());
						if(pt==null){
							idPT=process.createProcessTrack(ProcessTrackType.CLIENT, ta.getAccountId(), null,user.getUserId());
						}
						else{
							idPT=pt.getProcessTrackId();
						}
						Long detail=process.createProcessDetail(idPT,ProcessTrackDetailType.OBJECTION_ACCOUNT, "El cliente con Nro. de identificación " + user.getUserCode() +" eliminó la objeción con id:" + objectionId + " asociada a la cuenta " +ta.getAccountId(), user.getUserId(), null, 1, "", null,null,null,null);
						
						//proceso cliente
						TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,user.getUserId());
						if(ptc==null){
							c=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS,  user.getUserId(), null,user.getUserId());
						}
						else{
							c=ptc.getProcessTrackId();
						}
						process.createProcessDetail(c,ProcessTrackDetailType.OBJECTION_ACCOUNT , "Ha eliminado la objeción con id:" + objectionId + " asociada a la cuenta "+ta.getAccountId(), user.getUserId(), null, 1, "", null,null,null,null);
						
						sendMail(user,ta.getAccountId(),detail,null, user.getUserEmail(), " Eliminó la objeción asociada  a la cuenta: ");
						
						System.out.println("Enviando correo al cliente " + user.getUserEmail());
							
						outbox.insertMessageOutbox(user.getUserId(), 
					                   EmailProcess.DELETE_OBJECTION,
					                   to.getAccountId().getAccountId(),
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
								       null);
						
						res=true;
				
					}
				}
				
						
			}
		}catch(Exception ex){
			res= false;
		}
		
		return res;
	}

	@Override
    public boolean validateDateReclamation(Long accountId, Date dateTransaction){
    	boolean res = false;
    	
    	Query q= em.createNativeQuery("select account_opening_date from tb_account where account_id=?1");
    	q.setParameter(1, accountId);
    	
    	Timestamp fechaCreacion= (Timestamp) q.getSingleResult();

        if(dateTransaction.getTime() >= fechaCreacion.getTime()){
        	res=true;
        }
        else{
        	res=false;
        }
        System.out.println("fecha creacion cuenta: " + fechaCreacion);
        System.out.println("fecha Transaccion: " + dateTransaction);
       
    	return res;
    }
	
}
