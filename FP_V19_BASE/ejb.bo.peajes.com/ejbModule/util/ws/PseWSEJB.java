package util.ws;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;

import jpa.ReAccountBank;
import jpa.TbAccount;
import jpa.TbAgreement;
import jpa.TbAvalTransaction;
import jpa.TbChanel;
import jpa.TbProcessTrack;
import jpa.TbPseTransaction;
import jpa.TbSystemUser;
import jpa.TbUmbral;
import radomic.Radom;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import util.HistoricalRecharges;
import business.WSPse;

import com.ath.service.payments.pseservices.BankACHData;
import com.avvillas.cpv.ws.ClsDatosCrearTrandaccion;
import com.avvillas.cpv.ws.ClsEstadoTransaccion;
import com.avvillas.cpv.ws.ClsRespuestaCrearTransaccion;
import com.corficolombiana.facilpass.ws.connector.gpmntcommerce.dto.AddTransactionResponseDTO;
import com.corficolombiana.facilpass.ws.connector.gpmntcommerce.dto.CurrencyAmtDTO;
import com.corficolombiana.facilpass.ws.connector.gpmntcommerce.dto.CustNameDTO;
import com.corficolombiana.facilpass.ws.connector.gpmntcommerce.dto.FeeDTO;
import com.corficolombiana.facilpass.ws.connector.gpmntcommerce.dto.GetTransactionResponseDTO;
import com.corficolombiana.facilpass.ws.connector.gpmntcommerce.dto.PersonalDataDTO;
import com.corficolombiana.facilpass.ws.connector.gpmntcommerce.dto.ReferenceDTO;
import com.corficolombiana.facilpass.ws.connector.gpmntcommerce.dto.SecretListDTO;
import com.facilpass.util.MessageParser;
import com.facilpass.util.classes.SimpleTagMessageParser;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import constant.EEmailParam;
import constant.EmailProcess;
import constant.LogAction;
import constant.LogReference;
import constant.ProcessTrackDetailType;
import constant.ProcessTrackType;
import ejb.Log;
import ejb.Process;
import ejb.SystemParameters;
import ejb.User;
import ejb.email.Outbox;

/**
 * 
 * @author jromero
 * Ejb para el servicio PSE
 */
@Stateless(mappedName = "util/ws/PseWS")
public class PseWSEJB implements PseWS{
	
	@PersistenceContext(unitName = "bo")
	EntityManager em;
	
	@EJB(mappedName = "ejb/Log")
	private Log log;

	@EJB(mappedName="ejb/Process")
	private Process process;
	
	@EJB(mappedName="ejb/User")
	private User user;
	
	@EJB(mappedName ="ejb/email/Outbox")
	private Outbox outbox;
	
	@EJB(mappedName ="ejb/SystemParameters")
	private SystemParameters parametros;
	
	@EJB(mappedName = "util/ws/WSRecharge")
	private WSRecharge WSrecharge;
	
	
	@EJB(mappedName = "util/ws/WSservices")
	private WSservices services ;
	
	
	private static final String CHARSET_UTF_8 = "UTF-8";

	public PseWSEJB() {
		super();
	}
	
	@PostConstruct
	public void init() {

	}

	@Override
	public List<BankACHData> getPSEBanks(Long usrs, Long idClientAccount) {
		List<com.ath.service.payments.pseservices.BankACHData> r=new ArrayList<BankACHData>();
		WSPse ws=new WSPse();
		String msj = "Error de conexi�n con ThomasConsumoPSE.jar al generar lista de Entidades PSE:  ";
		try {
			//ws.setConnection(this.getConnection());
			String stringUrl = parametros.getParameterValueById(61L);
			ws.setStringUrl(stringUrl);
			r=ws.getPSEBanks();
			System.out.println("r.size: "+r.size());
			if(r.size() == 0){
				System.out.println("getPSEBanks.Error al generar la lista de entidades vacia");
				String msj1 = "Error en generar la lista de Entidades PSE. La lista se escuentra vac�a:  ";
				ArrayList<String> parameters=new ArrayList<String>();
				parameters.add("#PSEERR="+ msj1);		
				System.out.println("Notificaci�n Error al generar la lista de entidades");
				outbox.insertMessageOutbox(usrs, 
						EmailProcess.ERROR_LIST_BANK_PSE,
						idClientAccount,
						null,
						null, 
						null,
						null,
						null,
						null,
						null,
						usrs,
						null,
						null,
						null,
						true,
						parameters);
				System.out.println("Error lista entidades PSE Enviado ");
			}
		} catch (Exception e) {
			System.out.println("getPSEBanks().Error de conexi�n con ThomasConsumoPSE.jar al generar lista de Entidades PSE");
			
			ArrayList<String> parameters=new ArrayList<String>();
			parameters.add("#PSEERR="+ msj + (e.getMessage() == null?e: e.getMessage())); 			
			System.out.println("Notificaci�n Error de conexi�n con ThomasConsumoPSE.jar al generar lista de Entidades PSE");
			outbox.insertMessageOutbox(usrs, 
	                   EmailProcess.ERROR_LIST_BANK_PSE,
	                   idClientAccount,
	                   null,
	                   null, 
	                   null,
	                   null,
	                   null,
	                   null,
	                   null,
	                   usrs,
	                   null,
		               null,
		               null,
	                   true,
	                   parameters);
			
			return r;
		}
		return r;
		
	}
		
	public ClsRespuestaCrearTransaccion createTransaction(ClsDatosCrearTrandaccion cl, Long usrId, Long idAccount, String nombreBanco) throws Exception{
		
		ClsRespuestaCrearTransaccion cls = null;
		String RESPONCE_TYPE_COD = null;
		String msj = "Error al establecer conexi�n con la libraria ThomasConsumoPse.jar para Crear la Transacci�n:  ";
		WSPse ws=new WSPse();
		//Connection c = null;
		try{	
			/*c = this.getConnection();
			ws.setConnection(c);*/
			String stringUrl = parametros.getParameterValueById(61L);
			ws.setStringUrl(stringUrl);
			cls=ws.createTransaction(cl);
			
		}catch (Exception e) {
			System.out.println("createTransaction.Error al crear la transacci�n");
			if (cls!=null) {
				RESPONCE_TYPE_COD = String.valueOf(cls.getCodigoEstado());
				ArrayList<String> parameters=new ArrayList<String>();
				parameters.add("#PSEERR="+ msj + (e.getMessage() == null ? e:e.getMessage()));
				parameters.add("#PSECODERR="+(RESPONCE_TYPE_COD==null?"0":RESPONCE_TYPE_COD));
				parameters.add("#PSECB="+(cl.getPseCodigoBanco() == null || cl.getPseCodigoBanco().isEmpty()? " ": cl.getPseCodigoBanco()));
				parameters.add("#PSENB="+(nombreBanco == null || nombreBanco.isEmpty()?" ": nombreBanco));
				parameters.add("#VALREC="+(cl.getValor() == null ? 0L: cl.getValor()));
				System.out.println("PseWSEJB.parameters: "+parameters.toString());
				
				System.out.println("Notificaci�n Error de conexi�n con la libraria en CrearTransacci�n");
				outbox.insertMessageOutbox(usrId, 
						EmailProcess.PSE_ERROR,
						idAccount,
						null,
						null, 
						null,
						null,
						null,
						null,
						null,
						usrId,
						null,
						null,
						null,
						true,
						parameters);
			}
			throw e;
		}
		
		return cls;
	}

	public ClsEstadoTransaccion finishTransaction(Long trasaction, Long usrId, Long idAccount, String codBanc,String nomBanc, Long umbralId ) throws Exception{
		WSPse ws=new WSPse();
		ClsEstadoTransaccion cls= null;
		String RESPONCE_TYPE_COD = null;
		String msj = "Error al establecer conexi�n con la libreria ThomasConsumoPse.jar para finalizar la Transacci�n:  ";
		//Connection c = null;
		try{
			//c = this.getConnection();
			//ws.setConnection(c);
			String stringUrl = parametros.getParameterValueById(61L);
			ws.setStringUrl(stringUrl);
			cls=ws.finishTransaction(trasaction);
		}catch (Exception e) {
			System.out.println("BOS.finishTransaction().Error al finalizar la transacci�n:  Error de conexi�n con la libreria en finishTransaction");
			if (cls!=null) {
				RESPONCE_TYPE_COD = String.valueOf(cls.getCodigoEstado());
				e.printStackTrace();
				ArrayList<String> parameters=new ArrayList<String>();
				parameters.add("#PSEERR="+ msj + (e.getMessage() == null ? e : e.getMessage()));//codeError);
				parameters.add("#PSECODERR="+(RESPONCE_TYPE_COD==null?"0":RESPONCE_TYPE_COD));
				parameters.add("#PSECB="+(codBanc == null || codBanc.isEmpty()?" ":codBanc));
				parameters.add("#PSENB="+(nomBanc == null || nomBanc.isEmpty()? " ": nomBanc));
				parameters.add("#VALREC="+(umbralId == null ? 0L : umbralId));
				System.out.println("PseWSEJB.parameters: "+parameters.toString());
			
				outbox.insertMessageOutbox(usrId, 
		                   EmailProcess.PSE_ERROR,
		                   idAccount,
		                   null,
		                   null, 
		                   null,
		                   null,
		                   null,
		                   null,
		                   null,
		                   usrId,
		                   null,
			               null,
			               null,
		                   true,
		                   parameters);
			}
			throw e;
		}
		return cls;
	}
	
	public ClsEstadoTransaccion getEstadoTransaction(Long trasaction, Long usrId, Long idAccount, String codBanc,String nomBanc, Long umbralId) throws Exception{
		WSPse ws=new WSPse();
		ClsEstadoTransaccion cls=null;
		String RESPONCE_TYPE_COD = null;
		String msj = "Error al establecer conexi�n con la libraria ThomasConsumoPse.jar al validar el estado de la transacci�n:  ";
		//Connection c = null;
		try{
			//c = this.getConnection();
			//ws.setConnection(c);
			String stringUrl = parametros.getParameterValueById(61L);
			ws.setStringUrl(stringUrl);
			cls=ws.getEstadoTransaction(trasaction);
		}catch (Exception e) {
			if (cls!=null) {
				RESPONCE_TYPE_COD = String.valueOf(cls.getCodigoEstado());
				System.out.println("BOS.getEstadoTransaction().Error al validar el estado de la transacci�n:Error de conexi�n con la libraria en getEstadoTransaction");
				e.printStackTrace();
				ArrayList<String> parameters=new ArrayList<String>();
				parameters.add("#PSEERR="+ msj + (e.getMessage() == null ? e : e.getMessage()));//codeError);
				parameters.add("#PSECODERR="+(RESPONCE_TYPE_COD==null?"0":RESPONCE_TYPE_COD));
				parameters.add("#PSECB="+(codBanc == null || codBanc.isEmpty()?" ":codBanc));
				parameters.add("#PSENB="+(nomBanc == null || nomBanc.isEmpty()?" ": nomBanc));
				parameters.add("#VALREC="+(umbralId == null ? 0L: umbralId));
				System.out.println("PseWSEJB.parameters: "+parameters.toString());
				
				outbox.insertMessageOutbox(usrId, 
						EmailProcess.PSE_ERROR,
						idAccount,
						null,
						null, 
						null,
						null,
						null,
						null,
						null,
						usrId,
						null,
						null,
						null,
						true,
						parameters);			
			}
			throw e;
		}
		return cls;
	}
	

	@SuppressWarnings("unused")
	@Override
	public Long processTransactionPse(Long accountId,String pseUserType,String pseCodigoBanco,
			String nombreBanco,Long valor,String context,Long userCId,String ip){
		System.out.println("BOS.PseWSEJB.processTransactionPse()");
		Long respu=-4L;
		Long conId=-1L;
		String desc="",userCode="";
		String rq= "";
		String rs= "";
		String msgProcessAdmin = "";
		String msgProcessClient = "";
		boolean notification=false;
		String codeError="";
		String RESPONCE_TYPE_COD = null;
		TbUmbral u=new TbUmbral();
		TbPseTransaction t=new TbPseTransaction();
		Long codeAgreement;
		
		try{
			System.out.println("antes ");
			Query q = em.createNativeQuery("select code_agreement from tb_agreement ta, tb_process_agreement_channel tpac " +
					"where tpac.agreement_id = ta.agreement_id " +
					"and  tpac.agreement_id = (select agreement_id from tb_process_agreement_channel tpac where tpac.process_recharge_aval_id = 2)");
			codeAgreement = ((BigDecimal) q.getSingleResult()).longValue();
			
				
			conId = codeAgreement;
			userCode=user.getAccountById(accountId).getTbSystemUser().getUserCode();
			desc=parametros.getParameterValueById(62L);
			
			System.out.println("accountId: "+accountId);
			System.out.println("pseUserType: "+pseUserType);
			System.out.println("pseCodigoBanco: "+pseCodigoBanco);
			System.out.println("nombreBanco: "+nombreBanco);
			System.out.println("valor: "+valor);
			System.out.println("context: "+context);
			System.out.println("userCId: "+userCId);
			
			Long radId=null;
			Query query= em.createNativeQuery("select ACCOUNT_BANK_ID from VW_LAST_PRODUCT_BANK where ACCOUNT_ID=?1");
			query.setParameter(1, accountId);
			
			try{
				radId=((BigDecimal)query.getSingleResult()).longValue();
			}catch (NoResultException e) {
				System.out.println("No se encontro ReAccountBank para la cuenta "+accountId);
			}
			
			u.setTbAccount(em.find(TbAccount.class, accountId));
			u.setAverage((new BigDecimal(0)));
			u.setUmbralValue(new BigDecimal(valor));
			u.setUmbralTime(new Timestamp(System.currentTimeMillis()));	
			u.setUmbralState(0L);
			u.setTypeMessageId(1L);
			u.setUmbralChannel(1L);
			u.setAccountBankId(radId);
			u.setDescriptionPse(nombreBanco+" - PSE");
			
			em.persist(u);
			
			t.setConvenioId(conId);
			t.setReferenciaPago1(userCode);
			t.setReferenciaPago2(String.valueOf(accountId));
			t.setPseUserType(pseUserType);
			t.setPseCodigoBanco(pseCodigoBanco);
			t.setNombreBanco(nombreBanco);
			t.setValor(valor);
			t.setDescripcion(desc);
			t.setUrlRespuesta(null);
			t.setTbUmbral(u);
			t.setStateUrl(0L);
			
			em.persist(t);
			
			ClsDatosCrearTrandaccion p=new ClsDatosCrearTrandaccion();
			p.setConvenioId(conId.intValue());
			p.setReferenciaPago1(userCode);
			p.setReferenciaPago2(String.valueOf(accountId));
			p.setReferenciaPago3(null);
			p.setReferenciaPago4(null);
			p.setPseUserType(pseUserType);
			p.setPseCodigoBanco(pseCodigoBanco);
			p.setValor(new BigDecimal(valor));
			p.setDescripcion(desc);
			p.setCorreo(null);
			p.setTipoDocumentoId(null);
			p.setNumeroDocumento(null);
			p.setUrlRespuesta(context+"verificator.jspx?v="+
					this.encodePSETransaction(t.getPseTransactionId()+"+"+userCId));
			
			
			System.out.println("BOS.PseWSEJB.processTransactionPse() p.getConvenioId(): " + p.getConvenioId());
			System.out.println("BOS.PseWSEJB.processTransactionPse() p.getReferenciaPago1(): " + p.getReferenciaPago1());
			System.out.println("BOS.PseWSEJB.processTransactionPse() p.getReferenciaPago2(): " + p.getReferenciaPago2());
			System.out.println("BOS.PseWSEJB.processTransactionPse() p.getReferenciaPago3(): " + p.getReferenciaPago3());
			System.out.println("BOS.PseWSEJB.processTransactionPse() p.getReferenciaPago4(): " + p.getReferenciaPago4());
			System.out.println("BOS.PseWSEJB.processTransactionPse() p.getPseUserType(): " + p.getPseUserType());
			System.out.println("BOS.PseWSEJB.processTransactionPse() p.getPseCodigoBanco(): " + p.getPseCodigoBanco());
			System.out.println("BOS.PseWSEJB.processTransactionPse() p.getValor(): " + p.getValor());
			System.out.println("BOS.PseWSEJB.processTransactionPse() p.getDescripcion(): " + p.getDescripcion());
			System.out.println("BOS.PseWSEJB.processTransactionPse() p.getCorreo(): " + p.getCorreo());
			System.out.println("BOS.PseWSEJB.processTransactionPse() p.getTipoDocumentoId(): " + p.getTipoDocumentoId());
			System.out.println("BOS.PseWSEJB.processTransactionPse() p.getNumeroDocumento(): " + p.getNumeroDocumento());
			System.out.println("BOS.PseWSEJB.processTransactionPse() p.getUrlRespuesta(): " + p.getUrlRespuesta());		
			
			rq = this.generateXML(p,t.getReferenciaPago1(),0L);
			ClsRespuestaCrearTransaccion r=null;
			boolean enviarNotificacionDeLibreria=false;
			try {
				 r=this.createTransaction(p, u.getTbAccount().getTbSystemUser().getUserId(), u.getTbAccount().getAccountId(),nombreBanco );
				 if (r==null) {
					 u.setUmbralState(8L);
					 em.merge(u);
					 em.flush();
					 respu=-8L;
					 throw new Exception("No se pudo crear la transacci�n,debido a que la respuesta de la solicitud fue nula");
				}
			} catch (Exception e) {
			     u.setUmbralState(8L);
				 em.merge(u);
				 em.flush();
				 respu=-8L;
				 try{	
						FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
						.put("idPseT", this.encodePSETransaction(String.valueOf(t.getPseTransactionId())));
					}catch(IllegalStateException i){
						System.out.println("[ Error en PseWSEJB.FacesContext ]");
						i.printStackTrace();
					}
				throw e;
			}
			
			if(r!=null){
				
				System.out.println("BOS.PseWSEJB.processTransactionPse() r.getCodigoEstado(): " + r.getCodigoEstado());
				System.out.println("BOS.PseWSEJB.processTransactionPse() r.getUrlRedirigir(): " + r.getUrlRedirigir());
				System.out.println("BOS.PseWSEJB.processTransactionPse() r.getDescripcionEstado(): " + r.getDescripcionEstado());
				System.out.println("BOS.PseWSEJB.processTransactionPse() r.getTransaccionId(: " + r.getTransaccionId());
			
				System.out.println("BOS.PseWSEJB.processTransactionPse() t.getReferenciaPago1(): " + t.getReferenciaPago1());
				
				rs = this.generateXML(r,t.getReferenciaPago1(),1L);
				
				t.setUrlRespuesta(p.getUrlRespuesta());
				t.setCodigoEstadoCrear((long)r.getCodigoEstado());
				t.setDescripcionEstadoCrear(r.getDescripcionEstado());
				t.setTransactionId(r.getTransaccionId()==null?null:(long)r.getTransaccionId());
				t.setUrlRedirigir(r.getUrlRedirigir());
				
				em.merge(t);
				if(r.getTransaccionId()!=null){
					System.out.println("BOS.PseWSEJB.processTransactionPse() t.getCodigoEstadoCrear(): " + t.getCodigoEstadoCrear());
					if(t.getCodigoEstadoCrear()!=0L){
						notification=true;
						RESPONCE_TYPE_COD =t.getCodigoEstadoCrear().toString();		
						 codeError=this.process.getResponseDescByCode(Long.valueOf(RESPONCE_TYPE_COD), 0L);
						u.setUmbralState(2L);
						em.merge(u);
						if(t.getCodigoEstadoCrear()==1L){//Error: ingreso de datos erroneo
							respu=-1L;
						}else if(t.getCodigoEstadoCrear()==2L||t.getCodigoEstadoCrear()==3L){//Error: Error de conexi�n CPV y ACH
							respu=-2L;	
						}
						outbox.insertMessageOutbox(user.getSystemMasterById(userCId),
								EmailProcess.RESOURCE_ALLOCATION_REJECTED,
								u.getTbAccount().getAccountId(),
								(radId!=null?em.find(ReAccountBank.class, radId).getBankAccountId().getBankAccountId():null), 
								WSrecharge.getTransactionByUmbral(u.getUmbralId()), 
								null,
								null,
								null,
								(radId!=null?em.find(ReAccountBank.class, radId).getBankAccountId().getBankAccountId():null),
								null,
								userCId,
								null,
								null,
								null,
								true,
								null);
						try{	
							FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
							.put("idPseT", this.encodePSETransaction(String.valueOf(t.getPseTransactionId())));
						}catch(IllegalStateException i){
							System.out.println("[ Error en PseWSEJB.FacesContext ]");
							i.printStackTrace();
						}
						
					}else{
						notification=false;
						respu=t.getPseTransactionId();
					}

					if(t.getCodigoEstadoCrear() != 0L && t.getCodigoEstadoCrear() != 1L && t.getCodigoEstadoCrear() != 2L && t.getCodigoEstadoCrear() != 3L){
						
						RESPONCE_TYPE_COD =t.getCodigoEstadoCrear().toString();		
						codeError=this.process.getResponseDescByCode(Long.valueOf(RESPONCE_TYPE_COD), 0L);
						msgProcessAdmin="La recarga de recursos de la cuenta FacilPass N�. "+u.getTbAccount().getAccountId()+" present� el siguiente error: "+codeError+" - "+t.getCodigoEstadoFinalizar();
						msgProcessClient="La recarga de recursos de la cuenta FacilPass N�. "+u.getTbAccount().getAccountId()+" present� el siguiente error: "+codeError+".";
						Long userId = u.getTbAccount().getTbSystemUser().getUserId();
						Long codFinalizar = t.getCodigoEstadoFinalizar();
						accountId = u.getTbAccount().getAccountId();
						
						this.createProcessError(msgProcessAdmin, msgProcessClient, userId, ip, rq, rs, codFinalizar, accountId);
					}
				}else{
					System.out.println("Sin idTransaction, estado invalido");
					u.setUmbralState(8L);
					 em.merge(u);
					 em.flush();
					 respu=-8L;
					 RESPONCE_TYPE_COD =t.getCodigoEstadoCrear().toString();		
					 codeError=this.process.getResponseDescByCode(Long.valueOf(RESPONCE_TYPE_COD), 0L);
					 notification=true;						
				}
			}
			em.flush();
			
			if(notification==true){
				ArrayList<String> parameters=new ArrayList<String>();
				parameters.add("#PSEERR="+ (codeError == null || codeError.isEmpty() ?" ":codeError));
				parameters.add("#PSECODERR="+(RESPONCE_TYPE_COD==null?"0":RESPONCE_TYPE_COD));
				parameters.add("#PSECB="+(t.getPseCodigoBanco() == null || t.getPseCodigoBanco().isEmpty()?" ":t.getPseCodigoBanco()));
				parameters.add("#PSENB="+(t.getNombreBanco() == null || t.getNombreBanco().isEmpty()? " ":t.getNombreBanco()));
				parameters.add("#VALREC="+ (u.getUmbralValue() == null ? 0L: u.getUmbralValue().longValue()));
				System.out.println("PseWSEJB.parameters: "+parameters.toString());
				
				outbox.insertMessageOutbox(u.getTbAccount().getTbSystemUser().getUserId(), 
		                   EmailProcess.PSE_ERROR,
		                   u.getTbAccount().getAccountId(),
		                   null,
		                   null, 
		                   null,
		                   null,
		                   null,
		                   null,
		                   null,
		                   null,
		                   u.getTbAccount().getTbSystemUser().getTbCodeType().getCodeTypeId(),
			               null,
			               null,
		                   true,
		                   parameters);
			}
			
		}catch (Exception e) {
			System.out.println(" [ Error PseWSEJB.processTransactionPse ] ");
			e.printStackTrace();
			
           
			ArrayList<String> parameters=new ArrayList<String>();
			parameters.add("#PSEERR=" + "Se genero el siguiente error: " + (e.getMessage()==null?e:e.getMessage()));
			parameters.add("#PSECODERR="+(RESPONCE_TYPE_COD == null ? "0" : RESPONCE_TYPE_COD));
			parameters.add("#PSECB="+(t.getPseCodigoBanco() == null || t.getPseCodigoBanco().isEmpty() ? " " : t.getPseCodigoBanco()));
			parameters.add("#PSENB="+ (t.getNombreBanco() == null || t.getNombreBanco().isEmpty()?" ":t.getNombreBanco()));
			parameters.add("#VALREC="+ (u.getUmbralValue() == null ? 0L: u.getUmbralValue()));
			System.out.println("PseWSEJB.parameters: "+parameters.toString());
			
			outbox.insertMessageOutbox(u.getTbAccount().getTbSystemUser().getUserId(), 
	                   EmailProcess.PSE_ERROR,
	                   u.getTbAccount().getAccountId(),
	                   null,
	                   null, 
	                   null,
	                   null,
	                   null,
	                   null,
	                   null,
	                   null,
	                   u.getTbAccount().getTbSystemUser().getTbCodeType().getCodeTypeId(),
		               null,
		               null,
	                   true,
	                   parameters);
			log.insertLog("Se genero el siguiente error: " + (e.getMessage()==null?e:e.getMessage()), LogReference.RECHARGEPSE, LogAction.ERRORRECHARGEPSE, ip, u.getTbAccount().getTbSystemUser().getUserId());
			
			msgProcessAdmin="La recarga de recursos de la cuenta FacilPass N�. "+u.getTbAccount().getAccountId()+" no se cre�, debido a que no fue posible comunicarse con su entidad FacilPago " +
			t.getNombreBanco()+".";
			msgProcessClient="La recarga de recursos de la cuenta FacilPass N�. "+u.getTbAccount().getAccountId()+" no se cre�, debido a que no fue posible comunicarse con su entidad FacilPago " +
			t.getNombreBanco()+".";
			Long userId = u.getTbAccount().getTbSystemUser().getUserId();
			Long codFinalizar = t.getCodigoEstadoFinalizar();
			accountId = u.getTbAccount().getAccountId();
			
			this.createProcessError(msgProcessAdmin, msgProcessClient, userId, ip, rq, rs, codFinalizar, accountId);
		}
		System.out.println("respu: "+respu);
		return respu;
	}
	
	//process: 0 genera: 1 Sonda cliente
	
	@Override
	public Long endPseTransaction(Long pseId,String ip,Long cUserId,Long process){
		Long res=-4L;
		boolean notification=false;
		TbPseTransaction t=new TbPseTransaction();
		TbUmbral u= new TbUmbral();
		String rq= "";
		String rs= "";
		String msgProcessAdmin = "";
		String msgProcessClient = "";
		String codeError="";
		String RESPONCE_TYPE_COD = null;
		try{
			
			t=em.find(TbPseTransaction.class, pseId);
			u=t.getTbUmbral();
			ClsEstadoTransaccion r=null;
			
			rq= this.generateXML(t.getTransactionId(),t.getReferenciaPago1(),0L);
			
			if(process==0L){
				r=this.finishTransaction(t.getTransactionId(), u.getTbAccount().getTbSystemUser().getUserId(), u.getTbAccount().getAccountId(), t.getPseCodigoBanco(),t.getNombreBanco(), u.getUmbralValue().longValue());
			}else{
				try{
					r=this.getEstadoTransaction(t.getTransactionId(),u.getTbAccount().getTbSystemUser().getUserId(), u.getTbAccount().getAccountId(), t.getPseCodigoBanco(),t.getNombreBanco(), u.getUmbralValue().longValue());
					if (r==null) {
						u.setUmbralState(0L);
						em.merge(u);
						em.flush();
						throw new Exception("No se pudo procesar la transacci�n,debido a que la respuesta de la solicitud fue nula");
					}
				}catch (Exception e) {
					u.setUmbralState(0L);
					em.merge(u);
					em.flush();
					throw e;
				}				
			}
			
			if(r!=null){
				System.out.println("BOS.PseWSEJB.endPseTransaction().getEstadoTransactionFinalizar r.getCodigoEstado():  " + r.getCodigoEstado());
				System.out.println("BOS.PseWSEJB.endPseTransaction().getEstadoTransactionFinalizar r.getDescripcionEstado():  " + r.getDescripcionEstado());
				System.out.println("BOS.PseWSEJB.endPseTransaction().getEstadoTransactionFinalizar r.getAprobado():  " + r.getAprobado());
				System.out.println("BOS.PseWSEJB.endPseTransaction().getEstadoTransactionFinalizar  r.getFechaTransaccion():  " + (r.getFechaTransaccion() != null ? (new Timestamp(r.getFechaTransaccion().getTimeInMillis())) : ""));
				
				rs = this.generateXML(r,t.getReferenciaPago1(),1L);
				
				t.setCodigoEstadoFinalizar((long)r.getCodigoEstado());
				t.setDescripcionEstadoFinalizar(r.getDescripcionEstado());
				t.setAprobado(r.getAprobado()!=null?(r.getAprobado()==true?1L:0L):null);
				t.setFechaTransaccion(r.getFechaTransaccion()!=null?(new Timestamp(r.getFechaTransaccion().getTimeInMillis())):null);
				t.setFechaFinTransaccion(r.getFechaFinTransaccion()!=null?(new Timestamp(r.getFechaFinTransaccion().getTimeInMillis())):null);
				em.merge(t);
				
				ReAccountBank rab = null;
				Query qReAccountBank = em.createQuery("Select rab From ReAccountBank rab " +
						"where rab.accountId.accountId=?1 " +
						"and rab.state_account_bank = 1 ");
				qReAccountBank.setParameter(1, u.getTbAccount().getAccountId());
				try{
					rab = (ReAccountBank) qReAccountBank.getSingleResult();
				} catch(NoClassDefFoundError e){
					System.out.println("No se encontro ReAccountBank para la cuenta "+u.getTbAccount().getAccountId());
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Error ReAccountBank para la cuenta "+u.getTbAccount().getAccountId());
				}
				
				if(t.getCodigoEstadoFinalizar()==0L){
					if(t.getAprobado()!=null){
						if(t.getAprobado()==1L){
							u.setUmbralState(1L);
							em.merge(u);
							boolean appre = this.applyRecharge(u.getUmbralId(),
									0L, ip, cUserId);
							System.out.println("endPseTransaction.applyRecharge1: "+appre);
							
							if(appre){
								res=t.getPseTransactionId();
								
								outbox.insertMessageOutbox(user.getSystemMasterById(cUserId), 
										EmailProcess.RECHARGE_ALLOCATION_SUCCESSFUL,
										u.getTbAccount().getAccountId(),
										(rab==null?null:rab.getBankAccountId().getBankAccountId()), 
										WSrecharge.getTransactionByUmbral(u.getUmbralId()), 
										null,
										null,
										null,
										(rab==null?null:rab.getBankAccountId().getProduct().getBankId()),
										null,
										cUserId,
										null,
										null,
										null,
										true,
										null);	
								}else{
								res = -50L;
								}
							
						}else{
							u.setUmbralState(2L);
							em.merge(u);
							res=-6L;
							
							outbox.insertMessageOutbox(user.getSystemMasterById(cUserId),
									EmailProcess.RECHARGE_ALLOCATION_REJECTED,
									u.getTbAccount().getAccountId(),
									(rab==null?null:rab.getBankAccountId().getBankAccountId()), 
									WSrecharge.getTransactionByUmbral(u.getUmbralId()), 
									null,
									null,
									null,
									(rab==null?null:rab.getBankAccountId().getProduct().getBankId()),
									null,
									cUserId,
									null,
									null,
									null,
									true,
									null);
					    }
					}else{
						u.setUmbralState(0L);
						em.merge(u);
						res=-5L;
					}
				}else{
					notification=true;
					u.setUmbralState(2L);
					
					em.merge(u);
					if(t.getCodigoEstadoFinalizar()==1L){
						res=-1L;
					}else if(t.getCodigoEstadoFinalizar()==2L){
						res=-2L;
					}else if(t.getCodigoEstadoFinalizar()==3L){
						res=-2L;
					}
					
					outbox.insertMessageOutbox(user.getSystemMasterById(cUserId),
							EmailProcess.RECHARGE_ALLOCATION_REJECTED,
							u.getTbAccount().getAccountId(),
							(rab==null?null:rab.getBankAccountId().getBankAccountId()), 
							WSrecharge.getTransactionByUmbral(u.getUmbralId()), 
							null,
							null,
							null,
							(rab==null?null:rab.getBankAccountId().getProduct().getBankId()),
							null,
							cUserId,
							null,
							null,
							null,
							true,
							null);
				}
				
				if(t.getCodigoEstadoCrear() != 0L && t.getCodigoEstadoCrear() != 1L && t.getCodigoEstadoCrear() != 2L && t.getCodigoEstadoCrear() != 3L){
					
					RESPONCE_TYPE_COD =t.getCodigoEstadoFinalizar().toString();		
					codeError=this.process.getResponseDescByCode(Long.valueOf(RESPONCE_TYPE_COD), 0L);
					msgProcessAdmin="La recarga de recursos de la cuenta FacilPass N�. "+u.getTbAccount().getAccountId()+" present� el siguiente error: "+codeError+" - "+t.getCodigoEstadoFinalizar();
					msgProcessClient="La recarga de recursos de la cuenta FacilPass N�. "+u.getTbAccount().getAccountId()+" present� el siguiente error: "+codeError+".";
					Long userId = u.getTbAccount().getTbSystemUser().getUserId();
					Long codFinalizar = t.getCodigoEstadoFinalizar();
					Long accountId = u.getTbAccount().getAccountId();
					
					this.createProcessError(msgProcessAdmin, msgProcessClient, userId, ip, rq, rs, codFinalizar, accountId);
					
				}
			}
			System.out.println("PseWSEJB.notification: "+notification);
			
			if(notification==true){
				ArrayList<String> parameters=new ArrayList<String>();
				parameters.add("#PSEERR="+(codeError == null || codeError.isEmpty()?" ":codeError));
				parameters.add("#PSECODERR="+(RESPONCE_TYPE_COD==null?"0":RESPONCE_TYPE_COD));
				parameters.add("#PSECB="+(t.getPseCodigoBanco() == null || t.getPseCodigoBanco().isEmpty()?" ":t.getPseCodigoBanco()));
				parameters.add("#PSENB="+(t.getNombreBanco() == null || t.getNombreBanco().isEmpty()?" ":t.getNombreBanco()));
				parameters.add("#VALREC="+(u.getUmbralValue() == null ? 0L : u.getUmbralValue().longValue()));
				System.out.println("PseWSEJB.parameters: "+parameters.toString());
				
				outbox.insertMessageOutbox(u.getTbAccount().getTbSystemUser().getUserId(), 
		                   EmailProcess.PSE_ERROR,
		                   u.getTbAccount().getAccountId(),
		                   null,
		                   null, 
		                   null,
		                   null,
		                   null,
		                   null,
		                   null,
		                   null,
		                   u.getTbAccount().getTbSystemUser().getTbCodeType().getCodeTypeId(),
			               null,
			               null,
		                   true,
		                   parameters);
			}
			em.flush();
			
		}catch (Exception e) {
			System.out.println(" [ Error PseWSEJB.endPseTransaction ] ");
			e.printStackTrace();
			u=em.find(TbUmbral.class, this.getUmbralByPseId(pseId));
			if(u.getUmbralState()==3L || u.getUmbralState()==5L){
				u.setUmbralState(0L);
				em.merge(u);
				em.flush();
			}
			ArrayList<String> parameters=new ArrayList<String>();
			parameters.add("#PSEERR=" + "Se genero el siguiente error: " + (e.getMessage()==null?e:e.getMessage()));
			parameters.add("#PSECODERR="+(RESPONCE_TYPE_COD==null?"0":RESPONCE_TYPE_COD));
			parameters.add("#PSECB="+(t.getPseCodigoBanco() == null || t.getPseCodigoBanco().isEmpty()?" ":t.getPseCodigoBanco()));
			parameters.add("#PSENB="+(t.getNombreBanco() == null || t.getNombreBanco().isEmpty()?" ":t.getNombreBanco()));
			parameters.add("#VALREC="+u.getUmbralValue().longValue());
			System.out.println("PseWSEJB.parameters: "+parameters.toString());
			
			outbox.insertMessageOutbox(u.getTbAccount().getTbSystemUser().getUserId(), 
	                   EmailProcess.PSE_ERROR,
	                   u.getTbAccount().getAccountId(),
	                   null,
	                   null, 
	                   null,
	                   null,
	                   null,
	                   null,
	                   null,
	                   null,
	                   u.getTbAccount().getTbSystemUser().getTbCodeType().getCodeTypeId(),
		               null,
		               null,
	                   true,
	                   parameters);
			
			
			msgProcessAdmin="La recarga de recursos de la cuenta FacilPass N�. "+u.getTbAccount().getAccountId()+" no se cre�, debido a que no fue posible comunicarse con su entidad FacilPago " +
			t.getNombreBanco()+".";
			msgProcessClient="La recarga de recursos de la cuenta FacilPass N�. "+u.getTbAccount().getAccountId()+" no se cre�, debido a que no fue posible comunicarse con su entidad FacilPago " +
			t.getNombreBanco()+".";
			Long userId = u.getTbAccount().getTbSystemUser().getUserId();
			Long codFinalizar = t.getCodigoEstadoFinalizar();
			Long accountId = u.getTbAccount().getAccountId();
			
			this.createProcessError(msgProcessAdmin, msgProcessClient, userId, ip, rq, rs, codFinalizar, accountId);
			
			log.insertLog("Se genero el siguiente error: " + (e.getMessage()==null?e:e.getMessage()), LogReference.RECHARGEPSE, LogAction.ERRORRECHARGEPSE, ip, u.getTbAccount().getTbSystemUser().getUserId());
		}
		return res;
	}
	
	
	
	/*
	 * BEGIN FUNCTION ENDAVALTRANSACTION
	 * Funci�n Encargada de Recuperar resultado de transaciones para Prepago Aval
	 * (non-Javadoc)
	 * @see util.ws.PseWS#endAvalTransaction(java.lang.Long, java.lang.String, java.lang.Long, java.lang.Long)
	 */
	@Override
	public Long endAvalTransaction(Long avalId,String ip,Long cUserId,Long process){
		/*
		 * Posibles Respuestas
		 *res=-1L error No ingreso a condicional  res= -5 aprobado = null;	res= -1 -2  -4 -5 -7 Error transacci�n; res=-100 -200 -300 -600 -700 error Servicio; res=-50 error Appre
		 *res=6 OK 
		 */
		
		Long res=-1L;
		boolean notification=false;
		TbAvalTransaction t=new TbAvalTransaction();
		TbUmbral u= new TbUmbral();
		String rq= "";
		String rs= "";
		String msgProcessAdmin = "";
		String msgProcessClient = "";
		String codeError="";
		String RESPONCE_TYPE_COD = null;
		
		
		Long userId;
		Long accountId;
		Long bankId;
		Long bankAccountId;
		String bankName;
		String valueRecharge;
		String radId;
		Long rqUID;
		String channel;
		Date clientDt;
		String ipAddr;
		String idType;
		String idNum; 
		String agreementId;
		String pmtId ;
		
		
		
		try{
			
			t=em.find(TbAvalTransaction.class, avalId);
			u=t.getUmbralId();
			
			
			
			GetTransactionResponseDTO response = null;
			//WSservicesEJB wsgateway=new WSservicesEJB();	
			
			userId=t.getUserId().getUserId();
			accountId=u.getTbAccount().getAccountId();
			bankName=u.getDescriptionPse();
			bankId=0L;
			bankAccountId=0L;
			valueRecharge=u.getUmbralValue().toString();
			radId=u.getAccountBankId().toString();
			rqUID=t.getRequestId();
			channel="1";
			clientDt=new Timestamp(System.currentTimeMillis());
			ipAddr=ip;
			idType =t.getUserId().getTbCodeType().getCodeTypeAbbreviation();
			idNum=t.getUserId().getUserCode().toString();
			agreementId=t.getAgreementId().getAgreementId().toString();
			pmtId=t.getTransactionIdentifier();
			
			
			rq= this.generateXML(t.getAvalTransactionId(),t.getReference1(),0L);
			
						try{
								//response=wsgateway.getTransaction(rqUID, channel, clientDt, ipAddr, idType, idNum, agreementId, pmtId,userId,accountId,bankName,valueRecharge,radId);
								response=services.getTransaction(rqUID, channel, clientDt, ipAddr, idType, idNum, agreementId, pmtId,userId,accountId,bankName,valueRecharge,radId);
								
								if (response==null){
									u.setUmbralState(0L);
									em.merge(u);
									em.flush();
									throw new Exception("No se pudo procesar la transacci�n,debido a que la respuesta de la solicitud fue nula");
								}
						    }catch (Exception e){
						    	u.setUmbralState(0L);
								em.merge(u);
								em.flush();
								throw e;
							}				
						
			
					if(response!=null){
						
						System.out.println("BOS.PseWSEJB.endAvalTransaction => StatusCode:  " + response.getStatusCode());
						System.out.println("BOS.PseWSEJB.endAvalTransaction(). Descripcion " + response.getStatusDesc());
						System.out.println("BOS.PseWSEJB.endAvalTransaction(). EstadoTransaction Code " + response.getTransactionStatus().getTrnStatusCode());
						System.out.println("BOS.PseWSEJB.endAvalTransaction(). EstadoTransaction  Desc" + response.getTransactionStatus().getTrnStatusDesc());
						
						
						rs = this.generateXML(response,t.getReference1(),1L);
						
						t.setStateTransactionId(response.getTransactionStatus().getTrnStatusCode());
						t.setAprobado(response.getTransactionStatus().getTrnStatusCode() != null?(response.getTransactionStatus().getTrnStatusCode()==6L?1L:0L):null);
						em.merge(t);
							/*
							ReAccountBank rab = null;
							Query qReAccountBank = em.createQuery("Select rab From ReAccountBank rab " +
									"where rab.accountId.accountId=?1 " +
									"and rab.state_account_bank = 1 ");
							qReAccountBank.setParameter(1, u.getTbAccount().getAccountId());
							try{
								rab = (ReAccountBank) qReAccountBank.getSingleResult();
							} catch(NoClassDefFoundError e){
								System.out.println("No se encontro ReAccountBank para la cuenta "+u.getTbAccount().getAccountId());
							} catch (Exception e) {
								e.printStackTrace();
								System.out.println("Error ReAccountBank para la cuenta "+u.getTbAccount().getAccountId());
							}
							
							*/
							Query queryx= em.createNativeQuery("select BANK_ID from VW_LAST_PRODUCT_BANK where ACCOUNT_BANK_ID=?1");
							queryx.setParameter(1,u.getAccountBankId());
							
							try{
								bankId=Long.valueOf(queryx.getSingleResult().toString());
							}catch (NoResultException e) {
								System.out.println("No se encontro BankId para la cuenta "+u.getAccountBankId());
							}
							
							Query query= em.createNativeQuery("select BANK_ACCOUNT_ID from VW_LAST_PRODUCT_BANK where ACCOUNT_BANK_ID=?1");
							query.setParameter(1,u.getAccountBankId());
							
							try{
								bankAccountId=Long.valueOf(query.getSingleResult().toString());
							}catch (NoResultException e) {
								System.out.println("No se encontro BankAccountId para la cuenta "+u.getAccountBankId());
							}
						
						/*
						 * Si la transaccion es exitosa el sistema procede a crear registros en la siguientes tablas:
						 * tb_consignement
						 * Tb_transaction
						 * Actualiza los registros de las tablas 
						 * Tb_umbral => Actualiza el registro de Transaction_id Y umbral_state =>1 recarga aprobada
						 * TB_account => modifica los saldos de la cuenta
						 * 
						 */
						
							if(response.getStatusCode()==0L){
								if(t.getAprobado() !=null){
									if(t.getAprobado()==1L){
												u.setUmbralState(1L);
												em.merge(u);
												boolean appre = this.applyRecharge(u.getUmbralId(),0L, ip, cUserId);
												System.out.println("endAvalTransaction.applyRecharge1: "+appre);
												if(appre){
													res=t.getStateTransactionId();
													
													
													
													//METODO NUEVO PARA ENVIO DE NOTIFICACIONES NTF-12 APROBADA
													Map<String,Object> tags = new HashMap<String,Object>();
													Date date = Calendar.getInstance().getTime();
													tags.put(EEmailParam.CUENTA_FACIL_PASS.getTag(), u.getTbAccount().getAccountId()); //OK-1
													tags.put(EEmailParam.NOMB_CLI.getTag(), cUserId);//OK-4
													tags.put(EEmailParam.NOMB_BANC.getTag(),bankId);//OK-2
													tags.put(EEmailParam.NUM_ID.getTag(), cUserId);//OK-5
													tags.put(EEmailParam.BP.getTag(),bankAccountId);//OK-3
													//tags.put(EEmailParam.VAL_TRANS.getTag(),u.getUmbralValue());//BAD-12
													tags.put(EEmailParam.VAL_RECARG.getTag(),u.getUmbralValue());
													tags.put(EEmailParam.FECHA_ACCION.getTag(), date);//OK-18
													tags.put(EEmailParam.HORA_ACCION.getTag(), date);//OK-19
													tags.put(EEmailParam.BALANCE.getTag(), u.getTbAccount().getAccountId());//OK-41
																	
													MessageParser mp = new SimpleTagMessageParser(tags);
													
													outbox.insertMessageOutbox2(cUserId, EmailProcess.RECHARGE_ALLOCATION_SUCCESSFUL, true, mp);
													
													
												}else {
													res = -50L;
												}
									}else{
										u.setUmbralState(2L);
										
											notification=false;
												if(t.getStateTransactionId()==1L){
													System.out.println("EndAvalTransaction -> Respuesta del servicio => 1 Registrada  se deja la recarga Rechazada");
													res=-1L;
													notification=true;
													
												}else if(t.getStateTransactionId()==2L){
													System.out.println("EndAvalTransaction -> Respuesta del servicio => 2 Iniciada  se deja la recarga Rechazada");
													res=-2L;
													notification=true;
													
												}else if(t.getStateTransactionId()==3L){
													System.out.println("EndAvalTransaction -> Respuesta del servicio => 3 Pendiente  se deja la recarga Pendiente");
													u.setUmbralState(0L);
													res=-3L;
													
												}else if(t.getStateTransactionId()==4L){
													System.out.println("EndAvalTransaction -> Respuesta del servicio => 1 Cancelada  se deja la recarga Rechazada");
													res=-4L;
													notification=true;
													
												}else if(t.getStateTransactionId()==5L){
													System.out.println("EndAvalTransaction -> Respuesta del servicio => 1 Expirada  se deja la recarga Rechazada");
													res=-7L;
													notification=true;
													
												}else if(t.getStateTransactionId()==7L){
													System.out.println("EndAvalTransaction -> Respuesta del servicio => 7 Declinada  se deja la recarga Rechazada");
													res=-7L;
													notification=true;
													
												}
											em.merge(u);
										
											
											
									 }
									}else{
										u.setUmbralState(0L);
										em.merge(u);
										res=-5L;
										
										
									}
							
								
							}else{
								
								//NTF-12 RECHAZADA
								notification=true;
								u.setUmbralState(2L);
								em.merge(u);
								if(response.getStatusCode()==100L){
									res=-100L;
								}else if(response.getStatusCode()==200L){
									res=-200L;
								}else if(response.getStatusCode()==300L){
									res=-300L;
								}else if(response.getStatusCode()==600L){
									res=-600L;
								}else if(t.getStateTransactionId()==700L){
									res=-700L;
								}
							
							}
							
							
						//ERROR DESCONOCIDO
						if(response.getStatusCode() != 0L && response.getStatusCode() != 100L && response.getStatusCode() != 200L 
								&& response.getStatusCode() != 300L	&& response.getStatusCode() != 600L && response.getStatusCode() != 700L){
							
							
							RESPONCE_TYPE_COD =Long.toString(response.getStatusCode());		
							codeError=this.process.getResponseDescByCode(Long.valueOf(RESPONCE_TYPE_COD), 2L);
							msgProcessAdmin="La recarga de recursos de la cuenta FacilPass N�. "+u.getTbAccount().getAccountId()+" present� el siguiente error: "+codeError+" - "+t.getAvalTransactionId();
							msgProcessClient="La recarga de recursos de la cuenta FacilPass N�. "+u.getTbAccount().getAccountId()+" present� el siguiente error: "+codeError+".";
							userId = u.getTbAccount().getTbSystemUser().getUserId();
							Long codFinalizar = t.getAvalTransactionId();
							accountId = u.getTbAccount().getAccountId();
							this.createProcessError(msgProcessAdmin, msgProcessClient, userId, ip, rq, rs, codFinalizar, accountId);
							
						}
					}
			System.out.println("PseWSEJB.notification: "+notification);
			
			if(notification==true){
						
				//METODO NUEVO PARA ENVIO DE NOTIFICACIONES NTF-12 RECHAZADA
				Map<String,Object> tags = new HashMap<String,Object>();
				Date date = Calendar.getInstance().getTime();
				
				
				tags.put(EEmailParam.CUENTA_FACIL_PASS.getTag(), u.getTbAccount().getAccountId()); //OK-1
				tags.put(EEmailParam.NOMB_CLI.getTag(), cUserId);//OK-4
				tags.put(EEmailParam.NOMB_BANC.getTag(),bankId);//OK-2
				tags.put(EEmailParam.NUM_ID.getTag(), cUserId);//OK-5
 				tags.put(EEmailParam.BP.getTag(),bankAccountId);//OK-3
				//tags.put(EEmailParam.VAL_TRANS.getTag(),u.getUmbralValue());//BAD-12
				tags.put(EEmailParam.VAL_RECARG.getTag(),u.getUmbralValue());
				tags.put(EEmailParam.FECHA_ACCION.getTag(), date);//OK-18
				tags.put(EEmailParam.HORA_ACCION.getTag(), date);//OK-19
				tags.put(EEmailParam.BALANCE.getTag(), u.getTbAccount().getAccountId());//OK-41
								
				MessageParser mp = new SimpleTagMessageParser(tags);
				
				outbox.insertMessageOutbox2(cUserId, EmailProcess.RECHARGE_ALLOCATION_REJECTED, true, mp);
			}
			em.flush();
			
		}catch (Exception e) {
			res=-500L;
			System.out.println(" [ Error PseWSEJB.endAvalTransaction ] ");
			e.printStackTrace();
			u=em.find(TbUmbral.class, this.getUmbralByPseId(avalId));
			if(u.getUmbralState()==3L || u.getUmbralState()==5L){
				u.setUmbralState(0L);
				em.merge(u);
				em.flush();
			}
		
			
			Map<String,Object> tags = new HashMap<String,Object>();
			
			//METODO NUEVO ENVIO DE NOTIFICACION ERROR DE CONEXION O TIMEOUT NTF-10
			RESPONCE_TYPE_COD ="Error de conexi�n � Timeout";	
			Date date = Calendar.getInstance().getTime();
			tags.put(EEmailParam.TIPO_ID.getTag(), u.getTbAccount().getTbSystemUser().getTbCodeType().getCodeTypeId());
			tags.put(EEmailParam.NUM_ID.getTag(), cUserId);
			tags.put(EEmailParam.NOMB_CLI.getTag(), cUserId);
			tags.put(EEmailParam.CUENTA_FACIL_PASS.getTag(), u.getTbAccount().getAccountId());
			tags.put(EEmailParam.NOMBR_ENTIDAD.getTag(), u.getDescriptionPse());
			tags.put(EEmailParam.VAL_RECARG.getTag(),u.getUmbralValue());
			tags.put(EEmailParam.FECHA_ACCION.getTag(), date);
			tags.put(EEmailParam.HORA_ACCION.getTag(), date);
			tags.put(EEmailParam.ERRAVALC.getTag(), RESPONCE_TYPE_COD);
			MessageParser mp = new SimpleTagMessageParser(tags);
			outbox.insertMessageOutbox2(cUserId, EmailProcess.AVAL_URL_TIMEOUT, true, mp);
			
			
			msgProcessAdmin="La recarga de recursos de la cuenta FacilPass N�. "+u.getTbAccount().getAccountId()+" no se cre�, debido a que no fue posible comunicarse con su entidad FacilPago " +
					u.getDescriptionPse();
			
			msgProcessClient="La recarga de recursos de la cuenta FacilPass N�. "+u.getTbAccount().getAccountId()+" no se cre�, debido a que no fue posible comunicarse con su entidad FacilPago " +
					u.getDescriptionPse();
			 userId = u.getTbAccount().getTbSystemUser().getUserId();
			Long codFinalizar = t.getAvalTransactionId();
			 accountId = u.getTbAccount().getAccountId();
			this.createProcessError(msgProcessAdmin, msgProcessClient, userId, ip, rq, rs, codFinalizar, accountId);
			log.insertLog("Se genero el siguiente error: " + (e.getMessage()==null?e:e.getMessage()), LogReference.RECHARGEPSE, LogAction.ERRORRECHARGEPSE, ip, u.getTbAccount().getTbSystemUser().getUserId());
			
			
		}
		return res;
	}
	//END METHOD ENDAVALTRANSACTION
	
	
	public void createProcessError(String msgProcessAdmin, String msgProcessClient, Long userId, String ip,String rq,String rs,Long codFinalizar, Long accountId){
		
		System.out.println("inicio de creaci�n en segimiento de proceso - createProcessError Bos");
		try{
			
			//proceso administrador
			TbProcessTrack idProcClient = this.process.searchProcess(ProcessTrackType.CLIENT, userId);
			Long newProcClient = null;
			if(idProcClient == null){
				newProcClient = this.process.createProcessTrack(ProcessTrackType.CLIENT,userId, ip, userId);
			}else{
				newProcClient = idProcClient.getProcessTrackId();
			}
			Long detailA = this.process.createProcessDetail(newProcClient, ProcessTrackDetailType.MANUAL_RECHARGE,
					msgProcessAdmin, userId, ip,
					1, "Error al Crear Proceso para Recarga Manual de la cuenta "+
					accountId,1,rq,rs,null,null,1L);
			System.out.println("detailA ->endPseTransaction: " + detailA);
			
			//proceso cliente
			TbProcessTrack idProc = this.process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, userId);
			Long newProc = null;
			if(idProc == null){
				newProc = this.process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, userId, ip, userId);
			}else{
				newProc = idProc.getProcessTrackId();
			}
			Long detailC = this.process.createProcessDetail(newProc, ProcessTrackDetailType.MANUAL_RECHARGE,
					msgProcessClient, userId, ip, 1,
					"Error al Crear Proceso para Recarga Manual de la cuenta "+
					accountId,1,null,null,null,null,1L);
			System.out.println("detailC ->endPseTransaction: " + detailC);
		}catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	@Override
	public String encodePSETransaction(String idPse){
		String res="";
		try{
			res=new String(new BASE64Encoder().encode(new String(new BASE64Encoder().encode(idPse.getBytes(CHARSET_UTF_8)))
			.getBytes(CHARSET_UTF_8)));
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error PseWSEJB.encodePSETransaction ] ");
		}
		System.out.println("encodePSETransaction.res: "+res);
		return res;
	}
	
	@Override
	public String decodePSETransaction(String idPse){
		String res=null;
		try{
			res=new String(new BASE64Decoder().decodeBuffer(new String(new BASE64Decoder().decodeBuffer(idPse), CHARSET_UTF_8))
					, CHARSET_UTF_8);
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error PseWSEJB.decodePSETransaction ] ");
		}
		System.out.println("decodePSETransaction.res: "+res);
		return res;
	}
	
	@Override
	public String getUrlRedirigirById(Long pseId){
		String res="";
		try{
			TbPseTransaction t=em.find(TbPseTransaction.class, pseId);
			res=t.getUrlRedirigir();
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error PseWSEJB.getUrlRedirigirById ] ");
		}
		System.out.println("getUrlRedirigirById.res: "+res);
		return res;
	}
	
	@Override
	public String getUrlRedirigirByIdAval(Long avalId){
		String res="";
		try{
			TbAvalTransaction t=em.find(TbAvalTransaction.class, avalId);
			res=t.getUrlRequest();
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error PseWSEJB.getUrlRedirigirByIdAval ] ");
		}
		System.out.println("getUrlRedirigirByIdAval.res: "+res);
		return res;
	}
	
	@Override
	public boolean isUsedURL(Long idPse){
		boolean res=true;
		try{
			TbPseTransaction t=em.find(TbPseTransaction.class, idPse);
			if(t.getStateUrl()==1L){
				res=true;
			}else{
				t.setStateUrl(1L);
				em.merge(t);
				em.flush();
				res=false;
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error PseWSEJB.isUsedURL ] ");
		}
		return res;
	}
	
	
	@Override
	public boolean isUsedURLAval(Long idAval){
		boolean res=true;
		try{
			TbAvalTransaction t=em.find(TbAvalTransaction.class, idAval);
			if(t.getStateUrl()==1L){
				res=true;
			}else{
				t.setStateUrl(1L);
				em.merge(t);
				em.flush();
				res=false;
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error PseWSEJB.isUsedURLAVAL ] ");
		}
		return res;
	}
	
	@Override
	public boolean isOnTimeURLbyPSE(Long idPse){
		boolean res=false;
		Calendar c=null;
		try{
			int mins=Integer.parseInt(parametros.getParameterValueById(60L));
			c=Calendar.getInstance();
			TbPseTransaction t=em.find(TbPseTransaction.class, idPse);
			c.setTimeInMillis(t.getFechaTransaccion().getTime());
			c.add(Calendar.MINUTE, mins);
			if(c.getTime().after(new Date(System.currentTimeMillis()))){
				res=false;
			}else{
				res=true;
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error PseWSEJB.isOnTimeURLbyPSE ] ");
		}
		return res;
	}
	
	@Override
	public boolean isOnTimeURLbyUmbral(Long idPse){
		boolean res=false;
		Calendar c=null;
		try{
			int mins=Integer.parseInt(parametros.getParameterValueById(60L));
			c=Calendar.getInstance();
			TbPseTransaction t=em.find(TbPseTransaction.class, idPse);
			c.setTimeInMillis(t.getTbUmbral().getUmbralTime().getTime());
			c.add(Calendar.MINUTE, mins);
			System.out.println("c.getTime(): "+c.getTime());
			if(c.getTime().after(new Date(System.currentTimeMillis()))){
				res=true;
			}else{
				res=false;
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error PseWSEJB.isOnTimeURLbyUmbral ] ");
		}
		return res;
	}
	
	
	@Override
	public boolean isOnTimeURLbyUmbralAval(Long idAval){
		boolean res=false;
		Calendar c=null;
		try{
			int mins=Integer.parseInt(parametros.getParameterValueById(77L));
			c=Calendar.getInstance();
			TbAvalTransaction t=em.find(TbAvalTransaction.class, idAval);
			c.setTimeInMillis(t.getUmbralId().getUmbralTime().getTime());
			c.add(Calendar.MINUTE, mins);
			System.out.println("c.getTime(): "+c.getTime());
			if(c.getTime().after(new Date(System.currentTimeMillis()))){
				res=true;
			}else{
				res=false;
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error PseWSEJB.isOnTimeURLbyUmbralAval ] ");
		}
		return res;
	}
	
	
	
	@Override
	public boolean existURL(Long idPse){
		boolean res=false;
		try{
			Query q=em.createNativeQuery("SELECT count(pse_transaction_id) " +
					"FROM tb_pse_transaction " +
					"WHERE pse_transaction_id=?1");
			q.setParameter(1, idPse);
			Long cant=((BigDecimal)q.getSingleResult()).longValue();
			if(cant<=0){
				res=existURL(idPse);
			}else{
				res=true;
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error PseWSEJB.existURL ] ");
		}
		return res;
	}
	
	
	@Override
	public boolean existURLAval(Long idAval){
		boolean res=false;
		try{
			Query q=em.createNativeQuery("SELECT count(aval_transaction_id) " +
					"FROM tb_aval_transaction " +
					"WHERE aval_transaction_id=?1");
			q.setParameter(1, idAval);
			Long cant=((BigDecimal)q.getSingleResult()).longValue();
			if(cant<=0){
				res=false;
			}else{
				res=true;
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error PseWSEJB.existURLAval ] ");
		}
		return res;
	}
	
	@Override
	public Long getAccountByPse(Long idPse){
		Long res=-1L;
		try{
			TbPseTransaction t=em.find(TbPseTransaction.class, idPse);
			res=t.getTbUmbral().getTbAccount().getAccountId();
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error PseWSEJB.getAccountByPse ] ");
		}
		return res;
	}
	
	@Override
	public Long getAccountByAval(Long idAval){
		Long res=-1L;
		try{
			TbAvalTransaction t=em.find(TbAvalTransaction.class, idAval);
			res=t.getUmbralId().getTbAccount().getAccountId();
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error PseWSEJB.getAccountByAval ] ");
		}
		return res;
	}
	
	/*
	 * Funcion encargada de retornar una transaction aval para la notificacion NTF-09
	 * de VerificatorBean
	 * BM
	 */
	@Override
	public TbAvalTransaction getTransactionAval(Long idAval){
		TbAvalTransaction res=null;
		try{
			res=em.find(TbAvalTransaction.class, idAval);
			
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error PseWSEJB.getTransactionAval ] ");
		}
		return res;
		
	}
	
	@Override
	public Long getAprobadoById(Long idPse){
		Long res=-1L;
		try{
			TbPseTransaction t=em.find(TbPseTransaction.class, idPse);
			res=t.getAprobado();
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error PseWSEJB.getAccountByPse ] ");
		}
		return res;
	}
	
	@Override
	public Long getAprobadoByIdAval(Long idAval){
		Long res=-1L;
		try{
			TbAvalTransaction t=em.find(TbAvalTransaction.class, idAval);
			res=t.getAprobado();
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error PseWSEJB.getAccountByPse ] ");
		}
		return res;
	}
	
	@Override
	public Long getValorById(Long idPse){
		Long res=0L;
		try{
			TbPseTransaction t=em.find(TbPseTransaction.class, idPse);
			res=t.getValor();
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error PseWSEJB.getValorById ] ");
		}
		return res;
	}
	
	@Override
	public Long getValorByIdAval(Long idAval){
		Long res=0L;
		try{
			TbAvalTransaction t=em.find(TbAvalTransaction.class, idAval);
			res=t.getValueTransaction();
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error PseWSEJB.getValorByIdAval ] ");
		}
		return res;
	}
	
	@Override
	public String getBancoById(Long idPse){
		String res="N/A";
		try{
			TbPseTransaction t=em.find(TbPseTransaction.class, idPse);
			res=t.getNombreBanco();
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error PseWSEJB.getBancoById ] ");
		}
		return res;
	}
	
	@Override
	public String getBancoByIdAval(Long idAval){
		String res="N/A";
		TbUmbral tu;
		
		try{
			TbAvalTransaction t=em.find(TbAvalTransaction.class, idAval);
			tu=t.getUmbralId();
			tu.getTbAccount().getAccountId();
		
			Query queryx= em.createNativeQuery("select BANK_NAME from VW_LAST_PRODUCT_BANK where ACCOUNT_ID=?1");
			queryx.setParameter(1, tu.getTbAccount().getAccountId());
			res=queryx.getSingleResult().toString();
								
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error PseWSEJB.getBancoByIdAval ] ");
		}
		return res;
	}
	
	@Override
	public Long getCodigoFinById(Long idPse){
		Long res=-1L;
		try{
			TbPseTransaction t=em.find(TbPseTransaction.class, idPse);
			res=t.getCodigoEstadoFinalizar();
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error PseWSEJB.getCodigoFinById ] ");
		}
		return res;
	}
	
	@Override
	public String getBankByCode(List<BankACHData> l,String code){
		String name="PSE"; 
		try{
			for(int i=0;i<l.size();i++){
				if(l.get(i).getFinancialInstitutionCode().equals(code)){
					name=l.get(i).getFinancialInstitutionName();
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error PseWSEJB.getBankByCode ] ");
		}
		return name;
	}
	
	@Override
	public void createProcessForPse(Long creationUser, String ip,
			String messDetail, String messError, Long userId,Long processPSE,String file,Long pseId) {

				//creacion de proceso de cliente en Mi proceso
				TbProcessTrack idProc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, userId);
				Long newProc = null;
				if(idProc == null){
					newProc = process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, userId, ip, creationUser);
				}else{
					newProc = idProc.getProcessTrackId();
				}
				process.createProcessDetail(newProc, ProcessTrackDetailType.MANUAL_RECHARGE,
						messDetail, creationUser, ip, 1,
						messError,processPSE.intValue(),null,null,file,pseId,1L);

				//creacion de proceso de cliente
				TbProcessTrack idProcClient = process.searchProcess(ProcessTrackType.CLIENT, userId);
				Long newProcClient = null;
				if(idProcClient == null){
					newProcClient = process.createProcessTrack(ProcessTrackType.CLIENT, userId, ip, creationUser);
				}else{
					newProcClient = idProcClient.getProcessTrackId();
				}
			
				process.createProcessDetail(newProcClient, ProcessTrackDetailType.MANUAL_RECHARGE,
						messDetail, creationUser, ip,
						1, messError,processPSE.intValue(),null,null,file,pseId,1L);			
	
	}
	
	@Override
	public String getVoucherById(Long pseId){
		String res = "";
		try{
			/*Query q=em.createNativeQuery("SELECT PROCESS_TRACK_TRANS_PSE " +
					"FROM tb_process_track_detail " +
					"WHERE TRANSACTION_ID=?1");///PENDEINTE IDENTIFICAR SI ES PSE O EFECTIVO
			q.setParameter(1, pseId);*/
			
			Query q = em.createNativeQuery("SELECT distinct PROCESS_TRACK_TRANS_PSE,type_id " +
					"FROM tb_process_track_detail " +
					"WHERE TRANSACTION_ID = ?1 ");
			q.setParameter(1, pseId);
			Object[] result = (Object[]) q.getSingleResult();			
			String url = (String) result[0];
			Long type = Long.parseLong(result[1].toString());
			
			if(type == 1L || type == 2L){
				res = url;
			}else{
				res = url;
			}
			
			System.out.println("res: "+res);
			return res;
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error PseWSEJB.getVoucherById ] ");
			return null;
		}
	}
	
	@Override
	public Long getPseIdByUmbral(Long umbralId){
		try{
			Query q=em.createNativeQuery("select decode((SELECT count(*) FROM tb_pse_transaction t WHERE tpt.umbral_id=?1),0, tu.umbral_id, tpt.pse_transaction_id)" +
					"from tb_umbral tu " +
					"left join tb_pse_transaction tpt on tpt.umbral_id = tu.umbral_id " +
					"where tu.umbral_id= ?1 ");
			q.setParameter(1, umbralId);
			Long res=((BigDecimal)q.getSingleResult()).longValue();
			System.out.println("getPseIdByUmbral.res: "+res);
			return res;
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error PseWSEJB.getPseIdByUmbral ] ");
			return -1L;
		}
	}
	
	@Override
	public Long getAvalIdByUmbral(Long umbralId){
		try{
			Query q=em.createNativeQuery("select decode((SELECT count(*) FROM tb_aval_transaction t WHERE tpt.umbral_id=?1),0, tu.umbral_id, tpt.aval_transaction_id)" +
					"from tb_umbral tu " +
					"left join tb_aval_transaction tpt on tpt.umbral_id = tu.umbral_id " +
					"where tu.umbral_id= ?1 ");
			q.setParameter(1, umbralId);
			Long res=((BigDecimal)q.getSingleResult()).longValue();
			System.out.println("getPseIdByUmbral.res: "+res);
			return res;
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error PseWSEJB.getPseIdByUmbral ] ");
			return -1L;
		}
	}
	
	@Override
	public boolean applyRecharge(Long umbralId,Long state,String ip,Long cUserId){
		
		boolean retorno = false;
		BigDecimal tranId = null;
		try{
			
			Query transactioId = em.createNativeQuery("select transaction_id from tb_umbral where umbral_id = ?1 ");
			transactioId.setParameter(1, umbralId);
			tranId = (BigDecimal) transactioId.getSingleResult();
			
			
			if(tranId == null){
				System.out.println("tranId == null ");
				Query q=em.createNativeQuery("CALL sp_recharge(?1,?2)");
				q.setParameter(1, umbralId);
				q.setParameter(2, state);
				q.executeUpdate();
				retorno = true;
			}
			
			
		}catch (Exception e) {
			log.insertLog("Error al ejecutar procedimiento de recarga",
					LogReference.ACCOUNT_TRANSACTION, LogAction.OPERATIONFAILED, ip, cUserId);
			e.printStackTrace();
			System.out.println(" [ Error PseWSEJB.applyRecharge ] ");
			retorno = false;
		}
		
		return retorno;
	}
	
	//processState 3: Inicial; 4: Sonda General; 5: Sonda Cliente
	@Override
	public boolean verifyStateUmbral(Long umbralId, Long processState){
		try{
			System.out.println("umbralId: "+umbralId+", processState: "+processState);
			Query q=em.createNativeQuery("select UMBRAL_STATE from TB_UMBRAL where UMBRAL_ID=?1");
			q.setParameter(1, umbralId);
			Long estado=((BigDecimal)q.getSingleResult()).longValue();
			if(estado.equals(processState)){
				return true;
			}else{
				return false;
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error PseWSEJB.verifyStateUmbral ] ");
			return false;
		}
		

	}
	
	//processState 3: Inicial; 4: Sonda General; 5: Sonda Cliente
	@Override
	public boolean processingPse(Long pseId){
		try{
			TbPseTransaction t= em.find(TbPseTransaction.class, pseId);
			TbUmbral u = t.getTbUmbral();
			u.setUmbralState(3L);
			em.merge(u);
			em.flush();
			return true;
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error PseWSEJB.verifyStateUmbral ] ");
			return false;
		}
	}
	
	
	@Override
	public boolean processingAval(Long avalId){
		try{
			TbAvalTransaction t= em.find(TbAvalTransaction.class, avalId);
			TbUmbral u = t.getUmbralId();
			u.setUmbralState(3L);
			em.merge(u);
			em.flush();
			return true;
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error PseWSEJB.processingAval ] ");
			return false;
		}
	}
	
	@Override
	public Long getUmbralByPseId(Long pseId){
		try{
			Query q=em.createNativeQuery("SELECT umbral_id FROM tb_pse_transaction WHERE pse_transaction_id=?1");
			q.setParameter(1, pseId);
			Long res=((BigDecimal)q.getSingleResult()).longValue();
			System.out.println("getUmbralByPseId.res: "+res);
			return res;
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error PseWSEJB.getUmbralByPseId ] ");
			return -1L;
		}
	}
	
	@Override
	public Long getUmbralByAvalId(Long avalId){
		try{
			Query q=em.createNativeQuery("SELECT umbral_id FROM tb_aval_transaction WHERE aval_transaction_id=?1");
			q.setParameter(1, avalId);
			Long res=((BigDecimal)q.getSingleResult()).longValue();
			System.out.println("getUmbralByavalId.res: "+res);
			return res;
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error PseWSEJB.getUmbralByAvalId ] ");
			return -1L;
		}
	}
	
	//processState 3: Inicial; 4: Sonda General; 5: Sonda Cliente; 6: Sonda Cliente Aval; 7: Sonda General Aval;
	@Override
	public ArrayList<HistoricalRecharges> getPendingRecharges(Long accountId) {
		ArrayList<HistoricalRecharges> r=new ArrayList<HistoricalRecharges>();
		try{
			String query="SELECT umbral_id,umbral_time,type_message,umbral_value, bank_name, state, umbral_channel,umbral_state " +
			"FROM vw_historical_recharges " +
			"WHERE account_id=?1 " +
			"AND umbral_state in (0,4,5,6,7) " +
			"ORDER BY umbral_time ASC";
			System.out.println("getHistoricalRechargesByAccount.query: "+query);
			Query q=em.createNativeQuery(query);
			q.setParameter(1, accountId);
			List<?> result = q.getResultList();
			if(result != null){
				HistoricalRecharges h=null;
				for (Object o : result) {
					if (o != null && o instanceof Object[]) {
						h=new HistoricalRecharges();
						Object[] obj = (Object[]) o;
						h.setUmbralId(((BigDecimal)obj[0]).longValue());
						h.setUmbralTime(((Timestamp) obj[1]));
						h.setTypeRecharge(((String) obj[2]));
						h.setUmbralValue(((BigDecimal)obj[3]).longValue());
						h.setRechargeBank(((String) obj[4]));
						h.setUmbralState(((String) obj[5]));
						h.setUmbralChannel(((BigDecimal)obj[6]).longValue());
						h.setUmbralStateId(((BigDecimal)obj[7]).longValue());
						System.out.println(h.toString());
						r.add(h);

					}
				}
			}

		}catch (Exception e) {
			System.out.println("[ Error en PseWSEJB.getHistoricalRechargesByAccount. ]");
			e.printStackTrace();
		}
		return r;
	}
	
	//Type 0: OK; 1: Error
	@Override
	public String generateXML(Object o,String code,Long type){
		try{
			Radom rad = new Radom();
			Long num= rad.generate(1000L, 9999L, new ArrayList<Long>());
			
			SimpleDateFormat sf=new SimpleDateFormat("yyyyMMddHHmmssSSS");///TODO milisegundos
			String name= "PSE_"+code+"_R_"+sf.format(new Date())+num+(type==0L?"_rq.xml":"_rs.xml");
			BufferedWriter writer;
	        XStream xstream= null;
	        String xml= null;
	        xstream = new XStream(new DomDriver());
	        xml = xstream.toXML(o);
	        writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(parametros.getParameterValueById(52L)+
	        		"\\"+name), "UTF-8"));
            System.out.println(parametros.getParameterValueById(52L)+
	        		"\\"+name);
            writer.write(xml); 
            System.out.println("escrito"); 
            writer.close();
            return name;
		}catch (Exception e) {
			System.out.println("Error creando XML de request o response");
			e.printStackTrace();
			
			return null;
		}
	}
	
	/**
	 * M�todo encargado de actualizar el estado de las transacciones que se van a procesar correspondientes a PSE
	 */
	
	@Override
	public List<TbUmbral> getPendingRechargesForClient(Long accountId) {
		List<TbUmbral> umbralList=new ArrayList<TbUmbral>();
		TbUmbral u= null;
		try{			
			
			String query = "SELECT to_char(umbral_id) FROM Tb_Umbral u WHERE (u.umbral_State in (0,5) or (u.umbral_State in (4) and u.umbral_Modification_Time <= (systimestamp - ((1/1440)*5))))" +
			" AND u.umbral_Channel=1 AND u.account_Id=?1 ORDER BY umbral_Time ASC ";
			
					
			System.out.println("query: "+query);

			Query q=em.createNativeQuery(query);
			q.setParameter(1, accountId);

			for (Object obj : q.getResultList()) {
				Object o = (Object) obj;
				System.out.println("Objeto =>"+o.toString());
				u = em.find(TbUmbral.class,Long.parseLong(o.toString()));
				u.setUmbralState(5L);				
				u.setUmbralModificationTime(new Timestamp(System.currentTimeMillis()));
				em.merge(u);
				em.flush();
				umbralList.add(u);
			}

		}catch (Exception e) {
			e.printStackTrace(); 
			System.out.println(" [ Error PseWSEJB.getPendingRechargesForClient ] ");
		}
		return umbralList;
	}
	
	/**
	 * M�todo encargado de actualizar el estado de las transacciones que se van a procesar correspondientes a AVAL PREPAGO
	 * @author nathaly.ramirez
	 */
	@Override
	public List<TbUmbral> getPendingRechargesForClientAval(Long accountId) {
		List<TbUmbral> umbralList=new ArrayList<TbUmbral>();
		TbUmbral u= null;
		try{			

			String query = "SELECT to_char(umbral_id) FROM Tb_Umbral u WHERE (u.umbral_State in (0,6) or (u.umbral_State in (7) and u.umbral_Modification_Time <= (systimestamp - ((1/1440)*5))))" +
			" AND u.umbral_Channel=4 AND u.account_Id=?1 ORDER BY umbral_Time ASC ";
			
			System.out.println("query: "+query);

			Query q=em.createNativeQuery(query);
			q.setParameter(1, accountId);

			for (Object obj : q.getResultList()) {
				Object o = (Object) obj;

				u = em.find(TbUmbral.class,Long.parseLong(o.toString()));
				u.setUmbralState(5L); 
				u.setUmbralModificationTime(new Timestamp(System.currentTimeMillis()));
				em.merge(u);
				em.flush();
				umbralList.add(u);
			}

		}catch (Exception e) {
			e.printStackTrace(); 
			System.out.println(" [ Error PseWSEJB.getPendingRechargesForClientAval ] ");
		}
		return umbralList;
	}
	
	
	@Override
	public boolean isOnDayURLbyUmbral(Long idPse){
		boolean res=false;
		Calendar c=null;
		try{
			c=Calendar.getInstance();
			TbPseTransaction t=em.find(TbPseTransaction.class, idPse);
			c.setTimeInMillis(t.getTbUmbral().getUmbralTime().getTime());
			c.add(Calendar.DATE, 1);
			System.out.println("c.getTime(): "+c.getTime());
			if(c.getTime().after(new Date(System.currentTimeMillis()))){
				res=true;
			}else{
				res=false;
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error PseWSEJB.isOnDayURLbyUmbral ] ");
		}
		return res;
	}
	
	@Override
	public boolean isOnDayURLbyUmbralAval(Long idAval){
		boolean res=false;
		Calendar c=null;
		try{
			c=Calendar.getInstance();
			TbAvalTransaction t=em.find(TbAvalTransaction.class, idAval);
			c.setTimeInMillis(t.getUmbralId().getUmbralTime().getTime());
			c.add(Calendar.DATE, 1);
			System.out.println("c.getTime(): "+c.getTime());
			if(c.getTime().after(new Date(System.currentTimeMillis()))){
				res=true;
			}else{
				res=false;
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error PseWSEJB.isOnDayURLbyUmbral ] ");
		}
		return res;
	}
	
	@Override
	public boolean validateUserTransaction(Long pseId,Long userId){
		boolean res=false;
		try{
			TbPseTransaction t=em.find(TbPseTransaction.class, pseId);
			if(t.getTbUmbral().getTbAccount().getTbSystemUser().getUserId().
					equals(user.getSystemMasterById(userId))){
				res=true;
			}else{
				res=false;
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error PseWSEJB.validateUserTransaction ] ");
		}
		System.out.println("validateUserTransaction.res: "+res);
		return res;
	}
	
	public Connection getConnection() {
		try {
			Context context = new InitialContext();
			DataSource dataSource = (DataSource) context.lookup("jdbc/bo");
			Connection connection = dataSource.getConnection();
			System.out.println("PseConn: " + connection.getMetaData().getURL());
			return connection;
		} catch (NamingException e) {
			e.printStackTrace();
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	@Override
	public boolean listPendientes(Long idClientAccount){
		
		List<?> list = null;
		boolean ext = false;
		Query q = em.createNativeQuery("select tu.umbral_id from tb_umbral tu " +
				"inner join tb_pse_transaction tpt on tpt.umbral_id = tu.umbral_id " +
				"where tu.umbral_state = 0 " +  //todo
				"and tpt.aprobado is null " +
				"and tu.account_id = ?1 " );
		q.setParameter(1, idClientAccount);
		
		try{
			list = q.getResultList();
			if(list.size() >0){
				ext = true;
			}
		}catch (Exception e) {
			e.printStackTrace();
			ext = false;
		}
		
		return ext;
	}
	
/**
 * 	M�todo encargado de validar si el estado del convenio, el canal o la relaci�n entre los dos se encuentra activa
 *  @author nathaly.ramirez 
 *  @return boolean est
 */
	@SuppressWarnings({ "unused", "unchecked" })
	@Override
	public Long activeRelationshipAgreement(Long userId, Long idClientAccount){
		Long est = 5L;
		Object relation; 
		Long regPse;
		Long regGrupAval;
		List<?> list = null;
		
		
		String query = "SELECT count (rac.agreement_chanel_id)  " +
		"FROM tb_process_agreement_channel tpac, tb_chanel tc, tb_agreement ta, re_agreement_chanel rac " +
		"WHERE tpac.chanel_id = tc.chanel_id " +
		"and tpac.agreement_id = ta.agreement_id " +
		"and rac.chanel_id = tpac.chanel_id " +
		"and rac.agreement_id = tpac.agreement_id " +
		"and tpac.process_recharge_aval_id = 2 " +//Grupo Aval 
		"and rac.state_id = 1 " +
		"and ta.state_id = 1 " +
		"and tc.chanel_state = 1 " +
		"union all " +
		"SELECT count (rac.agreement_chanel_id)  " +
		"FROM tb_process_agreement_channel tpac, tb_chanel tc, tb_agreement ta, re_agreement_chanel rac " +
		"WHERE tpac.chanel_id = tc.chanel_id " +
		"and tpac.agreement_id = ta.agreement_id " +
		"and rac.chanel_id = tpac.chanel_id " +
		"and rac.agreement_id = tpac.agreement_id " +
		"and tpac.process_recharge_aval_id = 3 " + //PSE
		"and rac.state_id = 1 " +
		"and ta.state_id = 1 " +
		"and tc.chanel_state = 1 ";

		try{
			list = new ArrayList<Long>();
		
			list.addAll(em.createNativeQuery(query).getResultList());
			
			if(list != null){ 
				if(list.size() == 2L){
					if((list.get(0)).toString().equals("1") && (list.get(1)).toString().equals("1")){
						est = 1L; //Grupo Aval y PSE relaciones activas
					}else if((list.get(0)).toString().equals("1") && (list.get(1)).toString().equals("0") ){
						est = 2L;// Grupo Aval relaci�n activa
					}else if((list.get(0)).toString().equals("0") && (list.get(1)).toString().equals("1") ){
						est = 3L; //PSE relaci�n activa
					}else if((list.get(0)).toString().equals("0") && (list.get(1)).toString().equals("0") ){
						est = 4L; //Grupo Aval y PSE Sin relaci�n activa
						this.notificationWithoutActiveRelationship(userId, idClientAccount);
					}else{
						est = 5L;
						this.notificationWithoutActiveRelationship(userId, idClientAccount);
					}				
			}else if(list.size() == 0L){
				est = 5L;	
				this.notificationWithoutActiveRelationship(userId, idClientAccount);
			}
			}
		}catch (Exception e) {
			System.out.println("No existe la relaci�n entre el convenio y el canal, o no exite uno de los componentes");
			e.printStackTrace();
			est = 5L;
			this.notificationWithoutActiveRelationship(userId, idClientAccount);
		}
		return est;
	}
	
	
	public void notificationWithoutActiveRelationship(Long userId, Long idClientAccount ){
		System.out.println("notificationWithoutActiveRelationship");
		
		TbSystemUser tsu = em.find(TbSystemUser.class, userId);
		
		String	RESPONCE_TYPE_COD="La Relacion entre el Canal y el Convenio de entidades de grupo Aval y Pagos seguros en linea se encuentra inactiva";
		
		//Enviar NTF-05 ASIGNAR RECURSOS MANUALMENTE SIN CONVENIOS
		Map<String,Object> tags = new HashMap<String,Object>();
		Date date = Calendar.getInstance().getTime();
		//BM
		tags.put(EEmailParam.TIPO_ID.getTag(), tsu.getTbCodeType().getCodeTypeId()); //ok-17
		tags.put(EEmailParam.NUM_ID.getTag(), userId); //ok-5
		tags.put(EEmailParam.NOMB_CLI.getTag(), userId); // ok-4
		tags.put(EEmailParam.EMAIL_USU.getTag(), userId); //OK-65
		tags.put(EEmailParam.CUENTA_FACIL_PASS.getTag(), idClientAccount); //OK-1
		tags.put(EEmailParam.FECHA_ACCION.getTag(), date); //OK-18
		tags.put(EEmailParam.HORA_ACCION.getTag(), date); //OK-19
		tags.put(EEmailParam.ERRAVALD.getTag(), RESPONCE_TYPE_COD); //OK-46
		
		MessageParser mp = new SimpleTagMessageParser(tags);
		outbox.insertMessageOutbox2(userId, EmailProcess.RECHARGE_WITHOUT_AGREEMENT, true, mp);
		
	}
	
	/**
	 * Met�do encargado de crear la transacci�n en el portar de la entidad financiera
	 * @author nathaly.ramirez
	 * @param Long accountId
	 * @param Long userId
	 * @param String ipTransaction
	 * @param Long valueRecharge
	 * @return Long respuesta
	 */
	@Override
	public Long createTransactionAval(Long accountId, Long userId, String ipTransaction, Long valueRecharge,String context){
		
		//	Respu = -4L Estado inicial; respu=-8L Recarga Invalida; Respu= -100,-200,-300,-600,-700 error servicio
		/* PARA QUE NO EXISTA COMUNICACION CON EL SERVICIO SE TIENEN EN CUENTA LOS SIGUIENTES CRITERIOS
		 *  1) ADDTRANSACTION  RESPUESTA 300 
		 *  2) ERROR COMUNICANDOSE CON EL SERVICIO 
		 *  3)  QUE EL SERVICIO RESPONDA FUERA DE LAS RESPUESTAS ESPERADAS (100 - 200 - 600 - 700) 
		 */
		
		
		Long radId=null;
		Long respu=-4L;
		
		
		String rq= "";
		String rs= "";
		String bankName="";
		String codeError="";		
		String RESPONCE_TYPE_COD = null;
		String msgProcessAdmin = "";
		String msgProcessClient = "";
		
		Long   RqUID;
		String Channel;
		Date   ClientDT;
		String ipAddr;
		String custIdType;
		String custIdNum;
		String AgreementId;
		String PortalURL;
		
		Collection<SecretListDTO> SecretList=null;
		Collection<ReferenceDTO> reference=null;
		String secret;
		String secretId;
		
		
		Long OrderId;
		BigDecimal amt;
		String refId;
		
		TbAvalTransaction tat = new TbAvalTransaction();
		TbUmbral tu = new TbUmbral();
		TbAccount account=new TbAccount();
		TbChanel chanel=new TbChanel();
		TbAgreement agreement=new TbAgreement();
		TbSystemUser user=new TbSystemUser();
		
		
		boolean notification=false;
		
		
		try{
			//valor del campo processAgreementChannelId de la tabla  tb_process_agreement_channel con el fin de recuperar el canal y convenio de la modalidad prepago AVAL
			Long processAgreementChannelId=2L;
						
			Query que=em.createNativeQuery(" select CHANEL_ID, AGREEMENT_ID from tb_process_agreement_channel where PROCESS_RECHARGE_AVAL_ID=?1 ").setParameter(1, processAgreementChannelId);
			
			List<Object> list = (List<Object>) que.getResultList();
			
			if (list.size() > 0) {								
				
				for (int i = 0; i < list.size(); i++) {
					
					Object[] o = (Object[]) list.get(i);
					
					chanel = em.find(TbChanel.class, Long.parseLong(o[0].toString()));
					agreement = em.find(TbAgreement.class, Long.parseLong(o[1].toString()));
					
				}
				
			}
			
						
			account=em.find(TbAccount.class, accountId);
			user=em.find(TbSystemUser.class, userId);
			
					
			//Consulta el ultimo producto bancario asociado a la cuenta FacilPass		
			Query query= em.createNativeQuery("select ACCOUNT_BANK_ID from VW_LAST_PRODUCT_BANK where ACCOUNT_ID=?1");
			query.setParameter(1, accountId);
			
			try{
				radId=((BigDecimal)query.getSingleResult()).longValue();
			}catch (NoResultException e) {
				System.out.println("No se encontro ReAccountBank para la cuenta "+accountId);
			}
			
			Query queryx= em.createNativeQuery("select BANK_NAME from VW_LAST_PRODUCT_BANK where ACCOUNT_ID=?1");
			queryx.setParameter(1, accountId);
			
			try{
				bankName=queryx.getSingleResult().toString();
			}catch (NoResultException e) {
				System.out.println("No se encontro BankName para la cuenta "+accountId);
			}
			
			//Preparaci�n de datos para crea registro en TbUmbral (Recarga)
			
			tu.setTbAccount(em.find(TbAccount.class, accountId));
			tu.setAverage((new BigDecimal(0)));
			tu.setUmbralValue(new BigDecimal(valueRecharge));
			tu.setUmbralTime(new Timestamp(System.currentTimeMillis()));	
			tu.setUmbralState(0L);
			tu.setTypeMessageId(1L);
			tu.setUmbralChannel(4L);
			tu.setAccountBankId(radId);
			tu.setDescriptionPse(bankName);
			
			em.persist(tu);
			
			//Preparaci�n de datos para crea el registro en Tb_Aval_transacci�n (Transaci�n)
			
			tat.setChanelId(chanel);
			tat.setDateTransaction(new Timestamp(System.currentTimeMillis()));
			tat.setIpTransaction(ipTransaction);
			tat.setUserId(user);
			tat.setAgreementId(agreement);
			tat.setUrlRequest("https://www.google.com");
			tat.setSecretId("pending");
			tat.setSecret("pending");
			tat.setNure(account.getNure());
			tat.setValueTransaction(valueRecharge);
			tat.setReference1("1");
			tat.setTransactionType("0");
			tat.setUmbralId(tu);
			tat.setStateUrl(0L);
			
			
			
			//Persistir contexto
			em.persist(tat);
			
			//Preparaci�n de datos e instanciamiento de clase para consumir servicio
			RqUID=tat.getAvalTransactionId();
			Channel=chanel.getChanelId().toString();
			ClientDT=new Timestamp(System.currentTimeMillis());
			ipAddr=tat.getIpTransaction();
			custIdType= user.getTbCodeType().getCodeTypeAbbreviation();
			custIdNum=user.getUserCode();
			AgreementId=agreement.getAgreementId().toString();
			PortalURL=context+"verificator.jspx?a="+this.encodePSETransaction(tat.getAvalTransactionId()+"+"+userId);
			amt=new BigDecimal(tat.getValueTransaction());
			secretId=tat.getSecretId();
			secret=tat.getSecret();
			refId=tat.getReference1();
			
			SecretListDTO secretListx=(new SecretListDTO(secretId, "", 0L, secretId));
			SecretList = new ArrayList<SecretListDTO>();
			SecretList.add(secretListx);
			
			CustNameDTO custNameDto=new CustNameDTO("", user.getUserNames(), "", user.getUserSecondNames(), "",user.getUserId().toString()+"+"+tu.getTbAccount().getAccountId());
			PersonalDataDTO personaData=new PersonalDataDTO(custNameDto, custIdType, custIdNum, "", null, "", "", "", "", "", "", "", "", "","", null);
			
			CurrencyAmtDTO currency=new CurrencyAmtDTO(amt, "COP", null);
			FeeDTO fee=new FeeDTO();
			
			ReferenceDTO referencex=new ReferenceDTO(refId, "");
			reference = new ArrayList<ReferenceDTO>();
			reference.add(referencex);
			
			OrderId=Long.parseLong(tat.getNure());
			
			
			
			//Generaci�n del XML de RQ NOTA: se debe indicar si se debe generar con un nombre que especifique que la Cuenta es PSE-FACILPAGO � PSE-AVAL		
			rq = this.generateXML(tat.getAvalTransactionId(),tat.getReference1(),0L);
			
		
			AddTransactionResponseDTO response = null;
			//boolean enviarNotificacionDeLibreria=false;
				//WSservicesEJB wsgateway=new 	WSservicesEJB();			
				try{
					
					//Respuesta de la transacci�n
					//response=wsgateway.addTransactionRequest(RqUID, Channel, ClientDT, ipAddr, custIdType, custIdNum, AgreementId, PortalURL, SecretList, personaData, fee, reference,"1",userId,accountId);
					response=services.addTransactionRequest(RqUID, Channel, ClientDT, ipAddr, custIdType, custIdNum, AgreementId, PortalURL, SecretList, personaData, fee, reference,"1",userId,accountId);
					
					
					if(response==null){
						 
						 tu.setUmbralState(8L);
						 em.merge(tu);
						 em.flush();
						 respu=-8L;
						 throw new Exception("No se pudo crear la transacci�n,debido a que la respuesta de la solicitud fue nula");
						
						
					}
				}catch(Exception e){
					
					 tu.setUmbralState(8L);
					 em.merge(tu);
					 em.flush();
					 respu=-8L;
					 try{	
							FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
							.put("idAvalT", this.encodePSETransaction(String.valueOf(tat.getAvalTransactionId())));
						}catch(IllegalStateException i){
							System.out.println("[ Error en PseWSEJB.FacesContext ]");
							i.printStackTrace();
						}
					throw e;
					
				}
				//Si la transacci�n trae un valor diferente a nulo, realizamos un merge con los datos traidos por el servicio
				//Aqui
				if(response!=null){
					
					System.out.println("BOS.PseWSEJB.createTransactionAval() respx.getStatusCode(): " + response.getStatusCode());
					System.out.println("BOS.PseWSEJB.createTransactionAval() respx.getPortalURL(): " + response.getPortalURL());
					System.out.println("BOS.PseWSEJB.createTransactionAval() respx.getStatusDesc(): " + response.getStatusDesc());
					System.out.println("BOS.PseWSEJB.createTransactionAval() respx.getPmtId(): " + response.getPmtId());
					System.out.println("BOS.PseWSEJB.createTransactionAval() tat.getReference1(): " + tat.getReference1());
					
					rs = this.generateXML(response,tat.getReference1(),1L);
					
				
					tat.setStateTransactionId((response.getTransactionStatus().getTrnStatusCode()));
					tat.setDateCreateRequest(new Timestamp(System.currentTimeMillis()));
					tat.setTransactionIdentifier(response.getPmtId());
					tat.setUrlReturn(response.getPortalURL());
					tat.setAprobado(null);
					tat.setRequestId(response.getRqUID());
					
					
					
					em.merge(tat);
					//Si el servicio  Estado 0 (OK) sino imprimimos los posibles errores que nos puede entregar el servicio
					if(response.getRqUID() > 0L){
						System.out.println("BOS.PseWSEJB.createTransactionAval() tat.getStateTransactionId(): " + tat.getStateTransactionId());
						if(response.getStatusCode()!=0L){
							
							notification=true;
							RESPONCE_TYPE_COD =Long.toString(response.getStatusCode());		
							codeError=this.process.getResponseDescByCode(Long.valueOf(RESPONCE_TYPE_COD), 2L);
							tu.setUmbralState(2L);
							em.merge(tu);
								if(response.getStatusCode() ==	100L){//Error:  General
									respu=-100L;
								}else if(response.getStatusCode()	==	200L){//Error:   de datos
									respu=-200L;	
								}else if(response.getStatusCode()	==	300L){//Error:   Sistema no disponible
									respu=-300L;	
								}else if(response.getStatusCode()	==	600L){//Error: x trama 
									respu=-600L;	
								}else if(response.getStatusCode()	==  700L){//Error: x  procesamiento
									respu=-700L;	
								}
								
								
								try{	
									FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
									.put("idAvalT", this.encodePSETransaction(String.valueOf(tat.getAvalTransactionId())));
								}catch(IllegalStateException i){
									System.out.println("[ Error en PseWSEJB.FacesContext ]");
									i.printStackTrace();
								}
							
						}else{
							notification=false;
							respu=tat.getAvalTransactionId();
						}
							//Si la respuesta es diferente a los posibles Errores esperados, imprimimos dicho Error
								if( response.getStatusCode() !=	0L &&  response.getStatusCode() != 100L && response.getStatusCode() != 200L &&
										response.getStatusCode() != 300L && response.getStatusCode() != 600L && response.getStatusCode() != 700L){
								
									
								RESPONCE_TYPE_COD =Long.toString(response.getStatusCode());		
								codeError=this.process.getResponseDescByCode(response.getStatusCode(), 2L);
								
								msgProcessAdmin="La recarga de recursos de la cuenta Aval N�. "+tu.getTbAccount().getAccountId()+" present� el siguiente error: "+codeError+" - ";
								msgProcessClient="La recarga de recursos de la cuenta Aval N�. "+tu.getTbAccount().getAccountId()+" present� el siguiente error: "+codeError+".";
								Long codFinalizar = 0L;
								accountId = tu.getTbAccount().getAccountId();
								this.createProcessError(msgProcessAdmin, msgProcessClient, userId, tat.getIpTransaction(), rq, rs, codFinalizar, accountId);
								}
							}else{
								notification=true;
								System.out.println("Sin idTransaction, estado invalido");
								tu.setUmbralState(8L);
								em.merge(tu);
								em.flush();
								respu=-800L;		   
								
								
														
							}
				}
				
				em.flush();	
				//Si se presento algun Error, se envia una notificaci�n
				if(notification==true){
					//BM
					//Enviar NTF-06 ERROR TRANSACCI�N RECARGA AVAL
					Map<String,Object> tags = new HashMap<String,Object>();
					Date date = Calendar.getInstance().getTime();
					
					RESPONCE_TYPE_COD =Long.toString(response.getStatusCode());		
					codeError=this.process.getResponseDescByCode(Long.valueOf(RESPONCE_TYPE_COD), 2L);
					
					tags.put(EEmailParam.NOMB_CLI.getTag(), userId); //OK-4
					tags.put(EEmailParam.CUENTA_FACIL_PASS.getTag(), accountId); //OK-1
					tags.put(EEmailParam.TIPO_ID.getTag(), user.getTbCodeType().getCodeTypeId());//OK-17
					tags.put(EEmailParam.NUM_ID.getTag(), userId);//OK-5
					tags.put(EEmailParam.NOMB_CONVENIO.getTag(),agreement.getAgreementId());//OK-66
					tags.put(EEmailParam.ERRAVALC.getTag(), RESPONCE_TYPE_COD +" - "+ codeError);
					tags.put(EEmailParam.VAL_RECARG.getTag(), tu.getUmbralValue()); //OK-49
					tags.put(EEmailParam.FECHA_ACCION.getTag(), date);//OK-18
					tags.put(EEmailParam.HORA_ACCION.getTag(), date);//OK-19
					tags.put(EEmailParam.EMAIL_USU.getTag(), userId);//OK-65
					MessageParser mp = new SimpleTagMessageParser(tags);
					outbox.insertMessageOutbox2(userId, EmailProcess.AVAL_ERROR_CR, true, mp);
					
				}
		
		}catch (Exception e) {
			System.out.println(" [ Error PseWSEJB.processTransactionPse ] ");
			e.printStackTrace();
			   
			/*
			 * REALMENTE NO SE HABLA DE ENVIAR NOTIFICACIONES EN CASO DE QUE SE PRESENTE UN ERROR POR DATOS, CONEXION O TIMEOUT PARA EL PROCESO DE CREACION DE TRANSACCION
			 * 
			 */
			
			log.insertLog("Se genero el siguiente error: " + (e.getMessage()==null?e:e.getMessage()), LogReference.RECHARGEPSE, LogAction.ERRORRECHARGEPSE, tat.getIpTransaction(), tu.getTbAccount().getTbSystemUser().getUserId());
			
			msgProcessAdmin="La recarga de recursos de la cuenta FacilPass N�. "+tu.getTbAccount().getAccountId()+" no se cre�, debido a que no fue posible comunicarse con su entidad Aval  " +
					bankName ;	
			msgProcessClient="La recarga de recursos de la cuenta FacilPass N�. "+tu.getTbAccount().getAccountId()+" no se cre�, debido a que no fue posible comunicarse con su entidad Aval " +
					bankName;
			Long userCId = tu.getTbAccount().getTbSystemUser().getUserId();
			Long codFinalizar = this.user.getSystemMasterById(userId);
			accountId = tu.getTbAccount().getAccountId();
			
			this.createProcessError(msgProcessAdmin, msgProcessClient, userCId, tat.getIpTransaction(), rq, rs, codFinalizar, accountId);
			
			
		}
		return respu;
		
		
	}
	
	
	
	
}