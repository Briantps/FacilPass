package ejb;

import java.math.BigDecimal;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import util.TableProcess;
import util.TimeUtil;

import constant.EmailProcess;
import constant.LogAction;
import constant.LogReference;
import constant.ProcessTrackDetailType;
import constant.ProcessTrackType;
import crud.ObjectEM;
import ejb.email.Outbox;

import jpa.ReResponseTypeRecharge;
import jpa.ReUserRole;
import jpa.TbAccount;
import jpa.TbDevice;
import jpa.TbProcessTrack;
import jpa.TbProcessTrackDetail;
import jpa.TbProcessTrackDetailType;
import jpa.TbProcessTrackType;
import jpa.TbResponseType;
import jpa.TbRole;
import jpa.TbSystemUser;
import jpa.TbTask;
import jpa.TbUmbral;

/**
 * Session Bean implementation class ProcessEJB
 */
@Stateless(mappedName = "ejb/Process")
public class ProcessEJB implements Process {
	
	@PersistenceContext(unitName = "bo") 
	EntityManager em;
	
	@EJB(mappedName="ejb/Log")
	private Log log;
	
	private ObjectEM objectEM;
	
	@EJB(mappedName="ejb/Task")
	private Task task;
	
	@EJB(mappedName ="ejb/email/Outbox")
	private Outbox outbox;
	
	@EJB(mappedName="ejb/User")
	private User userEJB;

    /**
     * Default constructor. 
     */
    public ProcessEJB() {
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Process#createProcessTrack(constant.ProcessTrackType,
	 * java.lang.Long, java.lang.String, java.lang.Long)
	 */
    @Override
	public Long createProcessTrack(ProcessTrackType processTrackTypeId, Long id, String ip, Long creationUser) {
	    try {
	    	TbProcessTrackType ptt = em.find(TbProcessTrackType.class, processTrackTypeId.getId());
	    	
			TbProcessTrack process = new TbProcessTrack();
			process.setProcessTrackCreationDate(new Timestamp(System.currentTimeMillis()));
			process.setProcessTrackState(0);
			process.setTbProcessTrackType(ptt);
			process.setProcessTrackReferencedId(id);
			
			objectEM = new ObjectEM(em);	
			if(objectEM.create(process)){
				log.insertLog("Crear Proceso | Se ha creado el proceso para el Tipo: " + ptt.getProcessTrackTypeName() + " e ID: " + id + ".",
						LogReference.PROCESS, LogAction.CREATE, ip, creationUser);
				return process.getProcessTrackId();
			} else {
				log.insertLog("Crear Proceso | No se ha podido crear el proceso para el Tipo: " + ptt.getProcessTrackTypeName() + " e ID: " + id + ".",
						LogReference.PROCESS, LogAction.CREATEFAIL, ip, creationUser);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en ProcessEJB.createProcessTrack ] ");
			e.printStackTrace();
		}
		return null;
	}
   
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Process#createProcessDetail(java.lang.Long,
	 * constant.ProcessTrackDetailType, java.lang.String, java.lang.Long,
	 * java.lang.String, java.lang.Integer, java.lang.String)
	 */
	@Override
	public Long createProcessDetail(Long processTrackId, ProcessTrackDetailType detailType,
			String message, Long creationUser, String ip, Integer state, String messageError,int procesError,
			String nameFileRq,String nameFileRp,String filePse,Long pseId,Long typeID) {
		try {
			Long typeUser=0L;
			if (creationUser!=null){
				typeUser = (userEJB.getReTypeRole(creationUser));
			}
			
			
			
			TbProcessTrackDetail detail = new TbProcessTrackDetail();
			detail.setTbProcessTrack(em.find(TbProcessTrack.class, processTrackId));
			detail.setProcessTrackDetailDate(new Timestamp(System.currentTimeMillis()));
			detail.setProcessTrackDetailMessage(message);
			detail.setProcessTrackDetailError(Long.valueOf(procesError));
			detail.setProcessTrackDetailNameFileRq(nameFileRq);
			detail.setProcessTrackDetailNameFileRp(nameFileRp);
			detail.setTransactionId(pseId);
			detail.setProcessTrackTransPse(filePse);
			detail.setTbProcessTrackDetailType(em.find(TbProcessTrackDetailType.class, detailType.getId()));
			detail.setProcessTrackDetailState(state);
			detail.setTypeNoteId(typeID);
			
			/*
			 * @autor ablasquez
			 */
			if(detailType.getId()==ProcessTrackDetailType.RESET_PASSWORD.getId()){
				detail.setVerProcesoComo(1L); //Permite ver en el modulo del cliente la frace "Administrador FacilPass"
			}
			if(detailType.getId()==ProcessTrackDetailType.VEHICLES_ADMIN.getId()){
				detail.setVerProcesoComo(1L); //Permite ver en el modulo del cliente la frace "Administrador FacilPass"
			}
			if (typeUser == 1L) {
				if(detailType.getId()==ProcessTrackDetailType.UPDATE_LOW_BALANCE.getId()){
					detail.setVerProcesoComo(1L); //Permite ver en el modulo del cliente la frace "Administrador FacilPass"
				}
			}			
			if(detailType.getId()==ProcessTrackDetailType.BANK_ACCOUNT.getId()){
				System.out.println("detail de processEjb");
				detail.setVerProcesoComo(1L); //Permite ver en el modulo del cliente la frace "Administrador FacilPass"
			}
			if(detailType.getId()==ProcessTrackDetailType.UPDATE_MINIMUM_BALANCE.getId()){
				System.out.println("detail de processEjb");
				detail.setVerProcesoComo(1L); //Permite ver en el modulo del cliente la frace "Administrador FacilPass"
			}
			//AND 030 Asociación Cuenta PSE -psanchez
			if(detailType.getId()==ProcessTrackDetailType.CLIENT_ACCOUNT_ASSOCIATION.getId() && this.getRoleId(creationUser)==true){
				System.out.println("detail de processEjb");
				detail.setVerProcesoComo(3L); //Permite ver en el modulo del cliente la frase "ADMIN FACILPASS"
			}

			if(creationUser != null){
				detail.setTbSystemUser(em.find(TbSystemUser.class, creationUser));
			}
			
			objectEM = new ObjectEM(em);	
			if(objectEM.create(detail)){
				Long detailL = detail.getProcessTrackDetailId();
				log.insertLog("Se ha creado el detalle del proceso ID: " + detailL, LogReference.PROCESSDETAIL, LogAction.CREATE,
						 ip, creationUser);
				return detailL ;
			} else {
				log.insertLog(messageError, LogReference.PROCESSDETAIL, LogAction.CREATEFAIL, ip, creationUser);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error en ProcessEJB.createProcessTrackDetail ] ");
		}
		return null;
	}
	
	@Override
	public Long createProcessDetail(Long processTrackId, ProcessTrackDetailType detailType,
			String message, Long creationUser, String ip, Integer state, String messageError, Long typeId,Integer processTrack,String rQ,String rP) {
		try {
			Long typeUser=0L;
			if (creationUser!=null){
				typeUser = (userEJB.getReTypeRole(creationUser));
			}
			
			TbProcessTrackDetail detail = new TbProcessTrackDetail();
			detail.setTbProcessTrack(em.find(TbProcessTrack.class, processTrackId));
			detail.setProcessTrackDetailDate(new Timestamp(System.currentTimeMillis()));
			detail.setProcessTrackDetailMessage(message);
			detail.setTbProcessTrackDetailType(em.find(TbProcessTrackDetailType.class, detailType.getId()));
			detail.setProcessTrackDetailState(state);
			detail.setTypeNoteId(typeId);
			
			/*
			 * @autor ablasquez
			 */
			if(detailType.getId()==ProcessTrackDetailType.RESET_PASSWORD.getId()){
				detail.setVerProcesoComo(1L); //Permite ver en el modulo del cliente la frace "Administrador FacilPass"
			}
			
			if(detailType.getId()==ProcessTrackDetailType.VEHICLES_ADMIN.getId()){
				detail.setVerProcesoComo(1L); //Permite ver en el modulo del cliente la frace "Administrador FacilPass"
			}
			if (typeUser == 1L) {
				if(detailType.getId()==ProcessTrackDetailType.UPDATE_LOW_BALANCE.getId()){
					detail.setVerProcesoComo(1L); //Permite ver en el modulo del cliente la frace "Administrador FacilPass"
				}
			}
			if(detailType.getId()==ProcessTrackDetailType.BANK_ACCOUNT.getId()){
				System.out.println("detail de processEjb");
				detail.setVerProcesoComo(1L); //Permite ver en el modulo del cliente la frace "Administrador FacilPass"
			}
			if(detailType.getId()==ProcessTrackDetailType.UPDATE_MINIMUM_BALANCE.getId()){
				System.out.println("detail de processEjb");
				detail.setVerProcesoComo(1L); //Permite ver en el modulo del cliente la frace "Administrador FacilPass"
			}
			//AND 030 Asociación Cuenta PSE -psanchez
			if(detailType.getId()==ProcessTrackDetailType.CLIENT_ACCOUNT_ASSOCIATION.getId() && this.getRoleId(creationUser)==true){
				System.out.println("detail de processEjb");
				detail.setVerProcesoComo(3L); //Permite ver en el modulo del cliente la frase "ADMIN FACILPASS"
			}
			if(creationUser != null){
				detail.setTbSystemUser(em.find(TbSystemUser.class, creationUser));
			}
			
			objectEM = new ObjectEM(em);	
			if(objectEM.create(detail)){
				Long detailL = detail.getProcessTrackDetailId();
				log.insertLog("Se ha creado el detalle del proceso ID: " + detailL, LogReference.PROCESSDETAIL, LogAction.CREATE,
						 ip, creationUser);
				return detailL ;
			} else {
				log.insertLog(messageError, LogReference.PROCESSDETAIL, LogAction.CREATEFAIL, ip, creationUser);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error en ProcessEJB.createProcessTrackDetail ] ");
		}
		return null;
	}

	
	/*
	 * (non-Javadoc)
	 * @see ejb.Process#getPotentialClients()
	 */
	@Override
	public List<TbSystemUser> getPotentialClients() {
		List<TbSystemUser> list = new ArrayList<TbSystemUser>();
		try {
			String query = "SELECT ptr.process_track_referenced_id " +
									 "FROM "+
									    "tb_process_track ptr "+ 
									    "INNER JOIN tb_process_track_detail pd ON pd.process_track_id = ptr.process_track_id "+ 
									"WHERE  "+
									    "ptr.process_track_state = 0 "+ 
									    "AND ptr.process_track_type_id = ?1 "+
									    "AND pd.process_track_detail_type_id = ?2 "+
									    "AND pd.process_track_detail_state = 0 ";
			Query q = em.createNativeQuery(query);
			q.setParameter(1, ProcessTrackType.CLIENT.getId());
			q.setParameter(2, ProcessTrackDetailType.CLIENT_CREATION.getId());
			
			for(Object obj : q.getResultList()){
				list.add(em.find(TbSystemUser.class, Long.valueOf(obj.toString())));
			}
		} catch (Exception e) {
			System.out.println(" [ Error en ProcessEJB.getPotentialClients ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Process#affiliationDocsCheck(java.lang.Long, java.lang.Long,
	 * java.lang.String)
	 */
	@Override
	public boolean affiliationDocsCheck(Long id, Long creationUser, String ip) {
		try {
			String query = "SELECT pd.process_track_detail_id " +
									 "FROM " +
									    "tb_process_track ptr " + 
									    "INNER JOIN tb_process_track_detail pd ON pd.process_track_id = ptr.process_track_id " + 
									 "WHERE  " +
									    "ptr.process_track_state = 0 " +  
                                       "AND ptr.process_track_type_id = ?1 " +
									   "AND pd.process_track_detail_type_id = ?2 " +
									   "AND pd.process_track_detail_state = 0 " +
									   "AND ptr.process_track_referenced_id = ?3 ";
			
			Query q =  em.createNativeQuery(query);
			q.setParameter(1, ProcessTrackType.CLIENT.getId());
			q.setParameter(2, ProcessTrackDetailType.CLIENT_CREATION.getId());
			q.setParameter(3, id);
			
			TbProcessTrackDetail pt = em.find(TbProcessTrackDetail.class, Long.parseLong(q
					.getSingleResult().toString()) ) ;
			
			//Having the process track detail we proceed to closed it.
			pt.setProcessTrackDetailState(1);// closing the detail
			em.merge(pt);
			em.flush();
			
			TbProcessTrack ptc = searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, pt.getTbProcessTrack()
							.getProcessTrackReferencedId());
				
			//The process continue indicating the reception of client affiliation documents. 
			Long detail = this.createProcessDetail(pt.getTbProcessTrack().getProcessTrackId(), 
					ProcessTrackDetailType.CLIENT_DOCUMENT_RECEPTION,
					"Se recibieron los documentos de Afiliación", creationUser, ip, 0,
					"No se pudo indicar en el proceso que los documentos del cliente fueron recibidos. ID. Proceso :"
					+ pt.getTbProcessTrack().getProcessTrackId(),null,null,null,null); 
			
			this.createProcessDetail(ptc.getProcessTrackId(), 
					ProcessTrackDetailType.MY_CLIENT_DOCUMENT_RECEPTION,
					"Documentos de Afiliación registrados en el sistema como recibidos.", creationUser, ip, 1,
					"No se pudo indicar en el proceso que los documentos fueron recibidos. ID. Proceso :"
					+ ptc.getProcessTrackId(),null,null,null,null); 
			
			if (detail != null) {
				TbProcessTrackDetailType dt = em.find(TbProcessTrackDetailType.class,
						ProcessTrackDetailType.CLIENT_DOCUMENT_RECEPTION.getId());
				
				// Searching the client.
				TbSystemUser su = em.find(TbSystemUser.class, pt.getTbProcessTrack().getProcessTrackReferencedId());
				
				String name = su.getUserNames();
				
				if(su.getTbCodeType().getCodeTypeId().longValue() != 3L) {
					name += " " + su.getUserSecondNames();
				}
				
				// Creating the new Task
				task.createTask(detail, 0, new Timestamp(System.currentTimeMillis()),
						TimeUtil.calculateAds(dt.getAdsTime()), dt.getDetailTypePriority(),
						"Se recibieron los documentos de Afiliación de : " + name + 
						". Pendiente Verificación de Documentos.",
						dt.getTbTaskType().getTaskTypeId(), creationUser, ip, null);
				
				return true;
			}
		}catch (NoResultException ne) {
			System.out
					.println(" [ ProcessEJB.affiliationDocsCheck: No se encontró el registro para el usuario ID: "
							+ id + " ]");
		} catch (Exception e) {
			System.out.println(" [ Error en ProcessEJB.affiliationDocsCheck ] ");
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Process#getClientsWithActiveProcess()
	 */
	@Override
	public List<TbSystemUser> getClientsWithActiveProcess() {
		List<TbSystemUser> list = new ArrayList<TbSystemUser>();
		try {
			Query q = em
					.createNativeQuery("SELECT  pt.process_track_referenced_id FROM  tb_process_track pt "
							+ "WHERE pt.process_track_type_id = ?1 AND pt.process_track_state = 0");
			q.setParameter(1, ProcessTrackType.CLIENT.getId());
			for (Object obj : q.getResultList()) {
				list.add(em.find(TbSystemUser.class, Long.parseLong(obj
						.toString())));
			}
		} catch (Exception e) {
			System.out
					.println(" [ Error en ProcessEJB.getClientsWithActiveProcess ] ");
			e.printStackTrace();
		}
		return list;
	}

	
	/*
	 * (non-Javadoc)
	 * @see ejb.Process#getClientByProcessDetailType()
	 */
	@Override
	public List<TbSystemUser> getClientByProcessDetailType(ProcessTrackDetailType type) {
		List<TbSystemUser> list = new ArrayList<TbSystemUser>();
		try {
			Query q = em
					.createNativeQuery("SELECT  pt.process_track_referenced_id FROM tb_process_track_detail pd "
							+ "INNER JOIN tb_process_track pt ON pt.process_track_id = pd.process_track_id  "
							+ "WHERE pd.process_track_detail_type_id = ?1 "
							+ "AND pd.process_track_detail_state = 0 "
							+ "AND pt.process_track_type_id = ?2 "
							+ "AND pt.process_track_state = 0 ");
			q.setParameter(1, type.getId());
			q.setParameter(2, ProcessTrackType.CLIENT.getId());
			for (Object obj : q.getResultList()) {
				list.add(em.find(TbSystemUser.class, Long.parseLong(obj
						.toString())));
			}
		} catch (Exception e) {
			System.out.println(" [ Error en ProcessEJB.getClientByProcessDetailType ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Process#saveVerification(java.lang.Long, boolean,
	 * java.lang.String, java.lang.Long, java.lang.String)
	 */
	@Override
	public boolean saveVerification(Long idClient, boolean isVerificationOk,
			String observation, Long creationUser, String ip) {
		    int rolExist = 0;
		    BigDecimal rol = null;
		try {
//			TbProcessTrackDetail pd = getDetail(idClient,
//					ProcessTrackDetailType.CLIENT_DOCUMENT_RECEPTION, 0);
			
			TbProcessTrackDetail pd = getDetail(idClient,
					ProcessTrackDetailType.CLIENT_CREATION, 0);
			
			TbSystemUser su = em.find(TbSystemUser.class, pd.getTbProcessTrack().getProcessTrackReferencedId());
			
			TbProcessTrack ptClient = searchProcess(
					ProcessTrackType.MY_CLIENT_PROCESS, pd.getTbProcessTrack()
							.getProcessTrackReferencedId());
			
			if (pd != null) {
				if (isVerificationOk) {
					// Closing the detail (documents received and OK)
					pd.setProcessTrackDetailState(1); // Closed
					em.merge(pd);
					em.flush();
					
					System.out.println("usuario: "+su.getUserId());
					Query q = em
					.createNativeQuery("select user_role_id from re_user_role where user_id=?1");
					q.setParameter(1, su.getUserId());
					for (Object obj : q.getResultList()) {
						 rol = (BigDecimal) obj;
						System.out.println("role: "+rol.longValue());
						if(rol.longValue()==3L){
							rolExist = 1;
							break;
						}
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

					su.setUserState(userEJB.getState(3)); //Se cambia el estado del usuario de inactivo a validado

					em.merge(su);
					em.flush();
					
					// Creating the detail indicating that the verification was made.
					Long detail = createProcessDetail(pd.getTbProcessTrack().getProcessTrackId(), ProcessTrackDetailType.CLIENT_DOCUMENT_VERIFICATION,
							"Se realizó la verificación de Documentos: Satisfactoria.",
							creationUser, ip, 1, "No se pudo indicar en el proceso que la Verificación fue Ok. ID. Proceso :"
							+ pd.getTbProcessTrack().getProcessTrackId(),null,null,null,null);
					
					createProcessDetail(ptClient.getProcessTrackId(), ProcessTrackDetailType.MY_CLIENT_DOCUMENT_VERIFICATION,
							"Se realizó la verificación de Documentos: Satisfactoria.",
							creationUser, ip, 1, "No se pudo indicar en el proceso que la Verificación fue Ok. ID. Proceso :"
							+ ptClient.getProcessTrackId(),null,null,null,null);
					
					if (detail != null) {
										
						/*sendMail.sendMail(EmailType.CLIENT, su.getUserEmail(),
								EmailSubject.CLIENT_VERIFICATION, "Estimado Usuario, \r\n \r\nSe han aprobado los documentos satisfactoriamente enviados. Para activar y poder hacer uso del producto FacilPass por favor ingrese a www.facilpass.com con su usario y contraseña y continue con los pasos requeridos. \r\n \r\nCordialmente, \r\nFacilPass");*/
						outbox.insertMessageOutbox(su.getUserId(), 
				                   EmailProcess.DOCUMENT_VALIDATION_APPROVED,
				                   null,
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
						
						return true;
					} 
				} else {
					su.setUserState(userEJB.getState(-1)); //Se cambia el estado del usuario de inactivo a no validado

					em.merge(su);
					em.flush();
					// Create a detail indicating that the verification was made but has discrepancies.
					String msg = "Se realizó la verificación de Documentos: Rechazada por " + observation;
					
					
					String clien = ".  Cliente Verificado: " + su.getUserNames();
					
					if(su.getTbCodeType().getCodeTypeId().longValue() != 3L) {
						clien += " " + su.getUserSecondNames();
					}
					
					Long detail = createProcessDetail(
							pd.getTbProcessTrack().getProcessTrackId(), ProcessTrackDetailType.CLIENT_DOCUMENT_VERIFICATION_FAIL,
							msg, creationUser, ip, 1, "No se pudo indicar en el proceso que la Verificación No fue Satisfactoria. ID. Proceso :"
							+ pd.getTbProcessTrack().getProcessTrackId()+".",null,null,null,null);
					
					createProcessDetail(
							ptClient.getProcessTrackId(), ProcessTrackDetailType.MY_CLIENT_DOCUMENT_VERIFICATION_FAIL,
							msg, creationUser, ip, 1, "No se pudo indicar en el proceso que la Verificación No fue Satisfactoria. ID. Proceso :"
							+ ptClient.getProcessTrackId()+".",null,null,null,null);
					
					if (detail != null) {
						
						TbTask task = em.find(TbTask.class, pd.getProcessTrackDetailId());
						task.setTaskState(2);
						task.setTaskData(msg + clien);
						task.setTaskActive(false);
						em.merge(task);
						em.flush();
						
						/*sendMail.sendMail(EmailType.CLIENT, su.getUserEmail(),
								EmailSubject.CLIENT_VERIFICATION_FAIL, "Estimado Usuario, \r\n \r\nLos documentos enviados han sido rechazados. Por favor comuniquese con la linea 018000xxxxxxx para conocer el detalle del rechazo y las recomendaciones para volver a enviar los documentos y continuar con el proceso de activación. \r\n \r\nCordialmente, \r\nFacilPass");*/
						
						outbox.insertMessageOutbox(su.getUserId(), 
				                   EmailProcess.DOCUMENT_VALIDATION_REJECTED,
				                   null,
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
						
						return true;
					} 
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error en ProcessEJB.saveVerification ] ");
		}
		return false;
	}
	
	/**
	 * 
	 * @param idClient
	 * @param type
	 * @param state
	 * @return
	 */
	private TbProcessTrackDetail getDetail(Long idClient, ProcessTrackDetailType type, Integer state){
		TbProcessTrackDetail detail = null;
		try {
			String query = "SELECT pd.process_track_detail_id " +
									 "FROM " +
									    "tb_process_track ptr " + 
									    "INNER JOIN tb_process_track_detail pd ON pd.process_track_id = ptr.process_track_id " + 
									 "WHERE  " +
									   "ptr.process_track_state = 0 " +  
									   "AND ptr.process_track_referenced_id = ?1 "+
									   "AND pd.process_track_detail_type_id = ?2 " +
									   "AND pd.process_track_detail_state = ?3 ";
			Query q =  em.createNativeQuery(query).setParameter(1, idClient).setParameter(2, type.getId()).setParameter(3, state);
			detail = em.find(TbProcessTrackDetail.class, Long.parseLong(q.getSingleResult().toString()) ) ;
		} catch (Exception e) {
			System.out.println(" [ Error en ProcessEJB.getDetail ] ");
			e.printStackTrace();
		}
		return detail;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Process#getDevicesWithProcessByType(java.lang.Long)
	 */
	@Override
	public List<TbDevice> getDevicesWithProcessByType(Long type) {
		List<TbDevice> list = new ArrayList<TbDevice>();
		try {
			String query = "SELECT pt.process_track_referenced_id " +
			"FROM Tb_Process_Track pt " +
			"INNER JOIN Tb_Device td ON td.device_id = pt.process_track_referenced_id "+
			"WHERE pt.process_Track_Type_id = ?1 " +
			"AND pt.process_track_state = 0 ";
			
			if(type == -1L) {
				query += "AND td.device_type_id  IS NULL ";
			} else {
				query += "AND td.device_type_id  = ?2";
			}
			
			Query q = em.createNativeQuery(query);
			q.setParameter(1, ProcessTrackType.DEVICE.getId());
			
			if(type != -1L) {
				q.setParameter(2, type);
			} 
			
			for (Object obj : q.getResultList()) {
				list.add(em
						.find(TbDevice.class, Long.parseLong(obj.toString())));
			}
		} catch (Exception e) {
			System.out.println(" [ Error en ProcessEJB.getDevicesWithProceessByType ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see ejb.Process#getCustomizationProcessDetailByPlate(java.lang.String)
	 */
	@Override
	public List<TbProcessTrackDetail> getCustomizationProcessDetailByPlate(
			String plate) {
		List<TbProcessTrackDetail> list = new ArrayList<TbProcessTrackDetail>();
		try {
			Query q = em.createQuery("SELECT pd " +
													  "FROM " +
													  		"TbProcessTrack pt, " +
													  		"TbProcessTrackDetail pd, " +
													  		"TbDeviceCustomization dc " +
													  "WHERE " +
													  		"pt.processTrackId = pd.tbProcessTrack.processTrackId " +
													  		"AND pt.tbProcessTrackType.processTrackTypeId = ?1 " +
													        "AND pt.processTrackReferencedId = dc.deviceCustomizationId " +
													        "AND dc.tbVehicle.vehiclePlate = ?2 " +
													  "ORDER BY pt.processTrackId, pd.processTrackDetailDate ");
			q.setParameter(1, ProcessTrackType.CUSTOMIZATION.getId());
			q.setParameter(2, plate.toUpperCase());
			for (Object obj : q.getResultList()) {
				list.add((TbProcessTrackDetail) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en ProcessEJB.getCustomizationProcessDetailByPlate ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Process#searchProcess(constant.ProcessTrackType, java.lang.Long)
	 */
	@Override
	public TbProcessTrack searchProcess(ProcessTrackType processType, Long referencedId) {
		try {
			System.out.println("referencedId en searchProcess: "+referencedId);
			System.out.println("processType en searchProcess: "+processType);
			
			Query q = em
					.createQuery("SELECT pt FROM TbProcessTrack pt WHERE pt.tbProcessTrackType.processTrackTypeId = ?1 "
							+ " AND pt.processTrackReferencedId = ?2 order by pt.processTrackCreationDate ");
			q.setParameter(1, processType.getId());
			q.setParameter(2, referencedId);

			TbProcessTrack pt = (TbProcessTrack) q.getResultList().get(0);
			return pt;
		} catch (NoResultException nre) {
			System.out
					.println(" [ Error en ProcessEJB.searchProcess: No se encontraron resultados para el Id "
							+ processType.getId() + " : "+ referencedId+ ". ] ");
		} catch (Exception e) {
			System.out.println(" [ Error en ProcessEJB.searchProcess ] ");
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Process#getProcessDetails(java.lang.Long,
	 * constant.ProcessTrackType)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TableProcess> getProcessDetailsClient(Long referencedId,
			ProcessTrackType processType,Date dateStart,Date dateEnd,int page, int rows) {
		List<TableProcess> list = new ArrayList<TableProcess>();
		String dateBetwen=null;
		DateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");
		long userId=userEJB.getSystemMasterById(referencedId);
		String qry1, qry2, qry3 = "";
		if(dateEnd==null||dateStart==null){
			dateBetwen=" ";	
			System.out.println("no se busca por el filtro");
			}else{
				System.out.println(" busca por el filtro");
				String fIni = fecha.format(dateStart);
				String fFin = fecha.format(dateEnd);
				dateBetwen=" and pd.PROCESS_TRACK_DETAIL_DATE BETWEEN to_timestamp('"+fIni+" 00:00:01','dd/mm/yyyy HH24:mi:ss')" +
                " and to_timestamp('"+fFin+" 23:59:59','dd/mm/yyyy HH24:mi:ss') ";			
		}
		
		try {
			  Query user = 
				 em.createNativeQuery("SELECT user_id FROM tb_system_user " +
				 					  "WHERE system_user_master_id=?1 or user_id=?1 ");
			    user.setParameter(1, userId);
			    List<?> user1 = (List<?>)user.getResultList();

				String usuarios = user1.toString();
				usuarios=usuarios.replace('[', '(').replace(']', ')');
				
				qry1= "SELECT COUNT(*) FROM (";
				
				qry2= "SELECT * FROM (" ;
				
				qry3= "SELECT a.*, rownum rnum FROM (" +		
				      "SELECT pd.process_track_detail_id " +
					  "FROM tb_process_track ptr " +
					  "INNER JOIN tb_process_track_detail pd ON pd.process_track_id = ptr.process_track_id " +
					  "WHERE ptr.process_track_referenced_id in  " + usuarios +
					  "AND (pd.type_id in(0,1,2) or pd.type_id is null or pd.type_id >2) " +
					  "AND ptr.process_track_type_id= "+processType.getId()+""+dateBetwen+""; 
				
				if(page!=0){
					qry3 = qry3+"ORDER BY pd.process_track_detail_date DESC) a WHERE rownum <= "+(page*rows)+") WHERE rnum > "+((page*rows)-rows)+" ";	
					Query query = em.createNativeQuery(qry2+qry3);
					System.out.println(" [ Error en ProcessEJB.getProcessDetails ] "+qry2+qry3);
					ArrayList<Object>lists=(ArrayList<Object>) query.getResultList();
					for (int i=0;i<lists.size();i++) {
						Object [] obj=(Object[])lists.get(i);
							TbProcessTrackDetail ttd = em.find(TbProcessTrackDetail.class, ((BigDecimal)obj[0]).longValue()); 
							TableProcess tp = new TableProcess();
							tp.setDetailProcess(ttd.getProcessTrackDetailMessage());
							tp.setFileNameXMLRequest(ttd.getProcessTrackDetailNameFileRq());
							tp.setFileNameXMLResponce(ttd.getProcessTrackDetailNameFileRp());	
							tp.setFilePse(ttd.getProcessTrackTransPse());
							tp.setPseId(ttd.getTransactionId());
							tp.setDateProcess(ttd.getProcessTrackDetailDate());
							if(ttd.getProcessTrackDetailState()==1){
								tp.setStateProcess("Ok");
							}else if(ttd.getProcessTrackDetailState()==0){
								tp.setStateProcess("Abierto");
							}
							if(ttd.getProcessTrackDetailError()==null){							
								tp.setProcessError(false);
								tp.setProcessPSE(false);
							}else{
								if(ttd.getProcessTrackDetailError()==0){								
									tp.setProcessError(false);
									tp.setProcessPSE(false);
								}else if(ttd.getProcessTrackDetailError()==2){
									tp.setProcessPSE(true);
									tp.setProcessError(false);
								}else{
									if(ttd.getProcessTrackDetailError()==1){								
										tp.setProcessError(true);
										tp.setProcessPSE(false);
									}					
								}
							}
							
							if(ttd.getVerProcesoComo()==null){
								if(ttd.getTbSystemUser()==null){
									tp.setUserCreateProcess(" ");
								}else{
									if(ttd.getTbSystemUser().getTbCodeType().getCodeTypeId().equals(3L)){
										tp.setUserCreateProcess(ttd.getTbSystemUser().getUserNames());
									}else{
										tp.setUserCreateProcess(ttd.getTbSystemUser().getUserNames()+" "+ttd.getTbSystemUser().getUserSecondNames());
									}	
								}
							} else if(ttd.getVerProcesoComo().equals(1L)){
								tp.setUserCreateProcess("ADMINISTRADOR FACILPASS");
							}else if(ttd.getVerProcesoComo().equals(2L)){
								tp.setUserCreateProcess(ttd.getBankId().getBankName());
							} else if(ttd.getVerProcesoComo().equals(3L)){ 
								tp.setUserCreateProcess("ADMIN FACILPASS");
							}
							
							list.add(tp);
						}
				}
				else {
					Query query = em.createNativeQuery(qry1+qry3+")a )");
					list= query.getResultList();
				}			
		} catch (Exception e) {
			System.out.println(" [ Error en ProcessEJB.getProcessDetails ] ");
			e.printStackTrace();
		}
		return list;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Process#getProcessDetails(java.lang.Long,
	 * constant.ProcessTrackType)
	 */
	@Override
	public List<TbProcessTrackDetail> getProcessDetails(Long referencedId,
			ProcessTrackType processType) {
		List<TbProcessTrackDetail> list = new ArrayList<TbProcessTrackDetail>();
		try {
			if (processType.getId().equals(1L)){
				long userId=userEJB.getSystemMasterById(referencedId);
				Query user = 
					 em.createNativeQuery("SELECT user_id FROM tb_system_user " +
					 					  "WHERE system_user_master_id=?1 or user_id=?1 ");
				    user.setParameter(1, userId);
				    List<?> user1 = (List<?>)user.getResultList();

					String usuarios = user1.toString();
					usuarios=usuarios.replace('[', '(').replace(']', ')');

				    Query query;
							query = em.createNativeQuery("SELECT pd.process_track_detail_id " +
									"FROM tb_process_track ptr " +
									"INNER JOIN tb_process_track_detail pd ON pd.process_track_id = ptr.process_track_id " +
									"WHERE ptr.process_track_referenced_id in  " + usuarios +
									"AND ptr.process_track_type_id = 1 " +
									"ORDER BY pd.process_track_detail_date DESC "); 
							
							for (Object obj : query.getResultList()) {
								list.add(em.find(TbProcessTrackDetail.class, ((BigDecimal)obj).longValue()));
							}					
			}else{
				Query q = em.createQuery("SELECT pd FROM " +
														  		"TbProcessTrack pt, " +
														  		"TbProcessTrackDetail pd " +
														  "WHERE " +
														  		"pt.processTrackId = pd.tbProcessTrack.processTrackId " +
														  		"AND pt.tbProcessTrackType.processTrackTypeId = ?1 " +
														        "AND pt.processTrackReferencedId = ?2 " +
														  "ORDER BY pd.processTrackDetailDate desc");
				q.setParameter(1, processType.getId());
				q.setParameter(2, referencedId);
				for (Object obj : q.getResultList()) {
				list.add((TbProcessTrackDetail) obj);
				}
			}
		} catch (Exception e) {
			System.out.println(" [ Error en ProcessEJB.getProcessDetails ] ");
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<TbAccount> getAccountWithActiveProcess() {
		List<TbAccount> list = new ArrayList<TbAccount>();
		try {
			Query q = em
					.createNativeQuery("SELECT  DISTINCT pt.process_track_referenced_id FROM  tb_process_track pt "
							+ "WHERE pt.process_track_type_id = ?1 AND pt.process_track_state = 0");
			q.setParameter(1, ProcessTrackType.ACCOUNT1.getId());
			for (Object obj : q.getResultList()) {
				list.add(em.find(TbAccount.class, Long.parseLong(obj.toString())));
			}
		} catch (Exception e) {
			System.out
					.println(" [ Error en ProcessEJB.getAccountWithActiveProcess ] ");
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * @author jromero
	 * @param type 1: Aval 0: Pse
	 */
	@Override
	public String getResponseDescByCode(Long responseCode, Long type) {
		try{
			Query q=em.createNativeQuery("SELECT response_description " +
					"FROM tb_response_type " +
					"WHERE response_type_cod=?1 " +
					"AND response_entity=?2");
			q.setParameter(1, responseCode);
			q.setParameter(2, type);
			String description=(String)q.getSingleResult();
			return description;
		}catch (Exception e) {
			System.out.println(" [ Error en ProcessEJB.getResponseDescByCode ] ");
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Método encargado obtener el rol del usuario que realiza la asociación del producto PSE
	 * @author psanchez
	 * @return result
	 */
	public boolean getRoleId(Long creationUser) {
		boolean result=false;
		try{
			Query query= em.createNativeQuery("SELECT count(rur.user_role_id) " +
				                               "FROM re_user_role rur " +
				                               "WHERE rur.role_id = 1 " +
				                               "AND rur.user_id= ?1 ");
			query.setParameter(1, creationUser);
			BigDecimal count = (BigDecimal) query.getSingleResult();
			if(count.hashCode()>0){
				result=true;
			}
		}catch(NoResultException e){
			result=false;
		}catch(Exception ex){
			result=false;
		 }
		return result;
	}
	/**
	 * @author JGomez
	 * @param Codigo de respuesta
	 * @param tipo
	 * @param Codigo TB_UMBRAL
	 */
	public void insertRespuAval (Long id, Long type, Long umbralId){
		try {
			System.out.println("Entre a Crear el Mensaje de respuesta para TB_UMBRAL");
			
			
			Query q=em.createNativeQuery("SELECT RESPONSE_TYPE_ID " +
					"FROM tb_response_type " +
					"WHERE response_type_cod=?1 " +
					"AND response_entity=?2");
			q.setParameter(1, id);
			q.setParameter(2, type);
			BigDecimal code= (BigDecimal) q.getSingleResult();
			
			
			ReResponseTypeRecharge res = new ReResponseTypeRecharge();
			res.setResponseTypeId(em.find(TbResponseType.class, code.longValue()));
			res.setUmbralId(em.find(TbUmbral.class, umbralId));		
			objectEM = new ObjectEM(em);		
			objectEM.create(res);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al Crear el Mensaje de respuesta para TB_UMBRAL");
		}	
	}
}