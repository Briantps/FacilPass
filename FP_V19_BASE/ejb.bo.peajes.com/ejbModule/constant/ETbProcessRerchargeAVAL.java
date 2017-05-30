/**
 * 
 */
package constant;

import java.io.Serializable;

/**
 * clase que representa la tabla TB_PROCESS_RECHARGE_AVAL. Cada registro nuevo debe ser insertado en la enum 
 * @author r.bautista
 *
 */
public enum ETbProcessRerchargeAVAL implements Serializable {

	SG_C_AVAL(1L, null),
	SG_C_PSE(2L, null),
	
	/**
	 * Bryan insertar las suyas y devolver la enum para que todos nos actualicemos
	 */
	
	PAGO_PROG_AVAL_PREPAGO(4L, PaymentMethod.PREPAID),
	PAGO_PROG_AVAL_POSTPAGO(5L, PaymentMethod.POSTPAID),
	;
	
	/**
	 * Identificador del registro
	 */
	private Long id;
	
	/**
	 * Identificador del tipo de pago
	 */
	private PaymentMethod paymentMethod;

	/**
	 * 
	 * @param id
	 */
	private ETbProcessRerchargeAVAL(Long id, PaymentMethod paymentMethod) {
		this.id = id;
		this.paymentMethod = paymentMethod;
	}

	public Long getId() {
		return this.id;
	}

	
	public PaymentMethod getPpaymentMethod() {
		return this.paymentMethod;
	}
	
	/**
	 * Indica si el proceso tiene metodo de pago
	 * @return
	 */
	public boolean hasPaymentMethod(){
		return this.paymentMethod != null;
	}
}
