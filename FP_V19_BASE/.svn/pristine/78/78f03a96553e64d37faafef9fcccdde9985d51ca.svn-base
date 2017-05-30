/**
 * 
 */
package util.perti;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jpa.TbDeviceCustomization;
import jpa.TbStationBO;

/**
 * @author tmolina
 *
 */
public class Personalization implements Serializable {
	private static final long serialVersionUID = 2229736834453105758L;
	
	// Attributes ----- //
	
	private Integer itemNumber;
	
	private List<TbStationBO> stations;
	
	private String reg;
	
	private String station;
	
	private TbDeviceCustomization cus;
	
	// Constructor ---- //

	/**
	 * Constructor.
	 */
	public Personalization(){
		stations = new ArrayList<TbStationBO>();
	}
	
	// Getters and Setters ---- //

	/**
	 * @return the itemNumber
	 */
	public Integer getItemNumber() {
		return itemNumber;
	}

	/**
	 * @param itemNumber the itemNumber to set
	 */
	public void setItemNumber(Integer itemNumber) {
		this.itemNumber = itemNumber;
	}

	/**
	 * @return the stations
	 */
	public List<TbStationBO> getStations() {
		return stations;
	}

	/**
	 * @param stations the stations to set
	 */
	public void setStations(List<TbStationBO> stations) {
		this.stations = stations;
	}

	/**
	 * @return the reg
	 */
	public String getReg() {
		for(TbStationBO ts : getStations()){
			reg = ts.getTbDepartment().getDepartmentName();
			break;
		}
		return reg;
	}

	/**
	 * @param reg the reg to set
	 */
	public void setReg(String reg) {
		this.reg = reg;
	}

	/**
	 * @return the station
	 */
	public String getStation() {
		station = "";
		for(TbStationBO ts : getStations()){
			station += ts.getStationBOName() + "-"; 
		}
		return station;
	}

	/**
	 * @param station the station to set
	 */
	public void setStation(String station) {
		this.station = station;
	}

	/**
	 * @param cus the cus to set
	 */
	public void setCus(TbDeviceCustomization cus) {
		this.cus = cus;
	}

	/**
	 * @return the cus
	 */
	public TbDeviceCustomization getCus() {
		return cus;
	}
}
