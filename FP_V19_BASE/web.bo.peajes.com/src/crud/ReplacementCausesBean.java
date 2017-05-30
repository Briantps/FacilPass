/**
 * 
 */
package crud;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import sessionVar.SessionUtil;

import ejb.crud.ReplacementCause;

import jpa.TbReplacementCause;

/**
 * Bean to Manage the administration of Replacement Causes.
 * @author tmolina
 *
 */
public class ReplacementCausesBean implements Serializable {
	private static final long serialVersionUID = 925480315162572942L;

	@EJB(mappedName="ejb/ReplacementCause")
	private ReplacementCause cause;
	
	// Attributes.
	
	private boolean showModal;
	
	private String modalMessage;
	
	private List<TbReplacementCause> causesList;
	
	private Long causeId;
	
	private boolean showInsert;
	
	private String causeName;
	
	private boolean showEdit;

	/**
	 * Constructor.
	 */
	public ReplacementCausesBean() {
	}
	
	// Actions ------------ //
	
	/**
	 * Init Add Replacement Cause.
	 */
	public String initAddCause(){
		setShowInsert(true);
		setShowEdit(false);
		setCauseName(null);
		return null;
	}
	
	/**
	 * Inserts a new Replacement Cause.
	 */
	public String addCause(){
		setShowInsert(false);
		if(causeName.equals(null) || causeName.equals("") || !causeName.matches(".*\\w.*"))
		{
			setModalMessage("El nombre de la Causa de Reposición es Requerido.");
		}
		else if(causeName.length() > 50)
		{
			setModalMessage("La longitud del nombre de la Causa de Reposición no es correcta. Recuerde que debe ser máximo 50 caracteres.");
		}
		else
		{		
			Long result = cause.insertCause(causeName, SessionUtil.ip(), SessionUtil.sessionUser().getUserId());
			if (result != null) {
				if(result != -1L){
					setModalMessage("La Causa de Reposición ha sido creada con éxito.");
					setCausesList(null);
				} else {
					setModalMessage("Ya hay una Causa de reposición con ese nombre. Verifique.");
				}
			} else {
				setModalMessage("Error en Transacción.");
			}
		}
		setShowModal(true);
		return null;
	}
	
	/**
	 * Init Edition of Replacement Cause.
	 */
	public String initEditCause(){
		setShowInsert(false);
		setShowEdit(true);
		for(TbReplacementCause rc : causesList){
			if(causeId.longValue() == rc.getReplacementCauseId().longValue()){
				causeName = rc.getReplacementCauseName();
				break;
			}
		}
		return null;
	}
	
	/**
	 * Edits a Replacement Cause.
	 */
	public String editCause(){
		setShowEdit(false);
		setShowInsert(false);
		if(causeName.equals(null) || causeName.equals("") || !causeName.matches(".*\\w.*"))
		{
			setModalMessage("El nombre de la Ubicación de Instalación es Requerido.");
		}
		else if(causeName.length() > 50)
		{
			setModalMessage("La longitud del nombre de la Ubicación de Instalación no es correcta. Recuerde que debe ser máximo 50 caracteres.");
		}
		else
		{		
			if(causeName!=null){
				for(TbReplacementCause rc : causesList){
					if(causeId.longValue() != rc.getReplacementCauseId().longValue()
							&& rc.getReplacementCauseName().equals(causeName.toUpperCase())){
						setModalMessage("Ya hay una causa de reposición con ese nombre, Verifique.");
						setShowModal(true);
						return null;
					}
				}
				if (cause.editCause(causeId, causeName, SessionUtil.ip(),
						SessionUtil.sessionUser().getUserId())) {
					setModalMessage("La Causa de Reposición ha sido actualizada con éxito.");
					setCausesList(null);
				} else {
					setModalMessage("Error en Transacción.");
				}
			}
		}
		setShowModal(true);
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
	 * @param causesList the causesList to set
	 */
	public void setCausesList(List<TbReplacementCause> causesList) {
		this.causesList = causesList;
	}

	/**
	 * @return the causesList
	 */
	public List<TbReplacementCause> getCausesList() {
		if(causesList == null){
			causesList = new ArrayList<TbReplacementCause>();
			causesList = cause.getReplacementCauses();
		}
		return causesList;
	}

	/**
	 * @param causeId the causeId to set
	 */
	public void setCauseId(Long causeId) {
		this.causeId = causeId;
	}

	/**
	 * @return the causeId
	 */
	public Long getCauseId() {
		return causeId;
	}

	/**
	 * @param causeName the causeName to set
	 */
	public void setCauseName(String causeName) {
		this.causeName = causeName;
	}

	/**
	 * @return the causeName
	 */
	public String getCauseName() {
		return causeName;
	}
}