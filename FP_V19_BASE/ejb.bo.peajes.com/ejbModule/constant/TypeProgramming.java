package constant;

import jpa.TbTypeProgramming;

/**
 * Enum (Mapped IDs fromTable {@link TbTypeProgramming}
 * 
 * @author JGomez
 * @since 28-02-2017
 */
public enum TypeProgramming {
	
	FREQUENCY(1L),
	MINIMUM_BALANCE(2L);
	
	 private Long id;

	 /**
	  * 
	  * @param id
	  */
	 private TypeProgramming(Long id) {
	   this.id = id;
	 }

	 /**
	  * 
	  * @return id
	  */
	 public Long getId() {
	  	 return id;
	 }
	 
	 
	 /**
	  * Retorna el TypePrograming acorde al id recibido o null si no existe
	  * @param id identificador de TypePrograming a retornar
	  * @return TypePrograming con el id indicado
	  * @author TPS r.bautista
	  */
	 public static TypeProgramming getTypePrograming(Long id){
		 TypeProgramming resp = null;
		 
		 if(id == null){
			 return resp;
		 }
			 
		 
		 for(TypeProgramming tp : TypeProgramming.values()) {
			 if (tp.getId().equals(id)){
				 resp = tp;
				 break;
			 }
		 }
		 
		 return resp;
	 }
}