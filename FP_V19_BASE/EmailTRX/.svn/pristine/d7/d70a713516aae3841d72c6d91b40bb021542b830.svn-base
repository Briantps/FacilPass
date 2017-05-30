/**
 * 
 */
package jpa;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Persistence class for Table TB_CATEGORY
 * @author tmolina
 *
 */
@Entity
@Table(name="TB_CATEGORY")
public class TbCategory implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="CATEGORY_ID")
	private Long categoryId;
	
	@Column(name="CATEGORY_NAME")
	private String categoryName;
	
	//bi-directional one-to-many association to TbVehicle
	@OneToMany(mappedBy="tbCategory")
	private List<TbVehicle> tbVehicles;
	
	@Column(name="CATEGORY_CODE")
	private String categoryCode;
	
	@Column(name="CATEGORY_INVIAS_CODE")
	private String categoryInviasCode;
	
	/**
	 * Constructor
	 */
	public TbCategory(){
		super();
	}

	/**
	 * @param categoryId the categoryId to set
	 */
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	/**
	 * @return the categoryId
	 */
	public Long getCategoryId() {
		return categoryId;
	}

	/**
	 * @param categoryName the categoryName to set
	 */
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	/**
	 * @return the categoryName
	 */
	public String getCategoryName() {
		return categoryName;
	}

	/**
	 * @param tbVehicle the tbVehicle to set
	 */
	public void setTbVehicles(List<TbVehicle> tbVehicles) {
		this.tbVehicles = tbVehicles;
	}

	/**
	 * @return the tbVehicle
	 */
	public List<TbVehicle> getTbVehicles() {
		return tbVehicles;
	}

	/**
	 * @param categoryCode the categoryCode to set
	 */
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	/**
	 * @return the categoryCode
	 */
	public String getCategoryCode() {
		return categoryCode;
	}

	/**
	 * @param categoryInviasCode the categoryInviasCode to set
	 */
	public void setCategoryInviasCode(String categoryInviasCode) {
		this.categoryInviasCode = categoryInviasCode;
	}

	/**
	 * @return the categoryInviasCode
	 */
	public String getCategoryInviasCode() {
		return categoryInviasCode;
	}
}