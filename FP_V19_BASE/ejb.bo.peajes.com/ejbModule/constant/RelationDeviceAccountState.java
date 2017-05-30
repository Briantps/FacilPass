/**
 * 
 */
package constant;

/**
 * 
 * @author tmolina
 * @since 2011-10-11
 */
public enum RelationDeviceAccountState {
	
	ACTIVE(0),
	INACTIVE(1);

	 private Integer id;

	 /**
	  * 
	  * @param id
	  */
	 private RelationDeviceAccountState(Integer id) {
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