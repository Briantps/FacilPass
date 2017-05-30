/**
 * 
 */
package util;

import java.io.Serializable;

import jpa.TbOperationType;

/**
 * @author tmolina
 *
 */
public class Order implements Serializable {
	private static final long serialVersionUID = -2775060077101702467L;
	
	private TbOperationType operType;
	
	private Long numberOfOperations;

	/**
	 * Constructor. 
	 */
	public Order() {
	}

	/**
	 * @return the operType
	 */
	public TbOperationType getOperType() {
		return operType;
	}

	/**
	 * @param operType the operType to set
	 */
	public void setOperType(TbOperationType operType) {
		this.operType = operType;
	}

	/**
	 * @return the numberOfOperations
	 */
	public Long getNumberOfOperations() {
		return numberOfOperations;
	}

	/**
	 * @param numberOfOperations the numberOfOperations to set
	 */
	public void setNumberOfOperations(Long numberOfOperations) {
		this.numberOfOperations = numberOfOperations;
	}
}