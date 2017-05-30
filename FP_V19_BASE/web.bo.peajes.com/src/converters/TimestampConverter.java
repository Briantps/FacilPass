package converters;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import java.sql.Timestamp;

/**
 * Converts a SQL timestamp object to time string and viceversa.
 * 
 * Recommended to be used on richfaces calendar.
 * @author Mauricio Gil
 */
public class TimestampConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		try {
			DateFormat formatter = new SimpleDateFormat("dd/MM/yy");
			Date date = (Date)formatter.parse(value);
			return new Timestamp(date.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if(value == null || !(value instanceof Timestamp)){
			return null;
		}
		
		Timestamp t = (Timestamp)value;
		return new SimpleDateFormat("dd/MM/yy").format(t);
	}

}
