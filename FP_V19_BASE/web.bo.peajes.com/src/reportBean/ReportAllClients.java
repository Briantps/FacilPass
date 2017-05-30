package reportBean;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import report.AbstractBaseReportBean;
import util.ConnectionData;

/**
 * 
 * @author ablasquez
 */
public class ReportAllClients extends AbstractBaseReportBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private ConnectionData connection= new ConnectionData();
	private final String COMPILE_FILE_NAME = "AllClientsReport";
	
	private String exportOption1;
	
	public ReportAllClients(){

	}
	
	public String printPdf(){
		System.out.println("eo" + super.getExportOption());
		try {
			 super.prepareReport();
		} catch (Exception e) {
			System.out.println("Error ReportAllClients");
			e.printStackTrace();
		}
		return null;
	}
	
	public String printXls(){
		try {
			super.setExportOption(ExportOption.EXCEL);
			System.out.println("export option: " + super.getExportOption());
			super.prepareReport();
		} catch (Exception e) {
			System.out.println("Error ReportAllClients.Excel");
			e.printStackTrace();
		}
		return null;
	}
	
	
	/* (non-Javadoc)
	 * @see report.AbstractBaseReportBean#getReportParameters()
	 */
	@Override
	protected Map<String, Object> getReportParameters() {
		Map<String, Object> reportParameters = new HashMap<String, Object>();	
		if(super.getExportOption().equals(ExportOption.EXCEL)){
			reportParameters.put("IS_IGNORE_PAGINATION", true);
		}
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
		return "Reporte_Clientes_" + System.currentTimeMillis();
	}

	@Override
	protected JRDataSource getJRDataSource() {
		return null;
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
	 * @param exportOption1 the exportOption1 to set
	 */
	public void setExportOption1(String exportOption1) {
		this.exportOption1 = exportOption1;
	}

	/**
	 * @return the exportOption1
	 */
	public String getExportOption1() {
		return exportOption1;
	}
}
