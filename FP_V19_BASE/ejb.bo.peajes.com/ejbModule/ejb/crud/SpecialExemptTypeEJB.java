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

import jpa.TbDeviceType;
import jpa.TbSpecialExemptType;

import crud.ObjectEM;

import ejb.Log;

/**
 * Session Bean implementation class SpecialExemptTypeEJB
 */
@Stateless(mappedName = "ejb/SpecialExemptType")
public class SpecialExemptTypeEJB implements SpecialExemptType {
	
	@PersistenceContext(unitName = "bo")
	private EntityManager em;
	
	@EJB(mappedName = "ejb/Log")
	private Log log;
	
	private ObjectEM emObj;

    /**
     * Default constructor. 
     */
    public SpecialExemptTypeEJB() {
    }

    /*
     * (non-Javadoc)
     * @see ejb.crud.SpecialExemptType#getEspecialExemptTypes()
     */
	@Override
	public List<TbSpecialExemptType> getEspecialExemptTypes() {
		 List<TbSpecialExemptType> list = new ArrayList<TbSpecialExemptType>();
		try {
			Query q = em.createQuery("SELECT se FROM TbSpecialExemptType se ORDER BY se.tbDeviceType.deviceTypeId");
			for (Object obj : q.getResultList()) {
				list.add((TbSpecialExemptType) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en SpecialExemptTypeEJB.getEspecialExemptTypes. ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ejb.crud.SpecialExemptType#insertTbSpecialExemptType(java.lang.String,
	 * java.lang.String, java.lang.Long, java.lang.Long)
	 */
	@Override
	public Long insertTbSpecialExemptType(String specialExemptType, String ip, Long creationUser, Long deviceTypeId) {
		try {
			Query q = em.createQuery("SELECT se FROM TbSpecialExemptType se WHERE se.specialExemptTypeName = ?1" +
					" AND se.tbDeviceType.deviceTypeId = ?2");
			q.setParameter(1, specialExemptType.toUpperCase());
			q.setParameter(2, deviceTypeId);
			q.getSingleResult();
			
			return -1L;
		} catch (NoResultException e) {
			// creating
			TbSpecialExemptType se = new TbSpecialExemptType();
			se.setSpecialExemptTypeName(specialExemptType.toUpperCase());
			se.setTbDeviceType(em.find(TbDeviceType.class, deviceTypeId));
			
			emObj = new ObjectEM(em);
			if(emObj.create(se)){
				log.insertLog("Creación de Tipo de Especial Exento | Se ha creado el registro ID: " + 
						se.getSpecialExemptTypeId() + ".",
						LogReference.SPECIALEXEMPTTYPE, LogAction.CREATE, ip, creationUser);
				return 0L;
			} else {
				log.insertLog("Creación de Tipo de Especial Exento  | No se pudo el tipo de especial exento: " + specialExemptType + ".",
						LogReference.SPECIALEXEMPTTYPE, LogAction.CREATEFAIL, ip, creationUser);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en SpecialExemptTypeEJB.insertTbSpecialExemptType. ] ");
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see ejb.crud.SpecialExemptType#getExemptTypes()
	 */
	@Override
	public List<TbSpecialExemptType> getExemptTypes() {
		 List<TbSpecialExemptType> list = new ArrayList<TbSpecialExemptType>();
			try {
				Query q = em.createQuery("SELECT se FROM TbSpecialExemptType se " +
						" WHERE  se.tbDeviceType.deviceTypeId = 4" +
						" ORDER BY se.tbDeviceType.deviceTypeId");
				for (Object obj : q.getResultList()) {
					list.add((TbSpecialExemptType) obj);
				}
			} catch (Exception e) {
				System.out.println(" [ Error en SpecialExemptTypeEJB.getExemptTypes. ] ");
				e.printStackTrace();
			}
			return list;
	}
}