/**
 * 
 */
package reportBean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import jpa.ReAccountDevice;
import jpa.TbAccount;
import jpa.TbDevice;
import jpa.TbVehicle;
import net.sf.jasperreports.engine.JRDataSource;
import report.AbstractBaseReportBean;
import security.UserLogged;
import util.ConnectionData;

import ejb.Device;
import ejb.User;
import ejb.Vehicle;
import ejb.report.Report;

/**
 * @author tmolina
 *
 */
public class TraceabilityPlateReportBean extends AbstractBaseReportBean  implements Serializable {
	private static final long serialVersionUID = -3099986808739679757L;

	@EJB(mappedName = "ejb/Report")
	private Report report;
	
	@EJB(mappedName="ejb/Vehicle")
	private Vehicle vehicle;
	
	@EJB(mappedName="ejb/User")
	private User userEJB;

	@EJB(mappedName = "ejb/Device")
	private Device device;
	
	// Attributes --- //
	
	private String plate;
	
	private List<SelectItem> plateList;

	private Date initDate;
	
	private Date endDate;
	
	private Object[][] objectResult;
	
	// Modal --- //
			
	private boolean showData;
		
	private boolean showMessageError;
	
	private String messageError;
	
	private UserLogged usrs;

	/**
	 * Constructor.
	 */
	public TraceabilityPlateReportBean() {
		usrs = (UserLogged) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
		initDate= new Date();
		endDate=new Date();
		this.setPlate("");
		init();
	}

	
	// Actions --- //
	
	/**
	 * 
	 * @return
	 */
	public String init() {
		messageError="";
		setShowData(false);
		setShowMessageError(false);
		objectResult = null;
		return "init";
	}
	
	/**
	 * 
	 * @return
	 */
	public String search() {
		Long userId=userEJB.getSystemMasterById(usrs.getUserId());
			if (!plate.equals("0")) {
				this.setPlate(this.getPlate().toUpperCase());
				if(initDate != null) {
					if(endDate != null) {
						if ((endDate.after(initDate)) || (initDate.compareTo(endDate) == 0)) {	
							objectResult = report.getTraceabilityByPlateClient(initDate, endDate, plate, userId);						
							if (objectResult != null) {
								setShowData(true);
								setShowMessageError(false);
							} else {
								setMessageError("No se encontraron resultados.");
								setShowData(false);
								setShowMessageError(true);
							}
						} else {
							setMessageError("La fecha final debe ser mayor que la fecha inicial. Verifique el Rango.");
							setShowData(false);
							setShowMessageError(true);
						}
					} else {
						setMessageError("Seleccione la fecha Final.");
						setShowData(false);
						setShowMessageError(true);
					}
				} else {
					setMessageError("Seleccione la fecha Inicial.");
					setShowData(false);
					setShowMessageError(true);
				}
			}
			else {
				setMessageError("Usted no posee vehículos asociados.");
				setShowData(false);
				setShowMessageError(true);
			}	
		return null;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public String printPdf() {
		try {
			 super.prepareReport();
		} catch (Exception e) {
			System.out.println("Error en TraceabilityPlateReportBean");
			e.printStackTrace();
		}
		return null;
	}
	
	// Getters and Setters --- //

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
	
	// * ---------- Override Methods ------------ * //
	
	/*
	 * 
	 */
	@Override
	protected String getCompileFileName() {
		return "traceabilityReportByPlate";
	}

	/*
	 * (non-Javadoc)
	 * @see report.AbstractBaseReportBean#getFileName()
	 */
	@Override
	protected String getFileName() {
		return "Reporte_trazabilidad_placa_" + System.currentTimeMillis();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see report.AbstractBaseReportBean#getReportParameters()
	 */
	@Override
	protected Map<String, Object> getReportParameters() {
		this.setPlate(this.getPlate().toUpperCase());
		ReAccountDevice account= vehicle.getAccountByPlate(plate);		
		Map<String, Object> reportParameters = new HashMap<String, Object>();
		Long userId=userEJB.getSystemMasterById(usrs.getUserId());
		reportParameters.put("PLATE", plate);
		Timestamp fecIni = new Timestamp(initDate.getTime());
		Timestamp fecFin = new Timestamp(endDate.getTime() + 86399000);
		reportParameters.put("BEG_DATE", fecIni);
		reportParameters.put("END_DATE", fecFin);
		reportParameters.put("USER_ID", userId);
		
		if(account!=null){
			Long dt;
			BigDecimal balance = new BigDecimal(0);
			TbAccount a=account.getTbAccount();
			if(a!=null){
				dt=a.getDistributionTypeId().getDistributionTypeId();
				if(dt==1){
					balance= a.getAccountBalance();
				}
				else{
					balance=a.getAccountBalance();
					List<TbDevice> lis = new ArrayList<TbDevice>();
					lis=device.getDevicesByAccount(a.getAccountId());
					if(lis.size()>0){
						for(TbDevice t: lis){
							System.out.println("t: " +t);
							balance= balance.add(t.getDeviceCurrentBalance());
						}	
					}
				}

				reportParameters.put("account", a.getAccountId());
				System.out.println("account" + a.getAccountId());
			}
			
			reportParameters.put("balance", balance);
			System.out.println("balance" + balance);
			
		}
		
		if(super.getExportOption().equals(ExportOption.EXCEL)){
			reportParameters.put("IS_IGNORE_PAGINATION", true);
		}
		return reportParameters;
	}

	/*
	 * (non-Javadoc)
	 * @see report.AbstractBaseReportBean#getJRDataSource()
	 */
	@Override
	protected JRDataSource getJRDataSource() {
		return null;
	}
	
	/**
	 * @return the plate
	 */
	public String getPlate() {
		return plate;
	}

	/**
	 * @param plate the plate to set
	 */
	public void setPlate(String plate) {
		this.plate = plate;
	}

	/**
	 * @param plateList the plateList to set
	 */
	public void setPlateList(List<SelectItem> plateList) {
		this.plateList = plateList;
	}

	/**
	 * @return the plateList
	 */
	public List<SelectItem> getPlateList() {
		Long userId=userEJB.getSystemMasterById(usrs.getUserId());
		if (plateList == null) {
			plateList = new ArrayList<SelectItem>();
		} else {
			plateList.clear();
		}
		plateList.add(new SelectItem("-1","TODAS MIS PLACAS"));
		for (TbVehicle v : vehicle.getVehicleByClient(userId)) {
			plateList.add(new SelectItem(v.getVehiclePlate(), v
					.getVehiclePlate()));
		}
		if(plateList.size()<=1){
			plateList.add(new SelectItem("0","No posee vehículos asociados"));
		}
		return plateList;
	}

	@Override
	protected ConnectionData getDataConnection() {
		return (new ConnectionData());
	}
	
	public void ocult(){
		this.setShowData(false);
	}

}