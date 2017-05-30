package reportBean;


import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import report.AbstractBaseReportBean;
import util.ConnectionData;

public class TraceabilityVehicleBean extends AbstractBaseReportBean implements Serializable{


	private static final long serialVersionUID = 1L;
	
	private Date initDate1;
	
	private Date endDate1;
	
	private ConnectionData connection= new ConnectionData();
	
	private final String COMPILE_FILE_NAME = "traceabilityVehicles";
	
	private boolean showMessage;
	
	private String message;
	
	private boolean showReport;
	
	public TraceabilityVehicleBean(){
		setInitDate1(new Date());
		setEndDate1(new Date());
	}
	
	/**
	 * 
	 */
	public String printPDF(){
		try {
			if(initDate1!=null && endDate1!=null){
				if(initDate1.getTime()<=endDate1.getTime()){
					super.prepareReport();
				}
				else{
					this.setMessage("Error: La fecha final no debe ser menor a la fecha inicial");
					this.setShowMessage(true);
					return null;
				}	
			}
			else{
				this.setMessage("Error: La fecha inicial y la fecha final no pueden estar vacías.");
				this.setShowMessage(true);
				return null;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void validate(){
		if(initDate1!=null && endDate1!=null){
			if(initDate1.getTime()<=endDate1.getTime()){
				this.setShowReport(true);
			}
			else{
				this.setShowReport(false);
				this.setMessage("Error: La fecha final no debe ser menor a la fecha inicial");
				this.setShowMessage(true);
			}	
		}
		else{
			this.setShowReport(false);
			this.setMessage("Error: La fecha inicial y la fecha final no pueden estar vacías.");
			this.setShowMessage(true);
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
		return "Report_Traceability_vehicles" + System.currentTimeMillis();
	}

	@Override
	protected JRDataSource getJRDataSource() {
		return null;
	}

	public void setInitDate1(Date initDate1) {
		this.initDate1 = initDate1;
	}

	public Date getInitDate1() {
		return initDate1;
	}

	public void setEndDate1(Date endDate1) {
		this.endDate1 = endDate1;
	}

	public Date getEndDate1() {
		return endDate1;
	}

	public void setConnection(ConnectionData connection) {
		this.connection = connection;
	}

	public ConnectionData getConnection() {
		return connection;
	}

	public String getCOMPILE_FILE_NAME() {
		return COMPILE_FILE_NAME;
	}
	
	/* (non-Javadoc)
	 * @see report.AbstractBaseReportBean#getReportParameters()
	 */
	@Override
	protected Map<String, Object> getReportParameters() {
		Map<String, Object> reportParameters = new HashMap<String, Object>();	
	
		reportParameters.put("initDate", initDate1);
		reportParameters.put("endDate", endDate1);
		return reportParameters;
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
	
	public void hideModal(){
		this.setShowMessage(false);
		this.setMessage("");
	}

	/**
	 * @param showReport the showReport to set
	 */
	public void setShowReport(boolean showReport) {
		this.showReport = showReport;
	}

	/**
	 * @return the showReport
	 */
	public boolean isShowReport() {
		return showReport;
	}
	
	public void ocult(){
		this.setShowReport(false);
	}


}
