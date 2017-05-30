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
 * The persistent class for the TB_COLOR database table.
 * @author tmolina
 */
 @Entity
 @Table(name="TB_COLOR")
public class TbColor implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TB_COLOR_TBCOLORID_GENERATOR")
	@SequenceGenerator(name="TB_COLOR_TBCOLORID_GENERATOR",  sequenceName = "TB_COLOR_SEQ",allocationSize=1)
	@Column(name="COLOR_ID")
	private Long colorId;
	
	@Column(name="COLOR_NAME")
	private String colorName;
	
	//bi-directional many-to-one association to TbVehicle
	@OneToMany(mappedBy="tbColor")
	private Set<TbVehicle> tbVehicles;
	
	public TbColor(){
		super();
	}

	/**
	 * @param colorName the colorName to set
	 */
	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	/**
	 * @return the colorName
	 */
	public String getColorName() {
		return colorName;
	}

	/**
	 * @param colorId the colorId to set
	 */
	public void setColorId(Long colorId) {
		this.colorId = colorId;
	}

	/**
	 * @return the colorId
	 */
	public Long getColorId() {
		return colorId;
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