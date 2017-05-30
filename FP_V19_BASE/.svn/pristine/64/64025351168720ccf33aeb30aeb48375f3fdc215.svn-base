package jpa;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="TB_HOLIDAYS")
public class TbHolidays implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "TB_HOLIDAYS_SEQ", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "TB_HOLIDAYS_SEQ", sequenceName = "TB_HOLIDAYS_SEQ",allocationSize=1)
	
	@Column(name="ID_HOLIDAY")
	private Long idHoliday;
	
	@Column(name="HOLIDAY_NAME")
	private String holidayName;
	
	@Column(name="HOLIDAY_DATE")
	private Date holidayDate;
	
	@Column(name="HOLIDAY_STATE")
	private Integer holidayState;
	
	
	public TbHolidays(){
		super();
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

	/**
	 * @param holidayName the holidayName to set
	 */
	public void setHolidayName(String holidayName) {
		this.holidayName = holidayName;
	}

	/**
	 * @return the holidayName
	 */
	public String getHolidayName() {
		return holidayName;
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
	 * @param holidayState the holidayState to set
	 */
	public void setHolidayState(Integer holidayState) {
		this.holidayState = holidayState;
	}

	/**
	 * @return the holidayState
	 */
	public Integer getHolidayState() {
		return holidayState;
	}

}
