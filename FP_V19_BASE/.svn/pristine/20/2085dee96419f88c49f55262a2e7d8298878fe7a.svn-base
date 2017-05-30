
package mBeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import sessionVar.SessionUtil;
import util.UsrRole;
import ejb.Role;

/**
 * @author Cristian Avellaneda
 * 
 */
public class FormInsUserBean implements Serializable{
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

	private String userNames="";
	
	private String foreignCount="";
	
	private String foreignCity="";

	private String userSecondNames="";

	private String userPhone1="";

	private String userPhone2="";

	private String userAdress="";

	private String userCity;

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

	private boolean create;
	
	private boolean contractModal;
	
	private boolean aceptation;
	
	private long userId;
	
	private String msg;
	
	private String msgAcept;
	
	private boolean asigRole;
	
	private boolean aigRoleEnd;
	
	private String message;
	
	private boolean disabled;
	
	private String dv;
	
	private String contract;
	
	private String titleContract;
	
	private String foreign;
	
	private boolean aceptation2;
	
	private Boolean confirmExit=false;
	
	// Attributes --------------------------------------------------------------------------------

	
	// Constructor ----//

	public FormInsUserBean() {
			sTypePer = "1";
			listRoleString = new ArrayList<String>();
			listRol = new ArrayList<UsrRole>();
			listRoleSTarget = new ArrayList<String>();
			this.setUserPassword("Password");
			this.setSTypePer("1");
			this.setUserCodeType("1");
			this.setForePer(true);
	}
	

	// Actions -------//
	
	/**
	 * Changes the form according if is a natural person or a corporate body.
	 */
	public void changeTypePers(ValueChangeEvent event) {
		if (event.getNewValue().equals("2")) {
			sTypePer = event.getNewValue().toString();
			typePer = false;
			forePer = true;
			userCodeType = "NIT";
		} else {
			sTypePer = event.getNewValue().toString();
			typePer = true;
			forePer = true;
			userCodeType = "";
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
		if(userCodeType.equals("1")){
			forePer = true;
		}
		System.out.println(userCodeType);
	}
	
	
	/**
	 * Saves user roles.
	 */
	public String saveRole() {
		
		if(roleEjb.checkIfAnyUserRole(listRol, userId)){
			System.out.println("entre al if checkIfAnyUserRole");
			if(roleEjb.updateUserRole(listRol, userId)){
				System.out.println("entre al if updateUserRole");
				setMsg("Usuario y roles de usuario registrados con éxito.");
			} else {
				System.out.println("entre al else de updateUserRole");
				setMsg("Ha ocurrido un error al momento de inscribir el Usuario.");
			}
			this.asigRole = false;
		}else{
			System.out.println("entre al else de  checkIfAnyUserRole");
			setMsg("Debe seleccionar un rol.");	
		}	
		setAigRoleEnd(true);
		
		return null;
	}
	
	
private boolean validations(){
		

		if (userCodeType == "") {	
			setMsg("Seleccione el tipo de documento.");
			return false;
		}
		//------------------------validaciones de documento----------------

		else if(userCode == ""){
			setMsg("El No. de identificación es requerido.");
			return false;
		}
		
		else if(userCode.substring(0,1).equals("0")){
			setMsg("El No. de identificación no puede iniciar con 0.");
			return false;
		}

		else if(userCode.length()<6){
			setMsg("El No. identificación no debe ser menor a 6 ni mayor a 15 caracteres. Verifique");
			return false;
		}

		else if(!userCode.matches("([0-9]|\\s)+")){
			setMsg("El No. de identificación tiene caracteres inválidos. Verifique.");
			return false;
		}

		else if(userEjb.validateUserCodeUK(userCodeType.trim(), userCode.trim())) {
			setMsg("Existe un usuario registrado con el mismo número de documento. Verifique.");
			return false;
		}		
		
		//------------------------validaciones de Nombre------------------------------

		else if(userNames.trim().length() <= 0 ){
			setMsg("El nombre es requerido.");
			return false;
		}					

		else if(userNames.length()<3 || userNames.length()>100){
			setMsg("El nombre no debe ser menor a 3 ni mayor a 100 caracteres. Verifique");
			return false;	
		}
		
		else if(!userNames.matches("([a-z]|[A-Z]|[áéíóú]|[ÁÉÍÓÚ]|\\s)+")){
			setMsg("En nombre tiene caracteres inválidos. Verifique.");
			return false;
		}

		//------------------------validaciones de Apellido------------------------------



		else if(userSecondNames.trim().length() <= 0 ){
			setMsg("El apellido es requerido.");
			return false;
		}

		else if(userSecondNames.length()<3 || userSecondNames.length()>100){
			setMsg("El apellido no debe tener menos de 3 ni más de 50 caracteres. Verifique");
			return false;
		}
		
		else if(!userSecondNames.matches("([a-z]|[A-Z]|[áéíóú]|[ÁÉÍÓÚ]|[ñÑ]|\\s)+")){
			setMsg("El apellido tiene caracteres inválidos. Verifique.");
			return false;
		} 


		//------------------------validaciones de Usuario------------------------------   

		else if(userEmail == ""){
			setMsg("El usuario es requerido.");
			return false;
		}

		else if(userEmail.length()<5 || userEmail.length()>100){
			setMsg("El correo electrónico del campo usuario no debe tener menos de 5 ni más de 100 caracteres. Verifique");
			return false;
		}

		else if(userEmail.equals("ejemplo@mail.com")){
			setMsg("El usuario es requerido. ");
			return false;
		}

		else if(!this.isEmail(userEmail)){
			setMsg("El usuario tiene un correo inválido. Verifique");
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

			setMsg("El usuario y el usuario de confirmación deben ser iguales. Verifique");
			return false;
		}

		//validate that email doesn't exist.
		else if(userEjb.validateUserEmailUK(userEmail.toLowerCase())){
			setMsg("Existe un usuario con el mismo correo electrónico. Verifique.");
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
	
	/**
	 * Saves a system user.
	 */
	public String saveUser() {
		if(validations()){ 
				userId = this.saveSystemUser();
				if(userId != -1){
					clearVar();
					listRol = roleEjb.getCreateUsrRole(userId);
					setAsigRole(true);
				}else{
					setMsg("Ha ocurrido un error al momento de inscribir el Usuario.");
					setCreate(true);
				}
		} else {
			setCreate(true);
		}	
		return null;
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
	 * Saves an UserSystem.
	 * @return userId created.
	 */
	private Long saveSystemUser(){
		this.setUserEmail(this.getUserEmail().toLowerCase());
		return userEjb.createSystemUser(userCode.trim(), userNames.trim().toUpperCase(),
				userSecondNames.trim().toUpperCase(), userEmail.trim().toLowerCase(), userCodeType,
				SessionUtil.sessionUser().getUserId(), SessionUtil.ip());
	}
	/**
	 * Control Modal Panel
	 */
	public String hideModal() {
		this.setCreate(false);
		create=false;
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
			context.getExternalContext().getSessionMap().remove("formInsUserBean");
			return "createUser";
			}
			else return null;
	}
	
	
	public void hideModal2() {
		this.setCreate(false);
		create=false;
		if(msg!=null){
			if(msg.equals("Usuario y roles de usuario registrados con éxito.")){
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

	/**
	 * Clears variables.
	 */
	public void clearVar() {
	    if(msg!=null){
	    	System.out.println("msg :" + msg);
	    	if(msg.equals("Registro Exitoso") || msg.equals("Usuario registrado con éxito.") || msg.equals("Usuario y roles de usuario registrados con éxito.")){
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
	    	}
	    }
		
	}
	
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

	public boolean isContractModal() {
		return contractModal;
	}

	public void setContractModal(boolean contractModal) {
		this.contractModal = contractModal;
	}

	public String getContract() {
		return contract;
	}

	public void setContract(String contract) {
		this.contract = contract;
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

	public String getTitleContract() {
		return titleContract;
	}

	public void setTitleContract(String titleContract) {
		this.titleContract = titleContract;
	}


	public void setAceptation2(boolean aceptation2) {
		this.aceptation2 = aceptation2;
	}


	public boolean isAceptation2() {
		return aceptation2;
	}
	
}