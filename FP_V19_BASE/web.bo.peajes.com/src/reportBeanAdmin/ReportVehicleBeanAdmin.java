package reportBeanAdmin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import jpa.TbCategory;
import jpa.TbCodeType;
import net.sf.jasperreports.engine.JRDataSource;
import report.AbstractBaseReportBean;
import util.ConnectionData;
import ejb.Category;
import ejb.User;
import ejb.Vehicle;

/**
 * Bean implementation of Report Vehicle
 * @author Julian Romero jromero
 */

public class ReportVehicleBeanAdmin extends AbstractBaseReportBean implements Serializable {

	private static final long serialVersionUID = 7032048764316281149L;

	@EJB(mappedName="ejb/Vehicle")
	private Vehicle vehicleEJB;

	@EJB(mappedName="ejb/Category")
	private Category categoryEJB;

	@EJB(mappedName="ejb/User")
	private User userEJB;

	///Filter fields
	private List<SelectItem> codeTypesList;
	private Long codeType=0L;
	private String numberDoc="";
	private String firstName="";
	private String lastName="";
	private String email="";
	private String account="";
	private Long categoryType=0L;
	private String placaFil="";
	private String ad1Fil="";
	private String ad2Fil="";
	private String ad3Fil="";
	private Date dateIni;
	private Date dateEnd;
	private Date dateCurr;
	private boolean showReport=false;
	private boolean showModal;
	private String msg;

	private String consulta="";

	///Report 
	private final String COMPILE_FILE_NAME="reportVehiclesAdmin";
	private ConnectionData connection=new ConnectionData();

	public ReportVehicleBeanAdmin(){
		super();
	}
	
	@SuppressWarnings("unused")
	@PostConstruct
	private void init() {
		setDateCurr(new Date());
		System.out.println("ReportVehicleBeanAdmin.dateCurr: "+dateCurr);
		setDateIni(new Date());
		setDateEnd(new Date());
	}

	public String hideModal() {
		this.setMsg("");
		this.setShowModal(false);
		this.setShowReport(false);
		return null;
	}

	public String hideModalAll() {
		this.setMsg("");
		this.setShowModal(false);
		this.setShowReport(false);
		this.setConsulta("");
		return null;
	}

	public void searchFilter(){
		hideModalAll();
		System.out.println("searchFilter.codeType: "+codeType);
		System.out.println("searchFilter.numberDoc: "+numberDoc);
		System.out.println("searchFilter.firstName: "+firstName);
		System.out.println("searchFilter.lastName: "+lastName);
		System.out.println("searchFilter.email: "+email);
		System.out.println("searchFilter.account: "+account);
		System.out.println("searchFilter.categoryType: "+categoryType);
		System.out.println("searchFilter.placaFil: "+placaFil);
		System.out.println("searchFilter.ad1Fil: "+ad1Fil);
		System.out.println("searchFilter.ad2Fil: "+ad2Fil);
		System.out.println("searchFilter.ad3Fil: "+ad3Fil);
		System.out.println("searchFilter.dateIni: "+dateIni);
		System.out.println("searchFilter.dateEnd: "+dateEnd);
		if(postValidationSearch()){
			consulta=vehicleEJB.getVehiclesByReport(codeType, numberDoc, firstName, lastName,
					email, account, categoryType, placaFil, ad1Fil, ad2Fil,
					ad3Fil, dateIni, dateEnd);
			this.setShowModal(false);
			this.setShowReport(true);
		}
	}

	public void ocult(){
		this.setShowReport(false);
	}

	private boolean postValidationSearch(){
		if(numberDoc!="" && !numberDoc.matches("([0-9])+")){
			this.setMsg("El campo No.Identificación tiene caracteres inválidos. Verifique.");
			this.setShowModal(true);
			return false;
		}else if(firstName!="" && !firstName.matches("([a-z]|[A-Z]|[áéíóúü]|[ÁÉÍÓÚÜ]|[0-9]|[&/]|[_-]|[.]|[ñÑ]|\\s)+")){
			this.setMsg("El campo Nombre tiene caracteres inválidos. Verifique.");
			this.setShowModal(true);
			return false;
		}else if(lastName!="" && !lastName.matches("([a-z]|[A-Z]|[áéíóúü]|[ÁÉÍÓÚÜ]|[ñÑ]|\\s)+")){
			this.setMsg("El campo Apellidos tiene caracteres inválidos. Verifique.");
			this.setShowModal(true);
			return false;
		}else if(email!="" && !email.matches("([sa-zA-Z@.+_0-9-]|\\s)+")){
			this.setMsg("El campo Usuario tiene caracteres inválidos. Verifique.");
			this.setShowModal(true);
			return false;
		}else if(account!="" && !account.matches("([0-9])+")){
			this.setMsg("El campo Cuenta tiene caracteres inválidos. Verifique.");
			this.setShowModal(true);
			return false;
		}else if(placaFil!=""&&!placaFil.matches("([0-9]|[a-z]|[A-Z])+")){
			this.setMsg("El campo Placa tiene caracteres inválidos. Verifique.");
			this.setShowModal(true);
			return false;
		}else if(ad1Fil!=""&&!ad1Fil.matches("([a-z]|[A-Z]|[0-9]|[/]|[(]|[)]|[_-]|[.]|[ñÑ]|\\s)+")){
			this.setMsg("El campo Adicional 1 tiene caracteres inválidos. Verifique.");
			this.setShowModal(true);
			return false;
		}else if(ad2Fil!=""&&!ad2Fil.matches("([a-z]|[A-Z]|[0-9]|[/]|[(]|[)]|[_-]|[.]|[ñÑ]|\\s)+")){
			this.setMsg("El campo Adicional 2 tiene caracteres inválidos. Verifique.");
			this.setShowModal(true);
			return false;
		}else if(ad3Fil!=""&&!ad3Fil.matches("([a-z]|[A-Z]|[0-9]|[/]|[(]|[)]|[_-]|[.]|[ñÑ]|\\s)+")){
			this.setMsg("El campo Adicional 3 tiene caracteres inválidos. Verifique.");
			this.setShowModal(true);
			return false;
		}else if(dateIni == null && dateEnd != null) {
			this.setMsg("El campo Fecha Inicial está vacío.");
			this.setShowModal(true);
			return false;
		}else if(dateIni != null && dateEnd == null) {
			this.setMsg("El campo Fecha Final está vacío.");
			this.setShowModal(true);
			return false;
		}else if((dateIni!=null&&dateEnd!=null)&&(dateIni.getTime()>dateEnd.getTime())) {
			this.setMsg("La Fecha Inicial debe ser menor a la Fecha Final. Verifique.");
			this.setShowModal(true);
			return false;
		}else if((dateIni!=null&&dateEnd!=null)&&(dateIni.getTime()>dateCurr.getTime())) {
			this.setMsg("La Fecha Inicial debe ser menor a la Fecha Actual. Verifique.");
			this.setShowModal(true);
			return false;
		}else if((dateIni!=null&&dateEnd!=null)&&(dateEnd.getTime()>dateCurr.getTime())) {
			this.setMsg("La Fecha Final debe ser menor a la Fecha Actual. Verifique.");
			this.setShowModal(true);
			return false;
		}
		return true;
	}

	public void clearFilter(){
		dateCurr=new Date();
		codeType = -1L;
		numberDoc = "";
		firstName ="";
		lastName = "";
		categoryType = -1L;
		placaFil = "";
		ad1Fil ="";
		ad2Fil ="";
		ad3Fil ="";
		account = "";
		email = "";
		dateIni=new Date();
		dateEnd=new Date();
		consulta="";
		showReport=false;
	}

	public String printPdf(){
		try {
			super.prepareReport();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setShowModal(boolean showModal) {
		this.showModal = showModal;
	}
	public boolean isShowModal() {
		return showModal;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getMsg() {
		return msg;
	}

	public Long getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(Long categoryType) {
		this.categoryType = categoryType;
	}

	public String getPlacaFil() {
		return placaFil;
	}

	public void setPlacaFil(String placaFil) {
		this.placaFil = placaFil;
	}

	public String getAd1Fil() {
		return ad1Fil;
	}

	public void setAd1Fil(String ad1Fil) {
		this.ad1Fil = ad1Fil;
	}

	public String getAd2Fil() {
		return ad2Fil;
	}

	public void setAd2Fil(String ad2Fil) {
		this.ad2Fil = ad2Fil;
	}

	public String getAd3Fil() {
		return ad3Fil;
	}

	public void setAd3Fil(String ad3Fil) {
		this.ad3Fil = ad3Fil;
	}

	public List<SelectItem> getListCategories(){
		List<TbCategory> categories = categoryEJB.getCategories();
		List<SelectItem> list = new ArrayList<SelectItem>();
		list.add(new SelectItem(-1L,"-- Seleccione Uno --"));
		for (TbCategory cat : categories) {
			list.add(new SelectItem(cat.getCategoryId(), cat.getCategoryName()));
		}
		return list;
	}

	public void setCodeTypesList(List<SelectItem> codeTypesList) {
		this.codeTypesList = codeTypesList;
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

	public Long getCodeType() {
		return codeType;
	}

	public void setCodeType(Long codeType) {
		this.codeType = codeType;
	}

	public String getNumberDoc() {
		return numberDoc;
	}

	public void setNumberDoc(String numberDoc) {
		this.numberDoc = numberDoc;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public boolean isShowReport() {
		return showReport;
	}

	public void setShowReport(boolean showReport) {
		this.showReport = showReport;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Date getDateIni() {
		return dateIni;
	}

	public void setDateIni(Date dateIni) {
		this.dateIni = dateIni;
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
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
		return "ReportVehiclesAdmin" + System.currentTimeMillis();
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

	@Override
	protected Map<String, Object> getReportParameters() {
		Map<String, Object> reportParameters = new HashMap<String, Object>();
		reportParameters.put("queryString", consulta);
		if(super.getExportOption().equals(ExportOption.EXCEL)){
			reportParameters.put("IS_IGNORE_PAGINATION", true);
		}
		/*Crea iterador para recorrer la lista de parametros */
		Iterator<Entry<String,Object>> it = reportParameters.entrySet().iterator();

		/*Se inicia ciclo para recorrer lista de parametros*/
		while (it.hasNext()) {
			/*se realiza captura del objeto de acuerdo al iterado*/	
			Entry<String, Object> e = it.next();
			/*se valida si el parametro es IS_IGNORE_PAGINATION*/
			if(e.getKey().equals("IS_IGNORE_PAGINATION")){
				/*se valida si el tipo de reporte es Excel y si el valor del parametro es falso*/
				if((super.getExportOption().equals(ExportOption.EXCEL))&&(e.getValue().equals(false))){
					/*se setea nuevamente en true el parametro IS_IGNORE_PAGINATION*/				 
					reportParameters.put("IS_IGNORE_PAGINATION", true);
					System.out.println("Se cambio el parametro IS_IGNORE_PAGINATION a true");
				}
			}
		}
		System.out.println("par "+reportParameters.toString());
		return reportParameters;
	}

	public String getConsulta() {
		return consulta;
	}

	public void setConsulta(String consulta) {
		this.consulta = consulta;
	}

	public Date getDateCurr() {
		return dateCurr;
	}

	public void setDateCurr(Date dateCurr) {
		this.dateCurr = dateCurr;
	}
}