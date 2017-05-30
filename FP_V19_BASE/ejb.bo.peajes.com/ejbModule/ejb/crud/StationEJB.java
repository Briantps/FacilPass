package ejb.crud;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import constant.LogAction;
import constant.LogReference;

import jpa.TbCompany;
import jpa.TbDepartment;
import jpa.TbLane;
import jpa.TbStationBO;
import jpa.TbStationType;

import crud.ObjectEM;

import ejb.Log;

/**
 * Session Bean implementation class StationEJB
 */
@Stateless(mappedName = "ejb/Station")
public class StationEJB implements Station {
	
	@PersistenceContext(unitName = "bo")
	private EntityManager em;
	
	@EJB(mappedName = "ejb/Log")
	private Log log;
	
	private ObjectEM emObj;

    /**
     * Default constructor. 
     */
    public StationEJB() {
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ejb.crud.Station#insertStation(java.lang.String, java.lang.String,
	 * java.lang.Long, java.lang.Long, java.lang.String, java.lang.Long, java.lang.Long)
	 */
	@Override
	public String insertStation(String stationCode, String stationName,
			Long companyId, Long departmentId, String ip, Long creationUser,
			Long stationTypeId, Long numberLanes) {
		try {
			Query q = em
					.createQuery("SELECT ts FROM TbStationBO ts WHERE ts.stationBOCode = ?1");
			q.setParameter(1, stationCode.toUpperCase());
			q.getSingleResult();
			return "Ya Existe Una estación con el Código Ingresado. Verifique.";
		} catch (NonUniqueResultException e) {
			return "Ya Existe Una estación con el Código Ingresado. Verifique.";
		} catch (NoResultException e) {
			try {
				Query q = em
				.createQuery("SELECT ts FROM TbStationBO ts WHERE ts.stationBOName = ?1");
				q.setParameter(1, stationName.toUpperCase());
				q.getSingleResult();
				
				return "Ya Existe Una estación con El Nombre Ingresado. Verifique.";
			} catch (NonUniqueResultException eur) {
				return "Ya Existe Una estación con El Nombre Ingresado. Verifique.";
			} catch (NoResultException nre) {
				// creating the station.
				TbStationBO ts = new TbStationBO();
				ts.setStationBOCode(stationCode.toUpperCase());
				ts.setStationBOName(stationName.toUpperCase());
				ts.setTbStationType(em.find(TbStationType.class, stationTypeId));
				ts.setTbCompany(em.find(TbCompany.class, companyId));
				ts.setTbDepartment(em.find(TbDepartment.class, departmentId));
				ts.setNumberLanes(numberLanes);
				
				emObj = new ObjectEM(em);
				
				if(emObj.create(ts)){
					log.insertLog("Creación de Estación | Se ha creado La Estación ID: " + ts.getStationBOId() + ".",
							LogReference.STATIONBO, LogAction.CREATE, ip, creationUser);
					
					return "La estación se ha creado con éxito.";
				} else {
					log.insertLog("Creación de Estación | No se pudo crear La Estación: " + stationName + ".",
							LogReference.STATIONBO, LogAction.CREATEFAIL, ip, creationUser);
     			}
			}
		} catch (Exception e) {
			System.out.println(" [ Error en StationEJB.insertStation. ] ");
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.crud.Station#getStationList()
	 */
	@Override
	public List<TbStationBO> getStationList() {
		List<TbStationBO> list = new ArrayList<TbStationBO>();
		try {
			Query q = em.createQuery("SELECT ts FROM TbStationBO ts  " +
					" ORDER BY ts.stationBOName");
			for( Object obj: q.getResultList()) {
				list.add((TbStationBO) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en StationEJB.getStationList. ] ");
			e.printStackTrace();
		}
		return list;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.crud.Station#getStationList()
	 */
	@Override
	public List<TbStationBO> getStationListByCompany(Long companyId) {
		List<TbStationBO> list = new ArrayList<TbStationBO>();
		try {
			Query q = em.createQuery("SELECT ts FROM TbStationBO ts where tbCompany.companyId=?1 " +
					" ORDER BY ts.stationBOName").setParameter(1, companyId);
			for( Object obj: q.getResultList()) {
				list.add((TbStationBO) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en StationEJB.getStationListByCompany. ] ");
			e.printStackTrace();
		}
		return list;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.crud.Station#getStationList()
	 */
	@Override
	public List<TbLane> getLaneListByStation(Long stationId) {
		List<TbLane> list = new ArrayList<TbLane>();
		try {
			Query q = em.createQuery("SELECT tl FROM TbLane tl where tl.tbStation.stationBOId=?1 " +
					" ORDER BY tl.laneCode").setParameter(1, stationId);
			for( Object obj: q.getResultList()) {
				list.add((TbLane) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en StationEJB.getLaneListByStation. ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.crud.Station#getStationType()
	 */
	@Override
	public List<TbStationType> getStationType() {
		List<TbStationType> list = new ArrayList<TbStationType>();
		try {
			Query q = em.createQuery("SELECT st FROM TbStationType st");
			for (Object o : q.getResultList()) {
				list.add((TbStationType) o);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en StationEJB.getStationType. ] ");
			e.printStackTrace();
		}
		return list;
	}

}