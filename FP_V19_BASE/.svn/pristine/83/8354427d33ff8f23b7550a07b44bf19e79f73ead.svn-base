/**
 * 
 */
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
 *  The persistent class for the TB_SPECIAL_EXEMPT_OFFICE database table.
 * @author tmolina
 *
 */
@Entity
@Table(name="TB_SPECIAL_EXEMPT_OFFICE")
public class TbSpecialExemptOffice implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(sequenceName="TB_SPECIAL_EXEMPT_OFFICE_SEQ", name="TB_SPECIAL_EXEMPT_OFFICE_ID_GENERATOR",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TB_SPECIAL_EXEMPT_OFFICE_ID_GENERATOR")
	@Column(name="SPECIAL_EXEMPT_OFFICE_ID")
	private Long specialExemptOfficeId;
	
	@ManyToOne
	@JoinColumn(name="SPECIAL_EXEMPT_TYPE_ID")
	private TbSpecialExemptType tbSpecialExemptType;
	
	@Column(name="OFFICE_NAME")
	private String officeName;
	
	@Column(name="OFFICE_ADDRESS")
	private String officeAddress;
	
	@Column(name="OFFICE_PHONE")
	private String officePhone;
	
	@Column(name="OFFICE_FAX")
	private String officeFax;
	
	@Column(name="OFFICE_EMAIL")
	private String officeEmail;
	
	@Column(name="AUTHORIZED_BY")
	private String authoreizedBy;

	/**
	 * Constructor.
	 */
	public TbSpecialExemptOffice() {
		super();
	}

	/**
	 * @param specialExemptOfficeId the specialExemptOfficeId to set
	 */
	public void setSpecialExemptOfficeId(Long specialExemptOfficeId) {
		this.specialExemptOfficeId = specialExemptOfficeId;
	}

	/**
	 * @return the specialExemptOfficeId
	 */
	public Long getSpecialExemptOfficeId() {
		return specialExemptOfficeId;
	}

	/**
	 * @param officeName the officeName to set
	 */
	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	/**
	 * @return the officeName
	 */
	public String getOfficeName() {
		return officeName;
	}

	/**
	 * @param officePhone the officePhone to set
	 */
	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}

	/**
	 * @return the officePhone
	 */
	public String getOfficePhone() {
		return officePhone;
	}

	/**
	 * @param officeFax the officeFax to set
	 */
	public void setOfficeFax(String officeFax) {
		this.officeFax = officeFax;
	}

	/**
	 * @return the officeFax
	 */
	public String getOfficeFax() {
		return officeFax;
	}

	/**
	 * @param authoreizedBy the authoreizedBy to set
	 */
	public void setAuthoreizedBy(String authoreizedBy) {
		this.authoreizedBy = authoreizedBy;
	}

	/**
	 * @return the authoreizedBy
	 */
	public String getAuthoreizedBy() {
		return authoreizedBy;
	}

	/**
	 * @param officeEmail the officeEmail to set
	 */
	public void setOfficeEmail(String officeEmail) {
		this.officeEmail = officeEmail;
	}

	/**
	 * @return the officeEmail
	 */
	public String getOfficeEmail() {
		return officeEmail;
	}

	/**
	 * @param officeAddress the officeAddress to set
	 */
	public void setOfficeAddress(String officeAddress) {
		this.officeAddress = officeAddress;
	}

	/**
	 * @return the officeAddress
	 */
	public String getOfficeAddress() {
		return officeAddress;
	}

	/**
	 * @param tbSpecialExemptType the tbSpecialExemptType to set
	 */
	public void setTbSpecialExemptType(TbSpecialExemptType tbSpecialExemptType) {
		this.tbSpecialExemptType = tbSpecialExemptType;
	}

	/**
	 * @return the tbSpecialExemptType
	 */
	public TbSpecialExemptType getTbSpecialExemptType() {
		return tbSpecialExemptType;
	}
}