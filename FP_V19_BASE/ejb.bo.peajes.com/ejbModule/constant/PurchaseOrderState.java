/**
 * 
 */
package constant;

/**
 * Enum for Purchase Order States 
 * 
 * @author tmolina
 * @since 08-02-2011
 */
public enum PurchaseOrderState {
	
	NEW(0), 
	DETAILED(1);

	 private Integer id;

	 /**
	  * 
	  * @param id
	  */
	 private PurchaseOrderState(Integer id) {
	   this.id = id;
	 }

	 /**
	  * 
	  * @return id
	  */
	 public Integer getId() {
	  	 return id;
	 }
}