/**
 * 
 */
package report.dataSource;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 * @author tmolina
 *
 */
public class UserStatementReportDataSource implements JRDataSource{
	
// Attributes ---------------------------------------------------------------------------------------
	
	private Object[][] data;
	
	private int index = -1;

	public UserStatementReportDataSource(Object[][] object){
		super();
		this.data = object;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.jasperreports.engine.JRDataSource#getFieldValue(net.sf.jasperreports.engine.JRField)
	 */
	@Override
	public Object getFieldValue(JRField field) throws JRException {
		Object value = null;
		String fieldName = field.getName();
		
		if (fieldName.equals("DEVICE_CODE")) {
			value = data[index][0];
		} else if (fieldName.equals("PLATE")) {
			value = data[index][1];
		}else if (fieldName.equals("CATEGORY")) {
			value = data[index][2];
		}else if (fieldName.equals("DATE")) {
			value = data[index][3];
		}else if (fieldName.equals("STATION")) {
			value = data[index][4];
		}else if (fieldName.equals("LANE")) {
			value = data[index][5];
		}else if (fieldName.equals("PREVIOUS_BALANCE")) {
			value = data[index][6];
		}else if (fieldName.equals("OPERATION_VALUE")) {
			value = data[index][7];}
		else if (fieldName.equals("CURRENT_BALANCE")) {
			value = data[index][8];}
		else if (fieldName.equals("OPERATION")) {
			value = data[index][9];
		}	
		return value;
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.jasperreports.engine.JRDataSource#next()
	 */
	@Override
	public boolean next() throws JRException {
		index++;
		return (index < data.length);
	}
}