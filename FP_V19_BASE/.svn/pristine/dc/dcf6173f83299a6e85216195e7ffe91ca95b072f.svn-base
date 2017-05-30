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
import javax.faces.model.SelectItem;

import jpa.TbAccount;
import jpa.TbDevice;

import net.sf.jasperreports.engine.JRDataSource;
import report.AbstractBaseReportBean;
import report.dataSource.TraceabilityReportDataSource;
import sessionVar.SessionUtil;
import util.ConnectionData;
import ejb.Device;
import ejb.report.Report;

/**
 * @author tmolina
 * 
 */
public class TraceabilityReportBeanAdmin extends AbstractBaseReportBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final String COMPILE_FILE_NAME = "traceabilityReport";

	@EJB(mappedName = "ejb/Report")
	private Report report;
	
	@EJB(mappedName = "ejb/Device")
	private Device device;

	// Attributes --------------------- //

	List<String> list;

	private String tagId = "";

	private Date begDate;

	private Date endDate;
	
	private String pathImg="";

	private boolean modal;

	private String modalMessage;
	
	private List<SelectItem> deviceList;
	
	private String message;
	
	private boolean showData;
	
	private boolean showMessage;
	
	private Object[][] objectResult;
	
	private ConnectionData connection= new ConnectionData();

	/**
	 * CONTRUCTOR
	 */
	public TraceabilityReportBeanAdmin() {
		begDate = new Date();
		endDate = new Date();
	}
	
	public void execute1() {
		try {
			System.out.println("Entre aqui");
			 super.prepareReport();
		} catch (Exception e) {
			System.out.println("Error en TraceabilityReportBean");
			e.printStackTrace();
		}
	}

	
	public void validate(){
		this.setShowData(false);
		System.out.println("tagId" + tagId);
		try{
//             if(tagId!=""){
 				if(begDate!=null && endDate!=null){
					if(endDate.getTime()>= begDate.getTime()){
						objectResult = report.getTraceabilityByDeviceCode(begDate, endDate, tagId);	
						if (objectResult != null) {
							setShowData(true);
							setShowMessage(false);
						} else {
							setMessage("No se encontraron resultados.");
							setShowData(false);
							setShowMessage(true);
						}
					}
					else{
						this.setShowMessage(true);
						this.setMessage("Error: La fecha final no debe ser menor a la fecha inicial.");
						this.setShowData(false);
					}
					
				}
				else{
					this.setShowMessage(true);
					this.setMessage("Error: La fecha inicial y la fecha final no pueden estar vacías.");
					this.setShowData(false);
				} 
//             }
//
//			else{
//				this.setShowMessage(true);
//				this.setMessage("Error: El campo tag no puede estar vacio.");
//				this.setShowData(false);
//			} 

			
		}catch(Exception ex){
			ex.printStackTrace();
		}	
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
		Object[][] dataSource = objectResult;
		TraceabilityReportDataSource ds = new TraceabilityReportDataSource(dataSource);
		return ds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see report.AbstractBaseReportBean#getReportParameters()
	 */
	@Override
	protected Map<String, Object> getReportParameters() {
		
		TbAccount account= device.getAccountByDeviceCode(tagId);
		Map<String, Object> reportParameters = new HashMap<String, Object>();		
		reportParameters.put("ID_TAG", tagId);		
		//endDate.setTime(endDate.getTime() + 86399000L);
		Timestamp fechaInicial = new Timestamp(begDate.getTime());
		Timestamp fechaFinal = new Timestamp(endDate.getTime() + 86399000);
		System.out.println("Fecha Inicial: "+begDate);
		System.out.println("Fecha Final: "+endDate);
		/*reportParameters.put("BEG_DATE", begDate);
		reportParameters.put("END_DATE", endDate);*/
		reportParameters.put("BEG_DATE", fechaInicial);
		reportParameters.put("END_DATE", fechaFinal);
		reportParameters.put("pathImg", pathImg);
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

	
	public void hideModal(){
		this.setMessage("");
		this.setShowMessage(false);
		this.setTagId("");
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
		if (deviceList == null) {
			deviceList = new ArrayList<SelectItem>();
		} else {
			deviceList.clear();
		}
		String tipo, facial;
		for (TbDevice d : device.getDevicesByClient(SessionUtil.sessionUser()
				.getUserId())) {
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
			deviceList.add(new SelectItem(d.getDeviceCode(), tipo + " - ID Interno: " 
					+ d.getDeviceCode() 
					+ " -  " + facial
					));
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
		return null;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
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
	 * @param showMessage the showMessage to set
	 */
	public void setShowMessage(boolean showMessage) {
		this.showMessage = showMessage;
	}

	/**
	 * @return the showMessage
	 */
	public boolean isShowMessage() {
		return showMessage;
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
	
	public void ocult(){
		this.setShowData(false);
	}

	/**
	 * @param objectResult the objectResult to set
	 */
	public void setObjectResult(Object[][] objectResult) {
		this.objectResult = objectResult;
	}

	/**
	 * @return the objectResult
	 */
	public Object[][] getObjectResult() {
		return objectResult;
	}
}