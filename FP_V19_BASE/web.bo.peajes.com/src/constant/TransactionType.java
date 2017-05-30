/**
 * 
 */
package constant;

import jpa.TbTransactionType;

/**
 * Enum for {@link TbTransactionType} 
 * @author tmolina
 * @since 09-05-2011
 */
public enum TransactionType {

	CREDIT(1L),
	DEBIT(2L),
	ASSIGNMENT(3L),
	INTERNAL_TRANSFER(4L);

	 private Long id;

	 /**
	  * 
	  * @param id
	  */
	 private TransactionType(Long id) {
	   this.id = id;
	 }

	 /**
	  * 
	  * @return id
	  */
	 public Long getId() {
	  	 return id;
	 }
}