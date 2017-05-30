/**
 * 
 */
package reportBean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import jpa.TbAccount;
import jpa.TbDevice;

import net.sf.jasperreports.engine.JRDataSource;
import report.AbstractBaseReportBean;
import security.UserLogged;
import util.ConnectionData;
import ejb.Device;
import ejb.User;
import ejb.report.Report;

/**
 * @author tmolina
 * 
 */
public class TraceabilityReportBean extends AbstractBaseReportBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4175514641165575666L;

	private final String COMPILE_FILE_NAME = "traceabilityReport";

	@EJB(mappedName = "ejb/Report")
	private Report report;
	
	@EJB(mappedName = "ejb/Device")
	private Device device;
	
	@EJB(mappedName="ejb/User")
	private User userEJB;

	// Attributes --------------------- //

	List<String> list;

	private String tagId = "";

	private Date begDate;

	private Date endDate;
	
	private String pathImg="";

	private boolean modal;

	private String modalMessage;
	
	private List<SelectItem> deviceList;
	
	private boolean showBandreport = false;
	
	private boolean noDevice = false;
	
	private boolean showNoDevice = true;
	
	private UserLogged usrs;

	/**
	 * CONTRUCTOR
	 */
	public TraceabilityReportBean() {
		usrs = (UserLogged) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
		begDate = new Date();
		endDate = new Date();
		showBandreport = false;
		noDevice = false;
		showNoDevice =true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see report.AbstractBaseReportBean#getCompileFileName()
	 */
	@Override
	protected String getCompileFileName() {
		return COMPILE_FILE_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see report.AbstractBaseReportBean#getFileName()
	 */
	@Override
	protected String getFileName() {
		return "Reporte_Trazabilidad_"+System.currentTimeMillis();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see report.AbstractBaseReportBean#getJRDataSource()
	 */
	@Override
	protected JRDataSource getJRDataSource() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see report.AbstractBaseReportBean#getReportParameters()
	 */
	@Override
	protected Map<String, Object> getReportParameters() {
		Long userId=userEJB.getSystemMasterById(usrs.getUserId());

		TbAccount account= device.getAccountByDeviceCode(tagId);
		Map<String, Object> reportParameters = new HashMap<String, Object>();		
		reportParameters.put("ID_TAG", tagId);		
		Timestamp fechaInicial = new Timestamp(begDate.getTime());
		Timestamp fechaFinal = new Timestamp(endDate.getTime() + 86399000);
		reportParameters.put("BEG_DATE", fechaInicial);
		reportParameters.put("END_DATE", fechaFinal);
		reportParameters.put("pathImg", pathImg);	
		reportParameters.put("account", account!=null?account.getAccountId():null);
		reportParameters.put("balance", account!=null?account.getAccountBalance():null);
		reportParameters.put("USER_ID", userId);
		Long dt;
		if(account!=null){
			BigDecimal balance = new BigDecimal(0);
			dt=account.getDistributionTypeId().getDistributionTypeId();
			if(dt==1){
				balance= account.getAccountBalance();
			}
			else{
				balance=account.getAccountBalance();
				List<TbDevice> lis = new ArrayList<TbDevice>();
				lis=device.getDevicesByAccount(account.getAccountId());
				if(lis.size()>0){
					for(TbDevice t: lis){
						System.out.println("t: " +t);
						balance= balance.add(t.getDeviceCurrentBalance());
					}	
				}
			}
			reportParameters.put("account", account!=null?account.getAccountId():null);
			System.out.println("account" + account!=null?account.getAccountId():null);
			
			reportParameters.put("balance", balance);
			System.out.println("balance" + balance);
		}
	
		if(super.getExportOption().equals(ExportOption.EXCEL)){
			reportParameters.put("IS_IGNORE_PAGINATION", true);
		}
		return reportParameters;
	}

	// Actions

	/**
	 * 
	 * @return
	 */
	public String execute() {
		try {
			 super.prepareReport();
		} catch (Exception e) {
			System.out.println("Error en TraceabilityReportBean");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param suggest
	 * @return
	 */
	public List<String> autocomplete(Object suggest) {
		String pref = (String) suggest;
		ArrayList<String> result = new ArrayList<String>();
		if (list == null) {
			list = report.getDeviceCodes();
		}
		Iterator<String> iterator = list.iterator();
		while (iterator.hasNext()) {
			String elem = ((String) iterator.next());
			if ((elem != null && elem.toLowerCase().indexOf(pref.toLowerCase()) == 0)
					|| "".equals(pref)) {
				result.add(elem);
			}
		}
		return result;
	}

	// Getters and Setters ----- //
	
	/**
	 * @param tagId
	 *            the tagId to set
	 */
	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	/**
	 * @return the tagId
	 */
	public String getTagId() {
		return tagId;
	}

	/**
	 * @param begDate
	 *            the begDate to set
	 */
	public void setBegDate(Date begDate) {
		this.begDate = begDate;
	}

	/**
	 * @return the begDate
	 */
	public Date getBegDate() {
		return begDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param modal
	 *            the modal to set
	 */
	public void setModal(boolean modal) {
		this.modal = modal;
	}

	/**
	 * @return the modal
	 */
	public boolean isModal() {
		return modal;
	}

	/**
	 * @param modalMessage
	 *            the modalMessage to set
	 */
	public void setModalMessage(String modalMessage) {
		this.modalMessage = modalMessage;
	}

	/**
	 * @return the modalMessage
	 */
	public String getModalMessage() {
		return modalMessage;
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
	  Long userId=userEJB.getSystemMasterById(usrs.getUserId());
		if (deviceList == null) {
			deviceList = new ArrayList<SelectItem>();
		} else {
			deviceList.clear();
		}
		deviceList.add(new SelectItem("-1","TODOS MIS DISPOSITIVOS"));
		String tipo, facial;
		for (TbDevice d : device.getDevicesByClient(userId)) {
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
			deviceList.add(new SelectItem(d.getDeviceCode(), "Nro Facial: " + facial
					));
		}
		
		if(deviceList.size()<=0){
			noDevice = true;
		}
		return deviceList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see report.AbstractBaseReportBean#getDataConnection()
	 */
	@Override
	protected ConnectionData getDataConnection() {
		return (new ConnectionData());
	}
	
	public void existRowforClient(){
		Long userId=userEJB.getSystemMasterById(usrs.getUserId());
		//crea la validacion para saber si existen o no datos para generar el reporte del lado del cliente.
		if(tagId!=null){
			if(begDate!=null && endDate!=null){
				if(begDate==endDate){
					showBandreport = report.getExitDataForReportTrasability(tagId,begDate,endDate,userId);
					if(showBandreport == false){
						modalMessage = "No existe información para el reporte";
						modal = true;
					}
				}else{
					if(endDate.before(begDate)){
						modalMessage = "La Fecha Final debe ser Mayor o Igual a la Fecha Inicial";
						modal = true;
						showBandreport = false;
					}else{
						showBandreport = report.getExitDataForReportTrasability(tagId,begDate,endDate,userId);
						if(showBandreport == false){
							modalMessage = "No existe información para el reporte";
							modal = true;
						}
					}
				}
			}
			else {
				modalMessage = "La Fecha Inicial y la Fecha Final no deben estar vacías.";
				modal = true;
			}
			
		}else {
			modalMessage = "No se ha seleccionado ningún dispositivo";
			modal = true;
		}
	}

	public void setShowBandreport(boolean showBandreport) {
		this.showBandreport = showBandreport;
	}

	public boolean isShowBandreport() {
		return showBandreport;
	}
	
	public void hidemodal(){
		modal = false;
		modalMessage = "";
		showBandreport = false;
		noDevice = false;
		showNoDevice =false;
	}
	
	public void excPDF(){		
		try {
			 super.setExportOption(ExportOption.PDF);
			 super.prepareReport();
		} catch (Exception e) {
			System.out.println("Error en TraceabilityReportBean.excPDF");
			e.printStackTrace();
		}
	}
	
	public void excXLS(){		
		try {
			 super.setExportOption(ExportOption.EXCEL);			 
			 super.prepareReport();
		} catch (Exception e) {
			System.out.println("Error en TraceabilityReportBean.excXLS");
			e.printStackTrace();
		}
	}

	public void setNoDevice(boolean noDevice) {
		this.noDevice = noDevice;
	}

	public boolean isNoDevice() {
		return noDevice;
	}

	public void setShowNoDevice(boolean showNoDevice) {
		this.showNoDevice = showNoDevice;
	}

	public boolean isShowNoDevice() {
		return showNoDevice;
	}
	
	public void ocult(){
		this.setShowBandreport(false);
	}
}