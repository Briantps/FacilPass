package reportBean;


import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import ejb.Charges;
import ejb.User;
import ejb.crud.Bank;

import jpa.TbAgreement;
import jpa.TbBank;
import jpa.TbChanel;
import jpa.TbCodeType;
import jpa.TbPaymentMethod;
import jpa.TbStateRecharge;

import net.sf.jasperreports.engine.JRDataSource;
import report.AbstractBaseReportBean;
import util.ConnectionData;
import validator.Validation;

/**
 * 
 * @author psanchez
 */
public class AssignmentsRechargeReportBean extends AbstractBaseReportBean implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private ConnectionData connection= new ConnectionData();
	private final String COMPILE_FILE_NAME = "assignmentsRechargeReport";
	
	@EJB(mappedName="ejb/User")
	private User userEJB;
	
	@EJB(mappedName="ejb/Charges")
	private Charges chargeEJB;
	
	@EJB(mappedName="ejb/Bank")
	private Bank bankEJB;
	
	private Date initialDate;
	private Date finalDate;
	private Date currentDate;

	private List<SelectItem> paymentMethodList;
	private Long paymentMethodId;
	
	private List<SelectItem> chanelList;
	private Long chanelId;
	
	private List<SelectItem> agreementList;
	private Long agreementId;
	
	private List<SelectItem> stateRechargeList;
	private Long stateRechargeId;
		
	private List<SelectItem> codeTypeList;
	private Long codeTypeId;
		
	private List<SelectItem> bankList;
	private Long bankId;
	
	private String userCode;
	private String userNames;
	private String userSecondNames;
	private String plate;
	private String nure;
	private String accountId;
	
	// Modal --- //	
	private boolean showData;
	private boolean showModal;
	private String messageModal;
	
	public AssignmentsRechargeReportBean(){
		initialDate  = new Date();
		finalDate    = new Date();
		currentDate  = new Date();
	}
	
	public void generar(){
		try {
			 super.prepareReport();
		} catch (Exception e) {
			System.out.println("Error assignmentsRechargeReport");
			e.printStackTrace();
		}
	}
	
	
	/**
	 * MÈtodo encargado de validar los filtros (fecha inicial, fecha final) y generar el reporte auditReconciliationReport.
	 * @author psanchez
	 */
	public void search(){
	    try{
			if(initialDate== null) {
				this.setMessageModal("La Fecha Inicial es requerida.");
				this.setShowModal(true);
				this.setShowData(false);
			}
			else if(finalDate == null) {
				this.setMessageModal("La Fecha Final es requerida.");
				this.setShowModal(true);
				this.setShowData(false);
			}
			else if(initialDate.compareTo(finalDate)>0) {
		    	this.setMessageModal("La Fecha Inicial no debe ser mayor a la Final.");
		    	this.setShowModal(true);
				this.setShowData(false);
			}
			else if(initialDate.after(currentDate)){
    	    	this.setMessageModal("La Fecha Inicial no debe ser mayor a la fecha actual.");
    	    	this.setShowModal(true);
				this.setShowData(false);
    	    }
			else if(finalDate.after(currentDate)){
    	    	this.setMessageModal("La Fecha Final no debe ser mayor a la fecha actual.");
    	    	this.setShowModal(true);
				this.setShowData(false);
    	    }
			else if(!userCode.equals("") && !Validation.isNumeric(userCode)){
				this.setMessageModal("El campo No. IdentificaciÛn contiene caracteres inv·lidos.");
				this.setShowModal(true);
				this.setShowData(false);
    	    }
			else if(userNames!="" && !userNames.matches("([a-z]|[A-Z]|[·ÈÌÛ˙]|[¡…Õ”⁄]|[0-9]|[&/]|[_-]|[Ò—]|[.]|\\s)+")){
    	    	this.setMessageModal("El campo Nombre contiene caracteres inv·lidos.");
    	    	this.setShowModal(true);
				this.setShowData(false);
    	    }
			else if(userSecondNames!="" && !userSecondNames.matches("([a-z]|[A-Z]|[·ÈÌÛ˙]|[¡…Õ”⁄]|[Ò—]|\\s)+")){
    	    	this.setMessageModal("El campo Apellidos contiene caracteres inv·lidos.");
    	    	this.setShowModal(true);
				this.setShowData(false);
    	    }
			else if(accountId!=null && !accountId.equals("") && !Validation.isNumeric(accountId)){
    	    	this.setMessageModal("El campo Cuenta FacilPass contiene caracteres inv·lidos.");
    	    	this.setShowModal(true);
				this.setShowData(false);
    	    }
			else if(!plate.equals("") && !Validation.isAlphaNum(plate)){
    	    	this.setMessageModal("El campo Placa contiene caracteres inv·lidos.");
    	    	this.setShowModal(true);
				this.setShowData(false);
    	    }
			else if(nure!=null && !nure.equals("") && !Validation.isAlphaNum(nure)){
    	    	this.setMessageModal("El campo Referencia de Recarga contiene caracteres inv·lidos.");
    	    	this.setShowModal(true);
				this.setShowData(false);
    	    }
			else{
    	    	this.setShowData(true);
    	    	this.setShowModal(false);
			} 
	    }catch (Exception e) {
			System.out.println("Reporte Asignaciones y Recargas"+e.getLocalizedMessage());
			e.printStackTrace();
		}
	 }
	
	public void clearFilter(){
		userCode="";
		userNames="";
		userSecondNames="";
		plate="";
		nure="";
		accountId="";
		paymentMethodId = -1L;
		chanelId = -1L;
		agreementId = -1L;
		codeTypeId = -1L;
		stateRechargeId = -1L;
		bankId = -1L;
		showData=false;
		initialDate  = new Date();
		finalDate    = new Date();
		currentDate  = new Date();
	}
	
	/**
	 * Clear modal panel data.
	 */
	public void hideModal(){
		setMessageModal("");
		setShowModal(false);
	}

	@Override
	protected Map<String, Object> getReportParameters() {
		Map<String, Object> reportParameters = new HashMap<String, Object>();
		Timestamp initialDate_ = new Timestamp(initialDate.getTime());
		Timestamp finalDate_   = new Timestamp(finalDate.getTime() + 86399000);
			reportParameters.put("logo","FACILPASS");
			reportParameters.put("initialDate", initialDate_);
			reportParameters.put("finalDate", finalDate_);
			reportParameters.put("paymentMethodId", paymentMethodId);
			reportParameters.put("bankId", bankId);
			reportParameters.put("chanelId", chanelId);
			reportParameters.put("agreementId", agreementId);
			reportParameters.put("codeTypeId", codeTypeId);	
			reportParameters.put("userCode", userCode);
			reportParameters.put("userNames", userNames.toUpperCase());
			reportParameters.put("userSecondNames", userSecondNames.toUpperCase());
			reportParameters.put("accountId", accountId);
			reportParameters.put("stateRechargeId", stateRechargeId);
			reportParameters.put("plate", plate.toUpperCase());
			reportParameters.put("nure", nure);
			if(super.getExportOption().equals(ExportOption.EXCEL)){
				reportParameters.put("IS_IGNORE_PAGINATION", true);
			}
		return reportParameters;
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
		return "Report_Assignments_Recharge" + System.currentTimeMillis();
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

	public void setShowData(boolean showData) {
		this.showData = showData;
	}


	public boolean isShowData() {
		return showData;
	}


	public void setShowModal(boolean showModal) {
		this.showModal = showModal;
	}


	public boolean isShowModal() {
		return showModal;
	}


	public void setMessageModal(String messageModal) {
		this.messageModal = messageModal;
	}


	public String getMessageModal() {
		return messageModal;
	}

	public void ocult(){
		this.setShowData(false);
	}
	
    public Date getInitialDate() {
		return initialDate;
	}


	public void setInitialDate(Date initialDate) {
		this.initialDate = initialDate;
	}

	public Date getFinalDate() {
		return finalDate;
	}


	public void setFinalDate(Date finalDate) {
		this.finalDate = finalDate;
	}

	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}


	public Date getCurrentDate() {
		return currentDate;
	}


	public void setCodeTypeList(List<SelectItem> codeTypeList) {
		this.codeTypeList = codeTypeList;
	}


	public List<SelectItem> getCodeTypeList() {
		if(codeTypeList == null){
			codeTypeList = new ArrayList<SelectItem>();
		} else {
			codeTypeList.clear();
		}
		codeTypeList.add(new SelectItem(-1L, "--Seleccione Uno--"));
		for(TbCodeType c : userEJB.getCodeTypes()) {
			codeTypeList.add(new SelectItem(c.getCodeTypeId(), c.getCodeTypeAbbreviation()));
		}
		return codeTypeList;
	}


	public void setCodeTypeId(Long codeTypeId) {
		this.codeTypeId = codeTypeId;
	}


	public Long getCodeTypeId() {
		return codeTypeId;
	}


	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}


	public String getUserCode() {
		return userCode;
	}


	public void setUserNames(String userNames) {
		this.userNames = userNames;
	}


	public String getUserNames() {
		return userNames;
	}


	public void setUserSecondNames(String userSecondNames) {
		this.userSecondNames = userSecondNames;
	}


	public String getUserSecondNames() {
		return userSecondNames;
	}


	public void setPlate(String plate) {
		this.plate = plate;
	}


	public String getPlate() {
		return plate;
	}


	public void setNure(String nure) {
		this.nure = nure;
	}


	public String getNure() {
		return nure;
	}


	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}


	public String getAccountId() {
		return accountId;
	}


	public void setPaymentMethodList(List<SelectItem> paymentMethodList) {
		this.paymentMethodList = paymentMethodList;
	}


	public List<SelectItem> getPaymentMethodList() {
		if(paymentMethodList == null){
			paymentMethodList = new ArrayList<SelectItem>();
		} else {
			paymentMethodList.clear();
		}
		paymentMethodList.add(new SelectItem(-1L, "--Seleccione Uno--"));
		for(TbPaymentMethod c : chargeEJB.getPaymentMethod()) {
			paymentMethodList.add(new SelectItem(c.getPaymentMethodId(), c.getPaymentMethod()));
		}
		return paymentMethodList;
	}


	public void setPaymentMethodId(Long paymentMethodId) {
		this.paymentMethodId = paymentMethodId;
	}


	public Long getPaymentMethodId() {
		return paymentMethodId;
	}


	public void setChanelList(List<SelectItem> chanelList) {
		this.chanelList = chanelList;
	}


	public List<SelectItem> getChanelList() {
		if(chanelList == null){
			chanelList = new ArrayList<SelectItem>();
		} else {
			chanelList.clear();
		}
		chanelList.add(new SelectItem(-1L, "--Seleccione Uno--"));
		for(TbChanel c : chargeEJB.getChanel()) {
			chanelList.add(new SelectItem(c.getChanelId(), c.getChanelType()));
		}
		return chanelList;
	}


	public void setChanelId(Long chanelId) {
		this.chanelId = chanelId;
	}


	public Long getChanelId() {
		return chanelId;
	}


	public void setAgreementList(List<SelectItem> agreementList) {
		this.agreementList = agreementList;
	}


	public List<SelectItem> getAgreementList() {
		if(agreementList == null){
			agreementList = new ArrayList<SelectItem>();
		} else {
			agreementList.clear();
		}
		agreementList.add(new SelectItem(-1L, "--Seleccione Uno--"));
		for(TbAgreement c : chargeEJB.getAgreement()) {
			agreementList.add(new SelectItem(c.getAgreementId(),c.getNameAgreement()));
		}
		return agreementList;
	}


	public void setAgreementId(Long agreementId) {
		this.agreementId = agreementId;
	}


	public Long getAgreementId() {
		return agreementId;
	}

	public void setStateRechargeList(List<SelectItem> stateRechargeList) {
		this.stateRechargeList = stateRechargeList;
	}

	public List<SelectItem> getStateRechargeList() {
		if(stateRechargeList == null){
			stateRechargeList = new ArrayList<SelectItem>();
		} else {
			stateRechargeList.clear();
		}
		stateRechargeList.add(new SelectItem(-1L, "--Seleccione Uno--"));
		for(TbStateRecharge c : chargeEJB.getStateRecharge()) {
			stateRechargeList.add(new SelectItem(c.getStateRechargeId(),c.getNameStateRecharge()));
		}
		return stateRechargeList;
	}

	public void setStateRechargeId(Long stateRechargeId) {
		this.stateRechargeId = stateRechargeId;
	}

	public Long getStateRechargeId() {
		return stateRechargeId;
	}

	public void setBankList(List<SelectItem> bankList) {
		this.bankList = bankList;
	}

	public List<SelectItem> getBankList() {
		if(bankList == null){
			bankList = new ArrayList<SelectItem>();
		} else {
			bankList.clear();
		}
		bankList.add(new SelectItem(-1L, "--Seleccione Uno--"));
		for(TbBank c : bankEJB.getBanks()) {
			bankList.add(new SelectItem(c.getBankId(),c.getBankName()));
		}
		return bankList;
	}

	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}

	public Long getBankId() {
		return bankId;
	}

}
