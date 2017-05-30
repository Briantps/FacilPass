package reportBeanAdmin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;

import report.AbstractBaseReportBean;

import util.ConnectionData;
import validator.Validation;

import ejb.Config;
import ejb.Transaction;
import ejb.User;
import ejb.Vehicle;

import jpa.TbAccount;
import jpa.TbCodeType;
import jpa.TbSystemUser;
import jpa.TbVehicle;

public class FileTransactionBeanAdmin extends AbstractBaseReportBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@EJB(mappedName="ejb/User")
	private User userEJB;
	
	@EJB(mappedName="ejb/Vehicle")
	private Vehicle vehicle;
	
	@EJB(mappedName="ejb/Config")
	private Config config;
	

	@EJB(mappedName="ejb/Transaction")
	private Transaction transactionEJB;

	private Long accountId=-1L;
	
	private Date dateIni;
	
	private Date dateEnd;
	
	private List<SelectItem> accountList;
	
    private TbSystemUser tsu;
	
	private boolean showData;
	
	private boolean showMessage;
	
	private boolean showMessageError;
	
	private String messageError;
	
	private Long plateId=-1L;
	
	private List<SelectItem> plateList;
	
	private boolean inProcess;
	
	private String messageProcess;
	
	private Long userId;
	
	private Long idClient;
	
	private String codeClient="";
	
	private Long codeType;
	
	private TbSystemUser client;
	
	private List<SelectItem> codeTypesList;
	
	private List<TbCodeType> codeTypes;
	
	private String nameFile;
	
	private String option;
	
	private boolean showPanelDownload;
	
	private boolean showBotton;
	
	private BigDecimal previousBalance;
	
	private BigDecimal newBalance;
	
	private boolean showAccounts1;
	
	private boolean showAccounts2;
	
	private List<TbAccount> lis;
	
	private ConnectionData connection=new ConnectionData();
	
    private final String COMPILE_FILE_NAME="reporteGeneral";
	
	
	public FileTransactionBeanAdmin(){
		accountId=0L;
		plateId=0L;
		accountList= new ArrayList<SelectItem>();
		accountList.add(new SelectItem(0L, "Seleccione Cuenta"));
		plateList = new ArrayList<SelectItem>();
		plateList.add(new SelectItem(0L, "Seleccione Placa"));
		this.setShowAccounts1(false);
		this.setShowAccounts2(false);
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
		if(plateList!=null){
			plateList.clear();
		}
		//plateList.add(new SelectItem(0L,"Seleccione Placa"));	
		if(this.getAccountId()!=null){
			if(accountId==-1L||accountId==0L){
				plateList.add(new SelectItem(-1L,"TODAS LAS PLACAS"));	
				System.out.println("cuenta -1" + accountId);
				System.out.println("placa " + plateId);
			}
			//else if(accountId==0L){
				//System.out.println("cuenta 0" + accountId);
				//System.out.println("placa " + plateId);
			//}
			else{
				plateList.add(new SelectItem(-1L,"TODAS LAS PLACAS"));
				for (TbVehicle v : vehicle.getVehicleByAccount(idClient, accountId)) {
					plateList.add(new SelectItem(v.getVehicleId(), v.getVehiclePlate()));
				}
				if(plateList.size()==1){
					plateList.clear();
					plateList.add(new SelectItem(-2,"Sin información"));
				}
				System.out.println("cuenta " + accountId);
				System.out.println("placa " + plateId);
			}
		}
		return plateList;
	}
	
	public String getPlates() {
//		if(plateList!=null){
//			plateList.clear();
//		}
//		plateList.add(new SelectItem(0L,"Seleccione Placa"));	
//		if(this.getAccountId()!=null){
//			if(accountId==-1L){
//				plateList.add(new SelectItem(-1L,"TODAS LAS PLACAS"));	
//				System.out.println("cuenta -1" + accountId);
//				System.out.println("placa " + plateId);
//			}
//			else if(accountId==0L){
//				System.out.println("cuenta 0" + accountId);
//				System.out.println("placa " + plateId);
//			}
//			else{
//				plateList.add(new SelectItem(-1L,"TODAS LAS PLACAS"));
//				for (TbVehicle v : vehicle.getVehicleByAccount(idClient, accountId)) {
//					plateList.add(new SelectItem(v.getVehicleId(), v.getVehiclePlate()));
//				}
//				if(plateList.size()==1){
//					plateList.clear();
//					plateList.add(new SelectItem(-2,"Sin información"));
//				}
//				System.out.println("cuenta " + accountId);
//				System.out.println("placa " + plateId);
//			}
//		}
		return null;
	}
	
	
	public void validate(){
		this.setShowData(true);
		System.out.println("accountId en validate() :" + accountId);
		System.out.println("plateId en validate() :" + plateId);
		try{
			if(codeClient!=null&&!codeClient.equals("")){
				if(accountId!=0L){
					if(plateId!=0L){
						if(plateId!=-2L){
							if(dateIni!=null && dateEnd!=null){
								String fechaA=config.getParameter(38L);
								System.out.println("[fechaA]"+fechaA);
								String fechaI= fechaA!=null ? fechaA : "01/05/2014";
								System.out.println("[fechaI]"+fechaI);
								SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd/MM/yyyy");
								Date fechaBase = null;
								try {
									fechaBase = formatoDelTexto.parse(fechaI);
								} catch (ParseException ex) {
									ex.printStackTrace();
								}
								if(dateIni.getTime()>=fechaBase.getTime()){
									System.out.println("FechaMayor");
									if(dateEnd.getTime()>= dateIni.getTime()){
										if(dateEnd.getTime()<=(new Date()).getTime()){
											setShowMessageError(false);
											this.setShowPanelDownload(true);
//											if(generateFile()){
//												this.setMessageError("El archivo se generó correctamente, de clic en el botón Aceptar para poder descargarlo");
//												this.setShowMessage(true);
//											}
//											else{
//												this.setMessageError("Error al generar archivo");
//												this.setShowMessageError(true);
//											}
										}
										else{
											this.setShowMessageError(true);
											this.setMessageError("Error: La fecha final no debe ser mayor a la fecha actual.");
											this.setShowData(false);
										}	
									}
										
									else{
										this.setShowMessageError(true);
										this.setMessageError("Error: La fecha final no debe ser menor a la fecha inicial.");
										this.setShowData(false);
									}
								}else{
									this.setShowMessageError(true);
									this.setMessageError("El reporte General se generará a partir de la siguiente fecha "+ fechaI);
									this.setShowData(false);
								}
							}
							else{
									this.setShowMessageError(true);
									this.setMessageError("Error: La fecha inicial y la fecha final no pueden estar vacías.");
									this.setShowData(false);
							}
						}else{
							this.setMessageError("Error: La cuenta no tiene vehículos.");
							this.setShowMessageError(true);
						}
					}
					else{
						this.setShowMessageError(true);
						this.setMessageError("Error: Seleccione una placa.");
					}
				}else{
					this.setShowMessageError(true);
					this.setMessageError("Error: Presione Buscar Cuentas y seleccione una cuenta.");
				}
				
			}else{
				this.setShowMessageError(true);
				this.setMessageError("Error: Digite el Nro. de identificación del cliente.");
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
				listDescRec=transactionEJB.getTransactionDescRec(accountId, plateId, idClient, f1, f2);
				//listRec= transactionEJB.getTransactionRec(accountId, plateId, idClient, f1, f2);
				
				
				
				for(String s: listDescRec){
					//pw.write("");
					//pw.append(s+"\n");
					pw.write(s);  
					pw.println();
				}
				/*for(String t: listRec){
					pw.append(t+"\n");
				}*/
				res=true;
			} catch (IOException e) {
				res=false;
				e.printStackTrace();
			}
			finally{
				nameFile= f.getPath();
				try {
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
	 * @param idClient the idClient to set
	 */
	public void setIdClient(Long idClient) {
		this.idClient = idClient;
	}

	/**
	 * @return the idClient
	 */
	public Long getIdClient() {
		return idClient;
	}

	/**
	 * @param codeClient the codeClient to set
	 */
	public void setCodeClient(String codeClient) {
		this.codeClient = codeClient;
	}

	/**
	 * @return the codeClient
	 */
	public String getCodeClient() {
		return codeClient;
	}

	/**
	 * @param codeType the codeType to set
	 */
	public void setCodeType(Long codeType) {
		this.codeType = codeType;
	}

	/**
	 * @return the codeType
	 */
	public Long getCodeType() {
		return codeType;
	}

	/**
	 * @param client the client to set
	 */
	public void setClient(TbSystemUser client) {
		this.client = client;
	}

	/**
	 * @return the client
	 */
	public TbSystemUser getClient() {
		return client;
	}

	/**
	 * @param codeTypesList the codeTypesList to set
	 */
	public void setCodeTypesList(List<SelectItem> codeTypesList) {
		this.codeTypesList = codeTypesList;
	}

	/**
	 * @return the codeTypesList
	 */
	public List<SelectItem> getCodeTypesList() {
		if(codeTypesList==null){
			codeTypesList = new ArrayList<SelectItem>();
			for (TbCodeType type : getCodeTypes()) {
				codeTypesList.add(new SelectItem(type.getCodeTypeId(), type
						.getCodeTypeAbbreviation()));
			}
		}
		return codeTypesList;
	}

	/**
	 * @param codeTypes the codeTypes to set
	 */
	public void setCodeTypes(List<TbCodeType> codeTypes) {
		this.codeTypes = codeTypes;
	}

	/**
	 * @return the codeTypes
	 */
	public List<TbCodeType> getCodeTypes() {
		if(codeTypes == null){
			codeTypes = new ArrayList<TbCodeType>();
		}
		codeTypes = userEJB.getCodeTypes();
		return codeTypes;
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


	public String search() {
		accountList.clear();
	    //plateList.clear();
	    //plateList.add(new SelectItem(0L, "Seleccione Placa"));
	    this.setShowData(false);
			idClient = -1L;
			if (codeClient!="") {
				if(Validation.isNumeric(codeClient)){
					client = userEJB.getUserByCode(codeClient, codeType);
					if (client != null) {
						idClient = client.getUserId();
						this.setIdClient(client.getUserId());
						System.out.println("client " + client.getUserCode());
						System.out.println("idClient " + idClient);
						

						lis=userEJB.getClientAccount(idClient);
						
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
								accountList.add(new SelectItem(-1, "TODAS LAS CUENTAS"));

								System.out.println("el cliente varias cuentas");
								
								for(TbAccount t: lis){
									accountList.add(new SelectItem(t.getAccountId(), t.getAccountId().toString()));
								}
								this.setShowAccounts1(true);
								this.setShowAccounts2(false);
								setShowMessageError(false);
								this.setShowBotton(true);
							}	
						}

						else{
							System.out.println("lis es null ");
						}
					} else {
						accountList.add(new SelectItem(0L, "Seleccione cuenta"));
						System.out.println("Entre al else de client == null");
						this.setMessageError("Error: No se encontraron datos para el cliente digitado.");
						this.setShowMessageError(true);
					}
				}else{
					accountList.add(new SelectItem(0L, "Seleccione cuenta"));
					this.setMessageError("Error: El campo No. de Identificación + DV solo debe contener números.");
					this.setShowMessageError(true);
				}
				
				
			} else {
				accountList.add(new SelectItem(0L, "Seleccione cuenta"));
				System.out.println("Entre al else de codeClient == null");
				this.setMessageError("Error: Debe digitar el número de identificación del cliente.");
				this.setShowMessageError(true);
				setShowData(false);
				
			}
			System.out.println("client" + client);
			System.out.println("idClient" + idClient);
			System.out.println("codeClient" + codeClient);
			
			return null;
	}
	
	public String changeTypeDoc() {
		setShowMessageError(false);
		showMessageError = false;
		showData = false;
		this.idClient = null;
		this.codeClient = "";
		this.setCodeClient("");
		accountId=0L;
		plateId=0L;
		accountList.clear();
		accountList.add(new SelectItem(0L, "Seleccione Cuenta"));
		plateList.clear();
		plateList.add(new SelectItem(0L, "Seleccione Placa"));
		this.setShowPanelDownload(false);
		return null;
	}
	
	public void ocult1(){
		accountId=0L;
		accountList.clear();
		accountList.add(new SelectItem(0L, "Seleccione Cuenta"));
		plateId=0L;
		plateList.clear();
		plateList.add(new SelectItem(0L, "Seleccione Placa"));
		this.setShowData(false);
		this.setShowPanelDownload(false);
	}


	/**
	 * @param showBotton the showBotton to set
	 */
	public void setShowBotton(boolean showBotton) {
		this.showBotton = showBotton;
	}


	/**
	 * @return the showBotton
	 */
	public boolean isShowBotton() {
		return showBotton;
	}


	/**
	 * @param previousBalance the previousBalance to set
	 */
	public void setPreviousBalance(BigDecimal previousBalance) {
		this.previousBalance = previousBalance;
	}


	/**
	 * @return the previousBalance
	 */
	public BigDecimal getPreviousBalance() {
		return previousBalance;
	}


	/**
	 * @param newBalance the newBalance to set
	 */
	public void setNewBalance(BigDecimal newBalance) {
		this.newBalance = newBalance;
	}


	/**
	 * @return the newBalance
	 */
	public BigDecimal getNewBalance() {
		return newBalance;
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
		return "ReporteGeneral"+System.currentTimeMillis();
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

		System.out.println("codigo" + codeClient);
		System.out.println("userId" + idClient);
		Timestamp f1= new Timestamp(dateIni.getTime());
		Timestamp f2= new Timestamp(dateEnd.getTime() + 86399999);
		
		reportParameters.put("dateIni",f1);
		reportParameters.put("dateEnd",f2);
		reportParameters.put("codigo", codeClient);
		reportParameters.put("userId", idClient);
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




}
