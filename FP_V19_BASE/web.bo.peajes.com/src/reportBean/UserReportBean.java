/**
 * 
 */
package reportBean;



import javax.ejb.EJB;

import net.sf.jasperreports.engine.JRDataSource;
import report.AbstractBaseReportBean;
import report.dataSource.UserReportDataSource;
import util.ConnectionData;
import ejb.report.Report;

/**
 * @author tmolina
 * 
 */
public class UserReportBean extends AbstractBaseReportBean {

	private final String COMPILE_FILE_NAME = "userReport";

	@EJB(mappedName = "ejb/Report")
	private Report report;

	public UserReportBean() {
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
	 * @see report.AbstractBaseReportBean#getJRDataSource()
	 */
	@Override
	protected JRDataSource getJRDataSource() {
		Object[][] object = report.getUserData();
		UserReportDataSource dataSource = new UserReportDataSource(object);
		return dataSource;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see report.AbstractBaseReportBean#getFileName()
	 */
	@Override
	protected String getFileName() {
		return "Reporte_Usuarios";
	}

	/**
	 * 
	 * @return
	 */
	public String execute() {
		try {
			super.prepareReport();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected ConnectionData getDataConnection() {
		return null;
	}
}