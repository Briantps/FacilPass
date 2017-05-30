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
 * @author tmolina
 *
 */
@Entity
@Table(name="TB_DEVICE_CUSTOMIZATION_STATE")
public class TbDeviceCustomizationState implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="DEVICE_CUSTOMIZATION_STATE_ID")
	private Long deviceCustomizationStateId;
	
	@Column(name="CUSTOMIZATION_STATE_NAME")
	private String customizationStateName;

	/**
	 * Constructor.
	 */
	public TbDeviceCustomizationState() {
		super();
	}

	/**
	 * @param deviceCustomizationStateId the deviceCustomizationStateId to set
	 */
	public void setDeviceCustomizationStateId(Long deviceCustomizationStateId) {
		this.deviceCustomizationStateId = deviceCustomizationStateId;
	}

	/**
	 * @return the deviceCustomizationStateId
	 */
	public Long getDeviceCustomizationStateId() {
		return deviceCustomizationStateId;
	}

	/**
	 * @param customizationStateName the customizationStateName to set
	 */
	public void setCustomizationStateName(String customizationStateName) {
		this.customizationStateName = customizationStateName;
	}

	/**
	 * @return the customizationStateName
	 */
	public String getCustomizationStateName() {
		return customizationStateName;
	}
}