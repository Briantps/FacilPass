package mBeans;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import constant.EmailProcess;
import constant.LogAction;
import constant.LogReference;
import constant.UserState;

import jpa.TbSystemUser;
import jpa.TbUserData;
import jpa.TbUserStateType;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

import report.ReportConfigUtil;
import sessionVar.SessionUtil;
import upload.FileUtil;
import util.Cities;
import util.ConnectionData;
import util.UsrRole;
import util.ValidationPassword;
import ejb.Contract;
import ejb.DataPolicies;
import ejb.Document;
import ejb.Log;
import ejb.Role;
import ejb.SystemParameters;
import ejb.email.Outbox;
import exeptions.NotCreateAccountException;

/**
 * @author Cristian Avellaneda
 * 
 */
public class FormOutSideBean implements Serializable{
	private static final long serialVersionUID = 1L;

	// Attributes --------------------------------------------------------------------------------

	private String captchaSellerInput;

	private boolean typePer;

	private boolean forePer = false;

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

	private String userEmail="mysuper@mail.com";

	private String userPassword="Password";

	private String userEmail2="mysuper@mail.com";

	private String userPassword2 = "Password";

	private String userCodeTypeNit="";

	private String userNit;

	private String razSoc;

	private String nombreR;
	
	private boolean modal;

	private List<UsrRole> listRol;

	private List<SelectItem> listRoles;

	private List<jpa.TbRole> listRolesAsig;

	private List<String> listRoleString;

	private List<String> listRoleSTarget;
	
	@EJB(mappedName="ejb/Role")
	private Role roleEjb;

	/**
	 * EJB que se encarga de enviar la notificacion por el gestor de correos
	 */
	@EJB(mappedName = "ejb/email/Outbox")
	private Outbox outbox;
	
	@EJB(mappedName="ejb/User")
	private ejb.User userEjb;

	@EJB(mappedName="ejb/Document")
	private Document documentEJB;

	@EJB(mappedName="ejb/Contract")
	private Contract contractEJB;

	@EJB(mappedName="ejb/Log")
	private Log logEJB;

	@EJB(mappedName ="ejb/SystemParameters")
	private SystemParameters SystemParametersEJB;

	@PersistenceContext(unitName = "bo")
	EntityManager em;

	private boolean create;

	private boolean contractModal;

	private boolean aceptation;

	private boolean aceptation2;

	private long userId;

	private String msg;

	private String msgAcept;

	private List<SelectItem> cities;

	private boolean asigRole;

	private boolean aigRoleEnd;

	private String message;

	private boolean disabled;

	private String dv;

	private String contract;

	private String titleContract;

	private String foreign;

	private Boolean confirmExit=false;

	private int maxSizePassword=50;

	private List<Cities> auxCities;

	private String COMPILE_FILE_NAME = " ";

	private final String COMPILE_DIR = "/jasper/design/";
	
	@EJB(mappedName = "ejb/DataPolicies")
	private DataPolicies dataPoliciesEJB;

		private String msgLog;
	
	private boolean cancelRegister;
	
	private boolean aceptcient;

	private boolean enableacept = false;
	
	private String policiesNatura;
	
	private boolean aceptPersonCient;

	private boolean enablesend = false;

	private String ip;

	// Constructor ----//

	public FormOutSideBean() {
		ip=SessionUtil.ip();
		System.out.println("Ingrse a FormOutSideBean");
		sTypePer = "1";
		listRoleString = new ArrayList<String>();
		listRol = new ArrayList<UsrRole>();
		listRoleSTarget = new ArrayList<String>();
		this.setUserPassword("Password");
		this.setSTypePer("1");
		this.setUserCodeType("1");
		this.setForePer(true);
		this.setEnableacept(false);
	}

	@SuppressWarnings("unused")
	@PostConstruct
	private void init() {
		this.auxCities = userEjb.getCities();
	}

	// Actions -------//

	public String backIndexOutside() {
		this.clear();
		return "backIndexOutside";
	}

	/**
	 * Changes the form according if is a natural person or a corporate body.
	 */
	public void changeTypePers(ValueChangeEvent event) {
		System.out.println("Ingrse a changeTypePers");
	
			this.setAceptPersonCient(false);
			System.out.println("estado del check " + aceptPersonCient);
		if (event.getNewValue().equals("3")||event.getNewValue().equals("NIT")) {
			System.out.println("entro a NIT");
			sTypePer = "2";
			typePer = false;
			forePer = true;
			enablesend = true;
			userCodeType = "NIT";
			userCodeTypeAux = "NIT";
			
		} else {
			sTypePer = event.getNewValue().toString();
			typePer = true;
			enablesend = false;
			forePer = true;
			userCodeType = "";
			userCodeTypeAux = "";
			
		
		}
	}

	/**
	 * Changes the form according if the location is foreign.
	 */
	public void changeLocation(ValueChangeEvent event) {
		System.out.println("Ingrse a changeLocation");
		System.out.print("ea " + event.getNewValue());
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
		System.out.println("Esta seleccionado ComboBox Identificacion");
		if(event.getNewValue().toString().equals("3")){
			this.changeTypePers(event);
			}else{
				this.changeTypePers(event);
		userCodeType = event.getNewValue().toString();
		if (userCodeType.equals("1")) {
			forePer = true;
		}
		System.out.println(userCodeType);
			}
	}

	private boolean validations() {
		System.out.println("Ingrse a validations");
		setMaxSizePassword(userEjb.getSizePassword().intValue());

		Boolean wrongPhone = false;
		Boolean wrongPhone2 = false;

		try {
			int phoneUser = Integer.parseInt(userPhone1);
			if (phoneUser <= 10) {
				wrongPhone = true;
			}
		} catch (Exception e) {
			System.out.println("Numero con extension.");
		}

		if (!"".equals(userPhone2)) {
			try {
				int phoneUser2 = Integer.parseInt(userPhone2);
				if (phoneUser2 <= 10) {
					wrongPhone2 = true;
				}
			} catch (Exception e) {
				System.out.println("Numero con extension.");
			}
		}

		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		javax.servlet.http.HttpSession session = request.getSession();
		String parm = captchaSellerInput;
		System.out.println("parm " + parm);
		String c = (String) session.getAttribute(MyCaptcha.CAPTCHA_KEY);
		System.out.println("c " + c);
		if (userCodeType == "") {
			setMsg("Seleccione el Tipo de Documento.");
			return false;
		}
		// ------------------------validaciones de documento,NIT y dígito de
		// verificacion----------------
		else if ((userCodeType.equals("3")) && (userCode == "")) {
			setMsg("El NIT es requerido.");
			return false;
		} else if ((!userCodeType.equals("3")) && (userCode == "")) {
			setMsg("El No. de identificación es requerido.");
			return false;
		}

		else if ((userCodeType.equals("3")) && userCode.length() < 6) {
			setMsg("El NIT no debe ser menor a 6 ni mayor a 15 caracteres. Verifique.");
			return false;
		}

		else if ((!userCodeType.equals("3")) && userCode.length() < 6) {
			setMsg("El No. identificación no debe ser menor a 6 ni mayor a 15 caracteres. Verifique");
			return false;
		}

		else if ((userCodeType.equals("3"))
				&& userCode.substring(0, 1).equals("0")) {
			setMsg("El NIT no puede iniciar con 0.");
			return false;
		}

		else if ((!userCodeType.equals("3"))
				&& userCode.substring(0, 1).equals("0")) {
			setMsg("El No. de identificación no puede iniciar con 0.");
			return false;
		}

		else if ((userCodeType.equals("3"))
				&& !userCode.matches("([0-9]|\\s)+")) {
			setMsg("El NIT tiene caracteres inválidos, por favor verifique.");
			return false;
		}

		else if ((!userCodeType.equals("3"))
				&& !userCode.matches("([0-9]|\\s)+")) {
			setMsg("El No. de identificación tiene caracteres inválidos, por favor verifique.");
			return false;
		}

		else if ((userCodeType.equals("3")) && "".equals(dv.trim())) {
			setMsg("El dígito de verificación es requerido.");
			return false;
		}

		else if ((userCodeType.equals("3"))&& !dv.matches("([0-9]|\\s)+")) {
			setMsg("El dígito de verificación debe ser numérico y de un dígito, por favor verifique.");
			return false;
		}

		if ((userCodeType.equals("3"))) {
			String nit = userCode + dv;
			System.out.println("el nit:" + nit);
			if (userEjb.validateUserCodeUK(userCodeType.trim(), nit)) {
				setMsg("Existe un usuario registrado con el mismo número de Nit. Verifique.");
				return false;
			}
		}

		else {
			if (userEjb
					.validateUserCodeUK(userCodeType.trim(), userCode.trim())) {
				setMsg("Existe un usuario registrado con el mismo número de documento. Verifique.");
				return false;
			}
		}
		// ------------------------validaciones de Nombre y Razon
		// Social------------------------------

		if ((!userCodeType.equals("3")) && userNames.trim().length() <= 0) {
			setMsg("El nombre es requerido.");
			return false;
		}

		else if ((userCodeType.equals("3"))&& userNames.trim().length() <= 0) {
			setMsg("La razón social es requerida.");
			return false;
		}

		else if ((userCodeType.equals("3"))
				&& (userNames.length() < 3 || userNames.length() > 100)) {
			setMsg("La razón social no debe ser menor a 3 ni mayor a 100 caracteres. Verifique");
			return false;
		}

		else if ((!userCodeType.equals("3"))
				&& (userNames.length() < 3 || userNames.length() > 100)) {
			setMsg("El nombre no debe ser menor a 3 ni mayor a 100 caracteres. Verifique");
			return false;
		}

		else if ((!userCodeType.equals("3"))
				&& !userNames
						.matches("([a-z]|[A-Z]|[áéíóú]|[ÁÉÍÓÚ]|[ñÑ]|\\s)+")) {
			setMsg("En nombre tiene caracteres inválidos, por favor verifique.");
			return false;
		}

		else if ((userCodeType.equals("3"))
				&& !userNames
						.matches("([a-z]|[A-Z]|[áéíóú]|[ÁÉÍÓÚ]|[0-9]|[&/]|[_-]|[.]|[ñÑ]|\\s)+")) {
			setMsg("La razón social tiene caracteres inválidos, por favor verifique.");
			return false;
		}

		// ------------------------validaciones de Apellido y Nombre
		// Representante legal------------------------------

		else if ((!userCodeType.equals("3"))
				&& userSecondNames.trim().length() <= 0) {
			setMsg("El apellido es requerido.");
			return false;
		}

		else if ((userCodeType.equals("3"))
				&& userSecondNames.trim().length() <= 0) {
			setMsg("El nombre del representante legal es requerido.");
			return false;
		}

		else if ((!userCodeType.equals("3"))
				&& (userSecondNames.length() < 3 || userSecondNames.length() > 100)) {
			setMsg("El apellido no debe tener menos de 3 ni más de 50 caracteres. Verifique");
			return false;
		}

		else if ((userCodeType.equals("3"))
				&& (userSecondNames.length() < 3 || userSecondNames.length() > 100)) {
			setMsg("El nombre del representante legal no debe tener menos de 3 ni más de 50 caracteres. Verifique");
			return false;
		}

		else if ((!userCodeType.equals("3"))
				&& !userSecondNames
						.matches("([a-z]|[A-Z]|[áéíóú]|[ÁÉÍÓÚ]|[ñÑ]|\\s)+")) {
			setMsg("El apellido tiene caracteres inválidos, por favor verifique.");
			return false;
		}

		else if ((userCodeType.equals("3"))
				&& !userSecondNames
						.matches("([a-z]|[A-Z]|[áéíóú]|[ÁÉÍÓÚ]|[ñÑ]|\\s)+")) {
			setMsg("El nombre del representante legal tiene caracteres inválidos, por favor verifique.");
			return false;
		}

		// ------------------------validaciones Pais constitucion Razon
		// Social------------------------------
		else if ((userCodeType.equals("3")) && countryRS == "") {
			setMsg("El país de constitución de la razón social es requerido.");
			return false;
		}

		else if ((userCodeType.equals("3"))
				&& !countryRS
						.matches("([a-z]|[A-Z]|[áéíóú]|[ÁÉÍÓÚ]|[ñÑ]|\\s)+")) {
			setMsg("El país de constitución de la razón social tiene caracteres inválidos, por favor verifique.");
			return false;
		}

		// ------------------------validaciones de Identificacion representante
		// legal-----------------------
		else if ((userCodeType.equals("3")) && legalCode == "") {
			setMsg("El No. de identificación del representante legal es requerido.");
			return false;
		}

		else if ((userCodeType.equals("3")) && legalCode.length() < 6) {
			setMsg("El No. identificación del representante legal debe ser mínimo de 6 dígitos.");
			return false;
		}

		else if ((userCodeType.equals("3"))
				&& !legalCode.matches("([0-9]|\\s)+")) {
			setMsg("El No. de identificación del representante legal tiene caracteres inválidos, por favor verifique.");
			return false;
		}

		// ------------------------validaciones de
		// Direccion------------------------------
		else if (userAdress == "") {
			setMsg("La dirección es requerida.");
			return false;
		}

		else if (userAdress.length() < 7 || userAdress.length() > 100) {
			setMsg("La dirección no debe tener menos de 7 ni más de 50 caracteres. Verifique");
			return false;
		}

		// ------------------------validaciones de Ciudad Extranjero y Pais
		// Extranjero------------------------------

		else if (userCity.equals("0") && foreignCity == "") {
			setMsg("La ciudad en el extranjero es requerida.");
			return false;
		} else if (userCity.equals("0") && foreignCount == "") {
			setMsg("El país en el extranjero es requerido.");
			return false;
		}

		else if (userCity.equals("0")
				&& !foreignCity
						.matches("([a-z]|[A-Z]|[áéíóú]|[ÁÉÍÓÚ]|[ñÑ]|\\s)+")) {
			setMsg("La ciudad en el extranjero tiene caracteres inválidos, por favor verifique.");
			return false;
		}

		else if (userCity.equals("0")
				&& !foreignCount
						.matches("([a-z]|[A-Z]|[áéíóú]|[ÁÉÍÓÚ]|[ñÑ]|\\s)+")) {
			setMsg("El país en el extranjero tiene caracteres inválidos, por favor verifique.");
			return false;
		}

		// ------------------------validaciones de
		// Celuar------------------------------

		else if (userPhone1 == "") {
			setMsg("El celular es requerido.");
			return false;
		}

		else if (userPhone1.length() < 7 || userPhone1.length() > 30) {
			setMsg("El celular no debe tener menos de 7 ni más de 30 caracteres. Verifique");
			return false;
		}

		else if (wrongPhone) {
			setMsg("El numero " + userPhone1
					+ " no es un celular valido. Verifique");
			return false;
		}

		else if (!userPhone1.matches("([0-9]|\\s)+")) {
			setMsg("El celular tiene caracteres inválidos, por favor verifique.");
			return false;
		}

		// ------------------------validaciones de Telefono
		// opcional------------------------------

		else if (userPhone2 != ""
				&& (userPhone2.length() < 7 || userPhone2.length() > 30)) {
			setMsg("El teléfono opcional no debe tener menos de 7 ni más de 30 caracteres. Verifique");
			return false;
		}

		else if (userPhone2 != "" && wrongPhone2) {
			setMsg("El numero " + userPhone2
					+ " no es un teléfono opcional valido. Verifique");
			return false;
		}

		else if (userPhone2 != ""
				&& (!userPhone2.matches("([0-9]|[a-z]|[A-Z]|\\s)+"))) {
			setMsg("El teléfono opcional tiene caracteres inválidos, por favor verifique.");
			return false;
		}

		// ------------------------validaciones de
		// Usuario------------------------------

		else if (userEmail == "") {
			setMsg("El usuario es requerido.");
			return false;
		}

		else if (userEmail.length() < 5 || userEmail.length() > 100) {
			setMsg("El correo electrónico del campo usuario no debe tener menos de 5 ni más de 100 caracteres. Verifique");
			return false;
		}

		else if (userEmail.equals("mysuper@mail.com")) {
			setMsg("El usuario es requerido. ");
			return false;
		}

		else if (!this.isEmail(userEmail)) {
			setMsg("El usuario tiene un correo inválido. Verifique");
			return false;
		}

		// ------------------------validaciones de Confirmar
		// Usuario------------------------------

		else if (userEmail2 == "" || userEmail2.equals("mysuper@mail.com")) {
			setMsg("El campo confirmar usuario es requerido.");
			return false;
		}

		else if (!userEmail.toLowerCase().equals(userEmail2.toLowerCase())) {
			System.out.println("Entro a validar los correos");
			System.out.println("userEmail " + userEmail);
			System.out.println("userEmail2 " + userEmail2);

			setMsg("El usuario y el usuario de confirmación deben ser iguales. Verifique");
			return false;
		}

		// validate that email doesn't exist.
		else if (userEjb.validateUserEmailUK(userEmail.toLowerCase())) {
			setMsg("Existe un usuario con el mismo correo electrónico. Verifique.");
			return false;
		}

		// ------------------------validaciones de
		// Contraseña------------------------------

		else if (userPassword == "" || userPassword == "Password") {
			setMsg("La contraseña es requerida.");
			return false;
		}

		ValidationPassword valPass = null;
		try {
			valPass = userEjb.validationPassword(userPassword, userCode);
		} catch (Exception e) {
			valPass.setValidation(false);
			valPass.setMessageValidate("La contraseña no cumple con las caracteristicas requeridas. Verifique.");
		}

		if (!valPass.getValidation()) {
			setMsg(valPass.getMessageValidate());
			return false;
		}

		// ------------------------validaciones de Confirmar
		// Contraseña------------------------------
		else if (userPassword2 == "" || userPassword2 == "Password") {
			setMsg("El campo confirmar contraseña es requerido.");
			return false;
		}

		else if (!userPassword.equals(userPassword2)) {
			setMsg("Los campos contraseña y confirmar contraseña deben ser iguales.");
			return false;
		}

		else if ((userCodeType.equals("3"))&& legalCodeType == "") {
			setMsg("Seleccione el tipo de documento del representante legal.");
			return false;
		}

		// ------------------------validaciones de
		// Captcha------------------------------
		else if (parm == "" || parm == null) {
			setMsg("El código captcha es requerido.");
			System.out.println("captcha vacio");
			return false;
		} else if (!parm.equals(c)) {
			// CAPTCHA INCORRECT INPUT
			setMsg("Ingrese correctamente el código captcha.");
			System.out.println("captcha incorrecto");
			return false;
		} /*else if (aceptPersonCient && !typePer) {
			// PERSONA NATURAL NO ACEPTO LOS TERMINOS
			setMsg("Debe aceptar los Términos.");
			System.out.println("Terminos no aceptados");
			return false;
		}*/

		return true;
	}

	public boolean isEmail(String correo) {
		Pattern pat = null;
		Matcher mat = null;
		pat = Pattern
				.compile("^[A-Za-z0-9._+-]+@[A-Za-z0-9.-]+\\.[a-zA-Z]{2,4}$");
		mat = pat.matcher(correo);

		System.out.println("res en isEmail " + mat.matches());
		return mat.matches();
	}

	/**
	 * Saves a client.
	 */
	public String saveClien() {
		System.out.println("Ingrese a saveClien ");
		if (validations()) {
			message = this.save(false);
			setMsg(message);
		}
		setCreate(false);
		setModal(true);
		
		if (typePer) {
			setAceptPersonCient(false);
			setEnablesend(false);				
		}
			
		return null;
	}

	/**
	 * Saves a client from outside.
	 * 
	 * @return
	 */
	public String saveClienOutside() {
		System.out.println("Ingrese a saveClienOutside ");
		//Error de seguridad la contraseña no debe imprimirse
		/*System.out.println("userPassword " + userPassword);
		System.out.println("userPassword2 " + userPassword2);*/
		if (validations()) {
			setCaptchaSellerInput("");
			if (this.validatePassword(true, userPassword)) {
				if (this.validatePassword(true, userPassword2)) {
					if (userPassword.equals(userPassword2)) {
						if ((userCodeType.equals("3"))) {
							setTitleContract("CONTRATO DE TÉRMINOS Y CONDICIONES PARA PERSONAS JURÍDICAS");
							setContract(userEjb.getContractLP(userCodeType,
									userCode + "-" + dv,
									userNames.toUpperCase(),
									userSecondNames.toUpperCase(),
									userAdress.toUpperCase(), userCity,
									foreignCount.toUpperCase(),
									userEmail.toLowerCase(), legalCodeType,
									legalCode, countryRS.toUpperCase()));
						} else {
							if (userCity.equals("0")) {
								setContract(userEjb.getContractNP(userCodeType,
										userCode, userNames.toUpperCase(),
										userSecondNames.toUpperCase(),
										userAdress.toUpperCase(),
										foreignCity.toUpperCase(),
										foreignCount.toUpperCase(),
										userEmail.toLowerCase()));
							} else {
								setContract(userEjb.getContractNP(userCodeType,
										userCode, userNames.toUpperCase(),
										userSecondNames.toUpperCase(),
										userAdress.toUpperCase(), userCity,
										"COLOMBIA", userEmail.toLowerCase()));
							}
							setTitleContract("CONTRATO DE TÉRMINOS Y CONDICIONES PARA PERSONAS NATURALES");
						}
						setCreate(false);
						setModal(false);
						setContractModal(true);
					} else {
						setMessage("La Contraseña y la Contraseña de Confirmación deben ser iguales. Verifique");
						setMsg(message);
						setCreate(false);
						setModal(true);
						if (typePer) {
							setAceptPersonCient(false);
							setEnablesend(false);				
						}
					}
				} else {
					setMessage("El campo Confirmar Contraseña es requerido.");
					setMsg(message);
					setCreate(false);
					setModal(true);
					if (typePer) {
						setAceptPersonCient(false);
						setEnablesend(false);				
					}
				}
			} else {
				setMessage("El campo Contraseña es requerido.");
				setMsg(message);
				setCreate(false);
				setModal(true);
				if (typePer) {
					setAceptPersonCient(false);
					setEnablesend(false);				
				}
			}
		} else {
			setCaptchaSellerInput("");
			setCreate(false);
			setModal(true);
			if (typePer) {
				setAceptPersonCient(false);
				setEnablesend(false);				
			}
		}

		return null;
	}

	/**
	 * Saves an UserSystem.
	 * 
	 * @return userId created.
	 */
	private String save(boolean outside) {
		System.out.println("Ingrese a save ");
		Long id = null;

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
		String uct = (userCodeType.equals("NIT") ? "3" : userCodeType);
		System.out.print("uct" + uct);
		if ((userCodeType.equals("3"))) {
			foreignCount = countryRS;
		}
		System.out.println("se creo cliente persona natural");

		return userEjb.createClient(outside, id, SessionUtil.ip(), uct.trim(),
				userCode.trim(), userNames.trim().toUpperCase(),
				userSecondNames.trim().toUpperCase(), userPhone1.trim(),
				userPhone2.trim(), userAdress.trim().toUpperCase(), userCity
						.trim(), userEmail.trim().toLowerCase(), userPassword
						.trim(), dv, legalCodeType, legalCode.trim(),
				foreignCity.trim().toUpperCase(), foreignCount.trim()
						.toUpperCase());
	}

	public String openAceptation() {
		System.out.println("Ingrese a openAceptation ");
		setContractModal(false);
		setMsgAcept("Con esta aceptación EL USUARIO reitera su aceptación y conocimiento de todos los términos "
				+ "y condiciones del contrato FacilPass.");
		this.setEnableacept(false);
		setAceptation2(true);
		return null;
	}

	public String makeAceptation() {
		System.out.println("Ingrese a makeAceptation ");
		setMessage(this.save(true));
		setMsg(message);
		setAceptation(false);
		if (msg.equals("Registro Exitoso")
				|| msg.equals("Usuario registrado con éxito.")
				|| msg.equals("Usuario y roles de usuario registrados con éxito.")) {
			setCreate(false);
			setModal(true);
		} else {
			setCreate(true);
			setModal(false);
		}
		System.out.println("se creo cliente persona natural");
		confirmExit = true;
		this.savePdf(userCode, userCodeType);
		return null;
	}

	public String makeAceptation2() {
		if ((userCodeType.equals("NIT")||userCodeType.equals("3"))) {
			setMessage(this.save(true));
			setMsg(message);
			setAceptation2(false);
			if (msg.equals("Registro Exitoso")
					|| msg.equals("Usuario registrado con éxito.")
					|| msg.equals("Usuario y roles de usuario registrados con éxito.")) {
				setCreate(false);
				setModal(true);
			} else {
				setCreate(true);
				setModal(false);
			}
			userCode = userCode + dv;
			System.out.println("se creo cliente persona juridica");
			confirmExit = true;
			this.savePdf(userCode, userCodeType);
		} else {
			setMsgAcept("EL USUARIO así mismo autoriza a FACILPASS S.A.S. para recopilar, administrar y hacer "
					+ "uso de la información personal suministrada, para fines de la adecuada prestación "
					+ "del servicio contratado.");
			setAceptation2(false);
			setAceptation(true);
		}
		return null;
	}

	public String cancelAceptation() {
		this.clear();
		setAceptation(false);
		setAceptation2(false);
		setCancelRegister(false);
		this.setAceptcient(false);
		if (typePer) {
			setAceptPersonCient(false);
			setEnablesend(false);				
		}
		return "successOut";
	}

	public boolean validatePassword(boolean outside, String password) {
		boolean res = false;
		if (outside) {
			if (userPassword == "") {
				res = false;
			} else {
				res = true;
			}
		} else {
			res = true;
		}

		return res;
	}

	/**
	 * Control Modal Panel
	 */
	public String hideModal() {
		this.setCreate(false);
		this.setModal(false);
		this.setAceptation(false);
		this.setAceptation2(false);
		this.setCancelRegister(false);
		if (typePer) {
			setAceptPersonCient(false);
			setEnablesend(false);				
		}
		create = false;
		modal = false;
		setAsigRole(false);
		setAigRoleEnd(false);

		System.out.println("msg en hideModal " + msg);
		System.out.println("message en hideModal " + message);
		if (msg != null) {
			if (msg.equals("Registro Exitoso")) {
				System.out.println("disabled true");
				this.setDisabled(true);
			} else {
				this.setDisabled(false);
			}
		}
		System.out.println("disabled " + disabled);
		this.clearVar();
		if (confirmExit) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().getSessionMap()
					.remove("formOutSideBean");
			return "successOut";
		} else
			return null;
	}

	public void hideModalContract() {
		System.out.println("off modal contract");
		setContractModal(false);
		this.setEnableacept(false);	
		setAceptcient(false);
		if (typePer) {
			setAceptPersonCient(false);
			setEnablesend(false);				
		}
	}

	public void hideModal2() {
		this.setCreate(false);
		this.setModal(false);
		this.setAceptPersonCient(false);
		create = false;
		modal = false;
		if (msg != null) {
			if (msg.equals("Usuario y roles de usuario registrados con éxito.")) {
				System.out.println("Entre al if hideModal2");
				this.setAsigRole(false);
				this.clearVar();
			} else if (msg.equals("Debe seleccionar un rol.")) {
				System.out.println("Entre al else if hideModal2");

			}
			setAigRoleEnd(false);
		}

	}

	public void clear() {
		System.out.println("Ingrese a clear ");
		setUserCode("");
		if (userCodeType.equals("NIT") || userCodeType.equals("3")) {
			setUserCodeType("NIT");
			setSTypePer("2");
		} else {
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
		setCaptchaSellerInput("");
		setForePer(true);
		setAceptation(false);
		setAceptation2(false);
		setContractModal(false);
		setCancelRegister(false);
		setUserPassword("");
		setUserPassword2("");
		setAceptPersonCient(false);

		this.setDv("");
	}

	/**
	 * Clears variables.
	 */
	public void clearVar() {
		if (msg != null) {
			System.out.println("msg " + msg);
			if (msg.equals("Registro Exitoso")
					|| msg.equals("Usuario registrado con éxito.")
					|| msg.equals("Usuario y roles de usuario registrados con éxito.")) {
				System.out.println("Entre aqui");
				setUserCode("");
				if (userCodeType.equals("NIT") || userCodeType.equals("3")) {
					setUserCodeType("NIT");
					setSTypePer("2");
				} else {
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
				setCaptchaSellerInput("");
				setForePer(true);
				this.setDv("");
				setAceptation2(false);
			} else {
				if (msg.equals("No fue posible generar el contrato. Para obtenerlo ingrese al menú Cuenta FacilPass opción Contrato, donde podrá realizar la descarga.")) {
					System.out.println("No fue posible generar el contrato");
					setUserCode("");
					if (userCodeType.equals("NIT") || userCodeType.equals("3")) {
						setUserCodeType("NIT");
						setSTypePer("2");
					} else {
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
					setCaptchaSellerInput("");
					setForePer(true);
					this.setDv("");
					setAceptation2(false);
				}
			}
		}

	}

	public String clearCaptcha() {
		setCaptchaSellerInput("");
		return null;
	}

	// -------------------------

	public String savePdf(String userCode, String userCodeType) {
		System.out.println("Ingrese a savePdf ");
		if (userCodeType.equals("NIT")||userCodeType.equals("3")) {
			Long userCodeTypeL = Long.parseLong("3");

			COMPILE_FILE_NAME = "contractLP";
			try {
				TbSystemUser tsu = userEjb.getUserByCode(userCode,
						userCodeTypeL);
				this.prepareReport(tsu.getUserId(), COMPILE_FILE_NAME);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error savePdf contractLP");
			}
		} else {
			Long userCodeTypeL = Long.parseLong(userCodeType);
			COMPILE_FILE_NAME = "contractNP";
			try {
				TbSystemUser tsu = userEjb.getUserByCode(userCode,
						userCodeTypeL);
				this.prepareReport(tsu.getUserId(), COMPILE_FILE_NAME);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error savePdf contractNP");
			}
		}
		if (msg.equals("Registro Exitoso")
				|| msg.equals("Usuario registrado con éxito.")
				|| msg.equals("Usuario y roles de usuario registrados con éxito.")) {
			setCreate(false);
			setModal(true);
		} else {
			setCreate(true);
			setModal(false);
		}
		return null;
	}

	public String prepareReport(long userId, String compile_file_name) {
		TbSystemUser user = null;
		try {
			user = em.find(TbSystemUser.class, userId);

			ExternalContext externalContext = FacesContext.getCurrentInstance()
					.getExternalContext();
			ServletContext context = (ServletContext) externalContext
					.getContext();

			ReportConfigUtil.compileReport(context, getCompileDir(),
					compile_file_name);
			File reportFile = new File(ReportConfigUtil.getJasperFilePath(
					context, getCompileDir(), compile_file_name + ".jasper"));

			String path = "";
			String systemParametersValue = SystemParametersEJB
					.getParameterValueById(28L);

			String uploadedFileName = FileUtil
					.trimFilePath("Contrato_Clientes_"
							+ System.currentTimeMillis());
			path = systemParametersValue.trim()
					+ "/"
					+ user.getUserCode()
					+ "-"
					+ userEjb.getDocumentClient(user.getUserCode(), userId,
							SessionUtil.ip(), userId);
			
			java.io.File directory = new java.io.File(path.toString());
			System.out.println("directory: "+directory.toString());
			Long documentId = null;
			if (!directory.exists()) {
				// It returns true if directory is a directory.
				boolean result = directory.mkdir();
				if (result) {
					System.out.println("DIR created--->" + result);
				}

				JasperPrint jasperPrint = ReportConfigUtil.fillReport(
						reportFile, getReportParameters(userId),
						getJRDataSource(), getDataConnection());
				JasperExportManager.exportReportToPdfFile(jasperPrint,
						directory + "/" + uploadedFileName + ".pdf");

				// se crea el archivo PDF
				java.io.File uniqueFile = FileUtil.uniqueFile(new java.io.File(
						path), uploadedFileName);
				// se persiste en tabla tb_document
				documentId = documentEJB.insertDocumentDisk(userId, 1L, 5L,
						uniqueFile.toString() + ".pdf");
			} else {

				documentId = contractEJB.getTypeContract(userId);
			}
			boolean response = contractEJB.signContract(userId, documentId,
					SessionUtil.ip());
			System.out.println("FormOutSideBean-->response firma digital:"
					+ response);
			if (response == false) {
				System.out.println("FormOutSideBean-->response firma digital:"
						+ response);
				setMsg("No fue posible generar el contrato. Para obtenerlo ingrese al menú Cuenta FacilPass opción Contrato, donde podrá realizar la descarga.");

			} else if (response == true) {
				// Actualizo el estado del cliente a estado cliente si es presona natural
				System.out.println("Tipo de cliente: "+user.getTbCodeType().getCodeTypeAbbreviation());
				if (!user.getTbCodeType().getCodeTypeAbbreviation().equals("NIT")){
					System.out.println("Actualizacion 1");
					user.setUserState(em.find(TbUserStateType.class,
							UserState.ACTIVE.getId()));
					userEjb.updateUser(user);
					System.out.println("Cambio de rol Out: "+userEjb.changeRoleUser(user.getUserId(), (long)constant.Role.CLIENT.getId()));
				}
				// inserto la base de datos que acepto el contrato
				contractEJB.saveRegacceptscontrac(userId, SessionUtil.ip(),
						aceptcient);
				
				// Creacion de la Cuenta-Facil-Pass Persona Natural
				Long creoCuenta = null;
				if (!user.getTbCodeType().getCodeTypeAbbreviation().equals("NIT")){	
					creoCuenta = userEjb.createAccount(user.getUserId(),
							50000L, user.getUserId(), ip, -1L, -1L);
					if (creoCuenta != null && creoCuenta <= -2L) {
						throw new NotCreateAccountException(creoCuenta);
					}
				}
			}

		} catch (NotCreateAccountException e) {
			outbox.insertMessageOutbox(userId,
					EmailProcess.ERROR_CREATE_ACCOUNT, null, null, null, null,
					null, null, null, null, null, user.getTbCodeType()
							.getCodeTypeId(), null, null, true, null);

		}catch (Exception e) {
			System.out.println("FormOutSideBean-->Exception");
			setMsg("No fue posible generar el contrato. Para obtenerlo ingrese al menú Cuenta FacilPass opción Contrato, donde podrá realizar la descarga.");
			msgLog = "Error al generar firma digital. Mensaje: ";
			ArrayList<String> parameters=new ArrayList<String>();
			parameters.add("#ERR="+e.getMessage());
			outbox.insertMessageOutbox(userId,
					EmailProcess.ERROR_DIGITAL_SIGNATURE, null, null, null, null,
					null, null, null, null, null, user.getTbCodeType()
							.getCodeTypeId(), null, null, true, parameters);
			msgLog = msgLog + e.toString() + ".";
			
			logEJB.insertLog(msgLog, LogReference.CLIENT,
					LogAction.OPERATIONFAILED, SessionUtil.ip(), userId);
			e.printStackTrace(System.out);
		}
		return null;
	}
	
	
	
	protected ConnectionData getDataConnection() {
		ConnectionData factory = new ConnectionData();
		//factory.getConnection();
		return factory;
	}

	protected Map<String, Object> getReportParameters(Long userId) {
		Map<String, Object> reportParameters = new HashMap<String, Object>();

		BigDecimal userCodeType = userEjb.getCodeTypeId(userId);
		TbUserData userData = userEjb.getUserDataById(userId);

		if (userCodeType.equals(BigDecimal.valueOf(3))) {
			Date fecha = new Timestamp(System.currentTimeMillis());

			reportParameters.put("IDLEGAL", String.valueOf(userData.getUserDataContactId()));
			reportParameters.put("NOMBRE", String.valueOf(userData.getUserDataOfficeName()));
			reportParameters.put("PAISRC", String.valueOf(userData.getForeignCountry()));
			reportParameters.put("ID", String.valueOf(userData.getTbSystemUser().getUserCode()));
			reportParameters.put("CIUDAD", String.valueOf(userData.getTbMunicipality().getMunicipalityName()));
			reportParameters.put("NOMLEGAL", String.valueOf(userData.getUserDataContact()));
			reportParameters.put("DIRECCION", userData.getUserDataAddress());
			reportParameters.put("PAIS", "COLOMBIA");
			reportParameters.put("MAIL", userData.getTbSystemUser().getUserEmail());
			reportParameters.put("MES", new SimpleDateFormat("MMMM").format(fecha));
			reportParameters.put("DIA", new SimpleDateFormat("dd").format(fecha));
			reportParameters.put("AÑO", new SimpleDateFormat("yyyy").format(fecha));
			reportParameters.put("DIANOMBRE", getNomDayByNum(Integer.parseInt(
					new SimpleDateFormat("d").format(fecha))));
			reportParameters.put("NOMLEGALFP", contractEJB.getParameterValueById(24L));
			reportParameters.put("ADDRESSFP", contractEJB.getParameterValueById(25L));
			reportParameters.put("NUMDOCLEGAL", contractEJB.getParameterValueById(26L));
			reportParameters.put("TYPEDOCLEGAL", contractEJB.getParameterValueById(27L));
			reportParameters.put("RAZONFP", contractEJB.getParameterValueById(29L));
			reportParameters.put("NITFP", contractEJB.getParameterValueById(28L));
			System.out.println("parametros persona juridica "+reportParameters.toString());
			
			return reportParameters;
		} else {
			Date fecha = new Timestamp(System.currentTimeMillis());

			reportParameters.put("ID", String.valueOf(userData.getTbSystemUser().getUserCode()));
			reportParameters.put("NOM", String.valueOf(userData.getTbSystemUser().getUserNames()+
					" "+userData.getTbSystemUser().getUserSecondNames()));
			reportParameters.put("TID", String.valueOf(userData.getTbSystemUser().getTbCodeType()
					.getCodeTypeAbbreviation()));
			reportParameters.put("MAIL", String.valueOf(userData.getTbSystemUser().getUserEmail()));
			Long muni=null;
			
			try {				
				muni=userData.getTbMunicipality().getMunicipalityId();
			} catch (Exception e2) {
				e2.printStackTrace();
				muni=0l;
			}
			
			if(muni.intValue()==0){
				reportParameters.put("CITY",userData.getForeignCity());
				reportParameters.put("COUNTRY",userData.getForeignCountry());
			}else{
				reportParameters.put("CITY",userData.getTbMunicipality().getMunicipalityName());
				reportParameters.put("COUNTRY", "COLOMBIA");
			}
			reportParameters.put("ADDRESS",userData.getUserDataAddress());
			reportParameters.put("MES", new SimpleDateFormat("MMMM").format(fecha));
			reportParameters.put("DIA", new SimpleDateFormat("dd").format(fecha));
			reportParameters.put("AÑO", new SimpleDateFormat("yyyy").format(fecha));
			reportParameters.put("DIANOMBRE", getNomDayByNum(Integer.parseInt(
					new SimpleDateFormat("d").format(fecha))));
			reportParameters.put("NOMLEGALFP", contractEJB.getParameterValueById(24L));
			reportParameters.put("ADDRESSFP", contractEJB.getParameterValueById(25L));
			reportParameters.put("NUMDOCLEGAL", contractEJB.getParameterValueById(26L));
			reportParameters.put("TYPEDOCLEGAL", contractEJB.getParameterValueById(27L));
			reportParameters.put("RAZONFP", contractEJB.getParameterValueById(29L));
			reportParameters.put("NITFP", contractEJB.getParameterValueById(28L));
			System.out.println("parametros persona natural "+reportParameters.toString());
			return reportParameters;
		}
	}

	public String getNomDayByNum(int day) {
		String salida = "";
		switch (day) {
		case 1:
			salida = "uno";
			break;
		case 2:
			salida = "dos";
			break;
		case 3:
			salida = "tres";
			break;
		case 4:
			salida = "Cuatro";
			break;
		case 5:
			salida = "cinco";
			break;
		case 6:
			salida = "seis";
			break;
		case 7:
			salida = "siete";
			break;
		case 8:
			salida = "ocho";
			break;
		case 9:
			salida = "nueve";
			break;
		case 10:
			salida = "diez";
			break;
		case 11:
			salida = "once";
			break;
		case 12:
			salida = "doce";
			break;
		case 13:
			salida = "trece";
			break;
		case 14:
			salida = "catorce";
			break;
		case 15:
			salida = "quince";
			break;
		case 16:
			salida = "dieciséis";
			break;
		case 17:
			salida = "diecisiete";
			break;
		case 18:
			salida = "dieciocho";
			break;
		case 19:
			salida = "diecinueve";
			break;
		case 20:
			salida = "veinte";
			break;
		case 21:
			salida = "veintiuno";
			break;
		case 22:
			salida = "veintidós";
			break;
		case 23:
			salida = "veintitrés";
			break;
		case 24:
			salida = "veinticuatro";
			break;
		case 25:
			salida = "veinticinco";
			break;
		case 26:
			salida = "veintiséis";
			break;
		case 27:
			salida = "veintisiete";
			break;
		case 28:
			salida = "veintiocho";
			break;
		case 29:
			salida = "veintinueve";
			break;
		case 30:
			salida = "treinta";
			break;
		case 31:
			salida = "treintaiuno";
			break;
		default:
			salida = "Otro número";
			break;
		}
		return salida;
	}

	// Se genera mensaje cuando se intenta cancelar el registro.
	public void cancelRegisterM() {
		setMsgAcept("Señor usuario está seguro de cancelar el proceso de registro");
		setCancelRegister(true);

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
	 * @param msg
	 *            the msg to set
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
	 * @param cities
	 *            the cities to set
	 */
	public void setCities(List<SelectItem> cities) {
		this.cities = cities;
	}

	/**
	 * @return the cities
	 */
	public List<SelectItem> getCities() {
		cities = new ArrayList<SelectItem>();
		String city;
		for (Cities ci : auxCities) {
			city = ci.getCity() + " - " + "("
					+ ci.getDepartment().substring(0, 4) + ")";
			if (ci.getCityCode().equals("0")) {
				if (userCodeType.equals("2")) {
					cities.add(new SelectItem(ci.getCityCode(), city));
				}
			} else {
				cities.add(new SelectItem(ci.getCityCode(), city));
			}
		}
		return cities;
	}

	public void activatebutton() {

		if (aceptcient) {
			this.setEnableacept(true);
		} else {
			this.setEnableacept(false);
		}

	}

	public void activatebutton1(ValueChangeEvent event) {
		System.out.println("Entre a Activar o inactivar el boton Send");
		if (((Boolean) event.getNewValue())) {
			this.setEnablesend(true);
		} else {
			this.setEnablesend(false);
		}

	}

	/**
	 * @param asigRole
	 *            the asigRole to set
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
	 * @param aigRoleEnd
	 *            the aigRoleEnd to set
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
	 * @param message
	 *            the message to set
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
	 * @param disabled
	 *            the disabled to set
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
	 * @param userEmail2
	 *            the userEmail2 to set
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
	 * @param userPassword2
	 *            the userPassword2 to set
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
	 * @param dv
	 *            the dv to set
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

	public String getCaptchaSellerInput() {
		return captchaSellerInput;
	}

	public void setCaptchaSellerInput(String captchaSellerInput) {
		this.captchaSellerInput = captchaSellerInput;
	}

	public void setAceptation2(boolean aceptation2) {
		this.aceptation2 = aceptation2;
	}

	public boolean isAceptation2() {
		return aceptation2;
	}

	public void setMaxSizePassword(int maxSizePassword) {
		this.maxSizePassword = maxSizePassword;
	}

	public int getMaxSizePassword() {
		return maxSizePassword;
	}

	public void setUserCodeTypeAux(String userCodeTypeAux) {
		this.userCodeTypeAux = userCodeTypeAux;
	}

	public String getUserCodeTypeAux() {
		return userCodeTypeAux;
	}

	public void setAuxCities(List<Cities> auxCities) {
		this.auxCities = auxCities;
	}

	public List<Cities> getAuxCities() {
		return auxCities;
	}

	public void setCOMPILE_FILE_NAME(String cOMPILE_FILE_NAME) {
		COMPILE_FILE_NAME = cOMPILE_FILE_NAME;
	}

	public String getCOMPILE_FILE_NAME() {
		return COMPILE_FILE_NAME;
	}

	/**
	 * 
	 * @return JRDataSource.
	 */
	protected JRDataSource getJRDataSource() {
		return null;
	}

	/**
	 * 
	 * @return File Name
	 */
	protected String getFileName() {
		return "Contrato_Clientes_" + System.currentTimeMillis();
	}

	/**
	 * 
	 * @return COMPILE_DIR
	 */
	protected String getCompileDir() {
		return COMPILE_DIR;
	}

	public void setContractEJB(Contract contractEJB) {
		this.contractEJB = contractEJB;
	}

	public Contract getContractEJB() {
		return contractEJB;
	}

	public boolean isModal() {
		return modal;
	}

	public void setModal(boolean modal) {
		this.modal = modal;
	}

	public boolean isAceptcient() {
		return aceptcient;
	}

	public void setAceptcient(boolean aceptcient) {
		this.aceptcient = aceptcient;
	}

	public boolean isEnableacept() {
		return enableacept;
	}

	public void setEnableacept(boolean enableacept) {
		this.enableacept = enableacept;
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

	public boolean isCancelRegister() {
		return cancelRegister;
	}

	public void setCancelRegister(boolean cancelRegister) {
		this.cancelRegister = cancelRegister;
	}

	public String getPoliciesNatura() {
		this.setPoliciesNatura(dataPoliciesEJB.getTextHTML(2L));
		return policiesNatura;
	}

	public void setPoliciesNatura(String policiesNatura) {
		this.policiesNatura = policiesNatura;
	}

}