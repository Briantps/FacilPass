package jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The persistent class for the TB_COMPANY_TYPE database table.
 * @author tmolina
 *
 */
@Entity
@Table(name = "TB_COMPANY_TYPE")
public class TbCompanyType implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="COMPANY_TYPE_ID")
	private Long companyTypeId;
	
	@Column(name="COMPANY_TYPE_DESCRIPTION")
	private String companyTypeDescription;
	
	/**
	 * Constructor
	 */
	public TbCompanyType(){
		super();
	}

	/**
	 * @param companyTypeId the companyTypeId to set
	 */
	public void setCompanyTypeId(Long companyTypeId) {
		this.companyTypeId = companyTypeId;
	}

	/**
	 * @return the companyTypeId
	 */
	public Long getCompanyTypeId() {
		return companyTypeId;
	}

	/**
	 * @param companyTypeDescription the companyTypeDescription to set
	 */
	public void setCompanyTypeDescription(String companyTypeDescription) {
		this.companyTypeDescription = companyTypeDescription;
	}

	/**
	 * @return the companyTypeDescription
	 */
	public String getCompanyTypeDescription() {
		return companyTypeDescription;
	}
}