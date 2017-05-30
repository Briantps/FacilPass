/**
 * 
 */
package constant;

/**
 * Enum for TB_DEVICE_CUSTOMIZATION_STATE. 
 * @author tmolina
 * @since 22-02-2011
 */
public enum CustomizationState {
	
	NEW(1L),
	REJECTED(2L), 
	APPROVED(3L),
	CUSTOMIZED(4L),
	INSTALLED(5L);

	 private Long id;

	 /**
	  * @param id
	  */
	 private CustomizationState(Long id) {
	   this.id = id;
	 }

	 /**
	  * @return id
	  */
	 public Long getId() {
	  	 return id;
	 }
}