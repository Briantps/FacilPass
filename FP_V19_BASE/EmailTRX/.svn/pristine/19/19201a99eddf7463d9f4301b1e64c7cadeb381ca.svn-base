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
 * The persistent class for the TB_PAY_TYPE database table.
 * @author tmolina
 *
 */
@Entity
@Table(name="TB_PAY_TYPE")
public class TbPayType implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="TBPAYTYPE_PAYTYPEID_GENERATOR",sequenceName="TB_PAY_TYPE_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBPAYTYPE_PAYTYPEID_GENERATOR")
	@Column(name="PAY_TYPE_ID")
	private Long payTypeId;
	
	@Column(name="PAY_TYPE_NAME")
	private String payTypeName;
	
	/**
	 * Constructor.
	 */
	public TbPayType() {
		super();
	}

	/**
	 * @param payTypeId the payTypeId to set
	 */
	public void setPayTypeId(Long payTypeId) {
		this.payTypeId = payTypeId;
	}

	/**
	 * @return the payTypeId
	 */
	public Long getPayTypeId() {
		return payTypeId;
	}

	/**
	 * @param payTypeName the payTypeName to set
	 */
	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}

	/**
	 * @return the payTypeName
	 */
	public String getPayTypeName() {
		return payTypeName;
	}
}