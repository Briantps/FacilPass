package reportBeanAdmin;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;
import ejb.User;

import jpa.TbCodeType;

import net.sf.jasperreports.engine.JRDataSource;

import report.AbstractBaseReportBean;
import util.ConnectionData;
import util.InfoClient;
import validator.Validation;

public class ReportLogsAdminDeviceBean extends AbstractBaseReportBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@EJB(mappedName="ejb/User")
	private User userEjb;
	
	private InfoClient cliente;
	
	private Long typeId;
	
	private String codClient;
	
	private String nomClient;
	
	private String apeClient;
	
	private String user;
	
	private String accountClient;
	
	private String plateClient;
	
	private String aditional1;
	
	private String aditional2;
	
	private String aditional3;
	
	private Date dateIni;
	
	private Date dateEnd;
	
	private List<SelectItem> typeList;
	
	private List<Long> clientList;
	
	private final String COMPILE_FILE_NAME="reportLogsAdminDevice";
	
	private String messageModal;
	
	private boolean showModal;
	
	private boolean showData;
	
	private ConnectionData connection=new ConnectionData();


	public ReportLogsAdminDeviceBean(){
		dateIni= new Date();
		dateEnd= new Date();
	}
	
	/**
	 * @param typeId the typeId to set
	 */
	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}


	/**
	 * @return the typeId
	 */
	public Long getTypeId() {
		return typeId;
	}



	/**
	 * @param codClient the codClient to set
	 */
	public void setCodClient(String codClient) {
		this.codClient = codClient;
	}

	/**
	 * @return the codClient
	 */
	public String getCodClient() {
		return codClient;
	}

	/**
	 * @param nomClient the nomClient to set
	 */
	public void setNomClient(String nomClient) {
		this.nomClient = nomClient;
	}

	/**
	 * @return the nomClient
	 */
	public String getNomClient() {
		return nomClient;
	}

	/**
	 * @param apeClient the apeClient to set
	 */
	public void setApeClient(String apeClient) {
		this.apeClient = apeClient;
	}

	/**
	 * @return the apeClient
	 */
	public String getApeClient() {
		return apeClient;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param accountClient the accountClient to set
	 */
	public void setAccountClient(String accountClient) {
		this.accountClient = accountClient;
	}

	/**
	 * @return the accountClient
	 */
	public String getAccountClient() {
		return accountClient;
	}

	/**
	 * @param plateClient the plateClient to set
	 */
	public void setPlateClient(String plateClient) {
		this.plateClient = plateClient;
	}

	/**
	 * @return the plateClient
	 */
	public String getPlateClient() {
		return plateClient;
	}

	/**
	 * @param aditional1 the aditional1 to set
	 */
	public void setAditional1(String aditional1) {
		this.aditional1 = aditional1;
	}

	/**
	 * @return the aditional1
	 */
	public String getAditional1() {
		return aditional1;
	}

	/**
	 * @param aditional2 the aditional2 to set
	 */
	public void setAditional2(String aditional2) {
		this.aditional2 = aditional2;
	}

	/**
	 * @return the aditional2
	 */
	public String getAditional2() {
		return aditional2;
	}

	/**
	 * @param aditional3 the aditional3 to set
	 */
	public void setAditional3(String aditional3) {
		this.aditional3 = aditional3;
	}

	/**
	 * @return the aditional3
	 */
	public String getAditional3() {
		return aditional3;
	}

	/**
	 * @param dateIni the dateIni to set
	 */
	public void setDateIni(Date dateIni) {
		this.dateIni = dateIni;
	}

	/**
	 * @return the dateIni
	 */
	public Date getDateIni() {
		return dateIni;
	}

	/**
	 * @param dateEnd the dateEnd to set
	 */
	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}

	/**
	 * @return the dateEnd
	 */
	public Date getDateEnd() {
		return dateEnd;
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
		if(typeList==null){
			typeList = new ArrayList<SelectItem>();
			typeList.add(new SelectItem(-1L,"--Seleccione Uno--"));
			for (TbCodeType type : userEjb.getCodeTypes()) {
				typeList.add(new SelectItem(type.getCodeTypeId(), type.getCodeTypeAbbreviation()));
			}
		}
		return typeList;
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
		return "ReportLogsAdminDevice"+System.currentTimeMillis();
	}

	@Override
	protected JRDataSource getJRDataSource() {
		return null;
	}
	
	public String printPdf(){
		try {
			 super.prepareReport();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param cliente the cliente to set
	 */
	public void setCliente(InfoClient cliente) {
		this.cliente = cliente;
	}

	/**
	 * @return the cliente
	 */
	public InfoClient getCliente() {
		return cliente;
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
	 * @param messageModal the messageModal to set
	 */
	public void setMessageModal(String messageModal) {
		this.messageModal = messageModal;
	}

	/**
	 * @return the messageModal
	 */
	public String getMessageModal() {
		return messageModal;
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

	public String search(){
		cliente=null;
		if(codClient.equals("") && nomClient.equals("") && apeClient.equals("") && user.equals("") && (accountClient!=null && accountClient.equals(""))
				&& plateClient.equals("") && aditional1.equals("") && aditional2.equals("") && aditional3.equals("") && dateIni==null && dateEnd==null){
			
			this.setMessageModal("Digite un filtro de búsqueda");
			this.setShowModal(true);
			
		}else if(!codClient.equals("") && typeId==-1L){
			this.setMessageModal("Debe seleccionar un tipo de Identificación.");
			this.setShowModal(true);
		}
		else if(!codClient.equals("") && !Validation.isNumeric(codClient)){
			this.setMessageModal("El campo No. Identificación solo debe contener números.");
			this.setShowModal(true);
		}
		else if (!nomClient.equals("") && !Validation.isAlphaNumCompany2(nomClient)){
			this.setMessageModal("El campo Nombre tiene caracteres inválidos.");
			this.setShowModal(true);
		}
		else if(!apeClient.equals("") && !Validation.apellidoCliente(apeClient)){
			this.setMessageModal("El campo Apellidos tiene caracteres inválidos.");
			this.setShowModal(true);
		}
		else if(user!="" && !Validation.isEmail2(user)){
			this.setMessageModal("El campo Usuario tiene caracteres inválidos.");
			this.setShowModal(true);
	    }
		else if(accountClient!=null && !accountClient.equals("") && !Validation.isNumeric(accountClient)){
			this.setMessageModal("El campo Cuenta FacilPass solo debe contener números.");
			this.setShowModal(true);
		}
		else if(!plateClient.equals("") && !Validation.isAlphaNum(plateClient)){
			this.setMessageModal("El campo Placa solo debe contener números y letras.");
			this.setShowModal(true);
		}
		else if(aditional1!="" && !Validation.aditional(aditional1)){
			this.setMessageModal("El campo Adicional 1 tiene caracteres inválidos.");
			this.setShowModal(true);
		}
		else if(aditional2!="" && !Validation.aditional(aditional2)){
			this.setMessageModal("El campo Adicional 2 tiene caracteres inválidos.");
			this.setShowModal(true);
		}
		else if(aditional3!="" && !Validation.aditional(aditional3)){
			this.setMessageModal("El campo Adicional 3 tiene caracteres inválidos.");
			this.setShowModal(true);
		}

		else if(dateIni==null || dateEnd==null){
			this.setMessageModal("La campo Fecha Inicial y Fecha Final no deben estar vacíos.");
			this.setShowModal(true);
		}
		else if(dateIni.after(dateEnd)){
			this.setMessageModal("La Fecha Inicial no debe ser mayor a la Fecha Final.");
			this.setShowModal(true);
		}
		else if(dateEnd.after(new Date())){
			this.setMessageModal("La Fecha Final no debe ser mayor a la fecha actual.");
			this.setShowModal(true);
		}
		else{
			this.setShowData(true);
		}

		return "";
	}
	
	
	@Override
	protected Map<String, Object> getReportParameters() {
		Map<String, Object> reportParameters = new HashMap<String, Object>();	
		Timestamp dateIni1 = new Timestamp(dateIni.getTime());
		Timestamp dateEnd1 = new Timestamp(dateEnd.getTime() + 86399999);
		this.search();
		Long cuenta=null;
		try{
			if(accountClient!=null && !accountClient.trim().equals("") ){
				cuenta= Long.parseLong(accountClient);
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		
		
		reportParameters.put("typeId", typeId!=-1L?typeId:null);
		reportParameters.put("userCode", codClient!=null?(!codClient.equals("")?codClient:null):null);
		reportParameters.put("userName", nomClient!=null?(!nomClient.equals("")?nomClient:null):null);
		reportParameters.put("userName2", apeClient!=null?(!apeClient.equals("")?apeClient:null):null);
		reportParameters.put("userEmail", user!=null?(!user.equals("")?user:null):null);
		reportParameters.put("accountId", cuenta);
		reportParameters.put("vehicle", plateClient!=null?(!plateClient.equals("")?plateClient:null):null);
		reportParameters.put("aditional1", aditional1!=null?(!aditional1.equals("")?aditional1:null):null);
		reportParameters.put("aditional2", aditional2!=null?(!aditional2.equals("")?aditional2:null):null);
		reportParameters.put("aditional3", aditional3!=null?(!aditional3.equals("")?aditional3:null):null);
		reportParameters.put("dateIni1", dateIni1);
		reportParameters.put("dateEnd1", dateEnd1);

		if(super.getExportOption().equals(ExportOption.EXCEL)){
			reportParameters.put("IS_IGNORE_PAGINATION", true);
		}
		return reportParameters;
	}
	
	public void hideModal(){
		this.setShowModal(false);
		this.setMessageModal("");
		this.setShowData(false);
	}
	
	public void ocult(){
		this.setShowData(false);
	}

	public void setClientList(List<Long> clientList) {
		this.clientList = clientList;
	}

	public List<Long> getClientList() {
		return clientList;
	}
	
}
