package ejb;

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

import jpa.TbAgreement;
import jpa.TbChanel;
import jpa.TbCharges;
import jpa.TbPaymentMethod;
import jpa.TbStateRecharge;

@Stateless(mappedName = "ejb/Charges")
public class ChargesEJB implements Charges {

	@PersistenceContext(unitName = "bo") EntityManager em;
	
	private ObjectEM objectEM;
	
	@EJB(mappedName="ejb/Log")
	private Log log;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TbCharges> getAllCharges() {
		Query q = em.createQuery("Select ch From TbCharges ch Order by ch.chargeId");
		List<TbCharges> charges = (List<TbCharges>) q.getResultList();
		return charges;
	}

	@Override
	public boolean setCharge(String nameCharge, int typeCharge, Long valueCharge, String ip, Long user) {
		String typeChargeText="";		
		try {			
			if(typeCharge == 1){
				typeChargeText = "Fijo";
			} else if(typeCharge == 0){
				typeChargeText = "Variable";
			}
			
			TbCharges cargo = new TbCharges();
			cargo.setChargeDescription(nameCharge);
			cargo.setChargeTypeValue(typeCharge);
			cargo.setChargeTypeValueText(typeChargeText);
			cargo.setChargeValue(valueCharge);
			
			objectEM = new ObjectEM(em);
			if(objectEM.create(cargo)){
				log.insertLog("Se ha creado un Nuevo Cargo [ Nombre:"+nameCharge+", Valor: "+typeChargeText+" - "+(valueCharge!=null?valueCharge.toString():""), LogReference.CUSTOMIZATION, LogAction.CREATE, ip, user);
				return true;
			} else {
				log.insertLog("Error al crear Nuevo Cargo [ Nombre:"+nameCharge+", Valor: "+typeChargeText+" - "+(valueCharge!=null?valueCharge.toString():""), LogReference.CUSTOMIZATION, LogAction.CREATEFAIL, ip, user);
				return false;
			}
			
		}catch (Exception e){
			System.out.println("[ Error en : ChargesEJB.setCharge");
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public boolean editCharge(Long chargeId, String nameCharge,
			int typeCharge, Long valueCharge, String ip, Long user) {
		String typeChargeText="";	
		String nameChargeant = null;
		String typeChargeTextant = null;
		Long valueChargeant = null;
		String valueTxtAnt = "";
		String valueTxt = "";
		try {			
				if(typeCharge == 1){
					typeChargeText = "Fijo";
				} else if(typeCharge == 0){
					typeChargeText = "Variable";
				}
			
			TbCharges tbc = em.find(TbCharges.class, chargeId);
			
			nameChargeant = tbc.getChargeDescription();
			typeChargeTextant = tbc.getChargeTypeValueText();
			valueChargeant = tbc.getChargeValue();
			
			tbc.setChargeDescription(nameCharge);
			tbc.setChargeTypeValue(typeCharge);
			tbc.setChargeTypeValueText(typeChargeText);
			tbc.setChargeValue(valueCharge);
			
			if(valueChargeant != null){
				valueTxtAnt = valueChargeant.toString();
			}
			
			if(valueCharge != null){
				valueTxt = valueCharge.toString();
			}
			
			objectEM = new ObjectEM(em);
			if(objectEM.update(tbc)){
				log.insertLog("Se cambiado el cargo "+chargeId+" Datos Anteriores[ Nombre:"+nameChargeant+", Valor: "+typeChargeTextant+" - "+valueTxtAnt+"| Nuevos Datos [ Nombre:"+nameCharge+", Valor: "+typeChargeText+" - "+valueTxt, LogReference.CUSTOMIZATION, LogAction.CREATE, ip, user);
				return true;
			} else {
				log.insertLog("Error al cambiar el cargo "+chargeId, LogReference.CUSTOMIZATION, LogAction.CREATEFAIL, ip, user);
				return false;
			}
			
		}catch (Exception e){
			System.out.println("[ Error en : ChargesEJB.editCharge");
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public String removeCharge(Long chargeId, String ip, Long user) {
		String nameCharge = null;
		String typeChargeText = null;
		Long valueCharge = null;
		try{
			Query q = em.createNativeQuery("select count(*) from Tb_Objection where Charge_Id = ?1");
			q.setParameter(1, chargeId);
			Long cantidad = ((BigDecimal)q.getSingleResult()).longValue();
			System.out.println("cantidad: "+cantidad);
			if(cantidad == 0L){
				TbCharges tbc = em.find(TbCharges.class, chargeId);
				nameCharge = tbc.getChargeDescription();
				typeChargeText = tbc.getChargeTypeValueText();
				valueCharge = tbc.getChargeValue();
				objectEM = new ObjectEM(em);
				if(objectEM.delete(tbc)){
					log.insertLog("Se ha eliminado el Cargo [ Nombre:"+nameCharge+", Valor: "+typeChargeText+" - "+(valueCharge!=null?valueCharge.toString():""), LogReference.CUSTOMIZATION, LogAction.CREATE, ip, user);
					return "Transacción Exitosa";
				} else {
					log.insertLog("Error al eliminar el Cargo [ Nombre:"+nameCharge+", Valor: "+typeChargeText+" - "+(valueCharge!=null?valueCharge.toString():""), LogReference.CUSTOMIZATION, LogAction.CREATEFAIL, ip, user);
					return "Error al Eliminar el Cargo";
				}
			} else {
				return "No fue posible Eliminar el Cargo ya Que Existen Objeciones Relacionadas";
			}
		}catch (Exception e){
			System.out.println("[ Error en : ChargesEJB.removeCharge");
			e.printStackTrace();
		}
		return "Error al Eliminar el Cargo";
	}

	@Override
	public TbCharges getChargeById(Long chargeId) {
		TbCharges tbc = em.find(TbCharges.class, chargeId);		
		return tbc;
	}


	@SuppressWarnings("unchecked")
	@Override
	public boolean existChargeI(String nameCharge){
		boolean res=false;
		try{
			System.out.print("nombre del cargo: " + nameCharge);
			Query q= em.createNativeQuery("select charge_description from tb_charges where upper(charge_description)=upper(?1)");
			q.setParameter(1, nameCharge);
			List<String> list= new ArrayList<String>();
			list=q.getResultList();
			if(list!=null){
				if(list.size()>0){
					res=true;
				}
				else{
					res=false;
				}
			}
			else{
				res=false;
			}
	
		}catch(NoResultException ex){
			res=false;
		}
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean existChargeU(Long ChargeId, String nameCharge){
		boolean res=false;
		try{
			System.out.print("nombre del cargo: " + nameCharge);
			System.out.print("id del cargo: " + ChargeId);
			Query q= em.createNativeQuery("select charge_id from tb_charges where charge_id <>?2 and (upper(charge_description)=upper(?1))");
			q.setParameter(1, nameCharge);
			q.setParameter(2, ChargeId);
			List<Object> list= new ArrayList<Object>();
			list=q.getResultList();
			System.out.println("lista" + list);
			if(list!=null){
				if(list.size()>0){
					res=true;
				}
				else{
					res=false;
				}
			}
			else{
				res=false;
			}
	
		}catch(NoResultException ex){
			res=false;
		}
		return res;
	}

	@Override
	public String nameById(Long chargeId) {
		try{
			TbCharges ch = em.find(TbCharges.class, chargeId);
			if(ch != null){
				return ch.getChargeDescription();
			}else{
				return null;
			}
		}catch(NoResultException e){
			e.printStackTrace();
			return null;
		}
	}
	
	
	@Override
	public List<TbPaymentMethod> getPaymentMethod() {
		List<TbPaymentMethod> list = new ArrayList<TbPaymentMethod>();
		try {
			Query q = em.createQuery("SELECT ct FROM TbPaymentMethod ct");
			for (Object obj : q.getResultList()) {
				list.add((TbPaymentMethod) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en ChargesEJB.getPaymentMethod ] ");
			e.printStackTrace();
		}
		return list;
	}

	
	@Override
	public List<TbChanel> getChanel() {
		List<TbChanel> list = new ArrayList<TbChanel>();
		try {
			Query q = em.createQuery("SELECT ct FROM TbChanel ct");
			for (Object obj : q.getResultList()) {
				list.add((TbChanel) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en ChargesEJB.TbChanel ] ");
			e.printStackTrace();
		}
		return list;
	}
	
	@Override
	public List<TbAgreement> getAgreement() {
		List<TbAgreement> list = new ArrayList<TbAgreement>();
		try {
			Query q = em.createQuery("SELECT ct FROM TbAgreement ct");
			for (Object obj : q.getResultList()) {
				list.add((TbAgreement) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en ChargesEJB.TbAgreement ] ");
			e.printStackTrace();
		}
		return list;
	}
	
	
	@Override
	public List<TbStateRecharge> getStateRecharge() {
		List<TbStateRecharge> list = new ArrayList<TbStateRecharge>();
		try {
			Query q = em.createQuery("SELECT ct FROM TbStateRecharge ct where stateRecharge=1 ");
			for (Object obj : q.getResultList()) {
				list.add((TbStateRecharge) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en ChargesEJB.TbStateRecharge ] ");
			e.printStackTrace();
		}
		return list;
	}
	
}
