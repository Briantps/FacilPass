package jpa;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="TB_TRANSACTION_DAC")
public class TbTransactionDac implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="TRANSACTION_DAC_ID")
	private Long transactionDacId;
	
	@Column(name="CATEGORY_ID")
	private Long categoryId;
	
	@Column(name="TRANSACTION_DAC_DATE")
	private Timestamp transactionDacDate;
	
	@Column(name="S1")
	private Long sensor1;
	
	@Column(name="S2")
	private Long sensor2;
	
	@Column(name="S3")
	private Long sensor3;
	
	@Column(name="S4")
	private Long sensor4;
	
	@Column(name="S5")
	private Long sensor5;
	
	@Column(name="S6")
	private Long sensor6;
	
	@Column(name="S7")
	private Long sensor7;
	
	@Column(name="S8")
	private Long sensor8;
	
	@Column(name="INDUCTIVE")
	private Long inductive;
	
	@Column(name="OPTICAL")
	private Long optical;
	
	@Column(name="SENT")
	private String sent;
	
	@Column(name="BACKFLOW")
	private String backFlow;
	
	@OneToOne
	@JoinColumn(name="TRANSACTION_ID")
	private TbTransaction transactionId;

	
	public TbTransactionDac(){
		super();
	}

	/**
	 * @param transactionDacId the transactionDacId to set
	 */
	public void setTransactionDacId(Long transactionDacId) {
		this.transactionDacId = transactionDacId;
	}

	/**
	 * @return the transactionDacId
	 */
	public Long getTransactionDacId() {
		return transactionDacId;
	}


	/**
	 * @param sensor1 the sensor1 to set
	 */
	public void setSensor1(Long sensor1) {
		this.sensor1 = sensor1;
	}

	/**
	 * @return the sensor1
	 */
	public Long getSensor1() {
		return sensor1;
	}

	/**
	 * @param sensor2 the sensor2 to set
	 */
	public void setSensor2(Long sensor2) {
		this.sensor2 = sensor2;
	}

	/**
	 * @return the sensor2
	 */
	public Long getSensor2() {
		return sensor2;
	}

	/**
	 * @param sensor3 the sensor3 to set
	 */
	public void setSensor3(Long sensor3) {
		this.sensor3 = sensor3;
	}

	/**
	 * @return the sensor3
	 */
	public Long getSensor3() {
		return sensor3;
	}

	/**
	 * @param sensor4 the sensor4 to set
	 */
	public void setSensor4(Long sensor4) {
		this.sensor4 = sensor4;
	}

	/**
	 * @return the sensor4
	 */
	public Long getSensor4() {
		return sensor4;
	}

	/**
	 * @param sensor5 the sensor5 to set
	 */
	public void setSensor5(Long sensor5) {
		this.sensor5 = sensor5;
	}

	/**
	 * @return the sensor5
	 */
	public Long getSensor5() {
		return sensor5;
	}

	/**
	 * @param sensor6 the sensor6 to set
	 */
	public void setSensor6(Long sensor6) {
		this.sensor6 = sensor6;
	}

	/**
	 * @return the sensor6
	 */
	public Long getSensor6() {
		return sensor6;
	}

	/**
	 * @param sensor7 the sensor7 to set
	 */
	public void setSensor7(Long sensor7) {
		this.sensor7 = sensor7;
	}

	/**
	 * @return the sensor7
	 */
	public Long getSensor7() {
		return sensor7;
	}

	/**
	 * @param sensor8 the sensor8 to set
	 */
	public void setSensor8(Long sensor8) {
		this.sensor8 = sensor8;
	}

	/**
	 * @return the sensor8
	 */
	public Long getSensor8() {
		return sensor8;
	}

	/**
	 * @param inductive the inductive to set
	 */
	public void setInductive(Long inductive) {
		this.inductive = inductive;
	}

	/**
	 * @return the inductive
	 */
	public Long getInductive() {
		return inductive;
	}

	/**
	 * @param optical the optical to set
	 */
	public void setOptical(Long optical) {
		this.optical = optical;
	}

	/**
	 * @return the optical
	 */
	public Long getOptical() {
		return optical;
	}

	/**
	 * @param transactionId the transactionId to set
	 */
	public void setTransactionId(TbTransaction transactionId) {
		this.transactionId = transactionId;
	}

	/**
	 * @return the transactionId
	 */
	public TbTransaction getTransactionId() {
		return transactionId;
	}

	/**
	 * @param categoryId the categoryId to set
	 */
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	/**
	 * @return the categoryId
	 */
	public Long getCategoryId() {
		return categoryId;
	}

	/**
	 * @param sent the sent to set
	 */
	public void setSent(String sent) {
		this.sent = sent;
	}

	/**
	 * @return the sent
	 */
	public String getSent() {
		return sent;
	}

	/**
	 * @param backFlow the backFlow to set
	 */
	public void setBackFlow(String backFlow) {
		this.backFlow = backFlow;
	}

	/**
	 * @return the backFlow
	 */
	public String getBackFlow() {
		return backFlow;
	}

	/**
	 * @param transactionDacDate the transactionDacDate to set
	 */
	public void setTransactionDacDate(Timestamp transactionDacDate) {
		this.transactionDacDate = transactionDacDate;
	}

	/**
	 * @return the transactionDacDate
	 */
	public Timestamp getTransactionDacDate() {
		return transactionDacDate;
	}

//	/**
//	 * @param transactionDacDate the transactionDacDate to set
//	 */
//	public void setTransactionDacDate(String transactionDacDate) {
//		this.transactionDacDate = transactionDacDate;
//	}
//
//	/**
//	 * @return the transactionDacDate
//	 */
//	public String getTransactionDacDate() {
//		int i=transactionDacDate.indexOf(".");
//		transactionDacDate=transactionDacDate.substring(0, i) + "  "  + transactionDacDate.substring(i+1).trim();
//		return transactionDacDate.replace(". 0", "").replace(".", ":");
//	}




}
