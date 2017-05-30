package jpa;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="TB_DETAIL_INVOICE")
public class TbDetailInvoice implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
    @Column(name="DETAIL_ID")
	private Long detailId;
	
	@Column(name="TRANSACTION_DATE")
	private Timestamp transactionDate;
	
	@Column(name="NAME_GRANTING")
	private String nameC;
	
	@Column(name="STATION_ID")
	private Long stationId;
	
	@Column(name="LANE_ID")
	private Long laneId;
	
	@Column(name="OPERATION_VALUE")
	private BigDecimal operationValue;
	
	@Column(name="STATION_NAME")
	private String stationName;
	
	@ManyToOne
	@JoinColumn(name="INVOICE_ID")
	private TbInvoice invoiceId;
	
	public TbDetailInvoice(){
		super();
	}

	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}

	public Long getDetailId() {
		return detailId;
	}

	public void setTransactionDate(Timestamp transactionDate) {
		this.transactionDate = transactionDate;
	}

	public Timestamp getTransactionDate() {
		return transactionDate;
	}

	public void setNameC(String nameC) {
		this.nameC = nameC;
	}

	public String getNameC() {
		return nameC;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setLaneId(Long laneId) {
		this.laneId = laneId;
	}

	public Long getLaneId() {
		return laneId;
	}

	public void setOperationValue(BigDecimal operationValue) {
		this.operationValue = operationValue;
	}

	public BigDecimal getOperationValue() {
		return operationValue;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getStationName() {
		return stationName;
	}

	public void setInvoiceId(TbInvoice invoiceId) {
		this.invoiceId = invoiceId;
	}

	public TbInvoice getInvoiceId() {
		return invoiceId;
	}

}
