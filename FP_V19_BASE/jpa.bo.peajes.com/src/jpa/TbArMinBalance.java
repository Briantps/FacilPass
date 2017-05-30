package jpa;

import java.io.Serializable;

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
 * JPA map to the minimum balance table
 * @author Cristhian Buchely
 */
@Entity
@Table(name="TB_AR_MIN_BALANCE")
public class TbArMinBalance implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="TB_AR_MIN_BALANCE_ARMINBALANCEID_GENERATOR")
	@SequenceGenerator(name="TB_AR_MIN_BALANCE_ARMINBALANCEID_GENERATOR", sequenceName="TB_AR_MIN_BALANCE_SEQ",allocationSize=1)
	@Column(name="AR_MIN_BALANCE_ID")
	private Long arMinBalanceId;
	
	@ManyToOne
	@JoinColumn(name="AUTOMATIC_RECHARGE_ID")
	private TbAutomaticRecharge tbAutomaticRecharge;
	
	@Column(name="AR_MIN_BALANCE_STATE")
	private Long arMinBalanceState;
	
	@Column(name="MIN_BALANCE")
	private Double minBalance;
	
	@Column(name = "TRANSACTION_TRIGGER")
	private Integer transactionTrigger;
	
	//Setters / Getters
	
	public Long getArMinBalanceId(){ return arMinBalanceId;}
	public void setArMinBalanceId(Long var){arMinBalanceId=var;}
	
	public TbAutomaticRecharge getTbAutomaticRecharge(){ return tbAutomaticRecharge;}
	public void setTbAutomaticRecharge(TbAutomaticRecharge var){tbAutomaticRecharge=var;}
	
	public Long getArMinBalanceState(){ return arMinBalanceState;}
	public void setArMinBalanceState(Long var){arMinBalanceState=var;}
	
	public Double getMinBalance(){ return minBalance;}
	public void setMinBalance(Double var){minBalance=var;}
	
	public void setTransactionTrigger(Integer value){transactionTrigger=value;}
	public Integer getTransactionTrigger(){return transactionTrigger;}
	
}
