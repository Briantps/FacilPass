package mBeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import jpa.TbSystemUser;
import jpa.TbUserData;

import security.UserLogged;
import sessionVar.SessionUtil;
import util.Cities;

/**
 * Bean to Manage the edit Data Client.
 * @author psanchez
 *
 */
public class EditDataClientBean implements Serializable {
	private static final long serialVersionUID = 731945118516042526L;
	
	@PersistenceContext(unitName = "bo") 
	EntityManager em;

	@EJB(mappedName="ejb/User")
	private ejb.User userEJB;
	
	// Attributes.
	private List<TbUserData> clientDataList;
	private Long dataId;
	
	private List<SelectItem> cities;
	private Long city;
	
	private String userPhone1="";
	private String userPhone2="";
	private String userAdress="";
	
	/** Control Modal **/
	private boolean showModal;
	private boolean showEdit;
	private boolean showConfirmation;
	private boolean showModalValidate;

	private String modalMessage;
	
	private UserLogged usrs;

	/**
	 * Constructor.
	 */
	public EditDataClientBean() {
		usrs = (UserLogged) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
	}
	
	@PostConstruct
	public void init(){
		this.getClientDataList();
	}

	/**
	 * Método encargado de mostrar la vista con los datos ingresados por el cliente en sesión(ciudad, dirección, celular, telefono opcional).
	 * @author psanchez
	 */
	public void initEditDataClient(){
		this.setShowEdit(true);
		for(TbUserData ud: clientDataList){
			if(dataId.longValue() ==ud.getUserDataId()){
				userAdress=ud.getUserDataAddress();
				userPhone1=ud.getUserDataPhone();
				userPhone2=ud.getUserDataOptionalPhone();
				break;
			}
		}
	}
   
	
	/**
	 * Método encargado de validar los datos ingresados por el cliente en sesión(ciudad, dirección, celular, telefono opcional).
	 * @author psanchez
	 */
	public void editDataClient(){
		this.setShowModal(false);
		//this.setShowEdit(false);
		try{
			Boolean wrongPhone=false;
			Boolean wrongPhone2=false;
	            if(userPhone1!="" && (!userPhone1.matches("([0-9])+"))){
					this.setModalMessage("El campo Celular tiene caracteres inválidos. Verifique.");
				}else if(userPhone2!="" && (!userPhone2.matches("([0-9]|[a-z]|[A-Z]|\\s)+"))){
					this.setModalMessage("El campo Teléfono Opcional tiene caracteres inválidos. Verifique.");
				}
				try {
					int phoneUser = Integer.parseInt(userPhone1);
					if(phoneUser<=10){
						wrongPhone=true;
					}
				} catch (Exception e) {
				}
				
				if(!"".equals(userPhone2)){
					try {
						int phoneUser2 = Integer.parseInt(userPhone2);
						if(phoneUser2<=10){
							wrongPhone2=true;
						}
					} catch (Exception e) {
					}
				}
				 if((userPhone1.equals(null) || userPhone1.equals("")) && (userAdress.equals(null) || userAdress.equals(""))){
					setModalMessage("Ingrese datos en campos correspondientes.");
				 }else if(userPhone1.equals(null) || userPhone1.equals("")){
					setModalMessage("El campo Celular es requerido.");
				 }else if(userPhone1.length() < 7 || userPhone1.length() > 30){
					setModalMessage("La longitud del campo Celular no es correcta. Recuerde que debe estar entre 7 y 30 caracteres.");	
				 }else if(wrongPhone){
			 		setModalMessage("El número "+userPhone1+" no es un Celular válido. Verifique.");
				 }else if(userAdress.equals(null) || userAdress.equals("")){	
					setModalMessage("El campo Dirección es requerido.");
				 }else if(userAdress.length() < 7 || userAdress.length() > 50){
					setModalMessage("La longitud del campo Dirección no es correcta. Recuerde que debe estar entre 7 y 50 caracteres.");
				 }else if(userPhone2.length() > 0 && (userPhone2.length() < 7 || userPhone2.length() > 30)){
					setModalMessage("La longitud del campo Teléfono Opcional no es correcta. Recuerde que debe estar entre 7 y 30 caracteres.");
				 }else if(wrongPhone2){
					setModalMessage("El número "+userPhone2+" no es un Teléfono Opcional válido. Verifique.");
			     }else {
				    setShowModal(false);
					setShowConfirmation(true);
					setModalMessage(null);
					setModalMessage("¿Está seguro de realizar cambios?");
	 			}
		 }catch(Exception ex){
			 setModalMessage("Error al Actualizar Información del cliente.");
			 setShowModal(true);
			 ex.printStackTrace();
		 }
		setShowModal(true);
	}
	
	/**
	 * Método encargado de registrar los datos ingresados por el cliente en sesión(ciudad, dirección, celular, telefono opcional).
	 * @author psanchez
	 */	
	public void saveDataClient() {
		this.setShowConfirmation(false);
		this.setShowModal(false);
		if(userEJB.editDataClient(userEJB.getSystemMasterById(usrs.getUserId()), dataId, city, userAdress, userPhone1, 
				userPhone2, SessionUtil.ip(), SessionUtil.sessionUser().getUserId())==true){
			this.setModalMessage("Los datos del cliente han sido modificados con éxito.");
		} else {
			this.setModalMessage("Error en Transacción");
		}
		this.setShowModalValidate(true);
	}

	
	/**
	 * Hides modal windows.
	 */
	public void hideModal(){
		this.setShowEdit(true);
		this.setShowModal(false);
		this.setShowModalValidate(false);
		this.setShowConfirmation(false);
		this.setModalMessage(null);
	}
	
	public void hideModalValidate() {
		this.setShowModal(false);
		this.setShowModalValidate(false);
		this.setShowConfirmation(false);
		this.setModalMessage(null);
	}
	
	
	public void hideModal2() {
		this.setShowEdit(false);
		this.setShowModal(false);
		this.setShowModalValidate(false);
		this.setShowConfirmation(false);
	}
	

   /**
    * Getters and Setters 
    */   

	public void setShowModal(boolean showModal) {
		this.showModal = showModal;
	}

	public boolean isShowModal() {
		return showModal;
	}

	public void setModalMessage(String modalMessage) {
		this.modalMessage = modalMessage;
	}

	public String getModalMessage() {
		return modalMessage;
	}
	
	public void setShowEdit(boolean showEdit) {
		this.showEdit = showEdit;
	}

	public boolean isShowEdit() {
		return showEdit;
	}
	
	public void setShowModalValidate(boolean showModalValidate) {
		this.showModalValidate = showModalValidate;
	}

	public boolean isShowModalValidate() {
		return showModalValidate;
	}

	public void setShowConfirmation(boolean showConfirmation) {
		this.showConfirmation = showConfirmation;
	}

	public boolean isShowConfirmation() {
		return showConfirmation;
	}

	public Long getDataId() {
		return dataId;
	}

	public void setDataId(Long dataId) {
		this.dataId = dataId;
	}
	
	public void setClientDataList(List<TbUserData> clientDataList) {
		this.clientDataList = clientDataList;
	}

	/**
	 * Método encargado de listar datos del cliente en sesión(ciudad, dirección, celular, telefono opcional).
	 * @author psanchez
	 */	
	public List<TbUserData> getClientDataList() {
		if(clientDataList == null) {
			clientDataList = new ArrayList<TbUserData>();
		}else{
			clientDataList.clear();
		}
		clientDataList = userEJB.getClientData(userEJB.getSystemMasterById(usrs.getUserId()));
	  return clientDataList;		
    }
		
	public void setCities(List<SelectItem> cities) {
		this.cities = cities;
	}

	/**
	 * Método encargado de listar municipio-departamento
	 * @author psanchez
	 */	
	public List<SelectItem> getCities() {
		TbSystemUser systemUser = em.find(TbSystemUser.class, userEJB.getSystemMasterById(usrs.getUserId()));
		cities = new ArrayList<SelectItem>();
		List<Cities> c = userEJB.getCities();
		String city;
		for(Cities ci : c){
			city=ci.getCity() + " - "  + "(" + ci.getDepartment().substring(0,4) + ")";
			if(ci.getCityCode().equals("0")){
				if(systemUser.getTbCodeType().getCodeTypeId().equals(2L)){
					cities.add(new SelectItem(ci.getCityCode(),city));
				}
			}else{
				cities.add(new SelectItem(ci.getCityCode(),city));
			}
		}
		return cities;
	}
	
	public void setCity(Long city) {
		this.city = city;
	}

	public Long getCity() {
		return city;
	}
	
	public String getUserPhone1() {
		return userPhone1;
	}

	public void setUserPhone1(String userPhone1) {
		this.userPhone1 = userPhone1;
	}

	public String getUserPhone2() {
		return userPhone2;
	}

	public void setUserPhone2(String userPhone2) {
		this.userPhone2 = userPhone2;
	}

	public String getUserAdress() {
		return userAdress;
	}

	public void setUserAdress(String userAdress) {
		this.userAdress = userAdress;
	}

}