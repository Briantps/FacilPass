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

import ejb.crud.ExemptOffice;
import ejb.crud.SpecialExemptType;

import jpa.TbSpecialExemptOffice;
import jpa.TbSpecialExemptType;

/**
 * @author tmolina
 *
 */
public class ExemptOfficeBean implements Serializable {
	private static final long serialVersionUID = -54220742800233266L;

	@EJB(mappedName="ejb/ExemptOffice")
	private ExemptOffice office;
	
	@EJB(mappedName="ejb/SpecialExemptType")
	private SpecialExemptType types;
	
	// Attributes---- //
	
	private boolean showModal;
	
	private String modalMessage;
	
	private List<TbSpecialExemptOffice> officeList;
	
	private Long officeId;
	
	private boolean showInsert;
	
	private String officeName;
	
	private boolean showEdit;
	
	private Long exemptTypeId;
	
	private List<SelectItem> exemptTypes;
	
	private String authoreizedBy;
	
	private String officeAddress;
	
	private String officeEmail;
	
	private String officeFax;
	
	private String officePhone;
	
	private String errorMessage;
	
	private boolean showError;
	
	private String typeName;

	/**
	 * Constructor.
	 */
	public ExemptOfficeBean() {
	}
	
	// Actions ------------ //
	
	/**
	 * Clear Data.
	 */
	public String clear(){
		setOfficeName(null);
		setExemptTypeId(null);
		setAuthoreizedBy(null);
		setOfficeAddress(null);
		setOfficeEmail(null);
		setOfficeFax(null);
		setOfficePhone(null);
		return null;
	}
	
	/**
	 * Init Add Exempt Office.
	 */
	public String initAddOffice(){
		clear();
		setShowInsert(true);
		setShowEdit(false);
		return null;
	}
	
	/**
	 * Inserts a new Exempt Office.
	 */
	public String addOffice(){
		if(exemptTypeId!=null && exemptTypeId!=-1L){
			if(officeAddress!=null && officeName.trim().length()>0){
				setShowError(false);
				setShowInsert(false);
				Long result = office.insertTbSpecialExemptOffice(officeName,
						SessionUtil.ip(), SessionUtil.sessionUser().getUserId(), exemptTypeId,
						authoreizedBy, officeAddress, officeEmail, officeFax, officePhone);
				if (result != null) {
					if(result != -1L){
						setModalMessage("Transacción Exitosa.");
						setOfficeList(null);
					} else {
						setModalMessage("Ya hay una Dependencia con ese nombre. Verifique.");
					}
				} else {
					setModalMessage("Error en Transacción.");
				}
				setShowModal(true);
			} else {
				setShowError(true);
				setErrorMessage("Debe digitar un nombre de dependencia válido.");
			}
		} else {
			setShowError(true);
			setErrorMessage("Debe escoger un tipo para la dependencia.");
		}
		return null;
	}
	
	/**
	 * Init Edition of Exempt Office.
	 */
	public String initEditOffice(){
		setShowInsert(false);
		setShowEdit(true);
		for(TbSpecialExemptOffice rc : officeList){
			if(officeId.longValue() == rc.getSpecialExemptOfficeId().longValue()){
				officeName = rc.getOfficeName();
				officeAddress = rc.getOfficeAddress();
				officeEmail = rc.getOfficeEmail();
				officeFax = rc.getOfficeFax();
				officePhone = rc.getOfficePhone();
				typeName = rc.getTbSpecialExemptType().getSpecialExemptTypeName();
				break;
			}
		}
		return null;
	}
	
	/**
	 * Edits a Exempt Office.
	 */
	public String editOffice(){
		setShowEdit(false);
		setShowInsert(false);
		if(officeName!=null){
			
			TbSpecialExemptOffice temp = null;
			for (TbSpecialExemptOffice rc : officeList) {
				if (officeId.longValue() == rc.getSpecialExemptOfficeId()
						.longValue()) {
					temp = rc;
					break;
				}
			}
				
			for(TbSpecialExemptOffice rc : officeList){
				if (temp.getTbSpecialExemptType().getSpecialExemptTypeId()
						.longValue() == rc.getTbSpecialExemptType()
						.getSpecialExemptTypeId().longValue()) {
					if(officeId.longValue() != rc.getSpecialExemptOfficeId().longValue()
							&& rc.getOfficeName().equals(officeName.toUpperCase())){
						setModalMessage("Ya hay una dependencia con ese nombre, Verifique.");
						setShowModal(true);
						return null;
					}
				}
			}
			if (office.editTbSpecialExemptOffice(officeName, SessionUtil.ip(),
					SessionUtil.sessionUser().getUserId(), authoreizedBy,
					officeAddress, officeEmail, officeFax, officePhone,
					officeId)) {
				setModalMessage("Transacción Exitosa.");
				setOfficeList(null);
			} else {
				setModalMessage("Error en Transacción.");
			}
			setShowModal(true);
		}
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
	 * @param exemptTypeId the exemptTypeId to set
	 */
	public void setExemptTypeId(Long exemptTypeId) {
		this.exemptTypeId = exemptTypeId;
	}

	/**
	 * @return the exemptTypeId
	 */
	public Long getExemptTypeId() {
		return exemptTypeId;
	}

	/**
	 * @param officeList the officeList to set
	 */
	public void setOfficeList(List<TbSpecialExemptOffice> officeList) {
		this.officeList = officeList;
	}

	/**
	 * @return the officeList
	 */
	public List<TbSpecialExemptOffice> getOfficeList() {
		if(officeList == null) {
			officeList = new ArrayList<TbSpecialExemptOffice>();
			officeList = office.getEspecialExemptOffice();
		}
		return officeList;
	}

	/**
	 * @param officeId the officeId to set
	 */
	public void setOfficeId(Long officeId) {
		this.officeId = officeId;
	}

	/**
	 * @return the officeId
	 */
	public Long getOfficeId() {
		return officeId;
	}

	/**
	 * @param officeName the officeName to set
	 */
	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	/**
	 * @return the officeName
	 */
	public String getOfficeName() {
		return officeName;
	}

	/**
	 * @param authoreizedBy the authoreizedBy to set
	 */
	public void setAuthoreizedBy(String authoreizedBy) {
		this.authoreizedBy = authoreizedBy;
	}

	/**
	 * @return the authoreizedBy
	 */
	public String getAuthoreizedBy() {
		return authoreizedBy;
	}

	/**
	 * @param officeAddress the officeAddress to set
	 */
	public void setOfficeAddress(String officeAddress) {
		this.officeAddress = officeAddress;
	}

	/**
	 * @return the officeAddress
	 */
	public String getOfficeAddress() {
		return officeAddress;
	}

	/**
	 * @param officeFax the officeFax to set
	 */
	public void setOfficeFax(String officeFax) {
		this.officeFax = officeFax;
	}

	/**
	 * @return the officeFax
	 */
	public String getOfficeFax() {
		return officeFax;
	}

	/**
	 * @param officePhone the officePhone to set
	 */
	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}

	/**
	 * @return the officePhone
	 */
	public String getOfficePhone() {
		return officePhone;
	}

	/**
	 * @param officeEmail the officeEmail to set
	 */
	public void setOfficeEmail(String officeEmail) {
		this.officeEmail = officeEmail;
	}

	/**
	 * @return the officeEmail
	 */
	public String getOfficeEmail() {
		return officeEmail;
	}

	/**
	 * @param exemptTypes the exemptTypes to set
	 */
	public void setExemptTypes(List<SelectItem> exemptTypes) {
		this.exemptTypes = exemptTypes;
	}

	/**
	 * @return the exemptTypes
	 */
	public List<SelectItem> getExemptTypes() {
		if(exemptTypes ==  null){
			exemptTypes = new ArrayList<SelectItem>();
			exemptTypes.add(new SelectItem(-1L, "--Seleccione uno--"));
			for (TbSpecialExemptType st : types.getExemptTypes()) {
				exemptTypes.add(new SelectItem(st.getSpecialExemptTypeId(), st
						.getSpecialExemptTypeName()));
			}
		}
		return exemptTypes;
	}

	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param showError the showError to set
	 */
	public void setShowError(boolean showError) {
		this.showError = showError;
	}

	/**
	 * @return the showError
	 */
	public boolean isShowError() {
		return showError;
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
}