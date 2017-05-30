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
@Table(name = "TB_ACCEPTS_CONTRACT")
public class TbAcceptsContract implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "TB_ACCEPTS_CONTRACT_SEQ", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "TB_ACCEPTS_CONTRACT_SEQ", sequenceName = "TB_ACCEPTS_CONTRACT_SEQ",allocationSize=1)
	
	@Column(name="ACCEPTS_CONTRACT_ID")
	private Long acceptsContractId;

	@Column(name="ACCEPTS_DATE")
	private Timestamp acceptsDate;
	
	@Column(name="ACCEPTS_IP")
	private String deviceIp;
		
	@ManyToOne
	@JoinColumn(name="USER_ID")
	private TbSystemUser userId;
	
		
	@Column(name="CHECK_USER")
	private Long checkUser;
	
	
	@Column(name="STAMP_ID")
	private Long stampId;
	
	
	/**
	 * Constructor
	 */
    public TbAcceptsContract() {
    	super();
    }




	public Long getAcceptsContractId() {
		return acceptsContractId;
	}




	public void setAcceptsContractId(Long acceptsContractId) {
		this.acceptsContractId = acceptsContractId;
	}




	public Timestamp getAcceptsDate() {
		return acceptsDate;
	}




	public void setAcceptsDate(Timestamp acceptsDate) {
		this.acceptsDate = acceptsDate;
	}




	public String getDeviceIp() {
		return deviceIp;
	}




	public void setDeviceIp(String deviceIp) {
		this.deviceIp = deviceIp;
	}


	


	public TbSystemUser getUserId() {
		return userId;
	}




	public void setUserId(TbSystemUser userId) {
		this.userId = userId;
	}




	public Long getCheckUser() {
		return checkUser;
	}




	public void setCheckUser(Long checkUser) {
		this.checkUser = checkUser;
	}




	public Long getStampId() {
		return stampId;
	}




	public void setStampId(Long stampId) {
		this.stampId = stampId;
	}

   

}