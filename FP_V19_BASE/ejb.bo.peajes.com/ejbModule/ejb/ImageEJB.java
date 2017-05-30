package ejb;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
import jpa.TbCodeImagesRejected;
import jpa.TbImage;
import jpa.TbNotificationTransaction;
import jpa.TbObjection;
import jpa.TbProcessTrack;
import jpa.TbSystemUser;
import jpa.TbTransaction;
import jpa.TbTransactionState;
import jpa.TbTransactionValidateState;
import util.Images;
import util.NotificationsList;

@Stateless(mappedName="ejb/Image")
public class ImageEJB implements Image{
	
	@PersistenceContext(unitName = "bo") 
	EntityManager em;
	
	@EJB(mappedName="ejb/Process")
	private Process process;
	
	@EJB(mappedName ="ejb/sendMail")
	private SendMail sendMail;
	
	@EJB(mappedName ="ejb/email/Outbox")
	private Outbox outbox;
	
	private ObjectEM objectEM;
	
	@EJB(mappedName="ejb/Log")
	private Log log;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Images> getImages(String url,String url2){
		List<Images> img = null;
	    //Query q=em.createNativeQuery("select system_parameter_value from tb_system_parameter where system_parameter_id in (11,12) order by system_parameter_id");
		Query q =em.createQuery("select p.systemParameterValue from TbSystemParameter p where p.systemParameterId in (11L,12L)");
		List<Object> lis=(List<Object>) q.getResultList();
		String r1, r2;
		r1= (String) lis.get(0);
		r2= (String) lis.get(1);
	    
		if(r1==null){
			r1="http://localhost:81/pictures/";
		}
		if(r2==null){
			r2="c:\\properties\\pictures\\";
		}
		System.out.println("ruta1" + r1+url2);
		System.out.println("ruta2" + r2 +url);
		
		//File file= new File(url);
		File file2= new File(r2+url);
		String p=file2.getAbsolutePath();
		String [] list= file2.list();
		if(list!=null){
			img= new ArrayList<Images>();
			int ind=1;
			for(int i=0;i<list.length;i++){
				if(!list[i].equals("Thumbs.db")){
					System.out.println("nombre" + list[i]);
					Images im= new Images();
					//im.setName("http://localhost:80/webdav/"+url2+list[i]);
					im.setName(r1+url2+list[i]);
					im.setPath(url);
					im.setId(ind);
					ind++;
					System.out.println("ruta" + im.getName());
					img.add(im);	
				}
				
			}
			System.out.println("path " + p);
		}
		return img;
	}

	@Override
	public List<TbCodeImagesRejected> getCodesImagesRejected() {
		List<TbCodeImagesRejected> list= new ArrayList<TbCodeImagesRejected>();
		Query q= em.createQuery("select t from TbCodeImagesRejected t where t.type=1");
		List<?> lis=q.getResultList();
		for(Object o:lis){
			TbCodeImagesRejected tn=(TbCodeImagesRejected) o;
			list.add(tn);
		}
		return list;
	}

	@Override
	public boolean createNotification(String idPhoto, Date date, String name,
			String notification, Long typeId, String usrs,Long idTran, Long idClient, String dateTransaction) {
		System.out.println("id foto" + idPhoto);
		System.out.println("date en ejb" + date);
		boolean res=false;
		TbImage ti = null;
		try{
			TbCodeImagesRejected tc=null;
			Object o=null;
			if(!typeId.equals(-1L)){
				tc= em.find(TbCodeImagesRejected.class, typeId);
			}
			
			TbTransaction tt= em.find(TbTransaction.class, idTran);
			
			try{
				Query q= em.createQuery("select t from TbImage t where t.ImageUrl=?1").setParameter(1, name);
				o=q.getSingleResult();
				if(o!=null){
					ti=(TbImage) o;
				}
			}catch(NoResultException ex){
				if(o==null){
					ti= new TbImage();
					ti.setImageUrl(name);
					ti.setTypeRejectedId(tc);
					
					em.persist(ti);
				}
			}
			TbNotificationTransaction tn= new TbNotificationTransaction();
			tn.setCodeImageId(tc);
			tn.setDescription(notification);
			tn.setImageId(ti);
			tn.setNotificationDate(date);
			tn.setTransactionId(tt);
			tn.setUser(usrs);
			
			em.persist(tn);
			
			if(typeId.equals(-1L)){
				TbSystemUser cl= em.find(TbSystemUser.class, idClient);
		    	/*sendMail.sendMail(EmailType.CLIENT, cl.getUserEmail(), EmailSubject.CLIENT_CREATE_NOTIFICATION, 
		    			"Buen Dia, \r\n Se ha Creado Notificación de Infracción a la transacción del dia "+ dateTransaction );*/
				outbox.insertMessageOutbox(cl.getUserId(), 
		                   EmailProcess.CREATE_NOTIFICATION,
		                   tt.getTbAccount().getAccountId(),
		                   null, 
		                   tt.getTransactionId(), 
		                   tt.getTbDevice().getDeviceId(),
		                   tt.getTbVehicle().getVehicleId(),
		                   null,
		                   null,
		                   null,
		                   Long.parseLong(usrs),
		                   null,
		                   null,
		                   null,
		                   true,
					       null);
			}
			res=true;
		}catch(Exception ex){
			ex.printStackTrace();
			res=false;
		}
		return res;
	}

	@Override
	public List<NotificationsList> getNotifications(Long transactionId2) {
		List<NotificationsList> lista= new ArrayList<NotificationsList>();
		try{
			
			Query q= em.createNativeQuery("select tn.notification_date, tn.description as desc1, ci.description as desc2 , ti.image_url , tn.usrs from tb_notification tn " 
                    + " inner join tb_image ti on ti.image_id= tn.image_id "
                    + " left join codes_images_rejected ci on tn.code_image_rejected_id= ci.notification_type_id "
                    + " where tn.transaction_id=?1 and (tn.description<>'SE RECHAZÓ IMAGEN' or tn.description is null) order by tn.notification_date");
			
                   q.setParameter(1,transactionId2);
                   List<?> list=q.getResultList(); 
                   System.out.println("entre al ejb");
                   
                   for(int i=0;i<list.size();i++){
                	   Object[] o= (Object[]) list.get(i);
                	   NotificationsList n= new NotificationsList();
                	   n.setNotificationDate(o[0].toString());
                	   n.setNotification(o[1]!=null?o[1].toString():"NO APLICA");
                	   n.setDescriptionType(o[2]!=null?o[2].toString():"NO APLICA");
                	   n.setImageUrl(o[3].toString());
                	   n.setUsrs(o[4].toString());
                	   
                       System.out.println("fecha" +o[0].toString());
                       System.out.println(o[1]!=null?o[1].toString():"NO APLICA");
                       System.out.println("tipo problema" + (o[2]!=null?o[2].toString():"NO APLICA"));
                       System.out.println("imagen" +o[3].toString());
                       System.out.println("usuario" +o[4].toString());
                       
                	   lista.add(n);

                   }
			}catch(Exception ex){
				ex.printStackTrace();
			}
		
			return lista;
		}

	@Override
	public List<TbCodeImagesRejected> getCodesImagesRejected2() {
		List<TbCodeImagesRejected> list= new ArrayList<TbCodeImagesRejected>();
		Query q= em.createQuery("select t from TbCodeImagesRejected t where t.type=2");
		List<?> lis=q.getResultList();
		for(Object o:lis){
			TbCodeImagesRejected tn=(TbCodeImagesRejected) o;
			list.add(tn);
		}
		return list;
	}

	@Override
	public List<NotificationsList> getNotifications2(Long transactionId2) {
		List<NotificationsList> lista= new ArrayList<NotificationsList>();
		try{
			
			Query q= em.createNativeQuery("select tn.notification_date, tn.description as desc1, ci.description as desc2 , ti.image_url , tn.usrs from tb_notification tn " 
                    + " inner join tb_image ti on ti.image_id= tn.image_id "
                    + " left join codes_images_rejected ci on tn.code_image_rejected_id= ci.notification_type_id "
                    + " where tn.transaction_id=?1 and tn.description='SE RECHAZÓ IMAGEN' order by tn.notification_date");
			
                   q.setParameter(1,transactionId2);
                   List<?> list=q.getResultList(); 
                   System.out.println("entre al ejb");
                   
                   for(int i=0;i<list.size();i++){
                	   Object[] o= (Object[]) list.get(i);
                	   NotificationsList n= new NotificationsList();
                	   n.setNotificationDate(o[0].toString());
                	   n.setNotification(o[1]!=null?o[1].toString():"NO APLICA");
                	   n.setDescriptionType(o[2]!=null?o[2].toString():"NO APLICA");
                	   n.setImageUrl(o[3].toString());
                	   n.setUsrs(o[4].toString());
                	   
                       System.out.println("fecha" +o[0].toString());
                       System.out.println(o[1]!=null?o[1].toString():"NO APLICA");
                       System.out.println("tipo problema" + (o[2]!=null?o[2].toString():"NO APLICA"));
                       System.out.println("imagen" +o[3].toString());
                       System.out.println("usuario" +o[4].toString());
                       
                	   lista.add(n);

                   }
			}catch(Exception ex){
				ex.printStackTrace();
			}
		
			return lista;
		}

	
	@Override
	public String createNotificationRejected(String idPhoto, Date date, String name,
			String notification, Long typeId, String usrs,Long idTran) {
		System.out.println("id foto" + idPhoto);
		System.out.println("date en ejb" + date);
		String res="";
		TbImage ti = null;
		TbCodeImagesRejected tc=null;
		TbTransaction tt = null;
		Object o2 = null;
		try{
			
			if(!typeId.equals(-1L)){
				tc= em.find(TbCodeImagesRejected.class, typeId);
			}
			
			tt= em.find(TbTransaction.class, idTran);
			
			Query q= em.createQuery("select t from TbImage t where t.ImageUrl=?1").setParameter(1, name);
			Object o=q.getSingleResult();
			if(o!=null){
				ti= (TbImage) o;
				try{
					Query qq= em.createQuery("select t from TbNotificationTransaction t where t.imageId=?1 and t.description='SE RECHAZÓ IMAGEN'");
					qq.setParameter(1, ti);
					
					o2=qq.getSingleResult();	

					if(o2!=null){
						res="!!!Error Esta Imagen ya fue Rechazada";	
					}
				
				}catch(NoResultException ex){
					if(o2==null){
						TbNotificationTransaction tn= new TbNotificationTransaction();
						tn.setCodeImageId(tc);
						tn.setDescription(notification);
						tn.setImageId(ti);
						tn.setNotificationDate(date);
						tn.setTransactionId(tt);
						tn.setUser(usrs);
							
						em.persist(tn);
							
						res="Transacción Realizada Correctamente";
					}
				}
				
			}
			
		}catch(NoResultException e){
			ti= new TbImage();
			ti.setImageUrl(name);
			ti.setTypeRejectedId(tc);
				
			em.persist(ti);
			
			try{
				Query qq= em.createQuery("select t from TbNotificationTransaction t where t.imageId=?1 and t.description='SE RECHAZÓ IMAGEN'");
				qq.setParameter(1, ti);
				
				o2=qq.getSingleResult();	

				if(o2!=null){
					res="!!!Error Esta Imagen ya fue Rechazada";	
				}
			
			}catch(NoResultException ex){
				if(o2==null){
					TbNotificationTransaction tn= new TbNotificationTransaction();
					tn.setCodeImageId(tc);
					tn.setDescription(notification);
					tn.setImageId(ti);
					tn.setNotificationDate(date);
					tn.setTransactionId(tt);
					tn.setUser(usrs);
						
					em.persist(tn);
						
					res="Transacción Realizada Correctamente";
				}
			}
			
		}
		return res;
	}
	

	@Override
	public boolean createObjection(Date date, Date dateTransaction, String objection, String usrs, Long accountId, String ip, Long chargeId, Long companyId, Long stationId, Long laneId){
		boolean res=false;
		try{
			TbCharges tc= em.find(TbCharges.class, chargeId);
			TbAccount ta=em.find(TbAccount.class, accountId);
			
			TbObjection to= new TbObjection();
			to.setDate(new Timestamp(date.getTime()));
			to.setDateTransaction(new Timestamp(dateTransaction.getTime()));
			to.setObjection(objection);
			to.setUsrs(usrs);
			to.setAccountId(em.find(TbAccount.class, accountId));
			to.setCharge(tc);
			to.setCompanyId(companyId);
			to.setStationId(stationId);
			to.setLaneId(laneId);
			to.setState(ObjectionStateType.PENDING_AUTHORIZATION.getId());
			
			em.persist(to);
			Long idPT, c;
			Long user=Long.parseLong(usrs);
			//proceso administrador
			TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, user);
			if(pt==null){
				idPT=process.createProcessTrack(ProcessTrackType.CLIENT,  user, ip, Long.parseLong(usrs));
			}
			else{
				idPT=pt.getProcessTrackId();
			}
			Long detail=process.createProcessDetail(idPT,ProcessTrackDetailType.OBJECTION_ACCOUNT, objection, user, ip, 0, "", null,null,null,null);
			
			//proceso cliente
			TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,user);
			if(ptc==null){
				c=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, user, ip, user);
			}
			else{
				c=ptc.getProcessTrackId();
			}
			process.createProcessDetail(c,ProcessTrackDetailType.OBJECTION_ACCOUNT , objection,user, "ip", 0, "", null,null,null,null);
			
			//sendMail(usrs, accountId,detail,ip);
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
	                   user,
	                   null,
	                   null,
	                   null,
	                   true,
				       null);
			
			res=true;
		}catch(Exception ex){
			res=false;
			ex.printStackTrace();
		}
		
		return res;
	}
	
	public void sendMail(String usrs, Long accountId, Long detailId, String ip){
		Query q= em.createNativeQuery("select ts.user_email from tb_task_type tt "
                                       + " inner join re_task_type_user tu on tt.task_type_id= tu.task_type_id "
                                       + " inner join tb_system_user ts on tu.user_id= ts.user_id "
                                       + " where tt.task_type_id=12");
		
		List<?> lis=q.getResultList();
		
		for(Object o: lis){
			String email=(String) o;
		
			/*sendMail.sendMail(EmailType.ADMIN, email, EmailSubject.CLIENT_CREATE_OBJECTION,
					"Buen Dia, \r\n El Cliente " + usrs + " creó la objeción asociada a la cuenta " + accountId);*/
			
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TbTransactionState> getStateTransaction() {
		
		 List<TbTransactionState> list1= new ArrayList<TbTransactionState>();
		Query q= em.createQuery("select t from TbTransactionState t");
		List<Object> lis=q.getResultList();
		
		for(Object o: lis){
			TbTransactionState tt= (TbTransactionState) o;
			list1.add(tt);
		}
		
		return list1; 
	}

	@Override
	public boolean crateValidation(Long typeId, String notification, Long userId, Long transactionId) {
		boolean res=false;
		try{
			objectEM = new ObjectEM(em);
			TbTransactionState tt=em.find(TbTransactionState.class, typeId);
			
			if(tt!=null){
				TbTransactionValidateState tv= new TbTransactionValidateState();
				tv.setDateValidation(new Timestamp(System.currentTimeMillis()));
				tv.setObservation(notification);
				tv.setUserId(userId);
				tv.setStateId(tt);
				tv.setTransactionId(transactionId);
			    
				if(objectEM.create(tv)){
					Query q=em.createNativeQuery("update tb_transaction set validate_type_id=?1 where transaction_id=?2")
					.setParameter(1, tv.getValidateId()).setParameter(2, transactionId);
					
					int i=q.executeUpdate();
					if(i==1){
						log.insertLog("El usuario: " + userId + " validó la transacción con id: " + transactionId +
								" y la clasificadó como: " + tt.getNameState() + ". Notificación: " + notification , LogReference.TRANSACTION, LogAction.CREATE,
								"localhost", userId);
						res=true;
					}
				}
				else{
					res=false;
				}	
			}
			else{
				res=false;
			}
			
		}
		catch(Exception ex){
			ex.printStackTrace();
			res=false;
		}
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getInfoTransactionValidate(Long transactionId){
		List<String> info= new ArrayList<String>();
			
		Query q= em.createNativeQuery("select date_validation || ' -  ' || observation from tb_transaction_validate_state where transaction_id=?1 order by date_validation ");
		q.setParameter(1, transactionId);
		info= (List<String>) q.getResultList();
		
		return info;
	}
}
