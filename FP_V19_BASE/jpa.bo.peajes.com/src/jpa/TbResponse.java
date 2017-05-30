package jpa;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The persistent class for the TB_RESPONSE database table.
 * 
 */

@Entity
@Table(name="TB_RESPONSE")
public class TbResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="TB_RESPONSE_GENERATOR", sequenceName="TB_RESPONSE_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TB_RESPONSE_GENERATOR")
	@Column(name="RESPONSE_ID", unique=true, nullable=false, precision=19)
	private Long responsetId;

	@Column(name="RESPONSE_DESCRIPTION")
	private String responseDescription;
	
	@Column(name="DATE_RESPONSE")
	private Timestamp dateResponse;
	
	
	public TbResponse(){
		super();
	}

	public void setResponsetId(Long responsetId) {
		this.responsetId = responsetId;
	}

	public Long getResponsetId() {
		return responsetId;
	}

	public void setResponseDescription(String responseDescription) {
		this.responseDescription = responseDescription;
	}

	public String getResponseDescription() {
		return responseDescription;
	}

	public void setDateResponse(Timestamp dateResponse) {
		this.dateResponse = dateResponse;
	}

	public Timestamp getDateResponse() {
		return dateResponse;
	}

}
