package process.device;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import security.UserLogged;
import sessionVar.SessionUtil;
import util.AllActiveDispositive;

import ejb.Category;
import ejb.EnrollDevice;
import ejb.User;
import jpa.TbCategory;


/**
 * Bean to manage Active device.
 * @author psanchez
 *
 */
public class ActiveDeviceBean implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@EJB(mappedName = "ejb/Category")
	private Category categoryEJB;
	
	@EJB(mappedName="ejb/EnrollDevice")
	private EnrollDevice enrollDeviceEJB;
	
	@EJB(mappedName="ejb/User")
	private User userEJB;
	
	private boolean showModal;
	private boolean showModalError;
	private boolean showConfirmation;
	private boolean showConfirmationAll;
	
	private String message;
	private String corfirmMessage;
	
	/* --Category --*/
	private Long category;
	private List<SelectItem> categoriesList;
	
	private List<AllActiveDispositive> listActiveDispositive;
	private List<AllActiveDispositive> listaTemp;
	
	private boolean searchok= false;
	private boolean refrescar=true;
	
	private Long deviceId;
	private String plate;
	private String aditional1;
	private String aditional2;
	private String aditional3;
	private String account;
	private String deviceCode;
	private String deviceFacial;
	private String observation;
	
	private UserLogged usrs;
	
	public ActiveDeviceBean(){
		super();
		setUsrs((UserLogged) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user"));
	}

	public void searchFilter(){
		this.showModal = false;
		this.showModalError = false;
		this.showConfirmation = false;
		this.showConfirmationAll = false;
		this.refrescar = false;
		if(postValidationSearch()){
			if((plate.equals("")) && 
				(category == -1L) && 
				(aditional1.equals("")) && 
				(aditional2.equals("")) && 
				(aditional3.equals("")) &&
				(account.equals(""))) { 
				this.message = "No existe datos para realizar la búsqueda.";
				this.showModal= true;
			}else{
				String userId = Long.toString(usrs.getUserId());
				List<AllActiveDispositive> listfilter = null;
				listfilter = enrollDeviceEJB.getDeviceByFiltersClient(plate.toUpperCase(), aditional1.toUpperCase(), 
						aditional2.toUpperCase(), aditional3.toUpperCase(), category, account, String.valueOf((userEJB.getSystemMasterById(usrs.getUserId()))));
				if(listfilter.size()<=0){
					this.message = "No se encontraron resultados para la búsqueda.";
					this.showModal = true;
					this.listActiveDispositive.clear();
					this.setSearchok(false);
				}else {
					this.listActiveDispositive = listfilter;
					this.setSearchok(true);
					this.setShowModalError(false);
				}
				this.refrescar = (true);
			}
		}
	}
	

	public String enrolarConfirm(){
		this.setShowConfirmation(false);
		this.setShowConfirmationAll(false);
		this.setShowModalError(false);
		int cant=0;
		for(util.AllActiveDispositive dis: listActiveDispositive){
			if(dis.getDeviceCodeU()!=null && dis.isActive()){
				cant++;	
			}
		}
		if(cant==0){
			this.setMessage("Debe seleccionar mínimo un dispositivo para ser activado.");
			this.setShowModal(true);
		}
		else if(cant==1) {
			this.activate();
			listaTemp= new ArrayList<AllActiveDispositive>();
				for(AllActiveDispositive dis : listActiveDispositive){
					if(dis.getReAccountDeviceIdU()!=null && dis.getReAccountDeviceIdU()!=0L && dis.isActive()){
				        listaTemp.add(dis);
				    }
				}
		}else{
			this.activate();
			listaTemp= new ArrayList<AllActiveDispositive>();
				for(AllActiveDispositive dis : listActiveDispositive){
					if(dis.getReAccountDeviceIdU()!=null && dis.getReAccountDeviceIdU()!=0L && dis.isActive()){
				        listaTemp.add(dis);
				    }
				}
		}
		return deviceCode;	
	}
	
	
	public String activate(){
			int cant=0;
			for(util.AllActiveDispositive dis: listActiveDispositive){
				if(dis.isActive()){
					cant++;	
				}
			}
			if(cant==0){
				this.setMessage("Debe seleccionar un vehículo y un tag para ser enrolado.");
				this.setShowModal(true);
			}
			else if(cant==1) {
				this.setShowConfirmationAll(false);
				this.setCorfirmMessage("¿Desea confirmar la activación de "+cant+" dispositivo seleccionado?");
				this.setShowConfirmation(true);
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
				listaTemp= new ArrayList<AllActiveDispositive>();
					for(AllActiveDispositive dis : listActiveDispositive){
						if(dis.getReAccountDeviceIdU()!=null && dis.getReAccountDeviceIdU()!=0L && dis.isActive()){
					        listaTemp.add(dis);
					    }
					}
			}
		return null;	
	}

	public String enrolar(){
		this.setShowConfirmation(false);
		this.setShowConfirmationAll(false);
		
		if(enrollDeviceEJB.getActiveDispositive(listaTemp,
					observation, SessionUtil.ip(), SessionUtil.sessionUser().getUserId())){
			this.setMessage("La activación del dispositivo ha sido éxitosa.");
			this.setShowModal(true);
			this.listActiveDispositive.clear();
		}else{
			this.setMessage("El dispositivo no ha sido activado.");
			this.setShowModal(true);
			/*this.listActiveDispositive.clear();*/
		}
		return null;
	}
	
	

	public String enrolarAll(){
		this.setShowConfirmation(false);
		this.setShowConfirmationAll(false);

		List<AllActiveDispositive> listActiveDispositive = this.getListaTemp();
		if(listActiveDispositive.size()>0){
			if(enrollDeviceEJB.getActiveDispositiveAll(listActiveDispositive,
					observation, SessionUtil.ip(), SessionUtil.sessionUser().getUserId())){
				this.setMessage("La activación de los dispositivos ha sido éxitosa.");
				this.setShowModal(true);
				this.listActiveDispositive.clear();
			}else{
				this.setMessage("No se realizo activación en los dispositivos");
				this.setShowModal(true);
				this.listActiveDispositive.clear();
			}
		}else{
		  this.setMessage("No existe dispositivos para realizar la acción.");
		  this.setShowModal(true);
	    }
		return null;
	}
	
	
	private boolean postValidationSearch(){
	   if(account!="" && !account.matches("([0-9]|\\s)+")){ 
			this.setMessage("El campo Cuenta FacilPass tiene caracteres inválidos. Verifique.");
			this.setShowModalError(true);
			return false;
		}else if(plate!="" && !plate.matches("([0-9]|[a-z]|[A-Z]|[ñÑ]|\\s)+")){
			this.setMessage("El campo Placa tiene caracteres inválidos. Verifique.");
			this.setShowModalError(true);
			return false;
		}else if(aditional1!="" && !aditional1.matches("([a-z]|[A-Z]|[0-9]|[/]|[(]|[)]|[_-]|[.]|[ñÑ]|\\s)+")){
			this.setMessage("El campo Adicional 1 tiene caracteres inválidos. Verifique.");
			this.setShowModalError(true);
			return false;
		}else if(aditional2!="" && !aditional2.matches("([a-z]|[A-Z]|[0-9]|[/]|[(]|[)]|[_-]|[.]|[ñÑ]|\\s)+")){
			this.setMessage("El campo Adicional 2 tiene caracteres inválidos. Verifique.");
			this.setShowModalError(true);
			return false;
		}else if(aditional3!="" && !aditional3.matches("([a-z]|[A-Z]|[0-9]|[/]|[(]|[)]|[_-]|[.]|[ñÑ]|\\s)+")){
			this.setMessage("El campo Adicional 3 tiene caracteres inválidos. Verifique.");
			this.setShowModalError(true);
			return false;
		}
		return true;
	}
	
	
	public String hideModal(){
		this.clearFilter();
		this.setMessage("");
		this.setShowModal(false);
		this.setShowModalError(false);
		this.setShowConfirmation(false);
		this.setShowConfirmationAll(false);
		return null;
	}
	
	
	public String hideModal2(){
		this.setMessage("");
		this.setShowModal(false);
		this.setShowModalError(false);
		this.setShowConfirmation(false);
		this.setShowConfirmationAll(false);
		return null;
	}
	
	public String hideModal3(){
		this.setMessage("");
		this.setShowModal(false);
		this.setShowModalError(false);
		this.setShowConfirmation(false);
		this.setShowConfirmationAll(false);
		return null;
	}

	public String clearFilter(){		
		this.setPlate("");
		this.setcategory(-1L);
		this.setAditional1("");
		this.setAditional2("");
		this.setAditional3("");
		this.setAccount("");
		this.setSearchok(false);
		this.getListActiveDispositive();
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
		String userId = Long.toString(usrs.getUserId());
		if(listActiveDispositive == null) {
			listActiveDispositive = new ArrayList<AllActiveDispositive>();			
		}
		else{
			if(searchok==false){
				listActiveDispositive.clear();
			} 
		}
		if(searchok==false){
			listActiveDispositive = enrollDeviceEJB.getAllDevice(String.valueOf((userEJB.getSystemMasterById(usrs.getUserId()))));
		} 
		if(searchok==true){
			listaTemp=listActiveDispositive;
		}
		refrescar = true;
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
	
	public void setRefrescar(boolean refrescar) {
		this.refrescar = refrescar;
	}

	public boolean isRefrescar() {
		return refrescar;
	}
	
	public void setSearchok(boolean searchok) {
		this.searchok = searchok;
	}

	public boolean isSearchok() {
		return searchok;
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

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	public Long getDeviceId() {
		return deviceId;
	}

	public void setUsrs(UserLogged usrs) {
		this.usrs = usrs;
	}

	public UserLogged getUsrs() {
		return usrs;
	}

	

}
