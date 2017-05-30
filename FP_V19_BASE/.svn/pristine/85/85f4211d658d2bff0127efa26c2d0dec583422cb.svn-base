package jpa;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.math.BigDecimal;

/**
 * The persistent class for the TB_ACCOUNT database table.
 * 
 */
@Entity
@Table(name="TB_ACCOUNT")
public class TbAccount implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TB_ACCOUNT_TBACCOUNTID_GENERATOR")
	@SequenceGenerator(name = "TB_ACCOUNT_TBACCOUNTID_GENERATOR", sequenceName = "TB_ACCOUNT_SEQ",allocationSize=1)
	@Column(name="ACCOUNT_ID")
	private Long accountId;

	@Column(name="ACCOUNT_BALANCE")
	private BigDecimal accountBalance;

	@Column(name="ACCOUNT_OPENING_DATE")
	private Timestamp accountOpeningDate;

	@Column(name="ACCOUNT_STATE_TYPE_ID")
	private Long accountState;
	
	@Column(name="PREFERENCE")
	private String preference;

	//bi-directional many-to-one association to TbSystemUser
    @ManyToOne
	@JoinColumn(name="USER_ID")
	private TbSystemUser tbSystemUser;
    
    @ManyToOne
    @JoinColumn(name="ACCOUNT_TYPE_ID")
    private TbAccountType tbAccountType;
    
    @OneToOne
    @JoinColumn(name="DISTRIBUTION_TYPE_ID")
    private TbDistributionType distributionTypeId;
    
    @Column(name="ACCOUNT_CLOSE_DATE")
	private Timestamp accountCloseDate;
    
    @Column(name="WITHOUT_BALANCE")
    private Long withoutBalance;
    
    @Column(name="NURE")
	private String nure;
	/**
	 * Constructor
	 */
    public TbAccount() {
    	super();
    }

    /**
     * 
     * @return accountId
     */
	public Long getAccountId() {
		return this.accountId;
	}

	/**
	 * Setter for accountId
	 * @param accountId
	 */
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	/**
	 * 
	 * @return accountBalance
	 */
	public BigDecimal getAccountBalance() {
		return this.accountBalance;
	}

	/**
	 * Setter for accountBalance
	 * @param accountBalance
	 */
	public void setAccountBalance(BigDecimal accountBalance) {
		this.accountBalance = accountBalance;
	}

	/**
	 * 
	 * @return accountOpeningDate
	 */ 
	public Timestamp getAccountOpeningDate() {
		return this.accountOpeningDate;
	}

	/**
	 * Setter for accountOpeningDate
	 * @param accountOpeningDate
	 */
	public void setAccountOpeningDate(Timestamp accountOpeningDate) {
		this.accountOpeningDate = accountOpeningDate;
	}

	/**
	 * 
	 * @return accountState
	 */
	public Long getAccountState() {
		return this.accountState;
	}

	/**
	 * Setter for accountState
	 * @param accountState
	 */
	public void setAccountState(Long accountState) {
		this.accountState = accountState;
	}

	/**
	 * 
	 * @return tbSystemUser
	 */
	public TbSystemUser getTbSystemUser() {
		return this.tbSystemUser;
	}

	/**
	 * Setter for tbSystemUser
	 * @param tbSystemUser
	 */
	public void setTbSystemUser(TbSystemUser tbSystemUser) {
		this.tbSystemUser = tbSystemUser;
	}

	public void setDistributionTypeId(TbDistributionType distributionTypeId) {
		this.distributionTypeId = distributionTypeId;
	}

	public TbDistributionType getDistributionTypeId() {
		return distributionTypeId;
	}

	public void setTbAccountType(TbAccountType tbAccountType) {
		this.tbAccountType = tbAccountType;
	}

	public TbAccountType getTbAccountType() {
		return tbAccountType;
	}

	/**
	 * @param preference the preference to set
	 */
	public void setPreference(String preference) {
		this.preference = preference;
	}

	/**
	 * @return the preference
	 */
	public String getPreference() {
		return preference;
	}

	public void setAccountCloseDate(Timestamp accountCloseDate) {
		this.accountCloseDate = accountCloseDate;
	}

	public Timestamp getAccountCloseDate() {
		return accountCloseDate;
	}

	public Long getWithoutBalance() {
		return withoutBalance;
	}

	public void setWithoutBalance(Long withoutBalance) {
		this.withoutBalance = withoutBalance;
	}

	public void setNure(String nure) {
		this.nure = nure;
	}

	public String getNure() {
		return nure;
	}

}