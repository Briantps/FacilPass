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
 * @author ablasquez
 *
 */
@Entity
@Table(name="TB_DISTRIBUTION_TYPE")
public class TbDistributionType  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="DISTRIBUTION_TYPE_ID")
	private Long distributionTypeId;
	
	@Column(name="DISTRIBUTION_TYPE_NAME")
	private String distributionTypeName;
	
	@Column(name="DISTRIBUTION_TYPE_DESCRIPTION")
	private String distributionTypeDescription;
	
	public TbDistributionType(){
		super();
	}

	public void setDistributionTypeId(Long distributionTypeId) {
		this.distributionTypeId = distributionTypeId;
	}

	public Long getDistributionTypeId() {
		return distributionTypeId;
	}

	public void setDistributionTypeName(String distributionTypeName) {
		this.distributionTypeName = distributionTypeName;
	}

	public String getDistributionTypeName() {
		return distributionTypeName;
	}

	public void setDistributionTypeDescription(
			String distributionTypeDescription) {
		this.distributionTypeDescription = distributionTypeDescription;
	}

	public String getDistributionTypeDescription() {
		return distributionTypeDescription;
	}

	
}