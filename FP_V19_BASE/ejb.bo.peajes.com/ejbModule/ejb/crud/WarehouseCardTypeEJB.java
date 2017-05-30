package ejb.crud;

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

import jpa.TbWarehouseCardType;

import crud.ObjectEM;

import ejb.Log;

/**
 * Session Bean implementation class WarehouseCardTypeEJB
 */
@Stateless(mappedName = "ejb/WarehouseCardType")
public class WarehouseCardTypeEJB implements WarehouseCardType {
	
	@PersistenceContext(unitName = "bo")
	private EntityManager em;
	
	@EJB(mappedName = "ejb/Log")
	private Log log;
	
	private ObjectEM emObj;

    /**
     * Default constructor. 
     */
    public WarehouseCardTypeEJB() {
    }
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.crud.WarehouseCardType#editCardType(java.lang.Long,
	 * java.lang.String, java.lang.String, java.lang.Long)
	 */
	@Override
	public boolean editCardType(Long cardTypeId, String cardType, String ip,
			Long creationUser) {
		try {	
			TbWarehouseCardType wh = em.find(TbWarehouseCardType.class, cardTypeId);
			String old = wh.getWarehouseCardTypeName();
			wh.setWarehouseCardTypeName(cardType.toUpperCase());
			
			//update
			emObj = new ObjectEM(em);
			if(emObj.update(wh)){
				log.insertLog("Editar Tipo de Tarjeta | Se ha actualizado el tipo de tarjeta ID: " + wh.getWarehouseCardTypeId()
						+ ". Antes: " + old + ".",
						LogReference.WAREHOUSECARDTYPE, LogAction.UPDATE, ip, creationUser);
				return true;
			} else {
				log.insertLog("Editar Tipo de Tarjeta | No Se ha podido actualizar ID: " +
						wh.getWarehouseCardTypeId() + " a : "+ cardType + ".",
						LogReference.WAREHOUSECARDTYPE, LogAction.UPDATEFAIL, ip, creationUser);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en WarehouseCardTypeEJB.editCardType. ] ");
			e.printStackTrace();
		}
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.crud.WarehouseCardType#getWarehouseCardType()
	 */
	@Override
	public List<TbWarehouseCardType> getWarehouseCardType() {
		List<TbWarehouseCardType> list = new ArrayList<TbWarehouseCardType>();
		try {
			Query q = em.createQuery("SELECT wt FROM TbWarehouseCardType wt ORDER BY wt.warehouseCardTypeId");
			for (Object obj : q.getResultList()) {
				list.add((TbWarehouseCardType) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en WarehouseCardTypeEJB.getWarehouseCardType. ] ");
			e.printStackTrace();
		}
		return list;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.crud.WarehouseCardType#insertCardType(java.lang.String,
	 * java.lang.String, java.lang.Long)
	 */
	@Override
	public Long insertCardType(String cardType, String ip, Long creationUser) {
		try {
			Query q = em.createQuery("SELECT wt FROM TbWarehouseCardType wt WHERE wt.warehouseCardTypeName = ?1");
			q.setParameter(1, cardType.toUpperCase());
			q.getSingleResult();
			
			return -1L;
		} catch (NoResultException e) {
			// creating card type.
			TbWarehouseCardType ct = new TbWarehouseCardType();
			ct.setWarehouseCardTypeName(cardType.toUpperCase());
			
			emObj = new ObjectEM(em);
			if(emObj.create(ct)){
				log.insertLog("Creación de Tipo de Tarjeta | Se ha creado el registro ID: " + ct.getWarehouseCardTypeId() + ".",
						LogReference.WAREHOUSECARDTYPE, LogAction.CREATE, ip, creationUser);
				return 0L;
			} else {
				log.insertLog("Creación de Tipo de Tarjeta | No se pudo crear el Tipo de Tarjeta:  " + cardType + ".",
						LogReference.WAREHOUSECARDTYPE, LogAction.CREATEFAIL, ip, creationUser);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en WarehouseCardTypeEJB.insertCardType. ] ");
			e.printStackTrace();
		}
		return null;
	}
}
