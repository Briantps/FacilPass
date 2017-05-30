package process.account;

import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import jpa.TbAccount;
import jpa.TbAlarmBalance;
import jpa.TbBank;
import jpa.TbBankTransact;
import jpa.TbFrequency;
import jpa.TbTypeProgramming;
import security.UserLogged;
import sessionVar.SessionUtil;
import util.ClientRecharge;
import util.ServiceResponse;
import util.ws.dtos.WsRtaConsultTransAval;

import com.facilpass.ejb.ProgramaticRechargeEvaluation;

import constant.AccountStateType;
import constant.EAlarmType;
import constant.EFrequency;
import constant.ETbProcessRerchargeAVAL;
import constant.ETypePerson;
import constant.PaymentMethod;
import constant.TPS_Constants;
import constant.TypeConfAutomaticRecharge;
import constant.TypeProgramming;
import ejb.AlarmBalance;
import ejb.ElectronicRecharge;
import ejb.IConfAutomaticProgramming;
import ejb.User;
import ejb.crud.Bank;

/**
 * 
 * Clase bean creada para la administraci�n de recargas autom�ticas
 * 
 * @author TPS r.bautista
 * 
 */

public class ElectronicRechargeBean implements Serializable {
	private static final long serialVersionUID = 8162813547847156020L;

	/**
	 * Contiene el identificador para elementos de combos cuando se seleccion�
	 * el item inicial
	 */
	private static final long NO_SELECCION = -1L;
	
	/**
	 * Contiene el inicio del mensaje para cuentas prepago
	 */
	private static final String MENSAJE_PREPAGO = "Puede programar recargas a la cuenta FacilPass a partir de $";

	/**
	 * Contiene el inicio del mensaje para cuentas postpago
	 */
	private static final String MENSAJE_POSPAGO = "Puede programar asignaciones de recursos a la cuenta FacilPass a partir de $";

	@EJB(mappedName = "ejb/ElectronicRecharge")
	private ElectronicRecharge electronicRecharge;

	@EJB(mappedName = "ejb/User")
	private User userEJB;

	@EJB(mappedName = "ejb/IConfAutomaticProgramming")
	private IConfAutomaticProgramming confAutomaticProgrammingEJB;

	@EJB(mappedName = "ejb/AlarmBalance")
	private AlarmBalance alarmBalance;

	@EJB(mappedName = "ejb/Bank")
	private Bank bankEJB;

	@EJB(mappedName = "com/facilpass/ejb/ProgramaticRechargeEvaluation")
	private ProgramaticRechargeEvaluation sonda;

	private UserLogged usrs;
	// *** Atributes **//
	private List<SelectItem> clientAccounts;
	private List<SelectItem> typeProgramming;
	private List<ClientRecharge> automaticRechargeList;
	private List<SelectItem> frecuecyList;

	// Entidades del grupo Aval
	// private List<SelectItem> entityList;
	// Id de la entidad seleccionada
	private Long avalId;
	// Contiene las entidades presentadas
	private List<TbBank> entidades;
	// Items de entidades aval
	private List<SelectItem> entityList;
	// items de tipos de personas
	private List<SelectItem> personList;

	private Long idClientAccount;
	private Long frecuencySelect;
	private Long typeProgrammingSelect;
	private Long automaticRechargeId;
	private Long valueRecharge;
	private Long paymentMethod;
	// saldo cuenta
	private Long nbalanceAcount;

	private Date dateRecharge;

	// Control
	private boolean showModal;
	private boolean showInsert;	
	private boolean showModalDelete;

	private boolean showConfirmation;
	private boolean showConfirmationDelete;
	private boolean showTypeProgramming;
	public boolean typeFrequency = false;
	public boolean typeMinimumBalance = false;
	public boolean showTypePerson = false;

	// variable de ayuda
	private boolean restaurarDatos;

	private String valueRechText;
	private String corfirmMessage;

	private String modalMessage;
	private String messageHTML;

	/**
	 * Atributos Saldo Minimo
	 * */
	private String balanceAcount;

	private String assingRecharge;
	private Long respRadioPerson;

	/**
	 * Atributos de Tooltips
	 * */
	private String typeProgrammingHelpTooltip;
	private String frequncyHelpTooltip;
	private String minimumBalanceHelpTooltip;
	private String initialDateTooltip;
	private String rechargeValueTooltip;
	private String assingValueTooltip;
	private String linkDeletetooltip;
	
	// Mensaje campo Valor para Saldo
	private String msgBalValue;
	
	// Mensaje del parametro
//	private String paramMsg;
	// Contiene el mensaje para el parametro 71
	private String param71;
	// Contiene el mensaje para el parametro 30	
	private String param30;

	// auxiliar para saber si fue redirigido externamente
	private String paramRedirected;

	/**
	 * Indica el n�mero para hacer focus en caso de ser necsario
	 */
	private int numFocus;

	private boolean showMinimumBalanceModal;
	
	private String modalMinimumBalanceMessage;
	
	/**
	 * Constructor vacio
	 */
	public ElectronicRechargeBean() {
		usrs = (UserLogged) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get("user");
	}

	@PostConstruct
	public void init() {
		this.initFields();
		this.initParamMsg();
		this.initAccounts();
		this.initFrequencies();
		this.initTypeProgramming();
		this.initAutomaticRechargeList();
		this.initTypePerson();
		this.initEntities();
		// Se llenan los mensajes, Tooltip y tipo de persona
		this.initMainMessage();
		this.typeProgrammingHelpTooltip = confAutomaticProgrammingEJB
				.getMessageTooltip(TypeConfAutomaticRecharge.TYPE_PROGRAMMING_HELP_TOOLTIP
						.getId());
		this.initialDateTooltip = confAutomaticProgrammingEJB
				.getMessageTooltip(TypeConfAutomaticRecharge.INITIAL_DATE_TOOLTIP
						.getId());
		this.rechargeValueTooltip = confAutomaticProgrammingEJB
				.getMessageTooltip(TypeConfAutomaticRecharge.RECHARGE_VALUE_TOOLTIP
						.getId());
		this.assingValueTooltip = confAutomaticProgrammingEJB
				.getMessageTooltip(TypeConfAutomaticRecharge.ASSING_VALUE_TOOLTIP
						.getId());
		this.linkDeletetooltip = confAutomaticProgrammingEJB
				.getMessageTooltip(TypeConfAutomaticRecharge.LINK_DELETE_TOOLTIP
						.getId());
		this.showTypePerson = confAutomaticProgrammingEJB
				.getTypePerson(TypeConfAutomaticRecharge.TYPE_PERSON.getId());

		this.checkRedirect();
	}

	/**
	 * Inicializa el campo paramMsg
	 */
	private void initParamMsg(){
		this.param71 = ElectronicRechargeBean.MENSAJE_PREPAGO + electronicRecharge.getPrepaidValue();
		this.param30 = ElectronicRechargeBean.MENSAJE_POSPAGO + electronicRecharge.getPostPaidValue();
	}
	
	/**
	 * Inicializa los elementos de los tipos de persona
	 */
	private void initTypePerson(){
		this.personList = new ArrayList<SelectItem>();
		
		for (ETypePerson tp : ETypePerson.values()){
			this.personList.add(new SelectItem(tp.getId(),
					tp.getNombre()));
		}

	}

	/**
	 * 
	 * Inicializa las cuentas del usuario
	 */

	private void initAccounts() {
		Long userId = userEJB.getSystemMasterById(usrs.getUserId());

		clientAccounts = new ArrayList<SelectItem>();
		clientAccounts.add(new SelectItem(ElectronicRechargeBean.NO_SELECCION,
				"--Seleccione Cuenta--"));

		for (TbAccount su : userEJB
				.getClientAccountProgrammingAutomatic(userId)) {

			if ((su.getAccountState().longValue() == AccountStateType.ACTIVE
					.getId()) || (su.getAccountState().longValue() == 7L)) {
				clientAccounts.add(new SelectItem(su.getAccountId(),
						"Cuenta No. " + su.getAccountId()));
			}

		}

	}

	/**


	 * Inicializa las entidades registradas en el sistema
	 */

	private void initEntities() {
		List<TbBank> aval = bankEJB.getBanksAval();
		this.entityList = new ArrayList<SelectItem>();

		if (aval != null) {
			this.entidades = aval;
			for (TbBank tb : aval){
				// si fue seleccionada para programar
				if (tb.getBankProgramming().intValue() == 1){
					this.entityList.add(new SelectItem(tb.getBankId(), ""));
				}

			}

		}

		this.avalId = null;
	}

	/**



	 * Inicializa las frecuencias
	 */

	private void initFrequencies() {
		frecuecyList = new ArrayList<SelectItem>();
		frecuecyList.add(new SelectItem(ElectronicRechargeBean.NO_SELECCION,
				"--Seleccione Frecuencia--"));
		for (TbFrequency fq : electronicRecharge.getListFrequency()) {
			EFrequency ef = EFrequency.getFrequency(fq.getFrequencyId());
			
			switch (ef) {
			case SEMANAL :
			case QUINCENAL :
			case MENSUAL :
				frecuecyList.add(new SelectItem(ef.getId(),
						fq.getFrequencyDescript()));
				break;
			}
		}

	}

	/**
	 * Inicializa los tipos de frecuencia
	 */
	private void initTypeProgramming() {
		typeProgramming = new ArrayList<SelectItem>();

		typeProgramming.add(new SelectItem(ElectronicRechargeBean.NO_SELECCION,
				"--Seleccione el tipo--"));
		for (TbTypeProgramming tp : electronicRecharge.getListTypeProgramming()) {
			typeProgramming.add(new SelectItem(tp.getTypeProgramminId(), tp
					.getTypeProgrammingDesc()));
		}

	}

	/**
	 * 
	 * 
	 * Inicializa el listado de recargas automaticas realizadas
	 */

	private void initAutomaticRechargeList() {
		Long userId = userEJB.getSystemMasterById(usrs.getUserId());

		this.automaticRechargeList = electronicRecharge
				.getAutomaticRechargebyClient(userId);
	}

	/**
	 * 
	 * Inicializa el mensaje principal de ayuda de la forma
	 */

	private void initMainMessage() {
		this.messageHTML = confAutomaticProgrammingEJB
				.getMessageTooltip(TypeConfAutomaticRecharge.START_HELP_MESSAGE
						.getId());
	}

	/**
	 * Verifica si se invoca por redireccionamiento
	 */
	private void onLoadFromExternalURL() {

		// Invocar el CU_A04 el cual carga el TbBankTransact
		WsRtaConsultTransAval resp = this.cargarURL(this.paramRedirected);
		if (resp != null) {
			int val = resp.getBankTrans().getStateCode().intValue();
			switch (val) {
			// Valores de la tabla TB_STATE_RECHARGE
			case 0:
				// Pendiente
				this.setModalMessage("La transacci�n a�n est� pendiente, por favor valide m�s tarde en esta misma opci�n");
				this.showConfirmation = false;
				this.showModal = true;
				break;
			case 1:
				// aprobada
				// si no fue procesada por la sonda... ejecuto la siguiente l�nea electronicRecharge.updateElectronicRecharge
				if (!resp.isProcesoSonda()) {
					electronicRecharge.updateElectronicRecharge(resp.getBankTrans()
							.getBankTransactId(), this.consulteAval().getBankId(),
							true, SessionUtil.sessionUser().getUserId(),
							SessionUtil.ip(), null);
				}

				this.setModalMessage(this.getMensajeResultadoProgramacion(true));
				this.showConfirmation = false;
				this.showModal = true;
				this.restaurarDatos = true;
				break;
			case 2:
				// rechazada
				// si no fue procesada por la sonda... ejecuto la siguiente l�nea electronicRecharge.updateElectronicRecharge
				if (!resp.isProcesoSonda()) {
					electronicRecharge.updateElectronicRecharge(resp.getBankTrans()
							.getBankTransactId(), this.consulteAval().getBankId(),
							false, SessionUtil.sessionUser().getUserId(),
							SessionUtil.ip(), resp.getErrorId());
				}
				this.setModalMessage(this.getMensajeResultadoProgramacion(false));
				this.showConfirmation = false;
				this.showModal = true;
				break;

			}

		}

	}

	/**
	 * Verifica que el par�metro de la URL
	 * 
	 * @param urlParam
	 *            par�metro recibido en la URL
	 */

	private WsRtaConsultTransAval cargarURL(String urlParam) {
		// Este m�todo representa el CU-A04
		WsRtaConsultTransAval rta = null;
		Long userId = SessionUtil.sessionUser().getUserId();
		String ip = SessionUtil.ip();

		ServiceResponse sresp = electronicRecharge.checkURL(userId, urlParam, ip);

		rta = (WsRtaConsultTransAval) sresp.getEntity();
		if (rta == null) {
			// Hay problemas con la URL
			try {
				// No es la mejor manera pero funciona
				String paginaInicio = "../../index.jspx";

				switch (sresp.getId().intValue()) {
				case TPS_Constants.EXPIRED:
					// invalidar sesi�n (SOLO CON REDIRIGIRLO LA APLICACION
					// INVALIDA LA SESION)
					FacesContext
							.getCurrentInstance()
							.getExternalContext()
							.getSessionMap()
							.put(TPS_Constants.MENSAJE_TXT_LOGIN,
									sresp.getMsj());
					// FacesContext.getCurrentInstance().getExternalContext().redirect(paginaInicio);
				case TPS_Constants.NOT_EXIST:
					// invalidar sesi�n (SOLO CON REDIRIGIRLO LA APLICACION
					// INVALIDA LA SESION)
					// FacesContext.getCurrentInstance().getExternalContext().redirect(paginaInicio);
					// break;
				case TPS_Constants.USED:
					// invalidar sesi�n (SOLO CON REDIRIGIRLO LA APLICACION
					// INVALIDA LA SESION)
					FacesContext.getCurrentInstance().getExternalContext()
							.redirect(paginaInicio);
					break;
				}

			} catch (IOException ie) {
				this.setModalMessage("Se presentaron problemas con la aplicaci�n. Por favor vuelva a ingresar.");
				this.showConfirmation = false;
				this.showModal = true;
			}

			return rta;
		}

		if (rta.isConnectionProblems()) {
			// Problemas de conexi�n o time out del WS
			this.setModalMessage("No fue posible validar la transacci�n, por favor valide con su entidad");
			this.showConfirmation = false;
			this.showModal = true;
			rta = null;
			return rta;
		}

		if (!rta.isSuccessOperation()) {
			this.setModalMessage(sresp.getMsj());
			this.showConfirmation = false;
			this.showModal = true;
			rta = null;
			return rta;
		}

		return rta;
	}

	/**
	 * Verifica si se est� redirigiendo la pagina de una url externa. En tal
	 * caso carga la informacion
	 */
	private void checkRedirect() {
		String param = (String) FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap().get("v");

		if ((param == null) || (param.equals(""))) {
			return;
		}

		TbBankTransact tb = electronicRecharge.getBankTransactByURL(param);
		if ( (tb != null) && (tb.getAutomaticRecharge() != null) ) {
			// la url existe y se pueden cargar los datos
			this.loadDataByRedirect(tb);
		}

		this.paramRedirected = param;
		this.onLoadFromExternalURL();

	}

	/**
	 * Carga la informaci�n en el formulario
	 * 
	 * @param tb
	 */

	private void loadDataByRedirect(TbBankTransact tb) {
		// bandera para distintas b�squedas
		boolean bandera = false;
		Long accountId = tb.getAutomaticRecharge().getAccountId()
				.getAccountId();

		// Cargue de la cuenta
		// Se busca la cuenta, si no esta se agrega...
		for (SelectItem se : this.clientAccounts) {
			Long id = (Long) se.getValue();
			if (id.longValue() == accountId.longValue()) {
				bandera = true;
				break;
			}

		}

		if (!bandera) {
			this.clientAccounts.add(new SelectItem(accountId, "Cuenta No. "
					+ accountId));
		}

		this.idClientAccount = accountId;
		this.setPaymentMethod(bankEJB
				.getPlaymentMethodAccount(this.idClientAccount));
		// Cargue del tipo de programacion
		this.typeProgrammingSelect = tb.getAutomaticRecharge()
				.getTypeProgramming().getTypeProgramminId();

		if (this.typeProgrammingSelect.longValue() == TypeProgramming.FREQUENCY
				.getId()) {

			// Carga la frecuencia
			this.frecuencySelect = tb.getAutomaticRecharge().getFrequencyId()
					.getFrequencyId();

			// Carga la fecha de inicio
			this.dateRecharge = tb.getAutomaticRecharge()
					.getAutomaticRechargeDate();
			// tipo de programaci�n
			this.typeFrequency = true;

		} else {
			// Carga el saldo cuenta
			this.balanceAcount = "" + tb.getBalanceValue();
			this.nbalanceAcount = tb.getBalanceValue();
			// tipo de programaci�n
			this.typeMinimumBalance = true;
		}

		this.valueRecharge = tb.getAutomaticRecharge()
				.getAutomaticRechargeValue();
		this.valueRechText = "" + this.valueRecharge;

		if (this.isPersonTypeVisible()) {
			this.respRadioPerson = tb.getPersonType();
		}

		if (this.isEntityVisible()) {
			this.avalId = tb.getBankId();
		}


		this.showElements();
	}

	/**
	 * Retorna el mensaje para el resultado de la programacion
	 * 
	 * @param aceptada
	 *            indica si fue aceptada o rechazada
	 * 
	 * @return mensaje
	 */
	private String getMensajeResultadoProgramacion(boolean aceptada) {
		String estado = aceptada ? "aprobada" : "rechazada";

		TbBank tb = this.consulteAval();
		return "La recarga de recursos programada por valor de $"
				+ new DecimalFormat("#,###,###,###").format(this.valueRecharge)
				+ " para la cuenta FacilPass No. " + this.idClientAccount
				+ " fue " + estado + " por la entidad " + tb.getBankName();
	}

	/**
	 * 
	 * Inicializa los campos basicos del formulario
	 */

	private void initFields() {
		this.idClientAccount = ElectronicRechargeBean.NO_SELECCION;
		this.typeProgrammingSelect = ElectronicRechargeBean.NO_SELECCION;
		this.typeFrequency = false;
		this.typeMinimumBalance = false;
		this.dateRecharge = new Date();
		this.valueRecharge = null;
		this.valueRechText = null;
		this.frecuencySelect = ElectronicRechargeBean.NO_SELECCION;
		this.restaurarDatos = false;
		this.showConfirmation = false;
		this.showConfirmationDelete = false;
		this.showModal = false;
		this.showModalDelete = false;
		this.balanceAcount = null;
		this.paramRedirected = null;
		this.respRadioPerson = ElectronicRechargeBean.NO_SELECCION;
		this.paymentMethod = null;
		// Foco
		this.numFocus = 0;
		this.showMinimumBalanceModal = false;
		this.modalMinimumBalanceMessage = "";
	}

	// Getters and setters -------------- //

	public void setFrecuecyList(List<SelectItem> frecuecyList) {
		this.frecuecyList = frecuecyList;
	}

	public void setShowModal(boolean showModal) {
		this.showModal = showModal;
	}

	public boolean isShowModalDelete() {
		return showModalDelete;
	}

	public void setShowModalDelete(boolean showModalDelete) {
		this.showModalDelete = showModalDelete;
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
	
	public boolean isShowConfirmationDelete() {
		return showConfirmationDelete;
	}

	public void setShowConfirmationDelete(boolean showConfirmationDelete) {
		this.showConfirmationDelete = showConfirmationDelete;
	}

	/**
	 * @param valueRecharge
	 *            the valueRecharge to set
	 */
	public void setValueRecharge(Long valueRecharge) {
		this.valueRecharge = valueRecharge;
	}

	/**
	 * @return the valueRecharge
	 */
	public Long getValueRecharge() {
		return valueRecharge;
	}

	/**
	 * @param electronicRecharge
	 *            the electronicRecharge to set
	 */
	public void setElectronicRecharge(ElectronicRecharge electronicRecharge) {
		this.electronicRecharge = electronicRecharge;
	}

	/**
	 * @return the electronicRecharge
	 */
	public ElectronicRecharge getElectronicRecharge() {
		return electronicRecharge;
	}

	/**
	 * @param dateRecharge
	 *            the dateRecharge to set
	 */
	public void setDateRecharge(Date dateRecharge) {
		this.dateRecharge = dateRecharge;
	}

	/**
	 * @return the dateRecharge
	 */
	public Date getDateRecharge() {
		return dateRecharge;
	}

	/**
	 * @param frecuencySelect
	 *            the frecuencySelect to set
	 */
	public void setFrecuencySelect(Long frecuencySelect) {
		this.frecuencySelect = frecuencySelect;
	}

	/**
	 * @return the frecuencySelect
	 */
	public Long getFrecuencySelect() {
		return frecuencySelect;
	}

	/**
	 * @param idClientAccount
	 *            the idClientAccount to set
	 */
	public void setIdClientAccount(Long idClientAccount) {
		this.idClientAccount = idClientAccount;
	}

	/**
	 * @return the idClientAccount
	 */
	public Long getIdClientAccount() {
		return idClientAccount;
	}

	/**
	 * 
	 * @param clientAccounts
	 */
	public void setClientAccounts(List<SelectItem> clientAccounts) {
		this.clientAccounts = clientAccounts;
	}

	/**
	 * @param valueRechText
	 *            the valueRechText to set
	 */
	public void setValueRechText(String valueRechText) {
		this.valueRechText = valueRechText;
	}

	/**
	 * @return the valueRechText
	 */
	public String getValueRechText() {
		return valueRechText;
	}

	/**
	 * @param electronicRechargeId
	 *            the electronicRechargeId to set
	 */
	public void setAutomaticRechargeId(Long automaticRechargeId) {
		this.automaticRechargeId = automaticRechargeId;
	}

	/**
	 * @return the electronicRechargeId
	 */
	public Long getAutomaticRechargeId() {
		return automaticRechargeId;
	}

	/**
	 * 
	 * @param showConfirmation
	 *            the showConfirmation to set
	 */
	public void setShowConfirmation(boolean showConfirmation) {
		this.showConfirmation = showConfirmation;
	}

	/**
	 * @return the showConfirmation
	 */
	public boolean isShowConfirmation() {
		return showConfirmation;
	}

	/**
	 * @param corfirmMessage
	 *            the corfirmMessage to set
	 */
	public void setCorfirmMessage(String corfirmMessage) {
		this.corfirmMessage = corfirmMessage;
	}

	/**
	 * @return the corfirmMessage
	 */
	public String getCorfirmMessage() {
		return corfirmMessage;
	}

	/**
	 * @param automaticRechargeList
	 *            the automaticRechargeList to set
	 */
	public void setAutomaticRechargeList(
			List<ClientRecharge> automaticRechargeList) {
		this.automaticRechargeList = automaticRechargeList;
	}

	/**
	 * 
	 * Realiza la validaci�n del valor minimo a recargar. En caso de ser
	 * inv�lido muestra mensaje de error.
	 * 
	 * @return true si es valido. False de lo contrario.
	 */
	private boolean getMinValueRecharge() {
		boolean resp = true;
		ETbProcessRerchargeAVAL procType;
		Long userId = SessionUtil.sessionUser().getUserId();

		if (paymentMethod.longValue() == PaymentMethod.POSTPAID.getId()) {
			procType = ETbProcessRerchargeAVAL.PAGO_PROG_AVAL_POSTPAGO;
		} else {
			procType = ETbProcessRerchargeAVAL.PAGO_PROG_AVAL_PREPAGO;
		}

		// Inicio caso de uso CU-A08
		ServiceResponse a = electronicRecharge.checkMinRechargeValue(
				this.valueRecharge, procType, userId, this.idClientAccount);
		if (!a.isSuccess()) {
			String msj = "Transacci�n rechazada";
			if (a.getMsj() != null) {
				msj = a.getMsj();
			}

			this.setModalMessage(msj);
			this.showConfirmation = false;
			this.showModal = true;
			resp = false;
		}
		// Fin CU-A08

		return resp;
	}

	/**
	 * Retorna el valor m�nimo a recargar por saldo m�nimo
	 * 
	 * @return valor m�nimo a recargar por saldo m�nimo
	 */
	private long getMinBalanceValue() {
		TbAlarmBalance tb = alarmBalance.getInfoByType(this.idClientAccount,
				EAlarmType.MONEY);

		return tb.getValminimo();
	}

	public String getTypeProgrammingHelpTooltip() {
		return typeProgrammingHelpTooltip;
	}

	public void setTypeProgrammingHelpTooltip(String typeProgrammingHelpTooltip) {
		this.typeProgrammingHelpTooltip = typeProgrammingHelpTooltip;
	}

	public String getFrequncyHelpTooltip() {
		return frequncyHelpTooltip;
	}

	public void setFrequncyHelpTooltip(String frequncyHelpTooltip) {
		this.frequncyHelpTooltip = frequncyHelpTooltip;
	}

	public String getMinimumBalanceHelpTooltip() {
		return minimumBalanceHelpTooltip;
	}

	public void setMinimumBalanceHelpTooltip(String minimumBalanceHelpTooltip) {
		this.minimumBalanceHelpTooltip = minimumBalanceHelpTooltip;
	}

	public String getInitialDateTooltip() {
		return initialDateTooltip;
	}

	public void setInitialDateTooltip(String initialDateTooltip) {
		this.initialDateTooltip = initialDateTooltip;
	}

//	public String getRechargeValueTooltip() {
//		return rechargeValueTooltip;
//	}
//
//	public void setRechargeValueTooltip(String rechargeValueTooltip) {
//		this.rechargeValueTooltip = rechargeValueTooltip;
//	}

//	public String getAssingValueTooltip() {
//		return assingValueTooltip;
//	}
//
//	public void setAssingValueTooltip(String assingValueTooltip) {
//		this.assingValueTooltip = assingValueTooltip;
//	}

	public String getLinkDeletetooltip() {
		return linkDeletetooltip;
	}

	public void setLinkDeletetooltip(String linkDeletetooltip) {
		this.linkDeletetooltip = linkDeletetooltip;
	}

	public boolean isShowTypePerson() {
		return showTypePerson;
	}

	public void setShowTypePerson(boolean showTypePerson) {
		this.showTypePerson = showTypePerson;
	}

	public String getBalanceAcount() {
		return balanceAcount;
	}

	public void setBalanceAcount(String balanceAcount) {
		this.balanceAcount = balanceAcount;
	}

	public String getAssingRecharge() {
		return assingRecharge;
	}

	public void setAssingRecharge(String assingRecharge) {
		this.assingRecharge = assingRecharge;
	}

	public Long getRespRadioPerson() {
		return this.respRadioPerson;
	}

	public void setRespRadioPerson(Long respRadioPerson) {
		this.respRadioPerson = respRadioPerson;
	}

	public Long getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(Long paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getMessageHTML() {
		return messageHTML;
	}

	public void setMessageHTML(String messageHTML) {
		this.messageHTML = messageHTML;
	}

	public boolean isTypeFrequency() {
		return typeFrequency;
	}

	public void setTypeFrequency(boolean typeFrequency) {
		this.typeFrequency = typeFrequency;
	}

	public boolean isTypeMinimumBalance() {
		return typeMinimumBalance;
	}

	public void setTypeMinimumBalance(boolean typeMinimumBalance) {
		this.typeMinimumBalance = typeMinimumBalance;
	}

	public List<SelectItem> getTypeProgramming() {
		return typeProgramming;
	}

	public void setTypeProgramming(List<SelectItem> typeProgramming) {
		this.typeProgramming = typeProgramming;
	}

	public boolean isShowTypeProgramming() {
		return showTypeProgramming;
	}

	public void setShowTypeProgramming(boolean showTypeProgramming) {
		this.showTypeProgramming = showTypeProgramming;
	}

	public Long getTypeProgrammingSelect() {
		return typeProgrammingSelect;
	}

	public void setTypeProgrammingSelect(Long typeProgrammingSelect) {
		this.typeProgrammingSelect = typeProgrammingSelect;
	}

	public String showConfirmRemovePage() {
		StringBuilder builder=new StringBuilder();
		ClientRecharge clientRecharge=null;
		Long paymentMethodId1=null;
		
		for(int i=0;i<automaticRechargeList.size();i++)
		{
		 ClientRecharge cr=automaticRechargeList.get(i);
		 if(cr.getAutomaticRechargeId().longValue()==automaticRechargeId.longValue())
		 {
		  clientRecharge=cr;
	      break;
		 }
		}
		
		if ( clientRecharge != null)
		{
		 String XXX=null;
		 String FFFF="" + clientRecharge.getAccountId().longValue();
		 
		 paymentMethodId1 = bankEJB.getPlaymentMethodAccount(clientRecharge.getAccountId());
		 if(paymentMethodId1.longValue() == PaymentMethod.PREPAID.getId().longValue()) XXX="recarga";
		 else XXX="asignaci�n";
		 
		 builder.append("Desea desactivar la ").append(XXX).append(" de recursos programada para la cuenta FacilPass ").append(FFFF).append("?");
		}
		
		setShowConfirmation(false);
		setShowConfirmationDelete(true);
		//Incluir
		setCorfirmMessage(builder.toString());
		
		return null;
	}
	
	
	public String showConfirmRemove(){
		setShowInsert(false);
		setShowConfirmation(true);
		setCorfirmMessage("�Est� seguro que desea eliminar el Recurso Programado?");
		return null;
	}

	public Long getAvalId() {
		return this.avalId;
	}

	public void setAvalId(Long avalId) {
		this.avalId = avalId;
	}

	/**
	 * retorna las cuentas del cliente
	 */
	public List<SelectItem> getClientAccounts() {
		return clientAccounts;
	}

	/**
	 * Retorna el listado de recargas automaticas hechas
	 * 
	 * @return
	 */
	public List<ClientRecharge> getAutomaticRechargeList() {
		return this.automaticRechargeList;
	}

	/**
	 * M�todo encargado de anular recargas autom�ticas asociadas al cliente en
	 * session.
	 * 
	 * @author psanchez
	 */
	public String cancelElectronicRechargePage() {
		setShowConfirmationDelete(false);
		Long userId = userEJB.getSystemMasterById(usrs.getUserId());

		if (userId != null) {
			if (electronicRecharge.undoElectronicRecharge(automaticRechargeId,
					SessionUtil.ip(), SessionUtil.sessionUser().getUserId())) {
				this.setModalMessage("El recurso programado ha sido eliminado.");
				initAutomaticRechargeList();
				initAccounts();
			} else {
				this.setModalMessage("Error en Transacci�n.");
				this.initAutomaticRechargeList();
				this.initAccounts();
			}

			this.setShowModalDelete(true);			
		} else {
			this.initAutomaticRechargeList();
			this.initAccounts();
		}

		return null;
	}
	
	
	/**
	 * M�todo encargado de anular recargas autom�ticas asociadas al cliente en session.
	 * @author psanchez
	 */
	public String cancelElectronicRecharge(){
		
		setShowConfirmation(false);
		Long userId=userEJB.getSystemMasterById(usrs.getUserId());
		//String userCode=userEJB.getSystemUserCode(userId);
		if(userId!=null){
			if (electronicRecharge.undoElectronicRecharge(automaticRechargeId,SessionUtil.ip(), SessionUtil
					.sessionUser().getUserId())){
				this.setModalMessage("El recurso programado ha sido eliminado.");
				this.initAutomaticRechargeList();
			}else{
				this.setModalMessage("Error en Transacci�n.");
				this.initAutomaticRechargeList();
			}

			this.setShowModal(true);
		} else {
			this.initAutomaticRechargeList();
		}

		return null;		
		
	}
	

	/**
	 * M�todo para listar frecuencia
	 */
	public List<SelectItem> getFrecuecyList() {
		return frecuecyList;
	}

	/**
	 * M�todo encargado de crear recargas autom�ticas asociadas al cliente en
	 * session.
	 * 
	 * @author psanchez
	 */
	public void addElectronicRecharge() {
		long valmin;
		int diferenciaEnDias = 1;
		Date fechaActual = Calendar.getInstance().getTime();
		long tiempoActual = fechaActual.getTime();
		long unDia = diferenciaEnDias * 24 * 60 * 60 * 1000;
		Date fechaAnterior = new Date(tiempoActual - unDia);

		if (this.numFocus != 0){
			return;
		}

		this.showConfirmation = false;
		this.showModal = false;
		this.numFocus = 0;

		// Esto se deja porque Luis Gabriel indic�
		if (this.idClientAccount.longValue() == ElectronicRechargeBean.NO_SELECCION) {
			this.setModalMessage("Seleccione la Cuenta FacilPass.");
			this.showModal = true;
			return;
		}

		if (this.electronicRecharge
				.haveProductDissociation(this.idClientAccount)) {
			this.setModalMessage("Error: La Cuenta FacilPass esta en proceso de desvinculaci�n del Producto Bancario. Verifique.");
			this.showModal = true;
			return;
		}

		if (this.typeFrequency) {
			if (this.frecuencySelect.longValue() == ElectronicRechargeBean.NO_SELECCION) {
				this.setModalMessage("Seleccione la Frecuencia.");
				this.showModal = true;
				return;
			}

		}

		if (this.typeMinimumBalance) {
			if ((this.balanceAcount == null) || (this.balanceAcount.isEmpty())) {
				this.setModalMessage("Ingrese el Saldo cuenta FacilPass.");
				this.showModal = true;
				return;
			}
			
			if (!this.balanceAcount.matches("([0-9.])+")) {
				this.setModalMessage("El Saldo cuenta FacilPass tiene caracteres inv�lidos. Verifique.");
				this.showModal = true;
				return;
			}
			
			try{
				this.nbalanceAcount = Long.parseLong(this.balanceAcount.replace(".", "").replace(",", ""));
			} catch (NumberFormatException nfe) {
				nfe.printStackTrace();
				this.setModalMessage("El saldo de la cuenta FacilPass tiene caracteres inv�lidos. Verifique.");
				this.showModal = true;
				return;
			}

			if (this.nbalanceAcount < 0) {
				this.setModalMessage("Ingrese el valor del saldo de la cuenta FacilPass.");
				this.showModal = true;
				return;
			}

			valmin = this.getMinBalanceValue();
			if (this.nbalanceAcount < valmin) {
				this.setModalMessage("El valor Ingresado es menor al valor del saldo m�nimo asignado para la cuenta.");
				this.showModal = true;
				return;
			}

		}

		if ((this.valueRechText == null) || (this.valueRechText.equals(""))) {
			this.setModalMessage("Ingrese el " + this.getValueFieldName() + ".");
			this.showModal = true;
			return;
		}

		if (!this.valueRechText.matches("([0-9.])+")) {
			this.setModalMessage("El " + this.getValueFieldName() + " tiene caracteres inv�lidos. Verifique.");
			this.setShowModal(true);
			return;
		}

		try {
			this.valueRecharge = Long.parseLong(valueRechText.replace(".","").replace(",", ""));
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
			this.setModalMessage("El " + this.getValueFieldName() + " tiene caracteres inv�lidos. Verifique.");
			this.showModal = true;
			return;
		}

		if (!this.getMinValueRecharge()) {
			return;
		}

		if (this.typeFrequency) {
			if (this.dateRecharge == null) {
				this.setModalMessage("Seleccione la Fecha Inicial.");
				this.showModal = true;
				return;
			}

			if (this.dateRecharge.getTime() < fechaAnterior.getTime()) {
				this.setModalMessage("Error: La fecha inicial debe ser igual o mayor a la fecha actual. Verifique.");
				this.showModal = true;
				return;
			}

		}

		if (this.isPersonTypeVisible()) {
			if (respRadioPerson == null) {
				this.setModalMessage("Seleccione tipo de persona.");
				this.showModal = true;
				return;
			}

		}

		if (this.isEntityVisible()) {
			if (this.avalId == null) {
				this.setModalMessage("Seleccione la entidad de su producto Bancario.");
				this.showModal = true;
				return;
			}

		}

		// validaciones exitosas
		this.setModalMessage("�Est� seguro que desea asignar el Recurso Programado?");
		this.showConfirmation = true;
	}

	/**
	 * Inserta la nueva recarga programada
	 */
	public void saveElectronicRecharge() {
		if (this.paymentMethod.longValue() == PaymentMethod.PREPAID.getId()
				.longValue()) {
			this.savePrepaidProgramming();
		} else {
			this.savePostpaidProgramming();
		}

	}

	/**
	 * Realiza el proceso de generar recarga programada por prepago
	 */
	private void savePrepaidProgramming() {
		TbBankTransact tbt;
		Long bankId = this.consulteAval().getBankId();
		Long userId = SessionUtil.sessionUser().getUserId();

		TypeProgramming typeP = TypeProgramming
				.getTypePrograming(this.typeProgrammingSelect);
		Long tempBalanceValue = null;
		if (this.typeMinimumBalance) {
			tempBalanceValue = this.nbalanceAcount;
		}

		this.showConfirmation = false;
		this.showModal = false;

		ETypePerson etp = ETypePerson.getTypePerson(this.respRadioPerson);
		// Invocaci�n a CU-A03
		ServiceResponse sr = electronicRecharge.createTransactionAuthorization(
				this.getAppContext(), SessionUtil.ip(), this.idClientAccount, this.valueRecharge,
				bankId, userId, tempBalanceValue, typeP, this.frecuencySelect, etp);
		// Fin CU-A03
		if (!sr.isSuccess()) {
			setModalMessage(sr.getMsj());
			this.showModal = true;
			return;
		}

		tbt = (TbBankTransact) sr.getEntity();
		this.setModalMessage(null);
		try {
			Integer status = 1; // Pendiente

			Long masterUserId = userEJB.getSystemMasterById(this.usrs
					.getUserId());

			Long result = electronicRecharge.insertElectronicRechargePrepaid(
					tbt.getBankTransactId(), masterUserId,
					this.idClientAccount, this.valueRecharge,
					this.frecuencySelect, SessionUtil.ip(), userId,
					this.dateRecharge, this.consulteAval().getBankId(), typeP,
					status, tempBalanceValue);

			if (result == null) {
				setModalMessage("Error: Problemas procesando la solicitud.");
				showConfirmation = false;
				showModal = true;
				return;
			}

			if (result.longValue() == -1L) {
				setModalMessage("Error: La Cuenta FacilPass tiene recurso programado. Verifique.");
				showConfirmation = false;
				showModal = true;
				return;
			}

			// Se debe realizar el redireccionamiento a la url recibida
			FacesContext.getCurrentInstance().getExternalContext().redirect(tbt.getBankUrl());
		} catch (NullPointerException n) {
			setModalMessage("Error en la Transacci�n");
			showConfirmation = false;
			showModal = true;

		} catch (IOException e) {
		 setModalMessage("Problemas redirigiendo");
		 showConfirmation = false;
		 showModal = true;
		 }

	}

	/**
	 * Realiza el proceso de generar recarga programada por postpago
	 */
	private void savePostpaidProgramming() {
		Long userId = SessionUtil.sessionUser().getUserId();
		Long masterUserId = userEJB.getSystemMasterById(this.usrs.getUserId());

		TypeProgramming typeP = TypeProgramming
				.getTypePrograming(this.typeProgrammingSelect);
		Long tempBalanceValue = null;
		if (this.typeMinimumBalance) {
			tempBalanceValue = this.nbalanceAcount;
		}

		Long result = electronicRecharge.insertElectronicRechargePostpaid(
				SessionUtil.ip(), userId, this.idClientAccount,
				this.valueRecharge, this.frecuencySelect, masterUserId,
				this.dateRecharge, typeP, tempBalanceValue);

		if (result == null) {
			setModalMessage("Problemas realizando la transacci�n");
			showConfirmation = false;
			showModal = true;
			return;
		}

		// No se present� error
		setModalMessage("Transacci�n Exitosa");
		showConfirmation = false;
		showModal = true;
		this.restaurarDatos = true;
	}

	/**
	 * Retorna la entidad AVAL seleccionada
	 * 
	 * @return Entidad seleccionada o null
	 */
	private TbBank consulteAval() {
		TbBank res = null;

		if (this.avalId == null) {
			return res;
		}

		for (TbBank tb : this.entidades) {
			if (tb.getBankId().equals(this.avalId)) {
				res = tb;
				break;
			}

		}

		return res;
	}

	/**
	 * Hides modal windows.
	 */
	public String hideModal() {
		this.setShowInsert(false);
		this.setShowModal(false);
		this.setShowModalDelete(false);
		this.setShowConfirmationDelete(false);
		this.setShowConfirmation(false);		
		this.setModalMessage(null);
		if (this.restaurarDatos) {
			this.initFields();
			this.initAutomaticRechargeList();
			this.initAccounts();
			this.initMainMessage();
		}

		return null;
	}

	/**
	 * Controla el evento de selecci�n del tipo de programaci�n
	 */
	public void frequencySelected(){
		this.hideFieldsByFrequency();

		if (this.idClientAccount != ElectronicRechargeBean.NO_SELECCION
				&& this.typeProgrammingSelect != ElectronicRechargeBean.NO_SELECCION) {
			this.loadFrequencySelected();
		} else {
			this.ocultTot();
		}

	}

	/**
	 * Carga los toolTips y valores de frecuencia o saldo seleccionado
	 */
	private void loadFrequencySelected(){
		// Tipo Frecuencia
		this.setTypeFrequency(typeProgrammingSelect.longValue() == TypeProgramming.FREQUENCY
				.getId());
		// frecuencia y saldo minimo son valores opuestos
		this.setTypeMinimumBalance(!this.typeFrequency);
		if (this.typeFrequency) {
			// PostPago
			if (this.paymentMethod.longValue() == PaymentMethod.POSTPAID.getId()) {
				this.setMessageHTML(confAutomaticProgrammingEJB
						.getMessageTooltip(TypeConfAutomaticRecharge.FREQUENCY_HELP_MESSAGE_POS
								.getId()));
				this.frequncyHelpTooltip = confAutomaticProgrammingEJB
						.getMessageTooltip(TypeConfAutomaticRecharge.FREQUENCY_HELP_TOOLTIP_POS
								.getId());
			} else {
				// Prepago
				setMessageHTML(confAutomaticProgrammingEJB
						.getMessageTooltip(TypeConfAutomaticRecharge.FREQUENCY_HELP_MESSAGE_PRE
								.getId()));
				this.frequncyHelpTooltip = confAutomaticProgrammingEJB
						.getMessageTooltip(TypeConfAutomaticRecharge.FREQUENCY_HELP_TOOLTIP_PRE
								.getId());
			}

		} else {
			// Tipo saldo minimo
			if (this.paymentMethod.longValue() == PaymentMethod.POSTPAID.getId()) {
				// postpago
				this.setMessageHTML(confAutomaticProgrammingEJB
						.getMessageTooltip(TypeConfAutomaticRecharge.MINIMUMBALANCE_HELP_MESSAGE_POS
								.getId()));
				this.minimumBalanceHelpTooltip = confAutomaticProgrammingEJB
						.getMessageTooltip(TypeConfAutomaticRecharge.MINIMUMBALANCE_HELP_TOOLTIP_POS
								.getId());
			} else {
				// prepago
				this.setMessageHTML(confAutomaticProgrammingEJB
						.getMessageTooltip(TypeConfAutomaticRecharge.MINIMUMBALANCE_HELP_MESSAGE_PRE
								.getId()));
				this.minimumBalanceHelpTooltip = confAutomaticProgrammingEJB
						.getMessageTooltip(TypeConfAutomaticRecharge.MINIMUMBALANCE_HELP_TOOLTIP_PRE
								.getId());
			}

		}

	}
	
	/**
	 * Limpia los elementos y los presenta acorde a la selecci�n de la cuenta y el tipo de programaci�n del usuario
	 */
	public void showElements() {
		if (this.idClientAccount != ElectronicRechargeBean.NO_SELECCION
				&& this.typeProgrammingSelect != ElectronicRechargeBean.NO_SELECCION) {
			this.loadFrequencySelected();
		} else {
			this.ocultTot();
		}

	}

	/**
	 * consulta si la cuenta tiene programaciones pendientes
	 */
	public void modalityAccount() {
		this.ocultTot();

		if (this.idClientAccount != ElectronicRechargeBean.NO_SELECCION) {
			this.setPaymentMethod(bankEJB.getPlaymentMethodAccount(this.idClientAccount));
			this.typeProgrammingSelect = ElectronicRechargeBean.NO_SELECCION;
			this.showElements();
			
			if (this.getPaymentMethod().longValue() == PaymentMethod.PREPAID.getId().longValue() ){
				// Se activa la sonda cliente solo si es cuenta prepago
				Long userId = SessionUtil.sessionUser().getUserId();
				String ip = SessionUtil.ip();
				int resp;
	
				try {
					// Proceso de Sonda Cliente CU_A01
					resp = sonda.processClientProbe(this.idClientAccount, ip,
							userId);
					if (resp == ProgramaticRechargeEvaluation.ACCOUNT_WITH_ERROR
							|| resp == ProgramaticRechargeEvaluation.ACCOUNT_WITH_PENDING_TRANSACTIONS) {
						this.idClientAccount = ElectronicRechargeBean.NO_SELECCION;
						this.setModalMessage("En este momento tiene una autorizaci�n pendiente, por favor intente m�s tarde");
						this.showConfirmation = false;
	
						this.showModal = true;
					}
	
				} catch (Exception e) {
					this.idClientAccount = ElectronicRechargeBean.NO_SELECCION;
					setModalMessage("Problemas consultando autorizaciones. Por favor intente m�s tarde");
					this.showConfirmation = false;
					this.showModal = true;
				}

			}

		}
//		else {
//			this.ocultTot();
//		}

	}

	/**
	 * Limpia valores
	 */
	public void ocultTot() {
		this.hideFieldsByFrequency();
		this.setTypeProgrammingSelect(ElectronicRechargeBean.NO_SELECCION);
	}

	/**
	 * Limpia valores, excepto el tipo de programaci�n seleccionado
	 */
	private void hideFieldsByFrequency(){
		this.balanceAcount = null;
		this.valueRechText = null;
		this.setFrecuencySelect(ElectronicRechargeBean.NO_SELECCION);
		this.setTypeMinimumBalance(false);
		this.setTypeFrequency(false);
		this.respRadioPerson = ElectronicRechargeBean.NO_SELECCION;
		this.avalId = null;
		this.messageHTML = confAutomaticProgrammingEJB
				.getMessageTooltip(TypeConfAutomaticRecharge.START_HELP_MESSAGE
						.getId());
	}

	/**
	 * Retorna el contexto de la aplicacion basado en la url
	 * 
	 * @return Contexto de la aplicacion
	 */
	private String getAppContext() {
		HttpServletRequest origRequest = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();


		String context = origRequest.getRequestURL().substring(0,
				origRequest.getRequestURL()

				.indexOf(origRequest.getPathInfo()) + 1);

		return context;
	}

	/**
	 * Indica si se debe mostrar el campo tipo de persona
	 * 
	 * @return true si hay que mostrarlo por el editor de texto. Se selecciono
	 *         tipo de programacion. La cuenta seleccionada es prepago
	 * 
	 */
	public boolean isPersonTypeVisible() {
		boolean resp = false;

		if (this.showTypePerson) {
			if (this.paymentMethod != null) {
				if (this.paymentMethod.longValue() == PaymentMethod.PREPAID
						.getId().longValue()) {

					resp = this.typeMinimumBalance || this.typeFrequency;
				}

			}

		}

		return resp;
	}
	
	/**
	 * @param showInsert the showInsert to set
	 */
	public void setShowInsert(boolean showInsert) {
		this.showInsert = showInsert;
	}

	/**
	 * @return the showInsert
	 */
	public boolean isShowInsert() {
		return showInsert;
	}

	/**
	 * Indica si se debe mostrar el campo Entidad
	 * 
	 * @return true si se selecciono cuenta de tipo prepago. Se selecciono tipo
	 *         de programacion
	 */
	public boolean isEntityVisible() {
		boolean resp = false;

		if (this.paymentMethod != null) {
			if (this.paymentMethod.longValue() == PaymentMethod.PREPAID.getId()
					.longValue()) {

				resp = this.typeMinimumBalance || this.typeFrequency;
			}

		}

		return resp;
	}

	/**
	 * Retorna el label para Saldo M�nimo acorde al tipo de pago asociado a la
	 * cuenta
	 * 
	 * @return valor del campo
	 */
	public String getMsgBalValue() {
		this.msgBalValue = "";
		if (this.paymentMethod != null) {
			this.msgBalValue = this.getValueFieldName() + ": ";
		}

		return this.msgBalValue;
	}

	/**
	 * Retorna el nombre del campo valor (a asignar/recargar) acorde al tipo de cuenta seleccionada
	 * @return recargar/asignar o vac�o en caso de no haber seleccionado cuenta
	 */
	private String getValueFieldName(){
		String resp = "";
		if (this.paymentMethod != null) {
			if (this.paymentMethod.longValue() == PaymentMethod.PREPAID.getId()) {
				resp = "Valor a recargar";
			} else {
				resp = "Valor a asignar";
			}

		}
		
		return resp;
	}
	
	/**
	 * Retorna el valor del tooltip acorde al valor del campo del valor de recarga
	 * @return valor del tooltip
	 */
	public String getValueFieldToolTip(){
		String resp = "";
		if (this.paymentMethod != null) {
			if (this.paymentMethod.longValue() == PaymentMethod.PREPAID.getId()) {
				resp = this.rechargeValueTooltip;
			} else {
				resp = this.assingValueTooltip;
			}

		}

		return resp;
	}

	/**
	 * Indica si se seleccion� una cuenta prepago
	 * @return
	 */
	public boolean isAccountSelected(){
		boolean resp = false;
		
		resp = (this.idClientAccount.longValue() !=  ElectronicRechargeBean.NO_SELECCION) && (this.paymentMethod != null);
//		if ( (this.idClientAccount.longValue() !=  ElectronicRechargeBean.NO_SELECCION) && (this.paymentMethod != null) ){
//			resp = this.paymentMethod.longValue() == PaymentMethod.PREPAID.getId().longValue();
//		}
		
		return resp;
	}

	/**
	 * Indica si la primer grafica debe estar habilitada
	 * @return true si la lista de items de entidades tiene al menos un elemento
	 */
	public boolean isGraphic1Enabled(){
		return !this.entityList.isEmpty();
	}

	/**
	 * Indica si la segunda grafica debe estar habilitada
	 * @return true si la lista de items de entidades tiene al menos dos elemento
	 */
	public boolean isGraphic2Enabled(){
		return this.entityList.size() > 1;
	}

	/**
	 * Indica si la tercer grafica debe estar habilitada
	 * @return true si la lista de items de entidades tiene al menos tres elemento
	 */
	public boolean isGraphic3Enabled(){
		return this.entityList.size() > 2;
	}

	/**
	 * Indica si la cuarta grafica debe estar habilitada
	 * @return true si la lista de items de entidades tiene al menos cuatro elemento
	 */
	public boolean isGraphic4Enabled(){
		return this.entityList.size() > 3;
	}

	/**
	 * Retorna el path de la primer gr�fica que se despliega
	 * @return path de la primer gr�fica
	 */
	public String getGraphic1Path(){
		String path = "";
		if (!this.entityList.isEmpty()){
			path = this.getPath((Long)this.entityList.get(0).getValue());
		}

		return path;
	}

	/**
	 * Retorna el path de la segunda gr�fica que se despliega
	 * @return path de la segunda gr�fica
	 */
	public String getGraphic2Path(){
		String path = "";
		if (this.entityList.size() > 1){
			path = this.getPath((Long)this.entityList.get(1).getValue());
		}

		return path;
	}

	/**
	 * Retorna el path de la tercer gr�fica que se despliega
	 * @return path de la tercer gr�fica
	 */
	public String getGraphic3Path(){
		String path = "";
		if (this.entityList.size() > 2){
			path = this.getPath((Long)this.entityList.get(2).getValue());
		}

		return path;
	}

	/**
	 * Retorna el path de la cuarta gr�fica que se despliega
	 * @return path de la cuarta gr�fica
	 */
	public String getGraphic4Path(){
		String path = "";
		if (this.entityList.size() > 3){
			path = this.getPath((Long)this.entityList.get(3).getValue());
		}

		return path;
	}

	/**
	 * Retorna el path de la imagen acorde al id de la entidad bancaria
	 * @param id identificador de la entidad bancaria
	 * @return path de la imagen
	 */
	private String getPath(Long id){
		// Este metodo y los que lo invocan se crean porque se supone SOLO se manejan 4 entidades aval
		// y las im�genes est�n quemadas en rutas espec�ficas, que no est�n parametrizadas
		String path = "";
		int val = id.intValue();

		switch(val){
		case 1 :
			path = "/img/bbta.png";
			break;
		case 2 :
			path = "/img/popular.png";
			break;
		case 23 :
			path = "/img/occidente.png";
			break;
		case 52 :
			path = "/img/avVillas.png";
			break;
		}
		return path;
	}

	/**
	 * Retorna la cantidad de entidades a presentar
	 * @return this.entityList.size();
	 */
	public int getGraphicCount(){
		return this.entityList.size();
	}
	
	public void setMsgBalValue(String msgBalValue) {
		this.msgBalValue = msgBalValue;
	}

	public List<SelectItem> getPersonList() {
		return this.personList;
	}

	public void setPersonList(List<SelectItem> personList) {
		this.personList = personList;
	}

	public String getParamMsg() {
		String resp = "";
		if (this.paymentMethod.longValue() == PaymentMethod.PREPAID.getId().longValue()){
			resp = this.param71;
		} else {
			resp = this.param30;
		}

		return resp;
	}

//	public void setParamMsg(String paramMsg) {
//		this.paramMsg = paramMsg;
//	}

	public List<SelectItem> getEntityList() {
		return this.entityList;
	}

	public void setEntityList(List<SelectItem> entityList) {
		this.entityList = entityList;
	}

	public int getNumFocus() {
		return this.numFocus;
	}

	public void setNumFocus(int numFocus) {
		this.numFocus = numFocus;
	}	

	/**
	 * Retorna la altura de la ventana para mostrar mensaje del modal message
	 * @return
	 */
	public int getAdviceHeight(){
		int resp = 115;
		int len, resto, lineas;
		
		if (this.modalMessage != null && !this.modalMessage.isEmpty()) {
			len = this.modalMessage.length();
			// 110 es la longitud m�xima donde no se afecta en nada
			resto = len - 110;
			if (resto > 0) {
				// 55 es la cantidad de caracteres por l�nea en la ventana
				lineas = (resto / 55) + 1;
				// 29 es la proporci�n de l�neas en la ventana
				resp = resp + (15 * lineas);
			}

		}

		return resp;
	}

	/**
	 * Verifica si el saldo ingresado es mayor al minimo configurado para la cuenta
	 */
	public void onBalanceBlur(){
		long minBal = this.getMinBalanceValue();
		
//		if (this.showModal) {
		if (this.showMinimumBalanceModal){
			return;
		}
		
		this.numFocus = 0;

		if (this.paymentMethod.longValue() == PaymentMethod.PREPAID.getId().longValue() ){
		
			if ((this.balanceAcount != null) && (!this.balanceAcount.isEmpty())) {
				if (!this.balanceAcount.matches("([0-9.])+")) {
//					this.setModalMessage("El Saldo cuenta FacilPass tiene caracteres inv�lidos. Verifique.");
//					this.showModal = true;
					this.modalMinimumBalanceMessage = "El Saldo cuenta FacilPass tiene caracteres inv�lidos. Verifique.";
					this.showMinimumBalanceModal = true;
					this.numFocus = 1;
					this.balanceAcount = "";
					return;
				}
				
				this.nbalanceAcount = Long.parseLong(this.balanceAcount.replace(".", "").replace(",", ""));
				if (this.nbalanceAcount < minBal) {
					DecimalFormat dFormat = new DecimalFormat("#,###,###,###");
//					this.setModalMessage("El campo Saldo cuenta FacilPass no puede ser menor a $" + dFormat.format(minBal));
//					this.showModal = true;
					this.modalMinimumBalanceMessage = "El campo Saldo cuenta FacilPass no puede ser menor a $" + dFormat.format(minBal);
					this.showMinimumBalanceModal = true;
					this.numFocus = 1;
					this.balanceAcount = "";
					return;
				}
	
			}

		}

	}

	/**
	 * Verifica si el saldo ingresado es mayor al minimo configurado para la cuenta
	 */
	public void onMouseOut(){
		long minBal = this.getMinBalanceValue();
		
//		if (this.showModal){
		if (this.showMinimumBalanceModal){
			return;
		}

		this.numFocus = 0;

		if (this.paymentMethod.longValue() == PaymentMethod.PREPAID.getId().longValue() ){
		
			if ((this.balanceAcount != null) && (!this.balanceAcount.isEmpty())) {
				if (!this.balanceAcount.matches("([0-9.])+")) {
					// this.setModalMessage("El Saldo cuenta FacilPass tiene caracteres inv�lidos. Verifique.");
//					this.showModal = true;
					this.modalMinimumBalanceMessage = "El Saldo cuenta FacilPass tiene caracteres inv�lidos. Verifique.";
					this.showMinimumBalanceModal = true;
					this.numFocus = 1;
					this.balanceAcount = "";
					return;
				}
				
				this.nbalanceAcount = Long.parseLong(this.balanceAcount.replace(".", "").replace(",", ""));
				if (this.nbalanceAcount < minBal) {
					DecimalFormat dFormat = new DecimalFormat("#,###,###,###");
//					this.setModalMessage("El campo Saldo cuenta FacilPass no puede ser menor a $" + dFormat.format(minBal));
//					this.showModal = true;
					this.modalMinimumBalanceMessage = "El campo Saldo cuenta FacilPass no puede ser menor a $" + dFormat.format(minBal);
					this.showMinimumBalanceModal = true;
					this.numFocus = 1;
					this.balanceAcount = "";
					return;
				}
	
			}

		}
		
	}

	/**
	 * Indica si se debe validar el campo de saldo m�nimo
	 * @return true si seleccion� saldo m�nimo y la cuenta seleccionada es prepago
	 */
	public boolean isBalanceValidated(){
		return this.validateBalance(true);
	}

	/**
	 * Indica si no se debe validar el campo de saldo m�nimo
	 * @return true si seleccion� saldo m�nimo y la cuenta seleccionada es prepago
	 */
	public boolean isBalanceNotValidated(){
		return this.validateBalance(false);
	}

	/**
	 * Indica si se debe o no validar el campo de saldo m�nimo acorde al tipo de cuenta seleccionada
	 * @param prepaid indica el tipo de cuenta seleccionada
	 * @return 
	 */
	private boolean validateBalance(boolean prepaid){
		if (!this.typeMinimumBalance){
			return false;
		}

		if (prepaid){
			return ( (this.paymentMethod != null) && ( this.paymentMethod.longValue() == PaymentMethod.PREPAID.getId().longValue() ) );
		}
		
		return ( (this.paymentMethod != null) && ( this.paymentMethod.longValue() != PaymentMethod.PREPAID.getId().longValue() ) );
	}

	/**
	 * Oculta la venta de aviso de error de valor de saldo m�nimo
	 */
	public void hideMinimumBalanceModal(){
		this.showMinimumBalanceModal = false;
		this.modalMinimumBalanceMessage = "";
	}

	public boolean isShowMinimumBalanceModal() {
		return this.showMinimumBalanceModal;
	}

	public void setShowMinimumBalanceModal(boolean showMinimumBalanceModal) {
		this.showMinimumBalanceModal = showMinimumBalanceModal;
	}

	public String getModalMinimumBalanceMessage() {
		return this.modalMinimumBalanceMessage;
	}

	public void setModalMinimumBalanceMessage(String modalMinimumBalanceMessage) {
		this.modalMinimumBalanceMessage = modalMinimumBalanceMessage;
	}

}
