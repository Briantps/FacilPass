
package mBeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import sessionVar.SessionUtil;
import util.Cities;
import util.UsrRole;
import ejb.Role;

/**
 * @author Cristian Avellaneda
 * 
 */
public class FormInsBean implements Serializable{
	private static final long serialVersionUID = 1L;

	// Attributes --------------------------------------------------------------------------------

	private boolean typePer;

	private boolean forePer = true;

	private String sTypePer="1";

	private String userCode="";

	private String legalCode="";

	private String countryRS="";

	private String legalCodeType;

	private String userCodeType;

	private String userCodeTypeAux;

	private String userNames="";

	private String foreignCount="";

	private String foreignCity="";

	private String userSecondNames="";

	private String userPhone1="";

	private String userPhone2="";

	private String userAdress="";

	private String userCity="";

	private String userEmail="ejemplo@mail.com";

	private String userPassword="";

	private String userEmail2="ejemplo@mail.com";

	private String userPassword2="";

	private String userCodeTypeNit="";

	private String userNit;

	private String razSoc;

	private String nombreR;

	private List<UsrRole> listRol;

	private List<SelectItem> listRoles;

	private List<jpa.TbRole> listRolesAsig;

	private List<String> listRoleString;

	private List<String> listRoleSTarget;

	@EJB(mappedName="ejb/Role")
	private Role roleEjb;

	@EJB(mappedName="ejb/User")
	private ejb.User userEjb;

	@PersistenceContext(unitName = "bo") 
	EntityManager em;

	private boolean create;

	private boolean modal;

	private boolean aceptation;

	private long userId;

	private String msg;

	private String msgAcept;

	private List<SelectItem> cities;

	private boolean asigRole;

	private boolean aigRoleEnd;

	private String message;

	private boolean disabled;

	private String dv;

	private String foreign;

	private boolean aceptation2;

	private Boolean confirmExit=false;

	private List<Cities> auxCities;

	private boolean showModalInfo;

	private boolean showModalConfir;


	// Attributes --------------------------------------------------------------------------------


	// Constructor ----//

	public FormInsBean() {
		sTypePer = "1";
		listRoleString = new ArrayList<String>();
		listRol = new ArrayList<UsrRole>();
		listRoleSTarget = new ArrayList<String>();
		this.setUserPassword("Password");
		this.setSTypePer("1");
		this.setUserCodeType("1");
		this.setForePer(true);
	}

	@SuppressWarnings("unused")
	@PostConstruct
	private void init() {
		this.setUserId(-1L);
		boolean validate=false;
		this.setShowModalConfir(false);
		this.auxCities = userEjb.getCities();
	}

	// Actions -------//
	/**
	 * Changes the form according if is a natural person or a corporate body.
	 */
	public void changeTypePers(ValueChangeEvent event) {
		if (event.getNewValue().equals("3")) {
			sTypePer = "2";	
			typePer = false;
			forePer = true;
			setUserCodeType("NIT");
			userCodeTypeAux = "NIT";
			System.out.println("LLEGO CON:"+event.getNewValue()+"  SE CABIO A NIT:"+getUserCodeType());
		} else {
			sTypePer = event.getNewValue().toString();
			typePer = true;
			forePer = true;
			setUserCodeType("");
			userCodeTypeAux = "";
		}
	}

	/**
	 * Changes the form according if the location is foreign.
	 */
	public void changeLocation(ValueChangeEvent event) {
		if (event.getNewValue().equals("0")) {
			forePer = false;
			foreign = "Extranjero";
		} else {
			forePer = true;
			foreign = "";
		}
	}

	/**
	 * Changes the form according if the location is foreign.
	 */
	public void changeTypeID(ValueChangeEvent event) {
	
		userCodeType=event.getNewValue().toString();
		System.out.print("eveto del comboBox userCodeType:"+userCodeType);
		System.out.print("eveto del comboBox :"+event.getNewValue().toString());
		this.changeTypePers(event);
		if(userCodeType.equals("1")){
			forePer = true;
		}
		System.out.println(userCodeType);
	}


	private boolean validations(){
		Boolean wrongPhone=false;
		Boolean wrongPhone2=false;

		try {
			int phoneUser = Integer.parseInt(userPhone1);
			if(phoneUser<=10){
				wrongPhone=true;
			}
		} catch (Exception e) {
			System.out.println("Numero con extension.");
		}

		if(!"".equals(userPhone2)){
			try {
				int phoneUser2 = Integer.parseInt(userPhone2);
				if(phoneUser2<=10){
					wrongPhone2=true;
				}
			} catch (Exception e) {
				System.out.println("Numero con extension.");
			}
		}

		if (userCodeType == "") {	
			setMsg("Seleccione el Tipo de Documento.");
			return false;
		}
		//------------------------validaciones de documento,NIT y dÌgito de verificacion----------------
		else if((userCodeType.equals("3")) && (userCode == "")){
			setMsg("El NIT es requerido.");
			return false;	
		}
		else if(( !userCodeType.equals("3")) && ( userCode == "")){
			setMsg("El No. de identificaciÛn es requerido.");
			return false;
		}

		else if((userCodeType.equals("3")) && userCode.length()<6){
			setMsg("El NIT no debe ser menor a 6 ni mayor a 15 caracteres. Verifique");
			return false;
		}

		else if((!userCodeType.equals("3"))&& userCode.length()<6){
			setMsg("El No. de identificaciÛn no debe ser menor a 6 ni mayor a 15 caracteres. Verifique");
			return false;
		}


		else if( userCodeType.equals("3") && userCode.substring(0,1).equals("0")){
			setMsg("El NIT no puede iniciar con 0.");
			return false;
		}

		else if((!userCodeType.equals("3")) && userCode.substring(0,1).equals("0")){
			setMsg("El No. de identificaciÛn no puede iniciar con 0.");
			return false;
		}

		else if((userCodeType.equals("3")) && !userCode.matches("([0-9]|\\s)+")){
			setMsg("El NIT tiene caracteres inv·lidos. Verifique.");
			return false;
		}

		else if((!userCodeType.equals("3")) && !userCode.matches("([0-9]|\\s)+")){
			setMsg("El No. de identificaciÛn tiene caracteres inv·lidos. Verifique.");
			return false;
		}

		else if((userCodeType.equals("3")) && "".equals(dv.trim())){
			setMsg("El dÌgito de verificaciÛn es requerido.");
			return false;
		}

		else if((userCodeType.equals("3")) && !dv.matches("([0-9]|\\s)+")){
			setMsg("El dÌgito de verificaciÛn debe ser numÈrico y de un dÌgito. Verifique.");
			return false;
		}

		if(userCodeType.equals("3")){
			String nit = userCode+dv;
			System.out.println("el nit:"+nit);
			if(userEjb.validateUserCodeUK(userCodeType.trim(),nit)){
				setMsg("Existe un usuario registrado con el mismo n˙mero de Nit. Verifique.");
				return false;
			}
		}

		else {
			if(userEjb.validateUserCodeUK(userCodeType.trim(), userCode.trim())){
				setMsg("Existe un usuario registrado con el mismo n˙mero de documento. Verifique.");
				return false;
			}
		}
		//------------------------validaciones de Nombre y Razon Social------------------------------

		if((!userCodeType.equals("3")) && userNames.trim().length() <= 0 ){
			setMsg("El nombre es requerido.");
			return false;
		}		

		else if((userCodeType.equals("3")) && userNames.trim().length() <= 0 ){
			setMsg("La razÛn social es requerida.");
			return false;
		}				

		else if((userCodeType.equals("3")) && (userNames.length()<3 || userNames.length()>100)){
			setMsg("La razÛn social no debe ser menor a 3 ni mayor a 100 caracteres. Verifique");
			return false;
		}

		else if((!userCodeType.equals("3")) && (userNames.length()<3 || userNames.length()>100)){
			setMsg("El nombre no debe ser menor a 3 ni mayor a 100 caracteres. Verifique");
			return false;	
		}

		else if((!userCodeType.equals("3")) && !userNames.matches("([a-z]|[A-Z]|[·ÈÌÛ˙]|[¡…Õ”⁄]|[Ò—]|\\s)+")){
			setMsg("El nombre tiene caracteres inv·lidos. Verifique.");
			return false;
		}

		else if((userCodeType.equals("3")) && !userNames.matches("([a-z]|[A-Z]|[·ÈÌÛ˙]|[¡…Õ”⁄]|[0-9]|[&/]|[_-]|[.]|[Ò—]|\\s)+")){
			setMsg("La razÛn social tiene caracteres inv·lidos. Verifique.");
			return false;
		}

		//------------------------validaciones de Apellido y Nombre Representante legal------------------------------


		else if((!userCodeType.equals("3")) && userSecondNames.trim().length() <= 0 ){
			setMsg("El apellido es requerido.");
			return false;
		}

		else if((userCodeType.equals("3")) && userSecondNames.trim().length() <= 0 ){
			setMsg("El nombre del representante legal es requerido.");
			return false;
		} 

		else if((!userCodeType.equals("3")) && (userSecondNames.length()<3 || userSecondNames.length()>100)){
			setMsg("El apellido no debe tener menos de 3 ni m·s de 50 caracteres. Verifique");
			return false;
		}

		else if((userCodeType.equals("3")) && (userSecondNames.length()<3 || userSecondNames.length()>100)){
			setMsg("El nombre del representante legal no debe tener menos de 3 ni m·s de 50 caracteres. Verifique");
			return false;
		}

		else if((!userCodeType.equals("3")) && !userSecondNames.matches("([a-z]|[A-Z]|[·ÈÌÛ˙]|[¡…Õ”⁄]|[Ò—]|\\s)+")){
			setMsg("El apellido tiene caracteres inv·lidos. Verifique.");
			return false;
		}

		else if((userCodeType.equals("3")) && !userSecondNames.matches("([a-z]|[A-Z]|[·ÈÌÛ˙]|[¡…Õ”⁄]|[Ò—]|\\s)+")){
			setMsg("El nombre del representante legal tiene caracteres inv·lidos. Verifique.");
			return false;
		}

		//------------------------validaciones Pais constitucion Razon Social------------------------------  
		else if((userCodeType.equals("3")) && countryRS == ""){
			setMsg("El paÌs de constituciÛn de la razÛn social es requerido.");
			return false;
		}

		else if((userCodeType.equals("3")) && !countryRS.matches("([a-z]|[A-Z]|[·ÈÌÛ˙]|[¡…Õ”⁄]|[Ò—]|\\s)+")){
			setMsg("El paÌs de constituciÛn de la razÛn social tiene caracteres inv·lidos. Verifique.");
			return false;
		}

		//------------------------validaciones de Identificacion representante legal-----------------------
		else if((userCodeType.equals("3")) && legalCode == ""){
			setMsg("El No. de identificaciÛn del representante legal es requerido.");
			return false;
		}

		else if((userCodeType.equals("3")) && legalCode.length()<6){
			setMsg("El No. identificaciÛn del representante legal debe ser mÌnimo de 6 dÌgitos.");
			return false;
		}

		else if((userCodeType.equals("3")) && !legalCode.matches("([0-9]|\\s)+")){
			setMsg("El No. de identificaciÛn del representante legal tiene caracteres inv·lidos. Verifique.");
			return false;
		}

		//------------------------validaciones de Direccion------------------------------ 
		else if(userAdress == ""){
			setMsg("La direcciÛn es requerida.");
			return false;
		}

		else if(userAdress.length()<7 || userAdress.length()>100){
			setMsg("La direcciÛn no debe tener menos de 7 ni m·s de 100 caracteres. Verifique");
			return false;
		}

		//------------------------validaciones de Ciudad Extranjero y Pais Extranjero------------------------------   

		else if(userCity.equals("0") && foreignCity == ""){
			setMsg("La ciudad en el extranjero es requerida.");
			return false;
		}
		else if(userCity.equals("0") && foreignCount == ""){
			setMsg("El paÌs en el extranjero es requerido.");
			return false;
		} 

		else if(userCity.equals("0") && !foreignCity.matches("([a-z]|[A-Z]|[·ÈÌÛ˙]|[¡…Õ”⁄]|[Ò—]|\\s)+")){
			setMsg("La ciudad en el extranjero tiene caracteres inv·lidos. Verifique.");
			return false;
		}

		else if(userCity.equals("0") && !foreignCount.matches("([a-z]|[A-Z]|[·ÈÌÛ˙]|[¡…Õ”⁄]|[Ò—]|\\s)+")){
			setMsg("El paÌs en el extranjero tiene caracteres inv·lidos. Verifique.");
			return false;
		}

		//------------------------validaciones de Celuar------------------------------ 

		else if(userPhone1 == ""){
			setMsg("El celular es requerido.");
			return false;
		}				

		else if(userPhone1.length()<7 || userPhone1.length()>30){
			setMsg("El celular no debe tener menos de 7 ni m·s de 30 caracteres. Verifique");
			return false;
		}

		else if(wrongPhone){
			setMsg("El numero "+userPhone1+" no es un celular valido. Verifique");
			return false;
		}

		else if(!userPhone1.matches("([0-9]|\\s)+")){
			setMsg("El celular tiene caracteres inv·lidos. Verifique.");
			return false;
		}

		//------------------------validaciones de Telefono opcional------------------------------ 	

		else if(userPhone2!="" && (userPhone2.length()<7 || userPhone2.length()>30)){
			setMsg("El telÈfono opcional no debe tener menos de 7 ni m·s de 30 caracteres. Verifique");
			return false;
		}	

		else if(userPhone2!="" && wrongPhone2){
			setMsg("El numero "+userPhone2+" no es un telÈfono opcional valido. Verifique");				
			return false;
		}

		else if(userPhone2!="" && (!userPhone2.matches("([0-9]|[a-z]|[A-Z]|\\s)+"))){
			setMsg("El telÈfono opcional tiene caracteres inv·lidos. Verifique.");
			return false;
		}

		//------------------------validaciones de Usuario------------------------------   

		else if(userEmail == ""){
			setMsg("El usuario es requerido.");
			return false;
		}

		else if(userEmail.length()<5 || userEmail.length()>100){
			setMsg("El correo electrÛnico del campo usuario no debe tener menos de 5 ni m·s de 100 caracteres. Verifique");
			return false;
		}

		else if(userEmail.equals("ejemplo@mail.com")){
			setMsg("El usuario es requerido. ");
			return false;
		}

		else if(!this.isEmail(userEmail)){
			setMsg("El usuario tiene un correo inv·lido. Verifique");
			return false;
		}

		//------------------------validaciones de Confirmar Usuario------------------------------  

		else if(userEmail2 == "" || userEmail2.equals("ejemplo@mail.com")){
			setMsg("El campo confirmar usuario es requerido.");
			return false;
		}

		else if(!userEmail.toLowerCase().equals(userEmail2.toLowerCase())){
			System.out.println("Entro a validar los correos");
			System.out.println("userEmail " +userEmail);
			System.out.println("userEmail2 "+userEmail2);

			setMsg("El usuario y el usuario de confirmaciÛn deben ser iguales. Verifique");
			return false;
		}

		//validate that email doesn't exist.
		else if(userEjb.validateUserEmailUK(userEmail.toLowerCase())){
			setMsg("Existe un usuario con el mismo correo electrÛnico. Verifique.");
			return false;
		}		
		return true;
	}


	public boolean isEmail(String correo) {
		Pattern pat = null;
		Matcher mat = null; 
		pat = Pattern.compile("^[A-Za-z0-9._+-]+@[A-Za-z0-9.-]+\\.[a-zA-Z]{2,4}$"); 
		mat = pat.matcher(correo);

		System.out.println("res en isEmail " + mat.matches());
		return mat.matches();
	}

	public String validate(){
		System.out.println("validate---");
		System.out.println("userCode: "+userCode);
		System.out.println("userNames: "+userNames);
		System.out.println("userSecondNames: "+userSecondNames);
		System.out.println("userAdress: "+userAdress);
		System.out.println("userCity: "+userCity);
		System.out.println("userPhone1: "+userPhone1);
		System.out.println("userPhone2: "+userPhone2);
		System.out.println("userEmail: "+userEmail);
		System.out.println("userEmail2: "+userEmail2);
		System.out.println("userCodeType: "+userCodeType);
		this.setShowModalConfir(false);
		if (validations()) {
			this.setShowModalInfo(false);
			this.setMsg("øEst· seguro de realizar la creaciÛn del cliente?.");
			this.setShowModalInfo(false);
			setModal(false);
			this.setShowModalConfir(true);
		}else{
			setCreate(false);
			this.setShowModalInfo(false);
			setModal(true);
		}
		System.out.println("Final.validate");
		return null;
	}

	/**
	 * Saves a client.
	 */
	public String saveClien() {
		System.out.println("saveClien---");
		this.setShowModalConfir(false);
		System.out.print("saveClient  changeTypePers:"+userCodeType);
		if(userCodeType.equals("3")){
			setMessage(this.save(false));
			setMsg(message);
			setAceptation2(false);
			if(msg.equals("Registro Exitoso") || msg.equals("Usuario registrado con Èxito.") || msg.equals("Usuario y roles de usuario registrados con Èxito.")){
				setModal(true);
				setCreate(false);
			}else{
				setModal(true);
				setCreate(false);
			}

			userCode=userCode+dv;
			System.out.println("se creo cliente persona juridica");
			confirmExit=true;
		}else{
			setMessage(this.save(false));
			setMsg(message);
			setAceptation(false);
			if(msg.equals("Registro Exitoso") || msg.equals("Usuario registrado con Èxito.") || msg.equals("Usuario y roles de usuario registrados con Èxito.")){
				setModal(true);
				setCreate(false);
			}else{
				setModal(true);
				setCreate(false);
			}
			System.out.println("se creo cliente persona natural");
			confirmExit=true;
		}
		return null;
	}


	private String save(boolean outside){

		Long id = null;
				System.out.println("userCodeType:"+getUserCodeType());
				System.out.println("userCode:"+userCode);
				System.out.println("dv:"+dv);
				System.out.println("");
				
				
		if (!outside) { // A system User is creating the client
			id = SessionUtil.sessionUser().getUserId();
			if(userCodeType.equals("3")){
				userPassword = userCode+dv;
			}else{
				userPassword = userCode;
			}
		}
		this.setUserEmail(this.getUserEmail().toLowerCase());
		System.out.print("userCodeType" + userCodeType);
		String uct= (userCodeType.equals("NIT")?"3":userCodeType);
		System.out.print("uct" + uct);
		if(userCodeType.equals("NIT")||userCodeType.equals("3")){
			foreignCount=countryRS;
		}
		return userEjb.createClient(outside, id, SessionUtil.ip(), uct
				.trim(), userCode.trim(), userNames.trim().toUpperCase(), userSecondNames.trim()
				.toUpperCase(), userPhone1.trim(), userPhone2.trim(), userAdress.trim()
				.toUpperCase(), userCity.trim(), userEmail.trim().toLowerCase(), userPassword.trim(), dv,
				legalCodeType,legalCode.trim(),foreignCity.trim().toUpperCase(),foreignCount.trim().toUpperCase());
	}

	public boolean validatePassword(boolean outside, String password){
		boolean res=false;
		if(outside){
			if(userPassword==""){
				res=false;
			}
			else{
				res=true;
			}
		}
		else{
			res=true;
		}

		return res;
	}

	/**
	 * Control Modal Panel
	 */
	public String hideModal() {
		this.setShowModalInfo(false);
		this.setShowModalConfir(false);
		this.setCreate(false);
		this.setModal(false);
		create=false;
		modal=false;
		setAsigRole(false);
		setAigRoleEnd(false);

		System.out.println("msg en hideModal " + msg);
		System.out.println("message en hideModal " + message);
		if(msg!=null){
			if(msg.equals("Registro Exitoso")){
				System.out.println("disabled true");
				this.setDisabled(true);
			}
			else{
				this.setDisabled(false);
			}
		}
		System.out.println("disabled " + disabled);
		this.clearVar();

		if(confirmExit){
			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().getSessionMap().remove("formInsBean");
			return "createClient";
		}
		else return null;
	}

	public void hideModalConfir(){
		System.out.println("off modal contract");
		this.setShowModalConfir(false);
	}

	public void hideModal2() {
		this.setShowModalConfir(false);
		this.setCreate(false);
		this.setModal(false);
		create=false;
		modal=false;
		if(msg!=null){
			if(msg.equals("Usuario y roles de usuario registrados con Èxito.")){
				System.out.println("Entre al if hideModal2");
				this.setAsigRole(false);
				this.clearVar();
			}
			else if (msg.equals("Debe seleccionar un rol.")){
				System.out.println("Entre al else if hideModal2");

			}
			setAigRoleEnd(false);
		}


	}

	public void clear(){
		System.out.println("Entre aqui");
		setUserCode("");
		if(userCodeType.equals("NIT") || userCodeType.equals("3")){
			setUserCodeType("NIT");
			setSTypePer("2");
		}else {
			setUserCodeType("1");
			setSTypePer("1");
		}
		setUserNames("");
		setUserSecondNames("");
		setUserPhone1("");
		setUserPhone2("");
		setUserAdress("");
		setUserCity("");
		setUserEmail("");
		setUserEmail2("");
		setUserCodeTypeNit("");
		setUserNit("");
		setRazSoc("");
		setNombreR("");
		setForeignCity("");
		setForeignCount("");
		setCountryRS("");
		setLegalCode("");
		setForePer(true);
		this.setDv("");
	}

	public String cancelAceptation(){
		this.clear();
		return null;
	}

	/**
	 * Clears variables.
	 */
	public void clearVar() {
		if(msg!=null){
			System.out.println("msg :" + msg);
			if(msg.equals("Registro Exitoso") || msg.equals("Usuario registrado con Èxito.") || msg.equals("Usuario y roles de usuario registrados con Èxito.")){
				System.out.println("Entre aqui1");
				setUserCode("");
				if(userCodeType.equals("NIT") || userCodeType.equals("3")){
					setUserCodeType("NIT");
					setSTypePer("2");
				}else {
					setUserCodeType("1");
					setSTypePer("1");
				}
				setUserNames("");
				setUserSecondNames("");
				setUserPhone1("");
				setUserPhone2("");
				setUserAdress("");
				setUserCity("");
				setUserEmail("");
				setUserEmail2("");
				setUserCodeTypeNit("");
				setUserNit("");
				setRazSoc("");
				setNombreR("");
				setForeignCity("");
				setForeignCount("");
				setCountryRS("");
				setLegalCode("");
				setForePer(true);
				this.setDv("");
				this.setUserId(0L);
			} 
		}
	}
	//-------------------------

	// Getters and Setters ----------//
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserCodeTypeNit() {
		return userCodeTypeNit;
	}

	public void setUserCodeTypeNit(String userCodeTypeNit) {
		this.userCodeTypeNit = userCodeTypeNit;
	}

	public String getUserNit() {
		return userNit;
	}

	public void setUserNit(String userNit) {
		this.userNit = userNit;
	}

	public String getRazSoc() {
		return razSoc;
	}

	public void setRazSoc(String razSoc) {
		this.razSoc = razSoc;
	}

	public String getNombreR() {
		return nombreR;
	}

	public void setNombreR(String nombreR) {
		this.nombreR = nombreR;
	}

	public void setTypePer(boolean typePer) {
		this.typePer = typePer;
	}

	public boolean isTypePer() {
		return typePer;
	}

	public void setSTypePer(String sTypePer) {
		this.sTypePer = sTypePer;
	}

	public String getSTypePer() {
		return sTypePer;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getUserCode() {
		return userCode;
	}

	public String getUserCodeType() {
		return userCodeType;
	}

	public void setUserCodeType(String userCodeType) {
		this.userCodeType = userCodeType;
	}

	public String getUserNames() {
		return userNames;
	}

	public void setUserNames(String userNames) {
		this.userNames = userNames;
	}

	public String getUserSecondNames() {
		return userSecondNames;
	}

	public void setUserSecondNames(String userSecondNames) {
		this.userSecondNames = userSecondNames;
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

	public String getUserCity() {
		return userCity;
	}

	public void setUserCity(String userCity) {
		this.userCity = userCity;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public List<SelectItem> getListRoles() {
		return listRoles;
	}

	public void setListRoles(List<SelectItem> listRoles) {
		this.listRoles = listRoles;
	}

	public Role getRoleEjb() {
		return roleEjb;
	}

	public void setRoleEjb(Role roleEjb) {
		this.roleEjb = roleEjb;
	}

	public void setCreate(boolean create) {
		this.create = create;
	}

	public boolean isCreate() {
		return create;
	}

	public void setListRolesAsig(List<jpa.TbRole> listRolesAsig) {
		this.listRolesAsig = listRolesAsig;
	}

	public List<jpa.TbRole> getListRolesAsig() {
		return listRolesAsig;
	}

	public void setListRol(List<UsrRole> listRol) {
		this.listRol = listRol;
	}

	public List<UsrRole> getListRol() {
		return listRol;
	}

	public void setListRoleString(List<String> listRoleString) {
		this.listRoleString = listRoleString;
	}

	public List<String> getListRoleString() {
		return listRoleString;
	}

	public void setListRoleSTarget(List<String> listRoleSTarget) {
		this.listRoleSTarget = listRoleSTarget;
	}

	public List<String> getListRoleSTarget() {
		return listRoleSTarget;
	}

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
		List<Cities> c = auxCities;
		String city;
		for(Cities ci : c){
			city=ci.getCity() + " - "  + "(" + ci.getDepartment().substring(0,4) + ")";
			if(ci.getCityCode().equals("0")){
				if(userCodeType.equals("2")){
					cities.add(new SelectItem(ci.getCityCode(),city));
				}
			}else{
				cities.add(new SelectItem(ci.getCityCode(),city));
			}
		}
		return cities;
	}

	/**
	 * @param asigRole the asigRole to set
	 */
	public void setAsigRole(boolean asigRole) {
		this.asigRole = asigRole;
	}

	/**
	 * @return the asigRole
	 */
	public boolean isAsigRole() {
		return asigRole;
	}

	/**
	 * @param aigRoleEnd the aigRoleEnd to set
	 */
	public void setAigRoleEnd(boolean aigRoleEnd) {
		this.aigRoleEnd = aigRoleEnd;
	}

	/**
	 * @return the aigRoleEnd
	 */
	public boolean isAigRoleEnd() {
		return aigRoleEnd;
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
	 * @param disabled the disabled to set
	 */
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	/**
	 * @return the disabled
	 */
	public boolean isDisabled() {
		return disabled;
	}

	/**
	 * @param userEmail2 the userEmail2 to set
	 */
	public void setUserEmail2(String userEmail2) {
		this.userEmail2 = userEmail2;
	}

	/**
	 * @return the userEmail2
	 */
	public String getUserEmail2() {
		return userEmail2;
	}

	/**
	 * @param userPassword2 the userPassword2 to set
	 */
	public void setUserPassword2(String userPassword2) {
		this.userPassword2 = userPassword2;
	}

	/**
	 * @return the userPassword2
	 */
	public String getUserPassword2() {
		return userPassword2;
	}

	/**
	 * @param dv the dv to set
	 */
	public void setDv(String dv) {
		this.dv = dv;
	}

	/**
	 * @return the dv
	 */
	public String getDv() {
		return dv;
	}

	public boolean isForePer() {
		return forePer;
	}

	public void setForePer(boolean forePer) {
		this.forePer = forePer;
	}

	public String getForeignCount() {
		return foreignCount;
	}

	public void setForeignCount(String foreignCount) {
		this.foreignCount = foreignCount;
	}

	public String getForeignCity() {
		return foreignCity;
	}

	public void setForeignCity(String foreignCity) {
		this.foreignCity = foreignCity;
	}

	public String getForeign() {
		return foreign;
	}

	public void setForeign(String foreign) {
		this.foreign = foreign;
	}

	public String getLegalCode() {
		return legalCode;
	}

	public void setLegalCode(String legalCode) {
		this.legalCode = legalCode;
	}

	public String getLegalCodeType() {
		return legalCodeType;
	}

	public void setLegalCodeType(String legalCodeType) {
		this.legalCodeType = legalCodeType;
	}

	public String getCountryRS() {
		return countryRS;
	}

	public void setCountryRS(String countryRS) {
		this.countryRS = countryRS;
	}

	public String getMsgAcept() {
		return msgAcept;
	}

	public void setMsgAcept(String msgAcept) {
		this.msgAcept = msgAcept;
	}

	public boolean isAceptation() {
		return aceptation;
	}

	public void setAceptation(boolean aceptation) {
		this.aceptation = aceptation;
	}

	public void setAceptation2(boolean aceptation2) {
		this.aceptation2 = aceptation2;
	}


	public boolean isAceptation2() {
		return aceptation2;
	}


	public void setUserCodeTypeAux(String userCodeTypeAux) {
		this.userCodeTypeAux = userCodeTypeAux;
	}


	public String getUserCodeTypeAux() {
		return userCodeTypeAux;
	}

	public boolean isModal() {
		return modal;
	}

	public void setModal(boolean modal) {
		this.modal = modal;
	}

	public boolean isShowModalInfo() {
		return showModalInfo;
	}

	public void setShowModalInfo(boolean showModalInfo) {
		this.showModalInfo = showModalInfo;
	}

	public boolean isShowModalConfir() {
		return showModalConfir;
	}

	public void setShowModalConfir(boolean showModalConfir) {
		this.showModalConfir = showModalConfir;
	}

}