package util.ws.dtos;

import java.io.Serializable;

import jpa.TbBankTransact;

/**
 * clase que representa la respuesta de consulta de transacciones AVAL
 * @author r.bautista
 *
 */
public class WsRtaConsultTransAval extends AbstractWsRta implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TbBankTransact bankTrans;

	/**
	 * Estados de la transacción
	 */
	public static final int APROVED = 1;
	public static final int REJECTED = 0;
	public static final int PENDING = -1;
	
	/**
	 * Contiene el estado de la transacción
	 * -1 - rechazada
	 * 0 - pendiente
	 * 1 - aprobada
	 */
	private int status;
	
	/**
	 * Indica si lo proceso la sonda
	 */
	private boolean procesoSonda;

	/**
	 * Constructor vacío.
	 * Inicializa el estado en pendiente
	 */
	public WsRtaConsultTransAval() {
		super();
		this.status = WsRtaConsultTransAval.PENDING;;
		this.procesoSonda = false;
	}

	public TbBankTransact getBankTrans() {
		return this.bankTrans;
	}

	public void setBankTrans(TbBankTransact bankTrans) {
		this.bankTrans = bankTrans;
	}

	public void setStatus(int status){
		switch(status){
		case APROVED:
		case REJECTED :
		case PENDING :
			this.status = status;
			break;
		default :
			throw new IllegalArgumentException("Valor de estado invalido");
		}

	}

	/**
	 * Indica si la transacción fue aceptada
	 * @return
	 */
	public boolean isAccepted(){
		return this.status == APROVED;
	}

	/**
	 * Indica si la transacción fue rechazada
	 * @return
	 */
	public boolean isRejected(){
		return this.status == REJECTED;
	}

	public boolean isProcesoSonda() {
		return this.procesoSonda;
	}

	public void setProcesoSonda(boolean procesoSonda) {
		this.procesoSonda = procesoSonda;
	}

}
