package constant;

public enum VialTypeOpertionTag {
	
	DESCUENTO(4L),
	RECARGA(5L),
	DESCUENTONORMAL(7L);
	
	private Long tipo;
	
	private VialTypeOpertionTag(Long t){
		this.tipo=t;
	}
	
	public Long getTipo(){
		return tipo;
	}
}
