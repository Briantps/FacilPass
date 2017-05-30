/**
 * 
 */
package process.warehouse;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import constant.DeviceState;
import constant.DeviceType;
import constant.TagManufacturer;
import constant.WarehouseOperationType;

import sessionVar.SessionUtil;
import util.device.ServiceCommand;

import jpa.TbDevice;
import jpa.TbPoint;
import jpa.TbTagType;
import jpa.TbWarehouse;

import ejb.Warehouse;
import ejb.crud.Point;
import ejb.crud.TagType;

/**
 * @author tmolina
 *
 */
public class EntryOrderBean implements Serializable {
	private static final long serialVersionUID = -889770722862725059L;
	
	@EJB(mappedName="ejb/Point")
	private Point point;
	
	@EJB(mappedName="ejb/Warehouse")
	private Warehouse warehouse;
	
	@EJB(mappedName="ejb/Device")
	private ejb.Device deviceEJB;
	
	@EJB(mappedName = "ejb/TagType")
	private TagType tagType;
	
	// Attributes ----- //
	
	private boolean showPanelEntry; 
	
	private TbPoint tbPoint;
	
	private List<String> loga;
	
	private List<String> logc;
	
	private String entryNumber;
	
	private Long cardQuantity;
	
	private TbWarehouse warehouseId;
	
	private Long left;
	
	private List<TbDevice> deviceList;
	
	private ServiceCommand command;
	
	private String deviceCode;
	
	private boolean compareToPrechargeFile;
	
	private boolean entryCard;
	
	private boolean entryTag;
	
	private boolean showEntryDetailPanelTag;
	
	private boolean showDetail;
	
	/* Tag Data */
	
	private String tagSerial;
	
	private List<SelectItem> typeList;
	
	private Long tagTypeId;

	/**
	 * Constructor
	 */
	public EntryOrderBean() {	
	}
	
	@PostConstruct
	public String validatePoint() {
		init();
		return null;
	}
	
	// Helpers ---------- //
	
	/**
	 * 
	 */
	public String init() {
		tbPoint = point.getUserPoint(SessionUtil.sessionUser().getUserId(),
				SessionUtil.ip());
		if (tbPoint != null) {
			setShowPanelEntry(true);
			compareToPrechargeFile = warehouse.compareToPrechargeFile();
			loga = new ArrayList<String>();
			logc = new ArrayList<String>();
			deviceList = new ArrayList<TbDevice>();
			setTypeList(null);
			entryCard = true;
			entryTag = false;
			showDetail = false;
			showEntryDetailPanelTag = false;
			setCardQuantity(null);
			setDeviceList(null);
			setEntryNumber(null);
			setLeft(null);
			setWarehouseId(null);
		} else {
			setShowPanelEntry(false);
		}
		return null;
	}
	
	/**
	 * Gets current time, String format
	 * @return
	 */
	private String getCurrentTime() {
		try {
			SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			return "[" + f.format(new Date()) + "] ";
		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * 
	 * @param response
	 */
	private void processRequest(String response) {
		if (response.contains("TIMEOUT")) {
			logc.add(getCurrentTime() +  "No se ha detectado tarjeta en el lector.");
		} else if (response.contains("TRANSACTIONLOCKED")) {
			logc.add(getCurrentTime() +  "No es posible realizar lectura de la tarjeta.");
		} else if (response.contains("ERR")) {
			logc.add(getCurrentTime() +  "No es posible realizar la transacción.");
		} else if (response.contains("SALDOMAX")) {
			logc.add(getCurrentTime() +  "No es posible realizar la recarga porque excede el valor máximo de recarga.");
		} else if (response.contains("NOACCESS")) {
			logc.add(getCurrentTime() +  "No es posible leer la Tarjeta.");
		} else if (response.contains("VENCIDA")) {
			logc.add(getCurrentTime() +  "La tarjeta se encuentra vencida.");
		} else {
			logc.add(getCurrentTime() +  "No es posible realizar la transacción por favor verifique el Lector.");
		}
	}
	
	// Actions ---------- //
	
	/**
	 * 
	 * Clears Communication Log.
	 */
	public String clearLogC(){
		logc.clear();
		return null;
	}
	
	/**
	 * 
	 * Clears Application Log.
	 */
	public String clearLogA(){
		loga.clear();
		return null;
	}
	
	/**
	 * 
	 */
	public String saveEntry() {
		// Validate entry number
		if (warehouse.searchWarehouseNumber(entryNumber,
				WarehouseOperationType.ENTRY)) {
			warehouseId = warehouse.saveEntry(SessionUtil.ip(), SessionUtil
					.sessionUser().getUserId(), cardQuantity,entryNumber, entryCard);
			if(warehouseId != null ) {
				left = cardQuantity;
				setShowDetail(true);
				if(deviceList == null) {
					deviceList = new ArrayList<TbDevice>();
				} else {
					deviceList.clear();
				}
				if(entryCard) {
					setShowEntryDetailPanelTag(false);
				} else {
					setTagSerial(null);
					setShowEntryDetailPanelTag(true);
				}
			} else {
				setShowDetail(false);
				loga.add(getCurrentTime() +"Ha ocurrido Un Error al guardar la Orden de Entrada."); 
			}
		} else {
			setShowDetail(false);
			loga.add(getCurrentTime() +" Ya Existe una orden de entrada, con el número ingresado."); 
		}
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public String saveCard() {
		String response;
		StringTokenizer stringTokenizer = null;
		command = new ServiceCommand(tbPoint.getPointIp(), tbPoint.getPointPort().toString());
		try {
			response = command.sendCommand("1");
			if (response.contains("*")) {
				processRequest(response);
				deviceCode = null;
			} else {
				stringTokenizer = new StringTokenizer(response);
				deviceCode = (stringTokenizer.nextToken(";"));
				logc.add(getCurrentTime() +  " Se ha leído la Tarjeta.");
				
				this.save(deviceCode);
				
			} // end asking for response.
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			command.sendCommand("0");
		}
		return null;
	}	
	
	/**
	 * 
	 */
	public String saveTag(){
		if (this.left != null && this.left.longValue() > 0) {
			
			TbTagType manu = null;
			for (TbTagType t : tagType.getTagType()) {
				if (t.getTagTypeId().longValue() == tagTypeId
						.longValue()) {
					manu = t;
				}
			}
			
			if (tagSerial.length() == manu.getSerialLength()) {
				
				if (!compareToPrechargeFile) {
					if (manu.getTagTypeId().longValue() != TagManufacturer.SIRIT
							.getId().longValue()) {
						if (!tagSerial.replaceAll(" ", "").substring(10, 12)
								.equals(manu.getTagTypeCode())) {
							loga.add(getCurrentTime()
									+ " El código de fabricante no corresponde al seleccionado.");
							this.tagSerial = null;
							return null;
						}
					}
				}
				
				String internalId = "";
				
				if(TagManufacturer.SIRIT.getId().longValue() == manu.getTagTypeId().longValue()){
					internalId = String.valueOf(Long.parseLong(tagSerial,16));
				} else { // if QFREE or KAPSCH
					internalId = tagSerial.substring(0, 2).replaceAll("0", "")
					+ tagSerial.substring(2, tagSerial.length() - 5);
				}

				this.save(internalId);
			}  else {
				loga.add(getCurrentTime() + " El serial debe ser de " + manu.getSerialLength() + 
						" caracteres. Verifique.");
			}
		} else {
			loga.add(getCurrentTime()
							+ " Ya se han ingresado todos los dispositivos de la Orden.");
		}
		this.tagSerial = null;
		return null;
	}
	
	/**
	 * 
	 * @param code
	 */
	private void save (String code) {
		TbDevice de = null;
		Long result = warehouse.searchDeviceToEntry(code);
		
		// Search device.
		if (result != null) {
			if (result == 1L) {
				
				// Exists and device state is "precharge"
				de = deviceEJB.getDeviceByCode(code);
				
				if (warehouse.changeDeviceStateEntry(de.getDeviceId(),
						SessionUtil.ip(), SessionUtil.sessionUser().getUserId())) {
					
					if(warehouse.saveEntryRelation(SessionUtil.ip(), SessionUtil.sessionUser().getUserId(),
							warehouseId.getWarehouseId(), de.getDeviceId(), compareToPrechargeFile)){
						deviceList.add(de);
						left = warehouseId.getDeviceQuantity().longValue() - deviceList.size();
						loga.add(getCurrentTime() +  " Se ha asociado el dispositivo con la Orden de entrada.");
					} else {
						loga.add(getCurrentTime() +  " No se pudo asociar el dispositivo con la Orden de entrada.");
					}
				} else {
					loga.add(getCurrentTime() +  " No se ha podido cambiar el estado del dispositivo.");
				}
				
			} else if( result == 3L) {
				// Exists but already has an entry associated.
				
				de =  deviceEJB.getDeviceByCode(code);
				loga.add(getCurrentTime() +  " Hay un dispositivo registrado con el Mismo ID: "  + de.getDeviceCode() +
						". Estado: " + de.getTbDeviceState().getDeviceStateDescription());
			} else if (result == 0L || result == 2L) {
				if (compareToPrechargeFile) { // Have to do the validation
					// Device does not exists/ or state = supplier // Report Attempt
					warehouse.reportAttempt(code, SessionUtil
							.ip(), SessionUtil.sessionUser().getUserId());
					loga.add(getCurrentTime() +  " Ha Intendado Registrar un dispositivo sin que éste se encuentre en el " +
							"archivo de precarga. Se ha enviado la alarma a los Interesados. Verifique. ");
				}  else {
					
					boolean res;
					
					if(result == 2L) {
						
						de = deviceEJB.getDeviceByCode(code);
						res = warehouse.changeDeviceStateEntry(de.getDeviceId(),
								SessionUtil.ip(), SessionUtil.sessionUser().getUserId());
					} else {
						 //Entry devices without validation
						String facial = null;
						Long devType = null;
						if(entryTag) {
							facial = tagSerial;
							devType=  DeviceType.TAG.getId();
						}
						
						res = deviceEJB.saveDevice(code, facial, null, devType, 
								DeviceState.WAREHOUSE.getId() , null,null,SessionUtil.ip(),
								SessionUtil.sessionUser().getUserId());
						de = deviceEJB.getDeviceByCode(code);
						if (entryTag) {
							deviceEJB.createRealationtagType(de.getDeviceId(),
									tagTypeId, SessionUtil.ip(), SessionUtil.sessionUser().getUserId());
						}
					}
					if (res) {
						loga.add(getCurrentTime() +  " Se ha guardado el dispositivo en el Sistema Exitosamente.");
						// Saving the relation
						if(warehouse.saveEntryRelation(SessionUtil.ip(), SessionUtil.sessionUser().getUserId(),
								warehouseId.getWarehouseId(), de.getDeviceId(), false)){
							deviceList.add(de);
							left = warehouseId.getDeviceQuantity().longValue() - deviceList.size();
							loga.add(getCurrentTime() +  " Se ha asociado el dispositivo con la Orden de entrada.");
						} else {
							loga.add(getCurrentTime() +  " No se pudo asociar el dispositivo con la Orden de entrada.");
						}
					} else {
						loga.add(getCurrentTime() +  " No se ha podido guardar el dispositivo en el Sistema, Intente Nuevamente.");
					}
				}
			}
			
		} else {
			loga.add(getCurrentTime() +  " Ha Ocurrido un Error al Buscar en el Sistema el ID del dispositivo.");
		}
	}
	
	// Getters and Setters -------- //

	/**
	 * @return the compareToPrechargeFile
	 */
	public boolean isCompareToPrechargeFile() {
		return compareToPrechargeFile;
	}

	/**
	 * @param compareToPrechargeFile the compareToPrechargeFile to set
	 */
	public void setCompareToPrechargeFile(boolean compareToPrechargeFile) {
		this.compareToPrechargeFile = compareToPrechargeFile;
	}

	/**
	 * @param showPanelEntry the showPanelEntry to set
	 */
	public void setShowPanelEntry(boolean showPanelEntry) {
		this.showPanelEntry = showPanelEntry;
	}

	/**
	 * @return the showPanelEntry
	 */
	public boolean isShowPanelEntry() {
		return showPanelEntry;
	}

	/**
	 * @param loga the loga to set
	 */
	public void setLoga(List<String> loga) {
		this.loga = loga;
	}

	/**
	 * @return the loga
	 */
	public List<String> getLoga() {
		if(loga != null) {
			Collections.reverse(loga);
		}
		return loga;
	}

	/**
	 * @param logc the logc to set
	 */
	public void setLogc(List<String> logc) {
		this.logc = logc;
	}

	/**
	 * @return the logc
	 */
	public List<String> getLogc() {
		if(logc != null) {
			Collections.reverse(logc);
		}
		return logc;
	}

	/**
	 * @param entryNumber the entryNumber to set
	 */
	public void setEntryNumber(String entryNumber) {
		this.entryNumber = entryNumber;
	}

	/**
	 * @return the entryNumber
	 */
	public String getEntryNumber() {
		return entryNumber;
	}

	/**
	 * @param cardQuantity the cardQuantity to set
	 */
	public void setCardQuantity(Long cardQuantity) {
		this.cardQuantity = cardQuantity;
	}

	/**
	 * @return the cardQuantity
	 */
	public Long getCardQuantity() {
		return cardQuantity;
	}

	/**
	 * @param warehouseId the warehouseId to set
	 */
	public void setWarehouseId(TbWarehouse warehouseId) {
		this.warehouseId = warehouseId;
	}

	/**
	 * @return the warehouseId
	 */
	public TbWarehouse getWarehouseId() {
		return warehouseId;
	}

	/**
	 * @param left the left to set
	 */
	public void setLeft(Long left) {
		this.left = left;
	}

	/**
	 * @return the left
	 */
	public Long getLeft() {
		return left;
	}

	/**
	 * @param device the device to set
	 */
	public void setDeviceList(List<TbDevice> device) {
		this.deviceList = device;
	}

	/**
	 * @return the device
	 */
	public List<TbDevice> getDeviceList() {
		return deviceList;
	}

	/**
	 * @param entryCard the entryCard to set
	 */
	public void setEntryCard(boolean entryCard) {
		this.entryCard = entryCard;
	}

	/**
	 * @return the entryCard
	 */
	public boolean isEntryCard() {
		if (entryTag) {
			entryCard = false;
		} else {
			entryCard = true;
		}
		return entryCard;
	}

	/**
	 * @param entryTag the entryTag to set
	 */
	public void setEntryTag(boolean entryTag) {
		this.entryTag = entryTag;
	}

	/**
	 * @return the entryTag
	 */
	public boolean isEntryTag() {
		if (entryCard) {
			entryTag = false;
		} else {
			entryTag = true;
		}
		return entryTag;
	}

	/**
	 * @param showEntryDetailPanelTag the showEntryDetailPanelTag to set
	 */
	public void setShowEntryDetailPanelTag(boolean showEntryDetailPanelTag) {
		this.showEntryDetailPanelTag = showEntryDetailPanelTag;
	}

	/**
	 * @return the showEntryDetailPanelTag
	 */
	public boolean isShowEntryDetailPanelTag() {
		return showEntryDetailPanelTag;
	}

	/**
	 * @param tagSerial the tagSerial to set
	 */
	public void setTagSerial(String tagSerial) {
		this.tagSerial = tagSerial;
	}

	/**
	 * @return the tagSerial
	 */
	public String getTagSerial() {
		return tagSerial;
	}

	/**
	 * @param typeList the typeList to set
	 */
	public void setTypeList(List<SelectItem> typeList) {
		this.typeList = typeList;
	}

	/**
	 * @return the typeList
	 */
	public List<SelectItem> getTypeList() {
		if (typeList == null) {
			typeList = new ArrayList<SelectItem>();
			for(TbTagType tt : tagType.getTagType()) {
				typeList.add(new SelectItem(tt.getTagTypeId(), tt.getTagTypeName()));
			}
		}
		return typeList;
	}

	/**
	 * @param tagTypeId the tagTypeId to set
	 */
	public void setTagTypeId(Long tagTypeId) {
		this.tagTypeId = tagTypeId;
	}

	/**
	 * @return the tagTypeId
	 */
	public Long getTagTypeId() {
		return tagTypeId;
	}

	/**
	 * @param showDetail the showDetail to set
	 */
	public void setShowDetail(boolean showDetail) {
		this.showDetail = showDetail;
	}

	/**
	 * @return the showDetail
	 */
	public boolean isShowDetail() {
		return showDetail;
	}
}