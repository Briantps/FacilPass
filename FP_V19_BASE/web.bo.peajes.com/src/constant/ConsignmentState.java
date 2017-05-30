/**
 * 
 */
package constant;

/**
 * Enum for a Consignment State.
 * 
 * @author tmolina
 * @since 08-02-2011
 */
public enum ConsignmentState {
	
	NEW(0L), 
	APPROVED(1L),
	REJECTED(2L),
	USED(3L);

	 private Long id;

	 /**
	  * 
	  * @param id
	  */
	 private ConsignmentState(Long id) {
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