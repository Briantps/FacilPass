package report.dataSource;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class ReportClearingDS implements JRDataSource{
// Attributes ---------------------------------------------------------------------------------------
	
	private Object[][] data;
	
	private int index = -1;

	public ReportClearingDS(Object[][] object){
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
		
		if (fieldName.equals("TIPO")) {
			value = data[index][0];
		} else if (fieldName.equals("NOMBRE")) {
			value = data[index][1];
		}else if (fieldName.equals("TOTAL_RECAUDO")) {
			value = data[index][2];
		}else if (fieldName.equals("TOTAL_RECARGA")) {
			value = data[index][3];
		}else if (fieldName.equals("VALOR_PAGAR")) {
			value = data[index][4];
		}else if (fieldName.equals("VALOR_COBRAR")) {
			value = data[index][5];
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
