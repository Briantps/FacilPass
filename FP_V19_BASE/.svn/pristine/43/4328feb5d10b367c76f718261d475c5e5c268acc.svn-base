package jpa;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TB_PAYMENT_METHOD database table.
 * 
 */
@Entity
@Table(name="TB_PAYMENT_METHOD")
public class TbPaymentMethod implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TB_PAYMENT_METHOD_PAYMENTMETHODID_GENERATOR", sequenceName="PAYMENT_METHOD_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TB_PAYMENT_METHOD_PAYMENTMETHODID_GENERATOR")
	@Column(name="PAYMENT_METHOD_ID")
	private Long paymentMethodId;

	@Column(name="DESCRIPTION_PAYMENT_METHOD")
	private String descriptionPaymentMethod;

	@Column(name="PAYMENT_METHOD")
	private String paymentMethod;

    public TbPaymentMethod() {
    	super();
    }

	public Long getPaymentMethodId() {
		return this.paymentMethodId;
	}

	public void setPaymentMethodId(Long paymentMethodId) {
		this.paymentMethodId = paymentMethodId;
	}

	public String getDescriptionPaymentMethod() {
		return this.descriptionPaymentMethod;
	}

	public void setDescriptionPaymentMethod(String descriptionPaymentMethod) {
		this.descriptionPaymentMethod = descriptionPaymentMethod;
	}

	public String getPaymentMethod() {
		return this.paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	
}