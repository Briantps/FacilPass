package newReportBean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import jpa.TbAccount;
import jpa.TbSystemUser;

import ejb.Config;
import ejb.Transaction;
import ejb.User;

import net.sf.jasperreports.engine.JRDataSource;

import report.AbstractBaseReportBean;
import sessionVar.SessionUtil;
import util.ConnectionData;

public class DetailAccountMoveBean extends AbstractBaseReportBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@EJB(mappedName="ejb/User")
	private User userEJB;
	
	@EJB(mappedName="ejb/Transaction")
	private Transaction transactionEJB;
	
	@EJB(mappedName="ejb/Config")
	private Config config;
	
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
	
	private BigDecimal previousBalance;
	
	private BigDecimal newBalance;
	
	private boolean showAccounts1;
	
	private boolean showAccounts2;
	
	private List<TbAccount> lis;
	
	
	public DetailAccountMoveBean(){
		tsu= SessionUtil.sessionUser();
	}
	
	@PostConstruct
	public void initMethod(){
		userId=userEJB.getSystemMasterById(tsu.getUserId());
		accountList= new ArrayList<SelectItem>();
		accountList.add(new SelectItem(-1, "TODAS MIS CUENTAS"));
		
		lis= new ArrayList<TbAccount>();
		lis=userEJB.getClientAccount(userId);
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
				System.out.println("el cliente varias cuentas");
				for(TbAccount t: lis){
					accountList.add(new SelectItem(t.getAccountId(), t.getAccountId().toString()));
				}
				this.setShowAccounts1(true);
				this.setShowAccounts2(false);
			}	
		}
		else{
			System.out.println("lis es null ");
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
		String cliente= tsu.getUserCode()+ " - " + tsu.getUserNames()+" " +tsu.getUserSecondNames();
		reportParameters.put("cliente",cliente);
		userId=userEJB.getSystemMasterById(tsu.getUserId());
		reportParameters.put("user_id",userId);
		
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
		System.out.println("previousBalance: " + previousBalance);
		System.out.println("newBalance: " + newBalance);
		System.out.println("Es Excel: "+super.getExportOption().equals(ExportOption.EXCEL));
		
		if(super.getExportOption().equals(ExportOption.EXCEL)){
			reportParameters.put("IS_IGNORE_PAGINATION", true);
		}
		/**
		 * @author ablasquez
		 * @date 21/01/14 15:51
		 * Valida que el parametro de ignorar paginacion este en true cuando el reporte es en formato excel
		 * de no ser así lo setea nuevamente
		 */
		
		/*Crea iterador para recorrer la lista de parametros */
		Iterator<Entry<String,Object>> it = reportParameters.entrySet().iterator();
		
		/*Se inicia ciclo para recorrer lista de parametros*/
		while (it.hasNext()) {
		 /*se realiza captura del objeto de acuerdo al iterado*/	
		 Entry<String, Object> e = it.next();
		 /*se valida si el parametro es IS_IGNORE_PAGINATION*/
		 if(e.getKey().equals("IS_IGNORE_PAGINATION")){
			 /*se valida si el tipo de reporte es Excel y si el valor del parametro es falso*/
			 if((super.getExportOption().equals(ExportOption.EXCEL))&&(e.getValue().equals(false))){
				 /*se setea nuevamente en true el parametro IS_IGNORE_PAGINATION*/				 
				 reportParameters.put("IS_IGNORE_PAGINATION", true);
				 System.out.println("Se cambio el parametro IS_IGNORE_PAGINATION a true");
			 }
		 }
		}
		/** fin del proceso de validacion de parametro ignorar paginacion */
		
		return reportParameters;
	}
	
	
	public void validate(){
		this.setShowData(true);
		try{
			
			String fechaInicial= config.getParameter(38L);
			String fechaInicialDef= fechaInicial!=null ? fechaInicial : "01/05/2014";
			
			Date fechaIniParametrizada;
			SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
			fechaIniParametrizada=sdf.parse(fechaInicialDef);
			System.out.println("Fecha Inicial: " + fechaInicialDef + " fecha converitda: " +fechaIniParametrizada);
			
			
 				if(dateIni!=null && dateEnd!=null){
 				  if(dateIni.getTime() >= fechaIniParametrizada.getTime()){
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
 				  }
 				  else{
						this.setShowMessageError(true);
						this.setMessageError("El reporte Detalle Movimiento Cuenta FacilPass se generará a partir de " + fechaInicial);
						this.setShowData(false);
				  }
				}
				else{
					this.setShowMessageError(true);
					this.setMessageError("Error: La fecha inicial y la fecha final no pueden estar vacías.");
					this.setShowData(false);
				} 


			
		}catch(Exception ex){
			ex.printStackTrace();
		}	
	}

	public void init(){
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
	 * @param previousBalance the previousBalance to set
	 */
	public void setNewBalance(BigDecimal newBalance) {
		this.newBalance = newBalance;
	}

	/**
	 * @return the previousBalance
	 */
	public BigDecimal getNewBalance() {
		return newBalance;
	}
	
}
