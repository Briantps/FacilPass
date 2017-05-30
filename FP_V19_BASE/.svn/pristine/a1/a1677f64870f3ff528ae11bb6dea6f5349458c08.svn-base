/**
 * 
 */
package process.warehouse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import jpa.TbWarehouseState;

import report.ReportBean;
import report.dataSource.EntryWarehouseByDateDataSource;
import report.dataSource.EntryWarehouseByNumberDataSource;
import util.warehouse.WarehouseTo;

import ejb.Warehouse;
import ejb.report.Report;

/**
 * @author tmolina
 *
 */
public class EntryOrderConsultBean implements Serializable {
	private static final long serialVersionUID = 790999305495596808L;
	
	@EJB(mappedName = "ejb/Report")
	private Report report;
	
	@EJB(mappedName="ejb/Warehouse")
	private Warehouse warehouse;
	
	// Attributes ----- //
	
	private Date initDate;
	
	private Date endDate;
	
	private String message;
	
	private boolean showMessage;
	
	private boolean showData;
	
	private Object[][] obj;
	
	private String entryNumber;
	
	private WarehouseTo w;
	
	private Long warehouseStateId;
	
	private List<SelectItem> stateList;

	/**
	 * Constructor
	 */
	public EntryOrderConsultBean () {
	}
	
	/**
	 * 
	 */
	public String init() {
		setInitDate(null);
		setEndDate(null);
		setEntryNumber(null);
		setShowData(false);
		setShowMessage(false);
		w = null;
		obj = null;
		setMessage(null);
		setStateList(null);
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	private String getStateName() {
		if(warehouseStateId.longValue() == -1) {
			return "Todos";
		}
		for (TbWarehouseState ws : warehouse.getWarehouseState()) {
			if (ws.getWarehouseStateId().longValue() == warehouseStateId
					.longValue()) {
				return ws.getWarehouseStateName();
			}
		}
		return null;
	}
	
	// actions --- //
	
	public String consultEntryByDate() {
		if (initDate.before(endDate)) {
			
			obj = report.getEntryWarehouseByDate(initDate, endDate,
					warehouseStateId);
			if (obj != null) {
				setShowMessage(false);
				setShowData(true);
			} else {
				setMessage("No se encontró información con las fechas seleccionadas.");
				setShowMessage(true);
				setShowData(false);
			}
		} else {
			setMessage("La fecha inicial debe ser menor a la fecha final.");
			setShowMessage(true);
			setShowData(false);
		}	
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public String printPdf() {
		Map<String, Object> reportParameters = new HashMap<String, Object>();
		reportParameters.put("TITLE", "REPORTE DISPOSITIVOS POR FECHA");
		reportParameters.put("TEXT_FIELD_1", initDate);
		reportParameters.put("TEXT_FIELD_2", endDate);
		reportParameters.put("TEXT_FIELD_3", getStateName());

		ReportBean bean = new ReportBean("entryWarehouseByDate",
				reportParameters, "repote_entrada_dispositivos_fecha_"
						+ System.currentTimeMillis(),
				new EntryWarehouseByDateDataSource(obj));
		bean.printReport();
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public String consultEntryByNumber() {
		w = report.getEntryWarehouseByNumber(entryNumber);
		if(w != null) {
			setShowMessage(false);
			setShowData(true);
		} else {
			setMessage("No se encontró información con el número de orden ingresado. Verifique.");
			setShowMessage(true);
			setShowData(false);
		}
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public String printPdfNumber() {
		Map<String, Object> reportParameters = new HashMap<String, Object>();
		reportParameters.put("TITLE", "REPORTE DISPOSITIVOS POR NÚMERO DE ORDEN");
		
		reportParameters.put("TEXT_FIELD_1", entryNumber.toString());
		reportParameters.put("TEXT_FIELD_2", w.getWarehouse().isWarehouseIsCard()? "Tarjetas" : "Tags");
		reportParameters.put("TEXT_FIELD_3", w.getWarehouse().getCreationDate());
		reportParameters.put("TEXT_FIELD_4", w.getWarehouse().getDeviceQuantity().toString());
		reportParameters.put("TEXT_FIELD_5", w.getLeft().toString());
		reportParameters.put("TEXT_FIELD_6", w.getWarehouse().getTbWarehouseState().getWarehouseStateName());

		EntryWarehouseByNumberDataSource ds = null;
		if (w.getObj() != null) {
			ds = new EntryWarehouseByNumberDataSource(w.getObj());
		}
		ReportBean bean = new ReportBean("entryWarehouseByNumber",
				reportParameters, "repote_entrada_dispositivos_numero_orden_"
						+ System.currentTimeMillis(), ds);
		bean.printReport();
		return null;
	}
	
	// Getters and Setters ---- //
	
	/**
	 * @param initDate the initDate to set
	 */
	public void setInitDate(Date initDate) {
		this.initDate = initDate;
	}

	/**
	 * @return the initDate
	 */
	public Date getInitDate() {
		return initDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
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
	 * @param entryNumber the entryNumber to set
	 */
	public void setEntryNumber(String entryNumber) {
		this.entryNumber = entryNumber;
	}

	/**
	 * @return the entryNumber
	 */
	public String getEntryNumber() {
		return entryNumber;
	}

	/**
	 * @param warehouseStateId the warehouseStateId to set
	 */
	public void setWarehouseStateId(Long warehouseStateId) {
		this.warehouseStateId = warehouseStateId;
	}

	/**
	 * @return the warehouseStateId
	 */
	public Long getWarehouseStateId() {
		return warehouseStateId;
	}

	/**
	 * @param stateList the stateList to set
	 */
	public void setStateList(List<SelectItem> stateList) {
		this.stateList = stateList;
	}

	/**
	 * @return the stateList
	 */
	public List<SelectItem> getStateList() {
		if (stateList == null) {
			stateList = new ArrayList<SelectItem>();
			stateList.add(new SelectItem(-1L, "Todos"));
			for (TbWarehouseState ws : warehouse.getWarehouseState()) {
				stateList.add(new SelectItem(ws.getWarehouseStateId(), ws
						.getWarehouseStateName()));
			}
		}
		return stateList;
	}	
}
