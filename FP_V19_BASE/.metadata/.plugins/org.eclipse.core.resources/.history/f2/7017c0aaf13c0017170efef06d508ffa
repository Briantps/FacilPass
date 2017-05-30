package ejb;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import constant.ETbProcessRerchargeAVAL;
import constant.ETypePerson;
import constant.TypeProgramming;

import util.ClientRecharge;
import util.ReElectronicRecharge;
import util.ServiceResponse;

import jpa.TbBankTransact;
import jpa.TbFrequency;
import jpa.TbTypeProgramming;

@Remote
public interface ElectronicRecharge {

	public List<TbFrequency> getListFrequency();

	/**
	 * Crea el proceso con detalle para el inicio de solicitud acorde al tipo de programacion indicado de tipo prepago
	 * @param bankTransactId identificador de la transaccion bancario generada previamente para la recarga electronica a crear
	 * @param userId usuario al cual se le realiza el inicio
	 * @param idClientAccount identificador de la cuenta
	 * @param valueRecharge valor a recargar
	 * @param frecuencySelect tipo de frecuencia
	 * @param ip direccion ip de la maquina
	 * @param creationUser usuario que lo crea
	 * @param dateRecharge fecha de inicio seleccionada
	 * @param entityId identificador de la entidad AVAL seleccionada
	 * @param tPrograming tipo de programacion seleccionada
	 * @param status estado (aceptada/ rechazada)
	 * @param balanceValue valor del saldo
	 * @return identificador de la recarga automatica generada
	 * @author TPS r.bautista
	 */
	public Long insertElectronicRechargePrepaid(Long bankTransactId , Long userId, Long idClientAccount, Long valueRecharge, Long frecuencySelect,
			String ip, Long creationUser, Date dateRecharge, Long entityId, TypeProgramming tPrograming, Integer status, Long balanceValue);

	/**
	 * Realiza el registro de la transaccion automatica de tipo postpago
	 * @param ip direccion IP de la maquina
	 * @param userId usuario al cual se le asocia la recarga automatica
	 * @param idClientAccount identificador de la cuenta FacilPass
	 * @param valueRecharge valor a recarga
	 * @param frecuencySelect tipo de frecuencia seleccionada
	 * @param creationUser usuario para auditoria
	 * @param dateRecharge fecha desde cuando inicia la programacion
	 * @param tPrograming tipo de programacion
	 * @param balanceValue valor del saldo minimo
	 * @return identificador del la recarga generada
	 * @author TPS r.bautista
	 */
	public Long insertElectronicRechargePostpaid(String ip, Long userId, Long idClientAccount, Long valueRecharge, Long frecuencySelect, 
			Long creationUser, Date dateRecharge, TypeProgramming tPrograming, Long balanceValue);
	
	
	public List<ReElectronicRecharge> getAccountAsociatesByClient(Long userCode);
	
	/**
	 * Método creado para listar las recargas automáticas de cliente en sesión
	 * @param userId identificador del usuario
	 * @author TPS r.bautista
	 */
	public List<ClientRecharge> getAutomaticRechargebyClient(Long userId);
	
	public boolean undoElectronicRecharge(Long automaticRechargeId, String ip,
			Long creationUser);

	public boolean haveProductDissociation(Long idClientAccount);
	
	public List<TbTypeProgramming> getListTypeProgramming();

	/**
	 * Retorna la TbBankTtransact asociada a la url indicada
	 * @param url identificador del TbBankTtransact
	 * @return TbBankTtransact o null
	 * @author TPS r.bautista
	 */
	public TbBankTransact getBankTransactByURL(String url);

	/**
	 * Actualiza proceso y detalle de proceso de la recarga automatica acorde a si fue aceptada o rechazada
	 * Actualiza el estado de la recarga automatica a aceptada o eliminada
	 * @param bankTransactId id bank transaction para cargar la informacion 
	 * @param entityId identificador de la entidad
	 * @param aceptada indica si fue aceptada o rechazada
	 * @param authorUserId usuario que realiza la transaccion
	 * @param ip Direccion ip de la maquina
	 * @param errorId identificador del error del web service, en caso de existir
	 * @author TPS r.bautista
	 */
	public void updateElectronicRecharge(Long bankTransactId, Long entityId, boolean aceptada, Long authorUserId, String ip, Long errorId );
	
	/**
	 * Genera la transaccion autorizacion recarga programada en la entidad
	 * @param contexto contexto de la aplicacion
	 * @param ip ip desde donde se consume el servicio
	 * @param accountId identificador de la cuenta facilpass
	 * @param value valor de la recarga
	 * @param bankId identificador del banco
	 * @param userId usuario sobre el cual se realiza la operacion
	 * @param balanceValue valor del saldo
	 * @param type tipo de programacion (frecuencia/saldo minimo)
	 * @param freqId identificador del tipo de frecuencia
	 * @param eTypePeron indica el tipo de persona seleccionada en caso de ser necesario
	 * @return Respuesta con mensajes y la TbBankTransact generada
	 * @author TPS r.bautista
	 */
	public ServiceResponse createTransactionAuthorization(String context, String ip, Long accountId, Long value, Long bankId, Long userId, Long balanceValue, TypeProgramming type, Long freqId, ETypePerson eTypePeron);

	/**
	 * Verifica la url. Si no existe o ya fue usada, envía correo con el error. En caso de ser válida la inactiva
	 * @param userId usuario que realiza la carga de la pagina
	 * @param url url recibida por parametro
	 * @param ip ip de la maquina que realiza la soicitud
	 * @return ServiceResponse con WsRtaConsultTransAval con la transaccion bancaria asociada
	 * @author TPS r.bautista
	 */
	public ServiceResponse checkURL(Long userId, String url, String ip);

	/**
	 * Valida si el valor indicado es mayor a el valor minimo de canal o acuerdo.
	 * Si no hay parametrizacion entre el tipo de proceso y canal-acuerdo lanza NoParametrizationException y envia mensaje 
	 * @param value valor a validar
	 * @param processType tipo de proceso
	 * @param userId usuario que usa el servicio
	 * @param accountId cuenta sobre la cual se realiza el chequeo
	 * @return respuesta del servicio ejecutado
	 * @author TPS r.bautista
	 */
	public ServiceResponse checkMinRechargeValue(Long value, ETbProcessRerchargeAVAL processType, Long userId, Long accountId); 

	/**
	 * Retorna el minimo cuando se selecciona una cuenta de tipo prepago
	 * @return Valor configurado para cuentas prepago
	 */
	public String getPrepaidValue();
	
	/**
	 * Retorna el valor cuando se selecciona una cuenta de tipo pospago
	 * @return Valor configurado para cuenta pospago
	 */
	public String getPostPaidValue();

}
