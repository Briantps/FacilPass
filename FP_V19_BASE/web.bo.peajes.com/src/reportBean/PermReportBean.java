/**
 * 
 */
package reportBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import jpa.TbRole;

import ejb.Role;

import net.sf.jasperreports.engine.JRDataSource;
import report.AbstractBaseReportBean;
import util.ConnectionData;

/**
 * @author tmolina
 *
 */
public class PermReportBean extends AbstractBaseReportBean {
	
	@EJB(mappedName="ejb/Role")
	private Role roleEJB;
	
	private ConnectionData connection = new ConnectionData();
	
	private final String COMPILE_FILE_NAME = "roleReport";
	
	// Attributes ---------------------- //
	
	private Long roleId;
	
	private List<SelectItem> roleList;
	
	/**
	 * Constructor
	 */
	public PermReportBean() {
	}
	
	@PostConstruct
	public void init() {
		roleList = new ArrayList<SelectItem>();
		roleList.add(new SelectItem("-1", "--Todos--"));
		for(TbRole r : roleEJB.getAllRoles()){
			roleList.add(new SelectItem(r.getRoleId(), r.getRoleName()));
		}	
	}
	
	
	// Actions ------------------------- //
	
	/**
	 * 
	 */
	public String execute(){
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
		return "Reporte_Permisos_Rol_" + System.currentTimeMillis();
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
		
//		String filterValue = "";
//		if (roleId.longValue() != -1L) {
//			filterValue = " WHERE rol.role_id = " + roleId + " ";
//		}
		reportParameters.put("filterValue", roleId);
		
		if(super.getExportOption().equals(ExportOption.EXCEL)){
			reportParameters.put("IS_IGNORE_PAGINATION", true);
		}
		
		return reportParameters;
	}
	
	@Override
	protected ConnectionData getDataConnection() {
		return connection;
	}
	
	// Getters and Setters ------------------ //

	/**
	 * @param roleId the roleId to set
	 */
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	/**
	 * @return the roleId
	 */
	public Long getRoleId() {
		return roleId;
	}

	/**
	 * @param roleList the roleList to set
	 */
	public void setRoleList(List<SelectItem> roleList) {
		this.roleList = roleList;
	}

	/**
	 * @return the roleList
	 */
	public List<SelectItem> getRoleList() {
		return roleList;
	}
}