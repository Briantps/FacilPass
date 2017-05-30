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
public class ReportAllClientsDataSource implements JRDataSource{

	// Attributes ---------------------------------------------------------------------------------------
	
	private Object[][] data;
	
	private int index = -1;
	
	public ReportAllClientsDataSource(Object[][] object) {
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
		
		if (fieldName.equals("ID_CLIENT")) {
			value = data[index][0];
		} else if (fieldName.equals("NAME_CLIENT")) {
			value = data[index][1];
		}else if (fieldName.equals("ADDRESS")) {
			value = data[index][2];
		}else if (fieldName.equals("PHONE")) {
			value = data[index][3];
		}else if (fieldName.equals("FAX")) {
			value = data[index][4];
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