package constant;

import java.io.Serializable;

/**
 * Enumeracion que representa la tabla TB_EMAIL_PARAMETERS
 * @author r.bautista
 *
 */
public enum EEmailParam implements Serializable{



	// Tipos de datos
	// N numérico
	// A Alfabetico
	// M Dinero
	// D Fecha
	// T Hora
	
	CUENTA_FACIL_PASS(1L, "#CFP", "N"),
	NOMB_BANC(2L, "#BN", "A"),
	NOMB_CLI(4L, "#CLI", "A"),
	NUM_ID(5L, "#CC", "N"),
	BP(3L,"#BP","A"),
	VAL_TRANS(12L, "#VLT", "N"),
	NOMB_USU(15L, "#USER", "A"),
	TIPO_ID(17L, "#TPCC", "A"),
	FECHA_ACCION(18L, "#FECPROC", "D"),
	HORA_ACCION(19L, "#HORPROC", "T"),
	NO_ID_CLIENT_NUEVO(29L, "#NEWCC", "N"),
	NOMB_CLIENT_NUEVO(30L, "#NEWCLI", "A"),
	OB_ERROR(39L, "#OBERROR", "A"),

	BALANCE(41L, "#BALANCE", "M"), //BM
	
	IP_ACCION(44L, "#ACCIP", "A"),
	URL_ACCION(45L, "#ACCURL", "A"),
	PSE_ERROR(46L, "#PSEERR", "A"),

	COD_ENTIDAD(47L, "#PSECB", "A"), //BM
	NOMBR_ENTIDAD(48L, "#PSENB", "A"), //BM

	VAL_RECARG(49L, "#VALREC", "M"),
	ERROR_CONV_CANAL(63L, "#ERAGREEM", "A"),
	EMAIL_USU(65L, "#CUE", "A"),
	NOMB_CONVENIO(66L, "#NOMCO", "A"),
	FRECUENCIA(67L, "#FREQ", "A"),
	TIPO_PROGRAMACION(68L, "#TIPRG", "A"),
	ERROR_AVAL(69L, "#ERRAVAL", "A"),
	ERROR_DINAMICO(72l, "#ERRVAR", "A"),
	SALDO_BANK_TRANSAC(73l, "#SALTB", "M"),
	VALOR_AUTO_RECHARGE(74l, "#VALTAR", "M"),
	MIN_BALANCE(75L, "#SALAMB", "M"),
	ERROR_DESC(76L, "#ERRDESC", "A"),
	
	ERRAVALD(80L, "#ERRAVALD", "A"), //BM
	ERRAVALC(81L, "#ERRAVALC", "A"), //BM
	ENTAVALN(82L, "#ENTAVALN", "A"), //BM
	ENTAVALC(83L, "#ENTAVALC", "A"), //BM


	;

	/**
	 * Contiene el id
	 */
	private Long id;
	
	/**
	 * Contiene el tag
	 */
	private String tag;

	/**
	 * Contiene el tipo de dato
	 */
	private String dataType;

	private EEmailParam(Long id, String tag, String dataType){
		this.id = id;
		this.tag = tag;
		this.dataType = dataType;
	}

	public Long getId() {
		return this.id;
	}

	public String getTag() {
		return this.tag;
	}

	public String getDataType() {
		return this.dataType;
	}

	/**
	 * Indica si el tipo de dato del filtro es numerico
	 * @return
	 */
	public boolean isNumeric(){
		return this.dataType.equals("N");
	}
	
	/**
	 * Indica si el tipo de dato del filtro es alfanumerico
	 * @return
	 */
	public boolean isAlphabetic(){
		return this.dataType.equals("A");
	}

	public boolean isMoney(){
		return this.dataType.equals("M");
	}

	public boolean isDate(){
		return this.dataType.equals("D");
	}

	public boolean isTime(){
		return this.dataType.equals("T");
	}

	/**
	 * Retorna el EEmailParam asociado al tag indicado
	 * @param tag tag del parametro que se busca
	 * @return EEmailParam asociado al tag o null
	 */
	public static EEmailParam getEmailParam(String tag){
		EEmailParam resp = null;
		
		if (tag == null || tag.isEmpty()){
			return resp;
		}
		
		for(EEmailParam ep : EEmailParam.values()){
			if (ep.getTag().equals(tag)){
				resp = ep;
				break;
			}

		}

		return resp;
	}

	/**
	 * Retorna el EEmailParam asociado al id indicado
	 * @param id identificador del EEmailParam a buscar
	 * @return EEmailParam asociado al id o null
	 */
	public static EEmailParam getEmailParam(Long id){
		EEmailParam resp = null;
		
		if (id == null){
			return resp;
		}
		
		for(EEmailParam ep : EEmailParam.values()){
			if (ep.getId().longValue() == id.longValue()){
				resp = ep;
				break;
			}

		}

		return resp;
	}

}
