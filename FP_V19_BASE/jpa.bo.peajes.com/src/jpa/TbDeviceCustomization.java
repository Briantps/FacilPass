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
 * The persistent class for the TB_DEVICE_CUSTOMIZATION database table.
 * @author tmolina
 *
 */
@Entity
@Table(name="TB_DEVICE_CUSTOMIZATION")
public class TbDeviceCustomization implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TB_DEVICE_CUSTOMIZATION_DEVICECUSTOMIZATIONID_GENERATOR")
	@SequenceGenerator(name="TB_DEVICE_CUSTOMIZATION_DEVICECUSTOMIZATIONID_GENERATOR", sequenceName="TB_DEVICE_CUSTOMIZATION_SEQ",allocationSize=1)
	@Column(name="DEVICE_CUSTOMIZATION_ID")
	private Long deviceCustomizationId;
	
	@ManyToOne
	@JoinColumn(name="OPERATION_TYPE_ID")
	private TbOperationType tbOperationType;
	
	@ManyToOne
	@JoinColumn(name="SPECIAL_EXEMPT_TYPE_ID")
	private TbSpecialExemptType tbSpecialExemptType;
	
	@Column(name="FILING_NUMBER")
	private String filingNumber;
	
	@Column(name="FILING_DATE")
	private Timestamp filingDate;
	
	@Column(name="OFFICIAL_DOCUMENT_NUMBER")
	private String officialDocumentNumber;
	
	@Column(name="OFFICIAL_DOCUMENT_DATE")
	private Timestamp officialDocuemntDate;
	
	@ManyToOne
	@JoinColumn(name="ACCOUNT_ID")
	private TbAccount tbAccount;
	
	@ManyToOne
	@JoinColumn(name="DEVICE_ID")
	private TbDevice tbDevice;
	
	@ManyToOne
	@JoinColumn(name="VEHICLE_ID")
	private TbVehicle tbVehicle;
	
	@Column(name="COR_RECEIVED_DATE")
	private Timestamp corReceivedDate;
	
	@ManyToOne
	@JoinColumn(name="SPECIAL_EXEMPT_OFFICE_ID")
	private TbSpecialExemptOffice tbSpecialExemptOffice;
	
	@ManyToOne
	@JoinColumn(name="DEVICE_CUSTOMIZATION_STATE_ID")
	private TbDeviceCustomizationState tbDeviceCustomizationState;
	
	@ManyToOne
	@JoinColumn(name="CREATION_REPOSITION_USER")
	private TbSystemUser creationRepositionUser;
	
	@Column(name="CREATION_REPOSITION_DATE")
	private Timestamp creationRepositionDate;
	
	@ManyToOne
	@JoinColumn(name="CUSTOMIZATION_USER")
	private TbSystemUser customizationUser;
	
	@Column(name="CUSTOMIZATION_DATE")
	private Timestamp customizationDate;
	
	@Column(name="CATEGORY_CONCESSION")
	private String categoryConcession;
	
	@Column(name="CATEGORY_INVIAS")
	private String categoryInvias;
	
	@ManyToOne
	@JoinColumn(name="DEPARTMENT_ID")
	private TbDepartment tbDepartment;
	
	@Column(name="PAY")
	private String pay;
	
	@Column(name="TYPED")
	private String typed;
	
	@Column(name="SURPLUS_VALUE")
	private Long surplusValue;
	
	@Column(name="OBSERVATION")
	private String observation;
	
	@ManyToOne
	@JoinColumn(name="REPLACEMENT_CAUSE_ID")
	private TbReplacementCause tbReplacementCause;
	
	@Column(name="BLACK_LIST")
	private String blackList;
	
	
	/**
	 * Constructor.
	 */
	public TbDeviceCustomization() {
		super();
	}

	/**
	 * @param deviceCustomizationId the deviceCustomizationId to set
	 */
	public void setDeviceCustomizationId(Long deviceCustomizationId) {
		this.deviceCustomizationId = deviceCustomizationId;
	}

	/**
	 * @return the deviceCustomizationId
	 */
	public Long getDeviceCustomizationId() {
		return deviceCustomizationId;
	}

	/**
	 * @param tbOperationType the tbOperationType to set
	 */
	public void setTbOperationType(TbOperationType tbOperationType) {
		this.tbOperationType = tbOperationType;
	}

	/**
	 * @return the tbOperationType
	 */
	public TbOperationType getTbOperationType() {
		return tbOperationType;
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

	/**
	 * @param filingNumber the filingNumber to set
	 */
	public void setFilingNumber(String filingNumber) {
		this.filingNumber = filingNumber;
	}

	/**
	 * @return the filingNumber
	 */
	public String getFilingNumber() {
		return filingNumber;
	}

	/**
	 * @param filingDate the filingDate to set
	 */
	public void setFilingDate(Timestamp filingDate) {
		this.filingDate = filingDate;
	}

	/**
	 * @return the filingDate
	 */
	public Timestamp getFilingDate() {
		return filingDate;
	}

	/**
	 * @param officialDocumentNumber the officialDocumentNumber to set
	 */
	public void setOfficialDocumentNumber(String officialDocumentNumber) {
		this.officialDocumentNumber = officialDocumentNumber;
	}

	/**
	 * @return the officialDocumentNumber
	 */
	public String getOfficialDocumentNumber() {
		return officialDocumentNumber;
	}

	/**
	 * @param officialDocuemntDate the officialDocuemntDate to set
	 */
	public void setOfficialDocuemntDate(Timestamp officialDocuemntDate) {
		this.officialDocuemntDate = officialDocuemntDate;
	}

	/**
	 * @return the officialDocuemntDate
	 */
	public Timestamp getOfficialDocuemntDate() {
		return officialDocuemntDate;
	}

	/**
	 * @param tbAccount the tbAccount to set
	 */
	public void setTbAccount(TbAccount tbAccount) {
		this.tbAccount = tbAccount;
	}

	/**
	 * @return the tbAccount
	 */
	public TbAccount getTbAccount() {
		return tbAccount;
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

	/**
	 * @param corReceivedDate the corReceivedDate to set
	 */
	public void setCorReceivedDate(Timestamp corReceivedDate) {
		this.corReceivedDate = corReceivedDate;
	}

	/**
	 * @return the corReceivedDate
	 */
	public Timestamp getCorReceivedDate() {
		return corReceivedDate;
	}

	/**
	 * @param tbSpecialExemptOffice the tbSpecialExemptOffice to set
	 */
	public void setTbSpecialExemptOffice(TbSpecialExemptOffice tbSpecialExemptOffice) {
		this.tbSpecialExemptOffice = tbSpecialExemptOffice;
	}

	/**
	 * @return the tbSpecialExemptOffice
	 */
	public TbSpecialExemptOffice getTbSpecialExemptOffice() {
		return tbSpecialExemptOffice;
	}

	/**
	 * @param tbDeviceCustomizationState the tbDeviceCustomizationState to set
	 */
	public void setTbDeviceCustomizationState(TbDeviceCustomizationState tbDeviceCustomizationState) {
		this.tbDeviceCustomizationState = tbDeviceCustomizationState;
	}

	/**
	 * @return the tbDeviceCustomizationState
	 */
	public TbDeviceCustomizationState getTbDeviceCustomizationState() {
		return tbDeviceCustomizationState;
	}

	/**
	 * @param creationRepositionUser the creationRepositionUser to set
	 */
	public void setCreationRepositionUser(TbSystemUser creationRepositionUser) {
		this.creationRepositionUser = creationRepositionUser;
	}

	/**
	 * @return the creationRepositionUser
	 */
	public TbSystemUser getCreationRepositionUser() {
		return creationRepositionUser;
	}

	/**
	 * @param creationRepositionDate the creationRepositionDate to set
	 */
	public void setCreationRepositionDate(Timestamp creationRepositionDate) {
		this.creationRepositionDate = creationRepositionDate;
	}

	/**
	 * @return the creationRepositionDate
	 */
	public Timestamp getCreationRepositionDate() {
		return creationRepositionDate;
	}

	/**
	 * @param customizationUser the customizationUser to set
	 */
	public void setCustomizationUser(TbSystemUser customizationUser) {
		this.customizationUser = customizationUser;
	}

	/**
	 * @return the customizationUser
	 */
	public TbSystemUser getCustomizationUser() {
		return customizationUser;
	}

	/**
	 * @param customizationDate the customizationDate to set
	 */
	public void setCustomizationDate(Timestamp customizationDate) {
		this.customizationDate = customizationDate;
	}

	/**
	 * @return the customizationDate
	 */
	public Timestamp getCustomizationDate() {
		return customizationDate;
	}

	/**
	 * @param categoryConcession the categoryConcession to set
	 */
	public void setCategoryConcession(String categoryConcession) {
		this.categoryConcession = categoryConcession;
	}

	/**
	 * @return the categoryConcession
	 */
	public String getCategoryConcession() {
		return categoryConcession;
	}

	/**
	 * @param categoryInvias the categoryInvias to set
	 */
	public void setCategoryInvias(String categoryInvias) {
		this.categoryInvias = categoryInvias;
	}

	/**
	 * @return the categoryInvias
	 */
	public String getCategoryInvias() {
		return categoryInvias;
	}

	/**
	 * @param tbDepartment the tbDepartment to set
	 */
	public void setTbDepartment(TbDepartment tbDepartment) {
		this.tbDepartment = tbDepartment;
	}

	/**
	 * @return the tbDepartment
	 */
	public TbDepartment getTbDepartment() {
		return tbDepartment;
	}

	/**
	 * @param pay the pay to set
	 */
	public void setPay(String pay) {
		this.pay = pay;
	}

	/**
	 * @return the pay
	 */
	public String getPay() {
		return pay;
	}

	/**
	 * @param typed the typed to set
	 */
	public void setTyped(String typed) {
		this.typed = typed;
	}

	/**
	 * @return the typed
	 */
	public String getTyped() {
		return typed;
	}

	/**
	 * @param surplusValue the surplusValue to set
	 */
	public void setSurplusValue(Long surplusValue) {
		this.surplusValue = surplusValue;
	}

	/**
	 * @return the surplusValue
	 */
	public Long getSurplusValue() {
		return surplusValue;
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
	 * @param tbReplacementCause the tbReplacementCause to set
	 */
	public void setTbReplacementCause(TbReplacementCause tbReplacementCause) {
		this.tbReplacementCause = tbReplacementCause;
	}

	/**
	 * @return the tbReplacementCause
	 */
	public TbReplacementCause getTbReplacementCause() {
		return tbReplacementCause;
	}

	/**
	 * @param blackList the blackList to set
	 */
	public void setBlackList(String blackList) {
		this.blackList = blackList;
	}

	/**
	 * @return the blackList
	 */
	public String getBlackList() {
		return blackList;
	}
}
