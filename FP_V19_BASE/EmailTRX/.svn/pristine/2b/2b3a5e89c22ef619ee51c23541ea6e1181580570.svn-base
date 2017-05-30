package jpa;

import java.io.Serializable;
import java.sql.Date;
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
@Table(name="TB_SYSTEM_USER")
public class TbSystemUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TB_SYSTEM_USER_TBSYSTEMUSERID_GENERATOR")
	@SequenceGenerator(name = "TB_SYSTEM_USER_TBSYSTEMUSERID_GENERATOR", sequenceName = "TB_SYSTEM_USER_SEQ")
	@Column(name="USER_ID")
	private Long userId;

	@Column(name="USER_CODE")
	private String userCode;

	@ManyToOne
	@JoinColumn(name="CODE_TYPE_ID")
	private TbCodeType tbCodeType;

	@Column(name="USER_EMAIL")
	private String userEmail;
	
	@Column(name="USER_ACCESS_LOGIN")
	private Timestamp userAccesstLogin;

	@Column(name="USER_LAST_LOGIN")
	private Timestamp userLastLogin;

	@Column(name="USER_NAMES")
	private String userNames;

	@Column(name="USER_PASSWORD")
	private String userPassword;

	@Column(name="USER_PWD_DATE")
	private Timestamp userPwdDate;
	
	@Column(name="USER_REGISTRATION_DATE")
	private Timestamp userRegistrationDate;

	@Column(name="USER_SECOND_NAMES")
	private String userSecondNames;
	
	@ManyToOne
	@JoinColumn(name = "USER_STATE")
	private TbUserStateType userState;
	
	@Column(name = "USER_STATE_CONNECTION")
	private Integer userStateConnection;

    @Column (name = "USER_FIRST_LOGIN")
	private Integer userFirstLogin;
    
    @Column (name = "USER_PWD_EXPIRATION") 
    private Date userPwdExpiration;
    
    @Column (name = "USER_LOCK_DATE")
    private Timestamp userLockDate;
    
    @Column (name = "USER_COUNT_INTENT")
	private Integer userCountIntent;
    
    @Column (name = "USER_LAST_INTENT_DATE")
    private Timestamp userLastIntentDate;
    
    @Column (name = "SYSTEM_USER_MASTER_ID")
	private Long systemUserMasterId;

	    
	/**
	 * Constructor
	 */
    public TbSystemUser() {
    	super();
    }

    public String toString(){
    	return this.userNames;
    }
    
    public boolean equals(Object obj){
    	if(!(obj instanceof TbSystemUser)){
    		return false;
    	} else {
    		TbSystemUser user = (TbSystemUser)obj;
    		return user.userId == this.userId;
    	}    	 
    }
    
    /**
     * 
     * @return userId
     */
	public Long getUserId() {
		return this.userId;
	}

	/**
	 * Setter for userId
	 * @param userId
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * 
	 * @return userCode
	 */
	public String getUserCode() {
		return this.userCode;
	}

	/**
	 * Setter for userCode
	 * @param userCode
	 */
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	/**
	 * 
	 * @return userEmail
	 */
	public String getUserEmail() {
		return this.userEmail;
	}

	/**
	 * Setter for userEmail
	 * @param userEmail
	 */
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	
	/**
	 * @param userAccesstLogin the userAccesstLogin to set
	 */
	public void setUserAccesstLogin(Timestamp userAccesstLogin) {
		this.userAccesstLogin = userAccesstLogin;
	}

	/**
	 * @return the userAccesstLogin
	 */
	public Timestamp getUserAccesstLogin() {
		return userAccesstLogin;
	}

	/**
	 * 
	 * @return userLastLogin
	 */
	public Timestamp getUserLastLogin() {
		return this.userLastLogin;
	}

	/**
	 * Setter for userLastLogin
	 * @param userLastLogin
	 */
	public void setUserLastLogin(Timestamp userLastLogin) {
		this.userLastLogin = userLastLogin;
	}

	/**
	 * 
	 * @return userNames
	 */
	public String getUserNames() {
		return this.userNames;
	}

	/**
	 * Setter for userNames
	 * @param userNames
	 */
	public void setUserNames(String userNames) {
		this.userNames = userNames;
	}

	/**
	 * 
	 * @return userPassword
	 */
	public String getUserPassword() {
		return this.userPassword;
	}

	/**
	 * Setter for userPassword
	 * @param userPassword
	 */
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	/**
	 * 
	 * @return userPwdDate
	 */
	public Timestamp getUserPwdDate() {
		return this.userPwdDate;
	}

	/**
	 * Setter for userPwdDate
	 * @param userPwdDate
	 */
	public void setUserPwdDate(Timestamp userPwdDate) {
		this.userPwdDate = userPwdDate;
	}

	/**
	 * 
	 * @return userSecondNames
	 */
	public String getUserSecondNames() {
		return this.userSecondNames;
	}

	/**
	 * Setter for userSecondNames
	 * @param userSecondNames
	 */
	public void setUserSecondNames(String userSecondNames) {
		this.userSecondNames = userSecondNames;
	}

	/**
	 * @param tbCodeType the tbCodeType to set
	 */
	public void setTbCodeType(TbCodeType tbCodeType) {
		this.tbCodeType = tbCodeType;
	}

	/**
	 * @return the tbCodeType
	 */
	public TbCodeType getTbCodeType() {
		return tbCodeType;
	}

	public void setUserFirstLogin(Integer userFirstLogin) {
		this.userFirstLogin = userFirstLogin;
	}

	public Integer getUserFirstLogin() {
		return userFirstLogin;
	}

	/**
	 * @param userPwdExpiration the userPwdExpiration to set
	 */
	public void setUserPwdExpiration(Date userPwdExpiration) {
		this.userPwdExpiration = userPwdExpiration;
	}

	/**
	 * @return the userPwdExpiration
	 */
	public Date getUserPwdExpiration() {
		return userPwdExpiration;
	}

	/**
	 * @param userLockDate the userLockDate to set
	 */
	public void setUserLockDate(Timestamp userLockDate) {
		this.userLockDate = userLockDate;
	}

	/**
	 * @return the userLockDate
	 */
	public Timestamp getUserLockDate() {
		return userLockDate;
	}

	/**
	 * @param userCountIntent the userCountIntent to set
	 */
	public void setUserCountIntent(Integer userCountIntent) {
		this.userCountIntent = userCountIntent;
	}

	/**
	 * @return the userCountIntent
	 */
	public Integer getUserCountIntent() {
		return userCountIntent;
	}

	/**
	 * @param userLastIntentDate the userLastIntentDate to set
	 */
	public void setUserLastIntentDate(Timestamp userLastIntentDate) {
		this.userLastIntentDate = userLastIntentDate;
	}

	/**
	 * @return the userLastIntentDate
	 */
	public Timestamp getUserLastIntentDate() {
		return userLastIntentDate;
	}

	/**
	 * @param userStateConnection the userStateConnection to set
	 */
	public void setUserStateConnection(Integer userStateConnection) {
		this.userStateConnection = userStateConnection;
	}

	/**
	 * @return the userStateConnection
	 */
	public Integer getUserStateConnection() {
		return userStateConnection;
	}

	public void setUserState(TbUserStateType userState) {
		this.userState = userState;
	}

	public TbUserStateType getUserState() {
		return userState;
	}

	/**
	 * @param systemUserMasterId the systemUserMasterId to set
	 */
	public void setSystemUserMasterId(Long systemUserMasterId) {
		this.systemUserMasterId = systemUserMasterId;
	}

	/**
	 * @return the systemUserMasterId
	 */
	public Long getSystemUserMasterId() {
		return systemUserMasterId;
	}

	public Timestamp getUserRegistrationDate() {
		return userRegistrationDate;
	}

	public void setUserRegistrationDate(Timestamp userRegistrationDate) {
		this.userRegistrationDate = userRegistrationDate;
	}
	

}