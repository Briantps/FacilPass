package ejb;

import java.sql.Clob;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jpa.TbConfAutomaticRecharge;
import jpa.TbState;
import jpa.TbSystemUser;
import jpa.TbTypeConfAutoRechar;
import constant.LogAction;
import constant.LogReference;
import crud.ObjectEM;


@Stateless(mappedName = "ejb/IConfAutomaticProgramming")
public class ConfAutomaticProgrammingEJB implements IConfAutomaticProgramming{



	@EJB(mappedName="ejb/Log")
	private Log log;

	@PersistenceContext(unitName = "bo") 
	EntityManager em;

	private ObjectEM objectEM;


	@SuppressWarnings("unchecked")
	public String[] getTextHTML(Long typeConfAutoRecharId) {
		String textHTML = " ";
		String state = "0";
		String type = "1";
		try {
			System.out.println("Entre a ConfAutomaticProgrammingEJB.getTextHTML");

			Query q = em.createNativeQuery("select TCAR.CONF_AUTOMATIC_RECHARGE_TXT, TCAR.STATE_ID " +
					"from TB_CONF_AUTOMATIC_RECHARGE TCAR " +					
					"where TCAR.TYPE_CONF_AUTO_RECHAR_ID =?1 " +
					"and TCAR.STATE_ID = 1");
			q.setParameter(1, typeConfAutoRecharId);
			
			List<Object> lis= (List<Object>)q.getResultList();
			if(lis.size()>0){
				Object[] obj=(Object[]) lis.get(0);
				if(obj[0]!=null){
					Clob clob=(Clob) (obj[0]);
					textHTML=clob.getSubString(1, (int) clob.length());
				}	
							
				state = obj[1].toString();
			}else{
				System.out.println("Entre a NoResultexeption en ConfAutomaticProgrammingEJB.getTextHTML");
			}	
		}catch (NoResultException ex) {
			textHTML = " ";		
			System.out.println("Entre a NoResultexeption en ConfAutomaticProgrammingEJB.getTextHTML");
		}catch (Exception e) {
			e.printStackTrace();
			textHTML = " ";
			System.out.println("Error en ConfAutomaticProgrammingEJB.getTextHTML");
		}
		try {
			type = (em.find(TbTypeConfAutoRechar.class, typeConfAutoRecharId)).getTbTypeMessageWysiwyg().getTypeMessageWysiwygId().toString();			
			System.out.println(textHTML+"  "+state+"    "+type);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al intentar obtener el tipo para el editor");
		}
		
		
		String[] respuesta = {textHTML,state,type};
		
		return respuesta;
	}


	@Override
	public String setCreateTXT(Long userId, String textHtml,Long typeConfAutoRecharId) {
		String resp;
		System.out.println("Entre a ConfAutomaticProgrammingEJB.setCreateTXT");
		try {
			TbConfAutomaticRecharge tcar = new TbConfAutomaticRecharge();
			
			
			tcar.setConfAutomaticRechargeTxt(textHtml);
			tcar.setConfAutomaticRechargeDate(new Timestamp(System.currentTimeMillis()));
			tcar.setTypeConfAutomRechar(em.find(TbTypeConfAutoRechar.class, typeConfAutoRecharId));
			tcar.setUserId(em.find(TbSystemUser.class, userId));
			tcar.setStateId(em.find(TbState.class, 1L));
			
			em.persist(tcar);
			em.flush();
			
			
			objectEM = new ObjectEM(em);	
			if(objectEM.create(tcar)){				
				long id=tcar.getConfiAutomaticRechargeId();
				Query q = em.createNativeQuery("update TB_CONF_AUTOMATIC_RECHARGE " +
						"set STATE_ID = 0 " +
						"where CONF_AUTOMATIC_RECHARGE_ID != ?1 " +
						"and TYPE_CONF_AUTO_RECHAR_ID = ?2 " +
						"and STATE_ID = 1");
				q.setParameter(1, id);
				q.setParameter(2, typeConfAutoRecharId);				
				q.executeUpdate();

				log.insertLog("Configuracion recarga programada | Se ha configurado un nuevo mensaje, accion realizada por el usuario: " + userId, 
						LogReference.CONFIAUTOMATICPROGRAMMING,
						LogAction.CREATE, null, userId);

			}
			resp = "Se han guardado los cambios con éxito";		


		} catch (Exception e) {
			System.out.println("Error en setCreateTXT");
			resp = "Error en la transacción";
			e.printStackTrace();
		}
		return resp;
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

	@Override
	public List<TbTypeConfAutoRechar> getListTypeAutoRecharge() {
		List<TbTypeConfAutoRechar> list = new ArrayList<TbTypeConfAutoRechar>();		

		try {
			Query q = em.createQuery("select typeConf from  TbTypeConfAutoRechar typeConf where typeConf.tbState.stateId=1");
			for (Object object : q.getResultList()) {
				list.add((TbTypeConfAutoRechar) object);
				System.out.println();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error a consultar getListTypeAutoRecharge");
		}
		return list;		
	}
	/**
	 * Metodo que obtiene el valor de caracteres maximos permitidos para el tipo
	 * */
	public long getLengthValidate(Long typeConfAutoRecharId){
		long maxCarac = 0;
		try {			
			TbTypeConfAutoRechar type = new TbTypeConfAutoRechar();
			type = em.find(TbTypeConfAutoRechar.class, typeConfAutoRecharId);
			maxCarac = type.getTbSizeTable().getSize();
		} catch (Exception e) {
			e.printStackTrace();
			maxCarac = 0;
			System.out.println(" getLengthValidate Error al obtener el valor de caracteres maximos para el tipo: " + typeConfAutoRecharId);
		}
		
		return maxCarac;
	}
	/**
	 * Metodo que actualiza el valor cuando la seleccion es por tipo de persona
	 * @param tipo de mensaje
	 * @param Accion Inactivar o Activar
	 * @param Usuario
	 * */
	public String setUpdateTypePerson(Long typeConfAutoRecharId, Long action, Long userId){
		String Mensaje="";
		long id=0;
		try {
			
			try {
				Query q = em.createQuery("select confiAutomaticRechargeId from TbConfAutomaticRecharge tcar where tcar.typeConfAutomRechar.typeConfAutoRecharId = ?1");
				q.setParameter(1, typeConfAutoRecharId);
				id=(Long) q.getSingleResult();				
			} catch (Exception e) {
				id=0;
			}
			TbConfAutomaticRecharge tcar =  new TbConfAutomaticRecharge();
			objectEM = new ObjectEM(em);
			if(id==0){	
				
				
				tcar.setConfAutomaticRechargeTxt("");
				tcar.setConfAutomaticRechargeDate(new Timestamp(System.currentTimeMillis()));
				tcar.setTypeConfAutomRechar(em.find(TbTypeConfAutoRechar.class, typeConfAutoRecharId));
				tcar.setUserId(em.find(TbSystemUser.class, userId));
				tcar.setStateId(em.find(TbState.class, action));
				
					
				if(objectEM.create(tcar)){	
					Mensaje= "Se han guardado los cambios con éxito";
				}else{
					Mensaje= "Error al realizar la operación";
				}
				
			}else{				
				tcar = em.find(TbConfAutomaticRecharge.class, id);
				tcar.setStateId(em.find(TbState.class, action));
												
				if(objectEM.update(tcar)){	
					Mensaje= "Se han guardado los cambios con éxito";
				}else{
					Mensaje= "Error al realizar la operación";
				}				
			}
	
				
		} catch (Exception e) {
			e.printStackTrace();
			Mensaje= "Error al realizar la operación";
		}	
		return Mensaje;
	}
	
	public int[] getStateType(){
		int state = 0,type = 0;
		
		int[] respu = {state,type};
		
		return respu;
		 
	}
	
	public String getMessageTooltip(Long confiAutomaticRechargeId){
		String message="";		
		try {
			message = (String) em.createQuery("select confAutomaticRechargeTxt from TbConfAutomaticRecharge where typeConfAutomRechar.typeConfAutoRecharId = ?1 and stateId.stateId = 1")
												.setParameter(1, confiAutomaticRechargeId)
												.getSingleResult();
		}catch (NullPointerException Npe) {
			message="";
			System.out.println("Error al intentar obtener el mensaje o TOOLTIP NullPointerException " +confiAutomaticRechargeId);
		}catch (NoResultException Nre) {
			message="";
			System.out.println("Error al intentar obtener el mensaje o TOOLTIP NoResultException " +confiAutomaticRechargeId);				
		}catch (Exception e) {
			message="";
			e.printStackTrace();
			System.out.println("Error al intentar obtener el mensaje getMessageTooltip() " +confiAutomaticRechargeId);
		}		
		return message;
	}
	
	public boolean getTypePerson(Long confiAutomaticRechargeId){
		
		Long resp = 0L;		
		try {
			resp = (Long) em.createQuery("select count(*) from TbConfAutomaticRecharge where typeConfAutomRechar.typeConfAutoRecharId = ?1 and stateId.stateId = 1")
												.setParameter(1, confiAutomaticRechargeId)
												.getSingleResult();
			if(resp==0){
				return false;
			}else{
				return true;
			}			
		}catch (NullPointerException Npe) {			
			System.out.println("Error al intentar obtener el Tipo de persona NullPointerException " +confiAutomaticRechargeId);
			return false;
		}catch (NoResultException Nre) {
			System.out.println("Error al intentar obtener el Tipo de persona NoResultException " +confiAutomaticRechargeId);
			return false;
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al intentar obtener el Tipo de persona getTypePerson() " +confiAutomaticRechargeId);
			return false;
		}
	}

}
