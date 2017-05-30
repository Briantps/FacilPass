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
 * Entity implementation class for Entity: TbAutomaticRecharge
 *
 */
@Entity
@Table(name="TB_AUTOMATIC_RECHARGE_DETA")
public class TbAutomaticRechargeDeta implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TB_AUTOMATIC_RECHARGE_DETA_GENERATOR")
	@SequenceGenerator(name = "TB_AUTOMATIC_RECHARGE_DETA_GENERATOR", sequenceName = "TB_AUTOMATIC_RECHARGE_DETA_SEQ",allocationSize=1)
	@Column(name="AUTOMATIC_RECHARGE_DETA_ID")
	private long automaticRechargeDetaId;
	
	@ManyToOne
	@JoinColumn(name="AUTOMATIC_RECHARGE_ID")
	private TbAutomaticRecharge automaticRechargeId;
		
	@Column(name="LAST_EXECUTION")
	private Timestamp lastExecution;

	@Column(name = "ESTADO")
	private Integer estado;
	/**
	 * @param automaticRechargeDetaId the automaticRechargeDetaId to set
	 */
	public void setAutomaticRechargeDetaId(Long automaticRechargeDetaId) {
		this.automaticRechargeDetaId = automaticRechargeDetaId;
	}

	/**
	 * @return the automaticRechargeDetaId
	 */
	public Long getAutomaticRechargeDetaId() {
		return automaticRechargeDetaId;
	}


	/**
	 * @param automaticRechargeId the automaticRechargeId to set
	 */
	public void setAutomaticRechargeId(TbAutomaticRecharge automaticRechargeId) {
		this.automaticRechargeId = automaticRechargeId;
	}

	/**
	 * @return the automaticRechargeId
	 */
	public TbAutomaticRecharge getAutomaticRechargeId() {
		return automaticRechargeId;
	}

	/**
	 * @param lastExecution the lastExecution to set
	 */
	public void setLastExecution(Timestamp lastExecution) {
		this.lastExecution = lastExecution;
	}

	/**
	 * @return the lastExecution
	 */
	public Timestamp getLastExecution() {
		return lastExecution;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(Integer estado) {
		this.estado = estado;
	}

	/**
	 * @return the estado
	 */
	public Integer getEstado() {
		return estado;
	}

}
