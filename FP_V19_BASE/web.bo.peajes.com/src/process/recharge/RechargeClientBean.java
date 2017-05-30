package process.recharge;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jpa.ReAccountBank;
import jpa.TbAccount;
import jpa.TbProcessTrack;
import jpa.TbSystemUser;
import linews.InvokeSqdm;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import report.ReportConfigUtil;
import security.UserLogged;
import sessionVar.SessionUtil;
import upload.FileUtil;
import util.ConnectionData;
import util.HistoricalRecharges;
import util.ObjectRechage;
import util.ws.PseWS;
import util.ws.WSRecharge;
import validator.Validation;

import com.ath.service.payments.pseservices.BankACHData;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import constant.AccountStateType;
import constant.EmailProcess;
import constant.ProcessTrackDetailType;
import constant.ProcessTrackType;
import ejb.Process;
import ejb.ProcessAgreementChannel;
import ejb.SystemParameters;
import ejb.User;
import ejb.email.Outbox;


public class RechargeClientBean implements Serializable {
	private static final long serialVersionUID = 6500805642305307299L;

	@EJB(mappedName = "ejb/User")
	private User user;

	@EJB(mappedName = "ejb/Process")
	private Process proceso;

	@EJB(mappedName = "util/ws/WSRecharge")
	private WSRecharge WSrecharge;

	@PersistenceContext(unitName = "bo")
	private EntityManager em;

	@EJB(mappedName ="ejb/email/Outbox")
	private Outbox outbox;

	@EJB(mappedName ="util/ws/PseWS")
	private PseWS pseWS;

	@EJB(mappedName ="ejb/SystemParameters")
	private SystemParameters parametros;
	
	
	@EJB(mappedName ="ejb/ProcessAgreementChannel")
	private ProcessAgreementChannel processac;


	private Long idClientAccount;

	private List<SelectItem> clientAccounts;

	private Long valueRecharge;

	private String valueRechText;

	private String msgModal;
	
	private String msgConfiRecharge="";

	private boolean showModal;

	private boolean showConfirmationModal;

	private long valmin;

	private String valMinText="";

	private boolean showBotton;

	private boolean showWait;

	private UserLogged usrs;

	private boolean showPseAccount=false;
	
	private boolean showPse=false;
	
	private boolean showPse1=false;
	
	private boolean showAvalAccount=false;
	
	private boolean showAceptar = true;
	
	private boolean showCancelar = true;
	
	private boolean noButtons = false;
	
	private boolean noBanks = false;

	private String typePer="0";

	private String idPseEntity;

	private List<SelectItem> pseEntities=null;
	
	private List<BankACHData> banks=null; 

	private List<HistoricalRecharges> rechargeList;
	
	private List<HistoricalRecharges> pendingRechargeList;
	
	private boolean showRechargeList;

	public int processError;
	
	private String context="";
	
	private String pseIdStr=null;
	
	private String avalIdStr=null;
	
	private Long pseId=-1L;
	
	private Long avalId=-1L;
	
	private String bankName="";
	
	private Long expUmbralId=-1L;
	
	private String tooltipCuenta="";
	
	private String tooltipEntidad="";
	
	private String tooltipValor="";
	
	private String tooltipPersona="";
	
	private String tooltipAval="";
	
	private String tooltipPSE="";
	
	private boolean showModalPSE=false;
	
	private boolean showConfirmationModalPSE=false;
	
	private boolean showConfirmationModalAVAL=false;
	
	private boolean showConfiModalRecharge=false;
	
	public boolean disableButtons=false;
	
	private String COMPILE_FILE_NAME = " ";

	private final String COMPILE_DIR = "/jasper/design/";
	
	private String msgRe;
	
	private String msgNoButtons;
	
	private String histRecharge;
	
	private int page = 1;
	
	private boolean messageMinrecharge = false;
	
	private static final int DEFAULT_BUFFER_SIZE = 10240; // 10KB.
	
	/**
	 * Constante que almacena el filtro por el cual filtra la lista de pendientes 
	 */
	private static final String STADO_PENDIENTE="Pendiente";
		
	private static final Long PREPAGO=71L;
	
	private static final Long POSTPAGO=30L;	
	
	private boolean showPending=false;
	private int bt = 0; 
	
	private int key =-1; 
		
	

	public RechargeClientBean(){
		usrs = (UserLogged) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
		this.setShowBotton(false);
	}

	@PostConstruct
	public void init(){
		Long valor,aprobado;
		String nomBank;
		msgModal = "";
		showModal = false;
		showConfirmationModal =  false;
		showConfirmationModalPSE = false;
		showModalPSE = false;
		HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		
		System.out.println("url1: "+origRequest.getPathInfo());
		context=origRequest.getRequestURL().substring(0,origRequest.getRequestURL().indexOf(origRequest.getPathInfo())+1);
		
		System.out.println("context: "+context);
		
		
		
		pseIdStr=(String)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pse");
		avalIdStr=(String)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("aval");
		
		if(pseIdStr!=null){key=0;}
		if(avalIdStr!=null){key=1;}
		
		System.out.println("pseIdStr: "+pseIdStr);
		System.out.println("avalIdStr: "+avalIdStr);
		
		
		switch(key){
			
			case 0:
				//INGRESO URL PSE		
				if(pseIdStr!=null){
					try{
						FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
						.put("pse", null);
						pseId=Long.parseLong(pseWS.decodePSETransaction(pseIdStr));
						System.out.println("pseId: "+pseId);
						aprobado=pseWS.getAprobadoById(pseId);
						valor=pseWS.getValorById(pseId);
						nomBank=pseWS.getBancoById(pseId);
						DecimalFormat nf = new DecimalFormat("###,###,###");
						this.setIdClientAccount(pseWS.getAccountByPse(pseId));
						if(aprobado!=null){
							if(idClientAccount!=-1L){
								this.setShowRechargeList(true);
								this.setShowPseAccount(true);
								if(aprobado==0L){
									msgModal="La recarga de recursos por valor de $"+nf.format(valor)
										+" para la Cuenta FacilPass No. "+idClientAccount+" fue rechazada " +
												"por la entidad FACILPAGO - "+nomBank+". Para ver el comprobante " +
														"de la transacci�n de clic en el bot�n Ver Comprobante.";
								}else if(aprobado==1L){
									msgModal="La recarga de recursos por valor de $"+nf.format(valor)
									+" para la Cuenta FacilPass No. "+idClientAccount+" fue aprobada " +
											"por la entidad FACILPAGO - "+nomBank+". Para ver el comprobante " +
													"de la transacci�n de clic en el bot�n Ver Comprobante.";
								}
								showConfirmationModalPSE = true;
							}else{
								msgModal="Error, por favor int�ntelo nuevamente.";
								showModal = true;
							}
						}else{
							if(idClientAccount!=-1L){
								this.setShowPseAccount(true);
								this.setShowRechargeList(true);
								pseWS.processingPse(pseId);
								Long res=-5L;
								if(pseWS.verifyStateUmbral(pseWS.getUmbralByPseId(pseId), 3L)){
									res=pseWS.endPseTransaction(pseId,SessionUtil.ip(),usrs.getUserId(),0L);
								}
								System.out.println("res: "+res);
								if(res==-3L||res==-4L){
									msgModal="No fue posible validar la transacci�n, por favor valide con " +
											"su entidad.";
									showModalPSE = true;
								}else{
									if(res==-1L){
										pseWS.createProcessForPse(usrs.getUserId(), SessionUtil.ip(), "La recarga de Recursos " +
												"por valor de $"+nf.format(valor)+" para la Cuenta FacilPass No. "+idClientAccount+" rechazada " +
												"por la entidad FACILPAGO - "+nomBank+". Para ver el comprobante de la transacci�n de clic ", 
												"Error al Crear Proceso para Recarga Manual de la cuenta "+
												idClientAccount, user.getSystemMasterById(usrs.getUserId()),2L,
												this.saveVoucher(user.getSystemMasterById(usrs.getUserId()),
														pseId),pseId);
										msgModal="Se�or usuario se present� un error en los datos que ingres�, para " +
												"verificar su transacci�n, por favor valide con su entidad.";
										showModalPSE = true;
									}else if(res==-2L){
										pseWS.createProcessForPse(usrs.getUserId(), SessionUtil.ip(), "La recarga de Recursos " +
												"por valor de $"+nf.format(valor)+" para la Cuenta FacilPass No. "+idClientAccount+" rechazada " +
												"por la entidad FACILPAGO - "+nomBank+". Para ver el comprobante de la transacci�n de clic ", 
												"Error al Crear Proceso para Recarga Manual de la cuenta "+
												idClientAccount, user.getSystemMasterById(usrs.getUserId()),2L,
												this.saveVoucher(user.getSystemMasterById(usrs.getUserId()),
														pseId),pseId);
										msgModal="No hay comunicaci�n con su entidad financiera, para " +
											"verificar su transacci�n, por favor valide con su entidad.";
										showModalPSE = true;
									}else if(res==-5L){
										msgModal="La transacci�n a�n est� pendiente, por favor valide m�s tarde " +
												"en est� misma opci�n.";
										showModalPSE = true;
									}else if(res==-6L){
										pseWS.createProcessForPse(usrs.getUserId(), SessionUtil.ip(), "La recarga de Recursos " +
												"por valor de $"+nf.format(valor)+" para la Cuenta FacilPass No. "+idClientAccount+" rechazada " +
												"por la entidad FACILPAGO - "+nomBank+". Para ver el comprobante de la transacci�n de clic ", 
												"Error al Crear Proceso para Recarga Manual de la cuenta "+
												idClientAccount, user.getSystemMasterById(usrs.getUserId()),2L,
												this.saveVoucher(user.getSystemMasterById(usrs.getUserId()),
														pseId),pseId);
										msgModal="La recarga de recursos por valor de $"+nf.format(valor)
										+" para la Cuenta FacilPass No. "+idClientAccount+" fue rechazada " +
												"por la entidad FACILPAGO - "+nomBank+". Para ver el comprobante " +
														"de la transacci�n de clic en el bot�n Ver Comprobante.";
										showConfirmationModalPSE = true;
									}else{
										pseWS.createProcessForPse(usrs.getUserId(), SessionUtil.ip(), "La recarga de Recursos " +
												"por valor de $"+nf.format(valor)+" para la Cuenta FacilPass No. "+idClientAccount+" aprobada " +
												"por la entidad FACILPAGO - "+nomBank+". Para ver el comprobante de la transacci�n de clic ", 
												"Error al Crear Proceso para Recarga Manual de la cuenta "+
												idClientAccount, user.getSystemMasterById(usrs.getUserId()),2L,
												this.saveVoucher(user.getSystemMasterById(usrs.getUserId()),
														pseId),pseId);
										msgModal="La recarga de recursos por valor de $"+nf.format(valor)
										+" para la Cuenta FacilPass No. "+idClientAccount+" fue aprobada " +
												"por la entidad FACILPAGO - "+nomBank+". Para ver el comprobante " +
														"de la transacci�n de clic en el bot�n Ver Comprobante.";
										changeAccount();
										showConfirmationModalPSE = true;
									}
								}
							}else{
								msgModal="Error, por favor int�ntelo nuevamente.";
								showModal = true;
							}
						}
					}catch (Exception e) {
						e.printStackTrace();
						System.out.println("[ Error en RechargeClientBean.init ]");
					}
				}
				
				break;
			
			
			case 1:
				
				//INGRESO URL PARA ENTIDAD AVAL
				if(avalIdStr!=null){
					try{
						FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
						.put("aval", null);
						avalId=Long.parseLong(pseWS.decodePSETransaction(avalIdStr));
						System.out.println("avalId: "+avalId);
						aprobado=pseWS.getAprobadoByIdAval(avalId);
						valor=pseWS.getValorByIdAval(avalId);
						nomBank=pseWS.getBancoByIdAval(avalId);
						DecimalFormat nf = new DecimalFormat("###,###,###");
						this.setIdClientAccount(pseWS.getAccountByAval(avalId));
						if(aprobado!=null){
							if(idClientAccount!=-1L){
								this.setShowRechargeList(true);
								this.setShowPseAccount(true);
								if(aprobado==0L){
									msgModal="La recarga de recursos por valor de $"+nf.format(valor)
										+" para la Cuenta FacilPass No. "+idClientAccount+" fue rechazada " +
												"por la entidad FACILPAGO - "+nomBank+". Para ver el comprobante " +
														"de la transacci�n de clic en el bot�n Ver Comprobante.";
								}else if(aprobado==1L){
									msgModal="La recarga de recursos por valor de $"+nf.format(valor)
									+" para la Cuenta FacilPass No. "+idClientAccount+" fue aprobada " +
											"por la entidad FACILPAGO - "+nomBank+". Para ver el comprobante " +
													"de la transacci�n de clic en el bot�n Ver Comprobante.";
								}
								showConfirmationModalPSE = true;
							}else{
								msgModal="Error, por favor int�ntelo nuevamente.";
								showModal = true;
							}
						}else{
							if(idClientAccount!=-1L){
								this.setShowPseAccount(true);
								this.setShowRechargeList(true);
								pseWS.processingAval(avalId);
								Long res=-5L;
								if(pseWS.verifyStateUmbral(pseWS.getUmbralByAvalId(avalId), 3L)){
									res=pseWS.endAvalTransaction(avalId,SessionUtil.ip(),usrs.getUserId(),0L);
								}
								System.out.println("res: "+res);
								if(res==-500L ){
									msgModal="No fue posible validar la transacci�n, por favor valide con " +
											"su entidad.";
									showModalPSE = true;
								}else{
									if(res==-200L || res==-600  || res==-700){
										pseWS.createProcessForPse(usrs.getUserId(), SessionUtil.ip(), "La recarga de Recursos " +
												"por valor de $"+nf.format(valor)+" para la Cuenta FacilPass No. "+idClientAccount+" rechazada " +
												"por la entidad FACILPAGO - "+nomBank+". Para ver el comprobante de la transacci�n de clic ", 
												"Error al Crear Proceso para Recarga Manual de la cuenta "+
												idClientAccount, user.getSystemMasterById(usrs.getUserId()),2L,
												this.saveVoucherAval(user.getSystemMasterById(usrs.getUserId()),
														avalId),avalId);
										msgModal="Se�or usuario se present� un error en los datos que ingres�, para " +
												"verificar su transacci�n, por favor valide con su entidad.";
										showModalPSE = true;
									}else if(res==-300L || res ==- 100){
										pseWS.createProcessForPse(usrs.getUserId(), SessionUtil.ip(), "La recarga de Recursos " +
												"por valor de $"+nf.format(valor)+" para la Cuenta FacilPass No. "+idClientAccount+" rechazada " +
												"por la entidad FACILPAGO - "+nomBank+". Para ver el comprobante de la transacci�n de clic ", 
												"Error al Crear Proceso para Recarga Manual de la cuenta "+
												idClientAccount, user.getSystemMasterById(usrs.getUserId()),2L,
												this.saveVoucherAval(user.getSystemMasterById(usrs.getUserId()),
														avalId),avalId);
										msgModal="No hay comunicaci�n con su entidad financiera " +
											" intente m�s tarde.";
										showModalPSE = true;
									}else if(res==-5L ||	res==-3L){
										msgModal="La transacci�n a�n est� pendiente, por favor valide m�s tarde " +
												"en est� misma opci�n.";
										showModalPSE = true;
									}else if(res==-1L || res==-2L ||  res==-4L || res==-5L || res==-7L ){
										pseWS.createProcessForPse(usrs.getUserId(), SessionUtil.ip(), "La recarga de Recursos " +
												"por valor de $"+nf.format(valor)+" para la Cuenta FacilPass No. "+idClientAccount+" rechazada " +
												"por la entidad FACILPAGO - "+nomBank+". Para ver el comprobante de la transacci�n de clic ", 
												"Error al Crear Proceso para Recarga Manual de la cuenta "+
												idClientAccount, user.getSystemMasterById(usrs.getUserId()),2L,
												this.saveVoucherAval(user.getSystemMasterById(usrs.getUserId()),
														avalId),avalId);
										msgModal="La recarga de recursos por valor de $"+nf.format(valor)
										+" para la Cuenta FacilPass No. "+idClientAccount+" fue rechazada " +
												"por la entidad FACILPAGO - "+nomBank+". Para ver el comprobante " +
														"de la transacci�n de clic en el bot�n Ver Comprobante.";
										showConfirmationModalAVAL = true;
									}else if(res==6L){
										pseWS.createProcessForPse(usrs.getUserId(), SessionUtil.ip(), "La recarga de Recursos " +
												"por valor de $"+nf.format(valor)+" para la Cuenta FacilPass No. "+idClientAccount+" aprobada " +
												"por la entidad FACILPAGO - "+nomBank+". Para ver el comprobante de la transacci�n de clic ", 
												"Error al Crear Proceso para Recarga Manual de la cuenta "+
												idClientAccount, user.getSystemMasterById(usrs.getUserId()),2L,
												this.saveVoucherAval(user.getSystemMasterById(usrs.getUserId()),
														avalId),avalId);
										msgModal="La recarga de recursos por valor de $"+nf.format(valor)
										+" para la Cuenta FacilPass No. "+idClientAccount+" fue aprobada " +
												"por la entidad FACILPAGO - "+nomBank+". Para ver el comprobante " +
														"de la transacci�n de clic en el bot�n Ver Comprobante.";
										
										changeAccount();
										showConfirmationModalAVAL = true;
									}
								}
							}else{
								msgModal="Error, por favor int�ntelo nuevamente.";
								showModal = true;
							}
						}
					}catch (Exception e) {
						e.printStackTrace();
						System.out.println("[ Error en RechargeClientBean.init ]");
					}
				}
				
				break;
				
			default:
				break;
 		
		}
		
		
		
	}
	
	public boolean isMessageMinrecharge() {
		return messageMinrecharge;
	}

	public void setMessageMinrecharge(boolean messageMinrecharge) {
		this.messageMinrecharge = messageMinrecharge;
	}
	
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setIdClientAccount(Long idClientAccount) {
		this.idClientAccount = idClientAccount;
	}

	public Long getIdClientAccount() {
		return idClientAccount;
	}

	public void setClientAccounts(List<SelectItem> clientAccounts) {
		this.clientAccounts = clientAccounts;
	}
	
	public List<SelectItem> getClientAccounts() {
		try {
			Long userId=user.getSystemMasterById(usrs.getUserId());
			if(clientAccounts == null){
				clientAccounts = new ArrayList<SelectItem>();
			} else {
				clientAccounts.clear();
			}
			clientAccounts.add(new SelectItem(-1L, "--Seleccione Una--"));
			
				for(TbAccount su : user.getClientAccount(userId)){
					System.out.println(su.getAccountState().longValue());
					if((su.getAccountState().longValue()==AccountStateType.ACTIVE.getId()) ||
							(su.getAccountState().longValue()==7L)){
						clientAccounts.add(new SelectItem(su.getAccountId(), "Cuenta No. " + su.getAccountId()));
					}			
				}			
		} catch (Exception e) {
			e.printStackTrace();
			clientAccounts = new ArrayList<SelectItem>();
			clientAccounts.clear();
			clientAccounts.add(new SelectItem(-1L, "--Seleccione Una--"));
			msgModal = "Ha Ocurrido un Error en el sistema, vuelva a intentarlo.";					
			showModal = true;
		}
		return clientAccounts;
	}

	public void setValueRecharge(Long valueRecharge) {
		this.valueRecharge = valueRecharge;
	}

	public Long getValueRecharge() {
		return valueRecharge;
	}

	public void setMsgModal(String msgModal) {
		this.msgModal = msgModal;
	}

	public String getMsgModal() {
		return msgModal;
	}

	public void setShowModal(boolean showModal) {
		this.showModal = showModal;
	}

	public boolean isShowModal() {
		return showModal;
	}


	public void msgSavePse(){
				
		try {
			System.out.println("msgSavePse");
			hideModals();
			System.out.println("idClientAccount: "+idClientAccount);
			System.out.println("idPseEntity: "+idPseEntity);
			System.out.println("typePer: "+typePer);
			valueRechText=valueRechText.replaceFirst("^0*", "").trim();
			System.out.println("valueRechText2 " + valueRechText);
			//showPseAccount = false;
			if(idClientAccount != -1L){
				if(!idPseEntity.equals("-1L")){
					bankName=pseWS.getBankByCode(banks, idPseEntity);
					System.out.println("bankName: "+bankName);
					System.out.println("valueRechText: "+valueRechText);
					if((valueRechText.equals("")) || (valueRechText.equals(null))){
						//showPseAccount = false;
						msgModal = "El campo Valor a Asignar es requerido.";
						showConfirmationModal = false;
						showModalPSE = true;
					}else if(!Validation.isNumericPuntoYComaNoConsecutive(valueRechText)){
						msgModal = "El campo Valor a Asignar contiene caracteres inv�lidos, por favor verifique";
						showConfirmationModal = false;
						//showPseAccount = false;
						showModalPSE = true;
					} else {
						if((valueRechText.equals("")) || (valueRechText.equals(null))){
							msgModal = "El campo Valor a Asignar es requerido.";
							showConfirmationModal = false;
							//showPseAccount = false;
							showModalPSE = true;					
						} else{
							valueRecharge = Long.parseLong(valueRechText.replace(".", "").replace(",", ""));
						}
						//this.getValmin(POSTPAGO);
						this.getValMonMinRec(3L);
						System.out.println("valmin2 "+valmin);
						if(valueRecharge < valmin){
							//showPseAccount = false;
							msgModal = "El valor Ingresado es menor al valor m�nimo para recarga";
							showConfirmationModal = false;
							showModalPSE = true;
						} else {
							msgModal = "Se va a realizar una asignaci�n de recursos por valor de $"+valueRechText+
								" con la entidad bancaria "+bankName;
							showModalPSE = false;
							showConfirmationModal = true;
						}
					}
				}else{
					//showPseAccount = false;
					msgModal = "No ha seleccionado la entidad bancaria";
					showConfirmationModal = false;
					showModalPSE = true;
				}
			} else {
				showPseAccount = false;
				msgModal = "No ha seleccionado cuenta";
				showConfirmationModal = false;
				showModal = true;
			}
		} catch(Exception e){
			e.printStackTrace();
			showPseAccount = false;
			msgModal = "Ha Ocurrido un Error";
			showConfirmationModal = false;
			showModal = true;
		}
	}

	public void msgSave(){
		try {
			System.out.println("msgSave");
			hideModals();
			valueRechText=valueRechText.replaceFirst("^0*", "").trim();
			System.out.println("valueRechText2 " + valueRechText);
			if(idClientAccount != -1L){
				System.out.println("valueRechText: "+valueRechText);
				if((valueRechText.equals("")) || (valueRechText.equals(null))){
					showPseAccount = false;
					msgModal = "El campo Valor a Asignar es requerido.";
					showConfirmationModal = false;
					showModal = true;
				}else if(!Validation.isNumericPuntoYComaNoConsecutive(valueRechText)){
					msgModal = "El campo Valor a Asignar contiene caracteres inv�lidos, por favor verifique";
					showConfirmationModal = false;
					showPseAccount = false;
					showModal = true;
				} else {	
					if((valueRechText.equals("")) || (valueRechText.equals(null))){
						msgModal = "El campo Valor a Asignar es requerido.";
						showConfirmationModal = false;
						showPseAccount = false;
						showModal = true;					
					} else{
						valueRecharge = Long.parseLong(valueRechText.replace(".", "").replace(",", ""));
					}
					//this.getValmin(PREPAGO);
					this.getValMonMinRec(1L);
					System.out.println("valmin2 "+valmin);
					if(valueRecharge < valmin){
						showPseAccount = false;
						msgModal = "El valor Ingresado es menor al valor m�nimo para recarga";
						showConfirmationModal = false;
						showModal = true;
					} else {
						showPseAccount = false;
						msgModal = "Asignaci�n de Recursos por valor de "+valueRechText;
						msgModal = msgModal+"\n"+ "�Est� seguro de realizar esta operaci�n?";
						showModal = false;
						showConfirmationModal = true;
					}
				}
			} else {
				showPseAccount = false;
				msgModal = "No ha seleccionado cuenta";
				showConfirmationModal = false;
				showModal = true;
				this.setMessageMinrecharge(false);
			}
		} catch(Exception e){
			e.printStackTrace();
			showPseAccount = false;
			msgModal = "Ha Ocurrido un Error";
			showConfirmationModal = false;
			showModal = true;
		}
	}

	public void changeAccount(){
		
		Long res;
		showPending = false;
		this.valMinText="";
		this.valueRechText="";
		this.setMessageMinrecharge(false);
		
		if(idClientAccount==-1L){
			this.setShowPseAccount(false);
			this.setShowRechargeList(false);
			this.setShowAvalAccount(false);
			this.setShowPse(false);
			this.setShowPse1(false);
			this.setShowAceptar(true);
			this.setShowCancelar(true);
			this.setNoButtons(false);
			this.setNoBanks(false);	
			
		}else{
			this.setShowRechargeList(true);
			try {
				if(user.validatePseAccount(idClientAccount)){
					
							
										
					msgRe = "Recarga";
					histRecharge = "Recargas";
					setMessageMinrecharge(true);
					this.getPseEntities();
					this.setShowPseAccount(true);
					this.setShowPse1(false);
					this.setShowConfiModalRecharge(false);					
					this.setShowAceptar(false);
					this.setShowCancelar(false);
					this.setNoBanks(false);
					
					res = pseWS.activeRelationshipAgreement(usrs.getUserId(),idClientAccount);
					
					if(res == 1L){
						getValMonMinRec(2L);
						this.setShowAvalAccount(true);	
						this.setShowPse(true);
						this.setNoButtons(false);						
					}else if(res == 2L){
						
						getValMonMinRec(2L);
						
						this.setShowAvalAccount(true);
						this.setShowPse(false);
						this.setNoButtons(false);
					}else if(res == 3L){
						
						getValMonMinRec(3L);
						
						this.setShowPse(true);
						this.setNoButtons(false);
						this.setShowAvalAccount(false);
					}else if(res == 4L){
						this.setNoButtons(true);
						msgNoButtons = "La opci�n est� fuera de servicio temporalmente";
					}else if(res == 5L){
						this.setNoButtons(true);
						msgNoButtons = "La opci�n est� fuera de servicio temporalmente";
					}
																		
				}else{
					
					getValMonMinRec(1L);	
					
					msgRe = "Asignaci�n";
					histRecharge = "Asignaciones de Recursos";
					setMessageMinrecharge(true);
					this.setShowPseAccount(false);
					this.setShowAvalAccount(false);
					this.setShowPse(false);
					this.setShowPse1(false);
					this.setShowConfiModalRecharge(false);
					this.setShowAceptar(true);
					this.setShowCancelar(true);
					this.setNoButtons(false);
					this.setNoBanks(false);
				}
				
			} catch (Exception e) {
				this.setShowPseAccount(false);
				setMessageMinrecharge(false);
				this.setShowAvalAccount(false);
				this.setShowPse(false);
				this.setShowPse1(false);
				this.setShowConfiModalRecharge(false);
				this.setNoButtons(false);
				this.setNoBanks(false);
				e.printStackTrace();
			}			
		}
		
		System.out.println("changeAccount.ShowPse: "+showPse);
		
		if(showPse || showAvalAccount){
			pendingRechargeList=pseWS.getPendingRecharges(idClientAccount);
			if(pendingRechargeList.size()>0){

				try{
					if(this.showAvalAccount){
					this.callThreadSondaClient();
					}
					if(this.showPseAccount){
					this.callThreadSondaClientAval();
					}
					//pendingRechargeList=pseWS.getPendingRecharges(idClientAccount);
					this.getRechargeList();
					pendingRechargeList.clear();
					for (HistoricalRecharges pendi : rechargeList) {
						if(pendi.getUmbralState().equals(STADO_PENDIENTE)&& (pendi.getUmbralChannel() == 1 || pendi.getUmbralChannel() == 4) ){
							pendingRechargeList.add(pendi);
						}
					}
					/*if(pseWS.listPendientes(idClientAccount)){*/
					if(pendingRechargeList.size()>0){
						msgModal="Usted tienen transacciones a�n pendientes por aplicar, " +
						"desea continuar con una nueva transacci�n?";
						showPending=true;
						this.setShowPseAccount(true);
						System.out.println("Logica validaci�n");
					}
				}catch (Exception e) {						
					System.out.println("error sonda cliente");
					msgModal = "Ha Ocurrido un Error en el sistema, vuelva a intentarlo.";					
					showModal = true;
					e.printStackTrace();
				}				
			}else{
				this.getRechargeList();
			}
		}else{
			this.getRechargeList();
		}				
		this.setShowRechargeList(true);
		
		System.out.println("lista actualizada...");
	}

	private void callThreadSondaClient() {		
		
			hideModals();
			ExternalContext externalContext = FacesContext.getCurrentInstance()
			.getExternalContext();
			ServletContext cont = (ServletContext) externalContext
			.getContext();
			HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance()
			.getExternalContext().getRequest();
			ThreadSondaClientPSEBean t=new ThreadSondaClientPSEBean();
			t.setAccountId(idClientAccount);
			t.setContext(cont);
			t.setReq(origRequest);
			t.run();
		
	}

	private void callThreadSondaClientAval() {		
		
		hideModals();
		ExternalContext externalContext = FacesContext.getCurrentInstance()
		.getExternalContext();
		ServletContext cont = (ServletContext) externalContext
		.getContext();
		HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance()
		.getExternalContext().getRequest();
		ThreadSondaClientAval sondaAval=new ThreadSondaClientAval();
		sondaAval.setAccountId(idClientAccount);
		sondaAval.setContext(cont);
		sondaAval.setReq(origRequest);
		sondaAval.start();
	
}
	
	public void onComplete() {  
		FacesContext.getCurrentInstance().
		addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
				"Transacci�n en proceso...", "Transacci�n en proceso..."));  
	}

	public void cancel(){
		System.out.println("Cancel");
		
		System.out.println("idClientAccount: "+idClientAccount);
		System.out.println("bankName: "+bankName);
		System.out.println("valueRechText: "+valueRechText);
		
		setPage(1);
		showModalPSE = false;
		showConfirmationModalPSE = false;
		showPending = false;
		msgModal = "";
		showModal = false;
		showConfirmationModal =  false;
		this.setShowRechargeList(false);
		this.setShowBotton(false);
		showPseAccount=false;
		bankName="";
		valueRecharge = null;
		this.setIdClientAccount(-1L);
		valueRechText = "";
		this.setMessageMinrecharge(false);
		this.setShowAvalAccount(false);
		this.setShowAceptar(true);
		this.setShowPse(false);
		this.setShowPse1(false);
		this.setShowCancelar(true);
		this.setNoButtons(false);
	}

	public void cancelPSE(){
		System.out.println("cancelPse");
		System.out.println("idClientAccount: "+idClientAccount);
		System.out.println("bankName: "+bankName);
		System.out.println("valueRechText: "+valueRechText);
		
		boolean respu = false;
		try{
			respu = user.validatePseAccount(idClientAccount);
		}catch (Exception e) {
			System.out.println("error en cancelPSE validatePseAccount");
			e.printStackTrace();
		}
		
		
		if(respu){
			//NTF-01
			pseWS.createProcessForPse(SessionUtil.sessionUser().getUserId(), SessionUtil.ip(),
					"La recarga fue cancelada por el usuario, con los siguientes datos, por valor $"+
					valueRechText+" para la Cuenta FacilPass No. "+idClientAccount+
					" de la entidad FACILPAGO - "+bankName,
					"Error al Crear Proceso para Recarga Manual de la cuenta "+
					idClientAccount, user.getAccountById(idClientAccount).getTbSystemUser().getUserId(),0L,
					null,null);
		}
		cancel();
		msgModal = "Se cancel� la recarga de recursos";
		showModal = true;
		this.setMessageMinrecharge(false);
		this.setShowConfiModalRecharge(false);
	}
	public void setShowConfirmationModal(boolean showConfirmationModal) {
		this.showConfirmationModal = showConfirmationModal;
	}

	public boolean isShowConfirmationModal() {
		return showConfirmationModal;
	}	

	public void method(){
		showWait=true;
	}

	public void method2(){
		showWait=false;
	}

	public void save(){
		try{			
			System.out.println("Save");
			hideModals();
			showConfirmationModal =  false;		
			Long userId=user.getSystemMasterById(usrs.getUserId());
			String msgProcessClient = "";
			String msgProcessAdmin = "";
			/** verifica si la cuenta esta en proceso de disociacion
			 * 
			 */
			if(user.getIsAccountdissociationProcess(idClientAccount)){
				msgModal = "La cuenta seleccionada est� en proceso de Disociaci�n";
				showConfirmationModal =  false;
				showModal = true;
			} else {
				boolean respu = false;
				try{
					respu = user.validatePseAccount(idClientAccount);
				}catch (Exception e) {
					System.out.println("error en save validatePseAccount...");
					e.printStackTrace();
				}
				
				if(respu){
					try {
						System.out.println("idPseEntity: "+idPseEntity);
						Long r=pseWS.processTransactionPse(idClientAccount, String.valueOf(typePer), idPseEntity, 
											bankName,valueRecharge,context,
											usrs.getUserId(), SessionUtil.ip());
						if(r<=0L){
							
							if(r!=-8L){
								String idPSEStr=(String)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("idPseT");
								FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("idPseT", null);
								System.out.println("idPSEStr: "+idPSEStr);
								Long idPse=Long.parseLong(pseWS.decodePSETransaction(idPSEStr));
								System.out.println("idPse: "+idPse);
								DecimalFormat nf = new DecimalFormat("###,###,###");
								pseWS.createProcessForPse(usrs.getUserId(), SessionUtil.ip(), "La recarga de Recursos " +
										"por valor de $"+nf.format(valueRecharge)+" para la Cuenta FacilPass No. "+idClientAccount+" fue rechazada " +
										"por la entidad FACILPAGO - "+bankName+". Para ver el comprobante de la transacci�n de clic ", 
										"Error al Crear Proceso para Recarga Manual de la cuenta "+
										idClientAccount, userId,2L,
										this.saveVoucher(userId,idPse),idPse);
							}
							
							if(r==-4L){
								msgModal = "Ha Ocurrido un Error.";
								showConfirmationModal = false;
								showModal = true;
							}else if(r==-3){
								msgModal = "Ocurri� un error de conexi�n.";
								showConfirmationModal = false;
								showModal = true;
							}else if(r==-1L){
								msgModal = "Se�or usuario se present� un error en los datos que ingres�, por favor confirme " +
										"los datos e intente nuevamente.";
								showConfirmationModal = false;
								showModalPSE = true;
							}else if(r==-2L){
								msgModal = "No hay comunicaci�n con su entidad financiera, intente m�s tarde.";
								showConfirmationModal = false;
								showModal = true;
							}else if(r==-8L){
								msgModal = "No fue posible comunicarse con su entidad, por favor intente nuevamente. ";
								showConfirmationModal = false;
								showModal = true;
							}
						}else{
							System.out.println("3: "+pseWS.getUrlRedirigirById(r));
							FacesContext.getCurrentInstance().getExternalContext().redirect(pseWS.getUrlRedirigirById(r));
						}
					} catch(Exception e){
						e.printStackTrace();
						showPseAccount = false;
						msgModal = "Ha Ocurrido un Error.";
						showConfirmationModal = false;
						showModal = true;
					}
				}else{
					try {						
					
					System.out.println("boton 1 : "+ this.isShowBotton());
					Long umbral =  WSrecharge.createUmbral(idClientAccount, 
							new BigDecimal(0), 
							new BigDecimal(valueRecharge),
							new Timestamp(System.currentTimeMillis()),
							0L,
							1L);

					ObjectRechage obj = WSrecharge.createListObj(umbral);
					InvokeSqdm rechargeWS= new InvokeSqdm();
					String data =rechargeWS.InvokeRecharge(obj);
					
					System.out.println("despues de invokeRecharge");
				    String dataServiceArray[] = data.split(";");
				    String id=dataServiceArray[0].trim();
    				// String id="300";
                    String nameFileRq=dataServiceArray[1].trim();
                    String nameFileRp=dataServiceArray[2].trim();
				    System.out.println("DATOS TRAIDOS DESDE EL WEBSERVICE"+dataServiceArray[0]);
					System.out.println("DATOS TRAIDOS DESDE EL WEBSERVICE"+dataServiceArray[1]);
				    System.out.println("DATOS TRAIDOS DESDE EL WEBSERVICE"+dataServiceArray[2]);
				    System.out.println("nameFileRq"+nameFileRq);
				    System.out.println("nameFileRp"+nameFileRp);   
				   
				    if(nameFileRq == null || nameFileRq.equals("null")){
				    	System.out.println("Dentro de validaci�n Rq null");
				    	 nameFileRq = this.xmlGenerate(obj, id, 0L);
				    }
				    if(nameFileRp==null || nameFileRp.equals("null")){	
				    	System.out.println("Dentro de validaci�n Rs null");
				    	nameFileRp = this.xmlGenerate("Transacci�n no pudo ser procesada por el banco", id, 1L);	
				    }	            	
						
					ReAccountBank rab = null;
					Query qReAccountBank = em.createQuery("Select rab From ReAccountBank rab where rab.accountId.accountId=?1 and rab.state_account_bank = 1 ");
					qReAccountBank.setParameter(1, idClientAccount);
					try{
						rab = (ReAccountBank) qReAccountBank.getSingleResult();
					} catch(NoClassDefFoundError e){
						System.out.println("No se encontro ReAccountBank para la cuenta "+idClientAccount);
					}
					String bankName = WSrecharge.getBankNameByAccount(idClientAccount);
					Long transactionId = WSrecharge.getTransactionByUmbral(umbral);
					if(Long.valueOf(id)== 0L){
						processError=0;//dato para validar si es un proceso de transaccion exitosa
						this.setShowConfirmationModal(false);
						msgModal = "Transacci�n Exitosa";
						this.setShowModal(true);
						outbox.insertMessageOutbox(userId, 
								EmailProcess.RESOURCE_ALLOCATION_SUCCESSFUL,
								idClientAccount,
								rab.getBankAccountId().getBankAccountId(), 
								transactionId, 
								null,
								null,
								null,
								rab.getBankAccountId().getProduct().getBankId(),
								null,
								SessionUtil.sessionUser().getUserId(),
								null,
								null,
								null,
								true,
								null);
						 msgProcessClient = "Asignaci�n de Recursos por valor de $"+valueRechText+" para la Cuenta FacilPass No. "+idClientAccount+" aprobada por "+bankName+".";				
						 msgProcessAdmin = "Asignaci�n de Recursos por valor de $"+valueRechText+" para la Cuenta FacilPass No. "+idClientAccount+" aprobada por "+bankName+".";				
						valueRecharge = null;
						valueRechText =	"";
						this.setIdClientAccount(-1L);	

						FacesContext context = FacesContext.getCurrentInstance();
						context.getExternalContext().getSessionMap().remove("accountSettingsBean");
					} else {						
					
						processError=1;//datro para validar si es un proceso de transaccion exitosa
						pseWS.applyRecharge(umbral,Long.valueOf(id), SessionUtil.ip(), SessionUtil.sessionUser().getUserId());
						//sendMail.sendMail(EmailType.CLIENT, user.getSystemUser(SessionUtil.sessionUser().getUserId()).getUserEmail(), EmailSubject.RECHARGE_NOTIFICATION, "Error al Recargar su cuenta FacilPass "+idClientAccount);
						outbox.insertMessageOutbox(user.getSystemUser(SessionUtil.sessionUser().getUserId()).getUserId(),
								EmailProcess.RESOURCE_ALLOCATION_REJECTED,
								idClientAccount,
								rab.getBankAccountId().getBankAccountId(), 
								transactionId, 
								null,
								null,
								null,
								rab.getBankAccountId().getProduct().getBankId(),
								null,
								SessionUtil.sessionUser().getUserId(),
								null,
								null,
								null,
								true,
								null);


						//msgProcess = "Asignaci�n de Recursos por valor de $"+valueRechText+" para la Cuenta FacilPass No. "+idClientAccount+" rechazada por "+bankName+".";

						String RESPONCE_TYPE_COD =id;//dato que viene del servicio web  	
					    Long RESPONSE_ENTITY=1L;//dato que viene del servicio web
					    if(RESPONCE_TYPE_COD=="Time Out"){				    	
					    	RESPONCE_TYPE_COD="5";
					    }
						String codeError=proceso.getResponseDescByCode(Long.valueOf(RESPONCE_TYPE_COD), RESPONSE_ENTITY);
						msgProcessAdmin="La asignaci�n de recursos de la cuenta FacilPass N�. "+idClientAccount+" present� el siguiente error: "+codeError+" - "+id;
						
//						proceso.createProcessDetail(newProcClient, ProcessTrackDetailType.MANUAL_RECHARGE,
						//		msgProcess.substring(0, msgProcess.length()-1)+" - "+id, SessionUtil.sessionUser().getUserId(), SessionUtil.ip(), 1, "Error al Crear Proceso para Recarga Manual de la cuenta "+idClientAccount,processError);
											
						msgProcessClient="La asignaci�n de recursos de la cuenta FacilPass N�. "+idClientAccount+" present� el siguiente error: "+codeError+".";							  
						//Se inserta respuesta al generar error cuando se intenta realizar una recarga
						proceso.insertRespuAval(Long.parseLong(RESPONCE_TYPE_COD),RESPONSE_ENTITY,umbral);
						msgModal = codeError;
						showConfirmationModal =  false;
						showModal = true;				
						
					}

					//creacion de proceso de cliente en Mi proceso
					TbProcessTrack idProc = proceso.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, userId);
					Long newProc = null;
					if(idProc == null){
						newProc = proceso.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, userId, SessionUtil.ip(), SessionUtil.sessionUser().getUserId());
					}else{
						newProc = idProc.getProcessTrackId();
					}
					proceso.createProcessDetail(newProc, ProcessTrackDetailType.MANUAL_RECHARGE,
							msgProcessClient, SessionUtil.sessionUser().getUserId(), SessionUtil.ip(), 1,
							"Error al Crear Proceso para Recarga Manual de la cuenta "+
							idClientAccount,processError,null,null,null,null,0L);

					//creacion de proceso de cliente
					TbProcessTrack idProcClient = proceso.searchProcess(ProcessTrackType.CLIENT, userId);
					Long newProcClient = null;
					if(idProcClient == null){
						newProcClient = proceso.createProcessTrack(ProcessTrackType.CLIENT, userId, SessionUtil.ip(), SessionUtil.sessionUser().getUserId());
					}else{
						newProcClient = idProcClient.getProcessTrackId();
					}
				
					proceso.createProcessDetail(newProcClient, ProcessTrackDetailType.MANUAL_RECHARGE,
							msgProcessAdmin, SessionUtil.sessionUser().getUserId(), SessionUtil.ip(),
							1, "Error al Crear Proceso para Recarga Manual de la cuenta "+
							idClientAccount,processError,nameFileRq,nameFileRp,null,null,0L);
					} catch (Exception e) {
						showPseAccount=false;
						msgModal = "Error en la Transacci�n.";
						showConfirmationModal =  false;
						showModal = true;
						showModalPSE = false;
					}
				}
					
			}
		}catch(NullPointerException n){
			showPseAccount=false;
			msgModal = "Error en la Transacci�n.";
			showConfirmationModal =  false;
			showModal = true;
			showModalPSE = false;
		}
	}
	public String xmlGenerate(Object o,String code,Long type){
		System.out.println("xmlGenerate");
	try{
		 SimpleDateFormat sf=new SimpleDateFormat("yyyyMMddHHmmss");
			String name=code+"_R_"+sf.format(new Date())+(type==0L?"_rq.xml":"_rs.xml");
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
			e.printStackTrace();
			System.out.println("Error creando XML de request y response");
			return null;
		}	
	}
	public void hideModals(){
		validatePseAccount();
		System.out.println("hideModals");
		setPage(1);
		this.setShowPending(false);
		this.setShowConfirmationModal(false);
		this.setShowModal(false);
		this.setShowConfirmationModalPSE(false);
		this.setShowConfirmationModalAVAL(false);
		this.setShowModalPSE(false);
		this.setMessageMinrecharge(true);
	}

	public void setValueRechText(String valueRechText) {
		this.valueRechText = valueRechText;
	}

	public String getValueRechText() {
		return valueRechText;
	}

	public void setValmin(long valmin) {
		this.valmin = valmin;
	}

	public long getValmin(Long param) {
		valMinText = parametros.getParameterValueById(param);
		if((valMinText.equals(null)) || (valMinText.equals(""))){
			valmin = 0L;
		}else{
			valmin = Long.parseLong(valMinText);
			DecimalFormat nf = new DecimalFormat("###,###,###");
			valMinText = nf.format(valmin);
		}
		System.out.println("valmin "+valmin);
		return valmin;
	}
	/*
	 * Funci�n Validar Monto Recarga
	 */
	public long getValMonMinRec(long num)
	{
		
		valMinText= processac.getParameterValueById(num);
		valmin = Long.parseLong(valMinText);
		if(valmin>0){
			return valmin;	
			
		}else{
			if(showAvalAccount || showPseAccount){
			valmin=getValmin(PREPAGO);
			}else{
				valmin=getValmin(POSTPAGO);
				
			}
			
		}
		return valmin;
		
	}

	public void setValMinText(String valMinText) {
		this.valMinText = valMinText;
	}

	
	public String getValMinText() {
		if(valMinText.equals(null)){
			valMinText = parametros.getParameterValueById(71L);
		}
		
		if((valMinText.equals(null)) || (valMinText.equals(""))){
			valMinText = "0";
		}else{
			DecimalFormat nf = new DecimalFormat("###,###,###");
			valMinText = nf.format(Long.parseLong(valMinText.replace(".","")));
		}
		return valMinText;
	}

	/**
	 * @param showBotton the showBotton to set
	 */
	public void setShowBotton(boolean showBotton) {
		this.showBotton = showBotton;
	}

	/**
	 * @return the showBotton
	 */
	public boolean isShowBotton() {
		return showBotton;
	}

	public void disabledBotton(){
		this.setShowBotton(true);
	}

	/**
	 * @param showWait the showWait to set
	 */
	public void setShowWait(boolean showWait) {
		this.showWait = showWait;
	}

	/**
	 * @return the showWait
	 */
	public boolean isShowWait() {
		return showWait;
	}

	public boolean isShowPseAccount() {
		return showPseAccount;
	}

	public void setShowPseAccount(boolean showPseAccount) {
		this.showPseAccount = showPseAccount;
	}

	public String getTypePer() {
		return typePer;
	}

	public void setTypePer(String typePer) {
		this.typePer = typePer;
	}

	public String getIdPseEntity() {
		return idPseEntity;
	}

	public void setIdPseEntity(String idPseEntity) {
		this.idPseEntity = idPseEntity;
	}

	public List<SelectItem> getPseEntities() {
		
		
		if(pseEntities == null){
			pseEntities = new ArrayList<SelectItem>();
		}else{
			pseEntities.clear();
		}
		pseEntities.add(new SelectItem("-1L", "--Seleccione Una--"));
		if(bt == 0){
			if(banks==null||banks.size()<=0){

				this.getFinancialInstitutions();
				
				bt++;
			}	
		
		}
		for(int i=0;i<banks.size();i++){
			pseEntities.add(new SelectItem(banks.get(i).getFinancialInstitutionCode(),
				banks.get(i).getFinancialInstitutionName()));
		}
		
		return pseEntities;
	}
	
	public void getFinancialInstitutions(){
		if(banks == null){
			banks=new ArrayList<BankACHData>();			
		}else{
			banks.clear();
		}
		banks=pseWS.getPSEBanks(usrs.getUserId(),idClientAccount);		
		
	}

	public void setPseEntities(List<SelectItem> pseEntities) {
		this.pseEntities = pseEntities;
	}
	/**
	 * M�todo que lista los de las recargas por cuenta
	 */
	public List<HistoricalRecharges> getRechargeList() {
		if(rechargeList == null) {
			rechargeList = new ArrayList<HistoricalRecharges>();
		} else{
			rechargeList.clear();
		}
		rechargeList = user.getHistoricalRechargesByAccount(idClientAccount);
		return rechargeList;		
	}

	public void setRechargeList(List<HistoricalRecharges> rechargeList) {
		this.rechargeList = rechargeList;
	}

	public boolean isShowRechargeList() {
		return showRechargeList;
	}

	public void setShowRechargeList(boolean showRechargeList) {
		this.showRechargeList = showRechargeList;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getPseIdStr() {
		return pseIdStr;
	}

	public void setPseIdStr(String pseIdStr) {
		this.pseIdStr = pseIdStr;
	}

	public Long getPseId() {
		return pseId;
	}

	public void setPseId(Long pseId) {
		this.pseId = pseId;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public Long getExpUmbralId() {
		return expUmbralId;
	}

	public void setExpUmbralId(Long expUmbralId) {
		this.expUmbralId = expUmbralId;
	}

	public String getTooltipCuenta() {
		tooltipCuenta=parametros.getParameterValueById(55L);
		return tooltipCuenta;
	}

	public void setTooltipCuenta(String tooltipCuenta) {
		this.tooltipCuenta = tooltipCuenta;
	}

	public String getTooltipEntidad() {
		tooltipEntidad=parametros.getParameterValueById(58L);
		return tooltipEntidad;
	}

	public void setTooltipEntidad(String tooltipEntidad) {
		this.tooltipEntidad = tooltipEntidad;
	}

	public String getTooltipValor() {
		tooltipValor=parametros.getParameterValueById(56L);
		return tooltipValor;
	}

	public void setTooltipValor(String tooltipValor) {
		this.tooltipValor = tooltipValor;
	}

	public String getTooltipPersona() {
		tooltipPersona=parametros.getParameterValueById(57L);
		return tooltipPersona;
	}

	public void setTooltipPersona(String tooltipPersona) {
		this.tooltipPersona = tooltipPersona;
	}

	public boolean isShowModalPSE() {
		return showModalPSE;
	}

	public void setShowModalPSE(boolean showModalPSE) {
		this.showModalPSE = showModalPSE;
	}

	public boolean isShowConfirmationModalPSE() {
		return showConfirmationModalPSE;
	}

	public void setShowConfirmationModalPSE(boolean showConfirmationModalPSE) {
		this.showConfirmationModalPSE = showConfirmationModalPSE;
	}
	
	public boolean isShowConfirmationModalAVAL() {
		return showConfirmationModalAVAL;
	}

	public void setShowConfirmationModalAVAL(boolean showConfirmationModalAVAL) {
		this.showConfirmationModalAVAL = showConfirmationModalAVAL;
	}

	public boolean isDisableButtons() {
		return disableButtons;
	}

	public void setDisableButtons(boolean disableButtons) {
		this.disableButtons = disableButtons;
	}
	
	public String saveVoucher(Long userId,Long pseId) {
		String COMPILE_FILE_NAME = "pseVoucher";
		try{
			System.out.println("saveVoucher");
			return this.prepareReport(userId,pseId, COMPILE_FILE_NAME);
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error RechargeClientBean.savePdf ] ");
			return null;
		}
	}
	
	public String saveVoucherAval(Long userId,Long avalId) {
		String COMPILE_FILE_NAME = "pseVoucher";
		try{
			System.out.println("saveVoucher");
			return this.prepareReportAval(userId,avalId, COMPILE_FILE_NAME);
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error RechargeClientBean.savePdf ] ");
			return null;
		}
	}
	
	public String downloadVoucher() {
		String p=null;
		String f=null;
		String fileUrl=null;
		this.setShowConfirmationModalPSE(false);
		hideModals();
		System.out.println("PSE =>"+pseId);
		System.out.println("Aval =>"+avalId);
		try{
			
			System.out.println("Muesta PDF");
			System.out.println("downloadVoucher.pseId: "+pseId);
			System.out.println("downloadVoucher.expUmbralId: "+expUmbralId);
			if(expUmbralId==-1L){
				fileUrl=pseWS.getVoucherById(pseId);
			}else{
				fileUrl=pseWS.getVoucherById(pseWS.getPseIdByUmbral(expUmbralId));
				expUmbralId=-1L;
			}
			System.out.println("fileUrl: "+fileUrl);
			if(fileUrl!=null){
				fileUrl=fileUrl.replace("/", "\\");
				p=fileUrl.substring(0,fileUrl.lastIndexOf("\\"));
				f=fileUrl.substring(fileUrl.lastIndexOf("\\")+1);
				System.out.println("fileUrl: "+fileUrl);
				System.out.println("p: "+p);
				System.out.println("f: "+f);
				java.io.File directory = new java.io.File(fileUrl);
				if(directory.exists()){
					FacesContext facesContext = FacesContext.getCurrentInstance();
					ExternalContext externalContext = facesContext.getExternalContext();
					HttpServletResponse response = (HttpServletResponse) externalContext
							.getResponse();
					File file = new File(p,
							f);
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
								+ f + "\"");
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
						System.out.println(" [ Error RechargeClientBean.downloadVoucher ]");
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
				}else{
					setMsgModal("No se encontr� el archivo solicitado.");
					setShowModalPSE(true);
				}
			}else{
				setMsgModal("No se encontr� el archivo solicitado.");
				setShowModalPSE(true);
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error RechargeClientBean.downloadVoucher 1 ] ");
			setMsgModal("Ha ocurrido un error, intente m�s tarde.");
			setShowModalPSE(true);
		}
		return null;
	}
	
	
	public String downloadVoucheraval() {
		String p=null;
		String f=null;
		String fileUrl=null;
		this.setShowConfirmationModalAVAL(false);
		hideModals();
		System.out.println("PSE =>"+pseId);
		System.out.println("Aval =>"+avalId);
		try{
			
			System.out.println("Muesta PDF");
			System.out.println("downloadVoucher.pseId: "+pseId);
			System.out.println("downloadVoucher.expUmbralId: "+expUmbralId);
			if(expUmbralId==-1L){
				fileUrl=pseWS.getVoucherById(avalId);
			}else{
				fileUrl=pseWS.getVoucherById(pseWS.getAvalIdByUmbral(expUmbralId));
				expUmbralId=-1L;
			}
			System.out.println("fileUrl: "+fileUrl);
			if(fileUrl!=null){
				fileUrl=fileUrl.replace("/", "\\");
				p=fileUrl.substring(0,fileUrl.lastIndexOf("\\"));
				f=fileUrl.substring(fileUrl.lastIndexOf("\\")+1);
				System.out.println("fileUrl: "+fileUrl);
				System.out.println("p: "+p);
				System.out.println("f: "+f);
				java.io.File directory = new java.io.File(fileUrl);
				if(directory.exists()){
					FacesContext facesContext = FacesContext.getCurrentInstance();
					ExternalContext externalContext = facesContext.getExternalContext();
					HttpServletResponse response = (HttpServletResponse) externalContext
							.getResponse();
					File file = new File(p,
							f);
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
								+ f + "\"");
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
						System.out.println(" [ Error RechargeClientBean.downloadVoucher ]");
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
				}else{
					setMsgModal("No se encontr� el archivo solicitado.");
					setShowModalPSE(true);
				}
			}else{
				setMsgModal("No se encontr� el archivo solicitado.");
				setShowModalPSE(true);
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error RechargeClientBean.downloadVoucher 1 ] ");
			setMsgModal("Ha ocurrido un error, intente m�s tarde.");
			setShowModalPSE(true);
		}
		return null;
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

	public String prepareReport(Long userId,Long pseId, String compile_file_name) {
		TbSystemUser us = null;
		try {
			System.out.println("prepareReport");
			us = em.find(TbSystemUser.class, userId);

			ExternalContext externalContext = FacesContext.getCurrentInstance()
					.getExternalContext();
			ServletContext context = (ServletContext) externalContext
					.getContext();

			ReportConfigUtil.compileReport(context, getCompileDir(),
					compile_file_name);
			File reportFile = new File(ReportConfigUtil.getJasperFilePath(
					context, getCompileDir(), compile_file_name + ".jasper"));

			String path = "";
			String systemParametersValue = parametros.getParameterValueById(28L);

			String uploadedFileName = FileUtil.trimFilePath("PSE_Voucher_"+ System.currentTimeMillis());
			System.out.println("uploadedFileName: "+uploadedFileName);
			path = systemParametersValue.trim()+ "/"+ us.getUserCode()+ "-"
					+ user.getDocumentClient(us.getUserCode(), userId,
							SessionUtil.ip(), userId);

			java.io.File directory = new java.io.File(path.toString());
			System.out.println("directorio: "+directory.toString());
			if (!directory.exists()) {
				System.out.println("Existe");
				// It returns true if directory is a directory.
				boolean result = directory.mkdir();
				if (result) {
					System.out.println("DIR created--->" + result);
				}
			}
			JasperPrint jasperPrint = ReportConfigUtil.fillReport(
					reportFile, getReportParameters(pseId),
					getJRDataSource(), getDataConnection());
			JasperExportManager.exportReportToPdfFile(jasperPrint,
					directory + "/" + uploadedFileName + ".pdf");
			// se crea el archivo PDF
			java.io.File uniqueFile = FileUtil.uniqueFile(new java.io.File(
					path), uploadedFileName);
			System.out.println("doc: "+uniqueFile.toString());
			return uniqueFile.toString()+ ".pdf";
		}catch (Exception e) {
			System.out.println("RechargeClientBean-->Exception");
			e.printStackTrace();
			return null;
		}
	}
	
	public String prepareReportAval(Long userId,Long pseId, String compile_file_name) {
		TbSystemUser us = null;
		try {
			System.out.println("prepareReport");
			us = em.find(TbSystemUser.class, userId);

			ExternalContext externalContext = FacesContext.getCurrentInstance()
					.getExternalContext();
			ServletContext context = (ServletContext) externalContext
					.getContext();

			ReportConfigUtil.compileReport(context, getCompileDir(),
					compile_file_name);
			File reportFile = new File(ReportConfigUtil.getJasperFilePath(
					context, getCompileDir(), compile_file_name + ".jasper"));

			String path = "";
			String systemParametersValue = parametros.getParameterValueById(28L);

			String uploadedFileName = FileUtil.trimFilePath("AVAL_Voucher_"+ System.currentTimeMillis());
			System.out.println("uploadedFileName: "+uploadedFileName);
			path = systemParametersValue.trim()+ "/"+ us.getUserCode()+ "-"
					+ user.getDocumentClient(us.getUserCode(), userId,
							SessionUtil.ip(), userId);

			java.io.File directory = new java.io.File(path.toString());
			System.out.println("directorio: "+directory.toString());
			if (!directory.exists()) {
				System.out.println("Existe");
				// It returns true if directory is a directory.
				boolean result = directory.mkdir();
				if (result) {
					System.out.println("DIR created--->" + result);
				}
			}
			JasperPrint jasperPrint = ReportConfigUtil.fillReport(
					reportFile, getReportParameters(pseId),
					getJRDataSource(), getDataConnection());
			JasperExportManager.exportReportToPdfFile(jasperPrint,
					directory + "/" + uploadedFileName + ".pdf");
			// se crea el archivo PDF
			java.io.File uniqueFile = FileUtil.uniqueFile(new java.io.File(
					path), uploadedFileName);
			System.out.println("doc: "+uniqueFile.toString());
			return uniqueFile.toString()+ ".pdf";
		}catch (Exception e) {
			System.out.println("RechargeClientBean-->Exception");
			e.printStackTrace();
			return null;
		}
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
		return "PSE_Voucher_" + System.currentTimeMillis();
	}

	/**
	 * 
	 * @return COMPILE_DIR
	 */
	protected String getCompileDir() {
		return COMPILE_DIR;
	}
	
	protected ConnectionData getDataConnection() {
		ConnectionData factory = new ConnectionData();
		//factory.getConnection();
		return factory;
	}

	protected Map<String, Object> getReportParameters(Long pseId) {
		Map<String, Object> reportParameters = new HashMap<String, Object>();
		reportParameters.put("idTransaccion", pseId);
		reportParameters.put("RAZONFP", parametros.getParameterValueById(64L));
		reportParameters.put("NITFP", parametros.getParameterValueById(65L));
		System.out.println("parametros comprobante: "+reportParameters.toString());
		return reportParameters;
	}

	public List<HistoricalRecharges> getPendingRechargeList() {
		return pendingRechargeList;
	}

	public void setPendingRechargeList(List<HistoricalRecharges> pendingRechargeList) {
		this.pendingRechargeList = pendingRechargeList;
	}

	public boolean isShowPending() {
		return showPending;
	}

	public void setShowPending(boolean showPending) {
		this.showPending = showPending;
	}
	
	public void validatePseAccount(){
		try {
			if(user.validatePseAccount(idClientAccount)){
				if(this.showAvalAccount){
					getValMonMinRec(2L);
				}else
				{
				   getValMonMinRec(3L);
				}
				
			}else{
				
				getValMonMinRec(1L);
			}
			
		} catch (Exception e) {			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
	}

	public void setTooltipAval(String tooltipAval) {
		this.tooltipAval = tooltipAval;
	}

	public String getTooltipAval() {
		tooltipAval=parametros.getParameterValueById(79L);
		return tooltipAval;
	}

	public void setTooltipPSE(String tooltipPSE) {		
		this.tooltipPSE = tooltipPSE;
	}

	public String getTooltipPSE() {
		tooltipPSE=parametros.getParameterValueById(80L);
		return tooltipPSE;
	}

	public void setShowConfiModalRecharge(boolean showConfiModalRecharge) {
		this.showConfiModalRecharge = showConfiModalRecharge;
	}

	public boolean isShowConfiModalRecharge() {
		return showConfiModalRecharge;
	}

	public void setMsgConfiRecharge(String msgConfiRecharge) {
		this.msgConfiRecharge = msgConfiRecharge;
	}

	public String getMsgConfiRecharge() {
		return msgConfiRecharge;
	}
	
	public void rechargeAval(){
		System.out.println("rechargeAval");		
		try{
			
			if((valueRechText.equals("")) || (valueRechText.equals(null))){
			
				msgModal = "El campo Valor a Asignar es requerido.";
				showConfirmationModal = false;
				showModalPSE = true;
			}else if(!Validation.isNumericPuntoYComaNoConsecutive(valueRechText)){
				msgModal = "El campo Valor a Asignar contiene caracteres inv�lidos, por favor verifique";
				showConfirmationModal = false;
				showModalPSE = true;
			}else {
				if((valueRechText.equals("")) || (valueRechText.equals(null))){
					msgModal = "El campo Valor a Asignar es requerido.";
					showConfirmationModal = false;
					showModalPSE = true;					
				} else{
					valueRecharge = Long.parseLong(valueRechText.replace(".", "").replace(",", ""));
				}
				//this.getValmin(POSTPAGO);
				this.getValMonMinRec(2L);
				System.out.println("valmin2 "+valmin);
				if(valueRecharge < valmin){
					msgModal = "El valor Ingresado es menor al valor m�nimo para recarga";
					showConfirmationModal = false;
					showModalPSE = true;
				} else{
					showConfiModalRecharge = true;
					showPseAccount=true;
					bankName="PORTAL DE PAGOS AVAL";
					msgConfiRecharge = "Se va a realizar una recarga por valor de $" + valueRechText + " a trav�s de las entidades del Grupo Aval";
				}
			}
					
		}catch (Exception e) {
			System.out.println("Error rechargeAval: ");
			e.printStackTrace();
		}			
	}

	public void setShowAvalAccount(boolean showAvalAccount) {
		this.showAvalAccount = showAvalAccount;
	}

	public boolean isShowAvalAccount() {
		return showAvalAccount;
	}

	public void setShowPse(boolean showPse) {
		this.showPse = showPse;
	}

	public boolean isShowPse() {
		return showPse;
	}
	
	public void rechargePse(){		
		System.out.println("rechargePse");
		this.setShowAvalAccount(false);
		this.setShowPseAccount(true);		
		this.setShowPse(false);
		this.setShowPse1(true);
		this.setShowAceptar(false);
		this.setShowCancelar(true);
		this.setNoBanks(false);
		this.getValMonMinRec(3L);
		
		System.out.println("pseEntities: " + pseEntities.size());
		
		if(pseEntities.size() == 1){
			System.out.println("Validate banks");
			msgNoButtons = "Se presentaron problemas de comunicaci�n con las entidades, intente nuevamente; si persiste el inconveniente por favor comun�quese con nuestros canales de atenci�n";
			this.setNoBanks(true);
			System.out.println("isNoBanks: " + this.isNoBanks());
		}
						
		System.out.println("isShowPseAccount: " + this.isShowPseAccount());
		System.out.println("isShowAvalAccount: " + this.isShowAvalAccount());
		System.out.println("isShowPse: " + this.isShowPse());
		
	}
	
	
	public void confiRecharge(){
		try
		{
		boolean respu = false;
		Long userId=user.getSystemMasterById(usrs.getUserId());
			try{
				respu = user.validatePseAccount(idClientAccount);
			}catch (Exception e) {
				System.out.println("error en save validatePseAccount...");
				e.printStackTrace();
			}
		
			try{

				Long res=pseWS.createTransactionAval(idClientAccount, usrs.getUserId(), SessionUtil.ip(), valueRecharge,context);
				
				if(res<=0L){
					
					if(res!=-800L){
					String idPAVALtr=(String)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("idAvalT");
										FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("idAvalT", null);
										System.out.println("idAVALStr: "+idPAVALtr);
										Long idAval=Long.parseLong(pseWS.decodePSETransaction(idPAVALtr));
										System.out.println("idPse: "+idAval);
										DecimalFormat nf = new DecimalFormat("###,###,###");
										pseWS.createProcessForPse(usrs.getUserId(), SessionUtil.ip(), "La recarga de Recursos " +
												"por valor de $"+nf.format(valueRecharge)+" para la Cuenta FacilPass No. "+idClientAccount+" fue rechazada " +
												"por  - "+bankName+". Para ver el comprobante de la transacci�n de clic ", 
												"Error al Crear Proceso para Recarga Manual de la cuenta "+
												idClientAccount, userId,2L,
												this.saveVoucher(userId,idAval),idAval);
								}
					
										if(res == -4L ){
											msgModal = "Ha Ocurrido un Error.";
											showModalPSE = true;
											this.showConfiModalRecharge = false;
											
											showModal = true;
										}else if(res==-600L){
											msgModal = "Ocurri� un error de conexi�n.";
											showConfiModalRecharge = false;
											showModalPSE = true;
										}else if(res==-100L){
											msgModal = "ha Ocurrido un Error General.";
											showConfiModalRecharge = false;
											showModalPSE = true;
										}
										else if(res==-700L){
											msgModal = "Ocurri� un error de Procesamiento.";
											showConfiModalRecharge = false;
											showModalPSE = true;
										}
										else if(res==-200L){
											msgModal = "Se�or usuario se present� un error en los datos que ingres�, por favor confirme " +
													"los datos e intente nuevamente.";
											showConfiModalRecharge = false;
											showModalPSE = true;
										}else if(res==-300L){
											msgModal = "No hay comunicaci�n con su entidad financiera, intente m�s tarde.";
											showConfiModalRecharge = false;
											showModalPSE = true;
										}else if(res==-800L || res == -8L){
											msgModal = "No fue posible comunicarse con su entidad, por favor intente nuevamente. ";
											showConfiModalRecharge = false;
											showModalPSE = true;
											}					
						}else{
								System.out.println("3: "+pseWS.getUrlRedirigirByIdAval(res));
								FacesContext.getCurrentInstance().getExternalContext().redirect(pseWS.getUrlRedirigirByIdAval(res));
							 }
				} catch(Exception e){
				e.printStackTrace();
								showPseAccount = false;
								msgModal = "Ha Ocurrido un Error.";
								showConfiModalRecharge = false;
								showModalPSE = true;
									}
			
		}catch(NullPointerException n){
			showPseAccount=false;
			msgModal = "Error en la Transacci�n.";
			showConfiModalRecharge =  false;
			showModal = true;
			showModalPSE = false;
		}
		
		
	}
	
	
	public void close(){
		System.out.println("close");
		
		this.setNoBanks(false);
		
	}

	public void setShowPse1(boolean showPse1) {
		this.showPse1 = showPse1;
	}

	public boolean isShowPse1() {
		return showPse1;
	}

	public void setMsgRe(String msgRe) {
		this.msgRe = msgRe;
	}

	public String getMsgRe() {
		return msgRe;
	}

	public void setShowAceptar(boolean showAceptar) {
		this.showAceptar = showAceptar;
	}

	public boolean isShowAceptar() {
		return showAceptar;
	}


	public void setNoButtons(boolean noButtons) {
		this.noButtons = noButtons;
	}

	public boolean isNoButtons() {
		return noButtons;
	}

	public void setMsgNoButtons(String msgNoButtons) {
		this.msgNoButtons = msgNoButtons;
	}

	public String getMsgNoButtons() {
		return msgNoButtons;
	}

	public void setShowCancelar(boolean showCancelar) {
		this.showCancelar = showCancelar;
	}

	public boolean isShowCancelar() {
		return showCancelar;
	}

	public void setHistRecharge(String histRecharge) {
		this.histRecharge = histRecharge;
	}

	public String getHistRecharge() {
		return histRecharge;
	}

	public void setNoBanks(boolean noBanks) {
		this.noBanks = noBanks;
	}

	public boolean isNoBanks() {
		return noBanks;
	}
	
}