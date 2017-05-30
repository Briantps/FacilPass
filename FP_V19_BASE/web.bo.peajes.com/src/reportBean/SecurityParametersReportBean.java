package reportBean;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import report.AbstractBaseReportBean;
import util.ConnectionData;

/**
 * 
 * @author psanchez
 */
public class SecurityParametersReportBean extends AbstractBaseReportBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private ConnectionData connection= new ConnectionData();
	private final String COMPILE_FILE_NAME = "securityParametersReport";
	
	public SecurityParametersReportBean(){

	}
	
	public String execute(){
		try {
			 super.prepareReport();
		} catch (Exception e) {
			System.out.println("Error SecurityParametersReportBean");
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
		reportParameters.put("logo","FACILPASS");
		reportParameters.put("title","PARAMETROS DE SEGURIDAD");
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
		return "Report_Security_Parameters" + System.currentTimeMillis();
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
}
