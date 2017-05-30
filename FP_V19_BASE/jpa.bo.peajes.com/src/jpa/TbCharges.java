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
 * Entity implementation class for Entity: TbFrequency
 *
 */
@Entity
@Table(name="TB_CHARGES")
public class TbCharges implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TB_CHARGES_GENERATOR")
	@SequenceGenerator(name = "TB_CHARGES_GENERATOR", sequenceName = "TB_CHARGES_SEQ",allocationSize=1)
	@Column(name="CHARGE_ID")
	private Long chargeId;
	
	@Column(name="CHARGE_DESCRIPTION")
	private String chargeDescription;
	
	@Column(name="CHARGE_TYPE_VALUE")
	private int chargeTypeValue;
	
	@Column(name="CHARGE_VALUE")
	private Long chargeValue;
	
	@Column(name="CHARGE_TYPE_VALUE_TEXT")
	private String chargeTypeValueText;
	
	public void setChargeId(Long chargeId) {
		this.chargeId = chargeId;
	}

	public Long getChargeId() {
		return chargeId;
	}

	public void setChargeDescription(String chargeDescription) {
		this.chargeDescription = chargeDescription;
	}

	public String getChargeDescription() {
		return chargeDescription;
	}

	public void setChargeTypeValue(int chargeTypeValue) {
		this.chargeTypeValue = chargeTypeValue;
	}

	public int getChargeTypeValue() {
		return chargeTypeValue;
	}

	public void setChargeValue(Long chargeValue) {
		this.chargeValue = chargeValue;
	}

	public Long getChargeValue() {
		return chargeValue;
	}

	public void setChargeTypeValueText(String chargeTypeValueText) {
		this.chargeTypeValueText = chargeTypeValueText;
	}

	public String getChargeTypeValueText() {
		return chargeTypeValueText;
	}
}
