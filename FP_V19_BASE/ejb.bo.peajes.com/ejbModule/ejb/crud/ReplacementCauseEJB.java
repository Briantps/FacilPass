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

import jpa.TbReplacementCause;

import crud.ObjectEM;

import ejb.Log;

/**
 * Session Bean implementation class ReplacementCauseEJB
 */
@Stateless(mappedName = "ejb/ReplacementCause")
public class ReplacementCauseEJB implements ReplacementCause {
	
	@PersistenceContext(unitName = "bo")
	private EntityManager em;
	
	@EJB(mappedName = "ejb/Log")
	private Log log;
	
	private ObjectEM emObj;

    /**
     * Default constructor. 
     */
    public ReplacementCauseEJB() {
    }

    /*
     * (non-Javadoc)
     * @see ejb.crud.ReplacementCause#getReplacementCauses()
     */
	@Override
	public List<TbReplacementCause> getReplacementCauses() {
		 List<TbReplacementCause> list = new ArrayList<TbReplacementCause>();
		try {
			Query q = em.createQuery("SELECT rc FROM TbReplacementCause rc");
			for (Object obj : q.getResultList()) {
				list.add((TbReplacementCause) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en ReplacementCauseEJB.getReplacementCauses. ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see ejb.crud.ReplacementCause#insertCause(java.lang.String, java.lang.String, java.lang.Long)
	 */
	@Override
	public Long insertCause(String cause, String ip, Long creationUser) {
		try {
			Query q = em.createQuery("SELECT rc FROM TbReplacementCause rc WHERE rc.replacementCauseName = ?1");
			q.setParameter(1, cause.toUpperCase());
			q.getSingleResult();
			
			return -1L;
		} catch (NoResultException e) {
			// creating the installation location.
			TbReplacementCause rc = new TbReplacementCause();
			rc.setReplacementCauseName(cause.toUpperCase());
			
			emObj = new ObjectEM(em);
			if(emObj.create(rc)){
				log.insertLog("Creación de Causa de Reposición | Se ha creado la causa de reposición ID: " + rc.getReplacementCauseId() + ".",
						LogReference.REPLACEMENTCAUSE, LogAction.CREATE, ip, creationUser);
				return 0L;
			} else {
				log.insertLog("Creación de Causa de Reposición | No se pudo crear la causa de reposición: " + cause + ".",
						LogReference.REPLACEMENTCAUSE, LogAction.CREATEFAIL, ip, creationUser);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en ReplacementCauseEJB.insertCause. ] ");
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see ejb.crud.ReplacementCause#editCause(java.lang.Long, java.lang.String, java.lang.String, java.lang.Long)
	 */
	@Override
	public boolean editCause(Long causeId, String cause, String ip, Long creationUser) {
		try {	
			TbReplacementCause rc = em.find(TbReplacementCause.class, causeId);
			String old = rc.getReplacementCauseName();
			rc.setReplacementCauseName(cause.toUpperCase());
			
			//update
			emObj = new ObjectEM(em);
			if(emObj.update(rc)){
				log.insertLog("Editar Causa de Reposición | Se ha actualizado la causa de reposición ID: " + rc.getReplacementCauseId() 
						+ ". Antes: " + old + ".",
						LogReference.REPLACEMENTCAUSE, LogAction.UPDATE, ip, creationUser);
				return true;
			} else {
				log.insertLog("Editar Causa de Reposición | No Se ha podido actualizar la causa de reposición ID: " + rc.getReplacementCauseId() + " a : "+ cause + ".",
						LogReference.REPLACEMENTCAUSE, LogAction.UPDATEFAIL, ip, creationUser);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en ReplacementCauseEJB.editCause. ] ");
			e.printStackTrace();
		}
		return false;
	}
}