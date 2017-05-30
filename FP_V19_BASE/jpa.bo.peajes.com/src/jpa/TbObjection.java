package jpa;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="TB_OBJECTION")
public class TbObjection implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="OBJECTION_SEQ",sequenceName="TB_OBJECTION_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="OBJECTION_SEQ")
	@Column(name="OBJECTION_ID")
	private Long objectionId;
	
	@Column(name="OBJECTION")
	private String objection;
	
	@Column(name="OBJECTION_DATE")
	private Timestamp date;
	
	@Column(name="TRANSACTION_DATE")
	private Timestamp dateTransaction;
	
	@Column(name="USRS")
	private String usrs;
	
	@ManyToOne
	@JoinColumn(name="ACCOUNT_ID")
	private TbAccount accountId;
	
	@ManyToOne
	@JoinColumn(name="CHARGE_ID")
	private TbCharges charge;
	
	@Column(name="COMPANY_ID")
	private Long companyId;
	
	@Column(name="STATION_ID")
	private Long stationId;
	
	@Column(name="LANE_ID")
	private Long laneId;
	
	@Column(name="OBJECTION_STATE")
	private Long state;
	
	@Column(name = "ACCOUNT_BANK_ID")
	private Long accountBankId;
	
	public TbObjection(){
		super();
	}

	/**
	 * @param objectionId the objectionId to set
	 */
	public void setObjectionId(Long objectionId) {
		this.objectionId = objectionId;
	}

	/**
	 * @return the objectionId
	 */
	public Long getObjectionId() {
		return objectionId;
	}

	/**
	 * @param objection the objection to set
	 */
	public void setObjection(String objection) {
		this.objection = objection;
	}

	/**
	 * @return the objection
	 */
	public String getObjection() {
		return objection;
	}


	/**
	 * @param usrs the usrs to set
	 */
	public void setUsrs(String usrs) {
		this.usrs = usrs;
	}

	/**
	 * @return the usrs
	 */
	public String getUsrs() {
		return usrs;
	}

	/**
	 * @param charge the charge to set
	 */
	public void setCharge(TbCharges charge) {
		this.charge = charge;
	}

	/**
	 * @return the charge
	 */
	public TbCharges getCharge() {
		return charge;
	}

	/**
	 * @param companyId the companyId to set
	 */
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	/**
	 * @return the companyId
	 */
	public Long getCompanyId() {
		return companyId;
	}

	/**
	 * @param stationId the stationId to set
	 */
	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	/**
	 * @return the stationId
	 */
	public Long getStationId() {
		return stationId;
	}

	/**
	 * @param laneId the laneId to set
	 */
	public void setLaneId(Long laneId) {
		this.laneId = laneId;
	}

	/**
	 * @return the laneId
	 */
	public Long getLaneId() {
		return laneId;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Timestamp date) {
		this.date = date;
	}

	/**
	 * @return the date
	 */
	public Timestamp getDate() {
		return date;
	}

	/**
	 * @param dateTransaction the dateTransaction to set
	 */
	public void setDateTransaction(Timestamp dateTransaction) {
		this.dateTransaction = dateTransaction;
	}

	/**
	 * @return the dateTransaction
	 */
	public Timestamp getDateTransaction() {
		return dateTransaction;
	}

	public void setState(Long state) {
		this.state = state;
	}

	public Long getState() {
		return state;
	}

	public void setAccountId(TbAccount accountId) {
		this.accountId = accountId;
	}

	public TbAccount getAccountId() {
		return accountId;
	}

	public void setAccountBankId(Long accountBankId) {
		this.accountBankId = accountBankId;
	}

	public Long getAccountBankId() {
		return accountBankId;
	}




}
