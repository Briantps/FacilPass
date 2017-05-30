/**
 * 
 */
package process.device;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import ejb.crud.TagType;

import sessionVar.SessionUtil;
import util.AllInfoTagType;


import jpa.TbTagType;


/**
 * Bean to Manage Factory register.
 * @author psanchez
 */
public class EnterFactory implements Serializable {
	private static final long serialVersionUID = -7148175549095352008L;
	
	@EJB(mappedName = "ejb/TagType")
	private TagType tagTypeEJB;

	// Attributes
	private List<TbTagType> tagTypeList;
	private List<AllInfoTagType> listAllInfoTagTypes;
	
	private Long tagTypeId;
	private String searchTagTypeName;

	private String tagTypeCode;
	private String tagTypeName;
	private String serialLength;
	
	/** Control Modal **/
	private boolean showModalValidateI;
	private boolean showModalValidateE;
	private boolean showModalDelete;
    private boolean showModalResponse;
	private boolean showConfirmationE;
	private boolean showConfirmationI;
	private boolean showEdit;
	private boolean showInsert;
	private boolean searchok= false;

	private String modalMessage;
	private String corfirmMessage;

	/**
	 * Constructor.
	 */
	public EnterFactory() {
		this.setShowModalValidateI(false);
		this.setShowModalValidateE(false);
		this.setShowConfirmationI(false);
		this.setShowConfirmationE(false);
		this.setShowModalDelete(false);
		this.setShowModalResponse(false);
		this.setShowInsert(false);
		this.setShowEdit(false);
	}
	
	@PostConstruct
	public void init(){
		this.getListAllInfoTagTypes();
	}
		
	// Actions ------------ //
	
	public String initAddFactory(){
		this.setShowModalValidateI(false);
		this.setShowModalValidateE(false);
		this.setShowConfirmationI(false);
		this.setShowConfirmationE(false);
		this.setShowModalDelete(false);
		this.setShowModalResponse(false);
		this.setShowInsert(true);
		this.setShowEdit(false);
		this.setTagTypeCode(null);
		this.setTagTypeName(null);
		this.setSerialLength(null);
		return null;
	}
	
	public void showConfirmRemove(){
		this.setShowModalDelete(true);
		this.setModalMessage("¿Está seguro que desea eliminar el Fabricante?");
	}
	
	/**
	 * Método encargado de crear nuevo fabricante
	 * @author psanchez
	 */
	public void msgSave(){
	  showModalValidateI= false;
	  showInsert = false;
	  try{
		  if(postValidationNew()){
			if(tagTypeCode.equals(null) || tagTypeCode.equals("")){
		    	setModalMessage("Ingrese el Código Fabricante.");
	 			setShowModalValidateI(true);
	 		}else if(tagTypeCode!= null && tagTypeCode.length()!= 0){
				String tagTypeCodeS =  tagTypeCode.substring(0,1);
				if(tagTypeCodeS.equals("0")){
				setModalMessage("El Código Fabricante no debe iniciar con cero (0).");
				setShowModalValidateI(true);
			}else if(tagTypeName.equals(null)  || tagTypeName.equals("")){
				setModalMessage("Ingrese el Fabricante.");
	 			setShowModalValidateI(true);
	 		}else if(serialLength.equals(null) || serialLength.equals("")){
		 		setModalMessage("El Tamaño del ID Tag debe ser par de 6 a 50. Verifique.");
		 		setShowModalValidateI(true);
 			}else if(serialLength!= null && serialLength.length()!= 0){
 				int serialLength1  = Integer.parseInt(serialLength);
  			    Long serialLength2 = Long.parseLong(serialLength);
 	 		    boolean serialLength3 = this.esPar(serialLength1);
 				   if((serialLength2<6L || serialLength2>50L) ||
					  ((serialLength2>=6L && serialLength2<=50L) && (serialLength3==false))){
	 			 		setModalMessage("El Tamaño del ID Tag debe ser par de 6 a 50. Verifique.");
	 			 		setShowModalValidateI(true);
 				   }else if(tagTypeEJB.existTagTypeCode(tagTypeCode)==false){
 					    setModalMessage("El Código del Fabricante ya existe. Verifique.");
 					    setShowModalValidateI(true);
 				   }else if(tagTypeEJB.existTagTypeName(tagTypeName)==false){
 					    setModalMessage("El Nombre del Fabricante ya existe. Verifique.");
 					    setShowModalValidateI(true);
 				   }else {	
 					    setShowModalValidateI(false);
						setShowConfirmationI(true);
						setModalMessage(null);
						setCorfirmMessage("¿Está seguro de crear el fabricante?");
				   }
 			 }  
	 		}
		  }
		 }catch(NumberFormatException nfe){
			System.out.println(" [ Error en EnterFactory.msgSave. ] ");
 			setModalMessage("El campo Tamaño del ID Tag tiene caracteres inválidos. Verifique.");
 			setShowModalValidateI(true);
			System.out.println(nfe.getMessage());
		 }
	  }
	 
	  public String insertFactory(){
		this.setModalMessage(null);
		String result =tagTypeEJB.insertFactory(tagTypeCode, tagTypeName, serialLength, 
                SessionUtil.ip(), SessionUtil.sessionUser().getUserId());
		if(result != null ){
			setModalMessage(result);
		} else {
			setModalMessage(result);
		}
		setShowModalResponse(true);
		setShowModalValidateI(false);
		setShowConfirmationI(false);
		return null;
     }
		
	 
	/**
	 * Init Edition of initEditFactory.
	 */
	public void panelEdit(){
		for(AllInfoTagType th : listAllInfoTagTypes){
			if(tagTypeId.longValue() ==th.getTagTypeIdU().longValue()){
				tagTypeCode  = th.getTagTypeCodeU();
				tagTypeName  = th.getTagTypeNameU();
				serialLength = th.getSerialLengthU();
				break;
			}
		}
	  showEdit = true;
	}
   
	
	/**
	 * Método encargado de editar el fabricante.
	 * @author psanchez
	 */
	 public void editFactory(){
		showModalValidateE= false;
		showEdit = false;
		try{
			if(postValidationEdit()){
	 			 if(tagTypeName.equals(null)  || tagTypeName.equals("")){
	 				setModalMessage("Ingrese el Fabricante.");
	 				setShowModalValidateE(true);
	 			 }else if(serialLength.equals(null) || serialLength.equals("")){
			 		setModalMessage("El Tamaño del ID Tag debe ser par de 6 a 50. Verifique.");
			 		setShowModalValidateE(true);
	 			 }else if(serialLength.length()>0){
	 				int serialLength1  = Integer.parseInt(serialLength);
	  			    Long serialLength2 = Long.parseLong(serialLength);
	 	 		    boolean serialLength3 = this.esPar(serialLength1);
	 	 		    
	 				   if((serialLength2<6L || serialLength2>50L) ||
						  ((serialLength2>=6L && serialLength2<=50L) && (serialLength3==false))){
		 			 		setModalMessage("El Tamaño del ID Tag debe ser par de 6 a 50. Verifique.");
		 			 		setShowModalValidateE(true);
	 				   } else if(tagTypeEJB.existTagTypeU(tagTypeId, tagTypeName)==false){
	 					    setModalMessage("El Fabricante ya existe. Verifique.");
	 					    setShowModalValidateE(true);
	 				   }else {
						    setShowModalValidateE(false);
							setShowConfirmationE(true);
							setModalMessage(null);
							setCorfirmMessage("¿Está seguro de realizar cambios?");
	 				   }
	 			 }
			}
		 }catch(NumberFormatException nfe){
			System.out.println(" [ Error en EnterFactory.msgSaveEdit. ] ");
			setModalMessage("El campo Tamaño del ID Tag tiene caracteres inválidos. Verifique.");
			setShowModalValidateE(true);
			System.out.println(nfe.getMessage());
		 }
	  }		
		
	 public String saveFactory() {
		this.setModalMessage(null);
		String result =tagTypeEJB.editTagType(tagTypeId, tagTypeCode, tagTypeName, serialLength, 
				   SessionUtil.ip(), SessionUtil.sessionUser().getUserId());
		setModalMessage(result);
        if(result != null ){
			setModalMessage(result);
		} else {
			setModalMessage("Error en Transacción");
		}
        setShowModalValidateE(false);    
        setShowModalResponse(true);
		setShowConfirmationE(false);
		return null;
	}

		

	public void searchFilter(){
	  try{
		  if(postValidationSearch()){
			  if(!searchTagTypeName.equals("")){
	   	        List<AllInfoTagType> listfilter = null;
				listfilter = tagTypeEJB.getFactoryName(searchTagTypeName);
				if(listfilter.size()<=0){
					this.setModalMessage("No se encontraron resultados de la búsqueda.");
					this.setShowModalResponse(true);
					listAllInfoTagTypes.clear();
					this.setSearchok(false);
				}else{
					this.setListAllInfoTagType(listfilter);
					this.setSearchok(true);
					this.setShowModalResponse(false);
				}
			  } else {
				 this.setModalMessage("Ingrese el nombre del fabricante.");
				 this.setShowModalResponse(true);
			  }
		  }
	    } catch (Exception e) {
		   System.out.println(" [ Error en EnterFactory.searchFilter ] ");
		   this.setModalMessage("Error en transación");
		   this.setShowModalResponse(true);
		   e.printStackTrace();
	     } 
	}
	
	/**
	 * Método encargado de desactivar fabricantes.
	 * @author psanchez
	 */
	public void delete(){
	  showModalDelete = false;
	  showModalResponse = false;
	  try{
		   if(tagTypeEJB.removeTagType(tagTypeId, SessionUtil.ip(), SessionUtil.sessionUser().getUserId())) {
			    setModalMessage("El fabricante tiene dispositivos activos.");
			    setShowModalResponse(true);
			    getListAllInfoTagTypes();
		   } else {
			    setModalMessage("El fabricante ha sido eliminado con éxito.");
			    setShowModalResponse(true);
			    getListAllInfoTagTypes();
			}
	   }catch(Exception ex){
	     System.out.println(" [ Error en EnterFactory.delete. ] ");
	     setModalMessage("Error al desactivar el fabricante.");
		 setShowModalResponse(true);
         ex.printStackTrace();
	   }
	}
	
		
	public void clearFilter(){
		tagTypeName ="";
		searchTagTypeName ="";
		this.setSearchok(false);
		this.getListAllInfoTagTypes();
	}
	
	
	public boolean esPar(int numero) {
		  if (numero%2==0)
		    return true;
		  else
		    return false;
		}
	

	private boolean postValidationNew(){
		if(tagTypeCode!="" && (!tagTypeCode.matches("([0-9])+"))){
			this.setModalMessage("El campo Código Fabricante tiene caracteres inválidos. Verifique.");
			this.setShowModalValidateI(true);
			return false;
		}else if(tagTypeName!="" && (!tagTypeName.matches("([a-z]|[A-Z]|[áéíóúñü]|[ÁÉÍÓÚÑÜ]|[0-9]|\\s)+"))){
			this.setModalMessage("El campo Fabricante tiene caracteres inválidos. Verifique.");
			this.setShowModalValidateI(true);
			return false;
		}else if(serialLength!="" && (!serialLength.matches("([0-9])+"))){
			this.setModalMessage("El campo Tamaño del ID Tag tiene caracteres inválidos. Verifique.");
			this.setShowModalValidateI(true);
			return false;
		}
		return true;		
	}
	
	
	private boolean postValidationEdit(){
		if(tagTypeName!="" && (!tagTypeName.matches("([a-z]|[A-Z]|[áéíóúñü]|[ÁÉÍÓÚÑÜ]|[0-9]|\\s)+"))){
			this.setModalMessage("El campo Fabricante tiene caracteres inválidos. Verifique.");
			this.setShowModalValidateE(true);
			return false;
		}else if(serialLength!="" && (!serialLength.matches("([0-9]|\\s)+"))){
			this.setModalMessage("El campo Tamaño del ID Tag tiene caracteres inválidos. Verifique.");
			this.setShowModalValidateE(true);
			return false;
		}
		return true;		
	}
	
	
	private boolean postValidationSearch(){
		if(searchTagTypeName!="" && (!searchTagTypeName.matches("([a-z]|[A-Z]|[áéíóúñü]|[ÁÉÍÓÚÑÜ]|[0-9]|\\s)+"))){
			this.setModalMessage("El campo Fabricante tiene caracteres inválidos. Verifique.");
			this.setShowModalResponse(true);
			return false;
		}
		return true;		
	}
	
	/**
	 * Hides modal windows.
	 */
	public String hideModal(){
		this.setShowModalValidateI(false);
		this.setShowModalValidateE(false);
		this.setShowConfirmationI(false);
		this.setShowConfirmationE(false);
		this.setShowModalDelete(false);
		this.setShowModalResponse(false);
		this.setShowInsert(false);
		this.setShowEdit(true);
		return null;
	}
	
	public String hideModal2() {
		this.setShowModalValidateI(false);
		this.setShowModalValidateE(false);
		this.setShowConfirmationI(false);
		this.setShowConfirmationE(false);
		this.setShowModalDelete(false);
		this.setShowModalResponse(false);
		this.setShowInsert(false);
		this.setShowEdit(false);
		this.clearFilter();
		return null;
	}
	
	
	public String hideModal3(){
		this.setShowModalValidateI(false);
		this.setShowModalValidateE(false);
		this.setShowConfirmationI(false);
		this.setShowConfirmationE(false);
		this.setShowModalDelete(false);
		this.setShowModalResponse(false);
		this.setShowInsert(true);
		this.setShowEdit(false);
		return null;
	}

		
	// Getter and Setter ----------------- // 

	/**
	 * @param showModalValidateI the showModalValidateI to set
	 */
	public void setShowModalValidateI(boolean showModalValidateI) {
		this.showModalValidateI = showModalValidateI;
	}

	/**
	 * @return the showModalValidateI
	 */
	public boolean isShowModalValidateI() {
		return showModalValidateI;
	}

	/**
	 * @param showInsert the showInsert to set
	 */
	public void setShowInsert(boolean showInsert) {
		this.showInsert = showInsert;
	}

	/**
	 * @return the showInsert
	 */
	public boolean isShowInsert() {
		return showInsert;
	}

	/**
	 * @param showModalValidateE the showModalValidateE to set
	 */
	public void setShowModalValidateE(boolean showModalValidateE) {
		this.showModalValidateE = showModalValidateE;
	}

	/**
	 * @return the showModalValidateE
	 */
	public boolean isShowModalValidateE() {
		return showModalValidateE;
	}

	/**
	 * @param searchok the searchok to set
	 */
	public void setSearchok(boolean searchok) {
		this.searchok = searchok;
	}

	/**
	 * @return the searchok
	 */
	public boolean isSearchok() {
		return searchok;
	}

	/**
	 * @param tagTypeEJB the tagTypeEJB to set
	 */
	public void setTagTypeEJB(TagType tagTypeEJB) {
		this.tagTypeEJB = tagTypeEJB;
	}

	/**
	 * @return the tagTypeEJB
	 */
	public TagType getTagTypeEJB() {
		return tagTypeEJB;
	}

	/**
	 * @param tagTypeName the tagTypeName to set
	 */
	public void setTagTypeName(String tagTypeName) {
		this.tagTypeName = tagTypeName;
	}

	/**
	 * @return the tagTypeName
	 */
	public String getTagTypeName() {
		return tagTypeName;
	}
	
	public String getTagTypeCode() {
		return tagTypeCode;
	}

	public void setTagTypeCode(String tagTypeCode) {
		this.tagTypeCode = tagTypeCode;
	}

	/**
	 * @param tagTypeId the tagTypeId to set
	 */
	public void setTagTypeId(Long tagTypeId) {
		this.tagTypeId = tagTypeId;
	}

	/**
	 * @return the tagTypeId
	 */
	public Long getTagTypeId() {
		return tagTypeId;
	}
	
	public String getSerialLength() {
		return serialLength;
	}

	public void setSerialLength(String serialLength) {
		this.serialLength = serialLength;
	}

	/**
	 * @param listAllInfoTagType the listAllInfoTagType to set
	 */
	public void setListAllInfoTagType(List<AllInfoTagType> listAllInfoTagTypes) {
		this.listAllInfoTagTypes = listAllInfoTagTypes;
	}

	/**
	 * @return the listAllInfoTagType
	 */
	public List<AllInfoTagType> getListAllInfoTagTypes() {
		if(listAllInfoTagTypes == null) {
			listAllInfoTagTypes = new ArrayList<AllInfoTagType>();			
			}else{
				if(searchok==false){
					listAllInfoTagTypes.clear();
				} 
			}
			if(searchok==false){
				listAllInfoTagTypes = tagTypeEJB.getAllInfoFactories();
			} 
		return listAllInfoTagTypes;
	}

	/**
	 * @param tagTypeList the tagTypeList to set
	 */
	public void setTagTypeList(List<TbTagType> tagTypeList) {
		this.tagTypeList = tagTypeList;
	}

	/**
	 * @return the tagTypeList
	 */
	public List<TbTagType> getTagTypeList() {
		if(tagTypeList == null) {
			tagTypeList = new ArrayList<TbTagType>();
		}else{
			tagTypeList.clear();
		}
		tagTypeList = tagTypeEJB.getTagType();
		return tagTypeList;
	}
	
	public String getSearchTagTypeName() {
		return searchTagTypeName;
	}

	public void setSearchTagTypeName(String searchTagTypeName) {
		this.searchTagTypeName = searchTagTypeName;
	}

	/**
	 * @param showEdit the showEdit to set
	 */
	public void setShowEdit(boolean showEdit) {
		this.showEdit = showEdit;
	}

	/**
	 * @return the showEdit
	 */
	public boolean isShowEdit() {
		return showEdit;
	}

	/**
	 * @param showModalDelete the showModalDelete to set
	 */
	public void setShowModalDelete(boolean showModalDelete) {
		this.showModalDelete = showModalDelete;
	}

	/**
	 * @return the showModalDelete
	 */
	public boolean isShowModalDelete() {
		return showModalDelete;
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

	public void setShowModalResponse(boolean showModalResponse) {
		this.showModalResponse = showModalResponse;
	}

	public boolean isShowModalResponse() {
		return showModalResponse;
	}

	public void setShowConfirmationE(boolean showConfirmationE) {
		this.showConfirmationE = showConfirmationE;
	}

	public boolean isShowConfirmationE() {
		return showConfirmationE;
	}

	public void setShowConfirmationI(boolean showConfirmationI) {
		this.showConfirmationI = showConfirmationI;
	}

	public boolean isShowConfirmationI() {
		return showConfirmationI;
	}


}