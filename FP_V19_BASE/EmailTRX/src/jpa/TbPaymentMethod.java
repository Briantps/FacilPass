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
* Entity implementation class for Entity: TbPaymentMethod
*
*/
@Entity
@Table(name="TB_PAYMENT_METHOD")
public class TbPaymentMethod implements Serializable{
private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TB_PAYMENT_METHOD_GENERATOR")
	@SequenceGenerator(name = "TB_PAYMENT_METHOD_GENERATOR", sequenceName = "TB_PAYMENT_METHOD_SEQ",allocationSize=1, initialValue=1)
	@Column(name="PAYMENT_METHOD_ID")
	private Long paymentMethodId;

	@Column(name="PAYMENT_METHOD")
	private String paymentMethod;
	
	@Column(name="DESCRIPTION_PAYMENT_METHOD")
	private String descriptionPaymentMethod;

	public void setPaymentMethodId(Long paymentMethodId) {
		this.paymentMethodId = paymentMethodId;
	}

	public Long getPaymentMethodId() {
		return paymentMethodId;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setDescriptionPaymentMethod(String descriptionPaymentMethod) {
		this.descriptionPaymentMethod = descriptionPaymentMethod;
	}

	public String getDescriptionPaymentMethod() {
		return descriptionPaymentMethod;
	}
	
	
	
}
