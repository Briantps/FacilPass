package jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *  The persistent class for the TB_STATION_TYPE database table.
 * @author tmolina
 *
 */
@Entity
@Table(name="TB_STATION_TYPE")
public class TbStationType  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="STATION_TYPE_ID")
	private Long stationTypeId;
	
	@Column(name="STATION_TYPE_NAME")
	private String stationTypeName;
	
	/**
	 * 
	 */
	public TbStationType(){
		super();
	}

	/**
	 * @param stationTypeId the stationTypeId to set
	 */
	public void setStationTypeId(Long stationTypeId) {
		this.stationTypeId = stationTypeId;
	}

	/**
	 * @return the stationTypeId
	 */
	public Long getStationTypeId() {
		return stationTypeId;
	}

	/**
	 * @param stationTypeName the stationTypeName to set
	 */
	public void setStationTypeName(String stationTypeName) {
		this.stationTypeName = stationTypeName;
	}

	/**
	 * @return the stationTypeName
	 */
	public String getStationTypeName() {
		return stationTypeName;
	}
}