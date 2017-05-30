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
 * Entity implementation class for Entity: TbRechargeOnline
 *
 */
@Entity
@Table(name="TB_RECHARGE_ONLINE")
public class TbRechargeOnline implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TB_RECHARGE_ONLINE_GENERATOR")
	@SequenceGenerator(name = "TB_RECHARGE_ONLINE_GENERATOR", sequenceName = "TB_RECHARGE_ONLINE_SEQ",
	allocationSize=1, initialValue=1)
	@Column(name="RECHARGE_ONLINE_ID")
	private Long rechargeOnlineId;
	
	@Column(name="REQUEST_ID")
	private String requestId;
	
	@Column(name="OPER_TYPE_ID")
	private Long operTypeId;
	
	@Column(name="AMOUNT")
	private Long amount;
	
	@Column(name="NURE")
	private Long nure;
		
	@Column(name="CODE_CHANEL")
	private Long codeChanel;
		
	@Column(name="CODE_AGREEMENT")
	private Long codeAgreement;	
	
	@Column(name="REQUEST_DATE")
	private Timestamp requestDate;
	
	@Column(name="CHANNEL_DATE")
	private Timestamp chanelDate;
	
	@Column(name="APPROVAL_NUMBER_RQ")
	private Long approvalNumberRq;
	
	@Column(name="APPROVAL_NUMBER_RP")
	private Long approvalNumberRp;
	
	@Column(name="CODE_RESPONSE")
	private String codeResponse;
	
	@Column(name="MESSAGE")
	private String message;
		
	@Column(name="UMBRAL_ID")
	private Long umbralId;
	
	@Column(name="BANK_ID")
	private Long bankId;

	public void setRechargeOnlineId(Long rechargeOnlineId) {
		this.rechargeOnlineId = rechargeOnlineId;
	}

	public Long getRechargeOnlineId() {
		return rechargeOnlineId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setOperTypeId(Long operTypeId) {
		this.operTypeId = operTypeId;
	}

	public Long getOperTypeId() {
		return operTypeId;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public Long getAmount() {
		return amount;
	}

	public void setNure(Long nure) {
		this.nure = nure;
	}

	public Long getNure() {
		return nure;
	}

	public void setRequestDate(Timestamp requestDate) {
		this.requestDate = requestDate;
	}

	public Timestamp getRequestDate() {
		return requestDate;
	}

	public void setChanelDate(Timestamp chanelDate) {
		this.chanelDate = chanelDate;
	}

	public Timestamp getChanelDate() {
		return chanelDate;
	}

	public void setApprovalNumberRq(Long approvalNumberRq) {
		this.approvalNumberRq = approvalNumberRq;
	}

	public Long getApprovalNumberRq() {
		return approvalNumberRq;
	}

	public void setApprovalNumberRp(Long approvalNumberRp) {
		this.approvalNumberRp = approvalNumberRp;
	}

	public Long getApprovalNumberRp() {
		return approvalNumberRp;
	}

	public void setCodeResponse(String codeResponse) {
		this.codeResponse = codeResponse;
	}

	public String getCodeResponse() {
		return codeResponse;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setCodeChanel(Long codeChanel) {
		this.codeChanel = codeChanel;
	}

	public Long getCodeChanel() {
		return codeChanel;
	}

	public void setCodeAgreement(Long codeAgreement) {
		this.codeAgreement = codeAgreement;
	}

	public Long getCodeAgreement() {
		return codeAgreement;
	}

	public void setUmbralId(Long umbralId) {
		this.umbralId = umbralId;
	}

	public Long getUmbralId() {
		return umbralId;
	}

	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}

	public Long getBankId() {
		return bankId;
	}


}
