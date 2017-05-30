package util;

import java.io.Serializable;
import java.util.Date;

public class AllInfoHoliday implements Serializable{
	private static final long serialVersionUID = -8394058584955856126L;
	
	private Long idHoliday;
	private Date holidayDate;
	private String description;

	 public AllInfoHoliday(){
	    	super();
	    }

	/**
	 * @param holidayDate the holidayDate to set
	 */
	public void setHolidayDate(Date holidayDate) {
		this.holidayDate = holidayDate;
	}

	/**
	 * @return the holidayDate
	 */
	public Date getHolidayDate() {
		return holidayDate;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param idHoliday the idHoliday to set
	 */
	public void setIdHoliday(Long idHoliday) {
		this.idHoliday = idHoliday;
	}

	/**
	 * @return the idHoliday
	 */
	public Long getIdHoliday() {
		return idHoliday;
	}
	 
	 
}
