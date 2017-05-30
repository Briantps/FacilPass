package mBeans;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.ejb.EJB;

import security.UserLogged;
import sessionVar.SessionUtil;
import util.Channel;
import validator.Validation;
import ejb.ChannelI;
import ejb.User;

/**
 * Clase que realiza la validacion para la administración de canales
 * 
 * @author ARivera, psanchez
 */

public class AdminChannelBean implements Serializable {
	private static final long serialVersionUID = 199399153082288754L;

	@EJB(mappedName = "ejb/User")
	private User userEJB;

	@EJB(mappedName = "ejb/ChannelI")
	private ChannelI channelEJB;

	private List<Channel> listChannel;
	private Long channelId;
	private String codeChannel;
	private String nameChannel;
	private String stateChanel;
	private String message;
	private String descriptionChannel;
    private String textRechargeValue;
	private boolean permissionUpdate;
	private boolean permissionActiveInactive;
	private boolean permissionCreate;
	private boolean permissionDelete;
	private boolean permActiveRecharge;
	private boolean showModalValidate;
	private boolean viewFormCreate;
	private boolean showUpdateChannel;
	private boolean showCreateChannel;
	private boolean showModalConfirmationRecharge;
	private boolean showModalConfirmationCreate;
	private boolean showModalConfirmationDelete;
	private boolean showModalConfirmationUpdate;
	private boolean showModalConfirmationUpdateEC;
	private boolean disableField=false;
	private boolean active=true;
	private int numero=0;
	private Long rechargeValue;	

	private UserLogged usrs;

	public AdminChannelBean() {
		usrs = (UserLogged) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
	}

	@PostConstruct
	public void init() {
		permissionCreate = userEJB.getPermission(usrs.getUserId(),"#{adminChannel.create}");

		permissionUpdate = userEJB.getPermission(usrs.getUserId(),"#{adminChannel.update}");

		permissionActiveInactive = userEJB.getPermission(usrs.getUserId(),"#{adminChannel.ActiveInactive}");

		permissionDelete = userEJB.getPermission(usrs.getUserId(),"#{adminChannel.delete}");
		
		permActiveRecharge = userEJB.getPermission(usrs.getUserId(),"adminChannelMinimalAssignment");
		
		getListChannel();
	}


	public boolean validateChanel() {
		if (codeChannel.trim().length() == 0) {
			setMessage("El campo Código es requerido.");
			return false;
		} 
		else if (nameChannel.trim().length() == 0) {
			setMessage("El campo Nombre es requerido.");
			return false;
		}
		else if (active==true && textRechargeValue.trim().length()==0) {
			setMessage("El campo Recarga/asignación mínima es requerido.");
			return false;
		}
		else if (active==true && textRechargeValue.trim().length()==0) {
			setMessage("El campo Recarga/asignación mínima es requerido.");
			return false;
		}
		else if (descriptionChannel.trim().length() == 0) {
			setMessage("El campo Descripción es requerido.");
			return false;
		}
		else if (!Validation.isNumeric(codeChannel.toString())) {
			setMessage("El campo Código contiene caracteres inválidos.");
			return false;
		}
		else if (!Validation.nameAgreement(nameChannel)) {
			setMessage("El campo Nombre contiene caracteres inválidos.");
			return false;
	    }
		else if (!nameChannel.matches("([a-z]|[A-Z]|[áéíóú]|[ÁÉÍÓÚ]|[&/]|[_-]|[.]|[ñÑ]|\\s)+")) {
			setMessage("El nombre Canal tiene caracteres inválidos.");
			return false;
	    }
		else if (active==true && !textRechargeValue.matches("([0-9.$])+")) {
			setMessage("El campo Recarga/asignación contiene caracteres inválidos.");
			return false;
		}
		else if(active==true && textRechargeValue!=""){
			if(Long.parseLong(textRechargeValue.replace(".", "").replace(",", "").replace("$", ""))<0){
				setMessage("El campo Recarga/asignación debe ser mayor o igual a cero $0 pesos.");
				return false;
			}
		}
		else if (!descriptionChannel
			.matches("([a-z]|[A-Z]|[áéíóú]|[ÁÉÍÓÚ]|[0-9]|[&/]|[_-]|[.,:;]|[()]|[ñÑ]|\\s)+")) {
			setMessage("El campo Descripción contiene caracteres inválidos.");
			return false;
	    }
		else if (codeChannel.trim().length()>4) {
			setMessage("El campo Código no debe contener más de 4 caracteres.");
			return false;
		} 
		else if (nameChannel.trim().length() >100) {
			setMessage("El campo Nombre no debe contener más de 100 caracteres.");
			return false;
		} 
		else if (descriptionChannel.trim().length()>200) {
			setMessage("El campo Descripción no debe contener más de 200 caracteres.");
			return false;
		} 
		return true;
	}
	
	public void confirmationSave() {
		if(validateChanel()==false){
			setShowModalValidate(true);
        }else if(channelEJB.existChannel(channelId, Long.valueOf(codeChannel), 1) != -1L) {
			Long existChannel = channelEJB.existChannel(channelId, Long.valueOf(codeChannel), 1);
			if (existChannel ==1) {
				setMessage("El código del canal ya existe.");
				setShowModalValidate(true);
	        }else {	        	
	        	setMessage("Error al validar el canal.");
				setShowModalValidate(true);
	        }
	    }else {
			setMessage("¿Está seguro de crear el canal "+ nameChannel+" con el código "+codeChannel+"?");
			setShowModalConfirmationCreate(true);
		}
	}

	public void createChannel() {
		try {
			Long activeRecharge = null;
			if(active==false){
				activeRecharge=0L;
				rechargeValue = null;
			}
			if(active==true){
				activeRecharge=1L;
				rechargeValue = Long.parseLong(textRechargeValue.replace(".", "").replace(",", "").replace("$", ""));
			}
			if (channelEJB.createChannel(Long.valueOf(codeChannel), nameChannel, descriptionChannel, activeRecharge, 
					rechargeValue, SessionUtil.ip(), SessionUtil.sessionUser().getUserId())){
				hideModalPanel();
				setMessage("Se ha creado el canal exitosamente.");
			} else {
				setMessage("Se ha presentado un error al momento de modificar el canal, por favor intente nuevamente.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			setMessage("Se ha presentado un error al momento de registrar el canal, por favor intente nuevamente.");
		}
		setShowModalValidate(true);	
	}
	
	
	public void confirmationUpdate() {
		if(validateChanel()==false){
			setShowModalValidate(true);
        }else if(channelEJB.existChannel(channelId, Long.valueOf(codeChannel), 2) != -1L) {
			Long existChannel = channelEJB.existChannel(channelId, Long.valueOf(codeChannel), 2);
			if (existChannel ==1) {
				setMessage("El código del canal ya existe.");
				setShowModalValidate(true);
	        }else {	        	
	        	setMessage("Error al validar el canal.");
				setShowModalValidate(true);
	        }	
        }else {
			setMessage("¿Está seguro de modificar el canal "+ nameChannel+" con el código "+codeChannel+"?");
			setShowModalConfirmationUpdate(true);
		}
	}

	public void updateChannel() {
		try {
			Long activeRecharge = null;
			if(active==false){
				activeRecharge=0L;
				rechargeValue = null;
			}
			if(active==true){
				activeRecharge=1L;
				rechargeValue = Long.parseLong(textRechargeValue.replace(".", "").replace(",", "").replace("$", ""));
			}
			if (channelEJB.updateChannel(channelId,Long.valueOf(codeChannel), nameChannel, descriptionChannel, 
					activeRecharge, rechargeValue, SessionUtil.ip(),SessionUtil.sessionUser().getUserId())){
				hideModalPanel();
			    setMessage("Se ha modificado el canal exitosamente.");
			} else {
				setMessage("Se ha presentado un error al momento de modificar el canal, por favor intente nuevamente.");
			}
		}  catch (Exception e) {
			e.printStackTrace();
			setMessage("Se ha presentado un error al momento de modificar el canal, por favor intente nuevamente.");
	   }
		setShowModalValidate(true);	
	}

	public void confirmaActiveInactive() {
		hideModalPanel();
		if (getStateChanel().equals("Activar")) {
			setMessage("¿Está seguro de " + getStateChanel() + " el canal "+ nameChannel + " con el código "+codeChannel+"?");
		} else {
			setMessage("¿Está seguro de "
					+ getStateChanel()
					+ " el canal "
					+ nameChannel
					+ "con el código "+codeChannel+"?. Recuerde que al realizar esta operación también se inactivarán las relaciones.");
		}
		setShowModalConfirmationUpdateEC(true);
	}
	
	

	public void incativeActiveChannel() {
		hideModalPanel();
		try {
			if(channelEJB.inactiveActiveChannel(channelId,Long.valueOf(codeChannel), 
					SessionUtil.ip(), SessionUtil.sessionUser().getUserId())){
				setMessage("El estado el canal se ha modificado exitosamente.");
			} else {
				setMessage("Se ha presentado un error al momento de modificar el estado del canal, por favor intente nuevamente.");
		    }
			setShowModalValidate(true);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			setMessage("Se ha presentado un error al momento de modificar el estado del canal, por favor intente nuevamente.");
		} catch (SQLException e) {
			e.printStackTrace();
			setMessage("Se ha presentado un error al momento de modificar el estado del canal, por favor intente nuevamente.");
		}
	}
	

	public void ConfirmationDeleteChannel() {
		hideModalPanel();
		setShowModalConfirmationDelete(true);
		setMessage("¿Está seguro de eliminar el canal " +nameChannel+ " con el código " + codeChannel + "?");
	}
	
	/**
	 * Método elimina el canal
	 * @author psanchez
	 */
	  public void deleteChannel() {
		hideModalPanel();
		try {
			if(channelEJB.deleteChannel(channelId, Long.valueOf(codeChannel), SessionUtil.ip(), SessionUtil.sessionUser().getUserId())){
				setMessage("Se ha eliminado el canal exitosamente.");
			} else {
				setMessage("Se ha presentado un error al momento de eliminar el canal, por favor intente nuevamente.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			setMessage("Se ha presentado un error al momento de eliminar el canal, por favor intente nuevamente.");
		}
		setShowModalValidate(true);
	}
	
	/**
	 * Método valida la asignación mínima
	 * @author psanchez
	 */
	public void validateMinimumAssignment(ValueChangeEvent event) {
		setNumero(0);
		try {
			if((Boolean) event.getNewValue()){
				setDisableField(false);
				setActive(true);
				setNumero(1);
			}else{
				setMessage("¿Está seguro que el Canal no valide la Recarga/asignación mínima?");
				setShowModalConfirmationRecharge(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void hideModal(){
		hideModal1();
		setActive(true);
        setDisableField(false);
	}
	
	public void hideModal1(){
		setShowModalValidate(false);
		setShowModalConfirmationRecharge(false);
		setShowModalConfirmationCreate(false);
		setShowModalConfirmationDelete(false);
		setShowModalConfirmationUpdate(false);
		setShowModalConfirmationUpdateEC(false);
		setMessage(null);
	}
	
	public void hideModal2(){
		setActive(false);
        setDisableField(true);
		setTextRechargeValue("");		
		setShowModalConfirmationRecharge(false);
	}
	
	public void hideModalPanel(){
		setViewFormCreate(false);
		setShowUpdateChannel(false);
		setShowCreateChannel(false);
	}

	public void showViewCreate() {
		setViewFormCreate(true);
		setShowCreateChannel(true);
		setShowUpdateChannel(false);
		setCodeChannel("");
		setNameChannel("");
		setActive(true);
		setDisableField(false);
		setTextRechargeValue("");
		setDescriptionChannel("");
		getListChannel();
	}

	/**
	 * Método carga formulario con datos a modificar
	 * @author psanchez
	 */
	public void panelUpdateChannel(){
		setShowUpdateChannel(true);
		setShowCreateChannel(false);
		setNumero(0);
		for(Channel chanel : listChannel){
			if(codeChannel==chanel.getCodeChannel()){
				Long activeRecharge = chanel.getActiveRecharge();
				if(activeRecharge == 1L){
					setActive(true);
					setDisableField(false);
				}else{
					setActive(false);
					setDisableField(true);
				}
				break;
			}
		}
	}

	public User getUserEJB() {
		return userEJB;
	}

	public void setUserEJB(User userEJB) {
		this.userEJB = userEJB;
	}

	public ChannelI getChannelEJB() {
		return channelEJB;
	}

	public void setChannelEJB(ChannelI channelEJB) {
		this.channelEJB = channelEJB;
	}

	public List<Channel> getListChannel() {
		if(listChannel == null) {
			listChannel = new ArrayList<Channel>();
		}else{
			listChannel.clear();
		}
		listChannel = channelEJB.getAllChannel();
		return listChannel;
	}

	public void setListChannel(ArrayList<Channel> listChannel) {
		this.listChannel = listChannel;
	}

	public boolean isPermissionUpdate() {
		return permissionUpdate;
	}

	public void setPermissionUpdate(boolean permissionUpdate) {
		this.permissionUpdate = permissionUpdate;
	}

	public boolean isPermissionActiveInactive() {
		return permissionActiveInactive;
	}

	public void setPermissionActiveInactive(boolean permissionActiveInactive) {
		this.permissionActiveInactive = permissionActiveInactive;
	}

	public boolean isPermissionCreate() {
		return permissionCreate;
	}

	public void setPermissionCreate(boolean permissionCreate) {
		this.permissionCreate = permissionCreate;
	}

	public String getCodeChannel() {
		return codeChannel;
	}

	public void setCodeChannel(String codeChannel) {
		this.codeChannel = codeChannel;
	}

	public String getNameChannel() {
		return nameChannel;
	}

	public void setNameChannel(String nameChannel) {
		this.nameChannel = nameChannel;
	}

	public String getDescriptionChannel() {
		return descriptionChannel;
	}

	public void setDescriptionChannel(String descriptionChannel) {
		this.descriptionChannel = descriptionChannel;
	}

	public boolean isViewFormCreate() {
		return viewFormCreate;
	}

	public void setViewFormCreate(boolean viewFormCreate) {
		this.viewFormCreate = viewFormCreate;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String menssage) {
		this.message = menssage;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public boolean isPermissionDelete() {
		return permissionDelete;
	}

	public void setPermissionDelete(boolean permissionDelete) {
		this.permissionDelete = permissionDelete;
	}

	public String getStateChanel() {
		return stateChanel;
	}

	public void setStateChanel(String stateChanel) {
		this.stateChanel = stateChanel;
	}

	public void setDisableField(boolean disableField) {
		this.disableField = disableField;
	}

	public boolean isDisableField() {
		return disableField;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isActive() {
		return active;
	}

	public void setPermActiveRecharge(boolean permActiveRecharge) {
		this.permActiveRecharge = permActiveRecharge;
	}

	public boolean isPermActiveRecharge() {
		return permActiveRecharge;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public int getNumero() {
		return numero;
	}

	public void setRechargeValue(Long rechargeValue) {
		this.rechargeValue = rechargeValue;
	}

	public Long getRechargeValue() {
		return rechargeValue;
	}

	public void setTextRechargeValue(String textRechargeValue) {
		this.textRechargeValue = textRechargeValue;
	}

	public String getTextRechargeValue() {
		return textRechargeValue;
	}
	

	public void setShowModalConfirmationRecharge(boolean showModalConfirmationRecharge) {
		this.showModalConfirmationRecharge = showModalConfirmationRecharge;
	}

	public boolean isShowModalConfirmationRecharge() {
		return showModalConfirmationRecharge;
	}

	public void setShowModalConfirmationCreate(boolean showModalConfirmationCreate) {
		this.showModalConfirmationCreate = showModalConfirmationCreate;
	}

	public boolean isShowModalConfirmationCreate() {
		return showModalConfirmationCreate;
	}

	public void setShowModalConfirmationDelete(boolean showModalConfirmationDelete) {
		this.showModalConfirmationDelete = showModalConfirmationDelete;
	}

	public boolean isShowModalConfirmationDelete() {
		return showModalConfirmationDelete;
	}

	public void setShowModalConfirmationUpdate(boolean showModalConfirmationUpdate) {
		this.showModalConfirmationUpdate = showModalConfirmationUpdate;
	}

	public boolean isShowModalConfirmationUpdate() {
		return showModalConfirmationUpdate;
	}

	public void setShowModalConfirmationUpdateEC(boolean showModalConfirmationUpdateEC) {
		this.showModalConfirmationUpdateEC = showModalConfirmationUpdateEC;
	}

	public boolean isShowModalConfirmationUpdateEC() {
		return showModalConfirmationUpdateEC;
	}

	public void setShowModalValidate(boolean showModalValidate) {
		this.showModalValidate = showModalValidate;
	}

	public boolean isShowModalValidate() {
		return showModalValidate;
	}

	public void setShowCreateChannel(boolean showCreateChannel) {
		this.showCreateChannel = showCreateChannel;
	}

	public boolean isShowCreateChannel() {
		if(permissionCreate==true && showUpdateChannel==false){
			return showCreateChannel=true;
		}
		return showCreateChannel;
	}

	public void setShowUpdateChannel(boolean showUpdateChannel) {
		this.showUpdateChannel = showUpdateChannel;
	}

	public boolean isShowUpdateChannel() {
		return showUpdateChannel;
	}

}