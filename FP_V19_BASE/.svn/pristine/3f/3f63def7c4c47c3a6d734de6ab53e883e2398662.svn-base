package ejb;

import java.util.List;

import javax.ejb.Remote;

import jpa.TbCourier;
import jpa.TbDevice;
import jpa.TbRollo;
import jpa.TbVehicle;

import util.AllActiveDispositive;
import util.DeviceState;
import util.DevicesAvailable;
import util.InfoClient;

@Remote
public interface EnrollDevice {
	
	public Long validateClient(Long typeId, String codClient, String nomClient,String apeClient, String user, Long accountClient,
			String plateClient, String aditional1, String aditional2,String aditional3);
	
	
	public InfoClient searchClient(Long typeId, String codClient, String nomClient,String apeClient, String user,
			Long accountClient,String plateClient, String aditional1, String aditional2,String aditional3);
	
	public List<TbCourier> getCouries();

	public List<TbRollo> getRollosByCourier(Long courierId);
	
	public List<Object[]> getVehiclesByClient(Long clientId);

	public List<Object[]> getDevicesByRollo(Long rolloId);
	
	public List<DevicesAvailable> getDevicesAvaliable(Long clientId, Long rolloId);
	
	public boolean enrolar(Long clientId, Long accountId,List<DevicesAvailable> devices, String ip, Long creationUser);
	
	public List<TbVehicle> getVehiclesActiveByClient(Long clientId, Long accountId);
	
	public boolean saveChangeDevice(Long clientId, Long accountId, Long deviceId,String facialNew, String facialOld,
			Long vehicleId, String observation, Long creationUser, String ip);
	
	public String getFacialByVehicle(Long vehicleId);
	
	public String getVehicleById(Long vehicleId);
	
	public TbDevice getDeviceAvailableByRollo(Long rolloId);

	public boolean saveReplaceDevice(Long clientId, Long accountId, Long deviceId,String facialNew, String facialOld,
            Long vehicleId,String observation, Long creationUser, String ip, Long cobro);

	public String getVehicleByPlateAditional(String placa, String aditional1,String aditional2, String aditional3);

	public List<AllActiveDispositive> getAllDevice(String userId);
	
	public List<AllActiveDispositive> getAllDevice();

	public List<AllActiveDispositive> getDeviceByFiltersClient(String plate, String aditional1, 
			String aditional2, String aditional3, Long category, String account, String userId);
	
	public List<AllActiveDispositive> getDeviceByFiltersAdmin(Long codeType,
			String doc, String userName, String secondName, String userEmail, String plate, String aditional1, 
			String aditional2, String aditional3, Long category, String account);

	public boolean getActiveDispositive(List<AllActiveDispositive> listaTemp, String observation, String ip, Long creationUser);

	public boolean getActiveDispositiveAll(List<AllActiveDispositive> listfilter, String observation, String ip, Long creationUser);
	
	public List<Long> searchListClient(Long typeId, String codClient,String nomClient, String apeClient, String user,
			Long accountClient, String plateClient, String aditional1,String aditional2, String aditional3);
	
	public List<DevicesAvailable> getVehiclesAvaliableByClient(Long clientId);
	
	public List<DeviceState> getDevices(Long rolloId);


	public String getPlateByDevice(Long accountId, Long deviceId);

	public Long validateInfoClient(Long codeType, String numberDoc, String userName, String secondName, String userEmail,
			String plate, String aditional1, String aditional2, String aditional3, Long category, String account);
	
}
