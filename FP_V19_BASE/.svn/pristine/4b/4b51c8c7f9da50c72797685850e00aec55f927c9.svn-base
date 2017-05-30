package ejb.crud;
import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import jpa.TbHolidays;

import util.AllInfoHoliday;


@Remote
public interface Holiday {
	
	public List<AllInfoHoliday> getInfoHolidasByFilters(String holidayName, String strDate);
	
	public List<AllInfoHoliday> getInfoHolidasByFiltersHolidayName(String holidayName);
	
	public List<AllInfoHoliday> getAllInfoHolidays();
	
	public List<TbHolidays> getHolidays();
	
	public String insertHoliday(Date holidayDate, String holidayName, String ip, Long creationUser);
	
	public boolean existHolidayU(Long idHoliday, Date holidayDate);
	
	public boolean existHoliday(Date holidayDate);
	
	public boolean getValidateYears(String date);
		
	public boolean removeHoliday(Long idHoliday, String ip, Long creationUser);

	public String editHoliday(Long idHoliday, Date holidayDate, String holidayName, 
							   String ip, Long userId);

}
