/**
 * 
 */
package reportBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import jpa.TbCodeType;
import jpa.TbSystemUser;

import ejb.User;

import net.sf.jasperreports.engine.JRDataSource;
import report.AbstractBaseReportBean;
import util.ConnectionData;
import validator.Validation;

/**
 * @author tmolina
 *
 */
public class UserPermReportBean extends AbstractBaseReportBean implements
		Serializable {
	private static final long serialVersionUID = 6799575818990317789L;

	@EJB(mappedName="ejb/User")
	private User userEJB;
	
	private ConnectionData connection = new ConnectionData();
	
	private final String COMPILE_FILE_NAME = "userRoleReport";
	
	// Attributes ---------------------- //
	
	private List<SelectItem> codeTypeList;
	
	private Long codeTypeId;
	
	private String userCode;
	
	private boolean showMessageError;
	
	private String messageError;
	
	private boolean showData;
	
	private TbSystemUser user;
	
	/**
	 * Constructor
	 */
	public UserPermReportBean() {
	}
	
	/**
	 * 
	 */
	@PostConstruct
	public void init() {
		codeTypeList = new ArrayList<SelectItem>();
		for(TbCodeType c : userEJB.getCodeTypes()) {
			codeTypeList.add(new SelectItem(c.getCodeTypeId(), c.getCodeTypeDescription()));
		}
	}
	
	// Actions ------------------------- //
	
	public String hideModal()
	{
		setShowData(false);
		setShowMessageError(false);
		setMessageError("");
		return null;
	}
	
	
	/**
	 * 
	 */
	public String search() {
		if(userCode.equals(null) || userCode.equals(""))
		{
			setShowData(false);
			setShowMessageError(true);
			setMessageError("Digite el Número de Identificación del Usuario.");
		}
		else if(!Validation.isNumeric(userCode)){
			setShowData(false);			
			setMessageError("El Número de Identificación del Usuario contiene caracteres inválidos.");
			setShowMessageError(true);
		}
		else
		{
			user = userEJB.getUserByCode(userCode, codeTypeId);
			if(user!= null) {
				setShowData(true);
				setShowMessageError(false);
			} else {
				setShowData(false);
				setShowMessageError(true);
				setMessageError("El Usuario digitado no se encuentra registrado en el sistema.");
			}
		}
		return null;
	}
	
	/**
	 * 
	 */
	public String execute(){
		System.out.println("Enters");
		try {
			super.prepareReport();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// Overwritten methods --------------- //

	@Override
	protected String getCompileFileName() {
		return COMPILE_FILE_NAME;
	}

	/*
	 * (non-Javadoc)
	 * @see report.AbstractBaseReportBean#getFileName()
	 */
	@Override
	protected String getFileName() {
		return "Reporte_Roles_Usuario_" + System.currentTimeMillis();
	}

	/*
	 * (non-Javadoc)
	 * @see report.AbstractBaseReportBean#getJRDataSource()
	 */
	@Override
	protected JRDataSource getJRDataSource() {
		return null;
	}

	/* (non-Javadoc)
	 * @see report.AbstractBaseReportBean#getReportParameters()
	 */
	@Override
	protected Map<String, Object> getReportParameters() {
		Map<String, Object> reportParameters = new HashMap<String, Object>();	
		
		String filterValue = "";
		filterValue = user.getUserId().toString();

		reportParameters.put("filterValue", filterValue);
		reportParameters.put("userName", user.getUserNames() + " "
				+ user.getUserSecondNames());
		
		if(super.getExportOption().equals(ExportOption.EXCEL)){
			reportParameters.put("IS_IGNORE_PAGINATION", true);
		}
		return reportParameters;
	}
	
	/*
	 * (non-Javadoc)
	 * @see report.AbstractBaseReportBean#getDataConnection()
	 */
	@Override
	protected ConnectionData getDataConnection() {
		return connection;
	}
	
	// Getters and Setters ------------------ //

	/**
	 * @param userCode the userCode to set
	 */
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	/**
	 * @return the userCode
	 */
	public String getUserCode() {
		return userCode;
	}

	/**
	 * @param showMessageError the showMessageError to set
	 */
	public void setShowMessageError(boolean showMessageError) {
		this.showMessageError = showMessageError;
	}

	/**
	 * @return the showMessageError
	 */
	public boolean isShowMessageError() {
		return showMessageError;
	}

	/**
	 * @param messageError the messageError to set
	 */
	public void setMessageError(String messageError) {
		this.messageError = messageError;
	}

	/**
	 * @return the messageError
	 */
	public String getMessageError() {
		return messageError;
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
	 * @param codeTypeList the codeTypeList to set
	 */
	public void setCodeTypeList(List<SelectItem> codeTypeList) {
		this.codeTypeList = codeTypeList;
	}

	/**
	 * @return the codeTypeList
	 */
	public List<SelectItem> getCodeTypeList() {
		return codeTypeList;
	}

	/**
	 * @param codeTypeId the codeTypeId to set
	 */
	public void setCodeTypeId(Long codeTypeId) {
		this.codeTypeId = codeTypeId;
	}

	/**
	 * @return the codeTypeId
	 */
	public Long getCodeTypeId() {
		return codeTypeId;
	}
}