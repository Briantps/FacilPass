/**
 * 
 */
package constant;

/**
 * Enum for TB_DEVICE_STATE
 * @author tmolina
 * @since 11-03-2011
 */
public enum DeviceState {
	
	PRECHARGE(0L),
	INITIALIZED(1L),
	CUSTOMIZED(2L),
	ACTIVE(6L),
	WAREHOUSE(10L),
	SUPPLIER(14L),
	BLACKLIST(9L),
	INACTIVE_BY_DISSOCIATION(16L);
	
	 private Long id;

	 /**
	  * 
	  * @param id
	  */
	 private DeviceState(Long id) {
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