package process.device;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.Application;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.swing.table.TableModel;

import net.sf.jasperreports.engine.JRDataSource;

import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import constant.LogAction;
import constant.LogReference;

import jpa.TbTagType;
import jpa.TbCompanyTag;
import jpa.TbCourier;
import jpa.TbRollo;

import report.AbstractBaseReportBean;
import security.UserLogged;
import sessionVar.SessionUtil;
import upload.FileUtil;
import util.ConnectionData;
import validator.Validation;

import ejb.Device;
import ejb.Log;
import ejb.SystemParameters;
import ejb.User;
import ejb.crud.TagType;
import ejb.crud.CompanyTag;

public class ChargeMassiveTagsBean  extends AbstractBaseReportBean implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@EJB(mappedName="ejb/User")
	private User userEJB;
		
	@EJB(mappedName ="ejb/SystemParameters")
	private SystemParameters SystemParametersEJB;
	
	@EJB(mappedName = "ejb/TagType")
	private TagType tagTypeEJB;
	
	@EJB(mappedName="ejb/Device")
	private Device deviceEJB;
	
	@EJB(mappedName="ejb/Log")
	private Log log;
	
	@EJB(mappedName="ejb/CompanyTag")
	private CompanyTag companyTagEJB;
	
	// Attributes-------------------//	
	private final String COMPILE_FILE_NAME="fileErrorLoadTags";
	private ConnectionData connection=new ConnectionData();
	
	private boolean autoUpload = false;
	private boolean useFlash = false;	
	private boolean showValidationFactory=false;
	private boolean showMessage;
	private boolean showMessage2;
	private boolean showMessage3;
	private boolean showMessage4;
	private boolean showMessage5;
	private boolean showData;
	private boolean showErrorFile;
	
	private String message;
		
	private String nameFile="";
	private String pathFile="";	
	
	private List<SelectItem> typeList;
	private Long tagTypeId=0L;
	private Long tagTypeIdV=0L;
	
	private Long deviceLength;
	
	private UserLogged usrs;	
	
	private TableModel listTags;
	
	
	private boolean showModal;
	private boolean showInsertCompanyTag;
	private boolean showInsertCourier;
	private boolean showInsertRoll;
	private boolean showModalValidateCompanyTag;
	private boolean showModalValidateCourier;
	private boolean showModalValidateRoll;
    private boolean showModalConfirmationCompanyTag;
	private boolean showModalConfirmationCourier;
	private boolean showModalConfirmationRoll;
	private boolean showModalAdmin;
	private boolean permAdd;
	private boolean disableFields=false;
	private boolean showOcult=false;
	
	private List<SelectItem> companyTagList;
	private Long companyTagId=0L;
	private Long companyTagIdNew=0L;
	private Long companyTagIdV=0L;
	
	private String companyTagName;
	private String courierName;
	private String rollName;
	private String nameperm;
	
    private List<SelectItem> courierList;
    private Long courierId;
    private String courierIdNew;

	private List<SelectItem> rolloList;
	private Long rolloId;
	
	private Long stateAddCompany;
	
	
		
	public ChargeMassiveTagsBean(){
		super();
		typeList= new ArrayList<SelectItem>();
		companyTagList = new ArrayList<SelectItem>();
	}
	
	@PostConstruct
	public void init(){
		setUsrs((UserLogged) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user"));
		nameperm = "createCompanyCourierRoll";
		permAdd = userEJB.getPermission(usrs.getUserId(),nameperm);
		
	}
	
	public void initAddCompanyTag(){
		this.setShowMessage(false);
		this.setShowInsertCompanyTag(true);
		this.setCompanyTagName(null);
		this.setShowData(false);
	}
	
	public void initAddCourier(){
		this.setCourierIdNew("");
		if(companyTagIdNew==-1L){
			this.setShowInsertCourier(false);
			this.setMessage("El campo Empresa es requerido.");
			this.setShowModalAdmin(true);	
		}
		else{	
			this.setShowModalAdmin(false);
			this.setShowInsertCourier(true);
			this.setCompanyTagName(companyTagEJB.getCompanyTagName(companyTagIdNew));
			this.setCourierName(null);		
		}
	}
	
	public void initAddRoll(){
		if(companyTagIdNew==-1L){
			this.setShowInsertRoll(false);
			this.setMessage("El campo Empresa es requerido.");
			this.setShowModalAdmin(true);	
		}
		else if(courierId==-1){
			this.setShowInsertRoll(false);
			this.setMessage("El campo Courier es requerido.");
			this.setShowModalAdmin(true);
		}
		else{
			this.setShowModalAdmin(false);
			this.setShowInsertRoll(true);
			this.setCompanyTagName(companyTagEJB.getCompanyTagName(companyTagIdNew));
			this.setCourierName(companyTagEJB.getCourierName(courierId));
			this.setRollName(null);		
		}
	}
	
	/**
	 * Método encargado de crear la empresa
	 * @author psanchez
	 */
	public void validationAddCompanyTag(){
	  this.hideModal();
	  try{
		    if(companyTagName.equals(null) || companyTagName.trim().equals("")){
		    	this.setMessage("El campo Nombre Empresa no puede estar vacío.");
		    	this.setShowModalValidateCompanyTag(true);
	        }
			else if(!companyTagName.matches("([a-z]|[A-Z]|[áéíóúñ]|[ÁÉÍÓÚÑ]|[0-9]|[!\"“”#$%&'()*+,-./;=¿?¡\\[\\]]||[/\\\\]|\\s)+")){	
				this.setMessage("El campo Nombre Empresa contiene caracteres inválidos.");
				this.setShowModalValidateCompanyTag(true);
			}
			else if(companyTagName.length()> 50L){
		    	this.setMessage("El campo Nombre Empresa excede los 50 caracteres.");
		    	this.setShowModalValidateCompanyTag(true);
		    }
			else{
		      String result = companyTagEJB.existCompanyTag(companyTagName);
			  if(result != null ){
				this.setMessage(result);
				this.setShowModalValidateCompanyTag(true);
			  }else {
				this.setShowModalValidateCompanyTag(false);
				this.setShowModalConfirmationCompanyTag(true);
				this.setMessage("¿Está seguro de crear la empresa "+ companyTagName+"?");
			   }
		   }	
		}catch(Exception e){
			this.setMessage("El campo Nombre Empresa contiene caracteres inválidos.");
			this.setShowModalValidateCompanyTag(true);
		}
	  }

	
	public void insertCompanyTag(){
	   this.hideModal();
	   try{
			String result =companyTagEJB.insertCompanyTag(companyTagName, SessionUtil.ip(), SessionUtil.sessionUser().getUserId());
			if(result != null ){
				this.setMessage(result);
			} else {
				this.setMessage("Error en transación.");
			}
			this.setShowMessage(true);
	   }catch(Exception e){
		   System.out.println(" [ Error en ChargeMassiveTagsBean.insertCompanyTag] " +e.getMessage());
	   }
    }
	
	/**
	 * Método encargado de crear la empresa
	 * @author psanchez
	 */
	public void validationAddCourier(){
	    this.hideModal();
		try{
		    if(courierIdNew==null || courierIdNew.equals("")){
		    	this.setMessage("El campo Id Courier no puede estar vacío.");
		    	this.setShowModalValidateCourier(true);
		    }
		    else if(!Validation.isNumeric(courierIdNew)){
				this.setMessage("El campo Id Courier contiene caracteres inválidos.");
		    	this.setShowModalValidateCourier(true);
			}
		    else if(courierIdNew.equals("0")){
		    	this.setMessage("El campo Id Courier debe contener valores superiores a cero (0).");
		    	this.setShowModalValidateCourier(true);
	        }
		    else if(courierIdNew.substring(0,1).equals("0")){
		    	this.setMessage("El campo Id Courier no debe iniciar con cero (0).");
		    	this.setShowModalValidateCourier(true);
	        }
		    else if(courierIdNew.length()> 15L){
		    	this.setMessage("El campo Id Courier debe ser máximo 15 dígitos.");
		    	this.setShowModalValidateCourier(true);
		    }
		    else if(courierName==null || courierName.trim().equals("")){
		    	this.setMessage("El campo Nombre Courier no puede estar vacío.");
		    	this.setShowModalValidateCourier(true);
		    }
			else if(!courierName.matches("([a-z]|[A-Z]|[áéíóúñ]|[ÁÉÍÓÚÑ]|[0-9]|[!\"#$%&'()*+,-./;=¿?¡\\[\\]]||[/\\\\]|\\s)+")){	
				this.setMessage("El campo Nombre Courier contiene caracteres inválidos.");
				this.setShowModalValidateCourier(true);
			}
			else if(courierName.length()> 50L){
		    	this.setMessage("El campo Nombre Courier excede los 50 caracteres.");
		    	this.setShowModalValidateCourier(true);
		    }
			else{
		      String result = companyTagEJB.existCourier(courierName, Long.parseLong(courierIdNew));
			  if(result != null ){
				this.setMessage(result);
				this.setShowModalValidateCourier(true);
			  }else {
				this.setShowModalValidateCourier(false);
				this.setShowModalConfirmationCourier(true);
				this.setMessage("¿Está seguro de crear el Courier "+ courierName+"?");
			  }
			}
	    }catch(Exception ex){
	    	this.setMessage("El campo Nombre Courier contiene caracteres inválidos.");
			this.setShowModalValidateCourier(true);
	   	}
	 }
	
	public void insertCourier(){
	   this.hideModal();
	   try{
			String result =companyTagEJB.insertCourier(Long.parseLong(courierIdNew), courierName, companyTagIdNew, SessionUtil.ip(), SessionUtil.sessionUser().getUserId());
			if(result != null ){
				this.setMessage(result);
			} else {
				this.setMessage("Error en transación.");
			}
			this.setShowModal(true);
	   }catch(Exception e){
		   System.out.println(" [ Error en EnterDevice.insertCourier] " +e.getMessage());
	   }
    } 
	
	/**
	 * Método encargado de crear la empresa
	 * @author psanchez
	 */
	public void validationAddRoll(){
	  this.hideModal();
	  try{
		   if(rollName.equals(null) || rollName.trim().equals("")){
		    	this.setMessage("El campo Nombre Rollo no puede estar vacío.");
		    	this.setShowModalValidateRoll(true);
	        }
			else if(!rollName.matches("([a-z]|[A-Z]|[áéíóúñ]|[ÁÉÍÓÚÑ]|[0-9]|[!\"#$%&'()*+,-./;=¿?¡\\[\\]]||[/\\\\]|\\s)+")){
				this.setMessage("El campo Nombre Rollo contiene caracteres inválidos.");
				this.setShowModalValidateRoll(true);
			}
			else if(rollName.length()> 50L){
		    	this.setMessage("El campo Nombre Rollo excede los 50 caracteres.");
		    	this.setShowModalValidateRoll(true);
		    }
			else{
		      String result = companyTagEJB.existRoll(rollName, rolloId);
			  if(result != null ){
				this.setMessage(result);
				this.setShowModalValidateRoll(true);
			  }else {
				this.setShowModalValidateRoll(false);
				this.setShowModalConfirmationRoll(true);
				this.setMessage("¿Está seguro de crear el rollo "+rollName+"?");
			  }
		   }	
		}catch(Exception e){
			this.setMessage("El campo Nombre Rollo contiene caracteres inválidos.");
			this.setShowModalValidateRoll(true);
		}
	 }
	
	public void insertRoll(){
	   this.hideModal();
	   try{
			String result =companyTagEJB.insertRoll(rollName, courierId, SessionUtil.ip(), SessionUtil.sessionUser().getUserId());
			if(result != null ){
				this.setCourierId(courierId);
				this.setMessage(result);
			} else {
				this.setMessage("Error en transación.");
			}
			this.setShowModal(true);
	   }catch(Exception e){
		   System.out.println(" [ Error en EnterDevice.insertRoll] " +e.getMessage());
	   }
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
	public void changeTypeList() {
	System.out.println("changeTypeList..tagTypeId...."+tagTypeId);
	System.out.println("changeTypeList..companyTagId...."+companyTagId);
	  this.setShowData(false);
	  	if(tagTypeId == -1L || companyTagId == -1L){
	  	   this.setShowData(false);
		} else {
		   System.out.println("else....changeTypeList");
		   this.setShowData(true);
		   this.setShowOcult(false);
		   this.setShowErrorFile(false);
		   this.setStateAddCompany(0L);
		}
	}

	
	public void loadFile(UploadEvent event){	
      try { 
        	String pathFile ="";
     	    String userCode=userEJB.getSystemUserCode(usrs.getUserId());
        	String systemParametersValue = SystemParametersEJB.getParameterValueById(29L);
        	String systemParametersChargue = SystemParametersEJB.getParameterValueById(43L);
			int cantChargueMassive = Integer.parseInt(systemParametersChargue.trim());

	    		if(systemParametersValue != null && systemParametersChargue!= null) {
				   if(userCode!= null || usrs.getUserId()!= null) {
	        			UploadItem item = event.getUploadItem();
	        			String fileName = FileUtil.trimFilePath(item.getFileName());
	        			pathFile = systemParametersValue.trim();
	        			java.io.File uniqueFile = FileUtil.uniqueFile(new java.io.File(pathFile), fileName);
	        			nameFile=uniqueFile.getName();
	        			FileUtil.write(uniqueFile, item.getData());
	        			listTags = deviceEJB.loadFileTags(uniqueFile);
	        			System.out.println("path------------->:"+pathFile+"; file:"+uniqueFile.getName()+"; listTags:"+listTags);
	        			System.out.println("path:"+pathFile+"; file:"+nameFile+"; listTags.getRowCount:"+listTags.getRowCount()+"; listTags.getColumnCount:"+listTags.getColumnCount());
	        			
	        			if(listTags.getRowCount()<=cantChargueMassive){
		        			if(listTags.getColumnCount()>0 && listTags.getRowCount()>0){
		        				if(listTags.getColumnCount()>=1 && listTags.getColumnCount()<=4){
				        			if(listTags.getRowCount()==0){
			        					if(uniqueFile.delete()){
			        						System.out.println("Se eliminó archivo para cargue: "+pathFile+":"+nameFile);
			        						this.setShowMessage(true);
				        					this.setMessage("El archivo no tiene Dispositivos para cargar, por favor verifique.");
			        					}else{
			        						System.out.println("No se pudo eliminar el archivo para cargue: "+pathFile+":"+nameFile);
			        					}
			        				}else {
			        					deviceLength = tagTypeEJB.lengDeviceId(tagTypeId);	
			        					Long cant=deviceEJB.validateMassiveTags(SessionUtil.sessionUser().getUserId(),SessionUtil.ip(), 
			        							listTags, tagTypeId, deviceLength, nameFile, companyTagId, usrs.getUserId());
		        						System.out.println("cantidad tag(s)......"+cant);
		        						System.out.println("listTags.getRowCount()......"+listTags.getRowCount());
			        					System.out.println("listTags.getColumnCount()......"+listTags.getColumnCount());
	        							System.out.println("tagTypeId..."+tagTypeId);
	        							System.out.println("companyTagId..."+companyTagId);
		        						long resultado = cant/listTags.getColumnCount();
			        					if(cant<=0L){
				            				this.setMessage("No registro ningún Tag, por favor verifique el archivo de errores.");
			        						this.setShowMessage2(true);
				        		        	this.setShowValidationFactory(false);
			        						this.setShowOcult(false);
			        					}if(cant==-1L){
				            				this.setMessage("No registro ningún Tag, por favor verifique el archivo de errores.");
			        						this.setShowMessage(true);
				        		        	this.setShowValidationFactory(false);
			        						this.setShowOcult(false);
			        					}else if(resultado<(long)listTags.getRowCount()){
			        						System.out.println("resultado..."+resultado);
			        						this.setMessage("No registro ningún Tag, por favor verifique el archivo de errores.");
			        						this.setShowMessage2(true);
				        		        	this.setShowValidationFactory(false);
			        						this.setShowOcult(false);
		        						}else {
				            				tagTypeIdV=tagTypeId;
				            				companyTagIdV=companyTagId;
		        							System.out.println("tagTypeIdV..."+tagTypeIdV);
		        							System.out.println("companyTagIdV..."+companyTagIdV);
			        						this.setShowErrorFile(false);
				        		        	this.setShowValidationFactory(true);
			        						
			        					}
			        				}
		        			}else{
		        				System.out.println("El archivo contiene mas de 4 columnas.");
		        				this.setShowMessage5(true);
		            			this.setMessage("El archivo contiene mas de 4 columnas. Verifique.");
		        			}
		        		}else{
		        			if(uniqueFile.delete()){
	    						System.out.println("Se eliminó archivo para cargue: "+pathFile+":"+nameFile);
	    						this.setShowMessage5(true);
	        					this.setMessage("El archivo no tiene Dispositivos para cargar, por favor verifique.");
	    					}else{
	    						System.out.println("No se pudo eliminar el archivo para cargue: "
	    								+pathFile+":"+nameFile);
	    					}
	        			}
				   }else{
					   System.out.println("El archivo excede la cantidad máxima de registros parametrizados "+cantChargueMassive);
						this.setShowMessage(true);
						this.setMessage("El archivo excede la cantidad máxima ("+cantChargueMassive+") de registros parametrizados. Verifique.");
				   }
				}else{
					this.setShowMessage(true);
					this.setMessage("Error en la carga masiva de Tags. Comuníquese con el Administrador."); 
			    	System.out.println("Error:La carpeta no existe o no esta compartida. Verifique.");
			    	clearUploadData();
			    	refresh();
				}
	    	}
        } catch (IOException ex) {
        	System.out.println(" [ CharegMassiveTagsBean.loadFile : IOException] ");
        	this.setShowMessage(true);
			this.setMessage("Error:Fallo la creación del directorio. Verifique.");
            ex.printStackTrace();
        }  catch (NullPointerException ne){
			System.out.println("[  CharegMassiveTagsBean.loadFile : NullPointerException] ");
			ne.printStackTrace();
			this.setShowMessage(true);
			this.setMessage("El archivo contiene mas de 4 columnas. Verifique.");
		}  catch (Exception e){
        	System.out.println(" [ CharegMassiveTagsBean.loadFile. : Exception java.lang.OutOfMemoryError: Java heap space] ");
        	e.printStackTrace();
			this.setShowMessage(true);
			this.setMessage("Error en la carga masiva de Tags. Comuníquese con el Administrador.");  
        }	
	} 
	
	
	
	public String validateFactory(){
		this.setShowValidationFactory(false);
		String factoryName=tagTypeEJB.getFactoryName(tagTypeId);
		if(tagTypeIdV.longValue()==tagTypeId.longValue() && companyTagIdV.longValue()==companyTagId.longValue()){
			this.setShowMessage3(true);
			this.setMessage("Se cargará(n) "+listTags.getRowCount()+
					" Tag(s) del Fabricante "+factoryName +", de clic en " +
					"Aceptar para confirmar la carga.");
		}else if(tagTypeIdV.longValue()!=tagTypeId.longValue()){
			this.setShowMessage5(true);
			this.setShowOcult(false);
			this.setMessage("El fabricante debe ser igual al seleccionado al inicio de la carga.");
			log.insertLogTag(nameFile, 2L, String.valueOf(1),
					"FABRICANTE", factoryName, "El fabricante debe ser igual al seleccionado al inicio de la carga.",
					"El fabricante debe ser igual al seleccionado al inicio de la carga.",usrs.getUserId());
			log.insertLog(
					"Carga Masiva Tag | Error selección fabricante: "
					+ factoryName + ", El fabricante debe ser igual al seleccionado al inicio de la carga.",
					LogReference.FACTORY, LogAction.CREATE, SessionUtil.ip(),usrs.getUserId());
		}
		else if(companyTagIdV.longValue()!=companyTagId.longValue()){
			this.setShowMessage5(true);
			this.setMessage("La empresa debe ser igual a la seleccionada al inicio de la carga.");
			this.setStateAddCompany(0L);
			log.insertLogTag(nameFile, 2L, String.valueOf(1),
					"EMPRESA", companyTagName, "La empresa debe ser igual a la seleccionada al inicio de la carga.",
					"La empresa debe ser igual a la seleccionada al inicio de la carga.",usrs.getUserId());
			log.insertLog(
					"Carga Masiva Tag | Error selección empresa: "
					+ companyTagName + ", La empresa debe ser igual a la seleccionada al inicio de la carga.",
					LogReference.COMPANY, LogAction.CREATE, SessionUtil.ip(),usrs.getUserId());
		}
		
		return null;
	}
	
	public String saveTags(){
		deviceLength = tagTypeEJB.lengDeviceId(tagTypeId);	
		Long cant=deviceEJB.createMassiveTags(SessionUtil.sessionUser()
				.getUserId(),SessionUtil.ip(), listTags, tagTypeId, deviceLength, nameFile, companyTagId, usrs.getUserId());
		if(cant==0L){
			this.setMessage("No registro ningún Tag, por favor verifique el archivo de errores.");
			this.setShowErrorFile(true);
			this.setShowOcult(false);
		}else{
			this.setMessage("Se carga(n) "+cant+" Tag(s).");
			this.setShowMessage(true);
			this.setShowMessage2(false);
			this.setShowMessage3(false);
			this.setShowMessage4(false);
			this.setShowMessage5(false);
			this.setShowData(false);
			this.setShowErrorFile(false);
			this.setTagTypeId(-1L);
			this.setCompanyTagId(-1L);
			this.setStateAddCompany(0L);
			this.setShowValidationFactory(false);
			this.clearUploadData();
		}
		return null;
	}
		
	public String cancelLoad(){
		this.hideModal();
		this.setShowErrorFile(false);
		java.io.File uniqueFile = new java.io.File(pathFile,nameFile);
		if(uniqueFile.delete()){
			System.out.println("Se eliminó archivo para cargue: "+pathFile+":"+nameFile);
		}else{
			System.out.println("No se pudo eliminar el archivo para cargue: "
					+pathFile+":"+nameFile);
		}
		this.setShowMessage5(true);
		this.setMessage("Carga de Tag(s) cancelada.");
		return null;
	}
			
	
	/**
	 * @param typeList the typeList to set
	 */
	public void setTypeList(List<SelectItem> typeList) {
		this.typeList = typeList;
	}

	/**
	 * @return the typeList
	 */
	public List<SelectItem> getTypeList() {
		if (typeList == null) {
			typeList = new ArrayList<SelectItem>();
		} else {
			typeList.clear();
		}
		typeList.add(new SelectItem(-1L,"--Seleccione Fabricante--"));
		for(TbTagType tt : tagTypeEJB.getTagType()) {
			if(tt.getTagState().intValue()== 1){
				typeList.add(new SelectItem(tt.getTagTypeId(), tt.getTagTypeName()));
			}
		}
		return typeList;
	}
	
	
	@Override
	protected Map<String, Object> getReportParameters() {
		Map<String, Object> reportParameters = new HashMap<String, Object>();
		reportParameters.put("userId", usrs.getUserId());
		reportParameters.put("nameFile", nameFile);
		System.out.println("reportParameters: "+reportParameters);
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
	
	
	public void hideModal(){
		this.setMessage("");
		this.setShowMessage(false);
		this.setShowMessage2(false);
		this.setShowMessage3(false);
		this.setShowMessage4(false);
		this.setShowMessage5(false);
		this.setShowData(false);
		this.setShowModal(false);
		this.setShowInsertCompanyTag(false);
		this.setShowInsertCourier(false);
		this.setShowInsertRoll(false);
		this.setShowModalValidateCompanyTag(false);
		this.setShowModalValidateCourier(false);
		this.setShowModalValidateRoll(false);
		this.setShowModalConfirmationCompanyTag(false);
		this.setShowModalConfirmationRoll(false);
		this.setShowModalConfirmationCourier(false);
		this.setShowValidationFactory(false);
		this.clearUploadData();
	}
	
	public void hideModal2(){
		this.setMessage("");
		this.setShowMessage(false);
		this.setShowMessage2(false);
		this.setShowMessage3(false);
		this.setShowMessage4(false);
		this.setShowMessage5(false);
		this.setShowErrorFile(true);
		this.setShowData(false);
		this.setTagTypeId(-1L);
		this.setCompanyTagId(-1L);
		this.setShowValidationFactory(false);
		this.clearUploadData();		
	}
	
	public void hideModal3(){
		this.setShowModalValidateCompanyTag(false);
		this.setShowModalConfirmationCompanyTag(false);
		this.setShowInsertCompanyTag(true);
		this.setShowMessage(false);
	}
	
	public void hideModal4(){
		this.setShowModalValidateCourier(false);
		this.setShowModalConfirmationCourier(false);
		this.setShowInsertCourier(true);
		this.setShowModal(false);
	}
	
	public void hideModal5(){
		this.setShowModalValidateRoll(false);
		this.setShowModalConfirmationRoll(false);
		this.setShowInsertRoll(true);
		this.setShowModal(false);
	}
	
	public void hideModal7(){
		this.setMessage("");
		this.setShowMessage(false);
		this.setShowMessage2(false);
		this.setShowMessage3(false);
		this.setShowMessage4(false);
		this.setShowMessage5(false);
		this.setShowData(false);
		this.setShowModal(false);
		this.setShowModalAdmin(false);
		this.setShowInsertCompanyTag(false);
		this.setShowInsertCourier(false);
		this.setShowInsertRoll(false);
		this.setShowModalValidateCompanyTag(false);
		this.setShowModalValidateCourier(false);
		this.setShowModalValidateRoll(false);
		this.setShowModalConfirmationCompanyTag(false);
		this.setShowModalConfirmationRoll(false);
		this.setShowModalConfirmationCourier(false);
		this.setShowValidationFactory(false);
		this.clearUploadData();
		this.setTagTypeId(-1L);
		this.setCompanyTagId(-1L);
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
	

	/**
	 * @param accountList the accountList to set
	 */
	public void setAccountList(List<SelectItem> accountList) {
		this.typeList = accountList;
	}

	/**
	 * @return the accountList
	 */
	public List<SelectItem> getAccountList() {
		return typeList;
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




	public void setTagTypeId(Long tagTypeId) {
		this.tagTypeId = tagTypeId;
	}

	public Long getTagTypeId() {
		return tagTypeId;
	}

	public void setTagTypeEJB(TagType tagTypeEJB) {
		this.tagTypeEJB = tagTypeEJB;
	}

	public TagType getTagTypeEJB() {
		return tagTypeEJB;
	}

	public void setDeviceEJB(Device deviceEJB) {
		this.deviceEJB = deviceEJB;
	}

	public Device getDeviceEJB() {
		return deviceEJB;
	}

	public void setShowMessage3(boolean showMessage3) {
		this.showMessage3 = showMessage3;
	}

	public boolean isShowMessage3() {
		return showMessage3;
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
	
	
	public TableModel getListTags() {
		return listTags;
	}

	public void setListTags(TableModel listTags) {
		this.listTags = listTags;
	}

	public void setShowErrorFile(boolean showErrorFile) {
		this.showErrorFile = showErrorFile;
	}

	public boolean isShowErrorFile() {
		return showErrorFile;
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
		return "ErrorFileLoadTags" + System.currentTimeMillis();
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

	public void setDeviceLength(Long deviceLength) {
		this.deviceLength = deviceLength;
	}

	public Long getDeviceLength() {
		return deviceLength;
	}

	public void setUsrs(UserLogged usrs) {
		this.usrs = usrs;
	}

	public UserLogged getUsrs() {
		return usrs;
	}

	public void setShowValidationFactory(boolean showValidationFactory) {
		this.showValidationFactory = showValidationFactory;
	}

	public boolean isShowValidationFactory() {
		return showValidationFactory;
	}

	public void setTagTypeIdV(Long tagTypeIdV) {
		this.tagTypeIdV = tagTypeIdV;
	}

	public Long getTagTypeIdV() {
		return tagTypeIdV;
	}

	public void setShowMessage2(boolean showMessage2) {
		this.showMessage2 = showMessage2;
	}

	public boolean isShowMessage2() {
		return showMessage2;
	}

	public void setShowMessage4(boolean showMessage4) {
		this.showMessage4 = showMessage4;
	}

	public boolean isShowMessage4() {
		return showMessage4;
	}

	public void setCompanyTagList(List<SelectItem> companyTagList) {
		this.companyTagList = companyTagList;
	}

	public List<SelectItem> getCompanyTagList() {
		if (companyTagList == null) {
			companyTagList = new ArrayList<SelectItem>();
		} else {
			companyTagList.clear();
		}
		companyTagList.add(new SelectItem(-1L,"--Seleccione Empresa--"));
		for(TbCompanyTag tct : companyTagEJB.getCompanyTag()) {
			if(tct.getCompanyTagState().intValue()== 1){
				companyTagList.add(new SelectItem(tct.getCompanyTagId(), tct.getCompanyTagName()));
			}
		}
		return companyTagList;
	}
	
	public void setCompanyTagId(Long companyTagId) {
		this.companyTagId = companyTagId;
	}

	public Long getCompanyTagId() {
		return companyTagId;
	}
	
	public CompanyTag getCompanyTagEJB() {
		return companyTagEJB;
	}

	public void setCompanyTagEJB(CompanyTag companyTagEJB) {
		this.companyTagEJB = companyTagEJB;
	}

	public void setCompanyTagName(String companyTagName) {
		this.companyTagName = companyTagName;
	}

	public String getCompanyTagName() {
		return companyTagName;
	}

	public void setShowInsertCompanyTag(boolean showInsertCompanyTag) {
		this.showInsertCompanyTag = showInsertCompanyTag;
	}

	public boolean isShowInsertCompanyTag() {
		return showInsertCompanyTag;
	}

	public void setShowModalValidateCompanyTag(boolean showModalValidateCompanyTag) {
		this.showModalValidateCompanyTag = showModalValidateCompanyTag;
	}

	public boolean isShowModalValidateCompanyTag() {
		return showModalValidateCompanyTag;
	}

	public void setShowModalConfirmationCompanyTag(
			boolean showModalConfirmationCompanyTag) {
		this.showModalConfirmationCompanyTag = showModalConfirmationCompanyTag;
	}

	public boolean isShowModalConfirmationCompanyTag() {
		return showModalConfirmationCompanyTag;
	}

	public void setCompanyTagIdV(Long companyTagIdV) {
		this.companyTagIdV = companyTagIdV;
	}

	public Long getCompanyTagIdV() {
		return companyTagIdV;
	}

	public void setNameperm(String nameperm) {
		this.nameperm = nameperm;
	}

	public String getNameperm() {
		return nameperm;
	}

	public void setPermAdd(boolean permAdd) {
		this.permAdd = permAdd;
	}

	public boolean getPermAdd() {
		return permAdd;
	}

	public void setStateAddCompany(Long stateAddCompany) {
		if (stateAddCompany!=1){
			this.setShowOcult(false);
			this.setDisableFields(false);
		}else{
			this.setShowOcult(true);
			this.setShowData(false);
			this.setShowErrorFile(false);
			this.setDisableFields(true);
			this.setTagTypeId(-1L);
			this.setCompanyTagIdNew(-1L);
			this.setCourierId(-1L);
			this.setRolloId(-1L);
		}
	}

	public Long getStateAddCompany() {
		return stateAddCompany;
	}

	public void setShowOcult(boolean showOcult) {
		this.showOcult = showOcult;
	}

	public boolean isShowOcult() {
		return showOcult;
	}

	public void setCourierList(List<SelectItem> courierList) {
		this.courierList = courierList;
	}

	public List<SelectItem> getCourierList() {
		courierList= new ArrayList<SelectItem>();
		courierList.add(new SelectItem(-1, "--Seleccione Uno--"));
		for(TbCourier tc : companyTagEJB.getCourier(companyTagIdNew)){
			courierList.add(new SelectItem(tc.getCourierId(), tc.getCourierName()));
		}
		return courierList;
	}

	public void setCourierId(Long courierId) {
		this.courierId = courierId;
	}

	public Long getCourierId() {
		return courierId;
	}

	public void setRolloList(List<SelectItem> rolloList) {
		this.rolloList = rolloList;
	}

	public List<SelectItem> getRolloList() {
		rolloList= new ArrayList<SelectItem>();
		rolloList.add(new SelectItem(-1, "--Seleccione Uno--"));
		if(courierId!=null && courierId>-1){
			for(TbRollo tr: companyTagEJB.getRollosByCourier(companyTagIdNew, courierId)){
				rolloList.add(new SelectItem(tr.getRollId(), tr.getRollName()));	
			}
		}
		return rolloList;
	}

	public void setRolloId(Long rolloId) {
		this.rolloId = rolloId;
	}

	public Long getRolloId() {
		return rolloId;
	}

	public void setShowInsertCourier(boolean showInsertCourier) {
		this.showInsertCourier = showInsertCourier;
	}

	public boolean isShowInsertCourier() {
		return showInsertCourier;
	}

	public void setShowInsertRoll(boolean showInsertRoll) {
		this.showInsertRoll = showInsertRoll;
	}

	public boolean isShowInsertRoll() {
		return showInsertRoll;
	}

	public void setCourierName(String courierName) {
		this.courierName = courierName;
	}

	public String getCourierName() {
		return courierName;
	}

	public void setRollName(String rollName) {
		this.rollName = rollName;
	}

	public String getRollName() {
		return rollName;
	}

	public void setShowModal(boolean showModal) {
		this.showModal = showModal;
	}

	public boolean isShowModal() {
		return showModal;
	}

	public void setShowModalConfirmationCourier(boolean showModalConfirmationCourier) {
		this.showModalConfirmationCourier = showModalConfirmationCourier;
	}

	public boolean isShowModalConfirmationCourier() {
		return showModalConfirmationCourier;
	}

	public void setShowModalConfirmationRoll(boolean showModalConfirmationRoll) {
		this.showModalConfirmationRoll = showModalConfirmationRoll;
	}

	public boolean isShowModalConfirmationRoll() {
		return showModalConfirmationRoll;
	}

	public void setShowModalValidateCourier(boolean showModalValidateCourier) {
		this.showModalValidateCourier = showModalValidateCourier;
	}

	public boolean isShowModalValidateCourier() {
		return showModalValidateCourier;
	}
	

	public void setShowModalValidateRoll(boolean showModalValidateRoll) {
		this.showModalValidateRoll = showModalValidateRoll;
	}

	public boolean isShowModalValidateRoll() {
		return showModalValidateRoll;
	}

	public void setDisableFields(boolean disableFields) {
		this.disableFields = disableFields;
	}

	public boolean isDisableFields() {
		return disableFields;
	}

	public void setCompanyTagIdNew(Long companyTagIdNew) {
		this.companyTagIdNew = companyTagIdNew;
	}

	public Long getCompanyTagIdNew() {
		return companyTagIdNew;
	}

	public void setShowModalAdmin(boolean showModalAdmin) {
		this.showModalAdmin = showModalAdmin;
	}

	public boolean isShowModalAdmin() {
		return showModalAdmin;
	}

	public String getCourierIdNew() {
		return courierIdNew;
	}

	public void setCourierIdNew(String courierIdNew) {
		this.courierIdNew = courierIdNew;
	}

	public void setShowMessage5(boolean showMessage5) {
		this.showMessage5 = showMessage5;
	}

	public boolean isShowMessage5() {
		return showMessage5;
	}


	}
