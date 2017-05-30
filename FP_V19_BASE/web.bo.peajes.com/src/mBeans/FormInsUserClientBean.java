
package mBeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import jpa.TbRole;

import sessionVar.SessionUtil;
import util.Cities;
import util.UsrRole;
import ejb.DataPolicies;
import ejb.Role;

/**
 * @author Cristian Avellaneda
 * 
 */
public class FormInsUserClientBean implements Serializable{
	private static final long serialVersionUID = 1L;
	
	// Attributes --------------------------------------------------------------------------------
	@EJB(mappedName="ejb/Role")
	private Role roleEJB;
	
	@EJB(mappedName="ejb/User")
	private ejb.User userEJB;
	
	@EJB(mappedName="ejb/DataPolicies")
	private DataPolicies dataPoliciesEJB;
	
	private List<SelectItem> roleClientList;
	private Long roleClientId;
	
	private boolean typePer;

	private String sTypePer="1";

	private String userCode="";

	private String userCodeType="1";

	private String userNames="";

	private String userSecondNames="";

	private String userPhone1="";

	private String userPhone2="";

	private String userAdress="";

	private String userCity;

	private String userEmail="ejemplo@mail.com";

	private String userPassword="";
	
	private String userEmail2="ejemplo@mail.com";

	private String userPassword2="";
	
	private String policiesNatura;

	private List<UsrRole> listRol;

	private List<SelectItem> listRoles;

	private List<jpa.TbRole> listRolesAsig;

	private List<String> listRoleString;

	private List<String> listRoleSTarget;


	private boolean create;
	
	private long userId;
	
	private String msg;
	
	private List<SelectItem> cities;
	
	private boolean asigRole;
	
	private boolean aigRoleEnd;
	
	private boolean aceptPersonCient;
	
	private boolean enablesend = false;
	
	private String message;
	
	private boolean disabled;
	
	private String dv;
	
	private boolean forePer = false;
	
	private String foreign;
	
	private String foreignCity="";
	
	private String foreignCount="";
	
   // Constructor ----//

	public FormInsUserClientBean() {
	}

	// Actions -------//
	
	private boolean validations(){
		Boolean wrongPhone=false;
		Boolean wrongPhone2=false;		
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
		if (userCodeType == "") {	
			setMsg("Seleccione el Tipo de Documento.");
			return false;
		}
		else if(userCode == ""){
			setMsg("El campo No. de Identificación es requerido.");
			return false;
		}
		else if(userCode.substring(0,1).equals("0")){
			setMsg("El campo No. de Identificación no debe iniciar con cero (0).");
			return false;
		}
		else if(userCode!="" && (!userCode.matches("([0-9])+"))){
			setMsg("El campo No. de Identificación tiene caracteres inválidos. Verifique.");
			return false;
		}
		if(userCode.length()<6 || userCode.length()>15){
			setMsg("El campo No. de Identificación no debe ser menor a 6 ni mayor a 15 caracteres. Verifique.");
			return false;
		}
		else if (userNames.trim().length() <= 0) {
			String mess = "El campo Nombre(s) es requerido.";
			setMsg(mess);
			return false;
		}
		else if (userNames!="" && (!userNames.matches("([a-z]|[A-Z]|[áéíóú]|[ÁÉÍÓÚ]|[ñÑ]|\\s)+"))){
			String mess = "El campo Nombre(s) tiene caracteres inválidos. Verifique.";
			setMsg(mess);
			return false;
		}
		else if(userNames.length()<3 || userNames.length()>100){
			setMsg("El campo Nombre(s) no debe ser menor a 3 ni mayor a 100 caracteres. Verifique.");
			return false;
		}
		else if (userSecondNames.trim().length() <= 0) {
			String mess = "El campo Apellidos es requerido.";
			setMsg(mess);
			return false;
		}
		else if (userSecondNames!="" && (!userSecondNames.matches("([a-z]|[A-Z]|[áéíóú]|[ÁÉÍÓÚ]|[ñÑ]|\\s)+"))){
			String mess = "El campo Apellidos tiene caracteres inválidos. Verifique.";
			setMsg(mess);
			return false;
		}
		else if(userSecondNames.length()<3 || userSecondNames.length()>100){
			setMsg("El Apellidos no debe tener menos de 3 ni más de 100 caracteres. Verifique.");
			return false;
		}
		else if(userAdress == ""){
			setMsg("El campo Dirección es requerido.");
			return false;
		}
		else if(userAdress.length()<7 || userAdress.length()>50){
			setMsg("El campo Dirección no debe tener menos de 7 ni más de 50 caracteres. Verifique.");
			return false;
		}
		else if(userPhone1 == ""){
			setMsg("El campo Celular es requerido.");
			return false;
		}
		else if(userPhone1!="" && (!userPhone1.matches("([0-9])+"))){
			setMsg("El campo Celular tiene caracteres inválidos. Verifique.");
			return false;
		}
		else if(userPhone1.length()<7 || userPhone1.length()>30){
			setMsg("El Celular no debe tener menos de 7 ni más de 30 caracteres. Verifique.");
			return false;
		}	
		else if(wrongPhone==true){
	 		setMsg("El número "+userPhone1+" no es un Celular válido.");
	 		return false;
		}
		else if(userPhone2!=""){
			if(userPhone2.length()<7 || userPhone2.length()>30){
				setMsg("El Teléfono Opcional no debe tener menos de 7 ni más de 30 caracteres. Verifique.");
				return false;
			}else if(wrongPhone2==true){
				setMsg("El número "+userPhone2+" no es un Teléfono válido.");
				return false;
			}else if(userPhone2!="" && (!userPhone2.matches("([0-9]|[a-z]|[A-Z]|\\s)+"))){
				setMsg("El campo Teléfono Opcional tiene caracteres inválidos. Verifique.");
				return false;
			}
		}
		if (userEmail.equals("ejemplo@mail.com") || userEmail2.equals("ejemplo@mail.com")){
			setMsg("El campo Usuario es requerido.");
			return false;
		}
		else if(userEmail == ""){
			setMsg("El campo Usuario es requerido.");
			return false;
		}
		else if(userEmail!="" && !userEmail.matches("^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\\.[a-zA-Z]{2,4}$")){
			setMsg("El campo Usuario tiene caracteres inválidos. Verifique.");
			return false;
		}
		else if(userEmail.length()<5 || userEmail.length()>100){
			setMsg("El Correo Electrónico no debe tener menos de 5 ni más de 100 caracteres. Verifique.");
			return false;
		}
		else if(userEmail2 == ""){
			setMsg("El campo Confirmar Usuario es requerido.");
			return false;
		}
		else if(!userEmail.equals(userEmail2)){
			setMsg("El Usuario y el Usuario de confirmación deben ser iguales. Verifique.");
			return false;
		}		
		else if(userEmail2!="" && !userEmail2.matches("^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\\.[a-zA-Z]{2,4}$")){
			setMsg("El campo Confirmar Usuario tiene caracteres inválidos. Verifique.");
			return false;
		}else if (!aceptPersonCient) {
			setMsg("Debe aceptar los Términos.");
			return false;
		}
		else {
			// validate if code + type code doesn't exist,	
			if(userEJB.validateUserCodeUK(userCodeType.trim(), userCode.trim())){
					setMsg("Existe un usuario registrado con el mismo No. de Identificación. Verifique.");
					return false;
			}
			//validate that email doesn't exist.
			if(userEJB.validateUserEmailUK(userEmail)){
						setMsg("Existe un usuario con el mismo Correo Electrónico. Verifique.");
						return false;
			}	
		}
		return true;
	}
	 
	public void activatebutton(ValueChangeEvent event) {
		System.out.println("Entre a Activar o inactivar el boton Send");
		if (((Boolean)event.getNewValue())){
			this.setEnablesend(true);
		}else {
			this.setEnablesend(false);
		}
						
	}
   
   /**
	 * Saves a system user.
	 */
  public String saveUserClient() {
	   if(validations()){
			if(userId != -1){
				if(userEJB.createUserRoleMaster(userCode, userCodeType, userNames.toUpperCase(), 
						userSecondNames.toUpperCase(), userPhone1, userPhone2, 
						userAdress.toUpperCase(),userCity, userEmail, roleClientId, 
						SessionUtil.ip(), SessionUtil.sessionUser().getUserId(),foreignCity,foreignCount)==true){
				setMsg("Usuario y rol de usuario registrados con éxito.");
				setCreate(true);
				}else{
					setMsg("Ha ocurrido un error al momento de inscribir el Usuario.");
					setCreate(true);
				}
			}else{
				setMsg("Ha ocurrido un error al momento de inscribir el Usuario.");
				setCreate(true);
			}	
			userCode="";
			userCodeType="1";
			userNames="";
			userSecondNames="";
			userPhone1="";
			userPhone2="";
			userAdress="";
			userCity="";
			userEmail="";
			userPassword="";
			userEmail2="";
			userPassword2="";
			foreignCity="";
            foreignCount="";
            aceptPersonCient=false;
            setForePer(false);
	   } else {
		   setCreate(true);
		   setAceptPersonCient(false);
		   setEnablesend(false);
	   }	
		return null;
	}
	
	
	public boolean validatePassword(boolean outside, String password){
	 boolean res=false;
		if(outside){
			if(userPassword==""){
			    res=false;
			}else{
				res=true;
			}
		}else{
			res=true;
		}
		return res;
	}
	
	
	/**
	 * Control Modal Panel
	 */
	public void hideModal() {
		this.setEnablesend(false);
		this.setCreate(false);
		create=false;
		setAsigRole(false);
		setAigRoleEnd(false);
		
		if(msg!=null){
			if(msg.equals("Registro Exitoso.")){
				this.setDisabled(true);
			}
			else{
				this.setDisabled(false);
			}
		}
		this.clearVar();
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
			}
			setAigRoleEnd(false);
		}
		
		
	}

	/**
	 * Clears variables.
	 */
	public void clearVar() {
	    if(msg!=null){
	    	System.out.println("msg " + msg);
	    	if(msg.equals("Registro Exitoso") || msg.equals("Usuario registrado con éxito.") || msg.equals("Usuario y roles de usuario registrados con éxito.")){
	    		setUserCode("");
	    		if(userCodeType.equals("NIT") || userCodeType.equals("3")){
	    			setUserCodeType("NIT");
	    		}else {
	    			setUserCodeType("1");
	    		}
	    		setUserNames("");
	    		setUserSecondNames("");
	    		setUserPhone1("");
	    		setUserPhone2("");
	    		setUserAdress("");
	    		setUserCity("");
	    		setUserEmail("");
	    		setUserEmail2("");
	    		setForeignCity("");
	    		setForeignCount("");
	    		aceptPersonCient=false;
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
		this.userEmail = userEmail.toLowerCase().trim();
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

	public Role getRoleEJB() {
		return roleEJB;
	}

	public void setRoleEJB(Role roleEJB) {
		this.roleEJB = roleEJB;
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
		List<Cities> c = userEJB.getCities();
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
		this.userEmail2 = userEmail2.toLowerCase().trim();;
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

	/**
	 * @param roleClientList the roleClientList to set
	 */
	public void setRoleClientList(List<SelectItem> roleClientList) {
		this.roleClientList = roleClientList;
	}

	/**
	 * @return the roleClientList
	 */
	public List<SelectItem> getRoleClientList() {
		if(roleClientList == null){
			roleClientList = new ArrayList<SelectItem>();
		}else{
			roleClientList.clear();
		}
		//typeRoleList.add(new SelectItem(-1L, "--Seleccione Tipo Rol--"));
		for(TbRole role : roleEJB.getTypeRoleClientList()){
			roleClientList.add(new SelectItem(role.getRoleId(), role.getRoleName()));
		}
		return roleClientList;
	}

	/**
	 * @param roleClientId the roleClientId to set
	 */
	public void setRoleClientId(Long roleClientId) {
		this.roleClientId = roleClientId;
	}

	/**
	 * @return the roleClientId
	 */
	public Long getRoleClientId() {
		return roleClientId;
	}
	
	/**
	 * Changes the form according if the location is foreign.
	 */
	public void changeTypeID(ValueChangeEvent event) {
		String codeType = event.getNewValue().toString();
		System.out.println("codeType en changeTypeId-->"+codeType);
		setUserCodeType(codeType);
		if(codeType.equals("1")){
			setForePer(false);
		}
	
	}
	
	/**
	 * Changes the form according if the location is foreign.
	 */
	public void changeLocation(ValueChangeEvent event) {
		System.out.println("change ea -->"+event.getNewValue());
		if (event.getNewValue().equals("0")) {
			setForePer(true);
			setForeign("Extranjero");
		} else {
			setForePer(false);
			setForeign("");
		}
	}

	public void setForePer(boolean forePer) {
		this.forePer = forePer;
	}

	public boolean isForePer() {
		return forePer;
	}

	public void setForeign(String foreign) {
		this.foreign = foreign;
	}

	public String getForeign() {
		return foreign;
	}

	public void setForeignCity(String foreignCity) {
		this.foreignCity = foreignCity;
	}

	public String getForeignCity() {
		return foreignCity;
	}

	public void setForeignCount(String foreignCount) {
		this.foreignCount = foreignCount;
	}

	public String getForeignCount() {
		return foreignCount;
	}

	public String getPoliciesNatura() {
		setPoliciesNatura(dataPoliciesEJB.getTextHTML(3L));
		return policiesNatura;
	}

	public void setPoliciesNatura(String policiesNatura) {
		this.policiesNatura = policiesNatura;
	}

	public boolean isAceptPersonCient() {
		return aceptPersonCient;
	}

	public void setAceptPersonCient(boolean aceptPersonCient) {
		this.aceptPersonCient = aceptPersonCient;
	}

	public boolean isEnablesend() {
		return enablesend;
	}

	public void setEnablesend(boolean enablesend) {
		this.enablesend = enablesend;
	}
	

}