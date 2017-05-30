/**
 * 
 */
package auxiliar;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author root
 * 
 */
public class SerializedTransaction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6923660762249805223L;
	private String deviceCode;
	private Long transactionType;
	private Long previousBalance;
	private Long newBalance;
	private Long transactionValue;
	private Timestamp transactionTime;
	private long laneId;

	/**
	 * Main constructor
	 */
	public SerializedTransaction(String deviceCode, Long transactionType,
			Long previousBalance, Long newBalance,
			Long transactionValue, Timestamp transactionTime,
			long idCarril) {
		this.deviceCode = deviceCode;
		this.laneId = idCarril;
		this.newBalance = newBalance;
		this.previousBalance = previousBalance;
		this.transactionTime = transactionTime;
		this.transactionType = transactionType;
		this.transactionValue = transactionValue;
	}

	public String getDeviceCode() {
		return deviceCode;
	}

	public Long getTransactionType() {
		return transactionType;
	}

	public Long getPreviousBalance() {
		return previousBalance;
	}

	public Long getNewBalance() {
		return newBalance;
	}

	public Long getTransactionValue() {
		return transactionValue;
	}

	public Timestamp getTransactionTime() {
		return transactionTime;
	}

	public long getLaneId() {
		return laneId;
	}

}
