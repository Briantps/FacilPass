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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The persistent class for the TB_SPECIAL_EXEMPT_TYPE database table.
 * @author tmolina
 *
 */
@Entity
@Table(name="TB_SPECIAL_EXEMPT_TYPE")
public class TbSpecialExemptType implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="SPECIAL_EXEMPT_TYPE_ID_GENERATOR",sequenceName="TB_SPECIAL_EXEMPT_TYPE_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator ="SPECIAL_EXEMPT_TYPE_ID_GENERATOR")
	@Column(name="SPECIAL_EXEMPT_TYPE_ID")
	private Long specialExemptTypeId;
	
	@Column(name="SPECIAL_EXEMPT_TYPE_NAME")
	private String specialExemptTypeName;
	
	@ManyToOne
	@JoinColumn(name="DEVICE_TYPE_ID")
	private TbDeviceType tbDeviceType;

	/**
	 * Constructor.
	 */
	public TbSpecialExemptType() {
		super();
	}

	/**
	 * @param specialExemptTypeId the specialExemptTypeId to set
	 */
	public void setSpecialExemptTypeId(Long specialExemptTypeId) {
		this.specialExemptTypeId = specialExemptTypeId;
	}

	/**
	 * @return the specialExemptTypeId
	 */
	public Long getSpecialExemptTypeId() {
		return specialExemptTypeId;
	}

	/**
	 * @param specialExemptTypeName the specialExemptTypeName to set
	 */
	public void setSpecialExemptTypeName(String specialExemptTypeName) {
		this.specialExemptTypeName = specialExemptTypeName;
	}

	/**
	 * @return the specialExemptTypeName
	 */
	public String getSpecialExemptTypeName() {
		return specialExemptTypeName;
	}

	/**
	 * @param tbDeviceType the tbDeviceType to set
	 */
	public void setTbDeviceType(TbDeviceType tbDeviceType) {
		this.tbDeviceType = tbDeviceType;
	}

	/**
	 * @return the tbDeviceType
	 */
	public TbDeviceType getTbDeviceType() {
		return tbDeviceType;
	}
}