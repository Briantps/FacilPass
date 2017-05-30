package ejb.crud;

import java.util.List;

import javax.ejb.Remote;

import jpa.TbLane;
import jpa.TbStationBO;
import jpa.TbStationType;

@Remote
public interface Station {
	
	/**
	 * 
	 * @param stationCode
	 * @param stationName
	 * @param companyId
	 * @param departmentId
	 * @param ip
	 * @param creationUser
	 * @param stationTypeId
	 * @param numberLanes
	 * @return
	 */
	public String insertStation(String stationCode, String stationName,
			Long companyId, Long departmentId, String ip, Long creationUser,
			Long stationTypeId, Long numberLanes);
	
	/**
	 * 
	 * @return {@link TbStationBO} List. 
	 */
	public List<TbStationBO> getStationList();
	
	/**
	 * 
	 * @return {@link TbStationType} {@link List}
	 */
	public List<TbStationType> getStationType();

	public List<TbStationBO> getStationListByCompany(Long companyId);

	public List<TbLane> getLaneListByStation(Long stationId);

}