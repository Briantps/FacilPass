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
public class TraceabilityReportDataSource implements JRDataSource{
	
// Attributes ---------------------------------------------------------------------------------------
	
	private Object[][] data;
	
	private int index = -1;

	public TraceabilityReportDataSource(Object[][] object){
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
		
		if (fieldName.equals("COMPANY")) {
			value = data[index][0];
		} else if (fieldName.equals("CATEGORIA")) {
			value = data[index][1];
		}else if (fieldName.equals("PLACA")) {
			value = data[index][2];
		}else if (fieldName.equals("TRANSACTION_TIME")) {
			value = data[index][3];
		}else if (fieldName.equals("CONC")) {
			value = data[index][4];
		}else if (fieldName.equals("STATION")) {
			value = data[index][5];
		}else if (fieldName.equals("LANE")) {
			value = data[index][6];
		}else if (fieldName.equals("PREVIOUS_BALANCE")) {
			value = data[index][7];
		}else if (fieldName.equals("TRANSACTION_VALUE")) {
			value = data[index][8];}
		else if (fieldName.equals("NEW_BALANCE")) {
			value = data[index][9];}
		else if (fieldName.equals("TRANSACCION")) {
			value = data[index][10];
		}	else if (fieldName.equals("TRANSACTION_DESCRIPTION")) {
			value = data[index][11];
		}	else if (fieldName.equals("ACCOUNT")) {
			value = data[index][12];
		}
		else if (fieldName.equals("FACIAL")) {
			value = data[index][13];
		}
		else if (fieldName.equals("MANUAL_TRANSACTION")) {
			value = data[index][14];
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