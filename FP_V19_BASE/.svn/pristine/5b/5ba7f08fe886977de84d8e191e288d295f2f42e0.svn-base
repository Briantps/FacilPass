/**
 * 
 */
package crud;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import sessionVar.SessionUtil;

import ejb.Master;
import ejb.crud.SpecialExemptType;

import jpa.TbDeviceType;
import jpa.TbSpecialExemptType;

/**
 * @author tmolina
 *
 */
public class SpecialExemptTypeBean implements Serializable {
	private static final long serialVersionUID = 4307314853821331540L;

	@EJB(mappedName="ejb/SpecialExemptType")
	private SpecialExemptType types;
	
	@EJB(mappedName = "ejb/Master")
	private Master master;
	
	// Attributes.
	
	private boolean showModal;
	
	private String modalMessage;
	
	private List<TbSpecialExemptType> typeList;
	
	private Long typeId;
	
	private boolean showInsert;
	
	private String typeName;
	
	private boolean showEdit;
	
	private List<SelectItem> deviceTypeList;
	
	private Long deviceTypeId;

	/**
	 * Constructor.
	 */
	public SpecialExemptTypeBean() {
	}
	
	// Actions ------------ //
	
	/**
	 * Init Add Special-Exempt Type.
	 */
	public String initAddType(){
		setShowInsert(true);
		setShowEdit(false);
		setTypeName(null);
		return null;
	}
	
	/**
	 * Inserts a new Special-Exempt Type.
	 */
	public String addType(){
		setShowInsert(false);
		Long result = types.insertTbSpecialExemptType(typeName, SessionUtil
				.ip(), SessionUtil.sessionUser().getUserId(), deviceTypeId);
		if (result != null) {
			if(result != -1L){
				setModalMessage("Transacción Exitosa.");
				setTypeList(null);
			} else {
				setModalMessage("Ya hay un tipo de exento y especial con ese nombre. Verifique.");
			}
		} else {
			setModalMessage("Error en Transacción.");
		}
		setShowModal(true);
		return null;
	}
	
	/**
	 * Init Edition of Special-Exempt Type.
	 */
	public String initEditType(){
		setShowInsert(false);
		setShowEdit(true);
		return null;
	}
	
	/**
	 * Edits a Special-Exempt Type.
	 */
	public String editType(){
		setShowEdit(false);
		setShowInsert(false);
		return null;
	}
	
	/**
	 * Hides modal windows.
	 */
	public String hideModal(){
		setShowInsert(false);
		setShowEdit(false);
		setShowModal(false);
		setModalMessage(null);
		return null;
	}
	
	// Getters and Setters ------------ //

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

	/**
	 * @param showInsert the showInsert to set
	 */
	public void setShowInsert(boolean showInsert) {
		this.showInsert = showInsert;
	}

	/**
	 * @return the showInsert
	 */
	public boolean isShowInsert() {
		return showInsert;
	}

	/**
	 * @param showEdit the showEdit to set
	 */
	public void setShowEdit(boolean showEdit) {
		this.showEdit = showEdit;
	}

	/**
	 * @return the showEdit
	 */
	public boolean isShowEdit() {
		return showEdit;
	}

	/**
	 * @param typeList the typeList to set
	 */
	public void setTypeList(List<TbSpecialExemptType> typeList) {
		this.typeList = typeList;
	}

	/**
	 * @return the typeList
	 */
	public List<TbSpecialExemptType> getTypeList() {
		if(typeList == null) {
			typeList = new ArrayList<TbSpecialExemptType>();
			typeList = types.getEspecialExemptTypes();
		}
		return typeList;
	}

	/**
	 * @param typeId the typeId to set
	 */
	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	/**
	 * @return the typeId
	 */
	public Long getTypeId() {
		return typeId;
	}

	/**
	 * @param typeName the typeName to set
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	/**
	 * @return the typeName
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * @param deviceTypeList the deviceTypeList to set
	 */
	public void setDeviceTypeList(List<SelectItem> deviceTypeList) {
		this.deviceTypeList = deviceTypeList;
	}

	/**
	 * @return the deviceTypeList
	 */
	public List<SelectItem> getDeviceTypeList() {
		if(deviceTypeList == null){
			deviceTypeList = new ArrayList<SelectItem>();
			for(TbDeviceType dt : master.getPertiDeviceTypes()){
				deviceTypeList.add(new SelectItem(dt.getDeviceTypeId(), dt.getDeviceTypeName()));
			}
		}
		return deviceTypeList;
	}

	/**
	 * @param deviceTypeId the deviceTypeId to set
	 */
	public void setDeviceTypeId(Long deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}

	/**
	 * @return the deviceTypeId
	 */
	public Long getDeviceTypeId() {
		return deviceTypeId;
	}
}