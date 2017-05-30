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
@Table(name = "RE_OPTION_ACTION")
public class ReOptionAction implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RE_OPTION_ACTION_REOPTIONACTIONID_GENERATOR")
	@SequenceGenerator(sequenceName = "RE_OPTION_ACTION_SEQ", name = "RE_OPTION_ACTION_REOPTIONACTIONID_GENERATOR",allocationSize=1)
	@Column(name = "OPTION_ACTION_ID")
	private Long optionActiontId;

	@Column(name = "BEHAVIOR")
	private String behavior;

	@ManyToOne
	@JoinColumn(name = "ACTION_ID")
	private TbAction actionId;

	@ManyToOne
	@JoinColumn(name = "OPTION_ID")
	private TbOption optionId;
	
	@Column(name = "OPTION_ACTION_STATE")
	private Integer optionActionState;
	
	@Column(name = "OPTION_ACTION_ORDER")
	private Integer optionActionOrder;
	
	@ManyToOne
	@JoinColumn(name = "OPTION_ACTION_REFERENCE")
	private TbOptActRefefenceType optionActionReference;
	/**
	 * Constructor
	 */
	public ReOptionAction(){
		super();
	}
	
	/**
	 * @param optionActiontId the optionActiontId to set
	 */
	public void setOptionActiontId(Long optionActiontId) {
		this.optionActiontId = optionActiontId;
	}

	/**
	 * @return the optionActiontId
	 */
	public Long getOptionActiontId() {
		return optionActiontId;
	}

	/**
	 * @param behavior the behavior to set
	 */
	public void setBehavior(String behavior) {
		this.behavior = behavior;
	}

	/**
	 * @return the behavior
	 */
	public String getBehavior() {
		return behavior;
	}

	/**
	 * @param actionId the actionId to set
	 */
	public void setActionId(TbAction actionId) {
		this.actionId = actionId;
	}

	/**
	 * @return the actionId
	 */
	public TbAction getActionId() {
		return actionId;
	}

	/**
	 * @param optionId the optionId to set
	 */
	public void setOptionId(TbOption optionId) {
		this.optionId = optionId;
	}

	/**
	 * @return the optionId
	 */
	public TbOption getOptionId() {
		return optionId;
	}

	/**
	 * @param optionActionState the optionActionState to set
	 */
	public void setOptionActionState(Integer optionActionState) {
		this.optionActionState = optionActionState;
	}

	/**
	 * @return the optionActionState
	 */
	public Integer getOptionActionState() {
		return optionActionState;
	}

	public void setOptionActionOrder(Integer optionActionOrder) {
		this.optionActionOrder = optionActionOrder;
	}

	public Integer getOptionActionOrder() {
		return optionActionOrder;
	}

	public void setOptionActionReference(TbOptActRefefenceType optionActionReference) {
		this.optionActionReference = optionActionReference;
	}

	public TbOptActRefefenceType getOptionActionReference() {
		return optionActionReference;
	}	
}