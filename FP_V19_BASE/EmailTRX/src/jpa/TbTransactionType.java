package jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The persistent class for the TB_TRANSACTION_TYPE database table.
 * 
 */
@Entity
@Table(name="TB_TRANSACTION_TYPE")
public class TbTransactionType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="TRANSACTION_TYPE_ID")
	private long transactionTypeId;

	@Column(name="TRANSACTION_TYPE_NAME")
	private String transactionTypeName;

	/**
	 * Constructor
	 */
    public TbTransactionType() {
    	super();
    }

	/**
	 * @param transactionTypeId the transactionTypeId to set
	 */
	public void setTransactionTypeId(long transactionTypeId) {
		this.transactionTypeId = transactionTypeId;
	}

	/**
	 * @return the transactionTypeId
	 */
	public long getTransactionTypeId() {
		return transactionTypeId;
	}

	/**
	 * @param transactionTypeName the transactionTypeName to set
	 */
	public void setTransactionTypeName(String transactionTypeName) {
		this.transactionTypeName = transactionTypeName;
	}

	/**
	 * @return the transactionTypeName
	 */
	public String getTransactionTypeName() {
		return transactionTypeName;
	}
}