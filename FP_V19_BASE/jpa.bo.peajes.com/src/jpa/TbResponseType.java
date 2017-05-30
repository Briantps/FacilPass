package jpa;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: TbResponseType
 *
 */
@Entity
@Table(name="TB_RESPONSE_TYPE")
public class TbResponseType implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="RESPONSE_TYPE_ID")
	private Long responseTypeId;
	
	@Column(name="RESPONSE_DESCRIPTION")
	private String responseDescrip;
	
	@Column(name="RESPONSE_TYPE_COD")
	private Long responseTypeCode;
	
	@Column(name="RESPONSE_ENTITY")
	private Long responseEntity;
	
	public TbResponseType() {
		super();
	}


	public void setResponseTypeId(Long responseTypeId) {
		this.responseTypeId = responseTypeId;
	}


	public Long getResponseTypeId() {
		return responseTypeId;
	}


	public void setResponseDescrip(String responseDescrip) {
		this.responseDescrip = responseDescrip;
	}


	public String getResponseDescrip() {
		return responseDescrip;
	}


	public Long getResponseTypeCode() {
		return responseTypeCode;
	}


	public void setResponseTypeCode(Long responseTypeCode) {
		this.responseTypeCode = responseTypeCode;
	}


	public Long getResponseEntity() {
		return responseEntity;
	}


	public void setResponseEntity(Long responseEntity) {
		this.responseEntity = responseEntity;
	}
   
}