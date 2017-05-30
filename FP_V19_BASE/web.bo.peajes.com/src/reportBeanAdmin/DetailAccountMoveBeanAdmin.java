package reportBeanAdmin;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import jpa.TbAccount;
import jpa.TbCodeType;
import jpa.TbSystemUser;

import ejb.Config;
import ejb.Transaction;
import ejb.User;

import net.sf.jasperreports.engine.JRDataSource;

import report.AbstractBaseReportBean;
import util.ConnectionData;
import validator.Validation;

public class DetailAccountMoveBeanAdmin extends AbstractBaseReportBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@EJB(mappedName="ejb/User")
	private User userEJB;
	
	@EJB(mappedName="ejb/Config")
	private Config config;
	
	@EJB(mappedName="ejb/Transaction")
	private Transaction transactionEJB;
	
	private Long accountId;
	
	private Date dateIni;
	
	private Date dateEnd;
	
	private List<SelectItem> accountList;
	
	private final String COMPILE_FILE_NAME="reportDetailAccount";
	
	private ConnectionData connection=new ConnectionData();

	private TbSystemUser tsu;
	
	private boolean showData;
	
	private boolean showMessageError;
	
	private String messageError;
	
	private Long userId;
	
	private Long idClient;
	
	private String codeClient="";
	
	private Long codeType;
	
	private TbSystemUser client;
	
	private List<SelectItem> codeTypesList;
	
	private List<TbCodeType> codeTypes;
	
	private boolean showBotton;
	
	private BigDecimal previousBalance;
	
	private BigDecimal newBalance;
	
	private boolean showAccounts1;
	
	private boolean showAccounts2;
	
	private List<TbAccount> lis;
	
	
	public DetailAccountMoveBeanAdmin(){
		accountList= new ArrayList<SelectItem>();
		accountList.add(new SelectItem(0L, "Seleccione cuenta"));
		this.setShowAccounts1(false);
		this.setShowAccounts2(false);
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
		return "ReportDetailAccountMove" + System.currentTimeMillis();
	}

	@Override
	protected JRDataSource getJRDataSource() {
		return null;
	}

	/**
	 * @return the cOMPILE_FILE_NAME
	 */
	public String getCOMPILE_FILE_NAME() {
		return COMPILE_FILE_NAME;
	}

	/**
	 * @param connection the connection to set
	 */
	public void setConnection(ConnectionData connection) {
		this.connection = connection;
	}

	/**
	 * @return the connection
	 */
	public ConnectionData getConnection() {
		return connection;
	}
	
	
	/**
	 * @param accountId the accountId to set
	 */
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	/**
	 * @return the accountId
	 */
	public Long getAccountId() {
		return accountId;
	}

	/**
	 * @param dateIni the dateIni to set
	 */
	public void setDateIni(Date dateIni) {
		this.dateIni = dateIni;
	}

	/**
	 * @return the dateIni
	 */
	public Date getDateIni() {
		return dateIni;
	}

	/**
	 * @param dateEnd the dateEnd to set
	 */
	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}

	/**
	 * @return the dateEnd
	 */
	public Date getDateEnd() {
		return dateEnd;
	}

	/**
	 * @param accountList the accountList to set
	 */
	public void setAccountList(List<SelectItem> accountList) {
		this.accountList = accountList;
	}

	/**
	 * @return the accountList
	 */
	public List<SelectItem> getAccountList() {
		return accountList;
	}

	/**
	 * @param tsu the tsu to set
	 */
	public void setTsu(TbSystemUser tsu) {
		this.tsu = tsu;
	}

	/**
	 * @return the tsu
	 */
	public TbSystemUser getTsu() {
		return tsu;
	}

	/**
	 * @param showData the showData to set
	 */
	public void setShowData(boolean showData) {
		this.showData = showData;
	}

	/**
	 * @return the showData
	 */
	public boolean isShowData() {
		return showData;
	}

	/**
	 * @param showMessageError the showMessageError to set
	 */
	public void setShowMessageError(boolean showMessageError) {
		this.showMessageError = showMessageError;
	}

	/**
	 * @return the showMessageError
	 */
	public boolean isShowMessageError() {
		return showMessageError;
	}

	/**
	 * @param messageError the messageError to set
	 */
	public void setMessageError(String messageError) {
		this.messageError = messageError;
	}

	/**
	 * @return the messageError
	 */
	public String getMessageError() {
		return messageError;
	}

	public String printPdf(){
		try {
			 super.prepareReport();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected Map<String, Object> getReportParameters() {
		Map<String, Object> reportParameters = new HashMap<String, Object>();
		
	    System.out.println("accountId:" + accountId);
		reportParameters.put("account_id", accountId);
		
		String cliente = "-";
		if(idClient!=null){
			if(idClient!=-1L){
				tsu= userEJB.getSystemUser(idClient);
				if(tsu!=null){
					cliente= tsu.getUserCode()+ " - " + tsu.getUserNames()+" " +tsu.getUserSecondNames();
				}
			}
		}
		
		reportParameters.put("cliente",cliente);
		reportParameters.put("user_id",idClient);
		Timestamp dateIni1 = new Timestamp(dateIni.getTime());
		Timestamp dateEnd1 = new Timestamp(dateEnd.getTime() + 86399999);
		
		System.out.println("dateIni1: " + dateIni1);
		System.out.println("dateEnd1: " + dateEnd1);
		
		reportParameters.put("dateIni", dateIni1);
		reportParameters.put("dateEnd", dateEnd1);

		if(accountId==-1){
			reportParameters.put("account", "TODAS MIS CUENTAS");
			previousBalance=null;
			reportParameters.put("saldoInicial", previousBalance);
			
			newBalance=null;
			reportParameters.put("newBalance", newBalance);
		}
		else{
			if(accountId!=null){
				reportParameters.put("account", accountId.toString());
				previousBalance=transactionEJB.getPreviousBalance(accountId, dateIni1, dateEnd1);
				reportParameters.put("saldoInicial", previousBalance);
				
				newBalance=transactionEJB.getNewBalance(accountId, dateIni1, dateEnd1);
				reportParameters.put("newBalance", newBalance);
			}
		}
		
		if(super.getExportOption().equals(ExportOption.EXCEL)){
			reportParameters.put("IS_IGNORE_PAGINATION", true);
		}
		return reportParameters;
	}
	
	
	public void validate(){
		this.setShowData(true);
		try{
			if(codeClient!=null&&!codeClient.equals("")){
				if(accountId!=0L){
					if(dateIni!=null && dateEnd!=null){
						String fechaA=config.getParameter(38L);
						System.out.println("[fechaA]"+fechaA);
						String fechaI= fechaA!=null ? fechaA : "01/05/2014";
						System.out.println("[fechaI]"+fechaI);
						SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd/MM/yyyy");
						Date fechaBase = null;
						try {
							fechaBase = formatoDelTexto.parse(fechaI);
						} catch (ParseException ex) {
							ex.printStackTrace();
						}
						if(dateIni.getTime()>=fechaBase.getTime()){
							System.out.println("FechaMayor");
							if(dateEnd.getTime()>= dateIni.getTime()){
								if(dateEnd.getTime()<=(new Date()).getTime()){
									setShowData(true);
									setShowMessageError(false);
								}else{
									this.setShowMessageError(true);
									this.setMessageError("Error: La fecha final no debe ser mayor a la fecha actual.");
									this.setShowData(false);
								}
							}
							else{
								this.setShowMessageError(true);
								this.setMessageError("Error: La fecha final no debe ser menor a la fecha inicial.");
								this.setShowData(false);
							}
						}else{
							this.setShowMessageError(true);
							this.setMessageError("El reporte Detalle Movimiento Cuenta FacilPass se generará a partir de "+ fechaI);
							this.setShowData(false);
						}
					}
					else{
						this.setShowMessageError(true);
						this.setMessageError("Error: La fecha inicial y la fecha final no pueden estar vacías.");
						this.setShowData(false);
					}
				}
				else{
					this.setShowMessageError(true);
					this.setMessageError("Error: Presione Buscar Cuentas y seleccione una cuenta.");
					this.setShowData(false);
				}
			}else{
				this.setShowMessageError(true);
				this.setMessageError("Error: Digite el Nro. de identificación del cliente.");
				this.setShowData(false);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}	
	}

	public void init(){
		accountId=0L;
		this.setShowMessageError(false);
		this.setMessageError("");
	}

	public void ocult(){
		this.setShowData(false);
	}


	/**
	 * @param userId the userId to set
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
	 * @param idClient the idClient to set
	 */
	public void setIdClient(Long idClient) {
		this.idClient = idClient;
	}


	/**
	 * @return the idClient
	 */
	public Long getIdClient() {
		return idClient;
	}


	/**
	 * @param codeClient the codeClient to set
	 */
	public void setCodeClient(String codeClient) {
		this.codeClient = codeClient;
	}


	/**
	 * @return the codeClient
	 */
	public String getCodeClient() {
		return codeClient;
	}


	/**
	 * @param codeType the codeType to set
	 */
	public void setCodeType(Long codeType) {
		this.codeType = codeType;
	}


	/**
	 * @return the codeType
	 */
	public Long getCodeType() {
		return codeType;
	}
	
	public String changeTypeDoc() {
		setShowMessageError(false);
		showMessageError = false;
		showData = false;
		this.idClient = null;
		this.codeClient = "";
		this.setCodeClient("");
		accountId=0L;
		accountList.clear();
		accountList.add(new SelectItem(0L, "Seleccione cuenta"));
		return null;
	}


	/**
	 * @param client the client to set
	 */
	public void setClient(TbSystemUser client) {
		this.client = client;
	}


	/**
	 * @return the client
	 */
	public TbSystemUser getClient() {
		return client;
	}
	
	public String search() {
		accountList.clear();
		this.setShowData(false);
		idClient = -1L;
		if (codeClient!="") {
			if(Validation.isNumeric(codeClient)){
				client = userEJB.getUserByCode(codeClient, codeType);
				if (client != null) {
					idClient = client.getUserId();
					this.setIdClient(client.getUserId());
					System.out.println("client " + client.getUserCode());
					System.out.println("idClient " + idClient);
					
					lis=userEJB.getClientAccount(idClient);
					
					if(lis!=null){
						if(lis.size()>0 && lis.size()==1){
							System.out.println("el cliente tiene una cuenta");
							TbAccount ta= lis.get(0);
							System.out.println("ta.getAccountId(): " + ta.getAccountId());
							this.setAccountId(ta.getAccountId());
							this.setShowAccounts1(false);
							this.setShowAccounts2(true);
							
						}
						else{
							accountList.add(new SelectItem(-1, "TODAS LAS CUENTAS"));
							
							for(TbAccount t: lis){
								accountList.add(new SelectItem(t.getAccountId(), t.getAccountId().toString()));
							}
							this.setShowAccounts1(true);
							this.setShowAccounts2(false);
							setShowMessageError(false);
							this.setShowBotton(true);
						}	
					}
					else{
						System.out.println("lis es null ");
					}
				} else {
					accountList.add(new SelectItem(0L, "Seleccione cuenta"));
					System.out.println("Entre al else de client == null");
					this.setMessageError("Error: No se encontraron datos para el cliente digitado.");
					this.setShowMessageError(true);
				}
			}
			else{
				accountList.add(new SelectItem(0L, "Seleccione cuenta"));
				this.setMessageError("Error: El campo No. de Identificación + DV solo debe contener números.");
				this.setShowMessageError(true);
			}
			
		} else {
			accountList.add(new SelectItem(0L, "Seleccione cuenta"));
			System.out.println("Entre al else de codeClient == null");
			this.setMessageError("Error: Debe digitar el número de identificación del cliente.");
			this.setShowMessageError(true);
			setShowData(false);
			
		}
		System.out.println("client" + client);
		System.out.println("idClient" + idClient);
		System.out.println("codeClient" + codeClient);     
		return null;
	}

	public List<TbCodeType> getCodeTypes() {
		if(codeTypes == null){
			codeTypes = new ArrayList<TbCodeType>();
		}
		codeTypes = userEJB.getCodeTypes();       
		return codeTypes;
	}

	/**
	 * @param codeTypesList the codeTypesList to set
	 */
	public void setCodeTypesList(List<SelectItem> codeTypesList) {
		this.codeTypesList = codeTypesList;
	}


	/**
	 * @return the codeTypesList
	 */
	public List<SelectItem> getCodeTypesList() {
		if(codeTypesList==null){
			codeTypesList = new ArrayList<SelectItem>();
			for (TbCodeType type : getCodeTypes()) {
				codeTypesList.add(new SelectItem(type.getCodeTypeId(), type
						.getCodeTypeAbbreviation()));
			}
		}
		return codeTypesList;
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


	public void ocult1(){
		accountId=0L;
		accountList.clear();
		accountList.add(new SelectItem(0L, "Seleccione cuenta"));
		this.setShowData(false);
		this.setShowBotton(false);
	}


	/**
	 * @param previousBalance the previousBalance to set
	 */
	public void setPreviousBalance(BigDecimal previousBalance) {
		this.previousBalance = previousBalance;
	}


	/**
	 * @return the previousBalance
	 */
	public BigDecimal getPreviousBalance() {
		return previousBalance;
	}


	/**
	 * @param newBalance the newBalance to set
	 */
	public void setNewBalance(BigDecimal newBalance) {
		this.newBalance = newBalance;
	}


	/**
	 * @return the newBalance
	 */
	public BigDecimal getNewBalance() {
		return newBalance;
	}


	/**
	 * @param showAccounts1 the showAccounts1 to set
	 */
	public void setShowAccounts1(boolean showAccounts1) {
		this.showAccounts1 = showAccounts1;
	}


	/**
	 * @return the showAccounts1
	 */
	public boolean isShowAccounts1() {
		return showAccounts1;
	}


	/**
	 * @param showAccounts2 the showAccounts2 to set
	 */
	public void setShowAccounts2(boolean showAccounts2) {
		this.showAccounts2 = showAccounts2;
	}


	/**
	 * @return the showAccounts2
	 */
	public boolean isShowAccounts2() {
		return showAccounts2;
	}


	/**
	 * @param lis the lis to set
	 */
	public void setLis(List<TbAccount> lis) {
		this.lis = lis;
	}


	/**
	 * @return the lis
	 */
	public List<TbAccount> getLis() {
		return lis;
	}
	
}
