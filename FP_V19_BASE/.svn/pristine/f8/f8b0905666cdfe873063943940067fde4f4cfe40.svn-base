package upload;

import java.io.FileInputStream;  
import java.io.IOException;  
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.ejb.EJB;
import javax.faces.application.Application;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;  
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletResponse;  

import ejb.SystemParameters;
import ejb.Device;
import ejb.User;

import jpa.TbCodeType;
import jpa.TbDeviceType;
import jpa.TbSystemParameter;

import security.UserLogged;
import sessionVar.SessionUtil;



public class FileDownloadBean implements Serializable {  
	
	/** Atributos **/
	private static final long serialVersionUID = -1L;
	private static final int DEFAULT_BUFFER_SIZE = 10240;  
		
	private ArrayList<File> files = new ArrayList<File>();
	private List<String> listClient;
	private List<SelectItem> typePathList;
    private String typePath = "";
    
	private List<SelectItem> codeTypeList;
	private Long codeTypeId;

	private UserLogged usrs;
	private String name;
	private String code;
	private long length;
		
	/** Control Modal **/
	private boolean show;
	private boolean showC;
	private boolean showD;
	private boolean showModal;
	private boolean showInfo;
	private boolean showMessage;
	private boolean showFile;
	private String corfirmMessage;
	private String modalMessage;
	private String messageInfo;
	private String suggest = "";

	@PersistenceContext(unitName = "bo") 
	EntityManager em;
	
	/** EJB **/
	@EJB(mappedName ="ejb/SystemParameters")
	private SystemParameters SystemParametersEJB;
	
	@EJB(mappedName="ejb/User")
	private User UserEJB;
	
	@EJB(mappedName="ejb/Device")
	private Device device;
	
	
	/**
	 * Constructor
	 */
	public FileDownloadBean (){
		usrs = (UserLogged) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
		listClient = new ArrayList<String>();
	}
	
	/**
	 * Hides modal windows.
	 */
	public void hideModal(){
		this.setShowModal(false);
		this.setModalMessage(null);
	}
	
	/**
	 * Método encargado de mostrar los archivos del cliente o dispositivos que se encuentran en el servidor.
	 * @author psanchez
	 */
	public String viewFiles(){    	
    	String path ="";
    	String typePath1 = typePath;
    	String systemParametersValue = SystemParametersEJB.getParameterName(typePath1);
    	

			if (systemParametersValue != null) {
	    		String codeS = getSelectedCode();
	    		if (codeS != null) {
	    			setMessageInfo("");
	    			setShowInfo(true);
	    			setShowMessage(false);
	    			path = systemParametersValue.trim()+"/"+codeS;
	    			java.io.File directory = new java.io.File(path);
	    			boolean isDirectory = directory.isDirectory();
	    			 if (isDirectory) {
	    				 // It returns true if directory is a directory.
	    				 System.out.println("the name you have entered is a directory  : "  +    directory);  
	    				 //It returns the absolute path of a directory.
	    				 System.out.println("the path is "  + directory.getAbsolutePath());
	    				 java.io.File[] strFilesDirs = directory.listFiles ( );
	    				
	    				 files = new ArrayList<File>(); 
	   
	    				 for ( int i = 0 ; i < strFilesDirs.length ; i ++ ) {
	    		     		 if ( strFilesDirs[i].isDirectory ( ) ){	
	    		     		 	 System.out.println ( "Directory: " + strFilesDirs[i] ) ;
	    		     		 }else if ( strFilesDirs[i].isFile ( )  && !strFilesDirs[i].getName().toLowerCase().contains("thumbs")){
	    		     		 	 System.out.println ( "File: " + strFilesDirs[i] + " (" + strFilesDirs[i].length ( ) + ")"  ) ;
	    		     		 	 
	    		     		 	java.io.File f = strFilesDirs[i];
	    		     		 	    		 	
	    		     		 	File file = new File(f);
	    		     		 	file.setLength(f.length());
	    		     		 	file.setName(f.getName());
	    		     		 	try {
	    		     		 		file.setData(FileUtil.readBytes(f));
	    						} catch (IOException e) {
	    							e.printStackTrace();
	    						}
	    		     		 	 files.add(file);
	    		     		 }
	    				 }
	    			} else {
	    				files.clear();
	    				//setShowMessage(true);
	        			setShowInfo(false);
	        			setMessageInfo("No tiene documentos relacionados.");
	        			this.setShowModal(true);
	    			}
	    		} else{
	    			//The selected code does not exist
	    			//setShowMessage(true);
	    			setShowInfo(false);
	    			setMessageInfo("El No. de Identificación de " + typePath +" no existe. Verifique.");
	    			this.setShowModal(true);
	    			files.clear();
	    		}
			 }else{
	 			//The file does not exist
	 	    		//setShowMessage(true);
	 				setShowInfo(false);
	 				setMessageInfo("Error al descargar documentos. Comuníquese con el Administrador."); 
	 			    System.out.println("Error:La carpeta no existe o no esta compartida. Verifique.");
	 		}
    	refresh();
		return null;
	}


	
	
	/**
	 * Método encargado de mostrar los archivos del cliente en sesión.
	 * @author psanchez
	 */
	public String viewFilesClient(){
		String path = "";
		String typePath = "Cliente";
    	String systemParametersValue = SystemParametersEJB.getParameterName(typePath);
    	
    	if (systemParametersValue != null) {
    		Long userId=UserEJB.getSystemMasterById(usrs.getUserId());
		    String userCode=UserEJB.getSystemUserCode(userId);
    		String codeS = userCode;
    		if (codeS != null) {
    			setMessageInfo("");
    			setShowInfo(true);
    			setShowMessage(false);
    			
    			path = systemParametersValue.trim()+"/"+codeS+"-"+UserEJB.getDocumentClient(codeS, userId, SessionUtil.ip(),SessionUtil.sessionUser().getUserId());
    			java.io.File directory = new java.io.File(path);
    			boolean isDirectory = directory.isDirectory();
    			 if (isDirectory) {
    				 // It returns true if directory is a directory.
    				 System.out.println("the name you have entered is a directory  : "  +    directory);  
    				 //It returns the absolute path of a directory.
    				 System.out.println("the path is "  + directory.getAbsolutePath());
    				 java.io.File[] strFilesDirs = directory.listFiles ( );
    				 files = new ArrayList<File>(); 
    				 for ( int i = 0 ; i < strFilesDirs.length ; i ++ ) {
    		     		 if ( strFilesDirs[i].isDirectory ( ) ){	
    		     		 	 System.out.println ( "Directory: " + strFilesDirs[i] ) ;
    		     		 }else if ( strFilesDirs[i].isFile ( )  && !strFilesDirs[i].getName().toLowerCase().contains("thumbs")){
    		     		 	 System.out.println ( "File: " + strFilesDirs[i] + " (" + strFilesDirs[i].length ( ) + ")"  ) ;
	    		     		 	java.io.File f = strFilesDirs[i];	 	
	    		     		 	File file = new File(f);
	    		     		 	file.setLength(f.length());
	    		     		 	file.setName(f.getName());
    		     		 	try {
    		     		 		file.setData(FileUtil.readBytes(f));
    						} catch (IOException e) {
    							e.printStackTrace();
    						}
    		     		 	 files.add(file);
    		     		 }
    				 }
    			} else {
    				files.clear();
    				setShowMessage(true);
        			setShowInfo(false);
        			setMessageInfo("No tiene documentos relacionados.");
    			}
    		} else{
    			//The selected code does not exist
    			files.clear();
    		}
    	}else{
    		setShowMessage(true);
			setShowInfo(false);
			setMessageInfo("Error al descargar documentos. Comuníquese con el Administrador."); 
		    System.out.println("Error:La carpeta no existe o no esta compartida. Verifique.");
		}
		return null;
	}


	public void downLoad() throws IOException {  
         FacesContext context = FacesContext.getCurrentInstance();  
         HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();  
         String path = "";  
         String typePath = "Cliente";
     	 String systemParametersValue = SystemParametersEJB.getParameterName(typePath);
 	     
         if (systemParametersValue != null) {
        	Long userId=UserEJB.getSystemMasterById(usrs.getUserId());
     	    String userCode=UserEJB.getSystemUserCode(userId);
     		String codeS = userCode;
     		if (codeS != null) {
     			path = systemParametersValue.trim()+"/"+codeS+"-"+UserEJB.getDocumentClient(codeS, userId, SessionUtil.ip(),SessionUtil.sessionUser().getUserId());
     			java.io.File directory = new java.io.File(path);
     			boolean isDirectory = directory.isDirectory();
     			 if (isDirectory) {
     				 // It returns true if directory is a directory.
     				 System.out.println("the name you have entered is a directory  : "  +    directory);  
     				 //It returns the absolute path of a directory.
     				 System.out.println("the path is "  + directory.getAbsolutePath()+"/"+name);

     				     java.io.File  f = new java.io.File(directory+"/"+name) ;
     				     File file = new File(f);
		     		     file.setLength(f.length());
		     		 	 file.setName(f.getName());
		     		 	 
		     		 	 
   		     		 	if (!f.exists()) {  
   		                    response.sendError(HttpServletResponse.SC_NOT_FOUND);  
   		                   return;  
   		               }  

   		               response.reset();  
   		               response.setBufferSize(DEFAULT_BUFFER_SIZE);
   		               MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
   		               String mimeType = mimeTypesMap.getContentType(f.getName());
   		               System.out.println("mimeType: "+mimeType);
   		               //response.setContentType("application/pdf");
   		               response.setContentType(mimeType);   		               
   		               response.setHeader("Content-Length", String.valueOf(f.length()));  
   		               response.setHeader("Content-Disposition", "attachment;filename=\""+ f.getName() + "\"");  


   		 		       FileInputStream fileInputStream = null;
   					   PrintWriter out = response.getWriter();
   		               try {  
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
   		              }  
     		     }
     	  }else{
     		//The file does not exist
  			setShowMessage(true);
  			setShowInfo(false);
  			setMessageInfo("Error al descargar documentos. Comuníquese con el Administrador.");
  			files.clear();
     	  }
     } 
	
	
	
	public void downLoadFileAdmin() throws IOException {  
       FacesContext context = FacesContext.getCurrentInstance();  
       HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();  
       
       String path = "";
       String typePath1 = typePath;
       String systemParametersValue = SystemParametersEJB.getParameterName(typePath1);
       
    	if (systemParametersValue != null) {
    		String codeS = getSelectedCode();
    		if (codeS != null) {
    			path = systemParametersValue.trim()+"/"+codeS;
    			java.io.File directory = new java.io.File(path);
    			boolean isDirectory = directory.isDirectory();
    			 if (isDirectory) {
    				 // It returns true if directory is a directory.
    				 System.out.println("the name you have entered is a directory  : "  +    directory);  
    				 //It returns the absolute path of a directory.
    				 System.out.println("the path is "  + directory.getAbsolutePath()+"/"+name);

    				     java.io.File  f = new java.io.File(directory+"/"+name) ;
    				     File file = new File(f);
		     		     file.setLength(f.length());
		     		 	 file.setName(f.getName());
		     		 	 
  		     		 	if (!f.exists()) {  
  		                    response.sendError(HttpServletResponse.SC_NOT_FOUND);  
    		               return;  
    		           }  
   		     		   response.reset();  
		               response.setBufferSize(DEFAULT_BUFFER_SIZE);
		               MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
		               String mimeType = mimeTypesMap.getContentType(f.getName());
		               System.out.println("mimeType: "+mimeType);
		               //response.setContentType("application/pdf");
		               response.setContentType(mimeType);   		               
		               response.setHeader("Content-Length", String.valueOf(f.length()));  
		               response.setHeader("Content-Disposition", "attachment;filename=\""+ f.getName() + "\"");  

  		 		       FileInputStream fileInputStream = null;
  					   PrintWriter out = response.getWriter();
  		               try {  
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
  		              }  
    		     }
    	   }else{
    			//The file does not exist
    			setShowMessage(true);
    			setShowInfo(false);
    			setMessageInfo("Error al descargar documentos. Comuníquese con el Administrador.");
    			files.clear();
    		}	
    } 
   
         

	/**
	 * Loads the list
	 */
	public void loadList(ValueChangeEvent event){		
		listClient.clear();
		typePath = event.getNewValue().toString();
		setCode("");
		this.setShow(true);
		if (typePath.equals("Cliente")) {
			listClient = UserEJB.getClients();
			setShowD(false);
			setShowC(true);
		} else if (typePath.equals("Dispositivo")) {
			listClient = device.getAllDevices();
			setShowC(false);
			setShowD(true);
		} else {
			setShowC(false);
			setShowD(false);
			this.setShow(false);
		}
		clearData();
		refresh();
	}
	
	
	/**
	 * Método encargado mostrar el panel para subir documentos de acuerdo al tipo de documento seleccionado.
	 * @author psanchez
	 */
	public void inputCode(ValueChangeEvent event) {
		this.setShowC(false);
		this.setShowD(false);
		this.setShow(true);
		this.setShowFile(false);
        this.setCode("");
		typePath = event.getNewValue().toString();
		if (typePath.equals("Cliente")) {
			setShowD(false);
			setShowC(true);
		} else if (typePath.equals("Dispositivo")) {
			setShowC(false);
			setShowD(true);
		}
	}
	
	/**
	 * Clears Data
	 */
	public void clearFilter() {
		setShowMessage(false);
		setShowFile(false);
		setMessageInfo("");
        code = "";
    }
	
	/**
	 * Método encargado de mostrar los archivos del cliente o dispositivos que se encuentran en el servidor.
	 * @author psanchez
	 */
	public void fileTypeList(){   
		setMessageInfo("");
		setShowMessage(false);
    	String path ="";
		String codeType = null;
    	String systemParametersValue = SystemParametersEJB.getParameterName(typePath);    	
			
    	if (systemParametersValue != null) {
    		if (code != null && !code.trim().equals("") ) {
				if(typePath.equals("Cliente")){
    				TbCodeType tc = em.find(TbCodeType.class, codeTypeId);
    				codeType = tc.getCodeTypeAbbreviation();
    			}else if(typePath.equals("Dispositivo")){
    				TbDeviceType td = em.find(TbDeviceType.class, codeTypeId);
    				codeType = td.getDeviceTypeName();
    			}
				if((UserEJB.getUserByCode(code, codeTypeId)!= null && typePath.equals("Cliente")) || device.getDeviceByCode(codeTypeId, code)!= null){
	    			path = systemParametersValue.trim()+"/"+code+"-"+codeType;
				    System.out.println("the name you have entered is a directory  : "  +    path);  
	    			java.io.File directory = new java.io.File(path);
	    			if (directory.isDirectory()) {
					 //It returns the absolute path of a directory.
					 System.out.println("the path is "  + directory.getAbsolutePath());
					 this.setShowFile(true);
					 java.io.File[] strFilesDirs = directory.listFiles ( );  				
					 files = new ArrayList<File>();  
	    				 for ( int i = 0 ; i < strFilesDirs.length ; i ++ ) {
	    		     		 if ( strFilesDirs[i].isDirectory ( ) ){	
	    		     		 	 System.out.println ( "Directory: " + strFilesDirs[i] ) ;
	    		     		 }else if ( strFilesDirs[i].isFile ( )  && !strFilesDirs[i].getName().toLowerCase().contains("thumbs")){
	    		     		 	 System.out.println ( "File: " + strFilesDirs[i] + " (" + strFilesDirs[i].length ( ) + ")"  ) ;	    		     		 	 
	    		     		 	java.io.File f = strFilesDirs[i];    		     		 	    		 	
	    		     		 	File file = new File(f);
	    		     		 	file.setLength(f.length());
	    		     		 	file.setName(f.getName());
	    		     		 	try {
	    		     		 		file.setData(FileUtil.readBytes(f));
	    						} catch (IOException e) {
	    							e.printStackTrace();
	    						}
	    		     		 	 files.add(file);
	    		     		 }
	    				 }
					} else {
		 			    System.out.println("No tiene documentos relacionados.");
		    			setMessageInfo("No tiene documentos relacionados.");
		    			this.setShowModal(true);
						this.setShowFile(false);
					}
	    		} else{
	    			//The selected code does not exist
	    			setShowInfo(false);
	    			setMessageInfo("El No. de Identificación del " + typePath +" no existe. Verifique.");
	    			this.setShowModal(true);
					this.setShowFile(false);
	    		}
    		} else{
    			setMessageInfo("Digite No. de Identificación del " + typePath +".");
    			this.setShowModal(true);
				this.setShowFile(false);
    		}
		 }else{
 				setMessageInfo("Error al descargar documentos. Comuníquese con el Administrador."); 
    			this.setShowModal(true);
 			    System.out.println("Error:La carpeta no existe o no esta compartida. Verifique.");
 		 }
	}
	
	
	public void downLoadFile() throws IOException { 
       FacesContext context = FacesContext.getCurrentInstance();  
       HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();  
       
       String path = "";
	   String codeType = "";
       String systemParametersValue = SystemParametersEJB.getParameterName(typePath);
       
    	if (systemParametersValue != null) {
				if(typePath.equals("Cliente")){
    				TbCodeType tc = em.find(TbCodeType.class, codeTypeId);
    				codeType = tc.getCodeTypeAbbreviation();
    			}else if(typePath.equals("Dispositivo")){
    				TbDeviceType td = em.find(TbDeviceType.class, codeTypeId);
    				codeType = td.getDeviceTypeName();
    			}
    			path = systemParametersValue.trim()+"/"+code+"-"+codeType;
    			java.io.File directory = new java.io.File(path);
     			boolean isDirectory = directory.isDirectory();
    			if (isDirectory) {
    				 // It returns true if directory is a directory.
    				 System.out.println("the name you have entered is a directory  : "  +    directory);  
    				 //It returns the absolute path of a directory.
    				 System.out.println("the path is "  + directory.getAbsolutePath()+"/"+name);

    				     java.io.File  f = new java.io.File(directory+"/"+name) ;
    				     File file = new File(f);
		     		     file.setLength(f.length());
		     		 	 file.setName(f.getName());
		     		 	 
  		     		 	if (!f.exists()) {  
  		                    response.sendError(HttpServletResponse.SC_NOT_FOUND);  
  		                   return;  
  		               }  
  		     		   response.reset();  
		               response.setBufferSize(DEFAULT_BUFFER_SIZE);
		               MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
		               String mimeType = mimeTypesMap.getContentType(f.getName());
		               System.out.println("mimeType: "+mimeType);
		               response.setContentType(mimeType);   		               
		               response.setHeader("Content-Length", String.valueOf(f.length()));  
		               response.setHeader("Content-Disposition", "attachment;filename=\""+ f.getName() + "\"");  

  		 		       FileInputStream fileInputStream = null;
  					   PrintWriter out = response.getWriter();
  		               try {  
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
  		            }  
	    }else{
			//The file does not exist
			setShowMessage(true);
			setShowInfo(false);
			setMessageInfo("Error al descargar documentos. Comuníquese con el Administrador.");
	    }	
    }
	
	public void setCodeTypeList(List<SelectItem> codeTypeList) {
		this.codeTypeList = codeTypeList;
	}


	public List<SelectItem> getCodeTypeList() {
		if(codeTypeList == null){
			codeTypeList = new ArrayList<SelectItem>();
		} else {
			codeTypeList.clear();
		}
		this.clearFilter();
		if(typePath.equals("Cliente")){
			for(TbCodeType c : UserEJB.getCodeTypes()) {
				codeTypeList.add(new SelectItem(c.getCodeTypeId(), c.getCodeTypeAbbreviation()));
			}
		}else if(typePath.equals("Dispositivo")){
			for(TbDeviceType d : device.getDevicesTypes()) {
				codeTypeList.add(new SelectItem(d.getDeviceTypeId(), d.getDeviceTypeName()));
			}
		}
		return codeTypeList;
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
	
	/**
	 * Clears Data
	 */
	public String clearData() {
		setShowMessage(false);
		setShowInfo(false);
		setMessageInfo("");
        files.clear();
        refresh();
        return null;
    }
	
	/**
	 * 
	 * @param suggest
	 * @return
	 */
	public List<String> autocomplete(Object suggest) {
		String pref = (String) suggest;
		ArrayList<String> result = new ArrayList<String>();	
	
		Iterator<String> iterator = listClient.iterator();
		while (iterator.hasNext()) {
			String elem = ((String) iterator.next());
			if ((elem != null && elem.toLowerCase().indexOf(pref.toLowerCase()) == 0)
					|| "".equals(pref)) {
				result.add(elem);
			}
		}
		return result;
	}
	
	/**
	 * Gets the path file object selected
	 * @return TbPathFile
	 */
	private String getSelectedCode(){
		for(String a: listClient){
			if(a.equalsIgnoreCase(code.trim())){
				return a;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @return file list size
	 */
	public int getSize() {
		 if (getFiles().size()>0){
		 	 return getFiles().size();
		 }else  {
	        return 0;
	    }
	}
	
	
	/**
	 * @param userCode the userCode to set
	 */
	public void setUserCode(String userCode) {
	}

	/**
	 * @return the userCode
	 */
	public String getUserCode() {
		String userCode=UserEJB.getSystemUser(usrs.getUserId()).getUserCode();
		return userCode;
	}
	
	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}
	
	/**
	 * @param files the files to set
	 */
	public void setFiles(ArrayList<File> files) {
		this.files = files;
	}

	/**
	 * @return the files
	 */
	public ArrayList<File> getFiles() {
		return files;
	}
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	/**
	 * @param showModal the showModal to set
	 */
	public void setShowModal(boolean showModal) {
		this.showModal = showModal;
	}

	/**
	 * @return the showModal
	 */
	public boolean isShowModal() {
		return showModal;
	}
	
	/**
	 * @param modalMessage the modalMessage to set
	 */
	public void setModalMessage(String modalMessage) {
		this.modalMessage = modalMessage;
	}

	/**
	 * @return the modalMessage
	 */
	public String getModalMessage() {
		return modalMessage;
	}
	
	/**
	 * @param corfirmMessage the corfirmMessage to set
	 */
	public void setCorfirmMessage(String corfirmMessage) {
		this.corfirmMessage = corfirmMessage;
	}

	/**
	 * @return the corfirmMessage
	 */
	public String getCorfirmMessage() {
		return corfirmMessage;
	}

	/**
	 * @param messageInfo the messageInfo to set
	 */
	public void setMessageInfo(String messageInfo) {
		this.messageInfo = messageInfo;
	}

	/**
	 * @return the messageInfo
	 */
	public String getMessageInfo() {
		return messageInfo;
	}

	/**
	 * @param showInfo the showInfo to set
	 */
	public void setShowInfo(boolean showInfo) {
		this.showInfo = showInfo;
	}

	/**
	 * @return the showInfo
	 */
	public boolean isShowInfo() {
		return showInfo;
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
	 * @return the typePathList
	 */
	public List<SelectItem> getTypePathList() {
		if(typePathList==null){
			typePathList = new ArrayList<SelectItem>();	
			typePathList.add(new SelectItem("-1", "--Seleccione Uno--"));
			for(TbSystemParameter t: SystemParametersEJB.getListPath()){
				typePathList.add(new SelectItem(t.getSystemParameterName(), t.getSystemParameterName()));
			}
		}
		return typePathList;
	}
	
	/**
	 * @param typePathList the typePathList to set
	 */
	public void setTypePathList(List<SelectItem> typePathList) {
		this.typePathList = typePathList;
	}


	
	/**
	 * @param typePath the typePath to set
	 */
	public void setTypePath(String typePath) {
		this.typePath = typePath;
	}

	/**
	 * @return the typePath
	 */
	public String getTypePath() {
		return typePath;
	}
	
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param showC the showC to set
	 */
	public void setShowC(boolean showC) {
		this.showC = showC;
	}

	/**
	 * @return the showC
	 */
	public boolean isShowC() {
		return showC;
	}

	/**
	 * @param showD the showD to set
	 */
	public void setShowD(boolean showD) {
		this.showD = showD;
	}

	/**
	 * @return the showD
	 */
	public boolean isShowD() {
		return showD;
	}

	/**
	 * @param show the show to set
	 */
	public void setShow(boolean show) {
		this.show = show;
	}

	/**
	 * @return the show
	 */
	public boolean isShow() {
		return show;
	}


	/**
	 * @param listClient the listClient to set
	 */
	public void setListClient(List<String> listClient) {
		this.listClient = listClient;
	}


	/**
	 * @return the listClient
	 */
	public List<String> getListClient() {
		return listClient;
	}


	/**
	 * @param suggest the suggest to set
	 */
	public void setSuggest(String suggest) {
		this.suggest = suggest;
	}


	/**
	 * @return the suggest
	 */
	public String getSuggest() {
		return suggest;
	}
	
	public void setCodeTypeId(Long codeTypeId) {
		this.codeTypeId = codeTypeId;
	}


	public Long getCodeTypeId() {
		return codeTypeId;
	}

	public void setShowFile(boolean showFile) {
		this.showFile = showFile;
	}

	public boolean isShowFile() {
		return showFile;
	}

 } 

