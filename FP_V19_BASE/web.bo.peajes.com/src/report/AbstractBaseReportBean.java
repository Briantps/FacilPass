package report;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.j2ee.servlets.BaseHttpServlet;

import report.ReportConfigUtil;
import util.ConnectionData;

public abstract class AbstractBaseReportBean {
	
	public enum ExportOption {PDF, HTML, EXCEL, RTF, XML}
	
	private ExportOption exportOption;
	
	private final String COMPILE_DIR = "/jasper/design/";
			
	/**
	 * Constructor
	 */
	public AbstractBaseReportBean() {
		super();
		setExportOption(ExportOption.PDF);
	}

	/**
	 * 
	 * @throws JRException
	 * @throws IOException
	 */
	protected void prepareReport() throws JRException, IOException {
		
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();	
		ServletContext context = (ServletContext) externalContext.getContext();
		HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
		HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
		
		ReportConfigUtil.compileReport(context, getCompileDir(), getCompileFileName());
		
		File reportFile = new File(ReportConfigUtil.getJasperFilePath(context, getCompileDir(), getCompileFileName()+".jasper"));	
		JasperPrint jasperPrint = ReportConfigUtil.fillReport(reportFile, getReportParameters(), getJRDataSource(), getDataConnection());		
		
		if(getExportOption().equals(ExportOption.HTML)) {	
			ReportConfigUtil.exportReportAsHtml(jasperPrint, response.getWriter());
		} else {		
			request.getSession().setAttribute(BaseHttpServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jasperPrint);
		
			String extension = getExportOption().toString().toLowerCase();
			if(extension.equals("excel")) extension = "xls";
			
			response.sendRedirect(request.getContextPath() + "/peajes/report/"
					+ getExportOption() + "/" + getFileName() + "." + extension);
		}
		FacesContext.getCurrentInstance().responseComplete();
	}
	
	// Getters and Setters -------------//
	
	/**
	 * 
	 * @return
	 */
	public ExportOption getExportOption() {
		return exportOption;
	}

	/**
	 * 
	 * @param exportOption
	 */
	public void setExportOption(ExportOption exportOption) {
		this.exportOption = exportOption;
	}
	
	/**
	 * 
	 * @return COMPILE_DIR
	 */
	protected String getCompileDir() {
		return COMPILE_DIR;
	}
	
	/**
	 * 
	 * @return Compile File Name
	 */
	protected abstract String getCompileFileName();
	
	/**
	 * 
	 * @return Report Parameters.
	 */
	protected Map<String, Object> getReportParameters() {
		return new HashMap<String, Object>();
	}
	
	/**
	 * 
	 * @return JRDataSource.
	 */
	protected abstract JRDataSource getJRDataSource();
	
	/**
	 * 
	 * @return  File Name
	 */
	protected abstract String getFileName();
	
	/**
	 * 
	 * @return ConnectionData.
	 */
	protected abstract ConnectionData getDataConnection();
}