/**
 * 
 */
package constant;

/**
 * Enum for Purchase Order Detail States.
 * 
 * @author tmolina
 * @since 08-02-2011
 */
public enum PurchaseOrderDetailState {
	
	NEW(0), 
	TO_WAREHOUSE(1),
	READY_TO_RECHARGE(2),
	PROCESS_FINISHED(3);

	 private Integer id;

	 /**
	  * 
	  * @param id
	  */
	 private PurchaseOrderDetailState(Integer id) {
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