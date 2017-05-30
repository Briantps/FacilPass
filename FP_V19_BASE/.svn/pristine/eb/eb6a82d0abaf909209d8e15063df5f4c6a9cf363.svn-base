package dictionary;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import sessionVar.SessionUtil;

import ejb.Dictionary;

import jpa.TbPasswordDictionary;
import jpa.TbSystemUser;

public class PasswordDictionary implements Serializable{

	private static final long serialVersionUID = 1L;

	private String newPassword;
	
	private String password;
	
	private Date dateCreation;
	
	private String userCode;
	
	private String message;
	
	private boolean showModal;
	
	private List<TbPasswordDictionary> lista;
	
	private Long passwordId;
	
	private boolean showUpdate;
	
	@EJB(mappedName="ejb/Dictionary")
	private Dictionary dictionary;
	
	private String newPassword2;
	
	private boolean showConfirm;
	
	private String corfirmMessage;
	
	public PasswordDictionary(){
		super();
	}
	
	@PostConstruct
	public void init(){
		this.getLista();
		System.out.println("Entre a metodo init");
	}

	/**
	 * @param newPassword the newPassword to set
	 */
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}


	/**
	 * @return the newPassword
	 */
	public String getNewPassword() {
		return newPassword;
	}


	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}


	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}


	/**
	 * @param dateCreation the dateCreation to set
	 */
	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}


	/**
	 * @return the dateCreation
	 */
	public Date getDateCreation() {
		return dateCreation;
	}


	/**
	 * @param userCode the userCode to set
	 */
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}


	/**
	 * @return the userCode
	 */
	public String getUserCode() {
		return userCode;
	}


	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}


	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
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
	 * @param lista the lista to set
	 */
	public void setLista(List<TbPasswordDictionary> lista) {
		this.lista = lista;
	}


	/**
	 * @return the lista
	 */
	public List<TbPasswordDictionary> getLista() {
		lista= new ArrayList<TbPasswordDictionary>();
		lista=dictionary.getPasswords();
		return lista;
	}
	
	/**
	 * @param passwordId the passwordId to set
	 */
	public void setPasswordId(Long passwordId) {
		this.passwordId = passwordId;
	}

	/**
	 * @return the passwordId
	 */
	public Long getPasswordId() {
		return passwordId;
	}
	
	/**
	 * @param showUpdate the showUpdate to set
	 */
	public void setShowUpdate(boolean showUpdate) {
		this.showUpdate = showUpdate;
	}

	/**
	 * @return the showUpdate
	 */
	public boolean isShowUpdate() {
		return showUpdate;
	}
	

	/**
	 * @param newPassword2 the newPassword2 to set
	 */
	public void setNewPassword2(String newPassword2) {
		this.newPassword2 = newPassword2;
	}

	/**
	 * @return the newPassword2
	 */
	public String getNewPassword2() {
		return newPassword2;
	}
	
	public String save(){
		TbSystemUser user = SessionUtil.sessionUser();
		if(newPassword!=null){
			if(!newPassword.equals("")){
				String varP= newPassword;
				String message="OK";
				if(!varP.trim().isEmpty()){
					message=dictionary.validateSize(newPassword);
					if(message.equals("OK")){
						boolean res=dictionary.savePassword(newPassword, user);
						if(res){
							this.setMessage("Transacción Exitosa.");
						}
						else{
							this.setMessage("Error en transacción. Verifique que la contraseña ya exista en el diccionario");
						}
					}
					else{
						this.setMessage("Error en transacción." + message);
					}
				
				}
				else{
					this.setMessage("Error en transacción. Digite una contraseña");
				}
			
			}
			else{
				this.setMessage("El campo de contraseña no debe estar vacío.");
			}
		}
		else{
			this.setMessage("El campo de contraseña no debe estar vacío.");
		}
		this.setShowModal(true);
		return null;
	}
	
	public String update(){
		this.setShowUpdate(true);
		return null;
	}
	
	public String delete(){
		this.setShowConfirm(false);
		this.setCorfirmMessage("");
		if(passwordId!=null){
			boolean res=dictionary.deletePassword(passwordId);
			if(res){
				this.setMessage("Transacción Exitosa");
			}
			else{
				this.setMessage("Error: en la transacción");
			}
		}
		else{
			this.setMessage("Error: Debe seleccionar una contraseña para borrar.");
		}
		this.setShowModal(true);
		return null;
	}
	
	public String hideModal(){
		this.setNewPassword("");
		this.setMessage("");
		this.setShowModal(false);
		this.setShowUpdate(false);
		this.setNewPassword2("");
		this.setShowConfirm(false);
		this.setCorfirmMessage("");
		this.setNewPassword2("");
		this.setShowUpdate(false);
		return null;
	}
	
	public String hideModal2(){
		this.setShowUpdate(false);
		return null;
	}
	
	

    public String updatePassword(){
    	if(passwordId!=null && newPassword2!=null){
    		String varP= newPassword2;
    		String message="OK";
			if(!varP.trim().isEmpty()){
				message= dictionary.validateSize(newPassword2);
				if(message.equals("OK")){
					boolean res=dictionary.updatePassword(passwordId, newPassword2);
					System.out.println("res " +res);
					System.out.println("newPassword2 " +newPassword2);
		    		if(res){
		    			this.setMessage("Transacción Exitosa");
		    		}
		    		else{
		    			this.setMessage("Error en la transacción. Verifique que la nueva contraseña ya exista en el diccionario");
		    		}
				}
				else{
					this.setMessage("Error en la transacción." + message);
				}
			
			}else{
				this.setMessage("Error en transacción. Digite una contraseña");
			}
    	}else{
    		this.setMessage("Debe seleccionar una contraseña para actualizar y esta no debe estar vacía");
    	}
    	this.setShowUpdate(false);
    	this.setShowModal(true);
    	return null;
    }

	/**
	 * @param showConfirm the showConfirm to set
	 */
	public void setShowConfirm(boolean showConfirm) {
		this.showConfirm = showConfirm;
	}

	/**
	 * @return the showConfirm
	 */
	public boolean isShowConfirm() {
		return showConfirm;
	}

	/**
	 * @param corfirmMessage the corfirmMessage to set
	 */
	public void setCorfirmMessage(String corfirmMessage) {
		this.corfirmMessage = corfirmMessage;
	}

	/**
	 * @return the corfirmMessage
	 */
	public String getCorfirmMessage() {
		return corfirmMessage;
	}
	
	public void deleteConfirm(){
		this.setCorfirmMessage("¿Está seguro que desea eliminar esta contraseña del diccionario de contraseñas inválidas?");
		this.setShowConfirm(true);
	}

}
