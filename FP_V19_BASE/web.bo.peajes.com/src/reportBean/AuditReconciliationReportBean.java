package reportBean;


import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import report.AbstractBaseReportBean;
import util.ConnectionData;

/**
 * 
 * @author psanchez
 */
public class AuditReconciliationReportBean extends AbstractBaseReportBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
    private Date initDate;
    private Date endDate;
	private Date fechaActual;
	private ConnectionData connection= new ConnectionData();
	private final String COMPILE_FILE_NAME = "auditReconciliationReport";
	
	// Modal --- //	
	private boolean showData;
	private boolean showModal;
	private String messageModal;
	
	// Acciones ---//
	public AuditReconciliationReportBean(){
		initDate= new Date();
		endDate=new Date();
		fechaActual  = new Date();
	}
	
	public String generar(){
		try {
			 super.prepareReport();
		} catch (Exception e) {
			System.out.println("Error auditReconciliationReport");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Clear modal panel data.
	 */
	public String hideModal(){
		setMessageModal("");
		setShowModal(false);
		return null;
	}
	
	
	/**
	 * Método encargado de validar los filtros (fecha inicial, fecha final) y generar el reporte auditReconciliationReport.
	 * @author psanchez
	 */
	public String execute(){
	    try{
			if(initDate != null && endDate != null) {
				 if(endDate.getTime()>= initDate.getTime()) {
		    		  if(initDate.after(fechaActual) || endDate.after(fechaActual)){
		    			  this.setMessageModal("Error: La fecha inicial y/o la fecha final no debe ser mayor a la actual.");
						  this.setShowData(false);
						  this.setShowModal(true);
		    	      }else{
		    			  this.setShowData(true);
		    			  this.setShowModal(false);
				      }
	    	    }else{
	    	    	this.setMessageModal("Error: La Fecha Inicial debe ser menor a la Fecha Final. Verifique.");
	    	    	this.setShowData(false);
	    	    	this.setShowModal(true);
			     }
			  }	else{
				this.setMessageModal("Error: La fecha inicial y la fecha final no pueden estar vacías.");
				this.setShowData(false);
				this.setShowModal(true);
			  }
			} catch (Exception e) {
				System.out.println("Error AuditReconciliationReportBean");
				e.printStackTrace();
			}
		 return null;
	 }

	@Override
	protected Map<String, Object> getReportParameters() {
		Map<String, Object> reportParameters = new HashMap<String, Object>();
		Timestamp fechaInicial = new Timestamp(initDate.getTime());
		Timestamp fechaFinal = new Timestamp(endDate.getTime() + 86399000);
			reportParameters.put("logo","FACILPASS");
			reportParameters.put("title","Auditoría Reconciliación RFID");
			reportParameters.put("initDate", fechaInicial);
			reportParameters.put("endDate", fechaFinal);
		return reportParameters;
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
		return "Report_Audit_Reconciliation_RFID" + System.currentTimeMillis();
	}

	@Override
	protected JRDataSource getJRDataSource() {
		return null;
	}
	
	// Getters and Setters --- //

	/**
	 * @param showModal the showModal to set
	 */
	public void setShowModal(boolean showModal) {
		this.showModal = showModal;
	}

	/**
	 * @return the showModal
	 */
	public boolean isShowModal() {
		return showModal;
	}
	
	/**
	 * @param messageModal the messageModal to set
	 */
	public void setMessageModal(String messageModal) {
		this.messageModal = messageModal;
	}

	/**
	 * @return the messageModal
	 */
	public String getMessageModal() {
		return messageModal;
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

	public void setConnection(ConnectionData connection) {
		this.connection = connection;
	}

	public ConnectionData getConnection() {
		return connection;
	}

	public String getCOMPILE_FILE_NAME() {
		return COMPILE_FILE_NAME;
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
	
	public void ocult(){
		this.setShowData(false);
	}

	public void setFechaActual(Date fechaActual) {
		this.fechaActual = fechaActual;
	}

	public Date getFechaActual() {
		return fechaActual;
	}
}
