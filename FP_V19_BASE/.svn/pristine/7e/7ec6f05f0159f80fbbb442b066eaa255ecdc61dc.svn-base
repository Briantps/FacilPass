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
 * The persistent class for the TB_INCLUSION_DETAIL database table.
 * @author tmolina
 */
 @Entity
 @Table(name="TB_INCLUSION_DETAIL")
public class TbInclusionDetail implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TB_INCLUSION_DETAIL_INCLUSIONDETAILID_GENERATOR")
	@SequenceGenerator(name="TB_INCLUSION_DETAIL_INCLUSIONDETAILID_GENERATOR",  sequenceName = "TB_INCLUSION_DETAIL_SEQ",allocationSize=1)
	@Column(name="INCLUSION_DETAIL_ID")
	private Long inclusionDetailId;
	
	@ManyToOne
	@JoinColumn(name="INCLUSION_ID")
	private TbInclusion tbInclusion;
	
	@ManyToOne
	@JoinColumn(name="DEVICE_CUSTOMIZATION_ID")
	private TbDeviceCustomization tbDeviceCustomization;
	
	@Column(name="DEVICE_FACIAL_ID")
	private String deviceFacialId;
	
	/**
	 * Constructor.
	 */
	public TbInclusionDetail(){
		super();
	}

	/**
	 * @param inclusionDetailId the inclusionDetailId to set
	 */
	public void setInclusionDetailId(Long inclusionDetailId) {
		this.inclusionDetailId = inclusionDetailId;
	}

	/**
	 * @return the inclusionDetailId
	 */
	public Long getInclusionDetailId() {
		return inclusionDetailId;
	}

	/**
	 * @param tbInclusion the tbInclusion to set
	 */
	public void setTbInclusion(TbInclusion tbInclusion) {
		this.tbInclusion = tbInclusion;
	}

	/**
	 * @return the tbInclusion
	 */
	public TbInclusion getTbInclusion() {
		return tbInclusion;
	}

	/**
	 * @param tbDeviceCustomization the tbDeviceCustomization to set
	 */
	public void setTbDeviceCustomization(TbDeviceCustomization tbDeviceCustomization) {
		this.tbDeviceCustomization = tbDeviceCustomization;
	}

	/**
	 * @return the tbDeviceCustomization
	 */
	public TbDeviceCustomization getTbDeviceCustomization() {
		return tbDeviceCustomization;
	}

	/**
	 * @param deviceFacialId the deviceFacialId to set
	 */
	public void setDeviceFacialId(String deviceFacialId) {
		this.deviceFacialId = deviceFacialId;
	}

	/**
	 * @return the deviceFacialId
	 */
	public String getDeviceFacialId() {
		return deviceFacialId;
	}
}