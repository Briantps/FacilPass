package ejb.crud;
import java.util.List;

import javax.ejb.Remote;

import jpa.TbPoint;

@Remote
public interface Point {
	
	/**
	 * 
	 * @return {@link TbPoint} {@link List}
	 */
	public List<TbPoint> getPoints();
	
	/**
	 * 
	 * @param pointIp
	 * @param pointPort
	 * @param ip
	 * @param creationUser
	 * @return {@link Long} -1 if the point exits, null if error, 0 if successful.
	 */
	public Long insertPoint(String pointIp, Integer pointPort,String pointName, String ip,
			Long creationUser);
	
	/**
	 * @param pointId
	 * @param pointIp
	 * @param pointPort
	 * @param ip
	 * @param creationUser
	 * @return <code>true</code> if successful, <code>false</code> otherwise.
	 */
	public boolean editPoint(Long pointId, String pointIp, Integer pointPort, String pointName, String ip,
			Long creationUser);
	
	/**
	 * 
	 * @param userId
	 * @return {@link TbPoint} {@link List}
	 */
	public List<TbPoint> getUserPoints(Long userId);
	
	/**
	 * 
	 * @param userId
	 * @return {@link TbPoint} {@link List}
	 */
	public List<TbPoint> getAvailablePoints(Long userId);
	
	/**
	 * 
	 * @param userId
	 * @param pointId
	 * @param creationUser
	 * @param ip
	 * @return <code>true</code> if successful, <code>false</code> otherwise.
	 */
	public boolean asignPointToClient(Long userId, Long pointId,
			Long creationUser, String ip);
	
	/**
	 * 
	 * @param userId
	 * @param pointId
	 * @param creationUser
	 * @param ip
	 * @return <code>true</code> if successful, <code>false</code> otherwise.
	 */
	public boolean removeClientPoint(Long userId, Long pointId,
			Long creationUser, String ip);
	
	/**
	 * 
	 * @param idUser
	 * @param ip
	 * @return {@link TbPoint}
	 */
	public TbPoint getUserPoint(Long idUser, String ip);
}