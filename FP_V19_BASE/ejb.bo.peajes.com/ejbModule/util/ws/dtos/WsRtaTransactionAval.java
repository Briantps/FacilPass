package util.ws.dtos;

import java.io.Serializable;

/**
 * Clase que representa la respuesta del consume del WebService WSCreateTransactionAval
 * Corresponde a CustomerLogin
 * 
 * @author r.bautista
 *
 */
public class WsRtaTransactionAval extends AbstractWsRta implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String bankUrl;

	private Long transactId;
	
	public WsRtaTransactionAval() {
		super();
		this.bankUrl = null;
		this.transactId = null;
	}

	public String getBankUrl() {
		return bankUrl;
	}

	public void setBankUrl(String bankUrl) {
		this.bankUrl = bankUrl;
	}

	public Long getTransactId() {
		return transactId;
	}

	public void setTransactId(Long transactId) {
		this.transactId = transactId;
	}
	
	

}
