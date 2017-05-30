package upload;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import ejb.Document;
import ejb.SystemParameters;
import ejb.User;

import security.UserLogged;
import sessionVar.SessionUtil;
import util.AllInfoVerification;


/**
 * @author psanchez
 *
 */
public class FileDownloadClientBean implements Serializable{
	private static final long serialVersionUID = -1L;
	private static final int DEFAULT_BUFFER_SIZE = 10240;  
	
	private ArrayList<File> files = new ArrayList<File>();
	
	@EJB(mappedName ="ejb/SystemParameters")
	private SystemParameters SystemParametersEJB;
	
	@EJB(mappedName="ejb/Document")
	private Document documentEJB;
	
	@EJB(mappedName="ejb/User")
	private User userEJB;
	
	// Attributes-------------------//
	private List<AllInfoVerification> listAllInfoVerification;
	private Long documentId;
	
	private String state;
	private String url;
	private Long codeUser;
	private Long userId;
	private UserLogged usrs;
	
	// Control visibility ----//
	private String modalMessage;
	private boolean showModal;
		
	/**
	 * Constructor.
	 */
	public FileDownloadClientBean(){
		usrs = (UserLogged) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
	}
	
	@PostConstruct
	public void init() {
		userId = userEJB.getSystemMasterById(usrs.getUserId());	
	}
	
	/**
	 * Método encargado de descargar de la ruta configurada (parámetro 28) los diferentes documentos: tarjeta de propiedad, contrato y otros.
	 * @author psanchez
	 */	
	public void downLoadFileAdmin() throws IOException {  
	      FacesContext context = FacesContext.getCurrentInstance();  
	      HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();  
	      String path = "";
		  String systemParametersValue = SystemParametersEJB.getParameterValueById(28L); 
		  String userCode=userEJB.getSystemUserCode(userId);
		  String codeType= userEJB.getDocumentClient(userCode, userId, SessionUtil.ip(), SessionUtil.sessionUser().getUserId());
		  
		   	if (systemParametersValue != null) {    
		   		if (codeUser != null) {
		   			path = systemParametersValue.trim()+"/"+codeUser+"-"+codeType;
		   			java.io.File directory = new java.io.File(path);
		   			boolean isDirectory = directory.isDirectory();
		   			 if (isDirectory) {
		   				System.out.println("url-->"+url);
	   			        java.io.File  f = new java.io.File(url) ;
	   			 	     File file = new File(f);
			     		     file.setLength(f.length());
			     		 	 file.setName(f.getName());

	 		     		 	if (!f.exists()) {  
	 		     		 		this.setShowModal(true);
	 		     	   		    this.setModalMessage("Error en la descarga de documentos. Comuníquese con el Administrador."); 
	 		                   return;   
	 		               }  
	 		     		   response.reset();  
			               response.setBufferSize(DEFAULT_BUFFER_SIZE);
			               MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
			               String mimeType = mimeTypesMap.getContentType(f.getName());
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
 		              } else {
 	 		            	this.setShowModal(true);
 	 		       		    this.setModalMessage("Error en la descarga de documentos. Comuníquese con el Administrador."); 
 	 		    	        System.out.println("Error: Verifique la existencia de la siguiente ruta "+isDirectory+"."); 
 	 		          } 
		   		  }
		   	   }else{
		   		this.setShowModal(true);
	   		    this.setModalMessage("Error en la descarga de documentos. Comuníquese con el Administrador."); 
		        System.out.println("Error:La carpeta no existe o no esta compartida. Verifique.");
	   			files.clear();
		   		}	
	    }
	
	
	/**
	 * Hides modal windows.
	 */
	public void hideModal(){
		this.setShowModal(false);
		this.setModalMessage(null);
	}
	
   /**
    * Getters and Setters 
    */ 
	public void setListAllInfoVerification(List<AllInfoVerification> listAllInfoVerification) {
		this.listAllInfoVerification = listAllInfoVerification;
	}

	/**
	 *  Método encargado de listar los documentos del cliente en sesión
	 * @author psanchez
	 * @return the listAllInfoVerification
	 */
	public List<AllInfoVerification> getListAllInfoVerification() {
		if(listAllInfoVerification == null) {
			listAllInfoVerification = new ArrayList<AllInfoVerification>();			
		}else{
			listAllInfoVerification.clear();
		}
		listAllInfoVerification = documentEJB.getDocumentbyClient(userId);
		return listAllInfoVerification;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setCodeUser(Long codeUser) {
		this.codeUser = codeUser;
	}

	public Long getCodeUser() {
		return codeUser;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getState() {
		return state;
	}

	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}

	public Long getDocumentId() {
		return documentId;
	}

	public void setModalMessage(String modalMessage) {
		this.modalMessage = modalMessage;
	}

	public String getModalMessage() {
		return modalMessage;
	}

	public void setShowModal(boolean showModal) {
		this.showModal = showModal;
	}

	public boolean isShowModal() {
		return showModal;
	}	
	
 }