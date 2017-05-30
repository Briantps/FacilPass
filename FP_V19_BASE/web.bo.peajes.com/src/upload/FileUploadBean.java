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

import jpa.TbCodeType;
import jpa.TbSystemParameter;

import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import security.UserLogged;
import sessionVar.SessionUtil;

import ejb.SystemParameters;
import ejb.Device;
import ejb.User;


public class FileUploadBean implements Serializable{
	private static final long serialVersionUID = -1L;
	
	/** Atributos **/
	private ArrayList<File> files = new ArrayList<File>();
	private List<String> list;		
	private List<SelectItem> typePathList;
	private String typePath = "";
	
	private String code;	
	private UserLogged usrs;
	
	private int uploadsAvailable = 5; // Not in Use...Yet
		
	//String url= "c:\\upload\\";

	/** Control Modal **/
	private boolean autoUpload = false;
	private boolean useFlash = false;	
	private boolean showModal;
	private boolean show;
	private boolean showC;
	private boolean showD;
	private String modalMessage;
	
	
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
	public FileUploadBean() {
		usrs = (UserLogged) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
		list = new ArrayList<String>();
		getCode();
	}
	 	
	/**
	 * Método encargado de guardar en el servidor archivos del cliente o dispositivos.
	 * @author psanchez
	 */
	public void listener(UploadEvent event){		
       try {        	
        	String path ="";
        	String typePath1 = typePath;
        	String systemParametersValue = SystemParametersEJB.getParameterName(typePath1);
        	
    		if (systemParametersValue != null) {
        		String codeS = getSelectedCode();
        		System.out.println("typePath: " + typePath);
        		if(!typePath.equals("-1")){
        			if (codeS!= null) {
        				if(typePath.equals("Cliente")){
        					String code="";
        					String type="";
        					char[] a=codeS.toCharArray();
        					for(int i=0;i<a.length;i++){
        						if(a[i]=='-'){
        							code=codeS.substring(0,i);
        							type=codeS.substring(i+1);
        						}
        					}
        					List<TbCodeType> list= UserEJB.getCodeTypes();
        					for(int j=0;j<list.size();j++){
        						if(list.get(j).getCodeTypeAbbreviation().equals(type)){
        							UserEJB.setStateSystemUser(UserEJB.
        									getUserByCode(code, list.get(j).getCodeTypeId()).getUserId(), 4);
        						}
        					}
        					System.out.println("Se estan subiendo archivos de clientes");
        				}
            			path = systemParametersValue.trim()+"/"+codeS;
		            	System.out.println("FileUploadBean.listener.path-->"+path);
            			UploadItem item = event.getUploadItem();
            			
            			String uploadedFileName = FileUtil.trimFilePath(item.getFileName());
            			java.io.File uniqueFile = FileUtil.uniqueFile(new java.io.File(path), uploadedFileName);
            			FileUtil.write(uniqueFile, item.getData());

            			File file = new File(uniqueFile);
            			file.setLength(item.getData().length);
            			file.setName(item.getFileName());
            			file.setData(item.getData());
            			files.add(file);
            			
    					this.setShowModal(true);
    					this.setModalMessage("Los documentos han sido enviados y están pendientes para su verificación.");
    				}else{
    					this.setShowModal(true);
    					this.setModalMessage("Debe digitar el Número de identificación de " + typePath +". Verifique.");
    					clearUploadData();
    					refresh();
    				}
        		}
        		else{
					this.setShowModal(true);
					this.setModalMessage("Debe seleccionar el tipo de documento. Verifique.");
					clearUploadData();
					refresh();
				}
        			
    		}else{
     			//The file does not exist
    			this.setShowModal(true);
     			this.setModalMessage("Error en el envío de documentos. Comuníquese con el Administrador.");
		    	System.out.println("Error:La carpeta no existe o no esta compartida. Verifique.");
     			clearUploadData();
				refresh();
     		}		
        } catch (IOException ex) {
        	System.out.println(" [ Error en FileUploadBean.listener : IOException] ");
            ex.printStackTrace();
        }  catch (Exception e){
        	e.printStackTrace();
        }
	}  
	
	/**
	 * Método encargado de guardar en el servidor los archivos del cliente en sesión.
	 * @author psanchez
	 */
	public void listenerClient(UploadEvent event){		
       try {       	
        	String path ="";
        	String typePath = "Cliente";
        	String systemParametersValue = SystemParametersEJB.getParameterName(typePath);
        	
    		if (systemParametersValue != null) {
    			Long userId=UserEJB.getSystemMasterById(usrs.getUserId());
    		    String userCode=UserEJB.getSystemUserCode(userId);
        		String codeS = userCode;
        		if ( codeS!= null) {
        			UserEJB.setStateSystemUser(SessionUtil.sessionUser().getUserId(), 4);
        			path = systemParametersValue.trim()+"/"+codeS+"-"+UserEJB.getDocumentClient(codeS, userId, SessionUtil.ip(),SessionUtil.sessionUser().getUserId());
	            	System.out.println("FileUploadBean.listenerClient.path-->"+path);
        			UploadItem item = event.getUploadItem();
        			String uploadedFileName = FileUtil.trimFilePath(item.getFileName());
        			java.io.File uniqueFile = FileUtil.uniqueFile(new java.io.File(path), uploadedFileName);
        			FileUtil.write(uniqueFile, item.getData());

        			File file = new File(uniqueFile);
        			file.setLength(item.getData().length);
        			file.setName(item.getFileName());
        			file.setData(item.getData());
        			files.add(file);
        			
        			this.setShowModal(true);
					this.setModalMessage("Los documentos han sido enviados y están pendientes para su verificación.");
				}else{
					this.setShowModal(true);
					this.setModalMessage("Error en el envío de documentos. Comuníquese con el Administrador");
					clearUploadData();
					refresh();
				}
    		}else{
     			//The file does not exist
    			this.setShowModal(true);
     			this.setModalMessage("Error en el envío de documentos. Comuníquese con el Administrador.");
		    	System.out.println("Error:La carpeta no existe o no esta compartida. Verifique.");
     			clearUploadData();
				refresh();
     		}		
        } catch (IOException ex) {
        	System.out.println(" [ Error en FileUploadBean.listener : IOException] ");
            ex.printStackTrace();
        }  catch (Exception e){
        	e.printStackTrace();
        }
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
	 * Clears Uploaded Data
	 */
	public String clearUploadData() {
        files.clear();
        return null;
    }
	
	/**
	 * Hides modal windows.
	 */
	 public String hideModal(){
		setShowModal(false);
		setModalMessage(null);
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
	 * Loads the list
	 */
	public void loadList(ValueChangeEvent event){	
		list.clear();
		typePath = event.getNewValue().toString();
		setShow(true);
		setCode("");
		if (typePath.equals("Cliente")) {
			list = UserEJB.getClients();
			this.setShowD(false);
			this.setShowC(true);
		} else if (typePath.equals("Dispositivo")) {
			list = device.getAllDevices();
			this.setShowC(false);
			this.setShowD(true);
		} else {
			setShow(false);
			this.setShowC(false);
			this.setShowD(false);
		}
		clearUploadData();
		refresh();
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
	
	/**
	 * Gets the path file object selected
	 * @return TbPathFile
	 */
	private String getSelectedCode(){
		System.out.println(code);
		for(String a: list){
			if(a.equalsIgnoreCase(code.trim())){
				return a;
			}
		}
		return null;
	}
	
	public String test(){//don't remove
		return null;
	}
	// Getters and Setters --------
	
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
	 * 
	 * @return currentTimeMillis
	 */
	public long getTimeStamp(){
        return System.currentTimeMillis();
    }

	/**
	 * @return the files
	 */
	public ArrayList<File> getFiles() {
		return files;
	}

	/**
	 * @param files the files to set
	 */
	public void setFiles(ArrayList<File> files) {
		this.files = files;
	}

	/**
	 * @return the uploadsAvailable
	 */
	public int getUploadsAvailable() {
		return uploadsAvailable;
	}

	/**
	 * @param uploadsAvailable the uploadsAvailable to set
	 */
	public void setUploadsAvailable(int uploadsAvailable) {
		this.uploadsAvailable = uploadsAvailable;
	}

	/**
	 * @return the autoUpload
	 */
	public boolean isAutoUpload() {
		return autoUpload;
	}

	/**
	 * @param autoUpload the autoUpload to set
	 */
	public void setAutoUpload(boolean autoUpload) {
		this.autoUpload = autoUpload;
	}

	/**
	 * @return the useFlash
	 */
	public boolean isUseFlash() {
		return useFlash;
	}

	/**
	 * @param useFlash the useFlash to set
	 */
	public void setUseFlash(boolean useFlash) {
		this.useFlash = useFlash;
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
	 * @param show the show to set
	 */
	public void setShow(boolean show) {
		this.show = show;
	}

	/**
	 * @return the showLabel
	 */
	public boolean isShow() {
		return show;
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
	
}