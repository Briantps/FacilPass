/**
 * 
 */
package process.device;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import constant.DeviceType;

import ejb.User;
import ejb.crud.CompanyTag;
import ejb.crud.TagType;

import jpa.TbCompanyTag;
import jpa.TbCourier;
import jpa.TbDevice;
import jpa.TbDeviceState;
import jpa.TbDeviceType;
import jpa.TbRollo;
import jpa.TbTagType;

import security.UserLogged;
import sessionVar.SessionUtil;
import validator.Validation;

/**
 * Bean to Manage Device register.
 * @author tmolina
 */
public class EnterDevice implements Serializable {
	private static final long serialVersionUID = -7148175549095352008L;
	
	@EJB(mappedName="ejb/Device")
	private ejb.Device deviceEJB;
	
	@EJB(mappedName="ejb/CompanyTag")
	private CompanyTag companyTagEJB;
	
	@EJB(mappedName = "ejb/TagType")
	private TagType tagType;
	
	@EJB(mappedName = "ejb/User")
	private User userEJB;

	// Attributes
	
	private Long typeDevId=0L;
	
	private Long tagTypeId=0L;	
	
	private Long deviceStateId=0L;
	
	private String facial="";
	
	private String modalMessage;
	
	private String deviceCode="";
	
	private boolean showTagManu;
	
	private boolean showModal;
	
	private boolean showInsertCompanyTag;
	
	private boolean showInsertCourier;
	
	private boolean showInsertRoll;
	
	private boolean showModalValidateCompanyTag;
	
	private boolean showModalValidateCourier;
	
	private boolean showModalValidateRoll;
	
    private boolean showModalConfirmationCompanyTag;
	
	private boolean showModalConfirmationCourier;
	
	private boolean showModalConfirmationRoll;
	
	private boolean permAdd;
	
	private Long deviceLength;
	
	private Long companyTagId;
	
	private Long courierId;
	
	private String courierIdNew;
	
	private Long rolloId;
	
	private String companyTagName;
	
	private String courierName;
	
	private String rollName;
	
	private String nameperm;
	
	private List<SelectItem> typeList;
	
	private List<SelectItem> devicesTypes;
	
	private List<SelectItem> deviceStates;
	
	private List<SelectItem> courierList;
	
	private List<SelectItem> rolloList;
	
	private List<SelectItem> companyTagList;
	
	private UserLogged usrs;	

	/**
	 * Constructor.
	 */
	public EnterDevice() {
		clear();
		showTagManu = true;
	}
	
	@PostConstruct
	public void init(){
		setUsrs((UserLogged) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user"));
		nameperm = "createCompanyCourierRoll";
		permAdd = userEJB.getPermission(usrs.getUserId(),nameperm);
		System.out.println("Permiso de Crear " + permAdd);
		clear();
		this.getTypeList();
	}
	
	// Actions ----------------- //
	
	public void clear() {	
		this.setDeviceCode("");
		this.setFacial("");
		this.setTypeDevId(null);
		this.setTagTypeId(0L);
		this.setDeviceStateId(null);
		this.setCompanyTagId(-1L);
		this.setCourierId(-1L);
		this.setRolloId(-1L);
	}
	
	public void hideModal() {
		this.setModalMessage("");
		this.setShowModal(false);
		this.setShowInsertCompanyTag(false);
		this.setShowInsertCourier(false);
		this.setShowInsertRoll(false);
		this.setShowModalValidateCompanyTag(false);
		this.setShowModalValidateCourier(false);
		this.setShowModalValidateRoll(false);
		this.setShowModalConfirmationCompanyTag(false);
		this.setShowModalConfirmationRoll(false);
		this.setShowModalConfirmationCourier(false);
	}
	
	public void hideModal1() {
		this.hideModal();
		this.setCourierIdNew("");
	}
	
	public void hideModal3(){
		this.setShowModalValidateCompanyTag(false);
		this.setShowModalConfirmationCompanyTag(false);
		this.setShowModal(false);
		this.setShowInsertCompanyTag(true);
	}
	
	public void hideModal4(){
		this.setShowModalValidateCourier(false);
		this.setShowModalConfirmationCourier(false);
		this.setShowModal(false);
		this.setShowInsertCourier(true);
	}
	
	public void hideModal5(){
		this.setShowModalValidateRoll(false);
		this.setShowModalConfirmationRoll(false);
		this.setShowModal(false);
		this.setShowInsertRoll(true);
	}
	
	public String changeTypeDevice (){
		if((typeDevId.longValue() == DeviceType.TAG.getId().longValue()) || (typeDevId.longValue() == DeviceType.TAGSTEP.getId().longValue())){
			showTagManu = true;						
		} else {
			showTagManu = false;
		}
		return null;
	}
			
	/**
	 * 
	 * @return
	 */
	public String save() {
		facial = facial.trim();
		if((tagTypeId == -1L) &&(showTagManu)){
			setModalMessage("No ha seleccionado un Fabricante.");
			setShowModal(true);
		}else {
		if (deviceCode != null && !deviceCode.equals("")) {
			deviceCode = deviceCode.toUpperCase();			
			if(deviceCode.length() > deviceLength.intValue()){
				setModalMessage("El Id del Dispositivo excede el largo según el fabricante.");
				setShowModal(true);
			} else if(deviceCode.length() < 6){
				setModalMessage("El Id del Dispositivo es menor el largo permitido. Debe ser mayor a 6 dígitos.");
				setShowModal(true);
			}else if(validateHexa(deviceCode.toUpperCase())==false) {
				setModalMessage("El Id del Dispositivo no es Válido. Solo se acepta Valores Hexadecimales.");
				setShowModal(true);
			} else if(deviceCode.length()%2!=0){
				setModalMessage("El Id del Dispositivo no es válido. La longitud debe ser par.");
				setShowModal(true);				
			}else if(companyTagId==null || companyTagId==-1){
				setModalMessage("El campo Empresa es requerido.");
				setShowModal(true);				
			}else if(courierId==null || courierId==-1){
				setModalMessage("El campo Courier es requerido.");
				setShowModal(true);				
			}
			else if(rolloId==null || rolloId==-1){
				setModalMessage("El campo Rollo es requerido.");
				setShowModal(true);				
			}
			else {
				if(facial != null && !facial.equals("")){					
					if(!validator.Validation.isAlphaNum(facial)){
						setModalMessage("El facial contiene caracteres inválidos");
						setShowModal(true);		
					}else{
						TbDevice de = deviceEJB.getDeviceByCode(deviceCode);
						if (de == null) {
							String result=deviceEJB.getDeviceByFacial(facial);
							if(result==null){
								if (deviceEJB.saveDeviceCompanyTag(deviceCode.toUpperCase(), facial, 0L,
										typeDevId, 15L, companyTagId, courierId, rolloId, SessionUtil.ip(),
										SessionUtil.sessionUser().getUserId())) {
									if ((typeDevId.longValue() == DeviceType.TAG.getId().longValue()) || (typeDevId.longValue() == DeviceType.TAGSTEP.getId().longValue()))
									 {
										TbDevice d = deviceEJB.getDeviceByCode(deviceCode);
										if(d != null){
											deviceEJB.createRealationtagType(d.getDeviceId(),
												tagTypeId, SessionUtil.ip(), SessionUtil
														.sessionUser().getUserId());
											clear();
											setShowModal(true);
											setModalMessage("Transacción Exitosa.");
										}else{
											setShowModal(true);
											setModalMessage("Error al intentar guardar el dispositivo.");
										}
									}
									
								} else {
									setShowModal(true);
									setModalMessage("Error al intentar guardar el dispositivo.");
								}
							} else {
								setModalMessage(result);
								setShowModal(true);
							}
						}else{
							setShowModal(true);
							setModalMessage("Error!!! Existe un Dispositivo con el mismo Id Interno, Verifique.");
						}
					}
				}else{
					setShowModal(true);
					setModalMessage("Digite el facial del dispositivo");
				}
			}		  
		}else{
			setShowModal(true);
			setModalMessage("Digite el Id del dispositivo");
		}
	    }
		return null;
	}
	
	public void initAddCompanyTag(){
		this.setShowModal(false);
		this.setShowInsertCompanyTag(true);
		this.setCompanyTagName(null);
	}
	
	public void initAddCourier(){
		this.setCourierIdNew("");
		if(companyTagId==-1L){
			this.setShowInsertCourier(false);
			setModalMessage("El campo Empresa es requerido.");
			setShowModal(true);	
		}
		else{	
			this.setShowModal(false);
			this.setShowInsertCourier(true);
			this.setCompanyTagName(companyTagEJB.getCompanyTagName(companyTagId));
			this.setCourierName(null);		
		}
	}
	
	public void initAddRoll(){
		if(companyTagId==-1L){
			this.setShowInsertRoll(false);
			setModalMessage("El campo Empresa es requerido.");
			setShowModal(true);	
		}
		else if(courierId==-1){
			this.setShowInsertRoll(false);
			setModalMessage("El campo Courier es requerido.");
			setShowModal(true);
		}
		else{
			this.setShowModal(false);
			this.setShowInsertRoll(true);
			this.setCompanyTagName(companyTagEJB.getCompanyTagName(companyTagId));
			this.setCourierName(companyTagEJB.getCourierName(courierId));
			this.setRollName(null);		
		}
	}
	
	/**
	 * Método encargado de crear la empresa
	 * @author psanchez
	 */
	public void validationAddCompanyTag(){
	  this.hideModal();
	  try{
		    if(companyTagName.equals(null) || companyTagName.trim().equals("")){
		    	this.setModalMessage("El Campo Empresa no puede estar vacío.");
		    	this.setShowModalValidateCompanyTag(true);
	        }
			else if(!companyTagName.matches("([a-z]|[A-Z]|[áéíóúñ]|[ÁÉÍÓÚÑ]|[0-9]|[!\"#$%&'()*+,-./;=¿?¡\\[\\]]||[/\\\\]|\\s)+")){
				this.setModalMessage("El Campo Empresa contiene caracteres inválidos.");
				this.setShowModalValidateCompanyTag(true);
			}
			else if(companyTagName.length()> 50L){
		    	this.setModalMessage("El tamaño del nombre de la empresa debe ser máximo 50 caracteres.");
		    	this.setShowModalValidateCompanyTag(true);
		    }
			else{
		      String result = companyTagEJB.existCompanyTag(companyTagName);
			  if(result != null ){
				this.setModalMessage(result);
				this.setShowModalValidateCompanyTag(true);
			  }else {
				this.setShowModalValidateCompanyTag(false);
				this.setShowModalConfirmationCompanyTag(true);
				this.setModalMessage("¿Está seguro de crear la empresa "+ companyTagName+"?");
			  }
		   }	
		}catch(Exception e){
			this.setModalMessage("El Campo Empresa contiene caracteres inválidos..");
			this.setShowModalValidateCompanyTag(true);
		}
	}
	
	public void insertCompanyTag(){
	   this.hideModal();
	   try{
			String result =companyTagEJB.insertCompanyTag(companyTagName, SessionUtil.ip(), SessionUtil.sessionUser().getUserId());
			if(result != null ){
				this.setModalMessage(result);
			} else {
				this.setModalMessage("Error en transación.");
			}
			this.setShowModal(true);
	   }catch(Exception e){
		   System.out.println(" [ Error en EnterDevice.insertCompanyTag] " +e.getMessage());
	   }
    } 
	
	
	/**
	 * Método encargado de crear la empresa
	 * @author psanchez
	 */
	public void validationAddCourier(){
	  this.hideModal();
	  try{
		    if(courierIdNew==null || courierIdNew.equals("")){
		    	this.setModalMessage("El campo Id Courier no puede estar vacío.");
		    	this.setShowModalValidateCourier(true);
		    }
		    else if(!Validation.isNumeric(courierIdNew)){
		    	this.setModalMessage("El campo Id Courier contiene caracteres inválidos.");
		    	this.setShowModalValidateCourier(true);  
		    }
		    else if(courierIdNew.equals("0")){
		    	this.setModalMessage("El campo Id Courier debe contener valores superiores a cero (0).");
		    	this.setShowModalValidateCourier(true);
	        }
		    else if(courierIdNew.substring(0,1).equals("0")){
		    	this.setModalMessage("El campo Id Courier no debe iniciar con cero (0).");
		    	this.setShowModalValidateCourier(true);
	        }
		    else if(courierIdNew.length()> 15L){
		    	this.setModalMessage("El campo Id Courier debe ser máximo 15 dígitos.");
		    	this.setShowModalValidateCourier(true);
		    }
		    else if(courierName==null || courierName.trim().equals("")){
		    	this.setModalMessage("El campo Nombre Courier no puede estar vacío.");
		    	this.setShowModalValidateCourier(true);
		    }
			else if(!courierName.matches("([a-z]|[A-Z]|[áéíóúñ]|[ÁÉÍÓÚÑ]|[0-9]|[!\"#$%&'()*+,-./;=¿?¡\\[\\]]||[/\\\\]|\\s)+")){
				this.setModalMessage("El campo Nombre Courier contiene caracteres inválidos.");
				this.setShowModalValidateCourier(true);
			}
			else if(courierName.length()> 50L){
		    	this.setModalMessage("El campo Nombre Courier excede los 50 caracteres.");
		    	this.setShowModalValidateCourier(true);
		    }
			else{
		      String result = companyTagEJB.existCourier(courierName, Long.parseLong(courierIdNew));
			  if(result != null ){
				this.setModalMessage(result);
				this.setShowModalValidateCourier(true);
			  }else {
				this.setShowModalValidateCourier(false);
				this.setShowModalConfirmationCourier(true);
				this.setModalMessage("¿Está seguro de crear el courier "+ courierName+"?");
			  }
		   }	
    	}catch(Exception e){
			this.setModalMessage("El campo Nombre Courier contiene caracteres inválidos.");
	    	this.setShowModalValidateCourier(true);
		}
	 }
	
	public void insertCourier(){
	   this.hideModal();
	   try{
			String result =companyTagEJB.insertCourier(Long.parseLong(courierIdNew), courierName, companyTagId, SessionUtil.ip(), SessionUtil.sessionUser().getUserId());
			if(result != null ){
				this.setCompanyTagId(companyTagId);
				this.setModalMessage(result);
			} else {
				this.setModalMessage("Error en transación.");
			}
			this.setShowModal(true);
	   }catch(Exception e){
		   System.out.println(" [ Error en EnterDevice.insertCourier] " +e.getMessage());
	   }
    } 
	
	/**
	 * Método encargado de crear la empresa
	 * @author psanchez
	 */
	public void validationAddRoll(){
	  this.hideModal();
	  try{
		  if(rollName.equals(null) || rollName.trim().equals("")){
		    	this.setModalMessage("El campo Nombre Rollo no puede estar vacío.");
		    	this.setShowModalValidateRoll(true);
	        }
			else if(!rollName.matches("([a-z]|[A-Z]|[áéíóúñ]|[ÁÉÍÓÚÑ]|[0-9]|[!\"#$%&'()*+,-./;=¿?¡\\[\\]]||[/\\\\]|\\s)+")){
				this.setModalMessage("El campo Nombre Rollo contiene caracteres inválidos.");
				this.setShowModalValidateRoll(true);
			}
			else if(rollName.length()> 50L){
		    	this.setModalMessage("El campo Nombre Rollo excede los 50 caracteres.");
		    	this.setShowModalValidateRoll(true);
		    }
			else{
		      String result = companyTagEJB.existRoll(rollName, rolloId);
			  if(result != null ){
				this.setModalMessage(result);
				this.setShowModalValidateRoll(true);
			  }else {
				this.setShowModalValidateRoll(false);
				this.setShowModalConfirmationRoll(true);
				this.setModalMessage("¿Está seguro de crear el rollo "+rollName+"?");
			  }
		   }
	    }catch(Exception e){
			this.setModalMessage("El campo Nombre Rollo contiene caracteres inválidos.");
			this.setShowModalValidateRoll(true);
		}
	 }
	
	public void insertRoll(){
	   this.hideModal();
	   try{
			String result =companyTagEJB.insertRoll(rollName, courierId, SessionUtil.ip(), SessionUtil.sessionUser().getUserId());
			if(result != null ){
				this.setCourierId(courierId);
				this.setModalMessage(result);
			} else {
				this.setModalMessage("Error en transación.");
			}
			this.setShowModal(true);
	   }catch(Exception e){
		   System.out.println(" [ Error en EnterDevice.insertRoll] " +e.getMessage());
	   }
    } 
	
	// Getter and Setter ----------------- // 

	/**
	 * @param devicesTypes the devicesTypes to set
	 */
	public void setDevicesTypes(List<SelectItem> devicesTypes) {
		this.devicesTypes = devicesTypes;
	}

	/**
	 * @return the deviceCode
	 */
	public String getDeviceCode() {
		return deviceCode;
	}

	/**
	 * @param deviceCode the deviceCode to set
	 */
	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}

	/**
	 * @return the devicesTypes
	 */
	public List<SelectItem> getDevicesTypes() {
		if (devicesTypes == null) {
			devicesTypes = new ArrayList<SelectItem>();
			for (TbDeviceType dt : deviceEJB.getDevicesTypes()) {
			 /* Lista unicamente los dispositivos clasificados como tag*/
				if(dt.getDeviceTypeClass()==0){
				devicesTypes.add(new SelectItem(dt.getDeviceTypeId(), dt
							.getDeviceTypeName()));
			  }
			}
		}
		return devicesTypes;
	}

	/**
	 * @param typeDevId the typeDevId to set
	 */
	public void setTypeDevId(Long typeDevId) {
		this.typeDevId = typeDevId;
	}

	/**
	 * @return the typeDevId
	 */
	public Long getTypeDevId() {
		return typeDevId;
	}

	/**
	 * @param facial the facial to set
	 */
	public void setFacial(String facial) {
		this.facial = facial;
	}

	/**
	 * @return the facial
	 */
	public String getFacial() {
		return facial;
	}

	/**
	 * @param deviceStates the deviceStates to set
	 */
	public void setDeviceStates(List<SelectItem> deviceStates) {
		this.deviceStates = deviceStates;
	}

	/**
	 * @return the deviceStates
	 */
	public List<SelectItem> getDeviceStates() {
		if (deviceStates == null) {
			deviceStates = new ArrayList<SelectItem>();
			for (TbDeviceState ds : deviceEJB.getDeviceStates()) {
				deviceStates.add(new SelectItem(ds.getDeviceStateId(), ds.getDeviceStateDescription()));
			}
		}
		return deviceStates;
	}

	/**
	 * @param deviceStateId the deviceStateId to set
	 */
	public void setDeviceStateId(Long deviceStateId) {
		this.deviceStateId = deviceStateId;
	}

	/**
	 * @return the deviceStateId
	 */
	public Long getDeviceStateId() {
		return deviceStateId;
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
	 * @param showTagManu the showTagManu to set
	 */
	public void setShowTagManu(boolean showTagManu) {
		this.showTagManu = showTagManu;
	}

	/**
	 * @return the showTagManu
	 */
	public boolean isShowTagManu() {
		return showTagManu;
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
		} else {
			typeList.clear();
		}
		typeList.add(new SelectItem(-1L,"--Seleccione Fabricante--"));
		for(TbTagType tt : tagType.getTagType()) {
			if(tt.getTagState().intValue()== 1){
				typeList.add(new SelectItem(tt.getTagTypeId(), tt.getTagTypeName()));
			}
		}
		return typeList;
	}
	
	/**
	 * @param showModal the showModal to set
	 */
	public void setShowModal(boolean showModal) {
		this.showModal = showModal;
	}

	/**
	 * @return the showModal
	 */
	public boolean isShowModal() {
		return showModal;
	}

	/**
	 * @param modalMessage the modalMessage to set
	 */
	public void setModalMessage(String modalMessage) {
		this.modalMessage = modalMessage;
	}

	/**
	 * @return the modalMessage
	 */
	public String getModalMessage() {
		return modalMessage;
	}

	public void setDeviceLength(Long deviceLength) {
		this.deviceLength = deviceLength;
	}

	public Long getDeviceLength() {
		if(deviceLength == null){
		  deviceLength = 6L;
		}
		return deviceLength;
	}
	
	public void changeTypeList(){
		if(tagTypeId == -1L){
			deviceLength = 6L;
		} else{
			deviceLength = tagType.lengDeviceId(tagTypeId);	
		}
	}
	
	public boolean validateHexa(String device){
		boolean result = false;
		int tamanio = device.length();
		char dato;
		for (int i=0; i<tamanio; i++){
			dato = device.charAt(i);
			if ((((int)dato) >=48) && (((int)dato) <=57)){
				result =  true;
			} else if ((((int)dato) >=65) && (((int)dato) <=70)){
				result =  true;
			} else {
				result =  false;
				break;
			}
		}
			
		return result;
	}

	/**
	 * @param rolloId the rolloId to set
	 */
	public void setRolloId(Long rolloId) {
		this.rolloId = rolloId;
	}

	/**
	 * @return the rolloId
	 */
	public Long getRolloId() {
		return rolloId;
	}


	/**
	 * @param courierList the courierList to set
	 */
	public void setCourierList(List<SelectItem> courierList) {
		this.courierList = courierList;
	}

	/**
	 * @return the courierList
	 */
	public List<SelectItem> getCourierList() {
		courierList= new ArrayList<SelectItem>();
		courierList.add(new SelectItem(-1, "--Seleccione Uno--"));
		for(TbCourier tc : companyTagEJB.getCourier(companyTagId)){
			courierList.add(new SelectItem(tc.getCourierId(), tc.getCourierName()));
		}
		return courierList;
	}
	
	
	/**
	 * @param rolloList the rolloList to set
	 */
	public void setRolloList(List<SelectItem> rolloList) {
		this.rolloList = rolloList;
	}

	/**
	 * @return the rollList
	 */
	public List<SelectItem> getRollList() {
		rolloList= new ArrayList<SelectItem>();
		rolloList.add(new SelectItem(-1, "--Seleccione Uno--"));
		if(courierId!=null && courierId>-1){
			for(TbRollo tr: companyTagEJB.getRollosByCourier(companyTagId, courierId)){
				rolloList.add(new SelectItem(tr.getRollId(), tr.getRollName()));	
			}
		}
		return rolloList;
	}

	public void setCompanyTagList(List<SelectItem> companyTagList) {
		this.companyTagList = companyTagList;
	}

	public List<SelectItem> getCompanyTagList() {
		if (companyTagList == null) {
			companyTagList = new ArrayList<SelectItem>();
		} else {
			companyTagList.clear();
		}
		companyTagList.add(new SelectItem(-1L,"--Seleccione Empresa--"));
		for(TbCompanyTag tct : companyTagEJB.getCompanyTag()) {
			if(tct.getCompanyTagState().intValue()== 1){
				companyTagList.add(new SelectItem(tct.getCompanyTagId(), tct.getCompanyTagName()));
			}
		}
		return companyTagList;
	}

	public void setCompanyTagId(Long companyTagId) {
		this.companyTagId = companyTagId;
	}

	public Long getCompanyTagId() {
		return companyTagId;
	}

	public void setShowInsertCourier(boolean showInsertCourier) {
		this.showInsertCourier = showInsertCourier;
	}

	public boolean isShowInsertCourier() {
		return showInsertCourier;
	}

	public void setShowInsertRoll(boolean showInsertRoll) {
		this.showInsertRoll = showInsertRoll;
	}

	public boolean isShowInsertRoll() {
		return showInsertRoll;
	}

	public void setCompanyTagName(String companyTagName) {
		this.companyTagName = companyTagName;
	}

	public String getCompanyTagName() {
		return companyTagName;
	}

	public void setCourierName(String courierName) {
		this.courierName = courierName;
	}

	public String getCourierName() {
		return courierName;
	}

	public void setRollName(String rollName) {
		this.rollName = rollName;
	}

	public String getRollName() {
		return rollName;
	}

	public void setShowInsertCompanyTag(boolean showInsertCompanyTag) {
		this.showInsertCompanyTag = showInsertCompanyTag;
	}

	public boolean isShowInsertCompanyTag() {
		return showInsertCompanyTag;
	}

	public void setShowModalValidateCompanyTag(boolean showModalValidateCompanyTag) {
		this.showModalValidateCompanyTag = showModalValidateCompanyTag;
	}

	public boolean isShowModalValidateCompanyTag() {
		return showModalValidateCompanyTag;
	}

	public void setShowModalValidateRoll(boolean showModalValidateRoll) {
		this.showModalValidateRoll = showModalValidateRoll;
	}

	public boolean isShowModalValidateRoll() {
		return showModalValidateRoll;
	}

	public void setShowModalValidateCourier(boolean showModalValidateCourier) {
		this.showModalValidateCourier = showModalValidateCourier;
	}

	public boolean isShowModalValidateCourier() {
		return showModalValidateCourier;
	}

	public void setShowModalConfirmationCompanyTag(
			boolean showModalConfirmationCompanyTag) {
		this.showModalConfirmationCompanyTag = showModalConfirmationCompanyTag;
	}

	public boolean isShowModalConfirmationCompanyTag() {
		return showModalConfirmationCompanyTag;
	}

	public void setShowModalConfirmationCourier(boolean showModalConfirmationCourier) {
		this.showModalConfirmationCourier = showModalConfirmationCourier;
	}

	public boolean isShowModalConfirmationCourier() {
		return showModalConfirmationCourier;
	}

	public void setShowModalConfirmationRoll(boolean showModalConfirmationRoll) {
		this.showModalConfirmationRoll = showModalConfirmationRoll;
	}

	public boolean isShowModalConfirmationRoll() {
		return showModalConfirmationRoll;
	}

	public void setNameperm(String nameperm) {
		this.nameperm = nameperm;
	}

	public String getNameperm() {
		return nameperm;
	}

	public void setPermAdd(boolean permAdd) {
		this.permAdd = permAdd;
	}

	public boolean isPermAdd() {
		return permAdd;
	}

	public void setUsrs(UserLogged usrs) {
		this.usrs = usrs;
	}

	public UserLogged getUsrs() {
		return usrs;
	}

	public void setCourierIdNew(String courierIdNew) {
		this.courierIdNew = courierIdNew;
	}

	public String getCourierIdNew() {
		return courierIdNew;
	}

}