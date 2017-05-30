package reportBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import net.sf.jasperreports.engine.JRDataSource;

import report.AbstractBaseReportBean;
import sessionVar.SessionUtil;
import util.ConnectionData;

import ejb.Role;
import ejb.User;

import jpa.TbAccount;
import jpa.TbCodeType;
import jpa.TbSystemUser;
import jpa.TbUserData;


/**
 * Bean to manage note account creation.
 * @author ablanco
 *
 */
public class InvoiceBean extends AbstractBaseReportBean implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@EJB(mappedName="ejb/User")
	private User user;
	
	@EJB(mappedName="ejb/Role")
	private Role role;
	
	// Attributes ------------- //
	
	private Long idAccount;
	
	private List<TbCodeType> codeTypes;
	
	private List<SelectItem> codeTypesList;
	
	private Long codeType;
	
	private String codeClient;
	
	private List<SelectItem> clientNames;
	
	private Long idClient;
	
	private List<TbSystemUser> clients; 
	
	private TbSystemUser client;
	
    // Control Visibility -----//
	
	private boolean showData;
	
	private boolean showMessage;
	
	// Control Modal ------//
	
	private boolean showModal;
	
	private String modalMessage;
	
	private List<TbAccount> accountList;
	
	private boolean showInvoice;
	
	private Date date;
	
	private Long typeId;
	
	private List<SelectItem> types;
	
	private ConnectionData connection= new ConnectionData();
	
	private final String COMPILE_FILE_NAME = "invoiceReport";

	private TbUserData userData;
	
	private String names;
	
	private String address;
	
	private String phone;
	
	private String city;
	
	private boolean disabled;
	
	private TbSystemUser tu;
	
	private boolean showReport;
	
	/**
	 * Constructor.
	 */
	public InvoiceBean() {
	   super();
       date=new Date();      
	}
	
	@PostConstruct
	public void init2(){
		tu=SessionUtil.sessionUser();
	    idClient=tu.getUserId();
		boolean res=role.isClient(idClient);
	       
		if(res==true){
		    codeClient= tu.getUserCode();
		    codeType=tu.getTbCodeType().getCodeTypeId();
			disabled=true;
		}
		else{
		    disabled=false;
		}
		System.out.println("res " + res);
		System.out.println("idcliente " + idClient);
		System.out.println("codeClient " + codeClient);
		System.out.println("codeType " + codeType);
		System.out.println("disabled " + disabled);
		   
	}
	
	/**
	 * @param disabled the disabled to set
	 */
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	/**
	 * @return the disabled
	 */
	public boolean isDisabled() {
		return disabled;
	}
	
	
	/**
	 * 
	 */
	public String printPDF(){
		try {
			super.prepareReport();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 
	 */
	public void init() {
		setCodeTypesList(null);
		setClientNames(null);
		setClients(null);
		setShowData(false);
		setShowMessage(false);
		setCodeClient("");

	}
	
	// Actions ------------------- //
	
	/**
	 * 
	 */
	public String changeTypeDoc() {
		setShowModal(false);
		showMessage = false;
		showData = false;
		this.idClient = null;
		this.codeClient = "";
		return null;
	}
	
	/**
	 * Searches a client by code and type code.
	 */
	public String search() {
		setShowModal(false);
		this.setShowInvoice(false);
		idClient = -1L;
		if (codeClient != "") {
			client = user.getUserByCode(codeClient, codeType);
			if (client != null) {
				idClient = client.getUserId();
				accountList = user.getClientAccount(idClient);
				setShowData(true);
				setShowMessage(false);
			} else {
				setShowMessage(true);
				setModalMessage("Error: No hay información para el cliente digitado." );
				setShowData(false);
			}
		} else {
			setModalMessage("Error: Debe digitar el número de identificación del cliente." );
			setShowMessage(true);
			setShowData(false);
		}
		System.out.println("codeClient" + codeClient);
		return null;
	}
	
	public String findUserData(){
		userData=user.getUserDataById(idClient);
		if(userData!=null){
			names=userData.getUserDataOfficeName();
			address=userData.getUserDataAddress();
			phone=String.valueOf(userData.getUserDataPhone());
			city=userData.getTbMunicipality().getMunicipalityName();	
		}
		
	
		return null;
	}
	
	/*
	 * Change the data of client.
	 */
	public String changeClient() {
		setShowModal(false);
		showMessage = false;
		if (idClient.longValue() == -1) {
			showData = false;
			this.codeType = null;
			this.codeClient = "";
		} else {
			this.showData = false;
			for(TbSystemUser u : getClients()){
				if(idClient.longValue() == u.getUserId().longValue()){
					accountList = user.getClientAccount(idClient);
					this.client = u;
					this.codeType = u.getTbCodeType().getCodeTypeId();
					this.codeClient = u.getUserCode();
					this.showData = true;
					break;
				}
			}
		}
		return null;
	}
	
	public String getInvoice(){
		
		
		return null;
	}
	
	/**
	 * Clean Modal Panel.
	 */
	public String hideModal(){
		setShowModal(false);
		setShowMessage(false);
		setModalMessage("");


		return null;
	}
	
	public String hideModal2(){

		setShowModal(false);
		setModalMessage("");


		return null;
	}

	public void showIn(){
		this.setShowInvoice(true);
	}
	
	// Getters and setters -------------- //

	/**
	 * @param codeTypesList the codeTypesList to set
	 */
	public void setCodeTypesList(List<SelectItem> codeTypesList) {
		this.codeTypesList = codeTypesList;
	}

	/**
	 * @return the codeTypesList
	 */
	public List<SelectItem> getCodeTypesList() {
		if(codeTypesList==null){
			codeTypesList = new ArrayList<SelectItem>();
			for (TbCodeType type : getCodeTypes()) {
				codeTypesList.add(new SelectItem(type.getCodeTypeId(), type
						.getCodeTypeAbbreviation()));
			}
		}
		return codeTypesList;
	}

	/**
	 * @param codeType the codeType to set
	 */
	public void setCodeType(Long codeType) {
		this.codeType = codeType;
	}

	/**
	 * @return the codeType
	 */
	public Long getCodeType() {
		return codeType;
	}

	/**
	 * @param codeClient the codeClient to set
	 */
	public void setCodeClient(String codeClient) {
		this.codeClient = codeClient;
	}

	/**
	 * @return the codeClient
	 */
	public String getCodeClient() {
		return codeClient;
	}

	/**
	 * @param clientNames the clientNames to set
	 */
	public void setClientNames(List<SelectItem> clientNames) {
		this.clientNames = clientNames;
	}

	/**
	 * @return the clientNames
	 */
	public List<SelectItem> getClientNames() {
		if (clientNames == null) {
			clientNames = new ArrayList<SelectItem>();
			
			clientNames.add(new SelectItem(-1, " "));
			for(TbSystemUser tu : getClients()){
				String name = tu.getUserNames();
				if(tu.getTbCodeType().getCodeTypeId().longValue() != 3){
					name = name + " " + tu.getUserSecondNames();
				}
				clientNames.add(new SelectItem(tu.getUserId(), name));
			}
		}		
		return clientNames;
	}

	/**
	 * @param idClient the idClient to set
	 */
	public void setIdClient(Long idClient) {
		this.idClient = idClient;
	}

	/**
	 * @return the idClient
	 */
	public Long getIdClient() {
		return idClient;
	}

	/**
	 * @param clients the clients to set
	 */
	public void setClients(List<TbSystemUser> clients) {
		this.clients = clients;
	}

	/**
	 * @return the clients
	 */
	public List<TbSystemUser> getClients() {
		if (clients == null) {
			clients = new ArrayList<TbSystemUser>();
			clients = user.getAllActiveClient();
		}
		return clients;
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
	 * @param client the client to set
	 */
	public void setClient(TbSystemUser client) {
		this.client = client;
	}

	/**
	 * @return the client
	 */
	public TbSystemUser getClient() {
		return client;
	}

	/**
	 * @param codeTypes the codeTypes to set
	 */
	public void setCodeTypes(List<TbCodeType> codeTypes) {
		this.codeTypes = codeTypes;
	}

	/**
	 * @return the codeTypes
	 */
	public List<TbCodeType> getCodeTypes() {
		if(codeTypes == null){
			codeTypes = new ArrayList<TbCodeType>();
		}
		codeTypes = user.getCodeTypes();
		return codeTypes;
	}


	/**
	 * @param accountList the accountList to set
	 */
	public void setAccountList(List<TbAccount> accountList) {
		this.accountList = accountList;
	}

	/**
	 * @return the accountList
	 */
	public List<TbAccount> getAccountList() {
		return accountList;
	}


	public void setIdAccount(Long idAccount) {
		this.idAccount = idAccount;
	}

	public Long getIdAccount() {
		return idAccount;
	}

	public void setShowInvoice(boolean showInvoice) {
		this.showInvoice = showInvoice;
	}

	public boolean isShowInvoice() {
		return showInvoice;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDate() {
		return date;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public Long getTypeId() {
		return typeId;
	}

	public void setTypes(List<SelectItem> types) {
		this.types = types;
	}

	public List<SelectItem> getTypes() {
		types= new ArrayList<SelectItem> ();
		types.add(new SelectItem(1,"Recargas"));
		types.add(new SelectItem(2,"Descuentos"));
		return types;
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
		return "Generating_Invoice" + System.currentTimeMillis();
	}

	@Override
	protected JRDataSource getJRDataSource() {
		return null;
	}

	public void setConnection(ConnectionData connection) {
		this.connection = connection;
	}

	public ConnectionData getConnection() {
		return connection;
	}

	public String getCOMPILE_FILE_NAME() {
		return COMPILE_FILE_NAME;
	}

	/* (non-Javadoc)
	 * @see report.AbstractBaseReportBean#getReportParameters()
	 */
	@Override
	protected Map<String, Object> getReportParameters() {
		Map<String, Object> reportParameters = new HashMap<String, Object>();	
	    this.findUserData();
		reportParameters.put("accountId", idAccount);
		reportParameters.put("date", date);
		reportParameters.put("typeId", typeId);
		reportParameters.put("names", names);
		reportParameters.put("address", address);
		reportParameters.put("phone", phone);
		reportParameters.put("cod", codeClient);
		reportParameters.put("city", city);
		return reportParameters;
	}

	public void setUserData(TbUserData userData) {
		this.userData = userData;
	}

	public TbUserData getUserData() {
		return userData;
	}

	public void setNames(String names) {
		this.names = names;
	}

	public String getNames() {
		return names;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress() {
		return address;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhone() {
		return phone;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCity() {
		return city;
	}

	/**
	 * @param tu the tu to set
	 */
	public void setTu(TbSystemUser tu) {
		this.tu = tu;
	}

	/**
	 * @return the tu
	 */
	public TbSystemUser getTu() {
		return tu;
	}

	/**
	 * @param showReport the showReport to set
	 */
	public void setShowReport(boolean showReport) {
		this.showReport = showReport;
	}

	/**
	 * @return the showReport
	 */
	public boolean isShowReport() {
		return showReport;
	}

    public void show(){
    	this.setShowReport(true);
    }

    public void ocult(){
    	this.setShowReport(false);
    }
}