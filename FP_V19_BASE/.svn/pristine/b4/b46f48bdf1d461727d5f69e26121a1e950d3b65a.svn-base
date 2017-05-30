package ejb;
import java.io.File;
import java.util.List;

import javax.ejb.Remote;
import javax.swing.table.TableModel;

import constant.DeviceType;

import jpa.ReAccountDevice;
import jpa.TbAccount;
import jpa.TbDevice;
import jpa.TbDeviceFacial;
import jpa.TbDeviceHistory;
import jpa.TbDeviceState;
import jpa.TbDeviceType;

@Remote
public interface Device {
	
	/**
	 * @return Device List.
	 */
	public List<String> getAllDevices();
	
	/**
	 * @return List of all Devices.
	 */
	public List<TbDevice> getDevices();	
	
	/**
	 * 
	 * @return List of devices with no account associated. 
	 */
	public List<TbDevice> getDevicesWithNoAccount();
	
	/**
	 * Associates a client account with a device.
	 * @param idClientAccount
	 * @param idDevice
	 * @param ip
	 * @param creationUser
	 * @return true if successful, false otherwise.
	 */
	public boolean saveAssociation(Long idClientAccount, Long idDevice, Long idVehicle, String ip,
			Long creationUser);
	
	/**
	 * 
	 * @param idClient
	 * @return List of devices associated with the client. 
	 */
	public List<TbDevice> getDevicesByClient(Long idClient);
	
	/**
	 * 
	 * @return {@link TbDeviceType} {@link List}
	 */
	public List<TbDeviceType> getDevicesTypes();
	
	/**
	 * 
	 * @return {@link TbDeviceState} {@link List}
	 */
	public List<TbDeviceState> getDeviceStates();
	
	/**
	 * 
	 * @param code
	 * @return {@link TbDevice}
	 */
	public TbDevice getDeviceByCode(String code);
	
	/**
	 * 
	 * @param code
	 * @param facial
	 * @param balance
	 * @param deviceType
	 * @param deviceState
	 * @param ip
	 * @param creationUser
	 * @return <code>true</code> if successful, <code>false</code> otherwise.
	 */
	public boolean saveDevice(String code, String facial, Long balance,
			Long deviceType, Long deviceState,Long courierId, Long rollId, String ip, Long creationUser);
	
	/**
	 * 
	 * @param deviceId
	 * @param tagTypeId
	 * @param ip
	 * @param creationUser
	 * @return
	 */
	public boolean createRealationtagType(Long deviceId, Long tagTypeId,
			String ip, Long creationUser);
	
	/**
	 * 
	 * @param deviceTypeId
	 * @param companyId
	 * @return
	 */
	public TbDeviceFacial getDeviceFacial(DeviceType deviceTypeId, Long companyId,
			String ip, Long creationUser);
	
	/**
	 * 
	 * @param deviceTypeId
	 * @return {@link TbDevice} {@link List}
	 */
	public List<TbDevice> getAvailableDeviceByType(Long deviceTypeId);
	
	/**
	 * 
	 * @param deviceId
	 * @return {@link ReAccountDevice} {@link List}
	 */
	public List<ReAccountDevice> getAccountDevice(Long deviceId);
	
	/**
	 * 
	 * @param idAccount
	 * @return List of devices associated with the account. 
	 */
	public List<TbDevice> getDevicesByAccount(Long idAccount);
	
	/**
	 * 
	 * @param idAccount
	 * @param deviceType
	 * @return {@link TbDevice} {@link List}
	 */
	public List<TbDevice> getDevicesByAccountAndType(Long idAccount,
			DeviceType deviceType);
	
	
	public List<TbDevice> getAvailableDeviceByTypeNoAssig(Long deviceTypeId);
	
	public boolean maxDevice(long idAccount);

	public List<ReAccountDevice> getAccountDeviceByAccount(Long accountId);

	public String getDeviceByFacial(String facial);	
	
	public Long getDevicesByAccountClient(Long accountBankId);	
	
	public TbAccount getAccountByDeviceCode(String deviceCode);

	public List<ReAccountDevice> searchReAccountDevice(Long type, String value);

	public boolean disableReAccountDevice(Long accountId, String deviceCode, Long relationId,String vehiclePlate, String ip, Long user);

	public List<TbDevice> getDevicesByState(Long stateId, String device);
	
	public Long getInitDeviceState(Long accountId, Long deviceId);

	public List<TbDevice> getInfoDevicesByFilters(Long codeType,Long idClientAccount, String codeClient);

	public Long stateManufacterByDevice(Long deviceId);
	
	public boolean haveActiveRelation(Long deviceId);
	
	public List<TbDeviceHistory> getDevicesHistoryByAccount(Long idAccount);
	
	public TableModel loadFileTags(File file);

	public Long createMassiveTags(Long creationUser, String ip, TableModel listTags,
			Long deviceLength, Long tagTypeId, String nameFile, Long companyTagId, Long userId);
	
	public Long validateMassiveTags(Long creationUser, String ip, TableModel listTags,
			Long deviceLength, Long tagTypeId, String nameFile, Long companyTagId, Long userId);
	
	public boolean deleteTempDevice();
	
	public boolean plateIsEnrollOtherClient(Long vehicleId, Long accountId);

	/**
	 * Associates a client account with a device.
	 * @param idClientAccount
	 * @param idDevice
	 * @param ip
	 * @param creationUser
	 * @return true if successful, false otherwise.
	 */
	public boolean saveAssociationCarril(Long idClientAccount, Long idDevice, Long idVehicle, String ip,
			Long creationUser);
	
	public ReAccountDevice getReAccountDeviceById(Long relationId);
	
	public Long getListReplicationAvalState (Long accountId);
	
	public String setStateChange (Long deviceid, Long state);
	
	public boolean saveDeviceCompanyTag(String code, String facial, Long balance,
			Long deviceType, Long deviceState, Long companyTagId, Long courierId, Long rollId, String ip, Long creationUser);

	public TbDevice getDeviceByCode(Long codeType, String code);
}