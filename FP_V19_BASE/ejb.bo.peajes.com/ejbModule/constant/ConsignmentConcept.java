/**
 * 
 */
package constant;

/**
 * Enum for a Consignment Concept.
 * 
 * @author tmolina
 * @since 03-05-2011
 */
public enum ConsignmentConcept {
	
	PURCHASE(1L), 
	RECHARGE(2L);

	 private Long id;

	 /**
	  * 
	  * @param id
	  */
	 private ConsignmentConcept(Long id) {
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