package jpa;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;

/**
 * The persistent class for the TB_PRODUCT database table.
 * 
 */
@Entity
@Table(name="TB_VEHICLE")
public class TbVehicle implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBVEHICLE_VEHICLEID_GENERATOR")
	@SequenceGenerator(name="TBVEHICLE_VEHICLEID_GENERATOR", sequenceName="TB_VEHICLE_SEQ",allocationSize=1)
	@Column(name="VEHICLE_ID")
	private Long vehicleId;
	
	@Column(name="VEHICLE_PLATE")
	private String vehiclePlate;
	
	@Column(name="VEHICLE_BEGINING_DATE")
	private Timestamp vehicleBeginingDate;
	
	@Column(name="VEHICLE_CHASSIS")
	private String vehicleChassis;

	@Column(name="VEHICLE_OBSERVATION")
	private String vehicleObservation;
    
    //bi-directional many-to-one association to TbColor
    @ManyToOne
    @JoinColumn(name="COLOR_ID")
    private TbColor tbColor;
    
    //bi-directional many-to-one association to TbBrand
    @ManyToOne
    @JoinColumn(name="BRAND_ID")
    private TbBrand tbBrand;
    
    //bi-directional many-to-one association to TbColor
    @ManyToOne
    @JoinColumn(name="CATEGORY_ID")
    private TbCategory tbCategory;
    
    @Column(name="VEHICLE_INTERNAL_CODE")
    private String vehicleInternalCode;
    
    @Column(name="LINE")
    private String line;
    
    @Column(name="ADITIONAL1")
    private String aditional1;
    
    @Column(name="ADITIONAL2")
    private String aditional2;
    
    @Column(name="ADITIONAL3")
    private String aditional3;
    
    @Column(name="VEHICLE_REGISTRATION_DATE")
	private Timestamp vehicleRegistrationDate;
    
    @Column(name="VEHICLE_ESPECIAL_PLATE")
	private Long vehicleEspecialPlate;
    
    @Column(name="VEHICLE_DELETED")
	private Long vehicleDeleted;
    
    /**
     * Constructor.
     */
    public TbVehicle() {
    	super();
    }

	/**
	 * @param tbColor the tbColor to set
	 */
	public void setTbColor(TbColor tbColor) {
		this.tbColor = tbColor;
	}

	/**
	 * @return the tbColor
	 */
	public TbColor getTbColor() {
		return tbColor;
	}

	/**
	 * @param tbBrand the tbBrand to set
	 */
	public void setTbBrand(TbBrand tbBrand) {
		this.tbBrand = tbBrand;
	}

	/**
	 * @return the tbBrand
	 */
	public TbBrand getTbBrand() {
		return tbBrand;
	}

	/**
	 * @param vehicleId the vehicleId to set
	 */
	public void setVehicleId(long vehicleId) {
		this.vehicleId = vehicleId;
	}

	/**
	 * @return the vehicleId
	 */
	public long getVehicleId() {
		return vehicleId;
	}

	/**
	 * @param vehiclePlate the vehiclePlate to set
	 */
	public void setVehiclePlate(String vehiclePlate) {
		this.vehiclePlate = vehiclePlate;
	}

	/**
	 * @return the vehiclePlate
	 */
	public String getVehiclePlate() {
		return vehiclePlate;
	}

	/**
	 * @param vehicleBeginingDate the vehicleBeginingDate to set
	 */
	public void setVehicleBeginingDate(Timestamp vehicleBeginingDate) {
		this.vehicleBeginingDate = vehicleBeginingDate;
	}

	/**
	 * @return the vehicleBeginingDate
	 */
	public Timestamp getVehicleBeginingDate() {
		return vehicleBeginingDate;
	}

	/**
	 * @param vehicleChassis the vehicleChassis to set
	 */
	public void setVehicleChassis(String vehicleChassis) {
		this.vehicleChassis = vehicleChassis;
	}

	/**
	 * @return the vehicleChassis
	 */
	public String getVehicleChassis() {
		return vehicleChassis;
	}

	/**
	 * @param vehicleObservation the vehicleObservation to set
	 */
	public void setVehicleObservation(String vehicleObservation) {
		this.vehicleObservation = vehicleObservation;
	}

	/**
	 * @return the vehicleObservation
	 */
	public String getVehicleObservation() {
		return vehicleObservation;
	}

	/**
	 * @param tbCategory the tbCategory to set
	 */
	public void setTbCategory(TbCategory tbCategory) {
		this.tbCategory = tbCategory;
	}

	/**
	 * @return the tbCategory
	 */
	public TbCategory getTbCategory() {
		return tbCategory;
	}

	/**
	 * @param vehicleInternalCode the vehicleInternalCode to set
	 */
	public void setVehicleInternalCode(String vehicleInternalCode) {
		this.vehicleInternalCode = vehicleInternalCode;
	}

	/**
	 * @return the vehicleInternalCode
	 */
	public String getVehicleInternalCode() {
		return vehicleInternalCode;
	}

	/**
	 * @param line the line to set
	 */
	public void setLine(String line) {
		this.line = line;
	}

	/**
	 * @return the line
	 */
	public String getLine() {
		return line;
	}

	/**
	 * @param aditional1 the aditional1 to set
	 */
	public void setAditional1(String aditional1) {
		this.aditional1 = aditional1;
	}

	/**
	 * @return the aditional1
	 */
	public String getAditional1() {
		return aditional1;
	}

	/**
	 * @param aditional2 the aditional2 to set
	 */
	public void setAditional2(String aditional2) {
		this.aditional2 = aditional2;
	}

	/**
	 * @return the aditional2
	 */
	public String getAditional2() {
		return aditional2;
	}

	/**
	 * @param aditional3 the aditional3 to set
	 */
	public void setAditional3(String aditional3) {
		this.aditional3 = aditional3;
	}

	/**
	 * @return the aditional3
	 */
	public String getAditional3() {
		return aditional3;
	}

	public Timestamp getVehicleRegistrationDate() {
		return vehicleRegistrationDate;
	}

	public void setVehicleRegistrationDate(Timestamp vehicleRegistrationDate) {
		this.vehicleRegistrationDate = vehicleRegistrationDate;
	}

	public Long getVehicleEspecialPlate() {
		return vehicleEspecialPlate;
	}

	public void setVehicleEspecialPlate(Long vehicleEspecialPlate) {
		this.vehicleEspecialPlate = vehicleEspecialPlate;
	}

	public Long getVehicleDeleted() {
		return vehicleDeleted;
	}

	public void setVehicleDeleted(Long vehicleDeleted) {
		this.vehicleDeleted = vehicleDeleted;
	}

	
}