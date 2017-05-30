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
 * The persistent class for the TB_ROLE database table.
 * 
 */
@Entity
@Table(name = "TB_MINIMUM_BALANCE")
public class TbMinimumBalance implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "TB_MINIMUM_BALANCE_SEQ", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "TB_MINIMUM_BALANCE_SEQ", sequenceName = "TB_MINIMUM_BALANCE_SEQ",allocationSize=1)
	
	
	@Column(name="MINIMUM_BALANCE_ID")
	private Long minimumbalanceid;

	@ManyToOne
	@JoinColumn(name="CATEGORY_ID")
	private TbCategory categoryid;
	
	@Column(name="CATEGORY_VALUE")
	private long categpryvalue;
    
	@Column(name="MINIMUM_BALANCE_VALUE")
	private long minimumbalancevalue;
	
	@Column(name="MINIMUM_BALANCE_DATE")
	private Timestamp minimumbalancedate;
	
	@ManyToOne
	@JoinColumn(name="USER_ID")
	private TbSystemUser userId;
	
	@Column(name="MINIMUM_BALANCE_OBSERV")
	private String minimumbalanceobserv;
	
	@ManyToOne
	@JoinColumn(name="MINIMUM_BALANCE_STATE_ID")
	private TbMinimumBalanceState minimumbalancestateid;
		
	
	/**
	 * Constructor
	 */
    public TbMinimumBalance() {
    	super();
    }


	public Long getMinimumbalanceid() {
		return minimumbalanceid;
	}


	public void setMinimumbalanceid(Long minimumbalanceid) {
		this.minimumbalanceid = minimumbalanceid;
	}


	public TbCategory getCategoryid() {
		return categoryid;
	}


	public void setCategoryid(TbCategory categoryid) {
		this.categoryid = categoryid;
	}


	public long getCategpryvalue() {
		return categpryvalue;
	}


	public void setCategpryvalue(long categpryvalue) {
		this.categpryvalue = categpryvalue;
	}


	public long getMinimumbalancevalue() {
		return minimumbalancevalue;
	}


	public void setMinimumbalancevalue(long minimumbalancevalue) {
		this.minimumbalancevalue = minimumbalancevalue;
	}


	public Timestamp getMinimumbalancedate() {
		return minimumbalancedate;
	}


	public void setMinimumbalancedate(Timestamp minimumbalancedate) {
		this.minimumbalancedate = minimumbalancedate;
	}


	public TbSystemUser getUserId() {
		return userId;
	}


	public void setUserId(TbSystemUser userId) {
		this.userId = userId;
	}


	public String getMinimumbalanceobserv() {
		return minimumbalanceobserv;
	}


	public void setMinimumbalanceobserv(String minimumbalanceobserv) {
		this.minimumbalanceobserv = minimumbalanceobserv;
	}


	public TbMinimumBalanceState getMinimumbalancestateid() {
		return minimumbalancestateid;
	}


	public void setMinimumbalancestateid(TbMinimumBalanceState minimumbalancestateid) {
		this.minimumbalancestateid = minimumbalancestateid;
	}



   

}