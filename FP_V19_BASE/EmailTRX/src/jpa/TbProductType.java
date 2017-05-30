package jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="TB_PRODUCT_TYPE")
public class TbProductType implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="PRODUCT_TYPE_ID")
	private Long productTypeId;
	
	@Column(name="PRODUCT_TYPE_DESCRIPTION")
	private String ProductTypeDescription;
	
	public TbProductType(){
		super();
	}

	/**
	 * @param productTypeId the productTypeId to set
	 */
	public void setProductTypeId(Long productTypeId) {
		this.productTypeId = productTypeId;
	}

	/**
	 * @return the productTypeId
	 */
	public Long getProductTypeId() {
		return productTypeId;
	}

	/**
	 * @param productTypeDescription the productTypeDescription to set
	 */
	public void setProductTypeDescription(String productTypeDescription) {
		ProductTypeDescription = productTypeDescription;
	}

	/**
	 * @return the productTypeDescription
	 */
	public String getProductTypeDescription() {
		return ProductTypeDescription;
	}
}
