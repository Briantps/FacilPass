/**
 * 
 */
package constant;

/**
 * Enum for TB_WAREHOUSE_STATE
 * @author tmolina
 * @since 14-03-2011
 */
public enum WarehouseState {
	
	PRELIMINARY(1L),
	DETAILED(2L),
	CANCELED(3L);

	 private Long id;

	 /**
	  * 
	  * @param id
	  */
	 private WarehouseState(Long id) {
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