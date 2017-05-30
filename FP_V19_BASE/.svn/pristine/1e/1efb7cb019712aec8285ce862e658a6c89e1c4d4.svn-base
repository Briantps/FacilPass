/**
 * 
 */
package report;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporter;

/**
 * @author tmolina
 *
 */
public class CustomXLSServlet extends AbstractXlsServlet{

		private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
		
		/*
		 * (non-Javadoc)
		 * @see report.AbstractXlsServlet#getXlsExporter()
		 */
		protected JRXlsAbstractExporter getXlsExporter(){
			return new  JExcelApiExporter() ;
		}
}