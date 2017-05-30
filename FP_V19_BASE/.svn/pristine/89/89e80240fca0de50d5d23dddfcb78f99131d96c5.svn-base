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
 * The persistent class for the TB_COMPANY_TAG database table.
 * @author psanchez
 *
 */
@Entity
@Table(name = "TB_COMPANY_TAG")
public class TbCompanyTag implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "TB_COMPANY_TAG_SEQ", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "TB_COMPANY_TAG_SEQ", sequenceName = "TB_COMPANY_TAG_SEQ",allocationSize=1)
	
	@Column(name="COMPANY_TAG_ID")
	private Long companyTagId;
	
	@Column(name="COMPANY_TAG_NAME")
	private String companyTagName;
	
	@Column(name="COMPANY_TAG_STATE")
	private Integer companyTagState;
	
	/**
	 * Constructor
	 */
	public TbCompanyTag(){
		super();
	}

	public void setCompanyTagId(Long companyTagId) {
		this.companyTagId = companyTagId;
	}

	public Long getCompanyTagId() {
		return companyTagId;
	}

	public void setCompanyTagName(String companyTagName) {
		this.companyTagName = companyTagName;
	}

	public String getCompanyTagName() {
		return companyTagName;
	}

	public void setCompanyTagState(Integer companyTagState) {
		this.companyTagState = companyTagState;
	}

	public Integer getCompanyTagState() {
		return companyTagState;
	}
	
}