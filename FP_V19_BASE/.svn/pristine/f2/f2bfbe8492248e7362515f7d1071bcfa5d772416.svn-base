/**
 * 
 */
package reportBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;

import jpa.TbSystemUser;
import jpa.TbUserData;

import net.sf.jasperreports.engine.JRDataSource;
import report.AbstractBaseReportBean;
import report.dataSource.UserStatementReportDataSource;
import util.ConnectionData;
import ejb.User;
import ejb.report.Report;

/**
 * @author tmolina
 * 
 */
public class UserStatementReportBean extends AbstractBaseReportBean {

	private final String COMPILE_FILE_NAME = "userStatement";

	@EJB(mappedName = "ejb/Report")
	private Report report;
	
	@EJB(mappedName = "ejb/User")
	private User userEjb;

	// Attributes
	// -----------------------------------------------------------------------

	List<String> list;

	private String userCode= "";

	private Date begDate;

	private Date endDate;

	/**
	 * CONTRUCTOR
	 */
	public UserStatementReportBean() {
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
		return "Extracto_de_Cuenta";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see report.AbstractBaseReportBean#getJRDataSource()
	 */
	@Override
	protected JRDataSource getJRDataSource() {
		Object[][] object = report.getUserStatementAccount(begDate, endDate, userCode);
		if(object != null) {
			UserStatementReportDataSource dataSource = new UserStatementReportDataSource(object);
			return dataSource;
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see report.AbstractBaseReportBean#getReportParameters()
	 */
	@Override
	protected Map<String, Object> getReportParameters() {
		Map<String, Object> reportParameters = new HashMap<String, Object>();
		
		TbSystemUser user = report.getTbSystemUserByCode(userCode);
		if(user!=null){
			if(user.getTbCodeType().getCodeTypeId()==3){
				reportParameters.put("USER_NAME", user.getUserNames());
			}else{
				reportParameters.put("USER_NAME", user.getUserNames()+user.getUserSecondNames());
			}
			
			List<TbUserData> userData = new ArrayList<TbUserData>();
			userData = userEjb.getClientData(user.getUserId());
			
			if(userData.size() > 0) {
				TbUserData ud = userData.get(0);
				reportParameters.put("USER_ADDRESS", ud.getUserDataAddress());
				reportParameters.put("USER_PHONE", ud.getUserDataPhone()+"");
			} else {
				reportParameters.put("USER_ADDRESS", null);
				reportParameters.put("USER_PHONE", null);
			}
		}		
		
		reportParameters.put("USER_CODE", userCode);
		reportParameters.put("BEG_DATE", begDate);
		reportParameters.put("END_DATE", endDate);
		return reportParameters;
	}

	// Actions

	/**
	 * 
	 * @return
	 */
	public String execute() {
		try {
			 super.prepareReport();
		} catch (Exception e) {
			System.out.println("Error en UserStatementReportBean");
			e.printStackTrace();
		}
		return null;
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
			list = report.getUserCodes();
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

	// Getters and Setters
	// ------------------------------------------------------------------------

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
	 * @param userCode the userCode to set
	 */
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	/**
	 * @return the userCode
	 */
	public String getUserCode() {
		return userCode;
	}

	@Override
	protected ConnectionData getDataConnection() {
		return null;
	}
}