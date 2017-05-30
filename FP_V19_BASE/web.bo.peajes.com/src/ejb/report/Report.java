package ejb.report;
import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import util.warehouse.WarehouseTo;

import jpa.TbSystemUser;

@Remote
public interface Report {
	
	/**
	 * Gets user Data = Name + LastName
	 * @return
	 */
	public Object[][] getUserData();
	
	/**
	 * @return List of Device Codes.
	 */
	public List<String> getDeviceCodes();
	
	/**
	 * 
	 * @param begDate Beginning date.
	 * @param endDate End date.
	 * @param deviceCode Device Code.
	 * @return
	 */
	public  Object[][]  getTraceabilityByDeviceCode( Date begDate, Date endDate, String deviceCode);
	
	/**
	 * 
	 * @param begDate Beginning date.
	 * @param endDate End date.
	 * @param userCode Device Code.
	 * @return
	 */
	public  Object[][]  getUserStatementAccount( Date begDate, Date endDate, String userCode);
	
	/**
	 * @return List of User Codes (Users that have an Account).
	 */
	public List<String> getUserCodes();
	
	/**
	 * Gets an User
	 * @param userCode User Code
	 */
	public TbSystemUser getTbSystemUserByCode(String userCode);
	
	/**
	 * Info All Clients. 
	 * @return
	 */
	public Object[][] getAllClients();
	
	/**
	 * 
	 * @param deviceTypeId Device Type: special, exempt, etc.
	 * @param applyCondition Indicates if apply the deviceTypeId on where clause.  
	 * @return Info of Device Customization. 
	 */
	public Object[][] getMasterByType(Long deviceTypeId, boolean applyCondition);
	
	/**
	 * 
	 * @param initDate
	 * @param endDate
	 * @param warehouseStateId
	 * @return
	 */
	public Object[][] getEntryWarehouseByDate(Date initDate, Date endDate,
			Long warehouseStateId);
	
	/**
	 * 
	 * @param entryNumber
	 * @return
	 */
	public WarehouseTo getEntryWarehouseByNumber(String entryNumber);
	
	/**
	 * 
	 * @param begDate Beginning date.
	 * @param endDate End date.
	 * @param plate
	 * @return
	 */
	public  Object[][]  getTraceabilityByPlate( Date begDate, Date endDate, String plate);
	
	/**
	 * 
	 * @param fecIni Beginning date.
	 * @param fecFin End date. 
	 * @return
	 */
	public Object[][] getClearing(Date fecIni, Date fecFin);
	
	public boolean getExitDataForReportTrasability(String deviceCode, Date begDate, Date endDate, Long user);
	
	public Object[][] getTraceabilityByPlateClient(Date begDate, Date endDate,String plate, Long user);
}