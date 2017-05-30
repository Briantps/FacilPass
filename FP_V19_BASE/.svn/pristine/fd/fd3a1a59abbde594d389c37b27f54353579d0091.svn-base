package process.device;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import sessionVar.SessionUtil;
import util.AllActiveDispositive;

import ejb.Category;
import ejb.EnrollDevice;
import ejb.User;
import jpa.TbCategory;
import jpa.TbCodeType;


/**
 * Bean to manage Active device.
 * @author psanchez
 *
 */
public class ActiveDeviceAdminBean implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@EJB(mappedName = "ejb/Category")
	private Category categoryEJB;
	
	@EJB(mappedName="ejb/EnrollDevice")
	private EnrollDevice enrollDeviceEJB;
	
	@EJB(mappedName="ejb/User")
	private User userEJB;
	
	private boolean showModal;
	private boolean showModalError;
	private boolean showModalErrorObservation;
	private boolean showConfirmation;
	private boolean showConfirmationAll;
	private boolean showObservation;
	private boolean showInfo=false;
	
	private String message;
	private String corfirmMessage;
	
	/* --Category --*/
	private Long category;
	private List<SelectItem> categoriesList;
	
	private List<AllActiveDispositive> listActiveDispositive;
	private List<AllActiveDispositive> listaTemp;
	
	private List<SelectItem> codeTypesList;
	private long codeType;
	
	private String numberDoc;
	private String userName;
	private String secondName;
	private String userEmail;
	private String plate;
	private String aditional1;
	private String aditional2;
	private String aditional3;
	private String account;
	private String deviceCode;
	private String deviceFacial;
	private String observation;
	

	public ActiveDeviceAdminBean(){
		super();
	}

	public void searchFilter(){
		this.showModal = false;
		this.showModalError = false;
		this.showConfirmation = false;
		this.showConfirmationAll = false;
		this.showObservation = false;
		this.setShowInfo(false);
		if(postValidationSearch()){
			if((codeType == -1L) && 
				(numberDoc.equals("")) && 
				(userName.equals("")) && 
				(secondName.equals("")) && 
				(userEmail.equals("")) && 
				(plate.equals("")) && 
				(category == -1L) && 
				(aditional1.equals("")) && 
				(aditional2.equals("")) && 
				(aditional3.equals("")) &&
				(account.equals(""))) { 
				this.message = "No existe datos para realizar la búsqueda.";
				this.showModal= true;
			}else{
				Long cont= enrollDeviceEJB.validateInfoClient(codeType, numberDoc, userName, secondName, userEmail, plate.toUpperCase(), 
						aditional1.toUpperCase(), aditional2.toUpperCase(), aditional3.toUpperCase(), category, account);
				if(cont==0){
					this.setShowInfo(false);
					this.setMessage("No se encontraron resultados para la búsqueda solicitada.");
					this.setShowModalError(true);
				}else if(cont==-1){
					this.setShowInfo(false);
					this.setMessage("Error en Transacción.");
					this.setShowModalError(true);
				}else if (cont>1){
					this.setShowInfo(false);
					this.setMessage("Hay más de un resultado en la consulta, por favor agregue otro campo en el filtro.");
					this.setShowModalError(true);
				}else {
					List<AllActiveDispositive> listfilter = this.getListActiveDispositive();
					listfilter = enrollDeviceEJB.getDeviceByFiltersAdmin(codeType, numberDoc, userName, secondName, userEmail, plate.toUpperCase(), 
							aditional1.toUpperCase(), aditional2.toUpperCase(), aditional3.toUpperCase(), category, account);
					if(listfilter.size()<=0){
						this.clearFilter();
						this.setShowInfo(false);
						this.message = "No se encontraron resultados para la búsqueda.";
						this.showModal = true;
						//this.listActiveDispositive.clear();
					}else {
						//this.listActiveDispositive = listfilter;
						this.setShowInfo(true);
						this.setShowModalError(false);
						this.setShowModalErrorObservation(false);
					}
				}
			}
		}/*else{
			this.clearFilter();
		}*/
	}
	

	public String enrolarConfirm(){
		this.setShowObservation(false);
		this.setShowConfirmation(false);
		this.setShowConfirmationAll(false);
		this.setShowModalError(false);
		this.setShowModalErrorObservation(false);
		int cant=0;
		try {
			for(AllActiveDispositive dis: listActiveDispositive){
				if(dis.getDeviceCodeU()!=null && dis.isActive()){
					observation = dis.getObservationU();
					cant++;	
				}
			}
			if(cant==0){
				this.setMessage("Debe seleccionar mínimo un dispositivo para ser activado.");
				this.setShowModal(true);
				this.setShowObservation(false);
			}
			else if(cant==1) {
				this.setShowObservation(true);
				listaTemp= new ArrayList<AllActiveDispositive>();
					for(AllActiveDispositive dis : listActiveDispositive){
						if(dis.getReAccountDeviceIdU()!=null && dis.getReAccountDeviceIdU()!=0L && dis.isActive()){
					        listaTemp.add(dis);
					    }
					}
			}else{
				this.setShowObservation(true);
				listaTemp= new ArrayList<AllActiveDispositive>();
				for(AllActiveDispositive dis : listActiveDispositive){
					if(dis.getReAccountDeviceIdU()!=null && dis.getReAccountDeviceIdU()!=0L && dis.isActive()){
						listaTemp.add(dis);
					}
				}
			}
		} catch (NullPointerException e) {
			this.setMessage("Debe seleccionar mínimo un dispositivo para ser activado.");
			this.setShowModal(true);
		}
		return deviceCode;	
	}
	
	
	public String activate(){
		this.setShowObservation(false);
		if(postValidationObservation()){
			int cant=0;
			List<AllActiveDispositive> listActiveDispositive = this.getListaTemp();
			for(AllActiveDispositive dis: listActiveDispositive){
				if(dis.isActive()){
					cant++;	
				}
			}
			if(cant==0){
				this.setMessage("Debe seleccionar un vehículo y un tag para ser enrolado.");
				this.setShowModal(true);
				this.setShowObservation(false);
			}
			else if(cant==1) {
				this.setShowConfirmationAll(false);
				this.setCorfirmMessage("¿Desea confirmar la activación de "+cant+" dispositivo seleccionado?");
				this.setShowConfirmation(true);
				this.setShowObservation(false);
				listaTemp= new ArrayList<AllActiveDispositive>();
					for(AllActiveDispositive dis : listActiveDispositive){
						if(dis.getReAccountDeviceIdU()!=null && dis.getReAccountDeviceIdU()!=0L && dis.isActive()){
					        listaTemp.add(dis);
					    }
					}		
			}else{
				this.setShowConfirmation(false);
				this.setCorfirmMessage("¿Desea confirmar la activación de los "+cant+" dispositivos seleccionados?");
				this.setShowConfirmationAll(true);
				this.setShowObservation(false);
				listaTemp= new ArrayList<AllActiveDispositive>();
					for(AllActiveDispositive dis : listActiveDispositive){
						if(dis.getReAccountDeviceIdU()!=null && dis.getReAccountDeviceIdU()!=0L && dis.isActive()){
					        listaTemp.add(dis);
					    }
					}
			}
		}
		return null;	
	}

	public String enrolar(){
		this.setShowConfirmation(false);
		this.setShowConfirmationAll(false);
		this.setShowObservation(false);
		
		if(enrollDeviceEJB.getActiveDispositive(listaTemp,
				observation, SessionUtil.ip(), SessionUtil.sessionUser().getUserId())){
			this.setMessage("La activación del dispositivo ha sido éxitosa.");
			this.setShowModal(true);
			this.clearFilter();
			this.setShowInfo(false);
		}else{
			this.setMessage("El dispositivo no ha sido activado.");
			this.setShowModal(true);
			this.clearFilter();
			this.setShowInfo(false);
		}
		return null;
	}
	
	
	
	public String enrolarAll(){
		this.setShowConfirmation(false);
		this.setShowConfirmationAll(false);
		this.setShowObservation(false);

		List<AllActiveDispositive> listActiveDispositive = this.getListaTemp();
		if(listActiveDispositive.size()>0){
			if(enrollDeviceEJB.getActiveDispositiveAll(listActiveDispositive,
					observation, SessionUtil.ip(), SessionUtil.sessionUser().getUserId())){
				this.setMessage("La activación de los dispositivos ha sido éxitosa.");
				this.setShowModal(true);
				this.clearFilter();
				this.setShowInfo(false);
			}else{
				this.setMessage("No se realizo activación en los dispositivos");
				this.setShowModal(true);
				this.clearFilter();
				this.setShowInfo(false);
			}
		}else{
		  this.setMessage("No existe dispositivos para realizar la acción.");
		  this.setShowModal(true);
		  this.setShowInfo(false);
	    }
		return null;
	}
	
	
	private boolean postValidationSearch(){
		if(numberDoc!="" && !numberDoc.matches("([0-9])+")){
			this.setMessage("El campo No.Identificación tiene caracteres inválidos. Verifique.");
			this.setShowModalError(true);
			return false;
		}
	    else if(userName!="" && !userName.matches("([a-z]|[A-Z]|[áéíóú]|[ÁÉÍÓÚ]|[0-9]|[&/]|[_-]|[.]|[ñÑ]|\\s)+")){
			this.setMessage("El campo Nombre tiene caracteres inválidos. Verifique.");
			this.setShowModalError(true);
			return false;
		}
	    else if(secondName!="" && !secondName.matches("([a-z]|[A-Z]|[áéíóú]|[ÁÉÍÓÚ]|[ñÑ]|\\s)+")){
			this.setMessage("El campo Apellidos tiene caracteres inválidos. Verifique.");
			this.setShowModalError(true);
			return false;
		}
	    else if(userEmail!="" && !userEmail.matches("([sa-zA-Z@.+_0-9-]|\\s)+")){
			this.setMessage("El campo Usuario tiene caracteres inválidos. Verifique.");
			this.setShowModalError(true);
			return false;
		}
	    else if(account!="" && !account.matches("([0-9]|\\s)+")){ 
			this.setMessage("El campo Cuenta FacilPass tiene caracteres inválidos. Verifique.");
			this.setShowModalError(true);
			return false;
		}
	    else if(plate!="" && !plate.matches("([0-9]|[a-z]|[A-Z]|[ñÑ]|\\s)+")){
			this.setMessage("El campo Placa tiene caracteres inválidos. Verifique.");
			this.setShowModalError(true);
			return false;
		}
	    else if(aditional1!="" && !aditional1.matches("([a-z]|[A-Z]|[0-9]|[/]|[(]|[)]|[_-]|[.]|[ñÑ]|\\s)+")){
			this.setMessage("El campo Adicional 1 tiene caracteres inválidos. Verifique.");
			this.setShowModalError(true);
			return false;
		}
	    else if(aditional2!="" && !aditional2.matches("([a-z]|[A-Z]|[0-9]|[/]|[(]|[)]|[_-]|[.]|[ñÑ]|\\s)+")){
			this.setMessage("El campo Adicional 2 tiene caracteres inválidos. Verifique.");
			this.setShowModalError(true);
			return false;
		}
	    else if(aditional3!="" && !aditional3.matches("([a-z]|[A-Z]|[0-9]|[/]|[(]|[)]|[_-]|[.]|[ñÑ]|\\s)+")){
			this.setMessage("El campo Adicional 3 tiene caracteres inválidos. Verifique.");
			this.setShowModalError(true);
			return false;
		}
		
		return true;
	}
	
	
	private boolean postValidationObservation(){
		if(observation.equals("")){
		  this.setMessage("El campo observación es requerido. Verifique.");
		  this.setShowModalErrorObservation(true);
			return false;
		}
		else if(observation.length()>500)
		{
			this.setMessage("El campo observación tiene una longitud superior a 500 caracteres. Verifique.");
			this.setShowModalErrorObservation(true);
			return false;
		}
		/*else if (!Validation.isObservation(observation)){
			setMessage("El campo observación posee caracteres inválidos. Verifique.");
		    this.setShowModalErrorObservation(true);
		}*/
		return true;
	}
	
	
	public String hideModal(){
		this.clearFilter();
		this.setMessage("");
		this.setShowModal(false);
		this.setShowModalError(false);
		this.setShowModalErrorObservation(false);
		this.setShowConfirmation(false);
		this.setShowConfirmationAll(false);
		this.setShowObservation(false);
		return null;
	}
	
	
	public String hideModal2(){
		this.setMessage("");
		this.setShowModal(false);
		this.setShowModalError(false);
		this.setShowModalErrorObservation(false);
		this.setShowConfirmation(false);
		this.setShowConfirmationAll(false);
		this.setShowObservation(false);
		return null;
	}
	
	
	public String hideModal3(){
		this.setMessage("");
		this.setShowModal(false);
		this.setShowModalError(false);
		this.setShowModalErrorObservation(false);
		this.setShowConfirmation(false);
		this.setShowConfirmationAll(false);
		this.setShowObservation(true);
		return null;
	}
	
	public String clearFilter(){
		codeType = -1L;
		numberDoc = "";
		userName ="";
		secondName ="";
		userEmail="";		
		this.setPlate("");
		this.setcategory(-1L);
		this.setAditional1("");
		this.setAditional2("");
		this.setAditional3("");
		this.setAccount("");
		this.setShowInfo(false);
	    return "";	
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	public String getPlate() {
		return plate;
	}

	public void setAditional1(String aditional1) {
		this.aditional1 = aditional1;
	}

	public String getAditional1() {
		return aditional1;
	}

	public void setAditional2(String aditional2) {
		this.aditional2 = aditional2;
	}


	public String getAditional2() {
		return aditional2;
	}

	public void setAditional3(String aditional3) {
		this.aditional3 = aditional3;
	}


	public String getAditional3() {
		return aditional3;
	}
	
	public void setcategory(Long category) {
		this.category = category;
	}

	public Long getCategory() {
		return category;
	}

	public void setCategoriesList(List<SelectItem> categoriesList) {
		this.categoriesList = categoriesList;
	}

	/**
	 * @return the categories
	 */
	public List<SelectItem> getCategoriesList() {
		if(categoriesList == null){
			categoriesList = new ArrayList<SelectItem>();
			categoriesList.add(new SelectItem(-1L, "-Seleccione Categoría-"));
			for (TbCategory c : categoryEJB.getCategories()) {
				categoriesList.add(new SelectItem(c.getCategoryId(), c.getCategoryCode()));
			}
		}
		return categoriesList;
	}

	public void setEnrollDeviceEJB(EnrollDevice enrollDeviceEJB) {
		this.enrollDeviceEJB = enrollDeviceEJB;
	}

	public EnrollDevice getEnrollDeviceEJB() {
		return enrollDeviceEJB;
	}

	public void setListActiveDispositive(List<AllActiveDispositive> listActiveDispositive) {
		this.listActiveDispositive = listActiveDispositive;
	}
	
	public List<AllActiveDispositive> getListActiveDispositive() {
		listActiveDispositive=enrollDeviceEJB.getDeviceByFiltersAdmin(codeType, numberDoc, userName, secondName, userEmail, plate.toUpperCase(), 
				aditional1.toUpperCase(), aditional2.toUpperCase(), aditional3.toUpperCase(), category, account);
		return listActiveDispositive;
	}

	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}

	public String getDeviceCode() {
		return deviceCode;
	}

	public void setDeviceFacial(String deviceFacial) {
		this.deviceFacial = deviceFacial;
	}

	public String getDeviceFacial() {
		return deviceFacial;
	}

	public void setShowModalError(boolean showModalError) {
		this.showModalError = showModalError;
	}


	public boolean isShowModalError() {
		return showModalError;
	}

	public void setShowConfirmation(boolean showConfirmation) {
		this.showConfirmation = showConfirmation;
	}

	public boolean isShowConfirmation() {
		return showConfirmation;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	
	public void setCorfirmMessage(String corfirmMessage) {
		this.corfirmMessage = corfirmMessage;
	}
	
	public String getCorfirmMessage() {
		return corfirmMessage;
	}


	public void setListaTemp(List<AllActiveDispositive> listaTemp) {
		this.listaTemp = listaTemp;
	}


	public List<AllActiveDispositive> getListaTemp() {
		return listaTemp;
	}

	public void setShowConfirmationAll(boolean showConfirmationAll) {
		this.showConfirmationAll = showConfirmationAll;
	}


	public boolean isShowConfirmationAll() {
		return showConfirmationAll;
	}


	public void setAccount(String account) {
		this.account = account;
	}


	public String getAccount() {
		return account;
	}


	public void setShowObservation(boolean showObservation) {
		this.showObservation = showObservation;
	}


	public boolean isShowObservation() {
		return showObservation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}
	

	public String getObservation() {
		return observation;
	}
	

	public void setShowModal(boolean showModal) {
		this.showModal = showModal;
	}

	
	public boolean isShowModal() {
		return showModal;
	}


	public void setCodeType(long codeType) {
		this.codeType = codeType;
	}

	public long getCodeType() {
		return codeType;
	}

	public void setCodeTypesList(List<SelectItem> codeTypesList) {
		this.codeTypesList = codeTypesList;
	}

	public List<SelectItem> getCodeTypesList() {
		if(codeTypesList == null) {
			codeTypesList = new ArrayList<SelectItem>();			
		}else{
			codeTypesList.clear();
		}
		codeTypesList.add(new SelectItem(-1L,"-- Seleccione Uno --"));
		for(TbCodeType c : userEJB.getCodeTypes()){
			codeTypesList.add(new SelectItem(c.getCodeTypeId(),c.getCodeTypeAbbreviation().toUpperCase()));
		}
		return codeTypesList;
	}

	public void setNumberDoc(String numberDoc) {
		this.numberDoc = numberDoc;
	}

	public String getNumberDoc() {
		return numberDoc;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	public String getSecondName() {
		return secondName;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setShowModalErrorObservation(boolean showModalErrorObservation) {
		this.showModalErrorObservation = showModalErrorObservation;
	}

	public boolean isShowModalErrorObservation() {
		return showModalErrorObservation;
	}

	public void setShowInfo(boolean showInfo) {
		this.showInfo = showInfo;
	}

	public boolean isShowInfo() {
		return showInfo;
	}	

}
