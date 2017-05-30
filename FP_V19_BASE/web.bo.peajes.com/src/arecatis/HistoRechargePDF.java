/**
 * 
 */
package arecatis;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jpa.TbPurchaseOrderDetail;

import net.sf.jasperreports.engine.JRDataSource;
import report.AbstractBaseReportBean;
import util.ConnectionData;

/**
 * @author tmolina
 *
 */
public class HistoRechargePDF extends AbstractBaseReportBean {
	
	private final String COMPILE_FILE_NAME = "histoRecharge";
	
	private Object[][] dataSource;
	
	private String tittle;

	/**
	 * Constructor.
	 */
	public HistoRechargePDF(List<TbPurchaseOrderDetail> po) {
		List<TbPurchaseOrderDetail> list = po;
		dataSource = new Object[list.size()][8];
		for(int i = 0; i < list.size(); i++){		
			TbPurchaseOrderDetail pd = list.get(i);
			dataSource[i][0]=pd.getTbPurchaseOrder().getPuchaseOrderNumber().toString();
			dataSource[i][1]=pd.getTbPurchaseOrder().getTbUserData().getTbSystemUser().getUserCode();
			dataSource[i][2]=pd.getTbPurchaseOrder().getTbUserData().getUserDataOfficeName();
			dataSource[i][3]=	pd.getDetailOperationValue().longValue();
			dataSource[i][4]=pd.getTbDevice().getDeviceCode();
			dataSource[i][5]=pd.getTbDevice().getDeviceFacialId();
			if(pd.getRechargeDate()!=null){
				dataSource[i][6]=new Date(pd.getRechargeDate().getTime());
			} else{
				dataSource[i][6]=null;
			}
			dataSource[i][7]=pd.getPurchaseOrderDetailId().toString();
		
			if (i == 0) {
				this.tittle = "Consulta Histórica de " + pd.getTbOperationType().getOperationTypeName() +"s";
			}
		}
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
		return "Consulta_Historica";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see report.AbstractBaseReportBean#getJRDataSource()
	 */
	@Override
	protected JRDataSource getJRDataSource() {
		HistoRechargeDataSource orderDataSource = new HistoRechargeDataSource(this.dataSource); 
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
		reportParameters.put("TITLE", this.tittle);
		return reportParameters;
	}
	
	/**
	 * Executes the report.
	 */
	public void execute() {
		try {
			 super.prepareReport();
		} catch (Exception e) {
			System.out.println("Error en HistoRechargePDF");
			e.printStackTrace();
		}
	}

	@Override
	protected ConnectionData getDataConnection() {
		return null;
	}
}