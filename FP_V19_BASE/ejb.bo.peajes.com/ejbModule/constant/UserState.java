/**
 * 
 */
package constant;

/**
 * Enum for USER STATES:
 * -1: Pre Enroll
 * 0 : Active user.
 * 1 : Inactive User.
 * 5 : Inactive for Administrator
 * 
 * @author tmolina
 * @since 15-02-2011
 */
public enum UserState {
	
	PREENROLL(-1),
	ACTIVE(0), 
	INACTIVE(1),
	CLIENT(2),
	VALIDATE(3),
	INACTIVE_FOR_ADMIN(5);

	 private Integer id;

	 /**
	  * 
	  * @param id
	  */
	 private UserState(Integer id) {
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