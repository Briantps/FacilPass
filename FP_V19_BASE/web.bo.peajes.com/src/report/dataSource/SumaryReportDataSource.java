/**
 * 
 */
package report.dataSource;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;


public class SumaryReportDataSource implements JRDataSource{
	
// Attributes ---------------------------------------------------------------------------------------
	
	private Object[][] data;
	
	private int index = -1;

	public SumaryReportDataSource(Object[][] object){
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
		
		if (fieldName.equals("FECHA")) {
			value = data[index][0];
		}else if (fieldName.equals("PREVIOUSBALANCE")) {
			value = data[index][1];
		}else if (fieldName.equals("DEBITOS")) {
			value = data[index][2];
		}else if (fieldName.equals("CREDITOS")) {
			value = data[index][3];
		}else if (fieldName.equals("NEWBALANCE")) {
			value = data[index][4];
		}else if (fieldName.equals("TRANSACCIONES")) {
			value = data[index][5];
		}else if (fieldName.equals("CONT")) {
			value = data[index][6];
		}else if (fieldName.equals("DETALLE")) {
			value = data[index][7];
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