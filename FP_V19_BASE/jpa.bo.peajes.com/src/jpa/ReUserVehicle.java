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
 * @author tmolina
 *
 */
@Entity
@Table(name = "RE_USER_VEHICLE")
public class ReUserVehicle implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="RE_USER_VEHICLE_USERVEHICLEID_GENERATOR")
	@SequenceGenerator(name="RE_USER_VEHICLE_USERVEHICLEID_GENERATOR",sequenceName="RE_USER_VEHICLE_SEQ",allocationSize=1)
	@Column(name="USER_VEHICLE_ID")
	private Long userVehicleId;
	
	@ManyToOne
	@JoinColumn(name="USER_ID")
	private TbSystemUser tbSystemUser;
	
	@ManyToOne
	@JoinColumn(name="VEHICLE_ID")
	private TbVehicle tbVehicle;
	
	@Column(name="DATE_ASSOCIATION")
	private Timestamp dateAssociation;
	
	@Column(name="DATE_DISASSOCIATION")
	private Timestamp dateDisassociation;
	
	@Column(name="STATE_RELATION")
	private Integer stateRelation;
	
	/**
	 * Constructor
	 */
	public ReUserVehicle(){
		super();
	}

	/**
	 * @param userVehicleId the userVehicleId to set
	 */
	public void setUserVehicleId(Long userVehicleId) {
		this.userVehicleId = userVehicleId;
	}

	/**
	 * @return the userVehicleId
	 */
	public Long getUserVehicleId() {
		return userVehicleId;
	}

	/**
	 * @param tbSystemUser the tbSystemUser to set
	 */
	public void setTbSystemUser(TbSystemUser tbSystemUser) {
		this.tbSystemUser = tbSystemUser;
	}

	/**
	 * @return the tbSystemUser
	 */
	public TbSystemUser getTbSystemUser() {
		return tbSystemUser;
	}

	/**
	 * @param tbVehicle the tbVehicle to set
	 */
	public void setTbVehicle(TbVehicle tbVehicle) {
		this.tbVehicle = tbVehicle;
	}

	/**
	 * @return the tbVehicle
	 */
	public TbVehicle getTbVehicle() {
		return tbVehicle;
	}

	public Timestamp getDateAssociation() {
		return dateAssociation;
	}

	public void setDateAssociation(Timestamp dateAssociation) {
		this.dateAssociation = dateAssociation;
	}

	public Integer getStateRelation() {
		return stateRelation;
	}

	public void setStateRelation(Integer stateRelation) {
		this.stateRelation = stateRelation;
	}

	public Timestamp getDateDisassociation() {
		return dateDisassociation;
	}

	public void setDateDisassociation(Timestamp dateDisassociation) {
		this.dateDisassociation = dateDisassociation;
	}
	
}