package ejb.crud;

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

import constant.LogAction;
import constant.LogReference;
import crud.ObjectEM;
import ejb.Log;

import jpa.TbCompanyTag;
import jpa.TbCourier;
import jpa.TbRollo;


/**
 * Session Bean implementation class CompanyTagEJB
 */
@Stateless(mappedName = "ejb/CompanyTag")
public class CompanyTagEJB implements CompanyTag {
	
	@PersistenceContext(unitName = "bo")
	private EntityManager em;
	
	@EJB(mappedName = "ejb/Log")
	private Log logEJB;
	
	private ObjectEM objectEM;

    /**
     * Default constructor. 
     */
    public CompanyTagEJB() {
    }
    


	@Override
	public List<TbCompanyTag> getCompanyTag() {
		List<TbCompanyTag> list = new ArrayList<TbCompanyTag>();
		try {
			Query q = em.createQuery("SELECT tc FROM TbCompanyTag tc WHERE tc.companyTagState = 1 ORDER By tc.companyTagName ");
			for (Object obj : q.getResultList()) {
				list.add((TbCompanyTag) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en CompanyTagEJB.getCompanyTag. ] ");
			e.printStackTrace();
		}
		return list;
	}



	/**
	 * M�todo encargado de insertar registros en la tabla Tb_company_tag
	 * @author psanchez
	 */
	@Override
	public String insertCompanyTag(String companyTagName, String ip, Long creationUser) {
		String result=null;
		try {
			TbCompanyTag tct = new TbCompanyTag();	
			tct.setCompanyTagName(companyTagName.trim().toUpperCase());
			tct.setCompanyTagState(1);
			
			objectEM = new ObjectEM(em);	
			if(objectEM.create(tct)){		
				logEJB.insertLog("Creaci�n de Compa�ia Tag | Se ha creado la compa�ia Tag ID: " + tct.getCompanyTagId() + ".",
					LogReference.COMPANY, LogAction.CREATE, ip, creationUser);
			    result= "Se ha registrado la empresa con �xito.";
			}else{
				logEJB.insertLog("Creaci�n de Compa�ia Tag | No se ha creado la compa�ia Tag ID: " + tct.getCompanyTagId() + ".",
						LogReference.COMPANY, LogAction.CREATE, ip, creationUser);
				result= "La empresa no ha sido creada.";
			}
		} catch (Exception e) {
			System.out.println(" [ Error en CompanyTagEJB.insertCompanyTag ] "+e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 * M�todo encargado de validar registros en la tabla tb_company_tag 
	 * @author psanchez
	 */
	@Override
	public String existCompanyTag(String companyTagName) {
	    String result=null;
				Query query= 
					em.createNativeQuery("SELECT count(company_tag_id) " +
							             "FROM tb_company_tag " +
										 "WHERE (upper(company_tag_name)=upper(?1)) ").setParameter(1, companyTagName.trim().toUpperCase());
				
				BigDecimal idCompanyTag =  (BigDecimal) query.getSingleResult();
				if(idCompanyTag.intValue()>=1){
			       result="El Nombre Empresa ya se encuentra registrado."; 
				}
		return result;
	}



	@Override
	public String insertCourier(Long courierId, String courierName, Long companyTagId, String ip, Long creationUser) {
		String result=null;
		try {
			TbCourier tc = new TbCourier();
			tc.setCourierId(Long.valueOf(courierId));
			tc.setDateCreation(new Timestamp(System.currentTimeMillis()));
			tc.setCourierName(courierName.trim().toUpperCase());
			tc.setCompanyTagId(em.find(TbCompanyTag.class, Long.valueOf(companyTagId)));
			tc.setCourierState(0);
			
			objectEM = new ObjectEM(em);	
			if(objectEM.create(tc)){		
				logEJB.insertLog("Creaci�n Courier | Se ha creado el courier ID: " + tc.getCourierId() + ".",
					LogReference.COMPANY, LogAction.CREATE, ip, creationUser);
			    result= "Se ha registrado el courier con �xito.";
			}else{
				logEJB.insertLog("Creaci�n Courier | No se ha creado el courier ID: " + tc.getCourierId() + ".",
						LogReference.COMPANY, LogAction.CREATE, ip, creationUser);
				result= "La courier no ha sido creado.";
			}
		} catch (Exception e) {
			System.out.println(" [ Error en CompanyTagEJB.insertCourier ] "+e.getMessage());
			e.printStackTrace();
		}
		return result;
	}



	@Override
	public String insertRoll(String rollName, Long courierId, String ip, Long creationUser) {
		String result=null;
		try {
			TbRollo tr = new TbRollo();	
			tr.setRollName(rollName.trim().toUpperCase());
			tr.setCourierId(em.find(TbCourier.class, Long.valueOf(courierId)));
			tr.setRollState(1);
			
			objectEM = new ObjectEM(em);	
			if(objectEM.create(tr)){		
				logEJB.insertLog("Creaci�n Rollo | Se ha creado el rollo ID: " + tr.getRollId() + ".",
					LogReference.COMPANY, LogAction.CREATE, ip, creationUser);
			    result= "Se ha registrado el rollo con �xito.";
			}else{
				logEJB.insertLog("Creaci�n Rollo | No se ha creado el rollo ID: " + tr.getRollId() + ".",
						LogReference.COMPANY, LogAction.CREATE, ip, creationUser);
				result= "El rollo no ha sido creado.";
			}
		} catch (Exception e) {
			System.out.println(" [ Error en CompanyTagEJB.insertRoll ] "+e.getMessage());
			e.printStackTrace();
		}
		return result;
	}



	@Override
	public String existCourier(String courierName, Long courierIdNew) {
		String result=null;
		Query query= 
			em.createNativeQuery("SELECT count(courier_id) " +
					             "FROM tb_courier " +
								 "WHERE (upper(courier_name)) = upper(?1) ").setParameter(1, courierName.trim().toUpperCase());
	        BigDecimal nameCourier = (BigDecimal) query.getSingleResult();
	        
        Query query1= 
			em.createNativeQuery("SELECT count(courier_id) " +
					             "FROM tb_courier " +
								 "WHERE courier_id = ?1 ").setParameter(1, courierIdNew);
		    BigDecimal idCourier = (BigDecimal) query1.getSingleResult();

		    if(idCourier.intValue()>=1){
			    result="El Id Courier ya se encuentra registrado."; 
			}
		    else if(nameCourier.intValue()>=1){
			    result="El Nombre Courier ya se encuentra registrado."; 
			}
		return result;
	}



	@Override
	public String existRoll(String rollName, Long rolloId) {
		String result=null;
		Query query= 
			em.createNativeQuery("SELECT count(roll_id) " +
					             "FROM tb_roll " +
								 "WHERE (upper(roll_name ))= upper(?1) ").setParameter(1, rollName.trim().toUpperCase());
	        BigDecimal nameRoll = (BigDecimal) query.getSingleResult();

		    if(nameRoll.intValue()>=1){
			    result="El nombre del rollo ya existe en el sistema."; 
			}
		return result;
	}

	
	public String getCompanyTagName(Long companyTagId){
		TbCompanyTag ct = em.find(TbCompanyTag.class, companyTagId);
		return ct.getCompanyTagName();	
	}
	
	public String getCourierName(Long courierId){
		TbCourier tc = em.find(TbCourier.class, courierId);
		return tc.getCourierName();	
	}



	@SuppressWarnings("unchecked")
	@Override
	public List<TbCourier> getCourier(Long companyTagId) {
		List<TbCourier> lista= new ArrayList<TbCourier>();
		
		Query q = em.createQuery("select cr from TbCourier cr where cr.companyTagId.companyTagId=?1 order by cr.courierName");
		q.setParameter(1, companyTagId);
		lista=q.getResultList();
		return lista;
	}
	
	@Override
	public List<TbRollo> getRollosByCourier(Long companyTagId, Long courierId){
		List<TbRollo> lista= new ArrayList<TbRollo>();
		
		Query q = em.createNativeQuery("SELECT tr.roll_id FROM tb_courier tc " +
				                       "LEFT JOIN tb_company_tag tct ON tc.company_tag_id = tct.company_tag_id " +
				                       "INNER JOIN tb_roll tr ON tc.courier_id = tr.courier_id " +
				                       "WHERE tct.company_tag_id = ?1 " +
				                       "AND tc.courier_id = ?2 ");
		q.setParameter(1, companyTagId);
		q.setParameter(2, courierId);
		for (Object obj : q.getResultList()) {
			lista.add(em.find(TbRollo.class, Long.parseLong(obj.toString())));
		}
		
		return lista;
	}



	@Override
	public boolean getpermission(Long userId, String nameperm) {
		long result = 0L;
		boolean resp = false;
		try {
			
			System.out.println("getpermission EJB. Entre a Validar Permisos " + userId + "," + nameperm);
			Query q = em.createNativeQuery ("select count(*) from re_role_option_action rroa "+
												"inner join re_option_action roa on roa.option_action_id = rroa.option_action_id "+
												"inner join re_user_role ruo on rroa.role_id  = ruo.role_id  "+
												"inner join tb_system_user tu on tu.user_id = ruo.user_id "+
												"where tu.user_id = ?1 "+
												"and roa.behavior = ?2 "+
												"and roa.option_action_state = 2");
			q.setParameter(1, userId);
			q.setParameter(2, nameperm);
			result =((BigDecimal) q.getSingleResult()).longValue();
				if (result > 0){
					resp = true;
				}
		}catch(NoResultException ex){
			resp = false;
		}catch(Exception e){
			e.printStackTrace();
			resp = false;
		}
		return resp;
	}

}