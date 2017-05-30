package jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The persistent class for the TB_STATION_BO database table.
 * 
 */
@Entity
@Table(name="TB_STATION_BO")
public class TbStationBO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TB_STATION_BO_STATIONBOID_GENERATOR", sequenceName="TB_STATION_BO_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TB_STATION_BO_STATIONBOID_GENERATOR")
	@Column(name="STATION_BO_ID", unique=true, nullable=false, precision=19)
	private long stationBOId;

	@Column(name="STATION_BO_CODE", nullable=false, length=20)
	private String stationBOCode;

	@Column(name="STATION_BO_NAME", nullable=false, length=50)
	private String stationBOName;
	
	@ManyToOne
	@JoinColumn(name="STATION_TYPE_ID")
	private TbStationType tbStationType;
	
	@ManyToOne
	@JoinColumn(name="COMPANY_ID", nullable=false)
	private TbCompany tbCompany;
	
	@ManyToOne
	@JoinColumn(name="DEPARTMENT_ID")
	private TbDepartment tbDepartment;
	
	@Column(name="NUMBER_LANES", length=2)
	private Long NumberLanes;

	/**
	 * Constructor
	 */
    public TbStationBO() {
    	super();
    }

	/**
	 * @param stationBOId the stationBOId to set
	 */
	public void setStationBOId(long stationBOId) {
		this.stationBOId = stationBOId;
	}

	/**
	 * @return the stationBOId
	 */
	public long getStationBOId() {
		return stationBOId;
	}

	/**
	 * @param stationBOCode the stationBOCode to set
	 */
	public void setStationBOCode(String stationBOCode) {
		this.stationBOCode = stationBOCode;
	}

	/**
	 * @return the stationBOCode
	 */
	public String getStationBOCode() {
		return stationBOCode;
	}

	/**
	 * @param stationBOName the stationBOName to set
	 */
	public void setStationBOName(String stationBOName) {
		this.stationBOName = stationBOName;
	}

	/**
	 * @return the stationBOName
	 */
	public String getStationBOName() {
		return stationBOName;
	}

	/**
	 * @param tbStationType the tbStationType to set
	 */
	public void setTbStationType(TbStationType tbStationType) {
		this.tbStationType = tbStationType;
	}

	/**
	 * @return the tbStationType
	 */
	public TbStationType getTbStationType() {
		return tbStationType;
	}

	/**
	 * @param tbCompany the tbCompany to set
	 */
	public void setTbCompany(TbCompany tbCompany) {
		this.tbCompany = tbCompany;
	}

	/**
	 * @return the tbCompany
	 */
	public TbCompany getTbCompany() {
		return tbCompany;
	}

	/**
	 * @param tbDepartment the tbDepartment to set
	 */
	public void setTbDepartment(TbDepartment tbDepartment) {
		this.tbDepartment = tbDepartment;
	}

	/**
	 * @return the tbDepartment
	 */
	public TbDepartment getTbDepartment() {
		return tbDepartment;
	}

	/**
	 * @param numberLanes the numberLanes to set
	 */
	public void setNumberLanes(Long numberLanes) {
		NumberLanes = numberLanes;
	}

	/**
	 * @return the numberLanes
	 */
	public Long getNumberLanes() {
		return NumberLanes;
	}

}