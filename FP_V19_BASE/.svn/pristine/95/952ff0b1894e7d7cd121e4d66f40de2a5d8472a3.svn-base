package ejb;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jpa.ReUserRole;
import jpa.TbDocument;
import jpa.TbProcessTrack;
import jpa.TbRole;
import jpa.TbSystemUser;
import jpa.TbTypeDocument;

import constant.EmailProcess;
import constant.LogAction;
import constant.LogReference;
import constant.ProcessTrackDetailType;
import constant.ProcessTrackType;

import crud.ObjectEM;
import ejb.email.Outbox;

import util.AllInfoVerification;


/**
 * @author psanchez
 *
 */
@Stateless(mappedName = "ejb/Document")
public class DocumentEJB implements Document {

	@PersistenceContext(unitName = "bo") 
	EntityManager em;

	private ObjectEM objectEM;
		
	@EJB(mappedName ="ejb/email/Outbox")
	private Outbox outbox;
	
	@EJB(mappedName="ejb/Process")
	private Process process;
	
	@EJB(mappedName="ejb/Log")
	private Log log;
	
	
	/**
	 * Default constructor.
	 */
	public DocumentEJB() {
         
	}

	@SuppressWarnings("unused")
	@Override
	public boolean getValidateDocument(Long documentId, Long typeDocumentId, 
        String documentState, String description, Long userId, String ip, Long creationUser) {
		int rolExist = 0;
	    BigDecimal rol = null;
		try {
	         TbSystemUser user   = em.find(TbSystemUser.class, userId);
			 	
			 Query query3 = 
				em.createNativeQuery("SELECT count(*) FROM tb_document " +
			  						 "WHERE user_id = ?1 " +
			  						 "AND document_id <> ?2 " +
			  						 "AND state_document=4 ");
				query3.setParameter(1, user.getUserId());
				query3.setParameter(2, documentId);
				
				BigDecimal count= (BigDecimal) query3.getSingleResult();
				Long Count = count.longValue(); 	
				
			if (documentState.equals("3") && typeDocumentId==1 && (user.getUserState().getUserStateId().longValue()==4L ||
					user.getUserState().getUserStateId().longValue()==-1L)) {	
				System.out.println(" [ pase por aqui---104---->] ");
				
				TbDocument td = em.find(TbDocument.class, documentId);
				td.setStateDocument(3);
				td.setDescription(description);
				td.setProcessDateDocument(new Timestamp(System.currentTimeMillis()));	
				
					if (Count>0L){
						Query q = em.createNativeQuery("SELECT user_role_id FROM re_user_role " +
														"WHERE user_id=?1 ");
														q.setParameter(1, user.getUserId());
														for (Object obj : q.getResultList()) {
														rol = (BigDecimal) obj;
														if(rol.longValue()==3L){
														rolExist = 1;
														break;
														}
						Query updUser = em.createNativeQuery("UPDATE tb_system_user SET user_state = 3 WHERE user_id = ?1 ");	
						updUser.setParameter(1, user.getUserId());
						updUser.executeUpdate();
						}
						
						if(rolExist == 0){
							ReUserRole ru = em.find(ReUserRole.class, rol.longValue());
							if(!(ru.getTbRole().getRoleId()== 3L)){						
								TbRole r = em.find(TbRole.class, 3L);
								ru.setTbRole(r);
								em.merge(ru);
								em.flush();
							}	
						}					
						objectEM = new ObjectEM(em);	
						if(objectEM.update(td)){
						log.insertLog("Validar Documentos | Se ha validado el documento: " + td.getDocumentId()+ 
						      " Documento " + td.getTbTypeDocument().getTypeDocumentDescription() + " fecha" + td.getProcessDateDocument(), 
						      LogReference.DOCUMENT_VERIFICATION, LogAction.CREATE, ip, creationUser);
						
						//proceso administrador
						TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, userId);
						Long idPTA;
						if(pt==null){
							idPTA=process.createProcessTrack(ProcessTrackType.CLIENT, userId, ip, creationUser);
						}else{
							idPTA=pt.getProcessTrackId();
						}
						
						Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.MY_CLIENT_DOCUMENT_VERIFICATION,  
							"El documento tipo "+td.getTbTypeDocument().getTypeDocumentDescription()+ " con nombre "+td.getNameDocument()+" ha sido aprobado.", creationUser, ip, 1, "", null,null,null,null);
						
						//proceso cliente
						TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, userId);
						Long idPTC;
						if(ptc==null){
							idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, userId, ip, creationUser);
						}else{
							idPTC=ptc.getProcessTrackId();
						}
						
						Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.MY_CLIENT_DOCUMENT_VERIFICATION,  
								"El documento tipo "+td.getTbTypeDocument().getTypeDocumentDescription()+ " con nombre "+td.getNameDocument()+" ha sido aprobado.", creationUser, ip, 1, "", null,null,null,null);
						
						//sendMail.sendMail(EmailType.CLIENT, user.getUserEmail(),
						//	EmailSubject.CLIENT_VERIFICATION, "Estimado Usuario, \r\n \r\nSe han aprobado los documentos satisfactoriamente enviados. Para activar y poder hacer uso del producto FacilPass por favor ingrese a www.facilpass.com con su usario y contraseña y continúe con los pasos requeridos. \r\n \r\nCordialmente, \r\nFacilPass");
						outbox.insertMessageOutbox(user.getUserId(), 
						       EmailProcess.DOCUMENT_VALIDATION_APPROVED,
						       null,
						       null,
						       null, 
						       null,
						       null,
						       null,
						       null,
						       td.getDocumentId(),
						       creationUser,
				               null,
				               null,
				               null,
						       true,
						       null);
						 return true;
						}
					  }else{
					    	System.out.println(" [ pase por aqui---176---->] ");
					    	Query q = em.createNativeQuery("SELECT user_role_id FROM re_user_role " +
															"WHERE user_id=?1 ");
															q.setParameter(1, user.getUserId());
                            for (Object obj : q.getResultList()) {
								rol = (BigDecimal) obj;
								if(rol.longValue()==3L){
								rolExist = 1;
								break;
								}
							Query updUser = em.createNativeQuery("UPDATE tb_system_user SET user_state = 3 WHERE user_id = ?1 ");	
							updUser.setParameter(1, user.getUserId());
							updUser.executeUpdate();
							}
							
							if(rolExist == 0){
							ReUserRole ru = em.find(ReUserRole.class, rol.longValue());
								if(!(ru.getTbRole().getRoleId()== 3L)){						
									TbRole r = em.find(TbRole.class, 3L);
									ru.setTbRole(r);
									em.merge(ru);
									em.flush();
								}	
							}					
							objectEM = new ObjectEM(em);	
							if(objectEM.update(td)){
							log.insertLog("Validar Documentos | Se ha validado el documento: " + td.getDocumentId()+ 
							  " Documento " + td.getTbTypeDocument().getTypeDocumentDescription() + " fecha" + td.getProcessDateDocument(), 
							  LogReference.DOCUMENT_VERIFICATION, LogAction.CREATE, ip, creationUser);
							
							//proceso administrador
							TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, userId);
							Long idPTA;
							if(pt==null){
							idPTA=process.createProcessTrack(ProcessTrackType.CLIENT, userId, ip, creationUser);
							}else{
							idPTA=pt.getProcessTrackId();
							}
							
							Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.MY_CLIENT_DOCUMENT_VERIFICATION,  
									"El documento tipo "+td.getTbTypeDocument().getTypeDocumentDescription()+ " con nombre "+td.getNameDocument()+" ha sido aprobado.", creationUser, ip, 1, "", null,null,null,null);
							
							//proceso cliente
							TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, userId);
							Long idPTC;
							if(ptc==null){
							idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, userId, ip, creationUser);
							}else{
							idPTC=ptc.getProcessTrackId();
							}
							
							Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.MY_CLIENT_DOCUMENT_VERIFICATION,  
									"El documento tipo "+td.getTbTypeDocument().getTypeDocumentDescription()+ " con nombre "+td.getNameDocument()+" ha sido aprobado.", creationUser, ip, 1, "", null,null,null,null);
							
							//sendMail.sendMail(EmailType.CLIENT, user.getUserEmail(),
							//EmailSubject.CLIENT_VERIFICATION, "Estimado Usuario, \r\n \r\nSe han aprobado los documentos satisfactoriamente enviados. Para activar y poder hacer uso del producto FacilPass por favor ingrese a www.facilpass.com con su usario y contraseña y continúe con los pasos requeridos. \r\n \r\nCordialmente, \r\nFacilPass");
							outbox.insertMessageOutbox(user.getUserId(), 
							   EmailProcess.DOCUMENT_VALIDATION_APPROVED,
							   null,
							   null,
							   null, 
							   null,
							   null,
							   null,
							   null,
							   td.getDocumentId(),
							   creationUser,
				               null,
				               null,
				               null,
							   true,
						       null);
							return true;
							}	
					    }	
					}else if (documentState.equals("-1") && typeDocumentId==1 && user.getUserState().getUserStateId().longValue()==4L) {	
						System.out.println(" [ pase por aqui---177---->] ");
						TbDocument td = em.find(TbDocument.class, documentId);
			            td.setStateDocument(-1);
			            td.setDescription(description);
						td.setProcessDateDocument(new Timestamp(System.currentTimeMillis()));	
						
							if (Count>0L){
								System.out.println(" [ pase por aqui---183---->] ");
						    	Query updUser = em.createNativeQuery("UPDATE tb_system_user SET user_state = 4 WHERE user_id = ?1 ");	
								updUser.setParameter(1, user.getUserId());
								updUser.executeUpdate();	
								
								
								System.out.println(" [ pase por aqui---creationUser---->] "+creationUser);
								System.out.println(" [ pase por aqui---userId---->] "+userId);


								objectEM = new ObjectEM(em);	
								if(objectEM.update(td)){
								log.insertLog("Rechazar Documentos | Se ha rechazado el documento ID: " + td.getDocumentId()+ 
						              	  " Documento: " + td.getTbTypeDocument().getTypeDocumentDescription() + " Fecha: " + td.getUploadDateDocument(), 
						                  LogReference.DOCUMENT_VERIFICATION, LogAction.CREATE, ip, creationUser);
							
								//proceso administrador
						        TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, userId);
								Long idPTA;
									if(pt==null){
										idPTA=process.createProcessTrack(ProcessTrackType.CLIENT, userId, ip, creationUser);
									}else{
										idPTA=pt.getProcessTrackId();
									}
									Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.MY_CLIENT_DOCUMENT_VERIFICATION,  
											"El documento "+td.getNameDocument()+ " fue rechazado con la siguiente observación: "+td.getDescription()+".", creationUser, ip, 1, "", null,null,null,null);
									
								//proceso cliente
						        TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, userId);
								Long idPTC;
									if(ptc==null){
										idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, userId, ip, creationUser);
									}else{
										idPTC=ptc.getProcessTrackId();
									}
									Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.MY_CLIENT_DOCUMENT_VERIFICATION,  
											"El documento "+td.getNameDocument()+ " fue rechazado con la siguiente observación: "+td.getDescription()+".", creationUser, ip, 1, "", null,null,null,null);
								
							    outbox.insertMessageOutbox(user.getUserId(),
							    	   EmailProcess.DOCUMENT_VALIDATION_REJECTED,
					                   null,
					                   null,
					                   null, 
					                   null,
					                   null,
					                   null,
					                   null,
					                   td.getDocumentId(),
					                   creationUser,
						               null,
						               null,
						               null,
					                   true,
								       null);
								return true;
								}
						    }else{
						    	System.out.println(" [ pase por aqui---226---->] ");
						    	Query updUser = em.createNativeQuery("UPDATE tb_system_user SET user_state = -1 WHERE user_id = ?1 ");	
								updUser.setParameter(1, user.getUserId());
								updUser.executeUpdate();	
								
								objectEM = new ObjectEM(em);	
								if(objectEM.update(td)){
								log.insertLog("Rechazar Documentos | Se ha rechazado el documento ID: " + td.getDocumentId()+ 
						              	  " Documento: " + td.getTbTypeDocument().getTypeDocumentDescription() + " Fecha: " + td.getUploadDateDocument(), 
						                  LogReference.DOCUMENT_VERIFICATION, LogAction.CREATE, ip, creationUser);
							
								//proceso administrador
						        TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, userId);
								Long idPTA;
									if(pt==null){
										idPTA=process.createProcessTrack(ProcessTrackType.CLIENT, userId, ip, creationUser);
									}else{
										idPTA=pt.getProcessTrackId();
									}
									Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.MY_CLIENT_DOCUMENT_VERIFICATION,  
											"El documento "+td.getNameDocument()+ " fue rechazado con la siguiente observación: "+td.getDescription()+".", creationUser, ip, 1, "", null,null,null,null);
									
								//proceso cliente
						        TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, userId);
								Long idPTC;
									if(ptc==null){
										idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, userId, ip, creationUser);
									}else{
										idPTC=ptc.getProcessTrackId();
									}
									Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.MY_CLIENT_DOCUMENT_VERIFICATION,  
											"El documento "+td.getNameDocument()+ " fue rechazado con la siguiente observación: "+td.getDescription()+".", creationUser, ip, 1, "", null,null,null,null);
								
									outbox.insertMessageOutbox(user.getUserId(),
									    	   EmailProcess.DOCUMENT_VALIDATION_REJECTED,
							                   null,
							                   null,
							                   null, 
							                   null,
							                   null,
							                   null,
							                   null,
							                   td.getDocumentId(),
							                   creationUser,
								               null,
								               null,
								               null,
							                   true,
										       null);
								return true;
						       }
						    }
					    }else if (documentState.equals("3") && (typeDocumentId==2 || typeDocumentId==3) && user.getUserState().getUserStateId().longValue()==4L) {
					    	    System.out.println(" [ pase por aqui---270---->] ");
						    	TbDocument td = em.find(TbDocument.class, documentId);
					            td.setStateDocument(3);
					            td.setDescription(description);
								td.setProcessDateDocument(new Timestamp(System.currentTimeMillis()));	
								
							    if (Count>0L){
							    	System.out.println(" [ pase por aqui---276---->] ");
							    	Query updUser = em.createNativeQuery("UPDATE tb_system_user SET user_state = 4 WHERE user_id = ?1 ");	
									updUser.setParameter(1, user.getUserId());
									updUser.executeUpdate();	
									
									objectEM = new ObjectEM(em);	
									if(objectEM.update(td)){
									log.insertLog("Validar Documentos | Se ha validado el documento ID: " + td.getDocumentId()+ 
							              	  " Documento: " + td.getTbTypeDocument().getTypeDocumentDescription() + " Fecha: " + td.getUploadDateDocument(), 
							                  LogReference.DOCUMENT_VERIFICATION, LogAction.CREATE, ip, creationUser);
								
									//proceso administrador
							        TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, userId);
									Long idPTA;
										if(pt==null){
											idPTA=process.createProcessTrack(ProcessTrackType.CLIENT, userId, ip, creationUser);
										}else{
											idPTA=pt.getProcessTrackId();
										}
										Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.MY_CLIENT_DOCUMENT_VERIFICATION,  
												"El documento tipo "+td.getTbTypeDocument().getTypeDocumentDescription()+ " con nombre "+td.getNameDocument()+" ha sido aprobado.", creationUser, ip, 1, "", null,null,null,null);
										
									//proceso cliente
							        TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, userId);
									Long idPTC;
										if(ptc==null){
											idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, userId, ip, creationUser);
										}else{
											idPTC=ptc.getProcessTrackId();
										}
										Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.MY_CLIENT_DOCUMENT_VERIFICATION,  
												"El documento tipo "+td.getTbTypeDocument().getTypeDocumentDescription()+ " con nombre "+td.getNameDocument()+" ha sido aprobado.", creationUser, ip, 1, "", null,null,null,null);
									
								    outbox.insertMessageOutbox(user.getUserId(),
								    	   EmailProcess.DOCUMENT_VALIDATION_APPROVED,
						                   null,
						                   null,
						                   null, 
						                   null,
						                   null,
						                   null,
						                   null,
						                   td.getDocumentId(),
						                   creationUser,
							               null,
							               null,
							               null,
						                   true,
									       null);
									  return true;
									}
							    }else{
							    	System.out.println(" [ pase por aqui---319---->] ");
							    	Query updUser = em.createNativeQuery("UPDATE tb_system_user SET user_state = -1 WHERE user_id = ?1 ");	
									updUser.setParameter(1, user.getUserId());
									updUser.executeUpdate();	
									
									objectEM = new ObjectEM(em);	
									if(objectEM.update(td)){
									//proceso administrador
							        TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, userId);
									Long idPTA;
										if(pt==null){
											idPTA=process.createProcessTrack(ProcessTrackType.CLIENT, userId, ip, creationUser);
										}else{
											idPTA=pt.getProcessTrackId();
										}
										Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.MY_CLIENT_DOCUMENT_VERIFICATION,  
												"El documento tipo "+td.getTbTypeDocument().getTypeDocumentDescription()+ " con nombre "+td.getNameDocument()+" ha sido aprobado.", creationUser, ip, 1, "", null,null,null,null);
										
									//proceso cliente
							        TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, userId);
									Long idPTC;
										if(ptc==null){
											idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, userId, ip, creationUser);
										}else{
											idPTC=ptc.getProcessTrackId();
										}
										Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.MY_CLIENT_DOCUMENT_VERIFICATION,  
												"El documento tipo "+td.getTbTypeDocument().getTypeDocumentDescription()+ " con nombre "+td.getNameDocument()+" ha sido aprobado.", creationUser, ip, 1, "", null,null,null,null);
								
										outbox.insertMessageOutbox(user.getUserId(),
										    	   EmailProcess.DOCUMENT_VALIDATION_APPROVED,
								                   null,
								                   null,
								                   null, 
								                   null,
								                   null,
								                   null,
								                   null,
								                   td.getDocumentId(),
								                   creationUser,
									               null,
									               null,
									               null,
								                   true,
											       null);
									return true;
							      }
							   }		
					    	}else if (documentState.equals("-1") && (typeDocumentId==2 || typeDocumentId==3) && user.getUserState().getUserStateId().longValue()==4L) {
							    System.out.println(" [ pase por aqui---359---->] ");
						    	TbDocument td = em.find(TbDocument.class, documentId);
					            td.setStateDocument(-1);
					            td.setDescription(description);
								td.setProcessDateDocument(new Timestamp(System.currentTimeMillis()));	
							
							    if (Count>0L){
							    	Query updUser = em.createNativeQuery("UPDATE tb_system_user SET user_state = 4 WHERE user_id = ?1 ");	
									updUser.setParameter(1, user.getUserId());
									updUser.executeUpdate();
									
									objectEM = new ObjectEM(em);	
									if(objectEM.update(td)){
									log.insertLog("Rechazar Documentos | Se ha rechazado el documento ID: " + td.getDocumentId()+ 
							              	  " Documento: " + td.getTbTypeDocument().getTypeDocumentDescription() + " Fecha: " + td.getUploadDateDocument(), 
							                  LogReference.DOCUMENT_VERIFICATION, LogAction.CREATE, ip, creationUser);
								
									//proceso administrador
							        TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, userId);
									Long idPTA;
										if(pt==null){
											idPTA=process.createProcessTrack(ProcessTrackType.CLIENT, userId, ip, creationUser);
										}else{
											idPTA=pt.getProcessTrackId();
										}
										Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.MY_CLIENT_DOCUMENT_VERIFICATION,  
												"El documento "+td.getNameDocument()+ " fue rechazado con la siguiente observación: "+td.getDescription()+".", creationUser, ip, 1, "", null,null,null,null);

									//proceso cliente
							        TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, user.getUserId());
									Long idPTC;
										if(ptc==null){
											idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, userId, ip, creationUser);
										}else{
											idPTC=ptc.getProcessTrackId();
										}
										Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.MY_CLIENT_DOCUMENT_VERIFICATION,  
												"El documento "+td.getNameDocument()+ " fue rechazado con la siguiente observación: "+td.getDescription()+".", creationUser, ip, 1, "", null,null,null,null);
									
										outbox.insertMessageOutbox(user.getUserId(),
										    	   EmailProcess.DOCUMENT_VALIDATION_REJECTED,
								                   null,
								                   null,
								                   null, 
								                   null,
								                   null,
								                   null,
								                   null,
								                   td.getDocumentId(),
								                   creationUser,
									               null,
									               null,
									               null,
								                   true,
											       null);
									return true;
									}
							    }else{
								    System.out.println(" [ pase por aqui---407---->] ");
							    	Query updUser = em.createNativeQuery("UPDATE tb_system_user SET user_state = -1 WHERE user_id = ?1 ");	
									updUser.setParameter(1, user.getUserId());
									updUser.executeUpdate();	
									
									objectEM = new ObjectEM(em);	
									if(objectEM.update(td)){
									log.insertLog("Rechazar Documentos | Se ha rechazado el documento ID: " + td.getDocumentId()+ 
							              	  " Documento: " + td.getTbTypeDocument().getTypeDocumentDescription() + " Fecha: " + td.getUploadDateDocument(), 
							                  LogReference.DOCUMENT_VERIFICATION, LogAction.CREATE, ip, creationUser);
								
									//proceso administrador
							        TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, userId);
									Long idPTA;
										if(pt==null){
											idPTA=process.createProcessTrack(ProcessTrackType.CLIENT, userId, ip, creationUser);
										}else{
											idPTA=pt.getProcessTrackId();
										}
										Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.MY_CLIENT_DOCUMENT_VERIFICATION,  
												"El documento "+td.getNameDocument()+ " fue rechazado con la siguiente observación: "+td.getDescription()+".", creationUser, ip, 1, "", null,null,null,null);
										
									//proceso cliente
							        TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, user.getUserId());
									Long idPTC;
										if(ptc==null){
											idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, userId, ip, creationUser);
										}else{
											idPTC=ptc.getProcessTrackId();
										}
										Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.MY_CLIENT_DOCUMENT_VERIFICATION,  
												"El documento "+td.getNameDocument()+ " fue rechazado con la siguiente observación: "+td.getDescription()+".", creationUser, ip, 1, "", null,null,null,null);
									
										outbox.insertMessageOutbox(user.getUserId(),
										    	   EmailProcess.DOCUMENT_VALIDATION_REJECTED,
								                   null,
								                   null,
								                   null, 
								                   null,
								                   null,
								                   null,
								                   null,
								                   td.getDocumentId(),
								                   creationUser,
									               null,
									               null,
									               null,
								                   true,
											       null);
									return true;
							      }
							    }
							}else if (documentState.equals("3") && (typeDocumentId==1 || typeDocumentId==2 || typeDocumentId==3)) {
								System.out.println(" [ pase por aqui---451---->] ");
								TbDocument td = em.find(TbDocument.class, documentId);
								td.setStateDocument(3);
					            td.setDescription(description);
								td.setProcessDateDocument(new Timestamp(System.currentTimeMillis()));	
								
					            objectEM = new ObjectEM(em);	
								if(objectEM.update(td)){
								log.insertLog("Validar Documentos | Se ha validado el documento ID: " + td.getDocumentId()+ 
								              " Documento " + td.getTbTypeDocument().getTypeDocumentDescription() + " fecha" + td.getProcessDateDocument(), 
								              LogReference.DOCUMENT_VERIFICATION, LogAction.CREATE, ip, creationUser);
								  
								//proceso administrador
						        TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, userId);
								Long idPTA;
									if(pt==null){
										idPTA=process.createProcessTrack(ProcessTrackType.CLIENT, userId, ip, creationUser);
									}else{
										idPTA=pt.getProcessTrackId();
									}
									
									Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.MY_CLIENT_DOCUMENT_VERIFICATION,  
											"El documento tipo "+td.getTbTypeDocument().getTypeDocumentDescription()+ " con nombre "+td.getNameDocument()+" ha sido aprobado.", creationUser, ip, 1, "", null,null,null,null);
									
								//proceso cliente
								TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,userId);
								Long idPTC;
									if(ptc==null){
										idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, userId, ip, creationUser);
									}else{
										idPTC=ptc.getProcessTrackId();
									}
									
								Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.MY_CLIENT_DOCUMENT_VERIFICATION,  
										"El documento tipo "+td.getTbTypeDocument().getTypeDocumentDescription()+ " con nombre "+td.getNameDocument()+" ha sido aprobado.", creationUser, ip, 1, "", null,null,null,null);
									
								outbox.insertMessageOutbox(user.getUserId(),
								    	   EmailProcess.DOCUMENT_VALIDATION_APPROVED,
						                   null,
						                   null,
						                   null, 
						                   null,
						                   null,
						                   null,
						                   null,
						                   td.getDocumentId(),
						                   creationUser,
							               null,
							               null,
							               null,
						                   true,
									       null);
									return true;
								  } 
							}else if (documentState.equals("-1") && (typeDocumentId==1 ||typeDocumentId==2 || typeDocumentId==3)) {	
								System.out.println(" [ pase por aqui---496---->] ");
								TbDocument td = em.find(TbDocument.class, documentId);
								td.setStateDocument(-1);
					            td.setDescription(description);
								td.setProcessDateDocument(new Timestamp(System.currentTimeMillis()));	
								
					            objectEM = new ObjectEM(em);	
								if(objectEM.update(td)){
								log.insertLog("Rechazar Documentos | Se ha rechazado el documento ID: " + td.getDocumentId()+ 
								              " Documento " + td.getTbTypeDocument().getTypeDocumentDescription() + " fecha" + td.getProcessDateDocument(), 
								              LogReference.DOCUMENT_VERIFICATION, LogAction.CREATE, ip, creationUser);
								  
								//proceso administrador
						        TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, userId);
								Long idPTA;
									if(pt==null){
										idPTA=process.createProcessTrack(ProcessTrackType.CLIENT, userId, ip, creationUser);
									}else{
										idPTA=pt.getProcessTrackId();
									}
								
								Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.MY_CLIENT_DOCUMENT_VERIFICATION,  
										"El documento "+td.getNameDocument()+ " fue rechazado con la siguiente observación: "+td.getDescription()+".", creationUser, ip, 1, "", null,null,null,null);
								
								//proceso cliente
								TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,user.getUserId());
								Long idPTC;
									if(ptc==null){
										idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, userId, ip, creationUser);
									}else{
										idPTC=ptc.getProcessTrackId();
									}
								
								Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.MY_CLIENT_DOCUMENT_VERIFICATION,  
										"El documento "+td.getNameDocument()+ " fue rechazado con la siguiente observación: "+td.getDescription()+".", creationUser, ip, 1, "", null,null,null,null);
								outbox.insertMessageOutbox(user.getUserId(),
								    	   EmailProcess.DOCUMENT_VALIDATION_REJECTED,
						                   null,
						                   null,
						                   null, 
						                   null,
						                   null,
						                   null,
						                   null,
						                   td.getDocumentId(),
						                   creationUser,
							               null,
							               null,
							               null,
						                   true,
									       null);
								return true;
							  } 
						}
			     } catch (Exception e) {
					System.out.println(" [ Error en DocumentEJB.getValidateDocument ] ");
					e.printStackTrace();
					return false;
				}
			return false;
		}

	
	@SuppressWarnings("unchecked")
	public List<AllInfoVerification> getStateDocumentByFilters(Long codeType, String numberDoc, String name, String secondName, 
		   String email, String accountId, String state, String fechaCargue, Date fechaInicial, Date fechaFinal, int page, int rows) {
	  List<AllInfoVerification> documents = new ArrayList<AllInfoVerification>();
	  String qry1, qry2, qry3 = "";

	  try {
		    qry1= "SELECT COUNT(*) FROM (";
			
			qry2= "SELECT * FROM (" ;
			
			qry3= "SELECT a.*, rownum rnum FROM (SELECT DISTINCT to_char(td.document_id), tu.user_id, tc.code_type_abbreviation, tu.user_code, " +
				  "tu.user_names, tu.user_second_names, tt.type_document_id, tt.type_document_description, td.state_document, td.url_document, " +
				  "(SELECT tu.user_names ||' '|| tu.user_second_names FROM tb_system_user tu WHERE tu.user_id = td.user_id_upload), " +
				  "decode(td.description,null,'',td.description), " +
				  "decode(td.upload_date_document,null,'-',TO_CHAR(td.upload_date_document, 'DD/MM/YYYY HH:MI:SS AM')),  " +
				  "decode(td.process_date_document,null,'-',TO_CHAR(td.process_date_document, 'DD/MM/YYYY HH:MI:SS AM')) " +
				  "FROM tb_document td " +
				  "INNER JOIN tb_system_user tu ON td.user_id = tu.user_id " +
				  "INNER JOIN re_user_role rer ON tu.user_id=rer.user_id " +
				  "INNER JOIN tb_role r ON rer.role_id=r.role_id " +
				  "LEFT JOIN tb_account ta ON ta.user_id=tu.user_id " +
				  "INNER JOIN tb_code_type tc ON tu.code_type_id = tc.code_type_id " +
				  "INNER JOIN tb_type_document tt ON td.type_document_id=tt.type_document_id " +
				  "WHERE r.role_id IN (2,3) " +
				  "AND td.state_document <> 5 " +
				  "AND td.state_document <> 6 ";
			
				if((codeType!=null) && (codeType!=-1L)){
					qry3 = qry3+"AND tc.code_type_id="+codeType+" ";
				}
				if(!numberDoc.equals("")){
					qry3 = qry3+"AND tu.user_code like '%"+numberDoc.trim()+"%' ";
				}
				if(!name.equals("")){
					qry3 = qry3+"AND Upper(tu.user_names) like '%"+name.toUpperCase()+"%' ";
				}
				if(!secondName.equals("")){
					qry3 = qry3+"AND Upper(tu.user_second_names) like '%"+secondName.toUpperCase()+"%' ";
				}
				if(!email.equals("") && email.contains("_")){
					qry3 = qry3+"AND Upper(tu.user_email) like '%\\_%' ESCAPE '\\' ";
				}
				if(!email.equals("") && !email.equals("_")){
					qry3 = qry3+"AND lower(tu.user_email) like '%"+email.toLowerCase()+"%' ";
				}
				if(!accountId.equals("")) {
					qry3 = qry3+"AND ta.account_id like '%"+accountId+"%' ";
				}
				if((fechaCargue.equals("1") || fechaCargue.equals("2")) && fechaInicial !=null && fechaFinal != null ) {
					qry3 = qry3+"AND td.upload_date_document BETWEEN " +
						"TO_TIMESTAMP('"+new SimpleDateFormat("dd/MM/yyyy").format(fechaInicial)+" 00:00:00,0000','DD/MM/YYYY HH24:MI:SS,FF') " +
						"AND TO_TIMESTAMP('"+new SimpleDateFormat("dd/MM/yyyy").format(fechaFinal)+" 23:59:59,9999','DD/MM/YYYY HH24:MI:SS,FF') " ;
				}
				if (state.equals("99")) {					
					qry3 = qry3+"AND 99="+state+" ";
			    }else if (state != null) {					
			    	qry3 = qry3+"AND td.state_document="+state+" ";
			    }
				
				
			    if (page!=0){
			    	if(rows==0) {
						qry3 = qry3+ "ORDER BY td.document_id DESC)a )";
					} else {
						qry3 = qry3+ "ORDER BY td.document_id DESC)a " +
			                     "WHERE rownum <= " +(page*rows)+") WHERE rnum > "+((page*rows)-rows)+" ";
					}
					Query query = em.createNativeQuery(qry2+qry3);
					List<Object> result= (List<Object>)query.getResultList();
					for(int i=0;i<result.size();i++){
						Object[] obj=(Object[]) result.get(i);				
						AllInfoVerification doc = new AllInfoVerification();
						if(doc!=null){	
							doc.setDocumentIdU(Long.parseLong(obj[0].toString()));
							doc.setUserIdU(Long.parseLong(obj[1].toString()));
							doc.setCodeTypeAbbrU(obj[2].toString());
							doc.setCodeUserU(Long.parseLong(obj[3].toString()));
							doc.setNameU(obj[4].toString());
							doc.setSecondNameU(obj[5].toString());
							doc.setTypeDocumentIdU(Long.parseLong(obj[6].toString()));
							doc.setTypeDocumentDescriptionU(obj[7].toString());
							doc.setStateU(Long.parseLong(obj[8].toString()));
							doc.setUrl(obj[9].toString());
							doc.setUserUploadU(obj[10]!=null?obj[10].toString():"");
							doc.setDescriptionU(obj[11]!=null?obj[11].toString():"");
							doc.setUploadDateDocumentU(obj[12].toString());
							doc.setProcessDateDocumentU(obj[13].toString());
							documents.add(doc);
						}
					}
	  			}else {
					Query query = em.createNativeQuery(qry1+qry3+")a )");
					documents= query.getResultList();
				}
		} catch (Exception e) {
			System.out.println(" [ Error en DocumentEJB.getStateDocumentByFilters. ] ");
			e.printStackTrace();
		}
	  return documents;
	}
	
	
		
	@Override
	public List<TbTypeDocument> getDocumentList() {
		List<TbTypeDocument> list = new ArrayList<TbTypeDocument>();
		try{
			Query query = 
				em.createQuery("Select m from TbTypeDocument m order by m.typeDocumentId ");			    
			    for (Object obj : query.getResultList()) {
					list.add((TbTypeDocument) obj);
				}
		}catch(Exception e){
			System.out.println("[ Error en DocumentEJB.getDocumentList ]");
			e.printStackTrace();
		}
	   return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<AllInfoVerification> getDocumentbyClient(Long userId){
		  ArrayList<AllInfoVerification> documents = new ArrayList<AllInfoVerification>();
	  try {
		    String qry = "";
			qry = "SELECT DISTINCT to_char(td.document_id), su.user_id, tc.code_type_abbreviation, su.user_code, " +
				  "su.user_names, su.user_second_names, tt.type_document_description, td.state_document, " +
				  "td.url_document FROM tb_document td " +
                  "INNER JOIN tb_system_user su ON td.user_id = su.user_id " +
				  "INNER JOIN tb_type_document tt ON td.type_document_id=tt.type_document_id " +
				  "INNER JOIN tb_code_type tc ON tt.type_document_id = tc.code_type_id " +
				  "AND td.state_document <> 5 " +
			      "AND su.user_id= ?1 " +
				  "ORDER BY td.document_id ASC ";

			    Query q = em.createNativeQuery(qry);
				q.setParameter(1, userId);
				List<Object> result= (List<Object>)q.getResultList();
				for(int i=0;i<result.size();i++){
					Object[] obj=(Object[]) result.get(i);
				
					AllInfoVerification doc = new AllInfoVerification();
					if(doc!=null){	
						doc.setDocumentIdU(Long.parseLong(obj[0].toString()));
						doc.setUserIdU(Long.parseLong(obj[1].toString()));
						doc.setCodeTypeAbbrU(obj[2].toString());
						doc.setCodeUserU(Long.parseLong(obj[3].toString()));
						doc.setNameU(obj[4].toString());
						doc.setSecondNameU(obj[5].toString());
						doc.setTypeDocumentDescriptionU(obj[6].toString());
						doc.setStateU(Long.parseLong(obj[7].toString()));
						doc.setUrl(obj[8].toString());
						documents.add(doc);
					}
				}
		} catch (Exception e) {
			System.out.println(" [ Error en DocumentEJB.getDocumentbyClient. ] ");
			e.printStackTrace();
		}
	   return documents;
	}

	
	@SuppressWarnings("unused")
	@Override
	public boolean insertDocument(Long userId, Long documentTypeId, Long documentState,
			String string, String uploadedFileName, String ip, Long creationUser) {
		TbDocument doc = new TbDocument();
        TbSystemUser systemUser   = em.find(TbSystemUser.class, userId);
	  try {
			if (systemUser.getUserState().getUserStateId().longValue()==-1L) {
				Query updUser = em.createNativeQuery("UPDATE tb_system_user SET user_state = 4 WHERE user_id = ?1 ");	
				updUser.setParameter(1, userId);
				updUser.executeUpdate();
			}
			doc.setTbSystemUser(systemUser);
			doc.setTbTypeDocument(em.find(TbTypeDocument.class, documentTypeId));
			doc.setStateDocument(documentState.intValue());
			doc.setUrlDocument(uploadedFileName);
			doc.setNameDocument(string);
			doc.setUploadDateDocument(new Timestamp(System.currentTimeMillis()));	
			doc.setUserIdUpload(creationUser);

			objectEM = new ObjectEM(em);
			if(objectEM.create(doc)){
				log.insertLog("Pendiente por validación documento | Se ha enviado el documento ID: " + doc.getDocumentId() + 
							  " Tipo Documento: " +doc.getTbTypeDocument().getTypeDocumentDescription() +".",
							  LogReference.DOCUMENT_VERIFICATION, LogAction.CREATE, ip, creationUser);
				
				//proceso administrador
		        TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, userId);
				Long idPTA;
					if(pt==null){
						idPTA=process.createProcessTrack(ProcessTrackType.CLIENT, userId, ip, creationUser);
					}else{
						idPTA=pt.getProcessTrackId();
					}
				Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.DOCUMENT, 
						  "El tipo de documento " + doc.getTbTypeDocument().getTypeDocumentDescription() +" con nombre " +
						  doc.getNameDocument() + " está pendiente por aprobación por parte del sistema FacilPass.", creationUser, ip, 1, "", null,null,null,null);
			
				//proceso cliente
				TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, userId);
				Long idPTC;
					if(ptc==null){
						idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, userId, ip, userId);
					}else{
						idPTC=ptc.getProcessTrackId();
					}
				Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.DOCUMENT, 
						"El tipo de documento " + doc.getTbTypeDocument().getTypeDocumentDescription() +" con nombre " +
						 doc.getNameDocument() + " está pendiente por aprobación por parte del sistema FacilPass.", creationUser, ip, 1, "", null,null,null,null);
			}
			
			outbox.insertMessageOutbox(systemUser.getUserId(), 
	                  EmailProcess.DOCUMENT_SEND,
	                  null,
	                  null,
	                  null, 
	                  null,
	                  null,
	                  null,
	                  null,
	                  doc.getDocumentId(),
	                  creationUser,
		              null,
		              null,
		              null,
	                  true,
				       null);
			return true;
		} catch (NoResultException e) {			
		} catch (Exception e) {
			System.out.println(" [ Error en DocumentEJB.insertDocument. ] ");
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Long insertDocumentDisk(long userId, Long documentTypeId, 
			Long documentState, String uploadedFileName) {
		try{
			TbDocument document = new TbDocument();
			TbSystemUser user   = em.find(TbSystemUser.class, userId);
			TbTypeDocument typeDocument = em.find(TbTypeDocument.class, documentTypeId);

			document.setTbSystemUser(user);
			document.setTbTypeDocument(typeDocument);
			document.setStateDocument(documentState.intValue());
			document.setUrlDocument(uploadedFileName);
			document.setUploadDateDocument(new Timestamp(System.currentTimeMillis()));	
			em.persist(document);
        	em.flush();
			return document.getDocumentId();
		} catch (Exception e) {
			System.out.println(" [ Error en DocumentEJB.insertDocumentDisk. ] ");
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	public boolean getValidateSearchDocument(List<AllInfoVerification> listfilter, String documentState, String ip, Long creationUser) {
		int rolExist = 0;
	    BigDecimal rol = null;
		try {
			for(AllInfoVerification dv: listfilter){
				Query user_Id = 
		        	 em.createNativeQuery("SELECT ts.user_id, td.document_id, td.type_document_id, ts.user_state " +
		        	 		              "FROM tb_document td " +
					        	 		  "INNER JOIN tb_system_user ts on ts.user_id=td.user_id " +
					        	 		  "WHERE td.state_document=4 " +
					        	 		  "AND td.user_id= ?1 " +
					        	 		  "AND td.document_id= ?2 " +
					        	 		  "AND td.type_document_id= ?3 " +
					        	 		  "ORDER BY 3 ASC ");
				user_Id.setParameter(1, dv.getUserIdU());
				user_Id.setParameter(2, dv.getDocumentIdU());
				user_Id.setParameter(3, dv.getTypeDocumentIdU());
				
				List<Object []> userId = (List<Object []>)user_Id.getResultList();
				  if(userId != null){
					  for (Object [] userIdS : userId){				 
						 Long usuario= Long.parseLong(userIdS[0].toString());
						 Long documentId= Long.parseLong(userIdS[1].toString());
						 String typeDocumentId= userIdS[2].toString();
						 Long userState = Long.parseLong(userIdS[3].toString());
						 System.out.println(" usuario: "+usuario +"; documentId: "+documentId +"; typeDocumentId: "+typeDocumentId+"; userState: "+userState);
						 if (documentState.equals("3") && typeDocumentId.equals("1") && (userState.longValue()==4L || userState.longValue()==-1L)) {	
							Query query = 
								em.createNativeQuery("SELECT user_role_id FROM re_user_role WHERE user_id=?1 ");
							query.setParameter(1, usuario);
							for (Object obj : query.getResultList()) {
								rol = (BigDecimal) obj;
								if(rol.longValue()==3L){
									rolExist = 1;
			     					break;
								}
								Query updUser = em.createNativeQuery("UPDATE tb_system_user SET user_state = 3 WHERE user_id = ?1 ");	
								updUser.setParameter(1, usuario);
								updUser.executeUpdate();
							}
							
							if(rolExist == 0){
								ReUserRole ru = em.find(ReUserRole.class, rol.longValue());
								if(!(ru.getTbRole().getRoleId()== 3L)){						
									TbRole r = em.find(TbRole.class, 3L);
									ru.setTbRole(r);
									em.merge(ru);
									em.flush();
								}	
							}
							
						TbDocument td = em.find(TbDocument.class, documentId);
						td.setStateDocument(3);
						td.setProcessDateDocument(new Timestamp(System.currentTimeMillis()));	
						
			            objectEM = new ObjectEM(em);	
						if(objectEM.update(td)){
							log.insertLog("Validar Documentos | Se ha validado el documento: " + td.getDocumentId()+ 
							              " Documento " + td.getTbTypeDocument().getTypeDocumentDescription() + " fecha" + td.getProcessDateDocument(), 
							              LogReference.DOCUMENT_VERIFICATION, LogAction.CREATE, ip, creationUser);
							//proceso administrador
					        TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, usuario);
							Long idPTA;
								if(pt==null){
									idPTA=process.createProcessTrack(ProcessTrackType.CLIENT, creationUser, ip, creationUser);
								}else{
									idPTA=pt.getProcessTrackId();
								}
			
								Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.MY_CLIENT_DOCUMENT_VERIFICATION,  
										"El documento tipo "+td.getTbTypeDocument().getTypeDocumentDescription()+ " con nombre "+td.getNameDocument()+" ha sido aprobado.", creationUser, ip, 1, "", null,null,null,null);
								
								//proceso cliente
							TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, usuario);
							Long idPTC;
								if(ptc==null){
									idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, creationUser, ip, creationUser);
								}else{
									idPTC=ptc.getProcessTrackId();
								}
		
								Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.MY_CLIENT_DOCUMENT_VERIFICATION,  
										"El documento tipo "+td.getTbTypeDocument().getTypeDocumentDescription()+ " con nombre "+td.getNameDocument()+" ha sido aprobado.", creationUser, ip, 1, "", null,null,null,null);
						
								outbox.insertMessageOutbox(usuario,
								    	   EmailProcess.DOCUMENT_VALIDATION_APPROVED,
						                   null,
						                   null,
						                   null, 
						                   null,
						                   null,
						                   null,
						                   null,
						                   td.getDocumentId(),
						                   creationUser,
							               null,
							               null,
							               null,
						                   true,
									       null);	
						    }
						}else if (documentState.equals("-1") && (typeDocumentId.equals("1") ||
								typeDocumentId.equals("2") || typeDocumentId.equals("3")) && userState.longValue()==4L) {
								System.out.println(" [ pase por aqui---1909---->] "+documentId);
								Query updUser = em.createNativeQuery("UPDATE tb_system_user SET user_state = -1 WHERE user_id = ?1 ");	
								updUser.setParameter(1, usuario);
								updUser.executeUpdate();	
										
								TbDocument td = em.find(TbDocument.class, documentId);
					            td.setStateDocument(-1);
								td.setProcessDateDocument(new Timestamp(System.currentTimeMillis()));	
									
								objectEM = new ObjectEM(em);	
								if(objectEM.update(td)){
								log.insertLog("Rechazar Documentos | Se ha rechazado el documento ID: " + td.getDocumentId()+ 
						              	  " Documento: " + td.getTbTypeDocument().getTypeDocumentDescription() + " Fecha: " + td.getUploadDateDocument(), 
						                  LogReference.DOCUMENT_VERIFICATION, LogAction.CREATE, ip, creationUser);
							
								//proceso administrador
						        TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, usuario);
								Long idPTA;
									if(pt==null){
										idPTA=process.createProcessTrack(ProcessTrackType.CLIENT, creationUser, ip, creationUser);
									}else{
										idPTA=pt.getProcessTrackId();
									}
									Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.MY_CLIENT_DOCUMENT_VERIFICATION,  
											"El documento tipo "+td.getTbTypeDocument().getTypeDocumentDescription()+ " con nombre "+td.getNameDocument()+" ha sido rechazado.", creationUser, ip, 1, "", null,null,null,null);
									
								//proceso cliente
						        TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, usuario);
								Long idPTC;
									if(ptc==null){
										idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, creationUser, ip, creationUser);
									}else{
										idPTC=ptc.getProcessTrackId();
									}
									Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.MY_CLIENT_DOCUMENT_VERIFICATION,  
											"El documento tipo "+td.getTbTypeDocument().getTypeDocumentDescription()+ " con nombre "+td.getNameDocument()+" ha sido rechazado.", creationUser, ip, 1, "", null,null,null,null);
								
									outbox.insertMessageOutbox(usuario,
									    	   EmailProcess.DOCUMENT_VALIDATION_REJECTED,
							                   null,
							                   null,
							                   null, 
							                   null,
							                   null,
							                   null,
							                   null,
							                   td.getDocumentId(),
							                   creationUser,
								               null,
								               null,
								               null,
							                   true,
										       null);
							  }
						}else if (documentState.equals("3") && (typeDocumentId.equals("2") || typeDocumentId.equals("3")) && userState.longValue()==4L) {	
							System.out.println(" [ pase por aqui---1964---->] "+documentId);
							TbDocument td = em.find(TbDocument.class, documentId);
							td.setStateDocument(3);
							td.setProcessDateDocument(new Timestamp(System.currentTimeMillis()));
							
							Query updUser = em.createNativeQuery("UPDATE tb_system_user SET user_state = -1 WHERE user_id = ?1 ");	
							updUser.setParameter(1, usuario);
							updUser.executeUpdate();
							
				            objectEM = new ObjectEM(em);	
							if(objectEM.update(td)){
							log.insertLog("Validar Documentos | Se ha validado el documento ID: " + td.getDocumentId()+ 
							              " Documento " + td.getTbTypeDocument().getTypeDocumentDescription() + " fecha" + td.getProcessDateDocument(), 
							              LogReference.DOCUMENT_VERIFICATION, LogAction.CREATE, ip, creationUser);
							  
							//proceso administrador
					        TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, usuario);
							Long idPTA;
								if(pt==null){
									idPTA=process.createProcessTrack(ProcessTrackType.CLIENT, creationUser, ip, creationUser);
								}else{
									idPTA=pt.getProcessTrackId();
								}
								Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.MY_CLIENT_DOCUMENT_VERIFICATION,  
										"El documento tipo "+td.getTbTypeDocument().getTypeDocumentDescription()+ " con nombre "+td.getNameDocument()+" ha sido aprobado.", creationUser, ip, 1, "", null,null,null,null);
								
							//proceso cliente
							TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,usuario);
							Long idPTC;
								if(ptc==null){
									idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, creationUser, ip, creationUser);
								}else{
									idPTC=ptc.getProcessTrackId();
								}
								Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.MY_CLIENT_DOCUMENT_VERIFICATION,  
										"El documento tipo "+td.getTbTypeDocument().getTypeDocumentDescription()+ " con nombre "+td.getNameDocument()+" ha sido aprobado.", creationUser, ip, 1, "", null,null,null,null);
								
								outbox.insertMessageOutbox(usuario,
								    	   EmailProcess.DOCUMENT_VALIDATION_APPROVED,
						                   null,
						                   null,
						                   null, 
						                   null,
						                   null,
						                   null,
						                   null,
						                   td.getDocumentId(),
						                   creationUser,
							               null,
							               null,
							               null,
						                   true,
									       null);
						  }		
					}else if (documentState.equals("-1") && (typeDocumentId.equals("1") || typeDocumentId.equals("2") || typeDocumentId.equals("3"))) {	
			
						System.out.println(" [ pase por aqui---2019---->] "+documentId);
						TbDocument td = em.find(TbDocument.class, documentId);
						td.setStateDocument(-1);
						td.setProcessDateDocument(new Timestamp(System.currentTimeMillis()));	
						
			            objectEM = new ObjectEM(em);	
						if(objectEM.update(td)){
						log.insertLog("Validar Documentos | Se ha rechazado el documento ID: " + td.getDocumentId()+ 
						              " Documento " + td.getTbTypeDocument().getTypeDocumentDescription() + " fecha" + td.getProcessDateDocument(), 
						              LogReference.DOCUMENT_VERIFICATION, LogAction.CREATE, ip, creationUser);
						  
						//proceso administrador
				        TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, usuario);
						Long idPTA;
							if(pt==null){
								idPTA=process.createProcessTrack(ProcessTrackType.CLIENT, creationUser, ip, creationUser);
							}else{
								idPTA=pt.getProcessTrackId();
							}
					 		Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.MY_CLIENT_DOCUMENT_VERIFICATION,  
									"El documento tipo "+td.getTbTypeDocument().getTypeDocumentDescription()+ " con nombre "+td.getNameDocument()+" ha sido rechazado.", creationUser, ip, 1, "", null,null,null,null);
							
							//proceso cliente
							TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, usuario);
							Long idPTC;
							if(ptc==null){
								idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, creationUser, ip, creationUser);
							}else{
								idPTC=ptc.getProcessTrackId();
							}
							Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.MY_CLIENT_DOCUMENT_VERIFICATION,  
								  "El documento tipo "+td.getTbTypeDocument().getTypeDocumentDescription()+ " con nombre "+td.getNameDocument()+" ha sido rechazado.", creationUser, ip, 1, "", null,null,null,null);
							
							outbox.insertMessageOutbox(usuario,
							    	   EmailProcess.DOCUMENT_VALIDATION_REJECTED,
					                   null,
					                   null,
					                   null, 
					                   null,
					                   null,
					                   null,
					                   null,
					                   td.getDocumentId(),
					                   creationUser,
						               null,
						               null,
						               null,
					                   true,
								       null);
						    } 
					     }else if (documentState.equals("3") && (typeDocumentId.equals("1") || typeDocumentId.equals("2") || typeDocumentId.equals("3"))) {	
								System.out.println(" [ pase por aqui---2069---->] "+documentId);
								TbDocument td = em.find(TbDocument.class, documentId);
								td.setStateDocument(3);
								td.setProcessDateDocument(new Timestamp(System.currentTimeMillis()));	
								
					            objectEM = new ObjectEM(em);	
								if(objectEM.update(td)){
								log.insertLog("Validar Documentos | Se ha validado el documento ID: " + td.getDocumentId()+ 
								              " Documento " + td.getTbTypeDocument().getTypeDocumentDescription() + " fecha" + td.getProcessDateDocument(), 
								              LogReference.DOCUMENT_VERIFICATION, LogAction.CREATE, ip, creationUser);
								  
								//proceso administrador
						        TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, usuario);
								Long idPTA;
								if(pt==null){
									idPTA=process.createProcessTrack(ProcessTrackType.CLIENT, creationUser, ip, creationUser);
								}else{
									idPTA=pt.getProcessTrackId();
								}
						 		Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.MY_CLIENT_DOCUMENT_VERIFICATION,  
						 				"El documento tipo "+td.getTbTypeDocument().getTypeDocumentDescription()+ " con nombre "+td.getNameDocument()+" ha sido aprobado.", creationUser, ip, 1, "", null,null,null,null);
								
								//proceso cliente
								TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, usuario);
								Long idPTC;
									if(ptc==null){
										idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, creationUser, ip, creationUser);
									}else{
										idPTC=ptc.getProcessTrackId();
									}
									Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.MY_CLIENT_DOCUMENT_VERIFICATION,  
											"El documento tipo "+td.getTbTypeDocument().getTypeDocumentDescription()+ " con nombre "+td.getNameDocument()+" ha sido aprobado.", creationUser, ip, 1, "", null,null,null,null);
									
									outbox.insertMessageOutbox(usuario,
									    	   EmailProcess.DOCUMENT_VALIDATION_APPROVED,
							                   null,
							                   null,
							                   null, 
							                   null,
							                   null,
							                   null,
							                   null,
							                   td.getDocumentId(),
							                   creationUser,
								               null,
								               null,
								               null,
							                   true,
										       null);
							     } 
						 }
					  }
				 }
		    }
			return true;
	     } catch (Exception e) {
			System.out.println(" [ Error en DocumentEJB.getValidateSearchDocument ] ");
			e.printStackTrace();
			return false;
		}
	}

 }