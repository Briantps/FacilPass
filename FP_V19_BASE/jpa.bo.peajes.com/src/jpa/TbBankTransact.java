/**
 * 
 */
package jpa;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Entidad representa la tabla TB_BANK_TRANSACT
 * @author TPS r.bautista
 *
 */
@Entity
@Table(name="TB_BANK_TRANSACT")
public class TbBankTransact implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "TB_BANK_TRANSACT_SEQ", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "TB_BANK_TRANSACT_SEQ", sequenceName = "TB_BANK_TRANSACT_SEQ",allocationSize=1)
	
	@Column(name="TB_BANK_TRANSACT_ID")
	private Long bankTransactId;
	
    @OneToOne
    @JoinColumn(name="AUTOMATIC_RECHARGE_ID")
    private TbAutomaticRecharge automaticRecharge;
    
    @Column(name="GENERATION_DATE")
	private Timestamp generationDate;
    
    @Column(name="USER_ID")
	private Long userId;
    
    @Column(name="STATE")
	private Integer state;
    
    @Column(name="EXECUTION_DATE")
	private Timestamp executionDate;
    
	@Column(name="BALANCE_VALUE")
	private Long balanceValue;

	@Column(name="AGREEMENT_ID")
	private Long agreementId;
	
	@Column(name="CHANNEL_ID")
	private Long channelId;
	
	@Column(name="NURE")
	private String nure;
	
	@Column(name="VALUE")
	private Long value;

    @Column(name="URL")
	private String urlGenerada;
    
    @Column(name="STATE_CODE")
	private Long stateCode;
    
    @Column(name="STATE_DESC")
	private String stateDescripcion;
    
    @Column(name="TRANSACTION_ID")
	private Long transactId;
    
    @Column(name="BANK_URL")
	private String bankUrl;

    @Column(name="BANK_ID")
	private Long bankId;
    
    @Column(name="PERSON_TYPE")
	private Long personType;

	/**
	 * 
	 */
	public TbBankTransact() {
		// TODO Auto-generated constructor stub
	}

	public Long getBankTransactId() {
		return bankTransactId;
	}

	public void setBankTransactId(Long bankTransactId) {
		this.bankTransactId = bankTransactId;
	}

	public TbAutomaticRecharge getAutomaticRecharge() {
		return automaticRecharge;
	}

	public void setAutomaticRecharge(TbAutomaticRecharge automaticRecharge) {
		this.automaticRecharge = automaticRecharge;
	}

	public Timestamp getGenerationDate() {
		return generationDate;
	}

	public void setGenerationDate(Timestamp generationDate) {
		this.generationDate = generationDate;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Timestamp getExecutionDate() {
		return executionDate;
	}

	public void setExecutionDate(Timestamp executionDate) {
		this.executionDate = executionDate;
	}

	public String getUrlGenerada() {
		return urlGenerada;
	}

	public void setUrlGenerada(String urlGenerada) {
		this.urlGenerada = urlGenerada;
	}

	public Long getBalanceValue() {
		return balanceValue;
	}

	public void setBalanceValue(Long balanceValue) {
		this.balanceValue = balanceValue;
	}

	public Long getAgreementId() {
		return agreementId;
	}

	public void setAgreementId(Long agreementId) {
		this.agreementId = agreementId;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public Long getValue() {
		return value;
	}

	public void setValue(Long value) {
		this.value = value;
	}

	public Long getStateCode() {
		return stateCode;
	}

	public void setStateCode(Long stateCode) {
		this.stateCode = stateCode;
	}

	public String getStateDescripcion() {
		return stateDescripcion;
	}

	public void setStateDescripcion(String stateDescripcion) {
		this.stateDescripcion = stateDescripcion;
	}

	public Long getTransactId() {
		return transactId;
	}

	public void setTransactId(Long transactId) {
		this.transactId = transactId;
	}

	public String getBankUrl() {
		return bankUrl;
	}

	public void setBankUrl(String bankUrl) {
		this.bankUrl = bankUrl;
	}

	public String getNure() {
		return nure;
	}

	public void setNure(String nure) {
		this.nure = nure;
	}

	public Long getBankId() {
		return bankId;
	}

	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}

	public Long getPersonType() {
		return personType;
	}

	public void setPersonType(Long personType) {
		this.personType = personType;
	}

	/**
	 * Retorna true si la url no ha sido utilizada
	 * @return
	 */
	public boolean isAvailable(){
		if (this.state == null){
			return false;
		}
		
		return this.state.intValue() == 0;
	}

	
}
