package util;

import java.sql.Timestamp;

public class Vehicles {
	private Long vehicleId;
	
	private String vehiclePlate;
	
	private Timestamp vehicleBeginingDate;
	
	private String vehicleChassis;

	private String vehicleObservation;
    
    private Long colorId;
    
    private Long brandId;
    
    private Long categoryId;
    
    private String vehicleInternalCode;
    
    private String line;
    
    private String aditional1;
    
    private String aditional2;
    
    private String aditional3;
    
    private String observation;
    
    //1 Placa especial=SI; 0 o null Placa no especial=""
    private String especialPlate;
    
    private boolean eliminable;

	public Vehicles() {
		super();
	}

	public Long getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(Long vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getVehiclePlate() {
		return vehiclePlate;
	}

	public void setVehiclePlate(String vehiclePlate) {
		this.vehiclePlate = vehiclePlate;
	}

	public Timestamp getVehicleBeginingDate() {
		return vehicleBeginingDate;
	}

	public void setVehicleBeginingDate(Timestamp vehicleBeginingDate) {
		this.vehicleBeginingDate = vehicleBeginingDate;
	}

	public String getVehicleChassis() {
		return vehicleChassis;
	}

	public void setVehicleChassis(String vehicleChassis) {
		this.vehicleChassis = vehicleChassis;
	}

	public String getVehicleObservation() {
		return vehicleObservation;
	}

	public void setVehicleObservation(String vehicleObservation) {
		this.vehicleObservation = vehicleObservation;
	}

	public Long getColorId() {
		return colorId;
	}

	public void setColorId(Long colorId) {
		this.colorId = colorId;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getVehicleInternalCode() {
		return vehicleInternalCode;
	}

	public void setVehicleInternalCode(String vehicleInternalCode) {
		this.vehicleInternalCode = vehicleInternalCode;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public String getAditional1() {
		return aditional1;
	}

	public void setAditional1(String aditional1) {
		this.aditional1 = aditional1;
	}

	public String getAditional2() {
		return aditional2;
	}

	public void setAditional2(String aditional2) {
		this.aditional2 = aditional2;
	}

	public String getAditional3() {
		return aditional3;
	}

	public void setAditional3(String aditional3) {
		this.aditional3 = aditional3;
	}

	public boolean isEliminable() {
		return eliminable;
	}

	public void setEliminable(boolean eliminable) {
		this.eliminable = eliminable;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public String getEspecialPlate() {
		return especialPlate;
	}

	public void setEspecialPlate(String especialPlate) {
		this.especialPlate = especialPlate;
	}
	
}