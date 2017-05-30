package upload;

import java.io.IOException;  
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.Application;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;  
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import ejb.SystemParameters;
import ejb.Device;
import ejb.User;

import jpa.TbSystemParameter;

import security.UserLogged;
import sessionVar.SessionUtil;



public class FileViewerBean implements Serializable {  
	private static final long serialVersionUID = -1L;	
	
	/** Atributos **/
	private ArrayList<File> files = new ArrayList<File>();
	private List<String> list;
	private List<SelectItem> typePathList;
    private String typePath = "";

	private UserLogged usrs;
	private String name;
	private String code;
	private long length;
	
	//String url= "c:\\upload\\";
	
	/** Control Modal **/
	private boolean show;
	private boolean showModal;
	private boolean showInfo;
	private boolean showMessage;
	private String corfirmMessage;
	private String modalMessage;
	private String messageInfo;

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
	public FileViewerBean (){
		usrs = (UserLogged) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
		list = new ArrayList<String>();
	}
	
	/**
	 * Hides modal windows.
	 */
	public String hideModal(){
		this.setShowModal(false);
		this.setModalMessage(null);
		return null;
	}
	
	/**
	 * Gets the files to show.
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
            	System.out.println("FileViewerBean.viewFiles.path-->"+path);
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
        			setMessageInfo("El Número de identificación ingresado no tiene documentos relacionados.");
    			}
    		} else{
    			//The selected code does not exist
    			setShowMessage(true);
    			setShowInfo(false);
    			setMessageInfo("El Número de identificación de " + typePath +" no Existe. Verifique.");
    			files.clear();
    		}
		 }else{
			//The file does not exist
 			setShowMessage(true);
 			setShowInfo(false);
 			setMessageInfo("Error al ver documentos. Comuníquese con el Administrador."); 
		    System.out.println("Error:La carpeta no existe o no esta compartida. Verifique.");
 			files.clear();
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
            	System.out.println("FileViewerBean.viewFilesClient.path-->"+path);
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
        			setMessageInfo("El Número de identificación no tiene documentos relacionados.");
    			}
    		} else{
    			//The selected code does not exist
    			files.clear();
    		}
    	}else{
    		setShowMessage(true);
 			setShowInfo(false);
 			setMessageInfo("Error al ver documentos. Comuníquese con el Administrador."); 
		    System.out.println("Error:La carpeta no existe o no esta compartida. Verifique.");
    	}
		return null;
	}

	
	/**
	 * Loads the list
	 */
	public void loadList(ValueChangeEvent event){		
		list.clear();
		typePath = event.getNewValue().toString();
		setShow(true);
		setCode("");
		if (typePath.equals("Cliente")) {
			list = UserEJB.getClients();
		} else if (typePath.equals("Dispositivo")) {
			list = device.getAllDevices();
		} else {
			setShow(false);
		}
		clearData();
		refresh();
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
	 * @param stream
	 * @param object
	 */
	public void paint(OutputStream stream, Object object) {
	        try {
				stream.write(getFiles().get((Integer)object).getData());
			} catch (IOException e) {
				System.out.println(" [ Error en FileUploadBean.paint : IOException] ");
				e.printStackTrace();
			}
	}
	
	/**
	 * 
	 * @param suggest
	 * @return
	 */
	public List<String> autocomplete(Object suggest) {
		String pref = (String) suggest;
		ArrayList<String> result = new ArrayList<String>();	
	
		Iterator<String> iterator = list.iterator();
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
		for(String a: list){
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
			typePathList.add(new SelectItem("-1", "-Seleccione Uno-"));
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


 } 

