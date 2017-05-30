/**
 * 
 */
package util;

import java.io.Serializable;

/**
 * @author tmolina
 *
 */
public class Cities implements Serializable{
	private static final long serialVersionUID = -5876084243674844973L;
	
	private String department;
	
	private String city;
	
	private String cityCode;

	/**
	 * @param department the department to set
	 */
	public void setDepartment(String department) {
		this.department = department;
	}

	/**
	 * @return the department
	 */
	public String getDepartment() {
		return department;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param cityCode the cityCode to set
	 */
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	/**
	 * @return the cityCode
	 */
	public String getCityCode() {
		return cityCode;
	}
}