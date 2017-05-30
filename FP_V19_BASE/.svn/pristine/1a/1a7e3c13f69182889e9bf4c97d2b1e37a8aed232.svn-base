/**
 * 
 */
package jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author tmolina
 * 
 */
@Entity
@Table(name = "TB_MUNICIPALITY")
public class TbMunicipality implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "MUNICIPALITY_ID")
	private Long municipalityId;

	@Column(name = "MUNICIPALITY_NAME")
	private String municipalityName;

	@ManyToOne
	@JoinColumn(name = "DEPARTMENT_ID")
	private TbDepartment tbDepartment;

	/**
	 * Constructor.
	 */
	public TbMunicipality() {
		super();
	}

	/**
	 * @param municipalityId the municipalityId to set
	 */
	public void setMunicipalityId(Long municipalityId) {
		this.municipalityId = municipalityId;
	}

	/**
	 * @return the municipalityId
	 */
	public Long getMunicipalityId() {
		return municipalityId;
	}

	/**
	 * @param municipalityName the municipalityName to set
	 */
	public void setMunicipalityName(String municipalityName) {
		this.municipalityName = municipalityName;
	}

	/**
	 * @return the municipalityName
	 */
	public String getMunicipalityName() {
		return municipalityName;
	}

	/**
	 * @param tbDepartment the tbDepartment to set
	 */
	public void setTbDepartment(TbDepartment tbDepartment) {
		this.tbDepartment = tbDepartment;
	}

	/**
	 * @return the tbDepartment
	 */
	public TbDepartment getTbDepartment() {
		return tbDepartment;
	}
}