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

import jpa.TbSpecialExemptOffice;
import jpa.TbSpecialExemptType;

import crud.ObjectEM;

import ejb.Log;

/**
 * Session Bean implementation class ExemptOfficeEJB
 */
@Stateless(mappedName = "ejb/ExemptOffice")
public class ExemptOfficeEJB implements ExemptOffice {
	
	@PersistenceContext(unitName = "bo")
	private EntityManager em;
	
	@EJB(mappedName = "ejb/Log")
	private Log log;
	
	private ObjectEM emObj;

    /**
     * Default constructor. 
     */
    public ExemptOfficeEJB() {
    }

   /*
    * (non-Javadoc)
    * @see ejb.crud.ExemptOffice#getEspecialExemptOffice()
    */
	@Override
	public List<TbSpecialExemptOffice> getEspecialExemptOffice() {
		 List<TbSpecialExemptOffice> list = new ArrayList<TbSpecialExemptOffice>();
		try {
			Query q = em.createQuery("SELECT se FROM TbSpecialExemptOffice se ORDER BY " +
					" se.tbSpecialExemptType.specialExemptTypeId");
			for (Object obj : q.getResultList()) {
				list.add((TbSpecialExemptOffice) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en ExemptOfficeEJB.getEspecialExemptOffice. ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.crud.ExemptOffice#insertTbSpecialExemptOffice(java.lang.String,
	 * java.lang.String, java.lang.Long, java.lang.Long)
	 */
	@Override
	public Long insertTbSpecialExemptOffice(String officeName, String ip, Long creationUser, 
			Long exemptTypeId, String authoreizedBy, String officeAddress, String officeEmail,
			String officeFax, String officePhone) {
		try {
			Query q = em.createQuery("SELECT se FROM TbSpecialExemptOffice se WHERE se.officeName = ?1" +
					" AND se.tbSpecialExemptType.specialExemptTypeId = ?2");
			q.setParameter(1, officeName.toUpperCase());
			q.setParameter(2, exemptTypeId);
			q.getSingleResult();
			
			return -1L;
		} catch (NoResultException e) {
			// creating
			TbSpecialExemptOffice se = new TbSpecialExemptOffice();
			se.setOfficeName(officeName.toUpperCase());
			se.setTbSpecialExemptType(em.find(TbSpecialExemptType.class, exemptTypeId));
			se.setAuthoreizedBy(authoreizedBy);
			se.setOfficeAddress(officeAddress);
			se.setOfficeEmail(officeEmail);
			se.setOfficeFax(officeFax);
			se.setOfficePhone(officePhone);
			
			emObj = new ObjectEM(em);
			if(emObj.create(se)){
				log.insertLog("Creación de Dependencia de Exento | Se ha creado el registro ID: " + 
						se.getSpecialExemptOfficeId() + ".",
						LogReference.SPECIALEXEMPTOFFICE, LogAction.CREATE, ip, creationUser);
				return 0L;
			} else {
				log.insertLog("Creación de Dependencia de Exento | No se pudo crear la dependencia de exento: " + officeName + ".",
						LogReference.SPECIALEXEMPTOFFICE, LogAction.CREATEFAIL, ip, creationUser);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en ExemptOfficeEJB.insertTbSpecialExemptOffice. ] ");
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.crud.ExemptOffice#editTbSpecialExemptOffice(java.lang.String,
	 * java.lang.String, java.lang.Long, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean editTbSpecialExemptOffice(String officeName, String ip,
			Long creationUser, String authoreizedBy, String officeAddress,
			String officeEmail, String officeFax, String officePhone, Long officeId) {
		try {	
			TbSpecialExemptOffice se =  em.find(TbSpecialExemptOffice.class, officeId);
			String old =  se.getOfficeName();
			se.setOfficeName(officeName.toUpperCase());
			se.setAuthoreizedBy(authoreizedBy);
			se.setOfficeAddress(officeAddress);
			se.setOfficeEmail(officeEmail);
			se.setOfficeFax(officeFax);
			se.setOfficePhone(officePhone);
			
			//update
			emObj = new ObjectEM(em);
			if(emObj.update(se)){
				log.insertLog("Editar Dependencia de Exento  | Se ha actualizado la dependencia ID: " + se.getSpecialExemptOfficeId() + "" +
						". Antes: " + old + ".",
						LogReference.SPECIALEXEMPTOFFICE, LogAction.UPDATE, ip, creationUser);
				return true;
			} else {
				log.insertLog("Editar Dependencia de Exento  | No Se pudo actualizar ID: " + se.getSpecialExemptOfficeId() +  ".",
						LogReference.SPECIALEXEMPTOFFICE, LogAction.UPDATEFAIL, ip, creationUser);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en ExemptOfficeEJB.editTbSpecialExemptOffice. ] ");
			e.printStackTrace();
		}
		return false;
	}
}