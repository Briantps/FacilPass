package ejb;
import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import constant.CustomizationState;
import constant.InclusionState;

import util.perti.InclusionDetail;
import util.perti.Personalization;

import jpa.TbDepartment;
import jpa.TbDevice;
import jpa.TbDeviceCustomization;
import jpa.TbDeviceType;
import jpa.TbInclusion;
import jpa.TbOperationType;
import jpa.TbReplacementCause;
import jpa.TbSpecialExemptOffice;
import jpa.TbSpecialExemptType;
import jpa.TbStationBO;

@Remote
public interface Master {
	
	/**
	 * Gets the device types for personalization: special or exempt 
	 * @return {@link TbDeviceType} {@link List}
	 */
	public List<TbDeviceType> getPertiDeviceTypes();
	
	/**
	 * Gets the operation types that  personalization of Tisc Uses, "New And Reposition"
	 * @return {@link TbOperationType} {@link List}
	 */
	public List<TbOperationType> getPertiOperationTypes();
	
	/**
	 * Gets the different types of special or exempt according the parameter. 
	 * @param deviceTypeId 
	 * @return {@link TbSpecialExemptType} {@link List}
	 */
	public List<TbSpecialExemptType> getSpecialExemptTypes(Long deviceTypeId);
	
	/**
	 * Gets the Exempt/Special type Offices. 
	 * @param specialExemptTypeId
	 * @return {@link TbSpecialExemptOffice} {@link List}
	 */
	public List<TbSpecialExemptOffice> getSpecialExemptOffice(Long specialExemptTypeId);
	
	/**
	 * 
	 * @return {@link TbDepartment} {@link List}
	 */
	public List<TbDepartment> getDepartments();
	
	/**
	 * 
	 * @param departmentId
	 * @return {@link TbStationBO} {@link List}
	 */
	public List<TbStationBO> getStationsByDepartment(Long departmentId);
	
	/**
	 * 
	 * @return {@link TbReplacementCause} {@link List}
	 */
	public List<TbReplacementCause> getReplacementCauses();
	
	/**
	 * 
	 * @param creationUser
	 * @param ip
	 * @param operationTypeId
	 * @param idType
	 * @param filingNumber
	 * @param filingDate
	 * @param corReceivedDate
	 * @param idCategory
	 * @param departmentId
	 * @param officialDocumentNumber
	 * @param officialDocuementDate
	 * @param payOption
	 * @param typedOption
	 * @param surplusValue
	 * @param observation
	 * @param causeId
	 * @param blackListOption
	 * @param idClientAccount
	 * @param vehicleId
	 * @param exemptOfficeId
	 * @param blackListOfficialNumber
	 * @param blackListOfficialDate
	 * @param blackListDate
	 * @param reported
	 * @param returned
	 * @param stationList
	 * @param especialExemptId
	 * @param oldDevice
	 * @return
	 */
	public boolean saveMaster(Long creationUser, String ip, Long operationTypeId, Long idType, String filingNumber,
			Date filingDate, Date corReceivedDate, Long idCategory, Long departmentId, String officialDocumentNumber,
			Date officialDocuementDate, String payOption, String typedOption, Long surplusValue, String observation,
			Long causeId, String blackListOption, Long idClientAccount, Long vehicleId, Long exemptOfficeId, String blackListOfficialNumber,
			Date blackListOfficialDate, Date blackListDate, String reported, String returned, List<TbStationBO> stationList,
			Long especialExemptId, TbDevice oldDevice);
	
	/**
	 * 
	 * @param deviceCustomizationStateId
	 * @return {@link TbDeviceCustomization} {@link List}
	 */
	public List<TbDeviceCustomization> getDeviceCustomizationByState(Long deviceCustomizationStateId);
	
	/**
	 * 
	 * @param deviceCustomizationId
	 * @param ip
	 * @param creationUser
	 * @return <code>true</code> is successful, <code>false</code> otherwise
	 */
	public boolean rejectMaster(Long deviceCustomizationId, String ip, Long creationUser);
	
	/**
	 * 
	 * @param deviceCustomizationId
	 * @param ip
	 * @param creationUser
	 * @param office
	 * @param property
	 * @param consig
	 * @return <code>true</code> is successful, <code>false</code> otherwise
	 */
	public boolean confirmMaster(Long deviceCustomizationId, String ip, Long creationUser,
			boolean office, boolean property, boolean consig);
	
	/**
	 * 
	 * @param deviceCustomizationStateId
	 * @param plate
	 * @return {@link TbDeviceCustomization} {@link List}
	 */
	public List<TbDeviceCustomization> getDeviceCustomizationByStatePlate(
			Long deviceCustomizationStateId, String plate);
	
	/**
	 * 
	 * @param plate Vehicle Plate
	 * @return String indicating if the plate has already a process related.
	 */
	public String validatePlate(String plate);
	
	/**
	 * 
	 * @param plate Vehicle Plate
	 * @return {@link TbDevice} (Old Device Associated with Plate) If device has been installed.
	 */
	public TbDevice getOldDeviceAssociatedWithPlate(String plate);
	
	/**
	 * Gets the device types for inclusion.
	 * @return {@link TbDeviceType} {@link List}
	 */
	public List<TbDeviceType> getInclusionDeviceTypes();
	
	/**
	 * 
	 * @param details
	 * @param ip
	 * @param creationUser
	 * @return Inclusion Number.
	 */
	public Long saveInclusion(List<InclusionDetail> details, String ip, Long creationUser);
	
	/**
	 * 
	 * @param inclusionNumber
	 * @return {@link TbInclusion}
	 */
	public TbInclusion getInclusionByNumber(Long inclusionNumber);
	
	/**
	 * 
	 * @param idState
	 * @return {@link TbInclusion} {@link List}
	 */
	public List<TbInclusion> getInclusionsByState(InclusionState idState);
	
	/**
	 * 
	 * @param inclusionId
	 * @return {@link InclusionDetail} {@link List}
	 */
	public List<InclusionDetail> getInclusionDetails(Long inclusionId);
	
	/**
	 * 
	 * @param searchByPlate
	 * @param searchByCardId
	 * @param cardId
	 * @param searchByCardFacial
	 * @param cardFacial
	 * @param plate
	 * @param deviceTypeId
	 * @return
	 */
	public TbDeviceCustomization getCustomization(boolean searchByPlate, boolean searchByCardId, String cardId,
			boolean searchByCardFacial, String cardFacial, String plate, Long deviceTypeId);
	
	/**
	 * 
	 * @param deviceCustomizationId
	 * @param installed
	 * @param installationCertificate
	 * @param installationSupport
	 * @param receiverName
	 * @param receiverIdentificationNumber
	 * @param installationDate
	 * @param locationId
	 * @param installationUserId
	 * @param creationUser
	 * @param ip
	 * @param observation
	 * @return <code>true</code> if successful, <code>false</code> otherwise.
	 */
	public boolean saveInstallation(Long deviceCustomizationId, String installed, String installationCertificate,
			String installationSupport, String receiverName, String receiverIdentificationNumber, Date installationDate,
			Long locationId, Long installationUserId, Long creationUser, String ip, String observation);
	
	/**
	 * 
	 * @param deviceCustomizationStateId
	 * @param operationTyepeId
	 * @return {@link TbDeviceCustomization} {@link List}
	 */
	public List<TbDeviceCustomization> getDeviceCustomizationByStateAndOperationType(
			Long deviceCustomizationStateId, Long operationTyepeId);
	
	/**
	 * 
	 * @param deviceCustomizationId
	 * @return {@link TbStation} {@link List}
	 */
	public List<TbStationBO> getSpecialStation(Long deviceCustomizationId);
	
	/**
	 * 
	 * @param deviceCustomizationId
	 * @param modUser
	 * @param ip
	 * @param idType
	 * @param filingNumber
	 * @param filingDate
	 * @param corReceivedDate
	 * @param idCategory
	 * @param departmentId
	 * @param officialDocumentNumber
	 * @param officialDocuementDate
	 * @param payOption
	 * @param typedOption
	 * @param surplusValue
	 * @param observation
	 * @param idClien
	 * @param vehicleId
	 * @param exemptOfficeId
	 * @param stationList
	 * @param especialExemptId
	 * @return
	 */
	public boolean saveFixMaster(Long deviceCustomizationId, Long modUser, String ip, Long idType, String filingNumber,
			Date filingDate, Date corReceivedDate, Long idCategory, Long departmentId, String officialDocumentNumber,
			Date officialDocuementDate, String payOption, String typedOption, Long surplusValue, String observation,
			Long idClientAccount, Long vehicleId, Long exemptOfficeId, List<TbStationBO> stationList, Long especialExemptId);
	
	/**
	 * 
	 * @param deviceCustomizationStateId
	 * @param plate
	 * @param chasis
	 * @param idType
	 * @param operationTypeId
	 * @return {@link TbDeviceCustomization} {@link List}
	 */
	public List<TbDeviceCustomization> getDeviceCustomization(
			Long deviceCustomizationStateId, String plate, String chasis, 
			Long idType, Long operationTypeId);
	
	/**
	 * 
	 * @return {@link InclusionDetail} {@link List}
	 */
	public List<InclusionDetail> getDeviceCustomizationsToInclude();
	
	/**
	 * 
	 * @param inclusionId
	 * @param details
	 * @param ip
	 * @param creationUser
	 * @return
	 */
	public boolean removeDetailInclusion(Long inclusionId,
			List<InclusionDetail> details, String ip, Long creationUser);
	
	/**
	 * 
	 * @param inclusionId
	 * @param detailsToAdd
	 * @param ip
	 * @param creationUser
	 * @return
	 */
	public boolean addDetailsToInclusion(Long inclusionId,
			List<InclusionDetail> detailsToAdd, String ip, Long creationUser);
	
	/**
	 * 
	 * @param inclusionId
	 * @param ip
	 * @param creationUser
	 * @return
	 */
	public boolean sendInclusion(Long inclusionId, String ip, Long creationUser);
	
	/**
	 * 
	 * @param customizationState
	 * @param operationTypeId
	 * @return {@link Personalization} {@link List}
	 */
	public List<Personalization> getSpecialDeviceCustomization(
			CustomizationState customizationState, Long operationTypeId);
	
	/**
	 * 
	 * @param customizationId
	 * @param ip
	 * @param creationUser
	 * @return
	 */
	public boolean savePersonalization(Long customizationId, String ip,
			Long creationUser);
}