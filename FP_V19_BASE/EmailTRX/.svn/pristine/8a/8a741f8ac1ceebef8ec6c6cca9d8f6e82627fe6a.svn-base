package jpa;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: TbUmbral
 *
 */
@Entity
@Table(name="TB_UMBRAL")
public class TbUmbral implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TB_UMBRAL_GENERATOR")
	@SequenceGenerator(name = "TB_UMBRAL_GENERATOR", sequenceName = "TB_UMBRAL_SEQ",
			allocationSize=1, initialValue=1)
			@Column(name="UMBRAL_ID")
			private Long umbralId;

	@ManyToOne
	@JoinColumn(name="ACCOUNT_ID")
	private TbAccount tbAccount;

	@Column(name="AVERAGE")
	private BigDecimal average;

	@Column(name="UMBRAL_VALUE")
	private BigDecimal umbralValue;

	@Column(name="UMBRAL_TIME")
	private Timestamp umbralTime;

	@Column(name="UMBRAL_STATE")
	private long umbralState;

	@Column(name="TYPE_MESSAGE_ID")
	private long typeMessageId;

	@Column(name="UMBRAL_CHANNEL")
	private Long umbralChannel;

	@Column(name = "ACCOUNT_BANK_ID")
	private Long accountBankId;

	@Column(name = "DESCRIPTION_PSE")
	private String descriptionPse;
	
	@ManyToOne
	@JoinColumn(name="AGREEMENT_CHANEL_ID")
	private ReAgreementChanel reAgreementChanel;

	public void setUmbralId(Long umbralId) {
		this.umbralId = umbralId;
	}

	public Long getUmbralId() {
		return umbralId;
	}

	public void setTbAccount(TbAccount tbAccount) {
		this.tbAccount = tbAccount;
	}

	public TbAccount getTbAccount() {
		return tbAccount;
	}

	public void setAverage(BigDecimal average) {
		this.average = average;
	}

	public BigDecimal getAverage() {
		return average;
	}

	public void setUmbralValue(BigDecimal umbralValue) {
		this.umbralValue = umbralValue;
	}

	public BigDecimal getUmbralValue() {
		return umbralValue;
	}

	public void setUmbralTime(Timestamp umbralTime) {
		this.umbralTime = umbralTime;
	}

	public Timestamp getUmbralTime() {
		return umbralTime;
	}

	public void setUmbralState(long umbralState) {
		this.umbralState = umbralState;
	}

	public long getUmbralState() {
		return umbralState;
	}

	public void setTypeMessageId(long typeMessageId) {
		this.typeMessageId = typeMessageId;
	}

	public long getTypeMessageId() {
		return typeMessageId;
	}

	public Long getUmbralChannel() {
		return umbralChannel;
	}

	public void setUmbralChannel(Long umbralChannel) {
		this.umbralChannel = umbralChannel;
	}

	public Long getAccountBankId() {
		return accountBankId;
	}

	public void setAccountBankId(Long accountBankId) {
		this.accountBankId = accountBankId;
	}

	public String getDescriptionPse() {
		return descriptionPse;
	}

	public void setDescriptionPse(String descriptionPse) {
		this.descriptionPse = descriptionPse;
	}

	public void setReAgreementChanel(ReAgreementChanel reAgreementChanel) {
		this.reAgreementChanel = reAgreementChanel;
	}

	public ReAgreementChanel getReAgreementChanel() {
		return reAgreementChanel;
	}

 
}