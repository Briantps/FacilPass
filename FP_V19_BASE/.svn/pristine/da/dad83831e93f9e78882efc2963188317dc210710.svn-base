package jpa;

import java.io.Serializable;
import java.sql.Timestamp;

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
@Table(name="TB_ADJUSTMENT_OBJECTION")
public class TbAdjustmentObjection implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="ADJUSTMENT_OBJECTION_SEQ",sequenceName="TB_ADJUSTMENT_OBJECTION_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ADJUSTMENT_OBJECTION_SEQ")
	@Column(name="ADJUSTMENT_OBJECTION_ID")
	private Long adjustmentObjectionId;
	
	@ManyToOne
	@JoinColumn(name="OBJECTION_ID")
	private TbObjection objectionId;
	
	@Column(name="ADJUSTMENT_VALUE")
	private Long adjustmentValue;
	
	@Column(name="ADJUSTMENT_STATE")
	private Long adjustmentState;
	
	@Column(name="USER_VERIFIER_ID")
	private Long userVerifierId;
	
	@Column(name="ADJUSTMENT_DATE")
	private Timestamp adjustmentDate;

	public void setAdjustmentObjectionId(Long adjustmentObjectionId) {
		this.adjustmentObjectionId = adjustmentObjectionId;
	}

	public Long getAdjustmentObjectionId() {
		return adjustmentObjectionId;
	}

	public void setObjectionId(TbObjection objectionId) {
		this.objectionId = objectionId;
	}

	public TbObjection getObjectionId() {
		return objectionId;
	}

	public void setAdjustmentValue(Long adjustmentValue) {
		this.adjustmentValue = adjustmentValue;
	}

	public Long getAdjustmentValue() {
		return adjustmentValue;
	}

	public void setAdjustmentState(Long adjustmentState) {
		this.adjustmentState = adjustmentState;
	}

	public Long getAdjustmentState() {
		return adjustmentState;
	}

	public void setUserVerifierId(Long userVerifierId) {
		this.userVerifierId = userVerifierId;
	}

	public Long getUserVerifierId() {
		return userVerifierId;
	}

	public void setAdjustmentDate(Timestamp adjustmentDate) {
		this.adjustmentDate = adjustmentDate;
	}

	public Timestamp getAdjustmentDate() {
		return adjustmentDate;
	}

}
