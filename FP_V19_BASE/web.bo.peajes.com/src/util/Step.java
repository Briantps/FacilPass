/**
 * 
 */
package util;

import java.io.Serializable;

/**
 * @author Frances Zucchet
 * 
 */
public class Step implements Serializable {
	private static final long serialVersionUID = -639422274746391891L;
	
	private int priority;
	
	private int adsTime;
	
	private String navigateTo;
	
	private String name;
	
	/**
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}

	public boolean equals(Object obj){
		if(obj instanceof Step){
			Step other = (Step)obj;
			return this.priority == other.priority &&
					this.adsTime == other.adsTime &&
					this.navigateTo.equalsIgnoreCase(other.navigateTo) &&
					this.name.equalsIgnoreCase(other.name);
		}
		return false;
	}
	
	/**
	 * @param priority
	 *            the priority to set
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * @return the adsTime
	 */
	public int getAdsTime() {
		return adsTime;
	}

	/**
	 * @param adsTime
	 *            the adsTime to set
	 */
	public void setAdsTime(int adsTime) {
		this.adsTime = adsTime;
	}

	public void setNavigateTo(String navigateTo) {
		this.navigateTo = navigateTo;
	}

	public String getNavigateTo() {
		return navigateTo;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}