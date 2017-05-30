/**
 * 
 */
package crud;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import sessionVar.SessionUtil;
import validator.Validation;

import ejb.crud.Company;

import jpa.TbCompany;
import jpa.TbCompanyType;

/**
 * Bean to Manage the administration of Company.
 * @author tmolina
 *
 */
public class CompanyBean implements Serializable {
	private static final long serialVersionUID = 731945118516042526L;

	@EJB(mappedName="ejb/Company")
	private Company company;
	
	// Attributes -------------- //
	
	private boolean showModal;
	
	private String modalMessage;
	
	private List<TbCompany> companyList;
	
	private Long companyId;
	
	private boolean showInsert;
	
	private String companyName="";
	
	private String companyNit="";
	
	private List<SelectItem> companyTypeList;
	
	private Long newCompanyId;
	
    private Long companyTypeId;
	
	private String fideicomiso="";
	
	private String nitFideicomiso="";
	
	private String nroContrato="";
	
	private String disclaimer="";
	
	private Long newCompanyTypeId;
	
    private String newCompanyName="";
	
	private String newCompanyNit="";
	
	private String newFideicomiso="";
	
	private String newNitFideicomiso="";
	
	private String newNroContrato="";
	
	private String newDisclaimer="";
	
    private boolean showUpdate;
    
    private boolean showModalError;
    
    private boolean showModalUpdateError;
    
    private boolean showOcult=true;
    
	private boolean showOcultDisclaimer=false;
    

	/**
	 * Constructor.
	 */
	public CompanyBean() {
	}
	
	@PostConstruct
	public void init(){
		this.getCompanyList();
	}
	
	// Actions ------------ //
	
	/**
	 * Init Add company.
	 */
	public String initAddCompany(){
		setShowInsert(true);
		this.setShowOcult(true);
		this.setShowOcultDisclaimer(false);
		setCompanyName(null);
		return null;
	}
	
	/**
	 * Inserts a new company.
	 */
	public String addCompany(){
		setShowInsert(false);
		this.setShowModal(false);
		this.setShowModalError(false);
		this.setShowModalUpdateError(false);

		if(companyNit.equals(null) || companyNit.equals(""))
		{
			setModalMessage("El Nit de la empresa es requerido.");
			this.setShowModalError(true);
		}
		else if(companyName.equals(null) || companyName.equals(""))
		{
			setModalMessage("El Nombre de la empresa es requerido.");
			this.setShowModalError(true);
		}
		else if(companyName.length() > 80)
		{
			setModalMessage("La longitud del nombre de la empresa no es correcta. Recuerde que debe ser máximo 80 caracteres.");
			this.setShowModalError(true);
		}
		else if(fideicomiso.equals(null) || fideicomiso.equals(""))
		{
			setModalMessage("El Nombre del fideicomiso es requerido.");
			this.setShowModalError(true);
		}
		else if(nitFideicomiso.equals(null) || nitFideicomiso.equals(""))
		{
			setModalMessage("El Nit del fideicomiso es requerido.");
			this.setShowModalError(true);
		}
		else if(nroContrato.equals(null) || nroContrato.equals(""))
		{
			setModalMessage("El Nro. de contrato de la concesión con el fideicomiso es requerido.");
			this.setShowModalError(true);
		}
		else if(!Validation.isNumeric(companyNit)){
			setModalMessage("El campo Nit. de la Empresa solo puede contener números.");
			this.setShowModalError(true);
		}
		else if(!Validation.isNumeric(nitFideicomiso)){
			setModalMessage("El campo Nit. del fideicomiso solo puede contener números.");
			this.setShowModalError(true);
		}
		else if(!Validation.isAlphaNumCompany(companyName)){
			setModalMessage("El campo Nombre de la empresa solo puede contener letras, números y estos caracteres especiales: &/-_,.");
			this.setShowModalError(true);
		}
		else if(!Validation.isAlphaNumCompany(fideicomiso)){
			setModalMessage("El campo Nombre del fideicomiso solo puede contener letras, números y estos caracteres especiales: &/-_,.");
			this.setShowModalError(true);
		}
		else if (!Validation.alphaNumSpaceNroContrato(nroContrato)){
			setModalMessage("El campo Nro. de Contrato solo puede contener letras, números y estos caracteres especiales: -/()._");
		    this.setShowModalError(true);
		}
		else if((disclaimer.equals(null) || disclaimer.equals("")) && companyTypeId==1){
			setModalMessage("El campo disclaimer es requerido. Verifique.");
			this.setShowModalError(true);
		}
		/*else if (!Validation.isDisclaimer(disclaimer) && companyTypeId==1){
			setModalMessage("El campo disclaimer posee caracteres inválidos. Verifique.");
		    this.setShowModalError(true);
		}*/
		else if(disclaimer.length() > 500  && companyTypeId==1)
		{
			setModalMessage("El campo disclaimer tiene una longitud superior a 500 caracteres. Verifique.");
			this.setShowModalError(true);
		}
		else
		{		
			Long result=null;
				try {
					result = company.insertCompany(companyNit,companyName, companyTypeId,
							SessionUtil.ip(), SessionUtil
							.sessionUser().getUserId(),fideicomiso,nitFideicomiso,nroContrato, disclaimer);
				} catch (Exception e) {
					e.printStackTrace();
				}
			if (result != null) {
				if(result != -1L){
					setModalMessage("La empresa ha sido creada con éxito.");
					setCompanyList(null);
				} else {
					setModalMessage("Verifique que no exista una empresa con el mismo nombre ni con el mismo nit.");
					this.setShowModalError(true);
				}
			} else {
				setModalMessage("Error en Transacción.");
			}
			setShowModal(true);
		}
		return null;
	}
	
	public String updateCompany(){
		setShowInsert(false);
		setShowUpdate(false);
		this.setShowModal(false);
		this.setShowModalError(false);
		this.setShowModalUpdateError(false);
		
		if(newCompanyNit.equals(null) || newCompanyNit.equals(""))
		{
			setModalMessage("El Nit de la empresa es requerido.");
			this.setShowModalUpdateError(true);
		}
		else if(newCompanyName.equals(null) || newCompanyName.equals(""))
		{
			setModalMessage("El Nombre de la empresa es requerido.");
			this.setShowModalUpdateError(true);
		}
		else if(newCompanyName.length() > 80)
		{
			setModalMessage("La longitud del nombre de la empresa no es correcta. Recuerde que debe ser máximo 80 caracteres.");
			this.setShowModalUpdateError(true);
		}
		else if(newFideicomiso.equals(null) || newFideicomiso.equals(""))
		{
			setModalMessage("El Nombre del fideicomiso es requerido.");
			this.setShowModalUpdateError(true);
		}
		else if(newNitFideicomiso.equals(null) || newNitFideicomiso.equals(""))
		{
			setModalMessage("El Nit del fideicomiso es requerido.");
			this.setShowModalUpdateError(true);
		}
		else if(newNroContrato.equals(null) || newNroContrato.equals(""))
		{
			setModalMessage("El Nro. de contrato de la concesión con el fideicomiso es requerido.");
			this.setShowModalUpdateError(true);
		}
		else if(!Validation.isNumeric(newCompanyNit)){
			setModalMessage("El campo Nit de la Empresa solo debe contener números.");
			this.setShowModalUpdateError(true);
		}
		else if (!Validation.isNumeric(newNitFideicomiso)){
			setModalMessage("El campo Nit del fideicomiso solo debe contener números.");
			this.setShowModalUpdateError(true);
		}
		else if(!Validation.isAlphaNumCompany(newCompanyName)){
			setModalMessage("El campo Nombre de la empresa solo puede contener letras, números y estos caracteres especiales: &/-_,.");
			this.setShowModalUpdateError(true);
		}
		else if(!Validation.isAlphaNumCompany(newFideicomiso)){
			setModalMessage("El campo Nombre del fideicomiso solo puede contener letras, números y estos caracteres especiales: &/-_,.");
			this.setShowModalUpdateError(true);
		}
		else if (!Validation.alphaNumSpaceNroContrato(newNroContrato)){
			setModalMessage("El campo Nro. de Contrato solo puede contener letras, números y estos caracteres especiales: -/()._");
			this.setShowModalUpdateError(true);
		}
		else if(newCompanyTypeId==1){
			 if(newDisclaimer==null || newDisclaimer.equals("")){
				 setModalMessage("El campo disclaimer es requerido. Verifique.");
				 this.setShowModalUpdateError(true);
			 }else if(newDisclaimer.length()>500){
				 setModalMessage("El campo disclaimer tiene una longitud superior a 500 caracteres. Verifique.");
				 this.setShowModalUpdateError(true);
			 }	
		}
		else{
		Long result=null;
		result = company.updateCompany(newCompanyId, newCompanyNit,newCompanyName, newCompanyTypeId,
					SessionUtil.ip(), SessionUtil.sessionUser().getUserId(),newFideicomiso, 
					newNitFideicomiso, newNroContrato, newDisclaimer);
			if (result != null) {
				if(result != -1L){
					setModalMessage("La empresa ha sido modificada con éxito.");
					setCompanyList(null);
				} else {
					setModalMessage("Verifique que no exista una empresa con el mismo nombre ni con el mismo nit.");
					this.setShowModalUpdateError(true);
				}
			} else {
				setModalMessage("Error en Transacción.");
			}
			setShowModal(true);
		}
		return null;
	}
	
	/**
	 * Método encargado de ocultar en el jsf el disclaimer al seleccionar tipo de empresa Concesión.
	 * @author psanchez
	 */
	public void changeMenu(ValueChangeEvent event){
		companyTypeId = (Long) event.getNewValue();
		if (companyTypeId!=1){
			this.setShowOcult(false);
			this.setShowOcultDisclaimer(true);
		}else{
			this.setShowOcultDisclaimer(false);
			this.setShowOcult(true);
		}
	}
	
	/**
	 * Hides modal windows.
	 */
	public String hideModal(){
		setShowInsert(false);
		setShowModal(false);
		setModalMessage(null);
		this.setCompanyName("");
		this.setFideicomiso("");
		this.setNitFideicomiso("");
		this.setNroContrato("");
		this.setCompanyNit("");
		this.setDisclaimer("");
		this.setShowUpdate(false);
		this.setNewCompanyName("");
		this.setNewFideicomiso("");
		this.setNewNitFideicomiso("");
		this.setNewNroContrato("");
		this.setNewCompanyNit("");
		this.setNewDisclaimer("");
		this.setShowModalError(false);
		this.setShowModalUpdateError(false);
		return null;
	}
	
	public String hideModal2(){
		this.setShowInsert(true);
		this.setShowUpdate(false);
		this.setShowModal(false);
		this.setShowModalError(false);
		this.setShowModalUpdateError(false);
		return null;
	}
	
	public String hideModal3(){
		this.setShowUpdate(true);
		this.setShowInsert(false);
		this.setShowModal(false);
		this.setShowModalError(false);
		this.setShowModalUpdateError(false);
		return null;
	}
	
	// Getters and Setters ------------ //

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
	 * @param companyId the companyId to set
	 */
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	/**
	 * @return the companyId
	 */
	public Long getCompanyId() {
		return companyId;
	}

	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * @param companyList the companyList to set
	 */
	public void setCompanyList(List<TbCompany> companyList) {
		this.companyList = companyList;
	}

	/**
	 * @return the companyList
	 */
	public List<TbCompany> getCompanyList() {
		if(companyList == null) {
			companyList = new ArrayList<TbCompany>();
		}else{
			companyList.clear();
		}
		companyList = company.getCompany();
	 return companyList;
	}
	
	/**
	 * @param companyTypeList the companyTypeList to set
	 */
	public void setCompanyTypeList(List<SelectItem> companyTypeList) {
		this.companyTypeList = companyTypeList;
	}

	/**
	 * @return the companyTypeList
	 */
	public List<SelectItem> getCompanyTypeList() {
		companyTypeList = new ArrayList<SelectItem>(); 
		for(TbCompanyType c: company.getCompanytype()){
			companyTypeList.add(new SelectItem(c.getCompanyTypeId(), c
					.getCompanyTypeDescription()));
		}
		return companyTypeList;
	}
	
	/**
	 * @param companyTypeId the companyTypeId to set
	 */
	public void setCompanyTypeId(Long companyTypeId) {
		this.companyTypeId = companyTypeId;
	}

	/**
	 * @return the companyTypeId
	 */
	public Long getCompanyTypeId() {
		return companyTypeId;
	}

	/**
	 * @param fideicomiso the fideicomiso to set
	 */
	public void setFideicomiso(String fideicomiso) {
		this.fideicomiso = fideicomiso;
	}

	/**
	 * @return the fideicomiso
	 */
	public String getFideicomiso() {
		return fideicomiso;
	}

	/**
	 * @param nitFideicomiso the nitFideicomiso to set
	 */
	public void setNitFideicomiso(String nitFideicomiso) {
		this.nitFideicomiso = nitFideicomiso;
	}

	/**
	 * @return the nitFideicomiso
	 */
	public String getNitFideicomiso() {
		return nitFideicomiso;
	}

	/**
	 * @param nroContrato the nroContrato to set
	 */
	public void setNroContrato(String nroContrato) {
		this.nroContrato = nroContrato;
	}

	/**
	 * @return the nroContrato
	 */
	public String getNroContrato() {
		return nroContrato;
	}

	/**
	 * @param companyNit the companyNit to set
	 */
	public void setCompanyNit(String companyNit) {
		this.companyNit = companyNit;
	}

	/**
	 * @return the companyNit
	 */
	public String getCompanyNit() {
		return companyNit;
	}

	/**
	 * @param newCompanyId the newCompanyId to set
	 */
	public void setNewCompanyId(Long newCompanyId) {
		this.newCompanyId = newCompanyId;
	}

	/**
	 * @return the newCompanyId
	 */
	public Long getNewCompanyId() {
		return newCompanyId;
	}

	/**
	 * @param newCompanyTypeId the newCompanyTypeId to set
	 */
	public void setNewCompanyTypeId(Long newCompanyTypeId) {
		this.newCompanyTypeId = newCompanyTypeId;
	}

	/**
	 * @return the newCompanyTypeId
	 */
	public Long getNewCompanyTypeId() {
		return newCompanyTypeId;
	}

	/**
	 * @param newCompanyName the newCompanyName to set
	 */
	public void setNewCompanyName(String newCompanyName) {
		this.newCompanyName = newCompanyName;
	}

	/**
	 * @return the newCompanyName
	 */
	public String getNewCompanyName() {
		return newCompanyName;
	}

	/**
	 * @param newCompanyNit the newCompanyNit to set
	 */
	public void setNewCompanyNit(String newCompanyNit) {
		this.newCompanyNit = newCompanyNit;
	}

	/**
	 * @return the newCompanyNit
	 */
	public String getNewCompanyNit() {
		return newCompanyNit;
	}

	/**
	 * @param newFideicomiso the newFideicomiso to set
	 */
	public void setNewFideicomiso(String newFideicomiso) {
		this.newFideicomiso = newFideicomiso;
	}

	/**
	 * @return the newFideicomiso
	 */
	public String getNewFideicomiso() {
		return newFideicomiso;
	}

	/**
	 * @param newNitFideicomiso the newNitFideicomiso to set
	 */
	public void setNewNitFideicomiso(String newNitFideicomiso) {
		this.newNitFideicomiso = newNitFideicomiso;
	}

	/**
	 * @return the newNitFideicomiso
	 */
	public String getNewNitFideicomiso() {
		return newNitFideicomiso;
	}

	/**
	 * @param newNroContrato the newNroContrato to set
	 */
	public void setNewNroContrato(String newNroContrato) {
		this.newNroContrato = newNroContrato;
	}

	/**
	 * @return the newNroContrato
	 */
	public String getNewNroContrato() {
		return newNroContrato;
	}
	
	public void update(){
		if(newCompanyTypeId!=1){
			this.setShowUpdate(true);
			this.setShowOcult(false);
			this.setShowOcultDisclaimer(true);
		}else {
			this.setShowUpdate(true);
			this.setShowOcult(true);
			this.setShowOcultDisclaimer(false);
		}
	}

	/**
	 * @param showUpdate the showUpdate to set
	 */
	public void setShowUpdate(boolean showUpdate) {
		this.showUpdate = showUpdate;
	}

	/**
	 * @return the showUpdate
	 */
	public boolean isShowUpdate() {
		return showUpdate;
	}

	/**
	 * @param showModalError the showModalError to set
	 */
	public void setShowModalError(boolean showModalError) {
		this.showModalError = showModalError;
	}

	/**
	 * @return the showModalError
	 */
	public boolean isShowModalError() {
		return showModalError;
	}

	/**
	 * @param showModalUpdateError the showModalUpdateError to set
	 */
	public void setShowModalUpdateError(boolean showModalUpdateError) {
		this.showModalUpdateError = showModalUpdateError;
	}

	/**
	 * @return the showModalUpdateError
	 */
	public boolean isShowModalUpdateError() {
		return showModalUpdateError;
	}

	public void setNewDisclaimer(String newDisclaimer) {
		this.newDisclaimer = newDisclaimer;
	}

	public String getNewDisclaimer() {
		return newDisclaimer;
	}

	public void setDisclaimer(String disclaimer) {
		this.disclaimer = disclaimer;
	}

	public String getDisclaimer() {
		return disclaimer;
	}

	public void setShowOcult(boolean showOcult) {
		this.showOcult = showOcult;
	}

	public boolean isShowOcult() {
		return showOcult;
	}

	public void setShowOcultDisclaimer(boolean showOcultDisclaimer) {
		this.showOcultDisclaimer = showOcultDisclaimer;
	}

	public boolean isShowOcultDisclaimer() {
		return showOcultDisclaimer;
	}

}