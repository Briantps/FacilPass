package ejb;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;

import util.TimeUtil;
import util.perti.InclusionDetail;
import util.perti.Personalization;

import constant.CustomizationState;
import constant.DeviceState;
import constant.DeviceType;
import constant.InclusionState;
import constant.LogAction;
import constant.LogReference;
import constant.OperationType;
import constant.ProcessTrackDetailType;
import constant.ProcessTrackType;
import constant.StationType;

import crud.ObjectEM;

import jpa.TbAccount;
import jpa.TbCategory;
import jpa.TbDepartment;
import jpa.TbDevice;
import jpa.TbDeviceCustomization;
import jpa.TbDeviceCustomizationState;
import jpa.TbDeviceFacial;
import jpa.TbDeviceState;
import jpa.TbDeviceType;
import jpa.TbInclusion;
import jpa.TbInclusionDetail;
import jpa.TbInclusionState;
import jpa.TbInstallation;
import jpa.TbInstallationLocation;
import jpa.TbOperationType;
import jpa.TbProcessTrack;
import jpa.TbProcessTrackDetailType;
import jpa.TbReplacementCause;
import jpa.TbSpecialExemptOffice;
import jpa.TbSpecialExemptType;
import jpa.TbSpecialStation;
import jpa.TbStationBO;
import jpa.TbSystemUser;
import jpa.TbVehicle;

/**
 * Session Bean implementation class MasterEJB
 */
@Stateless(mappedName = "ejb/Master")
public class MasterEJB implements Master {

	@PersistenceContext(unitName = "bo")
	private EntityManager em;
	
   @EJB(mappedName = "ejb/Log")
	private Log log;
	
	@EJB(mappedName = "ejb/Process")
	private Process process;
	
	@EJB(mappedName="ejb/Task")
	private Task task;
	
	@EJB(mappedName = "ejb/Device")
	private Device deviceEJB;
	
	private ObjectEM emObj;
	
    /**
     * Default constructor. 
     */
    public MasterEJB() {
    }

   /*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Master#getPertiDeviceTypes()
	 */
	@Override
	public List<TbDeviceType> getPertiDeviceTypes() {
		List<TbDeviceType> list = new ArrayList<TbDeviceType>();
		try{
			Query q = em.createQuery("SELECT dt FROM TbDeviceType dt WHERE dt.deviceTypeId IN (4, 5, 9)");
			for (Object obj : q.getResultList()) {
				list.add((TbDeviceType) obj);
			}
		}catch (Exception e) {
			System.out.println(" [ Error en MasterEJB.getPertiDeviceTypes ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Master#getPertiOperationTypes()
	 */
	@Override
	public List<TbOperationType> getPertiOperationTypes() {
		List<TbOperationType> list = new ArrayList<TbOperationType>();
		try {
			Query q = em.createQuery("SELECT ot FROM TbOperationType ot WHERE ot.operationTypeId IN (2, 3)");
			for (Object obj : q.getResultList()) {
				list.add((TbOperationType) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en MasterEJB.getPertiOperationTypes ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Master#getSpecialExemptTypes(java.lang.Long)
	 */
	@Override
	public List<TbSpecialExemptType> getSpecialExemptTypes(Long deviceTypeId) {
		List<TbSpecialExemptType> list = new ArrayList<TbSpecialExemptType>();
		try {
			Query q = em.createQuery("SELECT ee FROM TbSpecialExemptType ee WHERE ee.tbDeviceType.deviceTypeId = ?1");
			q.setParameter(1, deviceTypeId);
			for (Object obj : q.getResultList()) {
				list.add((TbSpecialExemptType) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en MasterEJB.getSpecialExemptTypes ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Master#getSpecialExemptOffice(java.lang.Long)
	 */
	@Override
	public List<TbSpecialExemptOffice> getSpecialExemptOffice(
			Long specialExemptTypeId) {
		List<TbSpecialExemptOffice> list = new ArrayList<TbSpecialExemptOffice>();
		try {
			Query q = em.createQuery("SELECT eo FROM TbSpecialExemptOffice eo WHERE eo.tbSpecialExemptType.specialExemptTypeId = ?1");
			q.setParameter(1, specialExemptTypeId);
			for (Object obj : q.getResultList()) {
				list.add((TbSpecialExemptOffice) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en MasterEJB.getSpecialExemptOffice ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Master#getDepartments()
	 */
	@Override
	public List<TbDepartment> getDepartments() {
		List<TbDepartment> list = new ArrayList<TbDepartment>();
		try {
			Query q = em.createQuery("SELECT dp FROM TbDepartment dp");
			for (Object obj : q.getResultList()) {
				list.add((TbDepartment) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en MasterEJB.getDepartments ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Master#getStationsByDepartment(java.lang.Long)
	 */
	@Override
	public List<TbStationBO> getStationsByDepartment(Long departmentId) {
		List<TbStationBO> list = new ArrayList<TbStationBO>();
		try {
			Query q = em.createQuery("SELECT sbo FROM TbStationBO sbo " +
					"WHERE sbo.tbStationType.stationTypeId = ?1 " +
					"AND sbo.tbDepartment.departmentId = ?2");
			q.setParameter(1, StationType.COLLECTION_STATION.getId());
			q.setParameter(2, departmentId);
			
			for (Object obj : q.getResultList()) {
				list.add((TbStationBO) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en MasterEJB.getStationsByDepartment ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see ejb.Master#getReplacementCauses()
	 */
	@Override
	public List<TbReplacementCause> getReplacementCauses() {
		List<TbReplacementCause> list = new ArrayList<TbReplacementCause>();
		try {
			Query q = em.createQuery("SELECT trc FROM TbReplacementCause trc");
			for (Object obj : q.getResultList()) {
				list.add((TbReplacementCause) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en MasterEJB.getReplacementCauses ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Master#saveMaster(java.lang.Long, java.lang.String,
	 * java.lang.Long, java.lang.Long, java.lang.String, java.util.Date,
	 * java.util.Date, java.lang.Long, java.lang.Long, java.lang.String,
	 * java.util.Date, java.lang.String, java.lang.String, java.lang.Long,
	 * java.lang.String, java.lang.Long, java.lang.String, java.lang.Long,
	 * java.lang.Long, java.lang.Long, java.lang.String, java.util.Date,
	 * java.util.Date, java.lang.String, java.lang.String, java.util.List,
	 * java.lang.Long, jpa.TbDevice)
	 */
	@Override
	public boolean saveMaster(Long creationUser, String ip,
			Long operationTypeId, Long idType, String filingNumber,
			Date filingDate, Date corReceivedDate, Long idCategory,
			Long departmentId, String officialDocumentNumber,
			Date officialDocuementDate, String payOption, String typedOption,
			Long surplusValue, String observation, Long causeId,
			String blackListOption, Long idClientAccount, Long vehicleId,
			Long exemptOfficeId, String blackListOfficialNumber,
			Date blackListOfficialDate, Date blackListDate, String reported,
			String returned, List<TbStationBO> stationList, Long especialExemptId, 
			TbDevice oldDevice) {
		try {
			
			// Start creating the row.
			TbDeviceCustomization cus = new TbDeviceCustomization();
			
			cus.setFilingNumber(filingNumber);
			cus.setFilingDate(new Timestamp(filingDate.getTime()));
			cus.setOfficialDocumentNumber(officialDocumentNumber);
			cus.setOfficialDocuemntDate(new Timestamp(officialDocuementDate.getTime()));
			cus.setCorReceivedDate(new Timestamp(corReceivedDate.getTime()));
			
			// New Record.
			cus.setTbDeviceCustomizationState(em.find(TbDeviceCustomizationState.class, CustomizationState.NEW.getId())); 
			
			cus.setCreationRepositionDate(new Timestamp(System.currentTimeMillis()));
			cus.setCustomizationDate(null);
			
			// Setting the Categories.
			TbCategory cat = em.find(TbCategory.class, idCategory);
			cus.setCategoryConcession(cat.getCategoryCode());
			cus.setCategoryInvias(cat.getCategoryInviasCode());
			
			cus.setPay(payOption);
			cus.setTyped(typedOption);
			cus.setSurplusValue(surplusValue);
			if (observation == null) {
				cus.setObservation(" ");
			} else {
				cus.setObservation(observation);
			}
			cus.setBlackList(blackListOption);
			
			cus.setTbOperationType(em.find(TbOperationType.class, operationTypeId));
			cus.setTbSpecialExemptType(em.find(TbSpecialExemptType.class, especialExemptId ));
			
			if(idType.longValue() == DeviceType.EXEMPT.getId()){ // Exempt
				cus.setTbSpecialExemptOffice(em.find(TbSpecialExemptOffice.class, exemptOfficeId));
				cus.setTbDepartment(em.find(TbDepartment.class, departmentId));
			}
			
			// Searching the account.
			
			cus.setTbAccount(em.find(TbAccount.class, idClientAccount));
			cus.setTbDevice(null);
			cus.setTbVehicle(em.find(TbVehicle.class, vehicleId));
			
			cus.setCreationRepositionUser(em.find(TbSystemUser.class, creationUser));
			cus.setCustomizationUser(null);
			
			if (operationTypeId.longValue() == OperationType.REPLACEMENT.getId()) {
				cus.setTbReplacementCause(em.find(TbReplacementCause.class, causeId));
			} 
			
			// save
			emObj = new ObjectEM(em);
			
			if (emObj.create(cus)) {
				log.insertLog(
						"Creación Personalización | Se ha creado la personalización ID: " + cus.getDeviceCustomizationId() + ".",
						LogReference.CUSTOMIZATION, LogAction.CREATE, ip, creationUser);
					
				if(idType.longValue() != DeviceType.EXEMPT.getId()){
					// Creating the relationship with stations if special. 
					TbDeviceCustomization dc = em.find(TbDeviceCustomization.class, cus.getDeviceCustomizationId());
				
					for (TbStationBO s : stationList) {
						TbSpecialStation ss = new TbSpecialStation();
						ss.setTbDeviceCustomization(dc);
						ss.setTbStationBO(em.find(TbStationBO.class, s.getStationBOId()));
						
						if (emObj.create(ss)) {
							log.insertLog(
									"Creación Personalización | Se ha creado la relación estación-especial ID: " + ss.getSpecialStationId() + ".",
									LogReference.SPECIAL, LogAction.CREATE, ip, creationUser);
						} else {
							log.insertLog(
									"Creación Personalización | No se ha podido crear la relación estación-especial ID personalización:  " 
									+ cus.getDeviceCustomizationId() +  " con la estación Código: " + s.getStationBOCode(),
									LogReference.SPECIAL, LogAction.CREATEFAIL, ip, creationUser);
						}
					}
				}
				
				// When Replacement, last device associated with vehicle, goes to black list. 
//				if (operationTypeId.longValue() == 3L) { 
//					TbBlackList bl = new TbBlackList();
//					bl.setBlackListDate(new Timestamp(blackListDate.getTime()));
//					bl.setBlackListOfficialDate(new Timestamp(blackListOfficialDate.getTime()));
//					bl.setBlackListOfficialNumber(blackListOfficialNumber);
//					bl.setReported(reported);
//					bl.setReturned(returned);
					
//					TbDevice device = em.find(TbDevice.class, oldDevice.getDeviceId());
//					bl.setTbDevice(device);
					
					// If created
//					if(emObj.create(bl)){
//						log.insertLog("Creación Personalización | Se ha enviado el dispositivo ID " + oldDevice.getDeviceId()
//								+ " a listas Negras.", 
//								LogReference.BLACKLIST, LogAction.CREATE, ip, creationUser);
//						
//						// Changing the device state.
//						device.setTbDeviceState(em.find(TbDeviceState.class, 9L));
//						if (emObj.update(device)) {
//							log.insertLog("Creación Personalización | Se ha cambiado el estado del dispositivo ID: " + oldDevice.getDeviceId()
//									+ " a: dispositivo en listas Negras.", 
//									LogReference.DEVICE, LogAction.UPDATE, ip, creationUser);
//							
//							// Searching the device process to indicate the state change.
//							TbProcessTrack pt = process.searchProcess(ProcessTrackType.DEVICE, device.getDeviceId());
//							if (pt != null) {
//								process.createProcessDetail(pt.getProcessTrackId(), ProcessTrackDetailType.DEVICE_BLACK_LIST, 
//										"Se ha enviado el dispositivo a Listas Negras."
//										, creationUser, ip, 1, "No se ha podido indicar en el proceso que se ha enviado el dispositivo ID: " +
//										device.getDeviceId() + " a listas negras, Proceso ID: "+ pt.getProcessTrackId() + "." );
//							}
//
//						} else {
//							log.insertLog("Creación Personalización | No se pudo indicar que el dispositivo ID " + oldDevice.getDeviceId()
//									+ " pasó a listas Negras.", 
//									LogReference.DEVICE, LogAction.UPDATEFAIL, ip, creationUser);
//						}
//					} else {
//						log.insertLog("Creación Personalización | No se pudo  enviar el dispositivo ID " + oldDevice.getDeviceId()
//								+ " a listas Negras.", 
//								LogReference.BLACKLIST, LogAction.CREATEFAIL, ip, creationUser);
//					}
//				}
				
				// Creating the customization process.
				Long processId = process.createProcessTrack(ProcessTrackType.CUSTOMIZATION, cus
						.getDeviceCustomizationId(), ip, creationUser);
				
				if (processId != null) { // If process was created, then create the detail.
					process.createProcessDetail(processId, ProcessTrackDetailType.CUSTOMIZATION_CREATION, 
							"Se ha creado un proceso de Personalización correspondiente a una Tarjeta " + cus.getTbOperationType().getOperationTypeName()
							+ " - (" + cus.getTbSpecialExemptType().getTbDeviceType().getDeviceTypeName() 
							+ ") asociado a la placa: " + cus.getTbVehicle().getVehiclePlate() + ".", 
							creationUser, ip, 1,
							"No se ha podido crear el detalle tipo: CREACIÓN DE PERSONALIZACIÓN, asociado al proceso: " + processId + " y a la personalización ID: " + cus.getDeviceCustomizationId()+ ".",null,null,null,null);
				}
				return true;
			} else {
				log.insertLog(
								"Creación Personalización | No se pudo crear la Personalización",
								LogReference.CUSTOMIZATION, LogAction.CREATEFAIL, ip, creationUser);
			}
			
		} catch (Exception e) {
			System.out.println(" [ Error en MasterEJB.saveMaster ] ");
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Master#getDeviceCustomizationByState(java.lang.Long)
	 */
	@Override
	public List<TbDeviceCustomization> getDeviceCustomizationByState(
			Long deviceCustomizationStateId) {
		List<TbDeviceCustomization> list = new ArrayList<TbDeviceCustomization>();
		try {
			Query q = em.createQuery("SELECT dc FROM TbDeviceCustomization dc " +
					"WHERE dc.tbDeviceCustomizationState.deviceCustomizationStateId =?1");
			q.setParameter(1, deviceCustomizationStateId);
			for (Object obj : q.getResultList()) {
				list.add((TbDeviceCustomization) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en MasterEJB.getDeviceCustomizationByState ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see ejb.Master#rejectMaster(java.lang.Long, java.lang.String, java.lang.Long)
	 */
	@Override
	public boolean rejectMaster(Long deviceCustomizationId, String ip,
			Long creationUser) {
		try {
			TbDeviceCustomization cus = em.find(TbDeviceCustomization.class, deviceCustomizationId);
			cus.setTbDeviceCustomizationState(em.find(TbDeviceCustomizationState.class, CustomizationState.REJECTED.getId()));
			
			// Update
			emObj = new ObjectEM(em);
			
			if (emObj.update(cus)) {
				log.insertLog("Autorización | Se ha rechazado la Personalización ID: " + cus.getDeviceCustomizationId(), 
						LogReference.CUSTOMIZATION, LogAction.UPDATE, ip, creationUser);
				
				// Searching the process 
				TbProcessTrack pt = process.searchProcess(ProcessTrackType.CUSTOMIZATION, deviceCustomizationId);
				
				// It was rejected, so change the process track state to inactive = 1
				pt.setProcessTrackState(1);
				
				if (emObj.update(pt)) {
					log.insertLog("Autorización | Se ha actualizado el estado del proceso ID: " + pt.getProcessTrackId(), 
							LogReference.PROCESS, LogAction.UPDATE, ip, creationUser);
					
					// If updating create a detail for information
					process.createProcessDetail(pt.getProcessTrackId(), ProcessTrackDetailType.CUSTOMIZATION_REJECTION, 
							"Se ha rechazado la Personalización ("+ cus.getTbSpecialExemptType().getTbDeviceType().getDeviceTypeName() + ") asociada a la Placa: " +
							cus.getTbVehicle().getVehiclePlate(), creationUser, ip, 1, "No se ha podido indicar en el proceso ID: " + pt.getProcessTrackId() +
							" que se ha rechazado dicho proceso de personalización.",null,null,null,null);
					
				} else{
					log.insertLog("Autorización | No se pudo actualizar el estado del proceso ID: " + pt.getProcessTrackId(), 
							LogReference.PROCESS, LogAction.UPDATEFAIL, ip, creationUser);
				}
				return true;
			} else {
				log.insertLog("Autorización | No se ha podido actualizar indicando que se ha rechazado la Personalización ID: " + cus.getDeviceCustomizationId(), 
						LogReference.CUSTOMIZATION, LogAction.UPDATEFAIL, ip, creationUser);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en MasterEJB.rejectMaster ] ");
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Master#confirmMaster(java.lang.Long, java.lang.String,
	 * java.lang.Long, boolean, boolean, boolean)
	 */
	@Override
	public boolean confirmMaster(Long deviceCustomizationId, String ip,
			Long creationUser, boolean office, boolean property, boolean consig) {
		try {
			TbDeviceCustomization cus = em.find(TbDeviceCustomization.class,
					deviceCustomizationId);
			cus.setTbDeviceCustomizationState(em.find(
					TbDeviceCustomizationState.class,
					CustomizationState.APPROVED.getId()));
			
			// Update
			emObj = new ObjectEM(em);
			
			if (emObj.update(cus)) {
				log.insertLog("Autorización | Se ha confirmado la Personalización ID: " + cus.getDeviceCustomizationId(), 
						LogReference.CUSTOMIZATION, LogAction.UPDATE, ip, creationUser);
				
				// Searching the process 
				TbProcessTrack pt = process.searchProcess(ProcessTrackType.CUSTOMIZATION, deviceCustomizationId);
				
				if (pt != null) {
					// If updating create a detail for information
					
					String docs = "Documentos Verificados: Oficio, Tarjeta de Propiedad" ;
					
					if(consig){
						docs += " y Consignación";
					}
					
					process.createProcessDetail(pt.getProcessTrackId(), ProcessTrackDetailType.CUSTOMIZATION_CONFIRMATION, 
							"Se ha Confirmado la Personalización ("+ cus.getTbSpecialExemptType().getTbDeviceType().getDeviceTypeName() + ") asociada a la Placa: " +
							cus.getTbVehicle().getVehiclePlate() + ". " + docs + ".", creationUser, ip, 1, "No se ha podido indicar en el proceso ID: " + pt.getProcessTrackId() +
							" que se ha confirmado dicho proceso de personalización.",null,null,null,null);
				} 
				
				return true;
			} else {
				log.insertLog("Autorización | No se ha podido actualizar indicando que se ha confirmado la Personalización ID: " + cus.getDeviceCustomizationId(), 
						LogReference.CUSTOMIZATION, LogAction.UPDATEFAIL, ip, creationUser);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en MasterEJB.confirmMaster ] ");
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Master#getDeviceCustomizationByStatePlate(java.lang.Long,
	 * java.lang.String)
	 */
	@Override
	public List<TbDeviceCustomization> getDeviceCustomizationByStatePlate(
			Long deviceCustomizationStateId, String plate) {
		List<TbDeviceCustomization> list = new ArrayList<TbDeviceCustomization>();
		try {
			Query q = em.createQuery("SELECT dc FROM TbDeviceCustomization dc " +
					"WHERE dc.tbDeviceCustomizationState.deviceCustomizationStateId =?1 " +
					"AND dc.tbVehicle.vehiclePlate = ?2");
			q.setParameter(1, deviceCustomizationStateId);
			q.setParameter(2, plate);
			for (Object obj : q.getResultList()) {
				list.add((TbDeviceCustomization) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en MasterEJB.getDeviceCustomizationByStatePlate ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see ejb.Master#validatePlate(java.lang.String)
	 */
	@Override
	public String validatePlate(String plate) {
		try {
			String result = null;
			Query q = em.createQuery("SELECT cu FROM TbDeviceCustomization cu " +
					" WHERE cu.tbVehicle.vehiclePlate = ?1 " +
					" ORDER BY cu.deviceCustomizationId DESC ").setMaxResults(1);
			q.setParameter(1, plate.toUpperCase());
			
			TbDeviceCustomization cus = (TbDeviceCustomization) q.getSingleResult();
				result = "La Placa ya tiene un proceso de personalización asociado, su estado actual es : "+ 
					cus.getTbDeviceCustomizationState().getCustomizationStateName() + ".";
			
			return result;
		} catch (NoResultException e) {
		} catch (Exception e) {
			System.out.println(" [ Error en MasterEJB.validatePlate ] ");
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Master#getOldDeviceAssociatedWithPlate(java.lang.String)
	 */
	@Override
	public TbDevice getOldDeviceAssociatedWithPlate(String plate) {
		try {
			Query q = em.createQuery("SELECT cu FROM TbDeviceCustomization cu " +
					" WHERE cu.tbVehicle.vehiclePlate = ?1 " +
					" ORDER BY cu.deviceCustomizationId DESC ").setMaxResults(1);
			q.setParameter(1, plate);
			
			TbDeviceCustomization cus = (TbDeviceCustomization) q.getSingleResult();
			if (cus.getTbDeviceCustomizationState().getDeviceCustomizationStateId().longValue() == CustomizationState.INSTALLED
					.getId().longValue()) {
				return cus.getTbDevice();
			}
		
		} catch (NoResultException e) {
		} catch (Exception e) {
			System.out.println(" [ Error en MasterEJB.getOldDeviceAsociatedWithPlate ] ");
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see ejb.Master#getInclusionDeviceTypes()
	 */
	@Override
	public List<TbDeviceType> getInclusionDeviceTypes() {
		List<TbDeviceType> list = new ArrayList<TbDeviceType>();
		try{
			Query q = em.createQuery("SELECT dt FROM TbDeviceType dt WHERE dt.deviceTypeId IN (5, 9)");
			for (Object obj : q.getResultList()) {
				list.add((TbDeviceType) obj);
			}
		}catch (Exception e) {
			System.out.println(" [ Error en MasterEJB.getPertiDeviceTypes ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Master#saveInclusion(java.util.List, java.lang.String,
	 * java.lang.Long)
	 */
	@Override
	public Long saveInclusion(List<InclusionDetail> details, String ip,
			Long creationUser) {
		try {
			// saving inclusion
			TbInclusion in = new TbInclusion();
			in.setInclusionDate(new Timestamp(System.currentTimeMillis()));
			in.setTbInclusionState(em.find(TbInclusionState.class, InclusionState.NEW.getId()));
			in.setCreationUser(em.find(TbSystemUser.class, creationUser));
			
			emObj = new ObjectEM(em);
			if (emObj.create(in)) {
				// save log
				log.insertLog("Guardar Inclusión | Se ha guardado la inclusión ID:" + in.getInclusionId() + ".", LogReference.INCLUSION, 
						LogAction.CREATE, ip, creationUser);
				
				// creating details
				for (InclusionDetail d : details) {
					if(d.isSelected()){
						TbInclusionDetail det = new TbInclusionDetail();
						det.setTbInclusion(in);
						det.setTbDeviceCustomization(em.find(
								TbDeviceCustomization.class, d.getCustomization()
										.getDeviceCustomizationId()));
						if(emObj.create(det)) {
							log.insertLog("Guardar Inclusión | Se ha guardado el detalle de inclusión ID: " + det.getInclusionDetailId() +  ".",
									LogReference.INCLUSION, LogAction.CREATE, ip, creationUser);
						} else {
							log.insertLog("Guardar Inclusión | No se pudo guadar el detalle de la inclusión ID: " + in.getInclusionId()
									+ " asociado a la personalización : " + d.getCustomization().getDeviceCustomizationId() +  ".",
									LogReference.INCLUSION, LogAction.CREATEFAIL, ip, creationUser);
						} // end create detail
					}
				}// end for details
				
				Query q = em.createNativeQuery("SELECT inclusion_number FROM tb_inclusion WHERE inclusion_id = ?1");
				q.setParameter(1, in.getInclusionId());
				
				Long inNunmber = Long.parseLong(q.getSingleResult().toString());
				
				// Creating a process for the inclusion
				Long proc = process.createProcessTrack(ProcessTrackType.INCLUSION, in.getInclusionId(), ip, creationUser);
				
				// Creating the detail 
				process.createProcessDetail(proc, ProcessTrackDetailType.INCLUSION_CREATION, 
						"Se ha creado La inclusión No. " +inNunmber + ".", creationUser, ip, 1,
						"No se ha podido indicar en el proceso que se ha creado la inclusión No. " + inNunmber + ".",null,null,null,null);
				
				return inNunmber;			
			} else {
				log.insertLog("Guardar Inclusión | No se pudo guadar la inclusión.", LogReference.INCLUSION, 
						LogAction.CREATEFAIL, ip, creationUser);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en MasterEJB.saveInclusion ] ");
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see ejb.Master#getInclusionByNumber(java.lang.Long)
	 */
	@Override
	public TbInclusion getInclusionByNumber(Long inclusionNumber) {
		try {
			Query q = em.createQuery("SELECT ti FROM TbInclusion ti WHERE ti.inclusionNumber =?1");
			q.setParameter(1, inclusionNumber);
			
			return (TbInclusion) q.getSingleResult();
		}catch (NoResultException e) {
		} catch (Exception e) {
			System.out.println(" [ Error en MasterEJB.getInclusionByNumber ] ");
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see ejb.Master#getInclusionsByState(constant.InclusionState)
	 */
	@Override
	public List<TbInclusion> getInclusionsByState(InclusionState idState) {
		List<TbInclusion> list = new ArrayList<TbInclusion>();
		try {
			Query q = em.createQuery("SELECT ti FROM TbInclusion ti WHERE ti.tbInclusionState.inclusionStateId = ?1 ORDER BY ti.inclusionNumber");
			q.setParameter(1, idState.getId());
			for (Object obj : q.getResultList()) {
				list.add((TbInclusion) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en MasterEJB.getInclusionsByState ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see ejb.Master#getInclusionDetails(java.lang.Long)
	 */
	@Override
	public List<InclusionDetail> getInclusionDetails(Long inclusionId) {
		List<InclusionDetail> list = new ArrayList<InclusionDetail>();
		try {
			Query q = em.createQuery("SELECT id FROM TbInclusionDetail id WHERE id.tbInclusion.inclusionId = ?1");
			q.setParameter(1, inclusionId);
	
			for(Object obj: q.getResultList()){
				TbInclusionDetail det = (TbInclusionDetail) obj;
				
				InclusionDetail in = new InclusionDetail();
				in.setSelected(true);
				in.setCustomization(em.find(TbDeviceCustomization.class,
								det.getTbDeviceCustomization()
										.getDeviceCustomizationId()));
				in.setDetail(det);
				String dep = "";
				String sta = "";
				
				Query q1 = em.createQuery("SELECT ss FROM TbSpecialStation ss " +
						" WHERE ss.tbDeviceCustomization.deviceCustomizationId = ?1");
				q1.setParameter(1, det.getTbDeviceCustomization().getDeviceCustomizationId());
					
				for (Object ob : q1.getResultList()) {
					TbSpecialStation ss = (TbSpecialStation) ob;

					dep = ss.getTbStationBO().getTbDepartment().getDepartmentName();
					sta += " " + ss.getTbStationBO().getStationBOName() + " -";
				}
				sta = sta.substring(0, sta.length() - 1);
					
				in.setStation(sta);
				in.setReg(dep);
				
				list.add(in);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en MasterEJB.getInclusionDetails ] ");
			e.printStackTrace();
		}
		return list;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Master#getCustomization(boolean, boolean, java.lang.String,
	 * boolean, java.lang.String, java.lang.String, java.lang.Long)
	 */
	@Override
	public TbDeviceCustomization getCustomization(boolean searchByPlate,
			boolean searchByCardId, String cardId, boolean searchByCardFacial,
			String cardFacial, String plate, Long deviceTypeId) {
		try {
			String query = "SELECT cu FROM TbDeviceCustomization cu " +
					" WHERE cu.tbDeviceCustomizationState.deviceCustomizationStateId NOT IN (2) " +
					" AND cu.tbSpecialExemptType.tbDeviceType.deviceTypeId = ?1 ";
			if(searchByPlate) {
				query += " AND cu.tbVehicle.vehiclePlate = ?2 ";
			}
			if(searchByCardFacial){
				query+=" AND cu.tbDevice.deviceFacialId = ?3 ";
			}
			if(searchByCardId) {
				query+=" AND cu.tbDevice.deviceCode = ?4 ";
			}
			query += " ORDER BY cu.deviceCustomizationId DESC ";
			
			Query q = em.createQuery(query).setMaxResults(1);
			q.setParameter(1, deviceTypeId);
			if(searchByPlate) {
				q.setParameter(2, plate.toUpperCase());
			}
			if(searchByCardFacial) {
				q.setParameter(3, cardFacial);
			}
			if(searchByCardId) {
				q.setParameter(4, cardId);
			}
			
			TbDeviceCustomization cus = (TbDeviceCustomization) q.getSingleResult();
			// If state = PERSONALIZADA 
			if (cus.getTbDeviceCustomizationState()
					.getDeviceCustomizationStateId().longValue() == CustomizationState.CUSTOMIZED
					.getId().longValue()) {
				return cus;
			}
		
		} catch (NoResultException e) {
		} catch (Exception e) {
			System.out.println(" [ Error en MasterEJB.getCustomization ] ");
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Master#saveInstallation(java.lang.Long, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.util.Date, java.lang.Long, java.lang.Long, java.lang.Long,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public boolean saveInstallation(Long deviceCustomizationId,
			String installed, String installationCertificate,
			String installationSupport, String receiverName,
			String receiverIdentificationNumber, Date installationDate,
			Long locationId, Long installationUserId, Long creationUser,
			String ip, String observation) {
		try {
			
			TbDeviceCustomization cus = em.find(TbDeviceCustomization.class, deviceCustomizationId);
			
			// Creating the installation Object.
			TbInstallation in = new TbInstallation();
			in.setCreationDate(new Timestamp(System.currentTimeMillis()));
			in.setCreationUser(em.find(TbSystemUser.class, creationUser));
			in.setInstallationCertificate(installationCertificate);
			in.setInstallationDate(new Timestamp(installationDate.getTime()));
			in.setInstallationSupport(installationSupport);
			in.setInstallationUser(em.find(TbSystemUser.class, installationUserId));
			in.setInstalled(installed);
			in.setObservation(observation);
			in.setReceiverIdentificationNumber(receiverIdentificationNumber);
			in.setReceiverName(receiverName);
			in.setTbDeviceCustomization(cus);
			in.setTbInstallationLocation(em.find(TbInstallationLocation.class, locationId));
			
			emObj = new ObjectEM(em);
			
			if (emObj.create(in)) {
				// save log.
				log.insertLog("Registro de Instalación TIES | Se ha creado el registro de instalación ID: " + in.getInstallationId() + ".",
						LogReference.INSTALLATIONTIES, LogAction.CREATE, ip, creationUser);
				
				// then changing the state at TbDeviceCustomization table to 5 = Installed.
				cus.setTbDeviceCustomizationState(em.find(TbDeviceCustomizationState.class, CustomizationState.INSTALLED.getId()));
				
				if(emObj.update(cus)) {
					
					log.insertLog("Registro de Instalación TIES | Se ha cambiado el estado a: Instalada de la personalización ID: " + cus.getDeviceCustomizationId() + ".",
							LogReference.CUSTOMIZATION, LogAction.UPDATEFAIL, ip, creationUser);
					
					// Searching the process to indicate.
					TbProcessTrack pt = process.searchProcess(ProcessTrackType.CUSTOMIZATION, cus.getDeviceCustomizationId());
					
					if(pt!=null){
						process.createProcessDetail(pt.getProcessTrackId(), ProcessTrackDetailType.CUSTOMIZATION_INSTALLATION, 
								"Se ha Instalado la tarjeta en el vehículo con placa: " + cus.getTbVehicle().getVehiclePlate() + ".",
								creationUser, ip, 1, "No se ha podido indicar en el proceso ID: " + pt.getProcessTrackId() + 
								" que la tarjeta ha sido instalada.",null,null,null,null);
					}
					return true;
				} else {
					log.insertLog("Registro de Instalación TIES | No se pudo indicar que la personalización ID: " + cus.getDeviceCustomizationId() + " paso a estado: Instalada.",
							LogReference.CUSTOMIZATION, LogAction.UPDATEFAIL, ip, creationUser);
				}
				
			} else {
				// if fail
				log.insertLog("Registro de Instalación TIES | No se ha podido registrar la instalación asociada al proceso " +
						"de personalización ID:" + cus.getDeviceCustomizationId() + ".",
						LogReference.INSTALLATIONTIES, LogAction.CREATEFAIL, ip, creationUser);
			}
			
		} catch (Exception e) {
			System.out.println(" [ Error en MasterEJB.saveInstallation ] ");
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ejb.Master#getDeviceCustomizationByStateAndOperationType(java.lang.Long,
	 * java.lang.Long)
	 */
	@Override
	public List<TbDeviceCustomization> getDeviceCustomizationByStateAndOperationType(
			Long deviceCustomizationStateId, Long operationTyepeId) {
		List<TbDeviceCustomization> list = new ArrayList<TbDeviceCustomization>();
		try {
			Query q = em.createQuery("SELECT dc FROM TbDeviceCustomization dc " +
					"WHERE dc.tbDeviceCustomizationState.deviceCustomizationStateId =?1 " +
					"AND dc.tbOperationType.operationTypeId = ?2");
			q.setParameter(1, deviceCustomizationStateId);
			q.setParameter(2, operationTyepeId);
			for (Object obj : q.getResultList()) {
				list.add((TbDeviceCustomization) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en MasterEJB.getDeviceCustomizationByStateAndOperationType ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Master#getSpecialStation(java.lang.Long)
	 */
	@Override
	public List<TbStationBO> getSpecialStation(Long deviceCustomizationId) {
		List<TbStationBO> list = new ArrayList<TbStationBO>();
		try {
			Query q = em.createQuery("SELECT ss FROM TbSpecialStation ss WHERE ss.tbDeviceCustomization.deviceCustomizationId = ?1");
			q.setParameter(1, deviceCustomizationId);
			
			for(Object ob: q.getResultList()){
				TbSpecialStation ss = (TbSpecialStation) ob;
				list.add(em.find(TbStationBO.class, ss.getTbStationBO().getStationBOId()));
			}
		} catch (Exception e) {
			System.out.println(" [ Error en MasterEJB.getSpecialStation ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Master#saveFixMaster(java.lang.Long, java.lang.Long,
	 * java.lang.String, java.lang.Long, java.lang.String, java.util.Date,
	 * java.util.Date, java.lang.Long, java.lang.Long, java.lang.String,
	 * java.util.Date, java.lang.String, java.lang.String, java.lang.Long,
	 * java.lang.String, java.lang.Long, java.lang.Long, java.lang.Long,
	 * java.util.List, java.lang.Long)
	 */
	@Override
	public boolean saveFixMaster(Long deviceCustomizationId, Long modUser,
			String ip, Long idType, String filingNumber, Date filingDate,
			Date corReceivedDate, Long idCategory, Long departmentId,
			String officialDocumentNumber, Date officialDocuementDate,
			String payOption, String typedOption, Long surplusValue,
			String observation, Long idClientAccount, Long vehicleId,
			Long exemptOfficeId, List<TbStationBO> stationList,
			Long especialExemptId) {
		try {
			
			emObj = new ObjectEM(em);
			
			TbDeviceCustomization previousCus = em.find(
					TbDeviceCustomization.class, deviceCustomizationId);
			
			TbVehicle vehicle = em.find(TbVehicle.class, vehicleId); 
			
			String logMessage = "";
		//	String msgProcess = "";
			
			if (vehicleId.longValue() != previousCus.getTbVehicle()
					.getVehicleId()) { // The Vehicle Plate Changed.
				
				logMessage += "El Vehículo asociado a la personalización ha cambiado, placa anterior: " 
					+ previousCus.getTbVehicle().getVehiclePlate() + " a: " + vehicle.getVehiclePlate() + ". ";
				//msgProcess = logMessage;
				
				previousCus.setTbVehicle(vehicle);
			}
			
			TbCategory cat =  em.find(TbCategory.class, idCategory);
			
			// If category changes
			if(!previousCus.getCategoryConcession().equals(cat.getCategoryCode()) &&
					!previousCus.getCategoryInvias().equals(cat.getCategoryInviasCode())) {
				
				logMessage += "Las Categorías han cambiado de Catagoría Invias: " +  previousCus.getCategoryInvias() + " a: " + cat.getCategoryInviasCode()+
				" y Categoría Concesión: " +  previousCus.getCategoryConcession() + " a: " + cat.getCategoryCode() + ". ";
				
				previousCus.setCategoryConcession(cat.getCategoryCode());
				previousCus.setCategoryInvias(cat.getCategoryInviasCode());
			}
			
			//if filing number change.
			if (!filingNumber.equals(previousCus.getFilingNumber())) {
				logMessage += "El Número de Radicación Ha Cambiado de: " + previousCus.getFilingNumber() + " a: " + filingNumber + ". ";
				previousCus.setFilingNumber(filingNumber);
			}
			
			//if filing date change.
			if (!filingDate.equals(previousCus.getFilingDate())) {
				logMessage += "La Fecha de Radicación Ha Cambiado de: " + previousCus.getFilingDate() + " a: " + filingDate + ". ";
				previousCus.setFilingDate(new Timestamp(filingDate.getTime()));
			}
			
			//if officialDocumentNumber change.
			if (!officialDocumentNumber.equals(previousCus.getOfficialDocumentNumber())) {
				logMessage += "El Número de Oficio Ha Cambiado de: " + previousCus.getOfficialDocumentNumber() + " a: " + officialDocumentNumber + ". ";
				previousCus.setOfficialDocumentNumber(officialDocumentNumber);
			}
			
			//if official Document date  change.
			if (!officialDocuementDate.equals(previousCus.getOfficialDocuemntDate())) {
				logMessage += "La Fecha de Oficio Ha Cambiado de: " + previousCus.getOfficialDocuemntDate() + " a: " + officialDocuementDate + ". ";
				previousCus.setOfficialDocuemntDate(new Timestamp(officialDocuementDate.getTime()));
			}
			
			//if Cor Received date change.
			if (!corReceivedDate.equals(previousCus.getCorReceivedDate())) {
				logMessage += "La Fecha de Recibido COR Ha Cambiado de: " + previousCus.getCorReceivedDate() + " a: " + corReceivedDate + ". ";
				previousCus.setCorReceivedDate(new Timestamp(corReceivedDate.getTime()));
			}
			
			//if typed option change.
			if (!typedOption.equals(previousCus.getTyped())) {
				logMessage += "La Opción de Digitado Ha Cambiado de: " + previousCus.getTyped() + " a: " + typedOption + ". ";
				previousCus.setTyped(typedOption);
			}
			
			//if surplus value  change.
			if(surplusValue != null) {
				if (previousCus.getSurplusValue() == null || surplusValue.longValue() != previousCus.getSurplusValue().longValue()) {
					logMessage += "El Valor Excedente Ha Cambiado de: " + previousCus.getSurplusValue() + " a: " + surplusValue + ". ";
					previousCus.setSurplusValue(surplusValue);
				}
			}
			
			//if pay option change.
			if (!payOption.equals(previousCus.getPay())) {
				logMessage += "La Opción de Pago Ha Cambiado de: " + previousCus.getPay()+ " a: " + payOption + ". ";
				if(!payOption.equals("EXCEDENTE")) {
					previousCus.setSurplusValue(null);
				}
				previousCus.setPay(payOption);
			}
			
			//if observation change.
			if(observation != null && observation.trim().length()>0){
				if (!observation.equals(previousCus.getObservation())) {
					logMessage += "La Observación Ha Cambiado. Antes: " + previousCus.getObservation() +". ";
					previousCus.setObservation(observation);
				}
			}
			
			//if client account change.
			if (idClientAccount.longValue() != previousCus.getTbAccount().getAccountId()) {
				logMessage += "La Cuenta de cliente Ha Cambiado. No. Anterior: " + previousCus.getTbAccount().getAccountId() +". ";
				previousCus.setTbAccount(em.find(TbAccount.class, idClientAccount));
			}
			
			// station manage.
			
			// delete stations
			Query qe = em.createQuery("SELECT ss FROM TbSpecialStation ss WHERE ss.tbDeviceCustomization.deviceCustomizationId =?1");
			qe.setParameter(1, previousCus.getDeviceCustomizationId());
			
			for(Object ob: qe.getResultList()){
				em.remove((TbSpecialStation)ob);
				em.flush();
			}
			
			//New special/carreteable or exempt type.
			TbSpecialExemptType type = em.find(TbSpecialExemptType.class, especialExemptId);
			
			if(type.getTbDeviceType().getDeviceTypeId().longValue() == DeviceType.CARRETEABLE.getId().longValue() ||
					type.getTbDeviceType().getDeviceTypeId().longValue() == DeviceType.SPECIAL.getId().longValue()) {
				//if special or carreteable creation stations
				for(TbStationBO s : stationList){
					TbSpecialStation ss = new TbSpecialStation();
					ss.setTbStationBO(em.find(TbStationBO.class, s.getStationBOId()));
					ss.setTbDeviceCustomization(previousCus);
					
					if (emObj.create(ss)) {
						log.insertLog(
								"Correción de Malas Digitaciones-Personalización | Se ha creado la relación estación-especial ID: " + ss.getSpecialStationId() + ".",
								LogReference.SPECIAL, LogAction.CREATE, ip, modUser);
					} else {
						log.insertLog(
								"Correción de Malas Digitaciones-Personalización | No se ha podido crear la relación estación-especial ID personalización:  " 
								+ previousCus.getDeviceCustomizationId() +  " con la estación Código: " + s.getStationBOCode(),
								LogReference.SPECIAL, LogAction.CREATEFAIL, ip, modUser);
					}
				}//end for
			}
			
			// if device type change
			if (previousCus.getTbSpecialExemptType().getTbDeviceType()
					.getDeviceTypeId().longValue() != type.getTbDeviceType().getDeviceTypeId().longValue()) {
				
				// If previous type was an exempt setting field related to exempt to null
				if (previousCus.getTbSpecialExemptType().getTbDeviceType()
						.getDeviceTypeId().longValue() == DeviceType.EXEMPT.getId().longValue()) {
					previousCus.setTbSpecialExemptOffice(null);
					previousCus.setTbDepartment(null);
				}
				
				// if previous type was special or carreteable 
				if (previousCus.getTbSpecialExemptType().getTbDeviceType().getDeviceTypeId().longValue() == DeviceType.SPECIAL.getId().longValue()
						|| previousCus.getTbSpecialExemptType().getTbDeviceType().getDeviceTypeId().longValue() == DeviceType.CARRETEABLE.getId().longValue()) {
					
					// if new = Exempt 
					if(type.getTbDeviceType().getDeviceTypeId().longValue()== DeviceType.EXEMPT.getId()) {
						previousCus.setTbDepartment(em.find(TbDepartment.class, departmentId));
						previousCus.setTbSpecialExemptOffice(em.find(TbSpecialExemptOffice.class, exemptOfficeId));
					} 
					
				}
				
				// saving the log.
				logMessage += "El Tipo de Dispositivo Ha Cambiado. Antes: "
					+ previousCus.getTbSpecialExemptType().getTbDeviceType().getDeviceTypeName() + ". "
					+ " Tipo: " + previousCus.getTbSpecialExemptType().getSpecialExemptTypeName() + ".";
				// doing the update.
				previousCus.setTbSpecialExemptType(type);
			} else {
				// if device type doesn't change... ask if this is exempt
				if (previousCus.getTbSpecialExemptType().getTbDeviceType()
						.getDeviceTypeId().longValue() == DeviceType.EXEMPT.getId().longValue()) {
					
					//if exempt type changes.
					if (previousCus.getTbSpecialExemptType().getSpecialExemptTypeId().longValue() != type.getSpecialExemptTypeId().longValue()) {
						logMessage += "El Tipo de Exento Ha Cambiado. Antes: " + previousCus.getTbSpecialExemptType().getSpecialExemptTypeName() +". ";
						previousCus.setTbSpecialExemptType(type);
					}
					
					//if exempt dependency changes.
					if (previousCus.getTbSpecialExemptOffice()
							.getSpecialExemptOfficeId().longValue() != exemptOfficeId.longValue()) {
						logMessage += "La dependencia de Exento Ha Cambiado. Antes: " + previousCus.getTbSpecialExemptOffice().getOfficeName() +". ";
						previousCus.setTbSpecialExemptOffice(em.find(TbSpecialExemptOffice.class, exemptOfficeId));
					}
				} else {
					//if special/carreteable type changes.
					if (previousCus.getTbSpecialExemptType().getSpecialExemptTypeId().longValue() != type.getSpecialExemptTypeId().longValue()) {
						logMessage += "El Tipo de Especial/Carreteable Ha Cambiado. Antes: " + previousCus.getTbSpecialExemptType().getSpecialExemptTypeName() +". ";
						previousCus.setTbSpecialExemptType(type);
					}
				}
			}
			
			previousCus.setTbDeviceCustomizationState(em.find(
					TbDeviceCustomizationState.class,
					CustomizationState.NEW.getId()));
			if(emObj.update(previousCus)) {
				if(logMessage!= null && logMessage.trim().length()>0){
					log.insertLog(logMessage, LogReference.CUSTOMIZATION, LogAction.UPDATE, ip, modUser);
				}
				
//				// Searching the process to indicate.
//				TbProcessTrack pt = process.searchProcess(ProcessTrackType.CUSTOMIZATION, previousCus.getDeviceCustomizationId());
//				
//				if(pt!=null){
//					process.createProcessDetail(pt.getProcessTrackId(), ProcessTrackDetailType.CUSTOMIZATION_CONFIRMATION, msgProcess + 
//							"Se ha Corregido el proceso de personalización. La placa asociada es: " + previousCus.getTbVehicle().getVehiclePlate() + ".",
//							modUser, ip, 1, "No se ha podido indicar en el proceso ID: " + pt.getProcessTrackId() + 
//							" que se ha Corregido el proceso de personalización..");
//				}
				
				return true;
			}
			
		} catch (Exception e) {
			System.out.println(" [ Error en MasterEJB.saveFixMaster ] ");
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<TbDeviceCustomization> getDeviceCustomization(
			Long deviceCustomizationStateId, String plate, String chasis,
			Long idType, Long operationTypeId) {
		List<TbDeviceCustomization> list = new ArrayList<TbDeviceCustomization>();
		try {
			String query = "SELECT dc FROM TbDeviceCustomization dc " +
								   "WHERE " +
								   		" dc.tbDeviceCustomizationState.deviceCustomizationStateId =?1 " +
								   		" AND dc.tbOperationType.operationTypeId = ?2 ";
			if (idType.longValue() != -1) {
				System.out.println("enteres");
				query += " AND dc.tbSpecialExemptType.tbDeviceType.deviceTypeId =?3 ";
			}
			
			if(plate != null && plate.trim().length() >0) {
				query += " AND dc.tbVehicle.vehiclePlate = ?4 ";
			}
			
			if(chasis != null && chasis.trim().length() >0) {
				query += " AND dc.tbVehicle.vehicleChassis = ?5 ";
			}
			
			//System.out.println(query);
			
			Query q = em.createQuery(query);
			q.setParameter(1, deviceCustomizationStateId);
			q.setParameter(2, operationTypeId);
			
			if (idType.longValue() != -1) {
				q.setParameter(3, idType);
			}
			
			if(plate != null && plate.trim().length() >0) {
				q.setParameter(4, plate);
			}
			
			if(chasis != null && chasis.trim().length() >0) {
				q.setParameter(5, chasis);
			}
			
			for (Object obj : q.getResultList()) {
				list.add((TbDeviceCustomization) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en MasterEJB.getDeviceCustomization ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Master#getDeviceCustomizationsToInclude()
	 */
	@Override
	public List<InclusionDetail> getDeviceCustomizationsToInclude() {
		List<InclusionDetail> list = new ArrayList<InclusionDetail>();
		try {
			Query q = em.createQuery("SELECT dc FROM " +
					" TbDeviceCustomization dc " +
					" WHERE dc.tbDeviceCustomizationState.deviceCustomizationStateId IN (1,3) " +
					" AND dc.tbSpecialExemptType.tbDeviceType.deviceTypeId NOT IN (4) " +
					" AND dc.deviceCustomizationId NOT IN ( SELECT ind.tbDeviceCustomization.deviceCustomizationId FROM TbInclusionDetail ind)");
			
			for (Object obj : q.getResultList()) {
				TbDeviceCustomization cus = (TbDeviceCustomization) obj;
				InclusionDetail id = new InclusionDetail();
				id.setCustomization(em.find(TbDeviceCustomization.class, cus.getDeviceCustomizationId()));
				
				String dep = "";
				String sta = "";
				
				Query q1 = em.createQuery("SELECT ss FROM TbSpecialStation ss " +
						" WHERE ss.tbDeviceCustomization.deviceCustomizationId = ?1");
				q1.setParameter(1, cus.getDeviceCustomizationId());
					
				for(Object ob: q1.getResultList()){
					TbSpecialStation ss = (TbSpecialStation) ob;
					dep = ss.getTbStationBO().getTbDepartment().getDepartmentName();
					sta += " " + ss.getTbStationBO().getStationBOName() + " -";
				}
				sta = sta.substring(0, sta.length() - 1);
					
				id.setStation(sta);
				id.setReg(dep);
				
				list.add(id);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en MasterEJB.getDeviceCustomizationsToInclude ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Master#removeDetailInclusion(java.lang.Long, java.util.List,
	 * java.lang.String, java.lang.Long)
	 */
	@Override
	public boolean removeDetailInclusion(Long inclusionId,
			List<InclusionDetail> details, String ip, Long creationUser) {
		try {
			TbInclusion in = em.find(TbInclusion.class, inclusionId);
			in.setInclusionUpdateDate(new Timestamp(System.currentTimeMillis()));
			in.setUpdateUser(em.find(TbSystemUser.class, creationUser));
			em.merge(in);
			em.flush();
			
			for(InclusionDetail i: details) {
				if(!i.isSelected()){
					
					Query q = em.createQuery("SELECT id FROM TbInclusionDetail id WHERE id.tbInclusion.inclusionId = ?1 " +
							" AND id.tbDeviceCustomization.deviceCustomizationId = ?2 ");
					q.setParameter(1, inclusionId);
					q.setParameter(2, i.getCustomization().getDeviceCustomizationId());
					
					TbInclusionDetail id = (TbInclusionDetail) q
							.getSingleResult();
					
					emObj = new ObjectEM(em);
					if (emObj.delete(id)) { 
						log.insertLog(
								"Eliminar Detalle en Inclusión | Inclusión ID: " + inclusionId + ", Registro de Personalización ID: "+ i.getCustomization()
												.getDeviceCustomizationId() + ".", LogReference.INCLUSION,
								LogAction.UPDATE, ip, creationUser);
					} else {
						log.insertLog("Eliminar Detalle en Inclusión | Inclusión ID: " + inclusionId + ", Registro de Personalización ID: " +
								i.getCustomization().getDeviceCustomizationId() + ".", LogReference.INCLUSION, LogAction.UPDATEFAIL, ip, creationUser);
						return false;
					}
				}
			}
			return true;
		} catch (Exception e) {
			System.out.println(" [ Error en MasterEJB.removeDetailInclusion ] ");
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Master#addDetailsToInclusion(java.lang.Long, java.util.List,
	 * java.lang.String, java.lang.Long)
	 */
	@Override
	public boolean addDetailsToInclusion(Long inclusionId,
			List<InclusionDetail> detailsToAdd, String ip, Long creationUser) {
		try {
			TbInclusion i = em.find(TbInclusion.class, inclusionId);
			i.setInclusionUpdateDate(new Timestamp(System.currentTimeMillis()));
			i.setUpdateUser(em.find(TbSystemUser.class, creationUser));
			em.merge(i);
			em.flush();
			
			for (InclusionDetail d : detailsToAdd) {
				if(d.isSelected()){
					TbInclusionDetail det = new TbInclusionDetail();
					det.setTbInclusion(i);
					det.setTbDeviceCustomization(em.find(
							TbDeviceCustomization.class, d.getCustomization()
									.getDeviceCustomizationId()));
					if(emObj.create(det)) {
						log.insertLog("Añadir Detalle Inclusión | Se ha guardado el detalle de inclusión ID: " + det.getInclusionDetailId() +  ".",
								LogReference.INCLUSION, LogAction.UPDATE, ip, creationUser);
					} else {
						log.insertLog("Añadir Detalle Inclusión | No se pudo guadar el detalle de la inclusión ID: " + i.getInclusionId()
								+ " asociado a la personalización : " + d.getCustomization().getDeviceCustomizationId() +  ".",
								LogReference.INCLUSION, LogAction.UPDATEFAIL, ip, creationUser);
						return false;
					} // end create detail
				}
			}// end for details
			log.insertLog("Añadir Detalle Inclusión | Se ha modificado la inclusión ID: " + i.getInclusionNumber() +  ".",
					LogReference.INCLUSION, LogAction.UPDATE, ip, creationUser);
			return true;
		} catch (Exception e) {
			System.out.println(" [ Error en MasterEJB.addDetailsToInclusion ] ");
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Master#sendInclusion(java.lang.Long, java.lang.String,
	 * java.lang.Long)
	 */
	@Override
	public boolean sendInclusion(Long inclusionId, String ip, Long creationUser) {
		try {
			TbInclusion i = em.find(TbInclusion.class, inclusionId);
			i.setTbInclusionState(em.find(TbInclusionState.class, InclusionState.SENT.getId()));
			i.setInclusionUpdateDate(new Timestamp(System.currentTimeMillis()));
			i.setUpdateUser(em.find(TbSystemUser.class, creationUser));
			emObj = new ObjectEM(em);
			
			if(emObj.update(i)){
				//save log 
				log.insertLog("Enviar Inclusión | Se ha modificado la inclusión ID: " + i.getInclusionNumber() +  ".",
						LogReference.INCLUSION, LogAction.UPDATE, ip, creationUser);
				
				Query q = em.createQuery("SELECT dt FROM TbInclusionDetail dt WHERE dt.tbInclusion.inclusionId = ?1 ");
				q.setParameter(1, i.getInclusionId());
				
				for(Object o : q.getResultList()){
					TbInclusionDetail id = (TbInclusionDetail) o;
					
					// Searching the stations where the plate is special
					Query qe = em.createQuery("SELECT ss FROM TbSpecialStation ss WHERE ss.tbDeviceCustomization.deviceCustomizationId =?1");
					qe.setParameter(1, id.getTbDeviceCustomization()	.getDeviceCustomizationId());
					
					for ( Object obj: qe.getResultList()) {
						TbSpecialStation ss = (TbSpecialStation) obj;
						TbStationBO sbo = em.find(TbStationBO.class, ss
								.getTbStationBO().getStationBOId());
						// searching the last facial
						TbDeviceFacial df = deviceEJB.getDeviceFacial(DeviceType.SPECIAL, sbo.getTbCompany()
										.getCompanyId(), ip, creationUser);
						
						String number = String.valueOf(df.getFacialConsecutive());
						id.setDeviceFacialId(df.getDeviceFacialPrefix()
								+ StringUtils.leftPad(number, df.getFacialLenght(), "0"));
						
						df.setFacialConsecutive(df.getFacialConsecutive() + 1);
						em.merge(df);
						em.merge(id);
						em.flush();
						break;
					} //end for stations
				}// end list of inclusion				
				
				// Searching the inclusion process to indicate that has been sent.
				TbProcessTrack pt = process.searchProcess(ProcessTrackType.INCLUSION, i.getInclusionId());
				if (pt != null) {
					Long detailId = process.createProcessDetail(pt.getProcessTrackId(), ProcessTrackDetailType.INCLUSION_SENT, 
							"Se Ha enviado la Inclusión al área Interesada.", creationUser, ip, 1, "No se Ha podido Indicar en el proceso " +
									"que se ha enviado la Inclusión ID: " + i.getInclusionId(),null,null,null,null);
					
					TbProcessTrackDetailType dt = em.find(TbProcessTrackDetailType.class,
							ProcessTrackDetailType.INCLUSION_SENT.getId());
					
					// Task Creation.
					task.createTask(detailId, 0, new Timestamp(System.currentTimeMillis()), 
							TimeUtil.calculateAds(dt.getAdsTime()), dt.getDetailTypePriority(), 
							"La Inclusión No. " + i.getInclusionNumber() + " ha sido enviada.", 
							dt.getTbTaskType().getTaskTypeId(), creationUser, ip, null);
				}
				return true;
			} else {
				log.insertLog("Enviar Inclusión | No se ha modificado la inclusión ID: " + i.getInclusionNumber() +  ".",
						LogReference.INCLUSION, LogAction.UPDATE, ip, creationUser);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en MasterEJB.sendInclusion ] ");
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ejb.Master#getSpecialDeviceCustomization(constant.CustomizationState,
	 * java.lang.Long)
	 */
	@Override
	public List<Personalization> getSpecialDeviceCustomization(
			CustomizationState customizationState, Long operationTypeId) {
		List<Personalization> list = new ArrayList<Personalization>();
		try {
			String query = "SELECT dc FROM TbDeviceCustomization dc " +
				" WHERE dc.tbDeviceCustomizationState.deviceCustomizationStateId =?1 " +
				"	AND dc.tbSpecialExemptType.tbDeviceType.deviceTypeId != ?2 ";
			
			if(operationTypeId.longValue() != -1L){
				query += " AND dc.tbOperationType.operationTypeId = ?3 ";
			}
			
			Query q = em.createQuery(query);
			q.setParameter(1, customizationState.getId());
			q.setParameter(2, DeviceType.EXEMPT.getId());
			
			if(operationTypeId.longValue() != -1L){
				q.setParameter(3, operationTypeId);
			}
			for (Object obj : q.getResultList()) {
				Personalization per = new Personalization();
				TbDeviceCustomization c = (TbDeviceCustomization) obj;
				per.setCus(c);
				
				Query qe = em.createQuery("SELECT ss FROM TbSpecialStation ss WHERE ss.tbDeviceCustomization.deviceCustomizationId =?1");
				qe.setParameter(1, c.getDeviceCustomizationId());
				
				List<TbStationBO> sl = new  ArrayList<TbStationBO>();
				
				for (Object o : qe.getResultList()) {
					TbSpecialStation ss = (TbSpecialStation) o;
					TbStationBO sbo = em.find(TbStationBO.class, ss
							.getTbStationBO().getStationBOId());
					sl.add(sbo);
				}
				per.setStations(sl);
				list.add(per);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en MasterEJB.getDeviceCustomization ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Master#savePersonalization(java.lang.Long, java.lang.String,
	 * java.lang.Long)
	 */
	@Override
	public boolean savePersonalization(Long customizationId, String ip,
			Long creationUser) {
		try {
			TbDeviceCustomization cus = em.find(TbDeviceCustomization.class,
					customizationId);
			cus.setTbDeviceCustomizationState(em.find(
					TbDeviceCustomizationState.class,
					CustomizationState.CUSTOMIZED.getId()));
			
			emObj = new ObjectEM(em);
			if (emObj.update(cus)) {
				log.insertLog("Personalización | Se ha personalizado ID: "+ cus.getDeviceCustomizationId(),
						LogReference.CUSTOMIZATION, LogAction.UPDATE, ip,creationUser);
				
				// saving into the process
				TbProcessTrack pp = process.searchProcess(ProcessTrackType.CUSTOMIZATION, cus.getDeviceCustomizationId());
				process.createProcessDetail(pp.getProcessTrackId(), ProcessTrackDetailType.CUSTOMIZATION,
						"Se ha hecho la personalización.", creationUser, ip, 1, 
						"No se ha podido indicar en el proceso que se ha hecho la personalización.",null,null,null,null);
				
				// updating device state
				TbDevice dev = em.find(TbDevice.class, cus.getTbDevice().getDeviceId());
				dev.setTbDeviceState(em.find(TbDeviceState.class, DeviceState.CUSTOMIZED.getId()));
				em.merge(dev);
				em.flush();
				
				//searching the process to indicate
				TbProcessTrack pd = process.searchProcess(ProcessTrackType.DEVICE, dev.getDeviceId());
				process.createProcessDetail(pd.getProcessTrackId(), ProcessTrackDetailType.CUSTOMIZATION, 
						"El Dispositivo ha sido personalizado.", creationUser, ip, 1,
						"No se ha podido indicar en el proceso que se ha personalizado el dispositivo.",null,null,null,null);
				
				return true;
			} else {
				log.insertLog("Personalización | No se ha podido personalizar ID: "+ cus.getDeviceCustomizationId(),
						LogReference.CUSTOMIZATION, LogAction.UPDATEFAIL, ip,creationUser);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en MasterEJB.getDeviceCustomization ] ");
			e.printStackTrace();
		}
		return false;
	}
}