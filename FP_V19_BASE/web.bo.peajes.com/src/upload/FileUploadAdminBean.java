package upload;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.Application;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollerEvent;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import jpa.TbTypeDocument;

import sessionVar.SessionUtil;
import util.AllInfoClient;

import ejb.Document;
import ejb.SystemParameters;
import ejb.User;

/**
 * @author psanchez
 *
 */
public class FileUploadAdminBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@EJB(mappedName="ejb/User")
	private User userEJB;
		
	@EJB(mappedName ="ejb/SystemParameters")
	private SystemParameters SystemParametersEJB;
	
	@EJB(mappedName="ejb/Document")
	private Document documentEJB;
	
	// Attributes-------------------//
	private ArrayList<File> files = new ArrayList<File>();
	private List<AllInfoClient> listAllInfoClients;
	private List<Integer> listaScroll;
	
	private boolean autoUpload = false;
	private boolean useFlash = false;		
	private boolean searchok = false;
	private boolean showMessage;
	private boolean showChargeOtherDocument;
	private boolean showChargeContractDocument;
	private boolean showDataTypeDocument;
	private String message;
	private String numberDoc;
	private String secondName;
	private String userName;
	private String placa;
	private String tagId;
	private String facial;
	private String filtroDescription;
	private String systemParametersValue;
	
	private Long accountId;
	private Long userId;
	
	private List<SelectItem> documentList;
	private Long documentTypeId=-1L;
	
	private int filtroId;
	private int page=1;
	private int valuesFor;
	
	/**
	 * Constructor.
	 */
	public FileUploadAdminBean(){
		super();
	}
	
	@PostConstruct
	public void init(){
       	systemParametersValue = SystemParametersEJB.getParameterValueById(28L);
		this.clearFilter();
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
		userId=null;
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
	 * Método encargado mostrar el panel para subir documentos de acuerdo al tipo de documento seleccionado.
	 * @author psanchez
	 */
	public void chargeDocument() {
	  this.setShowChargeContractDocument(false);
	  this.setShowChargeOtherDocument(false);
		if(documentTypeId.longValue()==1){
			 this.setShowChargeContractDocument(true);
		}else if(documentTypeId.longValue()==2 || documentTypeId.longValue()==3){
			 this.setShowChargeOtherDocument(true);
		}
	}
	
	public void validateDocument() {
		this.setShowChargeContractDocument(false);
		this.setShowChargeOtherDocument(false);
		this.setShowDataTypeDocument(false);
		this.setDocumentTypeId(-1L);
		if(userId!= null){
			  this.setShowDataTypeDocument(true);
		 }else{
			this.setShowMessage(true);
		    this.setMessage("Error al obtener datos del cliente.");
			this.setShowDataTypeDocument(false);
		 }
	}
	
	
	/**
	 * Método encargado de guardar en el servidor documentos tipo: tarjeta de propiedad y otros.
	 * @author psanchez
	 */
	public void chargeOtherDocument(UploadEvent event){
      try {   
        	String path ="";
        	String codeType=userEJB.getDocumentClient(numberDoc, userId, SessionUtil.ip(), SessionUtil.sessionUser().getUserId());

    		if(systemParametersValue != null) {
			   if(userId!= null && numberDoc!= null) {
				    if(documentTypeId.longValue()==2 || documentTypeId.longValue()==3){
	        			UploadItem item = event.getUploadItem();
	        			String uploadedFileName = FileUtil.trimFilePath(item.getFileName());
	            		path = systemParametersValue.trim()+"/"+numberDoc+"-"+codeType;
	        			java.io.File uniqueFile = FileUtil.uniqueFile(new java.io.File(path), uploadedFileName);
	        			System.out.println("FileUploadAdminBean.saveDocument.path:"+path+";uploadedFileName:"+uploadedFileName);
	        			FileUtil.write(uniqueFile, item.getData());

	        			File file = new File(uniqueFile);
	        			file.setLength(item.getData().length);
	        			file.setName(item.getFileName());
	        			file.setData(item.getData());
	        			files.add(file);	
	        			if(documentEJB.insertDocument(userId, documentTypeId, 4L, uploadedFileName,
		        				uniqueFile.toString(), SessionUtil.ip(), SessionUtil.sessionUser().getUserId())){ 
		        			this.setShowMessage(true);
		        			this.setMessage("El documento ha sido enviado y será validado por FacilPass.");
		        			this.clearFilter();
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
		    	this.refresh();
				System.out.println("Error:La carpeta no existe o no esta compartida. Verifique.");
			}
        } catch (IOException ex) {
        	System.out.println(" [ Error en FileUploadAdminBean.saveDocument : IOException] ");
        	this.setShowMessage(true);
			this.setMessage("Error al guardar el documento. Comuníquese con el Administrador.");
            ex.printStackTrace();
        }  catch (Exception e){
        	System.out.println(" [ Error en FileUploadBean.saveDocument : Exception] ");
        	e.printStackTrace();
			this.setShowMessage(true);
			this.setMessage("Error al guardar el documento. Comuníquese con el Administrador.");  
        }
	}
	
	/**
	 * Método encargado de guardar en el servidor documentos tipo: contrato FacilPass.
	 * @author psanchez
	 */
	public void chargeContractDocument(UploadEvent event){	
	   try {   
       	String path ="";
    	String codeType=userEJB.getDocumentClient(numberDoc, userId, SessionUtil.ip(), SessionUtil.sessionUser().getUserId());

   		if(systemParametersValue != null) {
			   if(userId!= null && numberDoc!= null) {
				    if(documentTypeId.longValue()==1){
	        			UploadItem item = event.getUploadItem();
	        			String uploadedFileName = FileUtil.trimFilePath(item.getFileName());
	            		path = systemParametersValue.trim()+"/"+numberDoc+"-"+codeType;
	        			java.io.File uniqueFile = FileUtil.uniqueFile(new java.io.File(path), uploadedFileName);
	        			System.out.println("FileUploadAdminBean.listener.path:"+path+";uploadedFileName:"+uploadedFileName);
	        			FileUtil.write(uniqueFile, item.getData());

	        			File file = new File(uniqueFile);
	        			file.setLength(item.getData().length);
	        			file.setName(item.getFileName());
	        			file.setData(item.getData());
	        			files.add(file);	
	        			if(documentEJB.insertDocument(userId, documentTypeId, 4L, uploadedFileName,
	        					uniqueFile.toString(), SessionUtil.ip(), SessionUtil.sessionUser().getUserId())){
		        			this.setShowMessage(true);
		        			this.setMessage("El documento ha sido enviado y será validado por FacilPass.");
		        			this.clearFilter();
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
		    	this.refresh();
		    	System.out.println("Error:La carpeta no existe o no esta compartida. Verifique.");
			}
       } catch (IOException ex) {
       		System.out.println(" [ Error en FileUploadBean.chargeContractDocument : IOException] ");
       		this.setShowMessage(true);
			this.setMessage("Error en el envío de documentos. Comuníquese con el Administrador.");
			ex.printStackTrace();
       }  catch (NullPointerException ex){
          	System.out.println(" [ Error en FileUploadBean.chargeContractDocument : NullPointerException] ");
           	ex.printStackTrace();
			this.setShowMessage(true);
			this.setMessage("Error en el envío de documentos. Comuníquese con el Administrador.");  
       }catch (Exception e){
       		System.out.println(" [ Error en FileUploadBean.chargeContractDocument : Exception] ");
       		e.printStackTrace();
       		this.setShowMessage(true);
       		this.setMessage("Error en el envío de documentos. Comuníquese con el Administrador.");  
       }
	}
	
	
	public void searchFilter(){
	    try{
			if(postValidationSearch()){
			    setPage(1);
		    	List<AllInfoClient> listfilter = null;
				listfilter = userEJB.getInfoClientsByFilters(page, 4, filtroId, filtroDescription);
				if(listfilter.size()<=0){
					this.setShowMessage(true);
				    this.setMessage("No se encontraron resultados de la búsqueda.");
					this.listAllInfoClients.clear();
					this.setSearchok(false);
				}else{
					this.listAllInfoClients = listfilter;
					this.setShowMessage(false);
					this.setSearchok(true);
				}
			 }
	    } catch (Exception e) {
			   System.out.println(" [ Error en ValidateDocumentBean.searchFilter ] ");
			   this.setMessage("Error en transación");
			   this.setShowMessage(true);
			   e.printStackTrace();
		 } 
	   } 
	
	
	private boolean postValidationSearch() {
		if(filtroDescription!="" && (!filtroDescription.matches("([a-z]|[A-Z]|[áéíóúü]|[ÁÉÍÓÚÜ]|[0-9]|[&/]|[_-]|[.]|[ñÑ]|\\s)+"))){
			this.setMessage("El dato ingresado tiene caracteres inválidos. Verifique.");
			this.setShowMessage(true);
			return false;
		}
		else if (filtroDescription.trim().length()==0 && (filtroId>=0) || 
			    (filtroDescription.trim().length()>0 && filtroId==0)){
		    	this.setShowMessage(true);
			    this.setMessage("Seleccione el filtro e ingrese datos para realizar la búsqueda.");
			return false;
		}
		return true;		
	}

	public void clearFilter(){
		filtroDescription = "";
		filtroId = 0;
		this.setPage(1);
		this.setSearchok(false);
		this.getListAllInfoClients();
	}
	
	public void getDataForScroll(){
	    try {
			if(searchok==false){
				setValuesFor(Integer.parseInt(String.valueOf(userEJB.getInfoClientsByFilters(0, 4, 0, "").get(0))));
			}else{
				this.setValuesFor(Integer.parseInt(String.valueOf(userEJB.getInfoClientsByFilters(0, 4, filtroId, filtroDescription).get(0))));
			}
			listaScroll=new ArrayList<Integer>();
			for(int i=0;i<getValuesFor();i++){	
				listaScroll.add(i);
			}
		} catch (Exception e) {
	     	e.printStackTrace();
		}
	}
	
	public void dataScroller(ActionEvent event)throws AbortProcessingException {
		DataScrollerEvent events=(DataScrollerEvent)event;
		page = events.getPage();
		setPage(1);
	}
	
	
	public void hideModal(){
		this.setMessage("");
		this.setShowMessage(false);
		this.setShowChargeOtherDocument(false);
		this.setShowChargeContractDocument(false);
		this.setShowDataTypeDocument(false);
		this.clearUploadData();
	}

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
	

	public void setNumberDoc(String numberDoc) {
		this.numberDoc = numberDoc;
	}

	public String getNumberDoc() {
		return numberDoc;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Long getAccountId() {
		return accountId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public void setDocumentList(List<SelectItem> documentList) {
		this.documentList = documentList;
	}

	/**
	 * @return the documentList
	 */
	public List<SelectItem> getDocumentList() {
		if(documentList == null){
			documentList = new ArrayList<SelectItem>();
		}else{
			documentList.clear();
		}
		documentList.add(new SelectItem(-1L, "--Seleccione Tipo Documento--"));
		for(TbTypeDocument document : documentEJB.getDocumentList()){
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

	public void setShowChargeOtherDocument(boolean showChargeOtherDocument) {
		this.showChargeOtherDocument = showChargeOtherDocument;
	}

	public boolean isShowChargeOtherDocument() {
		return showChargeOtherDocument;
	}

	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public String getTagId() {
		return tagId;
	}

	public void setFacial(String facial) {
		this.facial = facial;
	}

	public String getFacial() {
		return facial;
	}


	public List<AllInfoClient> getListAllInfoClients() {
       	if(listAllInfoClients == null) {
			listAllInfoClients = new ArrayList<AllInfoClient>();			
		}
		else if(searchok==false){
			 listAllInfoClients.clear(); 
		}
		if(searchok==false){
			this.getDataForScroll();
			listAllInfoClients = userEJB.getInfoClientsByFilters(page, 4, 0, "");
		}
		if (searchok==true){
            this.getDataForScroll();
		    listAllInfoClients = userEJB.getInfoClientsByFilters(page, 4, filtroId, filtroDescription);
		}
		return listAllInfoClients;
	}

	public void setFiltroId(int filtroId) {
		this.filtroId = filtroId;
	}

	public int getFiltroId() {
		return filtroId;
	}

	public void setFiltroDescription(String filtroDescription) {
		this.filtroDescription = filtroDescription;
	}

	public String getFiltroDescription() {
		return filtroDescription;
	}

	public void setShowChargeContractDocument (boolean showChargeContractDocument) {
		this.showChargeContractDocument = showChargeContractDocument;
	}

	public boolean isShowChargeContractDocument () {
		return showChargeContractDocument ;
	}

	public void setShowDataTypeDocument(boolean showDataTypeDocument) {
		this.showDataTypeDocument = showDataTypeDocument;
	}

	public boolean isShowDataTypeDocument() {
		return showDataTypeDocument;
	}
	
	public void setListaScroll(List<Integer> listaScroll) {
		this.listaScroll = listaScroll;
	}

	public List<Integer> getListaScroll() {
		return listaScroll;
	}
	
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public void setValuesFor(int valuesFor) {
		this.valuesFor = valuesFor;
	}

	public int getValuesFor() {
		return valuesFor;
	}

	public void setSearchok(boolean searchok) {
		this.searchok = searchok;
	}

	public boolean isSearchok() {
		return searchok;
	}

}
