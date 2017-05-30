/**
 * 
 */
package util.perti;

import java.io.Serializable;

import jpa.TbDeviceCustomization;
import jpa.TbInclusionDetail;

/**
 * @author tmolina
 *
 */
public class InclusionDetail implements Serializable {
	private static final long serialVersionUID = 5295405917919924180L;
	
	// Attributes ----- //
	
	private TbDeviceCustomization customization;
	
	private String reg;
	
	private String station;
	
	private boolean selected;
	
	private TbInclusionDetail detail;
	
	// Constructor ---- //

	/**
	 * Constructor.
	 */
	public InclusionDetail() {
	}
	
	/**
	 * @return the reg
	 */
	public String getReg() {
		return reg;
	}

	/**
	 * @param reg the reg to set
	 */
	public void setReg(String reg) {
		this.reg = reg;
	}

	/**
	 * @return the station
	 */
	public String getStation() {
		return station;
	}

	/**
	 * @param station the station to set
	 */
	public void setStation(String station) {
		this.station = station;
	}

	/**
	 * @param customization the customization to set
	 */
	public void setCustomization(TbDeviceCustomization customization) {
		this.customization = customization;
	}

	/**
	 * @return the customization
	 */
	public TbDeviceCustomization getCustomization() {
		return customization;
	}

	/**
	 * @param selected the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * @return the selected
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * @param detail the detail to set
	 */
	public void setDetail(TbInclusionDetail detail) {
		this.detail = detail;
	}

	/**
	 * @return the detail
	 */
	public TbInclusionDetail getDetail() {
		return detail;
	}
}