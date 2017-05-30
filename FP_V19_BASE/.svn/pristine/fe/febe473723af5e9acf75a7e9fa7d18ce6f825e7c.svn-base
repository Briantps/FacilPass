package reportBean;


import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import ejb.User;

import jpa.TbCodeType;
import jpa.TbSystemUser;

import net.sf.jasperreports.engine.JRDataSource;
import report.AbstractBaseReportBean;
import util.ConnectionData;

/**
 * 
 * @author psanchez
 */
public class ReconciliationReportBean extends AbstractBaseReportBean implements Serializable{
	
	@EJB(mappedName="ejb/User")
	private User userEJB;
	
	// Attributes --- //
	private static final long serialVersionUID = 1L;

	private TbSystemUser userId;
    private List<SelectItem> codeTypeList;
	private Long codeTypeId;
	private String nit;
	private Date fechaActual;
    private Date initDate;
    private Date endDate;
    
	
	// Modal --- //	
    private boolean showData;
	private boolean showModal;
	private String messageModal;
	
	private ConnectionData connection= new ConnectionData();
	private final String COMPILE_FILE_NAME = "reconciliationReport";
	
	public ReconciliationReportBean(){
		initDate= new Date();
		endDate=new Date();
		fechaActual  = new Date();
		this.setNit("");
	}
	
	
	@PostConstruct
	public void init() {
		this.getCodeTypeList();
	}
	
	public String generar(){
		try {
			 super.prepareReport();
		} catch (Exception e) {
			System.out.println("Error ReconciliationReportBean");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Clear modal panel data.
	 */
	public String hideModal(){
		setMessageModal("");
		setShowModal(false);
		return null;
	}
	
	
	/**
	 * Método encargado de validar los datos ingresados en los filtros (nit, fecha inicial, fecha final) y generar el reporte reconciliationReport.
	 * @author psanchez
	 */
	public String execute(){
	   try {
		   if(postValidationNew()){
	        if (!nit.equals("")) {
	        	userId = userEJB.getUserByCode(nit, codeTypeId);
	        	if(userId!= null) {
		        	if(initDate != null && endDate != null) {
				    	  if(endDate.getTime()>= initDate.getTime()) {
				    		  if(initDate.after(fechaActual) || endDate.after(fechaActual)){
				    			  this.setMessageModal("Error: La fecha inicial y/o la fecha final no debe ser mayor a la actual.");
								  this.setShowData(false);
								  this.setShowModal(true);
				    	      }else{
				    	    	  this.setShowData(true);
								  this.setShowModal(false);
						      }
				    	    }else{
								this.setMessageModal("Error: La Fecha Inicial debe ser menor a la Fecha Final. Verifique.");
								this.setShowData(false);
								this.setShowModal(true);
						    }
					  }else{
						 this.setMessageModal("Error: La Fecha Inicial y la Fecha Final no pueden estar vacías.");
						 this.setShowData(false);
						 this.setShowModal(true);
					  }
	        	}else {
	        		this.setShowData(false);
	        		this.setShowModal(true);
	        		this.setMessageModal("El Cliente digitado no se encuentra registrado en el sistema.");
				}	
		     }else{
		    	 this.setMessageModal("Digite el Número de Identificación del Cliente.");
		    	 this.setShowData(false);
		    	 this.setShowModal(true);
		     	}
		    } 
		 } catch (Exception e) {
				System.out.println("Error ReconciliationReportBean");
				e.printStackTrace();
		}
		return null;
	}

	
	 private boolean postValidationNew(){
			if(nit!="" && (!nit.matches("([0-9])+"))){
				this.setMessageModal("El campo No. de Identificación tiene caracteres inválidos. Verifique.");
				this.setShowModal(true);
				return false;
			}
			return true;		
		}
	
	
	/* (non-Javadoc)
	 * @see report.AbstractBaseReportBean#getReportParameters()
	 */
	@Override
	protected Map<String, Object> getReportParameters() {
		Map<String, Object> reportParameters = new HashMap<String, Object>();	
		Timestamp fechaInicial = new Timestamp(initDate.getTime());
		Timestamp fechaFinal = new Timestamp(endDate.getTime() + 86399000);
		    reportParameters.put("logo","FACILPASS");
		    reportParameters.put("title","Reconciliación");
		    reportParameters.put("userId", userId.getUserId().toString());
			reportParameters.put("initDate", fechaInicial);
			reportParameters.put("endDate", fechaFinal);
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
		return "Report_Reconciliation" + System.currentTimeMillis();
	}

	@Override
	protected JRDataSource getJRDataSource() {
		return null;
	}
	
	// Getters and Setters --- //

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
	
	public void setNit(String nit) {
		this.nit = nit;
	}

	public String getNit() {
		return nit;
	}

	public void setInitDate(Date initDate) {
		this.initDate = initDate;
	}

	public Date getInitDate() {
		return initDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getEndDate() {
		return endDate;
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
	
	public void ocult(){
		this.setShowData(false);
	}


	public void setCodeTypeList(List<SelectItem> codeTypeList) {
		this.codeTypeList = codeTypeList;
	}


	public List<SelectItem> getCodeTypeList() {
		codeTypeList = new ArrayList<SelectItem>();
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


	public void setFechaActual(Date fechaActual) {
		this.fechaActual = fechaActual;
	}


	public Date getFechaActual() {
		return fechaActual;
	}
}
