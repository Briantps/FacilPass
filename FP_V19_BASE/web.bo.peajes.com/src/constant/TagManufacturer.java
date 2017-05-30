/**
 * 
 */
package constant;

/**
 * Enum for TB_TAG_TYPE. 
 * @author tmolina
 * @since 2011-10-06
 */
public enum TagManufacturer {
	
	KAPSCH(1L),
	QFREE(2L),
	SIRIT(3L);

	 private Long id;

	 /**
	  * @param id
	  */
	 private TagManufacturer(Long id) {
	   this.id = id;
	 }

	 /**
	  * @return id
	  */
	 public Long getId() {
	  	 return id;
	 }
}