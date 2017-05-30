package jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="RE_RESPONSE_TYPE_RECHARGE")
public class ReResponseTypeRecharge implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "RE_RESPONSE_TYPE_RECHARGE_SEQ", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "RE_RESPONSE_TYPE_RECHARGE_SEQ", sequenceName = "RE_RESPONSE_TYPE_RECHARGE_SEQ",allocationSize=1)

	@Column(name="RESPONSE_TYPE_RECHARGE")
	private Long responseTyeRechargeId;

	@ManyToOne
	@JoinColumn(name="RESPONSE_TYPE_ID")
	private TbResponseType responseTypeId;

	@ManyToOne
	@JoinColumn(name="UMBRAL_ID")
	private TbUmbral umbralId;


	public ReResponseTypeRecharge(){
		super();
	}


	public Long getResponseTyeRechargeId() {
		return responseTyeRechargeId;
	}


	public void setResponseTyeRechargeId(Long responseTyeRechargeId) {
		this.responseTyeRechargeId = responseTyeRechargeId;
	}


	public TbResponseType getResponseTypeId() {
		return responseTypeId;
	}


	public void setResponseTypeId(TbResponseType responseTypeId) {
		this.responseTypeId = responseTypeId;
	}


	public TbUmbral getUmbralId() {
		return umbralId;
	}


	public void setUmbralId(TbUmbral umbralId) {
		this.umbralId = umbralId;
	}
}