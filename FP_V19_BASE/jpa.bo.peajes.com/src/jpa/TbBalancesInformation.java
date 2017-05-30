package jpa;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Timestamp;

/**
 * The persistent class for the TB_BALANCES_INFORMATION database table.
 * 
 */
@Entity
@Table(name="TB_BALANCES_INFORMATION")
public class TbBalancesInformation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TB_BALANCES_INFORMATION_BALANCESINFORMATIONID_GENERATOR", sequenceName="TB_BALANCES_INFORMATION_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TB_BALANCES_INFORMATION_BALANCESINFORMATIONID_GENERATOR")
	@Column(name="BALANCES_INFORMATION_ID")
	private long balancesInformationId;

	@Column(name="BALANCES_INFORMATION_DATE")
	private Timestamp balancesInformationDate;

	@Column(name="BALANCES_INFORMATION_STATE_ID")
	private Long balancesInformationStateId;

    
	@Column(name="BALANCES_INFORMATION_TXT")
	private String balancesInformationTxt;

	@ManyToOne
	@JoinColumn(name="USER_ID")
	private TbSystemUser userId;

    public TbBalancesInformation() {
    }

	public long getBalancesInformationId() {
		return this.balancesInformationId;
	}

	public void setBalancesInformationId(long balancesInformationId) {
		this.balancesInformationId = balancesInformationId;
	}

	public Timestamp getBalancesInformationDate() {
		return this.balancesInformationDate;
	}

	public void setBalancesInformationDate(Timestamp balancesInformationDate) {
		this.balancesInformationDate = balancesInformationDate;
	}

	public Long getBalancesInformationStateId() {
		return this.balancesInformationStateId;
	}

	public void setBalancesInformationStateId(Long balancesInformationStateId) {
		this.balancesInformationStateId = balancesInformationStateId;
	}

	public String getBalancesInformationTxt() {
		return this.balancesInformationTxt;
	}

	public void setBalancesInformationTxt(String balancesInformationTxt) {
		this.balancesInformationTxt = balancesInformationTxt;
	}

	public TbSystemUser getUserId() {
		return this.userId;
	}

	public void setUserId(TbSystemUser userId) {
		this.userId = userId;
	}

}