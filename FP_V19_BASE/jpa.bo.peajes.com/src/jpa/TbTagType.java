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
 * The persistent class for the TB_TAG_TYPE database table.
 * @author tmolina
 *
 */
@Entity
@Table(name="TB_TAG_TYPE")
public class TbTagType implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="TBTAGTYPE_TAGTYPEID_GENERATOR",sequenceName="TB_TAG_TYPE_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBTAGTYPE_TAGTYPEID_GENERATOR")
	@Column(name="TAG_TYPE_ID")
	private Long tagTypeId;
	
	@Column(name="TAG_TYPE_NAME")
	private String tagTypeName;
	
	@Column(name="TAG_TYPE_CODE")
	private String tagTypeCode;
	
	@Column(name="TAG_STATE")
	private Integer tagState;
	
	@Column(name="SERIAL_LENGTH")
	private Long serialLength;
	
	@Column(name="NUMBER_OF_SPLIT")
	private Long numberOfSplit;
	
	/**
	 * Constructor.
	 */
	public TbTagType() {
		super();
	}
	
	/**
	 * @param tagTypeId the tagTypeId to set
	 */
	public void setTagTypeId(Long tagTypeId) {
		this.tagTypeId = tagTypeId;
	}

	/**
	 * @return the tagTypeId
	 */
	public Long getTagTypeId() {
		return tagTypeId;
	}

	/**
	 * @param tagTypeName the tagTypeName to set
	 */
	public void setTagTypeName(String tagTypeName) {
		this.tagTypeName = tagTypeName;
	}

	/**
	 * @return the tagTypeName
	 */
	public String getTagTypeName() {
		return tagTypeName;
	}

	/**
	 * @param tagTypeCode the tagTypeCode to set
	 */
	public void setTagTypeCode(String tagTypeCode) {
		this.tagTypeCode = tagTypeCode;
	}

	/**
	 * @return the tagTypeCode
	 */
	public String getTagTypeCode() {
		return tagTypeCode;
	}

	/**
	 * @param serialLength the serialLength to set
	 */
	public void setSerialLength(Long serialLength) {
		this.serialLength = serialLength;
	}

	/**
	 * @return the serialLength
	 */
	public Long getSerialLength() {
		return serialLength;
	}

	/**
	 * @param numberOfSplit the numberOfSplit to set
	 */
	public void setNumberOfSplit(Long numberOfSplit) {
		this.numberOfSplit = numberOfSplit;
	}

	/**
	 * @return the numberOfSplit
	 */
	public Long getNumberOfSplit() {
		return numberOfSplit;
	}

	/**
	 * @param tagState the tagState to set
	 */
	public void setTagState(Integer tagState) {
		this.tagState = tagState;
	}

	/**
	 * @return the tagState
	 */
	public Integer getTagState() {
		return tagState;
	}


}