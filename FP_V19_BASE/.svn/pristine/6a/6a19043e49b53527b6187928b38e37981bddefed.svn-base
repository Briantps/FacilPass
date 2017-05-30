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
@Table(name = "TB_MINIMUM_BALANCE_STATE")
public class TbMinimumBalanceState implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "TB_MINIMUM_BALANCE_STATE_SEQ", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "TB_MINIMUM_BALANCE_STATE_SEQ", sequenceName = "TB_MINIMUM_BALANCE_STATE_SEQ",allocationSize=1)
	
	@Column(name="MINIMUM_BALANCE_STATE_ID")
	private Long minimumbalancestateid;

		
	@Column(name="MINIMUM_BALANCE_STATE_DESCRIPT")
	private String minimumbalancestatedescript;
		

	/**
	 * Constructor
	 */
    public TbMinimumBalanceState() {
    	super();
    }
	public Long getMinimumbalancestateid() {
		return minimumbalancestateid;
	}
	public void setMinimumbalancestateid(Long minimumbalancestateid) {
		this.minimumbalancestateid = minimumbalancestateid;
	}
	public String getMinimumbalancestatedescript() {
		return minimumbalancestatedescript;
	}
	public void setMinimumbalancestatedescript(String minimumbalancestatedescript) {
		this.minimumbalancestatedescript = minimumbalancestatedescript;
	}


}