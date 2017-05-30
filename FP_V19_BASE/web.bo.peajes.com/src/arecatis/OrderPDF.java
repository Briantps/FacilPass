/**
 * 
 */
package arecatis;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jpa.TbPurchaseOrder;
import jpa.TbPurchaseOrderDetail;

import net.sf.jasperreports.engine.JRDataSource;
import report.AbstractBaseReportBean;
import util.ConnectionData;
import util.PurchaseOrder;

/**
 * @author tmolina
 *
 */
public class OrderPDF extends AbstractBaseReportBean {
	
	private final String COMPILE_FILE_NAME = "purchaseOrder";
	
	private Object[][] dataSourceO;
	
	private String tittle;
	
	private Date dateO;
	
	private Long orderNum;
	
	private String typeCode;
	
	private String code;
	
	private String name;
	
	private String office;
	
	private String address;
	
	private String city;
	
	private String phone;
	
	private String checkNum;
	
	private String bank;
	
	private Date consigDate;
	
	private String consigOffice;
	
	private String consigNum;
	
	private String contact;
	
	private Long totConsig;
	
	private String pointOrder;
	
	private Long confNum;
	
	private Long rows;
	
	private Long totTrans;
	
	private String tpefe;
	
	private String tpcg;
	
	private String tpce;
	
	private String tpt;
	
	private String tpch;
	
	private String tptr;
	
	private Long total;
	
	private String invoiceNumber;

	/**
	 * Constructor.
	 */
	public OrderPDF(PurchaseOrder po) {
		
		List<TbPurchaseOrderDetail> list = po.getDetails();
		
		dataSourceO = new Object[list.size()][4];
		for(int i = 0; i < list.size(); i++){
			
			TbPurchaseOrderDetail pd = list.get(i);
			
			if( pd.getTbStationBO() != null){
				dataSourceO[i][0] = pd.getTbStationBO().getStationBOName();
			} else {
				dataSourceO[i][0] = null;
			}			
			dataSourceO[i][1] = pd.getDetailOperationValue();

			if(pd.getTbDevice() != null) {
				dataSourceO[i][2] = pd.getTbDevice().getDeviceFacialId();
			} else {
				dataSourceO[i][2] = null;
			}
			
			dataSourceO[i][3] = pd.getTbOperationType().getOperationTypeName().toUpperCase();
		}
		
		TbPurchaseOrder order = po.getOrder();
		
		this.tittle = "ORDEN DE PEDIDO"; //TODO parameter 
		this.dateO = new Date(order.getPurchaseOrderDate().getTime());
		this.orderNum = order.getPuchaseOrderNumber();
		this.typeCode = order.getTbUserData().getTbSystemUser().getTbCodeType().getCodeTypeAbbreviation();
		this.code = order.getTbUserData().getTbSystemUser().getUserCode();
		
		String name = order.getTbUserData().getTbSystemUser().getUserNames();
		
		if (order.getTbUserData().getTbSystemUser().getTbCodeType()
				.getCodeTypeId().longValue() != 3) {
			name += " " + order.getTbUserData().getTbSystemUser()
					.getUserSecondNames();
		}
		
		this.name = name;
		this.office = order.getTbUserData().getUserDataOfficeName();
		this.address = order.getTbUserData().getUserDataAddress();
		this.city = order.getTbUserData().getTbMunicipality().getMunicipalityName();
		this.phone = order.getTbUserData().getUserDataPhone();
		this.contact = order.getTbUserData().getUserDataContact();
		this.invoiceNumber = order.getInvoiceNumber();
		
		this.rows = new Long(list.size());
		this.pointOrder = order.getTbStationBO().getStationBOName();
		
		this.confNum = null;	
		
		this.tpefe = null;
		this.tpcg = null;
		this.tpce = null;
		this.tpt = null;
		this.tpch = null;
		this.tptr = null;
		this.totTrans = 0L;
		this.totConsig = order.getPurchaseOrderValue().longValue();
		this.total = totConsig + totTrans;		
		
		this.checkNum = null;
		this.bank = null;
		this.consigDate = null;
		this.consigOffice = null;	
		
		this.total = this.totConsig + this.totTrans;	
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see report.AbstractBaseReportBean#getCompileFileName()
	 */
	@Override
	protected String getCompileFileName() {
		return COMPILE_FILE_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see report.AbstractBaseReportBean#getFileName()
	 */
	@Override
	protected String getFileName() {
		return "Orden_de_Pedido";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see report.AbstractBaseReportBean#getJRDataSource()
	 */
	@Override
	protected JRDataSource getJRDataSource() {
		OrderDataSource orderDataSource = new OrderDataSource(this.dataSourceO); 
		return orderDataSource;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see report.AbstractBaseReportBean#getReportParameters()
	 */
	@Override
	protected Map<String, Object> getReportParameters() {
		Map<String, Object> reportParameters = new HashMap<String, Object>();
		
		reportParameters.put("TITTLE", this.tittle);
		reportParameters.put("DATE", this.dateO);
		reportParameters.put("ORDERNUM", this.orderNum);
		reportParameters.put("TYPECODE", this.typeCode);
		reportParameters.put("CODE", this.code);
		reportParameters.put("NAME", this.name);
		reportParameters.put("OFFICE", this.office);
		reportParameters.put("ADDRESS", this.address);
		reportParameters.put("CITY", this.city);
		reportParameters.put("PHONE", this.phone);
		reportParameters.put("CHECKNUM", this.checkNum);
		reportParameters.put("BANK", this.bank);
		reportParameters.put("CONSIGDATE", this.consigDate);
		reportParameters.put("CONSIGOFFICE", this.consigOffice);
		reportParameters.put("CONSIGNUM", this.consigNum);
		reportParameters.put("CONTACT", this.contact);
		reportParameters.put("TOTCONSIG", this.totConsig);
		reportParameters.put("POINTORDER", this.pointOrder);
		reportParameters.put("CONFNUM", this.confNum);
		reportParameters.put("ROWS", this.rows);
		reportParameters.put("TOTTRANS", this.totTrans);
		reportParameters.put("TPEFE", this.tpefe);
		reportParameters.put("TPCG", this.tpcg);
		reportParameters.put("TPCE", this.tpce);
		reportParameters.put("TPT", this.tpt);
		reportParameters.put("TPCH", this.tpch);
		reportParameters.put("TPTR", this.tptr);
		reportParameters.put("TOTAL", this.total);
		reportParameters.put("INVOICENUMBER", this.invoiceNumber);
		return reportParameters;
	}
	
	/**
	 * Executes the report.
	 */
	public void execute() {
		try {
			 super.prepareReport();
		} catch (Exception e) {
			System.out.println("Error en OrderPDF");
			e.printStackTrace();
		}
	}

	/**
	 * @param totTrans the totTrans to set
	 */
	public void setTotTrans(Long totTrans) {
		this.totTrans = totTrans;
	}

	/**
	 * @return the totTrans
	 */
	public Long getTotTrans() {
		return totTrans;
	}

	/**
	 * @param tpefe the tpefe to set
	 */
	public void setTpefe(String tpefe) {
		this.tpefe = tpefe;
	}

	/**
	 * @return the tpefe
	 */
	public String getTpefe() {
		return tpefe;
	}

	/**
	 * @param tpcg the tpcg to set
	 */
	public void setTpcg(String tpcg) {
		this.tpcg = tpcg;
	}

	/**
	 * @return the tpcg
	 */
	public String getTpcg() {
		return tpcg;
	}

	/**
	 * @param tpce the tpce to set
	 */
	public void setTpce(String tpce) {
		this.tpce = tpce;
	}

	/**
	 * @return the tpce
	 */
	public String getTpce() {
		return tpce;
	}

	/**
	 * @param tpch the tpch to set
	 */
	public void setTpch(String tpch) {
		this.tpch = tpch;
	}

	/**
	 * @return the tpch
	 */
	public String getTpch() {
		return tpch;
	}

	/**
	 * @param tpt the tpt to set
	 */
	public void setTpt(String tpt) {
		this.tpt = tpt;
	}

	/**
	 * @return the tpt
	 */
	public String getTpt() {
		return tpt;
	}

	/**
	 * @param tptr the tptr to set
	 */
	public void setTptr(String tptr) {
		this.tptr = tptr;
	}

	/**
	 * @return the tptr
	 */
	public String getTptr() {
		return tptr;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(Long total) {
		this.total = total;
	}

	/**
	 * @return the total
	 */
	public Long getTotal() {
		return total;
	}

	@Override
	protected ConnectionData getDataConnection() {
		return null;
	}
}