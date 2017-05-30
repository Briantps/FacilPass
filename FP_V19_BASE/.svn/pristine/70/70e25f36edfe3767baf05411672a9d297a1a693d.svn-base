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
 * The persistent class for the TB_OPTION database table.
 * 
 */
@Entity
@Table(name="TB_OPTION")
public class TbOption implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TB_OPTION_TBOPTIONID_GENERATOR")
	@SequenceGenerator(name = "TB_OPTION_TBOPTIONID_GENERATOR", sequenceName = "TB_OPTION_SEQ",allocationSize=1)
	@Column(name="OPTION_ID")
	private long optionId;

	@Column(name="OPTION_NAME")
	private String optionName;
	
	@Column(name="OPTION_ORDER")
	private Long optionOrder;
	
	@Column(name="OPTION_FATHER")
	private Long optionFatherId;

	/**
	 * Constructor
	 */
    public TbOption() {
    	super();
    }

    /**
     * 
     * @return optionId
     */
	public long getOptionId() {
		return this.optionId;
	}

	/**
	 * Setter for optionId
	 * @param optionId
	 */
	public void setOptionId(long optionId) {
		this.optionId = optionId;
	}

	/**
	 * 
	 * @return optionName
	 */
	public String getOptionName() {
		return this.optionName;
	}

	/**
	 * Setter for optionName
	 * @param optionName
	 */
	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}

	public void setOptionOrder(Long optionOrder) {
		this.optionOrder = optionOrder;
	}

	public Long getOptionOrder() {
		return optionOrder;
	}

	/**
	 * @param optionFatherId the optionFatherId to set
	 */
	public void setOptionFatherId(Long optionFatherId) {
		this.optionFatherId = optionFatherId;
	}

	/**
	 * @return the optionFatherId
	 */
	public Long getOptionFatherId() {
		return optionFatherId;
	}
}