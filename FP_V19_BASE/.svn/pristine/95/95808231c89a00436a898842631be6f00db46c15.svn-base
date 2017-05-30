/**
 * 
 */
package crud;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import jpa.TbCompany;
import jpa.TbDepartment;
import jpa.TbStationBO;
import jpa.TbStationType;

import sessionVar.SessionUtil;
import validator.Validation;

import ejb.Master;
import ejb.crud.Company;
import ejb.crud.Station;

/**
 * @author tmolina
 *
 */
public class StationBean implements Serializable {
	private static final long serialVersionUID = 1809689789721281206L;
	
	@EJB(mappedName="ejb/Station")
	private Station station;
	
	@EJB(mappedName="ejb/Company")
	private Company company;
	
	@EJB(mappedName = "ejb/Master")
	private Master master;
	
	// Attributes ----------------- //
	
	private boolean showModal;
	
	private String modalMessage;
	
	private boolean showModal2;
	
	private String modalMessage2;
	
	private boolean showConfirmation;
	
	private String confirmMessage;
	
	private String stationCode;
	
	private String stationName;
	
	private Long companyId;
	
	private Long departmentId;
	
	private Long stationTypeId;
	
	private String numberLanes;
	
	private List<SelectItem> companyList;
	
	private List<SelectItem> departments;
	
	private List<SelectItem> stationTypeList;
	
	// Consult  Stations ---- //
	
	private List<TbStationBO> stations;
	
	/**
	 * Constructor.
	 */
	public StationBean() {
	}
	
	// Actions ---- //
	
	/**
	 * Initialize variables. 
	 */
	public String init(){
		setShowModal(false);
		setStationName(null);
		setStationCode(null);
		setCompanyId(null);
		setDepartmentId(null);
		this.setNumberLanes("0");
		this.setStationTypeId(-1L);
		return null;
	}
	
	/**
	 * Hides modal panel. 
	 */
	public String hideModal() {
		setShowModal(false);
		setShowModal2(false);
		setShowConfirmation(false);
		return null;
	}
	
	public String hideModal2() {
		setShowModal(false);
		setShowConfirmation(false);
		setShowModal(false);
		setStationName(null);
		setStationCode(null);
		setCompanyId(null);
		setDepartmentId(null);
		this.setNumberLanes("0");
		this.setStationTypeId(-1L);
		return null;
	}
	

	

	public String initConfirm()
	{
		setModalMessage2(null);
		if(stationCode.equals(null) || stationCode.equals(""))
		{
			setModalMessage2("El código de la estación es requerido.");
			setShowModal2(true);
		}
		else if(stationName.equals(null) || stationName.equals(""))
		{
			setModalMessage2("El nombre de la estación es requerido");
			setShowModal2(true);
		}
		else if(companyId == null || companyId.longValue() == -1L)
		{
			setModalMessage2("Debe Seleccionar una empresa asociada a la Estación.");
			setShowModal2(true);
		}
		else if(departmentId == null || departmentId.longValue() == -1L)
		{
			setModalMessage2("Debe Seleccionar un Departamento.");
			setShowModal2(true);
		}
		else if(stationTypeId == null || stationTypeId.longValue() == -1L)
		{
			setModalMessage2("Debe Seleccionar el tipo de Estación.");
			setShowModal2(true);
		}
		else if(numberLanes == null || numberLanes.equals("") )
		{
			setModalMessage2("Debe digitar el número de carriles de la estación.");
			setShowModal2(true);
		}
		else if(numberLanes == "-1")
		{
			setModalMessage2("Debe digitar el número de carriles de la estación.");
			setShowModal2(true);
		}

		else if(!Validation.isNumeric(stationCode)){
			System.out.println("El codigo de la estacion contiene caracteres no numericos.");
			this.setModalMessage2("El campo Código de la Estación solo puede tener números.");
			this.setShowModal2(true);
		}
		
		else if (!Validation.isAlpha(stationName)){
			System.out.println("El nombre de la estacion contiene caracteres diferentes a letras");
			this.setModalMessage2("El campo Nombre solo puede tener Letras y espacios.");
			this.setShowModal2(true);
		}
		else if(!Validation.isNumeric(String.valueOf(numberLanes))){
			System.out.println("El numero de carriles contiene caracteres no numericos");
			this.setModalMessage2("El campo Número de Carriles solo puede contener números.");
			this.setShowModal2(true);
		}
		else if(stationCode.length() < 3 || stationCode.length() > 20)
		{
			setModalMessage2("La longitud del código de la estación no es correcta. Recuerde que debe contener entre 3 y 20 caracteres.");
			setShowModal2(true);
		}
	
		else if(stationName.length() < 3 || stationName.length() > 20)
		{
			setModalMessage2("La longitud del nombre de la estación no es correcta. Recuerde que debe contener entre 3 y 20 caracteres.");
			setShowModal2(true);
		}
	

		else {
			setShowModal2(false);
			setShowConfirmation(true);
			setModalMessage2(null);
			setConfirmMessage("¿Está seguro de realizar la transacción?");
		}
		return null;		
	}
	
	/**
	 * Saves Recharge Station.
	 */
	public String save() {
		setModalMessage2(null);
		String result = station.insertStation(stationCode, stationName, companyId, departmentId, 
				SessionUtil.ip(), SessionUtil.sessionUser().getUserId(),stationTypeId, Long.parseLong(numberLanes));
		if(result != null ){
			System.out.println("if 1");
			setModalMessage(result);
			setShowModal(true);
			setShowModal2(false);
			setShowConfirmation(false);
		} else {
			System.out.println("if 2");
			setModalMessage2("Error en Transacción");
			setModalMessage(null);
			setShowModal(false);
			setShowModal2(true);
			setShowConfirmation(false);
		}
		
		return null;
	}
	
	// Getters and Setters ----------- //

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
	 * @param showConfirmation the showConfirmation to set
	 */
	public void setShowConfirmation(boolean showConfirmation) {
		this.showConfirmation = showConfirmation;
	}

	/**
	 * @return the showConfirmation
	 */
	public boolean isShowConfirmation() {
		return showConfirmation;
	}

	/**
	 * @param confirmMessage the confirmMessage to set
	 */
	public void setConfirmMessage(String confirmMessage) {
		this.confirmMessage = confirmMessage;
	}

	/**
	 * @return the confirmMessage
	 */
	public String getConfirmMessage() {
		return confirmMessage;
	}	

	/**
	 * @param stationCode the stationCode to set
	 */
	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	/**
	 * @return the stationCode
	 */
	public String getStationCode() {
		return stationCode;
	}

	/**
	 * @param stationName the stationName to set
	 */
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	/**
	 * @return the stationName
	 */
	public String getStationName() {
		return stationName;
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
	 * @param departmentId the departmentId to set
	 */
	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	/**
	 * @return the departmentId
	 */
	public Long getDepartmentId() {
		return departmentId;
	}

	/**
	 * @param companyList the companyList to set
	 */
	public void setCompanyList(List<SelectItem> companyList) {
		this.companyList = companyList;
	}

	/**
	 * @return the companyList
	 */
	public List<SelectItem> getCompanyList() {
		if(companyList == null) {
			companyList = new ArrayList<SelectItem>();
			companyList.add(new SelectItem(-1L, "--Seleccione La empresa Asociada a la estación--"));
			for(TbCompany c : company.getCompany()) {
				companyList.add(new SelectItem(c.getCompanyId(), c.getCompanyName()));
			}
		}
		return companyList;
	}

	/**
	 * @param departments the departments to set
	 */
	public void setDepartments(List<SelectItem> departments) {
		this.departments = departments;
	}

	/**
	 * @return the departments
	 */
	public List<SelectItem> getDepartments() {
		if(departments == null){
			departments = new ArrayList<SelectItem>();
			departments.add(new SelectItem(-1L, "--Seleccione Uno--"));
			for (TbDepartment d : master.getDepartments()) {
				departments.add(new SelectItem(d.getDepartmentId(), d.getDepartmentName()));
			}
		}
		return departments;
	}

	/**
	 * @param stations the stations to set
	 */
	public void setStations(List<TbStationBO> stations) {
		this.stations = stations;
	}

	/**
	 * @return the stations
	 */
	public List<TbStationBO> getStations() {
		if(stations == null) {
			stations = new ArrayList<TbStationBO>();
			stations = station.getStationList();
		}
		return stations;
	}

	/**
	 * @param stationTypeId the stationTypeId to set
	 */
	public void setStationTypeId(Long stationTypeId) {
		this.stationTypeId = stationTypeId;
	}

	/**
	 * @return the stationTypeId
	 */
	public Long getStationTypeId() {
		return stationTypeId;
	}

	/**
	 * @param stationTypeList the stationTypeList to set
	 */
	public void setStationTypeList(List<SelectItem> stationTypeList) {
		this.stationTypeList = stationTypeList;
	}

	/**
	 * @return the stationTypeList
	 */
	public List<SelectItem> getStationTypeList() {
		if (stationTypeList == null) {
			stationTypeList = new ArrayList<SelectItem>();
			stationTypeList.add(new SelectItem(-1L, "--Seleccione Uno--"));
			for (TbStationType st : station.getStationType()) {
				stationTypeList.add(new SelectItem(st.getStationTypeId(), st
						.getStationTypeName()));
			}
		}
		return stationTypeList;
	}

	/**
	 * @param numberLanes the numberLanes to set
	 */
	public void setNumberLanes(String numberLanes) {
		this.numberLanes = numberLanes;
	}

	/**
	 * @return the numberLanes
	 */
	public String getNumberLanes() {
		return numberLanes;
	}

	/**
	 * @param showModal2 the showModal2 to set
	 */
	public void setShowModal2(boolean showModal2) {
		this.showModal2 = showModal2;
	}

	/**
	 * @return the showModal2
	 */
	public boolean isShowModal2() {
		return showModal2;
	}

	/**
	 * @param modalMessage2 the modalMessage2 to set
	 */
	public void setModalMessage2(String modalMessage2) {
		this.modalMessage2 = modalMessage2;
	}

	/**
	 * @return the modalMessage2
	 */
	public String getModalMessage2() {
		return modalMessage2;
	}
}