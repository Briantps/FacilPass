package constant;

public enum VialTypeTag {
	
	PREPAGO(1L),
	POSTPAGO(0L),
	PASOS(2L);
	
	private Long tipo;
	
	private VialTypeTag(Long t){
		this.tipo=t;
	}
	
	public Long getTipo(){
		return tipo;
	}
}
