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
public class EntryWarehouseByNumberDataSource implements JRDataSource{
	
	private Object[][] data;
	
	private int index = -1;
	
	public EntryWarehouseByNumberDataSource(Object[][] object) {
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
		
		if (fieldName.equals("DEVICE_ID")) {
			value = data[index][0];
		} else if (fieldName.equals("DATE_ENTRY")) {
			value = data[index][1];
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