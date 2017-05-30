package ejb.email;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import constant.LogAction;
import constant.LogReference;

import crud.ObjectEM;
import ejb.Log;

import jpa.ReEmailParametersProcess;
import jpa.TbEmailParameters;
import jpa.TbEmailProcess;
import jpa.TbEmailType;
import jpa.TbOptActRefefenceType;

@Stateless(mappedName = "ejb/email/EmailConfig")
public class EmailConfigEJB implements EmailConfig{
	
	@PersistenceContext(unitName = "bo")
	EntityManager em;
	
	@SuppressWarnings("unused")
	private ObjectEM objectEM;
	
	@EJB(mappedName="ejb/Log")
	private Log log;

	@SuppressWarnings("unchecked")
	@Override
	public List<TbOptActRefefenceType> getListReferences(Long idProcess) {
		try{
			System.out.println("Proceso Id: " + idProcess);
			List<TbOptActRefefenceType> lista = new ArrayList<TbOptActRefefenceType>();
			Query q = null;
			if(idProcess==null){				
				idProcess =(long) -1;
			}
			if(idProcess.equals(2L)){
				q = em.createQuery("Select r from TbOptActRefefenceType r  Where r.optActReferenceId = 'A' Order by r.optActReferenceId");				
			}else if (idProcess!=null && !idProcess.equals(-1L)){
				//Se cambia consulta quitando el estado del mensaje, para que un mensaje inactivo no se pueda volver a crear
				/*q = em.createQuery("Select r from TbOptActRefefenceType r  Where r.optActReferenceId not in (Select tet.tbReference.optActReferenceId From TbEmailType tet Where tet.tbEmailProcess.emailProcessId=?1 and tet.emailStatus=0) and  r.optActReferenceId <> 'A' Order by r.optActReferenceId");*/
				q = em.createQuery("Select r from TbOptActRefefenceType r  Where r.optActReferenceId not in (Select tet.tbReference.optActReferenceId From TbEmailType tet Where tet.tbEmailProcess.emailProcessId=?1) and  r.optActReferenceId <> 'A' Order by r.optActReferenceId");				
				q.setParameter(1, idProcess);
			}else{
				System.out.println("Se recibe el proceso: " + idProcess + " . No se encontro proceso. ------> lista vacia");
				lista.clear();
				return lista;
			}		
			
			lista= (List<TbOptActRefefenceType>) q.getResultList();
			
			return lista;
			
		}catch (NullPointerException ne){
			System.out.println("[ Error en EmailConfigEJB.getListReferences ]");
			ne.printStackTrace();
			return null;
		}
	}

	@Override
	public List<TbEmailProcess> getListProcessNoMessage() {
		try{
			List<TbEmailProcess> lista = new ArrayList<TbEmailProcess>();
			
			Query q = em.createNativeQuery("select EMAIL_PROCESS_ID from TB_EMAIL_PROCESS order by EMAIL_PROCESS_ID");			
			
			for (Object obj : q.getResultList()) {
				Long processId = ((BigDecimal)obj).longValue();
				TbEmailProcess tp = em.find(TbEmailProcess.class, processId);				
				if(!existMessageByProcess(tp.getEmailProcessId())){					
						lista.add(tp);
				}
			}
			return lista;
			
		}catch (NullPointerException ne){
			System.out.println("[ Error en EmailConfigEJB.getListProcess ]");
			ne.printStackTrace();
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TbEmailProcess> getListProcess() {
		try{
			List<TbEmailProcess> lista = new ArrayList<TbEmailProcess>();
			
			Query q = em.createQuery("Select r from TbEmailProcess r Order by r.emailProcessId");
			
			lista= (List<TbEmailProcess>) q.getResultList();
			
			return lista;
			
		}catch (NullPointerException ne){
			System.out.println("[ Error en EmailConfigEJB.getListProcess ]");
			ne.printStackTrace();
			return null;
		}
	}

	
	@Override
	public boolean setMessageEmail(String idType, Long idProcess, String subject, String emailAddressList, String message, String ip, Long user){
		try{
			TbEmailType email = new TbEmailType();
			email.setEmailTypeSubject(subject);
			email.setEmailTypeMessage(message);
			email.setTbEmailProcess(em.find(TbEmailProcess.class, idProcess));
			email.setTbReference(em.find(TbOptActRefefenceType.class, idType));
			email.setEmailAddressList(emailAddressList);
			email.setEmailStatus(0);
			
			ObjectEM objectEM = new ObjectEM(em);
			if(objectEM.create(email)){
				log.insertLog("Mensaje de Correo Creado Correctamente", LogReference.EMAIL, LogAction.CREATE, ip, user);
				return true;
			}else{
				log.insertLog("Error al Crear Mensaje", LogReference.EMAIL, LogAction.CREATEFAIL, ip, user);
				return false;
			}
		}catch(Exception e){
			System.out.println("[ Error en EmailConfigEJB.setMessageEmail ]");
			e.printStackTrace();
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ReEmailParametersProcess> getParameters(Long processId) {
		try{
			List<ReEmailParametersProcess> lista = new ArrayList<ReEmailParametersProcess>();
			
			Query q = em.createQuery("Select r from ReEmailParametersProcess r Where r.emailProcessId.emailProcessId=?1 Order by r.emailParameterId.emailParametersName");
			q.setParameter(1, processId);
			
			lista= (List<ReEmailParametersProcess>) q.getResultList();
			
			return lista;
		}catch(Exception e){
			System.out.println("[ Error en EmailConfigEJB.getParameters ]");
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String getAbbreviationByParameter(Long idParameter) {
		try{
			TbEmailParameters p = em.find(TbEmailParameters.class,idParameter);
			if(p != null){
				return (p.getEmailParametersAbbreviation());
			}else{
				return "";
			}
		}catch (Exception e){
			System.out.println("[ Error en EmailConfigEJB.getAbbreviationByParameter ]");
			e.printStackTrace();
			return "";
		}
	}

	@Override
	public String getReplaceByAbbreviation(String abbreviation) {
		try{
			String result = "";
			Query q = em.createNativeQuery("Select EMAIL_PARAMETERS_ID from TB_EMAIL_PARAMETERS where EMAIL_PARAMETERS_ABBREVIATION = ?1");
			q.setParameter(1, abbreviation);
			
			Long r = ((BigDecimal)q.getSingleResult()).longValue();
			
			if(r != null){
				TbEmailParameters p = em.find(TbEmailParameters.class,r);
				if(p !=null){
					result = p.getEmailParametersReplace()== null ? "": p.getEmailParametersReplace();
					
					Query q1 = em.createNativeQuery("Select to_char("+p.getEmailParametersFieldRef()+") from "+p.getEmailParametersTableRef()+" where rownum=1");
					
					String s = (String)q1.getSingleResult();
					
					result = result+s;
					
				}
			}
			return result;
		}catch (Exception e){
			System.out.println("[ Error en EmailConfigEJB.getReplaceByAbbreviation ]");
			e.printStackTrace();
			return "";
		}
	}

	@Override
	public String replaceParameterPrev(String template) {
		try{
			String mensaje = "";
			mensaje = template;
			Query q = em.createQuery("Select p from TbEmailParameters p order by p.emailParametersId");
			for (Object obj : q.getResultList()) {
				TbEmailParameters tp = (TbEmailParameters) obj;
				if(mensaje.contains(tp.getEmailParametersAbbreviation())){
					String repl = tp.getEmailParametersReplace()== null ? "": tp.getEmailParametersReplace();
					if(tp.getEmailParametersAbbreviation().equals("#VLT")){						
					    mensaje = mensaje.replaceAll(tp.getEmailParametersAbbreviation(), repl+"\\$"+tp.getEmailParametersDemo());
					}else{						
					    mensaje = mensaje.replaceAll(tp.getEmailParametersAbbreviation(), repl+tp.getEmailParametersDemo());
					}
						System.out.println("mensaje: "+mensaje);
				}
			}			
			return mensaje;
		}catch(Exception e){
			System.out.println("[ Error en replaceParameterPrev.insertMessageOutbox ]");
			e.printStackTrace();
			return template;
		}
	}

	@Override
	public List<TbEmailType> getListEmailTypeByProcess(Long processId) {
		try{
			List<TbEmailType> lista = new ArrayList<TbEmailType>();
			Query q = em.createQuery("Select p from TbEmailType p where p.tbEmailProcess.emailProcessId=?1 order by p.emailTypeId");
			q.setParameter(1, processId);
			for (Object obj : q.getResultList()) {
				TbEmailType tt = (TbEmailType) obj;
				lista.add(tt);
			}			
			return lista;
		}catch(Exception e){
			System.out.println("[ Error en replaceParameterPrev.getListEmailTypeByProcess ]");
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public TbEmailType getEmailTypebyId(Long emailId) {
		try{
			TbEmailType lista = em.find(TbEmailType.class, emailId);
			return lista;
		}catch(Exception e){
			System.out.println("[ Error en replaceParameterPrev.getEmailTypebyId ]");
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public boolean editMessageEmail(Long emailId, String idType, Long idProcess, String subject,String emailAddressList, String message, String ip, Long user){
		try{
			TbEmailType email = em.find(TbEmailType.class, emailId);
			email.setEmailTypeSubject(subject);
			email.setEmailTypeMessage(message);
			email.setTbEmailProcess(em.find(TbEmailProcess.class, idProcess));
			email.setTbReference(em.find(TbOptActRefefenceType.class, idType));
			email.setEmailAddressList(emailAddressList);
			
			ObjectEM objectEM = new ObjectEM(em);
			if(objectEM.update(email)){
				log.insertLog("Mensaje de Correo "+emailId+" Editado Correctamente", LogReference.EMAIL, LogAction.UPDATE, ip, user);
				return true;
			}else{
				log.insertLog("Error al editar Mensaje"+emailId, LogReference.EMAIL, LogAction.UPDATEFAIL, ip, user);
				return false;
			}
		}catch(Exception e){
			System.out.println("[ Error en EmailConfigEJB.editMessageEmail ]");
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean existMessageByProcess(Long porcessId){
		try{
			boolean result = true;			
			 Query q = em.createNativeQuery("select count(*) from tb_email_type where email_process_id=?1 and OPT_ACT_REFERENCE_ID in ('C') ");
			 q.setParameter(1, porcessId);
			 Integer cantidadC = (Integer) ((BigDecimal) q.getSingleResult()).intValue();
			 
			 Query qA = em.createNativeQuery("select count(*) from tb_email_type where email_process_id=?1 and OPT_ACT_REFERENCE_ID in ('A') ");
			 qA.setParameter(1, porcessId);
			 Integer cantidadA = (Integer) ((BigDecimal) qA.getSingleResult()).intValue();
			 
			 Query qU = em.createNativeQuery("select count(*) from tb_email_type where email_process_id=?1 and OPT_ACT_REFERENCE_ID in ('U') ");
			 qU.setParameter(1, porcessId);
			 Integer cantidadU = (Integer) ((BigDecimal) qU.getSingleResult()).intValue();
			 
		 
			 if(cantidadC >= 1 && cantidadU >= 1){
				 result = true;
			 }else if(cantidadA >= 1){
				 result = true;
			 }else{
				 result = false;
			 }
			 
			return result;
		}catch (Exception e){
			e.printStackTrace();
			return true;
		}
	}

	@Override
	public boolean deleteMessageEmail(Long emailId, String ip, Long user) {
		try{
			boolean result = false;			
			
			TbEmailType email = em.find(TbEmailType.class, emailId);
			email.setEmailStatus(1);
			ObjectEM objectEM = new ObjectEM(em);
			if(objectEM.update(email)){
				log.insertLog("Mensaje de Correo "+emailId+" Inactivado Correctamente", LogReference.EMAIL, LogAction.UPDATE, ip, user);
				result = true;
			}else{
				log.insertLog("Error al eliminar Mensaje"+emailId, LogReference.EMAIL, LogAction.UPDATEFAIL, ip, user);
				result = false;
			}
			return result;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}		
	}
	
	@Override
	public boolean activeMessageEmail(Long emailId, String ip, Long user) {
		try{
			boolean result = false;			
			
			TbEmailType email = em.find(TbEmailType.class, emailId);
			email.setEmailStatus(0);
			ObjectEM objectEM = new ObjectEM(em);
			if(objectEM.update(email)){
				log.insertLog("Mensaje de Correo "+emailId+" Activado Correctamente", LogReference.EMAIL, LogAction.UPDATE, ip, user);
				result = true;
			}else{
				log.insertLog("Error al activar Mensaje"+emailId, LogReference.EMAIL, LogAction.UPDATEFAIL, ip, user);
				result = false;
			}
			return result;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}		
	}

	@Override
	public boolean validateProcessTypeMsg(Long processId, String typeMsgId) {
		try {
			System.out.println("Entre a validar si existe el tipo de mensaje");
			Query q = em.createNativeQuery("select count(*) from tb_email_type where email_process_id=?1 and OPT_ACT_REFERENCE_ID = '?2'");
			q.setParameter(1, processId);
			q.setParameter(1, typeMsgId);
			Integer cantidadA = (Integer) ((BigDecimal) q.getSingleResult()).intValue();	
			System.out.println("Cantidad encontrada: "+cantidadA);
			return cantidadA > 0 ? Boolean.TRUE : Boolean.FALSE;
			
		} catch (NoResultException ne){
			System.out.println("Entre a NoResultException validateProcessTypeMsg");
			return Boolean.FALSE;
		} catch (Exception e) {
			System.out.println("Entre a Exception validateProcessTypeMsg");
			e.printStackTrace();
			return Boolean.FALSE;
		}
		
	}
	
}
