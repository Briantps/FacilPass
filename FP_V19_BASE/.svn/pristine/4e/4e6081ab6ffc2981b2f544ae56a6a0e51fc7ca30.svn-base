package jpa;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="TB_COURIER")
public class TbCourier implements Serializable{


	private static final long serialVersionUID = 1L;

	@Id
	//@GeneratedValue(generator = "TB_COURIER_SEQ", strategy = GenerationType.SEQUENCE)
	//@SequenceGenerator(name = "TB_COURIER_SEQ", sequenceName = "TB_COURIER_SEQ",allocationSize=1)
	@Column(name="COURIER_ID")
	private Long courierId;
	
	@ManyToOne
	@JoinColumn(name="COMPANY_TAG_ID")
	private TbCompanyTag companyTagId;
	
	@Column(name="COURIER_NAME")
	private String courierName;
	
	@Column(name="ADITIONAL1")
	private String aditional1;
	
	@Column(name="ADITIONAL2")
	private String aditional2;
	
	@Column(name="DATE_CREATION")
	private Timestamp dateCreation;
	
	@Column(name="COURIER_STATE")
	private Integer courierState;
	
	
	public TbCourier(){
		super();
	}


	/**
	 * @param courierId the courierId to set
	 */
	public void setCourierId(Long courierId) {
		this.courierId = courierId;
	}


	/**
	 * @return the courierId
	 */
	public Long getCourierId() {
		return courierId;
	}


	/**
	 * @param courierName the courierName to set
	 */
	public void setCourierName(String courierName) {
		this.courierName = courierName;
	}


	/**
	 * @return the courierName
	 */
	public String getCourierName() {
		return courierName;
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
	 * @param dateCreation the dateCreation to set
	 */
	public void setDateCreation(Timestamp dateCreation) {
		this.dateCreation = dateCreation;
	}


	/**
	 * @return the dateCreation
	 */
	public Timestamp getDateCreation() {
		return dateCreation;
	}


	/**
	 * @param courierState the courierState to set
	 */
	public void setCourierState(Integer courierState) {
		this.courierState = courierState;
	}


	/**
	 * @return the courierState
	 */
	public Integer getCourierState() {
		return courierState;
	}


	public void setCompanyTagId(TbCompanyTag companyTagId) {
		this.companyTagId = companyTagId;
	}


	public TbCompanyTag getCompanyTagId() {
		return companyTagId;
	}
	
}
