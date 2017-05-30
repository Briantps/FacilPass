/**
 * 
 */
package jpa;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The persistent class for the TB_BRAND database table.
 * @author tmolina
 */
 @Entity
 @Table(name="TB_BRAND")
public class TbBrand implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TB_BRAND_TBBRANDID_GENERATOR")
	@SequenceGenerator(name="TB_BRAND_TBBRANDID_GENERATOR",  sequenceName = "TB_BRAND_SEQ")
	@Column(name="BRAND_ID")
	private Long brandId;
	
	@Column(name="BRAND_NAME")
	private String brandName;
	
	//bi-directional many-to-one association to TbVehicle
	@OneToMany(mappedBy="tbBrand")
	private Set<TbVehicle> tbVehicles;
	
	public TbBrand(){
		super();
	}

	/**
	 * @param brandId the brandId to set
	 */
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	/**
	 * @return the brandId
	 */
	public Long getBrandId() {
		return brandId;
	}

	/**
	 * @param brandName the brandName to set
	 */
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	/**
	 * @return the brandName
	 */
	public String getBrandName() {
		return brandName;
	}

	/**
	 * @param tbVehicles the tbVehicles to set
	 */
	public void setTbVehicles(Set<TbVehicle> tbVehicles) {
		this.tbVehicles = tbVehicles;
	}

	/**
	 * @return the tbVehicles
	 */
	public Set<TbVehicle> getTbVehicles() {
		return tbVehicles;
	}
}