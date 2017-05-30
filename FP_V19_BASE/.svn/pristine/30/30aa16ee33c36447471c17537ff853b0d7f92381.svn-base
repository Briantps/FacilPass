package reportBean;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import jpa.TbSystemUser;
import jpa.TbUserData;
import jpa.TbUserStateType;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import report.AbstractBaseReportBean;
import report.ReportConfigUtil;
import security.UserLogged;
import sessionVar.SessionUtil;
import upload.FileUtil;
import util.ConnectionData;
import constant.EmailProcess;
import constant.LogAction;
import constant.LogReference;
import constant.UserState;
import ejb.Contract;
import ejb.Log;
import ejb.SystemParameters;
import ejb.email.Outbox;
import exeptions.NotCreateAccountException;

/**
 * 
 * @author jromero
 */
public class ContractClientsBean extends AbstractBaseReportBean implements
		Serializable {

	private static final long serialVersionUID = 1L;

	@PersistenceContext(unitName = "bo")
	EntityManager em;

	@EJB(mappedName = "ejb/Log")
	private Log logEJB;

	/**
	 * EJB que se encarga de enviar la notificacion por el gestor de correos
	 */
	@EJB(mappedName = "ejb/email/Outbox")
	private Outbox outbox;

	@EJB(mappedName = "ejb/User")
	private ejb.User userEjb;

	@EJB(mappedName = "ejb/Contract")
	private ejb.Contract contract;

	@EJB(mappedName = "ejb/Log")
	private Log log;

	@EJB(mappedName = "ejb/SystemParameters")
	private SystemParameters SystemParametersEJB;

	@EJB(mappedName = "ejb/Contract")
	private Contract contractEJB;

	private ConnectionData connection = new ConnectionData();
	private String COMPILE_FILE_NAME = "";

	private String exportOption1;

	private TbUserData userData;

	private Long codeType;

	private String target;

	private String msgEmail;

	private String msgLog;

	private String msgModal;

	private String msnContract;

	private boolean showModal;

	private boolean showPDF = false;

	private Long typeContract = -1L;

	private Long userId = 0l;
    private Long typePerson;
	private Long personNatural;
	private Long personJuridic;

	private String userCode = "";

	private String msgAcept;

	private boolean contractModal;

	private boolean button;

	private boolean aceptation;

	private boolean aceptation2;

	private boolean dataTable;

	private boolean aceptcient;

	private boolean enableacept;

	private UserLogged usrs;

	private String ip;

	private String message;

	private String msg;
	private String titleContract;

	/**
	 * Variable que almacena el id del usuario master.
	 */
	private Long masterID;

	private static final int DEFAULT_BUFFER_SIZE = 10240; // 10KB.

	public ContractClientsBean() {
		usrs = (UserLogged) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get("user");
		ip = SessionUtil.ip();

	}

	@PostConstruct
	public void init() {
		Long res=0L;
		Long docE=-1L;
		// se almacena el id del usuario master
		masterID = userEjb.getSystemMasterById(usrs.getUserId());
		System.out.println("masterID: "+masterID);
		setTypePerson(userEjb.getCodeTypeId(masterID).longValue());
		res =userEjb.getacceptanceofcontract(masterID);
		if(!getMsnContract().equals("")){
			System.out.println("getTypePerson(): "+getTypePerson());
			if(getTypePerson()==1L||getTypePerson()==2L){//validacion para tipo de persona Natural
				setPersonNatural(getTypePerson());//tipo de persona Natural	
				setTitleContract("CONTRATO DE TÉRMINOS Y CONDICIONES PARA PERSONAS NATURALES");
				if(res!=0L){
					printPdf();
					this.setContractModal(false);
					this.setDataTable(true);
				}else{
					docE=contractEJB.getStateDocument(masterID);
					System.out.println("Contract1: "+docE);
					if(docE==0L || docE==5L){
						this.setContractModal(true);
						this.setDataTable(false);
					}else{
						printPdf();
						this.setContractModal(false);
					}
				}
			}else{
				if(getTypePerson()==3){//validación para tipo de persona juridica
					setPersonJuridic(getTypePerson());//tipo de persona Juridica
					setTitleContract("CONTRATO DE TÉRMINOS Y CONDICIONES PARA PERSONAS JURÍDICAS");
					if(res!=0L){
						printPdf();
						this.setContractModal(false);
						this.setDataTable(true);
					}else{
						docE=contractEJB.getStateDocument(masterID);
						System.out.println("Contract2: "+docE);
						if(docE==0L || docE==5L){
							this.setContractModal(true);
							this.setDataTable(false);
						}else{
							printPdf();
							this.setContractModal(false);
						}				
					}
				}
			}
		}else{
		//	this.setContractModal(false);
		//	this.setDataTable(true);
			System.out.println("no hay contrato");
		}
	}

	public void openAceptation() {
		System.out.println("Ingrese a openAceptation ");
		setContractModal(false);
		setMsgAcept("Con esta aceptación EL USUARIO reitera su aceptación y conocimiento de todos los términos "
				+ "y condiciones del contrato FacilPass.");
		setAceptation2(true);

	}

	public void cancelAceptation() {
		setAceptation(false);
		setAceptation2(false);
		setEnableacept(false);
		setContractModal(false);

	}

	/**
	 * Metodo que realiza el proceso de generar el contrato
	 */
	public void makeAceptation() {
		System.out.println("Ingrese a makeAceptation ");
		printPdf();
		setAceptation(false);
	}

	public void hideModalContract() {
		setContractModal(false);
		init();

	}

	/**
	 * Metodo que muestra el mensaje de aceptacion de la recopilacion de
	 * informacion al usuario.
	 */
	public void makeAceptation2() {
		System.out.println("getTypePerson()2: "+getTypePerson());
		if(getTypePerson()==3){
			System.out.println("Ingrese a contrato cleinte ");
			printPdf();
			setAceptation(false);
			setAceptation2(false);
		}else{
			setMsgAcept("EL USUARIO así mismo autoriza a FACILPASS S.A.S. para recopilar, administrar y hacer "
					+ "uso de la información personal suministrada, para fines de la adecuada prestación "
					+ "del servicio contratado.");
			setAceptation2(false);
			setAceptation(true);
		}
	}

	public void activatebutton() {

		if (aceptcient) {
			this.setEnableacept(true);
		} else {
			this.setEnableacept(false);
		}

	}

	public String printPdf() {
		try {
			userData = userEjb.getUserDataById(userEjb.getSystemMasterById(SessionUtil.sessionUser().getUserId()));

			if (userData == null) {
				System.out.println("Este usuario es un cliente hijo.");
				userId = userEjb.getSystemMasterById(SessionUtil.sessionUser()
						.getUserId());
				userData = userEjb.getUserDataById(userId);
			}

		} catch (Exception e) {
			System.out.println("Excepcion en contractClientsBean");
			userId = userEjb.getSystemMasterById(SessionUtil.sessionUser()
					.getUserId());
			userData = userEjb.getUserDataById(userId);
		}
		if (userId == 0l) {
			userId = userEjb.getSystemMasterById(SessionUtil.sessionUser()
					.getUserId());
			userCode = userData.getTbSystemUser().getUserCode();
		}
		System.out.println("[userId]" + userId);
		System.out.println("[userCode]" + userCode);
		typeContract = contract.getTypeContract(userId);
		System.out.println("[typeContract]" + typeContract);
		if (typeContract == 0L) {
			Boolean validClient = true;
			System.out.println("Muesta de base de datos");
			try {
				codeType = userData.getTbSystemUser().getTbCodeType()
						.getCodeTypeId();
				System.out.println("the codeType:" + codeType);
			} catch (Exception e) {
				e.printStackTrace();
				validClient = false;// Unicamente los clientes pueden descargar
									// el contrato, no usuarios;
			}

			if (validClient) {

				if (codeType.intValue() == 3) {
					COMPILE_FILE_NAME = "contractLP";
					System.out.println(COMPILE_FILE_NAME);
				} else {
					COMPILE_FILE_NAME = "contractNP";
					System.out.println(COMPILE_FILE_NAME);
				}
			}

			try {
				System.out.println("[typeContractOld]" + typeContract);
				typeContract = this.saveReport(userId, userCode,
						COMPILE_FILE_NAME);
				System.out.println("[typeContract]" + typeContract);
				if (typeContract != 0L) {
					showPdfPV(userId, typeContract);
				} else {
					// setTarget("");
					setShowPDF(false);
					setShowModal(true);
					setMsgModal("No fue posible descargar el contrato, "
							+ "comuníquese con su asesor comercial o al call center de FacilPass.");
					System.out.println("No se pudo generar ningun archivo.");
				}
			} catch (Exception e) {
				setShowPDF(false);
				System.out.println("Error ContractClientsBean");
				e.printStackTrace();
			}
		} else if (typeContract == -1L) {
			// setTarget("");
			setShowPDF(false);
			setShowModal(true);
			setMsgModal("No fue posible descargar el contrato, "
					+ "comuníquese con su asesor comercial o al call center de FacilPass.");
			System.out
					.println("No fue posible descargar el contrato, "
							+ "comuníquese con su asesor comercial o al call center de FacilPass.");
		} else {
			showPdfPV(userId, typeContract);
		}
		return null;
	}

	public void showPdfPV(Long userId, Long typeContract) {
		boolean respu = false;
		boolean print = true;
		System.out.println("Muesta PDFPV");
		TbSystemUser user = null;
		try{
			user = em.find(TbSystemUser.class, userId);
			if (contract.getContract(typeContract)[2].equals("5")) {
				respu = contract.signContract(userId, typeContract,
						SessionUtil.ip());
				if (respu) {
					// Actualizo el estado del cliente a estado cliente si es presona natural
					if (!user.getTbCodeType().getCodeTypeAbbreviation().equals("NIT")){					
						user.setUserState(em.find(TbUserStateType.class,
								UserState.ACTIVE.getId()));
						userEjb.updateUser(user);
						System.out.println("Cambio de rol Contract: "+userEjb.changeRoleUser(user.getUserId(), (long)constant.Role.CLIENT.getId()));
					}
					// inserto la base de datos que acepto el contrato
					contractEJB.saveRegacceptscontrac(userId, SessionUtil.ip(),
							aceptcient);
					
					// Creacion de la Cuenta-Facil-Pass Persona Natural
					Long creoCuenta = null;
					if (!user.getTbCodeType().getCodeTypeAbbreviation().equals("NIT")){	
						if(!userEjb.verificationCta(user.getUserId())){//se verifica si tiene ya una cuenta creada
							creoCuenta = userEjb.createAccount(user.getUserId(),
									50000L, user.getUserId(), ip, -1L, -1L);
							if (creoCuenta != null && creoCuenta <= -2L) {
								throw new NotCreateAccountException(creoCuenta);
							}
						}
					}
					typeContract = contract.getTypeContract(userId);
					print = true;
				} else {
					print = false;
					System.out.println("[ No se pudo firmar el contrato. ]");
					setShowPDF(false);
					setShowModal(true);
					setMsgModal("No fue posible descargar el contrato, "
							+ "comuníquese con su asesor comercial o al call center de FacilPass.");
					System.out
							.println("No fue posible descargar el contrato, "
									+ "comuníquese con su asesor comercial o al call center de FacilPass.");
				}
			}
			if (print) {
				System.out.println("Path:" + contract.getContract(typeContract)[0]
						+ contract.getContract(typeContract)[1]);
				File file = new File(contract.getContract(typeContract)[0],
						contract.getContract(typeContract)[1]);
				if (file.exists()) {
					System.out.println("Existe archivo");
					setShowPDF(true);
				} else {
					msgLog = "Error al generar contrato. Mensaje: ";
					msgEmail = "El archivo "
							+ contract.getContract(typeContract)[0]
							+ contract.getContract(typeContract)[1] + " no existe.";
					msgLog = msgLog + "El archivo "
							+ contract.getContract(typeContract)[0]
							+ contract.getContract(typeContract)[1] + " no existe.";
					ArrayList<String> parameters=new ArrayList<String>();
					parameters.add("#ERR="+msgEmail);
					outbox.insertMessageOutbox(userId,
							EmailProcess.ERROR_DIGITAL_SIGNATURE, null, null, null, null,
							null, null, null, null, null, userEjb.getCodeTypeId(userId).longValue(), 
							null, null, true, parameters);
					log.insertLog(msgLog, LogReference.CLIENT,
							LogAction.OPERATIONFAILED, SessionUtil.ip(), userId);
					System.out
							.println(" [ Error ContractClientsBean.printPdf FileNotFound ]");
					setShowModal(true);
					setMsgModal("No fue posible descargar el contrato, "
							+ "comuníquese con su asesor comercial o al call center de FacilPass.");
					setShowPDF(false);
				}
			}
		}catch (NotCreateAccountException e) {
			outbox.insertMessageOutbox(userId,
					EmailProcess.ERROR_CREATE_ACCOUNT, null, null, null, null,
					null, null, null, null, null, user.getTbCodeType()
							.getCodeTypeId(), null, null, true, null);

		}catch (Exception e) {
			System.out.println("ContractClientsBean-->Exception");
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
	}

	public String showPdfVerify() {
		System.out.println("Muesta PDF");
		typeContract = contract.getTypeContract(userEjb
				.getSystemMasterById(SessionUtil.sessionUser().getUserId()));
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		HttpServletResponse response = (HttpServletResponse) externalContext
				.getResponse();
		File file = new File(contract.getContract(typeContract)[0],
				contract.getContract(typeContract)[1]);
		BufferedInputStream input = null;
		BufferedOutputStream output = null;

		try {
			System.out.println("Entra aqui");
			// Open file.
			input = new BufferedInputStream(new FileInputStream(file),
					DEFAULT_BUFFER_SIZE);
			response.reset();
			response.setHeader("Content-Type", "application/pdf");
			response.setHeader("Content-Length", String.valueOf(file.length()));
			response.setHeader("Content-Disposition", "inline; filename=\""
					+ contract.getContract(typeContract)[1] + "\"");
			output = new BufferedOutputStream(response.getOutputStream(),
					DEFAULT_BUFFER_SIZE);

			// Write file contents to response.
			byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
			int length;
			while ((length = input.read(buffer)) > 0) {
				output.write(buffer, 0, length);
			}

			// Finalize task.
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error ContractClientsBean.printPdf ]");
		} finally {
			// Gently close streams.
			System.out.println("Finally");
			close(output);
			close(input);
		}

		// Inform JSF that it doesn't need to handle response.
		// This is very important, otherwise you will get the following
		// exception in the logs:
		// java.lang.IllegalStateException: Cannot forward after response has
		// been committed.
		facesContext.responseComplete();
		return null;
	}

	public Long saveReport(long userId, String userCode,
			String compile_file_name) throws JRException, IOException {
		Long respu = 0L;
		try {
			ExternalContext externalContext = FacesContext.getCurrentInstance()
					.getExternalContext();
			ServletContext context = (ServletContext) externalContext
					.getContext();

			ReportConfigUtil.compileReport(context, getCompileDir(),
					getCompileFileName());
			File reportFile = new File(ReportConfigUtil.getJasperFilePath(
					context, getCompileDir(), getCompileFileName() + ".jasper"));

			String path = "";
			String systemParametersValue = SystemParametersEJB
					.getParameterValueById(28L);

			String uploadedFileName = FileUtil
					.trimFilePath("Contrato_Clientes_"
							+ System.currentTimeMillis());
			path = systemParametersValue.trim()
					+ "/"
					+ userCode
					+ "-"
					+ userEjb.getDocumentClient(userCode, userId,
							SessionUtil.ip(), userId);

			java.io.File directory = new java.io.File(path.toString());
			if (!directory.exists()) {
				// It returns true if directory is a directory.
				boolean result = directory.mkdir();
				if (result) {
					System.out.println("DIR created--->" + result);
				}
			}
			JasperPrint jasperPrint = ReportConfigUtil.fillReport(reportFile,
					getReportParameters(), getJRDataSource(),
					getDataConnection());
			JasperExportManager.exportReportToPdfFile(jasperPrint, directory
					+ "/" + uploadedFileName + ".pdf");

			// se crea el archivo PDF
			java.io.File uniqueFile = FileUtil.uniqueFile(
					new java.io.File(path), uploadedFileName);
			respu = contract.saveRegDocument(userId, 5, uniqueFile.toString()
					+ ".pdf", new Timestamp(System.currentTimeMillis()), null);
			System.out.println("[respu]" + respu);
		} catch (Exception e) {
			setShowPDF(false);
			System.out.println(" [ Error ContractClientsBean.saveReport ]");
			msgLog = "Error al generar contrato. Mensaje: ";
			msgLog = msgLog + e.toString() + ".";
			ArrayList<String> parameters=new ArrayList<String>();
			parameters.add("#ERR="+e.getMessage());
			outbox.insertMessageOutbox(userId,
					EmailProcess.ERROR_DIGITAL_SIGNATURE, null, null, null, null,
					null, null, null, null, null, userEjb.getCodeTypeId(userId).longValue(),
					null, null, true, parameters);
			log.insertLog(msgLog, LogReference.CLIENT,
					LogAction.OPERATIONFAILED, SessionUtil.ip(), userId);
			e.printStackTrace();
			respu = 0L;
		}
		return respu;
	}

	private static void close(Closeable resource) {
		if (resource != null) {
			try {
				resource.close();
			} catch (IOException e) {
				// Do your thing with the exception. Print it, log it or mail
				// it. It may be useful to
				// know that this will generally only be thrown when the client
				// aborted the download.
				e.printStackTrace();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see report.AbstractBaseReportBean#getReportParameters()
	 */
	@Override
	protected Map<String, Object> getReportParameters() {
		if (codeType.intValue() == 3) {
			Date fecha=SessionUtil.sessionUser().getUserRegistrationDate();
			if(fecha==null){
				fecha=SessionUtil.sessionUser().getUserPwdDate();
			}			
			Map<String, Object> reportParameters = new HashMap<String, Object>();	
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
			reportParameters.put("NOMLEGALFP", contract.getParameterValueById(24L));
			reportParameters.put("ADDRESSFP", contract.getParameterValueById(25L));
			reportParameters.put("NUMDOCLEGAL", contract.getParameterValueById(26L));
			reportParameters.put("TYPEDOCLEGAL", contract.getParameterValueById(27L));
			reportParameters.put("RAZONFP", contract.getParameterValueById(29L));
			reportParameters.put("NITFP", contract.getParameterValueById(28L));
			System.out.println("par "+reportParameters.toString());
			return reportParameters;
		} else {
			Date fecha=userData.getTbSystemUser().getUserRegistrationDate();
			if(fecha==null){
				fecha=SessionUtil.sessionUser().getUserPwdDate();
			}
			Map<String, Object> reportParameters = new HashMap<String, Object>();	
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
				System.out.println("if ContractClientsBean");
				reportParameters.put("CITY",userData.getForeignCity());
				reportParameters.put("COUNTRY",userData.getForeignCountry());
			}else{
				System.out.println("else ContractClientsBean");
				reportParameters.put("CITY",userData.getTbMunicipality().getMunicipalityName());
				reportParameters.put("COUNTRY", "COLOMBIA");
			}
			reportParameters.put("ADDRESS",userData.getUserDataAddress());
			reportParameters.put("MES", new SimpleDateFormat("MMMM").format(fecha));
			reportParameters.put("DIA", new SimpleDateFormat("dd").format(fecha));
			reportParameters.put("AÑO", new SimpleDateFormat("yyyy").format(fecha));
			reportParameters.put("DIANOMBRE", getNomDayByNum(Integer.parseInt(
					new SimpleDateFormat("d").format(fecha))));
			reportParameters.put("NOMLEGALFP", contract.getParameterValueById(24L));
			reportParameters.put("ADDRESSFP", contract.getParameterValueById(25L));
			reportParameters.put("NUMDOCLEGAL", contract.getParameterValueById(26L));
			reportParameters.put("TYPEDOCLEGAL", contract.getParameterValueById(27L));
			reportParameters.put("RAZONFP", contract.getParameterValueById(29L));
			reportParameters.put("NITFP", contract.getParameterValueById(28L));
			System.out.println("par "+reportParameters.toString());
			return reportParameters;
		}
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
		return "Contrato_Clientes_" + System.currentTimeMillis();
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

	/**
	 * @param exportOption1
	 *            the exportOption1 to set
	 */
	public void setExportOption1(String exportOption1) {
		this.exportOption1 = exportOption1;
	}

	/**
	 * @return the exportOption1
	 */
	public String getExportOption1() {
		return exportOption1;
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

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getMsgModal() {
		return msgModal;
	}

	public void setMsgModal(String msgModal) {
		this.msgModal = msgModal;
	}

	public boolean isShowModal() {
		return showModal;
	}

	public void setShowModal(boolean showModal) {
		this.showModal = showModal;
	}

	public void hideModal() {
		setMsgModal("");
		setShowPDF(false);
		setShowModal(false);
	}

	public boolean isShowPDF() {
		return showPDF;
	}

	public void setShowPDF(boolean showPDF) {
		this.showPDF = showPDF;
	}

	public boolean isContractModal() {
		return contractModal;
	}

	public void setContractModal(boolean contractModal) {
		this.contractModal = contractModal;
	}

	public boolean isButton() {
		return button;
	}

	public void setButton(boolean button) {
		this.button = button;
	}

	public String getMsgAcept() {
		return msgAcept;
	}

	public void setMsgAcept(String msgAcept) {
		this.msgAcept = msgAcept;
	}

	public boolean isAceptation2() {
		return aceptation2;
	}

	public void setAceptation2(boolean aceptation2) {
		this.aceptation2 = aceptation2;
	}

	public boolean isDataTable() {
		return dataTable;
	}

	public void setDataTable(boolean dataTable) {
		this.dataTable = dataTable;
	}

	public Long getPersonNatural() {
		
		return personNatural;
	}

	public void setPersonNatural(Long personNatural) {
		this.personNatural = personNatural;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isAceptation() {
		return aceptation;
	}

	public void setAceptation(boolean aceptation) {
		this.aceptation = aceptation;
	}

	

	public String getMsnContract() {
		setMsnContract(userEjb.getContractNPExisting(userEjb
				.getSystemMasterById(usrs.getUserId()),typePerson));		
		return msnContract;
	}

	public void setMsnContract(String msnContract) {
		this.msnContract = msnContract;
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

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getTitleContract() {
		return titleContract;
	}

	public void setTitleContract(String titleContract) {
	
		this.titleContract = titleContract;
	}

	public Long getPersonJuridic() {
		
		return personJuridic;
	}

	public void setPersonJuridic(Long personJuridic) {
		this.personJuridic = personJuridic;
	}

	public Long getTypePerson() {
		return typePerson;
	}

	public void setTypePerson(Long typePerson) {
		this.typePerson = typePerson;
	}

	public Long getMasterID() {
		return masterID;
	}

	public void setMasterID(Long masterID) {
		this.masterID = masterID;
	}
	

}
