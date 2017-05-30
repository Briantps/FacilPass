package jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;

@Entity
@Table(name = "TB_OPT_ACT_REFERENCE_TYPE")
public class TbOptActRefefenceType implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "OPT_ACT_REFERENCE_ID")
	private String optActReferenceId;
	
	@Column(name = "OPT_ACT_DESCRIPTION")
	private String optActDescription;

	public void setOptActReferenceId(String optActReferenceId) {
		this.optActReferenceId = optActReferenceId;
	}

	public String getOptActReferenceId() {
		return optActReferenceId;
	}

	public void setOptActDescription(String optActDescription) {
		this.optActDescription = optActDescription;
	}

	public String getOptActDescription() {
		return optActDescription;
	}

}
