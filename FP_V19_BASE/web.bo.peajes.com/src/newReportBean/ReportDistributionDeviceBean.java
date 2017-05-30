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
import jpa.TbDevice;
import jpa.TbSystemUser;

import ejb.Config;
import ejb.Device;
import ejb.User;

import net.sf.jasperreports.engine.JRDataSource;

import report.AbstractBaseReportBean;
import sessionVar.SessionUtil;
import util.ConnectionData;

public class ReportDistributionDeviceBean extends AbstractBaseReportBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@EJB(mappedName="ejb/User")
	private User userEJB;

	@EJB(mappedName = "ejb/Device")
	private Device device;
	
	@EJB(mappedName="ejb/Config")
	private Config config;
	
	private Long accountId=-1L;
	
	private Date dateIni;
	
	private Date dateEnd;
	
	private List<SelectItem> accountList;
	
	private final String COMPILE_FILE_NAME="reportDistributionDevice";
	
	private ConnectionData connection=new ConnectionData();

	private TbSystemUser tsu;
	
	private boolean showData;
	
	private boolean showMessageError;
	
	private String messageError;
	
	private Long deviceId=-1L;
	
	private List<SelectItem> deviceList;
	
	private Long userId;
		
	private boolean showAccounts1;
	
	private boolean showAccounts2;
	
	private List<TbAccount> lis;
	
	
	public ReportDistributionDeviceBean(){
		tsu= SessionUtil.sessionUser();
		deviceId=-1L;
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
		return "ReportDistributionDevice" + System.currentTimeMillis();
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
		reportParameters.put("accountId", accountId);
		reportParameters.put("deviceId", deviceId);
		String cliente= tsu.getUserCode()+ " - " + tsu.getUserNames()+" " +tsu.getUserSecondNames();
		reportParameters.put("cliente",cliente);
		reportParameters.put("userId",userId);
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
					this.setShowMessageError(true);
					this.setMessageError("El reporte Distribución Montos por Dispositivos se generará a partir de " + fechaInicial);
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
		deviceId=-1L;
	}

	public void ocult(){
		this.setShowData(false);
	}


	/**
	 * @param deviceId the deviceId to set
	 */
	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}


	/**
	 * @return the deviceId
	 */
	public Long getDeviceId() {
		return deviceId;
	}


	/**
	 * @param deviceList the deviceList to set
	 */
	public void setDeviceList(List<SelectItem> deviceList) {
		this.deviceList = deviceList;
	}


	/**
	 * @return the deviceList
	 */
	public List<SelectItem> getDeviceList() {
		deviceList= new ArrayList<SelectItem>();
		if(accountId==-1){
			deviceList.add(new SelectItem(-1L, "TODOS MIS DISPOSITIVOS"));
		}
		else{
			String tipo, facial;
			deviceList.add(new SelectItem(-1L, "TODOS MIS DISPOSITIVOS"));
			
			for (TbDevice d : device.getDevicesByAccount(accountId)) {
				if(d.getTbDeviceType() == null){
					tipo = "Tipo Sin Definir ";
				} else {
					tipo = d.getTbDeviceType().getDeviceTypeName();
				}
				if(d.getDeviceFacialId() == null) {
					facial = "Facial Sin Definir";
				} else {
					facial = d.getDeviceFacialId();
				}
				deviceList.add(new SelectItem(d.getDeviceId(), facial));
			}
			if(deviceList.size()==1){
				deviceList.clear();
				deviceList.add(new SelectItem("0","No posee dispositivos asociados"));
			}
		}

		return deviceList;
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
