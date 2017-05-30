package mBeans;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.application.Application;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.swing.table.TableModel;

import jpa.TbSystemUser;
import net.sf.jasperreports.engine.JRDataSource;

import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import report.AbstractBaseReportBean;
import security.UserLogged;
import sessionVar.SessionUtil;
import upload.FileUtil;
import util.ConnectionData;
import ejb.SystemParameters;
import ejb.User;
import ejb.Vehicle;

public class MassiveVehicleLoadClientBean extends AbstractBaseReportBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@EJB(mappedName="ejb/User")
	private User userEJB;
	
	@EJB(mappedName ="ejb/SystemParameters")
	private SystemParameters SystemParametersEJB;
	
	@EJB(mappedName="ejb/Vehicle")
	private Vehicle vehicleEJB;
	
	// Attributes-------------------//
	private final String COMPILE_FILE_NAME="fileErrorLoadVehicles";
	private ConnectionData connection=new ConnectionData();
	private boolean autoUpload = false;
	private boolean useFlash = false;		
	private boolean showMessage;
	private boolean showData=true;
	private boolean showChargeDocument;
	private boolean render1;
	private boolean res1;
	private boolean showMessage2;
	private boolean showMessage3;
	private boolean showMessage4;
	private boolean showErrorFile=false;
	private String message;
	private String extension;
	private String message2;
	private String userCode="";
	private TableModel listVeh;
	private String nameFile="";
	private String pathFile="";
	private Long userId;
	private String templateLink;
	private String instructiveLink;
	
	private TbSystemUser tt;
	
	private UserLogged usrs;
	
	
	public MassiveVehicleLoadClientBean(){
		super();
		usrs = (UserLogged) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
	}
	
	/**
	 * For code value updates
	 */
	public String reload(){
		FacesContext.getCurrentInstance().renderResponse();
		return null;
	}
	
	/**
	 * For refresh the page
	 */
	public void refresh() {
	    FacesContext context = FacesContext.getCurrentInstance();
	    Application application = context.getApplication();
	    UIViewRoot viewRoot = application.getViewHandler().createView(context, context.getViewRoot().getViewId());
	    context.setViewRoot(viewRoot);
	    context.renderResponse(); 
	  }
	
	public String test(){//don't remove
		return null;
	}
	
	/**
	 * Clears Uploaded Data
	 */
	public String clearUploadData() {
        return null;
    }
	
	// Getters and Setters --------
	
	/**
	 * Método encargado de cargar el documento.
	 * @author jromero
	 */
	public void loadDocument(UploadEvent event){	
    	System.out.println("MassiveVehicleClientBean.loadDocument");
       try {       	
    	   this.setShowErrorFile(false);
        	String path ="";
        	String typePath = "Cliente";
        	String systemParametersValue = SystemParametersEJB.getParameterName(typePath);
        	userId=userEJB.getSystemMasterById(usrs.getUserId());
     	    String userCode=userEJB.getSystemUserCode(userId);
		
    		if(systemParametersValue != null) {
        	   String codeS = userCode;
			   if(codeS!= null || userId!= null) {
        			UploadItem item = event.getUploadItem();
        			if(item.getData()==null){
        				this.setShowMessage(true);
            			this.setMessage("El archivo se encuentra vacío, por favor " +
            					"verifique el contenido según la plantilla o el instructivo.");
        			}else{
        				String uploadedFileName = FileUtil.trimFilePath(item.getFileName());
                		path = systemParametersValue.trim()+"/"+codeS+"-"+userEJB.getDocumentClient(codeS, userId, SessionUtil.ip(),SessionUtil.sessionUser().getUserId());
    	            	System.out.println("MassiveVehicleClientBean.loadDocument.path-->"+path);
            			java.io.File uniqueFile = FileUtil.uniqueFile(new java.io.File(path), uploadedFileName);
            			nameFile=uniqueFile.getName();
            			pathFile=path;
            			System.out.println("path:"+pathFile+";file:"+nameFile);
            			FileUtil.write(uniqueFile, item.getData());
            			listVeh=vehicleEJB.loadFileVehicles(uniqueFile);
            			if(listVeh!=null){
            				if((listVeh.getColumnName(0)
            						.toUpperCase().trim().equals("PLACA"))&&
    							(listVeh.getColumnName(1)
    									.toUpperCase().trim().equals("ADICIONAL1"))&&
    							(listVeh.getColumnName(2)
    									.toUpperCase().trim().equals("ADICIONAL2"))&&
    							(listVeh.getColumnName(3)
    									.toUpperCase().trim().equals("ADICIONAL3"))&&
    							((listVeh.getColumnName(4)
    									.toUpperCase().trim().equals("CATEGORÍA"))||
    							(listVeh.getColumnName(4)
    									.toUpperCase().trim().equals("CATEGORIA")))&&
    							((listVeh.getColumnName(5)
    									.toUpperCase().trim().equals("LÍNEA"))||
    							(listVeh.getColumnName(5)
    									.toUpperCase().trim().equals("LINEA")))&&
    							(listVeh.getColumnName(6)
    									.toUpperCase().trim().equals("ESPECIAL"))&&
    							((listVeh.getColumnName(7)
    									.toUpperCase().trim().equals("OBSERVACIÓN"))||
    							(listVeh.getColumnName(7)
    									.toUpperCase().trim().equals("OBSERVACION")))&&
    								listVeh.getColumnCount()==8){
            					
            					if(listVeh.getRowCount()==0){
            						if(uniqueFile.delete()){
                						System.out.println("Se eliminó archivo para cargue: "+pathFile+":"+nameFile);
                					}else{
                						System.out.println("No se pudo eliminar el archivo para cargue: "
                								+pathFile+":"+nameFile);
                					}
                					this.setShowMessage(true);
                					this.setMessage("El archivo no tiene ningún vehículo para cargar," +
                							" por favor verifique.");
                				}else{
                					this.setShowMessage3(true);
                    				this.setMessage("Se cargará(n) "+listVeh.getRowCount()+
                    						" vehículo(s), de clic en el botón " +
                    						"Aceptar para confirmar la carga.");
                				}
            				}else{
            					if(uniqueFile.delete()){
            						System.out.println("Se eliminó archivo para cargue: "+pathFile+":"+nameFile);
            					}else{
            						System.out.println("No se pudo eliminar el archivo para cargue: "
            								+pathFile+":"+nameFile);
            					}
            					this.setShowMessage(true);
            					this.setMessage("El archivo no tiene la estructura válida, " +
            							"por favor verifique la plantilla Excel y el instructivo PDF.");
            				}
            			}else{
            				System.out.println("No se pudo leer archivo");
            				this.setShowMessage(true);
                			this.setMessage("Ocurrió un erro al momento de cargar el archivo, " +
                					"por favor inténtelo más tarde.");
            			}
        			}
		        }else{
		    	   this.setShowMessage2(true);
				   this.setMessage2("Ingrese Número de Identificación.");
		        }
			}else{
				this.setShowMessage(true);
				this.setMessage("Error en la carga masiva de vehículos. Comuníquese con el Administrador."); 
		    	System.out.println("Error:La carpeta no existe o no esta compartida. Verifique.");
		    	refresh();
			}
        } catch (IOException ex) {
        	System.out.println(" [ Error en MassiveVehicleClientBean.loadDocument : IOException] ");
        	this.setShowMessage(true);
			this.setMessage("Error en la carga masiva de vehículos. Comuníquese con el Administrador.");
            ex.printStackTrace();
        }  catch (Exception e){
        	System.out.println(" [ Error en MassiveVehicleClientBean.loadDocument : Exception] ");
        	e.printStackTrace();
			this.setShowMessage(true);
			this.setMessage("Error en la carga masiva de vehículos. Comuníquese con el Administrador.");  
        }
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
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	
	public String hideModal(){
		this.setMessage("");
		this.setShowMessage(false);
		this.setShowMessage2(false);
		this.setShowMessage3(false);
		this.setShowMessage4(false);
		this.setMessage2("");
		this.setShowData(true);
		this.setShowChargeDocument(false);
		clearUploadData();
		userCode="";
		return null;
	}
	
	public String saveVehicles(){
		Long cant=vehicleEJB.createMassiveVehicles(SessionUtil.sessionUser()
				.getUserId(),SessionUtil.ip(),listVeh, nameFile, userId,1L);
		System.out.println("cantidadveh:"+cant);
		String mensaje="";
		if(cant==0L){
			mensaje="No se pudo registrar ningún vehículo, por favor verifique el archivo de errores.";
			this.setShowErrorFile(true);
		}else if(cant<(long)listVeh.getRowCount()){
			if(cant==1L){
				mensaje="Se registró "+cant+" vehículo, por favor verifique el archivo de errores para los restantes.";
			}else{
				mensaje="Se registraron "+cant+" vehículos, por favor verifique el archivo de errores para los restantes.";
			}
			this.setShowErrorFile(true);
		}else{
			if(cant==1L){
				mensaje="Se registró "+cant+" vehículo.";
			}else{
				mensaje="Se registraron "+cant+" vehículos.";
			}
			this.setShowErrorFile(false);
		}
		hideModal();
		this.setShowMessage(true);
		this.setMessage(mensaje);
		return null;
	}
	
	public String cancelLoad(){
		hideModal();
		this.setShowErrorFile(false);
		java.io.File uniqueFile = new java.io.File(pathFile,nameFile);
		if(uniqueFile.delete()){
			System.out.println("Se eliminó archivo para cargue: "+pathFile+":"+nameFile);
		}else{
			System.out.println("No se pudo eliminar el archivo para cargue: "
					+pathFile+":"+nameFile);
		}
		this.setShowMessage(true);
		this.setMessage("Carga de vehículos cancelada.");
		return null;
	}
	
	public void hideModal2(){
		this.setShowErrorFile(false);
		this.setShowMessage2(false);
		this.setMessage2("");
	}
	
	
	public void hideModal3(){
		this.setShowErrorFile(false);
		this.setShowMessage2(false);
		this.setMessage2("");
		this.setShowData(true);
	}
	
	public void hideModal4(){
		this.setShowErrorFile(false);
		hideModal();
		
	}
	
	public String loadTemplateLink(){
		templateLink=SystemParametersEJB.getParameterValueById(39L);
		try{
			File ficheroXLS = new File(templateLink);
			FacesContext ctx = FacesContext.getCurrentInstance();
			FileInputStream fis = new FileInputStream(ficheroXLS);
			byte[] bytes = new byte[1000];
			int read = 0;

			if (!ctx.getResponseComplete()) {
			   String fileName = ficheroXLS.getName();
			   String contentType = "application/vnd.ms-excel";
			   //String contentType = "application/pdf";
			   HttpServletResponse response =
			   (HttpServletResponse) ctx.getExternalContext().getResponse();

			   response.setContentType(contentType);

			   response.setHeader("Content-Disposition",
			                      "attachment;filename=\"" + fileName + "\"");

			   ServletOutputStream out = response.getOutputStream();

			   while ((read = fis.read(bytes)) != -1) {
			        out.write(bytes, 0, read);
			   }

			   out.flush();
			   out.close();
			   System.out.println("\nDescargado\n");
			   ctx.responseComplete();
			}
		}catch(IOException e){
			e.printStackTrace();
			hideModal();
			this.setShowErrorFile(false);
			this.setShowMessage(true);
			this.setMessage("La plantilla Excel no se pudo descargar.");
		}
		return null;
	}
	
	public String loadInstructiveLink(){
		instructiveLink=SystemParametersEJB.getParameterValueById(40L);
		try{
			File ficheroPDF = new File(instructiveLink);
			FacesContext ctx = FacesContext.getCurrentInstance();
			FileInputStream fis = new FileInputStream(ficheroPDF);
			byte[] bytes = new byte[1000];
			int read = 0;

			if (!ctx.getResponseComplete()) {
			   String fileName = ficheroPDF.getName();
			   //String contentType = "application/vnd.ms-excel";
			   String contentType = "application/pdf";
			   HttpServletResponse response =
			   (HttpServletResponse) ctx.getExternalContext().getResponse();

			   response.setContentType(contentType);

			   response.setHeader("Content-Disposition",
			                      "attachment;filename=\"" + fileName + "\"");

			   ServletOutputStream out = response.getOutputStream();

			   while ((read = fis.read(bytes)) != -1) {
			        out.write(bytes, 0, read);
			   }

			   out.flush();
			   out.close();
			   System.out.println("\nDescargado\n");
			   ctx.responseComplete();
			}
		}catch(IOException e){
			e.printStackTrace();
			hideModal();
			this.setShowErrorFile(false);
			this.setShowMessage(true);
			this.setMessage("El instructivo PDF no se pudo descargar.");
		}
		return null;
	}
	
	@Override
	protected Map<String, Object> getReportParameters() {
		Map<String, Object> reportParameters = new HashMap<String, Object>();
		reportParameters.put("userId", userId);
		reportParameters.put("userCode", userEJB.getSystemUserCode(userId));
		reportParameters.put("nameFile", nameFile);
		System.out.println("errorParameters: "+reportParameters);
		return reportParameters;
	}
	
	public String printPdf(){
		try {
			 super.prepareReport();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

	/**
	 * @param userCode the userCode to set
	 */
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	/**
	 * @return the userCode
	 */
	public String getUserCode() {
		String userCode = "";
		if(res1){
			userCode=tt.getUserCode();
		}
		else{
			userCode=this.userCode;
		}
		
		return userCode;
	}

	/**
	 * @param tt the tt to set
	 */
	public void setTt(TbSystemUser tt) {
		this.tt = tt;
	}

	/**
	 * @return the tt
	 */
	public TbSystemUser getTt() {
		return tt;
	}

	/**
	 * @param res1 the res1 to set
	 */
	public void setRes1(boolean res1) {
		this.res1 = res1;
	}

	/**
	 * @return the res1
	 */
	public boolean isRes1() {
		return res1;
	}

	/**
	 * @param showMessage2 the showMessage2 to set
	 */
	public void setShowMessage2(boolean showMessage2) {
		this.showMessage2 = showMessage2;
	}

	/**
	 * @return the showMessage2
	 */
	public boolean isShowMessage2() {
		return showMessage2;
	}

	/**
	 * @param message2 the message2 to set
	 */
	public void setMessage2(String message2) {
		this.message2 = message2;
	}

	/**
	 * @return the message2
	 */
	public String getMessage2() {
		return message2;
	}

	/**
	 * @param autoUpload the autoUpload to set
	 */
	public void setAutoUpload(boolean autoUpload) {
		this.autoUpload = autoUpload;
	}

	/**
	 * @return the autoUpload
	 */
	public boolean isAutoUpload() {
		return autoUpload;
	}

	/**
	 * @param useFlash the useFlash to set
	 */
	public void setUseFlash(boolean useFlash) {
		this.useFlash = useFlash;
	}

	/**
	 * @return the useFlash
	 */
	public boolean isUseFlash() {
		return useFlash;
	}

	/**
	 * @param usrs the usrs to set
	 */
	public void setUsrs(UserLogged usrs) {
		this.usrs = usrs;
	}

	/**
	 * @return the usrs
	 */
	public UserLogged getUsrs() {
		return usrs;
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
	 * @param render1 the render1 to set
	 */
	public void setRender1(boolean render1) {
		this.render1 = render1;
	}

	/**
	 * @return the render1
	 */
	public boolean isRender1() {
		return render1;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getExtension() {
		return extension;
	}

	public void setShowChargeDocument(boolean showChargeDocument) {
		this.showChargeDocument = showChargeDocument;
	}

	public boolean isShowChargeDocument() {
		return showChargeDocument;
	}

	public TableModel getListVeh() {
		return listVeh;
	}

	public void setListVeh(TableModel listVeh) {
		this.listVeh = listVeh;
	}

	public boolean isShowMessage3() {
		return showMessage3;
	}

	public void setShowMessage3(boolean showMessage3) {
		this.showMessage3 = showMessage3;
	}

	public String getNameFile() {
		return nameFile;
	}

	public void setNameFile(String nameFile) {
		this.nameFile = nameFile;
	}

	public String getPathFile() {
		return pathFile;
	}

	public void setPathFile(String pathFile) {
		this.pathFile = pathFile;
	}

	public boolean isShowMessage4() {
		return showMessage4;
	}

	public void setShowMessage4(boolean showMessage4) {
		this.showMessage4 = showMessage4;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getTemplateLink() {
		return templateLink;
	}

	public void setTemplateLink(String templateLink) {
		this.templateLink = templateLink;
	}

	public String getInstructiveLink() {
		return instructiveLink;
	}

	public void setInstructiveLink(String instructiveLink) {
		this.instructiveLink = instructiveLink;
	}

	public boolean isShowErrorFile() {
		return showErrorFile;
	}

	public void setShowErrorFile(boolean showErrorFile) {
		this.showErrorFile = showErrorFile;
	}

	@Override
	protected String getCompileFileName() {
		return COMPILE_FILE_NAME;
	}

	@Override
	protected ConnectionData getDataConnection() {
		return connection;
	}

	@Override
	protected String getFileName() {
		return "ErrorFileLoadVehicles" + System.currentTimeMillis();
	}

	@Override
	protected JRDataSource getJRDataSource() {
		return null;
	}

	/**
	 * @return the cOMPILE_FILE_NAME
	 */
	public String getCOMPILE_FILE_NAME() {
		return COMPILE_FILE_NAME;
	}

	/**
	 * @param connection the connection to set
	 */
	public void setConnection(ConnectionData connection) {
		this.connection = connection;
	}

	/**
	 * @return the connection
	 */
	public ConnectionData getConnection() {
		return connection;
	}

}
