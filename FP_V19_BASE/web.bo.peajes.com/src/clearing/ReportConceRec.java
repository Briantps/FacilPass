/**
 * 
 */
package clearing;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import net.sf.jasperreports.engine.JRDataSource;
import report.AbstractBaseReportBean;
import util.ConnectionData;
import ejb.report.Report;

/**
 * @author ablasquez
 * 
 */
public class ReportConceRec extends AbstractBaseReportBean {

	private final String COMPILE_FILE_NAME = "clearingConc";

	@SuppressWarnings("unused")
	@EJB(mappedName = "ejb/Report")
	private Report report;
	
	// Attributes --------------------- //

	private Date begDate;

	private Date endDate;
	
	private boolean modal;

	private String modalMessage;
	
	/**
	 * CONTRUCTOR
	 */
	public ReportConceRec() {
		begDate = new Date();
		endDate = new Date();
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
		return "Reporte_Recaudo_Concesion_"+System.currentTimeMillis();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see report.AbstractBaseReportBean#getJRDataSource()
	 */
	@Override
	protected JRDataSource getJRDataSource() {
		/*Object[][] object = report.getClearing(begDate, endDate);
		if(object != null) {
			ReportClearingDS dataSource = new ReportClearingDS(object);
			return dataSource;
		} else {
			return null;
		}*/
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see report.AbstractBaseReportBean#getReportParameters()
	 */
	@Override
	protected Map<String, Object> getReportParameters() {
		Map<String, Object> reportParameters = new HashMap<String, Object>();
		reportParameters.put("TITTLE", "RECAUDO POR CONCESION");
		reportParameters.put("fecIni", begDate);
		reportParameters.put("fecFin", endDate);	
		reportParameters.put("tipo", 2L);
		return reportParameters;
	}

	// Actions

	/**
	 * 
	 * @return
	 */
	public String execute() {
		try {
			if(begDate==null){
				modalMessage = "Falta Fecha Inicial";
				modal = true;
			}else if(endDate==null){
				modalMessage = "Falta Fecha Final";
				modal = true;
			}else if(endDate.before(begDate)){
				modalMessage = "La Fecha Final no puede ser menor a la Fecha Inicial";
				modal = true;
			}else{
				super.prepareReport();
			}
		} catch (Exception e) {			
			modalMessage = "Error al solicitar reporte";
			modal = true;
			e.printStackTrace();
		}
		return null;
	}

	// Getters and Setters ----- //
	
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see report.AbstractBaseReportBean#getDataConnection()
	 */
	@Override
	protected ConnectionData getDataConnection() {
		return (new ConnectionData());
	}
	
	public void hide(){
		modal = false;
	}
}