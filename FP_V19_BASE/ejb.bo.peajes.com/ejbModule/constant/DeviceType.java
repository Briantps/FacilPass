/**
 * 
 */
package constant;

/**
 * Enum for TB_DEVICE_TYPE. 
 * @author tmolina
 * @since 23-02-2011
 */
public enum DeviceType {
	
	TAG(0L),
	EXEMPT(4L),
	SPECIAL(5L),
	CARRETEABLE(9L),
	TAGSTEP(10L);;

	 private Long id;

	 /**
	  * @param id
	  */
	 private DeviceType(Long id) {
	   this.id = id;
	 }

	 /**
	  * @return id
	  */
	 public Long getId() {
	  	 return id;
	 }
}