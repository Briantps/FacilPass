package constant;

public enum AjustmentStateType {
	
	PENDING_FOR_APPLY(1L),
	APPLY(3L),
	REJECTED(2L);
	
	private Long id;

	 /**
	  * 
	  * @param id
	  */
	 private AjustmentStateType(Long id) {
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
