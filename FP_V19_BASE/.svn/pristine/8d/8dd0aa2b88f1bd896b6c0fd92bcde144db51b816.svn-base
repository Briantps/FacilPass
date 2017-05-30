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
 * @author tmolina
 *
 */
@Entity
@Table(name="TB_USER_DATA")
public class TbUserData implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "TBUSERDATA_USERDATAID_GENERATOR")
	@SequenceGenerator(name="TBUSERDATA_USERDATAID_GENERATOR" ,sequenceName="TB_USER_DATA_SEQ",allocationSize=1)
	@Column(name="USER_DATA_ID")
	private Long userDataId;
	
	@Column(name="USER_DATA_OFFICE_NAME")
	private String userDataOfficeName;
	
	@Column(name="USER_DATA_CONTACT")
	private String userDataContact;
	
	@Column(name="USER_DATA_PHONE")
	private String userDataPhone;
	
	@Column(name="USER_DATA_OPTIONAL_PHONE")
	private String userDataOptionalPhone;
	
	@Column(name="USER_DATA_ADDRESS")
	private String userDataAddress;
	
	@Column(name="FOREIGN_COUNTRY")
	private String foreignCountry;
	
	@Column(name="FOREIGN_CITY")
	private String foreignCity;
	
	@Column(name="USER_DATA_CONTACT_ID")
	private Long userDataContactId;
	
	@Column(name="USER_DATA_CONTACT_TYPE_ID")
	private Long userDataContactTypeId;
	
	@Column(name="USER_MAIN_DEPENDENCY")
	private Long userMainDependency;
	
	@ManyToOne
	@JoinColumn(name="MUNICIPALITY_ID")
	private TbMunicipality tbMunicipality;
	
	@ManyToOne
	@JoinColumn(name="USER_ID")
	private TbSystemUser tbSystemUser;

	/**
	 * Constructor
	 */
	public TbUserData() {
		super();
	}

	/**
	 * @param userDataId the userDataId to set
	 */
	public void setUserDataId(Long userDataId) {
		this.userDataId = userDataId;
	}

	/**
	 * @return the userDataId
	 */
	public Long getUserDataId() {
		return userDataId;
	}

	/**
	 * @param userDataOfficeName the userDataOfficeName to set
	 */
	public void setUserDataOfficeName(String userDataOfficeName) {
		this.userDataOfficeName = userDataOfficeName;
	}

	/**
	 * @return the userDataOfficeName
	 */
	public String getUserDataOfficeName() {
		return userDataOfficeName;
	}

	/**
	 * @param userDataContact the userDataContact to set
	 */
	public void setUserDataContact(String userDataContact) {
		this.userDataContact = userDataContact;
	}

	/**
	 * @return the userDataContact
	 */
	public String getUserDataContact() {
		return userDataContact;
	}

	/**
	 * @param userDataPhone the userDataPhone to set
	 */
	public void setUserDataPhone(String userDataPhone) {
		this.userDataPhone = userDataPhone;
	}

	/**
	 * @return the userDataPhone
	 */
	public String getUserDataPhone() {
		return userDataPhone;
	}

	/**
	 * @param userDataOptionalPhone the userDataOptionalPhone to set
	 */
	public void setUserDataOptionalPhone(String userDataOptionalPhone) {
		this.userDataOptionalPhone = userDataOptionalPhone;
	}

	/**
	 * @return the userDataOptionalPhone
	 */
	public String getUserDataOptionalPhone() {
		return userDataOptionalPhone;
	}

	/**
	 * @param userDataAddress the userDataAddress to set
	 */
	public void setUserDataAddress(String userDataAddress) {
		this.userDataAddress = userDataAddress;
	}

	/**
	 * @return the userDataAddress
	 */
	public String getUserDataAddress() {
		return userDataAddress;
	}

	/**
	 * @param tbMunicipality the tbMunicipality to set
	 */
	public void setTbMunicipality(TbMunicipality tbMunicipality) {
		this.tbMunicipality = tbMunicipality;
	}

	/**
	 * @return the tbMunicipality
	 */
	public TbMunicipality getTbMunicipality() {
		return tbMunicipality;
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

	public String getForeignCountry() {
		return foreignCountry;
	}

	public void setForeignCountry(String foreignCountry) {
		this.foreignCountry = foreignCountry;
	}

	public String getForeignCity() {
		return foreignCity;
	}

	public void setForeignCity(String foreignCity) {
		this.foreignCity = foreignCity;
	}

	public Long getUserDataContactId() {
		return userDataContactId;
	}

	public void setUserDataContactId(Long userDataContactId) {
		this.userDataContactId = userDataContactId;
	}

	public Long getUserDataContactTypeId() {
		return userDataContactTypeId;
	}

	public void setUserDataContactTypeId(Long userDataContactTypeId) {
		this.userDataContactTypeId = userDataContactTypeId;
	}

	public Long getUserMainDependency() {
		return userMainDependency;
	}

	public void setUserMainDependency(Long userMainDependency) {
		this.userMainDependency = userMainDependency;
	}
	
	
	
}