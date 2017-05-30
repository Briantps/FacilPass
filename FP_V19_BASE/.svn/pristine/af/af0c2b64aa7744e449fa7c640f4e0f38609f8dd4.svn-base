/**
 * 
 */
package configSystem;

import java.io.Serializable;

import javax.ejb.EJB;

import sessionVar.SessionUtil;
import ejb.Contract;

/**
 * @author jromero
 *
 */
public class AdminPassCrlBean implements Serializable{
	private static final long serialVersionUID = 5371504689499648558L;

	// Attributes --------------------------------------------------------------------------
	
	@EJB(mappedName="ejb/Contract")
	private Contract contractEJB;
	
	@EJB(mappedName="ejb/User")
	private ejb.User userEjb;
	
	private Long singParamId;
	private String oldPass;
	private String newPass;
	private String conPass;
	
	private boolean showModal;
	private boolean showConfirmation;
	private boolean showModalValidate;
	private boolean refrescar=true;

	private String modalMessage;
	private String corfirmMessage;
	private String msg;
	private boolean create;
	private boolean firstTime;
	private boolean error;
	private boolean modificate;
	private boolean update;
	private boolean modalConfir;
	
	// Constructor ------------------------------------------------------------------------
	
	public AdminPassCrlBean() {
	}
	
	// Actions ---------------------------------------------------------------------------
	
	/**
	 * Save a password.
	 */
	public String savePass(){
		firstTime=contractEJB.isFirstPass();
		if(!firstTime){
			System.out.println("No es primera vez");
			if(oldPass.equals(null)||oldPass.equals("")){
				setModalMessage("La contraseña actual es requerida.");
				setShowModal(true);
			}else if(!contractEJB.isOldPass(oldPass)){
				setModalMessage("Por favor digite correctamente la contraseña actual.");
				setShowModal(true);
			}else if(newPass.equals(null)||newPass.equals("")){
				setModalMessage("La nueva contraseña es requerida.");
				setShowModal(true);
			}else if(conPass.equals(null)||conPass.equals("")){
				setModalMessage("La confirmación de la nueva contraseña es requerida.");
				setShowModal(true);
			}else if(newPass.trim().length()<3){
				setModalMessage("La nueva contraseña debe contener mínimo 3 caracteres.");
				setShowModal(true);
			}else if(conPass.trim().length()<3){
				setModalMessage("La confirmación de la nueva contraseña debe contener mínimo" +
						" 3 caracteres.");
				setShowModal(true);
			}else if(!newPass.equals(conPass)){
				setModalMessage("La nueva contraseña y la confirmación deben ser iguales.");
				setShowModal(true);
			}else{
				System.out.println("Pasó validación 1");
				setShowModal(false);
				setShowConfirmation(true);
				setModalMessage(null);
				setCorfirmMessage("¿Está seguro que quiere realizar el cambio de contraseña?.");
			}
		}else{
			System.out.println("Es primera vez");
			if(newPass.equals(null)||newPass.equals("")){
				setModalMessage("La nueva contraseña es requerida.");
				setShowModal(true);
			}else if(conPass.equals(null)||conPass.equals("")){
				setModalMessage("La confirmación de la nueva contraseña es requerida.");
				setShowModal(true);
			}else if(newPass.trim().length()<3){
				setModalMessage("La nueva contraseña debe contener mínimo 3 caracteres.");
				setShowModal(true);
			}else if(conPass.trim().length()<3){
				setModalMessage("La confirmación de la nueva contraseña debe contener mínimo" +
						" 3 caracteres.");
				setShowModal(true);
			}else if(!newPass.equals(conPass)){
				setModalMessage("La nueva contraseña y la confirmación deben ser iguales.");
				setShowModal(true);
			}else{
				System.out.println("Pasó validación 2");
				setShowModal(false);
				setShowConfirmation(true);
				setModalMessage(null);
				setCorfirmMessage("¿Está seguro que quiere realizar el cambio de contraseña?.");
			}
		}
		return null;
	}
	
	
	
	/**
	 * Clear variables.
	 */
	private void clearVar() {
		oldPass="";
		newPass="";
		conPass="";
	}

	/**
	 * Control modal.
	 */
	public String hideModal() {
		this.setShowModal(false);
		this.setShowModalValidate(false);
		this.setShowConfirmation(false);
		this.setModalMessage(null);
		this.setConPass("");
		this.setNewPass("");
		this.setOldPass("");
		singParamId=0L;
		return null;
	}
	
	public String changePass(){
		this.setShowConfirmation(false);
		System.out.println("[newPass]"+newPass);
		if(contractEJB.changePassCrl(newPass, SessionUtil.ip(),
				userEjb.getSystemMasterById(SessionUtil.sessionUser()
						.getUserId()))){
			setModalMessage("Se ha realizado la modificación de la contraseña con éxito.");
		}else{
			setModalMessage("Error al modificar la contraseña, " +
					"comuníquese con el Administrador.");
		}
		clearVar();
		setShowModal(false);
		setShowModalValidate(true);
		setShowConfirmation(false);
		return null;
	}

	// Getters and Setters -------------------------------------------------------------

	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param create the create to set
	 */
	public void setCreate(boolean create) {
		this.create = create;
	}

	/**
	 * @return the create
	 */
	public boolean isCreate() {
		return create;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(boolean error) {
		this.error = error;
	}

	/**
	 * @return the error
	 */
	public boolean isError() {
		return error;
	}

	/**
	 * @param modificate the modificate to set
	 */
	public void setModificate(boolean modificate) {
		this.modificate = modificate;
	}

	/**
	 * @return the modificate
	 */
	public boolean isModificate() {
		return modificate;
	}

	/**
	 * @param update the update to set
	 */
	public void setUpdate(boolean update) {
		this.update = update;
	}

	/**
	 * @return the update
	 */
	public boolean isUpdate() {
		return update;
	}

	public Long getSingParamId() {
		return singParamId;
	}

	public void setSingParamId(Long singParamId) {
		this.singParamId = singParamId;
	}

	public String getOldPass() {
		return oldPass;
	}

	public void setOldPass(String oldPass) {
		this.oldPass = oldPass;
	}

	public String getNewPass() {
		return newPass;
	}

	public void setNewPass(String newPass) {
		this.newPass = newPass;
	}

	public String getConPass() {
		return conPass;
	}

	public void setConPass(String conPass) {
		this.conPass = conPass;
	}

	public boolean isFirstTime() {
		firstTime=contractEJB.isFirstPass();
		return firstTime;
	}

	public void setFirstTime(boolean firstTime) {
		this.firstTime = firstTime;
	}

	public boolean isModalConfir() {
		return modalConfir;
	}

	public void setModalConfir(boolean modalConfir) {
		this.modalConfir = modalConfir;
	}

	public boolean isShowModal() {
		return showModal;
	}

	public void setShowModal(boolean showModal) {
		this.showModal = showModal;
	}

	public boolean isShowConfirmation() {
		return showConfirmation;
	}

	public void setShowConfirmation(boolean showConfirmation) {
		this.showConfirmation = showConfirmation;
	}

	public boolean isShowModalValidate() {
		return showModalValidate;
	}

	public void setShowModalValidate(boolean showModalValidate) {
		this.showModalValidate = showModalValidate;
	}

	public boolean isRefrescar() {
		return refrescar;
	}

	public void setRefrescar(boolean refrescar) {
		this.refrescar = refrescar;
	}

	public String getModalMessage() {
		return modalMessage;
	}

	public void setModalMessage(String modalMessage) {
		this.modalMessage = modalMessage;
	}

	public String getCorfirmMessage() {
		return corfirmMessage;
	}

	public void setCorfirmMessage(String corfirmMessage) {
		this.corfirmMessage = corfirmMessage;
	}
	
	
}