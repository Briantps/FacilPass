package ejb;

import java.io.File;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.ejb.Remote;
import javax.swing.table.TableModel;

import jpa.ReAccountDevice;
import jpa.ReUserVehicle;
import jpa.TbVehicle;
import util.Vehicles;

/**
 * Defines a set of methods to perform control on contents of table TB_VEHICLE
 * @author Mauricio Gil
 */
@Remote
public interface Vehicle {

	/**
	 * Retrieves a vehicle object from database
	 * @param id Vehicle identifier
	 * @return Vehicle object
	 */
	public TbVehicle getVehicle(long id);

	/**
	 * Removes a vehicle from database
	 * @param vehicle Vehicle object
	 */
	public boolean removeVehicle(TbVehicle vehicle);

	/**
	 * Changes a vehicle in database
	 * @param vehicle Vehicle database
	 */
	public boolean updateVehicle(TbVehicle vehicle);
	
	/**
	 * Retrieves all the available vehicle objects from database
	 * @return List of vehicles
	 */
	public List<TbVehicle> getVehicles();

	/**
	 * Inserts a new vehicle object in database
	 * @param vehicle Vehicle object
	 */
	public boolean addVehicle(TbVehicle vehicle);

	/**
	 * Retrieves a vehicle list according to plate
	 * @param plate Plate initials or full plate for one object
	 * @return List of vehicles
	 */
	public List<TbVehicle> getVehiclesByPlate(String plate);
	
	/**
	 * Creates a vehicle.
	 * @param creationUser
	 * @param ip
	 * @param vehiclePlate
	 * @param vehicleChassis
	 * @param vehicleInternalCode
	 * @param colorId
	 * @param brandId
	 * @param categoryId
	 * @return -1 if the plate already exists, 0 if fails, new vehicle id if successful
	 */
	public Long createVehicle(Long creationUser, String ip, String vehiclePlate,  String vehicleChassis,
			String vehicleInternalCode, Long colorId, Long brandId, Long categoryId,String aditional1,
			String aditional2,String aditional3,String line,Timestamp model,String observ,Long userId);
	
	/**
	 * Gets the vehicle by plate.
	 * @param plate
	 * @return {@link TbVehicle}
	 */
	public TbVehicle getVehicleByPlate(String plate);
	
	/**
	 * 
	 * @param idClient
	 * @return
	 */
	public List<TbVehicle> getVehicleByClient(Long idClient);
	
	public List<TbVehicle> getVehicleNoRegister();
	
	public ReAccountDevice getAccountByPlate(String plate);

	public List<TbVehicle> getVehicleByAccount(Long userId, Long accountId);
	
	public boolean plateExit(String plate);
	
	public TableModel loadFileVehicles(File file);
	
	//Type=>0=Administrador;1=Cliente
	public Long createMassiveVehicles(Long creationUser,String ip,TableModel t,
			String fileName,Long userId,Long type);
	
	public boolean isPlacaValida(String plac);
	
	public boolean validateMassiveLoad(int row,int column, String value,
			String fileName,Long userId,boolean isEspecial);
	
	public List<Vehicles> getVehiclesByUser(Long userId);
	
	public boolean alterVehicle(Long creationUser, String ip, Long vehicleId, String vehicleChassis,
			String vehicleInternalCode, Long colorId, Long brandId, Long categoryId,String aditional1,
			String aditional2,String aditional3,String line,Timestamp model,String observ,boolean especial,Long userId);
	
	public boolean createDeletedVehicle(Long creationUser, String ip, Long vehicleId, String vehicleChassis,
			String vehicleInternalCode, Long colorId, Long brandId, Long categoryId,String aditional1,
			String aditional2,String aditional3,String line,Timestamp model,String observ,Long userId);
	
	public boolean deleteVehicle(Long creationUser, String ip, String vehiclePlate,Long userId);
	
	public boolean haveVehicleCountMoves(Long vehicleId);
	
	public boolean haveRelationTag(Long vehicleId);
	
	public boolean removeUserVehicleRelation(Long userId,Long vehicleId);
	
	public boolean deleteUserVehicleRelation(Long userId,Long vehicleId);
	
	//Type=>0=Administrador;1=Cliente
	public void createProcessForVehicles(Long creationUser, String ip, String messDetail,String messError,Long userId,Long type);
	
	//Type=>0=Administrador;1=Cliente
	public List<Vehicles> getUserVehiclesByFilters(Long codeType,String code,String firstName,String lastName,
			Long catId, String placa, String adicional1, String adicional2,
			String adicional3,Long type, int page, int rows);
	
	public boolean updateCategoryTag(String codeDev,Long cat);
	
	public boolean isEspecialPlate(String plate);
	
	//Type=>0=Administrador;1=Cliente
	public Long getUserByFiltersVehicle(Long codeType,String code,String firstName,String lastName,
			Long catId, String placa, String adicional1, String adicional2,
			String adicional3,Long type);
	
	public Long getUserIdByVehicleId(Long vehicleId);
	
	public String getVehiclesByReport(Long codeType,String code,String firstName,String lastName,String email,String account,
			Long catId, String placa,String adicional1, String adicional2,String adicional3,Date dateIni,Date dateEnd);
	
	/**
	 * 
	 * @param vehiclePlate
	 * @return Long
	 * 			-2: Error
	 * 			-1: Inactive Relation
	 * 			 0: No relation
	 * 			 1: Active Relation
	 */
	public Long typeRelationTag(String vehiclePlate);

	public boolean haveActiveRelationTag(Long vehicleId);
	
	public Long reasignVehicle(Long creationUser, String ip,Long vehicleId,Long userIdNew);
	
	public Long getLastInactiveRelationTag(Long vechileId);
	
	public ReUserVehicle getReUserVehicleByVehicleId(Long vehicleId);
	
	//Trae la maxima categoria del vehiculo Con Saldo,Saldo Bajo y sin Saldo del cliente mediante la cuenta
	public Long getMaxCategoryClient (Long Account);
}