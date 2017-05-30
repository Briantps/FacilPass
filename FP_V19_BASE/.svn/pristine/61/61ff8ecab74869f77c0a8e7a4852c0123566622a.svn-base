/**
 * 
 */
package crud;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import sessionVar.SessionUtil;
import util.AllInfoHoliday;

import ejb.crud.Holiday;

import jpa.TbHolidays;

/**
 * Bean to Manage the administration of Holidays.
 * @author psanchez
 *
 */
public class AdminHolidaysBean implements Serializable {
	private static final long serialVersionUID = 731945118516042526L;

	@EJB(mappedName="ejb/Holiday")
	private Holiday holidayEJB;
	
	// Attributes.
	private List<TbHolidays> holidayList;
	private List<AllInfoHoliday> listAllInfoHolidays;
	
	private Long idHoliday;
	private String holidayName;
	private Date holidayDate;
	private String holidaySearchName;
	private Date holidaySearchDate;
	private Date fechaActual;
	private Integer holidayState;
	
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
	public AdminHolidaysBean() {
		fechaActual = new Date();
	}
	
	@PostConstruct
	public void init(){
		this.getHolidayList();
	}
	
	// Actions ------------ //
	
	public String initAddHoliday(){
		this.setShowModalValidateI(false);
		this.setShowModalValidateE(false);
		this.setShowConfirmationI(false);
		this.setShowConfirmationE(false);
		this.setShowModalDelete(false);
		this.setShowModalResponse(false);
		this.setShowInsert(true);
		this.setShowEdit(false);
		this.setHolidayDate(null);
		this.setHolidayName(null);
		return null;
	}
	
	public String showConfirmRemove(){
		this.setShowModalDelete(true);
		this.setModalMessage("¿Está seguro de eliminar el día festivo?");
		return null;
	}
	
	/**
	 * Método encargado de crear nuevo día festivo
	 * @author psanchez
	 */
	public void addHolidays(){
	  showModalValidateI= false;
	  showInsert = false;
	  try{
		  if(postValidationNew()){
			 if(holidayDate!=null){
				 if(holidayEJB.existHoliday(holidayDate)==false){
				    setModalMessage("El día festivo ya existe. Verifique.");
				    setShowModalValidateI(true);
				  }else if(getHolidayDate().after(fechaActual)==true){
					  if(holidayName.equals("") || holidayName.equals(null)){
						 this.setModalMessage("Ingrese la descripción del día festivo.");
						 setShowModalValidateI(true);
					  }else {
						setShowModalValidateI(false);
						setShowConfirmationI(true);
						setModalMessage(null);
						setCorfirmMessage("¿Está seguro de crear el día festivo?");
					  }
				  }else {
					 this.setModalMessage("La fecha debe ser mayor a la fecha actual. Verifique.");
					 setShowModalValidateI(true);
				  }
			 } else {
				 this.setModalMessage("Ingrese la fecha.");
				 setShowModalValidateI(true);
			 }
		  }
	   } catch(Exception ex){
		 System.out.println(" [ Error en AdminHolidaysBean.addHolidays. ] ");
		 this.setModalMessage("Error en transación");
		 setShowModalValidateI(true);
	     ex.printStackTrace();
	   }
	}
	
	
	public String insertHoliday(){
		this.setModalMessage(null);
        String result = holidayEJB.insertHoliday(holidayDate, holidayName, 
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
	 * Método encargado de editar día festivo y/o descripción posteriores a la fecha actual.
	 * @author psanchez
	 */
	public void editHoliday(){
	  showModalValidateE= false;
	  showEdit = false;
	  try{
		  if(postValidationNew()){
			   if(holidayDate!=null){
				    if(holidayName.equals("") || holidayName.equals(null)){
					  	this.setModalMessage("Ingrese la descripción del día festivo.");
					  	setShowModalValidateE(true);
					 }else if(holidayEJB.existHolidayU(idHoliday, holidayDate)==false){
						setModalMessage("El día festivo ya existe. Verifique.");
						setShowModalValidateE(true);
					 }else if(getHolidayDate().before(fechaActual)){
						this.setModalMessage("La fecha debe ser mayor a la fecha actual. Verifique.");
						setShowModalValidateE(true);
					 }else {
						setShowModalValidateE(false);
						setShowConfirmationE(true);
						setModalMessage(null);
						setCorfirmMessage("¿Está seguro de realizar cambios?");
		 			 }
			    } else {
					this.setModalMessage("Ingrese la fecha.");
					setShowModalValidateE(true);
			    }
		    } 
		 }catch(Exception ex){
			 System.out.println(" [ Error en AdminHolidaysBean.editHoliday. ] ");
			 this.setModalMessage("Error en Transacción.");
			 setShowModalValidateE(true);
			 ex.printStackTrace();
		 }
	}
	
	
	
	public String saveHoliday() {
		this.setModalMessage(null);
		String result =holidayEJB.editHoliday(idHoliday, holidayDate, holidayName, SessionUtil.ip(), 
				SessionUtil.sessionUser().getUserId());
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
	
	/**
	 * Init Edition of initEditHoliday.
	 */
	public void initEditHoliday(){
		for(AllInfoHoliday th : listAllInfoHolidays){
			if(idHoliday.longValue() ==th.getIdHoliday().longValue()){
				holidayDate = th.getHolidayDate();
				holidayName = th.getDescription();
				break;
			}
		}
	  showEdit = true;
	}
	
		
	/**
	 * Método encargado de desactivar días festivos posteriores a la fecha actual.
	 * @author psanchez
	 */
	public void cancelHolidays(){
	 showModalDelete = false;
	 showModalResponse = false;
	 try{
		   if(getHolidayDate().after(fechaActual)){	
				if(holidayEJB.removeHoliday(idHoliday, SessionUtil.ip(), SessionUtil.sessionUser().getUserId())) {
					this.setModalMessage("El día festivo ha sido eliminado con éxito.");
					setShowModalResponse(true);
					this.setHolidayList(null);
				} else {
					this.setModalMessage("Error en Transacción.");
					setShowModalResponse(true);
					this.setHolidayList(null);
				}
			} else {
				this.setModalMessage("La fecha a eliminar debe ser mayor a la fecha actual. Verifique.");
				setShowModalResponse(true);
				this.setHolidayList(null);
			}
		}catch(Exception ex){
		   System.out.println(" [ Error en AdminHolidaysBean.cancelHolidays. ] ");
		   this.setModalMessage("Error en Transacción.");
		   setShowModalResponse(true);
	       ex.printStackTrace();
		}
	}
	
	
	public void searchFilter(){
	 long minYear=2012;
	 long maxYear=9999;
	 try{
		 if(postValidationSearch()){
			 if((holidaySearchName != null) && (holidaySearchDate != null) ){
				if(!holidaySearchDate.equals("")){
				   SimpleDateFormat sdf= new SimpleDateFormat("yyyy");
				   String strDate = sdf.format(holidaySearchDate);
				   long longDate = Long.parseLong(strDate);
					 if(longDate>minYear && longDate<maxYear){
		   	            List<AllInfoHoliday> listfilter = null;
					    listfilter = holidayEJB.getInfoHolidasByFilters(holidaySearchName, strDate);
						 if(listfilter.size()<=0){
							this.setModalMessage("No se encontraron resultados de la búsqueda.");
							this.setShowModalResponse(true);
							listAllInfoHolidays.clear();
							this.setSearchok(false);
						  }else{
							this.setListAllInfoHolidays(listfilter);
							this.setSearchok(true);
							this.setShowModalResponse(false);
					  	  }
					   }else{
						   this.listAllInfoHolidays.clear();
						   this.setModalMessage("El filtro de búsqueda año tiene un rango de 2013 hasta 9999. Verifique.");
						   this.setShowModalResponse(true);
					   }
				  }
			   }else if(!holidaySearchName.equals("")){
			   	        List<AllInfoHoliday> listfilter = null;
						listfilter = holidayEJB.getInfoHolidasByFiltersHolidayName(holidaySearchName);
							if(listfilter.size()<=0){
								this.setModalMessage("No se encontraron resultados de la búsqueda.");
								this.setShowModalResponse(true);
								listAllInfoHolidays.clear();
								this.setSearchok(false);
							}else{
								this.setListAllInfoHolidays(listfilter);
								this.setSearchok(true);
								this.setShowModalResponse(false);
							}
						}else{
							this.setModalMessage("Ingrese filtro de búsqueda.");
							this.setShowModalResponse(true);		
						}
			   }
		   } catch (Exception e) {
			   System.out.println(" [ Error en AdminHolidaysBean.searchFilter ] ");
			   this.setModalMessage("Error en transación");
			   e.printStackTrace();
		   } 
	}
	
	
	private boolean postValidationNew(){
		if(holidayName!="" && (!holidayName.matches("([a-z]|[A-Z]|[áéíóúñü]|[ÁÉÍÓÚÑÜ]|\\s)+"))){
			this.setModalMessage("El campo Descripción tiene caracteres inválidos. Verifique.");
			this.setShowModalValidateI(true);
			return false;
		}
		return true;		
	}
	
	
	private boolean postValidationSearch(){
		if(holidaySearchName!="" && (!holidaySearchName.matches("([a-z]|[A-Z]|[áéíóúñü]|[ÁÉÍÓÚÑÜ]|\\s)+"))){
			this.setModalMessage("El campo Descripción tiene caracteres inválidos. Verifique.");
			this.setShowModalResponse(true);
			return false;
		}
		return true;		
	}
	
	
	
	public void clearFilter(){
		holidaySearchName ="";
		holidaySearchDate = null;
		this.setSearchok(false);
		this.getListAllInfoHolidays();
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
	
	// Getters and Setters ------------ //

	
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
	 * @param holidayList the holidayList to set
	 */
	public void setHolidayList(List<TbHolidays> holidayList) {
		this.holidayList = holidayList;
	}

	/**
	 * @return the holidayList
	 */
	public List<TbHolidays> getHolidayList() {
		if(holidayList == null) {
			holidayList = new ArrayList<TbHolidays>();
		}else{
			holidayList.clear();
		}
		holidayList = holidayEJB.getHolidays();
	  return holidayList;		
    }

	/**
	 * @param idHoliday the idHoliday to set
	 */
	public void setIdHoliday(Long idHoliday) {
		this.idHoliday = idHoliday;
	}

	/**
	 * @return the idHoliday
	 */
	public Long getIdHoliday() {
		return idHoliday;
	}

	/**
	 * @param holidayName the holidayName to set
	 */
	public void setHolidayName(String holidayName) {
		this.holidayName = holidayName;
	}

	/**
	 * @return the holidayName
	 */
	public String getHolidayName() {
		return holidayName;
	}

	/**
	 * @param holidayDate the holidayDate to set
	 */
	public void setHolidayDate(Date holidayDate) {
		this.holidayDate = holidayDate;
	}

	/**
	 * @return the holidayDate
	 */
	public Date getHolidayDate() {
		return holidayDate;
	}

	/**
	 * @param holidayState the holidayState to set
	 */
	public void setHolidayState(Integer holidayState) {
		this.holidayState = holidayState;
	}

	/**
	 * @return the holidayState
	 */
	public Integer getHolidayState() {
		return holidayState;
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
	
	
	public Date getFechaActual() {
		return fechaActual;
	}

	public void setFechaActual(Date fechaActual) {
		this.fechaActual = fechaActual;
	}

	public void setListAllInfoHolidays(List<AllInfoHoliday> listAllInfoHolidays) {
		this.listAllInfoHolidays = listAllInfoHolidays;
	}

	/**
	 * @return the listAllInfoHolidays
	 */
	public List<AllInfoHoliday> getListAllInfoHolidays() {
		
		if(listAllInfoHolidays == null) {
		   listAllInfoHolidays = new ArrayList<AllInfoHoliday>();			
		}else{
			if(searchok==false){
				listAllInfoHolidays.clear();
			} 
		}
		if(searchok==false){
			listAllInfoHolidays = holidayEJB.getAllInfoHolidays();
			this.setHolidayList(null);

		} 
	  return listAllInfoHolidays;
	}

	
	public boolean isSearchok() {
		return searchok;
	}

	public void setSearchok(boolean searchok) {
		this.searchok = searchok;
	}

	public void setHolidaySearchName(String holidaySearchName) {
		this.holidaySearchName = holidaySearchName;
	}

	public String getHolidaySearchName() {
		return holidaySearchName;
	}

	public void setHolidaySearchDate(Date holidaySearchDate) {
		this.holidaySearchDate = holidaySearchDate;
	}

	public Date getHolidaySearchDate() {
		return holidaySearchDate;
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

	public void setShowModalValidateI(boolean showModalValidateI) {
		this.showModalValidateI = showModalValidateI;
	}

	public boolean isShowModalValidateI() {
		return showModalValidateI;
	}

	public void setShowModalValidateE(boolean showModalValidateE) {
		this.showModalValidateE = showModalValidateE;
	}

	public boolean isShowModalValidateE() {
		return showModalValidateE;
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