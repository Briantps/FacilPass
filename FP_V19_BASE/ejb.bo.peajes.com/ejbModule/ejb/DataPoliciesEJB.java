package ejb;

import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jpa.ReUserPolicyPersonalData;
import jpa.TbPolicyPersonalData;
import jpa.TbPolicyPersonalDataState;
import jpa.TbProcessTrack;
import jpa.TbSystemUser;
import jpa.TbTypeRole;
import jpa.TbUserPolicyDataState;
import constant.EmailProcess;
import constant.LogAction;
import constant.LogReference;
import constant.ProcessTrackDetailType;
import constant.ProcessTrackType;
import crud.ObjectEM;
import ejb.email.Outbox;


@Stateless(mappedName = "ejb/DataPolicies")
public class DataPoliciesEJB implements DataPolicies{

	
	
	@EJB(mappedName="ejb/Log")
	private Log log;
	
	@PersistenceContext(unitName = "bo") 
	EntityManager em;
	
	@EJB(mappedName="ejb/Process")
	private Process process;
	
	@EJB(mappedName ="ejb/email/Outbox")
	private Outbox outbox;
	
	private ObjectEM objectEM;
	
	
	public String getTextHTML(Long roleId) {
		String textHTML = "";
		
		try {
			System.out.println("Entre a DataPoliciesEJB.getTextHTML");
			
			Query q = em.createNativeQuery("select policy_personal_data_txt from tb_policy_personal_data where type_role_id = ?1 and policy_personal_data_state = 1");
			q.setParameter(1, roleId);
			Clob clob=(Clob) q.getSingleResult();
			textHTML=clob.getSubString(1, (int) clob.length());
						
		}catch (NoResultException ex) {
			textHTML = " ";		
			System.out.println("Entre a NoResultexeption en DataPoliciesEJB.getTextHTML");
		}catch (Exception e) {
			e.printStackTrace();
			textHTML = " ";
			System.out.println("Error en DataPoliciesEJB.getTextHTML");
		}
		
		return textHTML;
	}


	@Override
	public String setCreateTXT(Long userId, String textHtml, String ip,Long roleId) {
		String resp;
		System.out.println("Entre a DataPoliciesEJB.setCreateTXT");
		try {
			TbPolicyPersonalData dtxt = new TbPolicyPersonalData();
			
			dtxt.setPolicyTXT(textHtml);
			dtxt.setPolicyUserId(em.find(TbSystemUser.class, userId));
			dtxt.setPolicyPersonalDate(new Timestamp(System.currentTimeMillis()));
			dtxt.setPolicyState(em.find(TbPolicyPersonalDataState.class,1L));
			dtxt.setPolicyTypeRole(em.find(TbTypeRole.class, roleId));
			
			objectEM = new ObjectEM(em);	
			if(objectEM.create(dtxt)){
				
				System.out.println("Entre a Realizar Update setCreateTXT");
				System.out.println("IdPolicy" + dtxt.getPolicyPersonalDataId());
				
				Query q = em.createNativeQuery("update tb_policy_personal_data set policy_personal_data_state = 2 where type_role_id = ?1 and policy_personal_data_id <> ?2");
				q.setParameter(1, roleId);
				q.setParameter(2, dtxt.getPolicyPersonalDataId());
				int count = q.executeUpdate();
				
				System.out.println("La cantidad de mensajes inctivados es de: " + count);
			
				log.insertLog("Aviso Política | Se ha configurado un nuevo aviso de privacidad por el usuario : " + userId, 
						LogReference.DATA_POLICIES,
				LogAction.CREATE, ip, userId);
				
			}
			resp = "Se han guardado los cambios con éxito";		
			
			
		} catch (Exception e) {
			System.out.println("Error en setCreateTXT");
			resp = "Error en la transaccion";
			e.printStackTrace();
		}
		return resp;
	}


	@Override
	public boolean getNotExistsPermission(Long userId) {
		System.out.println("Entre a DataPoliciesEJB.getNotExistsPermission");
		try {
			
			Query q = em.createNativeQuery("select nvl(max(us_policy_pers_data_state),0) from re_user_policy_personal_data where user_id = ?1");
			q.setParameter(1, userId);
			
			BigDecimal resp =  (BigDecimal) q.getSingleResult();
			
			if (resp!=null && resp.longValue() == 2){
				return true;
			}else if (resp != null && resp.longValue() == 1) {
				return false;
			}								
		}catch (NoResultException e) {
			System.out.println("No se encontro registro de Aprobacion Creado");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		} 
		return true;
	}


	@SuppressWarnings("unused")
	@Override
	public void setCreatesPermission(Long creationUser, Long idPolitica, Long state,String ip,boolean outside,Long userId) {
		System.out.println("Entre a DataPoliciesEJB.setCreatesPermission. creationUser: " + creationUser + ", IdPolitica: " + idPolitica + ", State: " + state + ", ip: " + ip + ", Externo " + outside + ", UserId" + userId);

		Timestamp timeRejected = null;
		Timestamp timeAprobe = null;

		if (state == 1){
			timeAprobe = new Timestamp(System.currentTimeMillis());
			System.out.println("Fecha Aprueba: " + timeAprobe);
		}else{
			timeRejected = new Timestamp(System.currentTimeMillis());
			System.out.println("Fecha Rechazo: " + timeRejected);
		}
		Long userperm;
		Long idPermission;
		try {
			if (userId != null){
				idPermission = (getIdPermission(userId));
				userperm = userId;
			}else{
				idPermission = (getIdPermission(creationUser));
				userperm = creationUser;
			}
			
			if (idPermission == 0) {

				System.out.println("Entre a Crear Registro");
				ReUserPolicyPersonalData rup = new ReUserPolicyPersonalData();
				rup.setUserPolicyUserId(em.find(TbSystemUser.class, userperm));				
				rup.setUserPolicyPersonalAprobe(timeAprobe);
				rup.setUserPolicyPersonalRejected(timeRejected);
				rup.setUserPolicyState(em.find(TbUserPolicyDataState.class, state));
				rup.setUserPolicyPersonDataId(em.find(TbPolicyPersonalData.class, idPolitica));
				rup.setPolicyIp(ip);

				em.persist(rup);
				em.flush();
			}else if (idPermission>0) {

				System.out.println("Entre a Actualizar Registro: " + idPermission );
				ReUserPolicyPersonalData rup = em.find(ReUserPolicyPersonalData.class, idPermission);
				rup.setUserPolicyState(em.find(TbUserPolicyDataState.class,state));
				rup.setUserPolicyPersonalAprobe(timeAprobe);
				if (state == 2){
					rup.setUserPolicyPersonalRejected(timeRejected);
				}
				em.merge(rup);
				em.flush();
			}
			String dia;
			String mes;
			String anio;
			
			if (outside){
				if (state==1){

					Calendar cal = Calendar.getInstance();
					cal.setTime(timeAprobe);
					
					dia = getcerodaymon(cal.get(Calendar.DATE));
					mes = getcerodaymon((cal.get(Calendar.MONTH)+1));
					anio= String.valueOf(cal.get(Calendar.YEAR));
					
					anio= String.valueOf(cal.get(Calendar.YEAR));
					
					System.out.println("Time Calendar: " + cal.getTime());

					TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, creationUser);
					Long idPTA;
					if(pt==null){
						idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  creationUser, ip, creationUser);
					}
					else{
						idPTA=pt.getProcessTrackId();
					}
					
					Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.DATA_POLICIES, 
							"Señor Usuario el " + dia +  " de " + mes+ " de " + anio +
							" autorizó el tratamiento de sus datos personales y aceptó el aviso de privacidad",
							creationUser, ip,1,"Error en la Asignacion",null,null,null,null);			
					//proceso cliente
					TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,creationUser);
					Long idPTC;
					if(ptc==null){
						idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, creationUser, ip, creationUser);
					}
					else{
						idPTC=ptc.getProcessTrackId();
					}
					Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.DATA_POLICIES, 
							"Señor Usuario el " + dia +  " de " + mes + " de " + anio +
							" autorizó el tratamiento de sus datos personales y aceptó el aviso de privacidad",
							creationUser, ip,1,"Error en la Asignacion",null,null,null,null);

					log.insertLog("Aviso Política | El usuario: " + creationUser +" , Acepto la Política y proteccion de datos personales", 
							LogReference.DATA_POLICIES,
							LogAction.CREATE, ip, creationUser);

				}else if(state == 2){

					Calendar cal = Calendar.getInstance();
					cal.setTime(timeRejected);
					
					dia = getcerodaymon(cal.get(Calendar.DATE));
					mes = getcerodaymon((cal.get(Calendar.MONTH)+1));
					anio= String.valueOf(cal.get(Calendar.YEAR));
					
					System.out.println("Time Calendar: " + cal.getTime());
					TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, creationUser);
					Long idPTA;
					if(pt==null){
						idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  creationUser, ip, creationUser);
					}
					else{
						idPTA=pt.getProcessTrackId();
					}
					Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.DATA_POLICIES, 
							"Señor Usuario el " + dia +  " de " + mes + " de " + anio +
							" NO autorizó el tratamiento de sus datos personales y NO aceptó el aviso de privacidad",
							creationUser, ip,1,"Error en la Asignacion",null,null,null,null);			
					//proceso cliente
					TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,creationUser);
					Long idPTC;
					if(ptc==null){
						idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, creationUser, ip, creationUser);
					}
					else{
						idPTC=ptc.getProcessTrackId();
					}
					Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.DATA_POLICIES, 
							"Señor Usuario el " + dia +  " de " + mes + " de " + anio +
							" NO autorizó el tratamiento de sus datos personales y NO aceptó el aviso de privacidad",
							creationUser, ip,1,"Error en la Asignacion",null,null,null,null);
					log.insertLog("Aviso Política | El usuario: " + creationUser +" , NO Acepto la Política y proteccion de datos personales", 
							LogReference.DATA_POLICIES,
							LogAction.CREATE, ip, creationUser);
					
					TbSystemUser tsu = em.find(TbSystemUser.class, creationUser);
					
					outbox.insertMessageOutbox(creationUser, 
							EmailProcess.DATA_POLICIES, 
							null, 
							null, 
							null, 
							null, 
							null, 
							null, 
							null, 
							null, 
							creationUser, 
							tsu.getTbCodeType().getCodeTypeId(), 
							null,
							"Políticas y Datos Personales Rechazados", true,
							null);

				}
			}else{

				if (userId!=null){
					TbSystemUser tus = em.find(TbSystemUser.class, userId);


					Calendar cal = Calendar.getInstance();
					cal.setTime(timeAprobe);
					
					dia = getcerodaymon(cal.get(Calendar.DATE));
					mes = getcerodaymon((cal.get(Calendar.MONTH)+1));
					anio= String.valueOf(cal.get(Calendar.YEAR));
					
					anio= String.valueOf(cal.get(Calendar.YEAR));

					TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, creationUser);
					Long idPTA;
					if(pt==null){
						idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  creationUser, ip, creationUser);
					}
					else{
						idPTA=pt.getProcessTrackId();
					}
					Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.DATA_POLICIES, 
							"Señor Usuario el " + dia +  " de " + mes + " de " + anio +
							" autorizó el tratamiento de datos personales y aceptó el aviso de privacidad para la creación del usuario " + tus.getUserNames(),
							creationUser, ip,1,"Error en la Asignacion",null,null,null,null);			
					//proceso cliente
					TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,creationUser);
					Long idPTC;
					if(ptc==null){
						idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, creationUser, ip, creationUser);
					}
					else{
						idPTC=ptc.getProcessTrackId();
					}
					Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.DATA_POLICIES, 
							"Señor Usuario el " + dia +  " de " + mes + " de " + anio +
							" autorizó el tratamiento de datos personales y aceptó el aviso de privacidad para la creación del usuario " + tus.getUserNames(),
							creationUser, ip,1,"Error en la Asignacion",null,null,null,null);

					log.insertLog("Aviso Política | El Cliente: " + creationUser +" , Acepto la Política y proteccion de datos personales para el Usuario: " + creationUser, 
							LogReference.DATA_POLICIES,
							LogAction.CREATE, ip, creationUser);					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error en DataPoliciesEJB.setCreatesPermission");
		}

	}


	@Override
	public Long getIdHTML(long roleId) {
		Long resp;

		try {
			System.out.println("Entre a DataPoliciesEJB.getIdHTML");

			Query q = em.createNativeQuery("select policy_personal_data_id from tb_policy_personal_data where type_role_id = ?1 and policy_personal_data_state = 1");
			q.setParameter(1, roleId);
			resp = ((BigDecimal) q.getSingleResult()).longValue();

			if (resp==null){
				resp=0L;
			}

		}catch (NoResultException ex) {
			resp=0L;	
			System.out.println("Entre a NoResultexeption en DataPoliciesEJB.getTextHTML");
		}catch (Exception e) {
			resp=0L;
			System.out.println("Error en DataPoliciesEJB.getTextHTML");
			e.printStackTrace();
		}
		return resp;
	}
	
	public Long getIdPermission(long userId) {
		Long resp;
				
		try {
			System.out.println("Entre a DataPoliciesEJB.getIdPermission");
			
			Query q = em.createNativeQuery("select user_policy_personal_data_id from re_user_policy_personal_data where user_id = ?1");
			q.setParameter(1, userId);
			resp = ((BigDecimal) q.getSingleResult()).longValue();
			
			if (resp==null){
				resp=0L;
			}
			
		}catch (NoResultException ex) {
			resp=0L;	
			System.out.println("Entre a NoResultexeption en DataPoliciesEJB.getIdPermission");
		}catch (Exception e) {
			resp=0L;
			System.out.println("Error en DataPoliciesEJB.getIdPermission");
			e.printStackTrace();
		}
		return resp.longValue();
	}
	
	public String getcerodaymon (int inter){
		String respu = null;
		
		if(inter<9){
			respu="0"+String.valueOf(inter);
		}else{
			respu=String.valueOf(inter);
		}
		
		return respu;
	}

}
