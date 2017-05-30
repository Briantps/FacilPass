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
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.EJB;

import jpa.ReAccountDevice;
import jpa.TbAccount;
import jpa.TbDevice;
import net.sf.jasperreports.engine.JRDataSource;

import report.AbstractBaseReportBean;
import report.dataSource.TraceabilityByPlateReportDataSource;
import util.ConnectionData;
import ejb.Device;
import ejb.Vehicle;
import ejb.report.Report;

/**
 * @author tmolina
 *
 */
public class AllPlateReportBean extends AbstractBaseReportBean  implements Serializable {
	private static final long serialVersionUID = -3099986808739679757L;

	@EJB(mappedName = "ejb/Report")
	private Report report;
	
	@EJB(mappedName="ejb/Vehicle")
	private Vehicle vehicle;
	
	@EJB(mappedName = "ejb/Device")
	private Device device;
	
	// Attributes --- //
	
	private String plate;	

	private Date initDate;
	
	private Date endDate;
	
	private Object[][] objectResult;
	
	// Modal --- //
			
	private boolean showData;
		
	private boolean showMessageError;
	
	private String messageError;

	/**
	 * Constructor.
	 */
	public AllPlateReportBean() {
		initDate= new Date();
		endDate=new Date();
		this.setPlate("");
		init();
	}
	
	
	// Actions --- //
	
	/**
	 * 
	 * @return
	 */
	public String init() {
		setShowData(false);
		setShowMessageError(false);
		objectResult = null;
		messageError=" ";
		return null;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public String searchAll() {
		if (!plate.equals("")) {
			this.setPlate(this.getPlate().toUpperCase());
			if(this.isPlacaValida(plate)){
			    validate();
			}
			else{
				setMessageError("La placa debe estar compuesta por 3 letras y tres digitos ó 2 letras y 4 digitos. Verifique " +
						"la placa digitada.");
				setShowData(false);
				setShowMessageError(true);
			}
			
		} else {
			validate();
		}
		return null;
	}
	
	public void validate(){
		if(initDate != null) {
			if(endDate != null) {
				System.out.println("initDate: "+initDate);
				System.out.println("endDate: "+endDate);
				System.out.println(initDate.compareTo(endDate));
				if ((endDate.after(initDate)) || (initDate.compareTo(endDate) == 0)) {				
					objectResult = report.getTraceabilityByPlate(initDate, endDate, plate);						
					if (objectResult != null) {
						setShowData(true);
						setShowMessageError(false);
					} else {
						setMessageError("No se encontraron resultados.");
						setShowData(false);
						setShowMessageError(true);
					}
				} else {
					setMessageError("La fecha final debe ser mayor que la fecha inicial. Verifique el Rango.");
					setShowData(false);
					setShowMessageError(true);
				}
			} else {
				setMessageError("Seleccione la fecha Final.");
				setShowData(false);
				setShowMessageError(true);
			}
		} else {
			setMessageError("Seleccione la fecha Inicial.");
			setShowData(false);
			setShowMessageError(true);
		}
	}
	/**
	 * 
	 * @return
	 */
	public String printPdf() {
		try {
			 super.prepareReport();
		} catch (Exception e) {
			System.out.println("Error en AllPlateReportBean");
			e.printStackTrace();
		}
		return null;
	}
	
	// Getters and Setters --- //

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

	public void setInitDate(Date initDate) {
		this.initDate = initDate;
	}

	public Date getInitDate() {
		return initDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getEndDate() {
		return endDate;
	}
	
	// * ---------- Override Methods ------------ * //
	
	/*
	 * 
	 */
	@Override
	protected String getCompileFileName() {
		return "traceabilityReportByPlate";
	}

	/*
	 * (non-Javadoc)
	 * @see report.AbstractBaseReportBean#getFileName()
	 */
	@Override
	protected String getFileName() {
		return "Reporte_trazabilidad_placa_" + System.currentTimeMillis();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see report.AbstractBaseReportBean#getReportParameters()
	 */
	@Override
	protected Map<String, Object> getReportParameters() {
		this.setPlate(this.getPlate().toUpperCase());
		ReAccountDevice account= vehicle.getAccountByPlate(plate);	
		Map<String, Object> reportParameters = new HashMap<String, Object>();
		Timestamp fecIni = new Timestamp(initDate.getTime());
		Timestamp fecFin = new Timestamp(endDate.getTime() + 86399000);
		System.out.println("Fecha Inicial: "+fecIni);
		System.out.println("Fecha Final: "+fecFin);
		reportParameters.put("PLATE", plate);
		reportParameters.put("BEG_DATE", fecIni);
		reportParameters.put("END_DATE", fecFin);
		
		if(account!=null){
			Long dt;
			BigDecimal balance = new BigDecimal(0);
			TbAccount a=account.getTbAccount();
			if(a!=null){
				dt=a.getDistributionTypeId().getDistributionTypeId();
				if(dt==1){
					balance= a.getAccountBalance();
				}
				else{
					balance=a.getAccountBalance();
					List<TbDevice> lis = new ArrayList<TbDevice>();
					lis=device.getDevicesByAccount(a.getAccountId());
					if(lis.size()>0){
						for(TbDevice t: lis){
							System.out.println("t: " +t);
							balance= balance.add(t.getDeviceCurrentBalance());
						}	
					}
				}
			
				reportParameters.put("account", a.getAccountId());
				System.out.println("account" + a.getAccountId());
			}
			
			reportParameters.put("balance", balance);
			System.out.println("balance" + balance);
			
		}
		if(super.getExportOption().equals(ExportOption.EXCEL)){
			reportParameters.put("IS_IGNORE_PAGINATION", true);
		}
		return reportParameters;
	}

	/*
	 * (non-Javadoc)
	 * @see report.AbstractBaseReportBean#getJRDataSource()
	 */
	@Override
	protected JRDataSource getJRDataSource() {
		Object[][] dataSource = objectResult;
		TraceabilityByPlateReportDataSource ds = new TraceabilityByPlateReportDataSource(dataSource);
		return ds;
	}
	
	/**
	 * @return the plate
	 */
	public String getPlate() {
		return plate;
	}

	/**
	 * @param plate the plate to set
	 */
	public void setPlate(String plate) {
		this.plate = plate;
	}
	
	private boolean isPlacaValida(String plac) {
		Pattern p = Pattern.compile("[a-zA-Z]{2}[0-9]{4}");
		Matcher m = p.matcher(plac);

		if (m.matches()) {
			return true;
		} else {
			p = Pattern.compile("[a-zA-Z]{3}[0-9]{3}");
			m = p.matcher(plac);
			return m.matches();
		}
	}

	@Override
	protected ConnectionData getDataConnection() {
		return null;
	}
	
	public void ocult(){
		this.setShowData(false);
	}

}