package report;

import java.io.File;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletContext;

import util.ConnectionData;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;

public class ReportConfigUtil {
	
	/*
	 * PRIVATE METHODS
	 */	
	private static void setCompileTempDir(ServletContext context, String uri) {
		System.setProperty("jasper.reports.compile.temp", context.getRealPath(uri));
	}
		
	/*
	 * PUBLIC METHODS
	 */	
	public static boolean compileReport(ServletContext context, String compileDir, String filename) throws JRException {
		String jasperFileName = context.getRealPath(compileDir + filename +".jasper");
		File jasperFile = new File(jasperFileName);
		
		if(jasperFile.exists()) {  
			return true; // jasper file already exists, do not compile again
		}
		try {
			// jasper file has not been constructed yet, so compile the xml file
			setCompileTempDir(context, compileDir);
			
			String xmlFileName = jasperFileName.substring(0, jasperFileName.indexOf(".jasper"))+".jrxml";			
			JasperCompileManager.compileReportToFile(xmlFileName);			
			return true;
		}catch(Exception e) {
			 e.printStackTrace();
			 return false;
		}
	}
	
	/**
	 * 
	 * @param reportFile
	 * @param parameters
	 * @param jrDataSource
	 * @return
	 * @throws JRException
	 */
	public static JasperPrint fillReport(File reportFile, Map<String, Object> parameters, JRDataSource jrDataSource,
			ConnectionData connection ) throws JRException {
		parameters.put("Data", reportFile.getParentFile());	
		JasperPrint jasperPrint=null;
		if (jrDataSource == null && connection != null) {
			try {
			jasperPrint = JasperFillManager.fillReport(reportFile.getPath(),
					parameters, connection.getConnection());
				connection.closeConection();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					connection.closeConection();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else {
			jasperPrint = JasperFillManager.fillReport(reportFile.getPath(),
					parameters, jrDataSource);
		}
		return jasperPrint;
	}
	
	/**
	 * 
	 * @param context
	 * @param compileDir
	 * @param jasperFile
	 * @return
	 */
	public static String getJasperFilePath(ServletContext context, String compileDir, String jasperFile) {
		return context.getRealPath(compileDir + jasperFile);
	}
	
	/**
	 * 
	 * @param exporter
	 * @param jasperPrint
	 * @param out
	 * @throws JRException
	 */
	private static void exportReport(JRAbstractExporter exporter, JasperPrint jasperPrint, PrintWriter out) throws JRException {
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_WRITER, out);		
		exporter.exportReport();
	}

	/**
	 * 
	 * @param jasperPrint
	 * @param out
	 * @throws JRException
	 */
	public static void exportReportAsHtml(JasperPrint jasperPrint, PrintWriter out) throws JRException {
		JRHtmlExporter exporter = new JRHtmlExporter();	
		exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.FALSE);
		exporter.setParameter(JRHtmlExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
		exporter.setParameter(JRHtmlExporterParameter.CHARACTER_ENCODING, "ISO-8859-9");	
		exportReport(exporter, jasperPrint, out);
	}
}