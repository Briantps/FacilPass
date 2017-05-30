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
public class SerializedMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4195970035170013569L;

	private short header;
	private short operationType;
	/* nullable */
	private SerializedTransaction transaction;
	private short errorCode;
	/* nullable */
	private String info;
	private short priority;
	private Timestamp stamp;
	private String trailer;

	/**
	 * 
	 */
	public SerializedMessage(short header, short operationType,
			SerializedTransaction transaction, short errorCode, String info,
			short priority, Timestamp stamp, String trailer) {
		this.errorCode = errorCode;
		this.header = header;
		this.info = info;
		this.operationType = operationType;
		this.priority = priority;
		this.stamp = stamp;
		this.trailer = trailer;
		this.transaction = transaction;
	}

	public short getErrorCode() {
		return errorCode;
	}

	public short getHeader() {
		return header;
	}

	public String getInfo() {
		return info;
	}

	public short getOperationType() {
		return operationType;
	}

	public short getPriority() {
		return priority;
	}

	public Timestamp getStamp() {
		return stamp;
	}

	public String getTrailer() {
		return trailer;
	}

	public SerializedTransaction getTransaction() {
		return transaction;
	}

}