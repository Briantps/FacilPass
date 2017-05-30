package ejb;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import util.warehouse.WarehouseTo;

import jpa.ReDeviceWarehouse;
import jpa.TbDevice;
import jpa.TbDeviceCustomization;
import jpa.TbDeviceState;
import jpa.TbDeviceType;
import jpa.TbInclusion;
import jpa.TbInclusionDetail;
import jpa.TbInclusionState;
import jpa.TbProcessTrack;
import jpa.TbSystemParameter;
import jpa.TbSystemUser;
import jpa.TbWarehouse;
import jpa.TbWarehouseDependency;
import jpa.TbWarehouseOperationType;
import jpa.TbWarehouseState;

import constant.DeviceState;
import constant.DeviceType;
import constant.EmailSubject;
import constant.EmailType;
import constant.InclusionState;
import constant.LogAction;
import constant.LogReference;
import constant.ProcessTrackDetailType;
import constant.ProcessTrackType;
import constant.TagManufacturer;
import constant.WarehouseDependency;
import constant.WarehouseOperationType;
import constant.WarehouseState;

import crud.ObjectEM;

@Stateless(mappedName = "ejb/Warehouse")
public class WarehouseEJB implements Warehouse {
	
	@PersistenceContext(unitName = "bo")
	EntityManager em;
	
	@EJB(mappedName = "ejb/Log")
	private Log log;
	
	@EJB(mappedName="ejb/Process")
	private Process process;
	
	@EJB(mappedName ="ejb/sendMail")
	private SendMail sendMail;
	
	@EJB(mappedName="ejb/Device")
	private ejb.Device deviceEJB;
	
	private ObjectEM emObj;

	public WarehouseEJB() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Warehouse#saveEntry(java.lang.String, java.lang.Long,
	 * java.lang.Long, java.lang.String, boolean)
	 */
	@Override
	public TbWarehouse saveEntry(String ip, Long creationUser, Long cardQuantity, 
			String entryNumber, boolean isCard) {
		try {
			TbWarehouse war = new TbWarehouse();
			war.setCreationDate(new Timestamp(System.currentTimeMillis()));
			
			war.setDestinationId(em.find(TbWarehouseDependency.class, WarehouseDependency.WAREHOUSE.getId()));
			
			war.setDeviceQuantity(cardQuantity);
			war.setOrderNumber(entryNumber);
			war.setWarehouseIsCard(isCard);
			
			war.setOriginId(em.find(TbWarehouseDependency.class, WarehouseDependency.SUPPLIER.getId()));
			
			war.setTbWarehouseOperationType(em.find(TbWarehouseOperationType.class,
					WarehouseOperationType.ENTRY.getId()));
			
			war.setTbWarehouseState(em.find(TbWarehouseState.class, WarehouseState.PRELIMINARY.getId())); 
			
			emObj = new ObjectEM(em);
			if(emObj.create(war)) {
				log.insertLog("Orden Entada Almacén | Creado Regitro ID: "+ war.getWarehouseId()  + ".",
						LogReference.WAREHOUSE, LogAction.CREATE, ip, creationUser);
				return war;
			} else {
				log.insertLog("Orden Entada Almacén | No se puedo crear Orden No: "+ entryNumber  + ".",
						LogReference.WAREHOUSE, LogAction.CREATEFAIL, ip, creationUser);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en WarehouseEJB.saveEntry ] ");
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Warehouse#saveEntryRelation(java.lang.String, java.lang.Long,
	 * java.lang.Long, java.lang.Long)
	 */
	@Override
	public boolean saveEntryRelation(String ip, Long creationUser,
			Long warehouseId, Long deviceId, boolean compareToRechargeFile) {
		try {
			ReDeviceWarehouse dw = new ReDeviceWarehouse();
			dw.setTbDevice(em.find(TbDevice.class, deviceId));
			dw.setComparedToRechargeFile(compareToRechargeFile);
			dw.setDeviceWarehouseDate(new Timestamp(System.currentTimeMillis()));
			
			TbWarehouse w = em.find(TbWarehouse.class, warehouseId);
			dw.setTbWarehouse(w);
			
			emObj = new ObjectEM(em);
			if(emObj.create(dw)) {
				log.insertLog("Orden Entada Almacén - Relación Dispositivo Orden | Creado Regitro ID: "+ dw.getDeviceWarehouseId() + ".",
						LogReference.WAREHOUSE, LogAction.CREATE, ip, creationUser);
				
				Query q = em.createQuery("SELECT dw FROM ReDeviceWarehouse dw WHERE dw.tbWarehouse.warehouseId = ?1");
				q.setParameter(1, warehouseId);
				
				if (q.getResultList().size() > 0) {
					if (q.getResultList().size() == w.getDeviceQuantity()
							.intValue()) {
						
						// The order is detailed.
						w.setTbWarehouseState(em.find(TbWarehouseState.class,
								WarehouseState.DETAILED.getId())); 
						em.merge(w);
						em.flush();
						
						q = em.createNativeQuery("SELECT " + 
																	 " count(compared_to_precharge_file) " +
																  "FROM " +
																	 " re_device_warehouse " + 
																  "WHERE "  +
																	 " compared_to_precharge_file = 0 " +
																	 " AND warehouse_id = ?1"); 
						q.setParameter(1, warehouseId);
						System.out.println(warehouseId);
						Long value =	Long.valueOf(q.getSingleResult().toString());
						System.out.println(value + "v");
						if (value > 0) {
							// send email notification
							sendMail.sendMail(EmailType.ALERT_DEVICE, null, EmailSubject.ALERT_ENTRY_ORDER, 
									w.getOrderNumber() + " con " + value + " dispositivos sin ser verificados contra el archivo de precarga." );
						}
					}
				}
				
				// Saving into process.
				TbProcessTrack pt = process.searchProcess(ProcessTrackType.DEVICE, deviceId);
				
				if (pt != null) {
					// Creating process detail 
					process.createProcessDetail(pt.getProcessTrackId(), ProcessTrackDetailType.DEVICE_ENTRY_ORDER,
							"Dispositivo Ingresado. Orden de Entrada No. " + w.getOrderNumber() + ".",
							creationUser, ip, 1,"No se ha podido Indicar en el proceso que el dispositivo ID: "
							+ deviceId + " ha sido ingresado en la orden de entrada ID: "+ warehouseId + " . ID Proceso: " 
							+ pt.getProcessTrackId() + ".",null,null,null,null);
				}
				return true;
			} else {
				log.insertLog("Orden Entada Almacén - Relación Dispositivo Orden | No se pudo crear Relación Device ID y Warehouse ID: "
						+ deviceId  + " - " + warehouseId + ".",
						LogReference.WAREHOUSE, LogAction.CREATEFAIL, ip, creationUser);
			}
			
		} catch (Exception e) {
			System.out.println(" [ Error en WarehouseEJB.saveEntryRelation ] ");
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see ejb.Warehouse#getPendingEntryOrders()
	 */
	@Override
	public List<WarehouseTo> getPendingEntryOrders() {
		List<WarehouseTo> list = new ArrayList<WarehouseTo>();
		try {
			Query q = em
					.createQuery("SELECT wh FROM TbWarehouse wh WHERE wh.tbWarehouseState.warehouseStateId = ?1");
			q.setParameter(1, WarehouseState.PRELIMINARY.getId());
			
			for(Object obj : q.getResultList()) {
				TbWarehouse w = (TbWarehouse) obj;

				Query qd = em
						.createQuery("SELECT dw.tbDevice FROM ReDeviceWarehouse dw WHERE dw.tbWarehouse.warehouseId = ?1");
				qd.setParameter(1, w.getWarehouseId());
				
				WarehouseTo wt = new WarehouseTo();
				
				List<TbDevice>  listd = new ArrayList<TbDevice>();
				for(Object ob:qd.getResultList() ){
					listd.add((TbDevice)ob);
				}
				
				wt.setWarehouse(w);
				wt.setLeft(w.getDeviceQuantity() - qd.getResultList().size());
				wt.setDeviceList(listd);
			
				list.add(wt);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en WarehouseEJB.getPendingEntryOrders ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Warehouse#searchWarehouseNumber(java.lang.String,
	 * constant.WarehouseOperationType)
	 */
	@Override
	public boolean searchWarehouseNumber(String warehouseNumber,
			WarehouseOperationType warehouseOperationType) {
		try {
			Query q = em.createQuery("SELECT wa FROM TbWarehouse wa WHERE wa.orderNumber= ?1 " +
					" AND wa.tbWarehouseOperationType.warehouseOperationTypeId =?2" +
					" AND wa.tbWarehouseState.warehouseStateId NOT IN (?3) ");
			q.setParameter(1, warehouseNumber);
			q.setParameter(2, warehouseOperationType.getId());
			q.setParameter(3, WarehouseState.CANCELED.getId());
			
			q.getSingleResult();
		} catch (NoResultException ne) {
			return true;
		} catch (Exception e) {
			System.out.println(" [ Error en WarehouseEJB.searchWarehouseNumber ] ");
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see ejb.Warehouse#searchDeviceToEntry(java.lang.String)
	 */
	@Override
	public Long searchDeviceToEntry(String deviceCode) {
		try {
			Query q = em.createQuery("SELECT td FROM TbDevice td WHERE td.deviceCode = ?1");
			q.setParameter(1, deviceCode);
			
			TbDevice dev = (TbDevice) q.getSingleResult();
			if(dev != null ){
				if (dev.getTbDeviceState().getDeviceStateId().longValue() == DeviceState.PRECHARGE
						.getId().longValue()) {
					return 1L;
				} else if (dev.getTbDeviceState().getDeviceStateId().longValue() == DeviceState.SUPPLIER
						.getId().longValue()) {
					return 2L;
				}else {
					return 3L;
				}
			}
		} catch (NoResultException e){	
			return 0L;
		} catch (NonUniqueResultException e){
			System.out.println("[ Error en WarehouseEJB: Hay más de un resultado para el código de dispositivo ingresado "
							+ deviceCode + " ]");
		} catch (Exception e) {
			System.out.println(" [ Error en WarehouseEJB.searchDeviceToEntry ] ");
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Warehouse#changeDeviceStateEntry(java.lang.Long,
	 * java.lang.String, java.lang.Long)
	 */
	@Override
	public boolean changeDeviceStateEntry(Long idDevice, String ip,
			Long creationUser) {
		try {
			TbDevice d = em.find(TbDevice.class, idDevice);
			d.setTbDeviceState(em.find(TbDeviceState.class, DeviceState.WAREHOUSE.getId()));
			
			emObj = new ObjectEM(em);
			if(emObj.update(d)) {
				
				//saving log
				log.insertLog("Orden Entada Almacén - Ingresar Dispositivo | Actualizado  Registro ID: "+ d.getDeviceId() + ".",
						LogReference.WAREHOUSE, LogAction.UPDATE, ip, creationUser);
				
				//searching device process to update
				TbProcessTrack pt = process.searchProcess(ProcessTrackType.DEVICE, idDevice);
				
				if (pt != null) {
					// Creating process detail 
					process.createProcessDetail(pt.getProcessTrackId(), ProcessTrackDetailType.DEVICE_STATE_CHANGE,
							"Dispositivo ha cambiado de estado: En Almacén.", creationUser, ip, 1,
							"No se ha podido Indicar en el proceso que el dispositivo ID: "
							+ idDevice + " ha cambiado su estado a 'En Almacén'.  ID Proceso: " 
							+ pt.getProcessTrackId() + ".",null,null,null,null);
				}
				return true;
			} else {
				log.insertLog("Orden Entada Almacén - Ingresar Dispositivo | No se pudo Actualizar  Registro ID: "+ d.getDeviceId() + ".",
						LogReference.WAREHOUSE, LogAction.UPDATEFAIL, ip, creationUser);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en WarehouseEJB.changeDeviceStateEntry ] ");
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Warehouse#compareToPrechargeFile()
	 */
	@Override
	public boolean compareToPrechargeFile() {
		try {
			Query q = em
					.createQuery("SELECT sp FROM TbSystemParameter sp WHERE sp.systemParameterName = 'Archivo Precarga' ");
			TbSystemParameter sp = (TbSystemParameter) q.getSingleResult();
			if(sp.getSystemParameterValue().equals("SI")) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			System.out.println(" [ Error en WarehouseEJB.compareToPrechargeFile ] ");
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Warehouse#reportAttempt(java.lang.String, java.lang.String,
	 * java.lang.Long)
	 */
	@Override
	public boolean reportAttempt(String deviceCode, String ip, Long creationUser) {
		try {
			log.insertLog("Alerta de Almacén | Se ha intentado ingresar un dispositivo Id Interno :" + deviceCode + ".",
					LogReference.WAREHOUSE, LogAction.ATTEMPT_ENTRY_DEVICE, ip, creationUser);
			//Sending email
			sendMail.sendMail(EmailType.ALERT_DEVICE, null, EmailSubject.ALERT_DEVICE, deviceCode);
			return true;
			
		} catch (Exception e) {
			System.out.println(" [ Error en WarehouseEJB.reportAttempt ] ");
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see ejb.Warehouse#getPrechargePath()
	 */
	@Override
	public String getPrechargePath() {
		try {
			Query q = em
					.createQuery("SELECT sp FROM TbSystemParameter sp " +
							" WHERE sp.systemParameterName = 'Ruta Guardar Archivo Precarga' ");
			TbSystemParameter sp = (TbSystemParameter) q.getSingleResult();
			return sp.getSystemParameterValue();
		} catch (Exception e) {
			System.out.println(" [ Error en WarehouseEJB.getPrechargePath ] ");
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Warehouse#processPrecharge(java.util.List, java.lang.String,
	 * java.lang.Long, java.lang.Long)
	 */
	@Override
	public String processPrecharge(List<String> listToProcess, String ip,
			Long creationUser, Long idTagType) {
		try {
			int numberOfProcess = listToProcess.size();
			
			String error = "";
			String  exists = "";
			for (String serial : listToProcess) {
				
				String internalId = "";
				//Getting the Internal ID.
				if(TagManufacturer.SIRIT.getId().longValue() == idTagType.longValue()){
					internalId = String.valueOf(Long.parseLong(serial,16));
				} else { // if QFREE or KAPSCH
					System.out.println("SERIAL["+serial+"]---ID["+serial.substring(0,2).replaceAll("0", "")+serial.substring(2,serial.length()-5)+"]");
					internalId = serial.substring(0, 2).replaceAll("0", "")
					+ serial.substring(2, serial.length() - 5);
				}
				
				// first validate id does not exists 
				if (deviceEJB.getDeviceByCode(internalId) == null) {
					// device does not exits, so we create it.
					if(deviceEJB.saveDevice(internalId, serial.replace(" ", ""), null,DeviceType.TAG.getId(), DeviceState.PRECHARGE
									.getId(),null,null, ip, creationUser)) {
						TbDevice dev = deviceEJB.getDeviceByCode(internalId);
						deviceEJB.createRealationtagType(dev.getDeviceId(), idTagType, ip, creationUser);
					} else {
						numberOfProcess -= 1;
						error += serial + "|";
					}
				} else {
					// device exits create log and report it.
					numberOfProcess -= 1;
					exists += serial + "|";
				}
			}
			
			if (exists.length() > 0) {
				exists = ". Dispositivos ya existentes: |" + exists;
			}

			if (error.length() > 0) {
				error = ". Dispositivos que no se puedieron ingresar: |"
						+ error;
			}
			
			return "Se procesaron correctamente " + numberOfProcess + " de " + listToProcess.size() + " registros" + exists  + error + "." ;
		} catch (Exception e) {
			System.out.println(" [ Error en WarehouseEJB.processPrecharge ] ");
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Warehouse#deleteOrder(java.lang.Long, java.lang.String,
	 * java.lang.Long)
	 */
	@Override
	public boolean deleteOrder(Long entryOrderId, String ip, Long creationUser) {
		try {
			TbWarehouse w =  em.find(TbWarehouse.class, entryOrderId);
			
			// changing the state
			w.setTbWarehouseState(em.find(TbWarehouseState.class, WarehouseState.CANCELED.getId()));
			
			emObj = new ObjectEM(em);
			if(emObj.update(w)){
				
				// save log
				log.insertLog("Eliminar Orden de Entrada | Se ha modificado ID " + w.getWarehouseId(),
						LogReference.WAREHOUSE, LogAction.UPDATE, ip, creationUser);
				
				// looking for relations with devices.
				Query q = em.createQuery("SELECT dw FROM ReDeviceWarehouse dw " +
						" WHERE dw.tbWarehouse.warehouseId = ?1");
				q.setParameter(1, w.getWarehouseId());
				
				for (Object obj : q.getResultList()) {
					ReDeviceWarehouse rw = (ReDeviceWarehouse) obj;
					// update device.
					
					TbDevice d = rw.getTbDevice();
					String estado;
					if (rw.isComparedToRechargeFile()) {
						// the comparison with precharge file was made.
						d.setTbDeviceState(em.find(TbDeviceState.class,
								DeviceState.PRECHARGE.getId()));
						estado = "Precarga";
					} else {
						d.setTbDeviceState(em.find(TbDeviceState.class,
								DeviceState.SUPPLIER.getId()));
						estado="Proveedor";
					}
					
					if (emObj.update(d)) {
						// save log
						log.insertLog("Eliminar Orden de Entrada | Se ha modificado ID " + d.getDeviceId(),
								LogReference.DEVICE, LogAction.UPDATE, ip, creationUser);
						
						//searching device process to update
						TbProcessTrack pt = process.searchProcess(ProcessTrackType.DEVICE, d.getDeviceId());
						
						if (pt != null) {
							// Creating process detail 
							process.createProcessDetail(pt.getProcessTrackId(), ProcessTrackDetailType.DEVICE_STATE_CHANGE,
									"Dispositivo ha cambiado de estado: " + estado + ". Se ha desasociado de la Orden de Entrada No. " 
									+ w.getOrderNumber(), creationUser, ip, 1, "No se ha podido Indicar en el proceso que el dispositivo ID: "
									+ d.getDeviceId() + " ha cambiado su estado a  "+ estado  +". ID Proceso: " 
									+ pt.getProcessTrackId() + " Número de orden de entrada: " + w.getOrderNumber() +".",null,null,null,null);
						}
						
					} else {
						log.insertLog("Eliminar Orden de Entrada | Error al modificar ID " + d.getDeviceId(),
								LogReference.DEVICE, LogAction.UPDATEFAIL, ip, creationUser);
					}
				}
				return true;
			} else {
				log.insertLog("Eliminar Orden de Entrada | Error al modificar ID " + w.getWarehouseId(),
						LogReference.WAREHOUSE, LogAction.UPDATEFAIL, ip, creationUser);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en WarehouseEJB.deleteOrder ] ");
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Warehouse#getWarehouseState()
	 */
	@Override
	public List<TbWarehouseState> getWarehouseState() {
		List<TbWarehouseState> list = new ArrayList<TbWarehouseState>();
		try {
			Query q = em.createQuery("SELECT ws FROM TbWarehouseState ws");
			for (Object obj : q.getResultList()) {
				list.add((TbWarehouseState) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en WarehouseEJB.getWarehouseState ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Warehouse#changeParamCompareToPrechargeFile(java.lang.String,
	 * java.lang.String, java.lang.Long)
	 */
	@Override
	public boolean changeParamCompareToPrechargeFile(String value, String ip,
			Long creationUser) {
		try {
			Query q = em
			.createQuery("SELECT sp FROM TbSystemParameter sp WHERE sp.systemParameterName = 'Archivo Precarga' ");
			TbSystemParameter sp = (TbSystemParameter) q.getSingleResult();
			sp.setSystemParameterValue(value);
			
			emObj = new ObjectEM(em);
			if (emObj.update(sp)) {
				log.insertLog("Cambiar Propiedad Achivo Precarga. Valor: " + value,
						LogReference.ADMINPARAMETER, LogAction.UPDATE, ip,
						creationUser);
			} else {
				log.insertLog("Cambiar Propiedad Achivo Precarga. Valor: " + value,
						LogReference.ADMINPARAMETER, LogAction.UPDATEFAIL, ip,
						creationUser);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en WarehouseEJB.changeParamCompareToPrechargeFile ] ");
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Warehouse#saveInitialitazion(java.lang.Long, java.lang.Long,
	 * java.lang.String, java.lang.Long)
	 */
	@Override
	public boolean saveInitialitazion(Long deviceId, Long inclusionDetailId,
			String ip, Long creationUser) {
		try {
			TbDevice device = em.find(TbDevice.class, deviceId);
			
			// searching detail
			TbInclusionDetail detail = em.find(TbInclusionDetail.class, inclusionDetailId);
			
			TbDeviceCustomization cus = em.find(TbDeviceCustomization.class,
					detail.getTbDeviceCustomization().getDeviceCustomizationId());
			cus.setTbDevice(device);
			
			emObj = new ObjectEM(em);
			
			if(emObj.update(cus)) {
				
				//saving log
				log.insertLog("Guardar Inicialización | Se ha indicado el Id del dispositivo. ID personalización " + cus.getDeviceCustomizationId() , 
						LogReference.CUSTOMIZATION, LogAction.UPDATE, ip, creationUser);
				
				// searching de customization process
				TbProcessTrack pc = process.searchProcess(ProcessTrackType.CUSTOMIZATION, cus
								.getDeviceCustomizationId());
				process.createProcessDetail(pc.getProcessTrackId(),ProcessTrackDetailType.CUSTOMIZATION_DEVICE_ASSOCIATION,
						"Se ha asociado al proceso de personalización de la placa el dispositivo con Facial: " + detail.getDeviceFacialId()
						+ " e ID Interno: " + device.getDeviceCode() + ".", 
						creationUser, ip, 1, "No se pudo indicar en el proceso que se asociado la personalización ID: " + cus.getDeviceCustomizationId()+
						" e ID dispositivo: " + device.getDeviceId() + ". Process ID: " + pc.getProcessTrackId() + ".",null,null,null,null);
				
			} else {
				log.insertLog("Guardar Inicialización | Erro al indicar el Id del dispositivo. ID personalización " + cus.getDeviceCustomizationId() , 
						LogReference.CUSTOMIZATION, LogAction.UPDATEFAIL, ip, creationUser);
			}
			
			TbDeviceType dt = em.find(TbDeviceType.class, detail
					.getTbDeviceCustomization().getTbSpecialExemptType()
					.getTbDeviceType().getDeviceTypeId());
			
			//updating device
			device.setDeviceFacialId(detail.getDeviceFacialId());
			device.setTbDeviceState(em.find(TbDeviceState.class, DeviceState.INITIALIZED.getId()));
			device.setTbDeviceType(dt);
			
			emObj = new ObjectEM(em);
			if (emObj.update(device)) {
				
				// saving log
				log.insertLog("Guardar Inicialización | Actualizado Dispositivo ID:" + device.getDeviceId() + ".", 
						LogReference.DEVICE, LogAction.UPDATE, ip, creationUser);
				
				// saving into device process
				
				TbProcessTrack ptd = process.searchProcess(ProcessTrackType.DEVICE, device.getDeviceId());
				process.createProcessDetail(ptd.getProcessTrackId(), ProcessTrackDetailType.DEVICE_INITIALIZATION,
						"El Dispositivo ha sido Inicializado como: " + dt.getDeviceTypeName() + ". Asociado al vehículo de placa: " +
								 cus.getTbVehicle().getVehiclePlate() + ".", 
						creationUser, ip, 1, "No se ha podido indicar en el proceso que el dispositivo ha" +
								" sido inicializado. ID proceso: " + ptd.getProcessTrackId()+ ".",null,null,null,null);
			} else {
				log.insertLog("Guardar Inicialización | Error al actualizar Dispositivo ID:" + device.getDeviceId() + ".", 
						LogReference.DEVICE, LogAction.UPDATEFAIL, ip, creationUser);
			}
						
			// updating inclusion
			TbInclusion i = em.find(TbInclusion.class, detail.getTbInclusion()
					.getInclusionId());
			i.setInclusionUpdateDate(new Timestamp(System.currentTimeMillis()));
			i.setUpdateUser(em.find(TbSystemUser.class, creationUser));
			em.merge(i);
			em.flush();
			
			Query q = em.createQuery("SELECT id FROM TbInclusionDetail id WHERE id.tbInclusion.inclusionId = ?1");
			q.setParameter(1, i.getInclusionId());
			
			boolean ready = true;
			for(Object obj: q.getResultList()){
				TbInclusionDetail  di = (TbInclusionDetail) obj;
				if (di.getTbDeviceCustomization().getTbDevice() == null) {
					ready = false;
				}
			}
			
			if (ready) {
				i.setTbInclusionState(em.find(TbInclusionState.class, InclusionState.DETAILS_INITIALIZED.getId()));
				em.merge(i);
				em.flush();
				TbProcessTrack pi = process.searchProcess(ProcessTrackType.INCLUSION, i.getInclusionId());
				process.createProcessDetail(pi.getProcessTrackId(), ProcessTrackDetailType.INCLUSION_DETAILS_INITIALIZED,
						"Los detalles de la inclusión han sido inicializados.", creationUser, ip, 1, "No se ha podido indicar que los detalles de la inclusión" +
								" han sido inicializados. ID Proceso: " + pi.getProcessTrackId() + ".",null,null,null,null);
			}
			
			return true;
		} catch (Exception e) {
			System.out.println(" [ Error en WarehouseEJB.saveInitialitazion ] ");
			e.printStackTrace();
		}
		return false;
	}
}
