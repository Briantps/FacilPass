/**
 * 
 */
package jpa;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *  The persistent class for the TB_LOG database table.
 * @author jromero
 *
 */
@Entity
@Table(name="TB_LOG_VEHICLE")
public class TbLogVehicle implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator="TB_LOG_VEHI_LOGID_GENERATOR", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="TB_LOG_VEHI_LOGID_GENERATOR", sequenceName="TB_LOG_VEHICLE_SEQ", allocationSize=1)
	@Column(name="LOG_VEHICLE_ID")
	private Long logVehicleId;
	
	@Column(name="LOG_VEHICLE_TYPE")
	private Long logVehicleType;
	
	@Column(name="LOG_VEHICLE_ROW")
	private Long logVehicleRow;
	
	@Column(name="LOG_VEHICLE_FIELD")
	private String logVehicleField;
	
	@Column(name="LOG_VEHICLE_VALUE")
	private String logVehicleValue;

	@Column(name="LOG_VEHICLE_FILE")
	private String logVehicleFile;
	
	@Column(name="LOG_VEHICLE_DESCRIPTION")
	private String logVehicleDescription;
	
	@Column(name="LOG_VEHICLE_SOLUTION")
	private String logVehicleSolution;
	
	@Column(name="LOG_VEHICLE_DATE")
	private Timestamp logVehicleDate;
	
	@Column(name="USER_ID")
	private Long userId;
	
	@Column(name="LOG_VEHICLE_STATE")
	private Long logVehicleState;

	/**
	 * Constructor
	 */
	public TbLogVehicle(){
		super();
	}

	public Long getLogVehicleId() {
		return logVehicleId;
	}

	public void setLogVehicleId(Long logVehicleId) {
		this.logVehicleId = logVehicleId;
	}

	public Long getLogVehicleType() {
		return logVehicleType;
	}

	public void setLogVehicleType(Long logVehicleType) {
		this.logVehicleType = logVehicleType;
	}

	public Long getLogVehicleRow() {
		return logVehicleRow;
	}

	public void setLogVehicleRow(Long logVehicleRow) {
		this.logVehicleRow = logVehicleRow;
	}

	public String getLogVehicleField() {
		return logVehicleField;
	}

	public void setLogVehicleField(String logVehicleField) {
		this.logVehicleField = logVehicleField;
	}

	public String getLogVehicleValue() {
		return logVehicleValue;
	}

	public void setLogVehicleValue(String logVehicleValue) {
		this.logVehicleValue = logVehicleValue;
	}

	public String getLogVehicleFile() {
		return logVehicleFile;
	}

	public void setLogVehicleFile(String logVehicleFile) {
		this.logVehicleFile = logVehicleFile;
	}

	public String getLogVehicleDescription() {
		return logVehicleDescription;
	}

	public void setLogVehicleDescription(String logVehicleDescription) {
		this.logVehicleDescription = logVehicleDescription;
	}

	public String getLogVehicleSolution() {
		return logVehicleSolution;
	}

	public void setLogVehicleSolution(String logVehicleSolution) {
		this.logVehicleSolution = logVehicleSolution;
	}

	public Timestamp getLogVehicleDate() {
		return logVehicleDate;
	}

	public void setLogVehicleDate(Timestamp logVehicleDate) {
		this.logVehicleDate = logVehicleDate;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getLogVehicleState() {
		return logVehicleState;
	}

	public void setLogVehicleState(Long logVehicleState) {
		this.logVehicleState = logVehicleState;
	}

}