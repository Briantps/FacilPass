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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The persistent class for the TB_INSTALLATION_LOCATION database table.
 * @author tmolina
 */
 @Entity
 @Table(name="TB_INSTALLATION_LOCATION")
public class TbInstallationLocation implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TB_INSTALLATION_LOCATION_INSTALLATIONLOCATIONID_GENERATOR")
	@SequenceGenerator(name="TB_INSTALLATION_LOCATION_INSTALLATIONLOCATIONID_GENERATOR",  sequenceName = "TB_INSTALLATION_LOCATION_SEQ",allocationSize=1)
	@Column(name="INSTALLATION_LOCATION_ID")
	private Long installationLocationId;
	
	@Column(name="INSTALLATION_LOCATION_NAME")
	private String installationLocationName;
	
	/**
	 * Constructor.
	 */
	public TbInstallationLocation(){
		super();
	}

	/**
	 * @param installationLocationId the installationLocationId to set
	 */
	public void setInstallationLocationId(Long installationLocationId) {
		this.installationLocationId = installationLocationId;
	}

	/**
	 * @return the installationLocationId
	 */
	public Long getInstallationLocationId() {
		return installationLocationId;
	}

	/**
	 * @param installationLocationName the installationLocationName to set
	 */
	public void setInstallationLocationName(String installationLocationName) {
		this.installationLocationName = installationLocationName;
	}

	/**
	 * @return the installationLocationName
	 */
	public String getInstallationLocationName() {
		return installationLocationName;
	}
}