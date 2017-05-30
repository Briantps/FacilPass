/**
 * 
 */
package constant;

/**
 * Enum for TB_WAREHOUSE_OPERATION_TYPE
 * @author tmolina
 * @since 10-03-2011
 */
public enum WarehouseOperationType {
	
	ENTRY(1L),
	DEPARTURE(2L);

	 private Long id;

	 /**
	  * 
	  * @param id
	  */
	 private WarehouseOperationType(Long id) {
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