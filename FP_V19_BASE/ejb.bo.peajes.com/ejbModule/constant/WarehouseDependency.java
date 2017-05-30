/**
 * 
 */
package constant;

/**
 * Enum for TB_WAREHOUSE_DEPENDENCY. 
 * @author tmolina
 * @since 10-03-2011
 */
public enum WarehouseDependency {
	
	SUPPLIER(1L),
	WAREHOUSE(2L);

	 private Long id;

	 /**
	  * @param id
	  */
	 private WarehouseDependency(Long id) {
	   this.id = id;
	 }

	 /**
	  * @return id
	  */
	 public Long getId() {
	  	 return id;
	 }
}