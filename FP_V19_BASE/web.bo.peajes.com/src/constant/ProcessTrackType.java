/**
 * 
 */
package constant;

import jpa.TbProcessTrackType;

/**
 * Enum for ProcessTrackType (Mapped IDs fromTable {@link TbProcessTrackType}
 * 
 * @author tmolina
 * @since 07-02-2011
 */
public enum ProcessTrackType {
	
	CLIENT(1L), 
	DEVICE(2L), 
	CUSTOMIZATION(3L), 
	INCLUSION(4L),
	PURCHASE_ORDER(5L),
	MY_CLIENT_PROCESS(6L),
	ACCOUNT1(7L),
	AUTHOMATHIC_RECHARGE(8L);

	 private Long id;

	 /**
	  * 
	  * @param id
	  */
	 private ProcessTrackType(Long id) {
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