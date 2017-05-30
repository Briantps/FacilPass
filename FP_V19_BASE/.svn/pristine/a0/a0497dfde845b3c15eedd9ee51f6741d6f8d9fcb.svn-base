package newReportBean;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;

import ejb.Config;
import ejb.Transaction;
import ejb.User;
import ejb.Vehicle;

import report.AbstractBaseReportBean;
import report.AbstractBaseReportBean.ExportOption;
import sessionVar.SessionUtil;
import util.ConnectionData;

import jpa.TbAccount;
import jpa.TbSystemUser;
import jpa.TbVehicle;

public class FileTransactionBean extends AbstractBaseReportBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@EJB(mappedName="ejb/User")
	private User userEJB;
	
	@EJB(mappedName="ejb/Vehicle")
	private Vehicle vehicle;
	
	@EJB(mappedName="ejb/Transaction")
	private Transaction transactionEJB;
	
	@EJB(mappedName="ejb/Config")
	private Config config;

	private Long accountId=-1L;
	
	private Date dateIni;
	
	private Date dateEnd;
	
	private List<SelectItem> accountList;
	
    private TbSystemUser tsu;
	
	private boolean showData;
	
	private boolean showMessageError;
	
	private String messageError;
	
	private Long plateId=-1L;
	
	private List<SelectItem> plateList;
	
	private boolean inProcess;
	
	private String messageProcess;
	
	private Long userId;
	
    private String nameFile;
    
    private String option;
    
	private String codeClient="";
	
	private boolean showPanelDownload; 
	
	private boolean showMessage;
	
	private boolean showAccounts1;
	
	private boolean showAccounts2;
	
	private List<TbAccount> lis;
	
    private final String COMPILE_FILE_NAME="reporteGeneral";
	
	private ConnectionData connection=new ConnectionData(); 
	
	
	public FileTransactionBean(){
		setTsu(SessionUtil.sessionUser());
		setPlateId(-1L);
	}
	
	@PostConstruct
	public void init1(){
		userId=userEJB.getSystemMasterById(tsu.getUserId());
		accountList= new ArrayList<SelectItem>();
		accountList.add(new SelectItem(-1, "TODAS MIS CUENTAS"));
		
		lis= new ArrayList<TbAccount>();
		lis=userEJB.getClientAccount(userId);
		if(lis!=null){
			if(lis.size()>0 && lis.size()==1){
				System.out.println("el cliente tiene una cuenta");
				TbAccount ta= lis.get(0);
				System.out.println("ta.getAccountId(): " + ta.getAccountId());
				this.setAccountId(ta.getAccountId());
				this.setShowAccounts1(false);
				this.setShowAccounts2(true);
				
			}
			else{
				System.out.println("el cliente varias cuentas");
				for(TbAccount t: lis){
					accountList.add(new SelectItem(t.getAccountId(), t.getAccountId().toString()));
				}
				this.setShowAccounts1(true);
				this.setShowAccounts2(false);
			}	
		}
		else{
			System.out.println("lis es null ");
		}
	}

	/**
	 * @param accountId the accountId to set
	 */
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	/**
	 * @return the accountId
	 */
	public Long getAccountId() {
		return accountId;
	}

	/**
	 * @param dateIni the dateIni to set
	 */
	public void setDateIni(Date dateIni) {
		this.dateIni = dateIni;
	}

	/**
	 * @return the dateIni
	 */
	public Date getDateIni() {
		return dateIni;
	}

	/**
	 * @param dateEnd the dateEnd to set
	 */
	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}

	/**
	 * @return the dateEnd
	 */
	public Date getDateEnd() {
		return dateEnd;
	}

	/**
	 * @param accountList the accountList to set
	 */
	public void setAccountList(List<SelectItem> accountList) {
		this.accountList = accountList;
	}

	/**
	 * @return the accountList
	 */
	public List<SelectItem> getAccountList() {
		return accountList;
	}

	/**
	 * @param tsu the tsu to set
	 */
	public void setTsu(TbSystemUser tsu) {
		this.tsu = tsu;
	}

	/**
	 * @return the tsu
	 */
	public TbSystemUser getTsu() {
		return tsu;
	}

	/**
	 * @param showData the showData to set
	 */
	public void setShowData(boolean showData) {
		this.showData = showData;
	}

	/**
	 * @return the showData
	 */
	public boolean isShowData() {
		return showData;
	}

	/**
	 * @param showMessageError the showMessageError to set
	 */
	public void setShowMessageError(boolean showMessageError) {
		this.showMessageError = showMessageError;
	}

	/**
	 * @return the showMessageError
	 */
	public boolean isShowMessageError() {
		return showMessageError;
	}

	/**
	 * @param messageError the messageError to set
	 */
	public void setMessageError(String messageError) {
		this.messageError = messageError;
	}

	/**
	 * @return the messageError
	 */
	public String getMessageError() {
		return messageError;
	}

	/**
	 * @param plateId the plateId to set
	 */
	public void setPlateId(Long plateId) {
		this.plateId = plateId;
	}

	/**
	 * @return the plateId
	 */
	public Long getPlateId() {
		return plateId;
	}

	/**
	 * @param plateList the plateList to set
	 */
	public void setPlateList(List<SelectItem> plateList) {
		this.plateList = plateList;
	}

	/**
	 * @return the plateList
	 */
	public List<SelectItem> getPlateList() {
		plateList = new ArrayList<SelectItem>();
		if(this.getAccountId()!=null){
			if(this.getAccountId()==-1){
				plateList.add(new SelectItem("-1","TODAS MIS PLACAS"));	
			}
			else{
				plateList.add(new SelectItem("-1","TODAS MIS PLACAS"));
				for (TbVehicle v : vehicle.getVehicleByAccount(userId, accountId)) {
					plateList.add(new SelectItem(v.getVehicleId(), v.getVehiclePlate()));
				}
				if(plateList.size()==1){
					plateList.clear();
					plateList.add(new SelectItem("0","Sin información"));
				}
			}
		}
		
		return plateList;
	}
	
	public void validate(){
		this.setShowData(true);
		try{
			String fechaInicial= config.getParameter(38L);
			String fechaInicialDef= fechaInicial!=null ? fechaInicial : "01/05/2014";
			
			Date fechaIniParametrizada;
			SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
			fechaIniParametrizada=sdf.parse(fechaInicialDef);
			System.out.println("Fecha Inicial: " + fechaInicialDef + " fecha converitda: " +fechaIniParametrizada);
			
 				if(dateIni!=null && dateEnd!=null){
 				  if(dateIni.getTime() >= fechaIniParametrizada.getTime()){	
					if(dateEnd.getTime()>= dateIni.getTime()){
						if(dateEnd.getTime()<=(new Date()).getTime()){
							setShowMessageError(false);
							this.setShowPanelDownload(true);
//							if(generateFile()){
//								this.setMessageError("El archivo se generó correctamente, de clic en el botón Aceptar para poder descargarlo");
//								this.setShowMessage(true);
//							}
//							else{
//								this.setMessageError("Error al generar archivo");
//								this.setShowMessageError(true);
//							}
						}
						else{
							this.setMessageError("Error: La fecha final no debe ser mayor a la fecha actual.");
							this.setShowMessageError(true);
						}
						
						
					}
					else{
						this.setShowMessageError(true);
						this.setMessageError("Error: La fecha final no debe ser menor a la fecha inicial.");
						this.setShowData(false);
					}
 				  }
 				  else{
					this.setShowMessageError(true);
					this.setMessageError("El reporte General se generará a partir de la siguiente fecha " + fechaInicial);
					this.setShowData(false);
			      }
				}
				else{
					this.setShowMessageError(true);
					this.setMessageError("Error: La fecha inicial y la fecha final no pueden estar vacías.");
					this.setShowData(false);
				} 

			
		}catch(Exception ex){
			ex.printStackTrace();
		}	
	}

	public boolean generateFile() {
		
		boolean res=false;
		SimpleDateFormat sdf= new SimpleDateFormat("dd-MM-yyyyHH.mm.ss");
		Date d= new Date();
		String d1=sdf.format(d);
		BufferedWriter bw = null;
		PrintWriter pw = null;
		//List<String> listDesc= new ArrayList<String>();
		//List<String> listRec=new ArrayList<String>();
		List<String> listDescRec=new ArrayList<String>();
		String path=null;
		path=transactionEJB.getFileTransactionPath();
		if(path==null){
			System.out.println("path null");
			path="d:\\Archivos\\";
		}
		
		File f= new File(path+"file"+d1+".txt");
			try {
				if(f!=null){
					System.out.println("ruta: " + f.getAbsolutePath());
				}
				
				FileWriter fw= new FileWriter(f);
				bw= new BufferedWriter(fw);
				pw= new PrintWriter(bw);
				
				Timestamp f1= new Timestamp(dateIni.getTime());
				Timestamp f2= new Timestamp(dateEnd.getTime() + 86400000);
				//listDesc=transactionEJB.getTransactionDesc(accountId, plateId, userId, f1, f2);
				//listRec= transactionEJB.getTransactionRec(accountId, plateId, userId, f1, f2);
				
				listDescRec= transactionEJB.getTransactionDescRec(accountId, plateId, userId, f1, f2);
				
				System.out.println("size listDescRec:"+listDescRec.size());
				
				for(String s: listDescRec){
					pw.write(s);
					pw.println();
				}
				/*for(String t: listDescRec){
					pw.append(t+"\n");
				}*/
				res=true;
			} catch (IOException e) {
				res=false;
				e.printStackTrace();
			}
			finally{
				try {
					nameFile= f.getPath();
					if(pw!=null){
						pw.close();
					}
					if(bw!=null){
						bw.close();
					}
				} catch (IOException e) {
					res=false;
					e.printStackTrace();
				}
			}
		return res;
	}

	public void showDownload(){
		this.setShowMessage(false);
		this.setShowPanelDownload(true);
	}
	
	public void downloadFile(){
		if(generateFile()){
			FacesContext context = FacesContext.getCurrentInstance();  
	        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();  
		    try{
	        	System.out.println("metodo downloadFile - nameFile: " +  nameFile);
	    		System.out.println("path:" +nameFile);
	    		File f= new File(nameFile);
	    		
	    		if (!f.exists()) {  
	                   try {
	    				response.sendError(HttpServletResponse.SC_NOT_FOUND);
	    			} catch (IOException e) {
	    				e.printStackTrace();
	    			}  
	                  return;  
	              }  

	              response.reset();  
	              response.setBufferSize(10240);
	              MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
	              String mimeType = mimeTypesMap.getContentType(f.getName());
	              System.out.println("mimeType: "+mimeType);
	              response.setContentType("text/plain");   		               
	              response.setHeader("Content-Length", String.valueOf(f.length()));  
	              response.setHeader("Content-Disposition", "attachment;filename=\""+ f.getName() + "\"");  

	              try {  
	    	          FileInputStream fileInputStream = null;
	    		      PrintWriter out = response.getWriter();
	              
	           	      fileInputStream = new FileInputStream(f.getPath());
	         			  int i1; 
	         			  while ((i1=fileInputStream.read())!= -1) {
	         			    out.write(i1); 
	         			  } 
	         			  fileInputStream.close(); 
	         			  out.close(); 
	                  }catch (IOException e){
	           	      e.printStackTrace();
	           	   }  
	                 context.responseComplete();  
	        }catch(Exception e){
	        	e.printStackTrace();
	        }
		}
		else{
			this.setMessageError("Error al generar archivo");
			this.setShowMessageError(true);
		}  
	}
	
	
	public void init(){
		this.setShowMessageError(false);
		this.setMessageError("");
		this.setInProcess(false);
		plateId=-1L;
		this.setShowMessage(false);
	}

	public void ocult(){
		this.setShowData(false);
	}
	
	public void ocult2(){
		this.setShowPanelDownload(false);
	}

	/**
	 * @param inProcess the inProcess to set
	 */
	public void setInProcess(boolean inProcess) {
		this.inProcess = inProcess;
	}

	/**
	 * @return the inProcess
	 */
	public boolean isInProcess() {
		return inProcess;
	}

	/**
	 * @param messageProcess the messageProcess to set
	 */
	public void setMessageProcess(String messageProcess) {
		this.messageProcess = messageProcess;
	}

	/**
	 * @return the messageProcess
	 */
	public String getMessageProcess() {
		return messageProcess;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * @param nameFile the nameFile to set
	 */
	public void setNameFile(String nameFile) {
		this.nameFile = nameFile;
	}

	/**
	 * @return the nameFile
	 */
	public String getNameFile() {
		return nameFile;
	}

	/**
	 * @param showPanelDownload the showPanelDownload to set
	 */
	public void setShowPanelDownload(boolean showPanelDownload) {
		this.showPanelDownload = showPanelDownload;
	}

	/**
	 * @return the showPanelDownload
	 */
	public boolean isShowPanelDownload() {
		return showPanelDownload;
	}

	/**
	 * @param showMessage the showMessage to set
	 */
	public void setShowMessage(boolean showMessage) {
		this.showMessage = showMessage;
	}

	/**
	 * @return the showMessage
	 */
	public boolean isShowMessage() {
		return showMessage;
	}

	/**
	 * @param showAccounts1 the showAccounts1 to set
	 */
	public void setShowAccounts1(boolean showAccounts1) {
		this.showAccounts1 = showAccounts1;
	}

	/**
	 * @return the showAccounts1
	 */
	public boolean isShowAccounts1() {
		return showAccounts1;
	}

	/**
	 * @param showAccounts2 the showAccounts2 to set
	 */
	public void setShowAccounts2(boolean showAccounts2) {
		this.showAccounts2 = showAccounts2;
	}

	/**
	 * @return the showAccounts2
	 */
	public boolean isShowAccounts2() {
		return showAccounts2;
	}

	/**
	 * @param lis the lis to set
	 */
	public void setLis(List<TbAccount> lis) {
		this.lis = lis;
	}

	/**
	 * @return the lis
	 */
	public List<TbAccount> getLis() {
		return lis;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public String getOption() {
		return option;
	}

	@Override
	protected String getCompileFileName() {
		return COMPILE_FILE_NAME;
	}

	@Override
	protected JRDataSource getJRDataSource() {
		return null;
	}

	@Override
	protected String getFileName() {
		return "ReporteGeneral"+ System.currentTimeMillis();
	}

	@Override
	protected ConnectionData getDataConnection() {
		return connection;
	}
	
	public String printPdf(){
		try {
			 super.prepareReport();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected Map<String, Object> getReportParameters() {
		Map<String, Object> reportParameters = new HashMap<String, Object>();

		System.out.println("codigo" + (tsu!=null?tsu.getUserCode():""));
		System.out.println("userId" + userId);
		Timestamp f1= new Timestamp(dateIni.getTime());
		Timestamp f2= new Timestamp(dateEnd.getTime() + 86399999);
		
		reportParameters.put("dateIni",f1);
		reportParameters.put("dateEnd",f2);
		reportParameters.put("codigo", (tsu!=null?tsu.getUserCode():""));
		reportParameters.put("userId", userId);
		reportParameters.put("accountId", accountId);
		reportParameters.put("vehicleId", plateId);
		if(super.getExportOption().equals(ExportOption.EXCEL)){
			reportParameters.put("IS_IGNORE_PAGINATION", true);
		}
		
		return reportParameters;
	}

	public void setConnection(ConnectionData connection) {
		this.connection = connection;
	}

	public ConnectionData getConnection() {
		return connection;
	}

	public void setCodeClient(String codeClient) {
		this.codeClient = codeClient;
	}

	public String getCodeClient() {
		return codeClient;
	}

}
