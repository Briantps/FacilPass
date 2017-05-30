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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The persistent class for the RE_DEVICE_WAREHOUSE database table.
 * @author tmolina
 */
 @Entity
 @Table(name="RE_DEVICE_WAREHOUSE")
public class ReDeviceWarehouse implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RE_DEVICE_WAREHOUSE_DEVICEWAREHOUSEID_GENERATOR")
	@SequenceGenerator(name="RE_DEVICE_WAREHOUSE_DEVICEWAREHOUSEID_GENERATOR",  sequenceName = "RE_DEVICE_WAREHOUSE_SEQ",allocationSize=1)
	@Column(name="DEVICE_WAREHOUSE_ID")
	private Long deviceWarehouseId;
	
	@ManyToOne
	@JoinColumn(name="DEVICE_ID")
	private TbDevice tbDevice;
	
	@ManyToOne
	@JoinColumn(name="WAREHOUSE_ID")
	private TbWarehouse tbWarehouse;
	
	@Column(name="COMPARED_TO_PRECHARGE_FILE")
	private boolean comparedToRechargeFile;
	
	@Column(name="DEVICE_WAREHOUSE_DATE")
	private Timestamp deviceWarehouseDate;
	
	/**
	 * Constructor.
	 */
	public ReDeviceWarehouse(){
		super();
	}

	/**
	 * @param deviceWarehouseId the deviceWarehouseId to set
	 */
	public void setDeviceWarehouseId(Long deviceWarehouseId) {
		this.deviceWarehouseId = deviceWarehouseId;
	}

	/**
	 * @return the deviceWarehouseId
	 */
	public Long getDeviceWarehouseId() {
		return deviceWarehouseId;
	}

	/**
	 * @param tbDevice the tbDevice to set
	 */
	public void setTbDevice(TbDevice tbDevice) {
		this.tbDevice = tbDevice;
	}

	/**
	 * @return the tbDevice
	 */
	public TbDevice getTbDevice() {
		return tbDevice;
	}

	/**
	 * @param tbWarehouse the tbWarehouse to set
	 */
	public void setTbWarehouse(TbWarehouse tbWarehouse) {
		this.tbWarehouse = tbWarehouse;
	}

	/**
	 * @return the tbWarehouse
	 */
	public TbWarehouse getTbWarehouse() {
		return tbWarehouse;
	}

	/**
	 * @param comparedToRechargeFile the comparedToRechargeFile to set
	 */
	public void setComparedToRechargeFile(boolean comparedToRechargeFile) {
		this.comparedToRechargeFile = comparedToRechargeFile;
	}

	/**
	 * @return the comparedToRechargeFile
	 */
	public boolean isComparedToRechargeFile() {
		return comparedToRechargeFile;
	}

	/**
	 * @param deviceWarehouseDate the deviceWarehouseDate to set
	 */
	public void setDeviceWarehouseDate(Timestamp deviceWarehouseDate) {
		this.deviceWarehouseDate = deviceWarehouseDate;
	}

	/**
	 * @return the deviceWarehouseDate
	 */
	public Timestamp getDeviceWarehouseDate() {
		return deviceWarehouseDate;
	}
}