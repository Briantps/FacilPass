
package mBeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollerEvent;

import jpa.TbCodeType;
import jpa.TbSystemUser;

import util.AllInfoClient;
import util.Cities;
import util.ConnectionData;

import net.sf.jasperreports.engine.JRDataSource;
import report.AbstractBaseReportBean;
import sessionVar.SessionUtil;

import ejb.User;


public class ListClient extends AbstractBaseReportBean implements Serializable {
	private static final long serialVersionUID = 8799656478674716638L;
	@EJB(mappedName="ejb/User")
	private User userEJB;
	
	// Atributos de Búsqueda -------------//
	
	private List<TbSystemUser> listClient;
	private List<AllInfoClient> listAllInfoClients;
	private List<Integer> listaScroll;
	
	private List<SelectItem> codeTypesList;
	private Long codeTypeSearch;
	
	private String numberDocSearch="";
	private String userNameSearch="";
	private String secondNameSearch="";
	private String userEmailSearch="";
	private String accountIdSearch="";
	private String plateSearch="";
	
	private List<SelectItem> cities;
	private Long city;
	
	private Long userId;
	private Long userDataId;
	private Long category;
	private Long balanceAccount;
	private Long digitsBank;	
	private Long codeType;	
	private Long stateId;

	private String numberDoc;
	private String accountId;
	private String userName;
	private String secondName;
	private String userEmail;
	private String stateDesc;
	private String codeTypeAbbr;
	private String plate;
	private String nameBank;
	private String tagId;
	private String facial;
    private String typeDistr;
	private String creationClient;
	private String userPhone1;
	private String userPhone2;
	private String userAdress;
	private String modalMessage;
	
	private boolean showModal;
	private boolean showModalValidate;
	private boolean showEdit;
	private boolean searchok= false;
	private boolean showConfirmation;
	private boolean showConfirmationI;
	private boolean showConfirmationA;
	private boolean showConfirRestPass;
	private boolean showConfirResetQuestion,showResetQuestion;
	
	private int page=1;
	private int valuesFor;
	
	//Atributos para la generacion del Reporte de Clientes --//
	
	public boolean isShowConfirResetQuestion() {
		return showConfirResetQuestion;
	}

	public void setShowConfirResetQuestion(boolean showConfirResetQuestion) {
		this.showConfirResetQuestion = showConfirResetQuestion;
	}

	private ConnectionData connection= new ConnectionData();
	private final String COMPILE_FILE_NAME = "AllClientsReportXls";
	
	/**
	 * Constructor.
	 */
	public ListClient() {
	}
	
	@PostConstruct
	public void init(){
		this.clearFilter();
	}
	
	// Actions ------------ //
	/**
	 * Reestablish the client password.
	 */
	public void resetPassword() {
		if (userEJB.resetPasswordClient(userId, SessionUtil.ip(), SessionUtil.sessionUser().getUserId())) {
			this.modalMessage = "La contraseña del cliente ha sido restablecida con Éxito.";
		} else {
			this.modalMessage = "Error al realizar la Transacción, comuníquese con el Administrador.";
		}
		this.setShowConfirRestPass(false);
		this.setShowModalValidate(true);
	}
	
	public void confirmRestPasword(){
		this.setShowModalValidate(false);
		this.modalMessage=" ¿Está seguro de restablecer la contraseña del cliente "+userName+" "+secondName+". "+codeTypeAbbr+". "+numberDoc+"? ";
		this.setShowConfirRestPass(true);
	}
	
	/**
	 * Restablece preguntas de seguridad del cliente
	 * @psanchez
	 */
	public void resetSecurityQuestions() {
		if (userEJB.resetSecurityQuestions(userId, SessionUtil.ip(), SessionUtil.sessionUser().getUserId())) {
			this.modalMessage = "Las preguntas de seguridad del cliente han sido restablecidas con Éxito.";
		} else {
			this.modalMessage = "El cliente no tiene preguntas de seguridad configuradas.";
		}
		this.setShowConfirRestPass(false);
		this.setShowResetQuestion(true);
	}
	
	
      public void confirmResetQuetion(){
    	this.setShowModalValidate(false);
		this.modalMessage=" ¿Está seguro de restablecer las preguntas de seguridad del cliente "+userName+" "+secondName+". "+codeTypeAbbr+". "+numberDoc+"? ";
		this.setShowConfirResetQuestion(true);
	}
	
	/**
	 * Init Edition of initEditClient.
	 */
	public void initEditClient(){
		this.setShowEdit(true);
		for(AllInfoClient th : listAllInfoClients){
			if(userId.longValue() ==th.getUserIdU().longValue()){
				codeType      = th.getCodeTypeU();
				if (codeType!=null){
					creationClient= th.getCreationDateU();
					codeType      = th.getCodeTypeU();
					userDataId    = th.getUserDataIdU();
					codeTypeAbbr  = th.getCodeTypeAbbrU();
					userName      = th.getNameU();
					secondName    = th.getSecondNameU();
					userEmail     = th.getEmailU();
					stateId       = th.getStateIdU();
					userPhone1    = th.getPhoneU();
					userPhone2    = th.getOptionalPhoneU();
					userAdress    = th.getAddressU();
				}
		  break;
			}
		}
	}
	
	
	/**
	 * Método usado para inactivar usuarios del sistema.
	 */
	public void inactivateUserXAdmin() {
		 showConfirmationI = false;
		 showModal= false;
		 showEdit = false;
		try{
			if(postValidationNew()){
				if (userEJB.editInformationLegalPerson(userId, userDataId, userName, secondName, userEmail, city, 
				    userAdress, userPhone1, userPhone2, SessionUtil.ip(), SessionUtil.sessionUser().getUserId()) && 
				    userEJB.cancelSteps(userId, SessionUtil.ip(), SessionUtil.sessionUser().getUserId())){
	    			this.setShowEdit(false);
	    			this.setModalMessage("Transacción Exitosa.");
	    			this.setShowModalValidate(true);
	    			this.clearFilter();
	    			this.setListAllInfoClients(null);
		    	} 
			}
		}catch(Exception ex){
			 setModalMessage("Error al inactivar el cliente.");
			 setShowModal(true);
			 ex.printStackTrace();
		 }
	}
	
	/**
	 * Método usado para activar usuarios del sistema.
	 */
	public void activateUserXAdmin() {
		showConfirmationA = false;
		showModal= false;
		showEdit = false;
		try{
			if(postValidationNew()){
				if (userEJB.editInformationLegalPerson(userId, userDataId, userName, secondName, userEmail, city, 
				    userAdress, userPhone1, userPhone2, SessionUtil.ip(), SessionUtil.sessionUser().getUserId()) && 
				    userEJB.activateClient(userId, SessionUtil.ip(), SessionUtil.sessionUser().getUserId())) {
	    			this.setShowEdit(false);
	    			this.setModalMessage("Transacción Exitosa.");
	    			this.setShowModalValidate(true);
	    			this.clearFilter();
	    			this.setListAllInfoClients(null);
		    	}
			}
		}catch(Exception ex){
			 setModalMessage("Error al activar el cliente.");
			 setShowModal(true);
			 ex.printStackTrace();
		}
	}
	
	
	private boolean postValidationNew(){
		if(userName!=""  && !userName.matches("([a-z]|[A-Z]|[áéíóú]|[ÁÉÍÓÚ]|[0-9]|[&/]|[_-]|[.]|[ñÑ]|\\s)+") && codeType==3){
			this.setModalMessage("El campo Nombre tiene caracteres inválidos. Verifique.");
			this.setShowModal(true);
			return false;
		}else if(secondName!="" && !secondName.matches("([a-z]|[A-Z]|[áéíóú]|[ÁÉÍÓÚ]|[ñÑ]|\\s)+") && codeType==3){
			this.setModalMessage("El campo Apellidos tiene caracteres inválidos. Verifique.");
			this.setShowModal(true);
			return false;
		}else if(userPhone1!="" && (!userPhone1.matches("([0-9])+"))){
			this.setModalMessage("El campo Celular tiene caracteres inválidos. Verifique.");
			this.setShowModal(true);
			return false;
		}else if(userPhone2!="" && (!userPhone2.matches("([0-9]|[a-z]|[A-Z]|\\s)+"))){
			this.setModalMessage("El campo Teléfono Opcional tiene caracteres inválidos. Verifique.");
			this.setShowModal(true);
			return false;
		}else if(userEmail!="" && !userEmail.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")){
			this.setModalMessage("El campo Usuario tiene caracteres inválidos. Verifique.");
			this.setShowModal(true);
			return false;
		}else if(userName.equals(null) || userName.equals("")){
			setModalMessage("El campo Nombres es requerido.");
		    setShowModal(true);
		    return false;
		}else if(userName.length()<3 || userName.length()>100){
			setModalMessage("El campo Nombres no debe ser menor a 3 ni mayor a 100 caracteres. Verifique.");
		    setShowModal(true);
		    return false;
		}else if(secondName.equals(null) || secondName.equals("")){
			setModalMessage("El campo Apellidos es requerido.");
		    setShowModal(true);
		    return false;
		}else if(secondName.length()<3 || secondName.length()>100){
			setModalMessage("El campo Apellidos no debe ser menor a 3 ni mayor a 100 caracteres. Verifique.");
		    setShowModal(true);
		    return false;
		}else if(userEJB.editUserEmailUK(userId, userEmail)==false){
			setModalMessage("Existe un usuario con el mismo Correo Electrónico. Verifique.");	
		    setShowModal(true);
		    return false;
		}else if(userEmail == ""){
			setModalMessage("El campo Usuario es requerido.");
			setShowModal(true); 
		}else if(!this.checkEmail(userEmail)){
			setModalMessage("Correo inválido. Verifique.");
		    setShowModal(true);
		    return false;
		}else if(userPhone1.equals(null) || userPhone1.equals("")){
			setModalMessage("El campo Celular es requerido.");
			setShowModal(true);
			return false;
		}else if(userPhone1.length() < 7 || userPhone1.length() > 30){
			setModalMessage("La longitud del campo Celular no es correcta. Recuerde que debe estar entre 7 y 30 caracteres.");	
			setShowModal(true);
			return false;
		}else if(validateCell(userPhone1)==true){
			setModalMessage("El número "+userPhone1+" no es un Celular válido.");
			setShowModal(true);
			return false;
		}else if(userAdress.equals(null) || userAdress.equals("")){	
			setModalMessage("El campo Dirección es requerido.");
			setShowModal(true);
			return false;
		}else if(userAdress.length() < 7 || userAdress.length() > 50){
			setModalMessage("La longitud del campo Dirección no es correcta. Recuerde que debe estar entre 7 y 50 caracteres.");
			setShowModal(true);
			return false;
		}else if(userPhone2.length() > 0 && (userPhone2.length() < 7 || userPhone2.length() > 30)){
	    	setModalMessage("La longitud del campo Teléfono Opcional no es correcta. Recuerde que debe estar entre 7 y 30 caracteres.");
			setShowModal(true);
			return false;
    	}else if(validatePhone(userPhone2)==true){
			setModalMessage("El número "+userPhone2+" no es un Teléfono Opcional válido.");
			setShowModal(true);
			return false;
		}	
		return true;
	}
	
	
	
	private boolean postValidationSearch(){
		if((codeTypeSearch == -1L) && 
			(numberDocSearch.equals("")) && 
			(userNameSearch.equals("")) && 
			(secondNameSearch.equals("")) && 
			(userEmailSearch.equals("")) && 
			(accountIdSearch.equals("")) && 
			(plateSearch.equals(""))) { 
			this.modalMessage = "No existe datos para realizar la búsqueda.";
			this.showModalValidate= true;
			return false;
		}else if(numberDocSearch!="" && !numberDocSearch.matches("([0-9])+")){
			this.setModalMessage("El campo No.Identificación tiene caracteres inválidos. Verifique.");
			this.setShowModalValidate(true);
			return false;
		}else if(userNameSearch!="" && !userNameSearch.matches("([a-z]|[A-Z]|[áéíóú]|[ÁÉÍÓÚ]|[0-9]|[&/]|[_-]|[.]|[ñÑ]|\\s)+")){
			this.setModalMessage("El campo Nombre tiene caracteres inválidos. Verifique.");
			this.setShowModalValidate(true);
			return false;
		}else if(secondNameSearch!="" && !secondNameSearch.matches("([a-z]|[A-Z]|[áéíóú]|[ÁÉÍÓÚ]|[ñÑ]|\\s)+")){
			this.setModalMessage("El campo Apellidos tiene caracteres inválidos. Verifique.");
			this.setShowModalValidate(true);
			return false;
		}else if(userEmailSearch!="" && !userEmailSearch.matches("([sa-zA-Z@.+_0-9-]|\\s)+")){
			this.setModalMessage("El campo Usuario tiene caracteres inválidos. Verifique.");
			this.setShowModalValidate(true);
			return false;
		}else if(accountIdSearch!="" && !accountIdSearch.matches("([0-9]|\\s)+")){ 
			this.setModalMessage("El campo Cuenta FacilPass tiene caracteres inválidos. Verifique.");
			this.setShowModalValidate(true);
			return false;
		}else if(plateSearch!="" && !plateSearch.matches("([0-9]|[a-z]|[A-Z]|[ñÑ]|\\s)+")){
			this.setModalMessage("El campo Placa tiene caracteres inválidos. Verifique.");
			this.setShowModalValidate(true);
			return false;
		}
		return true;
	}
	
	
	public void showConfirmInactivateUserXAdmin(){
		showModal= false;
		showEdit = false;
		this.setShowConfirmationI(true);
		this.setModalMessage("Al inactivar el cliente se realizará la inactivación de todos los TAGS relacionados. ¿Desea continuar?");
	}
	
	
	public void showConfirmActivateUserXAdmin(){
		showModal= false;
		showEdit = false;
		this.setShowConfirmationA(true);
		this.setModalMessage("Al activar el cliente se realizará la activación de todos los TAGS relacionados. ¿Desea continuar?");
	}
	
	
	public void clearFilter(){
		codeTypeSearch = -1L;
		numberDocSearch = "";
		userNameSearch = "";
		secondNameSearch = "";
		userEmailSearch = "";		
		accountIdSearch = "";
		plateSearch = "";
		this.setPage(1);
		this.setSearchok(false);
		this.getListAllInfoClients();
	}
	
	
	public boolean validateCell(String userPhone1){
	  Boolean wrongPhone=false;
		try {
			int phoneUser = Integer.parseInt(userPhone1);
			if(phoneUser<=10){
				wrongPhone=true;
			}
		} catch (Exception e) {
			 wrongPhone=false;
		}
		return wrongPhone;
	}
	
	public boolean validatePhone(String userPhone2){
		Boolean wrongPhone2=false;	
		  if(!"".equals(userPhone2)){
				try {
					int phoneUser2 = Integer.parseInt(userPhone2);
					if(phoneUser2<=10){
						wrongPhone2=true;
					}
				} catch (Exception e) {
					wrongPhone2=false;	
				}
			}
		return wrongPhone2;
	  }

	/**
	 * Método encargado de validar los datos a editar (ciudad, dirección, celular, telefono opcional).
	 * @author psanchez
	 */
	public void editDataClient(){
		 showModal= false;
		 showEdit = false;
		try{
			if(postValidationNew()){
	    		setShowModal(false);
				setShowConfirmation(true);
				setModalMessage(null);
				setModalMessage("¿Está seguro de realizar cambios?");
			}
		 }catch(Exception ex){
			 setModalMessage("Error al modificar datos del cliente.");
			 setShowModal(true);
			 ex.printStackTrace();
		 }
	}
	
	
	public void saveDataClient() {
		setModalMessage(null);
		if(userEJB.editInformationLegalPerson(userId, userDataId, userName, secondName, userEmail, city, 
			        userAdress, userPhone1, userPhone2, SessionUtil.ip(), SessionUtil.sessionUser().getUserId())){
			setModalMessage("Los datos del cliente han sido modificados con éxito");
		} else {
			setModalMessage("Los campos obligatorios no deben estar vacíos.");
		}
		setShowModal(false);
		setShowModalValidate(true);
	    clearFilter();
		setShowConfirmation(false);
	}
	
	
	/**
	 * Hides modal windows.
	 */
	public void hideModal(){
		this.setShowEdit(true);
		this.setShowModal(false);
		this.setShowConfirmation(false);
		this.setShowConfirmationI(false);
		this.setShowConfirmationA(false);
	}
	
	public void hideModal2() {
		this.setShowEdit(false);
		this.setShowModal(false);
		this.setShowModalValidate(false);
		this.setShowConfirRestPass(false);
		this.setShowConfirResetQuestion(false);
		this.setShowResetQuestion(false);
		this.setShowConfirmation(false);
		this.setShowConfirmationI(false);
		this.setShowConfirmationA(false);
	}
	
	
	public void hideModal3() {
		this.clearFilter();
		this.setShowEdit(false);
		this.setShowModal(false);
		this.setShowModalValidate(false);
		this.setShowConfirmation(false);
		this.setShowConfirmationI(false);
		this.setShowConfirmationA(false);
	}
	

	/** checkEmail *  Valida si una direccion email es correcta. * *  
	 * @param String email a validar *  
	 * @return true si es correcto *  
	 * @author http://mirastro.wordpress.com 
	 * */
	public boolean checkEmail (String email) {
		Pattern p = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"); // Establecer el patrón
	    Matcher m = p.matcher(email); // Asociar el string al patrón
	   return m.matches();   	      // Comprobar si encaja
	}
	
	// Getters and Setters ------------ //
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
	 * @param listClient the listClient to set
	 */
	public void setListClient(List<TbSystemUser> listClient) {
		this.listClient = listClient;
	}

	/**
	 * @return the listClient
	 */
	public List<TbSystemUser> getListClient() {
		if(listClient == null) {
			listClient = new ArrayList<TbSystemUser>();
			listClient = userEJB.getAllUsersClients();
		}
		return listClient;
	}

	/**
	 * @param clientId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * @param modal the modal to set
	 */
	public void setShowModal(boolean showModal) {
		this.showModal = showModal;
	}

	/**
	 * @return the modal
	 */
	public boolean isShowModal() {
		return showModal;
	}

	public void setListAllInfoClients(List<AllInfoClient> listAllInfoClients) {
		this.listAllInfoClients = listAllInfoClients;
	}

	public List<AllInfoClient> getListAllInfoClients() {
		if(listAllInfoClients == null) {
			listAllInfoClients = new ArrayList<AllInfoClient>();			
		}else if(searchok==false){
			listAllInfoClients.clear(); 
		}
		if(searchok==false){
			this.getDataForScroll();
			listAllInfoClients = userEJB.getAllInfoClients(page, 11);
		}
	    if(searchok==true){
	    	this.getDataForScroll();
			listAllInfoClients = userEJB.getInfoClientsByFilters(codeTypeSearch, numberDocSearch, userNameSearch, 
					secondNameSearch, userEmailSearch, accountIdSearch, plateSearch, page, 11, 2, 0, "");
		}
		return listAllInfoClients;
	}
	
	public void getDataForScroll(){
		try {
			if(searchok==false){
				this.setValuesFor(Integer.parseInt(String.valueOf(userEJB.getAllInfoClients(0, 11).get(0))));
			}else{
				this.setValuesFor(Integer.parseInt(String.valueOf(userEJB.getInfoClientsByFilters(codeTypeSearch, numberDocSearch,  
						userNameSearch, secondNameSearch, userEmailSearch, accountIdSearch, plateSearch, 0, 11, 2, 0, "").get(0))));
			}
			listaScroll=new ArrayList<Integer>();
			for(int i=0;i<getValuesFor();i++){	
				listaScroll.add(i);
			}
		} catch (Exception e) {
	     	e.printStackTrace();
		}
	}
	
	public void dataScroller(ActionEvent event)throws AbortProcessingException {
		DataScrollerEvent events=(DataScrollerEvent)event;
		page = events.getPage();
		setPage(1);
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

	public void setCodeType(Long codeType) {
		this.codeType = codeType;
	}

	public Long getCodeType() {
		return codeType;
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

	public void setStateId(Long stateId) {
		this.stateId = stateId;
	}

	public Long getStateId() {
		return stateId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getAccountId() {
		return accountId;
	}
	
	/**
	 * @param cities the cities to set
	 */
	public void setCities(List<SelectItem> cities) {
		this.cities = cities;
	}

	/**
	 * @return the cities
	 */
	public List<SelectItem> getCities() {
		cities = new ArrayList<SelectItem>();
		List<Cities> c = userEJB.getCities();	
		Long b = new Long(2);
		String city;
		for(Cities ci : c){
			city=ci.getCity() + " - "  + "(" + ci.getDepartment().substring(0,4) + ")";
			if(ci.getCityCode().equals("0")){
				if(codeType== b){
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
	
	
	public void searchFilter(){
		this.showModalValidate = false;
		this.showConfirmation = false;
		if(postValidationSearch()){
		    setPage(1);
			List<AllInfoClient> listfilter = null;
			listfilter = userEJB.getInfoClientsByFilters(codeTypeSearch, numberDocSearch, userNameSearch, 
					secondNameSearch, userEmailSearch, accountIdSearch, plateSearch, page, 11, 2, 0, "");
			if(listfilter.size()<=0){
				this.modalMessage = "No se encontraron resultados de la búsqueda.";
				this.showModalValidate = true;
				this.listAllInfoClients.clear();
				this.setSearchok(false);
			}else{
				this.listAllInfoClients = listfilter;
				this.setSearchok(true);
				this.setShowModalValidate(false);
			}
		}
	}

	
	//Metodos para la generacion del reporte ------ //
	
	@Override
	protected Map<String, Object> getReportParameters() {
		Map<String, Object> reportParameters = new HashMap<String, Object>();		
		return reportParameters;
	}

	@Override
	protected String getCompileFileName() {
		return COMPILE_FILE_NAME;
	}

	@Override
	protected ConnectionData getDataConnection() {
		return connection;
	}

	@Override
	protected String getFileName() {
		return "Reporte_Clientes_" + System.currentTimeMillis();
	}

	@Override
	protected JRDataSource getJRDataSource() {
		return null;
	}
	
	public void setConnection(ConnectionData connection) {
		this.connection = connection;
	}

	public ConnectionData getConnection() {
		return connection;
	}

	public String getCOMPILE_FILE_NAME() {
		return COMPILE_FILE_NAME;
	}

	
	public String imprimirReporte(){
		try {
			
			super.setExportOption(ExportOption.EXCEL);
			super.prepareReport();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	public String getPlate() {
		return plate;
	}

	public void setShowEdit(boolean showEdit) {
		this.showEdit = showEdit;
	}

	public boolean isShowEdit() {
		return showEdit;
	}

	public void setStateDesc(String stateDesc) {
		this.stateDesc = stateDesc;
	}

	public String getStateDesc() {
		return stateDesc;
	}

	public void setCreationClient(String creationClient) {
		this.creationClient = creationClient;
	}

	public String getCreationClient() {
		return creationClient;
	}

	public void setCategory(Long category) {
		this.category = category;
	}

	public Long getCategory() {
		return category;
	}

	public void setTypeDistr(String typeDistr) {
		this.typeDistr = typeDistr;
	}

	public String getTypeDistr() {
		return typeDistr;
	}
	
	public Long getUserDataId() {
		return userDataId;
	}

	public void setUserDataId(Long userDataId) {
		this.userDataId = userDataId;
	}

	public void setShowConfirmation(boolean showConfirmation) {
		this.showConfirmation = showConfirmation;
	}


	public boolean isShowConfirmation() {
		return showConfirmation;
	}
	
	public void setShowConfirmationI(boolean showConfirmationI) {
		this.showConfirmationI = showConfirmationI;
	}

	public boolean isShowConfirmationI() {
		return showConfirmationI;
	}

	public void setShowConfirmationA(boolean showConfirmationA) {
		this.showConfirmationA = showConfirmationA;
	}

	public boolean isShowConfirmationA() {
		return showConfirmationA;
	}

	public void setBalanceAccount(Long balanceAccount) {
		this.balanceAccount = balanceAccount;
	}


	public Long getBalanceAccount() {
		return balanceAccount;
	}


	public void setNameBank(String nameBank) {
		this.nameBank = nameBank;
	}


	public String getNameBank() {
		return nameBank;
	}


	public void setDigitsBank(Long digitsBank) {
		this.digitsBank = digitsBank;
	}


	public Long getDigitsBank() {
		return digitsBank;
	}


	public void setTagId(String tagId) {
		this.tagId = tagId;
	}


	public String getTagId() {
		return tagId;
	}


	public void setFacial(String facial) {
		this.facial = facial;
	}

	public String getFacial() {
		return facial;
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
	
	public void setShowModalValidate(boolean showModalValidate) {
		this.showModalValidate = showModalValidate;
	}

	public boolean isShowModalValidate() {
		return showModalValidate;
	}

	public boolean getShowConfirRestPass() {
		return showConfirRestPass;
	}

	public void setShowConfirRestPass(boolean showConfirRestPass) {
		this.showConfirRestPass = showConfirRestPass;
	}
	public boolean isShowResetQuestion() {
		return showResetQuestion;
	}

	public void setShowResetQuestion(boolean showResetQuestion) {
		this.showResetQuestion = showResetQuestion;
	}

	public void setListaScroll(List<Integer> listaScroll) {
		this.listaScroll = listaScroll;
	}

	public List<Integer> getListaScroll() {
		return listaScroll;
	}
	
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public void setValuesFor(int valuesFor) {
		this.valuesFor = valuesFor;
	}

	public int getValuesFor() {
		return valuesFor;
	}
	
	public void setSearchok(boolean searchok) {
		this.searchok = searchok;
	}

	public boolean isSearchok() {
		return searchok;
	}

	public void setCodeTypeAbbr(String codeTypeAbbr) {
		this.codeTypeAbbr = codeTypeAbbr;
	}

	public String getCodeTypeAbbr() {
		return codeTypeAbbr;
	}

	public void setCodeTypeSearch(Long codeTypeSearch) {
		this.codeTypeSearch = codeTypeSearch;
	}

	public Long getCodeTypeSearch() {
		return codeTypeSearch;
	}
	
	public void setNumberDocSearch(String numberDocSearch) {
		this.numberDocSearch = numberDocSearch;
	}

	public String getNumberDocSearch() {
		return numberDocSearch;
	}

	public void setUserNameSearch(String userNameSearch) {
		this.userNameSearch = userNameSearch;
	}

	public String getUserNameSearch() {
		return userNameSearch;
	}
	
	public void setSecondNameSearch(String secondNameSearch) {
		this.secondNameSearch = secondNameSearch;
	}

	public String getSecondNameSearch() {
		return secondNameSearch;
	}

	public void setUserEmailSearch(String userEmailSearch) {
		this.userEmailSearch = userEmailSearch;
	}

	public String getUserEmailSearch() {
		return userEmailSearch;
	}

	public void setAccountIdSearch(String accountIdSearch) {
		this.accountIdSearch = accountIdSearch;
	}

	public String getAccountIdSearch() {
		return accountIdSearch;
	}

	public void setPlateSearch(String plateSearch) {
		this.plateSearch = plateSearch;
	}

	public String getPlateSearch() {
		return plateSearch;
	}

	

}