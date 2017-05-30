/**
 * 
 */
package jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The persistent class for the TB_SIGNATURE_PARAMETER database table.
 * @author jromero
 *
 */
@Entity
@Table(name="TB_SIGNATURE_PARAMETER")
public class TbSignatureParameter implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator= "TBSIGNATUREPARAMETER_SIGNATUREPARAMETERID_GENERATOR")
	@SequenceGenerator(name="TBSIGNATUREPARAMETER_SIGNATUREPARAMETERID_GENERATOR", sequenceName="TB_SIGNATURE_PARAMETER_SEQ", allocationSize=1)
	@Column(name="SIGN_PARAM_ID")
	private Long signParamId;
	
	@Column(name="SIGN_PARAM_NAME")
	private String signParamName;
	
	@Column(name="SIGN_PARAM_VALUE")
	private String signParamValue;
	
	@Column(name="SIGN_PARAM_DESCRIPTION")
	private String signParamDescription;
	
	@Column(name="SIGN_PARAM_STATE")
	private String signParamState;

	/**
	 * Constructor.
	 */
	public TbSignatureParameter() {
		super();
	}

	public Long getSignParamId() {
		return signParamId;
	}

	public void setSignParamId(Long signParamId) {
		this.signParamId = signParamId;
	}

	public String getSignParamName() {
		return signParamName;
	}

	public void setSignParamName(String signParamName) {
		this.signParamName = signParamName;
	}

	public String getSignParamValue() {
		return signParamValue;
	}

	public void setSignParamValue(String signParamValue) {
		this.signParamValue = signParamValue;
	}

	public String getSignParamDescription() {
		return signParamDescription;
	}

	public void setSignParamDescription(String signParamDescription) {
		this.signParamDescription = signParamDescription;
	}

	public String getSignParamState() {
		return signParamState;
	}

	public void setSignParamState(String signParamState) {
		this.signParamState = signParamState;
	}
	
}