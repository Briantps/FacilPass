package ejb.email;

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

import jpa.ReUserEmailProcess;
import jpa.TbAccount;
import jpa.TbEmailProcess;
import jpa.TbProcessTrack;
import jpa.TbSystemUser;
import constant.LogAction;
import constant.LogReference;
import constant.ProcessTrackDetailType;
import constant.ProcessTrackType;
import crud.ObjectEM;
import ejb.Log;
import ejb.Process;

@Stateless(mappedName = "ejb/email/ManagementNotifications")
public class ManagementNotificationsEJB implements ManagementNotifications{
	
	@PersistenceContext(unitName = "bo")
	EntityManager em;
	
	@SuppressWarnings("unused")
	private ObjectEM objectEM;
	
	@EJB(mappedName="ejb/Log")
	private Log log;
	
	@EJB(mappedName="ejb/Process")
	private Process process;
	
	private List<util.Notifications> notifications;
	
	@Override
	public long validateFilters(String[] filters) {
		try{
			/**
			 * Se obtiene los diferentes valores ingresados para los filtros
			 */
			Long codeType = Long.parseLong(filters[0]);
			String documetNumber = filters[1];
			String name = filters[2];
			String lastName = filters[3];
			String mail = filters[4];
			
			Long accountNumber = null;
			if(filters[5]!=null && !filters[5].trim().equals("")){
				accountNumber = Long.parseLong(filters[5]);
			}
			 
			
			String queryCount = "select count(*) "+
								"from ( "+
								"select distinct to_char(tu.user_id),tu.user_names from tb_system_user tu "+ 
								"inner join re_user_role rer on tu.user_id=rer.user_id  "+
								"inner join tb_role r on rer.role_id=r.role_id  "+
								"left join tb_account ta on ta.user_id=tu.user_id "+
								"where r.role_id in (2,3) ";
						
			if(codeType != -1L){
				queryCount = queryCount+" and tu.code_type_id="+codeType+" ";
			}
			
			if((documetNumber!=null) && (!documetNumber.equals(""))){
				queryCount = queryCount+" and tu.user_code='"+documetNumber+"' ";
			}
			
			if((name!=null) && (!name.equals(""))){
				queryCount = queryCount+" and tu.user_names like '%"+name.toUpperCase()+"%' ";
			}
			
			if((lastName!=null) && (!lastName.equals(""))){
				queryCount = queryCount+" and tu.user_second_names like '%"+lastName.toUpperCase()+"%' ";
			}
			
			if((mail!=null) && (!mail.equals(""))){
				queryCount = queryCount+" and tu.user_email ='"+mail.toLowerCase()+"' ";
			}
			
			if(accountNumber!=null){
				queryCount = queryCount+" and ta.account_id ="+accountNumber+" ";
			}
			
			queryCount = queryCount+"order by tu.user_names)";
			
			System.out.println("queryCount: "+queryCount);
			
			Query q = em.createNativeQuery(queryCount);
			
			long result = ((BigDecimal) q.getSingleResult()).longValue();
			
			
			return result;
			
		}catch(NoResultException e){
			e.printStackTrace();
			return 0;
		}catch(Exception e){
			e.printStackTrace();
			return -1;
		} 
	}

	@Override
	public TbSystemUser getClientByFilters(String[] filters) {
		try{
			/**
			 * Se obtiene los diferentes valores ingresados para los filtros
			 */
			Long codeType = Long.parseLong(filters[0]);
			String documetNumber = filters[1];
			String name = filters[2];
			String lastName = filters[3];
			String mail = filters[4];
			Long accountNumber = null;
			if(filters[5]!=null && !filters[5].trim().equals("")){
				accountNumber = Long.parseLong(filters[5]);
			}
			
			String queryCount = "select userId "+
								"from ( "+
								"select distinct tu.user_id as userId,tu.user_names from tb_system_user tu "+ 
								"inner join re_user_role rer on tu.user_id=rer.user_id  "+
								"inner join tb_role r on rer.role_id=r.role_id  "+
								"left join tb_account ta on ta.user_id=tu.user_id "+
								"where r.role_id in (2,3) ";
						
			if(codeType != -1L){
				queryCount = queryCount+" and tu.code_type_id="+codeType+" ";
			}
			
			if((documetNumber!=null) && (!documetNumber.equals(""))){
				queryCount = queryCount+" and tu.user_code='"+documetNumber+"' ";
			}
			
			if((name!=null) && (!name.equals(""))){
				queryCount = queryCount+" and tu.user_names like '%"+name.toUpperCase()+"%' ";
			}
			
			if((lastName!=null) && (!lastName.equals(""))){
				queryCount = queryCount+" and tu.user_second_names like '%"+lastName.toUpperCase()+"%' ";
			}
			
			if((mail!=null) && (!mail.equals(""))){
				queryCount = queryCount+" and tu.user_email ='"+mail.toLowerCase()+"' ";
			}
			
			if(accountNumber!=null){
				queryCount = queryCount+" and ta.account_id ="+accountNumber+" ";
			}
			
			queryCount = queryCount+"order by tu.user_names)";
			
			System.out.println("queryUser: "+queryCount);
			
			Query q = em.createNativeQuery(queryCount);
			
			BigDecimal idUser = (BigDecimal) q.getResultList().get(0);
			
			TbSystemUser result = em.find(TbSystemUser.class, idUser.longValue());
			
			return result;
			
		}catch(NoResultException e){
			e.printStackTrace();
			return null;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		} 
	}

	@Override
	public List<util.Notifications> getListNotificationsByClient(Long clientId) {
		try{
			notifications = new ArrayList<util.Notifications>();
			boolean active = false;
			util.Notifications notification = null;			
			
			this.loadUserEmailProcess(clientId);
			Query q = em.createQuery("Select ruep From ReUserEmailProcess ruep where ruep.tbSystemUser.userId=?1 Order by ruep.tbEmailProcess.emailProcessName");
			q.setParameter(1, clientId);
			if(q.getResultList()!=null){				
				for(Object o: q.getResultList()){
					ReUserEmailProcess ruep = (ReUserEmailProcess) o;
					if (ruep.getUserEmailProcessState()==1) {
						active = true;
					} else {
						active = false;
					}
					notification = new util.Notifications(ruep, active);
					notifications.add(notification);
				}
				return notifications;				
			}else{
				return null;
			}
		}catch(NullPointerException e){
			e.printStackTrace();
			return null;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void loadUserEmailProcess(Long clientId){
		try{
			Query emailProcess =em.createNativeQuery("select distinct ep.email_process_id "+
													 "from tb_email_process ep "+
													 "where ep.email_process_id not in (select email_process_id from re_user_email_process where user_id=?1) "+
													 "order by ep.EMAIL_PROCESS_ID");
			emailProcess.setParameter(1, clientId);
			
			if(emailProcess.getResultList()!=null){
				for(Object o: emailProcess.getResultList()){
					Long idEmailProcess = ((BigDecimal) o).longValue();
					
					TbEmailProcess ep = em.find(TbEmailProcess.class, idEmailProcess);
					TbSystemUser us = em.find(TbSystemUser.class, clientId);
					if(ep!=null && us!=null){
						ReUserEmailProcess ruep = new ReUserEmailProcess();
						ruep.setTbEmailProcess(ep);
						ruep.setTbSystemUser(us);
						ruep.setUserEmailProcessState(1);
						ruep.setUserEmailProcessTo(us.getUserEmail());
						
						em.persist(ruep);
						em.flush();
					}
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public String getAccountsByClient(Long clientId) {
		try{
			Query q = em.createQuery("Select ta From TbAccount ta Where ta.tbSystemUser.userId=?1");
			q.setParameter(1, clientId);
			int i=1;
			if(q.getResultList()!=null){
				String result="";
				for(Object o: q.getResultList()){
					TbAccount ta = (TbAccount) o;
					result=result+ta.getAccountId();
					if(i<q.getResultList().size()){
						result=result+"-";
					}
					i=i+1;					
				}
				return result;
			}else{
				return "";
			}			
		}catch(NoResultException e){
			e.printStackTrace();
			return "";
		}catch(NullPointerException e){
			e.printStackTrace();
			return "";
		}
	}

	@Override
	public Long saveNotifications(List<util.Notifications> newList, Long userLogg, String ipLogg) {
		try{
			Long result = 0L;						
			String mensaje = "";
			String mensajeProceso = "";
			Timestamp dateModif = null;
			Query q = em.createNativeQuery("Update Re_User_Email_Process set user_email_process_state=?1, modification_date=?3 where user_email_process_id=?2");
			
			for (util.Notifications notificacion : newList) {
				q.setParameter(2, notificacion.getUserEmail().getUserEmailProcessId());
				if(notificacion.isActive()){
					q.setParameter(1, 1);
					dateModif = new Timestamp(System.currentTimeMillis());									
					mensaje = "Se ha activado la notificación "+notificacion.getUserEmail().getTbEmailProcess().getEmailProcessName()+" Para el Cliente "+notificacion.getUserEmail().getTbSystemUser().getTbCodeType().getCodeTypeAbbreviation()+" - "+notificacion.getUserEmail().getTbSystemUser().getUserCode();
					mensajeProceso = "Se activó la notificación "+notificacion.getUserEmail().getTbEmailProcess().getEmailProcessName().toUpperCase()+" para su perfil";
				}else{
					q.setParameter(1, 0);
					dateModif = new Timestamp(System.currentTimeMillis());					
					mensaje = "Se ha desactivado la notificación "+notificacion.getUserEmail().getTbEmailProcess().getEmailProcessName()+" Para el Cliente "+notificacion.getUserEmail().getTbSystemUser().getTbCodeType().getCodeTypeAbbreviation()+" - "+notificacion.getUserEmail().getTbSystemUser().getUserCode();
					mensajeProceso = "Se desactivó la notificación "+notificacion.getUserEmail().getTbEmailProcess().getEmailProcessName().toUpperCase()+" para su perfil";
				}
				q.setParameter(3,dateModif);
				
				if(q.executeUpdate()>0){
					System.out.println(mensaje);
					log.insertLog(mensaje,LogReference.EMAIL ,LogAction.UPDATE , ipLogg, userLogg);
					insertViewProcess(mensajeProceso,dateModif,userLogg,notificacion.getUserEmail().getTbSystemUser().getUserId(),ipLogg);
					result=result+1;
				}else{
					System.out.println(mensaje);
					log.insertLog(mensaje,LogReference.EMAIL ,LogAction.UPDATEFAIL , ipLogg, userLogg);
				}
			}
			return result;
			
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public void insertViewProcess(String message, Timestamp dateProcess, Long userId, Long clientId, String ipLogg){
		try{
			TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, clientId);
			Long idPTA;
			if(pt==null){
				idPTA=process.createProcessTrack(ProcessTrackType.CLIENT, clientId, ipLogg, userId);
			}
			else{
				idPTA=pt.getProcessTrackId();
			}
			Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.MANAGEMENT_NOTIFICATION, 
					message,
					userId, ipLogg,1,"Error Administración de Notificaciones",null,null,null,null);
			System.out.println("id de procesos para lado amin: "+detailA);
			//proceso cliente
			TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,clientId);
			Long idPTC;
			if(ptc==null){
				idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, clientId, ipLogg, userId);
			}
			else{
				idPTC=ptc.getProcessTrackId();
			}
			Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.MANAGEMENT_NOTIFICATION, 
					message, userId, ipLogg, 1, "Error Administración de Notificaciones",null,null,null,null);
			System.out.println("id de procesos para lado cliente: "+detailC);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public TbSystemUser getClientByUserId(Long userId) {
		try{
						
			TbSystemUser user = em.find(TbSystemUser.class, userId);
			
			if(user.getSystemUserMasterId()==null){
				return user;
			} else{
				return (em.find(TbSystemUser.class, user.getSystemUserMasterId()));
			}
			
			
			
		}catch(NoResultException e){
			e.printStackTrace();
			return null;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		} 
	}

}
