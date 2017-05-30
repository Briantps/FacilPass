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

import jpa.TbWarehouseDependency;

import crud.ObjectEM;

import ejb.Log;

/**
 * Session Bean implementation class WarehouseDependencyEJB
 */
@Stateless(mappedName = "ejb/WarehouseDependency")
public class WarehouseDependencyEJB implements WarehouseDependency {
	
	@PersistenceContext(unitName = "bo")
	private EntityManager em;
	
	@EJB(mappedName = "ejb/Log")
	private Log log;
	
	private ObjectEM emObj;

    /**
     * Default constructor. 
     */
    public WarehouseDependencyEJB() {
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ejb.crud.WarehouseDependency#editDependency(java.lang.Long,
	 * java.lang.String, java.lang.String, java.lang.Long)
	 */
	@Override
	public boolean editDependency(Long dependencyId, String dependency,
			String ip, Long creationUser) {
		try {	
			TbWarehouseDependency wh = em.find(TbWarehouseDependency.class, dependencyId);
			String old = wh.getWarehouseDependencyName();
			wh.setWarehouseDependencyName(dependency.toUpperCase());
			
			//update
			emObj = new ObjectEM(em);
			if(emObj.update(wh)){
				log.insertLog("Editar Dependencia de Almacén | Se ha actualizado la dependencia ID: " + wh.getWarehouseDependencyId() 
						+ ". Antes: " + old + ".",
						LogReference.WAREHOUSEDEPENDENCY, LogAction.UPDATE, ip, creationUser);
				return true;
			} else {
				log.insertLog("Editar Dependencia de Almacén | No Se ha podido actualizar ID: " +
						wh.getWarehouseDependencyId() + " a : "+ dependency + ".",
						LogReference.WAREHOUSEDEPENDENCY, LogAction.UPDATEFAIL, ip, creationUser);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en WarehouseDependencyEJB.editDependency. ] ");
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.crud.WarehouseDependency#getWarehouseDependecy()
	 */
	@Override
	public List<TbWarehouseDependency> getWarehouseDependecy() {
		List<TbWarehouseDependency> list = new ArrayList<TbWarehouseDependency>();
		try {
			Query q = em.createQuery("SELECT wd FROM TbWarehouseDependency wd ORDER BY wd.warehouseDependencyName");
			for (Object obj : q.getResultList()) {
				list.add((TbWarehouseDependency) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en WarehouseDependencyEJB.getWarehouseDependecy. ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.crud.WarehouseDependency#insertDependency(java.lang.String,
	 * java.lang.String, java.lang.Long)
	 */
	@Override
	public Long insertDependency(String dependency, String ip, Long creationUser) {
		try {
			Query q = em.createQuery("SELECT wd FROM TbWarehouseDependency wd WHERE wd.warehouseDependencyName = ?1");
			q.setParameter(1, dependency.toUpperCase());
			q.getSingleResult();
			
			return -1L;
		} catch (NoResultException e) {
			// creating the warehouse dependency.
			TbWarehouseDependency wd = new TbWarehouseDependency();
			wd.setWarehouseDependencyName(dependency.toUpperCase());
			
			emObj = new ObjectEM(em);
			if(emObj.create(wd)){
				log.insertLog("Creación de Dependencia de Alamcén | Se ha creado la dependencia ID: " + wd.getWarehouseDependencyId() + ".",
						LogReference.WAREHOUSEDEPENDENCY, LogAction.CREATE, ip, creationUser);
				return 0L;
			} else {
				log.insertLog("Creación de Dependencia de Alamcén | No se pudo crear la  dependencia:  " + dependency + ".",
						LogReference.WAREHOUSEDEPENDENCY, LogAction.CREATEFAIL, ip, creationUser);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en WarehouseDependencyEJB.insertDependency. ] ");
			e.printStackTrace();
		}
		return null;
	}
}
