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
@Table(name="TB_TRANSACTION_VALIDATE_STATE")
public class TbTransactionValidateState implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TB_TRANSACTION_VALIDATE_STATE")
	@SequenceGenerator(name = "SEQ_TB_TRANSACTION_VALIDATE_STATE", sequenceName = "TB_TRANSACTION_VALIDATE_SEQ",allocationSize=1)
	@Column(name="VALIDATE_ID")
	private Long validateId;
	
	@ManyToOne
	@JoinColumn(name="TB_TRANSACTION_STATE_ID")
	private TbTransactionState stateId;

	@Column(name="OBSERVATION")
	private String observation;
	
	@Column(name="USER_ID")
	private Long userId;
	
	@Column(name="DATE_VALIDATION")
	private Timestamp dateValidation;
	
	@Column(name="TRANSACTION_ID")
	private Long transactionId;

	/**
	 * @param validateId the validateId to set
	 */
	public void setValidateId(Long validateId) {
		this.validateId = validateId;
	}

	/**
	 * @return the validateId
	 */
	public Long getValidateId() {
		return validateId;
	}

	/**
	 * @param stateId the stateId to set
	 */
	public void setStateId(TbTransactionState stateId) {
		this.stateId = stateId;
	}

	/**
	 * @return the stateId
	 */
	public TbTransactionState getStateId() {
		return stateId;
	}

	/**
	 * @param observation the observation to set
	 */
	public void setObservation(String observation) {
		this.observation = observation;
	}

	/**
	 * @return the observation
	 */
	public String getObservation() {
		return observation;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * @param dateValidation the dateValidation to set
	 */
	public void setDateValidation(Timestamp dateValidation) {
		this.dateValidation = dateValidation;
	}

	/**
	 * @return the dateValidation
	 */
	public Timestamp getDateValidation() {
		return dateValidation;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public Long getTransactionId() {
		return transactionId;
	}
	
	
	
}
