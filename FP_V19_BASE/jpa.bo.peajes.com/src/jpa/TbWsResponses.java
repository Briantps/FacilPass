package jpa;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the TB_WS_RESPONSES database table.
 * 
 */
@Entity
@Table(name="TB_WS_RESPONSES")
public class TbWsResponses implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="TB_WS_RESPONSES_ID")
	private Long tbWsResponsesId;

	private Long code;

	private String description;

	@Column(name="RESONSE_STATE")
	private Integer state;

	//bi-directional many-to-one association to TbWebServices
    @ManyToOne
	@JoinColumn(name="TB_WEB_SERVICES_ID")
	private TbWebServices tbWebService;

    public TbWsResponses() {
    }

	public Long getTbWsResponsesId() {
		return this.tbWsResponsesId;
	}

	public void setTbWsResponsesId(Long tbWsResponsesId) {
		this.tbWsResponsesId = tbWsResponsesId;
	}

	public Long getCode() {
		return this.code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getState() {
		return this.state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public TbWebServices getTbWebService() {
		return this.tbWebService;
	}

	public void setTbWebService(TbWebServices tbWebService) {
		this.tbWebService = tbWebService;
	}

	/**
	 * Indica si el código recibido es de operación exitosa o fallida
	 * @return true si el código de respuesta es un valor satisfactorio.
	 */
	public boolean isValid(){
		if ( this.state == null ) {
			return false;
		}

		return this.state.intValue() == 1; 
	}
	
	/**
	 * Retorna el resultado de la transaccion más amplio
	 * @return codigo - descripcion
	 */
	public String getDetailedDescription(){
		return "" + this.getCode() + " - " + this.getDescription();
	}
}