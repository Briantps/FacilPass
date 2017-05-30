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
public class HistoRechargeDataSource implements JRDataSource{

	private Object[][] data;
	
	private int index = -1;
	
	/**
	 * Constructor
	 */
	public HistoRechargeDataSource(Object object[][]) {
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
		
		if (fieldName.equals("NO_ORDER")) {
			value = data[index][0];
		} else if (fieldName.equals("NIT_CLIENT")) {
			value = data[index][1];
		}else if (fieldName.equals("NAME_CLIENT")) {
			value = data[index][2];
		}else if (fieldName.equals("VALUE")) {
			value = data[index][3];
		}else if (fieldName.equals("ID_CARD")) {
			value = data[index][4];
		}else if (fieldName.equals("FACIAL")) {
			value = data[index][5];
		}else if (fieldName.equals("DATE")) {
			value = data[index][6];
		}else if (fieldName.equals("CONF")) {
			value = data[index][7];
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
