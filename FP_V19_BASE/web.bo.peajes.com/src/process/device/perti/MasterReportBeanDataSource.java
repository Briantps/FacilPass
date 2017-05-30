/**
 * 
 */
package process.device.perti;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 * @author tmolina
 *
 */
public class MasterReportBeanDataSource implements JRDataSource {

	private Object[][] data;
	
	private int index = -1;
	
	/**
	 * Constructor
	 */
	public MasterReportBeanDataSource(Object object[][]) {
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
		
		if (fieldName.equals("IDFACIAL")) {
			value = data[index][0];
		} else if (fieldName.equals("PLATE")) {
			value = data[index][1];
		} else if (fieldName.equals("CHASSIS")) {
			value = data[index][2];
		} else if (fieldName.equals("COD_INT")) {
			value = data[index][3];
		} else if (fieldName.equals("TYPE_VEH")) {
			value = data[index][4];
		} else if (fieldName.equals("CAT_CONS")) {
			value = data[index][5];
		} else if (fieldName.equals("CAT_INV")) {
			value = data[index][6];
		} else if (fieldName.equals("RAD_NUM")) {
			value = data[index][7];
		} else if (fieldName.equals("DATE_NUM")) {
			value = data[index][8];
		} else if (fieldName.equals("CLIENT")) {
			value = data[index][9];
		} else if (fieldName.equals("DEP")) {
			value = data[index][10];
		} else if (fieldName.equals("REGIONAL")) {
			value = data[index][11];
		} else if (fieldName.equals("COLOR")) {
			value = data[index][12];
		} else if (fieldName.equals("BRAND")) {
			value = data[index][13];
		} else if (fieldName.equals("SEND_DOC")) {
			value = data[index][14];
		} else if (fieldName.equals("DATE_SEND_DOC")) {
			value = data[index][15];
		} else if (fieldName.equals("PAY")) {
			value = data[index][16];
		} else if (fieldName.equals("STATION")) {
			value = data[index][17];
		} else if (fieldName.equals("US_CREATION")) {
			value = data[index][18];
		} else if (fieldName.equals("DATE_CREATION")) {
			value = data[index][19];
		} else if (fieldName.equals("OBS")) {
			value = data[index][20];
		} else if (fieldName.equals("EMAIL")) {
			value = data[index][21];
		} else if (fieldName.equals("STATE")) {
			value = data[index][22];
		} else if (fieldName.equals("CUS_USER")) {
			value = data[index][23];
		} else if (fieldName.equals("CUS_DATE")) {
			value = data[index][24];
		} else if (fieldName.equals("PHONE")) {
			value = data[index][25];
		} else if (fieldName.equals("CARD_ID")) {
			value = data[index][26];
		} else if (fieldName.equals("REPLAC_CAUSE")) {
			value = data[index][27];
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
