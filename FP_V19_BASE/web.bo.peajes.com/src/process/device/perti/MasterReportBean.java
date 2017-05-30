/**
 * 
 */
package process.device.perti;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import jpa.TbDeviceType;

import ejb.Master;
import ejb.report.Report;

import net.sf.jasperreports.engine.JRDataSource;
import report.AbstractBaseReportBean;
import util.ConnectionData;

/**
 * @author tmolina
 *
 */
public class MasterReportBean extends AbstractBaseReportBean {
	
	private final String COMPILE_FILE_NAME = "master";
	
	@EJB(mappedName = "ejb/Report")
	private Report report;
	
	@EJB(mappedName = "ejb/Master")
	private Master master;
	
	private Long deviceTypeId = -1L;
	
	private List<SelectItem> deviceTypes;
	
	private Object[][] obj;

	/**
	 * Constructor.
	 */
	public MasterReportBean() {
	}
	
	// Methods ------------------ //
	
	/**
	 * Executes the report.
	 */
	public String execute() {
		try {
			if (deviceTypeId.longValue() == -1L) {
				this.obj = report.getMasterByType(deviceTypeId, false);
			} else {
				this.obj = report.getMasterByType(deviceTypeId, true);
			}
			super.setExportOption(ExportOption.EXCEL);
			super.prepareReport();
		} catch (Exception e) {
			System.out.println(" [ Error en MasterReportBean ]");
			e.printStackTrace();
		}
		return null;
	}
	
	// Getters and Setters ------------- //

	/*
	 * (non-Javadoc)
	 * @see report.AbstractBaseReportBean#getCompileFileName()
	 */
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
		return "maestra";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see report.AbstractBaseReportBean#getJRDataSource()
	 */
	@Override
	protected JRDataSource getJRDataSource() {
		MasterReportBeanDataSource data = new MasterReportBeanDataSource(this.obj);
		return data;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see report.AbstractBaseReportBean#getReportParameters()
	 */
	@Override
	protected Map<String, Object> getReportParameters() {
		Map<String, Object> reportParameters = new HashMap<String, Object>();
		return reportParameters;
	}

	/**
	 * @param deviceTypeId the deviceTypeId to set
	 */
	public void setDeviceTypeId(Long deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}

	/**
	 * @return the deviceTypeId
	 */
	public Long getDeviceTypeId() {
		return deviceTypeId;
	}

	/**
	 * @param deviceTypes the deviceTypes to set
	 */
	public void setDeviceTypes(List<SelectItem> deviceTypes) {
		this.deviceTypes = deviceTypes;
	}

	/**
	 * @return the deviceTypes
	 */
	public List<SelectItem> getDeviceTypes() {
		if (deviceTypes == null) {
			deviceTypes = new ArrayList<SelectItem>();
			deviceTypes.add(new SelectItem(-1L, "Todos"));
			for (TbDeviceType dt : master.getPertiDeviceTypes()) {
				deviceTypes.add(new SelectItem(dt.getDeviceTypeId(), dt
						.getDeviceTypeName()));
			}
		}
		return deviceTypes;
	}

	@Override
	protected ConnectionData getDataConnection() {
		return null;
	}
}
