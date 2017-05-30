package ejb;
import javax.ejb.Remote;

import constant.LogAction;
import constant.LogReference;

@Remote
public interface Log {
	
	/**
	 * 
	 * @param logMessage Message Log.
	 * @param logReference Subject Log makes a reference.
	 * @param logAction Action committed. 
	 * @param logIp  IP host transaction is made.
	 * @param user System User.
	 */
	public void insertLog(String logMessage, LogReference logReference,
			LogAction logAction, String logIp, Long user);
	
	public void insertLogVehicle(String nameFile,Long method,String row,String field,String value,
			String description,String solution,Long userId,Long state);

	public void insertLogTag(String nameFile, Long rowCount, String row, String field,
			String value, String description, String solution, Long userId);

}