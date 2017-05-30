package newReportBean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import jpa.TbAccount;
import jpa.TbSystemUser;
import jpa.TbVehicle;

import ejb.Config;
import ejb.User;
import ejb.Vehicle;

import net.sf.jasperreports.engine.JRDataSource;

import report.AbstractBaseReportBean;
import sessionVar.SessionUtil;
import util.ConnectionData;

public class ReportConsolidatedByVehicleBean extends AbstractBaseReportBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@EJB(mappedName="ejb/User")
	private User userEJB;
	
	@EJB(mappedName="ejb/Vehicle")
	private Vehicle vehicle;
	
	@EJB(mappedName="ejb/Config")
	private Config config;
	
	private Long accountId=-1L;
	
	private Date dateIni;
	
	private Date dateEnd;
	
	private List<SelectItem> accountList;
	
	private final String COMPILE_FILE_NAME="reportConsolidatedByVehicle";
	
	private ConnectionData connection=new ConnectionData();

	private TbSystemUser tsu;
	
	private boolean showData;
	
	private boolean showMessageError;
	
	private String messageError;
	
	private Long plateId=-1L;
	
	private List<SelectItem> plateList;
	
	private Long userId;
	
	private boolean showAccounts1;
	
	private boolean showAccounts2;
	
	private List<TbAccount> lis;
	
	
	public ReportConsolidatedByVehicleBean(){
		tsu= SessionUtil.sessionUser();
		plateId=-1L;
	}
	

	@PostConstruct
	public void init1(){
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
		return "ReportConsolidatedByVehicle" + System.currentTimeMillis();
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
		reportParameters.put("vehicle_id", plateId);
		String cliente= tsu.getUserCode()+ " - " + tsu.getUserNames()+" " +tsu.getUserSecondNames();
		reportParameters.put("cliente",cliente);
		reportParameters.put("user_id",userId);
		Timestamp dateIni1 = new Timestamp(dateIni.getTime());
		Timestamp dateEnd1 = new Timestamp(dateEnd.getTime() + 86399999);
		
		System.out.println("dateIni1: " + dateIni1);
		System.out.println("dateEnd1: " + dateEnd1);
		
		reportParameters.put("dateIni", dateIni1);
		reportParameters.put("dateEnd", dateEnd1);
		if(accountId==-1){
			reportParameters.put("account", "TODAS MIS CUENTAS");
		}
		else{
			if(accountId!=null){
				reportParameters.put("account", accountId.toString());
			}
		}
		
		if(plateId==-1){
			reportParameters.put("placa", "TODAS MIS PLACAS");
		}
		else{
			if(plateId!=null){
				TbVehicle tv=vehicle.getVehicle(plateId);
				String placa;
				if (tv!=null){
					placa= tv.getVehiclePlate();
					reportParameters.put("placa", placa);
				}
				else{
					reportParameters.put("placa", "");
				}
				
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
							}
							else{
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
						this.setMessageError("El reporte Recibo Consolidado Por Placa se generará a partir de " + fechaInicial);
						this.setShowMessageError(true);
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
		plateId=-1L;
	}

	public void ocult(){
		this.setShowData(false);
	}


	/**
	 * @param plateId the plateId to set
	 */
	public void setPlateId(Long plateId) {
		this.plateId = plateId;
	}


	/**
	 * @return the plateId
	 */
	public Long getPlateId() {
		return plateId;
	}


	/**
	 * @param plateList the plateList to set
	 */
	public void setPlateList(List<SelectItem> plateList) {
		this.plateList = plateList;
	}


	/**
	 * @return the plateList
	 */
	public List<SelectItem> getPlateList() {
//		if (plateList == null) {
//			plateList = new ArrayList<SelectItem>();
//		} else {
//			plateList.clear();
//		}
		plateList = new ArrayList<SelectItem>();
		if(this.getAccountId()!=null){
			if(this.getAccountId()==-1){
				plateList.add(new SelectItem("-1","TODAS MIS PLACAS"));	
			}
			else{
				plateList.add(new SelectItem("-1","TODAS MIS PLACAS"));
				for (TbVehicle v : vehicle.getVehicleByAccount(userId, accountId)) {
					plateList.add(new SelectItem(v.getVehicleId(), v.getVehiclePlate()));
				}
				if(plateList.size()==1){
					plateList.clear();
					plateList.add(new SelectItem("0","No posee vehículos asociados"));
				}
			}
		}
		
		return plateList;
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
}
