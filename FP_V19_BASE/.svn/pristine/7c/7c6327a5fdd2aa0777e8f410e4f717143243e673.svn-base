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

import jpa.ReUserPoint;
import jpa.TbPoint;
import jpa.TbSystemUser;

import crud.ObjectEM;

import ejb.Log;

/**
 * Session Bean implementation class PointEJB
 */
@Stateless(mappedName = "ejb/Point")
public class PointEJB implements Point {
	
	@PersistenceContext(unitName = "bo")
	private EntityManager em;
	
	@EJB(mappedName = "ejb/Log")
	private Log log;
	
	private ObjectEM emObj;

    /**
     * Default constructor. 
     */
    public PointEJB() {
    }
    
	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.crud.Point#getPoints()
	 */
	@Override
	public List<TbPoint> getPoints() {
		List<TbPoint> list = new ArrayList<TbPoint>();
		try {
			Query q = em.createQuery("SELECT po FROM TbPoint po");
			for (Object obj : q.getResultList()) {
				list.add((TbPoint) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en PointEJB.getPoints. ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.crud.Point#editPoint(java.lang.Long, java.lang.String,
	 * java.lang.Integer, java.lang.String, java.lang.Long)
	 */
	@Override
	public boolean editPoint(Long pointId, String pointIp, Integer pointPort, String pointName,
			String ip, Long creationUser) {
		try {	
			String logMessage = "";
			
			//Searching the point.
			TbPoint previousPoint = em.find(TbPoint.class, pointId);
			
			if (!pointIp.equals(previousPoint.getPointIp())) {
				logMessage += " Se ha cambiado la IP. Anterior IP: " + previousPoint.getPointIp() + 
				" Nueva IP: " + pointIp + ". ";
				previousPoint.setPointIp(pointIp);
			}
			
			if (pointPort != previousPoint.getPointPort()) {
				logMessage += " Se ha cambiado el Puerto. Anterior Puerto: " + previousPoint.getPointPort() + 
				" Nuevo Puerto: " + pointPort + ". ";
				previousPoint.setPointPort(pointPort);
			}
			
			if (!pointName.toUpperCase().equals(previousPoint.getPointName())) {
				logMessage += " Se ha cambiado el Nombre. Anterior Nombre: " + previousPoint.getPointName() + 
				" Nuevo Puerto: " + pointName + ". ";
				previousPoint.setPointName(pointName);
			}
			
			//update
			emObj = new ObjectEM(em);
			if(emObj.update(previousPoint)){
				log.insertLog("Editar Parámetros Sistemas de Recarga | Se ha actualizado l registro ID: " + previousPoint.getPointId() 
						+ logMessage,	LogReference.ADMINPARAMETER, LogAction.UPDATE, ip, creationUser);
				return true;
			} else {
				log.insertLog("Editar Parámetros Sistemas de Recarga | No Se ha podido actualizar el registro ID: " + previousPoint.getPointId() 
						+ " a  Ip, Puerto y Nombre: "+ pointIp + " - " + pointPort +" - "+pointName+".",
						LogReference.ADMINPARAMETER, LogAction.UPDATEFAIL, ip, creationUser);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en PointEJB.editPoint. ] ");
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.crud.Point#insertPoint(java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.Long)
	 */
	@Override
	public Long insertPoint(String pointIp, Integer pointPort,String pointName, String ip,
			Long creationUser) {
		try {
			Query q = em.createQuery("SELECT po FROM TbPoint po WHERE po.pointIp = ?1 " +
					" AND po.pointPort = ?2");
			q.setParameter(1, pointIp);
			q.setParameter(2, pointPort);
			q.getSingleResult();
			
			return -1L;
		} catch (NoResultException e) {
			// creating the point
			TbPoint po = new TbPoint();
			po.setPointIp(pointIp);
			po.setPointPort(pointPort);
			po.setPointName(pointName.toUpperCase());
			
			emObj = new ObjectEM(em);
			
			if(emObj.create(po)){
				log.insertLog("Sistema de Recarga | Se ha creado el registro ID: " + po.getPointId() + ".",
						LogReference.ADMINPARAMETER, LogAction.CREATE, ip, creationUser);
				return 0L;
			} else {
				log.insertLog("Sistema de Recarga | No se pudo crear el registro IP: " + pointIp + ". Puerto: " +  pointPort +  ".",
						LogReference.ADMINPARAMETER, LogAction.CREATEFAIL, ip, creationUser);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en PointEJB.insertPoint. ] ");
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see ejb.crud.Point#getUserPoints(java.lang.Long)
	 */
	@Override
	public List<TbPoint> getUserPoints(Long userId) {
		List<TbPoint> list = new ArrayList<TbPoint>();
		try {
			Query q = em.createQuery("SELECT up.tbPoint FROM ReUserPoint up WHERE up.tbSystemUser.userId = ?1 ");
			q.setParameter(1, userId);
			for(Object obj :  q.getResultList()) {
				list.add((TbPoint) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en PointEJB.getUserPoints. ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see ejb.crud.Point#getAvailablePoints(java.lang.Long)
	 */
	@Override
	public List<TbPoint> getAvailablePoints(Long userId) {
		List<TbPoint> list = new ArrayList<TbPoint>();
		try {
			Query q = em.createQuery("SELECT po FROM TbPoint po WHERE po.pointId NOT IN " +
						" (SELECT up.tbPoint.pointId FROM ReUserPoint up WHERE up.tbSystemUser.userId = ?1 )");
			q.setParameter(1, userId);
			for(Object obj :  q.getResultList()) {
				list.add((TbPoint) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en PointEJB.getAvailablePoints. ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.crud.Point#asignPointToClient(java.lang.Long, java.lang.Long,
	 * java.lang.Long, java.lang.String)
	 */
	@Override
	public boolean asignPointToClient(Long userId, Long pointId,
			Long creationUser, String ip) {
		try {
			ReUserPoint up = new ReUserPoint();
			up.setTbPoint(em.find(TbPoint.class, pointId));
			up.setTbSystemUser(em.find(TbSystemUser.class, userId));
			
			emObj = new ObjectEM(em);
			
			if(emObj.create(up)){
				log.insertLog("Asignar Punto-Cliente | Se ha asigando el punto ID: " + pointId + " y el usuario ID: " + userId + ".", 
						LogReference.RECHARGEPOINT, LogAction.CREATE, ip, creationUser);
				return true;
			} else {
				log.insertLog("Asignar Punto-Cliente | No se ha asignado el punto ID: " + pointId + " y el usuario ID: " + userId + ".",
						LogReference.RECHARGEPOINT, LogAction.CREATEFAIL, ip, creationUser);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en PointEJB.asignPointToClient. ] ");
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see ejb.crud.Point#removeClientPoint(java.lang.Long, java.lang.Long, java.lang.Long, java.lang.String)
	 */
	@Override
	public boolean removeClientPoint(Long userId, Long pointId,
			Long creationUser, String ip) {
		try {
			
			Query q = em.createQuery("SELECT up FROM ReUserPoint up WHERE up.tbPoint.pointId = ?1" +
					" AND up.tbSystemUser.userId = ?2 ");
			q.setParameter(1, pointId);
			q.setParameter(2, userId);
			
			ReUserPoint up = (ReUserPoint) q.getSingleResult();
			
			emObj = new ObjectEM(em);
			
			if(emObj.delete(up)){
				log.insertLog("Remover Asignación Punto-Cliente | Se ha quitado la asignación del punto ID: " + pointId + " y el usuario ID: " + userId + ".", 
						LogReference.RECHARGEPOINT, LogAction.DELETE, ip, creationUser);
				return true;
			} else {
				log.insertLog("Remover Asignación Punto-Cliente | No se ha quitado la asignación del punto ID: " + pointId + " y el usuario ID: " + userId + ".",
						LogReference.RECHARGEPOINT, LogAction.DELETEFAIL, ip, creationUser);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en PointEJB.removeClientPoint. ] ");
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.crud.Point#getUserPoint(java.lang.Long, java.lang.String)
	 */
	@Override
	public TbPoint getUserPoint(Long idUser, String ip) {
		try {
			Query q = em.createQuery("SELECT rup FROM ReUserPoint rup WHERE rup.tbSystemUser.userId = ?1 AND " +
					" rup.tbPoint.pointIp =  ?2").setMaxResults(1);
			q.setParameter(1, idUser);
			q.setParameter(2, ip);
			
			ReUserPoint rup = (ReUserPoint) q.getSingleResult();
			return rup.getTbPoint();
		}catch (NoResultException ne) {
			log.insertLog("Obtener IP Punto Recarga/Consulta | No se encontró un punto con la Ip y usuario ingresados. ",
					LogReference.RECHARGEPOINT, LogAction.ACCESSDENIED, ip, idUser);
		} catch (Exception e) {
			System.out.println(" [ Error en PointEJB.getUserPoint. ] ");
			e.printStackTrace();
		}
		return null;
	}
}