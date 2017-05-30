/**
 * 
 */
package jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author tmolina
 *
 */
@Entity
@Table(name="TB_CODE_TYPE")
public class TbCodeType  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="CODE_TYPE_ID")
	private Long codeTypeId;
	
	@Column(name="CODE_TYPE_DESCRIPTION")
	private String codeTypeDescription;
	
	@Column(name="CODE_TYPE_ABBREVIATION")
	private String codeTypeAbbreviation;
	
	public TbCodeType(){
		super();
	}

	/**
	 * @param codeTypeId the codeTypeId to set
	 */
	public void setCodeTypeId(Long codeTypeId) {
		this.codeTypeId = codeTypeId;
	}

	/**
	 * @return the codeTypeId
	 */
	public Long getCodeTypeId() {
		return codeTypeId;
	}

	/**
	 * @param codeTypeDescription the codeTypeDescription to set
	 */
	public void setCodeTypeDescription(String codeTypeDescription) {
		this.codeTypeDescription = codeTypeDescription;
	}

	/**
	 * @return the codeTypeDescription
	 */
	public String getCodeTypeDescription() {
		return codeTypeDescription;
	}

	/**
	 * @param codeTypeAbbreviation the codeTypeAbbreviation to set
	 */
	public void setCodeTypeAbbreviation(String codeTypeAbbreviation) {
		this.codeTypeAbbreviation = codeTypeAbbreviation;
	}

	/**
	 * @return the codeTypeAbbreviation
	 */
	public String getCodeTypeAbbreviation() {
		return codeTypeAbbreviation;
	}
}