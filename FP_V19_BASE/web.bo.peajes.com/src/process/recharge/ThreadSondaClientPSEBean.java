package process.recharge;

import java.io.File;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import oracle.toplink.essentials.exceptions.QueryException;

import jpa.TbSystemUser;
import jpa.TbUmbral;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import report.ReportConfigUtil;
import upload.FileUtil;
import util.ConnectionData;
import util.ws.PseWS;
import ejb.SystemParameters;
import ejb.User;

public class ThreadSondaClientPSEBean extends Thread implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long accountId;
	
	ServletContext context;
	
	HttpServletRequest req;
	
	ExternalContext externalContext;
	
	private PseWS pseWS;
	
	private List<TbUmbral> list;
	
	private User user;
	
	private SystemParameters parametros;
	
	private String COMPILE_FILE_NAME = " ";

	private final String COMPILE_DIR = "/jasper/design/";
	
	public ThreadSondaClientPSEBean() {
	}
	
	@Override
	public void run() {
		try{
			System.out.println("Hora de ejecución: Sonda cliente "+new Date());
			Context contex = new InitialContext();
			user = (User) contex.lookup("ejb/User");
			pseWS = (PseWS) contex.lookup("util/ws/PseWS");
			parametros = (SystemParameters) contex.lookup("ejb/SystemParameters");
			this.processingTransactions();
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println("Error de ejecución: Sonda cliente "+new Date());
		}
	}
	
	public void processingTransactions() throws SQLException{
		System.out.println("ThreadSondaClientPSE.accountId: "+accountId);
		DecimalFormat nf1 = new DecimalFormat("###,###,###");
		Long umbralId=-1L;
		Long pseId=-1L;
		Long userId=-1L;
		String nomBank="PSE";
		Long valor=0L;
		Long account=-1L;
		String ip=req.getRemoteAddr();
		System.out.println("ip: "+ip);
		list=pseWS.getPendingRechargesForClient(accountId);
		System.out.println("ThreadSondaClientPSE.size: "+list.size());
		for(int i=0;i<list.size();i++){
			umbralId=list.get(i).getUmbralId();
			System.out.println("umbralId: "+umbralId);
			pseId=pseWS.getPseIdByUmbral(umbralId);
			System.out.println("pseId: "+pseId);
			userId=list.get(i).getTbAccount().getTbSystemUser().getUserId();
			System.out.println("userId: "+userId);
			valor=pseWS.getValorById(pseId);
			System.out.println("valor: "+valor);
			nomBank=pseWS.getBancoById(pseId);
			System.out.println("nomBank: "+nomBank);
			account=list.get(i).getTbAccount().getAccountId();
			System.out.println("account: "+account);		
			if(pseWS.verifyStateUmbral(umbralId, 5L)){
				Long res=pseWS.endPseTransaction(pseId, ip, userId,1L);
				System.out.println("res: "+res);
				if(res==-1L||res==-2L||res==-6L){
					pseWS.createProcessForPse(userId, ip, "Asignación de Recursos " +
							"por valor de $"+nf1.format(valor)+" para la Cuenta FacilPass No. "+account+" rechazada " +
							"por la entidad FACILPAGO - "+nomBank+". Para ver el comprobante de la transacción de clic ", 
							"Error al Crear Proceso para Recarga Manual de la cuenta "+
							account, userId,2L,
							this.prepareReport(userId,pseId),pseId);
				}else if(res>0L){
					pseWS.createProcessForPse(userId, ip, "Asignación de Recursos " +
							"por valor de $"+nf1.format(valor)+" para la Cuenta FacilPass No. "+account+" aprobada " +
							"por la entidad FACILPAGO - "+nomBank+". Para ver el comprobante de la transacción de clic ", 
							"Error al Crear Proceso para Recarga Manual de la cuenta "+
							account, userId,2L,
							this.prepareReport(userId,pseId),pseId);
				}
			}
		}
	}
	
	public String prepareReport(Long userId,Long pseId) throws SQLException {
		TbSystemUser us = null;
		ConnectionData c = null;
		try {
			c = this.getDataConnection(); 
			String COMPILE_FILE_NAME = "pseVoucher";
			System.out.println("prepareReport");
			us = user.getSystemUser(userId);

			ReportConfigUtil.compileReport(context, getCompileDir(),
					COMPILE_FILE_NAME);
			File reportFile = new File(ReportConfigUtil.getJasperFilePath(
					context, getCompileDir(), COMPILE_FILE_NAME + ".jasper"));

			String path = "";
			String systemParametersValue = parametros.getParameterValueById(28L);

			String uploadedFileName = FileUtil.trimFilePath("PSE_Voucher_"+ System.currentTimeMillis());
			System.out.println("uploadedFileName: "+uploadedFileName);
			path = systemParametersValue.trim()+ "/"+ us.getUserCode()+ "-"
					+ user.getDocumentClient(us.getUserCode(), userId,
							req.getRemoteAddr(), userId);

			java.io.File directory = new java.io.File(path.toString());
			System.out.println("directorio: "+directory.toString());
			if (!directory.exists()) {
				System.out.println("Existe");
				// It returns true if directory is a directory.
				boolean result = directory.mkdir();
				if (result) {
					System.out.println("DIR created--->" + result);
				}
			}
			JasperPrint jasperPrint = ReportConfigUtil.fillReport(
					reportFile, getReportParameters(pseId),
					getJRDataSource(), c);
			JasperExportManager.exportReportToPdfFile(jasperPrint,
					directory + "/" + uploadedFileName + ".pdf");
			// se crea el archivo PDF
			java.io.File uniqueFile = FileUtil.uniqueFile(new java.io.File(
					path), uploadedFileName);
			System.out.println("doc: "+uniqueFile.toString());
			return uniqueFile.toString()+ ".pdf";
		}catch (Exception e) {
			System.out.println("RechargeClientBean-->Exception");
			e.printStackTrace();
			return null;
		}finally{
			if(c!=null){
				try{
				c.closeConection();
				}catch (QueryException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void setCOMPILE_FILE_NAME(String cOMPILE_FILE_NAME) {
		COMPILE_FILE_NAME = cOMPILE_FILE_NAME;
	}

	public String getCOMPILE_FILE_NAME() {
		return COMPILE_FILE_NAME;
	}

	/**
	 * 
	 * @return JRDataSource.
	 */
	protected JRDataSource getJRDataSource() {
		return null;
	}

	/**
	 * 
	 * @return File Name
	 */
	protected String getFileName() {
		return "PSE_Voucher_" + System.currentTimeMillis();
	}

	/**
	 * 
	 * @return COMPILE_DIR
	 */
	protected String getCompileDir() {
		return COMPILE_DIR;
	}
	
	protected ConnectionData getDataConnection() {
		ConnectionData factory = new ConnectionData();
		//factory.getConnection();
		return factory;
	}

	protected Map<String, Object> getReportParameters(Long pseId) {
		Map<String, Object> reportParameters = new HashMap<String, Object>();
		reportParameters.put("idTransaccion", pseId);
		reportParameters.put("RAZONFP", parametros.getParameterValueById(64L));
		reportParameters.put("NITFP", parametros.getParameterValueById(65L));
		System.out.println("parametros comprobante: "+reportParameters.toString());
		return reportParameters;
	}
	public Long getAccountId() {
		return accountId;
	}
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	public ServletContext getContext() {
		return context;
	}
	public void setContext(ServletContext context) {
		this.context = context;
	}
	public HttpServletRequest getReq() {
		return req;
	}
	public void setReq(HttpServletRequest req) {
		this.req = req;
	}
	public List<TbUmbral> getList() {
		return list;
	}
	public void setList(List<TbUmbral> list) {
		this.list = list;
	}

	public ExternalContext getExternalContext() {
		return externalContext;
	}

	public void setExternalContext(ExternalContext externalContext) {
		this.externalContext = externalContext;
	}
}