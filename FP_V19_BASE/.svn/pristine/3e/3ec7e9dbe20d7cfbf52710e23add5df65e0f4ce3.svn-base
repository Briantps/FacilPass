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

/**
 * Entity implementation class for Entity: TbDailyConcDetail
 *
 */
@Entity
@Table(name="TB_DAILY_CONC_DETAIL")
public class TbDailyConcDetail implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TB_DAILY_CONC_DETAIL_GENERATOR")
	@SequenceGenerator(name = "TB_DAILY_CONC_DETAIL_GENERATOR", sequenceName = "TB_DAILY_CONC_DETAIL_SEQ",allocationSize=1)
	@Column(name="DAILY_CONC_DETA_ID")
	private Long dailyConcDetaId;
	
	@Column(name="DAILY_CONC_DETA_DATE")
	private Timestamp dailyConcDetaDate;
	
	@Column(name="RECHARGE_VALUE")
	private Long rechargeValue;
	
	@ManyToOne
	@JoinColumn(name = "USER_ID")
	private TbSystemUser userId;
	
	@ManyToOne
	@JoinColumn(name = "ACCOUNT_ID")
	private TbAccount accountId;
	
	@ManyToOne
	@JoinColumn(name = "VEHICLE_ID")
	private TbVehicle vehicleId;
	
	@ManyToOne
	@JoinColumn(name = "DAILY_CONC_ID")
	private TbDailyConciliation dailyConcId;
	
	public TbDailyConcDetail() {
		super();
	}

	public Long getDailyConcDetaId() {
		return dailyConcDetaId;
	}

	public void setDailyConcDetaId(Long dailyConcDetaId) {
		this.dailyConcDetaId = dailyConcDetaId;
	}

	public Timestamp getDailyConcDetaDate() {
		return dailyConcDetaDate;
	}

	public void setDailyConcDetaDate(Timestamp dailyConcDetaDate) {
		this.dailyConcDetaDate = dailyConcDetaDate;
	}

	public Long getRechargeValue() {
		return rechargeValue;
	}

	public void setRechargeValue(Long rechargeValue) {
		this.rechargeValue = rechargeValue;
	}

	public TbSystemUser getUserId() {
		return userId;
	}

	public void setUserId(TbSystemUser userId) {
		this.userId = userId;
	}

	public TbAccount getAccountId() {
		return accountId;
	}

	public void setAccountId(TbAccount accountId) {
		this.accountId = accountId;
	}

	public TbVehicle getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(TbVehicle vehicleId) {
		this.vehicleId = vehicleId;
	}

	public TbDailyConciliation getDailyConcId() {
		return dailyConcId;
	}

	public void setDailyConcId(TbDailyConciliation dailyConcId) {
		this.dailyConcId = dailyConcId;
	}
	
}