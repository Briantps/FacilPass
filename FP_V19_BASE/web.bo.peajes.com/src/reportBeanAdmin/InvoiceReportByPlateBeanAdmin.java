package reportBeanAdmin;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import ejb.Config;
import ejb.User;

import jpa.TbAccount;
import jpa.TbCodeType;
import jpa.TbSystemUser;

import net.sf.jasperreports.engine.JRDataSource;

import report.AbstractBaseReportBean;
import util.ConnectionData;
import validator.Validation;

public class InvoiceReportByPlateBeanAdmin extends AbstractBaseReportBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@EJB(mappedName="ejb/User")
	private User userEJB;
	
	@EJB(mappedName="ejb/Config")
	private Config config;
	
	private Long accountId;
	
	private Timestamp dateIni;
	
	private Timestamp dateEnd;
	
	private String messageError;
	
	private boolean showData;
	
	private boolean showMessageError;
	
	private List<SelectItem> accountList;
	
	private ConnectionData connection= new ConnectionData();
	
	private final String COMPILE_FILE_NAME = "invoiceReportByPlate";
	
	private TbSystemUser tsu;
	
	private String month;
	
	private String year;
	
	private List<SelectItem> monthList;
	
	private List<SelectItem> yearList;
	
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
	
	
	public InvoiceReportByPlateBeanAdmin(){
		accountList= new ArrayList<SelectItem>();
		accountList.add(new SelectItem(0L, "Seleccione cuenta"));
		this.setShowAccounts1(false);
		this.setShowAccounts2(false);
	}
	
	/**
	 * @param dateIni the dateIni to set
	 */
	public void setDateIni(Timestamp dateIni) {
		this.dateIni = dateIni;
	}

	/**
	 * @return the dateIni
	 */
	public Timestamp getDateIni() {
		return dateIni;
	}

	/**
	 * @param dateEnd the dateEnd to set
	 */
	public void setDateEnd(Timestamp dateEnd) {
		this.dateEnd = dateEnd;
	}

	/**
	 * @return the dateEnd
	 */
	public Timestamp getDateEnd() {
		return dateEnd;
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
	
	public String printPdf(){
		try {
			 super.prepareReport();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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

	public String init(){
		accountId=0L;
		this.setShowMessageError(false);
		this.setMessageError("");
		return null;
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
		return "InvoiceReportByPlate" +System.currentTimeMillis();
	}

	@Override
	protected JRDataSource getJRDataSource() {
		return null;
	}
	
	public void ocult(){
		this.setShowData(false);
	}
	
	public void ocult1(){
		accountId=0L;
		accountList.clear();
		accountList.add(new SelectItem(0L, "Seleccione cuenta"));
		this.setShowData(false);
		this.setShowBotton(false);
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
	
	@Override
	protected Map<String, Object> getReportParameters() {
		Map<String, Object> reportParameters = new HashMap<String, Object>();
		
	    System.out.println("accountId:" + accountId);
		reportParameters.put("accountId", accountId);
		reportParameters.put("mes", obtenerMes() +" - "+ year);
		reportParameters.put("USERID",idClient);
		
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

		String fec1= year+ "-" + month  + "-01"+ " 00:00:00";	
		
		dateIni= Timestamp.valueOf(fec1);
		
		Calendar c= Calendar.getInstance();
		c.setTime(dateIni);
		c.add(Calendar.MONTH,1);
		dateEnd=new Timestamp(c.getTimeInMillis());
		
		System.out.println("dateIni: " + dateIni);
		System.out.println("dateEnd: " + dateEnd);
		reportParameters.put("BEG_DATE", dateIni);
		reportParameters.put("END_DATE", dateEnd);
		
		if(super.getExportOption().equals(ExportOption.EXCEL)){
			reportParameters.put("IS_IGNORE_PAGINATION", true);
		}
		return reportParameters;
	}
	
	
	public void validate(){
		this.setShowData(false);
		this.setShowMessageError(false);
		try{
			this.setMessageError("Error: Al generar Recibo Consolidado de Peajes");
			String fechaInicial= config.getParameter(38L);
			String dia="1";
			String mes="05";
			String anio="2014";
			try{
				if(fechaInicial!=null && fechaInicial != ""){
					dia=fechaInicial.substring(0,2);
					mes= fechaInicial.substring(3,5);
					anio= fechaInicial.substring(6);
				}
			}
			catch(Exception ex){ 
				ex.printStackTrace();
			}
			
			if(codeClient!=""){
				if(accountId!=0){
					
					System.out.println("Información de tb_system_parameter dia:" +dia +" mes:"+ mes  + " anio"+  anio);
					System.out.println("Información seleccionado por el cliente month:" + month + " year: " + year);
					int flag=0;
				    if(Integer.parseInt(year)>Integer.parseInt(anio)){
				    	flag=0;
				    	System.out.println("año mayor");
				    }
				    else{
				    	if(Integer.parseInt(year)==Integer.parseInt(anio)){
				    		System.out.println("año igual");
				    		if((Integer.parseInt(month) > Integer.parseInt(mes))){
				    			flag=0;
						    }
				    		else if((Integer.parseInt(month) == Integer.parseInt(mes))){
				    			
				    			if(Integer.parseInt(dia)==1){
				    				System.out.println("mes igual con dia 1");
							    	flag=0;
				    			}
				    			else{
				    				System.out.println("mes igual con dia diferente a 1");
							        flag=1;	
							        this.setMessageError("El reporte Recibo Consolidado de Peajes se generará a partir de "+ obtenerMes2(mes) +" - "+ anio);
				    			}
				    			
				    		}
				    		else{
				    			flag=1;
				    			System.out.println("mes menor");
				    			this.setMessageError("El reporte Recibo Consolidado de Peajes se generará a partir de "+ obtenerMes2(mes) +" - "+ anio);
				    		}
						 
				         }
				    	else{
				    		flag=1;	
				    		System.out.println("año menor");
				    		this.setMessageError("El reporte Recibo Consolidado de Peajes se generará a partir de "+ obtenerMes2(mes) +" - "+ anio);
				    	}
				        
				    }
				    if(flag==0){
				    	String fec1= year+ "-" + month  + "-01"+ " 00:00:00";	
						
						dateIni= Timestamp.valueOf(fec1);
						
						Calendar c= Calendar.getInstance();
						c.setTime(dateIni);
						c.add(Calendar.MONTH,1);
						dateEnd=new Timestamp(c.getTimeInMillis());
						if(dateIni.getTime()<=(new Date()).getTime()){
							setShowMessageError(false);
							setShowData(true);
						}
						else{
							this.setMessageError("Error: La fecha de generación no debe ser mayor a la fecha actual.");
							this.setShowData(false);
					    	this.setShowMessageError(true);
						}
				    	
				    }
				    else{
				    	this.setShowData(false);
				    	this.setShowMessageError(true);
				    }

					
				}else{
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
	 * @param month the month to set
	 */
	public void setMonth(String month) {
		this.month = month;
	}

	/**
	 * @return the month
	 */
	public String getMonth() {
		return month;
	}

	/**
	 * @param year the year to set
	 */
	public void setYear(String year) {
		this.year = year;
	}

	/**
	 * @return the year
	 */
	public String getYear() {
		return year;
	}

	/**
	 * @param monthList the monthList to set
	 */
	public void setMonthList(List<SelectItem> monthList) {
		this.monthList = monthList;
	}

	/**
	 * @return the monthList
	 */
	public List<SelectItem> getMonthList() {
		monthList= new ArrayList<SelectItem>();
		monthList.add(new SelectItem("01", "ENERO"));
		monthList.add(new SelectItem("02", "FEBRERO"));
		monthList.add(new SelectItem("03", "MARZO"));
		monthList.add(new SelectItem("04", "ABRIL"));
		monthList.add(new SelectItem("05", "MAYO"));
		monthList.add(new SelectItem("06", "JUNIO"));
		monthList.add(new SelectItem("07", "JULIO"));
		monthList.add(new SelectItem("08", "AGOSTO"));
		monthList.add(new SelectItem("09", "SEPTIEMBRE"));
		monthList.add(new SelectItem("10", "OCTUBRE"));
		monthList.add(new SelectItem("11", "NOVIEMBRE"));
		monthList.add(new SelectItem("12", "DICIEMBRE"));
		return monthList;
	}

	/**
	 * @param yearList the yearList to set
	 */
	public void setYearList(List<SelectItem> yearList) {
		this.yearList = yearList;
	}

	/**
	 * @return the yearList
	 */
	public List<SelectItem> getYearList() {
		yearList= new ArrayList<SelectItem>();
		yearList.add(new SelectItem("2013", "2013"));
		yearList.add(new SelectItem("2014", "2014"));
		yearList.add(new SelectItem("2015", "2015"));
		yearList.add(new SelectItem("2016", "2016"));
		yearList.add(new SelectItem("2017", "2017"));
		yearList.add(new SelectItem("2018", "2018"));
		return yearList;
	}
	
	public String obtenerMes(){
		String m = "";
		System.out.println("mes" + month);
		if(month!=null){
		    if(month.equals("01")){
		    	m="ENERO";
		    }
		    else if(month.equals("02")){
		    	m="FEBRERO";
		    }
		    else if(month.equals("03")){
		    	m="MARZO";
		    }
		    else if(month.equals("04")){
		    	m="ABRIL";
		    }
		    else if(month.equals("05")){
		    	m="MAYO";
		    }
		    else if(month.equals("06")){
		    	m="JUNIO";
		    }
		    else if(month.equals("07")){
		    	m="JULIO";
		    }
		    else if(month.equals("08")){
		    	m="AGOSTO";
		    }
		    else if(month.equals("09")){
		    	m="SEPTIEMBRE";
		    }
		    else if(month.equals("10")){
		    	m="OCTUBRE";
		    }
		    else if(month.equals("11")){
		    	m="NOVIEMBRE";
		    }
		    else{
		    	m="DICIEMBRE";
		    }
		    	
		}
		return m;
	}
	
	public String obtenerMes2(String mes){
		String m = "";
		System.out.println("mes" + mes);
		if(mes!=null){
		    if(mes.equals("01")){
		    	m="ENERO";
		    }
		    else if(mes.equals("02")){
		    	m="FEBRERO";
		    }
		    else if(mes.equals("03")){
		    	m="MARZO";
		    }
		    else if(mes.equals("04")){
		    	m="ABRIL";
		    }
		    else if(mes.equals("05")){
		    	m="MAYO";
		    }
		    else if(mes.equals("06")){
		    	m="JUNIO";
		    }
		    else if(mes.equals("07")){
		    	m="JULIO";
		    }
		    else if(mes.equals("08")){
		    	m="AGOSTO";
		    }
		    else if(mes.equals("09")){
		    	m="SEPTIEMBRE";
		    }
		    else if(mes.equals("10")){
		    	m="OCTUBRE";
		    }
		    else if(mes.equals("11")){
		    	m="NOVIEMBRE";
		    }
		    else{
		    	m="DICIEMBRE";
		    }
		    	
		}
		return m;
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
	 * @param codeTypes the codeTypes to set
	 */
	public void setCodeTypes(List<TbCodeType> codeTypes) {
		this.codeTypes = codeTypes;
	}

	/**
	 * @return the codeTypes
	 */
	public List<TbCodeType> getCodeTypes() {
		if(codeTypes == null){
			codeTypes = new ArrayList<TbCodeType>();
		}
		codeTypes = userEJB.getCodeTypes();
		return codeTypes;
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
							System.out.println("el cliente varias cuentas");
							
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
					this.setShowMessageError(true);
					this.setMessageError("Error: No se encontraron datos para el cliente digitado.");
				}	
			}else{
				accountList.add(new SelectItem(0L, "Seleccione cuenta"));
				this.setMessageError("Error: El campo No. de Identificación + DV solo debe contener números.");
				this.setShowMessageError(true);
			}
			
			
		} else {
			accountList.add(new SelectItem(0L, "Seleccione cuenta"));
			System.out.println("Entre al else de codeClient == null");
			this.setShowMessageError(true);
			this.setMessageError("Error: Debe digitar el número de identificación del cliente.");
			setShowData(false);
			
		}
		System.out.println("client" + client);
		System.out.println("idClient" + idClient);
		System.out.println("codeClient" + codeClient);
		return null;
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
