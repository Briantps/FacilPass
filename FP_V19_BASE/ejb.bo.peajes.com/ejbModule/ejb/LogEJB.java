package ejb;

import java.sql.Timestamp;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import constant.LogAction;
import constant.LogReference;

import jpa.TbLog;
import jpa.TbLogVehicle;
import jpa.TbLogTag;
/**
 * Session Bean implementation class LogEJB
 */
@Stateless(mappedName="ejb/Log")
public class LogEJB implements Log {

	@PersistenceContext(unitName = "bo")
	private EntityManager em;
	
    /**
     * Default constructor. 
     */
    public LogEJB() {
    }

   /*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Log#insertLog(java.lang.String, constant.LogReference,
	 * constant.LogAction, java.lang.String, java.lang.Long)
	 */
	@Override
	public void insertLog(String logMessage, LogReference logReference,
			LogAction logAction, String logIp, Long user) {
		try {
			TbLog log = new TbLog();
			log.setLogMessage(logMessage);
			log.setLogReference(logReference.toString());
			log.setLogAction(logAction.toString());
			log.setLogDate(new Timestamp(System.currentTimeMillis()));
			log.setLogIp(logIp);
			log.setUserId(user);

			em.persist(log);
			em.flush();

		} catch (Exception e) {
			System.out.println("Error en LogEJB.insertLog");
			e.printStackTrace();
		}
	}

	@Override
	public void insertLogVehicle(String nameFile, Long method, String row,
			String field, String value, String description, String solution,
			Long userId,Long state) {
		try {
			TbLogVehicle log = new TbLogVehicle();
			log.setLogVehicleDate(new Timestamp(System.currentTimeMillis()));
			log.setLogVehicleDescription(description);
			log.setLogVehicleField(field);
			log.setLogVehicleFile(nameFile);
			log.setLogVehicleRow(Long.parseLong(row));
			log.setLogVehicleSolution(solution);
			log.setLogVehicleType(method);
			log.setLogVehicleValue(value);
			log.setUserId(userId);
			log.setLogVehicleState(state);
			
			em.persist(log);
			em.flush();

		} catch (Exception e) {
			System.out.println("Error en LogEJB.insertLogVehicle");
			e.printStackTrace();
		}
		
	}
	
	
	@Override
	public void insertLogTag(String nameFile, Long rowCount, String row,
			String field, String value, String description, String solution, Long userId) {
		try {
			TbLogTag log = new TbLogTag();
			log.setLogTagDate(new Timestamp(System.currentTimeMillis()));
			log.setLogTagDescription(description);
			log.setLogTagField(field);
			log.setLogTagRowCount(rowCount);
			log.setLogTagFile(nameFile);
			log.setLogTagRow(Long.parseLong(row));
			log.setLogTagSolution(solution);
			log.setLogTagValue(value);
			log.setUserId(userId);
			
			em.persist(log);
			em.flush();

		} catch (Exception e) {
			System.out.println("Error en LogEJB.insertLogTag");
			e.printStackTrace();
		}
		
	}
}