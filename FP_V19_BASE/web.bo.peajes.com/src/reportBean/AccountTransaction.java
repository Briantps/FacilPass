/**
 * 
 */
package reportBean;

import net.sf.jasperreports.engine.JRDataSource;
import report.AbstractBaseReportBean;
import util.ConnectionData;

/**
 * @author tmolina
 *
 */
public class AccountTransaction extends AbstractBaseReportBean{

	public AccountTransaction() {
		
	}

	/*
	 * (non-Javadoc)
	 * @see report.AbstractBaseReportBean#getCompileFileName()
	 */
	@Override
	protected String getCompileFileName() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see report.AbstractBaseReportBean#getFileName()
	 */
	@Override
	protected String getFileName() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see report.AbstractBaseReportBean#getJRDataSource()
	 */
	@Override
	protected JRDataSource getJRDataSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ConnectionData getDataConnection() {
		// TODO Auto-generated method stub
		return null;
	}
}
