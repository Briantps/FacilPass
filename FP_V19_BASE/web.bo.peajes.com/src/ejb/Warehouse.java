package ejb;

import java.util.List;

import javax.ejb.Remote;

import constant.WarehouseOperationType;

import util.warehouse.WarehouseTo;

import jpa.TbWarehouse;
import jpa.TbWarehouseState;

@Remote
public interface Warehouse {

	/**
	 * 
	 * @param ip
	 * @param creationUser
	 * @param cardQuantity
	 * @param entryNumber
	 *@param isCard
	 * @return
	 */
	public TbWarehouse saveEntry(String ip, Long creationUser, Long cardQuantity,
			String entryNumber, boolean isCard);
	
	/**
	 * 
	 * @param ip
	 * @param creationUser
	 * @param warehouseId
	 * @param deviceId
	 * @param compareToRechargeFile
	 * @return
	 */
	public boolean saveEntryRelation(String ip, Long creationUser,
			Long warehouseId, Long deviceId, boolean compareToRechargeFile);
	
	/**
	 * 
	 * @return {@link WarehouseTo} {@link List}
	 */
	public List<WarehouseTo> getPendingEntryOrders();
	
	/**
	 * 
	 * @param warehouseNumber
	 * @param warehouseOperationType
	 * @return
	 */
	public boolean searchWarehouseNumber(String warehouseNumber,
			WarehouseOperationType warehouseOperationType);
	
	/**
	 * 
	 * @param deviceCode
	 * @return
	 */
	public Long searchDeviceToEntry(String deviceCode);
	
	/**
	 * 
	 * @param idDevice
	 * @param ip
	 * @param creationUser
	 * @return
	 */
	public boolean changeDeviceStateEntry(Long idDevice, String ip,
			Long creationUser);
	
	/**
	 * 
	 * @return
	 */
	public boolean compareToPrechargeFile();
	
	/**
	 * 
	 * @param deviceCode
	 * @param ip
	 * @param creationUser
	 * @return
	 */
	public boolean reportAttempt(String deviceCode, String ip,
			Long creationUser);
	
	/**
	 * 
	 * @return
	 */
	public String getPrechargePath();
	
	/**
	 * 
	 * @param listToProcess
	 * @param ip
	 * @param creationUser
	 * @param idTagType
	 * @return
	 */
	public String processPrecharge(List<String> listToProcess, String ip,
			Long creationUser, Long idTagType);
	
	/**
	 * 
	 * @param entryOrderId
	 * @param ip
	 * @param creationUser
	 * @return
	 */
	public boolean deleteOrder(Long entryOrderId, String ip, Long creationUser);
	
	/**
	 * 
	 * @return
	 */
	public List<TbWarehouseState> getWarehouseState();
	
	/**
	 * 
	 * @param value
	 * @param ip
	 * @param creationUser
	 * @return
	 */
	public boolean changeParamCompareToPrechargeFile(String value, String ip,
			Long creationUser);
	
	/**
	 * 
	 * @param deviceId
	 * @param inclusionDetailId
	 * @param ip
	 * @param creationUser
	 * @return
	 */
	public boolean saveInitialitazion(Long deviceId, Long inclusionDetailId,
			String ip, Long creationUser);
}