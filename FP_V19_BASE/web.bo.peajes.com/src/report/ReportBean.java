/**
 * 
 */
package report;

import java.util.Map;

import util.ConnectionData;

import net.sf.jasperreports.engine.JRDataSource;

/**
 * @author tmolina
 *
 */
public class ReportBean extends AbstractBaseReportBean {
	
	// Attributes --- //
	
	private String compileFileName;
	
	private Map<String, Object> reportParameters;
	
	private String fileName;
	
	private JRDataSource dataSource;

	// Constructor ----//
	
	/**
	 * 
	 * @param compileFileName
	 * @param reportParameters
	 * @param fileName
	 * @param dataSource
	 */
	public ReportBean(String compileFileName,
			Map<String, Object> reportParameters, String fileName,
			JRDataSource dataSource) {
		this.compileFileName = compileFileName;
		this.reportParameters = reportParameters;
		this.fileName = fileName;
		this.dataSource = dataSource;
	}
	
	// Actions -----//
	
	/**
	 * Print the report
	 */
	public String printReport(){
		try {
			super.prepareReport();
		} catch (Exception e) {
			System.out.println(" [ Error en ReportBean.printReport ] ");
			e.printStackTrace();
		}
		return null;
	}
	
	// Overwritten methods--- //
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see report.AbstractBaseReportBean#getCompileFileName()
	 */
	@Override
	protected String getCompileFileName() {
		return this.compileFileName;
	}
	
	/* (non-Javadoc)
	 * @see report.AbstractBaseReportBean#getReportParameters()
	 */
	@Override
	protected Map<String, Object> getReportParameters() {
		return this.reportParameters;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see report.AbstractBaseReportBean#getFileName()
	 */
	@Override
	protected String getFileName() {
		return this.fileName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see report.AbstractBaseReportBean#getJRDataSource()
	 */
	@Override
	protected JRDataSource getJRDataSource() {
		return this.dataSource;
	}

	@Override
	protected ConnectionData getDataConnection() {
		return null;
	}
}