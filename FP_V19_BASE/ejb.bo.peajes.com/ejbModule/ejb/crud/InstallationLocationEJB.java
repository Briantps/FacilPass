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

import jpa.TbInstallationLocation;

import crud.ObjectEM;

import ejb.Log;

/**
 * Session Bean implementation class InstallationLocationEJB
 */
@Stateless(mappedName = "ejb/InstallationLocation")
public class InstallationLocationEJB implements InstallationLocation {
	
	@PersistenceContext(unitName = "bo")
	private EntityManager em;
	
	@EJB(mappedName = "ejb/Log")
	private Log log;
	
	private ObjectEM emObj;

    /**
     * Default constructor. 
     */
    public InstallationLocationEJB() {
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ejb.crud.InstallationLocation#getInstallationLocations()
	 */
	@Override
	public List<TbInstallationLocation> getInstallationLocations() {
		 List<TbInstallationLocation> list = new ArrayList<TbInstallationLocation>();
		try {
			Query q = em.createQuery("SELECT il FROM TbInstallationLocation il");
			for (Object obj : q.getResultList()) {
				list.add((TbInstallationLocation) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en InstallationLocationEJB.getInstallationLocations. ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see ejb.crud.InstallationLocation#insertLocation(java.lang.String, java.lang.String, java.lang.Long)
	 */
	@Override
	public Long insertLocation(String location, String ip, Long creationUser) {
		try {
			
			Query q = em.createQuery("SELECT il FROM TbInstallationLocation il WHERE il.installationLocationName = ?1");
			q.setParameter(1, location.toUpperCase());
			q.getSingleResult();
			
			return -1L;
		} catch (NoResultException e) {
			// creating the installation location.
			TbInstallationLocation il = new TbInstallationLocation();
			il.setInstallationLocationName(location.toUpperCase());
			
			emObj = new ObjectEM(em);
			if(emObj.create(il)){
				log.insertLog("Creación de Ubicación de Instalación | Se ha creado la ubicación ID: " + il.getInstallationLocationId() + ".",
						LogReference.INSTALLATIONLOCATION, LogAction.CREATE, ip, creationUser);
				return 0L;
			} else {
				log.insertLog("Creación de Ubicación de Instalación | No se pudo crear la ubicación de instalación: " + location + ".",
						LogReference.INSTALLATIONLOCATION, LogAction.CREATEFAIL, ip, creationUser);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en InstallationLocationEJB.insertLocation. ] ");
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.crud.InstallationLocation#editLocation(java.lang.Long,
	 * java.lang.String, java.lang.String, java.lang.Long)
	 */
	@Override
	public boolean editLocation(Long locationId, String location, String ip, Long creationUser) {
		try {	
			TbInstallationLocation il = em.find(TbInstallationLocation.class, locationId);
			String old =  il.getInstallationLocationName();
			il.setInstallationLocationName(location.toUpperCase());
			
			//update
			emObj = new ObjectEM(em);
			if(emObj.update(il)){
				log.insertLog("Editar Ubicación de Instalación | Se ha actualizado la ubicación ID: " + il.getInstallationLocationId() 
						+ ". Antes: " + old + ".",
						LogReference.INSTALLATIONLOCATION, LogAction.UPDATE, ip, creationUser);
				return true;
			} else {
				log.insertLog("Editar Ubicación de Instalación | No Se ha podido actualizar ID: " + il.getInstallationLocationId() + " a : "+ location + ".",
						LogReference.INSTALLATIONLOCATION, LogAction.UPDATEFAIL, ip, creationUser);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en InstallationLocationEJB.editLocation. ] ");
			e.printStackTrace();
		}
		return false;
	}
}
