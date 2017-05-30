package process.verification;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletResponse;

import org.richfaces.event.DataScrollerEvent;

import net.sf.jasperreports.engine.JRDataSource;

import jpa.TbCodeType;

import ejb.Document;
import ejb.SystemParameters;
import ejb.User;

import report.AbstractBaseReportBean;
import sessionVar.SessionUtil;
import upload.File;
import util.AllInfoVerification;
import util.ConnectionData;


/**
 * @author psanchez
 *
 */
public class ValidateDocumentBean extends AbstractBaseReportBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_BUFFER_SIZE = 10240;  
	
	private ArrayList<File> files = new ArrayList<File>();
	
	@PersistenceContext(unitName = "bo") 
	EntityManager em;
	
	@EJB(mappedName="ejb/Document")
	private Document documentEJB;
	
	@EJB(mappedName="ejb/User")
	private User userEJB;
	
	@EJB(mappedName ="ejb/SystemParameters")
	private SystemParameters SystemParametersEJB;
	
	//Atributos para la generacion del Reporte de Clientes --//
	
	private ConnectionData connection= new ConnectionData();
	private final String COMPILE_FILE_NAME = "filterDocumentReportXls";
	
	// Attributes-------------------//
	private List<AllInfoVerification> listAllInfoVerification;
	private List<Integer> listaScroll;
	private List<SelectItem> codeTypesList;
	private Long codeType;
	
	private String state;
	private String url;
	private String numberDoc;
	private String userName;
	private String secondName;
	private String userEmail;
	private String accountId;
	private String codeTypeAbbr;
	private String fechaCargue;
	private String description;
	private String typeDocument;
	private String modalMessage;
	private String userUpload;

	private Long documentId;
	private Long typeDocumentId;
	private Long userId;

	private Date fechaInicial;
	private Date fechaFinal;
		
	// Control visibility ----//
	private boolean showConfirmV;
	private boolean showConfirmI;
	private boolean showConfirmation;
	private boolean showConfirmAllV;
	private boolean showConfirmAllI;
	private boolean showDescription;
	private boolean showModal;
	private boolean showModalError;
	private boolean showReport;
	private boolean searchok = false;
	
	private int page=1;
	private int valuesFor;
	
	/**
	 * Constructor.
	 */
	public ValidateDocumentBean(){

	}
	
	@PostConstruct
	public void init() {
		this.clearFilter();
	}
	
	
	/**
	 * Hides modal windows.
	 */
	public void hideModal(){
		this.clearFilter();
		this.setShowModal(false);
		this.setShowConfirmation(false);
		this.setShowConfirmI(false);
		this.setShowConfirmV(false);
		this.setShowConfirmAllI(false);
		this.setShowConfirmAllV(false);
		this.setShowDescription(false);
		this.setModalMessage(null);
	}	
	
	public void hideModal2(){
		this.setShowModal(false);
		this.setShowConfirmI(false);
		this.setShowConfirmV(false);
		this.setShowConfirmAllI(false);
		this.setShowConfirmAllV(false);
		this.setShowDescription(true);
		this.setShowConfirmation(false);
		this.setShowModalError(false);
		this.setModalMessage(null);
	}

	
	
	public void clearFilter(){
		codeType = -1L;
		numberDoc = "";
		userName ="";
		secondName ="";
		userEmail="";
		accountId="";
		state = "4";
		fechaCargue = "-1";
		fechaInicial = null;
		fechaFinal = null;
		description = "";
		documentId = 0L;
		this.setPage(1);
		this.setShowReport(false);
		this.setSearchok(false);
		this.getListAllInfoVerification();
	}
	
	public void ocult(){
		this.setShowReport(false);
	}
	
	public void showConfirmInvalidate(){
		this.setShowConfirmI(true);
		this.setModalMessage("¿Está seguro que desea rechazar el documento?");
	}
	
	public void showConfirmValidate(){
		this.setShowConfirmV(true);
		this.setModalMessage("¿Está seguro que desea validar el documento?");
	}
	
	public void showConfirmAllValidate(){
		this.setShowConfirmAllV(true);
		this.setModalMessage("¿Está seguro que desea validar los documentos?");
	}
	
	public void showConfirmAllInvalidate(){
		this.setShowConfirmAllI(true);
		this.setModalMessage("¿Está seguro que desea rechazar los documentos?");
	}
	
	public void initObservation(){
	    this.setShowDescription(true);
	}
	
	/**
	 * Método encargado de aceptar documentos cargados por el cliente y/o admón
	 * @author psanchez
	 */
	public void validateDocument(){
	 this.setShowConfirmV(false);
	 this.setShowConfirmI(false);
	 this.setShowDescription(false);
	 try{
		 System.out.println("----------> Entre a Validar.  documentId " + documentId +"; state /////" +state+"");
		 if(documentEJB.getValidateDocument(documentId, typeDocumentId, "3", description, userId, SessionUtil.ip(), SessionUtil.sessionUser().getUserId())) {
		   this.setModalMessage("El documento ha sido validado con éxito.");
		   Long creoCuenta = null;
			   if(codeTypeAbbr.equals("NIT") && typeDocumentId==1){
				   if(!userEJB.verificationCta(userId)){//se verifica si tiene ya una cuenta creada
						creoCuenta = userEJB.createAccount(userId,
								50002L, userId, SessionUtil.ip(), -1L, -1L);
						if (creoCuenta != null && creoCuenta <= -2L) {
							System.out.println("Error al Crear Cuenta FacilPass Persona Juridica ValidateDocumentBean.validateDocument");
						}
					}
			   }				   
		  }else{
		   this.setModalMessage("No se ha realizado acción sobre el documento. Verifique.");
		  }
		 this.clearFilter();
		 this.setListAllInfoVerification(null);  
		 this.setShowModal(true);
	 }catch(Exception ex){
		 System.out.println(" [ Error en ValidateDocumentBean.validateDocument. ] ");
		 this.setModalMessage("Error en Transacción.");
		 ex.printStackTrace();
	 }
		System.out.println("----------> Finalizo Validar.  documentId " + documentId);
	}
	
	
	/**
	 * Método encargado de rechazar documento cargados por el cliente y/o admón
	 * @author psanchez
	 */
	public void rejectDocument(){
	 this.setShowConfirmV(false);
	 this.setShowConfirmI(false);
	 this.setShowDescription(false);
	 try{
		 System.out.println("----------> Entre a Rechazar.  documentId " + documentId +"; state /////" +state+"");
		 if(documentEJB.getValidateDocument(documentId, typeDocumentId, "-1", description, userId, SessionUtil.ip(), SessionUtil.sessionUser().getUserId())) {
		   this.setModalMessage("El documento ha sido rechazado con éxito.");
	     } 
		 else{
		    this.setModalMessage("No se ha realizado acción sobre el documento. Verifique.");
		 }
		  this.clearFilter();
		  this.setListAllInfoVerification(null);
		  this.setShowModal(true);	 
	 }catch(Exception ex){
		 System.out.println(" [ Error en ValidateDocumentBean.validateDocument. ] ");
		 this.setModalMessage("Error en Transacción.");
		 ex.printStackTrace();
	 }
		System.out.println("----------> Finalizo Rechazar.  documentId " + documentId);
	}
	
	/**
	 * Método encargado de validar la descripción al rechazar el documento
	 * @author psanchez
	 */
	public void validateDescription(){
	 this.setShowConfirmV(false);
	 this.setShowConfirmI(false);
	 this.setShowModal(false);
	 this.setShowModalError(false);
	 this.setShowDescription(false);
		 try{	
			  if(description.equals(null) || description.equals(""))
				{
				  this.setModalMessage("El campo observación es requerido. Verifique.");
				  this.setShowModalError(true);
				}
				else if(description.length()>200)
				{
					this.setModalMessage("El campo observación tiene una longitud superior a 200 caracteres. Verifique.");
					this.setShowModalError(true);
				}
				else{
					this.setShowConfirmation(true);
					this.setModalMessage("¿Está seguro que desea rechazar el documento?");
					} 
			}catch(Exception ex){
				   System.out.println(" [ Error en ValidateDocumentBean.validateDocument. ] ");
				   this.setModalMessage("Error en Transacción.");
		           ex.printStackTrace();
			}
	}
	
	
	/**
	 * Método encargado de registrar la descripción al rechazar el documento
	 * @author psanchez
	 */
	public void saveDescription(){
	 this.setShowConfirmation(false);
	 this.setShowConfirmV(false);
	 this.setShowConfirmI(false);
	 this.setShowModal(false);
	 this.setShowModalError(false);
	 this.setShowDescription(false);
		try{
			System.out.println("----------> Entre a Rechazar.  documentId " + documentId+"; state --" +state+"");
			if(documentEJB.getValidateDocument(documentId, typeDocumentId, "-1", description, userId, SessionUtil.ip(), SessionUtil.sessionUser().getUserId())) {
				 this.setModalMessage("El documento ha sido rechazado con éxito.");
				 this.setShowModal(true);
				 this.clearFilter();
				 this.setListAllInfoVerification(null);
			  }else{
				   this.setModalMessage("No se ha realizado acción sobre el documento. Verifique.");
				   this.setShowModal(true);
				   this.clearFilter();
				   this.setListAllInfoVerification(null);  
			   }
			}catch(Exception ex){
				   System.out.println(" [ Error en ValidateDocumentBean.validateDocument. ] ");
				   this.setModalMessage("Error en Transacción.");
		           ex.printStackTrace();
			}
			System.out.println("----------> Finalizo Rechazar.  documentId " + documentId);
		}
			  
	/**
	 * Método encargado de validar todos los documento que se encuentran en la grilla con estado pendiente por validar
	 * @author psanchez
	 */
	public void validateAllDocument(){
	  this.setShowConfirmV(false);
	  this.setShowConfirmI(false);
	  this.setShowConfirmAllV(false);
	  this.setShowConfirmAllI(false);
	  try{	
		  if(state.equals("4")){
			  System.out.println("----------> Entre a Validar Total.");
			  List<AllInfoVerification> listfilter = null;
			  listfilter = documentEJB.getStateDocumentByFilters(codeType, numberDoc, userName, secondName, 
			    		 userEmail, accountId, state, fechaCargue, fechaInicial, fechaFinal, 1, 0);
			   System.out.println(" listfilter: "+listfilter.size() +"; statestate: 3" +"; state: "+state);
			   if(listfilter.size()>0){
				   if(documentEJB.getValidateSearchDocument(listfilter, "3", SessionUtil.ip(), SessionUtil.sessionUser().getUserId())) {					   
					   for (AllInfoVerification allInfoVe : listfilter) {
						   Long creoCuenta = null;
						   if(allInfoVe.getCodeTypeAbbrU().equals("NIT") && allInfoVe.getTypeDocumentIdU()==1){
							   if(!userEJB.verificationCta(allInfoVe.getUserIdU())){//se verifica si tiene ya una cuenta creada
									creoCuenta = userEJB.createAccount(allInfoVe.getUserIdU(),
											50002L, allInfoVe.getUserIdU(), SessionUtil.ip(), -1L, -1L);
									if (creoCuenta != null && creoCuenta <= -2L) {
										System.out.println("Error al Crear Cuenta FacilPass Persona Juridica ValidateDocumentBean.validateDocument");
									}
								}
						   }
					   }						   
					   this.setModalMessage("Los documentos han sido validados con éxito.");
					   this.setShowModal(true);
					   this.clearFilter();
					   this.setListAllInfoVerification(null);
				   }else{
					   this.setModalMessage("No se ha realizado acción sobre los documentos. Verifique.");
					   this.setShowModal(true);
					   this.clearFilter();
					   this.setListAllInfoVerification(null);  
				   }
			   }else{
				  this.setModalMessage("No existe documentos para realizar la acción.");
				  this.setShowModal(true);
			   }
		  }else{
			  this.setModalMessage("Solo se validan documentos con estado Pendiente por Validar.");
			  this.setShowModal(true);
		   }
		}catch(Exception ex){
		   System.out.println(" [ Error en ValidateDocumentBean.validateAllDocument. ] ");
		   this.setModalMessage("Error en Transacción.");
		   this.setShowModal(true);
           ex.printStackTrace();
		}
		System.out.println("----------> Finalizo Validar Total.");
	}
	
	
	/**
	 * Método encargado de validar todos los documento que se encuentran en la grilla con estado pendiente por validar
	 * @author psanchez
	 */
	public void rejectAllDocument(){
	  this.setShowConfirmV(false);
	  this.setShowConfirmI(false);
	  this.setShowConfirmAllV(false);
	  this.setShowConfirmAllI(false);
	  try{	
		  if(state.equals("4")){
			  System.out.println("----------> Entre a Validar Total.");
			  List<AllInfoVerification> listfilter = null;
			  listfilter = documentEJB.getStateDocumentByFilters(codeType, numberDoc, userName, secondName, 
			    		 userEmail, accountId, state, fechaCargue, fechaInicial, fechaFinal, 1, 0);
			   System.out.println(" listfilter: "+listfilter.size() +"; statestate: -1" +"; state: "+state);
			   if(listfilter.size()>0){
				   if(documentEJB.getValidateSearchDocument(listfilter, "-1", SessionUtil.ip(), SessionUtil.sessionUser().getUserId())) {
					   this.setModalMessage("Los documentos han sido rechazados con éxito.");
					   this.setShowModal(true);
					   this.clearFilter();
					   this.setListAllInfoVerification(null);
				   }else{
					   this.setModalMessage("No se ha realizado acción sobre los documentos. Verifique.");
					   this.setShowModal(true);
					   this.clearFilter();
					   this.setListAllInfoVerification(null);  
				   }
			   }else{
				  this.setModalMessage("No existe documentos para realizar la acción.");
				  this.setShowModal(true);
			   }
		  }else{
			  this.setModalMessage("Solo se validan documentos con estado Pendiente por Validar.");
			  this.setShowModal(true);
		   }
		}catch(Exception ex){
		   System.out.println(" [ Error en ValidateDocumentBean.validateAllDocument. ] ");
		   this.setModalMessage("Error en Transacción.");
		   this.setShowModal(true);
           ex.printStackTrace();
		}
		System.out.println("----------> Finalizo Validar Total.");
	}
	
	/**
	 * Método encargado de realizar la búsqueda de los datos ingresados
	 * @author psanchez
	 */
	public void searchFilter(){
	 try{
		 if(postValidationSearch()){
			 setPage(1);
	         List<AllInfoVerification> listfilter = null;    	
	         listfilter = documentEJB.getStateDocumentByFilters(codeType, numberDoc, userName, secondName, 
	    		 userEmail, accountId, state, fechaCargue, fechaInicial, fechaFinal, page, 10);
			if(listfilter.size()<=0){
				this.setModalMessage("No se encontraron resultados de la búsqueda.");
				this.setShowModal(true);
				this.listAllInfoVerification.clear();
				this.setShowReport(false);
				this.setSearchok(false);
			}else{
				this.listAllInfoVerification= listfilter;
				this.setShowModal(false);
				this.setShowReport(true);
				this.setSearchok(true);
			}
		 }
	 } catch (Exception e) {
		   System.out.println(" [ Error en ValidateDocumentBean.searchFilter ] ");
		   this.setModalMessage("Error en transación");
		   this.setShowModal(true);
		   e.printStackTrace();
	 } 
	}
	
	/**
	 * Método encargado de validar los datos ingresados en los filtros de búsqueda
	 * @author psanchez
	 */
	private boolean postValidationSearch(){
	    if(numberDoc!="" && !numberDoc.matches("([0-9])+")){
			this.setModalMessage("El campo No.Identificación tiene caracteres inválidos. Verifique.");
			this.setShowModal(true);
			return false;
		}
	    else if(userName!="" && !userName.matches("([a-z]|[A-Z]|[áéíóú]|[ÁÉÍÓÚ]|[0-9]|[&/]|[_-]|[.]|[ñÑ]|\\s)+")){
			this.setModalMessage("El campo Nombre tiene caracteres inválidos. Verifique.");
			this.setShowModal(true);
			return false;
		}
	    else if(secondName!="" && !secondName.matches("([a-z]|[A-Z]|[áéíóú]|[ÁÉÍÓÚ]|[ñÑ]|\\s)+")){
			this.setModalMessage("El campo Apellidos tiene caracteres inválidos. Verifique.");
			this.setShowModal(true);
			return false;
		}
	    else if(userEmail!="" && !userEmail.matches("([sa-zA-Z@.+_0-9-]|\\s)+")){
			this.setModalMessage("El campo Usuario tiene caracteres inválidos. Verifique.");
			this.setShowModal(true);
			return false;
		}
	    else if(accountId!="" && !accountId.matches("([0-9]|\\s)+")){ 
			this.setModalMessage("El campo Cuenta FacilPass tiene caracteres inválidos. Verifique.");
			this.setShowModal(true);
			return false;
		} 
	    else if((fechaCargue.equals("1") || fechaCargue.equals("2")) && (fechaInicial == null && fechaFinal == null)) {
			this.setModalMessage("El campo fecha inicial y fecha final estan vacíos.");
			this.setShowModal(true);
			return false;
		} 
	    else if((fechaCargue.equals("1") || fechaCargue.equals("2")) && (fechaInicial == null && fechaFinal != null)) {
			this.setModalMessage("El campo fecha inicial está vacío.");
			this.setShowModal(true);
			return false;
		}  
	    else if((fechaCargue.equals("1") || fechaCargue.equals("2")) && (fechaInicial != null && fechaFinal == null)) {
			this.setModalMessage("El campo fecha final está vacío.");
			this.setShowModal(true);
			return false;
		}
	    else if((fechaCargue.equals("1") || fechaCargue.equals("2")) && (fechaInicial.getTime()>fechaFinal.getTime())) {
	    	this.setModalMessage("La fecha inicial debe ser menor a la fecha final. Verifique.");
			this.setShowModal(true);
			return false;
	    }
	    else if (fechaCargue.equals("-1") && (fechaInicial !=null || fechaFinal != null)) {
			this.setModalMessage("Seleccione el campo fecha documentos.");
			this.setShowModal(true);
			return false;
		}
		return true;
	}
	
	/**
	 * Método encargado de descargar el documento selecionado
	 * @author psanchez
	 */
	public void downLoadFileAdmin() throws IOException {  
      FacesContext context = FacesContext.getCurrentInstance();  
      HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();  
      String path = "";
	  String systemParametersValue = SystemParametersEJB.getParameterValueById(28L);
	  String userCode=userEJB.getSystemUserCode(userId);
	  String codeType=userEJB.getDocumentClient(userCode, userId, SessionUtil.ip(), SessionUtil.sessionUser().getUserId());
		  
	   	if (systemParametersValue != null) {
	   		if (userCode != null) {
	   			path = systemParametersValue.trim()+"/"+userCode+"-"+codeType;
            	System.out.println("ValidateDocumentBean.downLoadFileAdmin.path-->"+path);
	   			java.io.File directory = new java.io.File(path);
	   			boolean isDirectory = directory.isDirectory();
	   			 if (isDirectory) {
	   				 java.io.File  f = new java.io.File(url);
	   				 File file = new File(f);
	     		     file.setLength(f.length());
	     		 	 file.setName(f.getName());
 	 
 		     		 	if (!f.exists()) {  
 		     		 		this.setShowModal(true);
 		     	   		    this.setModalMessage("Error en la descarga de documentos. Comuníquese con el Administrador."); 
 		     		        System.out.println("Error: Verifique la existencia del documento en la siguiente ruta "+isDirectory+".");
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
 		    	        System.out.println("Error: Verifique la existencia de la siguiente ruta "+directory+"."); 
 		              }
	   		     }
	   	   }else{
			this.setShowModal(true);
   		    this.setModalMessage("Error en la descarga de documentos. Comuníquese con el Administrador."); 
	        System.out.println("Error:La carpeta no existe o no esta compartida. Verifique.");
   			files.clear();
	   		}	
    } 

	
	//Metodos para la generacion del reporte ------ //
	@Override
	protected Map<String, Object> getReportParameters() {
		Map<String, Object> reportParameters = new HashMap<String, Object>();	
		
		DateFormat fechaHora = new SimpleDateFormat("yyyy/MM/dd");
  	
		String qry = "";
		qry = "SELECT DISTINCT to_char(td.document_id), tc.code_type_abbreviation AS TIPO_ID, " +
			  "tu.user_code AS IDENTIFICACION, " +
			  "tu.user_names AS NOMBRE, " +
			  "tu.user_second_names AS APELLIDO, " +
			  "tt.type_document_description AS TIPO_DOCUMENTO, " +
			  "DECODE(td.state_document,'3','Validado','-1','Rechazado','4','Pendiente por Validar') AS ESTADO_DOCUMENTO,  " +
			  "(SELECT tu.user_names ||' '|| tu.user_second_names FROM tb_system_user tu WHERE tu.user_id = td.user_id_upload) USUARIO_CARGUE, " +
			  "DECODE(td.description,null,'-',td.description) AS OBSERVACION, " +
			  "DECODE(td.upload_date_document,null,'-',TO_CHAR(td.upload_date_document, 'DD/MM/YYYY HH:MI:SS AM')) AS FECHA_INICIAL, " +
			  "DECODE(td.process_date_document,null,'-',TO_CHAR(td.process_date_document, 'DD/MM/YYYY HH:MI:SS AM')) AS FECHA_FINAL " +
			  "FROM tb_document td " +
			  "INNER JOIN tb_system_user tu ON td.user_id = tu.user_id " +
			  "INNER JOIN re_user_role rer ON tu.user_id=rer.user_id " +
			  "INNER JOIN tb_role r ON rer.role_id=r.role_id " +
			  "LEFT JOIN tb_account ta ON ta.user_id=tu.user_id " +
			  "INNER JOIN tb_code_type tc ON tu.code_type_id = tc.code_type_id " +
			  "INNER JOIN tb_type_document tt ON td.type_document_id=tt.type_document_id " +
			  "WHERE r.role_id IN (2,3) " +
			  "AND td.state_document <> 5 " +
			  "AND td.state_document <> 6 ";
		
			if((codeType!=null) && (codeType!=-1L)){
				qry = qry+"AND tc.code_type_id="+codeType+" ";
			}
			if(!numberDoc.equals("")){
				qry = qry+"AND tu.user_code like '%"+numberDoc.trim()+"%' ";
			}
			if(!userName.equals("")){
				qry = qry+"AND Upper(tu.user_names) like '%"+userName.toUpperCase()+"%' ";
			}
			if(!secondName.equals("")){
				qry = qry+"AND Upper(tu.user_second_names) like '%"+secondName.toUpperCase()+"%' ";
			}
			if(!userEmail.equals("")){
				qry = qry+"AND lower(tu.user_email) like '%"+userEmail.toLowerCase()+"%' ";
			}
			if(!accountId.equals("")) {
				qry = qry+"AND ta.account_id like '%"+accountId+"%' ";
			}
			if(fechaCargue.equals("1") && fechaInicial !=null && fechaFinal != null ) {	
				String fIni = fechaHora.format(fechaInicial);				
				String fFin = fechaHora.format(fechaFinal.getTime()+86400000);
				qry = qry+"AND td.upload_date_document between TO_DATE('"+fIni+"', 'yyyy/MM/dd') and TO_DATE('"+fFin+"', 'yyyy/MM/dd') " ;		
			}
			if(fechaCargue.equals("2") && fechaInicial !=null && fechaFinal != null ) {
				String fIni = fechaHora.format(fechaInicial);				
				String fFin = fechaHora.format(fechaFinal.getTime()+86400000);
				qry = qry+"AND td.process_date_document between TO_DATE('"+fIni+"', 'yyyy/MM/dd') and TO_DATE('"+fFin+"', 'yyyy/MM/dd') " ;
			}
			if (state.equals("99")) {					
				qry = qry+"AND 99="+state+" ";
		    }else if (state != null) {					
				qry = qry+"AND td.state_document="+state+" ";
		    }
			
		reportParameters.put("queryString", qry+"ORDER BY td.document_id DESC");
		
		return reportParameters;
	}


	public void generarReporte(){
		try {
			super.setExportOption(ExportOption.EXCEL);
			super.prepareReport();
		} catch (Exception e) {
			System.out.println("Error ValidateDocumentBean.imprimirReporte");
			e.printStackTrace();
		}
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
		return "Reporte Documentos" + System.currentTimeMillis();
	}

	@Override
	protected JRDataSource getJRDataSource() {
		return null;
	}
	
	/**
	 * @param listAllInfoVerification the listAllInfoVerification to set
	 */
	public void setListAllInfoVerification(List<AllInfoVerification> listAllInfoVerification) {
		this.listAllInfoVerification = listAllInfoVerification;
	}

	/**
	 * @return the listAllInfoVerification
	 */
	public List<AllInfoVerification> getListAllInfoVerification() {
		if(listAllInfoVerification == null) {
			listAllInfoVerification = new ArrayList<AllInfoVerification>();			
	    }else if(searchok==false){
	    	listAllInfoVerification.clear(); 
		}
		if(searchok==false){
			this.getDataForScroll();
			listAllInfoVerification = documentEJB.getStateDocumentByFilters(-1L, "", "", "", "", "", "4", "", null, null, page, 10);
		}
		if (searchok==true){
			this.getDataForScroll();
			listAllInfoVerification = documentEJB.getStateDocumentByFilters(codeType, numberDoc, userName, secondName, 
		    userEmail, accountId, state, fechaCargue, fechaInicial, fechaFinal, page, 10);
		}
	  return listAllInfoVerification;
	}
	
	
	public void getDataForScroll(){
		try {
			if(searchok==false){
				this.setValuesFor(Integer.parseInt(String.valueOf(documentEJB.getStateDocumentByFilters(-1L, "", "", 
						"", "", "", "4", "", null, null, 0, 10).get(0))));
			}else{
				this.setValuesFor(Integer.parseInt(String.valueOf(documentEJB.getStateDocumentByFilters(codeType, numberDoc, userName, 
				secondName, userEmail, accountId, state, fechaCargue, fechaInicial, fechaFinal, 0, 10).get(0))));
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
	
	/**
	 * @param codeTypeAbbr the codeTypeAbbr to set
	 */
	public void setCodeTypeAbbr(String codeTypeAbbr) {
		this.codeTypeAbbr = codeTypeAbbr;
	}

	/**
	 * @return the codeTypeAbbr
	 */
	public String getCodeTypeAbbr() {
		return codeTypeAbbr;
	}

	/**
	 * @param url the name to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the name
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param secondName the secondName to set
	 */
	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	/**
	 * @return the secondName
	 */
	public String getSecondName() {
		return secondName;
	}

	/**
	 * @param typeDocumentId the typeDocumentId to set
	 */
	public void setTypeDocumentId(Long typeDocumentId) {
		this.typeDocumentId = typeDocumentId;
	}

	/**
	 * @return the typeDocumentId
	 */
	public Long getTypeDocumentId() {
		return typeDocumentId;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
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
	 * @param documentId the documentId to set
	 */
	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}

	/**
	 * @return the documentId
	 */
	public Long getDocumentId() {
		return documentId;
	}

	/**
	 * @param showConfirmValidate the showConfirmValidate to set
	 */
	public void setShowConfirmV(boolean showConfirmV) {
		this.showConfirmV = showConfirmV;
	}

	/**
	 * @return the showConfirmValidate
	 */
	public boolean isShowConfirmV() {
		return showConfirmV;
	}

	/**
	 * @param showConfirmInvalidate the showConfirmInvalidate to set
	 */
	public void setShowConfirmI(boolean showConfirmI) {
		this.showConfirmI = showConfirmI;
	}

	/**
	 * @return the showConfirmInvalidate
	 */
	public boolean isShowConfirmI() {
		return showConfirmI;
	}


	/**
	 * @param showConfirmAllV the showConfirmAllV to set
	 */
	public void setShowConfirmAllV(boolean showConfirmAllV) {
		this.showConfirmAllV = showConfirmAllV;
	}

	/**
	 * @return the showConfirmAllV
	 */
	public boolean isShowConfirmAllV() {
		return showConfirmAllV;
	}

	/**
	 * @param showConfirmAllI the showConfirmAllI to set
	 */
	public void setShowConfirmAllI(boolean showConfirmAllI) {
		this.showConfirmAllI = showConfirmAllI;
	}

	/**
	 * @return the showConfirmAllI
	 */
	public boolean isShowConfirmAllI() {
		return showConfirmAllI;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getUserId() {
		return userId;
	}
	
	public List<SelectItem> getCodeTypesList() {
		if(codeTypesList == null) {
			codeTypesList = new ArrayList<SelectItem>();			
		}else{
			codeTypesList.clear();
		}
		codeTypesList.add(new SelectItem(-1L,"-- Seleccione Uno --"));
		for(TbCodeType c : userEJB.getCodeTypes()){
			codeTypesList.add(new SelectItem(c.getCodeTypeId(),c.getCodeTypeAbbreviation().toUpperCase()));
		}
		return codeTypesList;
	}

	public void setCodeType(Long codeType) {
		this.codeType = codeType;
	}

	public Long getCodeType() {
		return codeType;
	}

	public void setNumberDoc(String numberDoc) {
		this.numberDoc = numberDoc;
	}

	public String getNumberDoc() {
		return numberDoc;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setFechaCargue(String fechaCargue) {
		this.fechaCargue = fechaCargue;
	}

	public String getFechaCargue() {
		return fechaCargue;
	}

	public void setFechaInicial(Date fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	public Date getFechaInicial() {
		return fechaInicial;
	}

	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	public Date getFechaFinal() {
		return fechaFinal;
	}

	public void setShowDescription(boolean showDescription) {
		this.showDescription = showDescription;
	}

	public boolean isShowDescription() {
		return showDescription;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	
	public String getTypeDocument() {
		return typeDocument;
	}

	public void setTypeDocument(String typeDocument) {
		this.typeDocument = typeDocument;
	}

	public void setShowModalError(boolean showModalError) {
		this.showModalError = showModalError;
	}

	public boolean isShowModalError() {
		return showModalError;
	}

	public void setShowConfirmation(boolean showConfirmation) {
		this.showConfirmation = showConfirmation;
	}

	public boolean isShowConfirmation() {
		return showConfirmation;
	}


	public void setShowReport(boolean showReport) {
		this.showReport = showReport;
	}

	public boolean isShowReport() {
		return showReport;
	}


	public void setUserUpload(String userUpload) {
		this.userUpload = userUpload;
	}

	public String getUserUpload() {
		return userUpload;
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