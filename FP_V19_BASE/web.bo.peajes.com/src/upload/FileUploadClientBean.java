package upload;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.Application;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import jpa.TbTypeDocument;

import security.UserLogged;
import sessionVar.SessionUtil;

import ejb.Document;
import ejb.SystemParameters;
import ejb.User;

public class FileUploadClientBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@EJB(mappedName="ejb/User")
	private User userEJB;
	
	@EJB(mappedName ="ejb/SystemParameters")
	private SystemParameters SystemParametersEJB;
	
	@EJB(mappedName="ejb/Document")
	private Document DocumentEJB;
	
	// Attributes-------------------//
	private ArrayList<File> files = new ArrayList<File>();
	
	private boolean autoUpload = false;
	private boolean useFlash = false;		
	private boolean showMessage;
	private boolean showDataDocumentTPyOtro;
	private boolean showDataDocumentContract;
	private String message;

	private List<SelectItem> documentList;
	private Long documentTypeId=-1L;
	
	private UserLogged usrs;
	
	public FileUploadClientBean(){
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
	public void clearUploadData() {
        files.clear();
		documentTypeId=-1L;
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
	 * Método encargado de guardar en la ruta configurada (parámetro 28) documentos tipo: tarjeta de propiedad y otros.
	 * @author psanchez
	 */
	public void saveDocumentTPyOtro(UploadEvent event){	
       try {       	
        	String path ="";
        	String systemParametersValue = SystemParametersEJB.getParameterValueById(28L);
        	Long userId=userEJB.getSystemMasterById(usrs.getUserId());
     	    String userCode=userEJB.getSystemUserCode(userId);
     	    String codeType=userEJB.getDocumentClient(userCode, userId, SessionUtil.ip(), userId);
     	     
    		if(systemParametersValue != null) {
			   if(userCode!= null || userId!= null) {
				   if(documentTypeId.longValue()==2 || documentTypeId.longValue()==3){
	        			UploadItem item = event.getUploadItem();
	        			String uploadedFileName = FileUtil.trimFilePath(item.getFileName());
	            		path = systemParametersValue.trim()+"/"+userCode+"-"+codeType;
	        			java.io.File uniqueFile = FileUtil.uniqueFile(new java.io.File(path), uploadedFileName);
	        			System.out.println("path:"+path+";uploadedFileName:"+uploadedFileName);
	        			FileUtil.write(uniqueFile, item.getData());
	
	        			File file = new File(uniqueFile);
	        			file.setLength(item.getData().length);
	        			file.setName(item.getFileName());
	        			file.setData(item.getData());
	        			files.add(file);
	        			if(DocumentEJB.insertDocument(userId, documentTypeId, 4L, uploadedFileName, uniqueFile.toString(), 
	        						SessionUtil.ip(), SessionUtil.sessionUser().getUserId())){
	        				this.setShowMessage(true);
	        				this.setMessage("El documento ha sido enviado y será validado por FacilPass.");
	        			}else{
	        				this.setShowMessage(true);
		        			this.setMessage("Error al guardar el documento. Comuníquese con el Administrador.");
	        			}
				  }else {
				     this.setShowMessage(true);
					 this.setMessage("Seleccione Tipo de Documento.");
			      }
		        }else{
		    	   this.setShowMessage(true);
				   this.setMessage("Error al obtener datos del cliente. ");
		        }
			}else{
				this.setShowMessage(true);
				this.setMessage("Error en el envío de documentos. Comuníquese con el Administrador."); 
		    	System.out.println("Error:La carpeta no existe o no esta compartida. Verifique.");
			}
        } catch (IOException ex) {
        	System.out.println(" [ Error en FileUploadClientBean.saveDocumentTPyOtro : IOException] "+ex.getMessage());
        	this.setShowMessage(true);
			this.setMessage("Error en el envío de documentos. Comuníquese con el Administrador.");
            ex.printStackTrace();
        }  catch (Exception e){
        	System.out.println(" [ Error en FileUploadClientBean.saveDocumentTPyOtro : Exception] "+e.getMessage());
        	this.setShowMessage(true);
			this.setMessage("El documento ha sido enviado y será validado por FacilPass.");  
        	e.printStackTrace();
        }
	} 
	
	/**
	 * Método encargado de guardar en la ruta configurada (parámetro 28) documentos tipo: contrato FacilPass.
	 * @author psanchez
	 */
	public void saveDocumentContract(UploadEvent event){	
       try {       	
        	String path ="";
        	String systemParametersValue = SystemParametersEJB.getParameterValueById(28L); 
        	Long userId=userEJB.getSystemMasterById(usrs.getUserId());
     	    String userCode=userEJB.getSystemUserCode(userId);
     	    String codeType=userEJB.getDocumentClient(userCode, userId, SessionUtil.ip(), userId);
     	    
    		if(systemParametersValue != null) {
			   if(userCode!= null || userId!= null) {
				   if(documentTypeId.longValue()==1){
	        			UploadItem item = event.getUploadItem();
	        			String uploadedFileName = FileUtil.trimFilePath(item.getFileName());
	            		path = systemParametersValue.trim()+"/"+userCode+"-"+codeType;
	            		System.out.println("path:"+path+";uploadedFileName:"+uploadedFileName);
	        			java.io.File uniqueFile = FileUtil.uniqueFile(new java.io.File(path), uploadedFileName);
	        			FileUtil.write(uniqueFile, item.getData());
	
	        			File file = new File(uniqueFile);
	        			file.setLength(item.getData().length);
	        			file.setName(item.getFileName());
	        			file.setData(item.getData());
	        			files.add(file);	
	        			
	        			if(DocumentEJB.insertDocument(userId, documentTypeId, 4L, item.getFileName(), uniqueFile.toString(), 
	        						SessionUtil.ip(), SessionUtil.sessionUser().getUserId())){
		        			this.setShowMessage(true);
		        			this.setMessage("El documento ha sido enviado y será validado por FacilPass.");
		        			this.setShowMessage(true);
		        			this.setMessage("El documento ha sido enviado y será validado por FacilPass.");
	        			}else{
	        				this.setShowMessage(true);
		        			this.setMessage("Error al guardar el documento. Comuníquese con el Administrador.");
	        			}
				  }else {
				     this.setShowMessage(true);
					 this.setMessage("Seleccione Tipo de Documento.");
			      }
		        }else{
			    	this.setShowMessage(true);
					this.setMessage("Error al obtener datos del cliente.");
		        }
			}else{
				this.setShowMessage(true);
				this.setMessage("Error en el envío de documentos. Comuníquese con el Administrador."); 
		    	System.out.println("Error:La carpeta no existe o no esta compartida. Verifique.");
			}
        } catch (IOException ex) {
        	System.out.println(" [ Error en FileUploadClientBean.saveDocumentContract : IOException] "+ex.getMessage());
        	this.setShowMessage(true);
			this.setMessage("Error en el envío de documentos. Comuníquese con el Administrador.");
            ex.printStackTrace();
        }  catch (Exception e){
        	System.out.println(" [ Error en FileUploadClientBean.saveDocumentContract : Exception] "+e.getMessage());
			this.setShowMessage(true);
			this.setMessage("Error en el envío de documentos. Comuníquese con el Administrador.");  
        	e.printStackTrace();
        }
	} 
	
	/**
	 * Método encargado validar el panel para subir documentos de acuerdo al tipo de documento seleccionado.
	 * @author psanchez
	 */
	public void chargeDocument() {
	  this.setShowDataDocumentContract(false);
	  this.setShowDataDocumentTPyOtro(false);
	  if(documentTypeId.longValue()==1){
			 this.setShowDataDocumentContract(true);
		}else if(documentTypeId.longValue()==2 || documentTypeId.longValue()==3){
			 this.setShowDataDocumentTPyOtro(true);
		}
	}

	// Getters and Setters --------

	public void setShowMessage(boolean showMessage) {
		this.showMessage = showMessage;
	}


	public boolean isShowMessage() {
		return showMessage;
	}

	
	public void setMessage(String message) {
		this.message = message;
	}


	public String getMessage() {
		return message;
	}
	
	public void hideModal(){
		this.setMessage("");
		this.setShowMessage(false);
		this.setDocumentTypeId(-1L);
		this.setShowDataDocumentTPyOtro(false);
		this.setShowDataDocumentContract(false);
    	this.clearUploadData();
    	this.refresh();
	}


	public void setDocumentList(List<SelectItem> documentList) {
		this.documentList = documentList;
	}


	public List<SelectItem> getDocumentList() {
		if(documentList == null){
			documentList = new ArrayList<SelectItem>();
		}else{
			documentList.clear();
		}
		documentList.add(new SelectItem(-1L, "--Seleccione Tipo Documento--"));
		for(TbTypeDocument document : DocumentEJB.getDocumentList()){
			documentList.add(new SelectItem(document.getTypeDocumentId(), document.getTypeDocumentDescription()));
		}
		return documentList;
	}


	public void setDocumentTypeId(Long documentTypeId) {
		this.documentTypeId = documentTypeId;
	}


	public Long getDocumentTypeId() {
		return documentTypeId;
	}


	public void setAutoUpload(boolean autoUpload) {
		this.autoUpload = autoUpload;
	}


	public boolean isAutoUpload() {
		return autoUpload;
	}


	public void setUseFlash(boolean useFlash) {
		this.useFlash = useFlash;
	}


	public boolean isUseFlash() {
		return useFlash;
	}
	
	
	public ArrayList<File> getFiles() {
		return files;
	}

	
	public void setFiles(ArrayList<File> files) {
		this.files = files;
	}

	
	public void setUsrs(UserLogged usrs) {
		this.usrs = usrs;
	}

	
	public UserLogged getUsrs() {
		return usrs;
	}
	
	
	public void setShowDataDocumentTPyOtro(boolean showDataDocumentTPyOtro) {
		this.showDataDocumentTPyOtro = showDataDocumentTPyOtro;
	}

	
	public boolean isShowDataDocumentTPyOtro() {
		return showDataDocumentTPyOtro;
	}

	
	public void setShowDataDocumentContract(boolean showDataDocumentContract) {
		this.showDataDocumentContract = showDataDocumentContract;
	}

	
	public boolean isShowDataDocumentContract() {
		return showDataDocumentContract;
	}

}
