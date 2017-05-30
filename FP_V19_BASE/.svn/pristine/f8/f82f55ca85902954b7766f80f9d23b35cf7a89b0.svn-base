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
 * The persistent class for the TB_INSTALLATION database table.
 * @author tmolina
 */
 @Entity
 @Table(name="TB_INSTALLATION")
public class TbInstallation implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TB_INSTALLATION_INSTALLATIONID_GENERATOR")
	@SequenceGenerator(name="TB_INSTALLATION_INSTALLATIONID_GENERATOR",  sequenceName = "TB_INSTALLATION_SEQ",allocationSize=1)
	@Column(name="INSTALLATION_ID")
	private Long installationId;
	
	@ManyToOne
	@JoinColumn(name="DEVICE_CUSTOMIZATION_ID")
	private TbDeviceCustomization tbDeviceCustomization;
	
	@Column(name="INSTALLED")
	private String installed;
	
	@Column(name="INSTALLATION_CERTIFICATE")
	private String installationCertificate;
	
	@Column(name="INSTALLATION_SUPPORT")
	private String installationSupport;
	
	@ManyToOne
	@JoinColumn(name="INSTALLATION_LOCATION_ID")
	private TbInstallationLocation tbInstallationLocation;
	
	@Column(name="INSTALLATION_DATE")
	private Timestamp installationDate;
	
	@ManyToOne
	@JoinColumn(name="INSTALLATION_USER")
	private TbSystemUser installationUser;
	
	@Column(name="RECEIVER_NAME")
	private String receiverName;
	
	@Column(name="RECEIVER_IDENTIFICATION_NUMBER")
	private String receiverIdentificationNumber;
	
	@Column(name="OBSERVATION")
	private String observation;
	
	@Column(name="CREATION_DATE")
	private Timestamp creationDate;
	
	@ManyToOne
	@JoinColumn(name="CREATION_USER")
	private TbSystemUser creationUser;
	
	/**
	 * Constructor.
	 */
	public TbInstallation(){
		super();
	}

	/**
	 * @param installationId the installationId to set
	 */
	public void setInstallationId(Long installationId) {
		this.installationId = installationId;
	}

	/**
	 * @return the installationId
	 */
	public Long getInstallationId() {
		return installationId;
	}

	/**
	 * @param tbDeviceCustomization the tbDeviceCustomization to set
	 */
	public void setTbDeviceCustomization(TbDeviceCustomization tbDeviceCustomization) {
		this.tbDeviceCustomization = tbDeviceCustomization;
	}

	/**
	 * @return the tbDeviceCustomization
	 */
	public TbDeviceCustomization getTbDeviceCustomization() {
		return tbDeviceCustomization;
	}

	/**
	 * @param installed the installed to set
	 */
	public void setInstalled(String installed) {
		this.installed = installed;
	}

	/**
	 * @return the installed
	 */
	public String getInstalled() {
		return installed;
	}

	/**
	 * @param installationCertificate the installationCertificate to set
	 */
	public void setInstallationCertificate(String installationCertificate) {
		this.installationCertificate = installationCertificate;
	}

	/**
	 * @return the installationCertificate
	 */
	public String getInstallationCertificate() {
		return installationCertificate;
	}

	/**
	 * @param installationSupport the installationSupport to set
	 */
	public void setInstallationSupport(String installationSupport) {
		this.installationSupport = installationSupport;
	}

	/**
	 * @return the installationSupport
	 */
	public String getInstallationSupport() {
		return installationSupport;
	}

	/**
	 * @param tbInstallationLocation the tbInstallationLocation to set
	 */
	public void setTbInstallationLocation(TbInstallationLocation tbInstallationLocation) {
		this.tbInstallationLocation = tbInstallationLocation;
	}

	/**
	 * @return the tbInstallationLocation
	 */
	public TbInstallationLocation getTbInstallationLocation() {
		return tbInstallationLocation;
	}

	/**
	 * @param installationDate the installationDate to set
	 */
	public void setInstallationDate(Timestamp installationDate) {
		this.installationDate = installationDate;
	}

	/**
	 * @return the installationDate
	 */
	public Timestamp getInstallationDate() {
		return installationDate;
	}

	/**
	 * @param installationUser the installationUser to set
	 */
	public void setInstallationUser(TbSystemUser installationUser) {
		this.installationUser = installationUser;
	}

	/**
	 * @return the installationUser
	 */
	public TbSystemUser getInstallationUser() {
		return installationUser;
	}

	/**
	 * @param receiverName the receiverName to set
	 */
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	/**
	 * @return the receiverName
	 */
	public String getReceiverName() {
		return receiverName;
	}

	/**
	 * @param receiverIdentificationNumber the receiverIdentificationNumber to set
	 */
	public void setReceiverIdentificationNumber(
			String receiverIdentificationNumber) {
		this.receiverIdentificationNumber = receiverIdentificationNumber;
	}

	/**
	 * @return the receiverIdentificationNumber
	 */
	public String getReceiverIdentificationNumber() {
		return receiverIdentificationNumber;
	}

	/**
	 * @param observation the observation to set
	 */
	public void setObservation(String observation) {
		this.observation = observation;
	}

	/**
	 * @return the observation
	 */
	public String getObservation() {
		return observation;
	}

	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return the creationDate
	 */
	public Timestamp getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationUser the creationUser to set
	 */
	public void setCreationUser(TbSystemUser creationUser) {
		this.creationUser = creationUser;
	}

	/**
	 * @return the creationUser
	 */
	public TbSystemUser getCreationUser() {
		return creationUser;
	}
}