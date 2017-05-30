/**
 * 
 */
package arecatis;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 * @author tmolina
 *
 */
public class OrderDataSource implements JRDataSource{

	private Object[][] data;
	
	private int index = -1;
	
	/**
	 * Constructor
	 */
	public OrderDataSource(Object object[][]) {
		this.data = object;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.sf.jasperreports.engine.JRDataSource#getFieldValue(net.sf.jasperreports
	 * .engine.JRField)
	 */
	@Override
	public Object getFieldValue(JRField field) throws JRException {
		Object value = null;
		String fieldName = field.getName();
		
		if (fieldName.equals("RECPOINT")) {
			value = data[index][0];
		} else if (fieldName.equals("RVALUE")) {
			value = data[index][1];
		}else if (fieldName.equals("IDFAC")) {
			value = data[index][2];
		}else if (fieldName.equals("OPERTYPE")) {
			value = data[index][3];
		}
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.jasperreports.engine.JRDataSource#next()
	 */
	@Override
	public boolean next() throws JRException {
		index++;
		return (index < data.length);
	}
}
